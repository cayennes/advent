(ns aoc.day12-test
  (:require [aoc.day12 :as d]
            [clojure.test :refer [deftest is]]))

(deftest parse-works
  (is (= [{:position [-7 -8 9], :velocity [0 0 0]}
          {:position [-12 0 -4], :velocity [0 0 0]}]
         (d/parse "<x=-7, y=-8, z=9>\n<x=-12, y=0, z=-4>"))))

(deftest step-works
  (is (= [{:position [2 -1 1] :velocity [3 -1 -1]}
          {:position [3 -7 -4] :velocity [1 3 3]}
          {:position [1 -7 5] :velocity [-3 1 -3]}
          {:position [2 2 0] :velocity [-1 -3 1]}]
         (d/step [{:position [-1 0 2], :velocity [0 0 0]}
                  {:position [2 -10 -7], :velocity [0 0 0]}
                  {:position [4 -8 8], :velocity [0 0 0]}
                  {:position [3 5 -1], :velocity [0 0 0]}]))))

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

(deftest energy-works
  (is (= 36 (d/energy {:position [2 1 -3], :velocity [-3 -2 1]})))
  (is (= 45 (d/energy {:position [1 8 0], :velocity [-1 1 3]})))
  (is (= 80 (d/energy {:position [3 -6 1], :velocity [3 2 -3]})))
  (is (= 18 (d/energy {:position [2 0 4], :velocity [1 -1 -1]}))))

(deftest part1-result
  (is (= 12773 (d/part1))))

(deftest minimum-loop-length-works
  (is (= 3 (d/minimum-loop-length [4 9 2 4 6]))
      (= 2 (d/minimum-loop-length [5 4 0 2 0 2]))))

(deftest extract-coord-works
  (d/extract-coord [{:position [2 -1 1] :velocity [3 -1 -1]}
                    {:position [3 -7 -4] :velocity [1 3 3]}
                    {:position [1 -7 5] :velocity [-3 1 -3]}
                    {:position [2 2 0] :velocity [-1 -3 1]}]
                   2))

(deftest total-loop-length-works
  (is (= 2772
         (-> [{:position [-1 0 2], :velocity [0 0 0]}
              {:position [2 -10 -7], :velocity [0 0 0]}
              {:position [4 -8 8], :velocity [0 0 0]}
              {:position [3 5 -1], :velocity [0 0 0]}]
             (d/all-steps)
             (d/total-loop-length)))))

(deftest part2-result
  (is (= 306798770391636 (part2))))
