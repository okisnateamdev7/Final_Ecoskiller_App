# mcp-skill-evaluation-engine

**Ecoskiller | CAT-SEE — Competency Assessment & Real-Time Evaluation**  
MCP Server in Java | 22 Tools | Priority: HIGH | Security Level: HIGH

---

## Overview

The `skill-evaluation-engine` is Ecoskiller's mission-critical microservice for real-time competency
assessment. It evaluates candidates across **8 competency dimensions** during active assessment sessions
(Group Discussions, Interviews, Coding Challenges, Tests).

**Architecture Position:** Consumes WebSocket/Kafka streams from GD Orchestrator, interview-service,
and dojo-match-engine. Publishes `match.scored` Kafka events to belt-eligibility-service,
certification-engine, notification-service, and analytics-service.

---

## Tools (22)

| # | Tool Name | Category | Description |
|---|-----------|----------|-------------|
| 1 | `evaluate_session` | Core Evaluation | Start real-time evaluation for a session |
| 2 | `evaluate_candidate` | Core Evaluation | Aggregate weighted competency score |
| 3 | `get_competency_scores` | Core Evaluation | Retrieve cached 8-dimension scores |
| 4 | `get_competency_history` | Core Evaluation | Historical assessment data |
| 5 | `process_speech_signal` | Signal Processing | Analyze speech for communication signals |
| 6 | `process_code_submission` | Signal Processing | Evaluate code for technical depth |
| 7 | `process_behavioral_signal` | Signal Processing | GD participation patterns |
| 8 | `extract_nlp_features` | Signal Processing | NLP/BERT domain knowledge extraction |
| 9 | `start_evaluation_session` | Session Management | Create Redis-backed session |
| 10 | `end_evaluation_session` | Session Management | Finalize + publish Kafka event |
| 11 | `get_session_state` | Session Management | Real-time session status |
| 12 | `update_session_metadata` | Session Management | Update mutable session fields |
| 13 | `adjust_difficulty` | Adaptive Difficulty | Flow-state difficulty adjustment |
| 14 | `get_difficulty_level` | Adaptive Difficulty | Current difficulty + history |
| 15 | `detect_bias` | Bias & Fairness | Human vs algorithmic score variance |
| 16 | `get_fairness_report` | Bias & Fairness | Demographic distribution analysis |
| 17 | `get_skill_progression` | Skill Progression | Longitudinal growth tracking |
| 18 | `detect_skill_regression` | Skill Progression | Regression detection + alerts |
| 19 | `get_competency_framework` | Algorithm Config | Tenant scoring framework |
| 20 | `update_scoring_weights` | Algorithm Config | Update dimension weights |
| 21 | `health_check` | Observability | K8s liveness probe |
| 22 | `get_metrics` | Observability | Prometheus metrics |

---

## 8 Competency Dimensions

| Dimension | Measurement Sources | Default Weight |
|-----------|---------------------|----------------|
| Technical Depth | Code quality, algorithms, design patterns | 20% |
| Communication | Speech clarity, vocabulary, confidence | 15% |
| Problem-Solving | Solution correctness, creativity, efficiency | 15% |
| Collaboration | Turn-taking equity, idea amplification | 12% |
| Leadership | Proposal adoption, group influence | 12% |
| Adaptability | Error recovery, approach flexibility | 10% |
| Domain Knowledge | Terminology accuracy, contextual depth | 10% |
| Innovation | Solution novelty, improvement proposals | 6% |

---

## Assessment Type Weights

| Type | Weight | Description |
|------|--------|-------------|
| GD (Group Discussion) | 35% | Voice-based group discussion |
| Interview | 40% | Structured 1:1 video interview |
| Coding Challenge | 20% | Dojo match competitive coding |
| Test | 5% | Competency knowledge test |

---

## Security Features

