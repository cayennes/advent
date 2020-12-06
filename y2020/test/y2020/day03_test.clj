(ns y2020.day03-test
  (:require [y2020.day03 :as d]
            [y2020.util :as util]
            [clojure.string :as string]
            [clojure.test :refer [deftest is]]))

(def example
  "..##.......
   #...#...#..
   .#....#..#.
   ..#.#...#.#
   .#...##..#.
   ..#.##.....
   .#.#.#....#
   .#........#
   #.##...#...
   #...##....#
   .#..#...#.#")

(def example-lines (-> example util/read string/split-lines))

(deftest map-coord-works
  (is (= \. (d/map-coord example-lines [0 0])))
  (is (= \# (d/map-coord example-lines [0 1]))))

(deftest correct-results
  (is (= 252 (d/part1)))
  (is (= 2608962048 (d/part2))))

(comment "run"
  (clojure.test/run-tests))
