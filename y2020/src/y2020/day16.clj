(ns y2020.day16
  (:require [clojure.edn :as edn]
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
    {:field-restrictions (->> (string/split-lines valid-ranges)
                              (map parse-field-line)
                              (into {})) 
     :my-ticket (->> (lines-without-header my-ticket)
                     (first)
                     (parse-ticket-line))
     :nearby-tickets (->> (lines-without-header nearby-tickets)
                          (mapv parse-ticket-line))}))

(defn all-valid-values
  [field-restrictions]
  (->> field-restrictions
       (vals)
       (apply set/union)))

(defn part1*
  [{:keys [field-restrictions nearby-tickets]}]
  (let [all-values (apply concat nearby-tickets)
        valid-values (all-valid-values field-restrictions)]
    (apply + (remove valid-values all-values))))

(def part1 (util/make-run-fn "day16" parse part1*))

(defn find-valid-fields
  [values field-restrictions]
  (->> field-restrictions
       (filter #(set/subset? (set values) (val %)))
       (map key)
       (set)))

;; TODO: rename this?
(defn matching-fields
  [field-restrictions tickets]
  (for [values (apply mapv vector tickets)]
    (find-valid-fields values field-restrictions)))

(defn simplify-field-possibilities
  [field-possibilities]
  (let [size-1? (fn [coll] (= 1 (count coll)))
        singletons (->> field-possibilities
                        (filter size-1?)
                        (map first)
                        (set))
        simplified-possibilities (map #(if (size-1? %)
                                         %
                                         (set/difference % singletons))
                                      field-possibilities)]
    (if (= simplified-possibilities field-possibilities)
      (do (assert (every? size-1? simplified-possibilities)
                  (str simplified-possibilities " not fully determined"))
          (map first simplified-possibilities))
      (recur simplified-possibilities))))

(defn fields-in-order
  [field-restrictions tickets]
  (-> (matching-fields field-restrictions tickets)
      (simplify-field-possibilities)))

(defn valid-ticket?
  [ticket valid-values]
  (set/subset? (set ticket) valid-values))

(defn part2*
  ([input] (part2* input #"departure.*"))
  ([{:keys [field-restrictions nearby-tickets my-ticket] :as input}
    relevant-field-pattern]
   (let [all-valids (all-valid-values field-restrictions)
         valid-tickets (filter #(set/subset? % all-valids) nearby-tickets)
         field-order (fields-in-order field-restrictions
                                      (conj valid-tickets my-ticket))
         relevant-fields (filter #(re-matches relevant-field-pattern %)
                                 field-order)]
     (-> (map vector field-order my-ticket)
         (->> (into {}))
         (select-keys relevant-fields)
         (vals)
         (->> (apply *))))))

(def part2 (util/make-run-fn "day16" parse part2*))

(comment "run"
  (part1)
  (part2))
