(ns y2023.day01-test
  (:require [y2021.day01 :as d]
            [clojure.test :refer [deftest is]]))

(def ex
  "TODO: paste example code here")

(deftest part1-ex
  (is (= :TODO (d/part1 (d/parse ex)))))

(deftest part2-ex
  (is (= :TODO (d/part2 (d/parse ex)))))

(deftest correct-results
  (is (= :TODO (-> "day01" slurp-day parse part1)))
  (is (= :TODO (-> "day01" slurp-day parse part2))))
