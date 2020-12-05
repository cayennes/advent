(ns aoc.day16-test
  (:require [aoc.day16 :as d]
            [clojure.test :refer [deftest is]]))

(deftest parse-works
  (is (= [4 8 2 2 6 1 5 8]
         (d/parse "48226158"))
      (= [1 2 4] (d/parse "123\n"))))

(deftest pattern-works
  (is (= [0 0 1 1 1 0 0 0 -1 -1 -1]
         (take 11 (d/pattern 3))))
  (is (= [1 0 -1 0 1 0 -1 0]
         (take 8 (d/pattern 1)))))

(deftest phase-digit-works
  (is (= 4 (d/phase-digit [1 2 3 4 5 6 7 8] 0))))

(deftest phase-works
  (is (= [4 8 2 2 6 1 5 8]
         (d/phase [1 2 3 4 5 6 7 8]))))

(deftest part1-result
  (is (= "68764632" (d/part1))))

(deftest message-offset-works
  (is (= 1234567 (d/message-offset [1 2 3 4 5 6 7 8 4 8 2 2 6 1 5 8]))))

(deftest extract-message-works
  (is (= "84462026"
         (d/extract-message 
          [0 3 0 3 6 7 3 2 5 7 7 2 1 2 9 4 4 0 6 3 4 9 1 5 6 5 4 7 4 6 6 4]))))
