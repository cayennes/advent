(ns user
  (:require [nextjournal.clerk :as clerk]))

(comment "do this and then use editor binding in file to display"
  (clerk/serve! {:browse? true}))

(comment "or watch all the files"
  (clerk/serve! {:browse? true :watch-paths ["src"]}))
