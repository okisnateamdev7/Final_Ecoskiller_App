#!/bin/bash
# Build script for MCP-17 Royalty Server (Java)

set -e

echo "🏗️  Building MCP-17 Royalty Server..."

# Check Java
if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install Java 11 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -1)
echo "✅ Using: $JAVA_VERSION"

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven not found. Please install Maven 3.6 or higher."
    echo "   macOS: brew install maven"
    echo "   Linux: apt-get install maven"
    echo "   Windows: choco install maven"
    exit 1
fi

echo "✅ Maven found: $(mvn -version | head -1)"

# Build
echo ""
echo "📦 Building JAR..."
mvn clean package -q -DskipTests

if [ -f "target/mcp-17-royalty.jar" ]; then
    echo "✅ Build successful!"
    echo ""
    echo "📍 Output: target/mcp-17-royalty.jar"
    echo ""
    echo "🚀 To run:"
    echo "   java -jar target/mcp-17-royalty.jar"
    echo ""
    echo "💻 To connect to Claude Desktop:"
    echo "   Update ~/Library/Application Support/Claude/claude_desktop_config.json"
else
    echo "❌ Build failed"
    exit 1
fi
