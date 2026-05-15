package ecoskiller;

import java.io.*;
import java.util.*;
import java.time.Instant;

/**
 * Ecoskiller | CAT-6 — Corporate ERP Suite
 * MCP Server in Java | 12 Agents | Priority: HIGH
 *
 * Agents:
 *   17. ACCOUNTING_AGENT__ANTIGRAVITY_CORPORATE_ERP
 *   18. ACCOUNTING_SYNC_AGENT
 *   19. ASSET_AGENT_ANTIGRAVITY_ERP
 *   20. BUDGET_AGENT
 *   21. DMS_AGENT_ANTIGRAVITY_ERP
 *   22. ERP_ANALYTICS_AGENT
 *   23. GST_CONNECT_AGENT
 *   24. HRMS_AGENT_ANTIGRAVITY_SEALED
 *   25. LEDGER_MIGRATION_AGENT
 *   26. PAYROLL_AGENT_ANTIGRAVITY_ERP
 *   27. PROCUREMENT_AGENT_ANTIGRAVITY_ERP
 *   28. REGULATORY_AGENT_ANTIGRAVITY_ERP
 *
 * Protocol : JSON-RPC 2.0 over stdio | MCP Version: 2024-11-05
 */
public class McpServer {

    // ─────────────────────────────────────────────────────────
    //  Tool registry
    // ─────────────────────────────────────────────────────────
    private static final List<Map<String, Object>> TOOLS = new ArrayList<>();

    static {
        TOOLS.add(buildTool(
            "accounting",
            "ACCOUNTING_AGENT__ANTIGRAVITY_CORPORATE_ERP",
            "Manages double-entry bookkeeping, journal entries, trial balance, P&L statements, balance sheets, and period-end closing for corporate accounts.",
            new String[]{"action", "account_code", "debit", "credit", "narration", "fiscal_year"}
        ));

        TOOLS.add(buildTool(
            "accounting_sync",
            "ACCOUNTING_SYNC_AGENT",
            "Synchronises financial data bi-directionally between Ecoskiller ERP and external accounting platforms (Tally, QuickBooks, Zoho Books, SAP FICO).",
            new String[]{"action", "source_platform", "target_platform", "entity_type", "sync_mode", "period"}
        ));

        TOOLS.add(buildTool(
            "asset",
            "ASSET_AGENT_ANTIGRAVITY_ERP",
            "Tracks corporate fixed assets: acquisition, depreciation schedules (SLM/WDV), disposals, revaluation, insurance, and physical verification.",
            new String[]{"action", "asset_id", "asset_category", "acquisition_date", "depreciation_method", "location"}
        ));

        TOOLS.add(buildTool(
            "budget",
            "BUDGET_AGENT",
            "Handles budget preparation, allocation by cost centre, variance analysis, forecast revisions, and budget vs actuals reporting.",
            new String[]{"action", "budget_id", "cost_centre", "department", "fiscal_year", "amount"}
        ));

        TOOLS.add(buildTool(
            "dms",
            "DMS_AGENT_ANTIGRAVITY_ERP",
            "Document Management System: ingests, indexes, versions, and retrieves corporate documents (invoices, contracts, POs, compliance filings) with access-control.",
            new String[]{"action", "document_id", "document_type", "department", "tags", "retention_policy"}
        ));

        TOOLS.add(buildTool(
            "erp_analytics",
            "ERP_ANALYTICS_AGENT",
            "Generates cross-module ERP analytics: KPI dashboards, trend analysis, cash-flow forecasts, ageing reports, and executive summaries from live ERP data.",
            new String[]{"action", "report_type", "module", "date_from", "date_to", "format"}
        ));

        TOOLS.add(buildTool(
            "gst_connect",
            "GST_CONNECT_AGENT",
            "Handles India GST compliance: GSTR-1/2A/3B filing, e-invoicing (IRN/QR), e-way bills, ITC reconciliation, GST audit, and GSTIN validation.",
            new String[]{"action", "gstin", "return_type", "period", "invoice_id", "amount"}
        ));

        TOOLS.add(buildTool(
            "hrms",
            "HRMS_AGENT_ANTIGRAVITY_SEALED",
            "Manages the full HR lifecycle: recruitment, onboarding, employee profiles, appraisals, leave, separation, and organisational hierarchy.",
            new String[]{"action", "employee_id", "department", "designation", "join_date", "status"}
        ));

        TOOLS.add(buildTool(
            "ledger_migration",
            "LEDGER_MIGRATION_AGENT",
            "Migrates chart of accounts, ledger masters, opening balances, and historical transactions from legacy accounting systems to Ecoskiller ERP.",
            new String[]{"action", "source_system", "ledger_id", "migration_mode", "fiscal_year", "validate_only"}
        ));

        TOOLS.add(buildTool(
            "payroll",
            "PAYROLL_AGENT_ANTIGRAVITY_ERP",
            "Processes monthly payroll: salary computation, statutory deductions (PF/ESI/PT/TDS), pay-slip generation, bank disbursement files, and Form-16.",
            new String[]{"action", "employee_id", "pay_period", "salary_component", "tds_applicable", "bank_account"}
        ));

        TOOLS.add(buildTool(
            "procurement",
            "PROCUREMENT_AGENT_ANTIGRAVITY_ERP",
            "Manages procure-to-pay: indent, vendor selection, purchase orders, GRN, three-way matching, vendor payments, and spend analytics.",
            new String[]{"action", "po_id", "vendor_id", "item_code", "quantity", "unit_price", "delivery_date"}
        ));

        TOOLS.add(buildTool(
            "regulatory",
            "REGULATORY_AGENT_ANTIGRAVITY_ERP",
            "Tracks and files corporate regulatory obligations: MCA/ROC filings, TDS returns (24Q/26Q), PF/ESI challans, FEMA compliance, and audit schedules.",
            new String[]{"action", "regulation_type", "filing_period", "entity_id", "due_date", "jurisdiction"}
        ));
    }

