# 🔒 REPUTATION_SCORE_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED AGENT SPECIFICATION
### Artifact Class: Production Agent Blueprint
### Mutation Policy: ADD-ONLY via version bump
### Execution Mode: DETERMINISTIC + VALIDATED
### Creative Interpretation: FORBIDDEN
### Assumption Filling: FORBIDDEN
### Default Behavior: DENY
### Failure Mode: STOP_EXECUTION
### Governance Class: Core Trust Infrastructure

---

> ⚠️ SEAL NOTICE: This document is a locked production artifact for the Ecoskiller Antigravity SaaS platform. No section may be altered, reinterpreted, or partially implemented. All changes require a formal version bump with full audit trail. Any implementation that deviates from this specification is an INVALID BUILD and must STOP EXECUTION immediately.

---

## 1️⃣ AGENT IDENTITY (MANDATORY — LOCKED)

```yaml
AGENT_NAME:               REPUTATION_SCORE_AGENT
AGENT_ID:                 RSA-003
VERSION:                  v1.0.0
SYSTEM_ROLE:              Multi-Dimensional Trust & Reputation Intelligence Engine
PRIMARY_DOMAIN:           User Reputation | Recruiter Trust | Institute Credibility |
                          SME Reliability | Mentor Authority | Platform-Wide Trust Propagation
EXECUTION_MODE:           Deterministic + Validated
DATA_SCOPE:               Per-User | Per-Recruiter | Per-Institute | Per-SME | Per-Tenant |
                          Per-Domain Track | Cross-Signal Aggregation (Within Tenant Only)
TENANT_SCOPE:             Strict Isolation — Zero Cross-Tenant Score Leakage
FAILURE_POLICY:           HALT on ambiguity — No silent failure — No partial score emission
CREATIVE_INTERPRETATION:  FORBIDDEN
ASSUMPTION_FILLING:       FORBIDDEN
MUTATION_POLICY:          Add-only | Versioned | Backward-compatible
SECURITY_MODEL:           Zero-Trust Multi-Tenant
DATA_POLICY:              Append-Only Audit Trail — Score history never deleted
PLATFORM:                 Ecoskiller Antigravity
ARCHITECTURE:             Microservices + Event-Driven
SCALE_TARGET:             10M–100M users
COMPLIANCE_MODE:          ENABLED
SCORE_OVERRIDE_BY_AI:     FORBIDDEN
SCORE_OVERRIDE_AUTONOMY:  NONE — Human review required for all manual overrides
```

---

## 2️⃣ PURPOSE DECLARATION

### The Problem This Agent Solves

Ecoskiller Antigravity operates across five radically different actor classes — Students, Recruiters, Institutes, SMEs, and Mentors — each of whom must earn and maintain **trust** on the platform in order to participate in high-stakes flows: job offers, certifications, hiring decisions, mentor authority, and platform access.

Without a centralized, authoritative, multi-dimensional reputation engine, the following critical failures occur:

- **Recruiters** with zero verified track record get equal visibility with trusted verified employers — exposing students to fraudulent or unreliable job offers
- **Students** with fabricated or inflated credentials cannot be distinguished from genuinely skilled candidates by the job match system
- **Institutes** that provide low-quality training or misrepresent outcomes cannot be downgraded in recommendation ranking
- **SMEs** that repeatedly fail to honor hiring commitments are not penalized — creating dangerous patterns in the talent marketplace
- **Mentors** whose students consistently fail to progress cannot be identified and decertified
- **The platform's anti-collusion and anti-abuse systems** have no trust-weighted signal to modulate content visibility, feature access, and dispute resolution priority
- **Recruiters, students, and institutions** have no verifiable signal of platform-wide credibility to present to external stakeholders

The **REPUTATION_SCORE_AGENT** is the single authoritative engine that computes, maintains, decays, publishes, and governs **Reputation Score Objects (RSO)** for every actor on the platform — across multiple dimensions, with full audit trails, drift detection, fraud resistance, and downstream publishing to every system that depends on trust signals.

This agent does NOT determine outcomes. It computes trust signals. Downstream agents use these signals in their own decision logic. The distinction is mandatory and non-negotiable.

### Core Responsibilities (All Mandatory)

1. **Consume** structured events from all contributing upstream agents
2. **Compute** multi-dimensional Reputation Score Objects (RSO) per actor per trigger
3. **Apply temporal decay** to all score dimensions — scores degrade without sustained activity
4. **Detect and neutralize** inflated scores from colluded, farmed, or manipulated signals
5. **Publish** RSOs to all authorized downstream consumers via Kafka events and query API
6. **Enforce score floors and ceilings** per actor class and domain track
7. **Maintain immutable score history** — every RSO version is archived permanently
8. **Emit growth engine hooks** on significant reputation milestones
9. **Expose explainability metadata** — every score must be traceable to its contributing signals
10. **Govern shadow-limiting** — low-reputation actors receive reduced platform visibility without explicit notification (except for critical score floor breaches which are disclosed)

### Downstream Agents That Depend on This Agent

```yaml
DOWNSTREAM_CONSUMERS:
  - JOB_MATCH_AGENT              → uses student + recruiter RSO for eligibility and match weighting
  - RECRUITER_DASHBOARD_AGENT    → displays recruiter trust tier and reliability score to candidates
  - RANK_ENGINE_AGENT            → uses RSO signals for leaderboard and search ranking position
  - CONTENT_VISIBILITY_AGENT     → uses RSO for feed ranking and shadow-limiting decisions
  - COMPLIANCE_AUDIT_AGENT       → uses RSO for governance review prioritization
  - DISPUTE_RESOLUTION_AGENT     → uses RSO to weight credibility of disputing parties
  - APPEALS_GOVERNANCE_AGENT     → reads RSO history for belt and certification appeal context
  - ANTI_COLLUSION_AGENT         → receives REPUTATION_FRAUD_SIGNAL events for investigation
  - FEATURE_STORE_AGENT          → receives RSO-derived temporal feature vectors
  - NOTIFICATION_AGENT           → receives milestone events and score threshold breach alerts
  - PUBLIC_PROFILE_AGENT         → renders trust tier and reputation badge on public profile (React/SEO)
  - PARENT_VISIBILITY_AGENT      → reads student RSO in trust-stripped summary format
  - PASSPORT_AGGREGATION_AGENT   → reads RSO trust tier and fraud flags for UPO trust_tier field
  - OBSERVABILITY_AGENT          → receives all performance and anomaly metrics
  - ADMIN_GOVERNANCE_AGENT       → reads RSO for moderation prioritization and shadow-limit status
  - CERTIFICATION_AGENT          → uses mentor RSO to validate mentor authority before cert award
  - BILLING_SERVICE              → uses recruiter/institute RSO for premium feature eligibility gating
```

### Upstream Agents That Feed This Agent

```yaml
UPSTREAM_PRODUCERS:
  - PASSPORT_AGGREGATION_AGENT   → PASSPORT_UPDATED events (skill, belt, certification signals)
  - MATCH_SCORING_AGENT          → MATCH_COMPLETED, MATCH_FORFEITED events (Dojo performance)
  - MENTOR_FEEDBACK_AGENT        → MENTOR_EVALUATION_SUBMITTED events (peer + mentor scoring)
  - EVALUATOR_AGENT              → EVALUATOR_SCORE_SUBMITTED events
  - JOB_APPLICATION_AGENT        → JOB_OFFER_ACCEPTED, JOB_OFFER_DECLINED, JOB_OFFER_RETRACTED,
                                   JOB_APPLICATION_GHOSTED events
  - PROJECT_MILESTONE_AGENT      → PROJECT_MILESTONE_PASSED, PROJECT_MILESTONE_FAILED events
  - CERTIFICATION_AGENT          → CERTIFICATION_ISSUED, CERTIFICATION_REVOKED events
  - GAMIFICATION_AGENT           → XP_UPDATED, BADGE_EARNED, STREAK_UPDATED events
  - IDENTITY_VERIFICATION_AGENT  → IDENTITY_VERIFIED, IDENTITY_REVOKED events
  - ANTI_COLLUSION_AGENT         → COLLUSION_FLAG_RAISED, COLLUSION_FLAG_CLEARED events
  - CONTENT_MODERATION_AGENT     → CONTENT_VIOLATION_CONFIRMED, CONTENT_APPEAL_UPHELD events
  - DISPUTE_RESOLUTION_AGENT     → DISPUTE_RESOLVED_IN_FAVOR, DISPUTE_RESOLVED_AGAINST events
  - RECRUITER_VERIFICATION_AGENT → RECRUITER_VERIFIED, RECRUITER_SUSPENDED events
  - INSTITUTE_VERIFICATION_AGENT → INSTITUTE_VERIFIED, INSTITUTE_SUSPENDED events
  - PEER_RATING_AGENT            → PEER_RATING_SUBMITTED events (post-match, post-interview)
  - EMPLOYER_FEEDBACK_AGENT      → EMPLOYER_FEEDBACK_SUBMITTED events (post-hire)
  - MENTOR_CALIBRATION_AGENT     → MENTOR_CALIBRATION_PASSED, MENTOR_CALIBRATION_FAILED events
  - SESSION_SERVICE              → LOGIN_STREAK_UPDATED, INACTIVITY_THRESHOLD_REACHED events
  - ADMIN_GOVERNANCE_AGENT       → MANUAL_REPUTATION_OVERRIDE_AUTHORIZED events (human-approved only)
```

---

## 3️⃣ ACTOR CLASSES & SCORE DIMENSIONS (LOCKED)

