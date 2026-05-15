# PHONE_EVENT_SCHEMA_VALIDATION_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ECOSKILLER — ANTIGRAVITY MODULE
**Status:** `SEALED · LOCKED · GOVERNED · DETERMINISTIC`  
**Version:** `v1.0`  
**Mutation Policy:** `ADD-ONLY via version bump`  
**Interpretation Authority:** `NONE`  
**Execution Authority:** `Human declaration only`  
**Default Behavior:** `DENY`  
**Failure Mode:** `STOP → REPORT → NO PARTIAL OUTPUT`  
**Companion Agent:** `PHONE_API_CONTRACT_REGISTRY_AGENT v1.0`

---

## SECTION 0 — ANTIGRAVITY DECLARATION

ANTIGRAVITY is the architectural law inside Ecoskiller that eliminates invisible systemic gravity — the weight of corrupted events, malformed payloads, schema drift, and unvalidated phone data flowing silently through the Kafka event bus, poisoning downstream consumers, inflating fraud signals, and corrupting audit trails.

The PHONE_EVENT_SCHEMA_VALIDATION_AGENT is the **schema enforcement organ of ANTIGRAVITY** for all phone-surface Kafka events. It does not produce business logic. It does not route messages. It does not store events. It **intercepts, validates, classifies, and either clears or quarantines** every phone-related Kafka message before any downstream consumer is permitted to act on it.

This agent exists because:

- Phone events carry PII-adjacent data (phone hashes, device fingerprints, trust signals) that must be structurally correct before reaching analytics, fraud detection, or governance systems
- Schema drift between microservice versions is the most common cause of silent data corruption in event-driven architectures
- A malformed `otp.issued` event that passes silently can create phantom verification records, fake trust scores, and exploitable audit gaps
- The Event Schema Registry is declared as a mandatory pre-code contract in Ecoskiller's Contract-First execution law (SECTION F of Master Execution Prompt) — this agent enforces that law at runtime
- No phone event may traverse the Kafka bus without being schema-validated, version-checked, and audit-logged by this agent

**Absence of this agent → ALL phone.events topic consumers HALT.**  
**Schema validation failure → Message routed to Dead Letter Queue. No retry without human review.**

---

## SECTION 1 — AGENT IDENTITY

```
AGENT_NAME                : PHONE_EVENT_SCHEMA_VALIDATION_AGENT
AGENT_CLASS               : Trust Infrastructure · Schema Enforcement · Event Governance
DOMAIN_MODULE             : ENTERPRISE_OPTIMIZATION
SUB_MODULE                : TRUST_INFRASTRUCTURE
ANTIGRAVITY_LAYER         : PHONE_EVENT_SURFACE
COMPANION_AGENT           : PHONE_API_CONTRACT_REGISTRY_AGENT v1.0
SERVICE_NAMESPACE         : trust-infra
KUBERNETES_NAMESPACE      : ecoskiller-trust
DEPLOY_TARGET             : k3s / GCP Kubernetes
LANGUAGE_RUNTIME          : Spring Boot (Java 21) — preferred for schema validation throughput
SCHEMA_FORMAT             : AsyncAPI 2.6 (canonical) · JSON Schema Draft 2020-12 (runtime)
SCHEMA_STORAGE            : PostgreSQL (phone_event_schema_registry schema)
SCHEMA_CACHE              : Redis (compiled schema objects · version index)
EVENT_BUS                 : Apache Kafka
VALIDATION_POSITION       : Producer-side interceptor + Consumer-side guard (dual-layer)
DLQ_TOPIC                 : phone.events.dlq
QUARANTINE_TOPIC          : phone.events.quarantine
AUDIT_STORE               : ClickHouse (phone_schema_validation_audit table)
OBSERVABILITY             : Prometheus · Loki · OpenTelemetry
SECURITY_LAYER            : Keycloak (JWT internal) · OPA (schema mutation policy) · Vault (secrets)
```

---

## SECTION 2 — AGENT POSITION IN THE EVENT FLOW

### 2.1 Architectural Position (LOCKED)

This agent operates at **two enforcement points** in the phone event pipeline. Neither point is optional. Removing either point voids the ANTIGRAVITY guarantee.

```
POINT 1 — PRODUCER INTERCEPTOR (Pre-publish gate)
══════════════════════════════════════════════════
[PHONE_API_CONTRACT_REGISTRY_AGENT]
         │
         │ emits event payload
         ▼
[PHONE_EVENT_SCHEMA_VALIDATION_AGENT — Producer Interceptor]
         │
         ├── PASS → publish to phone.events topic
         ├── FAIL → route to phone.events.dlq + emit schema_violation event
         └── QUARANTINE → route to phone.events.quarantine + alert Admin Governance


POINT 2 — CONSUMER GUARD (Pre-processing gate per downstream consumer)
═══════════════════════════════════════════════════════════════════════
[phone.events topic]
         │
         ▼
[PHONE_EVENT_SCHEMA_VALIDATION_AGENT — Consumer Guard]
         │
         ├── Fraud Detection Engine
         ├── Analytics Service → ClickHouse
         ├── Admin Governance Service
         ├── Parent Dashboard Service
         ├── Notification Service
         └── Immutable Archive Service
         │
         ├── PASS → deliver to consumer
         ├── FAIL → block delivery + DLQ + log
         └── VERSION_MISMATCH → route to version negotiation handler
```

### 2.2 What This Agent Validates (LOCKED)

| Validation Layer | Description |
|---|---|
| Structural integrity | JSON is well-formed, all required fields present |
| Type enforcement | Every field matches declared type in registered schema |
| Enum enforcement | Enum fields contain only declared values |
| Format enforcement | UUIDs are valid UUIDs, dates are ISO 8601, IPs are valid INET |
| PII boundary enforcement | Phone numbers in E.164 plaintext are REJECTED — hash only permitted |
| Null safety | Non-nullable fields must not be null or empty string |
| Size bounds | String lengths, array lengths, numeric ranges within declared bounds |
| Schema version match | `schema_version` field in payload matches registered active version |
| Contract linkage | `contract_id` in OTP events references an ACTIVE contract in registry |
| Sequence integrity | Event ordering constraints respected (e.g., `otp.verified` requires prior `otp.issued`) |
| Tenant isolation | `tenant_id` is present and non-null in all tenant-scoped events |
| Timestamp sanity | `event_timestamp` is within ±5 minutes of server clock |
| Duplicate detection | Idempotency key checked against Redis deduplication window |

### 2.3 What This Agent Does NOT Do (HARD BOUNDARY)

```
DOES NOT modify event payloads — pass or reject, never mutate
DOES NOT implement business logic — structural validation only
DOES NOT store event data — audit records reference event_id only
DOES NOT perform fraud scoring — routes events to Fraud Detection Engine
DOES NOT make retry decisions — all retry policy is DLQ-owner responsibility
DOES NOT block the Kafka broker — validation is async with configurable timeout
DOES NOT grant schema write access to application services — schema registry is write-locked
DOES NOT validate non-phone events — jurisdiction is phone.events topic ONLY
```

Violation of any boundary → STOP EXECUTION → REPORT AGENT-BOUNDARY-BREACH

---

## SECTION 3 — GOVERNED KAFKA TOPICS

### 3.1 Primary Topic (Full Jurisdiction)

```
TOPIC                : phone.events
PARTITIONS           : 6 (minimum) · 12 (at scale)
REPLICATION_FACTOR   : 3
RETENTION_MS         : 604800000 (7 days)
CLEANUP_POLICY       : delete
MAX_MESSAGE_BYTES    : 102400 (100 KB — phone events are small; oversized = reject)
COMPRESSION_TYPE     : lz4
```

### 3.2 Dead Letter Queue (DLQ) — Managed by This Agent

```
TOPIC                : phone.events.dlq
PARTITIONS           : 3
REPLICATION_FACTOR   : 3
RETENTION_MS         : 2592000000 (30 days)
CLEANUP_POLICY       : delete
ACCESS               : Platform Admin + Compliance Officer (read) · This agent (write)
REPLAY_POLICY        : Human-approved replay only · no automatic retry
```

### 3.3 Quarantine Topic — High-Severity Violations

```
TOPIC                : phone.events.quarantine
PARTITIONS           : 3
REPLICATION_FACTOR   : 3
RETENTION_MS         : 7776000000 (90 days — extended for forensic review)
CLEANUP_POLICY       : delete
ACCESS               : Compliance Officer + Platform Admin ONLY
REPLAY_POLICY        : FORBIDDEN without Compliance Officer sign-off
```

### 3.4 Internal Validation Audit Topic

```
TOPIC                : phone.schema.audit
PARTITIONS           : 3
REPLICATION_FACTOR   : 3
RETENTION_MS         : 2592000000 (30 days)
PURPOSE              : Written by this agent for every validation decision (PASS or FAIL)
CONSUMERS            : Analytics Service (ClickHouse ingest) · Admin Governance Service
```

