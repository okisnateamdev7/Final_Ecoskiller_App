#!/bin/bash
# ─────────────────────────────────────────────────────────────
#  Ecoskiller CAT-5 — Institute ERP Suite | Agent Tests
#  Usage:  bash test_agents.sh            (pass/fail summary)
#          bash test_agents.sh --verbose  (full JSON output)
# ─────────────────────────────────────────────────────────────

VERBOSE=false
[[ "$1" == "--verbose" ]] && VERBOSE=true

PASS=0; FAIL=0
JAR="target/mcp-05-institute-erp.jar"
SERVER_CMD="java -jar $JAR"

run_test() {
    local name="$1"
    local payload="$2"
    local expect="$3"

    response=$(echo "$payload" | $SERVER_CMD 2>/dev/null)

    if echo "$response" | grep -q "$expect"; then
        echo "  ✅  $name"
        PASS=$((PASS+1))
    else
        echo "  ❌  $name  [expected: $expect]"
        FAIL=$((FAIL+1))
    fi

    $VERBOSE && echo "     → $response"
}

echo ""
echo "══════════════════════════════════════════════════════════"
echo "  Ecoskiller MCP | CAT-5 Institute ERP Suite"
echo "  $(date)"
echo "══════════════════════════════════════════════════════════"
echo ""

# ── initialize / tools/list ────────────────────────────────
echo "[ core ]"
run_test "initialize" \
  '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}' \
  "2024-11-05"

run_test "tools/list returns 12 tools" \
  '{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}' \
  "academic_structure"

run_test "ping" \
  '{"jsonrpc":"2.0","id":3,"method":"ping","params":{}}' \
  "pong"

# ── academic_structure ─────────────────────────────────────
echo ""
echo "[ ACADEMIC_STRUCTURE_AGENT ]"
run_test "create program" \
  '{"jsonrpc":"2.0","id":10,"method":"tools/call","params":{"name":"academic_structure","arguments":{"action":"create_program","department_id":"DEPT-CS","program_id":"PROG-BTECH","course_id":"CS-301","semester":"SEM-5"}}}' \
  "ACADEMIC_STRUCTURE_AGENT__ANTIGRAVITY_SEALED"

run_test "curriculum check" \
  '{"jsonrpc":"2.0","id":11,"method":"tools/call","params":{"name":"academic_structure","arguments":{"action":"check_curriculum","department_id":"DEPT-MECH"}}}' \
  "SUCCESS"

# ── attendance ─────────────────────────────────────────────
echo ""
echo "[ ATTENDANCE_AGENT ]"
run_test "mark attendance" \
  '{"jsonrpc":"2.0","id":20,"method":"tools/call","params":{"name":"attendance","arguments":{"action":"mark","student_id":"STU-1001","course_id":"CS-201","date":"2025-06-10","session":"MORNING"}}}' \
  "ATTENDANCE_AGENT__ANTIGRAVITY_SEALED"

run_test "shortage alert check" \
  '{"jsonrpc":"2.0","id":21,"method":"tools/call","params":{"name":"attendance","arguments":{"action":"shortage_check","student_id":"STU-2003"}}}' \
  "SUCCESS"

# ── campus ─────────────────────────────────────────────────
echo ""
echo "[ CAMPUS_AGENT ]"
run_test "book classroom" \
  '{"jsonrpc":"2.0","id":30,"method":"tools/call","params":{"name":"campus","arguments":{"action":"book","facility_type":"CLASSROOM","facility_id":"ROOM-B202","booking_date":"2025-06-12","capacity":"45"}}}' \
  "CAMPUS_AGENT"

run_test "lab availability" \
  '{"jsonrpc":"2.0","id":31,"method":"tools/call","params":{"name":"campus","arguments":{"action":"check_availability","facility_type":"LAB","facility_id":"LAB-CS-01"}}}' \
  "SUCCESS"

# ── data_normalization ─────────────────────────────────────
echo ""
echo "[ DATA_NORMALIZATION_AGENT ]"
run_test "normalize student batch" \
  '{"jsonrpc":"2.0","id":40,"method":"tools/call","params":{"name":"data_normalization","arguments":{"action":"normalize","source_system":"LEGACY-SAP","entity_type":"STUDENT","batch_id":"BATCH-202","rules_profile":"STRICT"}}}' \
  "DATA_NORMALIZATION_AGENT__INSTITUTE_ERP__ANTIGRAVITY"

run_test "dedup check" \
  '{"jsonrpc":"2.0","id":41,"method":"tools/call","params":{"name":"data_normalization","arguments":{"action":"dedup","entity_type":"FACULTY","batch_id":"BATCH-099"}}}' \
  "SUCCESS"

# ── exam_engine ────────────────────────────────────────────
echo ""
echo "[ EXAM_ENGINE_AGENT ]"
run_test "schedule exam" \
  '{"jsonrpc":"2.0","id":50,"method":"tools/call","params":{"name":"exam_engine","arguments":{"action":"schedule","exam_id":"EXAM-2025-06","course_id":"CS-401","exam_type":"SEMESTER","scheduled_date":"2025-11-20"}}}' \
  "EXAM_ENGINE_AGENT__ANTIGRAVITY_SEALED"

run_test "generate question paper" \
  '{"jsonrpc":"2.0","id":51,"method":"tools/call","params":{"name":"exam_engine","arguments":{"action":"generate_paper","exam_id":"EXAM-2025-06","course_id":"CS-401"}}}' \
  "SUCCESS"

