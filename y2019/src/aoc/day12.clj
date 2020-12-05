(ns aoc.day12
  (:require [aoc.util :as util]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.math.numeric-tower :as nt]))

(defn parse
  [input]
  (->> input
       (re-seq #"x=([-0-9]*).*?y=([-0-9]*).*?z=([-0-9]*)")
       (mapv rest)
       (mapv #(mapv edn/read-string %))
       (mapv (fn [p] {:position p :velocity [0 0 0]}))))

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
  (mapv #(-> % (update-velocity objects) (update-position))
        objects))

(defn all-steps
  [objects]
  (iterate step objects))

(defn steps
  [objects n]
  (nth (all-steps objects) n))

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

(defn minimum-loop-length
  [s]
  (loop [[x & more] s
         seen {}
         n 0]
    (cond
      (seen x) (- n (seen x))
      (empty? more) nil
      :else (recur more
                   (assoc seen x n)
                   (inc n)))))

(defn extract-coord
  [objects axis-index]
  (vals objects)
  (mapv (fn [o] (mapv #(get % axis-index) (vals o)))
        objects))

(defn total-loop-length
  [step-seq]
  (reduce nt/lcm
          (for [axis-index (range 3)]
            (minimum-loop-length
             (map #(extract-coord % axis-index)
                  step-seq)))))

(defn part2
  []
  (-> (io/resource "day12") (slurp) (parse)
      (all-steps)
      (total-loop-length)))
