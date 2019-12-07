(ns aoc.day6
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.test :refer [is run-tests]]))

(defn parse
  {:test #(is (= {:B :A :C :B}
                 (parse "A)B\nB)C")))}
  [input]
  (into {}
        (for [line (string/split input #"\n")
              :let [[orbitee orbiter] (string/split line #"\)")]]
          [(keyword orbiter) (keyword orbitee)])))

(defn path-to-center
  [orbit-map start]
  (->> (iterate orbit-map start)
       (take-while identity)))

(defn total-orbits
  {:test #(is (= 3 (total-orbits {:B :A :C :B})))}
  [orbit-map]
  (->> (for [k (keys orbit-map)]
         (->> (path-to-center orbit-map k)
              (count)
              (dec)))
       (apply +)))

(defn day6-1
  {:test #(is (= 110190 (day6-1)))}
  []
  (-> (io/resource "day6")
      (slurp)
      (parse)
      (total-orbits)))

(defn length-the-same [s1 s2]
  (->> (map vector s1 s2)
       (take-while #(apply = %))
       (count)))

(defn length-between
  {:test #(is (= 4
                 (length-between {:L :K, :SAN :I, :I :D, :F :E, :D :C, :B :COM, :J :E, :C :B, :E :D, :G :B, :H :G, :K :J, :YOU :K}
                                 :YOU :SAN)))}
  [orbit-map start end]
  (let [start-path (path-to-center orbit-map start)
        end-path (path-to-center orbit-map end)
        untraveled (length-the-same (reverse start-path)
                                    (reverse end-path))]
    (- (+ (count start-path)
          (count end-path))
       (* 2 untraveled)
       2)))

(defn day6-2
  {:test #(is (= 343 (day6-2)))}
  []
  (-> (io/resource "day6")
      (slurp)
      (parse)
      (length-between :YOU :SAN)))

(run-tests)
