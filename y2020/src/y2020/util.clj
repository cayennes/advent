(ns y2020.util
  (:require [clojure.java.io :as io]))

(defn read-input*
  [filename parse-fn]
  (-> filename io/resource slurp parse-fn))

(def read-input (memoize read-input*))
