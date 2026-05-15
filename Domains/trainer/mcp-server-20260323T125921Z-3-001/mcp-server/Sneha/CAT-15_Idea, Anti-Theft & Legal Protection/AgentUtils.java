package com.ecoskiller.mcp15;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.security.MessageDigest;
import java.time.Instant;
import java.util.UUID;

/**
 * Shared utilities for all CAT-15 agents.
 */
public final class AgentUtils {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    private AgentUtils() {}

    /** Build a minimal MCP tool definition. */
    public static ObjectNode buildToolDef(String name, String description, ObjectNode inputSchema) {
        ObjectNode def = MAPPER.createObjectNode();
        def.put("name",        name);
        def.put("description", description);
        def.set("inputSchema", inputSchema);
        return def;
    }

    /** Build a JSON Schema object type with the given properties. */
    public static ObjectNode schemaObject(String... requiredFields) {
        ObjectNode schema = MAPPER.createObjectNode();
        schema.put("type", "object");
        schema.putObject("properties");
        if (requiredFields.length > 0) {
            ArrayNode req = schema.putArray("required");
            for (String f : requiredFields) req.add(f);
        }
        return schema;
    }

    /** Add a string property to a schema. */
    public static void addStringProp(ObjectNode schema, String name, String description) {
        ObjectNode prop = ((ObjectNode) schema.get("properties")).putObject(name);
        prop.put("type",        "string");
        prop.put("description", description);
    }

    /** Add an integer property to a schema. */
    public static void addIntProp(ObjectNode schema, String name, String description) {
        ObjectNode prop = ((ObjectNode) schema.get("properties")).putObject(name);
        prop.put("type",        "integer");
        prop.put("description", description);
    }

    /** Add a number property to a schema. */
    public static void addNumberProp(ObjectNode schema, String name, String description) {
        ObjectNode prop = ((ObjectNode) schema.get("properties")).putObject(name);
        prop.put("type",        "number");
        prop.put("description", description);
    }

    /** Add a boolean property to a schema. */
    public static void addBoolProp(ObjectNode schema, String name, String description) {
        ObjectNode prop = ((ObjectNode) schema.get("properties")).putObject(name);
        prop.put("type",        "boolean");
        prop.put("description", description);
    }

    /** SHA-256 hex of input string. */
    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Generate a deterministic UUID from a seed string. */
    public static String deterministicId(String seed) {
        return UUID.nameUUIDFromBytes(seed.getBytes()).toString();
    }

    /** Current ISO-8601 timestamp. */
    public static String now() {
        return Instant.now().toString();
    }

    /** Cosine similarity of two double arrays (returns -1..1). */
    public static double cosineSimilarity(double[] a, double[] b) {
        if (a.length != b.length) throw new IllegalArgumentException("Vector length mismatch");
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot   += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        double denom = Math.sqrt(normA) * Math.sqrt(normB);
        return denom == 0 ? 0.0 : dot / denom;
    }

    /** Simulate a lightweight embedding from an input string (for demo purposes). */
    public static double[] pseudoEmbed(String text, int dims) {
        double[] vec = new double[dims];
        int hash = text.hashCode();
        for (int i = 0; i < dims; i++) {
            vec[i] = Math.sin(hash * (i + 1) * 0.1);
        }
        return vec;
    }
}
