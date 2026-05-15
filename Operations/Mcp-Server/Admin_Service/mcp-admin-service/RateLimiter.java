package com.ecoskiller.admin.mcp.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Token-bucket rate limiter keyed by adminId.
 * Prevents abuse of the MCP server (brute-force, DoS).
 *
 * Default: 100 requests per 60 seconds per admin.
 */
public class RateLimiter {

    private static final Logger LOG = Logger.getLogger(RateLimiter.class.getName());

    private final int    maxRequests;
    private final long   windowMs;

    private static class Bucket {
        AtomicInteger count = new AtomicInteger(0);
        long windowStart     = System.currentTimeMillis();
    }

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimiter(int maxRequests, long windowMs) {
        this.maxRequests = maxRequests;
        this.windowMs    = windowMs;
    }

    /**
     * Checks and increments the rate counter for adminId.
     * Throws SecurityException if the limit is exceeded.
     */
    public void check(String adminId) {
        Bucket bucket = buckets.computeIfAbsent(adminId, k -> new Bucket());

        synchronized (bucket) {
            long now = System.currentTimeMillis();
            if (now - bucket.windowStart > windowMs) {
                // Reset window
                bucket.windowStart = now;
                bucket.count.set(0);
            }
            int current = bucket.count.incrementAndGet();
            if (current > maxRequests) {
                LOG.warning("[RateLimit] Exceeded for admin: " + adminId + " (" + current + " req)");
                throw new SecurityException("Rate limit exceeded. Max " + maxRequests +
                    " requests per " + (windowMs / 1000) + "s.");
            }
        }
    }
}
