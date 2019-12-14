(ns aoc.day14
  (:require [aoc.util :as util]
            [clojure.edn :as edn]
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

(defn create-with-reaction
  [{:keys [needed spares] :as plan} chemical reaction]
  (if (needed chemical)
    {:needed (merge-with + (:ingredients reaction) needed)
     :spares (merge-with + spares {chemical (:yield reaction)})}
    plan))

(defn plan-all
  [reactions]
  (loop [plan {:needed {:FUEL 1} :spares {}}]
    (if (= [:ORE] (keys (:needed plan)))
      (get-in plan [:needed :ORE])
      (let [chemical (->> plan :needed keys (filter #(not= % :ORE)) first)]
         (-> plan
             (take-from-spares chemical)
             (create-with-reaction chemical (reactions chemical))
             (recur))))))

(defn part1
  []
  (plan-all (util/read-input "day14" parse)))

