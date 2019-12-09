(ns aoc.intcode-computer-test
  (:require [clojure.test :refer [deftest is]]
            [aoc.intcode-computer :as ic]))

(deftest day-2-1-examples ;; incomplete
  (is (= 3500
         (-> [1 9 10 3 2 3 11 0 99 30 40 50]
             ic/new-computer
             ic/exec-all
             :program
             first))))

(deftest op-3-works
  (is (= [16 0 99]
         (:program (ic/exec-once (ic/new-computer [3 0 99] [16])))))
  (is (= {:program [3 0 99]
          :awaiting-input true}
         (select-keys (ic/exec-once (ic/new-computer [3 0 99] []))
                      [:program :awaiting-input])))
  (is (= {:program [0 0 99]}
         (select-keys (ic/exec-once (assoc (ic/new-computer [3 0 99] [0])
                                           :awaiting-input true))
                      [:program :awaiting-input]))))

(deftest op-4-works
  (is (= [4] (:output (ic/exec-once {:program [4 0 99] :position 0})))))

(deftest exec-all-works
  (is (= 17 (-> (ic/new-computer [3 0 4 0 99] [17])
                (ic/exec-all)
                :output
                last))))

(deftest day2-1-result
  (is (= 4576384 (ic/day2-1))))

(comment "day2-2 result is slower than I want to test"
  (= 5398 (ic/day2-2)))

(deftest parameter-mode-works
  (is (= 0 (ic/parameter-mode 1002 1))
      (= 1 (ic/parameter-mode 1006 2))))

(deftest day-5-1-examples ;; incomplete
  (is (= [1000]
         (-> [3 21 1008 21 8 20 1005 20 22 107 8 21 20 1006 20 31 1106 0 36 98 0 0 1002 21 125 20 4 20 1105 1 46 104 999 1105 1 46 1101 1000 1 20 4 20 1105 1 46 98 99]
             (ic/new-computer [8])
             (ic/exec-all)
             (:output)))))

(deftest day5-1-result
  (is (= 15314507 (ic/day5-1))))

(deftest day5-2-examples
  (is (= 1 
         (-> (ic/new-computer [3 9 8 9 10 9 4 9 99 -1 8]
                              [8])
             (ic/exec-all)
             (:output)
             (last))))
  (is (= 0 
         (-> (ic/new-computer [3 3 1108 -1 8 3 4 3 99]
                              [7])
             (ic/exec-all)
             (:output)
             (last))))
  (is (= 1 
         (-> (ic/new-computer [3 12 6 12 15 1 13 14 13 4 13 99 -1 0 1 9]
                              [8])
             (ic/exec-all)
             (:output)
             (last)))))

(deftest day5-2-result
  (is (= 652726 (ic/day5-2))))

(deftest add-first-input-works
  (is [{:input [1 2]}] (ic/add-first-input [{:input [1]}] 2)))

(deftest run-multiple-works
  (is (= 43210
         (-> (ic/amplifier-vec [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0]
                            [4 3 2 1 0])
             (ic/add-first-input 0)
             (ic/run-multiple)
             (ic/final-output))))
  (is (= 54321
         (-> (ic/amplifier-vec [3 23 3 24 1002 24 10 24 1002 23 -1 23 101 5 23 23 1 24 23 23 4 23 99 0 0]
                            [0 1 2 3 4])
             (ic/add-first-input 0)
             (ic/run-multiple)
             (ic/final-output))))
  (is (= 65210
         (-> (ic/amplifier-vec [3 31 3 32 1002 32 10 32 1001 31 -2 31 1007 31 0 33 1002 33 7 33 1 33 31 31 1 32 31 31 4 31 99 0 0 0]
                            [1 0 4 3 2])
             (ic/add-first-input 0)
             (ic/run-multiple)
             (ic/final-output)))))

(deftest loop-input-works
  (is (= [{:input [78]} {:output [56 78]}]
         (ic/loop-input [{} {:output [56 78]}]))))

(deftest run-multiple-until-done-works
  (is (= 43210
         (ic/final-output
          (ic/run-multiple-until-done
           (ic/add-first-input
            (ic/amplifier-vec [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0]
                              [4 3 2 1 0])
            0)))))
  (is (= 139629729
         (ic/final-output
          (ic/run-multiple-until-done
           (ic/add-first-input
            (ic/amplifier-vec [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 0 0 5]
                              [9 8 7 6 5])
            0)))))
  (is (= 18216
         (ic/final-output
          (ic/run-multiple-until-done
           (ic/add-first-input
            (ic/amplifier-vec [3 52 1001 52 -5 52 3 53 1 52 56 54 1007 54 5 55 1005 55 26 1001 54 -5 54 1105 1 12 1 53 54 53 1008 54 0 55 1001 55 1 55 2 53 55 53 4 53 1001 56 -1 56 1005 56 6 99 0 0 0 0 10]
                              [9 7 8 5 6])
            0))))))

(deftest find-max-amplification-works
  (is (= 43210
          (ic/find-max-amplification [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0] (range 5)))))

(deftest day7-1-result
  (is (= 272368 (ic/day7-1))))

(deftest day7-2-result
  (is (= 19741286 (ic/day7-2))))

(deftest relative-mode-works
  (is (= [109 1 36 2 203 0 99]
         (-> (ic/new-computer [109 1 109 2 203 0 99] [36])
             (ic/exec-all)
             (:program))))
  (is (= [109 6 2202 1 2 3 99 10 11 110]
         (-> (ic/new-computer [109 6 22202 1 2 3 99 10 11 0])
             (ic/exec-all)
             (:program)))))

(deftest day9-examples
  (let [quine [109 1 204 -1 1001 100 1 100 1008 100 16 101 1006 101 0 99]]
    (is (= quine (-> quine ic/new-computer ic/exec-all :output))))
  (is (= [1219070632396864] (-> [1102 34915192 34915192 7 4 7 99 0] ic/new-computer ic/exec-all :output)))
  (is (= [1125899906842624] (-> [104 1125899906842624 99] ic/new-computer ic/exec-all :output))))
