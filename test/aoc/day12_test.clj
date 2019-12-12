(ns aoc.day12-test
  (:require [aoc.day12 :as d]
            [clojure.test :refer [deftest is]]))

(deftest parse-works
  (is (= [{:position [-7 -8 9], :velocity [0 0 0]}
          {:position [-12 -3 -4], :velocity [0 0 0]}]
         (d/parse "<x=-7, y=-8, z=9>\n<x=-12, y=-3, z=-4>"))))

(deftest update-by-works
  (is (= {:position [2 3 0] :velocity [1 2 -1]}
         (d/update-by {:position [1 1 1] :velocity [0 1 0]}
                      {:position [5 3 0] :velocity [1 2 3]}))))

(deftest step-works
  (is (= [{:position [2 2 2] :velocity [2 2 2]}
          {:position [2 2 2] :velocity [0 0 0]}]
         (d/step [{:position [0 0 0] :velocity [1 1 1]}
                  {:position [2 2 2] :velocity [1 1 1]}]))))

(deftest steps-works
  (is (= [{:position [2 2 2] :velocity [2 2 2]}
          {:position [2 2 2] :velocity [0 0 0]}]
         (d/steps [{:position [0 0 0] :velocity [1 1 1]}
                   {:position [2 2 2] :velocity [1 1 1]}]
                  1)))
  (is (= [{:position [4 4 4] :velocity [2 2 2]}
          {:position [2 2 2] :velocity [0 0 0]}]
         (d/steps [{:position [0 0 0] :velocity [1 1 1]}
                   {:position [2 2 2] :velocity [1 1 1]}]
                  2))))