### 3.5 Schema Mutation Events Topic

```
TOPIC                : phone.schema.mutations
PARTITIONS           : 1
REPLICATION_FACTOR   : 3
RETENTION_MS         : 31536000000 (365 days — full year schema lineage)
PURPOSE              : Records every schema version registration, deprecation, revocation
CONSUMERS            : Admin Governance · CI/CD Pipeline Validator
```

---

## SECTION 4 — SCHEMA REGISTRY DATA MODEL

### 4.1 Core Tables (PostgreSQL — phone_event_schema_registry schema)

```sql
-- MASTER EVENT TYPE REGISTRY
CREATE TABLE phone_event_types (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_type            TEXT NOT NULL UNIQUE,         -- e.g. otp.issued
  domain                TEXT NOT NULL DEFAULT 'phone', -- locked to phone for this agent
  description           TEXT NOT NULL,
  owning_service        TEXT NOT NULL,                -- Service that PRODUCES this event
  is_active             BOOLEAN DEFAULT TRUE,
  is_deprecated         BOOLEAN DEFAULT FALSE,
  deprecated_at         TIMESTAMPTZ,
  deprecation_reason    TEXT,
  sunset_at             TIMESTAMPTZ,                  -- After this: REJECT at producer
  requires_contract_id  BOOLEAN DEFAULT FALSE,        -- OTP events require contract linkage
  requires_tenant_id    BOOLEAN DEFAULT TRUE,
  pii_fields            TEXT[] DEFAULT '{}',          -- Field names classified as PII
  created_at            TIMESTAMPTZ DEFAULT now(),
  updated_at            TIMESTAMPTZ DEFAULT now()
);

-- SCHEMA VERSION REGISTRY
CREATE TABLE phone_event_schema_versions (
  id                        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_type_id             UUID NOT NULL REFERENCES phone_event_types(id),
  schema_version            TEXT NOT NULL,             -- Semantic: MAJOR.MINOR.PATCH
  json_schema               JSONB NOT NULL,            -- Full JSON Schema Draft 2020-12
  asyncapi_fragment         JSONB,                     -- AsyncAPI 2.6 message fragment
  state                     TEXT NOT NULL DEFAULT 'DRAFT',
  -- STATES: DRAFT · ACTIVE · DEPRECATED · REVOKED
  is_backward_compatible    BOOLEAN NOT NULL,
  breaking_change_summary   TEXT,                      -- Required if NOT backward_compatible
  approved_by               UUID REFERENCES users(id),
  approved_at               TIMESTAMPTZ,
  activated_at              TIMESTAMPTZ,
  deprecated_at             TIMESTAMPTZ,
  revoked_at                TIMESTAMPTZ,
  revocation_reason         TEXT,
  migration_notes           TEXT,                      -- How producers/consumers should adapt
  compiled_validator_ref    TEXT,                      -- Redis key of compiled validator object
  created_at                TIMESTAMPTZ DEFAULT now(),
  UNIQUE(event_type_id, schema_version)
);

-- VALIDATION DECISIONS LOG (append-only)
CREATE TABLE phone_schema_validation_log (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_id              TEXT NOT NULL,                 -- producer-assigned idempotency key
  event_type            TEXT NOT NULL,
  schema_version        TEXT NOT NULL,
  validation_point      TEXT NOT NULL CHECK (validation_point IN ('PRODUCER', 'CONSUMER')),
  consumer_service      TEXT,                          -- null if PRODUCER point
  decision              TEXT NOT NULL CHECK (decision IN ('PASS', 'FAIL', 'QUARANTINE', 'VERSION_MISMATCH')),
  failure_codes         TEXT[] DEFAULT '{}',           -- Structured failure codes
  failure_detail        JSONB DEFAULT '{}',            -- Field-level mismatch detail
  payload_size_bytes    INTEGER,
  processing_latency_ms INTEGER,
  tenant_id             UUID,
  producer_service      TEXT NOT NULL,
  kafka_partition       INTEGER,
  kafka_offset          BIGINT,
  dlq_routed            BOOLEAN DEFAULT FALSE,
  quarantine_routed     BOOLEAN DEFAULT FALSE,
  validated_at          TIMESTAMPTZ DEFAULT now()
) PARTITION BY RANGE (validated_at);

-- IDEMPOTENCY DEDUPLICATION WINDOW (overflow from Redis — 24h persistence)
CREATE TABLE phone_event_dedup_log (
  event_id              TEXT NOT NULL,
  event_type            TEXT NOT NULL,
  first_seen_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  occurrence_count      INTEGER DEFAULT 1,
  PRIMARY KEY (event_id, event_type)
);

-- SCHEMA COMPATIBILITY MATRIX
CREATE TABLE phone_schema_compatibility_rules (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_type_id         UUID NOT NULL REFERENCES phone_event_types(id),
  from_version          TEXT NOT NULL,
  to_version            TEXT NOT NULL,
  compatibility_type    TEXT NOT NULL CHECK (compatibility_type IN (
                          'FULL_COMPATIBLE',
                          'BACKWARD_COMPATIBLE',
                          'FORWARD_COMPATIBLE',
                          'BREAKING',
                          'UNKNOWN'
                        )),
  auto_migration_possible BOOLEAN DEFAULT FALSE,
  migration_transformer_ref TEXT,                      -- Service/function that can upgrade payload
  declared_at           TIMESTAMPTZ DEFAULT now(),
  declared_by           UUID REFERENCES users(id),
  UNIQUE(event_type_id, from_version, to_version)
);

-- DLQ MANIFEST
CREATE TABLE phone_dlq_manifest (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  original_event_id     TEXT NOT NULL,
  event_type            TEXT NOT NULL,
  kafka_topic           TEXT NOT NULL,                 -- Source topic
  kafka_partition       INTEGER,
  kafka_offset          BIGINT,
  dlq_topic             TEXT NOT NULL,
  dlq_partition         INTEGER,
  dlq_offset            BIGINT,
  failure_codes         TEXT[] NOT NULL,
  failure_summary       TEXT NOT NULL,
  payload_snapshot      JSONB,                         -- Sanitized — PII fields replaced with [REDACTED]
  tenant_id             UUID,
  producer_service      TEXT NOT NULL,
  routed_at             TIMESTAMPTZ DEFAULT now(),
  reviewed_by           UUID REFERENCES users(id),
  reviewed_at           TIMESTAMPTZ,
  review_decision       TEXT CHECK (review_decision IN ('REPLAY', 'DISCARD', 'ESCALATE', 'PENDING')),
  replayed_at           TIMESTAMPTZ,
  replay_result         TEXT,
  is_closed             BOOLEAN DEFAULT FALSE
);
```

### 4.2 Redis State Keys (Deterministic Namespace)

```
schema:version:active:{event_type}              → Current active schema version string
schema:compiled:{event_type}:{version}          → Compiled validator object (binary) · TTL = 1h refresh
schema:dedup:{event_id}                         → Deduplication flag · TTL = 300s (OTP events) / 86400s (others)
schema:event_types:index                        → Set of all active event type strings
schema:deprecated:{event_type}                  → Deprecation flag with sunset timestamp
schema:validation:rate:{producer_service}       → Validation throughput counter · TTL = 60s
schema:dlq:pending_count                        → Gauge: unreviewed DLQ entries
schema:quarantine:pending_count                 → Gauge: unreviewed quarantine entries
schema:version:mismatch:{event_type}            → Counter of version mismatches in last hour · TTL = 3600s
```

---

## SECTION 5 — GOVERNED EVENT TYPE REGISTRY

All phone events governed by this agent. Each event type has a registered JSON schema that must be ACTIVE before the event may traverse the bus.

### 5.1 Complete Event Type Manifest (SEALED)

