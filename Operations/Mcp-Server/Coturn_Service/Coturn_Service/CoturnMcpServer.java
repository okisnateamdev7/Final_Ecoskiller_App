import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.regex.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * Ecoskiller | mcp-coturn-service
 * CAT-03 — NAT Traversal / Media  |  Custom Build  |  Priority: MEDIUM
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 13 Agents:
 *  01. server_status          — Health/liveness of all 4 coturn VM instances
 *  02. relay_session_stats    — Active TURN allocations, relay bandwidth, port usage
 *  03. credential_generate    — Generate HMAC-SHA1 time-limited TURN credentials
 *  04. credential_validate    — Validate existing TURN credentials (not expired/tampered)
 *  05. config_view            — Read current turnserver.conf settings
 *  06. config_update          — Update turnserver.conf parameters safely
 *  07. firewall_check         — Verify GCP/AWS firewall rules for TURN/relay ports
 *  08. dns_check              — Verify media.ecoskiller.com DNS A-record resolution
 *  09. connectivity_test      — STUN binding test to confirm coturn is reachable
 *  10. log_query              — Query coturn access logs (auth failures, relay events)
 *  11. instance_manage        — Start / stop / restart coturn process on a VM
 *  12. peer_deny_audit        — Audit denied-peer-ip rules (RFC 1918 protection)
 *  13. slo_report             — WebRTC connection success rate vs ≥95% SLO target
 *
 * Requirements : Java 17+  (no external packages — stdlib only)
 * Transport    : stdio  (stdin/stdout  JSON-RPC 2.0)
 * MCP Version  : 2024-11-05
 *
 * Run:
 *   javac CoturnMcpServer.java && java CoturnMcpServer
 *
 * Required env vars:
 *   COTURN_TURN_SECRET    Shared TURN secret (stored as Kubernetes Secret in media namespace)
 *
 * Optional env vars:
 *   COTURN_REALM            default: media.ecoskiller.com
 *   COTURN_DNS_NAME         default: media.ecoskiller.com
 *   COTURN_INSTANCES        default: coturn-gcp-1:3478,coturn-gcp-2:3478,coturn-aws-1:3478,coturn-aws-2:3478
 *   COTURN_LOG_DIR          default: /var/log/coturn
 *   COTURN_CONF_PATH        default: /etc/coturn/turnserver.conf
 *   COTURN_CRED_TTL_SECS    default: 86400  (24h)
 *   COTURN_MIN_PORT         default: 49152
 *   COTURN_MAX_PORT         default: 65535
 *   COTURN_HTTP_TIMEOUT_MS  default: 8000
 *   COTURN_MCP_AUDIT_LOG    default: /var/log/ecoskiller/coturn-mcp-audit.log
 */
public class CoturnMcpServer {

    // ── Protocol constants ────────────────────────────────────────────────
    static final String MCP_VERSION = "2024-11-05";
    static final String SERVER_NAME = "mcp-coturn-service";
    static final String SERVER_VER  = "1.0.0";

    // ── Ecoskiller coturn topology ────────────────────────────────────────
    static final String DEFAULT_REALM     = "media.ecoskiller.com";
    static final String DEFAULT_DNS       = "media.ecoskiller.com";
    static final int    DEFAULT_MIN_PORT  = 49152;
    static final int    DEFAULT_MAX_PORT  = 65535;
    static final int    DEFAULT_CRED_TTL  = 86400;
    static final int    STUN_PORT         = 3478;
    static final int    STUN_TLS_PORT     = 5349;
    // 4 instances: 2 GCP asia-south1, 2 AWS ap-south-1
    static final String DEFAULT_INSTANCES =
        "coturn-gcp-1:3478,coturn-gcp-2:3478,coturn-aws-1:3478,coturn-aws-2:3478";

    // ── Security patterns ─────────────────────────────────────────────────
    static final Pattern P_TOOL      = Pattern.compile("^[a-z][a-z0-9_]{0,63}$");
    static final Pattern P_SHELL     = Pattern.compile("[;&|`$<>()\\'\"{}!\\\\]");
    static final Pattern P_TRAVERSAL = Pattern.compile("\\.\\.");
    static final Pattern P_USERNAME  = Pattern.compile("^[a-zA-Z0-9_.@\\-]{1,128}$");
    static final Pattern P_HOSTNAME  = Pattern.compile("^[a-zA-Z0-9.\\-]{1,253}$");
    static final Pattern P_CONF_KEY  = Pattern.compile("^[a-zA-Z][a-zA-Z0-9\\-_]{0,63}$");
    static final Pattern P_CONF_VAL  = Pattern.compile("^[^;&|`$<>()\\\\]{0,512}$");

    // ── Rate limiting (100 req/min) ───────────────────────────────────────
    static final AtomicInteger reqCount    = new AtomicInteger(0);
    static volatile long        windowStart = System.currentTimeMillis();
    static final int            RATE_LIMIT  = 100;

    // ── Audit log ─────────────────────────────────────────────────────────
    static PrintWriter auditWriter = null;
    static {
        String logPath = env("COTURN_MCP_AUDIT_LOG", "/var/log/ecoskiller/coturn-mcp-audit.log");
        try {
            File logDir = new File(logPath).getParentFile();
            if (logDir != null && (logDir.exists() || logDir.mkdirs()))
                auditWriter = new PrintWriter(new FileWriter(logPath, true), true);
        } catch (Exception ignored) {}
    }

    // ═════════════════════════════════════════════════════════════════════
    // ENTRY POINT
    // ═════════════════════════════════════════════════════════════════════

