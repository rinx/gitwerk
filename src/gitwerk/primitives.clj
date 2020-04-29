(ns gitwerk.primitives
  (:require
    [clojure.pprint :as pprint]))

(defn getenv [e]
  (System/getenv e))

(def clj-primitives
  {'pprint pprint/pprint
   'getenv getenv})