| Event Type | Owning Service | Contract ID Required | Tenant ID Required | PII Fields (hash only) |
|---|---|---|---|---|
| `otp.issued` | PHONE_API_CONTRACT_REGISTRY_AGENT | YES | YES | `phone_hash`, `device_fingerprint` |
| `otp.verified` | PHONE_API_CONTRACT_REGISTRY_AGENT | YES | YES | `phone_hash` |
| `otp.failed` | PHONE_API_CONTRACT_REGISTRY_AGENT | YES | YES | `phone_hash` |
| `otp.expired` | PHONE_API_CONTRACT_REGISTRY_AGENT | YES | YES | `phone_hash` |
| `otp.attempt_limit_reached` | PHONE_API_CONTRACT_REGISTRY_AGENT | YES | YES | `phone_hash` |
| `phone.registered` | User Service | NO | YES | `phone_hash` |
| `phone.verified` | PHONE_API_CONTRACT_REGISTRY_AGENT | NO | YES | `phone_hash` |
| `phone.blacklisted` | Admin Governance Service | NO | NO | `phone_hash` |
| `phone.unblacklisted` | Admin Governance Service | NO | NO | `phone_hash` |
| `phone.trust_score_updated` | PHONE_API_CONTRACT_REGISTRY_AGENT | NO | YES | `phone_hash` |
| `trust_binding.initiated` | PHONE_API_CONTRACT_REGISTRY_AGENT | YES | YES | `parent_phone_hash` |
| `trust_binding.otp_dispatched` | PHONE_API_CONTRACT_REGISTRY_AGENT | YES | YES | `parent_phone_hash` |
| `trust_binding.activated` | PHONE_API_CONTRACT_REGISTRY_AGENT | YES | YES | `parent_phone_hash` |
| `trust_binding.terminated` | PHONE_API_CONTRACT_REGISTRY_AGENT | NO | YES | `parent_phone_hash` |
| `phone.abuse` | PHONE_API_CONTRACT_REGISTRY_AGENT | NO | YES | `phone_hash` |
| `phone.abuse.resolved` | Admin Governance Service | NO | YES | `phone_hash` |
| `campaign.sms.dispatched` | PHONE_API_CONTRACT_REGISTRY_AGENT | YES | YES | — |
| `campaign.sms.delivery_report` | Notification Service | YES | YES | — |
| `provider.failover.triggered` | PHONE_API_CONTRACT_REGISTRY_AGENT | NO | NO | — |
| `provider.health.degraded` | PHONE_API_CONTRACT_REGISTRY_AGENT | NO | NO | — |
| `provider.health.restored` | PHONE_API_CONTRACT_REGISTRY_AGENT | NO | NO | — |
| `phone.contract.state_changed` | PHONE_API_CONTRACT_REGISTRY_AGENT | YES | YES | — |
| `fraud.signal.phone` | Fraud Detection Engine | NO | YES | `phone_hash` |
| `phone.dnd.registered` | Admin Governance Service | NO | YES | `phone_hash` |
| `phone.dlt.violation` | PHONE_API_CONTRACT_REGISTRY_AGENT | YES | YES | — |

**Total Governed Event Types: 25**  
Unregistered event type on `phone.events` topic → IMMEDIATE QUARANTINE

---

## SECTION 6 — JSON SCHEMA DEFINITIONS (ALL 25 EVENT TYPES)

### 6.1 Base Envelope Schema (Required by ALL events)

Every phone event MUST include this base envelope. Validation fails if any base field is absent or malformed.

```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "$id": "phone_event_base_envelope",
  "type": "object",
  "required": [
    "event_id",
    "event_type",
    "schema_version",
    "producer_service",
    "event_timestamp",
    "idempotency_key"
  ],
  "properties": {
    "event_id": {
      "type": "string",
      "format": "uuid",
      "description": "Globally unique event identifier (UUID v4)"
    },
    "event_type": {
      "type": "string",
      "minLength": 3,
      "maxLength": 80,
      "pattern": "^[a-z][a-z0-9]*([._][a-z][a-z0-9]*)*$",
      "description": "Dot-notated event type string matching registry"
    },
    "schema_version": {
      "type": "string",
      "pattern": "^\\d+\\.\\d+\\.\\d+$",
      "description": "Semantic version of the schema this payload conforms to"
    },
    "producer_service": {
      "type": "string",
      "minLength": 3,
      "maxLength": 100,
      "description": "Canonical service name of the Kafka producer"
    },
    "event_timestamp": {
      "type": "string",
      "format": "date-time",
      "description": "ISO 8601 UTC timestamp of event creation"
    },
    "idempotency_key": {
      "type": "string",
      "minLength": 16,
      "maxLength": 128,
      "description": "Unique key for deduplication within the dedup window"
    },
    "tenant_id": {
      "type": ["string", "null"],
      "format": "uuid",
      "description": "Null only for platform-scoped events (e.g., provider health)"
    },
    "correlation_id": {
      "type": ["string", "null"],
      "format": "uuid",
      "description": "Trace correlation ID for distributed tracing (OpenTelemetry)"
    }
  },
  "additionalProperties": true
}
```

### 6.2 PII Field Constraint (Enforced Across ALL Events)

```json
{
  "$id": "phone_pii_field_constraint",
  "description": "All phone_hash fields must be SHA-256 hex strings (64 chars). E.164 plaintext is a QUARANTINE-level violation.",
  "phone_hash": {
    "type": "string",
    "pattern": "^[a-f0-9]{64}$",
    "description": "SHA-256 hex digest of E.164 normalized phone number"
  },
  "parent_phone_hash": {
    "type": "string",
    "pattern": "^[a-f0-9]{64}$"
  },
  "device_fingerprint": {
    "type": "string",
    "minLength": 16,
    "maxLength": 256,
    "description": "Opaque device fingerprint string — no raw identifiers"
  }
}
```

> **QUARANTINE TRIGGER:** If any event contains a field matching the E.164 pattern `^\+?[1-9]\d{7,14}$` in any string value — regardless of field name — the event is immediately routed to `phone.events.quarantine` and a `CRITICAL` security alert is emitted.

### 6.3 Core Event Schemas

```json
{
  "$id": "otp.issued/1.0.0",
  "allOf": [{ "$ref": "phone_event_base_envelope" }],
  "required": [
    "event_id", "event_type", "schema_version", "producer_service",
    "event_timestamp", "idempotency_key", "tenant_id",
    "reference_id", "phone_hash", "purpose", "contract_id",
    "provider_id", "expires_at", "ip_address"
  ],
  "properties": {
    "event_type":    { "const": "otp.issued" },
    "schema_version":{ "const": "1.0.0" },
    "reference_id":  { "type": "string", "format": "uuid" },
    "phone_hash":    { "type": "string", "pattern": "^[a-f0-9]{64}$" },
    "purpose": {
      "type": "string",
      "enum": ["LOGIN", "REGISTER", "TRUST_BIND", "TX_CONFIRM", "RESET"]
    },
    "contract_id":   { "type": "string", "format": "uuid" },
    "provider_id":   { "type": "string", "enum": ["PROV-01", "PROV-02", "PROV-03"] },
    "expires_at":    { "type": "string", "format": "date-time" },
    "ip_address":    { "type": "string", "format": "ipv4" },
    "device_fingerprint": { "type": ["string","null"], "minLength": 16, "maxLength": 256 }
  },
  "additionalProperties": false
}
```

```json
{
  "$id": "otp.verified/1.0.0",
  "allOf": [{ "$ref": "phone_event_base_envelope" }],
  "required": [
    "event_id", "event_type", "schema_version", "producer_service",
    "event_timestamp", "idempotency_key", "tenant_id",
    "reference_id", "phone_hash", "verified_at",
    "attempt_count", "contract_id"
  ],
  "properties": {
    "event_type":     { "const": "otp.verified" },
    "schema_version": { "const": "1.0.0" },
    "reference_id":   { "type": "string", "format": "uuid" },
    "phone_hash":     { "type": "string", "pattern": "^[a-f0-9]{64}$" },
    "verified_at":    { "type": "string", "format": "date-time" },
    "attempt_count":  { "type": "integer", "minimum": 1, "maximum": 3 },
    "contract_id":    { "type": "string", "format": "uuid" },
    "trust_score_delta": { "type": "integer", "minimum": -100, "maximum": 100 }
  },
  "additionalProperties": false
}
```

```json
{
  "$id": "otp.failed/1.0.0",
  "allOf": [{ "$ref": "phone_event_base_envelope" }],
  "required": [
    "event_id", "event_type", "schema_version", "producer_service",
    "event_timestamp", "idempotency_key", "tenant_id",
    "reference_id", "phone_hash", "reason", "attempt_count", "contract_id"
  ],
  "properties": {
    "event_type":     { "const": "otp.failed" },
    "schema_version": { "const": "1.0.0" },
    "reference_id":   { "type": "string", "format": "uuid" },
    "phone_hash":     { "type": "string", "pattern": "^[a-f0-9]{64}$" },
    "reason": {
      "type": "string",
      "enum": ["INVALID_OTP", "EXPIRED", "MAX_ATTEMPTS_EXCEEDED", "CONTRACT_INVALID", "DELIVERY_FAILED"]
    },
    "attempt_count":  { "type": "integer", "minimum": 0, "maximum": 3 },
    "contract_id":    { "type": "string", "format": "uuid" }
  },
  "additionalProperties": false
}
```

