package com.ecoskiller.mcp.jicofo.tools;

import com.ecoskiller.mcp.jicofo.server.BaseTool;
import com.ecoskiller.mcp.jicofo.server.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;

/**
 * Tool: jvb_bridge_selection
 *
 * Selects the optimal Jitsi Video Bridge (JVB) instance for a new conference session.
 * Jicofo's most critical routing decision: made once at room creation, cannot change mid-session.
 *
 * Selection criteria (§3.2 / §4.2):
 *   1. Current participant load (Jitsi Bridge REST API)
 *   2. CPU/memory headroom (Prometheus metrics)
 *   3. Geographic proximity (GCP asia-south1 vs AWS ap-south-1)
 *   4. Bridge health status (HEALTHY | DEGRADED | UNAVAILABLE)
 *
 * Multi-bridge support: >500 concurrent participants distributed across 20+ JVB instances.
 * Default per-bridge limit: 50 participants per session.
 *
 * Operations:
 *   - SELECT:     Select the best bridge for a new session
 *   - REASSIGN:   Reassign a session to a different bridge (for load rebalancing — requires disconnect)
 *   - GET_LOAD:   Get current load across all bridges
 */
public class JvbBridgeSelectionTool extends BaseTool {

    @Override public String getName()        { return "jvb_bridge_selection"; }
    @Override public String getDescription() {
        return "Select the optimal Jitsi Video Bridge (JVB) instance for a new session or get load distribution " +
               "across all bridges. Jicofo evaluates participant load, CPU headroom, geographic proximity, and " +
               "bridge health. Selection is final at room creation — changing bridges mid-session disconnects all participants. " +
               "Operations: SELECT | REASSIGN | GET_LOAD";
    }

    @Override
    protected void addProperties(ObjectMapper m, ObjectNode props) {
        addProp(m, props, "operation",          "string",  "Operation: SELECT | REASSIGN | GET_LOAD");
        addProp(m, props, "session_id",         "string",  "Session ID for SELECT/REASSIGN");
        addProp(m, props, "session_type",       "string",  "SESSION type: GROUP_DISCUSSION | INTERVIEW (affects bridge preference)");
        addProp(m, props, "preferred_region",   "string",  "Preferred cloud region: gcp-asia-south1 | aws-ap-south-1 (optional)");
        addProp(m, props, "expected_participants","integer","Expected participant count — used to pre-screen bridge capacity");
        addProp(m, props, "current_bridge_id",  "string",  "Current bridge ID for REASSIGN operation");
        addProp(m, props, "force_reassign",     "boolean", "Force bridge reassignment even if it disconnects participants (REASSIGN only)");
    }

    @Override
    public ObjectNode buildInputSchema(ObjectMapper m) {
        return baseSchema(m, "operation");
    }

    @Override
    public ToolResult execute(JsonNode args) {
        String op         = str(args, "operation",            "").toUpperCase();
        String sessionId  = sanitize(str(args, "session_id", ""));
        String sessType   = sanitize(str(args, "session_type","GROUP_DISCUSSION"));
        String region     = sanitize(str(args, "preferred_region", ""));
        int    expected   = num(args, "expected_participants", 6);
        String curBridge  = sanitize(str(args, "current_bridge_id", ""));
        boolean forceRe   = bool(args, "force_reassign", false);

        return switch (op) {
            case "SELECT"   -> selectBridge(sessionId, sessType, region, expected);
            case "REASSIGN" -> reassignBridge(sessionId, curBridge, region, forceRe);
            case "GET_LOAD" -> getBridgeLoad();
            default         -> ToolResult.error("Unknown operation: '" + op + "'. Supported: SELECT | REASSIGN | GET_LOAD");
        };
    }

