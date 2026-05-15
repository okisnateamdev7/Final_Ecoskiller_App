# trivy-mcp-server

**Ecoskiller | Trivy — Container Image Vulnerability Scanner**  
MCP Server in Java | 13 Tools | Security: HIGH | Protocol: JSON-RPC 2.0 / MCP 2024-11-05

---

## Overview

Java MCP server that exposes the full Ecoskiller Trivy security workflow as
Claude-callable tools. Mirrors the exact `scan:trivy` GitLab CI stage used in
the Ecoskiller pipeline — blocking CRITICAL/HIGH CVEs from reaching Harbor.

Covers:
- Offline `.tar` scanning (CI artifact model)
- Registry image scanning
- Filesystem / dependency scanning
- Dockerfile misconfiguration scanning
- Severity gate evaluation
- CVE lookup and `.trivyignore` management
- DPDPA 2023 / ISO 27001 compliance reports
- Pipeline status checks
- Policy management

---

## Tools (13)

| # | Tool Name | Description |
|---|-----------|-------------|
| 1 | `scan_image_tar` | Scan a Docker `.tar` artifact (mirrors CI `scan:trivy` stage) |
| 2 | `scan_image_registry` | Pull and scan from Harbor or Docker Hub |
| 3 | `scan_filesystem` | Scan a local directory for dependency vulnerabilities |
| 4 | `scan_config` | Scan Dockerfile/Helm/K8s configs for misconfigurations |
| 5 | `get_scan_report` | Retrieve a previous scan result by ID |
| 6 | `list_scan_history` | List recent scans in this session |
| 7 | `check_severity_gate` | Evaluate scan output against CRITICAL/HIGH policy |
| 8 | `get_cve_details` | Look up a specific CVE ID with NVD/OSV links |
| 9 | `add_trivyignore_entry` | Add a CVE to `.trivyignore` with documented justification |
| 10 | `list_trivyignore` | List current `.trivyignore` entries |
| 11 | `get_compliance_report` | Generate DPDPA 2023 / ISO 27001 compliance summary |
| 12 | `get_pipeline_status` | Check gate status for an image/environment |
| 13 | `update_severity_policy` | Update the active severity threshold policy |

---

## Security Architecture

### 1. Input Sanitization (`InputSanitizer.java`)
Every argument to every tool passes through `InputSanitizer.sanitize()` before execution:
- **Shell metacharacter blocking** — `;`, `&&`, `|`, `` ` ``, `$`, `()`, `<>`, etc.
- **Path traversal prevention** — `../` detection + allowed-root enforcement
- **Image name validation** — strict `registry/ns/image:tag` regex
- **CVE ID format enforcement** — only `CVE-YYYY-NNNNN` accepted
- **Oversized input rejection** — max 1024 chars per argument
- **Null byte detection** — prevents path truncation attacks

### 2. Process Execution (`TrivyRunner.java`)
The Trivy binary is **never** invoked via `Runtime.exec(String)` (shell-interpolated form).
Always uses `ProcessBuilder(List<String>)` — arguments are passed directly to the OS without shell expansion.

```java
// ✅ Safe — no shell, no interpolation
new ProcessBuilder(Arrays.asList("/usr/local/bin/trivy", "image", "--input", tarPath))

// ❌ Never used — shell injection risk
Runtime.getRuntime().exec("trivy image --input " + tarPath)
```

### 3. Rate Limiting (`RateLimiter.java`)
Sliding-window rate limiter: **60 requests/minute** (configurable).
Prevents DoS abuse of the MCP endpoint.

### 4. API Key Enforcement (`ApiKeyValidator.java`)
Set `TRIVY_MCP_API_KEY=<secret>` in the environment to enforce key-based auth.
Keys are compared using `MessageDigest.isEqual()` (constant-time, no timing attacks).
Leave unset for development (no enforcement).

### 5. Execution Timeout
All Trivy scans have a **300-second timeout** (configurable per call).
`process.destroyForcibly()` ensures no zombie processes on timeout.

### 6. stderr Separation
All logs go to **stderr** — never stdout.
The stdout JSON-RPC stream is always clean.

---

## Requirements

- **Java 8+** (no external dependencies — standard library only)
- **Trivy binary** installed at `/usr/local/bin/trivy` (or set `TRIVY_BINARY` env var)

### Install Trivy
```bash
# Linux (recommended for CI)
curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin

