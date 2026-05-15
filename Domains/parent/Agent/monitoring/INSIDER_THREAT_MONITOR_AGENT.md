# 🔒 INSIDER_THREAT_MONITOR_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED AGENT SPECIFICATION
**Status:** FINAL · SEALED · GOVERNED · DETERMINISTIC  
**Artifact Class:** Tier-0 Security & Compliance Governance Agent  
**Mutation Policy:** ADD-ONLY via version bump  
**Interpretation Authority:** NONE  
**Creative Deviation:** FORBIDDEN  
**Assumption Filling:** FORBIDDEN  
**Default Behavior:** DENY  
**Override Authority:** NONE  
**Clearance Required to Read Alerts:** SECURITY_LEAD + COMPLIANCE_ADMIN  

---

## 🔐 EXECUTION MODE DECLARATION

```
AGENT_LOCK_STATUS                  = SEALED
EXECUTION_MODE                     = DETERMINISTIC + VALIDATED
MUTATION_POLICY                    = ADD_ONLY
CREATIVE_INTERPRETATION            = FORBIDDEN
ASSUMPTION_FILLING                 = FORBIDDEN
DEFAULT_BEHAVIOR                   = DENY
FAILURE_MODE                       = STOP_EXECUTION → LOG_INCIDENT → ESCALATE
OVERRIDE_AUTHORITY                 = NONE
BYPASS_ATTEMPTS                    = CLASSIFIED AS P0_SECURITY_VIOLATION → AUTO_ESCALATE
SELF_MODIFICATION                  = ABSOLUTELY FORBIDDEN
AUTO_REMEDIATION                   = FORBIDDEN (detect + alert + escalate only)
DECISION_AUTONOMY                  = ZERO beyond declared rules
ALERT_SUPPRESSION                  = ABSOLUTELY FORBIDDEN
FALSE_POSITIVE_SUPPRESSION         = FORBIDDEN without SECURITY_LEAD written approval
AGENT_OUTPUTS_CLASSIFIED           = RESTRICTED — accessible only by declared roles
AGENT_OWN_LOGS_CLASSIFICATION      = SECRET — accessible only by COMPLIANCE_ADMIN
                                     and SECURITY_LEAD
```

---

## 1️⃣ AGENT IDENTITY (NON-NEGOTIABLE)

```
AGENT_NAME            = INSIDER_THREAT_MONITOR_AGENT
AGENT_ID              = ITMA-ANTIGRAVITY-001
SYSTEM_ROLE           = Internal Actor Behavioral Anomaly Detector,
                        Privileged Access Abuse Enforcer, Data Exfiltration
                        Guardian, Compliance Violation Detector &
                        Zero-Trust Internal Posture Enforcer
PRIMARY_DOMAIN        = Security / Compliance / Governance / Audit
EXECUTION_MODE        = Deterministic + Validated
DATA_SCOPE            = All internal actor sessions (admins, ops staff,
                        ML engineers, DevOps, tenant admins, privileged
                        service accounts, interns, API key holders),
                        their access logs, audit trails, behavioral
                        sequences, data export requests, privilege
                        escalation events, and unusual operational patterns.
                        This agent NEVER accesses user PII directly —
                        it operates on access metadata, behavioral
                        signals, and audit log patterns only.
TENANT_SCOPE          = Platform-Wide (monitors all tenant admin actors)
                        + Per-Tenant Isolation of findings
                        (finding about Tenant A is not disclosed to Tenant B)
FAILURE_POLICY        = Halt on Ambiguity → Log → Escalate → Never Silent
AGENT_CLASS           = Tier-0 Security Governance Agent
PLATFORM              = Ecoskiller Antigravity
ARCHITECTURE          = Microservices + Event-Driven
SCALE_TARGET          = 10M–100M Users
                        Monitors up to 10,000 internal actors simultaneously
```

### Critical Distinction: Why INSIDER Threat, Not External

The Ecoskiller Antigravity platform has a robust perimeter security posture (WAF, mTLS, JWT, CAPTCHA, rate limiting, brute-force protection via R51, etc.). **The INSIDER_THREAT_MONITOR_AGENT (ITMA) addresses a fundamentally different threat surface:** actors who are already authenticated, already authorized, and who have legitimate access credentials — but who may misuse those credentials in ways that harm the platform, its tenants, its users, or its compliance posture.

| Insider Actor Class | Platform Context |
|---|---|
| Platform Super Admins | Access to tenant management, billing, emergency overrides, ops console |
| Tenant Admins (Institute / Enterprise) | Access to student/employee data within their tenant |
| ML Engineers | Access to training data, model weights, feature stores |
| DevOps / Infra Engineers | Access to Kubernetes, databases, secrets vault, CI/CD |
| Ops Console Operators | Access to moderation queues, content review, user account actions |
| Compliance Officers | Access to audit logs, regulatory exports, right-to-forget tools |
| Customer Support Agents | Access to user accounts for support resolution |
| Intern Developers | Limited but real access to dev/staging environments |
| Privileged Service Accounts | Automated agents with elevated access scopes |
| API Key Holders | External integrations with elevated tenant-scoped permissions |

Without ITMA, none of the following are detectable until the damage is done:

- A support agent browsing thousands of user profiles without support tickets
- A ML engineer exfiltrating training feature data via API
- A tenant admin querying students from a different institution within the same tenant
- A DevOps engineer accessing production secrets vault outside maintenance windows
- A compromised admin account conducting reconnaissance before a data breach
- A disgruntled ops operator silently modifying reputation scores
- A privileged service account consuming 10x its normal data volume
- A compliance officer bulk-exporting audit logs to an unauthorized destination
- An intern accessing production data they were never supposed to reach

---

## 2️⃣ PURPOSE DECLARATION

### What Problem This Agent Solves

ITMA continuously monitors **behavioral patterns** of all privileged internal actors across the Ecoskiller Antigravity platform. It operates on access logs, audit trails, API call sequences, data volume patterns, time-of-day patterns, and privilege usage patterns — never on user content or PII directly.

**Core functions:**

1. **Behavioral Baseline Establishment** — For every registered internal actor, establish what "normal" looks like: what they access, when, at what volume, from what location, in what sequence.

2. **Anomaly Detection** — Detect deviations from established behavioral baselines that indicate potential misuse, account compromise, or policy violation.

3. **Privilege Abuse Detection** — Detect internal actors using legitimate access to perform actions outside their operational purpose (e.g., support agent browsing non-ticket-related profiles).

4. **Data Exfiltration Detection** — Detect unusual data export, bulk download, or data volume patterns that could indicate unauthorized data exfiltration.

5. **Reconnaissance Detection** — Detect access patterns consistent with reconnaissance behavior (wide enumeration of records, systematic data mapping).

6. **Privilege Escalation Abuse** — Detect unauthorized or anomalous privilege escalation attempts or usage of elevated credentials outside approved change windows.

7. **Off-Hours Access Detection** — Detect accesses occurring outside an actor's established working pattern, especially for high-privilege operations.

8. **Cross-Tenant Boundary Probing** — Detect tenant admins attempting to access or enumerate data outside their assigned tenant boundary.

9. **Compliance Action Abuse** — Detect misuse of compliance tools (bulk audit log export, right-to-forget tools, moderation overrides).

10. **Service Account Anomaly Detection** — Detect privileged service accounts consuming data outside their registered scope or at anomalous volumes.

11. **Insider Threat Scoring** — Maintain a continuously updated risk score for every internal actor, aggregating behavioral signals over time.

12. **Immutable Evidence Package Generation** — For every confirmed or suspected incident, generate a forensic-quality evidence package for investigation.

**What ITMA does NOT do:**
- It does NOT access, read, or analyze user content (posts, messages, resumes)
- It does NOT access PII fields of end users directly
- It does NOT auto-terminate sessions (escalate only — human authorization required)
- It does NOT directly discipline or restrict actors (governance authority only)
- It does NOT expose findings to parties without declared clearance
- It does NOT flag actors for external law enforcement (governance + legal team decision only)

### What Input It Consumes

```
FROM AUDIT_LOG_AGENT (primary — mandatory):
  - All internal actor action events (append-only audit stream)
  - Includes: actor_id, action_type, entity_type, entity_id, timestamp,
    tenant_id, role_used, ip_address_hash, session_id, result_code

FROM API_GATEWAY_SERVICE:
  - Internal actor API call telemetry (endpoint, method, response_code,
    response_time, payload_size_bytes, tenant_id)
  - NOT payload content — metadata only

FROM AUTHORIZATION_SERVICE:
  - RBAC privilege escalation events
  - Permission check outcomes (granted/denied) per actor per session
  - Role assignment change events

FROM IDENTITY_SERVICE:
  - Login events (timestamp, ip_hash, device_fingerprint_hash, mfa_used)
  - Session creation and termination events
  - Failed authentication events per actor
  - Password/credential change events

FROM DATA_EXPORT_SERVICE:
  - All data export requests (actor_id, export_type, record_count,
    destination_type, tenant_id, timestamp)
  - Right-to-forget execution events
  - Bulk audit log export events

FROM SECRETS_VAULT (HashiCorp Vault):
  - Secret access events (actor_id, secret_path, operation, timestamp)
  - No secret values — access metadata only

FROM KUBERNETES_API_SERVER:
  - kubectl/API access events for privileged ops actors
  - Pod exec events, log access events, secret access events
  - Namespace boundary crossing events

FROM CI_CD_PIPELINE (GitLab CE):
  - Pipeline execution events per actor
  - Variable/secret access events in pipeline
  - Deployment approval events

FROM DB_POOL_MANAGER:
  - Direct database query volume per service account
  - Unusual table access patterns per service account
  - Query execution counts outside normal service bounds

FROM TENANT_MANAGEMENT_SERVICE:
  - Tenant boundary access attempts
  - Cross-tenant admin action events

FROM OPS_CONSOLE_SERVICE:
  - All ops console user actions (emergency override uses, moderation
    actions, account lock/unlock, billing overrides)
  - Action volume and frequency per ops actor

FROM AGENT_REGISTRY_SERVICE:
  - Privileged service account registration and scope changes
```

