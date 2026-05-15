# ─────────────────────────────────────────────────────────────────────────────
# Stage 1: Build
# ─────────────────────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build
COPY pom.xml .
# Download deps in a separate layer (cached unless pom.xml changes)
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -q -DskipTests

# ─────────────────────────────────────────────────────────────────────────────
# Stage 2: Runtime (minimal JRE — reduces attack surface + image size)
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

# Non-root user — Ecoskiller security baseline requirement
RUN addgroup -S mcpgroup && adduser -S mcpuser -G mcpgroup

# Log directory (writable by mcpuser)
RUN mkdir -p /var/log/ecoskiller && chown mcpuser:mcpgroup /var/log/ecoskiller

WORKDIR /app
COPY --from=builder /build/target/mcp-harbor-service.jar .

USER mcpuser

# MCP server runs on stdio — no port binding required
# Environment variables injected by Kubernetes Secrets in ops namespace:
#   HARBOR_URL, HARBOR_USER, HARBOR_PASSWORD
# Optional:
#   HARBOR_PROJECT         (default: ecoskiller)
#   HARBOR_API_TIMEOUT_MS  (default: 10000)
#   HARBOR_TLS_VERIFY      (default: true; set "false" for local dev only)
#   HARBOR_MCP_AUDIT_LOG   (default: /var/log/ecoskiller/harbor-mcp-audit.log)

ENTRYPOINT ["java", \
  "-Xmx256m", \
  "-Xms64m", \
  "-Djava.util.logging.SimpleFormatter.format=%1$tF %1$tT [%4$s] %2$s %5$s%6$s%n", \
  "-jar", "mcp-harbor-service.jar"]

LABEL org.opencontainers.image.title="mcp-harbor-service" \
      org.opencontainers.image.description="Ecoskiller Harbor Registry MCP Server — 12 agents" \
      org.opencontainers.image.version="1.0.0" \
      org.opencontainers.image.source="harbor.ecoskiller.internal/ecoskiller/mcp-harbor-service"