    private ToolResult selectBridge(String sessionId, String sessType, String region, int expected) {
        // Simulated bridge selection logic
        // In production: Jicofo queries Bridge REST API and Prometheus
        String selectedBridge;
        String selectionReason;

        if (region.contains("aws")) {
            selectedBridge   = "jvb-aws-1";
            selectionReason  = "Requested AWS region; bridge has 23 participants, CPU 38% — lowest load in region";
        } else {
            selectedBridge   = "jvb-gcp-1";
            selectionReason  = "Default GCP asia-south1; bridge has 18 participants, CPU 31% — lowest overall load";
        }

        boolean audioOnly = sessType.equals("GROUP_DISCUSSION");
        int     capacity  = 50 - expected; // Remaining capacity after expected

        return ToolResult.success(json(
            "status",              "BRIDGE_SELECTED",
            "session_id",          sessionId.isBlank() ? "pending-session" : sessionId,
            "selected_bridge",     selectedBridge,
            "bridge_region",       selectedBridge.contains("gcp") ? "gcp-asia-south1" : "aws-ap-south-1",
            "bridge_host",         selectedBridge + ".jitsi.ecoskiller.internal",
            "current_participants","18",
            "cpu_pct",             "31",
            "memory_pct",          "44",
            "remaining_capacity",  String.valueOf(capacity),
            "audio_only_forced",   String.valueOf(audioOnly),
            "selection_reason",    selectionReason,
            "selection_criteria",  "[\"participant_load\",\"cpu_headroom\",\"geographic_proximity\",\"health_status\"]",
            "selected_at",         Instant.now().toString(),
            "note",                "Bridge selection is FINAL at room creation — changing mid-session disconnects all participants"
        ));
    }

    private ToolResult reassignBridge(String sessionId, String curBridge, String newRegion, boolean force) {
        if (!isValidSessionId(sessionId))
            return ToolResult.error("Invalid session_id required for REASSIGN.");
        if (curBridge.isBlank())
            return ToolResult.error("current_bridge_id required for REASSIGN.");
        if (!force)
            return ToolResult.error("REASSIGN requires force_reassign=true. Warning: this will disconnect all " +
                                    "participants in session " + sessionId + " for ~2 seconds during bridge migration.");

        String newBridge = curBridge.equals("jvb-gcp-1") ? "jvb-gcp-2" : "jvb-gcp-1";

        return ToolResult.success(json(
            "status",            "REASSIGNMENT_INITIATED",
            "session_id",        sessionId,
            "old_bridge",        curBridge,
            "new_bridge",        newBridge,
            "expected_downtime", "< 2 seconds (bridge migration SLA)",
            "kafka_event",       "bridge.failover_started → analytics-service",
            "jicofo_action",     "Participants migrating from " + curBridge + " to " + newBridge +
                                 "; XMPP signalling rerouted; Redis bridge_id updated",
            "initiated_at",      Instant.now().toString()
        ));
    }

    private ToolResult getBridgeLoad() {
        return ToolResult.success(json(
            "status",       "OK",
            "total_bridges","4",
            "bridges", "[" +
                "{\"id\":\"jvb-gcp-1\",\"region\":\"gcp-asia-south1\",\"health\":\"HEALTHY\",\"participants\":18,\"sessions\":3,\"cpu_pct\":31,\"memory_pct\":44,\"capacity_remaining\":32}," +
                "{\"id\":\"jvb-gcp-2\",\"region\":\"gcp-asia-south1\",\"health\":\"HEALTHY\",\"participants\":23,\"sessions\":4,\"cpu_pct\":38,\"memory_pct\":51,\"capacity_remaining\":27}," +
                "{\"id\":\"jvb-aws-1\",\"region\":\"aws-ap-south-1\",\"health\":\"HEALTHY\",\"participants\":11,\"sessions\":2,\"cpu_pct\":22,\"memory_pct\":36,\"capacity_remaining\":39}," +
                "{\"id\":\"jvb-aws-2\",\"region\":\"aws-ap-south-1\",\"health\":\"DEGRADED\",\"participants\":41,\"sessions\":7,\"cpu_pct\":87,\"memory_pct\":78,\"capacity_remaining\":9,\"degraded_reason\":\"cpu_high\"}" +
            "]",
            "recommended_bridge","jvb-aws-1",
            "total_participants","93",
            "queried_at",       Instant.now().toString(),
            "prometheus_source","jicofo bridge metrics scrape interval: 15s"
        ));
    }

    private void addProp(ObjectMapper m, ObjectNode props, String name, String type, String desc) {
        ObjectNode p = m.createObjectNode();
        p.put("type", type);
        p.put("description", desc);
        props.set(name, p);
    }
}
