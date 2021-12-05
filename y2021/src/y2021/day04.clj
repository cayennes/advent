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
  [{:keys [boards numbers]}]
  (let [[number & new-numbers] numbers
        new-boards (map #(mark-num % number) boards)
        winning-board (some #(if (win? %) %) new-boards)]
    (merge {:boards new-boards
            :numbers new-numbers}
           (if winning-board
             {:winning-score (* number (total-uncalled winning-board))}))))

(defn part1*
  [input]
  (:winning-score (util/iterate-until turn :winning-score input)))

(defn part1
  ([input] (-> input parse part1*))
  ([] (-> "day04" io/resource slurp part1)))
