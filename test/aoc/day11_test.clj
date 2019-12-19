(ns aoc.day11-test
  (:require [aoc.day11 :as d]
            [aoc.util :as util]
            [clojure.test :refer [deftest is]]))

(deftest move-robot-works
  (is (= {:orientation [-1 0] :position [-1 0]}
         (d/move-robot {:orientation [0 -1] :position [0 0]} 0)))
  (is (= {:orientation [1 0] :position [1 0]}
         (d/move-robot {:orientation [0 -1] :position [0 0]} 1))))

(deftest part1-result
  (is (= 1932 (d/part1))))

(deftest part2-result
  (is (= (util/despace ".####..##..#..#.#..#..##....##.####.###....
                        .#....#..#.#..#.#.#..#..#....#.#....#..#...
                        .###..#....####.##...#.......#.###..#..#...
                        .#....#.##.#..#.#.#..#.##....#.#....###....
                        .#....#..#.#..#.#.#..#..#.#..#.#....#.#....
                        .####..###.#..#.#..#..###..##..####.#..#...")
         (d/part2))))

