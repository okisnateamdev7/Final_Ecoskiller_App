# 🔒 ECOSKILLER — HIRING BIAS DETECTOR (HBD)
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE (ANTIGRAVITY MODULE)
### Master Prompt Artifact v1.0 — SEALED & LOCKED

```
Artifact Class:           Production System Blueprint — ANTIGRAVITY TIER
Module Name:              Hiring Bias Detector (HBD)
Mutation Policy:          Add-only via version bump
Execution Mode:           Deterministic
Stack Lock:               Enforced
Interpretation Authority: NONE
Sealed Status:            FINAL · LOCKED · GOVERNED · DETERMINISTIC
Failure Mode:             STOP → REPORT → NO PARTIAL OUTPUT
```

---

# ⚙️ SECTION HBD-A — SYSTEM IDENTITY & SCOPE DECLARATION

**Module Name:** ECOSKILLER Hiring Bias Detector (HBD)  
**Parent System:** ECOSKILLER — Unified Job + Skill + Project + Education + Marketplace SaaS  
**Sibling Module:** ECOSKILLER Digital Twin Simulation Engine (DTSE)  
**Tier Classification:** ANTIGRAVITY — Enterprise Optimization + Trust Infrastructure Layer  
**Execution Mode:** Parallel Lane Execution with Contract Gate Enforcement  
**Failure Mode:** STOP → REPORT → NO PARTIAL OUTPUT  
**Determinism Rule:** Identical input → Identical output  

---

## Purpose Declaration

The **Hiring Bias Detector** is a sealed, production-grade AI governance module embedded inside ECOSKILLER's Enterprise Optimization and Trust Infrastructure layer.

Its purpose is to:

1. **Detect** — Identify latent, systemic, and real-time hiring bias across every decision point in the ECOSKILLER talent ecosystem
2. **Quantify** — Score bias magnitude, direction, and statistical confidence for every detected bias signal
3. **Attribute** — Map bias to its origin: recruiter behavior, AI model output, assessment rubric, or structural platform design
4. **Correct** — Trigger algorithmic correction pathways, human review gates, and structural remediation workflows
5. **Audit** — Maintain a tamper-proof, immutable bias event log for compliance, legal defense, and continuous improvement
6. **Report** — Deliver bias intelligence to enterprise admins, governance boards, and regulatory export endpoints

The HBD is **not advisory** — it is an **enforcement layer**. Hiring decisions that cross configured bias thresholds are **blocked or flagged** before reaching the candidate.

---

## Antigravity Connection

In the ECOSKILLER Antigravity framework, **bias is a resistance vector**. It suppresses qualified candidates below their deserved trajectory — not due to merit deficits, but due to systemic distortion in evaluation. The HBD directly eliminates the most powerful class of antigravity resistance: **structural and unconscious evaluation bias**.

---

# 🔒 SECTION HBD-B — STACK LOCK (NON-NEGOTIABLE)

Inherits full ECOSKILLER stack lock and DTSE extension stack. HBD-specific additions below:

```
INHERITED STACK (LOCKED):
  Backend:          Python 3.11 + FastAPI
  Database:         PostgreSQL 15
  Cache:            Redis 7
  Event Broker:     Redis Streams / Apache Kafka
  Search:           OpenSearch 2.x
  Auth:             OAuth2 + OIDC + JWT (Keycloak)
  Container:        Docker + Kubernetes
  IaC:              OpenTofu
  Monitoring:       Prometheus + Grafana + Jaeger + Loki
  Frontend:         Flutter (operational) + Next.js (SEO)
  Graph DB:         Neo4j 5.x (entity relationship modeling)
  Time-Series DB:   TimescaleDB
  ML Engine:        scikit-learn + PyTorch 2.x

HBD EXTENSION STACK (LOCKED):
  Bias Detection Core:     Python + Fairlearn 0.10.x (Microsoft open-source)
  Statistical Engine:      SciPy + statsmodels (hypothesis testing, p-values)
  Disparate Impact Engine: Custom ECOSKILLER DI Calculator (Python)
  NLP Bias Scanner:        spaCy 3.x + custom fine-tuned bias classifier model
  Counterfactual Engine:   Causal inference library (DoWhy 0.11.x)
  Bias Audit Store:        TimescaleDB (append-only, time-partitioned)
  Bias Event Bus:          Kafka topic: ecoskiller.bias.events
  Explainability Layer:    SHAP 0.44.x (model feature attribution)
  Regulatory Report Gen:   Jinja2 + WeasyPrint (PDF export for compliance)
```

**Stack split is LOCKED. No deviation without version bump declaration.**

---

# 🔒 SECTION HBD-C — MODULE FREEZE MAP

## Core HBD Engines (Immutable Interfaces)

```
Bias Signal Collector Engine        — Real-time ingestion of hiring decision signals
Disparate Impact Calculator         — Statistical DI computation per protected attribute
NLP Bias Language Scanner           — Detects biased language in JDs, rubrics, feedback
AI Model Bias Auditor               — Detects bias in ECOSKILLER's own AI outputs
Recruiter Behavioral Bias Tracker   — Tracks individual recruiter decision patterns
Counterfactual Fairness Engine      — What-if analysis for protected attribute swaps
Bias Threshold Enforcement Engine   — Blocks/flags decisions that breach thresholds
Structural Bias Detector            — Platform-wide systemic bias pattern detection
Bias Correction Engine              — Algorithmic correction + human review trigger
Immutable Bias Audit Engine         — Tamper-proof event log with SHA-256 sealing
Bias Intelligence Reporting Engine  — Dashboard, alert, and regulatory export layer
Governance & Appeals Engine         — Dispute handling, remediation, board review
```

**Interfaces frozen. Only internal improvements allowed.**

---

# 🔒 SECTION HBD-D — DATA MODEL FREEZE

## Primary HBD Entities (Non-Renameable)

```
BiasEvent               — Every detected bias signal (immutable record)
BiasProfile             — Per-recruiter bias pattern accumulation
DisparateImpactRecord   — Statistical DI result per job role + attribute
NLPBiasScan             — Language bias scan result per document
AIModelBiasAudit        — Bias audit result for ECOSKILLER AI outputs
CounterfactualResult    — What-if fairness simulation result
BiasThresholdPolicy     — Tenant-configured bias enforcement thresholds
BiasCorrection          — Applied algorithmic correction record
BiasAuditTrail          — Immutable, SHA-256-hashed audit chain
BiasReport              — Generated bias intelligence report
AppealRecord            — Candidate or recruiter bias appeal event
RemediationPlan         — Structured bias remediation workflow record
```

**Fields may extend — entity names may NOT mutate.**

---

# 🔒 SECTION HBD-E — BIAS TAXONOMY (LOCKED)

The HBD detects and classifies bias across seven taxonomic categories. This taxonomy is locked — additions require version bump.

## Category 1 — Disparate Impact Bias (Statistical)