### What Output It Produces

```
- Per-actor behavioral risk score (continuously updated, 0–100)
- Insider threat alerts (classified by severity: P0, P1, P2, P3)
- Behavioral anomaly reports (per actor, per session, per incident)
- Reconnaissance detection reports
- Data exfiltration detection reports
- Privilege abuse reports
- Cross-tenant boundary probe reports
- Compliance tool abuse reports
- Service account anomaly reports
- Insider threat evidence packages (forensic-quality, immutable)
- Aggregated insider threat posture reports (weekly, for SECURITY_LEAD)
- Actor risk trend signals (for FEATURE_STORE_AGENT — anonymized)
```

### Upstream Agents / Services That Feed This Agent

```
MANDATORY (ITMA halts if any unavailable):
  - AUDIT_LOG_AGENT
  - API_GATEWAY_SERVICE
  - AUTHORIZATION_SERVICE
  - IDENTITY_SERVICE
  - TENANT_MANAGEMENT_SERVICE
  - AGENT_REGISTRY_SERVICE

OPTIONAL (degraded mode if unavailable):
  - DATA_EXPORT_SERVICE
  - SECRETS_VAULT_AUDIT
  - KUBERNETES_API_AUDIT
  - CI_CD_PIPELINE_AUDIT
  - DB_POOL_MANAGER_AUDIT
  - OPS_CONSOLE_SERVICE
```

### Downstream Agents / Services That Depend on This Agent

```
PRIMARY:
  - SECURITY_AGENT               (receives all P0/P1 insider threat alerts)
  - GOVERNANCE_AGENT             (receives compliance violation alerts)
  - COMPLIANCE_AGENT             (receives audit-grade evidence packages)
  - INCIDENT_MANAGEMENT_AGENT    (receives P0/P1 escalation triggers)
  - AUDIT_LOG_AGENT              (receives ITMA's own immutable log entries)

SECONDARY:
  - OBSERVABILITY_AGENT          (receives ITMA health metrics — NOT alert content)
  - FEATURE_STORE_AGENT          (receives anonymized platform risk posture vectors)

STRICTLY FORBIDDEN DOWNSTREAM:
  - NOTIFICATION_SERVICE         (NO user-facing notifications about ITMA findings)
  - ANY_PUBLIC_API               (ITMA findings are NEVER exposed to external APIs)
  - OTHER_TENANTS                (tenant-specific findings stay within that tenant's
                                  governance channel — never cross-disclosed)
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

Zero null tolerance on required fields. All inputs must be validated. Log all failures.

```json
INPUT_SCHEMA: {
  "required_fields": [
    "signal_id",
    "signal_type",
    "actor_id",
    "actor_role",
    "actor_type",
    "session_id",
    "signal_timestamp_utc",
    "source_service_id",
    "tenant_id",
    "environment",
    "payload_schema_version"
  ],
  "optional_fields": [
    "entity_type",
    "entity_id",
    "action_type",
    "api_endpoint",
    "api_method",
    "response_code",
    "payload_size_bytes",
    "ip_address_hash",
    "device_fingerprint_hash",
    "export_type",
    "export_record_count",
    "secret_path_hash",
    "namespace",
    "mfa_verified",
    "privilege_level_used",
    "change_window_active",
    "query_count",
    "table_accessed"
  ],
  "signal_type_enum": [
    "AUDIT_ACTION_EVENT",
    "API_CALL_TELEMETRY",
    "LOGIN_EVENT",
    "SESSION_EVENT",
    "PRIVILEGE_ESCALATION_EVENT",
    "DATA_EXPORT_REQUEST",
    "SECRET_ACCESS_EVENT",
    "K8S_PRIVILEGED_ACCESS_EVENT",
    "CICD_PIPELINE_EVENT",
    "DB_QUERY_VOLUME_SIGNAL",
    "OPS_CONSOLE_ACTION_EVENT",
    "ROLE_ASSIGNMENT_CHANGE_EVENT",
    "CROSS_TENANT_ACCESS_ATTEMPT",
    "FAILED_AUTH_EVENT"
  ],
  "actor_type_enum": [
    "PLATFORM_SUPER_ADMIN",
    "TENANT_ADMIN_INSTITUTE",
    "TENANT_ADMIN_ENTERPRISE",
    "ML_ENGINEER",
    "DEVOPS_ENGINEER",
    "OPS_CONSOLE_OPERATOR",
    "COMPLIANCE_OFFICER",
    "SUPPORT_AGENT",
    "INTERN_DEVELOPER",
    "PRIVILEGED_SERVICE_ACCOUNT",
    "API_KEY_HOLDER"
  ],
  "validation_rules": [
    "signal_id MUST be UUID v4 — reject all other formats",
    "actor_id MUST resolve in ACTOR_REGISTRY — reject unknown actors",
    "actor_role MUST match registered role for actor_id — reject mismatches",
    "actor_type MUST be a declared enum value — reject unlisted types",
    "session_id MUST resolve to an active or recently-closed session — reject orphan signals",
    "signal_timestamp_utc MUST be ISO-8601 UTC — reject malformed",
    "signal_timestamp_utc MUST NOT be older than 24 hours — reject stale signals",
    "signal_timestamp_utc MUST NOT be more than 60 seconds in the future — reject clock-skew",
    "tenant_id MUST resolve to an active tenant — reject unresolved",
    "environment MUST be one of [dev, test, staging, production]",
    "payload_size_bytes MUST be non-negative integer if provided",
    "export_record_count MUST be non-negative integer if provided",
    "ip_address_hash MUST be SHA-256 format if provided — reject raw IPs",
    "signal_type MUST be a declared enum value — reject unlisted types",
    "source_service_id MUST be a registered service — reject unknown sources"
  ],
  "security_checks": [
    "Validate mTLS source certificate of signal-emitting service",
    "Validate signal emitter is an authorized audit signal source (not user-space)",
    "Reject any signal containing raw PII (email, phone, name in any field)",
    "Validate ip_address_hash format — reject raw IP addresses in any field",
    "Reject payload exceeding 512KB",
    "Validate TLS 1.3 minimum on all signal transport",
    "Validate that signal source service is not a user-facing service",
    "Reject signals emitted by actors claiming to report on themselves
     through user-space channels (all signals must come from system services only)"
  ],
  "domain_checks": [
    "Verify actor_id's tenant_id matches signal tenant_id",
    "Verify PLATFORM_SUPER_ADMIN actors are registered in SUPER_ADMIN_REGISTRY",
    "Cross-tenant signals only valid for PLATFORM_SUPER_ADMIN actor_type",
    "All other actor_types emitting cross-tenant signals = REJECT + SECURITY_FLAG"
  ],
  "null_policy": {
    "required_fields": "ZERO NULL TOLERANCE — reject and log immediately",
    "optional_fields": "NULL allowed but must be explicitly declared null",
    "action_on_rejection": "LOG_VALIDATION_FAILURE → EMIT_REJECTION_EVENT → NO_PARTIAL_PROCESSING"
  }
}
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

All outputs must carry version reference, audit reference, and full traceability. Outputs are CLASSIFIED — only accessible to declared roles.

