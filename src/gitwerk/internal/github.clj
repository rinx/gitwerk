(ns gitwerk.internal.github
 (:require
   [clj-http.lite.client :as http]
   [gitwerk.internal.json :as json]))

(def base-url "https://api.github.com")
(def version-header "Accept: application/vnd.github.v3+json")

(defn call
  ([url]
   (call url nil))
  ([url body]
   (let [res (http/get url
                   {:body body})
         status (:status res)
         body (-> res
                  (:body)
                  (json/read-value))]
     (when (= status 200)
       body))))

(defn fetch-pr-list
  [owner repo]
  (call (str base-url "/repos/" owner "/" repo "/pulls")))

(def exports
  {'fetch-pr-list fetch-pr-list})

(comment
  (fetch-pr-list "rinx" "dotfiles")
  )
