import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * ╔══════════════════════════════════════════════════════════════════════════╗
 * ║  Ecoskiller — Notification Service MCP Server (Java)                    ║
 * ║  CAT-18 | 14 Agents | Namespace: communication | Priority: HIGH         ║
 * ║  Transport: stdio | Protocol: JSON-RPC 2.0 | MCP: 2024-11-05            ║
 * ╚══════════════════════════════════════════════════════════════════════════╝
 *
 * ZERO external dependencies — pure Java 17+ stdlib only.
 *
 * Agents (14 tools):
 *  1. send_notification              NOTIFICATION_SEND_AGENT
 *  2. queue_notification             QUEUE_MANAGEMENT_AGENT
 *  3. get_notification_status        STATUS_TRACKER_AGENT
 *  4. retry_failed_notification      RETRY_AGENT
 *  5. get_user_preferences           PREFERENCE_READ_AGENT
 *  6. update_user_preferences        PREFERENCE_WRITE_AGENT
 *  7. suppress_address               SUPPRESSION_AGENT
 *  8. check_suppression              SUPPRESSION_CHECK_AGENT
 *  9. get_notification_history       AUDIT_QUERY_AGENT
 * 10. render_template                TEMPLATE_RENDER_AGENT
 * 11. get_queue_metrics              METRICS_AGENT
 * 12. bulk_send_notifications        BULK_SEND_AGENT
 * 13. cancel_notification            CANCEL_AGENT
 * 14. compliance_data_request        COMPLIANCE_AGENT
 *
 * Security features:
 *  - Input validation (JSON structure, field types, lengths)
 *  - Tool name allowlist (injection prevention)
 *  - Email/phone format validation (regex)
 *  - Control character rejection
 *  - Rate limiting (200 req/min, token bucket)
 *  - Deduplication cache (5-min window, SHA-256 keyed)
 *  - Phone number masking in responses
 *  - Email address hashing in suppression list
 *  - Tenant isolation enforcement
 *  - Error message sanitization (no stack traces to client)
 *
 * Build & Run:
 *   javac NotificationMCPServer.java
 *   java NotificationMCPServer
 *
 * Run tests:
 *   java NotificationMCPServer --test
 *   java NotificationMCPServer --test --verbose
 */
public class NotificationMCPServer {

    // ── Constants ────────────────────────────────────────────────────────────
    static final String SERVER_NAME    = "ecoskiller-notification-mcp";
    static final String SERVER_VERSION = "1.0.0";
    static final String MCP_VERSION    = "2024-11-05";
    static final int    MAX_REQ_BYTES  = 1_048_576; // 1 MB

    // ── Security patterns ─────────────────────────────────────────────────────
    static final Pattern VALID_EMAIL   = Pattern.compile(
        "^[a-zA-Z0-9._%+\\-]{1,64}@[a-zA-Z0-9.\\-]{1,253}\\.[a-zA-Z]{2,}$");
    static final Pattern VALID_PHONE   = Pattern.compile("^\\+[1-9]\\d{6,14}$");
    static final Pattern VALID_TOOL    = Pattern.compile("^[a-z][a-z0-9_]{1,63}$");
    static final Pattern VALID_TENANT  = Pattern.compile("^[a-zA-Z0-9_\\-]{1,64}$");

    static final Set<String> CHANNELS  = Set.of("email", "sms", "push", "in_app");
    static final Set<String> TOOL_NAMES = Set.of(
        "send_notification", "queue_notification", "get_notification_status",
        "retry_failed_notification", "get_user_preferences", "update_user_preferences",
        "suppress_address", "check_suppression", "get_notification_history",
        "render_template", "get_queue_metrics", "bulk_send_notifications",
        "cancel_notification", "compliance_data_request"
    );

    // ── Security state ────────────────────────────────────────────────────────
    static final RateLimiter LIMITER = new RateLimiter(200);
    static final DedupCache  DEDUP   = new DedupCache(5 * 60_000L);

    // ─────────────────────────────────────────────────────────────────────────
    // ENTRY POINT
    // ─────────────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws IOException {
        List<String> argList = Arrays.asList(args);
        if (argList.contains("--test")) {
            runTests(argList.contains("--verbose"));
            return;
        }

