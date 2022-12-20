(ns y2021.day10
  (:require [clojure.java.io :as io]))

(defn parse
  [input]
  ())

(def delim-matches {\( \), \[ \], \{ \}})

(defn validate
  [s]
  (reduce (fn [stack c]
            (cond 
              ((set (keys delim-matches)) c) (cons c stack)
              (= (delim-matches c) (first stack)) (rest stack)
              :else (reduced {:invalid-character c})))
          '()
          s))
