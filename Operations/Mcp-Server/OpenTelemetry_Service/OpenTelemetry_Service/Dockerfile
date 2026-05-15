# Multi-stage build for OpenTelemetry MCP Server

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom.xml
COPY pom.xml .

# Download dependencies
RUN mvn dependency:download-sources dependency:download-javadoc

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy

LABEL maintainer="EcoSkiller <ops@ecoskiller.com>"
LABEL description="OpenTelemetry MCP Server - Secure Distributed Tracing"
LABEL version="1.0.0"

# Create non-root user
RUN groupadd -r otel && useradd -r -g otel otel

# Install necessary tools
RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Create application directory
WORKDIR /opt/otel-mcp-server

# Copy JAR from builder
COPY --from=builder /build/target/otel-mcp-server-1.0.0-jar-with-dependencies.jar ./app.jar

# Create logs directory
RUN mkdir -p /opt/otel-mcp-server/logs && \
    chown -R otel:otel /opt/otel-mcp-server

# Create directories for TLS certificates
RUN mkdir -p /etc/otel-mcp-server/certs && \
    chown -R otel:otel /etc/otel-mcp-server

# Switch to non-root user
USER otel

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:9090 -X POST \
        -H "Content-Type: application/json" \
        -d '{"jsonrpc":"2.0","method":"ping","id":"health"}' || exit 1

# Set environment variables
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
ENV SERVER_HOST="0.0.0.0"
ENV SERVER_PORT="9090"
ENV ENVIRONMENT="production"
ENV OTEL_EXPORTER_OTLP_ENDPOINT="http://otel-collector.ops.svc.cluster.local:4317"
ENV SAMPLING_RATE="0.1"
ENV TLS_ENABLED="true"
ENV LOG_LEVEL="INFO"

# Expose ports
EXPOSE 9090

# Run the application
ENTRYPOINT ["java"]
CMD ["-server", \
     "-Duser.timezone=UTC", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-Dlogging.level=INFO", \
     "${JAVA_OPTS}", \
     "-jar", "app.jar"]
