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
  {:test #(is (= {:count 8, :location [3 4]}
                 (find-best-location [[1 0] [4 0] [0 2] [1 2] [2 2] [3 2] [4 2] [4 3] [3 4] [4 4]])))}
  [asteroids]
  (apply max-key :count
         (map #(array-map :count (count (asteroid-direction asteroids %))
                          :location %)
              asteroids)))

(deftest day10-1-examples
  (is (= {:count 33, :location [5 8]}
         (-> "......#.#.\n#..#.#....\n..#######.\n.#.#.###..\n.#..#.....\n..#....#.#\n#..#....#.\n.##.#..###\n##...#..#.\n.#....####"
             list-asteroids
             find-best-location)))

  (is (= {:count 35, :location [1 2]}
         (-> "#.#...#.#.\n.###....#.\n.#....#...\n##.#.#.#.#\n....#.#.#.\n.##..###.#\n..#...##..\n..##....##\n......#...\n.####.###."
             list-asteroids
             find-best-location)))
  (is (= {:count 41, :location [6 3]}
         (-> ".#..#..###\n####.###.#\n....###.#.\n..###.##.#\n##.##.#.#.\n....###..#\n..#.#..#.#\n#..#.#.###\n.##...##.#\n.....#.#.."
             list-asteroids
             find-best-location)))
  (is (= {:count 210, :location [11 13]}
         (-> ".#..##.###...#######\n##.############..##.\n.#.######.########.#\n.###.#######.####.#.\n#####.##.#.##.###.##\n..#####..#.#########\n####################\n#.####....###.#.#.##\n##.#################\n#####.##.###..####..\n..######..##.#######\n####.##.####...##..#\n.#####..#.######.###\n##...#.##########...\n#.##########.#######\n.####.#.###.###.#.##\n....##.##.###..#####\n.#.#.###########.###\n#.#.#.#####.####.###\n###.##.####.##.#..##"
            list-asteroids
            find-best-location))))

(defn day10-1
  {:test #(is (= {:count 309, :location [37 25]} (day10-1)))}
  []
  (-> (io/resource "day10") (slurp)
      (list-asteroids)
      (find-best-location)))

(defn ordered-asteroid-groups-in-quadrant
  {:test #(is (= [[[0 0]]]
                 (ordered-asteroid-groups-in-quadrant {[1 :left-side] [[0 0]]}
                                                      true false)))}
  [groups top? right?]
  (->> (select-keys groups
                    (filter #(and (vector? %)
                                  ((if top? neg? pos?) (first %))
                                  (= (if right? :right-side :left-side) (second %)))
                            (keys groups)))
       (sort-by #(* (if right? -1 1) (first (first %))))
       (map second)))

(defn vaporization-order
  [base asteroids]
  (let [directions (asteroid-direction asteroids base)
        groups-in-order (concat [(:up directions)]
                                (ordered-asteroid-groups-in-quadrant directions true true)
                                [(:right directions)]
                                (ordered-asteroid-groups-in-quadrant directions false true)
                                [(:down directions)]
                                (ordered-asteroid-groups-in-quadrant directions false false)
                                [(:left directions)]
                                (ordered-asteroid-groups-in-quadrant directions true false))]
    (mapcat sort groups-in-order)))

(deftest day10-2-examples
  (is (= :TODO
         (->> ".#....#####...#..\n##...##.#####..##\n##...#...#.#####.\n..#.....X...###..\n..#.#.....#....##"
             (list-asteroids)
             (vaporization-order [9 4])
             (take 9)))))

(defn day10-2
  []
  (->> (io/resource "day10") (slurp)
       (list-asteroids)
       (vaporization-order [37 25])
       (drop 199)
       (first)
       (#(+ (* 100 (first %)) (second %)))))