```json
{
  "$id": "trust_binding.activated/1.0.0",
  "allOf": [{ "$ref": "phone_event_base_envelope" }],
  "required": [
    "event_id", "event_type", "schema_version", "producer_service",
    "event_timestamp", "idempotency_key", "tenant_id",
    "binding_id", "student_user_id", "parent_phone_hash",
    "binding_type", "activated_at", "consent_timestamp",
    "consent_ip", "contract_id"
  ],
  "properties": {
    "event_type":       { "const": "trust_binding.activated" },
    "schema_version":   { "const": "1.0.0" },
    "binding_id":       { "type": "string", "format": "uuid" },
    "student_user_id":  { "type": "string", "format": "uuid" },
    "parent_phone_hash":{ "type": "string", "pattern": "^[a-f0-9]{64}$" },
    "binding_type": {
      "type": "string",
      "enum": ["GUARDIAN", "PARENT_PRIMARY", "PARENT_SECONDARY"]
    },
    "activated_at":     { "type": "string", "format": "date-time" },
    "consent_timestamp":{ "type": "string", "format": "date-time" },
    "consent_ip":       { "type": "string", "format": "ipv4" },
    "contract_id":      { "type": "string", "format": "uuid" },
    "immutable_archive_ref": { "type": ["string","null"], "description": "WORM archive record ID — populated after archive write" }
  },
  "additionalProperties": false
}
```

```json
{
  "$id": "phone.abuse/1.0.0",
  "allOf": [{ "$ref": "phone_event_base_envelope" }],
  "required": [
    "event_id", "event_type", "schema_version", "producer_service",
    "event_timestamp", "idempotency_key",
    "phone_hash", "signal_type", "severity", "auto_action_taken", "detected_at"
  ],
  "properties": {
    "event_type":       { "const": "phone.abuse" },
    "schema_version":   { "const": "1.0.0" },
    "phone_hash":       { "type": "string", "pattern": "^[a-f0-9]{64}$" },
    "signal_type": {
      "type": "string",
      "enum": ["OTP_FLOOD","VELOCITY_BREACH","SIM_SWAP_SUSPECT","PUMPING_PATTERN","TEMPLATE_TAMPER","BLACKLIST_HIT","TRUST_BIND_MISMATCH"]
    },
    "severity": {
      "type": "string",
      "enum": ["LOW", "MEDIUM", "HIGH", "CRITICAL"]
    },
    "auto_action_taken":{
      "type": "string",
      "enum": ["NONE", "SUSPENDED", "QUARANTINED", "BLOCKED", "CONTRACT_REVOKED"]
    },
    "detected_at":      { "type": "string", "format": "date-time" },
    "metadata":         { "type": "object", "additionalProperties": true },
    "tenant_id":        { "type": ["string","null"], "format": "uuid" }
  },
  "additionalProperties": false
}
```

```json
{
  "$id": "provider.failover.triggered/1.0.0",
  "allOf": [{ "$ref": "phone_event_base_envelope" }],
  "required": [
    "event_id", "event_type", "schema_version", "producer_service",
    "event_timestamp", "idempotency_key",
    "from_provider", "to_provider", "reason", "failed_at", "circuit_breaker_id"
  ],
  "properties": {
    "event_type":         { "const": "provider.failover.triggered" },
    "schema_version":     { "const": "1.0.0" },
    "from_provider":      { "type": "string", "enum": ["PROV-01","PROV-02","PROV-03"] },
    "to_provider":        { "type": "string", "enum": ["PROV-01","PROV-02","PROV-03","NONE"] },
    "reason":             { "type": "string", "maxLength": 500 },
    "failed_at":          { "type": "string", "format": "date-time" },
    "circuit_breaker_id": { "type": "string", "minLength": 8, "maxLength": 64 },
    "consecutive_failures":{ "type": "integer", "minimum": 1 }
  },
  "additionalProperties": false
}
```

> **Schemas for all 25 event types follow the same structural pattern above.**  
> Each event type must have a `$id` matching `{event_type}/{schema_version}` exactly.  
> Any deviation → schema registration REJECTED by this agent.

---

## SECTION 7 — VALIDATION RULE ENGINE

### 7.1 Validation Rule Categories (SEALED)

```
CATEGORY-V01  : STRUCTURAL    — JSON well-formedness, required fields, type checking
CATEGORY-V02  : SEMANTIC      — Business logic constraints (enum values, contract linkage)
CATEGORY-V03  : PII_SAFETY    — Phone number format enforcement (hash only), field scan
CATEGORY-V04  : TEMPORAL      — Timestamp sanity, ordering constraints, expiry logic
CATEGORY-V05  : DEDUPLICATION — Idempotency key uniqueness within dedup window
CATEGORY-V06  : VERSIONING    — Schema version match, backward compatibility check
CATEGORY-V07  : SIZE_BOUNDS   — Payload size, field length, array cardinality limits
CATEGORY-V08  : TENANT_SCOPE  — Tenant ID presence for tenant-scoped events
CATEGORY-V09  : CONTRACT_LINK — Contract ID resolution against PHONE_API_CONTRACT_REGISTRY_AGENT
CATEGORY-V10  : SEQUENCE      — Event ordering (e.g., otp.verified must follow otp.issued)
```

### 7.2 Rule-to-Failure-Code Mapping (LOCKED)

| Failure Code | Category | Trigger | Disposition |
|---|---|---|---|
| `V01-MALFORMED_JSON` | V01 | Payload is not valid JSON | DLQ |
| `V01-MISSING_REQUIRED` | V01 | Required field absent | DLQ |
| `V01-TYPE_MISMATCH` | V01 | Field type does not match schema | DLQ |
| `V01-ENUM_VIOLATION` | V01 | Enum field contains undeclared value | DLQ |
| `V02-UNKNOWN_EVENT_TYPE` | V02 | Event type not in registry | QUARANTINE |
| `V02-CONTRACT_NOT_ACTIVE` | V02 | `contract_id` references non-ACTIVE contract | DLQ |
| `V02-CONTRACT_NOT_FOUND` | V02 | `contract_id` UUID not found in registry | QUARANTINE |
| `V03-PLAINTEXT_PHONE_DETECTED` | V03 | E.164 pattern found in any string field | QUARANTINE + CRITICAL ALERT |
| `V03-HASH_FORMAT_INVALID` | V03 | Phone hash is not 64-char hex | DLQ |
| `V04-TIMESTAMP_FUTURE` | V04 | `event_timestamp` > server_now + 5min | DLQ |
| `V04-TIMESTAMP_STALE` | V04 | `event_timestamp` < server_now - 5min | DLQ |
| `V04-SEQUENCE_VIOLATED` | V04 | `otp.verified` with no prior `otp.issued` in window | QUARANTINE |
| `V05-DUPLICATE_IDEMPOTENCY_KEY` | V05 | Same idempotency key seen within dedup window | DISCARD (no DLQ, counter++) |
| `V06-SCHEMA_VERSION_UNKNOWN` | V06 | `schema_version` not in registry for event type | DLQ |
| `V06-SCHEMA_VERSION_REVOKED` | V06 | `schema_version` is in REVOKED state | QUARANTINE |
| `V06-SCHEMA_VERSION_DEPRECATED` | V06 | `schema_version` is in DEPRECATED state | DLQ + WARN producer |
| `V07-PAYLOAD_TOO_LARGE` | V07 | Payload > 100 KB | DLQ |
| `V07-STRING_TOO_LONG` | V07 | String field exceeds max length | DLQ |
| `V08-MISSING_TENANT_ID` | V08 | Tenant-scoped event missing `tenant_id` | DLQ |
| `V09-CONTRACT_ID_REQUIRED` | V09 | OTP event missing `contract_id` | DLQ |
| `V10-SEQUENCE_ORPHAN` | V10 | Verified/failed event with no matching issued event | QUARANTINE |

### 7.3 Validation Decision Matrix

```
Decision : PASS
Condition: All rules pass · schema version ACTIVE · no PII violation

Decision : FAIL → DLQ
Condition: Structural violations · type mismatches · missing required fields ·
           deprecated schema version · timestamp stale · missing tenant

Decision : QUARANTINE
Condition: Unknown event type · REVOKED schema version · plaintext phone detected ·
           contract not found · sequence orphan · contract integrity violation

Decision : DISCARD
Condition: Duplicate idempotency key within dedup window (not an error — expected)

Decision : VERSION_MISMATCH → Version Negotiation Handler
Condition: Schema version declared in payload does not match registry ACTIVE version
           BUT is within BACKWARD_COMPATIBLE range per compatibility matrix
           → Attempt auto-upgrade via migration_transformer_ref
           → If transformer succeeds: re-validate at target version → PASS or FAIL
           → If transformer absent: DLQ
```

---

## SECTION 8 — SCHEMA VERSIONING GOVERNANCE

### 8.1 Versioning Rules (NON-NEGOTIABLE)

