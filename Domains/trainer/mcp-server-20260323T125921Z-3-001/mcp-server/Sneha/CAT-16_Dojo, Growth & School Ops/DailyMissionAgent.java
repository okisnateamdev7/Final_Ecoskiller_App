package com.ecoskiller.mcp16;

import com.google.gson.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * DAILY_MISSION_AGENT_ANTIGRAVITY
 *
 * CAT-16 — Dojo, Growth & School Ops
 *
 * Responsibilities:
 *  - Generate personalized daily missions for learners / school participants
 *  - Track mission completion status
 *  - Assign XP / skill points on completion
 *  - Support streak management
 *  - Return mission details filtered by difficulty, subject, or user tier
 */
public class DailyMissionAgent {

    // -------------------------------------------------------------------------
    // Supported actions
    // -------------------------------------------------------------------------
    private static final Set<String> VALID_ACTIONS = Set.of(
        "generate_mission",
        "get_mission_status",
        "complete_mission",
        "get_streak",
        "list_missions",
        "reset_daily"
    );

    private static final Set<String> VALID_DIFFICULTIES = Set.of("easy", "medium", "hard", "elite");
    private static final Set<String> VALID_SUBJECTS = Set.of(
        "math", "science", "language", "coding", "art", "music", "sports",
        "critical_thinking", "entrepreneurship", "general"
    );
    private static final Set<String> VALID_TIERS = Set.of("seed", "sprout", "bloom", "master");

    // -------------------------------------------------------------------------
    // Tool definition (returned by tools/list)
    // -------------------------------------------------------------------------

    public JsonObject toolDefinition() {
        JsonObject tool = new JsonObject();
        tool.addProperty("name",        "daily_mission");
        tool.addProperty("description",
            "DAILY_MISSION_AGENT_ANTIGRAVITY — Manages daily learning missions for EcoSkiller " +
            "Dojo participants. Generates, tracks, completes, and resets daily missions with " +
            "XP rewards, streak bonuses, and tier-based difficulty scaling.");

        // Input schema
        JsonObject schema = new JsonObject();
        schema.addProperty("type", "object");

        JsonObject props = new JsonObject();

        // action
        JsonObject action = new JsonObject();
        action.addProperty("type", "string");
        action.addProperty("description",
            "Operation to perform. One of: generate_mission, get_mission_status, " +
            "complete_mission, get_streak, list_missions, reset_daily");
        JsonArray actionEnum = new JsonArray();
        VALID_ACTIONS.forEach(actionEnum::add);
        action.add("enum", actionEnum);
        props.add("action", action);

        // user_id
        JsonObject userId = new JsonObject();
        userId.addProperty("type", "string");
        userId.addProperty("description", "Unique identifier for the learner/participant.");
        props.add("user_id", userId);

        // mission_id
        JsonObject missionId = new JsonObject();
        missionId.addProperty("type", "string");
        missionId.addProperty("description",
            "Mission ID (required for complete_mission and get_mission_status).");
        props.add("mission_id", missionId);

        // difficulty
        JsonObject diff = new JsonObject();
        diff.addProperty("type", "string");
        diff.addProperty("description",
            "Mission difficulty filter: easy | medium | hard | elite. Default: medium.");
        JsonArray diffEnum = new JsonArray();
        VALID_DIFFICULTIES.forEach(diffEnum::add);
        diff.add("enum", diffEnum);
        props.add("difficulty", diff);

        // subject
        JsonObject subject = new JsonObject();
        subject.addProperty("type", "string");
        subject.addProperty("description",
            "Subject area for mission generation. Default: general.");
        props.add("subject", subject);

        // tier
        JsonObject tier = new JsonObject();
        tier.addProperty("type", "string");
        tier.addProperty("description",
            "Learner tier: seed | sprout | bloom | master. Affects XP multiplier.");
        props.add("tier", tier);

        // date
        JsonObject date = new JsonObject();
        date.addProperty("type", "string");
        date.addProperty("description",
            "ISO date (yyyy-MM-dd) for which to generate / query missions. Defaults to today.");
        props.add("date", date);

        schema.add("properties", props);
        JsonArray required = new JsonArray();
        required.add("action");
        required.add("user_id");
        schema.add("required", required);

        tool.add("inputSchema", schema);
        return tool;
    }

