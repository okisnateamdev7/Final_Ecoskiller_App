# 🔒 SEALED & LOCKED AGENT SPECIFICATION
# POPULATION PERCENTILE ENGINE AGENT
## Ecoskiller Antigravity — Comparative Intelligence & Ranking Core
---

```
DOCUMENT_CLASS              = PRODUCTION AGENT SPECIFICATION
ARTIFACT_VERSION            = v1.0.0
MUTATION_POLICY             = ADD-ONLY (versioned bump mandatory for any change)
EXECUTION_MODE              = DETERMINISTIC + VALIDATED
CREATIVE_INTERPRETATION     = FORBIDDEN
ASSUMPTION_FILLING          = FORBIDDEN
DEFAULT_BEHAVIOR            = DENY
FAILURE_MODE                = HALT ON AMBIGUITY
CROSS_TENANT_ACCESS         = FORBIDDEN
AUTO_RANK_MUTATION          = FORBIDDEN WITHOUT AUDIT
SEAL_STATUS                 = LOCKED
```

---

## 🧬 SECTION 1 — AGENT IDENTITY (NON-NEGOTIABLE)

```yaml
AGENT_NAME:              POPULATION_PERCENTILE_ENGINE_AGENT
AGENT_ID:                ECO-AG-PPE-002
AGENT_CLASS:             Statistical ML Agent (Primary) + Distribution Governance Layer
SYSTEM_ROLE:             Population-level Comparative Ranking, Percentile Computation,
                         Cohort Segmentation, and Fairness-Controlled Rank Emission Engine
PRIMARY_DOMAIN:          Intelligence Analytics | Competitive Ranking | Cohort Intelligence |
                         Fairness Engineering | Normative Comparison
EXECUTION_MODE:          Deterministic + Validated
DATA_SCOPE:
  - Individual intelligence scores (per-dimension, per-domain-track)
  - Aggregated population distributions (per cohort, per tenant, per platform-global)
  - Skill scores (Dojo belt levels, match outcomes)
  - Championship competitive rankings
  - Age-cohort normative bands
  - Domain-track segmented distributions
  - Temporal score snapshots (historical + current)
TENANT_SCOPE:            STRICT ISOLATION
                         — Tenant-internal percentiles computed within tenant boundary
                         — Platform-global percentiles computed across anonymized population
                         — Cross-tenant raw data access FORBIDDEN
FAILURE_POLICY:          HALT ON AMBIGUITY — no silent failure, no partial rank emission
PLATFORM:                Ecoskiller Antigravity
ARCHITECTURE:            Microservices + Event-Driven
ML_RATIO:                80% Traditional ML (Statistical Distribution, Regression, Clustering)
AI_RATIO:                20% LLM-Assisted (Percentile narrative generation, cohort insight summaries)
SECURITY_MODEL:          Zero-Trust Multi-Tenant
DATA_POLICY:             Append-Only Audit Trail
DOMAIN_TRACKS:           Arts | Commerce | Science | Technology | Administration
INTELLIGENCE_DIMENSIONS: Linguistic | Logical-Mathematical | Spatial | Musical |
                         Bodily-Kinesthetic | Interpersonal | Intrapersonal |
                         Naturalist | Existential
```

---

## 🎯 SECTION 2 — PURPOSE DECLARATION

### 2.1 The Core Problem

Ecoskiller measures intelligence, skills, and performance across millions of users spanning diverse age groups, domains, educational backgrounds, and cultural contexts. Without a **population-relative ranking engine**, every score exists in a vacuum — a raw score of 74 in Logical-Mathematical intelligence is meaningless unless framed against the population: Is this 74th percentile? 55th? 92nd?

The **POPULATION_PERCENTILE_ENGINE_AGENT** is the authoritative source of truth for **where any user stands relative to every comparable population segment** — their age cohort, domain cohort, institute cohort, national cohort, and global cohort — across all nine intelligence dimensions and all tracked skill domains.

This agent is the engine behind:
- The **Intelligence Radar Chart** percentile overlays (Module 2 — Intelligence Measurement System)
- The **Championship seeding fairness** (balanced bracket generation)
- The **Recruiter ranked search** (top X% in dimension Y within domain Z)
- The **Parent Dashboard** ("Your child is in the top 15% of their age group for Logical-Mathematical intelligence")
- The **Institute Benchmarking** ("Your institute ranks 3rd in the state for Science domain intelligence growth")
- The **AI Mentor guidance** ("You are approaching the 75th percentile threshold — 3 more practice sessions may close the gap")

### 2.2 What This Agent Is NOT

