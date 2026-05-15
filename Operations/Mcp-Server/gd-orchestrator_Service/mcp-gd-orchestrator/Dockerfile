# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build
RUN apk add --no-cache maven
COPY pom.xml ./
RUN mvn dependency:go-offline -q 2>/dev/null || true
COPY src ./src
RUN javac --release 17 -d out \
  src/main/java/io/ecoskiller/mcp/gd/json/Json.java \
  src/main/java/io/ecoskiller/mcp/gd/GDMCPServer.java \
  src/main/java/io/ecoskiller/mcp/gd/GDAgents.java && \
  jar cfe mcp-gd-orchestrator-1.0.0.jar io.ecoskiller.mcp.gd.GDMCPServer -C out .

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine AS runtime
LABEL org.opencontainers.image.title="mcp-gd-orchestrator"
LABEL org.opencontainers.image.description="Ecoskiller GD Orchestrator MCP Server"
LABEL org.opencontainers.image.vendor="Ecoskiller"

RUN addgroup -g 1000 mcpgroup && adduser -D -u 1000 -G mcpgroup mcpuser
WORKDIR /app
COPY --from=builder /build/mcp-gd-orchestrator-1.0.0.jar ./mcp-gd-orchestrator.jar
RUN chown mcpuser:mcpgroup /app/mcp-gd-orchestrator.jar && chmod 550 /app/mcp-gd-orchestrator.jar
USER mcpuser

ENTRYPOINT ["java", "-Xmx256m", "-XX:+ExitOnOutOfMemoryError",
            "-jar", "/app/mcp-gd-orchestrator.jar"]
