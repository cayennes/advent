(ns aoc.day10
  (:require [aoc.util :as u]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.test :refer [is deftest]]))

(defn list-asteroids
  {:test #(do (is (= #{[0 0] [1 1] [1 0]} (set (list-asteroids "##\n.#"))))
              (is (= #{[1 0] [4 0] [0 2] [1 2] [2 2] [3 2] [4 2] [4 3] [3 4] [4 4]}
                     (set (list-asteroids ".#..#\n.....\n#####\n....#\n...##")))))}
  [diagram-string]
  (for [[y row] (map-indexed list (string/split diagram-string #"\n"))
        [x ch] (map-indexed list row)
        :when (= \# ch)]
      [x y]))

(defn angle
  {:test #(do (is (= :up (angle [4 4] [4 0])))
              (is (= :down (angle [4 4] [4 5])))
              (is (= :right (angle [4 4] [6 4])))
              (is (= :left (angle [4 4] [2 4])))
              (is (= [-1 :right-side] (angle [4 4] [5 3])))
              (is (= [1 :right-side] (angle [4 4] [5 5])))
              (is (= [-1 :left-side] (angle [4 4] [3 5])))
              (is (= [1 :left-side] (angle [4 4] [3 3]))))}
  [[base-x base-y] [asteroid-x asteroid-y]]
  (cond
    (= asteroid-y base-y)
    (if (< base-x asteroid-x)
      :right
      :left)

    (= asteroid-x base-x)
    (if (< asteroid-y base-y)
      :up
      :down)

    :else [(/ (- asteroid-y base-y) (- asteroid-x base-x))
           (if (< base-x asteroid-x) :right-side :left-side)]))

(defn by-direction
  [base asteroids]
  (->> asteroids
       (filter #(not= base %))
       (group-by #(angle base %))))

(defn find-best-location
  {:test #(is (= {:count 8, :location [3 4]}
                 (find-best-location [[1 0] [4 0] [0 2] [1 2] [2 2] [3 2] [4 2] [4 3] [3 4] [4 4]])))}
  [asteroids]
  (apply max-key :count
         (map #(array-map :count (count (by-direction % asteroids))
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
  {:test #(do (is (= [[[0 0]]]
                  (ordered-asteroid-groups-in-quadrant {[1 :left-side] [[0 0]]
                                                        [1 :right-side] [[2 2]]}
                                                       true false)))
              (is (= [[[2 2]]]
                     (ordered-asteroid-groups-in-quadrant {[1 :left-side] [[0 0]]
                                                           [1 :right-side] [[2 2]]}
                                                          false true))))}
  [groups top? right?]
  (->> (select-keys groups
                    (filter #(and (vector? %)
                                  ;; top right and bottom left are negative
                                  ((if (= top? right?) neg? pos?) (first %))
                                  (= (if right? :right-side :left-side) (second %)))
                            (keys groups)))
       ;; first is the key, first of that is the slope
       (sort-by #(first (first %)))
       (map second)))

(defn zipseqs
  {:test #(is (= [[0 0] [1 nil]]
                 (zipseqs (range 2) (range 1))))}
  [& seqs]
  (->> seqs
       (map #(concat % (repeat nil)))
       (apply map vector)
       (take-while #(some some? %))))

(defn distance-ish-from
  "comparable distance for asteroids at the same angle"
  {:test #(is (= 1 (distance-ish-from [0 0] [1 0])))}
  [[base-x base-y] [asteroid-x asteroid-y]]
  (if (= base-x asteroid-x)
    (u/abs (- base-y asteroid-y))
    (u/abs (- base-x asteroid-x))))

(defn vaporization-order
  [base asteroids]
  (let [directions (by-direction base asteroids)
        groups-in-order (concat [(:up directions)]
                                (ordered-asteroid-groups-in-quadrant directions true true)
                                [(:right directions)]
                                (ordered-asteroid-groups-in-quadrant directions false true)
                                [(:down directions)]
                                (ordered-asteroid-groups-in-quadrant directions false false)
                                [(:left directions)]
                                (ordered-asteroid-groups-in-quadrant directions true false))]
    (->> groups-in-order
         (map (partial sort-by (partial distance-ish-from base)))
         (apply zipseqs)
         (apply concat)
         (filter some?))))

(deftest day10-2-examples
  (is (= [[8 1] [9 0] [9 1] [10 0] [9 2] [11 1] [12 1] [11 2] [15 1]]
         (->> ".#....#####...#..\n##...##.#####..##\n##...#...#.#####.\n..#.....#...###..\n..#.#.....#....##"
              (list-asteroids)
              (vaporization-order [8 3])
              (take 9))))
  (is (= [4 4]
         (->> ".#....#####...#..\n##...##.#####..##\n##...#...#.#####.\n..#.....#...###..\n..#.#.....#....##"
              (list-asteroids)
              (vaporization-order [8 3])
              (drop 17)
              (first)))))

(defn day10-2
  {:test #(is (= 416 (day10-2)))}
  []
  (->> (io/resource "day10") (slurp)
       (list-asteroids)
       (vaporization-order [37 25])
       (drop 199)
       (first)
       (#(+ (* 100 (first %)) (second %)))))
