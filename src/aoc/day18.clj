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

;; this is too slow and eventually runs out of memory. a better approach would
;; involve building a tree from the center and using an algorithm similar to the
;; orbits.

(defn part1
  []
  (min-steps (util/read-input "day18" util/string->position-map)))
