(ns y2020.day07
  (:require [clojure.string :as string]
            [y2020.util :as util]))

(defn parse
  [input]
  (->>
   (for [line (string/split-lines input)
         :let [[_ outer-bag] (re-find #"^([\w ]+) bags contain" line)
               inner-bag-info (re-seq #"(\d+) ([\w ]+) bag" line)]]
     [outer-bag
      (->> (for [[_ number color] inner-bag-info]
             [color number])
           (into {}))])
   (into {})))

#_(def part1 (util/make-run-fn "day07" parse part1*))

#_(def part2 (util/make-run-fn "day07" parse part2*))

(comment "run"
  (part1)
  (part2))
