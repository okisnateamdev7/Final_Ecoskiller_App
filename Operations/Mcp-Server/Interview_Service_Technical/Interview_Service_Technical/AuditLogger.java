package com.ecoskiller.mcp.interview.security;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.logging.Logger;

/**
 * Immutable Audit Logger
 *
 * Logs all tool calls, security events, and outcomes.
 * Format: JSON structured log → Grafana Loki / ClickHouse
 *
 * Per spec:
 * - All recruiter actions logged: scheduled, started, paused, resumed,
 *   completed, submitted feedback, requested deletion
 * - Each log includes: recruiter_id, action, timestamp, details, IP
 * - Retention: 7 years (GST compliance)
 *
 * Production: write to ClickHouse via HTTP ingest endpoint.
 */
public class AuditLogger {

    private static final Logger LOG = Logger.getLogger(AuditLogger.class.getName());

    public void log(String eventType, String tool, String tenantId, JsonNode args) {
        // Sanitize: never log auth_token
        String safeArgs = "{}";
        if (args != null) {
            safeArgs = sanitize(args.toString());
        }

        String entry = String.format(
            "{\"event\":\"%s\",\"tool\":\"%s\",\"tenant_id\":\"%s\",\"timestamp\":\"%s\",\"args\":%s}",
            eventType, tool, tenantId, Instant.now().toString(), safeArgs
        );

        // In production: async write to ClickHouse + stderr for Loki
        System.err.println("[AUDIT] " + entry);
        LOG.info("AUDIT: " + eventType + " tool=" + tool + " tenant=" + tenantId);
    }

    /**
     * Remove sensitive fields from serialized args before logging.
     */
    private String sanitize(String args) {
        // Redact auth_token, media_token, encryption_key
        return args
            .replaceAll("\"auth_token\":\"[^\"]*\"", "\"auth_token\":\"[REDACTED]\"")
            .replaceAll("\"media_token\":\"[^\"]*\"", "\"media_token\":\"[REDACTED]\"")
            .replaceAll("\"encryption_key\":\"[^\"]*\"", "\"encryption_key\":\"[REDACTED]\"")
            .replaceAll("\"password\":\"[^\"]*\"", "\"password\":\"[REDACTED]\"");
    }
}
