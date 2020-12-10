(ns y2020.day09-test
  (:require [y2020.day09 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(def example
  [35 20 15 25 47 40 62 55 65 95 102 117 150 182 127 219 299 277 309 576])

(deftest is-sum-of-two-of-works
  (is (= true (d/is-sum-of-two-of 8 [1 2 7])))
  (is (= false (d/is-sum-of-two-of 10 [1 2 7]))))