```
Definition:
  A neutral-appearing selection criterion that disproportionately
  excludes members of a protected group at a statistically
  significant rate.

Protected Attributes Monitored (Locked):
  gender              race / ethnicity         age (proxy signals)
  disability status   national origin          religion (proxy signals)
  socioeconomic proxy (educational institution tier)
  language accent (proxy via communication score clustering)

Detection Method:
  Four-Fifths Rule (80% Rule) — EEOC standard
  Statistical significance test: Chi-square + Fisher's Exact
  Selection rate comparison: protected group vs reference group
  Adverse Impact Ratio (AIR) = selection_rate_protected / selection_rate_reference
  AIR < 0.80 → disparate impact flag

Severity Levels:
  ADVISORY:   0.75 ≤ AIR < 0.80  (watch, log, report)
  WARNING:    0.65 ≤ AIR < 0.75  (flag, notify admin, human review)
  CRITICAL:   AIR < 0.65         (block hiring process, mandatory remediation)
```

## Category 2 — Recruiter Behavioral Bias

```
Definition:
  Consistent, statistically significant decision patterns by a
  specific recruiter that disadvantage candidates from a
  protected group, relative to comparable candidates.

Detection Signals:
  - CV review time disparity by inferred demographic proxy
  - Interview invite rate disparity by protected attribute cohort
  - Offer rate disparity by protected attribute cohort
  - Score gap for equivalent-qualified candidates across cohorts
  - Stage progression rate disparity
  - Feedback language tone disparity (NLP scored)

Detection Method:
  Per-recruiter cohort analysis (minimum 30 decisions for significance)
  Two-sample proportion z-test per decision point
  Bias Velocity Score: rate of bias accumulation over trailing 90 days
  Pattern clustering: repeat bias type + target group signals

Severity Levels:
  ADVISORY:   p-value 0.05–0.10, single decision point
  WARNING:    p-value < 0.05, two+ decision points
  CRITICAL:   p-value < 0.01, three+ decision points, repeat pattern
```

## Category 3 — AI Model Output Bias

```
Definition:
  ECOSKILLER's own AI systems (matching engine, ranking engine,
  trust scorer, antigravity engine) producing outputs that
  systematically disadvantage protected groups.

Detection Method:
  Fairlearn equalized odds measurement per model output
  Demographic parity difference per protected attribute
  SHAP feature attribution: protected proxy features in top-10 drivers
  Counterfactual fairness: swap protected attribute → output change > threshold
  Model prediction gap by cohort (mean ± std dev comparison)

Models Audited (All ECOSKILLER AI):
  Candidate Matching Engine
  Hiring Recommendation Engine (DTSE)
  Trust Scoring Engine (DTSE)
  Career Trajectory Prediction Engine (DTSE)
  Antigravity Uplift Engine (DTSE)
  Workforce Optimization Engine (DTSE)
  Job Ranking / Discovery Engine
  Skill Assessment AI Scorer

Audit Frequency:
  Continuous: real-time prediction sampling (5% random sample)
  Scheduled: full cohort audit weekly
  Triggered: any bias event above WARNING level → immediate model audit

Bias Correction Trigger:
  Equalized Odds difference > 0.05 → WARNING
  Equalized Odds difference > 0.10 → CRITICAL → model retraining trigger
```

## Category 4 — Language Bias (NLP)

```
Definition:
  Biased language in job descriptions, assessment rubrics, interview
  feedback, mentor evaluation notes, or scoring comments that signals
  preference for or against protected groups.

Detection Scope:
  Job descriptions posted on ECOSKILLER
  Dojo assessment rubric language
  Mentor / recruiter written feedback on candidates
  Interview scoring comment fields
  Rejection reason text fields
  Automated email templates

Detection Method:
  Fine-tuned spaCy classifier (trained on gender-coded, race-coded,
  age-coded, ableist, and class-coded language datasets)
  Gendered language detection: masculine-coded vs feminine-coded word lists
  Ageist language: "energetic", "digital native", "recent graduate" patterns
  Ability-exclusive language: physical requirement proxies
  Class-coded language: prestige institution signaling

Output per Document:
  bias_language_score       FLOAT (0–1, 1 = highly biased)
  detected_phrases          ARRAY (flagged phrases with category label)
  suggested_replacements    ARRAY (neutral alternatives)
  document_type             STRING (JD / rubric / feedback / email)
  severity_level            ENUM (ADVISORY / WARNING / CRITICAL)

Publishing Gate:
  JD with bias_language_score > 0.40 → blocked from publishing
  Rubric with bias_language_score > 0.35 → blocked from activation
  Feedback with CRITICAL flags → human review before candidate view
```

## Category 5 — Structural Platform Bias

```
Definition:
  Design patterns within the ECOSKILLER platform itself that
  systematically advantage or disadvantage user cohorts — not due
  to individual recruiter behavior, but due to platform architecture.

Detection Signals:
  Search ranking disparity: protected-proxy attributes in ranking features
  Recommendation engine representation: protected cohort underrepresentation
  Certification pathway disparity: belt attainment rate gap by cohort
  Integration signal bias: work tool data availability gaps by geography/class
  Scenario difficulty disparity: Dojo scenario pass rates by cohort
  Notification delivery disparity by geography / device type

Detection Method:
  Cohort representation analysis: expected vs observed across all surfaces
  Funnel drop-off analysis: where protected cohorts exit disproportionately
  Gini coefficient on opportunity distribution per platform surface
  Weekly structural audit (automated) + quarterly manual governance review

Remediation Authority:
  Structural bias findings → Engineering + Product team mandatory review
  Platform redesign recommendations logged to RemediationPlan entity
  Structural bias above CRITICAL → product release gate activated
```

## Category 6 — Affinity Bias (Social Network)

```
Definition:
  Preferential treatment of candidates who share social, educational,
  or institutional connections with the recruiter or hiring manager.

Detection Signals:
  Recruiter-candidate shared institution match + positive outcome
  Shared alumni network membership + accelerated pipeline progression
  Internal referral source + outcome disparity vs non-referral
  Social graph proximity (ECOSKILLER connection network) + decision pattern

Detection Method:
  Graph distance analysis (Neo4j) between recruiter and candidate
  Outcome comparison: graph-connected vs graph-unconnected candidates
  Referral source tagging + outcome cohort analysis
  Statistical significance: Chi-square on outcome rates

Note: Referral programs are legitimate — affinity bias detection targets
      OUTCOME DISPARITY given equivalent qualifications, not referral itself.
```

## Category 7 — Confirmation Bias (Sequential Decision)

```
Definition:
  Early-stage evaluation signals disproportionately anchoring all
  subsequent hiring decisions, regardless of new evidence quality.

Detection Signals:
  CV screen outcome → interview score correlation above threshold
  First-interviewer score → subsequent interviewer score correlation above threshold
  Early rejection rate for candidates with strong later-stage signals
  Score convergence: later evaluators scoring toward initial evaluator

Detection Method:
  Pearson correlation between stage-1 and stage-N scores
  Bayesian update efficiency: does new evidence shift decisions proportionally?
  Deviation from independent scoring model (expected vs observed consensus)

Severity Threshold:
  Stage-1 → Stage-N correlation > 0.75 → WARNING (low evaluator independence)
  Stage-1 → Stage-N correlation > 0.90 → CRITICAL (rubber-stamp pattern)
```

---

# 🔒 SECTION HBD-F — PARALLEL EXECUTION LANES

