# mcp-02-governance — Ecoskiller Governance & Compliance MCP Server

**CAT-02 | Governance & Compliance**
**Namespace:** `core` | **Priority:** HIGH | **Agents:** 12

---

## Overview

Java Spring Boot MCP server for `mcp-02-governance`. Exposes **12 agents** as **MCP tools**
using Spring AI's MCP Server framework over HTTP/SSE transport on port `8082`.

---

## Agents & Tools

| # | Agent | Tools Exposed |
|---|-------|---------------|
| 1  | `AUDIT_COMPLIANCE_AGENT`          | run_compliance_check, get_audit_history, resolve_finding, generate_compliance_report |
| 2  | `BACKUP_DR_AGENT`                 | trigger_backup, verify_integrity, initiate_failover, get_replication_status, restore_tenant |
| 3  | `BILLING_GOVERNANCE_AGENT`        | validate_invoice, detect_anomaly, reconcile_revenue, enforce_subscription_limits |
| 4  | `DATA_GOVERNANCE_AGENT`           | classify_dataset, score_quality, handle_erasure_request, enforce_retention_policy |
| 5  | `DEVSECOPS_AGENT`                 | run_sast_scan, scan_container_image, detect_secrets, generate_sbom, enforce_security_gate |
| 6  | `FORENSICS_AGENT`                 | investigate_incident, get_user_activity, package_evidence |
| 7  | `GEO_COMPLIANCE_AGENT`            | check_data_residency, approve_cross_border_transfer, map_regulatory_requirements |
| 8  | `LEGAL_POLICY_AGENT`              | get_active_policies, trigger_reacceptance, place_hold, assess_policy_change_impact |
| 9  | `MODERATION_AGENT`                | check_content, take_action, handle_appeal, get_queue |
| 10 | `POLICY_EVOLUTION_AGENT`          | create_draft, approve_and_publish, get_diff, rollback |
| 11 | `TENANCY_GOVERNANCE_AGENT`        | audit_isolation, suspend_tenant, terminate_tenant, get_governance_summary |
| 12 | `ZERO_TRUST_AGENT`                | verify_identity, evaluate_device, enforce_least_privilege, detect_lateral_movement, configure_microsegmentation |

**Total tools: 49**

---

## Tech Stack

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring AI 1.0.0** (MCP Server)
- **Transport:** HTTP + SSE (ASYNC mode)
- **Port:** `8082`

---

## Project Structure

```
mcp-02-governance/
├── pom.xml
└── src/main/
    ├── java/com/ecoskiller/mcp/governance/
    │   ├── McpGovernanceApplication.java
    │   ├── config/
    │   │   └── McpServerConfig.java
    │   └── agents/
    │       ├── AuditComplianceAgent.java
    │       ├── BackupDrAgent.java
    │       ├── BillingGovernanceAgent.java
    │       ├── DataGovernanceAgent.java
    │       ├── DevsecopsAgent.java
    │       ├── ForensicsAgent.java
    │       ├── GeoComplianceAgent.java
    │       ├── LegalPolicyAgent.java
    │       ├── ModerationAgent.java
    │       ├── PolicyEvolutionAgent.java
    │       ├── TenancyGovernanceAgent.java
    │       └── ZeroTrustAgent.java
    └── resources/
        └── application.yml
```

---

## Build & Run

```bash
mvn clean package -DskipTests
java -jar target/mcp-02-governance-1.0.0.jar
```

---

## MCP Client Configuration

```json
{
  "mcpServers": {
    "ecoskiller-governance": {
      "url": "http://localhost:8082/sse",
      "type": "sse"
    }
  }
}
```

---

## MCP Endpoints

| Endpoint | Description |
|----------|-------------|
| `GET  http://localhost:8082/sse` | SSE connection |
| `POST http://localhost:8082/mcp/message` | MCP message endpoint |
| `GET  http://localhost:8082/actuator/health` | Health check |

---

*Ecoskiller Engineering | mcp-02-governance | Java Spring AI MCP Server*
