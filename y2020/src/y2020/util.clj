(ns y2020.util
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn string-lines
  "for use with test input"
  [input-string line-parse-fn]
  (->> (string/split input-string #"\n")
       (mapv string/trim)
       (mapv line-parse-fn)))

(defn input-lines*
  ([filename line-parse-fn]
   (with-open [rdr (-> filename io/resource io/reader)]
     (mapv line-parse-fn (line-seq rdr))))
  ([filename]
   (input-lines* identity)))

(def input-lines (memoize input-lines*))

(defn input*
  [filename parse-fn]
  (-> filename io/resource io/reader slurp parse-fn))

(def input (memoize input*))

(defn trim-lines
  [s]
  (string/replace s #"^ +" ""))

(defn count-satisfying
  [s pred]
  (->> s
       (filter pred)
       (count)))
