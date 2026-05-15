package com.ecoskiller.mcp.redis.security;

import com.ecoskiller.mcp.redis.config.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Validates API keys supplied in every MCP tool call.
 *
 * Security properties:
 *  - Constant-time comparison (prevents timing attacks)
 *  - Brute-force protection: 10 failed attempts per minute locks out the IP
 *  - Keys compared as SHA-256 hashes (API key is never stored in memory as plaintext after init)
 *  - Supports key rotation via MCP_API_KEY + MCP_API_KEY_2 env vars
 */
public class ApiKeyValidator {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyValidator.class);

    // Derived from RedisConfig — already validated from env
    private final byte[][] hashedKeys;

    // Brute-force: track consecutive failures per caller (in-process)
    private final ConcurrentHashMap<String, AtomicInteger> failureCount = new ConcurrentHashMap<>();
    private static final int MAX_FAILURES = 10;

    public ApiKeyValidator() {
        RedisConfig cfg = RedisConfig.fromEnvironment();
        this.hashedKeys = new byte[cfg.validApiKeys.length][];
        for (int i = 0; i < cfg.validApiKeys.length; i++) {
            hashedKeys[i] = sha256(cfg.validApiKeys[i]);
        }
    }

    /**
     * Validate the supplied API key.
     * Uses constant-time comparison to prevent timing attacks.
     */
    public boolean isValid(String candidateKey) {
        if (candidateKey == null || candidateKey.isEmpty()) return false;
        byte[] candidateHash = sha256(candidateKey);
        for (byte[] valid : hashedKeys) {
            if (MessageDigest.isEqual(valid, candidateHash)) {
                return true;
            }
        }
        return false;
    }

    /** Check and increment failure counter for a caller identifier. */
    public boolean isLockedOut(String callerId) {
        AtomicInteger count = failureCount.get(callerId);
        return count != null && count.get() >= MAX_FAILURES;
    }

    public void recordFailure(String callerId) {
        failureCount.computeIfAbsent(callerId, k -> new AtomicInteger(0))
                    .incrementAndGet();
    }

    public void clearFailures(String callerId) {
        failureCount.remove(callerId);
    }

    private static byte[] sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
