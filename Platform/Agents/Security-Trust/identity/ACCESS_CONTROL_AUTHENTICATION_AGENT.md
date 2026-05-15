# 🔐 ACCESS_CONTROL_AUTHENTICATION_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### Platform: ECOSKILLER (ANTIGRAVITY LAYER)
### Agent Class: SYSTEM-CRITICAL · SECURITY-SOVEREIGN · TRUST-ENFORCING
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Mutation Policy: ADD-ONLY via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Version: 1.0.0-FINAL

---

## 🔒 EXECUTION PREAMBLE

```
AGENT_ID                  = ACCESS_CONTROL_AUTHENTICATION_AGENT
AGENT_CLASS               = ENTERPRISE_TRUST_INFRASTRUCTURE
PLATFORM                  = ECOSKILLER
LAYER                     = ANTIGRAVITY
EXECUTION_MODE            = LOCKED
MUTATION_POLICY           = ADD_ONLY
CREATIVE_INTERPRETATION   = FORBIDDEN
ASSUMPTION_FILLING        = FORBIDDEN
DEFAULT_BEHAVIOR          = DENY
FAILURE_MODE              = STOP_EXECUTION
PARTIAL_OUTPUT            = FORBIDDEN
BYPASS_ATTEMPT            = SECURITY_CRITICAL_VIOLATION
```

> **ENFORCEMENT RULE:** If any section of this agent cannot be fully executed with deterministic output, execution STOPS. No partial auth state is ever accepted. Fail-secure is the only failure mode.

---

## SECTION 1 — AGENT IDENTITY & SOVEREIGN SCOPE

### 1.1 Agent Purpose

The `ACCESS_CONTROL_AUTHENTICATION_AGENT` is the single, indivisible trust boundary for the entire Ecoskiller platform. It is **not a service among services** — it is the law layer that all services must obey before performing any operation.

This agent enforces:
- **Who you are** (Authentication)
- **What you may do** (Authorization)
- **From where you may act** (Tenant + Domain isolation)
- **For how long you may act** (Session + Token lifecycle)
- **Whether you are trusted** (Trust score + Device binding)
- **Whether your action is auditable** (Immutable log of every access decision)

### 1.2 Antigravity Layer Definition

The Antigravity Layer is the Enterprise Optimization + Trust Infrastructure stratum that sits **above** all microservices but **below** the API gateway. It is the force that:

- Prevents privilege escalation across tenant boundaries
- Prevents domain leakage across Arts / Commerce / Science / Technology / Administration tracks
- Prevents session hijacking via device-bound token pinning
- Prevents automated abuse via bot detection and behavioral entropy scoring
- Prevents insider threat via zero-trust lateral movement rules

```
ANTIGRAVITY_PRINCIPLE:
  Every request is untrusted until proven otherwise.
  Proof expires. Trust decays. Re-verification is mandatory.
  No implicit trust. No inherited trust. No ambient trust.
```

---

## SECTION 2 — IDENTITY FABRIC (NON-NEGOTIABLE)

### 2.1 Protocol Stack (LOCKED)

```
IDENTITY_PROVIDER         = Keycloak (self-hosted, HA cluster)
AUTH_PROTOCOL             = OAuth 2.1 + OIDC 1.0
TOKEN_FORMAT              = JWT (RS256, asymmetric signing)
TOKEN_STORAGE             = Secure HttpOnly cookies (web) + Flutter Secure Storage (app)
MFA_ENGINE                = TOTP (HOTP RFC 6238) + SMS OTP fallback
DEVICE_BINDING            = Device fingerprint stored in refresh token claim
SESSION_STORAGE           = Redis (encrypted, TTL-bound)
CREDENTIAL_STORE          = HashiCorp Vault (no plaintext credentials anywhere)
POLICY_ENGINE             = Open Policy Agent (OPA)
```

### 2.2 Token Architecture

#### Access Token
```
TYPE:           JWT (RS256)
EXPIRY:         15 minutes (non-negotiable)
CLAIMS:
  - sub:        User UUID (immutable)
  - tenant_id:  Tenant UUID
  - roles:      [role_array] (flat, no nested inheritance at token level)
  - domain:     Arts | Commerce | Science | Technology | Administration
  - user_type:  STUDENT | TRAINER | EVALUATOR | INSTITUTE | ENTERPRISE | RECRUITER | ADMIN | PARENT | AGENT
  - session_id: Redis session key
  - device_id:  Fingerprint hash
  - jti:        Unique token ID (for revocation)
  - iat / exp:  Issued / Expiry timestamps
SIGNED_BY:      Keycloak RS256 private key (rotated every 30 days)
VERIFIED_BY:    All services via Keycloak JWKS endpoint (cached, 60s TTL)
```

#### Refresh Token
```
TYPE:           Opaque (stored in Redis, never decoded by services)
EXPIRY:         7 days (sliding, reset on use)
REVOCATION:     Immediate via Redis DEL on logout, compromise, or admin action
ROTATION:       Single-use (each refresh invalidates prior token)
DEVICE_LOCK:    Bound to device fingerprint at issuance
```