The RSA operates across five distinct actor classes. Each has a unique dimension set. Score logic must NOT be shared or merged across actor classes.

### 3A. STUDENT Reputation Score Dimensions

```yaml
STUDENT_SCORE_DIMENSIONS:

  SKILL_CREDIBILITY_SCORE:
    Weight:           22%
    Sources:          Completed skills count, skill depth index, industry alignment score,
                      certification-to-skill ratio, skill_gap_flags cleared
    Decay:            YES — 5% per 30 days of no new skill activity
    Floor:            0.0
    Ceiling:          1.0

  DOJO_PERFORMANCE_SCORE:
    Weight:           20%
    Sources:          Win rate, avg match score, pressure scenarios passed, total matches,
                      opponent quality rating, replay analysis clean flag
    Decay:            YES — 3% per 30 days of no Dojo activity
    Floor:            0.0
    Ceiling:          1.0
    Fraud_Guard:      Scores from flagged/collusion-suspected matches excluded

  BELT_AUTHORITY_SCORE:
    Weight:           18%
    Sources:          Current belt tier numeric, time-held at current belt, mentor certification
                      quality score, belt progression velocity (penalize suspicious speed)
    Decay:            YES — 2% per 60 days of no belt progression activity
    Floor:            0.0
    Ceiling:          1.0

  PROJECT_EXECUTION_SCORE:
    Weight:           15%
    Sources:          Milestones passed ratio (passed/total), evidence verified count,
                      mentor evaluation composite, portfolio generated flag,
                      project completion rate
    Decay:            YES — 4% per 45 days of no project activity
    Floor:            0.0
    Ceiling:          1.0

  PEER_TRUST_SCORE:
    Weight:           10%
    Sources:          Peer ratings received (post-match, post-collaboration),
                      kudos/GG received count, report_credibility_weight of raters,
                      false_report_penalty_flags
    Decay:            YES — 3% per 30 days of no peer interaction
    Floor:            0.0
    Ceiling:          1.0
    Fraud_Guard:      Peer ratings from suspicious/colluded accounts discounted

  BEHAVIORAL_INTEGRITY_SCORE:
    Weight:           10%
    Sources:          Content violations (negative), dispute outcomes won/lost,
                      collusion flags (heavily negative), inactivity periods,
                      false report submissions, account age
    Decay:            NO — violations persist; only cleared by appeal or time-bound rehabilitation
    Floor:            0.0
    Ceiling:          1.0

  IDENTITY_VERIFICATION_SCORE:
    Weight:           5%
    Sources:          KYC verified, institute verified, phone verified, email verified,
                      employer verified (if applicable), verification freshness
    Decay:            YES — identity verifications expire per policy TTL
    Floor:            0.0
    Ceiling:          1.0

STUDENT_COMPOSITE_REPUTATION_SCORE:
    Formula:          Weighted sum of all 7 dimensions
    Range:            0.0 – 1.0
    Precision:        4 decimal places
    Recompute_Trigger: Any contributing event received
    Publish_Trigger:   Score change > 0.005 OR score crosses a tier boundary
```

### 3B. RECRUITER Reliability Score Dimensions

```yaml
RECRUITER_SCORE_DIMENSIONS:

  OFFER_INTEGRITY_SCORE:
    Weight:           30%
    Sources:          Offers made vs retracted ratio, ghosting rate (applications never responded to),
                      offer acceptance rate, time-to-response on applications,
                      job posting accuracy vs actual role delivered
    Decay:            YES — 5% per 60 days of no hiring activity
    Floor:            0.0
    Ceiling:          1.0
    Fraud_Guard:      Self-referral offer loops detected and zeroed

  CANDIDATE_FEEDBACK_SCORE:
    Weight:           25%
    Sources:          Post-interview satisfaction ratings from candidates (1-5 stars),
                      interview experience complaint rate, interview completion rate,
                      interviewer punctuality signals
    Decay:            YES — 4% per 45 days
    Floor:            0.0
    Ceiling:          1.0

  JOB_POSTING_QUALITY_SCORE:
    Weight:           20%
    Sources:          Job description completeness, salary transparency flag,
                      posted vs filled ratio, duplicate posting penalty,
                      moderation violation count on postings
    Decay:            YES — 3% per 30 days of no posting activity
    Floor:            0.0
    Ceiling:          1.0

  IDENTITY_AND_COMPANY_SCORE:
    Weight:           15%
    Sources:          Company domain verified, recruiter KYC verified,
                      company registration verified, company tenure signals
    Decay:            YES — verifications expire per TTL policy
    Floor:            0.0
    Ceiling:          1.0

  PLATFORM_CONDUCT_SCORE:
    Weight:           10%
    Sources:          Content violations on postings/messages, dispute outcomes,
                      collusion or fake job flag history, abuse reports confirmed
    Decay:            NO — violations persist; cleared only by appeal or rehabilitation
    Floor:            0.0
    Ceiling:          1.0

RECRUITER_COMPOSITE_RELIABILITY_SCORE:
    Formula:          Weighted sum of all 5 dimensions
    Range:            0.0 – 1.0
    SME_PENALTY:      Additional 0.1 deduction applied for SMEs with < 12 months verified
                      hiring history (SME reliability risk penalty — per platform rules)
    Publish_Trigger:  Score change > 0.01 OR tier boundary crossed
```

### 3C. INSTITUTE Credibility Score Dimensions

```yaml
INSTITUTE_SCORE_DIMENSIONS:

  STUDENT_OUTCOME_SCORE:
    Weight:           35%
    Sources:          Job placement rate of enrolled students, student belt progression rate,
                      certification pass rate within institute cohort,
                      employer feedback on institute graduates
    Decay:            YES — 5% per 90 days of no outcome data
    Floor:            0.0
    Ceiling:          1.0

  CURRICULUM_QUALITY_SCORE:
    Weight:           25%
    Sources:          Skill-to-industry alignment of offered tracks, curriculum version
                      freshness (stale curriculum penalized), trainer quality scores,
                      course completion rates
    Decay:            YES — 4% per 60 days
    Floor:            0.0
    Ceiling:          1.0

  MENTOR_POOL_QUALITY_SCORE:
    Weight:           20%
    Sources:          Average mentor RSO of institute's mentor pool,
                      mentor calibration pass rate, certified mentor ratio
    Decay:            YES — 3% per 60 days
    Floor:            0.0
    Ceiling:          1.0

  VERIFICATION_AND_COMPLIANCE_SCORE:
    Weight:           15%
    Sources:          Institute domain verified, registration verified, admin KYC verified,
                      compliance audit clean flag, data protection policy accepted
    Decay:            YES — verifications expire
    Floor:            0.0
    Ceiling:          1.0

  PLATFORM_CONDUCT_SCORE:
    Weight:           5%
    Sources:          Content violations, dispute outcomes, abuse flags confirmed
    Decay:            NO — violations persist
    Floor:            0.0
    Ceiling:          1.0

INSTITUTE_COMPOSITE_CREDIBILITY_SCORE:
    Formula:          Weighted sum of all 5 dimensions
    Range:            0.0 – 1.0
    Publish_Trigger:  Score change > 0.015 OR tier boundary crossed
```

### 3D. SME (Small/Medium Enterprise) Reliability Score Dimensions

```yaml
SME_SCORE_DIMENSIONS:

  HIRING_FOLLOW_THROUGH_SCORE:
    Weight:           35%
    Sources:          Commitments made (job posted, interview scheduled) vs completed,
                      offer retraction rate, no-show interview rate, ghost-candidate rate,
                      time-to-hire vs posted timeline
    Decay:            YES — 5% per 60 days
    Floor:            0.0
    Ceiling:          1.0
    NOTE:             This is the highest-weighted dimension for SMEs due to
                      historically high commitment failure rate in SME segment

  PAYMENT_AND_CONTRACT_SCORE:
    Weight:           25%
    Sources:          Dispute resolution outcomes related to payment,
                      invoice honor rate (platform-mediated projects),
                      contract fulfillment signals from project completion
    Decay:            YES — 4% per 90 days
    Floor:            0.0
    Ceiling:          1.0

  CANDIDATE_EXPERIENCE_SCORE:
    Weight:           20%
    Sources:          Post-interview satisfaction ratings, interview no-show rate,
                      feedback response rate to candidates
    Decay:            YES — 4% per 45 days
    Floor:            0.0
    Ceiling:          1.0

  VERIFICATION_SCORE:
    Weight:           15%
    Sources:          Company domain verified, GST/registration verified, admin KYC verified
    Decay:            YES — verifications expire
    Floor:            0.0
    Ceiling:          1.0

  PLATFORM_CONDUCT_SCORE:
    Weight:           5%
    Sources:          Content violations, fake job flag confirmed, abuse reports
    Decay:            NO
    Floor:            0.0
    Ceiling:          1.0

SME_COMPOSITE_RELIABILITY_SCORE:
    Formula:          Weighted sum of all 5 dimensions
    PROBATION_PENALTY: SMEs in first 12 months receive a 0.15 score ceiling reduction
                       (SME_PROBATION_MODE = ACTIVE for first year)
    Range:            0.0 – 1.0
    Publish_Trigger:  Score change > 0.01 OR tier boundary crossed
```

### 3E. MENTOR Authority Score Dimensions

