# 🔒 SKILL_VERIFICATION_PROOF_AGENT.md
## ECOSKILLER ANTIGRAVITY — GOVERNANCE & CORE CONTROL LAYER
### Status: `SEALED` · `LOCKED` · `GOVERNED` · `DETERMINISTIC`
### Mutation Policy: `ADD-ONLY via version bump`
### Interpretation Authority: `NONE`
### Execution Authority: `Human declaration only`
### Document Version: `v1.0.0`
### Classification: `CORE GOVERNANCE — TIER 0 — TRUST CRITICAL`

---

> ⚠️ **SEAL NOTICE**: This agent specification is sealed and locked. No field, rule, formula, evidence type, or verification pathway may be modified, interpreted, inferred, extended, or overridden without a formally declared versioned amendment. All prior versions remain permanently immutable. Any execution deviating from this specification MUST HALT and immediately escalate to COMPLIANCE_AGENT and SECURITY_AGENT. Architecture interpretation is FORBIDDEN. Assumption filling is FORBIDDEN.

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```yaml
AGENT_NAME:            SKILL_VERIFICATION_PROOF_AGENT
AGENT_ID:              ECSK-AGT-GOVERN-0044
SYSTEM_ROLE:           >
  Skill Evidence Collector, Multi-Source Verifier,
  Proof-of-Skill Issuer & Trust Badge Arbitrator
PRIMARY_DOMAIN:        Cross-Domain Skill Governance
                       (Arts, Commerce, Science, Technology, Administration)
LAYER:                 Governance & Core Control
PLATFORM:              Ecoskiller Antigravity
EXECUTION_MODE:        Deterministic + Validated + Evidence-Gated
DATA_SCOPE:            >
  Skill completion records, Dojo session results, project milestone
  evidence, assessment scores, employer verification signals,
  institute verification tokens, belt records, peer endorsements,
  portfolio artifacts, intelligence profile scores, learning path
  progression data, certification issuance records, trust badge state.
  Write authority: SKILL_PROOF_STORE, BADGE_REGISTRY, AUDIT_STORE.
  Read authority: DOJO_SESSION_STORE, BELT_REGISTRY, ASSESSMENT_STORE,
  PROJECT_MILESTONE_STORE, EMPLOYER_VERIFICATION_REGISTRY,
  INSTITUTE_VERIFICATION_REGISTRY, INTELLIGENCE_PROFILE_STORE,
  RBAC_REGISTRY, TENANT_CONFIG, MODEL_REGISTRY.
TENANT_SCOPE:          Strict Tenant Isolation — skill proofs never cross tenant boundaries
FAILURE_POLICY:        >
  HALT_ON_AMBIGUITY |
  REJECT_INCOMPLETE_EVIDENCE |
  BLOCK_BADGE_ON_LOW_CONFIDENCE |
  ESCALATE_ON_FRAUD_SIGNAL |
  NO_SILENT_FAILURES
SECURITY_MODEL:        Zero-Trust Multi-Tenant + Evidence Integrity Chain
AUDIT_POLICY:          Append-Only Immutable Audit Trail — every verification decision logged
VERSION:               v1.0.0
BACKWARD_COMPATIBLE:   TRUE
```

> **RULE**: This agent is the single authority for issuing verified skill proof records and trust badges on Ecoskiller Antigravity. No other agent, service, system, or human actor may issue, modify, or revoke skill badges outside the declared pathways of this agent. Any attempt = GOVERNANCE_VIOLATION.

---

## 2️⃣ PURPOSE DECLARATION

### Problem Solved

Ecoskiller Antigravity positions itself as a unified Job Portal + Skill Development + Project Execution + Education + Marketplace SaaS. Its core value proposition to employers, recruiters, institutes, and students depends entirely on one guarantee: **verified, evidence-backed skill credentials that cannot be faked, inflated, or gamed.**

At scale (10M–100M users) across five domain tracks and a multi-tenant architecture serving students, trainers, evaluators, institutes, enterprises, recruiters, admins, and parents — the following critical failures become inevitable without a dedicated Skill Verification Proof Agent:

- **Credential inflation** — users claiming skill badges without complete evidence chains
- **Assessment bypass** — certificates issued from incomplete or anomaly-flagged sessions
- **Cross-tenant contamination** — skill proofs leaking across tenant boundaries
- **Stale credential issuance** — badges issued from outdated rubric versions without re-certification triggers
- **Employer trust collapse** — recruiter-facing skill data not traceable to raw evidence
- **Institute verification fraud** — unverified institution credentials inflating skill credibility
- **Portfolio fabrication** — portfolio artifacts submitted without verifiable evidence links
- **Intelligence profile manipulation** — Howard Gardner intelligence scores gamed to bias career path recommendations
- **Belt version mismatch** — belts awarded under superseded rubrics presented as current
- **Royalty system abuse** — unverified skills used to claim innovation royalty eligibility

The `SKILL_VERIFICATION_PROOF_AGENT` solves all of these by acting as the platform's **single evidence collection, multi-source verification, and trust badge issuance authority** — consuming evidence from every skill-generation surface (Dojo, Projects, Assessments, Learning Paths, Employer Feedback, Institution Verification), computing a governed confidence score, and issuing cryptographically-referenced, versioned skill proof records and trust badges only when all verification gates pass.

### Input Consumed
- Dojo session completion records (from DOJO_SESSION_ORCHESTRATION_AGENT)
- Belt promotion records (from BELT_ENGINE)
- Project milestone completion evidence (from PROJECT_MILESTONE_EVALUATOR)
- Learning path completion events (from LEARNING_PATH_AGENT)
- Skill gap assessment results (from SKILL_RANKING_AGENT)
- Intelligence profile scores (from INTELLIGENCE_PROFILE_AGENT)
- Employer skill feedback signals (from EMPLOYER_FEEDBACK_AGENT)
- Institution domain verification tokens (from INSTITUTION_VERIFICATION_AGENT)
- Company employee verification signals (from COMPANY_VERIFICATION_AGENT)
- Peer endorsement events (from REPUTATION_SCORE_AGENT)
- Anomaly flags (from ANOMALY_BEHAVIOR_DETECTION_AGENT)
- Confidence interval envelopes (from INTELLIGENCE_CONFIDENCE_INTERVAL_AGENT)
- Portfolio artifact submissions (from PORTFOLIO_AGENT)
- Rubric + skill version references (from SCENARIO_REGISTRY)

### Output Produced
- Signed, versioned Skill Proof Records stored in SKILL_PROOF_STORE
- Trust Badge issuance commands to BADGE_REGISTRY
- Verified skill vectors emitted to FEATURE_STORE_AGENT
- Employer-facing Skill Proof Reports (via MARKETPLACE_AGENT)
- Certificate generation triggers to CERTIFICATE_ENGINE
- Royalty eligibility signals to ROYALTY_ENGINE
- Skill credential verification webhooks (for enterprise LMS/HRIS integration)
- Rejection records with detailed evidence gap reports
- Audit records for every verification decision
- Re-verification trigger events when rubric or model version changes

