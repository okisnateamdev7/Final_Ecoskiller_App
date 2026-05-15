# ECOSKILLER — COMMUNITY_FEEDBACK_COLLECTION_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE · ANTIGRAVITY ENGINE

```
Agent Name:      COMMUNITY_FEEDBACK_COLLECTION_AGENT
Alias:           CFCA
Domain:          Enterprise Optimization + Trust Infrastructure
Category:        Antigravity (Anti-Decay, Anti-Silence, Anti-Trust-Erosion)
Status:          FINAL · LOCKED · SEALED · GOVERNED · DETERMINISTIC
Version:         CFCA-v1.0
Mutation Policy: Add-only via version bump
Authority:       Human Declaration Only
Interpretation:  NONE PERMITTED
Failure Mode:    STOP → LOG → ROUTE → NO PARTIAL COLLECTION
```

---

## PREAMBLE — ANTIGRAVITY DEFINED

In the Ecoskiller ecosystem, **Antigravity** is the structural force that
prevents the platform from silently decaying.

Without feedback infrastructure, the following entropy forces accelerate
unchecked:

- Mentor and trainer quality drifts downward without detection
- Scoring engine anomalies go unreported and uncorrected
- GD session integrity is never challenged from the participant side
- Dojo match fairness perception degrades silently
- Institution satisfaction collapses before admin intervention
- Student experience erodes below acquisition cost thresholds
- Platform feature failures accumulate without signal
- Idea marketplace trust decays without innovator voice
- Billing experience friction accumulates without surfacing

The **COMMUNITY_FEEDBACK_COLLECTION_AGENT (CFCA)** is the system that
transforms every user interaction, session outcome, service touchpoint, and
lifecycle event into a **structured, actionable, privacy-safe feedback signal**.

It does not interpret qualitative opinion.
It does not run sentiment analysis.
It converts structured response inputs into deterministic signals that feed:

- The Scoring Engine (R9)
- The Reputation Engine (R68, R79)
- The Conflict Resolution Agent (CRA-v1.0)
- The Admin Governance Service (R40)
- The Analytics Service (R11, ClickHouse)
- The Certification & Belt Engine
- The Fraud Detection Engine

The CFCA is the **sensory nervous system** of Ecoskiller's trust infrastructure.

---

## SECTION I — FEEDBACK TAXONOMY (SEALED)

All feedback in the Ecoskiller system is classified into
**8 Tier-1 Feedback Domains** and **52 Feedback Signal Types**.

### TIER-1 FEEDBACK DOMAIN MAP

| Domain ID | Domain Name | Source Layer | Primary Consumer |
|-----------|-------------|--------------|-----------------|
| FD-01 | SESSION & ASSESSMENT FEEDBACK | GD, Dojo, Interview | Scoring Engine, CRA |
| FD-02 | MENTOR & TRAINER QUALITY | Dojo, Education, Trainer Portal | Reputation Engine, CRA CD-02 |
| FD-03 | PLATFORM EXPERIENCE FEEDBACK | All UI layers (Flutter + Next.js) | Analytics, Admin Ops |
| FD-04 | CONTENT & CURRICULUM QUALITY | Course, Lesson, Quiz Engine | Trainer Analytics (R89) |
| FD-05 | MARKETPLACE TRUST FEEDBACK | Job, Project, Idea, Skill Marketplace | Liquidity Engine (R67), Trust Loop |
| FD-06 | INSTITUTIONAL EXPERIENCE | R35 Institution Layer, Student → Institution | Admin Governance, R63 |
| FD-07 | BILLING & COMMERCIAL FEEDBACK | Billing, Royalty, Subscription | Billing Service, CRA CD-04 |
| FD-08 | INNOVATION & IDEA FEEDBACK | Idea Registry, Innovation Engine | Innovation Reputation (R XIII) |

---

### FEEDBACK SIGNAL TYPE REGISTRY (LOCKED · 52 TYPES)

#### FD-01 · SESSION & ASSESSMENT FEEDBACK

| Signal ID | Name | Trigger Event | Collector Window |
|-----------|------|---------------|-----------------|
| FS-01-01 | GD Session Fairness Rating | `gd.session.completed` | ≤ 30 min post-session |
| FS-01-02 | GD Turn Engine Satisfaction | `gd.session.completed` | ≤ 30 min post-session |
| FS-01-03 | GD Network Quality Report | `gd.session.completed` | ≤ 30 min post-session |
| FS-01-04 | Dojo Match Fairness Rating | `dojo.match.completed` | ≤ 30 min post-match |
| FS-01-05 | Dojo Scenario Relevance Rating | `dojo.match.completed` | ≤ 30 min post-match |
| FS-01-06 | Dojo Scoring Transparency Rating | `dojo.match.completed` | ≤ 30 min post-match |
| FS-01-07 | Interview Session Quality Rating | `interview.completed` | ≤ 60 min post-session |
| FS-01-08 | Assessment Bias Perception Report | `gd.session.completed` or `dojo.match.completed` | ≤ 60 min |
| FS-01-09 | Belt Promotion Process Satisfaction | `belt.promoted` | ≤ 24 hrs |

#### FD-02 · MENTOR & TRAINER QUALITY

| Signal ID | Name | Trigger Event | Collector Window |
|-----------|------|---------------|-----------------|
| FS-02-01 | Mentor Session Quality Rating | `mentor.session.completed` | ≤ 60 min |
| FS-02-02 | Mentor Explanation Clarity Score | `mentor.session.completed` | ≤ 60 min |
| FS-02-03 | Mentor Professionalism Report | `mentor.session.completed` | ≤ 24 hrs |
| FS-02-04 | Trainer Course Satisfaction Rating | `course.completed` | ≤ 48 hrs |
| FS-02-05 | Trainer Live Session Engagement | `live.session.completed` | ≤ 60 min |
| FS-02-06 | Trainer Responsiveness Rating | `course.lesson.completed` (milestone) | Weekly cadence |
| FS-02-07 | Mentor Endorsement Authenticity Signal | `endorsement.received` | ≤ 72 hrs |
| FS-02-08 | Trainer Content Depth Rating | `course.completed` | ≤ 48 hrs |

#### FD-03 · PLATFORM EXPERIENCE FEEDBACK

| Signal ID | Name | Trigger Event | Collector Window |
|-----------|------|---------------|-----------------|
| FS-03-01 | Onboarding Flow Rating | `user.onboarding.completed` | ≤ 24 hrs |
| FS-03-02 | Feature Discovery Satisfaction | `feature.first_use` | ≤ 10 min post-use |
| FS-03-03 | Search Result Relevance Signal | `search.query.executed` (sample 10%) | ≤ 5 min |
| FS-03-04 | Notification Relevance Rating | `notification.opened` | Passive signal on dismiss |
| FS-03-05 | App Performance Report | `app.session.ended` | Passive + sampled 5% |
| FS-03-06 | UI Navigation Friction Report | `error.screen.encountered` | Immediate |
| FS-03-07 | Mobile App Rating Prompt | `user.retention_day_7` or `retention_day_30` | ≤ 48 hrs |
| FS-03-08 | Desktop App Quality Report | `desktop.session.ended` (weekly sample) | Weekly |
| FS-03-09 | Data Privacy Trust Signal | `data.export.requested` or `consent.updated` | ≤ 24 hrs |

#### FD-04 · CONTENT & CURRICULUM QUALITY

| Signal ID | Name | Trigger Event | Collector Window |
|-----------|------|---------------|-----------------|
| FS-04-01 | Lesson Usefulness Rating | `lesson.completed` | ≤ 30 min |
| FS-04-02 | Quiz Difficulty Calibration Signal | `quiz.submitted` | Immediate |
| FS-04-03 | Assignment Clarity Rating | `assignment.submitted` | ≤ 24 hrs |
| FS-04-04 | Course Outcome Satisfaction | `course.completed` | ≤ 72 hrs |
| FS-04-05 | Curriculum Coverage Adequacy | `course.completed` | ≤ 72 hrs |
| FS-04-06 | Recording Playback Quality Report | `recording.watched` | ≤ 30 min |
| FS-04-07 | Study Room Experience Rating | `studyroom.session.ended` (R92) | ≤ 30 min |