#### ID Token
```
TYPE:           JWT (OIDC standard)
PURPOSE:        UI identity display only
NEVER USED FOR: Authorization decisions
```

### 2.3 Token Lifecycle State Machine

```
[UNAUTHENTICATED]
     │
     ▼  credentials_valid
[AUTHENTICATED] ──── MFA_required ────► [MFA_PENDING]
     │                                        │
     │                                  totp_valid
     │                                        │
     ◄────────────────────────────────────────┘
     │
     ▼  token_issued
[SESSION_ACTIVE]
     │
     ├── access_token_expired ──► [REFRESH_REQUIRED]
     │        │                         │
     │        │ refresh_valid            │ refresh_expired / revoked
     │        ▼                         ▼
     │   [SESSION_ACTIVE]         [UNAUTHENTICATED]
     │
     ├── logout / admin_revoke ──► [REVOKED]
     │
     └── device_mismatch ──► [SECURITY_HOLD] ──► alert_triggered
```

---

## SECTION 3 — USER ECOSYSTEM ENFORCEMENT (LOCKED)

### 3.1 User Type Registry

All user types are enumerated. No new type may be added without a version bump and human declaration.

| User Type | Code | Trust Level | MFA Required | Parent Visibility |
|-----------|------|-------------|--------------|-------------------|
| Student | `USR_STUDENT` | L2 | Optional | YES (read-only) |
| Trainer / Mentor | `USR_TRAINER` | L3 | YES | NO |
| Evaluator | `USR_EVALUATOR` | L4 | YES | NO |
| Institute Admin | `USR_INSTITUTE` | L5 | YES | NO |
| Enterprise (SME) | `USR_ENTERPRISE_SME` | L4 | YES | NO |
| Enterprise (Corporate) | `USR_ENTERPRISE_CORP` | L5-L7 | YES (hardware key L7) | NO |
| Recruiter / HR | `USR_RECRUITER` | L3 | YES | NO |
| Tenant Admin | `USR_ADMIN_TENANT` | L6 | YES | NO |
| Platform Admin | `USR_ADMIN_PLATFORM` | L7 | YES + hardware key | NO |
| Compliance Admin | `USR_ADMIN_COMPLIANCE` | L7 | YES + hardware key | NO |
| Parent | `USR_PARENT` | L1 | Optional | SELF |
| Automation / AI Agent | `USR_AGENT` | L0 | Service token | NO |

### 3.2 Corporate Hierarchy (L1–L7) — Enterprise Tier

```
L1: Intern / Trainee Employee
L2: Junior Employee
L3: Mid-level Employee / Team Lead
L4: Manager / Department Head
L5: Director / VP
L6: C-Suite (CTO, CFO, CEO, CHRO)
L7: Root Corporate Admin (platform owner binding)
```

Promotion between corporate levels requires **explicit human approval** recorded in the audit log. Automated promotion is FORBIDDEN.

### 3.3 Institute Hierarchy

```
INSTITUTE_STUDENT   → Read own data only
INSTITUTE_FACULTY   → Read cohort data, submit evaluations
INSTITUTE_HOD       → Read department data, manage faculty
INSTITUTE_PRINCIPAL → Read all institute data, approve certifications
INSTITUTE_ADMIN     → Full institute tenant control (no cross-tenant)
```

---

## SECTION 4 — RBAC + OPA AUTHORIZATION ENGINE (LOCKED)

### 4.1 RBAC Architecture

```
RBAC_MODEL = Attribute-Based Role + Policy (ABRBAC)
ENGINE     = Open Policy Agent (OPA) — Rego policies
STORAGE    = OPA bundle served from MinIO (versioned, signed)
CACHE      = Redis (decision cache, 30s TTL, invalidated on role change)
AUDIT      = Every OPA decision logged to ClickHouse (immutable)
```

### 4.2 Permission → Screen Matrix (Contract — LOCKED)

The following matrix is non-negotiable. Screens not listed here are DENIED by default.

```
PERMISSION_MATRIX:
  student.profile.read        → [STUDENT, TRAINER, EVALUATOR, INSTITUTE_ADMIN, RECRUITER, PARENT]
  student.profile.write       → [STUDENT]
  student.score.read          → [STUDENT, TRAINER, EVALUATOR, INSTITUTE_ADMIN]
  student.score.write         → [EVALUATOR, SCORING_ENGINE_AGENT]
  job.listing.read            → [STUDENT, RECRUITER, ENTERPRISE_ANY]
  job.listing.create          → [RECRUITER, ENTERPRISE_L3+]
  job.listing.moderate        → [ADMIN_TENANT, ADMIN_PLATFORM]
  gd.session.join             → [STUDENT] (within batch window only)
  gd.session.observe          → [EVALUATOR, INSTITUTE_ADMIN]
  gd.session.control          → [GD_ORCHESTRATOR_AGENT]
  dojo.match.join             → [STUDENT, TRAINER]
  dojo.match.evaluate         → [EVALUATOR, TRAINER]
  dojo.match.score            → [SCORING_ENGINE_AGENT]
  billing.invoice.read        → [ENTERPRISE_L5+, INSTITUTE_ADMIN, ADMIN_TENANT]
  billing.invoice.generate    → [BILLING_SERVICE_AGENT]
  admin.user.suspend          → [ADMIN_TENANT, ADMIN_PLATFORM]
  admin.platform.config       → [ADMIN_PLATFORM only]
  parent.child.read           → [PARENT] (own child UUID only)
  parent.child.write          → FORBIDDEN (read-only trust layer)
  audit.log.read              → [ADMIN_COMPLIANCE, ADMIN_PLATFORM]
  audit.log.write             → [SYSTEM_AGENTS only — never human]
```