### Downstream Agents Depending on This Agent
```
BADGE_REGISTRY                    # Receives badge issuance commands
CERTIFICATE_ENGINE                # Receives certificate generation triggers
JOB_MATCH_ML_AGENT                # Receives verified skill vectors for matching
LEARNING_PATH_AGENT               # Receives verified skill state for path recalculation
REPUTATION_SCORE_AGENT            # Receives skill verification signals
GROWTH_ENGINE                     # Receives XP/Rank triggers on badge issuance
ROYALTY_ENGINE                    # Receives skill eligibility signals for innovation economy
MARKETPLACE_AGENT                 # Receives employer-facing skill proof reports
COMPLIANCE_AGENT                  # Receives all audit records + fraud escalation
OBSERVABILITY_AGENT               # Receives all verification metrics
FEATURE_STORE_AGENT               # Receives verified skill feature vectors
NOTIFICATION_AGENT                # Receives user-facing badge issuance notifications
INTELLIGENCE_PROFILE_AGENT        # Receives verification feedback for profile enrichment
TALENT_MARKETPLACE_AGENT          # Receives authenticated skill proof for hiring marketplace
```

### Upstream Agents Feeding This Agent
```
DOJO_SESSION_ORCHESTRATION_AGENT  # Session completion records
BELT_ENGINE                       # Belt promotion records
PROJECT_MILESTONE_EVALUATOR       # Project evidence packages
LEARNING_PATH_AGENT               # Completion events
SKILL_RANKING_AGENT               # Assessment results and gap analysis
INTELLIGENCE_PROFILE_AGENT        # Gardner intelligence type scores
EMPLOYER_FEEDBACK_AGENT           # Employer verification signals
INSTITUTION_VERIFICATION_AGENT    # Domain-verified institution tokens
COMPANY_VERIFICATION_AGENT        # Employee verification signals
REPUTATION_SCORE_AGENT            # Peer endorsement events
ANOMALY_BEHAVIOR_DETECTION_AGENT  # Integrity flags
INTELLIGENCE_CONFIDENCE_INTERVAL_AGENT  # CI envelopes for scoring output
PORTFOLIO_AGENT                   # Portfolio artifact submissions
SCENARIO_REGISTRY                 # Rubric + skill version references
```

---

## 3️⃣ SKILL VERIFICATION TAXONOMY (LOCKED)

```yaml
SKILL_PROOF_TYPES:
  # Each type has a distinct evidence chain and verification pathway.
  # Types are sealed — new types require MAJOR version bump.

  TYPE_1_DOJO_BELT_SKILL:
    description: >
      Skill verified through Dojo Group Discussion arena — belt-level achievement.
      Highest trust tier. Evidence chain includes: session record, scoring record,
      belt eligibility gate result, mentor certification reference, rubric version,
      CI confidence score, and anomaly clearance.
    trust_tier: GOLD
    minimum_evidence_sources: 5
    badge_style: DOJO_BELT_{color}
    employer_visibility: FULL — replay evidence accessible under role-based signed URL

  TYPE_2_PROJECT_MILESTONE_SKILL:
    description: >
      Skill verified through real-world project milestone completion with
      evidence-based evaluation. Includes: milestone record, evaluator sign-off,
      artifact hash, mentor review reference.
    trust_tier: SILVER
    minimum_evidence_sources: 3
    badge_style: PROJECT_VERIFIED
    employer_visibility: SUMMARY + artifact link (consent-gated)

  TYPE_3_ASSESSMENT_SKILL:
    description: >
      Skill verified through platform assessment (non-Dojo). Includes:
      assessment score record, CI confidence score, anti-cheat clearance.
    trust_tier: BRONZE
    minimum_evidence_sources: 2
    badge_style: ASSESSMENT_VERIFIED
    employer_visibility: SCORE_SUMMARY only

  TYPE_4_LEARNING_PATH_COMPLETION:
    description: >
      Skill verified through structured learning path completion with
      performance-linked milestones.
    trust_tier: BRONZE
    minimum_evidence_sources: 2
    badge_style: LEARNING_VERIFIED
    employer_visibility: COMPLETION_SUMMARY only

  TYPE_5_EMPLOYER_ENDORSED_SKILL:
    description: >
      Skill endorsed by a verified employer entity. Requires employer
      domain verification token. Highest external credibility signal.
    trust_tier: PLATINUM (when combined with TYPE_1 or TYPE_2)
    minimum_evidence_sources: 1 (employer token required)
    badge_style: EMPLOYER_ENDORSED
    employer_visibility: FULL — employer name + verification date

  TYPE_6_INSTITUTION_CERTIFIED_SKILL:
    description: >
      Skill certified by a verified educational institution. Requires
      institution domain verification token.
    trust_tier: SILVER
    minimum_evidence_sources: 1 (institution token required)
    badge_style: INSTITUTION_CERTIFIED
    employer_visibility: INSTITUTION_NAME + certification date

  TYPE_7_INTELLIGENCE_PROFILE_SKILL:
    description: >
      Skill aligned with verified Howard Gardner intelligence type profile.
      Used for career path alignment and talent matching.
      Evidence: intelligence assessment scores from Dojo exercises.
    trust_tier: INFORMATIONAL (career guidance only — not employer-presentable alone)
    minimum_evidence_sources: 3 (multi-session intelligence data)
    badge_style: INTELLIGENCE_ALIGNED
    employer_visibility: NONE — internal platform use only

TRUST_TIER_HIERARCHY:
  PLATINUM > GOLD > SILVER > BRONZE > INFORMATIONAL

COMPOSITE_BADGE_RULE:
  A user may hold composite badges when multiple proof types apply to same skill:
  example: DOJO_BELT_GOLD + EMPLOYER_ENDORSED = PLATINUM composite badge
  composite_computation: This agent computes, issues, and seals all composite badges
```

---

## 4️⃣ INPUT CONTRACT (STRICT)

### SKILL_PROOF_REQUEST_SCHEMA

```json
{
  "required_fields": [
    "proof_request_id",
    "tenant_id",
    "user_id",
    "skill_id",
    "skill_version",
    "domain_tag",
    "proof_type",
    "evidence_package",
    "requesting_agent_id",
    "timestamp_utc",
    "request_signature"
  ],
  "optional_fields": [
    "portfolio_artifact_refs",
    "employer_verification_token",
    "institution_verification_token",
    "parent_proof_id",
    "intelligence_profile_ref",
    "override_justification",
    "localization_tag",
    "enterprise_export_flag"
  ],
  "evidence_package_schema": {
    "required_per_proof_type": {
      "TYPE_1_DOJO_BELT_SKILL": [
        "dojo_session_id",
        "belt_record_id",
        "scoring_record_id",
        "mentor_certification_ref",
        "rubric_version",
        "ci_confidence_score",
        "anomaly_clearance_ref",
        "session_integrity_status"
      ],
      "TYPE_2_PROJECT_MILESTONE_SKILL": [
        "milestone_record_id",
        "evaluator_sign_off_ref",
        "artifact_hash",
        "project_id",
        "completion_timestamp_utc"
      ],
      "TYPE_3_ASSESSMENT_SKILL": [
        "assessment_id",
        "assessment_score",
        "ci_confidence_score",
        "anti_cheat_clearance_ref"
      ],
      "TYPE_4_LEARNING_PATH_COMPLETION": [
        "learning_path_id",
        "completion_record_id",
        "performance_score",
        "completion_timestamp_utc"
      ],
      "TYPE_5_EMPLOYER_ENDORSED_SKILL": [
        "employer_verification_token",
        "employer_id",
        "endorsement_date_utc",
        "skill_context_description"
      ],
      "TYPE_6_INSTITUTION_CERTIFIED_SKILL": [
        "institution_verification_token",
        "institution_id",
        "certification_date_utc",
        "program_reference"
      ],
      "TYPE_7_INTELLIGENCE_PROFILE_SKILL": [
        "intelligence_profile_id",
        "intelligence_type",
        "intelligence_score",
        "session_evidence_refs",
        "profile_version"
      ]
    }
  },
  "validation_rules": [
    "proof_request_id MUST be UUID v4 — globally unique",
    "tenant_id MUST match authenticated JWT claim",
    "user_id MUST belong to tenant_id — cross-tenant = REJECT + SECURITY_ALERT",
    "skill_id MUST exist in SKILL_REGISTRY for this tenant and domain",
    "skill_version MUST match current published version in SKILL_REGISTRY",
    "domain_tag MUST be one of [Arts, Commerce, Science, Technology, Administration]",
    "proof_type MUST be one of the seven declared TYPE identifiers",
    "evidence_package MUST contain all required fields for the declared proof_type",
    "requesting_agent_id MUST be registered in AGENT_REGISTRY",
    "request_signature MUST be HMAC-SHA256 of canonical request payload",
    "ci_confidence_score (where required) MUST be float [0.0–1.0]",
    "session_integrity_status (where required) MUST be CLEAN — ANOMALY_FLAGGED or DISPUTED = REJECT",
    "anomaly_clearance_ref MUST reference a record in ANOMALY_STORE with governance_outcome = CLEARED"
  ],
  "security_checks": [
    "JWT tenant_id isolation verified on every request",
    "Cross-tenant user_id = REJECT + SECURITY_INCIDENT",
    "employer_verification_token validated against EMPLOYER_VERIFICATION_REGISTRY",
    "institution_verification_token validated against INSTITUTION_VERIFICATION_REGISTRY",
    "Duplicate proof_request_id within 3600s = idempotent response — no duplicate proof created",
    "Artifact hashes verified against PORTFOLIO_AGENT registry — tampering = REJECT + FRAUD_ALERT",
    "All evidence references must be verifiable in their respective source stores"
  ],
  "domain_checks": [
    "skill_id domain must match domain_tag — cross-domain skill injection = REJECT",
    "Dojo session domain must match domain_tag",
    "Intelligence type must be valid for domain context"
  ]
}
```

