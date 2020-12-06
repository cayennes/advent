(ns y2020.day05
  (:require [clojure.string :as string]
            [y2020.util :as util]))

(defn parse
  [input]
  (map vec (string/split-lines input)))

(defn bsp->number
  [s]
  (->> s
       (map {\F 0 \B 1
             \L 0 \R 1})
       (reduce #(+ (* 2 %1) %2))))

(defn taken-seats
  [input]
  (mapv bsp->number input))

(defn part1*
  [input]
  (apply max (taken-seats input)))

(def part1 (util/make-run-fn "day05" parse part1*))

(defn part2*
  [input]
  (let [taken (set (taken-seats input))]
    (->> (range (* 128 8))
         (partition 3 1)
         (filter (fn [[before this after]]
                   (and (not (taken this))
                        (taken before)
                        (taken after))))
         (first)
         (second))))

(def part2 (util/make-run-fn "day05" parse part2*))

(comment "run"
  (part1)
  (part2))
