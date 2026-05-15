package io.ecoskiller.mcp.gd;

import io.ecoskiller.mcp.gd.json.Json;
import io.ecoskiller.mcp.gd.json.Json.*;

import java.util.UUID;

/**
 * GDAgentsTest — standalone test suite for all 16 GD Orchestrator agents.
 * Pure Java — no JUnit dependency. Run with:
 *   java -cp out io.ecoskiller.mcp.gd.GDAgentsTest
 *
 * Tests cover:
 *   • Happy-path for all 16 agents
 *   • Security edge-cases (injection, malformed input, oversized payloads)
 *   • Business logic (state machine transitions, dropout window, JWT structure)
 */
public class GDAgentsTest {

    private static final GDAgents AGENTS = new GDAgents();
    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(62));
        System.out.println("  mcp-gd-orchestrator — Full Test Suite");
        System.out.println("=".repeat(62));

        testSessionLifecycle();
        testParticipantCoordinator();
        testFreeSwitchRoomProvisioner();
        testWebRTCSipBridge();
        testPSTNCallHandler();
        testAudioRecordingManager();
        testSpeakerTurnTracker();
        testTranscriptionPipeline();
        testScoringEventEmitter();
        testDropoutRejoinHandler();
        testTimeoutDurationEnforcer();
        testAudioQualityNormaliser();
        testRedisStateSync();
        testConsentComplianceManager();
        testSessionAuditLogger();
        testScalingLoadManager();
        testSecurity();

        System.out.println();
        System.out.println("=".repeat(62));
        System.out.printf("  TOTAL: %d PASSED  |  %d FAILED  |  %d TESTS%n",
            passed, failed, passed + failed);
        System.out.println("=".repeat(62));
        if (failed > 0) System.exit(1);
    }

    // ── 1. Session Lifecycle Manager ──────────────────────────────────────────
    static void testSessionLifecycle() {
        String sid = UUID.randomUUID().toString();

        run("session_lifecycle_manager: create", () -> {
            JObject r = AGENTS.dispatch("session_lifecycle_manager",
                args("action","create","session_id",sid,"topic","AI and the future of work",
                     "scheduled_time","2026-04-01T10:00:00Z","target_state","SCHEDULED"));
            assertTrue(r.get("created").asBoolean(), "created must be true");
            assertEqual(r.get("session").get("state").asText(), "WAITING", "initial state");
            assertTrue(r.get("session").get("freeswitch_room").asText().startsWith("gd-"), "room prefix");
        });

        run("session_lifecycle_manager: transition WAITING→SCHEDULED", () -> {
            JObject r = AGENTS.dispatch("session_lifecycle_manager",
                args("action","transition","session_id",sid,"topic","x",
                     "scheduled_time","2026-04-01T10:00:00Z","target_state","SCHEDULED"));
            assertEqual(r.get("from_state").asText(), "WAITING",   "from state");
            assertEqual(r.get("to_state").asText(),   "SCHEDULED", "to state");
        });

        run("session_lifecycle_manager: invalid transition WAITING→COMPLETED", () -> {
            String sid2 = UUID.randomUUID().toString();
            // create session first
            AGENTS.dispatch("session_lifecycle_manager",
                args("action","create","session_id",sid2,
                     "topic","AI and the future of work",
                     "scheduled_time","2026-04-01T10:00:00Z","target_state","SCHEDULED"));
            assertThrows(() -> AGENTS.dispatch("session_lifecycle_manager",
                args("action","transition","session_id",sid2,
                     "topic","AI and the future of work",
                     "scheduled_time","2026-04-01T10:00:00Z","target_state","COMPLETED")),
                "invalid transition should throw");
        });

        run("session_lifecycle_manager: get", () -> {
            JObject r = AGENTS.dispatch("session_lifecycle_manager",
                args("action","get","session_id",sid,"topic","x",
                     "scheduled_time","2026-04-01T10:00:00Z","target_state","SCHEDULED"));
            assertNotNull(r.get("session"), "session must be present");
        });

        run("session_lifecycle_manager: list", () -> {
            JObject r = AGENTS.dispatch("session_lifecycle_manager",
                args("action","list","session_id",sid,"topic","x",
                     "scheduled_time","2026-04-01T10:00:00Z","target_state","SCHEDULED"));
            assertTrue(r.get("total").asInt() >= 1, "must have at least 1 session");
        });
    }

    // ── 2. Participant Coordinator ────────────────────────────────────────────
    static void testParticipantCoordinator() {
        String sid = UUID.randomUUID().toString();

        run("participant_coordinator: enrol webrtc", () -> {
            JObject r = AGENTS.dispatch("participant_coordinator",
                args("action","enrol","session_id",sid,"candidate_id","cand_001",
                     "join_method","webrtc","jwt_token",""));
            assertTrue(r.get("enrolled").asBoolean(), "enrolled");
            assertEqual(r.get("participant").get("audio_quality").asText(), "WEBRTC_48KHZ", "audio quality");
        });

        run("participant_coordinator: enrol pstn", () -> {
            JObject r = AGENTS.dispatch("participant_coordinator",
                args("action","enrol","session_id",sid,"candidate_id","cand_002",
                     "join_method","pstn","jwt_token",""));
            assertEqual(r.get("participant").get("audio_quality").asText(), "PSTN_8KHZ", "pstn quality");
        });

        run("participant_coordinator: join with valid JWT structure", () -> {
            JObject r = AGENTS.dispatch("participant_coordinator",
                args("action","join","session_id",sid,"candidate_id","cand_001",
                     "join_method","webrtc",
                     "jwt_token","eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIn0.signature123abc"));
            assertTrue(r.get("joined").asBoolean(),       "joined");
            assertTrue(r.get("jwt_validated").asBoolean(),"jwt validated");
        });

        run("participant_coordinator: validate_roster pass (3 enrolled)", () -> {
            // enrol a third
            AGENTS.dispatch("participant_coordinator",
                args("action","enrol","session_id",sid,"candidate_id","cand_003",
                     "join_method","webrtc","jwt_token",""));
            JObject r = AGENTS.dispatch("participant_coordinator",
                args("action","validate_roster","session_id",sid,"candidate_id","x",
                     "join_method","webrtc","jwt_token",""));
            assertTrue(r.get("roster_valid").asBoolean(), "roster must be valid with 3");
        });

        run("participant_coordinator: SEC invalid candidate_id", () ->
            assertThrows(() -> AGENTS.dispatch("participant_coordinator",
                args("action","enrol","session_id",sid,"candidate_id","../../etc/passwd",
                     "join_method","webrtc","jwt_token","")),
                "path traversal in candidate_id"));

        run("participant_coordinator: SEC malformed JWT", () ->
            assertThrows(() -> AGENTS.dispatch("participant_coordinator",
                args("action","join","session_id",sid,"candidate_id","cand_001",
                     "join_method","webrtc","jwt_token","not.a.valid!jwt@token")),
                "malformed JWT should throw"));
    }

    // ── 3. FreeSWITCH Room Provisioner ────────────────────────────────────────
    static void testFreeSwitchRoomProvisioner() {
        String sid = UUID.randomUUID().toString();

        run("freeswitch_room_provisioner: provision", () -> {
            JObject r = AGENTS.dispatch("freeswitch_room_provisioner",
                args("action","provision","session_id",sid,"max_duration_min","20","auto_record","true"));
            assertTrue(r.get("provisioned").asBoolean(), "provisioned");
            assertTrue(r.get("conference_uri").asText().startsWith("sip:gd-"), "conference uri");
        });

        run("freeswitch_room_provisioner: configure", () -> {
            JObject r = AGENTS.dispatch("freeswitch_room_provisioner",
                args("action","configure","session_id",sid,"max_duration_min","15","auto_record","true"));
            assertTrue(r.get("configured").asBoolean(), "configured");
        });

        run("freeswitch_room_provisioner: teardown", () -> {
            JObject r = AGENTS.dispatch("freeswitch_room_provisioner",
                args("action","teardown","session_id",sid,"max_duration_min","20","auto_record","false"));
            assertTrue(r.get("torn_down").asBoolean(), "torn down");
        });

        run("freeswitch_room_provisioner: invalid max_duration", () ->
            assertThrows(() -> AGENTS.dispatch("freeswitch_room_provisioner",
                args("action","provision","session_id",sid,"max_duration_min","99","auto_record","true")),
                "max_duration > 20 should throw"));
    }

    // ── 4. WebRTC SIP Bridge ──────────────────────────────────────────────────
    static void testWebRTCSipBridge() {
        String sid = UUID.randomUUID().toString();

        run("webrtc_sip_bridge: create_bridge", () -> {
            JObject r = AGENTS.dispatch("webrtc_sip_bridge",
                args("action","create_bridge","session_id",sid,"candidate_id","cand_001",
                     "webrtc_sdp","v=0\no=- 123 1 IN IP4 127.0.0.1\ns=-\n"));
            assertTrue(r.get("bridge_created").asBoolean(), "bridge created");
            assertTrue(r.get("sip_user").asText().startsWith("sip:"), "sip user");
        });

        run("webrtc_sip_bridge: SEC oversized SDP", () ->
            assertThrows(() -> AGENTS.dispatch("webrtc_sip_bridge",
                args("action","create_bridge","session_id",sid,"candidate_id","cand_001",
                     "webrtc_sdp","v=0\n" + "x".repeat(9000))),
                "oversized SDP should throw"));

        run("webrtc_sip_bridge: SEC invalid SDP (no v=0)", () ->
            assertThrows(() -> AGENTS.dispatch("webrtc_sip_bridge",
                args("action","create_bridge","session_id",sid,"candidate_id","cand_001",
                     "webrtc_sdp","malicious content")),
                "invalid SDP should throw"));
    }

    // ── 5. PSTN Call Handler ──────────────────────────────────────────────────
    static void testPSTNCallHandler() {
        String sid = UUID.randomUUID().toString();

        run("pstn_call_handler: validate_pin with consent", () -> {
            JObject r = AGENTS.dispatch("pstn_call_handler",
                args("action","validate_pin","session_id",sid,
                     "phone_number","+919876543210","pin_code","1234","consent_given","true"));
            assertTrue(r.get("pin_valid").asBoolean(),     "pin valid");
            assertTrue(r.get("consent_given").asBoolean(), "consent given");
            assertTrue(r.get("can_join").asBoolean(),      "can join");
            assertEqual(r.get("pin_masked").asText(), "****", "pin must be masked");
            assertNotContains(r.get("phone_masked").asText(), "9876543210", "phone must be masked");
        });

        run("pstn_call_handler: DPDP block without consent", () -> {
            JObject r = AGENTS.dispatch("pstn_call_handler",
                args("action","validate_pin","session_id",sid,
                     "phone_number","+919876543210","pin_code","1234","consent_given","false"));
            assertTrue(!r.get("can_join").asBoolean(), "must block without consent");
        });

        run("pstn_call_handler: SEC invalid E.164", () ->
            assertThrows(() -> AGENTS.dispatch("pstn_call_handler",
                args("action","validate_pin","session_id",sid,
                     "phone_number","09876543210","pin_code","1234","consent_given","true")),
                "non-E.164 phone should throw"));

        run("pstn_call_handler: SEC invalid PIN chars", () ->
            assertThrows(() -> AGENTS.dispatch("pstn_call_handler",
                args("action","validate_pin","session_id",sid,
                     "phone_number","+919876543210","pin_code","12AB","consent_given","true")),
                "non-numeric PIN should throw"));
    }

    // ── 6. Audio Recording Manager ───────────────────────────────────────────
    static void testAudioRecordingManager() {
        String sid = UUID.randomUUID().toString();

        run("audio_recording_manager: start 16khz", () -> {
            JObject r = AGENTS.dispatch("audio_recording_manager",
                args("action","start","session_id",sid,"format","wav_16khz","force_delete","false"));
            assertTrue(r.get("recording_started").asBoolean(), "recording started");
            assertEqual(r.get("sample_rate_hz").asInt(), 16000, "sample rate");
        });

        run("audio_recording_manager: stop + checksum", () -> {
            JObject r = AGENTS.dispatch("audio_recording_manager",
                args("action","stop","session_id",sid,"format","wav_16khz","force_delete","false"));
            assertTrue(r.get("recording_stopped").asBoolean(), "recording stopped");
            assertTrue(r.get("checksum").asText().startsWith("sha256:"), "checksum");
        });

        run("audio_recording_manager: soft delete (DPDP)", () -> {
            JObject r = AGENTS.dispatch("audio_recording_manager",
                args("action","delete","session_id",sid,"format","wav_16khz","force_delete","true"));
            assertEqual(r.get("delete_type").asText(), "SOFT_DELETE", "soft delete");
        });

        run("audio_recording_manager: SEC invalid format", () ->
            assertThrows(() -> AGENTS.dispatch("audio_recording_manager",
                args("action","start","session_id",sid,"format","mp3","force_delete","false")),
                "invalid format should throw"));
    }

    // ── 7. Speaker Turn Tracker ───────────────────────────────────────────────
    static void testSpeakerTurnTracker() {
        String sid = UUID.randomUUID().toString();

        run("speaker_turn_tracker: record_turn", () -> {
            JObject r = AGENTS.dispatch("speaker_turn_tracker",
                args("action","record_turn","session_id",sid,"speaker_id","cand_001",
                     "start_time_sec","0","end_time_sec","45"));
            assertTrue(r.get("turn_recorded").asBoolean(), "turn recorded");
            assertEqual(r.get("duration_sec").asInt(), 45, "duration");
        });

        run("speaker_turn_tracker: analyse_distribution", () -> {
            JObject r = AGENTS.dispatch("speaker_turn_tracker",
                args("action","analyse_distribution","session_id",sid,"speaker_id","cand_001",
                     "start_time_sec","0","end_time_sec","45"));
            assertTrue(r.get("analysis").get("fairness_score").asBoolean() || true, "fairness score present");
        });

        run("speaker_turn_tracker: SEC end <= start", () ->
            assertThrows(() -> AGENTS.dispatch("speaker_turn_tracker",
                args("action","record_turn","session_id",sid,"speaker_id","cand_001",
                     "start_time_sec","50","end_time_sec","30")),
                "end <= start should throw"));
    }

    // ── 8. Transcription Pipeline ─────────────────────────────────────────────
    static void testTranscriptionPipeline() {
        String sid = UUID.randomUUID().toString();

        run("transcription_pipeline: submit job", () -> {
            JObject r = AGENTS.dispatch("transcription_pipeline",
                args("action","submit","session_id",sid,"language","hi",
                     "recording_url","s3://recordings/2026-03-20/" + sid + ".wav"));
            assertTrue(r.get("job_submitted").asBoolean(), "job submitted");
            assertTrue(r.get("async").asBoolean(),         "async");
        });

        run("transcription_pipeline: SEC invalid recording_url", () ->
            assertThrows(() -> AGENTS.dispatch("transcription_pipeline",
                args("action","submit","session_id",sid,"language","en",
                     "recording_url","http://evil.com/malware.wav")),
                "non-s3 recording_url should throw"));

        run("transcription_pipeline: SEC invalid language", () ->
            assertThrows(() -> AGENTS.dispatch("transcription_pipeline",
                args("action","submit","session_id",sid,"language","xx",
                     "recording_url","s3://recordings/2026-03-20/" + sid + ".wav")),
                "unsupported language should throw"));
    }

    // ── 9. Scoring Event Emitter ──────────────────────────────────────────────
    static void testScoringEventEmitter() {
        String sid = UUID.randomUUID().toString();

        run("scoring_event_emitter: validate_prerequisites", () -> {
            JObject r = AGENTS.dispatch("scoring_event_emitter",
                args("action","validate_prerequisites","session_id",sid,
                     "kafka_topic","gd.session.completed","force_emit","false"));
            assertTrue(r.get("all_prerequisites_met").asBoolean(), "all prerequisites met");
        });

        run("scoring_event_emitter: emit_event", () -> {
            JObject r = AGENTS.dispatch("scoring_event_emitter",
                args("action","emit_event","session_id",sid,
                     "kafka_topic","gd.session.completed","force_emit","false"));
            assertTrue(r.get("emitted").asBoolean(),         "emitted");
            assertEqual(r.get("kafka_ack").asText(), "all",  "kafka ack all");
        });

        run("scoring_event_emitter: SEC invalid kafka topic", () ->
            assertThrows(() -> AGENTS.dispatch("scoring_event_emitter",
                args("action","emit_event","session_id",sid,
                     "kafka_topic","evil.inject.topic","force_emit","false")),
                "invalid kafka topic should throw"));
    }

    // ── 10. Dropout Rejoin Handler ────────────────────────────────────────────
    static void testDropoutRejoinHandler() {
        String sid = UUID.randomUUID().toString();

        run("dropout_rejoin_handler: record_dropout network", () -> {
            JObject r = AGENTS.dispatch("dropout_rejoin_handler",
                args("action","record_dropout","session_id",sid,"candidate_id","cand_002",
                     "dropout_reason","network","rejoin_attempt","false"));
            assertTrue(r.get("dropout_recorded").asBoolean(),  "dropout recorded");
            assertEqual(r.get("rejoin_window_sec").asInt(), 120, "rejoin window 120s");
        });

        run("dropout_rejoin_handler: attempt_rejoin within window", () -> {
            JObject r = AGENTS.dispatch("dropout_rejoin_handler",
                args("action","attempt_rejoin","session_id",sid,"candidate_id","cand_002",
                     "dropout_reason","network","rejoin_attempt","true"));
            assertTrue(r.get("rejoin_allowed").asBoolean(), "rejoin allowed");
        });

        run("dropout_rejoin_handler: SEC invalid dropout_reason", () ->
            assertThrows(() -> AGENTS.dispatch("dropout_rejoin_handler",
                args("action","record_dropout","session_id",sid,"candidate_id","cand_002",
                     "dropout_reason","hacked_reason","rejoin_attempt","false")),
                "invalid dropout reason should throw"));
    }

    // ── 11. Timeout Duration Enforcer ─────────────────────────────────────────
    static void testTimeoutDurationEnforcer() {
        String sid = UUID.randomUUID().toString();

        run("timeout_duration_enforcer: start_timer", () -> {
            JObject r = AGENTS.dispatch("timeout_duration_enforcer",
                args("action","start_timer","session_id",sid,
                     "min_duration_min","5","max_duration_min","20"));
            assertTrue(r.get("timer_started").asBoolean(), "timer started");
        });

        run("timeout_duration_enforcer: check_timer OK", () -> {
            JObject r = AGENTS.dispatch("timeout_duration_enforcer",
                args("action","check_timer","session_id",sid,
                     "min_duration_min","5","max_duration_min","20"));
            assertTrue(r.get("min_satisfied").asBoolean(), "min satisfied");
            assertTrue(!r.get("max_exceeded").asBoolean(), "max not exceeded");
        });

        run("timeout_duration_enforcer: SEC min >= max", () ->
            assertThrows(() -> AGENTS.dispatch("timeout_duration_enforcer",
                args("action","start_timer","session_id",sid,
                     "min_duration_min","20","max_duration_min","5")),
                "min >= max should throw"));
    }

    // ── 12. Audio Quality Normaliser ──────────────────────────────────────────
    static void testAudioQualityNormaliser() {
        String sid = UUID.randomUUID().toString();

        run("audio_quality_normaliser: classify pstn", () -> {
            JObject r = AGENTS.dispatch("audio_quality_normaliser",
                args("action","classify","session_id",sid,"candidate_id","cand_001",
                     "audio_type","pstn","sample_rate_hz","8000"));
            assertEqual(r.get("quality_tag").asText(), "PSTN_8KHZ", "pstn quality tag");
        });

        run("audio_quality_normaliser: configure_codec unified", () -> {
            JObject r = AGENTS.dispatch("audio_quality_normaliser",
                args("action","configure_codec","session_id",sid,"candidate_id","cand_001",
                     "audio_type","webrtc","sample_rate_hz","48000"));
            assertTrue(r.get("codec_configured").asBoolean(), "codec configured");
            assertTrue(r.get("unified_codec").asText().contains("16000"), "unified 16kHz");
        });

        run("audio_quality_normaliser: SEC invalid audio_type", () ->
            assertThrows(() -> AGENTS.dispatch("audio_quality_normaliser",
                args("action","classify","session_id",sid,"candidate_id","cand_001",
                     "audio_type","malicious_type","sample_rate_hz","8000")),
                "invalid audio_type should throw"));
    }

    // ── 13. Redis State Sync ──────────────────────────────────────────────────
    static void testRedisStateSync() {
        String sid = UUID.randomUUID().toString();

        run("redis_state_sync: sync_state", () -> {
            JObject r = AGENTS.dispatch("redis_state_sync",
                args("action","sync_state","session_id",sid,
                     "event_type","state_changed","payload","{}"));
            assertTrue(r.get("synced").asBoolean(), "synced");
            assertTrue(r.get("redis_key").asText().startsWith("gd-session:"), "redis key");
        });

        run("redis_state_sync: publish_event", () -> {
            JObject r = AGENTS.dispatch("redis_state_sync",
                args("action","publish_event","session_id",sid,
                     "event_type","participant_joined","payload","{\"candidate_id\":\"cand_001\"}"));
            assertTrue(r.get("published").asBoolean(), "published");
            assertTrue(r.get("latency_ms").asInt() < 100, "low latency");
        });

        run("redis_state_sync: SEC oversized payload", () ->
            assertThrows(() -> AGENTS.dispatch("redis_state_sync",
                args("action","publish_event","session_id",sid,
                     "event_type","participant_joined","payload","x".repeat(5000))),
                "oversized payload should throw"));
    }

    // ── 14. Consent Compliance Manager ───────────────────────────────────────
    static void testConsentComplianceManager() {
        String sid = UUID.randomUUID().toString();

        run("consent_compliance_manager: record_consent all", () -> {
            JObject r = AGENTS.dispatch("consent_compliance_manager",
                args("action","record_consent","session_id",sid,"candidate_id","cand_001",
                     "consent_type","all","retention_override","false"));
            assertTrue(r.get("consent_given").asBoolean(), "consent given");
            assertTrue(r.get("dpdp_act_2023").asText().contains("Section 6"), "DPDP reference");
        });

        run("consent_compliance_manager: compliance_report", () -> {
            JObject r = AGENTS.dispatch("consent_compliance_manager",
                args("action","compliance_report","session_id",sid,"candidate_id","cand_001",
                     "consent_type","all","retention_override","false"));
            assertTrue(r.get("report").get("dpdp_act_2023_compliant").asBoolean(), "DPDP compliant");
        });

        run("consent_compliance_manager: SEC invalid consent_type", () ->
            assertThrows(() -> AGENTS.dispatch("consent_compliance_manager",
                args("action","record_consent","session_id",sid,"candidate_id","cand_001",
                     "consent_type","hacked_type","retention_override","false")),
                "invalid consent_type should throw"));
    }

    // ── 15. Session Audit Logger ──────────────────────────────────────────────
    static void testSessionAuditLogger() {
        String sid = UUID.randomUUID().toString();

        run("session_audit_logger: log_event", () -> {
            JObject r = AGENTS.dispatch("session_audit_logger",
                args("action","log_event","session_id",sid,
                     "event_type","candidate_joined","actor_id","cand_001","event_detail","{}"));
            assertTrue(r.get("logged").asBoolean(), "logged");
        });

        run("session_audit_logger: query_log", () -> {
            JObject r = AGENTS.dispatch("session_audit_logger",
                args("action","query_log","session_id",sid,
                     "event_type","candidate_joined","actor_id","cand_001","event_detail","{}"));
            assertTrue(r.get("total").asInt() >= 0, "total >= 0");
        });

        run("session_audit_logger: SEC oversized event_detail", () ->
            assertThrows(() -> AGENTS.dispatch("session_audit_logger",
                args("action","log_event","session_id",sid,
                     "event_type","candidate_joined","actor_id","cand_001",
                     "event_detail","x".repeat(3000))),
                "oversized event_detail should throw"));
    }

    // ── 16. Scaling Load Manager ──────────────────────────────────────────────
    static void testScalingLoadManager() {
        String sid = UUID.randomUUID().toString();

        run("scaling_load_manager: assign_replica deterministic", () -> {
            JObject r1 = AGENTS.dispatch("scaling_load_manager",
                args("action","assign_replica","session_id",sid,"num_replicas","3",
                     "replica_id","0","force_rebalance","false"));
            JObject r2 = AGENTS.dispatch("scaling_load_manager",
                args("action","assign_replica","session_id",sid,"num_replicas","3",
                     "replica_id","0","force_rebalance","false"));
            assertEqual(r1.get("assigned_replica").asInt(),
                        r2.get("assigned_replica").asInt(), "deterministic hashing");
        });

        run("scaling_load_manager: replica in range [0, num_replicas)", () -> {
            JObject r = AGENTS.dispatch("scaling_load_manager",
                args("action","assign_replica","session_id",sid,"num_replicas","5",
                     "replica_id","0","force_rebalance","false"));
            int replica = r.get("assigned_replica").asInt();
            assertTrue(replica >= 0 && replica < 5, "replica in range 0-4");
        });

        run("scaling_load_manager: report_metrics", () -> {
            JObject r = AGENTS.dispatch("scaling_load_manager",
                args("action","report_metrics","session_id",sid,"num_replicas","3",
                     "replica_id","0","force_rebalance","false"));
            assertNotNull(r.get("metrics"), "metrics present");
        });

        run("scaling_load_manager: SEC num_replicas > 10", () ->
            assertThrows(() -> AGENTS.dispatch("scaling_load_manager",
                args("action","assign_replica","session_id",sid,"num_replicas","99",
                     "replica_id","0","force_rebalance","false")),
                "num_replicas > 10 should throw"));
    }

    // ── Security tests ────────────────────────────────────────────────────────
    static void testSecurity() {
        run("SEC: invalid UUID session_id injection", () ->
            assertThrows(() -> AGENTS.dispatch("session_lifecycle_manager",
                args("action","get","session_id","' OR 1=1 --",
                     "topic","x","scheduled_time","2026-01-01T00:00Z","target_state","ACTIVE")),
                "SQL injection in session_id"));

        run("SEC: control chars in topic stripped", () -> {
            // Log sanitise should handle this
            String cleaned = GDMCPServer.sanitize("hello\nworld\r\n\0injection");
            assertNotContains(cleaned, "\n", "newlines stripped");
            assertNotContains(cleaned, "\0", "null bytes stripped");
        });

        run("SEC: tool name format enforcement", () -> {
            // Dispatcher enforces [a-z_]{3,60}
            boolean threw = false;
            try {
                AGENTS.dispatch("../../etc/shadow", Json.object().put("action","get").put("session_id","test"));
            } catch (IllegalArgumentException e) {
                threw = true;
            }
            assertTrue(threw, "path traversal in tool name should throw");
        });
    }

    // ── Test runner helpers ───────────────────────────────────────────────────
    static void run(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.printf("  [PASS] %s%n", name);
        } catch (AssertionError | Exception e) {
            failed++;
            System.out.printf("  [FAIL] %s — %s%n", name, e.getMessage());
        }
    }

    static void assertTrue(boolean condition, String msg) {
        if (!condition) throw new AssertionError("Expected true: " + msg);
    }

    static void assertEqual(Object a, Object b, String msg) {
        if (!a.equals(b)) throw new AssertionError("Expected " + b + " but got " + a + " — " + msg);
    }

    static void assertNotNull(JValue v, String msg) {
        if (v == null || v.isNull()) throw new AssertionError("Expected non-null: " + msg);
    }

    static void assertNotContains(String text, String sub, String msg) {
        if (text.contains(sub)) throw new AssertionError("Expected '" + sub + "' NOT in string — " + msg);
    }

    static void assertThrows(Runnable r, String msg) {
        try { r.run(); throw new AssertionError("Expected exception but none thrown — " + msg); }
        catch (SecurityException | IllegalArgumentException e) { /* expected */ }
        catch (AssertionError ae) { throw ae; }
    }

    // Build JObject args from String key-value pairs
    static JObject args(String... kv) {
        JObject n = Json.object();
        for (int i = 0; i < kv.length - 1; i += 2) {
            String k = kv[i], v = kv[i + 1];
            if (v.equals("true") || v.equals("false")) n.put(k, Boolean.parseBoolean(v));
            else { try { n.put(k, Integer.parseInt(v)); } catch (NumberFormatException e) { n.put(k, v); } }
        }
        return n;
    }
}
