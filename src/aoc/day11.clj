(ns aoc.day11
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]
            [clojure.java.io :as io]))

;;        [0 -1]
;; [-1 0] [0  0] [1 0]
;;        [0  1]

(defn rotate
  [[x y] rotation-code]
  (case rotation-code
    0 [y (- x)] ;; left
    1 [(- y) x])) ;; right

(defn move-robot
  [robot rotation-code]
  (let [new-orientation (rotate (:orientation robot) rotation-code)]
    (-> robot
        (assoc :orientation new-orientation)
        (update :position #(map + new-orientation %)))))

(defn last-two
  [s]
  (->> s reverse (take 2) reverse))

(defn do-stuff
  [{:keys [robot hull computer]}]
  (let [current-color (get hull (:position robot) 0)
        next-computer (-> computer (ic/add-input current-color) (ic/exec-all))
        [paint-color rotation-code] (-> next-computer :output last-two)]
    {:computer next-computer
     :hull (assoc hull (:position robot) paint-color)
     :robot (move-robot robot rotation-code)}))

(defn do-everything
  [program initial-hull]
  (->> {:hull initial-hull
        :robot {:position [0 0]
                :orientation [0 -1]}
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

(defn day11-2
  []
  (-> (io/resource "day11") (slurp) (ic/parse)
      (do-everything {[0 0] 1})
      (:hull)
      (util/show-location-map {0 "." 1 "#"})
      (println)))
