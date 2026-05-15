#!/bin/bash
# ─────────────────────────────────────────────────────────────────────────────
# Ecoskiller mcp-24 — Build & Run Script
# CAT-24: Scoring & Fairness / Security & Compliance / Billing & Quota / Event & Contract
# ─────────────────────────────────────────────────────────────────────────────
set -e
DIR="$(cd "$(dirname "$0")" && pwd)"
SRC="$DIR/src/main/java"
OUT="$DIR/out"

echo "[build] Compiling Java sources..."
mkdir -p "$OUT"
javac -d "$OUT" $(find "$SRC" -name "*.java")
echo "[build] Compilation successful — $(find "$OUT" -name "*.class" | wc -l) class files."

case "${1:-}" in
  test)
    echo "[test] Running agent tests..."
    java -cp "$OUT" com.ecoskiller.mcp.TestAgents "${@:2}"
    ;;
  server)
    echo "[server] Starting MCP server on stdio..."
    java -cp "$OUT" com.ecoskiller.mcp.server.McpServer
    ;;
  *)
    echo ""
    echo "Usage:"
    echo "  ./build.sh test            # Run all 45 tests"
    echo "  ./build.sh test --verbose  # Tests with full JSON output"
    echo "  ./build.sh server          # Start the MCP stdio server"
    echo ""
    ;;
esac
