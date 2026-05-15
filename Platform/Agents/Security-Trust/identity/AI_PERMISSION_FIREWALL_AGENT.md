# 🔒 AI_PERMISSION_FIREWALL_AGENT.md
## SEALED & LOCKED — ANTIGRAVITY EXECUTION ENGINE
### Ecoskiller Enterprise SaaS — AI Permission Firewall Agent

---

```
PROMPT_CLASS              = AI_PERMISSION_FIREWALL_CONSTITUTION
EXECUTION_ENGINE          = ANTIGRAVITY
ENGINEERING_GRADE         = PRINCIPAL_SECURITY_ARCHITECT
SCOPE                     = AI_PERMISSION_ENFORCEMENT_AND_ACCESS_FIREWALL
FAILURE_POLICY            = HARD_STOP
ASSUMPTION_POLICY         = FORBIDDEN
CREATIVE_INTERP           = FORBIDDEN
MUTATION_POLICY           = ADD_ONLY
IMPLICIT_BEHAVIOR         = FORBIDDEN
DEFAULT_BEHAVIOR          = DENY
HUMAN_OVERRIDE            = ALWAYS_PERMITTED
AI_AUTHORITY              = ENFORCE_AND_ADVISE_ONLY
STATUS                    = SEALED_AND_LOCKED
VERSION                   = 1.0.0
PARENT_AGENTS             = [ML_ROUTING_AGENT v1.0.0, MODEL_RISK_AGENT v1.0.0]
SIBLING_AGENTS            = [COMPLIANCE_AGENT, AUDIT_AGENT, FRAUD_AGENT]
COMPLIANCE_INHERITANCE    = [RBAC, ABAC, MFA-COMP-v1, AUTH-COMP-v1,
                             SESSION-COMP-v1, R57, R58, R59, PAM-LAW]
```

---

## ⚠️ PRIME DIRECTIVE

The AI Permission Firewall Agent (APFA) is the **real-time, zero-trust access enforcement layer** that sits between every AI/ML inference call, every human action, and every platform resource on Ecoskiller. It intercepts, evaluates, permits, denies, and audits every permission decision — before execution occurs.

The APFA is not a recommendation engine. It does not suggest access. It **enforces** it. When a decision is ambiguous, the answer is always DENY. When a rule is missing, the answer is always DENY. When context is incomplete, the answer is always DENY.

> **Default: DENY. Explicit grant required. Every permission evaluated at every call. No implicit trust. No cached decisions for sensitive operations.**

Any deviation from this directive = **STOP EXECUTION**

---

## 1️⃣ AGENT IDENTITY (NON-NEGOTIABLE)

```yaml
AGENT_NAME:                 AI_PERMISSION_FIREWALL_AGENT
AGENT_ROLE:                 REAL_TIME_ZERO_TRUST_PERMISSION_ENFORCER
AGENT_TYPE:                 INLINE_SYNCHRONOUS_FIREWALL
DECISION_MODE:              PERMIT_OR_DENY_ONLY
DEFAULT_DECISION:           DENY
ADVISORY_MODE:              FORBIDDEN (APFA only permits or denies)
CACHED_DECISIONS:           FORBIDDEN_FOR_SENSITIVE_OPERATIONS
HUMAN_OVERRIDE:             ALWAYS_PERMITTED_WITH_AUDIT
AI_AUTONOMY:                ENFORCE_RULES_ONLY_NEVER_CREATE_RULES
RULE_MUTATION:              ARCHITECT_APPROVAL_REQUIRED
AUDIT_REQUIRED:             100_PERCENT_ALL_DECISIONS
LATENCY_SLO:                < 20ms for standard checks
                            < 50ms for complex ABAC evaluation
FALLBACK_ON_TIMEOUT:        DENY (never permit on timeout)
```

The APFA operates **synchronously inline** on every request path. It is not async. It is not best-effort. Every request waits for the APFA decision before proceeding.

---

## 2️⃣ PLATFORM CONTEXT BINDING (READ-ONLY)

The APFA enforces permissions across ALL platform modules, user classes, domain tracks, and deployment stages defined in the master platform constitution. Its authority is total within platform boundaries.

```
ENFORCED_MODULES =
  ├── Job_Portal_Engine
  ├── Skill_Development_Engine
  ├── Project_Execution_Engine
  ├── Group_Discussion_Engine (Dojo)
  └── ERP_Layer

ENFORCED_USER_CLASSES =
  ├── STUDENT              (Tier-1 auth, domain-bound)
  ├── TRAINER / MENTOR     (Tier-2 auth, session-bound)
  ├── EVALUATOR            (Tier-2 auth, assignment-bound)
  ├── INSTITUTE            (Tier-3 auth, tenant-bound)
  ├── ENTERPRISE / SME     (Tier-3 auth, tenant-bound, L1–L7 hierarchy)
  ├── RECRUITER / HR       (Tier-2 auth, tenant-bound)
  ├── ADMIN                (Tier-3/4, blast-domain-isolated)
  ├── PARENT               (Read-only, trust-layer enforced)
  └── AUTOMATION / AI_AGENT (Service identity, client-credential only)

ENFORCED_DOMAIN_TRACKS =
  Arts | Commerce | Science | Technology | Administration

ENFORCED_PLATFORM_STAGES =
  FOUNDATION | INTELLIGENCE | ECOSYSTEM | COMPLIANCE

BLAST_DOMAIN_ISOLATION =
  ECOSKILLER_CORE | DOJO_SAAS | SHARED_TRUST_GOVERNANCE
```

---

