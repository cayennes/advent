(ns y2021.day02_test
  (:require [y2021.day02 :as d]
            [clojure.test :refer [deftest is]]))

(def ex
  "forward 5
   down 5
   forward 8
   up 3
   down 8
   forward 2")

(deftest parse-works
  (is (= [['forward 5]
          ['down 5]
          ['forward 8]
          ['up 3]
          ['down 8]
          ['forward 2]]
         (d/parse ex))))

(deftest part1-ex
  (is (= 150 (d/part1 ex))))

(deftest part1-answer
  (is (= 1561344) (d/part1)))

(deftest part2-ex
  (is (= 900 (d/part2 ex))))

(deftest part2-answer
  (is (= 1848454425 (d/part2))))
