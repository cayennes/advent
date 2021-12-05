(ns y2021.day03
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [y2021.util :as util]))

(defn parse
  [input]
  (->> input
       string/split-lines
       (map (partial map (partial str)))
       (map (partial map edn/read-string))))

(defn most-common
  [digits]
  (if (>= (* 2 (apply + digits))
          (count digits))
    1
    0))

(def flip-digit {1 0, 0 1})

(defn least-common
  [digits]
  (flip-digit (most-common digits)))

(defn zip-most-common
  [seqs]
  (for [items-at-place (apply map vector seqs)]
    (most-common items-at-place)))

(defn flip-digits
  [digits]
  (map flip-digit digits))

(defn binary-digits->int
  [digits]
  (Integer/parseInt (apply str digits) 2))

(defn part1*
  [seqs]
  (let [gamma-digits (zip-most-common seqs)
        epsilon-digits (flip-digits gamma-digits)]
    (* (binary-digits->int gamma-digits)
       (binary-digits->int epsilon-digits))))

(defn part1
  ([input] (-> input parse part1*))
  ([] (-> "day03" io/resource slurp part1)))

(defn filter-digit
  [possibles i digit]
  (filter #(= digit (nth % i)) possibles))

(defn filter-step
  [{:keys [possibles i f]
    :or {i 0}}]
  (let [digit (f (map #(nth % i) possibles))]
    {:possibles (if (= 1 (count possibles))
                  possibles
                  (filter-digit possibles i digit))
     :i (inc i)
     :f f}))

(defn filter-all
  [seqs f]
  (-> (util/iterate-until filter-step
                          #(<= (count (:possibles %)) 1)
                          {:possibles seqs :f f})
      :possibles
      first
      binary-digits->int))

(defn part2*
  [seqs]
  (* (filter-all seqs most-common)
     (filter-all seqs least-common)))

(defn part2
  ([input] (-> input parse part2*))
  ([] (-> "day03" io/resource slurp part2)))
