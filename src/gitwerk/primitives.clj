(ns gitwerk.primitives
  (:require
    [clojure.pprint :as pprint]
    [clojure.java.shell :as shell]))

(defn getenv [e]
  (System/getenv e))

(def clj-primitives
  {'pprint pprint/pprint
   'getenv getenv
   'sh shell/sh})
