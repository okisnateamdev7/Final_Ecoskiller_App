# 🔒 AGENT_ACCESS_PERMISSION_FIREWALL
## ECOSKILLER ANTIGRAVITY — TRUST, IDENTITY & SAFEGUARDS CORE
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Classification: SECURITY-CRITICAL · ZERO-TRUST · SYSTEM-WIDE ENFORCEMENT LAYER
### Mutation Policy: ADD-ONLY via Version Bump
### Interpretation Authority: NONE
### Execution Authority: Human Declaration Only

---

## 🔐 EXECUTION LOCK

```
EXECUTION_MODE                  = DETERMINISTIC + VALIDATED + SYNCHRONOUS + APPEND-ONLY
MUTATION_POLICY                 = ADD_ONLY
CREATIVE_INTERPRETATION         = FORBIDDEN
ASSUMPTION_FILLING              = FORBIDDEN
DEFAULT_BEHAVIOR                = DENY_ALL — explicit ALLOW required for every agent action
FAILURE_MODE                    = STOP_EXECUTION + LOG_INCIDENT + BLOCK_REQUEST + ESCALATE
AGENT_VERSION                   = v1.0.0
CLASSIFICATION                  = SECURITY_CRITICAL · ZERO_TRUST_ENFORCEMENT
TRUST_DEFAULT                   = ZERO — no agent, service, or caller is trusted by default
ALLOW_DEFAULT                   = DENY — every access requires an explicit grant in the Permission Matrix
BYPASS_POLICY                   = FORBIDDEN — no agent, admin, or emergency path bypasses this firewall
SILENT_PASS_POLICY              = FORBIDDEN — every decision (ALLOW or DENY) is logged immutably
SCOPE                           = ALL inter-agent calls · ALL agent-to-data-store calls ·
                                  ALL agent-to-event-bus calls · ALL agent-to-external-service calls
                                  on the Ecoskiller Antigravity platform
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```yaml
AGENT_NAME:             AGENT_ACCESS_PERMISSION_FIREWALL
SYSTEM_ROLE:            Zero-Trust Inter-Agent Security Enforcement · Permission Policy Arbiter ·
                        Identity Verification Sentinel · Scope Boundary Guard ·
                        Cross-Tenant Isolation Enforcer · Audit Trail Mandator
PRIMARY_DOMAIN:         Platform Security Infrastructure / Zero-Trust Architecture /
                        Inter-Agent Authorization / Access Control Enforcement
EXECUTION_MODE:         Deterministic + Synchronous + Validated + Cryptographically Verified
DATA_SCOPE:             Agent Identity Registry · Permission Matrix · Token Store ·
                        Scope Registry · Tenant Isolation Map · Domain Isolation Map ·
                        Access Decision Log · Violation Log · Circuit Breaker State
TENANT_SCOPE:           ABSOLUTE ISOLATION — this firewall enforces isolation for ALL tenants
                        It is itself tenant-agnostic: it validates tenant identity but
                        never holds cross-tenant data in a single evaluation context
FAILURE_POLICY:         DENY on any ambiguity | DENY on any missing credential |
                        DENY on any scope mismatch | LOG every decision | ESCALATE P0 violations
PLATFORM_CONTEXT:       Ecoskiller Antigravity — Enterprise Multi-Tenant SaaS
                        at 10M–100M user scale
POSITION_IN_STACK:      Enforced BEFORE any agent processes a request.
                        No agent may receive or process a call without prior
                        AGENT_ACCESS_PERMISSION_FIREWALL clearance.
                        This is a synchronous inline enforcer — not a periodic auditor.
```

This agent is the **zero-trust enforcement spine** of Ecoskiller Antigravity. Every call between agents, every agent-to-datastore access, every agent-to-event-bus publish, and every agent-to-external-service call is intercepted, validated, and decided — ALLOW or DENY — by this firewall before execution proceeds. It operates with zero ambient trust. It assumes every caller is potentially malicious until cryptographically and policy-verified. It never guesses. It never passes on ambiguity. It never allows emergency bypass. It is the single, unchallengeable authority on inter-agent access across the entire platform.

---

## 2️⃣ PURPOSE DECLARATION

### 2.1 Problem Solved

The AGENT_ACCESS_PERMISSION_FIREWALL resolves the following critical security architecture gaps in Ecoskiller Antigravity:

- **Implicit Agent Trust**: Agents currently assume calls from other agents are legitimate — no inter-agent identity verification at call time
- **Scope Creep Risk**: Agents can call operations beyond their declared scope without detection
- **Cross-Tenant Pollution**: No enforcement layer blocking an agent from accessing data outside its tenant context mid-call
- **Cross-Domain Leakage**: Domain isolation rules (Arts vs Technology vs Commerce) not enforced at call layer — only at data layer
- **Privilege Escalation**: Agents could receive elevated permissions not defined in their specification without detection
- **Compromised Agent Propagation**: If one agent is compromised, no firewall prevents lateral movement to other agents
- **Missing Audit Completeness**: Inter-agent calls not uniformly audited — gaps exist between agent-level logs
- **Token Replay Vulnerability**: Agent-to-agent tokens not validated for replay, expiry, or scope binding
- **Event Bus Pollution**: Any agent can publish any event to the event bus without scope validation
- **Datastore Access Sprawl**: Agents reading data stores outside their declared DATA_SCOPE without enforcement
- **External Service Call Leakage**: Agents making undeclared external service calls not blocked or audited

### 2.2 What This Agent Intercepts (COMPLETE CALL SURFACE)

```yaml
INTERCEPTED_CALL_TYPES:
  AGENT_TO_AGENT:         Synchronous API calls between any two Antigravity agents
  AGENT_TO_DATASTORE:     Database reads/writes, cache reads/writes, object storage access
  AGENT_TO_EVENT_BUS:     Redis Streams publish and consume operations
  AGENT_TO_VAULT:         Hashicorp Vault secret reads
  AGENT_TO_EXTERNAL:      Outbound calls to external APIs, payment gateways, notification services
  AGENT_TO_MEDIA:         LiveKit, WebRTC, signed URL generation, recording access
  AGENT_TO_SEARCH:        OpenSearch query operations
  AGENT_TO_AI_PROVIDER:   LLM inference calls (scoped by declared AI_USAGE_SCOPE)
  HUMAN_OPERATOR_TO_AGENT: Admin/operator direct agent access (emergency procedures)