### NULL TOLERANCE POLICY

```yaml
null_tolerance:                    ZERO on all required fields
missing_required_evidence_field:   REJECT → EVIDENCE_GAP_REPORT → LOG → no badge issued
null_optional_field:               ACCEPT → FLAG in audit record
incomplete_evidence_package:       REJECT → detailed gap report emitted → re-submission required
expired_verification_token:        REJECT → notify source agent → re-verification required
```

---

## 5️⃣ VERIFICATION PIPELINE — DETAILED EXECUTION

```yaml
# All verification steps execute in declared order.
# No step may be skipped. No step may be reordered.

STEP_1_REQUEST_VALIDATION:
  action: Validate all fields per INPUT_CONTRACT Section 4
  failure: REJECT + EVIDENCE_GAP_REPORT + LOG

STEP_2_EVIDENCE_COMPLETENESS_CHECK:
  action: >
    Verify that evidence_package contains ALL required fields
    for the declared proof_type. Every referenced record must
    exist and be readable in its source store.
  failure: REJECT + EVIDENCE_GAP_REPORT detailing missing fields

STEP_3_EVIDENCE_INTEGRITY_CHECK:
  action: >
    For each evidence reference in the package:
    - Verify record exists in source store
    - Verify record tenant_id matches request tenant_id
    - Verify record domain_tag matches request domain_tag
    - Verify record is not in a disputed/corrupted/void state
    - For TYPE_1: verify session_integrity_status = CLEAN
    - For TYPE_2: verify artifact_hash matches PORTFOLIO_AGENT registry
    - For TYPE_3/4: verify anti_cheat or anomaly clearance exists
    - For TYPE_5/6: verify token authenticity and expiry
  failure: REJECT + INTEGRITY_FAILURE_REPORT + LOG
  fraud_signal: Tampered artifact hash or forged token = FRAUD_ALERT → ANOMALY_BEHAVIOR_DETECTION_AGENT

STEP_4_ANOMALY_CLEARANCE_CHECK:
  action: >
    Query ANOMALY_BEHAVIOR_DETECTION_AGENT records for user_id:
    - Any unresolved CRITICAL or HIGH anomaly → REJECT (proof blocked)
    - Any unresolved MEDIUM anomaly related to this skill domain → DEFER
    - All CRITICAL/HIGH anomalies resolved → PROCEED
    - MEDIUM anomaly in different domain → PROCEED with flag
  failure: BLOCK_BADGE + LOG + notify COMPLIANCE_AGENT

STEP_5_CONFIDENCE_SCORE_GATE:
  action: >
    Retrieve CI envelope from INTELLIGENCE_CONFIDENCE_INTERVAL_AGENT
    for each scoring-based evidence source.
    Compute weighted verification confidence score:
    VERIFICATION_CONFIDENCE = weighted_average(
      ci_score_per_evidence_source,
      completeness_ratio,
      evidence_freshness_score,
      anomaly_clearance_score,
      inter_source_consistency_score
    )
  thresholds:
    GOLD_BADGE:       >= 0.85
    SILVER_BADGE:     >= 0.70
    BRONZE_BADGE:     >= 0.60
    INFORMATIONAL:    >= 0.50
    REJECT:           < 0.50 — no badge issued; evidence gap report emitted
  failure: BLOCK_BADGE + LOG_LOW_CONFIDENCE + re-submission permitted after evidence gap resolved

STEP_6_RUBRIC_VERSION_CHECK:
  action: >
    Verify that all evidence was produced under current or
    explicitly approved rubric versions.
    Stale rubric evidence (superseded by MAJOR version change):
    → Require re-verification under current rubric (per T17 Dojo spec)
    Minor rubric change (MINOR/PATCH bump):
    → Accept with rubric_version_mismatch_flag in proof record
  failure: REJECT_WITH_RE_VERIFICATION_REQUIRED + LOG

STEP_7_MENTOR_CERTIFICATION_CHECK (TYPE_1 only):
  action: >
    Verify mentor_certification_ref in MENTOR_CERTIFICATION_REGISTRY.
    Mentor must have been certified at time of session.
    Expired or revoked mentor certification at session time:
    → Belt evidence from that session is INVALID for TYPE_1 proof
  failure: REJECT_TYPE_1 + LOG + notify MENTOR_CALIBRATION_AGENT

STEP_8_EMPLOYER_TOKEN_VALIDATION (TYPE_5 only):
  action: >
    Validate employer_verification_token against EMPLOYER_VERIFICATION_REGISTRY.
    Verify: employer domain verified, token not revoked, not expired.
    Employer must be a verified company entity (per R37 — Company Verification Law).
  failure: REJECT_TYPE_5 + LOG

STEP_9_INSTITUTION_TOKEN_VALIDATION (TYPE_6 only):
  action: >
    Validate institution_verification_token against INSTITUTION_VERIFICATION_REGISTRY.
    Verify: institution domain verified, email-domain match, token valid.
    Institution must be a verified entity (per R35 — Institution Verification Law).
  failure: REJECT_TYPE_6 + LOG

STEP_10_PROOF_RECORD_GENERATION:
  action: >
    Assemble the final SKILL_PROOF_RECORD with:
    - Unique proof_id (UUID v4)
    - All verified evidence references (immutable links)
    - trust_tier classification
    - verification_confidence_score
    - rubric_version and model_version stamps
    - proof_type and domain_tag
    - issuer: SKILL_VERIFICATION_PROOF_AGENT v1.0.0
    - issued_at_utc: current timestamp
    - expiry_policy: see Section 7
    - employer_visibility_level: per proof_type definition
    - tenant_id + user_id (pseudonymised for storage)
    - audit_reference (UUID v4)
    Sign proof record with platform-level HMAC-SHA256 (tamper-evident)
  output: Sealed, signed SKILL_PROOF_RECORD → written to SKILL_PROOF_STORE

STEP_11_BADGE_ISSUANCE:
  action: >
    Emit BADGE_ISSUANCE_COMMAND to BADGE_REGISTRY.
    If composite conditions met (multiple proof types), compute composite badge.
    Badge includes: proof_id, trust_tier, skill_id, skill_version,
    domain_tag, issued_at_utc, rubric_version, verification_agent_version.
  badge_fraud_prevention: >
    Badge payload is signed. Any badge not verifiable against
    SKILL_PROOF_STORE proof_id = COUNTERFEIT — SECURITY_ALERT

STEP_12_DOWNSTREAM_PIPELINE_TRIGGERS:
  action_a: Emit CERTIFICATE_TRIGGER to CERTIFICATE_ENGINE (for GOLD and SILVER badges)
  action_b: Emit SKILL_VERIFIED_VECTOR to FEATURE_STORE_AGENT
  action_c: Emit XP_UPDATE_EVENT to GROWTH_ENGINE (confidence-weighted XP delta)
  action_d: Emit RANK_UPDATE_EVENT to RANK_UPDATE_AGENT
  action_e: Emit ROYALTY_ELIGIBILITY_SIGNAL to ROYALTY_ENGINE (TYPE_1 + TYPE_2 only)
  action_f: Emit TALENT_MARKETPLACE_UPDATE to TALENT_MARKETPLACE_AGENT
  action_g: Notify user via NOTIFICATION_AGENT

STEP_13_AUDIT_RECORD:
  action: Write immutable audit record to AUDIT_STORE (per Section 8)
  mandatory: TRUE — no verification may complete without an audit record
```

