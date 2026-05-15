# 🔒 FAKE PROFILE DETECTION MODEL — ANTIGRAVITY PROTOCOL
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ECOSKILLER × DOJO SAAS — SEALED PRODUCTION ARTIFACT v1.0

```
Artifact Class:       Enterprise Trust Intelligence Subsystem
System Codename:      ANTIGRAVITY — PHANTOM SHIELD
Module Code:          AG-FPDM-001
Mutation Policy:      Add-only via version bump
Execution Mode:       Deterministic
Stack Lock:           Enforced
Interpretation Auth:  NONE
Seal Status:          LOCKED · FINAL · GOVERNED · PRODUCTION-READY
```

---

# ⚠️ PHANTOM SHIELD — SYSTEM IDENTITY DECLARATION

## What PHANTOM SHIELD Is

PHANTOM SHIELD is the Fake Profile Detection Model (FPDM) embedded within the ANTIGRAVITY Enterprise Optimization + Trust Infrastructure layer of the Ecoskiller Unified Talent OS. It is a multi-signal, deterministic, AI-governed detection engine that identifies, classifies, flags, and quarantines fraudulent, synthetic, cloned, farmed, and manipulated user profiles across all Ecoskiller roles: Students, Professionals, Recruiters, Trainers, Mentors, and Institutes.

PHANTOM SHIELD does not ban accounts. It detects, signals, and escalates. Enforcement authority belongs to the Governance Board and Trust Administrators only.

## Why It Exists

The Ecoskiller Unified Talent OS handles verified skill proofs, belt certifications, hiring decisions, championship results, and institutional reputation scores. A single fake profile that successfully inflates its skill rating or certification history can corrupt downstream hiring decisions, pollute leaderboards, devalue legitimate certifications, and erode the institutional trust that is Ecoskiller's core competitive moat. PHANTOM SHIELD eliminates this attack surface.

## What It Is Not

```
✗ Not a ban engine — enforcement is human-governed
✗ Not a legal identity verifier — it signals, not certifies
✗ Not a scoring engine — it feeds Trust Score only
✗ Not a match arbiter — match results are governed by Dojo Match Engine
✗ Not accessible to students or non-admin roles
```

## Authority Boundary

```
PHANTOM SHIELD reads from:    All Ecoskiller entity layers (signals only)
PHANTOM SHIELD writes to:     trust_flags table (append-only)
                              phantom_audit_log (immutable)
                              Trust Score modifier queue
PHANTOM SHIELD signals to:    Governance Board alert queue
                              Enterprise Admin dashboard
                              Dojo Trust Engine (T9, T15 lock bindings)
PHANTOM SHIELD does NOT:      Modify user records
                              Delete profiles
                              Override match results
                              Trigger automatic account suspension
```

---

# 🔒 SECTION A — PHANTOM SHIELD STACK LOCK (NON-NEGOTIABLE)

## A1. Detection Processing Backend

```
Language:               Python 3.11
Framework:              FastAPI microservice (isolated service boundary)
ML Runtime:             scikit-learn + PyTorch (lightweight inference)
AI Classification:      Claude API (claude-sonnet) — behavioral narrative analysis
Graph Engine:           NetworkX — social graph anomaly detection
Rule Engine:            Custom deterministic rule evaluator (no external deps)
Event Broker:           Redis Streams (Ecoskiller Event Bus)
Database Read:          PostgreSQL 15 (read replica — no write to core DB)
Database Write:         PostgreSQL 15 (trust_flags, phantom_audit_log only)
Cache:                  Redis 7 (signal computation cache, TTL-controlled)
Queue:                  Celery + Redis (async detection jobs)
Search Index:           OpenSearch 2.x (duplicate fingerprint search)
```

## A2. Client Surface Integration Points

```
Flutter App:            Trust Admin Panel — flag review + escalation UI
React Web:              Enterprise Trust Portal — audit reports + bulk review
API Gateway:            Kong OSS — /api/v1/trust/phantom/*
Auth:                   JWT + RBAC (trust_admin | governance_board roles only)
Notification:           Postal (email digest) + FCM (push alert to trust_admin)
```

## A3. Module Isolation Rules

```
PHANTOM SHIELD runs as:   Isolated FastAPI microservice
Port boundary:            Internal only (no external exposure)
DB access:                Read replica for source data
                          Dedicated write schema: phantom_schema
Data crossing:            Only via Event Bus (Redis Streams)
No direct DB writes to:   users, profiles, matches, scores, belts, certifications
```

🚫 PHANTOM SHIELD cannot write to any Dojo Engine table
🚫 PHANTOM SHIELD cannot read raw payment or billing data
🚫 PHANTOM SHIELD cannot access raw PII beyond what is tokenized

Stack split is LOCKED.

---

# 🔒 SECTION B — DETECTION SIGNAL UNIVERSE (FROZEN — 18 SIGNAL CLASSES)

Every profile evaluation runs all 18 Signal Classes in parallel. Each class returns an independent risk score (0.0–1.0). The Composite Risk Score is computed from all 18 signals via the weighted fusion model (Section F).

---

### SIGNAL CLASS 01 — IDENTITY COHERENCE SIGNAL
```
What it detects:    Inconsistency between declared identity data points
Signals checked:
  - Name ↔ Institute domain mismatch
  - Name ↔ LinkedIn/GitHub/tool profile mismatch (if integrated)
  - Age ↔ stated graduation year impossibility
  - City ↔ institution location mismatch (flagged, not blocked)
  - Phone region ↔ stated city mismatch
Output:
  identity_coherence_score        (0.0–1.0, lower = more suspicious)
  incoherent_field_pairs[]        (STRING pairs)
  identity_coherence_flag         (BOOL)
```

---

### SIGNAL CLASS 02 — ACCOUNT VELOCITY SIGNAL
```
What it detects:    Abnormal speed of account setup and activity
Signals checked:
  - Time from registration to first skill claim < threshold
  - Time from registration to first certification < threshold
  - Profile completion > 90% within 24 hours of signup
  - Multiple role switches within first 7 days
  - Bulk connections/follows within first 48 hours
Thresholds:
  first_skill_claim_min_hours:    24
  first_cert_min_days:            7
  bulk_connection_48h_threshold:  50
Output:
  velocity_risk_score             (0.0–1.0)
  velocity_breach_flags[]         (STRING[])
  velocity_anomaly_detected       (BOOL)
```

---