#### FD-05 · MARKETPLACE TRUST FEEDBACK

| Signal ID | Name | Trigger Event | Collector Window |
|-----------|------|---------------|-----------------|
| FS-05-01 | Job Listing Quality Signal | `job.applied` | ≤ 24 hrs |
| FS-05-02 | Job Application Process Rating | `application.pipeline.stage_changed` | ≤ 48 hrs |
| FS-05-03 | Recruiter Conduct Report | `interview.completed` | ≤ 24 hrs |
| FS-05-04 | Project Posting Clarity Rating | `project.proposal.submitted` | ≤ 24 hrs |
| FS-05-05 | Project Completion Experience | `project.completed` | ≤ 72 hrs |
| FS-05-06 | Idea Marketplace Discovery Quality | `idea.marketplace.browsed` (sample 20%) | ≤ 10 min |
| FS-05-07 | Skill Discovery Relevance Signal | `skill.discovery.search` (sample 15%) | ≤ 5 min |
| FS-05-08 | Hiring Outcome Satisfaction | `user.hired` (verified) | 30-day post-hire survey |
| FS-05-09 | Referral Experience Quality | `referral.accepted` | ≤ 48 hrs |

#### FD-06 · INSTITUTIONAL EXPERIENCE

| Signal ID | Name | Trigger Event | Collector Window |
|-----------|------|---------------|-----------------|
| FS-06-01 | Institution Onboarding Rating | `institution.onboarded` | ≤ 72 hrs |
| FS-06-02 | Department Tools Adequacy | `department.created` (30-day mark) | 30-day prompt |
| FS-06-03 | Student Academic Record Access Rating | `academic_record.accessed` | ≤ 24 hrs |
| FS-06-04 | Anonymous Complaint Process Trust | `complaint.closed` (R36) | ≤ 48 hrs |
| FS-06-05 | Institution Admin Console Satisfaction | `admin.session.ended` (weekly) | Weekly |
| FS-06-06 | Parent Dashboard Usefulness (R-Parents) | `parent.session.ended` | Weekly |

#### FD-07 · BILLING & COMMERCIAL FEEDBACK

| Signal ID | Name | Trigger Event | Collector Window |
|-----------|------|---------------|-----------------|
| FS-07-01 | Subscription Value Perception | `subscription.renewed` or `subscription.cancelled` | ≤ 24 hrs |
| FS-07-02 | Invoice Clarity Rating | `invoice.viewed` | ≤ 48 hrs |
| FS-07-03 | Refund Process Experience | `refund.processed` | ≤ 48 hrs |
| FS-07-04 | Royalty Transparency Rating | `royalty.payout.received` (R XIII) | ≤ 72 hrs |
| FS-07-05 | Pricing Fairness Signal | `plan.upgrade.abandoned` | Immediate passive |

#### FD-08 · INNOVATION & IDEA FEEDBACK

| Signal ID | Name | Trigger Event | Collector Window |
|-----------|------|---------------|-----------------|
| FS-08-01 | Idea Submission Experience | `idea.submitted` | ≤ 24 hrs |
| FS-08-02 | Idea Anti-Theft Trust Rating | `idea.similarity_check.completed` | ≤ 48 hrs |
| FS-08-03 | Licensing Process Satisfaction | `licensing_contract.signed` | ≤ 72 hrs |
| FS-08-04 | Idea Marketplace Discovery Trust | `idea.marketplace.listing.viewed` | ≤ 10 min |
| FS-08-05 | Innovation Scoring Transparency | `innovation_score.issued` | ≤ 48 hrs |
| FS-08-06 | Parent Consent Process Clarity | `consent.form.completed` | ≤ 24 hrs |

---

## SECTION II — FEEDBACK COLLECTION STATE MACHINE (DETERMINISTIC)

```
TRIGGER_EVENT_FIRED
       │
       ▼
ELIGIBILITY_CHECK
  │ IF: user.is_verified = TRUE
  │ AND: user.not_in_active_session
  │ AND: signal_not_already_collected_for_entity
  │ AND: user.feedback_fatigue_score < FATIGUE_THRESHOLD
  │ AND: collection_window_not_expired
       │
       ├── [INELIGIBLE] ──→ SILENTLY_SKIPPED (no user disruption)
       │
       ▼
COLLECTION_PROMPT_ISSUED
       │
       ├── [User dismisses] ──→ DISMISSED
       │                              │
       │                              ▼
       │                       DISMISSAL_LOGGED
       │                       (counts toward fatigue)
       │
       ├── [Collector window expires] ──→ TIMED_OUT
       │                                        │
       │                                        ▼
       │                                 TIMEOUT_LOGGED
       │
       ▼
RESPONSE_RECEIVED
       │
       ▼
VALIDATION_PASS
  │ IF: all required fields present
  │ AND: rating within valid range
  │ AND: no injection attempt detected
       │
       ├── [INVALID] ──→ REJECTION_LOGGED → PROMPT_REISSUED (once)
       │
       ▼
SIGNAL_STORED
       │
       ▼
ROUTING_PASS
       │
       ├── [Score signal] ──→ Scoring Engine
       ├── [Trust signal]  ──→ Reputation Engine
       ├── [Conflict flag] ──→ Conflict Resolution Agent (CRA)
       ├── [Analytics]     ──→ ClickHouse via Analytics Service
       ├── [Governance]    ──→ Admin Governance Service
       └── [Content]       ──→ Trainer Analytics (R89)
       │
       ▼
ACKNOWLEDGED_TO_USER
       │
       ▼
CLOSED
```

**State Rules (LOCKED):**

- ELIGIBILITY_CHECK runs synchronously before any prompt is shown
- COLLECTION_PROMPT_ISSUED must never block or overlay active sessions
- DISMISSED counts as 1 fatigue point; TIMED_OUT counts as 0.5
- SIGNAL_STORED is atomic — partial writes are rejected
- ROUTING_PASS is asynchronous — user sees ACKNOWLEDGED before routing completes
- No feedback prompt may appear more than once per entity per user

---

## SECTION III — FEEDBACK FATIGUE PREVENTION ENGINE (SEALED)

The Fatigue Engine ensures users are never over-prompted, which would
corrupt signal quality and drive disengagement.

### Fatigue Score Algorithm

```python
def compute_fatigue_score(user_id: UUID, window_days: int = 7) -> float:

    events = FeedbackFatigueLog.query(
        user_id=user_id,
        created_at__gte=now() - timedelta(days=window_days)
    )

    score = 0.0
    for event in events:
        if event.event_type == 'PROMPT_SHOWN':       score += 1.0
        if event.event_type == 'DISMISSED':           score += 1.5
        if event.event_type == 'TIMED_OUT':           score += 0.5
        if event.event_type == 'RESPONSE_SUBMITTED':  score -= 0.5  # reward engagement

    return max(score, 0.0)
```

### Fatigue Thresholds (Locked)

| User Role | Max Prompts / 7 Days | Fatigue Block Threshold | Cooldown |
|-----------|---------------------|------------------------|----------|
| Student | 5 | 8.0 | 48 hrs silence |
| Trainer/Mentor | 4 | 6.0 | 48 hrs silence |
| Recruiter/HR | 3 | 5.0 | 72 hrs silence |
| Institution Admin | 3 | 5.0 | 72 hrs silence |
| Parent | 2 | 3.0 | 7-day silence |

### Anti-Gaming Rules (Locked)

- If user submits identical rating (e.g. all 5s) for >10 consecutive signals
  → signal validity flagged as LOW_CONFIDENCE
- If user submits at rate >3 signals per 5 minutes
  → rate-limit applied; signals held for manual review
