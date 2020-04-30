(ns gitwerk.internal.json
  (:require
   [cheshire.core :as cheshire]
   [camel-snake-kebab.core :as csk]))

(defn read-value [obj]
  (cheshire/parse-string obj csk/->kebab-case-keyword))

(defn write-json [obj]
  (cheshire/generate-string obj))

(def exports
  {'read-value read-value
   'write-json write-json})
