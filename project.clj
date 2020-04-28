(defproject gitwerk "0.1.0-SNAPSHOT"
  :description "A tool for conditional semantic versioning on CI platforms"
  :url "https://github.com/rinx/gitwerk"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.2-alpha1"]
                 [org.clojure/tools.cli "0.4.2"]
                 [metosin/jsonista "0.2.2"]
                 [camel-snake-kebab "0.4.0"]
                 [io.quarkus/quarkus-jgit "1.1.0.CR1"]
                 [borkdude/sci "0.0.13-alpha.17"]
                 [org.martinklepsch/clj-http-lite "0.4.3"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [orchestra "2019.02.06-1"]]
                   :source-paths ["dev"]}
             :uberjar {:aot :all
                       :main gitwerk.core}})
