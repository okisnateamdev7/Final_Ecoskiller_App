# TRANSCRIPT_SCHEMA_VERSION_MIGRATION_AGENT.md

```
╔══════════════════════════════════════════════════════════════════════════════════════════════╗
║       ECOSKILLER ANTIGRAVITY — TRANSCRIPT SCHEMA VERSION MIGRATION AGENT                   ║
║                         SEALED · LOCKED · GOVERNED · DETERMINISTIC                         ║
║                                                                                              ║
║  Agent ID           : AGTSVM-004                                                             ║
║  Mutation Policy    : Add-only via version bump                                              ║
║  Interpretation     : NONE PERMITTED                                                         ║
║  Execution Auth     : Human declaration only                                                 ║
║  Classification     : CRITICAL INFRASTRUCTURE — ZERO-DOWNTIME SCHEMA EVOLUTION LAYER       ║
╚══════════════════════════════════════════════════════════════════════════════════════════════╝
```

---

## SECTION 0 — SEAL DECLARATION

This agent specification is **SEALED**.

> No component of this agent may be interpreted, rewritten, abbreviated, inferred,
> or executed in any manner inconsistent with what is explicitly declared below.
> Any ambiguity MUST halt execution and escalate to the Governance Authority immediately.
> No silent assumptions, no creative interpretation, no default-fill behaviour.
> Every action taken by this agent is logged, signed, and immutable.

**GOVERNANCE AUTHORITY   :** `ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT`
**COMPLIANCE AUTHORITY   :** `AUDIT_COMPLIANCE_AGENT`
**SECURITY AUTHORITY     :** `ZERO_TRUST_AGENT`
**DATA AUTHORITY         :** `DATA_GOVERNANCE_AGENT`
**SCHEMA AUTHORITY       :** `ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPATIBILITY_AGENT`
**VERSION AUTHORITY      :** `AGENT_VERSION_COMPATIBILITY_AGENT`
**POLICY AUTHORITY       :** `POLICY_VERSION_CONTROL_AGENT`
**MIGRATION AUTHORITY    :** `ZERO_DOWNTIME_MIGRATION_AGENT`
**LEGAL AUTHORITY        :** `LEGAL_POLICY_AGENT`

---

## SECTION 1 — AGENT IDENTITY (MANDATORY)

```yaml
AGENT_NAME             : TRANSCRIPT_SCHEMA_VERSION_MIGRATION_AGENT
AGENT_ID               : AGTSVM-004
SYSTEM_ROLE            : Zero-Downtime Transcript Schema Lifecycle Manager,
                         Multi-Store Migration Orchestrator,
                         Consumer Compatibility Gatekeeper,
                         Rollback Safety Enforcer,
                         and Cross-Domain Schema Drift Eliminator
PRIMARY_DOMAIN         : Schema Governance / Transcript Lifecycle /
                         Database Migration Orchestration /
                         Consumer Compatibility Enforcement /
                         Zero-Downtime Deployment Safety
EXECUTION_MODE         : Deterministic + Validated + Zero-Downtime +
                         Rollback-Safe + Audit-Traced
DATA_SCOPE             : Transcript schema definitions (all versions),
                         migration scripts and manifests,
                         consumer compatibility matrices,
                         store migration state records (PostgreSQL, Redis, ClickHouse,
                         Kafka topic schemas, OpenSearch index mappings, MinIO document formats),
                         Flyway migration history,
                         rollback checkpoints,
                         feature flag states per migration phase
                         — NO raw session data, NO audio, NO PII
TENANT_SCOPE           : Global schema operations are platform-wide;
                         Tenant-specific data migrations are strictly tenant-isolated
                         (row-level security + migration job partitioning by tenant_id)
FAILURE_POLICY         : HALT on ambiguity — STOP → CHECKPOINT → LOG → ESCALATE
                         — NO partial migration completion under any circumstance
MUTATION_POLICY        : Add-only schema evolution;
                         field removal is PROHIBITED without full deprecation cycle
AUDIT_POLICY           : Append-only immutable audit trail for every migration step,
                         every validation gate, every rollback action
SECURITY_MODEL         : Zero-trust — every migration step signed,
                         every store write validated, every consumer gate enforced
VERSION                : v1.0.0
STATUS                 : ACTIVE
```

---

## SECTION 2 — PURPOSE DECLARATION

### 2.1 The Fundamental Problem

The Transcript Agent Triad (`PHONE_BOT_VOICE_DETECTION_AGENT` → `TRANSCRIPT_AGGREGATION_AGENT` → `TRANSCRIPT_VERSIONING_AGENT`) produces and governs `SESSION_TRANSCRIPT` objects that are consumed by **14+ downstream agents** across **8 data stores** simultaneously.

The platform operates at **10M–100M users** with **continuous deployment** on Kubernetes (k3s) and a strict **add-only, versioned mutation policy**. As the platform evolves — adding new session types, new intelligence dimensions, new scoring fields, new legal metadata requirements — the `SESSION_TRANSCRIPT` schema **must evolve**.

This evolution creates a set of simultaneous, interlocking challenges that no single service can resolve alone:

```
CHALLENGE 1 — MULTI-STORE SCHEMA CONSISTENCY
  A transcript schema change affects:
    PostgreSQL   → versioned_transcripts table schema (Flyway migration)
    ClickHouse   → analytics tables for GD metrics, scoring distributions
    OpenSearch   → index mappings for candidate discovery, session search
    Kafka        → topic schema / Avro/JSON schema registry
    Redis        → cached VERSION_CHAIN_INDEX structure
    MinIO        → JSON document format for WORM-archived transcripts
    Flutter App  → UI widget schema for session report rendering
    React Web    → SEO-indexed session summary schema
  Every store must migrate in coordination, or consumers break.

CHALLENGE 2 — ZERO DOWNTIME REQUIREMENT
  The platform runs 24/7 with live GD sessions, live scoring, live parent reports.
  A migration that takes any store offline for even 30 seconds causes:
    - Active GD sessions losing their scoring pipeline
    - Parents receiving report generation failures
    - Legal evidence packaging breaking mid-generation
  All migrations MUST be zero-downtime.

CHALLENGE 3 — BACKWARD COMPATIBILITY WINDOW
  SCORING_ENGINE v2.1.0 was built against TS-SCHEMA-v1.0.0.
  PARENT_LLM_INSIGHT_NARRATIVE_AGENT v1.3.0 was also built against TS-SCHEMA-v1.0.0.
  If TRANSCRIPT_AGGREGATION_AGENT is upgraded to produce TS-SCHEMA-v1.1.0:
    - New transcripts use v1.1.0
    - Historical transcripts remain at v1.0.0
    - Both consumers must work correctly simultaneously
    - Consumers must know exactly when to upgrade

CHALLENGE 4 — ROLLBACK SAFETY
  A migration that reaches ClickHouse but fails at OpenSearch must be
  reversible to the exact pre-migration state across ALL stores simultaneously.
  Partial migrations leave the system in an inconsistent state that is
  worse than a clean failure.

CHALLENGE 5 — LEGAL ARCHIVE IMMUTABILITY
  Transcripts in WORM archive (MinIO) cannot be retroactively modified.
  Schema evolution must produce a forward-only document format versioning
  strategy that allows old documents to be read by new readers without
  modification.

CHALLENGE 6 — TENANT DATA ISOLATION DURING MIGRATION
  A migration running across 1000 tenants must never mix tenant data.
  A migration that fails for one tenant must not affect other tenants.
  Per-tenant migration progress must be independently trackable.

CHALLENGE 7 — FLYWAY COORDINATION
  Flyway manages PostgreSQL schema migration scripts in a strictly ordered,
  versioned sequence. This agent must coordinate with Flyway to ensure
  that agent-level migration logic and DB-level DDL changes are always
  executed in the correct order with the correct gates between them.
```

> The `TRANSCRIPT_SCHEMA_VERSION_MIGRATION_AGENT` is the **zero-downtime schema evolution
> orchestrator** that plans, validates, gates, executes, monitors, and rolls back all
> transcript schema version migrations across every data store, every consumer,
> and every tenant — without interrupting live platform operations.

### 2.2 What This Agent Does NOT Do

```
✗  Does not produce transcripts              — that is AGTAA-002's responsibility
✗  Does not version transcripts              — that is AGTVA-003's responsibility
✗  Does not validate session events          — that is AGTAA-002's responsibility
✗  Does not write application code           — migration scripts are human-authored,
                                               this agent validates and executes them
✗  Does not make schema design decisions     — it enforces decisions made by
                                               DATA_GOVERNANCE_AGENT and human architects
✗  Does not adjudicate consumer upgrade SLAs — it enforces them
✗  Does not archive transcripts              — that is IMMUTABLE_ARCHIVE_SERVICE
```

### 2.3 Upstream Agents (Feed This Agent)

```
DATA_GOVERNANCE_AGENT                  → Schema evolution declaration + migration authorization
ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPAT   → Schema compatibility matrix + consumer registry
AGENT_VERSION_COMPATIBILITY_AGENT      → Consumer version declarations + upgrade SLA tracking
TRANSCRIPT_VERSIONING_AGENT (AGTVA-003)→ Schema migration version creation trigger
TRANSCRIPT_AGGREGATION_AGENT (AGTAA-002)→ New schema version production trigger
ZERO_DOWNTIME_MIGRATION_AGENT          → Deployment strategy coordination
AUDIT_COMPLIANCE_AGENT                 → Compliance-driven migration requirements
LEGAL_POLICY_AGENT                     → Legal retention format requirements
DEVSECOS_AGENT                         → CI/CD pipeline integration for migration gates
OBSERVABILITY_AGENT                    → Migration health metrics consumer
```