---

## 6️⃣ EVIDENCE FRESHNESS POLICY (LOCKED)

```yaml
FRESHNESS_RULES:
  # Evidence beyond these windows triggers re-verification
  TYPE_1_DOJO_BELT_SKILL:
    validity_window: 24 months
    re_verification_trigger: >
      Major rubric change OR mentor recertification cycle OR
      belt version MAJOR bump (per T17)

  TYPE_2_PROJECT_MILESTONE_SKILL:
    validity_window: 36 months
    re_verification_trigger: >
      Project rubric MAJOR version change

  TYPE_3_ASSESSMENT_SKILL:
    validity_window: 12 months
    re_verification_trigger: >
      Assessment model retrain with accuracy delta > 5%

  TYPE_4_LEARNING_PATH_COMPLETION:
    validity_window: 18 months
    re_verification_trigger: >
      Learning path major curriculum revision

  TYPE_5_EMPLOYER_ENDORSED_SKILL:
    validity_window: 60 months (5 years)
    re_verification_trigger: >
      Employer verification token expired or employer entity status changed

  TYPE_6_INSTITUTION_CERTIFIED_SKILL:
    validity_window: 60 months (5 years)
    re_verification_trigger: >
      Institution verification token expired or institution status changed

  TYPE_7_INTELLIGENCE_PROFILE_SKILL:
    validity_window: 12 months
    re_verification_trigger: >
      Intelligence model retrain or assessment rubric change

EXPIRY_ENFORCEMENT:
  action_on_expiry: >
    Badge status changed to EXPIRED in BADGE_REGISTRY.
    Re_VERIFICATION_TRIGGER_EVENT emitted to user and NOTIFICATION_AGENT.
    Expired badges NOT removed — historical record preserved.
    Expired badges NOT presentable to employers as current.
  auto_deletion: FORBIDDEN — all badge records are permanent
```

---

## 7️⃣ ML / AI LOGIC LAYER

### Verification Confidence Scorer (ML-Based — Primary)

```yaml
MODEL_TYPE:           Multi-Feature Weighted Regression
PURPOSE: >
  Computes VERIFICATION_CONFIDENCE_SCORE [0.0–1.0] for each
  proof request by integrating multiple evidence source quality signals.

FEATURES_USED:
  - ci_score_dojo_session            # From INTELLIGENCE_CONFIDENCE_INTERVAL_AGENT
  - ci_score_assessment              # CI score from assessment output
  - evidence_completeness_ratio      # Fraction of optional evidence present
  - evidence_source_count            # More independent sources = higher confidence
  - evidence_freshness_score         # Recency vs validity window
  - mentor_calibration_score         # Mentor's current calibration rating
  - inter_rater_reliability_score    # From Dojo scoring records
  - anomaly_clearance_score          # 1.0 = cleared, 0.0 = active anomaly
  - rubric_version_match_score       # 1.0 = current rubric, < 1.0 = legacy
  - employer_verification_weight     # Bonus when employer token present
  - institution_verification_weight  # Bonus when institution token present
  - peer_endorsement_count_normalized  # Normalized peer endorsement signal
  - intelligence_profile_alignment   # Career path alignment score (TYPE_7)

CONFIDENCE_BAND_THRESHOLDS:
  GOLD_ISSUABLE:        >= 0.85
  SILVER_ISSUABLE:      >= 0.70
  BRONZE_ISSUABLE:      >= 0.60
  INFORMATIONAL_ONLY:   >= 0.50
  REJECT_NO_BADGE:      < 0.50

TRAINING_FREQUENCY:   Monthly (1st of month, 02:00 UTC)
DRIFT_DETECTION:      PSI per feature, checked every 10,000 requests or 7 days
ROLLBACK_TRIGGER:     Badge precision drops > 2% vs prior version (employer feedback correlation)
VERSION_CONTROL:      MODEL_REGISTRY — immutable reference, semantic versioning
```

### Skill Gap Intelligence Engine (ML-Based — Secondary)

```yaml
MODEL_TYPE:           Multi-label Classification
PURPOSE: >
  When a proof request is REJECTED due to insufficient evidence,
  this model generates a personalised SKILL_GAP_REPORT identifying:
  - Which evidence components are missing
  - Which Dojo sessions, learning paths, or projects would
    fill the gap most efficiently for this user's profile
  - Estimated time to re-verification readiness

FEATURES_USED:
  - missing_evidence_types
  - user_current_skill_vector        # From FEATURE_STORE_AGENT
  - user_intelligence_profile        # From INTELLIGENCE_PROFILE_AGENT
  - domain_tag
  - tenant_available_scenarios       # From SCENARIO_REGISTRY (tenant-scoped)
  - user_learning_history

TRAINING_FREQUENCY:   Monthly
OUTPUT:               SKILL_GAP_REPORT JSON — emitted to LEARNING_PATH_AGENT
```

### AI-Assisted Layer (LLM-Based — Scoped)

```yaml
AI_USAGE_SCOPE: >
  Strictly limited to:
  1. Human-readable narrative generation for SKILL_GAP_REPORTs
  2. Employer-facing skill proof summary generation
     (structured, deterministic — no open-ended narrative)
  3. Dispute explanation generation for compliance review panels
  4. Re-verification guidance generation for users

  AI MUST NOT:
  - Override ML-computed verification confidence scores
  - Issue, modify, or revoke badges
  - Access unmasked PII or raw evidence files
  - Make eligibility decisions
  - Operate outside defined scope — any attempt = HALT + SECURITY_INCIDENT

PROMPT_GOVERNANCE:
  versioned: TRUE
  deterministic_structure: ENFORCED
  creative_interpretation: FORBIDDEN
  prompt_registry: PROMPT_REGISTRY_AGENT
  prompt_immutability: ADD-ONLY
  max_response_tokens: 800 — bounded output
```

