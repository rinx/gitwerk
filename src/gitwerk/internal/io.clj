(ns gitwerk.internal.io
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string])
  (:import
    [java.io BufferedReader]))

(defn read-from-stdin []
  (->> (BufferedReader. *in*)
       (line-seq)
       (string/join "\n")))

(defn safe-read
  [file]
  (if (.exists (io/file file))
    (slurp file)
    (throw
      (Exception.
        (str "File not found: " file)))))

(def exports
  {'read-from-stdin read-from-stdin
   'safe-read safe-read})