### 2.4 Downstream Agents (Depend on This Agent)

```
TRANSCRIPT_AGGREGATION_AGENT (AGTAA-002)   → Notified when to produce new schema version
TRANSCRIPT_VERSIONING_AGENT (AGTVA-003)    → Receives SCHEMA_MIGRATION version trigger
SCORING_ENGINE                             → Receives consumer upgrade gate clearance
GD_POST_SESSION_ANALYTICS_AGENT           → Receives ClickHouse migration completion signal
INTELLIGENCE_SCORING_ML_AGENT             → Receives schema compatibility clearance
PARENT_LLM_INSIGHT_NARRATIVE_AGENT        → Receives schema-safe production signal
PASSPORT_AGGREGATION_AGENT                → Receives compatibility gate clearance
LEGAL_DOCUMENT_GENERATION_SERVICE         → Receives MinIO document format migration signal
AUDIT_COMPLIANCE_AGENT                    → Receives migration audit trail
OBSERVABILITY_AGENT                       → Receives migration phase metrics
IMMUTABLE_ARCHIVE_SERVICE                 → Receives MinIO schema migration instructions
ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPAT      → Receives updated compatibility matrix
AGENT_VERSION_COMPATIBILITY_AGENT         → Receives consumer upgrade notifications
```

---

## SECTION 3 — SCHEMA VERSION TAXONOMY

### 3.1 Schema Version Identifier Format

```
SCHEMA_VERSION_ID : TS-SCHEMA-vMAJOR.MINOR.PATCH

MAJOR : Breaking change — existing field removed, field type changed,
        required field added, array structure changed.
        Requires full consumer upgrade before old schema support is sunset.
        Dual-write period: minimum 30 days.
        Consumer upgrade SLA: 60 days from MAJOR release.

MINOR : Non-breaking addition — new optional field added,
        new enum value added to existing field,
        new nested object added with all-optional fields.
        Consumers not required to upgrade immediately.
        Consumers may ignore unknown fields (must be documented in consumer contract).
        Dual-write period: minimum 7 days.

PATCH : Non-structural fix — documentation correction, validation rule tightening,
        constraint clarification. No field structure change.
        No consumer upgrade required.
        No dual-write period needed.
```

### 3.2 Affected Data Stores Per Change Type

```yaml
STORE_IMPACT_MATRIX:

  MAJOR_CHANGE:
    PostgreSQL    : DDL migration via Flyway (ALTER TABLE or new table) — REQUIRED
    ClickHouse    : Schema evolution (new column or table ALTER) — REQUIRED
    OpenSearch    : Index mapping update or re-index — REQUIRED
    Kafka         : Schema registry update (Avro/JSON schema) — REQUIRED
    Redis         : Cache key structure update or TTL policy change — CONDITIONAL
    MinIO         : New document format version — REQUIRED (new version tag, old remains)
    Flutter App   : Widget schema update — REQUIRED (UI contract broken)
    React Web     : Page schema update — REQUIRED (SEO schema broken)
    Consumers     : All declared consumers must upgrade within SLA

  MINOR_CHANGE:
    PostgreSQL    : DDL migration via Flyway (ADD COLUMN) — REQUIRED
    ClickHouse    : ADD COLUMN — REQUIRED
    OpenSearch    : Mapping update (add field) — REQUIRED
    Kafka         : Schema registry update (add field) — REQUIRED
    Redis         : No structural change — OPTIONAL (cache invalidation only)
    MinIO         : Document format remains compatible — no version bump needed
    Flutter App   : No breaking change — optional upgrade recommended
    React Web     : No breaking change — optional upgrade recommended
    Consumers     : Encouraged but not required to upgrade

  PATCH_CHANGE:
    PostgreSQL    : No DDL — OPTIONAL (constraint update only)
    ClickHouse    : No change
    OpenSearch    : No change
    Kafka         : No change
    Redis         : No change
    MinIO         : No change
    Consumers     : No upgrade required
```

---

## SECTION 4 — INPUT CONTRACT (STRICT)

### 4.1 Trigger Events

This agent responds to exactly **four trigger event types**:

```yaml
TRIGGER_TYPE_1 — SCHEMA_EVOLUTION_DECLARED:
  event       : transcript.schema.evolution_declared
  source      : DATA_GOVERNANCE_AGENT
  payload     : {migration_request_id, current_schema_version, target_schema_version,
                 change_type (MAJOR|MINOR|PATCH), change_manifest,
                 authorized_by_human_actor, authorization_timestamp_utc,
                 requested_migration_start_utc, tenant_scope (ALL|specific_tenant_ids)}
  action      : Validate declaration → run pre-flight checks → produce MIGRATION_PLAN

TRIGGER_TYPE_2 — MIGRATION_EXECUTION_APPROVED:
  event       : transcript.schema.migration_approved
  source      : DATA_GOVERNANCE_AGENT (after human architect review of MIGRATION_PLAN)
  payload     : {migration_plan_id, approved_by_human_actor, approval_timestamp_utc,
                 approved_execution_window_utc_start, approved_execution_window_utc_end}
  action      : Execute MIGRATION_PLAN within approved window

TRIGGER_TYPE_3 — MIGRATION_ROLLBACK_REQUESTED:
  event       : transcript.schema.rollback_requested
  source      : DATA_GOVERNANCE_AGENT or ADMIN_GOVERNANCE_SERVICE
  payload     : {migration_plan_id, rollback_reason, rollback_authorized_by,
                 rollback_authorization_timestamp_utc, target_rollback_checkpoint}
  action      : Execute rollback to declared checkpoint

TRIGGER_TYPE_4 — CONSUMER_UPGRADE_STATUS_UPDATE:
  event       : transcript.schema.consumer_upgrade_status
  source      : AGENT_VERSION_COMPATIBILITY_AGENT
  payload     : {migration_plan_id, consumer_agent_id, consumer_agent_version,
                 upgrade_status (UPGRADED|IN_PROGRESS|BLOCKED|OVERDUE),
                 declared_compatible_schema_version}
  action      : Update consumer compatibility matrix; enforce SLA gates
```

### 4.2 Input Validation Schema

```json
INPUT_VALIDATION: {
  "required_fields_all_triggers": [
    "migration_request_id",
    "tenant_id",
    "trigger_type",
    "trigger_source_agent",
    "trigger_timestamp_utc",
    "request_signature"
  ],
  "trigger_specific_required": {
    "SCHEMA_EVOLUTION_DECLARED": [
      "current_schema_version", "target_schema_version", "change_type",
      "change_manifest", "authorized_by_human_actor", "authorization_timestamp_utc"
    ],
    "MIGRATION_EXECUTION_APPROVED": [
      "migration_plan_id", "approved_by_human_actor", "approval_timestamp_utc",
      "approved_execution_window_utc_start", "approved_execution_window_utc_end"
    ],
    "MIGRATION_ROLLBACK_REQUESTED": [
      "migration_plan_id", "rollback_reason", "rollback_authorized_by",
      "rollback_authorization_timestamp_utc", "target_rollback_checkpoint"
    ],
    "CONSUMER_UPGRADE_STATUS_UPDATE": [
      "migration_plan_id", "consumer_agent_id", "consumer_agent_version", "upgrade_status"
    ]
  },
  "validation_rules": [
    "migration_request_id must be UUID v4",
    "trigger_source_agent must be in AUTHORIZED_TRIGGER_SOURCES whitelist",
    "request_signature must be verified (HMAC-SHA256 via Vault-managed key)",
    "trigger_timestamp_utc must be within 120 seconds of server receipt (replay prevention)",
    "current_schema_version must match the ACTIVE version in SCHEMA_REGISTRY",
    "target_schema_version must be exactly one SEMVER increment ahead of current",
    "change_type must match actual diff between current and target schema manifests",
    "For MIGRATION_EXECUTION_APPROVED: migration_plan_id must reference a PLAN_READY plan",
    "For MIGRATION_ROLLBACK_REQUESTED: target_rollback_checkpoint must exist and be COMMITTED",
    "authorized_by_human_actor and approved_by_human_actor must be verified against Auth Service"
  ],
  "security_checks": [
    "Verify JWT of trigger_source_agent",
    "Verify HMAC request_signature",
    "Verify human actor identity against Auth Service for all authorization fields",
    "Reject if migration_request_id already exists (idempotency guard)",
    "Reject if approved_execution_window has already passed"
  ],
  "domain_checks": [
    "Verify no other migration is in EXECUTING state for the same schema stream",
    "Verify all stores in STORE_IMPACT_MATRIX are reachable before approving execution",
    "Verify all critical consumers are registered in compatibility matrix",
    "For MAJOR change: verify dual-write infrastructure is provisioned and tested"
  ]
}
```

### 4.3 Change Manifest Contract

The `change_manifest` field in SCHEMA_EVOLUTION_DECLARED must be a fully structured document:

```json
CHANGE_MANIFEST: {
  "manifest_id"               : "UUID",
  "schema_stream"             : "SESSION_TRANSCRIPT",
  "current_schema_version"    : "TS-SCHEMA-v1.0.0",
  "target_schema_version"     : "TS-SCHEMA-v1.1.0",
  "change_type"               : "MINOR",
  "change_author"             : "human architect actor_id",
  "change_description"        : "string — human-readable description",
  "field_changes": [
    {
      "change_id"             : "UUID",
      "operation"             : "ADD | DEPRECATE | TYPE_CHANGE | RENAME | REMOVE",
      "field_path"            : "string — e.g. turn_records[].intelligence_signal_vector",
      "field_type_before"     : "null (ADD) | string | float | object | array",
      "field_type_after"      : "object",
      "required_before"       : false,
      "required_after"        : false,
      "nullable_after"        : true,
      "default_value"         : "null",
      "deprecation_date"      : "ISO-8601 | null",
      "removal_target_version": "TS-SCHEMA-vX.X.X | null",
      "consumer_impact_class" : "NONE | ADDITIVE | BREAKING",
      "flyway_script_ref"     : "V20250601__add_intelligence_signal_vector.sql | null",
      "clickhouse_ddl_ref"    : "ch_migration_20250601_001.sql | null",
      "opensearch_mapping_ref": "os_mapping_20250601_001.json | null",
      "kafka_schema_ref"      : "kafka_schema_ts_v1_1_0.avsc | null"
    }
  ],
  "backward_compatible"       : true,
  "dual_write_required"       : false,
  "consumer_upgrade_required" : false,
  "migration_scripts_hash"    : "SHA-256 of all referenced script files",
  "manifest_hash"             : "SHA-256 of this manifest object"
}
```

---

## SECTION 5 — MIGRATION PLAN ENGINE

### 5.1 Pre-Flight Validation Suite (Runs Before Every Migration Plan Is Generated)

All pre-flight checks must PASS before a MIGRATION_PLAN is produced.
Any FAIL → HALT + REPORT with specific failure reason. No partial plans.

```yaml
PRE_FLIGHT_CHECKS:

  PF-001 — SCHEMA_DELTA_VALIDATION:
    action   : Compute actual diff between current and target schema manifests
    validate : Computed diff matches declared field_changes in change_manifest
    fail     : "Schema delta mismatch — declared changes do not match actual diff"

  PF-002 — ADD_ONLY_ENFORCEMENT:
    action   : Verify no existing field is REMOVED or TYPE_CHANGED in this migration
               (unless full deprecation cycle is complete — Section 7)
    fail     : "REMOVE or TYPE_CHANGE detected without completed deprecation cycle"

  PF-003 — FLYWAY_SCRIPT_INTEGRITY:
    action   : Verify all referenced Flyway SQL scripts exist, are syntactically valid,
               and have the correct version prefix (V{timestamp}__{description}.sql)
    validate : SHA-256 of each script matches declared hash in change_manifest
    fail     : "Flyway script missing, corrupt, or hash mismatch"

  PF-004 — STORE_REACHABILITY:
    action   : Health-check all stores listed in STORE_IMPACT_MATRIX for this change_type
    validate : All stores return healthy status within 5 seconds
    fail     : "Store {store_name} unreachable — migration cannot proceed"

  PF-005 — CONSUMER_REGISTRY_COMPLETENESS:
    action   : Verify all consumers of SESSION_TRANSCRIPT are registered in
               ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPATIBILITY_AGENT
    validate : Registered consumers >= MINIMUM_CONSUMER_COUNT (14 for this schema)
    fail     : "Consumer registry incomplete — unregistered consumers may break silently"

  PF-006 — ROLLBACK_SCRIPT_INTEGRITY:
    action   : Verify all rollback scripts exist and are valid for every migration script
    validate : For every forward script, a corresponding rollback script exists
    fail     : "Rollback script missing for forward script {script_name}"

  PF-007 — DUAL_WRITE_READINESS (MAJOR changes only):
    action   : Verify dual-write infrastructure (write to both old and new schema versions)
               is provisioned and tested for TRANSCRIPT_AGGREGATION_AGENT
    validate : DUAL_WRITE_TEST_STATUS = PASSED in feature flag registry (Unleash)
    fail     : "Dual-write not provisioned for MAJOR schema change"

  PF-008 — ACTIVE_MIGRATION_LOCK:
    action   : Verify no other migration is in EXECUTING or PAUSED state for this schema stream
    fail     : "Concurrent migration detected — only one active migration per schema stream"

  PF-009 — LEGAL_HOLD_CLEARANCE:
    action   : Verify no transcripts under LEGAL_HOLD will be affected by store-level DDL
    validate : Query AGTVA-003 for count of LEGAL_HOLD transcripts
               If count > 0 → require explicit legal clearance from LEGAL_POLICY_AGENT
    fail     : "Legal hold transcripts exist — require LEGAL_POLICY_AGENT clearance"

  PF-010 — EXECUTION_WINDOW_VALIDATION:
    action   : Validate proposed execution window
    validate : Window must be outside peak GD session hours (configurable per tenant)
               Window must be minimum 4 hours for MAJOR, 2 hours for MINOR
               Window must be in the future
    fail     : "Execution window invalid — overlaps peak hours or insufficient duration"

  PF-011 — FEATURE_FLAG_READINESS:
    action   : Verify Unleash feature flags for all migration phases are defined
               and set to DISABLED (will be enabled per phase during execution)
    fail     : "Feature flags not configured for migration phases"

  PF-012 — TENANT_ISOLATION_VERIFICATION:
    action   : For tenant-scoped migrations, verify migration jobs can be
               independently partitioned by tenant_id
    fail     : "Tenant isolation cannot be guaranteed for this migration scope"
```

### 5.2 Migration Plan Schema

```json
MIGRATION_PLAN: {
  "migration_plan_id"         : "UUID",
  "migration_request_id"      : "UUID",
  "schema_stream"             : "SESSION_TRANSCRIPT",
  "current_schema_version"    : "TS-SCHEMA-v1.0.0",
  "target_schema_version"     : "TS-SCHEMA-v1.1.0",
  "change_type"               : "MINOR",
  "plan_status"               : "PLAN_READY | APPROVED | EXECUTING | PAUSED |
                                 COMPLETED | ROLLED_BACK | FAILED",
  "plan_created_at_utc"       : "ISO-8601",
  "preflight_results"         : {"PF-001": "PASS", "PF-002": "PASS", ...},
  "preflight_completed_at_utc": "ISO-8601",

  "execution_phases"          : ["array of EXECUTION_PHASE objects — see Section 6"],

  "rollback_strategy"         : {
    "rollback_type"           : "FULL | PARTIAL_CHECKPOINT",
    "checkpoints"             : ["array of ROLLBACK_CHECKPOINT objects"],
    "max_rollback_window_min" : "integer — max time to complete full rollback",
    "rollback_tested"         : "boolean"
  },

  "dual_write_config"         : {
    "enabled"                 : "boolean",
    "dual_write_start_utc"    : "ISO-8601 | null",
    "dual_write_end_utc"      : "ISO-8601 | null",
    "old_schema_writer"       : "AGTAA-002-v{N}",
    "new_schema_writer"       : "AGTAA-002-v{N+1}"
  },

  "consumer_gates"            : [
    {
      "consumer_agent_id"     : "string",
      "gate_type"             : "UPGRADE_BEFORE_CUTOVER | UPGRADE_WITHIN_SLA | OPTIONAL",
      "minimum_version"       : "string",
      "gate_deadline_utc"     : "ISO-8601",
      "gate_status"           : "PENDING | CLEARED | OVERDUE | BLOCKED"
    }
  ],

  "tenant_scope"              : "ALL | specific_tenant_ids[]",
  "tenant_migration_states"   : [
    {
      "tenant_id"             : "UUID",
      "migration_status"      : "PENDING | IN_PROGRESS | COMPLETED | FAILED | ROLLED_BACK",
      "current_phase"         : "string",
      "phase_started_utc"     : "ISO-8601 | null",
      "phase_completed_utc"   : "ISO-8601 | null",
      "error_details"         : "string | null"
    }
  ],

  "estimated_duration_min"    : "integer",
  "zero_downtime_validated"   : "boolean",
  "model_version"             : "AGTSVM-004-v1.0.0",
  "audit_reference"           : "UUID"
}
```

---

## SECTION 6 — EXECUTION PIPELINE (ZERO-DOWNTIME, PHASED)

Every migration executes across **12 strictly ordered phases**. Each phase has an entry gate, an execution action, an exit validation, and a rollback checkpoint. No phase begins without prior phase gate clearance.

```
╔═══════════════════════════════════════════════════════════════════════╗
║  PHASE EXECUTION RULE: Every phase must complete fully or roll back.  ║
║  No phase may be skipped. No phase may partially execute.             ║
║  Every phase writes a ROLLBACK_CHECKPOINT before its first action.   ║
╚═══════════════════════════════════════════════════════════════════════╝
```

### Phase 0 — EXECUTION_LOCK

```yaml
ENTRY_GATE    : migration_plan.plan_status == APPROVED
                AND execution window is active
                AND PF-008 (no concurrent migration) confirmed fresh
ACTION        : Set migration_plan.plan_status = EXECUTING
                Acquire distributed lock: "schema_migration_lock:{schema_stream}"
                via Redis (TTL: execution window duration + 30 min buffer)
                Enable Unleash feature flag: migration.{migration_plan_id}.active
ROLLBACK_CP   : CHECKPOINT_0 — pre-migration state confirmed, lock held
EXIT_VALIDATE : Lock acquired, flag enabled, plan status = EXECUTING
```

### Phase 1 — CONSUMER_GATE_VALIDATION

```yaml
ENTRY_GATE    : CHECKPOINT_0 committed
ACTION        : Validate all UPGRADE_BEFORE_CUTOVER consumer gates are CLEARED
                Check AGENT_VERSION_COMPATIBILITY_AGENT for each critical consumer
                For MAJOR changes: ALL critical consumers must be cleared
                For MINOR changes: proceed if >= 80% of consumers cleared (log others)
ROLLBACK_CP   : CHECKPOINT_1 — gate validation result recorded
EXIT_VALIDATE : Gate clearance requirements met per change_type
FAIL_ACTION   : HALT — release lock — notify blocked consumers — set plan PAUSED
                Re-attempt permitted after consumer upgrade confirmation
```

