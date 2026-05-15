#!/bin/bash
# Quick Start - Run the MCP-17 Royalty Server

set -e

echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                  MCP-17 Royalty Server (Java)                  ║"
echo "║              EcoSkiller | CAT-17 Marketplace & Royalty         ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Step 1: Check Java
echo "📋 Step 1: Checking Java..."
if ! command -v java &> /dev/null; then
    echo "   ❌ Java not installed"
    echo "   📥 Download from: https://adoptopenjdk.net/"
    exit 1
fi
JAVA_VER=$(java -version 2>&1 | grep -oP 'version "\K[^"]*' | head -1)
echo "   ✅ Java $JAVA_VER found"
echo ""

# Step 2: Check Maven
echo "📋 Step 2: Checking Maven..."
if ! command -v mvn &> /dev/null; then
    echo "   ❌ Maven not installed"
    echo "   📥 macOS:   brew install maven"
    echo "   📥 Ubuntu:  sudo apt-get install maven"
    echo "   📥 Windows: choco install maven"
    exit 1
fi
MVN_VER=$(mvn -version 2>&1 | grep "Apache Maven" | head -1)
echo "   ✅ $MVN_VER"
echo ""

# Step 3: Build
echo "📋 Step 3: Building..."
if [ ! -f "pom.xml" ]; then
    echo "   ❌ pom.xml not found. Run from project root."
    exit 1
fi

echo "   ⏳ Compiling Java code..."
mvn clean package -q -DskipTests 2>/dev/null

if [ -f "target/mcp-17-royalty.jar" ]; then
    JAR_SIZE=$(du -h target/mcp-17-royalty.jar | cut -f1)
    echo "   ✅ Build successful! (Size: $JAR_SIZE)"
else
    echo "   ❌ Build failed"
    exit 1
fi
echo ""

# Step 4: Run
echo "📋 Step 4: Starting server..."
echo "   ✅ MCP-17 Royalty Server is ready"
echo ""
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "🚀 Running server..."
echo ""

java -jar target/mcp-17-royalty.jar
