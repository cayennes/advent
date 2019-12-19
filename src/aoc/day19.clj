(ns aoc.day19
  (:require [aoc.util :as util]
            [aoc.intcode-computer :as ic]))

(def coords (for [x (range 50) y (range 50)] [x y]))

(defn get-influence*
  [coord]
  (-> (util/read-input "day19" ic/parse)
      (ic/new-computer)
      (ic/add-inputs coord)
      (ic/exec-all)
      (:output)
      (first)))

(def get-influence (memoize get-influence*))

(defn part1
  []
  (apply + (for [x (range 50) y (range 50)] (get-influence [x y]))))

(defn display
  [x1 x2 y1 y2]
  (doseq [y (range y1 (inc y2))]
    (println
     (apply str
            (for [x (range x1 (inc x2))]
              (if (= 1 (get-influence [x y]))
                "#"
                "."))))))
