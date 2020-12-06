(ns y2020.day06
  (:require [y2020.util :as util]
            [clojure.set :as set]
            [clojure.string :as string]))

(defn parse
  [input]
  (map #(map set (string/split-lines %))
       (util/split-on-blanks input)))

(defn part1*
  [input]
  (->> input
       (map #(apply set/union %))
       (map count)
       (apply +)))

(def part1 (util/make-run-fn "day06" parse part1*))

(defn part2*
  [input]
  (->> input
       (map #(apply set/intersection %))
       (map count)
       (apply +)))

(def part2 (util/make-run-fn "day06" parse part2*))

(comment "run"
  (part1)
  (part2))