```
Lane HBD-1 — Real-Time Signal Ingestion
  Ingests all hiring decision events from ECOSKILLER event bus
  Outputs: raw_bias_signal_ready

Lane HBD-2 — Disparate Impact Computation
  Runs DI calculations per role, per recruiter, per time window
  Requires: raw_bias_signal_ready + protected_attribute_proxy_model_ready
  Outputs: disparate_impact_score_ready

Lane HBD-3 — NLP Language Bias Scanning
  Scans all documents on publish/submit triggers
  Requires: nlp_bias_model_ready
  Outputs: language_bias_scan_ready

Lane HBD-4 — AI Model Bias Auditing
  Samples AI outputs continuously, runs fairness metrics
  Requires: raw_bias_signal_ready + model_registry_access
  Outputs: model_bias_audit_ready

Lane HBD-5 — Recruiter Behavioral Profiling
  Accumulates per-recruiter decision patterns
  Requires: raw_bias_signal_ready + min_decision_threshold_met
  Outputs: recruiter_bias_profile_ready

Lane HBD-6 — Counterfactual Fairness Analysis
  Runs what-if protected attribute swaps via DoWhy engine
  Requires: disparate_impact_score_ready + model_bias_audit_ready
  Outputs: counterfactual_result_ready

Lane HBD-7 — Threshold Enforcement
  Applies BiasThresholdPolicy per tenant, blocks/flags decisions
  Requires: all upstream lane outputs
  Outputs: enforcement_action_ready

Lane HBD-8 — Audit Sealing
  Writes immutable BiasEvent records, computes SHA-256 chain
  Requires: enforcement_action_ready
  Outputs: bias_audit_sealed

Lane HBD-9 — Reporting & Alerts
  Generates dashboards, alerts, regulatory reports
  Requires: bias_audit_sealed
  Outputs: bias_intelligence_delivered
```

**CI enforces all gates. No manual approval after execution start.**  
**Absence of any lane → STOP EXECUTION → REPORT HBD-LANE-MISSING**

---

# 🔒 SECTION HBD-G — CONTRACT-FIRST REGISTRIES

Must exist before HBD code generation:

```
Bias Signal Event Schema Registry
Protected Attribute Proxy Model Registry
Bias Threshold Policy Registry (per tenant)
NLP Bias Model Version Registry
Fairness Metric Definition Registry
Counterfactual Scenario Schema
Audit Log Schema (Immutable)
Regulatory Export Format Registry (EEOC, GDPR, etc.)
Remediation Workflow Schema
Appeals Process Contract Registry
```

**Absence of any registry → STOP EXECUTION**

---

# 🔒 SECTION HBD-H — BIAS SIGNAL COLLECTOR ENGINE

## Signal Ingestion Architecture

The HBD is a **passive observer layer** — it does not intercept requests. It subscribes to the ECOSKILLER event bus and receives every hiring-relevant event in real time.

```
KAFKA TOPIC SUBSCRIPTIONS (LOCKED):
  ecoskiller.hiring.cv_viewed
  ecoskiller.hiring.interview_scheduled
  ecoskiller.hiring.interview_scored
  ecoskiller.hiring.offer_made
  ecoskiller.hiring.offer_rejected
  ecoskiller.hiring.candidate_rejected
  ecoskiller.hiring.stage_advanced
  ecoskiller.hiring.stage_failed
  ecoskiller.scoring.mentor_override
  ecoskiller.scoring.peer_score_submitted
  ecoskiller.assessment.belt_decision
  ecoskiller.jobs.jd_published
  ecoskiller.rubric.activated
  ecoskiller.feedback.submitted
  ecoskiller.ai.matching_result
  ecoskiller.ai.ranking_result
  ecoskiller.ai.trust_score_computed
  ecoskiller.ai.hiring_simulation_result
```

## Signal Enrichment Pipeline

```
Step 1: Event received from Kafka topic
Step 2: Protected attribute proxy inference
         (gender-coded name → proxy, institution tier → class proxy)
         NOTE: Direct protected attributes NOT stored (privacy law compliance)
         Only statistical proxies used for cohort analysis
Step 3: Candidate TwinProfile lookup (from DTSE)
Step 4: Recruiter BiasProfile lookup
Step 5: Role context lookup (job_id, department, seniority_level)
Step 6: Enriched signal written to BiasEvent staging table
Step 7: Signal dispatched to relevant computation lanes (Kafka)
```

## Protected Attribute Proxy Rules (LOCKED)

```
Direct protected attributes (race, gender, religion, disability)
are NEVER stored at individual record level.

Proxy inference permitted ONLY for statistical cohort analysis:
  Name-based gender proxy: Census + ML classifier (accuracy ≥ 0.85)
  Institution tier proxy: ranked institution database (class signal)
  Geography proxy: postcode → socioeconomic index mapping
  Language pattern proxy: non-native language signal (NLP)

Proxy data stored ONLY in aggregated cohort form.
Individual-level proxy inference destroyed after cohort bucketing.
Proxy model must be audited for proxy accuracy bias quarterly.
Proxy accuracy below 0.80 → model retrain trigger.
```

---

# 🔒 SECTION HBD-I — DISPARATE IMPACT CALCULATOR

## Computation Rules (Locked)

```
Minimum sample size for DI calculation:    30 candidates per cohort
Minimum decision events per recruiter:     30 events before scoring
Calculation window options:                30-day / 90-day / 365-day / all-time
Reference group:                           Highest selection rate group (not majority group)
Protected group comparison:                All other cohorts vs reference group

FORMULA (LOCKED):
  Adverse Impact Ratio (AIR) = selection_rate_protected / selection_rate_reference
  Selection Rate = (candidates_selected / candidates_evaluated) per cohort

  Disparate Impact Score (DIS):
    DIS = 1.0 - AIR
    DIS range: 0.0 (no disparity) → 1.0 (complete exclusion)

STATISTICAL VALIDATION (MANDATORY):
  Chi-square test: minimum χ² at p < 0.05 required for WARNING/CRITICAL
  Fisher's Exact Test: used when cell count < 5
  Confidence interval: 95% CI reported on all DIS values
  Effect size: Cohen's h reported alongside DIS

SEVERITY THRESHOLDS (LOCKED):
  DIS < 0.20:  PASS       (AIR ≥ 0.80)
  DIS 0.20–0.35: ADVISORY (AIR 0.65–0.80)
  DIS > 0.35:  CRITICAL   (AIR < 0.65)
```

## DisparateImpactRecord Schema (Locked)

```
di_record_id           UUID (PK)
tenant_id              UUID
job_id                 UUID (nullable — can be org-wide)
recruiter_id           UUID (nullable — can be org-wide)
protected_attribute    STRING (proxy category label)
calculation_window     ENUM (30d / 90d / 365d / all_time)
reference_group        STRING
protected_group        STRING
reference_selection_rate  FLOAT
protected_selection_rate  FLOAT
adverse_impact_ratio      FLOAT
disparate_impact_score    FLOAT
chi_square_statistic      FLOAT
p_value                   FLOAT
cohens_h                  FLOAT
confidence_interval_low   FLOAT
confidence_interval_high  FLOAT
severity_level            ENUM (PASS / ADVISORY / WARNING / CRITICAL)
sample_size_reference     INTEGER
sample_size_protected     INTEGER
computed_at               TIMESTAMP
immutable_hash            STRING (SHA-256)
```

