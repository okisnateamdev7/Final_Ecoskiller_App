package com.ecoskiller.mcp;
import com.ecoskiller.mcp.protocol.JsonRpcHandler;

/**
 * Tests all 13 CAT-25 agents (19 test cases total).
 * Usage: java -cp out com.ecoskiller.mcp.TestAgents [--verbose]
 */
public class TestAgents {
    private static final JsonRpcHandler H = new JsonRpcHandler();
    private static boolean verbose = false;
    private static int passed = 0, failed = 0;

    public static void main(String[] argv) {
        for (String a : argv) if ("--verbose".equals(a)) verbose = true;
        System.out.println("=== Ecoskiller mcp-25 — CAT-25 Agent Tests ===\n");

        // ── Protocol ──────────────────────────────────────────────────────────
        run("initialize", rpc("initialize"));
        run("ping",       rpc("ping"));
        run("tools/list", rpcList());

        // ── Analytics & ERP ───────────────────────────────────────────────────
        run("clickhouse_metric_normalization (ms->s)",
            call("clickhouse_metric_normalization",
                "metric_name","stt_latency","raw_value","3500","unit","ms","source_table","phone_events"));

        run("clickhouse_metric_normalization (bytes->KB)",
            call("clickhouse_metric_normalization",
                "metric_name","audio_size","raw_value","204800","unit","bytes","source_table","recordings"));

        run("erp_go_report_integration",
            call("erp_go_report_integration",
                "tenant_id","t-001","report_type","monthly_scores","period","2025-01","record_count","500"));

        run("phone_feature_vector_emission",
            call("phone_feature_vector_emission",
                "session_id","sess-001","model_target","scoring_v4","features_csv","fluency,pace,vocabulary,grammar,confidence"));

        run("attendance_behavior_analytics (at-risk)",
            call("attendance_behavior_analytics",
                "learner_id","l-001","sessions_total","20","sessions_attended","13","late_joins","4"));

        run("attendance_behavior_analytics (excellent)",
            call("attendance_behavior_analytics",
                "learner_id","l-002","sessions_total","20","sessions_attended","19","late_joins","1"));

        run("enrollment_analytics",
            call("enrollment_analytics",
                "institution_id","inst-001","cohort","2025-Q1","enrolled","200","active","120","completed","60"));

        // ── DevOps & Environment ──────────────────────────────────────────────
        run("multi_environment_phone_config_validator (consistent)",
            call("multi_environment_phone_config_validator",
                "config_key","sip.codec","dev_value","OPUS","staging_value","OPUS","prod_value","OPUS"));

        run("multi_environment_phone_config_validator (prod drift)",
            call("multi_environment_phone_config_validator",
                "config_key","sip.codec","dev_value","OPUS","staging_value","OPUS","prod_value","G711"));

        run("phone_backup_restore_validation (valid)",
            call("phone_backup_restore_validation",
                "backup_id","bkp-20250101","backup_size_mb","2048","checksum","sha256abcdef","age_hours","6"));

        run("phone_backup_restore_validation (stale)",
            call("phone_backup_restore_validation",
                "backup_id","bkp-old","backup_size_mb","512","checksum","sha256xyz","age_hours","48"));

        run("phone_end_to_end_trace",
            call("phone_end_to_end_trace",
                "trace_id","trace-abc123","session_id","sess-002","span_count","12","total_latency_ms","650"));

        run("phone_external_webhook (delivered)",
            call("phone_external_webhook",
                "webhook_url","https://partner.example.com/hook","event_type","session.completed","payload_size_bytes","1024","attempt","1"));

        run("phone_external_webhook (dead letter)",
            call("phone_external_webhook",
                "webhook_url","https://partner.example.com/hook","event_type","session.completed","payload_size_bytes","1024","attempt","4"));

        run("phone_infra_health_check (healthy)",
            call("phone_infra_health_check",
                "service_name","phone-gateway","component","postgres","response_time_ms","45"));

        run("phone_infra_health_check (critical)",
            call("phone_infra_health_check",
                "service_name","phone-gateway","component","redis","response_time_ms","0"));

        run("phone_monitoring_clock_authority (synced)",
            call("phone_monitoring_clock_authority",
                "node_id","node-1","local_epoch_ms","1700000000000","ntp_epoch_ms","1700000000020"));

        run("phone_monitoring_clock_authority (major drift)",
            call("phone_monitoring_clock_authority",
                "node_id","node-3","local_epoch_ms","1700000000000","ntp_epoch_ms","1700000002000"));

        run("cross_node_time_drift_monitor (ok)",
            call("cross_node_time_drift_monitor",
                "node_a","node-1","node_b","node-2","epoch_a_ms","1700000000050","epoch_b_ms","1700000000080"));

        run("cross_node_time_drift_monitor (critical)",
            call("cross_node_time_drift_monitor",
                "node_a","node-1","node_b","node-4","epoch_a_ms","1700000000000","epoch_b_ms","1700000002500"));

        run("model_governance_registry (register)",
            call("model_governance_registry",
                "model_id","stt-scorer-v4","operation","register","version","v4.0.0","owner_team","ml-platform"));

        run("model_governance_registry (promote)",
            call("model_governance_registry",
                "model_id","stt-scorer-v4","operation","promote","version","v4.0.0","owner_team","ml-platform"));

        run("model_governance_registry (deprecate)",
            call("model_governance_registry",
                "model_id","stt-scorer-v2","operation","deprecate","version","v2.3.1","owner_team","ml-platform"));

        // ── Negative ─────────────────────────────────────────────────────────
        runExpectError("unknown_tool", call("does_not_exist_xyz"));

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    // ── Builders ──────────────────────────────────────────────────────────────

    private static String rpc(String method) {
        return "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"" + method + "\"}";
    }
    private static String rpcList() {
        return "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"tools/list\",\"params\":{}}";
    }
    private static String call(String tool, String... kvs) {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i + 1 < kvs.length; i += 2) {
            if (i > 0) sb.append(',');
            String v = kvs[i + 1];
            boolean num = v.matches("-?\\d+(\\.\\d+)?");
            sb.append("\"").append(kvs[i]).append("\":");
            if (num) sb.append(v); else sb.append("\"").append(v.replace("\"","'")).append("\"");
        }
        sb.append("}");
        return "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"tools/call\"," +
               "\"params\":{\"name\":\"" + tool + "\",\"arguments\":" + sb + "}}";
    }

    // ── Assertions ───────────────────────────────────────────────────────────

    private static void run(String label, String req) {
        try {
            String resp = H.handle(req);
            if (resp != null && resp.contains("\"result\"")) {
                passed++; System.out.println("[PASS] " + label);
            } else {
                failed++; System.out.println("[FAIL] " + label + "\n       => " + resp);
            }
            if (verbose) System.out.println("       " + resp);
        } catch (Exception e) {
            failed++; System.out.println("[ERROR] " + label + ": " + e.getMessage());
        }
    }

    private static void runExpectError(String label, String req) {
        try {
            String resp = H.handle(req);
            if (resp != null && resp.contains("\"error\"")) {
                passed++; System.out.println("[PASS] " + label + " (error as expected)");
            } else {
                failed++; System.out.println("[FAIL] " + label + " — expected error, got: " + resp);
            }
        } catch (Exception e) {
            failed++; System.out.println("[ERROR] " + label + ": " + e.getMessage());
        }
    }
}
