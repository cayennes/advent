(ns aoc.day19-test
  (:require [aoc.day19 :as d]
            [clojure.test :refer [deftest is]]))

(deftest part1-result
  (is (= 197 (d/part1))))

(deftest part2-result
  (is (= 9181022 (d/part2))))
