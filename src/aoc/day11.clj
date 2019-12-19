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

(defn do-everything
  [program initial-hull]
  (ic/run-in-world
   (ic/new-computer program)
   {:hull initial-hull
    :robot {:position [0 0]
            :orientation [0 -1]}}
   instruct-robot
   make-color-input))

(defn part1
  []
  (-> (io/resource "day11") (slurp) (ic/parse)
      (do-everything {})
      (get-in [:world :hull])
      (count)))

(defn part2
  []
  (let [result (-> (io/resource "day11") (slurp) (ic/parse)
                   (do-everything {[0 0] 1})
                   (get-in [:world :hull])
                   (util/show-location-map {0 "." 1 "#"}))]
    (println result)
    result))
