package com.ecoskiller.mcp.institute.config;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Antigravity Tool Registry for mcp-05-institute-erp.
 *
 * <p>Every CAT-05 agent is registered here with:
 * <ul>
 *   <li>name        — snake_case tool name used in tools/call</li>
 *   <li>description — what the agent does</li>
 *   <li>inputSchema — strict JSON Schema (SEALED agents = more constraints)</li>
 *   <li>agentRef    — the .md filename from the Antigravity vault</li>
 *   <li>sealed      — whether ANTIGRAVITY_SEALED contract is enforced</li>
 * </ul>
 * </p>
 */
@Component
public class ToolRegistry {

    public record ToolDef(
            String name,
            String description,
            Map<String, Object> inputSchema,
            String agentRef,
            boolean sealed
    ) {}

    private final List<ToolDef> tools = List.of(

        // ── 1. ACADEMIC_STRUCTURE_AGENT (SEALED) ──────────────────────────────
        new ToolDef(
            "academic_structure",
            """
            Manages the full academic structure of an institute: departments, programmes,
            batches, sections, subjects, and their relationships. ANTIGRAVITY_SEALED —
            all write operations require a tenant_id and pass schema validation before
            persisting to the ERP core.
            """,
            schema(Map.of(
                "tenant_id",   prop("string",  "Tenant UUID (required for all ops)"),
                "action",      propEnum("Action to perform",
                                "create_department","update_department","delete_department",
                                "create_programme","list_programmes",
                                "create_batch","assign_section",
                                "link_subject","unlink_subject","get_structure"),
                "payload",     prop("object",  "Action-specific data (see agent spec)"),
                "dry_run",     prop("boolean", "Validate without committing (default false)")
            ), List.of("tenant_id","action")),
            "ACADEMIC_STRUCTURE_AGENT__ANTIGRAVITY_SEALED.md", true
        ),

        // ── 2. ATTENDANCE_AGENT (SEALED) ──────────────────────────────────────
        new ToolDef(
            "attendance",
            """
            Records and queries student / faculty attendance. Supports biometric,
            RFID, and manual entry modes. Enforces institution-level attendance
            policies. ANTIGRAVITY_SEALED — prevents retroactive manipulation beyond
            the allowed correction window.
            """,
            schema(Map.of(
                "tenant_id",   prop("string",  "Tenant UUID"),
                "action",      propEnum("Action",
                                "mark_attendance","bulk_mark","get_report",
                                "get_defaulters","update_policy","correct_entry"),
                "class_id",    prop("string",  "Class / section identifier"),
                "date",        prop("string",  "ISO-8601 date (YYYY-MM-DD)"),
                "entries",     prop("array",   "Array of {student_id, status} objects"),
                "report_type", propEnum("Report granularity","daily","weekly","monthly","custom")
            ), List.of("tenant_id","action")),
            "ATTENDANCE_AGENT__ANTIGRAVITY_SEALED.md", true
        ),

        // ── 3. CAMPUS_AGENT ───────────────────────────────────────────────────
        new ToolDef(
            "campus",
            """
            Manages physical campus assets: buildings, rooms, labs, libraries,
            canteens, and facility booking. Integrates with the transport and
            exam-engine agents for room allocation.
            """,
            schema(Map.of(
                "tenant_id",  prop("string",  "Tenant UUID"),
                "action",     propEnum("Action",
                               "add_building","add_room","update_room",
                               "book_facility","release_booking",
                               "list_available","get_floor_plan"),
                "building_id",prop("string",  "Building identifier"),
                "room_id",    prop("string",  "Room / facility identifier"),
                "payload",    prop("object",  "Action-specific data")
            ), List.of("tenant_id","action")),
            "CAMPUS_AGENT.md", false
        ),

        // ── 4. DATA_NORMALIZATION_AGENT (SEALED) ──────────────────────────────
        new ToolDef(
            "data_normalization",
            """
            Normalises incoming ERP data from legacy systems, CSV imports, or
            third-party integrations into the Antigravity canonical schema.
            ANTIGRAVITY_SEALED — quarantines rows that fail validation instead
            of silently coercing them.
            """,
            schema(Map.of(
                "tenant_id",   prop("string",  "Tenant UUID"),
                "source",      propEnum("Data source type",
                                "csv","json","xml","legacy_db","api_feed"),
                "entity_type", propEnum("Target ERP entity",
                                "student","faculty","course","fee","attendance","result"),
                "raw_data",    prop("array",   "Array of raw records"),
                "rules",       prop("object",  "Optional override normalisation rules"),
                "mode",        propEnum("Run mode","validate_only","normalize","normalize_and_commit")
            ), List.of("tenant_id","source","entity_type","raw_data")),
            "data_normalization_agent__institute_erp__antigravity.md", true
        ),

        // ── 5. EXAM_ENGINE_AGENT (SEALED) ─────────────────────────────────────
        new ToolDef(
            "exam_engine",
            """
            End-to-end examination management: schedule exams, assign invigilators,
            generate hall tickets, record marks, compute results, and publish grade
            sheets. ANTIGRAVITY_SEALED — result publication requires dual-approval.
            """,
            schema(Map.of(
                "tenant_id",    prop("string",  "Tenant UUID"),
                "action",       propEnum("Action",
                                 "schedule_exam","assign_invigilator",
                                 "generate_hall_tickets","enter_marks","bulk_enter_marks",
                                 "compute_result","publish_result",
                                 "get_timetable","get_result_sheet"),
                "exam_id",      prop("string",  "Exam identifier (required for most ops)"),
                "payload",      prop("object",  "Action-specific payload"),
                "approver_id",  prop("string",  "Required for publish_result (dual-approval)")
            ), List.of("tenant_id","action")),
            "EXAM_ENGINE_AGENT__ANTIGRAVITY_SEALED.md", true
        ),

        // ── 6. FACULTY_MANAGEMENT_AGENT (SEALED) ──────────────────────────────
        new ToolDef(
            "faculty_management",
            """
            Manages faculty profiles, appointments, subject assignments, workload,
            appraisals, and offboarding. ANTIGRAVITY_SEALED — payroll-linked
            changes require HR approval workflow before taking effect.
            """,
            schema(Map.of(
                "tenant_id",  prop("string",  "Tenant UUID"),
                "action",     propEnum("Action",
                               "create_faculty","update_profile","deactivate",
                               "assign_subject","unassign_subject",
                               "set_workload","get_workload",
                               "initiate_appraisal","get_appraisal"),
                "faculty_id", prop("string",  "Faculty UUID"),
                "payload",    prop("object",  "Action-specific payload"),
                "hr_token",   prop("string",  "HR approval token (payroll-linked ops)")
            ), List.of("tenant_id","action")),
            "FACULTY_MANAGEMENT_AGENT__ANTIGRAVITY_SEALED.md", true
        ),

        // ── 7. FEE_MANAGEMENT_AGENT ───────────────────────────────────────────
        new ToolDef(
            "fee_management",
            """
            Manages fee structures, instalment schedules, payments, receipts,
            concessions, and outstanding dues. Integrates with the payment gateway
            via the PAYMENT_CONNECT_AGENT in mcp-07.
            """,
            schema(Map.of(
                "tenant_id",    prop("string",  "Tenant UUID"),
                "action",       propEnum("Action",
                                 "create_fee_structure","update_fee_structure",
                                 "assign_to_batch","record_payment",
                                 "apply_concession","get_dues","generate_receipt",
                                 "send_reminder","get_collection_report"),
                "student_id",   prop("string",  "Student UUID (payment ops)"),
                "fee_head_id",  prop("string",  "Fee head identifier"),
                "amount",       prop("number",  "Payment amount in INR paise"),
                "payload",      prop("object",  "Extended payload")
            ), List.of("tenant_id","action")),
            "FEE_MANAGEMENT_AGENT.md", false
        ),

        // ── 8. LMS_MIGRATION_AGENT ────────────────────────────────────────────
        new ToolDef(
            "lms_migration",
            """
            Migrates course content, SCORM packages, quiz banks, and learner
            progress data from external LMS platforms (Moodle, Canvas, Blackboard,
            Google Classroom) into the Ecoskiller LMS. Runs as a staged pipeline
            with rollback support.
            """,
            schema(Map.of(
                "tenant_id",    prop("string",  "Tenant UUID"),
                "action",       propEnum("Action",
                                 "start_migration","get_status","pause","resume",
                                 "rollback","map_courses","validate_content",
                                 "get_migration_report"),
                "source_lms",   propEnum("Source LMS platform",
                                 "moodle","canvas","blackboard","google_classroom","custom"),
                "source_config",prop("object",  "Source LMS connection config"),
                "migration_id", prop("string",  "Migration job ID (for status/resume/rollback)"),
                "dry_run",      prop("boolean", "Simulate without writing (default false)")
            ), List.of("tenant_id","action")),
            "LMS_MIGRATION_AGENT.md", false
        ),

        // ── 9. PLAGIARISM_CONNECT_AGENT ───────────────────────────────────────
        new ToolDef(
            "plagiarism_connect",
            """
            Submits student assignments and research papers to plagiarism detection
            engines (Turnitin, iThenticate, Unicheck) and retrieves similarity
            reports. Stores results against the student record in the ERP.
            """,
            schema(Map.of(
                "tenant_id",    prop("string",  "Tenant UUID"),
                "action",       propEnum("Action",
                                 "submit_document","get_report","bulk_submit",
                                 "configure_engine","get_engine_status"),
                "student_id",   prop("string",  "Student UUID"),
                "document_url", prop("string",  "URL or base64 of the document"),
                "engine",       propEnum("Plagiarism engine",
                                 "turnitin","ithenticate","unicheck","internal"),
                "submission_id",prop("string",  "Returned by submit; used for get_report")
            ), List.of("tenant_id","action")),
            "PLAGIARISM_CONNECT_AGENT.md", false
        ),

        // ── 10. STUDENT_RECORD_AGENT ──────────────────────────────────────────
        new ToolDef(
            "student_record",
            """
            Complete student lifecycle management: admission, enrolment, profile
            updates, document vault, academic history, disciplinary records,
            and alumni transition. Acts as the single source of truth for all
            other CAT-05 agents.
            """,
            schema(Map.of(
                "tenant_id",    prop("string",  "Tenant UUID"),
                "action",       propEnum("Action",
                                 "admit_student","update_profile","enrol_in_batch",
                                 "transfer","suspend","graduate","alumni_transition",
                                 "upload_document","get_academic_history",
                                 "search","get_full_record"),
                "student_id",   prop("string",  "Student UUID"),
                "payload",      prop("object",  "Action-specific payload"),
                "document_type",propEnum("Document category",
                                 "marksheet","certificate","id_proof","fee_receipt",
                                 "medical","other")
            ), List.of("tenant_id","action")),
            "STUDENT_RECORD_AGENT_COMPLETE.md", false
        ),

        // ── 11. TAXONOMY_MIGRATION_AGENT (SEALED) ─────────────────────────────
        new ToolDef(
            "taxonomy_migration",
            """
            Migrates subject taxonomies, skill tags, competency frameworks, and
            bloom-level mappings from legacy curricula into the Ecoskiller taxonomy
            graph. ANTIGRAVITY_SEALED — conflicts between source and target
            taxonomies must be resolved before commit.
            """,
            schema(Map.of(
                "tenant_id",    prop("string",  "Tenant UUID"),
                "action",       propEnum("Action",
                                 "import_taxonomy","map_skills","resolve_conflict",
                                 "validate_mapping","commit","rollback",
                                 "export_mapping_report"),
                "source_format",propEnum("Source taxonomy format",
                                 "csv","json","skos","lom","custom"),
                "taxonomy_data",prop("array",   "Raw taxonomy nodes"),
                "conflict_resolution",
                                propEnum("How to handle conflicts",
                                 "manual","prefer_source","prefer_target","merge"),
                "migration_id", prop("string",  "Job ID (status / rollback ops)")
            ), List.of("tenant_id","action")),
            "taxonomy_migration_agent__institute_erp__antigravity.md", true
        ),

        // ── 12. TRANSPORT_AGENT ───────────────────────────────────────────────
        new ToolDef(
            "transport",
            """
            Manages institute transport: routes, buses, GPS tracking, driver
            assignments, student boarding lists, and transport fee integration.
            Pushes real-time ETA events to the Dojo (mcp-16) via event bus.
            """,
            schema(Map.of(
                "tenant_id",   prop("string",  "Tenant UUID"),
                "action",      propEnum("Action",
                                "create_route","update_route","assign_bus",
                                "assign_driver","update_gps","get_eta",
                                "manage_boarding_list","get_route_report",
                                "send_delay_alert"),
                "route_id",    prop("string",  "Route identifier"),
                "bus_id",      prop("string",  "Bus / vehicle identifier"),
                "student_ids", prop("array",   "List of student UUIDs for boarding"),
                "payload",     prop("object",  "Action-specific payload")
            ), List.of("tenant_id","action")),
            "TRANSPORT_AGENT.md", false
        )
    );

    // ── Accessors ─────────────────────────────────────────────────────────────

    public List<ToolDef> all() { return tools; }

    public ToolDef find(String name) {
        return tools.stream()
                .filter(t -> t.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    // ── Schema helpers ────────────────────────────────────────────────────────

    private static Map<String, Object> prop(String type, String desc) {
        return Map.of("type", type, "description", desc);
    }

    private static Map<String, Object> propEnum(String desc, String... values) {
        return Map.of("type", "string", "description", desc, "enum", List.of(values));
    }

    private static Map<String, Object> schema(Map<String, Object> props,
                                               List<String> required) {
        Map<String, Object> s = new LinkedHashMap<>();
        s.put("type", "object");
        s.put("properties", props);
        s.put("required", required);
        return s;
    }
}
