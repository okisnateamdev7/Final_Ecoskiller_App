package com.ecoskiller.mcp.security;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Token Bucket rate limiter for MCP request throttling.
 *
 * Prevents:
 * - Brute-force enumeration attacks
 * - Resource exhaustion DoS
 * - Runaway client loops
 *
 * Strategy: Sliding window per client key.
 * Default: 100 requests/minute (configurable via ServerConfig).
 */
public class RateLimiter {

    private static final Logger LOGGER = Logger.getLogger(RateLimiter.class.getName());
    private static final long WINDOW_MS = 60_000L; // 1 minute

    private final int maxRequestsPerWindow;
    private final ConcurrentHashMap<String, WindowCounter> counters = new ConcurrentHashMap<>();

    public RateLimiter(int maxRequestsPerWindow) {
        this.maxRequestsPerWindow = maxRequestsPerWindow;
    }

    /**
     * Check if a request should be allowed for the given key.
     * @param clientKey IP address, session ID, or "global"
     * @return true if request is allowed, false if rate limited
     */
    public boolean allowRequest(String clientKey) {
        long now = Instant.now().toEpochMilli();
        WindowCounter counter = counters.computeIfAbsent(clientKey, k -> new WindowCounter(now));

        synchronized (counter) {
            // Reset window if expired
            if (now - counter.windowStart > WINDOW_MS) {
                counter.windowStart = now;
                counter.count.set(0);
            }

            int current = counter.count.incrementAndGet();
            if (current > maxRequestsPerWindow) {
                LOGGER.warning("Rate limit exceeded for key: " + clientKey +
                        " (" + current + "/" + maxRequestsPerWindow + " req/min)");
                return false;
            }
            return true;
        }
    }

    /** Get current request count for a key */
    public int getCurrentCount(String clientKey) {
        WindowCounter counter = counters.get(clientKey);
        if (counter == null) return 0;
        return counter.count.get();
    }

    /** Reset counter for a key (used in tests) */
    public void reset(String clientKey) {
        counters.remove(clientKey);
    }

    private static class WindowCounter {
        volatile long windowStart;
        final AtomicInteger count = new AtomicInteger(0);

        WindowCounter(long start) {
            this.windowStart = start;
        }
    }
}
