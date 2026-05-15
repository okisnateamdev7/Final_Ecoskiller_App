# ─── Stage 1: Build ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Cache dependencies before copying source
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -q 2>/dev/null || true

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests -q

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
# eclipse-temurin:21-jre-alpine — ~60-70% smaller than JDK image (per Ecoskiller infra v15)
FROM eclipse-temurin:21-jre-alpine AS runtime

# Security: create non-root user (Ecoskiller k8s PodSecurityStandard baseline)
RUN addgroup -S mcp && adduser -S mcp -G mcp

WORKDIR /app

# Copy only the fat JAR
COPY --from=builder /build/target/licensing-contract-mcp-1.0.0.jar app.jar

# OpenTelemetry Java Agent (Ecoskiller infra v15 — zero code change instrumentation)
# Uncomment when deploying to k8s with Jaeger:
# ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar /app/otel-agent.jar

# Security: run as non-root
USER mcp

# MCP stdio transport — no exposed ports needed
# ENV vars from Kubernetes ConfigMap / Secrets:
#   KEYCLOAK_ISSUER_URL, MINIO_ENDPOINT, MINIO_BUCKET_CONTRACTS
#   DEFAULT_MIN_ROYALTY_RATE, DEFAULT_MAX_ROYALTY_RATE
#   DEFAULT_MIN_TERM_YEARS, DEFAULT_MAX_TERM_YEARS
#   SIGNATURE_TTL_DAYS, MINOR_CACHE_TTL_SECONDS
#   RATE_CONFIG_CACHE_TTL_SECONDS

# JVM tuning for container-aware memory limits (k8s resource limits: 512Mi)
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+ExitOnOutOfMemoryError \
               -Djava.security.egd=file:/dev/./urandom \
               -Dfile.encoding=UTF-8"

# OpenTelemetry (uncomment with agent):
# ENV JAVA_TOOL_OPTIONS="-javaagent:/app/otel-agent.jar"
# ENV OTEL_SERVICE_NAME=licensing-contract-service-mcp
# ENV OTEL_EXPORTER_OTLP_ENDPOINT=http://jaeger-collector.monitoring.svc.cluster.local:4317

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