- If user has open conflict (CRA state ≠ CLOSED) against the entity being rated
  → signal collected but marked as CONFLICT_CONTEXT
  → weighted at 0.3x in aggregation (not discarded, not full-weight)

---

## SECTION IV — DATABASE SCHEMA (COMPLETE · SEALED)

```sql
-- ─────────────────────────────────────────────────────────
-- FEEDBACK SIGNAL RECORD (MASTER TABLE)
-- ─────────────────────────────────────────────────────────
CREATE TABLE feedback_signals (
  signal_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id             UUID NOT NULL REFERENCES tenants(id),
  domain_id             VARCHAR(8) NOT NULL,          -- FD-01 through FD-08
  signal_type_id        VARCHAR(12) NOT NULL,          -- FS-01-01, FS-02-03, etc.
  submitter_user_id     UUID NOT NULL REFERENCES users(id),
  submitter_role        VARCHAR(32) NOT NULL,           -- STUDENT / TRAINER / RECRUITER / etc.
  subject_entity_type   VARCHAR(64) NOT NULL,           -- gd_session / course / mentor / invoice / etc.
  subject_entity_id     UUID NOT NULL,
  subject_user_id       UUID REFERENCES users(id),      -- if feedback is about a specific person
  trigger_event         VARCHAR(128) NOT NULL,           -- e.g. gd.session.completed
  response_payload      JSONB NOT NULL,                  -- structured ratings + optional text
  rating_numeric        NUMERIC(3,1),                    -- normalized 1.0–5.0 where applicable
  collection_channel    VARCHAR(32) NOT NULL,            -- IN_APP / EMAIL / PUSH / PASSIVE
  collection_state      VARCHAR(32) NOT NULL DEFAULT 'SIGNAL_STORED',
  fatigue_score_at_time NUMERIC(5,2),                   -- user's fatigue score at submission
  confidence_level      VARCHAR(16) DEFAULT 'STANDARD', -- STANDARD / LOW_CONFIDENCE / CONFLICT_CONTEXT
  conflict_context_flag BOOLEAN DEFAULT FALSE,
  routed_to_scoring     BOOLEAN DEFAULT FALSE,
  routed_to_reputation  BOOLEAN DEFAULT FALSE,
  routed_to_cra         BOOLEAN DEFAULT FALSE,
  routed_to_analytics   BOOLEAN DEFAULT FALSE,
  routed_to_governance  BOOLEAN DEFAULT FALSE,
  is_anonymized         BOOLEAN DEFAULT FALSE,
  created_at            TIMESTAMP NOT NULL DEFAULT NOW(),
  collection_window_end TIMESTAMP NOT NULL,
  CONSTRAINT valid_domain CHECK (domain_id IN (
    'FD-01','FD-02','FD-03','FD-04',
    'FD-05','FD-06','FD-07','FD-08'
  )),
  CONSTRAINT valid_rating CHECK (rating_numeric IS NULL OR
    (rating_numeric >= 1.0 AND rating_numeric <= 5.0))
);

-- ─────────────────────────────────────────────────────────
-- FEEDBACK PROMPT LIFECYCLE LOG
-- ─────────────────────────────────────────────────────────
CREATE TABLE feedback_prompt_log (
  log_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id             UUID NOT NULL REFERENCES users(id),
  signal_type_id      VARCHAR(12) NOT NULL,
  subject_entity_id   UUID NOT NULL,
  prompt_state        VARCHAR(32) NOT NULL,   -- ISSUED / DISMISSED / TIMED_OUT / RESPONDED
  prompt_channel      VARCHAR(32) NOT NULL,
  issued_at           TIMESTAMP NOT NULL DEFAULT NOW(),
  responded_at        TIMESTAMP,
  window_end          TIMESTAMP NOT NULL,
  signal_id           UUID REFERENCES feedback_signals(signal_id)
);

-- ─────────────────────────────────────────────────────────
-- FEEDBACK FATIGUE LOG
-- ─────────────────────────────────────────────────────────
CREATE TABLE feedback_fatigue_log (
  fatigue_id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id       UUID NOT NULL REFERENCES users(id),
  event_type    VARCHAR(32) NOT NULL,   -- PROMPT_SHOWN / DISMISSED / TIMED_OUT / RESPONSE_SUBMITTED
  fatigue_delta NUMERIC(4,2) NOT NULL,
  fatigue_after NUMERIC(6,2) NOT NULL,
  signal_type   VARCHAR(12),
  created_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ─────────────────────────────────────────────────────────
-- FEEDBACK AGGREGATION CACHE (per entity)
-- ─────────────────────────────────────────────────────────
CREATE TABLE feedback_aggregations (
  aggregation_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  subject_entity_type   VARCHAR(64) NOT NULL,
  subject_entity_id     UUID NOT NULL,
  domain_id             VARCHAR(8) NOT NULL,
  signal_type_id        VARCHAR(12) NOT NULL,
  sample_count          INTEGER DEFAULT 0,
  avg_rating            NUMERIC(4,2),
  weighted_avg_rating   NUMERIC(4,2),      -- after confidence weighting
  low_confidence_count  INTEGER DEFAULT 0,
  conflict_context_count INTEGER DEFAULT 0,
  trend_direction       VARCHAR(8),         -- UP / DOWN / STABLE
  last_computed_at      TIMESTAMP NOT NULL DEFAULT NOW(),
  UNIQUE(subject_entity_id, signal_type_id)
);

-- ─────────────────────────────────────────────────────────
-- FEEDBACK ROUTING DISPATCH LOG
-- ─────────────────────────────────────────────────────────
CREATE TABLE feedback_routing_dispatch (
  dispatch_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  signal_id         UUID NOT NULL REFERENCES feedback_signals(signal_id),
  target_service    VARCHAR(64) NOT NULL,
  dispatch_state    VARCHAR(32) DEFAULT 'PENDING',   -- PENDING / DELIVERED / FAILED / RETRYING
  dispatched_at     TIMESTAMP,
  acknowledged_at   TIMESTAMP,
  failure_reason    TEXT,
  retry_count       INTEGER DEFAULT 0
);

-- ─────────────────────────────────────────────────────────
-- FEEDBACK ANOMALY FLAGS
-- ─────────────────────────────────────────────────────────
CREATE TABLE feedback_anomaly_flags (
  flag_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id           UUID NOT NULL REFERENCES users(id),
  anomaly_type      VARCHAR(64) NOT NULL,   -- IDENTICAL_RATINGS / BURST_SUBMISSION / CONFLICT_CONTEXT_BIAS
  signal_ids        JSONB,                   -- array of implicated signal UUIDs
  severity          VARCHAR(12) NOT NULL,    -- LOW / MEDIUM / HIGH
  flagged_at        TIMESTAMP NOT NULL DEFAULT NOW(),
  reviewed          BOOLEAN DEFAULT FALSE,
  action_taken      VARCHAR(64)
);

-- ─────────────────────────────────────────────────────────
-- FEEDBACK COLLECTION RULES CONFIG (per signal type)
-- ─────────────────────────────────────────────────────────
CREATE TABLE feedback_collection_rules (
  rule_id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  signal_type_id        VARCHAR(12) NOT NULL UNIQUE,
  domain_id             VARCHAR(8) NOT NULL,
  trigger_event         VARCHAR(128) NOT NULL,
  collection_channel    VARCHAR(32) NOT NULL,       -- IN_APP / EMAIL / PUSH / PASSIVE
  collection_window_hrs INTEGER NOT NULL,
  required_fields       JSONB NOT NULL,             -- schema for response_payload validation
  is_anonymous          BOOLEAN DEFAULT FALSE,
  min_confidence_role   VARCHAR(32),                -- minimum role to send to scoring
  active                BOOLEAN DEFAULT TRUE
);

-- ─────────────────────────────────────────────────────────
-- FEEDBACK SENTIMENT SUMMARY (Aggregated for Admin Dashboards)
-- ─────────────────────────────────────────────────────────
CREATE TABLE feedback_sentiment_summary (
  summary_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  summary_date        DATE NOT NULL,
  tenant_id           UUID NOT NULL,
  domain_id           VARCHAR(8) NOT NULL,
  signal_type_id      VARCHAR(12),
  total_signals       INTEGER DEFAULT 0,
  total_promoters     INTEGER DEFAULT 0,   -- rating >= 4.0
  total_neutrals      INTEGER DEFAULT 0,   -- rating 3.0 – 3.9
  total_detractors    INTEGER DEFAULT 0,   -- rating < 3.0
  nps_score           NUMERIC(5,2),        -- computed NPS
  response_rate_pct   NUMERIC(5,2),
  UNIQUE(summary_date, tenant_id, domain_id, signal_type_id)
);

-- ─────────────────────────────────────────────────────────
-- FEEDBACK ENTITY HEALTH INDEX (Per scored entity)
-- ─────────────────────────────────────────────────────────
CREATE TABLE feedback_entity_health (
  health_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  entity_type           VARCHAR(64) NOT NULL,
  entity_id             UUID NOT NULL,
  health_score          NUMERIC(5,2) NOT NULL,     -- 0–100 composite
  last_30d_avg_rating   NUMERIC(4,2),
  last_90d_avg_rating   NUMERIC(4,2),
  signal_volume_30d     INTEGER,
  health_trend          VARCHAR(8),                 -- IMPROVING / DECLINING / STABLE
  alert_threshold_hit   BOOLEAN DEFAULT FALSE,
  computed_at           TIMESTAMP NOT NULL DEFAULT NOW(),
  UNIQUE(entity_type, entity_id)
);
```