---

## 8️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS:                2,000 – 50,000 proof requests/second at peak
LATENCY_TARGETS:
  simple_badge_verification:  p99 < 500ms (single evidence source, cached)
  full_multi_source_proof:    p99 < 3s (all evidence sources, full pipeline)
  certificate_generation:     async — p99 < 30s (certificate pipeline separate)
  employer_report_generation: async — p99 < 60s

MAX_CONCURRENCY:              50,000 simultaneous verification pipelines
QUEUE_STRATEGY:               >
  TYPE_5 (Employer Endorsed) and TYPE_6 (Institution Certified):
    Priority queue — employer-critical, fastest SLA
  TYPE_1 (Dojo Belt) and TYPE_2 (Project):
    Standard priority queue
  TYPE_3/4 (Assessment/Learning):
    Bulk queue — burst-tolerant
  Re-verification triggers:
    Low-priority background queue

HORIZONTAL_SCALING:           TRUE — stateless pods, Kubernetes HPA
STATELESS:                    TRUE — all state in SKILL_PROOF_STORE (PostgreSQL) + Redis cache
EVENT_DRIVEN:                 TRUE — Kafka consumer for incoming verification requests
ASYNC_PROCESSING:             TRUE — certificate and report generation fully async
IDEMPOTENT:                   TRUE — duplicate proof_request_id = idempotent response

CACHE_STRATEGY:
  skill_registry_entries:        TTL 3600s
  employer_verification_tokens:  TTL 300s (short — tokens must be current)
  institution_tokens:            TTL 300s
  mentor_calibration_scores:     TTL 1800s
  rubric_version_map:            TTL 3600s
  user_anomaly_status:           TTL 120s (short — must reflect latest ABDA state)
```

---

## 9️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  validation:                JWT claim match on every request
  cross_tenant_skill_proof:  FORBIDDEN — hard block at storage layer
  violation_action:          HALT + SECURITY_INCIDENT + ALERT_SECURITY_AGENT

BADGE_INTEGRITY:
  signing:                   Every SKILL_PROOF_RECORD HMAC-SHA256 signed at issuance
  verification_endpoint:     Public verification API returns proof authenticity status
  counterfeit_detection:     Badge_id not found in SKILL_PROOF_STORE = COUNTERFEIT_ALERT
  tamper_detection:          HMAC mismatch on badge payload = TAMPER_ALERT + SECURITY_INCIDENT

EVIDENCE_INTEGRITY:
  artifact_hash_verification: Every portfolio artifact hash verified against PORTFOLIO_AGENT
  token_authenticity:         Employer and institution tokens verified against registries
  cross_reference_check:      All evidence record IDs cross-referenced in source stores
  stale_evidence_prevention:  Validity windows enforced on every verification

EMPLOYER_DATA_SECURITY:
  employer_visibility_enforcement: >
    employer_visibility_level enforced per proof_type definition.
    TYPE_3/4/7 never visible to employers regardless of request.
    Employer access to TYPE_1 replays requires signed time-limited URL
    issued by DOJO_SESSION_ORCHESTRATION_AGENT, not directly by this agent.

PII_PROTECTION:
  user_id:                   Pseudonymised in all proof records and audit logs
  employer_token:            Masked in audit storage — reference only
  portfolio_artifacts:       Access via signed URL only — no direct storage in proof record

ENCRYPTION:
  proof_records_at_rest:     AES-256
  evidence_refs_in_transit:  TLS 1.3
  badge_payload:             HMAC-SHA256 signed
  certificate_files:         Encrypted at rest in MinIO + signed download URL

ACCESS_CONTROL:
  badge_issuance:            This agent only — no other service may write to BADGE_REGISTRY
  proof_record_mutation:     FORBIDDEN — append-only SKILL_PROOF_STORE
  employer_report_access:    Gated by MARKETPLACE_AGENT + employer verification
```

---

## 🔟 AUDIT & TRACEABILITY

Every verification execution — whether approved or rejected — produces an immutable audit record:

```json
{
  "timestamp_utc": "ISO 8601 UTC",
  "actor_id": "user_id (pseudonymised) + tenant_id composite",
  "proof_request_id": "UUID v4",
  "proof_id": "UUID v4 (null if rejected)",
  "execution_trace_id": "UUID v4 — per verification pipeline run",
  "skill_id": "UUID",
  "skill_version": "semantic version",
  "proof_type": "TYPE_1 through TYPE_7",
  "domain_tag": "Arts | Commerce | Science | Technology | Administration",
  "tenant_id": "masked reference",
  "input_hash": "SHA-256 of normalised evidence_package",
  "output_hash": "SHA-256 of normalised proof record (null if rejected)",
  "model_version": "SVPA_v1.0.0 + confidence_model_ref",
  "decision_path": {
    "step_1_validation": "PASS | FAIL",
    "step_2_completeness": "PASS | FAIL | GAP_DETAIL",
    "step_3_integrity": "PASS | FAIL",
    "step_4_anomaly_clearance": "CLEARED | BLOCKED | DEFERRED",
    "step_5_confidence_score": "float",
    "step_5_confidence_band": "GOLD | SILVER | BRONZE | INFORMATIONAL | REJECT",
    "step_6_rubric_version": "CURRENT | LEGACY_ACCEPTED | REJECTED",
    "step_7_mentor_cert": "VERIFIED | N_A | FAILED",
    "step_8_employer_token": "VERIFIED | N_A | FAILED",
    "step_9_institution_token": "VERIFIED | N_A | FAILED",
    "final_decision": "APPROVED | REJECTED | DEFERRED"
  },
  "confidence_score": "final VERIFICATION_CONFIDENCE_SCORE",
  "trust_tier_issued": "PLATINUM | GOLD | SILVER | BRONZE | INFORMATIONAL | NONE",
  "anomaly_flags": ["FRAUD_SIGNAL", "TAMPERED_ARTIFACT", "EXPIRED_TOKEN", "STALE_RUBRIC"],
  "badge_id": "UUID (null if rejected)",
  "audit_schema_version": "AUDIT_SCHEMA_v3",
  "re_verification_required": "boolean",
  "skill_gap_report_emitted": "boolean"
}
```

> **IMMUTABILITY RULE**: All audit records are written once. No UPDATE, DELETE, or PATCH permitted. Any attempt = SECURITY_INCIDENT.

---

