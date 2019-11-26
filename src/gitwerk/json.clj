(ns gitwerk.json
  (:require
   [clojure.spec.alpha :as spec]
   [jsonista.core :as jsonista]
   [camel-snake-kebab.core :as csk]))

(def json-mapper
  (jsonista/object-mapper
   {:pretty true
    :decode-key-fn csk/->kebab-case-keyword}))

(defn read-value [obj]
  (jsonista/read-value obj json-mapper))
