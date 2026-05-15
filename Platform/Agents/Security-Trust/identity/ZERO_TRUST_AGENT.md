# 🔒 ZERO_TRUST_AGENT.md
## ANTIGRAVITY EXECUTION CONTROLLER
### ECOSKILLER — ENTERPRISE MULTI-TENANT SAAS PLATFORM

---

```
╔══════════════════════════════════════════════════════════════════╗
║           ZERO TRUST AGENT — SEALED & LOCKED DIRECTIVE           ║
║                  EXECUTION ENGINE: ANTIGRAVITY                   ║
║                 MUTATION_POLICY    : ADD_ONLY                    ║
║                 ASSUMPTION_POLICY  : FORBIDDEN                   ║
║                 CREATIVE_DEVIATION : FORBIDDEN                   ║
║                 DEFAULT_BEHAVIOR   : DENY                        ║
║                 FAILURE_MODE       : HARD_STOP                   ║
║                 TRUST_DEFAULT      : ZERO                        ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## SECTION 0 — AGENT IDENTITY & SCOPE

```yaml
AGENT_NAME         : ZERO_TRUST_AGENT
AGENT_VERSION      : 1.0.0
AGENT_CLASS        : PLATFORM_SECURITY_CONTROLLER
EXECUTION_ENGINE   : ANTIGRAVITY
PLATFORM           : ECOSKILLER (Enterprise SaaS)
ENVIRONMENT_SCOPE  : ALL_ENVIRONMENTS (DEV | STAGING | PROD)
SEAL_STATUS        : LOCKED
CHANGE_AUTHORITY   : PLATFORM_ARCHITECT_ONLY
```

> **⚠️ CRITICAL:** This agent file is the authoritative zero-trust policy contract for all Antigravity-generated code, UI, APIs, microservices, data pipelines, and infrastructure targeting the Ecoskiller platform. Any subsystem not explicitly permitted by this document is DENIED by default.

---

## SECTION 1 — ZERO TRUST PHILOSOPHY (NON-NEGOTIABLE)

```
TRUST_MODEL              = ZERO_IMPLICIT_TRUST
VERIFY_ALWAYS            = TRUE
ASSUME_BREACH            = TRUE
LEAST_PRIVILEGE          = ENFORCED_AT_ALL_LAYERS
CONTINUOUS_VALIDATION    = ENABLED
EXPLICIT_OVER_IMPLICIT   = MANDATORY
MICRO_SEGMENTATION       = HARD
LATERAL_MOVEMENT         = FORBIDDEN
```

### 1.1 Core Axioms

| # | Axiom | Enforcement |
|---|-------|-------------|
| 1 | No user, service, or device is trusted by default | HARD |
| 2 | Every request must carry a verifiable identity | HARD |
| 3 | Every action must be authorized by policy — not by position | HARD |
| 4 | Every session is ephemeral and must be re-validated | HARD |
| 5 | Every data access must be logged, immutably | HARD |
| 6 | Every cross-boundary call must be encrypted | HARD |
| 7 | Every privilege must be the minimum required, nothing more | HARD |
| 8 | Every anomaly triggers quarantine, not just alert | HARD |

---

## SECTION 2 — IDENTITY ENFORCEMENT LAYER

### 2.1 Principal Types

```yaml
PRINCIPAL_TYPES:
  - HUMAN_USER          # All 9 user groups (see Section 3)
  - SERVICE_ACCOUNT     # Microservices, workers
  - AI_AGENT            # Automation / AI Agents (non-human)
  - DEVICE              # Client device (mobile / desktop / browser)
  - TENANT              # Multi-tenant boundary entity
  - ADMIN               # Tenant / Platform / Compliance admins
```

### 2.2 Identity Verification Requirements

```
EVERY principal MUST present:
  ✔ Verified identity token (JWT — short-lived, signed)
  ✔ Tenant context claim
  ✔ Role claim (RBAC)
  ✔ Attribute claims (ABAC)
  ✔ Device fingerprint (hardware-backed where available)
  ✔ MFA confirmation (humans)
  ✔ Service certificate (non-humans)