### Phase 2 — DUAL_WRITE_ACTIVATION (MAJOR changes only; SKIP for MINOR/PATCH)

```yaml
ENTRY_GATE    : CHECKPOINT_1 committed AND change_type == MAJOR
ACTION        : Enable Unleash flag: migration.{id}.dual_write
                TRANSCRIPT_AGGREGATION_AGENT (AGTAA-002) begins writing
                BOTH old schema (v1.0.0) AND new schema (v1.1.0) simultaneously
                New-schema transcripts tagged: schema_version = target
                Old-schema transcripts continue: schema_version = current
                Dual-write verified via canary check (10 test transcripts)
ROLLBACK_CP   : CHECKPOINT_2 — dual-write active, canary verified
EXIT_VALIDATE : Canary transcripts validated against both schemas
FAIL_ACTION   : Disable dual-write flag → ROLLBACK to CHECKPOINT_1
```

### Phase 3 — POSTGRESQL_MIGRATION (Flyway)

```yaml
ENTRY_GATE    : CHECKPOINT_2 committed (or CHECKPOINT_1 for MINOR)
ACTION        : Execute Flyway migration scripts in declared order
                Scripts are ADD-ONLY (ADD COLUMN, CREATE TABLE, ADD CONSTRAINT)
                New columns added as NULLABLE with no DEFAULT that breaks existing rows
                Flyway records migration in flyway_schema_history (append-only)
                TENANT_SCOPE: if tenant-specific, run migrations per tenant schema
                Monitor: query response time before/after (< 5% degradation allowed)
ROLLBACK_CP   : CHECKPOINT_3 — PostgreSQL DDL committed, Flyway history updated
EXIT_VALIDATE : Flyway reports all scripts SUCCESSFUL
                SELECT on new columns returns NULL (not error) for all existing rows
                Index creation completed (no missing indices)
FAIL_ACTION   : Flyway rollback scripts executed → ROLLBACK to CHECKPOINT_2 or CHECKPOINT_1
```

### Phase 4 — CLICKHOUSE_MIGRATION

```yaml
ENTRY_GATE    : CHECKPOINT_3 committed
ACTION        : Execute ClickHouse DDL (ADD COLUMN to relevant MergeTree tables)
                ClickHouse ADD COLUMN is non-blocking (online schema change)
                New columns default to NULL or declared default value
                Verify: INSERT of new-schema record succeeds
                Verify: SELECT of old-schema record returns NULL for new column (not error)
ROLLBACK_CP   : CHECKPOINT_4 — ClickHouse DDL applied
EXIT_VALIDATE : Test INSERT and SELECT pass for both old and new schema records
FAIL_ACTION   : DROP COLUMN (ClickHouse supports this for recently added columns)
                → ROLLBACK to CHECKPOINT_3
```

### Phase 5 — OPENSEARCH_INDEX_MIGRATION

```yaml
ENTRY_GATE    : CHECKPOINT_4 committed
ACTION        : PUT mapping update to existing index (additive fields only)
                OpenSearch dynamic mapping update is non-breaking for ADD
                For MAJOR (re-index required):
                  Create new index: transcript_sessions_v{N+1}
                  Start index alias dual-write (old + new index)
                  Background re-index job: _reindex API call (async)
                For MINOR: PUT _mapping update directly (no re-index)
ROLLBACK_CP   : CHECKPOINT_5 — OpenSearch mapping updated
EXIT_VALIDATE : Test document PUT and GET pass for both schema versions
                For MAJOR: re-index job started and alias dual-write confirmed active
FAIL_ACTION   : Revert mapping (remove added fields) → ROLLBACK to CHECKPOINT_4
```

### Phase 6 — KAFKA_SCHEMA_REGISTRY_UPDATE

```yaml
ENTRY_GATE    : CHECKPOINT_5 committed
ACTION        : Register new schema version in Kafka Schema Registry
                (Confluent-compatible schema registry, self-hosted)
                Register as BACKWARD_COMPATIBLE (new fields are optional)
                For MAJOR: register as NEW_VERSION (compatibility mode: FORWARD)
                Update topic configuration to accept both schema versions
                Producers (AGTAA-002) declare compatibility with both versions
ROLLBACK_CP   : CHECKPOINT_6 — Kafka schema registry updated
EXIT_VALIDATE : Test message production and consumption under both schema versions
                Schema registry returns COMPATIBLE for backward compatibility check
FAIL_ACTION   : Delete new schema version registration → ROLLBACK to CHECKPOINT_5
```

### Phase 7 — REDIS_CACHE_INVALIDATION

```yaml
ENTRY_GATE    : CHECKPOINT_6 committed
ACTION        : Identify all Redis cache keys that reference schema-sensitive structures
                For VERSION_CHAIN_INDEX cache: flush keys matching pattern
                "version_chain:{tenant_id}:*" — forces cache rebuild on next read
                For MINOR: selective key invalidation (only affected structures)
                For MAJOR: full cache flush for transcript-related key namespaces
                Cache rebuild is lazy (on next read) — no downtime
ROLLBACK_CP   : CHECKPOINT_7 — Redis cache invalidated
EXIT_VALIDATE : Verify cache rebuild succeeds with new schema on first access
                No stale schema version served from cache
FAIL_ACTION   : Force cache rebuild with old schema → ROLLBACK to CHECKPOINT_6
```

### Phase 8 — MINIO_DOCUMENT_FORMAT_VERSIONING (MAJOR changes only; SKIP for MINOR/PATCH)

```yaml
ENTRY_GATE    : CHECKPOINT_7 committed AND change_type == MAJOR
ACTION        : Register new document format version in MinIO metadata schema
                Create new MinIO bucket path: transcripts/schema/v{N+1}/
                Old documents remain at: transcripts/schema/v{N}/  (IMMUTABLE)
                New transcripts produced by AGTAA-002 (new schema) go to v{N+1}/
                Old transcripts are NEVER migrated in-place (WORM protection)
                Create MinIO lifecycle policy for new path
                Update IMMUTABLE_ARCHIVE_SERVICE to route new-schema transcripts to new path
ROLLBACK_CP   : CHECKPOINT_8 — MinIO paths and policies configured
EXIT_VALIDATE : Test write to new path succeeds
                Test read from old path still succeeds (old documents intact)
FAIL_ACTION   : Delete new MinIO path configuration → ROLLBACK to CHECKPOINT_7
```

### Phase 9 — TRANSCRIPT_AGGREGATION_AGENT_CUTOVER

```yaml
ENTRY_GATE    : CHECKPOINT_8 committed (or CHECKPOINT_7 for MINOR)
ACTION        : Enable Unleash flag: migration.{id}.new_schema_production
                TRANSCRIPT_AGGREGATION_AGENT (AGTAA-002) switches to producing
                ONLY new schema version (TS-SCHEMA-v{N.N.N})
                For MAJOR: disable dual-write flag (stop old schema production)
                First new-schema transcript triggers AGTVA-003 SCHEMA_MIGRATION version
                Monitor: first 100 new-schema transcripts for validation errors
ROLLBACK_CP   : CHECKPOINT_9 — AGTAA-002 producing new schema exclusively
EXIT_VALIDATE : 100 new-schema transcripts produced, validated, and versioned cleanly
                SCORING_ENGINE successfully processes first 10 new-schema transcripts
FAIL_ACTION   : Re-enable old schema flag, disable new schema flag
                → ROLLBACK to CHECKPOINT_8
```

### Phase 10 — CONSUMER_COMPATIBILITY_GATE_VERIFICATION

```yaml
ENTRY_GATE    : CHECKPOINT_9 committed
ACTION        : Verify all registered consumers process new-schema transcripts correctly
                For UPGRADE_BEFORE_CUTOVER consumers: must be processing successfully
                For UPGRADE_WITHIN_SLA consumers: log current status, set SLA deadline
                Monitor OBSERVABILITY_AGENT for consumer error rates on new schema
                Sample check: 1000 new-schema transcripts through each consumer
ROLLBACK_CP   : CHECKPOINT_10 — consumer compatibility verified for live traffic
EXIT_VALIDATE : Zero critical consumer errors on new-schema transcripts
                All UPGRADE_BEFORE_CUTOVER consumers processing successfully
FAIL_ACTION   : Identify failing consumer → notify + halt that consumer's processing
                → ROLLBACK to CHECKPOINT_9 if critical consumer is failing
```

### Phase 11 — OPENSEARCH_RE-INDEX_COMPLETION (MAJOR changes only)

```yaml
ENTRY_GATE    : CHECKPOINT_10 committed AND change_type == MAJOR
ACTION        : Wait for background re-index job completion
                (or force completion if within execution window)
                Switch index alias from old index to new index
                Disable index alias dual-write
                Old index retained for 7 days (rollback window) then deleted
ROLLBACK_CP   : CHECKPOINT_11 — index alias switched to new index
EXIT_VALIDATE : Search queries return correct results from new index
                Old index still accessible (not deleted yet)
FAIL_ACTION   : Switch alias back to old index → ROLLBACK to CHECKPOINT_10
```

### Phase 12 — COMPLETION_AND_CLEANUP

