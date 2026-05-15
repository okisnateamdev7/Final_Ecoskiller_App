#!/bin/bash
# build.sh — Compile and package the Trivy MCP Server

set -e

echo "Building Trivy MCP Server..."

SRCDIR="src/main/java"
OUTDIR="target/classes"
JARFILE="target/trivy-mcp-server.jar"
MAINCLASS="com.ecoskiller.trivy.server.TrivyMcpServer"
TESTCLASS="com.ecoskiller.trivy.server.TrivyMcpServerTest"

mkdir -p target/classes

# Find all Java source files
SOURCES=$(find "$SRCDIR" -name "*.java" | tr '\n' ' ')

echo "Compiling..."
javac -d "$OUTDIR" -source 8 -target 8 $SOURCES

echo "Creating manifest..."
mkdir -p target
cat > target/MANIFEST.MF <<EOF
Manifest-Version: 1.0
Main-Class: $MAINCLASS
EOF

echo "Packaging JAR..."
jar cfm "$JARFILE" target/MANIFEST.MF -C "$OUTDIR" .

echo ""
echo "✅ Build complete: $JARFILE"
echo ""
echo "Run server:  java -jar $JARFILE"
echo "Run tests:   java -cp $JARFILE $TESTCLASS"
echo "Run tests v: java -cp $JARFILE $TESTCLASS --verbose"
