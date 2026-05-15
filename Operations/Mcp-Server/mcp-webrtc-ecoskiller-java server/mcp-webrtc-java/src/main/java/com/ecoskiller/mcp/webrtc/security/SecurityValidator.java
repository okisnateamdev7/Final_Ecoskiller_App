package com.ecoskiller.mcp.webrtc.security;

import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.util.logging.Logger;

/**
 * SecurityValidator — Central security gate for the WebRTC MCP Server.
 *
 * Protections implemented:
 *   1. JSON-RPC request structure validation
 *   2. Tool name whitelist enforcement
 *   3. Input field sanitisation (XSS / injection prevention)
 *   4. JWT token format validation (structure, not crypto — crypto lives in auth-service)
 *   5. Session ID / Room name format enforcement
 *   6. ICE candidate input validation
 *   7. Rate limiting per caller identity (in-memory, configurable)
 *   8. DTLS fingerprint format validation (SHA-256 hex)
 *   9. Numeric range validation helpers
 *  10. Audit event generation
 */
public class SecurityValidator {

    private static final Logger LOG = Logger.getLogger(SecurityValidator.class.getName());

    // ── Tool whitelist ──────────────────────────────────────────────────
    private static final Set<String> ALLOWED_TOOLS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
        "webrtc_session_create",
        "webrtc_session_terminate",
        "webrtc_jwt_issue",
        "webrtc_ice_negotiate",
        "webrtc_sdp_offer_answer",
        "webrtc_dtls_srtp_status",
        "webrtc_media_quality",
        "webrtc_gd_mute_control",
        "webrtc_turn_allocation",
        "webrtc_pstn_bridge",
        "webrtc_participant_stats",
        "webrtc_audit_log"
    )));

    // ── Patterns ────────────────────────────────────────────────────────
    // session_id: alphanumeric + hyphens, 8-64 chars
    private static final Pattern SESSION_ID_PATTERN  = Pattern.compile("^[a-zA-Z0-9\\-_]{8,64}$");
    // tenant_id: alphanumeric + hyphens, 4-32 chars
    private static final Pattern TENANT_ID_PATTERN   = Pattern.compile("^[a-zA-Z0-9\\-]{4,32}$");
    // user_id: alphanumeric + hyphens, 4-64 chars
    private static final Pattern USER_ID_PATTERN     = Pattern.compile("^[a-zA-Z0-9\\-_@.]{4,64}$");
    // JWT: three Base64url segments separated by dots
    private static final Pattern JWT_FORMAT_PATTERN  = Pattern.compile("^[A-Za-z0-9_\\-]+\\.[A-Za-z0-9_\\-]+\\.[A-Za-z0-9_\\-]+$");
    // DTLS SHA-256 fingerprint: 32 hex pairs colon-separated
    private static final Pattern DTLS_FP_PATTERN     = Pattern.compile("^([0-9A-Fa-f]{2}:){31}[0-9A-Fa-f]{2}$");
    // SDP candidate (basic check — starts with "candidate:")
    private static final Pattern ICE_CANDIDATE_PATTERN = Pattern.compile("^candidate:[a-zA-Z0-9+/=\\s.:]+$");
    // IP address (v4 or v6 rough check)
    private static final Pattern IP_PATTERN          = Pattern.compile(
        "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)" +
        "|([0-9a-fA-F:]+)$");
    // Allowed audio codecs
    private static final Set<String> ALLOWED_CODECS  = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("opus", "vp8", "vp9", "h264", "pcma", "pcmu")));
    // Allowed session modes
    private static final Set<String> ALLOWED_MODES   = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("gd", "interview", "dojo", "pstn")));

    // ── Rate limiting ────────────────────────────────────────────────────
    // Simple sliding-window: max 30 calls / minute per caller_id
    private static final int RATE_LIMIT_MAX   = 30;
    private static final int RATE_LIMIT_WINDOW_MS = 60_000;
    private final ConcurrentHashMap<String, Deque<Long>> rateLimitMap = new ConcurrentHashMap<>();

    // ── Public API ───────────────────────────────────────────────────────

    /** Validate top-level JSON-RPC 2.0 structure. */
    public boolean isValidJsonRpcRequest(JSONObject req) {
        if (req == null) return false;
        if (!"2.0".equals(req.optString("jsonrpc"))) return false;
        String method = req.optString("method", "");
        if (method.isEmpty() || method.length() > 64) return false;
        // Prevent method injection
        if (!method.matches("[a-zA-Z0-9_/]+")) return false;
        return true;
    }

    /** Check tool name against whitelist. */
    public boolean isAllowedToolName(String name) {
        return name != null && ALLOWED_TOOLS.contains(name);
    }

    /** Validate and sanitise a session ID. Throws SecurityException if invalid. */
    public String requireSessionId(JSONObject args, String field) {
        String val = requireString(args, field, 8, 64);
        if (!SESSION_ID_PATTERN.matcher(val).matches()) {
            throw new SecurityException("Invalid session_id format: " + sanitiseForLog(val));
        }
        return val;
    }

    /** Validate tenant ID. */
    public String requireTenantId(JSONObject args, String field) {
        String val = requireString(args, field, 4, 32);
        if (!TENANT_ID_PATTERN.matcher(val).matches()) {
            throw new SecurityException("Invalid tenant_id format: " + sanitiseForLog(val));
        }
        return val;
    }

    /** Validate user ID. */
    public String requireUserId(JSONObject args, String field) {
        String val = requireString(args, field, 4, 64);
        if (!USER_ID_PATTERN.matcher(val).matches()) {
            throw new SecurityException("Invalid user_id format: " + sanitiseForLog(val));
        }
        return val;
    }

    /** Validate JWT token format (structure only). */
    public String requireJwtToken(JSONObject args, String field) {
        String val = requireString(args, field, 20, 2048);
        if (!JWT_FORMAT_PATTERN.matcher(val).matches()) {
            throw new SecurityException("Invalid JWT format for field: " + field);
        }
        return val;
    }

    /** Validate DTLS-SRTP fingerprint (SHA-256). */
    public String requireDtlsFingerprint(JSONObject args, String field) {
        String val = requireString(args, field, 95, 95);
        if (!DTLS_FP_PATTERN.matcher(val).matches()) {
            throw new SecurityException("Invalid DTLS fingerprint format (expected SHA-256 hex pairs): " + field);
        }
        return val.toLowerCase();
    }

    /** Validate ICE candidate string. */
    public String requireIceCandidate(JSONObject args, String field) {
        String val = requireString(args, field, 10, 512);
        if (!ICE_CANDIDATE_PATTERN.matcher(val).matches()) {
            throw new SecurityException("Invalid ICE candidate format: " + field);
        }
        return val;
    }

    /** Validate IP address. */
    public String requireIpAddress(JSONObject args, String field) {
        String val = requireString(args, field, 7, 45);
        if (!IP_PATTERN.matcher(val).matches()) {
            throw new SecurityException("Invalid IP address: " + field);
        }
        return val;
    }

    /** Validate port (1–65535). */
    public int requirePort(JSONObject args, String field) {
        int val = requireInt(args, field, 1, 65535);
        return val;
    }

    /** Validate audio codec. */
    public String requireCodec(JSONObject args, String field) {
        String val = requireString(args, field, 2, 16).toLowerCase();
        if (!ALLOWED_CODECS.contains(val)) {
            throw new SecurityException("Unsupported codec: " + sanitiseForLog(val));
        }
        return val;
    }

    /** Validate session mode (gd / interview / dojo / pstn). */
    public String requireSessionMode(JSONObject args, String field) {
        String val = requireString(args, field, 2, 16).toLowerCase();
        if (!ALLOWED_MODES.contains(val)) {
            throw new SecurityException("Invalid session mode: " + sanitiseForLog(val));
        }
        return val;
    }

    /** Validate that an optional string field, if present, passes length bounds and no injection chars. */
    public Optional<String> optionalSafeString(JSONObject args, String field, int maxLen) {
        if (!args.has(field)) return Optional.empty();
        String val = args.optString(field, "").trim();
        if (val.isEmpty()) return Optional.empty();
        if (val.length() > maxLen) throw new SecurityException("Field too long: " + field);
        return Optional.of(sanitiseString(val));
    }

    /** Apply per-caller rate limiting. callerId can be session_id or user_id. */
    public void enforceRateLimit(String callerId) {
        if (callerId == null || callerId.isEmpty()) return;
        long now = System.currentTimeMillis();
        Deque<Long> window = rateLimitMap.computeIfAbsent(callerId, k -> new ArrayDeque<>());
        synchronized (window) {
            // drop entries outside window
            while (!window.isEmpty() && now - window.peekFirst() > RATE_LIMIT_WINDOW_MS) {
                window.pollFirst();
            }
            if (window.size() >= RATE_LIMIT_MAX) {
                throw new SecurityException("Rate limit exceeded for caller: " + sanitiseForLog(callerId));
            }
            window.addLast(now);
        }
    }

    /** Generate a safe audit event string (no injection vectors). */
    public String buildAuditEvent(String action, String sessionId, String userId) {
        return String.format("ACTION=%s SESSION=%s USER=%s TS=%d",
            sanitiseForLog(action),
            sanitiseForLog(sessionId),
            sanitiseForLog(userId),
            System.currentTimeMillis());
    }

    // ── Private helpers ─────────────────────────────────────────────────

    public String requireString(JSONObject args, String field, int minLen, int maxLen) {
        if (!args.has(field)) throw new SecurityException("Missing required field: " + field);
        String val = args.optString(field, "").trim();
        if (val.length() < minLen || val.length() > maxLen) {
            throw new SecurityException("Field '" + field + "' length out of range [" + minLen + "," + maxLen + "]");
        }
        return val;
    }

    public int requireInt(JSONObject args, String field, int min, int max) {
        if (!args.has(field)) throw new SecurityException("Missing required field: " + field);
        int val;
        try { val = args.getInt(field); }
        catch (Exception e) { throw new SecurityException("Field '" + field + "' must be an integer"); }
        if (val < min || val > max) {
            throw new SecurityException("Field '" + field + "' out of range [" + min + "," + max + "]");
        }
        return val;
    }

    public boolean requireBoolean(JSONObject args, String field) {
        if (!args.has(field)) throw new SecurityException("Missing required field: " + field);
        try { return args.getBoolean(field); }
        catch (Exception e) { throw new SecurityException("Field '" + field + "' must be boolean"); }
    }

    /** Remove control chars and truncate for safe log output. */
    private String sanitiseForLog(String s) {
        if (s == null) return "<null>";
        return s.replaceAll("[\\p{Cntrl}]", "").substring(0, Math.min(s.length(), 80));
    }

    /** Strip HTML/script injection vectors from a user-supplied string. */
    private String sanitiseString(String s) {
        return s.replaceAll("[<>\"'`]", "").trim();
    }
}
