(ns gitwerk.command.clone
  (:require
   [clojure.spec.alpha :as spec]
   [gitwerk.externs.git :as git]))

(defn run [& args]
  (case (count args)
    1 (git/clone (first args))
    (throw
      (Exception. "'clone' takes one argument"))))
