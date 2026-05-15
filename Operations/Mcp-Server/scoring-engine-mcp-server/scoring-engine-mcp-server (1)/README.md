# mcp-scoring-engine-ecoskiller

**Ecoskiller | CAT-SCORING-ENGINE — AI-Powered Assessment Scoring & Belt Advancement**  
MCP Server in Java | 18 Agents | Priority: HIGH | Namespace: analytics

---

## Overview

Secure Java MCP server implementing the full `scoring-engine` specification for the Ecoskiller platform. Covers asynchronous ML scoring for GD/interview/dojo assessments, multi-dimensional score computation (communication/technical/cultural_fit), belt eligibility determination, bias/fairness monitoring, model version management, score explanations (DPDPA 2023 right-to-explanation), and candidate rights processing.

Built from `Scoring_Engine_Technical_Specification.docx` (v1.0, March 2026).

---

## Agents (18)

| # | Tool Name | Core Responsibility |
|---|-----------|-------------------|
| 1 | `score_submit_request` | Queue assessment for async ML scoring; model version traffic split; idempotent |
| 2 | `score_get` | Retrieve scores from Redis cache (TTL 1h) → PostgreSQL; full dimension + feature importance |
| 3 | `gd_score_compute` | Score GD session: per-participant communication analysis; audio quality confidence multiplier |
| 4 | `interview_score_compute` | Hybrid scoring: 70% AI + 30% interviewer feedback; Q&A rubric evaluation |
| 5 | `dojo_score_compute` | Code challenge scoring: solvedness + test pass rate + code quality (cyclomatic complexity) |
| 6 | `score_rescore` | Re-score with newer model; DPDPA right-to-re-evaluation; cache invalidation |
| 7 | `belt_eligibility_determine` | Evaluate belt advancement (bronze→silver→...→master); publishes belt.eligibility.determined |
| 8 | `score_explanation_generate` | DPDPA 2023 right-to-explanation; feature importance per dimension; improvement tips |
| 9 | `bias_detection_run` | Disparate impact ratio (must be > 0.8); score variance by demographic (must be < 5%) |
| 10 | `model_version_manage` | Traffic split routing; promote/rollback; gradual rollout 10%→50%→100% |
| 11 | `model_performance_metrics` | Accuracy, F1 per dimension, latency p50/p95/p99, throughput, fairness metrics |
| 12 | `score_history_get` | Candidate scoring history; trend analysis; best-of-3 support |
| 13 | `scoring_audit_log_query` | Immutable audit trail (DPDPA/SOC 2); assessment_id + model_version + feature_importance |
| 14 | `queue_status_get` | Kafka consumer lag; HPA pod status; estimated wait time; SLA monitoring |
| 15 | `score_manual_override` | Admin score correction; original preserved; audit logged; belt re-evaluated |
| 16 | `dimension_weight_update` | Per-job-domain weights (must sum = 100%); optional batch rescore |
| 17 | `service_health` | PostgreSQL + Redis + Kafka + MinIO + ML model health; Prometheus metrics summary |
| 18 | `candidate_rights_request` | DPDPA 2023: right_to_explanation, right_to_access, right_to_erasure, right_to_re_evaluation |

---

## Scoring Model

### Overall Score Formula
```
overall = (weightComm/100 × comm_score) + (weightTech/100 × tech_score) + (weightCult/100 × cult_score)
```

### Default Weights
| Dimension | Default | Engineering Role | Sales Role |
|-----------|---------|-----------------|------------|
| Communication | 35% | 30% | 40% |
| Technical | 40% | 50% | 25% |
| Cultural Fit | 25% | 20% | 35% |

### Belt Thresholds
| Belt | Min Overall Score |
|------|-----------------|
| Bronze | 40+ |
| Silver | 55+ |
| Gold | 70+ |
| Platinum | 80+ |
| Diamond | 90+ |
| Master | 95+ |

### Dojo Score Components (Technical Only)
- Problem solved: 50 pts
- Test pass rate: 25 pts (tests_passed_pct × 0.25)
- Code quality: 25 pts (low cyclomatic complexity + test coverage)

### GD Audio Quality Multipliers
- Good audio: 1.0x confidence
- Degraded audio: 0.93x confidence
- Poor audio: 0.85x confidence

