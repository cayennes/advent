(ns aoc.day15
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]
            [clojure.math.numeric-tower :as nt]))

;; we have infinite robots, each with its own computer inside it
;; but they share a map.
;; we will spawn new ones in each location to explore multiple directions for a
;; breadth first search, and drop robots that are in the same place after the
;; same number of steps
;; {:robots [{:computer {...} :position [-2 0]} ...]
;;  :map {[0 0] :empty [-1 0] :wall ...}

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
              result (case result-code 0 :wall 1 :empty 2 :oxygen)]
        :when (= ::unvisited (get-in world [:map moving-to] ::unvisited))]
    {:map-data {moving-to result}
     :robot {:computer new-computer
             :position (if (= :wall result)
                         (:position robot)
                         moving-to)}}))

(defn merge-robot-data
  [world explorers]
  (let [new-map (apply merge (:map world) (map :map-data explorers))]
    (if (= (:map world) new-map)
      (assoc world :fully-explored true)
      {:robots (util/distinct-by :position (map :robot explorers))
       :map new-map})))

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
       (:oxygen)))

(defn initial-world
  [program]
  {:robots [{:computer (ic/new-computer program)
             :position [0 0]}]
   :map {[0 0] :empty}})

(defn part1
  []
  (let [[world move-count] (->> (util/read-input "day15" ic/parse)
                                (initial-world)
                                (util/counting-iterate-until
                                 explore-step
                                 oxygen-system-found?))]
    (println (util/show-location-map (:map world)
                                     {:wall "#"
                                      :empty "."
                                      :oxygen "O" }))
    move-count))

(defn complete-map
  [program]
  (->> program
       (initial-world)
       (util/iterate-until explore-step :fully-explored)
       (:map)))

(defn adjacent-positions
  [position]
  (map #(mapv + % position) (vals moves)))

(defn fill-step
  [room-map]
  (into room-map
        (for [[position contents] room-map
              adjacent-position (adjacent-positions position)
              :when (and (= :oxygen contents)
                         (= :empty (room-map adjacent-position)))]
          [adjacent-position :oxygen])))

(defn oxygenation-complete?
  [room-map]
  (not (some #(= :empty %) (vals room-map))))

(defn part2
  []
  (->> (util/read-input "day15" ic/parse)
       (complete-map)
       (util/counting-iterate-until
        fill-step
        oxygenation-complete?)
       (second)))