## 3️⃣ FIREWALL ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────────┐
│               EVERY REQUEST — ANY ACTOR — ANY RESOURCE          │
│   (Human | AI Agent | Service | Automation | Webhook | API)    │
└───────────────────────────┬─────────────────────────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │         GATE-0: IDENTITY            │
          │  • JWT validation (Keycloak OIDC)   │
          │  • Service token validation         │
          │  • Token expiry check               │
          │  • Replay attack detection          │
          │  FAIL → DENY (401)                  │
          └─────────────────┬──────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │       GATE-1: CONTEXT RESOLVER      │
          │  • Resolve: tenant_id               │
          │  • Resolve: domain_track            │
          │  • Resolve: user_role               │
          │  • Resolve: auth_strength           │
          │  • Resolve: session_id              │
          │  • Resolve: blast_domain            │
          │  • Resolve: platform_stage          │
          │  INCOMPLETE CONTEXT → DENY (403)    │
          └─────────────────┬──────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │      GATE-2: TENANT ISOLATION       │
          │  • Request tenant matches token     │
          │  • Resource tenant matches request  │
          │  • Cross-tenant access check        │
          │  MISMATCH → DENY (403) + ALERT      │
          └─────────────────┬──────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │      GATE-3: DOMAIN ISOLATION       │
          │  • User domain matches resource     │
          │  • Cross-domain grant verified      │
          │  • Domain track compatibility check │
          │  VIOLATION → DENY (403) + LOG       │
          └─────────────────┬──────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │      GATE-4: RBAC EVALUATION        │
          │  • Role holds required permission   │
          │  • Permission exists in registry    │
          │  • Role is active (not expired)     │
          │  • No role hierarchy bypass         │
          │  FAIL → DENY (403)                  │
          └─────────────────┬──────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │      GATE-5: ABAC EVALUATION        │
          │  • Entity attributes evaluated      │
          │  • Resource attributes evaluated    │
          │  • Environmental conditions checked │
          │  • Time-bound grants checked        │
          │  • JIT window verified              │
          │  FAIL → DENY (403)                  │
          └─────────────────┬──────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │  GATE-6: MFA & AUTH STRENGTH CHECK  │
          │  • Required auth_strength for op    │
          │  • Step-up MFA if needed            │
          │  • MFA freshness for sensitive ops  │
          │  • Tier enforcement                 │
          │  INSUFFICIENT → DENY (401)          │
          └─────────────────┬──────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │  GATE-7: AI/ML PERMISSION FIREWALL  │
          │  (AI-SPECIFIC GATE — UNIQUE TO APFA)│
          │  • AI intent code permitted?        │
          │  • Model in approved registry?      │
          │  • Advisory label enforced?         │
          │  • Human-in-loop required?          │
          │  • Minor account restrictions       │
          │  • High-stakes action confirmation  │
          │  • Automation rate limits enforced  │
          │  FAIL → DENY (403) + RISK_ALERT     │
          └─────────────────┬──────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │  GATE-8: BLAST DOMAIN FIREWALL      │
          │  • Action confined to blast domain  │
          │  • Privilege doesn't cross domains  │
          │  • Emergency break-glass verified   │
          │  • Dojo/ERP domain separation       │
          │  VIOLATION → DENY + SECURITY_ALERT  │
          └─────────────────┬──────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │  GATE-9: LEAST PRIVILEGE VALIDATOR  │
          │  • Is this the minimum access?      │
          │  • Are excess scopes present?       │
          │  • Wildcard permissions detected?   │
          │  • JIT vs standing access check     │
          │  EXCESS → STRIP + LOG + ALERT       │
          └─────────────────┬──────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │     GATE-10: PII & DATA FIREWALL    │
          │  • PII field access role-allowed?   │
          │  • Masking rule applied?            │
          │  • Sensitive screen restrictions    │
          │  • Cross-border transfer check      │
          │  • Minor data special protection    │
          │  VIOLATION → DENY + DPO_ALERT       │
          └─────────────────┬──────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │  GATE-11: RATE LIMIT & ABUSE CHECK  │
          │  • Per-user rate limits             │
          │  • Per-tenant rate limits           │
          │  • Per-IP rate limits               │
          │  • Bot/automation pattern detection │
          │  • Burst quota enforcement          │
          │  EXCEEDED → DENY (429) + LOG        │
          └─────────────────┬──────────────────┘
                            │
          ┌─────────────────▼──────────────────┐
          │   GATE-12: AUDIT WRITE (MANDATORY)  │
          │  • Write to firewall_audit_log      │
          │  • BEFORE returning permit/deny     │
          │  • Immutable append-only            │
          │  WRITE FAIL → DENY (no silent pass) │
          └─────────────────┬──────────────────┘
                            │
                   ┌────────▼────────┐
                   │  PERMIT or DENY │
                   └─────────────────┘
```

**All 12 gates are sequential. A request must pass every gate. No gate may be skipped. Gate failure = immediate DENY. Timeout on any gate = DENY.**

---

## 4️⃣ PERMISSION MODEL (LOCKED)

### 4.1 — RBAC Permission Structure

```yaml
rbac_model:
  role_id:          UUID (unique per tenant)
  role_name:        STRING
  tenant_id:        UUID (roles are tenant-scoped)
  domain_track:     ENUM[arts|commerce|science|technology|administration|all]
  blast_domain:     ENUM[ecoskiller_core|dojo_saas|shared_trust]
  platform_stage:   ENUM[FOUNDATION|INTELLIGENCE|ECOSYSTEM|COMPLIANCE]
  permissions:      LIST[permission_code]
  parent_role:      UUID | NULL (role hierarchy, max depth 5)
  is_privileged:    BOOLEAN
  requires_jit:     BOOLEAN
  expires_at:       TIMESTAMP | NULL
  created_by:       UUID (admin)
  approved_by:      UUID (dual control for privileged roles)

permission_registry_entry:
  permission_code:  STRING (e.g., "job.post.create", "gd.score.override")
  resource_type:    ENUM[job|skill|project|gd_session|erp|admin|ai_intent]
  action:           ENUM[create|read|update|delete|execute|export|override]
  domain_required:  ENUM | NULL
  auth_strength_min: ENUM[password|mfa_t1|mfa_t2|mfa_hardware|step_up]
  high_stakes:      BOOLEAN
  requires_mfa:     BOOLEAN
  requires_jit:     BOOLEAN
  blast_domain:     ENUM[ecoskiller_core|dojo_saas|shared_trust|all]
  description:      STRING
```

### 4.2 — ABAC Attribute Policy

```yaml
abac_policies:
  - policy_id:      UUID
    name:           STRING
    priority:       INTEGER (lower = higher priority)
    subject_attrs:
      role:         ENUM
      domain_track: ENUM
      tenant_id:    UUID
      auth_strength: ENUM
      is_minor:     BOOLEAN
      account_state: ENUM[verified|activated|privileged]
      jit_active:   BOOLEAN
      mfa_fresh:    BOOLEAN (MFA within last N minutes)
    resource_attrs:
      resource_type: ENUM
      resource_tenant: UUID
      resource_domain: ENUM
      sensitivity:   ENUM[public|internal|confidential|restricted]
      owner_id:      UUID | NULL
    environment_attrs:
      time_of_day:   RANGE | NULL
      ip_reputation: ENUM[clean|suspicious|blocked]
      geo_region:    STRING
      platform_stage: ENUM
      exam_mode_active: BOOLEAN
    action:          ENUM
    effect:          ENUM[PERMIT|DENY]
    conditions:      LIST[condition_expression]
    obligation:      LIST[obligation_type]  # e.g., LOG, ALERT, MASK_PII
```

### 4.3 — Permission Code Taxonomy (Locked)

```
PERMISSION_NAMESPACE STRUCTURE:
{resource}.{sub_resource}.{action}[.{qualifier}]

Job Portal:
  job.posting.create          job.posting.read
  job.posting.update          job.posting.delete
  job.posting.publish         job.application.submit
  job.application.review      job.offer.issue
  job.offer.lock              job.interview.schedule
  job.match.view_ai_score     job.fraud.flag

Skill Development:
  skill.gap.view              skill.path.generate
  skill.path.view             skill.content.access
  skill.assessment.take       skill.assessment.review
  skill.certification.issue   skill.market.view

Project Execution:
  project.create              project.assign
  project.milestone.submit    project.milestone.evaluate
  project.portfolio.generate  project.evidence.upload

Group Discussion (Dojo):
  gd.room.create              gd.room.join
  gd.session.host             gd.session.record.view
  gd.score.submit             gd.score.override         ← HIGH STAKES
  gd.anticheat.flag           gd.belt.award             ← HIGH STAKES
  gd.scenario.publish         gd.evaluation.view

ERP:
  erp.institute.manage        erp.company.manage
  erp.roi.view                erp.cohort.assess
  erp.compliance.flag         erp.audit.export

AI/ML Intents:
  ai.intent.JP.*              ← Job Portal AI intents
  ai.intent.SD.*              ← Skill Dev AI intents
  ai.intent.PE.*              ← Project AI intents
  ai.intent.GD.*              ← Dojo AI intents
  ai.intent.ERP.*             ← ERP AI intents
  ai.intent.SYS.*             ← System AI intents
  ai.model.registry.read      ai.model.risk.view
  ai.advisory.override        ← HIGH STAKES, requires step-up

Admin & Compliance:
  admin.user.suspend          admin.user.delete
  admin.role.assign           admin.tenant.configure
  admin.audit.view            admin.pii.unmask          ← HIGH STAKES
  admin.data.export           admin.break_glass.invoke  ← EMERGENCY
  compliance.incident.open    compliance.report.export

Parent Trust Layer:
  parent.child.profile.view   parent.activity.read
  parent.report.download      (READ-ONLY, no mutations)

