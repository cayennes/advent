(ns y2020.day01
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [criterium.core :as criterium]
            [y2020.util :as util]))

(defn part1
  []
  (let [input (util/input-lines "day01" edn/read-string)
        input-count (count input)]
    (first
     (for [i (range input-count)
           j (range i)
           :let [x (input i)
                 y (input j)]
           :when (= 2020 (+ x y))]
       (* x y)))))


(defn part2
  []
  (let [input (util/input-lines "day01" edn/read-string)
        input-count (count input)]
    (first
     (for [i (range input-count)
           j (range i)
           k (range j)
           :let [x (input i)
                 y (input j)
                 z (input k)]
           :when (= 2020 (+ x y z))]
       (* x y z)))))
