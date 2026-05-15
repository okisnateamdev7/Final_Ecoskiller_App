#!/bin/bash
# ─────────────────────────────────────────────────────────────
#  Ecoskiller CAT-7 — One-Click Integrations | Agent Tests
#  Usage:  bash test_agents.sh            (pass/fail summary)
#          bash test_agents.sh --verbose  (full JSON output)
# ─────────────────────────────────────────────────────────────

VERBOSE=false
[[ "$1" == "--verbose" ]] && VERBOSE=true

PASS=0; FAIL=0
JAR="target/mcp-07-one-click-integrations.jar"
SERVER_CMD="java -jar $JAR"

run_test() {
    local name="$1"
    local payload="$2"
    local expect="$3"
    response=$(echo "$payload" | $SERVER_CMD 2>/dev/null)
    if echo "$response" | grep -q "$expect"; then
        echo "  ✅  $name"; PASS=$((PASS+1))
    else
        echo "  ❌  $name  [expected: $expect]"; FAIL=$((FAIL+1))
    fi
    $VERBOSE && echo "     → $response"
}

echo ""
echo "══════════════════════════════════════════════════════════"
echo "  Ecoskiller MCP | CAT-7 One-Click Integrations"
echo "  $(date)"
echo "══════════════════════════════════════════════════════════"
echo ""

# ── core ──────────────────────────────────────────────────
echo "[ core ]"
run_test "initialize" \
  '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}' \
  "2024-11-05"
run_test "tools/list returns sso_integration" \
  '{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}' \
  "sso_integration"
run_test "tools/list returns calendar_sync" \
  '{"jsonrpc":"2.0","id":3,"method":"tools/list","params":{}}' \
  "calendar_sync"
run_test "ping" \
  '{"jsonrpc":"2.0","id":4,"method":"ping","params":{}}' \
  "pong"

# ── 73_SSO_INTEGRATION_AGENT ──────────────────────────────
echo ""
echo "[ 73_SSO_INTEGRATION_AGENT ]"

run_test "configure Google OIDC" \
  '{"jsonrpc":"2.0","id":10,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"configure","provider":"GOOGLE","protocol":"OIDC","domain":"school.ecoskiller.app","client_id":"google-client-001","mfa_required":"true","session_timeout_min":"480"}}}' \
  "73_SSO_INTEGRATION_AGENT"

run_test "Google auth endpoint resolved" \
  '{"jsonrpc":"2.0","id":11,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"configure","provider":"GOOGLE","protocol":"OIDC","domain":"school.ecoskiller.app"}}}' \
  "accounts.google.com"

run_test "configure Microsoft Azure AD" \
  '{"jsonrpc":"2.0","id":12,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"configure","provider":"AZURE_AD","protocol":"SAML2","domain":"corp.ecoskiller.app","mfa_required":"true"}}}' \
  "microsoftonline.com"

run_test "configure Okta OIDC" \
  '{"jsonrpc":"2.0","id":13,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"configure","provider":"OKTA","protocol":"OIDC","domain":"org.ecoskiller.app"}}}' \
  "okta.com"

run_test "configure Auth0" \
  '{"jsonrpc":"2.0","id":14,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"configure","provider":"AUTH0","protocol":"OAUTH2","domain":"auth0.ecoskiller.app"}}}' \
  "auth0.com"

run_test "configure LDAP" \
  '{"jsonrpc":"2.0","id":15,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"configure","provider":"LDAP","protocol":"LDAP","domain":"internal.ecoskiller.app"}}}' \
  "ldap://"

run_test "MFA enforcement present" \
  '{"jsonrpc":"2.0","id":16,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"configure","provider":"GOOGLE","mfa_required":"true"}}}' \
  "TOTP"

run_test "PKCE enabled" \
  '{"jsonrpc":"2.0","id":17,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"configure","provider":"MICROSOFT","protocol":"OIDC"}}}' \
  "pkce_enabled"

run_test "role mapping present" \
  '{"jsonrpc":"2.0","id":18,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"configure","provider":"GOOGLE","role_mapping":"CUSTOM"}}}' \
  "ECOSKILLER_ADMIN"

run_test "audit logging present" \
  '{"jsonrpc":"2.0","id":19,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"audit_log","provider":"GOOGLE"}}}' \
  "audit_logging"

