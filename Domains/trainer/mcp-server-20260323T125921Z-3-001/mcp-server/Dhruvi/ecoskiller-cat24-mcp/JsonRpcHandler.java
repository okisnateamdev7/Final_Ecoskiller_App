package com.ecoskiller.mcp.protocol;
import com.ecoskiller.mcp.agents.*;
import java.util.*;

public class JsonRpcHandler {
    private final Map<String, Agent> agents = new LinkedHashMap<>();

    public JsonRpcHandler() {
        // ── Scoring & Fairness ──
        register(new CallCostCalculationAgent());
        register(new CallRateLimitAgent());
        register(new HighUsageAlertAgent());
        register(new OfflineGoToDojoScoreSyncAgent());
        register(new PhoneAiExplainabilityAgent());
        register(new PhoneBehaviorAnalyticsAgent());
        register(new PhoneScoringInputSanitizerAgent());
        register(new PhoneSpeakingTimeAgent());
        register(new PhoneScoreDisputeAnalyticsAgent());
        register(new ScoreBiasAuditAgent());
        register(new ScoringModelDeprecationAgent());
        register(new PhoneMinimumParticipationAgent());
        // ── Security & Compliance ──
        register(new MediaSessionSecurityAgent());
        register(new VoiceImpersonationDetectionAgent());
        register(new PhoneBotVoiceDetectionAgent());
        register(new PhoneDomainIsolationAgent());
        register(new PhoneRoleEscalationGuardAgent());
        register(new TenantAudioObjectIsolationAgent());
        register(new TenantTranscriptEncryptionAgent());
        register(new ShortLivedTokenRevocationAgent());
        register(new PhonePermissionMatrixAgent());
        register(new PhoneCrossSessionBehaviorAgent());
        register(new HumanOverrideAuditAgent());
        // ── Billing & Quota ──
        register(new TenantQuotaEnforcementAgent());
        register(new PhoneResourceQuotaAgent());
        register(new SmsSegmentCalculationAgent());
        register(new TelecomUsageReconciliationAgent());
        register(new PhoneFeatureGatingAgent());
        register(new PhoneTenantBoundaryEnforcementAgent());
        register(new PhoneTransparencyNotificationAgent());
        // ── Event & Contract ──
        register(new KafkaEventSchemaDriftAgent());
        register(new GlobalEventRegistrySyncAgent());
        register(new PhoneEventSchemaValidationAgent());
        register(new PhoneApiContractRegistryAgent());
        register(new PhoneParticipantIdentityAgent());
        register(new PhoneParticipationReputationAgent());
    }

    private void register(Agent a) { agents.put(a.toolName(), a); }

    public String handle(String json) {
        try {
            Map<String, Object> req = JsonParser.parse(json);
            String method = (String) req.get("method");
            Object id = req.get("id");
            if ("initialize".equals(method)) return respond(id, buildInit());
            if ("ping".equals(method))       return respond(id, new LinkedHashMap<>());
            if ("tools/list".equals(method)) return respond(id, buildList());
            if ("tools/call".equals(method)) return handleCall(id, req);
            return error(id, -32601, "Method not found: " + method);
        } catch (Exception e) {
            return error(null, -32700, "Parse error: " + e.getMessage());
        }
    }

    private Map<String,Object> buildInit() {
        Map<String,Object> r = new LinkedHashMap<>();
        r.put("protocolVersion","2024-11-05");
        Map<String,Object> si = new LinkedHashMap<>();
        si.put("name","mcp-24-scoring-billing"); si.put("version","1.0.0");
        r.put("serverInfo",si);
        Map<String,Object> cap = new LinkedHashMap<>();
        cap.put("tools", new LinkedHashMap<>());
        r.put("capabilities",cap);
        return r;
    }

    private Map<String,Object> buildList() {
        List<Map<String,Object>> tools = new ArrayList<>();
        for (Agent a : agents.values()) {
            Map<String,Object> t = new LinkedHashMap<>();
            t.put("name", a.toolName());
            t.put("description", a.description());
            t.put("inputSchema", a.inputSchema());
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
        Agent a = agents.get(name);
        if (a == null) return error(id, -32602, "Unknown tool: " + name);
        try {
            Map<String,Object> content = new LinkedHashMap<>();
            content.put("type","text"); content.put("text", a.execute(args));
            Map<String,Object> r = new LinkedHashMap<>();
            r.put("content", Collections.singletonList(content));
            r.put("isError", false);
            return respond(id, r);
        } catch (Exception e) {
            Map<String,Object> content = new LinkedHashMap<>();
            content.put("type","text"); content.put("text","Error: "+e.getMessage());
            Map<String,Object> r = new LinkedHashMap<>();
            r.put("content", Collections.singletonList(content)); r.put("isError",true);
            return respond(id, r);
        }
    }

    private String respond(Object id, Object result) {
        Map<String,Object> r = new LinkedHashMap<>();
        r.put("jsonrpc","2.0"); r.put("id",id); r.put("result",result);
        return JsonSerializer.toJson(r);
    }
    private String error(Object id, int code, String msg) {
        Map<String,Object> e = new LinkedHashMap<>();
        e.put("code",code); e.put("message",msg);
        Map<String,Object> r = new LinkedHashMap<>();
        r.put("jsonrpc","2.0"); r.put("id",id); r.put("error",e);
        return JsonSerializer.toJson(r);
    }
}