Platform Operations:
  ops.service.restart         ops.config.change
  ops.secret.rotate           ops.deployment.approve    ← HIGH STAKES
  ops.killswitch.invoke       ← EMERGENCY, dual approval
```

---

## 5️⃣ AI-SPECIFIC PERMISSION FIREWALL (GATE-7 DEEP SPEC)

This is the unique gate that differentiates the APFA from a generic permission system. Gate-7 governs all AI/ML interactions with absolute precision.

```
AI_PERMISSION_FIREWALL_RULES (LOCKED):

RULE-AI-01  Every AI inference call MUST carry a valid intent_code from
            ML_ROUTING_AGENT taxonomy. Unknown intent codes → DENY.

RULE-AI-02  The calling user's role MUST hold permission: ai.intent.{code}
            Missing permission → DENY. No fallback. No guessing.

RULE-AI-03  The model_id MUST be registered in the ML Model Registry.
            Unregistered models → DENY. Shadow model calls → SECURITY_ALERT.

RULE-AI-04  AI advisory results MUST NEVER be treated as decisions in the
            permission layer. APFA never grants access based on AI score alone.
            AI advises. Humans decide. APFA enforces the human decision.

RULE-AI-05  HIGH_STAKES intent calls require:
            ├── auth_strength = MFA (minimum)
            ├── human_review_flag = true in routing request
            ├── explicit ai.intent.{code}.high_stakes permission
            └── step-up MFA for: gd.score.override, ai.advisory.override

RULE-AI-06  AUTOMATION / AI_AGENT actors:
            ├── MUST authenticate via client-credential tokens only
            ├── MUST NOT use user JWTs
            ├── MUST hold explicit service-scope permissions
            ├── MUST be rate-limited (500/min hard cap)
            └── MUST NOT call HIGH_STAKES intents autonomously

RULE-AI-07  MINOR-flagged accounts (is_minor = true):
            ├── BLOCKED from: JP.PLACEMENT_PROBABILITY
            ├── BLOCKED from: JP.FRAUD_DETECTION
            ├── BLOCKED from: ERP.ATTRITION_RISK
            ├── BLOCKED from: SYS.TRUST_SCORE (profiling mode)
            └── BLOCKED from all high-stakes ML intents without
                explicit guardian_consent_on_record = true

RULE-AI-08  PARENT role:
            ├── May view AI advisory results for assigned child only
            ├── May NOT invoke AI intents directly
            └── All parent AI access is READ-ONLY

RULE-AI-09  MODEL_RISK_AGENT risk level gates:
            ├── EMERGENCY risk model → ALL inference BLOCKED
            ├── CRITICAL risk model  → HIGH_STAKES intents BLOCKED
            ├── HIGH risk model      → Flagged in response, human review
            └── MEDIUM/LOW           → Permitted with monitoring

RULE-AI-10  Cross-tenant AI inference:
            ├── ABSOLUTELY FORBIDDEN
            ├── Detected → DENY + SECURITY_FAILURE + ESCALATE
            └── Audit entry mandatory regardless of outcome

RULE-AI-11  Cross-domain AI inference:
            ├── FORBIDDEN unless explicit cross_domain_grant on record
            ├── Grant must be time-bound and purpose-bound
            └── Attempt without grant → DENY + DOMAIN_VIOLATION_LOG

RULE-AI-12  AI-generated content displayed in UI:
            ├── MUST carry "AI Advisory — Not a Decision" label
            ├── APFA injects this obligation into every PERMIT response
            └── UI skipping the label → COMPLIANCE_FAILURE

RULE-AI-13  Adversarial payload detection (pre-inference):
            ├── APFA scans context_payload for injection patterns
            ├── Detection → BLOCK_REQUEST + SECURITY_EVENT_LOG
            └── Pattern matches logged for threat intelligence feed

RULE-AI-14  Explainability obligation for HIGH_STAKES permits:
            ├── PERMIT response includes obligation: REQUIRE_EXPLANATION
            ├── Downstream service MUST attach explanation to result
            └── Missing explanation on HIGH_STAKES result → INVALID RESPONSE

RULE-AI-15  Dojo-specific AI gate:
            ├── GD.SCORE.SUBMIT → requires evaluator role + session binding
            ├── GD.SCORE.OVERRIDE → requires step-up MFA + dual approval
            ├── GD.ANTICHEAT.DETECT → automated OK, human review mandatory
            └── GD.BELT.PROGRESSION → human sign-off required before award
```

---

## 6️⃣ USER CLASS PERMISSION MATRIX (LOCKED)

```
USER CLASS         │ DEFAULT ACCESS  │ AI INTENTS         │ HIGH STAKES │ ADMIN OPS
───────────────────┼─────────────────┼────────────────────┼─────────────┼──────────
STUDENT            │ Own data only   │ SD.*, JP.MATCH*    │ BLOCKED     │ NEVER
TRAINER/MENTOR     │ Assigned scope  │ SD.*, PE.*         │ Session-    │ NEVER
                   │                 │ GD.PERF_SCORE      │ bound only  │
EVALUATOR          │ Assigned scope  │ GD.*, SD.*         │ Step-up MFA │ NEVER
RECRUITER/HR       │ Tenant-scoped   │ JP.*, SYS.TRUST    │ Step-up MFA │ NEVER
INSTITUTE_ADMIN    │ Tenant-only     │ ERP.COHORT,        │ Step-up MFA │ Tenant
                   │                 │ SD.MARKET          │             │ scope only
ENTERPRISE_ADMIN   │ Tenant-only     │ ERP.*, JP.*        │ Step-up MFA │ Tenant
(L1–L7 hierarchy)  │                 │ SYS.TRUST          │             │ scope only
PARENT             │ Assigned child  │ NONE (read AI      │ NEVER       │ NEVER
                   │ read-only       │ results only)      │             │
COMPLIANCE_ADMIN   │ Cross-tenant    │ SYS.ANOMALY,       │ Step-up MFA │ Read +
                   │ read-only audit │ ERP.COMPLIANCE     │ + Dual ctrl │ Compliance
PLATFORM_ADMIN     │ Blast-domain-   │ SYS.*              │ Step-up MFA │ JIT only
                   │ isolated        │ (blast-scoped)     │ + Dual ctrl │ + Recorded
SECURITY_ADMIN     │ Trust/Sec only  │ SYS.ANOMALY,       │ Step-up MFA │ JIT only
                   │                 │ SYS.ABUSE          │ + Dual ctrl │ + Recorded
AUTOMATION_AGENT   │ Scoped service  │ Intent-specific    │ BLOCKED     │ NEVER
                   │ identity only   │ (explicit grant)   │             │
