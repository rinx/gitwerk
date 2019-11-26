(ns user
  (:require
   [clojure.tools.namespace.repl :as repl
    :refer [refresh refresh-all]]))

(repl/set-refresh-dirs "src" "test" "dev")

(defn dev []
  (require 'dev)
  (in-ns 'dev)
  :ok)
