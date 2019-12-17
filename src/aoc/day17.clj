(ns aoc.day17
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]
            [clojure.string :as string]))

(defn ascii
  [int-seq]
  (apply str (map char int-seq)))

(defn string->position-map
  [s]
  (map-indexed
   (fn [i row]
     (map-indexed
      (fn [j col] :TODO) row)
     (string/split #"\n"))))

(defn part1
  []
  (-> (util/read-input "day17" ic/parse)
      (ic/new-computer)
      (ic/exec-all)
      (:output)))

(comment (-> (util/read-input "day17" ic/parse)
             (ic/new-computer)
             (ic/exec-all)
             (:output)
             (ascii)
             (println)))