```json
OUTPUT_SCHEMA: {

  "BEHAVIORAL_RISK_SCORE_UPDATE": {
    "risk_record_id":                "UUID (immutable)",
    "actor_id_hash":                 "SHA-256 of actor_id (never raw actor_id in output)",
    "actor_type":                    "string",
    "tenant_id":                     "string",
    "evaluation_timestamp_utc":      "ISO-8601",
    "risk_score_current":            "integer 0–100",
    "risk_score_previous":           "integer 0–100",
    "risk_score_delta":              "integer (signed)",
    "risk_tier":                     "LOW | ELEVATED | HIGH | CRITICAL",
    "contributing_signals":          ["array of signal_type codes contributing to score"],
    "trend_direction":               "STABLE | RISING | FALLING | SPIKE",
    "recommended_review":            "boolean"
  },

  "INSIDER_THREAT_ALERT": {
    "alert_id":                      "UUID (immutable)",
    "alert_timestamp_utc":           "ISO-8601",
    "alert_severity":                "P0 | P1 | P2 | P3",
    "threat_category":               "string (enum — see Section 8)",
    "actor_id_hash":                 "SHA-256 of actor_id",
    "actor_type":                    "string",
    "tenant_id":                     "string",
    "environment":                   "string",
    "alert_summary":                 "string (max 200 chars, no PII)",
    "evidence_signal_ids":           ["array of signal_ids that triggered alert"],
    "behavioral_context":            {
      "baseline_value":              "float | integer",
      "observed_value":              "float | integer",
      "deviation_factor":            "float",
      "observation_window_minutes":  "integer"
    },
    "recommended_action":            "MONITOR | INVESTIGATE | RESTRICT_SESSION | ESCALATE_P0",
    "escalation_target":             "string | null",
    "evidence_package_id":           "UUID (reference to immutable evidence package)"
  },

  "EVIDENCE_PACKAGE": {
    "package_id":                    "UUID (immutable, WORM-stored)",
    "package_timestamp_utc":         "ISO-8601",
    "alert_id":                      "UUID (reference)",
    "actor_id_hash":                 "SHA-256 of actor_id",
    "actor_type":                    "string",
    "tenant_id":                     "string",
    "incident_window_start_utc":     "ISO-8601",
    "incident_window_end_utc":       "ISO-8601",
    "signal_count":                  "integer",
    "signal_ids":                    ["array of all signal_ids in evidence set"],
    "behavioral_timeline":           ["ordered array of behavioral events — no PII content"],
    "anomaly_indicators":            ["array of specific anomaly codes"],
    "chain_of_custody_hash":         "SHA-256 of entire package (tamper detection)",
    "classification":                "RESTRICTED",
    "access_roles":                  ["SECURITY_LEAD", "COMPLIANCE_ADMIN"]
  }
},

"confidence_score":   "0.0–1.0 (float, two decimal places)",
"model_version":      "ITMA-DETECTION-ENGINE-v{major}.{minor}.{patch}",
"audit_reference":    "UUID (immutable, append-only)",
"next_trigger_event": [
  "RISK_SCORE_UPDATED",
  "INSIDER_THREAT_ALERT_P0 (conditional)",
  "INSIDER_THREAT_ALERT_P1 (conditional)",
  "INSIDER_THREAT_ALERT_P2 (conditional)",
  "INSIDER_THREAT_ALERT_P3 (conditional)",
  "EVIDENCE_PACKAGE_CREATED (conditional)",
  "ESCALATION_TRIGGERED (conditional)",
  "GOVERNANCE_REVIEW_REQUIRED (conditional)"
]
```

**Classification Rule:**  
All ITMA outputs are classified as RESTRICTED. They are accessible only to SECURITY_LEAD and COMPLIANCE_ADMIN. No other role, agent, or service may read ITMA alert outputs. Requests from unauthorized roles are rejected and logged as security incidents.

---

## 5️⃣ ML / AI LOGIC LAYER

### Behavioral Baseline Engine (Primary — 60% of detection)

```
MODEL_TYPE              = Unsupervised Behavioral Baseline + Statistical Deviation

BASELINE_CONSTRUCTION:
  For every registered internal actor, ITMA maintains:
    - Rolling 30-day behavioral baseline per actor
    - Baseline features (see below)
    - Baseline recomputed weekly (rolling window)
    - New actors: 14-day observation period before baseline-based alerting
      (signals still collected — no baseline alerts for first 14 days)

BASELINE_FEATURES_PER_ACTOR:
  Access Pattern Features:
    - api_calls_per_hour (mean, std, max)
    - api_calls_per_day (mean, std, max)
    - unique_entities_accessed_per_day (mean, std, max)
    - unique_tenants_accessed_per_day (for PLATFORM_SUPER_ADMIN)
    - unique_api_endpoints_per_day (mean, std, max)
    - session_duration_minutes (mean, std)
    - session_count_per_day (mean, std)
    - active_hours_distribution (histogram — hourly buckets)
    - active_days_distribution (bitmask — day of week)
    - ip_hash_diversity (count of unique ip_address_hashes per 7 days)
    - mfa_verification_rate (ratio of mfa_verified=true sessions)

  Data Volume Features:
    - export_requests_per_day (mean, std, max)
    - export_record_count_per_request (mean, std, max)
    - total_export_record_count_per_day (mean, std, max)
    - payload_size_bytes_per_call (mean, std, max)
    - total_data_transferred_bytes_per_day (mean, std, max)

  Privilege Usage Features:
    - privilege_escalation_events_per_day (mean, std)
    - secret_access_events_per_day (mean, std, max)
    - ops_console_high_privilege_actions_per_day (mean, std)
    - emergency_override_uses_per_30d (count)
    - role_assignment_changes_made_per_day (mean, std)

  Failure/Rejection Features:
    - failed_auth_events_per_day (mean, std)
    - permission_denied_events_per_day (mean, std)
    - rejected_api_calls_per_day (mean, std)

DEVIATION_DETECTION:
  Z-score based detection:
    z_score = (observed_value - baseline_mean) / baseline_std
    z_score > 3.0 → anomaly_flag (MINOR)
    z_score > 5.0 → anomaly_flag (MAJOR)
    z_score > 8.0 → anomaly_flag (CRITICAL)
  
  Volume surge detection:
    observed_value > (baseline_max × 2.5) → volume_surge_flag
    observed_value > (baseline_max × 5.0) → critical_volume_surge_flag
  
  Off-hours detection:
    Action in hour bucket with baseline frequency < 0.02 (< 2% of days)
    AND privilege_level_used = HIGH or CRITICAL → off_hours_high_privilege_flag

TRAINING_FREQUENCY = Baseline recomputed weekly per actor (rolling 30-day window)
VERSION_CONTROL    = Baseline snapshots versioned and stored immutably per actor
```

### Pattern Recognition Engine (Secondary — 30% of detection)

```
MODEL_TYPE              = Supervised Classification (known threat patterns)
                          + Sequence Pattern Matching (behavioral sequences)

KNOWN_THREAT_PATTERNS:
  (Predefined rule sets — deterministic, not ML-based)
  See Section 8 for full pattern definitions.

SEQUENCE_PATTERN_MATCHING:
  - Sliding window of N=50 most recent events per actor
  - Match against known malicious behavioral sequences
  - Example: login → enumerate users → bulk export → session end
    within a 2-hour window = reconnaissance pattern
  - Example: login at off-hours → access secrets vault → access DB directly
    → no standard service API calls → exfiltration pattern indicator
  
MODEL_TYPE_SUPERVISED   = Gradient Boosting Classifier (XGBoost)
FEATURES_USED           = All baseline features + rolling deviation scores
                          + sequence pattern match indicators
                          + time-of-day encoding + actor_type encoding
LABELS                  = Behavioral risk tier (LOW / ELEVATED / HIGH / CRITICAL)
TRAINING_FREQUENCY      = Monthly (on anonymized, labeled incident history)
TRAINING_DATA_POLICY    = NO raw PII, NO actor_id in features
                          Actor represented by anonymized behavioral vector only
DRIFT_DETECTION         = Weekly distribution check (KL divergence > 0.1 → retrain flag)
                          Monthly accuracy check (precision drop > 5% → retrain flag)
```

### Rules Engine (Supplementary — 10% of detection)

```
HARD_RULE_ENGINE:
  Deterministic rules that trigger regardless of baseline or ML:
  These are ALWAYS-ON — no baseline observation period, no ML threshold.
  
  See Section 8 for complete hard rule definitions.
  All hard rules trigger at minimum P1 (some at P0).
```

### AI Usage Policy

```
AI_USAGE_SCOPE          = NONE in any detection, scoring, or escalation path
                          AI MAY assist ONLY in generating the plain-language
                          summary section of evidence packages (offline, for
                          SECURITY_LEAD review — never in real-time detection)
PROMPT_GOVERNANCE       = Versioned | Deterministic templates | No creative interpretation
                          AI MUST NOT influence risk scores
                          AI MUST NOT classify threat categories
                          AI MUST NOT recommend investigation actions
```

---

## 6️⃣ SCALABILITY DESIGN

```
EXPECTED_RPS            = 1,000–20,000 signals/sec at peak
                          (10,000 internal actors × up to 2 signals/sec each)
LATENCY_TARGET:
  Signal ingestion P99   = < 100ms
  Risk score update P99  = < 500ms
  P0 alert emission P99  = < 1,000ms (< 1 second from signal ingestion)
  Evidence package gen P99 = < 10 seconds (async)

MAX_CONCURRENCY         = 1,000 parallel signal processors
QUEUE_STRATEGY          = Kafka-backed ingestion (topic: itma.signals.raw)
                          Priority channel for hard-rule triggers: itma.signals.priority
                          Evidence package queue: itma.evidence.requests
                          Dead Letter Queue: itma.signals.dlq

SCALING_MODEL           = Horizontal auto-scaling (Kubernetes HPA)
                          Stateless execution per ITMA instance

EXECUTION_STATE         = Stateless per execution
                          Actor behavioral baselines: PostgreSQL + Redis cache
                          Incident evidence packages: WORM object storage
                          Risk scores: Redis (TTL = 24h, persisted to PostgreSQL daily)

IDEMPOTENCY             = All signal processing idempotent
                          (Idempotency key: signal_id + actor_id_hash + signal_timestamp_utc)

ASYNC_PROCESSING        = Yes — Kafka consumer group model
PRIORITY_PROCESSING     = Hard-rule triggers bypass standard queue
                          → priority channel → P99 < 1 second to alert emission

CIRCUIT_BREAKER         = Enabled on all upstream signal source connections
                          (5 failures in 30s → open circuit, retry after 60s)
```

