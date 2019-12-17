(ns aoc.day17
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]
            [clojure.string :as string]))

(defn ascii
  [int-seq]
  (apply str (map char int-seq)))

(defn string->position-map
  [diagram-string]
  (into {}
        (for [[y row] (map-indexed vector (string/split diagram-string #"\n"))
              [x ch] (map-indexed vector row)]
          [[x y] (str ch)])))

(defn intersection?
  [scaffold-map pos]
  (every? #(= (scaffold-map %) "#")
          (conj (util/adjacent-positions pos) pos)))

(defn all-intersections
  [scaffold-map]
  (filter #(intersection? scaffold-map (first %)) scaffold-map))

(defn intersections-checksum
  [scaffold-map]
  (->> scaffold-map
       (all-intersections)
       (map first)
       (map #(apply * %))
       (apply +)))

;; TODO: add test that this is 4112
(defn part1
  []
  (let [output-str (-> (util/read-input "day17" ic/parse)
                       (ic/new-computer)
                       (ic/exec-all)
                       (:output)
                       (ascii))]
    (println output-str)
    (-> output-str
        (string->position-map)
        (intersections-checksum))))
