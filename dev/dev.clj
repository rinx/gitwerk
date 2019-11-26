(ns dev
  (:require
   [clojure.java.io :as io]
   [clojure.pprint :refer [pprint]]
   [clojure.tools.namespace.repl :as repl
    :refer [refresh refresh-all]]
   [clojure.spec.alpha :as spec]
   [orchestra.spec.test :as stest]))

(stest/instrument)

(defn reset []
  (refresh))
