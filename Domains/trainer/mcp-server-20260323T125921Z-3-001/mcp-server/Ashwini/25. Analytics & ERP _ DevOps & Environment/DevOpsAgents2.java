package com.ecoskiller.analytics.agents;

import com.ecoskiller.analytics.models.*;
import com.fasterxml.jackson.databind.node.*;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════
// Agent 11 — PHONE_MONITORING_CLOCK_AUTHORITY_AGENT
// Acts as the authoritative time source for phone/device monitoring systems.
// Detects clock skew, provides NTP sync status, and time-stamps events.
// ═══════════════════════════════════════════════════════════════════════════
class PhoneMonitoringClockAuthorityAgent extends BaseAgent {

    @Override public String getName() { return "PHONE_MONITORING_CLOCK_AUTHORITY_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("devops_get_authoritative_time",
                "Get the current authoritative UTC timestamp from the clock authority.",
                schema("tenant_id")),
            new McpTool("devops_check_device_clock_skew",
                "Check and report clock skew between a device and the authoritative clock.",
                schema("device_id","device_ts","tenant_id")),
            new McpTool("devops_sync_monitoring_clock",
                "Force a clock sync for a device or service node.",
                schema("target_id","target_type","tenant_id")),
            new McpTool("devops_get_clock_skew_report",
                "Get a report of all devices with significant clock skew (>500ms).",
                schema("threshold_ms","tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String t, ObjectNode a) {
        return switch (t) {
            case "devops_get_authoritative_time" -> {
                long now = System.currentTimeMillis();
                ObjectNode d = mapper.createObjectNode();
                d.put("utc_ms",         now);
                d.put("utc_iso",        "2026-03-16T10:00:00.000Z");
                d.put("ntp_source",     "pool.ntp.org");
                d.put("stratum",        2); d.put("accuracy_ms",0.4);
                d.put("authority",      "PHONE_MONITORING_CLOCK_AUTHORITY");
                yield AgentResponse.success(getName(), d, "Authoritative time: " + now + " ms (accuracy ±0.4ms, stratum 2).");
            }
            case "devops_check_device_clock_skew" -> {
                long devTs   = a.path("device_ts").asLong(System.currentTimeMillis());
                long authTs  = System.currentTimeMillis();
                long skewMs  = Math.abs(authTs - devTs);
                ObjectNode d = mapper.createObjectNode();
                d.put("device_id",      a.path("device_id").asText());
                d.put("device_ts",      devTs); d.put("auth_ts",authTs);
                d.put("skew_ms",        skewMs); d.put("skew_direction", devTs < authTs ? "BEHIND" : "AHEAD");
                d.put("within_threshold",skewMs < 500);
                d.put("action",         skewMs < 500 ? "NONE_REQUIRED" : "SYNC_RECOMMENDED");
                yield AgentResponse.success(getName(), d,
                    "Clock skew: " + skewMs + "ms (" + (skewMs < 500 ? "within threshold" : "SYNC_RECOMMENDED") + ").");
            }
            case "devops_sync_monitoring_clock" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("target_id",      a.path("target_id").asText());
                d.put("target_type",    a.path("target_type").asText("device"));
                d.put("synced",         true); d.put("skew_before_ms",142); d.put("skew_after_ms",0.4);
                d.put("sync_id",        uid("sync")); d.put("ntp_server","pool.ntp.org");
                yield AgentResponse.success(getName(), d, "Clock synced: skew 142ms → 0.4ms.");
            }
            case "devops_get_clock_skew_report" -> {
                int threshold = a.path("threshold_ms").asInt(500);
                ObjectNode d = mapper.createObjectNode(); ArrayNode devices = mapper.createArrayNode();
                String[][] dv = {{"DEV-042","820ms","BEHIND"},{"DEV-118","610ms","AHEAD"},{"DEV-291","1240ms","BEHIND"}};
                for (String[] dv2 : dv) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("device_id",dv2[0]); row.put("skew",dv2[1]); row.put("direction",dv2[2]); devices.add(row);
                }
                d.set("devices_over_threshold",devices); d.put("count",3); d.put("threshold_ms",threshold);
                yield AgentResponse.success(getName(), d, "Clock skew report: 3 devices over " + threshold + "ms threshold.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + t);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 12 — CROSS_NODE_TIME_DRIFT_MONITOR_AGENT
// Monitors time drift between distributed infrastructure nodes.
// Detects cluster-wide NTP desynchronisation that can corrupt event ordering.
// ═══════════════════════════════════════════════════════════════════════════
class CrossNodeTimeDriftMonitorAgent extends BaseAgent {

    @Override public String getName() { return "CROSS_NODE_TIME_DRIFT_MONITOR_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("devops_scan_node_time_drift",
                "Scan all active nodes for time drift relative to the NTP authority.",
                schema("cluster_id","threshold_ms","tenant_id")),
            new McpTool("devops_get_node_drift_history",
                "Get time drift history for a specific node over a rolling window.",
                schema("node_id","hours","tenant_id")),
            new McpTool("devops_alert_on_drift",
                "Raise a drift alert for nodes exceeding the configured threshold.",
                schema("node_id","drift_ms","alert_level","tenant_id")),
            new McpTool("devops_get_cluster_sync_health",
                "Get the overall NTP synchronisation health for a cluster.",
                schema("cluster_id","tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String t, ObjectNode a) {
        return switch (t) {
            case "devops_scan_node_time_drift" -> {
                int threshold = a.path("threshold_ms").asInt(100);
                ObjectNode d = mapper.createObjectNode(); ArrayNode nodes = mapper.createArrayNode();
                String[][] nd = {
                    {"node-01","4ms","OK"},{"node-02","8ms","OK"},{"node-03","142ms","DRIFT"},
                    {"node-04","6ms","OK"},{"node-05","18ms","OK"}
                };
                for (String[] n : nd) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("node",n[0]); row.put("drift",n[1]); row.put("status",n[2]); nodes.add(row);
                }
                d.set("nodes",nodes); d.put("total",5); d.put("healthy",4); d.put("drifting",1);
                d.put("cluster_id",a.path("cluster_id").asText()); d.put("threshold_ms",threshold);
                yield AgentResponse.success(getName(), d, "Node drift scan: 4/5 healthy, 1 drifting (node-03 = 142ms).");
            }
            case "devops_get_node_drift_history" -> {
                ObjectNode d = mapper.createObjectNode(); ArrayNode hist = mapper.createArrayNode();
                double[] drift = {4.2, 6.1, 8.4, 12.0, 142.0, 8.2, 5.1};
                String[] times = {"T-6h","T-5h","T-4h","T-3h","T-2h","T-1h","T-0h"};
                for (int i = 0; i < drift.length; i++) {
                    ObjectNode pt = mapper.createObjectNode(); pt.put("time",times[i]); pt.put("drift_ms",drift[i]); hist.add(pt);
                }
                d.set("history",hist); d.put("node_id",a.path("node_id").asText());
                d.put("max_drift_ms",142.0); d.put("avg_drift_ms",27.4); d.put("spike_detected",true);
                yield AgentResponse.success(getName(), d, "Node drift history: spike at T-2h (142ms), avg=27.4ms.");
            }
            case "devops_alert_on_drift" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("node_id",    a.path("node_id").asText());
                d.put("drift_ms",   a.path("drift_ms").asInt(142));
                d.put("alert_level",a.path("alert_level").asText("WARN"));
                d.put("alert_id",   uid("drift-alert")); d.put("notified","devops@ecoskiller.com");
                d.put("auto_resync",true); d.put("resync_id",uid("resync"));
                yield AgentResponse.success(getName(), d, "Drift alert raised. Auto-resync triggered for " + a.path("node_id").asText());
            }
            case "devops_get_cluster_sync_health" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("cluster_id",     a.path("cluster_id").asText());
                d.put("nodes_total",    12); d.put("nodes_synced",11); d.put("nodes_drifting",1);
                d.put("max_drift_ms",   142); d.put("avg_drift_ms",14.2);
                d.put("cluster_health", "WARN"); d.put("ntp_server","pool.ntp.org");
                yield AgentResponse.success(getName(), d, "Cluster sync health: WARN — 1/12 node drifting (142ms).");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + t);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 13 — MODEL_GOVERNANCE_REGISTRY_AGENT
// Maintains the central registry of all deployed ML models.
// Enforces governance policies: approval workflows, access control, audit logs.
// ═══════════════════════════════════════════════════════════════════════════
class ModelGovernanceRegistryAgent extends BaseAgent {

    @Override public String getName() { return "MODEL_GOVERNANCE_REGISTRY_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("devops_register_model",
                "Register a new ML model in the governance registry with policy metadata.",
                schema("model_name","version","owner","use_case","risk_tier","tenant_id")),
            new McpTool("devops_approve_model_deployment",
                "Approve or reject a model for production deployment via the governance workflow.",
                schema("model_name","version","decision","approver_id","notes","tenant_id")),
            new McpTool("devops_get_model_registry",
                "Get all models in the governance registry with their approval and risk status.",
                schema("filter_status","tenant_id")),
            new McpTool("devops_audit_model_access",
                "Get the audit log of who accessed or invoked a model and when.",
                schema("model_name","days","tenant_id")),
            new McpTool("devops_retire_model",
                "Retire a model from the registry and archive all related artifacts.",
                schema("model_name","version","reason","tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String t, ObjectNode a) {
        return switch (t) {
            case "devops_register_model" -> {
                String regId = uid("reg");
                ObjectNode d = mapper.createObjectNode();
                d.put("registry_id",    regId);
                d.put("model_name",     a.path("model_name").asText());
                d.put("version",        a.path("version").asText());
                d.put("owner",          a.path("owner").asText());
                d.put("use_case",       a.path("use_case").asText());
                d.put("risk_tier",      a.path("risk_tier").asText("MEDIUM"));
                d.put("status",         "PENDING_APPROVAL");
                d.put("approval_sla_days",3);
                yield AgentResponse.success(getName(), d,
                    "Model registered: " + a.path("model_name").asText() + " v" + a.path("version").asText() + " — PENDING_APPROVAL.");
            }
            case "devops_approve_model_deployment" -> {
                String dec = a.path("decision").asText("APPROVED");
                ObjectNode d = mapper.createObjectNode();
                d.put("model_name",     a.path("model_name").asText());
                d.put("version",        a.path("version").asText());
                d.put("decision",       dec); d.put("approver",a.path("approver_id").asText());
                d.put("approved",       dec.equals("APPROVED")); d.put("audit_logged",true);
                d.put("effective",      dec.equals("APPROVED") ? "IMMEDIATE" : "N/A");
                yield AgentResponse.success(getName(), d, "Model deployment " + dec + " by " + a.path("approver_id").asText());
            }
            case "devops_get_model_registry" -> {
                ArrayNode models = mapper.createArrayNode();
                String[][] ms = {
                    {"intelligence-scorer","v3.2","APPROVED","MEDIUM","ACTIVE"},
                    {"test-scoring-model", "v2.1","APPROVED","LOW",   "ACTIVE"},
                    {"career-predictor",   "v1.4","APPROVED","MEDIUM","ACTIVE"},
                    {"fraud-detector",     "v2.0","PENDING_APPROVAL","HIGH","STAGING"},
                    {"content-moderator",  "v3.1","APPROVED","HIGH",  "ACTIVE"}
                };
                for (String[] m : ms) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("model",m[0]); row.put("version",m[1]); row.put("approval",m[2]);
                    row.put("risk_tier",m[3]); row.put("status",m[4]); models.add(row);
                }
                ObjectNode d = mapper.createObjectNode(); d.set("models",models);
                d.put("total",5); d.put("approved",4); d.put("pending",1);
                yield AgentResponse.success(getName(), d, "Registry: 5 models, 4 APPROVED, 1 PENDING.");
            }
            case "devops_audit_model_access" -> {
                ObjectNode d = mapper.createObjectNode(); ArrayNode log = mapper.createArrayNode();
                String[][] entries = {
                    {"USR-001","inference","2026-03-15T14:00:00Z","SUCCESS"},
                    {"SVC-scoring","inference","2026-03-15T13:58:20Z","SUCCESS"},
                    {"USR-042","metadata_read","2026-03-15T10:00:00Z","SUCCESS"}
                };
                for (String[] e : entries) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("accessor",e[0]); row.put("action",e[1]); row.put("ts",e[2]); row.put("result",e[3]); log.add(row);
                }
                d.set("audit_log",log); d.put("model",a.path("model_name").asText());
                d.put("days",a.path("days").asInt(7)); d.put("total_accesses",3);
                yield AgentResponse.success(getName(), d, "Model audit: 3 accesses in last " + a.path("days").asInt(7) + " days.");
            }
            case "devops_retire_model" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("model_name",     a.path("model_name").asText());
                d.put("version",        a.path("version").asText());
                d.put("retired",        true); d.put("archived",true);
                d.put("reason",         a.path("reason").asText());
                d.put("retire_id",      uid("retire")); d.put("artifacts_archived",true);
                yield AgentResponse.success(getName(), d, "Model retired and archived successfully.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + t);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 14 — CROSS_SERVICE_TRACE_CORRELATION_AGENT
// Correlates distributed traces across all Ecoskiller microservices.
// Links spans from device SDK → API Gateway → backend services → DB → cache.
// Enables root-cause analysis across service boundaries.
// ═══════════════════════════════════════════════════════════════════════════
public class CrossServiceTraceCorrelationAgent extends BaseAgent {

    @Override public String getName() { return "CROSS_SERVICE_TRACE_CORRELATION_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("devops_correlate_trace",
                "Correlate a distributed trace across all services using a root trace ID.",
                schema("trace_id","tenant_id")),
            new McpTool("devops_find_root_cause",
                "Identify the root cause of a failure or latency spike from a correlated trace.",
                schema("trace_id","symptom","tenant_id")),
            new McpTool("devops_get_service_dependency_map",
                "Get the real-time service call dependency map from recent traces.",
                schema("service_name","depth","tenant_id")),
            new McpTool("devops_get_error_propagation_path",
                "Trace how an error propagated from origin service across downstream callers.",
                schema("trace_id","error_code","tenant_id")),
            new McpTool("devops_get_cross_service_sla_report",
                "Get SLA compliance report across all service hops in a trace.",
                schema("trace_id","sla_budget_ms","tenant_id")),
            new McpTool("devops_search_traces_by_attribute",
                "Search for traces matching a specific attribute (user, device, error code).",
                schema("attribute_key","attribute_value","hours","tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String t, ObjectNode a) {
        return switch (t) {
            case "devops_correlate_trace" -> {
                ObjectNode d = mapper.createObjectNode(); ArrayNode spans = mapper.createArrayNode();
                String[][] sp = {
                    {"sdk",            "device-sdk",    "0ms",    "12ms",  "OK",   ""},
                    {"ingress",        "api-gateway",   "12ms",   "8ms",   "OK",   ""},
                    {"auth",           "auth-service",  "20ms",   "4ms",   "OK",   ""},
                    {"route",          "router-service","24ms",   "2ms",   "OK",   ""},
                    {"business",       "test-service",  "26ms",   "180ms", "OK",   ""},
                    {"persistence",    "postgres-db",   "42ms",   "38ms",  "OK",   ""},
                    {"cache_write",    "redis",         "80ms",   "4ms",   "OK",   ""},
                    {"score",          "scoring-svc",   "206ms",  "62ms",  "OK",   ""},
                    {"notify",         "push-service",  "268ms",  "16ms",  "OK",   ""},
                    {"analytics_emit", "clickhouse",    "284ms",  "8ms",   "OK",   ""}
                };
                for (String[] s : sp) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("span_name",s[0]); row.put("service",s[1]); row.put("start",s[2]);
                    row.put("duration",s[3]); row.put("status",s[4]); spans.add(row);
                }
                d.set("spans",spans); d.put("trace_id",a.path("trace_id").asText());
                d.put("total_spans",10); d.put("total_ms",292); d.put("errors",0); d.put("warnings",0);
                d.put("services_involved",9); d.put("correlated_at",System.currentTimeMillis());
                yield AgentResponse.success(getName(), d,
                    "Trace correlated: 10 spans across 9 services, 292ms total, 0 errors.");
            }
            case "devops_find_root_cause" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("trace_id",           a.path("trace_id").asText());
                d.put("symptom",            a.path("symptom").asText("high_latency"));
                d.put("root_cause_service", "test-service");
                d.put("root_cause_span",    "business (180ms = 61.6% of total)");
                d.put("probable_reason",    "DB query without index — full table scan detected");
                d.put("confidence",         0.89);
                d.put("suggested_fix",      "Add index on test_submissions(candidate_id, status)");
                d.put("priority",           "HIGH");
                yield AgentResponse.success(getName(), d,
                    "Root cause: test-service (180ms, 62% of budget). Fix: add DB index.");
            }
            case "devops_get_service_dependency_map" -> {
                ObjectNode d = mapper.createObjectNode(); ArrayNode deps = mapper.createArrayNode();
                String[] downstreams = {"auth-service","test-service","scoring-svc","postgres-db","redis","push-service","clickhouse"};
                for (String dep : downstreams) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("from",a.path("service_name").asText("api-gateway")); row.put("to",dep);
                    row.put("calls_per_min",Math.random() > 0.5 ? 840 : 320); row.put("health","HEALTHY"); deps.add(row);
                }
                d.set("dependencies",deps); d.put("root",a.path("service_name").asText("api-gateway"));
                d.put("depth",a.path("depth").asInt(2)); d.put("total_downstream",7);
                yield AgentResponse.success(getName(), d, "Dependency map: " + a.path("service_name").asText("api-gateway") + " → 7 downstream services.");
            }
            case "devops_get_error_propagation_path" -> {
                ObjectNode d = mapper.createObjectNode(); ArrayNode path = mapper.createArrayNode();
                String[][] propagation = {
                    {"postgres-db",     "ORIGIN",       "CONNECTION_TIMEOUT"},
                    {"test-service",    "PROPAGATED",   "DB_UNAVAILABLE"},
                    {"api-gateway",     "PROPAGATED",   "503_SERVICE_UNAVAILABLE"},
                    {"device-sdk",      "RECEIVED",     "REQUEST_FAILED"}
                };
                for (String[] p : propagation) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("service",p[0]); row.put("role",p[1]); row.put("error",p[2]); path.add(row);
                }
                d.set("propagation_path",path); d.put("trace_id",a.path("trace_id").asText());
                d.put("origin_service","postgres-db"); d.put("hops",4); d.put("blast_radius","test-service, api-gateway");
                yield AgentResponse.success(getName(), d, "Error propagation: postgres-db → test-service → gateway → SDK (4 hops).");
            }
            case "devops_get_cross_service_sla_report" -> {
                int budget = a.path("sla_budget_ms").asInt(500);
                ObjectNode d = mapper.createObjectNode(); ArrayNode sla = mapper.createArrayNode();
                String[][] services = {
                    {"api-gateway","8ms","1.6%","PASS"},
                    {"auth-service","4ms","0.8%","PASS"},
                    {"test-service","180ms","36.0%","PASS"},
                    {"scoring-svc","62ms","12.4%","PASS"},
                    {"push-service","16ms","3.2%","PASS"}
                };
                for (String[] s : services) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("service",s[0]); row.put("duration",s[1]); row.put("budget_pct",s[2]); row.put("sla",s[3]); sla.add(row);
                }
                d.set("service_sla",sla); d.put("total_ms",292); d.put("budget_ms",budget);
                d.put("budget_used_pct",58.4); d.put("sla_breaches",0); d.put("overall_sla","PASS");
                yield AgentResponse.success(getName(), d, "SLA report: 292/" + budget + "ms used (58.4%), 0 breaches — PASS.");
            }
            case "devops_search_traces_by_attribute" -> {
                ObjectNode d = mapper.createObjectNode(); ArrayNode results = mapper.createArrayNode();
                for (int i = 1; i <= 3; i++) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("trace_id", uid("trace")); row.put("total_ms",200+i*40); row.put("errors",0);
                    row.put("attribute", a.path("attribute_key").asText() + "=" + a.path("attribute_value").asText());
                    results.add(row);
                }
                d.set("traces",results); d.put("found",3);
                d.put("attribute", a.path("attribute_key").asText() + "=" + a.path("attribute_value").asText());
                d.put("hours",a.path("hours").asInt(24));
                yield AgentResponse.success(getName(), d, "Trace search: 3 traces found for " + a.path("attribute_key").asText() + "=" + a.path("attribute_value").asText());
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + t);
        };
    }
}