```
RULE-SV-01  : All schemas use Semantic Versioning: MAJOR.MINOR.PATCH
RULE-SV-02  : PATCH version bump: Allowable for documentation-only changes,
              description updates, additional metadata — no structural change
RULE-SV-03  : MINOR version bump: Allowable for adding OPTIONAL fields only.
              Adding optional fields is BACKWARD COMPATIBLE.
              Removing optional fields is FORWARD BREAKING — requires MAJOR bump.
RULE-SV-04  : MAJOR version bump: Required for any of the following:
              (a) Adding or removing required fields
              (b) Changing a field's type
              (c) Narrowing an enum (removing values)
              (d) Changing additionalProperties from true to false
              (e) Renaming any field
              (f) Changing field format constraints
RULE-SV-05  : A new schema version begins in DRAFT state.
              DRAFT → ACTIVE requires Platform Admin approval.
              No automated promotion to ACTIVE.
RULE-SV-06  : When a new MAJOR version is activated, the prior version enters
              DEPRECATED state — not immediately REVOKED.
              Deprecation window = 30 days minimum.
              During deprecation: producers receive WARN, consumers still receive events.
RULE-SV-07  : After deprecation window: prior version enters REVOKED state.
              Revoked version events → QUARANTINE (not DLQ).
RULE-SV-08  : Schema REVOCATION requires Compliance Officer sign-off if the
              event type handles PII fields.
RULE-SV-09  : Maximum 2 concurrent ACTIVE versions per event type.
              Third version activation requires prior version deprecation first.
RULE-SV-10  : Schema changes to otp.issued, otp.verified, trust_binding.activated
              require dual-approval (Platform Admin + Compliance Officer).
              These are HIGH-TRUST events. Single-approval is FORBIDDEN.
```

### 8.2 Schema Evolution Compatibility Decision Tree

```
Is the change adding an optional field?
  YES → MINOR bump · BACKWARD COMPATIBLE · single approval
  NO  ↓

Is the change removing any field (required or optional)?
  YES → MAJOR bump · BREAKING · dual approval required for PII events
  NO  ↓

Is the change modifying a field type, format, or enum values?
  YES → MAJOR bump · BREAKING · dual approval
  NO  ↓

Is the change modifying description or documentation only?
  YES → PATCH bump · COMPATIBLE · single approval
  NO  → Escalate to schema governance review
```

---

## SECTION 9 — SEQUENCE INTEGRITY ENFORCEMENT

Phone events have ordering dependencies. This agent enforces sequence integrity at the consumer guard level. Violations route to QUARANTINE, not DLQ.

### 9.1 Required Event Sequences (LOCKED)

```
SEQUENCE-01  : OTP LIFECYCLE
  otp.issued → [otp.verified | otp.failed | otp.expired | otp.attempt_limit_reached]
  Rule: otp.verified MUST be preceded by otp.issued with same reference_id
        within the OTP TTL window (300 seconds)
  Violation: otp.verified with no matching otp.issued → V10-SEQUENCE_ORPHAN → QUARANTINE

SEQUENCE-02  : TRUST BINDING LIFECYCLE
  trust_binding.initiated → trust_binding.otp_dispatched → trust_binding.activated
  Rule: trust_binding.activated MUST be preceded by trust_binding.otp_dispatched
        with same binding_id within the consent window (86400 seconds)
  Violation: trust_binding.activated with no prior otp_dispatched → V10-SEQUENCE_ORPHAN → QUARANTINE

SEQUENCE-03  : PROVIDER FAILOVER SEQUENCE
  provider.health.degraded → provider.failover.triggered → provider.health.restored
  Rule: provider.failover.triggered SHOULD be preceded by provider.health.degraded
        within 120 seconds
  Violation: WARN only (provider events may arrive out of order due to partition skew)
             → log V04-SEQUENCE_VIOLATED at WARN level, not QUARANTINE

SEQUENCE-04  : PHONE ABUSE ESCALATION
  phone.abuse (MEDIUM/HIGH) → [phone.blacklisted | phone.abuse.resolved]
  Rule: phone.abuse events at CRITICAL severity MUST be followed by either
        phone.blacklisted or phone.abuse.resolved within 1 hour
  Monitoring: Alert fired if unresolved CRITICAL abuse signal > 60 min
              No sequence violation (async resolution is valid)

SEQUENCE-05  : CONTRACT STATE TRANSITIONS
  phone.contract.state_changed must carry a valid (from_state → to_state) pair
  matching the state machine defined in PHONE_API_CONTRACT_REGISTRY_AGENT
  Invalid transition pair → V02-ENUM_VIOLATION → DLQ
```

### 9.2 Sequence State Redis Keys

```
seq:otp:{reference_id}                  → TTL = 300s · stores "issued" state
seq:trust_bind:{binding_id}             → TTL = 86400s · stores "otp_dispatched" state
seq:abuse_critical:{phone_hash}         → TTL = 3600s · stores unresolved CRITICAL count
```

---

## SECTION 10 — DEAD LETTER QUEUE GOVERNANCE

### 10.1 DLQ Review Rules (NON-NEGOTIABLE)

```
RULE-DLQ-01  : All DLQ entries must be reviewed within 72 hours of routing
RULE-DLQ-02  : DLQ review requires Platform Admin role minimum
RULE-DLQ-03  : Review decision options: REPLAY | DISCARD | ESCALATE_TO_QUARANTINE
RULE-DLQ-04  : REPLAY requires: (a) root cause identified, (b) schema fixed or
               confirmed as transient, (c) replay approved in dlq_manifest.review_decision
RULE-DLQ-05  : Replay injects event back to phone.events topic with a new
               idempotency_key prefixed with REPLAY_{original_idempotency_key}
               (prevents dedup filter blocking legitimate replay)
RULE-DLQ-06  : Maximum 3 replays per original event_id — 4th attempt requires
               Compliance Officer approval
RULE-DLQ-07  : DLQ entries older than 30 days with review_decision = PENDING
               trigger an automated escalation alert to Compliance Officer
RULE-DLQ-08  : Payload snapshots in dlq_manifest have all PII fields replaced with
               [REDACTED] before storage — this is irreversible
RULE-DLQ-09  : DLQ volume alerts:
               >100 entries in 1 hour → HIGH alert to Platform Admin
               >500 entries in 1 hour → CRITICAL alert + page on-call
RULE-DLQ-10  : Any DLQ entry carrying failure code V03-PLAINTEXT_PHONE_DETECTED
               is automatically escalated to phone.events.quarantine with
               CRITICAL severity — DLQ entry is closed
```

### 10.2 Quarantine Review Rules (STRICTER)

```
RULE-QR-01  : Quarantine entries require Compliance Officer review (not just Platform Admin)
RULE-QR-02  : Quarantine entries retained 90 days minimum (forensic window)
RULE-QR-03  : REPLAY from quarantine requires dual sign-off (Compliance + Platform Admin)
RULE-QR-04  : V03-PLAINTEXT_PHONE_DETECTED quarantine entries trigger:
              (a) Wazuh SIEM security incident
              (b) Automated notification to Data Protection Officer (DPO) role if configured
              (c) Producing service is flagged for security audit
RULE-QR-05  : V02-UNKNOWN_EVENT_TYPE quarantine triggers investigation of producing
              service — unexpected event types indicate unauthorized code changes
```

---

## SECTION 11 — API CONTRACT (OpenAPI 3.1)

