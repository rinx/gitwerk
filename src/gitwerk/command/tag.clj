(ns gitwerk.command.tag
  (:require
   [clojure.spec.alpha :as spec]
   [gitwerk.externs.git :as git]))

(defn run [& args]
  (-> (git/repo ".")
      (git/tags)
      (println)))
