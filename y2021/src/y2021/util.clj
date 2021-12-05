(ns y2021.util)

(defn iterate-until
  [iter pred init]
  (->> (iterate iter init)
       (drop-while #(not (pred %)))
       (first)))
