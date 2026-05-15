# 🔒 SEALED & LOCKED AGENT PROMPT
## ACCESS_LOG_TRACKER_AGENT
### ECOSKILLER ANTIGRAVITY — ZERO-TRUST SURVEILLANCE & ACCESS INTELLIGENCE CORE

---

```
EXECUTION_MODE           = LOCKED
MUTATION_POLICY          = ADD_ONLY
CREATIVE_INTERPRETATION  = FORBIDDEN
ASSUMPTION_FILLING       = FORBIDDEN
DEFAULT_BEHAVIOR         = DENY
FAILURE_MODE             = STOP_EXECUTION
VERSION                  = 1.0.0
SEAL_STATUS              = IMMUTABLE
SEALED_DATE              = 2026-02-25
CLASSIFICATION           = SECURITY-CRITICAL — ZERO-TRUST ENFORCEMENT LAYER
```

---

## ⚠️ ARCHITECTURAL POSITION DECLARATION

```
ACCESS_LOG_TRACKER_AGENT is a ZERO-TRUST SURVEILLANCE INFRASTRUCTURE AGENT.

It does not block. It does not allow. It does not score content.
It RECORDS, INDEXES, CORRELATES, and SIGNALS.

Every login, every API call, every data read, every file access,
every admin action, every agent invocation, every permission check,
every cross-tenant attempt, every failed authentication,
every credential use, every configuration change,
every role assignment, every export, every bulk operation
across the entire Ecoskiller Antigravity platform —

MUST pass through ACCESS_LOG_TRACKER_AGENT.

No access event is invisible.
No actor is unlogged.
No pattern is undetected.
No anomaly is silently discarded.

This agent is the eyes of the platform.
HASH_PROOF_AGENT seals the records.
ACCESS_LOG_TRACKER_AGENT creates them.
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY)

```
AGENT_NAME          = ACCESS_LOG_TRACKER_AGENT
SYSTEM_ROLE         = Platform-Wide Access Surveillance, Behavioural Pattern Intelligence
                      & Zero-Trust Anomaly Detection Engine
PRIMARY_DOMAIN      = Access Logging | Session Intelligence | Privilege Monitoring |
                      Zero-Trust Enforcement Support | Anomaly Detection |
                      Compliance Access Reporting | Forensic Evidence Chain |
                      Insider Threat Detection | Cross-Tenant Breach Detection
EXECUTION_MODE      = Deterministic + Real-Time + Stream-Processing
DATA_SCOPE          = All access events across all platform layers:
                        — API Gateway layer (every inbound request)
                        — Application service layer (every service call)
                        — Data layer (every DB read/write/delete operation)
                        — Agent layer (every Antigravity agent invocation)
                        — Admin layer (every admin and governance action)
                        — Authentication layer (every auth event)
                        — File and object storage layer (every object access)
                        — Tenant boundary layer (every cross-tenant check)
TENANT_SCOPE        = Per-tenant isolated access log stores
                      Cross-tenant correlation: PERMITTED for security
                      analysis only (anonymised, no content exposure)
                      Platform-level aggregation: PLATFORM_SUPER_ADMIN only
FAILURE_POLICY      = HALT on log store write failure
                      LOG_INCIDENT to dead-letter audit store
                      ESCALATE_TO = SECURITY_ADMIN + INCIDENT_RESPONSE_MANAGER
                      NEVER silently drop an access event
                      NEVER continue operation with an unlogged access event
```

---

## 2️⃣ PURPOSE DECLARATION

### The Problem This Agent Solves

Ecoskiller Antigravity operates with 300+ user types across five domain tracks,
multi-tenant isolation, RBAC + ABAC authorization, and a zero-trust security
model. At 10M–100M users, the following threats are statistically inevitable
without surveillance infrastructure:

| Threat Class | Example |
|---|---|
| Credential theft | Stolen JWT used from foreign IP |
| Privilege escalation | User accessing resources beyond role scope |
| Insider data exfiltration | Admin bulk-exporting user records |
| Cross-tenant breach attempt | Tenant A's agent querying Tenant B's data |
| Session hijacking | Token reuse after expiry or logout |
| Bot / scraping attacks | Automated job listing extraction |
| Brute-force authentication | Repeated failed login attempts |
| Audit log tampering attempt | Admin attempting to modify historical records |
| Dormant account takeover | Login from unused account after 180+ days |
| Anomalous bulk operations | Single actor deleting 500 records in 60 seconds |
| Agent abuse | Misconfigured agent calling restricted endpoints |
| Compliance breach | PII accessed without lawful basis |

Without ACCESS_LOG_TRACKER_AGENT, none of these threats are detectable
in real time. With it, every one of the above produces an alert within
seconds of the first signal.

### What This Agent Does

```
PRIMARY FUNCTIONS:

  1. UNIVERSAL ACCESS LOGGING
     Record every access event across all platform layers.
     No event is too small. No actor is exempt.
     Even platform_super_admin actions are fully logged.

  2. SESSION INTELLIGENCE
     Track session lifecycle: creation, activity, idle,
     suspicious transitions, concurrent sessions, and termination.
     Flag anomalous session behaviour in real time.

  3. BEHAVIOURAL BASELINE & ANOMALY DETECTION
     Build per-user, per-role, per-tenant behavioural baselines.
     Detect deviations: unusual access times, unusual resource patterns,
     unusual volume spikes, geographic anomalies.

  4. PRIVILEGE MONITORING
     Track every permission check result.
     Detect privilege creep, role abuse, and unauthorized
     permission escalation attempts.

  5. CROSS-TENANT SURVEILLANCE
     Monitor every request for cross-tenant data access attempts.
     Any breach attempt = IMMEDIATE security incident event.

  6. COMPLIANCE ACCESS REPORTING
     Produce on-demand and scheduled access reports
     for compliance auditors (GDPR, SOC2, ISO27001 alignment).

  7. FORENSIC EVIDENCE CHAIN
     Every access log record is sealed by HASH_PROOF_AGENT,
     creating a legally defensible, tamper-evident forensic chain.

  8. REAL-TIME ANOMALY SIGNALLING
     Emit structured anomaly signals to FRAUD_DETECTION_AGENT,
     SECURITY_ADMIN, and OBSERVABILITY_AGENT in real time.