```yaml
ENTRY_GATE    : CHECKPOINT_11 committed (or CHECKPOINT_10 for MINOR)
ACTION        : Update SCHEMA_REGISTRY: current_schema_version = target_schema_version
                Update ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPAT: compatibility matrix updated
                Notify AGENT_VERSION_COMPATIBILITY_AGENT: migration complete, SLA deadlines active
                Disable Unleash flags: migration.{id}.active, migration.{id}.dual_write
                Release distributed lock: "schema_migration_lock:{schema_stream}"
                Set migration_plan.plan_status = COMPLETED
                Emit: transcript.schema.migration_completed
                Schedule: PF-009 re-run after 24h (legal hold clearance confirmation)
                Log: final migration audit record
ROLLBACK_CP   : N/A — COMPLETED is terminal state
EXIT_VALIDATE : SCHEMA_REGISTRY reflects new version
                All Unleash migration flags are DISABLED
                Distributed lock released
                Migration plan status = COMPLETED
```

---

## SECTION 7 — DEPRECATION LIFECYCLE ENGINE

### 7.1 Field Deprecation Protocol (Mandatory Before Any REMOVE Operation)

No field may be removed in a single migration. All removals require a **minimum 2-version deprecation cycle**:

```
STEP 1 — DEPRECATION_DECLARATION (MINOR version):
  Add DEPRECATED annotation to field in schema manifest
  Set deprecation_date in change_manifest
  Set removal_target_version (must be >= 2 MINOR versions ahead)
  Notify all consumers: "Field {path} deprecated, to be removed in {version}"
  Consumers have until removal_target_version to stop consuming the field

STEP 2 — DEPRECATION_ENFORCEMENT (MINOR version):
  After deprecation_date:
  Log WARNING when deprecated field is populated in new transcripts
  Log WARNING when deprecated field is read by any consumer
  Begin tracking: consumer usage of deprecated field (via OBSERVABILITY_AGENT)
  Report: consumers still reading deprecated field to AGENT_VERSION_COMPATIBILITY_AGENT

STEP 3 — REMOVAL_ELIGIBILITY_CHECK (Before MAJOR version):
  Verify: zero consumers reading deprecated field (from OBSERVABILITY_AGENT telemetry)
  Verify: zero new transcripts populating deprecated field
  Verify: all declared consumers have acknowledged removal via version update
  If any check fails → EXTEND deprecation, do not proceed to STEP 4

STEP 4 — REMOVAL (MAJOR version):
  Execute REMOVE operation in change_manifest
  Flyway: DROP COLUMN (with careful timing — only when zero active read traffic)
  ClickHouse: DROP COLUMN
  OpenSearch: create new index without field (re-index required — triggers Phase 11)
  Kafka: remove from schema (FULL_TRANSITIVE compatibility break — new major version)
```

### 7.2 Breaking Change Declaration Requirements

For any `MAJOR` version migration, the following must be completed before MIGRATION_EXECUTION_APPROVED:

```yaml
BREAKING_CHANGE_REQUIREMENTS:
  - Human architect sign-off on change_manifest
  - DATA_GOVERNANCE_AGENT formal authorization
  - LEGAL_POLICY_AGENT clearance (ensures archive format compatibility)
  - All UPGRADE_BEFORE_CUTOVER consumers confirmed upgraded or in active upgrade sprint
  - Dual-write infrastructure tested in staging environment
  - Rollback tested in staging environment (rollback drill — mandatory)
  - MIGRATION_VALIDATION_ENGINE_ANTIGRAVITY sign-off on staging run
  - Zero active LEGAL_HOLD transcripts in affected stores (or LEGAL_POLICY_AGENT waiver)
```

---

## SECTION 8 — ROLLBACK ENGINE

### 8.1 Rollback Architecture

```yaml
ROLLBACK_TYPES:

  FULL_ROLLBACK:
    trigger   : Any phase failure where forward progress is not safe
    action    : Reverse all completed phases in reverse order
                Each phase has a documented and tested rollback script
    duration  : Maximum 30 minutes (enforced; alert if exceeded)
    guarantee : Platform returns to CHECKPOINT_0 state (pre-migration)

  CHECKPOINT_ROLLBACK:
    trigger   : Phase failure where prior phases are safe to retain
                (e.g., Phase 6 fails — PostgreSQL and ClickHouse changes are fine to keep)
    action    : Roll back only phases after the safe checkpoint
    requires  : Explicit authorization from DATA_GOVERNANCE_AGENT
                (default is FULL_ROLLBACK)

  CONSUMER_ROLLBACK:
    trigger   : Consumer compatibility failure after CHECKPOINT_9
                (new schema production is live but a consumer is breaking)
    action    : Re-enable dual-write (old schema re-produced for failing consumer)
                DOES NOT roll back store changes
                Consumer given 48-hour window to fix or explicit rollback authorization
```

### 8.2 Rollback Checkpoint Registry

```json
ROLLBACK_CHECKPOINT: {
  "checkpoint_id"           : "UUID",
  "checkpoint_number"       : "integer (0–11)",
  "migration_plan_id"       : "UUID",
  "checkpoint_committed_utc": "ISO-8601",
  "phase_name"              : "string",
  "store_states": {
    "postgresql"            : {"migration_applied": "string | null", "flyway_version": "string"},
    "clickhouse"            : {"ddl_applied": "boolean", "columns_added": ["string"]},
    "opensearch"            : {"mapping_version": "string", "alias_target": "string"},
    "kafka"                 : {"schema_version_registered": "string | null"},
    "redis"                 : {"cache_keys_invalidated": "integer"},
    "minio"                 : {"new_path_created": "boolean"}
  },
  "rollback_script_refs"    : {"postgresql": "string", "clickhouse": "string", ...},
  "rollback_tested"         : "boolean",
  "rollback_tested_at_utc"  : "ISO-8601 | null"
}
```

### 8.3 Rollback Execution Algorithm

```
ROLLBACK_ALGORITHM(target_checkpoint):

STEP 1 — VALIDATE ROLLBACK AUTHORIZATION
  Verify: rollback_authorized_by is human actor in ADMIN_GOVERNANCE_SERVICE
  Verify: target_rollback_checkpoint exists and is COMMITTED
  Log: ROLLBACK_INITIATED with authorization proof

STEP 2 — HALT FORWARD PROGRESS
  Disable Unleash flags: migration.{id}.new_schema_production
  Disable Unleash flags: migration.{id}.dual_write (if active)
  TRANSCRIPT_AGGREGATION_AGENT reverts to old schema production immediately

STEP 3 — IDENTIFY PHASES TO REVERSE
  phases_to_reverse = [all committed phases AFTER target_checkpoint] reversed

STEP 4 — EXECUTE PHASE REVERSALS (reverse order)
  FOR each phase in phases_to_reverse:
    Execute phase rollback script
    Validate phase reversal success (query/test each store)
    If reversal fails → ESCALATE to DATA_GOVERNANCE_AGENT (cannot auto-recover)
    Log: PHASE_REVERSED

STEP 5 — VERIFY PLATFORM STATE
  Compare all store states to CHECKPOINT_{target} snapshot
  Verify consumers are processing old-schema transcripts correctly
  Verify SCHEMA_REGISTRY still reflects old schema as ACTIVE

STEP 6 — RELEASE AND NOTIFY
  Release distributed lock: "schema_migration_lock:{schema_stream}"
  Set migration_plan.plan_status = ROLLED_BACK
  Emit: transcript.schema.migration_rolled_back
  Notify all consumers: old schema is active
  Notify DATA_GOVERNANCE_AGENT: rollback complete, root cause investigation required

STEP 7 — POST-ROLLBACK AUDIT
  Write comprehensive rollback audit record
  Trigger: OBSERVABILITY_AGENT dashboard refresh
  Mandatory: root cause analysis within 48 hours before re-attempt
```

---

## SECTION 9 — ML / AI LOGIC LAYER

### 9.1 Architecture Overview

```
AI MUST ASSIST ML — AI MUST NOT REPLACE ML
All migration execution decisions are rule-primary.
ML layer handles risk prediction and impact estimation.
AI layer is used ONLY for documentation generation and anomaly narratives.
```

### 9.2 ML Layer — Migration Risk Scorer

Predicts migration risk before plan approval to inform human architect decision.

```yaml
MODEL_TYPE          : Gradient Boosted Multi-Output Regressor
OUTPUTS             :
  - estimated_duration_min     (regression)
  - rollback_probability       (regression 0.0–1.0)
  - consumer_break_probability (regression 0.0–1.0)
  - store_impact_severity      (classification: LOW | MEDIUM | HIGH | CRITICAL)

FEATURES_USED       :
  - change_type                    (MAJOR | MINOR | PATCH)
  - field_changes_count            (number of field changes in manifest)
  - breaking_changes_count         (consumer_impact_class == BREAKING count)
  - stores_affected_count          (number of stores in STORE_IMPACT_MATRIX)
  - consumers_requiring_upgrade    (count from consumer registry)
  - current_active_session_rate    (sessions/hour at planned migration time)
  - historical_migration_duration  (prior migrations of same change_type)
  - historical_rollback_rate       (platform rollback frequency)
  - tenant_count_in_scope          (migration tenant scope)
  - legal_hold_transcript_count    (from AGTVA-003)
  - dual_write_required            (boolean)

THRESHOLD:
  rollback_probability > 0.40     → FLAG as HIGH_RISK → require additional human review
  consumer_break_probability > 0.30 → FLAG → require consumer compatibility pre-validation
  store_impact_severity == CRITICAL → FLAG → require DATA_GOVERNANCE_AGENT escalation

TRAINING_FREQUENCY  : After every completed migration (online learning + monthly batch retrain)
DRIFT_DETECTION     : Monitor prediction accuracy vs actual outcomes; alert if MAE > 15%
VERSION_CONTROL     : Model version stored in every MIGRATION_PLAN's audit_reference
```

