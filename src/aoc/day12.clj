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

(defn extract-coord
  [object coord]
  (mapv #(get % coord) (vals object)))

(defn loop-size-by-coordinate
  [object-seq]
  (loop [[object & more] object-seq
         seen [#{} #{} #{}]
         n 0
         loop-sizes [nil nil nil]]
    (if (every? some? loop-sizes)
      loop-sizes
      (let [seen? (-> (->> (range 3)
                           (filter #(nil? (loop-sizes %)))
                           (map #(vector % (extract-coord object %))))
                      (as-> [coord state] ((seen coord) state)))]
        (recur more
               (mapv #(update conj %1 (extract-coord %2)) seen (range 3))
               (inc n)
               (mapv #(if (or %1 (if (seen? %2) n)) loop-sizes (range))))))))

