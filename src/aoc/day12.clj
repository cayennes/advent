(ns aoc.day12
  (:require [aoc.util :as util]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn parse
  [input]
  (->> input
       (re-seq #"x=([-0-9]*).*?y=([-0-9]*).*?z=([-0-9]*)")
       (map rest)
       (map #(mapv edn/read-string %))
       (map (fn [p] {:position p :velocity [0 0 0]}))))

(defn sign
  [number]
  (cond
    (pos? number) 1
    (neg? number) -1
    :else 0))

(defn update-velocity-by
  [this-object other-object]
  (assoc this-object :velocity (mapv (fn [tv tp op] (+ tv (sign (- op tp))))
                                     (:velocity this-object)
                                     (:position this-object)
                                     (:position other-object))))

(defn update-velocity
  [object objects]
  (reduce update-velocity-by object (filter #(not= object %) objects)))

(defn update-position
  [{:keys [velocity] :as object}]
  (update object :position #(mapv + % velocity)))

(defn step
  [objects]
  (map #(-> % (update-velocity objects) (update-position))
       objects))

(defn steps
  [objects n]
  (nth (iterate step objects) n))

(defn energy
  [{:keys [position velocity]}]
  (* (apply + (map util/abs position)) ;; potential
     (apply + (map util/abs velocity)))) ;; kinetic

(defn part1
  []
  (-> (io/resource "day12") (slurp) (parse)
      (steps 1000)
      (->> (map energy)
           (apply +))))

