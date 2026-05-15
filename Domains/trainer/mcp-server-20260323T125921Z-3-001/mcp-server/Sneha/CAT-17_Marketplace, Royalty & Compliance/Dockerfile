# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
# Download dependencies first (layer cache)
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

LABEL org.opencontainers.image.title="EcoSkiller MCP-17 Royalty Server"
LABEL org.opencontainers.image.description="CAT-17 — Marketplace, Royalty & Compliance (18 agents)"
LABEL org.opencontainers.image.version="1.0.0"

WORKDIR /app
COPY --from=build /app/target/mcp-17-royalty-1.0.0-jar-with-dependencies.jar server.jar

# MCP servers communicate over stdio — no exposed port needed
ENTRYPOINT ["java", "-jar", "server.jar"]
