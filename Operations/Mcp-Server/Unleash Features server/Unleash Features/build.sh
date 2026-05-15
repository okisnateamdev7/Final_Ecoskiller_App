#!/bin/bash
set -e

SRCDIR="src/main/java"
OUTDIR="target/classes"
JARFILE="target/unleash-mcp-server.jar"
MAINCLASS="com.ecoskiller.unleash.server.UnleashMcpServer"
TESTCLASS="com.ecoskiller.unleash.server.UnleashMcpServerTest"

echo "Building Unleash Feature Flag MCP Server..."
mkdir -p "$OUTDIR"

echo "Compiling..."
SOURCES=$(find "$SRCDIR" -name "*.java" | tr '\n' ' ')
javac -d "$OUTDIR" $SOURCES

echo "Packaging..."
printf "Manifest-Version: 1.0\nMain-Class: %s\n" "$MAINCLASS" > target/MANIFEST.MF
jar cfm "$JARFILE" target/MANIFEST.MF -C "$OUTDIR" .

echo ""
echo "✅ Build complete: $JARFILE"
echo ""
echo "Run server    : java -jar $JARFILE"
echo "Run tests     : java -cp $JARFILE $TESTCLASS"
echo "Verbose tests : java -cp $JARFILE $TESTCLASS --verbose"
echo "With API key  : UNLEASH_MCP_API_KEY=secret java -jar $JARFILE"