```yaml
openapi: 3.1.0
info:
  title: PHONE_EVENT_SCHEMA_VALIDATION_AGENT
  version: "1.0"
  description: |
    Schema registry and validation governance API for all phone-surface
    Kafka events in Ecoskiller. ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
    — ANTIGRAVITY MODULE.

servers:
  - url: https://api.ecoskiller.com/trust/phone-schema
    description: Production (internal service only)
  - url: https://api.ecoskiller.staging/trust/phone-schema
    description: Staging

security:
  - bearerAuth: []

paths:

  /schemas:
    get:
      summary: List all registered event type schemas
      operationId: listSchemas
      x-rbac-roles: [PLATFORM_ADMIN, COMPLIANCE_OFFICER, OPS_INTERNAL, CI_CD_VALIDATOR]
      parameters:
        - name: event_type
          in: query
          schema: { type: string }
        - name: state
          in: query
          schema: { type: string, enum: [DRAFT, ACTIVE, DEPRECATED, REVOKED] }
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/SchemaListResponse'

  /schemas/{event_type}/versions:
    get:
      summary: Get all versions for an event type
      operationId: getSchemaVersions
      x-rbac-roles: [PLATFORM_ADMIN, COMPLIANCE_OFFICER, OPS_INTERNAL]
      parameters:
        - name: event_type
          in: path
          required: true
          schema: { type: string }
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/SchemaVersionListResponse'

  /schemas/{event_type}/versions/{version}:
    post:
      summary: Register a new schema version (DRAFT state)
      operationId: registerSchemaVersion
      x-rbac-roles: [PLATFORM_ADMIN]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/RegisterSchemaVersionRequest'
      responses:
        "201": { description: Schema version registered in DRAFT state }
        "400": { description: Invalid schema or version pattern }
        "409": { description: Version already registered for this event type }

  /schemas/{event_type}/versions/{version}/activate:
    post:
      summary: Activate a DRAFT schema version (requires approval)
      operationId: activateSchemaVersion
      x-rbac-roles: [PLATFORM_ADMIN]
      description: |
        For PII event types (otp.issued, otp.verified, trust_binding.activated),
        BOTH Platform Admin and Compliance Officer signatures are required.
        A second approval call with COMPLIANCE_OFFICER role is mandatory.
      responses:
        "200": { description: Schema activated · prior version enters DEPRECATED }
        "400": { description: Schema not in DRAFT state }
        "403": { description: Dual approval required for PII event type }
        "409": { description: Two active versions already exist for event type }

  /schemas/{event_type}/versions/{version}/deprecate:
    post:
      summary: Manually deprecate an active schema version
      operationId: deprecateSchemaVersion
      x-rbac-roles: [PLATFORM_ADMIN, COMPLIANCE_OFFICER]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [reason, sunset_at]
              properties:
                reason: { type: string }
                sunset_at: { type: string, format: date-time }
      responses:
        "200": { description: Schema deprecated · sunset date registered }

  /validate:
    post:
      summary: On-demand payload validation (CI/CD and dev testing)
      operationId: validatePayload
      x-rbac-roles: [PLATFORM_ADMIN, CI_CD_VALIDATOR, DEV_INTERNAL]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ValidatePayloadRequest'
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ValidatePayloadResponse'

  /dlq:
    get:
      summary: List DLQ entries pending review
      operationId: listDlqEntries
      x-rbac-roles: [PLATFORM_ADMIN, COMPLIANCE_OFFICER]
      parameters:
        - name: reviewed
          in: query
          schema: { type: boolean, default: false }
        - name: event_type
          in: query
          schema: { type: string }
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/DlqListResponse'

  /dlq/{dlq_entry_id}/review:
    post:
      summary: Submit review decision for a DLQ entry
      operationId: reviewDlqEntry
      x-rbac-roles: [PLATFORM_ADMIN]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [decision, reason]
              properties:
                decision:
                  type: string
                  enum: [REPLAY, DISCARD, ESCALATE_TO_QUARANTINE]
                reason: { type: string, minLength: 20 }
      responses:
        "200": { description: Review decision recorded }
        "400": { description: Entry already reviewed }
        "403": { description: Max replays reached — escalate to Compliance Officer }

  /quarantine:
    get:
      summary: List quarantine entries
      operationId: listQuarantineEntries
      x-rbac-roles: [COMPLIANCE_OFFICER, PLATFORM_ADMIN]
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/QuarantineListResponse'

  /compatibility/{event_type}/{from_version}/{to_version}:
    get:
      summary: Check compatibility between two schema versions
      operationId: checkCompatibility
      x-rbac-roles: [PLATFORM_ADMIN, CI_CD_VALIDATOR, DEV_INTERNAL]
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/CompatibilityCheckResponse'

  /health:
    get:
      summary: Agent health and schema registry readiness
      operationId: healthCheck
      x-rbac-roles: [OPS_INTERNAL, PLATFORM_ADMIN]
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/AgentHealthResponse'

components:
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT

  schemas:
    RegisterSchemaVersionRequest:
      type: object
      required: [schema_version, json_schema, is_backward_compatible]
      properties:
        schema_version:          { type: string, pattern: "^\\d+\\.\\d+\\.\\d+$" }
        json_schema:             { type: object }
        asyncapi_fragment:       { type: object }
        is_backward_compatible:  { type: boolean }
        breaking_change_summary: { type: string }
        migration_notes:         { type: string }

    ValidatePayloadRequest:
      type: object
      required: [event_type, schema_version, payload]
      properties:
        event_type:     { type: string }
        schema_version: { type: string }
        payload:        { type: object }

    ValidatePayloadResponse:
      type: object
      properties:
        decision:        { type: string, enum: [PASS, FAIL, QUARANTINE] }
        failure_codes:   { type: array, items: { type: string } }
        failure_detail:  { type: object }
        validation_latency_ms: { type: integer }

    AgentHealthResponse:
      type: object
      properties:
        status:                  { type: string, enum: [HEALTHY, DEGRADED, CRITICAL] }
        schema_registry_ready:   { type: boolean }
        redis_connected:         { type: boolean }
        kafka_producer_ready:    { type: boolean }
        kafka_consumer_ready:    { type: boolean }
        active_schema_count:     { type: integer }
        dlq_pending_count:       { type: integer }
        quarantine_pending_count:{ type: integer }
        validation_rate_per_sec: { type: number }
        phone_schema_ready:      { type: boolean }
```

---

## SECTION 12 — EVENT SCHEMA (AsyncAPI 2.6) — AGENT'S OWN EVENTS

```yaml
asyncapi: 2.6.0
info:
  title: PHONE_EVENT_SCHEMA_VALIDATION_AGENT Internal Events
  version: "1.0"

channels:

  phone.schema.audit:
    description: Validation decision record for every message processed
    publish:
      message:
        $ref: '#/components/messages/SchemaValidationAudit'

  phone.schema.mutations:
    description: Schema version lifecycle changes
    publish:
      message:
        oneOf:
          - $ref: '#/components/messages/SchemaVersionRegistered'
          - $ref: '#/components/messages/SchemaVersionActivated'
          - $ref: '#/components/messages/SchemaVersionDeprecated'
          - $ref: '#/components/messages/SchemaVersionRevoked'

components:
  messages:

    SchemaValidationAudit:
      payload:
        type: object
        required: [audit_id, event_id, event_type, schema_version, validation_point,
                   decision, validated_at, producer_service, processing_latency_ms]
        properties:
          audit_id:             { type: string, format: uuid }
          event_id:             { type: string }
          event_type:           { type: string }
          schema_version:       { type: string }
          validation_point:     { type: string, enum: [PRODUCER, CONSUMER] }
          consumer_service:     { type: ["string","null"] }
          decision:             { type: string, enum: [PASS, FAIL, QUARANTINE, DISCARD, VERSION_MISMATCH] }
          failure_codes:        { type: array, items: { type: string } }
          failure_detail:       { type: object }
          payload_size_bytes:   { type: integer }
          processing_latency_ms:{ type: integer }
          tenant_id:            { type: ["string","null"], format: uuid }
          producer_service:     { type: string }
          kafka_partition:      { type: integer }
          kafka_offset:         { type: integer }
          dlq_routed:           { type: boolean }
          quarantine_routed:    { type: boolean }
          validated_at:         { type: string, format: date-time }

    SchemaVersionActivated:
      payload:
        type: object
        required: [event_type, schema_version, activated_at, approved_by, prior_active_version]
        properties:
          event_type:           { type: string }
          schema_version:       { type: string }
          activated_at:         { type: string, format: date-time }
          approved_by:          { type: string, format: uuid }
          prior_active_version: { type: ["string","null"] }
          is_breaking_change:   { type: boolean }
```

---

## SECTION 13 — CI/CD INTEGRATION (LOCKED)

This agent provides a CI/CD validation endpoint used by the pipeline to enforce schema contracts before any service deployment. This is the **runtime enforcement** of Ecoskiller's Contract-First Execution Law.

### 13.1 CI Pipeline Schema Gate (NON-NEGOTIABLE)

```yaml
# Required stage in ALL CI pipelines for phone-surface services
# Absence of this gate → STOP pipeline → REPORT SCHEMA-GATE-MISSING

schema-validation-gate:
  stage: contract-validation          # Runs BEFORE build stage
  script:
    - |
      # Validate all event schemas declared by this service
      for schema_file in ./schemas/phone/*.json; do
        event_type=$(jq -r '."$id"' $schema_file | cut -d/ -f1)
        schema_version=$(jq -r '."$id"' $schema_file | cut -d/ -f2)
        
        response=$(curl -s -X POST \
          "${PHONE_SCHEMA_AGENT_URL}/validate-schema-registration" \
          -H "Authorization: Bearer ${CI_SERVICE_TOKEN}" \
          -H "Content-Type: application/json" \
          -d "{\"event_type\": \"${event_type}\", \"schema_version\": \"${schema_version}\", \"json_schema\": $(cat $schema_file)}")
        
        decision=$(echo $response | jq -r '.decision')
        
        if [ "$decision" != "COMPATIBLE" ]; then
          echo "SCHEMA GATE FAILED: ${event_type} v${schema_version}"
          echo "Reason: $(echo $response | jq -r '.reason')"
          exit 1
        fi
      done
      echo "SCHEMA GATE PASSED"
  rules:
    - changes:
        - schemas/phone/**
        - src/**/*.java      # Any source change triggers schema re-check
```

### 13.2 Schema Contract Test Suite Requirements