### SIGNAL CLASS 03 — DEVICE & SESSION FINGERPRINT SIGNAL
```
What it detects:    Multiple profiles sharing device, IP, or session patterns
Signals checked:
  - Same device fingerprint across > N distinct user_ids
  - Same IP subnet used for > N registrations within T window
  - Headless browser / automation tool signature detected
  - Impossible geographic transitions (login from Mumbai + London < 2 hours)
  - VPN / proxy / datacenter IP usage flagged (not blocked)
Thresholds:
  max_profiles_per_device:        3
  max_registrations_per_subnet:   10 per 24 hours
  geo_transition_min_hours:       6
Output:
  fingerprint_risk_score          (0.0–1.0)
  shared_device_profile_ids[]     (user_token[])
  impossible_geo_flag             (BOOL)
  automation_signal_detected      (BOOL)
```

---

### SIGNAL CLASS 04 — SKILL CLAIM AUTHENTICITY SIGNAL
```
What it detects:    Skill claims that are statistically inconsistent with user behavior
Signals checked:
  - Skills claimed without any matching activity (no drills, no matches, no courses)
  - Skill level jumps with no progression path (White → Black with no intermediates)
  - Skill claim rate > population norm for similar tenure users
  - Skills claimed but zero replay evidence in Dojo system
  - Tool-integration skill claims with no connected tool activity
Output:
  skill_claim_risk_score          (0.0–1.0)
  unsupported_claims[]            (skill_id[])
  claim_vs_activity_gap_score     (FLOAT)
  skill_authenticity_flag         (BOOL)
```

---

### SIGNAL CLASS 05 — CERTIFICATION INTEGRITY SIGNAL
```
What it detects:    Certificates that should not exist given user's match and score history
Signals checked:
  - Certification timestamp precedes required match count completion
  - Certification issued without mentor sign-off in audit log
  - Certificate hash not found in immutable certification log
  - Same certificate hash claimed by > 1 user_id
  - Certification belt version mismatch (cert version ≠ active rubric version)
Output:
  cert_integrity_score            (0.0–1.0)
  invalid_cert_ids[]              (cert_id[])
  orphaned_cert_count             (INT)
  cert_integrity_flag             (BOOL)
```

---

### SIGNAL CLASS 06 — SOCIAL GRAPH ANOMALY SIGNAL
```
What it detects:    Fake network behavior — bot-like follower/endorsement patterns
Signals checked:
  - Follower acquisition rate exceeds population 99th percentile
  - Endorsements from accounts with < 7 days tenure
  - Mutual endorsement clusters (A endorses B, B endorses A, zero other activity)
  - Follower graph is star topology (all follow one account, no cross connections)
  - Account has followers but zero content, activity, or connections
Graph Engine:       NetworkX — cluster coefficient, betweenness centrality, in-degree analysis
Output:
  social_graph_risk_score         (0.0–1.0)
  bot_follower_ratio              (FLOAT)
  mutual_endorse_cluster_ids[]    (user_token[])
  graph_topology_anomaly          (BOOL)
```

---

### SIGNAL CLASS 07 — CONTENT PATTERN SIGNAL
```
What it detects:    AI-generated, copy-pasted, or templated profile content
Signals checked:
  - Bio/About text cosine similarity > 0.88 across distinct profiles
  - Portfolio items duplicated across user_ids (hash match)
  - Post content copy rate (duplicate detection across feed)
  - Skills description text identical to another profile verbatim
  - Resume content hash shared across profiles
AI Tool:            Claude API — narrative authenticity classification
Output:
  content_originality_score       (0.0–1.0, lower = more suspicious)
  duplicate_content_hash_ids[]    (upload_id[])
  ai_generated_probability        (FLOAT)
  content_anomaly_flag            (BOOL)
```

---

### SIGNAL CLASS 08 — BEHAVIORAL RHYTHM SIGNAL
```
What it detects:    Non-human activity patterns (bot-operated accounts)
Signals checked:
  - Login timestamps cluster at exact intervals (e.g., every 3600.00 seconds)
  - Activity only during unusual hours (3am–5am local time > 80% of sessions)
  - Zero dwell time on pages (< 0.5 seconds per screen transition)
  - API call cadence matches scripted pattern (burst + pause + burst)
  - Mouse/touch event entropy below human threshold (desktop only)
Output:
  behavioral_rhythm_risk_score    (0.0–1.0)
  non_human_probability           (FLOAT)
  bot_pattern_signals[]           (STRING[])
  behavioral_anomaly_flag         (BOOL)
```

---

### SIGNAL CLASS 09 — RATING FARMING SIGNAL
```
What it detects:    Coordinated match farming to inflate ratings artificially
Signals checked:
  - Same opponent pair matched > threshold times (non-tournament context)
  - Win rate against specific opponent 100% with sample > 5 matches
  - Rating gain per match exceeds statistical ceiling
  - Match duration < minimum legitimate completion time
  - Mutual surrender patterns between accounts
Dojo Binding:       SECTION T9 — Collusion & Manipulation Detection Lock
Output:
  farming_risk_score              (0.0–1.0)
  farming_pair_ids[]              (user_token pairs)
  suspicious_match_ids[]          (match_id[])
  farming_flag                    (BOOL)
```

---

### SIGNAL CLASS 10 — MULTI-ACCOUNT SIGNAL
```
What it detects:    Single human operating multiple Ecoskiller accounts
Signals checked:
  - Shared device fingerprint across accounts (CLASS 03 cross-signal)
  - Shared recovery email domain with variation (john+1@, john+2@)
  - Sequential registration timing (< 5 minutes between accounts from same IP)
  - Identical skill set claimed across distinct accounts
  - Cross-account login patterns (session A logs out → session B logs in, same device)
  - Shared payment instrument across accounts (hashed token match only)
Output:
  multi_account_risk_score        (0.0–1.0)
  linked_account_tokens[]         (user_token[])
  confidence_of_same_human        (FLOAT)
  multi_account_flag              (BOOL)
```

---

### SIGNAL CLASS 11 — INSTITUTE VERIFICATION SIGNAL
```
What it detects:    Fake institute claims and unverified institutional affiliations
Signals checked:
  - Institute domain not in verified institution registry
  - Student email domain does not match claimed institute
  - Multiple students from same IP claiming different institutes
  - Institute profile created < 24 hours before first student enrollment
  - Institute logo hash matches another institution's logo (visual duplicate)
  - Zero faculty/staff accounts despite large student count
Output:
  institute_verify_risk_score     (0.0–1.0)
  domain_mismatch_flag            (BOOL)
  unverified_institute_flag       (BOOL)
  suspicious_enrollment_count     (INT)
```

---

