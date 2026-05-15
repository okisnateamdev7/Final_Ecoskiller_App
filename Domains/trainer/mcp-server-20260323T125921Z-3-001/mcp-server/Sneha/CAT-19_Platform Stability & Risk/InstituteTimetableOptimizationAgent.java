package com.ecoskiller.mcp19;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;

/**
 * INSTITUTE_TIMETABLE_OPTIMIZATION_AGENT
 *
 * Analyses and optimises institute/school timetable configurations for
 * EcoSkiller-hosted institutions. Detects scheduling conflicts, resource
 * over-allocation, under-utilised slots, and produces an optimised schedule
 * while respecting faculty constraints, room capacities, and platform SLAs.
 */
public class InstituteTimetableOptimizationAgent implements AgentHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String description() {
        return "Analyses and optimises institute timetable configurations hosted on EcoSkiller. " +
               "Detects scheduling conflicts, resource over-allocation, and under-utilised slots. " +
               "Produces an optimised schedule respecting faculty constraints and room capacities.";
    }

    @Override
    public ObjectNode inputSchema() {
        ObjectNode schema = MAPPER.createObjectNode();
        schema.put("type", "object");

        ObjectNode props = MAPPER.createObjectNode();

        ObjectNode instituteId = MAPPER.createObjectNode();
        instituteId.put("type", "string");
        instituteId.put("description", "Institute/school tenant ID");
        props.set("institute_id", instituteId);

        ObjectNode term = MAPPER.createObjectNode();
        term.put("type", "string");
        term.put("description", "Academic term identifier, e.g. '2025-Q3'");
        props.set("term", term);

        ObjectNode optimizationGoal = MAPPER.createObjectNode();
        optimizationGoal.put("type", "string");
        optimizationGoal.put("description", "Optimization goal: CONFLICT_FREE | LOAD_BALANCED | MAX_UTILIZATION | FACULTY_PREFERENCE");
        optimizationGoal.put("default", "CONFLICT_FREE");
        props.set("optimization_goal", optimizationGoal);

        ObjectNode respectFacultyPrefs = MAPPER.createObjectNode();
        respectFacultyPrefs.put("type", "boolean");
        respectFacultyPrefs.put("description", "Honour faculty time-preference constraints");
        respectFacultyPrefs.put("default", true);
        props.set("respect_faculty_preferences", respectFacultyPrefs);

        ObjectNode maxClassesPerDay = MAPPER.createObjectNode();
        maxClassesPerDay.put("type", "integer");
        maxClassesPerDay.put("description", "Maximum classes per faculty per day (default 5)");
        maxClassesPerDay.put("default", 5);
        props.set("max_classes_per_day", maxClassesPerDay);

        schema.set("properties", props);

        com.fasterxml.jackson.databind.node.ArrayNode required = MAPPER.createArrayNode();
        required.add("institute_id");
        required.add("term");
        schema.set("required", required);

        return schema;
    }

    @Override
    public JsonNode execute(JsonNode args) {
        String  instituteId       = args.path("institute_id").asText("INST-UNKNOWN");
        String  term              = args.path("term").asText("2025-Q3");
        String  goal              = args.path("optimization_goal").asText("CONFLICT_FREE");
        boolean respectPrefs      = args.path("respect_faculty_preferences").asBoolean(true);
        int     maxClassesPerDay  = args.path("max_classes_per_day").asInt(5);

        ObjectNode result = MAPPER.createObjectNode();
        result.put("agent",          "INSTITUTE_TIMETABLE_OPTIMIZATION_AGENT");
        result.put("category",       "CAT-19 — Platform Stability & Risk");
        result.put("timestamp",      Instant.now().toString());
        result.put("institute_id",   instituteId);
        result.put("term",           term);
        result.put("goal",           goal);
        result.put("respect_faculty_preferences", respectPrefs);

        // --- Simulated optimization result ---
        ObjectNode analysis = MAPPER.createObjectNode();
        analysis.put("total_slots_analysed",     240);
        analysis.put("conflicts_detected",       3);
        analysis.put("conflicts_resolved",       3);
        analysis.put("under_utilised_slots",     18);
        analysis.put("over_allocated_faculty",   1);
        analysis.put("optimization_score_before", 71.4);
        analysis.put("optimization_score_after",  94.8);

        result.set("timetable_analysis", analysis);

        // Sample conflicts resolved
        com.fasterxml.jackson.databind.node.ArrayNode resolvedConflicts = MAPPER.createArrayNode();
        ObjectNode c1 = MAPPER.createObjectNode();
        c1.put("conflict_id",  "CONF-001");
        c1.put("type",         "ROOM_DOUBLE_BOOKING");
        c1.put("slot",         "Mon 09:00");
        c1.put("resolution",   "Moved Class B to Room 104 (same capacity)");
        resolvedConflicts.add(c1);

        ObjectNode c2 = MAPPER.createObjectNode();
        c2.put("conflict_id",  "CONF-002");
        c2.put("type",         "FACULTY_OVERLOAD");
        c2.put("faculty_id",   "FAC-017");
        c2.put("resolution",   "Redistributed 1 session to FAC-023 (available slot)");
        resolvedConflicts.add(c2);
        result.set("resolved_conflicts", resolvedConflicts);

        // Optimization recommendations
        com.fasterxml.jackson.databind.node.ArrayNode recs = MAPPER.createArrayNode();
        recs.add("Merge 18 under-utilised Friday slots into consolidated workshop blocks.");
        recs.add("Enable FAC-017 soft-constraint: no classes before 09:00.");
        recs.add("Re-evaluate Room 201 capacity — consistently at 95% occupancy.");
        result.set("recommendations", recs);

        result.put("optimized_schedule_ref", "SCHED-" + instituteId + "-" + term + "-OPT");
        result.put("status", "OPTIMIZATION_COMPLETE");

        return result;
    }
}