---

## 7️⃣ MONITORED ACTOR PROFILES — BEHAVIORAL CONTRACTS

Each actor type has a declared behavioral contract. Deviations from this contract are the primary basis for alerts.

### 7A — PLATFORM_SUPER_ADMIN

```
BEHAVIORAL_CONTRACT:
  Authorized actions:    Tenant management, platform config, emergency overrides,
                         billing dispute resolution, ops console access, user moderation
  Expected volume:       Low-to-medium (< 500 API calls/day normal range)
  Expected hours:        Business hours (08:00–20:00 UTC, Mon–Fri baseline)
  Expected location:     Consistent ip_address_hash patterns
  MFA required:          ALWAYS — any session without MFA = IMMEDIATE P0 ALERT
  Change windows:        High-privilege operations (DB, K8s, secrets) only during
                         declared maintenance windows

HIGH-RISK BEHAVIORS:
  - Bulk user data access (> 100 user profiles per hour without open support tickets)
  - Cross-tenant data access (viewing data of Tenant A while managing Tenant B)
  - Off-hours access to emergency override tools
  - Role assignment changes without change ticket reference
  - Multiple failed MFA attempts followed by success (account compromise indicator)
  - Accessing billing data of tenants they are not assigned to manage
  - Downloading audit logs outside declared compliance windows
```

### 7B — TENANT_ADMIN_INSTITUTE / TENANT_ADMIN_ENTERPRISE

```
BEHAVIORAL_CONTRACT:
  Authorized scope:      Their own tenant's data ONLY
  Authorized actions:    User management within tenant, institution/company config,
                         student/employee record management, reporting
  Expected volume:       Medium (varies by institution size)
  Cross-tenant access:   ABSOLUTELY FORBIDDEN — any cross-tenant probe = P0 ALERT

HIGH-RISK BEHAVIORS:
  - Attempting to query user_ids outside their tenant
  - Bulk export of student/employee records exceeding normal patterns
  - Accessing domain verification tools for other tenants
  - Using invitation flows to create accounts outside their domain
  - Accessing moderation queues they are not assigned to
  - Generating reports that aggregate across tenants
```

### 7C — ML_ENGINEER

```
BEHAVIORAL_CONTRACT:
  Authorized scope:      ML training infrastructure, model registry, feature store
                         (NO direct production user data access)
  Authorized actions:    Model training triggers, feature store reads (aggregated),
                         model version deployment (in approved pipeline only)
  Expected volume:       Variable (training cycles cause bursts — registered in advance)
  PII access:            FORBIDDEN — ML engineers must never access production user PII
  Direct DB access:      FORBIDDEN outside training environment

HIGH-RISK BEHAVIORS:
  - Accessing PRODUCTION feature store outside declared training window
  - Querying individual user feature vectors (vs aggregate)
  - Attempting direct database access to production tables
  - Exporting feature data to destinations outside approved data pipeline
  - Accessing model inference logs that contain user_id references
  - Running model inference outside CI/CD pipeline (direct API calls)
  - Accessing secrets beyond ML service credentials
```

### 7D — DEVOPS_ENGINEER

```
BEHAVIORAL_CONTRACT:
  Authorized scope:      Infrastructure management, CI/CD pipeline, K8s cluster,
                         deployment operations
  Authorized actions:    Deployment, scaling, monitoring, incident response
  Secrets access:        Only service credentials for services they own
  Production access:     Only during declared maintenance/deployment windows
  Pod exec:              FORBIDDEN in production outside incident response
  Direct DB access:      FORBIDDEN in production outside incident response + approval

HIGH-RISK BEHAVIORS:
  - Pod exec into production pods outside maintenance windows
  - Accessing secrets outside their registered service ownership
  - Exporting Kubernetes logs containing user data to non-approved destinations
  - Direct production database connection outside incident response
  - Disabling audit logging in any service
  - Modifying RBAC policies outside change management
  - Accessing application-level data via infrastructure tools
```

### 7E — OPS_CONSOLE_OPERATOR

```
BEHAVIORAL_CONTRACT:
  Authorized actions:    Content moderation, user account actions (lock/unlock),
                         support ticket resolution, billing dispute first-line
  Expected profile access: ONLY profiles with associated open support tickets
  Expected volume:       Consistent with assigned ticket queue size

HIGH-RISK BEHAVIORS:
  - Accessing user profiles WITHOUT associated support tickets (browsing)
  - Accessing user financial records without a billing dispute ticket
  - Using emergency override tools without an approved incident ticket
  - Bulk downloading user data outside ticket scope
  - Accessing data of high-profile accounts not in their queue
  - Modifying reputation scores without moderation ticket
  - Accessing accounts of colleagues or known individuals
  - Elevated frequency of account unlocks in a short window
```

### 7F — COMPLIANCE_OFFICER

```
BEHAVIORAL_CONTRACT:
  Authorized actions:    Audit log queries, regulatory export generation,
                         right-to-forget execution, compliance report generation
  Expected volume:       Periodic (not continuous) — query bursts during audit periods
  Destinations:          Exports only to pre-approved, registered destinations

HIGH-RISK BEHAVIORS:
  - Exporting audit logs to destinations not in APPROVED_DESTINATIONS_REGISTRY
  - Executing right-to-forget operations in bulk (> 10/hour without batch approval)
  - Accessing audit logs of other compliance officers' actions
  - Querying audit logs without declared audit ticket reference
  - Exporting data outside declared compliance window
  - Accessing financial transaction data outside compliance mandate
```

### 7G — SUPPORT_AGENT

```
BEHAVIORAL_CONTRACT:
  Authorized actions:    View user account data for ticket resolution only
  Scope:                 ONLY users with active support tickets assigned to them
  PII access:            Limited fields only (no financial, no intelligence profile)

HIGH-RISK BEHAVIORS:
  - Profile access without associated support ticket = ALWAYS FLAG
  - Accessing user payment methods or billing history without billing ticket
  - Accessing user intelligence profiles (EIE/HIA) — not in support scope
  - Accessing accounts of users they know personally (conflict of interest)
  - Copying user contact information to external systems
  - High volume of profile views in a short period
```

### 7H — INTERN_DEVELOPER

```
BEHAVIORAL_CONTRACT:
  Authorized environments: DEV and TEST only
  Production access:       FORBIDDEN entirely
  Data access:             Seed data and anonymized test data only

HIGH-RISK BEHAVIORS:
  - ANY production environment access = IMMEDIATE P0 ALERT
  - ANY production credentials access = IMMEDIATE P0 ALERT
  - ANY production database connection = IMMEDIATE P0 ALERT
  - Attempting to access staging environment with production credentials
```

### 7I — PRIVILEGED_SERVICE_ACCOUNT

```
BEHAVIORAL_CONTRACT:
  Scope:           Exactly as declared in AGENT_REGISTRY_SERVICE
  Volume:          Within declared RPS and data volume bounds
  Destinations:    Only registered downstream services

HIGH-RISK BEHAVIORS:
  - API calls to endpoints not in registered scope
  - Data volume > 3x declared normal range
  - Calls to services outside registered downstream dependency list
  - Authentication from IP ranges outside registered network segment
  - Accessing endpoints during times not consistent with their execution cycle
```

---

## 8️⃣ THREAT DETECTION CATALOG — COMPLETE

### 8A — Reconnaissance Patterns

```
THREAT_CODE: RECON-001 — WIDE_ENTITY_ENUMERATION
  Definition: Actor accesses > baseline_max × 3 unique entity_ids
              (user profiles, job records, tenant records) within 1 hour
  Severity:   P2 first occurrence, P1 if repeated within 24 hours
  Hard rule:  No (baseline-based)
  Evidence:   Complete entity_id access sequence (hashed), timestamps

THREAT_CODE: RECON-002 — SYSTEMATIC_SCAN_PATTERN
  Definition: Actor accesses entities in sequential ID order
              (entity_id incrementing pattern within a session)
  Severity:   P1
  Hard rule:  Yes — triggers regardless of volume
  Evidence:   Sequential access pattern with timing

THREAT_CODE: RECON-003 — CROSS_MODULE_ENUMERATION
  Definition: Actor accesses entities across > 4 different entity_types
              within a 2-hour window with no associated support/ops ticket
  Severity:   P2
  Hard rule:  No (baseline-based)

THREAT_CODE: RECON-004 — PRIVILEGE_MAP_PROBE
  Definition: Actor generates > 20 permission_denied events within 30 minutes
              (probing permission boundaries)
  Severity:   P1
  Hard rule:  Yes
  Evidence:   All denied endpoint access patterns

THREAT_CODE: RECON-005 — SCHEMA_PROBING
  Definition: Direct database schema inspection queries from any actor
              outside declared DBA role
  Severity:   P0
  Hard rule:  Yes — immediate escalation
```

