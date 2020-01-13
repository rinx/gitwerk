(ns gitwerk.external.github
 (:require
   [clojure.spec.alpha :as spec]
   [clj-http.lite.client :as http]))

(def github-api
  "https://api.github.com")

(defn compose-url
  [base path]
  (str base path))

(defn http-get
  [ctx path]
  (let [github-api (or (get-in ctx [:env :github-api])
                       github-api)
        url (compose-url github-api path)
        token  (get-in ctx [:env :github-token])
        headers (cond-> {}
                  token (assoc "Authorization" (str "token " token)))]
    (http/get url
              {:headers headers})))

(defn http-post
  [ctx path body]
  (let [github-api (or (get-in ctx [:env :github-api])
                       github-api)
        url (compose-url github-api path)
        token (get-in ctx [:env :github-token])
        headers (cond-> {}
                  token (assoc "Authorization" (str "token " token)))]
    (http/get url
              {:headers headers
               :body body})))

(defn get-latest-release
  "View the latest published full release for the repository.
  https://developer.github.com/v3/repos/releases/#get-the-latest-release"
  [ctx org repo]
  (let [path (format "/repos/%s/%s/releases/latest" org repo)]
    (http-get ctx path)))

(defn create-release
  "Users with push access to the repository can create a release.
  https://developer.github.com/v3/repos/releases/#create-a-release

  opts
  {:tag_name string
   :target_commitish string
   :name string
   :body string
   :draft boolean
   :prerelease boolean}"
  [ctx org repo opts]
  (let [path (format "/repos/%s/%s/releases" org repo)]
    (http-post ctx path opts)))

(comment
  (get-latest-release {} "rinx" "gitwerk")
  )