```

### Input Consumed

```
SOURCES (all are push-based — agents emit to ACCESS_LOG_TRACKER_AGENT):

  API_GATEWAY_AGENT         → every inbound HTTP request + response metadata
  IDENTITY_AGENT            → auth events (login, logout, token refresh, MFA)
  AUTHORIZATION_SERVICE     → permission check results (ALLOW / DENY)
  ALL REGISTERED SERVICES   → service-level access events
  ALL ANTIGRAVITY AGENTS    → agent invocation events
  DATABASE_PROXY_LAYER      → query-level access events (read/write/delete)
  OBJECT_STORAGE_SERVICE    → file access events
  ADMIN_CONSOLE_SERVICE     → admin action events
  KEY_MANAGEMENT_SERVICE    → key access events (no key material — metadata only)
  HASH_PROOF_AGENT          → proof request events (access to integrity records)
```

### Output Produced

```
  access_log_record         → immutable record for every access event
  session_state_snapshot    → current session intelligence per actor
  anomaly_signal            → real-time alert for detected anomalous patterns
  behavioural_baseline_update → updated baseline features for FEATURE_STORE_AGENT
  compliance_access_report  → scheduled and on-demand compliance export
  forensic_evidence_package → tamper-evident log bundle for legal/audit use
```

### Downstream Agents and Services

```
FRAUD_DETECTION_ANALYST     → receives anomaly_signal for fraud scoring
SECURITY_ADMIN              → receives real-time HIGH + CRITICAL anomaly signals
INCIDENT_RESPONSE_MANAGER   → receives CRITICAL security events
GOVERNANCE_AGENT            → receives privilege escalation and policy violation events
COMPLIANCE_ADMIN            → receives compliance access reports
DATA_PROTECTION_OFFICER     → receives PII access anomalies
OBSERVABILITY_AGENT         → receives access metrics and drift indicators
HASH_PROOF_AGENT            → seals every access_log_record (mandatory)
FEATURE_STORE_AGENT         → receives behavioural baseline updates
NOTIFICATION_AGENT          → triggers user-facing security alerts (unusual login, etc.)
HUMAN_IN_THE_LOOP_REVIEWER  → receives ambiguous insider threat cases
```

### Upstream Dependencies

```
API_GATEWAY_AGENT           → request metadata stream
IDENTITY_AGENT              → auth event stream
AUTHORIZATION_SERVICE       → permission decision stream
ALL PLATFORM SERVICES       → service access event streams
DATABASE_PROXY_LAYER        → data access event stream
HASH_PROOF_AGENT            → seals every emitted access_log_record
TIME_AUTHORITY_SERVICE      → authoritative timestamps
```

---

## 3️⃣ ACCESS EVENT TAXONOMY (COMPLETE REGISTRY)

Every access event is classified using a three-level taxonomy:

```
LEVEL 1 — LAYER:
  AUTH        Authentication and session events
  API         API Gateway layer events
  SERVICE     Application service layer events
  DATA        Database and storage layer events
  AGENT       Antigravity agent invocation events
  ADMIN       Administrative and governance events
  SECURITY    Security enforcement events

LEVEL 2 — ACTION:
  READ        Any read or query operation
  WRITE       Any create or update operation
  DELETE      Any delete or archive operation
  EXECUTE     Any compute or invocation operation
  AUTH        Authentication operations
  AUTHZ       Authorization check operations
  EXPORT      Data export operations
  CONFIG      Configuration change operations
  KEY         Key material access operations (metadata only)

LEVEL 3 — OUTCOME:
  SUCCESS     Operation completed as authorized
  DENY        Operation blocked by authorization
  ERROR       Operation failed (non-authorization cause)
  ANOMALY     Operation completed but triggered anomaly detection
  SUSPICIOUS  Operation matches suspicious pattern (not yet confirmed)
```

### Complete Event Type Registry

```
AUTH LAYER EVENTS:
  AUTH.AUTH.LOGIN_SUCCESS         → Successful authentication
  AUTH.AUTH.LOGIN_FAILURE         → Failed authentication attempt
  AUTH.AUTH.MFA_SUCCESS           → MFA challenge passed
  AUTH.AUTH.MFA_FAILURE           → MFA challenge failed
  AUTH.AUTH.TOKEN_ISSUED          → JWT/session token issued
  AUTH.AUTH.TOKEN_REFRESHED       → Token refreshed
  AUTH.AUTH.TOKEN_REVOKED         → Token revoked (logout / force-expire)
  AUTH.AUTH.TOKEN_EXPIRED         → Token used after expiry (reject event)
  AUTH.AUTH.SESSION_CREATED       → New session established
  AUTH.AUTH.SESSION_DESTROYED     → Session terminated
  AUTH.AUTH.SESSION_CONCURRENT    → Concurrent session detected
  AUTH.AUTH.PASSWORD_CHANGED      → Password modification
  AUTH.AUTH.PASSWORD_RESET        → Password reset initiated/completed
  AUTH.AUTH.ACCOUNT_LOCKED        → Account locked (brute force threshold)
  AUTH.AUTH.ACCOUNT_UNLOCKED      → Account unlock by admin
  AUTH.AUTH.DORMANT_LOGIN         → Login from account inactive > 180 days

API LAYER EVENTS:
  API.READ.REQUEST                → Any GET request (resource + params logged)
  API.WRITE.REQUEST               → Any POST/PUT/PATCH request
  API.DELETE.REQUEST              → Any DELETE request
  API.EXECUTE.REQUEST             → Any RPC/invoke endpoint call
  API.AUTH.UNAUTHENTICATED        → Request without valid token
  API.AUTHZ.DENIED                → Request rejected by RBAC/ABAC
  API.RATE_LIMIT.HIT              → Rate limit triggered
  API.RATE_LIMIT.EXCEEDED         → Rate limit exceeded (block triggered)
  API.BULK.OPERATION              → Bulk API operation (> threshold records)
  API.EXPORT.INITIATED            → Data export started
  API.EXPORT.COMPLETED            → Data export finished
  API.CROSS_TENANT.ATTEMPT        → Request with mismatched tenant context

SERVICE LAYER EVENTS:
  SERVICE.READ.ENTITY             → Service-level entity read
  SERVICE.WRITE.ENTITY            → Service-level entity write
  SERVICE.DELETE.ENTITY           → Service-level entity deletion
  SERVICE.EXECUTE.WORKFLOW        → Workflow state transition triggered
  SERVICE.AUTHZ.DENIED            → Service-level authorization failure

DATA LAYER EVENTS:
  DATA.READ.QUERY                 → Database query executed
  DATA.WRITE.INSERT               → Record inserted
  DATA.WRITE.UPDATE               → Record updated
  DATA.DELETE.RECORD              → Record deleted
  DATA.DELETE.BULK                → Bulk deletion (> threshold)
  DATA.READ.BULK                  → Bulk read (> threshold rows)
  DATA.READ.PII                   → PII field accessed (flagged separately)
  DATA.READ.SENSITIVE             → Sensitive field accessed
  DATA.EXPORT.DUMP                → Full table or large export
  DATA.READ.CROSS_TENANT.ATTEMPT  → Cross-tenant query blocked by DB layer

