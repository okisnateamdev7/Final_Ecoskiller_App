package com.ecoskiller.dojo.agents;

import com.ecoskiller.dojo.security.SecurityManager;
import com.google.gson.*;

import java.util.*;

/**
 * FIND_MATCH_AGENT — Core real-time matching algorithm.
 *
 * Algorithm:
 *   1. Filter available interviewers (capacity check)
 *   2. Filter by skill compatibility (candidate level <= interviewer level)
 *   3. Filter by timezone working hours (08:00–18:00 interviewer local time)
 *   4. Filter by fairness (no repeat interviewer)
 *   5. Score: score = (skill_match*0.6) + (fairness_bonus*0.3) + (utilization*0.1)
 *   6. Return top match or fallback
 *
 * Latency target: <5 seconds (99th percentile)
 */
public class FindMatchAgent extends BaseAgent {

    // Skill levels ordered for comparison
    private static final Map<String, Integer> SKILL_RANK = Map.of(
        "BEGINNER", 1, "INTERMEDIATE", 2, "ADVANCED", 3, "EXPERT", 4
    );

    public FindMatchAgent(SecurityManager security) { super(security); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject s = schema("candidate_id", "assessment_id", "skill_level", "preferred_language", "timezone");
        addStringProp(s, "candidate_id",        "Unique candidate identifier (UUID format)");
        addStringProp(s, "assessment_id",        "Assessment session ID to match for");
        addEnumProp  (s, "skill_level",          "Candidate skill proficiency level",
                       "BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT");
        addStringProp(s, "preferred_language",   "Primary programming language (e.g. Java, Python, Go)");
        addStringProp(s, "timezone",             "Candidate timezone (e.g. Asia/Kolkata, America/New_York)");
        addIntProp   (s, "deadline_ms",          "Max time in ms to find match before timeout (default: 5000)");
        addBoolProp  (s, "allow_fallback",       "Allow fallback matching if no perfect match found");
        return buildToolDef(
            "find_match",
            "Core matching algorithm: finds the optimal interviewer for a waiting candidate. " +
            "Applies skill filtering, timezone checking, fairness optimization, and " +
            "confidence scoring. Returns match object with score, confidence level, and session URL. " +
            "Target latency: <5s (99th percentile).",
            s
        );
    }

    @Override
    public JsonObject execute(JsonObject args) {
        try {
            security.checkRateLimit("find_match");

            String candidateId  = str(args, "candidate_id");
            String assessmentId = str(args, "assessment_id");
            String skillLevel   = str(args, "skill_level");
            String language     = str(args, "preferred_language");
            String timezone     = str(args, "timezone");
            int    deadlineMs   = num(args, "deadline_ms", 5000);
            boolean allowFallback = args.has("allow_fallback") && args.get("allow_fallback").getAsBoolean();

            if (candidateId.isEmpty() || skillLevel.isEmpty()) {
                return error("candidate_id and skill_level are required");
            }

            long startMs = System.currentTimeMillis();

            // Simulate interviewer pool with deterministic demo data
            List<JsonObject> pool = buildSimulatedInterviewerPool(language);

            // Step 1: Availability filter
            List<JsonObject> available = pool.stream()
                .filter(i -> i.get("current_interviews").getAsInt() < i.get("max_concurrent").getAsInt())
                .filter(i -> i.get("interviews_today").getAsInt() < i.get("max_per_day").getAsInt())
                .toList();

            // Step 2: Skill filter
            int candidateRank = SKILL_RANK.getOrDefault(skillLevel, 0);
            List<JsonObject> skillFiltered = available.stream()
                .filter(i -> {
                    String iSkill = i.get("skill_" + language.toLowerCase()).getAsString();
                    int iRank = SKILL_RANK.getOrDefault(iSkill, 0);
                    return iRank >= candidateRank; // interviewer must be >= candidate level
                })
                .toList();

            // Step 3: Timezone filter (08:00–18:00 interviewer local time)
            List<JsonObject> tzFiltered = skillFiltered.stream()
                .filter(i -> isValidInterviewTime(i.get("timezone").getAsString()))
                .toList();

            // Step 4: Fairness filter (no repeated interviewer for same candidate)
            List<JsonObject> fairFiltered = tzFiltered.stream()
                .filter(i -> !i.get("previous_candidates").getAsString().contains(candidateId))
                .toList();

            // Step 5: Score
            List<JsonObject> pool2score = fairFiltered.isEmpty() && allowFallback ? tzFiltered : fairFiltered;
            if (pool2score.isEmpty()) {
                return buildNoMatchResponse(candidateId, deadlineMs, allowFallback);
            }

            JsonObject best = null;
            double bestScore = -1;
            for (JsonObject interviewer : pool2score) {
                double score = computeScore(interviewer, candidateRank, language);
                if (score > bestScore) {
                    bestScore = score;
                    best = interviewer;
                }
            }

            long elapsed = System.currentTimeMillis() - startMs;

            return buildMatchResponse(candidateId, assessmentId, best, bestScore, elapsed, allowFallback && fairFiltered.isEmpty());

        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            return error("FindMatch failed: " + e.getMessage());
        }
    }

