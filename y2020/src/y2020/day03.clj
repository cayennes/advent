(ns y2020.day03
  (:require [y2020.util :as util]))

(defn map-coord
  [tree-map [x y]]
  (let [width (count (first tree-map))]
    (get-in tree-map [y (mod x width)])))

(defn collision-sequence
  [tree-map [x-step y-step]]
  (let [path (iterate (fn [[x y]] [(+ x-step x) (+ y-step y)]) [0 0])]
    (->> path
         (map #(map-coord tree-map %))
         (take-while some?))))

(defn count-collisions
  [tree-map angle]
  (-> tree-map
      (collision-sequence angle)
      (util/count-satisfying #(= \# %))))

(defn part1
  [tree-map]
  (count-collisions tree-map [3 1]))

(defn part2
  [tree-map]
  (* (count-collisions tree-map [1 1])
     (count-collisions tree-map [3 1])
     (count-collisions tree-map [5 1])
     (count-collisions tree-map [7 1])
     (count-collisions tree-map [1 2])))

(comment "run"
  (part1 (util/input-lines "day03" vec))
  (part2 (util/input-lines "day03" vec)))
