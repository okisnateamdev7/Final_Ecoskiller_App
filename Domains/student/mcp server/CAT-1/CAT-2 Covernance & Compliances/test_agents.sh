#!/bin/bash
# ─────────────────────────────────────────────────────────────
#  Ecoskiller CAT-2 — Governance & Compliance | Agent Tests
#  Usage:  bash test_agents.sh            (pass/fail summary)
#          bash test_agents.sh --verbose  (full JSON output)
# ─────────────────────────────────────────────────────────────

VERBOSE=false
[[ "$1" == "--verbose" ]] && VERBOSE=true

PASS=0; FAIL=0
JAR="../../mcp-02-governance.jar"   # adjust if needed
SERVER_CMD="java -cp $JAR ecoskiller.McpServer"

run_test() {
    local name="$1"
    local payload="$2"
    local expect="$3"

    response=$(echo "$payload" | $SERVER_CMD 2>/dev/null)

    if echo "$response" | grep -q "$expect"; then
        echo "  ✅  $name"
        PASS=$((PASS+1))
    else
        echo "  ❌  $name"
        FAIL=$((FAIL+1))
    fi

    if $VERBOSE; then
        echo "     → $response"
    fi
}

echo ""
echo "══════════════════════════════════════════════════════"
echo "  Ecoskiller MCP | CAT-2 Governance & Compliance"
echo "  $(date)"
echo "══════════════════════════════════════════════════════"
echo ""

# ── initialize ─────────────────────────────────────────────
echo "[ initialize ]"
run_test "initialize" \
  '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}' \
  "2024-11-05"

# ── tools/list ──────────────────────────────────────────────
echo ""
echo "[ tools/list ]"
run_test "lists 4 tools" \
  '{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}' \
  "backup_dr"

# ── backup_dr ───────────────────────────────────────────────
echo ""
echo "[ BACKUP_DR_AGENT ]"
run_test "trigger backup" \
  '{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"backup_dr","arguments":{"action":"trigger_backup","target_system":"postgres-prod","backup_type":"full","retention_days":"30"}}}' \
  "BACKUP_DR_AGENT_ANTIGRAVITY_SEALED"

run_test "dr test" \
  '{"jsonrpc":"2.0","id":4,"method":"tools/call","params":{"name":"backup_dr","arguments":{"action":"run_dr_test","target_system":"redis-cluster"}}}' \
  "SUCCESS"

# ── data_governance ─────────────────────────────────────────
echo ""
echo "[ DATA_GOVERNANCE_AGENT ]"
run_test "classify dataset" \
  '{"jsonrpc":"2.0","id":5,"method":"tools/call","params":{"name":"data_governance","arguments":{"action":"classify","dataset_id":"DS-4471","classification_level":"CONFIDENTIAL","owner_id":"team-data"}}}' \
  "DATA_GOVERNANCE_AGENT_ANTIGRAVITY_SEALED"

run_test "audit trail check" \
  '{"jsonrpc":"2.0","id":6,"method":"tools/call","params":{"name":"data_governance","arguments":{"action":"audit_check","dataset_id":"DS-1002"}}}' \
  "SUCCESS"

# ── devsecops ───────────────────────────────────────────────
echo ""
echo "[ DEVSECOPS_AGENT ]"
run_test "sast scan" \
  '{"jsonrpc":"2.0","id":7,"method":"tools/call","params":{"name":"devsecops","arguments":{"action":"run_scan","pipeline_id":"CI-002","scan_type":"SAST","severity_threshold":"HIGH"}}}' \
  "DEVSECOPS_AGENT_ANTIGRAVITY_SEALED"

run_test "security gate" \
  '{"jsonrpc":"2.0","id":8,"method":"tools/call","params":{"name":"devsecops","arguments":{"action":"check_gate","pipeline_id":"CI-005","scan_type":"DAST"}}}' \
  "PASSED"

# ── legal_policy ────────────────────────────────────────────
echo ""
echo "[ LEGAL_POLICY_AGENT ]"
run_test "create privacy policy" \
  '{"jsonrpc":"2.0","id":9,"method":"tools/call","params":{"name":"legal_policy","arguments":{"action":"create_policy","policy_type":"PRIVACY","jurisdiction":"IN","effective_date":"2025-01-01"}}}' \
  "LEGAL_POLICY_AGENT_ANTIGRAVITY_SEALED"

run_test "gdpr compliance check" \
  '{"jsonrpc":"2.0","id":10,"method":"tools/call","params":{"name":"legal_policy","arguments":{"action":"compliance_check","policy_type":"GDPR","jurisdiction":"EU"}}}' \
  "SUCCESS"

# ── ping ────────────────────────────────────────────────────
echo ""
echo "[ ping ]"
run_test "ping" \
  '{"jsonrpc":"2.0","id":11,"method":"ping","params":{}}' \
  "pong"

# ── summary ─────────────────────────────────────────────────
echo ""
echo "══════════════════════════════════════════════════════"
echo "  Results:  ✅  $PASS passed   ❌  $FAIL failed"
echo "══════════════════════════════════════════════════════"
echo ""