### SIGNAL CLASS 12 — RECRUITER IDENTITY SIGNAL
```
What it detects:    Fake or impersonating recruiter accounts
Signals checked:
  - Company domain claimed does not resolve or is disposable
  - Company name matches known legitimate company but email domain differs
  - Recruiter profile created with stock photo (image hash match against known stock DB)
  - Zero company verification (no LinkedIn org match, no GST/tax ID)
  - Recruiter posts suspicious job listings (salary > 10× market median for role)
  - Mass contact of candidates without job post (data harvesting signal)
Output:
  recruiter_identity_risk_score   (0.0–1.0)
  company_domain_valid            (BOOL)
  impersonation_flag              (BOOL)
  data_harvest_pattern_flag       (BOOL)
```

---

### SIGNAL CLASS 13 — ECONOMIC ABUSE SIGNAL
```
What it detects:    Financial fraud patterns targeting Ecoskiller's billing system
Signals checked:
  - Refund requests > threshold within rolling 30-day window
  - Account created, subscribed, certified, then refunded → repeat cycle
  - Tournament entry fee paid → won → refund requested (post-victory fraud)
  - Coupon code reuse across multi-account cluster
  - Free trial cycling (new accounts using same identity markers)
Dojo Binding:       SECTION T15 — Economic Abuse Controls Lock
Output:
  economic_abuse_score            (0.0–1.0)
  refund_abuse_flag               (BOOL)
  free_trial_cycle_flag           (BOOL)
  billing_fraud_pattern           (STRING[])
```

---

### SIGNAL CLASS 14 — MENTOR IMPERSONATION SIGNAL
```
What it detects:    Accounts falsely claiming mentor authority or certification
Signals checked:
  - Mentor badge displayed but no mentor certification record in audit log
  - Mentor commands issued by account without valid mentor role token
  - Mentor account with certification expired but still issuing overrides
  - Mentor calibration score drifted beyond tolerance (cross-check with T3 Lock)
  - Score overrides from uncertified mentor accounts
Dojo Binding:       SECTION T7 — Mentor Training & Certification Lock
                    SECTION T3 — Rater Calibration Lock
Output:
  mentor_impersonation_score      (0.0–1.0)
  invalid_override_ids[]          (event_id[])
  uncertified_command_count       (INT)
  mentor_impersonation_flag       (BOOL)
```

---

### SIGNAL CLASS 15 — PORTFOLIO FABRICATION SIGNAL
```
What it detects:    Fake project portfolios, fabricated work samples, stolen artifacts
Signals checked:
  - Portfolio file hashes matching known public GitHub repos submitted as own work
  - Portfolio upload timestamp predates account creation date
  - Image EXIF metadata ownership mismatch with profile identity
  - Portfolio item referenced in multiple distinct user profiles
  - Portfolio URL points to known template/demo project
  - Project claimed on profile with no matching Ecoskiller project record
Output:
  portfolio_fabrication_score     (0.0–1.0)
  stolen_artifact_ids[]           (item_id[])
  fabrication_evidence[]          (STRING[])
  portfolio_fabrication_flag      (BOOL)
```

---

### SIGNAL CLASS 16 — CHAMPIONSHIP MANIPULATION SIGNAL
```
What it detects:    Fake or manipulated championship participation and results
Signals checked:
  - Championship rank claimed without verifiable tournament_id
  - Tournament result hash not found in Tournament Engine audit log
  - Winner declared in match with anomalous scoring (CLASS 09 overlap)
  - Spectator-only account appearing in winner records
  - Championship badge issued before tournament end_date
  - Multiple accounts in same tournament from same device fingerprint
Dojo Binding:       Tournament Engine + SECTION T5 Match Fairness Lock
Output:
  championship_manipulation_score (0.0–1.0)
  invalid_result_ids[]            (tournament_result_id[])
  device_collision_in_tournament  (BOOL)
  manipulation_flag               (BOOL)
```

---

### SIGNAL CLASS 17 — REFERENCE CHAIN FRAUD SIGNAL
```
What it detects:    Fake referral rings and manufactured trust chains
Signals checked:
  - Account A referred by B, B referred by C, C referred by A (circular)
  - Referral cluster where all accounts have identical behavioral signatures
  - Referral bonus collected by account then account immediately deactivated
  - Endorsement chains: A endorses B, B endorses C, C endorses A — no external links
  - Influence score boosted exclusively via in-cluster activity
Output:
  reference_fraud_score           (0.0–1.0)
  circular_referral_chains[]      (user_token[][])
  endorsement_ring_ids[]          (user_token[])
  reference_fraud_flag            (BOOL)
```

---

### SIGNAL CLASS 18 — AI SYNTHETIC IDENTITY SIGNAL
```
What it detects:    Fully AI-generated fake personas (GAN faces, LLM bios, synthetic histories)
Signals checked:
  - Profile photo GAN/diffusion model detection (classifier model)
  - Bio text perplexity score below human writing baseline
  - Skill history follows suspiciously clean linear progression (no real-world variance)
  - All profile fields populated in single session with no edit history
  - Zero human behavioral entropy in interaction history
  - Username matches known synthetic identity naming patterns
AI Tools:           PyTorch GAN face classifier
                    Claude API — synthetic text narrative analysis
Output:
  synthetic_identity_score        (0.0–1.0)
  gan_face_probability            (FLOAT)
  synthetic_bio_probability       (FLOAT)
  synthetic_identity_flag         (BOOL)
```

---

# 🔒 SECTION C — PHANTOM SHIELD PROCESSING PIPELINE (FROZEN)

```
[Profile Event Trigger]
  ↓  (triggers: new registration | profile update | activity anomaly | scheduled scan)
[Profile Tokenizer] — PII masked to user_token; raw PII never enters FPDM
  ↓
[Signal Dispatcher] — fans out to all 18 Signal Classes in parallel
  ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓
[18 Signal Class Workers] — each runs independently, returns (score, flags, evidence)
  ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓
[Signal Aggregator] — collects all 18 results
  ↓
[Composite Risk Score Engine] — weighted fusion model (Section F)
  ↓
[Risk Tier Classifier] — assigns TIER 1–5 (Section G)
  ↓
[Evidence Packager] — bundles evidence payload (no raw PII)
  ↓
[Trust Flag Writer] — appends to trust_flags (append-only, immutable)
  ↓
[Phantom Audit Logger] — immutable event log (every detection run)
  ↓
[Event Publisher] — Redis Streams: fake_profile_detected | profile_cleared | review_required
  ↓
[Alert Dispatcher] — Governance Board queue | Trust Admin notification | Enterprise Dashboard
  ↓
[Trust Score Modifier] — Trust Score delta applied to user's trust_score field
```

No step may be skipped.
Failure at any step → STOP → REPORT → NO DETECTION CLAIM PERMITTED.
Detection runs that fail partway produce NO output — partial detection is never stored.

---

# 🔒 SECTION D — DATA MODEL FREEZE

## D1. Primary Entities (Non-renameable)

