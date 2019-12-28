(ns gitwerk.command.sci
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.pprint :as pprint]
   [sci.core :as sci]
   [gitwerk.internal.io :as internal.io]))

(def clj-primitives
  {'println println
   'pprint pprint/pprint})

(defn run-fn [sci-opts]
  (fn [ctx args]
    (try
      (let [file (first args)
            body (if (nil? file)
                   (internal.io/read-from-stdin)
                   (internal.io/safe-read file))
            opts (-> sci-opts
                     (update
                       :bindings
                       #(-> %
                            (merge clj-primitives)
                            (merge {'ctx ctx}))))
            res (sci/eval-string body opts)]
        (if (and (map? res) (:status res))
          res
          {:status 0
           :console-out res}))
      (catch Exception e
        {:status 1
         :console-out
         {:error (.getMessage e)}}))))
