(ns gitwerk.service.runner
  (:require
   [clojure.spec.alpha :as spec]
   [camel-snake-kebab.core :as csk]
   [gitwerk.command.clone :as command.clone]
   [gitwerk.command.log :as command.log]
   [gitwerk.command.semver :as command.semver]
   [gitwerk.command.semver-auto :as command.semver-auto]
   [gitwerk.command.tag :as command.tag]))

(defn dispatch
  [{:keys [args] :as ctx} cmd]
  (let [default (fn [_ _]
                  {:status 1
                   :invalid-arg? true})
        cmd (case cmd
              :clone command.clone/run
              :log command.log/run
              :semver command.semver/run
              :semver-auto command.semver-auto/run
              :tag command.tag/run
              default)]
    (cmd ctx args)))

(defn run
  [{:keys [command] :as ctx}]
  (->> command
       (csk/->kebab-case-keyword)
       (dispatch ctx)))
