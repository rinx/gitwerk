(ns gitwerk.command.ctx-semver
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.string :as string]
   [gitwerk.external.git :as git]
   [gitwerk.service.semver :as semver]
   [gitwerk.service.ctx-semver :as ctx-semver]))

(defn ctx-semver
  [repodir]
  (let [repo (git/repo repodir)
        message (-> repo
                    (git/latest-log)
                    :full-message)
        tag (or (-> repo
                    (git/tags)
                    (semver/latest-tag))
                (semver/default-version-str))
        new-tag (ctx-semver/ctx-semver message tag)]
    (when (not (= tag new-tag))
      (git/tag repo new-tag)
      {:old tag
       :new new-tag})))

(defn run [ctx _]
  (let [res (ctx-semver ".")]
    (if res
      {:status 0
       :console-out
       {:status :updated
        :old-version (:old res)
        :new-version (:new res)}}
      {:status 0
       :console-out
       {:status :not-updated}})))

(comment
  (run {} []))
