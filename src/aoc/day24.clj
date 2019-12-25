(ns aoc.day24
  (:require [aoc.util :as util]
            [clojure.math.numeric-tower :as nt]
            [clojure.set :as set]))

(defn parse
  [s]
  (set (keys (util/string->position-map s #{\.}))))

(defn live?
  [bug-coords coord]
  (let [surrounding-coords (set (util/adjacent-positions coord))
        surround-count (count (set/intersection surrounding-coords bug-coords))]
    (if (bug-coords coord)
      (= 1 surround-count)
      (#{1 2} surround-count))))

(defn next-map
  [bug-coords]
  (set (for [x (range 5)
             y (range 5)
             :let [coord [x y]]
             :when (live? bug-coords coord)]
         coord)))

(defn first-repeat
  [things]
  (loop [[thing & more-things] things
         found-things #{}]
    (if (found-things thing)
      thing
      (recur more-things (conj found-things thing)))))

(defn biodiversity-value
  [[x y]]
  (nt/expt 2 (+ x (* 5 y))))

(defn biodiversity-rating
  [bug-coords]
  (apply + (map biodiversity-value bug-coords)))

(defn inspect-map
  [bug-coords]
  (doseq [y (range 5)]
    (doseq [x (range 5)]
      (print (if (bug-coords [x y]) "#" ".")))
    (println))
  bug-coords)

(defn analyze
  [bug-coords]
  (->> bug-coords
       (iterate next-map)
       (first-repeat)
       (inspect-map)
       (biodiversity-rating)))

(defn part1
  []
  (analyze (util/read-input "day24" parse)))

(defn plutonianize
  [bug-coords]
  (set (map #(conj % 0) bug-coords)))
