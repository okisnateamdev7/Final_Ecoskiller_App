# mcp-15-idea-protection

**Ecoskiller | CAT-15 — Idea, Anti-Theft & Legal Protection**  
MCP Server in **Java** | 8 Agents | Priority: HIGH

---

## Agents

| # | Tool Name | Agent Class | Description |
|---|-----------|-------------|-------------|
| 1 | `idea_attribution_chain` | `IdeaAttributionChainAgent` | Builds a cryptographic attribution chain linking creator → contributors |
| 2 | `hash_proof` | `HashProofAgent` | Generates SHA-256 proof-of-existence for any content |
| 3 | `feature_store` | `FeatureStoreAgent` | Extracts and stores feature vectors for similarity lookup |
| 4 | `fast_similarity` | `FastSimilarityAgent` | Rapid cosine + Jaccard similarity check (sub-ms) |
| 5 | `deep_similarity` | `DeepSimilarityAgent` | Full forensic multi-dimensional similarity analysis |
| 6 | `copy_probability` | `CopyProbabilityAgent` | Bayesian copy-probability scoring with multi-signal input |
| 7 | `behavior_stream_processor` | `BehaviorStreamProcessorAgent` | Analyses user event streams to detect IP theft behaviour |
| 8 | `access_log_tracker` | `AccessLogTrackerAgent` | Creates tamper-evident hash-chained access log entries |

---

## Requirements

- **Java 11+**
- **Maven 3.6+** (for build & tests)

---

## Build

```bash
cd mcp-15-idea-protection
mvn clean package -q
```

This produces a fat JAR at:

```
target/mcp-15-idea-protection-1.0.0.jar
```

---

## Run the server

```bash
java -jar target/mcp-15-idea-protection-1.0.0.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol 2024-11-05).

---

## Run tests

```bash
mvn test
```

16 tests covering all 8 agents — happy path, edge cases, chain linking, and tool definition validation.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-15-idea-protection": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/mcp-15-idea-protection/target/mcp-15-idea-protection-1.0.0.jar"
      ]
    }
  }
}
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Project Structure

```
mcp-15-idea-protection/
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp15/
    │   ├── McpServer.java                   ← Main entry point (stdio loop)
    │   ├── AgentHandler.java                ← Agent interface
    │   ├── AgentUtils.java                  ← Shared utilities (hashing, embeddings)
    │   ├── IdeaAttributionChainAgent.java   ← Agent 1
    │   ├── HashProofAgent.java              ← Agent 2
    │   ├── FeatureStoreAgent.java           ← Agent 3
    │   ├── FastSimilarityAgent.java         ← Agent 4
    │   ├── DeepSimilarityAgent.java         ← Agent 5
    │   ├── CopyProbabilityAgent.java        ← Agent 6
    │   ├── BehaviorStreamProcessorAgent.java← Agent 7
    │   └── AccessLogTrackerAgent.java       ← Agent 8
    └── test/java/com/ecoskiller/mcp15/
        └── AgentTest.java                   ← 16 JUnit 5 tests
```

---

## Dependency

Only **one external dependency** — Jackson for JSON:

```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.15.2</version>
</dependency>
```
