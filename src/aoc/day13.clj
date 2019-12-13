(ns aoc.day13
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]))

(defn display
  [s]
  (reduce #(assoc [%1 %2] %3) (partition 3 s)))

(defn part1
  []
  (-> (util/read-input "day13" ic/parse)
      (ic/new-computer)
      (ic/exec-all)
      :output))

