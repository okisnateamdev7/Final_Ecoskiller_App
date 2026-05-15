package com.ecoskiller.analytics.server;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for mcp-25-analytics-devops.
 * Covers all 14 agents, protocol handling, routing, and tool logic.
 */
@DisplayName("mcp-25-analytics-devops — Unit Tests (14 Agents, 55+ cases)")
class AnalyticsMcpServerTest {

    private AnalyticsMcpServer server;

    @BeforeEach void setUp() { server = new AnalyticsMcpServer(); }

    // ═══════════════════════════════════════════════════════════════════════
    // Protocol
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("initialize returns mcp-25-analytics-devops and 14 agents")
    void testInitialize() {
        String r = method("initialize", "{}");
        assertTrue(r.contains("mcp-25-analytics-devops"));
        assertTrue(r.contains("2024-11-05"));
        assertTrue(r.contains("\"agents\":14"));
    }

    @Test @DisplayName("14 agents registered")
    void testAgentCount() { assertEquals(14, server.registry.count()); }

    @Test @DisplayName("tools/list includes tools from all 14 agents")
    void testToolsList() {
        String r = method("tools/list", "{}");
        // Analytics & ERP
        assertTrue(r.contains("analytics_normalize_clickhouse_metrics"));  // 1
        assertTrue(r.contains("analytics_pull_erp_report"));               // 2
        assertTrue(r.contains("analytics_emit_feature_vector"));           // 3
        assertTrue(r.contains("analytics_analyse_attendance_behaviour"));  // 4
        assertTrue(r.contains("analytics_get_enrollment_funnel"));         // 5
        // DevOps & Environment
        assertTrue(r.contains("devops_validate_phone_config"));            // 6
        assertTrue(r.contains("devops_validate_backup"));                  // 7
        assertTrue(r.contains("devops_run_e2e_trace"));                    // 8
        assertTrue(r.contains("devops_send_webhook"));                     // 9
        assertTrue(r.contains("devops_run_health_check"));                 // 10
        assertTrue(r.contains("devops_get_authoritative_time"));           // 11
        assertTrue(r.contains("devops_scan_node_time_drift"));             // 12
        assertTrue(r.contains("devops_register_model"));                   // 13
        assertTrue(r.contains("devops_correlate_trace"));                  // 14
    }

    @Test @DisplayName("ping returns empty result")
    void testPing()           { assertFalse(method("ping","{}").contains("\"error\"")); }

    @Test @DisplayName("unknown method → -32601")
    void testUnknownMethod()  { assertTrue(method("nonexistent","{}").contains("-32601")); }

    @Test @DisplayName("malformed JSON → -32700")
    void testMalformedJson()  { assertTrue(server.handleMessage("{not:json}").contains("-32700")); }

