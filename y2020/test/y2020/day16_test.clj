(ns y2020.day16-test
  (:require [y2020.day16 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(def example
  (util/read
   "class: 1-3 or 5-7
    row: 6-11 or 33-44
    seat: 13-40 or 45-50
    
    your ticket:
    7,1,14
    
    nearby tickets:
    7,3,47
    40,4,50
    55,2,20
    38,6,12"))

(deftest parse-field-line-works
  (is (= ["class" #{1 2 3 5 6 7}]
         (d/parse-field-line "class: 1-3 or 5-7"))))

(deftest parse-works
  (is (= {:valid-values {"class" #{1 2 3 5 6 7}
                         "row" #{6 7 8 9 10 11 33 34 35 36 37 38 39 40 41 42 43 44}
                         "seat" #{13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 45 46 47 48 49 50}}
          :my-ticket [7 1 14]
          :nearby-tickets [[7 3 47] [40 4 50] [55 2 20] [38 6 12]]}
         (d/parse example))))

(deftest part1-example
  (is (= 71 (d/part1* (d/parse example)))))

(deftest part1
  (is (= 23122 (d/part1))))

(def example2
  (util/read "class: 0-1 or 4-19
              row: 0-5 or 8-19
              seat: 0-13 or 16-19
              
              your ticket:
              11,12,13
              
              nearby tickets:
              3,9,18
              15,1,5
              5,14,9"))

(deftest values-for-fields
  (is (= [[7 40 55 38] [3 4 2 6] [47 50 20 12]]
         (d/values-for-fields [[7 3 47] [40 4 50] [55 2 20] [38 6 12]]))))

(deftest find-valid-fields
  (is (= #{"class" "row" "seat"}
         (d/find-valid-fields [18 5 9]
                              {"class" #{0 7 1 4 15 13 6 17 12 19 11 9 5 14 16 10 18 8}
                               "row" #{0 1 4 15 13 17 3 12 2 19 11 9 5 14 16 10 18 8}
                               "seat" #{0 7 1 4 13 6 17 3 12 2 19 11 9 5 16 10 18 8}}))))

(deftest fields-in-order
  (is (= ["row" "class" "seat"]
         (d/fields-in-order (d/parse example2)))))

(deftest part2-example
  (is (= ["row" "class" "seat"]
         (d/part2* (d/parse example2)))))