```

### 2.3 Output Produced
- Access Decision Records (ALLOW / DENY — every decision, every call, every agent)
- Scoped Agent Tokens (short-lived, cryptographically signed, scope-bound, tenant-bound)
- Violation Alerts (structured, severity-classified, immediately routed)
- Circuit Breaker State Updates (agent-level isolation on repeated violations)
- Audit Trail Entries (immutable, complete, tamper-evident per call)
- Permission Matrix Validation Results (on-demand and periodic)
- Agent Health Attestations (cryptographic proof of agent integrity at call time)

### 2.4 Downstream Agents
- `SECURITY_AGENT` — receives all P0/P1 violation alerts
- `GOVERNANCE_AGENT` — receives policy breach alerts and circuit breaker activations
- `OBSERVABILITY_AGENT` — receives firewall performance metrics and anomaly signals
- `EVIDENCE_PRESERVATION_AGENT` — all access decisions sealed as tamper-evident records
- `AUDIT_LOG_AGENT` — receives immutable access decision log stream

### 2.5 Upstream Agents
- **NONE** — this firewall has no upstream agent dependencies.
  It is the first layer. All other agents are downstream callers subject to its enforcement.
  Its own identity is bootstrapped from platform-level HSM-secured root credentials.
  It validates itself against the AGENT_REGISTRY at startup and on every 60-second heartbeat.

---

## 3️⃣ AGENT IDENTITY SYSTEM (LOCKED)

### 3.1 Agent Identity Registry

Every agent on the Antigravity platform must be registered before it can make or receive calls:

```json
AGENT_REGISTRY_ENTRY: {
  "agent_id": "UUID v4 — globally unique, immutable after registration",
  "agent_name": "string — matches AGENT_NAME in agent specification",
  "agent_version": "string — semantic version — must match deployed version",
  "agent_spec_hash": "SHA-256 of agent's sealed specification document",
  "tenant_scope": "ALL_TENANTS | SINGLE_TENANT | PLATFORM_INTERNAL",
  "domain_scope": ["Arts", "Commerce", "Science", "Technology", "Administration", "ALL"],
  "declared_data_scope": ["list of data stores agent is permitted to access"],
  "declared_upstream_agents": ["agent_ids this agent may receive calls from"],
  "declared_downstream_agents": ["agent_ids this agent may call"],
  "declared_event_topics_publish": ["Redis Streams topics this agent may publish to"],
  "declared_event_topics_consume": ["Redis Streams topics this agent may consume from"],
  "declared_external_endpoints": ["external service URLs this agent may call"],
  "declared_ai_providers": ["LLM endpoints this agent may call — from AI_USAGE_SCOPE"],
  "declared_vault_secret_paths": ["Vault paths this agent may read"],
  "trust_tier": "TIER_1_GOVERNANCE | TIER_2_CORE | TIER_3_INTELLIGENCE | TIER_4_SUPPORT",
  "circuit_breaker_state": "CLOSED | OPEN | HALF_OPEN",
  "registration_timestamp_utc": "ISO8601",
  "last_attestation_utc": "ISO8601",
  "spec_integrity_status": "VERIFIED | DRIFT_DETECTED | UNVERIFIED"
}
```

**Registration Rules:**
- Agent registration requires GOVERNANCE_AGENT + SECURITY_AGENT dual approval
- Agent spec_hash computed from sealed specification document — recomputed on every deployment
- spec_hash mismatch = UNVERIFIED agent = all calls DENIED until re-verification
- No self-registration — agents cannot register themselves
- Registry is append-only — deregistration = status flag change, record never deleted

### 3.2 Agent Trust Tiers

```yaml
TRUST_TIER_DEFINITIONS:

  TIER_1_GOVERNANCE:
    members:
      - GOVERNANCE_AGENT
      - SECURITY_AGENT
      - COMPLIANCE_AGENT
      - AGENT_ACCESS_PERMISSION_FIREWALL (self)
    privileges:
      - May receive calls from any tier
      - May call any TIER_2 or TIER_3 agent (within declared scope)
      - May read cross-tenant aggregated (anonymised) data with explicit policy
      - May trigger circuit breakers on any agent
    restrictions:
      - May NOT read raw user PII without specific declared scope
      - May NOT bypass EVIDENCE_PRESERVATION_AGENT chain requirements
      - Subject to same firewall enforcement as all other agents

  TIER_2_CORE:
    members:
      - IDENTITY_AGENT
      - EVIDENCE_PRESERVATION_AGENT
      - USER_RIGHTS_EXPLANATION_AGENT
      - UNIT_ECONOMICS_ENGINE_AGENT
      - BILLING_AGENT
      - CONSENT_ENGINE_AGENT
      - CERTIFICATION_ENGINE_AGENT
      - JOB_PORTAL_ENGINE_AGENT
    privileges:
      - May call TIER_3 agents within declared downstream scope
      - May call TIER_1 agents only for escalation/audit events
      - May access their declared data stores only
    restrictions:
      - May NOT call other TIER_2 agents unless explicitly declared in both agents' specs
      - May NOT access TIER_1 agents' internal data stores

  TIER_3_INTELLIGENCE:
    members:
      - SCORING_ENGINE_AGENT
      - ANTI_CHEAT_ENGINE_AGENT
      - PASSIVE_INTELLIGENCE_AGENT
      - IDEA_DNA_AGENT
      - FEATURE_STORE_AGENT
      - OBSERVABILITY_AGENT
      - DOJO_ENGINE_AGENT
      - ROYALTY_ENGINE
      - DISPUTE_RESOLUTION_AGENT
      - GROWTH_ENGINE_AGENT
      - NOTIFICATION_ENGINE_AGENT
      - MEDIA_ENGINE_AGENT
    privileges:
      - May call declared downstream agents within same or lower tier
      - May publish declared events to event bus
    restrictions:
      - May NOT call TIER_1 agents directly — must route via TIER_2 for escalation
      - May NOT access cross-tenant data stores under any condition
      - May NOT call undeclared external endpoints

  TIER_4_SUPPORT:
    members:
      - SEARCH_ENGINE_AGENT
      - CACHE_AGENT
      - AUDIT_LOG_AGENT
      - PARENT_VISIBILITY_AGENT
      - CURRICULUM_AGENT
      - REPORT_GENERATOR_AGENT
    privileges:
      - May only receive calls — very limited outbound call rights
      - May write to declared output stores
    restrictions:
      - May NOT initiate calls to TIER_1, TIER_2, TIER_3 agents
      - Exception: AUDIT_LOG_AGENT may receive writes from all tiers
```

---

## 4️⃣ PERMISSION MATRIX (LOCKED — Add-Only)

### 4.1 Matrix Structure

The Permission Matrix is the authoritative source of truth for every allowed inter-agent call. If a call is not in the matrix — it is DENIED. No exceptions.

```yaml
PERMISSION_MATRIX_ENTRY: {
  "matrix_id": "UUID v4",
  "caller_agent_id": "string — from AGENT_REGISTRY",
  "callee_agent_id": "string — from AGENT_REGISTRY OR data_store OR event_topic OR external_endpoint",
  "call_type": "AGENT_TO_AGENT | AGENT_TO_DATASTORE | AGENT_TO_EVENT_BUS |
                AGENT_TO_VAULT | AGENT_TO_EXTERNAL | AGENT_TO_AI_PROVIDER |
                AGENT_TO_MEDIA | AGENT_TO_SEARCH",
  "operation": "READ | WRITE | PUBLISH | CONSUME | EXECUTE | DELETE",
  "scope_conditions": {
    "tenant_scope": "OWN_TENANT | ALL_TENANTS | PLATFORM_INTERNAL",
    "domain_scope": ["list of allowed domains or ALL"],
    "data_filters": ["any field-level restrictions e.g. no_pii | no_raw_forensics"],
    "time_of_day_restriction": "NONE | BUSINESS_HOURS_ONLY | string cron expression",
    "rate_limit_per_minute": "integer — max calls per minute for this matrix entry",
    "payload_size_limit_kb": "integer — max payload size for this call"
  },
  "token_requirements": {
    "token_type": "AGENT_JWT | SCOPED_SERVICE_TOKEN | VAULT_APPROLE",
    "max_ttl_seconds": "integer — token lifetime for this call type",
    "require_tenant_binding": "boolean",
    "require_domain_binding": "boolean",
    "require_mfa_step_up": "boolean — for sensitive operations"
  },
  "version": "string — semantic version of this matrix entry",
  "approved_by": ["GOVERNANCE_AGENT_ID", "SECURITY_AGENT_ID"],
  "effective_from_utc": "ISO8601",
  "expires_utc": "ISO8601 or null for permanent"
}
```

### 4.2 Core Permission Matrix (Locked Baseline)

```yaml
# ─── GOVERNANCE TIER CALLS ──────────────────────────────────────────────────

GOVERNANCE_AGENT → EVIDENCE_PRESERVATION_AGENT:
  operations: [READ]
  scope: ALL_TENANTS (anonymised manifest only — not raw payloads)
  token_ttl: 300s | tenant_binding: false | domain_binding: false

GOVERNANCE_AGENT → COMPLIANCE_AGENT:
  operations: [READ, WRITE]
  scope: ALL_TENANTS | ALL_DOMAINS
  token_ttl: 300s

GOVERNANCE_AGENT → SECURITY_AGENT:
  operations: [READ, WRITE, EXECUTE]
  scope: ALL_TENANTS
  token_ttl: 300s

SECURITY_AGENT → ALL_AGENTS (circuit breaker write):
  operations: [EXECUTE — circuit_breaker_activate only]
  scope: ALL_TENANTS
  token_ttl: 60s | require_mfa_step_up: true

# ─── IDENTITY & CONSENT CALLS ────────────────────────────────────────────────

IDENTITY_AGENT → PostgreSQL (users, roles, tenants, parent_links):
  operations: [READ, WRITE]
  scope: OWN_TENANT | ALL_DOMAINS
  token_ttl: 120s | tenant_binding: true