```
PhantomProfile          — detection run record per user_token
TrustFlag               — individual flag issued per signal class breach
PhantomAuditLog         — immutable event log (every pipeline step)
SignalResult            — per-class result record for each detection run
ReviewDecision          — trust admin / governance board decision record
QuarantineRecord        — records when a profile is placed in review hold
```

## D2. PhantomProfile Schema

```sql
CREATE TABLE phantom_profiles (
    phantom_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_token            TEXT NOT NULL,               -- tokenized, never raw user_id
    tenant_id             UUID NOT NULL,
    detection_run_at      TIMESTAMPTZ DEFAULT NOW(),
    triggered_by          TEXT NOT NULL,               -- REGISTRATION | ACTIVITY | SCHEDULED | MANUAL
    composite_risk_score  NUMERIC(4,3) NOT NULL,       -- 0.000–1.000
    risk_tier             TEXT NOT NULL,               -- TIER_1 through TIER_5
    signal_count_breached INT NOT NULL DEFAULT 0,
    auto_quarantine       BOOLEAN DEFAULT FALSE,
    reviewed              BOOLEAN DEFAULT FALSE,
    reviewed_by           UUID,
    reviewed_at           TIMESTAMPTZ,
    final_verdict         TEXT,                        -- CLEARED | CONFIRMED_FAKE | ESCALATED | WATCH
    run_duration_ms       INT
);
```

## D3. TrustFlag Schema

```sql
CREATE TABLE trust_flags (
    flag_id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phantom_id            UUID NOT NULL REFERENCES phantom_profiles(phantom_id),
    user_token            TEXT NOT NULL,
    tenant_id             UUID NOT NULL,
    signal_class          TEXT NOT NULL,               -- SIGNAL_CLASS_01 through 18
    risk_score            NUMERIC(4,3) NOT NULL,
    evidence_payload      JSONB NOT NULL,              -- no raw PII
    anomaly_flag          BOOLEAN DEFAULT FALSE,
    created_at            TIMESTAMPTZ DEFAULT NOW()
    -- NO UPDATE PERMITTED
    -- NO DELETE PERMITTED
);
```

## D4. PhantomAuditLog Schema

```sql
CREATE TABLE phantom_audit_log (
    log_id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phantom_id            UUID NOT NULL,
    user_token            TEXT NOT NULL,
    pipeline_step         TEXT NOT NULL,               -- exact step name
    step_status           TEXT NOT NULL,               -- SUCCESS | FAILURE | SKIP
    step_payload          JSONB,
    actor_system          TEXT NOT NULL DEFAULT 'PHANTOM_SHIELD',
    created_at            TIMESTAMPTZ DEFAULT NOW()
    -- IMMUTABLE: no UPDATE, no DELETE ever
);
```

## D5. ReviewDecision Schema

```sql
CREATE TABLE review_decisions (
    decision_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phantom_id            UUID NOT NULL REFERENCES phantom_profiles(phantom_id),
    user_token            TEXT NOT NULL,
    decision_by           UUID NOT NULL,               -- trust_admin or governance_board user
    decision_role         TEXT NOT NULL,               -- TRUST_ADMIN | GOVERNANCE_BOARD
    verdict               TEXT NOT NULL,               -- CLEARED | CONFIRMED_FAKE | WATCH | ESCALATE
    reasoning             TEXT NOT NULL,               -- mandatory human rationale
    action_taken          TEXT,                        -- NONE | ACCOUNT_HOLD | CERT_REVOKED | SUSPENDED
    decided_at            TIMESTAMPTZ DEFAULT NOW(),
    appeal_eligible       BOOLEAN DEFAULT TRUE,
    appeal_deadline_at    TIMESTAMPTZ
    -- Append-only: no UPDATE permitted after decided_at
);
```

## D6. QuarantineRecord Schema

```sql
CREATE TABLE quarantine_records (
    quarantine_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phantom_id            UUID NOT NULL REFERENCES phantom_profiles(phantom_id),
    user_token            TEXT NOT NULL,
    quarantine_type       TEXT NOT NULL,               -- SOFT | HARD
    quarantine_reason     TEXT NOT NULL,
    quarantine_at         TIMESTAMPTZ DEFAULT NOW(),
    released_at           TIMESTAMPTZ,
    released_by           UUID,
    release_reason        TEXT
);
```

Fields may extend — never mutate.

---

# 🔒 SECTION E — DETECTION TRIGGER SYSTEM (FROZEN)

## E1. Trigger Events (All Active — None Optional)

```
TRIGGER_01:   NEW_REGISTRATION
              → Run on every new account created
              → Signal classes active: 01, 02, 03, 07, 11, 12, 18
              → Lightweight fast-path (< 5 seconds target)

TRIGGER_02:   PROFILE_UPDATE
              → Run when core identity fields updated
              → Signal classes active: 01, 04, 07, 11, 15, 18
              → Standard path (< 30 seconds target)

TRIGGER_03:   FIRST_CERTIFICATION_CLAIM
              → Run when user earns first belt or certification
              → Signal classes active: 04, 05, 08, 09, 14
              → Standard path (< 30 seconds target)

TRIGGER_04:   ACTIVITY_ANOMALY
              → Run when anomaly detected by Ecoskiller Event Bus
              → Signal classes active: 02, 03, 08, 09, 10, 13, 16
              → Real-time path (< 10 seconds target)

TRIGGER_05:   INTEGRATION_SYNC
              → Run when enterprise tool sync completes (GitHub, Jira, etc.)
              → Signal classes active: 04, 15, 17
              → Async path (< 120 seconds target)

TRIGGER_06:   SCHEDULED_WEEKLY_SCAN
              → Full 18-class scan on all active profiles
              → Batch path (background, no SLA)
              → Prioritizes: WATCH-listed profiles first

TRIGGER_07:   MANUAL_REVIEW_REQUEST
              → Trust admin or governance board initiates
              → Signal classes active: all 18
              → Full path (< 5 minutes target)

TRIGGER_08:   TOURNAMENT_ENTRY
              → Run before allowing entry into ranked or certification tournaments
              → Signal classes active: 09, 10, 16, 17
              → Blocking path (must clear before entry granted)

TRIGGER_09:   HIRING_MARKETPLACE_ACTIVATION
              → Run when profile activates for recruiter visibility
              → Signal classes active: 01, 04, 05, 11, 12, 15, 18
              → Blocking path (must clear before recruiter can view)
```

## E2. Trigger Routing Rules

```
Blocking triggers (08, 09):   Profile action HELD until TIER 1–3 result returned
Fast-path triggers (01, 04):  Result must return within SLA or default to TIER_3 WATCH
Batch trigger (06):           Never blocks any user action
Manual trigger (07):          Overrides all caching — always fresh run
```

