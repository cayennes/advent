(ns y2020.day04
  (:require [y2020.util :as util]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as string]))

(defn parse
  [input]
  (map #(apply array-map (string/split % #"[: \n]"))
       (util/split-on-blanks input)))

(def required-fields #{"byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid"})

(defn has-required-fields?
  [passport]
  (set/subset? required-fields (set (keys passport))))

(defn part1*
  [input]
  (util/count-satisfying input has-required-fields?))

(def part1 (util/make-run-fn "day04" parse part1*))

(defn has-valid-data?
  [{:strs [byr iyr eyr hgt hcl ecl pid] :as passport}]
  (and
   (has-required-fields? passport)
   ;; byr (Birth Year) - four digits; at least 1920 and at most 2002.
   (<= 1920 (edn/read-string byr) 2002)
   ;; iyr (Issue Year) - four digits; at least 2010 and at most 2020.
   (<= 2010 (edn/read-string iyr) 2020)
   ;; eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
   (<= 2020 (edn/read-string eyr) 2030)
   ;; hgt (Height) - a number followed by either cm or in:
   ;;   If cm, the number must be at least 150 and at most 193.
   ;;   If in, the number must be at least 59 and at most 76.
   (let [[_ height-str unit] (re-matches #"(\d+)(in|cm)" hgt)
         height (edn/read-string height-str)]
     (case unit
       "cm" (<= 150 height 193)
       "in" (<= 59 height 76)
       false))
   ;; hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
   (re-matches #"#[0-9a-f]{6}" hcl)
   ;; ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
   (#{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} ecl)
   ;; pid (Passport ID) - a nine-digit number, including leading zeroes.
   (re-matches #"[0-9]{9}" pid)))

(defn part2*
  [input]
  (util/count-satisfying input has-valid-data?))

(def part2 (util/make-run-fn "day04" parse part2*))

(comment "run"
  (part1)
  (part2))
