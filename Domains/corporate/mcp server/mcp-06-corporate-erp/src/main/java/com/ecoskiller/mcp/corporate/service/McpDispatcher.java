package com.ecoskiller.mcp.corporate.service;

import com.ecoskiller.mcp.corporate.config.ToolRegistry;
import com.ecoskiller.mcp.corporate.config.ToolRegistry.ToolDef;
import com.ecoskiller.mcp.corporate.tools.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * McpDispatcher — routes JSON-RPC 2.0 methods for mcp-06-corporate-erp.
 *
 * <p>Supported methods: {@code initialize}, {@code tools/list}, {@code tools/call}</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpDispatcher {

    private final ToolRegistry      registry;

    // ── All 12 CAT-06 agent tools ─────────────────────────────────────────────
    private final AccountingTool     accountingTool;
    private final AccountingSyncTool accountingSyncTool;
    private final AssetTool          assetTool;
    private final BudgetTool         budgetTool;
    private final DmsTool            dmsTool;
    private final ErpAnalyticsTool   erpAnalyticsTool;
    private final GstConnectTool     gstConnectTool;
    private final HrmsTool           hrmsTool;
    private final LedgerMigrationTool ledgerMigrationTool;
    private final PayrollTool        payrollTool;
    private final ProcurementTool    procurementTool;
    private final RegulatoryTool     regulatoryTool;

    // ── Entry point ───────────────────────────────────────────────────────────

    public Object dispatch(String method, JsonNode params, String requestId) {
        log.debug("MCP dispatch: method={} id={}", method, requestId);
        return switch (method) {
            case "initialize"  -> handleInitialize();
            case "tools/list"  -> handleToolsList();
            case "tools/call"  -> handleToolsCall(params);
            default -> Map.of("error", Map.of("code", -32601,
                              "message", "Method not found: " + method));
        };
    }

    // ── initialize ────────────────────────────────────────────────────────────

    private Map<String, Object> handleInitialize() {
        return Map.of(
            "protocolVersion", "2024-11-05",
            "serverInfo", Map.of("name", "mcp-06-corporate-erp", "version", "1.0.0"),
            "capabilities", Map.of("tools", Map.of("listChanged", false))
        );
    }

    // ── tools/list ────────────────────────────────────────────────────────────

    private Map<String, Object> handleToolsList() {
        List<Map<String, Object>> tools = registry.all().stream().map(t -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("name",        t.name());
            m.put("description", t.description().strip());
            m.put("inputSchema", t.inputSchema());
            m.put("_agentRef",   t.agentRef());
            m.put("_sealed",     t.sealed());
            return m;
        }).toList();
        return Map.of("tools", tools);
    }

    // ── tools/call ────────────────────────────────────────────────────────────

    private Object handleToolsCall(JsonNode params) {
        String   toolName = params.path("name").asText();
        JsonNode args     = params.path("arguments");

        ToolDef def = registry.find(toolName);
        if (def == null) {
            return errorResult("Unknown tool: " + toolName);
        }

        // SEALED: tenant_id is always mandatory
        if (def.sealed() && args.path("tenant_id").asText("").isBlank()) {
            return errorResult("[ANTIGRAVITY_SEALED] tenant_id is required for tool: " + toolName);
        }

        log.info("Invoking tool={} sealed={} tenant={}",
                toolName, def.sealed(), args.path("tenant_id").asText("n/a"));

        try {
            String resultText = switch (toolName) {
                case "accounting"      -> accountingTool.execute(args);
                case "accounting_sync" -> accountingSyncTool.execute(args);
                case "asset"           -> assetTool.execute(args);
                case "budget"          -> budgetTool.execute(args);
                case "dms"             -> dmsTool.execute(args);
                case "erp_analytics"   -> erpAnalyticsTool.execute(args);
                case "gst_connect"     -> gstConnectTool.execute(args);
                case "hrms"            -> hrmsTool.execute(args);
                case "ledger_migration"-> ledgerMigrationTool.execute(args);
                case "payroll"         -> payrollTool.execute(args);
                case "procurement"     -> procurementTool.execute(args);
                case "regulatory"      -> regulatoryTool.execute(args);
                default -> "Tool routing error";
            };
            return Map.of(
                "content", List.of(Map.of("type", "text", "text", resultText)),
                "isError", false);
        } catch (Exception ex) {
            log.error("Tool execution error tool={}", toolName, ex);
            return errorResult("Execution error: " + ex.getMessage());
        }
    }

    private Map<String, Object> errorResult(String msg) {
        return Map.of(
            "content", List.of(Map.of("type", "text", "text", msg)),
            "isError", true);
    }
}
