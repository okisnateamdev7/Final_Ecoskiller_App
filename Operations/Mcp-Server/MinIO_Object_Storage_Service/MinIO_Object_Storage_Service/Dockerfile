# Multi-stage build for MinIO MCP Server
# Stage 1: Builder
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy source code
COPY pom.xml .
COPY src/ ./src/

# Build the fat JAR
RUN mvn clean package -DskipTests -q

# Verify build
RUN test -f target/minio-mcp-server.jar || exit 1 && \
    echo "✅ Build successful: $(ls -lh target/minio-mcp-server.jar | awk '{print $5}')"

# Stage 2: Runtime
FROM eclipse-temurin:17-alpine

LABEL maintainer="Ecoskiller DevOps <devops@ecoskiller.com>"
LABEL description="Secure MinIO Object Storage MCP Server - Production Ready"
LABEL version="2.0"

# Install runtime dependencies
RUN apk add --no-cache \
    ca-certificates \
    curl \
    tini \
    dumb-init

# Create non-root user
RUN addgroup -S minio && adduser -S minio -G minio

# Set working directory
WORKDIR /app

# Copy JAR from builder
COPY --from=builder /build/target/minio-mcp-server.jar .

# Create directories for logs and data
RUN mkdir -p /var/log/minio-mcp && \
    chown -R minio:minio /app /var/log/minio-mcp

# Security: Change ownership to non-root
USER minio

# Set environment variables
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+ParallelRefProcEnabled -XX:+AlwaysPreTouch -Xms256m -Xmx512m"
ENV MINIO_ENDPOINT="minio.default.svc.cluster.local:9000"
ENV MINIO_USE_SSL="true"
ENV LOG_LEVEL="INFO"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:9000/health || exit 1

# Use tini as init system
ENTRYPOINT ["/sbin/tini", "--"]

# Default command - run the MCP server
CMD ["java", "${JAVA_OPTS}", "-jar", "minio-mcp-server.jar"]

# Metadata
EXPOSE 9000