```yaml
MENTOR_SCORE_DIMENSIONS:

  STUDENT_PROGRESSION_SCORE:
    Weight:           30%
    Sources:          Belt promotion rate of mentored students, student win rate improvement
                      over mentorship period, student certification pass rate,
                      student score improvement trajectory
    Decay:            YES — 5% per 60 days of no mentoring activity
    Floor:            0.0
    Ceiling:          1.0

  CALIBRATION_AUTHORITY_SCORE:
    Weight:           25%
    Sources:          Mentor calibration pass/fail history, score variance vs gold standard,
                      calibration frequency, re-certification compliance
    Decay:            YES — 6% per 30 days (calibration currency degrades faster)
    Floor:            0.0
    Ceiling:          1.0
    CRITICAL:         If MENTOR_CALIBRATION_FAILED event received → immediate 0.3 deduction
                      If calibration tolerance exceeded → auto-flag to CERTIFICATION_AGENT

  EVALUATION_QUALITY_SCORE:
    Weight:           20%
    Sources:          Inter-rater reliability score (vs peer evaluators),
                      evaluation completion rate, variance anomaly count,
                      favoritism detection flags
    Decay:            YES — 4% per 45 days
    Floor:            0.0
    Ceiling:          1.0

  BEHAVIORAL_CONDUCT_SCORE:
    Weight:           15%
    Sources:          Mentor misconduct reports confirmed, student complaint rate,
                      dispute outcomes against mentor, override ethics violations
    Decay:            NO — conduct violations persist
    Floor:            0.0
    Ceiling:          1.0

  IDENTITY_AND_CERTIFICATION_SCORE:
    Weight:           10%
    Sources:          Mentor KYC verified, mentor certification active, mentor domain verified,
                      mentor re-certification compliance, tenure on platform
    Decay:            YES — certifications expire
    Floor:            0.0
    Ceiling:          1.0

MENTOR_COMPOSITE_AUTHORITY_SCORE:
    Formula:          Weighted sum of all 5 dimensions
    Range:            0.0 – 1.0
    CRITICAL_FLOOR:   If MENTOR_COMPOSITE drops below 0.45 → emit MENTOR_AUTHORITY_CRITICAL_ALERT
                      to CERTIFICATION_AGENT immediately — no delay
    Publish_Trigger:  Score change > 0.01 OR tier boundary crossed OR CRITICAL_FLOOR breach
```

---

## 4️⃣ REPUTATION TIERS (LOCKED — ALL ACTOR CLASSES)

```yaml
REPUTATION_TIERS:
  ELITE:          score >= 0.90  | Label: "Elite"    | Indicator: Gold badge
  VERIFIED:       score >= 0.75  | Label: "Verified" | Indicator: Green badge
  INTERMEDIATE:   score >= 0.55  | Label: "Growing"  | Indicator: Blue badge
  BASIC:          score >= 0.35  | Label: "New"      | Indicator: Grey badge
  PROBATION:      score >= 0.20  | Label: "Limited"  | Indicator: Yellow badge
  RESTRICTED:     score <  0.20  | Label: "Restricted"| Indicator: Red badge — feature access limited

TIER_CHANGE_RULES:
  - Tier upgrades: Immediate on score crossing threshold
  - Tier downgrades: Immediate on score crossing threshold (no grace period)
  - RESTRICTED tier: Triggers SHADOW_LIMITING and feature gating (see Section 8)
  - PROBATION tier: Triggers reduced feature access but no shadow-limiting
  - Tier changes always emit REPUTATION_TIER_CHANGED event to all downstream consumers
  - Tier changes trigger NOTIFICATION_AGENT for user-facing alerts
```

---

## 5️⃣ INPUT CONTRACT (STRICT — LOCKED)

All inputs arrive as structured Kafka events. No direct DB reads from other services. No REST polling.

```json
INPUT_SCHEMA: {
  "required_fields": {
    "event_id":           "UUID v4",
    "event_type":         "string — must be in ACCEPTED_EVENT_TYPES list",
    "source_agent":       "string — must be in REGISTERED_UPSTREAM_AGENTS list",
    "actor_id":           "UUID — the user/recruiter/institute/SME/mentor being scored",
    "actor_class":        "STUDENT | RECRUITER | INSTITUTE | SME | MENTOR",
    "tenant_id":          "string — actor's registered tenant",
    "domain_track":       "Arts | Commerce | Science | Technology | Administration (where applicable)",
    "timestamp_utc":      "ISO 8601 UTC",
    "payload":            "object — event-specific payload (structure varies per event_type)",
    "payload_version":    "semver",
    "input_hash":         "SHA-256 of canonical payload JSON"
  },
  "optional_fields": {
    "session_id":         "string",
    "match_id":           "UUID",
    "job_id":             "UUID",
    "project_id":         "UUID",
    "related_actor_id":   "UUID — counterparty in dyadic events (e.g., peer who rated)",
    "confidence_score":   "float 0.0–1.0 — confidence of the upstream event signal",
    "fraud_signal":       "boolean — upstream agent flagged this event as potentially manipulated",
    "override_authority": "UUID — required for MANUAL_REPUTATION_OVERRIDE_AUTHORIZED events only"
  },
  "validation_rules": [
    "event_id must be UUID v4 and NOT previously processed (dedup check)",
    "event_type must be in ACCEPTED_EVENT_TYPES — reject all unknown event types",
    "source_agent must be in REGISTERED_UPSTREAM_AGENTS — reject all unregistered sources",
    "actor_id must exist in User/Recruiter/Institute/SME/Mentor registry",
    "actor_class must match actor_id's registered class — cross-class events rejected",
    "tenant_id must match actor's registered tenant",
    "domain_track must be one of 5 locked domain values (where applicable to actor_class)",
    "timestamp_utc must be ISO 8601 UTC and not older than STALENESS_THRESHOLD (48h)",
    "input_hash must equal SHA-256 of canonical payload JSON",
    "If fraud_signal = true → confidence_score must be present and <= 0.5"
  ],
  "security_checks": [
    "Reject events from unregistered source agents",
    "Reject cross-tenant events (actor_id tenant mismatch)",
    "Reject cross-class events (actor_class mismatch with registered class)",
    "Verify JWT claim of emitting service",
    "Reject events with timestamp > 48h staleness threshold",
    "If override_authority present → verify against ADMIN_APPROVED_OVERRIDE_REGISTRY"
  ],
  "null_tolerance_policy": "NULL FORBIDDEN in required fields. Any required null → REJECT_EVENT + LOG."
}
```

### Accepted Event Types (LOCKED LIST)

```yaml
ACCEPTED_EVENT_TYPES:

  STUDENT_SIGNALS:
    - PASSPORT_UPDATED
    - MATCH_COMPLETED
    - MATCH_FORFEITED
    - MENTOR_EVALUATION_SUBMITTED
    - EVALUATOR_SCORE_SUBMITTED
    - PROJECT_MILESTONE_PASSED
    - PROJECT_MILESTONE_FAILED
    - CERTIFICATION_ISSUED
    - CERTIFICATION_REVOKED
    - BADGE_EARNED
    - XP_UPDATED
    - STREAK_UPDATED
    - IDENTITY_VERIFIED
    - IDENTITY_REVOKED
    - COLLUSION_FLAG_RAISED
    - COLLUSION_FLAG_CLEARED
    - CONTENT_VIOLATION_CONFIRMED
    - CONTENT_APPEAL_UPHELD
    - DISPUTE_RESOLVED_IN_FAVOR
    - DISPUTE_RESOLVED_AGAINST
    - PEER_RATING_SUBMITTED
    - INACTIVITY_THRESHOLD_REACHED
    - LOGIN_STREAK_UPDATED

  RECRUITER_SIGNALS:
    - JOB_OFFER_RETRACTED
    - JOB_APPLICATION_GHOSTED
    - JOB_OFFER_ACCEPTED
    - INTERVIEW_SATISFACTION_RATING_SUBMITTED
    - JOB_POSTING_VIOLATION_CONFIRMED
    - RECRUITER_VERIFIED
    - RECRUITER_SUSPENDED
    - DISPUTE_RESOLVED_IN_FAVOR
    - DISPUTE_RESOLVED_AGAINST
    - COLLUSION_FLAG_RAISED
    - COLLUSION_FLAG_CLEARED

  INSTITUTE_SIGNALS:
    - STUDENT_OUTCOME_VERIFIED
    - CURRICULUM_VERSION_UPDATED
    - MENTOR_ADDED_TO_POOL
    - MENTOR_REMOVED_FROM_POOL
    - INSTITUTE_VERIFIED
    - INSTITUTE_SUSPENDED
    - COMPLIANCE_AUDIT_PASSED
    - COMPLIANCE_AUDIT_FAILED

  SME_SIGNALS:
    - JOB_OFFER_RETRACTED
    - JOB_APPLICATION_GHOSTED
    - INTERVIEW_NO_SHOW_CONFIRMED
    - PAYMENT_DISPUTE_RESOLVED_AGAINST
    - CONTRACT_FULFILLED
    - SME_VERIFIED
    - SME_SUSPENDED
    - CANDIDATE_EXPERIENCE_RATING_SUBMITTED

  MENTOR_SIGNALS:
    - MENTOR_EVALUATION_SUBMITTED
    - MENTOR_CALIBRATION_PASSED
    - MENTOR_CALIBRATION_FAILED
    - MENTOR_MISCONDUCT_CONFIRMED
    - MENTOR_MISCONDUCT_CLEARED
    - MENTOR_CERTIFICATION_ACTIVE
    - MENTOR_CERTIFICATION_REVOKED
    - STUDENT_BELT_PROMOTED_UNDER_MENTOR
    - STUDENT_BELT_PROMOTION_FAILED_UNDER_MENTOR

  UNIVERSAL_OVERRIDE:
    - MANUAL_REPUTATION_OVERRIDE_AUTHORIZED

Reject any event_type not in this list. Log rejection. No exceptions.
```

