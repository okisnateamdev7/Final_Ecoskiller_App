package com.ecoskiller.mcp.institute.service;

import com.ecoskiller.mcp.institute.config.ToolRegistry;
import com.ecoskiller.mcp.institute.config.ToolRegistry.ToolDef;
import com.ecoskiller.mcp.institute.tools.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * McpDispatcher — routes JSON-RPC 2.0 methods to the correct handler.
 *
 * <p>Supported methods:
 * <ul>
 *   <li>{@code initialize}   — returns server capabilities</li>
 *   <li>{@code tools/list}   — returns all registered tool descriptors</li>
 *   <li>{@code tools/call}   — dispatches to the appropriate agent tool</li>
 * </ul>
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpDispatcher {

    private final ToolRegistry registry;
    private final ObjectMapper  mapper;

    // Agent tools (one per CAT-05 agent)
    private final AcademicStructureTool  academicStructureTool;
    private final AttendanceTool         attendanceTool;
    private final CampusTool             campusTool;
    private final DataNormalizationTool  dataNormalizationTool;
    private final ExamEngineTool         examEngineTool;
    private final FacultyManagementTool  facultyManagementTool;
    private final FeeManagementTool      feeManagementTool;
    private final LmsMigrationTool       lmsMigrationTool;
    private final PlagiarismConnectTool  plagiarismConnectTool;
    private final StudentRecordTool      studentRecordTool;
    private final TaxonomyMigrationTool  taxonomyMigrationTool;
    private final TransportTool          transportTool;

    // ── Public entry point ────────────────────────────────────────────────────

    public Object dispatch(String method, JsonNode params, String requestId) {
        log.debug("MCP dispatch: method={} id={}", method, requestId);

        return switch (method) {
            case "initialize"  -> handleInitialize(params);
            case "tools/list"  -> handleToolsList();
            case "tools/call"  -> handleToolsCall(params, requestId);
            default -> Map.of("error", Map.of(
                    "code", -32601,
                    "message", "Method not found: " + method));
        };
    }

    // ── initialize ────────────────────────────────────────────────────────────

    private Map<String, Object> handleInitialize(JsonNode params) {
        return Map.of(
            "protocolVersion", "2024-11-05",
            "serverInfo", Map.of(
                "name",    "mcp-05-institute-erp",
                "version", "1.0.0"
            ),
            "capabilities", Map.of(
                "tools", Map.of("listChanged", false)
            )
        );
    }

    // ── tools/list ────────────────────────────────────────────────────────────

    private Map<String, Object> handleToolsList() {
        List<Map<String, Object>> tools = registry.all().stream()
            .map(t -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("name",        t.name());
                m.put("description", t.description().strip());
                m.put("inputSchema", t.inputSchema());
                // Antigravity metadata
                m.put("_agentRef",   t.agentRef());
                m.put("_sealed",     t.sealed());
                return m;
            }).toList();

        return Map.of("tools", tools);
    }

    // ── tools/call ────────────────────────────────────────────────────────────

    private Object handleToolsCall(JsonNode params, String requestId) {
        String toolName = params.path("name").asText();
        JsonNode args   = params.path("arguments");

        ToolDef def = registry.find(toolName);
        if (def == null) {
            return errorResult("Unknown tool: " + toolName);
        }

        // SEALED agent extra guard: tenant_id must be present
        if (def.sealed()) {
            String tenantId = args.path("tenant_id").asText("");
            if (tenantId.isBlank()) {
                return errorResult("[ANTIGRAVITY_SEALED] tenant_id is required for tool: " + toolName);
            }
        }

        log.info("Invoking tool={} sealed={} tenant={}", toolName, def.sealed(),
                args.path("tenant_id").asText("n/a"));

        try {
            String resultText = switch (toolName) {
                case "academic_structure"  -> academicStructureTool.execute(args);
                case "attendance"          -> attendanceTool.execute(args);
                case "campus"              -> campusTool.execute(args);
                case "data_normalization"  -> dataNormalizationTool.execute(args);
                case "exam_engine"         -> examEngineTool.execute(args);
                case "faculty_management"  -> facultyManagementTool.execute(args);
                case "fee_management"      -> feeManagementTool.execute(args);
                case "lms_migration"       -> lmsMigrationTool.execute(args);
                case "plagiarism_connect"  -> plagiarismConnectTool.execute(args);
                case "student_record"      -> studentRecordTool.execute(args);
                case "taxonomy_migration"  -> taxonomyMigrationTool.execute(args);
                case "transport"           -> transportTool.execute(args);
                default -> "Tool routing error";
            };

            return Map.of("content", List.of(Map.of("type", "text", "text", resultText)),
                          "isError", false);

        } catch (Exception ex) {
            log.error("Tool execution error tool={}", toolName, ex);
            return errorResult("Execution error: " + ex.getMessage());
        }
    }

    private Map<String, Object> errorResult(String msg) {
        return Map.of(
            "content", List.of(Map.of("type", "text", "text", msg)),
            "isError", true
        );
    }
}
