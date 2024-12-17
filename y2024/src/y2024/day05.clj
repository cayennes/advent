(ns y2024.day05
  (:require [clojure.string :as string]
            [clojure.set :as set]
            [clojure.java.io :as io]))

(def ex1
  "47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47")

(defn parse [input]
  (let [[rule-text update-text] (string/split input #"\n\n")
        rules (->>  (string/split rule-text #"\n")
                    (map #(string/split % #"\|"))
                    (map (partial map #(Integer/parseInt %))))
        updates (->> (string/split update-text  #"\n")
                     (map #(string/split % #","))
                     (map (partial map #(Integer/parseInt %))))]
    {:rules rules :updates updates}))

;; will be going through the update page by page
;; with each one, need to know if there is a violation of the rules
;; specifically we want to know if that page came after something it was supposed to come before
;; so as we go, we want a list of pages that should not appear after pages we have already seen
;; so the useful data structure for the rules is a list of pages that should not appear after a given page

(defn what-must-come-before
  [rules]
  (reduce (fn [m [p1 p2]]
            (assoc m p2 (conj (get m p2 #{}) p1)))
          {}
          rules))

(defn is-ordered-correctly
  [rules pages]
  (let [reqs (what-must-come-before rules)]
    (loop [disallowed #{}
           [current & remaining] pages]
      (cond (disallowed current) false
            (= nil remaining) true
            :else (recur (set/union disallowed (reqs current))
                         remaining)))))

(defn valid-updates
  [{:keys [rules updates]}]
  (filter (partial is-ordered-correctly rules) updates))

(defn middle-page
  [pages]
  (nth pages (/ (count pages) 2)))

(defn part1
  [parsed-input]
  (->> parsed-input
       valid-updates
       (map middle-page)
       (apply +)))

(comment "do things"
  (parse ex1)

  (-> ex1 parse :rules what-must-come-before)

  (check (:rules (parse ex1)) (first (:updates (parse ex1))))

  (valid-updates (parse ex1))

  (middle-page [1 2 3])

  (-> ex1 parse part1) ;; 143

  (-> "day05" io/resource slurp parse part1) ;; 6034

  )
