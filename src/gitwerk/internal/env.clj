(ns gitwerk.internal.env
  (:require
   [clojure.spec.alpha :as spec]))

(def github-api-envvar "GITHUB_API")
(def github-token-envvar "GITHUB_TOKEN")

(defn getenv [name]
  (System/getenv name))

(defn read-envs []
  {:github-api (getenv github-api-envvar)
   :github-token (getenv github-token-envvar)})
