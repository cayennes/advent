(ns aoc.day11
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]
            [clojure.java.io :as io]))

(defn move-robot
  [robot rotation-code]
  (-> robot
      (util/turn (case rotation-code 0 :left 1 :right))
      (util/move-forward 1)))

(defn do-stuff
  [{:keys [robot hull computer]}]
  (let [current-color (get hull (:position robot) 0)

        [[paint-color rotation-code] next-computer]
        (-> computer
            (ic/add-input current-color)
            (ic/exec-all)
            (ic/slurp-output))]
    {:computer next-computer
     :hull (assoc hull (:position robot) paint-color)
     :robot (move-robot robot rotation-code)}))

(defn do-everything
  [program initial-hull]
  (->> {:hull initial-hull
        :robot {:position [0 0]
                :orientation [0 -1]}
        :computer (ic/new-computer program)}
       (iterate do-stuff)
       (drop-while #(not (get-in % [:computer :halt])))
       (first)))

(defn day11-1
  []
  (-> (io/resource "day11") (slurp) (ic/parse)
      (do-everything {})
      (:hull)
      (count)))

(defn day11-2
  []
  (-> (io/resource "day11") (slurp) (ic/parse)
      (do-everything {[0 0] 1})
      (:hull)
      (util/show-location-map {0 "." 1 "#"})
      (println)))