    // -------------------------------------------------------------------------
    // Execution entry point
    // -------------------------------------------------------------------------

    public JsonElement execute(JsonObject args) {
        // --- Validate required fields ---
        String action = requireString(args, "action");
        String userId = requireString(args, "user_id");

        if (!VALID_ACTIONS.contains(action)) {
            return errorContent("INVALID_ACTION",
                "Unknown action '" + action + "'. Valid actions: " + VALID_ACTIONS);
        }

        String difficulty = optString(args, "difficulty", "medium");
        String subject    = optString(args, "subject",    "general");
        String tier       = optString(args, "tier",       "sprout");
        String missionId  = optString(args, "mission_id", null);
        String dateStr    = optString(args, "date",       LocalDate.now().toString());

        if (!VALID_DIFFICULTIES.contains(difficulty))
            return errorContent("INVALID_DIFFICULTY",
                "difficulty must be one of: " + VALID_DIFFICULTIES);
        if (!VALID_SUBJECTS.contains(subject))
            return errorContent("INVALID_SUBJECT",
                "subject must be one of: " + VALID_SUBJECTS);
        if (!VALID_TIERS.contains(tier))
            return errorContent("INVALID_TIER",
                "tier must be one of: " + VALID_TIERS);

        LocalDate date;
        try { date = LocalDate.parse(dateStr); }
        catch (Exception e) {
            return errorContent("INVALID_DATE", "date must be ISO format yyyy-MM-dd");
        }

        return switch (action) {
            case "generate_mission"   -> generateMission(userId, difficulty, subject, tier, date);
            case "get_mission_status" -> getMissionStatus(userId, missionId, date);
            case "complete_mission"   -> completeMission(userId, missionId, tier);
            case "get_streak"         -> getStreak(userId);
            case "list_missions"      -> listMissions(userId, date, difficulty, subject);
            case "reset_daily"        -> resetDaily(userId, date);
            default -> errorContent("UNREACHABLE", "Should never happen.");
        };
    }

    // =========================================================================
    // Action implementations
    // =========================================================================

    /** Generate a new daily mission for a user. */
    private JsonElement generateMission(
            String userId, String difficulty, String subject, String tier, LocalDate date) {

        String missionId = buildMissionId(userId, subject, difficulty, date);
        int    baseXp    = xpForDifficulty(difficulty);
        double multiplier= xpMultiplier(tier);
        int    totalXp   = (int) (baseXp * multiplier);

        JsonObject data = new JsonObject();
        data.addProperty("mission_id",       missionId);
        data.addProperty("user_id",          userId);
        data.addProperty("date",             date.toString());
        data.addProperty("title",            missionTitle(subject, difficulty));
        data.addProperty("description",      missionDescription(subject, difficulty));
        data.addProperty("subject",          subject);
        data.addProperty("difficulty",       difficulty);
        data.addProperty("tier",             tier);
        data.addProperty("xp_reward",        totalXp);
        data.addProperty("base_xp",          baseXp);
        data.addProperty("tier_multiplier",  multiplier);
        data.addProperty("status",           "pending");
        data.addProperty("expires_at",       date.plusDays(1).toString() + "T00:00:00Z");
        data.addProperty("created_at",       Instant.now().toString());
        data.add("tasks",                    buildTasks(subject, difficulty));
        data.add("completion_criteria",      buildCriteria(subject, difficulty));
        data.add("badges_on_completion",     buildBadges(subject, tier));

        return successContent("Mission generated successfully.", data);
    }

