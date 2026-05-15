package com.ecoskiller.mcp.irs;

import com.ecoskiller.mcp.irs.handler.McpRequestHandler;
import com.ecoskiller.mcp.irs.security.SecurityContext;
import com.ecoskiller.mcp.irs.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Innovation-Registry-Service MCP Server
 *
 * Entry point for the MCP server. Communicates via stdin/stdout using JSON-RPC 2.0.
 * Implements MCP protocol version 2024-11-05.
 *
 * Security: All inputs are validated, sanitized, and rate-limited.
 * Transport: stdio (stdin/stdout)
 */
public class McpServer {

    private static final Logger LOGGER = Logger.getLogger(McpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "mcp-innovation-registry";
    private static final String SERVER_VERSION = "1.0.0";

    private final ObjectMapper mapper;
    private final McpRequestHandler requestHandler;
    private volatile boolean running = true;

    public McpServer() {
        this.mapper = JsonUtil.createSecureMapper();
        this.requestHandler = new McpRequestHandler();
        setupShutdownHook();
    }

    public static void main(String[] args) {
        configureLogging();
        new McpServer().run();
    }

    public void run() {
        LOGGER.info("Innovation-Registry-Service MCP Server starting...");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true)) {

            String line;
            while (running && (line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    String response = processRequest(line);
                    if (response != null) {
                        writer.println(response);
                        writer.flush();
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error processing request", e);
                    String errorResponse = createErrorResponse(null, -32603, "Internal error: " + sanitizeErrorMessage(e.getMessage()));
                    writer.println(errorResponse);
                    writer.flush();
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Fatal IO error", e);
        }

        LOGGER.info("Innovation-Registry-Service MCP Server stopped.");
    }

    private String processRequest(String rawInput) throws Exception {
        // Security: enforce max input size (1MB)
        if (rawInput.length() > 1_048_576) {
            return createErrorResponse(null, -32700, "Request too large");
        }

        JsonNode request;
        try {
            request = mapper.readTree(rawInput);
        } catch (Exception e) {
            return createErrorResponse(null, -32700, "Parse error: invalid JSON");
        }

        // Validate JSON-RPC 2.0 structure
        if (!request.has("jsonrpc") || !"2.0".equals(request.get("jsonrpc").asText())) {
            return createErrorResponse(extractId(request), -32600, "Invalid Request: missing or invalid jsonrpc field");
        }
        if (!request.has("method")) {
            return createErrorResponse(extractId(request), -32600, "Invalid Request: missing method");
        }

        String method = request.get("method").asText();
        JsonNode id = extractId(request);
        JsonNode params = request.has("params") ? request.get("params") : mapper.createObjectNode();

        // Route to handler
        return requestHandler.handle(method, id, params, mapper, MCP_VERSION, SERVER_NAME, SERVER_VERSION);
    }

    private JsonNode extractId(JsonNode request) {
        return request.has("id") ? request.get("id") : null;
    }

    private String createErrorResponse(JsonNode id, int code, String message) {
        ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        if (id != null) {
            response.set("id", id);
        } else {
            response.putNull("id");
        }
        ObjectNode error = mapper.createObjectNode();
        error.put("code", code);
        error.put("message", message);
        response.set("error", error);
        try {
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"id\":null,\"error\":{\"code\":-32603,\"message\":\"Internal error\"}}";
        }
    }

    private String sanitizeErrorMessage(String message) {
        if (message == null) return "Unknown error";
        // Strip any potentially sensitive info from error messages
        return message.replaceAll("(?i)(password|secret|key|token|credential)\\s*=\\s*\\S+", "[REDACTED]")
                      .substring(0, Math.min(message.length(), 200));
    }

    private void setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            running = false;
            LOGGER.info("Shutdown signal received.");
        }));
    }

    private static void configureLogging() {
        // Log to stderr to avoid polluting stdout (MCP protocol channel)
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
        Logger.getLogger("").setLevel(Level.INFO);
    }
}
