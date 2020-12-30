(ns y2020.day19-test
  (:require [y2020.day19 :as d]
            [y2020.util :as util]
            [clojure.test :refer [deftest is]]))

(def example1
  (d/parse
   (util/read "0: 1 2
               1: \"a\"
               2: 1 3 | 3 1
               3: \"b\"

               a
               aba
               bab")))

(deftest parse-rule-works
  (is (= ["{0}" "({1}{2})"] (d/parse-rule "0: 1 2")))
  (is (= ["{1}" "(a)"] (d/parse-rule "1: \"a\"")))
  (is (= ["{2}" "({1}{3}|{3}{1})"] (d/parse-rule "2: 1 3 | 3 1"))))

(deftest process-rules-works
  (is (= {:rules-to-process {"{0}" "({1}{2})"
                             "{2}" "({1}{3}|{3}{1})"}
          :processed-rules {"{1}" "(a)"
                            "{3}" "(b)"}}
         (d/process-rules {:rules-to-process (:rules example1)
                           :processed-rules {}})))
  (is (= {:rules-to-process {"{0}" "((a){2})"}
          :processed-rules {"{1}" "(a)"
                            "{3}" "(b)"
                            "{2}" "((a)(b)|(b)(a))"}}
         (d/process-rules {:rules-to-process {"{0}" "({1}{2})"
                                              "{2}" "({1}{3}|{3}{1})"}
                           :processed-rules {"{1}" "(a)"
                                             "{3}" "(b)"}})))
  (is (= {:rules-to-process {}
          :processed-rules {"{1}" "(a)"
                            "{3}" "(b)"
                            "{2}" "((a)(b)|(b)(a))"
                            "{0}" "((a)((a)(b)|(b)(a)))"}}
         (d/process-rules {:rules-to-process {"{0}" "((a){2})"}
                           :processed-rules {"{1}" "(a)"
                                             "{3}" "(b)"
                                             "{2}" "((a)(b)|(b)(a))"}}))))

(deftest rule-0-regex-works
  []
  (is (= "((a)((a)(b)|(b)(a)))" (str (d/rule-0-regex (:rules example1))))))

(deftest part1
  (is (= 144 (d/part1))))

(comment "run tests"
  (clojure.test/run-tests))
