package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.protocol.JsonUtil;

import java.util.*;

/**
 * Central dispatcher for all 16 Application-Service MCP tools.
 *
 * Tool categories:
 *   Core CRUD       : submit_application, get_application, list_applications,
 *                     update_application_status, withdraw_application
 *   Scoring         : process_score_event, evaluate_belt_eligibility
 *   Audit           : get_application_audit_log, export_compliance_report
 *   Search          : search_applications, check_duplicate_application
 *   Analytics       : get_application_statistics
 *   Recruiter       : get_recruiter_pipeline, bulk_update_status
 *   Pipeline        : get_assessment_pipeline_status
 *   Validation      : validate_application_eligibility
 */
public class ToolRouter {

    private final ApplicationCrudTools  crudTools;
    private final ScoringTools          scoringTools;
    private final AuditTools            auditTools;
    private final AnalyticsTools        analyticsTools;
    private final RecruiterTools        recruiterTools;

    public ToolRouter() {
        this.crudTools      = new ApplicationCrudTools();
        this.scoringTools   = new ScoringTools();
        this.auditTools     = new AuditTools();
        this.analyticsTools = new AnalyticsTools();
        this.recruiterTools = new RecruiterTools();
    }

    // -------------------------------------------------------------------------
    // Dispatch
    // -------------------------------------------------------------------------

    public String dispatch(String toolName, Map<String, Object> args) {
        switch (toolName) {
            // --- Core CRUD ---
            case "submit_application":          return crudTools.submitApplication(args);
            case "get_application":             return crudTools.getApplication(args);
            case "list_applications":           return crudTools.listApplications(args);
            case "update_application_status":   return crudTools.updateStatus(args);
            case "withdraw_application":        return crudTools.withdrawApplication(args);
            case "validate_application_eligibility": return crudTools.validateEligibility(args);
            case "check_duplicate_application": return crudTools.checkDuplicate(args);

            // --- Scoring ---
            case "process_score_event":         return scoringTools.processScoreEvent(args);
            case "evaluate_belt_eligibility":   return scoringTools.evaluateBeltEligibility(args);

            // --- Audit & Compliance ---
            case "get_application_audit_log":   return auditTools.getAuditLog(args);
            case "export_compliance_report":    return auditTools.exportComplianceReport(args);

            // --- Analytics ---
            case "get_application_statistics":  return analyticsTools.getStatistics(args);
            case "search_applications":         return analyticsTools.searchApplications(args);

            // --- Recruiter ---
            case "get_recruiter_pipeline":      return recruiterTools.getRecruiterPipeline(args);
            case "bulk_update_status":          return recruiterTools.bulkUpdateStatus(args);
            case "get_assessment_pipeline_status": return recruiterTools.getAssessmentPipelineStatus(args);

            default:
                throw new IllegalArgumentException("Unknown tool: " + toolName);
        }
    }

    // -------------------------------------------------------------------------
    // Tool definitions (for tools/list response)
    // -------------------------------------------------------------------------

