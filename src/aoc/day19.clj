(ns aoc.day19
  (:require [aoc.util :as util]
            [aoc.intcode-computer :as ic]))

(def coords (for [x (range 50) y (range 50)] [x y]))

(defn get-influence
  [coord]
  (-> (util/read-input "day19" ic/parse)
      (ic/new-computer)
      (ic/add-inputs coord)
      (ic/exec-all)
      (:output)
      (first)))

(defn part1
  []
  (apply + (for [x (range 50) y (range 50)] (get-influence [x y]))))
