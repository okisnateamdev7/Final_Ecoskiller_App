# 🔒 NETWORK_EXPANSION_CAP_AGENT
## ECOSKILLER — ANTIGRAVITY AGENT SPECIFICATION
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Artifact Class: Production Agent Blueprint
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Companion Agents: DROPOUT_RISK_PREDICTION_AGENT (DRPA) · ATTENDANCE_BEHAVIOR_ANALYTICS_AGENT (ABAA)
### R28 Compliance: ENFORCED (Rule engine primary — Traditional ML for scoring — LLM for advisory text only)
### M5 Compliance: ENFORCED (Human executes training, model cannot self-claim deployment)

---

## ⚠️ SEAL DECLARATION

```
This document is SEALED.
No ambiguity is permitted.
No inference is permitted.
No implicit behavior is permitted.
No network expansion score is issued without a defined, auditable rule or model formula.
No growth action is triggered without a defined, enumerated protocol.
No cap decision is made without a traceable threshold computation chain.
Every score, label, tier, cap event, and expansion trigger in this document executes exactly as written.
Deviation = STOP EXECUTION → REPORT VIOLATION → NO PARTIAL OUTPUT.
```

---

# SECTION I — AGENT IDENTITY

```
Agent Name:           NETWORK_EXPANSION_CAP_AGENT (NECA)
Agent Class:          Antigravity Growth Intelligence Agent
Parent System:        ECOSKILLER — Unified Job + Skill + Project + Education SaaS
Agent Type:           Rule-Gated Network Growth Scorer + Expansion Cap Enforcer + Viral Loop Orchestrator
Execution Mode:       Deterministic signal pipeline → rule-scored tiers → cap enforcement → expansion unlock
R28 Tier Map:         See Section II
M5 Compliance:        Model architecture declared; training and deployment are human-executed responsibilities
Bias Policy:          Growth signals must never use protected demographic attributes (see Section XI)
Human Override:       Required for all HARD-CAP and EMERGENCY-FREEZE interventions
LLM Usage:            Permitted ONLY for human-readable growth summary generation (R28-3)
```

**Agent Mission:**

Monitor, score, govern, and orchestrate the organic network expansion behavior of every tenant, institution, company, student, trainer, and recruiter across the entire Ecoskiller platform. Detect viral growth signals. Enforce expansion caps to prevent abuse, spam, and artificial inflation. Unlock legitimate growth tiers. Suppress fraudulent network propagation. Emit deterministic growth events consumed by the Referral Engine (R52), Badge Engine (R53), Creator Amplification Engine (R54), Streak Engine (R57/R95), and Network Effect Propagation Engine (R58). Never issue a growth tier without a traceable signal. Never unlock an expansion privilege without a defined rule. Never act on incomplete data.

---

# SECTION II — R28 COMPLIANCE DECLARATION (MANDATORY)

Per **R28 — Intelligence Cost Optimization Law**, every intelligence feature must declare its model tier. This agent's full R28 compliance map is:

| Feature | R28 Tier | Model Class |
|---|---|---|
| Network growth score computation | Tier 2 | Gradient Boosting / LightGBM |
| Expansion tier classification (SEED / GROW / SCALE / CAPPED) | Tier 1 | Rule engine — threshold lookup |
| Cap enforcement routing | Tier 1 | Rule engine — deterministic decision tree |
| Viral coefficient computation | Tier 1 | Rule engine — formula-based |
| Fraud network detection | Tier 2 | Isolation Forest (anomaly detection) |
| Expansion unlock eligibility | Tier 1 | Rule engine — condition checklist |
| Human-readable growth summary | Tier 3 | LLM — advisory text only, not a decision input |
| Notification content personalization | Tier 3 | LLM — content generation only |

```
NO LLM MAY COMPUTE OR INFLUENCE A GROWTH SCORE.
NO LLM MAY TRIGGER A CAP DECISION OR EXPANSION UNLOCK.
NO LLM MAY CLASSIFY A NETWORK TIER.
LLM outputs are advisory text only.
Decision authority lives in Tier 1 rules and Tier 2 ML outputs exclusively.
```

## R28-5 Cost Declaration (Required)

| Component | Model | Est. Inference Cost / 1,000 Requests | Est. Monthly Cost at MVP (10K tenants) |
|---|---|---|---|
| Network growth score | LightGBM (self-hosted) | ~$0.001 | ~$0.40 |
| Fraud network detector | Isolation Forest (self-hosted) | ~$0.001 | ~$0.15 |
| LLM growth summary | claude-haiku-4-5 (API) | ~$0.25 | ~$30.00 (triggered only on SCALE tier + CAPPED events) |

```
Absence of R28 cost mapping → STOP EXECUTION
```

---

# SECTION III — M5 COMPLIANCE DECLARATION (MANDATORY)

Per **M5 — AI MODEL REALITY LAW**, this document declares the model architecture. Training, fairness evaluation, and deployment are **human-executed responsibilities**. This agent does NOT claim deployed model status. Deployment requires human execution logs.

**Human-executed responsibilities for NECA:**

```
✗ Acquisition of real network growth training data (from ClickHouse event store)
✗ Execution of LightGBM training pipeline (Python, scikit-learn / LightGBM)
✗ Fairness and bias evaluation (per Section XI of this document)
✗ Model retraining operations (per defined schedule in Section XII)
✗ Production deployment sign-off (requires senior ML engineer + platform admin)

AI MAY define:
✓ Model architecture (this document)
✓ Feature sets (Section VII)
✓ Evaluation metrics (Section X)
✓ Explainability format (Section IX)
✓ Retraining schedule (Section XII)
✓ Fairness criteria (Section XI)

NECA MODEL DEPLOYMENT STATUS: NOT CLAIMED UNTIL HUMAN LOG ATTACHED.
```

---

# SECTION IV — SCOPE LOCK

## 4.1 In-Scope Network Surfaces

| Surface | Signal Source | Growth Window |
|---|---|---|
| Referral Network (R52) | ecoskiller.referral.* events | Per-referral + rolling 30-day |
| Viral Content Sharing (R69) | ecoskiller.share.* events | Per-share + rolling 7-day |
| Network Effect Propagation (R58) | ecoskiller.seo.* + ecoskiller.profile.* events | Rolling 30-day |
| Campus Community Growth (R93) | ecoskiller.group.* + ecoskiller.campus.* events | Per-institution + rolling 30-day |
| Trainer Referral Growth (R86) | ecoskiller.trainer.referral.* events | Per-trainer + rolling 30-day |
| Student Peer Collaboration (R94) | ecoskiller.peer.* events | Per-batch + rolling 30-day |
| Streak Propagation (R57 / R95) | ecoskiller.streak.* events | Daily + rolling 7-day |
| Badge Virality (R53 / R91) | ecoskiller.badge.* events | Per-badge-award event |
| Microcommunity Growth (R55) | ecoskiller.community.* events | Per-community lifecycle |
| Recruiter Network Expansion | ecoskiller.recruiter.network.* events | Rolling 30-day |
| Dojo Match Invitations | ecoskiller.dojo.invite.* events | Per-match + rolling 7-day |

