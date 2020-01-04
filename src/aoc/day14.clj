(ns aoc.day14
  (:require [aoc.util :as util]
            [clojure.math.numeric-tower :as nt]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.string :as string]))

(defn parse-line
  [reaction-string]
  (let [[product & ingredients] (->> reaction-string
                                     (re-seq #"(\d+) ([A-Z]+)")
                                     (map rest)
                                     (reverse)
                                     (map #(mapv edn/read-string %))
                                     (map #(update % 1 keyword)))]
    [(second product) {:yield (first product)
                       :ingredients (zipmap (map second ingredients)
                                            (map first ingredients))}]))

(defn parse
  "parses reaction input to look like

  {:AAA {:yield 1 :ingredients {:BBB 2 :CCC 1}} ...} "
  [input]
  (->> (string/split input #"\n")
       (map parse-line)
       (into {})))

(defn decrease-in
  [m k n]
  (cond
    (= n (m k)) (dissoc m k)
    (< n (m k)) (update m k - n)))

;; input and output of these functions looks like
;; {:needed {:AAA 6 :BBB 2 ...} :spares {:CCC 2 ...}}

(defn take-from-spares
  [{:keys [needed spares] :as plan} chemical]
  (if-let [available (spares chemical)]
    (let [amount (min (needed chemical) available)]
      {:spares (decrease-in spares chemical amount)
       :needed (decrease-in needed chemical amount)})
    plan))

(defn multiply-vals
  [n m]
  (into (empty m)
        (for [[k v] m]
          [k (* n v)])))

(defn create-with-reaction
  [{:keys [needed spares] :as plan} chemical reaction]
  (let [reactions-needed (nt/ceil (/ (- (needed chemical 0)
                                        (spares chemical 0))
                                     (:yield reaction)))]
    (if (pos? reactions-needed)
      {:needed (merge-with +
                           (multiply-vals reactions-needed
                                          (:ingredients reaction))
                           needed)
       :spares (merge-with + spares {chemical (* reactions-needed
                                                 (:yield reaction))})}
      plan)))

(defn plan-all
  [reactions reagents]
  (loop [plan {:needed {:FUEL 1} :spares {}}]
    (if-let [chemical (->> plan :needed keys (filter #(not (reagents %))) first)]
      (-> plan
          (take-from-spares chemical)
          (create-with-reaction chemical (reactions chemical))
          (recur))
      plan)))

(defn part1
  []
  (-> (util/read-input "day14" parse)
      (plan-all #{:ORE})
      (get-in [:needed :ORE])))

;; TODO:
;; [x] make part1 a little more efficient by multiplying
;; [x] change plan-all to take a set of things it can use. these would be the things in supply afterwards
;; [ ] make a function that checks what items there aren't enough of to apply the result of plan-all
;; [ ] make a function that can apply the output of plan-all as many times as possible with a given supply - should do it multiplication instead of iteration
;; [ ] iterate by making as much fuel as possible with the plan-all for the given supply, then plan-all for subtracting the limiting reagents

(defn part2
  []
  (maximize-fuel (util/read-input "day14" parse) 1000000000000))
