(ns gitwerk.command.tag
  (:require
   [clojure.spec.alpha :as spec]
   [gitwerk.external.git :as git]))

(defn run [ctx args]
  (let [res (-> (git/repo ".")
                (git/tags))]
    (if res
      {:status 0
       :console-out res}
      {:status 1
       :console-out "could not fetch tags"})))
