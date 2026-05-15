package com.ecoskiller.ranking.agents;

import com.ecoskiller.ranking.McpAgent;
import com.ecoskiller.ranking.security.SecurityManager;
import com.google.gson.*;
import java.time.Instant;

/**
 * BaseAgent — shared scaffolding for all Candidate Ranking Engine MCP agents.
 */
public abstract class BaseAgent implements McpAgent {

    protected final SecurityManager security;
    protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // 8 assessment dimensions from the service spec
    protected static final String[] DIMENSIONS = {
        "communication", "problem_solving", "technical", "collaboration",
        "leadership", "innovation", "adaptability", "consistency"
    };

    protected BaseAgent(SecurityManager security) { this.security = security; }

    // ── Tool Definition DSL ───────────────────────────────────────────────────

    protected JsonObject buildToolDef(String name, String description, JsonObject inputSchema) {
        JsonObject t = new JsonObject();
        t.addProperty("name",        name);
        t.addProperty("description", description);
        t.add("inputSchema",         inputSchema);
        return t;
    }

    protected JsonObject schema(String... required) {
        JsonObject s = new JsonObject();
        s.addProperty("type", "object");
        s.add("properties", new JsonObject());
        if (required.length > 0) {
            JsonArray req = new JsonArray();
            for (String r : required) req.add(r);
            s.add("required", req);
        }
        return s;
    }

    protected void strProp (JsonObject sc, String n, String d)              { JsonObject p=new JsonObject(); p.addProperty("type","string");  p.addProperty("description",d); sc.getAsJsonObject("properties").add(n,p); }
    protected void intProp (JsonObject sc, String n, String d)              { JsonObject p=new JsonObject(); p.addProperty("type","integer"); p.addProperty("description",d); sc.getAsJsonObject("properties").add(n,p); }
    protected void numProp (JsonObject sc, String n, String d)              { JsonObject p=new JsonObject(); p.addProperty("type","number");  p.addProperty("description",d); sc.getAsJsonObject("properties").add(n,p); }
    protected void boolProp(JsonObject sc, String n, String d)              { JsonObject p=new JsonObject(); p.addProperty("type","boolean"); p.addProperty("description",d); sc.getAsJsonObject("properties").add(n,p); }
    protected void enumProp(JsonObject sc, String n, String d, String... v) {
        JsonObject p = new JsonObject(); p.addProperty("type","string"); p.addProperty("description",d);
        JsonArray en = new JsonArray(); for (String x : v) en.add(x); p.add("enum",en);
        sc.getAsJsonObject("properties").add(n,p);
    }

    // ── Response Helpers ──────────────────────────────────────────────────────

    protected JsonObject ok(JsonObject data) {
        data.addProperty("status",    "success");
        data.addProperty("timestamp", Instant.now().toString());
        return data;
    }

    protected JsonObject ok(String msg) {
        JsonObject r = new JsonObject();
        r.addProperty("status",    "success");
        r.addProperty("message",   msg);
        r.addProperty("timestamp", Instant.now().toString());
        return r;
    }

    protected JsonObject err(String msg) {
        JsonObject r = new JsonObject();
        r.addProperty("status",    "error");
        r.addProperty("error",     msg);
        r.addProperty("timestamp", Instant.now().toString());
        return r;
    }

    // ── Dimension Score Builder ────────────────────────────────────────────────

    protected JsonObject buildDimensionScores(double base) {
        JsonObject d = new JsonObject();
        double[] offsets = {2.1, -1.4, 3.0, -0.8, 1.5, -2.0, 0.7, -1.1};
        for (int i = 0; i < DIMENSIONS.length; i++) {
            double score = Math.min(100, Math.max(0, base + offsets[i]));
            d.addProperty(DIMENSIONS[i], Math.round(score * 10) / 10.0);
        }
        return d;
    }

    // ── Arg Accessors ─────────────────────────────────────────────────────────

    protected String  str (JsonObject a, String k) { return security.str(a,  k, ""); }
    protected int     num (JsonObject a, String k)  { return security.num(a,  k, 0); }
    protected double  dbl (JsonObject a, String k)  { return security.dbl(a,  k, 0.0); }
    protected boolean bool(JsonObject a, String k)  { return security.bool(a, k, false); }
}
