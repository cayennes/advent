^{:nextjournal.clerk/visibility {:code :fold}}
(ns y2022.day05
  (:require [nextjournal.clerk :as clerk]
            [clojure.java.io :as io]
            [clojure.string :as string]))

;; # Day 5: Supply Stacks
;;
;; ## Part One
;; 
;; The expedition can depart as soon as the final supplies have been unloaded from the ships. Supplies are stored in stacks of marked crates, but because the needed supplies are buried under many other crates, the crates need to be rearranged.
;; 
;; The ship has a giant cargo crane capable of moving crates between stacks. To ensure none of the crates get crushed or fall over, the crane operator will rearrange them in a series of carefully-planned steps. After the crates are rearranged, the desired crates will be at the top of each stack.
;; 
;; The Elves don't want to interrupt the crane operator during this delicate procedure, but they forgot to ask her which crate will end up where, and they want to be ready to unload them as soon as possible so they can embark.
;; 
;; They do, however, have a drawing of the starting stacks of crates and the rearrangement procedure (your puzzle input). For example:
;; 
;;         [D]    
;;     [N] [C]    
;;     [Z] [M] [P]
;;     1   2   3 
;; 
;;     move 1 from 2 to 1
;;     move 3 from 1 to 3
;;     move 2 from 2 to 1
;;     move 1 from 1 to 2
;; 
;; In this example, there are three stacks of crates. Stack 1 contains two crates: crate Z is on the bottom, and crate N is on top. Stack 2 contains three crates; from bottom to top, they are crates M, C, and D. Finally, stack 3 contains a single crate, P.
;; 
;; Then, the rearrangement procedure is given. In each step of the procedure, a quantity of crates is moved from one stack to a different stack. In the first step of the above rearrangement procedure, one crate is moved from stack 2 to stack 1, resulting in this configuration:
;; 
;;     [D]        
;;     [N] [C]    
;;     [Z] [M] [P]
;;     1   2   3 
;; 
;; In the second step, three crates are moved from stack 1 to stack 3. Crates are moved one at a time, so the first crate to be moved (D) ends up below the second and third crates:
;; 
;;     [Z]
;;     [N]
;;     [C] [D]
;;     [M] [P]
;;     1   2   3
;; 
;; Then, both crates are moved from stack 2 to stack 1. Again, because crates are moved one at a time, crate C ends up below crate M:
;; 
;;     [Z]
;;     [N]
;;     [M]     [D]
;;     [C]     [P]
;;     1   2   3
;; 
;; Finally, one crate is moved from stack 1 to stack 2:
;; 
;;     [Z]
;;     [N]
;;     [D]
;;     [C] [M] [P]
;;     1   2   3
;; 
;; The Elves just need to know which crate will end up on top of each stack; in this example, the top crates are C in stack 1, M in stack 2, and Z in stack 3, so you should combine these together and give the Elves the message CMZ.
;; 
;; After the rearrangement procedure completes, what crate ends up on top of each stack?

;; example:
^{::clerk/visibility {:code :fold}}
(def example
  "    [D]    
[N] [C]    
[Z] [M] [P]
1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
")

;; input:
^{::clerk/visibility {:code :fold}}
(def input
  (slurp (io/resource "day05")))


(defn parse
  [input]
  (let [[raw-start raw-instructions] (string/split input #" *1.*\n\n")]
    {:start (->> (string/split raw-start #"\n")
                 (map (fn [line]
                        (->> line
                             (partition 4 4 " ")
                             (map second))))
                 (apply map vector)
                 (map #(remove #{\space} %))
                 (map vector (range 1 100))
                 (into {})) 
     :instructions (map (fn [line]
                          (->> (string/split line #" ")
                               (partition 2)
                               (map (fn [[role number]]
                                      [(keyword role) (Integer/parseInt number)]))
                               (into {})))
                        (string/split raw-instructions #"\n"))}))

(def parsed-example
  (parse example))

(def parsed-input
  (parse input))

(defn execute-instruction
  [state {:keys [move from to]}]
  (-> state
      (update from #(drop move %))
      (update to #(concat (reverse (take move (state from))) %))))

(clerk/table
 (map vector
      (reductions execute-instruction (:start parsed-example) (:instructions parsed-example))
      (cons "initial" (:instructions parsed-example))))

(defn execute-all-instructions
  [{:keys [start instructions]}]
  (reduce execute-instruction start instructions))

(def rearranged-example
  (execute-all-instructions parsed-example))

(def rearranged
  (execute-all-instructions parsed-input))

(defn top-items
  [state]
  (apply str (map first (map state (range 1 (inc (count state)))))))

(def part1-example
  (top-items rearranged-example))

(def part1
  (top-items rearranged))

(defn execute-instruction-2
  [state {:keys [move from to]}]
  (-> state
      (update from #(drop move %))
      (update to #(concat (take move (state from)) %))))

(clerk/table
 (map vector
      (reductions execute-instruction-2 (:start parsed-example) (:instructions parsed-example))
      (cons "initial" (:instructions parsed-example))))

(defn execute-all-instructions-2
  [{:keys [start instructions]}]
  (reduce execute-instruction-2 start instructions))

(defn part2-answer
  [input]
  (-> input
      execute-all-instructions-2
      top-items))

(def part2-example
  (part2-answer parsed-example))

(def part2
  (part2-answer parsed-input))
