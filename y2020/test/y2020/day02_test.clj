(ns y2020.day02-test
  (:require [y2020.day02 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(deftest correct-results
  (is (= 500 (d/part1 (util/input-lines "day02" d/parse-line))))
  (is (= 313 (d/part2 (util/input-lines "day02" d/parse-line)))))

(comment "run"
  (clojure.test/run-tests))
