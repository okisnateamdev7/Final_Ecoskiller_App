# Assessment Data Scientist — Complete Gap Register

Status: ADD-ONLY · AUDIT-READY · GOVERNANCE PATCH SET
Scope: Ecoskiller + Dojo — Assessments, Psychometrics, Scoring, ML Evaluation

This document consolidates **ALL IDENTIFIED ASSESSMENT GAPS (1–25)** discovered across all hard rechecks.
Each gap is **designable, enforceable, auditable**, and strictly scoped to the Assessment Data Scientist Agent.

---

## GAP 1 — SKILL ONTOLOGY VERSION BINDING

**Problem**  
Assessment scores are not bound to evolving skill definitions.

**Mandatory Rule**  
- Every assessment score and item parameter must reference `skill_version_id`
- Ontology updates trigger recalibration or legacy marking

---

## GAP 2 — LEARNING ANALYTICS CONTAMINATION

**Problem**  
Engagement or learning signals may influence assessment scoring.

**Mandatory Rule**  
- Learning analytics forbidden in scoring or calibration

---

## GAP 3 — GAMIFICATION CONTAMINATION

**Problem**  
XP, badges, streaks bias assessment outcomes.

**Mandatory Rule**  
- Gamification signals excluded from all assessment analytics

---

## GAP 4 — TEMPORAL SEMANTICS & TIMEZONES

**Problem**  
Ambiguity in timed assessments across timezones.

**Mandatory Rule**  
- `event_time` in UTC mandatory
- `learner_timezone` stored separately

---

## GAP 5 — ACCESSIBILITY–PSYCHOMETRIC NEUTRALITY

**Problem**  
Accessibility accommodations bias item parameters.

**Mandatory Rule**  
- Accommodation effects isolated from calibration
- Score neutrality enforced

---

## GAP 6 — SMALL SAMPLE PRIVACY

**Problem**  
Item analytics on small cohorts risk re-identification.

**Mandatory Rule**  
- Minimum sample thresholds enforced
- Suppression or noise injection mandatory

---

## GAP 7 — ITEM DRIFT RESPONSE

**Problem**  
Detected item drift lacks action rules.

**Mandatory Rule**  
- Drift thresholds defined
- Automatic quarantine pending review

---

## GAP 8 — CUT SCORE GOVERNANCE

**Problem**  
Pass/fail thresholds change without formal control.

**Mandatory Rule**  
- Multi-review approval required
- Impact analysis mandatory

---

## GAP 9 — ADAPTIVE TESTING TRANSPARENCY

**Problem**  
Users do not understand adaptive behavior.

**Mandatory Rule**  
- Plain-language explanation of adaptivity required

---

## GAP 10 — CHEATING RESPONSE GOVERNANCE

**Problem**  
Automated cheating flags trigger unfair actions.

**Mandatory Rule**  
- Human review mandatory
- Appeal workflow enforced

---

## GAP 11 — SCORE CONFIDENCE MISALIGNMENT

**Problem**  
Low-confidence scores shown as definitive.

**Mandatory Rule**  
- Confidence intervals displayed
- Suppress or flag low-confidence scores

---

## GAP 12 — ASSESSMENT–CERTIFICATION BOUNDARY

**Problem**  
Assessments auto-trigger certification.

**Mandatory Rule**  
- Certification requires policy review
- Assessment advisory only

---

## GAP 13 — RETAKE & ATTEMPT GOVERNANCE

**Problem**  
Multiple attempts enable score gaming.

**Mandatory Rule**  
- Attempt limits defined
- Aggregation rules explicit

---

## GAP 14 — ASSESSMENT INCIDENT MODE

**Problem**  
System outages corrupt assessments.

**Mandatory Rule**  
- Session freeze rules
- Resume or invalidate logic defined

---

## GAP 15 — EXIT & DATA PORTABILITY

**Problem**  
Users cannot export assessment history.

**Mandatory Rule**  
- Personal results exportable
- Aggregate retention anonymized

---

## GAP 16 — DATA FINALITY DEPENDENCY

**Problem**  
Scores change silently after issuance.

**Mandatory Rule**  
- Scores declare `provisional | finalized`
- Post-final changes require reissue

---

## GAP 17 — FEATURE FRESHNESS

**Problem**  
Stale features influence scoring.

**Mandatory Rule**  
- Feature TTL enforced
- Scoring blocked on expired inputs

---

## GAP 18 — TENANT ITEM ISOLATION

**Problem**  
Item parameters leak across tenants.

**Mandatory Rule**  
- Item analytics tenant-scoped by default
- Cross-tenant calibration forbidden

---

## GAP 19 — SMALL-N ADAPTIVE INSTABILITY

**Problem**  
Adaptive testing unstable with low data.

**Mandatory Rule**  
- Minimum N for adaptivity
- Static fallback below threshold

---

## GAP 20 — PSYCHOMETRIC MODEL PRIORITY

**Problem**  
Multiple models yield conflicting scores.

**Mandatory Rule**  
- Single authoritative model per assessment version

---

## GAP 21 — ALERT FATIGUE

**Problem**  
Excessive psychometric alerts.

**Mandatory Rule**  
- Alert throttling
- Severity-gated alerts only

---

## GAP 22 — ASSESSMENT ACCESS AUDIT

**Problem**  
Untracked access to assessment data.

**Mandatory Rule**  
- Every access logged
- User-visible access history

---

## GAP 23 — REGULATORY REPORT FREEZE

**Problem**  
Inconsistent compliance reporting.

**Mandatory Rule**  
- Reports generated from frozen datasets
- Versioned snapshots mandatory

---

## GAP 24 — COST & COMPUTE GOVERNANCE

**Problem**  
Runaway assessment compute costs.

**Mandatory Rule**  
- Cost attribution per assessment
- Budget thresholds enforced

---

## GAP 25 — ASSESSMENT HUMILITY & LIMIT DISCLOSURE

**Problem**  
Scores treated as guarantees.

**Mandatory Rule**  
- Error margins displayed
- “Measurement, not guarantee” labels mandatory

---

## TERMINAL STATE

All gaps are:
- Add-only
- Enforceable
- Auditable
- Non-duplicative

No further Assessment Data Scientist design gaps exist.

END OF FILE

