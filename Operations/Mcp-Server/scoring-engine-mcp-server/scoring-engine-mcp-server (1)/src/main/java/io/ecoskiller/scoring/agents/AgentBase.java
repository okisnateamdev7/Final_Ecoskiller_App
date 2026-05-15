package io.ecoskiller.scoring.agents;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import io.ecoskiller.scoring.config.ServerConfig;
import io.ecoskiller.scoring.security.AuditLogger;

import java.time.Instant;
import java.util.UUID;

public interface AgentHandler {
    JsonNode getToolDefinition();
    JsonNode execute(JsonNode args) throws Exception;
}

abstract class BaseAgent implements AgentHandler {
    protected final ObjectMapper mapper = new ObjectMapper();
    protected final ServerConfig config;
    protected final AuditLogger audit;

    protected BaseAgent(ServerConfig c, AuditLogger a) { config = c; audit = a; }

    protected ObjectNode ok(String msg) {
        ObjectNode n = mapper.createObjectNode();
        n.put("status","success"); n.put("message",msg);
        n.put("timestamp", Instant.now().toString()); return n;
    }

    protected String genId(String p) {
        return p + "-" + UUID.randomUUID().toString().replace("-","").substring(0,12);
    }

    protected String str(JsonNode n, String f, String d) {
        return (n!=null && n.has(f) && !n.get(f).isNull()) ? n.get(f).asText() : d;
    }
    protected int num(JsonNode n, String f, int d) { return (n!=null&&n.has(f))?n.get(f).asInt(d):d; }
    protected double dbl(JsonNode n, String f, double d) { return (n!=null&&n.has(f))?n.get(f).asDouble(d):d; }
    protected boolean bool(JsonNode n, String f, boolean d) { return (n!=null&&n.has(f))?n.get(f).asBoolean(d):d; }

    protected void prop(ObjectNode p, String n, String desc) { p.putObject(n).put("type","string").put("description",desc); }
    protected void numProp(ObjectNode p, String n, String desc) { p.putObject(n).put("type","number").put("description",desc); }
    protected void intProp(ObjectNode p, String n, String desc) { p.putObject(n).put("type","integer").put("description",desc); }
    protected void boolProp(ObjectNode p, String n, String desc) { p.putObject(n).put("type","boolean").put("description",desc); }
    protected void auth(ObjectNode p) {
        prop(p,"jwt_token","Keycloak JWT Bearer token. SCORING_SYSTEM or ADMIN role required for scoring operations.");
    }

    protected ObjectNode toolDef(String name, String desc) {
        ObjectNode t = mapper.createObjectNode(); t.put("name",name); t.put("description",desc); return t;
    }
    protected ObjectNode schema(ObjectNode t) {
        ObjectNode s = t.putObject("inputSchema"); s.put("type","object"); return s;
    }
}
