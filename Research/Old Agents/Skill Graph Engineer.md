# Skill Graph Engineer — Complete Gap Register

Project: Ecoskiller + Dojo
Domain: Skill Graph Engineering
Scope: All Identified Gaps (1–40)
Status: Governance Complete

---

# SECTION A — CORE ONTOLOGY & LIFECYCLE GAPS

## GAP 1 — Skill Deprecation Governance
Define skill lifecycle states.
- status: active | deprecated | merged
- deprecation_reason required
- replacement_skill_id required for merges
- deprecated skills blocked from new mappings

## GAP 2 — Skill Alias Normalization
- Canonical global_skill_id required
- Alias table mandatory
- ML models must train only on canonical IDs

## GAP 3 — Ontology Breaking Change Detection
Schema changes must be labeled:
- non-breaking
- backward-compatible
- breaking
Breaking changes require migration + impact report.

## GAP 4 — Skill Granularity Standard
- Max hierarchy depth: 4
- Skill must belong to exactly one domain
- Micro-skill review required

## GAP 5 — Cross-Tenant Contamination
- Global graph separated from tenant graph
- Tenant inference cannot auto-modify global graph
- Federated aggregation required

---

# SECTION B — STRUCTURAL GRAPH INTEGRITY

## GAP 6 — Prerequisite Loop Resolution
- Detect cycles
- Auto-demote weakest-confidence edge
- SME review for recurring cycles

## GAP 7 — Embedding Drift Monitoring
- Monitor cosine distance distribution
- Drift threshold alerts
- Retraining required if exceeded

## GAP 8 — Centrality Fairness
- Normalize degree centrality
- Long-tail boosting
- Exposure caps for dominant nodes

## GAP 9 — Confidence Decay
- Relationship confidence decays over time
- Quarterly recalibration

## GAP 10 — Snapshot Rollback
- Retain last 5 production snapshots
- Snapshot integrity hash
- Rollback < 10 minutes

---

# SECTION C — TRACEABILITY & CONFIDENCE

## GAP 11 — Relationship Attribution
Each edge must include:
- source_type: SME | ML | hybrid
- supporting_evidence_id
- model_version (if ML)

## GAP 12 — Confidence Floor
- Edges below threshold remain experimental
- Cannot enter production graph

## GAP 13 — Impact Propagation
Skill changes must trigger:
- dependent recalculation
- recommendation compatibility test

## GAP 14 — RBAC Governance
Roles:
- Ontology Admin
- ML Engineer
- SME Reviewer
- Read-Only
Only Admin modifies schema.

## GAP 15 — Cost Governance
- Query rate limits
- Read replica scaling
- Idle shutdown policy

---

# SECTION D — ID, TEMPORAL & CERTIFICATION INTEGRITY

## GAP 16 — Skill ID Immutability
- global_skill_id never changes
- merges create alias only

## GAP 17 — Temporal Graph Queries
Graph must support:
- as_of_timestamp queries
- ontology_version queries
- snapshot_id queries

## GAP 18 — Skill Creation Evidence
New skill requires:
- SME validation OR
- ML threshold + usage evidence

## GAP 19 — Learning Path Determinism
- Deterministic traversal
- Snapshot-bound path generation

## GAP 20 — Assessment Calibration
- Psychometric difficulty recalibrates prerequisite weights

## GAP 21 — Certification Graph Freeze
Certification binds to:
- graph_snapshot_id
- ontology_version

## GAP 22 — Path Explanation Completeness
API must return:
- prerequisite chain
- confidence
- attribution

## GAP 23 — Graph Bootstrap
Initial graph must include:
- SME-seeded ontology
- baseline mappings

## GAP 24 — Disaster Recovery
- Cross-region replication
- Daily backup verification
- Recovery runbook

## GAP 25 — Limit Disclosure
Edges labeled:
- inferred
- validated
- experimental

---

# SECTION E — CONSISTENCY & FEATURE ALIGNMENT

## GAP 26 — Consistency Window
Snapshot, embeddings, APIs must share snapshot_id.

## GAP 27 — Merge History Traceability
Store:
- source_skill_id
- target_skill_id
- merge_timestamp

## GAP 28 — Query Rate Protection
- API throttling
- Depth limits

## GAP 29 — Feature Store Alignment
Embeddings must include:
- feature_store_version
- embedding_model_version

## GAP 30 — Incident Visibility
If graph degraded:
- fallback mode
- user-visible status

---

# SECTION F — HUMAN & OPERATIONAL GOVERNANCE

## GAP 31 — SME Override Governance
- Dual-review required
- Diff logging mandatory

## GAP 32 — Migration Dry-Run
- Mandatory staging simulation
- Diff report required

## GAP 33 — API Contract Testing
- Schema contract tests
- Snapshot compatibility tests

## GAP 34 — Schema Freeze Window
- Freeze 72 hours before production release

## GAP 35 — Documentation Sync
- Documentation auto-generated from schema

---

# SECTION G — SYSTEM INVARIANTS

## GAP 36 — Snapshot Pinning
All services must request explicit snapshot_id.

## GAP 37 — Latency Budget
- p95 < 200ms
- Traversal depth limit enforced

## GAP 38 — Cache Invalidation
Graph publish triggers cache invalidation.

## GAP 39 — Metric Ownership
Each metric must define:
- owner_role
- threshold
- escalation policy

## GAP 40 — Reference Dataset
Gold-standard dataset required for CI regression tests.

---

# FINAL STATUS

All 40 gaps are now documented with enforceable engineering controls.

Skill Graph Engineer domain is governance-complete.

