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
            (for [x (range (apply min (map first (keys hull)))
                           (inc (apply max (map first (keys hull)))))]
              (display-chars (get hull [x y] 0)))))))