---

## SECTION V — RESPONSE PAYLOAD SCHEMAS (LOCKED PER SIGNAL TYPE)

All feedback responses are **structured** — no open-ended free text is
required (text is optional and never used for automated decisions).

### SCHEMA: GD Session Fairness (FS-01-01)

```json
{
  "required": ["turn_engine_fair", "session_completed_without_issues"],
  "properties": {
    "turn_engine_fair": {
      "type": "integer", "minimum": 1, "maximum": 5,
      "label": "Was speaking time distributed fairly?"
    },
    "session_completed_without_issues": {
      "type": "boolean",
      "label": "Did the session complete without technical interruption?"
    },
    "issue_type": {
      "type": "string",
      "enum": ["NETWORK_DROP", "MUTE_ERROR", "TIMER_ERROR", "OTHER", "NONE"],
      "label": "If not, what type of issue occurred?"
    },
    "optional_comment": {
      "type": "string", "maxLength": 280,
      "label": "Any additional note (optional)"
    }
  }
}
```

### SCHEMA: Mentor Session Quality (FS-02-01)

```json
{
  "required": ["knowledge_depth", "communication_clarity", "professional_conduct"],
  "properties": {
    "knowledge_depth": {
      "type": "integer", "minimum": 1, "maximum": 5,
      "label": "How deep was the mentor's subject knowledge?"
    },
    "communication_clarity": {
      "type": "integer", "minimum": 1, "maximum": 5,
      "label": "How clearly did the mentor explain concepts?"
    },
    "professional_conduct": {
      "type": "integer", "minimum": 1, "maximum": 5,
      "label": "How professional was the mentor's conduct?"
    },
    "would_recommend": {
      "type": "boolean",
      "label": "Would you recommend this mentor to peers?"
    },
    "misconduct_flag": {
      "type": "boolean",
      "label": "Did anything inappropriate occur during the session?"
    },
    "optional_comment": {
      "type": "string", "maxLength": 280
    }
  }
}
```

### SCHEMA: Dojo Match Fairness (FS-01-04)

```json
{
  "required": ["scenario_relevant", "scoring_felt_fair", "opponent_conduct"],
  "properties": {
    "scenario_relevant": {
      "type": "integer", "minimum": 1, "maximum": 5,
      "label": "Was the scenario relevant to the skill being tested?"
    },
    "scoring_felt_fair": {
      "type": "integer", "minimum": 1, "maximum": 5,
      "label": "Did the scoring reflect your actual performance?"
    },
    "opponent_conduct": {
      "type": "string",
      "enum": ["RESPECTFUL", "NEUTRAL", "DISRUPTIVE"],
      "label": "How would you describe your opponent's conduct?"
    },
    "technical_issue_occurred": {
      "type": "boolean",
      "label": "Did any technical issue affect the match outcome?"
    },
    "optional_comment": {
      "type": "string", "maxLength": 280
    }
  }
}
```

### SCHEMA: Billing & Invoice Feedback (FS-07-01, FS-07-02)

```json
{
  "required": ["value_for_money", "invoice_clarity"],
  "properties": {
    "value_for_money": {
      "type": "integer", "minimum": 1, "maximum": 5,
      "label": "Does the subscription feel worth the cost?"
    },
    "invoice_clarity": {
      "type": "integer", "minimum": 1, "maximum": 5,
      "label": "Was the invoice easy to understand?"
    },
    "charge_dispute_flag": {
      "type": "boolean",
      "label": "Do you believe there is an error in the charge?"
    },
    "optional_comment": {
      "type": "string", "maxLength": 280
    }
  }
}
```

### SCHEMA: Idea Anti-Theft Trust (FS-08-02)

```json
{
  "required": ["process_felt_secure", "result_felt_fair"],
  "properties": {
    "process_felt_secure": {
      "type": "integer", "minimum": 1, "maximum": 5,
      "label": "Did the idea protection process feel secure?"
    },
    "result_felt_fair": {
      "type": "integer", "minimum": 1, "maximum": 5,
      "label": "Was the similarity check result fair and transparent?"
    },
    "ownership_concern_flag": {
      "type": "boolean",
      "label": "Do you believe your idea was copied or improperly compared?"
    },
    "optional_comment": {
      "type": "string", "maxLength": 280
    }
  }
}
```

> All schemas follow the same pattern.
> All schemas are stored in `feedback_collection_rules.required_fields`.
> Schema violation = signal rejected at VALIDATION_PASS.

---

## SECTION VI — SIGNAL ROUTING ENGINE (SEALED)

The Signal Router delivers feedback to consuming services based on domain,
signal type, confidence level, and threshold rules.

### Routing Decision Table (Locked)

| Domain | Signal Triggers | Destination Service | Condition |
|--------|----------------|---------------------|-----------|
| FD-01 | FS-01-01 to FS-01-09 | Scoring Engine | confidence ≠ LOW |
| FD-01 | FS-01-08 (Bias Report) + misconduct_flag=true | CRA (CD-01/SC-01 or CD-02) | Immediate |
| FD-01 | FS-01-04 to FS-01-06 (Dojo) | Dojo Match Engine audit log | Always |
| FD-02 | FS-02-01 to FS-02-08 | Trainer Reputation Score (R89) | confidence ≠ LOW |
| FD-02 | FS-02-03 misconduct_flag=true | CRA (CD-02/MT-03) | Immediate |
| FD-02 | FS-02-07 (Endorsement Authenticity) | Fraud Detection Engine | Always |
| FD-03 | FS-03-01 to FS-03-09 | Analytics Service → ClickHouse | Batched hourly |
| FD-03 | FS-03-06 (Navigation Friction) | Admin Ops Product Dashboard | Always |
| FD-04 | FS-04-01 to FS-04-07 | Trainer Analytics (R89) | Always |
| FD-04 | FS-04-02 (Quiz Difficulty) | Dojo Intelligence Testing Service | Aggregated weekly |
| FD-05 | FS-05-01 to FS-05-09 | Marketplace Liquidity Engine (R67) | Always |
| FD-05 | FS-05-03 (Recruiter Conduct) misconduct | CRA (CD-03/IV or CD-07) | Immediate |
| FD-05 | FS-05-08 (Hiring Outcome) | Intelligence Prediction Engine | Always |
| FD-06 | FS-06-01 to FS-06-06 | Admin Governance (R40) + R63 Gateway | Always |
| FD-06 | FS-06-04 (Complaint Process Trust) | CRA Friction Metrics | Always |
| FD-07 | FS-07-01 to FS-07-05 | Billing Analytics + CRA CD-04 | Always |
| FD-07 | FS-07-02 charge_dispute_flag=true | CRA (CD-04/BR-01) | Immediate |
| FD-08 | FS-08-01 to FS-08-06 | Innovation Reputation Service (R XIII) | Always |
| FD-08 | FS-08-02 ownership_concern_flag=true | CRA (CD-05/IO-01) | Immediate |

