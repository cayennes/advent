(ns y2020.day03-test
  (:require [y2020.day03 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(def example
  (util/string-lines
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
    .#..#...#.#"
   vec))

(deftest map-coord-works
  (is (= \. (d/map-coord example [0 0])))
  (is (= \# (d/map-coord example [0 1]))))

(deftest correct-results
  (is (= 252 (d/part1 (util/input-lines "day03" vec))))
  (is (= 2608962048 (d/part2 (util/input-lines "day03" vec)))))

(comment "run"
  (clojure.test/run-tests))