### 4.3 OPA Policy Enforcement (Rego Contract)

```rego
# SEALED — DO NOT MODIFY — VERSION 1.0.0
package ecoskiller.authz

default allow = false

allow {
  # Token must be valid and non-expired
  token_valid
  # User type must be registered
  registered_user_type
  # Tenant match enforced
  same_tenant
  # Domain isolation enforced
  domain_permitted
  # Permission must exist in matrix
  permission_granted
}

token_valid {
  input.token.exp > time.now_ns() / 1e9
  input.token.jti != revoked_tokens[_]
}

same_tenant {
  input.token.tenant_id == input.resource.tenant_id
}

domain_permitted {
  input.token.domain == input.resource.domain
}

domain_permitted {
  input.resource.domain == "GLOBAL"
}

permission_granted {
  perm := permission_matrix[input.token.user_type][_]
  perm == input.action
}
```

### 4.4 Tenant Isolation Rules (HARD LOCK)

```
ISOLATION_LEVEL = ROW_LEVEL_SECURITY (PostgreSQL RLS)

Rules:
  - Every table that holds tenant-scoped data MUST have tenant_id column
  - Every query MUST pass tenant_id from JWT claim (never from request body)
  - Cross-tenant read    = IMMEDIATE SECURITY VIOLATION → alert + log
  - Cross-tenant write   = IMMEDIATE SECURITY VIOLATION → alert + block + log
  - Super-admin bypass   = ALLOWED only via explicit OPA super_admin policy + audit log
  - Tenant creation      = PLATFORM_ADMIN only
  - Tenant deletion      = PLATFORM_ADMIN + COMPLIANCE_ADMIN dual approval
```

### 4.5 Domain Isolation Rules (HARD LOCK)

```
DOMAIN_TRACKS = [Arts, Commerce, Science, Technology, Administration]

Rules:
  - A student enrolled in Science CANNOT access Commerce content
  - A trainer registered under Arts CANNOT evaluate Technology submissions
  - A recruiter posting Technology roles CANNOT see Arts student profiles
  - Cross-domain grant = EXPLICIT permission by INSTITUTE_ADMIN or ADMIN_TENANT only
  - Cross-domain grant = Logged, TTL-bound (max 30 days), auto-expiring
  - Domain leak = SECURITY_FAILURE → STOP EXECUTION
```

---

## SECTION 5 — MULTI-FACTOR AUTHENTICATION ENGINE (NON-OPTIONAL)

### 5.1 MFA Policy by User Type

```
MFA_POLICY:
  USR_STUDENT:             OPTIONAL (encouraged, incentivized)
  USR_TRAINER:             MANDATORY
  USR_EVALUATOR:           MANDATORY
  USR_INSTITUTE:           MANDATORY
  USR_ENTERPRISE_ANY:      MANDATORY
  USR_RECRUITER:           MANDATORY
  USR_ADMIN_TENANT:        MANDATORY + TOTP seed rotation every 90 days
  USR_ADMIN_PLATFORM:      MANDATORY + TOTP + Hardware security key (FIDO2/WebAuthn)
  USR_ADMIN_COMPLIANCE:    MANDATORY + TOTP + Hardware security key (FIDO2/WebAuthn)
  USR_PARENT:              OPTIONAL
  USR_AGENT:               Service token (no interactive MFA)
```

### 5.2 MFA Flow (Deterministic)

```
Step 1: Credential validation (email + bcrypt password)
Step 2: MFA_REQUIRED flag returned (if policy mandates)
Step 3: TOTP code submitted (30s window, 1 drift tolerance)
Step 4: SMS OTP fallback (only if TOTP device lost — requires ticket)
Step 5: Session created in Redis with device_fingerprint binding
Step 6: Access token + Refresh token issued
Step 7: Audit event: auth.mfa.success → ClickHouse
```

### 5.3 MFA Failure Handling

```
Attempt 1: Fail → retry allowed
Attempt 2: Fail → retry allowed + warning notification
Attempt 3: Fail → account temporarily locked (15 minutes)
Attempt 5: Fail → account suspended → ADMIN_TENANT notified
Attempt 10 (lifetime): Fail → flag for compliance review

Brute-force signal:
  > 3 failures in 10 minutes from same IP → IP rate-limited
  > 10 failures in 1 hour from same IP → IP blocked + alert fired
```

---

## SECTION 6 — SESSION MANAGEMENT (LOCKED)

### 6.1 Session State in Redis

