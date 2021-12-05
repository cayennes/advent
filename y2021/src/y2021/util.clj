(ns y2021.util
  (:require [clojure.string :as string]))

(defn iterate-until
  [iter pred init]
  (->> (iterate iter init)
       (drop-while #(not (pred %)))
       (first)))

(defn split-on-blanks
  [input]
  (string/split input #"\n\n"))