### 8B — Data Exfiltration Patterns

```
THREAT_CODE: EXFIL-001 — BULK_EXPORT_ANOMALY
  Definition: export_record_count > (actor_baseline_max_export × 5)
              in a single request OR session
  Severity:   P1 (volume 5x–10x baseline), P0 (> 10x baseline)
  Hard rule:  P0 threshold is hard rule

THREAT_CODE: EXFIL-002 — UNAUTHORIZED_EXPORT_DESTINATION
  Definition: Export request targeting a destination not in APPROVED_DESTINATIONS_REGISTRY
  Severity:   P0
  Hard rule:  Yes — always P0 regardless of volume

THREAT_CODE: EXFIL-003 — STAGED_EXFILTRATION
  Definition: Multiple sequential export requests, each below alert threshold,
              but cumulative total in a session exceeds threshold
              (salami-slicing pattern)
  Detection:  Rolling session-level export accumulator
  Severity:   P1 (cumulative > 10x daily baseline)
  Hard rule:  No (session-accumulator based)

THREAT_CODE: EXFIL-004 — AUDIT_LOG_MASS_EXPORT
  Definition: Compliance officer bulk-exports audit logs covering
              > 30 days in a single request without declared compliance ticket
  Severity:   P1
  Hard rule:  Yes

THREAT_CODE: EXFIL-005 — DATA_COPY_TO_PERSONAL_ASSET
  Definition: API call patterns consistent with copying data to
              an unregistered external endpoint
              (detected via unusual response destination patterns in API gateway logs)
  Severity:   P0
  Hard rule:  Yes

THREAT_CODE: EXFIL-006 — ML_FEATURE_EXFILTRATION
  Definition: ML_ENGINEER querying individual-level feature vectors
              from FEATURE_STORE_AGENT at > 100 unique user vectors/hour
  Severity:   P1
  Hard rule:  Yes (individual user feature vectors are PII-adjacent)

THREAT_CODE: EXFIL-007 — SECRETS_BULK_ACCESS
  Definition: Actor accesses > 5 distinct secret paths in a single session
              outside declared maintenance window
  Severity:   P0 for DEVOPS_ENGINEER accessing production secrets
              P1 for any other role
  Hard rule:  Yes for production environment
```

### 8C — Privilege Abuse Patterns

```
THREAT_CODE: PRIV-001 — OFF_HOURS_HIGH_PRIVILEGE_OPS
  Definition: Actor executes HIGH or CRITICAL privilege operations
              (emergency override, role assignment, direct DB, K8s exec)
              during hours outside their baseline active window
              AND outside declared maintenance/incident window
  Severity:   P1
  Hard rule:  No (baseline-dependent)

THREAT_CODE: PRIV-002 — EMERGENCY_OVERRIDE_WITHOUT_TICKET
  Definition: PLATFORM_SUPER_ADMIN uses emergency override tool
              without a corresponding active incident ticket in INCIDENT_MANAGEMENT_AGENT
  Severity:   P0
  Hard rule:  Yes — always P0

THREAT_CODE: PRIV-003 — ROLE_ASSIGNMENT_SELF_ESCALATION
  Definition: Any actor modifies their own role or adds themselves
              to a privilege group
  Severity:   P0
  Hard rule:  Yes — always P0, immediate session review flag

THREAT_CODE: PRIV-004 — ROLE_ASSIGNMENT_BULK_CHANGE
  Definition: Actor creates/modifies > 10 role assignments in a 1-hour window
              outside declared onboarding batch
  Severity:   P1
  Hard rule:  No (volume-based)

THREAT_CODE: PRIV-005 — MFA_BYPASS_ATTEMPT
  Definition: Session proceeds on a high-privilege operation
              with mfa_verified = false
  Severity:   P0
  Hard rule:  Yes — always P0

THREAT_CODE: PRIV-006 — INTERN_PRODUCTION_ACCESS
  Definition: INTERN_DEVELOPER actor_type accesses any production environment signal
  Severity:   P0
  Hard rule:  Yes — always P0, immediate session review flag

THREAT_CODE: PRIV-007 — COMPLIANCE_TOOL_ABUSE
  Definition: Compliance officer executes right-to-forget operations
              at > 10/hour without batch approval ticket
  Severity:   P1
  Hard rule:  Yes
```

### 8D — Account Compromise Indicators

```
THREAT_CODE: COMP-001 — IMPOSSIBLE_TRAVEL
  Definition: Two login events for the same actor_id where the IP geographic
              distance implies travel that is physically impossible in the
              time between events
              (computed from ip_address_hash → geo-region mapping — no raw IPs stored)
  Severity:   P0 (if second login proceeds to high-privilege ops)
              P1 (if second login is standard access)
  Hard rule:  Yes

THREAT_CODE: COMP-002 — CREDENTIAL_STUFFING_PATTERN
  Definition: > 5 failed auth events for actor_id within 10 minutes
              followed by successful authentication
  Severity:   P1
  Hard rule:  Yes

THREAT_CODE: COMP-003 — NEW_DEVICE_HIGH_PRIVILEGE
  Definition: Login from device_fingerprint_hash not seen in last 30 days
              followed within 30 minutes by HIGH or CRITICAL privilege operation
  Severity:   P1
  Hard rule:  Yes

THREAT_CODE: COMP-004 — SESSION_ANOMALY_PATTERN
  Definition: Session exhibits > 3 concurrent anomaly indicators
              simultaneously (unusual hours + new device + elevated volume + off-pattern entities)
  Severity:   P0
  Hard rule:  No (composite detection)

THREAT_CODE: COMP-005 — CONCURRENT_SESSION_CONFLICT
  Definition: Two active sessions for same actor_id from different
              ip_address_hashes simultaneously, where at least one
              is in production environment
  Severity:   P0
  Hard rule:  Yes — always P0

THREAT_CODE: COMP-006 — POST_PASSWORD_CHANGE_BEHAVIORAL_SHIFT
  Definition: Significant behavioral deviation (risk score spike > 30 points)
              within 24 hours of a password/credential change event
  Severity:   P1 (account may have been transferred to new actor after credential change)
  Hard rule:  No (composite detection)
```

### 8E — Cross-Tenant & Domain Boundary Violations

```
THREAT_CODE: BOUNDARY-001 — CROSS_TENANT_DATA_PROBE
  Definition: Non-SUPER_ADMIN actor accessing entity_ids belonging to
              a tenant other than their registered tenant_id
  Severity:   P0
  Hard rule:  Yes — always P0

THREAT_CODE: BOUNDARY-002 — DOMAIN_TRACK_BOUNDARY_PROBE
  Definition: Actor systematically accessing records across domain_tracks
              they are not registered to manage (Arts/Commerce/Science/Technology/Admin)
  Severity:   P1
  Hard rule:  No (pattern-based)

THREAT_CODE: BOUNDARY-003 — ORPHAN_TENANT_ACCESS
  Definition: Actor accesses data belonging to a tenant that has been
              suspended or deleted (data should be inaccessible)
  Severity:   P0
  Hard rule:  Yes — always P0 (potential data access after deletion)

THREAT_CODE: BOUNDARY-004 — INTER_INSTITUTE_DATA_MIXING
  Definition: TENANT_ADMIN_INSTITUTE accessing records of students
              belonging to a different institution within the same umbrella tenant
  Severity:   P0
  Hard rule:  Yes
```

### 8F — Compliance & Governance Tool Abuse

```
THREAT_CODE: GOV-001 — AUDIT_LOG_DELETION_ATTEMPT
  Definition: Any actor attempting to delete or modify audit log records
  Severity:   P0
  Hard rule:  Yes — always P0, immediate escalation to COMPLIANCE_ADMIN

THREAT_CODE: GOV-002 — MONITORING_DISABLE_ATTEMPT
  Definition: Any actor attempting to disable or modify ITMA, AHWA,
              OBSERVABILITY_AGENT, or AUDIT_LOG_AGENT behavior
  Severity:   P0
  Hard rule:  Yes — always P0, immediate escalation to SECURITY_LEAD

THREAT_CODE: GOV-003 — ITMA_SELF_INVESTIGATION_ATTEMPT
  Definition: Any actor querying ITMA's own alert outputs or evidence packages
              without SECURITY_LEAD or COMPLIANCE_ADMIN role
  Severity:   P1
  Hard rule:  Yes

THREAT_CODE: GOV-004 — MASS_MODERATION_ACTION
  Definition: OPS_CONSOLE_OPERATOR executes > 50 moderation actions in 1 hour
              without an associated batch moderation task ticket
  Severity:   P1
  Hard rule:  No (volume-based)

THREAT_CODE: GOV-005 — REPUTATION_SCORE_TAMPERING
  Definition: Any actor directly modifying reputation_scores table or
              bypassing REPUTATION_SCORING_AGENT for score updates
  Severity:   P0
  Hard rule:  Yes

THREAT_CODE: GOV-006 — WHITELIST_SELF_ADDITION
  Definition: Any actor adding themselves to an IP allowlist, approved
              destinations registry, or bypass list
  Severity:   P0
  Hard rule:  Yes — always P0
```

### 8G — Service Account Threat Patterns