    /** Get status of a specific mission. */
    private JsonElement getMissionStatus(String userId, String missionId, LocalDate date) {
        if (missionId == null || missionId.isBlank())
            return errorContent("MISSING_MISSION_ID",
                "mission_id is required for get_mission_status");

        JsonObject data = new JsonObject();
        data.addProperty("mission_id",  missionId);
        data.addProperty("user_id",     userId);
        data.addProperty("date",        date.toString());
        data.addProperty("status",      "pending");   // real impl would query DB
        data.addProperty("progress",    0);
        data.addProperty("tasks_done",  0);
        data.addProperty("tasks_total", 3);
        data.addProperty("xp_earned",   0);
        data.addProperty("queried_at",  Instant.now().toString());

        return successContent("Mission status retrieved.", data);
    }

    /** Mark a mission as completed and award XP. */
    private JsonElement completeMission(String userId, String missionId, String tier) {
        if (missionId == null || missionId.isBlank())
            return errorContent("MISSING_MISSION_ID",
                "mission_id is required for complete_mission");

        // Derive difficulty from mission_id if encoded (format: <userId>-<subject>-<difficulty>-<date>)
        String difficulty = extractDifficulty(missionId);
        int    xpEarned   = (int) (xpForDifficulty(difficulty) * xpMultiplier(tier));
        boolean streakBonus = isStreakDay();   // simplified check

        JsonObject data = new JsonObject();
        data.addProperty("mission_id",       missionId);
        data.addProperty("user_id",          userId);
        data.addProperty("status",           "completed");
        data.addProperty("xp_earned",        xpEarned);
        data.addProperty("streak_bonus",     streakBonus);
        data.addProperty("streak_xp",        streakBonus ? 50 : 0);
        data.addProperty("total_xp_awarded", xpEarned + (streakBonus ? 50 : 0));
        data.addProperty("completed_at",     Instant.now().toString());
        data.addProperty("next_mission_in",  hoursUntilMidnight() + " hours");
        data.add("rewards",                  buildRewards(tier, difficulty, streakBonus));

        return successContent("Mission completed! XP awarded.", data);
    }

    /** Get the streak data for a user. */
    private JsonElement getStreak(String userId) {
        JsonObject data = new JsonObject();
        data.addProperty("user_id",          userId);
        data.addProperty("current_streak",   7);    // stub — real impl reads DB
        data.addProperty("longest_streak",   21);
        data.addProperty("streak_protected", false);
        data.addProperty("streak_shield_count", 1);
        data.addProperty("last_active_date", LocalDate.now().minusDays(1).toString());
        data.addProperty("next_milestone",   10);
        data.addProperty("milestone_reward", "STREAK_MASTER_BADGE + 500 XP");
        data.addProperty("at_risk",          false);
        data.addProperty("queried_at",       Instant.now().toString());

        return successContent("Streak data retrieved.", data);
    }

    /** List all missions for a given day, optionally filtered. */
    private JsonElement listMissions(
            String userId, LocalDate date, String difficulty, String subject) {

        JsonArray missions = new JsonArray();
        // Generate representative sample missions
        String[] subjects = subject.equals("general")
            ? new String[]{"math", "coding", "language", "science", "art"}
            : new String[]{subject};

        for (String s : subjects) {
            JsonObject m = new JsonObject();
            String mId = buildMissionId(userId, s, difficulty, date);
            m.addProperty("mission_id",  mId);
            m.addProperty("subject",     s);
            m.addProperty("difficulty",  difficulty);
            m.addProperty("title",       missionTitle(s, difficulty));
            m.addProperty("xp_reward",   xpForDifficulty(difficulty));
            m.addProperty("status",      "pending");
            m.addProperty("expires_at",  date.plusDays(1).toString() + "T00:00:00Z");
            missions.add(m);
        }

        JsonObject data = new JsonObject();
        data.addProperty("user_id",  userId);
        data.addProperty("date",     date.toString());
        data.addProperty("count",    missions.size());
        data.add("missions",         missions);

        return successContent("Missions listed.", data);
    }

    /** Reset daily missions for a user (admin / test use). */
    private JsonElement resetDaily(String userId, LocalDate date) {
        JsonObject data = new JsonObject();
        data.addProperty("user_id",     userId);
        data.addProperty("date",        date.toString());
        data.addProperty("reset_at",    Instant.now().toString());
        data.addProperty("missions_cleared", 0);
        data.addProperty("status",      "reset_complete");

        return successContent("Daily missions reset.", data);
    }

