package com.ecoskiller.trivy.mcp.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe per-client sliding-window rate limiter.
 *
 * Defaults: 100 requests per 60 seconds per client ID.
 * Override via ENV:
 *   TRIVY_RATE_LIMIT_MAX  (integer, default 100)
 *   TRIVY_RATE_LIMIT_MS   (long,    default 60000)
 */
public class RateLimiter {

    private static final int  DEFAULT_MAX = parseInt(System.getenv("TRIVY_RATE_LIMIT_MAX"), 100);
    private static final long DEFAULT_MS  = parseLong(System.getenv("TRIVY_RATE_LIMIT_MS"), 60_000L);

    private final int  maxRequests;
    private final long windowMs;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimiter() { this(DEFAULT_MAX, DEFAULT_MS); }

    public RateLimiter(int maxRequests, long windowMs) {
        this.maxRequests = maxRequests;
        this.windowMs    = windowMs;
    }

    /** @return true if the request is within the limit for this clientId. */
    public boolean allow(String clientId) {
        return buckets.computeIfAbsent(clientId, k -> new Bucket())
                      .tryConsume(maxRequests, windowMs);
    }

    private static final class Bucket {
        private final AtomicInteger count       = new AtomicInteger(0);
        private final AtomicLong    windowStart = new AtomicLong(System.currentTimeMillis());

        synchronized boolean tryConsume(int max, long windowMs) {
            long now = System.currentTimeMillis();
            if (now - windowStart.get() >= windowMs) { windowStart.set(now); count.set(0); }
            return count.incrementAndGet() <= max;
        }
    }

    private static int parseInt(String s, int def) {
        try { return s != null ? Integer.parseInt(s.trim()) : def; } catch (NumberFormatException e) { return def; }
    }
    private static long parseLong(String s, long def) {
        try { return s != null ? Long.parseLong(s.trim()) : def; } catch (NumberFormatException e) { return def; }
    }
}
