(ns gitwerk.command.log
  (:require
   [clojure.spec.alpha :as spec]
   [gitwerk.external.git :as git]))

(defn run [ctx args]
  (let [res (-> (git/repo ".")
                (git/logs))]
    (if res
      {:status 0
       :console-out res}
      {:status 1
       :console-out "could not fetch logs"})))
