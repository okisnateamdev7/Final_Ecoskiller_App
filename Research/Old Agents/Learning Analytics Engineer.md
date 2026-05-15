# Learning Analytics Engineer — Complete Gap Register

Status: ADD-ONLY · AUDIT-READY · GOVERNANCE PATCH SET
Scope: Ecoskiller + Dojo — Learning Analytics, Metrics, Insights, ML Analytics

This document consolidates **ALL IDENTIFIED LEARNING ANALYTICS GAPS (1–25)** discovered across all hard rechecks.
Each gap is **designable, enforceable, auditable**, and strictly scoped to the Learning Analytics Engineer Agent.

---

## GAP 1 — SKILL ONTOLOGY VERSION BINDING

**Problem**  
Learning analytics not bound to skill definition versions.

**Mandatory Rule**  
- Every learning metric must reference `skill_version_id`
- Ontology updates trigger re-computation or legacy labeling

---

## GAP 2 — PRACTICE vs ASSESSMENT ANALYTICS CONTAMINATION

**Problem**  
Practice data inflates mastery analytics.

**Mandatory Rule**  
- Analytics must declare signal source
- Mastery analytics may consume assessment-only data

---

## GAP 3 — GAMIFICATION SIGNAL BLEED

**Problem**  
Badges, XP, streaks bias learning analytics.

**Mandatory Rule**  
- Gamification signals excluded from learning analytics

---

## GAP 4 — COMMUNITY SIGNAL BLEED

**Problem**  
Social activity mistaken for learning progress.

**Mandatory Rule**  
- Community metrics excluded from learning progress analytics

---

## GAP 5 — ANALYTICS CONFIDENCE CALIBRATION

**Problem**  
Confidence scores unvalidated.

**Mandatory Rule**  
- Empirical calibration required
- Confidence decay over time enforced

---

## GAP 6 — TEMPORAL SEMANTICS & TIMEZONES

**Problem**  
Ambiguous time windows.

**Mandatory Rule**  
- `event_time` in UTC mandatory
- `learner_timezone` stored separately

---

## GAP 7 — BACKFILLED / LATE DATA DISCLOSURE

**Problem**  
Retroactive metric changes undisclosed.

**Mandatory Rule**  
- Retroactive analytics changes logged and disclosed

---

## GAP 8 — COHORT DEFINITION DRIFT

**Problem**  
Changing cohort logic breaks comparisons.

**Mandatory Rule**  
- Cohorts versioned
- Cross-version comparison forbidden

---

## GAP 9 — SMALL COHORT PRIVACY

**Problem**  
Risk of re-identification.

**Mandatory Rule**  
- Minimum cohort size enforced
- Suppression or noise injection mandatory

---

## GAP 10 — ACCESSIBILITY BIAS IN ANALYTICS

**Problem**  
Accessibility accommodations skew analytics.

**Mandatory Rule**  
- Accommodations must be analytics-neutral

---

## GAP 11 — ANALYTICS ↔ BILLING MISUSE

**Problem**  
Analytics used to gate plans or access.

**Mandatory Rule**  
- Analytics forbidden from influencing billing or entitlements

---

## GAP 12 — ANALYTICS ↔ CERTIFICATION DECISIONS

**Problem**  
Analytics replace formal assessment.

**Mandatory Rule**  
- Analytics advisory only
- Certification requires assessment evidence

---

## GAP 13 — ML DRIFT & STALENESS VISIBILITY

**Problem**  
Outdated models produce insights silently.

**Mandatory Rule**  
- Drift thresholds enforced
- User-visible “insight paused” state

---

## GAP 14 — INTERPRETATION HUMILITY

**Problem**  
Over-trust in analytics.

**Mandatory Rule**  
- “Does not imply…” disclaimers mandatory

---

## GAP 15 — ANALYTICS EXIT & PORTABILITY

**Problem**  
User analytics lost on exit.

**Mandatory Rule**  
- Personal analytics exportable
- Aggregates anonymized on retention

---

## GAP 16 — DATA FINALITY DEPENDENCY

**Problem**  
Analytics consume unstable data.

**Mandatory Rule**  
- Data state declared: provisional | finalized
- Provisional insights visually marked

---

## GAP 17 — FEATURE FRESHNESS

**Problem**  
Stale features drive insights.

**Mandatory Rule**  
- Feature TTL enforced
- Insights suppressed on expiry

---

## GAP 18 — MULTI-TENANT COMPARABILITY

**Problem**  
Cross-tenant benchmarks mislead.

**Mandatory Rule**  
- Analytics tenant-scoped
- Cross-tenant benchmarks forbidden unless anonymized & approved

---

## GAP 19 — INSTRUCTOR / MENTOR MISUSE

**Problem**  
Analytics used to pressure learners.

**Mandatory Rule**  
- Instructor analytics read-only
- Forbidden as grading justification

---

## GAP 20 — ALERT FATIGUE

**Problem**  
Excessive analytics alerts.

**Mandatory Rule**  
- Alert frequency caps
- Confidence-gated alerts only

---

## GAP 21 — ANALYTICS ACCESS PRIVACY

**Problem**  
Silent analytics surveillance.

**Mandatory Rule**  
- Every analytics view logged
- User-visible access history

---

## GAP 22 — REGULATORY REPORT FREEZE

**Problem**  
Inconsistent compliance reports.

**Mandatory Rule**  
- Compliance analytics from frozen datasets
- Versioned snapshots mandatory

---

## GAP 23 — ANALYTICS INCIDENT MODE

**Problem**  
Misleading analytics during outages.

**Mandatory Rule**  
- Insight generation paused
- Read-only analytics with incident banner

---

## GAP 24 — ANALYTICS COST GOVERNANCE

**Problem**  
Runaway analytics compute costs.

**Mandatory Rule**  
- Cost attribution per analytic job
- Budget thresholds enforced

---

## GAP 25 — SYSTEM HUMILITY & PREDICTION LIMITS

**Problem**  
Predictions treated as guarantees.

**Mandatory Rule**  
- Accuracy ranges displayed
- “Prediction, not guarantee” labels mandatory

---

## TERMINAL STATE

All gaps are:
- Add-only
- Enforceable
- Auditable
- Non-duplicative

No further Learning Analytics design gaps exist.

END OF FILE

