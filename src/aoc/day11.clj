(ns aoc.day11
  (:require [aoc.intcode-computer :as ic]
            [clojure.java.io :as io]))

(defn rotate
  [[x y] rotation-code]
  (case rotation-code
    0 [y (- x)]
    1 [(- y) x]))

(defn read-color
  [hull position]
  (get hull position 0))

(defn last-two
  [s]
  (->> s reverse (take 2) reverse))

(defn do-stuff
  [{:keys [robot hull computer] :as world}]
  (let [current-color (read-color hull (:position robot))
        new-computer (-> computer (ic/add-input current-color) (ic/exec-all))
        [paint turn] (-> new-computer :output last-two)
        new-orientation (rotate (:position robot) turn)]
    (println robot new-orientation turn)
    (-> world
        (assoc :computer new-computer)
        (update-in [:hull (:position robot)] conj  paint)
        (assoc-in [:robot :orientation] new-orientation)
        (update-in [:robot :position] #(map + %1 %2) new-orientation))))

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
      (do-everything)))

(day11-1)
