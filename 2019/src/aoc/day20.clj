(ns aoc.day20
  (:require [aoc.util :as util]
            [clojure.set :as set]))

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
       (filter #(not (#{:AA :ZZ} (second %))))
       (group-by second)
       (vals)
       (mapcat (fn [[[c1 _] [c2 _]]]
                 [[c1 #{c2}] [c2 #{c1}]]))
       (into {})))

(defn adjacency-map
  [raw-map]
  (apply
   merge-with
   set/union
   (for [[coord ch] raw-map
         adjacent-coord (util/adjacent-positions coord)
         :when (= "." ch (raw-map adjacent-coord))]
     {coord #{adjacent-coord}})))

(defn adjacent-coord-map
  [raw-map]
  (merge-with set/union
              (portal-adjacency-map raw-map)
              (adjacency-map raw-map)))

;; search like in oxygen puzzle

(defn find-label
  [raw-map label]
  (->> (labeled-coords raw-map)
       (filter #(= label (second %)))
       (first)
       (first)))

(defn search-step
  [{:keys [adjacency-map search-locations visited] :as acc}]
  (-> acc
      (assoc :search-locations
             (set (for [coord search-locations
                        next-coord (adjacency-map coord)
                        :when (not (visited next-coord))]
                    next-coord)))
      (update :visited #(set/union % search-locations))))

(defn solve-steps
  [raw-map]
  (second (let [start (find-label raw-map :AA)
                finish (find-label raw-map :ZZ)]
            (util/counting-iterate-until
             search-step
             #((:search-locations %) finish)
             {:adjacency-map (adjacent-coord-map raw-map)
              :search-locations #{start}
              :visited #{}}))))

(defn part1
  []
  (solve-steps
   (util/read-input "day20" #(util/string->position-map % #{\space \#}))))
