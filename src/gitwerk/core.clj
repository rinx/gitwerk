(ns gitwerk.core
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.tools.cli :as cli]
   [clojure.string :as string]
   [clojure.pprint :as pprint]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [gitwerk.internal.env :as env]
   [gitwerk.service.runner :as runner])
  (:gen-class))

(def cli-header
  (string/join
   "\n"
   (concat
    ["gitwerk is a CLI tool for supporting Git(Hub) operations on CI."
     ""
     "Usage: gitwerk [command] [options]"
     ""
     "Available commands:"]
    (mapv (fn [[k v]]
            (let [name (format "  %-16s " (name k))
                  desc (:description v)]
              (str name desc))) runner/definitions)
    [""
     "Options:"])))

(def cli-options
  [["-f" "--file PATH" "filename of config" :id :config-flename]
   ["-e" "--edn" "print result in edn format" :id :edn?]
   ["-d" "--debug" "debug flag" :id :debug?]
   ["-h" "--help" "print help" :id :help?]])

(defn edn-output
  [ctx res]
  (pprint/pprint res))

(defn std-output
  [{:keys [summary] :as ctx}
   {:keys [status invalid-arg? console-out]}]
  (when console-out
    (if (map? console-out)
      (pprint/pprint console-out)
      (println console-out)))
  (when invalid-arg?
    (println cli-header)
    (println summary))
  (if status
    (System/exit status)
    (System/exit 1)))

(defn run
  [{:keys [options] :as ctx}]
  (let [{:keys [edn?]} options
        res (runner/run ctx)]
    (if edn?
      (edn-output ctx res)
      (std-output ctx res))))

(defn main
  [{:keys [options summary arguments] :as parsed-result}]
  (let [{:keys [config-filename help?]} options
        ctx {:command (first arguments)
             :args (drop 1 arguments)
             :options options
             :summary summary
             :env (env/read-envs)}]
    (if (or help? (nil? (:command ctx)))
      (do
        (println cli-header)
        (println summary))
      (run ctx))))

(defn -main [& args]
  (try
    (-> args
        (cli/parse-opts
         cli-options
         :in-order true)
        (main))
    (catch Exception e
      (println (.getMessage e))
      (System/exit 1))
    (finally
      (shutdown-agents))))
