#!/bin/bash
# ─────────────────────────────────────────────────────────────────
# Jitsi Web Client MCP Server — Build & Run Script
# Ecoskiller Platform | No external dependencies required
# ─────────────────────────────────────────────────────────────────

set -e

SERVER_NAME="jitsi-web-client-mcp"
MAIN_CLASS="io.ecoskiller.mcp.server.JitsiMcpServer"
SRC_DIR="src/main/java"
OUT_DIR="target/classes"
JAR_FILE="target/${SERVER_NAME}.jar"

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  Jitsi Web Client MCP Server — Build"
echo "  Ecoskiller Platform | Java 17+"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Locate javac — try PATH first, then common JDK install locations
if command -v javac &>/dev/null; then
    JAVAC=$(command -v javac)
else
    for candidate in \
        /usr/lib/jvm/java-21-openjdk-amd64/bin/javac \
        /usr/lib/jvm/java-21-openjdk/bin/javac \
        /usr/lib/jvm/java-17-openjdk-amd64/bin/javac \
        /usr/lib/jvm/java-17-openjdk/bin/javac \
        /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/javac \
        /Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/bin/javac; do
        if [ -x "$candidate" ]; then JAVAC="$candidate"; break; fi
    done
fi

if [ -z "$JAVAC" ]; then
    echo "ERROR: javac not found. Please install JDK 17+ (not just JRE)."
    echo "  Ubuntu/Debian: sudo apt-get install openjdk-21-jdk"
    echo "  macOS:         brew install --cask temurin"
    echo "  Windows:       https://adoptium.net/"
    exit 1
fi

JAVA_VERSION=$("$JAVAC" -version 2>&1 | sed 's/javac \([0-9]*\).*/\1/')
echo "[1/4] javac ${JAVA_VERSION} detected ✓  ($JAVAC)"

mkdir -p "$OUT_DIR"
echo "[2/4] Output directory ready ✓"

SOURCES=$(find "$SRC_DIR" -name "*.java" | tr '\n' ' ')

echo "[3/4] Compiling..."
"$JAVAC" -source 17 -target 17 -d "$OUT_DIR" $SOURCES
echo "      Compilation successful ✓"

mkdir -p target
cat > target/MANIFEST.MF << EOF
Manifest-Version: 1.0
Main-Class: ${MAIN_CLASS}
Class-Path: .
Created-By: Ecoskiller Jitsi MCP Build
EOF

jar cfm "$JAR_FILE" target/MANIFEST.MF -C "$OUT_DIR" .
echo "[4/4] JAR created: $JAR_FILE ✓"

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  Build complete!"
echo ""
echo "  Run the server:"
echo "    ./run.sh"
echo ""
echo "  Set JWT secret (required for production):"
echo "    export JITSI_MCP_JWT_SECRET='your-secret-key-here'"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
