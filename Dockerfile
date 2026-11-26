# syntax=docker/dockerfile:1.7

ARG JDK_VERSION=17

FROM eclipse-temurin:${JDK_VERSION}-jdk AS builder
WORKDIR /workspace
COPY gradle gradle
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY build-logic build-logic
COPY . .
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :app:bootJar -x test --no-daemon

FROM eclipse-temurin:${JDK_VERSION}-jre AS runner
WORKDIR /app
ENV JAVA_OPTS=""

RUN groupadd --system spring && useradd --system --gid spring spring \
    && mkdir -p /app/logs && chown spring:spring /app/logs

COPY --from=builder --chown=spring:spring /workspace/app/build/libs/app-*.jar /app/app.jar

USER spring
EXPOSE 6033

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
