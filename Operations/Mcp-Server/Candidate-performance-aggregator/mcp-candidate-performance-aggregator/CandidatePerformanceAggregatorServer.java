package com.ecoskiller.mcp;

import com.ecoskiller.mcp.protocol.MCPProtocolHandler;
import com.ecoskiller.mcp.security.AggSecurityConfig;

import java.io.*;
import java.util.logging.*;

/**
 * Ecoskiller Candidate-Performance-Aggregator MCP Server
 * =======================================================
 * Service   : candidate-performance-aggregator
 * Namespace : ecoskiller/prod (k3s — GCP asia-south1 + AWS ap-south-1)
 * Transport : stdio (stdin/stdout) — JSON-RPC 2.0
 * MCP       : 2024-11-05
 * Tools     : 20 — full multi-track aggregation pipeline
 *
 * Pipeline stages:
 *   Ingest track scores (GD / Interview / Dojo) →
 *   Deduplicate by event_id (Redis dedup:{event_id}) →
 *   Acquire distributed lock (agg_lock:{candidate_id}:{cycle_id}) →
 *   Weighted aggregation with partial participation handling →
 *   Rubric-based normalization (tenant rubric) →
 *   Belt level mapping: BRONZE / SILVER / GOLD / PLATINUM →
 *   Persist to PostgreSQL + ClickHouse audit →
 *   Publish candidate.performance.computed to Kafka →
 *   Emit DLQ event on failure after 3 retries
 *
 * Default weights: GD=35% · Interview=40% · Dojo=25%
 *
 * Security:
 *   - Keycloak JWT (CANDIDATE / RECRUITER / ADMIN / SERVICE_ACCOUNT)
 *   - Tenant isolation (tenant_id — mirrors PostgreSQL RLS)
 *   - Input sanitisation (SQL injection / XSS / oversized payload)
 *   - Event deduplication (event_id idempotency)
 *   - Distributed lock simulation preventing concurrent aggregation
 *   - DPDPA 2023 score explainability + immutable audit trail
 *
 * Env vars:
 *   CPA_JWT_SECRET          — HMAC secret (dev) / Keycloak RS256 (prod)
 *   CPA_STRICT_MODE         — true = enforce JWT signature
 *   CPA_DEFAULT_GD_WEIGHT   — GD weight 0.0-1.0 (default 0.35)
 *   CPA_DEFAULT_INT_WEIGHT  — Interview weight (default 0.40)
 *   CPA_DEFAULT_DOJO_WEIGHT — Dojo weight (default 0.25)
 *   CPA_LOCK_TTL_SECONDS    — distributed lock TTL (default 30)
 *   CPA_DEDUP_TTL_HOURS     — dedup cache TTL hours (default 48)
 */
public class CandidatePerformanceAggregatorServer {

    private static final Logger LOG = Logger.getLogger(CandidatePerformanceAggregatorServer.class.getName());
    public static final String SERVER_VERSION = "1.0.0";
    public static final String MCP_VERSION    = "2024-11-05";
    public static final String SERVER_NAME    = "mcp-candidate-performance-aggregator";

    public static void main(String[] args) {
        configureLogging();
        LOG.info("==========================================================");
        LOG.info(" Ecoskiller CPA MCP Server v" + SERVER_VERSION);
        LOG.info(" Namespace: ecoskiller/prod | 20 tools | Transport: stdio");
        LOG.info(" Belt: BRONZE → SILVER → GOLD → PLATINUM");
        LOG.info("==========================================================");
        AggSecurityConfig.init();

        try (BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)))) {
            MCPProtocolHandler handler = new MCPProtocolHandler(out);
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) handler.handle(line);
            }
        } catch (IOException e) {
            LOG.severe("Fatal I/O: " + e.getMessage()); System.exit(1);
        }
    }

    private static void configureLogging() {
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        StreamHandler sh = new StreamHandler(System.err, new SimpleFormatter()) {
            @Override public synchronized void publish(LogRecord r) { super.publish(r); flush(); }
        };
        sh.setLevel(Level.ALL); root.addHandler(sh); root.setLevel(Level.INFO);
    }
}
