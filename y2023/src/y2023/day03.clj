(ns y2023.day03
  (:require [clojure.string :as string]
            [clojure.set :as set]
            [y2023.util :as util]))

(def ex
  "467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..")

(def small-ex
  ".12
.@.")

(defn parse
  [input]
  (mapv vec (string/split input #"\n")))


(defn with-adjacent-characters
  [schematic]
  (for [row (range (count schematic))]
    (for [column (range (count (first schematic)))]
      [(-> schematic (get row) (get column))
       (into #{}
             (for [adjacent-row [(dec row) row (inc row)]
                   adjacent-col [(dec column) column (inc column)]]
               (-> schematic (get adjacent-row) (get adjacent-col))))])))

(def number-chars
  #{\1 \2 \3 \4 \5 \6 \7 \8 \9 \0})

(defn number-char?
  [c]
  (-> c
      (number-chars)
      (boolean)))

(defn part-char?
  [c]
  (and c
       (not ((conj number-chars \.) c))))

(defn part-in-adjacent?
  [segment]
  (->> segment
       (map second)
       (apply set/union)
       (some part-char?)))

(defn part-number
  [segment]
  (->> segment
       (map first)
       (apply str)
       (Integer/parseInt)))

(defn part1
  [schematic]
  (apply + (for [row (with-adjacent-characters schematic)]
             (->> row
                  (partition-by #(number-char? (first %)))
                  (filter #(number-char? (first (first %))))
                  (filter part-in-adjacent?)
                  (map part-number)
                  (apply +)))))


(comment "part 1"
  (part-in-adjacent? '([\1 #{nil \. \1}] [\1 #{nil \. \1 \4}] [\4 #{nil \. \1 \4}]))
  (-> (util/slurp-day "day03") (parse) (part1))
  532428)
