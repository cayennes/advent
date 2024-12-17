(ns y2023.day01
  (:require [y2023.util :as util]
            [clojure.string :as string]))

(defn find-first-and-last-digit
  [line]
  (let [digits (filter #{\0 \1 \2 \3 \4 \5 \6 \7 \8 \9} line)
        first-digit (first digits)
        last-digit (last digits)]
    [first-digit last-digit]))


(defn digits-to-number
  [digits]
  (Integer/parseInt (apply str digits)))

(defn part1
  [input]
  (->> (string/split input #"\n" )
       (map find-first-and-last-digit)
       (map digits-to-number)
       (apply +)))

(comment "part 1"
  (-> "day01" util/slurp-day part1))

(def numbers-by-name
  {"one" "1"
   "two" "2"
   "three" "3"
   "four" "4"
   "five" "5"
   "six" "6"
   "seven" "7"
   "eight" "8"
   "nine" "9"
   "zero" "0"})

(defn find-first-and-last-number
  [line]
  (let [first-number (second (re-find #"([0-9]|one|two|three|four|five|six|seven|eight|nine|zero)" line))
        last-number (second (re-find #".*([0-9]|one|two|three|four|five|six|seven|eight|nine|zero)" line))]
    [(get numbers-by-name first-number first-number)
     (get numbers-by-name last-number last-number)]))

(defn part2
  [input]
  (->> (string/split input #"\n")
       (map find-first-and-last-number)
       (map digits-to-number)
       (apply +)))

(comment "part 2"
  (-> "day01" util/slurp-day part2))
