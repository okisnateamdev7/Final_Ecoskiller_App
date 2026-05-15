#!/bin/bash
# ─────────────────────────────────────────────────────────────────
# Jitsi Web Client MCP Server — Run Script
# ─────────────────────────────────────────────────────────────────

JAR_FILE="target/jitsi-web-client-mcp.jar"

if [ ! -f "$JAR_FILE" ]; then
    echo "JAR not found. Running build first..."
    ./build.sh
fi

# Security: Warn if using default secret
if [ -z "$JITSI_MCP_JWT_SECRET" ]; then
    echo "[WARN] JITSI_MCP_JWT_SECRET not set — using insecure default (dev only)" >&2
fi

# JVM security & performance flags
exec java \
    -server \
    -Xms64m -Xmx256m \
    -Djava.security.egd=file:/dev/./urandom \
    -Dfile.encoding=UTF-8 \
    -jar "$JAR_FILE" "$@"
