(ns y2023.day02
  (:require [clojure.string :as string]
            [y2023.util :as util]))

(def ex
  "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green")

(defn parse-line
  [line]
  (let [[_ game-id marbles] (re-find #"Game (\d+): (.*)" line)
        handfuls (string/split marbles #";")]
    {:id (Integer/parseInt game-id)
     :handfuls (map #(into {} (for [[_ number color] (re-seq #"(\d+) (\w+)" %)]
                                [(keyword color) (Integer/parseInt number)])) handfuls)}))

(defn parse
  [input]
  (map parse-line (string/split input #"\n")))

(defn possible-handful?
  [handful]
  (and (<= (:red handful 0) 12)
       (<= (:green handful 0) 13)
       (<= (:blue handful 0) 14)))

(defn id-if-possible
  [game]
  (if (every? possible-handful? (:handfuls game))
    (:id game)
    0))

(defn part1
  [games]
  (apply + (map id-if-possible games)))

(comment "part 1"
  (part1 (parse ex))
  (part1 (parse (util/slurp-day "day02"))))

;; part 2

(defn min-power
  [game]
  (let [min-cubes (apply merge-with max (:handfuls game))]
    (* (:red min-cubes 0) (:green min-cubes 0) (:blue min-cubes 0))))

(defn part2
  [games]
  (apply + (map min-power games)))

(comment "part 2"
  (part2 (parse ex))
  (part2 (parse (util/slurp-day "day02"))))
