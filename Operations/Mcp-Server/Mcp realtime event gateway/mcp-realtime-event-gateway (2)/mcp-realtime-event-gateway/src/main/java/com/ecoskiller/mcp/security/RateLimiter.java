package com.ecoskiller.mcp.security;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Sliding-window rate limiter for MCP request throttling.
 *
 * Protects against:
 *  • Brute-force enumeration
 *  • Resource exhaustion / DoS
 *  • Runaway client loops
 *
 * Also used per WebSocket client: max 100 events/sec (per documentation).
 * Default MCP throttle: 120 requests/minute.
 */
public class RateLimiter {

    private static final Logger LOGGER = Logger.getLogger(RateLimiter.class.getName());
    private static final long   WINDOW_MS = 60_000L; // 1-minute sliding window

    private final int maxPerWindow;
    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    public RateLimiter(int maxPerWindow) { this.maxPerWindow = maxPerWindow; }

    /** @return true if request allowed; false if rate-limited */
    public boolean allowRequest(String key) {
        long now = System.currentTimeMillis();
        Window w = windows.computeIfAbsent(key, k -> new Window(now));
        synchronized (w) {
            if (now - w.start > WINDOW_MS) { w.start = now; w.count.set(0); }
            int current = w.count.incrementAndGet();
            if (current > maxPerWindow) {
                LOGGER.warning("Rate limit hit: key=" + key + " count=" + current + "/" + maxPerWindow);
                return false;
            }
        }
        return true;
    }

    /** Per-second event rate limiter for WebSocket clients (100 events/sec max) */
    public boolean allowClientEvent(String clientId, int maxPerSec) {
        String key = "evt:" + clientId;
        long now   = System.currentTimeMillis();
        Window w   = windows.computeIfAbsent(key, k -> new Window(now));
        synchronized (w) {
            if (now - w.start > 1000) { w.start = now; w.count.set(0); }
            return w.count.incrementAndGet() <= maxPerSec;
        }
    }

    public int getCount(String key) {
        Window w = windows.get(key);
        return w == null ? 0 : w.count.get();
    }

    public void reset(String key) { windows.remove(key); }

    private static class Window {
        volatile long start;
        final AtomicInteger count = new AtomicInteger(0);
        Window(long start) { this.start = start; }
    }
}
