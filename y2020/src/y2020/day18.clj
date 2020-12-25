(ns y2020.day18
  (:require [clojure.string :as string]
            [clojure.edn :as edn]
            [y2020.util :as util]))

(defn compute-step
  [expr]
  (-> expr
      (string/replace #"(\(|^)(\d+) ([\+\*]) (\d+)"
                      (fn [[_ start n1 op n2]]
                        (str start ((case op "+" +, "*" *)
                                    (edn/read-string n1)
                                    (edn/read-string n2)))))
      (string/replace #"\((\d+)\)" "$1")))

(defn compute
  [expr]
  (if (re-matches #"\d+" expr)
    (edn/read-string expr)
    (recur (compute-step expr))))

(defn part1*
  [input]
  (apply + (map compute input)))

(def part1 (util/make-run-fn "day18" string/split-lines part1*))

#_(def part2 (util/make-run-fn "day18" string/split-lines part2*))

(comment "run"
(part1)
(part2))
