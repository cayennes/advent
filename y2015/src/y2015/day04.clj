(ns y2015.day04
  (:import java.security.MessageDigest))

(def input "bgvyzdsv")

;; copied from https://stackoverflow.com/a/54061178
(defn md5 [^String s]
  (let [algorithm (MessageDigest/getInstance "MD5")
        raw (.digest algorithm (.getBytes s))]
    (format "%032x" (BigInteger. 1 raw))))

(defn n-zeroes?
  [n s]
  (= (repeat n \0)
     (take n s)))

(defn mine
  [n]
  (->> (range)
       (map (juxt #(md5 (str input %)) identity))
       (filter #(n-zeroes? n (first %)))
       (first)
       (second)))

(defn part1
  []
  (mine 5))

(comment (= 254575 (part1)))

(defn part2
  []
  (mine 6))

(comment (= 1038736 (part2)))