---

# 🔒 SECTION F — COMPOSITE RISK SCORE ENGINE (FROZEN)

## F1. Signal Class Weights (v1.0)

```
Signal Class    Name                            Weight
─────────────────────────────────────────────────────
CLASS_01        Identity Coherence              0.06
CLASS_02        Account Velocity                0.05
CLASS_03        Device & Session Fingerprint    0.09
CLASS_04        Skill Claim Authenticity        0.08
CLASS_05        Certification Integrity         0.09
CLASS_06        Social Graph Anomaly            0.05
CLASS_07        Content Pattern                 0.05
CLASS_08        Behavioral Rhythm               0.07
CLASS_09        Rating Farming                  0.08
CLASS_10        Multi-Account                   0.09
CLASS_11        Institute Verification          0.05
CLASS_12        Recruiter Identity              0.04
CLASS_13        Economic Abuse                  0.06
CLASS_14        Mentor Impersonation            0.06
CLASS_15        Portfolio Fabrication           0.04
CLASS_16        Championship Manipulation       0.05
CLASS_17        Reference Chain Fraud           0.04
CLASS_18        AI Synthetic Identity           0.05
─────────────────────────────────────────────────────
                TOTAL WEIGHT                    1.00
```

## F2. Composite Score Formula

```
composite_risk_score = Σ (signal_class_score[i] × weight[i]) for i in 1..18

HARD ESCALATION RULE:
If any single class returns score = 1.00 (maximum certainty):
  → composite_risk_score minimum = 0.75 (floor override)
  → Risk tier minimum = TIER_4
  → Reason: even one definitive detection warrants elevated review

SOFT CLUSTER RULE:
If ≥ 3 signal classes breach 0.80:
  → composite_risk_score += 0.05 bonus (cluster amplification)
  → Reason: correlated multi-class signals indicate coordinated fraud
```

## F3. Score Modifiers

```
+0.08   — Profile has ZERO match history but claims belt rank
+0.06   — Account age < 30 days with > 10 certifications
+0.05   — Profile part of previously confirmed fake cluster
-0.05   — Profile has verified institutional email (active domain)
-0.05   — Profile has > 100 matches in Dojo audit log (legitimate engagement)
-0.08   — Profile has employer-verified skill endorsement from enterprise integration
-0.10   — Profile previously reviewed and CLEARED by Governance Board in last 90 days
```

---

# 🔒 SECTION G — RISK TIER CLASSIFICATION (FROZEN)

## G1. Tier Definitions

```
TIER 1 — CLEAN           (0.00–0.19)
  Action:     No flag raised
  Visibility: Trust indicator = VERIFIED (green)
  Review:     Not required
  Interval:   Next scheduled weekly scan only

TIER 2 — LOW RISK        (0.20–0.39)
  Action:     Soft watch flag created
  Visibility: Trust indicator = STANDARD (default)
  Review:     Not required — auto-clears if no escalation in 30 days
  Interval:   Bi-weekly re-scan

TIER 3 — MODERATE RISK   (0.40–0.59)
  Action:     WATCH flag raised; trust admin notified
  Visibility: Trust indicator = PENDING (amber, visible to trust_admin only)
  Review:     Trust admin review within 5 business days
  Interval:   Weekly re-scan; hiring marketplace access HELD

TIER 4 — HIGH RISK       (0.60–0.79)
  Action:     SOFT QUARANTINE — profile hidden from hiring marketplace
                               — certifications temporarily unverified
                               — tournament entry blocked
  Visibility: Trust indicator = UNDER REVIEW (trust_admin visible)
  Review:     Trust admin review within 48 hours MANDATORY
  Interval:   Continuous monitoring every 24 hours

TIER 5 — CRITICAL        (0.80–1.00)
  Action:     HARD QUARANTINE — profile fully suspended from all interactions
                               — all match results flagged for audit
                               — all certifications suspended
                               — Governance Board alerted immediately
  Visibility: Trust indicator = SUSPENDED
  Review:     Governance Board review within 24 hours MANDATORY
  Interval:   Continuous monitoring; no re-scan until board verdict
```

## G2. Tier Escalation Rules

```
TIER 2 → TIER 3:   If same profile hits TIER 2 in 3 consecutive scans
TIER 3 → TIER 4:   If trust admin misses 5-day review window
TIER 4 → TIER 5:   If new TRIGGER_04 anomaly fires during TIER 4 quarantine
TIER 5 → CLEARED:  Governance Board verdict CLEARED only
TIER 5 → BANNED:   Human enforcement decision only (outside PHANTOM SHIELD authority)
```

## G3. Appeal Rights

```
TIER 3:   User can submit appeal within 14 days of notification
TIER 4:   User can submit appeal within 7 days
TIER 5:   User can submit appeal within 48 hours via Dojo T16 Appeals System
All appeals routed to:  Governance Board via SECTION T16 — Appeals & Governance Board Lock
Appeal decisions:       Logged and versioned (immutable)
```

---

# 🔒 SECTION H — TRUST INFRASTRUCTURE BINDING (LOCKED)

## H1. Dojo Trust Lock Alignment Table

| PHANTOM SHIELD Signal | Dojo Section | Rule Binding |
|---|---|---|
| CLASS_09 Rating Farming | T9 — Collusion & Manipulation Detection | Flagged matches require audit before counting toward belts |
| CLASS_13 Economic Abuse | T15 — Economic Abuse Controls Lock | Billing fraud triggers account review |
| CLASS_14 Mentor Impersonation | T7 — Mentor Training & Certification Lock | Uncertified mentors cannot run ranked matches |
| CLASS_14 (Calibration drift) | T3 — Rater Calibration Lock | Mentors outside tolerance lose authority automatically |
| CLASS_05 Certification | T17 — Belt Version Governance Lock | Belt must have valid rubric version tag |
| CLASS_16 Championship | T5 — Match Fairness Engine Lock | Tournament fairness audits mandatory |
| CLASS_12 Recruiter | T14 — Talent Marketplace Trust Lock | Hiring decisions must reference verified match data |
| CLASS_18 Synthetic Identity | T9 + T15 combined | AI fake = highest collusion + economic abuse risk |
| All TIER 5 | T16 — Appeals & Governance Board | All TIER 5 decisions versioned + appealable |
| All TIER 4+ | T10 — Behavioral Safety Lock | Safety overrides scoring |

## H2. Trust Events Published to Event Bus

