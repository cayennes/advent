^{:nextjournal.clerk/visibility {:code :fold}}
(ns y2022.day21
  (:require [nextjournal.clerk :as clerk]
            [clojure.java.io :as io]
            [clojure.string :as string]))

;; # Day 21: Monkey Math
;;
;; ## Part One
;; 
;; The monkeys are back! You're worried they're going to try to steal your stuff again, but it seems like they're just holding their ground and making various monkey noises at you.
;; 
;; Eventually, one of the elephants realizes you don't speak monkey and comes over to interpret. As it turns out, they overheard you talking about trying to find the grove; they can show you a shortcut if you answer their riddle.
;; 
;; Each monkey is given a job: either to yell a specific number or to yell the result of a math operation. All of the number-yelling monkeys know their number from the start; however, the math operation monkeys need to wait for two other monkeys to yell a number, and those two other monkeys might also be waiting on other monkeys.
;; 
;; Your job is to work out the number the monkey named root will yell before the monkeys figure it out themselves.
;; 
;; For example:
;; 
;;     root: pppw + sjmn
;;     dbpl: 5
;;     cczh: sllz + lgvd
;;     zczc: 2
;;     ptdq: humn - dvpt
;;     dvpt: 3
;;     lfqf: 4
;;     humn: 5
;;     ljgn: 2
;;     sjmn: drzm * dbpl
;;     sllz: 4
;;     pppw: cczh / lfqf
;;     lgvd: ljgn * ptdq
;;     drzm: hmdt - zczc
;;     hmdt: 32
;; 
;; Each line contains the name of a monkey, a colon, and then the job of that monkey:
;; 
;; * A lone number means the monkey's job is simply to yell that number.
;; * A job like aaaa + bbbb means the monkey waits for monkeys aaaa and bbbb to yell each of their numbers; the monkey then yells the sum of those two numbers.
;; * aaaa - bbbb means the monkey yells aaaa's number minus bbbb's number.
;; * Job aaaa * bbbb will yell aaaa's number multiplied by bbbb's number.
;; * Job aaaa / bbbb will yell aaaa's number divided by bbbb's number.
;; 
;; So, in the above example, monkey drzm has to wait for monkeys hmdt and zczc to yell their numbers. Fortunately, both hmdt and zczc have jobs that involve simply yelling a single number, so they do this immediately: 32 and 2. Monkey drzm can then yell its number by finding 32 minus 2: 30.
;; 
;; Then, monkey sjmn has one of its numbers (30, from monkey drzm), and already has its other number, 5, from dbpl. This allows it to yell its own number by finding 30 multiplied by 5: 150.
;; 
;; This process continues until root yells a number: **152**.
;; 
;; However, your actual situation involves considerably more monkeys. What number will the monkey named root yell?

