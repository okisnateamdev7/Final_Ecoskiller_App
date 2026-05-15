# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src/ src/
RUN mvn package -DskipTests -q

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S scoring && adduser -S scoring -G scoring
WORKDIR /app
COPY --from=builder /build/target/scoring-engine-mcp-server-1.0.0.jar app.jar
RUN chown scoring:scoring /app/app.jar
USER scoring

LABEL org.opencontainers.image.title="Ecoskiller Scoring Engine MCP Server"
LABEL org.opencontainers.image.description="Secure Java MCP Server — AI-Powered Assessment Scoring"
LABEL org.opencontainers.image.version="1.0.0"

ENV JAVA_OPTS="-Xms64m -Xmx256m \
    -Djava.security.egd=file:/dev/./urandom \
    -Dnetworkaddress.cache.ttl=30 \
    -Dcom.sun.jndi.ldap.object.trustURLCodebase=false \
    -Dlog4j2.formatMsgNoLookups=true"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
