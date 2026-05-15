# MENTOR_MATCHING_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### Platform: ECOSKILLER · Execution Engine: ANTIGRAVITY
### Status: SEALED · LOCKED · VERSIONED · DETERMINISTIC
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Agent Class: ENTERPRISE_COORDINATION_AGENT_v1.0

---

## PREAMBLE — BINDING DECLARATION

This document is a **sealed agent specification** for the `MENTOR_MATCHING_AGENT` within the ECOSKILLER platform.

ECOSKILLER is a **Unified Job + Skill + Project + Education + Marketplace SaaS** operating in **multi-tenant enterprise mode** across five domain tracks: Arts · Commerce · Science · Technology · Administration.

The platform's confirmed user classes are:

- **Students** — skill builders, dojo participants, job seekers
- **Mentors / Trainers** — verified, domain-certified, reputation-scored, calibration-governed
- **Parents** — read-only trust layer, consent authority for minor students
- **Enterprises** — SMEs + Corporates, L1–L7 hierarchy, CSR-driven mentor sponsorship
- **Institutes / Colleges** — Principal, HOD, Professor, Staff, TPO — tenant mentor pool owners
- **Recruiters** — company-bound, hiring from mentor-verified talent pools
- **Government / NGO / Policy Bodies** — restricted settlement rules
- **Admins** — Tenant / Platform / Compliance governance

The `MENTOR_MATCHING_AGENT` operates **exclusively within** the `ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE` tier.

⚠️ **ANTIGRAVITY MUST NOT** reinterpret agent scope, matching rules, certification gates, or trust computation defined in this document.  
⚠️ **No AI may extend this agent's authority, matching criteria, or trust scoring beyond what is explicitly declared below.**  
⚠️ **This agent does NOT replace the Dojo Match Engine, Scoring Engine, or Belt Engine — it feeds verified mentors into those systems.**

---

## SECTION 1 — AGENT IDENTITY

```
AGENT_NAME          = MENTOR_MATCHING_AGENT
AGENT_CLASS         = ENTERPRISE_OPTIMIZATION + TRUST_INFRASTRUCTURE
PLATFORM            = ECOSKILLER
EXECUTION_ENGINE    = ANTIGRAVITY
AGENT_VERSION       = 1.0.0
NAMESPACE           = ecoskiller.enterprise.mentor
SERVICE_TYPE        = Microservice (event-driven, stateless logic, stateful infra)
DEPLOYMENT_TARGET   = Kubernetes namespace: enterprise
STACK_LOCK          = ENFORCED
INTERFACE_FREEZE    = REQUIRED
MUTATION_POLICY     = ADD-ONLY
DETERMINISM_RULE    = Identical input → Identical output
FAILURE_MODE        = STOP → REPORT → NO PARTIAL OUTPUT
LATENCY_SLO         = Mentor Commands ≤ 150 ms P95 (hard lock per R49.3)
CONTROL_SIGNAL_SLO  = Mentor Control Signal ≤ 100 ms P95 (hard lock per R49.5)
```

---

## SECTION 2 — PURPOSE AND SCOPE (NON-NEGOTIABLE)

The `MENTOR_MATCHING_AGENT` is responsible for the **full lifecycle of mentor identity, verification, certification, matching, calibration, and trust governance** across the ECOSKILLER platform.

It operates at the intersection of:

1. **Enterprise Optimization** — deterministically matching the right verified mentor to the right student, dojo match, or project — with zero coordinator bottleneck and zero subjective judgment
2. **Trust Infrastructure** — ensuring every mentor's identity, authority, calibration status, and reputation is continuously computed, audited, and propagated into platform-wide trust signals

It serves:
- Students seeking mentors for skill tracks, dojo matches, or career guidance
- Institutes managing their verified tenant mentor pools
- Enterprises sponsoring mentor programs for employees or CSR beneficiaries
- The Dojo Match Engine — which only accepts certified mentors as match supervisors
- The Scoring Engine — which only accepts mentor overrides from calibration-valid mentors
- The Belt & Certification Engine — which requires mentor confirmation before any promotion
- The Admin Governance Service — for misconduct review, discipline, and appeals

It does **NOT** serve:
- Voice GD moderation (separate: Voice GD Orchestrator)
- Dojo match execution (separate: Dojo Match Engine)
- Score computation (separate: Scoring Engine)
- Belt issuance (separate: Certification & Belt Engine)
- Billing plan management (separate: Billing & Subscription Service)

---

## SECTION 3 — CORE DESIGN PHILOSOPHY

```
Replace subjective mentor assignment → deterministic eligibility engine
Replace informal mentor approval → trust-gated certification state machine
Replace human calibration checks → automated drift detection and re-cert cycles
Replace opinion-based reputation → numeric weighted reputation formula
Replace discretionary override → dual-approval audit-logged override only
Replace informal bias detection → mentor bias index (computed, not declared)
```

The agent **explicitly avoids:**
- Personality-based mentor ranking
- Subjective "mentorship style" scoring
- Confidence scoring for mentor selection
- Leadership label inference

The agent operates **only on:**
- Verified identity and certification status
- Domain alignment (Arts / Commerce / Science / Technology / Administration)
- Tenant approval status
- Trust score (computed, not declared)
- Calibration validity window
- Capacity and availability (declared and locked)
- Bias index (computed from match scoring patterns)

---

## SECTION 4 — ARCHITECTURE POSITION

```
ECOSKILLER PLATFORM
│
├── core namespace          (auth, users, jobs)
├── realtime namespace      (GD, interviews, dojo)
├── media namespace         (Jitsi, LiveKit)
├── billing namespace
├── analytics namespace
├── admin namespace
└── enterprise namespace    ← MENTOR_MATCHING_AGENT LIVES HERE
    └── also feeds into:
        ├── realtime namespace (certified mentor tokens for dojo)
        └── core namespace    (mentor profiles on User Service)
```

