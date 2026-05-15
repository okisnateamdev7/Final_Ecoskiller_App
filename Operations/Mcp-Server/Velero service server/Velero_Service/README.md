# mcp-velero

**Ecoskiller | CAT-16 — Velero Backup, Restore & Disaster Recovery**
MCP Server in Java | 16 Agents | Priority: HIGH | Secure by Design

---

## Agents (16)

| # | Tool Name | Agent | Description |
|---|-----------|-------|-------------|
| 1 | `backup_create` | BACKUP_CREATE_AGENT | Create on-demand or pre-change backups |
| 2 | `backup_list` | BACKUP_LIST_AGENT | List all backup snapshots (7-day window) |
| 3 | `backup_delete` | BACKUP_DELETE_AGENT | Delete a named backup (rate-limited, confirm gate) |
| 4 | `backup_status` | BACKUP_STATUS_AGENT | Get phase and metadata of a named backup |
| 5 | `backup_describe` | BACKUP_DESCRIBE_AGENT | Full verbose backup description |
| 6 | `restore_create` | RESTORE_CREATE_AGENT | Full cluster or namespace-scoped restore |
| 7 | `restore_list` | RESTORE_LIST_AGENT | List all restore operations |
| 8 | `restore_status` | RESTORE_STATUS_AGENT | Get status of a named restore |
| 9 | `restore_describe` | RESTORE_DESCRIBE_AGENT | Full verbose restore description |
| 10 | `schedule_list` | SCHEDULE_LIST_AGENT | List all Schedule CRDs |
| 11 | `schedule_create` | SCHEDULE_CREATE_AGENT | Create a new backup schedule |
| 12 | `schedule_delete` | SCHEDULE_DELETE_AGENT | Delete a schedule (primary protected) |
| 13 | `backup_storage_location` | BACKUP_STORAGE_LOCATION_AGENT | Manage BSLs, DR failover to AWS |
| 14 | `backup_integrity_check` | BACKUP_INTEGRITY_CHECK_AGENT | Weekly integrity verification |
| 15 | `dr_drill_log` | DR_DRILL_LOG_AGENT | DR drill compliance reporting |
| 16 | `cluster_health` | CLUSTER_HEALTH_AGENT | Full ops dashboard health snapshot |

---

## Requirements

- **Java 17+**
- **Maven 3.8+** (for building)
- **Velero CLI** installed and in PATH (`velero` command accessible)
- Velero configured with MinIO BSL (GCP us-central1 primary)

---

## Build

```bash
mvn clean package -DskipTests
# Fat JAR output: target/mcp-velero-1.0.0.jar
```

---

## Run the server

```bash
java -jar target/mcp-velero-1.0.0.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).
All logging goes to **stderr** to keep stdout clean for JSON-RPC.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-velero": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-velero/target/mcp-velero-1.0.0.jar"]
    }
  }
}
```

---

## Run tests

```bash
mvn test                   # all tests (no Velero CLI required for security tests)
mvn test -Dsurefire.useFile=false   # with live console output
```

Tests cover:
- **SEC-01–14**: SecurityManager input validation, injection blocking, rate-limiting
- **SCHEMA-01–02**: All 16 tools expose valid MCP inputSchema
- **SAFETY-01–06**: Confirm gates, primary schedule protection, DR switch protection
- **INJECT-01–05**: Shell injection in every tool parameter type
- **DR-01–02**: DR drill log validation

---

## File Structure