    // =========================================================================
    // Helpers — content builders
    // =========================================================================

    private JsonArray buildTasks(String subject, String difficulty) {
        JsonArray tasks = new JsonArray();
        String[][] catalog = taskCatalog(subject, difficulty);
        for (String[] t : catalog) {
            JsonObject task = new JsonObject();
            task.addProperty("task_id",     UUID.randomUUID().toString().substring(0, 8));
            task.addProperty("description", t[0]);
            task.addProperty("type",        t[1]);
            task.addProperty("points",      Integer.parseInt(t[2]));
            tasks.add(task);
        }
        return tasks;
    }

    private JsonObject buildCriteria(String subject, String difficulty) {
        JsonObject c = new JsonObject();
        c.addProperty("min_tasks_required",  difficulty.equals("easy")  ? 2 : 3);
        c.addProperty("min_score_percent",   difficulty.equals("elite") ? 90 : 70);
        c.addProperty("time_limit_minutes",  timeLimitForDifficulty(difficulty));
        return c;
    }

    private JsonArray buildBadges(String subject, String tier) {
        JsonArray badges = new JsonArray();
        badges.add(subject.toUpperCase() + "_DAILY_COMPLETE");
        if (tier.equals("master")) badges.add("MASTER_MISSION_BADGE");
        return badges;
    }

    private JsonObject buildRewards(String tier, String difficulty, boolean streak) {
        JsonObject r = new JsonObject();
        r.addProperty("xp",     xpForDifficulty(difficulty));
        r.addProperty("coins",  coinsForDifficulty(difficulty));
        r.addProperty("badge",  difficulty.toUpperCase() + "_MISSION_COMPLETE");
        if (streak) r.addProperty("streak_badge", "STREAK_KEEPER");
        return r;
    }

    // =========================================================================
    // Helpers — mission metadata
    // =========================================================================

    private String missionTitle(String subject, String difficulty) {
        Map<String, String[]> titles = Map.of(
            "math",             new String[]{"Quick Calculations","Algebra Sprint","Equation Blitz","Math Olympiad Prep"},
            "science",          new String[]{"Lab Basics","Hypothesis Hunt","Experiment Design","Research Synthesis"},
            "coding",           new String[]{"Debug Run","Algorithm Warm-Up","Code Challenge","System Design Puzzle"},
            "language",         new String[]{"Word of the Day","Grammar Drill","Essay Spark","Literary Analysis"},
            "art",              new String[]{"Sketch Sprint","Color Theory","Creative Build","Master Composition"},
            "music",            new String[]{"Rhythm Practice","Scale Run","Improv Session","Composition Challenge"},
            "sports",           new String[]{"Warm-Up Circuit","Skill Drill","Endurance Push","Performance Peak"},
            "critical_thinking",new String[]{"Logic Puzzle","Debate Prep","Case Study","Strategic Breakdown"},
            "entrepreneurship", new String[]{"Idea Sketch","Pitch Prep","Market Research","Business Model Canvas"},
            "general",          new String[]{"Daily Explorer","Mixed Skills","Core Competency","All-Round Master"}
        );
        String[] t = titles.getOrDefault(subject, titles.get("general"));
        int idx = VALID_DIFFICULTIES.stream().toList().indexOf(difficulty);
        return t[Math.min(idx, t.length - 1)];
    }

    private String missionDescription(String subject, String difficulty) {
        return String.format(
            "Complete today's %s %s mission. Demonstrate your skills, earn XP, and keep your streak alive!",
            difficulty, subject.replace("_", " "));
    }

    private String[][] taskCatalog(String subject, String difficulty) {
        return new String[][]{
            {"Complete a 10-question quiz on " + subject,      "quiz",       "30"},
            {"Watch a 5-min explainer video on " + subject,    "video",      "20"},
            {"Submit a short written reflection (50+ words)",   "reflection", "25"}
        };
    }