## 4.2 In-Scope Entity Types (from 300-type master list)

```
Class A — Students & Learners (Types 1–40):     Full growth scoring + cap enforcement + expansion routing
Class B — Trainers & Educators (Types 41–75):   Full growth scoring + referral cap enforcement
Class C — Institute Staff (Types 76–110):        Tenant-level growth scoring + campus expansion cap
Class D — Employers / Recruiters (Types 111–160): Recruiter network expansion scoring + job virality cap
Class E — Freelancers (Types 161–200):           Project virality scoring + collaboration cap
Class F — Government / NGO (Types 201–230):       Aggregate-only network analytics — no individual cap
Class G — Platform Ops (Types 231–270):          Full visibility — no personal growth scoring
Class H — Future Roles (Types 271–300):          Scoped under digital twin tracks when live
```

## 4.3 Out-of-Scope (Forbidden)

```
FORBIDDEN: Applying growth caps based on protected demographic attributes
FORBIDDEN: Blocking organic growth of verified legitimate users without a defined cap rule
FORBIDDEN: Issuing expansion unlock to any entity with fewer than 3 network events on record
FORBIDDEN: Using growth tier as a hiring signal or shared with employers without consent
FORBIDDEN: Suppressing minor user network activity without parent/guardian consent record
FORBIDDEN: Any LLM involvement in computing or adjusting the network growth score
FORBIDDEN: Triggering cap enforcement when NECA signal feed has not synced within last 48 hours
FORBIDDEN: Generating a growth score when entity has zero verified connections on record
FORBIDDEN: Issuing two conflicting expansion tiers for the same entity in the same 24-hour window
FORBIDDEN: Sharing individual growth scores or cap records with third parties without consent
```

---

# SECTION V — ARCHITECTURE PLACEMENT

## 5.1 System Position

```
NECA sits in:
  Kubernetes namespace: analytics
  Co-located with: DRPA, ABAA
  Dependency direction:
    Consumes FROM: Referral Engine (R52), Badge Engine (R53), Creator Engine (R54),
                   Streak Engine (R57/R95), Network Propagation Engine (R58),
                   Campus Community (R93), Peer Collaboration (R94),
                   Trainer Referral (R86), Core Platform Activity Layer
    Produces TO:  Notification Service, Admin Governance, Platform Ops Dashboard,
                  Billing Service (growth metering), Feature Flag Manager (R39)

NECA is a READER and EMITTER. It does not modify session state, user profiles, or scores.
NECA never writes to ClickHouse directly.
NECA emits Kafka events → consumed by Notification, Governance, Billing, and Dashboard services.
```

## 5.2 Infrastructure Dependencies (Locked)

| Component | Role | NECA Usage |
|---|---|---|
| Apache Kafka | Event bus | Signal ingestion + growth event emission |
| ClickHouse | Analytics store | Feature query for rolling window aggregations |
| PostgreSQL | Source of truth | Entity profile, enrollment, verification state |
| Redis | State cache | Active expansion tier per entity (TTL: 24 hours) |
| Feature Store Service | ML feature storage | Centralized feature vectors for model inference |
| Model Registry Service | ML model versioning | Versioned LightGBM model artifact storage |
| Embedding & Model Inference Service | Model serving | LightGBM inference endpoint (self-hosted) |
| Vector Database Service (Qdrant) | Similarity search | Fraud network cluster similarity detection |
| Stream Processing Service | Real-time pipeline | Rolling window feature computation via Kafka Streams |
| Grafana | Dashboard rendering | Growth dashboards for platform ops and tenant admins |
| MinIO | Object storage | Growth audit archives, model artifacts |
| OpenTelemetry | Tracing | Full trace per growth computation |
| Prometheus + Loki | Metrics + Logs | Agent health, computation audit logs |
| Unleash | Feature flag control | Expansion privilege gating per tenant/user |

## 5.3 Data Flow (Non-Negotiable)

```
[Referral Events]  +  [Share Events]  +  [Badge Events]  +  [Streak Events]
+  [Community Events]  +  [Peer Events]  +  [SEO Propagation Events]
              ↓
     [Stream Processing Service]
     (rolling window feature computation — Kafka Streams)
              ↓
     [Feature Store Service]
     (feature vector stored per entity per computation window)
              ↓
     [Model Inference Service]
     (LightGBM inference → raw growth probability 0.0–1.0)
              ↓
     [Expansion Tier Classifier — RULE ENGINE (Tier 1)]
     (probability + rule conditions → SEED / GROW / SCALE / CAPPED)
              ↓
     [Cap Enforcement Engine — RULE ENGINE (Tier 1)]
     (tier × entity_type × cap_condition → defined enforcement action)
              ↓
     [Growth Score Store — Redis (TTL: 24h) + ClickHouse (permanent)]
              ↓
     [Kafka: ecoskiller.neca.growth_scored / ecoskiller.neca.cap_triggered / ecoskiller.neca.expansion_unlocked]
              ↓
     [Notification Service] + [Admin Governance] + [Billing Service] + [Platform Ops Dashboard]
              ↓ (SCALE tier + CAPPED events only, on-demand)
     [LLM Growth Summary Generator — Tier 3]
     (human-readable growth summary text — NOT a decision input)
```

---

# SECTION VI — EVENT SCHEMA (SEALED)

## 6.1 Kafka Topics Consumed from Growth Layer

```
ecoskiller.referral.code_created
ecoskiller.referral.invite_sent
ecoskiller.referral.accepted
ecoskiller.referral.fraud_flagged
ecoskiller.share.link_generated
ecoskiller.share.link_clicked
ecoskiller.share.external_platform_detected
ecoskiller.badge.awarded
ecoskiller.badge.viral_share_triggered
ecoskiller.streak.updated
ecoskiller.streak.shared_externally
ecoskiller.community.member_joined
ecoskiller.community.invite_sent
ecoskiller.community.waitlist_activated
ecoskiller.community.prestige_score_updated
ecoskiller.peer.project_created
ecoskiller.peer.member_invited
ecoskiller.peer.member_joined
ecoskiller.trainer.referral_sent
ecoskiller.trainer.referral_accepted
ecoskiller.dojo.match_invite_sent
ecoskiller.dojo.match_invite_accepted
```