---

## 6️⃣ OUTPUT CONTRACT (STRICT — LOCKED)

### 6A. Reputation Score Object (RSO) — Primary Output

```json
REPUTATION_SCORE_OBJECT: {
  "rso_id":                    "UUID v4",
  "rso_version":               "semver — incremented on every RSO update",
  "actor_id":                  "UUID",
  "actor_class":               "STUDENT | RECRUITER | INSTITUTE | SME | MENTOR",
  "tenant_id":                 "string",
  "domain_track":              "string (where applicable)",
  "computed_at_utc":           "ISO 8601 UTC",
  "model_version":             "string — scoring model version used",
  "audit_reference":           "UUID — links to immutable audit log entry",
  "confidence_score":          "float 0.0–1.0 — confidence in the composite score",

  "composite_score":           "float 0.0–1.0 — weighted aggregate of all dimensions",
  "reputation_tier":           "ELITE | VERIFIED | INTERMEDIATE | BASIC | PROBATION | RESTRICTED",
  "prior_composite_score":     "float | null — score before this computation",
  "prior_reputation_tier":     "string | null",
  "score_delta":               "float (signed) — change from prior",
  "tier_changed":              "boolean",
  "tier_direction":            "UPGRADED | DOWNGRADED | UNCHANGED | FIRST_SET",

  "dimension_scores":          "object — all dimension sub-scores (actor-class-specific structure)",
  "dimension_weights":         "object — weights used (for explainability)",
  "dimension_contribution":    "object — each dimension's weighted contribution to composite",

  "decay_applied":             "boolean",
  "decay_factors":             "object — which dimensions had decay applied and by how much",

  "fraud_guard_applied":       "boolean",
  "fraud_guard_details":       "array — signals that were excluded or discounted",

  "shadow_limiting_active":    "boolean",
  "shadow_limiting_reason":    "string | null",
  "feature_gates_active":      "array of string — feature names currently gated for this actor",

  "explainability_summary":    "object — top 3 positive and top 3 negative contributing signals",
  "contributing_event_ids":    "array of event_ids that triggered this computation",

  "next_trigger_events":       "array of event_types this emission may trigger downstream",
  "anomaly_flags":             "array — any anomalous signals detected in this computation",
  "rso_signature":             "HMAC-SHA256 of canonical RSO JSON — tamper detection"
}
```

### 6B. Explainability Summary Object (Embedded in RSO)

```json
EXPLAINABILITY_SUMMARY: {
  "top_positive_signals": [
    {
      "dimension":     "string",
      "signal_type":   "string",
      "contribution":  "float (positive)",
      "description":   "string — human-readable explanation"
    }
  ],
  "top_negative_signals": [
    {
      "dimension":     "string",
      "signal_type":   "string",
      "contribution":  "float (negative)",
      "description":   "string — human-readable explanation"
    }
  ],
  "next_improvement_actions": [
    {
      "action":        "string — what actor can do to improve score",
      "dimension":     "string — which dimension it would affect",
      "estimated_impact": "LOW | MEDIUM | HIGH"
    }
  ]
}
```

### 6C. Score Query Response (Read Path)

```json
RSO_QUERY_RESPONSE: {
  "request_id":            "UUID v4",
  "actor_id":              "UUID",
  "actor_class":           "string",
  "tenant_id":             "string",
  "current_rso":           "RSO object (full or summary based on requester_role)",
  "score_history_summary": {
    "rso_versions_count":  "integer",
    "score_30d_ago":       "float | null",
    "score_90d_ago":       "float | null",
    "peak_score_all_time": "float",
    "lowest_score_all_time": "float"
  },
  "generated_at_utc":      "ISO 8601 UTC"
}
```

### Output Rules (NON-NEGOTIABLE)

```yaml
- Every RSO MUST include confidence_score, model_version, audit_reference, rso_signature
- RSO MUST NOT be emitted for an actor without at least one verified identity signal
  → HOLD score computation, emit RSO_HOLD_PENDING_IDENTITY event to NOTIFICATION_AGENT
- RSO is NEVER emitted with confidence_score below CONFIDENCE_FLOOR (0.5)
  → Below floor: HOLD RSO, LOG incident, escalate to OBSERVABILITY_AGENT
- Shadow limiting and feature gating decisions MUST be reflected in RSO output
- All RSOs are published to: REPUTATION_TOPIC (Kafka) + REPUTATION_STORE (PostgreSQL, append-only)
- Every RSO version is immutable after publication — new computation creates new RSO version
- RSO signature verified on every read — tamper detection mandatory
```

---

## 7️⃣ TEMPORAL DECAY ENGINE (LOCKED)

Reputation is not static. It must reflect current, sustained activity. The decay engine is a mandatory, non-negotiable component.

```yaml
DECAY_ENGINE_SPECIFICATION:

  EXECUTION_SCHEDULE:     Daily at 02:00 UTC via Apache Airflow scheduled job
  SCOPE:                  ALL actors with RSO — no exceptions
  PROCESSING:             Stateless batch job — reads current RSO, computes decay, writes new RSO if change > threshold

  DECAY_ALGORITHM:
    For each actor:
      For each dimension that has DECAY=YES:
        days_since_last_activity = current_date - last_activity_date_for_dimension
        If days_since_last_activity > dimension.decay_start_threshold_days:
          decay_amount = dimension.decay_rate_per_period * floor(days_since_last_activity / 30)
          new_dimension_score = max(dimension.floor, prior_dimension_score - decay_amount)
        Else:
          no decay applied
      Recompute composite_score from new dimension scores
      If composite_score_delta > RSO_EMIT_THRESHOLD (0.005):
        emit new RSO version
        check tier boundary crossing
        emit REPUTATION_TIER_CHANGED if applicable

  DECAY_RATES_TABLE:
    Skill Credibility Score:       5% per 30 days of no skill activity
    Dojo Performance Score:        3% per 30 days of no Dojo activity
    Belt Authority Score:          2% per 60 days of no belt progression
    Project Execution Score:       4% per 45 days of no project activity
    Peer Trust Score:              3% per 30 days of no peer interaction
    Offer Integrity Score:         5% per 60 days of no hiring activity
    Candidate Feedback Score:      4% per 45 days
    Job Posting Quality Score:     3% per 30 days
    Student Outcome Score:         5% per 90 days
    Mentor Student Progression:    5% per 60 days
    Calibration Authority Score:   6% per 30 days

  DECAY_EXEMPTIONS:
    - BEHAVIORAL_INTEGRITY_SCORE: NO DECAY — violations persist until cleared
    - PLATFORM_CONDUCT_SCORE: NO DECAY — violations persist until cleared
    - Any dimension for actors currently in SHADOW_LIMITING: decay continues — shadow limit does not pause decay

  REHABILITATION_POLICY:
    - Behavioral_Integrity violations: 180-day rehabilitation window
      → After 180 days without new violation: score penalty reduced by 50%
      → Full reset requires COMPLIANCE_OFFICER approval + APPEALS_GOVERNANCE_AGENT confirmation
    - Platform_Conduct violations: 365-day rehabilitation window
    - Collusion flags: Remain until COLLUSION_FLAG_CLEARED event received from ANTI_COLLUSION_AGENT

  AUDIT:
    Every decay computation cycle must log:
    - actor_id, tenant_id, actor_class
    - dimensions_decayed (array), decay_amounts (array)
    - prior_composite_score, new_composite_score
    - execution_timestamp_utc
    - airflow_run_id
```

---

## 8️⃣ FRAUD GUARD ENGINE (LOCKED)

All score inputs must be weighted by their fraud risk. This engine is non-negotiable and always active.

```yaml
FRAUD_GUARD_SPECIFICATION:

  PURPOSE:
    Prevent artificial score inflation from colluded, farmed, or manipulated signals.
    Every input signal is weighted by its source credibility before contributing to the RSO.

  SIGNAL_CREDIBILITY_WEIGHTING:
    Input signals from high-reputation actors carry MORE weight:
      signal_effective_weight = signal_base_weight * source_credibility_multiplier
      source_credibility_multiplier = min(1.0, source_actor_composite_score + 0.3)
      → This means a RESTRICTED-tier actor's peer rating carries ~20% of face value
      → An ELITE-tier actor's peer rating carries 100% of face value

  COLLUSION_DETECTION_RULES:
    - Reciprocal high-score pairs: If actor A gives actor B high peer ratings AND actor B gives actor A high peer ratings repeatedly → flag both for ANTI_COLLUSION_AGENT review
    - Same-IP peer ratings: Peer ratings from actors sharing IP subnets are discounted 80%
    - Burst rating patterns: > 3 peer ratings for same actor in 24h window → fraud_signal = true, confidence_score = 0.2
    - Match farming: > 10 matches against same opponent in 7 days → scores from those matches excluded
    - New account bias: Actors with < 30 days platform age cannot contribute full-weight peer ratings (weight = 0.3)
    - Mentor favoritism: Mentor giving same student above-90th-percentile scores in > 70% of evaluations → flagged to ANTI_COLLUSION_AGENT

  SIGNAL_EXCLUSION_RULES:
    - Any event from an actor with COLLUSION_FLAG_RAISED (unresolved) = 0 weight
    - Any event with fraud_signal = true from upstream = maximum 20% contribution weight
    - Any event from RESTRICTED-tier actor (score < 0.20) = 30% contribution weight maximum
    - Duplicate event_ids = rejected entirely (idempotency guard)

  FRAUD_GUARD_AUDIT:
    Every exclusion or discount must be logged:
    - excluded_event_id, exclusion_reason, actor_id, timestamp_utc
    - Logged to reputation_fraud_guard_log (append-only)
    - Patterns above threshold emitted as REPUTATION_FRAUD_SIGNAL to ANTI_COLLUSION_AGENT
```

