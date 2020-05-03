(defproject gitwerk "0.1.0-SNAPSHOT"
  :description "A tool for conditional semantic versioning on CI platforms"
  :url "https://github.com/rinx/gitwerk"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.2-alpha1"]
                 [org.clojure/tools.cli "1.0.194"]
                 [cheshire "5.10.0"]
                 [clj-commons/clj-yaml "0.7.1"]
                 [io.quarkus/quarkus-jgit "1.4.1.Final"]
                 [borkdude/sci "0.0.13-alpha.17"]
                 [org.martinklepsch/clj-http-lite "0.4.3"]]
  :profiles {:uberjar {:aot :all
                       :global-vars {*assert* false}
                       :jvm-opts ["-Dclojure.spec.skip-macros=true"
                                  "-Dclojure.compiler.direct-linking=true"]
                       :main gitwerk.core}})
