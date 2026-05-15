# mcp-20-trust — Trust, Identity & Safeguards

**Ecoskiller MCP Server | CAT-20 | Namespace: `core` | Priority: `HIGH`**

---

## Overview

This MCP server hosts **10 agents** responsible for the full trust, identity, and safeguards layer of the Ecoskiller platform.

---

## Agents (10)

| # | Agent Name | Responsibility |
|---|-----------|----------------|
| 1 | `AGENT_ACCESS_PERMISSION_FIREWALL` | Fine-grained permission enforcement |
| 2 | `AGENT_FAILURE_RECOVERY_AGENT` | Detects and recovers from system failures |
| 3 | `CHILD_PROTECTION_EVIDENCE_AGENT` | Child safety incident management |
| 4 | `CONSENT_AND_PARENT_PERMISSION_AGENT` | COPPA/GDPR parental consent flows |
| 5 | `CROSS_PLATFORM_TRUST_SCORE_AGENT` | Unified trust scores across platforms |
| 6 | `EVIDENCE_PRESERVATION_AGENT` | Immutable evidence storage with chain-of-custody |
| 7 | `IDEA_DISPUTE_RESOLUTION_AGENT` | IP/idea ownership dispute handling |
| 8 | `IDENTITY_ASSURANCE_AGENT` | KYC, IAL levels, fraud detection |
| 9 | `USER_RIGHTS_EXPLANATION_AGENT` | DPDPA/GDPR user rights and DSAR management |
| 10 | `VENDOR_RISK_EVALUATION_AGENT` | Third-party vendor risk scoring |

---

## Quick Start

```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/mcp-20-trust-1.0.0.jar

# Server starts on http://localhost:8220/mcp-20-trust
```

---

## API Reference

### Server Info
```
GET /mcp-20-trust/
```

### Health Check
```
GET /mcp-20-trust/health
```

### List All Agents
```
GET /mcp-20-trust/agents
```

### Agent Health
```
GET /mcp-20-trust/agents/{AGENT_NAME}/health
```

### Invoke Agent
```
POST /mcp-20-trust/agents/{AGENT_NAME}/invoke

Headers:
  X-Tenant-Id: <tenant-id>
  X-User-Id:   <user-id>
  Content-Type: application/json

Body:
{
  "requestId": "optional-uuid",
  "action":    "ACTION_NAME",
  "payload":   { ...action-specific fields... }
}
```

---

## Example Calls

### Check Permission
```bash
curl -X POST http://localhost:8220/mcp-20-trust/agents/AGENT_ACCESS_PERMISSION_FIREWALL/invoke \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: tenant-001" \
  -H "X-User-Id: user-abc" \
  -d '{
    "action": "CHECK_PERMISSION",
    "payload": {
      "resource": "skills",
      "operation": "READ"
    }
  }'
```

### Compute Trust Score
```bash
curl -X POST http://localhost:8220/mcp-20-trust/agents/CROSS_PLATFORM_TRUST_SCORE_AGENT/invoke \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: tenant-001" \
  -H "X-User-Id: user-abc" \
  -d '{
    "action": "COMPUTE_SCORE",
    "payload": {
      "entityId": "user-abc",
      "entityType": "USER"
    }
  }'
```

### Verify Identity
```bash
curl -X POST http://localhost:8220/mcp-20-trust/agents/IDENTITY_ASSURANCE_AGENT/invoke \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: tenant-001" \
  -H "X-User-Id: admin" \
  -d '{
    "action": "VERIFY_IDENTITY",
    "payload": {
      "subjectUserId": "user-xyz",
      "idType": "AADHAAR",
      "idRef": "XXXX-XXXX-1234"
    }
  }'
```

### Request Parental Consent
```bash
curl -X POST http://localhost:8220/mcp-20-trust/agents/CONSENT_AND_PARENT_PERMISSION_AGENT/invoke \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: tenant-001" \
  -H "X-User-Id: child-001" \
  -d '{
    "action": "REQUEST_CONSENT",
    "payload": {
      "childUserId": "child-001",
      "parentEmail": "parent@example.com",
      "consentScope": "FULL_PLATFORM_ACCESS"
    }
  }'
```

---

## Configuration

```yaml
# application.yml
server:
  port: 8220
  servlet:
    context-path: /mcp-20-trust
```

---

## Tech Stack

- **Java 17**
- **Spring Boot 3.2**
- **Maven**

---

## Related MCP Servers

| Server | Category | Link |
|--------|----------|------|
| `mcp-03-security` | Security & Tenancy | CAT-03 |
| `mcp-02-governance` | Governance & Compliance | CAT-02 |
| `mcp-21-policy` | Policy Engine | CAT-21 |
| `mcp-18-core-control` | Core Control | CAT-18 |

---

*Ecoskiller Engineering | mcp-20-trust v1.0.0*
