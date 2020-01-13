(ns gitwerk.service.runner
  (:require
   [clojure.spec.alpha :as spec]
   [camel-snake-kebab.core :as csk]
   [gitwerk.command.clone :as command.clone]
   [gitwerk.command.ctx-semver :as command.ctx-semver]
   [gitwerk.command.log :as command.log]
   [gitwerk.command.sci :as command.sci]
   [gitwerk.command.semver :as command.semver]
   [gitwerk.command.release :as command.release]
   [gitwerk.command.latest-release :as command.latest-release]
   [gitwerk.command.tag :as command.tag]))

(def primitives
  {:clone
   {:command command.clone/run
    :description "clone git repository"}
   :log
   {:command command.log/run
    :description "show git logs of current directory"}
   :semver
   {:command command.semver/run
    :description "print incremented version"}
   :semver-auto
   {:command command.ctx-semver/run
    :description "alias of ctx-semver"}
   :ctx-semver
   {:command command.ctx-semver/run
    :description "contextual semver: increment version by latest git log message contexts"}
   :release
   {:command command.release/run
    :description "create release"}
   :latest-release
   {:command command.latest-release/run
    :description "get latest release"}
   :tag
   {:command command.tag/run
    :description "show git tags of current directory"}})

(def definitions
  (let [->binding (fn [[k v]]
                    (let [sym (symbol (name k))
                          com (:command v)]
                      [sym com]))
        bindings (->> primitives
                      (map ->binding)
                      (into {}))
        sci-opts {:bindings bindings}]
    (into primitives
          {:sci {:command (command.sci/run-fn sci-opts)
                 :description "run user-defined script (written in clojure)"}})))

(defn dispatch
  [{:keys [args] :as ctx} cmd]
  (let [default (fn [_ _]
                  {:status 1
                   :invalid-arg? true})
        cmd (or (:command (cmd definitions)) default)]
    (cmd ctx args)))

(defn run
  [{:keys [command] :as ctx}]
  (->> command
       (csk/->kebab-case-keyword)
       (dispatch ctx)))