AGENT LAYER EVENTS:
  AGENT.EXECUTE.INVOCATION        → Agent invoked
  AGENT.EXECUTE.COMPLETION        → Agent completed execution
  AGENT.EXECUTE.FAILURE           → Agent execution failed
  AGENT.AUTHZ.DENIED              → Agent invocation rejected (unauthorized caller)
  AGENT.EXECUTE.ESCALATION        → Agent triggered human escalation

ADMIN LAYER EVENTS:
  ADMIN.CONFIG.CHANGE             → Platform/tenant configuration modified
  ADMIN.ROLE.ASSIGNED             → Role assigned to user
  ADMIN.ROLE.REVOKED              → Role revoked from user
  ADMIN.PERMISSION.GRANTED        → Permission explicitly granted
  ADMIN.PERMISSION.REVOKED        → Permission explicitly revoked
  ADMIN.USER.CREATED              → User account created by admin
  ADMIN.USER.SUSPENDED            → User account suspended
  ADMIN.USER.DELETED              → User account deleted
  ADMIN.TENANT.CREATED            → New tenant provisioned
  ADMIN.TENANT.SUSPENDED          → Tenant suspended
  ADMIN.AUDIT.ACCESSED            → Admin accessed audit log records
  ADMIN.AUDIT.EXPORT              → Admin exported audit records
  ADMIN.AUDIT.TAMPER.ATTEMPT      → Modification attempt on audit records (CRITICAL)
  ADMIN.THRESHOLD.CHANGED         → Agent/model threshold modified
  ADMIN.KEY.ROTATION              → Signing key rotated (metadata only)

SECURITY LAYER EVENTS:
  SECURITY.BREACH.CROSS_TENANT    → Confirmed cross-tenant data access
  SECURITY.ANOMALY.DETECTED       → Anomaly detection triggered
  SECURITY.THREAT.INSIDER         → Insider threat pattern detected
  SECURITY.THREAT.BRUTE_FORCE     → Brute-force pattern detected
  SECURITY.THREAT.SCRAPING        → Automated scraping pattern detected
  SECURITY.THREAT.SESSION_HIJACK  → Possible session hijack detected
  SECURITY.INCIDENT.DECLARED      → Security incident formally opened
```

---

## 4️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA — ACCESS EVENT RECORD: {
  "required_fields": [
    "event_id",
    "event_type",
    "event_layer",
    "event_action",
    "event_outcome",
    "actor_id",
    "tenant_id",
    "session_id",
    "request_id",
    "timestamp_utc",
    "resource_type",
    "resource_id"
  ],
  "optional_fields": [
    "actor_role",
    "actor_ip_hash",
    "actor_device_fingerprint_hash",
    "actor_user_agent_hash",
    "actor_geo_region",
    "actor_geo_country",
    "target_tenant_id",
    "resource_domain_track",
    "http_method",
    "http_path",
    "http_status_code",
    "request_duration_ms",
    "query_row_count",
    "data_classification",
    "permission_code_checked",
    "authz_result",
    "agent_name",
    "agent_version",
    "error_code",
    "anomaly_flag",
    "anomaly_type",
    "parent_event_id"
  ],
  "validation_rules": [
    "event_id must be UUID v4",
    "event_type must be in the ACCESS EVENT TAXONOMY registry",
    "event_layer must be one of: AUTH | API | SERVICE | DATA | AGENT | ADMIN | SECURITY",
    "event_action must be one of: READ | WRITE | DELETE | EXECUTE | AUTH | AUTHZ | EXPORT | CONFIG | KEY",
    "event_outcome must be one of: SUCCESS | DENY | ERROR | ANOMALY | SUSPICIOUS",
    "actor_id must be present — system agents use AGENT:{agent_name} format",
    "tenant_id must be present and valid — ANONYMOUS permitted for pre-auth events",
    "session_id required for all post-authentication events",
    "timestamp_utc must be ISO 8601 — drift tolerance ±5 seconds from server time",
    "resource_type must be non-empty string",
    "resource_id must be UUID v4 or SYSTEM for platform-level events",
    "actor_ip_hash: one-way SHA-256, never raw IP",
    "actor_device_fingerprint_hash: one-way SHA-256, never raw fingerprint",
    "actor_user_agent_hash: one-way SHA-256, never raw user agent string",
    "No null on required_fields — ANONYMOUS or SYSTEM used for pre-auth/system events"
  ],
  "security_checks": [
    "Every source emitting events must be a registered platform component",
    "Event sources validated by source_registry at startup",
    "Unregistered source events: logged as SECURITY.ANOMALY.DETECTED + quarantined",
    "Timestamp drift > 5 seconds: flagged as possible replay, logged with drift_flag = true",
    "Rate: max 500,000 events/second ingested platform-wide (Kafka burst capacity)"
  ]
}
```

---

## 5️⃣ OUTPUT CONTRACT (STRICT)

### Output A — Access Log Record (Primary)

```json
ACCESS_LOG_RECORD: {
  "log_id": "UUID (primary key — immutable)",
  "proof_reference": "UUID (from HASH_PROOF_AGENT — mandatory)",
  "event_id": "UUID (source event ID)",
  "event_type": "string (taxonomy)",
  "event_layer": "string",
  "event_action": "string",
  "event_outcome": "string",
  "actor_id": "string",
  "actor_role": "string | null",
  "actor_ip_hash": "SHA-256 | null",
  "actor_device_fingerprint_hash": "SHA-256 | null",
  "actor_geo_region": "string | null",
  "actor_geo_country": "string | null",
  "tenant_id": "string",
  "target_tenant_id": "string | null",
  "session_id": "string | null",
  "request_id": "UUID",
  "resource_type": "string",
  "resource_id": "UUID | SYSTEM",
  "resource_domain_track": "string | null",
  "data_classification": "PUBLIC | INTERNAL | SENSITIVE | RESTRICTED | PII",
  "http_method": "string | null",
  "http_path": "string | null",
  "http_status_code": "integer | null",
  "request_duration_ms": "integer | null",
  "query_row_count": "integer | null",
  "permission_code_checked": "string | null",
  "authz_result": "ALLOW | DENY | null",
  "agent_name": "string | null",
  "anomaly_flag": "boolean",
  "anomaly_type": "string | null",
  "anomaly_severity": "NONE | LOW | MEDIUM | HIGH | CRITICAL | null",
  "parent_event_id": "UUID | null",
  "timestamp_utc": "ISO 8601",
  "ingested_at_utc": "ISO 8601",
  "model_version": "string",
  "audit_reference": "UUID"
}
```

