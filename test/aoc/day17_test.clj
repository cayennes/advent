(ns aoc.day17-test
  (:require [aoc.day17 :as d]
            [clojure.string :as string]
            [clojure.test :refer [deftest is]]))

(defn strip-spaces
  [s]
  (string/replace s " " ""))

(deftest string->position-map-works
  (is (= {[0 0] "#", [1 0] ".", [0 1] "#", [1 1] "#"}
         (-> "#.
              ##"
             strip-spaces
             d/string->position-map))))

(deftest part1-result
  (is (= 4112 (d/part1))))

(deftest available-turn-works
  (is (= :right
         (d/available-turn {[0 0] "#", [1 0] ".", [0 1] "#", [1 1] "#"}
                           {:position [0 1] :direction [-1 0]}))))

(deftest count-scaffolding-ahead
  (is (= 1
         (d/count-scaffolding-ahead {[0 0] "#", [1 0] ".", [0 1] "#", [1 1] "#"}
                                    {:position [0 1] :direction [0 -1]}))))

(deftest next-path-segment-works
  (is (= [:right 1]
         (d/next-path-segment {[0 0] "#", [1 0] ".", [0 1] "#", [1 1] "#"}
                              {:position [0 1] :direction [-1 0]}))))
