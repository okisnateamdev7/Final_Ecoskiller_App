#!/bin/bash
# ─────────────────────────────────────────────────────────────
#  Ecoskiller CAT-6 — Corporate ERP Suite | Agent Tests
#  Usage:  bash test_agents.sh            (pass/fail summary)
#          bash test_agents.sh --verbose  (full JSON output)
# ─────────────────────────────────────────────────────────────

VERBOSE=false
[[ "$1" == "--verbose" ]] && VERBOSE=true

PASS=0; FAIL=0
JAR="target/mcp-06-corporate-erp.jar"
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
echo "  Ecoskiller MCP | CAT-6 Corporate ERP Suite"
echo "  $(date)"
echo "══════════════════════════════════════════════════════════"
echo ""

# ── core ──────────────────────────────────────────────────
echo "[ core ]"
run_test "initialize" \
  '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}' \
  "2024-11-05"
run_test "tools/list returns accounting" \
  '{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}' \
  "accounting"
run_test "ping" \
  '{"jsonrpc":"2.0","id":3,"method":"ping","params":{}}' \
  "pong"

# ── accounting ─────────────────────────────────────────────
echo ""
echo "[ ACCOUNTING_AGENT ]"
run_test "journal entry" \
  '{"jsonrpc":"2.0","id":10,"method":"tools/call","params":{"name":"accounting","arguments":{"action":"journal_entry","account_code":"ACC-2001","debit":"50000","credit":"50000","narration":"Sales Revenue","fiscal_year":"FY-2025-26"}}}' \
  "ACCOUNTING_AGENT__ANTIGRAVITY_CORPORATE_ERP"
run_test "trial balance" \
  '{"jsonrpc":"2.0","id":11,"method":"tools/call","params":{"name":"accounting","arguments":{"action":"trial_balance","fiscal_year":"FY-2025-26"}}}' \
  "SUCCESS"

# ── accounting_sync ────────────────────────────────────────
echo ""
echo "[ ACCOUNTING_SYNC_AGENT ]"
run_test "sync from tally" \
  '{"jsonrpc":"2.0","id":20,"method":"tools/call","params":{"name":"accounting_sync","arguments":{"action":"sync","source_platform":"TALLY","target_platform":"ECOSKILLER-ERP","entity_type":"VOUCHER","sync_mode":"INCREMENTAL","period":"2025-06"}}}' \
  "ACCOUNTING_SYNC_AGENT"
run_test "sync from quickbooks" \
  '{"jsonrpc":"2.0","id":21,"method":"tools/call","params":{"name":"accounting_sync","arguments":{"action":"sync","source_platform":"QUICKBOOKS","sync_mode":"FULL"}}}' \
  "SUCCESS"

# ── asset ──────────────────────────────────────────────────
echo ""
echo "[ ASSET_AGENT ]"
run_test "add asset" \
  '{"jsonrpc":"2.0","id":30,"method":"tools/call","params":{"name":"asset","arguments":{"action":"add","asset_id":"AST-00200","asset_category":"VEHICLE","acquisition_date":"2025-01-15","depreciation_method":"WDV","location":"BRANCH-PUNE"}}}' \
  "ASSET_AGENT_ANTIGRAVITY_ERP"
run_test "depreciation schedule" \
  '{"jsonrpc":"2.0","id":31,"method":"tools/call","params":{"name":"asset","arguments":{"action":"depreciation_schedule","asset_id":"AST-00200"}}}' \
  "SUCCESS"

# ── budget ─────────────────────────────────────────────────
echo ""
echo "[ BUDGET_AGENT ]"
run_test "allocate budget" \
  '{"jsonrpc":"2.0","id":40,"method":"tools/call","params":{"name":"budget","arguments":{"action":"allocate","budget_id":"BUD-2025-HR","cost_centre":"CC-HR","department":"HR","fiscal_year":"FY-2025-26","amount":"2000000"}}}' \
  "BUDGET_AGENT"
run_test "variance analysis" \
  '{"jsonrpc":"2.0","id":41,"method":"tools/call","params":{"name":"budget","arguments":{"action":"variance_analysis","budget_id":"BUD-2025-HR"}}}' \
  "SUCCESS"

# ── dms ────────────────────────────────────────────────────
echo ""
echo "[ DMS_AGENT ]"
run_test "ingest invoice doc" \
  '{"jsonrpc":"2.0","id":50,"method":"tools/call","params":{"name":"dms","arguments":{"action":"ingest","document_id":"DOC-INV-9001","document_type":"INVOICE","department":"FINANCE","tags":"GST,VENDOR","retention_policy":"7Y"}}}' \
  "DMS_AGENT_ANTIGRAVITY_ERP"
run_test "retrieve contract" \
  '{"jsonrpc":"2.0","id":51,"method":"tools/call","params":{"name":"dms","arguments":{"action":"retrieve","document_id":"DOC-CON-0012","document_type":"CONTRACT"}}}' \
  "SUCCESS"

# ── erp_analytics ──────────────────────────────────────────
echo ""
echo "[ ERP_ANALYTICS_AGENT ]"
run_test "kpi dashboard" \
  '{"jsonrpc":"2.0","id":60,"method":"tools/call","params":{"name":"erp_analytics","arguments":{"action":"generate","report_type":"KPI_DASHBOARD","module":"FINANCE","date_from":"2025-04-01","date_to":"2025-06-30","format":"JSON"}}}' \
  "ERP_ANALYTICS_AGENT"
