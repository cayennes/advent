(ns aoc.util
  (:require [clojure.java.io :as io]))

(defn abs [n] (max n (- n)))

(defn read-input*
  [filename parse-fn]
  (-> filename io/resource slurp parse-fn))

(def read-input (memoize read-input*))
