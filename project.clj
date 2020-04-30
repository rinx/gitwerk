(defproject gitwerk "0.1.0-SNAPSHOT"
  :description "A tool for conditional semantic versioning on CI platforms"
  :url "https://github.com/rinx/gitwerk"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.2-alpha1"]
                 [org.clojure/tools.cli "1.0.194"]
                 [cheshire "5.10.0"]
                 [io.quarkus/quarkus-jgit "1.4.1.Final"]
                 [borkdude/sci "0.0.13-alpha.17"]
                 [org.martinklepsch/clj-http-lite "0.4.3"]]
  :plugins [[io.taylorwood/lein-native-image "0.3.1"]]
  :native-image {:name "gitwerk"
                 :opts ["-H:+ReportExceptionStackTraces"
                        "-H:Log=registerResource:"
                        "-H:ReflectionConfigurationFiles=reflection.json"
                        "--enable-url-protocols=http,https"
                        "--enable-all-security-services"
                        "-H:+JNI"
                        "--verbose"
                        "--no-fallback"
                        "--no-server"
                        "--report-unsupported-elements-at-runtime"
                        "--initialize-at-run-time=org.eclipse.jgit.transport.HttpAuthMethod$Digest"
                        "--initialize-at-run-time=org.eclipse.jgit.lib.GpgSigner"
                        "--initialize-at-run-time=io.quarkus.jsch.runtime.PortWatcherRunTime"
                        "--initialize-at-build-time"
                        "-H:IncludeResourceBundles=org.eclipse.jgit.internal.JGitText"
                        "--allow-incomplete-classpath"
                        "-J-Dclojure.spec.skip-macros=true"
                        "-J-Dclojure.compiler.direct-linking=true"
                        "-J-Xms2g"
                        "-J-Xmx7g"]}
  :profiles {:uberjar {:aot :all
                       :global-vars {*assert* false}
                       :jvm-opts ["-Dclojure.spec.skip-macros=true"
                                  "-Dclojure.compiler.direct-linking=true"]
                       :main gitwerk.core}})
