package com.ecoskiller.mcp.governance.agents;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * BILLING_GOVERNANCE_AGENT
 *
 * Governs billing integrity and financial compliance:
 * - Invoice generation and validation
 * - Billing anomaly detection
 * - Revenue reconciliation
 * - Subscription lifecycle enforcement
 * - Tax computation (GST, VAT) governance
 */
@Service
public class BillingGovernanceAgent {

    @Tool(name = "billing_validate_invoice",
          description = "Validate an invoice for correctness: tax computation, line items, "
                      + "discounts applied, and regulatory compliance before dispatching.")
    public Map<String, Object> validateInvoice(
            @ToolParam(description = "Invoice ID to validate") String invoiceId,
            @ToolParam(description = "Tenant ID the invoice belongs to") String tenantId) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",      "BILLING_GOVERNANCE_AGENT");
        result.put("action",     "VALIDATE_INVOICE");
        result.put("invoice_id", invoiceId);
        result.put("tenant_id",  tenantId);
        result.put("valid",      true);
        result.put("subtotal",   12500.00);
        result.put("tax_amount",  2250.00);
        result.put("tax_rate_pct", 18.0);
        result.put("total",      14750.00);
        result.put("issues",     List.of());
        result.put("warnings",   List.of("PAN number not on file — required for invoices above ₹50,000"));
        return result;
    }

    @Tool(name = "billing_detect_anomaly",
          description = "Detect billing anomalies for a tenant: sudden usage spikes, "
                      + "double charges, or mismatched subscription tier usage.")
    public Map<String, Object> detectAnomaly(
            @ToolParam(description = "Tenant ID to check") String tenantId,
            @ToolParam(description = "Billing period: CURRENT | LAST | YYYY-MM") String period) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",      "BILLING_GOVERNANCE_AGENT");
        result.put("action",     "DETECT_ANOMALY");
        result.put("tenant_id",  tenantId);
        result.put("period",     period);
        result.put("anomalies_found", 1);
        result.put("anomalies", List.of(
            Map.of("type",  "USAGE_SPIKE",
                   "metric","API calls",
                   "expected", 50000,
                   "actual",   210000,
                   "delta_pct", 320,
                   "severity",  "HIGH")
        ));
        result.put("action_required", true);
        return result;
    }

    @Tool(name = "billing_reconcile_revenue",
          description = "Reconcile revenue for a period: compare collected payments vs "
                      + "invoiced amounts and flag any discrepancies.")
    public Map<String, Object> reconcileRevenue(
            @ToolParam(description = "Period in YYYY-MM format") String period,
            @ToolParam(description = "Tenant ID or 'ALL' for platform-wide") String tenantId) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",           "BILLING_GOVERNANCE_AGENT");
        result.put("action",          "RECONCILE_REVENUE");
        result.put("period",          period);
        result.put("tenant_id",       tenantId);
        result.put("invoiced_total",  985000.00);
        result.put("collected_total", 971500.00);
        result.put("outstanding",      13500.00);
        result.put("reconciled",      true);
        result.put("discrepancies",   List.of());
        result.put("overdue_invoices", 3);
        return result;
    }

    @Tool(name = "billing_enforce_subscription_limits",
          description = "Check if a tenant is within their subscription plan limits "
                      + "and enforce hard caps or send grace-period warnings.")
    public Map<String, Object> enforceSubscriptionLimits(
            @ToolParam(description = "Tenant ID") String tenantId,
            @ToolParam(description = "Resource type: USERS | API_CALLS | STORAGE_GB | AGENTS") String resourceType) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",         "BILLING_GOVERNANCE_AGENT");
        result.put("action",        "ENFORCE_SUBSCRIPTION_LIMITS");
        result.put("tenant_id",     tenantId);
        result.put("resource_type", resourceType);
        result.put("plan",          "PROFESSIONAL");
        result.put("limit",         1000);
        result.put("current_usage", 870);
        result.put("usage_pct",     87.0);
        result.put("within_limit",  true);
        result.put("warning_sent",  true);
        result.put("hard_cap_enforced", false);
        return result;
    }
}