    public List<Map<String, Object>> getToolsDefinition() {
        List<Map<String, Object>> tools = new ArrayList<>();

        tools.add(buildTool("submit_application",
            "Submit a new candidate job application. Validates eligibility, enforces idempotency, " +
            "publishes job.applied Kafka event, and persists the record with APPLIED status. " +
            "Requires: auth_token (CANDIDATE role), tenant_id, job_id, candidate_id, idempotency_key.",
            prop("auth_token",      "string",  "Bearer JWT token from Keycloak (CANDIDATE role required)"),
            prop("tenant_id",       "string",  "Tenant identifier (multi-tenant isolation)"),
            prop("job_id",          "string",  "Target job posting ID"),
            prop("candidate_id",    "string",  "Candidate's user ID"),
            prop("assessment_type", "string",  "Assessment type: GD | INTERVIEW | DOJO | AI_EVAL | NONE"),
            prop("idempotency_key", "string",  "Unique key to prevent duplicate submissions"),
            prop("cover_note",      "string",  "Optional candidate cover note (max 5000 chars)")
        ));

        tools.add(buildTool("get_application",
            "Retrieve a single application by ID. Enforces tenant isolation and role-based access: " +
            "candidates can only see their own application; recruiters can see applications for their jobs.",
            prop("auth_token",      "string",  "Bearer JWT token"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("application_id",  "string",  "Application UUID to retrieve")
        ));

        tools.add(buildTool("list_applications",
            "List applications with cursor-based pagination and filters. RECRUITERs see only their jobs; " +
            "CANDIDATEs see only their own applications; ADMINs see all.",
            prop("auth_token",      "string",  "Bearer JWT token"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("status",          "string",  "Filter by status: APPLIED|SCREENED|ASSESSED|HIRED|REJECTED"),
            prop("assessment_type", "string",  "Filter by assessment type"),
            prop("recruiter_id",    "string",  "Filter by recruiter ID (ADMIN only)"),
            prop("min_score",       "number",  "Minimum overall score filter"),
            prop("max_score",       "number",  "Maximum overall score filter"),
            prop("page_size",       "number",  "Results per page (default: 20, max: 100)"),
            prop("cursor",          "string",  "Pagination cursor from previous response")
        ));

        tools.add(buildTool("update_application_status",
            "Update application status following the state machine: " +
            "APPLIED→SCREENED→ASSESSED→HIRED|REJECTED. Invalid transitions are rejected. " +
            "Publishes appropriate Kafka event on each transition. RECRUITER/ADMIN only.",
            prop("auth_token",      "string",  "Bearer JWT token (RECRUITER or ADMIN)"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("application_id",  "string",  "Application UUID"),
            prop("new_status",      "string",  "Target status: SCREENED|ASSESSED|HIRED|REJECTED"),
            prop("reason",          "string",  "Recruiter notes or rejection reason")
        ));

        tools.add(buildTool("withdraw_application",
            "Soft-delete an application (candidate withdrawal). Sets status to WITHDRAWN, " +
            "marks deleted=true, and logs the action. Cannot withdraw a HIRED application.",
            prop("auth_token",      "string",  "Bearer JWT token (CANDIDATE who owns it, or ADMIN)"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("application_id",  "string",  "Application UUID to withdraw")
        ));

        tools.add(buildTool("validate_application_eligibility",
            "Pre-submission eligibility check: verifies job exists, candidate is not blacklisted, " +
            "no duplicate application, and assessment quota not exceeded. Returns ELIGIBLE or INELIGIBLE with reasons.",
            prop("auth_token",      "string",  "Bearer JWT token"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("job_id",          "string",  "Job posting ID"),
            prop("candidate_id",    "string",  "Candidate user ID")
        ));

        tools.add(buildTool("check_duplicate_application",
            "Check whether a candidate has already applied to a specific job in this tenant. " +
            "Used for idempotency guard before submission.",
            prop("auth_token",      "string",  "Bearer JWT token"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("job_id",          "string",  "Job posting ID"),
            prop("candidate_id",    "string",  "Candidate user ID")
        ));

        tools.add(buildTool("process_score_event",
            "Consume a score_computed Kafka event from the Scoring Engine. Updates the application's " +
            "overall_score, dimension_scores, and triggers the application.assessed Kafka event. " +
            "Also initiates belt eligibility evaluation.",
            prop("auth_token",      "string",  "Bearer JWT token (ADMIN or service account)"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("application_id",  "string",  "Application UUID"),
            prop("session_id",      "string",  "Assessment session ID from Scoring Engine"),
            prop("overall_score",   "number",  "Composite score (0.0 – 100.0)"),
            prop("dimension_scores","string",  "JSON object: {communication, technical, leadership, ...}"),
            prop("assessment_type", "string",  "Assessment type: GD | INTERVIEW | DOJO | AI_EVAL")
        ));

        tools.add(buildTool("evaluate_belt_eligibility",
            "Evaluate whether a candidate meets belt eligibility criteria based on their scores. " +
            "Returns recommended belt level (NONE|BRONZE|SILVER|GOLD|PLATINUM) and gap analysis.",
            prop("auth_token",      "string",  "Bearer JWT token"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("application_id",  "string",  "Application UUID with scores to evaluate"),
            prop("target_belt",     "string",  "Optional: BRONZE|SILVER|GOLD|PLATINUM (defaults to auto-recommend)")
        ));

        tools.add(buildTool("get_application_audit_log",
            "Retrieve the immutable audit trail for an application. Includes every state change, " +
            "score update, actor ID, and timestamp. Required for DPDPA 2023 / GDPR compliance.",
            prop("auth_token",      "string",  "Bearer JWT token (RECRUITER, ADMIN, or owning CANDIDATE)"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("application_id",  "string",  "Application UUID")
        ));

        tools.add(buildTool("export_compliance_report",
            "Export a tenant's application data for DPDPA 2023 compliance, GDPR right-to-access " +
            "requests, and GST billing reconciliation. Includes soft-deleted records. ADMIN only.",
            prop("auth_token",      "string",  "Bearer JWT token (ADMIN only)"),
            prop("tenant_id",       "string",  "Tenant to export"),
            prop("candidate_id",    "string",  "Optional: filter to a specific candidate (GDPR request)"),
            prop("from_date",       "string",  "ISO date filter start (YYYY-MM-DD)"),
            prop("to_date",         "string",  "ISO date filter end (YYYY-MM-DD)")
        ));

        tools.add(buildTool("get_application_statistics",
            "Funnel analytics: application counts per status, conversion rates, average scores " +
            "by assessment type. Backed by ClickHouse in production.",
            prop("auth_token",      "string",  "Bearer JWT token (RECRUITER or ADMIN)"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("recruiter_id",    "string",  "Optional: scope stats to a specific recruiter")
        ));

        tools.add(buildTool("search_applications",
            "Full-text search across candidate name, job title, and notes. " +
            "Delegates to OpenSearch via search-indexer-service in production.",
            prop("auth_token",      "string",  "Bearer JWT token"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("query",           "string",  "Search query string"),
            prop("page_size",       "number",  "Results per page (default: 10)")
        ));

        tools.add(buildTool("get_recruiter_pipeline",
            "Recruiter dashboard view: all applications scoped to this recruiter's jobs, " +
            "grouped by status with assessment progress indicators.",
            prop("auth_token",      "string",  "Bearer JWT token (RECRUITER or ADMIN)"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("recruiter_id",    "string",  "Recruiter user ID"),
            prop("job_id",          "string",  "Optional: filter to a specific job")
        ));

        tools.add(buildTool("bulk_update_status",
            "Batch status update for multiple applications in a single operation. " +
            "Validates each transition, publishes Kafka events per application. RECRUITER/ADMIN only.",
            prop("auth_token",      "string",  "Bearer JWT token (RECRUITER or ADMIN)"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("application_ids", "string",  "Comma-separated list of application UUIDs"),
            prop("new_status",      "string",  "Target status for all applications"),
            prop("reason",          "string",  "Batch update reason / recruiter notes")
        ));

        tools.add(buildTool("get_assessment_pipeline_status",
            "Returns the full multi-stage assessment pipeline status for an application: " +
            "which assessments are pending, in-progress, or complete (GD→Interview→Dojo).",
            prop("auth_token",      "string",  "Bearer JWT token"),
            prop("tenant_id",       "string",  "Tenant identifier"),
            prop("application_id",  "string",  "Application UUID")
        ));

        return tools;
    }

    // -------------------------------------------------------------------------
    // Schema helpers
    // -------------------------------------------------------------------------

    private Map<String, Object> buildTool(String name, String description, Map<String, Object>... props) {
        Map<String, Object> tool = new LinkedHashMap<>();
        tool.put("name",        name);
        tool.put("description", description);

        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");

        Map<String, Object> properties = new LinkedHashMap<>();
        List<String>        required   = new ArrayList<>();
        required.add("auth_token");
        required.add("tenant_id");

        for (Map<String, Object> p : props) {
            String pName = (String) p.get("_name");
            p.remove("_name");
            properties.put(pName, p);
        }
        schema.put("properties", properties);
        schema.put("required",   required);
        tool.put("inputSchema", schema);
        return tool;
    }

    private Map<String, Object> prop(String name, String type, String description) {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("_name",       name);
        p.put("type",        type);
        p.put("description", description);
        return p;
    }
}
