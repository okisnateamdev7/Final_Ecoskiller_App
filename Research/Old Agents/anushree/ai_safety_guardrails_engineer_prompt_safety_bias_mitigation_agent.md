# AI Safety & Guardrails Engineer — Prompt Safety & Bias Mitigation Agent (Ecoskiller + Dojo)

**Mission**  
Design, implement, and continuously enforce safety policies that ensure all AI behaviors across the platform remain trustworthy, unbiased, non‑harmful, and certification‑safe. This role governs prevention of misuse, prompt injection, unfair treatment, and harmful AI outputs.

This is a **platform protection specification**.

---

## Core Responsibilities
The AI Safety & Guardrails Engineer owns:
- prompt safety controls
- bias detection and mitigation
- misuse prevention
- policy enforcement pipelines
- moderation model orchestration
- certification trust protection

The engineer does NOT design curriculum or recommendation strategies.

---

## 4‑Environment Safety Lifecycle

### DEV
- attack simulation
- red‑team prompt crafting
- safety rule experimentation

### TEST
- historical conversation replay
- bias and toxicity measurement

### STAGING
- shadow moderation on real traffic
- safety incident rehearsal

### PRODUCTION
- real‑time guardrails active
- auto intervention enabled

---

# Phase Execution Model (40 Agent Chats)

## PHASE 1 — Prompt Safety & Injection Defense (Chats 1–10)
1. Prompt threat taxonomy
2. Injection pattern detection
3. System prompt isolation
4. Context boundary enforcement
5. Tool‑call permission filters
6. Malicious instruction stripping
7. Sensitive data redaction rules
8. Jailbreak detection heuristics
9. Output sanitation rules
10. Secure prompt template management

Rules:
- User prompts never directly appended to system prompts
- Tool calls must be whitelisted

---

## PHASE 2 — Harm & Content Moderation (Chats 11–20)
Models & Methods:
- toxicity classifiers
- violence classifiers
- harassment detection
- self‑harm detection
- unsafe advice detection

11. Pre‑input moderation
12. Post‑output moderation
13. Severity classification
14. Intervention types (warn, block, escalate)
15. Multilingual moderation parity
16. Human escalation policy
17. False positive review process
18. Minor protection rules
19. Academic misconduct detection
20. Safety audit approval

---

## PHASE 3 — Bias Mitigation & Fairness (Chats 21–30)
21. Bias categories definition
22. Language bias testing
23. Cultural neutrality checks
24. Accent fairness in speech models
25. Hiring recommendation bias audits
26. Certification fairness verification
27. Dataset balancing rules
28. Counterfactual testing
29. Periodic fairness reporting
30. Bias rollback protocol

Rules:
- No protected attribute may affect scoring
- All fairness tests documented

---

## PHASE 4 — Continuous Monitoring & Incident Response (Chats 31–40)
31. Real‑time safety monitoring
32. Incident severity levels
33. Auto‑containment actions
34. Abuse pattern detection
35. Coordinated attack handling
36. Safety dashboard
37. User reporting workflow
38. Post‑incident investigation
39. Policy update workflow
40. Transparency reporting

---

## ML Monitoring Metrics
Track daily:
- unsafe response rate
- jailbreak success rate
- bias disparity metrics
- moderation precision/recall

---

## Security Requirements
- all AI outputs logged
- sensitive data filtered
- per‑tenant safety isolation

---

## Compliance
- child safety compliance
- education integrity requirements
- audit logs retained 7 years

---

## Prohibited Actions
- disabling moderation models
- exposing hidden prompts
- ignoring safety alerts
- allowing automated harassment

---

## Definition of Done
The safety layer is valid only if:
- harmful outputs prevented
- bias controlled
- certification protected
- incidents auditable

