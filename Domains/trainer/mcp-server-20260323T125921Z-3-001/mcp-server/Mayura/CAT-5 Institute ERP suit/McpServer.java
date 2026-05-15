package ecoskiller;

import java.io.*;
import java.util.*;
import java.time.Instant;

/**
 * Ecoskiller | CAT-5 — Institute ERP Suite
 * MCP Server in Java | 12 Agents | Priority: HIGH
 *
 * Agents:
 *   5.  ACADEMIC_STRUCTURE_AGENT__ANTIGRAVITY_SEALED
 *   6.  ATTENDANCE_AGENT__ANTIGRAVITY_SEALED
 *   7.  CAMPUS_AGENT
 *   8.  DATA_NORMALIZATION_AGENT__INSTITUTE_ERP__ANTIGRAVITY
 *   9.  EXAM_ENGINE_AGENT__ANTIGRAVITY_SEALED
 *   10. FACULTY_MANAGEMENT_AGENT__ANTIGRAVITY_SEALED
 *   11. FEE_MANAGEMENT_AGENT
 *   12. LMS_MIGRATION_AGENT
 *   13. PLAGIARISM_CONNECT_AGENT
 *   14. STUDENT_RECORD_AGENT_COMPLETE
 *   15. TAXONOMY_MIGRATION_AGENT__INSTITUTE_ERP__ANTIGRAVITY
 *   16. TRANSPORT_AGENT
 *
 * Protocol : JSON-RPC 2.0 over stdio | MCP Version: 2024-11-05
 */
public class McpServer {

    // ─────────────────────────────────────────────────────────
    //  Tool registry
    // ─────────────────────────────────────────────────────────
    private static final List<Map<String, Object>> TOOLS = new ArrayList<>();

    static {
        TOOLS.add(buildTool(
            "academic_structure",
            "ACADEMIC_STRUCTURE_AGENT__ANTIGRAVITY_SEALED",
            "Manages academic hierarchy: departments, programs, courses, semesters, credit structures, and curriculum versioning for the institute ERP.",
            new String[]{"action", "department_id", "program_id", "course_id", "semester"}
        ));

        TOOLS.add(buildTool(
            "attendance",
            "ATTENDANCE_AGENT__ANTIGRAVITY_SEALED",
            "Tracks student and faculty attendance, generates shortage alerts, computes attendance percentages, and syncs with biometric/RFID systems.",
            new String[]{"action", "student_id", "faculty_id", "course_id", "date", "session"}
        ));

        TOOLS.add(buildTool(
            "campus",
            "CAMPUS_AGENT",
            "Manages campus infrastructure: buildings, classrooms, labs, hostels, canteen, library, and facility booking/allocation.",
            new String[]{"action", "facility_type", "facility_id", "booking_date", "capacity"}
        ));

        TOOLS.add(buildTool(
            "data_normalization",
            "DATA_NORMALIZATION_AGENT__INSTITUTE_ERP__ANTIGRAVITY",
            "Cleanses, deduplicates, and normalises data ingested from legacy ERP systems, CSV imports, and third-party integrations before loading into Ecoskiller.",
            new String[]{"action", "source_system", "entity_type", "batch_id", "rules_profile"}
        ));

        TOOLS.add(buildTool(
            "exam_engine",
            "EXAM_ENGINE_AGENT__ANTIGRAVITY_SEALED",
            "Handles end-to-end exam lifecycle: scheduling, seating allocation, question-paper generation, online/offline proctoring, result processing, and grade publishing.",
            new String[]{"action", "exam_id", "course_id", "student_id", "exam_type", "scheduled_date"}
        ));

        TOOLS.add(buildTool(
            "faculty_management",
            "FACULTY_MANAGEMENT_AGENT__ANTIGRAVITY_SEALED",
            "Manages faculty profiles, workload allocation, timetable generation, appraisal cycles, leave management, and payroll linkage.",
            new String[]{"action", "faculty_id", "department_id", "workload_type", "period"}
        ));

        TOOLS.add(buildTool(
            "fee_management",
            "FEE_MANAGEMENT_AGENT",
            "Handles student fee structures, instalment scheduling, payment collection (online/offline), receipt generation, defaulter tracking, and scholarship adjustments.",
            new String[]{"action", "student_id", "fee_type", "amount", "due_date", "payment_mode"}
        ));

        TOOLS.add(buildTool(
            "lms_migration",
            "LMS_MIGRATION_AGENT",
            "Migrates course content, quizzes, assignments, and student progress data from legacy LMS platforms (Moodle, Blackboard, Canvas) into Ecoskiller LMS.",
            new String[]{"action", "source_lms", "course_id", "migration_mode", "target_env"}
        ));

        TOOLS.add(buildTool(
            "plagiarism_connect",
            "PLAGIARISM_CONNECT_AGENT",
            "Integrates with plagiarism detection engines (Turnitin, iThenticate, Unicheck) to submit documents, retrieve similarity scores, and enforce academic integrity policies.",
            new String[]{"action", "document_id", "student_id", "submission_type", "engine"}
        ));

        TOOLS.add(buildTool(
            "student_record",
            "STUDENT_RECORD_AGENT_COMPLETE",
            "Maintains complete student lifecycle records: admission, enrollment, academic history, awards, disciplinary records, and alumni transition.",
            new String[]{"action", "student_id", "record_type", "academic_year", "status"}
        ));

        TOOLS.add(buildTool(
            "taxonomy_migration",
            "TAXONOMY_MIGRATION_AGENT__INSTITUTE_ERP__ANTIGRAVITY",
            "Maps and migrates taxonomy structures (subject codes, grade scales, competency frameworks, Bloom's taxonomy levels) across ERP systems.",
            new String[]{"action", "source_taxonomy", "target_taxonomy", "entity_type", "mapping_version"}
        ));

        TOOLS.add(buildTool(
            "transport",
            "TRANSPORT_AGENT",
            "Manages institute transport: route planning, bus allocation, GPS tracking, student bus-pass issuance, driver records, and fuel/maintenance logs.",
            new String[]{"action", "route_id", "vehicle_id", "student_id", "trip_date", "stop_name"}
        ));
    }