---

## Security Architecture

| Layer | Implementation |
|-------|---------------|
| **JWT Validation** | HMAC-SHA256 (HS256), 5-min cache, constant-time comparison |
| **Assessment Type** | Enum allowlist: gd\|interview\|match\|dojo |
| **Belt Level** | Enum allowlist: bronze\|silver\|gold\|platinum\|diamond\|master |
| **Model Version** | Regex: v{1-3}(.{1-3})?(-{1-20})? |
| **Dimension** | Enum: communication\|technical\|cultural_fit\|overall |
| **Score Range** | Enforced: 0-100 for all scores |
| **Weight Sum** | Must equal exactly 100% (validated at update + config) |
| **Payload Guard** | 64KB cap + null byte detection |
| **Rate Limiting** | Token-bucket: 100/sec, 1000/min |
| **Audit Logging** | SOC 2 + DPDPA 2023 compliant; structured to stderr |
| **Container** | Non-root (UID 1000), read-only FS, all capabilities dropped |
| **Network** | NetworkPolicy: PostgreSQL (5432), Redis (6379), Kafka (9092), MinIO (9000) |

---

## Kafka Topics

| Event Consumed | Producer | Purpose |
|---------------|---------|---------|
| `gd.completed` | gd-orchestrator | GD scoring trigger |
| `interview.completed` | interview-service | Interview scoring trigger |
| `match.completed` | dojo-match-engine | Dojo scoring trigger |
| `score_request` | admin/system | Manual re-scoring |

| Event Produced | Consumers | Purpose |
|---------------|----------|---------|
| `score.computed` | application-service, recruiter-service, analytics-service | Scores available |
| `belt.eligibility.determined` | certification-engine | Belt certificate issuance |
| `scoring.failed` | admin-service (DLQ) | Failure handling |
| `model.performance.metrics` | analytics-service | Hourly model health |
| `score.explanation.generated` | candidate-service | DPDPA right-to-explanation |

---

## Build & Run

```bash
mvn package -DskipTests
export SCORING_JWT_SECRET="your-keycloak-secret-min-32-chars"
java -jar target/scoring-engine-mcp-server-1.0.0.jar

# Run 30 tests
mvn test
```

## Claude Desktop

```json
{
  "mcpServers": {
    "mcp-scoring-engine-ecoskiller": {
      "command": "java",
      "args": ["-jar", "/path/to/target/scoring-engine-mcp-server-1.0.0.jar"],
      "env": { "SCORING_JWT_SECRET": "your-secret" }
    }
  }
}
```

---

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SCORING_JWT_SECRET` | *(required)* | Keycloak HMAC-SHA256 JWT signing secret |
| `SCORING_SLA_MINUTES` | `15` | Scoring SLA window |
| `SCORING_CACHE_TTL_SECONDS` | `3600` | Redis score cache TTL (1 hour) |
| `SCORING_WEIGHT_COMMUNICATION` | `35` | Communication dimension weight (%) |
| `SCORING_WEIGHT_TECHNICAL` | `40` | Technical dimension weight (%) |
| `SCORING_WEIGHT_CULTURAL_FIT` | `25` | Cultural fit weight (%) — must sum to 100 |
| `SCORING_FAIRNESS_THRESHOLD` | `0.8` | Minimum disparate impact ratio |
| `SCORING_BIAS_VARIANCE_MAX` | `5` | Max score variance by demographic (%) |
| `SCORING_MODEL_PRODUCTION_VER` | `v1` | Production model version |
| `SCORING_CANDIDATE_TRAFFIC_PCT` | `10` | % traffic to candidate model |
| `SCORING_CONFIDENCE_THRESHOLD` | `0.6` | Min confidence before flagging for review |

---

## Protocol

- Transport: **stdio** (stdin/stdout) | Format: **JSON-RPC 2.0** | MCP Version: **2024-11-05**
- All stdout: pure JSON-RPC | All logs: stderr (ELK-ready)
- Namespace: **analytics** | SLA: 99% scored within 15 min | HPA: 3-20 replicas

---

*Ecoskiller Scoring Engine MCP Server v1.0.0 | March 2026 | CAT-SCORING-ENGINE*
