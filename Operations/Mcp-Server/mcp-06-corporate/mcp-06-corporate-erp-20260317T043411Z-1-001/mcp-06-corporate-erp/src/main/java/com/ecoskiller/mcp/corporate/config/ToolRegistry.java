package com.ecoskiller.mcp.corporate.config;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Antigravity Tool Registry for mcp-06-corporate-erp.
 *
 * <p>All 12 CAT-06 agents are registered with full JSON Schema input contracts.
 * SEALED agents carry stricter validation rules enforced by the dispatcher.</p>
 */
@Component
public class ToolRegistry {

    public record ToolDef(
        String name,
        String description,
        Map<String, Object> inputSchema,
        String agentRef,
        boolean sealed
    ) {}

    private final List<ToolDef> tools = List.of(

        // ── 1. ACCOUNTING_AGENT (SEALED) ──────────────────────────────────────
        new ToolDef(
            "accounting",
            """
            Full double-entry accounting engine for the corporate ERP.
            Manages chart of accounts, journal entries, vouchers, bank
            reconciliation, and period-close. ANTIGRAVITY_SEALED — posted
            entries are immutable; corrections require reversal vouchers.
            """,
            schema(Map.of(
                "tenant_id",     prop("string",  "Tenant UUID (mandatory)"),
                "action",        propEnum("Action",
                                   "create_account","update_account","deactivate_account",
                                   "post_journal","reverse_journal",
                                   "bank_reconcile","period_close","period_reopen",
                                   "get_trial_balance","get_balance_sheet","get_pnl"),
                "account_code",  prop("string",  "Chart-of-account code"),
                "voucher",       prop("object",  "Journal voucher object (debit/credit lines)"),
                "fiscal_period", prop("string",  "YYYY-MM (e.g. 2025-03)"),
                "dry_run",       prop("boolean", "Validate without posting (default false)")
            ), List.of("tenant_id", "action")),
            "ACCOUNTING_AGENT__ANTIGRAVITY_CORPORATE_ERP.md", true
        ),

        // ── 2. ACCOUNTING_SYNC_AGENT ───────────────────────────────────────────
        new ToolDef(
            "accounting_sync",
            """
            Synchronises accounting data between the Ecoskiller ERP and
            external accounting platforms (Tally, Zoho Books, QuickBooks,
            SAP). Supports pull, push, and bidirectional delta-sync modes.
            """,
            schema(Map.of(
                "tenant_id",   prop("string",  "Tenant UUID"),
                "action",      propEnum("Action",
                                 "start_sync","get_status","pause","resume",
                                 "rollback","configure","get_sync_report"),
                "platform",    propEnum("External accounting platform",
                                 "tally","zoho_books","quickbooks","sap","custom"),
                "sync_mode",   propEnum("Sync direction",
                                 "pull","push","bidirectional"),
                "sync_id",     prop("string",  "Sync job ID (status/rollback ops)"),
                "dry_run",     prop("boolean", "Simulate only")
            ), List.of("tenant_id", "action")),
            "ACCOUNTING_SYNC_AGENT.md", false
        ),

        // ── 3. ASSET_AGENT (SEALED) ────────────────────────────────────────────
        new ToolDef(
            "asset",
            """
            Fixed-asset lifecycle management: capitalisation, depreciation
            (SLM, WDV, Units-of-Production), revaluation, disposal, and
            insurance. ANTIGRAVITY_SEALED — disposal and revaluation require
            CFO-level approval token.
            """,
            schema(Map.of(
                "tenant_id",    prop("string",  "Tenant UUID"),
                "action",       propEnum("Action",
                                  "register_asset","update_asset",
                                  "run_depreciation","revalue_asset","dispose_asset",
                                  "transfer_asset","get_asset_register",
                                  "get_depreciation_schedule","insure_asset"),
                "asset_id",     prop("string",  "Asset UUID"),
                "method",       propEnum("Depreciation method",
                                  "SLM","WDV","units_of_production"),
                "cfo_token",    prop("string",  "CFO approval token (disposal/revalue)"),
                "payload",      prop("object",  "Action-specific data"),
                "fiscal_period",prop("string",  "YYYY-MM for depreciation run")
            ), List.of("tenant_id", "action")),
            "ASSET_AGENT_ANTIGRAVITY_ERP.md", true
        ),

        // ── 4. BUDGET_AGENT ────────────────────────────────────────────────────
        new ToolDef(
            "budget",
            """
            Corporate budget planning, allocation, variance analysis, and
            rolling forecasts. Supports departmental budgets, capex/opex
            split, and alerts when spending approaches limits.
            """,
            schema(Map.of(
                "tenant_id",    prop("string",  "Tenant UUID"),
                "action",       propEnum("Action",
                                  "create_budget","update_budget","approve_budget",
                                  "allocate_to_dept","get_variance_report",
                                  "set_alert_threshold","get_forecast",
                                  "lock_budget","get_utilisation"),
                "budget_id",    prop("string",  "Budget UUID"),
                "dept_id",      prop("string",  "Department UUID"),
                "fiscal_year",  prop("string",  "YYYY"),
                "payload",      prop("object",  "Budget lines / allocation data")
            ), List.of("tenant_id", "action")),
            "BUDGET_AGENT.md", false
        ),

        // ── 5. DMS_AGENT (SEALED) ──────────────────────────────────────────────
        new ToolDef(
            "dms",
            """
            Document Management System — upload, version, tag, search, and
            retention-policy management for corporate documents (contracts,
            invoices, compliance records). ANTIGRAVITY_SEALED — permanent
            deletion requires dual authorisation and is audit-logged.
            """,
            schema(Map.of(
                "tenant_id",     prop("string",  "Tenant UUID"),
                "action",        propEnum("Action",
                                   "upload_document","update_metadata","get_document",
                                   "new_version","search","tag_document",
                                   "set_retention_policy","archive","delete",
                                   "get_audit_trail"),
                "doc_id",        prop("string",  "Document UUID"),
                "document_type", propEnum("Document category",
                                   "contract","invoice","po","compliance",
                                   "board_resolution","tax_filing","other"),
                "tags",          prop("array",   "List of string tags"),
                "authoriser_id", prop("string",  "Second authoriser for delete"),
                "payload",       prop("object",  "Upload metadata or search query")
            ), List.of("tenant_id", "action")),
            "DMS_AGENT_ANTIGRAVITY_ERP.md", true
        ),

        // ── 6. ERP_ANALYTICS_AGENT ─────────────────────────────────────────────
        new ToolDef(
            "erp_analytics",
            """
            Cross-module ERP analytics: revenue trends, cost centre analysis,
            headcount vs payroll ratio, procurement spend analysis, and
            custom KPI dashboards. Queries ClickHouse via the analytics bus.
            """,
            schema(Map.of(
                "tenant_id",  prop("string",  "Tenant UUID"),
                "action",     propEnum("Action",
                                "run_report","schedule_report","get_kpi_dashboard",
                                "create_custom_kpi","get_cost_centre_breakdown",
                                "export_data","get_drill_down"),
                "report_type",propEnum("Report category",
                                "revenue","cost","headcount","procurement",
                                "asset","compliance","custom"),
                "date_from",  prop("string",  "ISO-8601 start date"),
                "date_to",    prop("string",  "ISO-8601 end date"),
                "filters",    prop("object",  "Optional dimension filters"),
                "format",     propEnum("Export format","json","csv","xlsx","pdf")
            ), List.of("tenant_id", "action")),
            "ERP_ANALYTICS_AGENT.md", false
        ),

        // ── 7. GST_CONNECT_AGENT ───────────────────────────────────────────────
        new ToolDef(
            "gst_connect",
            """
            GST compliance and filing automation: GSTIN validation, invoice
            reconciliation (GSTR-2A/2B), return preparation (GSTR-1/3B/9),
            e-invoicing (IRN generation), and e-way bill management.
            Integrates directly with the GSTN portal.
            """,
            schema(Map.of(
                "tenant_id",   prop("string",  "Tenant UUID"),
                "action",      propEnum("Action",
                                 "validate_gstin","reconcile_2a_2b",
                                 "prepare_gstr1","prepare_gstr3b","prepare_gstr9",
                                 "file_return","generate_irn","cancel_irn",
                                 "generate_eway_bill","cancel_eway_bill",
                                 "get_itc_summary","get_filing_status"),
                "gstin",       prop("string",  "15-digit GSTIN"),
                "return_period",prop("string", "MM-YYYY filing period"),
                "invoice_data",prop("object",  "Invoice payload for IRN/e-way"),
                "filing_id",   prop("string",  "Filing job reference")
            ), List.of("tenant_id", "action")),
            "GST_CONNECT_AGENT.md", false
        ),

        // ── 8. HRMS_AGENT (SEALED) ─────────────────────────────────────────────
        new ToolDef(
            "hrms",
            """
            Human Resource Management System: employee master, org hierarchy,
            leave management, performance cycles, offboarding, and ESIC/PF
            compliance. ANTIGRAVITY_SEALED — salary-grade changes and
            terminations require dual HR + management approval.
            """,
            schema(Map.of(
                "tenant_id",   prop("string",  "Tenant UUID"),
                "action",      propEnum("Action",
                                 "onboard_employee","update_employee","offboard_employee",
                                 "apply_leave","approve_leave","reject_leave",
                                 "update_salary_grade","get_org_chart",
                                 "start_performance_cycle","submit_appraisal",
                                 "get_headcount_report","get_compliance_summary"),
                "employee_id", prop("string",  "Employee UUID"),
                "manager_token",prop("string", "Manager approval token (grade change/offboard)"),
                "hr_token",    prop("string",  "HR approval token (offboard/grade change)"),
                "payload",     prop("object",  "Action-specific payload")
            ), List.of("tenant_id", "action")),
            "HRMS_AGENT_ANTIGRAVITY_SEALED.md", true
        ),

        // ── 9. LEDGER_MIGRATION_AGENT ──────────────────────────────────────────
        new ToolDef(
            "ledger_migration",
            """
            Migrates historical ledger data from legacy accounting systems
            (Tally, SAP, Oracle, custom) into the Ecoskiller ERP ledger.
            Runs as a staged pipeline with opening-balance validation and
            rollback support.
            """,
            schema(Map.of(
                "tenant_id",    prop("string",  "Tenant UUID"),
                "action",       propEnum("Action",
                                  "start_migration","get_status","pause","resume",
                                  "rollback","validate_opening_balances",
                                  "commit","get_migration_report"),
                "source_system",propEnum("Source accounting system",
                                  "tally","sap","oracle","quickbooks","custom"),
                "source_config",prop("object",  "Connection config for source system"),
                "migration_id", prop("string",  "Job ID (status/rollback ops)"),
                "from_date",    prop("string",  "Ledger history start date (YYYY-MM-DD)"),
                "to_date",      prop("string",  "Ledger history end date (YYYY-MM-DD)"),
                "dry_run",      prop("boolean", "Simulate without writing")
            ), List.of("tenant_id", "action")),
            "LEDGER_MIGRATION_AGENT.md", false
        ),

        // ── 10. PAYROLL_AGENT (SEALED) ─────────────────────────────────────────
        new ToolDef(
            "payroll",
            """
            End-to-end payroll processing: salary computation, statutory
            deductions (TDS, PF, ESIC, PT), pay-slip generation, bank
            disbursement files, Form-16, and payroll register.
            ANTIGRAVITY_SEALED — payroll runs require CFO sign-off before
            bank file release.
            """,
            schema(Map.of(
                "tenant_id",   prop("string",  "Tenant UUID"),
                "action",      propEnum("Action",
                                 "run_payroll","preview_payroll",
                                 "approve_payroll","release_bank_file",
                                 "generate_payslips","compute_tds",
                                 "generate_form16","get_payroll_register",
                                 "reprocess_employee","get_statutory_summary"),
                "payroll_month",prop("string", "MM-YYYY payroll period"),
                "employee_id", prop("string",  "Employee UUID (single-employee ops)"),
                "cfo_token",   prop("string",  "CFO approval token (approve/release)"),
                "payload",     prop("object",  "Additional parameters")
            ), List.of("tenant_id", "action")),
            "PAYROLL_AGENT_ANTIGRAVITY_ERP.md", true
        ),

        // ── 11. PROCUREMENT_AGENT (SEALED) ─────────────────────────────────────
        new ToolDef(
            "procurement",
            """
            Procure-to-pay lifecycle: purchase requisitions, vendor management,
            RFQ/RFP, purchase orders, GRN, invoice matching, and vendor
            payments. ANTIGRAVITY_SEALED — POs above ₹1 lakh require
            multi-level approval and budget availability check.
            """,
            schema(Map.of(
                "tenant_id",    prop("string",  "Tenant UUID"),
                "action",       propEnum("Action",
                                  "create_requisition","approve_requisition",
                                  "create_vendor","update_vendor","deactivate_vendor",
                                  "create_rfq","evaluate_rfq",
                                  "create_po","approve_po","amend_po","close_po",
                                  "record_grn","three_way_match",
                                  "create_vendor_invoice","schedule_payment",
                                  "get_spend_report"),
                "po_id",        prop("string",  "Purchase Order UUID"),
                "vendor_id",    prop("string",  "Vendor UUID"),
                "amount",       prop("number",  "PO value in INR paise"),
                "approver_token",prop("string", "Approval token for POs > ₹1 lakh"),
                "payload",      prop("object",  "Line items / extended data")
            ), List.of("tenant_id", "action")),
            "PROCUREMENT_AGENT_ANTIGRAVITY_ERP.md", true
        ),

        // ── 12. REGULATORY_AGENT (SEALED) ──────────────────────────────────────
        new ToolDef(
            "regulatory",
            """
            Regulatory compliance calendar and filing management: MCA (ROC
            filings), income-tax returns, FEMA, labour-law registers (Shops &
            Establishments, Factories Act), and custom regulatory frameworks.
            ANTIGRAVITY_SEALED — filed documents are locked and any amendment
            creates a new version with reason code.
            """,
            schema(Map.of(
                "tenant_id",    prop("string",  "Tenant UUID"),
                "action",       propEnum("Action",
                                  "get_compliance_calendar","mark_filed",
                                  "upload_filing","amend_filing",
                                  "set_reminder","get_overdue_list",
                                  "generate_mca_form","generate_it_return",
                                  "get_labour_register","get_filing_history"),
                "regulation",   propEnum("Regulation category",
                                  "MCA","income_tax","GST","FEMA",
                                  "labour_law","custom"),
                "filing_id",    prop("string",  "Filing reference ID"),
                "due_date",     prop("string",  "ISO-8601 due date override"),
                "amendment_reason", prop("string", "Mandatory for amend_filing"),
                "payload",      prop("object",  "Form data or document reference")
            ), List.of("tenant_id", "action")),
            "REGULATORY_AGENT_ANTIGRAVITY_ERP.md", true
        )
    );

    public List<ToolDef> all()         { return tools; }
    public ToolDef find(String name)   {
        return tools.stream().filter(t -> t.name().equals(name)).findFirst().orElse(null);
    }

    // ── Schema helpers ────────────────────────────────────────────────────────
    private static Map<String, Object> prop(String type, String desc) {
        return Map.of("type", type, "description", desc);
    }
    private static Map<String, Object> propEnum(String desc, String... values) {
        return Map.of("type", "string", "description", desc, "enum", List.of(values));
    }
    private static Map<String, Object> schema(Map<String, Object> props, List<String> required) {
        Map<String, Object> s = new LinkedHashMap<>();
        s.put("type", "object");
        s.put("properties", props);
        s.put("required", required);
        return s;
    }
}
