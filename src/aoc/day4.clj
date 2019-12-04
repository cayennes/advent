(ns aoc.day4)

(defn digits->number
  [digits]
  (reduce #(+ (* 10 %1) %2) digits))

(defn double-at
  [digits double-idx]
  (let [[start end] (split-at double-idx digits)]
    (concat start [(first end)] end)))

(defn possibilities-within
  [start end]
  (distinct
   (for [a (range 0 10)
         b (range a 10)
         c (range b 10)
         d (range c 10)
         e (range d 10)
         double-idx (range 5)
         :let [code (-> [a b c d e] (double-at double-idx) (digits->number))]
         :when (<= start code)
         :while (<= code end)]
     code)))

(defn day4a []
  (count (possibilities-within 206938 679128)))

(defn possibilities-within-b
  [start end]
  (distinct
   (for [a (range 0 10)
         b (range a 10)
         c (range b 10)
         d (range c 10)
         e (range d 10)
         double-idx (range 5)
         :let [pre-code [a b c d e]
               code (double-at pre-code double-idx)
               code-number (digits->number code)]
         :when (and (<= start code-number)
                    (not= (get pre-code double-idx)
                          (get pre-code (inc double-idx)))
                    (not= (get pre-code double-idx)
                          (get pre-code (dec double-idx))))
         :while (<= code-number end)]
     code-number)))

(defn day4b []
  (count (possibilities-within-b 206938 679128)))
