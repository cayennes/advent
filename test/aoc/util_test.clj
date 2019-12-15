(ns aoc.util-test
  (:require [aoc.util :as util]
            [clojure.test :refer [deftest is]]))

(deftest counting-iterate-until-works
  (is (= [3 5] (util/counting-iterate-until #(/ % 2) 96 odd?))))