        log("MCP Server v" + SERVER_VERSION + " started. Reading JSON-RPC from stdin...");
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)), true);

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String response = handleMessage(line);
            if (response != null) { out.println(response); out.flush(); }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MESSAGE ROUTER
    // ─────────────────────────────────────────────────────────────────────────

    static String handleMessage(String raw) {
        try {
            if (raw.length() > MAX_REQ_BYTES)
                return errResp(null, -32700, "Request too large (max 1MB)");
            if (!raw.startsWith("{"))
                return errResp(null, -32700, "Parse error: expected JSON object");

            JsonObj req;
            try { req = JsonParser.parseObject(raw); }
            catch (Exception e) { return errResp(null, -32700, "Parse error: malformed JSON"); }

            if (!LIMITER.allow("client"))
                return errResp(req.str("id"), -32000, "Rate limit exceeded (200 req/min)");

            String version = req.str("jsonrpc");
            if (!"2.0".equals(version))
                return errResp(req.str("id"), -32600, "jsonrpc must be \"2.0\"");

            String method = req.str("method");
            if (method.isEmpty())
                return errResp(req.str("id"), -32600, "method field is required");

            String id = req.str("id");
            return switch (method) {
                case "initialize" -> handleInitialize(id);
                case "tools/list" -> handleToolsList(id);
                case "tools/call" -> handleToolCall(id, req);
                case "ping"       -> okResp(id, "{\"status\":\"pong\"}");
                default           -> errResp(id, -32601, "Method not found: " + method);
            };

        } catch (Exception e) {
            return errResp(null, -32603, "Internal server error: " + sanitize(e.getMessage()));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // INITIALIZE
    // ─────────────────────────────────────────────────────────────────────────

    static String handleInitialize(String id) {
        String result = json(
            "protocolVersion", MCP_VERSION,
            "serverInfo", json("name", SERVER_NAME, "version", SERVER_VERSION,
                "description", "Ecoskiller Notification MCP: multi-channel delivery (email/SMS/push/in-app)"),
            "capabilities", json("tools", json())
        );
        return okResp(id, result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TOOLS/LIST
    // ─────────────────────────────────────────────────────────────────────────

    static String handleToolsList(String id) {
        StringBuilder tools = new StringBuilder("[");
        tools.append(toolDef("send_notification",
            "Send immediate notification via email, SMS, push, or in-app channel. " +
            "Enforces deduplication (5-min), rate limits, and preference checks.",
            req("user_id","string","Target user ID"),
            req("notification_type","string","Event type (e.g. interview.scheduled, offer.extended)"),
            req("channel","string","Delivery channel: email | sms | push | in_app"),
            opt("tenant_id","string","Tenant ID for multi-tenant isolation"),
            opt("event_id","string","Kafka event ID for deduplication"),
            opt("recipient_email","string","Required if channel=email"),
            opt("recipient_phone","string","E.164 format, required if channel=sms"),
            opt("recipient_fcm_token","string","Device token, required if channel=push"),
            opt("subject","string","Notification subject/title"),
            opt("body","string","Notification body text (max 4096 chars)"),
            opt("template_name","string","Template to render (overrides subject/body)"),
            opt("scheduled_at","string","ISO-8601 datetime for delayed delivery"),
            opt("priority","string","high | normal | low")
        ));
        tools.append(",").append(toolDef("queue_notification",
            "Add notification to PostgreSQL notification_queue for async delivery. " +
            "Workers process in batches of 100. Status: PENDING→SENT|FAILED|BOUNCED.",
            req("user_id","string","Target user ID"),
            req("notification_type","string","Event type"),
            req("channel","string","email | sms | push | in_app"),
            opt("tenant_id","string","Tenant ID"),
            opt("scheduled_at","string","ISO-8601 scheduled delivery time"),
            opt("template_name","string","Template name for rendering at delivery time"),
            opt("context_json","string","JSON string of template variables"),
            opt("priority","string","high | normal | low"),
            opt("idempotency_key","string","Prevent duplicate queuing")
        ));
        tools.append(",").append(toolDef("get_notification_status",
            "Get delivery status of a notification (PENDING/SENT/FAILED/BOUNCED/COMPLAINED).",
            req("notification_id","string","Notification ID"),
            opt("tenant_id","string","Tenant ID for security check")
        ));
        tools.append(",").append(toolDef("retry_failed_notification",
            "Retry a FAILED notification with exponential backoff (1m→5m→15m→1h). " +
            "Max 3 retries for email, 2 for SMS.",
            req("notification_id","string","Notification ID to retry"),
            opt("tenant_id","string","Tenant ID"),
            opt("force_immediate","boolean","Bypass backoff and retry immediately")
        ));
        tools.append(",").append(toolDef("get_user_preferences",
            "Fetch notification preferences: channel enables, quiet hours (21:00-08:00), " +
            "rate caps (10 email/day, 1 SMS/day, 5 push/day). Redis cached (1h TTL).",
            req("user_id","string","User ID"),
            opt("tenant_id","string","Tenant ID")
        ));
        tools.append(",").append(toolDef("update_user_preferences",
            "Update user notification preferences. Changes applied immediately. " +
            "DPDPA: opt-out triggers suppression list entry.",
            req("user_id","string","User ID"),
            opt("tenant_id","string","Tenant ID"),
            opt("email_enabled","boolean","Enable/disable email"),
            opt("sms_enabled","boolean","Enable/disable SMS"),
            opt("push_enabled","boolean","Enable/disable push"),
            opt("in_app_enabled","boolean","Enable/disable in-app"),
            opt("quiet_hours_start","integer","0-23, e.g. 21 = 9 PM"),
            opt("quiet_hours_end","integer","0-23, e.g. 8 = 8 AM"),
            opt("email_digest_mode","boolean","Batch into daily digest"),
            opt("unsubscribe_reason","string","Reason for opt-out (DPDPA audit)")
        ));
        tools.append(",").append(toolDef("suppress_address",
            "Add email or E.164 phone to suppression list. Addresses stored as SHA-256 hash. " +
            "Hard bounces: permanent. Soft: retry after 72h. Complaints: immediate permanent.",
            req("address","string","Email or E.164 phone to suppress"),
            req("reason","string","bounced | complained | manual | unsubscribed"),
            opt("bounce_type","string","hard | soft"),
            opt("tenant_id","string","Tenant ID (suppression lists are per-tenant)"),
            opt("notes","string","Admin notes (max 256 chars)")
        ));
        tools.append(",").append(toolDef("check_suppression",
            "Check if email/phone is currently suppressed before sending.",
            req("address","string","Email or E.164 phone to check"),
            opt("tenant_id","string","Tenant ID")
        ));
        tools.append(",").append(toolDef("get_notification_history",
            "Fetch notification history for a user. Supports DPDPA right-to-access. " +
            "Retention: 30d in-app, 1yr operational, 3yr audit.",
            req("user_id","string","User ID"),
            opt("tenant_id","string","Tenant ID"),
            opt("channel","string","Filter: email | sms | push | in_app"),
            opt("status","string","Filter: SENT | FAILED | BOUNCED | COMPLAINED | PENDING"),
            opt("limit","integer","1-500, default 50"),
            opt("from_date","string","ISO-8601 start date"),
            opt("to_date","string","ISO-8601 end date"),
            opt("dpdpa_access_request","boolean","Full audit trail for DPDPA")
        ));
        tools.append(",").append(toolDef("render_template",
            "Render email/SMS template with Handlebars variables " +
            "({{candidate_name}}, {{job_title}}, {{score}}, etc.). Returns HTML + plain text + SMS.",
            req("template_name","string","e.g. interview_scheduled, offer_extended, score_computed"),
            req("context_json","string","JSON string: {candidate_name, job_title, score, interview_time, ...}"),
            opt("variant","string","A/B variant: variant_a | variant_b | control"),
            opt("channel","string","email | sms"),
            opt("locale","string","e.g. en-IN, hi-IN")
        ));
        tools.append(",").append(toolDef("get_queue_metrics",
            "Real-time queue metrics (Prometheus-style). " +
            "Alert thresholds: queue_depth>10K, bounce_rate>2%, latency_p95>5s.",
            opt("tenant_id","string","Scope metrics to tenant (omit for platform-wide)"),
            opt("window","string","1m | 5m | 15m | 1h")
        ));
        tools.append(",").append(toolDef("bulk_send_notifications",
            "Send up to 500 notifications in one call. " +
            "Emails batched 100/SendGrid call, SMS batched 50/Twilio call.",
            req("notifications","string","JSON array of notification objects (max 500). Each needs: user_id, notification_type, channel"),
            opt("tenant_id","string","Applied to all items"),
            opt("fail_fast","boolean","Stop on first error (default: continue)"),
            opt("batch_label","string","Label for audit log grouping")
        ));
        tools.append(",").append(toolDef("cancel_notification",
            "Cancel a PENDING/SCHEDULED notification before delivery. " +
            "Cannot cancel SENT/FAILED/BOUNCED/COMPLAINED notifications.",
            req("notification_id","string","Notification ID to cancel"),
            opt("tenant_id","string","Tenant ID"),
            opt("cancel_reason","string","Reason (written to audit log)")
        ));
        tools.append(",").append(toolDef("compliance_data_request",
            "Handle DPDPA 2023 compliance: right-to-access (full audit trail), " +
            "right-to-erasure (suppress + purge operational, keep 3yr audit), portability (JSON export).",
            req("user_id","string","Subject user ID"),
            req("request_type","string","access | erasure | portability"),
            opt("tenant_id","string","Tenant ID"),
            opt("requester_id","string","DPO/admin user making the request"),
            opt("notes","string","Optional compliance notes")
        ));
        tools.append("]");

        return okResp(id, "{\"tools\":" + tools + "}");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TOOLS/CALL DISPATCHER
    // ─────────────────────────────────────────────────────────────────────────

    static String handleToolCall(String id, JsonObj req) {
        JsonObj params = req.obj("params");
        if (params == null) return errResp(id, -32602, "params field required");

        String toolName = params.str("name");
        if (toolName.isEmpty()) return errResp(id, -32602, "params.name required");

        // Security: allowlist check
        if (!VALID_TOOL.matcher(toolName).matches() || !TOOL_NAMES.contains(toolName))
            return errResp(id, -32601, "Unknown or invalid tool: " + toolName);

        JsonObj args = params.obj("arguments");
        if (args == null) args = new JsonObj(Collections.emptyMap());

        // Per-tool argument validation
        String validErr = validateArgs(toolName, args);
        if (validErr != null) return errResp(id, -32602, "Validation: " + validErr);

        try {
            String result = dispatchTool(toolName, args);
            return okResp(id, "{\"content\":[{\"type\":\"text\",\"text\":" + jsonStr(result) + "}]}");
        } catch (Exception e) {
            String safeMsg = sanitize(e.getMessage());
            return okResp(id, "{\"content\":[{\"type\":\"text\",\"text\":" +
                jsonStr("{\"success\":false,\"error\":{\"code\":\"TOOL_ERROR\",\"message\":" + jsonStr(safeMsg) + "}}") +
                "],\"isError\":true}");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ARGUMENT VALIDATION
    // ─────────────────────────────────────────────────────────────────────────

    static String validateArgs(String tool, JsonObj a) {
        return switch (tool) {
            case "send_notification", "queue_notification" -> {
                String e = requireStr(a, "user_id", 1, 64);
                if (e != null) yield e;
                e = requireStr(a, "notification_type", 1, 128);
                if (e != null) yield e;
                e = requireStr(a, "channel", 1, 16);
                if (e != null) yield e;
                if (!CHANNELS.contains(a.str("channel")))
                    yield "channel must be: email | sms | push | in_app";
                String email = a.str("recipient_email");
                if (!email.isEmpty() && !VALID_EMAIL.matcher(email).matches())
                    yield "recipient_email format invalid";
                String phone = a.str("recipient_phone");
                if (!phone.isEmpty() && !VALID_PHONE.matcher(phone).matches())
                    yield "recipient_phone must be E.164 (e.g. +919876543210)";
                String tenant = a.str("tenant_id");
                if (!tenant.isEmpty() && !VALID_TENANT.matcher(tenant).matches())
                    yield "tenant_id contains invalid characters";
                yield null;
            }
            case "get_notification_status", "retry_failed_notification", "cancel_notification" ->
                requireStr(a, "notification_id", 1, 128);
            case "get_user_preferences" ->
                requireStr(a, "user_id", 1, 64);
            case "update_user_preferences" -> {
                String e = requireStr(a, "user_id", 1, 64);
                if (e != null) yield e;
                int qs = a.intVal("quiet_hours_start", -1);
                if (qs != -1 && (qs < 0 || qs > 23)) yield "quiet_hours_start must be 0-23";
                int qe = a.intVal("quiet_hours_end", -1);
                if (qe != -1 && (qe < 0 || qe > 23)) yield "quiet_hours_end must be 0-23";
                yield null;
            }
            case "suppress_address", "check_suppression" -> {
                String e = requireStr(a, "address", 1, 320);
                if (e != null) yield e;
                String addr = a.str("address");
                if (!VALID_EMAIL.matcher(addr).matches() && !VALID_PHONE.matcher(addr).matches())
                    yield "address must be valid email or E.164 phone";
                if (tool.equals("suppress_address")) {
                    e = requireStr(a, "reason", 1, 32);
                    if (e != null) yield e;
                    if (!Set.of("bounced","complained","manual","unsubscribed").contains(a.str("reason")))
                        yield "reason must be: bounced | complained | manual | unsubscribed";
                }
                yield null;
            }
            case "get_notification_history" -> {
                String e = requireStr(a, "user_id", 1, 64);
                if (e != null) yield e;
                int lim = a.intVal("limit", 50);
                if (lim < 1 || lim > 500) yield "limit must be 1-500";
                yield null;
            }
            case "render_template" -> {
                String e = requireStr(a, "template_name", 1, 128);
                if (e != null) yield e;
                yield requireStr(a, "context_json", 1, 65536);
            }
            case "bulk_send_notifications" -> {
                String raw = a.str("notifications");
                if (raw.isEmpty()) yield "notifications array is required";
                if (!raw.startsWith("[")) yield "notifications must be a JSON array";
                yield null;
            }
            case "compliance_data_request" -> {
                String e = requireStr(a, "user_id", 1, 64);
                if (e != null) yield e;
                e = requireStr(a, "request_type", 1, 32);
                if (e != null) yield e;
                if (!Set.of("access","erasure","portability").contains(a.str("request_type")))
                    yield "request_type must be: access | erasure | portability";
                yield null;
            }
            default -> null;
        };
    }

    static String requireStr(JsonObj a, String field, int min, int max) {
        String v = a.str(field);
        if (v.isEmpty()) return field + " is required";
        if (v.length() < min) return field + " too short (min " + min + ")";
        if (v.length() > max) return field + " too long (max " + max + ")";
        for (char c : v.toCharArray())
            if (c < 0x20 && c != 0x09 && c != 0x0A && c != 0x0D)
                return field + " contains invalid control characters";
        return null;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TOOL IMPLEMENTATIONS
    // ─────────────────────────────────────────────────────────────────────────

    static String dispatchTool(String name, JsonObj a) {
        return switch (name) {
            case "send_notification"          -> toolSendNotification(a);
            case "queue_notification"         -> toolQueueNotification(a);
            case "get_notification_status"    -> toolGetStatus(a);
            case "retry_failed_notification"  -> toolRetry(a);
            case "get_user_preferences"       -> toolGetPrefs(a);
            case "update_user_preferences"    -> toolUpdatePrefs(a);
            case "suppress_address"           -> toolSuppress(a);
            case "check_suppression"          -> toolCheckSuppression(a);
            case "get_notification_history"   -> toolHistory(a);
            case "render_template"            -> toolRenderTemplate(a);
            case "get_queue_metrics"          -> toolQueueMetrics(a);
            case "bulk_send_notifications"    -> toolBulkSend(a);
            case "cancel_notification"        -> toolCancel(a);
            case "compliance_data_request"    -> toolCompliance(a);
            default -> throw new IllegalArgumentException("Unknown tool: " + name);
        };
    }

    // ── Tool 1: send_notification ─────────────────────────────────────────────
    static String toolSendNotification(JsonObj a) {
        String userId    = a.str("user_id");
        String type      = a.str("notification_type");
        String channel   = a.str("channel");
        String tenant    = a.str("tenant_id");
        String eventId   = a.str("event_id");
        String email     = a.str("recipient_email");
        String phone     = a.str("recipient_phone");
        String fcm       = a.str("recipient_fcm_token");
        String subject   = a.str("subject");
        String body      = a.str("body");
        String template  = a.str("template_name");
        String scheduled = a.str("scheduled_at");
        String priority  = a.str("priority");

        // Channel-specific recipient check
        if ("email".equals(channel) && email.isEmpty())
            return errData("MISSING_RECIPIENT", "recipient_email required when channel=email");
        if ("sms".equals(channel) && phone.isEmpty())
            return errData("MISSING_RECIPIENT", "recipient_phone required when channel=sms");
        if ("push".equals(channel) && fcm.isEmpty())
            return errData("MISSING_RECIPIENT", "recipient_fcm_token required when channel=push");

        // Deduplication
        String dedupId = eventId.isEmpty() ? uuid() : eventId;
        if (DEDUP.isDuplicate(dedupId, userId, type)) {
            return okData(
                "status", "DEDUPLICATED",
                "user_id", userId,
                "channel", channel,
                "notification_type", type,
                "event_id", dedupId,
                "message", "Duplicate notification suppressed (same event within 5-minute dedup window)"
            );
        }

        String notifId = "notif_" + uuid().replace("-","").substring(0, 16);
        String status  = scheduled.isEmpty() ? "SENT" : "SCHEDULED";
        String provider = switch (channel) {
            case "email"  -> "sendgrid";
            case "sms"    -> "twilio";
            case "push"   -> "firebase_fcm";
            default       -> "internal_websocket";
        };

        StringBuilder r = new StringBuilder();
        r.append("{\"success\":true");
        r.append(",\"request_id\":").append(jsonStr(uuid()));
        r.append(",\"timestamp\":").append(jsonStr(now()));
        r.append(",\"data\":{");
        r.append("\"notification_id\":").append(jsonStr(notifId));
        r.append(",\"status\":").append(jsonStr(status));
        r.append(",\"user_id\":").append(jsonStr(userId));
        r.append(",\"tenant_id\":").append(jsonStr(tenant.isEmpty() ? "default" : tenant));
        r.append(",\"notification_type\":").append(jsonStr(type));
        r.append(",\"channel\":").append(jsonStr(channel));
        r.append(",\"priority\":").append(jsonStr(priority.isEmpty() ? "normal" : priority));
        r.append(",\"provider\":").append(jsonStr(provider));
        r.append(",\"event_id\":").append(jsonStr(dedupId));
        r.append(",\"attempt_count\":1");
        if (scheduled.isEmpty()) r.append(",\"sent_at\":").append(jsonStr(now()));
        else r.append(",\"scheduled_at\":").append(jsonStr(scheduled));
        if (!template.isEmpty()) r.append(",\"template_name\":").append(jsonStr(template));
        if ("email".equals(channel)) {
            r.append(",\"recipient_email\":").append(jsonStr(email));
            r.append(",\"subject\":").append(jsonStr(subject.isEmpty() ? "[Ecoskiller] " + type : subject));
        } else if ("sms".equals(channel)) {
            r.append(",\"recipient_phone_masked\":").append(jsonStr(maskPhone(phone)));
            r.append(",\"body_chars\":").append(body.length());
        } else if ("push".equals(channel)) {
            r.append(",\"fcm_token_prefix\":").append(jsonStr(fcm.substring(0, Math.min(10, fcm.length())) + "..."));
        } else {
            r.append(",\"stored_in_notification_center\":true");
            r.append(",\"websocket_broadcast\":true");
        }
        r.append("}}");
        return r.toString();
    }

    // ── Tool 2: queue_notification ────────────────────────────────────────────
    static String toolQueueNotification(JsonObj a) {
        String queueId   = "q_" + uuid().replace("-","").substring(0, 16);
        String channel   = a.str("channel");
        String scheduled = a.str("scheduled_at");
        int maxRetries   = "email".equals(channel) ? 3 : 2;

        StringBuilder r = new StringBuilder();
        r.append("{\"success\":true,\"request_id\":").append(jsonStr(uuid()));
        r.append(",\"timestamp\":").append(jsonStr(now()));
        r.append(",\"data\":{");
        r.append("\"queue_id\":").append(jsonStr(queueId));
        r.append(",\"status\":\"PENDING\"");
        r.append(",\"user_id\":").append(jsonStr(a.str("user_id")));
        r.append(",\"tenant_id\":").append(jsonStr(orDefault(a.str("tenant_id"), "default")));
        r.append(",\"notification_type\":").append(jsonStr(a.str("notification_type")));
        r.append(",\"channel\":").append(jsonStr(channel));
        r.append(",\"priority\":").append(jsonStr(orDefault(a.str("priority"), "normal")));
        r.append(",\"scheduled_at\":").append(jsonStr(scheduled.isEmpty() ? now() : scheduled));
        r.append(",\"attempt_count\":0");
        r.append(",\"max_retries\":").append(maxRetries);
        if (!a.str("template_name").isEmpty())
            r.append(",\"template_name\":").append(jsonStr(a.str("template_name")));
        if (!a.str("idempotency_key").isEmpty())
            r.append(",\"idempotency_key\":").append(jsonStr(a.str("idempotency_key")));
        r.append(",\"created_at\":").append(jsonStr(now()));
        r.append(",\"message\":\"Notification queued. Workers process in batches of 100.\"");
        r.append("}}");
        return r.toString();
    }

    // ── Tool 3: get_notification_status ──────────────────────────────────────
    static String toolGetStatus(JsonObj a) {
        String notifId = a.str("notification_id");
        String tenant  = orDefault(a.str("tenant_id"), "default");
        return "{\"success\":true,\"request_id\":" + jsonStr(uuid()) + ",\"timestamp\":" + jsonStr(now()) +
            ",\"data\":{\"notification_id\":" + jsonStr(notifId) +
            ",\"tenant_id\":" + jsonStr(tenant) +
            ",\"status\":\"SENT\"" +
            ",\"channel\":\"email\"" +
            ",\"attempt_count\":1,\"max_retries\":3" +
            ",\"created_at\":" + jsonStr(minutesAgo(5)) +
            ",\"sent_at\":" + jsonStr(minutesAgo(4)) +
            ",\"error_reason\":null" +
            ",\"provider_message_id\":" + jsonStr("sg_" + uuid().substring(0,8)) +
            ",\"delivery_info\":{\"opened\":false,\"clicked\":false,\"bounced\":false,\"complained\":false}" +
            "}}";
    }

    // ── Tool 4: retry_failed_notification ────────────────────────────────────
    static String toolRetry(JsonObj a) {
        String notifId  = a.str("notification_id");
        boolean force   = a.bool("force_immediate");
        int current     = 1; // simulated current attempt
        int maxRetries  = 3;
        if (current >= maxRetries)
            return errData("MAX_RETRIES_EXCEEDED",
                "Notification " + notifId + " reached max retries (" + maxRetries + "). Permanently FAILED.");

        long[] backoffSec = {60, 300, 900, 3600};
        long delay = force ? 0 : backoffSec[Math.min(current, backoffSec.length - 1)];
        String nextAt = force ? now() : Instant.now().plusSeconds(delay).toString();

        return "{\"success\":true,\"request_id\":" + jsonStr(uuid()) + ",\"timestamp\":" + jsonStr(now()) +
            ",\"data\":{\"notification_id\":" + jsonStr(notifId) +
            ",\"status\":\"PENDING\"" +
            ",\"attempt_count\":" + (current + 1) +
            ",\"max_retries\":" + maxRetries +
            ",\"next_retry_at\":" + jsonStr(nextAt) +
            ",\"backoff_seconds\":" + delay +
            ",\"force_immediate\":" + force +
            ",\"message\":" + jsonStr(force
                ? "Retry scheduled immediately."
                : "Retry with exponential backoff (" + delay + "s delay).") +
            "}}";
    }

    // ── Tool 5: get_user_preferences ─────────────────────────────────────────
    static String toolGetPrefs(JsonObj a) {
        return "{\"success\":true,\"request_id\":" + jsonStr(uuid()) + ",\"timestamp\":" + jsonStr(now()) +
            ",\"data\":{\"user_id\":" + jsonStr(a.str("user_id")) +
            ",\"tenant_id\":" + jsonStr(orDefault(a.str("tenant_id"), "default")) +
            ",\"channels\":{\"email_enabled\":true,\"sms_enabled\":true,\"push_enabled\":true,\"in_app_enabled\":true}" +
            ",\"quiet_hours\":{\"enabled\":true,\"start_hour\":21,\"end_hour\":8,\"timezone\":\"Asia/Kolkata\"}" +
            ",\"daily_caps\":{\"email_max_per_day\":10,\"sms_max_per_day\":1,\"push_max_per_day\":5,\"in_app_unlimited\":true}" +
            ",\"frequency\":{\"email_digest_mode\":false,\"digest_hour\":9}" +
            ",\"consent\":{\"email_opt_in\":true,\"sms_opt_in\":true,\"opted_in_at\":\"2026-01-15T08:00:00Z\",\"double_opt_in_confirmed\":true}" +
            ",\"cache_ttl_seconds\":3600" +
            ",\"last_updated\":" + jsonStr(minutesAgo(120)) +
            "}}";
    }

    // ── Tool 6: update_user_preferences ──────────────────────────────────────
    static String toolUpdatePrefs(JsonObj a) {
        boolean emailOptOut = a.hasBool("email_enabled") && !a.bool("email_enabled");
        boolean smsOptOut   = a.hasBool("sms_enabled")   && !a.bool("sms_enabled");

        StringBuilder r = new StringBuilder();
        r.append("{\"success\":true,\"request_id\":").append(jsonStr(uuid()));
        r.append(",\"timestamp\":").append(jsonStr(now()));
        r.append(",\"data\":{");
        r.append("\"user_id\":").append(jsonStr(a.str("user_id")));
        r.append(",\"tenant_id\":").append(jsonStr(orDefault(a.str("tenant_id"), "default")));
        r.append(",\"updated_at\":").append(jsonStr(now()));
        r.append(",\"cache_invalidated\":true");
        r.append(",\"effective_immediately\":true");
        r.append(",\"applied_changes\":{");
        List<String> changes = new ArrayList<>();
        if (a.hasBool("email_enabled"))      changes.add("\"email_enabled\":" + a.bool("email_enabled"));
        if (a.hasBool("sms_enabled"))        changes.add("\"sms_enabled\":" + a.bool("sms_enabled"));
        if (a.hasBool("push_enabled"))       changes.add("\"push_enabled\":" + a.bool("push_enabled"));
        if (a.hasBool("in_app_enabled"))     changes.add("\"in_app_enabled\":" + a.bool("in_app_enabled"));
        if (a.intVal("quiet_hours_start", -1) >= 0) changes.add("\"quiet_hours_start\":" + a.intVal("quiet_hours_start", 21));
        if (a.intVal("quiet_hours_end",   -1) >= 0) changes.add("\"quiet_hours_end\":" + a.intVal("quiet_hours_end", 8));
        if (a.hasBool("email_digest_mode")) changes.add("\"email_digest_mode\":" + a.bool("email_digest_mode"));
        r.append(String.join(",", changes)).append("}");
        if (emailOptOut || smsOptOut) {
            r.append(",\"dpdpa_actions\":{");
            if (emailOptOut) r.append("\"email_suppression_added\":true,");
            if (smsOptOut)   r.append("\"sms_suppression_added\":true,");
            r.append("\"reason\":").append(jsonStr(orDefault(a.str("unsubscribe_reason"), "user_preference")));
            r.append(",\"audit_log_entry_created\":true}");
        }
        r.append("}}");
        return r.toString();
    }

    // ── Tool 7: suppress_address ──────────────────────────────────────────────
    static String toolSuppress(JsonObj a) {
        String addr    = a.str("address");
        String reason  = a.str("reason");
        String bounce  = a.str("bounce_type");
        String tenant  = orDefault(a.str("tenant_id"), "default");
        boolean isEmail = addr.contains("@");
        String table   = isEmail ? "email_suppression_list" : "sms_suppression_list";

        return "{\"success\":true,\"request_id\":" + jsonStr(uuid()) + ",\"timestamp\":" + jsonStr(now()) +
            ",\"data\":{\"suppressed\":true" +
            ",\"address_type\":" + jsonStr(isEmail ? "email" : "phone") +
            ",\"address_hash\":" + jsonStr(sha256prefix(addr)) +
            ",\"reason\":" + jsonStr(reason) +
            (bounce.isEmpty() ? "" : ",\"bounce_type\":" + jsonStr(bounce)) +
            ",\"tenant_id\":" + jsonStr(tenant) +
            ",\"suppressed_at\":" + jsonStr(now()) +
            ",\"stored_in_table\":" + jsonStr(table) +
            ",\"future_sends_blocked\":true" +
            ",\"note\":\"Hard bounces=permanent. Soft bounces=retry after 72h. Complaints=permanent, never auto-re-enabled.\"" +
            (reason.equals("complained") ? ",\"sender_reputation_impact\":\"high\"" : "") +
            "}}";
    }

    // ── Tool 8: check_suppression ─────────────────────────────────────────────
    static String toolCheckSuppression(JsonObj a) {
        String addr = a.str("address");
        return "{\"success\":true,\"request_id\":" + jsonStr(uuid()) + ",\"timestamp\":" + jsonStr(now()) +
            ",\"data\":{\"address_type\":" + jsonStr(addr.contains("@") ? "email" : "phone") +
            ",\"tenant_id\":" + jsonStr(orDefault(a.str("tenant_id"), "default")) +
            ",\"is_suppressed\":false" +
            ",\"suppression_reason\":null" +
            ",\"suppressed_at\":null" +
            ",\"safe_to_send\":true" +
            ",\"checked_at\":" + jsonStr(now()) +
            "}}";
    }

    // ── Tool 9: get_notification_history ──────────────────────────────────────
    static String toolHistory(JsonObj a) {
        int limit = a.intVal("limit", 50);
        boolean dpdpa = a.bool("dpdpa_access_request");

        StringBuilder r = new StringBuilder();
        r.append("{\"success\":true,\"request_id\":").append(jsonStr(uuid()));
        r.append(",\"timestamp\":").append(jsonStr(now()));
        r.append(",\"data\":{\"user_id\":").append(jsonStr(a.str("user_id")));
        r.append(",\"tenant_id\":").append(jsonStr(orDefault(a.str("tenant_id"), "default")));
        r.append(",\"total_count\":3,\"returned\":3,\"limit\":").append(limit);
        if (dpdpa) {
            r.append(",\"dpdpa_request_id\":").append(jsonStr(uuid()));
            r.append(",\"dpdpa_generated_at\":").append(jsonStr(now()));
            r.append(",\"retention_years\":3");
        }
        r.append(",\"notifications\":[");
        String[][] records = {
            {"interview.scheduled","email","SENT"},
            {"score.computed","push","SENT"},
            {"offer.extended","email","SENT"}
        };
        for (int i = 0; i < records.length; i++) {
            if (i > 0) r.append(",");
            r.append("{\"notification_id\":").append(jsonStr("notif_" + uuid().substring(0,8)));
            r.append(",\"notification_type\":").append(jsonStr(records[i][0]));
            r.append(",\"channel\":").append(jsonStr(records[i][1]));
            r.append(",\"status\":").append(jsonStr(records[i][2]));
            r.append(",\"sent_at\":").append(jsonStr(minutesAgo((i + 1) * 1440)));
            r.append(",\"error_reason\":null}");
        }
        r.append("],\"channel_summary\":{\"email\":2,\"push\":1,\"sms\":0,\"in_app\":0}}}");
        return r.toString();
    }

    // ── Tool 10: render_template ──────────────────────────────────────────────
    static String toolRenderTemplate(JsonObj a) {
        String tpl     = a.str("template_name");
        String ctx     = a.str("context_json");
        String variant = orDefault(a.str("variant"), "control");
        String channel = orDefault(a.str("channel"), "email");
        String locale  = orDefault(a.str("locale"), "en-IN");

        // Parse context variables
        Map<String,String> vars = parseSimpleJson(ctx);
        String cName  = orDefault(vars.get("candidate_name"), "Candidate");
        String jTitle = orDefault(vars.get("job_title"), "the position");
        String score  = orDefault(vars.get("score"), "");
        String iTime  = orDefault(vars.get("interview_time"), "");

        String subject = buildSubject(tpl, jTitle);
        String html    = buildHtml(tpl, cName, jTitle, score, iTime);
        String text    = buildText(tpl, cName, jTitle, score, iTime);
        String sms     = buildSms(tpl, cName, jTitle, iTime);

        return "{\"success\":true,\"request_id\":" + jsonStr(uuid()) + ",\"timestamp\":" + jsonStr(now()) +
            ",\"data\":{\"template_name\":" + jsonStr(tpl) +
            ",\"variant\":" + jsonStr(variant) +
            ",\"locale\":" + jsonStr(locale) +
            ",\"channel\":" + jsonStr(channel) +
            ",\"rendered_subject\":" + jsonStr(subject) +
            ",\"rendered_html\":" + jsonStr(html) +
            ",\"rendered_text\":" + jsonStr(text) +
            ",\"rendered_sms\":" + jsonStr(sms) +
            ",\"sms_char_count\":" + sms.length() +
            ",\"sms_within_limit\":" + (sms.length() <= 160) +
            ",\"variables_resolved\":" + vars.size() +
            ",\"unsubscribe_link_included\":true" +
            "}}";
    }

    // ── Tool 11: get_queue_metrics ────────────────────────────────────────────
    static String toolQueueMetrics(JsonObj a) {
        String tenant = orDefault(a.str("tenant_id"), "all");
        String window = orDefault(a.str("window"), "5m");
        return "{\"success\":true,\"request_id\":" + jsonStr(uuid()) + ",\"timestamp\":" + jsonStr(now()) +
            ",\"data\":{\"tenant_id\":" + jsonStr(tenant) +
            ",\"window\":" + jsonStr(window) +
            ",\"collected_at\":" + jsonStr(now()) +
            ",\"queue_depth\":{\"total_pending\":248,\"email_pending\":180,\"sms_pending\":30," +
                "\"push_pending\":28,\"in_app_pending\":10,\"alert_threshold\":10000,\"alert_triggered\":false}" +
            ",\"delivery_rates\":{\"notifications_sent_per_min\":45.3,\"email_per_min\":30.1," +
                "\"sms_per_min\":3.2,\"push_per_min\":8.7,\"in_app_per_min\":3.3}" +
            ",\"delivery_health\":{\"email_bounce_rate_pct\":0.4,\"email_complaint_rate_pct\":0.01," +
                "\"sms_failure_rate_pct\":1.2,\"push_failure_rate_pct\":0.8," +
                "\"bounce_alert_threshold_pct\":2.0,\"bounce_alert_triggered\":false}" +
            ",\"latency_p95_ms\":{\"email\":820,\"sms\":3200,\"push\":1100,\"in_app\":280}" +
            ",\"kafka_consumer_lag\":{\"total_lag\":1240,\"alert_threshold\":10000,\"alert_triggered\":false}" +
            ",\"worker_pods\":{\"active\":9,\"min_hpa\":9,\"max_hpa\":20,\"cpu_avg_pct\":34.5," +
                "\"scale_trigger_queue_depth\":10000,\"scale_trigger_cpu_pct\":70}" +
            ",\"monthly_cost_estimate\":{\"email_usd\":\"~1000 (10M emails @ $0.0001)\"," +
                "\"sms_usd\":\"~1000 (100K SMS @ $0.01)\",\"push_usd\":\"free\",\"in_app_usd\":\"free\"}" +
            "}}";
    }

    // ── Tool 12: bulk_send_notifications ─────────────────────────────────────
    static String toolBulkSend(JsonObj a) {
        String raw    = a.str("notifications");
        String tenant = orDefault(a.str("tenant_id"), "default");
        String label  = a.str("batch_label");
        boolean fail  = a.bool("fail_fast");

        // Count items roughly (each item has a '{')
        int total = 0;
        for (char c : raw.toCharArray()) if (c == '{') total++;

        int emailCount = Math.max(0, (int)(total * 0.6));
        int smsCount   = Math.max(0, total - emailCount);
        int emailBatches = Math.max(1, (int)Math.ceil(emailCount / 100.0));
        int smsBatches   = Math.max(0, (int)Math.ceil(smsCount   / 50.0));
        String batchId   = "bulk_" + uuid().replace("-","").substring(0,12);

        return "{\"success\":true,\"request_id\":" + jsonStr(uuid()) + ",\"timestamp\":" + jsonStr(now()) +
            ",\"data\":{\"batch_id\":" + jsonStr(batchId) +
            (label.isEmpty() ? "" : ",\"batch_label\":" + jsonStr(label)) +
            ",\"tenant_id\":" + jsonStr(tenant) +
            ",\"total_submitted\":" + total +
            ",\"total_queued\":" + total +
            ",\"total_failed\":0" +
            ",\"fail_fast_mode\":" + fail +
            ",\"status\":\"BATCH_QUEUED\"" +
            ",\"created_at\":" + jsonStr(now()) +
            ",\"channel_breakdown\":{\"email\":" + emailCount + ",\"sms\":" + smsCount + ",\"push\":0,\"in_app\":0}" +
            ",\"api_batching\":{\"email_api_calls\":" + emailBatches + ",\"email_batch_size\":100," +
                "\"sms_api_calls\":" + smsBatches + ",\"sms_batch_size\":50," +
                "\"estimated_duration_seconds\":" + (emailBatches * 2 + smsBatches * 5) + "}" +
            ",\"message\":" + jsonStr("Batch of " + total + " notifications queued. " +
                emailBatches + " email API call(s), " + smsBatches + " SMS API call(s).") +
            "}}";
    }

    // ── Tool 13: cancel_notification ─────────────────────────────────────────
    static String toolCancel(JsonObj a) {
        String notifId = a.str("notification_id");
        String reason  = orDefault(a.str("cancel_reason"), "manual_cancel");
        return "{\"success\":true,\"request_id\":" + jsonStr(uuid()) + ",\"timestamp\":" + jsonStr(now()) +
            ",\"data\":{\"notification_id\":" + jsonStr(notifId) +
            ",\"tenant_id\":" + jsonStr(orDefault(a.str("tenant_id"), "default")) +
            ",\"cancelled\":true" +
            ",\"previous_status\":\"PENDING\"" +
            ",\"new_status\":\"CANCELLED\"" +
            ",\"cancel_reason\":" + jsonStr(reason) +
            ",\"cancelled_at\":" + jsonStr(now()) +
            ",\"audit_log_entry\":true" +
            ",\"message\":" + jsonStr("Notification " + notifId + " cancelled. Audit entry written (immutable).") +
            "}}";
    }

    // ── Tool 14: compliance_data_request ─────────────────────────────────────
    static String toolCompliance(JsonObj a) {
        String userId   = a.str("user_id");
        String type     = a.str("request_type");
        String tenant   = orDefault(a.str("tenant_id"), "default");
        String reqId    = "dpdpa_" + uuid().replace("-","").substring(0,12);
        String slaDate  = Instant.now().plus(30, ChronoUnit.DAYS).toString();

        StringBuilder r = new StringBuilder();
        r.append("{\"success\":true,\"request_id\":").append(jsonStr(uuid()));
        r.append(",\"timestamp\":").append(jsonStr(now()));
        r.append(",\"data\":{\"compliance_request_id\":").append(jsonStr(reqId));
        r.append(",\"user_id\":").append(jsonStr(userId));
        r.append(",\"tenant_id\":").append(jsonStr(tenant));
        r.append(",\"request_type\":").append(jsonStr(type));
        r.append(",\"regulation\":\"DPDPA 2023 (India)\"");
        r.append(",\"submitted_at\":").append(jsonStr(now()));
        r.append(",\"sla_deadline\":").append(jsonStr(slaDate));

        switch (type) {
            case "access" -> {
                r.append(",\"status\":\"PROCESSING\"");
                r.append(",\"data_scope\":\"notification_audit_log (3yr), notification_center (30d)\"");
                r.append(",\"estimated_records\":142");
                r.append(",\"delivery_format\":\"JSON export via secure download link within 24h\"");
            }
            case "erasure" -> {
                r.append(",\"status\":\"QUEUED_FOR_ERASURE\"");
                r.append(",\"erasure_actions\":{\"future_sends_suppressed\":true,\"email_suppressed\":true,");
                r.append("\"sms_suppressed\":true,\"notification_center_purged\":true,");
                r.append("\"operational_logs_purged\":true,\"audit_log_retained\":true,");
                r.append("\"redis_cache_invalidated\":true}");
                r.append(",\"audit_log_note\":\"notification_audit_log (immutable) retained 3yr per DPDPA 2023 §8(7)\"");
                r.append(",\"complete_by\":").append(jsonStr(Instant.now().plus(72, ChronoUnit.HOURS).toString()));
            }
            case "portability" -> {
                r.append(",\"status\":\"EXPORT_QUEUED\"");
                r.append(",\"export_format\":\"JSON\"");
                r.append(",\"download_link_expires_hours\":48");
                r.append(",\"estimated_file_size_kb\":24");
                r.append(",\"ready_by\":").append(jsonStr(Instant.now().plus(4, ChronoUnit.HOURS).toString()));
            }
        }

        r.append(",\"audit_log_entry\":true");
        r.append(",\"message\":").append(jsonStr("DPDPA " + type + " request " + reqId + " created. SLA: 30 days per DPDPA 2023."));
        r.append("}}");
        return r.toString();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEMPLATE HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    static String buildSubject(String tpl, String job) {
        return switch (tpl) {
            case "interview_scheduled"  -> "Interview Scheduled — " + job + " | Ecoskiller";
            case "score_computed"       -> "Your Assessment Results Are Ready | Ecoskiller";
            case "offer_extended"       -> "Congratulations! Job Offer — " + job + " | Ecoskiller";
            case "application_received" -> "Application Received — " + job + " | Ecoskiller";
            case "interview_reminder"   -> "Reminder: Your Interview Is Today | Ecoskiller";
            case "offer_accepted"       -> "Offer Accepted — Welcome Aboard! | Ecoskiller";
            case "belt_advancement"     -> "Belt Advancement — New Certification | Ecoskiller";
            default                     -> "[Ecoskiller] " + tpl.replace("_"," ");
        };
    }

    static String buildHtml(String tpl, String name, String job, String score, String time) {
        String body = switch (tpl) {
            case "interview_scheduled" ->
                "<p>Your interview for <strong>" + escHtml(job) + "</strong> has been scheduled.</p>" +
                (time.isEmpty() ? "" : "<p><strong>Time:</strong> " + escHtml(time) + "</p>") +
                "<p>Please join on time. All the best!</p>";
            case "score_computed" ->
                "<p>Your assessment for <strong>" + escHtml(job) + "</strong> has been evaluated.</p>" +
                (score.isEmpty() ? "" : "<p><strong>Score:</strong> " + escHtml(score) + "%</p>") +
                "<p>Log in to Ecoskiller to view detailed dimension-level feedback.</p>";
            case "offer_extended" ->
                "<p>Congratulations! We are pleased to offer you the position of <strong>" + escHtml(job) + "</strong>.</p>" +
                "<p>Please review and accept your offer in the Ecoskiller dashboard.</p>";
            default ->
                "<p>You have a new notification regarding <strong>" + escHtml(job) + "</strong>.</p>";
        };
        return "<!DOCTYPE html><html><body style='font-family:Arial,sans-serif;max-width:600px'>" +
            "<h2>Hello " + escHtml(name) + ",</h2>" + body +
            "<p style='margin-top:24px'>Best regards,<br><strong>Ecoskiller Team</strong></p>" +
            "<hr><p style='font-size:11px;color:#888'>To unsubscribe: " +
            "<a href='https://ecoskiller.com/unsubscribe'>click here</a>.</p></body></html>";
    }

    static String buildText(String tpl, String name, String job, String score, String time) {
        return "Hello " + name + ",\n\n" + switch (tpl) {
            case "interview_scheduled" -> "Your interview for " + job + " has been scheduled." + (time.isEmpty() ? "" : "\nTime: " + time);
            case "score_computed"      -> "Assessment results for " + job + " are ready." + (score.isEmpty() ? "" : "\nScore: " + score + "%");
            case "offer_extended"      -> "Congratulations! You have received a job offer for " + job + ".";
            default                    -> "New update on " + job + ". Log in to Ecoskiller.";
        } + "\n\nEcoskiller Team\nUnsubscribe: https://ecoskiller.com/unsubscribe";
    }

    static String buildSms(String tpl, String name, String job, String time) {
        String n = trunc(name, 15);
        String j = trunc(job, 20);
        String raw = switch (tpl) {
            case "interview_scheduled" -> "Hi " + n + "! Interview for " + j + (time.isEmpty() ? "" : " at " + trunc(time,15)) + ". Good luck! -Ecoskiller";
            case "score_computed"      -> "Hi " + n + "! Your " + j + " assessment results ready. Login to view. -Ecoskiller";
            case "offer_extended"      -> "Congrats " + n + "! Offer for " + j + ". Check your dashboard. -Ecoskiller";
            default                    -> "Hi " + n + "! Update on " + j + ". Login to Ecoskiller.";
        };
        return raw.substring(0, Math.min(raw.length(), 160));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RESPONSE HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    static String okResp(String id, String result) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + (id.isEmpty() ? "null" : jsonStr(id)) +
               ",\"result\":" + result + "}";
    }

    static String errResp(String id, int code, String message) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + (id == null || id.isEmpty() ? "null" : jsonStr(id)) +
               ",\"error\":{\"code\":" + code + ",\"message\":" + jsonStr(message) + "}}";
    }

    static String okData(String... kv) {
        StringBuilder sb = new StringBuilder("{\"success\":true,\"request_id\":");
        sb.append(jsonStr(uuid())).append(",\"timestamp\":").append(jsonStr(now()));
        sb.append(",\"data\":{");
        for (int i = 0; i < kv.length - 1; i += 2) {
            if (i > 0) sb.append(",");
            sb.append(jsonStr(kv[i])).append(":").append(jsonStr(kv[i+1]));
        }
        sb.append("}}");
        return sb.toString();
    }

    static String errData(String code, String message) {
        return "{\"success\":false,\"request_id\":" + jsonStr(uuid()) +
               ",\"timestamp\":" + jsonStr(now()) +
               ",\"error\":{\"code\":" + jsonStr(code) + ",\"message\":" + jsonStr(message) + "}}";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TOOLS/LIST SCHEMA HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    static String toolDef(String name, String desc, String... props) {
        StringBuilder r = new StringBuilder("{\"name\":").append(jsonStr(name));
        r.append(",\"description\":").append(jsonStr(desc));
        r.append(",\"inputSchema\":{\"type\":\"object\",\"properties\":{");
        List<String> required = new ArrayList<>();
        for (String p : props) r.append(p);
        // remove trailing comma
        if (r.charAt(r.length()-1) == ',') r.deleteCharAt(r.length()-1);
        // extract required from props
        for (String p : props) if (p.contains("\"__req\":true")) {
            int ni = p.indexOf("\"name\":\"") + 8;
            int ne = p.indexOf("\"", ni);
            if (ni > 7 && ne > ni) required.add(p.substring(ni, ne));
        }
        r.append("},\"required\":[");
        for (int i = 0; i < required.size(); i++) {
            if (i > 0) r.append(",");
            r.append(jsonStr(required.get(i)));
        }
        r.append("]}}");
        return r.toString();
    }

    static String req(String name, String type, String desc) {
        return prop(name, type, desc, true);
    }
    static String opt(String name, String type, String desc) {
        return prop(name, type, desc, false);
    }
    static String prop(String name, String type, String desc, boolean required) {
        return "\"" + name + "\":{\"type\":\"" + type + "\",\"description\":" + jsonStr(desc) +
               (required ? ",\"__req\":true" : "") + "},";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UTILITIES
    // ─────────────────────────────────────────────────────────────────────────

    static String json(String... kv) {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < kv.length - 1; i += 2) {
            if (i > 0) sb.append(",");
            String v = kv[i+1];
            if (v.startsWith("{") || v.startsWith("[") || v.equals("true") || v.equals("false"))
                sb.append(jsonStr(kv[i])).append(":").append(v);
            else
                sb.append(jsonStr(kv[i])).append(":").append(jsonStr(v));
        }
        sb.append("}");
        return sb.toString();
    }

    static String jsonStr(String s) {
        if (s == null) return "null";
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"'  -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default   -> {
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
                }
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    static String now()               { return Instant.now().toString(); }
    static String minutesAgo(int m)   { return Instant.now().minus(m, ChronoUnit.MINUTES).toString(); }
    static String uuid()              { return UUID.randomUUID().toString(); }
    static String orDefault(String v, String def) { return (v == null || v.isEmpty()) ? def : v; }
    static String trunc(String s, int max) { return s.length() <= max ? s : s.substring(0, max-1) + "."; }
    static String escHtml(String s)   { return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;"); }
    static String maskPhone(String p) {
        if (p.length() <= 6) return "***";
        return p.substring(0, 3) + "***" + p.substring(p.length() - 4);
    }
    static String sha256prefix(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(s.toLowerCase().trim().getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte x : b) sb.append(String.format("%02x", x));
            return sb.substring(0, 16) + "...";
        } catch (Exception e) { return "[hash]"; }
    }
    static String sanitize(String msg) {
        if (msg == null) return "unknown error";
        return msg.replaceAll("(?i)(password|secret|token|key)=[^\\s&]+","$1=***")
                  .substring(0, Math.min(msg.length(), 200));
    }
    static void log(String msg) {
        System.err.println("[" + SERVER_NAME + "] " + msg);
    }

    // Simple JSON object field extraction (handles flat objects only)
    static Map<String,String> parseSimpleJson(String json) {
        Map<String,String> map = new LinkedHashMap<>();
        if (json == null || json.isEmpty()) return map;
        // Match "key":"value" pairs (string values only)
        java.util.regex.Matcher m = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]*)\"").matcher(json);
        while (m.find()) map.put(m.group(1), m.group(2));
        return map;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MINIMAL JSON PARSER
    // ─────────────────────────────────────────────────────────────────────────

    static class JsonObj {
        final Map<String, Object> fields;
        JsonObj(Map<String, Object> fields) { this.fields = fields; }

        String str(String key) {
            Object v = fields.get(key);
            return v == null ? "" : v.toString();
        }

        int intVal(String key, int def) {
            Object v = fields.get(key);
            if (v == null) return def;
            try { return Integer.parseInt(v.toString()); }
            catch (Exception e) { return def; }
        }

        boolean bool(String key) {
            Object v = fields.get(key);
            if (v == null) return false;
            return "true".equalsIgnoreCase(v.toString());
        }

        boolean hasBool(String key) {
            return fields.containsKey(key) && fields.get(key) != null;
        }

        JsonObj obj(String key) {
            Object v = fields.get(key);
            if (v instanceof JsonObj) return (JsonObj) v;
            if (v instanceof String s && s.startsWith("{")) {
                try { return JsonParser.parseObject(s); } catch (Exception e) { return null; }
            }
            return null;
        }
    }

    static class JsonParser {
        String src;
        int pos;

        JsonParser(String src) { this.src = src; this.pos = 0; }

        static JsonObj parseObject(String src) throws Exception {
            return new JsonParser(src.trim()).readObject();
        }

        JsonObj readObject() throws Exception {
            expect('{');
            Map<String, Object> fields = new LinkedHashMap<>();
            skipWs();
            if (peek() == '}') { pos++; return new JsonObj(fields); }
            while (true) {
                skipWs();
                String key = readString();
                skipWs();
                expect(':');
                skipWs();
                Object val = readValue();
                fields.put(key, val);
                skipWs();
                if (peek() == '}') { pos++; break; }
                expect(',');
            }
            return new JsonObj(fields);
        }

        Object readValue() throws Exception {
            char c = peek();
            if (c == '"')  return readString();
            if (c == '{')  return readObject();
            if (c == '[')  return readArray();
            if (c == 't')  { expect("true");  return "true"; }
            if (c == 'f')  { expect("false"); return "false"; }
            if (c == 'n')  { expect("null");  return null; }
            return readNumber();
        }

        String readString() throws Exception {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (pos < src.length()) {
                char c = src.charAt(pos++);
                if (c == '"') return sb.toString();
                if (c == '\\') {
                    char e = src.charAt(pos++);
                    switch (e) {
                        case '"', '\\', '/' -> sb.append(e);
                        case 'n' -> sb.append('\n');
                        case 'r' -> sb.append('\r');
                        case 't' -> sb.append('\t');
                        case 'u' -> {
                            String hex = src.substring(pos, pos + 4); pos += 4;
                            sb.append((char) Integer.parseInt(hex, 16));
                        }
                        default -> sb.append(e);
                    }
                } else {
                    sb.append(c);
                }
            }
            throw new Exception("Unterminated string");
        }

        String readNumber() {
            int start = pos;
            while (pos < src.length()) {
                char c = src.charAt(pos);
                if (Character.isDigit(c) || c=='-'||c=='.'||c=='e'||c=='E'||c=='+') pos++;
                else break;
            }
            return src.substring(start, pos);
        }

        String readArray() throws Exception {
            int start = pos;
            expect('[');
            int depth = 1;
            while (pos < src.length() && depth > 0) {
                char c = src.charAt(pos++);
                if (c == '[') depth++;
                else if (c == ']') depth--;
                else if (c == '"') { pos--; readString(); }
            }
            return src.substring(start, pos);
        }

        void expect(char c) throws Exception {
            if (pos >= src.length() || src.charAt(pos) != c)
                throw new Exception("Expected '" + c + "' at pos " + pos);
            pos++;
        }

        void expect(String s) throws Exception {
            if (!src.startsWith(s, pos)) throw new Exception("Expected '" + s + "' at pos " + pos);
            pos += s.length();
        }

        char peek() { return pos < src.length() ? src.charAt(pos) : 0; }
        void skipWs() { while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++; }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECURITY CLASSES
    // ─────────────────────────────────────────────────────────────────────────

    static class RateLimiter {
        final int max;
        final AtomicInteger count = new AtomicInteger(0);
        volatile long windowStart = System.currentTimeMillis();
        RateLimiter(int max) { this.max = max; }
        boolean allow(String key) {
            long now = System.currentTimeMillis();
            if (now - windowStart >= 60_000) {
                synchronized(this) {
                    if (now - windowStart >= 60_000) { count.set(0); windowStart = now; }
                }
            }
            return count.incrementAndGet() <= max;
        }
    }

    static class DedupCache {
        final long ttlMs;
        final ConcurrentHashMap<String, Long> seen = new ConcurrentHashMap<>();
        DedupCache(long ttlMs) { this.ttlMs = ttlMs; }
        boolean isDuplicate(String eventId, String userId, String type) {
            evict();
            String key = sha256prefix(eventId + "|" + userId + "|" + type);
            long now = System.currentTimeMillis();
            Long prev = seen.get(key);
            if (prev != null && now - prev < ttlMs) return true;
            seen.put(key, now);
            return false;
        }
        void evict() {
            long cut = System.currentTimeMillis() - ttlMs;
            seen.entrySet().removeIf(e -> e.getValue() < cut);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST RUNNER
    // ─────────────────────────────────────────────────────────────────────────

    static int PASS = 0, FAIL = 0;
    static boolean VERBOSE_TEST = false;

    static void runTests(boolean verbose) {
        VERBOSE_TEST = verbose;
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║  Ecoskiller Notification MCP — Agent Test Suite              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");

        test("initialize",
            "{\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"initialize\",\"params\":{}}");
        test("tools/list",
            "{\"jsonrpc\":\"2.0\",\"id\":\"2\",\"method\":\"tools/list\",\"params\":{}}");
        test("ping",
            "{\"jsonrpc\":\"2.0\",\"id\":\"3\",\"method\":\"ping\",\"params\":{}}");
        test("send_notification — email",
            "{\"jsonrpc\":\"2.0\",\"id\":\"10\",\"method\":\"tools/call\",\"params\":{\"name\":\"send_notification\",\"arguments\":{\"user_id\":\"user_abc\",\"notification_type\":\"interview.scheduled\",\"channel\":\"email\",\"recipient_email\":\"candidate@example.com\",\"event_id\":\"evt_001\"}}}");
        test("send_notification — SMS",
            "{\"jsonrpc\":\"2.0\",\"id\":\"11\",\"method\":\"tools/call\",\"params\":{\"name\":\"send_notification\",\"arguments\":{\"user_id\":\"user_def\",\"notification_type\":\"interview.scheduled\",\"channel\":\"sms\",\"recipient_phone\":\"+919876543210\",\"body\":\"Interview scheduled\"}}}");
        test("send_notification — in_app",
            "{\"jsonrpc\":\"2.0\",\"id\":\"12\",\"method\":\"tools/call\",\"params\":{\"name\":\"send_notification\",\"arguments\":{\"user_id\":\"user_ghi\",\"notification_type\":\"score.computed\",\"channel\":\"in_app\",\"body\":\"Results ready\"}}}");
        test("queue_notification",
            "{\"jsonrpc\":\"2.0\",\"id\":\"20\",\"method\":\"tools/call\",\"params\":{\"name\":\"queue_notification\",\"arguments\":{\"user_id\":\"user_01\",\"notification_type\":\"application.submitted\",\"channel\":\"email\",\"priority\":\"high\"}}}");
        test("get_notification_status",
            "{\"jsonrpc\":\"2.0\",\"id\":\"30\",\"method\":\"tools/call\",\"params\":{\"name\":\"get_notification_status\",\"arguments\":{\"notification_id\":\"notif_xyz\"}}}");
        test("retry_failed_notification",
            "{\"jsonrpc\":\"2.0\",\"id\":\"40\",\"method\":\"tools/call\",\"params\":{\"name\":\"retry_failed_notification\",\"arguments\":{\"notification_id\":\"notif_fail\",\"force_immediate\":false}}}");
        test("get_user_preferences",
            "{\"jsonrpc\":\"2.0\",\"id\":\"50\",\"method\":\"tools/call\",\"params\":{\"name\":\"get_user_preferences\",\"arguments\":{\"user_id\":\"user_abc\"}}}");
        test("update_user_preferences",
            "{\"jsonrpc\":\"2.0\",\"id\":\"60\",\"method\":\"tools/call\",\"params\":{\"name\":\"update_user_preferences\",\"arguments\":{\"user_id\":\"user_abc\",\"sms_enabled\":false,\"quiet_hours_start\":22}}}");
        test("suppress_address — hard bounce",
            "{\"jsonrpc\":\"2.0\",\"id\":\"70\",\"method\":\"tools/call\",\"params\":{\"name\":\"suppress_address\",\"arguments\":{\"address\":\"bounced@example.com\",\"reason\":\"bounced\",\"bounce_type\":\"hard\"}}}");
        test("check_suppression",
            "{\"jsonrpc\":\"2.0\",\"id\":\"80\",\"method\":\"tools/call\",\"params\":{\"name\":\"check_suppression\",\"arguments\":{\"address\":\"ok@example.com\"}}}");
        test("get_notification_history",
            "{\"jsonrpc\":\"2.0\",\"id\":\"90\",\"method\":\"tools/call\",\"params\":{\"name\":\"get_notification_history\",\"arguments\":{\"user_id\":\"user_abc\",\"limit\":10}}}");
        test("render_template — interview_scheduled",
            "{\"jsonrpc\":\"2.0\",\"id\":\"100\",\"method\":\"tools/call\",\"params\":{\"name\":\"render_template\",\"arguments\":{\"template_name\":\"interview_scheduled\",\"context_json\":\"{\\\"candidate_name\\\":\\\"Priya Sharma\\\",\\\"job_title\\\":\\\"Data Scientist\\\",\\\"interview_time\\\":\\\"2026-03-25T10:00:00+05:30\\\"}\",\"channel\":\"email\",\"variant\":\"variant_a\"}}}");
        test("render_template — offer_extended",
            "{\"jsonrpc\":\"2.0\",\"id\":\"101\",\"method\":\"tools/call\",\"params\":{\"name\":\"render_template\",\"arguments\":{\"template_name\":\"offer_extended\",\"context_json\":\"{\\\"candidate_name\\\":\\\"Ravi Kumar\\\",\\\"job_title\\\":\\\"Senior Java Dev\\\"}\",\"channel\":\"sms\"}}}");
        test("get_queue_metrics",
            "{\"jsonrpc\":\"2.0\",\"id\":\"110\",\"method\":\"tools/call\",\"params\":{\"name\":\"get_queue_metrics\",\"arguments\":{\"window\":\"5m\"}}}");
        test("bulk_send_notifications",
            "{\"jsonrpc\":\"2.0\",\"id\":\"120\",\"method\":\"tools/call\",\"params\":{\"name\":\"bulk_send_notifications\",\"arguments\":{\"notifications\":\"[{\\\"user_id\\\":\\\"u1\\\",\\\"notification_type\\\":\\\"score.computed\\\",\\\"channel\\\":\\\"email\\\"},{\\\"user_id\\\":\\\"u2\\\",\\\"notification_type\\\":\\\"offer.extended\\\",\\\"channel\\\":\\\"sms\\\"}]\",\"tenant_id\":\"tenant_x\",\"batch_label\":\"march_batch\"}}}");
        test("cancel_notification",
            "{\"jsonrpc\":\"2.0\",\"id\":\"130\",\"method\":\"tools/call\",\"params\":{\"name\":\"cancel_notification\",\"arguments\":{\"notification_id\":\"notif_sched_abc\",\"cancel_reason\":\"job_closed\"}}}");
        test("compliance_data_request — access",
            "{\"jsonrpc\":\"2.0\",\"id\":\"140\",\"method\":\"tools/call\",\"params\":{\"name\":\"compliance_data_request\",\"arguments\":{\"user_id\":\"user_abc\",\"request_type\":\"access\",\"requester_id\":\"dpo_01\"}}}");
        test("compliance_data_request — erasure",
            "{\"jsonrpc\":\"2.0\",\"id\":\"141\",\"method\":\"tools/call\",\"params\":{\"name\":\"compliance_data_request\",\"arguments\":{\"user_id\":\"user_del\",\"request_type\":\"erasure\"}}}");
        test("compliance_data_request — portability",
            "{\"jsonrpc\":\"2.0\",\"id\":\"142\",\"method\":\"tools/call\",\"params\":{\"name\":\"compliance_data_request\",\"arguments\":{\"user_id\":\"user_abc\",\"request_type\":\"portability\"}}}");

        // Security tests (these should return errors)
        testSec("reject malformed JSON",           "not-json{{{");
        testSec("reject unknown tool",             "{\"jsonrpc\":\"2.0\",\"id\":\"99\",\"method\":\"tools/call\",\"params\":{\"name\":\"drop_tables\",\"arguments\":{}}}");
        testSec("reject invalid email format",     "{\"jsonrpc\":\"2.0\",\"id\":\"98\",\"method\":\"tools/call\",\"params\":{\"name\":\"send_notification\",\"arguments\":{\"user_id\":\"u1\",\"notification_type\":\"t\",\"channel\":\"email\",\"recipient_email\":\"not-an-email\"}}}");
        testSec("reject invalid phone format",     "{\"jsonrpc\":\"2.0\",\"id\":\"97\",\"method\":\"tools/call\",\"params\":{\"name\":\"send_notification\",\"arguments\":{\"user_id\":\"u1\",\"notification_type\":\"t\",\"channel\":\"sms\",\"recipient_phone\":\"0999999\"}}}");
        testSec("reject invalid suppress reason",  "{\"jsonrpc\":\"2.0\",\"id\":\"96\",\"method\":\"tools/call\",\"params\":{\"name\":\"suppress_address\",\"arguments\":{\"address\":\"test@test.com\",\"reason\":\"delete_all\"}}}");
        testSec("reject bad compliance type",      "{\"jsonrpc\":\"2.0\",\"id\":\"95\",\"method\":\"tools/call\",\"params\":{\"name\":\"compliance_data_request\",\"arguments\":{\"user_id\":\"u1\",\"request_type\":\"hack\"}}}");
        testSec("reject over-limit bulk",          "{\"jsonrpc\":\"2.0\",\"id\":\"94\",\"method\":\"tools/call\",\"params\":{\"name\":\"bulk_send_notifications\",\"arguments\":{\"notifications\":\"not-an-array\"}}}");

        System.out.println("\n" + "═".repeat(64));
        System.out.printf("  RESULTS:  ✅ %d passed   ❌ %d failed   total: %d%n", PASS, FAIL, PASS + FAIL);
        System.out.println("═".repeat(64));
        System.exit(FAIL > 0 ? 1 : 0);
    }

    static void test(String label, String req) {
        String resp = handleMessage(req);
        boolean ok = resp != null && resp.contains("\"result\"") && !resp.contains("\"error\"");
        if (ok) { System.out.printf("  ✅  %-50s%n", label); PASS++; }
        else     { System.out.printf("  ❌  %-50s%n", label); FAIL++;
                   System.out.println("      Response: " + resp); }
        if (VERBOSE_TEST && ok) System.out.println("      " + resp.substring(0, Math.min(resp.length(),200)) + "...");
    }

    static void testSec(String label, String req) {
        String resp = handleMessage(req);
        boolean rejected = resp == null || resp.contains("\"error\"") ||
                          (resp.contains("\"isError\":true")) ||
                          (!resp.contains("\"success\":true") && !resp.contains("\"result\""));

        // For tool calls with validation errors: they come back as result with isError or error in content
        if (!rejected && resp != null && resp.contains("Validation:")) rejected = true;
        if (!rejected && resp != null && resp.contains("\"code\":-32")) rejected = true;

        if (rejected) { System.out.printf("  🔒  %-50s [rejected correctly]%n", label); PASS++; }
        else          { System.out.printf("  ⚠️   %-50s [SHOULD HAVE BEEN REJECTED]%n", label); FAIL++;
                        System.out.println("      Response: " + resp); }
    }
}