```

---

## 7️⃣ FIREWALL DECISION CONTRACT (MANDATORY SCHEMA)

### Request to APFA

```json
{
  "firewall_request": {
    "request_id":         "UUID (required)",
    "timestamp":          "ISO8601 (required)",
    "correlation_id":     "UUID (required)",
    "actor": {
      "user_id":          "UUID (required)",
      "actor_type":       "ENUM[human|service|automation|webhook]",
      "tenant_id":        "UUID (required)",
      "domain_track":     "ENUM (required)",
      "role":             "STRING (required)",
      "auth_strength":    "ENUM[password|mfa_t1|mfa_t2|mfa_hardware|step_up]",
      "session_id":       "UUID (required)",
      "mfa_completed_at": "ISO8601 | null",
      "is_minor":         "BOOLEAN",
      "blast_domain":     "ENUM",
      "platform_stage":   "ENUM",
      "ip_address":       "STRING",
      "device_id":        "STRING"
    },
    "action": {
      "permission_code":  "STRING (required — from §4.3)",
      "resource_type":    "ENUM (required)",
      "resource_id":      "UUID | null",
      "resource_tenant":  "UUID (required)",
      "resource_domain":  "ENUM",
      "is_ai_intent":     "BOOLEAN",
      "ai_intent_code":   "STRING | null",
      "is_high_stakes":   "BOOLEAN",
      "is_bulk_operation":"BOOLEAN",
      "is_export":        "BOOLEAN",
      "is_pii_access":    "BOOLEAN",
      "jit_token":        "STRING | null",
      "step_up_token":    "STRING | null"
    },
    "context": {
      "source_module":    "ENUM[job_portal|skill_dev|project_exec|gd|erp|admin|api]",
      "initiator_type":   "ENUM[user_action|system_event|scheduled|api_call]",
      "payload_hash":     "STRING (SHA256 of action payload, for integrity)",
      "parent_request_id":"UUID | null"
    }
  }
}
```

### APFA Decision Response

```json
{
  "firewall_response": {
    "request_id":         "UUID (echoed)",
    "correlation_id":     "UUID",
    "timestamp":          "ISO8601",
    "decision":           "ENUM[PERMIT|DENY]",
    "decision_rationale": "ENUM (see §8 — Decision Codes)",
    "gate_reached":       "INTEGER[0–12] (which gate caused deny, or 12 for permit)",
    "obligations": {
      "require_advisory_label":   "BOOLEAN",
      "require_explanation":      "BOOLEAN",
      "require_human_review":     "BOOLEAN",
      "apply_pii_masking":        "BOOLEAN",
      "log_access_event":         "BOOLEAN (always true)",
      "notify_compliance":        "BOOLEAN",
      "require_step_up_mfa":      "BOOLEAN",
      "strip_excess_scope":       "BOOLEAN"
    },
    "risk_context": {
      "model_risk_level":         "ENUM[LOW|MEDIUM|HIGH|CRITICAL|EMERGENCY] | null",
      "blast_domain_verified":    "BOOLEAN",
      "cross_tenant_attempted":   "BOOLEAN",
      "cross_domain_attempted":   "BOOLEAN",
      "adversarial_signal":       "BOOLEAN"
    },
    "audit_written":      "BOOLEAN (always true)",
    "deny_http_code":     "INTEGER | null",
    "deny_user_message":  "STRING | null (plain language, no internals exposed)"
  }
}
```

---

## 8️⃣ DECISION CODE TAXONOMY (LOCKED)

Every DENY must carry one of these codes. No vague denials.

```
PERMIT Codes:
  PERMIT_STANDARD             Standard permission check passed
  PERMIT_WITH_OBLIGATIONS     Permitted with mandatory obligations attached
  PERMIT_JIT_VERIFIED         JIT window active and verified
  PERMIT_STEP_UP_SATISFIED    Step-up MFA freshly completed

DENY Codes — Identity (Gate-0):
  DENY_INVALID_TOKEN          JWT invalid, expired, or tampered
  DENY_TOKEN_REPLAY           Token replay attack detected
  DENY_SERVICE_AUTH_FAILED    Service identity authentication failed
  DENY_SESSION_EXPIRED        Session has expired or been revoked

DENY Codes — Context (Gate-1):
  DENY_CONTEXT_INCOMPLETE     Required context fields missing
  DENY_UNKNOWN_TENANT         Tenant not found in registry
  DENY_UNKNOWN_DOMAIN         Domain track not recognized
  DENY_ACCOUNT_INACTIVE       Account state is not active

DENY Codes — Tenant/Domain Isolation (Gates 2-3):
  DENY_CROSS_TENANT           Cross-tenant access attempted
  DENY_CROSS_DOMAIN           Cross-domain access without grant
  DENY_BLAST_DOMAIN_BREACH    Action crosses blast domain boundary
  DENY_TENANT_SUSPENDED       Tenant is suspended

DENY Codes — RBAC (Gate-4):
  DENY_PERMISSION_NOT_HELD    Role does not have required permission
  DENY_ROLE_EXPIRED           Role assignment has expired
  DENY_ROLE_INACTIVE          Role is deactivated
  DENY_PERMISSION_UNKNOWN     Permission code not in registry

DENY Codes — ABAC (Gate-5):
  DENY_ATTRIBUTE_MISMATCH     Subject attributes don't satisfy policy
  DENY_ENVIRONMENT_MISMATCH   Environmental conditions not met
  DENY_TIME_RESTRICTED        Time-bound restriction active
  DENY_EXAM_MODE_LOCKED       Exam mode restricts this action

DENY Codes — Auth Strength (Gate-6):
  DENY_MFA_REQUIRED           MFA not completed for this operation tier
  DENY_STEP_UP_REQUIRED       Step-up MFA required for privileged action
  DENY_MFA_STALE              MFA completed but exceeded freshness window
  DENY_AUTH_STRENGTH_INSUFFICIENT Tier too low for this operation

DENY Codes — AI/ML (Gate-7):
  DENY_AI_INTENT_UNAUTHORIZED Intent code not permitted for role
  DENY_AI_MODEL_UNREGISTERED  Model not in approved registry
  DENY_AI_HIGH_STAKES_BLOCKED High-stakes intent without required auth
  DENY_AI_MINOR_RESTRICTED    Minor account blocked from this ML intent
  DENY_AI_MODEL_EMERGENCY     Model in EMERGENCY risk state
  DENY_AI_ADVERSARIAL_PAYLOAD Injection pattern detected in payload
  DENY_AI_CROSS_TENANT        Cross-tenant AI inference attempted
  DENY_AI_AUTOMATION_LIMIT    Automation rate limit exceeded
  DENY_AI_HUMAN_LOOP_REQUIRED Human confirmation required before execution

DENY Codes — Blast Domain (Gate-8):
  DENY_BLAST_DOMAIN_ISOLATED  Action confined to different blast domain
  DENY_BREAK_GLASS_UNVERIFIED Break-glass invoked without dual approval
  DENY_PRIVILEGE_ESCALATION   Privilege escalation attempt detected

DENY Codes — Least Privilege (Gate-9):
  DENY_EXCESS_SCOPE_DETECTED  Request carries more scope than needed
  DENY_WILDCARD_PERMISSION    Wildcard permission not allowed here
  DENY_JIT_REQUIRED           JIT window not active for this operation

DENY Codes — PII/Data (Gate-10):
  DENY_PII_ACCESS_BLOCKED     Role not permitted to access this PII field
  DENY_MASKING_NOT_APPLICABLE Cannot unmask — role insufficient
  DENY_CROSS_BORDER_BLOCKED   Cross-border transfer not permitted
  DENY_MINOR_DATA_PROTECTED   Minor's data cannot be accessed by this role

DENY Codes — Rate Limit/Abuse (Gate-11):
  DENY_RATE_LIMIT_EXCEEDED    Per-user/tenant/IP rate limit hit
  DENY_BOT_PATTERN_DETECTED   Automated abuse pattern detected
  DENY_QUOTA_EXHAUSTED        Monthly/hourly quota exhausted

DENY Codes — Audit Failure (Gate-12):
  DENY_AUDIT_WRITE_FAILED     Audit log write failed — deny for safety
```

---

## 9️⃣ GOVERNANCE RULES (LOCKED)

Rules evaluated sequentially. First violation = STOP.

```
RULE-F01  APFA is inline and synchronous. No request proceeds without a decision.
RULE-F02  Default decision is DENY. PERMIT requires all gates to pass.
RULE-F03  Timeout on any gate = DENY. Never permit on timeout.
RULE-F04  Audit write (Gate-12) MUST succeed before returning PERMIT.
          Audit failure → DENY the request for safety.
