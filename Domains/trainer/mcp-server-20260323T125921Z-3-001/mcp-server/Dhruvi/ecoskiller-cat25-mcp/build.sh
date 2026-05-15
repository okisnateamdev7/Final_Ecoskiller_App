#!/bin/bash
# ─────────────────────────────────────────────────────────────────────────────
# Ecoskiller mcp-25 — Build & Run Script
# CAT-25: Analytics & ERP / DevOps & Environment
# ─────────────────────────────────────────────────────────────────────────────
set -e
DIR="$(cd "$(dirname "$0")" && pwd)"
SRC="$DIR/src/main/java"
OUT="$DIR/out"

echo "[build] Compiling Java sources..."
mkdir -p "$OUT"
javac -d "$OUT" $(find "$SRC" -name "*.java")
echo "[build] Done — $(find "$OUT" -name "*.class" | wc -l) class files."

case "${1:-}" in
  test)
    echo "[test] Running all agent tests..."
    java -cp "$OUT" com.ecoskiller.mcp.TestAgents "${@:2}"
    ;;
  server)
    echo "[server] Starting MCP server (stdio)..."
    java -cp "$OUT" com.ecoskiller.mcp.server.McpServer
    ;;
  *)
    echo ""
    echo "Usage:"
    echo "  ./build.sh test            # Run all 27 tests"
    echo "  ./build.sh test --verbose  # Tests with full JSON output"
    echo "  ./build.sh server          # Start the MCP stdio server"
    echo ""
    ;;
esac
