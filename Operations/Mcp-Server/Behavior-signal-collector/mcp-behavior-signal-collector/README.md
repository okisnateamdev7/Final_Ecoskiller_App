# mcp-behavior-signal-collector

**Ecoskiller | behavior-signal-collector**  
MCP Server in Java | 20 Tools | Priority: HIGH | Namespace: realtime

Real-time behavioral signal ingestion, feature extraction, anomaly detection (IQR),
cohort normalization, multimodal fusion, drift detection, Kafka publishing,
DPDP 2023 / GDPR fairness auditing — complete ML signal pipeline in one MCP server.

---

## Tools (20)

### Ingestion
| # | Tool | Roles |
|---|------|-------|
| 1 | `ingest_signals` | SERVICE_ACCOUNT, CANDIDATE, RECRUITER, ADMIN |
| 2 | `ingest_kafka_event` | SERVICE_ACCOUNT, ADMIN |
| 3 | `configure_assessment` | RECRUITER, ADMIN |

### Processing (Feature Extraction, Normalization, Anomaly Detection)
| # | Tool | Roles |
|---|------|-------|
| 4 | `extract_features` | Any authenticated |
| 5 | `normalize_signals` | Any authenticated |
| 6 | `detect_anomalies` | Any authenticated |
| 7 | `aggregate_signals` | SERVICE_ACCOUNT, ADMIN |

### Pipeline
| # | Tool | Roles |
|---|------|-------|
| 8 | `run_full_pipeline` | SERVICE_ACCOUNT, ADMIN |
| 9 | `fuse_multimodal_signals` | Any authenticated |
| 10 | `detect_signal_drift` | SERVICE_ACCOUNT, ADMIN |
| 11 | `publish_aggregated_signals` | SERVICE_ACCOUNT, ADMIN |

### Query
| # | Tool | Roles |
|---|------|-------|
| 12 | `get_signal_quality` | RECRUITER, ADMIN |
| 13 | `get_assessment_signals` | Any (CANDIDATE: own signals only) |
| 14 | `replay_assessment` | ADMIN only |
| 15 | `get_metrics` | ADMIN, SERVICE_ACCOUNT |
| 16 | `get_kafka_event_log` | ADMIN, SERVICE_ACCOUNT |

### Fairness & Audit
| # | Tool | Roles |
|---|------|-------|
| 17 | `analyze_fairness` | ADMIN only |
| 18 | `add_cohort_data` | ADMIN, SERVICE_ACCOUNT |
| 19 | `get_audit_log` | ADMIN only |
| 20 | `get_quality_report` | RECRUITER, ADMIN |

---

## Signal Types & Validated Ranges

| Signal Type | Range | Source |
|---|---|---|
| `keyboard_latency` | 0–5000 ms | WEB_CLIENT |
| `mouse_movement_velocity` | 0–50000 px/s | WEB_CLIENT |
| `gaze_fixation_duration` | 0–30000 ms | EYE_TRACKER |
| `speech_pause_duration` | 0–60000 ms | INTERVIEW_RECORDER |
| `speech_rate_wpm` | 0–400 wpm | AUDIO_ANALYZER |
| `sentiment_score` | -1.0–1.0 | INTERVIEW_RECORDER |
| `code_commit_frequency` | 0–1000 | ASSESSMENT_SERVICE |
| `solution_revision_count` | 0–10000 | ASSESSMENT_SERVICE |
| `turn_taking_duration` | 0–600000 ms | GD_ORCHESTRATOR |
| `confidence_score` | 0–1.0 | AUDIO_ANALYZER |
| `gesture_velocity` | 0–100000 | MOBILE_APP |
| `filler_word_ratio` | 0–1.0 | AUDIO_ANALYZER |
| `interruption_count` | 0–500 | GD_ORCHESTRATOR |

---

## Signal Processing Pipeline

```
POST /signal/ingest
       │
       ▼
  [1] VALIDATE (range check, required fields)
       │
       ▼
  [2] DEDUPLICATE (message_id — Redis SET NX in prod)
       │
       ▼
  [3] FEATURE EXTRACT (mean, variance, p50, p95, skewness, trend)
       │
       ▼
  [4] NORMALIZE (z-score per cohort — prevents bias)
       │
       ▼
  [5] ANOMALY DETECT (IQR: Q1-1.5*IQR / Q3+1.5*IQR)
       │
       ▼
  [6] AGGREGATE (1-min tumbling window, count/mean/variance)
       │
       ▼
  [7] KAFKA PUBLISH → behavior.signals.aggregated
       │
       ▼
  [8] CLICKHOUSE WRITE (time-series, 7-year retention)
```

---

## Security Architecture

| Layer | Mechanism |
|---|---|
| Authentication | Keycloak Bearer JWT on every tool call |
| Authorization | RBAC: CANDIDATE / RECRUITER / ADMIN / SERVICE_ACCOUNT |
| Tenant isolation | `tenant_id` enforced on every query |
| Signal validation | Per-type value ranges enforced at ingestion |
| Input sanitisation | SQL injection + XSS pattern blocking |
| Deduplication | `message_id` idempotency (in-memory → Redis SET NX in prod) |
| Batch size cap | Max 1000 signals per ingest call (configurable) |
| Audit trail | Immutable PostgreSQL log (DPDP Act 2023 — 7-year retention) |

---

## Fairness & Bias Detection

The `analyze_fairness` tool computes:

1. **Distribution parity** — are signal distributions equal across cohorts?
2. **Disparate impact ratio** — 80% rule (US EEOC): if cohort B hire rate < 80% of cohort A → flag
3. **Fairness score** — 0–1 composite
4. **Recommended interventions**: COHORT_NORMALIZE, THRESHOLD_TUNE, SIGNAL_EXCLUDE, CAUSAL_ANALYSIS