### Service Separation (MANDATORY)

| Layer | Responsibility |
|---|---|
| MENTOR_MATCHING_AGENT | Matching logic, certification state, trust scoring, calibration, bias detection |
| PostgreSQL | Persistent state, mentor profiles, audit logs, certification records |
| Redis | State machines, slot locks, calibration timers, capacity counters |
| Kafka / RabbitMQ | Event emission → Dojo, Scoring, Certification, Analytics, Billing, Notifications |
| WebSocket | Real-time mentor command channel (≤100ms SLO) |
| Keycloak | Identity verification, RBAC enforcement per AUTHZ-I |
| Open Policy Agent | Centralized policy evaluation on every mentor authority decision |
| ClickHouse | Mentor performance analytics, bias index computation, calibration scoring |
| MinIO | Certification documents, conduct records, calibration reports, audit archives |
| Wazuh | Security audit trail, fake mentor account detection, collusion pattern alerts |
| OpenTelemetry | Distributed tracing across full mentor lifecycle |

**Critical principle:**  
The agent decides mentor eligibility.  
Redis enforces concurrency and capacity.  
PostgreSQL holds truth.  
Kafka propagates certification and trust signals to downstream engines.

---

## SECTION 5 — MENTOR TYPES AND DOMAIN LOCK (LOCKED)

### 5.1 Confirmed Mentor Types

```
Career Mentor          → General career lifecycle guidance
Startup Mentor         → Entrepreneurship and venture building tracks
Industry Expert Mentor → Domain-specific deep expertise
Corporate Mentor       → Enterprise L1–L7 skill alignment
Dojo Supervisor        → Certified match room authority (highest verification bar)
Project Mentor         → Milestone-based real-world project oversight
Research Coordinator   → Academic mentorship and research project supervision
Internship Coordinator → Industry internship pipeline and student placement
```

### 5.2 Domain Track Lock (HARD)

```
DOMAIN_TRACKS = Arts | Commerce | Science | Technology | Administration
```

Rules:
- Mentor may hold certification in **one or more** domain tracks
- Mentor may only supervise matches, sessions, or projects **within certified domains**
- Cross-domain assignment = **AUTHZ FAILURE**
- Domain certification is verified at tenant level — not platform level alone
- Domain leaks = **SECURITY FAILURE** (per platform DOMAIN_TRACK isolation rules)

### 5.3 Mentor Authority Tiers

| Tier | Authority | Certification Required |
|---|---|---|
| Tier-1 Verified Mentor | Profile visible, student guidance, career sessions | Identity + domain verification |
| Tier-2 Certified Supervisor | Dojo match supervision, score override (dual-approval) | Tier-1 + dojo onboarding program + calibration pass |
| Tier-3 Certification Board Member | Belt promotion confirmation, governance board participation | Tier-2 + extended calibration + admin nomination |

---

## SECTION 6 — AUTHZ-I COMPLIANCE (MENTOR & EVALUATOR AUTHORITY LOCK)

Per `AUTHZ-I — MENTOR & EVALUATOR AUTHORITY LOCK` (sealed in ECOSKILLER AUTHZ-MASTER-v1):

**Mentors and evaluators must satisfy ALL of the following before any authority action:**

```
✔ verified_identity          → Keycloak-confirmed, not just email-verified
✔ active_certification       → Certification not expired, not suspended
✔ tenant_approval            → Tenant admin has explicitly approved this mentor
✔ domain_alignment           → Mentor's certified domain matches session/match domain
```

**Hard Restrictions (NEVER violated):**

```
✗ Mentors cannot self-evaluate
✗ Evaluators cannot score their own students
✗ Score overrides require dual approval (two Tier-2+ mentors)
✗ No direct mentor DMs to minor students (AUTHZ-J enforcement)
✗ No unmoderated rooms containing minors
✗ No anonymous participants in mentor-supervised sessions
✗ No off-platform contact sharing with students
```

**Uncertified mentors:**
- Cannot be returned by the matching engine for dojo or certification use
- Cannot issue score overrides
- Cannot run ranked or certification matches
- Are visible in discovery only — not in assignment pools

---

## SECTION 7 — MENTOR CERTIFICATION STATE MACHINE (LOCKED)

```
CREATED
  → IDENTITY_VERIFICATION_PENDING
  → IDENTITY_VERIFIED
  → DOMAIN_CERTIFICATION_PENDING
  → DOMAIN_CERTIFIED
  → TENANT_APPROVAL_PENDING
  → TENANT_APPROVED
  → DOJO_ONBOARDING (if Tier-2 requested)
  → DOJO_ONBOARDING_COMPLETE
  → CALIBRATION_SCHEDULED
  → CALIBRATION_PASSED → ACTIVE_CERTIFIED
  → CALIBRATION_FAILED → REMEDIATION → CALIBRATION_SCHEDULED (retry once)
  → CALIBRATION_FAILED_FINAL → CERTIFICATION_REVOKED

FROM ACTIVE_CERTIFIED:
  → CALIBRATION_DUE (periodic cycle triggered)
  → CALIBRATION_SCHEDULED
  → CALIBRATION_PASSED → ACTIVE_CERTIFIED (renewed)
  → CALIBRATION_FAILED → CERTIFICATION_SUSPENDED → admin_review

FROM ACTIVE_CERTIFIED:
  → MISCONDUCT_FLAGGED → ADMIN_REVIEW → SUSPENDED or CLEARED
  → DISCIPLINE_REVIEW → CERTIFICATION_REVOKED (terminal) or REINSTATED

TERMINAL STATES:
  CERTIFICATION_REVOKED
  PERMANENTLY_BANNED
```

### State Machine Rules

