(ns y2021.day06
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))

(defn parse
  [input]
  (edn/read-string (str "(" input ")")))

(defn generation
  [fish]
  (concat (map #(if (zero? %) 6 (dec %)) fish)
          (->> fish (filter zero?) (map (constantly 8)))))

(defn fish-count-after
  [fish n]
  (-> (iterate generation fish)
      (nth n)
      (count)))

(defn part1*
  [fish]
  (fish-count-after fish 80))

(defn part1
  ([input] (-> input parse part1*))
  ([] (-> "day06" io/resource slurp part1)))
