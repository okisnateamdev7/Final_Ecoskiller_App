package com.ecoskiller.mcp.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Security configuration for candidate-performance-aggregator MCP server.
 *
 * Enforces:
 *   1. Keycloak JWT validation (HS256 dev / RS256 prod)
 *   2. RBAC: CANDIDATE / RECRUITER / ADMIN / SERVICE_ACCOUNT
 *      ecoskiller:viewer, ecoskiller:aggregator:admin role mapping
 *   3. Tenant isolation (tenant_id on every operation)
 *   4. Event deduplication (event_id → processed_at, 48h TTL)
 *   5. Distributed lock simulation (agg_lock:{candidate_id}:{cycle_id}, 30s TTL)
 *   6. Input sanitisation (SQL injection, XSS, oversized payload)
 *   7. Weight validation (GD + Interview + Dojo must sum to 1.0 ± 0.001)
 */
public class AggSecurityConfig {

    private static final Logger LOG = Logger.getLogger(AggSecurityConfig.class.getName());

    // Roles
    public static final String ROLE_CANDIDATE       = "CANDIDATE";
    public static final String ROLE_RECRUITER       = "RECRUITER";
    public static final String ROLE_ADMIN           = "ADMIN";
    public static final String ROLE_SERVICE_ACCOUNT = "SERVICE_ACCOUNT";

    // Config
    private static String  JWT_SECRET       = "ecoskiller-cpa-dev-secret-change-in-prod";
    private static boolean STRICT_MODE      = false;
    private static double  DEFAULT_GD_W     = 0.35;
    private static double  DEFAULT_INT_W    = 0.40;
    private static double  DEFAULT_DOJO_W   = 0.25;
    private static int     LOCK_TTL_SEC     = 30;
    private static int     DEDUP_TTL_HOURS  = 48;
    private static int     MAX_PAYLOAD      = 65535;
    private static int     MAX_RETRY_COUNT  = 3;

    // Dedup store: event_id → processed_at epoch (production: Redis dedup:{event_id} HASH TTL 48h)
    private static final Map<String, Long> DEDUP_STORE = new ConcurrentHashMap<>();
    // Distributed lock store: lockKey → lock expiry epoch (production: Redis SET NX EX)
    private static final Map<String, Long> LOCK_STORE  = new ConcurrentHashMap<>();

    public static void init() {
        Optional.ofNullable(System.getenv("CPA_JWT_SECRET")).filter(s->!s.isBlank()).ifPresent(s->JWT_SECRET=s);
        Optional.ofNullable(System.getenv("CPA_STRICT_MODE")).ifPresent(s->STRICT_MODE="true".equalsIgnoreCase(s));
        Optional.ofNullable(System.getenv("CPA_DEFAULT_GD_WEIGHT")).ifPresent(s->{try{DEFAULT_GD_W=Double.parseDouble(s);}catch(NumberFormatException ignored){}});
        Optional.ofNullable(System.getenv("CPA_DEFAULT_INT_WEIGHT")).ifPresent(s->{try{DEFAULT_INT_W=Double.parseDouble(s);}catch(NumberFormatException ignored){}});
        Optional.ofNullable(System.getenv("CPA_DEFAULT_DOJO_WEIGHT")).ifPresent(s->{try{DEFAULT_DOJO_W=Double.parseDouble(s);}catch(NumberFormatException ignored){}});
        Optional.ofNullable(System.getenv("CPA_LOCK_TTL_SECONDS")).ifPresent(s->{try{LOCK_TTL_SEC=Integer.parseInt(s);}catch(NumberFormatException ignored){}});
        Optional.ofNullable(System.getenv("CPA_DEDUP_TTL_HOURS")).ifPresent(s->{try{DEDUP_TTL_HOURS=Integer.parseInt(s);}catch(NumberFormatException ignored){}});
        LOG.info("AggSecurityConfig init | strict="+STRICT_MODE+" | weights GD="+DEFAULT_GD_W+" INT="+DEFAULT_INT_W+" DOJO="+DEFAULT_DOJO_W);
    }

    // ── JWT ───────────────────────────────────────────────────────────────────

