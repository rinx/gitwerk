(ns gitwerk.command.log
  (:require
   [clojure.spec.alpha :as spec]
   [gitwerk.external.git :as git]))

(defn run [& args]
  (-> (git/repo ".")
      (git/logs)
      (println)))
