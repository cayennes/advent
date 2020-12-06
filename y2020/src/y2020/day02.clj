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

(defn parse
  [input]
  (mapv parse-line (string/split-lines input)))

(defn valid-part1-password?
  [{:keys [n1 n2 character password]}]
  (<= n1
      (-> (group-by identity password)
          (get character)
          (count))
      n2))

(defn part1*
  [input]
  (util/count-satisfying input valid-part1-password?))

(def part1 (util/make-run-fn "day02" parse part1*))

(defn valid-part2-password?
  [{:keys [n1 n2 character password]}]
  (= #{true false}
     (set [(= character (get password (dec n1)))
           (= character (get password (dec n2)))])))

(defn part2*
  [input]
  (util/count-satisfying input valid-part2-password?))

(def part2 (util/make-run-fn "day02" parse part2*))

(comment "run"
  (part1)
  (part2))