run_test "connection test passed" \
  '{"jsonrpc":"2.0","id":20,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"test_connection","provider":"GOOGLE","domain":"test.ecoskiller.app"}}}' \
  "PASSED"

run_test "one_click_status CONFIGURED" \
  '{"jsonrpc":"2.0","id":21,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"status","provider":"OKTA"}}}' \
  "CONFIGURED"

# ── 81_CALENDAR_SYNC_MASTER_SEAL ──────────────────────────
echo ""
echo "[ 81_CALENDAR_SYNC_MASTER_SEAL ]"

run_test "sync Google Calendar bidirectional" \
  '{"jsonrpc":"2.0","id":30,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"sync","provider":"GOOGLE_CALENDAR","user_id":"USR-1001","calendar_id":"primary","sync_direction":"BIDIRECTIONAL","timezone":"Asia/Kolkata"}}}' \
  "81_CALENDAR_SYNC_MASTER_SEAL"

run_test "events_synced present" \
  '{"jsonrpc":"2.0","id":31,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"sync","provider":"GOOGLE_CALENDAR","user_id":"USR-1001"}}}' \
  "events_synced"

run_test "sync Microsoft Outlook" \
  '{"jsonrpc":"2.0","id":32,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"sync","provider":"MICROSOFT_OUTLOOK","user_id":"USR-2002","sync_direction":"PUSH","timezone":"UTC"}}}' \
  "MICROSOFT_OUTLOOK"

run_test "sync Apple iCal" \
  '{"jsonrpc":"2.0","id":33,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"sync","provider":"APPLE_ICAL","user_id":"USR-3003","sync_direction":"PULL"}}}' \
  "SUCCESS"

run_test "sync Exchange Server" \
  '{"jsonrpc":"2.0","id":34,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"sync","provider":"EXCHANGE_SERVER","user_id":"USR-4004","calendar_id":"inbox"}}}' \
  "SUCCESS"

run_test "timezone normalisation" \
  '{"jsonrpc":"2.0","id":35,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"sync","provider":"GOOGLE_CALENDAR","user_id":"USR-5005","timezone":"America/New_York"}}}' \
  "timezone_normalised"

run_test "recurring events expanded" \
  '{"jsonrpc":"2.0","id":36,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"sync","provider":"GOOGLE_CALENDAR","user_id":"USR-1001","include_recurring":"true"}}}' \
  "recurring_events_expanded"

run_test "conflict detection resolved" \
  '{"jsonrpc":"2.0","id":37,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"sync","provider":"MICROSOFT_OUTLOOK","conflict_strategy":"LATEST_WINS"}}}' \
  "conflicts_resolved"

run_test "video links preserved - Zoom" \
  '{"jsonrpc":"2.0","id":38,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"sync","provider":"ZOOM","user_id":"USR-6006"}}}' \
  "zoom_links_detected"

run_test "video links preserved - Teams" \
  '{"jsonrpc":"2.0","id":39,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"sync","provider":"MS_TEAMS","user_id":"USR-7007"}}}' \
  "teams_links_detected"

run_test "webhook registered" \
  '{"jsonrpc":"2.0","id":40,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"register_webhook","provider":"GOOGLE_CALENDAR","user_id":"USR-1001"}}}' \
  "webhook_registered"

run_test "RSVP propagated" \
  '{"jsonrpc":"2.0","id":41,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"sync","provider":"GOOGLE_CALENDAR","user_id":"USR-1001"}}}' \
  "rsvp_propagated"

run_test "one_click_status SYNCED" \
  '{"jsonrpc":"2.0","id":42,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"status","provider":"GOOGLE_CALENDAR","user_id":"USR-1001"}}}' \
  "SYNCED"

# ── edge cases ─────────────────────────────────────────────
echo ""
echo "[ edge cases ]"
run_test "unknown tool returns error" \
  '{"jsonrpc":"2.0","id":99,"method":"tools/call","params":{"name":"nonexistent_tool","arguments":{}}}' \
  "Unknown tool"
run_test "unknown method returns error" \
  '{"jsonrpc":"2.0","id":100,"method":"tools/unknown","params":{}}' \
  "Method not found"

# ── summary ────────────────────────────────────────────────
echo ""
echo "══════════════════════════════════════════════════════════"
TOTAL=$((PASS + FAIL))
echo "  Results:  ✅  $PASS passed   ❌  $FAIL failed   ($TOTAL total)"
echo "══════════════════════════════════════════════════════════"
echo ""
