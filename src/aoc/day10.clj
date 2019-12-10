(ns aoc.day10
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.test :refer [is deftest]]))

(defn list-asteroids
  {:test #(do (is (= #{[0 0] [1 1] [1 0]} (set (list-asteroids "##\n.#"))))
              (is (= #{[1 0] [4 0] [0 2] [1 2] [2 2] [3 2] [4 2] [4 3] [3 4] [4 4]}
                     (set (list-asteroids ".#..#\n.....\n#####\n....#\n...##")))))}
  [diagram-string]
  (filter some?
          (apply concat
                 (map-indexed
                  (fn [y row]
                    (map-indexed
                     (fn [x ch]
                       (if (= \# ch)
                         [x y]))
                     row))
                  (string/split diagram-string #"\n")))))

(defn angle
  [[base-x base-y] [asteroid-x asteroid-y]]
  (cond
    (= asteroid-y base-y)
    (if (< base-x asteroid-x)
      :pos-flat
      :neg-flat)

    (= asteroid-x base-x)
    (if (< base-y asteroid-y)
      :pos-vert
      :neg-vert)

    :else [(/ (- asteroid-x base-x) (- asteroid-y base-y))
           (< asteroid-x base-x)]))

(defn asteroid-direction
  [asteroids base]
  (->> asteroids
       (filter #(not= base %))
       (group-by #(angle base %))))

(defn find-best-location
  {:test #(is (= 8 (find-best-location [[1 0] [4 0] [0 2] [1 2] [2 2] [3 2] [4 2] [4 3] [3 4] [4 4]])))}
  [asteroids]
  (apply max (map #(count (asteroid-direction asteroids %))
                  asteroids)))

(deftest day10-1-examples
  (is (= 33
         (-> "......#.#.\n#..#.#....\n..#######.\n.#.#.###..\n.#..#.....\n..#....#.#\n#..#....#.\n.##.#..###\n##...#..#.\n.#....####"
             list-asteroids
             find-best-location)))

  (is (= 35
         (-> "#.#...#.#.\n.###....#.\n.#....#...\n##.#.#.#.#\n....#.#.#.\n.##..###.#\n..#...##..\n..##....##\n......#...\n.####.###."
             list-asteroids
             find-best-location)))
  (is (= 41
         (-> ".#..#..###\n####.###.#\n....###.#.\n..###.##.#\n##.##.#.#.\n....###..#\n..#.#..#.#\n#..#.#.###\n.##...##.#\n.....#.#.."
             list-asteroids
             find-best-location)))
  (is (= 210
         (-> ".#..##.###...#######\n##.############..##.\n.#.######.########.#\n.###.#######.####.#.\n#####.##.#.##.###.##\n..#####..#.#########\n####################\n#.####....###.#.#.##\n##.#################\n#####.##.###..####..\n..######..##.#######\n####.##.####...##..#\n.#####..#.######.###\n##...#.##########...\n#.##########.#######\n.####.#.###.###.#.##\n....##.##.###..#####\n.#.#.###########.###\n#.#.#.#####.####.###\n###.##.####.##.#..##"
            list-asteroids
            find-best-location))))

(defn day10-1
  {:test #(is (= 309 (day10-1)))}
  []
  (-> (io/resource "day10") (slurp)
      (list-asteroids)
      (find-best-location)))
