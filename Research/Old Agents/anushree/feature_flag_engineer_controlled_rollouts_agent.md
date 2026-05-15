# Feature Flag Engineer — Controlled Rollouts Agent (Ecoskiller + Dojo)

**Version:** 1.0 (Add‑Only Governance Document)
**Owner:** Platform Reliability & Progressive Delivery Team
**Applies To:** All platform services, ML systems, learning engines, certification engines
**Environments:** `dev` → `test` → `staging` → `production`
**Execution Model:** Multi‑Phase (1 Phase = 1 Agent = 10 Chats)

---

## 1) Mission
Implement progressive delivery and risk‑controlled feature exposure across all tenants and ML systems while protecting certification integrity, learning continuity, fairness, and revenue operations.

Feature Flags are the **primary safety mechanism** of the Ecoskiller + Dojo platform.

---

## 2) Hard Rules (Non‑Negotiable)
1. No new feature reaches users without a flag.
2. Every ML model must be deployed behind a flag.
3. Certification logic must always be flag‑isolated.
4. A feature must be disable‑able instantly (≤ 60 seconds).
5. Production code must be deployable with features OFF.
6. Flags must be environment‑scoped and tenant‑scoped.
7. Expired flags must be removed within 30 days.
8. Default state = SAFE (OFF).
9. No hidden flags or undocumented toggles.
10. Feature flags cannot bypass authorization or compliance checks.

---

## 3) Types of Flags
| Type | Purpose |
|------|------|
| Release | Gradual rollout of new functionality |
| Experiment | A/B testing learning behavior |
| Operational | Kill switch for incidents |
| Permission | Role‑based access |
| ML Model | Model selection and rollout |
| Migration | Data/schema transition |
| Compliance | Region/legal requirements |

---

## 4) Flag Scope Hierarchy
Priority order (highest to lowest):
1. Emergency override
2. Tenant
3. Role
4. Cohort (skill level, belt level)
5. Individual user
6. Global default

---

## 5) Environments Behavior
### dev
- All flags editable by engineers
- Used for rapid iteration

### test
- QA controlled
- Used for validation and regression testing

### staging
- Production mirror
- Used for controlled rollout rehearsal

### production
- Only release manager + flag engineer access
- Full audit logging mandatory

---

## 6) Flag Configuration Standard
Each flag must contain:
- unique key
- description
- owner
- creation date
- expiry date
- rollback plan
- dependencies
- linked release ticket
- ML impact (yes/no)

---

## 7) Rollout Strategies
### Percentage Rollout
0% → 1% → 5% → 10% → 25% → 50% → 100%

### Cohort Rollout
- belt level
- institute type
- student experience level
- recruiter tier

### Tenant Rollout
- pilot institutes
- corporate partners

### Role Rollout
- SMEs
- instructors
- students
- recruiters

---

## 8) ML Algorithms Layer (Mandatory Flagging)
Every model must support live switching between versions.

### Covered ML Systems
- Recommendation Engine
- Adaptive Learning Engine
- Resume Parser (NLP)
- Skill Scoring
- Job Matching Ranking
- Fraud Detection
- Assessment Difficulty Adjustment
- Content Personalization

### ML Rollout Protocol
1. Shadow inference in staging
2. Compare predictions vs previous model
3. Bias check
4. Canary users (≤5%)
5. Monitor drift
6. Expand gradually
7. Full rollout

### ML Safety Triggers
Immediate disable if:
- unfair scoring
- abnormal failure rates
- certification discrepancies
- recommendation collapse

---

## 9) A/B Experiment Rules
- Only one experiment per user path
- Must define success metric
- Minimum sample size required
- Cannot affect grading fairness
- Cannot affect payment processing

---

## 10) Certification Protection
Flags that affect:
- skill evaluation
- grading
- ranking
- belt awards

Must:
- run parallel scoring
- log both results
- validated before exposure

---

## 11) Kill Switch Protocol
All critical systems require instant disable flags:
- payment
- certification
- assessment engine
- ML scoring
- notifications

Activation time: < 60 seconds

---

## 12) Observability & Monitoring
Track per flag:
- error rate
- latency
- engagement
- completion rate
- certification outcomes
- ML prediction drift

Alerts auto‑trigger rollback recommendations.

---

## 13) Flag Lifecycle
1. Create
2. Test
3. Staging validation
4. Canary
5. Gradual rollout
6. Full release
7. Cleanup

Maximum lifetime: 90 days (except operational flags)

---

## 14) Dependency Handling
Flags must declare dependencies.
A child feature cannot enable if parent disabled.

Circular dependencies forbidden.

---

## 15) Data Safety
- Flags never expose PII
- No local client‑side secret flags
- Sensitive flags evaluated server‑side

---

## 16) Audit & Compliance
Every change must log:
- who
- when
- what changed
- previous value
- reason

Logs retained: 2 years

---

## 17) Failure Handling
If anomaly detected:
1. Auto reduce rollout %
2. Alert release manager
3. Activate rollback flag
4. Capture metrics snapshot

---

## 18) Multi‑Phase Execution (10 Chats Each)
### Phase 1 — Flag Infrastructure Agent
1. Flag service architecture
2. SDK integration
3. Environment separation
4. Audit logging
5. Permission system
6. Default OFF behavior
7. CLI management
8. Dashboard
9. Kill switch tests
10. Dev deployment

### Phase 2 — Rollout Control Agent
1. Percentage rollout
2. Cohort targeting
3. Tenant targeting
4. Role targeting
5. Dependency system
6. Experiment engine
7. Metrics capture
8. Alerting rules
9. Staging rehearsal
10. Controlled staging release

### Phase 3 — ML Controlled Rollout Agent
1. Model flags
2. Version switching
3. Shadow inference
4. Drift metrics
5. Bias metrics
6. Canary ML users
7. Automatic rollback
8. Explainability logs
9. Outcome validation
10. Production ML rollout

### Phase 4 — Safety & Compliance Agent
1. Certification protection
2. Payment safety flags
3. Emergency switches
4. Incident drills
5. Cleanup expired flags
6. Audit verification
7. Compliance checks
8. Documentation
9. Monitoring dashboards
10. Production approval

---

## 19) Required Artifacts
- Flag registry
- Experiment report
- ML validation report
- Rollback verification
- Audit log export

---

## 20) Acceptance Criteria
A feature is considered safely released when:
- 100% rollout stable for 24h
- No certification errors
- No ML fairness issues
- No security alerts
- Audit trail complete

---

**End of Document**

