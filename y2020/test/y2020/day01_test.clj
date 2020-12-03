(ns y2020.day01-test
  (:require [y2020.day01 :as d]
            [clojure.test :refer [deftest is]]))

(deftest correct-results
  (is (= 840324 (d/part1)))
  (is (= 170098110 (d/part2))))

(comment "run"
  (clojure.test/run-tests))
