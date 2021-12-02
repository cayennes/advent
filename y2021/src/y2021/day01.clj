(ns y2021.day01
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn parse
  [input]
  (map edn/read-string (string/split-lines input)))

(defn part1*
  [input]
  (->> input
       (partition 2 1)
       (filter (partial apply <))
       (count)))

(defn part1
  []
  (part1* (parse (slurp (io/resource "day01")))))

(defn part2*
  [input]
  (->> input
       (partition 3 1)
       (map (partial apply +))
       (part1*)))

(part2* (parse ex))

(defn part2
  []
  (part2* (parse (slurp (io/resource "day01")))))
