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
      :right
      :left)

    (= asteroid-x base-x)
    (if (< base-y asteroid-y)
      :up
      :down)

    :else [(/ (- asteroid-x base-x) (- asteroid-y base-y))
           (if (< asteroid-x base-x) :left-side :right-side)]))

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

(defn filter-keys
  [f m]
  (select-keys m (filter f (keys m))))

(defn vaporization-order
  [asteroids base]
  (let [directions (asteroid-direction asteroids base)
        groups-in-order (concat [(:up directions)]
                                (filter-keys #(and (pos? (first %)) (= :right (second %))) directions)
                                [(:right directions)]
                                (filter-keys #(and (neg? (first %)) (= :right (second %))) directions)
                                [(:down directions)]
                                (filter-keys #(and (pos? (first %)) (= :left (second %))) directions)
                                [(:left directions)]
                                (filter-keys #(and (neg? (first %)) (= :left (second %)))) directions)]
    (mapcat sort groups-in-order)))

(defn day10-2
  []
  (-> (io/resource "day10") (slurp)
      (vaporization-order :TODO-need-base)
      (drop 199)
      (first)))
