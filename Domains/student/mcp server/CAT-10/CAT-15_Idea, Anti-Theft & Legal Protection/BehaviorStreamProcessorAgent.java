package com.ecoskiller.mcp15;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

import static com.ecoskiller.mcp15.AgentUtils.*;

/**
 * BEHAVIOR_STREAM_PROCESSOR
 *
 * Analyses a stream of user behaviour events (views, downloads, copies, exports)
 * and produces a risk-scored profile to detect theft or misuse of protected ideas.
 */
public class BehaviorStreamProcessorAgent implements AgentHandler {

    @Override
    public ObjectNode toolDefinition(String toolName) {
        ObjectNode schema = schemaObject("user_id", "events_json");
        addStringProp(schema, "user_id",
            "The user whose behaviour is being analysed");
        addStringProp(schema, "events_json",
            "JSON array of event objects, each with: {type, idea_id, timestamp}. " +
            "Event types: VIEW | DOWNLOAD | COPY | EXPORT | SHARE | PRINT | SCREENSHOT");
        addStringProp(schema, "analysis_window",
            "Time window label: LAST_HOUR | LAST_DAY | LAST_WEEK | LAST_MONTH (default LAST_DAY)");
        addStringProp(schema, "protected_idea_ids",
            "Comma-separated IDs of ideas that are under IP protection (optional)");

        return buildToolDef(
            toolName,
            "BEHAVIOR_STREAM_PROCESSOR — Processes a user's event stream to detect suspicious " +
            "behaviour patterns (bulk downloads, repeat copies, export spikes) that may indicate " +
            "intentional IP theft. Returns a risk profile and flagged events.",
            schema
        );
    }

    @Override
    public ObjectNode execute(JsonNode args) throws Exception {
        String userId      = args.path("user_id").asText();
        String eventsJson  = args.path("events_json").asText("[]");
        String window      = args.path("analysis_window").asText("LAST_DAY");
        String protectedRaw = args.path("protected_idea_ids").asText("");

        // Parse events
        JsonNode eventsNode;
        try {
            eventsNode = MAPPER.readTree(eventsJson);
        } catch (Exception e) {
            eventsNode = MAPPER.createArrayNode();
        }

        Set<String> protectedIds = new HashSet<>();
        if (!protectedRaw.isBlank()) {
            for (String id : protectedRaw.split(",")) {
                String t = id.trim();
                if (!t.isEmpty()) protectedIds.add(t);
            }
        }

        // Tally event types
        Map<String, Integer> typeCounts = new LinkedHashMap<>();
        Map<String, Integer> ideaCounts = new LinkedHashMap<>();
        int totalEvents = 0;
        int protectedAccesses = 0;

        if (eventsNode.isArray()) {
            for (JsonNode ev : eventsNode) {
                String type   = ev.path("type").asText("UNKNOWN").toUpperCase();
                String ideaId = ev.path("idea_id").asText("");
                typeCounts.merge(type, 1, Integer::sum);
                if (!ideaId.isBlank()) ideaCounts.merge(ideaId, 1, Integer::sum);
                if (protectedIds.contains(ideaId)) protectedAccesses++;
                totalEvents++;
            }
        }

        // Risk scoring
        int downloadCount    = typeCounts.getOrDefault("DOWNLOAD",    0);
        int copyCount        = typeCounts.getOrDefault("COPY",        0);
        int exportCount      = typeCounts.getOrDefault("EXPORT",      0);
        int screenshotCount  = typeCounts.getOrDefault("SCREENSHOT",  0);
        int shareCount       = typeCounts.getOrDefault("SHARE",       0);

        double riskScore = 0;
        riskScore += Math.min(downloadCount   * 0.15, 0.25);
        riskScore += Math.min(copyCount       * 0.20, 0.30);
        riskScore += Math.min(exportCount     * 0.18, 0.25);
        riskScore += Math.min(screenshotCount * 0.12, 0.15);
        riskScore += Math.min(shareCount      * 0.10, 0.15);
        riskScore += Math.min(protectedAccesses * 0.10, 0.20);
        riskScore = Math.min(riskScore, 1.0);

        String riskLevel;
        if (riskScore >= 0.80)      riskLevel = "CRITICAL";
        else if (riskScore >= 0.60) riskLevel = "HIGH";
        else if (riskScore >= 0.40) riskLevel = "MEDIUM";
        else if (riskScore >= 0.20) riskLevel = "LOW";
        else                        riskLevel  = "NORMAL";

        // Flagged patterns
        ArrayNode flags = MAPPER.createArrayNode();
        if (copyCount > 3)        flags.add("BULK_COPY_DETECTED (" + copyCount + " events)");
        if (downloadCount > 5)    flags.add("MASS_DOWNLOAD_DETECTED (" + downloadCount + " events)");
        if (exportCount > 2)      flags.add("REPEATED_EXPORT (" + exportCount + " events)");
        if (screenshotCount > 2)  flags.add("SCREENSHOT_SPIKE (" + screenshotCount + " events)");
        if (protectedAccesses > 0) flags.add("PROTECTED_CONTENT_ACCESSED (" + protectedAccesses + " times)");

        // Build result
        ObjectNode result = MAPPER.createObjectNode();
        result.put("agent",           "BEHAVIOR_STREAM_PROCESSOR");
        result.put("status",          "PROCESSED");
        result.put("user_id",         userId);
        result.put("analysis_window", window);
        result.put("timestamp",       now());
        result.put("risk_level",      riskLevel);
        result.put("risk_score",      Math.round(riskScore * 1000.0) / 1000.0);
        result.put("total_events",    totalEvents);

        ObjectNode eventBreakdown = result.putObject("event_breakdown");
        for (Map.Entry<String, Integer> e : typeCounts.entrySet()) {
            eventBreakdown.put(e.getKey().toLowerCase(), e.getValue());
        }
        eventBreakdown.put("protected_accesses", protectedAccesses);

        ObjectNode topIdeas = result.putObject("top_accessed_ideas");
        ideaCounts.entrySet().stream()
            .sorted((a, b) -> b.getValue() - a.getValue())
            .limit(5)
            .forEach(e -> topIdeas.put(e.getKey(), e.getValue()));

        result.set("flagged_patterns", flags);

        ObjectNode recommend = result.putObject("recommendations");
        recommend.put("action",               riskScore >= 0.60 ? "FREEZE_USER_ACCESS" :
                                              riskScore >= 0.40 ? "TRIGGER_SECONDARY_REVIEW" :
                                                                   "CONTINUE_MONITORING");
        recommend.put("notify_ip_team",       riskScore >= 0.60);
        recommend.put("generate_audit_trail", riskScore >= 0.40);

        return result;
    }
}
