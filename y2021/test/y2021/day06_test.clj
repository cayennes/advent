(ns y2021.day06_test
  (:require [y2021.day06 :as d]
            [clojure.test :refer [deftest is]]))

(deftest generation-works
  (is (= {0 1, 1 1, 2 2, 3 1, 8 0, 6 0}
         (d/generation (frequencies [3,4,3,1,2]))))
  (is (= (frequencies [6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8])
         (d/generation (frequencies [0,1,0,5,6,0,1,2,2,3,0,1,2,2,2,3,3,4,4,5,7,8])))))

(deftest fish-count-after-works
  (is (= 26 (d/fish-count-after (frequencies [3,4,3,1,2]) 18))))

(deftest part1-ex
  (is (= 5934 (d/part1 "3,4,3,1,2"))))

(deftest part1-answer
  (is (= 374994 (d/part1))))

(deftest part2-ex
  (is (= 26984457539 (d/part2 "3,4,3,1,2"))))

(deftest part2
  (is (= 1686252324092 (d/part2))))
