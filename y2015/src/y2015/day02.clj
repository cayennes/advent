(ns y2015.day02
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [y2015.util :as util]))

(defn parse
  [s]
  (for [line (string/split s #"\n")]
    (for [num (string/split line #"x")]
      (edn/read-string num))))

(defn paper-for
  [[l w h]]
  (let [[small medium large] (sort [(* l w) (* l h) (* w h)])]
    (+ (* 3 small)
       (* 2 medium)
       (* 2 large))))

(comment
    (= 58 (paper-for [2 3 4])))

(defn part1
  []
  (->> (util/read-input "day02" parse)
       (map paper-for)
       (apply +)))

(defn ribbon-for
  [[l w h]]
  (let [[small medium _large] (sort [l w h])]
    (+ (* 2 small)
       (* 2 medium)
       (* l w h))))

(defn part2
  []
  (->> (util/read-input "day02" parse)
       (map ribbon-for)
       (apply +)))
