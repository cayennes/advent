(ns y2020.day06-test
  (:require [y2020.day06 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(deftest correct-results
  (is (= 6590 (d/part1)))
  (is (= 3288 (d/part2))))

(comment "run"
  (clojure.test/run-tests))