```
Event Name                        Trigger Condition
──────────────────────────────────────────────────────────────────
fake_profile_suspected            Composite score ≥ 0.40
fake_profile_high_risk            Composite score ≥ 0.60
fake_profile_critical             Composite score ≥ 0.80
profile_soft_quarantine           TIER 4 classification
profile_hard_quarantine           TIER 5 classification
profile_cleared                   Review verdict = CLEARED
cert_suspended_pending_review     TIER 4+ certification hold
cert_reinstated                   Post-CLEARED cert restoration
match_results_flagged             TIER 5 — all match results audit
governance_board_alert_raised     TIER 5 trigger
mentor_authority_suspended        CLASS_14 breach + TIER 4+
economic_abuse_billing_hold       CLASS_13 breach + TIER 4+
```

## H3. Trust Score Impact Table

```
Risk Tier Outcome       Trust Score Delta
────────────────────────────────────────
TIER 1 (CLEAN)          0 (no change)
TIER 2 (LOW)            -2 points
TIER 3 (MODERATE)       -8 points
TIER 4 (HIGH)           -20 points + Marketplace visibility suppressed
TIER 5 (CRITICAL)       Trust Score frozen at 0 until review
CLEARED (post-review)   +15 recovery bonus (partial restoration)
CONFIRMED_FAKE (verdict) Permanent 0 (non-reversible without board order)
```

---

# 🔒 SECTION I — API SURFACE LOCK (FROZEN)

All endpoints below are frozen. Extension by version bump only.

```
POST   /api/v1/trust/phantom/scan
         Body: { user_token, trigger_type }
         Auth: trust_admin | system_service
         Returns: { phantom_id, composite_risk_score, risk_tier }

GET    /api/v1/trust/phantom/{phantom_id}
         Auth: trust_admin | governance_board
         Returns: full PhantomProfile + all 18 SignalResults

GET    /api/v1/trust/phantom/profile/{user_token}/history
         Auth: trust_admin | governance_board
         Returns: all detection runs for this token, paginated

GET    /api/v1/trust/phantom/{phantom_id}/evidence
         Auth: governance_board only
         Returns: full evidence payload (no raw PII)

POST   /api/v1/trust/phantom/{phantom_id}/review
         Body: { verdict, reasoning, action_taken }
         Auth: trust_admin (TIER 3–4) | governance_board (TIER 5)
         Returns: ReviewDecision record

GET    /api/v1/trust/flags/{user_token}
         Auth: trust_admin | governance_board
         Returns: all TrustFlags for user_token, paginated

POST   /api/v1/trust/phantom/{phantom_id}/quarantine
         Body: { quarantine_type, reason }
         Auth: trust_admin (SOFT) | governance_board (HARD)
         Returns: QuarantineRecord

DELETE /api/v1/trust/phantom/{phantom_id}/quarantine
         Auth: governance_board only (release requires board decision)
         Returns: updated QuarantineRecord with release details

GET    /api/v1/trust/phantom/audit/{phantom_id}
         Auth: governance_board only
         Returns: immutable PhantomAuditLog for run

GET    /api/v1/trust/dashboard/summary
         Auth: trust_admin | governance_board
         Returns: live trust dashboard metrics (aggregated, anonymized)

POST   /api/v1/trust/phantom/{phantom_id}/appeal
         Auth: authenticated user (own profile only)
         Returns: appeal ticket_id → routed to T16 Appeals System

GET    /api/v1/trust/phantom/cluster/{cluster_id}
         Auth: governance_board only
         Returns: all linked profiles in detected fake cluster
```

Rate limiting: 100 scan requests / minute per service token
Human-initiated scans: 20 / hour per trust_admin

---

# 🔒 SECTION J — SECURITY HARDENING (PHANTOM SHIELD-SPECIFIC)

## J1. PII Protection Protocol

```
user_id → user_token:      HMAC-SHA256 keyed hash (key in secrets manager)
                            Token is one-way, non-reversible by PHANTOM SHIELD itself
                            Reverse mapping held ONLY in Core Identity Service
Profile photos:             Analyzed via external classifier, immediately discarded
                            Only GAN probability score stored — no image retained
Email addresses:            Domain retained, local part hashed before processing
Name fields:                Never stored in PHANTOM SHIELD tables
Location data:              Region-level only (city/country — no GPS)
```

## J2. Signal Evidence Rules

```
Evidence payloads:          May contain only: scores, boolean flags, count integers,
                            anonymized pair tokens, match_id references, timestamp ranges
Evidence payloads NEVER:    Contain full names, emails, photos, exact addresses,
                            raw session logs, or payment data
```

## J3. Access Segregation

```
PHANTOM SHIELD DB schema:   Isolated from Ecoskiller core schema
                            Read replica access only to core data
                            Write access to phantom_schema only
trust_admin role:           Read: TIER 3–4 evidence only
                            Write: TIER 3–4 review decisions only
governance_board role:      Read: all tiers, full audit log
                            Write: all review decisions, quarantine, release
No other role:              Has ANY access to PHANTOM SHIELD data
```

## J4. Audit Immutability Enforcement

```
PhantomAuditLog:            DB trigger blocks UPDATE and DELETE
TrustFlag:                  DB trigger blocks UPDATE and DELETE
ReviewDecision:             UPDATE blocked after decided_at is set
QuarantineRecord:           Release logged as new field — not an edit
```

---

# 🔒 SECTION K — FLUTTER + REACT UI SURFACE LOCK

## K1. Flutter App — Trust Admin Screens (Required)

```
Trust Dashboard Screen
  — Live risk tier distribution chart
  — Recent flags queue (TIER 3+ only)
  — Pending reviews counter + SLA timer

Profile Flag Review Screen
  — Signal class breakdown (18 cards)
  — Per-class score visualization
  — Evidence payload viewer (no raw PII)
  — Verdict submission form (mandatory reasoning field)

Quarantine Management Screen
  — Active SOFT quarantine list
  — Quarantine reason + duration
  — Release action (SOFT only — HARD requires board)

Alert Feed Screen
  — Real-time TIER 4+ alerts
  — Appeal submissions inbox

Appeal Review Screen
  — Appeal detail viewer
  — Supporting evidence (user-submitted)
  — Board decision submission
```

## K2. React Web — Enterprise Trust Portal Screens (Required)

```
/trust/dashboard             — SSR-rendered summary (auth required, no-index)
/trust/flags                 — Full flag history (governance board)
/trust/review/{phantom_id}   — Detailed review page
/trust/audit/{phantom_id}    — Immutable audit trail viewer
/trust/clusters              — Fake cluster visualization (graph view)
/trust/reports/weekly        — Weekly trust health report
/trust/appeal/{appeal_id}    — Appeal processing page (governance board)
```

🚫 Trust data never rendered on public-facing React SEO pages
🚫 No user_token ever exposed in React page URLs — use phantom_id only

---

