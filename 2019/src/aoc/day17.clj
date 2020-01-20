(ns aoc.day17
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]
            [clojure.string :as string]))

(defn intersection?
  [scaffold-map pos]
  (every? #(= (scaffold-map %) "#")
          (conj (util/adjacent-positions pos) pos)))

(defn all-intersections
  [scaffold-map]
  (filter #(intersection? scaffold-map (first %)) scaffold-map))

(defn intersections-checksum
  [scaffold-map]
  (->> scaffold-map
       (all-intersections)
       (map first)
       (map #(apply * %))
       (apply +)))

(defn part1
  []
  (let [output-str (-> (util/read-input "day17" ic/parse)
                       (ic/new-computer)
                       (ic/exec-all)
                       (:output)
                       (util/ascii))]
    (println output-str)
    (-> output-str
        (util/string->position-map)
        (intersections-checksum))))

;; these functions work with virtual robots that we can just tell us to move

(defn available-turn
  [scaffold-map {:keys [position orientation]}]
  (->> [:left :right]
       (filter #(= "#"
                   (scaffold-map (mapv + position (util/rotate orientation %)))))
       (first)))

(defn count-scaffolding-ahead
  [scaffold-map {:keys [position orientation]}]
  (->> (iterate #(mapv + % orientation) position)
       (rest) ;; drop current position
       (map scaffold-map)
       (take-while #(= "#" %))
       (count)))

(defn next-path-segment
  [scaffold-map robot]
  (if-let [turn (available-turn scaffold-map robot)]
    (let [length (count-scaffolding-ahead
                  scaffold-map
                  (update robot :orientation util/rotate turn))]
      [turn length])))

(defn follow-path
  "move the robot along the path, with no length restriction"
  [robot path]
  (reduce (fn [r [side length]]
            (-> r
                (util/turn side)
                (util/move-forward length)))
          robot
          path))

(defn all-segments
  [scaffold-map robot]
  (loop [path []]
    (if-let [next-segment (next-path-segment scaffold-map
                                             (follow-path robot path))]
      (recur (conj path next-segment))
      path)))

(defn calculate-map
  []
  (-> (util/read-input "day17" ic/parse)
      (ic/new-computer)
      (ic/exec-all)
      (:output)
      (util/ascii)
      (util/string->position-map)))

(defn complete-path
  []
  (all-segments (calculate-map)
                {:position [2 12] :orientation [0 -1]}))

;; A
;; [:right 10]
;; [:right 10]
;; [:right 6]
;; [:right 4]

;; B
;; [:right 10]
;; [:right 10]
;; [:left 4]

;; A
;; [:right 10]
;; [:right 10]
;; [:right 6]
;; [:right 4]

;; C
;; [:right 4]
;; [:left 4]
;; [:left 10]
;; [:left 10]

;; A
;; [:right 10]
;; [:right 10]
;; [:right 6]
;; [:right 4]

;; B
;; [:right 10]
;; [:right 10]
;; [:left 4]

;; C
;; [:right 4]
;; [:left 4]
;; [:left 10]
;; [:left 10]

;; B
;; [:right 10]
;; [:right 10]
;; [:left 4]

;; C
;; [:right 4]
;; [:left 4]
;; [:left 10]
;; [:left 10]

;; B
;; [:right 10]
;; [:right 10]
;; [:left 4]

(defn unspace
  "remove all spaces"
  [s]
  (string/replace s #" " ""))

(defn robot-input
  []
  (unspace "A,B,A,C,A,B,C,B,C,B
                 R,10,R,10,R,6,R,4
                 R,10,R,10,L,4
                 R,4,L,4,L,10,L,10
                 n
                 "))

(defn part2
  []
  (-> (util/read-input "day17" ic/parse)
      (ic/new-computer (robot-input))
      (assoc-in [:program 0] 2)
      (ic/exec-all)
      (:output)
      (last)))
