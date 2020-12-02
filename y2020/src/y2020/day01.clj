(ns y2020.day01
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [criterium.core :as criterium]
            [y2020.util :as util]))

(defn parse
  [s]
  (vec (for [line (string/split s #"\n")] ;
         (edn/read-string line))))

(defn part1
  []
  (let [input (util/read-input "day01.txt" parse)
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
  (let [input (util/read-input "day01.txt" parse)
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