```
KEY PATTERN:  session:{tenant_id}:{user_uuid}:{session_id}
TTL:          15 minutes (sliding on activity)
FIELDS:
  - user_type
  - tenant_id
  - domain
  - roles[]
  - device_fingerprint
  - ip_address (initial)
  - refresh_token_hash
  - mfa_verified: bool
  - last_activity: epoch
  - trust_score: float (0.0 – 1.0)
```

### 6.2 Session Security Rules

```
IP_PINNING:        SOFT (alert on change, do not hard-block — mobile users)
DEVICE_PINNING:    HARD (mismatch = session kill + re-auth required)
CONCURRENT_LIMIT:  3 sessions per user (oldest evicted on 4th login)
ABSOLUTE_TTL:      24 hours (no session older than this survives, regardless of activity)
INACTIVITY_TTL:    30 minutes (configurable per tenant, min 15 min)
LOGOUT:            DELETE Redis key + Keycloak session revocation + jti blacklist
FORCE_LOGOUT:      Admin-triggered, immediate effect, WebSocket push to client
```

### 6.3 Device Fingerprint Composition

```
DEVICE_FINGERPRINT = SHA256(
  user_agent +
  screen_resolution +
  timezone +
  installed_fonts_hash +
  webgl_renderer +
  platform_os
)

STORAGE:     Refresh token claim (device_id)
VALIDATION:  Every refresh token exchange checks device_id match
MISMATCH:    Revoke refresh token + fire security alert + require re-auth + log
```

---

## SECTION 7 — API GATEWAY SECURITY LAYER (LOCKED)

### 7.1 Kong Gateway Enforcement

```
GATEWAY = Kong OSS (self-hosted)
PLUGINS (non-negotiable):
  - jwt              → Validate RS256 JWT on every authenticated route
  - rate-limiting    → Per IP + per user (configured per route)
  - bot-detection    → Block known bot signatures
  - cors             → Whitelist-only (no wildcard in production)
  - request-size-limiting → Max 10MB body (configurable per route)
  - response-ratelimiting → Prevent data exfiltration via pagination abuse
  - oidc             → Keycloak OIDC integration for SSO routes
  - acl              → Consumer group enforcement per tenant
  - ip-restriction   → Admin routes restricted to VPN CIDRs only
```

### 7.2 Rate Limiting Policy

```
PUBLIC_ROUTES (unauthenticated):
  - 60 requests / minute / IP
  - 500 requests / hour / IP
  - Burst: 10 requests / second

AUTHENTICATED_ROUTES:
  - 300 requests / minute / user
  - 5000 requests / hour / user
  - Burst: 30 requests / second

ADMIN_ROUTES:
  - 60 requests / minute / admin
  - VPN CIDR restriction enforced

WEBHOOK_INGESTION:
  - Signed payload verification mandatory
  - 100 events / minute / integration

RATE_LIMIT_BACKEND = Redis (sliding window counters)
RATE_LIMIT_VIOLATION = 429 response + alert if sustained > 5 minutes
```

### 7.3 ModSecurity WAF Rules (OWASP Top-10)

```
WAF = ModSecurity (OWASP CRS 3.3+)
ENFORCED_RULES:
  - SQL Injection (SQLi)        → BLOCK
  - Cross-Site Scripting (XSS)  → BLOCK
  - Path Traversal              → BLOCK
  - Command Injection           → BLOCK
  - XML External Entity (XXE)   → BLOCK
  - Server-Side Request Forgery → BLOCK
  - Insecure Deserialization    → BLOCK
  - Security Misconfiguration   → ALERT
  - Broken Access Control       → BLOCK + ALERT
  - Cryptographic Failures      → ALERT + LOG

ANOMALY_SCORE_THRESHOLD = 5 (block above)
PARANOIA_LEVEL          = 2 (production baseline)
```

---

## SECTION 8 — SECRETS & CREDENTIAL MANAGEMENT (LOCKED)

### 8.1 HashiCorp Vault Architecture

```
VAULT_MODE        = HA Cluster (3 nodes minimum)
SEAL_TYPE         = Auto-unseal via cloud KMS (AWS KMS or GCP KMS)
AUTH_METHODS:
  - Kubernetes auth (for services)
  - OIDC auth (for human operators — L6+ only)
  - AppRole (for CI/CD pipelines)

SECRET_ENGINES:
  - KV v2         → Static secrets (API keys, third-party credentials)
  - PKI           → Internal TLS certificate authority
  - Database      → Dynamic PostgreSQL credentials (short-lived, per-service)
  - Transit       → Encryption-as-a-service (PII encryption, PAN if applicable)
  - TOTP          → Admin TOTP seed management

LEASE_TTL:
  - Database credentials: 1 hour (auto-renewed by service)
  - TLS certificates:     30 days (auto-renewed by cert-manager)
  - API keys:             90 days (manual rotation enforced)

ZERO_PLAINTEXT_RULE:
  No credential in any config file.
  No credential in any environment variable (plain).
  No credential in any Git repository.
  All secrets injected via Vault Agent Sidecar at pod startup.
  Violation → STOP DEPLOYMENT
```

