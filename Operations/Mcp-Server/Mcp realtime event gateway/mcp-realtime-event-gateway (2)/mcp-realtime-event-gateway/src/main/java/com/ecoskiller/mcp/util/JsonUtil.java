package com.ecoskiller.mcp.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * Factory for hardened Jackson ObjectMapper.
 *
 * Security settings:
 *  • Default typing DISABLED — prevents deserialization gadget RCE (CVE-2017-7525 family)
 *  • FAIL_ON_NUMBERS_FOR_ENUMS — reject numeric enum values
 *  • WRITE_DATES_AS_TIMESTAMPS disabled — ISO-8601 strings only
 *  • FAIL_ON_EMPTY_BEANS disabled — prevents noisy serialisation failures
 */
public final class JsonUtil {
    private JsonUtil() {}

    public static ObjectMapper createSecureMapper() {
        return JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
                .build();
    }
}
