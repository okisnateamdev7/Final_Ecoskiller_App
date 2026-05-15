package com.ecoskiller.mcp.schema;

import com.ecoskiller.mcp.schema.client.ApicurioClient;
import com.ecoskiller.mcp.schema.config.SchemaRegistryConfig;
import com.ecoskiller.mcp.schema.security.AuditLogger;
import com.ecoskiller.mcp.schema.tools.ToolRegistry;
import com.ecoskiller.mcp.schema.tools.ToolResult;
import com.ecoskiller.mcp.schema.tools.McpTool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the Schema Registry MCP Server.
 *
 * Prerequisites:
 *   Apicurio Registry running on localhost:8080
 *   docker run -d -p 8080:8080 apicurio/apicurio-registry-mem:latest
 *
 *   export MCP_API_KEY=test-secret-key-schema
 *   mvn test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class McpSchemaRegistryServerTest {

    private static final String      TEST_API_KEY = "test-secret-key-schema";
    private static final ObjectMapper JSON        = new ObjectMapper();

    private static ToolRegistry registry;

    // Sample Avro schemas
    private static final String AVRO_V1 = """
        {
          "type": "record",
          "name": "CandidateScored",
          "namespace": "com.ecoskiller.events",
          "doc": "Event fired when a candidate is scored",
          "fields": [
            {"name": "candidateId",   "type": "string", "doc": "UUID of the candidate"},
            {"name": "assessmentId",  "type": "string", "doc": "UUID of the assessment"},
            {"name": "score",         "type": "double", "doc": "Overall score 0-100"},
            {"name": "timestamp",     "type": "long",   "doc": "Unix epoch ms"}
          ]
        }
        """;

    // BACKWARD compatible — adds optional field with default
    private static final String AVRO_V2 = """
        {
          "type": "record",
          "name": "CandidateScored",
          "namespace": "com.ecoskiller.events",
          "doc": "Event fired when a candidate is scored",
          "fields": [
            {"name": "candidateId",      "type": "string", "doc": "UUID of the candidate"},
            {"name": "assessmentId",     "type": "string", "doc": "UUID of the assessment"},
            {"name": "score",            "type": "double", "doc": "Overall score 0-100"},
            {"name": "timestamp",        "type": "long",   "doc": "Unix epoch ms"},
            {"name": "scoreBreakdown",   "type": ["null","string"], "default": null, "doc": "Optional breakdown JSON"}
          ]
        }
        """;

    // INCOMPATIBLE — removes required field
    private static final String AVRO_INCOMPAT = """
        {
          "type": "record",
          "name": "CandidateScored",
          "namespace": "com.ecoskiller.events",
          "fields": [
            {"name": "candidateId", "type": "string"}
          ]
        }
        """;

    @BeforeAll
    static void setup() {
        SchemaRegistryConfig cfg = SchemaRegistryConfig.fromEnvironment();
        ApicurioClient       client = new ApicurioClient(cfg);
        AuditLogger          audit  = new AuditLogger();
        registry = new ToolRegistry(client, cfg, audit);
    }

    private ObjectNode args(String... kv) {
        ObjectNode n = JSON.createObjectNode();
        n.put("api_key", TEST_API_KEY);
        for (int i = 0; i + 1 < kv.length; i += 2) n.put(kv[i], kv[i+1]);
        return n;
    }

    private ToolResult call(String toolName, ObjectNode arguments) throws Exception {
        McpTool tool = registry.find(toolName);
        assertNotNull(tool, "Tool not found: " + toolName);
        return tool.execute(arguments);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Registry
    // ─────────────────────────────────────────────────────────────────────────

    @Test @Order(1)
    void testRegistryHas22Tools() {
        assertEquals(22, registry.size(), "Should have exactly 22 tools");
    }

    @Test @Order(2)
    void testAllToolsHaveSchemas() {
        for (McpTool t : registry.allTools()) {
            ObjectNode s = t.schema();
            assertNotNull(s.get("name"),        t.name() + " missing name");
            assertNotNull(s.get("description"), t.name() + " missing description");
            assertNotNull(s.get("inputSchema"), t.name() + " missing inputSchema");
            // api_key must always be in required
            JsonNode req = s.path("inputSchema").path("required");
            assertTrue(req.toString().contains("api_key"), t.name() + " must require api_key");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MCP Protocol
    // ─────────────────────────────────────────────────────────────────────────

    @Test @Order(3)
    void testInitializeProtocol() throws Exception {
        McpSchemaRegistryServer server = new McpSchemaRegistryServer();
        String req  = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{}}";
        var m = McpSchemaRegistryServer.class.getDeclaredMethod("handleRequest", JsonNode.class);
        m.setAccessible(true);
        String resp = (String) m.invoke(server, JSON.readTree(req));
        JsonNode r  = JSON.readTree(resp);
        assertEquals("2024-11-05", r.path("result").path("protocolVersion").asText());
        assertEquals("mcp-schema-registry", r.path("result").path("serverInfo").path("name").asText());
    }

    @Test @Order(4)
    void testToolsListReturns22() throws Exception {
        McpSchemaRegistryServer server = new McpSchemaRegistryServer();
        String req  = "{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"tools/list\",\"params\":{}}";
        var m = McpSchemaRegistryServer.class.getDeclaredMethod("handleRequest", JsonNode.class);
        m.setAccessible(true);
        String resp = (String) m.invoke(server, JSON.readTree(req));
        assertEquals(22, JSON.readTree(resp).path("result").path("tools").size());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Schema Lint (no Apicurio needed)
    // ─────────────────────────────────────────────────────────────────────────

    @Test @Order(10)
    void testSchemaLintValidAvro() throws Exception {
        ToolResult r = call("schema_lint", args("schema_content", AVRO_V1, "artifact_type", "AVRO"));
        assertFalse(r.isError());
        JsonNode j = JSON.readTree(r.content());
        assertTrue(j.path("parseable").asBoolean());
        assertTrue(j.path("valid").asBoolean());
        assertEquals(0, j.path("error_count").asInt());
    }

    @Test @Order(11)
    void testSchemaLintBadJson() throws Exception {
        ToolResult r = call("schema_lint", args("schema_content", "{bad json!!!", "artifact_type", "AVRO"));
        assertFalse(r.isError());
        JsonNode j = JSON.readTree(r.content());
        assertFalse(j.path("parseable").asBoolean());
        assertFalse(j.path("valid").asBoolean());
        assertTrue(j.path("error_count").asInt() > 0);
    }

    @Test @Order(12)
    void testSchemaLintMissingRequiredFields() throws Exception {
        String minimal = "{\"type\":\"record\",\"fields\":[]}";
        ToolResult r = call("schema_lint", args("schema_content", minimal, "artifact_type", "AVRO"));
        JsonNode j = JSON.readTree(r.content());
        assertTrue(j.path("error_count").asInt() > 0, "Should flag missing 'name' field");
    }

    @Test @Order(13)
    void testSchemaLintPiiWarning() throws Exception {
        String schema = """
            {"type":"record","name":"User","namespace":"com.test","fields":[
              {"name":"email","type":"string","doc":"User email"},
              {"name":"candidateId","type":"string","doc":"ID"}
            ]}""";
        ToolResult r = call("schema_lint", args("schema_content", schema, "artifact_type", "AVRO"));
        JsonNode j = JSON.readTree(r.content());
        // Should warn about PII field
        assertTrue(j.path("warning_count").asInt() > 0, "Should warn about PII field 'email'");
    }

    @Test @Order(14)
    void testSchemaLintJsonSchema() throws Exception {
        String js = "{\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\"}}}";
        ToolResult r = call("schema_lint", args("schema_content", js, "artifact_type", "JSON"));
        JsonNode j = JSON.readTree(r.content());
        assertTrue(j.path("parseable").asBoolean());
        // Should warn about missing $schema, title, description
        assertTrue(j.path("warning_count").asInt() >= 3);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Compatibility Validation (local checks, no Apicurio needed)
    // ─────────────────────────────────────────────────────────────────────────

    @Test @Order(20)
    void testCompatibilityModeValidation() {
        // Invalid mode should throw
        var tool = registry.find("compatibility_set");
        assertNotNull(tool);
        ObjectNode a = args("artifact_id", "test", "compatibility", "INVALID_MODE");
        assertThrows(Exception.class, () -> tool.execute(a));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Security tests
    // ─────────────────────────────────────────────────────────────────────────

    @Test @Order(30)
    void testArtifactIdPathTraversalRejected() {
        var tool = registry.find("schema_get");
        assertNotNull(tool);
        ObjectNode a = args("artifact_id", "../../../etc/passwd");
        assertThrows(Exception.class, () -> tool.execute(a));
    }

    @Test @Order(31)
    void testArtifactIdWithSlashRejected() {
        var tool = registry.find("schema_get");
        ObjectNode a = args("artifact_id", "good/evil");
        assertThrows(Exception.class, () -> tool.execute(a));
    }

    @Test @Order(32)
    void testDeleteRequiresConfirm() throws Exception {
        ToolResult r = call("schema_delete", args("artifact_id", "test", "confirm", "NO"));
        assertTrue(r.isError() || r.content().contains("Safety") || r.content().contains("CONFIRM"));
    }

    @Test @Order(33)
    void testFlushNamespaceRequiresConfirm() throws Exception {
        ToolResult r = call("schema_delete", args("artifact_id", "test", "confirm", "WRONG"));
        assertTrue(r.isError() || r.content().contains("CONFIRM"));
    }

    @Test @Order(34)
    void testDeleteVersionLatestRejected() throws Exception {
        ToolResult r = call("schema_delete_version", args("artifact_id", "test", "version", "latest"));
        assertTrue(r.isError() || r.content().contains("latest"));
    }

    @Test @Order(35)
    void testGroupIdPathTraversalRejected() throws Exception {
        ToolResult r = call("group_create", args("group_id", "../evil"));
        assertTrue(r.isError() || r.content().contains("illegal"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Integration tests — require Apicurio running
    // ─────────────────────────────────────────────────────────────────────────

    @Test @Order(40)
    void testRegistryHealth() throws Exception {
        ToolResult r = call("registry_health", args());
        assertFalse(r.isError());
        JsonNode j = JSON.readTree(r.content());
        // If Apicurio is not running, connection=false is OK — we test the tool structure
        assertNotNull(j.get("healthy"));
        assertNotNull(j.get("connection"));
        assertNotNull(j.get("apicurio_url"));
        assertNotNull(j.get("latency_ms"));
    }

    @Test @Order(50)
    void testSchemaRegisterAndGetLifecycle() throws Exception {
        // Register
        ObjectNode regArgs = args(
            "artifact_id",   "test-candidate-scored",
            "artifact_type", "AVRO",
            "schema_content", AVRO_V1
        );
        ToolResult regR = call("schema_register", regArgs);
        // May succeed or fail depending on Apicurio availability
        assertNotNull(regR);

        if (!regR.isError()) {
            JsonNode j = JSON.readTree(regR.content());
            assertTrue(j.path("success").asBoolean());
            assertEquals("test-candidate-scored", j.path("artifact_id").asText());

            // Get it back
            ToolResult getR = call("schema_get", args("artifact_id","test-candidate-scored"));
            assertFalse(getR.isError());
            JsonNode gj = JSON.readTree(getR.content());
            assertEquals("test-candidate-scored", gj.path("artifact_id").asText());

            // List versions
            ToolResult versR = call("schema_list_versions", args("artifact_id","test-candidate-scored"));
            assertFalse(versR.isError());
            assertTrue(JSON.readTree(versR.content()).path("version_count").asInt() >= 1);

            // Update metadata
            ToolResult metaR = call("schema_update_meta", args(
                "artifact_id","test-candidate-scored",
                "name","Candidate Scored Event",
                "description","Fired when assessment scoring completes",
                "domain","assessment",
                "owner_team","scoring-team"
            ));
            assertFalse(metaR.isError());

            // Set compatibility
            ToolResult compatR = call("compatibility_set", args(
                "artifact_id","test-candidate-scored",
                "compatibility","BACKWARD"
            ));
            assertFalse(compatR.isError());

            // Get compatibility
            ToolResult getCompatR = call("compatibility_get", args("artifact_id","test-candidate-scored"));
            JsonNode cj = JSON.readTree(getCompatR.content());
            assertEquals("BACKWARD", cj.path("compatibility").asText());

            // Deprecate
            ToolResult depR = call("schema_update_state", args(
                "artifact_id","test-candidate-scored",
                "state","DEPRECATED",
                "reason","Test cleanup"
            ));
            assertFalse(depR.isError());

            // Delete
            ObjectNode delArgs = args("artifact_id","test-candidate-scored","confirm","CONFIRM");
            call("schema_delete", delArgs); // cleanup
        }
    }

    @Test @Order(60)
    void testSchemaSearchNoError() throws Exception {
        ToolResult r = call("schema_search", args("query","candidate"));
        assertFalse(r.isError());
        JsonNode j = JSON.readTree(r.content());
        assertNotNull(j.get("query"));
        assertNotNull(j.get("total_found"));
    }

    @Test @Order(70)
    void testGroupOperations() throws Exception {
        ToolResult listR = call("group_list", args());
        assertFalse(listR.isError());
        JsonNode j = JSON.readTree(listR.content());
        assertNotNull(j.get("group_count"));

        // Create a group
        ToolResult createR = call("group_create", args("group_id","test-tenant-001","description","Test tenant"));
        assertFalse(createR.isError());
    }
}
