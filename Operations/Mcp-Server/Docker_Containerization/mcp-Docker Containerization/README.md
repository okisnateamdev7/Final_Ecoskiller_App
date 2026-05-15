# mcp-05-docker

**Ecoskiller | CAT-05 — Docker Containerization**  
MCP Server in Java | 15 Agents | Priority: HIGH

---

## Agents (15)

| # | Tool Name | Agent | Description |
|---|-----------|-------|-------------|
| 1 | `dockerfile_builder` | DOCKERFILE_BUILDER_AGENT | Generates secure multi-stage Dockerfiles with non-root user, health checks, pinned base images |
| 2 | `image_build_pipeline` | IMAGE_BUILD_PIPELINE_AGENT | BuildKit pipeline: tags with git commit hash, layer caching, multi-arch push |
| 3 | `registry_push` | REGISTRY_PUSH_AGENT | Pushes to Harbor with RBAC, content trust, Trivy auto-scan on push |
| 4 | `vulnerability_scanner` | VULNERABILITY_SCANNER_AGENT | Trivy CVE scan — blocks deployment on CRITICAL/HIGH CVEs |
| 5 | `sbom_generator` | SBOM_GENERATOR_AGENT | syft SBOM in CycloneDX/SPDX format for SOC2 compliance |
| 6 | `image_signing` | IMAGE_SIGNING_AGENT | Cosign image signing — Kubernetes admission controller verification |
| 7 | `base_image_manager` | BASE_IMAGE_MANAGER_AGENT | Alpine base image CVE tracking, upgrade recommendations |
| 8 | `layer_cache_optimizer` | LAYER_CACHE_OPTIMIZER_AGENT | Dockerfile layer analysis — up to 80% build time reduction |
| 9 | `multi_arch_builder` | MULTI_ARCH_BUILDER_AGENT | docker buildx amd64+arm64 manifest list builds |
| 10 | `health_check_configurator` | HEALTH_CHECK_CONFIGURATOR_AGENT | HEALTHCHECK + Kubernetes liveness/readiness probe config |
| 11 | `image_retention_policy` | IMAGE_RETENTION_POLICY_AGENT | Harbor retention rules — storage cost control |
| 12 | `environment_injection` | ENVIRONMENT_INJECTION_AGENT | 12-factor env var validation — no secrets baked into images |
| 13 | `build_log_analyzer` | BUILD_LOG_ANALYZER_AGENT | Build log parsing — bottleneck identification |
| 14 | `container_audit_trail` | CONTAINER_AUDIT_TRAIL_AGENT | SOC2 image provenance trail: Dockerfile → git commit → image digest |
| 15 | `multi_cloud_distribution` | MULTI_CLOUD_DISTRIBUTION_AGENT | Harbor → GCP asia-south1 + AWS ap-south-1 image distribution |

---

## Requirements

- Java 17+
- Maven 3.8+ (build only)

---

## Build

```bash
mvn package -DskipTests
# Output: target/mcp-05-docker-1.0.0.jar (fat JAR, all deps included)
```

---

## Run the server

```bash
java -jar target/mcp-05-docker-1.0.0.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).  
Logs go to **stderr** so stdout stays clean for JSON-RPC.

---

## Run tests

```bash
mvn test
# 29 tests covering all 15 agents + security edge cases
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-05-docker": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-05-docker-1.0.0.jar"]
    }
  }
}
```

---

## Docker (containerize the MCP server itself)

```bash
docker build -t harbor.ecoskiller.io/mcp-05-docker:v1.0.0 .
docker push harbor.ecoskiller.io/mcp-05-docker:v1.0.0
```

---

## Security

| Layer | Mechanism |
|-------|-----------|
| Input validation | Regex allowlists on all inputs (service names, image refs, git hashes) |
| Injection prevention | No Runtime.exec() with user input — no shell injection possible |
| Secret masking | Auth tokens masked in all output (first 4 chars + ****) |
| Log injection prevention | Control characters stripped from all logged/returned values |
| Size limits | Dockerfile (50KB), build logs (500KB) |
| Container hardening | Non-root user (UID 1000), read-only JAR permissions |
| JVM hardening | `-Xmx256m`, `-XX:+ExitOnOutOfMemoryError`, IPv4 stack |

---

## File Structure

```
mcp-05-docker/
├── pom.xml                                              ← Maven build (Java 17, fat JAR)
├── Dockerfile                                           ← Multi-stage, non-root, eclipse-temurin:17-jre-alpine
├── claude_desktop_config.json                           ← Claude Desktop config snippet
├── README.md
└── src/
    ├── main/java/io/ecoskiller/mcp/docker/
    │   ├── DockerMCPServer.java                         ← JSON-RPC 2.0 dispatcher, security layer
    │   └── DockerAgents.java                            ← 15 agent implementations
    └── test/java/io/ecoskiller/mcp/docker/
        └── DockerAgentsTest.java                        ← 29 JUnit 5 tests
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Ecoskiller Infrastructure Context

- **Registry:** Harbor 2.x — `harbor.ecoskiller.io`
- **CI/CD:** GitLab CI (self-hosted) → Docker BuildKit → Harbor → k3s
- **Runtime:** containerd on k3s (GCP asia-south1 + AWS ap-south-1)
- **Security scanner:** Trivy 0.48.x (blocks CRITICAL/HIGH CVEs)
- **Image signing:** Cosign + Kubernetes admission controller
- **Base image:** `node:18.15-alpine` (~50MB, minimal attack surface)
- **Build target:** ~200–500MB per microservice (multi-stage optimized)
