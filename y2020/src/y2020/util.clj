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
