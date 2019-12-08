FROM oracle/graalvm-ce:19.2.1 as graalvm

LABEL maintainer "rinx <rintaro.okamura@gmail.com>"

RUN gu install native-image
RUN curl -o /usr/bin/lein https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein \
    && chmod a+x /usr/bin/lein

RUN mkdir -p /gitwerk
WORKDIR /gitwerk

COPY project.clj project.clj

RUN lein deps

COPY src src

RUN lein uberjar

COPY reflection.json reflection.json

RUN native-image \
    -jar target/gitwerk-0.1.0-SNAPSHOT-standalone.jar \
    -H:Name=gitwerk \
    -H:+ReportExceptionStackTraces \
    -J-Dclojure.spec.skip-macros=true \
    -J-Dclojure.compiler.direct-linking=true \
    -H:Log=registerResource: \
    -H:ReflectionConfigurationFiles=reflection.json \
    --enable-url-protocols=http,https \
    --enable-all-security-services \
    -H:+JNI \
    --verbose \
    --no-fallback \
    --no-server \
    --report-unsupported-elements-at-runtime \
    --initialize-at-build-time \
    --initialize-at-run-time=org.eclipse.jgit.transport.HttpAuthMethod$Digest \
    -H:IncludeResourceBundles=org.eclipse.jgit.internal.JGitText \
    --allow-incomplete-classpath \
    -J-Xms2g \
    -J-Xmx6g

RUN mkdir -p /out/lib \
    && cp $JAVA_HOME/jre/lib/amd64/libsunec.so /out/lib/ \
    && cp $JAVA_HOME/jre/lib/security/cacerts /out

FROM ubuntu:latest

LABEL maintainer "rinx <rintaro.okamura@gmail.com>"

RUN apt-get update && apt-get install -y git && rm -rf /var/lib/apt/lists/*

COPY --from=graalvm /out /gitwerk-libs
COPY --from=graalvm /gitwerk/gitwerk /gitwerk

CMD ["/gitwerk", "-Djava.library.path=/gitwerk-libs/lib", "-Djavax.net.ssl.trustStore=/gitwerk-libs"]
