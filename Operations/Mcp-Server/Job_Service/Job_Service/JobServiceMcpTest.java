package com.ecoskiller.mcp;

import com.ecoskiller.mcp.json.JsonValue;
import com.ecoskiller.mcp.json.JsonValue.*;
import com.ecoskiller.mcp.security.SecurityManager;
import com.ecoskiller.mcp.tools.ToolRegistry;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * JobServiceMcpTest — end-to-end test suite (no JUnit needed).
 * Run via: java -cp target/... com.ecoskiller.mcp.JobServiceMcpTest
 */
public class JobServiceMcpTest {

    private static int pass = 0, fail = 0;

    // ── Build a mock Keycloak JWT (structural only — no real signature) ───
    static String jwt(String sub, String role, String domain) {
        String header  = b64("{\"alg\":\"RS256\",\"typ\":\"JWT\"}");
        long   exp     = System.currentTimeMillis() / 1000 + 3600;
        String payload = b64("{\"sub\":\"" + sub + "\",\"realm\":\"ecoskiller\"," +
                             "\"roles\":\"" + role + "\",\"domain\":\"" + domain + "\"," +
                             "\"exp\":" + exp + "}");
        String sig     = b64("mock-sig");
        return header + "." + payload + "." + sig;
    }

    static String b64(String s) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    // ─────────────────────────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("  Ecoskiller Job Service MCP — Test Suite  v1.0.0");
        System.out.println("═══════════════════════════════════════════════════════════\n");

        SecurityManager sec = new SecurityManager();
        ToolRegistry    reg = new ToolRegistry(sec);

        String admin  = jwt("admin-001",     "admin",     "Technology");
        String rec    = jwt("rec-001",        "recruiter", "Technology");
        String cand   = jwt("cand-001",       "candidate", "Technology");
        String svc    = jwt("svc-001",        "service",   "Technology");

        // ── 1. job_schema ──────────────────────────────────────────────
        test("job_schema returns schema + state machine", () -> {
            JsonObject r = reg.call("job_schema", args(cand));
            assertTrue(r.getBoolean("success"), "success=true");
            assertHas(r, "schema");
            assertHas(r, "status_machine");
        });

        // ── 2. job_health ──────────────────────────────────────────────
        test("job_health returns healthy", () -> {
            JsonObject r = reg.call("job_health", args(svc));
            assertEquals("healthy", r.getString("status"), "status=healthy");
        });

        // ── 3. job_salary_band_validate — valid ────────────────────────
        test("salary_band_validate — Technology valid", () -> {
            JsonObject a = args(admin)
                .put("domain", "Technology")
                .put("salary_min", 50000)
                .put("salary_max", 200000);
            JsonObject r = reg.call("job_salary_band_validate", a);
            assertTrue(r.getBoolean("valid"), "valid=true");
        });

        // ── 4. job_salary_band_validate — invalid ──────────────────────
        test("salary_band_validate — too low → invalid", () -> {
            JsonObject a = args(admin)
                .put("domain", "Technology")
                .put("salary_min", 1000)
                .put("salary_max", 5000);
            JsonObject r = reg.call("job_salary_band_validate", a);
            assertFalse(r.getBoolean("valid"), "valid=false");
        });

        // ── 5. job_create ──────────────────────────────────────────────
        final String[] jobId = {null};
        test("job_create — DRAFT status + kafka_event", () -> {
            JsonObject a = args(rec)
                .put("title",        "Senior Java Engineer")
                .put("domain",       "Technology")
                .put("description",  "Build microservices on GCP + AWS")
                .put("tenant_id",    "tenant-001")
                .put("recruiter_id", "rec-001")
                .put("salary_min",   80000)
                .put("salary_max",   200000)
                .put("location",     "Remote");
            JsonObject r = reg.call("job_create", a);
            assertTrue(r.getBoolean("success"), "success=true");
            assertEquals("DRAFT", r.getString("status"), "status=DRAFT");
            assertHas(r, "kafka_event");
            assertHas(r, "job_id");
            jobId[0] = r.getString("job_id");
        });

        // ── 6. job_get ─────────────────────────────────────────────────
        test("job_get — retrieves job", () -> {
            JsonObject r = reg.call("job_get", args(cand)
                .put("job_id",    jobId[0])
                .put("tenant_id", "tenant-001")
                .put("domain",    "Technology"));
            assertTrue(r.getBoolean("success"), "success=true");
            assertEquals("Senior Java Engineer", r.getObject("job").getString("title"), "title matches");
        });