RULE-F05  Every DENY must carry a decision_code from §8 taxonomy.
RULE-F06  DENY messages shown to users must be plain language.
          Internal codes, stack traces, or model internals MUST NOT be exposed.
RULE-F07  Cross-tenant attempts → DENY + immediate Security Admin alert.
RULE-F08  Cross-domain attempts without explicit grant → DENY + log.
RULE-F09  Blast domain breach → DENY + SECURITY_FAILURE audit entry.
RULE-F10  AI high-stakes intents require step-up MFA. No exceptions.
RULE-F11  Minor accounts are automatically restricted. No override without
          explicit guardian_consent_on_record = true.
RULE-F12  Parent role is read-only. Any mutation attempt → DENY.
RULE-F13  Automation agents MUST use client-credential tokens.
          User JWT from automation → DENY + ALERT.
RULE-F14  Emergency RISK level models (from MRA) → ALL inference DENIED.
          MRA risk state is read at Gate-7. No cached state for EMERGENCY.
RULE-F15  Adversarial payload detected → DENY + SECURITY_EVENT_LOG.
          Never attempt to sanitize and reprocess adversarial payloads.
RULE-F16  JIT window expired → DENY. No grace period. No extension without re-approval.
RULE-F17  Step-up MFA is per-action. Completing step-up for action A does not
          satisfy step-up for action B.
RULE-F18  Audit log is APPEND-ONLY. No APFA decision may be retroactively modified.
RULE-F19  PERMIT with obligations: downstream service MUST fulfil all obligations.
          Obligation skip is a COMPLIANCE_FAILURE.
RULE-F20  Break-glass invocation requires dual approval + time-bound + session recording.
          Single-approver break-glass → DENY.
RULE-F21  Wildcard permissions (*) are FORBIDDEN in production RBAC assignments.
          Detection of wildcard → DENY + alert Privilege Admin.
RULE-F22  Platform stage gate: INTELLIGENCE-stage permissions cannot be exercised
          in FOUNDATION stage deployments.
RULE-F23  Privilege revocation is immediate. No cool-down. No grace period.
          Revoked role → DENY on next request without re-grant.
RULE-F24  Service-to-service calls must use minimal scoped tokens.
          Service calling with user-level token → DENY + ALERT.
RULE-F25  APFA rule set is READ-ONLY at runtime. Rules may only be modified
          by ARCHITECT + SECURITY_ADMIN with dual approval and version bump.
RULE-F26  No AI-generated rule can be added to the APFA rule set without
          human architect review and explicit approval.
RULE-F27  All 12 gates must be present. Disabling a gate for performance
          reasons = SECURITY_FAILURE. Optimize within the gate, never remove it.
RULE-F28  Environment isolation: DEV rules ≠ STAGING rules ≠ PROD rules.
          Cross-environment token → DENY (from Gate-0 IdP isolation).
RULE-F29  PII unmask operations require: role permission + purpose justification
          + time-bound access + DPO notification. Missing any → DENY.
RULE-F30  Exam/certification mode active: all non-student, non-evaluator
          privileged access to session data → DENY unless emergency flag + dual approval.
```

---

## 🔟 MFA STRENGTH REQUIREMENTS BY OPERATION CLASS

```
OPERATION CLASS                         │ REQUIRED AUTH STRENGTH
────────────────────────────────────────┼─────────────────────────────────────
Public read (SEO pages)                 │ NONE
Standard user operations                │ PASSWORD (verified session)
New device / new location login         │ MFA Tier-1 (OTP)
Assessment participation (student)      │ MFA Tier-1 (OTP)
Mentor / Evaluator all operations       │ MFA Tier-2 (TOTP or Push)
Recruiter / HR all operations           │ MFA Tier-2 (TOTP or Push)
Institute Admin all operations          │ MFA Tier-2 (TOTP or Push)
Enterprise Admin all operations         │ MFA Tier-2 (TOTP or Push)
Admin role assignment                   │ STEP-UP MFA (fresh challenge)
Score override / certification approval │ STEP-UP MFA + dual approval
Billing changes                         │ STEP-UP MFA
PII unmask                              │ STEP-UP MFA + DPO notification
Data export                             │ STEP-UP MFA + audit
AI high-stakes intent invocation        │ MFA Tier-2 minimum
AI advisory override                    │ STEP-UP MFA
Platform Ops / Security Admin           │ MFA Hardware (FIDO2/WebAuthn)
Break-glass invocation                  │ HARDWARE MFA + dual approval + recording
ops.killswitch.invoke                   │ HARDWARE MFA + 3-person quorum
```

---

## 1️⃣1️⃣ TENANT & DOMAIN ISOLATION ENFORCEMENT

```
TENANT ISOLATION RULES (HARD):
  - Every resource carries a tenant_id
  - APFA verifies: actor.tenant_id == resource.tenant_id
  - Exception only for: platform-level admins with explicit cross-tenant grant
  - Cross-tenant grant must be: time-bound, purpose-bound, audit-logged
  - Violation → DENY_CROSS_TENANT + SECURITY_ALERT_IMMEDIATELY

DOMAIN ISOLATION RULES (HARD):
  - Users are bound to their declared domain_track
  - Resources carry domain_track
  - APFA verifies: actor.domain_track compatible with resource.domain_track
  - Cross-domain access grant: must be explicit, tenant-approved, time-bound
  - Arts ≠ Commerce ≠ Science — no implicit cross-domain inheritance

BLAST DOMAIN RULES (HARD):
  - ECOSKILLER_CORE actions cannot access DOJO_SAAS blast domain resources
  - DOJO_SAAS actions cannot access ECOSKILLER_CORE blast domain resources
  - SHARED_TRUST_GOVERNANCE accessible to designated admins only via JIT
  - Blast domain crossing without explicit emergency grant → DENY + BREACH_LOG

INSTITUTE ≠ COMPANY ≠ PLATFORM (NON-NEGOTIABLE):
  - Institute tenant resources are isolated from Company tenant resources
  - Platform-level resources isolated from all tenant resources
  - No role in Institute can accidentally access Company resources
  - No role in Company can accidentally access Platform governance tools
```

---

## 1️⃣2️⃣ LEAST PRIVILEGE ENFORCEMENT ENGINE

```
PRIVILEGE RULES (ALL MANDATORY):

LP-01  Every role assignment carries the minimum permissions needed.
       Excess permissions are stripped at Gate-9.

LP-02  Wildcard permissions (*) are FORBIDDEN in RBAC assignments.
       Detection at Gate-9 → DENY + alert Privilege Admin.

LP-03  Privileged operations require JIT elevation.
       Standing privileged access = NON-COMPLIANT.

LP-04  JIT grants are:
       ├── Time-bound (minutes/hours, not days)
       ├── Purpose-bound (declared reason required)
       ├── Domain-bound (blast domain scoped)
       └── Auto-expiring (no manual extension without re-approval)

LP-05  Privilege drift detection:
       ├── Daily scan of all active role assignments
       ├── Compare intended vs actual permissions
       ├── Flag excess permissions
       └── Notify Privilege Admin for remediation

LP-06  Service identities use minimal scoped tokens:
       ├── No shared service accounts
       ├── No long-lived tokens without rotation
       ├── No broad IAM roles for convenience
       └── Ephemeral credentials where possible

LP-07  Unused permissions are flagged after 30 days of non-use.
       Flagged permissions require owner attestation or revocation.

LP-08  Privilege revocation is immediate on:
       ├── Role change
       ├── Tenant exit
       ├── Session completion (for JIT)
       ├── Employment termination
       ├── Security incident
       └── Inactivity threshold breach
