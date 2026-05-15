#!/usr/bin/env bash
# ============================================================
# Ecoskiller | Longhorn MCP Server — Build Script
# No Maven / Gradle required. Pure javac + jar.
# ============================================================
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
SRC="$ROOT/src/main/java"
TEST_SRC="$ROOT/src/test/java"
OUT="$ROOT/out"
JAR="$ROOT/longhorn-mcp-server.jar"
TEST_JAR="$ROOT/longhorn-mcp-tests.jar"

echo "╔══════════════════════════════════════════════════════╗"
echo "║   Ecoskiller Longhorn MCP Server — Build             ║"
echo "╚══════════════════════════════════════════════════════╝"
echo ""

# Require Java 17+
java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [[ "$java_version" -lt 17 ]]; then
  echo "ERROR: Java 17+ required (found: $java_version)"
  exit 1
fi
echo "✓ Java $java_version detected"

# Clean
rm -rf "$OUT"
mkdir -p "$OUT/main" "$OUT/test"

# Compile main sources
echo ""
echo "Compiling main sources..."
export PATH="/usr/lib/jvm/java-21-openjdk-amd64/bin:$PATH"
find "$SRC" -name "*.java" | xargs javac --release 17 -d "$OUT/main"
echo "✓ Compiled"

# Package JAR with Main-Class manifest
echo ""
echo "Packaging JAR..."
jar --create --file="$JAR" \
    --main-class=com.ecoskiller.mcp.longhorn.server.LonghornMcpServer \
    -C "$OUT/main" .
echo "✓ JAR: $JAR"

# Compile tests (depends on main classes)
echo ""
echo "Compiling tests..."
find "$TEST_SRC" -name "*.java" | xargs javac --release 17 -cp "$OUT/main" -d "$OUT/test"
jar --create --file="$TEST_JAR" \
    --main-class=com.ecoskiller.mcp.longhorn.LonghornMcpTests \
    -C "$OUT/main" . -C "$OUT/test" .
echo "✓ Test JAR: $TEST_JAR"

echo ""
echo "Build complete!"
echo ""
echo "Run the server:"
echo "  java -jar $JAR"
echo ""
echo "Run tests:"
echo "  java -jar $TEST_JAR"