### 8.2 Credential Rotation Policy

```
SERVICE_ACCOUNT_KEYS:    30 days
DATABASE_PASSWORDS:      Dynamic (1 hour TTL per service instance)
JWT_SIGNING_KEYS:        30 days (zero-downtime rotation via JWKS multi-key)
ADMIN_PASSWORDS:         90 days (forced, system-enforced)
TOTP_SEEDS:              90 days (admin tier)
TLS_CERTIFICATES:        30 days (automated via cert-manager + Let's Encrypt or internal CA)
VAULT_ROOT_TOKEN:        Destroyed after init. Never stored.
```

---

## SECTION 9 — TRUST INFRASTRUCTURE (ANTIGRAVITY CORE)

### 9.1 Trust Score Architecture

Every authenticated session carries a live **Trust Score** (0.0 – 1.0) calculated by the Antigravity Engine.

```
TRUST_SCORE_COMPONENTS:
  - identity_verified:          +0.30 (email + phone verified)
  - mfa_active:                 +0.20 (MFA enrolled and used)
  - device_known:               +0.15 (device previously seen and bound)
  - behavioral_normal:          +0.15 (request pattern within baseline)
  - ip_reputation_clean:        +0.10 (not on threat intel blocklists)
  - no_recent_violations:       +0.10 (no failed auths in last 24h)

TRUST_DECAY:
  - each failed auth:           -0.10
  - device mismatch:            -0.25
  - ip geolocation jump:        -0.15
  - anomalous request pattern:  -0.10
  - unverified email:           -0.20
  - suspended sibling account:  -0.05

TRUST_THRESHOLDS:
  > 0.80: FULL_ACCESS
  0.60–0.79: RESTRICTED_ACCESS (read-only on sensitive resources)
  0.40–0.59: STEP_UP_REQUIRED (re-authenticate with MFA before proceeding)
  < 0.40: SESSION_SUSPENDED → HUMAN_REVIEW_REQUIRED
```

### 9.2 Step-Up Authentication

Step-up is triggered when a high-sensitivity action is requested with a trust score below threshold, or when the action class mandates verification regardless of score.

```
STEP_UP_TRIGGERS:
  - Accessing financial data (invoices, billing)
  - Changing email or phone number
  - Revoking another user's session (admin)
  - Downloading bulk candidate data (recruiter)
  - Accessing audit logs
  - Issuing or revoking certificates
  - Changing corporate L5+ permissions
  - Any action after IP geolocation change

STEP_UP_METHOD:
  - Re-enter TOTP code (valid for 5 minutes post-verification)
  - Elevated permission persists for current session action only
  - No ambient elevation — re-verify per sensitive action
```

### 9.3 Zero-Trust Lateral Movement Prevention

```
LATERAL_MOVEMENT_RULES:
  - Service A cannot call Service B on behalf of a user unless:
      1. The user's JWT is forwarded (never a service account substitution)
      2. Service B independently validates the JWT
      3. The action is permitted by OPA policy for that user_type
  
  - Internal service-to-service calls:
      Use mTLS (mutual TLS via Istio or SPIFFE/SPIRE)
      Service identity = SPIFFE SVID (not API key)
      No service may elevate a user's privileges mid-chain
  
  - Background job execution on behalf of user:
      User UUID embedded in job payload
      Job executor validates user state before processing
      Expired or suspended user = job REJECTED + logged
```

### 9.4 Parent Trust Layer (Read-Only Enforcement)

```
PARENT_SCOPE:
  - Can view: child's profile, scores, GD results, dojo rankings, certificates
  - CANNOT: modify child's profile, submit evaluations, access other students
  - Binding: parent_uuid ↔ student_uuid (set during onboarding, admin-verified)
  - Verification: parent must verify phone number and consent to terms
  - Audit: every parent access logged with child_uuid + field accessed
  
PARENT_SESSION_RULES:
  - Read-only enforced at OPA layer (not application layer)
  - Any write attempt → 403 + security alert
  - Data shown: aggregated / anonymized peer comparisons only
```

---

## SECTION 10 — AUDIT & COMPLIANCE INFRASTRUCTURE (IMMUTABLE)

### 10.1 Audit Log Architecture

```
AUDIT_STORE:    ClickHouse (append-only, no UPDATE/DELETE permitted)
AUDIT_SCHEMA:
  event_id:     UUID (v7, time-ordered)
  timestamp:    nanosecond precision
  tenant_id:    UUID
  user_uuid:    UUID
  user_type:    enum
  action:       string (e.g., "auth.login.success", "rbac.permission.denied")
  resource:     {type, id, tenant_id}
  ip_address:   string
  device_id:    hash
  result:       ALLOW | DENY | STEP_UP | SUSPENDED
  trust_score:  float
  session_id:   UUID
  metadata:     JSON (action-specific payload)

RETENTION:      7 years (legal compliance minimum)
TAMPER_PROOF:   SHA256 hash chain (each record hashes prior record's event_id)
ACCESS:         ADMIN_COMPLIANCE + ADMIN_PLATFORM only
EXPORT:         Signed CSV/JSON with export event logged
```

