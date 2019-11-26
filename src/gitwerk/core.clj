(ns gitwerk.core
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.tools.cli :as cli]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [camel-snake-kebab.core :as csk]
   [gitwerk.command.clone :as command.clone]
   [gitwerk.command.log :as command.log]
   [gitwerk.command.tag :as command.tag])
  (:gen-class))

(def cli-header "Usage: gitwerk [command] [options]")
(def cli-options
  [["-f" "--file PATH" "config"
    :id :config-filename
    :default "config.edn"]
   ["-h" "--help" :id :help?]])

(defn run
  [{:keys [command args summary] :as ctx}]
  (case (csk/->kebab-case-keyword command)
    :clone (apply command.clone/run args)
    :log (apply command.log/run args)
    :tag (apply command.tag/run args)
    (do
      (println cli-header)
      (println summary))))

(defn main
  [{:keys [options summary arguments] :as parsed-result}]
  (let [{:keys [config-filename help?]} options
        ctx {:command (first arguments)
             :args (drop 1 arguments)
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
      (println (.getMessage e)))
    (finally
      (shutdown-agents))))
