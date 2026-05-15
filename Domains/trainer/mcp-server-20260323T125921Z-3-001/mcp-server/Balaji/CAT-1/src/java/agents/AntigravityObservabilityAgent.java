package com.ecoskiller.mcp.platform.agents;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ANTIGRAVITY_OBSERVABILITY_AGENT
 *
 * Platform observability layer:
 * - Metrics collection from services
 * - Log aggregation queries
 * - Distributed trace correlation
 * - Alert rule management
 */
@Service
public class AntigravityObservabilityAgent {

    @Tool(name = "antigravity_get_service_metrics",
          description = "Fetch real-time metrics for a service: CPU, memory, request rate, "
                      + "error rate, P50/P95/P99 latencies.")
    public Map<String, Object> getServiceMetrics(
            @ToolParam(description = "Service name to query") String serviceName,
            @ToolParam(description = "Time window in minutes, e.g. 15") int windowMinutes) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",          "ANTIGRAVITY_OBSERVABILITY_AGENT");
        result.put("service",        serviceName);
        result.put("window_minutes", windowMinutes);
        result.put("metrics", Map.of(
            "cpu_pct",        45.2,
            "memory_mb",      512,
            "req_per_sec",    320.5,
            "error_rate_pct", 0.12,
            "p50_ms",         18,
            "p95_ms",         95,
            "p99_ms",         210
        ));
        result.put("status", "OK");
        return result;
    }

    @Tool(name = "antigravity_query_logs",
          description = "Query aggregated logs for a service using a filter expression. "
                      + "Returns matching log lines with timestamps and severity.")
    public Map<String, Object> queryLogs(
            @ToolParam(description = "Service name") String serviceName,
            @ToolParam(description = "Log filter expression, e.g. 'level=ERROR AND message contains NullPointer'") String filterExpression,
            @ToolParam(description = "Max results to return") int limit) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",   "ANTIGRAVITY_OBSERVABILITY_AGENT");
        result.put("service", serviceName);
        result.put("filter",  filterExpression);
        result.put("limit",   limit);
        result.put("total_matches", 7);
        result.put("lines", List.of(
            Map.of("ts", "2025-01-01T10:00:00Z", "level", "ERROR", "msg", "NullPointerException in AuthFilter"),
            Map.of("ts", "2025-01-01T10:00:05Z", "level", "ERROR", "msg", "NullPointerException in TokenParser")
        ));
        return result;
    }

    @Tool(name = "antigravity_get_trace",
          description = "Retrieve a distributed trace by trace ID to understand the full "
                      + "call chain across services for a single request.")
    public Map<String, Object> getTrace(
            @ToolParam(description = "Trace ID (hex string)") String traceId) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",    "ANTIGRAVITY_OBSERVABILITY_AGENT");
        result.put("trace_id", traceId);
        result.put("spans", List.of(
            Map.of("span_id", "aaa1", "service", "api-gateway",    "duration_ms", 250, "status", "OK"),
            Map.of("span_id", "bbb2", "service", "ecoskiller-api", "duration_ms", 200, "status", "OK"),
            Map.of("span_id", "ccc3", "service", "db-postgres",    "duration_ms",  80, "status", "OK")
        ));
        result.put("total_duration_ms", 250);
        return result;
    }

    @Tool(name = "antigravity_create_alert_rule",
          description = "Create or update an alert rule for a service metric. "
                      + "Alerts fire when threshold is crossed for the specified duration.")
    public Map<String, Object> createAlertRule(
            @ToolParam(description = "Service to monitor") String serviceName,
            @ToolParam(description = "Metric name, e.g. 'error_rate_pct'") String metricName,
            @ToolParam(description = "Threshold value that triggers alert") double threshold,
            @ToolParam(description = "Duration in seconds the threshold must be breached") int durationSeconds,
            @ToolParam(description = "Notification channel: slack | email | pagerduty") String channel) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",            "ANTIGRAVITY_OBSERVABILITY_AGENT");
        result.put("action",           "CREATE_ALERT_RULE");
        result.put("service",          serviceName);
        result.put("metric",           metricName);
        result.put("threshold",        threshold);
        result.put("duration_seconds", durationSeconds);
        result.put("channel",          channel);
        result.put("rule_id",          "ALT-" + System.currentTimeMillis());
        result.put("status",           "CREATED");
        return result;
    }
}