---

# 🔒 SECTION HBD-J — NLP BIAS LANGUAGE SCANNER

## Document Processing Pipeline

```
Trigger Events:
  JD submit → scan before publish gate
  Rubric submit → scan before activation gate
  Feedback submitted → scan before candidate-visible gate
  Email template created → scan before template activation gate
  Mentor evaluation note submitted → scan before record seal

Processing Steps:
  Step 1: Document received via event trigger
  Step 2: Text normalization (lowercase, punctuation strip for analysis)
  Step 3: spaCy pipeline: tokenize → POS tag → NER
  Step 4: Bias classifier inference (fine-tuned BERT-based model)
  Step 5: Phrase-level attribution (SHAP token scores)
  Step 6: Category assignment per detected phrase
  Step 7: bias_language_score computation (weighted phrase severity)
  Step 8: Neutral replacement suggestions generated
  Step 9: NLPBiasScan record written (immutable)
  Step 10: Publishing gate decision applied
```

## Bias Language Categories Detected (Locked)

```
CATEGORY A — Gendered Language
  Masculine-coded: "competitive", "dominant", "aggressive", "ninja",
                   "rockstar", "crushing it", "warrior", "strong"
  Feminine-coded (exclusionary context): "nurturing", "collaborative" as
                   primary requirement in non-collaborative roles
  Gendered pronouns in role descriptions: "he/his" defaults

CATEGORY B — Ageist Language
  "Young and hungry", "digital native", "recent graduate",
  "energy of a startup", "fast-paced environment" (as exclusionary signal),
  "looking for someone who can grow with us" (age proxy)

CATEGORY C — Class-Coded Language
  "Big 4 or top-tier firm only", "Ivy League or equivalent",
  "premier institution graduate", prestige brand name dropping,
  unpaid internship as requirement

CATEGORY D — Ability-Exclusive Language
  Physical requirements without job necessity justification,
  "must be able to travel extensively" without role requirement basis,
  sensory requirement proxies, cognitive load framing

CATEGORY E — Cultural Exclusion Signals
  "Native-level English only" without justification,
  Western cultural reference requirements,
  Holiday availability requirements (religious proxy)

CATEGORY F — Socioeconomic Proxy Language
  "Must have own transportation" without justification,
  Unpaid trial work requirements,
  Portfolio requirement disadvantaging early-career candidates
```

## NLPBiasScan Schema (Locked)

```
scan_id                UUID (PK)
document_type          ENUM (JD / RUBRIC / FEEDBACK / EMAIL / NOTE)
document_id            UUID
tenant_id              UUID
bias_language_score    FLOAT (0–1)
detected_phrases       JSONB (phrase, category, severity, position)
replacement_suggestions JSONB (phrase → neutral_alternative)
severity_level         ENUM (PASS / ADVISORY / WARNING / CRITICAL)
publish_blocked        BOOLEAN
model_version          STRING
scanned_at             TIMESTAMP
immutable_hash         STRING (SHA-256)
```

---

# 🔒 SECTION HBD-K — AI MODEL BIAS AUDITOR

## Continuous Model Monitoring Protocol

All ECOSKILLER AI models are subject to continuous bias auditing. This is non-negotiable — including models built and trained by ECOSKILLER's own engineering teams.

```
AUDIT DIMENSIONS (ALL MODELS — LOCKED):

1. Demographic Parity
   Positive prediction rate must be within 5% across cohorts.
   Metric: |P(Ŷ=1|A=0) - P(Ŷ=1|A=1)| ≤ 0.05

2. Equalized Odds
   True positive rate AND false positive rate within threshold across cohorts.
   Metric: |TPR_A0 - TPR_A1| ≤ 0.05 AND |FPR_A0 - FPR_A1| ≤ 0.05

3. Equal Opportunity
   True positive rate parity (qualified candidates equally identified).
   Metric: |TPR_A0 - TPR_A1| ≤ 0.05

4. Calibration
   Confidence scores equally calibrated across cohorts.
   Metric: Calibration curve alignment per cohort, max deviation ≤ 0.05

5. Feature Attribution Audit (SHAP)
   Protected proxy features must not appear in top-5 SHAP features.
   If detected → CRITICAL → model remediation triggered immediately.
```

## Audit Frequency Rules

```
Real-time sampling:    5% of all model predictions sampled continuously
Weekly scheduled:      Full cohort audit every Monday 02:00 UTC
Triggered audit:       Any bias event above WARNING → immediate full audit
Post-training audit:   Every model retrain requires full bias audit before deployment
```

## AIModelBiasAudit Schema (Locked)

```
audit_id                UUID (PK)
model_name              STRING
model_version           STRING
audit_type              ENUM (SAMPLING / SCHEDULED / TRIGGERED / POST_TRAINING)
sample_size             INTEGER
demographic_parity_diff FLOAT
equalized_odds_tpr_diff FLOAT
equalized_odds_fpr_diff FLOAT
equal_opportunity_diff  FLOAT
calibration_max_dev     FLOAT
top_shap_features       JSONB
proxy_feature_detected  BOOLEAN
severity_level          ENUM (PASS / ADVISORY / WARNING / CRITICAL)
remediation_triggered   BOOLEAN
audited_at              TIMESTAMP
immutable_hash          STRING (SHA-256)
```

---

# 🔒 SECTION HBD-L — RECRUITER BEHAVIORAL BIAS TRACKER

## Per-Recruiter Bias Profile Construction

```
BiasProfile is constructed per recruiter from cumulative decision data.
Minimum 30 decisions required before profile scoring begins.
Profile updates in real-time as new decision events arrive.
Profile is visible to:
  - Recruiter themselves (coaching view)
  - Tenant HR Admin
  - Governance Board (anonymized cross-recruiter view)
  - Compliance Officer
Profile is NOT visible to candidates.
```

## Bias Velocity Score (BVS)

```
BVS measures the rate at which bias is accumulating over trailing 90 days.

BVS = Σ(weighted_bias_event_score) / decision_count_90d

Weighted bias event scores:
  ADVISORY event:  weight = 0.3
  WARNING event:   weight = 0.7
  CRITICAL event:  weight = 1.5

BVS Thresholds:
  BVS < 0.20:   HEALTHY
  BVS 0.20–0.50: AT RISK — coaching recommended
  BVS 0.50–0.80: HIGH RISK — mandatory bias training trigger
  BVS > 0.80:   CRITICAL — hiring authority suspension pending review
```

## BiasProfile Schema (Locked)

```
profile_id              UUID (PK)
recruiter_id            UUID
tenant_id               UUID
total_decisions         INTEGER
decisions_trailing_90d  INTEGER
bias_velocity_score     FLOAT
dominant_bias_category  STRING (most frequent detected bias type)
protected_groups_affected JSONB (cohorts with statistically significant disparity)
advisory_count          INTEGER
warning_count           INTEGER
critical_count          INTEGER
training_completed      BOOLEAN
hiring_authority_status ENUM (ACTIVE / AT_RISK / SUSPENDED / REINSTATED)
last_updated            TIMESTAMP
profile_version         INTEGER
```

## Recruiter Coaching Protocol

