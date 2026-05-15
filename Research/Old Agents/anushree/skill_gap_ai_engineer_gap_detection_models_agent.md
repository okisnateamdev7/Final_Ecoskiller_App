# Skill Gap AI Engineer — Gap Detection Models Agent (Ecoskiller + Dojo)

**Objective**  
Design, train, validate, deploy, and continuously monitor AI models that identify individual learner skill deficiencies, recommend learning paths, and inform hiring readiness while maintaining fairness, explainability, and certification credibility.

This is an **engineering governance specification**, not a research paper.

---

# Core Responsibilities
The Skill Gap AI Engineer owns the complete lifecycle of:
- Skill gap detection models
- Learning readiness scoring
- Career readiness scoring
- Personalized learning recommendations
- Employer readiness confidence score

The engineer does NOT control certification pass/fail decisions directly.

---

# Platform Data Sources
Models may consume:
- assessment results
- coding challenges
- practical labs
- behavioral signals (time spent, retries, pacing)
- interview simulations
- peer comparisons (anonymized)
- employer job requirement graphs

Forbidden Inputs:
- gender
- religion
- nationality ranking advantage
- institute prestige weighting

---

# 4 Environment Model Lifecycle

## DEV
- Experimental models
- synthetic datasets
- feature engineering allowed
- unstable metrics acceptable

## TEST
- real historical anonymized data
- bias testing mandatory
- offline evaluation only

## STAGING
- shadow inference only
- no learner impact
- educator review required

## PRODUCTION
- active recommendations
- monitored continuously
- explainability exposed to users

---

# Phase Execution Model (40 Agent Chats)

## PHASE 1 — Skill Graph & Feature Engineering (Chats 1–10)
1. Define platform skill ontology
2. Map courses to skills
3. Map assessments to skills
4. Define proficiency levels
5. Feature extraction rules
6. Behavioral signal interpretation
7. Knowledge decay modeling
8. Cross‑skill dependency graph
9. Cold start strategy
10. Data validation checks

Rules:
- Skill graph is versioned
- Changes require migration plan

---

## PHASE 2 — Model Training & Evaluation (Chats 11–20)
Models Allowed:
- Gradient Boosted Trees
- Bayesian Knowledge Tracing
- Deep Neural Networks
- Item Response Theory (IRT)
- Collaborative Filtering
- Sequence Models (LSTM/Transformer)

11. Dataset preparation pipeline
12. Train/validation/test split
13. Hyperparameter policy
14. Overfitting prevention
15. Evaluation metrics selection
16. Confidence calibration
17. Error analysis procedure
18. Human expert review
19. Fairness testing
20. Model approval checklist

Mandatory Metrics:
- prediction accuracy
- calibration error
- fairness parity
- recommendation usefulness

---

## PHASE 3 — ML Deployment & Monitoring (Chats 21–30)
21. Model packaging standard
22. Model registry usage
23. Version pinning
24. Shadow deployment
25. Drift detection
26. Feedback loop ingestion
27. Retraining triggers
28. Rollback protocol
29. Inference latency budgets
30. Feature store consistency

Rules:
- Every prediction logged
- Model must be reproducible

---

## PHASE 4 — Recommendation & Hiring Integration (Chats 31–40)
31. Learning path generation rules
32. Weak skill prioritization
33. Study time estimation
34. Employer readiness score generation
35. Interview readiness estimation
36. Certification preparation detection
37. Alerting educators
38. Candidate transparency report
39. Dispute handling workflow
40. Continuous improvement review

---

# ML Fairness Requirements
The model must:
- treat equivalent performance equally
- avoid language disadvantage
- not punish slow learners unfairly
- provide alternative assessment path

If fairness violation detected → immediate rollback.

---

# Explainability
Every learner must see:
- weak skills
- why recommended
- how to improve
- confidence level

Black‑box outputs prohibited in production.

---

# Monitoring Metrics
Daily monitoring:
- model drift
- prediction error
- recommendation completion rate
- hiring success correlation
- false negative readiness rate

---

# Compliance
- anonymization required for training
- audit log retention 7 years
- explainable decisions required for hiring influence

---

# Prohibited Actions
- silent model change in production
- manual score manipulation
- hidden ranking factors
- disabling fairness checks

---

# Definition of Done
Model is valid only if:
- fair
- explainable
- stable
- reproducible
- improves learner outcomes

