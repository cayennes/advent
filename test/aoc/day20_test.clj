(ns aoc.day20-test
  (:require [aoc.day20 :as d]
            [aoc.util :as util]
            [clojure.test :refer [deftest is]]))

(def ex1
  (util/string->position-map
   "         A           
         A           
  #######.#########  
  #######.........#  
  #######.#######.#  
  #######.#######.#  
  #######.#######.#  
  #####  B    ###.#  
BC...##  C    ###.#  
  ##.##       ###.#  
  ##...DE  F  ###.#  
  #####    G  ###.#  
  #########.#####.#  
DE..#######...###.#  
  #.#########.###.#  
FG..#########.....#  
  ###########.#####  
             Z       
             Z "))

(deftest maybe-label-works
  (is (= :AA (d/maybe-label ex1 [9 2]))))

(deftest labeled-coords-works
  (is (= #{[[2 8] :BC] [[9 6] :BC] [[2 13] :DE] [[11 12] :FG] [[2 15] :FG]
           [[9 2] :AA] [[13 16] :ZZ] [[6 10] :DE]}
         (set (d/labeled-coords ex1)))))
