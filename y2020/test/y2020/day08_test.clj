(ns y2020.day08-test
  (:require [y2020.day08 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(def example
  (util/read
   "nop +0
    acc +1
    jmp +4
    acc +3
    jmp -3
    acc -99
    acc +1
    jmp -4
    acc +6"))