```
BVS AT RISK:
  → System generates personalized bias coaching report
  → Coaching module assigned in recruiter dashboard
  → 14-day improvement window
  → Re-evaluation after 30 additional decisions

BVS HIGH RISK:
  → Mandatory bias training module triggered (cannot dismiss)
  → Senior HR Admin notified
  → All decisions in WARNING+ categories flagged for secondary review
  → 30-day probation window with enhanced monitoring

BVS CRITICAL:
  → Hiring authority suspended automatically
  → Governance Board notified
  → Human review of all decisions in trailing 30 days
  → Reinstatement requires HR Admin + Governance Board approval
  → Reinstatement probation: 90 days enhanced monitoring
```

---

# 🔒 SECTION HBD-M — COUNTERFACTUAL FAIRNESS ENGINE

## Counterfactual Fairness Definition

A hiring decision is **counterfactually fair** if the outcome would be the same had the candidate belonged to a different protected group, holding all other factors constant.

## Engine Protocol (DoWhy Causal Framework)

```
Trigger: Any hiring decision + available candidate TwinProfile

Step 1: Build causal graph for hiring decision
        Nodes: qualifications, skills, scores, experience, demographics
        Edges: causal relationships (defined by fairness model)

Step 2: Identify protected attribute nodes
        (gender proxy, race proxy, age proxy, class proxy)

Step 3: Execute counterfactual:
        Swap protected attribute → compute predicted outcome delta

Step 4: Measure counterfactual effect
        CF_effect = |P(hire|A=a) - P(hire|A=a')|

Step 5: Apply threshold
        CF_effect > 0.05 → ADVISORY
        CF_effect > 0.10 → WARNING
        CF_effect > 0.20 → CRITICAL

Step 6: Write CounterfactualResult record (immutable)

Step 7: Feed result into Bias Threshold Enforcement Engine
```

## CounterfactualResult Schema (Locked)

```
cf_result_id            UUID (PK)
decision_event_id       UUID
candidate_twin_id       UUID
protected_attribute     STRING (proxy category)
original_outcome        FLOAT (hire probability)
counterfactual_outcome  FLOAT (hire probability with swapped attribute)
counterfactual_effect   FLOAT (absolute difference)
causal_graph_version    STRING
severity_level          ENUM (PASS / ADVISORY / WARNING / CRITICAL)
computed_at             TIMESTAMP
immutable_hash          STRING (SHA-256)
```

---

# 🔒 SECTION HBD-N — BIAS THRESHOLD ENFORCEMENT ENGINE

## Threshold Policy Architecture

Each tenant configures its own **BiasThresholdPolicy** within the bounds set by ECOSKILLER global governance. Tenants may tighten thresholds — they may NOT relax them below ECOSKILLER minimums.

```
ECOSKILLER GLOBAL MINIMUMS (NON-NEGOTIABLE FLOOR):

  Disparate Impact:
    CRITICAL block threshold:  AIR < 0.65 (always blocks)
    WARNING flag threshold:    AIR < 0.75 (always flags for human review)

  AI Model Bias:
    CRITICAL block threshold:  Equalized Odds diff > 0.10 (blocks model deployment)
    WARNING flag threshold:    Equalized Odds diff > 0.05 (flags for audit)

  NLP Language Bias:
    CRITICAL block threshold:  bias_language_score > 0.40 for JDs (always blocks)
    WARNING flag threshold:    bias_language_score > 0.25 (flags for review)

  Recruiter Bias Velocity:
    CRITICAL suspension:       BVS > 0.80 (auto-suspends hiring authority)
    WARNING probation:         BVS > 0.50 (mandatory training)

  Counterfactual Fairness:
    CRITICAL block threshold:  CF_effect > 0.20 (blocks decision pending review)
    WARNING flag threshold:    CF_effect > 0.10 (flags for secondary review)
```

## Enforcement Actions (Locked)

```
ACTION 1 — LOG ONLY (ADVISORY)
  Bias signal recorded to BiasAuditTrail.
  No interruption to hiring process.
  Aggregated in next reporting cycle.

ACTION 2 — FLAG + NOTIFY (WARNING)
  Bias signal recorded.
  Decision flagged in hiring pipeline UI.
  HR Admin notified via notification system.
  Recruiter coaching prompt delivered.
  Human secondary review assigned.
  Decision proceeds but is marked as reviewed_required.

ACTION 3 — BLOCK + MANDATORY REVIEW (CRITICAL)
  Bias signal recorded.
  Decision BLOCKED — cannot proceed without explicit override.
  Override requires:
    - HR Admin approval (minimum)
    - Written justification (logged to audit)
    - Governance Board notification (if pattern repeat)
  Candidate notified of processing delay (not of bias detection).
  Block logged as BiasEvent with override_record if overridden.

ACTION 4 — SYSTEM HALT (EXTREME)
  Reserved for: systemic bias detected platform-wide (structural bias audit)
  Triggers: hiring feature temporary suspension pending engineering review
  Requires: CTO + Legal + Governance Board joint sign-off to lift
  Last resort only — documented case history required
```

---

# 🔒 SECTION HBD-O — BIAS CORRECTION ENGINE

## Algorithmic Correction Methods (Locked)

```
CORRECTION METHOD 1 — Threshold Adjustment
  Applied to: AI model output bias (Equalized Odds violation)
  Method: Adjust decision threshold per cohort to equalize TPR/FPR
  Constraint: Cannot be applied if it reduces overall accuracy > 3%
  Logging: All threshold adjustments logged with magnitude + rationale

CORRECTION METHOD 2 — Reweighting
  Applied to: Training data imbalance causing AI bias
  Method: Reweight underrepresented cohort samples in model training
  Trigger: Weekly scheduled audit + post-detection trigger
  Constraint: Reweighted model must pass full bias audit before deployment

CORRECTION METHOD 3 — Calibration
  Applied to: AI confidence score miscalibration by cohort
  Method: Platt scaling per cohort
  Trigger: Calibration deviation > 0.05 detected in audit

CORRECTION METHOD 4 — Language Rewriting (NLP Assisted)
  Applied to: JD, rubric, feedback language bias
  Method: Replacement suggestions delivered to author with explanation
  Constraint: Author must accept or override (no silent replacement)
  Override: Logged to audit trail with author ID + timestamp

CORRECTION METHOD 5 — Recruiter Shadowing
  Applied to: Recruiter BVS HIGH RISK or CRITICAL
  Method: All decisions reviewed by HR Admin in parallel for 30 days
  Output: Shadow review agreement rate logged
  Threshold: Shadow review agreement < 0.80 → escalation
```

## BiasCorrection Schema (Locked)

```
correction_id           UUID (PK)
bias_event_id           UUID
correction_method       ENUM (THRESHOLD / REWEIGHT / CALIBRATION / LANGUAGE / SHADOW)
applied_by              STRING (SYSTEM / HR_ADMIN / ENGINEER)
correction_detail       JSONB (method-specific parameters)
pre_correction_metric   FLOAT
post_correction_metric  FLOAT
correction_effective    BOOLEAN
applied_at              TIMESTAMP
immutable_hash          STRING (SHA-256)
```

---

# 🔒 SECTION HBD-P — IMMUTABLE BIAS AUDIT ENGINE

## Audit Chain Architecture

Every BiasEvent produces a record that is:

