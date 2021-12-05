(ns y2021.day04_test
  (:require [y2021.day04 :as d]
            [clojure.test :refer [deftest is]]))

(def ex
  "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7")

(def ex-parsed (d/parse ex))

(deftest parse-works
  (is (= {:numbers [7 4 9 5 11 17 23 2 0 14 21 24 10 16 13 6 15 25 12 22 18 20 8 19 3 26 1]
          :boards [[22 13 17 11 0
                    8 2 23 4 24
                    21 9 14 16 7
                    6 10 3 18 5
                    1 12 20 15 19]
                   [3 15 0 2 22
                    9 18 13 17 5
                    19 8 7 25 23
                    20 11 10 24 4
                    14 21 16 12 6]
                   [14 21 17 24 4
                    10 16 15 9 19
                    18 8 23 26 20
                    22 11 13 6 5
                    2 0 12 3 7]]}
         ex-parsed)))

(deftest mark-num-works
  (is (= [22 13 17 11 0
          8 2 23 4 24
          21 9 14 16 nil
          6 10 3 18 5
          1 12 20 15 19]
         (d/mark-num [22 13 17 11 0
                      8 2 23 4 24
                      21 9 14 16 7
                      6 10 3 18 5
                      1 12 20 15 19]
                     7))))

(deftest win?-works
  (is (= true (d/win? [3 15  0  2 22
                       9 18 13 17  5
                       nil nil nil nil nil
                       19  8  7 25 23
                       20 11 10 24  4])))
  (is (= true (d/win? [22 13 17 11 nil
                       8 2 23 4 nil
                       21 9 14 16 nil
                       6 10 3 18 nil
                       1 12 20 15 nil])))
  (is (= false (d/win? [22 13 17 11 nil
                        8 2 23 nil nil
                        21 9 nil 16 7
                        6 nil 3 18 nil
                        nil 12 20 15 nil]))))

(deftest total-uncalled
  (is (= 188 (d/total-uncalled [nil nil nil nil nil
                                10 16 15  nil 19
                                18  8 nil 26 20
                                22 nil 13  6  nil
                                nil nil 12  3  nil]))))

(deftest part1-ex
  (is (= 4512 (d/part1 ex))))

(deftest part1-answer
  (is (= 34506 (d/part1))))

(deftest part2-ex
  (is (= 1924 (d/part2 ex))))

(deftest part2-answer
  (is (= 7686 (d/part2))))
