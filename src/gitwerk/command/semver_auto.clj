(ns gitwerk.command.semver-auto
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.string :as string]
   [gitwerk.external.git :as git]
   [gitwerk.service.semver :as semver]
   [gitwerk.service.semver-auto :as semver-auto]))

(defn semver-auto
  [repodir dry-run?]
  (let [repo (git/repo repodir)
        message (-> repo
                    (git/latest-log)
                    :full-message)
        tag (or (-> repo
                    (git/tags)
                    (semver/latest-tag))
                (semver/default-version-str))
        new-tag (semver-auto/semver-auto message tag)]
    (when-not (= tag new-tag)
      (when-not dry-run?
        (git/tag repo new-tag))
      {:old tag
       :new new-tag})))

(defn run [{:keys [options] :as ctx} _]
  (let [{:keys [dry-run?]} options
        res (semver-auto "." dry-run?)]
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
