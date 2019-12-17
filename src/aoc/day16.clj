(ns aoc.day16
  (:require [aoc.util :as util]
            [clojure.edn :as edn]
            [clojure.math.numeric-tower :as nt]))

(defn parse
  [input]
  (->> input
       (map #(edn/read-string (str %)))
       (filter number?)))

(defn pattern
  [n]
  (->> [0 1 0 -1]
       (map #(repeat n %))
       (apply concat)
       (cycle)
       (rest)))

(defn phase-digit
  "calculates the 0-indexed nth digit of the next phase"
  [digits n]
  (->> digits
       (map * (pattern (inc n)))
       (apply +)
       (nt/abs)
       (#(mod % 10))))

(defn phase
  [digits]
  (map #(phase-digit digits %)
       (->> digits (count) (range))))

(defn part1
  []
  (-> (util/read-input "day16" parse)
      (->> (iterate phase))
      (nth 100)
      (->> (take 8)
           (apply str))))

(defn message-offset
  [digits]
  (->> digits (take 7) (util/digits->number)))

;; because the message is in the second half, digit d_i is equal to d_x + i
;; d_{x+1} + ith triangle number * d_{x+1} + ith pyramid number d_{x+2} + ...

(defn part2
  []
  #_(extract-message (util/read-input "day16" parse)))
