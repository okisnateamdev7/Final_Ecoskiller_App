package com.ecoskiller.dojo.agents;

import com.ecoskiller.dojo.McpAgent;
import com.ecoskiller.dojo.security.SecurityManager;
import com.google.gson.*;

import java.time.Instant;

/**
 * BaseAgent — common scaffolding for all Dojo Match Engine agents.
 * Provides tool definition DSL and standard response helpers.
 */
public abstract class BaseAgent implements McpAgent {

    protected final SecurityManager security;
    protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected BaseAgent(SecurityManager security) {
        this.security = security;
    }

    // ─── Tool Definition DSL ──────────────────────────────────────────────────

    protected JsonObject buildToolDef(String name, String description, JsonObject inputSchema) {
        JsonObject tool = new JsonObject();
        tool.addProperty("name", name);
        tool.addProperty("description", description);
        tool.add("inputSchema", inputSchema);
        return tool;
    }

    protected JsonObject schema(String... requiredFields) {
        JsonObject schema = new JsonObject();
        schema.addProperty("type", "object");
        JsonObject props = new JsonObject();
        schema.add("properties", props);
        if (requiredFields.length > 0) {
            JsonArray req = new JsonArray();
            for (String f : requiredFields) req.add(f);
            schema.add("required", req);
        }
        return schema;
    }

    protected void addStringProp(JsonObject schema, String name, String description) {
        JsonObject p = new JsonObject();
        p.addProperty("type", "string");
        p.addProperty("description", description);
        schema.getAsJsonObject("properties").add(name, p);
    }

    protected void addIntProp(JsonObject schema, String name, String description) {
        JsonObject p = new JsonObject();
        p.addProperty("type", "integer");
        p.addProperty("description", description);
        schema.getAsJsonObject("properties").add(name, p);
    }

    protected void addNumberProp(JsonObject schema, String name, String description) {
        JsonObject p = new JsonObject();
        p.addProperty("type", "number");
        p.addProperty("description", description);
        schema.getAsJsonObject("properties").add(name, p);
    }

    protected void addEnumProp(JsonObject schema, String name, String description, String... values) {
        JsonObject p = new JsonObject();
        p.addProperty("type", "string");
        p.addProperty("description", description);
        JsonArray en = new JsonArray();
        for (String v : values) en.add(v);
        p.add("enum", en);
        schema.getAsJsonObject("properties").add(name, p);
    }

    protected void addBoolProp(JsonObject schema, String name, String description) {
        JsonObject p = new JsonObject();
        p.addProperty("type", "boolean");
        p.addProperty("description", description);
        schema.getAsJsonObject("properties").add(name, p);
    }

    // ─── Response Helpers ─────────────────────────────────────────────────────

    protected JsonObject success(String message) {
        JsonObject r = new JsonObject();
        r.addProperty("status", "success");
        r.addProperty("message", message);
        r.addProperty("timestamp", Instant.now().toString());
        return r;
    }

    protected JsonObject success(JsonObject data) {
        data.addProperty("status", "success");
        data.addProperty("timestamp", Instant.now().toString());
        return data;
    }

    protected JsonObject error(String message) {
        JsonObject r = new JsonObject();
        r.addProperty("status", "error");
        r.addProperty("error", message);
        r.addProperty("timestamp", Instant.now().toString());
        return r;
    }

    protected String str(JsonObject args, String key) {
        return security.getString(args, key, "");
    }

    protected int num(JsonObject args, String key, int def) {
        return security.getInt(args, key, def);
    }

    protected double dbl(JsonObject args, String key, double def) {
        return security.getDouble(args, key, def);
    }
}