```
mcp-velero/
├── pom.xml
├── claude_desktop_config.json
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/velero/
    │   ├── server/
    │   │   └── VeleroMcpServer.java        ← Main MCP server (JSON-RPC 2.0)
    │   ├── security/
    │   │   └── SecurityManager.java         ← Input validation, audit log, rate-limit
    │   ├── tools/
    │   │   ├── BaseTool.java               ← Abstract base (all 16 tools extend this)
    │   │   ├── ToolRegistry.java           ← Tool registry for MCP tools/list + tools/call
    │   │   ├── BackupCreateTool.java       ← Agent 1
    │   │   ├── BackupListTool.java         ← Agent 2
    │   │   ├── BackupDeleteTool.java       ← Agent 3
    │   │   ├── BackupStatusTool.java       ← Agent 4
    │   │   ├── BackupDescribeTool.java     ← Agent 5
    │   │   ├── RestoreCreateTool.java      ← Agent 6
    │   │   ├── RestoreListTool.java        ← Agent 7
    │   │   ├── RestoreStatusTool.java      ← Agent 8
    │   │   ├── RestoreDescribeTool.java    ← Agent 9
    │   │   ├── ScheduleListTool.java       ← Agent 10
    │   │   ├── ScheduleCreateTool.java     ← Agent 11
    │   │   ├── ScheduleDeleteTool.java     ← Agent 12
    │   │   ├── BackupStorageLocationTool.java ← Agent 13
    │   │   ├── BackupIntegrityCheckTool.java  ← Agent 14
    │   │   ├── DrDrillLogTool.java         ← Agent 15
    │   │   └── ClusterHealthTool.java      ← Agent 16
    │   ├── model/
    │   │   └── ToolResult.java             ← Immutable ok/error result wrapper
    │   └── util/
    │       └── CommandRunner.java          ← Secure process builder (no shell)
    └── test/java/com/ecoskiller/mcp/velero/
        └── VeleroMcpTest.java              ← 25+ tests for all agents
```

---

## Security Architecture

### What is secured and how

| Threat | Mitigation |
|--------|-----------|
| **Shell injection** | `ProcessBuilder` with explicit arg list — no shell ever invoked |
| **Command injection in names** | `SAFE_NAME` regex allow-list: `[a-zA-Z0-9][a-zA-Z0-9_\-]{0,62}` |
| **Namespace abuse** | Explicit allow-list: `core,realtime,billing,analytics,admin,ops,media,velero` |
| **Accidental full restore** | `confirm=true` required; separate gate for full-cluster scope |
| **Primary schedule deletion** | `override_primary=true` required on top of `confirm=true` |
| **DR failover abuse** | `confirm=true` + rate-limit on `switch_to_dr` |
| **Rapid mass deletion** | 5-second cooldown per destructive operation key |
| **Audit trail** | Every tool call logged to `VELERO_AUDIT` logger (stderr) |
| **Stdout pollution** | All logging goes to stderr; stdout is pure JSON-RPC 2.0 |
| **Path traversal** | `PATH_TRAVERSAL` regex blocks `../` in all name fields |
| **Long input DoS** | Free-text fields truncated at 500 characters |

### What Velero excludes (enforced by this server)

Database PVCs are **always excluded** from backup scope — this is hardcoded in
`BackupCreateTool` and `ScheduleCreateTool`, not left to the caller:

| Volume | Backup Tool | Why excluded from Velero |
|--------|-------------|--------------------------|
| PostgreSQL PVC | `pg_dump` (daily + WAL) | Database-consistent backups; WAL streaming |
| Redis PVC | `BGSAVE` (15-min) | Higher frequency (15min vs 24hr RPO) |
| ClickHouse PVC | `clickhouse-backup` | Native incremental; shard-consistent |

---

## Protocol

- **Transport**: stdio (stdin/stdout)
- **Format**: JSON-RPC 2.0
- **MCP Version**: 2024-11-05
- **Methods**: `initialize`, `tools/list`, `tools/call`, `ping`

---

## EcoSkiller Context

| Property | Value |
|----------|-------|
| Platform | EcoSkiller k3s — ops namespace |
| Primary cloud | GCP us-central1 |
| DR cloud | AWS us-east-1 |
| Backup target | MinIO S3-compatible (self-hosted) |
| Backup scope | K8s manifests + ConfigMaps + Secrets + non-database PVCs |
| Namespaces | core, realtime, billing, analytics, admin, ops, media |
| Frequency | Daily at 02:00 (cron: `0 2 * * *`) |
| Retention | 7 days (TTL: `168h`) |
| RTO target | < 1 hour (full cluster), < 30 min (namespace) |
| DR drill | Quarterly full restore + Monthly namespace restore |
| Monitoring | Prometheus `/metrics` → Grafana → Alertmanager → ntfy + Mattermost |