```

---

## 1️⃣3️⃣ PAM (PRIVILEGED ACCESS MANAGEMENT) INTEGRATION

```
PAM_INTEGRATION_RULES:

PAM-F01  All privileged actions routed through PAM broker.
         Direct root/superuser access → DENY + BREACH_LOG.

PAM-F02  PAM broker generates scoped, time-limited credential
         for each approved JIT session. Credential never exposed to human.

PAM-F03  Every PAM session:
         ├── Full session recording (commands, keystrokes)
         ├── Real-time anomaly monitoring
         └── Immediate termination on policy violation

PAM-F04  Break-glass procedure:
         ├── STEP-1: Break-glass request filed with justification
         ├── STEP-2: Dual approval by two distinct approvers
         ├── STEP-3: Time-bound window granted (max 2 hours)
         ├── STEP-4: Full session recording activated
         ├── STEP-5: Real-time monitoring by Security Admin
         ├── STEP-6: Post-use mandatory audit within 24 hours
         └── Single-approver break-glass → DENY

PAM-F05  ops.killswitch.invoke (Platform kill-switch):
         ├── 3-person quorum required (no 2-person approval)
         ├── Hardware MFA for all 3 approvers
         ├── Recorded session
         ├── Auto-reversal after configurable window
         └── Post-invocation incident report mandatory

PAM-F06  Exam / certification protection mode:
         During active exams or certifications:
         ├── Score-altering operations: BLOCKED unless emergency
         ├── Emergency override: Emergency flag + dual approval + post-audit
         └── No silent intervention permitted
```

---

## 1️⃣4️⃣ ACCOUNT STATE FIREWALL

```
ACCOUNT_STATE_ENFORCEMENT:

The APFA checks account state at Gate-1 (Context Resolver).
No action proceeds if account is not in an active state.

STATE              │ APFA DECISION
───────────────────┼──────────────────────────────────────────
CREATED            │ DENY all actions (no access until verified)
VERIFIED           │ DENY non-baseline actions (verification only)
ACTIVATED          │ PERMIT baseline permissions only
PRIVILEGED         │ PERMIT role-based permissions (within RBAC)
SUSPENDED          │ DENY all actions (no login, no API)
DEACTIVATED        │ DENY all actions (no token accepted)
ARCHIVED           │ DENY all mutations (read-only compliance access)
DELETED            │ DENY all actions (token blacklisted)

State transition attempts that skip states → DENY + LIFECYCLE_VIOLATION_LOG
Silent account state changes → FORBIDDEN + SECURITY_ALERT
```

---

## 1️⃣5️⃣ RATE LIMITING & ABUSE PREVENTION

```yaml
rate_limits:
  per_user:
    student:           { per_minute: 60, per_hour: 500, burst: 80 }
    trainer_mentor:    { per_minute: 100, per_hour: 1000, burst: 150 }
    recruiter_hr:      { per_minute: 120, per_hour: 2000, burst: 180 }
    institute_admin:   { per_minute: 150, per_hour: 3000, burst: 200 }
    enterprise_admin:  { per_minute: 200, per_hour: 5000, burst: 300 }
    platform_admin:    { per_minute: 300, per_hour: 10000, burst: 400 }
    parent:            { per_minute: 20,  per_hour: 100,  burst: 30 }

  per_automation_agent:
    standard:          { per_minute: 500, per_hour: 20000, burst: false }
    high_priority:     { per_minute: 2000, per_hour: 50000, burst: false }
    requires_agent_token: true
    high_stakes_intents: { per_minute: 10, per_hour: 100, burst: false }

  per_ip:
    standard:          { per_minute: 200, per_hour: 2000 }
    suspicious_ip:     { per_minute: 10, per_hour: 50 }
    blocked_ip:        { rate: 0 }

  per_tenant:
    base_plan:         { per_minute: 1000, per_hour: 20000 }
    enterprise_plan:   { per_minute: 10000, per_hour: 200000 }

abuse_detection_patterns:
  - credential_stuffing:   > 5 failed logins from same IP in 1 minute
  - scraping:              > 1000 read-only requests from same IP in 5 min
  - bulk_ai_inference:     > 200 AI intent calls in 1 min from same user
  - permission_probing:    > 20 DENY responses in 1 min from same user
  - cross_tenant_probing:  any cross-tenant attempt
  - data_exfiltration:     export of > 10000 records in single session

abuse_responses:
  credential_stuffing:     BLOCK_IP + ALERT_SECURITY_ADMIN
  scraping:                RATE_THROTTLE + LOG
  bulk_ai_inference:       BLOCK_USER + ALERT_TRUST_SAFETY
  permission_probing:      BLOCK_USER_TEMPORARILY + ALERT_SECURITY
  cross_tenant_probing:    BLOCK_USER_IMMEDIATELY + ALERT_SECURITY_ADMIN
  data_exfiltration:       BLOCK_SESSION + ALERT_DPO + COMPLIANCE_INCIDENT
```

---

## 1️⃣6️⃣ PII & DATA FIREWALL RULES

```
PII_FIREWALL_RULES:

PII-F01  All PII fields are masked by default.
         Role must explicitly hold permission to access unmasked PII field.
         Missing PII permission → return masked value, not DENY.
         Exception: record-level access is denied, field-level falls back to masked.

PII-F02  PII fields classified by sensitivity:
         ├── LEVEL_1 (Low): display_name, avatar_url, domain_track
         ├── LEVEL_2 (Medium): email (masked), phone (masked)
         ├── LEVEL_3 (High): full_name, address, date_of_birth
         ├── LEVEL_4 (Restricted): government_id, financial_data
         └── LEVEL_5 (Critical): biometric, health, minor data

PII-F03  Unmask operations require:
         ├── Role permission: admin.pii.unmask
         ├── Purpose justification (mandatory field)
         ├── Time-bound access window (max 4 hours)
         ├── DPO notification (automated)
         └── Audit log: who, why, when, what unmasked

PII-F04  Minor data (is_minor = true):
         ├── Requires LEVEL_5 protection always
         ├── No profiling or behavioral analytics permitted
         ├── No AI advisory that profiles minor without guardian consent
         ├── Parent role may view child's summary only (not raw PII)
         └── Breach of minor data → immediate DPO + Compliance Admin alert

PII-F05  Cross-border PII transfer:
         ├── APFA checks geo_region at Gate-10
         ├── Disallowed regions → DENY_CROSS_BORDER_BLOCKED
         ├── Allowed with legal basis → LOG + DPO notification
         └── Unknown region → DENY (default)

PII-F06  Dojo session PII:
         ├── Usernames preferred over real names in recordings
         ├── Transcripts auto-redact PII fields
         ├── Recording replay respects viewer's masking level
         └── No PII overlay in recorded video streams
