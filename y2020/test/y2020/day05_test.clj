(ns y2020.day05-test
  (:require [y2020.day05 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(deftest correct-results
  (is (= 842 (d/part1 (util/input-lines "day05" vec))))
  (is (= 617 (d/part2 (util/input-lines "day05" vec)))))

(comment "run"
  (clojure.test/run-tests))
