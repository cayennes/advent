(ns aoc.day17
  (:require [aoc.intcode-computer :as ic]
            [aoc.util :as util]
            [clojure.string :as string]))

(defn ascii
  [int-seq]
  (apply str (map char int-seq)))

(defn string->position-map
  [diagram-string]
  (into {}
        (for [[y row] (map-indexed vector (string/split diagram-string #"\n"))
              [x ch] (map-indexed vector row)]
          [[x y] (str ch)])))

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
                       (ascii))]
    (println output-str)
    (-> output-str
        (string->position-map)
        (intersections-checksum))))

(defn available-turn
  [scaffold-map {:keys [position direction]}]
  (->> [:left :right]
       (filter #(= "#"
                   (scaffold-map (mapv + position (util/rotate direction %)))))
       (first)))

(defn count-scaffolding-ahead
  [scaffold-map {:keys [position direction]}]
  (->> (iterate #(mapv + % direction) position)
       (rest) ;; drop current position
       (map scaffold-map)
       (take-while #(= "#" %))
       (count)))

(defn next-path-segment
  [scaffold-map {:keys [position direction] :as robot}]
  (let [turn (available-turn scaffold-map robot)
        length (count-scaffolding-ahead
                scaffold-map
                (update robot :direction util/rotate turn))]
    [turn length]))

;; TODO: make the above function more iterable and/or make a function for a
;; robot to travel a path
