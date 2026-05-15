# Feedback Systems Engineer — Survey & NPS Systems Agent (Ecoskiller + Dojo)

**Version:** 1.0 (Add‑Only Governance & Experience Intelligence Manual)
**Owner:** Experience Intelligence & Insights Team
**Scope:** Surveys, NPS collection, in‑product feedback, sentiment analysis, learning satisfaction measurement, ML‑assisted feedback understanding
**Environments:** `dev` → `test` → `staging` → `production`
**Execution Model:** Multi‑Phase (1 Phase = 1 Agent = 10 Chats)

---

## 1) Mission
Continuously capture, interpret, and operationalize user feedback to improve learning quality, certification trust, and platform usability without biasing academic outcomes or user behavior.

This system converts **user voice into actionable platform intelligence**.

---

## 2) Core Responsibilities
- Build survey delivery infrastructure
- Implement NPS measurement
- Collect contextual feedback inside platform workflows
- Measure learning satisfaction
- Detect frustration points
- Integrate feedback into product decisions
- Support ML sentiment and intent understanding

---

## 3) Hard Rules
1. Feedback must never affect grading or certification.
2. Surveys must be non‑intrusive during assessments.
3. Every survey must have a clear purpose.
4. NPS must be calculated consistently across tenants.
5. Anonymous feedback must remain anonymous.
6. Feedback cannot reveal PII in analytics.
7. Feedback collection must respect opt‑out preferences.
8. ML interpretation must not misclassify academic performance.
9. Feedback frequency must be rate‑limited.
10. Feedback data must be auditable.

---

## 4) Feedback Types Supported
- Post‑lesson survey
- Course completion survey
- Assessment experience feedback
- Certification trust feedback
- Recruiter satisfaction
- Institute admin feedback
- Support interaction feedback
- Feature feedback prompts
- NPS (Net Promoter Score)

---

## 5) Environments Behavior
### dev
- survey template creation
- UI testing

### test
- QA validation of survey triggers
- scoring validation

### staging
- realistic workflow rehearsal
- ML sentiment shadow processing

### production
- live feedback collection
- operational dashboards

---

## 6) NPS Standard
NPS question:
"How likely are you to recommend Ecoskiller to others? (0–10)"

Categories:
- Promoters: 9–10
- Passives: 7–8
- Detractors: 0–6

NPS = %Promoters − %Detractors

Calculated per:
- student
- institute
- recruiter
- SME

---

## 7) Triggering Rules
Feedback triggers allowed only after:
- lesson completion
- certification issuance
- job application outcome
- support resolution

Forbidden during:
- exams
- timed assessments
- payment processing

---

## 8) Survey Design Standards
Each survey must define:
- objective
- target role
- trigger event
- maximum questions (≤5)
- estimated completion time (<60 seconds)

---

## 9) ML Algorithms Layer — Sentiment & Intent Analysis
### ML Capabilities
- sentiment analysis (positive/neutral/negative)
- topic extraction
- complaint detection
- usability issue detection
- learning difficulty detection

### Requirements
- model must be language tolerant
- bias evaluation mandatory
- false complaint detection prevented
- manual review for critical alerts

### Safety Rule
ML sentiment results cannot automatically change grades, rankings, or certifications.

---

## 10) Feedback Classification
Feedback categorized into:
- usability issue
- learning difficulty
- content quality
- technical bug
- assessment fairness
- placement experience
- instructor effectiveness

Critical categories trigger alerts.

---

## 11) Integration With Other Systems
Feedback data must be shared with:
- Product Ops Manager (prioritization)
- Adoption Analytics Engineer (correlation with usage)
- Rollout Engineer (release risk detection)
- Support Team (ticket creation)

---

## 12) Privacy & Compliance
- optional anonymity
- no public display without consent
- redaction of personal information
- retention: 12 months

---

## 13) Alerting Rules
Immediate alert if feedback indicates:
- certification distrust
- unfair assessment
- placement failure pattern
- repeated platform errors

---

## 14) Anti‑Bias Safeguards
System must prevent:
- instructor retaliation
- grading influence
- targeted negative review manipulation

---

## 15) Dashboards Required
- NPS dashboard
- satisfaction trends
- feature feedback
- sentiment trends
- learning difficulty heatmap

---

## 16) Multi‑Phase Execution (10 Chats Each)
### Phase 1 — Survey Infrastructure Agent
1. survey service architecture
2. templates system
3. trigger engine
4. rate limiting
5. role targeting
6. UI components
7. dev validation
8. opt‑out handling
9. data schema
10. dev deployment

### Phase 2 — NPS Measurement Agent
1. NPS implementation
2. role segmentation
3. dashboard setup
4. QA verification
5. staging rehearsal
6. baseline measurement
7. reporting structure
8. release preparation
9. documentation
10. production activation

### Phase 3 — ML Feedback Intelligence Agent
1. sentiment model integration
2. topic extraction
3. complaint detection
4. bias checks
5. shadow evaluation
6. alert rules
7. manual review workflow
8. correlation with adoption data
9. feedback classification
10. ML rollout support

### Phase 4 — Action & Continuous Improvement Agent
1. feedback triage
2. product prioritization
3. rollout risk detection
4. user communication insights
5. improvement recommendations
6. academic review alerts
7. support ticket integration
8. periodic reports
9. long‑term trend analysis
10. production validation

---

## 17) Required Artifacts
- survey catalog
- NPS report
- sentiment analysis report
- feedback classification report
- improvement recommendations

---

## 18) Acceptance Criteria
Feedback system operational only when:
- feedback collected reliably
- NPS calculated accurately
- ML sentiment functioning
- actionable insights produced
- privacy compliant

---

**End of Document**

