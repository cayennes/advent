(ns y2021.day01-test
  (:require [y2021.day01 :as d]
            [clojure.test :refer [deftest is]]))

(def ex
  "199
   200
   208
   210
   200
   207
   240
   269
   260
   263")

(deftest part1-ex
  (is (= 7 (d/part1* (d/parse ex)))))

(deftest part2-ex
  (is (= 5 (d/part2* (d/parse ex)))))

(deftest correct-results
  (is (= 1121 (d/part1)))
  (is (= 1065 (d/part2))))