---

## 9️⃣ SHADOW LIMITING ENGINE (LOCKED)

```yaml
SHADOW_LIMITING_SPECIFICATION:

  DEFINITION:
    Shadow limiting reduces an actor's platform visibility and certain feature access
    without explicitly notifying the actor that limiting is in effect.
    Exception: RESTRICTED tier (score < 0.20) triggers explicit notification of feature restriction.
    Exception: Feature gating breaches (features blocked) are disclosed to the actor.

  TRIGGER_CONDITIONS:
    - Actor enters RESTRICTED tier (composite_score < 0.20)
    - Active COLLUSION_FLAG_RAISED (unresolved) on actor
    - CONTENT_VIOLATION_CONFIRMED count >= 3 in 30-day window
    - DISPUTE_RESOLVED_AGAINST count >= 2 in 60-day window with no offsetting wins
    - MANUAL_REPUTATION_OVERRIDE_AUTHORIZED with shadow_limit = true by ADMIN_GOVERNANCE

  SHADOW_LIMITING_EFFECTS_BY_ACTOR_CLASS:

    STUDENT:
      - Content posts appear lower in public feed ranking (but not hidden)
      - Job applications de-prioritized in recruiter queue
      - Dojo match visibility reduced (fewer incoming match requests)
      - Peer rating submissions carry 30% weight until limit lifted

    RECRUITER:
      - Job postings appear lower in candidate discovery
      - Candidate application notifications delayed by 24h
      - New job posting requires manual moderation review
      - Premium features suspended until score recovers

    INSTITUTE:
      - Institute appears lower in student discovery and recommendations
      - New course publishing requires manual review
      - SEO-indexed profile marked as "Under Review"

    SME:
      - Job postings de-ranked in candidate discovery
      - New postings require manual moderation
      - Offer acceptance notifications delayed 48h (candidate given extra review window)
      - Platform warning shown to candidates viewing SME job posts

    MENTOR:
      - Cannot conduct new certification matches (CERTIFICATION_AGENT notified immediately)
      - Existing student assignments placed under additional oversight
      - Mentor profile removed from public discovery

  LIFTING_CONDITIONS:
    - Composite score returns above 0.25 through legitimate activity
    - COLLUSION_FLAG_CLEARED received from ANTI_COLLUSION_AGENT
    - CONTENT_VIOLATION count drops below threshold (rehabilitation window)
    - MANUAL_REPUTATION_OVERRIDE_AUTHORIZED with shadow_limit = false by ADMIN_GOVERNANCE

  AUDIT:
    Every shadow_limit activation and deactivation must be logged
    in reputation_shadow_limit_log (append-only)
```

---

## 🔟 ML / AI LOGIC LAYER

### ML Layer (PRIMARY — ~80% of computation)

```yaml
MODELS_USED:

  1. DIMENSION_SCORE_REGRESSORS (Per dimension per actor class)
     Type:               Gradient Boosted Regressor (XGBoost) — one model per dimension
     Target:             Dimension sub-score (0.0–1.0)
     Feature_Set:        Event-specific features for each dimension (see dimension specs)
     Training_Frequency: Monthly
     Drift_Detection:    PSI weekly — alert if PSI > 0.2

  2. FRAUD_PROBABILITY_CLASSIFIER
     Type:               Random Forest Classifier
     Target:             Binary — signal is fraudulent or manipulated (YES/NO)
     Features:           source_actor_composite_score, source_actor_age_days, burst_rating_flag,
                         reciprocal_rating_flag, ip_subnet_match_flag, event_frequency_7d,
                         confidence_score from upstream
     Training_Frequency: Weekly (high-frequency fraud pattern evolution)
     Drift_Detection:    PSI weekly

  3. COMPOSITE_SCORE_CALIBRATOR
     Type:               Ridge Regression (calibration layer over weighted dimension sum)
     Purpose:            Corrects for systematic bias in dimension weights across cohorts
     Training_Frequency: Monthly
     Note:               Calibration layer only — weights in Section 3 are primary

  4. DECAY_RATE_OPTIMIZER
     Type:               Bayesian Optimization
     Purpose:            Refines decay rates per dimension based on observed score-to-outcome
                         correlations (does recruiting outcome correlate with decayed score?)
     Training_Frequency: Quarterly
     Note:               Output is a recommended decay rate adjustment — requires human approval
                         before deployment (COMPLIANCE_OFFICER + ML_LEAD sign-off)

  5. TIER_BOUNDARY_CLASSIFIER
     Type:               Logistic Regression
     Purpose:            Validates that tier boundary crossings are genuine (not transient noise)
     Features:           Score delta, score velocity, dimension stability, fraud_guard_active
     Training_Frequency: Monthly

VERSION_CONTROL:
  - All model artifacts versioned (e.g., RSA-DIMENSION-SKILL-v1.0.0)
  - Immutable model artifacts — old versions retained for audit
  - model_version embedded in every RSO emission
  - Model rollback requires COMPLIANCE_OFFICER approval and audit entry

DRIFT_DETECTION:
  - PSI monitored weekly for all models
  - PSI > 0.2 → DRIFT_ALERT to OBSERVABILITY_AGENT
  - Accuracy drop > 5% → MODEL_RETRAIN_EVENT to ML_TRAINING_PIPELINE_AGENT
```

### AI Layer (SUPPLEMENTARY — ~20% advisory, strictly bounded)

```yaml
AI_USAGE_SCOPE:

  1. EXPLAINABILITY_NARRATIVE_GENERATION:
     Input:   explainability_summary object from RSO
     Output:  Human-readable explanation (max 4 sentences) of why score is at current level
     Usage:   User-facing "Why is my reputation score X?" UI panel in Flutter app
     Example: "Your Dojo Performance score has declined because you haven't participated
               in any matches in the past 45 days. Your Skill Credibility remains strong
               thanks to 3 recently completed certifications. Completing one Dojo match
               this week would improve your score by approximately 0.04 points."

  2. IMPROVEMENT_ACTION_ADVISOR:
     Input:   Current RSO, actor class, domain track, last 5 RSO versions
     Output:  Ranked list of 3 specific improvement actions with estimated impact
     Usage:   "What can I do to improve?" CTA in reputation dashboard
     Note:    Estimates must be clearly labeled as APPROXIMATE and not guaranteed

  3. ANOMALY_PLAIN_LANGUAGE:
     Input:   anomaly_flags from RSO computation
     Output:  Plain-language explanation for ADMIN/COMPLIANCE review panels only
     Note:    NOT shown to the actor themselves without ADMIN_GOVERNANCE approval

AI_BOUNDARIES (NON-NEGOTIABLE):
  - AI NEVER computes or modifies composite_score or any dimension_score
  - AI NEVER determines fraud_guard decisions
  - AI NEVER activates or deactivates shadow_limiting
  - AI NEVER determines tier boundary
  - AI NEVER makes feature gating decisions
  - AI output stored separately — NEVER written into core RSO fields
  - AI output failures do NOT block RSO computation or publication
  - All AI prompts versioned and registered in Config Service
  - No dynamic prompt construction from actor-supplied input

PROMPT_GOVERNANCE:
  - Prompt versions: RSA-EXPLAIN-PROMPT-v1.0, RSA-IMPROVE-PROMPT-v1.0, RSA-ANOMALY-PROMPT-v1.0
  - Deterministic slot-fill templates only — no free-form generation
  - Max AI output: 600 chars per narrative before truncation
  - Response validation: length check, format check, PII scan before serving
```

---

## 1️⃣1️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_EVENT_RPS:         600 events/sec (sustained) | 6,000 events/sec (peak — post-Dojo burst)
EXPECTED_QUERY_RPS:         3,000 queries/sec (recruiter, admin, student reads)
LATENCY_TARGET_COMPUTE:     RSO recomputed and published < 3 seconds P95 from event receipt
LATENCY_TARGET_QUERY:       RSO query response < 400ms P95
LATENCY_TARGET_DECAY_BATCH: Daily decay cycle completes within 4-hour window for 10M actors
MAX_CONCURRENCY:            2,000 parallel event-triggered compute jobs
QUEUE_STRATEGY:             Kafka topic partitioned by actor_id hash (ordered per actor)
                            Separate topics per actor_class for isolation
SCALING_MODEL:              Horizontal stateless workers — separate pools per actor class
EXECUTION_MODEL:            Stateless — all state in RSO_STORE + event stream
IDEMPOTENCY:                Each event_id processed exactly once (Redis dedup, TTL 30 days)
DECAY_BATCH_ENGINE:         Apache Airflow DAG — daily at 02:00 UTC
ASYNC_AI_ENRICHMENT:        Non-blocking — AI narrative generated async after RSO publication
READ_CACHE:                 Redis — hot RSO cache per actor (TTL 120 seconds, invalidated on new RSO)
FEATURE_GATE_CACHE:         Redis — feature gate decisions per actor (TTL 300 seconds)
ANALYTICS_STORE:            ClickHouse — score distribution metrics, tier analytics, decay telemetry
```

---

## 1️⃣2️⃣ SECURITY ENFORCEMENT (ZERO-TRUST — ALL MANDATORY)

```yaml
TENANT_ISOLATION:
  - Every RSO is scoped by tenant_id
  - Row-level security enforced at PostgreSQL level
  - No wildcard tenant queries or score comparisons across tenants
  - tenant_id resolved from JWT — never from actor-supplied payload