- All transitions are **backend-enforced** — not frontend-triggered
- Each transition emits a **Kafka event** with full context
- No state may be **skipped** — direct jumps are protocol violations
- Redis holds **live certification status cache** — PostgreSQL holds **canonical truth**
- Cache TTL for certification status: **60 seconds maximum**
- Divergence between cache and DB → **invalidate cache immediately, re-read from DB**
- Mentors outside calibration tolerance **lose certification authority automatically** (no admin action required)

---

## SECTION 8 — MENTOR REPUTATION SCORE (LOCKED FORMULA)

Per `R81 — TRAINER IDENTITY, STATUS & REPUTATION SYSTEM LAW`:

```
MENTOR_REPUTATION_SCORE = weighted_sum([
  student_feedback_score         * 0.30,   // avg rating from mentees, verified sessions only
  session_completion_rate        * 0.20,   // completed / scheduled (rolling 90 days)
  placement_success_rate         * 0.20,   // mentee placement outcomes (verified)
  content_quality_rating         * 0.15,   // rubric-scored session quality
  complaint_ratio_penalty        * -0.15,  // validated complaints / total sessions
])

INFLUENCE_RANK = percentile_rank(MENTOR_REPUTATION_SCORE, domain, tenant)
```

Additional trust signals (fed into Trust Infrastructure):

```
+12   Belt promotion confirmed and outcome validated after 90 days
+10   Calibration passed within window
+8    Peer endorsement by Tier-3 board member
+6    Zero complaints for 6-month window
+5    Student feedback average ≥ 4.5/5.0
-5    Single validated complaint
-10   Score override anomaly detected (drift > tolerance)
-12   Calibration missed (not failed — missed)
-15   Calibration failed
-20   Misconduct flag sustained after admin review
-40   Collusion/favoritism pattern confirmed
```

Rules:
- Formula is **immutable** — version bump required for any weight change
- Scores are **appended to audit log** — never overwritten
- Score disputes trigger **admin review** — not automatic score reversal
- All scores feed into platform-wide `reputation_scores` table (R79 compliance)
- Mentor reputation is **visible to students** (aggregate, not raw events)
- Mentor reputation is **visible to tenant admins** (full detail)

---

## SECTION 9 — MATCHING ENGINE (DETERMINISTIC)

### 9.1 Matching Algorithm

```python
# Deterministic, auditable, no subjective inference
def match_mentor_to_request(request_id, request_type):

    r = get_match_request(request_id)  # student, session, or dojo match

    candidates = query_mentors(
        domain=r.domain,
        tenant_id=r.tenant_id,
        certification_status='ACTIVE_CERTIFIED',
        tier_required=r.required_tier,
    )

    eligible = []
    for m in candidates:
        checks = [
            m.certification_status == 'ACTIVE_CERTIFIED',
            m.domain in r.required_domains,
            m.tenant_approved,
            m.calibration_valid(at=r.scheduled_time),
            m.capacity_available(r.scheduled_time, r.duration),
            m.trust_score >= r.min_trust_threshold,
            m.bias_index <= BIAS_INDEX_CEILING,
            not m.has_conflict(r.student_id),       # no prior conflict flag
            not same_entity(m.id, r.student_id),    # cannot self-assign
        ]
        if all(checks):
            eligible.append(m)

    if not eligible:
        return NO_MATCH_AVAILABLE, explain_all_failed(candidates)

    # Sort by: reputation_score DESC, then influence_rank DESC
    ranked = sort(eligible, key=lambda m: (m.reputation_score, m.influence_rank), order='DESC')

    return ranked[0], MATCH_REASONS(ranked[0])
```

### 9.2 Matching Rules (LOCKED)

- No confidence scoring
- No personality inference
- No "mentorship style" heuristics
- Only: certification status, domain, tenant approval, calibration validity, capacity, trust threshold, bias index, conflict check
- Match output includes **explicit reason codes** (logged to audit)
- Match rejection includes **explicit failure codes** for each failed check
- All match decisions are **reproducible and auditable**

### 9.3 Tenant Mentor Pool Isolation

```
Tenant A mentor pool → NEVER visible to Tenant B students
No shared mentor pools without explicit audit trail
Cross-tenant mentor sharing requires:
  - admin approval from both tenants
  - separate audit record
  - separate billing line
```

---

## SECTION 10 — CALIBRATION ENGINE (NON-OPTIONAL)

Per `SECTION T3 — RATER CALIBRATION LOCK`:

### 10.1 Calibration Process

```
CALIBRATION CYCLE: Every 90 days (Tier-2), Every 180 days (Tier-3)

Process:
1. System schedules calibration window (30-day notice)
2. Mentor assigned 3 gold-standard scored sessions
3. Mentor submits scores independently
4. System compares mentor scores against gold-standard scores
5. Drift computed per scoring dimension
6. Calibration scorecard generated
7. Pass/Fail determination:
   - All dimensions within tolerance → PASS
   - Any dimension outside tolerance → FAIL

DRIFT TOLERANCE:
  Score dimension drift    ≤ ±8%    per dimension
  Overall composite drift  ≤ ±5%    across all dimensions
```

### 10.2 Mentor Bias Index

```
BIAS_INDEX = computed from rolling match scoring history:

Signals:
  - reciprocal high-scoring pairs (match farming)
  - abnormal peer score clustering
  - rating inflation clusters
  - mentor favoritism patterns (same student systematically scored higher)
  - score variance anomaly vs. domain median

BIAS_INDEX_CEILING = 0.25 (any mentor above this is excluded from matching pool)

Detection feeds:
  - SECTION T9 — COLLUSION & MANIPULATION DETECTION LOCK
  - SECTION AFC-4 — DOJO MATCH & SCORING FRAUD LOCK
```

### 10.3 Calibration Failure Handling

