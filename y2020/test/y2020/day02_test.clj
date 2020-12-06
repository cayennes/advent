(ns y2020.day02-test
  (:require [y2020.day02 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(deftest correct-results
  (is (= 500 (d/part1)))
  (is (= 313 (d/part2))))

(comment "run"
  (clojure.test/run-tests))
