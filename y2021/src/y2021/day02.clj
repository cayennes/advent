(ns y2021.day02
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn parse
  [input]
  (->> input
       (string/split-lines)
       (map #(string/split % #" +"))
       (map #(map edn/read-string %))))

(defn part1*
  [input]
  (->> (reduce (fn [[x y] [direction distance]]
                 (case direction
                   forward [(+ x distance) y]
                   up [x (- y distance)]
                   down [x (+ y distance)]))
               [0 0]
               input)
       (apply *)))

(defn part1 []
  (-> "day02" io/resource slurp parse part1*))

(defn part2*
  [input]
  (->> (reduce (fn [[x y aim] [instruction amount]]
                 (case instruction
                   forward [(+ x amount) (+ y (* amount aim)) aim]
                   up [x y (- aim amount)]
                   down [x y (+ aim amount)]))
               [0 0 0]
               input)
       (butlast)
       (apply *)))

(defn part2 []
  (-> "day02" io/resource slurp parse part2*))
