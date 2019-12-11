(ns aoc.day11
  (:require [aoc.intcode-computer :as ic]
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
  [program]
  (->> {:hull {}
             :robot {:position [0 0]
                     :orientation [-1 0]}
        :computer (ic/new-computer program)}
       (iterate do-stuff)
       (drop-while #(not (get-in % [:computer :halt])))
       (first)))

(defn day11-1
  []
  (-> (io/resource "day11") (slurp) (ic/parse)
      (do-everything)
      :hull
      (count)))
