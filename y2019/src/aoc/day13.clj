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
    (println (util/show-location-map game {0 " " 1 "@" 2 "#" 3 "=" 4 "o"}))))

(defn get-human-input
  [screen]
  (display screen)
  (case (first (read-line))
    \d [-1]
    \f [1]
    [0]))

;; for actual play, use console repl rather than inside emacs
(defn play-game
  []
  (ic/run-in-world
   (-> (util/read-input "day13" ic/parse) (assoc 0 2) (ic/new-computer))
   {}
   update-screen
   get-human-input))

(defn get-ai-input
  [screen]
  (let [ball-x (->> screen (filter #(= 4 (second %))) (first) (first) (first))
        paddle-x (->> screen (filter #(= 3 (second %))) (first) (first) (first))]
    (cond
      (< ball-x paddle-x) [-1]
      (> ball-x paddle-x) [1]
      :else [0])))

(defn watch-ai-play
  []
  (ic/run-in-world
   (-> (util/read-input "day13" ic/parse) (assoc 0 2) (ic/new-computer))
   {}
   update-screen
   #(do (display %) (get-ai-input %))))

(defn part2
  []
  (-> (ic/run-in-world
       (-> (util/read-input "day13" ic/parse) (assoc 0 2) (ic/new-computer))
       {}
       update-screen
       get-ai-input)
      (:world)
      (display)))
