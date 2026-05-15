package com.ecoskiller.mcp.keycloak.security;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Token bucket rate limiter per tool name.
 *
 * Limits:
 *   - Default: 60 calls / minute per tool
 *   - Sensitive tools (authenticate_user, bulk_user_import): 20 calls / minute
 *
 * Prevents abuse of authentication endpoints and bulk operations.
 */
public class RateLimiter {

    private static final long WINDOW_MS = 60_000L;

    private record Bucket(AtomicLong count, AtomicLong windowStart) {}
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    private static int limitFor(String toolName) {
        return switch (toolName) {
            case "authenticate_user" -> 20;
            case "bulk_user_import"  -> 5;
            case "user_management"   -> 30;
            default                  -> 60;
        };
    }

    public boolean allow(String toolName) {
        long now = System.currentTimeMillis();
        Bucket bucket = buckets.computeIfAbsent(toolName,
            k -> new Bucket(new AtomicLong(0), new AtomicLong(now)));

        // Reset window if expired
        long windowStart = bucket.windowStart().get();
        if (now - windowStart > WINDOW_MS) {
            bucket.windowStart().compareAndSet(windowStart, now);
            bucket.count().set(0);
        }

        return bucket.count().incrementAndGet() <= limitFor(toolName);
    }
}
