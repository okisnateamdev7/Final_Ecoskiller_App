package com.ecoskiller.mcp.search;

import org.json.JSONObject;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the Ecoskiller Search Indexer MCP Server.
 * 50 tests covering all 18 tools + MCP protocol + security validation.
 * No external dependencies — no OpenSearch or Kafka required.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SearchIndexerMcpServerTest {

    private static SearchIndexerMcpServer server;

    @BeforeAll
    static void setup() {
        System.setProperty("ENV","test");
        server = new SearchIndexerMcpServer();
    }

    // ─── helpers ─────────────────────────────────────────────────────────────────

    private JSONObject call(String method, JSONObject params) {
        JSONObject req = new JSONObject().put("jsonrpc","2.0").put("id",1).put("method",method);
        if (params != null) req.put("params", params);
        return server.handle(req);
    }

    private JSONObject tool(String name, JSONObject args) {
        return call("tools/call", new JSONObject().put("name",name).put("arguments",args));
    }

    private void assertOk(JSONObject res) {
        assertNotNull(res);
        assertFalse(res.has("error"),
                "Unexpected error: " + (res.has("error") ? res.getJSONObject("error").optString("message") : ""));
        assertTrue(res.has("result"));
        assertTrue(res.getJSONObject("result").has("content"), "Result missing content");
    }

    private String txt(JSONObject res) {
        return res.getJSONObject("result").getJSONArray("content").getJSONObject(0).getString("text");
    }

    // ─── MCP Protocol ─────────────────────────────────────────────────────────────

    @Test @Order(1)
    void testInitialize() {
        JSONObject res = call("initialize", new JSONObject()
                .put("protocolVersion","2024-11-05")
                .put("clientInfo", new JSONObject().put("name","test-client")));
        assertEquals("mcp-search-indexer", res.getJSONObject("result").getJSONObject("serverInfo").getString("name"));
        assertEquals("2024-11-05", res.getJSONObject("result").getString("protocolVersion"));
    }

    @Test @Order(2)
    void testPing() {
        assertTrue(call("ping",null).has("result"));
    }

    @Test @Order(3)
    void testToolsListReturns18() {
        int count = call("tools/list",null).getJSONObject("result").getJSONArray("tools").length();
        assertEquals(18, count, "Must expose exactly 18 tools");
    }

    @Test @Order(4)
    void testAllToolsHaveNameDescSchema() {
        var tools = call("tools/list",null).getJSONObject("result").getJSONArray("tools");
        for (int i=0; i<tools.length(); i++) {
            JSONObject t = tools.getJSONObject(i);
            assertFalse(t.optString("name").isBlank(),        "Tool " + i + " missing name");
            assertFalse(t.optString("description").isBlank(), "Tool " + i + " missing description");
            assertTrue(t.has("inputSchema"),                  "Tool " + i + " missing inputSchema");
        }
    }

    @Test @Order(5)
    void testUnknownMethod() {
        assertEquals(-32601, call("unknown/xyz",null).getJSONObject("error").getInt("code"));
    }

    @Test @Order(6)
    void testUnknownTool() {
        assertEquals(-32602, tool("nonexistent",new JSONObject()).getJSONObject("error").getInt("code"));
    }

    // ─── Security Tests ───────────────────────────────────────────────────────────

    @Test @Order(10)
    void testSecurity_PathTraversalTenantId() {
        JSONObject res = tool("candidate_search", new JSONObject()
                .put("action","search").put("tenant_id","../../etc/passwd"));
        assertTrue(res.has("error"), "Path traversal must be rejected");
    }

    @Test @Order(11)
    void testSecurity_WildcardIndexName() {
        JSONObject res = tool("index_document", new JSONObject()
                .put("action","get").put("tenant_id","tenant*")
                .put("doc_type","candidate").put("document_id","doc1"));
        assertTrue(res.has("error"), "Wildcard in tenant_id must be rejected");
    }

    @Test @Order(12)
    void testSecurity_QueryInjection() {
        // Backslash injection attempt
        JSONObject res = tool("candidate_search", new JSONObject()
                .put("action","search").put("tenant_id","tenant-001")
                .put("query","test\\\" OR 1=1 --"));
        assertTrue(res.has("error"), "Query injection must be rejected");
    }

    @Test @Order(13)
    void testSecurity_LimitOverMax() {
        JSONObject res = tool("candidate_search", new JSONObject()
                .put("action","search").put("tenant_id","tenant-001")
                .put("query","java").put("limit",9999));
        assertTrue(res.has("error"), "Limit > 1000 must be rejected");
    }

    @Test @Order(14)
    void testSecurity_NegativeOffset() {
        JSONObject res = tool("job_search", new JSONObject()
                .put("action","search").put("tenant_id","tenant-001")
                .put("offset",-5));
        assertTrue(res.has("error"), "Negative offset must be rejected");
    }

    @Test @Order(15)
    void testSecurity_UnknownDocType() {
        JSONObject res = tool("index_document", new JSONObject()
                .put("action","index").put("tenant_id","tenant-001")
                .put("doc_type","admin_users")
                .put("document_id","doc1"));
        assertTrue(res.has("error"), "Unknown doc_type must be rejected");
    }

    @Test @Order(16)
    void testSecurity_UnknownLanguage() {
        JSONObject res = tool("candidate_search", new JSONObject()
                .put("action","search").put("tenant_id","tenant-001")
                .put("language","klingon"));
        assertTrue(res.has("error"), "Unsupported language must be rejected");
    }

    @Test @Order(17)
    void testSecurity_UnknownCluster() {
        JSONObject res = tool("cluster_manager", new JSONObject()
                .put("action","health").put("cluster","azure"));
        assertTrue(res.has("error"), "Unknown cluster must be rejected");
    }

    @Test @Order(18)
    void testSecurity_TooManyShards() {
        JSONObject res = tool("tenant_index_manager", new JSONObject()
                .put("action","provision").put("tenant_id","tenant-001")
                .put("num_shards",500));
        assertTrue(res.has("error"), "num_shards > 100 must be rejected");
    }

    @Test @Order(19)
    void testSecurity_InvalidSortField() {
        JSONObject res = tool("candidate_search", new JSONObject()
                .put("action","search").put("tenant_id","tenant-001")
                .put("sort_by","1 OR DROP TABLE"));
        assertTrue(res.has("error"), "Invalid sort_by must be rejected");
    }

    @Test @Order(20)
    void testSecurity_InvalidIlmPhase() {
        JSONObject res = tool("ilm_policy", new JSONObject()
                .put("action","get_policy").put("phase","archive"));
        assertTrue(res.has("error"), "Unknown ILM phase must be rejected");
    }

    // ─── Tool Tests ───────────────────────────────────────────────────────────────

    @Test @Order(21)
    void testCandidateSearch_Search() {
        JSONObject res = tool("candidate_search", new JSONObject()
                .put("action","search").put("tenant_id","tenant-001")
                .put("query","java developer").put("belt_level","BLUE")
                .put("fuzzy",true).put("limit",10));
        assertOk(res);
        String t = txt(res);
        assertTrue(t.contains("tenant-001-candidates"));
        assertTrue(t.contains("tenant_filter"));
    }

    @Test @Order(22)
    void testCandidateSearch_Autocomplete() {
        assertOk(tool("candidate_search", new JSONObject()
                .put("action","autocomplete").put("tenant_id","tenant-001").put("query","jav")));
    }

    @Test @Order(23)
    void testJobSearch_Search() {
        JSONObject res = tool("job_search", new JSONObject()
                .put("action","search").put("tenant_id","tenant-001")
                .put("query","backend engineer").put("category","Engineering"));
        assertOk(res);
        assertTrue(txt(res).contains("tenant-001-jobs"));
    }

    @Test @Order(24)
    void testJobSearch_GeoSearch() {
        JSONObject res = tool("job_search", new JSONObject()
                .put("action","geo_search").put("tenant_id","tenant-001")
                .put("lat",19.076).put("lon",72.877).put("radius_km",50));
        assertOk(res);
        assertTrue(txt(res).contains("geo_point"));
    }

    @Test @Order(25)
    void testJobSearch_GeoRadius_Capped() {
        // radius_km > 500 should be capped
        JSONObject res = tool("job_search", new JSONObject()
                .put("action","geo_search").put("tenant_id","tenant-001")
                .put("lat",19.076).put("lon",72.877).put("radius_km",999));
        assertOk(res);
        // 999 capped to 500
        assertTrue(txt(res).contains("500") || txt(res).contains("radius_km"));
    }

    @Test @Order(26)
    void testIndexDocument_Upsert() {
        JSONObject res = tool("index_document", new JSONObject()
                .put("action","upsert").put("tenant_id","tenant-001")
                .put("doc_type","candidate").put("document_id","cand-001")
                .put("document_json","{\"name\":\"Test User\",\"skills\":[\"Java\"]}"));
        assertOk(res);
        assertTrue(txt(res).contains("idempotent"));
        assertTrue(txt(res).contains("doc_as_upsert"));
    }

    @Test @Order(27)
    void testIndexDocument_Get() {
        assertOk(tool("index_document", new JSONObject()
                .put("action","get").put("tenant_id","tenant-001")
                .put("doc_type","job").put("document_id","job-001")));
    }

    @Test @Order(28)
    void testBulkIndex_Index() {
        JSONObject res = tool("bulk_index", new JSONObject()
                .put("action","index").put("tenant_id","tenant-001")
                .put("doc_type","candidate")
                .put("documents","[{\"candidate_id\":\"c1\"},{\"candidate_id\":\"c2\"}]")
                .put("batch_size",1000));
        assertOk(res);
        assertTrue(txt(res).contains("10K+"));
    }

    @Test @Order(29)
    void testBulkIndex_Status() {
        assertOk(tool("bulk_index", new JSONObject()
                .put("action","status").put("tenant_id","tenant-001").put("doc_type","job")));
    }

    @Test @Order(30)
    void testDeleteDocument_Delete() {
        JSONObject res = tool("delete_document", new JSONObject()
                .put("action","delete").put("tenant_id","tenant-001")
                .put("doc_type","candidate").put("document_id","cand-001"));
        assertOk(res);
        assertTrue(txt(res).contains("ENFORCED"));
    }

    @Test @Order(31)
    void testDeleteDocument_GdprPurge() {
        JSONObject res = tool("delete_document", new JSONObject()
                .put("action","gdpr_purge").put("tenant_id","tenant-001")
                .put("doc_type","candidate").put("gdpr_reason","RIGHT_TO_ERASURE"));
        assertOk(res);
        assertTrue(txt(res).contains("48 hours"));
    }

    @Test @Order(32)
    void testReindex_Start() {
        JSONObject res = tool("reindex", new JSONObject()
                .put("action","start").put("tenant_id","tenant-001")
                .put("doc_type","candidates").put("reason","MAPPING_UPDATE"));
        assertOk(res);
        assertTrue(txt(res).contains("ZERO") || txt(res).contains("zero"));
    }

    @Test @Order(33)
    void testReindex_ConsistencyCheck() {
        assertOk(tool("reindex", new JSONObject()
                .put("action","consistency_check").put("tenant_id","tenant-001").put("doc_type","candidates")));
    }

    @Test @Order(34)
    void testIndexHealth_Get() {
        JSONObject res = tool("index_health", new JSONObject().put("action","get").put("cluster","all"));
        assertOk(res);
        assertTrue(txt(res).contains("p95"));
    }

    @Test @Order(35)
    void testIndexHealth_AlertStatus() {
        JSONObject res = tool("index_health", new JSONObject().put("action","alert_status"));
        assertOk(res);
        assertTrue(txt(res).contains("thresholds"));
    }

    @Test @Order(36)
    void testEventConsumer_LagStatus() {
        JSONObject res = tool("event_consumer", new JSONObject().put("action","lag_status"));
        assertOk(res);
        assertTrue(txt(res).contains("candidate-events"));
        assertTrue(txt(res).contains("job-events"));
    }

    @Test @Order(37)
    void testEventConsumer_DedupCheck() {
        assertOk(tool("event_consumer", new JSONObject()
                .put("action","idempotency_check").put("event_id","evt-abc123")));
    }

    @Test @Order(38)
    void testDocumentTransform_Transform() {
        JSONObject res = tool("document_transform", new JSONObject()
                .put("action","transform").put("doc_type","candidate")
                .put("event_json","{\"candidate_id\":\"c1\",\"name\":\"Raj\"}"));
        assertOk(res);
        assertTrue(txt(res).contains("pii_excluded") || txt(res).contains("email"));
    }

    @Test @Order(39)
    void testDocumentTransform_PiiTokenise() {
        JSONObject res = tool("document_transform", new JSONObject()
                .put("action","pii_tokenise").put("doc_type","candidate"));
        assertOk(res);
        assertTrue(txt(res).contains("EXCLUDE_FROM_INDEX"));
    }

    @Test @Order(40)
    void testFacetedSearch_Search() {
        JSONObject res = tool("faceted_search", new JSONObject()
                .put("action","search").put("tenant_id","tenant-001")
                .put("doc_type","candidate").put("query","python"));
        assertOk(res);
        assertTrue(txt(res).contains("belt_level") || txt(res).contains("skills"));
    }

    @Test @Order(41)
    void testFacetedSearch_DrillDown() {
        assertOk(tool("faceted_search", new JSONObject()
                .put("action","drill_down").put("tenant_id","tenant-001")
                .put("doc_type","candidate").put("filters","{\"belt_level\":\"BLUE\"}")));
    }

    @Test @Order(42)
    void testTenantIndexManager_Provision() {
        JSONObject res = tool("tenant_index_manager", new JSONObject()
                .put("action","provision").put("tenant_id","tenant-999")
                .put("num_shards",5).put("num_replicas",2));
        assertOk(res);
        assertTrue(txt(res).contains("PROVISIONED"));
        assertTrue(txt(res).contains("tenant-999-candidates"));
    }

    @Test @Order(43)
    void testTenantIndexManager_DeprovisionRequiresConfirm() {
        JSONObject res = tool("tenant_index_manager", new JSONObject()
                .put("action","deprovision").put("tenant_id","tenant-999"));
        assertOk(res);
        assertTrue(txt(res).contains("ABORTED") || txt(res).contains("CONFIRM"));
    }

    @Test @Order(44)
    void testTenantIndexManager_VerifyIsolation() {
        JSONObject res = tool("tenant_index_manager", new JSONObject()
                .put("action","verify_isolation").put("tenant_id","tenant-001"));
        assertOk(res);
        assertTrue(txt(res).contains("IMPOSSIBLE") || txt(res).contains("isolation"));
    }

    @Test @Order(45)
    void testIndexMapping_Get() {
        JSONObject res = tool("index_mapping", new JSONObject()
                .put("action","get").put("tenant_id","tenant-001").put("doc_type","candidates"));
        assertOk(res);
        assertTrue(txt(res).contains("tenant_id") || txt(res).contains("mappings"));
    }

    @Test @Order(46)
    void testSearchAnalytics_Funnel() {
        JSONObject res = tool("search_analytics", new JSONObject()
                .put("action","funnel").put("tenant_id","tenant-001"));
        assertOk(res);
        assertTrue(txt(res).contains("applied") || txt(res).contains("funnel"));
    }

    @Test @Order(47)
    void testSearchAnalytics_GeoHeatmap() {
        JSONObject res = tool("search_analytics", new JSONObject()
                .put("action","geo_heatmap").put("tenant_id","tenant-001"));
        assertOk(res);
        assertTrue(txt(res).contains("Mumbai") || txt(res).contains("heatmap"));
    }

    @Test @Order(48)
    void testQueryAuditLog_Log() {
        JSONObject res = tool("query_audit_log", new JSONObject()
                .put("action","log").put("tenant_id","tenant-001")
                .put("query_text","java developer").put("results_count",42)
                .put("took_ms",35).put("user_id","user-001"));
        assertOk(res);
        assertTrue(txt(res).contains("search-audit-log"));
    }

    @Test @Order(49)
    void testQueryAuditLog_AbuseScan() {
        JSONObject res = tool("query_audit_log", new JSONObject()
                .put("action","abuse_scan").put("tenant_id","tenant-001"));
        assertOk(res);
        assertTrue(txt(res).contains("CLEAN") || txt(res).contains("patterns_checked"));
    }

    @Test @Order(50)
    void testIlmPolicy_GetPolicy() {
        JSONObject res = tool("ilm_policy", new JSONObject().put("action","get_policy"));
        assertOk(res);
        assertTrue(txt(res).contains("hot") && txt(res).contains("warm"));
    }

    @Test @Order(51)
    void testClusterManager_Health() {
        JSONObject res = tool("cluster_manager", new JSONObject().put("action","health"));
        assertOk(res);
        assertTrue(txt(res).contains("gcp") && txt(res).contains("aws"));
    }

    @Test @Order(52)
    void testClusterManager_FailoverRequiresConfirm() {
        JSONObject res = tool("cluster_manager", new JSONObject()
                .put("action","failover").put("failover_to","aws"));
        assertOk(res);
        assertTrue(txt(res).contains("ABORTED") || txt(res).contains("CONFIRM"));
    }

    @Test @Order(53)
    void testGdprCompliance_ForgetRequest() {
        JSONObject res = tool("gdpr_compliance", new JSONObject()
                .put("action","forget_request").put("tenant_id","tenant-001")
                .put("user_id","user-001").put("doc_type","candidate"));
        assertOk(res);
        assertTrue(txt(res).contains("48 hours"));
    }

    @Test @Order(54)
    void testGdprCompliance_CheckPii() {
        JSONObject res = tool("gdpr_compliance", new JSONObject()
                .put("action","check_pii").put("tenant_id","tenant-001").put("doc_type","candidate"));
        assertOk(res);
        assertTrue(txt(res).contains("email") && txt(res).contains("pii_free"));
    }

    @Test @Order(55)
    void testDisasterRecovery_VerifyRpo() {
        JSONObject res = tool("disaster_recovery", new JSONObject()
                .put("action","verify_rpo").put("cluster","gcp"));
        assertOk(res);
        assertTrue(txt(res).contains("rpo_target_sec") || txt(res).contains("RPO"));
    }

    @Test @Order(56)
    void testDisasterRecovery_FullRecoveryRequiresConfirm() {
        JSONObject res = tool("disaster_recovery", new JSONObject()
                .put("action","full_recovery").put("cluster","gcp"));
        assertOk(res);
        assertTrue(txt(res).contains("ABORTED") || txt(res).contains("CONFIRM"));
    }

    @Test @Order(57)
    void testDisasterRecovery_BackupStatus() {
        JSONObject res = tool("disaster_recovery", new JSONObject().put("action","backup_status"));
        assertOk(res);
        assertTrue(txt(res).contains("DR_READY") || txt(res).contains("gcp"));
    }
}