```
Required test categories for every phone-surface service:
  ✓ POSITIVE_TEST    : Valid payload matching active schema → expect PASS
  ✓ MISSING_REQUIRED : Remove each required field → expect FAIL with V01-MISSING_REQUIRED
  ✓ TYPE_VIOLATION   : Wrong type on each field → expect FAIL with V01-TYPE_MISMATCH
  ✓ PII_LEAK_TEST    : Insert E.164 phone in every string field → expect QUARANTINE
  ✓ ENUM_VIOLATION   : Invalid enum value → expect FAIL with V01-ENUM_VIOLATION
  ✓ STALE_TIMESTAMP  : event_timestamp > 5min ago → expect FAIL with V04-TIMESTAMP_STALE
  ✓ DUPLICATE_KEY    : Reuse idempotency_key within window → expect DISCARD
  ✓ VERSION_MISMATCH : Declare old but backward-compatible version → expect VERSION_MISMATCH + auto-upgrade
  ✓ REVOKED_VERSION  : Declare revoked version → expect QUARANTINE
  ✓ OVERSIZED        : Payload > 100KB → expect FAIL with V07-PAYLOAD_TOO_LARGE

Test absence → STOP pipeline → REPORT SCHEMA-TESTS-MISSING
```

---

## SECTION 14 — OBSERVABILITY (NON-OPTIONAL)

### 14.1 Prometheus Metrics (All Required)

```
# Validation throughput
phone_schema_validations_total{event_type, validation_point, decision}
phone_schema_validation_latency_ms{event_type, validation_point}   → Histogram

# Failure tracking
phone_schema_failures_total{event_type, failure_code, disposition}
phone_schema_dlq_routed_total{event_type, failure_code}
phone_schema_quarantine_routed_total{event_type, failure_code}
phone_schema_discarded_duplicate_total{event_type}

# PII safety — CRITICAL metric
phone_schema_pii_violation_total{event_type, producer_service}     → Alert on ANY > 0

# Schema registry health
phone_schema_active_version_count{event_type}
phone_schema_deprecated_version_count{event_type}
phone_schema_dlq_pending_total                                     → Gauge
phone_schema_quarantine_pending_total                              → Gauge
phone_schema_version_mismatch_total{event_type, declared_version}

# Sequence integrity
phone_schema_sequence_violations_total{sequence_id, violation_type}

# Agent internals
phone_schema_agent_kafka_consumer_lag{topic, partition}            → Gauge
phone_schema_agent_redis_cache_hit_rate                            → Gauge
phone_schema_agent_registry_db_query_latency_ms                   → Histogram
```

### 14.2 Alerting Rules

```
ALERT PhonePiiViolationDetected
  expr: increase(phone_schema_pii_violation_total[5m]) > 0
  severity: CRITICAL
  action: Page Compliance Officer · Wazuh SIEM incident · quarantine producing service

ALERT PhoneSchemaQueueBacklog
  expr: phone_schema_dlq_pending_total > 100
  for: 10m
  severity: HIGH
  action: Alert Platform Admin · Slack ops-critical

ALERT PhoneSchemaValidationLatencyHigh
  expr: histogram_quantile(0.95, phone_schema_validation_latency_ms) > 50
  for: 5m
  severity: MEDIUM
  action: Investigate Redis cache miss rate · scale agent replicas

ALERT PhoneSchemaVersionMismatchSpike
  expr: rate(phone_schema_version_mismatch_total[5m]) > 10
  severity: HIGH
  action: Alert producing service team · check deployment version sync

ALERT PhoneSchemaAgentKafkaLag
  expr: phone_schema_agent_kafka_consumer_lag > 5000
  for: 3m
  severity: HIGH
  action: Scale agent · investigate Kafka partition health

ALERT PhoneSchemaUnreviewedQuarantine
  expr: phone_schema_quarantine_pending_total > 0
  for: 1h
  severity: HIGH
  action: Alert Compliance Officer — quarantine entries require review

ALERT PhoneSchemaRegistryUnavailable
  expr: up{job="phone-schema-validation-agent"} == 0
  severity: CRITICAL
  action: Page on-call · ALL phone event consumers halt · incident declared
```

### 14.3 Loki Log Labels (Mandatory)

```
{
  app="phone-schema-validation-agent",
  event_type="...",
  schema_version="...",
  validation_point="PRODUCER|CONSUMER",
  decision="PASS|FAIL|QUARANTINE|DISCARD",
  producer_service="...",
  tenant_id="...",
  failure_code="..."     # omit on PASS
}

PROHIBITION: event_id may appear in logs. phone_hash MUST NOT appear in logs.
             Violation = Wazuh alert.
```

### 14.4 OpenTelemetry Traces (Required Spans)

```
validate_event (root)
  ├── parse_json
  ├── lookup_schema_version (Redis cache → PostgreSQL fallback)
  ├── run_structural_validation
  ├── run_pii_scan
  ├── run_semantic_validation
  │     └── verify_contract_id (calls PHONE_API_CONTRACT_REGISTRY_AGENT via REST)
  ├── run_sequence_check (Redis lookup)
  ├── run_dedup_check (Redis lookup + write)
  ├── route_decision
  │     ├── publish_to_main_topic (on PASS)
  │     ├── publish_to_dlq (on FAIL)
  │     └── publish_to_quarantine (on QUARANTINE)
  └── write_audit_log (async · non-blocking)
```

---

## SECTION 15 — SECURITY ENFORCEMENT

```
SEC-SV-01  : Schema registry write access is restricted to PLATFORM_ADMIN role +
             COMPLIANCE_OFFICER for PII event types — no service account may
             register or activate schemas without human RBAC assignment
SEC-SV-02  : PII field scan runs on EVERY event payload — field name agnostic.
             Any string matching E.164 regex pattern in ANY field → QUARANTINE
SEC-SV-03  : Validation audit logs are append-only in PostgreSQL.
             No UPDATE or DELETE permitted on phone_schema_validation_log.
SEC-SV-04  : DLQ payload snapshots are sanitized before storage:
             All fields listed in phone_event_types.pii_fields are replaced
             with [REDACTED] — this sanitization is irreversible
SEC-SV-05  : Inter-service calls (to PHONE_API_CONTRACT_REGISTRY_AGENT for
             contract verification) use Vault-issued short-lived service tokens.
             Token TTL = 300s. Token renewal is automatic.
SEC-SV-06  : Compiled validator objects in Redis are signed with an HMAC key
             (Vault-managed). Unsigned or tampered validators → reject + re-compile
             from source PostgreSQL schema
SEC-SV-07  : Schema mutation API endpoints log every call (requester, timestamp,
             IP, action) to ClickHouse schema_mutation_audit table
SEC-SV-08  : V03-PLAINTEXT_PHONE_DETECTED events trigger an automated notification
             to the producing service's registered security contact within 60 seconds
SEC-SV-09  : Access to quarantine entries requires COMPLIANCE_OFFICER role and
             creates an audit log entry before content is viewable
SEC-SV-10  : All API endpoints sit behind Kong API Gateway with ModSecurity WAF.
             Rate limit: 500 requests/minute per service account for validation API.
             Schema registry mutation endpoints: 10 requests/minute per user.
```

---

## SECTION 16 — FAILURE HANDLING (DETERMINISTIC)

| Failure Scenario | Agent Action | Recovery Path |
|---|---|---|
| Redis unreachable | Suspend dedup + version cache · fallback to PostgreSQL for all lookups · emit `phone_schema_agent_degraded` | Auto-reconnect · restore cache on recovery |
| PostgreSQL unreachable | HALT all schema lookups · reject all events with SERVICE_UNAVAILABLE · emit CRITICAL alert | DB HA failover (k3s persistent volume) |
| Kafka DLQ topic unreachable | HOLD failed events in in-memory buffer (max 1000 events, 5 min) · emit CRITICAL alert | Auto-flush to DLQ on restore · discard if buffer full |
| PHONE_API_CONTRACT_REGISTRY_AGENT unreachable | Skip contract_id verification · log `V02-CONTRACT_VERIFY_SKIPPED` · allow event to pass with WARN | Alert + retry contract verification async within 30s |
| Compiled validator HMAC mismatch | Discard cached validator · re-compile from PostgreSQL · log security event | Auto-heal from source schema |
| Kafka consumer lag > 10,000 messages | Alert + auto-scale agent replicas (HPA) · emit backpressure signal | HPA handles · manual intervention if lag persists > 15 min |
| Schema activation while consumers are live | Route new events to NEW version · route old-version events to compatibility handler for 30-day deprecation window | Standard deprecation flow |
| All schema versions revoked for event type | QUARANTINE all events of that type · alert Platform Admin immediately | Admin must register and activate a new schema version |

**Fail-closed in all cases. Partial validation is NOT permitted.**

---

## SECTION 17 — DEPLOYMENT SPECIFICATION

### 17.1 Kubernetes Manifest Requirements