1. Written to the **BiasAuditTrail** table (TimescaleDB, append-only partition)
2. SHA-256 hashed (input payload + timestamp + previous record hash = chain)
3. Verified on every read
4. Exported to cold storage daily

```
BiasAuditTrail Schema (Locked):

  audit_id               UUID (PK)
  event_type             STRING (bias category + action taken)
  entity_type            ENUM (CANDIDATE / RECRUITER / JOB / MODEL / PLATFORM)
  entity_id              UUID
  tenant_id              UUID
  bias_category          ENUM (DI / NLP / AI / BEHAVIORAL / STRUCTURAL / AFFINITY / CONFIRMATION)
  severity_level         ENUM (PASS / ADVISORY / WARNING / CRITICAL)
  enforcement_action     ENUM (LOG / FLAG / BLOCK / HALT)
  override_applied       BOOLEAN
  override_by            UUID (nullable)
  override_justification TEXT (nullable)
  source_signal_ids      ARRAY (UUIDs of contributing BiasEvents)
  payload_hash           STRING (SHA-256 of event payload)
  chain_hash             STRING (SHA-256 of payload_hash + previous chain_hash)
  recorded_at            TIMESTAMP (immutable, server-side)

CHAIN INTEGRITY RULE:
  On every read → verify chain_hash against recomputed value
  Mismatch → STOP → CRITICAL ALERT → REPORT AUDIT INTEGRITY BREACH
  Chain breach cannot be self-healed — requires security team investigation
```

---

# 🔒 SECTION HBD-Q — BIAS INTELLIGENCE REPORTING ENGINE

## Mandatory Dashboards (Locked)

```
DASHBOARD 1 — Real-Time Bias Monitor
  Active bias events (last 60 minutes)
  Severity distribution (PASS / ADVISORY / WARNING / CRITICAL)
  Enforcement action counts
  Trending bias categories
  Alert feed

DASHBOARD 2 — Disparate Impact Tracker
  AIR by role, department, recruiter, time window
  Protected cohort outcome funnel (application → hire)
  DI trend lines (30d / 90d / 365d)
  Threshold breach history

DASHBOARD 3 — Recruiter Bias Leaderboard
  BiasVelocityScore per recruiter (anonymized for team view)
  Training completion status
  Hiring authority status
  Improvement trajectory (trailing 90d)

DASHBOARD 4 — AI Model Fairness Monitor
  Equalized odds per model (live)
  SHAP feature drift (proxy feature emergence alert)
  Calibration curves by cohort
  Model audit history

DASHBOARD 5 — Language Bias Scan Results
  Scan volume (docs/day)
  Block rate (% docs blocked at publish gate)
  Top bias phrases detected (platform-wide, anonymized)
  Author correction acceptance rate

DASHBOARD 6 — Structural Bias Map
  Platform funnel representation by cohort
  Search/recommendation disparity heatmap
  Gini coefficient trend (opportunity distribution)
  Remediation plan status tracker

DASHBOARD 7 — Compliance & Regulatory Export
  EEOC-format adverse impact report (US)
  GDPR processing basis audit log (EU)
  Custom export per jurisdiction
  Audit chain integrity status
```

## Alert Rules (Locked)

```
DI CRITICAL breach detected → IMMEDIATE ALERT → HR Admin + Legal
AI model CRITICAL bias detected → IMMEDIATE ALERT → ML Engineer + CTO
NLP scan block rate > 20% → DAILY SUMMARY ALERT → Content Team
Recruiter BVS CRITICAL → IMMEDIATE ALERT → HR Admin + Governance Board
Audit chain hash mismatch → CRITICAL ALERT → Security Team → STOP
Platform structural bias CRITICAL → ALERT → CTO + Product + Legal
Override applied to CRITICAL block → IMMEDIATE ALERT → Governance Board
```

---

# 🔒 SECTION HBD-R — GOVERNANCE & APPEALS ENGINE

## Candidate Appeals Process

```
Trigger: Candidate files bias appeal via platform (hire/rejection dispute)

Step 1: Appeal record created (AppealRecord entity)
Step 2: BiasAuditTrail queried for all events related to candidate + role
Step 3: Counterfactual analysis re-run for the specific decision
Step 4: Disparate impact check for the specific role + recruiter
Step 5: Appeals Officer assigned (role: appeals_officer)
Step 6: Decision package assembled (audit trail + fairness metrics + replay)
Step 7: Appeals Officer makes determination within SLA (10 business days)
Step 8: Determination logged to AppealRecord (immutable)
Step 9: Candidate notified of outcome (not of specific bias detection)
Step 10: If upheld → remediation action applied to recruiter + role
```

## Governance Board Reporting

```
Monthly mandatory report to Governance Board:
  - Total bias events by category and severity
  - Enforcement action breakdown
  - Override usage (who, why, outcome)
  - Recruiter bias velocity trend
  - AI model fairness trend
  - Platform structural bias trend
  - Open appeals + resolution rate
  - Regulatory compliance status

Governance Board authorities:
  - Approve recruiter reinstatement after CRITICAL suspension
  - Mandate platform engineering changes for structural bias
  - Approve exceptions to global threshold minimums (rare, documented)
  - Commission external fairness audit (annual)
```

## AppealRecord Schema (Locked)

```
appeal_id               UUID (PK)
candidate_id            UUID
job_id                  UUID
recruiter_id            UUID
tenant_id               UUID
appeal_reason           TEXT
appeal_filed_at         TIMESTAMP
bias_audit_trail_refs   ARRAY (UUIDs)
counterfactual_result_id UUID
di_record_id            UUID
appeals_officer_id      UUID
determination           ENUM (UPHELD / DISMISSED / INCONCLUSIVE)
determination_rationale TEXT
determination_at        TIMESTAMP
remediation_applied     BOOLEAN
remediation_detail      JSONB
immutable_hash          STRING (SHA-256)
```

---

# 🔒 SECTION HBD-S — INTEGRATION WIRING LOCK

## Mandatory Cross-System Integration

```
HBD ↔ ECOSKILLER Event Bus (Kafka):
  Subscribe: All hiring + scoring + AI output events
  Publish:   Bias enforcement actions, alert events

HBD ↔ DTSE (Digital Twin Simulation Engine):
  TwinProfile data → protected attribute proxy inference
  Counterfactual engine → DTSE Hiring Simulation enrichment
  Trust Scoring Engine → bias history as trust score input (BCS dimension)

HBD ↔ Dojo SaaS:
  Rubric submissions → NLP bias scan before activation
  Mentor evaluation notes → NLP bias scan before candidate view
  Scenario difficulty disparity → structural bias signal input

HBD ↔ Integration Hub (EIE — 100 Tool System):
  Work tool skill data → protected proxy correlation check
  Integration availability gap by geography → structural bias signal

HBD ↔ Talent Marketplace:
  JD publish → NLP bias scan gate (must pass before listing)
  Candidate search ranking → AI model bias audit (weekly)
  Recruiter hiring decisions → behavioral bias tracker input

HBD ↔ Billing Engine:
  Bias audit report generation metered (enterprise plan)
  Regulatory export metered (compliance tier add-on)

HBD ↔ Notification System:
  ADVISORY → in-app notification (recruiter + admin)
  WARNING → email + in-app + push (recruiter + admin)
  CRITICAL → email + in-app + push + Slack webhook (admin + legal)
  EXTREME → all channels + escalation tree

HBD ↔ Observability Stack:
  All bias events → structured logs (Loki)
  Bias detection latency → Prometheus metrics
  Enforcement action rate → Grafana dashboard
  Model fairness metrics → Grafana model panel
```

