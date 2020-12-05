(ns y2020.day05
  (:require [y2020.util :as util]))

(defn bsp->number
  [s]
  (->> s
       (map {\F 0 \B 1
             \L 0 \R 1})
       (reduce #(+ (* 2 %1) %2))))

(defn taken-seats
  [input]
  (mapv bsp->number input))

(defn part1
  [input]
  (apply max (taken-seats input)))

(defn part2
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

(comment "run"
  (part1 (util/input-lines "day05" vec))
  (part2 (util/input-lines "day05" vec)))
