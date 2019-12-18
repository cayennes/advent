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

(defn digits->number
  [digits]
  (reduce #(+ (* 10 %1) %2) digits))

(defn adjacent-positions
  [position]
  (mapv #(mapv + % position) [[0 -1] [0 1] [-1 0] [1 0]]))

(defn string->position-map
  ([diagram-string]
   (string->position-map diagram-string nil))
  ([diagram-string filler-char]
   (into {}
         (for [[y row] (map-indexed vector (string/split diagram-string #"\n"))
               [x ch] (map-indexed vector row)
               :when (not= filler-char ch)]
           [[x y] (str ch)]))))

(defn despace
  [s]
  (string/replace s " " ""))

;; robot etc. moving

;;        [0 -1]
;; [-1 0] [0  0] [1 0]
;;        [0  1]

(defn rotate-coord
  [[x y] side]
  (case side
    :left [y (- x)]
    :right [(- y) x]))

(defn turn
  [robot side]
  (update robot :orientation rotate-coord side))

(defn move-forward
  [{:keys [orientation] :as robot} n]
  (update robot :position #(mapv + % (mapv * orientation [n n]))))
