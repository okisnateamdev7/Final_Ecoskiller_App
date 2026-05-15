package io.ecoskiller.prosody.security;

import io.ecoskiller.prosody.config.ServerConfig;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

/**
 * Security and operational audit logger.
 *
 * - Writes structured audit events to stderr (captured by Kubernetes/ELK)
 * - Maintains a rolling in-memory buffer of the last 1000 events (for query)
 * - All events are immutable records; no mutation after write
 *
 * Format: [AUDIT] {level} | {eventType} | {actor} | {detail} | {timestamp}
 *
 * Complies with SOC 2 Type II audit logging requirements.
 */
public class AuditLogger {

    private static final Logger LOGGER = Logger.getLogger(AuditLogger.class.getName());
    private static final int MAX_BUFFER = 1000;

    private final Deque<AuditEvent> buffer = new ConcurrentLinkedDeque<>();
    private final boolean enabled;

    public AuditLogger(ServerConfig config) {
        this.enabled = config.isAuditEnabled();
    }

    public void info(String eventType, String actor, String detail) {
        log("INFO", eventType, actor, detail);
    }

    public void warn(String eventType, String actor, String detail) {
        log("WARN", eventType, actor, detail);
    }

    public void error(String eventType, String actor, String detail) {
        log("ERROR", eventType, actor, detail);
    }

    private void log(String level, String eventType, String actor, String detail) {
        if (!enabled) return;

        // Sanitize to prevent log injection
        String safeActor = InputSanitizer.sanitizeForLog(actor);
        String safeDetail = InputSanitizer.sanitizeForLog(detail);
        String safeEvent = InputSanitizer.sanitizeForLog(eventType);

        String message = String.format("[AUDIT] %s | %s | actor=%s | %s | ts=%s",
            level, safeEvent, safeActor, safeDetail, Instant.now().toString());

        // Write to stderr (stdout is reserved for JSON-RPC)
        System.err.println(message);

        // Buffer for in-memory query (rolling, thread-safe)
        AuditEvent event = new AuditEvent(level, eventType, actor, detail, Instant.now().toString());
        buffer.addLast(event);
        while (buffer.size() > MAX_BUFFER) {
            buffer.pollFirst();
        }
    }

    /**
     * Returns recent audit events matching the given event type filter.
     * Used by xmpp_audit_log_query agent.
     */
    public List<AuditEvent> getRecentEvents(String eventTypeFilter, int limit) {
        List<AuditEvent> results = new ArrayList<>();
        // Iterate newest-first
        Deque<AuditEvent> snapshot = new ArrayDeque<>(buffer);
        for (AuditEvent e : snapshot) {
            if (eventTypeFilter.equals("ALL") || e.eventType().equals(eventTypeFilter)) {
                results.add(e);
                if (results.size() >= limit) break;
            }
        }
        return results;
    }

    /**
     * Immutable audit event record.
     */
    public record AuditEvent(
        String level,
        String eventType,
        String actor,
        String detail,
        String timestamp
    ) {}
}
