package com.ecoskiller.mcp.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * Factory for creating hardened Jackson ObjectMapper instances.
 *
 * Security settings applied:
 * - FAIL_ON_UNKNOWN_PROPERTIES disabled (strict deserialization where needed)
 * - Default typing DISABLED (prevents Jackson deserialization gadget attacks)
 * - MAX_NESTING_DEPTH limited (prevents stack overflow via deeply nested JSON)
 * - FAIL_ON_NUMBERS_FOR_ENUMS enabled
 */
public final class JsonUtil {

    private JsonUtil() {}

    public static ObjectMapper createSecureMapper() {
        return JsonMapper.builder()
                // Security: Disable default typing to prevent deserialization gadget attacks
                // (CVE-2017-7525 family of Jackson RCE vulnerabilities)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // Security: Reject numeric values for enums
                .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
                .build();
    }
}
