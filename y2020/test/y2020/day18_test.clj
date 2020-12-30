(ns y2020.day18-test
  (:require [y2020.day18 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest testing is]]))

(deftest compute-step-works
  (is (= "2" (d/compute-step "1 + 1")))
  (is (= "6 * 4 * 30" (d/compute-step "2 * 3 * 4 * (5 * 6)"))))

(deftest basic-compute-works
  (is (= 51 (d/basic-compute "1 + (2 * 3) + (4 * (5 + 6))")))
  (is (= 26 (d/basic-compute "2 * 3 + (4 * 5)")))
  (is (= 437 (d/basic-compute "5 + (8 * 3 + 9 + 3 * 4 * 3)")))
  (is (= 12240 (d/basic-compute "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
  (is (= 13632 (d/basic-compute "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))))

(deftest part1
  (is (= 1451467526514 (d/part1))))

(deftest parts-work-to-solve-example
  (testing "first example"
    (is (= "(1 + (2 * 3) + (4 * 11))"
           (d/add-available "(1 + (2 * 3) + (4 * (5 + 6)))")))
    (is (= "(1 + 6 + 44)"
           (d/multiply-available "(1 + (2 * 3) + (4 * 11))")))
    (is (= "51" (d/add-available "(1 + 6 + 44)"))))
  (testing "third example"
    (is (= "5 + (8 * 15 * 4 * 3)"
           (d/add-available "5 + (8 * 3 + 9 + 3 * 4 * 3)")))
    (is (= "5 + 1440"
           (d/multiply-available "5 + (8 * 15 * 4 * 3)")))
    (is (= "1445"
           (-> "5 + 1440" d/add-available)))))

(deftest advanced-compute-step-works
  (testing "first example"
    (is (= "1 + 6 + 44"
           (d/advanced-compute-step "1 + (2 * 3) + (4 * (5 + 6))")))
    (is (= "51"
           (d/advanced-compute-step "(1 + 6 + 44)")))))

(deftest advanced-compute-works
  (is (= 51 (d/advanced-compute "1 + (2 * 3) + (4 * (5 + 6))")))
  (is (= 46 (d/advanced-compute "2 * 3 + (4 * 5)")))
  (is (= 1445 (d/advanced-compute "5 + (8 * 3 + 9 + 3 * 4 * 3)")))
  (is (= 669060
         (d/advanced-compute "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
  (is
   (= 23340
      (d/advanced-compute "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))))

(deftest part2
  (is (= 224973686321527 (d/part2))))

(comment "run tests"
  (clojure.test/run-tests))
