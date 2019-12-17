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
             d/string->position-map)))
  (is (= {[0 0] "#", [1 0] ".", [0 1] "#", [1 1] "#"}
         (-> "#.
              ##"
             strip-spaces
             d/string->position-map)))
  )

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

(deftest follow-path-works
  ;; ####
  ;; #  #F
  ;; S
  (is (= {:position [4 1], :orientation [1 0]}
         (d/follow-path {:position [0 2] :orientation [1 0]}
                        [[:left 2] [:right 3] [:right 1] [:left 1]]))))

(deftest all-segments-works
  ;; ####
  ;; #..##
  ;; >
  (is (= [[:left 2] [:right 3] [:right 1] [:left 1]]
         (d/all-segments {[0 0] "#" [1 0] "#" [1 1] "." [3 0] "#" [4 1] "#"
                          [0 2] ">" [2 0] "#" [3 1] "#" [2 1] "." [0 1] "#"}
                         {:position [0 2] :orientation [1 0]}))))

(deftest unspace-works
  (is (= "ABC\nDEF"
         (d/unspace "ABC
                     DEF"))))

(deftest part2-result
  (is (= 578918 (d/part2))))
