package com.ecoskiller.antigravity.cat26.tools;

import com.ecoskiller.antigravity.cat26.agents.*;
import com.ecoskiller.antigravity.cat26.model.McpModels.*;

import java.util.*;

/**
 * CAT-26 Tool Registry
 * ═══════════════════════════════════════════════════════════════════════
 * 29 Agents | 58 Tools — Organizer & Network Management — ANTIGRAVITY
 * ═══════════════════════════════════════════════════════════════════════
 *
 *  #  | Agent                              | Prefix                   | Tools
 * ────────────────────────────────────────────────────────────────────────
 *  1  | IMPACT_MEASUREMENT                 | impact__                 | 2
 *  2  | REVENUE_TREND_ANALYTICS            | revenue_trend__          | 2
 *  3  | REVENUE_STREAM_DIVERSIFICATION     | rev_diversification__    | 2
 *  4  | NETWORK_HEALTH_MONITOR             | network_health__         | 2
 *  5  | DROPOUT_RISK_PREDICTION            | dropout_risk__           | 2
 *  6  | PHONE_MONITORING_CLOCK_AUTHORITY   | phone_clock__            | 2
 *  7  | POLICY_COMPLIANCE                  | policy_compliance__      | 2
 *  8  | DATA_PRIVACY_COMPLIANCE            | data_privacy__           | 2
 *  9  | SAFETY_COMPLIANCE_MONITOR          | safety_compliance__      | 2
 *  10 | STANDARD_AUDIT                     | std_audit__              | 2
 *  11 | CONTRACT_MONITORING                | contract_monitor__       | 2
 *  12 | LEGAL_DOCUMENT_MANAGEMENT          | legal_docs__             | 2
 *  13 | GOVERNMENT_SCHEME_FUNDING          | govt_scheme__            | 2
 *  14 | CSR_FUNDS_ALLOCATION               | csr_funds__              | 2
 *  15 | FRAUD_DETECTION                    | fraud_detect__           | 2
 *  16 | FINANCIAL_ANOMALY_DETECTION        | fin_anomaly__            | 2
 *  17 | CRISIS_MANAGEMENT                  | crisis__                 | 2
 *  18 | EMERGENCY_RESPONSE                 | emergency__              | 2
 *  19 | EMERGENCY_RESPONSE_POLICY          | erp__                    | 2
 *  20 | FAILURE_CASE_ANALYSIS              | failure_analysis__       | 2
 *  21 | FRANCHISE_CONCENTRATION_RISK       | franchise_risk__         | 2
 *  22 | NETWORK_EXPANSION_CAP              | expansion_cap__          | 2
 *  23 | NETWORK_SHUTDOWN_PROTOCOL          | shutdown_protocol__      | 2
 *  24 | RISK_REGISTER_MANAGEMENT           | risk_register__          | 2
 *  25 | ROLE_OVERLOAD_MONITOR              | role_overload__          | 2
 *  26 | MEDIA_RELATIONS                    | media_relations__        | 2
 *  27 | PUBLIC_REPUTATION_MONITOR          | pub_reputation__         | 2
 *  28 | PRICING_RECALIBRATION              | pricing__                | 2
 *  29 | SUCCESSION_AT_MASTER_LEVEL         | succession__             | 2
 * ────────────────────────────────────────────────────────────────────────
 *  TOTAL: 58 tools
 */
public class Cat26ToolRegistry {

    public static List<McpTool> getAllTools() {
        List<McpTool> all = new ArrayList<>();
        all.addAll(AgentsGroup1.allTools());  // Agents 1–8
        all.addAll(AgentsGroup2.allTools());  // Agents 9–16
        all.addAll(AgentsGroup3.allTools());  // Agents 17–24
        all.addAll(AgentsGroup4.allTools());  // Agents 25–29
        return all;
    }

    public static AgentResult dispatch(String toolName, Map<String, Object> arguments) {
        // Group 1: Agents 1–8
        if (toolName.startsWith("impact__")
         || toolName.startsWith("revenue_trend__")
         || toolName.startsWith("rev_diversification__")
         || toolName.startsWith("network_health__")
         || toolName.startsWith("dropout_risk__")
         || toolName.startsWith("phone_clock__")
         || toolName.startsWith("policy_compliance__")
         || toolName.startsWith("data_privacy__"))
            return AgentsGroup1.dispatch(toolName, arguments);

        // Group 2: Agents 9–16
        if (toolName.startsWith("safety_compliance__")
         || toolName.startsWith("std_audit__")
         || toolName.startsWith("contract_monitor__")
         || toolName.startsWith("legal_docs__")
         || toolName.startsWith("govt_scheme__")
         || toolName.startsWith("csr_funds__")
         || toolName.startsWith("fraud_detect__")
         || toolName.startsWith("fin_anomaly__"))
            return AgentsGroup2.dispatch(toolName, arguments);

        // Group 3: Agents 17–24
        if (toolName.startsWith("crisis__")
         || toolName.startsWith("emergency__")
         || toolName.startsWith("erp__")
         || toolName.startsWith("failure_analysis__")
         || toolName.startsWith("franchise_risk__")
         || toolName.startsWith("expansion_cap__")
         || toolName.startsWith("shutdown_protocol__")
         || toolName.startsWith("risk_register__"))
            return AgentsGroup3.dispatch(toolName, arguments);

        // Group 4: Agents 25–29
        if (toolName.startsWith("role_overload__")
         || toolName.startsWith("media_relations__")
         || toolName.startsWith("pub_reputation__")
         || toolName.startsWith("pricing__")
         || toolName.startsWith("succession__"))
            return AgentsGroup4.dispatch(toolName, arguments);

        return new AgentResult("CAT26_REGISTRY", "ERROR",
            Map.of("error", "No agent registered for tool: " + toolName));
    }
}
