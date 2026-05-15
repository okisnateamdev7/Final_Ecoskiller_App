package com.ecoskiller.tie.mcp.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple sliding-window rate limiter (per client ID).
 * Thread-safe. No external dependencies.
 *
 * Default: 100 requests per 60 seconds per client.
 * Configurable via constructor or ENV vars:
 *   TIE_RATE_LIMIT_MAX  (default: 100)
 *   TIE_RATE_LIMIT_MS   (default: 60000)
 */
public class RateLimiter {

    private static final int  DEFAULT_MAX = parseInt(System.getenv("TIE_RATE_LIMIT_MAX"), 100);
    private static final long DEFAULT_MS  = parseLong(System.getenv("TIE_RATE_LIMIT_MS"), 60_000L);

    private final int  maxRequests;
    private final long windowMs;

    private final Map<String, ClientBucket> buckets = new ConcurrentHashMap<>();

    public RateLimiter(int maxRequests, long windowMs) {
        this.maxRequests = maxRequests;
        this.windowMs    = windowMs;
    }

    public RateLimiter() {
        this(DEFAULT_MAX, DEFAULT_MS);
    }

    public boolean allow(String clientId) {
        ClientBucket bucket = buckets.computeIfAbsent(clientId, k -> new ClientBucket());
        return bucket.tryConsume(maxRequests, windowMs);
    }

    // ── Inner State ───────────────────────────────────────────────────────────

    private static class ClientBucket {
        private final AtomicInteger count     = new AtomicInteger(0);
        private final AtomicLong    windowStart = new AtomicLong(System.currentTimeMillis());

        synchronized boolean tryConsume(int max, long windowMs) {
            long now = System.currentTimeMillis();
            if (now - windowStart.get() >= windowMs) {
                windowStart.set(now);
                count.set(0);
            }
            int current = count.incrementAndGet();
            return current <= max;
        }
    }

    private static int parseInt(String s, int def) {
        try { return s != null ? Integer.parseInt(s.trim()) : def; } catch (NumberFormatException e) { return def; }
    }

    private static long parseLong(String s, long def) {
        try { return s != null ? Long.parseLong(s.trim()) : def; } catch (NumberFormatException e) { return def; }
    }
}
