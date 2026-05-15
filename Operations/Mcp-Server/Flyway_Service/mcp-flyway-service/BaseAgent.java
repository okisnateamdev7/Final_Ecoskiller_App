package com.ecoskiller.flyway.agents;

import com.ecoskiller.flyway.McpAgent;
import com.ecoskiller.flyway.security.SecurityManager;
import com.google.gson.*;

import java.time.Instant;

/**
 * BaseAgent — shared scaffolding for all Flyway Service MCP agents.
 * Provides tool definition DSL, safe response helpers, and schema constants.
 */
public abstract class BaseAgent implements McpAgent {

    protected final SecurityManager security;
    protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // All 13 Ecoskiller managed schemas
    protected static final String ALL_SCHEMAS =
        "core,billing,analytics,realtime,admin,notification," +
        "scoring,certification,interview,job,phone_bridge," +
        "royalty,innovation,intelligence,legal";

    protected BaseAgent(SecurityManager security) {
        this.security = security;
    }

    // ── Tool Definition DSL ───────────────────────────────────────────────────

    protected JsonObject buildToolDef(String name, String description, JsonObject inputSchema) {
        JsonObject tool = new JsonObject();
        tool.addProperty("name",        name);
        tool.addProperty("description", description);
        tool.add("inputSchema",         inputSchema);
        return tool;
    }

    protected JsonObject schema(String... requiredFields) {
        JsonObject s = new JsonObject();
        s.addProperty("type", "object");
        s.add("properties", new JsonObject());
        if (requiredFields.length > 0) {
            JsonArray req = new JsonArray();
            for (String f : requiredFields) req.add(f);
            s.add("required", req);
        }
        return s;
    }

    protected void addStringProp(JsonObject schema, String name, String desc) {
        JsonObject p = new JsonObject();
        p.addProperty("type", "string");
        p.addProperty("description", desc);
        schema.getAsJsonObject("properties").add(name, p);
    }

    protected void addIntProp(JsonObject schema, String name, String desc) {
        JsonObject p = new JsonObject();
        p.addProperty("type", "integer");
        p.addProperty("description", desc);
        schema.getAsJsonObject("properties").add(name, p);
    }

    protected void addBoolProp(JsonObject schema, String name, String desc) {
        JsonObject p = new JsonObject();
        p.addProperty("type", "boolean");
        p.addProperty("description", desc);
        schema.getAsJsonObject("properties").add(name, p);
    }

    protected void addEnumProp(JsonObject schema, String name, String desc, String... values) {
        JsonObject p = new JsonObject();
        p.addProperty("type",        "string");
        p.addProperty("description", desc);
        JsonArray en = new JsonArray();
        for (String v : values) en.add(v);
        p.add("enum", en);
        schema.getAsJsonObject("properties").add(name, p);
    }

    // ── Response Helpers ──────────────────────────────────────────────────────

    protected JsonObject ok(String message) {
        JsonObject r = new JsonObject();
        r.addProperty("status",    "success");
        r.addProperty("message",   message);
        r.addProperty("timestamp", Instant.now().toString());
        return r;
    }

    protected JsonObject ok(JsonObject data) {
        data.addProperty("status",    "success");
        data.addProperty("timestamp", Instant.now().toString());
        return data;
    }

    protected JsonObject err(String message) {
        JsonObject r = new JsonObject();
        r.addProperty("status",    "error");
        r.addProperty("error",     message);
        r.addProperty("timestamp", Instant.now().toString());
        return r;
    }

    // ── Arg Accessors ─────────────────────────────────────────────────────────

    protected String str(JsonObject a, String key)              { return security.getString(a, key, ""); }
    protected int    num(JsonObject a, String key, int def)     { return security.getInt(a, key, def); }
    protected boolean bool(JsonObject a, String key, boolean d) { return security.getBool(a, key, d); }
}