;; example:
^{::clerk/visibility {:code :fold}}
(def example "root: pppw + sjmn
dbpl: 5
cczh: sllz + lgvd
zczc: 2
ptdq: humn - dvpt
dvpt: 3
lfqf: 4
humn: 5
ljgn: 2
sjmn: drzm * dbpl
sllz: 4
pppw: cczh / lfqf
lgvd: ljgn * ptdq
drzm: hmdt - zczc
hmdt: 32")

;; input:
^{::clerk/visibility {:code :fold}}
(def input
  (slurp (io/resource "day21")))

(defn parse
  [input]
  (reduce (fn [jobs line]
            (let [[monkey output] (string/split line #": ")]
              (assoc jobs (keyword monkey)
                     (if (re-matches #"\d+" output)
                       (Integer/parseInt output)
                       (let [[operand1 operator operand2] (string/split output #" ")]
                         {:operator (load-string operator)
                          :operands [(keyword operand1) (keyword operand2)]})))))
          {}
          (string/split input #"\n")))

(def parsed-example (parse example))

(def parsed-input (parse input))

(defn calculate
  ([jobs number-or-monkey]
   (if (number? number-or-monkey)
     number-or-monkey
     (let [job (jobs number-or-monkey)]
       (if (map? job)
         (apply (:operator job) (map #(calculate jobs %) (:operands job)))
         job))))
  ([jobs] (calculate jobs :root)))

(clerk/table
 (map #(vector % (parsed-example %) (calculate parsed-example %)) (keys parsed-example)))

(def example-answer-1
  (calculate parsed-example))

(def answer-1
  (calculate parsed-input))

(= 158661812617812 answer-1)

;; ## Part Two
;; 
;; Due to some kind of monkey-elephant-human mistranslation, you seem to have misunderstood a few key details about the riddle.
;; 
;; First, you got the wrong job for the monkey named root; specifically, you got the wrong math operation. The correct operation for monkey root should be =, which means that it still listens for two numbers (from the same two monkeys as before), but now checks that the two numbers match.
;; 
;; Second, you got the wrong monkey for the job starting with humn:. It isn't a monkey - it's you. Actually, you got the job wrong, too: you need to figure out what number you need to yell so that root's equality check passes. (The number that appears after humn: in your input is now irrelevant.)
;; 
;; In the above example, the number you need to yell to pass root's equality test is 301. (This causes root to get the same number, 150, from both of its monkeys.)
;; 
;; What number do you yell to pass root's equality test?

;; ### First, process the jobs map so that all things that can be calculated 

(defn replace-known-monkeys
  "replace operands whose monkeys shout numbers with those numbers"
  [jobs]
  (into {}
        (for [[monkey job] jobs]
          [monkey (cond
                    (not (map? job))
                    job

                    ;; if the monkey has two numbers to work with, do the operation
                    (and (:operator job) (every? number? (:operands job)))
                    (apply (:operator job) (:operands job))

                    ;; job the operands are monkey names for monkeys who have numbers, replace them with those
                    :else
                    (update job :operands
                            (fn [operands]
                              (map #(if (number? (jobs %)) (jobs %) %)
                                   operands))))])))

;; and do the opposite process in order to fill in things that must be known based on what the results are

(def example-single-replacement
  (replace-known-monkeys parsed-example))

(defn first-consecutive-duplicate
  [s]
  (->> s
       (partition 2 1) ;; sequence of adjacent pairs
       (filter #(apply = %))
       (first)
       (first)))

(= :d (first-consecutive-duplicate [:c :a :d :d :e :b :b]))

(defn iterate-until-unchanged
  [f x]
  (first-consecutive-duplicate (iterate f x)))

(= 4 (iterate-until-unchanged #(min (inc %) 4) 0))

(defn replace-all-possible
  [jobs]
  (iterate-until-unchanged replace-known-monkeys
                           (dissoc jobs :humn :root)))

(def example-simplified
  (replace-all-possible parsed-example))

(def simplified
  (replace-all-possible parsed-input))

;; ## then make a structure with the inverse of everything so we can calculate the one we need

(defn inverse-instructions
  [simplified-jobs]
  (into {}
        (for [[monkey job] simplified-jobs
              :when (not (number? job))
              :let [operand-monkey (->> job (:operands) (filter keyword?) (first))
                    {:keys [operator operands]} job]]
          [operand-monkey
           (cond
             (keyword? (first operands))
             ;; monkey = operand-monkey op operand-number
             ;; => operand-monkey = monkey inv-op operand-number
             {:operator ({+ -, - +, * /,/ *} (:operator job))
              :operands [monkey (second operands)]}

             (#{+ *} operator)
             ;; m = n + o => o = m - n
             ;; m = n * o => o = m / n
             {:operator ({+ -, * /} operator)
              :operands [monkey (first operands)]}

             :else
             ;; m = n - o => o = n - m
             ;; m = n / o => o = n / m
             {:operator operator
              :operands [(first operands) monkey]})])))

(def example-inverse-simplified
  (inverse-instructions example-simplified))

(def inverse-simplified
  (inverse-instructions simplified))

;; ## put in what we know we need



(->> example-simplified
     (filter (fn [[monkey instructions]]
               (and ((set (-> parsed-example :root :operands)) monkey)
                    (number? instructions))))
     (first))

(-> (set (-> parsed-example :root :operands))
    (disj :sjmn))


(defn solve
  [jobs simplified inverse]
  ;; find the two monkeys that are equal and set the unknown to the known value
  (let [equal-monkeys (set (-> jobs :root :operands))
        [known-name known-value] (->> simplified
                                      (filter (fn [[monkey instructions]]
                                                (and (equal-monkeys monkey)
                                                     (number? instructions))))
                                      (first))
        unknown-name (-> equal-monkeys
                         (disj known-name)
                         (first))]
    (-> inverse
        (assoc unknown-name known-value)
        (calculate :humn))))

(def example-answer-b
  (solve parsed-example example-simplified example-inverse-simplified))

(def answer-b
  (solve parsed-input simplified inverse-simplified))
