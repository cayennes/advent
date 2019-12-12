(ns aoc.day12
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn parse
  [input]
  (->> input
       (re-seq #"x=([-1-9]*).*?y=([-1-9]*).*?z=([-1-9]*)")
       (map rest)
       (map #(mapv edn/read-string %))
       (map (fn [p] {:position p :velocity [0 0 0]}))))

(defn sign
  [number]
  (cond
    (pos? number) 1
    (neg? number) -1
    :else 0))

(defn update-by
  [this-object other-object]
  (let [new-velocity (mapv (fn [tv tp op] (+ tv (sign (- op tp))))
                           (:velocity this-object)
                           (:position this-object)
                           (:position other-object))]
    {:velocity new-velocity
     :position (mapv + (:position this-object) new-velocity)}))

(defn step
  [objects]
  (map (fn [object] (reduce update-by object (filter #(not= object %) objects)))
       objects))

(defn steps
  [objects n]
  (nth (iterate step objects) n))

(defn part1
  []
  (-> (io/resource "day12") (slurp) (parse)
      (steps 1000)))
