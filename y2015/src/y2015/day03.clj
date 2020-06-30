(ns y2015.day03
  (:require [y2015.util :as util]))

(defn parse
  [s]
  (map {\< [-1 0] \^ [0 -1] \> [1 0] \v [0 1]} s))

(defn positions
  [directions]
  (reductions util/vec+ [0 0] directions))

(defn homes-visited
  [directions]
  (->> directions
       (positions)
       (distinct)
       (count)))

(defn part1
  []
  (homes-visited (util/read-input "day03" parse)))

(defn homes-visited-by-pair
  [directions]
  (->> (concat (positions (take-nth 2 directions))
               (positions (take-nth 2 (rest directions))))
       (distinct)
       (count)))

(comment
    "tests"
  (= 3 (homes-visited-by-pair (parse "^v")))
  (= 3 (homes-visited-by-pair (parse "^>v<")))
  (= 11 (homes-visited-by-pair (parse "^v^v^v^v^v"))))

(defn part2
  []
  (homes-visited-by-pair (util/read-input "day03" parse)))