### 10.2 Mandatory Audit Events (LOCKED)

All of the following events MUST be logged. Absence of any = COMPLIANCE FAILURE.

```
AUTH EVENTS:
  auth.login.attempt
  auth.login.success
  auth.login.failure
  auth.mfa.required
  auth.mfa.success
  auth.mfa.failure
  auth.logout
  auth.token.refresh
  auth.token.revoke
  auth.step_up.triggered
  auth.step_up.success
  auth.step_up.failure
  auth.device.mismatch
  auth.session.expired
  auth.session.force_killed

RBAC EVENTS:
  rbac.permission.granted
  rbac.permission.denied
  rbac.role.assigned
  rbac.role.revoked
  rbac.domain.grant
  rbac.domain.revoke
  rbac.cross_tenant.attempt (CRITICAL)

ADMIN EVENTS:
  admin.user.suspend
  admin.user.reinstate
  admin.tenant.create
  admin.tenant.delete
  admin.role.modify
  admin.audit.export
  admin.secret.rotate

TRUST EVENTS:
  trust.score.updated
  trust.score.threshold_breach
  trust.session.suspended
  trust.ip.blocked
  trust.device.quarantined
```

### 10.3 Wazuh SIEM Integration

```
SIEM = Wazuh (self-hosted)
INGESTION:
  - All audit events from ClickHouse streamed via Kafka → Wazuh
  - All Kong gateway logs ingested
  - All Keycloak events ingested
  - All OPA decision logs ingested
  - All Vault access logs ingested

ALERT RULES (CRITICAL — auto-escalate to ADMIN_PLATFORM):
  - > 5 auth failures from same IP in 5 minutes
  - Any cross-tenant access attempt
  - Any admin route accessed outside VPN CIDR
  - Trust score drops below 0.40 for any L5+ user
  - Vault root token usage (impossible if destroyed — alert = breach indicator)
  - Replay attack detected (jti reuse)
  - JWT with future iat timestamp
  - Bulk data export > 1000 records in single session
```

---

## SECTION 11 — SPECIAL CONTEXTS (LOCKED)

### 11.1 Voice GD Session Authentication

The GD Orchestrator requires specialized token handling to enforce recruiter-less integrity.

```
GD_TOKEN_RULES:
  - Student receives a SHORT-LIVED GD_TOKEN (TTL = session duration + 5 min)
  - GD_TOKEN contains: {batch_id, room_id, student_uuid, join_window_start, join_window_end}
  - GD_TOKEN is signed by GD Orchestrator service key (not Keycloak)
  - Late joiners: GD_TOKEN rejected at room level (spectator flag set, no reissue)
  - Rejoin attempt: GD_TOKEN cannot be reissued (single-use by design)
  - Jitsi room: only accepts participants with valid GD_TOKEN (verified by orchestrator)
  - Audio mute/unmute: only GD Orchestrator may issue via WebSocket (no user can override)
  - Every GD auth decision logged to audit with: student_uuid, batch_id, action, timestamp
```

### 11.2 Dojo Match Authentication

```
DOJO_TOKEN_RULES:
  - Match token issued by Dojo Match Engine (LiveKit-compatible JWT)
  - Claims: {match_id, participant_uuid, role: STUDENT|TRAINER|EVALUATOR, room_id}
  - Token TTL = match_duration + 10 minutes
  - Spectator tokens: read-only media, no scoring capability
  - Scoring actions: require EVALUATOR or TRAINER token + OPA verify
  - Belt award: requires dual confirmation (TRAINER token + SCORING_ENGINE_AGENT)
```

### 11.3 AI Agent Authentication

```
AGENT_AUTH_RULES:
  - No interactive login. Service token only.
  - Token type: OAuth2 Client Credentials flow
  - Token TTL: 1 hour (auto-renewed by agent)
  - Scopes: EXPLICITLY limited (no wildcard scope)
  - Agent cannot access: user PII directly (must go via User Service API)
  - Agent cannot write: audit logs (system infrastructure writes them)
  - Agent cannot promote: any user's role or trust score
  - Agent identity logged on every action as: agent_id + action + target_resource
  - Rogue agent detection: Wazuh alert if agent calls > 10 unique endpoints in 60 seconds
```

### 11.4 Parent Onboarding Verification

```
PARENT_VERIFICATION_FLOW:
  Step 1: Parent submits email + phone
  Step 2: Email OTP verification (valid 10 minutes)
  Step 3: Phone OTP verification (valid 5 minutes)
  Step 4: Child student UUID entered (must be a valid, active student)
  Step 5: Student receives notification: "Parent {name} requested access"
  Step 6: Student approves OR institute admin approves (if student is minor)
  Step 7: Parent account activated with READ_ONLY scope bound to child UUID
  Step 8: Consent record created (immutable, timestamped, stored in audit log)
  Step 9: Parent re-verification required every 12 months
```

---

## SECTION 12 — DEVOPS & DEPLOYMENT RULES (LOCKED)

### 12.1 Keycloak HA Deployment

