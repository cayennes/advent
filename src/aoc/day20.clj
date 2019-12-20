(ns aoc.day20
  (:require [aoc.util :as util]))

;; for each dot, find any labels

(defn maybe-label
  [raw-map coord]
  (first (for [orientation [[[0 1] [0 2]]
                            [[-2 0] [-1 0]]
                            [[0 -2] [0 -1]]
                            [[1 0] [2 0]]]
               :let [possible-coords (map #(util/vec+ % coord) orientation)
                     possible-label (apply str (map raw-map possible-coords))]
               :when (re-matches #"[A-Z]{2}" possible-label)]
           (keyword possible-label))))

(defn labeled-coords
  [raw-map]
  (for [[coord ch] raw-map
        :let [label (maybe-label raw-map coord)]
        :when (and label
                   (= "." ch))]
    [coord label]))

;; turn into a map of coord to adjacent coords

(defn portal-adjacency-map
  [raw-map]
  (->> raw-map
       (labeled-coords)
       (group-by second)
       (vals)
       (mapcat (fn [[[c1 _] [c2 _]]]
                 [[c1 #{c2}] [c2 #{c1}]]))
       (into {})))

(defn adjacency-map
  [raw-map]
  (->> raw-map
       (filter #(= "." (val %)))))

(defn adjacent-coord-map
  [raw-map]
  (merge-with set/union
              (portal-adjacency-map raw-map)
              (adjacency-map raw-map)))

;; search like in oxygen puzzle

(defn part1
  []
  (util/read-input "day20" #(util/string->position-map % #{\space \#})))