# 🔒 SECTION L — INTEGRATION WITH ECOSKILLER TALENT OS

PHANTOM SHIELD directly serves and protects the following Ecoskiller Modules:

```
MODULE 15 — Trust System:
  → Primary data supplier for Verified Users, Verified Institutes, Verified Recruiters
  → Fraud Detection (feature 6) = PHANTOM SHIELD TIER output
  → Fake Profile Detection (feature 8) = PHANTOM SHIELD primary function
  → Trust Score (feature 9) = PHANTOM SHIELD Trust Score delta consumer

MODULE 7 — Recruiter System:
  → Hiring Marketplace only shows TIER 1–2 profiles
  → Candidate reliability scores informed by PHANTOM SHIELD clean history

MODULE 4 — Championship System:
  → CLASS 16 feeds directly into AI Cheating Detection (feature 38)
  → TIER 4+ blocks tournament entry (feature 19 Knockout protection)

MODULE 1 — Identity System:
  → Verified Identity (feature 7) requires TIER 1–2 minimum
  → Blockchain certificates only issued to TIER 1 profiles

MODULE 3 — Skill System (Dojo):
  → CLASS 04, 05, 09 protect Skill Certifications and Belt awards
  → Skill Reliability Score (feature 19) partially computed from PHANTOM SHIELD clean flag

MODULE 9 — Integration System:
  → PHANTOM SHIELD rescans any profile after enterprise tool integration sync
  → Protects against fake skill extraction from manipulated tool data

MODULE 13 — Financial System:
  → CLASS 13 Economic Abuse feeds billing hold to Wallet + Escrow systems
  → Refund requests from TIER 4+ profiles routed to manual review
```

---

# 🔒 SECTION M — ONE-CLICK INTEGRATION PROTECTION LAYER

The Ecoskiller Talent OS includes One-Click Integration with 100 enterprise tools (Workday, BambooHR, GitHub, Salesforce, Jira, Figma, etc.). PHANTOM SHIELD adds a Trust Validation Pass on every integration data ingest.

## M1. Integration Trust Validation Rules

```
On GitHub integration sync:
  → Validate commit authorship against user_token identity signals
  → Detect repo cloning patterns (forked public repos claimed as original)
  → Flag if commit timestamps cluster at bot-like intervals (CLASS 08 overlap)

On ATS integration sync (Greenhouse, Lever, SmartRecruiters):
  → Validate that Ecoskiller profile matches ATS candidate record
  → Flag if ATS shows rejection history that profile omits

On LMS integration sync (Moodle, Google Classroom):
  → Validate course completion dates against Ecoskiller skill claim dates
  → Flag if LMS completion date postdates Ecoskiller skill claim (impossible sequence)

On CRM integration sync (Salesforce, HubSpot, Zoho CRM):
  → Extract reliability signals (deal close rates, tenure in role)
  → Cross-validate against claimed sales skill levels
  → Flag inconsistencies (CLASS 04 overlap)

On HR integration sync (Workday, Darwinbox, BambooHR):
  → Validate employment dates against profile claims
  → Flag employment gap omissions exceeding threshold
  → Detect if same employee_id appears in multiple Ecoskiller profiles (CLASS 10 overlap)
```

## M2. Migration Trust Validation Rules

```
On 1-Click Migration from any platform:
  → Full 18-class PHANTOM SHIELD scan triggered (TRIGGER_07 equivalent)
  → Historical data integrity check — timestamps must be logically consistent
  → Migrated profiles enter TIER 2 watch-hold for 14 days (minimum observation period)
  → TIER 2 hold released only if no anomalies detected during observation window
```

---

# 🔒 SECTION N — TEST GATE LOCK (PHANTOM SHIELD-SPECIFIC)

No production deployment without passing all test gates.

```
Signal Class Unit Tests (per class × 18):
  ✔ Known fake profile → correct class fires
  ✔ Known legitimate profile → class does not fire
  ✔ Edge: brand-new profile with zero data → handled gracefully
  ✔ Edge: profile with only partial data → no false TIER 5

Composite Score Tests:
  ✔ Single CLASS score = 1.00 → floor rule applied correctly
  ✔ 3+ classes breach 0.80 → cluster amplification applied
  ✔ Score modifiers applied in correct order
  ✔ Score stays within 0.000–1.000 bounds

Tier Classification Tests:
  ✔ Score 0.00 → TIER 1
  ✔ Score 0.19 → TIER 1
  ✔ Score 0.20 → TIER 2
  ✔ Score 0.60 → TIER 4
  ✔ Score 1.00 → TIER 5
  ✔ Escalation rule: 3× TIER 2 → promoted to TIER 3

Trigger Tests:
  ✔ TRIGGER_08 (tournament entry) — blocks profile correctly
  ✔ TRIGGER_09 (hiring marketplace) — blocks profile correctly
  ✔ TRIGGER_01 (registration) — completes within 5s SLA
  ✔ Failed partial detection → NO output stored

Immutability Tests:
  ✔ PhantomAuditLog → UPDATE rejected at DB level
  ✔ TrustFlag → DELETE rejected at DB level
  ✔ ReviewDecision → UPDATE rejected after decided_at set

PII Tests:
  ✔ user_id never appears in any PHANTOM SHIELD table
  ✔ Full name never stored in evidence payload
  ✔ Profile photo never stored — only GAN score
  ✔ Email local part never stored — only domain

Performance Tests:
  ✔ TRIGGER_01 fast-path: p95 < 5 seconds under 1000 concurrent registrations
  ✔ TRIGGER_08 blocking-path: p99 < 10 seconds
  ✔ Full 18-class scan: p95 < 5 minutes
  ✔ Weekly batch scan of 1M profiles: completes within 24h window

Concurrency Tests:
  ✔ Same user_token scanned simultaneously — idempotent result
  ✔ Celery worker failure mid-pipeline → no partial result stored
```

---

# 🔒 SECTION O — OBSERVABILITY LOCK (PHANTOM SHIELD-SPECIFIC)