        // ── 7. domain isolation ────────────────────────────────────────
        test("job_get — wrong domain → not found (domain isolation)", () -> {
            JsonObject r = reg.call("job_get", args(cand)
                .put("job_id",    jobId[0])
                .put("tenant_id", "tenant-001")
                .put("domain",    "Arts")); // wrong domain
            assertFalse(r.getBoolean("success"), "success=false");
        });

        // ── 8. job_update with CAS ─────────────────────────────────────
        test("job_update — updates title, version increments", () -> {
            JsonObject r = reg.call("job_update", args(rec)
                .put("job_id",    jobId[0])
                .put("tenant_id", "tenant-001")
                .put("version",   1)
                .put("title",     "Lead Java Engineer — Ecoskiller Platform"));
            assertTrue(r.getBoolean("success"), "success=true");
            assertEquals(2, r.getInt("new_version"), "version=2");
        });

        // ── 9. CAS conflict ────────────────────────────────────────────
        test("job_update — stale version → CAS conflict", () -> {
            JsonObject r = reg.call("job_update", args(rec)
                .put("job_id",    jobId[0])
                .put("tenant_id", "tenant-001")
                .put("version",   1)  // stale
                .put("title",     "Conflict"));
            assertFalse(r.getBoolean("success"), "success=false");
            assertHas(r, "current_version");
        });

        // ── 10. status transition DRAFT → MODERATION_REQUIRED ─────────
        test("job_status_transition DRAFT → MODERATION_REQUIRED", () -> {
            JsonObject r = reg.call("job_status_transition", args(rec)
                .put("job_id",        jobId[0])
                .put("tenant_id",     "tenant-001")
                .put("target_status", "MODERATION_REQUIRED"));
            assertTrue(r.getBoolean("success"), "success=true");
            assertEquals("MODERATION_REQUIRED", r.getString("new_status"), "new_status matches");
        });

        // ── 11. invalid transition ─────────────────────────────────────
        test("job_status_transition — invalid transition throws", () -> {
            try {
                reg.call("job_status_transition", args(rec)
                    .put("job_id",        jobId[0])
                    .put("tenant_id",     "tenant-001")
                    .put("target_status", "ARCHIVED")); // invalid from MODERATION_REQUIRED
                assertFail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                assertContains(e.getMessage(), "Invalid state transition", "correct error");
            }
        });

        // ── 12. job_moderate — APPROVE ────────────────────────────────
        test("job_moderate — admin approves → PUBLISHED", () -> {
            JsonObject r = reg.call("job_moderate", args(admin)
                .put("job_id",      jobId[0])
                .put("action",      "APPROVE")
                .put("moderator_id","admin-001"));
            assertTrue(r.getBoolean("success"), "success=true");
            assertEquals("PUBLISHED", r.getString("new_status"), "new_status=PUBLISHED");
        });

        // ── 13. job_list ───────────────────────────────────────────────
        test("job_list — finds published jobs", () -> {
            JsonObject r = reg.call("job_list", args(cand)
                .put("tenant_id", "tenant-001")
                .put("domain",    "Technology")
                .put("status",    "PUBLISHED"));
            assertTrue(r.getBoolean("success"), "success=true");
            assertTrue(r.getLong("total") >= 1, "total >= 1");
        });

        // ── 14. job_search_fulltext ────────────────────────────────────
        test("job_search_fulltext — finds 'Java' in published", () -> {
            JsonObject r = reg.call("job_search_fulltext", args(cand)
                .put("query",     "Java")
                .put("tenant_id", "tenant-001")
                .put("domain",    "Technology"));
            assertTrue(r.getBoolean("success"), "success=true");
            assertTrue(r.getLong("total") >= 1, "total >= 1");
        });

        // ── 15. job_seo_generate ───────────────────────────────────────
        test("job_seo_generate — returns canonical_url + og tags", () -> {
            JsonObject r = reg.call("job_seo_generate", args(rec)
                .put("job_id",    jobId[0])
                .put("tenant_id", "tenant-001"));
            assertTrue(r.getBoolean("success"), "success=true");
            assertHas(r.getObject("seo"), "canonical_url");
            assertHas(r.getObject("seo"), "og_title");
        });

        // ── 16. job_visibility_update ──────────────────────────────────
        test("job_visibility_update — sets TENANT_ONLY", () -> {
            JsonObject r = reg.call("job_visibility_update", args(rec)
                .put("job_id",    jobId[0])
                .put("tenant_id", "tenant-001")
                .put("visibility","TENANT_ONLY"));
            assertTrue(r.getBoolean("success"), "success=true");
            assertEquals("TENANT_ONLY", r.getString("new_visibility"), "new_visibility correct");
        });