This agent does NOT:
- Generate raw intelligence scores (that is the ASSESSMENT_ENGINE_AGENT's role)
- Make growth predictions (that is the INTELLIGENCE_GROWTH_TIME_SERIES_AGENT's role)
- Issue belt certifications (that is the BELT_ENGINE's role)
- Assign XP rewards directly (it emits triggers; RANK_ENGINE acts)

This agent ONLY computes **population-relative position** and emits structured, auditable rank objects.

### 2.3 Input Consumed

| Input Source | Data Type | Trigger |
|---|---|---|
| IGTS_AGENT | Current intelligence score per dimension | On trajectory computation |
| ASSESSMENT_ENGINE_AGENT | Raw assessment score events | On assessment completion |
| DOJO_SCORING_AGENT | Match skill scores | On match finalization |
| CHAMPIONSHIP_RESULT_AGENT | Competitive outcome vectors | On round completion |
| FEATURE_STORE_AGENT | User demographic + domain features | Batch refresh |
| INSTITUTE_REGISTRY_AGENT | Cohort membership maps | On cohort change |
| BATCH_SCHEDULER | Nightly full population recalculation | Scheduled 02:30 UTC |

### 2.4 Output Produced

| Output | Consumer | Scope |
|---|---|---|
| User Percentile Profile | User Dashboard, Parent, Recruiter | Per user, per dimension |
| Cohort Rank Position | Institute Dashboard, AI Mentor | Per cohort |
| National Percentile | Platform Leaderboard, Championship Seeding | Anonymized |
| Global Percentile | Global Rankings, Recruiter Search | Anonymized |
| Population Distribution Snapshot | Observability Agent, Audit Store | Platform-wide |
| Rank Update Events | Growth Engine, Notification Service | Per rank band crossing |
| Fairness-Adjusted Competition Bands | Championship Seeding Agent | Per tournament |
| Cohort Intelligence Distribution | Institute Analytics Agent | Per institute cohort |

### 2.5 Upstream Agents (Feed This Agent)

```
UPSTREAM_AGENTS:
  - INTELLIGENCE_GROWTH_TIME_SERIES_AGENT   (current score vectors)
  - ASSESSMENT_ENGINE_AGENT                 (raw scored assessment events)
  - DOJO_SCORING_AGENT                      (match skill score packages)
  - CHAMPIONSHIP_RESULT_AGENT               (competitive performance vectors)
  - FEATURE_STORE_AGENT                     (demographic + domain feature vectors)
  - INSTITUTE_REGISTRY_AGENT                (cohort membership + institute metadata)
  - BATCH_SCHEDULER                         (nightly full-population recalculation trigger)
```

### 2.6 Downstream Agents (Depend On This Agent)

```
DOWNSTREAM_AGENTS:
  - USER_DASHBOARD_SERVICE                  (renders percentile badges, radar overlays)
  - PARENT_INSIGHT_AGENT                    (child's comparative rank narrative)
  - RECRUITER_INTELLIGENCE_AGENT            (ranked candidate pool queries)
  - AI_MENTOR_AGENT                         (gap-to-next-band guidance)
  - INSTITUTE_ANALYTICS_AGENT               (institute cohort benchmarking)
  - CHAMPIONSHIP_SEEDING_AGENT              (fairness-bracketed tournament pairing)
  - RANK_ENGINE_AGENT                       (XP + rank tier update triggers)
  - ALERT_SERVICE_AGENT                     (rank band crossing notifications)
  - OBSERVABILITY_AGENT                     (distribution drift + anomaly signals)
  - FEATURE_STORE_AGENT                     (receives emitted percentile features)
  - PLATFORM_LEADERBOARD_SERVICE            (public + tenant-scoped leaderboards)
```

---

## 📥 SECTION 3 — INPUT CONTRACT (STRICT)

### 3.1 Trigger Types & Input Schemas

This agent accepts **three distinct input trigger types**. Each has its own schema. Processing a wrong trigger type against the wrong schema = IMMEDIATE REJECTION.

---

#### TRIGGER TYPE 1 — REAL-TIME SCORE EVENT (On new score arrival)

```json
INPUT_SCHEMA_RT: {
  "required_fields": [
    "event_id",
    "trigger_type",
    "user_id",
    "tenant_id",
    "domain_track",
    "intelligence_dimension",
    "current_score",
    "score_timestamp_utc",
    "source_agent_id",
    "model_version_source"
  ],
  "optional_fields": [
    "age_years",
    "cohort_id",
    "institute_id",
    "country_code",
    "state_code",
    "assessment_type",
    "score_confidence",
    "previous_score"
  ],
  "validation_rules": [
    "trigger_type must equal 'SCORE_EVENT'",
    "event_id must be UUID v4 — idempotency key",
    "user_id must be UUID v4",
    "tenant_id must match session JWT claim",
    "domain_track must be in [Arts, Commerce, Science, Technology, Administration]",
    "intelligence_dimension must be in defined 9-dimension enum",
    "current_score must be Float in [0.0, 100.0]",
    "score_timestamp_utc must be ISO 8601 parseable",
    "score_timestamp_utc must not be future-dated (reject if > now + 60s)",
    "source_agent_id must be a registered upstream agent",
    "model_version_source must be valid semver"
  ],
  "security_checks": [
    "Validate tenant_id against JWT",
    "Validate user_id belongs to tenant_id via row-level security",
    "Validate source_agent_id is whitelisted upstream",
    "Validate domain_track matches user's registered domain",
    "Reject if event_id already processed (dedup check in Redis, TTL 24h)",
    "Reject if score_timestamp_utc is older than 30 days (stale event)"
  ],
  "domain_checks": [
    "intelligence_dimension must be active and mapped to a valid construct",
    "domain_track must be user's registered primary or explicitly granted secondary domain",
    "Cross-domain inference prohibited"
  ]
}
```

---

#### TRIGGER TYPE 2 — BATCH RECALCULATION (Nightly full population)

```json
INPUT_SCHEMA_BATCH: {
  "required_fields": [
    "event_id",
    "trigger_type",
    "tenant_id",
    "batch_date_utc",
    "scope",
    "scheduler_auth_token"
  ],
  "optional_fields": [
    "domain_track_filter",
    "intelligence_dimension_filter",
    "cohort_id_filter",
    "country_code_filter"
  ],
  "validation_rules": [
    "trigger_type must equal 'BATCH_RECALCULATION'",
    "scope must be in [TENANT, PLATFORM_GLOBAL, COHORT, INSTITUTE]",
    "batch_date_utc must be ISO 8601, within 24h window of trigger",
    "scheduler_auth_token must be validated against internal scheduler signing key",
    "No user_id in batch trigger — population-level only"
  ],
  "security_checks": [
    "scheduler_auth_token must be cryptographically verified",
    "PLATFORM_GLOBAL scope requires PLATFORM_ADMIN authorization",
    "TENANT scope requires TENANT_ADMIN authorization",
    "Reject unrecognized scope values"
  ],
  "domain_checks": [
    "If domain_track_filter provided: must be in valid domain enum",
    "If cohort_id_filter provided: cohort must exist in INSTITUTE_REGISTRY_AGENT"
  ]
}
```

---

#### TRIGGER TYPE 3 — ON-DEMAND QUERY (Recruiter / Mentor / Parent query)

```json
INPUT_SCHEMA_QUERY: {
  "required_fields": [
    "event_id",
    "trigger_type",
    "requesting_actor_id",
    "requesting_role",
    "tenant_id",
    "subject_user_id",
    "query_dimensions",
    "query_scope"
  ],
  "optional_fields": [
    "cohort_filter",
    "domain_filter",
    "age_band_filter",
    "country_filter",
    "include_distribution_snapshot",
    "include_gap_analysis"
  ],
  "validation_rules": [
    "trigger_type must equal 'ON_DEMAND_QUERY'",
    "requesting_role must be in [STUDENT, PARENT, MENTOR, RECRUITER, INSTITUTE_ADMIN, TENANT_ADMIN, PLATFORM_ADMIN]",
    "query_dimensions must be non-empty array subset of 9-dimension enum",
    "query_scope must be in [SELF, COHORT, INSTITUTE, NATIONAL, GLOBAL]",
    "subject_user_id must be UUID v4"
  ],
  "security_checks": [
    "PARENT role: subject_user_id must be linked child — reject otherwise",
    "MENTOR role: subject_user_id must be assigned mentee — reject otherwise",
    "RECRUITER role: subject_user_id must have consent flag active — reject without consent",
    "STUDENT role: subject_user_id must equal requesting_actor_id (self-only)",
    "INSTITUTE_ADMIN: subject must belong to institute's tenant",
    "GLOBAL scope queries: only RECRUITER, INSTITUTE_ADMIN, TENANT_ADMIN, PLATFORM_ADMIN permitted",
    "Validate requesting_actor_id belongs to tenant_id"
  ],
  "domain_checks": [
    "If domain_filter provided: must match user's registered domain or explicitly granted access",
    "Recruiter accessing candidate data: candidate consent_scope must include 'PERCENTILE_SHARE'"
  ]
}
```

### 3.2 Input Rejection Policy

```yaml
REJECT_ON:
  - Any required field missing or null
  - Malformed UUID (any field)
  - Score out of [0.0, 100.0] bounds
  - Unknown trigger_type
  - Expired or future-dated timestamps
  - Duplicate event_id (idempotency guard)
  - Unauthorized role for requested scope
  - Unverified scheduler token (batch trigger)
  - Consent flag absent (recruiter query)
  - Domain isolation violation
  - Stale event (score > 30 days old on real-time trigger)

ON_REJECT:
  ACTION:              STOP_EXECUTION immediately
  LOG:                 LOG_VALIDATION_FAILURE to immutable audit store
  EMIT:                Structured rejection event to OBSERVABILITY_AGENT
  RESPONSE:            Return structured rejection object (no partial output)
  RETRY:               Caller must correct and resubmit — no auto-retry by this agent

REJECTION_OBJECT: {
  "rejection_id":       "UUID",
  "timestamp_utc":      "ISO 8601",
  "event_id":           "original event_id",
  "input_hash":         "SHA-256 of rejected payload",
  "trigger_type":       "string",
  "rejection_reason":   "string (human-readable)",
  "field_violations":   ["list of specific field violations"],
  "security_violation": "boolean",
  "source_agent":       "string",
  "audit_reference":    "UUID"
}
```

---

## 📤 SECTION 4 — OUTPUT CONTRACT (STRICT)

### 4.1 Real-Time Score Event Output

```json
OUTPUT_SCHEMA_RT: {
  "result_object": {
    "user_id":                     "UUID",
    "tenant_id":                   "UUID",
    "intelligence_dimension":      "string",
    "domain_track":                "string",
    "current_score":               "Float [0-100]",
    "percentile_ranks": {
      "tenant_cohort":             "Float [0-100] | null if cohort_size < 30",
      "domain_cohort":             "Float [0-100]",
      "age_cohort":                "Float [0-100] | null if age unavailable",
      "institute_cohort":          "Float [0-100] | null if not institute-linked",
      "national":                  "Float [0-100]",
      "platform_global":           "Float [0-100]"
    },
    "rank_band":                   "ELITE | ADVANCED | PROFICIENT | DEVELOPING | FOUNDATIONAL",
    "rank_band_previous":          "string | null",
    "rank_band_changed":           "boolean",
    "percentile_delta_30d": {
      "tenant_cohort":             "Float (signed delta)",
      "platform_global":           "Float (signed delta)"
    },
    "gap_to_next_band": {
      "score_gap":                 "Float",
      "next_band":                 "string",
      "estimated_assessments_needed": "int | null"
    },
    "population_stats": {
      "sample_size_tenant":        "int",
      "sample_size_global":        "int",
      "distribution_mean":         "Float",
      "distribution_std":          "Float",
      "distribution_median":       "Float",
      "p25":                       "Float",
      "p75":                       "Float",
      "p90":                       "Float"
    },
    "fairness_flags": {
      "small_cohort_warning":      "boolean (true if N < 30)",
      "distribution_skew_flag":    "boolean",
      "sparse_data_flag":          "boolean"
    },
    "rank_update_trigger":         "RANK_UPDATE_EVENT | null",
    "xp_update_trigger":           "XP_UPDATE_EVENT | null",
    "share_trigger":               "SHARE_TRIGGER_EVENT | null"
  },
  "confidence_score":              "Float [0-1]",
  "model_version":                 "semver",
  "audit_reference":               "UUID",
  "next_trigger_events":           ["list of triggered downstream events"],
  "computation_metadata": {
    "population_snapshot_age_seconds": "int",
    "computation_duration_ms":     "int",
    "drift_flag":                  "boolean",
    "model_health":                "HEALTHY | DEGRADED | UNAVAILABLE"
  }
}
```

### 4.2 Batch Recalculation Output

```json
OUTPUT_SCHEMA_BATCH: {
  "result_object": {
    "batch_id":                    "UUID",
    "batch_date_utc":              "ISO 8601",
    "scope":                       "string",
    "tenant_id":                   "UUID",
    "records_processed":           "int",
    "records_updated":             "int",
    "records_failed":              "int",
    "rank_band_changes": {
      "promotions":                "int",
      "demotions":                 "int",
      "unchanged":                 "int"
    },
    "distribution_snapshots":      ["per-dimension distribution objects"],
    "fairness_audit_summary": {
      "small_cohort_count":        "int",
      "skewed_distributions":      "int",
      "sparse_data_segments":      "int"
    },
    "anomalies_detected":          "int",
    "batch_duration_seconds":      "int"
  },
  "confidence_score":              "Float [0-1]",
  "model_version":                 "semver",
  "audit_reference":               "UUID",
  "next_trigger_events":           ["RANK_UPDATE_EVENT bulk payload", "DISTRIBUTION_UPDATED_EVENT"]
}
```

### 4.3 On-Demand Query Output

```json
OUTPUT_SCHEMA_QUERY: {
  "result_object": {
    "subject_user_id":             "UUID",
    "requesting_actor_id":         "UUID",
    "query_dimensions":            ["list of dimensions returned"],
    "query_scope":                 "string",
    "percentile_profiles":         ["array of per-dimension percentile_rank objects"],
    "cohort_position": {
      "rank_in_cohort":            "int | null",
      "cohort_size":               "int | null",
      "top_X_percent":             "Float | null"
    },
    "comparative_narrative":       "string (LLM-generated, validated) | null",
    "gap_analysis":                ["gap_to_next_band per dimension"] ,
    "distribution_snapshot":       "object | null (only if include_distribution_snapshot=true)",
    "consent_scope_applied":       "string",
    "data_anonymization_applied":  "boolean"
  },
  "confidence_score":              "Float [0-1]",
  "model_version":                 "semver",
  "audit_reference":               "UUID",
  "next_trigger_events":           []
}
```

### 4.4 Output Guarantees

```yaml
ALL_OUTPUTS_MUST_INCLUDE:
  - confidence_score (Float 0-1, never null)
  - model_version (semver, immutable)
  - audit_reference (UUID, append-only log entry)
  - next_trigger_events (may be empty array, never null)

SMALL_COHORT_POLICY:
  - If cohort_size < 30: DO NOT compute percentile for that cohort scope
  - Set percentile = null, set small_cohort_warning = true
  - Log suppression reason in audit record
  - This protects individual privacy in small cohorts

ANONYMIZATION_POLICY:
  - GLOBAL and NATIONAL scope percentiles computed on anonymized population only
  - Raw user data never aggregated across tenants
  - Differential privacy noise injection for population sizes < 1000

MINIMUM_CONFIDENCE_POLICY:
  - confidence_score < 0.65: Deliver with DEGRADED_CONFIDENCE flag
  - confidence_score < 0.45: HALT — do not deliver, escalate to OBSERVABILITY_AGENT
  - Championship seeding: confidence_score < 0.75 triggers manual seeding review
```

---

## 🤖 SECTION 5 — ML / AI LOGIC LAYER

### 5.1 ML Layer (80% — Primary Computation Engine)

#### 5.1.1 Core Percentile Computation Method

```yaml
COMPUTATION_ARCHITECTURE:
  Method:
    Primary:    Empirical Cumulative Distribution Function (ECDF)
                — Non-parametric, no distributional assumptions
                — Exact rank computation within tenant/cohort
    Secondary:  Kernel Density Estimation (KDE)
                — Smooth distribution for small-cohort interpolation
                — Bandwidth: Scott's rule, adaptive per cohort size
    Tertiary:   Normal Score Transformation (NST)
                — For cross-cohort normalization (age-adjusted)
                — Applied only when age data available and verified

  STRATIFICATION_DIMENSIONS:
    — Dimension 1: intelligence_dimension (9 dimensions)
    — Dimension 2: domain_track (5 tracks)
    — Dimension 3: age_band (5-10, 11-15, 16-18, 19-22, 23-29, 30-39, 40+)
    — Dimension 4: scope level (tenant | institute | cohort | national | global)
    — All combinations maintained as independent distribution objects
    — Total distribution objects maintained: 9 × 5 × 7 × 5 = 1,575 per population segment

  RANK_BAND_CLASSIFICATION:
    Model:     Rule-based boundary classifier (threshold-defined, not ML-generated)
    Thresholds (IMMUTABLE — requires MAJOR version bump to change):
      ELITE:          >= 90th percentile
      ADVANCED:       >= 75th percentile and < 90th
      PROFICIENT:     >= 50th percentile and < 75th
      DEVELOPING:     >= 25th percentile and < 50th
      FOUNDATIONAL:   < 25th percentile
    Note: Thresholds are population-relative, NOT absolute score thresholds

  GAP_TO_NEXT_BAND:
    Algorithm:  Score inverse-CDF lookup on distribution
                — Find score corresponding to next band lower boundary
                — Compute delta from current score
                — Estimate assessments_needed using growth velocity from IGTS_AGENT
    Precision:  Rounded to 1 decimal place
```

#### 5.1.2 Population Distribution Maintenance

```yaml
DISTRIBUTION_STORE:
  Database:          TimescaleDB (time-series distribution snapshots)
  Update_Frequency:
    Realtime_trigger:  Incremental ECDF update on each score event
                       — O(log N) insertion via sorted structure (Redis Sorted Sets)
                       — Full ECDF recomputed asynchronously post-insertion
    Batch_nightly:     Full population recalculation (02:30 UTC)
                       — All distributions rebuilt from source scores
                       — Replaces incremental accumulation errors
    
  DISTRIBUTION_SNAPSHOT_OBJECT: {
    "snapshot_id":           "UUID",
    "dimension":             "string",
    "domain_track":          "string",
    "scope":                 "string",
    "cohort_id":             "UUID | null",
    "snapshot_timestamp":    "ISO 8601",
    "population_size":       "int",
    "percentile_lookup_table": {
      "p1": "Float", "p5": "Float", "p10": "Float",
      "p25": "Float", "p50": "Float", "p75": "Float",
      "p90": "Float", "p95": "Float", "p99": "Float"
    },
    "mean":                  "Float",
    "std":                   "Float",
    "skewness":              "Float",
    "kurtosis":              "Float",
    "min_score":             "Float",
    "max_score":             "Float",
    "kde_bandwidth":         "Float",
    "model_version":         "semver"
  }

  CACHE_STRATEGY:
    - Distribution snapshots cached in Redis (sorted sets + hash maps)
    - TTL: 10 minutes (realtime path); refreshed on each batch run
    - Cache miss: fallback to TimescaleDB read (SLA protected)
    - Stale cache flag emitted if snapshot_age > 15 minutes
```

#### 5.1.3 Feature Set

```yaml
FEATURES_USED:

  Score Features (Primary):
    - current_score (Float [0-100])
    - previous_score (Float [0-100] | null)
    - score_source_type (enum: ADAPTIVE_TEST | DOJO_MATCH | CHAMPIONSHIP | PROJECT | BEHAVIORAL)
    - score_confidence (Float [0-1] from source agent)
    - scoring_consistency_index (rolling std dev of last 5 scores)

  User Segmentation Features:
    - age_years (Int | null)
    - age_band_encoded (Int: 0-6)
    - domain_track_encoded (Int: 0-4)
    - intelligence_dimension_encoded (Int: 0-8)
    - platform_tenure_days (Int)
    - institute_id_hashed (anonymized)
    - country_code (ISO 3166-1 alpha-2)
    - state_code (ISO 3166-2 | null)
    - cohort_size (Int — population count for this segment)

  Population Features:
    - segment_mean (Float — current distribution mean)
    - segment_std (Float — current distribution std dev)
    - segment_p50 (Float — median)
    - segment_p75 (Float — 75th percentile boundary)
    - segment_p90 (Float — 90th percentile boundary)
    - kde_score_at_percentile (Float — KDE-smoothed lookup)
    - population_freshness_hours (Float — age of distribution snapshot)

  Fairness Features:
    - cohort_size_category (LARGE: >1000 | MEDIUM: 100-999 | SMALL: 30-99 | SUPPRESSED: <30)
    - skewness_flag (boolean: |skewness| > 1.5)
    - sparse_data_flag (boolean: cohort_size < 100)
    - differential_privacy_applied (boolean)

TRAINING_FREQUENCY:
  ECDF / KDE:       Continuous incremental + nightly full rebuild (no ML training cycle — statistical)
  Rank_Band_Classifier: Rule-based (no training required — threshold-governed)
  Gap_Estimator:    Uses IGTS_AGENT growth velocity — weekly updated (inherited from IGTS)
  
VERSION_CONTROL:
  - Distribution snapshot version tagged per nightly batch run
  - model_version referenced in every output and audit record
  - Immutable snapshot archive retained (append-only)
  - No snapshot deletion
```

#### 5.1.4 Fairness & Anti-Bias Controls (MANDATORY)

```yaml
FAIRNESS_ENGINE:

  SMALL_COHORT_PROTECTION:
    Rule:       Suppress percentile if cohort_size < 30
    Reason:     Individual privacy at risk in small populations
    Action:     Return null for that scope level, emit small_cohort_warning=true

  DIFFERENTIAL_PRIVACY:
    Trigger:    National and Global scope computations
    Method:     Laplacian noise injection (ε = 0.1)
    Applied_to: Distribution parameter outputs only — not individual percentile ranks
    Audit:      dp_applied flag in output + audit record

  SKEW_DETECTION:
    Monitor:    Distribution skewness per segment (|skewness| > 1.5 = flagged)
    Action:     Apply Box-Cox transformation before percentile lookup
    Audit:      transformation_applied flag in output

  OUTLIER_HANDLING:
    Method:     Winsorization at p1 and p99 before distribution fitting
    Reason:     Prevent outlier score inflation / deflation of population ranks
    Audit:      Winsorization bounds logged per snapshot

  CULTURAL_BIAS_GUARD:
    Rule:       No intelligence dimension percentile may incorporate features
                that are culturally exclusive (language-locked, geography-locked test items)
    Enforcement: Input schema domain_checks — scenario fairness flag from DOJO_SCORING_AGENT
    Audit:      cultural_bias_check_passed flag logged per computation

  GENDER_DOMAIN_NEUTRALITY:
    Rule:       Gender is NOT a segmentation feature for percentile computation
    Reason:     Prevents discriminatory ranking by gender
    Enforcement: gender field excluded from all input schemas (even if provided, ignored)

  MINIMUM_REPRESENTATION_RULE:
    Rule:       If any sub-group (age_band × domain × dimension) has < 100 members,
                fall back to the next broader scope (e.g., cohort → national)
    Audit:      fallback_scope_used flag in output
```

### 5.2 AI Layer (20% — Narrative Assist Only)

```yaml
AI_USAGE_SCOPE:
  PERMITTED:
    - Generate parent-friendly percentile narrative
      (e.g., "Aanya is in the top 12% of students her age in Logical-Mathematical intelligence")
    - Generate recruiter-facing percentile insight summary
    - Generate institute cohort comparison narrative
    - Generate AI Mentor gap-analysis phrasing for motivation
    - Summarize distribution anomaly in human-readable alert

  FORBIDDEN:
    - AI may NOT compute percentiles
    - AI may NOT determine rank bands
    - AI may NOT change output numeric values
    - AI may NOT override fairness flags
    - AI may NOT generate cohort distribution statistics
    - AI may NOT execute decision logic of any kind
    - Any numeric value in AI output must exactly match ML output
    - AI output that contradicts ML output: DISCARD AI text, deliver ML only

PROMPT_GOVERNANCE:
  - All prompts versioned (semver)
  - Deterministic prompt templates only — no open-ended system prompts
  - prompt_version logged in audit record per execution
  - Hallucination guard: post-process all AI output for numeric integrity
  - If AI output contains a number not matching ML output: REJECT AI output entirely
  - LLM has access ONLY to the structured ML result object — no raw user data
  - All narrative output: max 3 sentences per dimension, plain language

LLM_ISOLATION:
  - LLM receives anonymized input (no raw PII)
  - LLM session stateless — no conversation history retained
  - LLM cannot trigger downstream events
  - LLM timeout: 3s — on timeout, deliver ML output with narrative_status = "UNAVAILABLE"
```

### 5.3 Drift Detection

```yaml
DRIFT_DETECTION:

  POPULATION_DISTRIBUTION_DRIFT:
    Monitor:    KS-test on daily distribution snapshots vs. 7-day rolling baseline
    Frequency:  Nightly (post-batch) + spot-checks every 6 hours
    Alert_threshold: KS p-value < 0.01 (significant shift)
    Action:     Flag distribution as DRIFT_DETECTED, notify OBSERVABILITY_AGENT,
                hold championship seeding until drift resolved

  SCORE_INJECTION_ANOMALY:
    Monitor:    Z-score of new score vs. user's own historical distribution
                — Z > 3.5: SUSPICIOUS flag
                — Z > 5.0: BLOCK + ESCALATE to TRUST_SYSTEM_AGENT
    Reason:     Detect score farming, collusion, or system manipulation

  RAPID_COHORT_SHIFT:
    Monitor:    If > 5% of a cohort's ranks change within 24h (excluding batch window)
    Action:     Flag for COLLUSION_DETECTION_AGENT review
    Reason:     Sudden mass rank shift indicates orchestrated gaming

  SPARSE_COHORT_DRIFT:
    Monitor:    Cohort sizes dropping below minimums (30 → suppress threshold)
    Action:     Auto-fallback to broader scope, emit cohort_deprecated event

  RATER_SCORE_BIAS_INHERITANCE:
    Monitor:    If DOJO_SCORING_AGENT emits mentor_bias_flag for a score:
                Exclude that score from percentile computation until re-calibrated
    Action:     Mark affected percentile as PROVISIONAL until clean score received
```

---

## ⚡ SECTION 6 — SCALABILITY DESIGN

```yaml
EXPECTED_RPS:
  Current (10M users):     8,000 RPS (real-time trigger path)
  Target (100M users):     80,000 RPS (horizontal scale)
  Batch path:              Not RPS-bound — scheduled, parallelized

LATENCY_TARGET:
  Real-time trigger:       p50 < 80ms | p95 < 250ms | p99 < 500ms
  On-demand query:         p50 < 100ms | p95 < 350ms | p99 < 700ms
  Batch nightly:           Complete within 4-hour window (02:30–06:30 UTC)

MAX_CONCURRENCY:
  Real-time workers:       20,000 parallel
  Batch workers:           500 parallel (tenant-partitioned)
  Query workers:           5,000 parallel

QUEUE_STRATEGY:
  Real-time path:          Kafka topic: ecoskiller.percentile.score-events
                           Partitioned by: user_id hash (32 partitions)
  Batch trigger:           Kafka topic: ecoskiller.percentile.batch-trigger
                           Single partition (ordered batch execution)
  Query path:              Synchronous REST with async cache-first pattern

PROCESSING_MODEL:          Async event-driven (real-time + batch); sync cache-first (queries)

STATE_STORAGE:
  Population distributions:  TimescaleDB (append-only snapshots)
  Realtime ECDF structures:   Redis Sorted Sets (per segment, per dimension)
  User rank cache:            Redis Hash (user_id → rank profile, TTL 10min)
  Batch job state:            PostgreSQL (job registry, checkpointing)
  Dedup cache (event_id):     Redis SET (TTL 24h)

IDEMPOTENCY:
  - event_id used as idempotency key
  - Duplicate events safely skipped via Redis dedup SET
  - Batch runs idempotent: re-running for same batch_date_utc overwrites same records

PARTITIONING_STRATEGY:
  TimescaleDB:    Time-partitioned by snapshot_timestamp (daily chunks)
                  Space-partitioned by (dimension, domain_track, scope)
  Redis:          Sorted sets keyed as:  pct:{tenant_id}:{dimension}:{domain}:{scope}
                  Hash maps keyed as:    rank:{user_id}:{dimension}

HORIZONTAL_SCALING:
  - All processing nodes stateless
  - Shared state via Redis + TimescaleDB only
  - Auto-scaling: Kubernetes HPA on Kafka consumer lag metric
  - Scale-up trigger: consumer lag > 1,000 messages
  - Scale-down delay: 5 minutes (prevent thrashing)

CACHING_HIERARCHY:
  L1: In-memory (worker process, TTL 30s) — hot distribution lookups only
  L2: Redis (TTL 10min) — user rank profiles + distribution snapshots
  L3: TimescaleDB — authoritative store, cache miss fallback
```

---

## 🔐 SECTION 7 — SECURITY ENFORCEMENT

```yaml
MANDATORY_CONTROLS:

  TENANT_ISOLATION:
    - Tenant_id validated against JWT on every request
    - Row-level security enforced at TimescaleDB layer
    - All distribution objects scoped by tenant_id
    - PLATFORM_GLOBAL distributions use anonymized aggregates only — no tenant PII
    - Cross-tenant raw data access: IMMEDIATE HALT + SECURITY_INCIDENT

  DOMAIN_ISOLATION:
    - domain_track validated against user's registered domain
    - Cross-domain percentile queries: FORBIDDEN without explicit ABAC grant
    - Domain leaks classified as SECURITY_FAILURE

  AUTHORIZATION (RBAC + ABAC):
    STUDENT:          Read own percentile profile only (SELF scope)
    PARENT:           Read linked child's profile only (verified child link required)
    MENTOR:           Read assigned mentees' profiles (mentee relationship verified)
    RECRUITER:        Read candidate profiles (candidate consent_scope verified)
    INSTITUTE_ADMIN:  Read own cohort distributions (institute membership verified)
    TENANT_ADMIN:     Read all tenant-scoped distributions
    PLATFORM_ADMIN:   Read platform-global anonymized distributions only
    AUTOMATION_AGENT: Registered upstream agents only (agent whitelist)

  CONSENT_ENFORCEMENT:
    - Recruiter access to candidate percentile: requires consent_scope = 'PERCENTILE_SHARE'
    - Consent verified at request time (not cached)
    - Consent revocation: immediate access termination + cache invalidation
    - Consent audit: every access logged with consent_verification_id

  ENCRYPTION:
    - At-rest: AES-256 (TimescaleDB + Redis at-rest encryption)
    - In-transit: TLS 1.3 (all inter-service communication)
    - PII fields: field-level encryption (age, location — if stored)
    - Secrets: Secret Manager only (no env plaintext in production)

  ANONYMIZATION:
    - GLOBAL / NATIONAL scope: differential privacy applied (ε = 0.1)
    - Population statistics never expose individual records
    - Cohort < 30: suppress output entirely (k-anonymity minimum)

  AUDIT_LOGGING:
    - Append-only immutable audit log per execution
    - Access log per actor_id + subject_user_id
    - Consent verification logged per recruiter query
    - Log tamper detection via SHA-256 chaining (see Section 8)

  API_SECURITY:
    - JWT short-lived access tokens (15 min expiry)
    - Rate limiting: 100 RPS per user_id, 1000 RPS per tenant
    - WAF in front of all API surfaces
    - Abuse threshold: > 500 queries/min per actor triggers ALERT + temporary throttle

  FORBIDDEN:
    - Cross-tenant queries
    - Raw population data export to external systems
    - Unauthenticated access to any percentile data
    - Bypassing consent checks
    - Plaintext PII in any log field
    - Manual percentile overrides without audit trail
```

---

## 📋 SECTION 8 — AUDIT & TRACEABILITY

Every execution MUST produce an immutable audit record immediately upon completion or rejection:

```json
AUDIT_RECORD: {
  "audit_id":                    "UUID (primary key)",
  "audit_chain_hash":            "SHA-256(previous_audit_id + current_payload)",
  "timestamp_utc":               "ISO 8601",
  "agent_id":                    "ECO-AG-PPE-002",
  "agent_version":               "semver",
  "trigger_type":                "SCORE_EVENT | BATCH_RECALCULATION | ON_DEMAND_QUERY",
  "event_id":                    "original event_id (idempotency key)",
  "actor_id":                    "requesting_actor_id or scheduler_id",
  "requesting_role":             "string",
  "tenant_id":                   "UUID",
  "subject_user_id":             "UUID | null (null for batch scope)",
  "input_hash":                  "SHA-256 of input payload",
  "output_hash":                 "SHA-256 of output payload",
  "model_version":               "semver (ECDF/KDE snapshot version used)",
  "prompt_version":              "semver | null (LLM prompt version if used)",
  "distribution_snapshot_id":    "UUID (which population snapshot was used)",
  "population_size_used":        "int",
  "dimension_computed":          "string",
  "scope_computed":              "string",
  "percentile_result":           "Float | null (null if suppressed or rejected)",
  "rank_band_result":            "string | null",
  "rank_band_changed":           "boolean",
  "confidence_score":            "Float",
  "small_cohort_suppressed":     "boolean",
  "dp_applied":                  "boolean",
  "fairness_flags":              ["list of fairness flag codes"],
  "anomaly_flags":               ["list of anomaly codes"],
  "consent_verification_id":     "UUID | null",
  "rejection_flag":              "boolean",
  "rejection_reason":            "string | null",
  "downstream_events_emitted":   ["list of event types emitted"],
  "processing_duration_ms":      "int",
  "drift_flag":                  "boolean",
  "previous_audit_hash":         "SHA-256 (chain integrity link)"
}
```

```yaml
AUDIT_POLICY:
  IMMUTABILITY:         No UPDATE or DELETE on audit records — ever
  STORAGE:              PostgreSQL append-only audit table with WAL enabled
  CHAIN_INTEGRITY:      SHA-256 linking to previous record — tamper-evident
  RETENTION:            Minimum 7 years (configurable per tenant compliance regime)
  ACCESS:               Read-only by Tenant Admin, Platform Admin, Compliance Agent
  EXPORT:               Audit export via COMPLIANCE_AGENT only — not raw DB access
  CONSENT_AUDIT:        Every recruiter query logs consent_verification_id for GDPR compliance
```

---

## 🚨 SECTION 9 — FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  INVALID_INPUT:
    Action:           STOP_EXECUTION immediately
    Log:              LOG_VALIDATION_FAILURE (immutable)
    Escalate:         EMIT rejection_event to OBSERVABILITY_AGENT + source agent
    Retry:            NO — caller must correct and resubmit
    Output:           Structured rejection object (Section 3.2)
    Silent_failure:   FORBIDDEN

  MODEL_UNAVAILABLE (Redis down / TimescaleDB unreachable):
    Action:           STOP_EXECUTION
    Log:              LOG_INCIDENT (severity: CRITICAL)
    Escalate:         ESCALATE_TO = OBSERVABILITY_AGENT + PLATFORM_ADMIN + ON_CALL_ENGINEER
    Retry:            YES — 3 attempts: 2s / 8s / 30s exponential backoff
    Output:           SERVICE_UNAVAILABLE event (no partial output)
    Fallback:         If L2 Redis cache available and snapshot_age < 60min:
                      Serve cached distribution with stale_data_flag=true
                      — ONLY for non-championship, non-certification contexts

  LLM_TIMEOUT (AI narrative layer):
    Action:           CONTINUE — deliver ML output, skip LLM narrative
    Log:              LOG_INCIDENT (severity: LOW)
    Escalate:         Notify OBSERVABILITY_AGENT
    Retry:            YES — 1 retry after 2s, then skip
    Output:           ML result delivered, narrative = null, narrative_status = "UNAVAILABLE"

  DATA_CORRUPTION (integrity check failure on distribution snapshot):
    Action:           STOP_EXECUTION
    Log:              LOG_INCIDENT (severity: CRITICAL)
    Escalate:         ESCALATE_TO = PLATFORM_ADMIN + DATA_INTEGRITY_AGENT
    Retry:            NO — triggers manual investigation and batch rebuild
    Output:           CORRUPTION_ALERT event
    Recovery:         Fallback to previous nightly snapshot (n-1 batch)

  CONFIDENCE_BELOW_0.65:
    Action:           DELIVER output with DEGRADED_CONFIDENCE flag
    Log:              LOG_LOW_CONFIDENCE
    Escalate:         EMIT alert to ALERT_SERVICE_AGENT
    Restriction:      DO NOT use for championship seeding decisions
    Restriction:      DO NOT use for belt promotion inputs
    Retry:            NO — await next score event

  CONFIDENCE_BELOW_0.45:
    Action:           STOP_EXECUTION, do not deliver output
    Log:              LOG_INCIDENT (severity: HIGH)
    Escalate:         ESCALATE_TO = OBSERVABILITY_AGENT
    Output:           Structured error: { "status": "CONFIDENCE_TOO_LOW", "threshold": 0.45 }

  SCORE_INJECTION_ANOMALY (Z-score > 3.5):
    Action:           FLAG output as SUSPICIOUS, deliver with anomaly_flag set
    Log:              LOG_ANOMALY (severity: MEDIUM)
    Escalate:         EMIT to TRUST_SYSTEM_AGENT + COLLUSION_DETECTION_AGENT
    Restriction:      Suspicious scores excluded from championship seeding

  SCORE_INJECTION_ANOMALY (Z-score > 5.0):
    Action:           BLOCK score from percentile computation
    Log:              LOG_INCIDENT (severity: HIGH)
    Escalate:         ESCALATE_TO = TRUST_SYSTEM_AGENT (automatic review)
    Output:           Score NOT reflected in percentile — pending review

  COHORT_RAPID_SHIFT (> 5% rank changes in 24h outside batch window):
    Action:           FLAG cohort as ANOMALOUS, hold rank update events
    Log:              LOG_ANOMALY (severity: HIGH)
    Escalate:         EMIT to COLLUSION_DETECTION_AGENT
    Restriction:      Hold championship seeding for affected cohort

  BATCH_WINDOW_OVERRUN (nightly batch not complete by 06:30 UTC):
    Action:           Emit BATCH_OVERRUN_ALERT, continue processing
    Log:              LOG_INCIDENT (severity: MEDIUM)
    Escalate:         Notify PLATFORM_ADMIN
    Fallback:         Previous batch snapshot remains active until new one complete

POLICY_SUMMARY:
  NO_SILENT_FAILURES:       TRUE
  PARTIAL_OUTPUT:           FORBIDDEN
  ASSUMPTION_ON_FAILURE:    FORBIDDEN
  STALE_DATA_FALLBACK:      Permitted ONLY for non-competitive, non-certification contexts
                            with explicit stale_data_flag=true in output
```

---

## 🔗 SECTION 10 — INTER-AGENT DEPENDENCY MAP

### 10.1 Upstream Event Contracts

```yaml
FROM: INTELLIGENCE_GROWTH_TIME_SERIES_AGENT
  Event:          TRAJECTORY_COMPUTED
  Payload:        { user_id, dimension, current_score, confidence, timestamp }
  SLA:            Delivered within 3s of IGTS computation
  Usage:          Triggers real-time percentile update

FROM: ASSESSMENT_ENGINE_AGENT
  Event:          ASSESSMENT_COMPLETED
  Payload:        { user_id, dimension, score, confidence, timestamp, assessment_type }
  SLA:            Delivered within 5s of assessment submit
  Usage:          Triggers real-time percentile update

FROM: DOJO_SCORING_AGENT
  Event:          MATCH_SCORED
  Payload:        { user_id, skill_dimension, weighted_score, mentor_bias_flag, match_id }
  SLA:            Delivered within 10s of match finalization
  Note:           If mentor_bias_flag = true: score marked PROVISIONAL until recalibration

FROM: CHAMPIONSHIP_RESULT_AGENT
  Event:          CHAMPIONSHIP_ROUND_COMPLETE
  Payload:        { user_id, round_id, performance_vector, ranking }
  SLA:            Delivered within 30s of round finalization
  Usage:          Triggers percentile update + championship seeding input refresh

FROM: FEATURE_STORE_AGENT
  Event:          FEATURE_VECTOR_UPDATED
  Payload:        { user_id, feature_name, feature_value, timestamp }
  Frequency:      Batch-delivered every 5 minutes
  Usage:          Refreshes demographic segmentation features

FROM: INSTITUTE_REGISTRY_AGENT
  Event:          COHORT_MEMBERSHIP_CHANGED
  Payload:        { cohort_id, user_id, change_type: JOINED | LEFT, timestamp }
  SLA:            Delivered within 60s of membership change
  Usage:          Triggers cohort distribution rebuild for affected cohort

FROM: BATCH_SCHEDULER
  Event:          BATCH_RECALCULATION_TRIGGER
  Payload:        { batch_id, batch_date_utc, scope, auth_token }
  Schedule:       Nightly 02:30 UTC
  Usage:          Triggers full population recalculation
```

### 10.2 Downstream Event Contracts

```yaml
TO: RANK_ENGINE_AGENT
  Trigger:        RANK_UPDATE_EVENT (on rank_band_changed = true)
  Payload:        {
    user_id, dimension, old_rank_band, new_rank_band,
    old_percentile, new_percentile, timestamp
  }
  SLA:            Emitted within 2s of percentile computation

TO: ALERT_SERVICE_AGENT
  Trigger:        RANK_BAND_CROSSING_ALERT | REGRESSION_ALERT | PLATEAU_ALERT
  Payload:        {
    user_id, alert_type, dimension, previous_band,
    current_band, context_narrative
  }

TO: CHAMPIONSHIP_SEEDING_AGENT
  Trigger:        SEEDING_PROFILE_UPDATED
  Payload:        {
    user_id, dimension, percentile_global, percentile_cohort,
    confidence_score, fairness_flags, seeding_eligible: boolean
  }
  Restriction:    confidence_score < 0.75 → seeding_eligible = false

TO: AI_MENTOR_AGENT
  Trigger:        PERCENTILE_PROFILE_UPDATED
  Payload:        {
    user_id, dimension, current_percentile, rank_band,
    gap_to_next_band, next_band_label
  }

TO: PARENT_INSIGHT_AGENT
  Trigger:        GROWTH_COMPARATIVE_UPDATED
  Payload:        {
    child_id, dimension, age_cohort_percentile,
    national_percentile, narrative_summary
  }

TO: INSTITUTE_ANALYTICS_AGENT
  Trigger:        COHORT_DISTRIBUTION_UPDATED
  Payload:        {
    cohort_id, dimension, distribution_snapshot,
    rank_band_distribution, growth_velocity_avg
  }

TO: RECRUITER_INTELLIGENCE_AGENT
  Trigger:        CANDIDATE_RANK_UPDATED
  Payload:        {
    user_id, dimension, global_percentile, domain_percentile,
    rank_band, consent_scope_applied
  }

TO: OBSERVABILITY_AGENT
  Trigger:        Any anomaly, drift, incident, or low-confidence event
  Payload:        Structured incident object (severity, context, affected scope)

TO: FEATURE_STORE_AGENT (emit back — feedback loop)
  Trigger:        PERCENTILE_COMPUTED
  Payload:        {
    user_id, feature_name, feature_value, timestamp, source_agent
  }
  Features_emitted:
    - current_global_percentile_{dimension}
    - current_cohort_percentile_{dimension}
    - rank_band_encoded_{dimension}
    - percentile_delta_30d_{dimension}
    - gap_to_elite_percentile_{dimension}

TO: PLATFORM_LEADERBOARD_SERVICE
  Trigger:        LEADERBOARD_POSITION_UPDATED
  Payload:        {
    user_id, dimension, scope, rank_position, percentile,
    anonymized_handle, timestamp
  }
  Note:           Leaderboard uses display handles — no PII exposed on public boards

EVENT_BUS:            Kafka (Apache Avro — schema registry enforced)
EVENT_DELIVERY:       At-least-once with idempotency guard at all consumers
SCHEMA_REGISTRY:      Confluent Schema Registry (all Avro schemas versioned)
```

---

## 🌊 SECTION 11 — PASSIVE INTELLIGENCE COMPATIBILITY

```yaml
FEATURE_STORE_EMIT_CONTRACT:
  This agent emits the following feature vectors to FEATURE_STORE_AGENT
  on every successful percentile computation:

  FEATURES_EMITTED (per dimension):
    {
      "user_id":        "UUID",
      "feature_name":   "string (see list below)",
      "feature_value":  "Float | Int (encoded)",
      "timestamp":      "ISO 8601",
      "source_agent":   "POPULATION_PERCENTILE_ENGINE_AGENT"
    }

  FEATURE_NAMES_EMITTED:
    current_global_percentile_{dimension}           (Float [0-100])
    current_cohort_percentile_{dimension}           (Float [0-100] | null)
    current_national_percentile_{dimension}         (Float [0-100])
    rank_band_encoded_{dimension}                   (Int: 0=FOUNDATIONAL...4=ELITE)
    percentile_delta_30d_{dimension}                (Float, signed)
    percentile_delta_90d_{dimension}                (Float, signed)
    gap_to_elite_percentile_{dimension}             (Float — score gap to 90th pct)
    gap_to_next_band_score_{dimension}              (Float)
    cohort_rank_position_{dimension}                (Int | null)
    small_cohort_flag_{dimension}                   (Int: 0|1)
    seeding_eligible_flag                           (Int: 0|1)
    distribution_skew_flag_{dimension}              (Int: 0|1)

  CONSUMPTION_BY_DOWNSTREAM:
    RECRUITER_INTELLIGENCE_AGENT:   uses global_percentile for candidate ranking
    CHAMPIONSHIP_SEEDING_AGENT:     uses percentile + seeding_eligible for bracket fairness
    AI_MENTOR_AGENT:                uses gap_to_next_band for personalized guidance
    IGTS_AGENT:                     uses percentile_delta as trajectory validation signal
    PLACEMENT_PROBABILITY_AGENT:    uses global_percentile + rank_band for hiring prediction
```

---

## 🏆 SECTION 12 — GROWTH ENGINE HOOK

```yaml
RANK_BAND_CROSSING_EVENTS:
  Trigger conditions:
    - User moves from DEVELOPING → PROFICIENT (50th percentile crossed)
    - User moves from PROFICIENT → ADVANCED (75th percentile crossed)
    - User moves from ADVANCED → ELITE (90th percentile crossed)
    - User enters Top 1% nationally for any dimension
    - User enters Top 1% globally for any dimension
    - User becomes #1 in their institute cohort for any dimension

  Events Emitted:
    RANK_UPDATE_EVENT:
      { user_id, dimension, old_band, new_band, old_percentile, new_percentile, timestamp }
    
    XP_UPDATE_EVENT:
      { user_id, xp_earned, reason: "PERCENTILE_BAND_PROMOTION", dimension, new_band }
      XP_VALUES (immutable — requires MAJOR version bump to change):
        DEVELOPING → PROFICIENT:     500 XP
        PROFICIENT → ADVANCED:     1,500 XP
        ADVANCED → ELITE:          5,000 XP
        Top 1% National:           2,000 XP bonus
        Top 1% Global:            10,000 XP bonus
        #1 Institute Cohort:         800 XP bonus
    
    SHARE_TRIGGER_EVENT:
      { user_id, achievement: "PERCENTILE_MILESTONE", dimension, new_band, share_card_data }
      share_card_data includes:
        — Percentile badge (visual)
        — "Top X% in [Dimension] Intelligence — Ecoskiller Antigravity"
        — Anonymous cohort size reference
        — No raw score exposed on share card

DEMOTION_POLICY:
  - Rank band demotion also triggers RANK_UPDATE_EVENT (no XP deduction)
  - Demotion triggers ALERT_SERVICE_AGENT (supportive — not punitive messaging)
  - Demotion triggers AI_MENTOR_AGENT with recovery guidance
  - Share trigger NOT fired on demotion

CHAMPIONSHIP_SEEDING_HOOK:
  - CHAMPIONSHIP_SEEDING_AGENT subscribes to SEEDING_PROFILE_UPDATED event
  - Fairness brackets built on global_percentile ± 15 percentile band
  - Users with confidence_score < 0.75 excluded from ranked seeding
  - Seeding recalculated fresh for each tournament — no cached seeding reuse
```

---

## 📊 SECTION 13 — PERFORMANCE MONITORING

```yaml
AGENT_METRICS (reported continuously to OBSERVABILITY_AGENT):

  Throughput:
    - events_processed_per_second (target: stable up to 80,000 RPS)
    - batch_records_per_second (target: > 50,000 during nightly batch)

  Success_Rate:
    Target:     > 99.7% (excluding caller-side validation failures)
    Alert:      < 99.0%
    Critical:   < 98.0%

  Error_Rate:
    Target:     < 0.3%
    Alert:      > 0.5%
    Critical:   > 1.0%

  Latency:
    Real-time p50:    Target < 80ms   | Alert > 150ms  | Critical > 300ms
    Real-time p95:    Target < 250ms  | Alert > 400ms  | Critical > 700ms
    Real-time p99:    Target < 500ms  | Alert > 800ms  | Critical > 1500ms
    Query p50:        Target < 100ms  | Alert > 200ms  | Critical > 400ms

  Cache_Metrics:
    Cache_hit_rate:   Target > 90% (real-time path)
    Cache_miss_rate:  Alert if > 15% sustained
    Redis_memory:     Alert if > 80% of allocated memory

  Queue_Metrics:
    Kafka_consumer_lag:   Target < 500 | Alert > 2,000 | Critical > 10,000

  Drift_Metrics:
    Distribution_drift_events_per_week:   Baseline + 2σ alert
    Score_anomaly_rate_per_1000:          Baseline + 2σ alert
    Cohort_rapid_shift_events:            Any event → immediate alert

  Confidence_Score_Distribution:
    Rolling 7-day mean confidence:   Alert if < 0.75
    Low_confidence_rate (< 0.65):    Alert if > 5% of executions

  Batch_Metrics:
    Batch_completion_time:           Target < 4h | Alert > 3.5h | Critical > 4h (overrun)
    Batch_failure_rate:              Alert > 0.1% of records failed
    Records_with_rank_change:        Tracked and compared to prior batch (anomaly if > 20% delta)

  Fairness_Metrics:
    Small_cohort_suppression_rate:   Tracked per segment (alert if suppression > 15% of queries)
    DP_applied_rate:                 Tracked (informational)
    Cultural_bias_check_failures:    Any failure → immediate SECURITY alert

DASHBOARDS:
  - Grafana: Real-time agent health dashboard
  - Panels: Latency heatmap, throughput, error rate, confidence distribution,
            queue lag, drift indicators, fairness metrics, batch progress tracker
  - Alerts: PagerDuty integration (severity HIGH + CRITICAL)
  - SLA Dashboard: Tenant-facing uptime and percentile freshness indicators
```

---

## 🔄 SECTION 14 — VERSIONING POLICY

```yaml
VERSION_FORMAT:         Semantic versioning (MAJOR.MINOR.PATCH)
AGENT_VERSION:          ECO-AG-PPE-002 v1.0.0

CHANGE_CLASSIFICATION:
  PATCH bump:
    - Bug fix in computation logic
    - Performance optimization
    - Log field addition
    - Cache tuning
    - Alert threshold adjustment

  MINOR bump:
    - New optional output field
    - New scope level (e.g., new regional scope)
    - New fairness metric
    - New feature emitted to Feature Store
    - New LLM narrative template

  MAJOR bump:
    - Rank band threshold change (ELITE/ADVANCED/PROFICIENT/DEVELOPING/FOUNDATIONAL boundaries)
    - Core percentile computation method change (ECDF → parametric, etc.)
    - Input schema field rename or removal
    - Cohort segmentation dimension addition or removal
    - XP award values change
    - Differential privacy epsilon change
    - Minimum cohort size threshold change

MUTATION_POLICY:        ADD-ONLY
  - New fields may be added to all schemas
  - Existing fields may NOT be renamed or removed without MAJOR bump
  - All consumers must be tolerant of additional unknown fields

DISTRIBUTION_SNAPSHOT_VERSIONING:
  - Every nightly batch creates a new versioned distribution snapshot
  - Snapshots are immutable once written
  - Snapshot version referenced in every output and audit record
  - Previous N=30 snapshots retained (30-day history)
  - Older snapshots archived (never deleted)

BACKWARD_COMPATIBILITY:
  - MAJOR version changes: 90-day backward compatibility window
  - All consumers notified via PLATFORM_CHANGE_NOTIFICATION_SERVICE
  - Blue/green deployment for MAJOR changes
  - Rollback: previous agent version re-deployable within 15 minutes

RANK_BAND_THRESHOLD_FREEZE:
  - Thresholds are IMMUTABLE within a MAJOR version
  - Any threshold change requires:
    1. MAJOR version bump
    2. Governance board approval (DOJO GOVERNANCE_BOARD_LOCK)
    3. User notification (existing ranks may change)
    4. Full population recalculation after change
    5. Audit record of threshold change decision
```

---

## 🚫 SECTION 15 — NON-NEGOTIABLE RULES

```yaml
THIS AGENT MUST NOT:
  COMPUTATION_RULES:
    - Compute percentile using ML models that are not explainable (black-box forbidden)
    - Use parametric distribution assumptions without testing goodness-of-fit
    - Manually set a user's percentile rank without going through computation pipeline
    - Backfill historical rank records with newly computed values (historical records immutable)
    - Suppress or omit anomaly flags from output to make output look cleaner
    - Use gender as a segmentation feature under any condition

  DATA_RULES:
    - Access raw score data across tenant boundaries
    - Expose individual user scores in population distribution outputs
    - Expose cohort-level data to users who are not members of that cohort
    - Allow recruiter access to percentile data without verified consent
    - Mix domain-track data in cross-domain percentile computation

  GOVERNANCE_RULES:
    - Change rank band thresholds without MAJOR version bump + governance approval
    - Override championship seeding without CHAMPIONSHIP_SEEDING_AGENT authority
    - Emit rank update events without computation having completed successfully
    - Allow LLM to alter numeric output values
    - Produce partial output on any failure condition
    - Assume any missing input field value

  AUDIT_RULES:
    - Modify or delete any audit record
    - Omit audit record on any execution (success or failure)
    - Log PII in unencrypted audit fields

  SECURITY_RULES:
    - Execute cross-tenant population aggregation using raw PII
    - Bypass RBAC/ABAC checks for any query type
    - Serve stale cache data (> 60 min) in competitive or certification contexts
    - Allow unauthenticated access to any percentile endpoint

  GENERAL:
    - Create hidden scoring or ranking logic outside this documented specification
    - Execute functionality outside the defined scope
    - Interpret ambiguous input — HALT and reject instead
    - Allow AI layer to make decisions — AI assists narrative only
```

---

## 🔒 FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════════╗
║      POPULATION PERCENTILE ENGINE AGENT — GOVERNANCE SEAL            ║
╠══════════════════════════════════════════════════════════════════════╣
║  Platform:              Ecoskiller Antigravity                        ║
║  Agent ID:              ECO-AG-PPE-002                               ║
║  Agent Version:         v1.0.0                                       ║
║  Seal Date:             2026-02-26 UTC                               ║
║  Specification Class:   PRODUCTION LOCKED ARTIFACT                   ║
╠══════════════════════════════════════════════════════════════════════╣
║  EXECUTION MODE:        DETERMINISTIC                                ║
║  MUTATION POLICY:       ADD-ONLY VERSIONED                           ║
║  CREATIVE INTERPRET:    FORBIDDEN                                     ║
║  ASSUMPTION FILLING:    FORBIDDEN                                     ║
║  SILENT FAILURE:        FORBIDDEN                                     ║
║  PARTIAL OUTPUT:        FORBIDDEN                                     ║
╠══════════════════════════════════════════════════════════════════════╣
║  PRIMARY METHOD:        ECDF + KDE (Statistical — Non-Parametric)    ║
║  RANK BANDS:            5-tier threshold-governed (IMMUTABLE v1)     ║
║  AI LAYER:              Narrative-only (zero decision authority)      ║
║  DRIFT DETECTION:       ACTIVE (KS-test + PSI + Z-score anomaly)     ║
║  FAIRNESS ENGINE:       ACTIVE (small-cohort | DP | skew | outlier)  ║
║  GENDER SEGMENTATION:   PERMANENTLY FORBIDDEN                        ║
╠══════════════════════════════════════════════════════════════════════╣
║  AUDIT TRAIL:           IMMUTABLE APPEND-ONLY (SHA-256 chained)      ║
║  TENANT ISOLATION:      HARD ENFORCED                                ║
║  DOMAIN ISOLATION:      HARD ENFORCED                                ║
║  CONSENT ENFORCEMENT:   MANDATORY (recruiter path)                   ║
║  ANONYMIZATION:         DIFFERENTIAL PRIVACY (global/national scope) ║
║  SECURITY MODEL:        ZERO-TRUST                                   ║
╠══════════════════════════════════════════════════════════════════════╣
║  TRIGGER TYPES:         3 (RT Score Event | Batch | On-Demand Query) ║
║  UPSTREAM AGENTS:       7 registered                                 ║
║  DOWNSTREAM AGENTS:     11 registered                                ║
║  EVENT BUS:             KAFKA (Avro + Schema Registry)               ║
║  FEATURE STORE:         BIDIRECTIONAL (12 features emitted)          ║
║  GROWTH ENGINE:         ACTIVE (XP tiers + Share triggers locked)    ║
║  LEADERBOARD:           INTEGRATED (anonymized handles only)         ║
╠══════════════════════════════════════════════════════════════════════╣
║  CHAMPIONSHIP SEEDING:  FAIRNESS-CONTROLLED (±15 percentile band)    ║
║  SEEDING CONFIDENCE:    Minimum 0.75 required for ranked seeding      ║
║  PROVISIONAL SCORES:    Mentor-bias flagged scores excluded until clean║
║  BATCH WINDOW:          02:30–06:30 UTC (4-hour max)                 ║
╠══════════════════════════════════════════════════════════════════════╣
║  RANK BAND THRESHOLDS:  ELITE≥90 | ADV≥75 | PROF≥50 | DEV≥25 | FOUND║
║  XP AWARDS:             LOCKED (MAJOR bump required to change)       ║
║  SMALL COHORT FLOOR:    30 users minimum (below → suppress)          ║
║  DP EPSILON:            0.1 (global/national scope)                  ║
╠══════════════════════════════════════════════════════════════════════╣
║  INTERPRETATION AUTH:   NONE                                         ║
║  ARCHITECTURE AUTH:     LOCKED                                       ║
║  RANK BAND CHANGE AUTH: GOVERNANCE BOARD ONLY + MAJOR VERSION BUMP   ║
║  SPEC AUTHORITY:        ECOSKILLER PLATFORM GOVERNANCE               ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

*This document is a sealed production artifact for the Ecoskiller Antigravity platform.
Any modification to rank band thresholds, computation methodology, fairness controls,
or security rules requires a MAJOR version bump with governance board approval.
All other modifications require at minimum a PATCH version bump and backward-compatibility validation.
The agent must not deviate from this specification under any operating condition.*

**END LOCKED ARTIFACT — POPULATION_PERCENTILE_ENGINE_AGENT v1.0.0**