**No manual sync permitted. Event-driven only.**

---

# 🔒 SECTION HBD-T — SECURITY LOCK

```
Data Access Controls:
  BiasProfile: recruiter_self_read, hr_admin_read, governance_board_read
  BiasAuditTrail: hr_admin_read, compliance_officer_read, governance_board_read
  DisparateImpactRecord: hr_admin_read, compliance_officer_read
  NLPBiasScan: author_read (own docs only), hr_admin_read
  AIModelBiasAudit: ml_engineer_read, cto_read, governance_board_read
  AppealRecord: candidate_own_read (outcome only), appeals_officer_read, hr_admin_read

Write Controls:
  BiasEvent: SYSTEM ONLY (no human write)
  BiasAuditTrail: SYSTEM ONLY, append-only (no human write, no update, no delete)
  BiasCorrection: SYSTEM + authorized_engineer (with audit log)
  Override: hr_admin_write (with mandatory justification field)

Encryption:
  All BiasAuditTrail records encrypted at rest (AES-256)
  BiasProfile data encrypted at rest
  Protected proxy data: ephemeral only, destroyed after cohort bucketing
  Regulatory export files: encrypted in transit (TLS 1.3) + at rest

Anonymization:
  Cross-recruiter bias reports: anonymized recruiter IDs
  Cross-tenant bias benchmarks: fully anonymized, aggregated only
  Regulatory submissions: per jurisdiction anonymization rules applied
```

---

# 🔒 SECTION HBD-U — TEST & QA LOCK

```
Bias Detection Accuracy Tests:
  DI Calculator: validated against EEOC synthetic datasets
  NLP Scanner: precision ≥ 0.85, recall ≥ 0.80 on bias benchmark
  AI Auditor: validated against AIF360 benchmark datasets
  Counterfactual Engine: validated against DoWhy test suite

Enforcement Tests:
  CRITICAL block: verified blocks decision before DB write
  WARNING flag: verified flags appear in recruiter UI within 2s
  Override: verified override requires justification field
  Override audit: verified override logged to immutable chain

Audit Chain Tests:
  SHA-256 hash generation: verified on every write
  Chain verification: verified on every read
  Mismatch detection: verified triggers CRITICAL alert
  Append-only: verified no update/delete SQL possible

Integration Tests:
  Kafka consumer: all 18 subscribed topics receive and process
  DTSE twin lookup: latency < 200ms p95
  Notification delivery: all severity levels trigger correct channels

Load Tests:
  10,000 bias signal events/minute ingestion
  1,000 concurrent DI calculations
  100 concurrent NLP scans
  Audit chain write latency < 50ms p95

Regulatory Compliance Tests:
  EEOC adverse impact report format validated
  GDPR processing log format validated
  Data residency enforcement verified per tenant region
```

**No production deploy without passing all test gates.**

---

# 🔒 SECTION HBD-V — DEVOPS & DEPLOYMENT LOCK

```
HBD Kubernetes Namespaces:
  ecoskiller-hbd-dev
  ecoskiller-hbd-test
  ecoskiller-hbd-staging
  ecoskiller-hbd-prod

HBD Microservices (Containerized — Each Isolated):
  hbd-signal-collector        (Kafka consumer, signal enrichment)
  hbd-di-calculator           (Disparate impact computation)
  hbd-nlp-scanner             (Language bias detection)
  hbd-ai-auditor              (Model fairness audit)
  hbd-behavioral-tracker      (Recruiter bias profiling)
  hbd-counterfactual          (DoWhy causal inference)
  hbd-threshold-enforcement   (Block/flag decision engine)
  hbd-correction-engine       (Algorithmic correction)
  hbd-audit-sealer            (Immutable audit chain writer)
  hbd-reporting               (Dashboard data + regulatory export)
  hbd-appeals                 (Appeals workflow engine)
  hbd-inference-api           (NLP + fairness model inference)

Autoscaling:
  hbd-signal-collector:   HPA min=3, max=30 (Kafka lag > 1000 messages)
  hbd-di-calculator:      HPA min=2, max=20 (CPU > 70%)
  hbd-nlp-scanner:        HPA min=2, max=20 (queue depth > 100)
  hbd-inference-api:      HPA min=3, max=30 (latency > 300ms)
  hbd-audit-sealer:       HPA min=2, max=10 (write rate > 500/s)
  All others:             HPA min=2, max=10

Model Versioning (Locked):
  NLP bias classifier: versioned in MinIO model registry
  Fairness metric models: versioned in MinIO model registry
  Rollback: previous 3 versions available
  No silent model updates — versioned deployment pipeline only
  Post-deployment: mandatory bias benchmark re-run before traffic promotion
```

---

# 🔒 SECTION HBD-W — MULTI-TENANT ARCHITECTURE LOCK

```
BiasThresholdPolicy scoped per tenant.
Tenants may TIGHTEN thresholds — may NOT relax below global minimums.
Tenant bias reports: isolated to tenant_id at row level.
Cross-tenant bias benchmarks: aggregated + anonymized only.
Enterprise tenant features:
  - Full recruiter bias profiling (BiasProfile)
  - Regulatory export API
  - Governance Board reporting package
  - Custom threshold configuration
  - Shadow review workflow
  - External fairness auditor API access
Starter / Professional tenants:
  - Core DI tracking
  - NLP scan at publish gate
  - AI model audit (platform-wide, shared result)
  - Advisory + Warning notifications
```

---

# 🔒 SECTION HBD-X — BILLING & PLAN TIERING LOCK

```
STARTER PLAN:
  NLP Bias Scan on JD publish:         INCLUDED (5 JDs/month)
  Disparate Impact tracking:            Platform-level only (not per-recruiter)
  AI model bias audit:                  Shared platform audit (no tenant-specific)
  Bias alerts (ADVISORY + WARNING):     INCLUDED

PROFESSIONAL PLAN:
  All Starter features
  NLP Bias Scan:                        Unlimited JDs + rubrics
  Per-recruiter DI tracking:            INCLUDED (up to 10 recruiters)
  Recruiter BiasProfile:                INCLUDED (basic)
  CRITICAL enforcement actions:         INCLUDED
  Bias intelligence dashboard:          INCLUDED

ENTERPRISE PLAN:
  All Professional features
  Full recruiter bias profiling:        Unlimited recruiters
  Counterfactual Fairness Engine:       INCLUDED
  AI model bias audit (tenant-specific): INCLUDED
  Structural bias analysis:             INCLUDED
  Regulatory export API:                INCLUDED (EEOC / GDPR formats)
  Governance Board reporting package:   INCLUDED
  Shadow review workflow:               INCLUDED
  Custom threshold configuration:       INCLUDED
  External auditor API access:          INCLUDED
  SLA: CRITICAL alert delivery < 60s
  SLA: Regulatory export generation < 4 hours
```

---