```
THREAT_CODE: SVC-001 — SCOPE_BOUNDARY_VIOLATION
  Definition: PRIVILEGED_SERVICE_ACCOUNT calling API endpoints not in
              its registered scope declaration
  Severity:   P1 (1–5 unknown endpoints), P0 (> 5 or critical endpoints)
  Hard rule:  Yes for P0 threshold

THREAT_CODE: SVC-002 — VOLUME_SURGE_ANOMALY
  Definition: PRIVILEGED_SERVICE_ACCOUNT data consumption
              > 3x declared normal range
  Severity:   P1
  Hard rule:  No (baseline-based)

THREAT_CODE: SVC-003 — ROGUE_SERVICE_ACCOUNT_BEHAVIOR
  Definition: Service account exhibiting behavioral pattern matching
              reconnaissance or exfiltration patterns (see 8A, 8B)
  Severity:   P0
  Hard rule:  Composite — triggers on pattern match

THREAT_CODE: SVC-004 — CREDENTIAL_AGE_ABUSE
  Definition: Service account operating with credentials past their
              registered rotation deadline (detected via credential age metadata)
  Severity:   P2
  Hard rule:  No (age-based flag)
```

---

## 9️⃣ INSIDER THREAT RISK SCORING MODEL

### Risk Score Components

```
RISK_SCORE_RANGE:      0 (clean) → 100 (critical)
UPDATE_FREQUENCY:      Real-time on every signal ingestion
PERSISTENCE:           Redis (current score, TTL 24h), PostgreSQL (daily snapshots)
ACTOR_SCOPE:           Every registered internal actor has an active risk score

RISK_TIER_THRESHOLDS:
  LOW:       0–24     (baseline normal behavior)
  ELEVATED:  25–49    (notable deviations — review recommended)
  HIGH:      50–74    (significant anomalies — investigation required)
  CRITICAL:  75–100   (imminent threat indicators — immediate action required)

SCORE_COMPONENT_WEIGHTS:
  Baseline deviation contribution:   40% of score
  Pattern match contribution:        35% of score
  Hard rule trigger history:         15% of score
  Composite anomaly indicators:      10% of score

SCORE_DECAY:
  If no anomaly signals in 24 hours:  score decays 10% toward baseline
  If no anomaly signals in 7 days:    score decays 50% toward baseline
  If no anomaly signals in 30 days:   score resets to 0
  Score NEVER decays automatically if an active P0 or P1 alert is open
  Score decay HALTED while investigation is in progress

ACTOR_ONBOARDING_PERIOD:
  First 14 days: Risk score computed but NOT used for alerts
  (Baseline establishment period — signals collected, no false positives)
  After 14 days: Full alerting activated

SCORE_ESCALATION_RULES:
  Score crosses 50 → P2 alert + recommend review
  Score crosses 75 → P1 alert + mandatory investigation
  Score crosses 90 → P0 alert + immediate escalation to SECURITY_LEAD
  Any P0 hard rule trigger → Score immediately set to 90+
  Score recalculated after P0 resolution — does NOT auto-reset below 50
  without SECURITY_LEAD manual clearance
```

---

## 🔟 EVIDENCE PACKAGE SPECIFICATION

Every P0 and P1 alert generates an immutable forensic evidence package. P2 and P3 alerts generate a summary record only.

```
EVIDENCE_PACKAGE_CONTENTS:

1. ACTOR PROFILE SNAPSHOT (at time of incident):
   - actor_id_hash (SHA-256 — no raw actor_id)
   - actor_type
   - actor_registered_role
   - actor_tenant_id
   - actor_onboarding_date
   - actor_risk_score_history (last 30 days — anonymized)
   - actor_baseline_summary (behavioral baseline at incident time)

2. INCIDENT TIMELINE:
   - signal_id list (all signals contributing to alert)
   - Chronological event sequence (timestamps, action_types, entity_types)
   - NO entity_ids in plain text (all hashed for privacy)
   - NO PII content in any field
   - Deviation indicators per event

3. DETECTION RATIONALE:
   - Threat code(s) triggered
   - Specific thresholds breached (observed value vs baseline)
   - Pattern matches identified
   - Hard rules triggered (if any)
   - ML model risk score at time of alert

4. BEHAVIORAL CONTEXT:
   - 24-hour behavioral timeline preceding alert
   - Comparison to 30-day baseline
   - Comparable actor_type peer comparison (anonymized aggregate)

5. CHAIN OF CUSTODY:
   - Package generation timestamp
   - ITMA instance ID that generated the package
   - ITMA version
   - Package hash (SHA-256 of entire package — tamper detection)
   - Immutable storage reference (WORM object store path)

STORAGE:  WORM-compliant object storage
RETENTION: Minimum 7 years (legal/regulatory compliance)
ACCESS:    SECURITY_LEAD + COMPLIANCE_ADMIN only
           Every evidence package read is logged in AUDIT_LOG_AGENT
MODIFICATION: ABSOLUTELY FORBIDDEN (WORM guarantee)
```

---

## 1️⃣1️⃣ SECURITY ENFORCEMENT

```
SECURITY_MODEL = Zero-Trust Internal (actors are not trusted by default
                 even with valid credentials)

ITMA SELF-SECURITY:
  ✅ ITMA's own configuration is immutable (sealed specification)
  ✅ ITMA's alert outputs are classified RESTRICTED — not accessible
     by the actors ITMA monitors
  ✅ ITMA's Kafka topic (itma.signals.raw) is write-only from signal sources
     — no actor can read or replay signals
  ✅ ITMA's evidence packages stored in a separate WORM bucket inaccessible
     to any ops/admin actor except SECURITY_LEAD and COMPLIANCE_ADMIN
  ✅ ITMA itself is monitored by AGENT_HEALTH_WATCHDOG_AGENT (AHWA)
  ✅ Any attempt to disable ITMA = P0 + immediate SECURITY_LEAD escalation
     via independent out-of-band channel (not through Kafka — ITMA dead = escalate anyway)
  ✅ ITMA never logs actor_id in plain text — always SHA-256 hashed
  ✅ ITMA never stores IP addresses — only ip_address_hash (SHA-256)
  ✅ ITMA never accesses user PII content — metadata and behavioral signals only

INPUT SIGNAL SECURITY:
  ✅ mTLS on all signal sources
  ✅ TLS 1.3 minimum
  ✅ Signal source must be a registered system service (not user-space)
  ✅ PII scan on all incoming signals — reject and escalate if PII detected
  ✅ No actor may emit signals about other actors to ITMA
     (signals come from system services only — never from actors)

OUTPUT SECURITY:
  ✅ Alert outputs encrypted at rest (AES-256)
  ✅ Alert outputs accessible only to SECURITY_LEAD + COMPLIANCE_ADMIN roles
  ✅ Every alert read is logged in AUDIT_LOG_AGENT (creating an audit trail
     of who reviewed insider threat alerts)
  ✅ No alert content reaches notification systems, user-facing APIs,
     or tenant-level dashboards

FORBIDDEN:
  ❌ No actor may read ITMA alerts without SECURITY_LEAD or COMPLIANCE_ADMIN role
  ❌ No ITMA alert is ever forwarded to the actor being monitored
  ❌ No raw actor_id in any ITMA output (hashed only)
  ❌ No raw IP addresses in any ITMA signal, log, or output
  ❌ No ITMA alert exposed to external APIs or user-facing surfaces
  ❌ No ITMA configuration modifiable by any ops actor
  ❌ No ITMA monitoring scope reducible by any internal actor
  ❌ No suppression of P0 alerts under any circumstances
  ❌ No evidence package deletable by any actor (WORM enforcement)
  ❌ No cross-tenant alert disclosure (Tenant A findings never reach Tenant B)
```

---

## 1️⃣2️⃣ AUDIT & TRACEABILITY

Every ITMA evaluation cycle produces an immutable audit entry. ITMA's own audit entries are also classified RESTRICTED.

```json
AUDIT_LOG_SCHEMA: {
  "audit_id":                        "UUID (immutable, system-generated)",
  "timestamp_utc":                   "ISO-8601 UTC",
  "itma_instance_id":                "ITMA pod instance ID",
  "signal_id":                       "UUID (reference)",
  "actor_id_hash":                   "SHA-256 of actor_id",
  "actor_type":                      "string",
  "tenant_id":                       "string",
  "environment":                     "string",
  "signal_type":                     "string",
  "input_hash":                      "SHA-256 of raw signal payload",
  "output_hash":                     "SHA-256 of output alert (if generated)",
  "detection_engine_version":        "ITMA-DETECTION-ENGINE-v{x}.{y}.{z}",
  "ml_model_version":                "ML-RISK-SCORER-v{x}.{y}.{z} | NOT_INVOKED",
  "threat_codes_triggered":          ["array of THREAT_CODE strings"],
  "hard_rules_triggered":            ["array of hard rule codes"],
  "risk_score_before":               "integer 0–100",
  "risk_score_after":                "integer 0–100",
  "alert_generated":                 "boolean",
  "alert_severity":                  "P0 | P1 | P2 | P3 | NONE",
  "evidence_package_id":             "UUID | null",
  "confidence_score":                "float 0.00–1.00",
  "anomaly_flags":                   ["array"],
  "escalation_emitted":              "boolean",
  "processing_time_ms":              "integer",
  "classification":                  "RESTRICTED"
}
```

