#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────
#  Ecoskiller ModSecurity WAF MCP Server — Build & Run
# ─────────────────────────────────────────────────────────
set -e

SRC="src/com/ecoskiller/waf"
OUT="out"
PKG="com.ecoskiller.waf"

echo "▶ Compiling..."
mkdir -p "$OUT"
javac -encoding UTF-8 -d "$OUT" "$SRC/McpWafServer.java"
echo "✓ Compiled to $OUT/"

# Check what action to take
case "${1:-run}" in
  run)
    echo "▶ Starting MCP server (stdin/stdout JSON-RPC 2.0)..."
    java -cp "$OUT" "${PKG}.McpWafServer"
    ;;
  test)
    echo "▶ Compiling tests..."
    javac -encoding UTF-8 -cp "$OUT" -d "$OUT" "$SRC/test_agents.java"
    echo "▶ Running tests..."
    java -cp "$OUT" "${PKG}.test_agents" "${@:2}"
    ;;
  jar)
    echo "▶ Building JAR..."
    jar cfe mcp-waf-modsecurity.jar "${PKG}.McpWafServer" -C "$OUT" .
    echo "✓ Created mcp-waf-modsecurity.jar"
    echo "  Run: java -jar mcp-waf-modsecurity.jar"
    ;;
  *)
    echo "Usage: $0 [run|test|jar] [--verbose]"
    ;;
esac
