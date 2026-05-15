# 🔒 PASSPORT_HISTORY_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED AGENT SPECIFICATION
### Artifact Class: Production Agent Blueprint
### Mutation Policy: ADD-ONLY via version bump
### Execution Mode: DETERMINISTIC + VALIDATED
### Creative Interpretation: FORBIDDEN
### Assumption Filling: FORBIDDEN
### Default Behavior: DENY
### Failure Mode: STOP_EXECUTION
### Audit Standard: IMMUTABLE APPEND-ONLY

---

> ⚠️ SEAL NOTICE: This document is a locked production artifact for the Ecoskiller Antigravity SaaS platform. No section may be altered, reinterpreted, or partially implemented. All changes require a formal version bump with full audit trail. Any implementation that deviates from this specification is an INVALID BUILD and must STOP EXECUTION immediately.

---

## 1️⃣ AGENT IDENTITY (MANDATORY — LOCKED)

```yaml
AGENT_NAME:              PASSPORT_HISTORY_AGENT
AGENT_ID:                PHA-002
VERSION:                 v1.0.0
DEPENDENCY_CHAIN:        Downstream of PASSPORT_AGGREGATION_AGENT (PAA-001)
SYSTEM_ROLE:             Immutable Credential Timeline Recorder, Version Archivist & Change Explainer
PRIMARY_DOMAIN:          User Credential History | Passport Version Governance | Audit Trail Management | Change Intelligence
EXECUTION_MODE:          Deterministic + Validated
DATA_SCOPE:              Per-User | Per-Tenant | Per-Domain Track | All-Time Historical
TENANT_SCOPE:            Strict Isolation (Zero Cross-Tenant Access — Hard Enforced)
FAILURE_POLICY:          HALT on ambiguity — No silent failure — No partial history write
CREATIVE_INTERPRETATION: FORBIDDEN
ASSUMPTION_FILLING:      FORBIDDEN
MUTATION_POLICY:         Add-only | Versioned | Backward-compatible
SECURITY_MODEL:          Zero-Trust Multi-Tenant
DATA_POLICY:             Append-Only Immutable Ledger — HISTORY RECORDS NEVER MUTATED
PLATFORM:                Ecoskiller Antigravity
ARCHITECTURE:            Microservices + Event-Driven
SCALE_TARGET:            10M–100M users
COMPLIANCE_MODE:         ENABLED
DUPLICATION:             FORBIDDEN
CONFLICT:                DENY
```

---

## 2️⃣ PURPOSE DECLARATION

### What Problem This Agent Solves

The **PASSPORT_AGGREGATION_AGENT (PAA-001)** produces the current state of a user's credential passport. However, the platform demands something equally critical: a **complete, tamper-proof, human-readable, and machine-queryable record of every single change** that has ever occurred to a user's passport across their entire lifetime on the platform.

Without this agent, the following critical platform guarantees cannot be upheld:

- **Recruiters** cannot verify *when* a skill was acquired or *under what conditions* a belt was earned — only that it currently exists
- **Institute admins and compliance officers** cannot audit the sequence of events that led to a user's current trust tier
- **Users** cannot understand their own progression, regression, or how their profile evolved over time
- **Dispute resolution systems** cannot reconstruct the state of a passport at an arbitrary historical timestamp
- **Legal and compliance layers** (GDPR, India DPDP, enterprise audits) cannot produce evidence of what data existed at what point in time
- **The Appeals & Governance Board** cannot review contested certification or belt promotion decisions against historical evidence
- **AI/ML systems** cannot train on temporal passport sequences without a reliable, ordered, versioned history store
- **Anti-collusion and anti-manipulation engines** cannot detect abnormal passport velocity without tracking historical delta patterns

The **PASSPORT_HISTORY_AGENT** solves all of the above by serving as the single authoritative keeper of the **immutable credential timeline** — a permanent, ordered, versioned ledger of every state the user's passport has ever held, every change that caused each transition, and every actor/agent responsible for that change.

This agent does NOT decide what changes. It records what happened, enriches each change with contextual metadata, computes delta intelligence between versions, and makes the timeline queryable by authorized consumers.

### Core Responsibilities (All Mandatory)

1. **Receive** every `PASSPORT_VERSION_BUMPED` event from `PASSPORT_AGGREGATION_AGENT`
2. **Archive** the full User Passport Object (UPO) snapshot for every version, permanently
3. **Compute and store the precise delta** between each consecutive passport version
4. **Classify the nature** of each change (belt progression, skill gain, trust tier shift, anomaly flag, etc.)
5. **Enrich** each history entry with causal metadata (which upstream event triggered it, which agent, which actor)
6. **Publish** queryable history timelines to authorized downstream consumers
7. **Enforce immutability** — history records are never modified, never deleted
8. **Detect velocity anomalies** in passport change patterns (e.g., suspiciously rapid progressions)
9. **Serve legal and compliance exports** of historical passport states
10. **Provide point-in-time passport reconstruction** for any historical timestamp

### What Input It Consumes

Structured events from `PASSPORT_AGGREGATION_AGENT` exclusively, plus query requests from authorized consumers. See Section 3.

### What Output It Produces

- **History Ledger Entries (HLE)** — immutable per-version archive records
- **Delta Intelligence Objects (DIO)** — computed change analysis between consecutive versions
- **Timeline Query Responses** — structured, paginated, versioned timeline data
- **Point-in-Time Passport Snapshots** — full UPO reconstruction at any historical moment
- **Compliance Export Packages** — legal-grade chronological evidence bundles
- **Velocity Anomaly Alerts** — structured events for suspicious change patterns
- **Progress Narrative Feeds** — AI-assisted human-readable change summaries for user-facing UI

### Downstream Agents That Depend on It

```yaml
DOWNSTREAM_CONSUMERS:
  - RECRUITER_DASHBOARD_AGENT       → queries passport timeline for candidate credential verification
  - COMPLIANCE_AUDIT_AGENT          → reads full history for audit and legal review
  - DISPUTE_RESOLUTION_AGENT        → reconstructs point-in-time passport for contested events
  - APPEALS_GOVERNANCE_AGENT        → reads versioned history for belt/certification appeals
  - ANTI_COLLUSION_AGENT            → receives velocity anomaly events for investigation
  - PUBLIC_PROFILE_AGENT            → reads progression milestones for SEO timeline display (React/Next.js)
  - PARENT_VISIBILITY_AGENT         → reads child history in read-only stripped format
  - OBSERVABILITY_AGENT             → receives performance and health metrics
  - NOTIFICATION_AGENT              → receives milestone and anomaly triggers for user alerts
  - FEATURE_STORE_AGENT             → receives temporal feature vectors (passport change velocity, delta signals)
  - LEGAL_EXPORT_AGENT              → requests full compliance export packages (GDPR/DPDP)
  - ML_TRAINING_PIPELINE_AGENT      → reads ordered versioned history sequences for model training
```

### Upstream Agents That Feed It

```yaml
UPSTREAM_PRODUCERS:
  - PASSPORT_AGGREGATION_AGENT (PAA-001)
      → MANDATORY AND ONLY SOURCE OF PASSPORT_VERSION_BUMPED EVENTS
      → No other agent may write to this agent's input
      → Direct writes from any other source = SECURITY VIOLATION + STOP_EXECUTION
```

---

## 3️⃣ INPUT CONTRACT (STRICT — LOCKED)

All inputs arrive as structured Kafka events. No direct DB writes by any external agent. No REST mutations. No polling. Event-only ingestion.

### Primary Input: PASSPORT_VERSION_BUMPED Event