# macOS
brew install trivy

# Docker (for CI use)
docker pull aquasec/trivy:latest
```

---

## Build & Run

```bash
# Build
chmod +x build.sh
./build.sh

# Run MCP server (stdin/stdout transport)
java -jar target/trivy-mcp-server.jar

# With API key enforcement
TRIVY_MCP_API_KEY=my-secret java -jar target/trivy-mcp-server.jar

# Custom Trivy binary path
TRIVY_BINARY=/opt/trivy/bin/trivy java -jar target/trivy-mcp-server.jar
```

---

## Run Tests

```bash
# Quick pass/fail
java -cp target/trivy-mcp-server.jar com.ecoskiller.trivy.server.TrivyMcpServerTest

# With full JSON output
java -cp target/trivy-mcp-server.jar com.ecoskiller.trivy.server.TrivyMcpServerTest --verbose
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "trivy-mcp-server": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/trivy-mcp-server/target/trivy-mcp-server.jar"],
      "env": {
        "TRIVY_BINARY": "/usr/local/bin/trivy",
        "TRIVY_MCP_API_KEY": "your-secret-key"
      }
    }
  }
}
```

---

## Ecoskiller Pipeline Integration

This server mirrors the exact Ecoskiller CI/CD Trivy flow:

```
build:docker    → produces image.tar artifact
     ↓
scan:trivy      → scan_image_tar tool (exit-code 1 if CRITICAL/HIGH)
     ↓ (only if exit-code=0)
push:harbor     → only Trivy-approved images
     ↓
deploy:staging  → Kubernetes namespace receives clean image
     ↓ (manual approval)
deploy:prod     → Two-factor gate: Trivy scan + human review
```

Environments: `dev → test → staging → production`  
Policy: CRITICAL (CVSS 9-10) and HIGH (CVSS 7-8.9) block pipeline immediately.  
MEDIUM/LOW: logged, tracked, sprint-level remediation.

---

## Compliance

Trivy scan evidence supports:
- **DPDPA 2023** — Technical security measures for personal data protection
- **ISO/IEC 27001** — A.12.6.1 (vulnerability management), A.14.2.7 (supply chain)
- **IT Act 2000** — Section 43A reasonable security practices

Use `get_compliance_report` to generate audit-ready evidence per framework.

---

## File Structure

```
trivy-mcp-server/
├── build.sh
├── README.md
├── claude_desktop_config.json
└── src/main/java/com/ecoskiller/trivy/
    ├── server/
    │   ├── TrivyMcpServer.java        ← Main entry point (JSON-RPC loop)
    │   └── TrivyMcpServerTest.java    ← Integration test suite
    ├── tools/
    │   ├── McpTool.java               ← Tool interface
    │   └── AllTools.java              ← All 12 tool implementations
    ├── tools/ScanImageTarTool.java    ← Tool 1 (primary CI tool)
    ├── model/
    │   └── ToolResult.java
    ├── security/
    │   ├── InputSanitizer.java        ← Injection/traversal prevention
    │   ├── RateLimiter.java           ← Sliding-window rate limiting
    │   └── ApiKeyValidator.java       ← Constant-time key validation
    └── util/
        ├── TrivyRunner.java           ← Secure ProcessBuilder executor
        ├── JsonUtils.java             ← Zero-dep JSON-RPC builder/parser
        └── Logger.java               ← stderr logger
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`
