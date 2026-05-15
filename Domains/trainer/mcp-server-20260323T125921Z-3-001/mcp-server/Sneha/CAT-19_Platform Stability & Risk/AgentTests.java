package com.ecoskiller.mcp19;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for all 6 CAT-19 agents.
 * Run: mvn test
 */
class AgentTests {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // ------------------------------------------------------------------ //
    //  1. AGENT_HEALTH_WATCHDOG_AGENT
    // ------------------------------------------------------------------ //
    @Test
    void testAgentHealthWatchdog_allAgents() throws Exception {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("agent_id", "*");
        args.put("check_type", "all");
        args.put("latency_threshold_ms", 2000);
        args.put("auto_escalate", false);

        JsonNode result = new AgentHealthWatchdogAgent().execute(args);

        assertEquals("AGENT_HEALTH_WATCHDOG_AGENT", result.path("agent").asText());
        assertEquals("HEALTHY", result.path("health_report").path("status").asText());
        assertFalse(result.path("auto_escalate_triggered").asBoolean());
    }

    @Test
    void testAgentHealthWatchdog_specificAgent() throws Exception {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("agent_id", "ROYALTY_CALCULATION_AGENT");

        JsonNode result = new AgentHealthWatchdogAgent().execute(args);

        assertEquals("ROYALTY_CALCULATION_AGENT", result.path("target_agent").asText());
        assertNotNull(result.path("health_report"));
    }

    // ------------------------------------------------------------------ //
    //  2. EMERGENCY_PLATFORM_LOCKDOWN_AGENT
    // ------------------------------------------------------------------ //
    @Test
    void testEmergencyLockdown_dryRunFull() throws Exception {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("lockdown_type",  "FULL");
        args.put("reason",         "Security drill");
        args.put("initiated_by",   "ADMIN-001");
        args.put("dry_run",        true);

        JsonNode result = new EmergencyPlatformLockdownAgent().execute(args);

        assertEquals("SIMULATED", result.path("status").asText());
        assertTrue(result.path("dry_run").asBoolean());
        assertTrue(result.path("lockdown_id").asText().startsWith("LKD-"));
    }

    @Test
    void testEmergencyLockdown_tenantScope() throws Exception {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("lockdown_type",  "TENANT");
        args.put("reason",         "Suspected breach");
        args.put("initiated_by",   "INSIDER_THREAT_MONITOR_AGENT");
        args.put("tenant_id",      "TENANT-XYZ");
        args.put("dry_run",        false);

        JsonNode result = new EmergencyPlatformLockdownAgent().execute(args);

        assertEquals("LOCKDOWN_ENGAGED", result.path("status").asText());
        assertEquals("TENANT-XYZ", result.path("tenant_id").asText());
        assertTrue(result.path("actions_taken").isArray());
    }

    // ------------------------------------------------------------------ //
    //  3. INSIDER_THREAT_MONITOR_AGENT
    // ------------------------------------------------------------------ //
    @Test
    void testInsiderThreatMonitor_sweepAll() throws Exception {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("user_id",       "*");
        args.put("window_hours",   48);
        args.put("risk_threshold", 65);

        JsonNode result = new InsiderThreatMonitorAgent().execute(args);

        assertEquals("INSIDER_THREAT_MONITOR_AGENT", result.path("agent").asText());
        assertNotNull(result.path("threat_analysis").path("risk_score"));
        assertTrue(result.path("threat_analysis").path("risk_score").asDouble() >= 0);
    }

    @Test
    void testInsiderThreatMonitor_specificUser() throws Exception {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("user_id", "ADMIN-007");

        JsonNode result = new InsiderThreatMonitorAgent().execute(args);

        assertEquals("ADMIN-007", result.path("evaluated_user").asText());
        assertFalse(result.path("threat_analysis").path("alert_triggered").asBoolean());
    }

    // ------------------------------------------------------------------ //
    //  4. INSTITUTE_TIMETABLE_OPTIMIZATION_AGENT
    // ------------------------------------------------------------------ //
    @Test
    void testTimetableOptimization_conflictFree() throws Exception {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("institute_id",           "INST-PUNE-01");
        args.put("term",                   "2025-Q3");
        args.put("optimization_goal",      "CONFLICT_FREE");
        args.put("respect_faculty_preferences", true);

        JsonNode result = new InstituteTimetableOptimizationAgent().execute(args);

        assertEquals("OPTIMIZATION_COMPLETE", result.path("status").asText());
        assertEquals(3, result.path("timetable_analysis").path("conflicts_resolved").asInt());
        assertTrue(result.path("timetable_analysis")
                         .path("optimization_score_after").asDouble() > 90.0);
    }

    @Test
    void testTimetableOptimization_returnsScheduleRef() throws Exception {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("institute_id", "INST-MUM-05");
        args.put("term",         "2025-Q4");

        JsonNode result = new InstituteTimetableOptimizationAgent().execute(args);

        assertTrue(result.path("optimized_schedule_ref").asText().contains("INST-MUM-05"));
    }

    // ------------------------------------------------------------------ //
    //  5. INTEGRATION_HEALTH_MONITOR_AGENT
    // ------------------------------------------------------------------ //
    @Test
    void testIntegrationHealthMonitor_allIntegrations() throws Exception {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("integration_id", "*");
        args.put("window_minutes",  60);
        args.put("sla_uptime_percent", 99.5);

        JsonNode result = new IntegrationHealthMonitorAgent().execute(args);

        assertEquals("INTEGRATION_HEALTH_MONITOR_AGENT", result.path("agent").asText());
        assertTrue(result.path("integrations").isArray());
        assertEquals(5, result.path("integrations").size());
        assertEquals("PARTIAL_DEGRADATION", result.path("summary").path("overall_status").asText());
    }

    @Test
    void testIntegrationHealthMonitor_alertsPresent() throws Exception {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("integration_id",     "*");
        args.put("sla_uptime_percent",  99.5);

        JsonNode result = new IntegrationHealthMonitorAgent().execute(args);

        assertTrue(result.path("alerts").isArray());
        assertTrue(result.path("alerts").size() > 0);
        assertEquals("LMS-MOODLE", result.path("alerts").get(0).path("integration").asText());
    }

    // ------------------------------------------------------------------ //
    //  6. MODEL_EXPLAINABILITY_DIFF_AGENT
    // ------------------------------------------------------------------ //
    @Test
    void testModelExplainabilityDiff_basic() throws Exception {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("model_id",          "skill_confidence_model");
        args.put("baseline_version",  "v2.1.0");
        args.put("candidate_version", "v2.2.0");
        args.put("drift_threshold_psi", 0.2);

        JsonNode result = new ModelExplainabilityDiffAgent().execute(args);

        assertEquals("MODEL_EXPLAINABILITY_DIFF_AGENT", result.path("agent").asText());
        assertEquals("APPROVE_WITH_MONITORING", result.path("overall_verdict").asText());
        assertTrue(result.path("feature_importance_diff").isArray());
        assertFalse(result.path("prediction_drift").path("drift_flag").asBoolean());
    }

    @Test
    void testModelExplainabilityDiff_reportIdContainsModelId() throws Exception {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("model_id",          "attrition_risk_model");
        args.put("baseline_version",  "v1.0.0");
        args.put("candidate_version", "v1.1.0");

        JsonNode result = new ModelExplainabilityDiffAgent().execute(args);

        assertTrue(result.path("report_id").asText().contains("attrition_risk_model"));
        assertTrue(result.path("fairness_diff").isArray());
        assertTrue(result.path("governance_flags").isArray());
    }
}
