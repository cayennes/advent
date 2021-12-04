(ns y2021.day03
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn parse
  [input]
  (->> input
       string/split-lines
       (map (partial map (partial str)))
       (map (partial map edn/read-string))))

(defn zip-most-common
  [seqs]
  (let [num-items (count seqs)]
    (for [items-at-place (apply map vector seqs)]
      (Math/round (double (/ (apply + items-at-place) num-items))))))

(defn part1*
  [seqs]
  (let [thing-to-xor (-> (count (first seqs))
                         (repeat 1)
                         (->> (apply str))
                         (Integer/parseInt 2))
        most-common-digits (-> seqs
                               (zip-most-common)
                               (->> (apply str))
                               (Integer/parseInt 2))
        least-common-digits (bit-xor thing-to-xor most-common-digits)]
    (* most-common-digits least-common-digits)))

(defn part1
  ([input] (-> input parse part1*))
  ([] (-> "day03" io/resource slurp part1)))
