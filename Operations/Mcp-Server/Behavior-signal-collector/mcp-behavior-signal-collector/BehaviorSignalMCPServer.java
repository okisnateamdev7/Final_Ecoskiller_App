package com.ecoskiller.mcp;

import com.ecoskiller.mcp.protocol.MCPProtocolHandler;
import com.ecoskiller.mcp.security.SignalSecurityConfig;

import java.io.*;
import java.util.logging.*;

/**
 * Ecoskiller Behavior-Signal-Collector MCP Server
 * =================================================
 * Service   : behavior-signal-collector
 * Namespace : realtime (k3s — GCP asia-south1 + AWS ap-south-1)
 * Transport : stdio (stdin/stdout) — JSON-RPC 2.0
 * MCP       : 2024-11-05
 * Tools     : 20 — full behavioral signal pipeline
 *
 * Pipeline:
 *   Ingest → Validate → Deduplicate → Feature Extract →
 *   Normalize → Anomaly Detect → Aggregate → Publish →
 *   Audit → Fairness
 *
 * Security:
 *   - Keycloak JWT validation (CANDIDATE / RECRUITER / ADMIN / SERVICE_ACCOUNT)
 *   - Tenant isolation (tenant_id on every operation)
 *   - Input sanitisation (injection + oversized payload protection)
 *   - Signal value range enforcement per signal type
 *   - Idempotency via message_id deduplication
 *   - Immutable DPDP 2023 / GDPR audit log (7-year retention)
 *
 * Env vars:
 *   BSC_JWT_SECRET        — HMAC secret (dev) / Keycloak RS256 (prod)
 *   BSC_STRICT_MODE       — true = enforce JWT signature
 *   BSC_MAX_BATCH_SIZE    — max signals per ingest call (default 1000)
 *   BSC_ANOMALY_IQR_MULT  — IQR multiplier for outlier detection (default 1.5)
 *   BSC_QUALITY_THRESHOLD — min quality score before alert (default 0.85)
 */
public class BehaviorSignalMCPServer {

    private static final Logger LOG = Logger.getLogger(BehaviorSignalMCPServer.class.getName());
    public static final String SERVER_VERSION = "1.0.0";
    public static final String MCP_VERSION    = "2024-11-05";
    public static final String SERVER_NAME    = "mcp-behavior-signal-collector";

    public static void main(String[] args) {
        configureLogging();
        LOG.info("======================================================");
        LOG.info(" Ecoskiller Behavior-Signal-Collector MCP Server v" + SERVER_VERSION);
        LOG.info(" Namespace: realtime | 20 tools | Transport: stdio");
        LOG.info("======================================================");
        SignalSecurityConfig.init();

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
