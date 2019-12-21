(ns gitwerk.external.github
 (:require
   [clojure.spec.alpha :as spec]
   [clj-http.lite.client :as http]))

(defn call
  ([url]
   (call url nil))
  ([url body]
   (http/get url
             {:body body})))
