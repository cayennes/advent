(ns aoc.day18
  (:require [aoc.util :as util]))

;; see day 15 for some useful code

(defn part1
  []
  (util/read-input "day18" #(util/string->position-map % \#)))
