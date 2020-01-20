(ns aoc.day25
  (:require [aoc.util :as util]
            [aoc.intcode-computer :as ic]))

(defn print-only-update-world
  [_ outputs]
  (println (util/ascii outputs)))

(defn human-input
  [_]
  (str (read-line) "\n"))

(defn play-game
  []
  (->> (ic/run-in-world
        (ic/new-computer (util/read-input "day25" ic/parse))
        nil
        print-only-update-world
        human-input)
       :output
       util/ascii
       println))
