package com.ecoskiller.mcp.licensing;

import com.ecoskiller.mcp.licensing.security.SecurityManager;
import com.ecoskiller.mcp.licensing.tools.*;
import com.ecoskiller.mcp.licensing.util.JsonRpcHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ecoskiller | CAT-17 — Marketplace, Royalty & Compliance
 * MCP Server: licensing-contract-service (Java)
 *
 * Implements the Model Context Protocol (MCP) 2024-11-05 via stdio JSON-RPC 2.0.
 * Provides 10 tools covering the full contract lifecycle for IP licensing agreements
 * between candidates and businesses on the Ecoskiller platform.
 *
 * Security: JWT validation, input sanitisation, rate limiting, audit logging.
 * Compliance: India IT Act 2000, DPDPA 2023, GST (SAC 9973).
 */
public class LicensingContractMCPServer {

    private static final Logger LOG = Logger.getLogger(LicensingContractMCPServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_VERSION = "1.0.0";
    private static final String SERVER_NAME = "ecoskiller-licensing-contract-mcp";

    private final ContractLifecycleTool    contractLifecycleTool;
    private final ContractAmendmentTool    contractAmendmentTool;
    private final ContractTerminationTool  contractTerminationTool;
    private final SignatureOrchestrationTool signatureTool;
    private final RoyaltyRateValidationTool  royaltyRateTool;
    private final MinorCandidateTool       minorCandidateTool;
    private final ContractSearchTool       contractSearchTool;
    private final ContractVersionsTool     contractVersionsTool;
    private final ComplianceAuditTool      complianceAuditTool;
    private final KafkaEventTool           kafkaEventTool;
    private final SecurityManager          securityManager;
    private final JsonRpcHandler           rpcHandler;

    public LicensingContractMCPServer() {
        this.securityManager        = new SecurityManager();
        this.rpcHandler             = new JsonRpcHandler();
        this.contractLifecycleTool  = new ContractLifecycleTool(securityManager);
        this.contractAmendmentTool  = new ContractAmendmentTool(securityManager);
        this.contractTerminationTool = new ContractTerminationTool(securityManager);
        this.signatureTool          = new SignatureOrchestrationTool(securityManager);
        this.royaltyRateTool        = new RoyaltyRateValidationTool(securityManager);
        this.minorCandidateTool     = new MinorCandidateTool(securityManager);
        this.contractSearchTool     = new ContractSearchTool(securityManager);
        this.contractVersionsTool   = new ContractVersionsTool(securityManager);
        this.complianceAuditTool    = new ComplianceAuditTool(securityManager);
        this.kafkaEventTool         = new KafkaEventTool(securityManager);
    }

    public static void main(String[] args) throws IOException {
        configureLogging();
        new LicensingContractMCPServer().run();
    }

    private static void configureLogging() {
        // Direct all logs to stderr so stdout stays clean for JSON-RPC
        Logger root = Logger.getLogger("");
        for (var h : root.getHandlers()) root.removeHandler(h);
        var handler = new java.util.logging.StreamHandler(System.err,
                new java.util.logging.SimpleFormatter());
        handler.setLevel(Level.ALL);
        root.addHandler(handler);
        root.setLevel(Level.INFO);
    }

    public void run() throws IOException {
        LOG.info("[MCP] " + SERVER_NAME + " v" + SERVER_VERSION + " starting on stdio");

        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    out = new PrintWriter(new OutputStreamWriter(System.out), true);

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                JSONObject request  = new JSONObject(line);
                JSONObject response = handleRequest(request);
                if (response != null) {
                    out.println(response.toString());
                    out.flush();
                }
            } catch (Exception e) {
                LOG.log(Level.WARNING, "[MCP] Parse error: " + e.getMessage(), e);
                out.println(rpcHandler.parseError(null).toString());
                out.flush();
            }
        }
        LOG.info("[MCP] stdin closed – server shutting down");
    }

    // ── Request Dispatch ───────────────────────────────────────────────────────

    private JSONObject handleRequest(JSONObject req) {
        Object id     = req.has("id") ? req.get("id") : null;
        String method = req.optString("method", "");
        JSONObject params = req.optJSONObject("params");
        if (params == null) params = new JSONObject();

        return switch (method) {
            case "initialize"  -> handleInitialize(id, params);
            case "ping"        -> rpcHandler.success(id, new JSONObject().put("pong", true));
            case "tools/list"  -> handleToolsList(id);
            case "tools/call"  -> handleToolsCall(id, params);
            default            -> rpcHandler.methodNotFound(id, method);
        };
    }

    // ── initialize ─────────────────────────────────────────────────────────────

    private JSONObject handleInitialize(Object id, JSONObject params) {
        String clientVersion = params.optString("protocolVersion", "unknown");
        LOG.info("[MCP] Client connected, protocol=" + clientVersion);

        JSONObject capabilities = new JSONObject()
                .put("tools", new JSONObject().put("listChanged", false));

        JSONObject serverInfo = new JSONObject()
                .put("name", SERVER_NAME)
                .put("version", SERVER_VERSION);

        JSONObject result = new JSONObject()
                .put("protocolVersion", MCP_VERSION)
                .put("capabilities", capabilities)
                .put("serverInfo", serverInfo)
                .put("instructions",
                    "Ecoskiller licensing-contract-service MCP. " +
                    "All tools require a 'jwt_token' field in arguments for Keycloak RBAC validation. " +
                    "Royalty rates: 0.01–0.05%. Licensing terms: 10–15 years. " +
                    "Minor candidates require guardian consent before contract creation.");

        return rpcHandler.success(id, result);
    }

    // ── tools/list ─────────────────────────────────────────────────────────────

    private JSONObject handleToolsList(Object id) {
        JSONArray tools = new JSONArray();

        // 1. contract_lifecycle
        tools.put(buildToolDef("contract_lifecycle",
            "Create, retrieve, or advance a licensing contract through its state machine. " +
            "States: DRAFT → PENDING_REVIEW → PENDING_SIGNATURE → ACTIVE → SUSPENDED → TERMINATED. " +
            "Required role: ecoskiller:licensing:create (create) or ecoskiller:licensing:admin (state transitions).",
            new JSONObject()
                .put("type", "object")
                .put("required", new JSONArray().put("action").put("jwt_token"))
                .put("properties", new JSONObject()
                    .put("action", prop("string", "One of: create | get | advance_state"))
                    .put("jwt_token", prop("string", "Keycloak Bearer JWT"))
                    .put("idea_id", prop("string", "UUID of the idea being licensed"))
                    .put("licensee_business_id", prop("string", "UUID of the licensing business"))
                    .put("idea_owner_candidate_id", prop("string", "UUID of the candidate who owns the idea"))
                    .put("tenant_id", prop("string", "Tenant identifier"))
                    .put("proposed_royalty_rate", prop("number", "Rate between 0.0001 and 0.0005"))
                    .put("proposed_term_years", prop("integer", "10 to 15 years"))
                    .put("territorial_scope", prop("string", "national | regional | global"))
                    .put("usage_scope", prop("string", "exclusive | non-exclusive"))
                    .put("contract_id", prop("string", "UUID for get/advance_state"))
                    .put("new_state", prop("string", "Target state for advance_state"))
                    .put("actor_id", prop("string", "Keycloak user ID performing the transition"))
                    .put("reason", prop("string", "Reason for state change"))
                    .put("licensee_gstin", prop("string", "GST Identification Number of licensee"))
                    .put("hsn_sac_code", prop("string", "SAC code, default 9973 for IP licensing"))
                )));

        // 2. contract_amendment
        tools.put(buildToolDef("contract_amendment",
            "Submit or retrieve a contract amendment. Creates a new version, re-triggers the signing flow, " +
            "and emits licensing.contract.amended Kafka event. Both parties must consent. " +
            "Required role: ecoskiller:licensing:create.",
            new JSONObject()
                .put("type", "object")
                .put("required", new JSONArray().put("action").put("contract_id").put("jwt_token"))
                .put("properties", new JSONObject()
                    .put("action", prop("string", "submit_amendment | get_amendments"))
                    .put("contract_id", prop("string", "UUID of the contract to amend"))
                    .put("jwt_token", prop("string", "Keycloak Bearer JWT"))
                    .put("amended_royalty_rate", prop("number", "New rate (optional)"))
                    .put("amended_term_years", prop("integer", "New term (optional)"))
                    .put("amended_territorial_scope", prop("string", "New scope (optional)"))
                    .put("amendment_reason", prop("string", "Justification for amendment"))
                    .put("both_party_consent", prop("boolean", "Must be true to proceed"))
                    .put("actor_id", prop("string", "Keycloak user ID of requester"))
                )));

        // 3. contract_termination
        tools.put(buildToolDef("contract_termination",
            "Initiate or check the status of a contract termination. Validates minimum royalty clause " +
            "compliance with the Royalty Audit Service before allowing termination. Returns 202 Accepted " +
            "with async compliance check status. Required role: ecoskiller:licensing:admin.",
            new JSONObject()
                .put("type", "object")
                .put("required", new JSONArray().put("action").put("contract_id").put("jwt_token"))
                .put("properties", new JSONObject()
                    .put("action", prop("string", "initiate | check_status"))
                    .put("contract_id", prop("string", "UUID of the contract to terminate"))
                    .put("jwt_token", prop("string", "Keycloak Bearer JWT"))
                    .put("terminating_party_id", prop("string", "Keycloak ID of the party requesting termination"))
                    .put("termination_reason", prop("string", "Legal reason for termination"))
                    .put("effective_date", prop("string", "ISO-8601 date for termination effective date"))
                )));

        // 4. signature_orchestration
        tools.put(buildToolDef("signature_orchestration",
            "Manage digital signature flow for a contract. Routes to Digital Signature Service, " +
            "tracks per-party completion, and handles timeout (default 7 days). " +
            "Required role: ecoskiller:licensing:create.",
            new JSONObject()
                .put("type", "object")
                .put("required", new JSONArray().put("action").put("contract_id").put("jwt_token"))
                .put("properties", new JSONObject()
                    .put("action", prop("string", "request_signatures | get_status | record_signed | handle_timeout"))
                    .put("contract_id", prop("string", "UUID of the contract"))
                    .put("jwt_token", prop("string", "Keycloak Bearer JWT"))
                    .put("signer_id", prop("string", "Keycloak ID of the signer (for record_signed)"))
                    .put("signer_role", prop("string", "candidate | business_representative | guardian"))
                    .put("signature_hash", prop("string", "Digital signature hash from Digital Signature Service"))
                    .put("document_url", prop("string", "MinIO pre-signed URL of the contract PDF"))
                    .put("signature_deadline", prop("string", "ISO-8601 datetime for signature deadline"))
                )));

        // 5. royalty_rate_validation
        tools.put(buildToolDef("royalty_rate_validation",
            "Validate a royalty rate and term against the platform_rate_config for a given tenant. " +
            "Enforces 0.01–0.05% rate range and 10–15 year term limits. " +
            "Supports per-tenant overrides configured via admin-service. No auth role required (internal validation).",
            new JSONObject()
                .put("type", "object")
                .put("required", new JSONArray().put("action").put("jwt_token"))
                .put("properties", new JSONObject()
                    .put("action", prop("string", "validate | get_config | set_config"))
                    .put("jwt_token", prop("string", "Keycloak Bearer JWT"))
                    .put("tenant_id", prop("string", "Tenant for config lookup"))
                    .put("proposed_rate", prop("number", "Royalty rate to validate"))
                    .put("proposed_term_years", prop("integer", "Term to validate"))
                    .put("min_rate", prop("number", "Custom min rate for set_config (admin only)"))
                    .put("max_rate", prop("number", "Custom max rate for set_config (admin only)"))
                    .put("min_term_years", prop("integer", "Custom min term for set_config (admin only)"))
                    .put("max_term_years", prop("integer", "Custom max term for set_config (admin only)"))
                )));

        // 6. minor_candidate
        tools.put(buildToolDef("minor_candidate",
            "Handle minor candidate IP protection: check minor status, record guardian consent, " +
            "and trigger age-18 ownership transfer. Enforces Indian Contract Act Section 11 compliance. " +
            "Required role: ecoskiller:licensing:create or system (for ownership_transfer).",
            new JSONObject()
                .put("type", "object")
                .put("required", new JSONArray().put("action").put("candidate_id").put("jwt_token"))
                .put("properties", new JSONObject()
                    .put("action", prop("string", "check_minor_status | record_guardian_consent | trigger_ownership_transfer"))
                    .put("candidate_id", prop("string", "UUID of the candidate"))
                    .put("jwt_token", prop("string", "Keycloak Bearer JWT"))
                    .put("contract_id", prop("string", "UUID of the contract (for consent)"))
                    .put("guardian_id", prop("string", "Keycloak ID of the guardian"))
                    .put("consent_reference_id", prop("string", "DPDPA consent reference from Innovation Trust Governance"))
                    .put("tenant_id", prop("string", "Tenant identifier"))
                )));

        // 7. contract_search
        tools.put(buildToolDef("contract_search",
            "Search and filter contracts with pagination. Returns metadata only — no document content. " +
            "Pre-signed MinIO URLs (15-min TTL) available via contract_lifecycle get. " +
            "Required role: ecoskiller:viewer.",
            new JSONObject()
                .put("type", "object")
                .put("required", new JSONArray().put("jwt_token"))
                .put("properties", new JSONObject()
                    .put("jwt_token", prop("string", "Keycloak Bearer JWT"))
                    .put("status", prop("string", "Filter: DRAFT | PENDING_REVIEW | PENDING_SIGNATURE | ACTIVE | SUSPENDED | TERMINATED"))
                    .put("idea_id", prop("string", "Filter by idea UUID"))
                    .put("candidate_id", prop("string", "Filter by candidate UUID"))
                    .put("licensee_business_id", prop("string", "Filter by business UUID"))
                    .put("tenant_id", prop("string", "Filter by tenant"))
                    .put("date_from", prop("string", "ISO-8601 start date"))
                    .put("date_to", prop("string", "ISO-8601 end date"))
                    .put("page", prop("integer", "Page number, default 1"))
                    .put("page_size", prop("integer", "Results per page, max 100, default 20"))
                )));

        // 8. contract_versions
        tools.put(buildToolDef("contract_versions",
            "Retrieve the full immutable version history of a contract from royalty.contract_versions. " +
            "Each version includes changed_fields, actor, change_reason, version_document_url, and timestamp. " +
            "Required role: ecoskiller:viewer.",
            new JSONObject()
                .put("type", "object")
                .put("required", new JSONArray().put("contract_id").put("jwt_token"))
                .put("properties", new JSONObject()
                    .put("contract_id", prop("string", "UUID of the contract"))
                    .put("jwt_token", prop("string", "Keycloak Bearer JWT"))
                    .put("version_number", prop("integer", "Optional: retrieve a specific version"))
                )));

        // 9. compliance_audit
        tools.put(buildToolDef("compliance_audit",
            "Write to or query the ClickHouse royalty_audit_log. Records all state transitions, " +
            "amendments, and access events with actor_id, action, contract_id, and timestamp. " +
            "Actions: log_event | query_audit_trail | check_worm_archive. " +
            "Required role: ecoskiller:licensing:admin for query; system for log_event.",
            new JSONObject()
                .put("type", "object")
                .put("required", new JSONArray().put("action").put("jwt_token"))
                .put("properties", new JSONObject()
                    .put("action", prop("string", "log_event | query_audit_trail | check_worm_archive | trigger_archive"))
                    .put("jwt_token", prop("string", "Keycloak Bearer JWT"))
                    .put("contract_id", prop("string", "UUID of the contract"))
                    .put("tenant_id", prop("string", "Tenant identifier"))
                    .put("actor_id", prop("string", "Keycloak user ID performing the action"))
                    .put("audit_action", prop("string", "CONTRACT_CREATED | STATE_TRANSITIONED | AMENDED | SIGNED | TERMINATED | ARCHIVED"))
                    .put("previous_state", prop("string", "Previous contract state"))
                    .put("new_state", prop("string", "New contract state"))
                    .put("metadata_json", prop("string", "JSON string of additional audit metadata"))
                    .put("date_from", prop("string", "ISO-8601 start for query"))
                    .put("date_to", prop("string", "ISO-8601 end for query"))
                )));

        // 10. kafka_events
        tools.put(buildToolDef("kafka_events",
            "Simulate or inspect Kafka event flow for the licensing-contract-service. " +
            "Publish contract lifecycle events or consume/inspect DLQ entries. " +
            "Topics: licensing.contract.signed, .amended, .terminated, .signature.timeout, " +
            "licensing.ownership.transferred, document.generation.requested, " +
            "signature.requested, archive.requested. " +
            "Required role: ecoskiller:licensing:admin.",
            new JSONObject()
                .put("type", "object")
                .put("required", new JSONArray().put("action").put("jwt_token"))
                .put("properties", new JSONObject()
                    .put("action", prop("string", "publish_event | inspect_dlq | list_topics"))
                    .put("jwt_token", prop("string", "Keycloak Bearer JWT"))
                    .put("topic", prop("string", "Kafka topic name"))
                    .put("event_type", prop("string", "Event type to publish"))
                    .put("contract_id", prop("string", "Contract UUID for event payload"))
                    .put("tenant_id", prop("string", "Tenant for event payload"))
                    .put("payload_json", prop("string", "Full event payload as JSON string"))
                )));

        return rpcHandler.success(id, new JSONObject().put("tools", tools));
    }

    // ── tools/call ─────────────────────────────────────────────────────────────

    private JSONObject handleToolsCall(Object id, JSONObject params) {
        String toolName = params.optString("name", "");
        JSONObject args = params.optJSONObject("arguments");
        if (args == null) args = new JSONObject();

        // Security: validate JWT before any tool invocation
        String jwt = args.optString("jwt_token", "");
        String secErr = securityManager.validateJwt(jwt);
        if (secErr != null) {
            return rpcHandler.toolError(id, "AUTH_FAILED: " + secErr);
        }

        try {
            String result = switch (toolName) {
                case "contract_lifecycle"      -> contractLifecycleTool.execute(args);
                case "contract_amendment"      -> contractAmendmentTool.execute(args);
                case "contract_termination"    -> contractTerminationTool.execute(args);
                case "signature_orchestration" -> signatureTool.execute(args);
                case "royalty_rate_validation" -> royaltyRateTool.execute(args);
                case "minor_candidate"         -> minorCandidateTool.execute(args);
                case "contract_search"         -> contractSearchTool.execute(args);
                case "contract_versions"       -> contractVersionsTool.execute(args);
                case "compliance_audit"        -> complianceAuditTool.execute(args);
                case "kafka_events"            -> kafkaEventTool.execute(args);
                default                        -> throw new IllegalArgumentException("Unknown tool: " + toolName);
            };
            return rpcHandler.toolSuccess(id, result);
        } catch (IllegalArgumentException e) {
            return rpcHandler.toolError(id, "VALIDATION_ERROR: " + e.getMessage());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[MCP] Tool execution error: " + toolName, e);
            return rpcHandler.toolError(id, "INTERNAL_ERROR: " + e.getMessage());
        }
    }

    // ── Helpers ─────────────────────────────────────────────────────────────────

    private JSONObject buildToolDef(String name, String description, JSONObject inputSchema) {
        return new JSONObject()
                .put("name", name)
                .put("description", description)
                .put("inputSchema", inputSchema);
    }

    private JSONObject prop(String type, String description) {
        return new JSONObject().put("type", type).put("description", description);
    }
}
