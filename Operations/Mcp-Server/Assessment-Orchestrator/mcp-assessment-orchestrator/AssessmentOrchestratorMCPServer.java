package com.ecoskiller.mcp;

import com.ecoskiller.mcp.protocol.MCPProtocolHandler;
import com.ecoskiller.mcp.security.SecurityConfig;

import java.io.*;
import java.util.logging.*;

/**
 * Ecoskiller Assessment-Orchestrator MCP Server
 * ==============================================
 * Service   : assessment-orchestrator
 * Namespace : realtime (k3s — GCP asia-south1 + AWS ap-south-1)
 * Transport : stdio (stdin/stdout) — JSON-RPC 2.0
 * MCP       : 2024-11-05
 * Tools     : 20 — full lifecycle of GD / Interview / Dojo sessions
 *
 * Security layers:
 *   - Keycloak JWT validation on every tool call
 *   - RBAC: CANDIDATE / RECRUITER / MODERATOR / ADMIN / SERVICE_ACCOUNT
 *   - Multi-tenant isolation (tenant_id scoped on every operation)
 *   - Input sanitisation (SQL injection, XSS, oversized payloads)
 *   - Jitsi JWT issuance (HS256, 5-min TTL)
 *   - Idempotency keys on all state-changing & completion events
 *   - Distributed lock simulation for slot booking
 */
public class AssessmentOrchestratorMCPServer {

    private static final Logger LOG = Logger.getLogger(AssessmentOrchestratorMCPServer.class.getName());
    public static final String SERVER_VERSION = "1.0.0";
    public static final String MCP_VERSION    = "2024-11-05";
    public static final String SERVER_NAME    = "mcp-assessment-orchestrator";

    public static void main(String[] args) {
        configureLogging();
        LOG.info("=======================================================");
        LOG.info(" Ecoskiller Assessment-Orchestrator MCP Server v" + SERVER_VERSION);
        LOG.info(" Namespace: realtime | Transport: stdio | 20 tools");
        LOG.info("=======================================================");
        SecurityConfig.init();

        try (BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)))) {

            MCPProtocolHandler handler = new MCPProtocolHandler(out);
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) handler.handle(line);
            }
        } catch (IOException e) {
            LOG.severe("Fatal I/O error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void configureLogging() {
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        StreamHandler stderr = new StreamHandler(System.err, new SimpleFormatter()) {
            @Override public synchronized void publish(LogRecord r) { super.publish(r); flush(); }
        };
        stderr.setLevel(Level.ALL);
        root.addHandler(stderr);
        root.setLevel(Level.INFO);
    }
}
