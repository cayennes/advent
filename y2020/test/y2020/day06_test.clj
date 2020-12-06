(ns y2020.day06-test
  (:require [y2020.day06 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(deftest correct-results
  (is (= 6590 (d/part1 (util/input "day06" d/parse))))
  (is (= 3288 (d/part2 (util/input "day06" d/parse)))))

(comment "run"
  (clojure.test/run-tests))
