package com.ecoskiller.antigravity.cat26.agents;

import com.ecoskiller.antigravity.cat26.model.McpModels.*;

import java.time.Instant;
import java.util.*;

// ══════════════════════════════════════════════════════════════════════
//  AGENT 9 — SAFETY_COMPLIANCE_MONITOR_AGENT
// ══════════════════════════════════════════════════════════════════════
class SafetyComplianceMonitorAgent {
    static final String NAME = "SAFETY_COMPLIANCE_MONITOR_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("safety_compliance__run_check",
                "Run safety compliance check for a venue, event, or organizer. Validates child safety, emergency exits, and operational safety protocols.",
                new InputSchema(Map.of(
                    "entity_id",   new PropSchema("string","Venue/Event/Organizer ID"),
                    "check_type",  new PropSchema("string","CHILD_SAFETY, VENUE_SAFETY, OPERATIONAL, ALL",
                        List.of("CHILD_SAFETY","VENUE_SAFETY","OPERATIONAL","ALL"))
                ), List.of("entity_id","check_type"))),
            new McpTool("safety_compliance__get_open_incidents",
                "Get all open safety incidents for a network or organizer with severity and resolution status.",
                new InputSchema(Map.of(
                    "network_id", new PropSchema("string","Network or organizer ID"),
                    "severity",   new PropSchema("string","CRITICAL, HIGH, MEDIUM, ALL",
                        List.of("CRITICAL","HIGH","MEDIUM","ALL"))
                ), List.of("network_id","severity")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "safety_compliance__run_check" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("check_type", args.get("check_type"));
                r.put("safety_score", 91.0);
                r.put("status","COMPLIANT");
                r.put("checks_passed", 22);
                r.put("checks_failed", 1);
                r.put("failures", List.of(
                    Map.of("check","First aid kit expiry","severity","MEDIUM","action","Replace by event date")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "safety_compliance__get_open_incidents" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("network_id", args.get("network_id"));
                r.put("open_incidents", 2);
                r.put("incidents", List.of(
                    Map.of("id","INC-0012","type","CHILD_SAFETY","severity","HIGH",
                           "description","Unverified volunteer present at youth event","status","UNDER_REVIEW"),
                    Map.of("id","INC-0018","type","VENUE_SAFETY","severity","MEDIUM",
                           "description","Emergency exit blocked at venue ORG-044","status","REMEDIATION_PENDING")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 10 — STANDARD_AUDIT_AGENT
// ══════════════════════════════════════════════════════════════════════
class StandardAuditAgent {
    static final String NAME = "STANDARD_AUDIT_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("std_audit__run_audit",
                "Run a standardized audit for an organizer, event, or network node. Returns audit report with findings.",
                new InputSchema(Map.of(
                    "entity_id",  new PropSchema("string","Entity to audit"),
                    "audit_type", new PropSchema("string","FINANCIAL, OPERATIONAL, COMPLIANCE, FULL",
                        List.of("FINANCIAL","OPERATIONAL","COMPLIANCE","FULL")),
                    "period",     new PropSchema("string","Audit period e.g. 2025-Q2")
                ), List.of("entity_id","audit_type"))),
            new McpTool("std_audit__get_audit_history",
                "Retrieve audit history for an entity with scores and findings over time.",
                new InputSchema(Map.of(
                    "entity_id", new PropSchema("string","Entity ID"),
                    "limit",     new PropSchema("integer","Number of past audits to return. Default: 5")
                ), List.of("entity_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "std_audit__run_audit" -> {
                String auditId = "AUD-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("audit_id", auditId);
                r.put("entity_id", args.get("entity_id"));
                r.put("audit_type", args.get("audit_type"));
                r.put("period", args.getOrDefault("period","2025-Q2"));
                r.put("overall_score", 84.2);
                r.put("status","AUDIT_PASSED");
                r.put("findings", List.of(
                    Map.of("area","Financial Records","score",89.0,"status","PASS"),
                    Map.of("area","Operational Docs", "score",81.0,"status","PASS"),
                    Map.of("area","KYC Compliance",   "score",76.0,"status","CONDITIONAL_PASS","note","2 trainer docs expiring soon")
                ));
                r.put("next_audit_due","2025-10-01");
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "std_audit__get_audit_history" -> {
                int limit = ((Number) args.getOrDefault("limit",5)).intValue();
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("audit_history", List.of(
                    Map.of("audit_id","AUD-2025Q2","period","2025-Q2","score",84.2,"status","PASSED"),
                    Map.of("audit_id","AUD-2025Q1","period","2025-Q1","score",79.8,"status","PASSED"),
                    Map.of("audit_id","AUD-2024Q4","period","2024-Q4","score",71.5,"status","CONDITIONAL")
                ).subList(0, Math.min(limit, 3)));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 11 — CONTRACT_MONITORING_AGENT
// ══════════════════════════════════════════════════════════════════════
class ContractMonitoringAgent {
    static final String NAME = "CONTRACT_MONITORING_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("contract_monitor__get_active_contracts",
                "List all active contracts for an organizer or network node with status, expiry, and key terms.",
                new InputSchema(Map.of(
                    "entity_id",        new PropSchema("string","Organizer/Entity ID"),
                    "expiry_within_days",new PropSchema("integer","Flag contracts expiring within N days. Default: 30")
                ), List.of("entity_id"))),
            new McpTool("contract_monitor__check_milestone",
                "Check whether contract milestones and SLAs have been met. Returns compliance status per milestone.",
                new InputSchema(Map.of(
                    "contract_id", new PropSchema("string","Contract ID"),
                    "milestone_id",new PropSchema("string","Specific milestone ID. Optional — all if omitted")
                ), List.of("contract_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "contract_monitor__get_active_contracts" -> {
                int expiryDays = ((Number) args.getOrDefault("expiry_within_days", 30)).intValue();
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("total_active_contracts", 7);
                r.put("expiring_within_days", expiryDays);
                r.put("expiring_soon", List.of(
                    Map.of("contract_id","CTR-1042","type","FRANCHISE_AGREEMENT","expires","2025-07-10",
                           "days_remaining",21,"status","RENEWAL_PENDING"),
                    Map.of("contract_id","CTR-1088","type","TRAINER_CONTRACT","expires","2025-07-28",
                           "days_remaining",39,"status","ACTIVE")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "contract_monitor__check_milestone" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("contract_id", args.get("contract_id"));
                r.put("milestones_checked", 4);
                r.put("milestones_met", 3);
                r.put("milestones_missed", 1);
                r.put("details", List.of(
                    Map.of("milestone","Student enrollment target 500","status","MET","actual",542),
                    Map.of("milestone","Revenue share submission Q1","status","MET","actual","On time"),
                    Map.of("milestone","Trainer certification 100%","status","MISSED","actual","82% certified")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 12 — LEGAL_DOCUMENT_MANAGEMENT_AGENT
// ══════════════════════════════════════════════════════════════════════
class LegalDocumentManagementAgent {
    static final String NAME = "LEGAL_DOCUMENT_MANAGEMENT_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("legal_docs__get_document_status",
                "Get status of required legal documents for an organizer (MOUs, agreements, registrations, certificates).",
                new InputSchema(Map.of(
                    "entity_id",  new PropSchema("string","Organizer/Entity ID"),
                    "doc_category",new PropSchema("string","ALL, REGISTRATION, MOU, COMPLIANCE, KYC",
                        List.of("ALL","REGISTRATION","MOU","COMPLIANCE","KYC"))
                ), List.of("entity_id"))),
            new McpTool("legal_docs__flag_expiring",
                "Flag all legal documents expiring within N days for a network. Optionally trigger renewal reminders.",
                new InputSchema(Map.of(
                    "network_id",    new PropSchema("string","Network ID"),
                    "expiry_days",   new PropSchema("integer","Days to expiry window. Default: 60"),
                    "send_reminders",new PropSchema("boolean","Send renewal reminders. Default: false")
                ), List.of("network_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "legal_docs__get_document_status" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("total_required_docs", 12);
                r.put("docs_valid", 10);
                r.put("docs_expired", 1);
                r.put("docs_missing", 1);
                r.put("documents", List.of(
                    Map.of("doc","Society Registration","status","VALID","expiry","2027-03-31"),
                    Map.of("doc","PAN Card","status","VALID","expiry","N/A"),
                    Map.of("doc","GST Certificate","status","EXPIRED","expiry","2024-12-31","action","RENEW_URGENT"),
                    Map.of("doc","MSME Registration","status","MISSING","action","SUBMIT_REQUIRED")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "legal_docs__flag_expiring" -> {
                int days = ((Number) args.getOrDefault("expiry_days", 60)).intValue();
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("network_id", args.get("network_id"));
                r.put("expiry_window_days", days);
                r.put("expiring_doc_count", 8);
                r.put("reminders_sent", Boolean.TRUE.equals(args.getOrDefault("send_reminders", false)) ? 8 : 0);
                r.put("expiring_docs", List.of(
                    Map.of("entity_id","ORG-0041","doc","GST Certificate","expires","2025-07-15","days",26),
                    Map.of("entity_id","ORG-0078","doc","Trainer Insurance","expires","2025-08-01","days",43)
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 13 — GOVERNMENT_SCHEME_FUNDING_AGENT
// ══════════════════════════════════════════════════════════════════════
class GovernmentSchemeFundingAgent {
    static final String NAME = "GOVERNMENT_SCHEME_FUNDING_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("govt_scheme__find_eligible_schemes",
                "Find government funding schemes an organizer or program is eligible for based on profile and activities.",
                new InputSchema(Map.of(
                    "entity_id",  new PropSchema("string","Organizer/Program ID"),
                    "state",      new PropSchema("string","State code e.g. MH, UP, GJ"),
                    "domain",     new PropSchema("string","SKILLING, EDUCATION, SPORTS, RURAL_DEV, WOMEN_EMP",
                        List.of("SKILLING","EDUCATION","SPORTS","RURAL_DEV","WOMEN_EMP"))
                ), List.of("entity_id"))),
            new McpTool("govt_scheme__track_application",
                "Track status of a submitted government scheme funding application.",
                new InputSchema(Map.of(
                    "application_id", new PropSchema("string","Application reference ID"),
                    "scheme_id",      new PropSchema("string","Scheme identifier")
                ), List.of("application_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "govt_scheme__find_eligible_schemes" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("state", args.getOrDefault("state","MH"));
                r.put("eligible_schemes", List.of(
                    Map.of("scheme","PM Kaushal Vikas Yojana 4.0","amount_inr",1200000,"deadline","2025-09-30","status","OPEN"),
                    Map.of("scheme","Maharashtra Skill Mission","amount_inr",800000,"deadline","2025-08-15","status","OPEN"),
                    Map.of("scheme","National Apprenticeship Scheme","amount_inr",500000,"deadline","2025-10-31","status","OPEN")
                ));
                r.put("total_potential_funding_inr", 2500000);
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "govt_scheme__track_application" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("application_id", args.get("application_id"));
                r.put("scheme_id", args.get("scheme_id"));
                r.put("status","UNDER_REVIEW");
                r.put("submitted_on","2025-05-20");
                r.put("last_updated","2025-06-10");
                r.put("next_action","Field verification scheduled — 2025-07-08");
                r.put("estimated_disbursement","2025-08-30");
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 14 — CSR_FUNDS_ALLOCATION_AGENT
// ══════════════════════════════════════════════════════════════════════
class CsrFundsAllocationAgent {
    static final String NAME = "CSR_FUNDS_ALLOCATION_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("csr_funds__find_csr_partners",
                "Find CSR-eligible corporate partners for network programs based on focus area, geography, and CSR budget.",
                new InputSchema(Map.of(
                    "program_id",   new PropSchema("string","Program/Activity ID"),
                    "focus_area",   new PropSchema("string","EDUCATION, SKILLING, RURAL, WOMEN, ENVIRONMENT"),
                    "state",        new PropSchema("string","State filter e.g. MH")
                ), List.of("program_id","focus_area"))),
            new McpTool("csr_funds__track_allocation",
                "Track CSR fund allocation and utilization for a program or organizer.",
                new InputSchema(Map.of(
                    "entity_id", new PropSchema("string","Program or Organizer ID"),
                    "period",    new PropSchema("string","FY or quarter e.g. 2025-26")
                ), List.of("entity_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "csr_funds__find_csr_partners" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("program_id", args.get("program_id"));
                r.put("focus_area", args.get("focus_area"));
                r.put("potential_partners", List.of(
                    Map.of("company","Tata Consultancy Services","csr_budget_inr",5000000,"alignment","HIGH","contact","csr@tcs.com"),
                    Map.of("company","Infosys Foundation","csr_budget_inr",3200000,"alignment","HIGH","contact","foundation@infosys.com"),
                    Map.of("company","Wipro Cares","csr_budget_inr",2100000,"alignment","MEDIUM","contact","cares@wipro.com")
                ));
                r.put("total_potential_inr", 10300000);
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "csr_funds__track_allocation" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("period", args.getOrDefault("period","2025-26"));
                r.put("total_csr_received_inr", 2800000);
                r.put("utilized_inr", 2100000);
                r.put("utilization_pct", 75.0);
                r.put("utilization_by_category", Map.of(
                    "Infrastructure",1200000, "Trainer Stipends",600000, "Student Scholarships",300000
                ));
                r.put("remaining_inr", 700000);
                r.put("reporting_due","2026-04-30");
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 15 — FRAUD_DETECTION_AGENT
// ══════════════════════════════════════════════════════════════════════
class FraudDetectionAgent {
    static final String NAME = "FRAUD_DETECTION_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("fraud_detect__scan_entity",
                "Scan an organizer or user entity for fraud signals across enrollment, financial, and identity dimensions.",
                new InputSchema(Map.of(
                    "entity_id",   new PropSchema("string","Entity to scan"),
                    "entity_type", new PropSchema("string","ORGANIZER, USER, TRAINER, INSTITUTE",
                        List.of("ORGANIZER","USER","TRAINER","INSTITUTE")),
                    "scan_depth",  new PropSchema("string","QUICK, STANDARD, DEEP",
                        List.of("QUICK","STANDARD","DEEP"))
                ), List.of("entity_id","entity_type"))),
            new McpTool("fraud_detect__get_active_alerts",
                "Get active fraud alerts across the network. Filter by severity or type.",
                new InputSchema(Map.of(
                    "severity",   new PropSchema("string","CRITICAL, HIGH, MEDIUM, ALL",
                        List.of("CRITICAL","HIGH","MEDIUM","ALL")),
                    "fraud_type", new PropSchema("string","ENROLLMENT_FRAUD, PAYMENT_FRAUD, IDENTITY_FRAUD, ALL",
                        List.of("ENROLLMENT_FRAUD","PAYMENT_FRAUD","IDENTITY_FRAUD","ALL"))
                ), List.of("severity")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "fraud_detect__scan_entity" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("entity_type", args.get("entity_type"));
                r.put("fraud_score", 12.4);
                r.put("risk_level","LOW");
                r.put("verdict","CLEAN");
                r.put("signals_checked", 28);
                r.put("anomalies_found", 1);
                r.put("anomalies", List.of(
                    Map.of("signal","Enrollment spike +400% in 3 days","severity","LOW","action","MONITOR")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "fraud_detect__get_active_alerts" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("total_active_alerts", 4);
                r.put("alerts", List.of(
                    Map.of("alert_id","FRD-0021","type","PAYMENT_FRAUD","entity","ORG-0041",
                           "severity","HIGH","detected_at","2025-06-18","status","INVESTIGATING"),
                    Map.of("alert_id","FRD-0019","type","ENROLLMENT_FRAUD","entity","USER-8821",
                           "severity","MEDIUM","detected_at","2025-06-15","status","OPEN")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 16 — FINANCIAL_ANOMALY_DETECTION_AGENT
// ══════════════════════════════════════════════════════════════════════
class FinancialAnomalyDetectionAgent {
    static final String NAME = "FINANCIAL_ANOMALY_DETECTION_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("fin_anomaly__scan_transactions",
                "Scan financial transactions for anomalies: unusual amounts, velocity spikes, round-trip transfers, or timing patterns.",
                new InputSchema(Map.of(
                    "entity_id",  new PropSchema("string","Organizer/Tenant to scan"),
                    "period",     new PropSchema("string","Period to scan e.g. 2025-06"),
                    "sensitivity",new PropSchema("string","LOW, MEDIUM, HIGH sensitivity for anomaly detection",
                        List.of("LOW","MEDIUM","HIGH"))
                ), List.of("entity_id"))),
            new McpTool("fin_anomaly__explain_anomaly",
                "Get detailed explanation and evidence for a specific detected financial anomaly.",
                new InputSchema(Map.of(
                    "anomaly_id", new PropSchema("string","Anomaly ID from scan results")
                ), List.of("anomaly_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "fin_anomaly__scan_transactions" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("period", args.getOrDefault("period","2025-06"));
                r.put("transactions_scanned", 3841);
                r.put("anomalies_detected", 3);
                r.put("anomalies", List.of(
                    Map.of("id","ANO-0041","type","VELOCITY_SPIKE","severity","HIGH",
                           "description","12 payouts in 4 hours — avg is 2/day","amount_inr",485000),
                    Map.of("id","ANO-0042","type","ROUND_AMOUNT","severity","MEDIUM",
                           "description","5 consecutive ₹50,000 exact transfers","amount_inr",250000),
                    Map.of("id","ANO-0043","type","OFF_HOURS","severity","LOW",
                           "description","Transaction at 03:47 AM IST","amount_inr",12500)
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "fin_anomaly__explain_anomaly" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("anomaly_id", args.get("anomaly_id"));
                r.put("type","VELOCITY_SPIKE");
                r.put("baseline","avg 2 payouts/day over 90 days");
                r.put("observed","12 payouts in 4-hour window on 2025-06-18");
                r.put("deviation_sigma", 4.7);
                r.put("recommendation","HOLD_AND_INVESTIGATE");
                r.put("evidence_transaction_ids", List.of("TXN-88412","TXN-88415","TXN-88421","TXN-88430"));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

/** Public dispatcher for Agents 9–16 */
public class AgentsGroup2 {
    public static List<McpTool> allTools() {
        List<McpTool> all = new ArrayList<>();
        all.addAll(SafetyComplianceMonitorAgent.tools());
        all.addAll(StandardAuditAgent.tools());
        all.addAll(ContractMonitoringAgent.tools());
        all.addAll(LegalDocumentManagementAgent.tools());
        all.addAll(GovernmentSchemeFundingAgent.tools());
        all.addAll(CsrFundsAllocationAgent.tools());
        all.addAll(FraudDetectionAgent.tools());
        all.addAll(FinancialAnomalyDetectionAgent.tools());
        return all;
    }
    public static AgentResult dispatch(String tool, Map<String, Object> args) {
        if (tool.startsWith("safety_compliance__"))return SafetyComplianceMonitorAgent.execute(tool,args);
        if (tool.startsWith("std_audit__"))        return StandardAuditAgent.execute(tool,args);
        if (tool.startsWith("contract_monitor__")) return ContractMonitoringAgent.execute(tool,args);
        if (tool.startsWith("legal_docs__"))       return LegalDocumentManagementAgent.execute(tool,args);
        if (tool.startsWith("govt_scheme__"))      return GovernmentSchemeFundingAgent.execute(tool,args);
        if (tool.startsWith("csr_funds__"))        return CsrFundsAllocationAgent.execute(tool,args);
        if (tool.startsWith("fraud_detect__"))     return FraudDetectionAgent.execute(tool,args);
        if (tool.startsWith("fin_anomaly__"))      return FinancialAnomalyDetectionAgent.execute(tool,args);
        return new AgentResult("GROUP2","ERROR", Map.of("error","No agent for: " + tool));
    }
}
