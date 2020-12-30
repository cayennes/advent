(ns y2020.util
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

;; reading input

(defmulti read type)

(defmethod read java.net.URL
  [input-resource]
  (slurp input-resource))

(defmethod read java.lang.String
  [input-string]
  (string/replace input-string #"(?m)^ *" ""))

(defn split-on-blanks
  [input]
  (string/split input #"\n\n"))

;; running

(defn make-run-fn
  [resource-name parse-fn main-fn]
  (let [real-input-resource (io/resource resource-name)
        f #(-> % read parse-fn main-fn)]
    (fn
      ([] (f real-input-resource))
      ([test-input] (f test-input)))))

;; useful functions

(defn count-satisfying
  [s pred]
  (->> s
       (filter pred)
       (count)))

(defn iterate-until
  [iter pred init]
  (->> (iterate iter init)
       (drop-while #(not (pred %)))
       (first)))
