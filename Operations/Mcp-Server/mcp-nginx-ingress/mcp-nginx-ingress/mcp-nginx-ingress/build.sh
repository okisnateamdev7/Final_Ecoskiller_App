#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────
#  build.sh  —  Compile and optionally run the NGINX MCP Server
# ─────────────────────────────────────────────────────────────
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
SRC="$ROOT/src/main/java"
OUT="$ROOT/out"

echo "=== Building mcp-nginx-ingress ==="
mkdir -p "$OUT"

# Collect all .java files
SOURCES=$(find "$SRC" -name "*.java" | tr '\n' ' ')

javac -source 11 -target 11 -d "$OUT" $SOURCES
echo "✓ Compiled to $OUT"

# Create executable jar with manifest
cd "$OUT"
cat > manifest.mf <<EOF
Main-Class: com.ecoskiller.mcp.nginx.NginxIngressMcpServer
EOF
jar cfm "$ROOT/mcp-nginx-ingress.jar" manifest.mf .
echo "✓ Built $ROOT/mcp-nginx-ingress.jar"

if [[ "${1:-}" == "--test" ]]; then
    echo ""
    echo "=== Running tests ==="
    java -cp "$OUT" com.ecoskiller.mcp.nginx.NginxIngressMcpTestRunner "${2:-}"
fi

echo ""
echo "=== Done ==="
echo ""
echo "Start server:"
echo "  java -jar mcp-nginx-ingress.jar"
echo ""
echo "Or via Maven wrapper:"
echo "  java -cp out com.ecoskiller.mcp.nginx.NginxIngressMcpServer"
