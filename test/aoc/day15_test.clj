(ns aoc.day15-test
  (:require [aoc.day15 :as d]
            [clojure.test :refer [deftest is]]))

(deftest part1-result
  (is (= 228 (d/part1))))

(deftest fill-step-works
  (is (= {[0 0] :empty [0 1] :oxygen [0 2] :oxygen [0 3] :oxygen}
         (d/fill-step {[0 0] :empty [0 1] :empty [0 2] :oxygen [0 3] :empty}))))

(deftest part2-result
  (is (= 348 (d/part2))))
