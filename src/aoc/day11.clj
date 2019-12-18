(ns aoc.day11
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]
            [clojure.java.io :as io]))

(defn move-robot
  [robot rotation-code]
  (-> robot
      (util/turn (case rotation-code 0 :left 1 :right))
      (util/move-forward 1)))

(defn make-color-input
  [{:keys [robot hull]}]
  [(get hull (:position robot) 0)])

(defn instruct-robot
  [{:keys [robot hull]} [paint-color rotation-code]]
  {:hull (assoc hull (:position robot) paint-color)
   :robot (move-robot robot rotation-code)})

(defn do-stuff
  [{:keys [robot hull computer]}]
  (let [inputs (make-color-input {:robot robot :hull hull})
        [instructions next-computer] (-> computer
                                         (ic/add-inputs inputs)
                                         (ic/exec-all)
                                         (ic/slurp-output))]
    (assoc (instruct-robot {:robot robot :hull hull} instructions)
           :computer next-computer)))

(defn do-everything
  [program initial-hull]
  (->> {:hull initial-hull
        :robot {:position [0 0]
                :orientation [0 -1]}
        :computer (ic/new-computer program)}
       (iterate do-stuff)
       (drop-while #(not (get-in % [:computer :halt])))
       (first)))

(defn part1
  []
  (-> (io/resource "day11") (slurp) (ic/parse)
      (do-everything {})
      (:hull)
      (count)))

(defn part2
  []
  (let [result (-> (io/resource "day11") (slurp) (ic/parse)
                   (do-everything {[0 0] 1})
                   (:hull)
                   (util/show-location-map {0 "." 1 "#"}))]
    (println result)
    result))