    private double computeScore(JsonObject interviewer, int candidateRank, String language) {
        // skill_match_ratio: how closely does interviewer skill match candidate (closer = better)
        String iSkillStr = interviewer.get("skill_" + language.toLowerCase()).getAsString();
        int iRank = SKILL_RANK.getOrDefault(iSkillStr, 1);
        double skillDelta = iRank - candidateRank; // 0 = perfect match, higher = overqualified
        double skillMatchRatio = 1.0 - Math.min(skillDelta / 3.0, 0.5); // slight penalty for overqualification

        // fairness_bonus: fewer interviews today → higher bonus
        int interviewsToday = interviewer.get("interviews_today").getAsInt();
        int maxPerDay = interviewer.get("max_per_day").getAsInt();
        double fairnessBonus = 1.0 - ((double) interviewsToday / maxPerDay);

        // utilization_priority: fewer current concurrent → higher priority
        int current = interviewer.get("current_interviews").getAsInt();
        int maxConcurrent = interviewer.get("max_concurrent").getAsInt();
        double utilizationPriority = 1.0 - ((double) current / maxConcurrent);

        return (skillMatchRatio * 0.6) + (fairnessBonus * 0.3) + (utilizationPriority * 0.1);
    }

    private boolean isValidInterviewTime(String timezone) {
        // Simulate local hour check — in production use java.time.ZoneId
        int simulatedHour = simulateLocalHour(timezone);
        return simulatedHour >= 8 && simulatedHour < 18;
    }

    private int simulateLocalHour(String timezone) {
        // Demo: map timezones to a plausible hour for demonstration
        java.time.ZoneId zone;
        try {
            zone = java.time.ZoneId.of(timezone);
        } catch (Exception e) {
            zone = java.time.ZoneId.of("UTC");
        }
        return java.time.ZonedDateTime.now(zone).getHour();
    }

    private List<JsonObject> buildSimulatedInterviewerPool(String language) {
        // Deterministic demo pool
        List<JsonObject> pool = new ArrayList<>();
        String lang = language.toLowerCase();

        String[][] data = {
            {"IV-001", "Alice Chen",    "ADVANCED", "Asia/Kolkata",       "2", "3", "8", ""},
            {"IV-002", "Bob Martinez",  "INTERMEDIATE", "America/New_York","1", "3", "8", ""},
            {"IV-003", "Carol Singh",   "EXPERT",   "Europe/London",       "0", "2", "6", ""},
            {"IV-004", "David Kim",     "ADVANCED", "Asia/Tokyo",          "3", "3", "8", "prev-cand-X"},
            {"IV-005", "Eva Rodriguez", "INTERMEDIATE", "America/Chicago", "0", "3", "8", ""},
        };

        for (String[] d : data) {
            JsonObject iv = new JsonObject();
            iv.addProperty("id",                 d[0]);
            iv.addProperty("name",               d[1]);
            iv.addProperty("skill_" + lang,      d[2]);
            iv.addProperty("timezone",           d[3]);
            iv.addProperty("current_interviews", Integer.parseInt(d[4]));
            iv.addProperty("max_concurrent",     Integer.parseInt(d[5]));
            iv.addProperty("max_per_day",        Integer.parseInt(d[6]));
            iv.addProperty("interviews_today",   Integer.parseInt(d[4]) + 1);
            iv.addProperty("previous_candidates", d[7]);
            pool.add(iv);
        }
        return pool;
    }

    private JsonObject buildMatchResponse(String candidateId, String assessmentId,
                                          JsonObject interviewer, double score,
                                          long elapsedMs, boolean isFallback) {
        String matchId = "MATCH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String sessionUrl = "wss://dojo.ecoskiller.io/session/" + matchId;

        String confidence = score >= 0.9 ? "HIGH" : score >= 0.7 ? "MEDIUM" : "LOW";

        JsonObject result = new JsonObject();
        result.addProperty("match_id",           matchId);
        result.addProperty("candidate_id",       candidateId);
        result.addProperty("assessment_id",      assessmentId);
        result.addProperty("interviewer_id",     interviewer.get("id").getAsString());
        result.addProperty("interviewer_name",   interviewer.get("name").getAsString());
        result.addProperty("match_score",        Math.round(score * 100));
        result.addProperty("confidence",         confidence);
        result.addProperty("session_url",        sessionUrl);
        result.addProperty("scheduled_start",    java.time.Instant.now().plusSeconds(5).toString());
        result.addProperty("latency_ms",         elapsedMs);
        result.addProperty("is_fallback_match",  isFallback);
        result.addProperty("kafka_topic",        "dojo.match.created");

        return success(result);
    }

    private JsonObject buildNoMatchResponse(String candidateId, int deadlineMs, boolean fallbackEnabled) {
        JsonObject result = new JsonObject();
        result.addProperty("candidate_id",     candidateId);
        result.addProperty("error_code",       "UNAVAILABLE_INTERVIEWER");
        result.addProperty("error",            "No suitable interviewer available within deadline");
        result.addProperty("deadline_ms",      deadlineMs);
        result.addProperty("fallback_enabled", fallbackEnabled);
        result.addProperty("action",           "Candidate re-queued for next available match cycle");
        result.addProperty("retry_after_ms",   5000);
        return result;
    }
}
