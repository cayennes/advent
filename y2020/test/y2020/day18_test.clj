(ns y2020.day18-test
  (:require [y2020.day18 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(deftest compute-step-works
  (is (= "2" (d/compute-step "1 + 1")))
  (is (= "6 * 4 * 30" (d/compute-step "2 * 3 * 4 * (5 * 6)"))))

(deftest compute-works
  (is (= 51 (d/compute "1 + (2 * 3) + (4 * (5 + 6))")))
  (is (= 26 (d/compute "2 * 3 + (4 * 5)")))
  (is (= 437 (d/compute "5 + (8 * 3 + 9 + 3 * 4 * 3)")))
  (is (= 12240 (d/compute "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
  (is (= 13632 (d/compute "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))))

(deftest part1
  (is (= 1451467526514 (part1))))
