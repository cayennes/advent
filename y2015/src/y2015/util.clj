(ns y2015.util
  (:require [clojure.java.io :as io]))

(defn read-input*
  [filename parse-fn]
  (-> filename io/resource slurp parse-fn))

(def read-input (memoize read-input*))

(defn vec+
  [& vs]
  (apply mapv + vs))
