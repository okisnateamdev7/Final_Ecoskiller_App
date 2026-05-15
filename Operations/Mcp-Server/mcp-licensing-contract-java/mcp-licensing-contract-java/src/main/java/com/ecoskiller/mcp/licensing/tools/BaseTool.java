package com.ecoskiller.mcp.licensing.tools;

import com.ecoskiller.mcp.licensing.security.SecurityManager;
import org.json.JSONObject;

import java.time.Instant;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Abstract base for all licensing-contract-service MCP tools.
 * Provides shared helpers: timestamp, UUID generation, response building.
 */
public abstract class BaseTool {

    protected final Logger LOG = Logger.getLogger(getClass().getName());
    protected final SecurityManager security;

    protected BaseTool(SecurityManager security) {
        this.security = security;
    }

    /** Entry point for every tool invocation. */
    public abstract String execute(JSONObject args) throws Exception;

    // ── Response builders ────────────────────────────────────────────────────

    protected String ok(JSONObject payload) {
        return new JSONObject()
                .put("status", "success")
                .put("timestamp", nowIso())
                .put("data", payload)
                .toString(2);
    }

    protected String okMessage(String message) {
        return new JSONObject()
                .put("status", "success")
                .put("timestamp", nowIso())
                .put("message", message)
                .toString(2);
    }

    // ── Common helpers ───────────────────────────────────────────────────────

    protected String nowIso() {
        return Instant.now().toString();
    }

    protected String newUuid() {
        return UUID.randomUUID().toString();
    }

    protected String require(JSONObject args, String field) {
        String v = args.optString(field, "").trim();
        if (v.isEmpty()) {
            throw new IllegalArgumentException("Required field missing: " + field);
        }
        return v;
    }

    protected String opt(JSONObject args, String field, String def) {
        String v = args.optString(field, def);
        return v == null || v.isBlank() ? def : v.trim();
    }

    protected JSONObject simulationNote() {
        JSONObject note = new JSONObject();
        if (security.isSimulationMode()) {
            note.put("simulation_mode", true)
                .put("note", "Running in SIMULATION_MODE. No real DB/Kafka/MinIO calls are made. " +
                             "Set SIMULATION_MODE=false and provide real connection env vars for production.");
        }
        return note;
    }
}
