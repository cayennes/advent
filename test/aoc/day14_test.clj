(ns aoc.day14-test
  (:require [aoc.day14 :as d]
            [clojure.test :refer [deftest is]]))


(deftest parse-line-works
  (is (= [:A {:yield 10 :reagents {:ORE 10}}] (d/parse-line "10 ORE => 10 A"))))
