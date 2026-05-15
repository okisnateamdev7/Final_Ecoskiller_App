# Recommendation System Engineer — Complete Gap Register

Status: ADD-ONLY · AUDIT-READY · GOVERNANCE PATCH SET
Scope: Ecoskiller + Dojo — Recommendation, Personalization, Ranking, Discovery

This document consolidates **ALL IDENTIFIED RECOMMENDATION SYSTEM GAPS (1–25)** discovered across all hard rechecks.
Each gap is **designable, enforceable, auditable**, and strictly scoped to the Recommendation System Engineer Agent.

---

## GAP 1 — SKILL ONTOLOGY VERSION COUPLING

**Problem**  
Recommendations are not bound to evolving skill definitions.

**Mandatory Rule**  
- Every recommendation must reference `skill_version_id`
- Ontology updates trigger recalculation or legacy labeling

---

## GAP 2 — PRACTICE vs ASSESSMENT SIGNAL PURITY

**Problem**  
Practice behavior inflates readiness recommendations.

**Mandatory Rule**  
- Recommendation features must declare signal intent
- Advancement recommendations require assessment-grade signals

---

## GAP 3 — GAMIFICATION FEEDBACK LOOP

**Problem**  
XP, streaks, ranks reinforce addictive loops.

**Mandatory Rule**  
- Gamification signals forbidden as recommendation inputs

---

## GAP 4 — COMMUNITY POPULARITY BLEED

**Problem**  
Likes, replies bias learning discovery.

**Mandatory Rule**  
- Community popularity metrics excluded from ranking models

---

## GAP 5 — TEMPORAL SEMANTICS

**Problem**  
Stale or mistimed recommendations.

**Mandatory Rule**  
- `event_time` in UTC mandatory
- Recency decay functions declared and versioned

---

## GAP 6 — FEEDBACK LOOP AMPLIFICATION

**Problem**  
Self-reinforcing ranking bias.

**Mandatory Rule**  
- Amplification thresholds defined
- Automatic dampening enforced

---

## GAP 7 — LONG-TAIL EXPOSURE FAIRNESS

**Problem**  
Low-volume skills starved of visibility.

**Mandatory Rule**  
- Minimum exposure quotas for low-volume skills

---

## GAP 8 — ACCESSIBILITY-AWARE FILTERING

**Problem**  
Users recommended inaccessible content.

**Mandatory Rule**  
- Recommendations filtered by user accessibility profile

---

## GAP 9 — CONFIDENCE CALIBRATION

**Problem**  
False certainty in recommendations.

**Mandatory Rule**  
- Empirical confidence calibration
- Confidence decay enforced

---

## GAP 10 — EXPLANATION FIDELITY

**Problem**  
Generic explanations mislead users.

**Mandatory Rule**  
- Explanations generated from actual feature contributions

---

## GAP 11 — COLD-START OVER-PERSONALIZATION

**Problem**  
Invasive inference for new users.

**Mandatory Rule**  
- Personalization ramp-up stages defined

---

## GAP 12 — MULTI-TENANT TRAINING LEAKAGE

**Problem**  
Cross-tenant data leakage.

**Mandatory Rule**  
- Tenant-isolated training by default
- Aggregated anonymized training only with approval

---

## GAP 13 — ML DRIFT & STALENESS VISIBILITY

**Problem**  
Outdated models produce recommendations silently.

**Mandatory Rule**  
- Drift thresholds enforced
- User-visible “recommendations paused” state

---

## GAP 14 — INCIDENT MODE BEHAVIOR

**Problem**  
Erratic recommendations during outages.

**Mandatory Rule**  
- Fallback to safe defaults
- Incident banner mandatory

---

## GAP 15 — EXIT & PORTABILITY

**Problem**  
Loss of personalization control on exit.

**Mandatory Rule**  
- Personalization profile exportable
- Reset impact disclosed

---

## GAP 16 — DATA FINALITY DEPENDENCY

**Problem**  
Recommendations built on provisional data.

**Mandatory Rule**  
- Data state declared: provisional | finalized
- Re-ranking after finalization logged

---

## GAP 17 — FEATURE FRESHNESS & TTL

**Problem**  
Stale features drive personalization.

**Mandatory Rule**  
- Feature TTL enforced
- Recommendations suppressed if critical features expired

---

## GAP 18 — SMALL COHORT PRIVACY

**Problem**  
Risk of implicit re-identification.

**Mandatory Rule**  
- Minimum cohort size enforced
- Noise injection or fallback required

---

## GAP 19 — ROLE-BASED MISUSE PREVENTION

**Problem**  
Institutions or mentors pressure learners.

**Mandatory Rule**  
- Institutional views read-only
- Recommendations forbidden as mandates

---

## GAP 20 — ALERT & NOTIFICATION FATIGUE

**Problem**  
Cognitive overload from recommendation alerts.

**Mandatory Rule**  
- Alert frequency caps
- Confidence-gated notifications only

---

## GAP 21 — ACCESS AUDITABILITY

**Problem**  
Silent manipulation or surveillance.

**Mandatory Rule**  
- Every recommendation view & override logged
- User-visible access history

---

## GAP 22 — REGULATORY REPORT FREEZE

**Problem**  
Inconsistent compliance disclosures.

**Mandatory Rule**  
- Reports generated from frozen datasets
- Versioned snapshots mandatory

---

## GAP 23 — INCIDENT MODE COST SAFETY

**Problem**  
Runaway compute during incidents.

**Mandatory Rule**  
- High-cost models auto-suspended
- Fallback heuristics enforced

---

## GAP 24 — COST GOVERNANCE

**Problem**  
Unbounded recommendation infra spend.

**Mandatory Rule**  
- Cost attribution per model & surface
- Budget thresholds enforced

---

## GAP 25 — SYSTEM HUMILITY & LIMIT DISCLOSURE

**Problem**  
Recommendations treated as guarantees.

**Mandatory Rule**  
- “Suggestion, not guarantee” labels mandatory
- Accuracy ranges displayed where applicable

---

## TERMINAL STATE

All gaps are:
- Add-only
- Enforceable
- Auditable
- Non-duplicative

No further Recommendation System design gaps exist.

END OF FILE

