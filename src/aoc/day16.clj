(ns aoc.day16
  (:require [aoc.util :as util]))

(defn number->digits
  [number]
  (->> number
       (iterate (fn [digit remaining]
                  [(mod remaining 10) (quot remaining 10)])))
  (rest)
  (map first))
