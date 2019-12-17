(ns aoc.day11-test
  (:require [aoc.day11 :as d]
            [clojure.test :refer [deftest is]]))

(deftest move-robot-works
  (is (= {:orientation [-1 0] :position [-1 0]}
         (d/move-robot {:orientation [0 -1] :position [0 0]} 0)))
  (is (= {:orientation [1 0] :position [1 0]}
         (d/move-robot {:orientation [0 -1] :position [0 0]} 1))))

(deftest part1-result
  (is (= 1932 (d/day11-1))))

;; TODO: test part2
(comment
    (d/day11-2))

;; .####..##..#..#.#..#..##....##.####.###....
;; .#....#..#.#..#.#.#..#..#....#.#....#..#...
;; .###..#....####.##...#.......#.###..#..#...
;; .#....#.##.#..#.#.#..#.##....#.#....###....
;; .#....#..#.#..#.#.#..#..#.#..#.#....#.#....
;; .####..###.#..#.#..#..###..##..####.#..#...