## 6.2 Kafka Topics Consumed from Platform Activity Layer

```
ecoskiller.user.created
ecoskiller.user.verified
ecoskiller.seo.page_generated
ecoskiller.seo.page_indexed
ecoskiller.profile.public_viewed
ecoskiller.profile.external_share
ecoskiller.job.applied
ecoskiller.job.viral_share_detected
ecoskiller.course.enrolled
ecoskiller.course.viral_invite_triggered
ecoskiller.success_story.published
ecoskiller.success_story.external_share
ecoskiller.user.login
ecoskiller.streak.reset
```

## 6.3 Kafka Topics Emitted by NECA

```
ecoskiller.neca.growth_scored
ecoskiller.neca.tier_changed
ecoskiller.neca.cap_triggered
ecoskiller.neca.expansion_unlocked
ecoskiller.neca.fraud_network_detected
ecoskiller.neca.stale_data_warning
ecoskiller.neca.cap_lifted
ecoskiller.neca.growth_anomaly_detected
```

## 6.4 Canonical Growth Scored Event Envelope (Sealed)

```json
{
  "event_id": "uuid-v4",
  "event_type": "ecoskiller.neca.growth_scored",
  "entity_id": "uuid-v4",
  "entity_type": "user | tenant | institution | company | trainer | recruiter",
  "tenant_id": "uuid-v4",
  "user_type_code": "integer (1–300)",
  "timestamp_utc": "ISO-8601",
  "growth_probability": "float 0.0–1.0",
  "expansion_tier": "SEED | GROW | SCALE | CAPPED",
  "previous_tier": "SEED | GROW | SCALE | CAPPED | null",
  "tier_changed": "boolean",
  "viral_coefficient": "float 0.0–N.N",
  "feature_snapshot_id": "uuid-v4",
  "model_version": "string",
  "computation_window_days": "integer",
  "cap_active": "boolean",
  "cap_reason": "string | null",
  "expansion_unlock_triggered": "boolean",
  "fraud_flag": "boolean",
  "summary_available": "boolean",
  "schema_version": "1.0"
}
```

**Schema violations → event discarded → dead-letter topic → alert → no partial emission.**

---

# SECTION VII — FEATURE CATALOG (SEALED)

All features are computed by the Stream Processing Service from growth layer events and platform activity events. No feature may be computed outside this catalog without a version bump.

## 7.1 Referral & Invitation Features

| Feature Name | Computation | Window |
|---|---|---|
| `f_referral_sent_count` | count of referral.invite_sent events | Rolling 30 days |
| `f_referral_accepted_count` | count of referral.accepted events | Rolling 30 days |
| `f_referral_conversion_rate` | referral_accepted / referral_sent | Rolling 30 days |
| `f_referral_fraud_flags` | count of referral.fraud_flagged events | Rolling 30 days |
| `f_referral_channel_diversity` | count of distinct channels used (link / QR / email / WhatsApp / SMS) | Rolling 30 days |
| `f_days_since_last_referral` | today − last referral.invite_sent timestamp | Absolute |

## 7.2 Viral Sharing & Content Features

| Feature Name | Computation | Window |
|---|---|---|
| `f_share_links_generated` | count of share.link_generated events | Rolling 7 days |
| `f_share_link_click_rate` | share.link_clicked / share.link_generated | Rolling 7 days |
| `f_external_platform_shares` | count of share.external_platform_detected events | Rolling 7 days |
| `f_success_story_shares` | count of success_story.external_share events | Rolling 30 days |
| `f_seo_pages_generated` | count of seo.page_generated events | Rolling 30 days |
| `f_seo_pages_indexed` | count of seo.page_indexed events | Rolling 30 days |
| `f_profile_external_views` | count of profile.external_share events | Rolling 30 days |

## 7.3 Community & Peer Network Features

| Feature Name | Computation | Window |
|---|---|---|
| `f_community_invites_sent` | count of community.invite_sent events | Rolling 30 days |
| `f_community_members_joined` | count of community.member_joined events (attributed to entity) | Rolling 30 days |
| `f_community_prestige_score` | current CommunityPrestigeScore from community service | Current |
| `f_peer_project_invites_sent` | count of peer.member_invited events | Rolling 30 days |
| `f_peer_project_members_joined` | count of peer.member_joined events (attributed) | Rolling 30 days |
| `f_dojo_invites_sent` | count of dojo.match_invite_sent events | Rolling 7 days |
| `f_dojo_invite_acceptance_rate` | dojo.match_invite_accepted / dojo.match_invite_sent | Rolling 7 days |

## 7.4 Streak & Habit Propagation Features

| Feature Name | Computation | Window |
|---|---|---|
| `f_streak_shared_externally_count` | count of streak.shared_externally events | Rolling 7 days |
| `f_badge_viral_shares` | count of badge.viral_share_triggered events | Rolling 30 days |
| `f_current_streak` | streak_count from StreakTracker (R57/R95) | Current |
| `f_streak_resets_30d` | count of streak.reset events | Rolling 30 days |

## 7.5 Network Growth Aggregate Features

| Feature Name | Computation | Window |
|---|---|---|
| `f_total_attributed_signups` | total users registered via entity's referral or share links | Cumulative |
| `f_viral_coefficient` | avg new users generated per existing user = f_referral_accepted_count / active_base | Rolling 30 days |
| `f_network_depth_2` | count of second-degree connections activated by entity's direct referrals | Rolling 30 days |
| `f_days_since_last_network_event` | today − last growth layer event of any type | Absolute |
| `f_active_growth_channels` | count of distinct growth channels with ≥1 event in window | Rolling 30 days |

## 7.6 Derived Cap Indicator Features (Rule-Computed, Tier 1)

These are binary flags computed by the rule engine before ML inference. They serve as hard-signal inputs.

| Feature Name | Rule |
|---|---|
| `f_flag_fraud_referral_cluster` | f_referral_fraud_flags >= 3 in rolling 7 days |
| `f_flag_self_referral_loop` | referral accepted by entity's own secondary account (device or IP match) |
| `f_flag_mass_invite_burst` | f_community_invites_sent >= 50 in rolling 24 hours |
| `f_flag_zero_conversion` | f_referral_sent_count >= 20 AND f_referral_conversion_rate == 0.0 (rolling 30d) |
| `f_flag_artificial_share_burst` | f_share_links_generated >= 100 AND f_share_link_click_rate < 0.01 (rolling 7d) |
| `f_flag_viral_coefficient_spike` | f_viral_coefficient > 5.0 in rolling 7 days (anomaly threshold) |
| `f_flag_network_dormancy` | f_days_since_last_network_event >= 21 |

---

# SECTION VIII — GROWTH SCORING MODEL (SEALED)

