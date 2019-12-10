(ns aoc.day10
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.test :refer [is deftest]]))

(defn list-locations
  {:test #(do (is (= #{[0 0] [1 1] [1 0]} (set (list-locations "##\n.#"))))
              (is (= #{[1 0] [4 0] [0 2] [1 2] [2 2] [3 2] [4 2] [4 3] [3 4] [4 4]}
                     (set (list-locations ".#..#\n.....\n#####\n....#\n...##")))))}
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

(defn count-asteroid-angles
  [asteroids [base-x base-y]]
  (->> asteroids
       (filter #(not= [base-x base-y] %))
       (map (fn [[asteroid-x asteroid-y]]
              (cond
                (zero? (- asteroid-y base-y))
                (if (pos? (- asteroid-x base-x))
                  :pos-flat
                  :neg-flat)

                (zero? (- asteroid-x base-x))
                (if (pos? (- asteroid-y base-y))
                  :pos-vert
                  :neg-vert)

                :else (/ (- asteroid-x base-x)
                         (- asteroid-y base-y)))))
       (distinct)
       (count)))

(defn find-best-location
  {:test #(is (= 8 (find-best-location [[1 0] [4 0] [0 2] [1 2] [2 2] [3 2] [4 2] [4 3] [3 4] [4 4]])))}
  [asteroids]
  (apply max (map #(count-asteroid-angles asteroids %)
                  asteroids)))

(deftest day10-1-examples
  (is (= 33
         (-> "......#.#.\n#..#.#....\n..#######.\n.#.#.###..\n.#..#.....\n..#....#.#\n#..#....#.\n.##.#..###\n##...#..#.\n.#....####"
             list-locations
             find-best-location)))

  (is (= 35
         (-> "#.#...#.#.\n.###....#.\n.#....#...\n##.#.#.#.#\n....#.#.#.\n.##..###.#\n..#...##..\n..##....##\n......#...\n.####.###."
             list-locations
             find-best-location)))
  (is (= 41
         (-> ".#..#..###\n####.###.#\n....###.#.\n..###.##.#\n##.##.#.#.\n....###..#\n..#.#..#.#\n#..#.#.###\n.##...##.#\n.....#.#.."
             list-locations
             find-best-location)))
  (is (= 210
         (->".#..##.###...#######\n##.############..##.\n.#.######.########.#\n.###.#######.####.#.\n#####.##.#.##.###.##\n..#####..#.#########\n####################\n#.####....###.#.#.##\n##.#################\n#####.##.###..####..\n..######..##.#######\n####.##.####...##..#\n.#####..#.######.###\n##...#.##########...\n#.##########.#######\n.####.#.###.###.#.##\n....##.##.###..#####\n.#.#.###########.###\n#.#.#.#####.####.###\n###.##.####.##.#..##"
            list-locations
            find-best-location))))

(defn day10-1
  []
  (-> (io/resource "day10") (slurp)
      (list-locations)
      (find-best-location)))