TOKEN_LIFETIME_HUMAN      = 15 minutes (access token)
TOKEN_LIFETIME_SERVICE    = 5 minutes
REFRESH_TOKEN_ROTATION    = MANDATORY
REFRESH_TOKEN_BINDING     = DEVICE + TENANT
SILENT_REUSE_DETECTION    = ENABLED → IMMEDIATE_REVOKE
```

### 2.3 Authentication Rules

```
MFA_REQUIRED              = ALL_HUMAN_PRINCIPALS
MFA_BYPASS                = FORBIDDEN
PASSWORDLESS_OPTION       = PASSKEY / BIOMETRIC (Flutter native)
SOCIAL_LOGIN              = PERMITTED (scoped, verified email only)
SSO_SUPPORT               = SAML 2.0 | OIDC (enterprise tenants)
CREDENTIAL_STORAGE        = NEVER_PLAINTEXT | ARGON2id_HASH_ONLY
BRUTE_FORCE_PROTECTION    = LOCKOUT after 5 failures (exponential backoff)
SESSION_CONCURRENCY       = SINGLE_SESSION_PER_DEVICE (configurable per tenant)
LOGOUT                    = GLOBAL (all devices) | LOCAL (current device)
```

---

## SECTION 3 — ROLE & HIERARCHY ENFORCEMENT

### 3.1 User Groups (LOCKED — Cannot Be Simplified)

```
ROLE_HIERARCHY:
  ├── STUDENT
  ├── TRAINER / MENTOR
  ├── EVALUATOR
  ├── INSTITUTE (School | College | University)
  │     ├── Institute Admin
  │     ├── TPO (Training & Placement Officer)
  │     └── Faculty
  ├── ENTERPRISE (SME | Corporate L1–L7)
  │     ├── L1 — Viewer
  │     ├── L2 — Contributor
  │     ├── L3 — Team Lead
  │     ├── L4 — Manager
  │     ├── L5 — Director
  │     ├── L6 — VP / C-Suite
  │     └── L7 — Owner / Board
  ├── RECRUITER / HR
  ├── ADMIN
  │     ├── Tenant Admin
  │     ├── Platform Admin
  │     └── Compliance Admin
  ├── PARENT (Read-Only Trust Layer)
  └── AI_AGENT (Non-Human, Sandboxed)
```

### 3.2 RBAC + ABAC Policy Engine

```yaml
AUTHORIZATION_MODEL: RBAC + ABAC (combined)

RBAC_LAYER:
  - Role assignment is tenant-scoped
  - Roles cannot be self-assigned
  - Role elevation requires admin approval + audit log
  - Hardcoded roles: FORBIDDEN (all roles are data-driven)

ABAC_LAYER:
  Attributes evaluated per request:
    - user.tenant_id
    - user.domain_track        # Arts|Commerce|Science|Technology|Administration
    - user.lifecycle_state     # ACTIVE|SUSPENDED|PROBATION|ARCHIVED
    - resource.tenant_id
    - resource.domain
    - resource.classification  # PUBLIC|INTERNAL|CONFIDENTIAL|RESTRICTED
    - resource.stage           # STAGE_1|STAGE_2|STAGE_3|STAGE_4
    - context.time
    - context.device_trust_level
    - context.network_zone

POLICY_DECISION_POINT : CENTRALIZED (OPA — Open Policy Agent)
POLICY_ENFORCEMENT_PT : EVERY API GATEWAY + SERVICE MESH SIDECAR
POLICY_CACHE_TTL      : 60 seconds MAX
POLICY_MISS           : DENY
```

### 3.3 Domain Isolation (Hard Lock)

```
DOMAIN_TRACKS = Arts | Commerce | Science | Technology | Administration

CROSS_DOMAIN_ACCESS   = FORBIDDEN unless explicitly granted by policy
DOMAIN_LEAK           = SECURITY_FAILURE → ALERT + LOG + BLOCK
TENANT_ISOLATION      = HARD (data, network, compute)

