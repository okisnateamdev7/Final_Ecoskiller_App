# ─────────────────────────────────────────────────────────────────────────────
# Ecoskiller Admin MCP Server — Dockerfile
# Multi-stage build | Non-root user | Minimal attack surface
# ─────────────────────────────────────────────────────────────────────────────

# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copy pom first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source
COPY src/ src/

# Build fat JAR
RUN mvn package -DskipTests -q

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

# Security: run as non-root
RUN addgroup -S mcpgroup && adduser -S mcpuser -G mcpgroup

WORKDIR /app

# Copy fat JAR from builder
COPY --from=builder /build/target/mcp-admin-service-1.0.0.jar app.jar

# Audit log directory (writable by mcpuser)
RUN mkdir -p /app/logs && chown mcpuser:mcpgroup /app/logs

USER mcpuser

# ── Environment variables ─────────────────────────────────────────────────────
# Override at runtime via docker -e or Kubernetes secrets
ENV KEYCLOAK_ISSUER="https://keycloak.ecoskiller.internal/realms/ecoskiller"
ENV JWT_STRICT_SIGNATURE="true"
ENV AUDIT_LOG_FILE="/app/logs/audit.log"

# ── Health check ──────────────────────────────────────────────────────────────
# MCP uses stdio — no HTTP port needed.
# Health is checked at the deployment level (process alive).
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD pgrep -f "app.jar" > /dev/null || exit 1

# ── Entrypoint ────────────────────────────────────────────────────────────────
# JVM hardening flags:
#   -Djava.security.manager — enable security manager
#   -Dfile.encoding=UTF-8   — explicit encoding
ENTRYPOINT ["java", \
  "-Dfile.encoding=UTF-8", \
  "-Djava.awt.headless=true", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
