(ns aoc.intcode-computer
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.test :refer [deftest is run-tests]]))

(defn parse
  [input]
  (mapv edn/read-string (string/split input #",")))

(defn next-instruction
  [{:keys [program position]}]
  (mod (get program position)
       100))

(defn immediate-mode?
 [op n]
 (pos? 
  (mod (quot op (* 100 n))
   10)))

(defn read-parameter
  [program position n raw?]
  (let [op (get program position)
        raw-arg (get program (+ n position))]
    (if (or raw?
            (immediate-mode? op n))
      raw-arg
      (get program raw-arg))))

(defn binary-op
  [{:keys [position] :as computer} f]
  (-> computer
      (update :program
              #(assoc %
                      (read-parameter % position 3 true)
                      (f (read-parameter % position 1 false)
                         (read-parameter % position 2 false))))
      (update :position #(+ 4 %))))

(defn new-computer
  [program input]
  {:program program
   :position 0
   :input (or input [])
   :output []})

(defmulti exec-once next-instruction)

(defmethod exec-once 1
  [computer]
  (binary-op computer +))

(defmethod exec-once 2
  [computer]
  (binary-op computer *))

(defmethod exec-once 3
  [{:keys [position input] :as computer}]
  (-> computer
      (update :program
              #(assoc %
                      (read-parameter % position 1 true)
                      (first input)))
      (update :input rest)
      (update :position + 2)))

(deftest op-3-works
  (is (= [4] (:output (exec-once {:program [4 0 99] :position 0})))))

(defmethod exec-once 4
  [{:keys [program position] :as computer}]
  (-> computer
      (update :output conj (read-parameter program position 1 false))
      (update :position + 2)))

(defmethod exec-once 5
  [{:keys [program position] :as computer}]
  (if (not= 0 (read-parameter program position 1 false))
    (assoc computer :position (read-parameter program position 2 false))
    (update computer :position + 3)))

(defmethod exec-once 6
  [{:keys [program position] :as computer}]
  (if (= 0 (read-parameter program position 1 false))
    (assoc computer :position (read-parameter program position 2 false))
    (update computer :position + 3)))

(defmethod exec-once 7
  [computer]
  (binary-op computer #(if (< %1 %2) 1 0)))

(defmethod exec-once 8
  [computer]
  (binary-op computer #(if (= %1 %2) 1 0)))

(defmethod exec-once 99
  [computer]
  (assoc computer :halt true))

(defn exec-all
  {:test #(is (= 17 (-> (exec-all [3 0 4 0 99] [17])
                        :output
                        last)))}
  ([program input]
   (->> (iterate exec-once (new-computer program input))
        (take-while #(not (:halt %)))
        last))
  ([program] (exec-all program nil)))

(defn day2a
  {:test #(is (= 4576384 (day2a)))}
  []
  (-> (parse (slurp (io/resource "day2")))
      (assoc 1 12
             2 2)
      (exec-all)
      (:program)
      (first)))

(defn day2b
  []
  (let [input (parse (slurp (io/resource "day2")))]
    (first (for [noun (range 100)
                verb (range 100)
                 :let [result (-> (assoc input 1 noun 2 verb)
                                  (exec-all)
                                  (:program)
                                  (first))]
                :when (= 19690720 result)]
            (+ verb (* 100 noun))))))

(comment "slow test"
  (= 5398 (day2b)))

(defn day5-1
  {:test #(is (= 15314507 (day5-1)))}
  []
  (-> (parse (slurp (io/resource "day5")))
      (exec-all [1])
      (:output)
      (last)))

(deftest day5-2-examples
  (is (= 1 
         (-> (exec-all [3 9 8 9 10 9 4 9 99 -1 8]
                       [8])
             (:output)
             (last))))
  (is (= 0 
         (-> (exec-all [3 3 1108 -1 8 3 4 3 99]
                       [7])
             (:output)
             (last))))
  (is (= 1 
         (-> (exec-all [3 12 6 12 15 1 13 14 13 4 13 99 -1 0 1 9]
                       [8])
             (:output)
             (last)))))

(defn day5-2
  {:test #(is (= 652726 (day5-2)))}
  []
  (-> (parse (slurp (io/resource "day5")))
      (exec-all [5])
      (:output)
      (last)))

(run-tests)
