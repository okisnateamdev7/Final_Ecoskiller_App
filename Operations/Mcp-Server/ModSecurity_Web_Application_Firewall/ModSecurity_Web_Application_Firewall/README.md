# mcp-waf-modsecurity

**Ecoskiller | ModSecurity Web Application Firewall**  
MCP Server in Java | 18 Agents | Priority: HIGH  
Architecture: Kubernetes Multi-Cloud (GCP + AWS)

---

## Agents (18)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1 | `inspect_request` | INSPECT_REQUEST_AGENT |
| 2 | `detect_sql_injection` | DETECT_SQL_INJECTION_AGENT |
| 3 | `detect_xss` | DETECT_XSS_AGENT |
| 4 | `detect_command_injection` | DETECT_COMMAND_INJECTION_AGENT |
| 5 | `rate_limit_check` | RATE_LIMIT_AGENT |
| 6 | `detect_bot` | DETECT_BOT_AGENT |
| 7 | `inspect_response` | INSPECT_RESPONSE_AGENT |
| 8 | `audit_log_query` | AUDIT_LOG_AGENT |
| 9 | `evaluate_custom_rule` | CUSTOM_RULE_AGENT |
| 10 | `get_anomaly_score` | ANOMALY_SCORE_AGENT |
| 11 | `block_ip` | BLOCK_IP_AGENT |
| 12 | `whitelist_ip` | WHITELIST_IP_AGENT |
| 13 | `geo_ip_check` | GEO_IP_AGENT |
| 14 | `detect_data_leak` | DATA_LEAK_AGENT |
| 15 | `get_security_events` | SECURITY_EVENTS_AGENT |
| 16 | `update_rules` | UPDATE_RULES_AGENT |
| 17 | `health_check` | HEALTH_CHECK_AGENT |
| 18 | `get_waf_stats` | WAF_STATS_AGENT |

---

## Security Features

### Attack Detection (OWASP CRS)
| Layer | Coverage | CRS Rules |
|-------|----------|-----------|
| SQL Injection | UNION SELECT, DROP, EXEC, blind, time-based | 942xxx |
| XSS | `<script>`, event handlers, `javascript:`, encoded variants | 941xxx |
| Command Injection | Shell metacharacters, `$()`, backticks, `&&` | 930xxx |
| Path Traversal | `../`, `%2e%2e/`, double-encoded | 930xxx |
| Bot Detection | User-Agent fingerprint, rate patterns, scanner tools | Custom |
| Data Leaks | Credit card, SSN, API keys, passwords in responses | 1005xxx |

### Defence-in-Depth Layers
1. **Phase 1** – HTTP header inspection (User-Agent, Host, Auth)
2. **Phase 2** – Request body inspection (JSON/form/XML payloads)
3. **Phase 3** – URL & query parameter analysis
4. **Phase 4** – Response header inspection
5. **Phase 5** – Response body PII/data-leak scan
6. **Anomaly Scoring** – Accumulate points across rules; auto-block at ≥ 50
7. **Rate Limiting** – Sliding 60-second window (1 000 req/min general; 5/min login)
8. **IP Blocklist / Whitelist** – Admin-controlled + auto-block on anomaly threshold
9. **Geo-IP** – Country allowlist enforcement (default: India / `IN`)
10. **Audit Trail** – Last 500 requests logged with PII masking

### PII Masking (always applied in logs)
- Credit card numbers → `####-####-####-XXXX`
- SSN → `***-**-****`
- Passwords → `[REDACTED]`
- API keys / secrets → `[REDACTED]`

---

## Requirements

- **Java 11+** (tested on Java 21 OpenJDK)
- No external dependencies — pure Java stdlib only

---

## Build & Run

```bash
# Option 1: Run via source
chmod +x build.sh
./build.sh run          # start MCP server (stdin/stdout)

# Option 2: Run via pre-built JAR
java -jar mcp-waf-modsecurity.jar

# Option 3: Direct javac
mkdir -p out
javac -d out src/com/ecoskiller/waf/McpWafServer.java
java  -cp out com.ecoskiller.waf.McpWafServer
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Run Tests

```bash
./build.sh test             # quick pass/fail  (42 test cases)
./build.sh test --verbose   # with full JSON output

# Or directly:
javac -cp out -d out src/com/ecoskiller/waf/test_agents.java
java  -cp out com.ecoskiller.waf.test_agents
```

Expected output: **PASSED: 42 | FAILED: 0 | TOTAL: 42**

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-waf-modsecurity": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/mcp-waf-modsecurity/mcp-waf-modsecurity.jar"
      ]
    }
  }
}
```

---

## File Structure

```
mcp-waf-modsecurity/
├── src/
│   └── com/ecoskiller/waf/
│       ├── McpWafServer.java       ← Main MCP server (all 18 agents + JSON-RPC)
│       └── test_agents.java        ← 42 test cases for all agents
├── out/                            ← Compiled .class files (after build)
├── mcp-waf-modsecurity.jar         ← Runnable JAR
├── build.sh                        ← Build & run script
├── claude_desktop_config.json      ← Claude Desktop config snippet
└── README.md
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Example Calls

### Detect SQL Injection
```json
{
  "jsonrpc": "2.0", "id": 1, "method": "tools/call",
  "params": {
    "name": "detect_sql_injection",
    "arguments": { "input": "1' UNION SELECT username,password FROM users--" }
  }
}
```

### Full Request Inspection
```json
{
  "jsonrpc": "2.0", "id": 2, "method": "tools/call",
  "params": {
    "name": "inspect_request",
    "arguments": {
      "method": "POST",
      "uri": "/api/assessments/123/submit",
      "headers": { "User-Agent": "Mozilla/5.0", "Host": "api.ecoskiller.com" },
      "body": "{\"answer\": \"1' OR '1'='1\"}",
      "client_ip": "198.51.100.45"
    }
  }
}
```

### Block an IP
```json
{
  "jsonrpc": "2.0", "id": 3, "method": "tools/call",
  "params": {
    "name": "block_ip",
    "arguments": { "action": "block", "client_ip": "198.51.100.45", "reason": "repeated SQLi attempts" }
  }
}
```

---

## Compliance

| Standard | Coverage |
|----------|----------|
| OWASP Top 10 | A01-A10 via CRS rule groups |
| DPDP Act 2023 | PII detection + audit logging |
| GDPR | Data-leak prevention + PII masking |
| SOC2 | Immutable audit trail, access control |

---

## Architecture Integration

```
Internet → NGINX Ingress (TLS) → ModSecurity WAF (THIS SERVER)
                                      ↓ (allowed)
                         Assessment / Candidate / Interview Services
                                      ↓ (audit)
                         Elasticsearch + Kafka (waf-security-events)
```
