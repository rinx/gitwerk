(ns gitwerk.internal.yaml
  (:require
   [clj-yaml.core :as yaml]))

(defn read-value [obj]
  (yaml/parse-string obj))

(defn write-yaml [obj]
  (yaml/generate-string obj))

(def exports
  {'read-value read-value
   'write-yaml write-yaml})
