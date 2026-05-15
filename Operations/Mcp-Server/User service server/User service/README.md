# user-service-mcp

**Ecoskiller | user-service — Multi-Tenant Profile Management**  
MCP Server in Java | 20 Tools | Security: 11 Layers  
Protocol: JSON-RPC 2.0 / MCP 2024-11-05 | Zero External Dependencies  
Kubernetes Namespace: `core` | Replicas: 3–10 (HPA)

---

## Overview

Java MCP server implementing the complete Ecoskiller user-service microservice as Claude-callable tools.
Built from the Ecoskiller technical documentation covering multi-tenant RLS isolation, pgvector skill
embeddings, gamification (streaks, badges, scenarios, themes, goals), Kafka event integration,
GDPR/SOC2 compliance (soft-delete, immutable audit), and profile completion tracking.

---

## Tools (20)

| # | Tool | Description |
|---|------|-------------|
| 1  | `create_user`            | Create user profile — all 7 types (triggered by `auth.user_registered`) |
| 2  | `get_user`               | Fetch profile — RLS enforced, cross-tenant returns empty |
| 3  | `update_profile`         | Update name/bio/education/experience/certifications |
| 4  | `update_skills`          | Add/remove skills with pgvector 1536-dim embeddings |
| 5  | `update_preferences`     | Theme, notification channels, privacy level, language |
| 6  | `get_profile_completion` | Completion % used by recruiter visibility algorithm |
| 7  | `list_users`             | Tenant-scoped listing with type/domain/completion filters |
| 8  | `search_by_skill`        | `GET /users/search?skill=X` — pgvector cosine similarity |
| 9  | `get_skill_vector`       | ML-consumable pgvector data for matching-service |
| 10 | `award_badge`            | `scoring.badge_awarded` Kafka consumer |
| 11 | `unlock_scenario`        | `dojo.scenario_completed` Kafka consumer |
| 12 | `update_streak`          | Daily streak — Redis lookup, PostgreSQL sync |
| 13 | `manage_goals`           | `user_goals` table — CREATE / UPDATE_PROGRESS / LIST |
| 14 | `manage_showcase`        | `profile_showcases` — max 3 achievements for recruiters |
| 15 | `unlock_theme`           | Gamification theme rewards for milestones |
| 16 | `soft_delete_user`       | GDPR soft delete — `archived_at`, NEVER hard delete |
| 17 | `verify_user`            | Email/phone verification from auth-service |
| 18 | `get_audit_log`          | `user_audit_log` — immutable, Elasticsearch forensics |
| 19 | `get_tenant_stats`       | Analytics: type/domain/skill/completion distribution |
| 20 | `simulate_kafka_event`   | Test all 4 inbound Kafka event flows |

---

## User Types

| Type | Description |
|------|-------------|
| `CANDIDATE` | Learners, job applicants — primary assessment targets |
| `RECRUITER` | HR hiring staff — post jobs, screen candidates |
| `MENTOR` | Skill development, project mentoring, reputation tracked |
| `TRAINER` | Deliver skill programs, course instructors |
| `EVALUATOR` | Score GD sessions and assessments |
| `ADMIN` | Tenant/platform level — compliance and governance |
| `PARENT` | Read-only trust layer for minors |

---

## Domain Tracks (Isolation Boundary)

`ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION`

Cross-domain access is forbidden unless explicitly granted. Enforced at query level.

---

## RLS Security Model

Every single tool requires `tenant_id`. This mirrors PostgreSQL Row-Level Security (RLS) policies:

```
-- Production RLS policy:
CREATE POLICY tenant_isolation ON user_profiles
  USING (tenant_id = current_setting('app.tenant_id'));
```

In this MCP server: all `UserStore` operations enforce `tenantId` at the application layer.
Cross-tenant access **silently returns empty** — identical to PostgreSQL RLS behaviour.
A missing or malformed `tenant_id` throws a `SecurityException` before any data access.

---

## Profile Completion Algorithm

```
Full Name      : +15%
Bio            : +10%
Education      : +20%
Experience     : +15%
Skills (any)   : +15%
Skills (3+)    : +5%
Email verified : +10%
Phone verified : +5%
Has badges     : +3%
Showcase set   : +2%
─────────────────
Total          : 100%

≥ 50% → Recruiter visibility boost activated
         (GET /users/{id}/profile-completion used by matching algorithm)
```

---

## Kafka Integration

| Event (inbound) | Source | Tool |
|----------------|--------|------|
| `auth.user_registered` | auth-service | `create_user` |
| `scoring.badge_awarded` | scoring-service | `award_badge` |
| `dojo.scenario_completed` | dojo-match-engine | `unlock_scenario` |
| `certification.belt_earned` | certification-service | `update_profile` (certs) |

| Event (outbound) | Consumers |
|-----------------|-----------|
| `user.profile_created` | notification-service, analytics-service |
| `user.profile_updated` | notification-service, analytics-service |
| `user.profile_completed` (≥50%) | notification-service (recruiter alerts) |
| `user.skills_updated` | job-service, matching-service |
| `user.preferences_changed` | notification-service |

---

## Security Architecture (11 Layers)

