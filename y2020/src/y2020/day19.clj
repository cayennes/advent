(ns y2020.day19
  (:require [clojure.string :as string]
            [y2020.util :as util]))

(defn parse-rule
  [input]
  (let [[id rule-string] (string/split input #": ")]
    [(str "{" id "}")
     (-> rule-string
         (string/replace #" *(\d+) *" (fn [[_ d]] (str "{" d "}")))
         (string/replace "\"" "")
         (#(str "(" % ")")))]))

(defn parse
  [input]
  (let [[rules messages] (util/split-on-blanks input)]
    {:rules (into {} (map parse-rule (string/split-lines rules)))
     :messages (string/split-lines messages)}))

(defn processed?
  [rule]
  (re-matches #"\D+" rule))

(defn process-rules
  [{:keys [rules-to-process processed-rules]}]
  (let [maybe-processed-rules
        (for [[id rule] rules-to-process]
          [id (string/replace rule #"\{\d+\}" #(or (processed-rules %) %))])]
    {:rules-to-process (into {}
                             (remove #(-> % second processed?)
                                     maybe-processed-rules))
     :processed-rules (into processed-rules
                            (filter #(-> % second processed?)
                                    maybe-processed-rules))}))

(defn rule-0-regex
  [rules]
  (-> (util/iterate-until process-rules
                          #(contains? (:processed-rules %) "{0}")
                          {:rules-to-process rules
                           :processed-rules {}})
      (get-in [:processed-rules "{0}"])
      (re-pattern)))

(defn part1*
  [{:keys [rules messages]}]
  (util/count-satisfying messages
                         #(re-matches (rule-0-regex rules) %)))

(def part1 (util/make-run-fn "day19" parse part1*))

(comment "run"
  (part1)
  (part2))
