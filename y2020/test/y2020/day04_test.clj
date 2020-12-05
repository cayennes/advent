(ns y2020.day04-test
  (:require [y2020.day04 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(def part1-example
  (-> "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
       byr:1937 iyr:2017 cid:147 hgt:183cm
       
       iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
       hcl:#cfa07d byr:1929
       
       hcl:#ae17e1 iyr:2013
       eyr:2024
       ecl:brn pid:760753108 byr:1931
       hgt:179cm
       
       hcl:#cfa07d eyr:2025 pid:166559648
       iyr:2011 ecl:brn hgt:59in"
      util/trim-lines
      d/parse))

(deftest correct-part1-example
  (is (= 2 (d/part1 example))))

(deftest correct-results
  (is (= 264 (d/part1 (util/input "day04" d/parse))))
  (is (= 224 (d/part2 (util/input "day04" d/parse)))))

(comment "run"
  (clojure.test/run-tests))
