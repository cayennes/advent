(ns aoc.day15
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]))

;; we have infinite robots, each with its own computer inside it
;; but they share a map.
;; we will spawn new ones in each location to explore multiple directions for a
;; breadth first search, and drop robots that are in the same place after the
;; same number of steps
;; {:robots [{:computer {...} :position [-2 0]} ...]
;;  :map {[0 0] :corridor [-1 0] :wall ...}

(def moves {1 [0 -1] 2 [0 1] 3 [-1 0] 4 [1 0]})

(defn next-explorers
  [world]
  (for [robot (:robots world)
        [move-code motion] moves
        :let [moving-to (mapv + (:position robot) motion)
              [[result-code] new-computer] (-> (:computer robot)
                                               (ic/add-input move-code)
                                               (ic/exec-all)
                                               (ic/slurp-output))
              result (case result-code 0 :wall 1 :corrider 2 :oxygen-system)]
        :when (= ::unvisited (get-in world [:map moving-to] ::unvisited))]
    {:map-data {moving-to result}
     :robot {:computer new-computer
             :position (if (= :wall result)
                         (:position robot)
                         moving-to)}}))

(defn merge-robot-data
  [world explorers]
  (let [new-map (apply merge (:map world) (map :map-data explorers))]
    {:robots (util/distinct-by :position (map :robot explorers))
     :map new-map}))

(defn explore-step
  [world]
  (->> world
       next-explorers
       (merge-robot-data world)))

(defn oxygen-system-found?
  [world]
  (->> (:map world)
       (vals)
       (set)
       (:oxygen-system)))

(defn initial-world
  [program]
  {:robots [{:computer (ic/new-computer program)
             :position [0 0]}]
   :map {[0 0] :corridor}})

;; TODO: write test asserting this is 228
(defn part1
  []
  (let [[world move-count] (->> (util/read-input "day15" ic/parse)
                                (initial-world )
                                (util/counting-iterate-until
                                 explore-step
                                 oxygen-system-found?))]
    (println (util/show-location-map (:map world)
                                     {:wall "#"
                                      :corrider "."
                                      :oxygen-system "O" }))
    move-count))
