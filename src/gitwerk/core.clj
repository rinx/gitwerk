(ns gitwerk.core
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.tools.cli :as cli]
   [clojure.string :as string]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [gitwerk.service.runner :as runner])
  (:gen-class))

(def cli-header
  (string/join
    "\n"
    ["gitwerk is a CLI tool for supporting Git(Hub) operations on CI."
     ""
     "Usage: gitwerk [command] [options]"
     ""
     "Available commands:"
     "  clone [url]   clone git repository"
     "  log           show git logs of current directory"
     "  semver [type] print incremented version"
     "  semver-auto   increment version by latest git log message contexts"
     "  tag           show git tags of current directory"
     ""
     "Options:"]))
(def cli-options
  [["-f" "--file PATH" "config"
    :id :config-filename
    :default "config.edn"]
   ["-e" "--edn" :id :edn?]
   ["-d" "--debug" :id :debug?]
   ["-h" "--help" :id :help?]])

(defn edn-output
  [ctx res]
  (println res))

(defn std-output
  [{:keys [summary] :as ctx}
   {:keys [status invalid-arg? console-out]}]
    (when console-out
      (println console-out))
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
             :summary summary}]
    (if (or help? (nil? (:command ctx)))
      (do
        (println cli-header)
        (println summary))
      (run ctx))))

(defn -main [& args]
  (try
    (-> args
        (cli/parse-opts cli-options)
        (main))
    (catch Exception e
      (println (.getMessage e))
      (System/exit 1))
    (finally
      (shutdown-agents))))
