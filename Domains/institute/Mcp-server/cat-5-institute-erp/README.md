# mcp-05-institute-erp

**Ecoskiller | CAT-5 — Institute ERP Suite**  
MCP Server in Java | 12 Agents | Priority: HIGH

---

## Agents (12)

| # | Tool Name | Agent |
|---|-----------|-------|
| 5  | `academic_structure` | ACADEMIC_STRUCTURE_AGENT__ANTIGRAVITY_SEALED |
| 6  | `attendance`         | ATTENDANCE_AGENT__ANTIGRAVITY_SEALED |
| 7  | `campus`             | CAMPUS_AGENT |
| 8  | `data_normalization` | DATA_NORMALIZATION_AGENT__INSTITUTE_ERP__ANTIGRAVITY |
| 9  | `exam_engine`        | EXAM_ENGINE_AGENT__ANTIGRAVITY_SEALED |
| 10 | `faculty_management` | FACULTY_MANAGEMENT_AGENT__ANTIGRAVITY_SEALED |
| 11 | `fee_management`     | FEE_MANAGEMENT_AGENT |
| 12 | `lms_migration`      | LMS_MIGRATION_AGENT |
| 13 | `plagiarism_connect` | PLAGIARISM_CONNECT_AGENT |
| 14 | `student_record`     | STUDENT_RECORD_AGENT_COMPLETE |
| 15 | `taxonomy_migration` | TAXONOMY_MIGRATION_AGENT__INSTITUTE_ERP__ANTIGRAVITY |
| 16 | `transport`          | TRANSPORT_AGENT |

---

## Requirements

- Java 11+
- Maven 3.6+ *(build only — zero runtime dependencies)*

---

## Build

```bash
mvn clean package
# produces: target/mcp-05-institute-erp.jar
```

---

## Run the server

```bash
java -jar target/mcp-05-institute-erp.jar
```

Communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-05-institute-erp": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-05-institute-erp/target/mcp-05-institute-erp.jar"]
    }
  }
}
```

---

## Run tests

```bash
bash test_agents.sh           # 27 tests — pass/fail summary
bash test_agents.sh --verbose # with full JSON output
```

---

## File Structure

```
mcp-05-institute-erp/
├── pom.xml                              ← Maven build (fat JAR, Java 11+)
├── test_agents.sh                       ← Bash test runner (27 tests)
├── README.md
└── src/main/java/ecoskiller/
    └── McpServer.java                   ← Main MCP server (all 12 agents)
```

---

## Agent Capabilities

### 5. `academic_structure` — ACADEMIC_STRUCTURE_AGENT__ANTIGRAVITY_SEALED
Manages academic hierarchy: departments, programs, courses, semesters, credit structures, curriculum versioning, accreditation.  
**Params:** `action`, `department_id`, `program_id`, `course_id`, `semester`

---

### 6. `attendance` — ATTENDANCE_AGENT__ANTIGRAVITY_SEALED
Tracks student/faculty attendance, shortage alerts, biometric/RFID sync, leave adjustments.  
**Params:** `action`, `student_id`, `faculty_id`, `course_id`, `date`, `session`

---

### 7. `campus` — CAMPUS_AGENT
Facility booking/allocation: classrooms, labs, hostels, canteen, library, smart board, QR tokens.  
**Params:** `action`, `facility_type`, `facility_id`, `booking_date`, `capacity`

---

### 8. `data_normalization` — DATA_NORMALIZATION_AGENT__INSTITUTE_ERP__ANTIGRAVITY
Cleanses, deduplicates, and normalises data from legacy ERPs and CSV imports.  
**Params:** `action`, `source_system`, `entity_type`, `batch_id`, `rules_profile`

---

### 9. `exam_engine` — EXAM_ENGINE_AGENT__ANTIGRAVITY_SEALED
End-to-end exam lifecycle: scheduling, seating, question-paper generation, proctoring, grading.  
**Params:** `action`, `exam_id`, `course_id`, `student_id`, `exam_type`, `scheduled_date`

---

### 10. `faculty_management` — FACULTY_MANAGEMENT_AGENT__ANTIGRAVITY_SEALED
Faculty profiles, workload allocation, timetables, appraisals, leave, payroll linkage.  
**Params:** `action`, `faculty_id`, `department_id`, `workload_type`, `period`

---

### 11. `fee_management` — FEE_MANAGEMENT_AGENT
Fee structures, instalment scheduling, payment collection, receipt generation, defaulter tracking, scholarships.  
**Params:** `action`, `student_id`, `fee_type`, `amount`, `due_date`, `payment_mode`

---

### 12. `lms_migration` — LMS_MIGRATION_AGENT
Migrates content, quizzes, assignments, and student progress from Moodle / Blackboard / Canvas to Ecoskiller LMS.  
**Params:** `action`, `source_lms`, `course_id`, `migration_mode`, `target_env`

---

### 13. `plagiarism_connect` — PLAGIARISM_CONNECT_AGENT
Turnitin / iThenticate / Unicheck integration: submit docs, retrieve similarity scores, enforce integrity policies.  
**Params:** `action`, `document_id`, `student_id`, `submission_type`, `engine`

---

### 14. `student_record` — STUDENT_RECORD_AGENT_COMPLETE
Complete student lifecycle: admission, enrollment, academic history, awards, disciplinary, alumni transition.  
**Params:** `action`, `student_id`, `record_type`, `academic_year`, `status`

---

### 15. `taxonomy_migration` — TAXONOMY_MIGRATION_AGENT__INSTITUTE_ERP__ANTIGRAVITY
Maps/migrates subject codes, grade scales, Bloom's taxonomy, competency frameworks (NEP2020, NSQF) across ERPs.  
**Params:** `action`, `source_taxonomy`, `target_taxonomy`, `entity_type`, `mapping_version`

---

### 16. `transport` — TRANSPORT_AGENT
Route planning, bus allocation, GPS tracking, bus-pass issuance, driver records, fuel/maintenance logs.  
**Params:** `action`, `route_id`, `vehicle_id`, `student_id`, `trip_date`, `stop_name`

---

## Protocol

| Property | Value |
|----------|-------|
| Transport | stdio (stdin/stdout) |
| Format | JSON-RPC 2.0 |
| MCP Version | 2024-11-05 |
| Methods | `initialize`, `tools/list`, `tools/call`, `ping` |

---

## Sample call

```bash
echo '{"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"student_record","arguments":{"action":"fetch","student_id":"STU-1001","record_type":"ACADEMIC","academic_year":"2024-25"}}}' \
  | java -jar target/mcp-05-institute-erp.jar
```
