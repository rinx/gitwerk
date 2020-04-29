(ns gitwerk.prelude)

(def default
  "(defn clone [repository]
  (git/clone repository))

(defn log
 ([]
  (log \".\"))
 ([repodir]
  (-> (git/repo repodir)
      (git/logs))))

(defn tag
  ([]
   (tag \".\"))
  ([repodir]
   (-> (git/repo repodir)
       (git/tags))))

(defn repl []
  (println \"not implemented yet\"))

(defn contextual-semver
  ([]
   (contextual-semver \".\"))
  ([repodir]
   (let [repo (git/repo repodir)
         message (-> repo
                     (git/latest-log)
                     :full-message)
         tag (or (-> repo
                     (git/tags)
                     (semver/latest-tag))
                 (semver/default-version-str))
         new-tag (semver/contextual-semver message tag)]
     (if (not (= tag new-tag))
       (do
         (git/tag repo new-tag)
         {:status :updated
          :old-version tag
          :new-version new-tag})
       {:status :not-updated}))))")
