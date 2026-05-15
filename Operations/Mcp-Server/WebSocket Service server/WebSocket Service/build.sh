#!/bin/bash
set -e
echo "Building Ecoskiller WebSocket Service MCP Server..."
SRCDIR="src/main/java"
OUTDIR="target/classes"
JARFILE="target/websocket-service-mcp.jar"
MAIN="com.ecoskiller.websocket.server.WebSocketMcpServer"
TEST="com.ecoskiller.websocket.server.WebSocketMcpServerTest"
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
