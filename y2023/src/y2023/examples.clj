(ns y2023.examples
  (:require [clojure.string :as string]))

;; # defining things

;; a value

(def n 17)

n ;; => 17

;; a function

(defn double
  [x]
  (* 2 x))

(double 8) ;; => 16

;; let first has square brackets with names given to things, and those names mean those things until the end of the parenthese that surround the let. The value of the whole thing is the last value inside the perentheses outside the square brackets.

(let [x 5
      y (+ x 2)]
  y) ;; => 7

;; # working with lists

;; vectors are the easiest type of list to create in clojure

(def some-vector [4 3 "things"])

;; map takes a list and apply a function to every element in it

(map double [1 4 2]) ;; => (2 8 4)

;; take a list and keep only the items for which some function is true
;; (the pos? function returns true if a number is greater than 0)

(filter pos? [-5 0 4 2 -1]) ;; => (4 2)

;; # working with maps

(def m {:one 1
        :two 2})

(get m :two) ;; => 2

(get m :three "not found") ;; => "not found"

;; # working with strings

;; split a string into a list of strings, seperating on a given seperator

(string/split "a-b-cde-fg" #"-") ;; => ["a" "b" "cde" "fg"]

;; regular expressions

;; (re-find regex string) returns a vector with the thing in the string that matches the whole regex and the things that match the parts in parentheses (or just the match if there are no parentheses in the regex)

(re-find #"a(b)c(d)" "12ab34abcd56") ;; => ["abcd" "b" "d"]

(re-find #"e" "abcdefg") ;; => "e"

;; | seperates different options for what to match

(re-find #"b(1|2)" "a1b2") ;; => ["b2" "2"]

(re-find #"(1|2|one|two)" "twone3twone") ;; => ["two" "two"]

;; . matches anything

(re-find #"a.a" "abbaca") ;; => "aca"

;; * after something changes it to match any number zero or more of that thing. It will do the most possible.

(re-find #"a.*a" "abbaccd") ;; => "abba"

(re-find #".*(1|2|one|two)" "twone3twone") ;; => ["twone3twone" "one"]
