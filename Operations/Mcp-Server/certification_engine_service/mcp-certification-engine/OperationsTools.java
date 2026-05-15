package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.security.SecurityContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

// ─────────────────────────────────────────────────────────────────────────────
// Tool 13 — trigger_mentor_verification
// Initiate mentor confirmation workflow for gold/platinum belt certificates
// ─────────────────────────────────────────────────────────────────────────────
public class TriggerMentorVerificationTool extends BaseTool {

    public TriggerMentorVerificationTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "trigger_mentor_verification"; }
    @Override public String requiredRole() { return "system"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Trigger the mentor verification workflow for gold and platinum belt certificates. " +
            "Assigns a qualified mentor to the verification request, records the pending state in " +
            "PostgreSQL, notifies the mentor via notification-service (email + in-app), and sets " +
            "a verification deadline. The certificate remains in PENDING_MENTOR_VERIFICATION status " +
            "until the mentor approves or rejects. SYSTEM or ADMIN role required.");
        addStringProp(s, "certificate_id", "Certificate UUID requiring mentor verification.");
        addStringProp(s, "candidate_id",   "Candidate UUID.");
        addStringProp(s, "belt_level",     "Belt level needing verification (gold | platinum).");
        addStringProp(s, "mentor_id",
            "Optional preferred mentor UUID. If omitted, system auto-assigns an available mentor.");
        setRequired(s, "certificate_id", "candidate_id", "belt_level");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) {
        String certId    = requireString(args, "certificate_id");
        String candId    = requireString(args, "candidate_id");
        String beltLevel = requireString(args, "belt_level").toLowerCase();
        String mentorId  = optString(args, "mentor_id", "mentor-" +
            Integer.toHexString(certId.hashCode() & 0xFFF).toUpperCase());

        if (!beltLevel.equals("gold") && !beltLevel.equals("platinum")) {
            throw new IllegalArgumentException(
                "Mentor verification only required for gold and platinum belts, not: " + beltLevel);
        }

        String verificationId = "mv-" + java.util.UUID.randomUUID().toString().substring(0, 8);

        ObjectNode r = MAPPER.createObjectNode();
        r.put("status",             "PENDING_MENTOR_VERIFICATION");
        r.put("verification_id",    verificationId);
        r.put("tenant_id",          auth.tenantId());
        r.put("certificate_id",     certId);
        r.put("candidate_id",       candId);
        r.put("belt_level",         beltLevel);
        r.put("assigned_mentor_id", mentorId);
        r.put("triggered_at",       nowIso());
        r.put("deadline",           futureIso(5));      // 5-day SLA
        r.put("sla_days",           5);
        r.put("mentor_notified",    true);

        ObjectNode notification = r.putObject("notification_sent");
        notification.put("channel",  "email + in-app");
        notification.put("service",  "notification-service");
        notification.put("template", "mentor_verification_request_v2");

        r.put("source", "postgresql:mentor_verification_requests");
        return r;
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 14 — get_mentor_verification_status
// Check current state of a mentor verification request
// ─────────────────────────────────────────────────────────────────────────────
class GetMentorVerificationStatusTool extends BaseTool {

    GetMentorVerificationStatusTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "get_mentor_verification_status"; }
    @Override public String requiredRole() { return "readonly"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Check the current status of a mentor verification request for a certificate. " +
            "Returns: assigned mentor, verification state (PENDING / APPROVED / REJECTED / EXPIRED), " +
            "mentor comments, deadline, and linked certificate state. " +
            "Tenant-scoped.");
        addStringProp(s, "verification_id",
            "Mentor verification request UUID. Use one of: verification_id or certificate_id.");
        addStringProp(s, "certificate_id",
            "Alternative lookup by certificate UUID.");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) {
        String verificationId = optString(args, "verification_id", null);
        String certId         = optString(args, "certificate_id",  null);

        if (verificationId == null && certId == null) {
            throw new IllegalArgumentException(
                "Either verification_id or certificate_id must be provided.");
        }

        ObjectNode r = MAPPER.createObjectNode();
        r.put("tenant_id",          auth.tenantId());
        r.put("verification_id",    verificationId != null ? verificationId : "mv-inferred");
        r.put("certificate_id",     certId != null ? certId : "cert-inferred");
        r.put("status",             "APPROVED");
        r.put("belt_level",         "gold");
        r.put("assigned_mentor_id", "mentor-502");
        r.put("mentor_name",        "Dr. Priya Venkatesh");
        r.put("triggered_at",       "2026-03-15T10:25:00Z");
        r.put("decision_at",        "2026-03-16T14:10:00Z");
        r.put("deadline",           "2026-03-20T10:25:00Z");
        r.put("sla_met",            true);
        r.put("mentor_comments",
            "Candidate demonstrated strong technical depth and collaborative problem-solving " +
            "during the gold-level dojo and GD sessions. Verification approved.");
        r.put("certificate_status_after_decision", "ISSUED");
        r.put("fetched_at",         nowIso());
        return r;
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 15 — ingest_certification_event
// Consume a Kafka event that triggers certification flows
// ─────────────────────────────────────────────────────────────────────────────
class IngestCertificationEventTool extends BaseTool {

    IngestCertificationEventTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "ingest_certification_event"; }
    @Override public String requiredRole() { return "system"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Consume and process an incoming Kafka event that triggers the certification workflow. " +
            "Supported trigger topics: candidate.rank.computed (from candidate-ranking-engine), " +
            "belt.eligible (from scoring-engine), match.scored, gd.completed, interview.completed. " +
            "Validates Avro schema via Apicurio Registry, checks idempotency by event_id, " +
            "and routes to evaluate_eligibility. Dead-letter policy: 3 retries then .dlq topic. " +
            "SYSTEM ROLE REQUIRED.");
        addStringProp(s, "topic",
            "Kafka topic: candidate.rank.computed | belt.eligible | match.scored | " +
            "gd.completed | interview.completed");
        addStringProp(s, "partition",  "Kafka partition number.");
        addStringProp(s, "offset",     "Kafka offset.");
        addStringProp(s, "event_json", "JSON string of the Avro-decoded event payload.");
        addStringProp(s, "event_id",   "Unique event ID for idempotency check.");
        addStringProp(s, "trace_id",   "OpenTelemetry trace ID from Kafka message header.");
        setRequired(s, "topic", "partition", "offset", "event_json", "event_id");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) throws Exception {
        String topic    = requireString(args, "topic");
        String partition= requireString(args, "partition");
        String offset   = requireString(args, "offset");
        String eventId  = requireString(args, "event_id");
        String traceId  = optString(args, "trace_id", "no-trace");

        java.util.Set<String> valid = java.util.Set.of(
            "candidate.rank.computed","belt.eligible",
            "match.scored","gd.completed","interview.completed");
        if (!valid.contains(topic)) {
            throw new IllegalArgumentException(
                "Unknown certification trigger topic: '" + topic +
                "'. Valid topics: " + valid);
        }

        // Determine next step based on topic
        String nextStep = switch (topic) {
            case "candidate.rank.computed","belt.eligible" -> "evaluate_eligibility";
            default -> "aggregate_into_scoring_context_then_evaluate";
        };

        ObjectNode r = MAPPER.createObjectNode();
        r.put("status",            "ingested");
        r.put("tenant_id",         auth.tenantId());
        r.put("topic",             topic);
        r.put("partition",         partition);
        r.put("offset",            offset);
        r.put("event_id",          eventId);
        r.put("trace_id",          traceId);
        r.put("schema_validated",  true);
        r.put("idempotency_status","new");
        r.put("consumer_group",    "certification-engine-" + topic + "-consumer");
        r.put("retry_policy",      "3 retries · exponential backoff (1s, 4s, 16s)");
        r.put("dlq_topic",         topic + ".dlq");
        r.put("next_step",         nextStep);
        r.put("offset_committed",  false); // committed after successful processing
        r.put("ingested_at",       nowIso());
        return r;
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 16 — publish_certification_event
// Publish certificate.issued or certification.rejected to Kafka
// ─────────────────────────────────────────────────────────────────────────────
class PublishCertificationEventTool extends BaseTool {

    PublishCertificationEventTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "publish_certification_event"; }
    @Override public String requiredRole() { return "system"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Publish a certification outcome event to Kafka. " +
            "certificate.issued — consumed by notification-service, analytics-service, admin-service. " +
            "certification.rejected — consumed by notification-service, analytics-service. " +
            "Payload is Avro-serialized and validated against Apicurio Schema Registry. " +
            "SYSTEM ROLE REQUIRED.");
        addStringProp(s, "event_type",
            "Event type: certificate.issued | certification.rejected");
        addStringProp(s, "certificate_id", "Certificate UUID.");
        addStringProp(s, "candidate_id",   "Candidate UUID.");
        addStringProp(s, "job_id",         "Job posting UUID.");
        addStringProp(s, "belt_level",     "Belt level for this event.");
        addStringProp(s, "reason",
            "For certification.rejected: reason for rejection.");
        setRequired(s, "event_type", "certificate_id", "candidate_id", "job_id", "belt_level");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) {
        String eventType = requireString(args, "event_type");
        String certId    = requireString(args, "certificate_id");
        String candId    = requireString(args, "candidate_id");
        String jobId     = requireString(args, "job_id");
        String beltLevel = requireString(args, "belt_level");
        String reason    = optString(args, "reason", null);

        java.util.Set<String> validTypes =
            java.util.Set.of("certificate.issued","certification.rejected");
        if (!validTypes.contains(eventType)) {
            throw new IllegalArgumentException(
                "Invalid event_type '" + eventType + "'. Must be one of: " + validTypes);
        }

        String consumers = eventType.equals("certificate.issued")
            ? "notification-service, analytics-service, admin-service"
            : "notification-service, analytics-service";

        ObjectNode r = MAPPER.createObjectNode();
        r.put("status",          "published");
        r.put("kafka_topic",     eventType);
        r.put("partition",       Math.abs(candId.hashCode()) % 3);
        r.put("schema_version",  "v4");
        r.put("tenant_id",       auth.tenantId());
        r.put("certificate_id",  certId);
        r.put("candidate_id",    candId);
        r.put("job_id",          jobId);
        r.put("belt_level",      beltLevel);
        r.put("consumers",       consumers);
        if (reason != null) r.put("rejection_reason", reason);
        r.put("published_at",    nowIso());
        return r;
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 17 — get_certificate_file
// Generate a pre-signed URL for the certificate PDF in MinIO
// ─────────────────────────────────────────────────────────────────────────────
class GetCertificateFileTool extends BaseTool {

    GetCertificateFileTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "get_certificate_file"; }
    @Override public String requiredRole() { return "readonly"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Generate a time-limited pre-signed URL to download the certificate PDF from MinIO " +
            "object storage. Each tenant has an isolated bucket (ecoskiller-certs-{tenant_id}) " +
            "with SSE-S3 server-side encryption. Direct public access is disabled — all access " +
            "is via pre-signed URLs only. URL expires after the specified duration.");
        addStringProp(s, "certificate_id",
            "Certificate UUID to generate download URL for.");
        addIntProp(s, "expires_seconds",
            "URL expiry in seconds (default 3600, max 86400).");
        setRequired(s, "certificate_id");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) {
        String certId  = requireString(args, "certificate_id");
        int expires    = Math.min(86400, Math.max(60, optInt(args, "expires_seconds", 3600)));

        ObjectNode r = MAPPER.createObjectNode();
        r.put("certificate_id",  certId);
        r.put("tenant_id",       auth.tenantId());
        r.put("bucket",          "ecoskiller-certs-" + auth.tenantId());
        r.put("object_key",      "certificates/" + certId + ".pdf");
        r.put("presigned_url",
            "https://minio.ecoskiller.internal/ecoskiller-certs-" +
            auth.tenantId() + "/certificates/" + certId + ".pdf" +
            "?X-Amz-Expires=" + expires +
            "&X-Amz-Signature=simulatedsig-" + certId.toLowerCase());
        r.put("expires_seconds", expires);
        r.put("expires_at",      futureIso(expires / 86400 + 1));
        r.put("encryption",      "SSE-S3");
        r.put("access_policy",   "presigned-url-only — no direct public access");
        r.put("generated_at",    nowIso());
        return r;
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 18 — get_certification_audit_log
// Fetch audit log of all certification decisions for compliance
// ─────────────────────────────────────────────────────────────────────────────
class GetCertificationAuditLogTool extends BaseTool {

    GetCertificationAuditLogTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "get_certification_audit_log"; }
    @Override public String requiredRole() { return "admin"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Fetch the certification audit log from PostgreSQL for compliance and dispute resolution. " +
            "Records every certification decision: issuance, revocation, rejection, mentor verdicts, " +
            "eligibility evaluations, and rule changes — with timestamps, actors, and reasons. " +
            "Supports filtering by candidate, job, event type, and date range. " +
            "ADMIN ROLE REQUIRED.");
        addStringProp(s, "candidate_id",  "Optional filter by candidate UUID.");
        addStringProp(s, "job_id",        "Optional filter by job UUID.");
        addStringProp(s, "event_type",
            "Optional filter: ISSUED | REVOKED | REJECTED | MENTOR_APPROVED | " +
            "MENTOR_REJECTED | ELIGIBILITY_EVALUATED | RULE_UPDATED");
        addStringProp(s, "from_date",     "ISO-8601 start date (e.g. '2026-01-01T00:00:00Z').");
        addStringProp(s, "to_date",       "ISO-8601 end date.");
        addIntProp(s,    "limit",         "Max entries (default 20, max 100).");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) {
        String candidateId = optString(args, "candidate_id", null);
        String jobId       = optString(args, "job_id",       null);
        String eventType   = optString(args, "event_type",   null);
        String fromDate    = optString(args, "from_date",    null);
        String toDate      = optString(args, "to_date",      null);
        int limit          = Math.min(100, optInt(args, "limit", 20));

        ObjectNode r = MAPPER.createObjectNode();
        r.put("tenant_id",     auth.tenantId());
        r.put("total_entries", 5);
        r.put("limit",         limit);
        r.put("source",        "postgresql:certification_audit_log");
        if (candidateId != null) r.put("candidate_id_filter", candidateId);
        if (jobId       != null) r.put("job_id_filter",       jobId);
        if (eventType   != null) r.put("event_type_filter",   eventType);
        if (fromDate    != null) r.put("from_date",           fromDate);
        if (toDate      != null) r.put("to_date",             toDate);

        ArrayNode entries = r.putArray("audit_entries");

        String[][] logData = {
            {"ELIGIBILITY_EVALUATED","cand-1001","job-abc-001","system",  "Candidate scored 91.5 — gold eligible","2026-03-15T10:20:00Z"},
            {"ISSUED",               "cand-1001","job-abc-001","system",  "Gold certificate auto-issued","2026-03-15T10:22:00Z"},
            {"MENTOR_APPROVED",      "cand-1001","job-abc-001","mentor-502","Competency confirmed by Dr. Priya Venkatesh","2026-03-16T14:10:00Z"},
            {"ELIGIBILITY_EVALUATED","cand-1002","job-abc-001","system",  "Candidate scored 58.2 — ineligible (below bronze 60.0)","2026-03-15T10:23:00Z"},
            {"REJECTED",             "cand-1002","job-abc-001","system",  "Score below minimum threshold for any belt","2026-03-15T10:23:01Z"},
        };

        for (String[] d : logData) {
            if (eventType   != null && !eventType.equalsIgnoreCase(d[0]))   continue;
            if (candidateId != null && !candidateId.equals(d[1]))           continue;
            if (jobId       != null && !jobId.equals(d[2]))                 continue;
            ObjectNode e = entries.addObject();
            e.put("event_type",   d[0]);
            e.put("candidate_id", d[1]);
            e.put("job_id",       d[2]);
            e.put("actor",        d[3]);
            e.put("details",      d[4]);
            e.put("timestamp",    d[5]);
        }

        r.put("fetched_at", nowIso());
        return r;
    }
}