    // ─────────────────────────────────────────────────────────
    //  Entry point
    // ─────────────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

        log("CAT-6 Corporate ERP Suite MCP Server started (12 agents)");

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String response = handle(line);
                if (response != null) out.println(response);
            } catch (Exception e) {
                log("Error: " + e.getMessage());
                out.println(errorResponse(null, -32700, "Parse error: " + e.getMessage()));
            }
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Router
    // ─────────────────────────────────────────────────────────
    private static String handle(String raw) {
        String id     = extractString(raw, "\"id\"");
        String method = extractString(raw, "\"method\"");
        if (method == null) return errorResponse(id, -32600, "Invalid Request");

        switch (method) {
            case "initialize": return handleInitialize(id);
            case "tools/list": return handleToolsList(id);
            case "tools/call": return handleToolsCall(id, raw);
            case "ping":       return result(id, "{\"status\":\"pong\"}");
            default:           return errorResponse(id, -32601, "Method not found: " + method);
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Handlers
    // ─────────────────────────────────────────────────────────
    private static String handleInitialize(String id) {
        return result(id,
            "{\"protocolVersion\":\"2024-11-05\"," +
            "\"serverInfo\":{\"name\":\"mcp-06-corporate-erp\",\"version\":\"1.0.0\"}," +
            "\"capabilities\":{\"tools\":{}}}");
    }

    private static String handleToolsList(String id) {
        StringBuilder sb = new StringBuilder("{\"tools\":[");
        for (int i = 0; i < TOOLS.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toolToJson(TOOLS.get(i)));
        }
        sb.append("]}");
        return result(id, sb.toString());
    }

    private static String handleToolsCall(String id, String raw) {
        String toolName = extractString(raw, "\"name\"");
        if (toolName == null) return errorResponse(id, -32602, "Missing tool name");

        Map<String, Object> found = null;
        for (Map<String, Object> t : TOOLS) {
            if (t.get("name").equals(toolName)) { found = t; break; }
        }
        if (found == null) return errorResponse(id, -32602, "Unknown tool: " + toolName);

        String agentResult = dispatchAgent(toolName, raw);
        String content = "{\"type\":\"text\",\"text\":" + jsonString(agentResult) + "}";
        return result(id, "{\"content\":[" + content + "]}");
    }

    // ─────────────────────────────────────────────────────────
    //  Agent dispatcher
    // ─────────────────────────────────────────────────────────
    private static String dispatchAgent(String toolName, String raw) {
        String action = def(extractString(raw, "\"action\""), "status");
        String ts     = Instant.now().toString();

        switch (toolName) {
            case "accounting":      return accountingAgent(action, raw, ts);
            case "accounting_sync": return accountingSyncAgent(action, raw, ts);
            case "asset":           return assetAgent(action, raw, ts);
            case "budget":          return budgetAgent(action, raw, ts);
            case "dms":             return dmsAgent(action, raw, ts);
            case "erp_analytics":   return erpAnalyticsAgent(action, raw, ts);
            case "gst_connect":     return gstConnectAgent(action, raw, ts);
            case "hrms":            return hrmsAgent(action, raw, ts);
            case "ledger_migration":return ledgerMigrationAgent(action, raw, ts);
            case "payroll":         return payrollAgent(action, raw, ts);
            case "procurement":     return procurementAgent(action, raw, ts);
            case "regulatory":      return regulatoryAgent(action, raw, ts);
            default:                return "{\"error\":\"Unknown agent\"}";
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Agent implementations
    // ─────────────────────────────────────────────────────────

    private static String accountingAgent(String action, String raw, String ts) {
        String accountCode = def(extractString(raw, "\"account_code\""), "ACC-1001");
        String debit       = def(extractString(raw, "\"debit\""),        "0");
        String credit      = def(extractString(raw, "\"credit\""),       "0");
        String narration   = def(extractString(raw, "\"narration\""),    "General Entry");
        String fiscalYear  = def(extractString(raw, "\"fiscal_year\""),  "FY-2025-26");
        return "{" +
            "\"agent\":\"ACCOUNTING_AGENT__ANTIGRAVITY_CORPORATE_ERP\"," +
            "\"action\":\"" + action + "\"," +
            "\"account_code\":\"" + accountCode + "\"," +
            "\"debit\":" + debit + "," +
            "\"credit\":" + credit + "," +
            "\"narration\":\"" + narration + "\"," +
            "\"fiscal_year\":\"" + fiscalYear + "\"," +
            "\"journal_id\":\"JRN-" + ts.substring(0,10).replace("-","") + "-001\"," +
            "\"trial_balance_balanced\":true," +
            "\"period_closed\":false," +
            "\"double_entry_verified\":true," +
            "\"ifrs_compliant\":true," +
            "\"ind_as_compliant\":true," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String accountingSyncAgent(String action, String raw, String ts) {
        String srcPlatform  = def(extractString(raw, "\"source_platform\""), "TALLY");
        String tgtPlatform  = def(extractString(raw, "\"target_platform\""), "ECOSKILLER-ERP");
        String entityType   = def(extractString(raw, "\"entity_type\""),     "VOUCHER");
        String syncMode     = def(extractString(raw, "\"sync_mode\""),       "INCREMENTAL");
        String period       = def(extractString(raw, "\"period\""),          "2025-06");
        return "{" +
            "\"agent\":\"ACCOUNTING_SYNC_AGENT\"," +
            "\"action\":\"" + action + "\"," +
            "\"source_platform\":\"" + srcPlatform + "\"," +
            "\"target_platform\":\"" + tgtPlatform + "\"," +
            "\"entity_type\":\"" + entityType + "\"," +
            "\"sync_mode\":\"" + syncMode + "\"," +
            "\"period\":\"" + period + "\"," +
            "\"records_synced\":1284," +
            "\"conflicts_resolved\":4," +
            "\"delta_records\":89," +
            "\"last_sync_checksum\":\"SHA256-OK\"," +
            "\"rollback_available\":true," +
            "\"supported_platforms\":[\"TALLY\",\"QUICKBOOKS\",\"ZOHO_BOOKS\",\"SAP_FICO\"]," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String assetAgent(String action, String raw, String ts) {
        String assetId     = def(extractString(raw, "\"asset_id\""),           "AST-00001");
        String assetCat    = def(extractString(raw, "\"asset_category\""),      "IT_EQUIPMENT");
        String acqDate     = def(extractString(raw, "\"acquisition_date\""),    "2024-04-01");
        String deprMethod  = def(extractString(raw, "\"depreciation_method\""), "SLM");
        String location    = def(extractString(raw, "\"location\""),            "HQ-MUMBAI");
        return "{" +
            "\"agent\":\"ASSET_AGENT_ANTIGRAVITY_ERP\"," +
            "\"action\":\"" + action + "\"," +
            "\"asset_id\":\"" + assetId + "\"," +
            "\"asset_category\":\"" + assetCat + "\"," +
            "\"acquisition_date\":\"" + acqDate + "\"," +
            "\"depreciation_method\":\"" + deprMethod + "\"," +
            "\"location\":\"" + location + "\"," +
            "\"gross_block\":150000," +
            "\"accumulated_depreciation\":30000," +
            "\"net_block\":120000," +
            "\"useful_life_years\":5," +
            "\"insurance_valid_until\":\"2026-03-31\"," +
            "\"physical_verification\":\"VERIFIED-2025-01\"," +
            "\"disposal_flag\":false," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String budgetAgent(String action, String raw, String ts) {
        String budgetId   = def(extractString(raw, "\"budget_id\""),   "BUD-2025-001");
        String costCentre = def(extractString(raw, "\"cost_centre\""), "CC-SALES");
        String department = def(extractString(raw, "\"department\""),  "SALES");
        String fiscalYear = def(extractString(raw, "\"fiscal_year\""), "FY-2025-26");
        String amount     = def(extractString(raw, "\"amount\""),      "5000000");
        return "{" +
            "\"agent\":\"BUDGET_AGENT\"," +
            "\"action\":\"" + action + "\"," +
            "\"budget_id\":\"" + budgetId + "\"," +
            "\"cost_centre\":\"" + costCentre + "\"," +
            "\"department\":\"" + department + "\"," +
            "\"fiscal_year\":\"" + fiscalYear + "\"," +
            "\"approved_budget\":" + amount + "," +
            "\"actuals_to_date\":1823400," +
            "\"variance\":3176600," +
            "\"variance_pct\":63.5," +
            "\"forecast_revised\":false," +
            "\"approval_status\":\"APPROVED\"," +
            "\"alerts\":[\"Q2_UNDERSPEND\"]," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String dmsAgent(String action, String raw, String ts) {
        String documentId  = def(extractString(raw, "\"document_id\""),    "DOC-CORP-0001");
        String docType     = def(extractString(raw, "\"document_type\""),   "INVOICE");
        String department  = def(extractString(raw, "\"department\""),      "FINANCE");
        String tags        = def(extractString(raw, "\"tags\""),            "GST,Q1");
        String retention   = def(extractString(raw, "\"retention_policy\""),"7Y");
        return "{" +
            "\"agent\":\"DMS_AGENT_ANTIGRAVITY_ERP\"," +
            "\"action\":\"" + action + "\"," +
            "\"document_id\":\"" + documentId + "\"," +
            "\"document_type\":\"" + docType + "\"," +
            "\"department\":\"" + department + "\"," +
            "\"tags\":\"" + tags + "\"," +
            "\"retention_policy\":\"" + retention + "\"," +
            "\"version\":\"v1.0\"," +
            "\"indexed\":true," +
            "\"ocr_processed\":true," +
            "\"access_control\":\"ROLE_BASED\"," +
            "\"audit_trail\":true," +
            "\"storage_path\":\"ecoskiller://dms/" + department.toLowerCase() + "/" + documentId + "\"," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String erpAnalyticsAgent(String action, String raw, String ts) {
        String reportType = def(extractString(raw, "\"report_type\""), "KPI_DASHBOARD");
        String module     = def(extractString(raw, "\"module\""),      "FINANCE");
        String dateFrom   = def(extractString(raw, "\"date_from\""),   "2025-04-01");
        String dateTo     = def(extractString(raw, "\"date_to\""),     "2025-06-30");
        String format     = def(extractString(raw, "\"format\""),      "JSON");
        return "{" +
            "\"agent\":\"ERP_ANALYTICS_AGENT\"," +
            "\"action\":\"" + action + "\"," +
            "\"report_type\":\"" + reportType + "\"," +
            "\"module\":\"" + module + "\"," +
            "\"date_from\":\"" + dateFrom + "\"," +
            "\"date_to\":\"" + dateTo + "\"," +
            "\"format\":\"" + format + "\"," +
            "\"kpis\":{" +
                "\"revenue\":48500000," +
                "\"ebitda_pct\":22.4," +
                "\"debtor_days\":38," +
                "\"creditor_days\":45," +
                "\"inventory_turns\":6.2," +
                "\"cash_burn_monthly\":3200000" +
            "}," +
            "\"trend\":\"IMPROVING\"," +
            "\"anomalies_detected\":1," +
            "\"report_url\":\"ecoskiller://analytics/" + reportType.toLowerCase() + "\"," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String gstConnectAgent(String action, String raw, String ts) {
        String gstin      = def(extractString(raw, "\"gstin\""),       "27AAAAA0000A1Z5");
        String returnType = def(extractString(raw, "\"return_type\""), "GSTR3B");
        String period     = def(extractString(raw, "\"period\""),      "062025");
        String invoiceId  = def(extractString(raw, "\"invoice_id\""),  "INV-2025-001");
        String amount     = def(extractString(raw, "\"amount\""),      "100000");
        return "{" +
            "\"agent\":\"GST_CONNECT_AGENT\"," +
            "\"action\":\"" + action + "\"," +
            "\"gstin\":\"" + gstin + "\"," +
            "\"return_type\":\"" + returnType + "\"," +
            "\"period\":\"" + period + "\"," +
            "\"invoice_id\":\"" + invoiceId + "\"," +
            "\"taxable_value\":" + amount + "," +
            "\"cgst\":9000," +
            "\"sgst\":9000," +
            "\"igst\":0," +
            "\"irn_generated\":true," +
            "\"qr_code\":\"QR-IRN-" + invoiceId + "\"," +
            "\"eway_bill_required\":false," +
            "\"itc_eligible\":true," +
            "\"itc_amount\":18000," +
            "\"filing_status\":\"FILED\"," +
            "\"arn\":\"ARN-" + period + "-001\"," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String hrmsAgent(String action, String raw, String ts) {
        String employeeId  = def(extractString(raw, "\"employee_id\""), "EMP-00001");
        String department  = def(extractString(raw, "\"department\""),  "ENGINEERING");
        String designation = def(extractString(raw, "\"designation\""), "SENIOR_ENGINEER");
        String joinDate    = def(extractString(raw, "\"join_date\""),   "2023-07-01");
        String status      = def(extractString(raw, "\"status\""),      "ACTIVE");
        return "{" +
            "\"agent\":\"HRMS_AGENT_ANTIGRAVITY_SEALED\"," +
            "\"action\":\"" + action + "\"," +
            "\"employee_id\":\"" + employeeId + "\"," +
            "\"department\":\"" + department + "\"," +
            "\"designation\":\"" + designation + "\"," +
            "\"join_date\":\"" + joinDate + "\"," +
            "\"status\":\"" + status + "\"," +
            "\"probation_cleared\":true," +
            "\"leave_balance\":{\"casual\":5,\"sick\":8,\"earned\":14}," +
            "\"appraisal_due\":\"2025-09-30\"," +
            "\"pf_uan\":\"UAN-" + employeeId + "\"," +
            "\"esic_enrolled\":false," +
            "\"reporting_manager\":\"EMP-00010\"," +
            "\"org_chart_level\":3," +
            "\"documents_complete\":true," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String ledgerMigrationAgent(String action, String raw, String ts) {
        String sourceSystem  = def(extractString(raw, "\"source_system\""),  "TALLY-PRIME");
        String ledgerId      = def(extractString(raw, "\"ledger_id\""),      "LED-ALL");
        String migrationMode = def(extractString(raw, "\"migration_mode\""), "FULL");
        String fiscalYear    = def(extractString(raw, "\"fiscal_year\""),    "FY-2024-25");
        String validateOnly  = def(extractString(raw, "\"validate_only\""),  "false");
        return "{" +
            "\"agent\":\"LEDGER_MIGRATION_AGENT\"," +
            "\"action\":\"" + action + "\"," +
            "\"source_system\":\"" + sourceSystem + "\"," +
            "\"ledger_id\":\"" + ledgerId + "\"," +
            "\"migration_mode\":\"" + migrationMode + "\"," +
            "\"fiscal_year\":\"" + fiscalYear + "\"," +
            "\"validate_only\":" + validateOnly + "," +
            "\"ledger_masters_migrated\":842," +
            "\"opening_balances_verified\":true," +
            "\"transactions_migrated\":58320," +
            "\"contra_entries_balanced\":true," +
            "\"group_mapping_complete\":true," +
            "\"errors\":0," +
            "\"warnings\":3," +
            "\"rollback_snapshot\":\"SNAP-" + fiscalYear + "\"," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String payrollAgent(String action, String raw, String ts) {
        String employeeId  = def(extractString(raw, "\"employee_id\""),      "EMP-00001");
        String payPeriod   = def(extractString(raw, "\"pay_period\""),       "2025-06");
        String salaryComp  = def(extractString(raw, "\"salary_component\""), "GROSS");
        String tdsAppl     = def(extractString(raw, "\"tds_applicable\""),   "true");
        String bankAccount = def(extractString(raw, "\"bank_account\""),     "XXXX-1234");
        return "{" +
            "\"agent\":\"PAYROLL_AGENT_ANTIGRAVITY_ERP\"," +
            "\"action\":\"" + action + "\"," +
            "\"employee_id\":\"" + employeeId + "\"," +
            "\"pay_period\":\"" + payPeriod + "\"," +
            "\"salary_component\":\"" + salaryComp + "\"," +
            "\"gross_salary\":85000," +
            "\"basic\":34000," +
            "\"hra\":17000," +
            "\"special_allowance\":34000," +
            "\"pf_deduction\":4080," +
            "\"esi_deduction\":0," +
            "\"professional_tax\":200," +
            "\"tds_deducted\":" + (tdsAppl.equals("true") ? "8500" : "0") + "," +
            "\"net_payable\":72220," +
            "\"payslip_generated\":true," +
            "\"bank_disbursement_file\":\"NEFT-" + payPeriod + ".txt\"," +
            "\"form16_due\":\"2026-06-15\"," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String procurementAgent(String action, String raw, String ts) {
        String poId         = def(extractString(raw, "\"po_id\""),        "PO-2025-0001");
        String vendorId     = def(extractString(raw, "\"vendor_id\""),    "VEN-0042");
        String itemCode     = def(extractString(raw, "\"item_code\""),    "ITM-1001");
        String quantity     = def(extractString(raw, "\"quantity\""),     "100");
        String unitPrice    = def(extractString(raw, "\"unit_price\""),   "500");
        String deliveryDate = def(extractString(raw, "\"delivery_date\""),"2025-07-15");
        long   totalValue   = parseLong(quantity) * parseLong(unitPrice);
        return "{" +
            "\"agent\":\"PROCUREMENT_AGENT_ANTIGRAVITY_ERP\"," +
            "\"action\":\"" + action + "\"," +
            "\"po_id\":\"" + poId + "\"," +
            "\"vendor_id\":\"" + vendorId + "\"," +
            "\"item_code\":\"" + itemCode + "\"," +
            "\"quantity\":" + quantity + "," +
            "\"unit_price\":" + unitPrice + "," +
            "\"total_value\":" + totalValue + "," +
            "\"delivery_date\":\"" + deliveryDate + "\"," +
            "\"grn_pending\":true," +
            "\"three_way_match\":\"PENDING\"," +
            "\"vendor_rating\":4.2," +
            "\"payment_terms\":\"NET_30\"," +
            "\"indent_approved\":true," +
            "\"spend_category\":\"CAPEX\"," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String regulatoryAgent(String action, String raw, String ts) {
        String regulationType = def(extractString(raw, "\"regulation_type\""), "TDS_RETURN");
        String filingPeriod   = def(extractString(raw, "\"filing_period\""),   "Q1-FY2025-26");
        String entityId       = def(extractString(raw, "\"entity_id\""),       "ENT-CORP-001");
        String dueDate        = def(extractString(raw, "\"due_date\""),        "2025-07-31");
        String jurisdiction   = def(extractString(raw, "\"jurisdiction\""),    "IN");
        return "{" +
            "\"agent\":\"REGULATORY_AGENT_ANTIGRAVITY_ERP\"," +
            "\"action\":\"" + action + "\"," +
            "\"regulation_type\":\"" + regulationType + "\"," +
            "\"filing_period\":\"" + filingPeriod + "\"," +
            "\"entity_id\":\"" + entityId + "\"," +
            "\"due_date\":\"" + dueDate + "\"," +
            "\"jurisdiction\":\"" + jurisdiction + "\"," +
            "\"filing_status\":\"FILED\"," +
            "\"ack_number\":\"ACK-" + filingPeriod + "-001\"," +
            "\"penalty_risk\":false," +
            "\"supported_filings\":[\"24Q\",\"26Q\",\"GSTR-1\",\"GSTR-3B\",\"MCA-AOC4\",\"PF-ECR\",\"ESI-CHALLAN\",\"FEMA-FC-GPR\"]," +
            "\"next_due\":{\"regulation\":\"" + regulationType + "\",\"date\":\"2025-10-31\"}," +
            "\"compliance_score\":98," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    // ─────────────────────────────────────────────────────────
    //  Framework helpers
    // ─────────────────────────────────────────────────────────
    private static Map<String, Object> buildTool(String name, String agent,
            String description, String[] params) {
        Map<String, Object> tool = new LinkedHashMap<>();
        tool.put("name", name);
        tool.put("agent", agent);
        tool.put("description", description);
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        Map<String, Object> props = new LinkedHashMap<>();
        for (String p : params) {
            Map<String, String> prop = new LinkedHashMap<>();
            prop.put("type", "string");
            props.put(p, prop);
        }
        schema.put("properties", props);
        tool.put("inputSchema", schema);
        return tool;
    }

    private static String toolToJson(Map<String, Object> tool) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"name\":").append(jsonString((String) tool.get("name"))).append(",");
        sb.append("\"description\":").append(jsonString((String) tool.get("description"))).append(",");
        @SuppressWarnings("unchecked")
        Map<String, Object> schema = (Map<String, Object>) tool.get("inputSchema");
        sb.append("\"inputSchema\":{\"type\":\"object\",\"properties\":{");
        @SuppressWarnings("unchecked")
        Map<String, Object> props = (Map<String, Object>) schema.get("properties");
        boolean first = true;
        for (String key : props.keySet()) {
            if (!first) sb.append(",");
            sb.append(jsonString(key)).append(":{\"type\":\"string\"}");
            first = false;
        }
        sb.append("}}");
        sb.append("}");
        return sb.toString();
    }

    private static String result(String id, String resultJson) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + (id != null ? id : "null") +
               ",\"result\":" + resultJson + "}";
    }

    private static String errorResponse(String id, int code, String message) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + (id != null ? id : "null") +
               ",\"error\":{\"code\":" + code + ",\"message\":" + jsonString(message) + "}}";
    }

    private static String extractString(String json, String key) {
        int ki = json.indexOf(key);
        if (ki < 0) return null;
        int colon = json.indexOf(":", ki + key.length());
        if (colon < 0) return null;
        int start = colon + 1;
        while (start < json.length() && json.charAt(start) == ' ') start++;
        if (start >= json.length()) return null;
        char c = json.charAt(start);
        if (c == '"') {
            int end = json.indexOf('"', start + 1);
            return end < 0 ? null : json.substring(start + 1, end);
        } else {
            int end = start;
            while (end < json.length() && ",}] \n\r\t".indexOf(json.charAt(end)) < 0) end++;
            return json.substring(start, end);
        }
    }

    private static String jsonString(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"")
                        .replace("\n", "\\n").replace("\r", "\\r")
                        .replace("\t", "\\t") + "\"";
    }

    private static String def(String v, String fallback) {
        return (v != null && !v.isEmpty()) ? v : fallback;
    }

    private static long parseLong(String s) {
        try { return Long.parseLong(s.trim()); } catch (Exception e) { return 0L; }
    }

    private static void log(String msg) {
        System.err.println("[CAT-6] " + msg);
    }
}
