(ns y2020.day02
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [y2020.util :as util]))

(defn parse-line
  [s]
  (let [[_ n1 n2 character password] (re-matches #"(\d+)-(\d+) (.): (.*)" s)]
    {:n1 (Integer. n1)
     :n2 (Integer. n2)
     :character (first character)
     :password (vec password)}))

(defn valid-part1-password?
  [{:keys [n1 n2 character password]}]
  (<= n1
      (-> (group-by identity password)
          (get character)
          (count))
      n2))

(defn part1
  [input]
  (util/count-satisfying input valid-part1-password?))

(defn valid-part2-password?
  [{:keys [n1 n2 character password]}]
  (= #{true false}
     (set [(= character (get password (dec n1)))
           (= character (get password (dec n2)))])))

(defn part2
  [input]
  (util/count-satisfying input valid-part2-password?))

(comment "run"
  (part1 (util/input-lines "day02" parse-line))
  (part2 (util/input-lines "day02" parse-line)))
