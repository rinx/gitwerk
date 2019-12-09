(ns gitwerk.command.tag
  (:require
   [clojure.spec.alpha :as spec]
   [gitwerk.external.git :as git]))

(defn run [ctx args]
  (-> (git/repo ".")
      (git/tags)
      (println)))