    // ─────────────────────────────────────────────────────────
    //  Entry point
    // ─────────────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

        log("CAT-5 Institute ERP Suite MCP Server started (12 agents)");

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String response = handle(line);
                if (response != null) out.println(response);
            } catch (Exception e) {
                log("Error: " + e.getMessage());
                out.println(errorResponse(null, -32700, "Parse error: " + e.getMessage()));
            }
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Router
    // ─────────────────────────────────────────────────────────
    private static String handle(String raw) {
        String id     = extractString(raw, "\"id\"");
        String method = extractString(raw, "\"method\"");
        if (method == null) return errorResponse(id, -32600, "Invalid Request");

        switch (method) {
            case "initialize": return handleInitialize(id);
            case "tools/list": return handleToolsList(id);
            case "tools/call": return handleToolsCall(id, raw);
            case "ping":       return result(id, "{\"status\":\"pong\"}");
            default:           return errorResponse(id, -32601, "Method not found: " + method);
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Handlers
    // ─────────────────────────────────────────────────────────
    private static String handleInitialize(String id) {
        return result(id,
            "{\"protocolVersion\":\"2024-11-05\"," +
            "\"serverInfo\":{\"name\":\"mcp-05-institute-erp\",\"version\":\"1.0.0\"}," +
            "\"capabilities\":{\"tools\":{}}}");
    }

    private static String handleToolsList(String id) {
        StringBuilder sb = new StringBuilder("{\"tools\":[");
        for (int i = 0; i < TOOLS.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toolToJson(TOOLS.get(i)));
        }
        sb.append("]}");
        return result(id, sb.toString());
    }

    private static String handleToolsCall(String id, String raw) {
        String toolName = extractString(raw, "\"name\"");
        if (toolName == null) return errorResponse(id, -32602, "Missing tool name");

        Map<String, Object> tool = null;
        for (Map<String, Object> t : TOOLS) {
            if (t.get("name").equals(toolName)) { tool = t; break; }
        }
        if (tool == null) return errorResponse(id, -32602, "Unknown tool: " + toolName);

        String agentResult = dispatchAgent(toolName, raw);
        String content = "{\"type\":\"text\",\"text\":" + jsonString(agentResult) + "}";
        return result(id, "{\"content\":[" + content + "]}");
    }

    // ─────────────────────────────────────────────────────────
    //  Agent dispatcher
    // ─────────────────────────────────────────────────────────
    private static String dispatchAgent(String toolName, String raw) {
        String action = extractString(raw, "\"action\"");
        if (action == null) action = "status";
        String ts = Instant.now().toString();

        switch (toolName) {
            case "academic_structure": return academicStructureAgent(action, raw, ts);
            case "attendance":         return attendanceAgent(action, raw, ts);
            case "campus":             return campusAgent(action, raw, ts);
            case "data_normalization": return dataNormalizationAgent(action, raw, ts);
            case "exam_engine":        return examEngineAgent(action, raw, ts);
            case "faculty_management": return facultyManagementAgent(action, raw, ts);
            case "fee_management":     return feeManagementAgent(action, raw, ts);
            case "lms_migration":      return lmsMigrationAgent(action, raw, ts);
            case "plagiarism_connect": return plagiarismConnectAgent(action, raw, ts);
            case "student_record":     return studentRecordAgent(action, raw, ts);
            case "taxonomy_migration": return taxonomyMigrationAgent(action, raw, ts);
            case "transport":          return transportAgent(action, raw, ts);
            default:                   return "{\"error\":\"Unknown agent\"}";
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Agent implementations
    // ─────────────────────────────────────────────────────────

    private static String academicStructureAgent(String action, String raw, String ts) {
        String deptId    = def(extractString(raw, "\"department_id\""), "DEPT-001");
        String programId = def(extractString(raw, "\"program_id\""),    "PROG-CS");
        String courseId  = def(extractString(raw, "\"course_id\""),     "CS-101");
        String semester  = def(extractString(raw, "\"semester\""),      "SEM-1");
        return "{" +
            "\"agent\":\"ACADEMIC_STRUCTURE_AGENT__ANTIGRAVITY_SEALED\"," +
            "\"action\":\"" + action + "\"," +
            "\"department_id\":\"" + deptId + "\"," +
            "\"program_id\":\"" + programId + "\"," +
            "\"course_id\":\"" + courseId + "\"," +
            "\"semester\":\"" + semester + "\"," +
            "\"total_credits\":120," +
            "\"electives_available\":18," +
            "\"curriculum_version\":\"v3.2\"," +
            "\"accreditation_status\":\"NBA_APPROVED\"," +
            "\"outcome_based_education\":true," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String attendanceAgent(String action, String raw, String ts) {
        String studentId = def(extractString(raw, "\"student_id\""), "STU-0000");
        String facultyId = def(extractString(raw, "\"faculty_id\""), "FAC-0000");
        String courseId  = def(extractString(raw, "\"course_id\""),  "CS-101");
        String date      = def(extractString(raw, "\"date\""),       ts.substring(0, 10));
        String session   = def(extractString(raw, "\"session\""),    "MORNING");
        return "{" +
            "\"agent\":\"ATTENDANCE_AGENT__ANTIGRAVITY_SEALED\"," +
            "\"action\":\"" + action + "\"," +
            "\"student_id\":\"" + studentId + "\"," +
            "\"faculty_id\":\"" + facultyId + "\"," +
            "\"course_id\":\"" + courseId + "\"," +
            "\"date\":\"" + date + "\"," +
            "\"session\":\"" + session + "\"," +
            "\"attendance_percentage\":82.5," +
            "\"shortage_alert\":false," +
            "\"biometric_synced\":true," +
            "\"rfid_verified\":true," +
            "\"leave_adjusted\":false," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String campusAgent(String action, String raw, String ts) {
        String facilityType = def(extractString(raw, "\"facility_type\""), "CLASSROOM");
        String facilityId   = def(extractString(raw, "\"facility_id\""),   "ROOM-A101");
        String bookingDate  = def(extractString(raw, "\"booking_date\""),  ts.substring(0, 10));
        String capacity     = def(extractString(raw, "\"capacity\""),      "60");
        return "{" +
            "\"agent\":\"CAMPUS_AGENT\"," +
            "\"action\":\"" + action + "\"," +
            "\"facility_type\":\"" + facilityType + "\"," +
            "\"facility_id\":\"" + facilityId + "\"," +
            "\"booking_date\":\"" + bookingDate + "\"," +
            "\"capacity\":" + capacity + "," +
            "\"available\":true," +
            "\"smart_board\":true," +
            "\"ac_enabled\":true," +
            "\"wifi_zone\":\"ZONE-A\"," +
            "\"booking_confirmed\":true," +
            "\"qr_token\":\"QR-" + facilityId + "-" + bookingDate + "\"," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String dataNormalizationAgent(String action, String raw, String ts) {
        String sourceSystem = def(extractString(raw, "\"source_system\""), "LEGACY-ERP");
        String entityType   = def(extractString(raw, "\"entity_type\""),   "STUDENT");
        String batchId      = def(extractString(raw, "\"batch_id\""),      "BATCH-001");
        String rulesProfile = def(extractString(raw, "\"rules_profile\""), "DEFAULT");
        return "{" +
            "\"agent\":\"DATA_NORMALIZATION_AGENT__INSTITUTE_ERP__ANTIGRAVITY\"," +
            "\"action\":\"" + action + "\"," +
            "\"source_system\":\"" + sourceSystem + "\"," +
            "\"entity_type\":\"" + entityType + "\"," +
            "\"batch_id\":\"" + batchId + "\"," +
            "\"rules_profile\":\"" + rulesProfile + "\"," +
            "\"records_processed\":4850," +
            "\"duplicates_removed\":37," +
            "\"null_fields_imputed\":112," +
            "\"format_corrections\":290," +
            "\"validation_pass_rate\":0.994," +
            "\"output_schema\":\"ECOSKILLER_V2\"," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String examEngineAgent(String action, String raw, String ts) {
        String examId    = def(extractString(raw, "\"exam_id\""),       "EXAM-2025-01");
        String courseId  = def(extractString(raw, "\"course_id\""),     "CS-101");
        String studentId = def(extractString(raw, "\"student_id\""),    "STU-0000");
        String examType  = def(extractString(raw, "\"exam_type\""),     "INTERNAL");
        String schedDate = def(extractString(raw, "\"scheduled_date\""),ts.substring(0, 10));
        return "{" +
            "\"agent\":\"EXAM_ENGINE_AGENT__ANTIGRAVITY_SEALED\"," +
            "\"action\":\"" + action + "\"," +
            "\"exam_id\":\"" + examId + "\"," +
            "\"course_id\":\"" + courseId + "\"," +
            "\"student_id\":\"" + studentId + "\"," +
            "\"exam_type\":\"" + examType + "\"," +
            "\"scheduled_date\":\"" + schedDate + "\"," +
            "\"seating_allocated\":true," +
            "\"question_paper_generated\":true," +
            "\"online_proctoring\":true," +
            "\"result_processed\":false," +
            "\"grade_published\":false," +
            "\"malpractice_flags\":0," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String facultyManagementAgent(String action, String raw, String ts) {
        String facultyId   = def(extractString(raw, "\"faculty_id\""),    "FAC-0001");
        String deptId      = def(extractString(raw, "\"department_id\""), "DEPT-CS");
        String workload    = def(extractString(raw, "\"workload_type\""), "TEACHING");
        String period      = def(extractString(raw, "\"period\""),        "AY-2025");
        return "{" +
            "\"agent\":\"FACULTY_MANAGEMENT_AGENT__ANTIGRAVITY_SEALED\"," +
            "\"action\":\"" + action + "\"," +
            "\"faculty_id\":\"" + facultyId + "\"," +
            "\"department_id\":\"" + deptId + "\"," +
            "\"workload_type\":\"" + workload + "\"," +
            "\"period\":\"" + period + "\"," +
            "\"teaching_hours_per_week\":16," +
            "\"courses_assigned\":4," +
            "\"timetable_generated\":true," +
            "\"leave_balance_days\":12," +
            "\"appraisal_due_date\":\"2025-03-31\"," +
            "\"payroll_linked\":true," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String feeManagementAgent(String action, String raw, String ts) {
        String studentId  = def(extractString(raw, "\"student_id\""),   "STU-0000");
        String feeType    = def(extractString(raw, "\"fee_type\""),      "TUITION");
        String amount     = def(extractString(raw, "\"amount\""),        "25000");
        String dueDate    = def(extractString(raw, "\"due_date\""),      "2025-07-31");
        String payMode    = def(extractString(raw, "\"payment_mode\""),  "ONLINE");
        return "{" +
            "\"agent\":\"FEE_MANAGEMENT_AGENT\"," +
            "\"action\":\"" + action + "\"," +
            "\"student_id\":\"" + studentId + "\"," +
            "\"fee_type\":\"" + feeType + "\"," +
            "\"amount\":" + amount + "," +
            "\"due_date\":\"" + dueDate + "\"," +
            "\"payment_mode\":\"" + payMode + "\"," +
            "\"receipt_generated\":true," +
            "\"receipt_id\":\"REC-" + studentId + "-001\"," +
            "\"defaulter_flag\":false," +
            "\"scholarship_applied\":false," +
            "\"late_fee\":0," +
            "\"balance_due\":0," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String lmsMigrationAgent(String action, String raw, String ts) {
        String sourceLms    = def(extractString(raw, "\"source_lms\""),     "MOODLE");
        String courseId     = def(extractString(raw, "\"course_id\""),      "CS-101");
        String migMode      = def(extractString(raw, "\"migration_mode\""), "FULL");
        String targetEnv    = def(extractString(raw, "\"target_env\""),     "ECOSKILLER-PROD");
        return "{" +
            "\"agent\":\"LMS_MIGRATION_AGENT\"," +
            "\"action\":\"" + action + "\"," +
            "\"source_lms\":\"" + sourceLms + "\"," +
            "\"course_id\":\"" + courseId + "\"," +
            "\"migration_mode\":\"" + migMode + "\"," +
            "\"target_env\":\"" + targetEnv + "\"," +
            "\"content_modules_migrated\":48," +
            "\"quizzes_migrated\":22," +
            "\"assignments_migrated\":15," +
            "\"student_progress_migrated\":true," +
            "\"media_files_transferred\":310," +
            "\"scorm_packages\":4," +
            "\"validation_errors\":0," +
            "\"rollback_snapshot\":true," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String plagiarismConnectAgent(String action, String raw, String ts) {
        String documentId  = def(extractString(raw, "\"document_id\""),     "DOC-00001");
        String studentId   = def(extractString(raw, "\"student_id\""),      "STU-0000");
        String subType     = def(extractString(raw, "\"submission_type\""), "THESIS");
        String engine      = def(extractString(raw, "\"engine\""),          "TURNITIN");
        return "{" +
            "\"agent\":\"PLAGIARISM_CONNECT_AGENT\"," +
            "\"action\":\"" + action + "\"," +
            "\"document_id\":\"" + documentId + "\"," +
            "\"student_id\":\"" + studentId + "\"," +
            "\"submission_type\":\"" + subType + "\"," +
            "\"engine\":\"" + engine + "\"," +
            "\"similarity_score\":7.4," +
            "\"similarity_threshold\":20.0," +
            "\"integrity_passed\":true," +
            "\"report_url\":\"https://ecoskiller.internal/plagiarism/" + documentId + "\"," +
            "\"flagged_sections\":0," +
            "\"policy_action\":\"CLEAR\"," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String studentRecordAgent(String action, String raw, String ts) {
        String studentId  = def(extractString(raw, "\"student_id\""),   "STU-0000");
        String recordType = def(extractString(raw, "\"record_type\""),  "ACADEMIC");
        String acYear     = def(extractString(raw, "\"academic_year\""),"2024-25");
        String status     = def(extractString(raw, "\"status\""),       "ACTIVE");
        return "{" +
            "\"agent\":\"STUDENT_RECORD_AGENT_COMPLETE\"," +
            "\"action\":\"" + action + "\"," +
            "\"student_id\":\"" + studentId + "\"," +
            "\"record_type\":\"" + recordType + "\"," +
            "\"academic_year\":\"" + acYear + "\"," +
            "\"enrollment_status\":\"" + status + "\"," +
            "\"cgpa\":8.74," +
            "\"backlogs\":0," +
            "\"scholarships\":[\"MERIT-2024\"]," +
            "\"disciplinary_incidents\":0," +
            "\"awards\":[\"BEST_PROJECT_SEM3\"]," +
            "\"alumni_transition_ready\":false," +
            "\"documents_verified\":true," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String taxonomyMigrationAgent(String action, String raw, String ts) {
        String sourceTax  = def(extractString(raw, "\"source_taxonomy\""), "LEGACY-CODES");
        String targetTax  = def(extractString(raw, "\"target_taxonomy\""), "ECOSKILLER-V2");
        String entityType = def(extractString(raw, "\"entity_type\""),     "SUBJECT");
        String mapVersion = def(extractString(raw, "\"mapping_version\""), "v1.0");
        return "{" +
            "\"agent\":\"TAXONOMY_MIGRATION_AGENT__INSTITUTE_ERP__ANTIGRAVITY\"," +
            "\"action\":\"" + action + "\"," +
            "\"source_taxonomy\":\"" + sourceTax + "\"," +
            "\"target_taxonomy\":\"" + targetTax + "\"," +
            "\"entity_type\":\"" + entityType + "\"," +
            "\"mapping_version\":\"" + mapVersion + "\"," +
            "\"entities_mapped\":512," +
            "\"unmapped_entities\":3," +
            "\"grade_scales_aligned\":true," +
            "\"blooms_taxonomy_mapped\":true," +
            "\"competency_frameworks\":[\"NEP2020\",\"NSQF\"]," +
            "\"conflict_resolutions\":5," +
            "\"audit_log_generated\":true," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    private static String transportAgent(String action, String raw, String ts) {
        String routeId   = def(extractString(raw, "\"route_id\""),   "RT-007");
        String vehicleId = def(extractString(raw, "\"vehicle_id\""), "BUS-12");
        String studentId = def(extractString(raw, "\"student_id\""), "STU-0000");
        String tripDate  = def(extractString(raw, "\"trip_date\""),  ts.substring(0, 10));
        String stopName  = def(extractString(raw, "\"stop_name\""),  "MAIN-GATE");
        return "{" +
            "\"agent\":\"TRANSPORT_AGENT\"," +
            "\"action\":\"" + action + "\"," +
            "\"route_id\":\"" + routeId + "\"," +
            "\"vehicle_id\":\"" + vehicleId + "\"," +
            "\"student_id\":\"" + studentId + "\"," +
            "\"trip_date\":\"" + tripDate + "\"," +
            "\"stop_name\":\"" + stopName + "\"," +
            "\"bus_pass_issued\":true," +
            "\"gps_tracking\":true," +
            "\"current_location\":\"EN_ROUTE\"," +
            "\"eta_minutes\":8," +
            "\"driver_verified\":true," +
            "\"fuel_log_updated\":true," +
            "\"maintenance_due\":false," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    // ─────────────────────────────────────────────────────────
    //  JSON-RPC helpers
    // ─────────────────────────────────────────────────────────
    private static Map<String, Object> buildTool(String name, String agent,
            String description, String[] params) {
        Map<String, Object> tool = new LinkedHashMap<>();
        tool.put("name", name);
        tool.put("agent", agent);
        tool.put("description", description);
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        Map<String, Object> props = new LinkedHashMap<>();
        for (String p : params) {
            Map<String, String> prop = new LinkedHashMap<>();
            prop.put("type", "string");
            props.put(p, prop);
        }
        schema.put("properties", props);
        tool.put("inputSchema", schema);
        return tool;
    }

    private static String toolToJson(Map<String, Object> tool) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"name\":").append(jsonString((String) tool.get("name"))).append(",");
        sb.append("\"description\":").append(jsonString((String) tool.get("description"))).append(",");
        @SuppressWarnings("unchecked")
        Map<String, Object> schema = (Map<String, Object>) tool.get("inputSchema");
        sb.append("\"inputSchema\":{\"type\":\"object\",\"properties\":{");
        @SuppressWarnings("unchecked")
        Map<String, Object> props = (Map<String, Object>) schema.get("properties");
        boolean first = true;
        for (String key : props.keySet()) {
            if (!first) sb.append(",");
            sb.append(jsonString(key)).append(":{\"type\":\"string\"}");
            first = false;
        }
        sb.append("}}");
        sb.append("}");
        return sb.toString();
    }

    private static String result(String id, String resultJson) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + (id != null ? id : "null") +
               ",\"result\":" + resultJson + "}";
    }

    private static String errorResponse(String id, int code, String message) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + (id != null ? id : "null") +
               ",\"error\":{\"code\":" + code + ",\"message\":" + jsonString(message) + "}}";
    }

    private static String extractString(String json, String key) {
        int ki = json.indexOf(key);
        if (ki < 0) return null;
        int colon = json.indexOf(":", ki + key.length());
        if (colon < 0) return null;
        int start = colon + 1;
        while (start < json.length() && json.charAt(start) == ' ') start++;
        if (start >= json.length()) return null;
        char c = json.charAt(start);
        if (c == '"') {
            int end = json.indexOf('"', start + 1);
            if (end < 0) return null;
            return json.substring(start + 1, end);
        } else {
            int end = start;
            while (end < json.length() && ",}] \n\r\t".indexOf(json.charAt(end)) < 0) end++;
            return json.substring(start, end);
        }
    }

    private static String jsonString(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"")
                        .replace("\n", "\\n").replace("\r", "\\r")
                        .replace("\t", "\\t") + "\"";
    }

    private static String def(String value, String fallback) {
        return (value != null && !value.isEmpty()) ? value : fallback;
    }

    private static void log(String msg) {
        System.err.println("[CAT-5] " + msg);
    }
}