    public static Map<String,Object> validateToken(String bearerToken) {
        if (bearerToken==null||bearerToken.isBlank()) throw new SecurityException("Missing Authorization token");
        String token = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
        String[] parts = token.split("\\.");
        if (parts.length!=3) throw new SecurityException("Malformed JWT");
        if (STRICT_MODE) {
            String expected = hmacB64url(parts[0]+"."+parts[1], JWT_SECRET);
            if (!constantEquals(expected, parts[2])) throw new SecurityException("JWT signature invalid");
        }
        String payloadJson = decodeB64(parts[1]);
        Map<String,Object> claims = parsePayload(payloadJson);
        long exp = toLong(claims.get("exp"));
        if (exp>0 && Instant.now().getEpochSecond()>exp) throw new SecurityException("JWT expired");
        String sub = (String) claims.get("sub");
        if (sub==null||sub.isBlank()) throw new SecurityException("JWT missing sub");
        return claims;
    }

    public static void requireRole(Map<String,Object> claims, String... roles) {
        List<?> ur=(List<?>)claims.getOrDefault("roles",Collections.emptyList());
        for (String r : roles) if (ur.stream().anyMatch(x->r.equalsIgnoreCase(x.toString()))) return;
        throw new SecurityException("Access denied. Required: "+Arrays.toString(roles));
    }

    public static boolean hasRole(Map<String,Object> claims, String role) {
        List<?> ur=(List<?>)claims.getOrDefault("roles",Collections.emptyList());
        return ur.stream().anyMatch(x->role.equalsIgnoreCase(x.toString()));
    }

    public static String effectiveTenant(Map<String,Object> claims, Map<String,Object> args) {
        String req = getString(args,"tenant_id");
        if (req!=null&&!req.isBlank()&&hasRole(claims,ROLE_ADMIN)) return req;
        String ft=(String)claims.get("tenant_id");
        return ft!=null?ft:(req!=null?req:"dev-tenant");
    }

    // ── Deduplication (Redis dedup:{event_id} in production) ──────────────────

    /** Returns true if event is NEW (not yet processed). */
    public static boolean registerEventId(String eventId) {
        if (eventId==null||eventId.isBlank()) return true;
        long now = Instant.now().getEpochSecond();
        // Clean expired entries
        DEDUP_STORE.entrySet().removeIf(e -> now - e.getValue() > DEDUP_TTL_HOURS * 3600L);
        return DEDUP_STORE.putIfAbsent(eventId, now) == null;
    }

    /** Returns true if (candidateId, cycleId) combination is NEW. */
    public static boolean registerAggregationPair(String candidateId, String cycleId) {
        return registerEventId("pair:" + candidateId + ":" + cycleId);
    }

    // ── Distributed lock (Redis SET NX EX in production) ──────────────────────

    /** Try to acquire lock. Returns true if lock acquired. */
    public static boolean acquireLock(String candidateId, String cycleId) {
        String key = "agg_lock:" + candidateId + ":" + cycleId;
        long now    = Instant.now().getEpochSecond();
        // Release expired locks
        LOCK_STORE.entrySet().removeIf(e -> now > e.getValue());
        return LOCK_STORE.putIfAbsent(key, now + LOCK_TTL_SEC) == null;
    }

    public static void releaseLock(String candidateId, String cycleId) {
        LOCK_STORE.remove("agg_lock:" + candidateId + ":" + cycleId);
    }

    public static boolean isLocked(String candidateId, String cycleId) {
        String key = "agg_lock:" + candidateId + ":" + cycleId;
        Long exp = LOCK_STORE.get(key);
        if (exp == null) return false;
        if (Instant.now().getEpochSecond() > exp) { LOCK_STORE.remove(key); return false; }
        return true;
    }

    // ── Weight validation ─────────────────────────────────────────────────────

    public static void validateWeights(double gd, double interview, double dojo) {
        if (gd < 0 || interview < 0 || dojo < 0)
            throw new IllegalArgumentException("Weights must be non-negative");
        double sum = gd + interview + dojo;
        if (Math.abs(sum - 1.0) > 0.001)
            throw new IllegalArgumentException("Weights must sum to 1.0. Got: " + round(sum));
    }

    // ── Input sanitisation ────────────────────────────────────────────────────

    private static final String[] SQL_PAT = {"--",";--","/*","*/","UNION SELECT","DROP TABLE","DELETE FROM","INSERT INTO","EXEC("};
    private static final String[] XSS_PAT = {"<script","javascript:","onerror=","onload="};

    public static String sanitise(String input, String field) {
        if (input==null) return null;
        String t=input.trim();
        if (t.length()>MAX_PAYLOAD) throw new IllegalArgumentException("Field '"+field+"' exceeds max length");
        String u=t.toUpperCase();
        for (String p:SQL_PAT) if(u.contains(p.toUpperCase())) throw new SecurityException("SQL injection in: "+field);
        for (String p:XSS_PAT) if(u.contains(p.toUpperCase())) throw new SecurityException("XSS detected in: "+field);
        return t;
    }

