(ns gitwerk.command.sci
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.pprint :as pprint]
   [clojure.edn :as edn]
   [sci.core :as sci]
   [clj-http.lite.client :as http]
   [gitwerk.internal.io :as internal.io]
   [gitwerk.internal.json :as json]))

(def clj-primitives
  {'println println
   'pprint pprint/pprint
   'json/read-value json/read-value
   'edn/read-string edn/read-string
   'env #(or (System/getenv %) "")
   'http/get http/get
   'http/post http/post})

(defn run-fn [sci-opts]
  (fn [ctx args]
    (try
      (let [file (first args)
            body (if (nil? file)
                   (internal.io/read-from-stdin)
                   (internal.io/safe-read file))
            ctx (dissoc ctx :command :summary)
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
