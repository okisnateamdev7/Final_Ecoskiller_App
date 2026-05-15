package com.ecoskiller.mcp.longhorn.security;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe sliding-window rate limiter.
 *
 * Limits the number of tool calls within a given time window.
 * Protects against runaway agentic loops and abuse.
 *
 * Default: 60 calls per 60 seconds (global, not per-tool).
 */
public class RateLimiter {

    private final int maxRequests;
    private final long windowMs;

    /** Tracks per-tool call counts and window start times */
    private final ConcurrentHashMap<String, WindowCounter> counters = new ConcurrentHashMap<>();

    public RateLimiter(int maxRequests, long windowMs) {
        this.maxRequests = maxRequests;
        this.windowMs    = windowMs;
    }

    /**
     * Returns true if the request is allowed, false if the rate limit is exceeded.
     */
    public boolean allowRequest(String toolName) {
        String key = toolName != null ? toolName : "global";
        WindowCounter counter = counters.computeIfAbsent(key, k -> new WindowCounter());
        return counter.tryAcquire(maxRequests, windowMs);
    }

    private static class WindowCounter {
        private final AtomicInteger count     = new AtomicInteger(0);
        private final AtomicLong   windowStart = new AtomicLong(System.currentTimeMillis());

        synchronized boolean tryAcquire(int max, long windowMs) {
            long now = System.currentTimeMillis();
            long start = windowStart.get();

            if (now - start >= windowMs) {
                // Slide the window
                windowStart.set(now);
                count.set(1);
                return true;
            }

            int current = count.incrementAndGet();
            return current <= max;
        }
    }
}