### Output B — Anomaly Signal (Real-Time)

```json
ANOMALY_SIGNAL: {
  "signal_id": "UUID",
  "anomaly_type": "string (see Anomaly Catalogue Section 7)",
  "anomaly_severity": "LOW | MEDIUM | HIGH | CRITICAL",
  "actor_id": "string",
  "tenant_id": "string",
  "session_id": "string | null",
  "triggering_event_ids": ["UUID"],
  "evidence_summary": "string (plain language, max 500 chars)",
  "contributing_signals": [
    {
      "signal_name": "string",
      "signal_value": "string | number | boolean",
      "threshold_breached": "string | null"
    }
  ],
  "recommended_response": "MONITOR | ALERT | SOFT_BLOCK | HARD_BLOCK | ESCALATE",
  "auto_action_taken": "string | null",
  "timestamp_utc": "ISO 8601",
  "proof_reference": "UUID"
}
```

### Output C — Session Intelligence Snapshot

```json
SESSION_STATE_SNAPSHOT: {
  "session_id": "string",
  "actor_id": "string",
  "tenant_id": "string",
  "session_created_utc": "ISO 8601",
  "last_activity_utc": "ISO 8601",
  "session_duration_seconds": "integer",
  "event_count": "integer",
  "resource_types_accessed": ["string"],
  "unique_resources_accessed": "integer",
  "geo_locations_seen": ["string"],
  "devices_seen": "integer",
  "auth_events": "integer",
  "deny_events": "integer",
  "anomaly_events": "integer",
  "pii_access_count": "integer",
  "bulk_operations_count": "integer",
  "session_risk_score": "float [0.00–1.00]",
  "session_status": "ACTIVE | IDLE | SUSPICIOUS | TERMINATED | HIJACKED"
}
```

---

## 6️⃣ BEHAVIOURAL BASELINE ENGINE

### Baseline Construction

```
BASELINE_SCOPE: Per-actor, per-role, per-tenant, per-domain-track

BASELINE_FEATURES:
  — typical_login_time_range       (hour-of-day distribution, 7-day rolling)
  — typical_login_geo_countries    (set of observed countries, 30-day rolling)
  — typical_login_devices          (hashed device fingerprint set, 30-day rolling)
  — avg_daily_api_request_count    (7-day rolling average)
  — avg_daily_unique_resources     (7-day rolling average)
  — typical_session_duration_min   (7-day rolling average)
  — typical_resource_types         (set of resource types accessed, 30-day rolling)
  — typical_bulk_operation_rate    (operations/hour, 7-day rolling)
  — typical_data_export_frequency  (exports/week, 30-day rolling)
  — typical_denied_request_rate    (denials/day, 7-day rolling)
  — typical_pii_access_rate        (PII reads/day, 30-day rolling)
  — typical_admin_action_rate      (admin actions/day, 30-day rolling — admin roles only)

BASELINE_UPDATE_FREQUENCY:
  — Incremental: every 24 hours (rolling window update)
  — Full rebuild: weekly
  — Cold-start: new actors receive role-cohort baseline
    until 7 days of personal data accumulated

BASELINE_STORAGE:
  — FEATURE_STORE_AGENT (append-only feature vectors)
  — Namespace: access_behaviour:{user_id}:{tenant_id}
```

### Anomaly Scoring Engine

```
ANOMALY_SCORING_MODEL = Isolation Forest + Statistical Z-Score Ensemble

For each incoming access event batch (per session, 60-second windows):
  1. Extract session features (current window)
  2. Compare against personal baseline
  3. Compare against role-cohort baseline
  4. Compute Z-score deviation per feature
  5. Isolation Forest anomaly score (0.0–1.0)
  6. Ensemble: weighted average
  7. Map to anomaly_severity tier

ANOMALY_SEVERITY_THRESHOLDS:
  isolation_forest_score / z_score_deviation → anomaly_severity
  ────────────────────────────────────────────────────────────
  < 0.3  / z < 2.0   → NONE
  0.3–0.5 / z 2.0–3.0 → LOW (log only)
  0.5–0.7 / z 3.0–4.0 → MEDIUM (alert SECURITY_ADMIN)
  0.7–0.85 / z 4.0–5.0 → HIGH (SOFT_BLOCK + alert)
  > 0.85 / z > 5.0   → CRITICAL (HARD_BLOCK option + ESCALATE)

TRAINING_FREQUENCY:
  — Isolation Forest: retrained weekly per role-cohort
  — Threshold calibration: monthly
  — Drift detection: KS-test on score distributions, weekly
```

---

## 7️⃣ ANOMALY CATALOGUE (COMPLETE REGISTRY)

Every detected anomaly pattern has a registered type:

```
AUTHENTICATION ANOMALIES:
  ANON_AUTH_001 — Brute force: > 10 failed logins in 5 minutes
  ANON_AUTH_002 — Impossible travel: login from two countries within 2 hours
  ANON_AUTH_003 — New country login: first-ever login from new country
  ANON_AUTH_004 — Dormant account activation: account unused > 180 days
  ANON_AUTH_005 — Concurrent session from different device + country
  ANON_AUTH_006 — Token replay: expired token used after revocation
  ANON_AUTH_007 — MFA bypass attempt: MFA failures > 5 within 10 minutes
  ANON_AUTH_008 — Credential stuffing pattern: multiple accounts, same IP hash

SESSION ANOMALIES:
  ANON_SESS_001 — Session duration spike: > 3σ above baseline
  ANON_SESS_002 — Resource volume spike: > 3σ above daily baseline
  ANON_SESS_003 — Unusual access time: activity outside 99th percentile time window
  ANON_SESS_004 — Device change mid-session: device fingerprint changes
  ANON_SESS_005 — Geo change mid-session: country changes within active session
  ANON_SESS_006 — Session token shared: same token from 2+ distinct IPs

PRIVILEGE ANOMALIES:
  ANON_PRIV_001 — Repeated DENY events: > 5 denied requests in 10 minutes
  ANON_PRIV_002 — Privilege escalation attempt: requesting resources above role scope
  ANON_PRIV_003 — Role creep: gradual expansion of accessed resource types
  ANON_PRIV_004 — Admin access outside normal pattern: admin tools outside hours
  ANON_PRIV_005 — Sensitive field access spike: PII reads > 3σ above baseline
  ANON_PRIV_006 — Permission check failures cluster: same permission, multiple actors

DATA ACCESS ANOMALIES:
  ANON_DATA_001 — Bulk read spike: > 1,000 rows in single session (non-admin)
  ANON_DATA_002 — Bulk export: export > 500 records (flag regardless of role)
  ANON_DATA_003 — Bulk delete: delete > 50 records in 60 seconds
  ANON_DATA_004 — Repeated PII access: PII fields accessed > 3σ above baseline
  ANON_DATA_005 — Systematic enumeration: sequential resource IDs accessed
  ANON_DATA_006 — Write pattern anomaly: write volume > 3σ above baseline
  ANON_DATA_007 — Off-hours data dump: large export between 00:00–04:00 local time

CROSS-TENANT ANOMALIES:
  ANON_XTEN_001 — Cross-tenant API request: tenant_id mismatch in JWT vs request
  ANON_XTEN_002 — Cross-tenant data query: DB-layer cross-tenant query blocked
  ANON_XTEN_003 — Cross-tenant resource reference: resource_id belongs to different tenant
  ANON_XTEN_004 — Tenant ID manipulation: URL/header tenant ID differs from JWT

AGENT ANOMALIES:
  ANON_AGNT_001 — Unregistered agent invocation: unknown caller source
  ANON_AGNT_002 — Agent scope violation: agent accessing outside declared DATA_SCOPE
  ANON_AGNT_003 — Agent invocation spike: agent called > 3σ above baseline rate
  ANON_AGNT_004 — Agent output tamper attempt: modification attempt on sealed output

ADMIN ANOMALIES:
  ANON_ADMN_001 — Audit log access spike: admin reading audit logs > 3σ baseline
  ANON_ADMN_002 — Audit tampering attempt: ANY attempt to modify audit records (CRITICAL)
  ANON_ADMN_003 — Mass role assignment: > 20 role changes by single admin in 1 hour
  ANON_ADMN_004 — Sensitive config change outside change window
  ANON_ADMN_005 — Multiple admin account creation: > 5 admin accounts in 24 hours
  ANON_ADMN_006 — Self-permission grant: admin granting permissions to own account

INSIDER THREAT PATTERNS:
  ANON_INSD_001 — Pre-termination data exfiltration: large exports by actor
                  with upcoming account deactivation flag
  ANON_INSD_002 — Access pattern change: sudden shift in resource access profile
  ANON_INSD_003 — Credential sharing indicator: same session from > 2 devices
  ANON_INSD_004 — Systematic PII harvesting: PII accessed across many user records
  ANON_INSD_005 — Off-boarding data grab: bulk export within 7 days of role change
```

---

## 8️⃣ PII ACCESS GOVERNANCE

```
PII_CLASSIFICATION_FIELDS (platform-wide registry):

  HIGH SENSITIVITY (RESTRICTED):
    — full_name + email + phone (combined)
    — password_hash (any access = CRITICAL anomaly unless KEY_MANAGEMENT)
    — government_id fields
    — financial account details
    — health or disability data

  MEDIUM SENSITIVITY (SENSITIVE):
    — email alone
    — phone alone
    — date_of_birth
    — precise location data
    — biometric identifiers

  LOW SENSITIVITY (INTERNAL):
    — username / display name
    — institution name
    — general location (city / country)

PII ACCESS LOGGING RULES:
  — Every access to RESTRICTED PII fields: logged with data_classification = PII
  — Every access to SENSITIVE PII fields: logged with data_classification = SENSITIVE
  — Bulk PII access (> 10 records in one session): ANOMALY flagged immediately
  — PII access without lawful_basis_code in request context: FLAGGED
  — PII access reports: generated for DATA_PROTECTION_OFFICER monthly
    and on-demand for GDPR subject access requests
  — PII access by non-authorized role: ANON_PRIV_005 triggered immediately

LAWFUL_BASIS_CODE_REGISTRY:
  — CONSENT          User has given explicit consent
  — CONTRACT         Access necessary for contract performance
  — LEGAL_OBLIGATION Access required by law
  — VITAL_INTERESTS  Emergency safety access
  — PUBLIC_TASK      Platform compliance function
  — LEGITIMATE_INT   Legitimate platform interest (documented)
  — SYSTEM           Internal system operation (audit, security)
```

---

## 9️⃣ SESSION LIFECYCLE TRACKING

```
SESSION_STATES:
  CREATED      → Session token issued, first request received
  ACTIVE       → Regular activity within expected parameters
  IDLE         → No activity for > 15 minutes (tenant-configurable)
  SUSPICIOUS   → Anomaly detected (session continues under enhanced monitoring)
  FORCE_REVIEW → Session flagged for immediate human review
  TERMINATED   → Session ended (logout, expiry, or force-terminate)
  HIJACKED     → Session confirmed as compromised (tokens revoked)

STATE TRANSITIONS:
  CREATED → ACTIVE         On first successful API request
  ACTIVE → IDLE            On 15-minute inactivity timeout
  IDLE → ACTIVE            On any new request within idle window
  IDLE → TERMINATED        On idle timeout > tenant max idle (default: 30 min)
  ACTIVE → SUSPICIOUS      On anomaly detection (MEDIUM+)
  SUSPICIOUS → ACTIVE      On anomaly resolved by human reviewer
  SUSPICIOUS → FORCE_REVIEW On anomaly escalated
  ANY → TERMINATED         On explicit logout or admin force-terminate
  ANY → HIJACKED           On ANON_SESS_006 confirmed

SESSION TRACKING EVENTS EMITTED:
  Kafka topic: session.state.transitions
  Consumers: SECURITY_ADMIN, NOTIFICATION_AGENT (user-facing alerts),
             FRAUD_DETECTION_AGENT

CONCURRENT SESSION POLICY:
  Default: max 3 concurrent sessions per user (tenant-configurable)
  Breach: ANON_SESS_005 flagged
  Enforcement: AUTHORIZATION_SERVICE gates on session count
```

---

## 🔟 COMPLIANCE ACCESS REPORTING

