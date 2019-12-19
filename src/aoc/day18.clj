(ns aoc.day18
  (:require [aoc.util :as util]
            [clojure.string :as string]))

(defn key?
  [mark]
  (boolean (re-matches #"[a-z]" mark)))

(defn count-keys
  [area]
  (->> area
       (vals)
       (filter key?)
       (count)))

(defn initial-searcher
  [area]
  {:position (->> area (filter #(= "@" (second %))) (first) (first))
   :keyring #{}
   :visited #{}})

(defn expand-searcher
  [searcher area]
  (distinct
   (for [next-position (util/adjacent-positions (:position searcher))
         :let [mark (area next-position)]
         :when (and (not ((:visited searcher) next-position))
                    (or (#{"." "@"} mark)
                        (key? mark)
                        ((:keyring searcher) (if mark (string/lower-case mark)))))]
     (if (key? mark)
       (-> searcher
           (assoc :position next-position)
           (update :keyring #(conj % mark))
           (assoc :visited #{}))
       (-> searcher
           (assoc :position next-position)
           (update :visited #(conj % (:position searcher))))))))

(defn step-searchers
  [{:keys [searchers area] :as world}]
  (assoc world :searchers (vec (mapcat #(expand-searcher % area) searchers))))

(defn done-fn
  [area]
  (let [key-count (count-keys area)]
    (fn [{:keys [searchers]}]
      (or (some #(= key-count (count (:keyring %))) searchers)
          (empty? searchers)))))

(defn min-steps
  [area]
  (second (util/counting-iterate-until
           step-searchers
           (done-fn area)
           {:searchers [(initial-searcher area)]
            :area area})))

;; this is too slow and eventually runs out of memory. a better approach:

;; first, use searchers from the center never backtrack and store their whole
;; path. collect them as they get stuck in different corners, and keep the one
;; that got there the quickest (some will loop around the center first) and
;; store their whole path. collect them as they get stuck in different corners,
;; and keep the one that got there the quickest (some will loop around the
;; center first)

;; use that to create a list of all possible orders to pick up keys, given door
;; locations

;; also use it to (once each) calculate the length between each pair of keys
;; (ignoing doors), using an algorithm like the orbits, with some special casing
;; within one of the origin

;; then go through them to the shortest path

(defn part1
  []
  (min-steps (util/read-input "day18" util/string->position-map)))
