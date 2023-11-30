(ns y2023.util
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(defn slurp-day
  [s]
  (-> s io/resource slurp))

(defn iterate-until
  [iter pred init]
  (->> (iterate iter init)
       (drop-while #(not (pred %)))
       (first)))
