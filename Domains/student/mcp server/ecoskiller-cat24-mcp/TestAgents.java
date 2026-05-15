package com.ecoskiller.mcp;
import com.ecoskiller.mcp.protocol.JsonRpcHandler;

/**
 * Tests all 36 CAT-24 agents (44 test cases total).
 * Usage: java -cp out com.ecoskiller.mcp.TestAgents [--verbose]
 */
public class TestAgents {
    private static final JsonRpcHandler H = new JsonRpcHandler();
    private static boolean verbose = false;
    private static int passed = 0, failed = 0;

    public static void main(String[] argv) {
        for (String a : argv) if ("--verbose".equals(a)) verbose = true;
        System.out.println("=== Ecoskiller mcp-24 — CAT-24 Agent Tests ===\n");

        // ── Protocol ──────────────────────────────────────────────────────────
        run("initialize", rpc("initialize", null, null));
        run("ping",       rpc("ping", null, null));
        run("tools/list", rpc("tools/list", null, null));

        // ── Scoring & Fairness ────────────────────────────────────────────────
        run("call_cost_calculation",
            call("call_cost_calculation","session_id","sess-001","duration_seconds","90","carrier_tier","premium"));
        run("call_rate_limit (ok)",
            call("call_rate_limit","tenant_id","t-001","calls_in_window","50","limit","100"));
        run("call_rate_limit (exceeded)",
            call("call_rate_limit","tenant_id","t-002","calls_in_window","120","limit","100"));
        run("high_usage_alert (warning)",
            call("high_usage_alert","tenant_id","t-003","usage_percent","85"));
        run("high_usage_alert (critical)",
            call("high_usage_alert","tenant_id","t-004","usage_percent","102"));
        run("offline_goto_dojo_score_sync",
            call("offline_goto_dojo_score_sync","learner_id","l-001","offline_scores","85,90,78","session_ids","s1,s2,s3"));
        run("phone_ai_explainability",
            call("phone_ai_explainability","session_id","sess-002","score","88","model_version","v3"));
        run("phone_behavior_analytics",
            call("phone_behavior_analytics","learner_id","l-002","sessions_analysed","10"));
        run("phone_scoring_input_sanitizer",
            call("phone_scoring_input_sanitizer","session_id","sess-003","raw_transcript","This is a valid transcript for scoring."));
        run("phone_speaking_time",
            call("phone_speaking_time","session_id","sess-004","speaker_durations_csv","SPEAKER_1:120,SPEAKER_2:80"));
        run("phone_score_dispute_analytics",
            call("phone_score_dispute_analytics","dispute_id","disp-001","session_id","sess-005","reason","Score calculation error"));
        run("score_bias_audit",
            call("score_bias_audit","model_id","scoring-v4","sample_size","5000"));
        run("scoring_model_deprecation",
            call("scoring_model_deprecation","model_id","scoring-v2","deprecation_date","2025-06-30"));
        run("phone_minimum_participation",
            call("phone_minimum_participation","session_id","sess-006","speaking_seconds","45","min_threshold_seconds","30"));

        // ── Security & Compliance ─────────────────────────────────────────────
        run("media_session_security",
            call("media_session_security","session_id","sess-007","encryption_protocol","DTLS-SRTP","certificate_fingerprint","sha256:ab12"));
        run("voice_impersonation_detection",
            call("voice_impersonation_detection","session_id","sess-008","claimed_speaker_id","learner-42","voice_confidence_score","45"));
        run("phone_bot_voice_detection",
            call("phone_bot_voice_detection","session_id","sess-009","bot_probability_score","85"));
        run("phone_domain_isolation (blocked)",
            call("phone_domain_isolation","tenant_id","t-001","resource_tenant_id","t-002","resource_type","audio"));
        run("phone_role_escalation_guard",
            call("phone_role_escalation_guard","user_id","u-001","current_role","learner","requested_role","admin"));
        run("tenant_audio_object_isolation",
            call("tenant_audio_object_isolation","tenant_id","t-001","audio_object_id","t-001-rec-abc","operation","read"));
        run("tenant_transcript_encryption",
            call("tenant_transcript_encryption","tenant_id","t-001","transcript_id","tx-001","operation","encrypt"));
        run("short_lived_token_revocation",
            call("short_lived_token_revocation","token_id","tok-xyz","revocation_reason","logout"));
        run("phone_permission_matrix",
            call("phone_permission_matrix","user_id","u-002","role","instructor","action","view_score","resource","session"));
        run("phone_cross_session_behavior",
            call("phone_cross_session_behavior","learner_id","l-003","sessions_reviewed","8","anomaly_score","75"));
        run("human_override_audit",
            call("human_override_audit","override_id","ovr-001","reviewer_id","rev-001","original_decision","PASS","new_decision","FAIL"));

        // ── Billing & Quota ───────────────────────────────────────────────────
        run("tenant_quota_enforcement (soft)",
            call("tenant_quota_enforcement","tenant_id","t-001","resource","calls","current_usage","950","quota_limit","1000"));
        run("phone_resource_quota (exceeded)",
            call("phone_resource_quota","session_id","sess-010","resource_type","bandwidth_kbps","consumed","600","max_allowed","512"));
        run("sms_segment_calculation (gsm)",
            call("sms_segment_calculation","message","Hello from Ecoskiller! Your session score is ready. Please login to view your detailed results and feedback."));
        run("sms_segment_calculation (ucs2)",
            call("sms_segment_calculation","message","Ecoskiller score ready"));
        run("telecom_usage_reconciliation",
            call("telecom_usage_reconciliation","tenant_id","t-001","internal_minutes","1200","carrier_minutes","1208"));
        run("phone_feature_gating (basic blocked)",
            call("phone_feature_gating","tenant_id","t-001","feature_name","custom_scoring","tenant_plan","basic"));
        run("phone_feature_gating (pro allowed)",
            call("phone_feature_gating","tenant_id","t-002","feature_name","custom_scoring","tenant_plan","pro"));
        run("phone_tenant_boundary_enforcement",
            call("phone_tenant_boundary_enforcement","source_tenant","t-001","target_tenant","t-002","data_type","transcript"));
        run("phone_transparency_notification",
            call("phone_transparency_notification","session_id","sess-011","participant_id","p-001","notification_type","recording_consent"));

        // ── Event & Contract ──────────────────────────────────────────────────
        run("kafka_event_schema_drift (stable)",
            call("kafka_event_schema_drift","topic","ecoskiller.phone.sessions","registered_schema_version","v5","observed_schema_hash","abcdef"));
        run("kafka_event_schema_drift (drift)",
            call("kafka_event_schema_drift","topic","ecoskiller.phone.sessions","registered_schema_version","v5","observed_schema_hash","xyz999"));
        run("global_event_registry_sync",
            call("global_event_registry_sync","service_id","mcp-24","event_count","15","registry_version","v4"));
        run("phone_event_schema_validation",
            call("phone_event_schema_validation","event_type","session.completed","payload","session_id:s1,tenant_id:t1"));
        run("phone_api_contract_registry",
            call("phone_api_contract_registry","contract_id","phone-api-v2","operation","register","api_version","v2"));
        run("phone_participant_identity",
            call("phone_participant_identity","session_id","sess-012","claimed_identity","student@school.edu","idp_token","eyJhbGciOiJSUzI1NiJ9verylongtoken"));
        run("phone_participation_reputation",
            call("phone_participation_reputation","learner_id","l-005","sessions_completed","20","avg_score","82","disputes_raised","1"));

        // ── Negative ──────────────────────────────────────────────────────────
        runExpectError("unknown_tool",
            call("nonexistent_tool_xyz"));

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    // ── JSON-RPC builders (no nested quotes) ─────────────────────────────────

    /** Build a generic RPC request. */
    private static String rpc(String method, String name, String argJson) {
        if (name == null) return "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"" + method + "\"}";
        return "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"" + method
             + "\",\"params\":{\"name\":\"" + name + "\",\"arguments\":" + argJson + "}}";
    }

    /** Build a tools/call request from alternating key-value pairs (all treated as strings). */
    private static String call(String tool, String... kvs) {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i + 1 < kvs.length; i += 2) {
            if (i > 0) sb.append(',');
            String val = kvs[i + 1];
            // If value looks like an integer, don't quote it
            boolean isNum = val.matches("-?\\d+(\\.\\d+)?");
            sb.append("\"").append(kvs[i]).append("\":");
            if (isNum) sb.append(val);
            else sb.append("\"").append(val.replace("\"","'")).append("\"");
        }
        sb.append("}");
        return "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"tools/call\",\"params\":{\"name\":\"" + tool + "\",\"arguments\":" + sb + "}}";
    }

    // ── Assertion helpers ─────────────────────────────────────────────────────

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
            if (verbose) System.out.println("       " + resp);
        } catch (Exception e) {
            failed++; System.out.println("[ERROR] " + label + ": " + e.getMessage());
        }
    }
}
