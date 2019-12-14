(ns aoc.day14-test
  (:require [aoc.day14 :as d]
            [clojure.test :refer [deftest is]]))


(deftest parse-line-works
  (is (= [:A {:yield 10 :ingredients {:ORE 10}}]
         (d/parse-line "10 ORE => 10 A"))))

(deftest parse-works
  (is (= {:A {:yield 10 :ingredients {:ORE 10}}
          :B {:yield 1 :ingredients {:ORE 1}}
          :C {:yield 1 :ingredients {:B 1 :A 7}}
          :D {:yield 1 :ingredients {:C 1 :A 7}}
          :E {:yield 1 :ingredients {:D 1 :A 7}}
          :FUEL {:yield 1 :ingredients {:E 1 :A 7}}}
         (d/parse "10 ORE => 10 A\n1 ORE => 1 B\n7 A, 1 B => 1 C\n7 A, 1 C => 1 D\n7 A, 1 D => 1 E\n7 A, 1 E => 1 FUEL"))))

(deftest decrease-in-works
  (is (= {:A 3 :B 2} (d/decrease-in {:A 7 :B 2} :A 4)))
  (is (= {:B 2} (d/decrease-in {:A 4 :B 2} :A 4))))

(deftest take-from-spares-works
  (is (= {:needed {:A 3} :spares {}}
         (d/take-from-spares {:needed {:A 5} :spares {:A 2}} :A))))

(deftest create-with-reaction-works
  (is (= {:needed {:A 2 :B 6} :spares {:A 3}}
         (d/create-with-reaction {:needed {:A 2} :spares {}}
                                 [:A {:yield 3 :ingredients {:B 6}}]))))
(deftest plan-all-works
  (is (= 10 (d/plan-all {:FUEL {:yield 1 :ingredients {:ORE 10}}}))))