```
First failure:
  → CERTIFICATION_SUSPENDED (cannot supervise new matches)
  → Remediation program assigned (min 2 weeks)
  → Retry calibration permitted once

Second consecutive failure:
  → CERTIFICATION_REVOKED (terminal for current cycle)
  → Admin review required for reinstatement
  → Reinstatement requires: admin approval + new full onboarding + calibration pass

Missed calibration window:
  → Auto-suspension after window close + 7 days grace
  → No exceptions
  → Trust penalty: -12 points
```

---

## SECTION 11 — DATA MODEL (LOCKED, ADD-ONLY)

### Primary Entities

```
MentorProfile
MentorDomainCertification
MentorVerificationRecord
MentorTenantApproval
MentorCapacitySlot
MentorAssignment
MentorCalibrationRecord
MentorCalibrationScorecard
MentorReputationScore
MentorReputationEvent
MentorBadge
MentorMilestone
MentorEndorsement
MentorBiasIndexSnapshot
MentorConflictFlag
MentorScoreOverride           ← dual-approval required
MentorOverrideAuditLog        ← immutable
MentorMisconductReport
MentorDisciplineRecord
MentorConductAuditLog         ← immutable, append-only
MentorMatchRequest
MentorMatchResult
MentorMatchReasonLog          ← why this mentor was matched or rejected
```

### Immutable Fields (Never mutate post-creation)

```
MentorVerificationRecord.*              (entire record immutable after creation)
MentorScoreOverride.override_id
MentorScoreOverride.approved_by[]       (both approvers locked at creation)
MentorOverrideAuditLog.*                (entire table append-only)
MentorConductAuditLog.*                 (entire table append-only)
MentorCalibrationRecord.gold_scores     (reference scores never editable)
MentorMatchReasonLog.*                  (match decision log append-only)
MentorDisciplineRecord.created_at
MentorDisciplineRecord.incident_hash
```

---

## SECTION 12 — RBAC PERMISSION MATRIX (LOCKED)

| Action | Student | Parent (Minor) | Mentor (Tier-1) | Mentor (Tier-2) | Mentor (Tier-3) | Institute Admin | Tenant Admin | Platform Admin |
|---|---|---|---|---|---|---|---|---|
| Browse mentor profiles | ✅ | 👁️ Read | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Request mentor match | ✅ | ❌ | ❌ | ❌ | ❌ | ✅ | ✅ | ✅ |
| Accept match | ❌ | ❌ | ✅ | ✅ | ✅ | ❌ | ❌ | ✅ |
| Supervise dojo match | ❌ | ❌ | ❌ | ✅ | ✅ | ❌ | ❌ | ✅ |
| Submit score override | ❌ | ❌ | ❌ | ✅ (dual) | ✅ (dual) | ❌ | ❌ | ✅ |
| View calibration status | ❌ | ❌ | ✅ (own) | ✅ (own) | ✅ (own) | ✅ | ✅ | ✅ |
| Approve tenant mentor | ❌ | ❌ | ❌ | ❌ | ❌ | ✅ | ✅ | ✅ |
| View bias index | ❌ | ❌ | ✅ (own) | ✅ (own) | ✅ (own) | ✅ | ✅ | ✅ |
| View mentor reputation | ✅ (agg) | 👁️ (agg) | ✅ (own full) | ✅ (own full) | ✅ (own full) | ✅ (full) | ✅ (full) | ✅ (all) |
| File misconduct report | ✅ | ❌ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Review misconduct | ❌ | ❌ | ❌ | ❌ | ✅ (board) | ✅ | ✅ | ✅ |
| Revoke certification | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ | ✅ | ✅ |
| View conduct audit log | ❌ | ❌ | ✅ (own) | ✅ (own) | ✅ (own) | ✅ (tenant) | ✅ (tenant) | ✅ (all) |
| Emergency session end | ❌ | ❌ | ❌ | ✅ | ✅ | ❌ | ❌ | ✅ |

**Minor Safety Rules (AUTHZ-J, NON-NEGOTIABLE):**

```
No direct mentor DMs to minors → ENFORCED AT PLATFORM LAYER
No anonymous mentors in minor-facing sessions → ENFORCED
No unmoderated sessions with minors → ENFORCED
Any off-platform contact sharing attempt → LOGGED + FLAGGED + ADMIN ALERT
```

---

## SECTION 13 — MENTOR SCORE OVERRIDE PROTOCOL (DUAL-APPROVAL REQUIRED)

Per `AUTHZ-I` and `mentor override ↔ rating recompute` (Section P6):

```
OVERRIDE FLOW:

1. Tier-2 or Tier-3 mentor submits override request:
   Fields: match_id, original_score, proposed_score, dimension, justification_text

2. System validates:
   - Submitter is ACTIVE_CERTIFIED
   - Submitter's domain matches match domain
   - Submitter did NOT supervise the original match (no self-review)
   - Bias index below ceiling

3. Second Tier-2/Tier-3 mentor (different person) must approve:
   - Must NOT be from same institution as submitter
   - Must review justification independently
   - Override approved only on both confirmations

4. On dual approval:
   - MentorScoreOverride record created (immutable)
   - MentorOverrideAuditLog entry appended
   - Kafka event: mentor.score_override.approved
   - Scoring Engine recomputes affected scores
   - Belt eligibility re-evaluated if affected

5. On single rejection:
   - Override REJECTED
   - Both decisions logged
   - No score change

OVERRIDE MONITORING:
  mentor_override_frequency dashboard (Grafana, non-optional)
  High override frequency → anomaly alert → admin review
```

---

## SECTION 14 — COLLUSION AND FAVORITISM DETECTION (LOCKED)

Per `SECTION T9`, `SECTION AFC-4`, `SECTION AFC-6`:

### Detected Patterns

