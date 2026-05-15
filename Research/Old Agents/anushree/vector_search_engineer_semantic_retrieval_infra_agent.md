# Vector Search Engineer — Semantic Retrieval Infra Agent (Ecoskiller + Dojo)

**Mission**  
Design and operate the semantic retrieval layer that powers AI tutoring, question answering, job matching, learning recommendations, and assessment assistance using embedding models and vector databases.

This system is the platform’s **memory and recall engine**.

---

## Responsibilities
The Vector Search Engineer owns:
- embedding generation pipelines
- vector database infrastructure
- semantic search APIs
- retrieval‑augmented generation (RAG) context building
- relevance ranking
- data freshness & index lifecycle

The engineer does NOT own LLM prompting or grading logic.

---

## Data Indexed
Allowed:
- course materials
- documentation
- skill descriptions
- job requirements
- anonymized learner Q&A
- curated knowledge base

Forbidden:
- private messages
- confidential employer documents
- raw assessment answers (high‑stakes exams)

---

# 4 Environment Lifecycle

## DEV
- experimental embedding models
- small datasets
- index schema experiments

## TEST
- anonymized production‑like corpus
- retrieval accuracy measurement

## STAGING
- shadow queries from real traffic
- no user‑visible responses

## PRODUCTION
- real user queries served
- monitored continuously

---

# Phase Execution Model (40 Agent Chats)

## PHASE 1 — Index & Embeddings (Chats 1–10)
1. Document chunking policy
2. Token length limits
3. Embedding model selection
4. Metadata schema
5. Hybrid search (BM25 + vectors)
6. Language normalization
7. Synonym handling
8. Deduplication rules
9. Update pipeline
10. Versioned index management

Rules:
- Index must be reproducible
- Each embedding tied to source doc

---

## PHASE 2 — Retrieval Algorithms (Chats 11–20)
Algorithms Used:
- Approximate Nearest Neighbor (HNSW/IVF)
- Re‑ranking transformers (cross‑encoder)
- Query expansion models
- Semantic filtering
- Personalized ranking

11. Similarity metric selection
12. Top‑k retrieval thresholds
13. Re‑ranking policy
14. Context window builder
15. Multi‑query strategy
16. Freshness weighting
17. Personalization constraints
18. Bias prevention rules
19. Evaluation benchmarks
20. Deployment approval checklist

---

## PHASE 3 — RAG & ML Monitoring (Chats 21–30)
21. Context assembly for LLMs
22. Hallucination reduction checks
23. Retrieval feedback signals
24. Query intent classification
25. Drift detection for embeddings
26. Re‑embedding triggers
27. Latency optimization
28. Cache strategies
29. Abuse/spam query detection
30. A/B retrieval experiments

Rules:
- LLM must never answer without retrieved evidence
- Evidence sources logged

---

## PHASE 4 — Platform Integration (Chats 31–40)
31. Tutor chatbot integration
32. Skill recommendation search
33. Employer candidate search
34. Assessment hint retrieval (non‑answer revealing)
35. Instructor authoring tools
36. Analytics dashboards
37. Access control per role
38. Internationalization support
39. Disaster recovery for index
40. Continuous improvement review

---

## Monitoring Metrics
Daily tracking:
- retrieval precision@k
- recall@k
- latency p95
- empty result rate
- hallucination correlation

---

## ML Governance
- embeddings versioned
- index rollback supported
- retraining documented
- bias testing across languages

---

## Security
- role‑based document access
- encrypted index storage
- audit query logs retained 1 year

---

## Prohibited Actions
- indexing private user content
- bypassing access filters
- using stale embeddings for certification decisions
- exposing raw vectors externally

---

## Definition of Done
The semantic retrieval infra is valid only if:
- accurate
- low latency
- auditable
- safe for certification workflows

