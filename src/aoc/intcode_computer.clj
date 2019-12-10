(ns aoc.intcode-computer
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn parse
  [input]
  (mapv edn/read-string (string/split input #",")))

(defn next-instruction
  [{:keys [program position]}]
  (mod (get program position)
       100))

(defn parameter-mode
  [op n]
  ({0 :position 1 :immediate 2 :relative}
   (mod (quot op (int (Math/pow 10 (inc n)))) 10)))

(defn read-memory
  [program n]
  (get program n (if (>= n 0) 0)))

(defn read-parameter
  [{:keys [program position relative-base]} n write?]
  (let [op (read-memory program position)
        raw-arg (read-memory program (+ n position))]
    (case (parameter-mode op n)
      :position (if write?
                  raw-arg
                  (read-memory program raw-arg))
      :immediate raw-arg
      :relative (if write?
                  (+ relative-base raw-arg)
                  (read-memory program (+ relative-base raw-arg))))))

(defn increase-memory
  [program n]
  (vec (concat program (repeat n 0))))

(defn write-memory
  [program n val]
  (cond
    (<= n (count program)) (assoc program n val)
    ;; make program vector much bigger so we don't have to do this repeatedly
    (pos? n) (-> program (increase-memory n) (assoc n val))))

(defn binary-op
  [computer f]
  (-> computer
      (update :program
              #(write-memory %
                             (read-parameter computer 3 true)
                             (f (read-parameter computer 1 false)
                                (read-parameter computer 2 false))))
      (update :position #(+ 4 %))))

(defn new-computer
  ([program input]
   {:program program
    :position 0
    :input (or input [])
    :output []
    :relative-base 0})
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
  [{:keys [input] :as computer}]
  (if (empty? input)
    (assoc computer :awaiting-input true)
    (-> computer
        (dissoc :awaiting-input)
        (update :program
                #(write-memory %
                               (read-parameter computer 1 true)
                               (first input)))
        (update :input rest)
        (update :position + 2))))

;; write output
(defmethod exec-once 4
  [computer]
  (-> computer
      (update :output conj (read-parameter computer 1 false))
      (update :position + 2)))

(defmethod exec-once 5
  [computer]
  (if (not= 0 (read-parameter computer 1 false))
    (assoc computer :position (read-parameter computer 2 false))
    (update computer :position + 3)))

(defmethod exec-once 6
  [computer]
  (if (= 0 (read-parameter computer 1 false))
    (assoc computer :position (read-parameter computer 2 false))
    (update computer :position + 3)))

(defmethod exec-once 7
  [computer]
  (binary-op computer #(if (< %1 %2) 1 0)))

(defmethod exec-once 8
  [computer]
  (binary-op computer #(if (= %1 %2) 1 0)))

;; update relative base
(defmethod exec-once 9
  [computer]
  (-> computer
      (update :relative-base
              + (read-parameter computer 1 false))
      (update :position + 2)))

(defmethod exec-once 99
  [computer]
  (assoc computer :halt true))

(defn needs-input?
  [computer]
  (and (:awaiting-input computer)
       (empty? (:input computer))))

(defn exec-all
  [computer]
  (->> (iterate exec-once computer)
       (drop-while #(not (or (:halt %)
                             (needs-input? %))))
       first))

(defn day2-1
  []
  (-> (parse (slurp (io/resource "day2")))
      (assoc 1 12
             2 2)
      (new-computer)
      (exec-all)
      (:program)
      (first)))

(defn day2-2
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

(defn day5-1
  []
  (-> (parse (slurp (io/resource "day5")))
      (new-computer [1])
      (exec-all)
      (:output)
      (last)))

(defn day5-2
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
  [computers input]
  (update-in computers [0 :input] #(concat % [input])))

(defn run-multiple
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
  [computers]
  (add-first-input computers (last (:output (last computers)))))

(defn run-multiple-until-done
  [computers]
  (let [first-run (run-multiple computers)]
    (->> first-run
         (iterate #(run-multiple (loop-input %)))
         (cons first-run)
         (drop-while #(not (:halt (last %))))
         (first))))

(defn find-max-amplification
  [program phases]
  (apply max
         (for [phase-order (permutations phases)]
           (-> (amplifier-vec program phase-order)
               (add-first-input 0)
               (run-multiple-until-done)
               (final-output)))))

(defn day7-1
  []
  (-> (io/resource "day7") (slurp) (parse)
      (find-max-amplification (range 5))))

(defn day7-2
  []
  (-> (io/resource "day7") (slurp) (parse)
      (find-max-amplification (range 5 10))))

(defn day9-1
  []
  (-> (io/resource "day9") (slurp) (parse)
      (new-computer [1])
      (exec-all)
      (:output)))

(defn day9-2
  []
  (-> (io/resource "day9") (slurp) (parse)
      (new-computer [2])
      (exec-all)
      (:output)
      (last)))
