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
  [program position n write?]
  (let [op (get program position)
        raw-arg (get program (+ n position))]
    (if (or write?
            (immediate-mode? op n))
      raw-arg
      (get program raw-arg))))

(defn exec-once
  [{:keys [program position input output]
    :or {output []}
    :as computer}]
  (case (next-instruction computer)
    1 (-> computer
          (update :program
                  #(assoc %
                          (read-parameter % position 3 true)
                          (+ (read-parameter % position 1 false)
                             (read-parameter % position 2 false))))
          (update :position #(+ 4 %)))
    2  (-> computer
          (update :program
                  #(assoc %
                          (get % (+ 3 position))
                          (* (get % (get % (+ 1 position)))
                             (get % (get % (+ 2 position))))))
          (update :position + 4))
    3 (-> computer
          (update :program
                  #(assoc %
                          (get % (inc position))
                          (first input)))
          (update :input rest)
          (update :position + 2))
    4 (-> computer
          (update :output conj (get program (+ 1 position)))
          (update :position + 2))
    99 (assoc computer :halt true)))

(defn exec-all
  {:test #(is (= 17 (exec-all [3 0 4 0 99] [17])))} ;; TODO: oops, this isn't output. make exec-all work with output. I guess it should have a whole computer datastructure after all.
  ([program input]
   (try (->> (iterate exec-once {:program program :position 0 :input input})
             (take-while #(not (:halt %)))
             last
             :program
             first)
        #_(catch Exception e
            nil)))
  ([program] (exec-all program [])))

(defn day2a
  {:test #(is (= 4576384 (day2a)))}
  []
  (-> (parse (slurp (io/resource "day2")))
      (assoc 1 12
             2 2)
      (exec-all)))

(defn day2b
  []
  (first (for [noun (range 100)
               verb (range 100)
               :let [output (exec-all (assoc (parse (slurp (io/resource "day2")))
                                             1 noun
                                             2 verb))]
               :when (= 19690720 output)]
           (+ verb (* 100 noun)))))

(comment "slow tests"
  (= 5398 (day2b)))

(run-tests)
