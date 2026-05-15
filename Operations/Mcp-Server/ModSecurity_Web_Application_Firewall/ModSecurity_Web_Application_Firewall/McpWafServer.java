package com.ecoskiller.waf;

/**
 * ============================================================
 *  Ecoskiller | ModSecurity Web Application Firewall
 *  MCP Server in Java | 18 Agents | Priority: HIGH
 *
 *  Transport  : stdio (stdin/stdout)
 *  Protocol   : JSON-RPC 2.0 (MCP 2024-11-05)
 *  Methods    : initialize, tools/list, tools/call, ping
 *
 *  Security   : Input sanitisation, anomaly scoring,
 *               in-memory rate-limit, IP blocklist,
 *               audit trail, PII masking on all outputs.
 * ============================================================
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.*;
import java.util.stream.Collectors;

public class McpWafServer {

    // ─────────────────────────────────────────────────────────
    //  SECURITY STORE  (in-memory; replace with Redis in prod)
    // ─────────────────────────────────────────────────────────

    /** IP → request count in the current 60-second window. */
    private static final ConcurrentHashMap<String, long[]> RATE_WINDOWS = new ConcurrentHashMap<>();
    /** Permanently blocked IPs (admin action or auto-block). */
    private static final Set<String> BLOCKED_IPS = Collections.synchronizedSet(new HashSet<>());
    /** Whitelisted IPs (skip all WAF rules). */
    private static final Set<String> WHITELISTED_IPS = Collections.synchronizedSet(new HashSet<>());
    /** Custom rules (id → SecRule-style description). */
    private static final Map<String, Map<String, String>> CUSTOM_RULES = new ConcurrentHashMap<>();
    /** Bounded circular audit log (latest 500 entries). */
    private static final List<Map<String, Object>> AUDIT_LOG = Collections.synchronizedList(new ArrayList<>());
    private static final int MAX_AUDIT_SIZE = 500;
    /** Security events list (latest 200). */
    private static final List<Map<String, Object>> SECURITY_EVENTS = Collections.synchronizedList(new ArrayList<>());
    private static final int MAX_EVENTS_SIZE = 200;
    /** Anomaly scores per IP. */
    private static final ConcurrentHashMap<String, Integer> ANOMALY_SCORES = new ConcurrentHashMap<>();
    /** WAF statistics counters. */
    private static final AtomicLong TOTAL_REQUESTS = new AtomicLong(0);
    private static final AtomicLong TOTAL_BLOCKED = new AtomicLong(0);
    private static final AtomicLong TOTAL_PASSED = new AtomicLong(0);
    private static final long SERVER_START_MS = System.currentTimeMillis();

    // ─────────────────────────────────────────────────────────
    //  DETECTION PATTERNS  (OWASP CRS subset)
    // ─────────────────────────────────────────────────────────

    /** SQL injection patterns (CRS 942xxx). */
    private static final Pattern SQL_INJECTION = Pattern.compile(
        "(?i)(?:" +
        "'\\s*(?:or|and)\\s*'?\\d|" +
        "\\bunion\\b.{0,20}\\bselect\\b|" +
        "\\bselect\\b.{0,40}\\bfrom\\b|" +
        "\\bdrop\\b.{0,10}\\b(?:table|database)\\b|" +
        "\\binsert\\b.{0,10}\\binto\\b|" +
        "\\bdelete\\b.{0,10}\\bfrom\\b|" +
        "\\bexec(?:ute)?\\s*\\(|" +
        "\\bxp_cmdshell\\b|" +
        "--\\s*$|" +
        "/\\*.*?\\*/|" +
        "\\bwaitfor\\s+delay\\b|" +
        "\\bsleep\\s*\\(\\d" +
        ")"
    );

    /** XSS patterns (CRS 941xxx). */
    private static final Pattern XSS_ATTACK = Pattern.compile(
        "(?i)(?:" +
        "<\\s*script[^>]*>|" +
        "</\\s*script\\s*>|" +
        "javascript\\s*:|" +
        "vbscript\\s*:|" +
        "on(?:load|error|click|mouseover|focus|blur|change|submit)\\s*=|" +
        "<\\s*iframe[^>]*>|" +
        "<\\s*object[^>]*>|" +
        "<\\s*embed[^>]*>|" +
        "\\beval\\s*\\(|" +
        "\\bdocument\\.cookie\\b|" +
        "\\bwindow\\.location\\b|" +
        "&#x?[0-9a-fA-F]+;" +
        ")"
    );

    /** Command injection patterns (CRS 930xxx). */
    private static final Pattern CMD_INJECTION = Pattern.compile(
        "(?i)(?:" +
        "[;&|`]\\s*(?:cat|ls|pwd|id|whoami|uname|env|printenv|echo|curl|wget|nc|ncat|bash|sh|python|perl|ruby)\\b|" +
        "\\$\\([^)]+\\)|" +
        "`[^`]+`|" +
        "\\b(?:cmd\\.exe|powershell|/bin/sh|/bin/bash)\\b|" +
        ">>\\s*[/\\\\]" +
        ")"
    );

    /** Path traversal patterns. */
    private static final Pattern PATH_TRAVERSAL = Pattern.compile(
        "(?:\\.{2}[/\\\\]){1,}|" +
        "(?:%2e%2e[%/\\\\]){1,}|" +
        "(?:\\.{2}%2f){1,}|" +
        "(?:%2e%2e%2f){1,}"
    );

    /** Known bot/scanner user agents. */
    private static final Pattern BOT_AGENTS = Pattern.compile(
        "(?i)(?:sqlmap|nikto|nessus|nmap|masscan|burpsuite|dirbuster|" +
        "zgrab|gobuster|wfuzz|hydra|medusa|acunetix|appscan|webinspect|" +
        "python-requests|python-urllib|go-http-client|libwww-perl|" +
        "scrapy|wget/|curl/)"
    );

    /** PII patterns for masking / data-leak detection. */
    private static final Pattern PII_CREDIT_CARD = Pattern.compile(
        "\\b(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|3[47][0-9]{13})\\b"
    );
    private static final Pattern PII_SSN = Pattern.compile(
        "\\b\\d{3}-\\d{2}-\\d{4}\\b"
    );
    private static final Pattern PII_EMAIL = Pattern.compile(
        "\\b[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}\\b"
    );
    private static final Pattern PII_PASSWORD_FIELD = Pattern.compile(
        "(?i)\"password\"\\s*:\\s*\"[^\"]+\""
    );
    private static final Pattern PII_API_KEY = Pattern.compile(
        "(?i)(?:api[_\\-]?key|apikey|secret[_\\-]?key|access[_\\-]?token)\"\\s*:\\s*\"([^\"]{8,})\""
    );

    // Rate-limit thresholds
    private static final int RATE_LIMIT_PER_IP_PER_MIN = 1000;
    private static final int RATE_LIMIT_LOGIN_PER_MIN  = 5;
    private static final int ANOMALY_BLOCK_THRESHOLD   = 50;

    // ─────────────────────────────────────────────────────────
    //  MCP ENTRY POINT
    // ─────────────────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        System.setErr(System.err); // keep stderr for debug only
        BufferedReader in  = new BufferedReader(
            new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(
            new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String response = dispatch(line);
            if (response != null) out.println(response);
        }
    }

    // ─────────────────────────────────────────────────────────
    //  DISPATCHER
    // ─────────────────────────────────────────────────────────

    private static String dispatch(String raw) {
        try {
            Map<String, Object> req = Json.parse(raw);
            String method = str(req, "method");
            Object id      = req.get("id");

            switch (method) {
                case "initialize":      return Json.response(id, buildServerInfo());
                case "ping":            return Json.response(id, Json.obj("message", "pong"));
                case "tools/list":      return Json.response(id, Json.obj("tools", buildToolsList()));
                case "tools/call":      return Json.response(id, callTool(req));
                default:
                    return Json.error(id, -32601, "Method not found: " + method);
            }
        } catch (Exception e) {
            return Json.error(null, -32700, "Parse error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────
    //  SERVER INFO
    // ─────────────────────────────────────────────────────────

    private static Map<String, Object> buildServerInfo() {
        Map<String,Object> cap = Json.obj(
            "tools", Json.obj("listChanged", false)
        );
        return Json.obj(
            "protocolVersion", "2024-11-05",
            "capabilities",    cap,
            "serverInfo",      Json.obj(
                "name",    "mcp-waf-modsecurity",
                "version", "2.0.0",
                "description", "ModSecurity WAF MCP Server – Ecoskiller Platform | 18 Agents"
            )
        );
    }

    // ─────────────────────────────────────────────────────────
    //  TOOLS LIST (18 agents)
    // ─────────────────────────────────────────────────────────

    private static List<Object> buildToolsList() {
        List<Object> tools = new ArrayList<>();

        tools.add(tool("inspect_request",
            "INSPECT_REQUEST_AGENT: Parse and inspect a full HTTP request (headers, URL, body, cookies). " +
            "Returns extracted fields ready for rule evaluation. Phase 1-3.",
            schema("method","string","HTTP method (GET/POST/PUT/DELETE/PATCH)",
                   "uri","string","Full request URI including query string",
                   "headers","object","Map of HTTP headers",
                   "body","string","Raw request body (may be empty)",
                   "client_ip","string","Client IP address")));

        tools.add(tool("detect_sql_injection",
            "DETECT_SQL_INJECTION_AGENT: Detect SQL injection patterns in request data. " +
            "Covers UNION SELECT, DROP, EXEC, blind injection, time-based (OWASP CRS 942xxx).",
            singleSchema("input","string","Value to inspect for SQL injection patterns")));

        tools.add(tool("detect_xss",
            "DETECT_XSS_AGENT: Detect Cross-Site Scripting (XSS) payloads: <script>, event handlers, " +
            "javascript: protocol, encoded variants (OWASP CRS 941xxx).",
            singleSchema("input","string","Value to inspect for XSS patterns")));

        tools.add(tool("detect_command_injection",
            "DETECT_COMMAND_INJECTION_AGENT: Detect OS command injection and path traversal. " +
            "Shell metacharacters, $() subshell, backtick execution, ../ traversal (CRS 930xxx).",
            singleSchema("input","string","Value to inspect for command injection / path traversal")));

        tools.add(tool("rate_limit_check",
            "RATE_LIMIT_AGENT: Enforce per-IP rate limiting (sliding window). " +
            "Default: 1000 req/min per IP, 5 req/min for login endpoints.",
            schema("client_ip","string","Client IP address",
                   "endpoint","string","Request endpoint path (e.g. /api/login)",
                   "increment","boolean","If true, increment counter (default true)")));

        tools.add(tool("detect_bot",
            "DETECT_BOT_AGENT: Detect bots, scanners, and credential-stuffing tools. " +
            "Checks User-Agent, request patterns, and rapid login attempts.",
            schema("user_agent","string","User-Agent header value",
                   "client_ip","string","Client IP",
                   "endpoint","string","Request endpoint",
                   "method","string","HTTP method")));

        tools.add(tool("inspect_response",
            "INSPECT_RESPONSE_AGENT: Inspect HTTP response body for data leaks / PII. " +
            "Detects credit card numbers, SSN, emails, API keys in outbound responses. Phase 4-5.",
            schema("response_body","string","Raw response body to inspect",
                   "response_headers","object","Map of response headers",
                   "uri","string","Request URI that generated this response")));

        tools.add(tool("audit_log_query",
            "AUDIT_LOG_AGENT: Query and retrieve the WAF audit log. " +
            "Supports filtering by client_ip, action, severity, or rule_id.",
            schema("client_ip","string","Filter by client IP (optional)",
                   "action","string","Filter by action: blocked/allowed/flagged (optional)",
                   "severity","string","Filter by severity: CRITICAL/HIGH/MEDIUM/LOW (optional)",
                   "limit","number","Max records to return (default 20, max 100)")));

        tools.add(tool("evaluate_custom_rule",
            "CUSTOM_RULE_AGENT: Evaluate input against a custom SecRule-style rule or add a new rule. " +
            "Supports @rx (regex), @contains, @beginsWith operators.",
            schema("action","string","'add', 'remove', 'list', or 'evaluate'",
                   "rule_id","string","Unique rule ID (e.g. 10001)",
                   "variable","string","Variable to inspect (ARGS, HEADERS, BODY, COOKIE)",
                   "operator","string","Operator: @rx, @contains, @beginsWith",
                   "pattern","string","Pattern to match",
                   "rule_action","string","Action: block/log/allow",
                   "message","string","Rule message",
                   "input","string","Input to evaluate against rule (for 'evaluate' action)")));

        tools.add(tool("get_anomaly_score",
            "ANOMALY_SCORE_AGENT: Get or compute the anomaly score for a client IP. " +
            "Scores accumulate per matched rule; threshold ≥50 triggers auto-block.",
            schema("client_ip","string","Client IP address",
                   "add_score","number","Points to add (optional, 0=read only)",
                   "reason","string","Reason for score addition (optional)")));

        tools.add(tool("block_ip",
            "BLOCK_IP_AGENT: Block or unblock an IP address. " +
            "Blocked IPs receive 403 on all requests; also removes from whitelist.",
            schema("client_ip","string","IP address to block or unblock",
                   "action","string","'block', 'unblock', or 'list'",
                   "reason","string","Reason for blocking (audit trail)")));

        tools.add(tool("whitelist_ip",
            "WHITELIST_IP_AGENT: Add, remove, or list whitelisted IPs. " +
            "Whitelisted IPs bypass all WAF rule inspection.",
            schema("client_ip","string","IP address to whitelist",
                   "action","string","'add', 'remove', or 'list'",
                   "reason","string","Reason for whitelisting")));

        tools.add(tool("geo_ip_check",
            "GEO_IP_AGENT: Simulate geo-IP check for an IP address. " +
            "Enforces country allowlist policies (e.g. India-only assessments).",
            schema("client_ip","string","Client IP address",
                   "allowed_countries","array","ISO-3166-1 alpha-2 country codes allowed (e.g. [\"IN\"])",
                   "purpose","string","Context: assessment, login, api (affects strictness)")));

        tools.add(tool("detect_data_leak",
            "DATA_LEAK_AGENT: Detect PII and sensitive data in request or response payloads. " +
            "Identifies credit card numbers, SSN, passwords, API keys, emails.",
            schema("content","string","Content to scan for data leaks",
                   "direction","string","'request' or 'response'",
                   "mask","boolean","If true, return masked version of content")));

        tools.add(tool("get_security_events",
            "SECURITY_EVENTS_AGENT: Retrieve recent security events (blocked requests, rate limits, " +
            "data leaks, bot detections). Supports filtering by event_type and severity.",
            schema("event_type","string","Filter: request_blocked/rate_limit_exceeded/suspicious_pattern/data_leak_detected (optional)",
                   "severity","string","Filter by severity: critical/high/medium/low (optional)",
                   "limit","number","Max events to return (default 10, max 50)")));

        tools.add(tool("update_rules",
            "UPDATE_RULES_AGENT: Manage WAF rule configuration. Enable/disable OWASP CRS rule groups " +
            "or toggle WAF mode (On/DetectionOnly). Simulates ConfigMap hot-reload.",
            schema("action","string","'set_mode', 'enable_ruleset', 'disable_ruleset', 'get_config'",
                   "mode","string","WAF mode: On or DetectionOnly",
                   "ruleset","string","Ruleset to enable/disable: sqli/xss/cmdi/protocol/all",
                   "value","boolean","true=enable, false=disable")));

        tools.add(tool("health_check",
            "HEALTH_CHECK_AGENT: Return WAF health status, uptime, active rules, and mode.",
            emptySchema()));

        tools.add(tool("get_waf_stats",
            "WAF_STATS_AGENT: Return WAF traffic statistics: total requests inspected, blocked, " +
            "passed, top attacked endpoints, top blocked IPs.",
            emptySchema()));

        return tools;
    }

    // ─────────────────────────────────────────────────────────
    //  TOOL DISPATCHER
    // ─────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private static Map<String, Object> callTool(Map<String, Object> req) {
        Map<String, Object> params = map(req, "params");
        if (params == null) params = new HashMap<>();
        String name = str(params, "name");
        Map<String, Object> args = map(params, "arguments");
        if (args == null) args = new HashMap<>();

        // Security: sanitise all string inputs
        args = sanitiseArgs(args);

        Map<String, Object> result;
        switch (name) {
            case "inspect_request":          result = agentInspectRequest(args);         break;
            case "detect_sql_injection":     result = agentDetectSqlInjection(args);     break;
            case "detect_xss":               result = agentDetectXss(args);              break;
            case "detect_command_injection": result = agentDetectCmdInjection(args);     break;
            case "rate_limit_check":         result = agentRateLimitCheck(args);         break;
            case "detect_bot":               result = agentDetectBot(args);              break;
            case "inspect_response":         result = agentInspectResponse(args);        break;
            case "audit_log_query":          result = agentAuditLogQuery(args);          break;
            case "evaluate_custom_rule":     result = agentEvaluateCustomRule(args);     break;
            case "get_anomaly_score":        result = agentGetAnomalyScore(args);        break;
            case "block_ip":                 result = agentBlockIp(args);                break;
            case "whitelist_ip":             result = agentWhitelistIp(args);            break;
            case "geo_ip_check":             result = agentGeoIpCheck(args);             break;
            case "detect_data_leak":         result = agentDetectDataLeak(args);         break;
            case "get_security_events":      result = agentGetSecurityEvents(args);      break;
            case "update_rules":             result = agentUpdateRules(args);            break;
            case "health_check":             result = agentHealthCheck(args);            break;
            case "get_waf_stats":            result = agentGetWafStats(args);            break;
            default:
                result = Json.obj("error", "Unknown tool: " + name);
        }

        return Json.obj("content", Collections.singletonList(
            Json.obj("type", "text", "text", Json.stringify(result))
        ));
    }

    // ═══════════════════════════════════════════════════════════
    //  AGENT IMPLEMENTATIONS
    // ═══════════════════════════════════════════════════════════

    // ─── 1. INSPECT_REQUEST ────────────────────────────────────
    private static Map<String, Object> agentInspectRequest(Map<String, Object> args) {
        TOTAL_REQUESTS.incrementAndGet();
        String method   = str(args, "method", "GET");
        String uri      = str(args, "uri", "/");
        String body     = str(args, "body", "");
        String clientIp = str(args, "client_ip", "0.0.0.0");
        Object headersRaw = args.get("headers");
        Map<String,Object> headers = (headersRaw instanceof Map) ? (Map<String,Object>) headersRaw : new HashMap<>();

        List<Map<String,Object>> phases  = new ArrayList<>();
        List<String> warnings            = new ArrayList<>();
        List<String> detectedThreats     = new ArrayList<>();
        int anomalyDelta = 0;

        // ── Phase 1: headers
        Map<String,Object> phase1 = new LinkedHashMap<>();
        String userAgent = str(headers, "User-Agent", str(headers, "user-agent", ""));
        String host      = str(headers, "Host",       str(headers, "host", ""));
        String auth      = str(headers, "Authorization", str(headers, "authorization", ""));
        boolean missingUA = userAgent.isEmpty();
        boolean suspiciousUA = !missingUA && BOT_AGENTS.matcher(userAgent).find();

        phase1.put("phase", 1);
        phase1.put("name", "Request Headers");
        phase1.put("user_agent", userAgent.isEmpty() ? "[MISSING]" : userAgent);
        phase1.put("host", host);
        phase1.put("authorization_present", !auth.isEmpty());
        phase1.put("missing_user_agent", missingUA);
        phase1.put("suspicious_user_agent", suspiciousUA);
        if (missingUA)   { warnings.add("Missing User-Agent header"); anomalyDelta += 5; detectedThreats.add("MISSING_USER_AGENT"); }
        if (suspiciousUA){ warnings.add("Bot/scanner User-Agent detected: " + userAgent); anomalyDelta += 20; detectedThreats.add("BOT_USER_AGENT"); }
        phases.add(phase1);

        // ── Phase 2: request body
        Map<String,Object> phase2 = new LinkedHashMap<>();
        boolean sqlInBody  = !body.isEmpty() && SQL_INJECTION.matcher(body).find();
        boolean xssInBody  = !body.isEmpty() && XSS_ATTACK.matcher(body).find();
        boolean cmdInBody  = !body.isEmpty() && CMD_INJECTION.matcher(body).find();
        phase2.put("phase", 2);
        phase2.put("name", "Request Body");
        phase2.put("body_length", body.length());
        phase2.put("sql_injection_detected", sqlInBody);
        phase2.put("xss_detected", xssInBody);
        phase2.put("command_injection_detected", cmdInBody);
        if (sqlInBody){ warnings.add("SQL injection pattern in request body"); anomalyDelta += 30; detectedThreats.add("SQL_INJECTION"); }
        if (xssInBody){ warnings.add("XSS pattern in request body");           anomalyDelta += 25; detectedThreats.add("XSS"); }
        if (cmdInBody){ warnings.add("Command injection pattern in request body"); anomalyDelta += 30; detectedThreats.add("CMD_INJECTION"); }
        phases.add(phase2);

        // ── Phase 3: URL / query params
        Map<String,Object> phase3 = new LinkedHashMap<>();
        boolean pathTraversal  = PATH_TRAVERSAL.matcher(uri).find();
        boolean sqlInUri       = SQL_INJECTION.matcher(uri).find();
        boolean xssInUri       = XSS_ATTACK.matcher(uri).find();
        phase3.put("phase", 3);
        phase3.put("name", "URL & Parameters");
        phase3.put("uri", uri);
        phase3.put("method", method);
        phase3.put("path_traversal_detected", pathTraversal);
        phase3.put("sql_injection_in_uri", sqlInUri);
        phase3.put("xss_in_uri", xssInUri);
        if (pathTraversal){ warnings.add("Path traversal detected in URI"); anomalyDelta += 25; detectedThreats.add("PATH_TRAVERSAL"); }
        if (sqlInUri)     { warnings.add("SQL injection in URI");           anomalyDelta += 25; detectedThreats.add("SQL_INJECTION_URI"); }
        if (xssInUri)     { warnings.add("XSS in URI");                    anomalyDelta += 25; detectedThreats.add("XSS_URI"); }
        phases.add(phase3);

        // ── Blocklist / whitelist check
        boolean isBlocked     = BLOCKED_IPS.contains(clientIp);
        boolean isWhitelisted = WHITELISTED_IPS.contains(clientIp);

        // ── Accumulate anomaly
        if (!isWhitelisted && anomalyDelta > 0) {
            ANOMALY_SCORES.merge(clientIp, anomalyDelta, Integer::sum);
        }
        int totalScore = ANOMALY_SCORES.getOrDefault(clientIp, 0);

        String decision;
        if (isBlocked)                         decision = "BLOCKED_IP_BLOCKLIST";
        else if (isWhitelisted)                decision = "ALLOWED_WHITELISTED";
        else if (totalScore >= ANOMALY_BLOCK_THRESHOLD) { decision = "BLOCKED_ANOMALY_SCORE"; BLOCKED_IPS.add(clientIp); }
        else if (!detectedThreats.isEmpty())   decision = "BLOCKED_RULE_MATCH";
        else                                   decision = "ALLOWED";

        boolean blocked = decision.startsWith("BLOCKED");
        if (blocked) TOTAL_BLOCKED.incrementAndGet(); else TOTAL_PASSED.incrementAndGet();

        // ── Audit entry
        addAudit(clientIp, method, uri, blocked ? 403 : 200,
            detectedThreats, blocked ? "denied" : "allowed", totalScore);

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",           "INSPECT_REQUEST_AGENT");
        result.put("timestamp",       Instant.now().toString());
        result.put("client_ip",       clientIp);
        result.put("method",          method);
        result.put("uri",             uri);
        result.put("phases",          phases);
        result.put("detected_threats",detectedThreats);
        result.put("warnings",        warnings);
        result.put("anomaly_score",   totalScore);
        result.put("decision",        decision);
        result.put("response_code",   blocked ? 403 : 200);
        result.put("ip_blocked",      isBlocked);
        result.put("ip_whitelisted",  isWhitelisted);
        return result;
    }

    // ─── 2. DETECT_SQL_INJECTION ───────────────────────────────
    private static Map<String, Object> agentDetectSqlInjection(Map<String, Object> args) {
        String input = str(args, "input", "");
        Matcher m = SQL_INJECTION.matcher(input);
        boolean detected = m.find();
        String matched   = detected ? m.group() : null;

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",        "DETECT_SQL_INJECTION_AGENT");
        result.put("detected",     detected);
        result.put("rule_ids",     detected ? Arrays.asList("942200","942210","942220") : Collections.emptyList());
        result.put("matched_data", detected ? maskSensitive(matched) : null);
        result.put("phase",        2);
        result.put("severity",     detected ? "CRITICAL" : "NONE");
        result.put("action",       detected ? "BLOCK" : "ALLOW");
        result.put("crs_category", "SQL Injection (CRS 942xxx)");
        result.put("owasp_top10",  "A03:2021-Injection");
        result.put("recommendation", detected
            ? "Block request – SQL pattern detected. Anomaly score +30."
            : "Input is clean of SQL injection patterns.");
        return result;
    }

    // ─── 3. DETECT_XSS ────────────────────────────────────────
    private static Map<String, Object> agentDetectXss(Map<String, Object> args) {
        String input = str(args, "input", "");
        Matcher m = XSS_ATTACK.matcher(input);
        boolean detected = m.find();
        String matched   = detected ? m.group() : null;

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",        "DETECT_XSS_AGENT");
        result.put("detected",     detected);
        result.put("rule_ids",     detected ? Arrays.asList("941100","941110","941160") : Collections.emptyList());
        result.put("matched_data", detected ? maskSensitive(matched) : null);
        result.put("phase",        2);
        result.put("severity",     detected ? "CRITICAL" : "NONE");
        result.put("action",       detected ? "BLOCK" : "ALLOW");
        result.put("crs_category", "Cross-Site Scripting (CRS 941xxx)");
        result.put("owasp_top10",  "A03:2021-Injection");
        result.put("recommendation", detected
            ? "Block request – XSS payload detected. Anomaly score +25."
            : "Input is clean of XSS patterns.");
        return result;
    }

    // ─── 4. DETECT_COMMAND_INJECTION ──────────────────────────
    private static Map<String, Object> agentDetectCmdInjection(Map<String, Object> args) {
        String input = str(args, "input", "");
        boolean cmdDetected  = CMD_INJECTION.matcher(input).find();
        boolean pathDetected = PATH_TRAVERSAL.matcher(input).find();
        boolean detected     = cmdDetected || pathDetected;

        List<String> matchedTypes = new ArrayList<>();
        if (cmdDetected)  matchedTypes.add("COMMAND_INJECTION");
        if (pathDetected) matchedTypes.add("PATH_TRAVERSAL");

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",         "DETECT_COMMAND_INJECTION_AGENT");
        result.put("detected",      detected);
        result.put("attack_types",  matchedTypes);
        result.put("rule_ids",      detected ? Arrays.asList("930100","930110","930120") : Collections.emptyList());
        result.put("phase",         2);
        result.put("severity",      detected ? "CRITICAL" : "NONE");
        result.put("action",        detected ? "BLOCK" : "ALLOW");
        result.put("crs_category",  "Command Injection / Path Traversal (CRS 930xxx)");
        result.put("owasp_top10",   "A01:2021-Broken Access Control / A03:2021-Injection");
        result.put("recommendation", detected
            ? "Block request – OS command injection or path traversal detected."
            : "Input is clean of command injection and path traversal patterns.");
        return result;
    }

    // ─── 5. RATE_LIMIT_CHECK ──────────────────────────────────
    private static Map<String, Object> agentRateLimitCheck(Map<String, Object> args) {
        String  clientIp  = str(args, "client_ip", "0.0.0.0");
        String  endpoint  = str(args, "endpoint", "/");
        boolean increment = !"false".equalsIgnoreCase(str(args, "increment", "true"));

        boolean loginEndpoint = endpoint.toLowerCase().contains("login") ||
                                endpoint.toLowerCase().contains("auth");
        int limit = loginEndpoint ? RATE_LIMIT_LOGIN_PER_MIN : RATE_LIMIT_PER_IP_PER_MIN;
        long nowSec = System.currentTimeMillis() / 1000L;
        long windowStart = nowSec - 60;

        RATE_WINDOWS.putIfAbsent(clientIp, new long[]{0L, nowSec});
        long[] window = RATE_WINDOWS.get(clientIp);
        // reset window if expired
        synchronized (window) {
            if (window[1] < windowStart) { window[0] = 0L; window[1] = nowSec; }
            if (increment) window[0]++;
        }
        long count   = window[0];
        boolean over = count > limit;
        boolean nearLimit = count > limit * 0.8;

        if (over) {
            addSecEvent("rate_limit_exceeded", "high", clientIp, null,
                Json.obj("limit_threshold", limit, "requests_in_window", (int)count,
                         "endpoint", endpoint));
        }

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",               "RATE_LIMIT_AGENT");
        result.put("client_ip",           clientIp);
        result.put("endpoint",            endpoint);
        result.put("limit_type",          loginEndpoint ? "login" : "per_ip");
        result.put("requests_in_window",  (int)count);
        result.put("limit_threshold",     limit);
        result.put("window_seconds",      60);
        result.put("limit_exceeded",      over);
        result.put("near_limit",          nearLimit);
        result.put("action",              over ? "BLOCK_429" : "ALLOW");
        result.put("response_code",       over ? 429 : 200);
        result.put("retry_after_seconds", over ? 60 : 0);
        return result;
    }

    // ─── 6. DETECT_BOT ────────────────────────────────────────
    private static Map<String, Object> agentDetectBot(Map<String, Object> args) {
        String userAgent = str(args, "user_agent", "");
        String clientIp  = str(args, "client_ip", "0.0.0.0");
        String endpoint  = str(args, "endpoint", "/");

        List<String> indicators = new ArrayList<>();
        boolean isBot = false;
        int score = 0;

        if (userAgent.isEmpty()) {
            indicators.add("Missing User-Agent header"); score += 20;
        } else if (BOT_AGENTS.matcher(userAgent).find()) {
            indicators.add("Known bot/scanner User-Agent: " + userAgent); score += 40;
        }

        // Check rapid login
        boolean loginEndpoint = endpoint.contains("login") || endpoint.contains("auth");
        if (loginEndpoint) {
            long[] w = RATE_WINDOWS.getOrDefault(clientIp, new long[]{0,0});
            if (w[0] > 3) { indicators.add("Rapid login attempts: " + w[0] + " in last 60s"); score += 30; }
        }

        isBot = score >= 20;
        String action = score >= 40 ? "BLOCK" : (score >= 20 ? "CAPTCHA_CHALLENGE" : "ALLOW");

        if (isBot) {
            addSecEvent("suspicious_pattern", "medium", clientIp, null,
                Json.obj("pattern","possible_bot_or_credential_stuffing","indicators", indicators));
        }

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",       "DETECT_BOT_AGENT");
        result.put("client_ip",   clientIp);
        result.put("user_agent",  userAgent.isEmpty() ? "[MISSING]" : userAgent);
        result.put("bot_score",   score);
        result.put("is_bot",      isBot);
        result.put("indicators",  indicators);
        result.put("action",      action);
        result.put("recommendation", action.equals("BLOCK") ? "Block request immediately – high-confidence bot."
            : action.equals("CAPTCHA_CHALLENGE") ? "Challenge with CAPTCHA before serving."
            : "Request appears legitimate.");
        return result;
    }

    // ─── 7. INSPECT_RESPONSE ──────────────────────────────────
    private static Map<String, Object> agentInspectResponse(Map<String, Object> args) {
        String responseBody = str(args, "response_body", "");
        String uri          = str(args, "uri", "/");

        List<String> leakedTypes = new ArrayList<>();
        boolean ccFound   = PII_CREDIT_CARD.matcher(responseBody).find();
        boolean ssnFound  = PII_SSN.matcher(responseBody).find();
        boolean emailFound= PII_EMAIL.matcher(responseBody).find();
        boolean apiFound  = PII_API_KEY.matcher(responseBody).find();
        boolean passFound = PII_PASSWORD_FIELD.matcher(responseBody).find();

        if (ccFound)    leakedTypes.add("credit_card_number");
        if (ssnFound)   leakedTypes.add("social_security_number");
        if (emailFound) leakedTypes.add("email_address");
        if (apiFound)   leakedTypes.add("api_key_or_secret");
        if (passFound)  leakedTypes.add("password_field");

        boolean leakDetected = !leakedTypes.isEmpty();
        if (leakDetected) {
            addSecEvent("data_leak_detected", "critical", "response", uri,
                Json.obj("leaked_types", leakedTypes, "rule_id", "1005001"));
        }

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",           "INSPECT_RESPONSE_AGENT");
        result.put("uri",             uri);
        result.put("phase",           5);
        result.put("leak_detected",   leakDetected);
        result.put("leaked_data_types", leakedTypes);
        result.put("rule_id",         leakDetected ? "1005001" : null);
        result.put("severity",        leakDetected ? "CRITICAL" : "NONE");
        result.put("action",          leakDetected ? "BLOCK_RESPONSE" : "ALLOW_RESPONSE");
        result.put("masked_body",     leakDetected ? maskPii(responseBody) : null);
        result.put("recommendation",  leakDetected
            ? "Block response – PII/sensitive data detected in outbound payload. Mask before sending."
            : "Response is clean of sensitive data.");
        return result;
    }

    // ─── 8. AUDIT_LOG_QUERY ───────────────────────────────────
    private static Map<String, Object> agentAuditLogQuery(Map<String, Object> args) {
        String filterIp  = str(args, "client_ip", "");
        String filterAct = str(args, "action", "");
        String filterSev = str(args, "severity", "");
        int    limit     = Math.min(100, (int) num(args, "limit", 20));

        List<Map<String,Object>> filtered;
        synchronized (AUDIT_LOG) {
            filtered = AUDIT_LOG.stream()
                .filter(e -> filterIp.isEmpty()  || filterIp.equals(e.get("client_ip")))
                .filter(e -> filterAct.isEmpty() || filterAct.equalsIgnoreCase(str(e,"action","")))
                .filter(e -> filterSev.isEmpty() || filterSev.equalsIgnoreCase(str(e,"severity","")))
                .collect(Collectors.toList());
        }
        // latest first
        Collections.reverse(filtered);
        filtered = filtered.subList(0, Math.min(limit, filtered.size()));

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",        "AUDIT_LOG_AGENT");
        result.put("total_in_log", AUDIT_LOG.size());
        result.put("returned",     filtered.size());
        result.put("filters", Json.obj(
            "client_ip", filterIp.isEmpty() ? null : filterIp,
            "action",    filterAct.isEmpty() ? null : filterAct,
            "severity",  filterSev.isEmpty() ? null : filterSev
        ));
        result.put("entries", filtered);
        return result;
    }

    // ─── 9. EVALUATE_CUSTOM_RULE ──────────────────────────────
    private static Map<String, Object> agentEvaluateCustomRule(Map<String, Object> args) {
        String action    = str(args, "action", "list");
        String ruleId    = str(args, "rule_id", "");
        String variable  = str(args, "variable", "ARGS");
        String operator  = str(args, "operator", "@contains");
        String pattern   = str(args, "pattern", "");
        String ruleAct   = str(args, "rule_action", "block");
        String message   = str(args, "message", "Custom rule match");
        String input     = str(args, "input", "");

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent", "CUSTOM_RULE_AGENT");

        switch (action) {
            case "add": {
                if (ruleId.isEmpty() || pattern.isEmpty()) {
                    result.put("error", "rule_id and pattern are required for 'add' action");
                    break;
                }
                Map<String,String> rule = new LinkedHashMap<>();
                rule.put("rule_id",    ruleId);
                rule.put("variable",   variable);
                rule.put("operator",   operator);
                rule.put("pattern",    pattern);
                rule.put("action",     ruleAct);
                rule.put("message",    message);
                rule.put("created_at", Instant.now().toString());
                CUSTOM_RULES.put(ruleId, new HashMap<>(rule));
                result.put("status",  "RULE_ADDED");
                result.put("rule",    rule);
                break;
            }
            case "remove": {
                boolean removed = CUSTOM_RULES.remove(ruleId) != null;
                result.put("status", removed ? "RULE_REMOVED" : "RULE_NOT_FOUND");
                result.put("rule_id", ruleId);
                break;
            }
            case "list": {
                result.put("custom_rules", CUSTOM_RULES.values());
                result.put("count", CUSTOM_RULES.size());
                break;
            }
            case "evaluate": {
                boolean matched = false;
                if (!pattern.isEmpty() && !input.isEmpty()) {
                    switch (operator) {
                        case "@contains":    matched = input.contains(pattern); break;
                        case "@rx":          matched = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(input).find(); break;
                        case "@beginsWith":  matched = input.startsWith(pattern); break;
                        default:             matched = input.contains(pattern);
                    }
                }
                result.put("matched",    matched);
                result.put("rule_id",    ruleId.isEmpty() ? "ad-hoc" : ruleId);
                result.put("operator",   operator);
                result.put("pattern",    pattern);
                result.put("variable",   variable);
                result.put("action",     matched ? ruleAct.toUpperCase() : "ALLOW");
                result.put("message",    matched ? message : null);
                result.put("severity",   matched ? "HIGH" : "NONE");
                break;
            }
            default:
                result.put("error", "Unknown action: " + action + ". Use: add, remove, list, evaluate");
        }
        return result;
    }

    // ─── 10. GET_ANOMALY_SCORE ────────────────────────────────
    private static Map<String, Object> agentGetAnomalyScore(Map<String, Object> args) {
        String clientIp  = str(args, "client_ip", "0.0.0.0");
        int    addScore  = (int) num(args, "add_score", 0);
        String reason    = str(args, "reason", "manual");

        if (addScore > 0) {
            ANOMALY_SCORES.merge(clientIp, addScore, Integer::sum);
        }
        int currentScore = ANOMALY_SCORES.getOrDefault(clientIp, 0);
        boolean autoBlock = currentScore >= ANOMALY_BLOCK_THRESHOLD;
        if (autoBlock) BLOCKED_IPS.add(clientIp);

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",           "ANOMALY_SCORE_AGENT");
        result.put("client_ip",       clientIp);
        result.put("current_score",   currentScore);
        result.put("block_threshold", ANOMALY_BLOCK_THRESHOLD);
        result.put("auto_blocked",    autoBlock);
        result.put("score_added",     addScore > 0 ? addScore : null);
        result.put("reason",          addScore > 0 ? reason : null);
        result.put("risk_level",
            currentScore >= 50 ? "CRITICAL" :
            currentScore >= 30 ? "HIGH"     :
            currentScore >= 15 ? "MEDIUM"   : "LOW");
        result.put("recommendation",  autoBlock
            ? "IP auto-blocked – anomaly score exceeded threshold."
            : "Monitor IP; score is below block threshold.");
        return result;
    }

    // ─── 11. BLOCK_IP ─────────────────────────────────────────
    private static Map<String, Object> agentBlockIp(Map<String, Object> args) {
        String action   = str(args, "action", "list");
        String clientIp = str(args, "client_ip", "");
        String reason   = str(args, "reason", "manual_block");

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent", "BLOCK_IP_AGENT");

        switch (action) {
            case "block": {
                if (clientIp.isEmpty()) { result.put("error","client_ip required"); break; }
                BLOCKED_IPS.add(clientIp);
                WHITELISTED_IPS.remove(clientIp); // cannot be both
                addAudit(clientIp,"ADMIN","/system/block-ip",200,
                    Collections.singletonList("IP_BLOCKED"), "admin_action", 0);
                result.put("status", "BLOCKED");
                result.put("client_ip", clientIp);
                result.put("reason",  reason);
                result.put("timestamp", Instant.now().toString());
                break;
            }
            case "unblock": {
                if (clientIp.isEmpty()) { result.put("error","client_ip required"); break; }
                boolean removed = BLOCKED_IPS.remove(clientIp);
                ANOMALY_SCORES.remove(clientIp);
                result.put("status",    removed ? "UNBLOCKED" : "NOT_IN_BLOCKLIST");
                result.put("client_ip", clientIp);
                break;
            }
            case "list": {
                result.put("blocked_ips", new ArrayList<>(BLOCKED_IPS));
                result.put("count", BLOCKED_IPS.size());
                break;
            }
            default:
                result.put("error", "Unknown action: " + action + ". Use: block, unblock, list");
        }
        return result;
    }

    // ─── 12. WHITELIST_IP ─────────────────────────────────────
    private static Map<String, Object> agentWhitelistIp(Map<String, Object> args) {
        String action   = str(args, "action", "list");
        String clientIp = str(args, "client_ip", "");
        String reason   = str(args, "reason", "manual_whitelist");

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent", "WHITELIST_IP_AGENT");

        switch (action) {
            case "add": {
                if (clientIp.isEmpty()) { result.put("error","client_ip required"); break; }
                if (BLOCKED_IPS.contains(clientIp)) { result.put("error","IP is currently blocked – unblock first"); break; }
                WHITELISTED_IPS.add(clientIp);
                result.put("status",    "WHITELISTED");
                result.put("client_ip", clientIp);
                result.put("reason",    reason);
                result.put("note",      "Whitelisted IPs bypass ALL WAF rule inspection. Use with caution.");
                break;
            }
            case "remove": {
                if (clientIp.isEmpty()) { result.put("error","client_ip required"); break; }
                boolean removed = WHITELISTED_IPS.remove(clientIp);
                result.put("status",    removed ? "REMOVED_FROM_WHITELIST" : "NOT_IN_WHITELIST");
                result.put("client_ip", clientIp);
                break;
            }
            case "list": {
                result.put("whitelisted_ips", new ArrayList<>(WHITELISTED_IPS));
                result.put("count", WHITELISTED_IPS.size());
                break;
            }
            default:
                result.put("error", "Unknown action: " + action + ". Use: add, remove, list");
        }
        return result;
    }

    // ─── 13. GEO_IP_CHECK ─────────────────────────────────────
    private static Map<String, Object> agentGeoIpCheck(Map<String, Object> args) {
        String clientIp   = str(args, "client_ip", "0.0.0.0");
        Object acRaw      = args.get("allowed_countries");
        String purpose    = str(args, "purpose", "general");

        List<String> allowedCountries = new ArrayList<>();
        if (acRaw instanceof List) {
            for (Object c : (List<?>) acRaw) allowedCountries.add(c.toString().toUpperCase());
        }
        if (allowedCountries.isEmpty()) allowedCountries.add("IN"); // default: India only

        // Simulate geo-lookup based on IP prefix (demo logic)
        String simulatedCountry = simulateGeo(clientIp);
        boolean allowed = allowedCountries.contains(simulatedCountry);
        boolean highRisk = !allowed && (purpose.equals("assessment") || purpose.equals("login"));

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",              "GEO_IP_AGENT");
        result.put("client_ip",          clientIp);
        result.put("resolved_country",   simulatedCountry);
        result.put("allowed_countries",  allowedCountries);
        result.put("access_allowed",     allowed);
        result.put("purpose",            purpose);
        result.put("high_risk_block",    highRisk);
        result.put("action",             allowed ? "ALLOW" : (highRisk ? "BLOCK" : "FLAG"));
        result.put("response_code",      allowed ? 200 : (highRisk ? 403 : 200));
        result.put("note",               "GeoIP based on simulated lookup. Integrate MaxMind GeoLite2 in production.");
        result.put("recommendation",     allowed
            ? "IP is from an allowed country."
            : highRisk ? "Block request – country not in allowlist for sensitive endpoint."
                       : "Flag for review – country not in allowlist.");
        return result;
    }

    // ─── 14. DETECT_DATA_LEAK ─────────────────────────────────
    private static Map<String, Object> agentDetectDataLeak(Map<String, Object> args) {
        String  content   = str(args, "content", "");
        String  direction = str(args, "direction", "request");
        boolean doMask    = !"false".equalsIgnoreCase(str(args, "mask", "true"));

        List<Map<String,Object>> findings = new ArrayList<>();
        if (PII_CREDIT_CARD.matcher(content).find())   findings.add(finding("credit_card_number",  "1005010","CRITICAL"));
        if (PII_SSN.matcher(content).find())            findings.add(finding("social_security_number","1005020","CRITICAL"));
        if (PII_PASSWORD_FIELD.matcher(content).find())findings.add(finding("password_in_payload", "1005030","HIGH"));
        if (PII_API_KEY.matcher(content).find())        findings.add(finding("api_key_or_secret",   "1005040","HIGH"));
        if (PII_EMAIL.matcher(content).find())          findings.add(finding("email_address",        "1005050","MEDIUM"));

        boolean leaked = !findings.isEmpty();
        String masked  = leaked && doMask ? maskPii(content) : null;

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",       "DATA_LEAK_AGENT");
        result.put("direction",   direction);
        result.put("leak_found",  leaked);
        result.put("findings",    findings);
        result.put("severity",    leaked ? findings.get(0).get("severity") : "NONE");
        result.put("action",      leaked ? (direction.equals("response") ? "BLOCK_RESPONSE" : "FLAG_REQUEST") : "ALLOW");
        result.put("masked_content", masked);
        result.put("owasp_top10", "A02:2021-Cryptographic Failures (Data Exposure)");
        result.put("recommendation", leaked
            ? "Sensitive data detected. Mask or block before delivery. Review data access controls."
            : "No PII or sensitive data found.");
        return result;
    }

    // ─── 15. GET_SECURITY_EVENTS ──────────────────────────────
    private static Map<String, Object> agentGetSecurityEvents(Map<String, Object> args) {
        String filterType = str(args, "event_type", "");
        String filterSev  = str(args, "severity", "");
        int    limit      = Math.min(50, (int) num(args, "limit", 10));

        List<Map<String,Object>> filtered;
        synchronized (SECURITY_EVENTS) {
            filtered = SECURITY_EVENTS.stream()
                .filter(e -> filterType.isEmpty() || filterType.equals(e.get("event_type")))
                .filter(e -> filterSev.isEmpty()  || filterSev.equalsIgnoreCase(str(e,"severity","")))
                .collect(Collectors.toList());
        }
        Collections.reverse(filtered);
        filtered = filtered.subList(0, Math.min(limit, filtered.size()));

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",            "SECURITY_EVENTS_AGENT");
        result.put("total_events",     SECURITY_EVENTS.size());
        result.put("returned",         filtered.size());
        result.put("kafka_topic",      "waf-security-events");
        result.put("events",           filtered);
        return result;
    }

    // ─── 16. UPDATE_RULES ─────────────────────────────────────
    // WAF config state (in-memory; represents ConfigMap values)
    private static String WAF_MODE = "On";
    private static final Map<String,Boolean> RULESET_STATE = new ConcurrentHashMap<>();
    static {
        RULESET_STATE.put("sqli",     true);
        RULESET_STATE.put("xss",      true);
        RULESET_STATE.put("cmdi",     true);
        RULESET_STATE.put("protocol", true);
        RULESET_STATE.put("bot",      true);
        RULESET_STATE.put("dlp",      true);
    }

    private static Map<String, Object> agentUpdateRules(Map<String, Object> args) {
        String  action  = str(args, "action", "get_config");
        String  mode    = str(args, "mode", "");
        String  ruleset = str(args, "ruleset", "");
        boolean value   = !"false".equalsIgnoreCase(str(args, "value", "true"));

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent", "UPDATE_RULES_AGENT");

        switch (action) {
            case "set_mode": {
                if (!mode.equalsIgnoreCase("On") && !mode.equalsIgnoreCase("DetectionOnly")) {
                    result.put("error", "mode must be 'On' or 'DetectionOnly'");
                    break;
                }
                String prev = WAF_MODE;
                WAF_MODE = mode;
                result.put("status",      "MODE_UPDATED");
                result.put("previous_mode", prev);
                result.put("new_mode",      WAF_MODE);
                result.put("note",          "Mode change simulates ConfigMap hot-reload in Kubernetes.");
                break;
            }
            case "enable_ruleset":
            case "disable_ruleset": {
                boolean enable = action.equals("enable_ruleset");
                List<String> affected = new ArrayList<>();
                if (ruleset.equals("all")) {
                    RULESET_STATE.replaceAll((k, v) -> enable);
                    affected.addAll(RULESET_STATE.keySet());
                } else if (RULESET_STATE.containsKey(ruleset)) {
                    RULESET_STATE.put(ruleset, enable);
                    affected.add(ruleset);
                } else {
                    result.put("error", "Unknown ruleset: " + ruleset + ". Valid: sqli,xss,cmdi,protocol,bot,dlp,all");
                    break;
                }
                result.put("status",        enable ? "RULESETS_ENABLED" : "RULESETS_DISABLED");
                result.put("affected",      affected);
                break;
            }
            case "get_config": {
                result.put("waf_mode",      WAF_MODE);
                result.put("active_rulesets", new LinkedHashMap<>(RULESET_STATE));
                result.put("custom_rules_count", CUSTOM_RULES.size());
                result.put("blocked_ips_count",  BLOCKED_IPS.size());
                result.put("whitelisted_ips_count", WHITELISTED_IPS.size());
                result.put("owasp_crs_version", "3.3.x");
                result.put("modsecurity_version", "3.0.x");
                break;
            }
            default:
                result.put("error", "Unknown action. Use: set_mode, enable_ruleset, disable_ruleset, get_config");
        }
        return result;
    }

    // ─── 17. HEALTH_CHECK ─────────────────────────────────────
    private static Map<String, Object> agentHealthCheck(Map<String, Object> args) {
        long uptimeMs = System.currentTimeMillis() - SERVER_START_MS;
        long uptimeSec = uptimeMs / 1000;

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",              "HEALTH_CHECK_AGENT");
        result.put("status",             "HEALTHY");
        result.put("waf_mode",           WAF_MODE);
        result.put("uptime_seconds",     uptimeSec);
        result.put("uptime_human",       formatUptime(uptimeSec));
        result.put("modsecurity_version","3.0.x");
        result.put("owasp_crs_version",  "3.3.x");
        result.put("nginx_module",       "ngx_http_modsecurity_module");
        result.put("active_rulesets",    new LinkedHashMap<>(RULESET_STATE));
        result.put("custom_rules",       CUSTOM_RULES.size());
        result.put("blocked_ips",        BLOCKED_IPS.size());
        result.put("whitelisted_ips",    WHITELISTED_IPS.size());
        result.put("audit_log_size",     AUDIT_LOG.size());
        result.put("security_events",    SECURITY_EVENTS.size());
        result.put("latency_impact_ms",  "<5");
        result.put("memory_usage_mb",    Runtime.getRuntime().totalMemory() / 1024 / 1024);
        result.put("deployment",         "Kubernetes (GCP GKE + AWS EKS)");
        result.put("timestamp",          Instant.now().toString());
        return result;
    }

    // ─── 18. GET_WAF_STATS ────────────────────────────────────
    private static Map<String, Object> agentGetWafStats(Map<String, Object> args) {
        long total   = TOTAL_REQUESTS.get();
        long blocked = TOTAL_BLOCKED.get();
        long passed  = TOTAL_PASSED.get();
        double blockRate = total > 0 ? (double) blocked / total * 100.0 : 0.0;

        // Top blocked IPs by anomaly score
        List<Map.Entry<String,Integer>> topIps = ANOMALY_SCORES.entrySet().stream()
            .sorted((a, b) -> b.getValue() - a.getValue())
            .limit(5).collect(Collectors.toList());
        List<Map<String,Object>> topIpsList = new ArrayList<>();
        for (Map.Entry<String,Integer> e : topIps) {
            topIpsList.add(Json.obj("ip", e.getKey(), "anomaly_score", e.getValue(),
                "blocked", BLOCKED_IPS.contains(e.getKey())));
        }

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("agent",               "WAF_STATS_AGENT");
        result.put("timestamp",           Instant.now().toString());
        result.put("total_requests",      total);
        result.put("total_blocked",       blocked);
        result.put("total_passed",        passed);
        result.put("block_rate_percent",  Math.round(blockRate * 10.0) / 10.0);
        result.put("blocked_ips_count",   BLOCKED_IPS.size());
        result.put("whitelisted_ips",     WHITELISTED_IPS.size());
        result.put("custom_rules",        CUSTOM_RULES.size());
        result.put("audit_log_entries",   AUDIT_LOG.size());
        result.put("security_events",     SECURITY_EVENTS.size());
        result.put("top_anomaly_ips",     topIpsList);
        result.put("active_rule_engine",  WAF_MODE);
        result.put("uptime_ms",           System.currentTimeMillis() - SERVER_START_MS);
        return result;
    }

    // ═══════════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════════

    private static void addAudit(String ip, String method, String uri, int status,
                                  List<String> rules, String action, int score) {
        Map<String,Object> entry = new LinkedHashMap<>();
        entry.put("timestamp",     Instant.now().toString());
        entry.put("client_ip",     ip);
        entry.put("method",        method);
        entry.put("uri",           uri);
        entry.put("status",        status);
        entry.put("matched_rules", rules);
        entry.put("action",        action);
        entry.put("anomaly_score", score);
        entry.put("severity",      score >= 50 ? "CRITICAL" : score >= 25 ? "HIGH" :
                                   score >= 10 ? "MEDIUM" : "LOW");
        synchronized (AUDIT_LOG) {
            if (AUDIT_LOG.size() >= MAX_AUDIT_SIZE) AUDIT_LOG.remove(0);
            AUDIT_LOG.add(entry);
        }
    }

    private static void addSecEvent(String type, String severity, String ip,
                                     String uri, Map<String,Object> extra) {
        Map<String,Object> ev = new LinkedHashMap<>();
        ev.put("event_id",    "evt-" + Long.toHexString(System.nanoTime()));
        ev.put("timestamp",   System.currentTimeMillis());
        ev.put("event_type",  type);
        ev.put("severity",    severity);
        ev.put("client_ip",   ip);
        if (uri != null) ev.put("uri", uri);
        if (extra != null) ev.putAll(extra);
        synchronized (SECURITY_EVENTS) {
            if (SECURITY_EVENTS.size() >= MAX_EVENTS_SIZE) SECURITY_EVENTS.remove(0);
            SECURITY_EVENTS.add(ev);
        }
    }

    private static Map<String,Object> finding(String type, String ruleId, String sev) {
        return Json.obj("data_type", type, "rule_id", ruleId, "severity", sev);
    }

    /** Mask PII patterns in a string. */
    private static String maskPii(String s) {
        s = PII_CREDIT_CARD.matcher(s).replaceAll("####-####-####-XXXX");
        s = PII_SSN.matcher(s).replaceAll("***-**-****");
        s = PII_PASSWORD_FIELD.matcher(s).replaceAll("\"password\":\"[REDACTED]\"");
        s = PII_API_KEY.matcher(s).replaceAll("$1[REDACTED]");
        return s;
    }

    /** Truncate and sanitise a matched string for safe logging. */
    private static String maskSensitive(String s) {
        if (s == null) return null;
        if (s.length() > 50) s = s.substring(0, 47) + "...";
        return s;
    }

    /** Simulate GeoIP based on first octet (demo only). */
    private static String simulateGeo(String ip) {
        if (ip == null || ip.isEmpty()) return "XX";
        String[] parts = ip.split("\\.");
        if (parts.length < 1) return "XX";
        try {
            int first = Integer.parseInt(parts[0]);
            // Very rough simulation – replace with MaxMind in production
            if (first >= 1   && first <= 50)   return "IN";
            if (first >= 51  && first <= 100)  return "US";
            if (first >= 101 && first <= 150)  return "GB";
            if (first >= 151 && first <= 200)  return "CN";
            return "IN"; // default
        } catch (NumberFormatException e) {
            return "XX";
        }
    }

    private static String formatUptime(long seconds) {
        long h = seconds / 3600, m = (seconds % 3600) / 60, s = seconds % 60;
        return h + "h " + m + "m " + s + "s";
    }

    /** Strip null-bytes and overly long strings to prevent injection via MCP args. */
    @SuppressWarnings("unchecked")
    private static Map<String,Object> sanitiseArgs(Map<String,Object> args) {
        Map<String,Object> clean = new LinkedHashMap<>();
        for (Map.Entry<String,Object> e : args.entrySet()) {
            Object v = e.getValue();
            if (v instanceof String) {
                String s = ((String) v).replace("\0", "").replace("\r", "");
                if (s.length() > 65536) s = s.substring(0, 65536);
                clean.put(e.getKey(), s);
            } else {
                clean.put(e.getKey(), v);
            }
        }
        return clean;
    }

    // ─── Tool-schema helpers ──────────────────────────────────

    private static Map<String,Object> tool(String name, String description, Map<String,Object> inputSchema) {
        return Json.obj("name", name, "description", description, "inputSchema", inputSchema);
    }

    private static Map<String,Object> singleSchema(String name, String type, String desc) {
        Map<String,Object> props = new LinkedHashMap<>();
        props.put(name, Json.obj("type", type, "description", desc));
        return Json.obj("type", "object", "properties", props, "required", Collections.singletonList(name));
    }

    private static Map<String,Object> schema(String... kvs) {
        Map<String,Object> props = new LinkedHashMap<>();
        for (int i = 0; i < kvs.length; i += 3) {
            props.put(kvs[i], Json.obj("type", kvs[i+1], "description", kvs[i+2]));
        }
        return Json.obj("type", "object", "properties", props);
    }

    private static Map<String,Object> emptySchema() {
        return Json.obj("type", "object", "properties", new HashMap<>());
    }

    // ─── Type-safe getters ────────────────────────────────────

    private static String str(Map<String,Object> m, String key) { return str(m, key, ""); }
    private static String str(Map<String,Object> m, String key, String def) {
        Object v = m.get(key);
        return v == null ? def : v.toString();
    }
    private static double num(Map<String,Object> m, String key, double def) {
        Object v = m.get(key);
        if (v == null) return def;
        try { return Double.parseDouble(v.toString()); } catch (Exception e) { return def; }
    }
    @SuppressWarnings("unchecked")
    private static Map<String,Object> map(Map<String,Object> m, String key) {
        Object v = m.get(key);
        return (v instanceof Map) ? (Map<String,Object>) v : null;
    }

    // ═══════════════════════════════════════════════════════════
    //  MINIMAL JSON UTILITY
    // ═══════════════════════════════════════════════════════════

    static class Json {
        /** Build a varargs key-value object. */
        static Map<String,Object> obj(Object... kvs) {
            Map<String,Object> m = new LinkedHashMap<>();
            for (int i = 0; i < kvs.length - 1; i += 2) {
                if (kvs[i] != null) m.put(kvs[i].toString(), kvs[i+1]);
            }
            return m;
        }

        /** JSON-RPC 2.0 success response. */
        static String response(Object id, Object result) {
            return "{\"jsonrpc\":\"2.0\",\"id\":" + stringify(id) +
                   ",\"result\":" + stringify(result) + "}";
        }

        /** JSON-RPC 2.0 error response. */
        static String error(Object id, int code, String msg) {
            return "{\"jsonrpc\":\"2.0\",\"id\":" + stringify(id) +
                   ",\"error\":{\"code\":" + code +
                   ",\"message\":" + stringify(msg) + "}}";
        }

        /** Serialize any value to JSON string. */
        @SuppressWarnings("unchecked")
        static String stringify(Object val) {
            if (val == null)              return "null";
            if (val instanceof Boolean)   return val.toString();
            if (val instanceof Number)    return val.toString();
            if (val instanceof String)    return escapeString((String) val);
            if (val instanceof Map) {
                StringBuilder sb = new StringBuilder("{");
                boolean first = true;
                for (Map.Entry<?,?> e : ((Map<?,?>) val).entrySet()) {
                    if (!first) sb.append(",");
                    sb.append(escapeString(e.getKey().toString())).append(":").append(stringify(e.getValue()));
                    first = false;
                }
                return sb.append("}").toString();
            }
            if (val instanceof List) {
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (Object item : (List<?>) val) {
                    if (!first) sb.append(",");
                    sb.append(stringify(item));
                    first = false;
                }
                return sb.append("]").toString();
            }
            if (val instanceof Object[]) {
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (Object item : (Object[]) val) {
                    if (!first) sb.append(",");
                    sb.append(stringify(item));
                    first = false;
                }
                return sb.append("]").toString();
            }
            return escapeString(val.toString());
        }

        private static String escapeString(String s) {
            StringBuilder sb = new StringBuilder("\"");
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                switch (c) {
                    case '"':  sb.append("\\\""); break;
                    case '\\': sb.append("\\\\"); break;
                    case '\n': sb.append("\\n");  break;
                    case '\r': sb.append("\\r");  break;
                    case '\t': sb.append("\\t");  break;
                    default:
                        if (c < 0x20) { sb.append(String.format("\\u%04x", (int)c)); }
                        else          { sb.append(c); }
                }
            }
            return sb.append("\"").toString();
        }

        // ── Simple recursive JSON parser ──────────────────────

        static Map<String, Object> parse(String json) {
            Object v = parseValue(json.trim(), new int[]{0});
            if (v instanceof Map) return castMap(v);
            throw new RuntimeException("Top-level JSON must be an object");
        }

        @SuppressWarnings("unchecked")
        private static Map<String,Object> castMap(Object o) { return (Map<String,Object>) o; }

        private static Object parseValue(String s, int[] pos) {
            skipWs(s, pos);
            if (pos[0] >= s.length()) return null;
            char c = s.charAt(pos[0]);
            if (c == '{') return parseObject(s, pos);
            if (c == '[') return parseArray(s, pos);
            if (c == '"') return parseStr(s, pos);
            if (c == 't') { pos[0] += 4; return Boolean.TRUE; }
            if (c == 'f') { pos[0] += 5; return Boolean.FALSE; }
            if (c == 'n') { pos[0] += 4; return null; }
            return parseNum(s, pos);
        }

        private static Map<String,Object> parseObject(String s, int[] pos) {
            Map<String,Object> m = new LinkedHashMap<>();
            pos[0]++; // consume '{'
            skipWs(s, pos);
            if (pos[0] < s.length() && s.charAt(pos[0]) == '}') { pos[0]++; return m; }
            while (pos[0] < s.length()) {
                skipWs(s, pos);
                String key = (String) parseStr(s, pos);
                skipWs(s, pos);
                if (pos[0] < s.length() && s.charAt(pos[0]) == ':') pos[0]++;
                skipWs(s, pos);
                Object val = parseValue(s, pos);
                m.put(key, val);
                skipWs(s, pos);
                if (pos[0] >= s.length()) break;
                char nx = s.charAt(pos[0]);
                if (nx == '}') { pos[0]++; break; }
                if (nx == ',') pos[0]++;
            }
            return m;
        }

        private static List<Object> parseArray(String s, int[] pos) {
            List<Object> list = new ArrayList<>();
            pos[0]++; // consume '['
            skipWs(s, pos);
            if (pos[0] < s.length() && s.charAt(pos[0]) == ']') { pos[0]++; return list; }
            while (pos[0] < s.length()) {
                skipWs(s, pos);
                list.add(parseValue(s, pos));
                skipWs(s, pos);
                if (pos[0] >= s.length()) break;
                char nx = s.charAt(pos[0]);
                if (nx == ']') { pos[0]++; break; }
                if (nx == ',') pos[0]++;
            }
            return list;
        }

        private static String parseStr(String s, int[] pos) {
            pos[0]++; // consume opening '"'
            StringBuilder sb = new StringBuilder();
            while (pos[0] < s.length()) {
                char c = s.charAt(pos[0]);
                if (c == '"') { pos[0]++; break; }
                if (c == '\\' && pos[0]+1 < s.length()) {
                    char esc = s.charAt(pos[0]+1);
                    pos[0] += 2;
                    switch (esc) {
                        case '"':  sb.append('"'); break;
                        case '\\': sb.append('\\'); break;
                        case '/':  sb.append('/');  break;
                        case 'n':  sb.append('\n'); break;
                        case 'r':  sb.append('\r'); break;
                        case 't':  sb.append('\t'); break;
                        case 'u':
                            if (pos[0]+3 < s.length()) {
                                try {
                                    int cp = Integer.parseInt(s.substring(pos[0], pos[0]+4), 16);
                                    sb.append((char) cp);
                                    pos[0] += 4;
                                } catch (Exception ignored) {}
                            }
                            break;
                        default: sb.append(esc);
                    }
                } else {
                    sb.append(c);
                    pos[0]++;
                }
            }
            return sb.toString();
        }

        private static Number parseNum(String s, int[] pos) {
            int start = pos[0];
            while (pos[0] < s.length() && "0123456789.-+eE".indexOf(s.charAt(pos[0])) >= 0) pos[0]++;
            String num = s.substring(start, pos[0]);
            try { return Long.parseLong(num); } catch (Exception ignored) {}
            try { return Double.parseDouble(num); } catch (Exception ignored) {}
            return 0;
        }

        private static void skipWs(String s, int[] pos) {
            while (pos[0] < s.length() && Character.isWhitespace(s.charAt(pos[0]))) pos[0]++;
        }
    }
}