```
Required Metrics:
  - phantom_scans_total (counter, by trigger_type)
  - phantom_scan_duration_seconds (histogram, by trigger_type)
  - risk_tier_distribution (gauge, by tier, by tenant)
  - signal_class_fire_rate (gauge, by class)
  - false_positive_rate (gauge, updated after review verdicts)
  - tier5_incidents_total (counter)
  - quarantine_active_count (gauge)
  - review_sla_breach_count (counter, by tier)
  - appeals_pending_count (gauge)
  - trust_score_delta_distribution (histogram)

Dashboards Required (Grafana):
  - PHANTOM SHIELD Live Threat Monitor
  - Risk Tier Distribution by Tenant
  - Signal Class Fire Rate Heatmap (18 classes × time)
  - Review Queue SLA Dashboard (TIER 3: 5-day | TIER 4: 48h | TIER 5: 24h)
  - Weekly Trust Health Report (auto-generated PDF)
  - False Positive Tracking Dashboard

Alerts Required:
  - TIER 5 incident → immediate PagerDuty (P1)
  - TIER 4 review SLA breach (> 48h) → PagerDuty (P2)
  - TIER 3 review queue > 100 items → Slack alert to trust_admin
  - Signal CLASS_09 fire rate spike > 5% → Security alert
  - Signal CLASS_18 (synthetic identity) spike > 2% → Security alert
  - PhantomAuditLog write failure → CRITICAL (P0 — data integrity)
  - Celery queue depth > 500 jobs → Infra alert
```

---

# 🔒 SECTION P — CHANGE GOVERNANCE RULES

## Allowed (No Version Bump Required):
```
Add new evidence field to existing Signal Class output (non-breaking)
Add new canonical alias to identity coherence checks
Add new observable metric to Section O
Add new test case to Section N
Tune signal class weights within ±0.02 per class
Add new trigger type (new trigger = add-only, no removal)
```

## Requires Version Bump (Controlled Change):
```
Change any Signal Class output schema
Change Composite Risk Score formula
Change tier threshold boundaries
Change risk tier action definitions
Change any API endpoint path, method, or schema
Change weight values by > 0.02 per class
Add a new Signal Class (CLASS_19+)
```

## Forbidden (Governance Board Decision Required):
```
Remove any Signal Class
Remove any API endpoint
Lower TIER 5 threshold (making system less sensitive)
Allow any role below trust_admin to access detection data
Modify TrustFlag or PhantomAuditLog immutability constraints
Allow automatic account suspension (enforcement must remain human)
```

---

# 🔒 SECTION Q — MASTER PROMPT SEAL BLOCK

Add this block to the Ecoskiller Master Prompt (SECTION K of Dojo SaaS Artifact) immediately after the ANTIGRAVITY Excel Pattern Detection seal block:

```
═══════════════════════════════════════════════════════════════════
ANTIGRAVITY MODULE — PHANTOM SHIELD
FAKE PROFILE DETECTION MODEL — PRODUCTION ENABLED
Module Code: AG-FPDM-001

Detection Signal Classes:         18 (SIGNAL_CLASS_01–18) LOCKED
Risk Tier System:                 5-Tier (TIER 1–TIER 5) LOCKED
Composite Scoring:                Weighted fusion + hard escalation + cluster rules
PII Protocol:                     user_token only — raw PII never enters PHANTOM SHIELD
Trigger System:                   9 triggers LOCKED (blocking + async + scheduled)
Quarantine Authority:             SOFT = trust_admin | HARD = governance_board only
Enforcement Authority:            Human only — PHANTOM SHIELD does not ban accounts
Immutability:                     TrustFlag + PhantomAuditLog — no UPDATE/DELETE ever
Dojo Trust Bindings:              T3, T5, T7, T9, T10, T14, T15, T16, T17 LOCKED
Trust Score Integration:          Active — tier outcome modifies trust_score
Hiring Marketplace Protection:    TIER 3+ blocks recruiter visibility
Tournament Protection:            TIER 4+ blocks entry
Championship Integrity:           CLASS_16 + T5 binding enforced
Certification Integrity:          CLASS_05 + T17 binding enforced
Integration Protection:           All 100 tool syncs pass through Trust Validation
Migration Protection:             All 1-click migrations enter 14-day TIER 2 hold
API Surface:                      12 endpoints LOCKED
Flutter UI:                       6 trust admin screens LOCKED
React Portal:                     7 trust portal pages LOCKED
Stack:                            Python + FastAPI + NetworkX + PyTorch + Claude API
Change Authority:                 Governance Board for forbidden changes
Interpretation Authority:         NONE
Architecture Authority:           LOCKED
Mutation Policy:                  Add-only via version bump

PHANTOM SHIELD DOES NOT:
  — Store raw PII
  — Ban or delete accounts
  — Override match engine results
  — Access cross-tenant data
  — Operate without immutable audit trail

PHANTOM SHIELD IS:
  — A 18-class fake profile detection engine
  — A trust signal generator and Trust Score modifier
  — A hiring marketplace integrity gate
  — A championship and certification integrity guard
  — A first-class Trust Infrastructure citizen in Ecoskiller Talent OS

SEAL: LOCKED · FINAL · GOVERNED · DETERMINISTIC · PRODUCTION-READY
═══════════════════════════════════════════════════════════════════
```

---

# 🔒 FINAL GOVERNANCE SEAL

```
PHANTOM SHIELD — FAKE PROFILE DETECTION MODEL
ANTIGRAVITY ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
ECOSKILLER × DOJO SAAS — PRODUCTION ARTIFACT v1.0

✔ 18 Signal Classes Defined & Frozen
✔ Processing Pipeline Frozen (14 steps, parallel fan-out)
✔ 9 Detection Triggers Defined & Locked
✔ Composite Risk Score Engine Frozen (weighted + modifiers)
✔ 5-Tier Risk Classification System Locked
✔ Tier Escalation Rules Frozen
✔ 6 Data Entity Schemas Frozen (immutable audit enforcement)
✔ Dojo Trust Lock Alignment (10 sections bound)
✔ 12 Trust Events Published to Event Bus
✔ Trust Score Impact Table Locked
✔ 12 API Endpoints Frozen
✔ PII Protection Protocol Locked
✔ Signal Evidence Rules Locked
✔ Access Segregation Architecture Locked
✔ Flutter Trust Admin UI Locked (6 screens)
✔ React Trust Portal UI Locked (7 pages)
✔ Talent OS Module Integration Mapped (7 modules)
✔ One-Click Integration Protection Layer Sealed
✔ Migration Trust Validation Rules Sealed
✔ Test Gate Requirements Sealed
✔ Observability Layer Sealed (metrics + dashboards + alerts)
✔ Change Governance Rules Locked (3 tiers)
✔ Master Prompt Seal Block Issued

Interpretation Authority:   NONE
Execution Authority:        Human declaration only
Architecture Authority:     LOCKED
Mutation Policy:            Add-only via version bump
Enforcement Model:          Human-governed — no automatic bans

PHANTOM SHIELD SEAL STATUS: ██████████ LOCKED ██████████
```

---

*Document generated under Ecoskiller Enterprise Optimization + Trust Infrastructure governance.*
*Dojo SaaS Production Mode Active. ANTIGRAVITY Layer Engaged. Stack Locked.*
*Mutation Add-Only Versioned. Architecture Interpretation Forbidden.*
