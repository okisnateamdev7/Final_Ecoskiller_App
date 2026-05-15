package com.ecoskiller.mcp16;

import com.google.gson.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DAILY_MISSION_AGENT_ANTIGRAVITY
 */
class DailyMissionAgentTest {

    private final DailyMissionAgent agent = new DailyMissionAgent();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private JsonObject args(String... kvPairs) {
        JsonObject o = new JsonObject();
        for (int i = 0; i < kvPairs.length - 1; i += 2)
            o.addProperty(kvPairs[i], kvPairs[i + 1]);
        return o;
    }

    private JsonObject parse(JsonElement result) {
        JsonObject root = result.getAsJsonObject();
        String text = root.getAsJsonArray("content")
                          .get(0).getAsJsonObject()
                          .get("text").getAsString();
        return JsonParser.parseString(text).getAsJsonObject();
    }

    private boolean isError(JsonElement result) {
        return result.getAsJsonObject().get("isError").getAsBoolean();
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Tool definition is complete and valid")
    void testToolDefinition() {
        JsonObject def = agent.toolDefinition();
        assertEquals("daily_mission", def.get("name").getAsString());
        assertTrue(def.has("description"));
        assertTrue(def.has("inputSchema"));
        JsonObject schema = def.getAsJsonObject("inputSchema");
        assertTrue(schema.getAsJsonObject("properties").has("action"));
        assertTrue(schema.getAsJsonObject("properties").has("user_id"));
        JsonArray required = schema.getAsJsonArray("required");
        assertEquals(2, required.size());
    }

    @Test
    @DisplayName("generate_mission — success with defaults")
    void testGenerateMissionDefault() {
        JsonElement result = agent.execute(args(
            "action", "generate_mission",
            "user_id", "user_001"
        ));
        assertFalse(isError(result));
        JsonObject data = parse(result).getAsJsonObject("data");
        assertEquals("user_001", data.get("user_id").getAsString());
        assertEquals("medium",   data.get("difficulty").getAsString());
        assertEquals("general",  data.get("subject").getAsString());
        assertEquals("pending",  data.get("status").getAsString());
        assertTrue(data.get("xp_reward").getAsInt() > 0);
    }

    @Test
    @DisplayName("generate_mission — elite difficulty for master tier")
    void testGenerateMissionEliteMaster() {
        JsonElement result = agent.execute(args(
            "action",     "generate_mission",
            "user_id",    "user_master_42",
            "difficulty", "elite",
            "tier",       "master",
            "subject",    "coding"
        ));
        assertFalse(isError(result));
        JsonObject data = parse(result).getAsJsonObject("data");
        assertEquals("elite",  data.get("difficulty").getAsString());
        assertEquals("master", data.get("tier").getAsString());
        assertEquals("coding", data.get("subject").getAsString());
        // master multiplier 1.5 × 500 elite XP = 750
        assertEquals(750, data.get("xp_reward").getAsInt());
    }

    @Test
    @DisplayName("complete_mission — returns xp and rewards")
    void testCompleteMission() {
        JsonElement result = agent.execute(args(
            "action",     "complete_mission",
            "user_id",    "user_007",
            "mission_id", "user_007-math-hard-2026-03-16",
            "tier",       "bloom"
        ));
        assertFalse(isError(result));
        JsonObject data = parse(result).getAsJsonObject("data");
        assertEquals("completed", data.get("status").getAsString());
        assertTrue(data.get("total_xp_awarded").getAsInt() > 0);
    }

    @Test
    @DisplayName("get_streak — returns streak info")
    void testGetStreak() {
        JsonElement result = agent.execute(args(
            "action",  "get_streak",
            "user_id", "user_streaker"
        ));
        assertFalse(isError(result));
        JsonObject data = parse(result).getAsJsonObject("data");
        assertTrue(data.get("current_streak").getAsInt() >= 0);
        assertTrue(data.has("longest_streak"));
        assertTrue(data.has("next_milestone"));
    }

    @Test
    @DisplayName("list_missions — returns mission array")
    void testListMissions() {
        JsonElement result = agent.execute(args(
            "action",  "list_missions",
            "user_id", "user_list_test",
            "date",    "2026-03-16"
        ));
        assertFalse(isError(result));
        JsonObject data = parse(result).getAsJsonObject("data");
        assertTrue(data.get("count").getAsInt() > 0);
        assertTrue(data.getAsJsonArray("missions").size() > 0);
    }

    @Test
    @DisplayName("list_missions — subject filter")
    void testListMissionsSubjectFilter() {
        JsonElement result = agent.execute(args(
            "action",  "list_missions",
            "user_id", "user_filter",
            "subject", "math"
        ));
        assertFalse(isError(result));
        JsonObject data = parse(result).getAsJsonObject("data");
        JsonArray missions = data.getAsJsonArray("missions");
        assertEquals(1, missions.size());
        assertEquals("math", missions.get(0).getAsJsonObject().get("subject").getAsString());
    }

    @Test
    @DisplayName("reset_daily — confirms reset")
    void testResetDaily() {
        JsonElement result = agent.execute(args(
            "action",  "reset_daily",
            "user_id", "user_admin",
            "date",    "2026-03-16"
        ));
        assertFalse(isError(result));
        JsonObject data = parse(result).getAsJsonObject("data");
        assertEquals("reset_complete", data.get("status").getAsString());
    }

    @Test
    @DisplayName("Error — missing required action")
    void testMissingAction() {
        assertThrows(Exception.class, () ->
            agent.execute(args("user_id", "user_no_action"))
        );
    }

    @Test
    @DisplayName("Error — invalid difficulty")
    void testInvalidDifficulty() {
        JsonElement result = agent.execute(args(
            "action",     "generate_mission",
            "user_id",    "user_bad",
            "difficulty", "impossible"
        ));
        assertTrue(isError(result));
        JsonObject body = parse(result);
        assertEquals("INVALID_DIFFICULTY", body.get("error_code").getAsString());
    }

    @Test
    @DisplayName("Error — invalid action name")
    void testInvalidAction() {
        JsonElement result = agent.execute(args(
            "action",  "destroy_everything",
            "user_id", "user_bad"
        ));
        assertTrue(isError(result));
        assertEquals("INVALID_ACTION", parse(result).get("error_code").getAsString());
    }

    @Test
    @DisplayName("Error — complete_mission without mission_id")
    void testCompleteMissionMissingId() {
        JsonElement result = agent.execute(args(
            "action",  "complete_mission",
            "user_id", "user_no_id"
        ));
        assertTrue(isError(result));
        assertEquals("MISSING_MISSION_ID", parse(result).get("error_code").getAsString());
    }

    @Test
    @DisplayName("Error — invalid date format")
    void testInvalidDate() {
        JsonElement result = agent.execute(args(
            "action",  "generate_mission",
            "user_id", "user_date",
            "date",    "not-a-date"
        ));
        assertTrue(isError(result));
        assertEquals("INVALID_DATE", parse(result).get("error_code").getAsString());
    }
}
