package com.ecoskiller.mcp.institute;

import com.ecoskiller.mcp.institute.config.ToolRegistry;
import com.ecoskiller.mcp.institute.service.McpDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * McpController — HTTP entry point for mcp-05-institute-erp.
 *
 * <pre>
 *   POST /mcp          ← JSON-RPC 2.0 (Content-Type: application/json)
 *   GET  /mcp/health   ← liveness probe
 *   GET  /mcp/manifest ← server identity + tool count
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/mcp")
@RequiredArgsConstructor
public class McpController {

    private final McpDispatcher dispatcher;
    private final ToolRegistry  registry;
    private final ObjectMapper  mapper;

    // ── POST /mcp ─────────────────────────────────────────────────────────────

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> handle(@RequestBody JsonNode body) {

        String  id     = body.path("id").asText(null);
        String  method = body.path("method").asText("");
        JsonNode params = body.path("params");

        log.debug("→ POST /mcp method={} id={}", method, id);

        Object result = dispatcher.dispatch(method, params, id);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        response.put("result", result);

        return ResponseEntity.ok(response);
    }

    // ── GET /mcp/health ───────────────────────────────────────────────────────

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status",  "UP",
            "server",  "mcp-05-institute-erp",
            "version", "1.0.0"
        ));
    }

    // ── GET /mcp/manifest ─────────────────────────────────────────────────────

    @GetMapping("/manifest")
    public ResponseEntity<Map<String, Object>> manifest() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id",          "mcp-05-institute-erp");
        m.put("name",        "Ecoskiller Institute ERP MCP Server");
        m.put("version",     "1.0.0");
        m.put("namespace",   "erp");
        m.put("category",    "CAT-05");
        m.put("priority",    "HIGH");
        m.put("toolCount",   registry.all().size());
        m.put("sealedTools", registry.all().stream().filter(ToolRegistry.ToolDef::sealed).count());
        m.put("tools",       registry.all().stream()
                .map(t -> Map.of(
                    "name",    t.name(),
                    "agent",   t.agentRef(),
                    "sealed",  t.sealed()))
                .toList());
        return ResponseEntity.ok(m);
    }
}