```
REPORT TYPES:

  R_ACCESS_001 — GDPR Subject Access Log
    Triggered: On Data Subject Access Request (DSAR)
    Content: All access events for a specific actor over requested period
    Format: Structured JSON + PDF summary
    Timeline: Generated within 24 hours of request
    Recipient: DATA_PROTECTION_OFFICER → subject

  R_ACCESS_002 — Admin Action Audit Report
    Triggered: Monthly (automated) + on-demand
    Content: All ADMIN layer events per tenant
    Format: Structured JSON + CSV
    Recipient: COMPLIANCE_ADMIN + GOVERNANCE_AGENT

  R_ACCESS_003 — Privilege Usage Report
    Triggered: Weekly (automated)
    Content: Permission check results, deny rates, role usage analysis
    Format: Structured JSON
    Recipient: COMPLIANCE_ADMIN + SECURITY_ADMIN

  R_ACCESS_004 — PII Access Report
    Triggered: Monthly (automated) + on-demand
    Content: All PII field access events by actor and purpose
    Format: Structured JSON
    Recipient: DATA_PROTECTION_OFFICER

  R_ACCESS_005 — Security Anomaly Summary
    Triggered: Daily (automated) + real-time for CRITICAL
    Content: All anomaly signals, resolution status, open incidents
    Format: Structured JSON + dashboard feed
    Recipient: SECURITY_ADMIN + CISO (monthly executive summary)

  R_ACCESS_006 — Cross-Tenant Attempt Report
    Triggered: Any cross-tenant event (real-time) + weekly summary
    Content: All ANON_XTEN_* events with evidence
    Format: Structured JSON
    Recipient: CISO + DATA_PROTECTION_OFFICER + GOVERNANCE_AGENT

  R_ACCESS_007 — Forensic Evidence Package
    Triggered: On security incident declaration
    Content: Complete access log bundle for incident timeframe,
             hash-sealed by HASH_PROOF_AGENT, Merkle-proofed
    Format: Sealed JSON + verification manifest
    Recipient: INCIDENT_RESPONSE_MANAGER + legal team (if applicable)

ALL REPORTS:
  — Sealed by HASH_PROOF_AGENT before delivery
  — Stored in compliance_reports store (append-only)
  — Access to reports is itself logged as ADMIN.AUDIT.ACCESSED
```

---

## 1️⃣1️⃣ INTER-AGENT DEPENDENCY MAP

```
UPSTREAM (event sources — push model):
  API_GATEWAY_AGENT         → Kafka: api.access.events
  IDENTITY_AGENT            → Kafka: auth.access.events
  AUTHORIZATION_SERVICE     → Kafka: authz.decision.events
  ALL PLATFORM SERVICES     → Kafka: service.access.events
  DATABASE_PROXY_LAYER      → Kafka: data.access.events
  ADMIN_CONSOLE_SERVICE     → Kafka: admin.action.events
  ALL ANTIGRAVITY AGENTS    → Kafka: agent.invocation.events
  HASH_PROOF_AGENT          → Kafka: hash.proof.request.events (access to proof records)

DOWNSTREAM (consumers of access log outputs):
  HASH_PROOF_AGENT          → Kafka: hash.proof.request.queue (seals every log record)
  FRAUD_DETECTION_AGENT     → Kafka: access.anomaly.signals
  SECURITY_ADMIN            → Kafka: access.anomaly.signals (HIGH + CRITICAL)
  INCIDENT_RESPONSE_MANAGER → Kafka: access.security.incident.events
  GOVERNANCE_AGENT          → Kafka: access.privilege.violation.events
  COMPLIANCE_ADMIN          → Kafka: access.compliance.report.ready
  DATA_PROTECTION_OFFICER   → Kafka: access.pii.anomaly.signals
  OBSERVABILITY_AGENT       → Kafka: access.metrics.stream
  FEATURE_STORE_AGENT       → Kafka: feature.store.ingest (behavioural baselines)
  NOTIFICATION_AGENT        → Kafka: access.user.security.alert.events
  HUMAN_IN_THE_LOOP_REVIEWER→ Kafka: access.insider.threat.review.queue

KAFKA TOPICS CONSUMED:
  access.events.api         api.access.events stream
  access.events.auth        auth.access.events stream
  access.events.authz       authz.decision.events stream
  access.events.service     service.access.events stream
  access.events.data        data.access.events stream
  access.events.admin       admin.action.events stream
  access.events.agent       agent.invocation.events stream

KAFKA TOPICS EMITTED:
  access.log.records        → all consumers (sealed records)
  access.anomaly.signals    → FRAUD_DETECTION_AGENT, SECURITY_ADMIN
  access.security.incident  → INCIDENT_RESPONSE_MANAGER, CISO
  access.session.transitions→ SECURITY_ADMIN, NOTIFICATION_AGENT
  access.pii.signals        → DATA_PROTECTION_OFFICER
  access.privilege.signals  → GOVERNANCE_AGENT
  access.metrics.stream     → OBSERVABILITY_AGENT
  access.compliance.events  → COMPLIANCE_ADMIN
  feature.store.ingest      → FEATURE_STORE_AGENT
```

---

## 1️⃣2️⃣ SCALABILITY DESIGN

```
EXPECTED_RPS              = 100,000 access events/second (peak — 100M user scale)
                            Rationale: every user action generates 3–8 access events
                            across API, service, and data layers
LATENCY_TARGET            = Ingestion p99 < 10ms (fire-and-forget write path)
                            Anomaly detection p95 < 500ms (stream processing)
                            Compliance query p95 < 2 seconds
MAX_CONCURRENCY           = Stateless ingestion pods — auto-scaled by Kafka lag
QUEUE_STRATEGY            = Kafka topics partitioned by tenant_id
                            Priority lanes:
                              PRIORITY_CRITICAL: ADMIN + SECURITY events (immediate)
                              PRIORITY_HIGH: AUTH + AUTHZ events (< 50ms)
                              PRIORITY_NORMAL: API + SERVICE + DATA events
                            Dead-letter queue for failed writes (no event dropped)
STORAGE_STRATEGY          = Time-partitioned append-only PostgreSQL
                            (partitioned by tenant_id + month)
                            Hot storage: 90 days (query-optimised)
                            Warm storage: 90–365 days (compressed)
                            Cold storage: 365+ days (MinIO object store, sealed)
                            Retention policy: 7 years (audit compliance minimum)
SEARCH_INDEX              = OpenSearch 2.x — real-time access log indexing
                            Full-text search on resource_type, actor_id, event_type
                            Time-series queries on anomaly_flag = true
IDEMPOTENCY               = event_id deduplication (Redis, TTL: 72h)
SCALING_MODEL             = Kubernetes HPA on Kafka consumer lag
                            Stateless ingestion pods
                            Stateful stream-processing pods (anomaly detection)
                            with leader-election for baseline computation
```

---

## 1️⃣3️⃣ SECURITY ENFORCEMENT

