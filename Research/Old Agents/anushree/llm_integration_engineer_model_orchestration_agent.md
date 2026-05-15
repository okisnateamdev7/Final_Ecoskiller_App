# LLM Integration Engineer — Model Orchestration Agent (Ecoskiller + Dojo)

**Mission**  
Architect, coordinate, and safely operate all Large Language Model interactions across the platform, ensuring reliable reasoning, controlled behavior, cost efficiency, explainability, and compatibility with certification workflows.

This agent governs HOW the platform talks to AI models — not WHAT they decide.

---

## Responsibilities
The LLM Integration Engineer owns:
- model routing
- provider abstraction
- prompt execution pipelines
- tool/function calling orchestration
- RAG integration with vector retrieval
- safety filtering and response validation

The engineer does NOT design curriculum or grading rubrics.

---

## Supported Model Types
- reasoning LLMs
- lightweight chat models
- code generation models
- speech‑to‑text models
- text‑to‑speech models
- moderation/safety models

---

## 4‑Environment Lifecycle
### DEV
- new provider experimentation
- prompt testing
- mock data only

### TEST
- anonymized real scenarios
- deterministic replay testing

### STAGING
- shadow real traffic
- response comparison enabled

### PRODUCTION
- user‑visible responses
- monitored continuously

---

# Phase Execution Model (40 Agent Chats)

## PHASE 1 — Provider Abstraction Layer (Chats 1–10)
1. Unified model interface contract
2. Provider failover routing
3. API key isolation per environment
4. Request schema standardization
5. Rate limit handling
6. Token accounting policy
7. Timeout & retry strategy
8. Cost budget guardrails
9. Streaming response handling
10. Provider health monitoring

Rules:
- No direct model calls from application code
- All calls via orchestration service

---

## PHASE 2 — Prompt & Tool Orchestration (Chats 11–20)
Algorithms & Methods:
- Tool/function calling
- Chain‑of‑thought internal reasoning (hidden)
- Retrieval‑augmented generation (RAG)
- Self‑consistency sampling
- Guarded decoding

11. System prompt governance
12. Context window assembly
13. Function calling policy
14. Structured JSON outputs
15. Tool priority ordering
16. Safety filters (pre‑prompt)
17. Output validation (post‑response)
18. Multi‑step reasoning workflows
19. Caching strategy
20. Prompt version control

---

## PHASE 3 — Reliability & Safety (Chats 21–30)
21. Hallucination detection heuristics
22. Evidence grounding checks
23. Response confidence scoring
24. PII leakage detection
25. Toxicity moderation routing
26. Adversarial prompt handling
27. Red‑team testing protocol
28. Automatic fallback responses
29. Conversation memory policy
30. Incident rollback procedure

Rules:
- Unverified answers must be labeled uncertain
- Safety model runs before and after response

---

## PHASE 4 — Platform Integration (Chats 31–40)
31. Tutor chatbot integration
32. Assessment explanation generation
33. Interview simulator orchestration
34. Career guidance assistant
35. Multilingual support routing
36. Accessibility (voice interface)
37. Analytics logging
38. Continuous evaluation benchmarks
39. Model upgrade migration plan
40. Long‑term audit storage

---

## ML Monitoring Metrics
Track continuously:
- hallucination rate
- grounded answer rate
- tool‑call success rate
- token cost per user
- latency p95

---

## Security
- prompt injection defense required
- secret filtering
- per‑tenant isolation

---

## Compliance
- audit logs retained 7 years
- model decisions reproducible
- certification workflows protected

---

## Prohibited Actions
- direct production model swap
- disabling safety filters
- bypassing retrieval layer
- unlogged prompt changes

---

## Definition of Done
The orchestration layer is valid only if:
- reliable
- auditable
- safe
- cost controlled
- reproducible

