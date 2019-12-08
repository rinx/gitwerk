(ns gitwerk.service.semver
  (:require
    [clojure.spec.alpha :as spec]
    [clojure.string :as string]))

(def delimiter ".")

(defn parse-int [n]
  (Integer/parseInt n))

(defn parse-refs [s]
  (let [matches (re-seq #"^refs/tags/(.+)" s)]
    (when matches
      (let [matches (nth matches 0)]
        (nth matches 1)))))

(defn str->version [s]
  (let [matches (re-seq #"^(.*)(\d+)\.(\d+)\.(\d+)(.*)$" s)]
    (when matches
      (let [matches (nth matches 0)]
        {:prefix (nth matches 1)
         :major (parse-int (nth matches 2))
         :minor (parse-int (nth matches 3))
         :patch (parse-int (nth matches 4))
         :suffix (nth matches 5)}))))

(defn version->str [{:keys [prefix major minor patch suffix]}]
  (str prefix major delimiter minor delimiter patch suffix))

(defn default-version-str
  ([]
   (default-version-str {:prefix "v"}))
  ([{:keys [prefix suffix]}]
   (-> {:major 0 :minor 0 :patch 0}
       (cond->
        prefix (assoc :prefix prefix)
        suffix (assoc :suffix suffix))
       (version->str))))

(defn patch [v]
  (-> v
      (update :patch inc)))

(defn minor [v]
  (-> v
      (assoc :patch 0)
      (update :minor inc)))

(defn major [v]
  (-> v
      (assoc :patch 0)
      (assoc :minor 0)
      (update :major inc)))

(defn prefix [v new-prefix]
  (assoc v :prefix new-prefix))

(defn suffix [v new-suffix]
  (assoc v :suffix new-suffix))

(comment
  (parse-refs "refs/tags/0.0.1-alpha")

  (str->version "1.2.3")
  (str->version "v1.2.3")
  (str->version "1.2.3-alpha")
  (str->version "v1.2.3-alpha")

  (version->str {:major 1 :minor 2 :patch 3})
  (version->str {:prefix "v" :major 1 :minor 2 :patch 3 :suffix "-alpha"})

  (default-version-str)
  (default-version-str {:suffix "-alpha"})
  (default-version-str {:prefix "v" :suffix "-alpha"})

  (patch (str->version "1.2.3"))
  (minor (str->version "1.2.3"))
  (major (str->version "1.2.3")))