    private int xpForDifficulty(String d) {
        return switch (d) {
            case "easy"  -> 100;
            case "medium"-> 200;
            case "hard"  -> 350;
            case "elite" -> 500;
            default      -> 200;
        };
    }

    private int coinsForDifficulty(String d) {
        return switch (d) {
            case "easy"  -> 10;
            case "medium"-> 20;
            case "hard"  -> 35;
            case "elite" -> 60;
            default      -> 20;
        };
    }

    private double xpMultiplier(String tier) {
        return switch (tier) {
            case "seed"   -> 1.0;
            case "sprout" -> 1.1;
            case "bloom"  -> 1.25;
            case "master" -> 1.5;
            default       -> 1.0;
        };
    }

    private int timeLimitForDifficulty(String d) {
        return switch (d) {
            case "easy"  -> 30;
            case "medium"-> 45;
            case "hard"  -> 60;
            case "elite" -> 90;
            default      -> 45;
        };
    }

    private String buildMissionId(String userId, String subject, String difficulty, LocalDate date) {
        return userId + "-" + subject + "-" + difficulty + "-" + date.toString();
    }

    private String extractDifficulty(String missionId) {
        String[] parts = missionId.split("-");
        // Format: <userId>-<subject>-<difficulty>-<date>
        // difficulty is parts[parts.length - 2 - 1] but varies; default medium
        for (String p : parts) {
            if (VALID_DIFFICULTIES.contains(p)) return p;
        }
        return "medium";
    }

    private boolean isStreakDay() {
        // Simplified: real impl checks DB for yesterday's completion
        return true;
    }

    private long hoursUntilMidnight() {
        LocalDateTime now       = LocalDateTime.now();
        LocalDateTime midnight  = now.toLocalDate().plusDays(1).atStartOfDay();
        return java.time.Duration.between(now, midnight).toHours();
    }

    // =========================================================================
    // Response envelope helpers
    // =========================================================================

    private JsonElement successContent(String message, JsonObject data) {
        JsonObject payload = new JsonObject();
        payload.addProperty("success",    true);
        payload.addProperty("agent",      "DAILY_MISSION_AGENT_ANTIGRAVITY");
        payload.addProperty("category",   "CAT-16 Dojo, Growth & School Ops");
        payload.addProperty("message",    message);
        payload.addProperty("timestamp",  Instant.now().toString());
        payload.add("data", data);

        JsonArray content = new JsonArray();
        JsonObject textBlock = new JsonObject();
        textBlock.addProperty("type", "text");
        textBlock.addProperty("text", new GsonBuilder().setPrettyPrinting().create().toJson(payload));
        content.add(textBlock);

        JsonObject result = new JsonObject();
        result.add("content", content);
        result.addProperty("isError", false);
        return result;
    }

    private JsonElement errorContent(String code, String message) {
        JsonObject payload = new JsonObject();
        payload.addProperty("success",   false);
        payload.addProperty("agent",     "DAILY_MISSION_AGENT_ANTIGRAVITY");
        payload.addProperty("error_code",code);
        payload.addProperty("message",   message);
        payload.addProperty("timestamp", Instant.now().toString());

        JsonArray content = new JsonArray();
        JsonObject textBlock = new JsonObject();
        textBlock.addProperty("type", "text");
        textBlock.addProperty("text", new GsonBuilder().setPrettyPrinting().create().toJson(payload));
        content.add(textBlock);

        JsonObject result = new JsonObject();
        result.add("content", content);
        result.addProperty("isError", true);
        return result;
    }

    // =========================================================================
    // Argument helpers
    // =========================================================================

    private String requireString(JsonObject args, String key) {
        if (!args.has(key) || args.get(key).isJsonNull())
            throw new McpServer.McpException(-32602,
                "Missing required parameter: " + key);
        return args.get(key).getAsString().trim();
    }

    private String optString(JsonObject args, String key, String defaultVal) {
        if (!args.has(key) || args.get(key).isJsonNull()) return defaultVal;
        String v = args.get(key).getAsString().trim();
        return v.isEmpty() ? defaultVal : v;
    }
}
