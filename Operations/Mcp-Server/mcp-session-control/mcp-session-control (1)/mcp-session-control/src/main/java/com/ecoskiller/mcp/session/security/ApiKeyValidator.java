package com.ecoskiller.mcp.session.security;

import com.ecoskiller.mcp.session.config.SessionControlConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

/**
 * Validates MCP API keys on every tool call.
 * SHA-256 hashed storage — plaintext keys never retained.
 * Constant-time comparison — prevents timing-based key enumeration attacks.
 * Supports two keys (MCP_API_KEY + MCP_API_KEY_2) for zero-downtime rotation.
 */
public class ApiKeyValidator {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyValidator.class);
    private final byte[][] hashedKeys;

    public ApiKeyValidator(SessionControlConfig cfg) {
        this.hashedKeys = new byte[cfg.validApiKeys.length][];
        for (int i = 0; i < cfg.validApiKeys.length; i++) {
            hashedKeys[i] = sha256(cfg.validApiKeys[i]);
        }
        log.debug("ApiKeyValidator initialised with {} key(s)", hashedKeys.length);
    }

    public boolean isValid(String candidate) {
        if (candidate == null || candidate.isEmpty()) return false;
        byte[] h = sha256(candidate);
        for (byte[] valid : hashedKeys) {
            if (MessageDigest.isEqual(valid, h)) return true;
        }
        return false;
    }

    private static byte[] sha256(String input) {
        try {
            return MessageDigest.getInstance("SHA-256")
                                .digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 unavailable", e);
        }
    }
}
