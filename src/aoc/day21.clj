(ns aoc.day21
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]
            [clojure.string :as string]))

(defn run-springdroid
  [& scriptlines]
  (let [result (-> (ic/new-computer (util/read-input "day21" ic/parse)
                                    (str (string/join "\n" scriptlines) "\n"))
                   (ic/exec-all)
                   (:output))]
    (try
      (println (util/ascii result))
      (catch java.lang.IllegalArgumentException e
        (println (util/ascii (butlast result)))
        (last result)))))

(comment
    ;; just walk
    (run-springdroid "WALK")
  ;; jump in hole
  (run-springdroid "NOT D J"
                   "WALK")
  ;; jump over immediate gap
  (run-springdroid "NOT A J"
                   "WALK")
  ;; jump all the time
  (run-springdroid "NOT T J"
                   "WALK")
  ;; jump when safe
  (run-springdroid "AND T J"
                   "OR D J"
                   "WALK")
  ;; check persistent state - turns out there isn't any
  (run-springdroid "OR T J"
                   "NOT T T"
                   "WALK"))


(defn part1
  []
  ;; jump visible holes when safe
  ;; (and d (or (not a) (not b) (not c)))
  (run-springdroid "OR A T"
                   "AND B T"
                   "AND C T"
                   "NOT T T"
                   "OR D J"
                   "AND T J"
                   "WALK"))