**Storage:** Append-only Kafka topic `audit.log.stream` (classified partition) → WORM object storage. Retention: minimum 7 years. Access: SECURITY_LEAD + COMPLIANCE_ADMIN only.

---

## 1️⃣3️⃣ FAILURE POLICY

No silent failures. No suppressed alerts. No partial processing.

```
FAILURE SCENARIOS AND RESPONSES:

[F-01] AUDIT_LOG_AGENT UNAVAILABLE (primary signal source):
  → IMMEDIATE P0 ESCALATION (audit trail broken = cannot monitor insider activity)
  → LOG locally (in-memory buffer, max 30 min TTL)
  → ESCALATE_TO = SECURITY_LEAD + INFRA_OPS_TEAM via out-of-band channel
  → RETRY_POLICY = Reconnect every 10s (max 10 attempts)
  → After 10 failures: DECLARE MONITORING_GAP_INCIDENT + SECURITY_LEAD notification
  → NEVER claim monitoring continuity during an audit source outage

[F-02] SIGNAL FROM UNREGISTERED SOURCE:
  → REJECT signal
  → LOG_UNREGISTERED_SOURCE (security incident — could be signal spoofing attempt)
  → ESCALATE_TO = SECURITY_AGENT
  → RETRY_POLICY = NONE

[F-03] ACTOR_ID NOT IN ACTOR_REGISTRY:
  → REJECT signal
  → LOG_UNKNOWN_ACTOR (security incident — could be orphan/ghost account)
  → ESCALATE_TO = SECURITY_AGENT + IDENTITY_TEAM
  → RETRY_POLICY = NONE — must be resolved by identity team

[F-04] HARD RULE TRIGGERED (any threat code marked "Hard rule: Yes"):
  → EMIT ALERT regardless of ML confidence or baseline availability
  → Hard rules operate independently of ML model availability
  → Hard rules are never suppressed by confidence threshold
  → EVIDENCE PACKAGE generated immediately
  → ESCALATE based on declared severity (P0 rules: < 1 second target)

[F-05] ML RISK SCORING MODEL UNAVAILABLE:
  → FALLBACK to rules-engine + baseline deviation only
  → Hard rules continue operating (unaffected)
  → FLAG: ml_model_version = NOT_INVOKED in all outputs
  → LOG_ML_UNAVAILABLE
  → ESCALATE_TO = ML_OPS_TEAM
  → RETRY_POLICY = Retry every 60s (max 3 attempts), then rules-only mode

[F-06] ACTOR IN ONBOARDING PERIOD (< 14 days):
  → Collect signals → Update risk score → DO NOT emit alerts
  → Hard rules STILL active during onboarding period
  → Log all signals for post-onboarding baseline use

[F-07] EVIDENCE PACKAGE WRITE FAILURE:
  → HALT alert emission for that incident (alert without evidence is incomplete)
  → LOG P0 STORAGE FAILURE
  → ESCALATE_TO = SECURITY_LEAD + INFRA_OPS_TEAM
  → Buffer evidence in encrypted memory (max 15 min TTL)
  → RETRY_POLICY = Exponential backoff (5s, 15s, 30s), max 5 retries
  → After 5 failures: emit alert WITHOUT package, flag: evidence_package_id = PENDING_STORAGE

[F-08] CONFIDENCE BELOW THRESHOLD (< 0.60) for NON-HARD-RULE alerts:
  → Downgrade severity by one level (P0→P1, P1→P2, P2→P3)
  → Flag output: low_confidence = true
  → Still emit alert (downgraded severity)
  → NOTE: Hard rules are NEVER downgraded by confidence threshold

[F-09] ITMA SELF: KAFKA BROKER UNAVAILABLE:
  → Buffer signals in-memory (max 10,000, 5 min TTL, encrypted)
  → LOG_KAFKA_UNAVAILABLE
  → ESCALATE_TO = INFRA_OPS_TEAM via out-of-band channel
  → RETRY_POLICY = Reconnect every 10s, flush buffer on reconnect
  → Hard-rule triggers: held in non-evictable priority buffer during outage

[F-10] P0 ALERT EMISSION FAILURE (escalation channel unavailable):
  → Attempt secondary out-of-band channel (email / SMS to SECURITY_LEAD)
  → Log failure with full evidence package locally
  → DECLARE ESCALATION_CHANNEL_FAILURE_INCIDENT
  → RETRY_POLICY = Retry escalation every 30s until delivered
  → P0 alerts are NEVER silently dropped

[F-11] ATTEMPT TO DISABLE OR MODIFY ITMA:
  → REJECT modification attempt
  → EMIT P0_SECURITY_VIOLATION immediately
  → LOG with full actor context
  → ESCALATE_TO = SECURITY_LEAD via out-of-band channel (bypassing Kafka
                  in case Kafka itself is the vector)
  → RETRY_POLICY = NONE — requires SECURITY_LEAD manual clearance
```

---

## 1️⃣4️⃣ INTER-AGENT DEPENDENCY MAP

```
UPSTREAM_AGENTS (mandatory):
  - AUDIT_LOG_AGENT
  - API_GATEWAY_SERVICE
  - AUTHORIZATION_SERVICE
  - IDENTITY_SERVICE
  - TENANT_MANAGEMENT_SERVICE
  - AGENT_REGISTRY_SERVICE

UPSTREAM_AGENTS (optional):
  - DATA_EXPORT_SERVICE
  - SECRETS_VAULT_AUDIT
  - KUBERNETES_API_AUDIT
  - CI_CD_PIPELINE_AUDIT
  - DB_POOL_MANAGER_AUDIT
  - OPS_CONSOLE_SERVICE

DOWNSTREAM_AGENTS (primary):
  - SECURITY_AGENT                    (receives all P0/P1 alerts)
  - GOVERNANCE_AGENT                  (receives compliance violation alerts)
  - COMPLIANCE_AGENT                  (receives audit evidence packages)
  - INCIDENT_MANAGEMENT_AGENT         (receives P0/P1 escalation triggers)
  - AUDIT_LOG_AGENT                   (receives ITMA's own audit entries)

DOWNSTREAM_AGENTS (secondary):
  - OBSERVABILITY_AGENT               (receives ITMA health metrics ONLY —
                                       never alert content)
  - FEATURE_STORE_AGENT               (anonymized platform risk posture vectors only)

EVENTS_EMITTED:
  - RISK_SCORE_UPDATED
  - INSIDER_THREAT_ALERT_P0           (< 1 second target — priority channel)
  - INSIDER_THREAT_ALERT_P1
  - INSIDER_THREAT_ALERT_P2
  - INSIDER_THREAT_ALERT_P3
  - EVIDENCE_PACKAGE_CREATED
  - EVIDENCE_PACKAGE_STORAGE_FAILURE
  - MONITORING_GAP_DECLARED           (when audit signal source unavailable)
  - MONITORING_GAP_RESOLVED
  - ACTOR_ONBOARDING_COMPLETE         (14-day baseline period complete)
  - ITMA_SELF_DISABLE_ATTEMPT_DETECTED
  - ESCALATION_CHANNEL_FAILURE
  - BASELINE_RECOMPUTED               (weekly per actor)
  - THREAT_PATTERN_MODEL_UPDATED      (on ML model version update)
```

---

## 1️⃣5️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```json
EMIT_FEATURE_VECTOR: {
  "user_id":        null,
  "entity_type":    "PLATFORM_RISK_POSTURE",
  "entity_id":      "PLATFORM_AGGREGATE",
  "feature_name":   "string (e.g., active_high_risk_actors_count,
                     p0_alerts_last_24h, monitoring_gap_minutes_last_7d,
                     mean_actor_risk_score, critical_tier_actor_count)",
  "feature_value":  "float",
  "timestamp":      "ISO-8601 UTC",
  "source_agent":   "INSIDER_THREAT_MONITOR_AGENT",
  "domain_track":   "PLATFORM_WIDE"
}
```

**Strict rules:**
- NO individual actor risk scores in feature vectors
- NO actor identifiers (hashed or otherwise) in feature vectors
- ONLY platform-aggregate security posture metrics
- These vectors are used for platform health dashboards and board-level risk reporting only

---

## 1️⃣6️⃣ PERFORMANCE MONITORING

```
METRICS_EMITTED (Prometheus — to OBSERVABILITY_AGENT):
  NOTE: Metric labels contain NO actor identifiers or threat details.
        Only operational performance metrics visible to observability stack.

  itma_signals_received_total           (counter, labels: signal_type, environment)
  itma_signals_rejected_total           (counter, labels: rejection_reason)
  itma_evaluations_completed_total      (counter)
  itma_risk_score_updates_total         (counter, labels: risk_tier)
  itma_alerts_emitted_total             (counter, labels: severity)
  itma_hard_rules_triggered_total       (counter)
  itma_evidence_packages_created_total  (counter)
  itma_evidence_package_failures_total  (counter)
  itma_processing_latency_ms            (histogram: p50, p95, p99)
  itma_p0_alert_latency_ms              (histogram: P0 emission latency)
  itma_actors_monitored_gauge           (gauge, labels: actor_type)
  itma_actors_by_risk_tier_gauge        (gauge, labels: risk_tier)
  itma_ml_model_invocations_total       (counter, labels: model_version)
  itma_baseline_recompute_total         (counter)
  itma_monitoring_gap_seconds_total     (counter — tracks total monitoring gap time)

