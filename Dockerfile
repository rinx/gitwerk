ARG GRAALVM_VERSION=latest

FROM oracle/graalvm-ce:${GRAALVM_VERSION} as graalvm

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

COPY native-config native-config

RUN lein native-image

RUN mkdir -p /out/lib \
    && cp $JAVA_HOME/jre/lib/amd64/libsunec.so /out/lib/ \
    && cp $JAVA_HOME/jre/lib/security/cacerts /out

FROM ubuntu:latest

LABEL maintainer "rinx <rintaro.okamura@gmail.com>"

RUN apt-get update && apt-get install -y git && rm -rf /var/lib/apt/lists/*

COPY --from=graalvm /out /gitwerk-libs
COPY --from=graalvm /gitwerk/gitwerk /gitwerk

CMD ["/gitwerk", "-Djava.library.path=/gitwerk-libs/lib", "-Djavax.net.ssl.trustStore=/gitwerk-libs"]
