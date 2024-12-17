(ns y2023.day01-test
  (:require [y2023.day01 :as d]
            [clojure.test :refer [deftest is]]
            [y2023.util :as util]))

;; part 2

(def ex1
  "1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet")

(deftest find-first-and-last-digit-works
  (is (= [\1 \2] (d/find-first-and-last-digit "1abc2"))))

(deftest part1-ex
  (is (= 142 (d/part1 ex1))))

(deftest part1
  (is (= 54390 (d/part1 (util/slurp-day "day01")))))

;; part 2

(def ex2
  "two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen")

(deftest find-first-and-last-number-works
  (is (= ["2" "9"] (d/find-first-and-last-number "two1nine")))
  (is (= ["2" "4"] (d/find-first-and-last-number "xtwone3four")))
  (is (= ["7" "6"] (d/find-first-and-last-number "7pqrstsixteen")))
  (is (= ["2" "1"] (d/find-first-and-last-number "twone4twone"))))

(deftest part2-ex
  (is (= 281 (d/part2 ex2))))

(deftest part2
  (is (= 54277 (d/part2 (util/slurp-day "day01")))))
