# ─────────────────────────────────────────────────────────────────────────────
# Ecoskiller Apache Airflow MCP Server — Dockerfile
# Multi-stage build | Non-root user | ops namespace (same as Airflow)
# ─────────────────────────────────────────────────────────────────────────────

FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src/ src/
RUN mvn package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S airflowmcp && adduser -S airflowuser -G airflowmcp
WORKDIR /app
COPY --from=builder /build/target/mcp-airflow-service-1.0.0.jar app.jar
RUN mkdir -p /app/logs && chown airflowuser:airflowmcp /app/logs
USER airflowuser

ENV KEYCLOAK_ISSUER="https://keycloak.ecoskiller.internal/realms/ecoskiller"
ENV JWT_STRICT_SIGNATURE="true"
ENV AUDIT_LOG_FILE="/app/logs/airflow-audit.log"

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD pgrep -f "app.jar" > /dev/null || exit 1

ENTRYPOINT ["java",
  "-Dfile.encoding=UTF-8",
  "-Djava.awt.headless=true",
  "-XX:+UseContainerSupport",
  "-XX:MaxRAMPercentage=75.0",
  "-XX:+UseG1GC",
  "-jar", "app.jar"]
