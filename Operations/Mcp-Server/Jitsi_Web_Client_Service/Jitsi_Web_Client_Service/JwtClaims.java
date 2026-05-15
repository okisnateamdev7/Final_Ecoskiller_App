package io.ecoskiller.mcp.security;

/**
 * JWT Claims model for Ecoskiller Jitsi tokens.
 * Example payload:
 * {
 *   "sub": "cand-001",
 *   "role": "candidate",
 *   "assessmentId": "gd-12345",
 *   "roomId": "gd-12345",
 *   "permissions": ["join", "chat"],
 *   "exp": 1709000000,
 *   "iat": 1708996400,
 *   "iss": "ecoskiller.io",
 *   "jti": "nonce-abc123"
 * }
 */
public class JwtClaims {
    private String userId;      // sub
    private String role;        // candidate | interviewer | admin
    private String assessmentId;
    private String roomId;
    private long   exp;
    private long   iat;
    private String issuer;      // iss
    private String jti;         // nonce for replay prevention

    // Simple hand-rolled JSON field extractor (no external libs)
    public static JwtClaims fromJson(String json) {
        JwtClaims c = new JwtClaims();
        c.userId       = extractString(json, "sub");
        c.role         = extractString(json, "role");
        c.assessmentId = extractString(json, "assessmentId");
        c.roomId       = extractString(json, "roomId");
        c.issuer       = extractString(json, "iss");
        c.jti          = extractString(json, "jti");
        c.exp          = extractLong(json, "exp");
        c.iat          = extractLong(json, "iat");

        // Default role to candidate if not present
        if (c.role == null || c.role.isBlank()) c.role = "candidate";
        return c;
    }

    private static String extractString(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx + search.length());
        if (colon < 0) return null;
        // Skip whitespace
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
        if (start >= json.length()) return null;
        if (json.charAt(start) == '"') {
            int end = json.indexOf('"', start + 1);
            if (end < 0) return null;
            return json.substring(start + 1, end);
        }
        return null;
    }

    private static long extractLong(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx < 0) return 0;
        int colon = json.indexOf(':', idx + search.length());
        if (colon < 0) return 0;
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
        try {
            return Long.parseLong(json.substring(start, end));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Getters
    public String getUserId()      { return userId; }
    public String getRole()        { return role; }
    public String getAssessmentId(){ return assessmentId; }
    public String getRoomId()      { return roomId; }
    public long   getExp()         { return exp; }
    public long   getIat()         { return iat; }
    public String getIssuer()      { return issuer; }
    public String getJti()         { return jti; }

    @Override
    public String toString() {
        return "JwtClaims{userId='" + userId + "', role='" + role +
               "', roomId='" + roomId + "', exp=" + exp + "}";
    }
}
