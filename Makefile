XMS = 2g
XMX = 6g

NATIVE_IMAGE_CONFIG_OUTPUT_DIR=native-config

TARGET_JAR=target/gitwerk-0.1.0-SNAPSHOT-standalone.jar

.PHONY: all
all: gitwerk

.PHONY: clean
clean:
	rm -f gitwerk
	rm -rf target

.PHONY: install/native-image
install/native-image:
	gu install native-image

.PHONY: profile/native-image-config
profile/native-image-config: \
	$(NATIVE_IMAGE_CONFIG_OUTPUT_DIR) \
	$(TARGET_JAR)
	java -agentlib:native-image-agent=config-output-dir=$(NATIVE_IMAGE_CONFIG_OUTPUT_DIR) \
	    -jar $(TARGET_JAR) clone http://github.com/rinx/gitwerk
	(cd gitwerk; \
	    java -agentlib:native-image-agent=config-merge-dir=../$(NATIVE_IMAGE_CONFIG_OUTPUT_DIR) \
	    -jar ../$(TARGET_JAR) target/semver patch)
	(cd gitwerk; \
	    java -agentlib:native-image-agent=config-merge-dir=../$(NATIVE_IMAGE_CONFIG_OUTPUT_DIR) \
	    -jar ../$(TARGET_JAR) log)
	(cd gitwerk; \
	    java -agentlib:native-image-agent=config-merge-dir=../$(NATIVE_IMAGE_CONFIG_OUTPUT_DIR) \
	    -jar ../$(TARGET_JAR) tag)
	(cd gitwerk; \
	    java -agentlib:native-image-agent=config-merge-dir=../$(NATIVE_IMAGE_CONFIG_OUTPUT_DIR) \
	    -jar ../$(TARGET_JAR) semver-auto)
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
	-J-Dclojure.spec.skip-macros=true \
	-J-Dclojure.compiler.direct-linking=true \
	-H:Log=registerResource: \
	-H:ConfigurationFileDirectories=$(NATIVE_IMAGE_CONFIG_OUTPUT_DIR) \
	--enable-url-protocols=http,https \
	--enable-all-security-services \
	-H:+JNI \
	--verbose \
	--no-fallback \
	--no-server \
	--report-unsupported-elements-at-runtime \
	--initialize-at-run-time=org.eclipse.jgit.transport.HttpAuthMethod$$Digest \
	--initialize-at-run-time=org.eclipse.jgit.lib.GpgSigner \
	--initialize-at-run-time=io.quarkus.jgit.runtime.PortWatcherRunTime \
	--initialize-at-build-time \
	-H:IncludeResourceBundles=org.eclipse.jgit.internal.JGitText \
	--allow-incomplete-classpath \
	-J-Xms$(XMS) \
	-J-Xmx$(XMX)

$(TARGET_JAR): lein src
	./lein uberjar