```
TENANT_ISOLATION:
  — Access log store partitioned by tenant_id (schema-level isolation)
  — No cross-tenant log query except: PLATFORM_SUPER_ADMIN + CISO (dual auth)
  — Cross-tenant analytics: anonymised aggregates only (no actor_id exposed)

ACCESS CONTROL TO LOG RECORDS:
  — View own access logs: any authenticated user (own actor_id only)
  — View tenant access logs: compliance_admin | audit_admin | security_admin
  — View platform-wide logs: platform_super_admin + CISO (dual auth required)
  — Modify access logs: NOBODY — HARD BLOCK at database constraint level
  — Delete access logs: NOBODY — HARD BLOCK at database constraint level
  — Export access logs: compliance_admin | audit_admin (all exports are themselves logged)

LOG RECORD IMMUTABILITY:
  — All access log records: append-only PostgreSQL partitions
  — Schema-level INSERT-only constraint (no UPDATE, no DELETE permitted)
  — Any attempt triggers: ANON_ADMN_002 (CRITICAL) + SECURITY_INCIDENT_EVENT

SELF-PROTECTION:
  — ACCESS_LOG_TRACKER_AGENT's own access events are logged
    to a separate meta_access_log (cannot be accessed by standard compliance queries)
  — Meta_access_log accessible only by PLATFORM_SUPER_ADMIN + CISO
  — Any access to meta_access_log is itself recorded in a tertiary immutable store

ENCRYPTION:
  — All log records: AES-256 at rest
  — All event streams (Kafka): TLS 1.3 in transit
  — IP hashes, device fingerprints: one-way SHA-256 (irreversible)
  — PII fields in log records: additionally encrypted with tenant-specific key
    (tenant key managed by KEY_MANAGEMENT_SERVICE)
```

---

## 1️⃣4️⃣ AUDIT & TRACEABILITY

Every ACCESS_LOG_TRACKER_AGENT operation is itself meta-audited:

```json
META_AUDIT_RECORD: {
  "meta_audit_id": "UUID",
  "operation": "LOG_INGESTED | ANOMALY_DETECTED | REPORT_GENERATED |
                BASELINE_UPDATED | INCIDENT_DECLARED | TAMPER_ATTEMPT",
  "timestamp_utc": "ISO 8601",
  "event_count_processed": "integer",
  "anomaly_count_detected": "integer",
  "proof_reference": "UUID (HASH_PROOF_AGENT seal of this meta record)",
  "processing_node_id": "string",
  "model_version": "string",
  "error_count": "integer",
  "dropped_events": "integer (must always be 0 — non-zero = INCIDENT)"
}
```

**Guarantee:** `dropped_events` must always be 0.
Any non-zero value = immediate `SECURITY_INCIDENT_EVENT`.

---

## 1️⃣5️⃣ FAILURE POLICY

| Failure Condition | Response |
|---|---|
| Log store write failure | RETRY x5 → Dead-letter queue → NEVER drop event → LOG_INCIDENT → ESCALATE INCIDENT_RESPONSE_MANAGER |
| HASH_PROOF_AGENT unavailable | CONTINUE logging to unsealed buffer → seal in batch when restored → FLAG all records as pending_seal → ALERT SECURITY_ADMIN |
| Kafka topic unavailable | FALLBACK to synchronous write to emergency log store → LOG_INCIDENT → ESCALATE PLATFORM_DEVOPS_AGENT |
| Anomaly model unavailable | CONTINUE logging → DISABLE anomaly detection → LOG_INCIDENT → ALERT SECURITY_ADMIN (reduced protection mode) |
| OpenSearch index failure | CONTINUE logging to PostgreSQL → DISABLE real-time search → LOG_INCIDENT → ALERT PLATFORM_DEVOPS_AGENT |
| Baseline computation failure | FALLBACK to role-cohort baseline → LOG_INCIDENT → ALERT PLATFORM_DEVOPS_AGENT |
| Cross-tenant access detected | IMMEDIATE SECURITY_INCIDENT_EVENT → ESCALATE CISO + DPO → LOG with CRITICAL severity |
| Audit tamper attempt detected | IMMEDIATE SECURITY_INCIDENT_EVENT → ESCALATE CISO + DPO + GOVERNANCE → HARD BLOCK at DB layer |
| Event volume spike > 3x baseline | SCALE OUT (HPA trigger) → ALERT PLATFORM_DEVOPS_AGENT → LOG capacity event |
| Meta-audit chain break | CRITICAL SECURITY_INCIDENT → ESCALATE CISO + PLATFORM_SUPER_ADMIN + DPO |

```
RETRY_POLICY     = Exponential backoff: 10ms → 50ms → 200ms → 500ms → 1000ms → DLQ
SILENT_DROP      = FORBIDDEN — every event must be persisted or dead-lettered
SILENT_FAILURE   = FORBIDDEN — every failure produces a log entry
PARTIAL_RECORD   = FORBIDDEN — complete access_log_record or dead-letter
```

---

## 1️⃣6️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```
FEATURE VECTORS EMITTED TO FEATURE_STORE_AGENT after every session close:

  feature: access_daily_api_request_count_{7d_avg}
  feature: access_denied_request_rate_{7d_avg}
  feature: access_pii_access_count_{30d}
  feature: access_bulk_operation_count_{30d}
  feature: access_export_count_{30d}
  feature: access_anomaly_count_{90d}
  feature: access_unique_countries_{30d}
  feature: access_unique_devices_{30d}
  feature: access_session_duration_avg_{7d}
  feature: access_off_hours_activity_rate_{30d}
  feature: access_session_risk_score_latest
  feature: access_highest_anomaly_severity_{90d}

All vectors: Kafka topic → feature.store.ingest → FEATURE_STORE_AGENT

Usage: COPY_PROBABILITY_AGENT uses behavioural features for exam integrity scoring.
       FRAUD_DETECTION_AGENT uses access features for fraud risk profiling.
       RANK_UPDATE_AGENT uses integrity signals for trust-weighted XP.
```

---

## 1️⃣7️⃣ PERFORMANCE MONITORING

```
METRICS (emitted to OBSERVABILITY_AGENT every 30 seconds):

  access_log_agent_events_ingested_per_second     → track vs. platform RPS
  access_log_agent_p99_ingestion_latency_ms        → target: < 10ms
  access_log_agent_p95_anomaly_detection_latency_ms→ target: < 500ms
  access_log_agent_anomaly_detection_rate          → events/min
  access_log_agent_critical_anomaly_count          → alert: ANY > 0 (immediate)
  access_log_agent_dropped_event_count             → alert: ANY > 0 (CRITICAL)
  access_log_agent_unsealed_record_count           → alert: > 0 for > 60 seconds
  access_log_agent_cross_tenant_attempt_count      → alert: ANY > 0 (immediate)
  access_log_agent_audit_tamper_attempt_count      → alert: ANY > 0 (P0 incident)
  access_log_agent_log_store_write_success_rate    → target: 100%
  access_log_agent_baseline_freshness_hours        → alert: > 48 hours stale
  access_log_agent_kafka_consumer_lag_seconds      → alert: > 5 seconds
  access_log_agent_dead_letter_queue_depth         → alert: > 0
  access_log_agent_opensearch_index_lag_seconds    → alert: > 30 seconds

