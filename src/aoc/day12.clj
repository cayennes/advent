(ns aoc.day12
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn parse
  [input]
  {:position (->> input
                  (re-seq #"x=([-1-9]*).*?y=([-1-9]*).*?z=([-1-9]*)")
                  (map rest)
                  (map #(map edn/read-string %)))
   :velocity 0})

(defn sign
  [number]
  (cond
    (pos? number) 1
    (neg? number) -1
    :else 0))

(defn update-by
  [this-object other-object]
  (let [new-velocity (map (fn [tv tp op] (+ tp (sign (- tp op))))
                          (:velocity this-object)
                          (:position this-object)
                          (:position other-object))]
    {:velocity new-velocity
     :position (map + (:position this-object) (:velocity this-object))}))

(defn step
  [objects]
  (for [this-object objects
        other-object objects
        :when (not= this-object other-object)]))

(defn part1
  []
  (-> (io/resource "day12") (slurp) (parse)))
