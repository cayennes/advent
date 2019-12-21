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
  (is (= :BC (d/maybe-label ex1 [9 6]))))

(deftest labeled-coords-works
  (is (= #{[[6 10] :DE] [[9 6] :BC] [[2 15] :FG] [[13 16] :ZZ] [[9 2] :AA]
           [[2 8] :BC] [[2 13] :DE] [[11 12] :FG]}
         (set (d/labeled-coords ex1)))))

(deftest portal-adjacency-map-works
  (is (= {[2 8] #{[9 6]}, [9 6] #{[2 8]}, [2 13] #{[6 10]}, [6 10] #{[2 13]},
          [11 12] #{[2 15]}, [2 15] #{[11 12]}}
         (d/portal-adjacency-map ex1))))

(deftest adjacency-map-works
  (is (= {[0 0] #{[1 0]} [1 0] #{[0 0] [2 0]} [2 0] #{[1 0]}}
         (d/adjacency-map {[0 0] "." [1 0] "." [2 0] "." [3 0] "A"}))))

(deftest find-label-works
  (is (= [9 2] (d/find-label ex1 :AA))))

(deftest search-step-works
  (is (= {:adjacency-map (d/adjacent-coord-map ex1)
          :search-locations #{[9 3]}
          :visited #{[9 2]}}
         (d/search-step {:adjacency-map (d/adjacent-coord-map ex1)
                         :search-locations #{[9 2]}
                         :visited #{}}))))

(deftest solve-steps-works
  (is (= 686 (d/solve-steps ex1))))

(deftest part1-result
  (is (= 686 (d/part1))))
