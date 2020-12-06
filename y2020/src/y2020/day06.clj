(ns y2020.day06
  (:require [y2020.util :as util]
            [clojure.set :as set]
            [clojure.string :as string]))

(defn parse-group
  [group-input]
  (map set (string/split-lines group-input)))

(defn parse
  [input]
  (map parse-group (string/split input #"\n\n")))

(defn part1
  [input]
  (->> input
       (map #(apply set/union %))
       (map count)
       (apply +)))

(defn part2
  [input]
  (->> input
       (map #(apply set/intersection %))
       (map count)
       (apply +)))

(comment "run"
  (part1 (util/input "day06" parse))
  (part2 (util/input "day06" parse)))