run_test "cash flow forecast" \
  '{"jsonrpc":"2.0","id":61,"method":"tools/call","params":{"name":"erp_analytics","arguments":{"action":"forecast","report_type":"CASHFLOW","module":"TREASURY"}}}' \
  "SUCCESS"

# ── gst_connect ────────────────────────────────────────────
echo ""
echo "[ GST_CONNECT_AGENT ]"
run_test "file gstr3b" \
  '{"jsonrpc":"2.0","id":70,"method":"tools/call","params":{"name":"gst_connect","arguments":{"action":"file_return","gstin":"27AAAAA0000A1Z5","return_type":"GSTR3B","period":"062025","invoice_id":"INV-2025-100","amount":"250000"}}}' \
  "GST_CONNECT_AGENT"
run_test "generate irn" \
  '{"jsonrpc":"2.0","id":71,"method":"tools/call","params":{"name":"gst_connect","arguments":{"action":"generate_irn","invoice_id":"INV-2025-101","gstin":"29BBBBB1111B2Z3"}}}' \
  "SUCCESS"

# ── hrms ───────────────────────────────────────────────────
echo ""
echo "[ HRMS_AGENT ]"
run_test "onboard employee" \
  '{"jsonrpc":"2.0","id":80,"method":"tools/call","params":{"name":"hrms","arguments":{"action":"onboard","employee_id":"EMP-00550","department":"PRODUCT","designation":"PRODUCT_MANAGER","join_date":"2025-06-01","status":"ACTIVE"}}}' \
  "HRMS_AGENT_ANTIGRAVITY_SEALED"
run_test "appraisal cycle" \
  '{"jsonrpc":"2.0","id":81,"method":"tools/call","params":{"name":"hrms","arguments":{"action":"appraisal","employee_id":"EMP-00550"}}}' \
  "SUCCESS"

# ── ledger_migration ───────────────────────────────────────
echo ""
echo "[ LEDGER_MIGRATION_AGENT ]"
run_test "migrate from tally prime" \
  '{"jsonrpc":"2.0","id":90,"method":"tools/call","params":{"name":"ledger_migration","arguments":{"action":"migrate","source_system":"TALLY-PRIME","ledger_id":"LED-ALL","migration_mode":"FULL","fiscal_year":"FY-2024-25","validate_only":"false"}}}' \
  "LEDGER_MIGRATION_AGENT"
run_test "validate only mode" \
  '{"jsonrpc":"2.0","id":91,"method":"tools/call","params":{"name":"ledger_migration","arguments":{"action":"migrate","source_system":"SAP-FICO","fiscal_year":"FY-2024-25","validate_only":"true"}}}' \
  "SUCCESS"

# ── payroll ────────────────────────────────────────────────
echo ""
echo "[ PAYROLL_AGENT ]"
run_test "process payroll" \
  '{"jsonrpc":"2.0","id":100,"method":"tools/call","params":{"name":"payroll","arguments":{"action":"process","employee_id":"EMP-00001","pay_period":"2025-06","salary_component":"GROSS","tds_applicable":"true","bank_account":"XXXX-5678"}}}' \
  "PAYROLL_AGENT_ANTIGRAVITY_ERP"
run_test "generate payslip" \
  '{"jsonrpc":"2.0","id":101,"method":"tools/call","params":{"name":"payroll","arguments":{"action":"payslip","employee_id":"EMP-00001","pay_period":"2025-06"}}}' \
  "SUCCESS"

# ── procurement ────────────────────────────────────────────
echo ""
echo "[ PROCUREMENT_AGENT ]"
run_test "raise purchase order" \
  '{"jsonrpc":"2.0","id":110,"method":"tools/call","params":{"name":"procurement","arguments":{"action":"raise_po","po_id":"PO-2025-0500","vendor_id":"VEN-0099","item_code":"ITM-2002","quantity":"200","unit_price":"1500","delivery_date":"2025-08-01"}}}' \
  "PROCUREMENT_AGENT_ANTIGRAVITY_ERP"
run_test "three-way match" \
  '{"jsonrpc":"2.0","id":111,"method":"tools/call","params":{"name":"procurement","arguments":{"action":"three_way_match","po_id":"PO-2025-0500","vendor_id":"VEN-0099"}}}' \
  "SUCCESS"

# ── regulatory ─────────────────────────────────────────────
echo ""
echo "[ REGULATORY_AGENT ]"
run_test "file tds return 26q" \
  '{"jsonrpc":"2.0","id":120,"method":"tools/call","params":{"name":"regulatory","arguments":{"action":"file","regulation_type":"TDS_RETURN_26Q","filing_period":"Q1-FY2025-26","entity_id":"ENT-CORP-001","due_date":"2025-07-31","jurisdiction":"IN"}}}' \
  "REGULATORY_AGENT_ANTIGRAVITY_ERP"
run_test "pf ecr challan" \
  '{"jsonrpc":"2.0","id":121,"method":"tools/call","params":{"name":"regulatory","arguments":{"action":"file","regulation_type":"PF-ECR","filing_period":"2025-06","entity_id":"ENT-CORP-001"}}}' \
  "SUCCESS"

# ── summary ────────────────────────────────────────────────
echo ""
echo "══════════════════════════════════════════════════════════"
TOTAL=$((PASS + FAIL))
echo "  Results:  ✅  $PASS passed   ❌  $FAIL failed   ($TOTAL total)"
echo "══════════════════════════════════════════════════════════"
echo ""
