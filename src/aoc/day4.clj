(ns aoc.day4
  (:require [aoc.util :as util]
            [clojure.test :refer [is]]))

(defn double-item-at
  {:test #(do (is (= [0 0 1 2 3 4] (double-item-at (range 5) 0)))
              (is (= [0 1 2 3 4 4] (double-item-at (range 5) 4))))}
  [digits double-idx]
  (let [[start end] (split-at double-idx digits)]
    (vec (concat start [(first end)] end))))

(defn possibilities-within
  {:test #(is (= (set [111119 111122 111123])
                 (set (possibilities-within 111119 111123))))}
  [start end]
  (distinct
   (for [a (range 0 10)
         b (range a 10)
         c (range b 10)
         d (range c 10)
         e (range d 10)
         double-idx (range 5)
         :let [code (-> [a b c d e]
                        (double-item-at double-idx)
                        (util/digits->number))]
         :when (<= start code)
         :while (<= code end)]
     code)))

(defn day4a
  {:test #(is (= 1653 (day4a)))}
  []
  (count (possibilities-within 206938 679128)))

(defn possibilities-within-b
  {:test #(is (= [111122] (possibilities-within-b 111119 111123)))}
  [start end]
  (distinct
   (for [a (range 0 10)
         b (range a 10)
         c (range b 10)
         d (range c 10)
         e (range d 10)
         double-idx (range 5)
         :let [code (double-item-at [a b c d e] double-idx)
               code-number (util/digits->number code)]
         :when (and (<= start code-number)
                    (distinct? (get code double-idx)
                               (get code (dec double-idx))
                               (get code (+ 2 double-idx))))
         :while (<= code-number end)]
     code-number)))

(defn day4b
  {:test #(is (= 1133 (day4b)))}
  []
  (count (possibilities-within-b 206938 679128)))
