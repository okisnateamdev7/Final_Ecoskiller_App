#!/bin/bash
set -e
echo "Building Ecoskiller user-service MCP Server..."
SRCDIR="src/main/java"
OUTDIR="target/classes"
JARFILE="target/user-service-mcp.jar"
MAIN="com.ecoskiller.userservice.server.UserMcpServer"
TEST="com.ecoskiller.userservice.server.UserMcpServerTest"
mkdir -p target/classes
SOURCES=$(find "$SRCDIR" -name "*.java" | tr '\n' ' ')
COUNT=$(echo $SOURCES | wc -w | tr -d ' ')
echo "Compiling $COUNT source files..."
javac -d "$OUTDIR" $SOURCES
printf "Manifest-Version: 1.0\nMain-Class: %s\n" "$MAIN" > target/MANIFEST.MF
jar cfm "$JARFILE" target/MANIFEST.MF -C "$OUTDIR" .
echo ""
echo "✅ Build complete: $JARFILE"
echo ""
echo "Run server    : java -jar $JARFILE"
echo "Run tests     : java -cp $JARFILE $TEST"
echo "Verbose tests : java -cp $JARFILE $TEST --verbose"
echo "With API key  : USERSERVICE_MCP_API_KEY=secret java -jar $JARFILE"
