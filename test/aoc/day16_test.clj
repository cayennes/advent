(ns aoc.day16-test
  (:require [aoc.day16 :as d]
            [clojure.test :refer [deftest is]]))

(defn number->digits
  [number]
  (->> [nil number]
       (iterate (fn [[_digit remaining]]
                  [(mod remaining 10) (quot remaining 10)]))
       (rest)
       (take-while second)
       (map first)
       (reverse)))

(deftest phase-works
  (is (= (number->digits 48226158)
         (d/phase (number->digits 12345678)))))
