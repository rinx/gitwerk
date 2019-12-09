(ns gitwerk.command.semver-auto
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.string :as string]
   [gitwerk.external.git :as git]
   [gitwerk.service.semver :as semver]
   [gitwerk.service.semver-auto :as semver-auto]))

(defn run [ctx args]
  (let [repo (git/repo ".")
        message (-> repo
                    (git/latest-log)
                    :full-message)
        tag (or (-> repo
                    (git/tags)
                    (semver/latest-tag))
                (semver/default-version-str))
        new-tag (semver-auto/semver-auto message tag)]
    (when (not (= tag new-tag))
      (git/tag repo new-tag))))

(comment
  (run {} []))
