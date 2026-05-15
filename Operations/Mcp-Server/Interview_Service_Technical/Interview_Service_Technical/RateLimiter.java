package com.ecoskiller.mcp.interview.security;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Token-bucket rate limiter per tenant + tool combination.
 *
 * Limits:
 *  - Default:           100 req/min per tenant
 *  - schedule/complete:  20 req/min per tenant (write-heavy tools)
 *  - get_analytics:      10 req/min per tenant (expensive reads)
 *
 * Production: replace with Redis-backed distributed rate limiter
 * (INCR + EXPIRE via Jedis/Lettuce) for multi-pod Kubernetes deployment.
 */
public class RateLimiter {

    private static final Logger LOG = Logger.getLogger(RateLimiter.class.getName());
    private static final long WINDOW_MS = 60_000L; // 1-minute sliding window

    private record BucketKey(String tenantId, String tool) {}
    private record Bucket(AtomicInteger count, long windowStart) {}

    private final ConcurrentHashMap<BucketKey, Bucket> buckets = new ConcurrentHashMap<>();

    private static final int defaultLimit(String tool) {
        return switch (tool) {
            case "schedule_interview", "complete_interview" -> 20;
            case "get_analytics"                            -> 10;
            case "delete_recording"                         -> 5;
            default                                         -> 100;
        };
    }

    /**
     * @return true if the request is allowed, false if rate-limited
     */
    public boolean allow(String tenantId, String tool) {
        BucketKey key   = new BucketKey(tenantId, tool);
        long      now   = System.currentTimeMillis();
        int       limit = defaultLimit(tool);

        Bucket bucket = buckets.compute(key, (k, existing) -> {
            if (existing == null || (now - existing.windowStart()) > WINDOW_MS) {
                return new Bucket(new AtomicInteger(0), now);
            }
            return existing;
        });

        int count = bucket.count().incrementAndGet();
        if (count > limit) {
            LOG.warning("Rate limit exceeded: tenant=" + tenantId + " tool=" + tool + " count=" + count);
            return false;
        }
        return true;
    }
}