ACTOR_CLASS_ISOLATION:
  - Score dimensions are actor-class-specific and never merged
  - STUDENT score model never applied to RECRUITER actor and vice versa
  - Cross-class event signals rejected at input validation

ROLE-BASED QUERY ACCESS:
  - STUDENT: read own RSO only (full dimension breakdown)
  - RECRUITER: read candidate RSO summary (no dimension detail, no shadow_limit details)
  - INSTITUTE_ADMIN: read enrolled student RSO summaries within own tenant
  - PLATFORM_ADMIN: read-only full RSO access — all actors, full detail — full audit trail mandatory
  - COMPLIANCE_OFFICER: full RSO access including shadow_limit status and fraud flags
  - AUTOMATION_AGENT: registered agents only — scope pre-approved — read-only
  - PARENT: student RSO summary (reputation_tier + composite_score only, no dimension detail)
  - NO ACTOR may read another actor's full dimension breakdown (only their own)
  - NO RECRUITER may see shadow_limit_active or fraud_guard details of candidates

MANUAL_OVERRIDE_SECURITY:
  - MANUAL_REPUTATION_OVERRIDE_AUTHORIZED events require override_authority UUID
  - override_authority must match an entry in ADMIN_APPROVED_OVERRIDE_REGISTRY (Keycloak/OPA enforced)
  - All overrides logged with: override_authority_id, reason, prior_score, new_score, approver_chain
  - No automated agent may trigger a manual override event
  - Override events cannot increase composite_score by more than 0.15 in a single operation

ENCRYPTION:
  - RSO data encrypted at rest (AES-256)
  - rso_signature verified on every read
  - All inter-service communication over mTLS
  - Secrets via HashiCorp Vault only

AUDIT_LOGGING:
  - Every RSO computation logged to reputation_audit_log (append-only)
  - Every RSO read logged with actor_id, requester, scope, timestamp
  - Every fraud_guard exclusion logged to reputation_fraud_guard_log (append-only)
  - Every shadow_limit change logged to reputation_shadow_limit_log (append-only)
  - Every decay cycle logged to reputation_decay_log (append-only)
  - Every manual override logged to reputation_override_log (append-only)
```

---

## 1️⃣3️⃣ AUDIT & TRACEABILITY (MANDATORY — EVERY EXECUTION)

```json
RSO_AUDIT_LOG_ENTRY: {
  "audit_id":                   "UUID v4",
  "rso_id":                     "UUID v4",
  "rso_version":                "semver",
  "actor_id":                   "UUID",
  "actor_class":                "string",
  "tenant_id":                  "string",
  "computed_at_utc":            "ISO 8601 UTC",
  "trigger_event_ids":          "array of event_ids that triggered this computation",
  "trigger_event_types":        "array of event type names",
  "prior_composite_score":      "float | null",
  "new_composite_score":        "float",
  "score_delta":                "float (signed)",
  "tier_changed":               "boolean",
  "prior_tier":                 "string | null",
  "new_tier":                   "string",
  "decay_applied":              "boolean",
  "fraud_guard_applied":        "boolean",
  "fraud_guard_exclusions":     "integer — count of signals excluded",
  "shadow_limiting_changed":    "boolean",
  "shadow_limiting_active":     "boolean",
  "confidence_score":           "float",
  "model_version":              "string",
  "ai_enrichment_attempted":    "boolean",
  "ai_enrichment_succeeded":    "boolean",
  "processing_latency_ms":      "integer",
  "anomaly_flags":              "array",
  "escalation_triggered":       "boolean"
}
```

All audit logs stored in `reputation_audit_log` — append-only partition, no UPDATE or DELETE permitted under any circumstance.

---

## 1️⃣4️⃣ FAILURE POLICY (ALL CASES — NON-NEGOTIABLE)

```yaml
FAILURE_CASES:

  INVALID_INPUT_EVENT:
    Action:           REJECT_EVENT
    Log:              LOG_VALIDATION_FAILURE → dead_letter_queue + audit_log
    Escalate:         COMPLIANCE_AUDIT_AGENT
    Retry:            NO
    RSO_Publish:      BLOCKED
    Silent_Failure:   FORBIDDEN

  UNKNOWN_ACTOR_ID:
    Action:           REJECT_EVENT + HOLD
    Log:              LOG_UNKNOWN_ACTOR
    Escalate:         ADMIN_GOVERNANCE_AGENT
    Retry:            YES — retry after 60 seconds, max 3 attempts
    RSO_Publish:      BLOCKED until actor resolved

  UNREGISTERED_SOURCE_AGENT:
    Action:           REJECT_EVENT + EMIT_SECURITY_VIOLATION
    Log:              LOG_SECURITY_VIOLATION (critical)
    Escalate:         COMPLIANCE_AUDIT_AGENT + PLATFORM_ADMIN
    Retry:            NO
    Silent_Failure:   FORBIDDEN

  ML_MODEL_UNAVAILABLE:
    Action:           COMPUTE RSO using last known dimension scores + decay only (degraded mode)
    Log:              LOG_MODEL_UNAVAILABLE (warn)
    Escalate:         OBSERVABILITY_AGENT
    RSO_Publish:      PROCEED with model_version = "DEGRADED-v0.0"
    Retry:            YES — ML enrichment retried async, max 3 attempts

  CONFIDENCE_BELOW_FLOOR (< 0.5):
    Action:           HOLD RSO — do not publish
    Log:              LOG_LOW_CONFIDENCE_HOLD
    Escalate:         OBSERVABILITY_AGENT
    Notify:           NOTIFICATION_AGENT — inform actor of "score update pending"
    Retry:            YES — re-evaluate on next event received

  FRAUD_GUARD_CRITICAL_THRESHOLD:
    Condition:        > 80% of signals in a single computation excluded by fraud guard
    Action:           HOLD RSO, EMIT REPUTATION_FRAUD_CRITICAL to ANTI_COLLUSION_AGENT
    Log:              LOG_FRAUD_CRITICAL
    Escalate:         ANTI_COLLUSION_AGENT + COMPLIANCE_AUDIT_AGENT
    RSO_Publish:      BLOCKED until ANTI_COLLUSION_AGENT resolves
    Silent_Failure:   FORBIDDEN

  MENTOR_CRITICAL_FLOOR_BREACH:
    Condition:        MENTOR composite_score drops below 0.45
    Action:           PUBLISH RSO immediately + EMIT MENTOR_AUTHORITY_CRITICAL_ALERT
    Escalate:         CERTIFICATION_AGENT (suspend cert authority) + ADMIN_GOVERNANCE_AGENT
    Log:              LOG_MENTOR_CRITICAL_FLOOR
    Silent_Failure:   FORBIDDEN — must escalate within 30 seconds

  DECAY_BATCH_FAILURE:
    Condition:        Airflow decay job fails or takes > 6 hours
    Action:           ALERT ON_CALL_ENGINEER, do NOT emit partial decay RSOs
    Log:              LOG_DECAY_BATCH_FAILURE (critical)
    Escalate:         OBSERVABILITY_AGENT + ON_CALL_ENGINEER via PagerDuty
    Retry:            YES — Airflow retry policy: 3 retries, 30-minute intervals

  SHADOW_LIMIT_SYSTEM_ERROR:
    Condition:        Shadow limiting state cannot be computed or persisted
    Action:           FAIL SAFE to NO shadow limiting (allow actor through) — LOG the failure
    Log:              LOG_SHADOW_LIMIT_ERROR
    Escalate:         OBSERVABILITY_AGENT
    Silent_Failure:   FORBIDDEN

  DUPLICATE_EVENT:
    Action:           SKIP (idempotency via Redis dedup registry)
    Log:              LOG_DUPLICATE_SKIP
    RSO_Publish:      BLOCKED
    Silent_Failure:   FORBIDDEN — must log

GLOBAL_RULES:
  - No silent failures under any condition
  - Every failure produces an audit_log entry at minimum
  - ESCALATE_TO: OBSERVABILITY_AGENT (operational) | COMPLIANCE_AUDIT_AGENT (security/integrity)
  - Dead letter queue for all rejected events — inspectable by COMPLIANCE_AUDIT_AGENT
```

---

## 1️⃣5️⃣ INTER-AGENT DEPENDENCY MAP

### Events Emitted by This Agent

```yaml
EMITTED_EVENTS:
  - REPUTATION_SCORE_UPDATED           → all downstream consumers (every RSO publication)
  - REPUTATION_TIER_CHANGED            → NOTIFICATION_AGENT, RANK_ENGINE, JOB_MATCH, CONTENT_VISIBILITY
  - REPUTATION_FRAUD_SIGNAL            → ANTI_COLLUSION_AGENT (on fraud guard threshold breach)
  - REPUTATION_FRAUD_CRITICAL          → ANTI_COLLUSION_AGENT + COMPLIANCE_AUDIT_AGENT
  - MENTOR_AUTHORITY_CRITICAL_ALERT    → CERTIFICATION_AGENT + ADMIN_GOVERNANCE_AGENT
  - SHADOW_LIMIT_ACTIVATED             → ADMIN_GOVERNANCE_AGENT + CONTENT_VISIBILITY_AGENT
  - SHADOW_LIMIT_DEACTIVATED           → ADMIN_GOVERNANCE_AGENT + CONTENT_VISIBILITY_AGENT
  - REPUTATION_MILESTONE_REACHED       → NOTIFICATION_AGENT + GROWTH_ENGINE
  - REPUTATION_FEATURE_VECTOR          → FEATURE_STORE_AGENT (per RSO publication)
  - DRIFT_ALERT                        → OBSERVABILITY_AGENT (on ML model drift detection)
  - MODEL_RETRAIN_EVENT                → ML_TRAINING_PIPELINE_AGENT
  - RSO_HOLD_PENDING_IDENTITY          → NOTIFICATION_AGENT
  - REPUTATION_DECAY_CYCLE_COMPLETE    → OBSERVABILITY_AGENT (daily, post-Airflow job)
