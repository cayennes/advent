(ns y2020.day18
  (:require [clojure.string :as string]
            [clojure.edn :as edn]
            [y2020.util :as util]))

(defn basic-compute-step
  [expr]
  (-> expr
      (string/replace #"(\(|^)(\d+) ([\+\*]) (\d+)"
                      (fn [[_ start n1 op n2]]
                        (str start ((case op "+" +, "*" *)
                                    (edn/read-string n1)
                                    (edn/read-string n2)))))
      (string/replace #"\((\d+)\)" "$1")))

(defn basic-compute [expr]
  (if (re-matches #"\d+" expr)
    (edn/read-string expr)
    (recur (basic-compute-step expr))))

(defn part1*
  [input]
  (apply + (map basic-compute input)))

(def part1 (util/make-run-fn "day18" string/split-lines part1*))

(defn operate-on-found-numbers
  [expr regex op]
  (string/replace expr
                  regex
                  (fn [region]
                    (let [numbers (re-seq #"\d+" region)]
                      (->> numbers
                           (map edn/read-string)
                           (apply op)
                           (str))))))

(defn add-available
  [expr]
  (-> expr
      (operate-on-found-numbers #"\d+(?: \+ \d+)+" +)
      (string/replace #"\((\d+)\)" "$1")))

(defn multiply-available
  [expr]
  (operate-on-found-numbers expr #"\(\d+(?: \* \d+)+\)" *))

(defn advanced-compute-step
  [expr]
  (-> expr add-available multiply-available))

(defn advanced-compute [expr]
  (cond
    (re-matches #"\d+" expr) (edn/read-string expr)
    (re-matches #"\d+.+" expr) (advanced-compute (str "(" expr ")"))
    :else (recur (advanced-compute-step expr))))

(defn part2*
  [input]
  (apply + (map advanced-compute input)))

(def part2 (util/make-run-fn "day18" string/split-lines part2*))

(comment "run"
  (part1)
  (part2))
