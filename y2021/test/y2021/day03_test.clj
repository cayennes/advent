(ns y2021.day03_test
  (:require [y2021.day03 :as d]
            [clojure.test :refer [deftest is]]))

(def ex
  "00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010")

(def ex-parsed
  (d/parse ex))

(deftest parse-works
  (is (= [[1 0] [0 0]] (d/parse "10\n00"))))

(deftest zip-most-common-works
  (is (= [1 0] (d/zip-most-common [[1 0] [1 1] [0 0]])))
  (is (= [1 0 1 1 0] (d/zip-most-common ex-parsed))))

(deftest part1*-works
  (is (= 198 (d/part1* ex-parsed))))

(deftest part1-answer
  (is (= 2640986 (d/part1))))