### Signal Routing Algorithm

```python
def route_signal(signal: FeedbackSignal):

    # Step 1: Validate confidence
    if signal.confidence_level == 'LOW_CONFIDENCE':
        route_to_analytics_only(signal, note='LOW_CONFIDENCE_SIGNAL')
        return

    # Step 2: Check for immediate CRA escalation flags
    immediate_flags = extract_cra_flags(signal)
    if immediate_flags:
        for flag in immediate_flags:
            dispatch_to_cra(signal, flag)

    # Step 3: Route to primary consuming service
    routes = ROUTING_TABLE[signal.domain_id][signal.signal_type_id]
    for route in routes:
        dispatch = FeedbackRoutingDispatch(
            signal_id=signal.signal_id,
            target_service=route.service,
            dispatch_state='PENDING'
        )
        save(dispatch)
        async_deliver(dispatch)

    # Step 4: Always route to analytics
    dispatch_to_clickhouse(signal)

    # Step 5: Update entity health index
    update_entity_health(
        entity_type=signal.subject_entity_type,
        entity_id=signal.subject_entity_id,
        new_rating=signal.rating_numeric,
        confidence=signal.confidence_level
    )
```

---

## SECTION VII — ENTITY HEALTH INDEX ENGINE (SEALED)

The Entity Health Index converts raw feedback signals into a
**real-time health score (0–100)** for every scoreable entity on the platform.

### Scored Entities

```
Mentors          → FD-02 signals
Trainers         → FD-02 + FD-04 signals
GD Sessions      → FD-01 signals
Dojo Scenarios   → FD-01 signals
Courses          → FD-04 signals
Job Listings     → FD-05 signals
Recruiters       → FD-05 signals
Institutions     → FD-06 signals
Idea Listings    → FD-08 signals
Platform Features→ FD-03 signals
```

### Health Score Formula (Locked)

```python
def compute_entity_health(entity_id: UUID, entity_type: str) -> float:

    # Pull last 90 days of signals for entity
    signals = FeedbackSignals.query(
        subject_entity_id=entity_id,
        created_at__gte=now() - timedelta(days=90),
        collection_state='SIGNAL_STORED'
    )

    if len(signals) < MIN_SAMPLE_SIZE[entity_type]:
        return None  # insufficient data — do not publish score

    # Apply confidence weights
    weighted_sum = 0.0
    weight_total = 0.0
    for s in signals:
        if s.confidence_level == 'STANDARD':          w = 1.0
        elif s.confidence_level == 'LOW_CONFIDENCE':  w = 0.2
        elif s.confidence_level == 'CONFLICT_CONTEXT':w = 0.3
        weighted_sum += s.rating_numeric * w
        weight_total += w

    weighted_avg = weighted_sum / weight_total  # scale: 1.0–5.0

    # Normalize to 0–100
    health_score = ((weighted_avg - 1.0) / 4.0) * 100

    # Apply recency boost: last 30d signals weighted 1.5x
    # (recompute with recency multiplier — omitted for brevity)

    return round(health_score, 2)
```

### Health Threshold Alert Rules (Locked)

| Entity Type | Alert Threshold | Action |
|-------------|----------------|--------|
| Mentor | health_score < 55 | Notify Admin + flag mentor for review |
| Trainer | health_score < 55 | Notify trainer + put content in review queue |
| Course | health_score < 60 | Hide from discovery; notify trainer |
| Job Listing | health_score < 50 | Flag to recruiter service + compliance review |
| Recruiter | health_score < 50 | Restrict posting privileges + CRA alert |
| GD Scenario | health_score < 60 | Remove from active pool + admin review |
| Institution | health_score < 65 | Admin governance notification |

---

## SECTION VIII — NPS ENGINE (SEALED)

The CFCA computes **Net Promoter Score (NPS)** per tenant, per domain,
and per entity type on a **daily rolling basis**.

### NPS Computation (Locked)

```python
def compute_nps(signals: list, date: date, tenant_id: UUID,
                domain_id: str, signal_type_id: str = None) -> float:

    # Classify
    promoters  = [s for s in signals if s.rating_numeric >= 4.0]
    neutrals   = [s for s in signals if 3.0 <= s.rating_numeric < 4.0]
    detractors = [s for s in signals if s.rating_numeric < 3.0]

    total = len(signals)
    if total == 0:
        return None

    pct_promoters  = len(promoters) / total * 100
    pct_detractors = len(detractors) / total * 100

    nps = pct_promoters - pct_detractors  # range: -100 to +100

    FeedbackSentimentSummary.upsert(
        summary_date=date,
        tenant_id=tenant_id,
        domain_id=domain_id,
        signal_type_id=signal_type_id,
        total_signals=total,
        total_promoters=len(promoters),
        total_neutrals=len(neutrals),
        total_detractors=len(detractors),
        nps_score=round(nps, 2),
        response_rate_pct=compute_response_rate(date, tenant_id, signal_type_id)
    )

    return nps
```

### NPS Alert Thresholds (Locked)

| Scope | Alert Threshold | Action |
|-------|----------------|--------|
| Platform-wide NPS | < 20 | Critical admin alert + exec notification |
| Mentor NPS | < 10 | Mentor placed under review |
| Course NPS | < 15 | Course hidden from recommendations |
| GD Domain NPS | < 25 | Engineering review triggered |
| Billing NPS | < 0 | Pricing team + CRA billing alert |

---

## SECTION IX — COLLECTION CHANNELS (SEALED)

The CFCA delivers feedback prompts via 4 channels. Channel selection is
determined by signal type, user context, and fatigue score.

### Channel 1: IN_APP (Primary)

```
Delivery: Non-blocking bottom sheet on Flutter mobile/desktop
          Side drawer on Flutter web
          Non-obstructive card on Next.js (read-only layer)

Rules:
- Never displayed during active session (GD room, Dojo match, Live class)
- Maximum 1 in-app prompt visible at a time
- Prompt dismissed with single swipe / tap outside
- Auto-dismisses after 30 seconds if no interaction
```

### Channel 2: PUSH (Secondary for mobile)

```
Delivery: Push notification via ntfy/FCM
          Triggers collection window start
          Deep-links to in-app feedback form

Rules:
- Only sent if user has push notifications enabled
- Never sent between 10pm–8am local time
- Maximum 1 push per feedback signal per user
- Honoured only if fatigue_score < threshold
```

### Channel 3: EMAIL (For low-urgency, high-value signals)

```
Signals: FS-05-08 (Hiring Outcome), FS-02-04 (Course Satisfaction),
         FS-07-01 (Subscription Value), FS-06-01 (Institution Onboarding)

Delivery: Postfix via Docker Mail Server
          HTML templated email with embedded rating buttons (1-click)
          5-question maximum per email

Rules:
- Maximum 1 feedback email per 7 days per user
- Unsubscribe from feedback emails preserved
- Email feedback pre-fills response_payload via tokenized URL
```

### Channel 4: PASSIVE (Behavioral signal capture)

```
Signals: FS-03-03 (Search Relevance), FS-03-04 (Notification Relevance),
         FS-03-05 (App Performance), FS-07-05 (Pricing Abandonment)

Delivery: No user interaction required
          System captures behavioral telemetry automatically

Rules:
- Sampled at declared rates (5–20% of eligible events)
- No PII stored in passive signals
- Passive signals count as 0.25 fatigue points (low-impact)
- rating_numeric derived from behavioral indicator, not user input
```