# 🔒 SECTION HBD-Y — CHANGE GOVERNANCE LOCK

## Allowed Without Version Bump

```
Add new bias language phrase to NLP detection dictionary
Add new protected attribute proxy signal (with privacy review)
Add new dashboard widget to reporting layer
Add new notification channel
Add new regulatory export format
Tune existing model (within bias benchmark thresholds)
Add new coaching content to recruiter training module
```

## Requires Version Bump

```
Change DI calculation formula or severity thresholds
Change AI model fairness metric definitions
Change audit chain hash algorithm
Change enforcement action definitions
Change BiasThresholdPolicy global minimums
Change entity schema (rename or remove fields)
Change protected attribute proxy model architecture
Change appeals process SLA or workflow
```

## Forbidden Under Any Circumstance

```
Delete or mutate BiasAuditTrail records
Allow tenant to disable CRITICAL enforcement blocks
Allow AI to override human appeal determination
Lower global DI threshold minimums below EEOC floor
Remove NLP scan publish gate
Allow cross-tenant access to bias profiles
Remove chain hash integrity verification
Grant candidates access to bias detection details (privacy + legal risk)
```

---

# 🔒 SECTION HBD-Z — MASTER PROMPT SEAL BLOCK

```
═══════════════════════════════════════════════════════════════════════════
ECOSKILLER HIRING BIAS DETECTOR (HBD) — ANTIGRAVITY TIER
ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
PRODUCTION SYSTEM BLUEPRINT v1.0
═══════════════════════════════════════════════════════════════════════════

SEALED STATUS:            FINAL · LOCKED · GOVERNED · DETERMINISTIC
INTERPRETATION AUTH:      NONE
MUTATION POLICY:          ADD-ONLY VIA VERSION BUMP
EXECUTION MODE:           DETERMINISTIC
FAILURE MODE:             STOP → REPORT → NO PARTIAL OUTPUT

BIAS TAXONOMY LOCKED (7 CATEGORIES):
  ✔ Disparate Impact Bias (Statistical — 4/5ths Rule + Chi-square)
  ✔ Recruiter Behavioral Bias (BVS scoring, 4-tier severity)
  ✔ AI Model Output Bias (Equalized Odds, Demographic Parity, SHAP audit)
  ✔ Language Bias (NLP — spaCy + fine-tuned classifier, 6 categories)
  ✔ Structural Platform Bias (Gini, funnel analysis, cohort representation)
  ✔ Affinity Bias (Graph proximity, Neo4j analysis)
  ✔ Confirmation Bias (Sequential scoring correlation, Bayesian audit)

ENGINES LOCKED (12 ENGINES):
  ✔ Bias Signal Collector Engine (18 Kafka topic subscriptions)
  ✔ Disparate Impact Calculator (AIR + DIS + CI + Cohen's h)
  ✔ NLP Bias Language Scanner (6 category taxonomy, publish gate)
  ✔ AI Model Bias Auditor (all ECOSKILLER models, continuous + scheduled)
  ✔ Recruiter Behavioral Bias Tracker (BVS, coaching, suspension)
  ✔ Counterfactual Fairness Engine (DoWhy, CF_effect threshold)
  ✔ Bias Threshold Enforcement Engine (LOG / FLAG / BLOCK / HALT)
  ✔ Bias Correction Engine (5 methods: threshold/reweight/calibration/language/shadow)
  ✔ Immutable Bias Audit Engine (SHA-256 chain, append-only, encrypted)
  ✔ Bias Intelligence Reporting Engine (7 dashboards, 8 alert rules)
  ✔ Governance & Appeals Engine (10-step process, SLA-bound)
  ✔ Structural Bias Detector (Gini, funnel, platform-wide)

ENFORCEMENT LOCKED:
  ✔ CRITICAL block: Hiring decision blocked until HR Admin override
  ✔ Override: Mandatory justification, logged to immutable audit
  ✔ EXTREME halt: Requires CTO + Legal + Governance Board sign-off
  ✔ AI final decision: FORBIDDEN — Human authority preserved at all points
  ✔ Recruiter suspension: Automatic at BVS > 0.80 — Human reinstatement required

TRUST INFRASTRUCTURE LOCKED:
  ✔ Immutable BiasAuditTrail: SHA-256 chained, append-only, encrypted at rest
  ✔ Chain integrity: Verified on every read — mismatch → CRITICAL ALERT → STOP
  ✔ Protected data: Individual proxy attributes ephemeral only, cohort aggregation only
  ✔ Candidate privacy: Bias detection details not exposed to candidates
  ✔ Consent: Processing basis logged to GDPR audit trail

ANTIGRAVITY LOCKED:
  ✔ Bias as resistance vector: Quantified, attributed, corrected
  ✔ Counterfactual uplift: Protected attribute swap simulation for every CRITICAL decision
  ✔ Structural remediation: Platform design changes mandated for systemic bias
  ✔ Recruiter trajectory correction: Coaching → Training → Shadow → Suspension pathway

INTEGRATION LOCKED:
  ✔ DTSE ↔ HBD: Twin data enriches bias detection; bias history feeds Trust Score
  ✔ Dojo ↔ HBD: Rubric + mentor notes scanned; scenario disparity monitored
  ✔ EIE ↔ HBD: Tool availability gap monitored as structural bias signal
  ✔ Talent Marketplace ↔ HBD: JD publish gate + ranking bias audit
  ✔ Notification System: All 4 severity levels → mapped channel delivery

COMPLIANCE LOCKED:
  ✔ EEOC Adverse Impact Report: INCLUDED (Enterprise)
  ✔ GDPR Processing Log: INCLUDED (all tiers)
  ✔ Data Residency: Per tenant region
  ✔ Regulatory Export API: Enterprise tier
  ✔ External Auditor Access: Enterprise tier
  ✔ Annual fairness audit: Governance Board mandate

DEVOPS LOCKED:
  ✔ 12 isolated microservices (Kubernetes)
  ✔ 4 environments: dev / test / staging / prod
  ✔ Model versioning: MinIO registry, rollback 3 versions
  ✔ Autoscaling: Per service, trigger-based
  ✔ Test gates: Accuracy, enforcement, audit chain, integration, load, compliance

Architecture Interpretation Authority:      NONE
Architecture Mutation Authority:            LOCKED
Enforcement Override Authority:             HR ADMIN + WRITTEN JUSTIFICATION ONLY
Audit Record Deletion Authority:            NONE — FOREVER FORBIDDEN
AI Hiring Decision Authority:               NONE — HUMANS DECIDE

END ECOSKILLER HIRING BIAS DETECTOR SEAL — ANTIGRAVITY TIER v1.0
═══════════════════════════════════════════════════════════════════════════
```

---

*Document Class: Production System Blueprint*
*Sealed Under: ECOSKILLER HBD v1.0 — ANTIGRAVITY TIER*
*Parent System: ECOSKILLER Master Execution Prompt v12.0*
*Sibling Module: ECOSKILLER DTSE v1.0 — ANTIGRAVITY TIER*
*Dojo Integration: DOJO SAAS PRODUCTION MODE v1.0*
*Talent OS Integration: EIE 100-Tool Hub + EUME Migration Engine*
*Mutation: Add-Only via Version Bump*
*Interpretation: NONE PERMITTED*
