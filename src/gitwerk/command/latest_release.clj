(ns gitwerk.command.latest-release
  (:require
   [clojure.spec.alpha :as spec]
   [gitwerk.internal.json :as json]
   [gitwerk.external.github :as github]))

(defn run [ctx args]
  (let [org (first args)
        repo (second args)
        res (github/get-latest-release ctx org repo)]
    (if res
      {:status 0
       :console-out (-> res
                        :body
                        (json/read-value))}
      {:status 1})))
