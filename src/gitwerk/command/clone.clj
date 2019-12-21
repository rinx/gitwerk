(ns gitwerk.command.clone
  (:require
   [clojure.spec.alpha :as spec]
   [gitwerk.external.git :as git]))

(defn clone
  [repo]
  (let [res (git/clone repo)]
    (if res
      {:status 0}
      {:status 1})))

(defn run [ctx args]
  (case (count args)
    1 (clone (first args))
    {:status 1
     :console-out "'clone' takes one argument"}))