`InputSanitizer.java` applied to every argument of every tool:

| Layer | Check |
|-------|-------|
| 1 | Shell metacharacters: `;` `&&` `|` `` ` `` `$(` `${` `<>` `\n` |
| 2 | SQL injection: `OR` `SELECT` `UNION` `DROP` `--` `/*` |
| 3 | Script/XSS: `<script>` `javascript:` `eval(` `onload=` |
| 4 | Email format: RFC-compliant regex |
| 5 | UUID/ID format: alphanumeric + `-_` max 128 chars |
| 6 | Tenant ID: `[a-zA-Z0-9_-]` max 64 — RLS boundary |
| 7 | Enum validation: user_type, domain_track, privacy_level |
| 8 | Oversized input: 1024 chars (bio: 2000, JSON blobs: 8192) |
| 9 | Null byte detection: path truncation prevention |
| 10 | Language code: BCP-47 format |
| 11 | Timezone format: IANA tz format |

**RateLimiter** — 60 req/min sliding window.  
**Audit trail** — every mutation recorded in `user_audit_log`, never deleted.  
**Soft-delete only** — `archived_at` timestamp, PII masked after 90 days (GDPR).

---

## Data Tables Represented

```
users                   — id, tenant_id, email, user_type, created_at (immutable)
user_profiles           — full_name, bio, domain_track, education_json, experience_json
user_skills             — skill_name, level, skill_vector (pgvector 1536-dim)
user_preferences        — theme, notifications, language, timezone, privacy_level
profile_completions     — completion%, education_complete, experience_complete, skills_complete
user_scenario_unlocks   — scenario_id, unlock_method, unlocked_at
user_goals              — goal_type, target_value, current_value, deadline, completed
profile_showcases       — achievement_ids (JSON, max 3)
daily_streaks           — current_streak_days, longest_streak_days (Redis + PostgreSQL)
user_audit_log          — actor_id, action, delta, timestamp (immutable)
```

---

## Requirements

- **Java 8+** — zero external dependencies, stdlib only

---

## Build & Run

```bash
chmod +x build.sh && ./build.sh

# Run MCP server (stdin/stdout)
java -jar target/user-service-mcp.jar

# Run 87 tests
java -cp target/user-service-mcp.jar com.ecoskiller.userservice.server.UserMcpServerTest

# Verbose test output
java -cp target/user-service-mcp.jar com.ecoskiller.userservice.server.UserMcpServerTest --verbose
```

---

## Connect to Claude Desktop

`~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "user-service-mcp": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/user-service-mcp/target/user-service-mcp.jar"]
    }
  }
}
```

---

## File Structure

```
user-service-mcp/
├── build.sh
├── README.md
├── claude_desktop_config.json
└── src/main/java/com/ecoskiller/userservice/
    ├── server/
    │   ├── UserMcpServer.java           ← JSON-RPC 2.0 loop, 20 tools
    │   └── UserMcpServerTest.java       ← 87 integration tests
    ├── model/
    │   ├── UserProfile.java             ← Full domain model: 7 types, 10 tables
    │   ├── UserStore.java               ← RLS-enforcing ConcurrentHashMap registry
    │   └── ToolResult.java
    ├── tools/
    │   ├── McpTool.java                 ← Interface
    │   ├── CreateUserTool.java          ← Tool 1
    │   ├── GetUserTool.java             ← Tool 2
    │   ├── UpdateProfileTool.java       ← Tool 3
    │   ├── UpdateSkillsTool.java        ← Tool 4
    │   ├── UpdatePreferencesTool.java   ← Tool 5
    │   ├── GetProfileCompletionTool.java← Tool 6
    │   ├── ListUsersTool.java           ← Tool 7
    │   ├── SearchBySkillTool.java       ← Tool 8
    │   ├── GetSkillVectorTool.java      ← Tool 9
    │   ├── AwardBadgeTool.java          ← Tool 10
    │   ├── UnlockScenarioTool.java      ← Tool 11
    │   ├── UpdateStreakTool.java        ← Tool 12
    │   ├── ManageGoalsTool.java         ← Tool 13
    │   ├── ManageShowcaseTool.java      ← Tool 14
    │   ├── UnlockThemeTool.java         ← Tool 15
    │   ├── SoftDeleteUserTool.java      ← Tool 16
    │   ├── VerifyUserTool.java          ← Tool 17
    │   ├── GetAuditLogTool.java         ← Tool 18
    │   ├── GetTenantStatsTool.java      ← Tool 19
    │   └── SimulateKafkaEventTool.java  ← Tool 20
    ├── security/
    │   ├── InputSanitizer.java          ← 11 security layers
    │   └── RateLimiter.java             ← 60 req/min sliding window
    └── util/
        ├── JsonUtils.java               ← Zero-dep JSON-RPC 2.0
        └── Logger.java                  ← Structured JSON stderr logger
```

---

## Protocol

- Transport : **stdio** (stdin/stdout)  
- Format    : **JSON-RPC 2.0**  
- MCP Ver   : **2024-11-05**  
- Methods   : `initialize`, `tools/list`, `tools/call`, `ping`