    @Test @DisplayName("unknown tool → -32602")
    void testUnknownTool()    { assertTrue(tool("analytics_does_not_exist","{}").contains("-32602")); }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 1 — CLICKHOUSE_METRIC_NORMALIZATION_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 1: normalize_clickhouse_metrics returns quality 99.1")
    void testNormalizeClickhouse() {
        String r = tool("analytics_normalize_clickhouse_metrics",
            "{\"table_name\":\"events\",\"date_range\":\"2026-03\",\"target_schema_version\":\"v4\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"quality_after\":99.1"));
        assertTrue(r.contains("COMPLETE"));
        assertTrue(r.contains("2841804") || r.contains("2_841_804"));
    }

    @Test @DisplayName("Agent 1: validate_clickhouse_schema returns 42 columns matched")
    void testValidateClickhouseSchema() {
        String r = tool("analytics_validate_clickhouse_schema",
            "{\"table_name\":\"events\",\"expected_schema_id\":\"ES-v4\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"columns_expected\":42"));
        assertTrue(r.contains("\"valid\":true"));
    }

    @Test @DisplayName("Agent 1: detect_metric_drift shows STABLE")
    void testMetricDrift() {
        String r = tool("analytics_detect_metric_drift",
            "{\"table_name\":\"events\",\"metric\":\"session_count\",\"window_days\":\"30\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"status\":\"STABLE\""));
        assertTrue(r.contains("\"drift_detected\":false"));
    }

    @Test @DisplayName("Agent 1: backfill_missing_metrics returns 1842 rows filled")
    void testBackfill() {
        String r = tool("analytics_backfill_missing_metrics",
            "{\"table_name\":\"events\",\"metric\":\"score\",\"strategy\":\"carry_forward\",\"date_from\":\"2026-03-01\",\"date_to\":\"2026-03-15\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"rows_backfilled\":1842") || r.contains("1_842"));
        assertTrue(r.contains("COMPLETE"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 2 — ERP_GO_REPORT_INTEGRATION_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 2: pull_erp_report ingests 48240 rows")
    void testPullErpReport() {
        String r = tool("analytics_pull_erp_report",
            "{\"report_id\":\"RPT-REV\",\"report_type\":\"revenue_summary\",\"as_of_date\":\"2026-03-15\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"rows_ingested\":48240") || r.contains("48_240"));
        assertTrue(r.contains("SUCCESS"));
    }

    @Test @DisplayName("Agent 2: sync_erp_delta reports 614 added + 228 updated")
    void testErpDeltaSync() {
        String r = tool("analytics_sync_erp_delta",
            "{\"report_id\":\"RPT-REV\",\"last_sync_ts\":\"2026-03-14T06:00:00Z\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"rows_added\":614"));
        assertTrue(r.contains("\"rows_updated\":228"));
    }

    @Test @DisplayName("Agent 2: get_erp_sync_status shows 2 healthy 1 stale")
    void testErpSyncStatus() {
        String r = tool("analytics_get_erp_sync_status", "{\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"healthy\":2"));
        assertTrue(r.contains("\"stale\":1"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 3 — PHONE_FEATURE_VECTOR_EMISSION_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 3: emit_feature_vector returns 8 features at fv-phone-v2")
    void testEmitFeatureVector() {
        String r = tool("analytics_emit_feature_vector",
            "{\"device_id\":\"DEV-001\",\"session_id\":\"SES-001\",\"event_batch\":\"[]\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"dim\":8"));
        assertTrue(r.contains("fv-phone-v2"));
        assertTrue(r.contains("screen_time_min"));
    }

    @Test @DisplayName("Agent 3: batch_emit_vectors processes 4820 devices")
    void testBatchEmitVectors() {
        String r = tool("analytics_batch_emit_vectors",
            "{\"cohort_id\":\"C1\",\"session_date\":\"2026-03-15\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"devices_emitted\":4820") || r.contains("4_820"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 4 — ATTENDANCE_BEHAVIOR_ANALYTICS_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 4: analyse_attendance_behaviour shows ENGAGED")
    void testAttendanceBehaviour() {
        String r = tool("analytics_analyse_attendance_behaviour",
            "{\"entity_id\":\"STU-001\",\"entity_type\":\"student\",\"period\":\"2026-Q1\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"behaviour_signal\":\"ENGAGED\""));
        assertTrue(r.contains("\"risk_flag\":false"));
        assertTrue(r.contains("87.4"));
    }

    @Test @DisplayName("Agent 4: flag_chronic_absenteeism finds 14 students")
    void testChronicAbsenteeism() {
        String r = tool("analytics_flag_chronic_absenteeism",
            "{\"scope\":\"school\",\"scope_id\":\"SCH-001\",\"window_days\":\"30\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"flagged_count\":14"));
    }

    @Test @DisplayName("Agent 4: attendance-performance correlation r=0.72 significant")
    void testAttendanceCorrelation() {
        String r = tool("analytics_correlate_attendance_performance",
            "{\"school_id\":\"SCH-001\",\"subject\":\"Math\",\"period\":\"2026-Q1\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"pearson_r\":0.72"));
        assertTrue(r.contains("\"significant\":true"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 5 — ENROLLMENT_ANALYTICS_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 5: enrollment funnel shows 32.8% conversion")
    void testEnrollmentFunnel() {
        String r = tool("analytics_get_enrollment_funnel",
            "{\"school_id\":\"SCH-001\",\"period\":\"2026-Q1\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"awareness\":1200") || r.contains("1_200"));
        assertTrue(r.contains("32.8"));
    }

    @Test @DisplayName("Agent 5: enrollment forecast 412→487 in 6 months")
    void testEnrollmentForecast() {
        String r = tool("analytics_forecast_enrollment",
            "{\"school_id\":\"SCH-001\",\"forecast_months\":\"6\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"projected_enrolled\":487"));
        assertTrue(r.contains("18.2"));
    }

    @Test @DisplayName("Agent 5: compare enrollment shows SCH-003 leads")
    void testEnrollmentComparison() {
        String r = tool("analytics_compare_enrollment_growth",
            "{\"scope_ids_csv\":\"SCH-001,SCH-002,SCH-003\",\"period\":\"2026-Q1\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("SCH-003"));
        assertTrue(r.contains("22.7"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 6 — MULTI_ENVIRONMENT_PHONE_CONFIG_VALIDATOR_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 6: validate_phone_config passes 84 keys, 2 warnings")
    void testValidatePhoneConfig() {
        String r = tool("devops_validate_phone_config",
            "{\"device_id\":\"DEV-001\",\"environment\":\"production\",\"config_version\":\"v2.4.1\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"keys_checked\":84"));
        assertTrue(r.contains("\"valid\":true"));
        assertTrue(r.contains("\"warnings\":2"));
    }

    @Test @DisplayName("Agent 6: compare_env_configs finds 5 different keys (LOW risk)")
    void testCompareEnvConfigs() {
        String r = tool("devops_compare_env_configs",
            "{\"app_id\":\"APP-001\",\"base_env\":\"staging\",\"target_env\":\"production\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"keys_different\":5"));
        assertTrue(r.contains("\"risk_level\":\"LOW\""));
    }

    @Test @DisplayName("Agent 6: detect_config_drift finds 3 drifted keys (MEDIUM)")
    void testConfigDrift() {
        String r = tool("devops_detect_config_drift",
            "{\"app_id\":\"APP-001\",\"baseline_env\":\"production\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"drift_detected\":true"));
        assertTrue(r.contains("\"drifted_keys\":3"));
        assertTrue(r.contains("MEDIUM"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 7 — PHONE_BACKUP_RESTORE_VALIDATION_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 7: validate_backup returns VALID with 4820 files")
    void testValidateBackup() {
        String r = tool("devops_validate_backup",
            "{\"device_id\":\"DEV-001\",\"backup_id\":\"BKP-001\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"integrity\":\"VALID\""));
        assertTrue(r.contains("\"files_corrupt\":0"));
        assertTrue(r.contains("\"checksum_match\":true"));
    }

    @Test @DisplayName("Agent 7: restore_test queued with 8min estimate")
    void testRestoreTest() {
        String r = tool("devops_trigger_restore_test",
            "{\"device_id\":\"DEV-001\",\"backup_id\":\"BKP-001\",\"target_env\":\"sandbox\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"estimated_min\":8"));
        assertTrue(r.contains("QUEUED"));
    }

    @Test @DisplayName("Agent 7: snapshot comparison shows NORMAL_DELTA")
    void testSnapshotCompare() {
        String r = tool("devops_compare_backup_snapshots",
            "{\"backup_id_a\":\"BKP-001\",\"backup_id_b\":\"BKP-002\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("NORMAL_DELTA"));
        assertTrue(r.contains("\"data_loss\":false"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 8 — PHONE_END_TO_END_TRACE_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 8: run_e2e_trace completes SUCCESS with 14 spans")
    void testE2ETrace() {
        String r = tool("devops_run_e2e_trace",
            "{\"device_id\":\"DEV-001\",\"flow_name\":\"test_submission\",\"environment\":\"production\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"spans\":14"));
        assertTrue(r.contains("SUCCESS"));
        assertTrue(r.contains("\"errors\":0"));
    }

    @Test @DisplayName("Agent 8: analyse_trace_latency returns hotspot svc.test")
    void testTraceLatency() {
        String r = tool("devops_analyse_trace_latency",
            "{\"trace_id\":\"trace-001\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("svc.test"));
        assertTrue(r.contains("p95_ms"));
    }

    @Test @DisplayName("Agent 8: compare_trace_runs detects REGRESSION_MINOR")
    void testTraceComparison() {
        String r = tool("devops_compare_trace_runs",
            "{\"trace_id_a\":\"trace-001\",\"trace_id_b\":\"trace-002\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("REGRESSION_MINOR"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 9 — PHONE_EXTERNAL_WEBHOOK_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 9: send_webhook delivers with 200 response")
    void testSendWebhook() {
        String r = tool("devops_send_webhook",
            "{\"event_type\":\"test_submitted\",\"payload_json\":\"{}\",\"endpoint_id\":\"EP-001\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"response_code\":200"));
        assertTrue(r.contains("DELIVERED"));
    }

    @Test @DisplayName("Agent 9: list_webhook_endpoints shows 2 healthy 1 degraded")
    void testListWebhookEndpoints() {
        String r = tool("devops_list_webhook_endpoints", "{\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"healthy\":2"));
        assertTrue(r.contains("\"degraded\":1"));
    }

    @Test @DisplayName("Agent 9: retry_failed_webhooks recovers 6 of 7")
    void testRetryWebhooks() {
        String r = tool("devops_retry_failed_webhooks",
            "{\"hours\":\"24\",\"endpoint_id\":\"EP-003\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"retried\":7"));
        assertTrue(r.contains("\"succeeded\":6"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 10 — PHONE_INFRA_HEALTH_CHECK_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 10: health_check finds 6 healthy 1 degraded")
    void testInfraHealthCheck() {
        String r = tool("devops_run_health_check",
            "{\"environment\":\"production\",\"scope\":\"full\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"healthy\":6"));
        assertTrue(r.contains("\"degraded\":1"));
        assertTrue(r.contains("notification-svc"));
    }

    @Test @DisplayName("Agent 10: get_service_status returns 99.97% uptime")
    void testServiceStatus() {
        String r = tool("devops_get_service_status",
            "{\"service_name\":\"api-gateway\",\"environment\":\"production\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("99.97"));
        assertTrue(r.contains("HEALTHY"));
    }

    @Test @DisplayName("Agent 10: canary_check passes 12/12 smoke tests")
    void testCanaryCheck() {
        String r = tool("devops_trigger_canary_check",
            "{\"service_name\":\"test-service\",\"version\":\"v2.5.0\",\"environment\":\"production\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"passed\":12"));
        assertTrue(r.contains("PASS"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 11 — PHONE_MONITORING_CLOCK_AUTHORITY_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 11: get_authoritative_time returns stratum 2")
    void testAuthoritativeTime() {
        String r = tool("devops_get_authoritative_time", "{\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"stratum\":2"));
        assertTrue(r.contains("utc_ms"));
    }

    @Test @DisplayName("Agent 11: check_device_clock_skew returns skew and action")
    void testClockSkewCheck() {
        String r = tool("devops_check_device_clock_skew",
            "{\"device_id\":\"DEV-001\",\"device_ts\":\"1710000000000\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("skew_ms"));
        assertTrue(r.contains("action"));
    }

    @Test @DisplayName("Agent 11: get_clock_skew_report finds 3 devices over threshold")
    void testClockSkewReport() {
        String r = tool("devops_get_clock_skew_report",
            "{\"threshold_ms\":\"500\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"count\":3"));
        assertTrue(r.contains("DEV-042"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 12 — CROSS_NODE_TIME_DRIFT_MONITOR_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 12: scan_node_time_drift finds 1 drifting node")
    void testNodeDriftScan() {
        String r = tool("devops_scan_node_time_drift",
            "{\"cluster_id\":\"CLUSTER-01\",\"threshold_ms\":\"100\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"healthy\":4"));
        assertTrue(r.contains("\"drifting\":1"));
        assertTrue(r.contains("node-03"));
    }

    @Test @DisplayName("Agent 12: node drift history shows spike at 142ms")
    void testNodeDriftHistory() {
        String r = tool("devops_get_node_drift_history",
            "{\"node_id\":\"node-03\",\"hours\":\"6\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"max_drift_ms\":142.0"));
        assertTrue(r.contains("\"spike_detected\":true"));
    }

    @Test @DisplayName("Agent 12: cluster_sync_health shows WARN with 1 drifting")
    void testClusterSyncHealth() {
        String r = tool("devops_get_cluster_sync_health",
            "{\"cluster_id\":\"CLUSTER-01\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"cluster_health\":\"WARN\""));
        assertTrue(r.contains("\"nodes_drifting\":1"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 13 — MODEL_GOVERNANCE_REGISTRY_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 13: register_model enters PENDING_APPROVAL state")
    void testRegisterModel() {
        String r = tool("devops_register_model",
            "{\"model_name\":\"fraud-detector\",\"version\":\"v2.1\",\"owner\":\"ml-team\",\"use_case\":\"fraud\",\"risk_tier\":\"HIGH\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("PENDING_APPROVAL"));
        assertTrue(r.contains("registry_id"));
    }

    @Test @DisplayName("Agent 13: approve_model_deployment logs audit")
    void testApproveModel() {
        String r = tool("devops_approve_model_deployment",
            "{\"model_name\":\"fraud-detector\",\"version\":\"v2.1\",\"decision\":\"APPROVED\",\"approver_id\":\"USR-001\",\"notes\":\"OK\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"decision\":\"APPROVED\""));
        assertTrue(r.contains("\"audit_logged\":true"));
    }

    @Test @DisplayName("Agent 13: get_model_registry returns 5 models (4 approved)")
    void testModelRegistry() {
        String r = tool("devops_get_model_registry",
            "{\"filter_status\":\"ALL\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"total\":5"));
        assertTrue(r.contains("\"approved\":4"));
    }

    @Test @DisplayName("Agent 13: audit_model_access returns 3 access entries")
    void testModelAccessAudit() {
        String r = tool("devops_audit_model_access",
            "{\"model_name\":\"intelligence-scorer\",\"days\":\"7\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"total_accesses\":3"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Agent 14 — CROSS_SERVICE_TRACE_CORRELATION_AGENT
    // ═══════════════════════════════════════════════════════════════════════

    @Test @DisplayName("Agent 14: correlate_trace links 10 spans across 9 services")
    void testCorrelateTrace() {
        String r = tool("devops_correlate_trace",
            "{\"trace_id\":\"trace-001\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"total_spans\":10"));
        assertTrue(r.contains("\"services_involved\":9"));
        assertTrue(r.contains("\"errors\":0"));
    }

    @Test @DisplayName("Agent 14: find_root_cause points to test-service DB scan")
    void testFindRootCause() {
        String r = tool("devops_find_root_cause",
            "{\"trace_id\":\"trace-001\",\"symptom\":\"high_latency\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"root_cause_service\":\"test-service\""));
        assertTrue(r.contains("DB query without index"));
        assertTrue(r.contains("\"priority\":\"HIGH\""));
    }

    @Test @DisplayName("Agent 14: get_service_dependency_map shows 7 downstream")
    void testServiceDependencyMap() {
        String r = tool("devops_get_service_dependency_map",
            "{\"service_name\":\"api-gateway\",\"depth\":\"2\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"total_downstream\":7"));
        assertTrue(r.contains("api-gateway"));
    }

    @Test @DisplayName("Agent 14: error_propagation_path shows 4 hops from postgres")
    void testErrorPropagation() {
        String r = tool("devops_get_error_propagation_path",
            "{\"trace_id\":\"trace-001\",\"error_code\":\"CONNECTION_TIMEOUT\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"hops\":4"));
        assertTrue(r.contains("\"origin_service\":\"postgres-db\""));
    }

    @Test @DisplayName("Agent 14: cross_service_sla_report passes with 58.4% budget used")
    void testSlaReport() {
        String r = tool("devops_get_cross_service_sla_report",
            "{\"trace_id\":\"trace-001\",\"sla_budget_ms\":\"500\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"sla_breaches\":0"));
        assertTrue(r.contains("\"overall_sla\":\"PASS\""));
        assertTrue(r.contains("58.4"));
    }

    @Test @DisplayName("Agent 14: search_traces_by_attribute finds 3 traces")
    void testSearchTraces() {
        String r = tool("devops_search_traces_by_attribute",
            "{\"attribute_key\":\"user_id\",\"attribute_value\":\"USR-001\",\"hours\":\"24\",\"tenant_id\":\"T1\"}");
        assertTrue(r.contains("\"found\":3"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Helpers
    // ═══════════════════════════════════════════════════════════════════════

    private String method(String m, String params) {
        return server.handleMessage(
            "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"" + m + "\",\"params\":" + params + "}");
    }

    private String tool(String name, String args) {
        return server.handleMessage(
            "{\"jsonrpc\":\"2.0\",\"id\":99,\"method\":\"tools/call\"," +
            "\"params\":{\"name\":\"" + name + "\",\"arguments\":" + args + "}}");
    }
}
