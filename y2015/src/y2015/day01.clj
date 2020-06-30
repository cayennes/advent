(ns y2015.day01
  (:require [y2015.util :as util]))

(defn parse
  [s]
  (map {\( 1 \) -1} s))

(defn final-floor
  [motions]
  (apply + motions))

;; 138
(defn part1
  []
  (final-floor (util/read-input "day01" parse)))

(defn basement-index
  [motions]
  (->> motions
       (reductions + 0)
       (keep-indexed #(if (neg? %2) %1))
       (first)))

;; 1771
(defn part2
  []
  (basement-index (util/read-input "day01" parse)))
