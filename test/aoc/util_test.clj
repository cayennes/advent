(ns aoc.util-test
  (:require [aoc.util :as util]
            [clojure.test :refer [deftest is]]))

(deftest counting-iterate-until-works
  (is (= [3 5] (util/counting-iterate-until #(/ % 2) odd? 96))))

(deftest digits->number-works
  (is (= 17 (util/digits->number [1 7]))))


(deftest string->position-map-works
  (is (= {[0 0] "#", [1 0] ".", [0 1] "#", [1 1] "#"}
         (-> "#.
              ##"
             util/despace
             util/string->position-map)))
  (is (= {[0 0] "#", [1 0] ".", [0 1] "#", [1 1] "#"}
         (-> "#.
              ##"
             util/despace
             util/string->position-map))))

(deftest vec+-works
  (is (= [110 12  13 13]
         (util/vec+ [0 1 1 0] [10 11 12 13] [100 0 0 0]))))