| Feature | Implementation |
|---------|---------------|
| **JWT Validation** | Keycloak RS256 token validation (alg:none blocked) |
| **Rate Limiting** | 100 req/min sliding window per client (configurable) |
| **Input Sanitization** | Control character stripping, length limits (4096 chars) |
| **Tenant Isolation** | PostgreSQL RLS + Redis key prefixing by tenant_id |
| **Tenant ID Validation** | Allowlist regex: `^[a-zA-Z0-9_-]{1,64}$` |
| **Tool Allowlisting** | Only registered tools can be called (no dynamic dispatch) |
| **Admin Tool Protection** | JWT required for: detect_bias, get_fairness_report, update_scoring_weights, get_competency_framework |
| **Jackson Hardening** | Default typing disabled (prevents gadget RCE attacks) |

---

## Requirements

- **Java 17+**
- **Maven 3.8+**

No external services needed to run the MCP server in standalone mode.

---

## Build

```bash
mvn clean package -q
```

Produces: `target/mcp-skill-evaluation-engine-1.0.0.jar`

---

## Run the Server

```bash
java -jar target/mcp-skill-evaluation-engine-1.0.0.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).  
Logs go to **stderr** (stdout is reserved for MCP protocol).

---

## Run Tests

```bash
mvn test
```

26 tests covering tool logic, weighted aggregation, bias detection, JWT security,
rate limiting, input sanitization, and edge cases.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-skill-evaluation-engine": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-skill-evaluation-engine-1.0.0.jar"]
    }
  }
}
```

---

## Environment Variables (Configuration)

| Variable | Default | Description |
|----------|---------|-------------|
| `MCP_ENV` | `dev` | Environment: dev\|test\|stage\|prod |
| `MCP_JWT_VALIDATION_ENABLED` | `false` | Enable Keycloak JWT validation |
| `MCP_KEYCLOAK_ISSUER` | `https://auth.ecoskiller.com/realms/` | Keycloak realm base URL |
| `MCP_RATE_LIMIT_PER_MINUTE` | `100` | Max requests per minute |
| `MCP_LOG_LEVEL` | `INFO` | Log level: DEBUG\|INFO\|WARNING\|ERROR |

---

## File Structure

```
mcp-skill-evaluation-engine/
├── pom.xml                          ← Maven build (Java 17, uber-JAR)
├── README.md
└── src/
    ├── main/
    │   ├── java/com/ecoskiller/mcp/
    │   │   ├── server/
    │   │   │   └── SkillEvaluationMcpServer.java  ← Main MCP server (22 tools)
    │   │   ├── tools/
    │   │   │   ├── McpTool.java                   ← Tool interface
    │   │   │   ├── BaseTool.java                  ← Shared validation/sanitization
    │   │   │   ├── EvaluateSessionTool.java
    │   │   │   ├── EvaluateCandidateTool.java
    │   │   │   └── AllTools.java                  ← Remaining 18 tools
    │   │   ├── security/
    │   │   │   ├── JwtValidator.java              ← Keycloak JWT validation
    │   │   │   └── RateLimiter.java               ← Sliding window rate limiter
    │   │   ├── config/
    │   │   │   └── ServerConfig.java              ← Env-var driven configuration
    │   │   └── util/
    │   │       └── JsonUtil.java                  ← Hardened Jackson mapper
    │   └── resources/
    │       └── config.properties
    └── test/
        └── java/com/ecoskiller/mcp/
            └── SkillEvaluationEngineTest.java     ← 26 tests
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Kafka Events Published

| Event | Trigger | Consumers |
|-------|---------|-----------|
| `match.scored` | Session end (COMPLETED) | belt-eligibility-service, certification-engine, notification-service, analytics-service |
| `competency.evaluated` | Each dimension evaluation | Audit trail, fairness monitoring |
| `skill.regression_detected` | Score drops >10 pts | Coaching service, notifications |
| `assessment.quality_flagged` | Anomalous scoring detected | Admin review queue |