```json
INPUT_SCHEMA_PRIMARY: {
  "required_fields": {
    "event_id":                "UUID v4 — unique per event emission",
    "event_type":              "PASSPORT_VERSION_BUMPED (ONLY accepted event type for writes)",
    "source_agent":            "PASSPORT_AGGREGATION_AGENT — validated against REGISTERED_SOURCES",
    "user_id":                 "string — validated against User Service registry",
    "tenant_id":               "string — validated, tenant-isolated",
    "domain_track":            "Arts | Commerce | Science | Technology | Administration",
    "timestamp_utc":           "ISO 8601 UTC — event emission time",
    "passport_id":             "UUID v4 — links to UPO in PASSPORT_STORE",
    "passport_version_new":    "semver — the new version just emitted by PAA",
    "passport_version_prior":  "semver | null (null only on first-ever version v1.0.0)",
    "full_upo_snapshot":       "Full User Passport Object JSON — complete snapshot at this version",
    "input_hash":              "SHA-256 of canonical full_upo_snapshot JSON",
    "audit_reference":         "UUID — links to PAA's audit log entry for this emission",
    "model_version":           "string — model version used by PAA for this emission",
    "confidence_score":        "float 0.0–1.0",
    "trigger_event_ids":       "array of upstream event_ids that caused this version bump"
  },
  "optional_fields": {
    "anomaly_flags":           "array — populated by PAA if signals were anomalous",
    "trigger_event_types":     "array of human-readable event type names (for enrichment)",
    "completeness_score":      "float 0.0–1.0",
    "trust_tier":              "string — current trust tier in this version"
  },
  "validation_rules": [
    "event_id must be UUID v4 and NOT previously processed (idempotency check)",
    "event_type must be exactly PASSPORT_VERSION_BUMPED — reject all others",
    "source_agent must be exactly PASSPORT_AGGREGATION_AGENT — reject all others",
    "user_id must exist in User Service registry",
    "tenant_id must match user's enrolled tenant — reject cross-tenant",
    "domain_track must be one of the 5 locked domain values",
    "timestamp_utc must be ISO 8601 UTC and not older than STALENESS_THRESHOLD (24h)",
    "passport_version_new must be strictly greater than passport_version_prior (semver comparison)",
    "passport_version_prior must match the last known version in history ledger for this user",
    "full_upo_snapshot must deserialize without error",
    "input_hash must equal SHA-256 of canonical serialization of full_upo_snapshot",
    "audit_reference must be UUID v4",
    "confidence_score must be float between 0.0 and 1.0 inclusive",
    "trigger_event_ids must be non-empty array"
  ],
  "security_checks": [
    "Reject events from any source_agent other than PASSPORT_AGGREGATION_AGENT",
    "Validate tenant_id isolation — reject cross-tenant payloads",
    "Validate domain_track matches user's enrolled domain",
    "Verify input_hash matches recomputed hash of full_upo_snapshot",
    "Verify JWT claim of emitting service matches PASSPORT_AGGREGATION_AGENT identity",
    "Reject events with timestamp_utc older than 24h STALENESS_THRESHOLD",
    "Reject if passport_version_new <= passport_version_prior (version regression attack)",
    "Reject if passport_version_prior does not match last ledger entry (version gap attack)"
  ],
  "null_tolerance_policy": "NULL FORBIDDEN in all required fields. Optional fields may be absent entirely — never null. Any required null triggers REJECT_EVENT + LOG_VALIDATION_FAILURE + EMIT_HISTORY_INTEGRITY_ALERT."
}
```

### Secondary Input: Query Requests (Read-Only Access — Authorized Consumers Only)

```json
INPUT_SCHEMA_QUERY: {
  "required_fields": {
    "request_id":         "UUID v4",
    "requester_id":       "actor_id (user_id or agent_id making the request)",
    "requester_role":     "STUDENT | RECRUITER | INSTITUTE_ADMIN | PLATFORM_ADMIN | COMPLIANCE_OFFICER | PARENT | AUTOMATION_AGENT",
    "request_type":       "TIMELINE_FETCH | POINT_IN_TIME_FETCH | DELTA_FETCH | COMPLIANCE_EXPORT | VELOCITY_REPORT",
    "target_user_id":     "user_id of the passport owner being queried",
    "tenant_id":          "string — requester's tenant context"
  },
  "optional_fields": {
    "from_version":       "semver — start of range",
    "to_version":         "semver — end of range",
    "from_timestamp_utc": "ISO 8601 UTC",
    "to_timestamp_utc":   "ISO 8601 UTC",
    "change_category":    "BELT | SKILL | PROJECT | CERTIFICATION | TRUST_TIER | GAMIFICATION | IDENTITY | EMPLOYMENT | EVALUATION | ANOMALY",
    "page_size":          "integer 1–100 (default: 20)",
    "page_cursor":        "string — cursor-based pagination token",
    "include_upo_snapshot": "boolean — whether to include full UPO in response (default: false)"
  },
  "authorization_rules": [
    "STUDENT: may only query own history — target_user_id must equal requester_id",
    "RECRUITER: may query candidate history only with explicit candidate consent flag active",
    "INSTITUTE_ADMIN: may query enrolled student history within own tenant only",
    "PLATFORM_ADMIN: read-only access with full audit trail — any user within platform scope",
    "COMPLIANCE_OFFICER: read-only access with full audit trail — compliance export enabled",
    "PARENT: read-only stripped history (no PII beyond name and milestone summaries) — own child only",
    "AUTOMATION_AGENT: only registered agents in AUTHORIZED_QUERY_AGENTS list — scope restricted"
  ]
}
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT — LOCKED)

### 4A. History Ledger Entry (HLE) — Primary Write Output

The canonical immutable record stored for every passport version bump.

```json
HISTORY_LEDGER_ENTRY: {
  "hle_id":                    "UUID v4 — primary key",
  "hle_version":               "semver — version of HLE schema used",
  "user_id":                   "string",
  "tenant_id":                 "string",
  "domain_track":              "string",
  "passport_id":               "UUID v4",
  "passport_version":          "semver — the version this HLE records",
  "passport_version_prior":    "semver | null",
  "recorded_at_utc":           "ISO 8601 UTC — when this agent stored the entry",
  "event_timestamp_utc":       "ISO 8601 UTC — when PAA emitted the version bump",
  "agent_version":             "semver — this agent's version at time of recording",
  "model_version_paa":         "string — PAA's model version that produced this version",
  "audit_reference_paa":       "UUID — PAA's audit log reference",
  "trigger_event_ids":         "array of event_ids that caused this version bump",
  "trigger_event_types":       "array of human-readable event type names",
  "upo_snapshot":              "Full UPO JSON — complete passport state at this version",
  "upo_snapshot_hash":         "SHA-256 of upo_snapshot — tamper detection",
  "delta_object":              "Delta Intelligence Object (see 4B)",
  "change_categories":         "array — classified change types present in this version bump",
  "change_magnitude":          "MICRO | MINOR | SIGNIFICANT | MAJOR | CRITICAL",
  "change_magnitude_basis":    "string — which delta signals drove the magnitude classification",
  "confidence_score_paa":      "float 0.0–1.0 — PAA confidence at time of this version",
  "completeness_score_paa":    "float 0.0–1.0",
  "trust_tier_snapshot":       "string — trust tier at this version",
  "anomaly_flags_snapshot":    "array — anomaly flags present at this version",
  "velocity_signal":           "object — see 4C",
  "hle_signature":             "HMAC-SHA256 of canonical HLE JSON — tamper detection",
  "ai_narrative_summary":      "string | null — human-readable summary of what changed (AI-generated, supplementary only)",
  "is_first_version":          "boolean — true if passport_version_prior is null",
  "is_trust_tier_change":      "boolean",
  "is_belt_change":            "boolean",
  "is_anomaly_flagged":        "boolean"
}
```

### 4B. Delta Intelligence Object (DIO)

Computed between every consecutive passport version. Embedded within HLE.

```json
DELTA_INTELLIGENCE_OBJECT: {
  "delta_id":              "UUID v4",
  "from_version":          "semver",
  "to_version":            "semver",
  "computed_at_utc":       "ISO 8601 UTC",
  "field_deltas": {
    "belt_record": {
      "changed":           "boolean",
      "prior_belt":        "string | null",
      "new_belt":          "string | null",
      "direction":         "PROMOTED | DEMOTED | UNCHANGED | FIRST_AWARD"
    },
    "trust_tier": {
      "changed":           "boolean",
      "prior_tier":        "string | null",
      "new_tier":          "string | null",
      "direction":         "UPGRADED | DOWNGRADED | UNCHANGED | FIRST_SET"
    },
    "confidence_score": {
      "changed":           "boolean",
      "prior_value":       "float | null",
      "new_value":         "float",
      "delta":             "float (signed)",
      "direction":         "INCREASED | DECREASED | UNCHANGED"
    },
    "completeness_score": {
      "changed":           "boolean",
      "prior_value":       "float | null",
      "new_value":         "float",
      "delta":             "float (signed)"
    },
    "skills_completed": {
      "changed":           "boolean",
      "added":             "array of skill_ids newly completed",
      "count_delta":       "integer (signed)"
    },
    "certifications": {
      "changed":           "boolean",
      "issued":            "array of certification objects newly issued",
      "revoked":           "array of certification_ids newly revoked"
    },
    "dojo_record": {
      "changed":           "boolean",
      "total_matches_delta": "integer",
      "win_rate_delta":    "float (signed)",
      "pressure_scenarios_passed_delta": "integer"
    },
    "project_record": {
      "changed":           "boolean",
      "milestones_passed_delta": "integer",
      "projects_completed_delta": "integer"
    },
    "gamification_record": {
      "changed":           "boolean",
      "xp_delta":          "integer (signed)",
      "badges_earned":     "array of new badge_ids",
      "streak_delta":      "integer (signed)"
    },
    "employment_record": {
      "changed":           "boolean",
      "applications_delta": "integer",
      "offers_delta":       "integer"
    },
    "anomaly_flags": {
      "changed":           "boolean",
      "added":             "array of newly appeared anomaly flag strings",
      "cleared":           "array of anomaly flag strings no longer present"
    },
    "identity_block": {
      "changed":           "boolean",
      "verification_changes": "array of verification fields that changed"
    }
  },
  "total_fields_changed":  "integer — count of top-level delta blocks with changed=true",
  "net_positive_change":   "boolean — overall trajectory was positive",
  "delta_summary_vector":  "array of float — numerical encoding of key delta signals (for ML use)"
}
```

### 4C. Velocity Signal Object

Computed per HLE to detect abnormal progression patterns.

```json
VELOCITY_SIGNAL: {
  "user_id":                    "string",
  "passport_version":           "semver",
  "computed_at_utc":            "ISO 8601 UTC",
  "versions_last_7d":           "integer — how many version bumps in 7 days",
  "versions_last_30d":          "integer",
  "belt_promotions_last_30d":   "integer",
  "certifications_last_30d":    "integer",
  "xp_gain_last_7d":            "integer",
  "xp_gain_last_30d":           "integer",
  "velocity_score":             "float 0.0–1.0 — normalized velocity index",
  "velocity_band":              "NORMAL | ELEVATED | HIGH | SUSPICIOUS | CRITICAL",
  "velocity_anomaly_detected":  "boolean",
  "velocity_anomaly_reason":    "string | null — human-readable basis if anomaly",
  "velocity_flag_details":      "array of specific signals that contributed to flag"
}
```

### 4D. Timeline Query Response

```json
TIMELINE_QUERY_RESPONSE: {
  "request_id":          "UUID v4 — echoed from query input",
  "response_id":         "UUID v4",
  "generated_at_utc":    "ISO 8601 UTC",
  "user_id":             "string",
  "tenant_id":           "string",
  "total_versions":      "integer — total versions in user's history",
  "page_cursor_next":    "string | null — null if last page",
  "entries":             "array of HLE summaries (full HLE if include_upo_snapshot=true)",
  "metadata": {
    "earliest_version":  "semver",
    "latest_version":    "semver",
    "first_entry_at":    "ISO 8601 UTC",
    "last_entry_at":     "ISO 8601 UTC",
    "total_belt_changes": "integer",
    "total_trust_tier_changes": "integer",
    "total_anomaly_flags_ever": "integer"
  }
}
```

### 4E. Point-in-Time Passport Snapshot Response

```json
POINT_IN_TIME_RESPONSE: {
  "request_id":          "UUID v4",
  "requested_timestamp": "ISO 8601 UTC",
  "resolved_version":    "semver — latest version whose recorded_at_utc <= requested_timestamp",
  "upo_snapshot":        "Full UPO JSON at resolved_version",
  "upo_snapshot_hash":   "SHA-256 — for integrity verification",
  "hle_id":              "UUID — the HLE from which this was served",
  "freshness_note":      "string — e.g. 'Passport state as of 2025-09-01T14:32:00Z (version v3.12.0)'"
}
```

### Output Rules (NON-NEGOTIABLE)

```yaml
- Every HLE MUST include hle_signature (HMAC-SHA256) before storage
- Every HLE MUST include upo_snapshot_hash for tamper detection
- HLEs are NEVER modified after write — append-only ledger
- HLEs are NEVER deleted — including by compliance deletion requests
  → On GDPR/DPDP deletion: PII fields within HLE are anonymized in-place via PAA-approved redaction job
  → The HLE skeleton (version record, delta, timing, categories) is RETAINED permanently
  → Redaction is logged as a separate HLE_REDACTION_EVENT — not a mutation of the original HLE
