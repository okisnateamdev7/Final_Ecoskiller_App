package com.ecoskiller.mcp.royalty;

import org.json.JSONObject;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for MCP-17 Royalty Engine Server.
 * 45 tests covering all 18 tools + MCP protocol + security validation.
 * No external dependencies — runs without a database or Redis.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoyaltyEngineMcpServerTest {

    private static RoyaltyEngineMcpServer server;

    @BeforeAll
    static void setup() {
        System.setProperty("ENV","test");
        System.setProperty("AUDIT_LOG_PATH","/tmp/mcp17-test-audit.log");
        server = new RoyaltyEngineMcpServer();
    }

    // ─── helpers ─────────────────────────────────────────────────────────────────

    private JSONObject call(String method, JSONObject params) {
        JSONObject req = new JSONObject()
                .put("jsonrpc","2.0").put("id",1).put("method",method);
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
        assertTrue(res.getJSONObject("result").has("content"),
                "Result missing content array");
    }

    private String txt(JSONObject res) {
        return res.getJSONObject("result").getJSONArray("content")
                .getJSONObject(0).getString("text");
    }

    // ─── MCP Protocol ─────────────────────────────────────────────────────────────

    @Test @Order(1)
    void testInitialize() {
        JSONObject res = call("initialize", new JSONObject()
                .put("protocolVersion","2024-11-05")
                .put("clientInfo", new JSONObject().put("name","test-client")));
        assertEquals("mcp-17-royalty", res.getJSONObject("result").getJSONObject("serverInfo").getString("name"));
        assertEquals("2024-11-05",     res.getJSONObject("result").getString("protocolVersion"));
    }

    @Test @Order(2)
    void testPing() {
        JSONObject res = call("ping", null);
        assertTrue(res.has("result"));
    }

    @Test @Order(3)
    void testToolsListReturns18() {
        JSONObject res   = call("tools/list", null);
        int count = res.getJSONObject("result").getJSONArray("tools").length();
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
    void testUnknownMethodReturnsError() {
        JSONObject res = call("unknown/xyz", null);
        assertEquals(-32601, res.getJSONObject("error").getInt("code"));
    }

    @Test @Order(6)
    void testUnknownToolReturnsError() {
        JSONObject res = tool("nonexistent_tool", new JSONObject());
        assertEquals(-32602, res.getJSONObject("error").getInt("code"));
    }

    // ─── Security Validation ──────────────────────────────────────────────────────

    @Test @Order(10)
    void testSecurity_PathTraversalCreatorId() {
        JSONObject res = tool("tax_compliance", new JSONObject()
                .put("action","calculate").put("creator_id","../../etc/passwd"));
        assertTrue(res.has("error"), "Path traversal in creator_id must be rejected");
    }

    @Test @Order(11)
    void testSecurity_NegativeAmount() {
        JSONObject res = tool("royalty_calculation", new JSONObject()
                .put("action","flat_rate")
                .put("creator_id","creator-001")
                .put("sales_amount",-50000));
        assertTrue(res.has("error"), "Negative amount must be rejected");
    }

    @Test @Order(12)
    void testSecurity_InvalidRoyaltyRate() {
        JSONObject res = tool("royalty_calculation", new JSONObject()
                .put("action","flat_rate")
                .put("creator_id","creator-001")
                .put("sales_amount",100000)
                .put("royalty_rate",2.5)); // > 1.0
        assertTrue(res.has("error"), "royalty_rate > 1.0 must be rejected");
    }

    @Test @Order(13)
    void testSecurity_InvalidJurisdiction() {
        JSONObject res = tool("tax_compliance", new JSONObject()
                .put("action","calculate")
                .put("creator_id","creator-001")
                .put("jurisdiction","NARNIA"));
        assertTrue(res.has("error"), "Unknown jurisdiction must be rejected");
    }

    @Test @Order(14)
    void testSecurity_InvalidPayoutMethod() {
        JSONObject res = tool("royalty_distribution", new JSONObject()
                .put("action","initiate")
                .put("creator_id","creator-001")
                .put("payout_method","BITCOIN_ATM"));
        assertTrue(res.has("error"), "Unknown payout_method must be rejected");
    }

    @Test @Order(15)
    void testSecurity_XSSInDescription() {
        JSONObject res = tool("contract_lifecycle", new JSONObject()
                .put("action","get")
                .put("agreement_id","agr-001")
                .put("description","<script>alert('xss')</script>"));
        // < character triggers SAFE_TEXT rejection
        assertTrue(res.has("error"), "XSS characters in description must be rejected");
    }

    @Test @Order(16)
    void testSecurity_YearOutOfRange() {
        JSONObject res = tool("tax_compliance", new JSONObject()
                .put("action","generate_report")
                .put("creator_id","creator-001")
                .put("year",1899));
        assertTrue(res.has("error"), "Year out of range must be rejected");
    }

    // ─── Tool 1: tax_compliance ───────────────────────────────────────────────────

    @Test @Order(20)
    void testTaxCompliance_Calculate_India() {
        JSONObject res = tool("tax_compliance", new JSONObject()
                .put("action","calculate")
                .put("creator_id","creator-001")
                .put("jurisdiction","IN")
                .put("gross_amount",26000)
                .put("tax_id_provided",true)
                .put("currency","INR"));
        assertOk(res);
        String t = txt(res);
        assertTrue(t.contains("10%") || t.contains("tax_withheld"), "India TDS should show 10% rate");
    }

    @Test @Order(21)
    void testTaxCompliance_Calculate_US_NoSSN() {
        JSONObject res = tool("tax_compliance", new JSONObject()
                .put("action","calculate")
                .put("creator_id","creator-002")
                .put("jurisdiction","US")
                .put("gross_amount",10000)
                .put("tax_id_provided",false)
                .put("currency","USD"));
        assertOk(res);
        String t = txt(res);
        assertTrue(t.contains("24%"), "US backup withholding should be 24%");
    }

    @Test @Order(22)
    void testTaxCompliance_GenerateReport() {
        JSONObject res = tool("tax_compliance", new JSONObject()
                .put("action","generate_report")
                .put("creator_id","creator-001")
                .put("jurisdiction","US")
                .put("year",2025));
        assertOk(res);
        assertTrue(txt(res).contains("1099"));
    }

    // ─── Tool 2: school_auto_creation ────────────────────────────────────────────

    @Test @Order(23)
    void testSchoolAutoCreation_EligibilityAdult() {
        JSONObject res = tool("school_auto_creation", new JSONObject()
                .put("action","check_eligibility")
                .put("creator_id","creator-001")
                .put("creator_age",25));
        assertOk(res);
        assertFalse(txt(res).contains("\"guardian_required\":true"));
    }

    @Test @Order(24)
    void testSchoolAutoCreation_MinorRequiresGuardian() {
        JSONObject res = tool("school_auto_creation", new JSONObject()
                .put("action","create")
                .put("creator_id","minor-001")
                .put("creator_age",15)); // no guardian_id
        assertOk(res);
        assertTrue(txt(res).contains("Guardian ID required") || txt(res).contains("ERROR"));
    }

    // ─── Tool 3: royalty_wallet ───────────────────────────────────────────────────

    @Test @Order(25)
    void testRoyaltyWallet_GetBalance() {
        JSONObject res = tool("royalty_wallet", new JSONObject()
                .put("action","get_balance")
                .put("creator_id","creator-001")
                .put("currency","INR"));
        assertOk(res);
        assertTrue(txt(res).contains("pending_balance"));
    }

    @Test @Order(26)
    void testRoyaltyWallet_CheckThreshold_BelowMin() {
        JSONObject res = tool("royalty_wallet", new JSONObject()
                .put("action","check_threshold")
                .put("creator_id","creator-001")
                .put("amount",50.0)
                .put("minimum_threshold",100.0));
        assertOk(res);
        assertTrue(txt(res).contains("false") || txt(res).contains("below"));
    }

    // ─── Tool 4: royalty_system_unified ──────────────────────────────────────────

    @Test @Order(27)
    void testRoyaltySystemUnified_FullCycle() {
        JSONObject res = tool("royalty_system_unified", new JSONObject()
                .put("action","process_full_cycle")
                .put("creator_id","creator-001")
                .put("agreement_id","agr-001")
                .put("sales_amount",400_000)
                .put("jurisdiction","IN")
                .put("tax_id_provided",true)
                .put("minimum_guarantee",50_000)
                .put("period","Q1_2025")
                .put("territory","Asia"));
        assertOk(res);
        String t = txt(res);
        assertTrue(t.contains("tiered_royalty"), "Should include tiered royalty calc");
        assertTrue(t.contains("net_payout"),     "Should include net payout after tax");
        assertTrue(t.contains("ledger_entries"), "Should include ledger entries");
    }

    @Test @Order(28)
    void testRoyaltySystemUnified_TrialBalance() {
        JSONObject res = tool("royalty_system_unified", new JSONObject()
                .put("action","trial_balance")
                .put("creator_id","creator-001")
                .put("agreement_id","agr-001"));
        assertOk(res);
        assertTrue(txt(res).contains("balanced") || txt(res).contains("0.00"));
    }

    // ─── Tool 5: royalty_escrow ───────────────────────────────────────────────────

    @Test @Order(29)
    void testRoyaltyEscrow_HoldAndRelease() {
        JSONObject hold = tool("royalty_escrow", new JSONObject()
                .put("action","hold")
                .put("creator_id","creator-001")
                .put("amount",10000)
                .put("hold_reason","GUARDIAN_CONSENT"));
        assertOk(hold);
        assertTrue(txt(hold).contains("HELD"));

        JSONObject release = tool("royalty_escrow", new JSONObject()
                .put("action","release")
                .put("creator_id","creator-001")
                .put("release_notes","Guardian consent obtained"));
        assertOk(release);
        assertTrue(txt(release).contains("RELEASED"));
    }

    // ─── Tool 6: royalty_distribution ────────────────────────────────────────────

    @Test @Order(30)
    void testRoyaltyDistribution_InitiateACH() {
        JSONObject res = tool("royalty_distribution", new JSONObject()
                .put("action","initiate")
                .put("creator_id","creator-001")
                .put("amount",23400)
                .put("currency","INR")
                .put("payout_method","ACH"));
        assertOk(res);
        assertTrue(txt(res).contains("PENDING") || txt(res).contains("payment_id"));
    }

    @Test @Order(31)
    void testRoyaltyDistribution_GetMethods() {
        JSONObject res = tool("royalty_distribution", new JSONObject()
                .put("action","get_methods")
                .put("creator_id","creator-001"));
        assertOk(res);
        assertTrue(txt(res).contains("STABLECOIN") && txt(res).contains("WIRE"));
    }

    // ─── Tool 7: royalty_calculation ─────────────────────────────────────────────

    @Test @Order(32)
    void testRoyaltyCalculation_Tiered_400K() {
        // Q1: ₹400K sales → Tier1: 5%*100K=5000, Tier2: 7%*300K=21000 → Total=26000
        JSONObject res = tool("royalty_calculation", new JSONObject()
                .put("action","tiered")
                .put("creator_id","creator-001")
                .put("sales_amount",400_000));
        assertOk(res);
        assertTrue(txt(res).contains("26000"), "₹400K sales should yield ₹26,000 royalty");
    }

    @Test @Order(33)
    void testRoyaltyCalculation_Tiered_600K() {
        // Q3: ₹600K → 5K + 28K + 10K = 43K
        JSONObject res = tool("royalty_calculation", new JSONObject()
                .put("action","tiered")
                .put("creator_id","creator-001")
                .put("sales_amount",600_000));
        assertOk(res);
        assertTrue(txt(res).contains("43000"), "₹600K sales should yield ₹43,000 royalty");
    }

    @Test @Order(34)
    void testRoyaltyCalculation_MinGuaranteeDeficiency() {
        // Q2: ₹200K sales → 5K+7K=12K < quarterly min 12.5K → deficiency 500
        JSONObject res = tool("royalty_calculation", new JSONObject()
                .put("action","minimum_guarantee_check")
                .put("creator_id","creator-001")
                .put("sales_amount",200_000)
                .put("minimum_guarantee",50_000));
        assertOk(res);
        assertTrue(txt(res).contains("has_deficiency"));
    }

    @Test @Order(35)
    void testRoyaltyCalculation_CurrencyConvert() {
        JSONObject res = tool("royalty_calculation", new JSONObject()
                .put("action","currency_convert")
                .put("creator_id","creator-001")
                .put("sales_amount",26_000)
                .put("source_currency","INR")
                .put("target_currency","USD")
                .put("exchange_rate",83.654321));
        assertOk(res);
        assertTrue(txt(res).contains("USD"));
    }

    // ─── Tool 8: revenue_ingestion ────────────────────────────────────────────────

    @Test @Order(36)
    void testRevenueIngestion_Ingest() {
        JSONObject res = tool("revenue_ingestion", new JSONObject()
                .put("action","ingest")
                .put("creator_id","creator-001")
                .put("licensee_id","licensee-001")
                .put("agreement_id","agr-001")
                .put("sales_amount",400_000)
                .put("period","Q1_2025"));
        assertOk(res);
        assertTrue(txt(res).contains("INGESTED"));
    }

    // ─── Tool 9: parent_dashboard ────────────────────────────────────────────────

    @Test @Order(37)
    void testParentDashboard_GrantConsent_NoSignature() {
        // Should fail — no digital_signature provided
        JSONObject res = tool("parent_dashboard", new JSONObject()
                .put("action","grant_consent")
                .put("creator_id","minor-001")
                .put("guardian_id","guardian-001")
                .put("digital_signature",""));
        assertOk(res);
        assertTrue(txt(res).contains("ERROR") || txt(res).contains("required"));
    }

    @Test @Order(38)
    void testParentDashboard_GrantConsent_WithSignature() {
        JSONObject res = tool("parent_dashboard", new JSONObject()
                .put("action","grant_consent")
                .put("creator_id","minor-001")
                .put("guardian_id","guardian-001")
                .put("digital_signature","ABCDEF123456789SIGNED"));
        assertOk(res);
        assertTrue(txt(res).contains("CONSENTED"));
    }

    // ─── Tools 10–18 smoke tests ─────────────────────────────────────────────────

    @Test @Order(39)
    void testMarketDemandPrediction_Forecast() {
        JSONObject res = tool("market_demand_prediction", new JSONObject()
                .put("action","forecast").put("idea_id","idea-001"));
        assertOk(res);
        assertTrue(txt(res).contains("forecast_revenue"));
    }

    @Test @Order(40)
    void testLicenseGeneration_Generate() {
        JSONObject res = tool("license_generation", new JSONObject()
                .put("action","generate")
                .put("idea_id","idea-001")
                .put("creator_id","creator-001")
                .put("licensee_id","biz-001")
                .put("royalty_rate",0.07)
                .put("minimum_guarantee",50_000)
                .put("territory","Asia"));
        assertOk(res);
        assertTrue(txt(res).contains("agreement_id") && txt(res).contains("ACTIVE"));
    }

    @Test @Order(41)
    void testIdeaVisibilityControl_Set() {
        JSONObject res = tool("idea_visibility_control", new JSONObject()
                .put("action","set_visibility")
                .put("idea_id","idea-001")
                .put("creator_id","creator-001")
                .put("visibility","LICENSEE_ONLY"));
        assertOk(res);
    }

    @Test @Order(42)
    void testIdeaEvaluation_Tiered_Rate_Recommendation() {
        JSONObject res = tool("idea_evaluation", new JSONObject()
                .put("action","recommend_rate")
                .put("idea_id","idea-001")
                .put("creator_id","creator-001")
                .put("sales_amount",600_000));
        assertOk(res);
        assertTrue(txt(res).contains("10%"), ">500K sales should recommend 10% rate");
    }

    @Test @Order(43)
    void testDataRetentionPolicy_GetPolicy() {
        JSONObject res = tool("data_retention_policy", new JSONObject()
                .put("action","get_policy"));
        assertOk(res);
        assertTrue(txt(res).contains("7 years"));
    }

    @Test @Order(44)
    void testContractLifecycle_Transition_Terminate() {
        JSONObject res = tool("contract_lifecycle", new JSONObject()
                .put("action","transition")
                .put("agreement_id","agr-001")
                .put("agreement_status","TERMINATED")
                .put("reason","Agreement expired naturally"));
        assertOk(res);
        assertTrue(txt(res).contains("TERMINATED") || txt(res).contains("licensing.agreement.terminated"));
    }

    @Test @Order(45)
    void testAuditVerification_TrialBalance() {
        JSONObject res = tool("audit_verification", new JSONObject()
                .put("action","trial_balance"));
        assertOk(res);
        assertTrue(txt(res).contains("balanced") || txt(res).contains("BALANCED"));
    }

    @Test @Order(46)
    void testAuditVerification_FraudScan_LargeAmount() {
        JSONObject res = tool("audit_verification", new JSONObject()
                .put("action","fraud_scan")
                .put("creator_id","creator-001")
                .put("amount",2_000_000)); // > 1M triggers HIGH risk
        assertOk(res);
        assertTrue(txt(res).contains("HIGH") || txt(res).contains("fraud.suspected"));
    }

    @Test @Order(47)
    void testAuditVerification_FlagPayoutLargeAmount() {
        JSONObject res = tool("audit_verification", new JSONObject()
                .put("action","flag_payout")
                .put("creator_id","creator-001")
                .put("amount",1_500_000)); // > 1M requires multi-sig
        assertOk(res);
        assertTrue(txt(res).contains("PENDING_APPROVAL") || txt(res).contains("CFO"));
    }

    @Test @Order(48)
    void testBusinessMatching_Match() {
        JSONObject res = tool("business_matching", new JSONObject()
                .put("action","match")
                .put("idea_id","idea-001")
                .put("territory","Asia"));
        assertOk(res);
        assertTrue(txt(res).contains("match_score") || txt(res).contains("BIZ-"));
    }

    @Test @Order(49)
    void testCompetitionEngine_CreateAndDeclareWinners() {
        // Create
        JSONObject create = tool("competition_engine", new JSONObject()
                .put("action","create")
                .put("creator_id","creator-001")
                .put("prize_pool",100_000)
                .put("currency","INR"));
        assertOk(create);
        assertTrue(txt(create).contains("competition_id"));

        // Declare winners
        JSONObject winners = tool("competition_engine", new JSONObject()
                .put("action","declare_winners")
                .put("creator_id","creator-001")
                .put("competition_id","COMP-test-001"));
        assertOk(winners);
        assertTrue(txt(winners).contains("ESCROW_HELD") || txt(winners).contains("winners"));
    }
}