```

---

## 1️⃣7️⃣ FIREWALL AUDIT SCHEMA (IMMUTABLE)

All APFA decisions write to `firewall_audit_log`. Append-only. No exceptions.

```sql
CREATE TABLE firewall_audit_log (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  request_id            UUID NOT NULL,
  correlation_id        UUID NOT NULL,
  timestamp             TIMESTAMP NOT NULL DEFAULT NOW(),

  -- Actor context
  actor_id              UUID NOT NULL,
  actor_type            TEXT NOT NULL,  -- human|service|automation|webhook
  actor_tenant_id       UUID NOT NULL,
  actor_domain_track    TEXT NOT NULL,
  actor_role            TEXT NOT NULL,
  actor_blast_domain    TEXT NOT NULL,
  actor_auth_strength   TEXT NOT NULL,
  actor_session_id      UUID NOT NULL,
  actor_is_minor        BOOLEAN NOT NULL DEFAULT FALSE,
  actor_ip              TEXT NOT NULL,
  actor_device_id       TEXT,

  -- Action context
  permission_code       TEXT NOT NULL,
  resource_type         TEXT NOT NULL,
  resource_id           UUID,
  resource_tenant_id    UUID NOT NULL,
  resource_domain_track TEXT,
  is_ai_intent          BOOLEAN NOT NULL DEFAULT FALSE,
  ai_intent_code        TEXT,
  is_high_stakes        BOOLEAN NOT NULL DEFAULT FALSE,
  is_bulk_operation     BOOLEAN NOT NULL DEFAULT FALSE,
  is_pii_access         BOOLEAN NOT NULL DEFAULT FALSE,
  source_module         TEXT NOT NULL,
  platform_stage        TEXT NOT NULL,

  -- Decision
  decision              TEXT NOT NULL,   -- PERMIT|DENY
  decision_code         TEXT NOT NULL,   -- from §8 taxonomy
  gate_reached          INTEGER NOT NULL, -- 0–12
  latency_ms            INTEGER NOT NULL,

  -- Obligations (on PERMIT)
  advisory_label_required   BOOLEAN DEFAULT FALSE,
  explanation_required      BOOLEAN DEFAULT FALSE,
  human_review_required     BOOLEAN DEFAULT FALSE,
  pii_masking_applied       BOOLEAN DEFAULT FALSE,
  step_up_mfa_required      BOOLEAN DEFAULT FALSE,
  compliance_notified       BOOLEAN DEFAULT FALSE,

  -- Risk context
  model_risk_level          TEXT,
  cross_tenant_attempted    BOOLEAN NOT NULL DEFAULT FALSE,
  cross_domain_attempted    BOOLEAN NOT NULL DEFAULT FALSE,
  blast_domain_breach       BOOLEAN NOT NULL DEFAULT FALSE,
  adversarial_signal        BOOLEAN NOT NULL DEFAULT FALSE,
  rate_limit_triggered      BOOLEAN NOT NULL DEFAULT FALSE,
  abuse_pattern_detected    BOOLEAN NOT NULL DEFAULT FALSE,

  -- Security alerts
  security_alert_raised     BOOLEAN NOT NULL DEFAULT FALSE,
  dpo_notified              BOOLEAN NOT NULL DEFAULT FALSE,
  compliance_incident_opened BOOLEAN NOT NULL DEFAULT FALSE,

  created_at            TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Partition by actor_tenant_id + month for performance
-- No DELETE or UPDATE permitted on this table
-- Encryption at rest: MANDATORY (AES-256)
-- Retention: 5 years for HIGH_STAKES, 2 years standard
-- Index on: actor_id, resource_tenant_id, decision, ai_intent_code, timestamp
```

---

## 1️⃣8️⃣ OBSERVABILITY & ALERTING

```yaml
prometheus_metrics:
  - apfa_decisions_total{decision, decision_code, permission_code}
  - apfa_gate_failures_total{gate_number, decision_code}
  - apfa_latency_ms{gate, percentile}
  - apfa_ai_intent_denials_total{intent_code, reason}
  - apfa_cross_tenant_attempts_total{actor_tenant, resource_tenant}
  - apfa_cross_domain_attempts_total{actor_domain, resource_domain}
  - apfa_adversarial_payloads_total{intent_code}
  - apfa_minor_restrictions_total{intent_code}
  - apfa_mfa_required_total{operation_class}
  - apfa_pii_access_events_total{pii_level, masked}
  - apfa_rate_limit_hits_total{actor_type, limit_type}
  - apfa_break_glass_invocations_total
  - apfa_audit_write_failures_total  ← CRITICAL metric (DENY on audit fail)

grafana_dashboards:
  - APFA Decision Dashboard     (permit/deny rates, top deny reasons)
  - Security Threat Monitor     (cross-tenant, adversarial, privilege escalation)
  - AI Permission Gate Monitor  (AI intent denials, high-stakes, minor restrictions)
  - PII Access Control Board    (unmask events, minor data access, cross-border)
  - Rate Limit & Abuse Feed     (quotas, abuse patterns, bot detection)
  - PAM & Break-Glass Tracker   (JIT windows, break-glass events, privilege drift)
  - Compliance Obligation Board (advisory labels, explanations, human reviews)

pagerduty_alerts:
  cross_tenant_attempt_detected:     IMMEDIATE PAGE SECURITY_ADMIN
  blast_domain_breach_detected:      IMMEDIATE PAGE SECURITY_ADMIN
  adversarial_payload_detected:      IMMEDIATE PAGE SECURITY_ADMIN
  minor_data_breach_attempt:         IMMEDIATE PAGE DPO + COMPLIANCE_ADMIN
  audit_write_failure:               IMMEDIATE PAGE ONCALL (systemic risk)
  break_glass_invoked:               PAGE SECURITY_ADMIN + COMPLIANCE_ADMIN
  privilege_escalation_detected:     IMMEDIATE PAGE SECURITY_ADMIN
  abuse_pattern_sustained_10min:     PAGE TRUST_SAFETY_OFFICER
  apfa_latency_p99 > 100ms:          PAGE ML_INFRA_TEAM (SLO risk)
```

---

## 1️⃣9️⃣ ANTIGRAVITY EXECUTION BINDING INSTRUCTIONS

When Antigravity generates ANY code, infrastructure, UI, API contract, or service on the Ecoskiller platform, these 20 binding instructions apply without exception.

```
1.  EVERY API endpoint MUST pass through the APFA before processing.
    No endpoint may execute business logic without APFA PERMIT.
    Unprotected endpoints = SECURITY_FAILURE.

2.  EVERY AI/ML inference call MUST pass through Gate-7 specifically.
    Direct ML calls bypassing Gate-7 = CRITICAL VIOLATION.

3.  EVERY Flutter screen and React component consuming ML results
    MUST render the advisory label obligation from the APFA response.
    Unlabelled AI output in UI = NON-COMPLIANT BUILD.

4.  HIGH-STAKES operations (gd.score.override, ai.advisory.override,
    admin.pii.unmask, ops.deployment.approve) MUST generate:
    ├── Step-up MFA challenge flow
    ├── Dual approval UI for operations requiring it
    └── Session recording hook for PAM-brokered operations

5.  MINOR-flagged accounts MUST automatically have ML scope restricted.
    Generate account_type = minor guard in every user service resolver.

6.  PARENT role accounts MUST be read-only at the data layer.
    No mutation endpoint should be reachable by PARENT role.
    RBAC must have zero mutation permissions for PARENT role.

7.  AUTOMATION / AI_AGENT service tokens MUST use client-credential
    OAuth2 flow. Generate client_credentials grant — never authorization_code
    — in service-to-service auth configuration.

8.  ALL JIT privilege windows MUST auto-expire. Generate expiry_at on
    every JIT grant record. Missing expiry = SECURITY_FAILURE.

9.  BREAK-GLASS flow MUST generate dual-approval UI with distinct
    approver identity verification. Single-approver path = FORBIDDEN.

10. DOMAIN isolation MUST be enforced at the database query layer.
    Every ORM model must inject tenant_id + domain_track WHERE clause.
    Missing domain filter = DATA LEAKAGE RISK.

11. BLAST DOMAIN separation MUST be enforced with separate database
    schemas or separate database instances per blast domain.
    Shared tables across blast domains = ARCHITECTURAL VIOLATION.

12. PII masking MUST be applied server-side before response serialization.
    Client-side masking = FORBIDDEN (bypassable). Generate masking
    interceptors in the API response serializer layer.

13. RATE LIMITS MUST be enforced at the API gateway (Kong) layer,
    not inside individual services. Service-level rate limiting alone = INSUFFICIENT.
    Generate Kong rate-limit plugins for all routes.

14. APFA latency SLO is < 20ms for standard, < 50ms for ABAC.
    Generate Redis-backed permission cache for standard RBAC lookups.
    Cache TTL: 60 seconds. Invalidate on role change event (Kafka).
    ABAC policies evaluated fresh — no caching for complex policies.

15. AUDIT WRITE must precede PERMIT response. Generate:
    firewall_audit_log write in a database transaction that, if it fails,
    returns DENY (503) not PERMIT. Never permit without audit confirmation.

16. ALL DENY responses must be user-friendly.
    Generate an error message mapper that converts decision_code to
    plain-language messages. Never expose internal codes to users.

17. Step-up MFA is per-action. Generate ephemeral step_up_token with
    single-use constraint. Reuse of step_up_token = DENY.

18. CI/CD pipelines MUST include permission drift detection step.
    Generate a CI job that compares declared permissions vs actual
    RBAC assignments and fails the pipeline on drift detection.

19. Every new permission_code introduced in code MUST be registered
    in the permission_registry table via migration. Unknown permission
    codes at runtime = DENY. Missing migration = BUILD FAILURE.

20. Exam/certification mode detection MUST be injected into Gate-5
    (ABAC environmental conditions). Generate an exam_mode_service
    that exposes the active exam state to APFA context resolution.
```

---

## 2️⃣0️⃣ VERSIONING & CHANGE GOVERNANCE

```
APFA_VERSION = SEMVER (MAJOR.MINOR.PATCH)

MAJOR bump required for:
  - New gate added to the 12-gate pipeline
  - Existing gate logic fundamentally changed
  - New permission namespace added (§4.3)
  - Decision code taxonomy changes (§8)
  - User class permission matrix changes (§6)

MINOR bump required for:
  - New rate limit tier
  - New MFA strength mapping (§10)
  - New PII level classification
  - New abuse detection pattern
  - New Prometheus metric

PATCH bump required for:
  - Bug fixes in gate logic
  - Threshold adjustments (documented)
  - Documentation updates

CHANGE_CONTROL:
  ARCHITECT_APPROVAL_REQUIRED = true
  SECURITY_ADMIN_SIGN_OFF     = true
  DUAL_APPROVAL               = required for MAJOR version changes
  BACKWARD_COMPAT             = MINIMUM_2_VERSIONS
  RULE_CREATION_BY_AI         = FORBIDDEN (humans only)
  SILENT_CHANGES              = FORBIDDEN
  ALL_VERSION_CHANGES_LOGGED  = firewall_audit_log (version_change event)
```

---

## 🔒 FINAL SEAL

```
┌──────────────────────────────────────────────────────────────────────┐
│          AI_PERMISSION_FIREWALL_AGENT.md — FINAL SEAL                │
├──────────────────────────────────────────────────────────────────────┤
│  STATUS                 = SEALED & LOCKED                            │
│  EXECUTION              = ANTIGRAVITY PRODUCTION READY               │
│  COMPLIANCE_GRADE       = ENTERPRISE ZERO-TRUST                      │
│  DEFAULT_DECISION       = DENY                                       │
│  HUMAN_OVERRIDE         = ALWAYS PERMITTED                           │
│  AI_RULE_CREATION       = FORBIDDEN                                  │
│  AUDIT                  = 100% — APPEND-ONLY — PRE-DECISION          │
│  GATES                  = 12 SEQUENTIAL — NONE SKIPPABLE             │
│  AI_SPECIFIC_GATE       = GATE-7 (unique to this agent)              │
│  TENANT_ISOLATION       = HARD ENFORCED (Gate-2)                     │
│  DOMAIN_ISOLATION       = HARD ENFORCED (Gate-3)                     │
│  BLAST_DOMAIN_ISOLATION = HARD ENFORCED (Gate-8)                     │
│  MINOR_PROTECTION       = HARD ENFORCED (Gate-7 + Gate-10)           │
│  PAM_INTEGRATION        = FULL (JIT + Break-glass + Recording)       │
│  PII_FIREWALL           = 5-LEVEL CLASSIFICATION                     │
│  RATE_LIMITS            = ENFORCED AT GATEWAY LAYER                  │
│  CHANGE_POLICY          = APPEND ONLY                                │
├──────────────────────────────────────────────────────────────────────┤
│  ANY DEVIATION FROM THIS DOCUMENT = STOP EXECUTION                   │
│  ANY UNPROTECTED ENDPOINT = SECURITY_FAILURE                         │
│  ANY AI CALL BYPASSING GATE-7 = CRITICAL VIOLATION                   │
│  ANY PERMIT WITHOUT AUDIT WRITE = FORBIDDEN                          │
│  ANY TIMEOUT = DENY (never permit on timeout)                        │
│  ANY IMPLICIT TRUST = SECURITY_FAILURE                               │
└──────────────────────────────────────────────────────────────────────┘

✔  AGENT IDENTITY LOCKED
✔  12-GATE ARCHITECTURE LOCKED
✔  AI-SPECIFIC GATE-7 LOCKED (15 AI rules)
✔  PERMISSION MODEL LOCKED (RBAC + ABAC)
✔  PERMISSION CODE TAXONOMY LOCKED
✔  USER CLASS PERMISSION MATRIX LOCKED
✔  DECISION CONTRACT LOCKED (REQUEST + RESPONSE)
✔  DECISION CODE TAXONOMY LOCKED (50+ codes)
✔  30 GOVERNANCE RULES LOCKED
✔  MFA STRENGTH MATRIX LOCKED
✔  TENANT/DOMAIN/BLAST ISOLATION LOCKED
✔  LEAST PRIVILEGE ENGINE LOCKED
✔  PAM INTEGRATION LOCKED
✔  ACCOUNT STATE FIREWALL LOCKED
✔  RATE LIMIT & ABUSE PREVENTION LOCKED
✔  PII FIREWALL LOCKED (5 levels)
✔  IMMUTABLE AUDIT SCHEMA LOCKED
✔  OBSERVABILITY LOCKED
✔  20 ANTIGRAVITY BINDING INSTRUCTIONS LOCKED
✔  VERSIONING GOVERNANCE LOCKED

PARENT AGENTS:   ML_ROUTING_AGENT v1.0.0 | MODEL_RISK_AGENT v1.0.0
SIBLING AGENTS:  COMPLIANCE_AGENT | AUDIT_AGENT | FRAUD_AGENT
REPORTING TO:    SECURITY_ADMIN | COMPLIANCE_ADMIN | PLATFORM_SUPER_ADMIN
COMPLIANCE:      RBAC | ABAC | MFA-COMP-v1 | AUTH-COMP-v1 |
                 SESSION-COMP-v1 | R57 | R58 | R59 | PAM-LAW

FURTHER CHANGES = APPEND ONLY
CHANGE AUTHORITY = ARCHITECT + SECURITY_ADMIN DUAL APPROVAL REQUIRED
AI-CREATED RULES = FORBIDDEN (human architects only)
```

---

*AI_PERMISSION_FIREWALL_AGENT.md · Ecoskiller Enterprise SaaS · Version 1.0.0*
*Sealed for Antigravity Execution Engine · Principal Security Architect Grade*
*Inherits from: ML_ROUTING_AGENT v1.0.0 | MODEL_RISK_AGENT v1.0.0 | Platform Master Constitution*
*Compliance Chains: R57 (Least Privilege) | R58 (Account Lifecycle) | R59 (PAM) |*
*MFA-COMP-v1 | AUTH-COMP-v1 | SESSION-COMP-v1 | DATA-INTEGRITY-v1 | PM-O (PII)*
