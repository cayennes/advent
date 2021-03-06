(ns aoc.day3
  (:require [aoc.util :as u]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as string]))

(defn parse
  [input]
  (for [line (string/split input #"\n")]
    (for [[direction & length-as-string] (string/split line #",")]
      {:direction direction
       :length (edn/read-string (apply str length-as-string))})))

(def input
  (parse (slurp (io/resource "day3"))))

(def test-input
  (parse "R8,U5,L5,D3\nU7,R6,D4,L4"))

(defn segment-locations
  [{:keys [direction length]} [start-x start-y]]
  (for [l (range 1 (inc length))]
    [(+ start-x (case direction
                  \R l
                  \L (- l)
                  0))
     (+ start-y (case direction
                  \U l
                  \D (- l)
                  0))]))

(defn wire-loc-seq
  [wire]
  (reduce #(concat %1 (segment-locations %2 (last %1)))
          [[0 0]]
          wire))

(defn wire-diagram
  [wire wire-num]
  (zipmap (wire-loc-seq wire) (repeat #{wire-num})))

(defn wires-diagram
  [wires]
  (apply merge-with
         set/union
         (map-indexed #(wire-diagram %2 %1) wires)))

(defn closest-intersection
  [diagram]
  (first (for [d (range 1 100000)
               x (range (- d) (inc d))
               y [(- d (u/abs x)) (- (u/abs x) d)]
               :when (> (count (get diagram [x y]))
                        1)]
           d)))

(defn string->closest-intersection
  [s]
  (-> s parse wires-diagram closest-intersection))

(defn day3a
  []
  (-> input wires-diagram closest-intersection))

(comment
  (string->closest-intersection
   "R75,D30,R83,U83,L12,D49,R71,U7,L72
U62,R66,U55,R34,D71,R55,D58,R83")
  (string->closest-intersection
   "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
  (string->closest-intersection
   "R8,U5,L5,D3\nU7,R6,D4,L4"))

(defn all-pairs
  [s]
  (for [[i s-i] (map-indexed vector s)
        s-j (take i s)]
    #{s-i s-j}))

(defn wire-dist-diagram
  [wire wire-num]
  (zipmap (wire-loc-seq wire)
          (map #(set [{:wire wire-num :distance %}]) (range))))

(defn wires-dist-diagram
  [wires]
  (apply merge-with
         set/union
         (map-indexed #(wire-dist-diagram %2 %1) wires)))

(defn multiple-wires?
  [wire-dist-set]
  (> (count (distinct (map :wire wire-dist-set)))
     1))

(defn wires-dist
  [wire-dist-set]
  (apply min
         (for [[w1 w2] (map vec (all-pairs wire-dist-set))
               :when (not= (:wire w1) (:wire w2))]
           (+ (:distance w1) (:distance w2)))))

(comment (wires-dist #{{:wire 1, :distance 4} {:wire 0, :distance 2}}))

(defn shortest-path-intersection
  [input]
  (apply min
         (for [[loc wire-dists] (wires-dist-diagram input)
               :when (and
                      (not= [0 0] loc)
                      (multiple-wires? wire-dists))]
           (wires-dist wire-dists))))

(defn string->shortest-path-intersection
  [s]
  (-> s parse shortest-path-intersection))

(comment
  (string->shortest-path-intersection
   "R8,U5,L5,D3\nU7,R6,D4,L4")
  (string->shortest-path-intersection
   "R75,D30,R83,U83,L12,D49,R71,U7,L72
U62,R66,U55,R34,D71,R55,D58,R83")
  (string->shortest-path-intersection
   "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"))

(defn day3b [] (shortest-path-intersection input))