## 8.1 Model Declaration (R12 Compliant)

```
Model Type:           LightGBM Binary Classifier (or XGBoost — team decision at training time)
Problem:              Binary classification — Legitimate Organic Growth (1 = organic, 0 = artificial/stalled)
Output:               Probability score 0.0–1.0 (growth_probability)
Input:                Feature vector from Section VII (25 features + 7 derived flags = 32 total)
Training Data:        Historical growth layer events from ClickHouse
                      Minimum 90 days of data before first training run
                      Label definition: entity that achieved ≥2 verified referral acceptances
                      AND ≥1 external share click within 30-day window (positive class)
Evaluation Metrics:   AUC-ROC (primary), Precision@Recall-0.8, F1-Score
Explainability:       SHAP values computed for every inference (stored in Feature Store)
Retraining:           See Section XII
Deployment:           Human-gated (M5 compliance)
```

## 8.2 Expansion Tier Classification (Tier 1 Rule Engine — NOT ML)

```
growth_probability + rule conditions → expansion_tier mapping (immutable):

  SEED:   growth_probability < 0.30
          OR entity has < 3 total network events on record
          → New entrant. No viral mechanics unlocked yet.

  GROW:   growth_probability >= 0.30 AND < 0.65
          AND f_flag_fraud_referral_cluster = FALSE
          AND f_flag_self_referral_loop = FALSE
          → Active organic growth. Standard viral mechanics active.

  SCALE:  growth_probability >= 0.65
          AND f_flag_fraud_referral_cluster = FALSE
          AND f_flag_artificial_share_burst = FALSE
          AND f_viral_coefficient >= 1.0
          → High-velocity organic growth. Enhanced viral privileges unlocked.

  CAPPED: ANY cap override condition met (see Section VIII.3)
          → Growth mechanics suspended. Enforcement active.

Threshold change = version bump + human declaration + fairness re-audit.
No threshold may be changed in production without all three.
```

## 8.3 Fast-Path Cap Override (Tier 1 — Applied BEFORE ML Inference)

When any of the following conditions are met, the expansion tier is set to CAPPED by rule, bypassing the ML model. The ML score is still computed and stored for audit, but the tier is overridden.

```
OVERRIDE TO CAPPED (immediate, no ML required):
  Condition A: f_flag_fraud_referral_cluster = TRUE
  Condition B: f_flag_self_referral_loop = TRUE
  Condition C: f_flag_mass_invite_burst = TRUE
  Condition D: f_flag_artificial_share_burst = TRUE
  Condition E: f_referral_fraud_flags >= 5 in rolling 7 days (hard stop)
  Condition F: entity has active fraud_flag = TRUE from Fraud Detection Engine (Section XIV)
  Condition G: entity's network generates > 50 signups in < 24 hours without verified identity chain
  Condition H: entity emits > 200 share links in rolling 24 hours

OVERRIDE TO GROW (fast-path, ML still runs):
  Condition I: f_viral_coefficient_spike = TRUE AND fraud signals absent → audit flag only, no cap

CAP OVERRIDE REASON must be logged with the emitted neca.cap_triggered event.
Absence of reason code → STOP EMISSION → LOG NECA_CAP_REASON_MISSING
```

## 8.4 Expansion Unlock Protocol (Tier 1 Rule Engine)

```
Unlock from SEED → GROW:
  Condition: 3+ distinct network events in rolling 14 days
  AND entity.verified_status = TRUE
  AND f_flag_fraud_referral_cluster = FALSE
  → emit ecoskiller.neca.expansion_unlocked
  → notify entity via Notification Service
  → activate standard viral mechanics via Unleash feature flag

Unlock from GROW → SCALE:
  Condition: growth_probability >= 0.65
  AND f_viral_coefficient >= 1.0 (rolling 30 days)
  AND zero CAPPED events in last 30 days
  AND f_flag_artificial_share_burst = FALSE
  → emit ecoskiller.neca.expansion_unlocked (SCALE)
  → activate enhanced viral privileges via Unleash
  → alert Platform Ops (Type 232) for manual review before full unlock

Lift CAPPED tier:
  Requires: Human review by Platform Ops (Type 232) OR Admin Governance (Type 242)
  AND: zero re-trigger conditions for 7 consecutive days
  AND: human sign-off log entry attached
  THEN: → emit ecoskiller.neca.cap_lifted
        → tier resets to SEED (not to prior tier)
        → Unleash flags reset to SEED defaults
  Auto-lift is FORBIDDEN. CAPPED tier requires human unlock only.
```

## 8.5 Minimum Data Guard

```
IF total_network_events < 3 for the entity in the computation window:
  → tier = SEED (default)
  → DO NOT run ML inference
  → DO NOT emit growth_scored event
  → Log: NECA_INSUFFICIENT_DATA
  → No cap action triggered

Growth scoring begins only after 3 confirmed network events.
```

---

# SECTION IX — EXPLAINABILITY FORMAT (SEALED)

## 9.1 SHAP-Based Feature Attribution

Every inference produces a SHAP value vector stored in the Feature Store with the feature_snapshot_id. SHAP values are human-readable only — they do not re-enter the scoring pipeline.

## 9.2 Human-Readable Growth Summary (Tier 3 — LLM, SCALE and CAPPED tiers only)

```
Trigger:     expansion_tier == SCALE OR expansion_tier == CAPPED
             AND summary_requested == true
Model:       claude-haiku-4-5 (cost-optimized, R28-3 compliant)
Input:       Top 5 SHAP features by absolute value + feature values + entity_type_label + cap_reason (if CAPPED)
Output:      2–3 sentence plain-language explanation of growth signal contributors

CONSTRAINTS ON LLM SUMMARY:
  MUST NOT mention the numeric growth_probability score
  MUST NOT use labels like "going viral", "explosive growth", "at risk of ban"
  MUST use neutral, factual framing: "This entity's network activity shows..."
  MUST NOT make recommendations requiring human judgment (e.g., "investigate this user")
  MUST be stored alongside SHAP snapshot — never displayed without it
  MUST be marked: summary_type: LLM_GENERATED, NOT_A_DECISION
```

## 9.3 Summary Delivery Rules

```
Entity self-view:      Receives constructive summary only — NO growth_probability shown
                       ("Your network is expanding organically" / "Your growth is paused for review")
Tenant Admin:          Receives expansion_tier + top 3 contributing factor labels (feature names, not values)
Platform Ops:          Receives full SHAP breakdown + LLM summary
Employer:              NO individual growth data shared under any circumstance
Minor entity:          Simplified "activity summary" only — no tier labels, no cap labels
                       Requires guardian consent for any growth record visibility
```

---

