(ns y2020.day09
  (:require [y2020.util :as util]))

(defn parse
  [input]
  (mapv edn/read-string (string/split-lines input)))

(defn is-sum-of-two-of
  [n ns]
  (let [len (count ns)]
    (some #(= n %)
          (for [i (range 1 len)
                j (range i)]
            (+ (get ns i)
               (get ns j))))))

#_(def part1 (util/make-run-fn "day09" parse part1*))

#_(def part2 (util/make-run-fn "day09" parse part2*))

(comment "run"
  (part1)
  (part2))