## 1️⃣1️⃣ FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  INVALID_REQUEST:
    action: REJECT + EVIDENCE_GAP_REPORT
    log: LOG_VALIDATION_FAILURE with field-level detail
    escalate_to: COMPLIANCE_AGENT
    retry_policy: Re-submission permitted after evidence gap resolved

  INCOMPLETE_EVIDENCE_PACKAGE:
    action: REJECT + DETAILED_EVIDENCE_GAP_REPORT
    log: LOG_EVIDENCE_INCOMPLETE
    escalate_to: LEARNING_PATH_AGENT (for gap-filling recommendations)
    retry_policy: Re-submission after evidence collected

  EVIDENCE_INTEGRITY_FAILURE:
    action: REJECT + FRAUD_INVESTIGATION_FLAG
    log: LOG_INTEGRITY_FAILURE with evidence_ref detail
    escalate_to: ANOMALY_BEHAVIOR_DETECTION_AGENT + COMPLIANCE_AGENT
    retry_policy: NO_RETRY until COMPLIANCE_AGENT clears investigation

  ANOMALY_BLOCK:
    action: BLOCK_BADGE + DEFER_PROOF
    log: LOG_ANOMALY_BLOCK with anomaly_reference
    escalate_to: COMPLIANCE_AGENT
    retry_policy: Re-submission after anomaly resolved and cleared

  LOW_CONFIDENCE_SCORE (< 0.50):
    action: REJECT + SKILL_GAP_REPORT emitted
    log: LOG_LOW_CONFIDENCE with score detail
    escalate_to: LEARNING_PATH_AGENT
    retry_policy: Re-submission after additional evidence collected

  MODEL_UNAVAILABLE (confidence scorer):
    action: DEFER all requests — no badges issued in degraded mode
    log: LOG_MODEL_UNAVAILABLE
    escalate_to: MODEL_GOVERNANCE_AGENT + OBSERVABILITY_AGENT
    retry_policy: RETRY_3x with 2s backoff; after 3 failures → DEFER + queue for retry when model recovers
    note: Degraded mode must NEVER lower the evidence bar to compensate

  AI_TIMEOUT (narrative generation):
    action: Skip AI narrative — emit structured proof record without narrative
    note: AI timeout MUST NEVER block proof issuance or badge creation

  DATA_CORRUPTION (proof record write failure):
    action: HALT + ROLLBACK + QUARANTINE_REQUEST
    log: LOG_DATA_CORRUPTION
    escalate_to: DATA_INTEGRITY_AGENT + COMPLIANCE_AGENT
    retry_policy: NO_RETRY — manual forensic review required

  RUBRIC_VERSION_MISMATCH (MAJOR rubric change):
    action: REJECT_WITH_RE_VERIFICATION_REQUIRED
    log: LOG_RUBRIC_MISMATCH with version detail
    emit: RE_VERIFICATION_TRIGGER_EVENT to user via NOTIFICATION_AGENT
    retry_policy: Re-submission after user completes re-verification

SILENT_FAILURE_POLICY: ABSOLUTE ZERO TOLERANCE
  Every request — approved or rejected — must produce an audit record.
```

---

## 1️⃣2️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - DOJO_SESSION_ORCHESTRATION_AGENT
  - BELT_ENGINE
  - PROJECT_MILESTONE_EVALUATOR
  - LEARNING_PATH_AGENT
  - SKILL_RANKING_AGENT
  - INTELLIGENCE_PROFILE_AGENT
  - EMPLOYER_FEEDBACK_AGENT
  - INSTITUTION_VERIFICATION_AGENT
  - COMPANY_VERIFICATION_AGENT
  - REPUTATION_SCORE_AGENT
  - ANOMALY_BEHAVIOR_DETECTION_AGENT
  - INTELLIGENCE_CONFIDENCE_INTERVAL_AGENT
  - PORTFOLIO_AGENT
  - SCENARIO_REGISTRY

DOWNSTREAM_AGENTS:
  - BADGE_REGISTRY
  - CERTIFICATE_ENGINE
  - JOB_MATCH_ML_AGENT
  - LEARNING_PATH_AGENT
  - REPUTATION_SCORE_AGENT
  - GROWTH_ENGINE
  - ROYALTY_ENGINE
  - MARKETPLACE_AGENT
  - COMPLIANCE_AGENT
  - OBSERVABILITY_AGENT
  - FEATURE_STORE_AGENT
  - NOTIFICATION_AGENT
  - INTELLIGENCE_PROFILE_AGENT
  - TALENT_MARKETPLACE_AGENT

EVENT_TRIGGERS:
  EMITS:
    - SKILL_PROOF_ISSUED_EVENT
    - SKILL_PROOF_REJECTED_EVENT
    - BADGE_ISSUANCE_COMMAND
    - BADGE_EXPIRED_EVENT
    - CERTIFICATE_TRIGGER_EVENT
    - SKILL_GAP_REPORT_EVENT
    - RE_VERIFICATION_TRIGGER_EVENT
    - ROYALTY_ELIGIBILITY_SIGNAL_EVENT
    - TALENT_MARKETPLACE_UPDATE_EVENT
    - FRAUD_INVESTIGATION_FLAG_EVENT
    - XP_UPDATE_EVENT
    - RANK_UPDATE_EVENT
    - AUDIT_WRITTEN_EVENT

  CONSUMES:
    - DOJO_SESSION_CONCLUDED_EVENT
    - BELT_PROMOTION_EVENT
    - MILESTONE_COMPLETED_EVENT
    - LEARNING_PATH_COMPLETED_EVENT
    - ASSESSMENT_COMPLETED_EVENT
    - EMPLOYER_ENDORSEMENT_EVENT
    - INSTITUTION_CERTIFICATION_EVENT
    - PEER_ENDORSEMENT_EVENT
    - ANOMALY_CLEARED_EVENT
    - RUBRIC_VERSION_UPDATE_EVENT
    - SKILL_VERSION_UPDATE_EVENT
    - MODEL_VERSION_UPDATE_EVENT
```

---

## 1️⃣3️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```json
{
  "EMIT_FEATURE_VECTOR": {
    "user_id": "pseudonymised_user_token",
    "tenant_id": "tenant_scoped_id",
    "feature_name": "verified_skill_profile",
    "feature_value": "composite skill trust score [0.0 – 1.0]",
    "feature_sub_values": {
      "gold_badge_count": "integer",
      "silver_badge_count": "integer",
      "bronze_badge_count": "integer",
      "employer_endorsed_count": "integer",
      "institution_certified_count": "integer",
      "domain_skill_distribution": "object — per domain badge counts",
      "intelligence_aligned_skills": "array of intelligence types with verified skills",
      "most_recent_verification_utc": "ISO 8601",
      "verification_confidence_avg_30d": "float",
      "skill_gap_report_pending": "boolean"
    },
    "timestamp": "ISO 8601 UTC",
    "source_agent": "SKILL_VERIFICATION_PROOF_AGENT"
  }
}
```

> **Intelligence Economy Integration**: Verified skills are mapped to Howard Gardner intelligence types (Linguistic, Logical-Mathematical, Spatial, Bodily-Kinesthetic, Musical, Interpersonal, Intrapersonal, Naturalistic) and emitted as structured features. This enables the career path recommendation engine to match verified intelligence profiles with appropriate career tracks — not just marks or certificates, but real intelligence-validated skills.

---

## 1️⃣4️⃣ INNOVATION ECONOMY COMPATIBILITY

When this agent issues TYPE_1 or TYPE_2 skill proofs and the skill intersects with innovation or idea creation domains:

```json
{
  "ROYALTY_ELIGIBILITY_SIGNAL": {
    "user_id": "pseudonymised",
    "skill_proof_id": "UUID",
    "skill_id": "UUID",
    "trust_tier": "GOLD | SILVER",
    "proof_type": "TYPE_1 | TYPE_2",
    "domain_tag": "Arts | Commerce | Science | Technology | Administration",
    "verification_confidence": "float",
    "eligibility_for_royalty_economy": "boolean",
    "evidence_reference": "proof_id UUID"
  }
}
```

> **RULE**: Only TYPE_1 (GOLD) and TYPE_2 (SILVER) skill proofs may trigger ROYALTY_ENGINE eligibility. TYPE_3 through TYPE_7 do not qualify for royalty economy participation alone. This prevents unverified users from claiming innovation royalties on insufficient evidence.

Compatible with:
- `IDEA_DNA_AGENT` — verified skill vector enriches idea originality scoring
- `ROYALTY_ENGINE` — skill eligibility gate for royalty participation
- `COPY_DETECTION_ENGINE` — verified skill context for plagiarism analysis

---

## 1️⃣5️⃣ GROWTH ENGINE HOOK

```yaml
EMIT_ON_BADGE_ISSUANCE:
  XP_UPDATE_EVENT:
    conditions: "trust_tier IN [GOLD, SILVER, BRONZE] AND verification_confidence >= 0.60"
    payload:
      user_id: "pseudonymised"
      xp_delta: "tier-weighted XP — GOLD=highest, BRONZE=lowest"
      skill_id: "UUID"
      proof_id: "UUID (audit reference)"
      source_agent: "SKILL_VERIFICATION_PROOF_AGENT"

  RANK_UPDATE_EVENT:
    conditions: "trust_tier IN [GOLD, SILVER] AND verification_confidence >= 0.70"
    payload:
      user_id: "pseudonymised"
      skill_domain: "domain_tag"
      verification_confidence: "float"

  SHARE_TRIGGER_EVENT:
    conditions: >
      trust_tier = GOLD AND verification_confidence >= 0.90
      AND no active anomaly flags
    payload:
      achievement_type: "GOLD_SKILL_BADGE_EARNED"
      skill_name: "skill display name"
      user_id: "pseudonymised"