# SECTION X — EVALUATION METRICS (R12 COMPLIANT)

## 10.1 Model Quality Metrics

| Metric | Minimum Acceptable Threshold | Measurement Frequency |
|---|---|---|
| AUC-ROC | ≥ 0.76 | Per training run |
| Precision at Recall=0.80 | ≥ 0.63 | Per training run |
| F1-Score | ≥ 0.68 | Per training run |
| False Positive Rate (GROW falsely CAPPED) | ≤ 0.04 | Per training run |

```
Model below any threshold → REJECTED → human review required before deployment
```

## 10.2 Operational Performance Metrics (Prometheus)

| Metric | SLA |
|---|---|
| Feature computation lag (event → Feature Store) | < 15 seconds |
| Model inference latency (p99) | < 600 ms |
| Growth event emission lag (inference → Kafka) | < 3 seconds |
| Daily growth scoring coverage (% of active entities scored) | ≥ 93% |
| Stale data rate (entities skipped due to signal feed gap > 48h) | < 5% |
| Cap override enforcement latency (condition met → Kafka emitted) | < 5 seconds |

---

# SECTION XI — FAIRNESS & BIAS GOVERNANCE (MANDATORY)

## 11.1 Protected Attributes (Never Used as Features)

```
FORBIDDEN as features — direct or proxy:
  Gender
  Religion
  Caste / Social category
  Region / State of origin
  Language preference
  College tier (direct input)
  Family income proxy
  First-generation user status
  Disability status
  Age (except for minor protection rules — used only for consent gating, NOT scoring)

Any feature found to be a proxy for a protected attribute after SHAP analysis
→ REMOVE from feature catalog → version bump → retrain → re-audit
```

## 11.2 Fairness Metrics (Human-Evaluated Pre-Deployment)

| Fairness Check | Method | Threshold |
|---|---|---|
| Demographic parity across entity types | AUC-ROC disparity | ≤ 0.05 gap |
| Equalized odds across user_type_code groups | TPR/FPR comparison | ≤ 0.05 gap |
| Calibration across tenant_id | Mean predicted vs actual organic growth rate | ≤ 0.05 deviation |
| SHAP feature audit for proxy discrimination | Manual review by human | No protected attribute proxy in top 10 SHAP features |
| Cap rate parity across institution types | Cap_rate per institution tier | ≤ 0.08 gap |

```
Fairness audit failing any threshold → MODEL NOT DEPLOYED.
Fairness audit is human responsibility (M5 compliance).
```

## 11.3 Disparate Impact Log

```
Every batch scoring run produces a disparate_impact_log stored in MinIO.
Log contains: user_type_code distribution of CAPPED-tier assignments,
              per-tenant CAPPED rate, per-institution CAPPED rate,
              and SHAP feature dominance summary.
Log retained: 3 years (compliance requirement).
Log reviewed: quarterly by platform governance team.
```

---

# SECTION XII — RETRAINING SCHEDULE (SEALED)

## 12.1 Scheduled Retraining

```
Frequency:    Monthly (second Sunday of each month — Airflow DAG)
Data window:  Last 180 days of growth layer events from ClickHouse
Label window: 30-day forward window from feature snapshot date
Trigger:      Apache Airflow scheduled DAG: neca_monthly_retrain
Human gate:   Model diff report must be reviewed and approved before swap
Rollback:     Previous model version retained in Model Registry for 90 days
```

## 12.2 Drift-Triggered Retraining

```
Trigger conditions (any one):
  - AUC-ROC on holdout set drops below 0.72 for 3 consecutive daily checks
  - CAPPED tier assignment rate changes by > 20% week-over-week (population drift)
  - Mean feature value shift > 2 standard deviations in any top-5 SHAP feature
  - f_viral_coefficient distribution shifts by > 30% median from prior 30-day baseline
  - New major growth feature added to platform (R52–R58, R91–R95 extensions)

Action:
  → Emit ecoskiller.neca.growth_anomaly_detected
  → Alert: ML Engineer + Platform Admin
  → Human initiates emergency retraining
  → Model frozen at current version until new model passes evaluation
```

## 12.3 Model Version Registry

```
Model artifact storage: MinIO bucket: neca-models/
Version naming: neca_v{MAJOR}.{MINOR}_{YYYY-MM-DD}
Each version stores:
  - Trained model binary (.pkl or .ubj)
  - Feature schema version
  - Evaluation metrics JSON
  - Fairness audit report
  - SHAP summary plot
  - Human sign-off log entry
Versions retained: Last 5 production versions minimum.
```

---

# SECTION XIII — EXPANSION CAP ENFORCEMENT PROTOCOL (DETERMINISTIC)

## 13.1 Expansion Tier → Action Matrix

This matrix is the sole authority for what happens when an expansion tier is assigned or changes. No action outside this matrix is permitted.

| Expansion Tier | Entity Type | Condition | Enforcement Action | Actor |
|---|---|---|---|---|
| SEED | Any | Default entry state | No action. Viral mechanics locked. Growth scored daily. | System |
| SEED | Any | SEED for 14 consecutive days with ≥3 events | Nudge notification: "Complete your profile to unlock network features" | Notification Service |
| GROW | Student (Types 1–40) | First GROW | Push notification: growth milestone + referral CTA | Notification Service |
| GROW | Trainer (Types 41–75) | First GROW | Email: referral program activation notice | Notification Service |
| GROW | Any | GROW for 7+ consecutive days | Activate standard viral mechanics via Unleash | Feature Flag Manager |
| SCALE | Any | First SCALE | Unlock enhanced viral privileges + emit expansion_unlocked | Feature Flag Manager + Notification Service |
| SCALE | Any | SCALE for 3+ consecutive scoring cycles | Alert Platform Ops (Type 232) for audit review before extended privileges | Admin Governance |
| CAPPED | Any | Condition A–H triggered | Suspend all viral mechanics immediately via Unleash | Feature Flag Manager |
| CAPPED | Any | First CAPPED event | Push + Email: "Your network activity is under review" (no technical detail) | Notification Service |
| CAPPED | Any | CAPPED persists > 7 days | Alert Admin Governance (Type 242) + Tenant Admin | Admin Governance |
| CAPPED | Minor entity | Any CAPPED | Alert Parent trust layer (constructive framing only) | Notification Service → Parent |
| CAP LIFTED | Any | Human sign-off + 7 clean days | Reset to SEED → re-activate standard mechanics | System + Human |
| FRAUD DETECTED | Any | f_flag_fraud_referral_cluster + Isolation Forest anomaly | Suspend account viral access + alert Security Admin | Admin Governance |

## 13.2 Cap Enforcement Content Rules

