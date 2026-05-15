# mcp-ips-intelligence-profile (Java)

**Ecoskiller | Intelligence Profile Service — MCP Server**  
Java 17 | 15 Agents | Priority: CRITICAL | Secure

---

## Overview

Intelligence Profile Service (IPS) builds, maintains, and enriches permanent
**Intelligence DNA profiles** (8-type vector) for every Ecoskiller user.
It aggregates behavioral signals, assessment performance, skill vectors, and
learning patterns — enabling data-driven recruitment, predictive candidate
matching, and fairness-monitored hiring.

---

## Agents (15)

| # | Tool Name | Agent | Required Role |
|---|-----------|-------|---------------|
| 1 | `get_intelligence_profile` | PROFILE_READ_AGENT | admin, recruiter, candidate, ml_system |
| 2 | `get_skill_vectors` | SKILL_VECTOR_AGENT | admin, recruiter, candidate, ml_system |
| 3 | `search_candidates` | CANDIDATE_SEARCH_AGENT | admin, recruiter |
| 4 | `get_peer_benchmarks` | PEER_BENCHMARK_AGENT | admin, recruiter, candidate, ml_system |
| 5 | `get_risk_assessment` | RISK_ASSESSMENT_AGENT | admin, recruiter |
| 6 | `process_assessment_event` | ASSESSMENT_EVENT_AGENT | admin, ml_system |
| 7 | `process_skill_assessment` | SKILL_ASSESSMENT_AGENT | admin, ml_system |
| 8 | `process_gd_discussion` | GD_DISCUSSION_AGENT | admin, ml_system |
| 9 | `process_learning_interaction` | LEARNING_EVENT_AGENT | admin, ml_system |
| 10 | `compute_intelligence_dna` | DNA_COMPUTE_AGENT | admin, ml_system |
| 11 | `detect_profile_anomaly` | ANOMALY_DETECTION_AGENT | admin, ml_system, recruiter |
| 12 | `recalculate_vectors` | VECTOR_RECALC_AGENT | admin only |
| 13 | `get_fairness_audit` | FAIRNESS_AUDIT_AGENT | admin only |
| 14 | `rebuild_profiles` | PROFILE_REBUILD_AGENT | admin only |
| 15 | `get_profile_history` | HISTORY_AGENT | admin, recruiter |

---

## Security Architecture

### JWT Authentication (every tool call)
All tools require `_auth.token` — a valid JWT bearer token:

```json
{
  "_auth": { "token": "<JWT>" },
  "userId": "<uuid>",
  "tenantId": "<uuid>"
}
```

JWT is validated for:
- ✅ Structure (3 parts: header.payload.signature)
- ✅ Expiry (`exp` claim)
- ✅ Issuer trust (`iss` must contain "ecoskiller")
- ✅ Tenant isolation (`tenantId` in token must match request)
- ✅ RBAC roles (`roles` claim against tool permission matrix)
- ✅ Candidate self-access (`candidate` role can only read own profile)
- ✅ Input sanitization (SQL meta-chars, script injection stripped)
- ✅ UUID validation on all ID fields
- ✅ Rate limiting (120 req/min per token)

### RBAC Role Hierarchy
```
admin       → all 15 tools
recruiter   → read tools + anomaly detection + history
ml_system   → event processing + compute + admin tools excluded
candidate   → own profile + own skills + peer benchmarks only
```

### Multi-Tenant Isolation
- Every query is scoped to `tenantId`
- JWT `tenantId` claim must match request `tenantId`
- In production: PostgreSQL Row-Level Security enforces tenant boundary at DB layer
- Kafka events validated for tenant match before processing

---

## Intelligence DNA — 8-Type Vector

Each user profile encodes intelligence across 8 normalized [0,1] dimensions:

| Dimension | Description |
|-----------|-------------|
| `cognitive` | Problem-solving, coding, logic |
| `behavioral` | Collaboration, communication, leadership |
| `domainExpertise` | Technical skill depth per domain |
| `learningAgility` | Ability to acquire new skills |
| `personality` | Big Five traits |
| `trajectory` | Career progression patterns |
| `risk` | Churn probability, red flags |
| `uniqueness` | Differentiation from peer group |

---

## Requirements

- **Java 17+**
- **Maven 3.8+** (for building)
- No runtime dependencies beyond Jackson (bundled in fat JAR)

---

## Build

```bash
cd mcp-ips-java
mvn clean package -q
# Output: target/mcp-ips-intelligence-profile-1.0.0.jar
```

---

## Run the Server

```bash
java -jar target/mcp-ips-intelligence-profile-1.0.0.jar
```

Server communicates via **stdin/stdout** (JSON-RPC 2.0).  
All logs go to **stderr** — stdout is clean for MCP protocol.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-ips-intelligence-profile": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/mcp-ips-intelligence-profile-1.0.0.jar"]
    }
  }
}
```

---

## Run Tests

Tests call all 15 agents directly (no subprocess required):

```bash
# Compile first
mvn compile -q

# Quick pass/fail
mvn exec:java -Dexec.mainClass="com.ecoskiller.mcp.ips.TestAgents"

# With full JSON output
mvn exec:java -Dexec.mainClass="com.ecoskiller.mcp.ips.TestAgents" -Dexec.args="--verbose"
```

---

## File Structure

```
mcp-ips-java/
├── pom.xml
├── claude_desktop_config.json
├── README.md
└── src/main/java/com/ecoskiller/mcp/ips/
    ├── server/
    │   └── McpServer.java              ← Main MCP server (JSON-RPC dispatcher)
    ├── security/
    │   └── SecurityValidator.java      ← JWT + RBAC + rate-limit + input sanitizer
    ├── tools/
    │   ├── ToolRegistry.java           ← Tool registration + JSON schema definitions
    │   ├── IntelligenceProfileTools.java  ← Tools 1-5 (profile reads)
    │   ├── EventProcessingTools.java   ← Tools 6-9 (Kafka event ingestion)
    │   ├── AnalyticsTools.java         ← Tools 10-11, 15 (DNA, anomaly, history)
    │   └── AdminTools.java             ← Tools 12-14 (admin operations)
    └── TestAgents.java                 ← 15-agent test suite
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Production Upgrade Path

| Component | Current | Production |
|-----------|---------|------------|
| JWT validation | Base64 decode + claims parse | `nimbus-jose-jwt` + Keycloak JWKS |
| Profile storage | In-memory mock | PostgreSQL + Redis cache |
| Vector search | Mock results | Qdrant HNSW via REST |
| Kafka events | Simulated | KafkaJS consumer groups |
| ML inference | Deterministic mock | MLflow model registry |
| Rate limiting | In-memory Map | Redis sliding window |
