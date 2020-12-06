(ns y2020.day01
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [criterium.core :as criterium]
            [y2020.util :as util]))

(defn parse
  [input]
  (mapv edn/read-string (string/split-lines input)))

(defn part1*
  [input]
  (let [input-count (count input)]
    (first
     (for [i (range input-count)
           j (range i)
           :let [x (input i)
                 y (input j)]
           :when (= 2020 (+ x y))]
       (* x y)))))

(def part1 (util/make-run-fn "day01" parse part1*))

(defn part2*
  [input]
  (let [input-count (count input)]
    (first
     (for [i (range input-count)
           j (range i)
           k (range j)
           :let [x (input i)
                 y (input j)
                 z (input k)]
           :when (= 2020 (+ x y z))]
       (* x y z)))))

(def part2 (util/make-run-fn "day01" parse part2*))

(comment "run"
  (part1)
  (part2))