```
All notifications for CAPPED events to the entity:
  MUST use neutral, non-accusatory framing
  MUST NOT disclose specific fraud detection signals or model scores
  MUST NOT use words "banned", "fraud", "investigation", "suspended" in user-facing text
  MUST include a clear appeal pathway link
  MUST be generated by Notification Service using defined templates
  MAY be personalized by LLM (Tier 3) for SCALE events only

All administrative alerts to Admin Governance:
  MUST include: entity_id, expansion_tier, top 3 contributing feature labels (no raw values)
  MUST NOT include: growth_probability score or LLM summary text
  MUST include: cap_reason code + suggested human action (defined template — not LLM-generated)
  MUST be delivered via Admin Governance Service Dashboard

All Platform Ops alerts:
  MUST include: full SHAP breakdown + LLM summary + cap_reason + entity type
  MUST include: fraud_flag status + Isolation Forest anomaly score
```

## 13.3 Appeal Protocol (Deterministic)

```
Appeal pathway for CAPPED entities:
  → Entity submits appeal via /api/v1/neca/appeal/{entity_id}
  → Creates AppealRecord in PostgreSQL
  → Notifies Admin Governance (Type 242)
  → Human reviews within 72 hours (SLA)
  → Human decision: LIFT → emit cap_lifted | UPHOLD → log decision + notify entity
  → No automatic appeal resolution permitted
  → Appeal outcome logged to MinIO audit archive

Appeal review is a HUMAN-ONLY action.
No rule engine or ML model may resolve an appeal.
```

## 13.4 Enforcement Outcome Tracking

```
Every triggered cap or expansion unlock is logged:
  - enforcement_id (UUID)
  - entity_id
  - expansion_tier_at_trigger
  - enforcement_type (CAP | UNLOCK | FRAUD_SUSPEND)
  - delivered_at
  - appeal_submitted: boolean
  - appeal_outcome: LIFTED | UPHELD | PENDING | null
  - tier_30d_after_enforcement (populated asynchronously)
  - resolved: boolean

Outcome data feeds into model retraining (behavioral label enrichment).
Outcome log retained: 3 years.
```

---

# SECTION XIV — FRAUD NETWORK DETECTION (SEALED)

## 14.1 Fraud Detection Model (Tier 2)

```
Model Type:           Isolation Forest (anomaly detection)
Problem:              Detect artificial network inflation — fake referrals, coordinated share bursts,
                      self-referral loops, synthetic signup chains
Output:               Anomaly score 0.0–1.0 (fraud_anomaly_score)
Threshold:            fraud_anomaly_score >= 0.85 → fraud_flag = TRUE
Features:             f_referral_fraud_flags, f_referral_conversion_rate,
                      f_share_link_click_rate, f_viral_coefficient,
                      f_flag_self_referral_loop, f_flag_mass_invite_burst,
                      f_network_depth_2, f_active_growth_channels,
                      f_days_since_last_network_event
Training Data:        Historical labeled fraud cases from Admin Governance audit logs
                      Minimum 60 days of data before first training run
Deployment:           Human-gated (M5 compliance)
```

## 14.2 Fraud Cluster Detection (Tier 2)

```
Method:   Vector Database (Qdrant) — entity embedding similarity search
Purpose:  Detect coordinated networks of entities exhibiting synchronized referral patterns

Algorithm:
  Every 6 hours:
  → Compute entity growth behavior embedding (feature vector from Section VII)
  → Store in Qdrant with entity_id
  → Query: entities with cosine_similarity > 0.95 in rolling 24-hour window
  → If cluster size >= 5 entities with synchronized referral acceptance spikes:
    → Emit ecoskiller.neca.fraud_network_detected
    → Flag all cluster members for Admin Governance review
    → Do NOT auto-CAPPED without human confirmation
    → Human confirms → CAPPED applied to cluster
    → Human rejects → fraud_flag cleared + log decision
```

---

# SECTION XV — DASHBOARD SPECIFICATIONS

## 15.1 Role-Based Growth Dashboard Matrix

| Dashboard | Visible To | Content |
|---|---|---|
| Entity Self-View | Any user (self only) | Growth tier label (no probability), referral count, share performance, streak status |
| Minor Self-View | Minor user (self only) | "Activity summary" only — no tier labels, no cap labels |
| Parent Trust View | Parent (linked minor only) | "Network activity summary" — no cap detail, no tier |
| Tenant Admin | Tenant Admin (Type 77, Type 95) | Cohort growth heatmap, CAPPED count, expansion tier distribution, referral conversion rates |
| Institute Admin | Institution Admin (Type 77) | Campus network expansion by batch, CAPPED students count, viral mechanics active rate |
| Recruiter | Employer Recruiter (Type 122) | Aggregate job application virality metrics only — no individual growth data |
| Platform Ops | Analytics Admin (Types 258, 259), Platform Admin (Type 232) | All tenants, model health, CAPPED rate by tenant, fraud cluster alerts, fairness metrics |

## 15.2 Mandatory Dashboard Panels

**Tenant Admin Growth Dashboard must include:**
```
- Cohort expansion tier heatmap: entity × expansion_tier (current week)
- CAPPED entities count + trend (last 30 days)
- Viral coefficient by entity type (student / trainer / recruiter)
- Top growth channels (referral / share / badge / streak)
- CAPPED appeal queue status
- Export: CSV of CAPPED entities (GDPR/DPDP compliant, anonymized option)
```

**Platform Ops Dashboard must include:**
```
- Daily growth scoring coverage %
- CAPPED rate by tenant (anomaly detection alert if > 15% in any tenant)
- Fraud cluster detection count (last 7 days)
- Viral coefficient platform median + percentile distribution
- Model AUC-ROC on daily holdout (drift monitor)
- Stale data rate (signal feed gap > 48h)
- Fairness metric last audit date + pass/fail
- Retraining pipeline last run + next scheduled
- Appeal resolution SLA compliance (% resolved < 72h)
```

---

# SECTION XVI — DATA GOVERNANCE (SEALED)

## 16.1 Retention Policy

| Data Type | Retention | Storage |
|---|---|---|
| Feature vectors (per scoring run) | 90 days | Feature Store (hot) |
| Growth scores (all tiers) | 1 year | ClickHouse |
| SHAP value snapshots | 1 year | Feature Store |
| Cap enforcement logs | 3 years | PostgreSQL |
| Appeal records | 3 years | PostgreSQL |
| Fairness audit reports | 3 years | MinIO |
| Model versions | 5 production versions minimum | MinIO (neca-models/) |
| Disparate impact logs | 3 years | MinIO |
| Fraud network detection records | 3 years | MinIO |
| LLM-generated growth summaries | 90 days | PostgreSQL (text field) |
| Minor entity growth records | 90 days maximum unless consent extended | Flagged partition |

