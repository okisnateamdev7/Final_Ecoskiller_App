package com.ecoskiller.mcp;

import com.ecoskiller.mcp.protocol.MCPProtocolHandler;
import com.ecoskiller.mcp.security.SecurityConfig;

import java.io.*;
import java.util.logging.*;

/**
 * Ecoskiller Application-Service MCP Server
 * ==========================================
 * Category : CAT-APP (Hiring & Assessment Domain)
 * Namespace : core
 * Transport : stdio (stdin/stdout) — JSON-RPC 2.0
 * MCP Version: 2024-11-05
 * Tools    : 16 agents covering full application lifecycle
 *
 * Security Features:
 *   - JWT Bearer token validation (Keycloak OIDC)
 *   - Tenant-scoped data isolation (tenant_id on every operation)
 *   - Role-Based Access Control (CANDIDATE / RECRUITER / ADMIN)
 *   - Input sanitisation & SQL injection prevention
 *   - Idempotency key enforcement on all write operations
 *   - Immutable audit-log on every state change
 */
public class ApplicationMCPServer {

    private static final Logger LOG = Logger.getLogger(ApplicationMCPServer.class.getName());
    public static final String SERVER_VERSION = "1.0.0";
    public static final String MCP_VERSION   = "2024-11-05";
    public static final String SERVER_NAME   = "mcp-application-service";

    public static void main(String[] args) {
        configureLogging();

        LOG.info("=================================================");
        LOG.info(" Ecoskiller Application-Service MCP Server v" + SERVER_VERSION);
        LOG.info(" Transport : stdio | Protocol: JSON-RPC 2.0");
        LOG.info(" MCP Version: " + MCP_VERSION);
        LOG.info("=================================================");

        SecurityConfig.init();

        try (BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)))) {

            MCPProtocolHandler handler = new MCPProtocolHandler(out);

            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                handler.handle(line);
            }

        } catch (IOException e) {
            LOG.severe("Fatal I/O error: " + e.getMessage());
            System.exit(1);
        }
    }

    /** Route all internal logs to stderr so stdout stays clean for MCP messages. */
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