IDENTITY_AGENT → Hashicorp Vault (identity secrets path):
  operations: [READ]
  scope: PLATFORM_INTERNAL
  vault_paths: [/secret/identity/*]
  token_ttl: 60s

CONSENT_ENGINE_AGENT → PostgreSQL (consent_records table):
  operations: [READ, WRITE]
  scope: OWN_TENANT | ALL_DOMAINS
  token_ttl: 120s | tenant_binding: true | data_filters: [no_cross_user]

CONSENT_ENGINE_AGENT → EVIDENCE_PRESERVATION_AGENT:
  operations: [WRITE — CONSENT_CAPTURE event only]
  scope: OWN_TENANT
  token_ttl: 60s | tenant_binding: true

# ─── EVIDENCE PRESERVATION CALLS ─────────────────────────────────────────────

EVIDENCE_PRESERVATION_AGENT → PostgreSQL (evidence_chain, audit_log tables):
  operations: [READ, WRITE — INSERT only, no UPDATE no DELETE]
  scope: OWN_TENANT | ALL_DOMAINS
  token_ttl: 120s | tenant_binding: true

EVIDENCE_PRESERVATION_AGENT → MinIO Object Storage (evidence payloads):
  operations: [READ, WRITE — Object Lock enforced]
  scope: OWN_TENANT
  token_ttl: 60s | tenant_binding: true

EVIDENCE_PRESERVATION_AGENT → Hashicorp Vault (encryption key refs):
  operations: [READ]
  vault_paths: [/secret/evidence/keys/*]
  token_ttl: 60s

DOJO_ENGINE_AGENT → EVIDENCE_PRESERVATION_AGENT:
  operations: [WRITE — DOJO_SESSION_RECORDING, DOJO_SCORE_SUBMISSION, MENTOR_OVERRIDE_COMMAND]
  scope: OWN_TENANT | OWN_DOMAIN
  token_ttl: 60s | tenant_binding: true | domain_binding: true

SCORING_ENGINE_AGENT → EVIDENCE_PRESERVATION_AGENT:
  operations: [WRITE — DOJO_SCORE_FINAL only]
  scope: OWN_TENANT
  token_ttl: 60s | tenant_binding: true

ANTI_CHEAT_ENGINE_AGENT → EVIDENCE_PRESERVATION_AGENT:
  operations: [WRITE — ANTI_CHEAT_VIOLATION, ANTI_CHEAT_BEHAVIORAL_LOG]
  scope: OWN_TENANT | OWN_DOMAIN
  token_ttl: 60s | tenant_binding: true | domain_binding: true
  rate_limit_per_minute: 500

CERTIFICATION_ENGINE_AGENT → EVIDENCE_PRESERVATION_AGENT:
  operations: [READ — chain validation only | WRITE — CERTIFICATION_ISSUANCE]
  scope: OWN_TENANT
  token_ttl: 60s | tenant_binding: true

JOB_PORTAL_ENGINE_AGENT → EVIDENCE_PRESERVATION_AGENT:
  operations: [WRITE — OFFER_LETTER_ISSUED, OFFER_LETTER_ACCEPTED, OFFER_LETTER_REVOKED]
  scope: OWN_TENANT
  token_ttl: 60s | tenant_binding: true

PROJECT_EXECUTION_ENGINE_AGENT → EVIDENCE_PRESERVATION_AGENT:
  operations: [WRITE — PROJECT_MILESTONE_COMPLETION, PROJECT_SUBMISSION_ARTIFACT]
  scope: OWN_TENANT
  token_ttl: 60s | tenant_binding: true

# ─── UNIT ECONOMICS CALLS ─────────────────────────────────────────────────────

UNIT_ECONOMICS_ENGINE_AGENT → FEATURE_STORE_AGENT:
  operations: [READ — feature vectors only | WRITE — economics feature vectors]
  scope: OWN_TENANT
  token_ttl: 120s | tenant_binding: true | data_filters: [no_raw_billing]

UNIT_ECONOMICS_ENGINE_AGENT → BILLING_AGENT:
  operations: [READ — computed derivatives only, no raw billing ledger]
  scope: OWN_TENANT
  token_ttl: 120s | tenant_binding: true | data_filters: [no_raw_ledger]

BILLING_AGENT → Hashicorp Vault (payment gateway credentials):
  operations: [READ]
  vault_paths: [/secret/billing/payment-gateways/*]
  token_ttl: 30s | require_mfa_step_up: true

# ─── USER RIGHTS CALLS ────────────────────────────────────────────────────────

USER_RIGHTS_EXPLANATION_AGENT → IDENTITY_AGENT:
  operations: [READ — role, age_band, parent_link only]
  scope: OWN_TENANT
  token_ttl: 120s | tenant_binding: true | data_filters: [no_credentials, no_mfa_secrets]

USER_RIGHTS_EXPLANATION_AGENT → CONSENT_ENGINE_AGENT:
  operations: [READ — consent states | WRITE — consent withdrawal events]
  scope: OWN_TENANT
  token_ttl: 120s | tenant_binding: true | data_filters: [own_user_only]

USER_RIGHTS_EXPLANATION_AGENT → GOVERNANCE_AGENT:
  operations: [WRITE — grievance submissions, appeal submissions]
  scope: OWN_TENANT
  token_ttl: 60s | tenant_binding: true

USER_RIGHTS_EXPLANATION_AGENT → COMPLIANCE_AGENT:
  operations: [WRITE — erasure requests, data export requests]
  scope: OWN_TENANT
  token_ttl: 60s | tenant_binding: true | require_mfa_step_up: true

# ─── INTELLIGENCE TIER CALLS ─────────────────────────────────────────────────

PASSIVE_INTELLIGENCE_AGENT → PostgreSQL (behavioral events — write only):
  operations: [WRITE — append only]
  scope: OWN_TENANT | OWN_DOMAIN
  token_ttl: 120s | tenant_binding: true | domain_binding: true | data_filters: [no_pii_fields]

PASSIVE_INTELLIGENCE_AGENT → FEATURE_STORE_AGENT:
  operations: [WRITE — feature vectors only]
  scope: OWN_TENANT
  token_ttl: 120s | tenant_binding: true

SCORING_ENGINE_AGENT → Redis Streams (score events topic):
  operations: [PUBLISH]
  topics: [score.finalized, score.override.logged]
  scope: OWN_TENANT | OWN_DOMAIN
  token_ttl: 60s | tenant_binding: true

GROWTH_ENGINE_AGENT → PostgreSQL (xp, rank tables — write only):
  operations: [WRITE]
  scope: OWN_TENANT
  token_ttl: 120s | tenant_binding: true
  restriction: May NOT read economics tables | May NOT read evidence tables

NOTIFICATION_ENGINE_AGENT → Postal Email Gateway:
  operations: [WRITE — send only]
  scope: OWN_TENANT
  token_ttl: 30s | tenant_binding: true
  rate_limit_per_minute: 10000
  data_filters: [no_pii_in_logs, template_render_only]

NOTIFICATION_ENGINE_AGENT → FCM Push Gateway:
  operations: [WRITE — send only]
  scope: OWN_TENANT
  token_ttl: 30s
  rate_limit_per_minute: 50000

MEDIA_ENGINE_AGENT → LiveKit (room management):
  operations: [READ, WRITE, EXECUTE]
  scope: OWN_TENANT
  token_ttl: 3600s (room lifetime) | tenant_binding: true
  restriction: Token scope bound to single match_id — cross-room token forbidden

ANTI_CHEAT_ENGINE_AGENT → PASSIVE_INTELLIGENCE_AGENT:
  operations: [READ — behavioral baseline only]
  scope: OWN_TENANT | OWN_DOMAIN
  token_ttl: 60s | tenant_binding: true | domain_binding: true
  data_filters: [aggregated_vectors_only, no_raw_event_stream]

# ─── AI PROVIDER CALLS ───────────────────────────────────────────────────────

USER_RIGHTS_EXPLANATION_AGENT → LLM Inference Endpoint:
  operations: [EXECUTE]
  scope: PLATFORM_INTERNAL
  token_ttl: 30s
  payload_size_limit_kb: 64
  restriction: Input must be ML-computed vector — no raw PII in prompt
  rate_limit_per_minute: 500

UNIT_ECONOMICS_ENGINE_AGENT → LLM Inference Endpoint:
  operations: [EXECUTE]
  scope: PLATFORM_INTERNAL
  token_ttl: 30s
  payload_size_limit_kb: 32
  restriction: Input is ML-computed summary only — no raw billing data
  rate_limit_per_minute: 200

EVIDENCE_PRESERVATION_AGENT → LLM Inference Endpoint:
  operations: [EXECUTE]
  scope: PLATFORM_INTERNAL
  token_ttl: 30s
  payload_size_limit_kb: 32
  restriction: Input is ML-computed summary only — no raw forensic data | no raw PII
  rate_limit_per_minute: 100

# ─── EXPLICITLY FORBIDDEN CALLS (HARD DENY — LOGGED AS P0) ───────────────────

DENY_MATRIX:
  - ANY_AGENT → ANY_OTHER_TENANT_DATASTORE:         HARD DENY | P0 violation
  - ANY_AGENT → ANY_OTHER_DOMAIN_DATA (undeclared):  HARD DENY | P0 violation
  - TIER_3_AGENT → TIER_1_AGENT (non-escalation):    HARD DENY | P1 violation
  - TIER_4_AGENT → ANY_OUTBOUND_CALL:                HARD DENY | P1 violation
  - ANY_AGENT → AGENT_REGISTRY (write):              HARD DENY | P0 violation
  - ANY_AGENT → PERMISSION_MATRIX (write):           HARD DENY | P0 violation
  - ANY_AGENT → FIREWALL_AUDIT_LOG (write directly): HARD DENY | P0 violation
  - ANY_AGENT → LLM with raw PII in payload:         HARD DENY | P0 violation
  - ANY_AGENT → External endpoint (undeclared):       HARD DENY | P1 violation
  - GROWTH_ENGINE_AGENT → ECONOMICS_DATA:            HARD DENY | P1 violation
  - PARENT_VISIBILITY_AGENT → FORENSIC_ARCHIVE:      HARD DENY | P0 violation
  - AUTOMATION_ROLE → USER_RIGHTS_EXPLANATION_AGENT: HARD DENY | P1 violation
  - ANY_AGENT → BILLING_LEDGER (raw):                HARD DENY | P1 violation
  - ANY_AGENT → VAULT (undeclared path):             HARD DENY | P0 violation
```

---

## 5️⃣ TOKEN ARCHITECTURE (LOCKED)

### 5.1 Agent Token Types

```yaml
TOKEN_TYPE_1 — AGENT_JWT:
  purpose:          Standard inter-agent API call authentication
  issuer:           AGENT_ACCESS_PERMISSION_FIREWALL (self)
  algorithm:        RS256 (asymmetric — private key in Vault, public key distributed)
  claims_required:
    - sub:          caller_agent_id (from AGENT_REGISTRY)
    - aud:          callee_agent_id (specific target — not wildcard)
    - scope:        exact operation + data scope (e.g. "evidence.write.own_tenant")
    - tenant_id:    bound tenant UUID (OWN_TENANT calls only)
    - domain_tag:   bound domain (if domain_binding=true)
    - jti:          unique token ID (UUID v4 — for replay detection)
    - iat:          issued-at timestamp
    - exp:          expiry timestamp (max TTL from Permission Matrix)
    - agent_version: caller agent semantic version
    - spec_hash:    caller agent spec hash (verified against AGENT_REGISTRY)
  max_ttl:          3600s (exception: MediaEngine room tokens) — default per matrix entry
  replay_window:    Tracked by JTI cache — token reuse after first use = DENIED + P1 alert
  rotation:         Tokens are single-use for sensitive operations | multi-use within TTL for
                    high-frequency reads (per matrix entry configuration)

TOKEN_TYPE_2 — SCOPED_SERVICE_TOKEN:
  purpose:          Agent-to-datastore access (PostgreSQL, Redis, MinIO, OpenSearch)
  issuer:           Hashicorp Vault (AppRole authentication)
  mechanism:        Vault AppRole per agent — role_id + secret_id issued at startup
  scope:            Vault policy limits to declared paths only
  ttl:              120s (auto-renewed by agent) — Vault revokes on circuit breaker activation
  rotation:         secret_id rotated every 24 hours automatically
  restriction:      AppRole never shared between agents — 1 AppRole per registered agent

TOKEN_TYPE_3 — EVENT_BUS_TOKEN:
  purpose:          Redis Streams publish/consume authentication
  issuer:           AGENT_ACCESS_PERMISSION_FIREWALL (per-session)
  mechanism:        HMAC-SHA256 signed stream access credential
  scope:            Bound to declared topic list from Permission Matrix — no wildcard subscriptions
  ttl:              Per session (agent restart = new token)
  restriction:      Consumer group name must match agent_id — cross-agent consumer group DENIED

TOKEN_TYPE_4 — MEDIA_SESSION_TOKEN:
  purpose:          LiveKit room join and media operations
  issuer:           MEDIA_ENGINE_AGENT (after firewall clearance)
  mechanism:        LiveKit SDK token — role-metadata-bound
  scope:            Bound to single match_id — no cross-room validity
  ttl:              Session duration (max 4 hours)
  metadata_claims:  user_id, user_role, match_id, tenant_id, session_start_utc
  restriction:      Token metadata must match session participant record in DOJO_ENGINE_AGENT

TOKEN_TYPE_5 — EMERGENCY_OPERATOR_TOKEN:
  purpose:          Human operator direct agent access (break-glass scenario)
  issuer:           GOVERNANCE_AGENT (requires dual approval: GOVERNANCE + SECURITY roles)
  mechanism:        Time-limited scoped token with MFA step-up
  scope:            Explicitly declared operation only — no ambient access
  ttl:              3600s maximum (no renewal — must re-issue with new approval)
  audit:            Every use logged as P1 access event — mandatory post-use review
  restriction:      Issued at most 3 times per agent per 30-day period
                    4th request = automatic escalation to human security team
```

### 5.2 Token Validation Protocol

Every token presented to this firewall is validated in strict sequence:

```
STEP 1 — SIGNATURE VERIFICATION
  Verify RS256 signature against firewall's public key store
  Failure → DENY + LOG_INVALID_SIGNATURE (P1)

STEP 2 — EXPIRY CHECK
  Verify exp claim > current UTC timestamp
  Failure → DENY + LOG_EXPIRED_TOKEN (P2)

STEP 3 — REPLAY DETECTION
  Check JTI against replay cache (TTL = token exp - iat + 30s buffer)
  JTI already present → DENY + LOG_REPLAY_ATTACK (P0)
  Not present → insert JTI into replay cache

STEP 4 — CALLER IDENTITY VERIFICATION
  Verify sub (caller_agent_id) exists in AGENT_REGISTRY
  Verify agent_version matches AGENT_REGISTRY current version
  Verify spec_hash matches AGENT_REGISTRY spec_hash
  Mismatch → DENY + LOG_IDENTITY_MISMATCH (P1)
  spec_hash mismatch → DENY + LOG_SPEC_DRIFT (P1) + ALERT_SECURITY_AGENT

STEP 5 — CALLEE PERMISSION CHECK
  Verify aud (callee_agent_id) is in caller's declared_downstream_agents
  Verify operation is in Permission Matrix for this caller-callee pair
  Not in matrix → DENY + LOG_UNAUTHORIZED_CALL (P1)

STEP 6 — SCOPE VALIDATION
  Verify tenant_id claim matches request context (if tenant_binding=true)
  Verify domain_tag claim matches request context (if domain_binding=true)
  Mismatch → DENY + LOG_SCOPE_VIOLATION (P0 for tenant | P1 for domain)

STEP 7 — CIRCUIT BREAKER CHECK
  Check caller agent circuit_breaker_state in AGENT_REGISTRY
  State = OPEN → DENY + LOG_CIRCUIT_OPEN (P2)
  State = HALF_OPEN → ALLOW single probe request, monitor result

STEP 8 — RATE LIMIT CHECK
  Check current call rate for this caller-callee-operation pair
  Exceeds matrix rate_limit_per_minute → DENY + LOG_RATE_EXCEEDED (P2)

STEP 9 — PAYLOAD SIZE CHECK
  Verify request payload size ≤ matrix payload_size_limit_kb
  Exceeds → DENY + LOG_OVERSIZED_PAYLOAD (P2)

STEP 10 — ALLOW + AUDIT
  All checks passed → ALLOW
  Log ACCESS_DECISION (ALLOW) immutably
  Return scoped execution token to caller
```

---

## 6️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "caller_agent_id",
    "callee_agent_id_or_resource",
    "call_type",
    "operation",
    "agent_token",
    "request_timestamp_utc",
    "request_id"
  ],
  "optional_fields": [
    "tenant_id",
    "domain_tag",
    "payload_size_bytes",
    "payload_hash",
    "session_context_id",
    "emergency_operator_id",
    "emergency_justification"
  ],
  "validation_rules": [
    "caller_agent_id must exist in AGENT_REGISTRY — reject unknown callers",
    "callee_agent_id_or_resource must exist in Permission Matrix for this caller — deny if absent",
    "call_type must be in defined INTERCEPTED_CALL_TYPES — deny unknown call types",
    "operation must be in [READ, WRITE, PUBLISH, CONSUME, EXECUTE, DELETE] — deny unknown",
    "agent_token must be present and non-null — deny missing token immediately",
    "request_timestamp_utc must be ISO 8601 UTC and within ±30 seconds of server time — deny clock skew",
    "request_id must be UUID v4 — used for idempotency and audit linkage",
    "payload_size_bytes if present must match declared payload_size_limit_kb in matrix",
    "emergency fields: if emergency_operator_id present, EMERGENCY_OPERATOR_TOKEN required"
  ],
  "security_checks": [
    "Token validation runs full 10-step protocol before any other processing",
    "Clock skew > 30 seconds → DENY immediately (replay/time-manipulation protection)",
    "Any null value in required_fields → DENY immediately — no partial processing",
    "All inputs received over mTLS only — plaintext calls rejected at network layer"
  ]
}
```

---

## 7️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "decision": "ALLOW | DENY",
  "decision_id": "UUID v4 — unique per decision",
  "request_id": "string — echoed from input for correlation",
  "caller_agent_id": "string",
  "callee_agent_id_or_resource": "string",
  "call_type": "string",
  "operation": "string",
  "decision_timestamp_utc": "ISO8601",
  "deny_reason_code": "string or null — populated on DENY",
  "deny_reason_plain": "string or null — human-readable deny reason for operator logs",
  "violation_severity": "P0 | P1 | P2 | null — populated on DENY",
  "scoped_execution_token": "string or null — short-lived token for ALLOW decisions only",
  "scoped_token_ttl_seconds": "integer or null",
  "rate_limit_remaining": "integer — calls remaining this minute for this matrix entry",
  "circuit_breaker_state": "CLOSED | OPEN | HALF_OPEN",
  "audit_reference": "UUID v4 — links to immutable audit log entry",
  "firewall_version": "string — e.g. firewall-v1.0.0"
}
```

---

## 8️⃣ CIRCUIT BREAKER SYSTEM

```yaml
PURPOSE:
  Automatically isolate a misbehaving or compromised agent to prevent
  lateral damage across the platform. Operates independently of human response time.

CIRCUIT_BREAKER_STATES:
  CLOSED:    Normal operation — all permitted calls flow through
  OPEN:      Agent isolated — ALL calls to and from this agent DENIED
             Exception: GOVERNANCE_AGENT and SECURITY_AGENT may still send
             circuit_breaker_read operations for diagnostics
  HALF_OPEN: Probe state — one call allowed per minute to test recovery

TRIP_CONDITIONS (any one sufficient to open circuit):
  - 5 P0 violations within any 5-minute window from this agent
  - 20 P1 violations within any 15-minute window from this agent
  - spec_hash drift detected (agent running code not matching sealed spec)
  - Token replay attack detected from this agent
  - Cross-tenant data access attempt from this agent
  - SECURITY_AGENT manual trigger (requires GOVERNANCE_AGENT confirmation within 15 minutes
    or circuit auto-closes — prevents single-actor abuse of circuit breaker)
  - Agent heartbeat failure for > 120 seconds (possible compromise/crash)

RECOVERY_CONDITIONS (all required to close circuit):
  - SECURITY_AGENT + GOVERNANCE_AGENT dual approval
  - Agent re-attestation (spec_hash re-verified against deployed version)
  - Root cause identified and documented in INCIDENT_LOG
  - No P0 violations during HALF_OPEN probe period (minimum 5 probe requests)
  - Human security team sign-off for P0-trip circuits

CIRCUIT_BREAKER_AUDIT:
  Every state change (CLOSED→OPEN, OPEN→HALF_OPEN, HALF_OPEN→CLOSED) logged immutably
  with: triggering_event, approving_agents, timestamp, agent_version_at_trip
```

---

## 9️⃣ VIOLATION CLASSIFICATION & RESPONSE

```yaml
VIOLATION_TAXONOMY:

  P0_VIOLATIONS (CRITICAL — Immediate automated response + human escalation):
    - CROSS_TENANT_ACCESS_ATTEMPT:
        definition:   Agent attempts to access data from a tenant other than its token's tenant_id
        response:     DENY + CIRCUIT_BREAK_CALLER + LOG_P0 + ALERT_SECURITY_AGENT + ALERT_GOVERNANCE
        human_sla:    15 minutes to acknowledge

    - TOKEN_REPLAY_ATTACK:
        definition:   JTI presented for a second time
        response:     DENY + REVOKE_ALL_TOKENS_FOR_AGENT + CIRCUIT_BREAK_CALLER + LOG_P0 + ALERT_SECURITY
        human_sla:    15 minutes to acknowledge

    - AGENT_SPEC_DRIFT:
        definition:   Running agent's spec_hash ≠ AGENT_REGISTRY spec_hash
        response:     DENY_ALL_CALLS + CIRCUIT_BREAK_AGENT + LOG_P0 + ALERT_SECURITY + ALERT_GOVERNANCE
        human_sla:    15 minutes to acknowledge

    - PERMISSION_MATRIX_WRITE_ATTEMPT:
        definition:   Any agent attempts to write to AGENT_REGISTRY or PERMISSION_MATRIX
        response:     DENY + CIRCUIT_BREAK_CALLER + LOG_P0 + ALERT_SECURITY
        human_sla:    15 minutes to acknowledge

    - RAW_PII_IN_LLM_PAYLOAD:
        definition:   Detected raw PII fields in a payload destined for AI provider endpoint
        response:     DENY + LOG_P0 + ALERT_SECURITY + ALERT_GOVERNANCE
        human_sla:    30 minutes to acknowledge

    - FORENSIC_ARCHIVE_UNAUTHORIZED_READ:
        definition:   Agent without forensic read scope attempts to access forensic archive
        response:     DENY + CIRCUIT_BREAK_CALLER + LOG_P0 + ALERT_SECURITY
        human_sla:    15 minutes to acknowledge

    - PARENT_ACCESS_FORENSIC_DATA:
        definition:   PARENT_VISIBILITY_AGENT or any parent-scoped call attempts forensic archive read
        response:     DENY + LOG_P0 + ALERT_SECURITY + ALERT_GOVERNANCE
        human_sla:    30 minutes to acknowledge

  P1_VIOLATIONS (HIGH — Automated response + alert, human review within 1 hour):
    - UNAUTHORIZED_AGENT_CALL (not in Permission Matrix):
        response:     DENY + INCREMENT_VIOLATION_COUNTER + LOG_P1 + ALERT_SECURITY
        auto_circuit_trip: at 20 within 15 minutes

    - INVALID_TOKEN_SIGNATURE:
        response:     DENY + LOG_P1 + ALERT_SECURITY

    - AGENT_IDENTITY_MISMATCH (version or spec_hash discrepancy):
        response:     DENY + LOG_P1 + ALERT_SECURITY + REQUEST_ATTESTATION

    - CROSS_DOMAIN_UNDECLARED_ACCESS:
        response:     DENY + LOG_P1 + INCREMENT_VIOLATION_COUNTER

    - UNDECLARED_EXTERNAL_ENDPOINT:
        response:     DENY + LOG_P1 + ALERT_SECURITY

    - EXPIRED_TOKEN_USED:
        response:     DENY + LOG_P1

    - TIER_BOUNDARY_VIOLATION (TIER_3 calling TIER_1 directly):
        response:     DENY + LOG_P1 + ALERT_SECURITY

    - EMERGENCY_TOKEN_4TH_REQUEST:
        response:     DENY + LOG_P1 + ALERT_HUMAN_SECURITY_TEAM

  P2_VIOLATIONS (MEDIUM — Logged, monitored, alert on sustained pattern):
    - RATE_LIMIT_EXCEEDED:
        response:     DENY + LOG_P2 + BACKOFF_SIGNAL_TO_CALLER

    - OVERSIZED_PAYLOAD:
        response:     DENY + LOG_P2

    - CLOCK_SKEW_DETECTED (>30s but <120s):
        response:     DENY + LOG_P2

    - CIRCUIT_HALF_OPEN_PROBE_FAILED:
        response:     REOPEN_CIRCUIT + LOG_P2 + ALERT_SECURITY

    - EXPIRED_MATRIX_ENTRY_USED:
        response:     DENY + LOG_P2 + NOTIFY_GOVERNANCE_AGENT (matrix entry needs renewal)
```

---

## 🔟 ML / AI LOGIC LAYER

### 10.1 ML Layer (Primary — 90% of intelligence)

```yaml
NOTE: This is a SECURITY-CRITICAL enforcement agent.
      AI is used only for anomaly detection pattern analysis — never for access decisions.
      ALL access decisions are deterministic rule-based (Permission Matrix + Token validation).
      ML supplements with anomaly signal detection only.

MODEL_TYPE:
  - Anomaly Detection:
      - Behavioral baseline anomaly: detect unusual call patterns per agent per time window
      - Volume anomaly: call rate spikes suggesting compromise or misconfiguration
      - Graph anomaly: unexpected call paths in the agent dependency graph
      - Token usage anomaly: unusual token request patterns per agent
  - Classification:
      - Violation severity classifier: enrich human review with ML severity assessment
      - Lateral movement risk scorer: assess probability that detected violations indicate
        coordinated attack vs isolated misconfiguration

FEATURES_USED:
  - calls_per_minute_per_matrix_entry (rolling 5min window)
  - unique_callee_count_per_hour (per caller)
  - deny_rate_per_caller (rolling 15min window)
  - payload_size_distribution (per caller-callee pair)
  - token_request_rate (per agent per hour)
  - time_of_day_call_distribution (deviation from baseline)
  - call_sequence_entropy (expected call sequences vs observed)
  - cross_domain_attempt_rate (per caller)
  - error_rate_by_operation (per agent)
  - circuit_break_history_count (per agent, rolling 30 days)

TRAINING_FREQUENCY:
  - Anomaly baselines: weekly recompute from stable operation windows
  - Severity classifier: monthly
  - Lateral movement risk scorer: monthly

DRIFT_DETECTION:
  - Behavioral baselines drift naturally as platform evolves — weekly recalibration required
  - Rapid drift (PSI > 0.20 within 24h) = suspicious = ALERT_SECURITY_AGENT immediately

AI_IN_THIS_AGENT:
  AI_USAGE:         NONE in decision path
  AI_FORBIDDEN:     AI may NEVER participate in ALLOW/DENY decisions
                    AI may NEVER modify Permission Matrix
                    AI may NEVER modify Agent Registry
  ML_ONLY_USAGE:    Anomaly signal generation for human-review enrichment only
```

---

## 1️⃣1️⃣ AUDIT & TRACEABILITY

Every firewall decision is logged immutably and synchronously — the decision is NOT returned to the caller until the audit entry is written:

```json
FIREWALL_AUDIT_LOG_ENTRY: {
  "audit_id": "UUID v4",
  "decision_id": "UUID v4",
  "request_id": "UUID v4 — from caller",
  "decision": "ALLOW | DENY",
  "decision_timestamp_utc": "ISO8601",
  "processing_latency_us": "integer — microseconds",
  "caller_agent_id": "string",
  "caller_agent_version": "string",
  "caller_spec_hash_presented": "string",
  "caller_spec_hash_registry": "string",
  "spec_hash_match": "boolean",
  "callee_agent_id_or_resource": "string",
  "call_type": "string",
  "operation": "string",
  "tenant_id_presented": "string or null",
  "tenant_id_validated": "boolean",
  "domain_tag_presented": "string or null",
  "domain_tag_validated": "boolean",
  "token_type": "string",
  "token_jti": "string — for replay tracking",
  "token_exp": "ISO8601",
  "token_valid": "boolean",
  "validation_step_failed": "integer or null — which of 10 steps failed (if DENY)",
  "deny_reason_code": "string or null",
  "violation_severity": "P0 | P1 | P2 | null",
  "circuit_breaker_state_at_decision": "CLOSED | OPEN | HALF_OPEN",
  "rate_limit_consumed": "integer",
  "ml_anomaly_score": "decimal 0.0–1.0",
  "ml_anomaly_flags": ["string array"],
  "downstream_alerts_emitted": ["string list of alert events emitted"],
  "firewall_version": "string"
}
```

**Audit Immutability Contract:**
- Written synchronously — decision not returned until audit entry confirmed written
- PostgreSQL partition: INSERT-only, no UPDATE, no DELETE — enforced at DB role level
- Replicated to secondary immutable store within 5 seconds of write
- Audit log itself is protected by AGENT_ACCESS_PERMISSION_FIREWALL — no agent may write to it directly (firewall writes its own log via privileged internal path, not via agent call)
- Audit log integrity verified by EVIDENCE_PRESERVATION_AGENT chain every 6 hours

---

## 1️⃣2️⃣ SCALABILITY DESIGN

```yaml
EXECUTION_MODEL:      Synchronous inline — every call blocks until firewall decision returned
                      This is non-negotiable: async firewall = security gap

EXPECTED_DECISION_RATE:
  - 10M users:    50,000 firewall decisions/second (aggregate across all agent calls)
  - 100M users:   500,000 firewall decisions/second

LATENCY_TARGET:
  - ALLOW decision:  P95 < 5ms | P99 < 15ms (token validation is hot-path)
  - DENY decision:   P95 < 3ms (early exit on first failure step)
  - Audit write:     Async after decision returned — max 50ms before confirmation
                     Decision IS returned before audit write completes (non-blocking audit)
                     but caller CANNOT proceed until audit write confirmed (re-entrant lock)

DEPLOYMENT_MODEL:
  - Co-located sidecar per agent pod (Kubernetes sidecar container pattern)
  - Every agent pod contains: [agent container] + [firewall sidecar container]
  - All inter-agent calls route through local firewall sidecar — never direct socket
  - Firewall sidecar shares no memory with agent container — strict process isolation
  - Firewall sidecar communicates with central Permission Matrix via read-replica cache

PERMISSION_MATRIX_CACHE:
  - Redis read replica per firewall sidecar — TTL 60 seconds per entry
  - Cache miss → fetch from authoritative PostgreSQL Permission Matrix store
  - Cache poisoning protection: entries signed by firewall master key
  - Any signed entry failure → invalidate cache + re-fetch + alert SECURITY_AGENT

JTI_REPLAY_CACHE:
  - Redis with TTL = max(all token TTLs) + 30s buffer = 3630s maximum
  - Per-sidecar with synchronised invalidation to central Redis cluster
  - JTI write must complete before ALLOW decision returned

AGENT_REGISTRY_CACHE:
  - Redis read replica — TTL 30 seconds
  - Stale registry entry detected → re-fetch + re-validate spec_hash
  - spec_hash mismatch on cache refresh → CIRCUIT_BREAK_AGENT immediately

MAX_CONCURRENCY:      1 firewall sidecar per agent pod — scales with agent pod scaling
                      Central audit writer: 200 worker pods (Kubernetes HPA)
                      Central Permission Matrix: PostgreSQL with read replicas (1 per region)
```

---

## 1️⃣3️⃣ FIREWALL SELF-PROTECTION

```yaml
FIREWALL_INTEGRITY_RULES:

  SELF_ATTESTATION:
    - Firewall sidecar verifies its own binary hash against AGENT_REGISTRY on startup
    - Heartbeat self-attestation every 60 seconds
    - Failed self-attestation → HALT_SIDECAR → BLOCK_ALL_AGENT_CALLS → ALERT_SECURITY

  PERMISSION_MATRIX_IMMUTABILITY:
    - Permission Matrix stored in PostgreSQL with write-once policy (append-only)
    - New entries added via GOVERNANCE_AGENT + SECURITY_AGENT dual-signed proposal
    - Matrix update deploys as new version — old version retained for rollback
    - Any direct write to matrix bypassing proposal process = P0 security incident

  AGENT_REGISTRY_IMMUTABILITY:
    - Agent Registry: append-only — no direct modifications
    - New agent registration: requires GOVERNANCE_AGENT + SECURITY_AGENT dual approval
    - Status changes (circuit breaker, spec_hash update): logged and dual-approved
    - Any direct DB write to registry bypassing approval = P0 security incident

  FIREWALL_CANNOT_BE_CALLED_AS_CALLEE:
    - AGENT_ACCESS_PERMISSION_FIREWALL does not appear as a callee in any agent's
      declared_downstream_agents list
    - No agent may call the firewall via standard agent-to-agent path
    - Firewall is only accessible via its sidecar injection mechanism
    - Any attempt to call firewall as an agent = P0 violation + ALERT_SECURITY

  NO_BYPASS_PATH:
    - No emergency bypass exists in this firewall
    - EMERGENCY_OPERATOR_TOKEN still passes through firewall — just with different scope
    - "Emergency" does not mean "unchecked" — all emergency calls logged as P1 events
    - Any code path that reaches an agent without firewall validation = architecture violation
      → Must be reported as security incident and patched before production deployment

  FIREWALL_KEY_MANAGEMENT:
    - RS256 private key stored in Hashicorp Vault — firewall accesses via Vault AppRole
    - Key rotation: every 90 days — overlap period 24 hours (both old and new key valid)
    - Key compromise suspected: immediate rotation + all outstanding tokens revoked
    - Public keys distributed to all agents via signed manifest — agents verify manifest
```

---

## 1️⃣4️⃣ SECURITY ENFORCEMENT

```yaml
TRANSPORT_SECURITY:
  - All inter-agent calls: mTLS 1.3 — mutual authentication required
  - Plaintext inter-agent calls: BLOCKED at network policy layer (Kubernetes NetworkPolicy)
  - All firewall-to-audit-store calls: TLS 1.3 + DB-level encryption
  - All firewall-to-Permission-Matrix calls: TLS 1.3 + signed query responses

NETWORK_POLICY:
  - Kubernetes NetworkPolicy: each agent pod may only communicate with declared peers
  - No agent pod has unrestricted network egress
  - External endpoint calls: routed through egress proxy (declared endpoints allowlisted)
  - Undeclared external calls blocked at network layer (defense in depth with firewall)

TENANT_ISOLATION_ENFORCEMENT:
  - Firewall enforces tenant_id binding at token claim level
  - Additional enforcement: PostgreSQL Row-Level Security (defense in depth)
  - Firewall and RLS are independent layers — neither substitutes for the other
  - Cross-tenant detection at either layer = P0 incident

DOMAIN_ISOLATION_ENFORCEMENT:
  - domain_tag binding in token for all domain-scoped calls
  - Firewall validates domain claim matches agent's declared domain_scope in AGENT_REGISTRY
  - Cross-domain call without declaration = P1 incident

PRIVILEGE_ESCALATION_PREVENTION:
  - Agent token scope is the ceiling — agent cannot request higher scope than matrix entry allows
  - Scope downgrade allowed — agent may request narrower scope than matrix permits
  - Scope upgrade: requires new matrix entry + GOVERNANCE + SECURITY approval
  - Any token presenting scope not in matrix = P1 violation

SECRETS_MANAGEMENT:
  - No secret, credential, or key stored in agent environment variables in production
  - All secrets via Hashicorp Vault AppRole exclusively
  - Secret access logged by Vault + mirrored to firewall audit log
  - Vault policy per agent limited to declared vault_secret_paths in AGENT_REGISTRY
```

---

## 1️⃣5️⃣ FAILURE POLICY

```yaml
FIREWALL_FAILURE_MODES:

  SIDECAR_UNREACHABLE:
    cause:      Firewall sidecar pod crash or network isolation
    response:   Agent pod loses all outbound call capability (fail-closed)
                No agent call proceeds without sidecar confirmation
                ALERT_SECURITY_AGENT + ALERT_OBSERVABILITY immediately
                Agent pod flagged as UNHEALTHY in Kubernetes — no new calls routed to it

  PERMISSION_MATRIX_UNREACHABLE:
    cause:      All read replicas unavailable — authoritative store also unreachable
    response:   DENY_ALL decisions (fail-closed — never fail-open)
                Serve cached decisions for entries within 60-second freshness window
                Beyond 60 seconds without refresh: DENY_ALL + ALERT_OPS
                human_sla: 5 minutes to restore matrix access

  AUDIT_STORE_WRITE_FAILURE:
    cause:      Audit PostgreSQL write fails
    response:   DENY decision (cannot allow a call that cannot be audited)
                Buffer audit entry in local sidecar queue (max 1000 entries, 30 seconds)
                Retry writes during buffer window
                If buffer exhausted or 30 seconds elapsed without write: HALT_AGENT + ALERT_OPS

  JTI_CACHE_UNAVAILABLE:
    cause:      Redis replay cache unavailable
    response:   DENY_ALL decisions (cannot verify replay without JTI cache)
                ALERT_OPS immediately — Redis restoration is P1 operational event
                Do NOT fallback to stateless mode — replay protection is mandatory

  VAULT_UNREACHABLE (key fetch failure):
    cause:      Vault unavailable for token signing key fetch
    response:   DENY_ALL decisions (cannot validate signatures without keys)
                Use cached public key if within 5-minute freshness window
                Beyond 5 minutes: DENY_ALL + ALERT_OPS

  FIREWALL_ITSELF_COMPROMISED (self-attestation failure):
    cause:      Firewall binary hash mismatch on heartbeat
    response:   HALT_ALL_AGENT_CALLS on this pod
                ALERT_SECURITY_AGENT (P0)
                Do not attempt self-recovery — human security team must validate and redeploy

FAIL_SAFE_PRINCIPLE:
  All failure modes default to DENY_ALL. This firewall never fails open.
  A degraded firewall that cannot enforce policy is worse than no firewall.
  Fail closed. Always. No exceptions.

NO_SILENT_FAILURES:
  Every failure — including internal firewall failures — produces an immutable log entry.
  If the audit store is down, the failure is logged to local sidecar buffer.
  If the sidecar buffer is full, the agent pod halts.
  There is no code path that reaches "no log" for any failure scenario.
```

---

## 1️⃣6️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:   NONE — this firewall has no upstream agent dependencies.
                   It bootstraps from platform-level HSM root credentials.

DOWNSTREAM_AGENTS (receives reports/alerts from firewall):
  - SECURITY_AGENT:         Receives all P0 + P1 violation alerts
  - GOVERNANCE_AGENT:       Receives policy breach alerts, circuit breaker events
  - OBSERVABILITY_AGENT:    Receives firewall performance metrics, anomaly ML signals
  - EVIDENCE_PRESERVATION_AGENT: Seals firewall audit log chain every 6 hours
  - AUDIT_LOG_AGENT:        Receives continuous audit decision stream

EVENT_TRIGGERS_EMITTED:
  - FIREWALL_ALLOW:         Emitted on every ALLOW decision (to audit stream)
  - FIREWALL_DENY:          Emitted on every DENY decision (to audit stream + alert if P0/P1)
  - CIRCUIT_BREAKER_OPENED: Emitted when agent circuit opens
  - CIRCUIT_BREAKER_CLOSED: Emitted when agent circuit restored
  - SPEC_DRIFT_DETECTED:    Emitted when agent spec_hash mismatch found
  - REPLAY_ATTACK_DETECTED: P0 — emitted immediately on JTI replay
  - CROSS_TENANT_BLOCKED:   P0 — emitted on cross-tenant attempt
  - RATE_LIMIT_HIT:         P2 — emitted per rate limit breach
  - MATRIX_ENTRY_EXPIRING:  Emitted 72 hours before Permission Matrix entry expires
  - EMERGENCY_TOKEN_USED:   P1 — emitted every time emergency operator token is consumed

EVENT_TRIGGERS_CONSUMED:   NONE in normal operation.
                            Exception: SECURITY_AGENT may push manual circuit_breaker commands
                            via firewall's privileged internal control channel (not via agent call)
```

---

## 1️⃣7️⃣ PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_OBSERVABILITY_AGENT:

  decision_throughput:
    definition:       Firewall decisions per second (aggregate across all sidecars)
    target:           Scales linearly with platform RPS
    alert_threshold:  Decision latency P95 > 5ms for ALLOW | > 3ms for DENY

  allow_rate:
    definition:       Percentage of decisions resulting in ALLOW
    expected_range:   > 99.5% under normal operation
    alert_threshold:  < 98% for 5 consecutive minutes (mass denial = possible config error or attack)

  deny_rate_by_severity:
    definition:       P0/P1/P2 denial rates per 5-minute window
    target:           P0 = 0 | P1 < 5/hour | P2 < 50/hour per platform
    alert_threshold:  Any P0 = immediate alert | P1 > 5/hour = P1 alert

  circuit_breaker_open_count:
    definition:       Number of agents with OPEN circuit breakers at any time
    target:           0
    alert_threshold:  Any > 0 = P1 alert

  token_validation_latency_p95:
    definition:       95th percentile time for 10-step token validation protocol
    target:           < 3ms
    alert_threshold:  > 8ms for 3 consecutive minutes

  replay_attack_rate:
    definition:       JTI replay attempts per hour
    target:           0
    alert_threshold:  Any > 0 = immediate P0

  spec_drift_count:
    definition:       Agents with spec_hash mismatch at any point
    target:           0
    alert_threshold:  Any > 0 = immediate P1

  permission_matrix_cache_freshness:
    definition:       Age of oldest cached matrix entry in active use
    target:           < 60 seconds
    alert_threshold:  > 90 seconds = P2 alert

  jti_cache_hit_rate:
    definition:       Percentage of tokens found in JTI cache on replay check
    expected:         Near 0% (replay attempts should be rare)
    alert_threshold:  > 0.01% = investigate immediately

  emergency_token_usage_rate:
    definition:       Emergency operator tokens issued per 30-day window per agent
    target:           0 in steady state
    alert_threshold:  > 2 per agent per 30 days = P1 alert (approaching 4th-request block)
```

---

## 1️⃣8️⃣ VERSIONING POLICY

```yaml
VERSION_FORMAT:                 Semantic — MAJOR.MINOR.PATCH (e.g., v1.0.0)
CHANGE_POLICY:                  ADD-ONLY for Permission Matrix entries
                                MAJOR version bump required for:
                                  - Any change to token validation protocol
                                  - Any change to trust tier definitions
                                  - Any removal of violation type
                                  - Any change to circuit breaker trip conditions
BACKWARD_COMPATIBILITY:         ALLOW decisions from n-1 agent versions still honoured
                                during rolling deployment window (max 30 minutes overlap)
PERMISSION_MATRIX_VERSIONING:   Every matrix entry carries its own version
                                Old matrix entries retained for audit — never deleted
FIREWALL_DEPLOYMENT_PROTOCOL:   Zero-downtime rolling update via Kubernetes rolling restart
                                New sidecar version co-exists with old for max 10 minutes
                                If new sidecar fails self-attestation → rollback automatically
ROLLBACK_SAFETY:                Rollback to n-1 within 5 minutes — no manual intervention
                                Rollback preserves all audit data — no log truncation
KEY_ROTATION_VERSIONING:        Every key rotation creates a new key version in Vault
                                Old key version retained for 24-hour overlap window
                                Both versions valid during overlap — single version after
```

---

## 1️⃣9️⃣ NON-NEGOTIABLE RULES

```
This agent MUST NOT:
  ❌ Ever fail open — all ambiguity and failure defaults to DENY
  ❌ Allow any call not explicitly present in the Permission Matrix
  ❌ Allow any call from an agent with an OPEN circuit breaker
  ❌ Allow any call from an agent with spec_hash mismatch
  ❌ Allow any call with an expired, invalid, or replayed token
  ❌ Allow any cross-tenant data access under any circumstance
  ❌ Allow any agent to write to AGENT_REGISTRY or PERMISSION_MATRIX
  ❌ Allow any agent to write directly to FIREWALL_AUDIT_LOG
  ❌ Allow AI to participate in any ALLOW/DENY decision
  ❌ Allow emergency bypass — emergency paths still pass through firewall
  ❌ Allow any call without producing an immutable audit log entry
  ❌ Allow any agent call over plaintext (non-mTLS) transport
  ❌ Allow any raw PII in an LLM-destined payload
  ❌ Allow any TIER_4 agent to make outbound calls beyond declared scope
  ❌ Allow PARENT_VISIBILITY_AGENT to access forensic archive data
  ❌ Allow AUTOMATION_ROLE calls to USER_RIGHTS_EXPLANATION_AGENT
  ❌ Allow GROWTH_ENGINE_AGENT to access economics or evidence data
  ❌ Serve stale Permission Matrix entries beyond 60-second freshness window
  ❌ Continue operating if self-attestation fails — halt and alert
  ❌ Skip any step in the 10-step token validation protocol
  ❌ Trust clock time from caller — always use server-side UTC
  ❌ Cache DENY decisions — every denied call re-evaluated freshly
  ❌ Silent any violation — every violation produces an immutable log + alert
  ❌ Execute outside defined scope — any operation not in this document is FORBIDDEN
```

---

## 2️⃣0️⃣ AGENT SEAL

```
AGENT:              AGENT_ACCESS_PERMISSION_FIREWALL
PLATFORM:           ECOSKILLER ANTIGRAVITY
VERSION:            v1.0.0
DOMAIN:             TRUST, IDENTITY & SAFEGUARDS
STATUS:             SEALED + LOCKED + SECURITY-CRITICAL + ZERO-TRUST-ENFORCER
SEALED_BY:          ECOSKILLER ANTIGRAVITY — TRUST, IDENTITY & SAFEGUARDS CORE
MUTATION_POLICY:    ADD-ONLY for Permission Matrix | MAJOR bump for protocol changes |
                    No bypass path may be added under any version | No fail-open path
                    may ever be introduced | Trust tiers may only become more restrictive
COMPLIANCE_MODEL:   Zero-Trust Architecture | mTLS Mandatory | Sidecar Enforcement |
                    Fail-Closed Always | Append-Only Audit | Circuit Breaker Protection |
                    Spec Integrity Verified | Replay-Protected | Scope-Bound Tokens
EXECUTION_LAW:      Deterministic — Identical input → Identical decision
                    No partial enforcement. No silent passes. No emergency bypass.
                    No ambiguity resolution in favour of ALLOW.
                    When in doubt: DENY. Log. Escalate.
SECURITY_LAW:       No agent is trusted by default.
                    No call proceeds without firewall clearance.
                    No token is accepted without full 10-step validation.
                    No violation goes unlogged.
                    No circuit stays open without human review.
                    No spec drift goes undetected.
                    No cross-tenant call is permitted. Ever.

VIOLATION OF ANY RULE IN THIS DOCUMENT:
→ DENY_REQUEST
→ LOG_VIOLATION_INCIDENT (immutable, synchronous)
→ CIRCUIT_BREAK_VIOLATING_AGENT (if P0 or P1)
→ ESCALATE_TO: SECURITY_AGENT + GOVERNANCE_AGENT + HUMAN_SECURITY_TEAM
→ NO ALLOW DECISION EMITTED
→ NO SILENT RECOVERY — human sign-off required for circuit restoration

This document is the single source of truth for the AGENT_ACCESS_PERMISSION_FIREWALL.
All agent specifications must declare their upstream, downstream, event topics,
vault paths, external endpoints, and data stores in alignment with this firewall's
Permission Matrix. Any agent specification that conflicts with this firewall = INVALID.
Deviation without versioned amendment = ARCHITECTURE VIOLATION = PLATFORM SECURITY FAILURE.
```

---

*Document Version: v1.0.0 | Platform: Ecoskiller Antigravity | Domain: Trust, Identity & Safeguards | Classification: Enterprise Internal — Security-Critical — Zero-Trust — Restricted*
