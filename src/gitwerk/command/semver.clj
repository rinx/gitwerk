(ns gitwerk.command.semver
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.string :as string]
   [gitwerk.external.git :as git]
   [gitwerk.service.semver :as semver]))

(defn semver [ctx args]
  (let [semver-type (-> (first args)
                        (string/lower-case)
                        (keyword))
        semver-func (condp = semver-type
                      :patch semver/patch
                      :minor semver/minor
                      :major semver/major)]
    (-> (git/repo ".")
        (git/tags)
        (semver/latest-tag)
        (semver/str->version)
        (semver-func)
        (semver/version->str))))

(defn run [ctx args]
  (let [res (semver ctx args)]
    (if res
      {:status 0
       :console-out res}
      {:status 1
       :console-out "nothing updated"})))

(comment
  (run {} ["patch"])
  (run {} ["minor"])
  (run {} ["major"]))