```yaml
namespace: ecoskiller-trust

Deployment:
  replicas: 2 (minimum) · 8 (maximum HPA — validation is CPU-bound)
  resources:
    requests: { cpu: 500m, memory: 1Gi }
    limits:   { cpu: 2000m, memory: 2Gi }
  hpa:
    minReplicas: 2
    maxReplicas: 8
    targetCPUUtilizationPercentage: 65
    targetMemoryUtilizationPercentage: 70
  readinessProbe:
    httpGet: { path: /actuator/health/readiness, port: 8080 }
    initialDelaySeconds: 20
    periodSeconds: 10
  livenessProbe:
    httpGet: { path: /actuator/health/liveness, port: 8080 }
    initialDelaySeconds: 40
    periodSeconds: 20
  env:
    - name: KAFKA_BOOTSTRAP_SERVERS
      valueFrom: { configMapKeyRef: { name: kafka-config, key: bootstrap_servers } }
    - name: REDIS_URL
      valueFrom: { secretKeyRef: { name: redis-secret, key: url } }
    - name: POSTGRES_URL
      valueFrom: { secretKeyRef: { name: postgres-secret, key: url } }
    - name: PHONE_CONTRACT_REGISTRY_URL
      valueFrom: { configMapKeyRef: { name: trust-infra-config, key: contract_registry_url } }
    - name: VALIDATOR_HMAC_KEY
      valueFrom: { secretKeyRef: { name: vault-injected-secrets, key: validator_hmac_key } }
    - name: DLQ_TOPIC
      value: "phone.events.dlq"
    - name: QUARANTINE_TOPIC
      value: "phone.events.quarantine"
    - name: PII_SCAN_ENABLED
      value: "true"
    - name: DEDUP_WINDOW_SECONDS_DEFAULT
      value: "86400"
    - name: DEDUP_WINDOW_SECONDS_OTP
      value: "300"
    - name: MAX_PAYLOAD_BYTES
      value: "102400"
    - name: TIMESTAMP_TOLERANCE_SECONDS
      value: "300"
```

### 17.2 Environment Variable Requirements (All Environments)

```
REQUIRED (Vault-injected — never hardcoded):
  POSTGRES_URL, POSTGRES_USERNAME, POSTGRES_PASSWORD
  REDIS_URL, REDIS_PASSWORD
  KAFKA_BOOTSTRAP_SERVERS
  VALIDATOR_HMAC_KEY
  VAULT_ADDR
  OPA_URL
  WAZUH_WEBHOOK_URL

CONFIGURABLE (ConfigMap per environment):
  DLQ_TOPIC                              (default: phone.events.dlq)
  QUARANTINE_TOPIC                       (default: phone.events.quarantine)
  AUDIT_TOPIC                            (default: phone.schema.audit)
  PII_SCAN_ENABLED                       (default: true — NEVER set to false in prod)
  MAX_PAYLOAD_BYTES                      (default: 102400)
  TIMESTAMP_TOLERANCE_SECONDS            (default: 300)
  DEDUP_WINDOW_SECONDS_DEFAULT           (default: 86400)
  DEDUP_WINDOW_SECONDS_OTP               (default: 300)
  VALIDATOR_CACHE_TTL_SECONDS            (default: 3600)
  CONTRACT_VERIFY_TIMEOUT_MS             (default: 2000)
  CONTRACT_VERIFY_SKIP_ON_TIMEOUT        (default: true — log + warn, not block)
  DLQ_ALERT_THRESHOLD_PER_HOUR          (default: 100)
  QUARANTINE_ALERT_THRESHOLD_PENDING    (default: 1 — always alert)
  SEQUENCE_CHECK_ENABLED                (default: true)
```

---

## SECTION 18 — SERVICE DEPENDENCIES (CONTRACT GATES)

### 18.1 Upstream Dependencies (This Agent Requires)

| Service | Dependency Type | Gate Requirement |
|---|---|---|
| PHONE_API_CONTRACT_REGISTRY_AGENT | REST (contract_id verification) | `phone_trust_ready` MUST be TRUE |
| PostgreSQL | Schema registry persistence | `db_ready` MUST be ACTIVE |
| Redis | Compiled validators · dedup · sequence state | Redis cluster MUST be healthy |
| Kafka | Consumer (phone.events) + Producer (DLQ, quarantine, audit) | Event bus MUST be reachable |
| Keycloak | JWT validation for API access | `identity_ready` MUST be ACTIVE |
| OPA | Policy enforcement on schema mutations | `policy_engine_ready` MUST be ACTIVE |
| Vault | HMAC key for validator signing · service tokens | Vault MUST be reachable |
| Wazuh | Security incident push (PII violations, quarantine events) | Available at startup |

### 18.2 Downstream Gate Signal

On successful startup, schema loading, and health verification, this agent produces:

```
phone_schema_ready = TRUE
```

Services that require `phone_schema_ready` before consuming `phone.events`:
- Fraud Detection Engine
- Analytics Service (ClickHouse ingest path for phone events)
- Admin Governance Service
- Parent Dashboard Service
- Notification Service (inbound phone event consumers)
- Immutable Archive Service

**Absence of `phone_schema_ready` → ALL consumers of phone.events topic HALT.**

---

## SECTION 19 — CONTRACT GATE CHECKLIST

All items must be TRUE before agent declares `phone_schema_ready = TRUE`.

```
[ ] phone_event_schema_registry schema migrated (Flyway)
[ ] phone_event_types table seeded with all 25 governed event types
[ ] All 25 event type schemas registered in ACTIVE state in schema registry
[ ] Base envelope schema registered and active
[ ] PII field constraint schema registered and active
[ ] Redis connected · schema version cache populated
[ ] Compiled validators for all active schemas loaded to Redis and HMAC-signed
[ ] Kafka consumer group connected to phone.events topic
[ ] Kafka producer connected · phone.events.dlq topic created
[ ] Kafka producer connected · phone.events.quarantine topic created
[ ] Kafka producer connected · phone.schema.audit topic created
[ ] Kafka producer connected · phone.schema.mutations topic created
[ ] PHONE_API_CONTRACT_REGISTRY_AGENT reachable (health check passes)
[ ] OPA policy bundle loaded · phone_schema policies active
[ ] Keycloak JWT validation config active
[ ] Vault HMAC key injected · test signature passes
[ ] Wazuh webhook endpoint reachable
[ ] Prometheus metrics endpoint /metrics exposed
[ ] Loki log shipping active
[ ] OpenTelemetry collector reachable
[ ] PII scan E.164 regex compiled and tested (must detect +919876543210 as violation)
[ ] Dedup window keys namespace accessible in Redis
[ ] Sequence state keys namespace accessible in Redis
[ ] DLQ manifest table created and accessible
[ ] schema_compatibility_rules table seeded with all declared compatibility pairs
[ ] CI/CD validation endpoint /validate responding to test payload
[ ] Admin Governance Service subscribed to phone.schema.audit (Kafka)
[ ] Analytics Service subscribed to phone.schema.audit (Kafka → ClickHouse)
[ ] All 10 Prometheus alert rules deployed to Alertmanager
```

**All 30 checks must pass → `phone_schema_ready = TRUE` emitted.**  
**Any failure → STOP EXECUTION → REPORT PHONE-SCHEMA-NOT-READY → block all consumers.**

---

## SECTION 20 — MUTATION POLICY

```
ALLOWED        : Registering new event types (with human approval + version bump)
ALLOWED        : Adding MINOR schema versions (new optional fields only)
ALLOWED        : Registering MAJOR schema versions (with dual approval for PII types)
ALLOWED        : Extending the PII field scan regex to detect new phone formats
ALLOWED        : Adding new failure codes to the rule engine (MINOR agent version bump)
ALLOWED        : Adding new governed Kafka topics (human approval + agent version bump)

FORBIDDEN      : Disabling PII scan in production (PII_SCAN_ENABLED = false is a SECURITY VIOLATION)
FORBIDDEN      : Removing any of the 25 governed event types without sunset + migration plan
FORBIDDEN      : Auto-activating schema versions without human approval
FORBIDDEN      : Replaying quarantine entries without Compliance Officer sign-off
FORBIDDEN      : Mutating existing ACTIVE schema fields — always create new version
FORBIDDEN      : Storing plaintext phone numbers in any data store controlled by this agent
FORBIDDEN      : Adding AI/ML judgment to schema validation decisions
               (This agent is deterministic — structural rules only)
FORBIDDEN      : Bypassing sequence integrity checks for any reason
FORBIDDEN      : Setting CONTRACT_VERIFY_SKIP_ON_TIMEOUT to false without
               fallback latency analysis (risk: blocking entire validation pipeline)
```

---

## SECTION 21 — VERSION HISTORY

| Version | Status | Change |
|---|---|---|
| v1.0 | SEALED · ACTIVE | Initial declaration — all 21 sections locked · 25 event types governed · 30-point checklist |

---

**DOCUMENT END**  
**PHONE_EVENT_SCHEMA_VALIDATION_AGENT v1.0**  
**ECOSKILLER — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE — ANTIGRAVITY**  
`SEALED · LOCKED · GOVERNED · ADD-ONLY`
