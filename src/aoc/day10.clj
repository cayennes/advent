(ns aoc.day10
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.test :refer [is]]))

(defn list-locations
  [diagram]
  (filter some?
          (map-indexed
           (fn [y row]
             (map-indexed
              (fn [x ch]
                (if (= \# ch)
                  [x y]))
              row))
           (string/split #"\n" diagram))))

(defn asteroid-angles
  [asteroids [base-x base-y]]
  (-> asteroids
      (map (fn [[asteroid-x asteroid-y]]
             (/ (- asteroid-x base-x)
                (- asteroid-y base-y))))
      (distinct)
      (count)))