# ── faculty_management ─────────────────────────────────────
echo ""
echo "[ FACULTY_MANAGEMENT_AGENT ]"
run_test "assign workload" \
  '{"jsonrpc":"2.0","id":60,"method":"tools/call","params":{"name":"faculty_management","arguments":{"action":"assign_workload","faculty_id":"FAC-0042","department_id":"DEPT-CS","workload_type":"TEACHING","period":"AY-2025"}}}' \
  "FACULTY_MANAGEMENT_AGENT__ANTIGRAVITY_SEALED"

run_test "generate timetable" \
  '{"jsonrpc":"2.0","id":61,"method":"tools/call","params":{"name":"faculty_management","arguments":{"action":"generate_timetable","faculty_id":"FAC-0042"}}}' \
  "SUCCESS"

# ── fee_management ─────────────────────────────────────────
echo ""
echo "[ FEE_MANAGEMENT_AGENT ]"
run_test "collect fee" \
  '{"jsonrpc":"2.0","id":70,"method":"tools/call","params":{"name":"fee_management","arguments":{"action":"collect","student_id":"STU-1001","fee_type":"TUITION","amount":"35000","due_date":"2025-07-15","payment_mode":"UPI"}}}' \
  "FEE_MANAGEMENT_AGENT"

run_test "defaulter check" \
  '{"jsonrpc":"2.0","id":71,"method":"tools/call","params":{"name":"fee_management","arguments":{"action":"defaulter_report","fee_type":"HOSTEL"}}}' \
  "SUCCESS"

# ── lms_migration ──────────────────────────────────────────
echo ""
echo "[ LMS_MIGRATION_AGENT ]"
run_test "migrate moodle course" \
  '{"jsonrpc":"2.0","id":80,"method":"tools/call","params":{"name":"lms_migration","arguments":{"action":"migrate","source_lms":"MOODLE","course_id":"CS-501","migration_mode":"FULL","target_env":"ECOSKILLER-PROD"}}}' \
  "LMS_MIGRATION_AGENT"

run_test "validate migration" \
  '{"jsonrpc":"2.0","id":81,"method":"tools/call","params":{"name":"lms_migration","arguments":{"action":"validate","source_lms":"CANVAS","course_id":"MBA-301"}}}' \
  "SUCCESS"

# ── plagiarism_connect ─────────────────────────────────────
echo ""
echo "[ PLAGIARISM_CONNECT_AGENT ]"
run_test "submit document" \
  '{"jsonrpc":"2.0","id":90,"method":"tools/call","params":{"name":"plagiarism_connect","arguments":{"action":"submit","document_id":"DOC-00777","student_id":"STU-2211","submission_type":"THESIS","engine":"TURNITIN"}}}' \
  "PLAGIARISM_CONNECT_AGENT"

run_test "get similarity score" \
  '{"jsonrpc":"2.0","id":91,"method":"tools/call","params":{"name":"plagiarism_connect","arguments":{"action":"get_report","document_id":"DOC-00777"}}}' \
  "SUCCESS"

# ── student_record ─────────────────────────────────────────
echo ""
echo "[ STUDENT_RECORD_AGENT_COMPLETE ]"
run_test "fetch academic record" \
  '{"jsonrpc":"2.0","id":100,"method":"tools/call","params":{"name":"student_record","arguments":{"action":"fetch","student_id":"STU-3301","record_type":"ACADEMIC","academic_year":"2024-25","status":"ACTIVE"}}}' \
  "STUDENT_RECORD_AGENT_COMPLETE"

run_test "alumni transition" \
  '{"jsonrpc":"2.0","id":101,"method":"tools/call","params":{"name":"student_record","arguments":{"action":"alumni_transition","student_id":"STU-3301"}}}' \
  "SUCCESS"

# ── taxonomy_migration ─────────────────────────────────────
echo ""
echo "[ TAXONOMY_MIGRATION_AGENT ]"
run_test "map subject codes" \
  '{"jsonrpc":"2.0","id":110,"method":"tools/call","params":{"name":"taxonomy_migration","arguments":{"action":"map","source_taxonomy":"LEGACY-CODES","target_taxonomy":"ECOSKILLER-V2","entity_type":"SUBJECT","mapping_version":"v2.0"}}}' \
  "TAXONOMY_MIGRATION_AGENT__INSTITUTE_ERP__ANTIGRAVITY"

run_test "bloom mapping" \
  '{"jsonrpc":"2.0","id":111,"method":"tools/call","params":{"name":"taxonomy_migration","arguments":{"action":"bloom_map","source_taxonomy":"OLD-BLOOM","target_taxonomy":"NEP2020"}}}' \
  "SUCCESS"

# ── transport ──────────────────────────────────────────────
echo ""
echo "[ TRANSPORT_AGENT ]"
run_test "issue bus pass" \
  '{"jsonrpc":"2.0","id":120,"method":"tools/call","params":{"name":"transport","arguments":{"action":"issue_pass","route_id":"RT-012","vehicle_id":"BUS-07","student_id":"STU-4410","trip_date":"2025-06-01","stop_name":"CITY-CENTER"}}}' \
  "TRANSPORT_AGENT"

run_test "gps track" \
  '{"jsonrpc":"2.0","id":121,"method":"tools/call","params":{"name":"transport","arguments":{"action":"track","vehicle_id":"BUS-07","route_id":"RT-012"}}}' \
  "SUCCESS"

# ── summary ────────────────────────────────────────────────
echo ""
echo "══════════════════════════════════════════════════════════"
echo "  Results:  ✅  $PASS passed   ❌  $FAIL failed   ($(( PASS + FAIL )) total)"
echo "══════════════════════════════════════════════════════════"
echo ""
