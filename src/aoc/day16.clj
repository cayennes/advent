(ns aoc.day16
  (:require [aoc.util :as util]
            [clojure.edn :as edn]))

(defn parse
  [input]
  (map #(edn/read-string (str %)) input))

(defn pattern
  [n]
  (->> [0 1 0 -1]
       (map #(repeat n %))
       (apply concat)
       (rest)))

(defn phase
  [digits]
  (map #(->> digits
             (map * (pattern %))
             (apply +))
       (-> digits count range)))

(defn part1
  []
  (-> (util/read-input "day16" parse)
      (->> (iterate phase))
      (nth 100)))