### 9.3 ML Layer — Consumer SLA Predictor

Predicts which consumers will miss upgrade SLAs based on historical upgrade velocity.

```yaml
MODEL_TYPE          : Time-Series Forecasting (Prophet or ARIMA per consumer)
FEATURES_USED       :
  - consumer historical upgrade lag (days from notice to upgrade)
  - current sprint capacity (from engineering tracker if available, else historical avg)
  - change_type complexity score
  - consumer's declared schema version at time of notification

OUTPUT              : predicted_upgrade_date per consumer + confidence interval
USE                 : Set realistic gate_deadline_utc in MIGRATION_PLAN consumer_gates
ALERT               : If predicted_upgrade_date > gate_deadline_utc → FLAG to
                      AGENT_VERSION_COMPATIBILITY_AGENT for SLA negotiation
```

### 9.4 AI Assist Layer (Scoped — 20%)

```yaml
AI_USAGE_SCOPE:
  - Generate human-readable MIGRATION_PLAN summary for architect review
  - Generate consumer upgrade notification messages (per consumer_agent_id)
  - Generate rollback incident report narrative for DATA_GOVERNANCE_AGENT
  - Generate post-migration retrospective summary

PROMPT_GOVERNANCE:
  - All prompts versioned (AGTSVM-AI-PROMPT-vX.Y.Z)
  - AI output is advisory and narrative only
  - AI output NEVER modifies MIGRATION_PLAN structured fields
  - AI output tagged with ai_generated: true
  - Prompt version stored in audit_reference

AI MUST NOT:
  - Approve or reject migration plans
  - Authorize rollbacks
  - Execute any migration phase
  - Access cross-tenant schema data
  - Generate migration scripts (scripts are human-authored)
```

---

## SECTION 10 — CONSUMER COMPATIBILITY MANAGEMENT

### 10.1 Consumer Registry (Mandatory — All Must Be Registered)

```yaml
REGISTERED_CONSUMERS:
  CRITICAL (UPGRADE_BEFORE_CUTOVER for MAJOR):
    - SCORING_ENGINE
    - TRANSCRIPT_VERSIONING_AGENT (AGTVA-003)
    - AUDIT_COMPLIANCE_AGENT
    - LEGAL_DOCUMENT_GENERATION_SERVICE

  IMPORTANT (UPGRADE_WITHIN_SLA — 30 days for MAJOR, 7 days for MINOR):
    - GD_POST_SESSION_ANALYTICS_AGENT
    - INTELLIGENCE_SCORING_ML_AGENT
    - PARENT_LLM_INSIGHT_NARRATIVE_AGENT
    - PASSPORT_AGGREGATION_AGENT
    - ADMIN_GOVERNANCE_SERVICE
    - FORENSICS_AGENT

  OPTIONAL (UPGRADE_RECOMMENDED — no hard deadline):
    - BEHAVIOR_ANALYTICS_AGENT
    - OBSERVABILITY_AGENT
    - FRAUD_DETECTION_ENGINE
    - CAREER_PROBABILITY_MODEL
```

### 10.2 Consumer Notification Protocol

On `MIGRATION_PLAN` reaching `APPROVED` status:

```yaml
NOTIFICATION_SEQUENCE:
  T+0h  : Emit transcript.schema.migration_announced → ALL consumers
           Payload: {target_schema_version, change_type, field_changes_summary,
                     gate_type, gate_deadline_utc, change_manifest_url}

  T+7d  : Re-notify UPGRADE_BEFORE_CUTOVER consumers not yet upgraded
           Escalate to AGENT_VERSION_COMPATIBILITY_AGENT for follow-up

  T+14d : Final warning to UPGRADE_BEFORE_CUTOVER consumers
           If not upgraded → escalate to DATA_GOVERNANCE_AGENT for decision:
           delay migration OR proceed with consumer marked as incompatible

  T+{migration_day} : Pre-migration verification of all UPGRADE_BEFORE_CUTOVER gates
                      If any not cleared → HALT at Phase 1 (CONSUMER_GATE_VALIDATION)
```

---

## SECTION 11 — SECURITY ENFORCEMENT

```yaml
ZERO_TRUST_POLICY:
  - Every trigger validated: source agent JWT + HMAC request_signature
  - Every migration step requires re-validation of execution lock ownership
  - No implicit trust between phases — each phase re-verifies prerequisites
  - Replay window: 120 seconds (timestamp + HMAC prevents replay)

TENANT_ISOLATION:
  - Platform-wide DDL migrations run once (schema is shared)
  - Data migrations (backfill, reformat) run per-tenant with isolation
  - Per-tenant migration failures do not affect other tenants
  - Migration jobs partitioned by tenant_id in Kafka

AUTHORIZATION_REQUIREMENTS:
  - MIGRATION_PLAN generation: DATA_GOVERNANCE_AGENT authorization
  - MIGRATION_EXECUTION_APPROVED: human architect actor_id + DATA_GOVERNANCE_AGENT
  - ROLLBACK: human actor_id + DATA_GOVERNANCE_AGENT or ADMIN_GOVERNANCE_SERVICE
  - No agent may self-authorize any migration action

MIGRATION_SCRIPT_SECURITY:
  - All Flyway scripts are SHA-256 verified before execution
  - Scripts are stored in version-controlled repository (Forgejo CI)
  - Scripts reviewed by human architect before inclusion in change_manifest
  - No dynamically generated DDL — all scripts are static and pre-approved

ENCRYPTION:
  - All MIGRATION_PLAN records encrypted at rest (AES-256, Vault-managed)
  - Migration audit records HMAC-signed
  - All store communication over TLS 1.3

ACCESS_CONTROL:
  WRITE (migration plan)     : DATA_GOVERNANCE_AGENT + human architect
  EXECUTE (migration phases) : This agent only (with distributed lock)
  READ (migration status)    : All registered consumers + OBSERVABILITY_AGENT
  ROLLBACK                   : DATA_GOVERNANCE_AGENT + ADMIN_GOVERNANCE_SERVICE + human
  SCHEMA_REGISTRY_UPDATE     : This agent only (after COMPLETION)
```

---

## SECTION 12 — AUDIT & TRACEABILITY

Every migration operation writes the following immutable record:

```json
AUDIT_RECORD: {
  "timestamp_utc"              : "ISO-8601",
  "actor_id"                   : "TRANSCRIPT_SCHEMA_VERSION_MIGRATION_AGENT",
  "agent_version"              : "AGTSVM-004-v1.0.0",
  "operation_type"             : "PREFLIGHT_RUN | PLAN_GENERATED | MIGRATION_APPROVED |
                                  PHASE_STARTED | PHASE_COMPLETED | PHASE_FAILED |
                                  CHECKPOINT_COMMITTED | ROLLBACK_INITIATED |
                                  ROLLBACK_PHASE_REVERSED | MIGRATION_COMPLETED |
                                  CONSUMER_NOTIFIED | GATE_CLEARED | GATE_OVERDUE |
                                  DEPRECATION_DECLARED | REMOVAL_EXECUTED",
  "migration_plan_id"          : "UUID",
  "migration_request_id"       : "UUID",
  "schema_stream"              : "SESSION_TRANSCRIPT",
  "current_schema_version"     : "string",
  "target_schema_version"      : "string",
  "change_type"                : "MAJOR | MINOR | PATCH",
  "phase_name"                 : "string | null",
  "checkpoint_number"          : "integer | null",
  "stores_affected"            : ["array of store names"],
  "authorized_by_human"        : "string",
  "input_hash"                 : "SHA-256 of trigger payload",
  "output_hash"                : "SHA-256 of produced artifact (plan or checkpoint)",
  "ml_risk_score"              : {"rollback_probability": 0.12, "store_impact": "LOW"},
  "ai_assist_used"             : false,
  "ai_prompt_version"          : null,
  "operation_duration_ms"      : "integer",
  "consumer_gates_cleared"     : "integer",
  "consumer_gates_pending"     : "integer",
  "tenant_scope"               : "string",
  "tenants_affected"           : "integer",
  "anomaly_flags"              : ["array"],
  "error_code"                 : "string | null"
}
```

**Audit store**: `IMMUTABLE_ARCHIVE_SERVICE` — append-only, WORM-style
**Retention**: 7 years minimum (matches transcript retention)
**Integrity**: Every audit record HMAC-signed; tamper detection on read

---

## SECTION 13 — FAILURE POLICY