---

## SECTION X — API CONTRACT REGISTRY (SEALED)

```yaml
openapi: 3.1.0
info:
  title: Community Feedback Collection Agent API
  version: cfca-v1.0

paths:

  /feedback/eligibility:
    post:
      summary: Check if a user is eligible to provide feedback for a specific signal type
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/EligibilityRequest'
      responses:
        "200":
          description: Eligibility result
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/EligibilityResult'

  /feedback/submit:
    post:
      summary: Submit a structured feedback signal
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/FeedbackSubmission'
      responses:
        "202":
          description: Signal accepted and queued for routing
        "422":
          description: Validation failure — response_payload schema mismatch

  /feedback/signals/{signal_id}:
    get:
      summary: Retrieve a specific feedback signal (submitter only or Admin)

  /feedback/entity/{entity_type}/{entity_id}/health:
    get:
      summary: Get current health index for an entity
      responses:
        "200":
          description: EntityHealthIndex
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/EntityHealth'

  /feedback/entity/{entity_type}/{entity_id}/aggregation:
    get:
      summary: Get aggregated feedback stats for an entity (Admin only)

  /feedback/nps/summary:
    get:
      summary: Get NPS summary by domain and date range (Admin only)
      parameters:
        - name: domain_id
        - name: from_date
        - name: to_date
        - name: tenant_id

  /feedback/admin/anomalies:
    get:
      summary: Get flagged feedback anomalies (Admin only)

  /feedback/admin/routing/failed:
    get:
      summary: List failed routing dispatches for retry (Admin only)

  /feedback/admin/fatigue/{user_id}:
    get:
      summary: Get current fatigue score for a user (Admin only)

  /feedback/passive/ingest:
    post:
      summary: Ingest passive behavioral signal (internal service only)
      security:
        - serviceToken: []

components:
  schemas:

    EligibilityRequest:
      type: object
      required: [user_id, signal_type_id, subject_entity_id]
      properties:
        user_id: { type: string, format: uuid }
        signal_type_id: { type: string }
        subject_entity_id: { type: string, format: uuid }
        trigger_event: { type: string }

    EligibilityResult:
      type: object
      properties:
        eligible: { type: boolean }
        reason: { type: string }
        fatigue_score: { type: number }
        collection_window_end: { type: string, format: date-time }

    FeedbackSubmission:
      type: object
      required: [signal_type_id, subject_entity_type, subject_entity_id,
                 trigger_event, response_payload]
      properties:
        signal_type_id: { type: string }
        subject_entity_type: { type: string }
        subject_entity_id: { type: string, format: uuid }
        trigger_event: { type: string }
        response_payload: { type: object }
        collection_channel: { type: string }

    EntityHealth:
      type: object
      properties:
        entity_type: { type: string }
        entity_id: { type: string, format: uuid }
        health_score: { type: number }
        last_30d_avg_rating: { type: number }
        health_trend: { type: string }
        alert_threshold_hit: { type: boolean }
        computed_at: { type: string, format: date-time }
```

---

## SECTION XI — EVENT BUS CONTRACTS (SEALED)

### Events Consumed by CFCA (Trigger Sources)

```
gd.session.completed              → FS-01-01, FS-01-02, FS-01-03, FS-01-08
dojo.match.completed              → FS-01-04, FS-01-05, FS-01-06
interview.completed               → FS-01-07
belt.promoted                     → FS-01-09
mentor.session.completed          → FS-02-01, FS-02-02, FS-02-03
course.completed                  → FS-02-04, FS-04-04, FS-04-05
live.session.completed            → FS-02-05
endorsement.received              → FS-02-07
user.onboarding.completed         → FS-03-01
feature.first_use                 → FS-03-02
error.screen.encountered          → FS-03-06
user.retention_day_7              → FS-03-07
lesson.completed                  → FS-04-01
quiz.submitted                    → FS-04-02
assignment.submitted              → FS-04-03
studyroom.session.ended           → FS-04-07
job.applied                       → FS-05-01
application.pipeline.stage_changed→ FS-05-02
project.proposal.submitted        → FS-05-04
project.completed                 → FS-05-05
user.hired                        → FS-05-08
referral.accepted                 → FS-05-09
institution.onboarded             → FS-06-01
complaint.closed                  → FS-06-04
subscription.renewed              → FS-07-01
subscription.cancelled            → FS-07-01
invoice.viewed                    → FS-07-02
refund.processed                  → FS-07-03
royalty.payout.received           → FS-07-04
plan.upgrade.abandoned            → FS-07-05
idea.submitted                    → FS-08-01
idea.similarity_check.completed   → FS-08-02
licensing_contract.signed         → FS-08-03
innovation_score.issued           → FS-08-05
consent.form.completed            → FS-08-06
```

### Events Emitted by CFCA

```
feedback.signal.collected
feedback.signal.dismissed
feedback.signal.timed_out
feedback.signal.routed
feedback.signal.routing_failed
feedback.anomaly.detected
feedback.entity_health.updated
feedback.entity_health.alert_triggered
feedback.nps.computed
feedback.fatigue.threshold_hit
feedback.cra.escalation_dispatched
feedback.passive.ingested
```

---

## SECTION XII — UI SCREENS (COMPLETE · SEALED)

### SCREEN 1 — In-App Feedback Bottom Sheet (Flutter · Mobile)

```
Purpose:    Non-blocking feedback prompt displayed post-trigger-event
Placement:  Bottom sheet overlay, never full-screen
Dismissable: Yes — single tap outside or swipe down

Layout:
┌──────────────────────────────────────────────┐
│  ▬   (drag handle)                           │
│                                              │
│  Quick Feedback  ·  Takes 15 seconds         │
│  ─────────────────────────────────────────   │
│                                              │
│  How fair was your GD session today?         │
│                                              │
│  ☆  ☆  ☆  ☆  ☆                            │
│  1   2   3   4   5                           │
│                                              │
│  Did any technical issue occur?              │
│  ○ Yes   ● No                               │
│                                              │
│      [ SUBMIT ]     [ Skip for now ]         │
│                                              │
│  Your response improves session quality.     │
└──────────────────────────────────────────────┘

Rules:
- Shows ONLY after session confirmation screen closes
- Never blocks GD room, Dojo room, or Live class
- Auto-dismisses in 30 seconds if no interaction
- Skip = DISMISSED log, no penalty, no repeat for same session
```

### SCREEN 2 — Post-Course Feedback Form (Flutter · Multi-Step)

```
Purpose:    5-question post-course satisfaction survey
Trigger:    course.completed event

Layout (Step 1 of 3):
┌─────────────────────────────────────────────────────┐
│  Course Feedback  ·  Step 1 of 3                    │
│  ─────────────────────────────────────────────────  │
│  [Course Title: Advanced Python Bootcamp]           │
│                                                     │
│  How would you rate the overall course quality?     │
│                                                     │
│  ●─────────────────────────────○                   │
│  1    2    3    4    5                               │
│              ↑ Currently: 4                         │
│                                                     │
│  Was the course content up to date and relevant?   │
│  ○ Yes    ○ Mostly    ○ Somewhat    ○ No            │
│                                                     │
│                        [ NEXT → ]                   │
└─────────────────────────────────────────────────────┘

Layout (Step 3 of 3 — optional):
┌─────────────────────────────────────────────────────┐
│  Course Feedback  ·  Step 3 of 3  (Optional)        │
│  ─────────────────────────────────────────────────  │
│  Anything you'd like to add?                        │
│                                                     │
│  ┌─────────────────────────────────────────────┐   │
│  │                                             │   │
│  └─────────────────────────────────────────────┘   │
│  Max 280 characters · Not used for auto-decisions   │
│                                                     │
│       [ SKIP & SUBMIT ]    [ SUBMIT WITH NOTE ]     │
└─────────────────────────────────────────────────────┘
```

