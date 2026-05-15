# ── Stage 1: Build ───────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src/ src/
RUN mvn package -DskipTests -q

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

# Security: non-root user
RUN addgroup -S recruiter && adduser -S recruiter -G recruiter

WORKDIR /app
COPY --from=builder /build/target/recruiter-mcp-server-1.0.0.jar app.jar
RUN chown recruiter:recruiter /app/app.jar

USER recruiter

LABEL org.opencontainers.image.title="Ecoskiller Recruiter MCP Server"
LABEL org.opencontainers.image.description="Secure Java MCP Server — Talent Acquisition & Hiring Management"
LABEL org.opencontainers.image.version="1.0.0"

# JVM hardening: no JMX, short DNS TTL, Log4Shell mitigation
ENV JAVA_OPTS="-Xms64m -Xmx256m \
    -Djava.security.egd=file:/dev/./urandom \
    -Dnetworkaddress.cache.ttl=30 \
    -Dcom.sun.jndi.ldap.object.trustURLCodebase=false \
    -Dlog4j2.formatMsgNoLookups=true"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
