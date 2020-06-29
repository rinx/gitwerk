XMS = 2g
XMX = 7g

NATIVE_IMAGE_CONFIG_OUTPUT_DIR=native-config

TARGET_JAR=target/gitwerk-0.1.0-SNAPSHOT-standalone.jar

ADDITIONAL_OPTIONS=""

.PHONY: all
all: gitwerk

.PHONY: clean
clean:
	rm -f gitwerk
	rm -rf target

.PHONY: profile/native-image-config
profile/native-image-config: \
	$(NATIVE_IMAGE_CONFIG_OUTPUT_DIR) \
	$(TARGET_JAR)
	java -agentlib:native-image-agent=config-output-dir=$(NATIVE_IMAGE_CONFIG_OUTPUT_DIR) \
	    -jar $(TARGET_JAR) clone http://github.com/rinx/gitwerk
	(cd gitwerk; \
	    java -agentlib:native-image-agent=config-merge-dir=../$(NATIVE_IMAGE_CONFIG_OUTPUT_DIR) \
	    -jar ../$(TARGET_JAR) log)
	(cd gitwerk; \
	    java -agentlib:native-image-agent=config-merge-dir=../$(NATIVE_IMAGE_CONFIG_OUTPUT_DIR) \
	    -jar ../$(TARGET_JAR) tag)
	rm -rf gitwerk

$(NATIVE_IMAGE_CONFIG_OUTPUT_DIR):
	mkdir -p $@

lein:
	curl -o lein https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein \
	&& chmod a+x lein \
	&& ./lein version

gitwerk: \
	$(TARGET_JAR)
	native-image \
	-jar $(TARGET_JAR) \
	-H:Name=gitwerk \
	-H:+ReportExceptionStackTraces \
	-H:Log=registerResource: \
	-H:ReflectionConfigurationFiles=reflection.json \
	-H:+RemoveSaturatedTypeFlows \
	--enable-url-protocols=http,https \
	--enable-all-security-services \
	--install-exit-handlers \
	-H:+JNI \
	--verbose \
	--no-fallback \
	--no-server \
	--report-unsupported-elements-at-runtime \
	--initialize-at-run-time=org.eclipse.jgit.transport.HttpAuthMethod$$Digest \
	--initialize-at-run-time=org.eclipse.jgit.lib.GpgSigner \
	--initialize-at-run-time=io.quarkus.jsch.runtime.PortWatcherRunTime \
	--initialize-at-build-time \
	-H:IncludeResourceBundles=org.eclipse.jgit.internal.JGitText \
	--allow-incomplete-classpath \
	-J-Dclojure.spec.skip-macros=true \
	-J-Dclojure.compiler.direct-linking=true \
	-J-Xms$(XMS) \
	-J-Xmx$(XMX)

.PHONY: gitwerk-static
gitwerk-static: \
	$(TARGET_JAR)
	native-image \
	-jar $(TARGET_JAR) \
	-H:Name=gitwerk \
	-H:+ReportExceptionStackTraces \
	-H:Log=registerResource: \
	-H:ReflectionConfigurationFiles=reflection.json \
	-H:+RemoveSaturatedTypeFlows \
	--enable-url-protocols=http,https \
	--enable-all-security-services \
	--install-exit-handlers \
	-H:+JNI \
	--verbose \
	--no-fallback \
	--no-server \
	--report-unsupported-elements-at-runtime \
	--initialize-at-run-time=org.eclipse.jgit.transport.HttpAuthMethod$$Digest \
	--initialize-at-run-time=org.eclipse.jgit.lib.GpgSigner \
	--initialize-at-run-time=io.quarkus.jsch.runtime.PortWatcherRunTime \
	--initialize-at-build-time \
	-H:IncludeResourceBundles=org.eclipse.jgit.internal.JGitText \
	--allow-incomplete-classpath \
	--static \
	-J-Dclojure.spec.skip-macros=true \
	-J-Dclojure.compiler.direct-linking=true \
	-J-Xms$(XMS) \
	-J-Xmx$(XMX)

$(TARGET_JAR): lein src
	./lein uberjar
