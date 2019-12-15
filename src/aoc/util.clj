(ns aoc.util
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn abs [n] (max n (- n)))

(defn read-input*
  [filename parse-fn]
  (-> filename io/resource slurp parse-fn))

(def read-input (memoize read-input*))

(defn show-location-map
  [locations display-chars]
  (string/join
   "\n"
   (for [y (range (apply min (map second (keys locations)))
                  (inc (apply max (map second (keys locations)))))]
     (apply str
            (for [x (range (apply min (map first (keys locations)))
                           (inc (apply max (map first (keys locations)))))]
              (display-chars (locations [x y] 0) "?"))))))

(defn distinct-by
  [f s]
  (vals (zipmap (map f s) s)))

(defn iterate-until
  [f pred x]
  (->> (iterate f x)
       (drop-while #(not (pred %)))
       (first)))

(defn counting-iterate-until
  [f pred x]
  (->> (iterate (fn [[y i]] [(f y) (inc i)]) [x 0])
       (drop-while (fn [[y _]] (not (pred y))))
       (first)))
