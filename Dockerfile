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

COPY Makefile Makefile

RUN make

RUN mkdir -p /out/lib \
    && cp $JAVA_HOME/jre/lib/security/cacerts /out

FROM scratch

LABEL maintainer "rinx <rintaro.okamura@gmail.com>"

COPY --from=graalvm /usr/lib64/libstdc++.so.6.0.19 /usr/lib64/libstdc++.so.6
COPY --from=graalvm /out /gitwerk-libs
COPY --from=graalvm /gitwerk/gitwerk /gitwerk

CMD ["/gitwerk", "-Djavax.net.ssl.trustStore=/gitwerk-libs"]