        // ── 17. job_bulk_import — success ─────────────────────────────
        test("job_bulk_import — 3 jobs imported atomically", () -> {
            JsonArray jobs = JsonValue.array();
            for (int i = 1; i <= 3; i++) {
                jobs.add(JsonValue.object()
                    .put("title",        "Bulk Job " + i)
                    .put("description",  "Bulk imported")
                    .put("recruiter_id", "rec-001"));
            }
            JsonObject r = reg.call("job_bulk_import", args(rec)
                .put("tenant_id", "tenant-001")
                .put("domain",    "Technology")
                .set("jobs",      jobs));
            assertTrue(r.getBoolean("success"), "success=true");
            assertEquals(3, r.getInt("imported_count"), "imported_count=3");
        });

        // ── 18. job_bulk_import — atomic rollback ─────────────────────
        test("job_bulk_import — missing title → atomic rollback", () -> {
            JsonArray jobs = JsonValue.array();
            jobs.add(JsonValue.object().put("title", "Good Job"));
            jobs.add(JsonValue.object()); // missing title
            JsonObject r = reg.call("job_bulk_import", args(rec)
                .put("tenant_id", "tenant-001")
                .put("domain",    "Technology")
                .set("jobs",      jobs));
            assertFalse(r.getBoolean("success"), "success=false (rollback)");
            assertHas(r, "validation_errors");
        });

        // ── 19. job_audit_trail ────────────────────────────────────────
        test("job_audit_trail — shows ≥ 3 entries", () -> {
            JsonObject r = reg.call("job_audit_trail", args(admin)
                .put("job_id",    jobId[0])
                .put("tenant_id", "tenant-001"));
            assertTrue(r.getBoolean("success"), "success=true");
            assertTrue(r.getLong("total") >= 3, "at least 3 audit entries");
        });

        // ── 20. job_domain_stats ───────────────────────────────────────
        test("job_domain_stats — returns domain breakdown", () -> {
            JsonObject r = reg.call("job_domain_stats", args(admin)
                .put("tenant_id", "tenant-001"));
            assertTrue(r.getBoolean("success"), "success=true");
            assertHas(r, "domains");
        });

        // ── 21. RBAC — candidate blocked from admin tool ───────────────
        // Note: validateToken() is called in the MCP server layer (handleToolCall),
        // before registry.call(). We test it directly here to prove RBAC enforcement.
        test("RBAC — candidate cannot call job_moderate (admin only)", () -> {
            try {
                sec.validateToken(cand, "job_moderate"); // candidate JWT + admin-only tool
                assertFail("Expected SecurityException");
            } catch (SecurityException e) {
                assertContains(e.getMessage(), "Access denied", "access denied error");
            }
        });

        // ── 22. SQL injection blocked ──────────────────────────────────
        test("Security — SQL injection blocked in title", () -> {
            try {
                reg.call("job_create", args(rec)
                    .put("title",        "'; DROP TABLE jobs;--")
                    .put("domain",       "Technology")
                    .put("description",  "Desc")
                    .put("tenant_id",    "tenant-001")
                    .put("recruiter_id", "rec-001"));
                assertFail("Expected SecurityException");
            } catch (SecurityException e) {
                assertContains(e.getMessage(), "Injection", "injection blocked");
            }
        });

        // ── Results ────────────────────────────────────────────────────
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.printf("  Results: %d passed  /  %d failed  /  %d total%n", pass, fail, pass + fail);
        System.out.println("═══════════════════════════════════════════════════════════");
        if (fail > 0) System.exit(1);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────

    static JsonObject args(String token) {
        return JsonValue.object().put("bearer_token", token);
    }

    static void test(String name, Runnable fn) {
        System.out.printf("  %-63s", name + " ...");
        try { fn.run(); System.out.println(" ✅ PASS"); pass++; }
        catch (Throwable t) { System.out.println(" ❌ FAIL — " + t.getMessage()); fail++; }
    }

    static void assertTrue(boolean v, String msg)  { if (!v)  throw new AssertionError("Expected true: "  + msg); }
    static void assertFalse(boolean v, String msg) { if (v)   throw new AssertionError("Expected false: " + msg); }
    static void assertHas(JsonObject o, String k)  { if (o == null || !o.has(k)) throw new AssertionError("Missing field: " + k); }
    static void assertEquals(Object exp, Object act, String msg) {
        if (!String.valueOf(exp).equals(String.valueOf(act)))
            throw new AssertionError(msg + " — expected=" + exp + " actual=" + act);
    }
    static void assertContains(String s, String sub, String msg) {
        if (s == null || !s.toLowerCase().contains(sub.toLowerCase()))
            throw new AssertionError(msg + " — expected '" + sub + "' in: " + s);
    }
    static void assertFail(String msg) { throw new AssertionError(msg); }
}
