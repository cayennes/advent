(ns aoc.day13
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]))

(defn count-blocks
  [s]
  (->> s
       (drop 2)
       (take-nth 3)
       (filter #(= 2 %))
       (count)))

(defn part1
  []
  (-> (util/read-input "day13" ic/parse)
      (ic/new-computer)
      (ic/exec-all)
      (:output)
      (count-blocks)))

(defn pop-score
  [screen]
  [(screen [-1 0]) (dissoc screen [-1 0])])

(defn update-screen
  [screen new]
  (reduce (fn [screen [x y thing]]
            (assoc screen [x y] thing))
          screen
          (partition 3 new)))

(defn display
  [screen]
  (let [[score game] (pop-score screen)]
    (println "score:" score)
    (println (util/show-location-map game {0 " " 1 "@" 2 "#" 3 "-" 4 "o"}))))

(defn get-human-input
  [screen]
  (display screen)
  (case (first (read-line))
    \d -1
    \f 1
    0))

(defn play-game
  []
  (ic/run-in-world
   (-> (util/read-input "day13" ic/parse) (assoc 1 2) (ic/new-computer))
   {[0 0] "."}
   update-screen
   get-human-input))

(defn part2
  []
  (-> (util/read-input "day13" ic/parse)
      (assoc 1 2)
      (ic/new-computer)
      (ic/exec-all)
      (:output)
      (->> (update-screen {}))
      (get-human-input)))
