#!/bin/bash
set -e
echo "Building Ecoskiller Unleash Feature Flag MCP Server..."
SRCDIR="src/main/java"
OUTDIR="target/classes"
JARFILE="target/unleash-mcp-server.jar"
MAIN="com.ecoskiller.unleash.server.UnleashMcpServer"
TEST="com.ecoskiller.unleash.server.UnleashMcpServerTest"
mkdir -p target/classes
SOURCES=$(find "$SRCDIR" -name "*.java" | tr '\n' ' ')
echo "Compiling $(echo $SOURCES | wc -w | tr -d ' ') source files..."
javac -d "$OUTDIR" $SOURCES
printf "Manifest-Version: 1.0\nMain-Class: %s\n" "$MAIN" > target/MANIFEST.MF
jar cfm "$JARFILE" target/MANIFEST.MF -C "$OUTDIR" .
echo ""
echo "✅ Build complete: $JARFILE"
echo ""
echo "Run server    : java -jar $JARFILE"
echo "Run tests     : java -cp $JARFILE $TEST"
echo "Verbose tests : java -cp $JARFILE $TEST --verbose"
echo "With API key  : UNLEASH_MCP_API_KEY=secret java -jar $JARFILE"