SUCCESS_RATE_TARGET    = > 99.99% signal processing (security-critical)
P0_ALERT_LATENCY_SLA   = P99 < 1,000ms (< 1 second from signal ingestion)
PROCESSING_LATENCY_SLA = P99 < 100ms (signal ingestion)
                         P99 < 500ms (risk score update)

INTEGRATES_WITH:
  - OBSERVABILITY_AGENT (Prometheus + Grafana — operational metrics only)
  - Alertmanager (PagerDuty / Opsgenie — OPERATIONAL health alerts, not threat alerts)
  - Jaeger (distributed tracing — 100% sampling in all environments)
  - Encrypted audit log stream → WORM storage
```

---

## 1️⃣7️⃣ WEEKLY INSIDER THREAT POSTURE REPORT

ITMA generates a weekly aggregated posture report for SECURITY_LEAD review.

```
REPORT_SCHEDULE:       Every Monday 06:00 UTC
RECIPIENTS:            SECURITY_LEAD + COMPLIANCE_ADMIN ONLY
ACCESS:                RESTRICTED classification
DELIVERY:              Encrypted, via COMPLIANCE_AGENT report channel
                       NOT via standard notification system

REPORT_CONTENTS:
  - Total actors monitored per actor_type (no names)
  - Risk tier distribution (count per tier)
  - Alert count by severity and threat category (no actor identifiers)
  - Top 5 threat codes by frequency (no actor identifiers)
  - Monitoring gap summary (total minutes, cause)
  - Actors in CRITICAL tier (count only — SECURITY_LEAD requests detail separately)
  - Actor churn in last 7 days (new registrations, deregistrations)
  - Baseline stability summary (actors with high baseline volatility — count only)
  - Open P0/P1 investigations summary (count, age)
  - False positive summary (alerts closed without investigation — count only)
  - Recommended configuration tuning (threshold adjustments if warranted)
  - Evidence package storage utilization

REPORT_FORMAT:  Encrypted PDF + machine-readable JSON summary
IMMUTABILITY:   Reports are archived immediately on generation
                They cannot be modified after generation
```

---

## 1️⃣8️⃣ VERSIONING POLICY

```
VERSIONING_RULES:
  ✅ All changes: ADD-ONLY
  ✅ Every change: Semantic version bump
  ✅ All threat pattern additions: ADD-ONLY (existing patterns never removed)
  ✅ All threshold changes: Require SECURITY_LEAD approval + documented rationale
  ✅ All ML model updates: Require SECURITY_LEAD approval + diff analysis
  ✅ New actor_type additions: Require governance approval + behavioral contract definition
  ✅ Rollback: Safe to N-1 guaranteed
  ✅ Baseline history: Retained minimum 2 years per actor (post-offboarding)
  ✅ Evidence packages: WORM — no version can modify archived packages

CURRENT_VERSION: ITMA-v1.0.0
THREAT_PATTERN_CATALOG_VERSION: THREAT-CATALOG-v1.0.0

VERSION_HISTORY:
  v1.0.0 — Initial sealed specification (2025)
```

---

## 1️⃣9️⃣ DEPLOYMENT REQUIREMENTS

```
RUNTIME:               Kubernetes (containerized, HPA)
REPLICA_MIN:           3 (production — for high availability of security-critical agent)
                       2 (staging), 1 (test/dev)
REPLICA_MAX:           15 (auto-scale on signal ingestion volume)
RESOURCE_REQUESTS:     CPU: 500m, Memory: 1Gi (per replica)
RESOURCE_LIMITS:       CPU: 2000m, Memory: 4Gi (per replica)
                       (Baseline computation and ML scoring are memory-intensive)

HEALTH_CHECKS:
  Liveness:            /health/live (HTTP 200 — accessible only from cluster)
  Readiness:           /health/ready (HTTP 200 — audit signal sources connected)
  Startup:             60s grace period (baseline loading from PostgreSQL on startup)

NETWORK_ISOLATION:
  ITMA runs in a DEDICATED Kubernetes namespace: ecoskiller-security
  Network policy: ITMA may only receive signals from registered source services
  ITMA outputs: accessible only from SECURITY_AGENT, GOVERNANCE_AGENT,
                COMPLIANCE_AGENT, INCIDENT_MANAGEMENT_AGENT, AUDIT_LOG_AGENT
  NO direct external network access from ITMA namespace
  NO ingress from user-space namespaces

CONFIG_MANAGEMENT:     Kubernetes ConfigMap + Vault Secrets
                       Configuration is IMMUTABLE after deployment
                       Any config change requires new deployment + version bump
SECRETS:               HashiCorp Vault (accessed via dedicated ITMA AppRole only)
SERVICE_MESH:          Istio (mTLS — ITMA rejects all non-mTLS connections)
TRACING:               Jaeger (100% sampling always — security audit requirement)
EVIDENCE_STORAGE:      Dedicated WORM-compliant object storage bucket
                       (isolated from all other storage — ITMA access only)
BASELINE_STORAGE:      Dedicated PostgreSQL schema (isolated, no shared access)
RISK_SCORE_CACHE:      Dedicated Redis keyspace (no shared keyspace with other agents)
```

---

## 2️⃣0️⃣ NON-NEGOTIABLE ABSOLUTE RULES

```
❌ NEVER suppress a P0 alert under any circumstances
❌ NEVER suppress a hard-rule-triggered alert regardless of confidence score
❌ NEVER expose alert content to the actor being monitored
❌ NEVER expose alert content to any non-SECURITY_LEAD / non-COMPLIANCE_ADMIN role
❌ NEVER store raw actor_id in any output (SHA-256 hash only)
❌ NEVER store raw IP addresses in any signal, log, or output (hash only)
❌ NEVER access user PII content — behavioral metadata only
❌ NEVER generate an alert without a corresponding audit log entry
❌ NEVER allow evidence packages to be modified or deleted (WORM guarantee)
❌ NEVER allow monitoring scope to be reduced by any internal actor
❌ NEVER auto-terminate a session or restrict an actor — detect + escalate only
❌ NEVER allow ITMA's own configuration to be modified outside a sealed deployment
❌ NEVER disclose that a specific actor is under investigation to any other actor
❌ NEVER cross-disclose findings between tenants
❌ NEVER emit a monitoring continuity claim during an audit source outage
❌ NEVER allow AI/LLM in any real-time detection, scoring, or escalation path
❌ NEVER create hidden detection logic outside this specification
❌ NEVER modify historical baseline records (immutable)
❌ NEVER allow false positive suppression without SECURITY_LEAD written approval
❌ NEVER use the actor's own signals to bypass their own monitoring
   (an actor cannot "trick" ITMA by manipulating their own signal stream)
❌ NEVER allow ITMA to be used to target actors based on identity characteristics
   (monitoring is behavioral, not profiling-based — no demographic targeting)
```

---

## 🔐 AGENT SEAL DECLARATION

```
╔══════════════════════════════════════════════════════════════════════════╗
║         INSIDER_THREAT_MONITOR_AGENT — SEALED                           ║
║         Platform: Ecoskiller Antigravity                                ║
║         Version:  ITMA-v1.0.0                                           ║
║         Threat Catalog Version: THREAT-CATALOG-v1.0.0                  ║
║         Status:   FINAL · LOCKED · GOVERNED · CLASSIFIED               ║
║         Clearance: SECURITY_LEAD + COMPLIANCE_ADMIN only               ║
║                                                                          ║
║  This agent monitors internal actors — not external threats.            ║
║  It operates on behavioral metadata only — never on user content.      ║
║  It detects, alerts, and escalates — never auto-remediates.            ║
║  Its findings are classified and never disclosed to monitored actors.  ║
║  It cannot be disabled, modified, or have its scope reduced            ║
║  by any internal actor without triggering a P0 security alert.         ║
║                                                                          ║
║  Behavioral monitoring is not surveillance of identity.                 ║
║  This agent monitors what actors DO with access, not who they are.     ║
║                                                                          ║
║  This specification is COMPLETE and SEALED.                             ║
║  No interpretation beyond declared scope.                               ║
║  No assumption filling.                                                 ║
║  No creative deviation.                                                 ║
║  No mutation without version bump + SECURITY_LEAD + governance approval.║
║                                                                          ║
║  Any deviation from this specification =                                ║
║  STOP EXECUTION + SECURITY_LEAD + GOVERNANCE ESCALATION                 ║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

*Document generated under Ecoskiller Antigravity Master Agent Creation Framework. All sections comply with the Master Agent Creation Prompt v1.0, Ecoskiller Master Execution Prompt v12.0, R10 (Security Policies), R39 (Core Inbuilt Platform Tools), R40 (Admin & Ops Console Law), and R51 (Anti-Spam & Abuse Prevention Law). Mutation policy: Add-only. Authority: Human declaration only. Classification: RESTRICTED.*