```
Pattern 1: RECIPROCAL HIGH SCORING
  Trigger: Mentor A repeatedly scores Mentor B's students high,
           and Mentor B repeatedly scores Mentor A's students high.
  Detection: Pair-level score correlation > 0.85 over ≥ 5 sessions.

Pattern 2: MENTOR FAVORITISM
  Trigger: Specific student consistently scored above domain median
           by same mentor across multiple sessions.
  Detection: Per-student deviation from domain median > 15% in same mentor's sessions.

Pattern 3: RATING INFLATION CLUSTER
  Trigger: Mentor's average scores exceed domain average by > 12%
           over rolling 60-day window.

Pattern 4: MENTOR COLLUSION BILLING
  Trigger: Unusual certification fee patterns correlated with
           abnormal score clusters.
  Detection: Billing fraud signals cross-referenced with scoring anomalies.

Pattern 5: FAKE TOURNAMENT LOOPS
  Trigger: Mentor consistently approving certifications for
           participants in artificially created match sequences.
```

### Detection Consequences

```
Level 1 (Pattern detected, first occurrence):
  → FLAGGED status on affected matches
  → Matches excluded from belt calculation until audit
  → Mentor bias index updated
  → Admin notified

Level 2 (Pattern confirmed after audit):
  → CERTIFICATION_SUSPENDED
  → All pending belt confirmations from this mentor: HOLD
  → Trust score penalty applied
  → Admin review mandatory

Level 3 (Systematic, multi-pattern):
  → CERTIFICATION_REVOKED
  → All mentor-confirmed belts in review window: RE-AUDIT
  → Platform fraud report generated
  → Potential permanent ban (admin decision, not automatic)
```

---

## SECTION 15 — MENTOR EMERGENCY INTERVENTION (LOCKED)

Per `SECTION T10 — BEHAVIORAL SAFETY LOCK` and `SECTION WHS-7 — DOJO LIVE SESSION SAFETY LOCK`:

```
EMERGENCY INTERVENTION AUTHORITY: Tier-2 and Tier-3 certified mentors only

Actions available during live sessions:
  - SESSION_PAUSE    → Pause all activity, no data loss
  - PARTICIPANT_MUTE → Mute specific participant's audio/video
  - PARTICIPANT_REMOVE → Remove participant from session
  - SESSION_TERMINATE → End session immediately

Triggers:
  - Harassment detected or reported
  - Intimidation signals (student-reported)
  - Abuse flag from auto-detection
  - Technical integrity failure

Post-intervention requirements (MANDATORY):
  - MentorConductAuditLog entry within 15 minutes
  - Incident report filed (system-generated draft + mentor confirmation)
  - Admin notification (immediate)
  - Session transcript + metadata archived to MinIO (WORM)

Safety overrides scoring:
  - Session terminated via emergency → scoring for that session VOIDED
  - No trust penalty to students for voided session
  - Admin reviews whether match should be replayed

CONTROL SIGNAL SLO: ≤ 100 ms (hard lock per R49.5)
```

---

## SECTION 16 — EVENT SCHEMA REGISTRY (CONTRACT-FIRST, LOCKED)

All events must be registered **before** code generation. Absence → **STOP EXECUTION**

```yaml
events:

  - name: mentor.identity_verified
    producer: MENTOR_MATCHING_AGENT
    consumers: [NotificationService, AnalyticsService, AuditService]
    fields: [mentor_id, verification_record_id, verified_at, tenant_id]

  - name: mentor.domain_certified
    producer: MENTOR_MATCHING_AGENT
    consumers: [NotificationService, UserService, DojoMatchEngine]
    fields: [mentor_id, domain, tier, certified_at, tenant_id]

  - name: mentor.tenant_approved
    producer: MENTOR_MATCHING_AGENT
    consumers: [NotificationService, AnalyticsService]
    fields: [mentor_id, tenant_id, approved_by, approved_at]

  - name: mentor.calibration_scheduled
    producer: MENTOR_MATCHING_AGENT
    consumers: [NotificationService]
    fields: [mentor_id, calibration_window_start, calibration_window_end]

  - name: mentor.calibration_passed
    producer: MENTOR_MATCHING_AGENT
    consumers: [NotificationService, ReputationService, AnalyticsService]
    fields: [mentor_id, calibration_record_id, passed_at, drift_score]

  - name: mentor.calibration_failed
    producer: MENTOR_MATCHING_AGENT
    consumers: [NotificationService, AdminService, ReputationService]
    fields: [mentor_id, calibration_record_id, failed_at, failed_dimensions]

  - name: mentor.certification_suspended
    producer: MENTOR_MATCHING_AGENT
    consumers: [DojoMatchEngine, ScoringEngine, BeltEngine, NotificationService, AdminService]
    fields: [mentor_id, suspended_at, reason_code, tenant_id]

  - name: mentor.certification_revoked
    producer: MENTOR_MATCHING_AGENT
    consumers: [DojoMatchEngine, ScoringEngine, BeltEngine, NotificationService, AdminService, BillingService]
    fields: [mentor_id, revoked_at, reason_code, tenant_id, affected_match_ids]

  - name: mentor.matched
    producer: MENTOR_MATCHING_AGENT
    consumers: [NotificationService, AnalyticsService, BillingService]
    fields: [mentor_id, student_id, request_id, match_type, domain, tenant_id, matched_at]

  - name: mentor.match_failed
    producer: MENTOR_MATCHING_AGENT
    consumers: [NotificationService, AdminService, AnalyticsService]
    fields: [request_id, reason_codes, failed_at, tenant_id]

  - name: mentor.score_override_approved
    producer: MENTOR_MATCHING_AGENT
    consumers: [ScoringEngine, BeltEngine, AnalyticsService, AuditService]
    fields: [override_id, mentor_id, match_id, original_score, new_score, approved_by, approved_at]

  - name: mentor.bias_flag_raised
    producer: MENTOR_MATCHING_AGENT
    consumers: [AdminService, AnalyticsService, FraudDetectionEngine]
    fields: [mentor_id, pattern_type, evidence_window, flagged_at, tenant_id]

  - name: mentor.misconduct_reported
    producer: MENTOR_MATCHING_AGENT
    consumers: [AdminService, NotificationService, AuditService]
    fields: [mentor_id, reporter_id, report_type, report_id, reported_at]

  - name: mentor.emergency_intervention
    producer: MENTOR_MATCHING_AGENT
    consumers: [AdminService, NotificationService, AuditService, AnalyticsService]
    fields: [mentor_id, session_id, action_type, triggered_at, reason]

  - name: mentor.trust_event
    producer: MENTOR_MATCHING_AGENT
    consumers: [TrustScoreEngine, ReputationService]
    fields: [mentor_id, event_type, delta, reason_code, tenant_id, timestamp]
```

