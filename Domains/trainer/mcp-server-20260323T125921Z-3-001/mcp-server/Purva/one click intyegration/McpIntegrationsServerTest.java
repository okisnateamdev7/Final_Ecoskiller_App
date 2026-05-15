package com.ecoskiller.mcp.integrations;

import com.ecoskiller.mcp.integrations.agents.*;
import com.ecoskiller.mcp.integrations.model.McpProtocol.*;
import com.ecoskiller.mcp.integrations.tools.ToolRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class McpIntegrationsServerTest {

    @Autowired ToolRegistry toolRegistry;
    @Autowired SsoIntegrationAgent ssoAgent;
    @Autowired CalendarSyncAgent calendarAgent;

    // ── ToolRegistry tests ──────────────────────

    @Test void toolRegistry_registersAll18Agents() {
        assertEquals(18, toolRegistry.getToolCount(), "Should register exactly 18 CAT-07 agents");
    }

    @Test void toolRegistry_listTools_returnsAllTools() {
        ToolsListResult result = toolRegistry.listTools();
        assertNotNull(result.getTools());
        assertEquals(18, result.getTools().size());
    }

    @Test void toolRegistry_unknownTool_returnsError() {
        ToolCallResult result = toolRegistry.callTool("nonexistent_tool", Map.of());
        assertTrue(result.isError());
        assertTrue(result.getContent().get(0).getText().contains("not found"));
    }

    // ── SSO Agent ───────────────────────────────

    @Test void ssoAgent_configure_succeeds() {
        ToolCallResult result = ssoAgent.execute(Map.of(
            "action", "configure",
            "tenant_id", "school_abc",
            "provider", "google"
        ));
        assertFalse(result.isError());
        assertTrue(result.getContent().get(0).getText().contains("configured"));
    }

    @Test void ssoAgent_listProviders_succeeds() {
        ToolCallResult result = ssoAgent.execute(Map.of(
            "action", "list_providers",
            "tenant_id", "school_abc"
        ));
        assertFalse(result.isError());
    }

    @Test void ssoAgent_missingTenantId_returnsError() {
        ToolCallResult result = ssoAgent.execute(Map.of("action", "configure"));
        assertTrue(result.isError());
    }

    @Test void ssoAgent_configureMissingProvider_returnsError() {
        ToolCallResult result = ssoAgent.execute(Map.of(
            "action", "configure",
            "tenant_id", "school_abc"
            // provider intentionally missing
        ));
        assertTrue(result.isError());
        assertTrue(result.getContent().get(0).getText().contains("provider"));
    }

    // ── Calendar Agent ─────────────────────────

    @Test void calendarAgent_connect_succeeds() {
        ToolCallResult result = calendarAgent.execute(Map.of(
            "action", "connect",
            "tenant_id", "school_xyz",
            "provider", "google"
        ));
        assertFalse(result.isError());
        assertTrue(result.getContent().get(0).getText().contains("auth_url"));
    }

    @Test void calendarAgent_getFeedUrl_succeeds() {
        ToolCallResult result = calendarAgent.execute(Map.of(
            "action", "get_feed_url",
            "tenant_id", "school_xyz",
            "user_id", "user_001"
        ));
        assertFalse(result.isError());
        assertTrue(result.getContent().get(0).getText().contains(".ics"));
    }

    // ── Tool schema validation ──────────────────

    @Test void allTools_haveNonEmptyNames() {
        toolRegistry.listTools().getTools()
            .forEach(t -> assertFalse(t.getName().isBlank(), "Tool name must not be blank"));
    }

    @Test void allTools_haveNonEmptyDescriptions() {
        toolRegistry.listTools().getTools()
            .forEach(t -> assertFalse(t.getDescription().isBlank(), "Tool description must not be blank"));
    }

    @Test void allTools_haveObjectTypeSchema() {
        toolRegistry.listTools().getTools()
            .forEach(t -> {
                assertNotNull(t.getInputSchema(), "InputSchema must not be null for: " + t.getName());
                assertEquals("object", t.getInputSchema().getType(),
                    "Schema type must be 'object' for: " + t.getName());
            });
    }

    // ── Payment Agent via registry dispatch ────

    @Test void paymentConnect_createOrder_dispatches() {
        ToolCallResult result = toolRegistry.callTool("payment_connect", Map.of(
            "action", "create_order",
            "tenant_id", "school_001",
            "gateway", "razorpay",
            "amount_paise", "50000",
            "currency", "INR"
        ));
        assertFalse(result.isError());
        assertTrue(result.getContent().get(0).getText().contains("order_id"));
    }

    // ── WhatsApp Agent ─────────────────────────

    @Test void whatsappConnect_sendTemplate_succeeds() {
        ToolCallResult result = toolRegistry.callTool("whatsapp_connect", Map.of(
            "action", "send_template",
            "tenant_id", "school_001",
            "provider", "meta_cloud",
            "to_phone", "+919876543210",
            "template_name", "fee_reminder_v2"
        ));
        assertFalse(result.isError());
    }
}