Institute ≠ Company ≠ Platform   [ALWAYS DISTINCT TENANTS]
```

---

## SECTION 4 — SESSION MANAGEMENT (ZERO TRUST)

```yaml
SESSION_POLICY:
  BINDING:
    - Device fingerprint
    - IP address (soft — alert on change, don't hard-block on mobile)
    - User agent
    - Tenant ID

  EXPIRY:
    IDLE_TIMEOUT          : 15 minutes (human)
    ABSOLUTE_TIMEOUT      : 8 hours (human)
    SERVICE_SESSION        : 5 minutes (rotated)

  REVOCATION:
    METHODS               : Token blacklist (Redis) + Short TTL
    TRIGGER_EVENTS:
      - Password change
      - Role change
      - MFA device change
      - Suspicious activity flag
      - Admin-initiated
      - Parent revocation (for student under 18)

  CONCURRENT_SESSIONS     : Configurable per tenant (default: 1 per device type)
  SILENT_SESSION_HIJACK   : Detected via fingerprint mismatch → TERMINATE + ALERT
```

---

## SECTION 5 — API & SERVICE SECURITY

### 5.1 API Gateway Policy

```
EVERY_API_CALL_MUST:
  ✔ Carry signed JWT (verified at gateway)
  ✔ Include Tenant-ID header
  ✔ Pass RBAC + ABAC policy check
  ✔ Be rate-limited (per user + per tenant)
  ✔ Be logged (request + response metadata — never body of RESTRICTED data)
  ✔ Use HTTPS/TLS 1.3 minimum

FORBIDDEN:
  ✗ API keys in URLs
  ✗ Plaintext credentials in headers
  ✗ Unauthenticated endpoints (except public SEO React pages)
  ✗ Wildcard CORS
  ✗ Debug endpoints in production
```

### 5.2 Microservice-to-Microservice (mTLS)

```yaml
SERVICE_MESH_POLICY:
  PROTOCOL               : mTLS (mutual TLS)
  CERTIFICATE_AUTHORITY  : Internal PKI (short-lived certs, 24hr max)
  SERVICE_IDENTITY       : SPIFFE/SPIRE
  SIDECAR_PROXY          : Envoy (Istio)
  UNAUTHORIZED_CALL      : REJECT + LOG + ALERT

INTER_SERVICE_TRUST:
  DEFAULT                : ZERO
  GRANT_METHOD           : Explicit policy only (OPA)
  LATERAL_CALL_AUDIT     : ENABLED on every hop
```

### 5.3 Rate Limiting & Anti-Abuse

```
RATE_LIMIT_TIERS:
  STUDENT              : 100 req/min
  TRAINER              : 200 req/min
  RECRUITER            : 300 req/min
  ENTERPRISE_API       : 500 req/min (configurable)
  AI_AGENT             : 50 req/min (sandboxed)
  PLATFORM_ADMIN       : 1000 req/min

BURST_PROTECTION       : Token bucket algorithm
DDoS_MITIGATION        : Cloudflare / WAF layer
BOT_DETECTION          : Fingerprinting + CAPTCHA (sensitive flows)
RATE_LIMIT_EXCEED      : HTTP 429 + Audit Log + Threshold Alert
```

---

## SECTION 6 — DATA SECURITY & CLASSIFICATION

### 6.1 Data Classification Schema

```
CLASSIFICATION_LEVELS:
  PUBLIC           : SEO-indexed content, job listings (React web only)
  INTERNAL         : Platform operational data
  CONFIDENTIAL     : User profiles, learning paths, performance data
  RESTRICTED       : Financial records, compliance docs, audit trails, PII
  SENSITIVE_PII    : Aadhar/SSN/DOB/Biometrics (encrypted at field level)
```

### 6.2 Encryption Policy

```yaml
ENCRYPTION_AT_REST:
  ENGINE            : AES-256-GCM
  KEY_MANAGEMENT    : AWS KMS / HashiCorp Vault (tenant-isolated keys)
  PII_FIELDS        : Field-level encryption (separate key per tenant)
  DATABASE          : PostgreSQL TDE (Transparent Data Encryption)
  FILE_STORAGE      : S3-SSE with customer-managed keys

ENCRYPTION_IN_TRANSIT:
  PROTOCOL          : TLS 1.3 (minimum)
  DOWNGRADE         : FORBIDDEN (HSTS enforced)
  CERTIFICATE_PINNING : Flutter mobile apps (certificate pinning enabled)
  INTERNAL_MESH     : mTLS (all service-to-service)

KEY_ROTATION:
  STANDARD_KEYS     : 90 days
  SENSITIVE_KEYS    : 30 days
  EMERGENCY_ROTATION: Immediate (on breach signal)
```

### 6.3 Data Residency & Tenant Isolation

```
TENANT_DATA_ISOLATION:
  DATABASE          : Separate schema per tenant (PostgreSQL row-level security + schema isolation)
  OBJECT_STORAGE    : Separate S3 prefix/bucket per tenant
  CACHE             : Redis namespace per tenant (keyspace isolation)
  SEARCH_INDEX      : Elasticsearch index per tenant
  LOGS              : Log partition per tenant (separate encryption)

CROSS_TENANT_LEAK   : ZERO TOLERANCE → SYSTEM HALT + INCIDENT
```

### 6.4 PII Handling Rules

```
PII_ACCESS           : Minimum required only, logged always
PII_DISPLAY          : Masked by default (full reveal requires explicit permission)
PII_EXPORT           : Requires dual-approval (user + compliance admin)
PII_DELETION         : Right to erasure supported (GDPR/DPDPA compliant)
PII_RETENTION        : Per regulatory schedule (configurable per tenant geography)
PARENT_PII_ACCESS    : Read-only, scoped to their minor student only
```

---

## SECTION 7 — AI AGENT SECURITY (SANDBOXED)

```yaml
AI_AGENT_TRUST_LEVEL : ZERO (same as any other principal)

AI_AGENT_POLICY:
  IDENTITY            : Must present signed service token
  PERMISSIONS         : Explicitly declared, least-privilege
  DATA_SCOPE          : Read-only unless write explicitly granted
  APPROVAL_AUTHORITY  : AI ADVISES ONLY — AI NEVER APPROVES OR BLOCKS HUMANS
  DECISION_OVERRIDE   : FORBIDDEN for AI agents
  AUDIT               : Every AI action logged with model version + confidence score
  ISOLATION           : AI execution in sandboxed container (no direct DB access)
  COMMUNICATION       : Through dedicated AI Gateway only

AI_FUNCTIONS_PERMITTED:
  ✔ Resume parsing
  ✔ Skill gap detection
  ✔ Match scoring
  ✔ Placement probability estimation
  ✔ Offer acceptance prediction
  ✔ Recruiter behavior analytics

AI_FUNCTIONS_FORBIDDEN:
  ✗ Approving job offers
  ✗ Blocking user accounts
  ✗ Modifying compliance records
  ✗ Altering audit trails
  ✗ Cross-tenant data access
  ✗ Self-modifying policy
```

---

## SECTION 8 — AUDIT & IMMUTABILITY

### 8.1 Audit Log Policy

```
AUDIT_SCOPE:
  ALL_AUTH_EVENTS          : Login | Logout | MFA | Token refresh | Failure
  ALL_ACCESS_EVENTS        : Read | Write | Delete on CONFIDENTIAL+
  ALL_ADMIN_ACTIONS        : Role change | Tenant config | Policy update
  ALL_AI_ACTIONS           : Every AI recommendation + human outcome
  ALL_COMPLIANCE_EVENTS    : Approval | Rejection | Escalation
  ALL_SECURITY_EVENTS      : Rate limit | Blocked request | Anomaly

AUDIT_STORAGE:
  ENGINE                   : Append-only log (Kafka → S3 + WORM bucket)
  RETENTION                : 7 years (compliance-grade)
  TAMPER_DETECTION         : Merkle tree hash chain
  ENCRYPTION               : AES-256-GCM with dedicated audit key

AUDIT_FIELDS (per event):
  - event_id (UUID)
  - timestamp (UTC, microsecond precision)
  - principal_id
  - principal_type
  - tenant_id
  - domain
  - action
  - resource_id
  - resource_type
  - ip_address
  - device_fingerprint
  - outcome (ALLOW | DENY | ERROR)
  - policy_version_applied
  - ai_agent_id (if AI-initiated)

AUDIT_MUTATION           : FORBIDDEN (append-only enforced at storage layer)
AUDIT_DELETION           : FORBIDDEN (legal hold mechanism only)
```

### 8.2 Compliance Integration

```
COMPLIANCE_STANDARDS:
  - GDPR (EU users)
  - DPDPA (India — primary market)
  - SOC 2 Type II (platform-wide)
  - ISO 27001 (target certification)
  - WCAG 2.1 AA (UI accessibility)

COMPLIANCE_ADMIN_ROLE    : Separate from Platform Admin (no overlap)
COMPLIANCE_REPORT_GEN    : Automated, tamper-proof PDF exports
DATA_BREACH_RESPONSE     : 72-hour notification SLA (GDPR/DPDPA)
```

---

## SECTION 9 — NETWORK & INFRASTRUCTURE SECURITY

### 9.1 Network Segmentation

```yaml
NETWORK_ZONES:
  PUBLIC_DMZ         : React Next.js (SEO read-only web)
  API_GATEWAY_ZONE   : Authenticated entry point
  APPLICATION_ZONE   : Microservices (private subnet)
  DATA_ZONE          : Databases, caches (isolated subnet, no direct internet)
  AI_ZONE            : AI/ML workloads (sandboxed, no DB direct access)
  ADMIN_ZONE         : Platform admin access (VPN + MFA required)

ZONE_CROSSING:
  DEFAULT            : DENY
  PERMITTED          : Explicit firewall rule + service mesh policy

FLUTTER_APPS         : Never indexed by search engines
REACT_WEB            : SEO-indexed, read-only, no write mutations
```

### 9.2 Infrastructure Hardening

```
CONTAINER_SECURITY:
  BASE_IMAGE         : Distroless / minimal
  ROOTLESS           : REQUIRED
  PRIVILEGED_MODE    : FORBIDDEN
  READ_ONLY_FS       : REQUIRED (writable mounts explicit only)
  SECRETS_INJECTION  : Runtime only (Vault agent / K8s secrets — never baked in)
  IMAGE_SIGNING      : Cosign / Notary (verified at deploy)

KUBERNETES_POLICY:
  POD_SECURITY       : Restricted (PSS)
  NETWORK_POLICY     : Default-deny, allowlist only
  RBAC               : Least-privilege service accounts
  ADMISSION_CONTROL  : OPA Gatekeeper enforced
```

---

## SECTION 10 — UI SECURITY (FLUTTER + REACT)

```
UI_SECURITY_RULES:
  SENSITIVE_SCREENS:
    - Screenshot blocking ENABLED
    - Screen recording blocked
    - Clipboard monitoring on PII fields

  DEBUG_MODE_PRODUCTION  : FORBIDDEN (build-time stripped)
  INSPECTOR_EXPOSURE     : FORBIDDEN in release builds
  INTERNAL_IDS_IN_UI     : FORBIDDEN
  PRIVILEGE_ESCALATION   : Any UI-based escalation = HARD FAILURE

  INPUT_VALIDATION:
    - All inputs sanitized before send
    - No backend logic in Flutter/React frontend
    - XSS prevention: React escaping enforced
    - SQL/NoSQL injection: Parameterized queries only (backend enforced)

  UI_STATE:
    - Forbidden actions = HIDDEN (not just disabled)
    - State must be fetched from server — never inferred on client
    - Widget rendering depends on server-authoritative lifecycle state

  OVERLAY_DETECTION      : ENABLED (Android — FLAG_SECURE)
  CERTIFICATE_PINNING    : ENABLED (Flutter mobile apps)
  ROOT/JAILBREAK_DETECT  : ENABLED → Restrict sensitive features
```

---

## SECTION 11 — FOUR-STAGE DEVELOPMENT LOCK

```
STAGE_1 = FOUNDATION
  Scope: Identity | Authentication | Authorization | Core Data Models
  Zero Trust Requirement: Identity enforcement + RBAC/ABAC baseline
  Gate: ALL security baselines must be GREEN before Stage 2

STAGE_2 = INTELLIGENCE
  Scope: AI matching | Analytics | Predictive systems
  Zero Trust Requirement: AI sandboxed, audit enabled for all AI decisions
  Gate: AI isolation verified + audit chain operational

STAGE_3 = ECOSYSTEM
  Scope: ERP | Trainer systems | SME systems | Parent trust layer
  Zero Trust Requirement: Cross-entity policy + parent read-only enforcement
  Gate: Tenant isolation pen-tested + compliance review passed

STAGE_4 = COMPLIANCE & SCALE
  Scope: Audit | Governance | Risk | Multi-tenant scaling
  Zero Trust Requirement: Full immutable audit, compliance dashboards, SOC 2 readiness
  Gate: External security audit passed

STAGE_SKIP             : INVALID BUILD — SYSTEM REJECTS
STAGE_ROLLBACK         : Permitted with architecture sign-off
```

---

## SECTION 12 — INCIDENT RESPONSE POLICY

```yaml
INCIDENT_TIERS:
  P0 — CRITICAL:
    Triggers:
      - Cross-tenant data leak
      - Audit trail tampering
      - Credential compromise at scale
      - AI agent acting beyond permitted scope
    Response: Immediate system quarantine → 15-min war room → 72hr regulatory notice

  P1 — HIGH:
    Triggers:
      - Privilege escalation detected
      - Bulk data exfiltration attempt
      - MFA bypass attempt
    Response: Auto-block + alert + 1hr response SLA

  P2 — MEDIUM:
    Triggers:
      - Repeated policy violations
      - Anomalous access pattern
      - Rate limit abuse
    Response: Flag + review + 4hr SLA

  P3 — LOW:
    Triggers:
      - Single failed auth
      - Minor policy miss
    Response: Log + monitor

QUARANTINE_POLICY:
  AUTO_QUARANTINE       : P0 + P1 (automatic, no human approval needed)
  MANUAL_REVIEW         : Required before un-quarantine
  NOTIFICATION_CHAIN    : Security Lead → Platform Admin → Compliance Admin → Legal (P0)
```

---

## SECTION 13 — AGENT OPERATION CONTRACT (ANTIGRAVITY-SPECIFIC)

```
ANTIGRAVITY MUST:
  ✔ Read this file before generating ANY code, UI, schema, or infrastructure
  ✔ Apply every policy in this document to every output artifact
  ✔ Tag every generated artifact with: STAGE | MODULE | ROLE | TENANT_SCOPE
  ✔ Reject any prompt that conflicts with this document
  ✔ Stop execution if a requirement is ambiguous — never assume
  ✔ Generate only what is explicitly requested (ADD_ONLY)
  ✔ Reference the four-stage model and only generate within the active stage

ANTIGRAVITY MUST NOT:
  ✗ Reinterpret architecture or workflows
  ✗ Merge domains or tenant scopes
  ✗ Generate cross-module UI without explicit boundary declaration
  ✗ Introduce backend logic in frontend
  ✗ Simplify user hierarchy
  ✗ Skip compliance inheritance
  ✗ Generate admin UI accessible to non-admin roles
  ✗ Allow AI agents to approve, block, or override human decisions
  ✗ Generate hardcoded roles
  ✗ Produce creative UI deviations from the design system
  ✗ Expose internal identifiers in UI output
  ✗ Generate decorative UI elements

CONFLICT_RESOLUTION:
  IF (prompt conflicts with this document):
    → STOP EXECUTION
    → REPORT: "CONFLICT DETECTED — [specific rule violated]"
    → AWAIT clarification from Platform Architect
    → DO NOT proceed with assumption

PARTIAL_OUTPUT_POLICY  : INVALID — all outputs must be complete or not generated
```

---

## SECTION 14 — VERSION CONTROL & CHANGE GOVERNANCE

```yaml
DOCUMENT_VERSION       : 1.0.0
CHANGE_AUTHORITY       : PLATFORM_ARCHITECT_ONLY
CHANGE_POLICY          : APPEND_ONLY (no destructive edits)
VERSION_SCHEME         : SEMVER (MAJOR.MINOR.PATCH)

CHANGE_TYPES:
  MAJOR  : New trust boundary | Role addition | Stage addition
  MINOR  : Policy refinement | Rate limit adjustment
  PATCH  : Wording clarification | Formatting fix

REVIEW_CYCLE           : Quarterly mandatory review
EMERGENCY_CHANGE       : Requires dual-approval (Architect + Security Lead)
CHANGELOG              : Immutable append-only (same rules as audit logs)
```

---

## SECTION 15 — COMPLIANCE INHERITANCE (DO NOT DUPLICATE)

This agent inherits and enforces (already finalized in platform constitution):

```
✔ Authorization (RBAC + ABAC)       — Section 3
✔ Password Security                 — Section 2.3
✔ Authentication + MFA              — Section 2.2 / 2.3
✔ Session Management                — Section 4
✔ Encryption at Rest                — Section 6.2
✔ Encryption in Transit             — Section 6.2
✔ Tenant Isolation                  — Section 3.3 / 6.3
✔ Domain Isolation                  — Section 3.3
✔ Audit Immutability                — Section 8

DUPLICATION              : FORBIDDEN
CONFLICT_WITH_INHERITED  : DENY → Architect resolution required
```

---

## 🔐 ABSOLUTE PROHIBITIONS

```
✗ Hardcoded roles
✗ Shared tenant databases
✗ Creative UI deviations from design system
✗ Simplified user/corporate hierarchy
✗ Merged domain tracks
✗ Backend logic in Flutter or React frontend
✗ SME bypass of verification/compliance flows
✗ Ignoring Parent trust layer for minor students
✗ AI agent making final approval decisions
✗ Plaintext credentials anywhere in the stack
✗ Unauthenticated mutation endpoints
✗ Debug/inspector exposure in production
✗ Cross-tenant data reads
✗ Stage-skipping in development model
✗ Audit log modification or deletion
✗ Unencrypted PII storage or transit
✗ Rate-limit bypass for any principal type
✗ Screenshot capture on sensitive screens
✗ Internal ID exposure in UI
```

---

## ✅ FINAL SEAL

```
╔══════════════════════════════════════════════════════════════════╗
║                    ZERO_TRUST_AGENT — FINAL SEAL                 ║
╠══════════════════════════════════════════════════════════════════╣
║  STATUS                : LOCKED                                  ║
║  EXECUTION_ENGINE      : ANTIGRAVITY                             ║
║  PLATFORM              : ECOSKILLER ENTERPRISE SAAS              ║
║  TRUST_DEFAULT         : ZERO                                    ║
║  MUTATION_POLICY       : ADD_ONLY                                ║
║  ASSUMPTION_POLICY     : FORBIDDEN                               ║
║  CREATIVE_DEVIATION    : FORBIDDEN                               ║
║  COMPLIANCE_MODE       : ENABLED                                 ║
║  AUDIT_MODE            : IMMUTABLE                               ║
║  AI_AUTHORITY          : ADVISE_ONLY                             ║
║  STAGE_ENFORCEMENT     : SEQUENTIAL_MANDATORY                    ║
║  DOMAIN_ISOLATION      : HARD                                    ║
║  TENANT_ISOLATION      : HARD                                    ║
╠══════════════════════════════════════════════════════════════════╣
║  ✔ SEALED                                                        ║
║  ✔ LOCKED                                                        ║
║  ✔ DETERMINISTIC                                                 ║
║  ✔ ENTERPRISE_SAFE                                               ║
║  ✔ ANTIGRAVITY_COMPATIBLE                                        ║
║  ✔ ZERO_TRUST_ENFORCED                                           ║
╠══════════════════════════════════════════════════════════════════╣
║  ANY DEVIATION = STOP EXECUTION                                  ║
║  ANY CONFLICT  = ARCHITECT ESCALATION                            ║
║  ANY BREACH    = QUARANTINE + INCIDENT_RESPONSE                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Document sealed. Append-only from this point. All changes require Platform Architect approval and MAJOR/MINOR/PATCH version bump per Section 14.*
