# Data Engineer — Skill & Usage Pipelines Agent
## COMPLETE GAP REGISTER (ADD-ONLY · AUDIT-READY)

Scope: Ecoskiller + Dojo — Data Engineering, Skill Telemetry, Usage Analytics, ML Pipelines
Status: GOVERNANCE PATCH SET

This file consolidates **ALL IDENTIFIED GAPS (1–25)** discovered across every hard recheck.
Each gap is **designable, enforceable, auditable**, and strictly scoped to the Data Engineer agent.

---

## GAP 1 — SKILL ONTOLOGY VERSION DRIFT

**Problem**
Skill usage events are not bound to ontology versions.

**Mandatory Rule**
- Every skill event must include `skill_version_id`
- Ontology changes trigger reprocessing or legacy tagging

---

## GAP 2 — PRACTICE vs ASSESSMENT SIGNAL CONTAMINATION

**Problem**
Practice signals may inflate mastery metrics.

**Mandatory Rule**
- All events must declare intent: `practice | assessment | observation`
- ML features must never mix intents

---

## GAP 3 — REALTIME EVENT LOSS & REPLAY

**Problem**
Dropped realtime events corrupt analytics.

**Mandatory Rule**
- Durable event log mandatory
- Deterministic replay pipelines required

---

## GAP 4 — DATA FINALITY & CONSISTENCY WINDOW

**Problem**
Users see conflicting metrics.

**Mandatory Rule**
- Metrics must declare `provisional | finalized`
- Consistency window documented

---

## GAP 5 — ML LABEL LEAKAGE

**Problem**
Future labels leak into training data.

**Mandatory Rule**
- Point-in-time joins enforced
- Automated leakage tests mandatory

---

## GAP 6 — GLOBAL EVENT DUPLICATION

**Problem**
Duplicate events inflate metrics.

**Mandatory Rule**
- Globally unique `event_id`
- Deduplication at ingestion

---

## GAP 7 — DATA OWNERSHIP & SLA

**Problem**
No accountability for broken datasets.

**Mandatory Rule**
- Dataset owner declared
- SLA and escalation path mandatory

---

## GAP 8 — USAGE ↔ BILLING DATA COUPLING

**Problem**
Usage not linked to entitlements.

**Mandatory Rule**
- Usage events include entitlement snapshot
- Overuse logged, not dropped

---

## GAP 9 — CERTIFICATION EVIDENCE TRACEABILITY

**Problem**
Cannot reconstruct certification decisions.

**Mandatory Rule**
- Immutable evidence bundle per certification
- Event hashes referenced

---

## GAP 10 — COMMUNITY SIGNAL BLEED

**Problem**
Social activity misread as learning.

**Mandatory Rule**
- Community events excluded from skill metrics

---

## GAP 11 — FEATURE STALENESS

**Problem**
Expired features used in inference.

**Mandatory Rule**
- TTL defined per feature
- Expired features block inference

---

## GAP 12 — RETROACTIVE CONSENT ENFORCEMENT

**Problem**
Consent revocation not propagated.

**Mandatory Rule**
- Tombstone propagation
- Downstream purge jobs mandatory

---

## GAP 13 — DATA RESIDENCY & LOCALITY

**Problem**
Region laws not enforced.

**Mandatory Rule**
- Region tag mandatory
- Governed cross-region movement

---

## GAP 14 — ML EXPLANATION SNAPSHOT

**Problem**
ML outputs cannot be explained later.

**Mandatory Rule**
- Feature snapshot stored per inference

---

## GAP 15 — PIPELINE INCIDENT MODE

**Problem**
Pipelines corrupt data during incidents.

**Mandatory Rule**
- Write-freeze rules
- Post-incident reconciliation

---

## GAP 16 — TEMPORAL SEMANTICS

**Problem**
Timezone ambiguity in metrics.

**Mandatory Rule**
- `event_time` in UTC mandatory
- `user_timezone` stored separately

---

## GAP 17 — LATE & OUT-OF-ORDER EVENTS

**Problem**
Late events silently change metrics.

**Mandatory Rule**
- Watermark strategy
- Logged correction window

---

## GAP 18 — PRODUCER DATA CONTRACT ENFORCEMENT

**Problem**
Upstream services break schemas.

**Mandatory Rule**
- Producer-side validation mandatory
- Contract version pinned

---

## GAP 19 — SHADOW ANALYTICS

**Problem**
Multiple sources of truth emerge.

**Mandatory Rule**
- Governed views only
- Raw access restricted and audited

---

## GAP 20 — BACKFILL IMPACT GOVERNANCE

**Problem**
Backfills surprise downstream systems.

**Mandatory Rule**
- Impact declaration required
- Consumer notification mandatory

---

## GAP 21 — FEATURE DEPENDENCY GRAPH

**Problem**
Unknown blast radius of changes.

**Mandatory Rule**
- Feature lineage graph mandatory

---

## GAP 22 — ANALYTICS RBAC GRANULARITY

**Problem**
Overexposed analytical data.

**Mandatory Rule**
- Column-level RBAC
- Role-based dataset exposure

---

## GAP 23 — TRAIN–SERVE SKEW DETECTION

**Problem**
Model degradation unnoticed.

**Mandatory Rule**
- Distribution comparison jobs
- Skew alerts enforced

---

## GAP 24 — DATA COST GOVERNANCE

**Problem**
Runaway pipeline costs.

**Mandatory Rule**
- Cost attribution per pipeline
- Budget alerts mandatory

---

## GAP 25 — DATA INTERPRETATION HUMILITY

**Problem**
Metrics over-trusted.

**Mandatory Rule**
- Confidence intervals exposed
- “Does not imply…” disclosures

---

## TERMINAL STATE

All gaps are:
- Add-only
- Enforceable
- Auditable
- Non-duplicative

No further Data