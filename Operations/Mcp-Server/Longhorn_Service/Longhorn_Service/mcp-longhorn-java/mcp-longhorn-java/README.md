# mcp-longhorn-java

**Ecoskiller | CAT-INFRA — Longhorn Distributed Block Storage MCP Server**  
MCP Server in Java | 12 Agents | Priority: HIGH | Security: Hardened

---

## Overview

Longhorn is the CNCF-graduated distributed block storage layer for the Ecoskiller platform,
running on self-managed k3s clusters across **GCP (asia-south1)** and **AWS (ap-south-1)**.
This MCP server exposes Longhorn's full lifecycle management as 12 Claude-callable tools.

**No external dependencies. Pure Java 17+. Zero Maven / Gradle required.**

---

## Agents (12 Tools)

| # | Tool Name          | Agent                       | Description                                     |
|---|--------------------|-----------------------------|--------------------------------------------------|
| 1 | `volume_provision` | VOLUME_PROVISION_AGENT      | Provision a new Longhorn PVC (with encryption support) |
| 2 | `volume_expand`    | VOLUME_EXPAND_AGENT         | Online volume expansion without Pod restart     |
| 3 | `volume_delete`    | VOLUME_DELETE_AGENT         | Safe PVC deletion with confirmation token       |
| 4 | `volume_list`      | VOLUME_LIST_AGENT           | List all PVCs with health/replica status        |
| 5 | `snapshot_create`  | SNAPSHOT_CREATE_AGENT       | On-demand VolumeSnapshot (PITR capable)         |
| 6 | `snapshot_list`    | SNAPSHOT_LIST_AGENT         | List snapshots with Velero eligibility          |
| 7 | `snapshot_restore` | SNAPSHOT_RESTORE_AGENT      | Restore PVC from snapshot (cross-cloud DR)      |
| 8 | `snapshot_delete`  | SNAPSHOT_DELETE_AGENT       | Delete snapshot with confirmation token         |
| 9 | `backup_trigger`   | BACKUP_TRIGGER_AGENT        | Trigger incremental backup to MinIO S3 target   |
| 10| `backup_status`    | BACKUP_STATUS_AGENT         | Query backup state + RPO compliance             |
| 11| `replica_health`   | REPLICA_HEALTH_AGENT        | Per-volume replica placement + degraded alerts  |
| 12| `storage_metrics`  | STORAGE_METRICS_AGENT       | Node-level SSD utilisation + OpenCost data      |

---

## Security Features

| Layer              | Mechanism                                                                 |
|--------------------|---------------------------------------------------------------------------|
| **Input Validation** | All string args sanitised — rejects shell metacharacters, path traversal, oversized inputs |
| **Rate Limiting**  | Token-bucket: 60 calls/minute per tool — prevents runaway agentic loops  |
| **Confirmation Tokens** | Destructive ops (`volume_delete`, `snapshot_delete`) require exact token strings |
| **Business Rules** | OpenSearch PVCs **require** `encrypted=true` (GDPR compliance enforced)  |
| **Stdout Isolation** | All logging goes to stderr only — stdout is pure JSON-RPC 2.0            |
| **No Secrets in Responses** | MinIO credentials never returned in tool outputs                 |

---

## Requirements

- **Java 17+** (tested on Java 21)
- No external packages, frameworks, or build tools

---

## Build

```bash
# Requires javac on PATH (JDK 17+)
chmod +x build.sh
./build.sh
```

Produces two JARs:
- `longhorn-mcp-server.jar` — the MCP server
- `longhorn-mcp-tests.jar` — the self-contained test runner

---

## Run Tests

```bash
java -jar longhorn-mcp-tests.jar
# Expected: 50 passed, 0 failed
```

---

## Run the Server

```bash
java -jar longhorn-mcp-server.jar
```

The server reads JSON-RPC 2.0 from **stdin** and writes responses to **stdout**.
All logs go to **stderr** so they never pollute the MCP stream.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-longhorn": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/longhorn-mcp-server.jar"]
    }
  }
}
```

---

## File Structure

```
mcp-longhorn-java/
├── build.sh                              ← Zero-dependency build script
├── longhorn-mcp-server.jar               ← Runnable MCP server JAR (after build)
├── longhorn-mcp-tests.jar                ← Self-contained test runner (after build)
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/longhorn/
    │   ├── server/
    │   │   ├── LonghornMcpServer.java    ← Main entry point + request dispatch
    │   │   ├── JsonParser.java           ← Zero-dep recursive-descent JSON parser
    │   │   └── JsonRpc.java              ← JSON-RPC 2.0 response builder
    │   ├── tools/
    │   │   ├── ToolHandler.java          ← Tool interface
    │   │   ├── BaseTool.java             ← Schema helpers + arg extraction
    │   │   ├── VolumeProvisionTool.java  ← Agent 1
    │   │   ├── VolumeExpandTool.java     ← Agent 2
    │   │   ├── VolumeDeleteTool.java     ← Agent 3
    │   │   ├── VolumeListTool.java       ← Agent 4
    │   │   ├── SnapshotCreateTool.java   ← Agent 5
    │   │   ├── SnapshotListTool.java     ← Agent 6
    │   │   ├── SnapshotRestoreTool.java  ← Agent 7
    │   │   ├── SnapshotDeleteTool.java   ← Agent 8
    │   │   ├── BackupTriggerTool.java    ← Agent 9
    │   │   ├── BackupStatusTool.java     ← Agent 10
    │   │   ├── ReplicaHealthTool.java    ← Agent 11
    │   │   └── StorageMetricsTool.java   ← Agent 12
    │   └── security/
    │       ├── InputValidator.java       ← Sanitisation + injection prevention
    │       └── RateLimiter.java          ← Token-bucket rate limiter
    └── test/java/com/ecoskiller/mcp/longhorn/
        └── LonghornMcpTests.java         ← 50 tests, no external test framework
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Ecoskiller Infrastructure Context

| Property          | Value                                               |
|-------------------|-----------------------------------------------------|
| Storage Engine    | Longhorn (CNCF graduated, driver.longhorn.io)       |
| Kubernetes        | k3s self-managed on GCP n2-standard-8 + AWS m5.2xlarge |
| Node SSD          | 100 GiB per Core Worker Pool VM                     |
| Default Replicas  | 3 (cross-node, auto-healing)                        |
| Backup Target     | MinIO S3 @ `minio.ops.svc.cluster.local:9000` (GCP primary, AWS secondary) |
| Redis RPO         | < 15 minutes (BGSAVE CronJob to MinIO)              |
| Block Volume RPO  | < 60 minutes (incremental MinIO backup)             |
| Encrypted PVCs    | OpenSearch (LUKS), ClickHouse (AES-128-CTR app layer) |
| Cost              | $0 SaaS storage fees — fully in-cluster             |