---

## SECTION 17 — API CONTRACT REGISTRY (LOCKED)

```
# Mentor Discovery
GET    /api/v1/mentors                                 → Search/filter mentors (tenant-scoped)
GET    /api/v1/mentors/{id}                            → Mentor profile (RBAC-gated detail level)
GET    /api/v1/mentors/{id}/reputation                 → Reputation score and history
GET    /api/v1/mentors/{id}/calibration                → Calibration status and history (own/admin)
GET    /api/v1/mentors/{id}/bias-index                 → Bias index (own/admin only)

# Mentor Registration & Certification
POST   /api/v1/mentors/register                        → Submit mentor registration
POST   /api/v1/mentors/{id}/verify                     → Trigger identity verification
POST   /api/v1/mentors/{id}/certify                    → Domain certification submission
POST   /api/v1/mentors/{id}/tenant-approve             → Tenant admin approval
POST   /api/v1/mentors/{id}/dojo-onboard               → Dojo onboarding enrollment

# Calibration
GET    /api/v1/mentors/{id}/calibration/next           → Next calibration window
POST   /api/v1/mentors/{id}/calibration/submit         → Submit calibration scores
GET    /api/v1/mentors/{id}/calibration/{cid}/result   → Calibration scorecard

# Matching
POST   /api/v1/mentor-match/request                    → Create match request
GET    /api/v1/mentor-match/{request_id}               → Match status and result
POST   /api/v1/mentor-match/{request_id}/accept        → Mentor accepts match
POST   /api/v1/mentor-match/{request_id}/decline       → Mentor declines (reason required)

# Capacity
PUT    /api/v1/mentors/{id}/capacity                   → Update availability slots
GET    /api/v1/mentors/{id}/capacity                   → View current capacity

# Score Override
POST   /api/v1/mentors/overrides                       → Submit override request (Tier-2+)
POST   /api/v1/mentors/overrides/{id}/approve          → Second approver confirmation
GET    /api/v1/mentors/overrides/{id}                  → Override status

# Safety
POST   /api/v1/mentors/{id}/emergency/{session_id}     → Emergency intervention trigger
POST   /api/v1/mentors/{id}/misconduct-report          → File misconduct report

# Admin
GET    /api/v1/admin/mentors/bias-flags                → Active bias flags
GET    /api/v1/admin/mentors/calibration-due           → Upcoming calibration windows
POST   /api/v1/admin/mentors/{id}/suspend              → Admin suspension
POST   /api/v1/admin/mentors/{id}/revoke               → Admin revocation
GET    /api/v1/admin/mentors/conduct-audit             → Conduct audit log (scoped)
GET    /api/v1/admin/mentors/override-frequency        → Override frequency dashboard feed
```

All endpoints:
- JWT-authenticated (Keycloak)
- RBAC + ABAC enforced (Open Policy Agent) — every request evaluated against current certification status
- Rate-limited (Kong/Envoy)
- Audit-logged on every mutation

---

## SECTION 18 — FAILURE HANDLING (DETERMINISTIC)

| Failure Event | System Action |
|---|---|
| Identity verification timeout (72h) | Profile stays UNVERIFIED, re-trigger notification |
| Tenant approval not received (14 days) | Escalate to tenant admin, log |
| Calibration window missed (not failed) | Auto-suspend after 7-day grace, -12 trust, admin notified |
| Calibration failed | CERTIFICATION_SUSPENDED, remediation assigned |
| Second consecutive calibration failure | CERTIFICATION_REVOKED, admin review required |
| Bias index exceeds ceiling | Excluded from matching pool immediately, admin flagged |
| Collusion pattern confirmed | CERTIFICATION_SUSPENDED, affected matches flagged |
| Misconduct sustained | CERTIFICATION_SUSPENDED minimum, admin decision for revocation |
| Score override anomaly (high frequency) | Admin alert, override pause pending review |
| Dual approval not received in 48h | Override EXPIRES, no score change |
| Emergency intervention | Session terminated, audit log mandatory within 15min |
| Certification status cache miss | Fail closed — deny action, force DB read |
| Cross-domain assignment attempt | AUTHZ FAILURE, blocked, logged |
| Minor protection violation attempt | STOP + CRITICAL ALERT + admin + Wazuh event |
| No eligible mentor for request | NO_MATCH event emitted, request queued, admin notified if >24h |

**No retries on AUTHZ-I violations.**  
**No discretionary override by any single role below Admin.**  
**All failures produce immutable audit records.**

---

## SECTION 19 — OBSERVABILITY (NON-OPTIONAL)

### Metrics (Prometheus)

```
mentor_certification_status_total{status, domain, tenant_id}
mentor_calibration_pass_rate{domain, tenant_id}
mentor_bias_index_distribution{bucket, domain}
mentor_match_success_rate{request_type, domain, tenant_id}
mentor_match_latency_ms{percentile}
mentor_override_frequency_total{mentor_id, period}
mentor_emergency_intervention_total{action_type, tenant_id}
mentor_misconduct_report_total{report_type, outcome}
mentor_trust_score_distribution{bucket, domain, tenant_id}
mentor_command_signal_latency_ms{percentile}         ← must stay ≤ 100ms P95
```

