(ns aoc.day8
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.test :refer [is run-tests]]))

(defn parse-layers
  {:test #(is (=  '((0 2 2 2) (1 1 2 2) (2 2 1 2) (0 0 0 0))
                  (parse-layers "0222112222120000" 2 2)))}
  [input x-size y-size]
  (partition (* x-size y-size)
             (map #(edn/read-string (str %)) input)))

(defn eq-fn
  [x]
  #(= x %))

(defn checksum
  [layers]
  (let [layer (apply min-key #(count (filter zero? %)) layers)]
    (* (count (filter (eq-fn 1) layer))
       (count (filter (eq-fn 2) layer)))))

(defn day8-1
  {:test #(is (= 1950 (day8-1)))}
  []
  (-> (io/resource "day8") (slurp) (parse-layers 25 6)
      (checksum)))

(defn stack-layers
  {:test #(is (= [0 1 1 0] (stack-layers '((0 2 2 2) (1 1 2 2) (2 2 1 2) (0 0 0 0)))))}
  [layers]
  (reduce #(map (fn [im l] (if (= 2 im) l im)) %1 %2)
          layers))

(def display-chars
  {0 " "
   1 "@"
   2 "."})

(defn display
  [layers x-size y-size]
  (->> layers
       (stack-layers)
       (map display-chars)
       (partition x-size)
       (map #(apply str %))
       (string/join "\n")))

(defn day8-2
  []
  (-> (io/resource "day8") (slurp) (parse-layers 25 6)
      (display 25 6)
      (println)))

(run-tests)
