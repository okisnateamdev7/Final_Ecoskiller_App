package io.ecoskiller.scoring.security;

import com.fasterxml.jackson.databind.JsonNode;
import io.ecoskiller.scoring.config.ServerConfig;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.regex.Pattern;

// ═══════════════════════════════════════════════════════════════════════════════
// INPUT SANITIZER
// ═══════════════════════════════════════════════════════════════════════════════
public final class InputSanitizer {

    private static final Pattern ID_PATTERN     = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9\\-_]{0,63}$");
    private static final Pattern UUID_PATTERN   = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private static final Pattern HASH_PATTERN   = Pattern.compile("^(sha256:|sha512:|md5:)[a-fA-F0-9]{32,128}$");
    private static final Pattern MODEL_VER_PATTERN = Pattern.compile("^v[0-9]{1,3}(\\.\\d{1,3})?(-[a-zA-Z0-9]{1,20})?$");
    private static final Pattern ASSESSMENT_TYPE_PATTERN = Pattern.compile("^(gd|interview|match|dojo)$");
    private static final Pattern BELT_PATTERN   = Pattern.compile("^(bronze|silver|gold|platinum|diamond|master)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern DOMAIN_PATTERN = Pattern.compile("^(software_engineering|data_science|product_management|sales|marketing|design|finance|hr|other)$");
    private static final Pattern DIMENSION_PATTERN = Pattern.compile("^(communication|technical|cultural_fit|overall)$");
    private static final Pattern DEMOGRAPHIC_PATTERN = Pattern.compile("^(age|gender|location|education|all)$");
    private static final Pattern DATE_PATTERN   = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    private InputSanitizer() {}

    public static void validateId(String id, String field) {
        if (id == null || (!ID_PATTERN.matcher(id).matches() && !UUID_PATTERN.matcher(id).matches()))
            throw new SecurityException("Invalid " + field + " format: " + sanitizeForLog(id));
    }

    public static void validateAssessmentType(String type) {
        if (type == null || !ASSESSMENT_TYPE_PATTERN.matcher(type).matches())
            throw new SecurityException("Invalid assessment_type '" + sanitizeForLog(type)
                + "'. Allowed: gd|interview|match|dojo");
    }

    public static void validateModelVersion(String ver) {
        if (ver == null || !MODEL_VER_PATTERN.matcher(ver).matches())
            throw new SecurityException("Invalid model_version format: " + sanitizeForLog(ver)
                + ". Expected: v1, v2, v1.2, v2.0-beta");
    }

    public static void validateBelt(String belt) {
        if (belt == null || !BELT_PATTERN.matcher(belt).matches())
            throw new SecurityException("Invalid belt '" + sanitizeForLog(belt)
                + "'. Allowed: bronze|silver|gold|platinum|diamond|master");
    }

    public static void validateDomain(String domain) {
        if (domain == null || !DOMAIN_PATTERN.matcher(domain).matches())
            throw new SecurityException("Invalid domain: " + sanitizeForLog(domain));
    }

    public static void validateDimension(String dim) {
        if (dim == null || !DIMENSION_PATTERN.matcher(dim).matches())
            throw new SecurityException("Invalid dimension '" + sanitizeForLog(dim)
                + "'. Allowed: communication|technical|cultural_fit|overall");
    }

    public static void validateDemographic(String demo) {
        if (demo == null || !DEMOGRAPHIC_PATTERN.matcher(demo).matches())
            throw new SecurityException("Invalid demographic_group '" + sanitizeForLog(demo)
                + "'. Allowed: age|gender|location|education|all");
    }

    public static void validateDate(String date, String field) {
        if (date != null && !date.isBlank() && !DATE_PATTERN.matcher(date).matches())
            throw new IllegalArgumentException(field + " must be YYYY-MM-DD, got: " + sanitizeForLog(date));
    }

    public static void validateScore(double score, String field) {
        if (score < 0 || score > 100)
            throw new IllegalArgumentException(field + " must be 0-100, got: " + score);
    }

    public static void validateConfidence(double conf, String field) {
        if (conf < 0.0 || conf > 1.0)
            throw new IllegalArgumentException(field + " must be 0.0-1.0, got: " + conf);
    }

    public static void validateRange(int v, int min, int max, String field) {
        if (v < min || v > max)
            throw new IllegalArgumentException(field + " must be " + min + "-" + max + ", got: " + v);
    }

    public static void validatePositiveDouble(double v, String field) {
        if (v < 0) throw new IllegalArgumentException(field + " must be >= 0, got: " + v);
    }

    public static void requireNonBlank(String v, String field) {
        if (v == null || v.isBlank())
            throw new IllegalArgumentException("Required field '" + field + "' is missing");
    }

    public static void validateEnum(String v, String field, String... allowed) {
        for (String a : allowed) if (a.equalsIgnoreCase(v)) return;
        throw new IllegalArgumentException("Invalid '" + field + "': " + sanitizeForLog(v)
            + ". Allowed: " + Arrays.toString(allowed));
    }

    public static boolean isValidToolName(String name) {
        return name != null && !name.isEmpty() && name.length() <= 100 && name.matches("^[a-z_]+$");
    }

    public static String sanitizeText(String text, int maxLen) {
        if (text == null) return "";
        if (text.length() > maxLen) text = text.substring(0, maxLen);
        return text
            .replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
            .replace("\"", "&quot;").replace("'", "&#39;")
            .replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");
    }

    public static String sanitizeForLog(String v) {
        if (v == null) return "<null>";
        return v.replaceAll("[\\r\\n\\t]", " ").substring(0, Math.min(v.length(), 80));
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// JWT VALIDATOR — HMAC-SHA256, 5-min cache, constant-time comparison
// ═══════════════════════════════════════════════════════════════════════════════
class JwtValState { final long ts; final JwtValidator.Result result; JwtValState(long t, JwtValidator.Result r){ts=t;result=r;} }

public class JwtValidator {
    private final String secret;
    private final ConcurrentHashMap<String, JwtValState> cache = new ConcurrentHashMap<>();
    private static final long TTL = 5 * 60_000L;

    public JwtValidator(ServerConfig c) { secret = c.getJwtSecret(); }

    public Result validate(String token, String requiredRole) {
        if (token == null || token.isBlank()) return Result.fail("Token is null or empty");
        String key = hashToken(token);
        JwtValState cached = cache.get(key);
        if (cached != null && System.currentTimeMillis() - cached.ts < TTL) {
            if (cached.result.valid) {
                long exp = parseLong(cached.result.getClaim("exp"), 0);
                if (exp > 0 && Instant.now().getEpochSecond() > exp) { cache.remove(key); return Result.fail("Token expired (re-check)"); }
            }
            return cached.result;
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) return store(key, Result.fail("Invalid JWT: need 3 parts"));
        Map<String,String> hdr = decodeJson(parts[0]);
        if (!"HS256".equals(hdr.get("alg"))) return store(key, Result.fail("Unsupported alg: " + hdr.get("alg")));
        if (!constantTimeEq(hmac(parts[0]+"."+parts[1], secret), parts[2])) return store(key, Result.fail("Signature invalid"));
        Map<String,String> cl = decodeJson(parts[1]);
        long exp = parseLong(cl.get("exp"), 0);
        if (exp > 0 && Instant.now().getEpochSecond() > exp) return store(key, Result.fail("Token expired"));
        if (cl.getOrDefault("sub","").isBlank()) return store(key, Result.fail("Missing sub claim"));
        if (requiredRole != null && !requiredRole.isBlank() &&
            !cl.getOrDefault("roles","").toUpperCase().contains(requiredRole.toUpperCase()))
            return store(key, Result.fail("Role '" + requiredRole + "' required"));
        return store(key, Result.success(cl));
    }

    private Map<String,String> decodeJson(String b64) {
        try {
            String pad = b64 + "==".substring(0,(4-b64.length()%4)%4);
            String json = new String(Base64.getUrlDecoder().decode(pad), StandardCharsets.UTF_8);
            Map<String,String> m = new LinkedHashMap<>();
            json = json.replaceAll("^\\{|\\}$","");
            for (String p : json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")) {
                String[] kv = p.split(":",2);
                if (kv.length==2) m.put(kv[0].replaceAll("[\"\\s]",""), kv[1].replaceAll("^[\"\\s]+|[\"\\s]+$",""));
            }
            return m;
        } catch (Exception e) { return Map.of(); }
    }
    private String hmac(String data, String key) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            mac.init(new javax.crypto.spec.SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) { throw new RuntimeException("HMAC failed",e); }
    }
    private boolean constantTimeEq(String a, String b) {
        if (a==null||b==null||a.length()!=b.length()) return false;
        int r=0; for(int i=0;i<a.length();i++) r|=a.charAt(i)^b.charAt(i); return r==0;
    }
    private String hashToken(String t) {
        try { byte[] h=java.security.MessageDigest.getInstance("SHA-256").digest(t.getBytes(StandardCharsets.UTF_8)); return Base64.getEncoder().encodeToString(h).substring(0,32); }
        catch (Exception e) { return t.substring(0,Math.min(32,t.length())); }
    }
    private Result store(String k, Result r) { cache.put(k, new JwtValState(System.currentTimeMillis(),r)); return r; }
    private long parseLong(String s, long d) { try{return s==null?d:Long.parseLong(s.trim());}catch(Exception e){return d;} }

    public static final class Result {
        final boolean valid; private final String error; private final Map<String,String> claims;
        private Result(boolean v,String e,Map<String,String> c){valid=v;error=e;claims=c==null?Map.of():Collections.unmodifiableMap(c);}
        static Result success(Map<String,String> c){return new Result(true,null,c);}
        static Result fail(String e){return new Result(false,e,null);}
        public boolean isValid(){return valid;}
        public String getError(){return error;}
        public String getClaim(String k){return claims.getOrDefault(k,"");}
        public String getClaim(String k,String d){return claims.getOrDefault(k,d);}
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// RATE LIMITER — token bucket, per-client key
// ═══════════════════════════════════════════════════════════════════════════════
class Bucket { final AtomicInteger sec=new AtomicInteger(),min=new AtomicInteger(); volatile long sb=System.currentTimeMillis()/1000,mb=System.currentTimeMillis()/60_000; }

public class RateLimiter {
    private final int mSec, mMin;
    private final ConcurrentHashMap<String,Bucket> states = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String,Long> banned = new ConcurrentHashMap<>();

    public RateLimiter(ServerConfig c) { mSec=c.getRateLimitPerSec(); mMin=c.getRateLimitPerMin(); }

    public boolean allow(String key) {
        Long exp = banned.get(key);
        if (exp!=null) { if(exp==0||System.currentTimeMillis()<exp) return false; banned.remove(key); }
        Bucket b = states.computeIfAbsent(key, k->new Bucket());
        long ns=System.currentTimeMillis()/1000, nm=System.currentTimeMillis()/60_000;
        if(b.sb!=ns){b.sec.set(0);b.sb=ns;} if(b.mb!=nm){b.min.set(0);b.mb=nm;}
        return b.sec.incrementAndGet()<=mSec && b.min.incrementAndGet()<=mMin;
    }
    public void ban(String k, int s) { banned.put(k,s==0?0L:System.currentTimeMillis()+s*1000L); }
    public void unban(String k) { banned.remove(k); }
}


// ═══════════════════════════════════════════════════════════════════════════════
// AUDIT LOGGER — SOC 2 / DPDPA 2023 compliant, structured to stderr
// ═══════════════════════════════════════════════════════════════════════════════
public class AuditLogger {
    private static final Logger LOG = Logger.getLogger(AuditLogger.class.getName());
    private static final int BUF = 2000;
    private final boolean enabled;
    private final ConcurrentLinkedDeque<AuditEvent> buf = new ConcurrentLinkedDeque<>();

    public AuditLogger(ServerConfig c) { enabled = c.isAuditEnabled(); }

    public void info(String ev, String actor, String detail)  { log("INFO", ev, actor, detail); }
    public void warn(String ev, String actor, String detail)  { log("WARN", ev, actor, detail); }
    public void error(String ev, String actor, String detail) { log("ERROR",ev, actor, detail); }

    private void log(String lvl, String ev, String actor, String detail) {
        if (!enabled) return;
        String msg = String.format("[AUDIT] %s | %s | actor=%s | %s | ts=%s",
            lvl, InputSanitizer.sanitizeForLog(ev),
            InputSanitizer.sanitizeForLog(actor),
            InputSanitizer.sanitizeForLog(detail), Instant.now());
        System.err.println(msg);
        buf.addLast(new AuditEvent(lvl, ev, actor, detail, Instant.now().toString()));
        while (buf.size() > BUF) buf.pollFirst();
    }

    public List<AuditEvent> query(String filter, int limit) {
        List<AuditEvent> r = new ArrayList<>();
        for (AuditEvent e : new ArrayDeque<>(buf)) {
            if ("ALL".equals(filter) || e.eventType().equalsIgnoreCase(filter)) {
                r.add(e); if (r.size() >= limit) break;
            }
        }
        return r;
    }

    public record AuditEvent(String level, String eventType, String actor, String detail, String timestamp) {}
}


// ═══════════════════════════════════════════════════════════════════════════════
// REQUEST VALIDATOR
// ═══════════════════════════════════════════════════════════════════════════════
public class RequestValidator {
    private static final int MAX = 65536;

    public boolean isValidToolName(String n) { return InputSanitizer.isValidToolName(n); }

    public List<String> validate(String tool, JsonNode args) {
        List<String> errors = new ArrayList<>();
        if (args == null) return errors;
        String raw = args.toString();
        if (raw.length() > MAX) errors.add("Payload (" + raw.length() + " bytes) > 64KB limit");
        if (raw.contains("\u0000")) errors.add("Payload contains null bytes");
        return errors;
    }
}
