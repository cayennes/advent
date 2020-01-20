(ns aoc.day21-test
  (:require [aoc.day21 :as d]
            [clojure.test :refer [deftest is]]))

(deftest part1-result
  (is (= 19350258 (d/part1))))

(deftest part2-result
  (is (= 1142627861 (d/part2))))
