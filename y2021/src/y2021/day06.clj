(ns y2021.day06
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))

(defn parse
  [input]
  (frequencies (edn/read-string (str "(" input ")"))))

(defn generation
  [fish]
  (apply merge-with +
         {8 (fish 0 0)}
         (for [[k v] fish]
           {(if (zero? k) 6 (dec k)) v})))

(defn fish-count-after
  [fish n]
  (-> (iterate generation fish)
      (nth n)
      (vals)
      (->> (apply +))))

(defn part1*
  [fish]
  (fish-count-after fish 80))

(defn part1
  ([input] (-> input parse part1*))
  ([] (-> "day06" io/resource slurp part1)))

(defn part2*
  [fish]
  (fish-count-after fish 256))

(defn part2
  ([input] (-> input parse part2*))
  ([] (-> "day06" io/resource slurp part2)))
