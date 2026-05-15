package io.ecoskiller.prosody.security;

import io.ecoskiller.prosody.config.ServerConfig;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

// ═══════════════════════════════════════════════════════════════════════════════
// RATE LIMITER
// ═══════════════════════════════════════════════════════════════════════════════
/**
 * Token-bucket rate limiter for MCP requests.
 *
 * Limits per client key (derived from JWT prefix or IP):
 * - Max requests per second (configurable, default 100)
 * - Max requests per minute (configurable, default 1000)
 * - JID ban list (blocked indefinitely or for a duration)
 */
class RateLimiterState {
    final AtomicInteger requestsThisSecond = new AtomicInteger(0);
    final AtomicInteger requestsThisMinute = new AtomicInteger(0);
    volatile long secondBucket = System.currentTimeMillis() / 1000;
    volatile long minuteBucket = System.currentTimeMillis() / 60000;
}

public class RateLimiter {
    private static final Logger LOGGER = Logger.getLogger(RateLimiter.class.getName());

    private final int maxPerSecond;
    private final int maxPerMinute;
    private final ConcurrentHashMap<String, RateLimiterState> states = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> bannedJids = new ConcurrentHashMap<>();

    public RateLimiter(ServerConfig config) {
        this.maxPerSecond = config.getRateLimitPerSecond();
        this.maxPerMinute = config.getRateLimitPerMinute();
    }

    public boolean allowRequest(String clientKey) {
        // Check ban list
        Long banExpiry = bannedJids.get(clientKey);
        if (banExpiry != null) {
            if (banExpiry == 0 || System.currentTimeMillis() < banExpiry) {
                return false; // still banned
            } else {
                bannedJids.remove(clientKey); // ban expired
            }
        }

        RateLimiterState state = states.computeIfAbsent(clientKey, k -> new RateLimiterState());

        long nowSecond = System.currentTimeMillis() / 1000;
        long nowMinute = System.currentTimeMillis() / 60000;

        // Reset per-second counter if bucket changed
        if (state.secondBucket != nowSecond) {
            state.requestsThisSecond.set(0);
            state.secondBucket = nowSecond;
        }
        // Reset per-minute counter if bucket changed
        if (state.minuteBucket != nowMinute) {
            state.requestsThisMinute.set(0);
            state.minuteBucket = nowMinute;
        }

        int perSecond = state.requestsThisSecond.incrementAndGet();
        int perMinute = state.requestsThisMinute.incrementAndGet();

        return perSecond <= maxPerSecond && perMinute <= maxPerMinute;
    }

    public void banJid(String jid, int durationSeconds) {
        long expiry = durationSeconds == 0 ? 0 : System.currentTimeMillis() + (durationSeconds * 1000L);
        bannedJids.put(jid, expiry);
        LOGGER.warning("JID banned: " + jid + " duration=" + durationSeconds + "s");
    }

    public void unbanJid(String jid) {
        bannedJids.remove(jid);
        LOGGER.info("JID unbanned: " + jid);
    }

    public boolean isBanned(String jid) {
        Long expiry = bannedJids.get(jid);
        if (expiry == null) return false;
        if (expiry == 0 || System.currentTimeMillis() < expiry) return true;
        bannedJids.remove(jid);
        return false;
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// REQUEST VALIDATOR
// ═══════════════════════════════════════════════════════════════════════════════
/**
 * Validates MCP tool call arguments against defined rules.
 */
class RequestValidatorImpl {
    public List<String> validate(String toolName, com.fasterxml.jackson.databind.JsonNode args) {
        List<String> errors = new ArrayList<>();
        // Check for oversized payloads (prevent DoS)
        if (args != null) {
            String argsStr = args.toString();
            if (argsStr.length() > 65536) { // 64KB max
                errors.add("Request payload exceeds 64KB limit");
            }
        }
        return errors;
    }
}

// Make RequestValidator a public class in the same package