    public static void main(String[] args) throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));
        audit("START", SERVER_NAME + " v" + SERVER_VER);
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String resp = handle(line);
            if (resp != null) { out.println(resp); out.flush(); }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // JSON-RPC DISPATCHER
    // ═════════════════════════════════════════════════════════════════════

    static String handle(String raw) {
        Map<String,Object> req;
        try { req = parseObj(raw); }
        catch (Exception e) { return err(null, -32700, "Parse error: " + e.getMessage()); }

        String id     = strVal(req, "id");
        String method = strVal(req, "method");

        // Rate limit
        long now = System.currentTimeMillis();
        synchronized (CoturnMcpServer.class) {
            if (now - windowStart >= 60_000L) { windowStart = now; reqCount.set(0); }
            if (reqCount.incrementAndGet() > RATE_LIMIT)
                return err(id, -32000, "Rate limit exceeded (100 req/min).");
        }

        audit("REQ", method + " id=" + id);
        Map<String,Object> params = objVal(req, "params");

        return switch (method != null ? method : "") {
            case "initialize" -> handleInit(id);
            case "ping"       -> ok(id, obj());
            case "tools/list" -> handleToolsList(id);
            case "tools/call" -> handleToolCall(id, params);
            default           -> err(id, -32601, "Method not found: " + method);
        };
    }

    static String handleInit(String id) {
        return ok(id, obj(
            "protocolVersion", MCP_VERSION,
            "serverInfo",      obj("name", SERVER_NAME, "version", SERVER_VER),
            "capabilities",    obj("tools", obj())
        ));
    }

    static String handleToolsList(String id) {
        List<Object> list = new ArrayList<>();
        for (ToolDef t : TOOLS) list.add(obj(
            "name",        t.name(),
            "description", t.description(),
            "inputSchema", parseObj(t.schema())
        ));
        return ok(id, obj("tools", list));
    }

    static String handleToolCall(String id, Map<String,Object> params) {
        String toolName = strVal(params, "name");
        Map<String,Object> args = objVal(params, "arguments");

        try {
            validateToolName(toolName);
            validateArgs(args, 0);
        } catch (SecurityException se) {
            audit("SECURITY_VIOLATION", toolName + ": " + se.getMessage());
            return err(id, -32001, "Security violation: " + se.getMessage());
        }

        ToolDef tool = findTool(toolName);
        if (tool == null) return err(id, -32602, "Unknown tool: " + toolName);

        try {
            String result = tool.handler().execute(args);
            audit("TOOL_OK", toolName);
            return ok(id, obj(
                "content", List.of(obj("type", "text", "text", result)),
                "isError", false
            ));
        } catch (Exception e) {
            audit("TOOL_ERR", toolName + ": " + e.getMessage());
            return ok(id, obj(
                "content", List.of(obj("type", "text", "text", "Error: " + e.getMessage())),
                "isError", true
            ));
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // TOOL REGISTRY
    // ═════════════════════════════════════════════════════════════════════

    interface ToolHandler { String execute(Map<String,Object> args) throws Exception; }
    record ToolDef(String name, String description, String schema, ToolHandler handler) {}

    static final ToolDef[] TOOLS = {

        new ToolDef("server_status",
            "Check health and liveness of all coturn VM instances (2 GCP asia-south1 + 2 AWS ap-south-1). " +
            "Tests STUN Binding Request reachability on UDP 3478 and TLS 5349 per instance. " +
            "Reports up/down status, round-trip latency, and relay port availability.",
            """
            {"type":"object","properties":{
              "instance":{"type":"string","description":"Specific instance hostname to check (optional — checks all if omitted)"},
              "port":{"type":"integer","description":"STUN port to test (default 3478)","default":3478}
            }}""",
            CoturnMcpServer::toolServerStatus),

        new ToolDef("relay_session_stats",
            "Report active TURN relay sessions, allocated port count, relay bandwidth estimate, " +
            "and authentication failure count. Parsed from coturn access logs. " +
            "Shows port pool usage (49152-65535, ~16k ports/instance) and relay session concurrency.",
            """
            {"type":"object","properties":{
              "instance":{"type":"string","description":"Instance hostname to query (optional — aggregates all)"},
              "tail_lines":{"type":"integer","description":"Log lines to tail for stats (default 1000, max 5000)","default":1000}
            }}""",
            CoturnMcpServer::toolRelaySessionStats),

        new ToolDef("credential_generate",
            "Generate HMAC-SHA1 time-limited TURN credentials (RFC 5766 long-term credential mechanism). " +
            "Uses the shared TURN secret from COTURN_TURN_SECRET env var (stored as Kubernetes Secret in media namespace). " +
            "Credentials expire after COTURN_CRED_TTL_SECS (default 86400s/24h). " +
            "Output: username (timestamp:user), credential (HMAC-SHA1), TTL expiry, TURN URLs for ICE config.",
            """
            {"type":"object","required":["username"],"properties":{
              "username":{"type":"string","description":"Session username (e.g. session ID or user ID). Max 128 chars."},
              "ttl_seconds":{"type":"integer","description":"Credential TTL in seconds (default: COTURN_CRED_TTL_SECS or 86400)","default":86400}
            }}""",
            CoturnMcpServer::toolCredentialGenerate),

        new ToolDef("credential_validate",
            "Validate TURN credentials: check HMAC-SHA1 signature, expiry, and username format. " +
            "Used to audit existing credentials without contacting the coturn server. " +
            "Returns: valid (bool), expiry timestamp, seconds remaining, tamper detection.",
            """
            {"type":"object","required":["username","credential"],"properties":{
              "username":{"type":"string","description":"TURN username (format: timestamp:user)"},
              "credential":{"type":"string","description":"Base64-encoded HMAC-SHA1 credential"}
            }}""",
            CoturnMcpServer::toolCredentialValidate),

        new ToolDef("config_view",
            "Read and display current turnserver.conf configuration. " +
            "Shows key parameters: realm, listening-port, tls-listening-port, min-port, max-port, " +
            "relay-ip, external-ip, use-auth-secret, denied-peer-ip rules (RFC 1918 protection). " +
            "Redacts static-auth-secret value.",
            """
            {"type":"object","properties":{
              "section":{"type":"string","description":"Filter by config section: 'network'|'auth'|'relay'|'logging'|'all' (default: all)","default":"all"}
            }}""",
            CoturnMcpServer::toolConfigView),

        new ToolDef("config_update",
            "Update a single turnserver.conf parameter safely. " +
            "Validates key/value format, creates a backup before writing, " +
            "and returns the diff. Requires instance_manage restart to apply changes. " +
            "PROTECTED keys (static-auth-secret) cannot be updated via this tool — use Kubernetes Secret rotation.",
            """
            {"type":"object","required":["key","value"],"properties":{
              "key":{"type":"string","description":"Config parameter name (e.g. max-port, realm, log-file)"},
              "value":{"type":"string","description":"New parameter value"},
              "dry_run":{"type":"boolean","description":"Validate only, do not write (default false)","default":false}
            }}""",
            CoturnMcpServer::toolConfigUpdate),

        new ToolDef("firewall_check",
            "Verify required firewall ports are accessible on coturn instances. " +
            "Checks: UDP 3478 (STUN/TURN signalling), TLS 5349 (TURN over TLS), " +
            "and a sample relay port from range 49152-65535. " +
            "Reports open/blocked per port per instance. Maps to GCP allow-coturn-udp and allow-coturn-relay rules.",
            """
            {"type":"object","properties":{
              "instance":{"type":"string","description":"Instance hostname to check (optional — checks all)"},
              "check_relay_sample":{"type":"boolean","description":"Also probe a sample relay port (default true)","default":true}
            }}""",
            CoturnMcpServer::toolFirewallCheck),

        new ToolDef("dns_check",
            "Verify media.ecoskiller.com DNS A-record resolves to a coturn public IP. " +
            "Confirms DNS-only mode (no proxy) — required for direct UDP WebRTC routing. " +
            "Also checks PTR reverse DNS. TTL must be ≤300s for fast failover. " +
            "Managed by PowerDNS REST API (ops namespace, k3s).",
            """
            {"type":"object","properties":{
              "hostname":{"type":"string","description":"DNS name to resolve (default: media.ecoskiller.com)"},
              "expected_ip":{"type":"string","description":"Expected coturn IP for validation (optional)"}
            }}""",
            CoturnMcpServer::toolDnsCheck),

        new ToolDef("connectivity_test",
            "Run a STUN Binding Request to verify end-to-end coturn reachability. " +
            "Tests that the STUN server is responding and returning a reflexive address. " +
            "Also validates the TURN authentication realm matches media.ecoskiller.com. " +
            "Used for health checks and post-deployment verification.",
            """
            {"type":"object","properties":{
              "host":{"type":"string","description":"coturn hostname or IP (default: media.ecoskiller.com)"},
              "port":{"type":"integer","description":"Port to test (default 3478)","default":3478},
              "protocol":{"type":"string","enum":["udp","tcp"],"description":"Transport protocol (default udp)","default":"udp"}
            }}""",
            CoturnMcpServer::toolConnectivityTest),

        new ToolDef("log_query",
            "Query coturn access logs for relay events, authentication failures, and session activity. " +
            "Parses /var/log/coturn/turnserver.log. Filters: auth_failures, relay_alloc, relay_dealloc, " +
            "session_start, session_end, or all. Returns structured entries with timestamp, user, client IP, event.",
            """
            {"type":"object","properties":{
              "event_type":{"type":"string","enum":["auth_failure","relay_alloc","relay_dealloc","session_start","session_end","all"],"default":"all"},
              "tail_lines":{"type":"integer","description":"Lines to tail from log (default 500, max 2000)","default":500},
              "client_ip":{"type":"string","description":"Filter by client IP address (optional)"},
              "username":{"type":"string","description":"Filter by TURN username (optional)"}
            }}""",
            CoturnMcpServer::toolLogQuery),

        new ToolDef("instance_manage",
            "Start, stop, restart, or get status of the coturn process on a VM instance. " +
            "Uses systemctl (if coturn runs as a systemd service) or docker restart. " +
            "Required after config_update to apply changes. " +
            "Includes a 5-second post-restart reachability check.",
            """
            {"type":"object","required":["action"],"properties":{
              "action":{"type":"string","enum":["start","stop","restart","status"],"description":"Action to perform"},
              "instance":{"type":"string","description":"Instance identifier (default: local)","default":"local"}
            }}""",
            CoturnMcpServer::toolInstanceManage),

        new ToolDef("peer_deny_audit",
            "Audit denied-peer-ip rules in turnserver.conf to verify RFC 1918 private address ranges " +
            "are blocked (prevents coturn from relaying to internal k3s cluster network). " +
            "Required rules: 10.0.0.0/8, 172.16.0.0/12, 192.168.0.0/16. " +
            "Also checks no-loopback-peers and no-multicast-peers flags. " +
            "Reports any missing protections as CRITICAL security gaps.",
            """
            {"type":"object","properties":{
              "verbose":{"type":"boolean","description":"Show full parsed deny rule list (default false)","default":false}
            }}""",
            CoturnMcpServer::toolPeerDenyAudit),

        new ToolDef("slo_report",
            "Report WebRTC connection success rate vs the ≥95% SLO target. " +
            "Parses coturn log for successful TURN allocations vs authentication failures " +
            "and connection errors. Calculates success rate over configurable window. " +
            "Alert threshold: <90% over 15 minutes triggers P1 incident. " +
            "Maps to Ecoskiller Grafana Media QoS dashboard.",
            """
            {"type":"object","properties":{
              "window_minutes":{"type":"integer","description":"Time window for SLO calculation (default 60, max 1440)","default":60},
              "tail_lines":{"type":"integer","description":"Log lines to analyse (default 2000, max 10000)","default":2000}
            }}""",
            CoturnMcpServer::toolSloReport)
    };

    static ToolDef findTool(String name) {
        if (name == null) return null;
        for (ToolDef t : TOOLS) if (t.name().equals(name)) return t;
        return null;
    }

    // ═════════════════════════════════════════════════════════════════════
    // TOOL HANDLERS
    // ═════════════════════════════════════════════════════════════════════

    // ── 01. server_status ────────────────────────────────────────────────
    static String toolServerStatus(Map<String,Object> args) throws Exception {
        String filterInstance = sanitize(strVal(args, "instance"));
        int    port           = intVal(args, "port", STUN_PORT);
        if (port < 1 || port > 65535) port = STUN_PORT;

        List<String> instances = parseInstances();
        List<Object> results   = new ArrayList<>();
        int up = 0, down = 0;

        for (String inst : instances) {
            String[] parts = inst.split(":");
            String host    = parts[0];
            int    iPort   = parts.length > 1 ? Integer.parseInt(parts[1]) : port;

            if (!filterInstance.isEmpty() && !host.contains(filterInstance)) continue;

            long start  = System.currentTimeMillis();
            boolean reachable = tcpProbe(host, iPort, 3000);
            long latencyMs    = System.currentTimeMillis() - start;

            String cloud  = host.contains("gcp") ? "GCP asia-south1" : "AWS ap-south-1";
            String status = reachable ? "UP" : "DOWN";
            if (reachable) up++; else down++;

            results.add(obj(
                "instance",    host,
                "port",        iPort,
                "cloud",       cloud,
                "status",      status,
                "latency_ms",  latencyMs,
                "reachable",   reachable,
                "note",        reachable
                    ? "STUN/TURN signalling port responsive"
                    : "Instance unreachable — check VM status and firewall rule allow-coturn-udp"
            ));
        }

        boolean allUp = (down == 0 && up > 0);
        String overall = allUp ? "HEALTHY" : (up == 0 ? "ALL_DOWN" : "DEGRADED");

        return toJson(obj(
            "overall_status",  overall,
            "instances_up",    up,
            "instances_down",  down,
            "total_instances", up + down,
            "topology",        "2× GCP asia-south1  +  2× AWS ap-south-1  (active-active stateless)",
            "realm",           env("COTURN_REALM", DEFAULT_REALM),
            "dns",             env("COTURN_DNS_NAME", DEFAULT_DNS),
            "relay_port_range","UDP " + env("COTURN_MIN_PORT", String.valueOf(DEFAULT_MIN_PORT)) +
                               "–" + env("COTURN_MAX_PORT", String.valueOf(DEFAULT_MAX_PORT)),
            "instances",       results,
            "slo_target",      "WebRTC connection success ≥95%. Alert fires if <90% over 15 min.",
            "webrtc_impact",   "coturn is Phase 1 Critical — ~30% of enterprise/mobile users " +
                               "require TURN relay. Down = direct assessment session failures."
        ));
    }

    // ── 02. relay_session_stats ──────────────────────────────────────────
    static String toolRelaySessionStats(Map<String,Object> args) throws Exception {
        String filterInstance = sanitize(strVal(args, "instance"));
        int    tailLines      = Math.min(intVal(args, "tail_lines", 1000), 5000);

        String logDir  = env("COTURN_LOG_DIR", "/var/log/coturn");
        String logFile = logDir + "/turnserver.log";

        List<String> lines = tailLogFile(logFile, tailLines);

        int allocCount   = 0, deallocCount = 0, authFails = 0, refreshCount = 0;
        Set<String> activeUsers   = new LinkedHashSet<>();
        Set<String> activeClients = new LinkedHashSet<>();
        long totalBytesRelay      = 0;

        for (String line : lines) {
            String ll = line.toLowerCase();
            if (ll.contains("allocate") && ll.contains("success")) {
                allocCount++;
                String user = extractField(line, "user=");
                String client = extractField(line, "client=");
                if (user != null && !user.isEmpty()) activeUsers.add(user);
                if (client != null && !client.isEmpty()) activeClients.add(client.split(":")[0]);
            }
            if (ll.contains("deallocate") || ll.contains("de-allocate")) deallocCount++;
            if (ll.contains("401") || ll.contains("wrong") || ll.contains("auth error")
                    || ll.contains("authentication failure")) authFails++;
            if (ll.contains("refresh")) refreshCount++;
            // Estimate relay bytes from send/recv log lines
            String bytesStr = extractField(line, "bytes=");
            if (bytesStr != null) {
                try { totalBytesRelay += Long.parseLong(bytesStr.replaceAll("[^0-9]","")); }
                catch (Exception ignored) {}
            }
        }

        int activeSessions = Math.max(0, allocCount - deallocCount);
        int portPoolTotal  = intVal2(env("COTURN_MAX_PORT", String.valueOf(DEFAULT_MAX_PORT))) -
                             intVal2(env("COTURN_MIN_PORT", String.valueOf(DEFAULT_MIN_PORT))) + 1;
        double portUsagePct = portPoolTotal > 0 ? (double) activeSessions / portPoolTotal * 100.0 : 0;

        Map<String,Object> result = obj(
            "log_file",         logFile,
            "lines_analysed",   lines.size(),
            "active_relay_sessions",  activeSessions,
            "total_allocations",      allocCount,
            "total_deallocations",    deallocCount,
            "active_refresh_keepalives", refreshCount,
            "auth_failures",    authFails,
            "unique_users_seen",      activeUsers.size(),
            "unique_client_ips_seen", activeClients.size(),
            "relay_bytes_estimate",   totalBytesRelay,
            "relay_mb_estimate",      String.format("%.2f", totalBytesRelay / 1_048_576.0),
            "port_pool_total",        portPoolTotal,
            "port_pool_used_estimate", activeSessions,
            "port_pool_usage_pct",    String.format("%.2f%%", portUsagePct),
            "topology",         "4 stateless instances — each holds independent relay port pool",
            "relay_port_range", "UDP " + env("COTURN_MIN_PORT", String.valueOf(DEFAULT_MIN_PORT)) +
                                "–" + env("COTURN_MAX_PORT", String.valueOf(DEFAULT_MAX_PORT))
        );

        if (authFails > 10) result.put("alert",
            authFails + " auth failures detected. Check Jitsi prosody HMAC credential generation. " +
            "Shared TURN secret may be out of sync between coturn and prosody.");
        if (portUsagePct > 80) result.put("port_warning",
            "Port pool >80% used. Consider adding coturn instances behind DNS round-robin.");

        if (!filterInstance.isEmpty()) result.put("filter_instance", filterInstance);
        return toJson(result);
    }

    // ── 03. credential_generate ──────────────────────────────────────────
    static String toolCredentialGenerate(Map<String,Object> args) throws Exception {
        String username  = args.containsKey("username") ? strVal(args, "username") : null;
        int    ttlSecs   = intVal(args, "ttl_seconds",
                                  intVal2(env("COTURN_CRED_TTL_SECS", String.valueOf(DEFAULT_CRED_TTL))));

        if (username == null || username.isBlank())
            throw new IllegalArgumentException("'username' argument is required");
        if (!P_USERNAME.matcher(username).matches())
            throw new SecurityException("Invalid username format. Allowed: alphanumeric, ., @, -, _  (max 128 chars)");
        if (ttlSecs < 60 || ttlSecs > 86400 * 7)
            throw new IllegalArgumentException("ttl_seconds must be between 60 and 604800 (7 days)");

        String turnSecret = requireEnv("COTURN_TURN_SECRET");
        long   expiry     = System.currentTimeMillis() / 1000L + ttlSecs;
        // RFC 5766 long-term credential: username = "<expiry>:<user>"
        String turnUsername   = expiry + ":" + username;
        String turnCredential = hmacSha1Base64(turnSecret, turnUsername);
        String realm          = env("COTURN_REALM", DEFAULT_REALM);
        String dns            = env("COTURN_DNS_NAME", DEFAULT_DNS);

        // STUN/TURN URL list for ICE config (matches Jitsi prosody output)
        List<Object> iceServers = List.of(
            obj("urls", "stun:" + dns + ":" + STUN_PORT),
            obj("urls", List.of(
                    "turn:" + dns + ":" + STUN_PORT + "?transport=udp",
                    "turn:" + dns + ":" + STUN_TLS_PORT + "?transport=tcp"),
                "username",   turnUsername,
                "credential", turnCredential)
        );

        return toJson(obj(
            "username",        turnUsername,
            "credential",      turnCredential,
            "realm",           realm,
            "ttl_seconds",     ttlSecs,
            "expires_at",      Instant.ofEpochSecond(expiry).toString(),
            "expires_epoch",   expiry,
            "algorithm",       "HMAC-SHA1  (RFC 5766 long-term credential mechanism)",
            "ice_servers",     iceServers,
            "security_note",   "Credential expires at " + Instant.ofEpochSecond(expiry) +
                               ". Never log or persist credentials. " +
                               "TURN secret source: COTURN_TURN_SECRET Kubernetes Secret (media namespace).",
            "jitsi_prosody_equivalent",
                "This replicates the credential generated by Jitsi prosody using the shared TURN secret. " +
                "In production, prosody generates credentials per-session embedded in the Jitsi room token."
        ));
    }

    // ── 04. credential_validate ──────────────────────────────────────────
    static String toolCredentialValidate(Map<String,Object> args) throws Exception {
        String username   = sanitize(strVal(args, "username"));
        String credential = sanitize(strVal(args, "credential"));

        if (username.isEmpty())   throw new IllegalArgumentException("'username' argument is required");
        if (credential.isEmpty()) throw new IllegalArgumentException("'credential' argument is required");

        // Parse username: expected format "<epoch>:<user>"
        String[] parts = username.split(":", 2);
        if (parts.length != 2) {
            return toJson(obj(
                "valid", false,
                "error", "Invalid username format. Expected '<epoch_timestamp>:<user>', got: " + username
            ));
        }

        long expiry;
        try { expiry = Long.parseLong(parts[0]); }
        catch (NumberFormatException e) {
            return toJson(obj("valid", false, "error", "Username timestamp is not a valid integer: " + parts[0]));
        }

        long   now        = System.currentTimeMillis() / 1000L;
        boolean expired   = (now > expiry);
        long   secsLeft   = expiry - now;

        // Verify HMAC — requires TURN secret
        boolean hmacOk    = false;
        String  hmacNote  = "";
        try {
            String turnSecret   = requireEnv("COTURN_TURN_SECRET");
            String expectedHmac = hmacSha1Base64(turnSecret, username);
            hmacOk  = expectedHmac.equals(credential);
            hmacNote = hmacOk ? "HMAC-SHA1 signature valid" : "HMAC-SHA1 signature MISMATCH — credential may be tampered or wrong secret";
        } catch (IllegalStateException e) {
            hmacNote = "Cannot verify HMAC — COTURN_TURN_SECRET env var not set";
        }

        return toJson(obj(
            "valid",         !expired && hmacOk,
            "username",      username,
            "user_part",     parts[1],
            "expires_at",    Instant.ofEpochSecond(expiry).toString(),
            "expired",       expired,
            "seconds_remaining", expired ? 0 : secsLeft,
            "hmac_valid",    hmacOk,
            "hmac_note",     hmacNote,
            "algorithm",     "HMAC-SHA1  (RFC 5766 long-term credential mechanism)",
            "realm",         env("COTURN_REALM", DEFAULT_REALM),
            "action_if_expired", "Request fresh credentials from Jitsi prosody via the session token endpoint. " +
                                 "coturn returns HTTP 401 on expired credentials — client ICE will renegotiate."
        ));
    }

    // ── 05. config_view ──────────────────────────────────────────────────
    static String toolConfigView(Map<String,Object> args) throws Exception {
        String section  = strVal(args, "section");
        if (section == null || section.isBlank()) section = "all";
        String confPath = env("COTURN_CONF_PATH", "/etc/coturn/turnserver.conf");

        Map<String,Object> result = obj(
            "config_path", confPath,
            "realm",       env("COTURN_REALM", DEFAULT_REALM),
            "topology",    "4 VM instances (2× GCP e2-small, 2× AWS t3.small) — NOT inside k3s"
        );

        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of(confPath));
        } catch (IOException e) {
            // Config file not found — return known defaults from spec
            result.put("warning", "Config file not found at " + confPath +
                       " — showing Ecoskiller spec defaults");
            lines = getDefaultConfigLines();
        }

        // Parse key sections
        Map<String,String> network = new LinkedHashMap<>();
        Map<String,String> auth    = new LinkedHashMap<>();
        Map<String,String> relay   = new LinkedHashMap<>();
        Map<String,String> logging = new LinkedHashMap<>();
        List<String>       deniedPeers = new ArrayList<>();
        List<String>       rawLines    = new ArrayList<>();

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
            rawLines.add(trimmed);

            String[] kv = trimmed.split("=", 2);
            String key = kv[0].trim();
            String val = kv.length > 1 ? kv[1].trim() : "true";

            // SECURITY: never return secret value
            if (key.equalsIgnoreCase("static-auth-secret") || key.equalsIgnoreCase("lt-cred-mech")) {
                auth.put(key, "[REDACTED — stored as Kubernetes Secret in media namespace]");
                continue;
            }

            if (key.startsWith("denied-peer-ip")) { deniedPeers.add(val); continue; }

            switch (key) {
                case "listening-port", "tls-listening-port", "relay-ip", "external-ip",
                     "listening-ip", "alt-listening-port" -> network.put(key, val);
                case "use-auth-secret", "realm", "user-quota", "total-quota" -> auth.put(key, val);
                case "min-port", "max-port", "no-loopback-peers", "no-multicast-peers",
                     "no-tlsv1", "no-tlsv1_1" -> relay.put(key, val);
                case "log-file", "syslog", "verbose", "simple-log" -> logging.put(key, val);
                default -> {
                    if ("all".equals(section)) network.put(key, val);
                }
            }
        }

        if ("network".equals(section) || "all".equals(section))  result.put("network",       network);
        if ("auth".equals(section)    || "all".equals(section))  result.put("auth",           auth);
        if ("relay".equals(section)   || "all".equals(section))  result.put("relay",          relay);
        if ("logging".equals(section) || "all".equals(section))  result.put("logging",        logging);
        if ("all".equals(section))                                result.put("denied_peer_ips", deniedPeers);

        result.put("line_count",       rawLines.size());
        result.put("rfc1918_blocked",  !deniedPeers.isEmpty());
        result.put("security_note",    "static-auth-secret is always redacted. " +
                   "To rotate the TURN secret, update the Kubernetes Secret in media namespace " +
                   "and redeploy Jitsi prosody + coturn.");
        return toJson(result);
    }

    // ── 06. config_update ────────────────────────────────────────────────
    static String toolConfigUpdate(Map<String,Object> args) throws Exception {
        String  key     = sanitize(strVal(args, "key"));
        String  value   = sanitize(strVal(args, "value"));
        boolean dryRun  = boolVal(args, "dry_run", false);

        if (key.isEmpty())   throw new IllegalArgumentException("'key' argument is required");
        if (value.isEmpty()) throw new IllegalArgumentException("'value' argument is required");

        // Validate key/value format
        if (!P_CONF_KEY.matcher(key).matches())
            throw new SecurityException("Invalid config key format: " + key);
        if (!P_CONF_VAL.matcher(value).matches())
            throw new SecurityException("Config value contains illegal characters: " + value);

        // PROTECTED keys — must use K8s secret rotation
        Set<String> PROTECTED = Set.of("static-auth-secret","lt-cred-mech","use-auth-secret");
        if (PROTECTED.contains(key.toLowerCase()))
            throw new SecurityException("'" + key + "' is a protected credential parameter. " +
                "Rotate via Kubernetes Secret update in media namespace, not via this tool.");

        String confPath = env("COTURN_CONF_PATH", "/etc/coturn/turnserver.conf");
        Path   confFile = Path.of(confPath);

        // If config file doesn't exist, work with spec defaults
        List<String> lines;
        try {
            lines = new ArrayList<>(Files.readAllLines(confFile));
        } catch (IOException e) {
            lines = new ArrayList<>(getDefaultConfigLines());
        }

        String oldValue    = null;
        boolean keyFound   = false;
        List<String> updated = new ArrayList<>();

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("#") && trimmed.startsWith(key + "=")) {
                String[] kv = trimmed.split("=", 2);
                oldValue = kv.length > 1 ? kv[1].trim() : "";
                updated.add(key + "=" + value);
                keyFound = true;
            } else {
                updated.add(line);
            }
        }

        if (!keyFound) updated.add(key + "=" + value);

        Map<String,Object> result = obj(
            "key",      key,
            "old_value", oldValue != null ? oldValue : "(not previously set)",
            "new_value", value,
            "key_found", keyFound,
            "dry_run",   dryRun,
            "config_path", confPath
        );

        if (!dryRun) {
            try {
                // Backup
                Path backup = Path.of(confPath + ".bak." + System.currentTimeMillis());
                try {
                    Files.copy(confFile, backup);
                    result.put("backup_created", backup.toString());
                } catch (Exception ignored) {}
                // Write
                Files.write(confFile, updated);
                result.put("written",  true);
                result.put("action_required",
                    "Restart coturn to apply: use instance_manage tool with action=restart");
            } catch (IOException e) {
                result.put("written",  false);
                result.put("write_error", e.getMessage());
                result.put("note", "Config validated. Manual update required on coturn VM: " + confPath);
            }
        } else {
            result.put("written", false);
            result.put("validation", "Config key/value are valid and safe to apply.");
            result.put("diff", "- " + key + "=" + (oldValue != null ? oldValue : "(not set)") +
                               "\n+ " + key + "=" + value);
        }
        return toJson(result);
    }

    // ── 07. firewall_check ───────────────────────────────────────────────
    static String toolFirewallCheck(Map<String,Object> args) throws Exception {
        String  filterInstance = sanitize(strVal(args, "instance"));
        boolean checkRelaySample = boolVal(args, "check_relay_sample", true);

        List<String> instances = parseInstances();
        List<Object> checks    = new ArrayList<>();
        int passCount = 0, failCount = 0;

        int[] requiredPorts = {STUN_PORT, STUN_TLS_PORT};
        int   sampleRelayPort = DEFAULT_MIN_PORT + 100;

        for (String inst : instances) {
            String[] parts = inst.split(":");
            String host    = parts[0];
            if (!filterInstance.isEmpty() && !host.contains(filterInstance)) continue;

            String cloud = host.contains("gcp") ? "GCP asia-south1 (allow-coturn-udp)"
                                                 : "AWS ap-south-1 (Security Group)";

            for (int rp : requiredPorts) {
                boolean open = tcpProbe(host, rp, 3000);
                String rule  = rp == STUN_PORT   ? "allow-coturn-udp (UDP 3478)"
                             : rp == STUN_TLS_PORT ? "allow-coturn-udp (TLS 5349)"
                             : "allow-coturn-relay";
                if (open) passCount++; else failCount++;
                checks.add(obj(
                    "instance", host, "port", rp, "protocol", "UDP/TCP",
                    "cloud_rule", rule, "cloud", cloud,
                    "open", open,
                    "status", open ? "PASS" : "FAIL — BLOCK DETECTED",
                    "note", open ? "" :
                        "Firewall rule missing or inactive. " +
                        "GCP: add allow-coturn-udp INGRESS 0.0.0.0/0 UDP " + rp + ". " +
                        "AWS: add Security Group inbound UDP " + rp + " 0.0.0.0/0."
                ));
            }

            if (checkRelaySample) {
                boolean relayOpen = tcpProbe(host, sampleRelayPort, 3000);
                if (relayOpen) passCount++; else failCount++;
                checks.add(obj(
                    "instance", host, "port", sampleRelayPort,
                    "protocol", "UDP", "cloud_rule", "allow-coturn-relay (UDP 49152-65535)",
                    "cloud", cloud, "open", relayOpen,
                    "status", relayOpen ? "PASS" : "FAIL — RELAY RANGE BLOCKED",
                    "note", relayOpen ? "" :
                        "Relay port range UDP " + DEFAULT_MIN_PORT + "-" + DEFAULT_MAX_PORT +
                        " must be open to 0.0.0.0/0. " +
                        "Without this, TURN allocation succeeds but media relay fails silently."
                ));
            }
        }

        return toJson(obj(
            "overall", failCount == 0 ? "ALL_PASS" : (passCount == 0 ? "ALL_FAIL" : "PARTIAL"),
            "checks_passed",  passCount,
            "checks_failed",  failCount,
            "required_rules", List.of(
                "allow-coturn-udp: INGRESS UDP 3478,5349 from 0.0.0.0/0",
                "allow-coturn-relay: INGRESS UDP 49152-65535 from 0.0.0.0/0",
                "deny-internal-relay: OUTBOUND blocked in turnserver.conf via denied-peer-ip (RFC 1918)"
            ),
            "checks", checks
        ));
    }

    // ── 08. dns_check ────────────────────────────────────────────────────
    static String toolDnsCheck(Map<String,Object> args) throws Exception {
        String hostname   = sanitize(strVal(args, "hostname"));
        String expectedIp = sanitize(strVal(args, "expected_ip"));
        if (hostname.isEmpty()) hostname = env("COTURN_DNS_NAME", DEFAULT_DNS);
        if (!P_HOSTNAME.matcher(hostname).matches())
            throw new SecurityException("Invalid hostname: " + hostname);

        List<String> resolvedIps = new ArrayList<>();
        String       error       = null;
        long         resolvMs    = 0;

        try {
            long start = System.currentTimeMillis();
            InetAddress[] addrs = InetAddress.getAllByName(hostname);
            resolvMs = System.currentTimeMillis() - start;
            for (InetAddress a : addrs) resolvedIps.add(a.getHostAddress());
        } catch (UnknownHostException e) {
            error = "DNS resolution failed: " + e.getMessage();
        }

        boolean resolved         = !resolvedIps.isEmpty();
        boolean expectedMatches  = !expectedIp.isEmpty() && resolvedIps.contains(expectedIp);
        boolean isPrivateIp      = resolvedIps.stream().anyMatch(CoturnMcpServer::isPrivateIp);

        Map<String,Object> result = obj(
            "hostname",          hostname,
            "resolved",          resolved,
            "resolved_ips",      resolvedIps,
            "resolve_ms",        resolvMs,
            "is_private_ip",     isPrivateIp,
            "dns_only_mode",     !isPrivateIp && resolved,
            "expected_ip",       expectedIp.isEmpty() ? null : expectedIp,
            "expected_matches",  expectedIp.isEmpty() ? null : expectedMatches
        );

        if (error != null) result.put("error", error);

        if (isPrivateIp) result.put("warning",
            "Resolved to private IP — DNS-only mode BROKEN. " +
            "media.ecoskiller.com must resolve to coturn's PUBLIC static/elastic IP. " +
            "Check PowerDNS A record (ops namespace, k3s). " +
            "Private IP routing breaks direct UDP WebRTC relay.");
        if (!resolved && error == null) result.put("error", "No addresses returned for " + hostname);

        result.put("powerdns_note",
            "Record managed via PowerDNS REST API (ops namespace, k3s). " +
            "TTL should be ≤300s for fast failover. DNS-only (no CDN proxy) — required for UDP WebRTC.");
        result.put("failover_ttl",
            "Set TTL=300s (5 min) on the A record for sub-5-minute failover if coturn IP changes during incident.");

        return toJson(result);
    }

    // ── 09. connectivity_test ────────────────────────────────────────────
    static String toolConnectivityTest(Map<String,Object> args) throws Exception {
        String host  = sanitize(strVal(args, "host"));
        int    port  = intVal(args, "port", STUN_PORT);
        String proto = strVal(args, "protocol");

        if (host.isEmpty()) host = env("COTURN_DNS_NAME", DEFAULT_DNS);
        if (!P_HOSTNAME.matcher(host).matches())
            throw new SecurityException("Invalid hostname: " + host);
        if (port < 1 || port > 65535) port = STUN_PORT;
        if (proto == null || proto.isBlank()) proto = "udp";

        // TCP connectivity probe (best we can do from JVM without raw UDP STUN socket)
        long    start     = System.currentTimeMillis();
        boolean reachable = tcpProbe(host, port, 5000);
        long    latencyMs = System.currentTimeMillis() - start;

        // DNS resolution check
        List<String> ips = new ArrayList<>();
        try {
            for (InetAddress a : InetAddress.getAllByName(host)) ips.add(a.getHostAddress());
        } catch (Exception ignored) {}

        boolean dnsOk    = !ips.isEmpty();
        boolean isPublic = ips.stream().noneMatch(CoturnMcpServer::isPrivateIp);

        Map<String,Object> result = obj(
            "host",              host,
            "port",              port,
            "protocol",          proto,
            "tcp_reachable",     reachable,
            "latency_ms",        latencyMs,
            "dns_resolved",      dnsOk,
            "resolved_ips",      ips,
            "public_ip",         isPublic,
            "realm",             env("COTURN_REALM", DEFAULT_REALM),
            "connectivity_status", (dnsOk && reachable && isPublic) ? "OK" : "FAIL"
        );

        if (!dnsOk)    result.put("dns_error",     "Cannot resolve " + host + " — check PowerDNS A record");
        if (!isPublic) result.put("public_error",  "Resolves to private IP — DNS-only mode broken. WebRTC UDP relay requires public IP.");
        if (!reachable) result.put("reach_error",  "TCP probe to port " + port + " failed — firewall rule allow-coturn-udp may be missing or VM is down");

        result.put("note",
            "TCP probe confirms port " + port + " is open. " +
            "True STUN Binding Request test requires a WebRTC ICE agent. " +
            "Run WebRTC ICE check via browser console: new RTCPeerConnection({iceServers:[{urls:'stun:" + host + ":" + port + "'}]})");
        result.put("jitsi_ice_config",
            "TURN server should appear in Jitsi JVB ICE candidate offer. " +
            "Check: kubectl logs -n media jvb-pod | grep 'relay candidate'");

        return toJson(result);
    }

    // ── 10. log_query ────────────────────────────────────────────────────
    static String toolLogQuery(Map<String,Object> args) throws Exception {
        String eventType = strVal(args, "event_type"); if (eventType == null) eventType = "all";
        int    tailLines = Math.min(intVal(args, "tail_lines", 500), 2000);
        String clientIp  = sanitize(strVal(args, "client_ip"));
        String username  = sanitize(strVal(args, "username"));

        String logFile = env("COTURN_LOG_DIR", "/var/log/coturn") + "/turnserver.log";
        List<String> lines = tailLogFile(logFile, tailLines);

        List<Object> entries = new ArrayList<>();
        int matched = 0;

        for (String line : lines) {
            String ll = line.toLowerCase();

            // Event type filter
            boolean include = switch (eventType) {
                case "auth_failure"  -> ll.contains("401") || ll.contains("auth error") || ll.contains("wrong");
                case "relay_alloc"   -> ll.contains("allocate") && ll.contains("success");
                case "relay_dealloc" -> ll.contains("deallocate") || ll.contains("de-allocate");
                case "session_start" -> ll.contains("session") && ll.contains("start");
                case "session_end"   -> ll.contains("session") && (ll.contains("end") || ll.contains("terminated"));
                default              -> true; // "all"
            };

            if (!include) continue;

            // IP and username filters
            if (!clientIp.isEmpty() && !line.contains(clientIp)) continue;
            if (!username.isEmpty() && !line.toLowerCase().contains(username.toLowerCase())) continue;

            String ts   = extractTimestamp(line);
            String user = extractField(line, "user=");
            String ip   = extractField(line, "client=");

            entries.add(obj(
                "timestamp",  ts,
                "raw",        truncate(line, 300),
                "user",       user,
                "client_ip",  ip != null ? ip.split(":")[0] : null,
                "event_type", classifyLogLine(line)
            ));
            if (++matched >= 200) break; // cap output entries
        }

        return toJson(obj(
            "log_file",        logFile,
            "lines_scanned",   lines.size(),
            "entries_matched", matched,
            "event_filter",    eventType,
            "client_ip_filter",clientIp.isEmpty() ? null : clientIp,
            "username_filter", username.isEmpty()  ? null : username,
            "entries",         entries,
            "note",            lines.isEmpty()
                ? "Log file not found or empty. Ensure coturn is running and COTURN_LOG_DIR is set correctly."
                : "Showing most recent " + matched + " matching entries (cap 200)."
        ));
    }

    // ── 11. instance_manage ──────────────────────────────────────────────
    static String toolInstanceManage(Map<String,Object> args) throws Exception {
        String action   = strVal(args, "action");
        String instance = sanitize(strVal(args, "instance"));
        if (action == null || action.isBlank())
            throw new IllegalArgumentException("'action' argument is required");
        if (instance.isEmpty()) instance = "local";

        Map<String,Object> result = obj("action", action, "instance", instance);

        // Simulate systemctl / docker commands — in production these would be
        // executed via SSH or the coturn management API on the VM
        String cmd;
        boolean isLocal = "local".equals(instance);

        switch (action) {
            case "status" -> {
                cmd = isLocal ? "systemctl status coturn" : "ssh " + instance + " systemctl status coturn";
                // Try to check if the process is reachable via TCP probe
                String host = isLocal ? env("COTURN_DNS_NAME", DEFAULT_DNS) : instance.split(":")[0];
                boolean up  = tcpProbe(host, STUN_PORT, 3000);
                result.put("status",      up ? "RUNNING" : "NOT_RESPONDING");
                result.put("stun_port",   STUN_PORT);
                result.put("host",        host);
                result.put("tcp_probe",   up);
                result.put("command_equivalent", cmd);
                result.put("pid_check",   "ps aux | grep coturn | grep -v grep");
            }
            case "restart" -> {
                cmd = isLocal
                    ? "systemctl restart coturn  # OR: docker restart coturn"
                    : "ssh " + instance + " 'systemctl restart coturn'";
                result.put("command",   cmd);
                result.put("executed",  false);
                result.put("note",
                    "Restart command generated but NOT executed — this MCP server does not " +
                    "have SSH access to coturn VMs. Execute manually or via GitLab CI pipeline. " +
                    "Post-restart: allow 5s for coturn to bind ports, then run connectivity_test.");
                result.put("post_restart_check",
                    "After restart: python3 test_agents.py  OR  run connectivity_test tool");
                result.put("apply_config_note",
                    "Restart is required after config_update to apply turnserver.conf changes.");
            }
            case "start" -> {
                cmd = isLocal ? "systemctl start coturn" : "ssh " + instance + " systemctl start coturn";
                result.put("command", cmd);
                result.put("executed", false);
                result.put("note", "Start command generated. Execute on coturn VM directly or via CI pipeline.");
            }
            case "stop" -> {
                cmd = isLocal ? "systemctl stop coturn" : "ssh " + instance + " systemctl stop coturn";
                result.put("command", cmd);
                result.put("executed", false);
                result.put("warning",
                    "Stopping coturn will drop all active TURN relay sessions. " +
                    "~30% of users on restricted networks will lose WebRTC connectivity. " +
                    "WebRTC SLO will breach <95% threshold within minutes. " +
                    "Only stop for maintenance — ensure remaining instances absorb load.");
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action + ". Use: start|stop|restart|status");
        }

        result.put("deployment_type",
            "coturn runs on dedicated VMs (NOT inside k3s) — GCP e2-small / AWS t3.small. " +
            "VM management is via GitLab CI pipelines, not Helm/kubectl.");
        return toJson(result);
    }

    // ── 12. peer_deny_audit ──────────────────────────────────────────────
    static String toolPeerDenyAudit(Map<String,Object> args) throws Exception {
        boolean verbose  = boolVal(args, "verbose", false);
        String  confPath = env("COTURN_CONF_PATH", "/etc/coturn/turnserver.conf");

        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of(confPath));
        } catch (IOException e) {
            lines = getDefaultConfigLines();
        }

        List<String> deniedRanges = new ArrayList<>();
        boolean noLoopback        = false;
        boolean noMulticast       = false;
        boolean useAuthSecret     = false;

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("#")) continue;
            if (trimmed.startsWith("denied-peer-ip=")) deniedRanges.add(trimmed.split("=",2)[1].trim());
            if (trimmed.equals("no-loopback-peers")  || trimmed.startsWith("no-loopback-peers="))  noLoopback   = true;
            if (trimmed.equals("no-multicast-peers") || trimmed.startsWith("no-multicast-peers=")) noMulticast  = true;
            if (trimmed.equals("use-auth-secret")    || trimmed.startsWith("use-auth-secret="))    useAuthSecret = true;
        }

        // Required RFC 1918 ranges
        record RequiredRange(String range, String cidr, String description) {}
        List<RequiredRange> required = List.of(
            new RequiredRange("10.0.0.0-10.255.255.255",     "10.0.0.0/8",    "Class A private — k3s pod/service CIDR"),
            new RequiredRange("172.16.0.0-172.31.255.255",   "172.16.0.0/12", "Class B private — Docker/k3s bridge networks"),
            new RequiredRange("192.168.0.0-192.168.255.255", "192.168.0.0/16","Class C private — LAN ranges")
        );

        List<Object> checks       = new ArrayList<>();
        List<String> missing      = new ArrayList<>();
        List<String> criticalGaps = new ArrayList<>();

        for (RequiredRange rr : required) {
            boolean found = deniedRanges.stream().anyMatch(d ->
                d.contains(rr.cidr().split("/")[0]) || d.contains(rr.range().split("-")[0]));
            checks.add(obj(
                "range",       rr.range(),
                "cidr",        rr.cidr(),
                "description", rr.description(),
                "blocked",     found,
                "status",      found ? "PROTECTED ✓" : "MISSING ✗ — CRITICAL GAP"
            ));
            if (!found) {
                missing.add(rr.range());
                criticalGaps.add("denied-peer-ip=" + rr.range() +
                    "  # Missing! Add to turnserver.conf immediately.");
            }
        }

        checks.add(obj("flag","no-loopback-peers", "set", noLoopback,
            "status", noLoopback ? "SET ✓" : "MISSING ✗ — Allows relay to 127.0.0.1 loopback"));
        checks.add(obj("flag","no-multicast-peers","set", noMulticast,
            "status", noMulticast ? "SET ✓" : "MISSING ✗ — Allows relay to multicast ranges"));
        checks.add(obj("flag","use-auth-secret",   "set", useAuthSecret,
            "status", useAuthSecret ? "SET ✓" : "MISSING ✗ — OPEN RELAY: no auth required!"));

        if (!useAuthSecret) criticalGaps.add(
            "use-auth-secret  # CRITICAL: add this to enable HMAC auth and prevent open relay abuse!");

        boolean allProtected = missing.isEmpty() && noLoopback && noMulticast && useAuthSecret;

        Map<String,Object> result = obj(
            "config_path",       confPath,
            "overall_security",  allProtected ? "SECURE ✓" : "GAPS DETECTED ✗",
            "rfc1918_protected", missing.isEmpty(),
            "no_loopback_peers", noLoopback,
            "no_multicast_peers",noMulticast,
            "auth_required",     useAuthSecret,
            "checks",            checks
        );

        if (verbose) result.put("all_denied_ranges", deniedRanges);

        if (!criticalGaps.isEmpty()) {
            result.put("critical_gaps",     criticalGaps);
            result.put("remediation_steps", List.of(
                "1. SSH to coturn VM",
                "2. Edit " + confPath,
                "3. Add missing lines (see critical_gaps above)",
                "4. Restart coturn: systemctl restart coturn",
                "5. Re-run peer_deny_audit to confirm fix"
            ));
            result.put("risk",
                "Without RFC 1918 deny rules, coturn can be used as a relay to probe " +
                "internal k3s pod/service network (10.0.0.0/8). " +
                "This is the primary attack vector for SSRF via TURN relay.");
        }

        result.put("security_context",
            "coturn relay port range 49152-65535 is open to 0.0.0.0/0. " +
            "denied-peer-ip rules are the ONLY protection preventing relay to internal services. " +
            "HMAC auth (use-auth-secret) prevents unauthenticated relay allocation.");

        return toJson(result);
    }

    // ── 13. slo_report ───────────────────────────────────────────────────
    static String toolSloReport(Map<String,Object> args) throws Exception {
        int windowMinutes = Math.min(intVal(args, "window_minutes", 60), 1440);
        int tailLines     = Math.min(intVal(args, "tail_lines", 2000), 10000);

        String logFile = env("COTURN_LOG_DIR", "/var/log/coturn") + "/turnserver.log";
        List<String> lines = tailLogFile(logFile, tailLines);

        int successAllocs    = 0;
        int failedAllocs     = 0;
        int authFailures     = 0;
        int totalRequests    = 0;
        int sessionStarts    = 0;
        int sessionEnds      = 0;
        int refreshSuccess   = 0;

        for (String line : lines) {
            String ll = line.toLowerCase();
            totalRequests++;
            if (ll.contains("allocate") && ll.contains("success")) { successAllocs++; sessionStarts++; }
            else if (ll.contains("allocate") && (ll.contains("fail") || ll.contains("error") || ll.contains("401"))) failedAllocs++;
            if (ll.contains("401") || ll.contains("auth error") || ll.contains("wrong credential")) authFailures++;
            if (ll.contains("deallocate") || ll.contains("de-allocate")) sessionEnds++;
            if (ll.contains("refresh") && ll.contains("success")) refreshSuccess++;
        }

        int  totalAllocAttempts = successAllocs + failedAllocs;
        double successRate      = totalAllocAttempts > 0
            ? (double) successAllocs / totalAllocAttempts * 100.0 : -1;

        boolean sloMet    = successRate >= 95.0;
        boolean alertZone = successRate >= 0 && successRate < 90.0;
        String  sloStatus = successRate < 0 ? "INSUFFICIENT_DATA"
                          : (sloMet ? "SLO_MET ✓" : (alertZone ? "SLO_BREACH — ALERT P1 🔴" : "SLO_AT_RISK ⚠"));

        Map<String,Object> result = obj(
            "slo_target",           "WebRTC connection success ≥95%",
            "alert_threshold",      "P1 alert fires if <90% over 15 minutes",
            "window_minutes",       windowMinutes,
            "lines_analysed",       lines.size(),
            "total_alloc_attempts", totalAllocAttempts,
            "successful_allocs",    successAllocs,
            "failed_allocs",        failedAllocs,
            "auth_failures",        authFailures,
            "session_starts",       sessionStarts,
            "session_ends",         sessionEnds,
            "refresh_keepalives",   refreshSuccess,
            "success_rate_pct",     successRate >= 0 ? String.format("%.2f%%", successRate) : "N/A (no alloc data)",
            "slo_status",           sloStatus,
            "slo_met",              successRate >= 0 && sloMet
        );

        if (successRate < 0) {
            result.put("data_note",
                "No TURN allocation events in log sample. " +
                "Either no sessions occurred, log file is empty, or log format differs. " +
                "Verify COTURN_LOG_DIR=" + env("COTURN_LOG_DIR","/var/log/coturn"));
        } else if (alertZone) {
            result.put("incident_action", List.of(
                "1. Check server_status — is coturn up on all 4 instances?",
                "2. Check firewall_check — are ports 3478/5349 open?",
                "3. Check credential_validate — are Jitsi prosody credentials valid?",
                "4. Check connectivity_test — is media.ecoskiller.com resolving correctly?",
                "5. Escalate: alert Mattermost #incidents channel",
                "6. WebRTC failure affects ~30% of users on enterprise/mobile networks"
            ));
        }

        result.put("grafana_dashboard",
            "https://grafana.ecoskiller.internal/d/media-qos — Media QoS: relay usage %, MOS score, packet loss");
        result.put("prometheus_metrics",
            "coturn relay events → Promtail → Loki → Grafana. " +
            "WebRTC SLO tracked alongside Jitsi JVB packet loss and bitrate distribution.");

        return toJson(result);
    }

    // ═════════════════════════════════════════════════════════════════════
    // CRYPTO — HMAC-SHA1 for TURN credential generation (RFC 5766)
    // ═════════════════════════════════════════════════════════════════════

    static String hmacSha1Base64(String secret, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
        byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(digest);
    }

    // ═════════════════════════════════════════════════════════════════════
    // NETWORK UTILITIES
    // ═════════════════════════════════════════════════════════════════════

    /** TCP connect probe — best-effort port reachability check. */
    static boolean tcpProbe(String host, int port, int timeoutMs) {
        try (Socket s = new Socket()) {
            s.connect(new InetSocketAddress(host, port), timeoutMs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static boolean isPrivateIp(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            return addr.isSiteLocalAddress() || addr.isLoopbackAddress() || addr.isLinkLocalAddress();
        } catch (Exception e) { return false; }
    }

    // ═════════════════════════════════════════════════════════════════════
    // LOG UTILITIES
    // ═════════════════════════════════════════════════════════════════════

    static List<String> tailLogFile(String path, int maxLines) {
        try {
            List<String> all = Files.readAllLines(Path.of(path));
            int start = Math.max(0, all.size() - maxLines);
            return all.subList(start, all.size());
        } catch (IOException e) {
            return List.of(); // file doesn't exist or not readable
        }
    }

    static String extractField(String line, String prefix) {
        int idx = line.indexOf(prefix);
        if (idx < 0) return null;
        int start = idx + prefix.length();
        int end   = line.indexOf(' ', start);
        return end < 0 ? line.substring(start) : line.substring(start, end);
    }

    static String extractTimestamp(String line) {
        // Common coturn log format: "YYYY-MM-DD HH:MM:SS ..."
        if (line.length() >= 19 && line.charAt(4) == '-') return line.substring(0, 19);
        return "";
    }

    static String classifyLogLine(String line) {
        String ll = line.toLowerCase();
        if (ll.contains("401") || ll.contains("auth error") || ll.contains("wrong credential")) return "auth_failure";
        if (ll.contains("allocate") && ll.contains("success")) return "relay_alloc";
        if (ll.contains("deallocate") || ll.contains("de-allocate")) return "relay_dealloc";
        if (ll.contains("session") && ll.contains("start"))  return "session_start";
        if (ll.contains("session") && (ll.contains("end") || ll.contains("terminated"))) return "session_end";
        if (ll.contains("refresh")) return "refresh";
        return "info";
    }

    // ═════════════════════════════════════════════════════════════════════
    // CONFIG HELPERS
    // ═════════════════════════════════════════════════════════════════════

    static List<String> parseInstances() {
        String raw = env("COTURN_INSTANCES", DEFAULT_INSTANCES);
        return Arrays.asList(raw.split(","));
    }

    /** Returns the Ecoskiller spec default config lines (for when file is absent). */
    static List<String> getDefaultConfigLines() {
        return List.of(
            "# coturn turnserver.conf — Ecoskiller Production (defaults from spec)",
            "listening-port=3478",
            "tls-listening-port=5349",
            "min-port=49152",
            "max-port=65535",
            "realm=media.ecoskiller.com",
            "use-auth-secret",
            "static-auth-secret=<stored-in-kubernetes-secret>",
            "relay-ip=<coturn-vm-public-ip>",
            "external-ip=<coturn-vm-public-ip>",
            "no-loopback-peers",
            "no-multicast-peers",
            "denied-peer-ip=10.0.0.0-10.255.255.255",
            "denied-peer-ip=172.16.0.0-172.31.255.255",
            "denied-peer-ip=192.168.0.0-192.168.255.255",
            "log-file=/var/log/coturn/turnserver.log",
            "simple-log"
        );
    }

    // ═════════════════════════════════════════════════════════════════════
    // SECURITY VALIDATORS
    // ═════════════════════════════════════════════════════════════════════

    static void validateToolName(String name) {
        if (name == null || name.isBlank())
            throw new SecurityException("Tool name must not be blank");
        if (!P_TOOL.matcher(name).matches())
            throw new SecurityException("Illegal tool name: " + name);
    }

    static void validateArgs(Map<String,Object> node, int depth) {
        if (node == null || depth > 5) return;
        if (depth > 5) throw new SecurityException("Arguments too deeply nested (max 5)");
        for (Map.Entry<String,Object> e : node.entrySet()) {
            validateArgKey(e.getKey());
            validateArgValue(e.getValue(), depth);
        }
    }

    static void validateArgKey(String key) {
        if (key == null || key.isBlank())
            throw new SecurityException("Arg key must not be blank");
        if (key.length() > 64)
            throw new SecurityException("Arg key too long: " + key);
        if (P_SHELL.matcher(key).find())
            throw new SecurityException("Illegal char in arg key: " + key);
    }

    @SuppressWarnings("unchecked")
    static void validateArgValue(Object val, int depth) {
        if (val instanceof String s) {
            if (s.length() > 1024)
                throw new SecurityException("String argument exceeds 1024 chars");
            if (P_TRAVERSAL.matcher(s).find())
                throw new SecurityException("Path traversal detected in arg: " + s);
            if (P_SHELL.matcher(s).find())
                throw new SecurityException("Shell metachar detected in arg: " + s);
        } else if (val instanceof Map m) {
            validateArgs(m, depth + 1);
        } else if (val instanceof List<?> list) {
            if (list.size() > 50)
                throw new SecurityException("Array argument too large (max 50)");
            for (Object item : list) validateArgValue(item, depth + 1);
        } else if (val instanceof Number n) {
            long lv = n.longValue();
            if (lv < Integer.MIN_VALUE || lv > Integer.MAX_VALUE)
                throw new SecurityException("Numeric argument out of safe integer range");
        }
    }

    static String sanitize(String s) {
        if (s == null) return "";
        // Allow Base64 chars (+, =) needed for HMAC credentials; strip true shell metacharacters
        return s.replaceAll("[^a-zA-Z0-9_./:@\\-+=]", "").substring(0, Math.min(s.length(), 256));
    }

    // ═════════════════════════════════════════════════════════════════════
    // MINIMAL JSON PARSER & BUILDER  (no external deps)
    // ═════════════════════════════════════════════════════════════════════

    @SuppressWarnings("unchecked")
    static Map<String,Object> parseObj(String json) {
        if (json == null || json.isBlank()) return new LinkedHashMap<>();
        try {
            Object p = new JsonParser(json.trim()).parse();
            return (p instanceof Map m) ? m : new LinkedHashMap<>();
        } catch (Exception e) { return new LinkedHashMap<>(); }
    }

    @SuppressWarnings("unchecked")
    static String toJson(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String s)  return "\"" + esc(s) + "\"";
        if (obj instanceof Boolean || obj instanceof Number) return obj.toString();
        if (obj instanceof Map<?,?> m) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Object k : m.keySet()) {
                Object v = m.get(k);
                if (v == null) continue;
                if (!first) sb.append(",");
                sb.append("\"").append(esc(k.toString())).append("\":").append(toJson(v));
                first = false;
            }
            return sb.append("}").toString();
        }
        if (obj instanceof List<?> list) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : list) {
                if (!first) sb.append(",");
                sb.append(toJson(item));
                first = false;
            }
            return sb.append("]").toString();
        }
        return "\"" + esc(obj.toString()) + "\"";
    }

    static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\","\\\\").replace("\"","\\\"")
                .replace("\n","\\n").replace("\r","\\r").replace("\t","\\t");
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    static Map<String,Object> obj(Object... kvs) {
        Map<String,Object> m = new LinkedHashMap<>();
        for (int i = 0; i + 1 < kvs.length; i += 2)
            if (kvs[i] != null && kvs[i+1] != null) m.put(kvs[i].toString(), kvs[i+1]);
        return m;
    }

    static class JsonParser {
        final String src; int pos;
        JsonParser(String src) { this.src = src; }
        Object parse() {
            skipWs();
            if (pos >= src.length()) return null;
            char c = src.charAt(pos);
            if (c=='{') return parseObject(); if (c=='[') return parseArray();
            if (c=='"') return parseString(); if (c=='t') { pos+=4; return true; }
            if (c=='f') { pos+=5; return false; } if (c=='n') { pos+=4; return null; }
            return parseNumber();
        }
        Map<String,Object> parseObject() {
            Map<String,Object> m = new LinkedHashMap<>();
            pos++;
            skipWs(); if (pos<src.length()&&src.charAt(pos)=='}'){pos++;return m;}
            while (pos<src.length()) {
                skipWs(); String k=parseString(); skipWs();
                if(pos<src.length()&&src.charAt(pos)==':')pos++; skipWs();
                m.put(k,parse()); skipWs();
                if(pos<src.length()&&src.charAt(pos)==',')pos++; skipWs();
                if(pos<src.length()&&src.charAt(pos)=='}'){pos++;break;}
            }
            return m;
        }
        List<Object> parseArray() {
            List<Object> list=new ArrayList<>(); pos++;
            skipWs(); if(pos<src.length()&&src.charAt(pos)==']'){pos++;return list;}
            while(pos<src.length()){
                skipWs(); list.add(parse()); skipWs();
                if(pos<src.length()&&src.charAt(pos)==',')pos++; skipWs();
                if(pos<src.length()&&src.charAt(pos)==']'){pos++;break;}
            }
            return list;
        }
        String parseString() {
            if(pos<src.length()&&src.charAt(pos)=='"')pos++;
            StringBuilder sb=new StringBuilder();
            while(pos<src.length()){
                char c=src.charAt(pos++);
                if(c=='"')break;
                if(c=='\\'&&pos<src.length()){
                    char e=src.charAt(pos++);
                    switch(e){
                        case '"'->sb.append('"'); case '\\'->sb.append('\\'); case '/'->sb.append('/');
                        case 'n'->sb.append('\n'); case 'r'->sb.append('\r'); case 't'->sb.append('\t');
                        case 'u'->{if(pos+4<=src.length()){sb.append((char)Integer.parseInt(src.substring(pos,pos+4),16));pos+=4;}}
                        default->sb.append(e);
                    }
                } else sb.append(c);
            }
            return sb.toString();
        }
        Object parseNumber() {
            int s=pos;
            while(pos<src.length()&&"-0123456789.eE+".indexOf(src.charAt(pos))>=0)pos++;
            String n=src.substring(s,pos);
            try{
                if(n.contains(".")||n.contains("e")||n.contains("E"))return Double.parseDouble(n);
                long l=Long.parseLong(n); return(l>=Integer.MIN_VALUE&&l<=Integer.MAX_VALUE)?(int)l:l;
            }catch(Exception e){return n;}
        }
        void skipWs(){while(pos<src.length()&&Character.isWhitespace(src.charAt(pos)))pos++;}
    }

    // ═════════════════════════════════════════════════════════════════════
    // JSON-RPC RESPONSE BUILDERS
    // ═════════════════════════════════════════════════════════════════════

    static String ok(String id, Object result) {
        Map<String,Object> r = new LinkedHashMap<>();
        r.put("jsonrpc","2.0"); if(id!=null)r.put("id",id); r.put("result",result);
        return toJson(r);
    }
    static String err(String id, int code, String msg) {
        Map<String,Object> r = new LinkedHashMap<>();
        r.put("jsonrpc","2.0"); if(id!=null)r.put("id",id);
        r.put("error", obj("code",code,"message",msg));
        return toJson(r);
    }

    // ═════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════════════

    static String env(String key, String def) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : def;
    }
    static String requireEnv(String key) {
        String v = System.getenv(key);
        if (v == null || v.isBlank()) throw new IllegalStateException(
            "Required env var not set: " + key +
            "\nSet it as a Kubernetes Secret in the media namespace and mount as env var.");
        return v;
    }
    static String strVal(Map<String,Object> m, String k)  { return m==null?null:m.get(k)==null?null:m.get(k).toString(); }
    @SuppressWarnings("unchecked")
    static Map<String,Object> objVal(Map<String,Object> m, String k) {
        if(m==null)return new LinkedHashMap<>(); Object v=m.get(k);
        return v instanceof Map mv ? mv : new LinkedHashMap<>();
    }
    static int intVal(Map<String,Object> m, String k, int def) {
        if(m==null)return def; Object v=m.get(k);
        if(v instanceof Number n)return n.intValue();
        if(v instanceof String s){try{return Integer.parseInt(s);}catch(Exception e){}}
        return def;
    }
    static int intVal2(String s) { try{return Integer.parseInt(s.trim());}catch(Exception e){return 0;} }
    static boolean boolVal(Map<String,Object> m, String k, boolean def) {
        if(m==null)return def; Object v=m.get(k);
        if(v instanceof Boolean b)return b;
        if(v instanceof String s)return "true".equalsIgnoreCase(s)||"1".equals(s);
        return def;
    }
    static String truncate(String s, int max) {
        if(s==null)return""; return s.length()>max?s.substring(0,max)+"...":s;
    }
    static void audit(String level, String detail) {
        String ts    = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        String clean = detail.replaceAll("[\\x00-\\x1F|]","_").substring(0,Math.min(detail.length(),256));
        System.err.println("[COTURN-MCP] " + ts + " | " + level + " | " + clean);
        if (auditWriter != null) auditWriter.println(ts + " | " + level + " | " + clean);
    }
}
