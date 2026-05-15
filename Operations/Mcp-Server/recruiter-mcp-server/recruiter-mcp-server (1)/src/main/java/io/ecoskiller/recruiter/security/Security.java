package io.ecoskiller.recruiter.security;

import io.ecoskiller.recruiter.config.ServerConfig;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;

// ═══════════════════════════════════════════════════════════════════════════════
// RATE LIMITER — token bucket per client key
// ═══════════════════════════════════════════════════════════════════════════════
class BucketState {
    final AtomicInteger sec = new AtomicInteger();
    final AtomicInteger min = new AtomicInteger();
    volatile long secBucket = System.currentTimeMillis() / 1000;
    volatile long minBucket = System.currentTimeMillis() / 60_000;
}

public class RateLimiter {
    private final int maxSec, maxMin;
    private final ConcurrentHashMap<String, BucketState> states = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> banned = new ConcurrentHashMap<>();

    public RateLimiter(ServerConfig c) { this.maxSec = c.getRateLimitPerSec(); this.maxMin = c.getRateLimitPerMin(); }

    public boolean allow(String key) {
        Long exp = banned.get(key);
        if (exp != null) {
            if (exp == 0 || System.currentTimeMillis() < exp) return false;
            banned.remove(key);
        }
        BucketState s = states.computeIfAbsent(key, k -> new BucketState());
        long nowS = System.currentTimeMillis() / 1000;
        long nowM = System.currentTimeMillis() / 60_000;
        if (s.secBucket != nowS) { s.sec.set(0); s.secBucket = nowS; }
        if (s.minBucket != nowM) { s.min.set(0); s.minBucket = nowM; }
        return s.sec.incrementAndGet() <= maxSec && s.min.incrementAndGet() <= maxMin;
    }

    public void ban(String key, int secs) {
        banned.put(key, secs == 0 ? 0L : System.currentTimeMillis() + secs * 1000L);
    }
    public void unban(String key) { banned.remove(key); }
    public boolean isBanned(String key) {
        Long e = banned.get(key);
        if (e == null) return false;
        if (e == 0 || System.currentTimeMillis() < e) return true;
        banned.remove(key); return false;
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// AUDIT LOGGER — SOC 2 / DPDPA 2023 compliant structured event log
// ═══════════════════════════════════════════════════════════════════════════════
class AuditLoggerState {
    static final ConcurrentLinkedDeque<AuditLogger.AuditEvent> BUFFER = new ConcurrentLinkedDeque<>();
}

public class AuditLogger {
    private static final Logger LOG = Logger.getLogger(AuditLogger.class.getName());
    private static final int BUFFER_SIZE = 2000;
    private final boolean enabled;

    public AuditLogger(ServerConfig c) { this.enabled = c.isAuditEnabled(); }

    public void info(String event, String actor, String detail)  { log("INFO",  event, actor, detail); }
    public void warn(String event, String actor, String detail)  { log("WARN",  event, actor, detail); }
    public void error(String event, String actor, String detail) { log("ERROR", event, actor, detail); }

    private void log(String level, String event, String actor, String detail) {
        if (!enabled) return;
        String safeActor  = InputSanitizer.sanitizeForLog(actor);
        String safeDetail = InputSanitizer.sanitizeForLog(detail);
        String safeEvent  = InputSanitizer.sanitizeForLog(event);
        String msg = String.format("[AUDIT] %s | %s | actor=%s | %s | ts=%s",
            level, safeEvent, safeActor, safeDetail, Instant.now());
        System.err.println(msg);  // stderr only; stdout reserved for JSON-RPC
        AuditEvent e = new AuditEvent(level, event, actor, detail, Instant.now().toString());
        AuditLoggerState.BUFFER.addLast(e);
        while (AuditLoggerState.BUFFER.size() > BUFFER_SIZE) AuditLoggerState.BUFFER.pollFirst();
    }

    public List<AuditEvent> query(String eventFilter, String actorFilter, int limit) {
        List<AuditEvent> results = new ArrayList<>();
        for (AuditEvent e : new ArrayDeque<>(AuditLoggerState.BUFFER)) {
            boolean matchEvent = "ALL".equals(eventFilter) || e.eventType().equalsIgnoreCase(eventFilter);
            boolean matchActor = actorFilter == null || actorFilter.isBlank()
                || e.actor().contains(actorFilter);
            if (matchEvent && matchActor) {
                results.add(e);
                if (results.size() >= limit) break;
            }
        }
        return results;
    }

    public record AuditEvent(String level, String eventType, String actor, String detail, String timestamp) {}
}


// ═══════════════════════════════════════════════════════════════════════════════
// REQUEST VALIDATOR — payload size + null byte check
// ═══════════════════════════════════════════════════════════════════════════════
public class RequestValidator {
    private static final int MAX_PAYLOAD = 65536; // 64KB

    public boolean isValidToolName(String name) { return InputSanitizer.isValidToolName(name); }

    public List<String> validate(String toolName, JsonNode args) {
        List<String> errors = new ArrayList<>();
        if (args == null) return errors;
        String raw = args.toString();
        if (raw.length() > MAX_PAYLOAD)
            errors.add("Payload (" + raw.length() + " bytes) exceeds 64KB limit");
        if (raw.contains("\u0000"))
            errors.add("Payload contains null bytes — potential injection attack");
        return errors;
    }
}
