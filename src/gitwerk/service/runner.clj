(ns gitwerk.service.runner
  (:require
   [clojure.string :as string]
   [sci.core :as sci]
   [gitwerk.internal.git :as internal.git]
   [gitwerk.internal.github :as internal.github]
   [gitwerk.internal.io :as internal.io]
   [gitwerk.internal.json :as internal.json]
   [gitwerk.internal.semver :as internal.semver]
   [gitwerk.prelude :as prelude]
   [gitwerk.primitives :as primitives]))

(def nss
  {'git internal.git/exports
   'github internal.github/exports
   'io internal.io/exports
   'json internal.json/exports
   'semver internal.semver/exports})

(defn run
  [{:keys [command args options] :as ctx}]
  (let [filename (:filename options)
        prelude (if (nil? filename)
                  (when (:stdin? options)
                    (internal.io/read-from-stdin))
                  (internal.io/safe-read filename))
        tokens (->> args
                    (map #(str \" % \"))
                    (cons command)
                    (string/join " "))
        body (str \( tokens \))
        env (atom {})
        opts {:namespaces nss
              :bindings (merge primitives/clj-primitives
                               {'ctx ctx
                                'prelude (fn [] prelude/default)})
              :deny '[loop recur trampoline]
              :env env}
        ret-fn (fn [status out]
                 {:status status
                  :console-out out})]
    (try
      (->> (sci/binding [sci/out *out*
                         sci/in *in*]
             (sci/eval-string prelude/default opts)
             (when prelude
               (sci/eval-string prelude opts))
             (sci/eval-string body opts))
           (ret-fn 0))
      (catch Exception e
        (ret-fn 1 e)))))
