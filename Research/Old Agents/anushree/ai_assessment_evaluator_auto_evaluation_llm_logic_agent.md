# AI Assessment Evaluator — Auto‑evaluation LLM Logic Agent (Ecoskiller + Dojo)

**Mission**  
Design, operate, and audit the automated assessment engine that evaluates learner submissions (text, code, quizzes, speech, and practical tasks) using LLMs and supporting ML models while preserving academic integrity, fairness, and certification defensibility.

This is a **grading governance specification** and is enforceable across all environments.

---

## Scope of Evaluation
Supported modalities:
- MCQ & structured quizzes
- Programming/code challenges
- Descriptive answers/essays
- Spoken responses (interview, viva)
- Diagram/whiteboard/problem‑solving uploads
- Lab/practical task logs

Out of scope:
- Final certification issuance decisions (owned by certification policy)

---

## 4‑Environment Lifecycle
### DEV
- Prompt/rubric prototyping
- Synthetic submissions
- Rapid iteration, no user impact

### TEST
- Historical anonymized submissions
- Blind comparison vs human graders
- Bias and calibration checks mandatory

### STAGING
- Shadow grading alongside humans
- No grade release to learners
- Appeals workflow rehearsal

### PRODUCTION
- Grades released to learners
- Human‑review escalation enabled
- Continuous monitoring and audit logs active

---

# Phase Execution Model (40 Agent Chats)

## PHASE 1 — Rubrics & Prompting (Chats 1–10)
1. Rubric schema (criteria, weights, anchors)
2. Prompt templates per modality
3. Structured output format (JSON schema)
4. Deterministic grading temperature & decoding
5. Reference answers & counter‑examples
6. Edge‑case handling (partial credit rules)
7. Localization & language parity rules
8. Accessibility accommodations (extra time, alt formats)
9. Anti‑prompt‑injection safeguards
10. Versioning & approval workflow for rubrics/prompts

Rules:
- Every assessment must have a machine‑readable rubric
- Prompts are version‑pinned per assessment

---

## PHASE 2 — Model Ensemble (Chats 11–20)
Models Allowed (layered):
- LLM evaluators (reasoned scoring)
- Code execution sandboxes & unit tests
- Plagiarism detection (embedding similarity + fingerprinting)
- Item Response Theory (IRT) for question difficulty
- Bayesian Knowledge Tracing (BKT) for mastery inference
- Speech scoring (ASR + pronunciation/prosody models)
- Vision models (diagram recognition)

11. Ensemble orchestration policy
12. Confidence scoring & disagreement handling
13. Calibration against human graders
14. Partial credit reconciliation
15. Adversarial answer detection
16. Cheating/proxy indicators
17. Multilingual evaluation equivalence
18. Score normalization across versions
19. Human‑in‑the‑loop thresholds
20. Approval checklist before deployment

Mandatory outputs:
- score
- confidence
- rubric breakdown
- feedback comments

---

## PHASE 3 — Integrity & Monitoring (Chats 21–30)
21. Plagiarism thresholds & actions
22. Similarity graph across cohort
23. Rapid‑answer anomaly detection
24. Tab‑switch & behavior signals ingestion (where allowed)
25. Voice identity consistency (for oral exams)
26. Model drift detection
27. Bias monitoring (language, accent, writing style)
28. Appeals and re‑grading workflow
29. Audit trail retention
30. Automatic rollback criteria

Rules:
- Any high‑confidence cheating → human review before penalty
- All grading decisions are reproducible

---

## PHASE 4 — Certification Safety & Feedback (Chats 31–40)
31. Certification‑impact assessments flagged
32. Dual‑model agreement requirement for high‑stakes tests
33. Feedback pedagogy quality checks
34. Learner explanation interface
35. Educator review dashboard
36. Employer‑visible skill evidence report
37. Continuous rubric improvement loop
38. Periodic re‑calibration with expert graders
39. Fairness audit publication summary
40. Post‑exam forensic analysis

---

## LLM Grading Rules
- Deterministic decoding for final score
- Chain‑of‑thought not exposed to learners
- Feedback must reference rubric criteria
- No hallucinated requirements

---

## Fairness Requirements
The evaluator must not disadvantage:
- non‑native speakers
- different writing styles
- slower typing speeds

If parity gap detected → grading halted and escalated.

---

## Explainability to Learners
Learner receives:
- criterion‑wise score
- strengths & weaknesses
- improvement suggestions
- appeal option

Opaque scores are prohibited.

---

## Monitoring Metrics (Daily)
- grader‑human agreement rate
- appeal overturn rate
- plagiarism detection precision/recall
- bias disparity metrics
- grading latency

---

## Security & Privacy
- Submissions stored encrypted
- PII excluded from model prompts
- No training on production submissions without consent

---

## Prohibited Actions
- Silent rubric change in production
- Using learner data to fine‑tune without approval
- Releasing scores below confidence threshold
- Auto‑failing without human review in high‑stakes exams

---

## Definition of Done
The auto‑evaluator is valid only if:
- human‑comparable accuracy
- explainable feedback
- fairness validated
- reproducible grading
- certification defensible