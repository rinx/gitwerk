(ns gitwerk.service.semver-auto
  (:require
    [clojure.spec.alpha :as spec]
    [clojure.string :as string]
    [gitwerk.service.semver :as semver]))

(def default-patterns
  {:patch "\\[(patch|PATCH)\\]"
   :minor "\\[(minor|MINOR)\\]"
   :major "\\[(major|MAJOR)\\]"
   :prefix "\\[(tag\\.prefix|TAG\\.PREFIX)=([^\\[\\]]*)\\]"
   :suffix "\\[(tag\\.suffix|TAG\\.SUFFIX)=([^\\[\\]]*)\\]"})

(defn get-*fix-string [matches]
  (let [matches (nth matches 0)]
    (nth matches 2)))

(defn semver-auto [message tag]
  (let [pats default-patterns
        patch-matches (re-seq (re-pattern (:patch pats)) message)
        minor-matches (re-seq (re-pattern (:minor pats)) message)
        major-matches (re-seq (re-pattern (:major pats)) message)
        prefix-matches (re-seq (re-pattern (:prefix pats)) message)
        suffix-matches (re-seq (re-pattern (:suffix pats)) message)]
    (-> tag
        (semver/str->version)
        (cond->
          major-matches (semver/major)
          minor-matches (semver/minor)
          patch-matches (semver/patch)
          prefix-matches (semver/prefix (get-*fix-string prefix-matches))
          suffix-matches (semver/suffix (get-*fix-string suffix-matches)))
        (semver/version->str))))

(comment
  (get-*fix-string
    (re-seq (re-pattern (:prefix default-patterns)) "[tag.prefix=xxx]"))
  (get-*fix-string
    (re-seq (re-pattern (:prefix default-patterns)) "[tag.prefix=xxx] [patch]"))
  (get-*fix-string
    (re-seq (re-pattern (:suffix default-patterns)) "[tag.suffix=yyy]"))
  (get-*fix-string
    (re-seq (re-pattern (:suffix default-patterns)) "[tag.suffix=yyy][minor]")))