Cohort normalization via `normalize_signals` ensures typing speed for engineers (40–100 wpm)
is normalized differently than designers (30–80 wpm), preventing demographic bias.

---

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `BSC_JWT_SECRET` | `ecoskiller-bsc-dev-secret` | HMAC-HS256 JWT signing secret |
| `BSC_STRICT_MODE` | `false` | `true` = enforce JWT signature verification |
| `BSC_MAX_BATCH_SIZE` | `1000` | Max signals per ingest call |
| `BSC_ANOMALY_IQR_MULT` | `1.5` | IQR multiplier for outlier detection |
| `BSC_QUALITY_THRESHOLD` | `0.85` | Min quality score before alert |

---

## Kafka Topics

| Topic | Direction | Consumer |
|---|---|---|
| `assessment.submission` | **IN** (consumed) | From assessment-service |
| `gd.speaker.turn` | **IN** (consumed) | From gd-orchestrator |
| `interview.recorder_event` | **IN** (consumed) | From interview-service |
| `behavior.signals.aggregated` | **OUT** (produced) | To scoring-engine |

---

## Requirements

- **Java 11+** — zero external dependencies (pure JDK)
- Maven 3.x (optional — for build only)

---

## Build

```bash
# Compile
javac -d target/classes $(find src -name "*.java")

# Package
printf 'Manifest-Version: 1.0\nMain-Class: com.ecoskiller.mcp.BehaviorSignalMCPServer\n\n' > manifest.mf
jar cfm target/mcp-behavior-signal-collector.jar manifest.mf -C target/classes .

# Or with Maven
mvn package -q
```

---

## Run

```bash
java -jar target/mcp-behavior-signal-collector.jar
```

Transport: **stdio** (JSON-RPC 2.0). Logs → stderr; stdout = MCP messages only.

---

## Connect to Claude Desktop

```json
{
  "mcpServers": {
    "mcp-behavior-signal-collector": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/mcp-behavior-signal-collector.jar"],
      "env": {
        "BSC_JWT_SECRET": "your-keycloak-realm-secret",
        "BSC_STRICT_MODE": "false",
        "BSC_MAX_BATCH_SIZE": "1000",
        "BSC_ANOMALY_IQR_MULT": "1.5",
        "BSC_QUALITY_THRESHOLD": "0.85"
      }
    }
  }
}
```

---

## Run Tests

```bash
java -cp target/mcp-behavior-signal-collector.jar com.ecoskiller.mcp.TestAgents
java -cp target/mcp-behavior-signal-collector.jar com.ecoskiller.mcp.TestAgents --verbose
```

Expected: **30/30 PASSED**

---

## File Structure

```
mcp-behavior-signal-collector/
├── pom.xml
├── README.md
├── claude_desktop_config.json
└── src/main/java/com/ecoskiller/mcp/
    ├── BehaviorSignalMCPServer.java     ← Entry point (stdio loop)
    ├── TestAgents.java                  ← 30-test integration suite
    ├── protocol/
    │   ├── MCPProtocolHandler.java      ← JSON-RPC 2.0 dispatcher
    │   └── JsonUtil.java                ← Zero-dependency JSON parser (+ PublicParser)
    ├── security/
    │   └── SignalSecurityConfig.java    ← JWT, RBAC, range validation,
    │                                       dedup, sanitisation, IQR config
    ├── models/
    │   ├── SignalModels.java            ← BehaviorSignal, SignalFeatures,
    │   │                                   AggregatedSignal, AnomalyResult
    │   └── SignalRepository.java        ← Thread-safe store (→ ClickHouse + PostgreSQL)
    └── tools/
        ├── SignalToolRouter.java        ← 20-tool dispatcher + tools/list
        ├── IngestionTools.java          ← ingest_signals, ingest_kafka_event, configure_assessment
        ├── ProcessingTools.java         ← extract_features, normalize_signals,
        │                                   detect_anomalies, aggregate_signals
        ├── PipelineTools.java           ← run_full_pipeline, fuse_multimodal_signals,
        │                                   detect_signal_drift, publish_aggregated_signals
        └── QueryFairnessTools.java      ← QueryTools + FairnessAuditTools
                                            (get_signal_quality, get_assessment_signals,
                                             replay_assessment, get_metrics, get_kafka_event_log,
                                             analyze_fairness, add_cohort_data,
                                             get_audit_log, get_quality_report)
```

---

## Production Migration Checklist

| Component | Dev | Production |
|---|---|---|
| JWT verification | HS256 dev-sig passthrough | RS256 + Keycloak JWKS |
| Signal store | In-memory ConcurrentHashMap | ClickHouse (time-series, partitioned by date) |
| Audit log | In-memory List | PostgreSQL `behavior_signal_audit_log` |
| Kafka consumer | Event payload parsing | KafkaJS consumer group (partition by assessment_id) |
| Kafka producer | In-memory log | KafkaJS → `behavior.signals.aggregated` |
| Deduplication | In-memory LinkedHashSet | Redis `SET message_id NX EX 86400` |
| Assessment config | In-memory Map | Redis cache (TTL 1h) → PostgreSQL |
| Cohort norm params | Hardcoded defaults | ML training pipeline → Redis |
| Feature extraction | Pure Java statistics | scipy/numpy Python sidecar (gRPC) |
| Anomaly detection | IQR method | IQR + IsolationForest (scikit-learn) |
| Stream aggregation | In-memory grouping | Apache Beam / Flink (30s checkpointing) |