- Velocity anomaly detected → emit VELOCITY_ANOMALY_DETECTED event to ANTI_COLLUSION_AGENT within 500ms
- All query responses are read-only — this agent NEVER performs mutations in response to queries
- All query responses include audit logging of who queried what and when
- Point-in-time reconstruction must be deterministic — same timestamp always resolves to same version
```

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Layer (PRIMARY — ~80% of analytical weight)

```yaml
MODEL_TYPE: Multi-signal Anomaly Detection + Classification Ensemble

MODELS_USED:

  1. VELOCITY_ANOMALY_CLASSIFIER
     Type:                 Isolation Forest + Gradient Boosted Classifier (ensemble)
     Target:               velocity_band classification (NORMAL | ELEVATED | HIGH | SUSPICIOUS | CRITICAL)
     Features:             versions_last_7d, versions_last_30d, belt_promotions_last_30d,
                           certifications_last_30d, xp_gain_last_7d, xp_gain_last_30d,
                           peer_group_avg_velocity (cohort comparison),
                           domain_track_avg_velocity, days_active_on_platform,
                           match_count_last_30d, mentor_evaluation_count_last_30d
     Training_Frequency:   Weekly
     Drift_Detection:      PSI > 0.2 triggers DRIFT_ALERT

  2. CHANGE_MAGNITUDE_CLASSIFIER
     Type:                 Multi-class Random Forest
     Target:               change_magnitude (MICRO | MINOR | SIGNIFICANT | MAJOR | CRITICAL)
     Features:             total_fields_changed, belt_changed, trust_tier_changed,
                           certification_issued, anomaly_flags_added, confidence_delta,
                           xp_delta, skills_added_count, projects_completed_delta
     Training_Frequency:   Monthly

  3. PROGRESSION_TREND_REGRESSOR
     Type:                 ARIMA + LSTM (time-series ensemble)
     Target:               projected trajectory of key passport metrics (belt tier, trust tier, skill depth)
     Purpose:              Advisory only — fed to FEATURE_STORE_AGENT for downstream use
     Training_Frequency:   Monthly
     Note:                 Predictions are ADVISORY — never written into HLE as facts

  4. DELTA_PATTERN_CLUSTERER
     Type:                 K-Means Clustering
     Target:               Group users by historical delta pattern signature (for peer comparison)
     Features:             delta_summary_vector sequences over time
     Training_Frequency:   Monthly
     Output:               cluster_id stored in HLE as enrichment field

DRIFT_DETECTION_POLICY:
  - All models monitored weekly for PSI (Population Stability Index)
  - PSI > 0.2 → emit DRIFT_ALERT to OBSERVABILITY_AGENT
  - Accuracy drop > 5% in any model → emit MODEL_RETRAIN_EVENT
  - Retrain jobs are external — this agent consumes updated model artifacts only

VERSION_CONTROL:
  - All model artifacts stored with version tag (e.g., PHA-VELOCITY-v1.3.0)
  - Immutable model artifacts — old versions retained for audit replay
  - model_version embedded in every HLE entry
  - Model rollback requires compliance approval log entry