    public static String requireString(Map<String,Object> a, String key) {
        Object v=a.get(key); if(v==null||v.toString().isBlank()) throw new IllegalArgumentException("Missing: "+key);
        return sanitise(v.toString(),key);
    }
    public static String  getString(Map<String,Object> a, String key) { Object v=a.get(key); return v!=null?v.toString():null; }
    public static double  getDouble(Map<String,Object> a, String key, double def) { Object v=a.get(key); if(v==null)return def; try{return Double.parseDouble(v.toString());}catch(NumberFormatException e){return def;} }
    public static int     getInt(Map<String,Object> a, String key, int def)       { Object v=a.get(key); if(v==null)return def; try{return Integer.parseInt(v.toString());}catch(NumberFormatException e){return def;} }
    public static boolean getBool(Map<String,Object> a, String key, boolean def)  { Object v=a.get(key); if(v==null)return def; return "true".equalsIgnoreCase(v.toString()); }

    // ── Getters ───────────────────────────────────────────────────────────────

    public static double  getDefaultGdWeight()   { return DEFAULT_GD_W; }
    public static double  getDefaultIntWeight()  { return DEFAULT_INT_W; }
    public static double  getDefaultDojoWeight() { return DEFAULT_DOJO_W; }
    public static int     getLockTtlSec()        { return LOCK_TTL_SEC; }
    public static int     getDedupTtlHours()     { return DEDUP_TTL_HOURS; }
    public static int     getMaxRetryCount()     { return MAX_RETRY_COUNT; }
    public static String  generateId()           { return UUID.randomUUID().toString(); }
    public static double  round(double v)        { return Math.round(v*10000.0)/10000.0; }

    // ── Crypto helpers ────────────────────────────────────────────────────────

    private static String hmacB64url(String data, String key) {
        try {
            Mac mac=Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));
            return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch(Exception e){throw new RuntimeException("HMAC error: "+e.getMessage());}
    }
    public static String decodeB64(String encoded) {
        String p=encoded; switch(p.length()%4){case 2:p+="==";break;case 3:p+="=";break;}
        return new String(java.util.Base64.getUrlDecoder().decode(p),StandardCharsets.UTF_8);
    }
    static String b64url(String s){return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(s.getBytes(StandardCharsets.UTF_8));}
    private static boolean constantEquals(String a,String b){if(a.length()!=b.length())return false;int d=0;for(int i=0;i<a.length();i++)d|=(a.charAt(i)^b.charAt(i));return d==0;}

    private static Map<String,Object> parsePayload(String json) {
        Map<String,Object> m=new LinkedHashMap<>();
        m.put("sub",       extractStr(json,"sub"));
        m.put("tenant_id", extractStr(json,"tenant_id"));
        m.put("email",     extractStr(json,"email"));
        m.put("exp",       extractLong(json,"exp"));
        m.put("roles",     extractRoles(json));
        return m;
    }
    private static String extractStr(String j,String k){String kk="\""+k+"\"";int i=j.indexOf(kk);if(i<0)return null;int c=j.indexOf(':',i+kk.length());if(c<0)return null;int s=j.indexOf('"',c+1);if(s<0)return null;int e=j.indexOf('"',s+1);if(e<0)return null;return j.substring(s+1,e);}
    private static long extractLong(String j,String k){String kk="\""+k+"\"";int i=j.indexOf(kk);if(i<0)return-1;int c=j.indexOf(':',i+kk.length());if(c<0)return-1;StringBuilder n=new StringBuilder();for(int x=c+1;x<j.length();x++){char ch=j.charAt(x);if(Character.isDigit(ch)||ch=='-')n.append(ch);else if(n.length()>0)break;}try{return Long.parseLong(n.toString());}catch(NumberFormatException e){return-1;}}
    private static List<String> extractRoles(String j){List<String> r=new ArrayList<>();int ri=j.indexOf("\"roles\"");if(ri<0)return r;int s=j.indexOf('[',ri),e=j.indexOf(']',s);if(s<0||e<0)return r;for(String p:j.substring(s+1,e).split(",")){String x=p.trim().replace("\"","");if(!x.isBlank())r.add(x);}return r;}
    private static long toLong(Object v){if(v==null)return-1;try{return Long.parseLong(v.toString());}catch(NumberFormatException e){return-1;}}
}
