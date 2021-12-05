(ns y2021.day04
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [y2021.util :as util]))

(defn parse
  [input]
  (let [[numbers & boards] (map #(edn/read-string (str "[" % "]"))
                                (util/split-on-blanks input))]
    {:boards boards
     :numbers numbers}))

(defn mark-num
  [board n]
  (map #(if-not (= n %) %) board))

(defn win?
  [board]
  (let [rows (partition 5 board)
        cols (apply map vector rows)]
    (boolean (some (partial = (repeat 5 nil))
                   (concat rows cols)))))

(defn total-uncalled
  [board]
  (apply + (filter some? board)))

(defn turn
  [{:keys [boards numbers winning-score]}]
  (prn boards)
  (let [[number & new-numbers] numbers
        updated-boards (map #(mark-num % number) boards)
        winning-board (some #(if (win? %) %) updated-boards)]
    {:boards (remove win? updated-boards)
     :numbers new-numbers
     :winning-score (if winning-board
                      (* number (total-uncalled winning-board))
                      winning-score)}))

(defn part1*
  [input]
  (:winning-score (util/iterate-until turn :winning-score input)))

(defn part1
  ([input] (-> input parse part1*))
  ([] (-> "day04" io/resource slurp part1)))

(defn part2*
  [input]
  (:winning-score (util/iterate-until turn #(empty? (:numbers %)) input)))

(defn part2
  ([input] (-> input parse part2*))
  ([] (-> "day04" io/resource slurp part2)))
