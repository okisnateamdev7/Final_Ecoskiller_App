package com.ecoskiller.airflow.mcp.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/** Token-bucket rate limiter: 100 req / 60s per user. */
public class RateLimiter {

    private static final Logger LOG = Logger.getLogger(RateLimiter.class.getName());
    private final int  maxRequests;
    private final long windowMs;

    private static class Bucket {
        AtomicInteger count = new AtomicInteger(0);
        long windowStart = System.currentTimeMillis();
    }

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimiter(int maxRequests, long windowMs) {
        this.maxRequests = maxRequests;
        this.windowMs    = windowMs;
    }

    public void check(String userId) {
        Bucket b = buckets.computeIfAbsent(userId, k -> new Bucket());
        synchronized (b) {
            long now = System.currentTimeMillis();
            if (now - b.windowStart > windowMs) { b.windowStart = now; b.count.set(0); }
            int c = b.count.incrementAndGet();
            if (c > maxRequests) {
                LOG.warning("[RateLimit] Exceeded for: " + userId);
                throw new SecurityException("Rate limit exceeded: " + maxRequests +
                    " req/" + (windowMs / 1000) + "s");
            }
        }
    }
}
