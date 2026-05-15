# MCP-04 AI Governance Server
## Ecoskiller — CAT-04: AI & Intelligence Governance

**Priority:** HIGH | **Namespace:** `core` | **Port:** `8004`

---

## Agents (12)

| # | Agent | Tools | Purpose |
|---|-------|-------|---------|
| 1 | `AI_PERMISSION_FIREWALL_AGENT` | 3 | Policy enforcement for AI feature/model/data access |
| 2 | `BEHAVIOR_ANALYTICS_AGENT` | 3 | Drift detection, prompt injection, behavioral anomaly |
| 3 | `COMMUNITY_HEALTH_AGENT` | 3 | Toxicity scoring, misinformation, cohesion metrics |
| 4 | `ETHICS_AGENT` | 3 | Fairness, bias audit, EU AI Act / NIST compliance reports |
| 5 | `HUMAN_AI_INTERFACE_AGENT` | 3 | HITL deferral logic, override recording, feedback loops |
| 6 | `INCENTIVE_AGENT` | 3 | XP/reward program design, anti-gaming, eligibility evaluation |
| 7 | `ML_REHYDRATION_AGENT` | 3 | Model restore from checkpoints, validation gates |
| 8 | `ML_ROUTING_AGENT` | 3 | Intelligent request routing, cost/latency/accuracy policies |
| 9 | `MODEL_RISK_AGENT` | 3 | Risk assessment, live monitoring, risk score history |
| 10 | `POWER_BALANCE_AGENT` | 3 | Gini analysis, algorithmic power redistribution |
| 11 | `REPUTATION_AGENT` | 3 | Trust scoring, reputation events, recovery roadmaps |
| 12 | `STRATEGIC_SIMULATION_AGENT` | 4 | Monte Carlo simulations, stress tests, scenario comparison |

**Total tools: 37**

---

## Running

```bash
mvn spring-boot:run
# Server starts at http://localhost:8004
```

---

## MCP Protocol — Quick Reference

### Initialize
```bash
curl -X POST http://localhost:8004/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":"1","method":"initialize","params":{"protocolVersion":"2024-11-05"}}'
```

### List all tools
```bash
curl -X POST http://localhost:8004/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":"2","method":"tools/list","params":{}}'
```

### Example: Run ethics bias audit
```bash
curl -X POST http://localhost:8004/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc":"2.0","id":"3","method":"tools/call",
    "params":{
      "name":"ethics_bias_audit",
      "arguments":{
        "tenant_id":"acme-school",
        "model_id":"skill-rank-v3",
        "protected_attributes":"gender,age,caste",
        "metric":"demographic_parity"
      }
    }
  }'
```

### Example: Run a strategic simulation
```bash
curl -X POST http://localhost:8004/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc":"2.0","id":"4","method":"tools/call",
    "params":{
      "name":"simulation_run",
      "arguments":{
        "tenant_id":"acme-school",
        "simulation_type":"USER_GROWTH",
        "time_horizon_days":90,
        "monte_carlo_runs":1000
      }
    }
  }'
```

### Example: Evaluate firewall policy
```bash
curl -X POST http://localhost:8004/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc":"2.0","id":"5","method":"tools/call",
    "params":{
      "name":"firewall_evaluate_request",
      "arguments":{
        "tenant_id":"acme-school",
        "user_id":"teacher-001",
        "model_id":"claude-3-sonnet",
        "feature":"inference",
        "data_scope":"INTERNAL"
      }
    }
  }'
```

---

## Health & Info

```
GET http://localhost:8004/mcp/health
GET http://localhost:8004/mcp/info
GET http://localhost:8004/actuator/health
```

---

## Tech Stack

- Java 21 | Spring Boot 3.2 | Maven
- Protocol: MCP JSON-RPC 2.0 (version 2024-11-05)
- Port: 8004

---

*Ecoskiller Engineering | Sprint 2 — mcp-04-ai-governance*
