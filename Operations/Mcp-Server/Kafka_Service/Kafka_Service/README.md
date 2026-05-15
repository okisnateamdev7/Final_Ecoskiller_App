# mcp-kafka-service

**Ecoskiller | CAT-KAFKA — Apache Kafka MCP Server**  
Java 17 | Secure | 15 Tools | Apache Kafka 3.7.0

---

## Overview

Secure Java MCP server for managing the Ecoskiller platform's Apache Kafka 3.7.0 event streaming infrastructure.  
Covers all 10 canonical topics, DLQ management, consumer lag monitoring, and cluster operations.

---

## Tools (15)

| # | Tool | Description |
|---|------|-------------|
| 1 | `list_topics` | List all topics; optionally filter to Ecoskiller canonical topics |
| 2 | `create_topic` | Create topic + DLQ with spec-correct partitions and retention |
| 3 | `describe_topic` | Full topic details: partitions, ISR, replication, config |
| 4 | `delete_topic` | Delete topic (blocked in production) |
| 5 | `list_consumer_groups` | List all consumer groups; flags spec-convention compliance |
| 6 | `describe_consumer_group` | Group state, coordinator, members, assigned partitions |
| 7 | `get_consumer_lag` | Per-partition lag + SLO breach flag (threshold: 5000 msgs) |
| 8 | `publish_event` | Publish a JSON event (admin/test; acks=all, idempotent) |
| 9 | `list_dlq_topics` | List all *.dlq topics |
| 10 | `get_dlq_messages` | Read DLQ messages for inspection (read-only, no offset commit) |
| 11 | `get_topic_offsets` | Begin/end offsets and estimated message count per partition |
| 12 | `provision_ecoskiller_topics` | Idempotent bootstrap of all 10 topics + DLQs |
| 13 | `get_cluster_info` | Broker list, cluster ID, controller, KRaft mode |
| 14 | `reset_consumer_offset` | Reset group offsets to earliest/latest for replay |
| 15 | `get_ecoskiller_topic_map` | Full Kafka spec: producers, consumers, groups, DLQs, retention |

---

## Ecoskiller Canonical Topics

| Topic | Partitions | Retention | Producer |
|-------|-----------|-----------|----------|
| `user.created` | 3 | 7 days | user-service |
| `job.applied` | 6 | 30 days | application-service |
| `interview.completed` | 6 | 30 days | interview-service |
| `gd.completed` | 6 | 30 days | gd-orchestrator |
| `match.scored` | 3 | 30 days | scoring-engine |
| `belt.eligible` | 3 | 30 days | certification-engine |
| `invoice.generated` | 3 | 90 days | billing-service |
| `idea.submitted` | 3 | 30 days | innovation-service |
| `idea.fingerprinted` | 3 | 30 days | innovation-service |
| `royalty.calculated` | 3 | 90 days | royalty-engine |

Every topic has a `.dlq` companion (e.g. `gd.completed.dlq`).

---

## Security

| Layer | Mechanism |
|-------|-----------|
| **Authentication** | SHA-256 hashed Bearer token from `MCP_API_TOKEN` env var |
| **Token rotation** | Up to 5 tokens via `MCP_API_TOKEN`, `MCP_API_TOKEN_2` … |
| **Rate limiting** | 120 requests/minute per token (sliding window) |
| **Kafka auth** | SASL/SCRAM-SHA-512 + SSL (production) |
| **Input validation** | Topic/group name whitelist regex; injection prevention |
| **Prod guard** | Topic deletion blocked in `ECOSKILLER_ENV=prod` |
| **Audit logging** | Every tool call logged with token prefix + params |
| **Secrets** | All credentials from environment variables / k8s Secrets |
| **Container** | Non-root, read-only filesystem, all Linux capabilities dropped |

---

## Requirements

- Java 17+
- Maven 3.8+
- Kafka 3.7.0 cluster with SASL/SSL

---

## Build

```bash
mvn clean package -DskipTests
# Output: target/mcp-kafka-service-1.0.0.jar
```

## Test

```bash
mvn test
```

## Run

```bash
export MCP_API_TOKEN="your-secret-token-minimum-32-characters"
export KAFKA_BOOTSTRAP_SERVERS="localhost:9092"
export KAFKA_SASL_USERNAME="ecoskiller-mcp"
export KAFKA_SASL_PASSWORD="your-kafka-password"
export KAFKA_SECURITY_PROTOCOL="SASL_SSL"
export ECOSKILLER_ENV="dev"   # dev | test | stage | prod

java -jar target/mcp-kafka-service-1.0.0.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).  
Logs go to **stderr** — stdout is kept clean for MCP messages.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "ecoskiller-kafka-mcp": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-kafka-service-1.0.0.jar"],
      "env": {
        "MCP_API_TOKEN": "your-secret-token",
        "KAFKA_BOOTSTRAP_SERVERS": "kafka-0.kafka-headless.messaging.svc.cluster.local:9092",
        "KAFKA_SASL_USERNAME": "ecoskiller-mcp",
        "KAFKA_SASL_PASSWORD": "your-password",
        "KAFKA_SECURITY_PROTOCOL": "SASL_SSL",
        "ECOSKILLER_ENV": "dev"
      }
    }
  }
}
```

---

## Kubernetes Deployment

```bash
# Create secret
kubectl create secret generic ecoskiller-kafka-mcp-secret \
  --from-literal=MCP_API_TOKEN='<token>' \
  --from-literal=KAFKA_SASL_PASSWORD='<password>' \
  -n messaging

# Apply deployment
kubectl apply -f k8s-deployment.yaml
```

---

## Environment Variables

| Variable | Required | Description |
|----------|----------|-------------|
| `MCP_API_TOKEN` | Yes | Bearer token for MCP client authentication |
| `MCP_API_TOKEN_2` … `_5` | No | Additional tokens for rotation |
| `KAFKA_BOOTSTRAP_SERVERS` | Yes | Kafka broker connection string |
| `KAFKA_SASL_USERNAME` | Yes (SASL) | Kafka SASL username |
| `KAFKA_SASL_PASSWORD` | Yes (SASL) | Kafka SASL password |
| `KAFKA_SECURITY_PROTOCOL` | No | `SASL_SSL` (default) or `PLAINTEXT` (dev) |
| `KAFKA_SASL_MECHANISM` | No | `SCRAM-SHA-512` (default) |
| `KAFKA_SSL_TRUSTSTORE_PATH` | No | Custom CA truststore path |
| `KAFKA_SSL_TRUSTSTORE_PASS` | No | Truststore password |
| `ECOSKILLER_ENV` | No | `prod` \| `stage` \| `test` \| `dev` (default: `dev`) |

---

## File Structure

```
mcp-kafka-service/
├── pom.xml
├── README.md
├── claude_desktop_config.json
├── k8s-deployment.yaml
└── src/
    ├── main/java/com/ecoskiller/mcp/kafka/
    │   ├── server/
    │   │   └── KafkaMcpServer.java        ← Main MCP server + tool routing
    │   ├── tools/
    │   │   └── KafkaTools.java            ← All 15 Kafka tool implementations
    │   └── security/
    │       └── McpSecurityManager.java    ← Auth, rate limiting, validation, audit
    └── test/java/com/ecoskiller/mcp/kafka/
        └── KafkaMcpServerTest.java        ← Unit tests
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)  
- Format: **JSON-RPC 2.0**  
- MCP Version: **2024-11-05**  
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`
