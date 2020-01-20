(ns aoc.day19
  (:require [aoc.util :as util]
            [aoc.intcode-computer :as ic]))

(def coords (for [x (range 50) y (range 50)] [x y]))

(defn get-influence*
  [coord]
  (-> (util/read-input "day19" ic/parse)
      (ic/new-computer)
      (ic/add-inputs coord)
      (ic/exec-all)
      (:output)
      (first)))

(def get-influence (memoize get-influence*))

(defn part1
  []
  (apply + (for [x (range 50) y (range 50)] (get-influence [x y]))))

(defn display
  [x1 x2 y1 y2]
  (doseq [y (range y1 (inc y2))]
    (println
     (apply str
            (for [x (range x1 (inc x2))]
              (if (= 1 (get-influence [x y]))
                "#"
                "."))))))

(defn find-rightmost-influence
  [y]
  ;; looking at some rectangles, I could see that the rightmost influenced
  ;; square in a row was just very slightly to the left of the diagonal
  (let [x (util/iterate-until
           dec
           #(or (= 1 (get-influence [% y]))
                (neg? %))
           y)]
    (if (not= -1 x)
      x)))

(defn ship-fit
  "returns the [x y] for the top left corner if the ship fits starting at the
  given row"
  [top-y]
  (let [right-x (find-rightmost-influence top-y)
        left-x (- right-x 99)
        bottom-y (+ top-y 99)]
    (if (and (= 1 (get-influence [right-x top-y]))
             (= 1 (get-influence [left-x bottom-y])))
      [left-x top-y])))

(defn find-fit
  []
  ;; based on the 45 degree angle we can estimate this will be 100 rows before
  ;; the beam is 200 columns wide. it is 18 wide on the 100th row.
  (ship-fit (util/iterate-until inc ship-fit (int (- (/ (* 100 200) 18) 100)))))

(defn part2
  []
  (let [[x y] (find-fit)]
    (+ (* 10000 x) y)))
