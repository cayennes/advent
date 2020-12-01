(ns y2020.day01
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [y2020.util :as util]))

(defn parse
  [s]
  (for [line (string/split s #"\n")]
    (edn/read-string line)))

(defn part1
  []
  (let [input (util/read-input "day01.txt" parse)]
    (first
     (for [x input
           y input
           :when (= 2020 (+ x y))]
       (* x y)))))

(defn part2
  []
  (let [input (util/read-input "day01.txt" parse)]
    (first
     (for [x input
           y input
           z input
           :when (= 2020 (+ x y z))]
       (* x y z)))))