SUPPRESS_ALL_GROWTH_EVENTS:
  conditions:
    - verification outcome = REJECTED
    - any unresolved HIGH+ anomaly on user record
    - proof_type = TYPE_7 (intelligence informational — no XP)
    - badge status = EXPIRED

COMPOSITE_BADGE_BONUS:
  rule: >
    When composite PLATINUM badge achieved (TYPE_1 + TYPE_5),
    emit additional SHARE_TRIGGER_EVENT with achievement_type = PLATINUM_COMPOSITE_EARNED
    regardless of confidence score (PLATINUM already implies highest evidence standard)
```

---

## 1️⃣6️⃣ TALENT MARKETPLACE TRUST LOCK

```yaml
# Per Dojo Spec T14 — Talent Marketplace Trust Lock
EMPLOYER_FACING_PROOF_RULES:
  skill_proof_report_components:
    - badge_id + trust_tier
    - skill_id + skill_name + domain_tag
    - proof_type + evidence_summary (type-appropriate)
    - verification_confidence_score
    - issued_at_utc + expiry_status
    - rubric_version + assessment_authenticity_seal
    - replay_evidence_link (TYPE_1 only — signed time-limited URL, role-gated)

  employer_visibility_restrictions:
    TYPE_7_INTELLIGENCE_PROFILE: NEVER visible to employers
    EXPIRED_BADGES: marked EXPIRED — not presented as current
    REJECTED_PROOFS: NEVER visible — only issued proofs shown
    LOW_CONFIDENCE_BRONZE: shown with confidence_indicator = STANDARD

  assessment_authenticity_seal:
    definition: >
      Cryptographic seal on employer-facing report proving:
      evidence is real, unmodified, from verified Ecoskiller sources,
      issued by SKILL_VERIFICATION_PROOF_AGENT under declared version.
    format: HMAC-SHA256 of report payload + proof_id reference
    verification: Employer can verify seal via public verification API

  hiring_decision_data_rule:
    rule: >
      Hiring decisions facilitated by Ecoskiller platform MUST reference
      verified match data from this agent's issued proof records.
      Unverified or self-reported skill claims are NOT eligible for
      talent marketplace presentation.
    enforcement: MARKETPLACE_AGENT blocks listings without verified proof references
```

---

## 1️⃣7️⃣ ENTERPRISE INTEGRATION SUPPORT

```yaml
# Per Dojo Spec T18 — Enterprise Integration Lock
LMS_INTEGRATION:
  protocol: LTI 1.3 compatible output
  export_format: xAPI (Tin Can) compatible skill completion records
  endpoint: /api/v1/skill-proofs/{proof_id}/lti-export
  auth: OAuth 2.0 + enterprise SSO

HRIS_EXPORT:
  format: JSON + CSV
  fields: skill_name, trust_tier, issued_at, expiry_date, verification_confidence
  endpoint: /api/v1/skill-proofs/hris-export
  tenant_scoped: TRUE
  PII_handling: pseudonymised by default, de-anonymised only with explicit consent token

SKILL_REPORT_API:
  endpoint: /api/v1/skill-proofs/report
  auth: Enterprise API key + JWT
  rate_limit: 1000 requests/minute per enterprise tenant
  response_format: Signed JSON with assessment_authenticity_seal

AUDIT_EXPORT_CAPABILITY:
  scope: All proof issuance records for tenant
  format: Append-only JSON log export
  retention: 7 years minimum
  encryption: AES-256 export bundle + key delivered separately

SSO_ENTERPRISE_MODE:
  supported_providers: SAML 2.0, OAuth 2.0, OIDC
  role_mapping: Enterprise roles mapped to Ecoskiller RBAC via IDENTITY_AGENT
```

---

## 1️⃣8️⃣ SKILL_PROOF_STORE SCHEMA

```sql
-- Skill Proof Records — Append-Only
CREATE TABLE skill_proof_records (
  proof_id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  proof_request_id          UUID NOT NULL UNIQUE,
  tenant_id                 VARCHAR(128) NOT NULL,
  user_id_pseudonym         VARCHAR(256) NOT NULL,
  skill_id                  UUID NOT NULL,
  skill_version             VARCHAR(32) NOT NULL,
  domain_tag                VARCHAR(64) NOT NULL,
  proof_type                VARCHAR(32) NOT NULL,
  trust_tier                VARCHAR(16) NOT NULL,
  verification_confidence   NUMERIC(5,4) NOT NULL,
  rubric_version            VARCHAR(32) NOT NULL,
  model_version             VARCHAR(64) NOT NULL,
  evidence_refs             JSONB NOT NULL,     -- Immutable evidence reference package
  badge_id                  UUID,
  certificate_id            UUID,
  issued_at_utc             TIMESTAMPTZ NOT NULL DEFAULT now(),
  expiry_policy             JSONB NOT NULL,
  employer_visibility_level VARCHAR(32) NOT NULL,
  proof_signature           CHAR(64) NOT NULL,  -- HMAC-SHA256 tamper-evident seal
  audit_reference           UUID NOT NULL,
  status                    VARCHAR(16) NOT NULL DEFAULT 'ACTIVE'
                                CHECK (status IN ('ACTIVE','EXPIRED','SUSPENDED')),
  created_at                TIMESTAMPTZ NOT NULL DEFAULT now()
  -- MUTATION FORBIDDEN — append-only enforced at application layer
);

