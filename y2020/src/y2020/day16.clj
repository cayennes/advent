(ns y2020.day16
  (:require [clojure.edn :as edn]
            [clojure.math.combinatorics :as combo]
            [clojure.set :as set]
            [clojure.string :as string]
            [y2020.util :as util]))

(defn parse-field-line
  [line]
  (let [[field & numbers] (string/split line #": |-| or ")
        [a-min a-max b-min b-max] (mapv edn/read-string numbers)]
    [field (set/union (set (range a-min (inc a-max)))
                      (set (range b-min (inc b-max))))]))

(defn lines-without-header
  [section]
  (rest (string/split-lines section)))

(defn parse-ticket-line
  [line]
  (->> (string/split line #",")
       (mapv edn/read-string)))

(defn parse
  [input]
  (let [[valid-ranges my-ticket nearby-tickets] (util/split-on-blanks input)]
    ;; TODO: rename valid-values
    {:valid-values (->> (string/split-lines valid-ranges)
                        (map parse-field-line)
                        (into {})) 
     :my-ticket (->> (lines-without-header my-ticket)
                     (first)
                     (parse-ticket-line))
     :nearby-tickets (->> (lines-without-header nearby-tickets)
                          (mapv parse-ticket-line))}))

(defn all-valid-values
  [valid-values]
  (->> valid-values
       (vals)
       (apply set/union)))

(defn part1*
  [{:keys [valid-values nearby-tickets]}]
  (let [all-values (apply concat nearby-tickets)
        valid-values (all-valid-values valid-values)]
    (apply + (remove valid-values all-values))))

(def part1 (util/make-run-fn "day16" parse part1*))

(defn values-for-fields
  [tickets]
  (apply mapv vector tickets))

(defn find-valid-fields
  [values fields]
  (->> fields
       (filter #(set/subset? (set values) (val %)))
       (map key)
       (set)))

(defn local-possibilities
  [{:keys [valid-values nearby-tickets my-ticket]}]
  (for [values (values-for-fields (conj nearby-tickets my-ticket))]
    (find-valid-fields values valid-values)))

(defn fields-in-order
  [input]
  (->> (apply combo/cartesian-product (local-possibilities input))
       (filter #(apply distinct? %))
       (first)))

(defn valid-ticket?
  [ticket valid-values]
  (set/subset? (set ticket) valid-values))

(defn part2*
  [{:keys [valid-values nearby-tickets my-ticket] :as input}]
  (let [all-valids (all-valid-values valid-values)
        valid-tickets (filter #(set/subset? % valid-values) nearby-tickets)
        ;; TODO: fields-in-order should take a different signature
        field-order (fields-in-order (assoc input :nearby-tickets valid-tickets))]
    field-order))

(def part2 (util/make-run-fn "day16" parse part2*))

(comment "run"
  (part1)
  (part2))