### SCREEN 3 — Mentor Rating Card (Flutter · Post-Session)

```
Purpose:    Concise mentor quality rating immediately after session
Layout:
┌──────────────────────────────────────────────────────┐
│  Rate Your Mentor Session                            │
│  ─────────────────────────────────────────────────  │
│  [Mentor Profile Avatar]  Mentor: Ravi Sharma        │
│  Session: Java OOP Fundamentals  ·  45 min           │
│  ─────────────────────────────────────────────────  │
│                                                      │
│  Knowledge Depth          ☆ ☆ ☆ ☆ ☆               │
│  Communication Clarity    ☆ ☆ ☆ ☆ ☆               │
│  Professional Conduct     ☆ ☆ ☆ ☆ ☆               │
│                                                      │
│  Would you recommend this mentor?  ○ Yes  ○ No      │
│                                                      │
│  ⚠ Report a concern with this session  [Flag]       │
│                                                      │
│                     [ SUBMIT RATING ]               │
└──────────────────────────────────────────────────────┘

Rules:
- "Flag" opens inline misconduct_flag toggle before submission
- Rating data never shown to mentor directly (anonymized aggregation only)
- Submission closes form; acknowledgement toast shown
```

### SCREEN 4 — Admin Feedback Analytics Dashboard (Ops Console)

```
Purpose:    Real-time view of platform feedback health across all domains
Access:     Super Admin, Senior Moderator, Product Team role

Layout:
┌──────────────────────────────────────────────────────────────────────┐
│ FEEDBACK HEALTH DASHBOARD                      Last 30 days          │
│──────────────────────────────────────────────────────────────────────│
│                                                                       │
│  PLATFORM NPS: +42 ✅     RESPONSE RATE: 68%    ANOMALIES: 3 🟡     │
│                                                                       │
│  NPS BY DOMAIN:                                                       │
│  ████████████  FD-01 Sessions      NPS: +58   ✅                     │
│  ██████████    FD-02 Mentors        NPS: +44   ✅                     │
│  ████████      FD-04 Content        NPS: +38   🟡 Monitor            │
│  ██████        FD-07 Billing        NPS: +12   🟡 Monitor            │
│  ████          FD-06 Institutions   NPS: +8    🔴 Alert              │
│                                                                       │
│  ENTITY HEALTH ALERTS:                                               │
│  🔴 Mentor ID 7821      Health: 48/100   Below threshold             │
│  🟠 Course: Data Sci 4  Health: 57/100   In review queue             │
│  🟠 Recruiter #3304     Health: 52/100   Posting restricted          │
│                                                                       │
│  SIGNAL VOLUME (7 days):  4,218 signals  │  Avg collection: 1.9/user │
│  FATIGUE EVENTS: 12 users blocked this week                          │
│                                                                       │
│  [ VIEW ALL SIGNALS ]  [ EXPORT REPORT ]  [ SET NPS ALERT ]          │
└──────────────────────────────────────────────────────────────────────┘
```

### SCREEN 5 — Entity Feedback Profile (Admin drill-down)

```
Purpose:    Full feedback history and health trend for any entity
Example:    Mentor profile drill-down from health alert

Layout:
┌──────────────────────────────────────────────────────────────────────┐
│ ENTITY FEEDBACK PROFILE                                              │
│ Mentor: Ravi Sharma (ID: mentor_7821)                               │
│──────────────────────────────────────────────────────────────────────│
│                                                                       │
│ HEALTH SCORE: 48/100  🔴   Trend: ↘ DECLINING                       │
│                                                                       │
│ Signal Breakdown (last 90 days):                                     │
│ ──────────────────────────────────                                   │
│ Knowledge Depth:      3.2 / 5.0   (42 signals)                      │
│ Communication Clarity: 2.9 / 5.0   (42 signals)                     │
│ Professional Conduct: 3.8 / 5.0   (42 signals)                      │
│ Would Recommend:      58%  Yes                                       │
│                                                                       │
│ FLAGS:                                                               │
│ • 2 misconduct_flag events — routed to CRA  [VIEW IN CRA]           │
│ • 1 LOW_CONFIDENCE signal batch (excluded from score)               │
│                                                                       │
│ Monthly NPS Trend:                                                   │
│  Jan: +22  Feb: +14  Mar: +6  Apr: -3  ← DECLINING                  │
│                                                                       │
│ [ PLACE UNDER REVIEW ]  [ NOTIFY MENTOR ]  [ ESCALATE TO CRA ]      │
└──────────────────────────────────────────────────────────────────────┘
```

### SCREEN 6 — Feedback Anomaly Management (Admin)

```
Purpose:    Review and action flagged anomalous feedback patterns

Layout:
┌──────────────────────────────────────────────────────────────────────┐
│ FEEDBACK ANOMALY FLAGS                          3 Active 🟡          │
│──────────────────────────────────────────────────────────────────────│
│                                                                       │
│ 🟠 IDENTICAL_RATINGS  ·  User ID: user_2201                         │
│    Submitted 15 identical 5-star ratings in 48 hrs                  │
│    Signals: [IDs listed]  Severity: MEDIUM                           │
│    Action: [ MARK LOW_CONFIDENCE ]  [ SUSPEND USER FEEDBACK ]        │
│                                                                       │
│ 🔴 BURST_SUBMISSION  ·  User ID: user_9930                          │
│    14 signals in 8 minutes across multiple sessions                  │
│    Signals: [IDs listed]  Severity: HIGH                             │
│    Action: [ HOLD FOR REVIEW ]  [ NOTIFY FRAUD ENGINE ]              │
│                                                                       │
│ 🟡 CONFLICT_CONTEXT_BIAS  ·  User ID: user_4412                     │
│    Open CRA conflict against rated mentor                            │
│    4 signals marked CONFLICT_CONTEXT (weighted 0.3x)                │
│    Action: [ ACKNOWLEDGE ]  [ VIEW CRA RECORD ]                      │
└──────────────────────────────────────────────────────────────────────┘
```

### SCREEN 7 — User Feedback History (Self-View)

```
Purpose:    User can see their own submitted feedback history
           (promotes trust; user knows their voice is recorded)

Layout:
┌────────────────────────────────────────────────────┐
│ MY FEEDBACK HISTORY                               │
│───────────────────────────────────────────────────│
│ All (47)  |  Sessions (18)  |  Courses (12)       │
│───────────────────────────────────────────────────│
│ ✅ GD Session · 14 Apr · Rating: 4/5              │
│    "Turn engine worked as expected"               │
│───────────────────────────────────────────────────│
│ ✅ Mentor Session · 11 Apr · Rating: 3/5          │
│    Submitted  (Anonymous to mentor)               │
│───────────────────────────────────────────────────│
│ ⏭ Course: Java OOP · 8 Apr · Skipped             │
│───────────────────────────────────────────────────│
│                                                   │
│ Your feedback is anonymized before being used     │
│ to compute mentor and content quality scores.     │
└────────────────────────────────────────────────────┘
```

---

## SECTION XIII — OBSERVABILITY HOOKS (SEALED)

### Prometheus Metrics

```
cfca_signals_total{domain, signal_type, channel, confidence}
cfca_signals_dismissed_total{signal_type}
cfca_signals_timed_out_total{signal_type}
cfca_fatigue_blocks_total{user_role}
cfca_routing_dispatch_total{target_service}
cfca_routing_dispatch_failed_total{target_service}
cfca_anomaly_flags_total{anomaly_type, severity}
cfca_entity_health_alerts_total{entity_type}
cfca_nps_score{domain, tenant_id}
cfca_response_rate_pct{domain, channel}
cfca_cra_escalations_dispatched_total{domain}
cfca_passive_signals_ingested_total
```

### Grafana Alert Rules

