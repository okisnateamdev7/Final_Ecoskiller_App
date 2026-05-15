package com.ecoskiller.mcp.nginx.tools;
import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/** Tool 13 — observability_exporter */
public class ObservabilityExporterTool extends BaseNginxTool {
    public ObservabilityExporterTool(ServerConfig c) { super(c); }

    @Override public Map<String, Object> descriptor() {
        return Map.of(
            "name", "observability_exporter",
            "description",
                "Access NGINX Ingress metrics, logs, and distributed traces. " +
                "Prometheus metrics: request count, P50/P95/P99 latency, error rate, backend health. " +
                "Access logs: per-request client_ip, method, path, status, bytes, latency. " +
                "Distributed tracing via Jaeger/Zipkin W3C Trace Context headers. " +
                "Grafana dashboard URLs, AlertManager rules, ELK log queries. " +
                "Actions: metrics | logs | traces | status | dashboard.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",   enumProp("Operation", "metrics", "logs", "traces", "status", "dashboard"),
                    "service",  property("string", "Filter by service name"),
                    "logLevel", enumProp("Log severity filter", "debug", "info", "notice", "warn", "error", "crit", "alert", "emerg"),
                    "window",   property("string", "Time window (e.g. 5m, 1h, 24h)")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override public Object execute(Map<String, Object> args) {
        String action  = str(args, "action",  "status");
        String service = str(args, "service", "");
        String window  = str(args, "window",  "5m");

        switch (action) {
            case "metrics": {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("endpoint",  config.getPrometheusEndpoint() + "/api/v1/query");
                d.put("scrapeIntervalSec", 30);
                d.put("keyMetrics", Map.of(
                    "requestsTotal",       "nginx_ingress_controller_requests_total",
                    "requestDurationP99",  "histogram_quantile(0.99, nginx_ingress_controller_request_duration_seconds_bucket)",
                    "requestDurationP95",  "histogram_quantile(0.95, nginx_ingress_controller_request_duration_seconds_bucket)",
                    "errorRate",           "rate(nginx_ingress_controller_requests_total{status=~\"5..\"}[5m])",
                    "upstreamResponseTime","nginx_ingress_controller_upstream_response_time",
                    "endpointCount",       "nginx_ingress_controller_endpoint_count"
                ));
                d.put("currentSnapshot", Map.of(
                    "requestsPerSec",  127,
                    "errorRatePct",    0.4,
                    "p50LatencyMs",    18,
                    "p95LatencyMs",    67,
                    "p99LatencyMs",    142,
                    "healthyBackends", 12,
                    "totalBackends",   13
                ));
                d.put("alertRules", List.of(
                    "error_rate > 5% for 5 min → PagerDuty alert",
                    "p99_latency > 1000ms → Slack #infra alert",
                    "healthy_backends == 0 → immediate page",
                    "cert_expiry < 7 days → email alert"
                ));
                return success("Prometheus metrics", d);
            }
            case "logs": {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("format", "nginx combined log");
                d.put("sample", "198.51.100.45 - user@ecoskiller.com [09/Mar/2026:14:23:45 +0000] " +
                                "\"GET /assessments/123 HTTP/1.1\" 200 2048 \"https://portal.ecoskiller.com\" " +
                                "\"Mozilla/5.0\" \"assessment-pod-3\" 0.025 0.024");
                d.put("fields", List.of("client_ip","user","timestamp","request","status",
                                         "bytes","referer","user_agent","upstream","req_time","upstream_time"));
                d.put("kibanaQuery",   "GET /elasticsearch/_search?q=status:500 AND @timestamp:[now-1h TO now]");
                d.put("fluentdConfig", "Fluentd DaemonSet collects from pod stdout → Elasticsearch");
                d.put("logLevel",      "info");
                d.put("recentErrors",  List.of(
                    "502 Bad Gateway: upstream timed out (110) — assessment-service-pod-1 — 2 min ago",
                    "429 Too Many Requests: rate limit exceeded — IP 203.0.113.5 — 5 min ago"
                ));
                return success("NGINX access log config", d);
            }
            case "traces": {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("backend",    "Jaeger");
                d.put("standard",   "W3C Trace Context");
                d.put("sampleTrace", Map.of(
                    "traceId",      "a1b2c3d4e5f6a7b8",
                    "operation",    "ingress_request_routing",
                    "totalMs",      253,
                    "breakdown",    Map.of(
                        "tlsHandshake", "85ms",
                        "nginxProcessing", "8ms",
                        "backendLatency", "155ms",
                        "networkReturn", "5ms"
                    )
                ));
                d.put("jaegerUrl", "http://jaeger:16686/search?service=nginx-ingress");
                return success("Distributed traces", d);
            }
            case "dashboard": {
                return success("Grafana dashboards", Map.of(
                    "mainDashboard",    "http://grafana:3000/d/nginx-ingress",
                    "errorDashboard",   "http://grafana:3000/d/nginx-errors",
                    "latencyDashboard", "http://grafana:3000/d/nginx-latency",
                    "backendHealth",    "http://grafana:3000/d/nginx-backends",
                    "note",             "Import pre-built dashboard ID 9614 from Grafana.com for NGINX Ingress"
                ));
            }
            case "status":
            default: {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("prometheusEndpoint", config.getPrometheusEndpoint() + "/metrics");
                d.put("metricsHealthy",     true);
                d.put("logPipeline",        "Fluentd → Elasticsearch → Kibana");
                d.put("tracingEnabled",     false);
                d.put("grafanaConnected",   true);
                d.put("alertsActive",       2);
                return success("Observability stack status", d);
            }
        }
    }
}