### Dashboards (Grafana)

```
- Certification funnel: registered → verified → certified → active
- Calibration health: pass rate, drift scores, upcoming due
- Bias index distribution by domain and tenant
- Override frequency monitoring (anomaly threshold visible)
- Match success rate and fallback rate
- Trust score distribution trend (30/60/90 day)
- Misconduct pipeline: reported → reviewed → resolved
- Emergency intervention rate by session type
```

### Alerts

```
- Calibration due > 30 mentors overdue → ALERT OPS
- Bias index ceiling breach → IMMEDIATE ALERT ADMIN
- Collusion pattern detected → ALERT ADMIN + FRAUD ENGINE
- Override frequency anomaly (>3 overrides/week per mentor) → ALERT ADMIN
- Mentor command signal latency > 120ms P95 → PAGE OPS
- No eligible mentor for >5 requests in same domain → ALERT TENANT ADMIN
- Minor protection violation attempt → CRITICAL ALERT + WAZUH
```

### Distributed Tracing (OpenTelemetry)

```
Spans required for:
- Full mentor certification lifecycle
- Each matching request (mentor selection reasoning)
- Each calibration session
- Each score override (full dual-approval chain)
- Emergency intervention signal path
- Trust score computation chain
```

---

## SECTION 20 — SECURITY BASELINE (LOCKED)

```
✔ JWT authentication on all endpoints (Keycloak)
✔ RBAC + ABAC enforced via Open Policy Agent per AUTHZ-I
✔ Certification status checked on every authority action (never cached > 60s)
✔ Rate limiting per IP + per mentor (Kong/Envoy)
✔ All PII fields encrypted at rest (HashiCorp Vault)
✔ Mentor conduct records signed with SHA-256 hash
✔ Immutable audit logs (append-only PostgreSQL partitions)
✔ Calibration records stored in MinIO with WORM policy
✔ Wazuh SIEM on all agent API endpoints + fake mentor account detection
✔ CSRF protection on all state-changing operations
✔ No direct DMs to minor students enforced at platform layer (AUTHZ-J)
✔ No raw mentor PII in Kafka event payloads (reference IDs only)
✔ Tenant isolation enforced at database row level (multi-tenant)
✔ Cross-domain assignment attempts blocked at OPA level, not just application layer
✔ Score override dual-approval chain: both approvers' identities locked at creation
✔ Minor protection: any off-platform contact sharing attempt triggers Wazuh alert
```

---

## SECTION 21 — INTEGRATION MAP

```
MENTOR_MATCHING_AGENT
│
├── READS FROM
│   ├── Auth Service             → identity, certification flags, minor indicators
│   ├── User Service             → mentor profile, skill vectors, domain certifications
│   ├── Keycloak                 → live token validation, RBAC resolution
│   └── ClickHouse               → scoring history for bias index computation
│
├── WRITES TO
│   ├── PostgreSQL               → all persistent certification and matching state
│   ├── Redis                    → live certification cache, capacity locks, calibration timers
│   ├── MinIO                    → certification docs, calibration records, conduct archives
│   └── ClickHouse               → mentor performance metrics (via analytics service)
│
├── EMITS EVENTS TO (Kafka)
│   ├── DojoMatchEngine          → certified mentor tokens for match supervision
│   ├── ScoringEngine            → override approvals, certification status changes
│   ├── BeltEngine               → mentor certification validity for promotion gates
│   ├── TrustScoreEngine         → trust delta events per mentor
│   ├── ReputationService        → reputation propagation
│   ├── NotificationService      → all lifecycle notifications
│   ├── AnalyticsService         → funnel and performance metrics
│   ├── BillingService           → mentor payout events, license metering
│   ├── FraudDetectionEngine     → bias and collusion signals
│   └── AdminService             → misconduct, suspension, revocation escalations
│
└── GOVERNED BY
    ├── Open Policy Agent         → every authorization decision (AUTHZ-I, AUTHZ-J)
    ├── Wazuh                    → security audit, fake mentor detection
    └── Admin Governance Service  → discipline board, appeals, misconduct review
```

---

## SECTION 22 — MULTI-TENANT ISOLATION (MANDATORY)

```
Every database query MUST include tenant_id filter.
Every Kafka event MUST include tenant_id in payload.
Every API response MUST be scoped to requesting tenant.
Tenant mentor pools NEVER shared without explicit audit trail.
Cross-tenant mentor assignment:
  → requires admin approval from both tenants
  → requires separate billing record
  → cross-tenant query without approval → STOP + AUDIT + ALERT

Mentor payout: No mentor payout without settled fee (platform rule locked).
Mentor license pricing: tenant-specific, not platform-default.
```

---

## SECTION 23 — DEPLOYMENT SPECIFICATION

```yaml
namespace: enterprise
service: mentor-matching-agent
replicas: 2 (min) / 12 (max, HPA)
resources:
  requests:
    cpu: 500m
    memory: 768Mi
  limits:
    cpu: 2000m
    memory: 2Gi
health_check:
  liveness:  /health/live
  readiness: /health/ready
performance_priority: control-plane (Class-1 interactive, per R49.6)
env_files:
  dev:     /config/environments/dev.env
  test:    /config/environments/test.env
  staging: /config/environments/staging.env
  prod:    /config/environments/production.env
helm_chart: charts/mentor-matching-agent
db_migration: Flyway (auto on startup, staging/prod gated)
secrets: HashiCorp Vault (injected at runtime, never in image)
latency_budget:
  mentor_commands: 150ms P95
  control_signals: 100ms P95
  certification_cache_ttl: 60s
```

---

## SECTION 24 — ANTIGRAVITY EXECUTION RULES (BINDING)

