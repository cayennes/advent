(ns aoc.intcode-computer
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.test :refer [is run-tests]]))

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

(defn exec-once
  {:test #(is (= [4]
                 (:output (exec-once {:program [4 0 99] :position 0}))))}
  [{:keys [program position input output]
    :or {output []}
    :as computer}]
  (case (next-instruction computer)
    1 (binary-op computer +) 
    2 (binary-op computer *)
    3 (-> computer
          (update :program
                  #(assoc %
                          (read-parameter % position 1 true)
                          (first input)))
          (update :input rest)
          (update :position + 2))
    4 (-> computer
          (update :output conj (read-parameter program position 1 false))
          (update :position + 2))
    99 (assoc computer :halt true)))

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

(comment "slow tests"
  (= 5398 (day2b)))

(defn day5-1
  {:test #(is (= 15314507 (day5-1)))}
  []
  (-> (parse (slurp (io/resource "day5")))
      (exec-all [1])
      (:output)
      (last)))

(run-tests)
