(ns day1.core
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn fuel-required
  [mass]
  (- (quot mass 3)
     2))

(defn day1a
  []
  (->> (string/split (slurp (io/resource "day1")) #"\n")
       (map edn/read-string)
       (map fuel-required)
       (apply +)))

(defn total-fuel-required
  [mass]
  (->> (iterate #(max 0 (fuel-required %)) mass)
       (rest)
       (take-while pos?)
       (apply +)))

(defn day1b
  []
  (->> (string/split (slurp (io/resource "day1")) #"\n")
       (map edn/read-string)
       (map total-fuel-required)
       (apply +)))