```
ANTIGRAVITY_RULE_1:
  Generate only what is explicitly declared in this document.
  No additional entities, permissions, authority scopes, or logic
  may be inferred or added.

ANTIGRAVITY_RULE_2:
  Matching is rule-based (Section 9).
  Do NOT substitute ML confidence scoring for eligibility checks.
  Do NOT introduce personality or style inference.
  Determinism is mandatory — identical inputs produce identical outputs.

ANTIGRAVITY_RULE_3:
  The certification state machine (Section 7) is the ONLY valid lifecycle.
  No shortcut transitions permitted.
  No state may be skipped.
  Mentors outside calibration tolerance lose authority AUTOMATICALLY.

ANTIGRAVITY_RULE_4:
  AUTHZ-I compliance (Section 6) is NON-NEGOTIABLE.
  Any code path that allows an uncertified mentor to supervise a match,
  issue an override without dual approval, or evaluate own students
  is a CRITICAL VIOLATION.
  STOP GENERATION + REPORT VIOLATION.

ANTIGRAVITY_RULE_5:
  Minor protection (AUTHZ-J, Section 12) is NON-NEGOTIABLE.
  Any code path that allows direct mentor DM to a minor,
  or unmoderated minor session,
  is a CRITICAL VIOLATION.
  STOP GENERATION + REPORT VIOLATION.

ANTIGRAVITY_RULE_6:
  All events in Section 16 must be emitted.
  Silent state transitions are FORBIDDEN.

ANTIGRAVITY_RULE_7:
  The reputation formula (Section 8) is IMMUTABLE.
  Any proposed weight change requires a VERSION BUMP
  and explicit human declaration.

ANTIGRAVITY_RULE_8:
  Score overrides require dual approval (Section 13).
  Single-approver overrides DO NOT EXIST in this agent.

ANTIGRAVITY_RULE_9:
  Bias detection (Section 14) is not optional.
  All patterns must be detected and produce level-appropriate consequences.
  Collusion detection CANNOT be disabled by tenant config.

ANTIGRAVITY_RULE_10:
  Mentor command signal latency SLO ≤ 100ms P95 is a HARD CONTRACT.
  Any architecture that cannot guarantee this SLO is NON-COMPLIANT.

ANTIGRAVITY_RULE_11:
  All audit logs are APPEND-ONLY.
  Update or delete on MentorOverrideAuditLog, MentorConductAuditLog,
  MentorMatchReasonLog is FORBIDDEN at all layers.

ANTIGRAVITY_RULE_12:
  Certification status cache TTL = 60 seconds maximum.
  Stale certification cache cannot authorize mentor actions.
  On cache miss → fail closed, force DB read.

ANTIGRAVITY_RULE_13:
  This agent does NOT handle:
  - Voice GD moderation
  - Dojo match execution
  - Score computation
  - Belt issuance
  - Billing plans
  Any attempt to cross these boundaries → STOP EXECUTION.

ANTIGRAVITY_RULE_14:
  UI screens for this agent respect the platform dual-frontend law:
  - Operational mentor UI → Flutter
  - Public mentor discovery / SEO pages → Next.js
  No exceptions.

ANTIGRAVITY_RULE_15:
  Tenant isolation is enforced at the DATABASE layer — not just the application layer.
  Row-level security on all mentor tables is MANDATORY.
  Bypassing tenant_id filters via raw queries → SECURITY FAILURE.
```

---

## SECTION 25 — PRODUCTION READINESS GATES (ALL MUST PASS)

```
□ All event schemas registered (Section 16)
□ All API contracts registered (Section 17)
□ RBAC + ABAC permission matrix implemented and tested (Section 12)
□ Certification state machine unit tested for all paths including failures (Section 7)
□ AUTHZ-I compliance tested: uncertified mentor blocked from every authority action
□ AUTHZ-J compliance tested: minor protection enforced in all session paths
□ Matching engine tested: identical input → identical output (Section 9)
□ Calibration engine tested: pass/fail/missed scenarios with correct consequences (Section 10)
□ Bias index computed and ceiling enforcement tested (Section 14)
□ Dual-approval override flow tested: single-approver cannot complete override (Section 13)
□ Emergency intervention signal tested at ≤ 100ms P95 (Section 15)
□ Collusion detection tested: all 5 patterns detect correctly (Section 14)
□ Reputation formula tested against known inputs (Section 8)
□ Immutable audit log verified: no update/delete paths exist in schema
□ Multi-tenant isolation tested: cross-tenant query returns 0 rows
□ Certification status cache TTL tested: stale cache fails closed
□ Observability: all Prometheus metrics emitting (Section 19)
□ Security: OPA policies loaded and rejecting AUTHZ-I violations
□ Failure handling: all scenarios in Section 18 tested
□ Mentor command latency SLO ≤ 150ms P95 verified under load
□ Control signal latency SLO ≤ 100ms P95 verified under load
□ Helm chart deployed successfully in staging
□ Flyway migrations applied cleanly on fresh schema
□ All health endpoints returning 200

ABSENT GATE → STOP EXECUTION → NO PRODUCTION CLAIM PERMITTED
```

---

## SECTION 26 — FINAL SYSTEM DEFENSE

> "The `MENTOR_MATCHING_AGENT` eliminates subjective mentor assignment by converting mentor identity, certification, calibration, and matching into a deterministic trust-governed protocol where every mentor authority action is verified against live certification status, audited in an immutable log, bias-monitored continuously, and propagated into the platform-wide trust infrastructure — with zero single-approver overrides, zero uncertified supervision paths, and zero discretionary shortcuts."

---

```
DOCUMENT STATUS      : SEALED
VERSION              : 1.0.0
PLATFORM             : ECOSKILLER
AGENT                : MENTOR_MATCHING_AGENT
TIER                 : ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
ENGINE               : ANTIGRAVITY
LAST DECLARED BY     : Human Authority
MUTATION AUTHORITY   : Human declaration + version bump only
READY_FOR            : ANTIGRAVITY_PRODUCTION
```
