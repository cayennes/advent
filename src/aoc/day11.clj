(ns aoc.day11
  (:require [aoc.intcode-computer :as ic]
            [clojure.string :as string]
            [clojure.java.io :as io]))

(defn rotate
  [[x y] rotation-code]
  (case rotation-code
    0 [y (- x)]
    1 [(- y) x]))

(defn move-robot
  [robot rotation-code]
  (let [new-orientation (rotate (:orientation robot) rotation-code)]
    (-> robot
        (assoc :orientation new-orientation)
        (update :position #(map + % new-orientation)))))

(defn last-two
  [s]
  (->> s reverse (take 2) reverse))

(defn do-stuff
  [{:keys [robot hull computer]}]
  (let [current-color (first (get hull (:position robot) [0]))
        next-computer (-> computer (ic/add-input current-color) (ic/exec-all))
        [paint-color rotation-code] (-> next-computer :output last-two)]
    {:computer next-computer
     :hull (update hull (:position robot) conj paint-color)
     :robot (move-robot robot rotation-code)}))

(defn do-everything
  [program initial-hull]
  (->> {:hull initial-hull
        :robot {:position [0 0]
                :orientation [-1 0]}
        :computer (ic/new-computer program)}
       (iterate do-stuff)
       (drop-while #(not (get-in % [:computer :halt])))
       (first)))

(defn day11-1
  []
  (-> (io/resource "day11") (slurp) (ic/parse)
      (do-everything {})
      (:hull)
      (count)))

(defn show-hull
  [hull]
  (string/join
   "\n"
   (for [x (range (apply min (map first (keys hull)))
                  (inc (apply max (map first (keys hull)))))]
     (apply str (for [y (range (apply min (map second (keys hull)))
                               (inc (apply max (map second (keys hull)))))]
                  (case (first (get hull [x y] [0]))
                    0 "."
                    1 "#"))))))

(defn day11-2
  []
  (-> (io/resource "day11") (slurp) (ic/parse)
      (do-everything {[0 0] '(1)})
      (:hull)
      (show-hull)
      (println)))
;; turns out my robot is doing everything backwards. good enough.
