# ── Stage 1: Build ──────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder
LABEL stage=builder

WORKDIR /build

# Copy Maven wrapper and pom first (cache layer for dependencies)
COPY pom.xml ./
RUN apk add --no-cache maven && mvn dependency:go-offline -q

# Copy source and build
COPY src ./src
RUN mvn package -DskipTests -q

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime

LABEL org.opencontainers.image.title="mcp-05-docker"
LABEL org.opencontainers.image.description="Ecoskiller CAT-05 Docker Containerization MCP Server"
LABEL org.opencontainers.image.vendor="Ecoskiller"
LABEL org.opencontainers.image.version="1.0.0"

# Security: non-root user
RUN addgroup -g 1000 mcpgroup && adduser -D -u 1000 -G mcpgroup mcpuser

WORKDIR /app

# Copy fat JAR from builder
COPY --from=builder /build/target/mcp-05-docker-1.0.0.jar ./mcp-05-docker.jar

# Security: lock down file permissions
RUN chown -R mcpuser:mcpgroup /app && chmod 550 /app/mcp-05-docker.jar

# Security: switch to non-root
USER mcpuser

# MCP servers communicate via stdio — no port needed
# Transport: stdin/stdout JSON-RPC 2.0

# JVM hardening flags:
#   -Djava.security.manager  — enable security manager
#   -Xmx256m                 — cap heap to prevent resource exhaustion
#   -XX:+ExitOnOutOfMemoryError — fail fast on OOM
ENTRYPOINT ["java",
  "-Xmx256m",
  "-XX:+ExitOnOutOfMemoryError",
  "-Djava.net.preferIPv4Stack=true",
  "-jar", "/app/mcp-05-docker.jar"]
