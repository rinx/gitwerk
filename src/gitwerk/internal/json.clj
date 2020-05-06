(ns gitwerk.internal.json
  (:require
   [cheshire.core :as cheshire]))

(defn read-value [obj]
  (cheshire/parse-string obj true))

(defn write-json [obj]
  (cheshire/generate-string obj))

(def exports
  {'read-value read-value
   'write-json write-json})
