package com.ecoskiller.mcp.protocol;
import com.ecoskiller.mcp.agents.Agent;
import com.ecoskiller.mcp.agents.ClickhouseMetricNormalizationAgent;
import com.ecoskiller.mcp.agents.ErpGoReportIntegrationAgent;
import com.ecoskiller.mcp.agents.PhoneFeatureVectorEmissionAgent;
import com.ecoskiller.mcp.agents.AttendanceBehaviorAnalyticsAgent;
import com.ecoskiller.mcp.agents.EnrollmentAnalyticsAgent;
import com.ecoskiller.mcp.agents.MultiEnvironmentPhoneConfigValidatorAgent;
import com.ecoskiller.mcp.agents.PhoneBackupRestoreValidationAgent;
import com.ecoskiller.mcp.agents.PhoneEndToEndTraceAgent;
import com.ecoskiller.mcp.agents.PhoneExternalWebhookAgent;
import com.ecoskiller.mcp.agents.PhoneInfraHealthCheckAgent;
import com.ecoskiller.mcp.agents.PhoneMonitoringClockAuthorityAgent;
import com.ecoskiller.mcp.agents.CrossNodeTimeDriftMonitorAgent;
import com.ecoskiller.mcp.agents.ModelGovernanceRegistryAgent;
import java.util.*;

public class JsonRpcHandler {
    private final Map<String,Agent> agents = new LinkedHashMap<>();

    public JsonRpcHandler() {
        register(new ClickhouseMetricNormalizationAgent());
        register(new ErpGoReportIntegrationAgent());
        register(new PhoneFeatureVectorEmissionAgent());
        register(new AttendanceBehaviorAnalyticsAgent());
        register(new EnrollmentAnalyticsAgent());
        register(new MultiEnvironmentPhoneConfigValidatorAgent());
        register(new PhoneBackupRestoreValidationAgent());
        register(new PhoneEndToEndTraceAgent());
        register(new PhoneExternalWebhookAgent());
        register(new PhoneInfraHealthCheckAgent());
        register(new PhoneMonitoringClockAuthorityAgent());
        register(new CrossNodeTimeDriftMonitorAgent());
        register(new ModelGovernanceRegistryAgent());
    }

    private void register(Agent a) { agents.put(a.toolName(), a); }

    public String handle(String json) {
        try {
            Map<String,Object> req = JsonParser.parse(json);
            String method = (String) req.get("method");
            Object id = req.get("id");
            if ("initialize".equals(method)) return respond(id, buildInit());
            if ("ping".equals(method))       return respond(id, new LinkedHashMap<>());
            if ("tools/list".equals(method)) return respond(id, buildList());
            if ("tools/call".equals(method)) return handleCall(id, req);
            return err(id, -32601, "Method not found: " + method);
        } catch (Exception e) {
            return err(null, -32700, "Parse error: " + e.getMessage());
        }
    }

    private Map<String,Object> buildInit() {
        Map<String,Object> r = new LinkedHashMap<>();
        r.put("protocolVersion","2024-11-05");
        Map<String,Object> si = new LinkedHashMap<>();
        si.put("name","mcp-25-analytics-devops"); si.put("version","1.0.0");
        r.put("serverInfo",si);
        Map<String,Object> cap = new LinkedHashMap<>();
        cap.put("tools", new LinkedHashMap<>());
        r.put("capabilities",cap);
        return r;
    }

    private Map<String,Object> buildList() {
        List<Map<String,Object>> tools = new ArrayList<>();
        for (Agent ag : agents.values()) {
            Map<String,Object> t = new LinkedHashMap<>();
            t.put("name", ag.toolName());
            t.put("description", ag.description());
            t.put("inputSchema", ag.inputSchema());
            tools.add(t);
        }
        Map<String,Object> r = new LinkedHashMap<>();
        r.put("tools", tools);
        return r;
    }

    @SuppressWarnings("unchecked")
    private String handleCall(Object id, Map<String,Object> req) {
        Map<String,Object> params = (Map<String,Object>) req.getOrDefault("params", new LinkedHashMap<>());
        String name = (String) params.get("name");
        Map<String,Object> args = (Map<String,Object>) params.getOrDefault("arguments", new LinkedHashMap<>());
        Agent ag = agents.get(name);
        if (ag == null) return err(id, -32602, "Unknown tool: " + name);
        try {
            Map<String,Object> c = new LinkedHashMap<>();
            c.put("type","text"); c.put("text", ag.execute(args));
            Map<String,Object> r = new LinkedHashMap<>();
            r.put("content", Collections.singletonList(c)); r.put("isError",false);
            return respond(id, r);
        } catch (Exception e) {
            Map<String,Object> c = new LinkedHashMap<>();
            c.put("type","text"); c.put("text","Error: "+e.getMessage());
            Map<String,Object> r = new LinkedHashMap<>();
            r.put("content", Collections.singletonList(c)); r.put("isError",true);
            return respond(id, r);
        }
    }

    private String respond(Object id, Object result) {
        Map<String,Object> r = new LinkedHashMap<>();
        r.put("jsonrpc","2.0"); r.put("id",id); r.put("result",result);
        return JsonSerializer.toJson(r);
    }
    private String err(Object id, int code, String msg) {
        Map<String,Object> e = new LinkedHashMap<>();
        e.put("code",code); e.put("message",msg);
        Map<String,Object> r = new LinkedHashMap<>();
        r.put("jsonrpc","2.0"); r.put("id",id); r.put("error",e);
        return JsonSerializer.toJson(r);
    }
}