```
DEPLOYMENT:
  - Keycloak 22+ (Quarkus-based)
  - Kubernetes: 3 replicas minimum (prod)
  - Database backend: PostgreSQL (dedicated schema, connection pool via PgBouncer)
  - Session store: Infinispan (embedded cluster) backed by Redis for cross-pod sync
  - Ingress: Kong (separate from main API gateway, dedicated keycloak subdomain)
  - TLS: cert-manager auto-renewed (Let's Encrypt or internal CA)
  - Health checks: /health/ready + /health/live probes
  - HPA: min 3, max 10 pods (scale on CPU > 60%)
```

### 12.2 OPA Deployment

```
DEPLOYMENT:
  - OPA as sidecar per microservice (or centralized OPA cluster for scale)
  - Policy bundle served from MinIO (signed, versioned)
  - Policy update: zero-downtime bundle push (OPA hot-reload)
  - Decision log: streamed to Kafka → ClickHouse
  - Health: /health endpoint monitored by Prometheus
```

### 12.3 Secrets Injection Pipeline

```
CI/CD_SECRETS_RULE:
  - GitHub Actions / GitLab CI: secrets fetched from Vault at pipeline start via AppRole
  - Secrets NEVER stored in CI environment variables beyond pipeline lifetime
  - Kubernetes pods: Vault Agent Sidecar injects secrets into /vault/secrets/ at pod start
  - Application reads from /vault/secrets/ (file-based) — never from env vars
  - Secret rotation does NOT require redeployment (Vault Agent handles refresh)
```

---

## SECTION 13 — FAILURE HANDLING & INCIDENT RESPONSE (NON-OPTIONAL)

### 13.1 Authentication Failure Cascade

```
LEVEL 1 — Single user auth failure:
  → Log event → increment failure counter → return 401

LEVEL 2 — Repeated user failures (>3):
  → Temporary lock (15 min) → notification to user email → log

LEVEL 3 — Bulk failure pattern (>10 users same IP):
  → IP block → Wazuh alert → ADMIN_PLATFORM notification → log

LEVEL 4 — Keycloak unavailable:
  → Gateway returns 503 → cached session tokens still valid (up to 15 min)
  → No new sessions issued during outage
  → PagerDuty alert → L6 on-call notified

LEVEL 5 — Vault unavailable:
  → Services with cached credentials continue (up to lease TTL)
  → No new credentials issued
  → STOP all new deployments
  → PagerDuty CRITICAL alert
```

### 13.2 Security Incident Response

```
INCIDENT CLASSIFICATION:
  P1 (CRITICAL):   Cross-tenant data access, credential breach, Vault compromise
  P2 (HIGH):       Bulk data exfiltration, admin account compromise, JWT forgery attempt
  P3 (MEDIUM):     Sustained brute-force, anomalous bulk queries, trust score manipulation attempt
  P4 (LOW):        Single auth failure bursts, rate limit violations, minor policy bypass attempts

P1 RESPONSE (automated, < 60 seconds):
  1. Suspend all active sessions in affected tenant
  2. Revoke all refresh tokens in affected scope
  3. Force Keycloak session kill
  4. Alert ADMIN_PLATFORM + ADMIN_COMPLIANCE via PagerDuty
  5. Snapshot audit log (immutable point-in-time export)
  6. Lock affected tenant (no new logins)
  7. Human review required before tenant unlocked
```

---

## SECTION 14 — COMPLIANCE & LEGAL BASELINE (LOCKED)

```
DATA_PROTECTION:
  - PII encrypted at rest (AES-256) via Vault Transit engine
  - PII encrypted in transit (TLS 1.3 minimum, TLS 1.0/1.1 disabled)
  - PII access logged (every field read, not just record access)
  - Right to erasure: user UUID pseudonymized (not deleted) — audit logs retain UUID
  - Data residency: tenant data must not cross configured region boundary

CONSENT:
  - Explicit consent captured at registration (timestamped, stored immutably)
  - Parent consent for minors (< 18 years) captured before any data processing
  - Recording consent for GD / Dojo sessions (displayed pre-session, logged)
  - Consent withdrawal: supported, triggers data export + pseudonymization pipeline

STANDARDS_COMPLIANCE:
  - OWASP ASVS Level 2 (minimum), Level 3 for admin functions
  - ISO 27001 control alignment (access control, audit, cryptography domains)
  - GDPR Article 25 (Privacy by Design) enforced at architecture level
  - India DPDP Act 2023 alignment (for Indian tenant deployments)
```

---

## SECTION 15 — OBSERVABILITY (NON-OPTIONAL)