## 16.2 Privacy Constraints (Non-Negotiable)

```
PII (name, email, phone) NEVER stored in Feature Store or ClickHouse.
All stores reference entity_id (UUID) only.
Name resolution at display time via User Service join only.
Growth probability scores NEVER visible to employers or recruiters (individual level).
Cap labels NEVER shared to third parties without explicit entity consent.
Cross-tenant growth data access: FORBIDDEN at database level (RLS enforced on tenant_id).
Fraud cluster identities NEVER exposed in any public dashboard.
```

## 16.3 Consent Architecture

```
Consent required before:
  - Emitting growth data to Tenant Admin (entity consent or enrollment agreement)
  - Emitting growth data to employer (explicit per-batch consent)
  - Storing minor entity growth records (guardian consent)
  - Using entity growth data in model retraining (terms of service consent)

Consent registry in PostgreSQL (shared with ABAA and DRPA):
  consent_type: GROWTH_SCORING | TENANT_VISIBILITY | EMPLOYER_SHARE | MODEL_TRAINING

Consent missing → score computed internally for cap enforcement use only
               → NOT emitted to any external surface
               → Logged as NECA_CONSENT_GATED
```

---

# SECTION XVII — INTEGRATION CONTRACTS

## 17.1 Upstream Services (Signal Producers)

| Service | Signal Type | Contract Status |
|---|---|---|
| Referral Engine (R52) | referral.* events | LOCKED |
| Share Mechanics Service (R69) | share.* events | LOCKED |
| Badge Engine (R53 / R91) | badge.* events | LOCKED |
| Streak Engine (R57 / R95) | streak.* events | LOCKED |
| Community Engine (R55) | community.* events | LOCKED |
| Peer Collaboration Service (R94) | peer.* events | LOCKED |
| Trainer Referral Engine (R86) | trainer.referral.* events | LOCKED |
| Dojo Match Engine | dojo.invite.* events | LOCKED |
| Network Propagation Engine (R58) | seo.* + profile.* events | LOCKED |
| Success Story Service (R80) | success_story.* events | LOCKED |
| User Service | verified_status, user_type_code | LOCKED |
| Fraud Detection Engine (Section XIV) | fraud_anomaly_score | LOCKED |

## 17.2 Downstream Services (Consumers of NECA output)

| Service | Kafka Topic Consumed | Purpose |
|---|---|---|
| Notification Service | neca.cap_triggered, neca.expansion_unlocked | Entity nudges + cap notices |
| Admin Governance Service | neca.growth_scored, neca.cap_triggered, neca.fraud_network_detected | Tenant/admin alerts |
| Billing Service | neca.growth_scored | Growth metering (billable events per tier) |
| Analytics Service | neca.growth_scored | Platform-level growth analytics |
| Feature Flag Manager (Unleash) | neca.expansion_unlocked, neca.cap_triggered | Viral privilege activation/suspension |
| Model Registry Service | (internal) | Model artifact versioning |

## 17.3 API Contract (Sealed)

```
GET  /api/v1/neca/growth/{entity_id}                    — Current tier + top factors (role-gated)
GET  /api/v1/neca/cohort/{tenant_id}/summary             — Aggregate growth summary for tenant
GET  /api/v1/neca/institution/{tenant_id}/heatmap        — Growth heatmap data
GET  /api/v1/neca/caps/{entity_id}                       — Cap history (role-gated)
POST /api/v1/neca/appeal/{entity_id}                     — Submit cap appeal
GET  /api/v1/neca/appeals                                — Appeal queue (ADMIN_GOVERNANCE+ only)
PUT  /api/v1/neca/appeals/{appeal_id}/resolve            — Resolve appeal (HUMAN, ADMIN_GOVERNANCE only)
GET  /api/v1/neca/fraud/clusters                         — Active fraud cluster list (PLATFORM_OPS only)
GET  /api/v1/neca/model/health                           — Model AUC, last retrain, fairness status
POST /api/v1/neca/export/capped                          — CSV export (TENANT_ADMIN+ only)

All endpoints:
  - JWT required
  - RBAC enforced via OPA
  - Tenant-scoped: cross-tenant request → 403 Forbidden
  - Rate limited: 100 req/min per user
  - growth_probability field: NEVER returned to student-role or employer-role callers
  - cap_reason: NEVER returned to entity self-view (only constructive summary)
  - Response schema versioned
```

---

# SECTION XVIII — DEVOPS & OBSERVABILITY

## 18.1 Kubernetes Deployment

```
Namespace: analytics (shared with DRPA, ABAA)
Services:
  - neca-feature-pipeline        (Kafka Streams processor, stateless, scalable)
  - neca-inference-server         (LightGBM model server, stateless, horizontal)
  - neca-cap-enforcement-router   (rule engine, stateless)
  - neca-fraud-detector           (Isolation Forest + Qdrant, stateless)
  - neca-api-server               (REST API, stateless)
  - neca-scheduler                (Airflow DAG runner — retraining, fairness checks)

All services: Docker containers, Helm charts, blue/green deploy.
Model swap: zero-downtime via model version toggle in Model Registry.
No manual production deploys permitted.
```

## 18.2 Required Observability

```
Metrics (Prometheus):
  neca_features_computed_total               (counter, by entity_type)
  neca_inference_duration_seconds            (histogram, p50/p95/p99)
  neca_expansion_tier_distribution           (gauge, by tier × tenant_id)
  neca_caps_triggered_total                  (counter, by cap_reason)
  neca_expansion_unlocks_total               (counter, by tier × entity_type)
  neca_fraud_clusters_detected_total         (counter)
  neca_stale_data_entities_total             (gauge — entities skipped due to signal gap)
  neca_model_auc_current                     (gauge — updated daily)
  neca_scoring_coverage_percent              (gauge — % active entities scored daily)
  neca_appeal_resolution_sla_compliance      (gauge — % resolved < 72h)

Alerts:
  - neca_model_auc_current < 0.72 → PagerDuty (CRITICAL)
  - neca_stale_data_entities_total > 5% of active entities → Slack alert
  - neca_expansion_tier_distribution CAPPED rate > 25% in any tenant → CRITICAL alert
  - neca_fraud_clusters_detected_total > 3 in 24 hours → PagerDuty (SECURITY)
  - neca_inference_duration_seconds p99 > 1.2s → performance alert
  - neca_appeal_resolution_sla_compliance < 90% → Slack alert (Admin Governance)
  - Retraining DAG failure → PagerDuty (ML Engineer + Platform Admin)
```

---

# SECTION XIX — FAILURE HANDLING (DETERMINISTIC)

