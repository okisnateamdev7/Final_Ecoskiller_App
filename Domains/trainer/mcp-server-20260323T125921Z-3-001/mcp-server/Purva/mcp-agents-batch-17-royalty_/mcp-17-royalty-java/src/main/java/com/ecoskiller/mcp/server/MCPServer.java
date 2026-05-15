package com.ecoskiller.mcp.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ecoskiller.mcp.handlers.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MCPServer {
    private static final Logger logger = LoggerFactory.getLogger(MCPServer.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    
    private final MessageHandler messageHandler;
    private volatile boolean running = true;

    public MCPServer() {
        this.messageHandler = new MessageHandler();
    }

    public static void main(String[] args) {
        MCPServer server = new MCPServer();
        server.start();
    }

    public void start() {
        logger.info("🚀 MCP-17 Royalty Server starting...");
        logger.info("📦 Transport: stdio (stdin/stdout)");
        logger.info("📋 Format: JSON-RPC 2.0");
        logger.info("🎯 MCP Version: 2024-11-05");
        logger.info("🔧 Agents: 18 | Priority: HIGH");
        
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (running && scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    JsonNode requestNode = mapper.readTree(line);
                    JsonNode response = messageHandler.handle(requestNode);
                    
                    if (response != null) {
                        System.out.println(mapper.writeValueAsString(response));
                        System.out.flush();
                    }
                } catch (Exception e) {
                    logger.error("❌ Error processing request: {}", e.getMessage(), e);
                    JsonNode errorResponse = buildErrorResponse(null, -32603, "Internal Server Error");
                    System.out.println(mapper.writeValueAsString(errorResponse));
                    System.out.flush();
                }
            }
        } catch (Exception e) {
            logger.error("💥 Server crashed: {}", e.getMessage(), e);
            System.exit(1);
        }
        
        logger.info("🛑 Server shutdown");
    }

    private JsonNode buildErrorResponse(Object id, int code, String message) {
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.put("jsonrpc", "2.0");
        
        ObjectNode errorNode = JsonNodeFactory.instance.objectNode();
        errorNode.put("code", code);
        errorNode.put("message", message);
        
        response.set("error", errorNode);
        
        if (id != null) {
            response.put("id", id.toString());
        }
        
        return response;
    }

    public void shutdown() {
        running = false;
    }
}
