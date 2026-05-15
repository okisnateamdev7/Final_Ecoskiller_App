package com.ecoskiller.antigravity.cat26.agents;

import com.ecoskiller.antigravity.cat26.model.McpModels.*;

import java.time.Instant;
import java.util.*;

// ══════════════════════════════════════════════════════════════════════
//  AGENT 17 — CRISIS_MANAGEMENT_AGENT
// ══════════════════════════════════════════════════════════════════════
class CrisisManagementAgent {
    static final String NAME = "CRISIS_MANAGEMENT_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("crisis__declare_crisis",
                "Declare a platform or network crisis event. Triggers crisis protocol, stakeholder notifications, and containment actions.",
                new InputSchema(Map.of(
                    "crisis_type",  new PropSchema("string","FINANCIAL, SAFETY, REPUTATIONAL, OPERATIONAL, DATA_BREACH",
                        List.of("FINANCIAL","SAFETY","REPUTATIONAL","OPERATIONAL","DATA_BREACH")),
                    "severity",     new PropSchema("string","P1, P2, P3",List.of("P1","P2","P3")),
                    "affected_node",new PropSchema("string","Affected network node/organizer ID"),
                    "description",  new PropSchema("string","Crisis description")
                ), List.of("crisis_type","severity","description"))),
            new McpTool("crisis__get_active_crises",
                "Get all active crisis events with containment status, responsible team, and resolution ETA.",
                new InputSchema(Map.of(
                    "severity_filter",new PropSchema("string","P1, P2, P3, ALL",List.of("P1","P2","P3","ALL"))
                ), List.of()))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "crisis__declare_crisis" -> {
                String crisisId = "CRS-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("crisis_id", crisisId);
                r.put("crisis_type", args.get("crisis_type"));
                r.put("severity", args.get("severity"));
                r.put("declared_at", Instant.now().toString());
                r.put("status","CRISIS_ACTIVE");
                r.put("war_room_activated", "P1".equals(args.get("severity")));
                r.put("notifications_sent", List.of("ops@ecoskiller.com","legal@ecoskiller.com","ceo@ecoskiller.com"));
                r.put("auto_actions_triggered", List.of("EMERGENCY_PLATFORM_LOCKDOWN queued","INSIDER_THREAT_MONITOR alerted"));
                r.put("resolution_sla_hours", "P1".equals(args.get("severity")) ? 4 : 24);
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "crisis__get_active_crises" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("total_active", 1);
                r.put("crises", List.of(
                    Map.of("crisis_id","CRS-ACTIVE01","type","REPUTATIONAL","severity","P2",
                           "declared_at","2025-06-17T08:00:00Z","status","CONTAINMENT_IN_PROGRESS",
                           "owner","comms-team","eta_resolution","2025-06-20")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 18 — EMERGENCY_RESPONSE_AGENT
// ══════════════════════════════════════════════════════════════════════
class EmergencyResponseAgent {
    static final String NAME = "EMERGENCY_RESPONSE_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("emergency__trigger_response",
                "Trigger an emergency response protocol for a specific incident at a network location.",
                new InputSchema(Map.of(
                    "incident_type",new PropSchema("string","FIRE, MEDICAL, SECURITY, NATURAL_DISASTER, CROWD_SURGE",
                        List.of("FIRE","MEDICAL","SECURITY","NATURAL_DISASTER","CROWD_SURGE")),
                    "location_id",  new PropSchema("string","Venue or location ID"),
                    "reported_by",  new PropSchema("string","Reporter user ID or name"),
                    "description",  new PropSchema("string","Incident description")
                ), List.of("incident_type","location_id","reported_by"))),
            new McpTool("emergency__get_response_status",
                "Get real-time status of an active emergency response.",
                new InputSchema(Map.of(
                    "incident_id", new PropSchema("string","Incident ID from trigger")
                ), List.of("incident_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "emergency__trigger_response" -> {
                String incId = "INC-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("incident_id", incId);
                r.put("incident_type", args.get("incident_type"));
                r.put("location_id", args.get("location_id"));
                r.put("triggered_at", Instant.now().toString());
                r.put("status","RESPONSE_INITIATED");
                r.put("emergency_contacts_notified", List.of("local_police","nearest_hospital","organizer_lead"));
                r.put("auto_actions", List.of(
                    "Venue coordinator SMS sent",
                    "Emergency services dialed via PSTN bridge",
                    "Platform incident log created"
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "emergency__get_response_status" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("incident_id", args.get("incident_id"));
                r.put("status","UNDER_CONTROL");
                r.put("responders_on_scene", true);
                r.put("casualties_reported", 0);
                r.put("last_update", Instant.now().minusSeconds(300).toString());
                r.put("next_update_due_mins", 15);
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 19 — EMERGENCY_RESPONSE_POLICY_AGENT
// ══════════════════════════════════════════════════════════════════════
class EmergencyResponsePolicyAgent {
    static final String NAME = "EMERGENCY_RESPONSE_POLICY_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("erp__get_policy_for_incident",
                "Retrieve the applicable emergency response policy document and checklist for a given incident type.",
                new InputSchema(Map.of(
                    "incident_type",new PropSchema("string","FIRE, MEDICAL, SECURITY, NATURAL_DISASTER, CROWD_SURGE",
                        List.of("FIRE","MEDICAL","SECURITY","NATURAL_DISASTER","CROWD_SURGE")),
                    "event_scale",  new PropSchema("string","SMALL(<50), MEDIUM(50-500), LARGE(>500)",
                        List.of("SMALL","MEDIUM","LARGE"))
                ), List.of("incident_type"))),
            new McpTool("erp__validate_preparedness",
                "Validate emergency preparedness of a venue or organizer against policy requirements.",
                new InputSchema(Map.of(
                    "entity_id",  new PropSchema("string","Venue or Organizer ID")
                ), List.of("entity_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "erp__get_policy_for_incident" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("incident_type", args.get("incident_type"));
                r.put("policy_id","ERP-POL-2025-" + args.get("incident_type"));
                r.put("version","v2.1");
                r.put("checklist", List.of(
                    "1. Activate evacuation alarm",
                    "2. Call 112 emergency services",
                    "3. Notify designated organizer lead within 2 min",
                    "4. Account for all participants at assembly point",
                    "5. Do NOT re-enter until all-clear from authorities"
                ));
                r.put("contacts", Map.of("emergency_hotline","112","ecoskiller_ops","1800-XXX-XXXX"));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "erp__validate_preparedness" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("preparedness_score", 78.0);
                r.put("status","CONDITIONALLY_PREPARED");
                r.put("gaps", List.of(
                    Map.of("item","Emergency drill conducted","status","MISSING","required_by","Before next event"),
                    Map.of("item","First aid trained staff","status","PARTIAL","current",1,"required",2)
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 20 — FAILURE_CASE_ANALYSIS_AGENT
// ══════════════════════════════════════════════════════════════════════
class FailureCaseAnalysisAgent {
    static final String NAME = "FAILURE_CASE_ANALYSIS_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("failure_analysis__run_rca",
                "Run Root Cause Analysis (RCA) on a failure event. Returns 5-why analysis, contributing factors, and corrective actions.",
                new InputSchema(Map.of(
                    "event_id",    new PropSchema("string","Failure event or incident ID"),
                    "event_type",  new PropSchema("string","NETWORK_DROPOUT, LOW_ENROLLMENT, PAYMENT_FAILURE, QUALITY_FAILURE")
                ), List.of("event_id","event_type"))),
            new McpTool("failure_analysis__get_failure_patterns",
                "Get recurring failure patterns in the network over a period. Returns pattern frequency and impact.",
                new InputSchema(Map.of(
                    "period_months",new PropSchema("integer","Analysis period in months. Default: 6"),
                    "category",     new PropSchema("string","OPERATIONAL, FINANCIAL, QUALITY, SAFETY, ALL")
                ), List.of()))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "failure_analysis__run_rca" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("event_id", args.get("event_id"));
                r.put("event_type", args.get("event_type"));
                r.put("root_cause","Trainer assignment lead time reduced from 7 to 2 days in Q2");
                r.put("five_why", List.of(
                    "Why: Low enrollment → No marketing done",
                    "Why: No marketing → Budget not released in time",
                    "Why: Budget delay → Approval chain too long (5 approvers)",
                    "Why: 5 approvers → Policy not reviewed since 2022",
                    "Why: Policy stale → No periodic policy review schedule"
                ));
                r.put("corrective_actions", List.of(
                    Map.of("action","Reduce approval chain to 2 for events <₹50K","owner","ops","due","2025-07-15"),
                    Map.of("action","Quarterly policy review cadence","owner","legal","due","2025-08-01")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "failure_analysis__get_failure_patterns" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("period_months", args.getOrDefault("period_months",6));
                r.put("patterns", List.of(
                    Map.of("pattern","Trainer no-show","frequency",14,"impact_level","HIGH","trend","INCREASING"),
                    Map.of("pattern","Fee collection delay","frequency",28,"impact_level","MEDIUM","trend","STABLE"),
                    Map.of("pattern","Venue booking conflict","frequency",7,"impact_level","MEDIUM","trend","DECREASING")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 21 — FRANCHISE_CONCENTRATION_RISK_AGENT
// ══════════════════════════════════════════════════════════════════════
class FranchiseConcentrationRiskAgent {
    static final String NAME = "FRANCHISE_CONCENTRATION_RISK_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("franchise_risk__assess_concentration",
                "Assess revenue and enrollment concentration risk across franchise/network nodes. Returns HHI and top-N dependency.",
                new InputSchema(Map.of(
                    "network_id", new PropSchema("string","Network ID to assess"),
                    "metric",     new PropSchema("string","REVENUE, ENROLLMENT, BOTH",List.of("REVENUE","ENROLLMENT","BOTH"))
                ), List.of("network_id"))),
            new McpTool("franchise_risk__flag_overweight_nodes",
                "Flag network nodes that contribute disproportionately to revenue or enrollment (>threshold%).",
                new InputSchema(Map.of(
                    "network_id",new PropSchema("string","Network ID"),
                    "threshold_pct",new PropSchema("number","Flag nodes above this % share. Default: 25")
                ), List.of("network_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "franchise_risk__assess_concentration" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("network_id", args.get("network_id"));
                r.put("herfindahl_index", 0.28);
                r.put("concentration_risk","MODERATE");
                r.put("top_3_nodes_share_pct", 62.0);
                r.put("recommendation","Grow bottom-50 nodes to reduce top-3 dependency below 45%");
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "franchise_risk__flag_overweight_nodes" -> {
                double threshold = ((Number) args.getOrDefault("threshold_pct", 25.0)).doubleValue();
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("network_id", args.get("network_id"));
                r.put("threshold_pct", threshold);
                r.put("flagged", List.of(
                    Map.of("node_id","ORG-0001","revenue_share_pct",31.2,"risk","HIGH"),
                    Map.of("node_id","ORG-0008","revenue_share_pct",18.4,"revenue_share_enrollment_pct",28.1,"risk","MEDIUM")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 22 — NETWORK_EXPANSION_CAP_AGENT
// ══════════════════════════════════════════════════════════════════════
class NetworkExpansionCapAgent {
    static final String NAME = "NETWORK_EXPANSION_CAP_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("expansion_cap__check_eligibility",
                "Check whether a network or organizer is eligible to expand (add new nodes, franchises, or regions).",
                new InputSchema(Map.of(
                    "entity_id",       new PropSchema("string","Organizer/Master Organizer ID"),
                    "expansion_type",  new PropSchema("string","NEW_NODE, NEW_REGION, FRANCHISE_LICENSE",
                        List.of("NEW_NODE","NEW_REGION","FRANCHISE_LICENSE"))
                ), List.of("entity_id","expansion_type"))),
            new McpTool("expansion_cap__apply_cap",
                "Apply an expansion cap or moratorium to an organizer or region. Prevents new node creation.",
                new InputSchema(Map.of(
                    "entity_id", new PropSchema("string","Entity ID to cap"),
                    "reason",    new PropSchema("string","Reason for cap e.g. QUALITY_THRESHOLD_FAILED"),
                    "cap_days",  new PropSchema("integer","Duration of cap in days")
                ), List.of("entity_id","reason","cap_days")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "expansion_cap__check_eligibility" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("expansion_type", args.get("expansion_type"));
                r.put("eligible", true);
                r.put("eligibility_score", 82.0);
                r.put("criteria_met", List.of("Audit score ≥80","Min 6 months operating","No active violations"));
                r.put("criteria_failed", List.of());
                r.put("recommendation","APPROVED_FOR_EXPANSION");
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "expansion_cap__apply_cap" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("reason", args.get("reason"));
                r.put("cap_days", args.get("cap_days"));
                r.put("cap_id","CAP-" + UUID.randomUUID().toString().substring(0,8).toUpperCase());
                r.put("applied_at", Instant.now().toString());
                r.put("expires_at", Instant.now().plusSeconds(
                    ((Number) args.getOrDefault("cap_days",30)).longValue() * 86400).toString());
                r.put("status","CAP_ACTIVE");
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 23 — NETWORK_SHUTDOWN_PROTOCOL_AGENT
// ══════════════════════════════════════════════════════════════════════
class NetworkShutdownProtocolAgent {
    static final String NAME = "NETWORK_SHUTDOWN_PROTOCOL_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("shutdown_protocol__initiate",
                "Initiate a controlled shutdown protocol for a network node or organizer. Handles data migration, stakeholder notifications, and asset recovery.",
                new InputSchema(Map.of(
                    "entity_id",      new PropSchema("string","Node/Organizer ID to shut down"),
                    "shutdown_type",  new PropSchema("string","VOLUNTARY, FORCED, REGULATORY",
                        List.of("VOLUNTARY","FORCED","REGULATORY")),
                    "reason",         new PropSchema("string","Reason for shutdown"),
                    "effective_date", new PropSchema("string","Planned effective date ISO-8601")
                ), List.of("entity_id","shutdown_type","reason"))),
            new McpTool("shutdown_protocol__get_checklist",
                "Get the shutdown checklist and completion status for an entity currently in shutdown process.",
                new InputSchema(Map.of(
                    "entity_id", new PropSchema("string","Entity ID in shutdown")
                ), List.of("entity_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "shutdown_protocol__initiate" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("shutdown_type", args.get("shutdown_type"));
                r.put("reason", args.get("reason"));
                r.put("shutdown_id","SHD-" + UUID.randomUUID().toString().substring(0,8).toUpperCase());
                r.put("initiated_at", Instant.now().toString());
                r.put("status","SHUTDOWN_INITIATED");
                r.put("checklist_items", 14);
                r.put("auto_actions_started", List.of(
                    "Student records migration queued",
                    "Stakeholder notification emails sent",
                    "Platform access suspended",
                    "Asset recovery workflow started"
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "shutdown_protocol__get_checklist" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("checklist_completion_pct", 64.3);
                r.put("items", List.of(
                    Map.of("item","Student records transferred","status","DONE"),
                    Map.of("item","Outstanding fees collected","status","IN_PROGRESS","pct_done",72),
                    Map.of("item","Trainer contracts terminated","status","PENDING"),
                    Map.of("item","Legal handover documents signed","status","PENDING")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 24 — RISK_REGISTER_MANAGEMENT_AGENT
// ══════════════════════════════════════════════════════════════════════
class RiskRegisterManagementAgent {
    static final String NAME = "RISK_REGISTER_MANAGEMENT_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("risk_register__get_register",
                "Get the current risk register for a network, organizer, or platform-wide. Returns all risks with likelihood and impact ratings.",
                new InputSchema(Map.of(
                    "entity_id", new PropSchema("string","Entity or PLATFORM_WIDE"),
                    "category",  new PropSchema("string","FINANCIAL, OPERATIONAL, COMPLIANCE, REPUTATIONAL, ALL",
                        List.of("FINANCIAL","OPERATIONAL","COMPLIANCE","REPUTATIONAL","ALL"))
                ), List.of("entity_id"))),
            new McpTool("risk_register__add_risk",
                "Add a new risk entry to the register with assessment, mitigation plan, and owner.",
                new InputSchema(Map.of(
                    "entity_id",    new PropSchema("string","Entity ID"),
                    "risk_name",    new PropSchema("string","Risk name or description"),
                    "category",     new PropSchema("string","Risk category"),
                    "likelihood",   new PropSchema("string","LOW, MEDIUM, HIGH",List.of("LOW","MEDIUM","HIGH")),
                    "impact",       new PropSchema("string","LOW, MEDIUM, HIGH, CRITICAL",
                        List.of("LOW","MEDIUM","HIGH","CRITICAL")),
                    "mitigation",   new PropSchema("string","Mitigation plan"),
                    "owner",        new PropSchema("string","Risk owner name or ID")
                ), List.of("entity_id","risk_name","likelihood","impact")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "risk_register__get_register" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("total_risks", 12);
                r.put("critical_risks", 1);
                r.put("high_risks", 3);
                r.put("risks", List.of(
                    Map.of("id","RSK-001","name","Key organizer dependency","category","OPERATIONAL",
                           "likelihood","HIGH","impact","CRITICAL","owner","ops-lead","status","OPEN"),
                    Map.of("id","RSK-002","name","DPDP non-compliance fine","category","COMPLIANCE",
                           "likelihood","MEDIUM","impact","HIGH","owner","legal","status","MITIGATING"),
                    Map.of("id","RSK-003","name","Revenue concentration >30% single node","category","FINANCIAL",
                           "likelihood","HIGH","impact","HIGH","owner","finance","status","OPEN")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "risk_register__add_risk" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("risk_id","RSK-" + UUID.randomUUID().toString().substring(0,6).toUpperCase());
                r.put("entity_id", args.get("entity_id"));
                r.put("risk_name", args.get("risk_name"));
                r.put("likelihood", args.get("likelihood"));
                r.put("impact", args.get("impact"));
                r.put("owner", args.getOrDefault("owner","UNASSIGNED"));
                r.put("created_at", Instant.now().toString());
                r.put("status","OPEN");
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error","Unknown tool: " + tool));
        };
    }
}

/** Public dispatcher for Agents 17–24 */
public class AgentsGroup3 {
    public static List<McpTool> allTools() {
        List<McpTool> all = new ArrayList<>();
        all.addAll(CrisisManagementAgent.tools());
        all.addAll(EmergencyResponseAgent.tools());
        all.addAll(EmergencyResponsePolicyAgent.tools());
        all.addAll(FailureCaseAnalysisAgent.tools());
        all.addAll(FranchiseConcentrationRiskAgent.tools());
        all.addAll(NetworkExpansionCapAgent.tools());
        all.addAll(NetworkShutdownProtocolAgent.tools());
        all.addAll(RiskRegisterManagementAgent.tools());
        return all;
    }
    public static AgentResult dispatch(String tool, Map<String, Object> args) {
        if (tool.startsWith("crisis__"))           return CrisisManagementAgent.execute(tool,args);
        if (tool.startsWith("emergency__"))        return EmergencyResponseAgent.execute(tool,args);
        if (tool.startsWith("erp__"))              return EmergencyResponsePolicyAgent.execute(tool,args);
        if (tool.startsWith("failure_analysis__")) return FailureCaseAnalysisAgent.execute(tool,args);
        if (tool.startsWith("franchise_risk__"))   return FranchiseConcentrationRiskAgent.execute(tool,args);
        if (tool.startsWith("expansion_cap__"))    return NetworkExpansionCapAgent.execute(tool,args);
        if (tool.startsWith("shutdown_protocol__"))return NetworkShutdownProtocolAgent.execute(tool,args);
        if (tool.startsWith("risk_register__"))    return RiskRegisterManagementAgent.execute(tool,args);
        return new AgentResult("GROUP3","ERROR", Map.of("error","No agent for: " + tool));
    }
}
