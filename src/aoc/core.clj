(ns aoc.core
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn parse
  [input]
  (mapv edn/read-string (string/split input #",")))

(def input
  (parse (slurp (io/resource "day2"))))

(defn exec-once
  [{:keys [program position]}]
  (case (get program position)
    1 {:program (assoc program
                       (get program (+ 3 position))
                       (+ (get program (get program (+ 1 position)))
                          (get program (get program (+ 2 position)))))
       :position (+ 4 position)}
    2 {:program (assoc program
                       (get program (+ 3 position))
                       (* (get program (get program (+ 1 position)))
                          (get program (get program (+ 2 position)))))
       :position (+ 4 position)}
    99 {:program program
        :position position
        :halt true}))

(defn exec-all
  [program]
  (try (->> (iterate exec-once {:program program :position 0})
        (take-while #(not (:halt %)))
        last
        :program
        first)
       (catch Exception e
         nil)))

(defn day2a
  []
  (-> input
      (assoc 1 12
             2 2)
      (exec-all)))

(defn day2b
  []
  (first (for [noun (range 100)
               verb (range 100)
               :let [output (exec-all (assoc input 1 noun 2 verb))]
               :when (= 19690720 output)]
           (+ verb (* 100 noun)))))