| Failure | System Response |
|---|---|
| Signal feed stale > 48 hours | Skip scoring → emit neca.stale_data_warning → alert Platform Ops |
| Feature computation error | Log NECA_FEATURE_FAILURE → skip entity for this cycle → no tier assigned |
| Model inference timeout (3 retries) | Fall back to fast-path cap rule (Section VIII.3) → log NECA_ML_FALLBACK |
| Fast-path rule conditions not met | Tier = previous tier (carry forward, max 3 cycles) → then alert |
| Model AUC below 0.72 (runtime check) | Freeze model at current version → alert ML engineer → human review required |
| Fraud cluster detection failure (Qdrant unavailable) | Skip cluster check → log NECA_FRAUD_CLUSTER_SKIP → continue scoring without cluster result |
| Consent record missing | Score computed internally → NOT emitted → log NECA_CONSENT_GATED |
| Minor protection violation | STOP → REPORT MINOR_PROTECTION_BREACH → escalate to DPO |
| Tenant isolation violation | STOP → REPORT TENANT_ISOLATION_BREACH → alert Security Admin |
| Cap enforcement delivery failure (Notification Service down) | Retry 3x → log NECA_CAP_UNDELIVERED → queue for next cycle |
| Appeal resolution SLA breach (> 72 hours) | Escalate to Platform Ops → log NECA_APPEAL_SLA_BREACH |
| LLM summary generation failure | Growth event emitted WITHOUT summary → summary_available: false → no block |
| Unleash feature flag service unavailable | Cap state held in Redis → no privilege change during outage → alert Platform Ops |

```
NECA FAILURE PHILOSOPHY:
  A missing growth score is better than a wrong score.
  A held cap is better than a wrongly lifted cap.
  A degraded fallback is better than a silent failure.
  An undelivered unlock is better than a fraudulent unlock.
  Every failure is logged before any other action.
  No agent auto-corrects. Humans correct.
```

---

# SECTION XX — WHAT THIS AGENT NEVER DOES

```
NEVER issues a growth tier label to any user-facing surface using the words "viral", "flagged", "capped", "fraud"
NEVER shares individual growth probability scores with employers under any condition
NEVER applies a cap without a traceable, defined condition match in Section VIII.3
NEVER auto-lifts a CAPPED tier — human sign-off is mandatory in all cases
NEVER resolves an appeal automatically — human action is required for all appeal outcomes
NEVER uses protected demographic attributes as features (directly or as proxies)
NEVER deploys a new model version without human review and sign-off (M5)
NEVER uses an LLM to compute, adjust, or gate a growth score or cap decision
NEVER scores an entity with fewer than 3 network events on record
NEVER operates on stale signal data older than 48 hours without emitting a warning
NEVER issues two conflicting expansion tiers for the same entity in the same 24-hour window
NEVER retains growth scores beyond their defined retention window
NEVER claims a trained or deployed model without attached human execution logs (M5)
NEVER exposes fraud cluster member identities in any non-Platform Ops dashboard
NEVER suppresses minor entity network activity without guardian consent on record
NEVER allows cap enforcement to be triggered by an LLM or any Tier 3 component
```

---

# SECTION XXI — VERSION & CHANGE CONTROL

```
Document Version:     1.0
Status:               SEALED
Seal Date:            2026-03-04
Companion Documents:  DROPOUT_RISK_PREDICTION_AGENT v1.0
                      ATTENDANCE_BEHAVIOR_ANALYTICS_AGENT v1.0
Next Review:          Version bump required for any structural change

Allowed without version bump:
  - Add a new Kafka topic to consumed list (with new feature definition)
  - Add a new enforcement row to Section XIII matrix (additive only)
  - Add a new dashboard panel (additive only)
  - Update R28-5 cost estimates (non-structural)
  - Add a new alert threshold in Section XVIII (additive only)

Requires version bump + human declaration + fairness re-audit:
  - Change any expansion tier threshold in Section VIII.2
  - Change any fast-path cap override condition in Section VIII.3
  - Change any expansion unlock condition in Section VIII.4
  - Add or remove any feature from Section VII
  - Change model type or architecture
  - Change any fairness threshold in Section XI
  - Change any data retention period in Section XVI
  - Change LLM model used for summary generation
  - Add any new surface to Section IV scope
  - Add or modify any fraud detection threshold in Section XIV

IMPLICIT BEHAVIOR = FORBIDDEN.
ALL behavior is declared.
ALL scoring is defined.
ALL features are catalogued.
ALL cap conditions are enumerated.
ALL expansion unlocks are rule-gated.
ALL outputs are auditable.
```

---

## ✅ SEAL CONFIRMATION

```
✔ Agent identity declared with R28 and M5 compliance
✔ R28 tier map complete — every feature and model classified
✔ M5 law respected — human training and deployment authority preserved
✔ Scope locked — in-scope surfaces, entity types, and forbidden actions listed
✔ Architecture placement defined — consumer + emitter, never controller
✔ Kafka event schema sealed — canonical envelope specified
✔ Feature catalog locked — 32 features (25 computed + 7 derived flags), all formulas defined
✔ Growth scoring model declared — LightGBM, evaluation metrics, label definition
✔ Expansion tier classification rule-based (Tier 1) — NOT ML
✔ Fast-path cap override conditions enumerated — deterministic
✔ Expansion unlock conditions enumerated — rule-gated, not ML-gated
✔ Minimum data guard enforced (< 3 events → no score)
✔ Fraud network detection declared — Isolation Forest + Qdrant cluster detection
✔ Explainability format defined — SHAP + LLM advisory text (Tier 3 only)
✔ Evaluation metrics complete (R12 compliant)
✔ Fairness governance sealed — protected attributes listed, metrics defined, human gate enforced
✔ Retraining schedule declared — monthly scheduled + drift-triggered (Airflow)
✔ Cap enforcement matrix complete — all tier × entity_type × condition rows enumerated
✔ Enforcement content rules sealed — language constraints, framing rules
✔ Appeal protocol declared — human-only resolution, 72h SLA
✔ Dashboard RBAC matrix complete
✔ Data governance sealed — retention, privacy, consent architecture
✔ Integration contracts locked — all upstream and downstream services named
✔ Observability requirements specified — Prometheus metrics + alert thresholds
✔ Failure handling deterministic — 12 failure modes with defined responses
✔ Prohibited behaviors explicitly listed — 15 hard constraints
✔ Change control enforced — version bump requirements defined

NECA IS SEALED.
EXECUTION BEGINS ONLY ON HUMAN DECLARATION.
MODEL DEPLOYMENT STATUS: NOT CLAIMED UNTIL HUMAN EXECUTION LOG IS ATTACHED.
```