ALERT SEVERITY OVERRIDES:
  dropped_event_count > 0           → P0 CRITICAL (wake CISO + INCIDENT_RESPONSE)
  audit_tamper_attempt_count > 0    → P0 CRITICAL (wake CISO + DPO + GOVERNANCE)
  cross_tenant_attempt_count > 0    → P1 CRITICAL (wake CISO + DPO)
  critical_anomaly_count > 0        → P1 HIGH (alert SECURITY_ADMIN + CISO)
  unsealed_record_count > 60s       → P2 HIGH (alert SECURITY_ADMIN)
```

---

## 1️⃣8️⃣ VERSIONING POLICY

```
ALL_CHANGES              = ADD_ONLY
BACKWARD_COMPAT          = MANDATORY
                           Old log record formats must remain queryable
SCHEMA_MIGRATION         = Zero-downtime (new columns nullable, old columns retained)
EVENT_TAXONOMY_CHANGES   = New event types: ADD_ONLY
                           Existing event types: RENAME_FORBIDDEN, DELETE_FORBIDDEN
ANOMALY_CATALOGUE        = New anomaly types: ADD_ONLY via governance approval
                           Existing types: threshold adjustments require SECURITY_ADMIN approval
                           Deletion: FORBIDDEN
BASELINE_MODEL_VERSIONING= Side-by-side deployment, shadow mode before cutover
RETENTION_POLICY_CHANGES = Require DATA_PROTECTION_OFFICER + GOVERNANCE_AGENT approval
                           Retention may only be EXTENDED, never shortened
                           (shortening = potential evidence destruction = FORBIDDEN)
SCHEMA_VERSION           = Semver (MAJOR.MINOR.PATCH)
CUTOVER_POLICY           = GOVERNANCE_AGENT + SECURITY_ADMIN + CISO approval
```

---

## 1️⃣9️⃣ NON-NEGOTIABLE RULES

This agent **MUST NOT**:

```
❌ Drop, silently discard, or lose any access event under any condition
❌ Store raw IP addresses, raw device fingerprints, or raw user agent strings
   — all must be one-way hashed before storage
❌ Expose one tenant's access logs to another tenant
❌ Allow any actor (including platform_super_admin) to modify or delete a log record
❌ Omit HASH_PROOF_AGENT sealing for any access log record
❌ Bypass anomaly detection for any actor class (including platform admins)
❌ Expose the actor's full session activity to the actor themselves in real time
   — actors may request their own logs via DSAR process only
❌ Allow access to PII fields without lawful_basis_code validation
❌ Reduce log retention periods without DATA_PROTECTION_OFFICER + GOVERNANCE approval
❌ Disable or reduce anomaly detection sensitivity without SECURITY_ADMIN approval
❌ Cache or store raw authentication credentials (tokens, passwords) in any log field
❌ Treat admin actors as exempt from anomaly detection
❌ Emit access events to unauthenticated or unregistered consumers
❌ Execute enforcement actions directly
   — this agent SIGNALS, enforcement = AUTHORIZATION_SERVICE + SECURITY_ADMIN
❌ Modify this document without PLATFORM_SUPER_ADMIN + GOVERNANCE_AGENT + CISO triple approval
```

---

## 🔐 SEAL DECLARATION

```
AGENT_NAME           = ACCESS_LOG_TRACKER_AGENT
VERSION              = 1.0.0
SEAL_DATE            = 2026-02-25
SEALED_BY            = ECOSKILLER ANTIGRAVITY — ZERO-TRUST SURVEILLANCE &
                       ACCESS INTELLIGENCE CORE
CLASSIFICATION       = SECURITY-CRITICAL
MUTATION_POLICY      = ADD_ONLY
                       — This document may only grow via version bump
                       — No existing section may be altered, weakened, or removed
OVERRIDE_AUTHORITY   = PLATFORM_SUPER_ADMIN + GOVERNANCE_AGENT + CISO
                       (triple approval mandatory)
CREATIVE_DEVIATION   = FORBIDDEN
ASSUMPTION_FILLING   = FORBIDDEN
SILENT_DROP          = FORBIDDEN — zero tolerance for lost events
SILENT_FAILURE       = FORBIDDEN — zero tolerance for unlogged failures
RECORD_MODIFICATION  = FORBIDDEN — at database, schema, and agent layers
RECORD_DELETION      = FORBIDDEN — at database, schema, and agent layers
RAW_PII_STORAGE      = FORBIDDEN — all identifying fields one-way hashed
CROSS_TENANT_QUERY   = FORBIDDEN except PLATFORM_SUPER_ADMIN + CISO dual auth
ENFORCEMENT_SCOPE    = SURVEILLANCE + DETECTION + SIGNALLING ONLY
                       — This agent NEVER blocks or enforces directly
                       — Enforcement = AUTHORIZATION_SERVICE + SECURITY_ADMIN
SELF_EXEMPTION       = FORBIDDEN — platform admins are not exempt from logging
DETERMINISM          = Identical access event → Identical log record
                       (given same model_version and timestamp)
TRUST_HIERARCHY      = Zero-trust: no actor, role, or agent is trusted by default
                       Every access is logged, regardless of claimed authority
ENFORCEMENT_MODE     = DETERMINISTIC + STREAM-PROCESSED + TAMPER-EVIDENT + AUDITED

SELF-SEAL:
  ACCESS_LOG_TRACKER_AGENT's own operational events are themselves
  logged and sealed. The agent that watches everything
  is also watched.
  No surveillance blind spot is permitted anywhere on the platform.
```

> Any agent, human, or system that attempts to suppress, modify, delete, bypass,
> or weaken the access logging infrastructure is committing a **platform security
> violation** regardless of their role or claimed authority.
>
> All such attempts are themselves captured by this agent, sealed by HASH_PROOF_AGENT,
> and escalated immediately as `GOVERNANCE_VIOLATION` + `SECURITY_INCIDENT` events
> to `PLATFORM_SUPER_ADMIN`, `GOVERNANCE_AGENT`, `CISO`, and `DATA_PROTECTION_OFFICER`.
>
> The only entities who cannot be silently watched are those who have already been
> detected attempting to disable the watching.
> **That detection is irrevocable.**

---

*End of ACCESS_LOG_TRACKER_AGENT — Sealed & Locked v1.0.0*
