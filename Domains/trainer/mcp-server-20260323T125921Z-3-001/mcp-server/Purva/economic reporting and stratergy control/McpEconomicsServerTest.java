package com.ecoskiller.mcp.economics;

import com.ecoskiller.mcp.economics.agents.*;
import com.ecoskiller.mcp.economics.model.McpProtocol.*;
import com.ecoskiller.mcp.economics.tools.ToolRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class McpEconomicsServerTest {

    @Autowired ToolRegistry toolRegistry;
    @Autowired AntitrustAgent antitrustAgent;
    @Autowired BoardReportingAgent boardAgent;
    @Autowired DigitalTwinSimulationAgent digitalTwinAgent;
    @Autowired FinOpsAgent finOpsAgent;
    @Autowired PricingStrategyAgent pricingAgent;
    @Autowired RevenueRecognitionAgent revRecAgent;
    @Autowired TenantExportAgent exportAgent;

    // ── Registry ──────────────────────────────────────────

    @Test void registry_registers_17_agents() {
        assertEquals(17, toolRegistry.getToolCount(), "CAT-08 must have exactly 17 agents");
    }

    @Test void registry_listTools_all_have_valid_schema() {
        toolRegistry.listTools().getTools().forEach(t -> {
            assertNotNull(t.getName(), "Tool name null");
            assertFalse(t.getName().isBlank(), "Tool name blank");
            assertFalse(t.getDescription().isBlank(), "Description blank: " + t.getName());
            assertEquals("object", t.getInputSchema().getType(), "Schema type wrong: " + t.getName());
        });
    }

    @Test void registry_unknown_tool_returns_error() {
        ToolCallResult result = toolRegistry.callTool("no_such_tool", Map.of());
        assertTrue(result.isError());
        assertTrue(result.getContent().get(0).getText().contains("not found"));
    }

    // ── Antitrust ─────────────────────────────────────────

    @Test void antitrust_analyze_pricing_compliant() {
        var r = antitrustAgent.execute(Map.of("action","analyze_pricing","jurisdiction","india_cci"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("COMPLIANT"));
    }

    @Test void antitrust_get_risk_score() {
        var r = antitrustAgent.execute(Map.of("action","get_risk_score"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("risk_score"));
    }

    @Test void antitrust_missing_action_returns_error() {
        var r = antitrustAgent.execute(Map.of());
        assertTrue(r.isError());
    }

    // ── Board Reporting ───────────────────────────────────

    @Test void board_get_kpis_success() {
        var r = boardAgent.execute(Map.of("action","get_kpis","period","Q3-2025"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("total_tenants"));
    }

    @Test void board_revenue_summary_success() {
        var r = boardAgent.execute(Map.of("action","revenue_summary","period","Q3-2025"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("arr_inr"));
    }

    @Test void board_generate_pack_has_pdf_url() {
        var r = boardAgent.execute(Map.of("action","generate_pack","period","Q3-2025","audience","board"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("pdf_url"));
    }

    // ── Digital Twin ──────────────────────────────────────

    @Test void digital_twin_run_simulation_success() {
        var r = digitalTwinAgent.execute(Map.of(
            "action","run_simulation","simulation_type","growth",
            "horizon_months","12","confidence_level","p75"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("simulation_id"));
    }

    @Test void digital_twin_stress_test_success() {
        var r = digitalTwinAgent.execute(Map.of("action","stress_test"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("critical_threshold_breached"));
    }

    @Test void digital_twin_scenarios_returned() {
        var r = digitalTwinAgent.execute(Map.of("action","get_scenarios","simulation_type","pricing"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("Bear"));
    }

    // ── FinOps ────────────────────────────────────────────

    @Test void finops_spend_summary_success() {
        var r = finOpsAgent.execute(Map.of("action","get_spend_summary","period","2025-09","cloud_provider","aws"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("total_spend_inr"));
    }

    @Test void finops_identify_waste_success() {
        var r = finOpsAgent.execute(Map.of("action","identify_waste","period","2025-09"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("savings_if_fixed_inr"));
    }

    @Test void finops_unit_economics_success() {
        var r = finOpsAgent.execute(Map.of("action","unit_economics","period","2025-09"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("cost_per_tenant_inr"));
    }

    // ── Pricing Strategy ──────────────────────────────────

    @Test void pricing_current_pricing_success() {
        var r = pricingAgent.execute(Map.of("action","get_current_pricing","segment","institute"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("pricing_tiers"));
    }

    @Test void pricing_competitive_benchmark_success() {
        var r = pricingAgent.execute(Map.of("action","competitive_benchmark","segment","corporate"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("price_headroom"));
    }

    // ── Revenue Recognition ───────────────────────────────

    @Test void rev_rec_calculate_mrr_success() {
        var r = revRecAgent.execute(Map.of("action","calculate_mrr","period","2025-09"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("mrr_inr"));
    }

    @Test void rev_rec_calculate_arr_success() {
        var r = revRecAgent.execute(Map.of("action","calculate_arr","period","2025-09"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("arr_inr"));
    }

    @Test void rev_rec_recognize_subscription() {
        var r = revRecAgent.execute(Map.of(
            "action","recognize_subscription","tenant_id","t_001",
            "contract_value_inr","100000","contract_months","12","period","2025-09"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("journal_entry"));
    }

    // ── Tenant Export ─────────────────────────────────────

    @Test void export_initiate_success() {
        var r = exportAgent.execute(Map.of(
            "action","initiate_export","tenant_id","t_001",
            "modules","students,finance","format","json","reason","offboarding"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("export_id"));
        assertTrue(r.getContent().get(0).getText().contains("PDPB"));
    }

    @Test void export_missing_tenant_returns_error() {
        var r = exportAgent.execute(Map.of("action","initiate_export"));
        assertTrue(r.isError());
    }

    // ── Dispatch via registry ─────────────────────────────

    @Test void dispatch_seo_keyword_research() {
        var r = toolRegistry.callTool("seo_optimize",
            Map.of("action","keyword_research","keywords","institute ERP,skill assessment"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("opportunities"));
    }

    @Test void dispatch_hiring_bias_analyze_jd() {
        var r = toolRegistry.callTool("hiring_bias_detect",
            Map.of("action","analyze_jd","tenant_id","t_001",
                   "jd_text","Looking for rockstar developer, IIT preferred"));
        assertFalse(r.isError());
        assertTrue(r.getContent().get(0).getText().contains("biased_phrases_detected"));
    }
}