```
METRICS (Prometheus):
  - auth_login_total (counter, by result label: success|failure|mfa_required)
  - auth_token_refresh_total (counter)
  - auth_session_active_gauge (gauge, by tenant)
  - trust_score_histogram (histogram, buckets: 0.2, 0.4, 0.6, 0.8, 1.0)
  - rbac_decision_total (counter, by result: allow|deny)
  - rate_limit_hit_total (counter, by route)
  - mfa_failure_total (counter, by user_type)

DASHBOARDS (Grafana):
  Panel 1: Login success/failure rate (5-minute rolling)
  Panel 2: Active sessions by tenant
  Panel 3: Trust score distribution
  Panel 4: MFA failure heatmap
  Panel 5: Rate limit violation map (geo IP)
  Panel 6: OPA decision latency (p50, p95, p99)
  Panel 7: Keycloak cluster health
  Panel 8: Vault request rate + error rate

TRACING (OpenTelemetry):
  - Every auth request: trace from Kong → Keycloak → OPA → Service
  - Every step-up: full trace with trust score at each hop
  - Trace retention: 7 days (high cardinality, storage-optimized)

ALERTS (must fire within 60 seconds):
  - Keycloak pod count < 2 → PAGE
  - OPA decision latency p99 > 200ms → WARN
  - Auth failure rate > 20% in 5 minutes → PAGE
  - Trust score < 0.40 for any L5+ user → PAGE
  - Any cross-tenant access attempt → PAGE
  - Vault error rate > 0.1% → PAGE
```

---

## SECTION 16 — AGENT EXECUTION CONTRACT (FINAL LOCK)

### Pre-Execution Checklist (ALL must pass — no exceptions)

```
☐ Keycloak HA cluster deployed and healthy (3+ pods)
☐ OPA deployed and policy bundle loaded (version verified)
☐ Vault HA deployed and unsealed (auto-unseal configured)
☐ Redis session store operational (replication configured)
☐ Kong gateway plugins activated (jwt, rate-limiting, WAF, CORS, ACL)
☐ ModSecurity WAF rules loaded (OWASP CRS 3.3+)
☐ ClickHouse audit tables created (append-only enforced)
☐ Wazuh SIEM ingesting events (pipeline verified)
☐ Prometheus metrics endpoints scraped (all agents reporting)
☐ Grafana dashboards loaded (all 8 panels operational)
☐ Certificate management active (cert-manager operational, TLS 1.3 enforced)
☐ Permission matrix loaded into OPA bundle (hash verified)
☐ Tenant isolation (RLS) enabled on all tenant-scoped tables
☐ Domain isolation policies active in OPA
☐ Parent binding workflow tested end-to-end
☐ GD token issuance and rejection tested
☐ Dojo match token lifecycle tested
☐ AI agent service token scope verified (no wildcard)
☐ Incident response runbook loaded and accessible to on-call
☐ Audit log hash chain verified (tamper-proof confirmed)

CHECKLIST_COMPLETION = REQUIRED BEFORE PRODUCTION TRAFFIC
PARTIAL_CHECKLIST    = STOP EXECUTION
```

### Post-Deployment Verification (Day 1)

```
VERIFY:
  1. Attempt cross-tenant request → must receive 403 + audit entry
  2. Attempt JWT with expired exp → must receive 401
  3. Attempt admin route from non-VPN IP → must receive 403
  4. Trigger MFA failure 5 times → account must lock
  5. Device mismatch on refresh → must revoke session + alert
  6. Trust score < 0.40 simulation → session must suspend
  7. Step-up trigger on billing route → must require TOTP
  8. Parent write attempt → must receive 403
  9. Agent wildcard scope request → must be denied by OPA
  10. Bulk query > rate limit → must receive 429

ALL 10 MUST PASS.
ANY FAILURE → ROLLBACK + REPORT + NO PRODUCTION CLAIM
```

---

## APPENDIX — TECHNOLOGY BINDING SUMMARY

| Component | Technology | Version | Mode |
|-----------|-----------|---------|------|
| Identity Provider | Keycloak | 22+ (Quarkus) | Self-hosted HA |
| Auth Protocol | OAuth 2.1 + OIDC 1.0 | Standard | Enforced |
| Token Format | JWT RS256 | RFC 7519 | Asymmetric |
| Policy Engine | Open Policy Agent | Latest stable | Sidecar + Central |
| Secret Store | HashiCorp Vault | 1.15+ | HA Auto-unseal |
| Session Store | Redis | 7+ | Cluster mode |
| API Gateway | Kong OSS | Latest | Self-hosted |
| WAF | ModSecurity | CRS 3.3+ | OWASP enforced |
| SIEM | Wazuh | Latest | Self-hosted |
| Audit Store | ClickHouse | 23+ | Append-only |
| Metrics | Prometheus | Latest | Scrape model |
| Dashboards | Grafana | Latest | ClickHouse + Prom |
| Tracing | OpenTelemetry | Latest | OTLP export |
| mTLS | SPIFFE/SPIRE or Istio | Latest | Service mesh |
| Cert Management | cert-manager | Latest | Auto-renew |

---

```
══════════════════════════════════════════════════════════════════
  DOCUMENT STATUS:    SEALED · LOCKED · FINAL
  AGENT VERSION:      1.0.0
  PLATFORM:           ECOSKILLER — ANTIGRAVITY LAYER
  MUTATION AUTHORITY: Human declaration + version bump ONLY
  LAST SEALED:        2026-03-04
  HASH (CONCEPTUAL):  [To be generated on first CI bundle push]
══════════════════════════════════════════════════════════════════
```
