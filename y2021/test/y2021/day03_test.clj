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
  (is (= [1 0 1] (d/zip-most-common [[1 0 1] [1 1 1] [0 0 0] [1 0 0]])))
  (is (= [1 0 1 1 0] (d/zip-most-common ex-parsed))))

(deftest binary-digits->int-works
  (is (= 23 (d/binary-digits->int [1 0 1 1 1]))
      (= 10 (d/binary-digits->int [0 1 0 1 0]))))

(deftest part1-ex
  (is (= 198 (d/part1 ex))))

(deftest part1-answer
  (is (= 2640986 (d/part1))))

(deftest filter-digit-works
  (is (= [[1 0 1 1 1]] (d/filter-digit [[1 0 1 1 0] [1 0 1 1 1]] 4 1)))
  (is (= [[0 1 0] [0 1 1] [1 1 0] [1 1 1]]
         (d/filter-digit (for [x (range 2), y (range 2), z (range 2)]
                           [x y z])
                         1
                         1))))

(def expected-oxygen
  [[[1 1 1 1 0] [1 0 1 1 0] [1 0 1 1 1] [1 0 1 0 1] [1 1 1 0 0] [1 0 0 0 0] [1 1 0 0 1]]
   [[1 0 1 1 0] [1 0 1 1 1] [1 0 1 0 1] [1 0 0 0 0]]
   [[1 0 1 1 0] [1 0 1 1 1] [1 0 1 0 1]]
   [[1 0 1 1 0] [1 0 1 1 1]]
   [[1 0 1 1 1]]])

(def expected-co2
  [[[0 0 1 0 0] [0 1 1 1 1] [0 0 1 1 1] [0 0 0 1 0] [0 1 0 1 0]]
   [[0 1 1 1 1] [0 1 0 1 0]]
   [[0 1 0 1 0]]
   [[0 1 0 1 0]]
   [[0 1 0 1 0]]])

(deftest filter-step-works
  (is (= {:possibles (expected-oxygen 0)
          :f d/most-common
          :i 1}
         (d/filter-step {:possibles ex-parsed
                         :f d/most-common})))
  (is (= (expected-oxygen 1)
         (:possibles (d/filter-step {:possibles (expected-oxygen 0)
                                     :f d/most-common
                                     :i 1}))))
  (is (= (expected-oxygen 2)
         (:possibles (d/filter-step {:possibles (expected-oxygen 1)
                                     :f d/most-common
                                     :i 2}))))
  (is (= (expected-oxygen 3)
         (:possibles (d/filter-step {:possibles (expected-oxygen 2)
                                     :f d/most-common
                                     :i 3}))))
  (is (= (expected-oxygen 4)
         (:possibles (d/filter-step {:possibles (expected-oxygen 3)
                                     :f d/most-common
                                     :i 4}))))
  (is (= expected-oxygen
         (->> {:possibles ex-parsed :f d/most-common}
              (iterate d/filter-step)
              (rest)
              (take 5)
              (map :possibles))))
  (is (= expected-co2
         (->> {:possibles ex-parsed :f d/least-common}
              (iterate d/filter-step)
              (rest)
              (take 5)
              (map :possibles)))))

(deftest filter-all-works
  (is (= 23 (d/filter-all ex-parsed d/most-common)))
  (is (= 10 (d/filter-all ex-parsed d/least-common))))

(deftest part2-ex
  (is (= 230 (d/part2 ex))))

(deftest part2-answer
  (is (= 6822109 (d/part2))))