```yaml
PREFLIGHT_FAILURE (any PF check fails):
  action      : HALT plan generation + DETAILED_FAILURE_REPORT
  stop_exec   : YES
  log_to      : DATA_GOVERNANCE_AGENT + AUDIT_COMPLIANCE_AGENT
  escalate_to : Specific owner per failed check (e.g., PF-003 → DevSecOps)
  retry       : After declared failure is resolved and re-submitted

PHASE_EXECUTION_FAILURE:
  action      : CHECKPOINT → EVALUATE → ROLLBACK (if forward not safe)
  stop_exec   : YES for this phase and all subsequent phases
  log_to      : OBSERVABILITY_AGENT + AUDIT_COMPLIANCE_AGENT
  escalate_to : DATA_GOVERNANCE_AGENT (within 5 minutes of phase failure)
  auto_rollback: FULL_ROLLBACK unless CHECKPOINT_ROLLBACK explicitly authorized
  retry       : Only after root cause resolved + new MIGRATION_EXECUTION_APPROVED trigger

DISTRIBUTED_LOCK_TIMEOUT (lost during migration):
  action      : PAUSE migration + SAFETY_CHECK on all stores
  stop_exec   : YES
  log_to      : OBSERVABILITY_AGENT
  escalate_to : DATA_GOVERNANCE_AGENT
  auto_action : Attempt lock re-acquisition once; if fails → HALT + human decision required

ROLLBACK_SCRIPT_FAILURE (during rollback execution):
  action      : ESCALATE IMMEDIATELY — cannot auto-recover
  stop_exec   : YES
  log_to      : FORENSICS_AGENT + DATA_GOVERNANCE_AGENT + AUDIT_COMPLIANCE_AGENT
  escalate_to : Platform engineering team + DATA_GOVERNANCE_AGENT
  note        : This is a CRITICAL incident — platform may be in inconsistent state

CONSUMER_GATE_OVERDUE (SLA deadline passed, consumer not upgraded):
  action      : NOTIFY + ESCALATE (do NOT auto-block)
  stop_exec   : NO (for IMPORTANT/OPTIONAL consumers)
               YES (for UPGRADE_BEFORE_CUTOVER consumers in MAJOR migration)
  log_to      : AGENT_VERSION_COMPATIBILITY_AGENT + DATA_GOVERNANCE_AGENT
  escalate_to : Consumer team lead + DATA_GOVERNANCE_AGENT
  note        : Human decision required: extend SLA or force deprecation

LEGAL_HOLD_CLEARANCE_MISSING:
  action      : HALT Phase 3 (PostgreSQL) until LEGAL_POLICY_AGENT provides clearance
  stop_exec   : YES
  log_to      : LEGAL_POLICY_AGENT + AUDIT_COMPLIANCE_AGENT
  escalate_to : LEGAL_POLICY_AGENT
  retry       : After legal clearance received via TRIGGER_TYPE_1 re-authorization

CONCURRENT_MIGRATION_DETECTED:
  action      : HALT immediately
  stop_exec   : YES
  log_to      : DATA_GOVERNANCE_AGENT + AUDIT_COMPLIANCE_AGENT
  escalate_to : DATA_GOVERNANCE_AGENT
  note        : Only one migration per schema stream is permitted at any time

NO_SILENT_FAILURES: ENFORCED — every failure path produces a structured audit record
```

---

## SECTION 14 — INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  DATA_GOVERNANCE_AGENT                  : schema evolution authorization + migration approval
  ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPAT   : schema compatibility matrix queries
  AGENT_VERSION_COMPATIBILITY_AGENT      : consumer version status updates
  TRANSCRIPT_AGGREGATION_AGENT (AGTAA-002): new schema production coordination
  TRANSCRIPT_VERSIONING_AGENT (AGTVA-003) : schema migration version creation
  ZERO_DOWNTIME_MIGRATION_AGENT          : deployment strategy coordination
  LEGAL_POLICY_AGENT                     : legal hold clearance
  DEVSECOPS_AGENT                        : CI/CD pipeline integration

DOWNSTREAM_AGENTS:
  TRANSCRIPT_AGGREGATION_AGENT (AGTAA-002): receives production cutover signal
  TRANSCRIPT_VERSIONING_AGENT (AGTVA-003) : receives schema migration version trigger
  SCORING_ENGINE                          : receives schema compatibility clearance
  GD_POST_SESSION_ANALYTICS_AGENT        : receives ClickHouse migration signal
  PARENT_LLM_INSIGHT_NARRATIVE_AGENT     : receives schema-safe production signal
  PASSPORT_AGGREGATION_AGENT             : receives compatibility clearance
  LEGAL_DOCUMENT_GENERATION_SERVICE      : receives MinIO format migration signal
  AUDIT_COMPLIANCE_AGENT                 : receives migration audit trail
  OBSERVABILITY_AGENT                    : receives migration phase metrics
  IMMUTABLE_ARCHIVE_SERVICE              : receives MinIO schema migration instructions
  ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPAT   : receives updated compatibility matrix
  AGENT_VERSION_COMPATIBILITY_AGENT      : receives consumer upgrade notifications

EVENT_TRIGGERS:
  CONSUMED:
    - transcript.schema.evolution_declared      : → PREFLIGHT + PLAN_GENERATION
    - transcript.schema.migration_approved      : → MIGRATION_EXECUTION
    - transcript.schema.rollback_requested      : → ROLLBACK_EXECUTION
    - transcript.schema.consumer_upgrade_status : → CONSUMER_GATE_UPDATE

  EMITTED:
    - transcript.schema.preflight_completed     : consumed by DATA_GOVERNANCE_AGENT
    - transcript.schema.plan_ready              : consumed by DATA_GOVERNANCE_AGENT
    - transcript.schema.migration_started       : consumed by ALL registered consumers
    - transcript.schema.migration_announced     : consumed by ALL registered consumers
    - transcript.schema.phase_completed         : consumed by OBSERVABILITY_AGENT
    - transcript.schema.migration_completed     : consumed by ALL agents
    - transcript.schema.migration_rolled_back   : consumed by ALL agents
    - transcript.schema.consumer_gate_overdue   : consumed by AGENT_VERSION_COMPAT
    - transcript.schema.deprecation_declared    : consumed by ALL registered consumers
    - transcript.schema.field_removal_executed  : consumed by ALL registered consumers
    - security.migration_authorization_failure  : consumed by ZERO_TRUST_AGENT
```

---

## SECTION 15 — PERFORMANCE MONITORING

```yaml
METRICS (via Prometheus → Grafana):

  preflight_success_rate_pct            : % of declared migrations passing all PF checks
  migration_phase_duration_p95_min      : 95th percentile duration per phase
  migration_total_duration_p95_min      : 95th percentile end-to-end migration time
  migration_rollback_rate_pct           : % of migrations requiring rollback
  consumer_gate_clearance_rate_pct      : % of consumers clearing gates on time
  consumer_gate_overdue_count           : Active overdue consumer gates
  phase_failure_count_by_phase          : Failure count broken down by phase name
  store_health_during_migration         : Response time delta per store (before vs during)
  dual_write_overhead_pct               : Performance overhead of dual-write phase
  rollback_duration_p95_min             : 95th percentile rollback completion time
  schema_version_lag_by_consumer        : Versions behind latest stable per consumer
  deprecation_field_read_count          : Reads of deprecated fields (target = 0)
  zero_downtime_compliance_pct          : % of migrations with zero consumer downtime

DASHBOARDS (Grafana):
  - Migration Pipeline Status (current active migration phases)
  - Store Health During Migration (response time heatmap)
  - Consumer Upgrade Compliance Timeline
  - Schema Version Distribution (current version per consumer)
  - Rollback History Timeline
  - Deprecation Field Usage Monitor
  - Pre-flight Check Pass Rate by Check ID
  - Migration Risk Score Distribution

ALERTS:
  - migration_rollback_rate > 10% (rolling 30 days) → ALERT DATA_GOVERNANCE_AGENT
  - phase_failure at Phase 3 (PostgreSQL) → PAGE DATA_GOVERNANCE_AGENT immediately
  - rollback_duration > 30min → PAGE platform engineering team
  - consumer_gate_overdue for UPGRADE_BEFORE_CUTOVER gate → PAGE consumer team lead
  - store response time delta > 20% during migration → PAUSE migration + PAGE
  - deprecation_field_read_count > 0 after removal_target_version → CRITICAL ALERT
  - dual_write_overhead > 30% → ALERT + evaluate infrastructure capacity
```

---

## SECTION 16 — VERSIONING POLICY (SELF-REFERENTIAL)

```yaml
THIS_AGENT_VERSION_FORMAT  : AGTSVM-004-vMAJOR.MINOR.PATCH
MUTATION_POLICY            : Add-only — no field removal from MIGRATION_PLAN schema
BACKWARD_COMPAT            : All prior MIGRATION_PLAN schemas must remain readable
ROLLBACK_SAFETY            : Previous agent version retained 90 days post-rollout
MIGRATION_DOC              : Required for every MINOR or MAJOR version bump of this agent
CHANGELOG                  : Append-only CHANGELOG.md co-versioned with this spec
AI_PROMPT_VERSION          : AGTSVM-AI-PROMPT-vX.Y.Z (versioned separately)
SCHEMA_REGISTRY_ENTRY      : This agent's own schema registered in
                             ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPATIBILITY_AGENT
SELF_VERSION_IN_EVERY_OUTPUT: model_version field mandatory in all outputs
```

---

## SECTION 17 — NON-NEGOTIABLE RULES

This agent **MUST NOT**:

```
✗  Execute any migration phase without human authorization
   (MIGRATION_EXECUTION_APPROVED requires human actor_id)
✗  Skip any pre-flight check for any reason, including urgency
✗  Execute multiple migration phases simultaneously
   (strictly ordered, one phase at a time)
✗  Execute without a committed ROLLBACK_CHECKPOINT before each phase's first action
✗  Modify any existing Flyway migration script after it has been applied
   (Flyway history is immutable — new scripts only)
✗  Remove any field from any store without a completed deprecation cycle
✗  Allow a MAJOR migration to proceed without dual-write infrastructure verified
✗  Release the distributed lock before COMPLETED or ROLLED_BACK state
✗  Allow two concurrent migrations on the same schema stream
✗  Migrate WORM-archived transcript documents in-place
   (new document format version to new path only — old documents untouched)