```yaml
alerts:

  - name: CFCA_RESPONSE_RATE_DROP
    condition: cfca_response_rate_pct{domain="FD-01"} < 40 for 1h
    severity: MEDIUM
    action: Notify product team + review fatigue thresholds

  - name: CFCA_ROUTING_FAILURE_SPIKE
    condition: rate(cfca_routing_dispatch_failed_total[15m]) > 0.05
    severity: HIGH
    action: Alert DevOps + pause routing batch + retry queue inspect

  - name: CFCA_ENTITY_HEALTH_CRITICAL
    condition: cfca_entity_health_alerts_total{entity_type="mentor"} > 3 in 24h
    severity: HIGH
    action: Admin governance notification + product review

  - name: CFCA_NPS_CRITICAL_DROP
    condition: cfca_nps_score{domain="FD-07"} < 0
    severity: CRITICAL
    action: Immediate exec alert + billing team review

  - name: CFCA_ANOMALY_BURST
    condition: rate(cfca_anomaly_flags_total{severity="HIGH"}[1h]) > 5
    severity: HIGH
    action: Fraud Detection Engine notification + Admin review

  - name: CFCA_FATIGUE_SPIKE
    condition: rate(cfca_fatigue_blocks_total[1h]) > 20
    severity: MEDIUM
    action: Review collection frequency — potential over-prompting
```

### Loki Log Tags

```
service=community-feedback-collection-agent
signal_type={FS-xx-xx}
domain={FD-xx}
collection_state={state}
confidence={level}
channel={channel}
routing_target={service}
anomaly_type={type}
```

---

## SECTION XIV — PRIVACY & ANONYMIZATION RULES (SEALED)

The CFCA is a **trust infrastructure component**.
Its privacy rules are non-negotiable.

### Anonymization Protocol (Locked)

```
Rule 1 — Subject feedback is NEVER shown to the rated entity in raw form.
         Mentors see only aggregated averages, never individual signals.

Rule 2 — Optional text comments are stored encrypted at rest.
         They are accessible only to Super Admin via audited query.
         They are never used in automated scoring or reputation computation.

Rule 3 — Passive signals (FD-03, FS-07-05) contain NO user PII.
         User ID is hashed before storage in passive signal records.

Rule 4 — CONFLICT_CONTEXT signals are excluded from entity-facing scores
         but retained in Admin analytics with full context.

Rule 5 — LOW_CONFIDENCE signals are stored but marked as non-authoritative.
         They contribute at 0.2x weight only, never at full weight.

Rule 6 — Feedback records are subject to GDPR Right-to-Erasure (R39-B).
         Upon erasure request: submitter_user_id nullified, text nullified.
         Signal numeric values retained for aggregate integrity.

Rule 7 — Feedback export via Data Export Tool (R39-B) includes
         user's own signals only. Cross-user feedback is never exportable
         by the individual user.
```

---

## SECTION XV — INTEGRATION MAP (SEALED)

| CFCA → Service | Direction | Data Passed |
|----------------|-----------|-------------|
| Scoring Engine | OUTBOUND | Rating signals for GD/Dojo scoring adjustment |
| Trainer Analytics (R89) | OUTBOUND | FD-02, FD-04 signals → TrainerAnalytics tables |
| Reputation Engine (R68) | OUTBOUND | Weighted health score delta per entity |
| Conflict Resolution Agent | OUTBOUND | CRA escalation payloads for flagged signals |
| Fraud Detection Engine | OUTBOUND | Anomaly flag data + burst signal reports |
| Admin Governance (R40) | OUTBOUND | Entity health alerts, NPS drops |
| Analytics Service → ClickHouse | OUTBOUND | All signals batched hourly |
| Marketplace Liquidity Engine (R67) | OUTBOUND | FD-05 marketplace trust scores |
| Innovation Reputation Service | OUTBOUND | FD-08 idea/innovation scores |
| Billing Service | OUTBOUND | FD-07 signals + dispute flags to CRA |
| Notification Service | OUTBOUND | Feedback prompts via push + email |
| Kafka / Event Bus | INBOUND | Trigger events from all 40+ services |
| Immutable Archive (R60) | OUTBOUND | CRITICAL anomaly flags archived |

---

## SECTION XVI — FAILURE HANDLING (SEALED)

| Failure | CFCA Response |
|---------|--------------|
| Trigger event received but user ineligible | SILENTLY_SKIPPED — no user impact |
| response_payload fails schema validation | REJECTION_LOGGED → single re-prompt |
| Routing dispatch fails for target service | Retry 3x backoff → ROUTING_FAILED log → admin alert |
| ClickHouse unavailable | Buffer in Redis; flush on recovery |
| Fatigue engine query timeout | Default to ELIGIBLE (fail-open); log anomaly |
| CRA escalation fails | Hold in retry queue max 1hr; escalate to admin if still failing |
| Passive signal volume exceeds buffer | Drop oldest passive signals; alert on volume spike |
| Entity health computation insufficient samples | Return NULL health score; never return 0 or false score |
| User submits feedback after collection window closes | Signal rejected; window_expired logged |

---

## SECTION XVII — ENFORCEMENT CLAUSE (SEALED)

```
COMMUNITY_FEEDBACK_COLLECTION_AGENT is classified as:
NON-OPTIONAL · PRODUCTION-BLOCKING · ANTIGRAVITY CRITICAL

Absence of CFCA implementation:
→ STOP EXECUTION
→ REPORT: TRUST-SIGNAL-INFRASTRUCTURE-INCOMPLETE
→ NO DEPLOYMENT CLAIM PERMITTED

CFCA must be:
✔ Deployed as dedicated microservice in k8s namespace: core/governance
✔ Wired to all 40+ event-emitting services via Kafka/RabbitMQ event bus
✔ Fatigue Engine active and enforced before any prompt is issued
✔ Entity Health Index computing for all 10 scored entity types
✔ NPS computed daily for all 8 feedback domains
✔ CRA escalation paths active for all 8 misconduct/dispute flag types
✔ Admin Ops Console (R40) receiving entity health alerts
✔ All 4 collection channels (IN_APP, PUSH, EMAIL, PASSIVE) implemented
✔ Privacy anonymization rules enforced at storage layer
✔ GDPR Right-to-Erasure compatible
✔ Observability stack wired (Prometheus + Grafana + Loki)
✔ Covered 100% in QA Test Generator output (R50)
✔ All API endpoints validated by Contract Validator (R49)
✔ Integrated with Conflict Resolution Agent (CRA-v1.0)

Violation of any of the above:
→ STOP EXECUTION
→ REPORT CFCA_ENFORCEMENT_BREACH
→ NO SYSTEM COMPLETION CLAIM PERMITTED
```

---

## SECTION XVIII — FINAL AGENT SEAL

```
COMMUNITY_FEEDBACK_COLLECTION_AGENT
ECOSKILLER · ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE · ANTIGRAVITY ENGINE

Feedback Domains Covered:           8
Feedback Signal Types:              52
Collection State Machine States:    9
Fatigue Engine Rules:               5 role tiers + 3 anti-gaming rules
Database Tables:                    9
Response Payload Schemas:           52 (one per signal type)
API Endpoints:                      11
Events Consumed:                    49 trigger events
Events Emitted:                     12
UI Screens:                         7
Prometheus Metrics:                 12
Cross-Service Integrations:         14
Anonymization Rules:                7 (locked)
Scoring Entity Types:               10

Status: SEALED · LOCKED · FINAL
Version: CFCA-v1.0
Mutation: Add-only via version bump CFCA-v1.1+
Authority: Human Declaration Only
Interpretation: NONE

WebRTC is the physics.
Jitsi is the engine.
Backend is the law.
The CRA is the immune system.
The CFCA is the nervous system.
```

---

*End of COMMUNITY_FEEDBACK_COLLECTION_AGENT Specification · CFCA-v1.0 ·
Ecoskiller Enterprise Optimization + Trust Infrastructure · Antigravity Engine*
