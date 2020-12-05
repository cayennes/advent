(ns aoc.day24-test
  (:require [aoc.day24 :as d]
            [aoc.util :as util]
            [clojure.test :refer [deftest is]]))

(def ex
  (-> "....#
       #..#.
       #..##
       ..#..
       #...."
      (util/despace)
      (d/parse)))

(deftest first-repeat-works
  (is (= 6 (d/first-repeat [9 0 6 4 3 8 6 7]))))

(deftest example-works
  (is (= 2129920
         (d/analyze ex))))
