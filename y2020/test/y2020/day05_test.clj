(ns y2020.day05-test
  (:require [y2020.day05 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(deftest correct-results
  (is (= 842 (d/part1)))
  (is (= 617 (d/part2))))

(comment "run"
  (clojure.test/run-tests))
