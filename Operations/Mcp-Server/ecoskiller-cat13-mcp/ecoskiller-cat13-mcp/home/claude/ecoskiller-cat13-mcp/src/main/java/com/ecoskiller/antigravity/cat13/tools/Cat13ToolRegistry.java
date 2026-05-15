package com.ecoskiller.antigravity.cat13.tools;

import com.ecoskiller.antigravity.cat13.agents.*;
import com.ecoskiller.antigravity.cat13.model.McpModels.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CAT-13 Tool Registry
 * ═══════════════════════════════════════════════════════════════
 * Aggregates tools from all 18 agents under CAT-13:
 * Enterprise Optimization + Trust Infrastructure — ANTIGRAVITY
 * ═══════════════════════════════════════════════════════════════
 *
 *  Agent                                              | Tools | Prefix
 * ─────────────────────────────────────────────────────────────────────
 *  WORK_RELIABILITY_MODEL_87                          |  4    | work_reliability__
 *  SYNC_CONFLICT_RESOLVER                             |  2    | sync_conflict__
 *  STRUCTURED_SKILL_EXTRACTION_MODEL                  |  2    | skill_extraction__
 *  SECTION_83_CORPORATE_BENCHMARK_MODEL               |  2    | section83__
 *  SECTION_82_HIRING_ROI_MODEL                        |  2    | section82__
 *  SECTION_80_PRODUCTIVITY_INDEX_MODEL                |  2    | section80__
 *  REPUTATION_SCORE_ENGINE                            |  3    | reputation__
 *  MIGRATION_VALIDATION_ENGINE                        |  2    | migration__
 *  FRAUD_PATTERN_DETECTION_MODEL                      |  2    | fraud_pattern__
 *  FAKE_PROFILE_DETECTION_MODEL                       |  2    | fake_profile__
 *  EXCEL_PATTERN_DETECTION_MODEL                      |  1    | excel_pattern__
 *  DATA_NORMALIZATION_ENGINE                          |  2    | data_norm__
 *  CERTIFICATE_AUTHENTICITY_CLASSIFIER                |  2    | cert_auth__
 *  AUTOMATED_SHORTLISTING_ENGINE                      |  2    | shortlisting__
 *  ATTRITION_RISK_MODEL                               |  2    | attrition__
 *  ANTIGRAVITY_Performance_Metric_Mapper              |  2    | perf_metric__
 *  ANTIGRAVITY_90_Data_Cleaning_Classifier            |  2    | data_clean__
 *  ANTIGRAVITY_89_AI_Field_Mapping_Model              |  2    | ai_field_mapping__
 * ─────────────────────────────────────────────────────────────────────
 *  TOTAL TOOLS: 38
 */
public class Cat13ToolRegistry {

    /** Returns all 38 tools across all 18 CAT-13 agents */
    public static List<McpTool> getAllTools() {
        List<McpTool> all = new ArrayList<>();
        all.addAll(WorkReliabilityModel87Agent.tools());  // Agent 1
        all.addAll(AgentsGroup1.allTools());               // Agents 2–6
        all.addAll(AgentsGroup2.allTools());               // Agents 7–12
        all.addAll(AgentsGroup3.allTools());               // Agents 13–18
        return all;
    }

    /** Route a tool call to the correct agent */
    public static AgentResult dispatch(String toolName, Map<String, Object> arguments) {
        // Agent 1
        if (toolName.startsWith("work_reliability__"))
            return WorkReliabilityModel87Agent.execute(toolName, arguments);

        // Agents 2–6
        if (toolName.startsWith("sync_conflict__")
         || toolName.startsWith("skill_extraction__")
         || toolName.startsWith("section83__")
         || toolName.startsWith("section82__")
         || toolName.startsWith("section80__"))
            return AgentsGroup1.dispatch(toolName, arguments);

        // Agents 7–12
        if (toolName.startsWith("reputation__")
         || toolName.startsWith("migration__")
         || toolName.startsWith("fraud_pattern__")
         || toolName.startsWith("fake_profile__")
         || toolName.startsWith("excel_pattern__")
         || toolName.startsWith("data_norm__"))
            return AgentsGroup2.dispatch(toolName, arguments);

        // Agents 13–18
        if (toolName.startsWith("cert_auth__")
         || toolName.startsWith("shortlisting__")
         || toolName.startsWith("attrition__")
         || toolName.startsWith("perf_metric__")
         || toolName.startsWith("data_clean__")
         || toolName.startsWith("ai_field_mapping__"))
            return AgentsGroup3.dispatch(toolName, arguments);

        return new AgentResult("CAT13_REGISTRY", "ERROR",
            Map.of("error", "No agent registered for tool: " + toolName,
                   "hint",  "Check tool prefix against registry table in Cat13ToolRegistry.java"));
    }
}
