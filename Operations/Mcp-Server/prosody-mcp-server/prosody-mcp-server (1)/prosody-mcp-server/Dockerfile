# ── Build stage ───────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build
COPY pom.xml .
# Download dependencies first (layer cache friendly)
RUN mvn dependency:go-offline -q

COPY src/ src/
RUN mvn package -DskipTests -q

# ── Runtime stage ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

# Security: run as non-root user
RUN addgroup -S prosody && adduser -S prosody -G prosody

WORKDIR /app

# Copy fat JAR
COPY --from=builder /build/target/prosody-mcp-server-1.0.0.jar app.jar

# Change ownership
RUN chown prosody:prosody /app/app.jar

USER prosody

# MCP stdio transport — no ports exposed
# Kubernetes health: checked via Prosody HTTP endpoint, not this container

LABEL org.opencontainers.image.title="Ecoskiller Prosody MCP Server"
LABEL org.opencontainers.image.description="Secure Java MCP Server for XMPP Signaling"
LABEL org.opencontainers.image.version="1.0.0"

# JVM security options:
# - no JMX (attack surface)
# - no DNS caching beyond 30s
# - explicit TZ
ENV JAVA_OPTS="-Xms64m -Xmx256m \
    -Djava.security.egd=file:/dev/./urandom \
    -Dnetworkaddress.cache.ttl=30 \
    -Dcom.sun.jndi.ldap.object.trustURLCodebase=false \
    -Dlog4j2.formatMsgNoLookups=true"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
