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
  ([program input]
   {:program program
    :position 0
    :input (or input [])
    :output []})
  ([program]
   (new-computer program [])))

(defmulti exec-once next-instruction)

(defmethod exec-once 1
  [computer]
  (binary-op computer +))

(defmethod exec-once 2
  [computer]
  (binary-op computer *))

;; read input
(defmethod exec-once 3
  [{:keys [position input] :as computer}]
  (if (empty? input)
    (assoc computer :awaiting-input true)
    (-> computer
        (dissoc :awaiting-input)
        (update :program
                #(assoc %
                        (read-parameter % position 1 true)
                        (first input)))
        (update :input rest)
        (update :position + 2))))

(deftest op-3-works
  (is (= [16 0 99]
         (:program (exec-once (new-computer [3 0 99] [16])))))
  (is (= {:program [3 0 99]
          :awaiting-input true}
         (select-keys (exec-once (new-computer [3 0 99] []))
                      [:program :awaiting-input])))
  (is (= {:program [0 0 99]}
         (select-keys (exec-once (assoc (new-computer [3 0 99] [0])
                                        :awaiting-input true))
                      [:program :awaiting-input]))))

(defmethod exec-once 4
  [{:keys [program position] :as computer}]
  (-> computer
      (update :output conj (read-parameter program position 1 false))
      (update :position + 2)))

(deftest op-4-works
  (is (= [4] (:output (exec-once {:program [4 0 99] :position 0})))))

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
  {:test #(is (= 17 (-> (new-computer [3 0 4 0 99] [17])
                        (exec-all)
                        :output
                        last)))}
  [computer]
  (->> (iterate exec-once computer)
       (drop-while #(not (or (:halt %)
                             (:awaiting-input %))))
       first))

(defn day2a
  {:test #(is (= 4576384 (day2a)))}
  []
  (-> (parse (slurp (io/resource "day2")))
      (assoc 1 12
             2 2)
      (new-computer)
      (exec-all)
      (:program)
      (first)))

(defn day2b
  []
  (let [input (parse (slurp (io/resource "day2")))]
    (first (for [noun (range 100)
                verb (range 100)
                 :let [result (-> (assoc input 1 noun 2 verb)
                                  (new-computer)
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
      (new-computer [1])
      (exec-all)
      (:output)
      (last)))

(deftest day5-2-examples
  (is (= 1 
         (-> (new-computer [3 9 8 9 10 9 4 9 99 -1 8]
                           [8])
             (exec-all)
             (:output)
             (last))))
  (is (= 0 
         (-> (new-computer [3 3 1108 -1 8 3 4 3 99]
                           [7])
             (exec-all)
             (:output)
             (last))))
  (is (= 1 
         (-> (new-computer [3 12 6 12 15 1 13 14 13 4 13 99 -1 0 1 9]
                           [8])
             (exec-all)
             (:output)
             (last)))))

(defn day5-2
  {:test #(is (= 652726 (day5-2)))}
  []
  (-> (parse (slurp (io/resource "day5")))
      (new-computer [5])
      (exec-all)
      (:output)
      (last)))

(defn final-output
  "returns the last output of the last computer in a sequence"
  [computers]
  (-> computers last :output last))

(defn amplifier-vec
  "creates a vector of amplifier computers with the given phases"
  [program phases]
  (mapv #(new-computer program [%]) phases))

(defn add-first-input
  {:test #(is [{:input [1 2]}] (add-first-input [{:input [1]}] 2))}
  [computers input]
  (update-in computers [0 :input] #(concat % [input])))

(defn run-multiple
  {:test #(do (is (= 43210
                     (-> (amplifier-vec [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0]
                                        [4 3 2 1 0])
                         (add-first-input 0)
                         (run-multiple)
                         (final-output))))
              (is (= 54321
                     (-> (amplifier-vec [3 23 3 24 1002 24 10 24 1002 23 -1 23 101 5 23 23 1 24 23 23 4 23 99 0 0]
                                        [0 1 2 3 4])
                         (add-first-input 0)
                         (run-multiple)
                         (final-output))))
              (is (= 65210
                     (-> (amplifier-vec [3 31 3 32 1002 32 10 32 1001 31 -2 31 1007 31 0 33 1002 33 7 33 1 33 31 31 1 32 31 31 4 31 99 0 0 0]
                                        [1 0 4 3 2])
                         (add-first-input 0)
                         (run-multiple)
                         (final-output)))))}
  [computers]
  (vec
   (reductions (fn [previous-computer next-computer]
                 (exec-all
                  (update next-computer :input
                          #(concat % [(-> previous-computer :output last)]))))
               (exec-all (first computers))
               (rest computers))))

(defn permutations
  [coll]
  (if (<= (count coll) 1)
    [(seq coll)]
    (apply concat
           (for [item coll
                 :let [others (disj (set coll) item)]]
             (map #(conj % item) (permutations others))))))

(defn loop-input
  {:test #(is (= [{:input [78]} {:output [56 78]}]
                 (loop-input [{} {:output [56 78]}])))}
  [computers]
  (add-first-input computers (last (:output (last computers)))))

(defn run-multiple-until-done
  {:test #(do
            (is (= 43210
                   (final-output
                    (run-multiple-until-done
                     (add-first-input
                      (amplifier-vec [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0]
                                     [4 3 2 1 0])
                      0)))))
            #_(is (= 139629729
                     (final-output
                      (run-multiple-until-done
                       (add-first-input
                        (amplifier-vec [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 0 0 5]
                                       [9 8 7 6 5])
                        0)))))
            #_(is (= 18216
                   (final-output
                    (run-multiple-until-done
                     (add-first-input
                      (amplifier-vec [3 52 1001 52 -5 52 3 53 1 52 56 54 1007 54 5 55 1005 55 26 1001 54 -5 54 1105 1 12 1 53 54 53 1008 54 0 55 1001 55 1 55 2 53 55 53 4 53 1001 56 -1 56 1005 56 6 99 0 0 0 0 10]
                                     [9 7 8 5 6])
                      0))))))}
  [computers]
  (let [first-run (run-multiple computers)]
    (->> first-run
         (iterate #(run-multiple (loop-input %)))
         (cons first-run)
         (drop-while #(not (:halt (last %))))
         (first))))

(defn find-max-amplification
  {:test #(is (= 43210
                 (find-max-amplification [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0] (range 5))))}
  [program phases]
  (apply max
         (for [phase-order (permutations phases)]
           (-> (amplifier-vec program phase-order)
               (add-first-input 0)
               (run-multiple-until-done)
               (final-output)))))

(comment (->> (iterate run-multiple
                       (add-first-input
                        (amplifier-vec [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 0 0 5]
                                       [9 8 7 6 5])
                        0))
              (take 5)
              (last)))

(defn day7-1
  {:test #(is (= 272368 (day7-1)))}
  []
  (-> (io/resource "day7") (slurp) (parse)
      (find-max-amplification (range 5))))

(defn day7-2
  []
  (-> (io/resource "day7") (slurp) (parse)
      (find-max-amplification (range 5 10))))

(run-tests)
