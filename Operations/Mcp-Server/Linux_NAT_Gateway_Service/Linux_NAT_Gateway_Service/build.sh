#!/bin/bash
# ─────────────────────────────────────────────────────────────────────────────
# build.sh — Compile and package mcp-nat-gateway
# Ecoskiller | Linux NAT Gateway MCP Server (Java)
# No external dependencies required — pure JDK 17+
# ─────────────────────────────────────────────────────────────────────────────
set -e

SRC_ROOT="src/main/java"
OUT_DIR="target/classes"
JAR_NAME="target/mcp-nat-gateway.jar"
MAIN_CLASS="com.ecoskiller.mcp.nat.server.NatGatewayMcpServer"

echo "🔨 Compiling mcp-nat-gateway..."

mkdir -p "$OUT_DIR" target

# Collect all .java sources
mapfile -t SOURCES < <(find "$SRC_ROOT" -name "*.java")

echo "   Found ${#SOURCES[@]} source files"

javac -d "$OUT_DIR" \
      -source 17 -target 17 \
      --enable-preview \
      "${SOURCES[@]}"

echo "   ✅ Compilation successful"

# Create manifest
echo "Main-Class: $MAIN_CLASS" > target/MANIFEST.MF

# Package JAR
jar cfm "$JAR_NAME" target/MANIFEST.MF -C "$OUT_DIR" .

echo "   ✅ JAR created: $JAR_NAME"
echo ""
echo "🚀 Run with:"
echo "   java --enable-preview -jar $JAR_NAME"
echo ""
echo "🔗 Claude Desktop config snippet:"
echo '   "mcp-nat-gateway": {'
echo '     "command": "java",'
echo '     "args": ["--enable-preview", "-jar", "/absolute/path/to/mcp-nat-gateway/target/mcp-nat-gateway.jar"]'
echo '   }'
