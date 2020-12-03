(ns y2020.util
  (:require [clojure.java.io :as io]))

(defn input-lines*
  ([filename line-parse-fn]
   (with-open [rdr (-> filename io/resource io/reader)]
     (mapv line-parse-fn (line-seq rdr))))
  ([filename]
   (input-lines* identity)))

(def input-lines (memoize input-lines*))
