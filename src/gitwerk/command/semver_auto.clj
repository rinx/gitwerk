(ns gitwerk.command.semver-auto
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.string :as string]
   [gitwerk.external.git :as git]
   [gitwerk.service.semver :as semver]))

(def default-patterns
  {:patch "(\\[(patch|PATCH)\\])"
   :minor "(\\[(minor|MINOR)\\])"
   :major "(\\[(major|MAJOR)\\])"})

(defn semver-auto [message tag]
  (let [pats default-patterns
        patch-matches (re-seq (re-pattern (:patch pats)) message)
        minor-matches (re-seq (re-pattern (:minor pats)) message)
        major-matches (re-seq (re-pattern (:major pats)) message)]
    (-> tag
        (semver/str->version)
        (cond->
          major-matches (semver/major)
          minor-matches (semver/minor)
          patch-matches (semver/patch))
        (semver/version->str))))

(defn run [& args]
  (let [repo (git/repo ".")
        message (-> repo
                    (git/latest-log)
                    :full-message)
        tag (-> repo
                (git/latest-tag)
                (semver/parse-refs))
        new-tag (semver-auto message tag)]
    (when (not (= tag new-tag))
      (git/tag repo new-tag))))

(comment
  (run))