✗  Allow a consumer to read new-schema transcripts before its compatibility gate is cleared
   (for UPGRADE_BEFORE_CUTOVER consumers in MAJOR migrations)
✗  Generate migration scripts dynamically
   (all scripts must be human-authored, version-controlled, and hash-verified)
✗  Allow the AI layer to authorize, approve, or execute any migration action
✗  Proceed through Phase 9 (AGTAA-002 cutover) if Phase 10 consumer validation fails
   for a CRITICAL consumer
✗  Skip tenant isolation during data migration phases
✗  Emit a migration_completed event before SCHEMA_REGISTRY is updated
✗  Allow a rollback script to fail silently
✗  Proceed with schema removal before verifying zero consumer read traffic
   on the deprecated field via OBSERVABILITY_AGENT telemetry
✗  Execute outside the approved execution window
```

---

## SECTION 18 — DEPLOYMENT CONFIGURATION

```yaml
KUBERNETES_NAMESPACE    : ops
SERVICE_TYPE            : Deployment (low-replica — migration is rare, not high-frequency)
REPLICA_MIN             : 2 (HA for availability, not scale)
REPLICA_MAX             : 2 (migration must be singleton — distributed lock enforces)
RESOURCE_REQUEST        : 500m CPU, 512Mi RAM
RESOURCE_LIMIT          : 2000m CPU, 2Gi RAM
LIVENESS_PROBE          : /health/live
READINESS_PROBE         : /health/ready (checks all store connectivity + Vault)
STARTUP_PROBE           : 30s grace (schema registry load time)

KAFKA_TOPIC_IN          :
  - transcript.schema.versioning.queue   (partitioned by schema_stream)
KAFKA_TOPIC_OUT         :
  - transcript.schema.preflight_completed
  - transcript.schema.plan_ready
  - transcript.schema.migration_announced
  - transcript.schema.migration_started
  - transcript.schema.phase_completed
  - transcript.schema.migration_completed
  - transcript.schema.migration_rolled_back
  - transcript.schema.consumer_gate_overdue
  - transcript.schema.deprecation_declared
  - security.migration_authorization_failure

POSTGRES_USAGE          :
  - migration_plans (INSERT + status UPDATE only)
  - migration_checkpoints (INSERT-only)
  - migration_audit_records (INSERT-only)
  - flyway_schema_history (READ only — Flyway owns writes)

REDIS_USAGE             :
  - Distributed lock: "schema_migration_lock:{schema_stream}" (TTL = execution window)
  - Migration state cache: "migration_state:{migration_plan_id}" (TTL = 24h post-completion)
  - Consumer gate cache: "consumer_gate:{migration_plan_id}:{consumer_id}" (TTL = gate_deadline)

FLYWAY_INTEGRATION      :
  - Flyway managed as a Kubernetes Job (runs Flyway CLI on migration trigger)
  - Scripts mounted from version-controlled ConfigMap
  - Flyway history table is READ by this agent, WRITTEN by Flyway only

UNLEASH_FLAGS           :
  - migration.{migration_plan_id}.active
  - migration.{migration_plan_id}.dual_write
  - migration.{migration_plan_id}.new_schema_production

AIRFLOW_JOBS            :
  - Consumer upgrade SLA monitoring: daily check against gate deadlines
  - Deprecated field usage monitoring: hourly scan of OBSERVABILITY_AGENT telemetry
  - Post-migration retrospective trigger: 24h after COMPLETED status

SECRETS_MANAGED_BY      : HashiCorp Vault
  - Per-agent HMAC signing keys
  - Store connection credentials (PostgreSQL, ClickHouse, OpenSearch, Kafka, Redis, MinIO)
  - Schema registry credentials

ENVIRONMENTS            :
  - dev     : Mock store connections, no Flyway execution, plan-only mode
  - staging : Full execution with test stores — mandatory rollback drill before prod
  - prod    : Full stack, Vault prod, all stores, human approval gates enforced
```

---

## SECTION 19 — INTEGRATION ARCHITECTURE MAP

```
DATA_GOVERNANCE_AGENT
  │ transcript.schema.evolution_declared
  ▼
TRANSCRIPT_SCHEMA_VERSION_MIGRATION_AGENT (this agent — AGTSVM-004)
  │
  ├── PREFLIGHT (12 checks) ──────────────────────────────────────────────────┐
  │   PF-001 Schema delta    PF-002 Add-only    PF-003 Flyway hash            │
  │   PF-004 Store health    PF-005 Consumer registry  PF-006 Rollback scripts│
  │   PF-007 Dual-write      PF-008 No concurrent  PF-009 Legal hold          │
  │   PF-010 Window valid    PF-011 Feature flags  PF-012 Tenant isolation    │
  │                                                                            │
  ▼                                                                            │
MIGRATION_PLAN (sent to DATA_GOVERNANCE_AGENT for human approval)             │
  │                                                                            │
  │ MIGRATION_EXECUTION_APPROVED                                               │
  ▼                                                                            │
PHASE 0  — Execution Lock (Redis distributed lock)                            │
PHASE 1  — Consumer Gate Validation                                           │
PHASE 2  — Dual-Write Activation (MAJOR only) ← AGTAA-002                    │
PHASE 3  — PostgreSQL Migration (Flyway) ← Flyway Job                        │
PHASE 4  — ClickHouse Migration                                               │
PHASE 5  — OpenSearch Index Migration                                         │
PHASE 6  — Kafka Schema Registry Update                                       │
PHASE 7  — Redis Cache Invalidation                                           │
PHASE 8  — MinIO Document Format Versioning (MAJOR only)                      │
PHASE 9  — AGTAA-002 Cutover to New Schema ← AGTAA-002                       │
PHASE 10 — Consumer Compatibility Gate Verification                           │
PHASE 11 — OpenSearch Re-index Completion (MAJOR only)                        │
PHASE 12 — Completion + Cleanup ← SCHEMA_REGISTRY updated                    │
  │                                                                            │
  │ ROLLBACK_REQUESTED ────────────────────────────────────────────────────────┘
  ▼
ROLLBACK ENGINE (reverse phase order, script-driven, audit-traced)
  │
  ├── ALL STORES RESTORED TO PRE-MIGRATION STATE
  ├── AGTAA-002 REVERTS TO OLD SCHEMA PRODUCTION
  └── DATA_GOVERNANCE_AGENT NOTIFIED FOR ROOT CAUSE ANALYSIS
```

---

## SECTION 20 — ANTI-PATTERNS EXPLICITLY PROHIBITED

| Anti-Pattern | Why Prohibited |
|---|---|
| Executing migration without pre-flight checks | Unvalidated migration — store inconsistency risk |
| Skipping ROLLBACK_CHECKPOINT before each phase | No safe recovery point — catastrophic on failure |
| In-place migration of WORM-archived transcripts | Destroys immutability — legal and compliance violation |
| Dynamically generating DDL at runtime | Unreviewed DDL — untestable, security risk |
| Allowing concurrent migrations on same schema | Race condition — guaranteed inconsistency |
| Removing a field without deprecation cycle | Silent consumer breaks — zero recovery path |
| MAJOR migration without dual-write testing | Consumers receive unreadable transcripts during cutover |
| Migrating stores out of declared phase order | Partial consistency window — consumers break |
| Releasing distributed lock before terminal state | Another migration starts on corrupted state |
| Consumer consuming new-schema before gate cleared | Silent data corruption in consumer's outputs |
| Trusting Flyway success without exit validation | Flyway may report success with partial DDL |
| Rollback script failure handled silently | Platform left in inconsistent multi-store state |
| Skipping tenant isolation in data migrations | Cross-tenant data contamination |
| AI layer approving or executing any phase | Non-deterministic — governance contract broken |
| Schema field removal without zero-read verification | Consumers still reading field — data loss |
| Emitting migration_completed before schema registry update | Consumers believe migration is complete but registry disagrees |

---

## SECTION 21 — FINAL LOCK DECLARATION

```
╔══════════════════════════════════════════════════════════════════════════════════════════════╗
║                          AGENT SPECIFICATION — SEALED                                        ║
║                                                                                              ║
║  This document is the authoritative, governing specification for the                        ║
║  TRANSCRIPT_SCHEMA_VERSION_MIGRATION_AGENT operating within the Ecoskiller                  ║
║  Antigravity platform.                                                                       ║
║                                                                                              ║
║  Any deviation, abbreviation, re-interpretation, or undocumented extension of this          ║
║  specification constitutes a compliance violation and MUST be escalated to                  ║
║  ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT before any implementation proceeds.               ║
║                                                                                              ║
║  AGENT_ID          : AGTSVM-004                                                              ║
║  VERSION           : v1.0.0                                                                  ║
║  SCHEMA_STREAM     : SESSION_TRANSCRIPT (primary) + all derived store schemas               ║
║  STORES_GOVERNED   : PostgreSQL · ClickHouse · OpenSearch · Kafka · Redis · MinIO           ║
║  SEALED BY         : ECOSKILLER INTELLIGENCE & INNOVATION CORE                              ║
║  GOVERNANCE        : ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT                               ║
║  DATA AUTHORITY    : DATA_GOVERNANCE_AGENT                                                   ║
║  SCHEMA AUTHORITY  : ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPATIBILITY_AGENT                      ║
║  MIGRATION LOCK    : Add-only schema evolution — field removal requires deprecation cycle   ║
║  EXECUTION AUTH    : Human declaration only — no autonomous migration execution             ║
║  STATUS            : ACTIVE · PRODUCTION-READY                                              ║
╚══════════════════════════════════════════════════════════════════════════════════════════════╝
```