```

---

## 1️⃣6️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```json
REPUTATION_FEATURE_VECTOR: {
  "actor_id":              "UUID",
  "actor_class":           "string",
  "tenant_id":             "string",
  "feature_name":          "string",
  "feature_value":         "float | integer | string",
  "rso_version":           "semver",
  "timestamp_utc":         "ISO 8601 UTC",
  "source_agent":          "REPUTATION_SCORE_AGENT"
}
```

```yaml
FEATURE_EMISSIONS_PER_RSO_PUBLICATION:
  - rsa_composite_score                    (float)
  - rsa_reputation_tier_numeric            (int: RESTRICTED=1...ELITE=6)
  - rsa_score_delta_signed                 (float)
  - rsa_shadow_limiting_active             (int: 0/1)
  - rsa_fraud_guard_exclusion_count        (int)
  - rsa_dimension_skill_score              (float — STUDENT only)
  - rsa_dimension_dojo_score               (float — STUDENT only)
  - rsa_dimension_belt_authority           (float — STUDENT only)
  - rsa_dimension_offer_integrity          (float — RECRUITER/SME)
  - rsa_dimension_mentor_student_prog      (float — MENTOR only)
  - rsa_dimension_calibration_authority    (float — MENTOR only)
  - rsa_dimension_behavioral_integrity     (float — all actors)
  - rsa_days_since_last_score_improvement  (int)
  - rsa_tier_changed_flag                  (int: 0/1)
  - rsa_tier_direction_numeric             (int: DOWNGRADED=-1, UNCHANGED=0, UPGRADED=1)
```

---

## 1️⃣7️⃣ INNOVATION ECONOMY COMPATIBILITY

If actor is a STUDENT whose RSO includes project execution signals, emit:

```yaml
CONDITIONS:
  - actor_class = STUDENT
  - project_execution_score dimension is non-zero in this RSO
  - evidence_verified_count > 0 in passport signals

EMIT_TO: IDEA_DNA_AGENT, ROYALTY_ENGINE
EVENT:    REPUTATION_INNOVATION_SEGMENT
PAYLOAD:
  actor_id, tenant_id, domain_track,
  project_execution_score, evidence_quality_index,
  originality_signal (from EVALUATOR_AGENT inputs),
  rso_version, timestamp_utc, source_agent
```

---

## 1️⃣8️⃣ GROWTH ENGINE HOOK

```yaml
REPUTATION_MILESTONES:
  - First-ever RSO computed (first identity signal received)   → NOTIFICATION_AGENT
  - Tier upgrade: BASIC → INTERMEDIATE                         → NOTIFICATION_AGENT + SHARE_TRIGGER_EVENT
  - Tier upgrade: INTERMEDIATE → VERIFIED                      → NOTIFICATION_AGENT + SHARE_TRIGGER_EVENT + XP_UPDATE_EVENT
  - Tier upgrade: VERIFIED → ELITE                             → NOTIFICATION_AGENT + SHARE_TRIGGER_EVENT + XP_UPDATE_EVENT + RANK_UPDATE_EVENT
  - First time fraud guard cleared (after prior flag)          → NOTIFICATION_AGENT (rehabilitation milestone)
  - Shadow limit lifted after recovery                         → NOTIFICATION_AGENT
  - Composite score crosses 0.90 for first time               → SHARE_TRIGGER_EVENT + XP_UPDATE_EVENT

EVENT_FORMAT:
  REPUTATION_MILESTONE_REACHED: {
    actor_id, actor_class, tenant_id, milestone_type,
    milestone_description, prior_tier, new_tier,
    composite_score, rso_version, timestamp_utc
  }
```

---

## 1️⃣9️⃣ PERFORMANCE MONITORING

```yaml
METRICS (Prometheus → Grafana dashboards):

  OPERATIONAL:
    - rsa_rso_compute_success_rate          (target: > 99.5%)
    - rsa_rso_compute_error_rate            (alert: > 0.5%)
    - rsa_compute_latency_p95_ms            (target: < 3000ms)
    - rsa_query_latency_p95_ms              (target: < 400ms)
    - rsa_kafka_consumer_lag                (alert: > 8,000 messages)
    - rsa_decay_batch_duration_hours        (alert: > 4 hours)
    - rsa_dead_letter_queue_size            (alert: > 200 events)

  QUALITY:
    - rsa_avg_composite_score_by_actor_class (monitor cohort health)
    - rsa_tier_distribution_by_class        (monitor tier distribution stability)
    - rsa_shadow_limit_active_rate          (alert: > 3% of actors in tenant)
    - rsa_low_confidence_hold_rate          (alert: > 2%)
    - rsa_fraud_guard_exclusion_rate        (alert: > 5% of total signals)
    - rsa_mentor_critical_floor_count       (alert: any > 0)

  SECURITY:
    - rsa_unauthorized_source_rejection_count (alert: any > 0)
    - rsa_cross_tenant_rejection_count        (alert: any > 0)
    - rsa_manual_override_count_24h           (alert: > 10 in 24h)

  ML_DRIFT:
    - rsa_model_psi_dimension_regressors      (alert: any PSI > 0.2)
    - rsa_model_psi_fraud_classifier          (alert: PSI > 0.15 — tighter for fraud)
    - rsa_model_accuracy_delta               (alert: any model drop > 5%)

  ANALYTICS (ClickHouse):
    - Score distribution histograms by actor_class and tenant
    - Tier transition rate by actor_class per week
    - Fraud guard exclusion trends
    - Decay rate effectiveness (score-to-outcome correlation)
    All exported to Grafana analytical dashboards.
```

---

## 2️⃣0️⃣ DATA ARCHITECTURE (LOCKED)

```yaml
PRIMARY_RSO_STORE:
  Engine:           PostgreSQL
  Table:            reputation_score_objects
  Partitioning:     By tenant_id + actor_class + created_at (monthly range)
  Access:           INSERT for new RSOs — no UPDATE, no DELETE (DB trigger enforced)
  Indexing:         On (actor_id, rso_version), (actor_id, computed_at_utc),
                    (tenant_id, actor_class, reputation_tier),
                    (shadow_limiting_active), (fraud_guard_applied)

AUDIT_LOG_STORE:
  Engine:           PostgreSQL
  Table:            reputation_audit_log
  Access:           INSERT-ONLY — DB user has no UPDATE/DELETE grants

FRAUD_GUARD_LOG:
  Engine:           PostgreSQL
  Table:            reputation_fraud_guard_log
  Access:           INSERT-ONLY

SHADOW_LIMIT_LOG:
  Engine:           PostgreSQL
  Table:            reputation_shadow_limit_log
  Access:           INSERT-ONLY

DECAY_LOG:
  Engine:           PostgreSQL
  Table:            reputation_decay_log
  Access:           INSERT-ONLY

OVERRIDE_LOG:
  Engine:           PostgreSQL
  Table:            reputation_override_log
  Access:           INSERT-ONLY

ANALYTICS_STORE:
  Engine:           ClickHouse
  Purpose:          Score distribution analytics, tier trends, fraud telemetry
  Access:           Write from compute workers, read by Grafana

DEDUPLICATION_REGISTRY:
  Engine:           Redis
  Key:              rsa:dedup:{event_id}
  TTL:              30 days

READ_CACHE:
  Engine:           Redis
  Key:              rsa:cache:actor:{actor_id}:current
  TTL:              120 seconds
  Invalidation:     On any new RSO publication for actor_id

FEATURE_GATE_CACHE:
  Engine:           Redis
  Key:              rsa:gates:{actor_id}
  TTL:              300 seconds

KEY_TABLE: reputation_score_objects:
  - rso_id (UUID PK)
  - rso_version (semver)
  - actor_id (UUID, indexed)
  - actor_class (enum)
  - tenant_id (string, indexed)
  - domain_track (string)
  - computed_at_utc (timestamptz, indexed)
  - model_version (string)
  - audit_reference (UUID)
  - confidence_score (float)
  - composite_score (float, indexed)
  - reputation_tier (enum, indexed)
  - prior_composite_score (float | null)
  - prior_reputation_tier (string | null)
  - score_delta (float)
  - tier_changed (boolean)
  - tier_direction (string)
  - dimension_scores (jsonb)
  - dimension_weights (jsonb)
  - dimension_contribution (jsonb)
  - decay_applied (boolean)
  - decay_factors (jsonb)
  - fraud_guard_applied (boolean)
  - fraud_guard_details (jsonb)
  - shadow_limiting_active (boolean, indexed)
  - shadow_limiting_reason (text | null)
  - feature_gates_active (jsonb array)
  - explainability_summary (jsonb)
  - contributing_event_ids (jsonb array)
  - anomaly_flags (jsonb array)
  - rso_signature (string — HMAC-SHA256)
  → NO UPDATE. NO DELETE. DB trigger enforced. DB user INSERT-ONLY.
