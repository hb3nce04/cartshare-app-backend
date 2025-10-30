FROM azul/zulu-openjdk-alpine:21
ARG JAR_FILE=target/cartshare-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} cartshare-0.0.1-SNAPSHOT.jar
ENTRYPOINT java $JVM_OPTS -jar cartshare-0.0.1-SNAPSHOT.jar
