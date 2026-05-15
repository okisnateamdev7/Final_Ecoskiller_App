#!/bin/bash
# Quick functional test for the MCP server
# Usage: ./test_mcp.sh

JAR="target/licensing-contract-mcp-1.0.0.jar"
if [ ! -f "$JAR" ]; then
  echo "Build first: mvn package"
  exit 1
fi

echo "=== Testing MCP Server ==="
echo ""

# Simulate JWT for test (base64url encoded payload with admin roles)
PAYLOAD=$(echo -n '{"sub":"test-actor-uuid","iss":"http://keycloak.ecoskiller.internal/realms/ecoskiller","exp":9999999999,"realm_access":{"roles":["ecoskiller:licensing:create","ecoskiller:licensing:admin","ecoskiller:viewer"]}}' | base64 | tr '+/' '-_' | tr -d '=')
TEST_JWT="eyJhbGciOiJSUzI1NiJ9.${PAYLOAD}.fakesig"

run_test() {
  local NAME="$1"
  local PAYLOAD="$2"
  echo "--- $NAME ---"
  echo "$PAYLOAD" | java -jar "$JAR" 2>/dev/null | python3 -m json.tool 2>/dev/null | head -20
  echo ""
}

run_test "initialize" '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{}}}'

run_test "tools/list" '{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}'

run_test "contract_lifecycle.create" "{\"jsonrpc\":\"2.0\",\"id\":3,\"method\":\"tools/call\",\"params\":{\"name\":\"contract_lifecycle\",\"arguments\":{\"action\":\"create\",\"jwt_token\":\"$TEST_JWT\",\"idea_id\":\"550e8400-e29b-41d4-a716-446655440000\",\"licensee_business_id\":\"6ba7b810-9dad-11d1-80b4-00c04fd430c8\",\"idea_owner_candidate_id\":\"6ba7b811-9dad-11d1-80b4-00c04fd430c8\",\"tenant_id\":\"acme-corp\",\"proposed_royalty_rate\":0.0003,\"proposed_term_years\":12,\"territorial_scope\":\"national\",\"usage_scope\":\"non-exclusive\"}}}"

run_test "royalty_rate_validation" "{\"jsonrpc\":\"2.0\",\"id\":4,\"method\":\"tools/call\",\"params\":{\"name\":\"royalty_rate_validation\",\"arguments\":{\"action\":\"validate\",\"jwt_token\":\"$TEST_JWT\",\"proposed_rate\":0.0003,\"proposed_term_years\":12}}}"

echo "=== Done ==="
