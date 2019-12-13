(ns aoc.day13
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]))

(defn count-blocks
  [s]
  (->> s
       (drop 2)
       (take-nth 3)
       (filter #(= 2 %))
       (count)))

(defn part1
  []
  (-> (util/read-input "day13" ic/parse)
      (ic/new-computer)
      (ic/exec-all)
      (:output)
      (count-blocks)))

;; made this before realizing that's not what it's asking
(defn display
  [s]
  (reduce (fn [screen [x y thing]] (assoc screen [x y] thing))
          (partition 3 s)))