```

### AI Layer (SUPPLEMENTARY — ~20% advisory weight)

```yaml
AI_USAGE_SCOPE:
  1. NARRATIVE_SUMMARY_GENERATION:
     Input:  Delta Intelligence Object (DIO) fields
     Output: ai_narrative_summary — max 3 sentences, human-readable change explanation
     Usage:  User-facing UI (Flutter dashboard, passport timeline card)
     Example: "You earned your Silver Belt this week and completed 3 new Technology skills,
               raising your trust tier from Basic to Intermediate."

  2. PROGRESSION_INSIGHT_ADVISORY:
     Input:  Last 5 HLE entries for user
     Output: Advisory text for user notification ("You're progressing faster than 78% of
               Technology track peers this month")
     Usage:  NOTIFICATION_AGENT consumption only — never written to HLE core fields

  3. ANOMALY_EXPLANATION_GENERATOR:
     Input:  velocity_signal anomaly fields
     Output: Plain-language explanation of why the anomaly was flagged
     Usage:  COMPLIANCE_AUDIT_AGENT and admin review panels only

AI_BOUNDARIES (NON-NEGOTIABLE):
  - AI NEVER writes to any HLE core field
  - AI NEVER modifies delta_object or velocity_signal values
  - AI NEVER classifies velocity_band (ML handles this)
  - AI NEVER makes compliance or legal determinations
  - AI output stored in separate ai_enrichment_store — never embedded in immutable HLE
  - AI output failures do NOT block HLE write — HLE proceeds without AI narrative
  - All AI outputs versioned and logged (prompt_version + response_hash stored in ai_enrichment_store)

PROMPT_GOVERNANCE:
  - All prompts versioned (e.g., PHA-NARRATIVE-PROMPT-v1.0, PHA-ANOMALY-PROMPT-v1.0)
  - Prompt registry maintained in Config Service — no runtime prompt construction
  - No dynamic prompts from user-supplied input under any circumstance
  - Prompts are deterministic slot-fill templates from DIO + HLE fields
  - AI output must pass length validation (max 500 chars per narrative) before use
  - Prompt version recorded in ai_enrichment_store for every AI call
```

---

## 6️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_WRITE_RPS:       300 events/sec (sustained) — mirrors PAA emission rate
EXPECTED_WRITE_RPS_PEAK:  3,000 events/sec (burst during mass belt promotion or match completion waves)
EXPECTED_READ_RPS:         2,000 queries/sec (recruiter + compliance + student reads)
LATENCY_TARGET_WRITE:      HLE written and confirmed < 1 second P95 from event receipt
LATENCY_TARGET_READ:       Timeline query response < 500ms P95 for standard paginated requests
LATENCY_TARGET_PIT:        Point-in-time reconstruction < 1 second P95
MAX_CONCURRENCY:           2,000 parallel write + read workers
QUEUE_STRATEGY:            Kafka topic partitioned by user_id hash — ensures ordered per-user processing
SCALING_MODEL:             Horizontal stateless workers (separate write pool and read pool)
EXECUTION_MODEL:           Stateless — all state in HISTORY_LEDGER_STORE + SNAPSHOT_STORE
IDEMPOTENCY:               Each event_id processed exactly once (Redis deduplication registry, TTL 30 days)
ASYNC_AI_ENRICHMENT:       AI narrative generation is async and non-blocking — HLE write never waits for it
BACKPRESSURE:              Consumer lag monitored; auto-scale write workers when lag > 5,000 messages
READ_CACHE:                Hot timeline reads cached in Redis (TTL 60 seconds, invalidated on new HLE write)
SNAPSHOT_STORE:            Separate cold storage (S3-compatible object store) for full UPO snapshots
                           Hot index (PostgreSQL) stores metadata + delta only — snapshot fetched from cold store on demand
```

---

## 7️⃣ SECURITY ENFORCEMENT (ZERO-TRUST — ALL MANDATORY)

```yaml
WRITE_SECURITY:

  SOURCE_VALIDATION:
    - ONLY events from PASSPORT_AGGREGATION_AGENT accepted for writes
    - Source identity validated via JWT claim + registered service identity
    - Any write attempt from any other source = SECURITY_VIOLATION event + STOP_EXECUTION

  TENANT_ISOLATION:
    - Every HLE write scoped by tenant_id
    - Row-level security enforced at PostgreSQL level
    - No wildcard tenant queries or writes permitted
    - tenant_id resolved from JWT — never from user-supplied payload field

  DOMAIN_ISOLATION:
    - HLE records isolated by domain_track
    - Cross-domain history queries = SECURITY_FAILURE + COMPLIANCE_ALERT

  IMMUTABILITY_ENFORCEMENT:
    - PostgreSQL row-level triggers BLOCK any UPDATE or DELETE on history_ledger table
    - Application-level: no UPDATE or DELETE paths exist in codebase for HLE records
    - DB user account used by this agent has INSERT-ONLY grants on history_ledger (no UPDATE, no DELETE)
    - Separate read-only DB user for query operations

READ_SECURITY:

  ROLE-BASED ACCESS CONTROL:
    - STUDENT: own history only — enforced at query level by user_id == target_user_id check
    - RECRUITER: candidate history requires CONSENT_FLAG = ACTIVE in candidate's consent store
    - INSTITUTE_ADMIN: enrolled students within own tenant only — tenant_id scoped
    - PLATFORM_ADMIN: any user, read-only, full audit trail mandatory
    - COMPLIANCE_OFFICER: any user, read-only, compliance export enabled, audit trail mandatory
    - PARENT: own child only, PII stripped to name + milestone summaries only
    - AUTOMATION_AGENT: must be in AUTHORIZED_QUERY_AGENTS registry, scope pre-approved

  PII HANDLING:
    - Full UPO snapshots contain PII fields (name, contact, verification data)
    - PII fields served only to AUTHORIZED_PII_ROLES (STUDENT/self, COMPLIANCE_OFFICER, PLATFORM_ADMIN)
    - Recruiters receive REDACTED_UPO_VIEW (PII stripped, credentials visible)
    - Parents receive SUMMARY_VIEW (name + milestone events only)
    - All PII access logged with actor_id, timestamp, and data_scope_served

ENCRYPTION:
    - UPO snapshots encrypted at rest (AES-256) in cold storage (S3-compatible)
    - PostgreSQL HLE metadata table: PII fields encrypted at column level
    - All inter-service communication over mTLS
    - hle_signature and upo_snapshot_hash verified on every read for tamper detection
    - Secrets via Secret Manager only — no plaintext env vars in production

AUDIT_LOGGING_OF_READS:
    - Every timeline query logged: request_id, requester_id, requester_role, target_user_id, timestamp, data_scope_served
    - Every compliance export logged with export_package_id and recipient identity
    - Every point-in-time reconstruction logged
    - Query audit logs are append-only — separate from HLE write logs
```

---

## 8️⃣ AUDIT & TRACEABILITY (MANDATORY — EVERY OPERATION)

### Write Audit Log Entry (per HLE stored)

```json
WRITE_AUDIT_LOG_ENTRY: {
  "audit_id":                "UUID v4",
  "operation_type":          "HLE_WRITE",
  "hle_id":                  "UUID v4",
  "user_id":                 "string",
  "tenant_id":               "string",
  "passport_version":        "semver",
  "timestamp_utc":           "ISO 8601 UTC",
  "source_event_id":         "UUID — PASSPORT_VERSION_BUMPED event_id",
  "source_agent":            "PASSPORT_AGGREGATION_AGENT",
  "input_hash_verified":     "boolean",
  "hle_signature":           "HMAC-SHA256",
  "upo_snapshot_hash":       "SHA-256",
  "agent_version":           "semver — this agent's version",
  "model_version_used":      "string — velocity/magnitude models used",
  "ai_enrichment_attempted": "boolean",
  "ai_enrichment_succeeded": "boolean",
  "velocity_anomaly_emitted":"boolean",
  "processing_latency_ms":   "integer",
  "escalation_triggered":    "boolean"
}
```

### Read Audit Log Entry (per query executed)

```json
READ_AUDIT_LOG_ENTRY: {
  "audit_id":          "UUID v4",
  "operation_type":    "TIMELINE_QUERY | POINT_IN_TIME_FETCH | DELTA_FETCH | COMPLIANCE_EXPORT",
  "request_id":        "UUID v4",
  "requester_id":      "string",
  "requester_role":    "string",
  "target_user_id":    "string",
  "tenant_id":         "string",
  "timestamp_utc":     "ISO 8601 UTC",
  "data_scope_served": "FULL_PII | REDACTED | SUMMARY_ONLY",
  "versions_served":   "array of semver strings returned",
  "consent_verified":  "boolean — for recruiter access",
  "latency_ms":        "integer"
}
```

Both logs stored in `passport_history_audit_log` — append-only partition, no UPDATE or DELETE permitted under any circumstance.

---

## 9️⃣ FAILURE POLICY (NON-NEGOTIABLE — ALL CASES EXPLICITLY DEFINED)

```yaml
FAILURE_CASES:

  INVALID_INPUT_EVENT:
    Action:          REJECT_EVENT
    Log:             LOG_VALIDATION_FAILURE → dead_letter_queue + write_audit_log
    Escalate:        COMPLIANCE_AUDIT_AGENT
    Retry:           NO — invalid input is not retried
    HLE_Write:       BLOCKED
    Silent_Failure:  FORBIDDEN

  VERSION_GAP_DETECTED:
    Condition:       passport_version_prior in incoming event does not match last stored version for user
    Action:          STOP_EXECUTION for this user's write job
    Log:             LOG_VERSION_GAP_ALERT (critical severity)
    Escalate:        COMPLIANCE_AUDIT_AGENT + PASSPORT_AGGREGATION_AGENT (for investigation)
    HLE_Write:       BLOCKED — do not store potentially discontinuous history
    Retry:           HOLD — wait for PASSPORT_AGGREGATION_AGENT to emit gap-reconciliation event
    Silent_Failure:  FORBIDDEN

  VERSION_REGRESSION_DETECTED:
    Condition:       passport_version_new <= passport_version_prior (version went backward)
    Action:          STOP_EXECUTION + EMIT_SECURITY_ALERT
    Log:             LOG_SECURITY_VIOLATION (critical)
    Escalate:        COMPLIANCE_AUDIT_AGENT + PLATFORM_ADMIN
    HLE_Write:       BLOCKED
    Retry:           NO
    Silent_Failure:  FORBIDDEN

  UNAUTHORIZED_WRITE_ATTEMPT:
    Condition:       source_agent is NOT PASSPORT_AGGREGATION_AGENT
    Action:          REJECT_EVENT + STOP_EXECUTION + EMIT_SECURITY_VIOLATION
    Log:             LOG_SECURITY_VIOLATION (critical)
    Escalate:        COMPLIANCE_AUDIT_AGENT + PLATFORM_ADMIN
    HLE_Write:       BLOCKED
    Silent_Failure:  FORBIDDEN

  SNAPSHOT_HASH_MISMATCH:
    Condition:       Recomputed SHA-256 of full_upo_snapshot != input_hash in event
    Action:          REJECT_EVENT + STOP_EXECUTION
    Log:             LOG_DATA_INTEGRITY_FAILURE (critical)
    Escalate:        COMPLIANCE_AUDIT_AGENT + ON_CALL_ENGINEER
    HLE_Write:       BLOCKED
    Retry:           NO — integrity failure requires investigation
    Silent_Failure:  FORBIDDEN

  COLD_STORAGE_UNAVAILABLE:
    Condition:       UPO snapshot write to S3-compatible store fails
    Action:          QUEUE snapshot write with exponential retry — DO NOT block HLE metadata write
    Log:             LOG_STORAGE_DEGRADED
    Escalate:        OBSERVABILITY_AGENT (operational alert)
    HLE_Write:       PROCEED — metadata + delta written, snapshot queued for retry
    Retry:           YES — exponential backoff, max 5 attempts: 10s/30s/60s/120s/300s
    Silent_Failure:  FORBIDDEN

  ML_MODEL_UNAVAILABLE:
    Condition:       Velocity classifier or magnitude classifier unavailable
    Action:          WRITE HLE with velocity_band = UNKNOWN, change_magnitude = UNKNOWN
    Log:             LOG_MODEL_UNAVAILABLE (warn severity)
    Escalate:        OBSERVABILITY_AGENT
    HLE_Write:       PROCEED with degraded classification — never block on ML
    AI_Enrichment:   SKIP if model unavailable
    Retry:           YES — ML enrichment retried async, max 3 attempts
    Silent_Failure:  FORBIDDEN

  AI_ENRICHMENT_TIMEOUT:
    Condition:       AI narrative generation exceeds 3 second timeout
    Action:          SKIP AI narrative — HLE written without ai_narrative_summary
    Log:             LOG_AI_TIMEOUT (warn)
    Escalate:        OBSERVABILITY_AGENT (non-critical)
    HLE_Write:       PROCEED without narrative
    Silent_Failure:  FORBIDDEN

  VELOCITY_ANOMALY_DETECTED:
    Action:          WRITE HLE normally + EMIT VELOCITY_ANOMALY_DETECTED event to ANTI_COLLUSION_AGENT
    Log:             LOG_VELOCITY_ANOMALY
    Escalate:        ANTI_COLLUSION_AGENT + OBSERVABILITY_AGENT
    HLE_Write:       PROCEED — anomaly detection does not block history recording
    Note:            History is recorded for ALL events — anomaly or not. The anomaly event is a parallel action.

  DUPLICATE_EVENT:
    Condition:       event_id already in Redis deduplication registry
    Action:          SKIP (idempotency)
    Log:             LOG_DUPLICATE_SKIP
    HLE_Write:       BLOCKED — no duplicate HLE created
    Silent_Failure:  FORBIDDEN — must log the skip

  CROSS_TENANT_EVENT:
    Condition:       tenant_id in event does not match user's registered tenant
    Action:          REJECT_EVENT + STOP_EXECUTION + EMIT_SECURITY_VIOLATION
    Log:             LOG_SECURITY_VIOLATION (critical)
    Escalate:        COMPLIANCE_AUDIT_AGENT + PLATFORM_ADMIN
    Silent_Failure:  FORBIDDEN

GLOBAL_RULES:
  - No silent failures under any condition whatsoever
  - Every failure path produces a write_audit_log entry minimum
  - ESCALATE_TO: OBSERVABILITY_AGENT (operational) | COMPLIANCE_AUDIT_AGENT (security/integrity)
  - RETRY_POLICY: Exponential backoff where applicable — never infinite retry — max attempts defined per case
  - Dead letter queue for all rejected events — inspectable by COMPLIANCE_AUDIT_AGENT
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP

### Upstream (Only Source — Write Path)

```yaml
UPSTREAM_WRITE_SOURCE:
  PASSPORT_AGGREGATION_AGENT (PAA-001):
    Event:        PASSPORT_VERSION_BUMPED
    Kafka_Topic:  passport.versions.bumped
    Partition:    by user_id hash
    Exclusivity:  SOLE AUTHORIZED WRITE SOURCE — no other agent may trigger HLE writes
```

### Downstream (Read / Event Consumers)

```yaml
DOWNSTREAM_EVENT_CONSUMERS:

  ANTI_COLLUSION_AGENT:
    Trigger:      VELOCITY_ANOMALY_DETECTED
    Priority:     HIGH — delivered within 500ms of anomaly detection

  COMPLIANCE_AUDIT_AGENT:
    Trigger:      HISTORY_INTEGRITY_FAILURE | SECURITY_VIOLATION | COMPLIANCE_EXPORT_READY
    Priority:     CRITICAL

  OBSERVABILITY_AGENT:
    Trigger:      All performance metrics, error events, drift alerts
    Priority:     STANDARD

  NOTIFICATION_AGENT:
    Trigger:      PASSPORT_HISTORY_MILESTONE_REACHED (e.g., "1 year on platform", "50 matches recorded")
    Priority:     STANDARD

  FEATURE_STORE_AGENT:
    Trigger:      PASSPORT_HISTORY_FEATURE_VECTOR (temporal features emitted per HLE write)
    Priority:     STANDARD — async, non-blocking

  ML_TRAINING_PIPELINE_AGENT:
    Access:       Read-only batch export of ordered HLE sequences (scheduled, not event-driven)
    Auth:         Requires AUTHORIZED_QUERY_AGENTS registration + COMPLIANCE_OFFICER approval

  LEGAL_EXPORT_AGENT:
    Access:       On-demand compliance export packages
    Auth:         Requires COMPLIANCE_OFFICER role + audit logged
    Format:       Structured JSON + PDF summary
```

### Events Emitted by This Agent

```yaml
EMITTED_EVENTS:
  - HISTORY_LEDGER_ENTRY_WRITTEN       → OBSERVABILITY_AGENT (every write)
  - VELOCITY_ANOMALY_DETECTED          → ANTI_COLLUSION_AGENT (conditional)
  - HISTORY_INTEGRITY_FAILURE          → COMPLIANCE_AUDIT_AGENT (conditional)
  - SECURITY_VIOLATION                 → COMPLIANCE_AUDIT_AGENT + PLATFORM_ADMIN (conditional)
  - VERSION_GAP_DETECTED               → COMPLIANCE_AUDIT_AGENT + PAA (conditional)
  - PASSPORT_HISTORY_FEATURE_VECTOR    → FEATURE_STORE_AGENT (every write)
  - PASSPORT_HISTORY_MILESTONE_REACHED → NOTIFICATION_AGENT (conditional)
  - COMPLIANCE_EXPORT_READY            → LEGAL_EXPORT_AGENT (on-demand)
  - DRIFT_ALERT                        → OBSERVABILITY_AGENT (conditional, ML drift)
  - MODEL_RETRAIN_EVENT                → ML_TRAINING_PIPELINE_AGENT (conditional, drift threshold exceeded)
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent emits temporal feature vectors per HLE write. All emissions async and non-blocking.

```json
PASSPORT_HISTORY_FEATURE_VECTOR: {
  "user_id":                          "string",
  "tenant_id":                        "string",
  "feature_name":                     "string (see feature list below)",
  "feature_value":                    "float | integer | string",
  "feature_timestamp_utc":            "ISO 8601 UTC — timestamp of the HLE that produced this",
  "passport_version":                 "semver",
  "source_agent":                     "PASSPORT_HISTORY_AGENT"
}
```

```yaml
TEMPORAL_FEATURE_EMISSIONS (per HLE write):
  - pha_versions_last_7d                  (int)
  - pha_versions_last_30d                 (int)
  - pha_belt_promotions_last_30d          (int)
  - pha_certifications_last_30d           (int)
  - pha_xp_gain_last_7d                   (int)
  - pha_xp_gain_last_30d                  (int)
  - pha_velocity_score                    (float)
  - pha_velocity_band_numeric             (int: NORMAL=1, ELEVATED=2, HIGH=3, SUSPICIOUS=4, CRITICAL=5)
  - pha_change_magnitude_numeric          (int: MICRO=1, MINOR=2, SIGNIFICANT=3, MAJOR=4, CRITICAL=5)
  - pha_total_versions_lifetime           (int)
  - pha_days_since_first_version          (int)
  - pha_delta_cluster_id                  (int — from DELTA_PATTERN_CLUSTERER)
  - pha_net_positive_change_rate_30d      (float — ratio of positive to total changes)
  - pha_anomaly_frequency_30d             (float)
  - pha_confidence_trend_7d              (float — signed, direction of confidence movement)
```

---

## 1️⃣2️⃣ GDPR / DPDP COMPLIANCE HANDLING (CRITICAL)

History records are IMMUTABLE — they are never deleted. However, PII within records must be anonymizable on legal request. The following protocol is locked and non-negotiable.

```yaml
DATA_DELETION_PROTOCOL:

  TRIGGER:
    - GDPR erasure request (Article 17) verified by LEGAL_EXPORT_AGENT
    - India DPDP erasure request verified by LEGAL_EXPORT_AGENT
    - Platform admin deletion workflow with compliance approval

  PROCESS:
    Step 1: LEGAL_EXPORT_AGENT emits PII_REDACTION_AUTHORIZED event with user_id + legal_request_id
    Step 2: This agent receives event and initiates redaction job
    Step 3: Redaction job scans all HLE records for target user_id
    Step 4: For each HLE record, PII fields within upo_snapshot are overwritten with [REDACTED] marker
            PII fields: full_name, profile_image_url, contact details, government ID data
    Step 5: delta_object, velocity_signal, change_categories, timestamps, versions — PRESERVED
    Step 6: A new HLE_REDACTION_EVENT record is appended to the ledger (NOT a mutation of original HLE)
            This records: hle_id redacted, legal_request_id, redacted_fields_list, redacted_at_utc
    Step 7: Cold storage (S3) UPO snapshot for this user replaced with REDACTED version
    Step 8: Confirmation emitted to LEGAL_EXPORT_AGENT and COMPLIANCE_AUDIT_AGENT

  NON-NEGOTIABLE:
    - The HLE skeleton (version record, delta structure, timing, categories) is NEVER deleted
    - Only PII field values are overwritten with [REDACTED] marker
    - The HLE_REDACTION_EVENT itself is immutable and append-only
    - This process is logged in its entirety in passport_history_audit_log
    - Redaction does NOT break the version chain integrity
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

History milestones trigger growth engine events:

```yaml
MILESTONE_TRIGGERS:
  - 1st HLE ever written (platform onboarding)       → NOTIFICATION_AGENT ("Your passport journey begins")
  - 10th, 25th, 50th, 100th version recorded         → NOTIFICATION_AGENT + XP_UPDATE_EVENT advisory
  - First belt change recorded in history             → NOTIFICATION_AGENT
  - First trust tier upgrade recorded                 → NOTIFICATION_AGENT + SHARE_TRIGGER_EVENT advisory
  - 1 year / 2 year / 5 year history anniversary     → NOTIFICATION_AGENT + SHARE_TRIGGER_EVENT advisory
  - First anomaly-free 30-day stretch (if prior anomalies existed) → NOTIFICATION_AGENT

EVENT_FORMAT:
  PASSPORT_HISTORY_MILESTONE_REACHED: {
    user_id, tenant_id, milestone_type, milestone_description,
    passport_version_at_milestone, hle_id, timestamp_utc, source_agent
  }
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```yaml
METRICS (published to OBSERVABILITY_AGENT — Prometheus):

  OPERATIONAL:
    - pha_hle_write_success_rate            (target: > 99.9%)
    - pha_hle_write_error_rate              (alert: > 0.1%)
    - pha_write_latency_p95_ms              (target: < 1000ms)
    - pha_read_latency_p95_ms               (target: < 500ms)
    - pha_pit_reconstruction_latency_p95_ms (target: < 1000ms)
    - pha_kafka_consumer_lag                (alert: > 5,000 messages)
    - pha_snapshot_storage_error_rate       (alert: > 0.1%)
    - pha_dead_letter_queue_size            (alert: > 100 messages)

  QUALITY:
    - pha_velocity_anomaly_rate             (alert: > 2% of writes)
    - pha_version_gap_detection_count       (alert: any > 0)
    - pha_version_regression_detection_count (alert: any > 0)
    - pha_hash_mismatch_count               (alert: any > 0)
    - pha_ai_enrichment_success_rate        (target: > 97%)

  SECURITY:
    - pha_unauthorized_write_attempt_count  (alert: any > 0)
    - pha_cross_tenant_rejection_count      (alert: any > 0)
    - pha_security_violation_count          (alert: any > 0)

  ML_DRIFT:
    - pha_model_psi_velocity_classifier     (alert: PSI > 0.2)
    - pha_model_psi_magnitude_classifier    (alert: PSI > 0.2)
    - pha_model_accuracy_delta              (alert: any model drop > 5%)

  STORAGE:
    - pha_hle_total_records_count           (growth monitoring)
    - pha_snapshot_store_size_gb            (capacity planning)
    - pha_cold_storage_retrieval_latency_p95_ms (target: < 2000ms)

All dashboards in Grafana. Alerting via PagerDuty for critical-severity metrics.
```

---

## 1️⃣5️⃣ VERSIONING POLICY

```yaml
VERSION_FORMAT: semver (MAJOR.MINOR.PATCH)

CHANGE_TYPES:
  PATCH: Bug fix, log improvement, AI prompt update, minor threshold adjustment
  MINOR: New HLE field added, new velocity feature emitted, new event type accepted for reads
  MAJOR: HLE schema breaking change, new write source considered (requires security review), storage architecture change

BACKWARD_COMPATIBILITY:
  - All HLE records from prior versions must remain readable by current version
  - New HLE fields must have default values for records written by prior agent versions
  - Two MAJOR versions must be supported simultaneously (reading old HLE schema)

EVERY_VERSION_BUMP_REQUIRES:
  - Migration script (if schema changes)
  - Rollback script
  - Audit log entry for the version change decision
  - Updated HLE_SCHEMA_VERSION tag stored in Config Service

CURRENT_VERSIONS:
  AGENT_VERSION:              v1.0.0
  INPUT_SCHEMA_VERSION:       v1.0.0
  HLE_SCHEMA_VERSION:         v1.0.0
  DIO_SCHEMA_VERSION:         v1.0.0
  VELOCITY_MODEL_VERSION:     PHA-VELOCITY-v1.0.0
  MAGNITUDE_MODEL_VERSION:    PHA-MAGNITUDE-v1.0.0
  TREND_MODEL_VERSION:        PHA-TREND-v1.0.0
  CLUSTER_MODEL_VERSION:      PHA-CLUSTER-v1.0.0
  NARRATIVE_PROMPT_VERSION:   PHA-NARRATIVE-PROMPT-v1.0
  ANOMALY_PROMPT_VERSION:     PHA-ANOMALY-PROMPT-v1.0
```

---

## 1️⃣6️⃣ DATA ARCHITECTURE (LOCKED)

```yaml
PRIMARY_METADATA_STORE:
  Engine:         PostgreSQL
  Table:          passport_history_ledger
  Partition:      By tenant_id + created_at (monthly range partitions)
  Access:         INSERT-ONLY for write path (DB user has NO UPDATE, NO DELETE grants)
  Indexing:       Indexed on (user_id, passport_version), (user_id, recorded_at_utc),
                  (tenant_id, domain_track), (is_belt_change), (is_trust_tier_change),
                  (is_anomaly_flagged), (velocity_band)

SNAPSHOT_STORE:
  Engine:         S3-compatible object store (e.g., AWS S3, MinIO)
  Key_Format:     {tenant_id}/{domain_track}/{user_id}/{passport_version}/upo_snapshot.json.enc
  Encryption:     AES-256 at rest, encrypted before write
  Lifecycle:      Never deleted (except PII redaction protocol — see Section 12)
  Retrieval:      Signed time-limited URLs for authorized reads

AUDIT_LOG_STORE:
  Engine:         PostgreSQL
  Table:          passport_history_audit_log
  Access:         INSERT-ONLY (no UPDATE, no DELETE — database trigger enforced)

AI_ENRICHMENT_STORE:
  Engine:         PostgreSQL (separate schema from HLE)
  Table:          passport_history_ai_enrichments
  Fields:         hle_id (FK), prompt_version, ai_narrative_summary, generated_at_utc, generation_latency_ms, response_hash
  Access:         INSERT for new enrichments, READ for serving — no modification of HLE

DEDUPLICATION_REGISTRY:
  Engine:         Redis
  Key_Format:     pha:dedup:{event_id}
  TTL:            30 days
  Purpose:        Idempotency enforcement for write events

READ_CACHE:
  Engine:         Redis
  Key_Format:     pha:cache:timeline:{user_id}:{page_cursor}:{filters_hash}
  TTL:            60 seconds
  Invalidation:   On new HLE write for user_id, all related cache keys expire immediately

KEY_TABLES:
  passport_history_ledger:
    - hle_id (UUID PK)
    - hle_schema_version (semver)
    - user_id (string, indexed)
    - tenant_id (string, indexed)
    - domain_track (enum)
    - passport_id (UUID)
    - passport_version (semver, indexed)
    - passport_version_prior (semver | null)
    - recorded_at_utc (timestamptz, indexed)
    - event_timestamp_utc (timestamptz)
    - agent_version (semver)
    - model_version_paa (string)
    - model_versions_pha (jsonb — all models used)
    - audit_reference_paa (UUID)
    - trigger_event_ids (jsonb array)
    - trigger_event_types (jsonb array)
    - upo_snapshot_hash (string — SHA-256)
    - snapshot_store_key (string — S3 object key)
    - delta_object (jsonb)
    - change_categories (jsonb array)
    - change_magnitude (enum)
    - confidence_score_paa (float)
    - completeness_score_paa (float)
    - trust_tier_snapshot (string)
    - anomaly_flags_snapshot (jsonb array)
    - velocity_signal (jsonb)
    - hle_signature (string — HMAC-SHA256)
    - is_first_version (boolean)
    - is_trust_tier_change (boolean, indexed)
    - is_belt_change (boolean, indexed)
    - is_anomaly_flagged (boolean, indexed)
    → NO UPDATES. NO DELETES. Database trigger enforced. DB user INSERT-ONLY.

  passport_history_audit_log:
    - audit_id (UUID PK)
    - operation_type (enum)
    - hle_id (UUID | null)
    - request_id (UUID | null)
    - user_id (string)
    - tenant_id (string)
    - requester_id (string | null)
    - requester_role (string | null)
    - target_user_id (string | null)
    - timestamp_utc (timestamptz)
    - data_scope_served (enum | null)
    - processing_latency_ms (integer)
    - escalation_triggered (boolean)
    - additional_context (jsonb)
    → NO UPDATES. NO DELETES. Database trigger enforced.
```

---

## 1️⃣7️⃣ UI / RENDERING CONTRACT

This agent does not render UI. It provides data to rendering agents. The following contracts govern how this agent's data surfaces in the platform UI.

```yaml
FLUTTER_APP_SURFACES:

  STUDENT_PASSPORT_TIMELINE_VIEW:
    Source:         TIMELINE_QUERY endpoint (own user only)
    Display:        Chronological scrollable timeline of all passport changes
    Cards:          Per-HLE summary card showing: version, date, change_categories, ai_narrative_summary
    Filtering:      By change_category, by date range
    Detail:         Tap card → full delta view (fields changed, before/after values)
    Pagination:     Cursor-based, lazy-loaded (20 entries per page)
    Empty_State:    "Your credential journey starts here" (first-time empty)
    Fixed_Block:    Part of FIXED 40% dashboard — passport history timeline widget is non-removable

  RECRUITER_CANDIDATE_HISTORY_VIEW:
    Source:         TIMELINE_QUERY endpoint (redacted PII view, consent-gated)
    Display:        Candidate credential timeline (belt changes, certifications, trust tier changes only)
    PII:            Stripped — no personal contact data shown
    Consent_Gate:   Visible only when candidate CONSENT_FLAG = ACTIVE

  ADMIN_AUDIT_HISTORY_VIEW:
    Source:         COMPLIANCE_EXPORT endpoint (PLATFORM_ADMIN / COMPLIANCE_OFFICER only)
    Display:        Full HLE view with all delta details and audit references
    Export:         JSON + PDF export enabled

  PARENT_HISTORY_VIEW:
    Source:         TIMELINE_QUERY endpoint (SUMMARY_VIEW scope)
    Display:        Name + milestone events only (belt promotions, certifications, trust tier upgrades)
    PII:            Stripped to minimum

REACT_SEO_LAYER:
  PUBLIC_PROFILE_TIMELINE:
    Source:         PUBLIC_PROFILE_AGENT (which reads from this agent's query endpoint)
    Display:        Key credential milestones on public profile (belt earned, certification issued dates)
    Scope:          Non-sensitive milestones only — no anomaly flags, no delta details
    Rendering:      Next.js SSR — schema.org structured data for credential timeline
    Indexable:      YES (SEO surface — React layer)
    Mutations:      DISABLED — read-only clone

DESIGN_RULES:
  - Timeline cards use CLEAN | SOLID | ENTERPRISE design language
  - Light/Dark theme supported
  - Skeleton shimmer loading states on timeline fetch
  - Empty states with contextual guidance (not generic "No data")
  - Pagination must not show loading spinners beyond 2 seconds (timeout → error state)
  - No PII exposed in any React/SEO public view
```

---

## 1️⃣8️⃣ LEGAL & COMPLIANCE EXPORT SPECIFICATION

```yaml
COMPLIANCE_EXPORT_PACKAGE:
  Trigger:          Request from LEGAL_EXPORT_AGENT or COMPLIANCE_OFFICER role
  Auth:             COMPLIANCE_OFFICER role + legal_request_id required
  Contents:
    - All HLE records for target user (full JSON)
    - All UPO snapshots from cold store (decrypted for export, re-encrypted in export package)
    - All write audit log entries for target user
    - All read audit log entries where target_user_id matches
    - AI enrichment records for target user
    - HLE redaction event records (if any redactions occurred)
    - Metadata manifest: user_id, tenant_id, domain_track, first_version_date, last_version_date,
                         total_versions, total_hle_records, export_generated_at_utc
  Format:           NDJSON (one HLE per line) + structured manifest.json + optional PDF summary
  Encryption:       Export package encrypted with requestor's provided public key
  Audit:            Export event logged in passport_history_audit_log
  Jurisdiction:     Package includes data residency tag indicating storage region
  Supported_Frameworks: GDPR (EU) | India DPDP | Enterprise Audit (custom scope)
```

---

## 1️⃣9️⃣ DEPLOYMENT REQUIREMENTS

```yaml
CONTAINERIZATION:    Docker — Kubernetes deployment
WORKER_POOLS:        Separate pod pools for write workers and read workers
SCALING:             Horizontal pod autoscaling (HPA) — write pool scales on Kafka lag, read pool on CPU/RPS
DEPLOY_STRATEGY:     Blue/green with automated rollback
HEALTH_CHECKS:       /health/live (liveness) + /health/ready (readiness with Kafka + DB connectivity)
CONFIG:              Environment-separated config — no production secrets in code or env vars
SECRETS:             AWS Secrets Manager / HashiCorp Vault (mandatory)
MIGRATIONS:          DB migrations versioned and tied to agent version — no unversioned schema changes
ROLLBACK:            Automated rollback on write error rate spike > 0.1% in 5-minute window
STAGING:             Full staging environment validation required before production deploy
BACKUP:              Daily full DB backup — snapshot store has versioning enabled at object store level
RPO_TARGET:          < 1 hour (maximum data loss in worst-case failure)
RTO_TARGET:          < 15 minutes (maximum recovery time)
RESTORE_DRILLS:      Quarterly — recovery validation required

TEST_GATES (ALL MUST PASS — NO EXCEPTIONS):
  Unit Tests:
    - HLE write logic for all event types
    - Delta computation accuracy (all field types)
    - Velocity signal computation correctness
    - Idempotency enforcement (duplicate event handling)
    - Version gap detection
    - Version regression detection
    - Hash verification logic
    - Role-based access control enforcement per query type

  Integration Tests:
    - Kafka event consumption end-to-end from PAA mock
    - PostgreSQL INSERT-only enforcement (verify UPDATE/DELETE rejected at DB level)
    - Cold storage write and retrieval round-trip
    - Redis deduplication registry correctness
    - Point-in-time reconstruction for multiple timestamps

  Security Tests:
    - Unauthorized write source rejection
    - Cross-tenant query rejection
    - PII access control per role
    - hle_signature tamper detection

  Load Tests:
    - 3,000 write events/sec sustained for 15 minutes
    - 2,000 read queries/sec sustained for 15 minutes
    - Cold storage retrieval at scale (concurrent point-in-time requests)

  Compliance Tests:
    - GDPR redaction protocol end-to-end
    - Compliance export package generation and integrity verification
    - Audit log completeness verification

  Integrity Tests:
    - HLE version chain continuity (no gaps in sequence)
    - HLE_REDACTION_EVENT does not corrupt version chain
    - Snapshot hash matches stored UPO for 100 random HLE samples
```

---

## 2️⃣0️⃣ NON-NEGOTIABLE RULES (SEALED)

This agent MUST NOT, under any circumstance:

```yaml
FORBIDDEN:
  - Accept write events from any source other than PASSPORT_AGGREGATION_AGENT
  - Modify any HLE record after initial write — EVER
  - Delete any HLE record for any reason — EVER (redaction protocol replaces deletion for PII)
  - Produce a History Ledger Entry without hle_signature and upo_snapshot_hash
  - Store an HLE with a version gap from the prior version
  - Store an HLE where passport_version_new <= passport_version_prior
  - Allow any agent or user to query another tenant's history (zero cross-tenant tolerance)
  - Allow recruiter access to candidate history without verified CONSENT_FLAG = ACTIVE
  - Allow any AI layer output to overwrite or modify core HLE fields
  - Produce silent failures — every failure path must generate an audit log entry
  - Perform direct REST polling or database reads of other services — event-only + query-only
  - Block HLE writes due to AI enrichment failures — AI is supplementary, never blocking
  - Block HLE writes due to ML model unavailability — proceed with UNKNOWN classification
  - Expose PII fields to any role not in AUTHORIZED_PII_ROLES
  - Allow history queries without full audit logging of the read operation
  - Create hidden logic not documented in this specification
  - Override COMPLIANCE_AUDIT_AGENT, LEGAL_EXPORT_AGENT, or governance agents
  - Self-modify audit logs under any circumstance including its own error correction
  - Interpret ambiguous instructions — HALT and escalate instead
  - Mix domain data across domain tracks in any query or write path
  - Execute outside defined failure policies — no ad-hoc handling
```

---

## 2️⃣1️⃣ RELATIONSHIP TO PASSPORT_AGGREGATION_AGENT (PAA-001)

```yaml
DEPENDENCY_MODEL:
  - PHA is STRICTLY DOWNSTREAM of PAA — it never triggers PAA behavior
  - PHA reads PAA's emissions only — it has no write path back to PAA
  - PHA does not duplicate PAA's aggregation logic — it archives PAA's results
  - PHA's PASSPORT_HISTORY_FEATURE_VECTOR enriches the FEATURE_STORE independently
    from PAA's PASSPORT_FEATURE_VECTOR — both must be present for complete ML features
  - PHA's velocity anomaly alerts are INDEPENDENT of PAA's anomaly flags —
    they measure different things:
      PAA anomaly flags:  signal quality issues in current passport state
      PHA velocity flags: abnormal rate of change in passport history over time
  - Version chain continuity is a SHARED RESPONSIBILITY:
      PAA: must emit strictly monotonically increasing semver on every bump
      PHA: must verify continuity on receipt and halt on gap/regression
  - If PHA detects a version gap → PHA emits VERSION_GAP_DETECTED to PAA for investigation
    PAA is responsible for gap resolution — PHA waits for resolution event before writing
```

---

## 2️⃣2️⃣ SEAL DECLARATION

```
╔══════════════════════════════════════════════════════════════════════════╗
║            ECOSKILLER ANTIGRAVITY — SEALED AGENT ARTIFACT               ║
║                  PASSPORT_HISTORY_AGENT v1.0.0                          ║
║                        AGENT ID: PHA-002                                ║
║                                                                          ║
║  This specification is SEALED.                                           ║
║  No section may be altered without a formal version bump.                ║
║  No assumption may be filled by any implementing agent or developer.     ║
║  No creative interpretation is permitted under any circumstance.         ║
║  Any deviation from this spec constitutes an INVALID BUILD.              ║
║  An INVALID BUILD must STOP EXECUTION immediately.                       ║
║                                                                          ║
║  MUTATION_POLICY         = ADD-ONLY                                      ║
║  EXECUTION_MODE          = DETERMINISTIC + VALIDATED                     ║
║  FAILURE_MODE            = STOP_EXECUTION                                ║
║  SECURITY_MODEL          = ZERO-TRUST MULTI-TENANT                       ║
║  DATA_POLICY             = APPEND-ONLY IMMUTABLE LEDGER                  ║
║  CROSS_TENANT            = FORBIDDEN                                     ║
║  CROSS_DOMAIN            = FORBIDDEN                                     ║
║  SILENT_FAILURE          = FORBIDDEN                                     ║
║  HLE_MODIFICATION        = FORBIDDEN                                     ║
║  HLE_DELETION            = FORBIDDEN                                     ║
║  AI_OVERRIDE_OF_HLE      = FORBIDDEN                                     ║
║  UNAUTHORIZED_WRITE      = SECURITY_VIOLATION + STOP_EXECUTION           ║
║  VERSION_REGRESSION      = SECURITY_VIOLATION + STOP_EXECUTION           ║
║  VERSION_GAP             = HOLD + ESCALATE                               ║
║  PII_DELETION            = REDACTION_PROTOCOL_ONLY (skeleton retained)   ║
║                                                                          ║
║  Platform:       Ecoskiller Antigravity                                  ║
║  Architecture:   Microservices + Event-Driven                            ║
║  Scale Target:   10M–100M Users                                          ║
║  Agent Class:    Core Compliance & Historical Intelligence Infrastructure ║
║  Upstream:       PASSPORT_AGGREGATION_AGENT (PAA-001) — SOLE SOURCE      ║
║  Position:       Stage 4 — Compliance & Scale Layer                      ║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

*End of PASSPORT_HISTORY_AGENT.md — v1.0.0 — SEALED*
