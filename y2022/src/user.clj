(ns user
  (:require [nextjournal.clerk :as clerk]))

(comment "do this and then use editor binding in file to display"
  (clerk/serve! {:browse? true}))
