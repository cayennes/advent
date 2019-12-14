(ns aoc.day14
  (:require [aoc.util :as util]
            [clojure.edn :as edn]
            [clojure.string :as string]))

(defn parse-line
  [reaction-string]
  (let [[product & reagents] (->> reaction-string
                                  (re-seq #"(\d+) ([A-Z]+)")
                                  (map rest)
                                  (reverse)
                                  (map #(mapv edn/read-string %))
                                  (map #(update % 1 keyword)))]
    [(first product) {:yield (second product)
                      :reagents (into {} reagents)}]))

(defn parse
  "parses reaction input to look like

  {:AAA {:yield 1 :reagents {:BBB 2 :CCC 1}} ...} "
  [input]
  (-> input
      (string/split #"\n")
      (->> (map parse-line))
      (into {})))

(defn done?
  [{:keys [needed]}]
  (= [:ORE] (keys needed)))

(defn plan-step
  "replace something in needed with its ingredients, keeping track of spares

  input and output looks like
  {:needed {:AAA 6 :BBB 2 ...} :inventory {:CCC 2 ...}} "
  [{:keys [needed inventory]} reactions]
  (let [chemical (first (filter #(not= :ORE %) (keys needed)))
        {:keys [yield reagents]} (reaction chemical)]

    ))

(defn part1
  []
  (util/read-input "day14" parse))
