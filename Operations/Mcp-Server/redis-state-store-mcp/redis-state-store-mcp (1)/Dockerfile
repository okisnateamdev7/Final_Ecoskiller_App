# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /build
COPY pom.xml .
# Pre-download dependencies (cache layer)
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Ecoskiller Platform Engineering"
LABEL description="Redis State Store MCP Server — 18 tools, TLS, tenant-scoped"

# Non-root user
RUN addgroup -S mcpgroup && adduser -S mcpuser -G mcpgroup
USER mcpuser

WORKDIR /app
COPY --from=builder /build/target/redis-state-store-mcp-1.0.0.jar .

# MCP servers communicate via stdio — no EXPOSE needed
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "redis-state-store-mcp-1.0.0.jar"]
