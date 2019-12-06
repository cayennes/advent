(ns aoc.day6
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.test :refer [deftest is run-tests]]))

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

(run-tests)
