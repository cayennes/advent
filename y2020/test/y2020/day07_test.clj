(ns y2020.day07-test
  (:require [y2020.day07 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(def example
  (util/read
   "light red bags contain 1 bright white bag, 2 muted yellow bags.
    dark orange bags contain 3 bright white bags, 4 muted yellow bags.
    bright white bags contain 1 shiny gold bag.
    muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
    shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
    dark olive bags contain 3 faded blue bags, 4 dotted black bags.
    vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
    faded blue bags contain no other bags.
    dotted black bags contain no other bags."))

(deftest parse-works
  (is (= {"muted yellow" {"shiny gold" "2"
                          "faded blue" "9"}
          "light red" {"bright white" "1"
                       "muted yellow" "2"}
          "dotted black" {}
          "dark orange" {"bright white" "3"
                         "muted yellow" "4"}
          "bright white" {"shiny gold" "1"}
          "shiny gold" {"dark olive" "1"
                        "vibrant plum" "2"}
          "faded blue" {}
          "vibrant plum" {"faded blue" "5"
                          "dotted black" "6"}
          "dark olive" {"faded blue" "3"
                        "dotted black" "4"}}
         (d/parse example))))

(comment "run"
  (clojure.test/run-tests))
