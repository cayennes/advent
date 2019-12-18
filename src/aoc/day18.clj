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
   :keyring #{}})

(defn expand-searcher
  [searcher area]
  (for [next-position (util/adjacent-positions (:position searcher))
        :let [mark (area next-position)]
        :when (or (= "." mark)
                  ((:keyring searcher) (if mark (string/lower-case mark))))]
    (-> searcher
        (assoc :position next-position)
        (update :keyring #(if (key? mark) (conj % mark) %)))))

(defn step-searchers
  [{:keys [searchers area] :as world}]
  (assoc world :searchers (mapcat #(expand-searcher % area) searchers)))

(defn done-fn
  [area]
  (let [key-count (count-keys area)]
    (fn [{:keys [searchers]}]
      (some #(= key-count (count (:keychain %))) searchers))))

(defn min-steps
  [area]
  (second (util/counting-iterate-until
           step-searchers
           (done-fn area)
           (initial-searcher area))))

(defn part1
  []
  (min-steps (util/read-input "day18" util/string->position-map)))