-- Verification Audit Log — Append-Only (includes rejections)
CREATE TABLE skill_verification_audit (
  audit_id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  execution_trace_id        UUID NOT NULL UNIQUE,
  proof_request_id          UUID NOT NULL,
  proof_id                  UUID,
  tenant_id                 VARCHAR(128) NOT NULL,
  user_id_pseudonym         VARCHAR(256) NOT NULL,
  skill_id                  UUID NOT NULL,
  proof_type                VARCHAR(32) NOT NULL,
  domain_tag                VARCHAR(64) NOT NULL,
  decision                  VARCHAR(16) NOT NULL CHECK (decision IN ('APPROVED','REJECTED','DEFERRED')),
  confidence_score          NUMERIC(5,4),
  trust_tier_issued         VARCHAR(16),
  decision_path             JSONB NOT NULL,
  anomaly_flags             JSONB,
  re_verification_required  BOOLEAN NOT NULL DEFAULT FALSE,
  skill_gap_report_emitted  BOOLEAN NOT NULL DEFAULT FALSE,
  model_version             VARCHAR(64) NOT NULL,
  input_hash                CHAR(64) NOT NULL,
  output_hash               CHAR(64),
  created_at                TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_proofs_user ON skill_proof_records (user_id_pseudonym, tenant_id, status);
CREATE INDEX idx_proofs_skill ON skill_proof_records (skill_id, tenant_id, domain_tag);
CREATE INDEX idx_proofs_type ON skill_proof_records (proof_type, trust_tier, tenant_id);
CREATE INDEX idx_proofs_expiry ON skill_proof_records (issued_at_utc, tenant_id)
  WHERE status = 'ACTIVE';
CREATE INDEX idx_audit_user ON skill_verification_audit (user_id_pseudonym, tenant_id, created_at);
CREATE INDEX idx_audit_decision ON skill_verification_audit (decision, domain_tag, created_at);
```

---

## 1️⃣9️⃣ PERFORMANCE MONITORING

```yaml
METRICS:
  proof_issuance_success_rate:
    target: "> 99.5% of valid requests successfully processed"
    alert_threshold: "< 99.0%"

  evidence_rejection_rate:
    definition: "% of requests rejected due to evidence gaps"
    informational_target: "< 30% (high rejection = platform onboarding issue)"
    alert_threshold: "> 50% in any 24-hour window — curriculum review needed"

  verification_confidence_distribution:
    healthy_range:
      GOLD_ISSUED:         "> 30% of TYPE_1 requests"
      SILVER_ISSUED:       "> 40% of TYPE_2 requests"
      REJECTION_RATE:      "< 20% across all types"
    alert: "REJECTION > 40% for any proof_type = curriculum gap signal"

  latency:
    simple_verification_p99: "< 500ms"
    full_pipeline_p99:        "< 3s"
    alert_threshold:          "p99 > 5s"

  badge_fraud_detection_rate:
    definition: "Counterfeit badge verification attempts detected per day"
    alert_threshold: "> 10 per day per tenant"

  skill_gap_report_utilization:
    definition: "% of rejected users who re-submit within 30 days"
    target:     "> 60% (indicates gap reports are actionable)"
    alert:      "< 30% — report quality review needed"

  model_drift_indicator:
    definition: "PSI per feature per month"
    alert_threshold: "PSI > 0.25 for any feature"

OBSERVABILITY_INTEGRATION:
  agent: OBSERVABILITY_AGENT
  dashboards:
    - SKILL_VERIFICATION_TRUST_DASHBOARD
    - BADGE_ISSUANCE_ANALYTICS_DASHBOARD
    - EVIDENCE_GAP_ANALYSIS_DASHBOARD
  prometheus_metrics: TRUE
  grafana_alerts: TRUE
```

---

## 2️⃣0️⃣ VERSIONING POLICY

```yaml
VERSION_FORMAT:          Semantic Versioning — MAJOR.MINOR.PATCH
CURRENT_VERSION:         v1.0.0
MUTATION_POLICY:         ADD-ONLY

CHANGE_RULES:
  MAJOR_bump: >
    New proof types, changes to confidence thresholds, changes to
    trust tier definitions, changes to employer visibility rules,
    changes to evidence required fields — all require MAJOR bump.
    All existing proofs under prior MAJOR version remain valid
    per their issued trust_tier; re-verification required for new MAJOR claims.
  MINOR_bump: >
    New optional evidence fields, new downstream integrations,
    new enterprise export formats — non-breaking.
  PATCH_bump: >
    Bug fixes, threshold adjustments, documentation corrections.

BACKWARD_COMPATIBILITY:  MANDATORY for MINOR and PATCH
MIGRATION_REQUIRED:      MAJOR only — migration plan + backward compatibility window declared

PROOF_RECORD_VERSIONING:
  model_version: Stamped on every proof record at issuance — immutable
  rubric_version: Stamped on every proof record — defines which rubric evidence was evaluated under
  re_verification_trigger: MAJOR model bump or MAJOR rubric bump triggers re-verification cycle

BADGE_VERSIONING:
  rule: Badges carry rubric_version and model_version stamps
  stale_badge_handling: Per Section 6 (Evidence Freshness Policy)
  historical_record: All issued badges permanently preserved — never deleted
```

---

## 2️⃣1️⃣ NON-NEGOTIABLE RULES

```
THIS AGENT MUST NOT:
  ❌  Issue a badge or proof record without a complete, verified evidence package
  ❌  Issue a badge when session_integrity_status != CLEAN for TYPE_1 proofs
  ❌  Issue a badge when an unresolved CRITICAL or HIGH anomaly exists for the user
  ❌  Allow AI layer to override ML-computed verification confidence scores
  ❌  Issue TYPE_5 or TYPE_6 badges without validated employer/institution tokens
  ❌  Present TYPE_7 intelligence profile badges to employers
  ❌  Show expired badges as current to any downstream consumer
  ❌  Delete, modify, or truncate any proof record or audit record
  ❌  Cross tenant boundaries in proof creation, evidence lookup, or badge issuance
  ❌  Allow self-reported skill claims without evidence package validation
  ❌  Mix domain-tagged skill evidence across domain boundaries
  ❌  Issue royalty eligibility signals for TYPE_3 through TYPE_7 proofs alone
  ❌  Allow badge issuance by any agent or service other than this agent
  ❌  Process requests from unregistered agents or services
  ❌  Operate in degraded mode without explicitly flagging all deferred proofs
  ❌  Issue badges under superseded MAJOR rubric versions without explicit approval
  ❌  Accept employer or institution tokens that are expired or unverifiable
  ❌  Create hidden verification pathways not declared in this specification
  ❌  Fail silently — every failure must produce a logged, traceable incident
```

---

## 2️⃣2️⃣ DOCUMENT SEAL

```
═══════════════════════════════════════════════════════════════════════════════════════
  DOCUMENT SEAL — ECOSKILLER ANTIGRAVITY GOVERNANCE
═══════════════════════════════════════════════════════════════════════════════════════
  Agent Name:        SKILL_VERIFICATION_PROOF_AGENT
  Agent ID:          ECSK-AGT-GOVERN-0044
  Layer:             Governance & Core Control
  Platform:          Ecoskiller Antigravity
  Document Version:  v1.0.0
  Status:            SEALED · LOCKED · GOVERNED · DETERMINISTIC
  Mutation Policy:   ADD-ONLY via version bump
  Authority:         Human Declaration Only
  Sealed:            2026-02-28T00:00:00Z

  THIS DOCUMENT IS SEALED.
  No field, evidence rule, verification pathway, confidence threshold,
  badge type, or downstream trigger may be modified, reinterpreted,
  extended, or overridden without a formally declared versioned amendment
  following ADD-ONLY policy. All prior versions remain permanently immutable.

  ARCHITECTURE INTERPRETATION: FORBIDDEN
  ASSUMPTION FILLING: FORBIDDEN
  IMPLICIT BEHAVIOR: FORBIDDEN
  CREATIVE INTERPRETATION: FORBIDDEN

  VIOLATION OF THIS SEAL =
  GOVERNANCE_FAILURE + TRUST_COLLAPSE_RISK +
  IMMEDIATE ESCALATION TO SECURITY_AGENT + COMPLIANCE_AGENT
═══════════════════════════════════════════════════════════════════════════════════════
```

---

*End of SKILL_VERIFICATION_PROOF_AGENT.md — v1.0.0 — SEALED*
