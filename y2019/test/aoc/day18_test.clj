(ns aoc.day18-test
  (:require [aoc.day18 :as d]
            [aoc.util :as util]
            [clojure.test :refer [deftest is]]))

;; #########
;; #b.A.@.a#
;; #########
(def ex1 {[7 1] "a", [2 2] "#", [0 0] "#", [1 0] "#", [7 2] "#", [1 1] "b", [4 2] "#", [3 0] "#", [8 0] "#", [4 1] ".", [5 2] "#", [8 2] "#", [8 1] "#", [5 1] "@", [6 1] ".", [7 0] "#", [0 2] "#", [2 0] "#", [3 1] "A", [2 1] ".", [5 0] "#", [6 2] "#", [6 0] "#", [1 2] "#", [3 2] "#", [0 1] "#", [4 0] "#"})

(deftest count-keys-works
  (is (= 2 (d/count-keys ex1))))

(deftest initial-searcher-works
  (is (= {:position [5 1] :keyring #{}}
         (d/initial-searcher ex1))))

(deftest step-searchers-works
  (is (= #{{:position [4 1] :keyring #{}} {:position [6 1] :keyring #{}}}
         (-> {:searchers [{:position [5 1] :keyring #{}}] :area ex1}
             (d/step-searchers)
             (:searchers)
             (set)))))

(deftest done-fn-works
  (is (= true
         ((d/done-fn ex1) {:searchers [{:keyring #{"a" "b"}}]}))))

(deftest part1-examples
  (is (= 8
         (-> "#########
              #b.A.@.a#
              #########"
             util/despace
             util/string->position-map
             d/min-steps))))
