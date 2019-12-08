(ns gitwerk.external.git
 (:require
   [clojure.spec.alpha :as spec])
 (:import
   [java.io File]
   [org.eclipse.jgit.api CloneCommand Git]
   [org.eclipse.jgit.lib Constants Repository]
   [org.eclipse.jgit.storage.file FileRepositoryBuilder]))

(defn clone [uri]
  (-> (CloneCommand.)
      (.setURI uri)
      (.call)))

(defn repo [path]
  (let [git-path (str path "/" Constants/DOT_GIT)]
    (-> (FileRepositoryBuilder.)
        (.setGitDir (File. git-path))
        (.readEnvironment)
        (.findGitDir)
        (.build)
        (Git.))))

(defn parse-person [p]
  {:name (.getName p)
   :email (.getEmailAddress p)})

(defn parse-log [l]
  {:sha (.getName l)
   :short-message (.getShortMessage l)
   :full-message (.getFullMessage l)
   :author (parse-person (.getAuthorIdent l))
   :commiter (parse-person (.getCommitterIdent l))
   :time (.getCommitTime l)})

(defn logs [repo]
  (-> repo
      (.log)
      (.call)
      (.iterator)
      (iterator-seq)
      (->> (map parse-log))))

(defn tags [repo]
  (-> repo
      (.tagList)
      (.call)
      (->> (map #(.getName %)))))

(defn latest-log [repo]
  (-> repo
      (logs)
      (first)))

(defn latest-tag [repo]
  (-> repo
      (tags)
      (first)))

(comment
  (-> (repo ".")
      (logs)))