```

---

## 2️⃣1️⃣ UI / RENDERING CONTRACT

This agent does NOT render UI. It provides RSO data to rendering agents. The following contracts govern UI surfaces.

```yaml
FLUTTER_APP_SURFACES:

  STUDENT_REPUTATION_DASHBOARD:
    Access:          Own RSO only — full dimension breakdown
    Displays:
      - Composite score (large, prominent)
      - Reputation tier badge
      - Dimension breakdown (spider/radar chart — 7 dimensions)
      - Score delta from 30 days ago
      - Explainability summary (AI-generated narrative)
      - Top 3 improvement actions
      - Score history sparkline (30-day)
    Fixed_Block:     Part of FIXED 40% dashboard — reputation block non-removable
    Data_Source:     RSA Query API (authenticated, self-only)

  RECRUITER_CANDIDATE_TRUST_VIEW:
    Access:          Summary RSO only — composite_score + reputation_tier + badge
    Displays:
      - Trust tier badge (ELITE/VERIFIED/INTERMEDIATE/BASIC/PROBATION/RESTRICTED)
      - Composite score (rounded to 1 decimal)
      - SME reliability warning (if applicable)
    Does NOT display:
      - Dimension breakdown
      - Shadow_limit_active status
      - Fraud_guard details
      - Anomaly flags
    Consent_Gate:    Requires student consent for score visibility

  ADMIN_GOVERNANCE_FULL_VIEW:
    Access:          Full RSO including shadow_limit, fraud_guard, anomaly flags
    Use:             Moderation, override decisions, compliance review
    Audit:           Every admin RSO read is logged

  PARENT_TRUST_VIEW:
    Access:          Reputation tier + composite score only (own child)
    Displays:        "Trust Status: Verified / Growing / New" (no numeric score)

REACT_SEO_LAYER:
  PUBLIC_PROFILE_TRUST_BADGE:
    Source:          PUBLIC_PROFILE_AGENT (reads RSO tier badge via query API)
    Displays:        Reputation tier badge + "Verified by Ecoskiller" text (React/SSR)
    Scope:           ELITE and VERIFIED tiers only — lower tiers not publicly displayed
    Excludes:        RESTRICTED, PROBATION tiers from public profile (show "Building Profile")
    SEO:             Schema.org EducationalOccupationalCredential structured data for verified actors

DESIGN_RULES:
  - Reputation badge uses CLEAN | SOLID | ENTERPRISE design language
  - Badge colors: Gold (ELITE), Green (VERIFIED), Blue (INTERMEDIATE), Grey (BASIC), Yellow (PROBATION)
  - RESTRICTED tier: No public badge — "Profile Under Review" shown to external viewers
  - Skeleton shimmer loading state on RSO fetch — max 1500ms before error state shown
  - Score sparkline: Compact 30-day trend line, no detailed labels
  - Improvement actions: Max 3 shown, each with HIGH/MEDIUM/LOW impact indicator
```

---

## 2️⃣2️⃣ VERSIONING POLICY

```yaml
VERSION_FORMAT:     semver (MAJOR.MINOR.PATCH)

CHANGE_TYPES:
  PATCH:  Bug fix, log improvement, AI prompt update, threshold micro-adjustment (<= 1%)
  MINOR:  New dimension added, new event type accepted, new actor class feature, new decay rule
  MAJOR:  Dimension weight changes (>5%), tier boundary changes, actor class restructure,
          fraud guard algorithm change, shadow limit rule change

ALL_CHANGE_RULES:
  - ADD-ONLY — no dimension or field removal
  - Backward compatible RSO reads — old RSO versions readable by current agent version
  - Two MAJOR versions supported simultaneously
  - Dimension weight changes: require COMPLIANCE_OFFICER + DATA_SCIENCE_LEAD sign-off
  - Decay rate changes: require COMPLIANCE_OFFICER sign-off + documented rationale

CURRENT_VERSIONS:
  AGENT_VERSION:              v1.0.0
  RSO_SCHEMA_VERSION:         v1.0.0
  STUDENT_MODEL_VERSION:      RSA-STUDENT-v1.0.0
  RECRUITER_MODEL_VERSION:    RSA-RECRUITER-v1.0.0
  INSTITUTE_MODEL_VERSION:    RSA-INSTITUTE-v1.0.0
  SME_MODEL_VERSION:          RSA-SME-v1.0.0
  MENTOR_MODEL_VERSION:       RSA-MENTOR-v1.0.0
  FRAUD_CLASSIFIER_VERSION:   RSA-FRAUD-v1.0.0
  EXPLAIN_PROMPT_VERSION:     RSA-EXPLAIN-PROMPT-v1.0
  IMPROVE_PROMPT_VERSION:     RSA-IMPROVE-PROMPT-v1.0
```

---

## 2️⃣3️⃣ NON-NEGOTIABLE RULES (SEALED)

This agent MUST NOT, under any circumstance:

```yaml
FORBIDDEN:
  - Compute a single RSO model for all actor classes — classes are STRICTLY ISOLATED
  - Allow AI layer to set, modify, or influence composite_score or any dimension_score
  - Emit an RSO without rso_signature and confidence_score
  - Allow cross-tenant RSO queries under any circumstance
  - Allow any actor to read another actor's dimension breakdown (self-only rule is absolute)
  - Activate shadow limiting without logging to reputation_shadow_limit_log
  - Allow manual overrides without a verified override_authority UUID in ADMIN_APPROVED_OVERRIDE_REGISTRY
  - Allow manual overrides to increase composite_score by more than 0.15 in a single operation
  - Emit silent failures under any condition
  - Allow decay batch to emit partial RSOs (all-or-nothing per actor)
  - Merge domain data across domain tracks
  - Expose shadow_limiting_active status to the actor themselves via standard query paths
    → Exception: RESTRICTED tier actors receive explicit feature restriction notification
  - Allow RESTRICTED-tier mentor to hold certification authority — automatic CERTIFICATION_AGENT notification is mandatory
  - Accept events from any unregistered source agent
  - Allow score to remain above tier ceiling if fraud guard exclusions would push it below
  - Create hidden scoring logic not documented in this specification
  - Override or bypass COMPLIANCE_AUDIT_AGENT, ANTI_COLLUSION_AGENT, or ADMIN_GOVERNANCE_AGENT
  - Delete RSO records — append-only forever
  - Apply decay to BEHAVIORAL_INTEGRITY_SCORE or PLATFORM_CONDUCT_SCORE (violations persist)
  - Skip fraud guard for any event regardless of source reputation or urgency
```

---

## 2️⃣4️⃣ SEAL DECLARATION

```
╔══════════════════════════════════════════════════════════════════════════════╗
║              ECOSKILLER ANTIGRAVITY — SEALED AGENT ARTIFACT                 ║
║                    REPUTATION_SCORE_AGENT v1.0.0                            ║
║                          AGENT ID: RSA-003                                  ║
║                                                                              ║
║  This specification is SEALED.                                               ║
║  No section may be altered without a formal version bump.                    ║
║  No assumption may be filled by any implementing agent or developer.         ║
║  No creative interpretation is permitted under any circumstance.             ║
║  Any deviation from this spec constitutes an INVALID BUILD.                  ║
║  An INVALID BUILD must STOP EXECUTION immediately.                           ║
║                                                                              ║
║  MUTATION_POLICY              = ADD-ONLY                                     ║
║  EXECUTION_MODE               = DETERMINISTIC + VALIDATED                    ║
║  FAILURE_MODE                 = STOP_EXECUTION                               ║
║  SECURITY_MODEL               = ZERO-TRUST MULTI-TENANT                      ║
║  DATA_POLICY                  = APPEND-ONLY IMMUTABLE LEDGER                 ║
║  CROSS_TENANT                 = FORBIDDEN                                    ║
║  ACTOR_CLASS_MIXING           = FORBIDDEN                                    ║
║  AI_SCORE_OVERRIDE            = FORBIDDEN                                    ║
║  SILENT_FAILURE               = FORBIDDEN                                    ║
║  RSO_DELETION                 = FORBIDDEN                                    ║
║  UNAPPROVED_MANUAL_OVERRIDE   = FORBIDDEN                                    ║
║  CROSS_CLASS_DIMENSION_REUSE  = FORBIDDEN                                    ║
║  SHADOW_LIMIT_WITHOUT_LOG     = FORBIDDEN                                    ║
║  MENTOR_CRITICAL_UNSUPPRESSED = MANDATORY ESCALATION WITHIN 30s             ║
║  FRAUD_GUARD                  = ALWAYS ACTIVE — NO BYPASS                   ║
║  DECAY_ENGINE                 = DAILY — MANDATORY — NO EXEMPTIONS            ║
║                                                                              ║
║  Actor Classes:  STUDENT | RECRUITER | INSTITUTE | SME | MENTOR              ║
║  Tiers:          ELITE | VERIFIED | INTERMEDIATE | BASIC | PROBATION |       ║
║                  RESTRICTED                                                  ║
║  Platform:       Ecoskiller Antigravity                                      ║
║  Architecture:   Microservices + Event-Driven                                ║
║  Scale Target:   10M–100M Actors                                             ║
║  Agent Class:    Core Trust & Governance Infrastructure                      ║
║  Position:       Stage 2 — Intelligence Layer + Stage 4 — Compliance Layer   ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

*End of REPUTATION_SCORE_AGENT.md — v1.0.0 — SEALED*
