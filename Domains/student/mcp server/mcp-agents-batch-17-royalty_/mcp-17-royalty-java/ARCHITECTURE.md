# Architecture Document — MCP-17 Royalty Server (Java)

## System Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                      Client Layer                               │
│  (Claude Desktop / IDE / Custom Application)                    │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                  JSON-RPC 2.0 (stdio)
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                    MCPServer (Main)                             │
│  ├─ Reads from stdin                                            │
│  ├─ Parses JSON-RPC 2.0                                         │
│  ├─ Routes to MessageHandler                                    │
│  └─ Writes to stdout                                            │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                  MessageHandler (Router)                        │
│  ├─ initialize       → Server Info                              │
│  ├─ tools/list       → ToolRegistry                             │
│  ├─ tools/call       → Agent.execute()                          │
│  └─ ping             → Health Check                             │
└──────────────────────────┬──────────────────────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
   ┌─────────┐      ┌────────────┐     ┌──────────┐
   │ Agent 1 │      │  Agent N   │     │ Agent 18 │
   │ (base)  │      │ (extends)  │     │ (final)  │
   └────┬────┘      └────┬───────┘     └────┬─────┘
        │                │                   │
        └────────────────┼───────────────────┘
                         │
              ┌──────────┴──────────┐
              │                     │
              ▼                     ▼
         ┌─────────────┐      ┌────────────┐
         │  Business   │      │  External  │
         │   Logic     │      │  Services  │
         └─────────────┘      └────────────┘
              │                     │
              ▼                     ▼
         ┌─────────────┐      ┌────────────┐
         │ Validation  │      │   APIs     │
         │ & Rules     │      │ & Webhooks │
         └─────────────┘      └────────────┘
```

---

## Class Hierarchy

### Agent Architecture

```
┌─────────────────────────────────────────┐
│          Agent (Interface)              │
│  - execute(toolName, args)              │
│  - getAgentName()                       │
│  - getDescription()                     │
└────────────────┬────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────┐
│       BaseAgent (Abstract)              │
│  # mapper: ObjectMapper                 │
│  # logger: Logger                       │
│  + execute(...)                         │
│  + createResponse(...)                  │
│  + createErrorResponse(...)             │
│  + getStringParam(...)                  │
└────────────────┬────────────────────────┘
                 │
    ┌────────────┴─────────────┬──────────┬─────────┐
    │                          │          │         │
    ▼                          ▼          ▼         ▼
┌─────────────┐      ┌─────────────┐   ...  ┌──────────────┐
│TaxCompliance│      │RoyaltyWallet│        │AuditVerif... │
│   Agent     │      │   Agent     │        │    Agent     │
└─────────────┘      └─────────────┘        └──────────────┘
```

### Package Structure

```
com.ecoskiller.mcp
├── server/
│   └── MCPServer                    # Entry point
├── handlers/
│   ├── MessageHandler               # JSON-RPC router
│   └── ToolRegistry                 # Tool definitions
└── agents/
    ├── Agent                        # Interface
    ├── BaseAgent                    # Abstract base
    ├── Agents1to9                   # Agents 1-9
    └── Agents10to18                 # Agents 10-18
```

---

## Message Flow

### 1. Initialize Request

```
CLIENT REQUEST:
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": {}
}

                     ↓

MCPServer.start()
  → Scanner reads stdin
  → mapper.readTree() parses JSON
  → messageHandler.handle()

                     ↓

MessageHandler.handleInitialize(id)
  → Creates server info
  → Creates capabilities
  → Returns response

                     ↓

SERVER RESPONSE:
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "serverInfo": {
      "name": "mcp-17-royalty",
      "version": "1.0.0"
    },
    "protocolVersion": {
      "version": "2024-11-05"
    },
    "capabilities": {}
  }
}
```

### 2. Tools List Request

```
CLIENT REQUEST:
{
  "jsonrpc": "2.0",
  "id": 2,
  "method": "tools/list",
  "params": {}
}

                     ↓

MessageHandler.handleToolsList(id)
  → toolRegistry.getToolsList()
  → Returns all 18 tools with schemas

                     ↓

SERVER RESPONSE:
{
  "jsonrpc": "2.0",
  "id": 2,
  "result": [
    {
      "name": "tax_compliance",
      "description": "Handles tax compliance...",
      "inputSchema": { ... },
      "_meta": {
        "agent": "TAX_COMPLIANCE_AGENT",
        "category": "CAT-17"
      }
    },
    ... (17 more tools)
  ]
}
```

### 3. Tool Call Request

```
CLIENT REQUEST:
{
  "jsonrpc": "2.0",
  "id": 3,
  "method": "tools/call",
  "params": {
    "name": "royalty_calculation",
    "arguments": {
      "revenue": 100000,
      "royalty_percentage": 5.0
    }
  }
}

                     ↓

MessageHandler.handleToolCall(id, params)
  → Extract tool name: "royalty_calculation"
  → Find agent: RoyaltyCalculationAgent
  → agent.execute(toolName, arguments)

                     ↓

RoyaltyCalculationAgent.executeToolLogic()
  → getDoubleParam("revenue") → 100000
  → getDoubleParam("royalty_percentage") → 5.0
  → Calculate: 100000 * (5.0 / 100) = 5000
  → createResponse("success", data)

                     ↓

SERVER RESPONSE:
{
  "jsonrpc": "2.0",
  "id": 3,
  "result": {
    "status": "success",
    "data": {
      "revenue": 100000,
      "royalty_percentage": 5.0,
      "calculated_royalty": 5000.0
    },
    "timestamp": 1710595200000,
    "agent": "ROYALTY_CALCULATION_AGENT"
  }
}
```

---

## Agent State Management

### Stateless Design (Default)

Each agent is **stateless** and processes requests independently:

```java
public class RoyaltyCalculationAgent extends BaseAgent {
    // No instance variables (except inherited logger)
    
    @Override
    protected JsonNode executeToolLogic(String toolName, JsonNode arguments) {
        // Pure function: same inputs → same outputs
        double revenue = getDoubleParam(arguments, "revenue", 0.0);
        double result = calculate(revenue);
        return createResponse("success", result);
    }
}
```

### Optional: Persistent State

If needed, add state management:

```java
public class RoyaltyWalletAgent extends BaseAgent {
    private Map<String, Double> wallets = new ConcurrentHashMap<>();
    
    @Override
    protected JsonNode executeToolLogic(String toolName, JsonNode arguments) {
        String walletId = getStringParam(arguments, "wallet_id", "default");
        
        // Access/modify state
        double balance = wallets.getOrDefault(walletId, 0.0);
        wallets.put(walletId, balance + 1000);
        
        return createResponse("success", wallets.get(walletId));
    }
}
```

---

## Error Handling

### Agent-Level Exception Handling

```java
@Override
public JsonNode execute(String toolName, JsonNode arguments) throws Exception {
    logger.debug("🔧 {} executing: {}", getAgentName(), toolName);
    
    try {
        return executeToolLogic(toolName, arguments);
    } catch (Exception e) {
        logger.error("❌ {} tool {} failed: {}", 
            getAgentName(), toolName, e.getMessage());
        throw e;  // Re-throw to MessageHandler
    }
}
```

### Message Handler-Level Exception Handling

```java
try {
    JsonNode result = agent.execute(toolName, arguments);
    return buildResponse(id, result);
} catch (Exception e) {
    logger.error("❌ Agent execution failed: {}", e.getMessage());
    return buildErrorResponse(id, -32603, "Agent execution error: " + e.getMessage());
}
```

### Error Response Format

```json
{
  "jsonrpc": "2.0",
  "id": "request-id",
  "error": {
    "code": -32603,
    "message": "Agent execution error: Division by zero"
  }
}
```

---

## Performance Characteristics

### Request Latency Breakdown

```
Total: ~5-15ms per request

├─ JSON Parse:        ~1ms
├─ Agent Lookup:      <1ms
├─ Business Logic:    1-10ms (depends on agent)
├─ Response Build:    ~1ms
├─ JSON Serialize:    ~1ms
└─ stdout.flush():    ~1ms
```

### Memory Usage

```
Baseline (empty server):  ~80MB (JVM overhead)
Per Agent Instance:       ~5KB
Per Tool Definition:      ~2KB
Total for 18 Agents:      ~150MB (peak)
```

### Throughput

```
Sequential (stdio):       1-10 requests/sec (I/O bound)
Parallel (HTTP layer):    100-1000 requests/sec
CPU Bound (no I/O):       10k+ requests/sec
```

---

## Concurrency

### Thread Safety

The current implementation is **single-threaded** because stdio is inherently sequential:

```
Request 1 → Process → Response 1
Request 2 → Process → Response 2
Request 3 → Process → Response 3
```

### If Parallel Processing is Needed

Wrap with async HTTP server (Spring Boot, Quarkus):

```java
@RestController
@RequestMapping("/mcp")
public class MCPRestController {
    @PostMapping("/tools/call")
    public CompletableFuture<JsonNode> callTool(@RequestBody JsonNode params) {
        return CompletableFuture.supplyAsync(() -> 
            messageHandler.handle(params)
        );
    }
}
```

---

## Extension Points

### 1. Add New Agent

**File:** `src/main/java/com/ecoskiller/mcp/agents/MyAgent.java`

```java
public class MyNewAgent extends BaseAgent {
    @Override
    public String getAgentName() { return "MY_NEW_AGENT"; }

    @Override
    public String getDescription() { return "Description"; }

    @Override
    protected JsonNode executeToolLogic(String toolName, JsonNode arguments) {
        // Your logic here
        return createResponse("success", data);
    }
}
```

**Register in MessageHandler:**

```java
agents.put("my_new_tool", new MyNewAgent());
```

**Register in ToolRegistry:**

```java
tools.add(createTool("my_new_tool", "MY_NEW_AGENT", 
    "Description", new String[]{"param1", "param2"}));
```

### 2. Add Authentication

**File:** `src/main/java/com/ecoskiller/mcp/handlers/AuthenticationHandler.java`

```java
public class AuthenticationHandler {
    public boolean authenticate(JsonNode request) {
        String token = request.path("auth").path("token").asText();
        return validateToken(token);
    }
    
    private boolean validateToken(String token) {
        // Your validation logic
        return true;
    }
}
```

**Use in MessageHandler:**

```java
if (!authHandler.authenticate(request)) {
    return buildErrorResponse(id, -32600, "Unauthorized");
}
```

### 3. Add Metrics/Monitoring

```java
public class MetricsCollector {
    private AtomicLong totalRequests = new AtomicLong(0);
    private AtomicLong totalErrors = new AtomicLong(0);
    private Map<String, AtomicLong> agentStats = new ConcurrentHashMap<>();
    
    public void recordRequest(String agentName) {
        totalRequests.incrementAndGet();
        agentStats.computeIfAbsent(agentName, k -> new AtomicLong(0))
                  .incrementAndGet();
    }
    
    public void recordError() {
        totalErrors.incrementAndGet();
    }
    
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("total_requests", totalRequests.get());
        metrics.put("total_errors", totalErrors.get());
        metrics.put("agent_stats", agentStats);
        return metrics;
    }
}
```

---

## Deployment Scenarios

### Scenario 1: Claude Desktop

```
User runs Claude Desktop
    ↓
Reads ~/.claude_desktop_config.json
    ↓
Spawns JVM: java -jar /path/to/mcp-17-royalty.jar
    ↓
Connects via stdio
    ↓
Sends JSON-RPC requests
```

### Scenario 2: Docker Container

```
docker run -i ecoskiller/mcp-17-royalty:latest
    ↓
JVM starts inside container
    ↓
Accepts stdio connections
    ↓
Processes requests
```

### Scenario 3: HTTP Bridge (with Spring Boot)

```
HTTP Client (REST API)
    ↓
Spring Boot Controller
    ↓
MessageHandler (existing)
    ↓
Agents (existing)
```

---

## Testing Strategy

### Unit Tests

Test each agent independently:

```java
@Test
public void testRoyaltyCalculation() {
    Agent agent = new RoyaltyCalculationAgent();
    ObjectNode args = JsonNodeFactory.instance.objectNode();
    args.put("revenue", 100000);
    args.put("royalty_percentage", 5.0);
    
    JsonNode result = agent.execute("royalty_calculation", args);
    
    assertEquals("success", result.path("status").asText());
    assertEquals(5000.0, result.path("data").path("calculated_royalty").asDouble());
}
```

### Integration Tests

Test message flow end-to-end:

```java
@Test
public void testFullMessageFlow() {
    MessageHandler handler = new MessageHandler();
    
    ObjectNode request = JsonNodeFactory.instance.objectNode();
    request.put("jsonrpc", "2.0");
    request.put("id", 1);
    request.put("method", "tools/call");
    
    ObjectNode params = JsonNodeFactory.instance.objectNode();
    params.put("name", "royalty_calculation");
    ObjectNode args = JsonNodeFactory.instance.objectNode();
    args.put("revenue", 100000);
    params.set("arguments", args);
    request.set("params", params);
    
    JsonNode response = handler.handle(request);
    
    assertEquals("2.0", response.path("jsonrpc").asText());
    assertEquals("success", response.path("result").path("status").asText());
}
```

### Load Tests

Test under high load:

```bash
# Using Apache JMeter
# 1000 requests, 10 threads, royalty_calculation
jmeter -n -t load_test.jmx -l results.jtl -e -o report
```

---

## Monitoring & Observability

### Logging Levels

- **DEBUG:** Entry/exit, parameter values, decision points
- **INFO:** Agent initialization, tool calls, successful operations
- **WARN:** Recoverable errors, deprecated calls
- **ERROR:** Failures, exceptions, crashes

### Metrics to Track

```
- Requests per second (RPS)
- Error rate
- P50/P95/P99 latency
- Agent execution times
- Memory usage over time
```

### Example Prometheus Integration

```java
Counter totalRequests = Counter.builder("mcp_requests_total")
    .description("Total MCP requests")
    .tag("category", "CAT-17")
    .register(registry);

Timer executionTime = Timer.builder("mcp_execution_duration_seconds")
    .description("MCP tool execution time")
    .register(registry);
```

---

## Security Considerations

### Input Validation

```java
protected void validateArguments(JsonNode arguments) throws IllegalArgumentException {
    if (arguments == null || !arguments.isObject()) {
        throw new IllegalArgumentException("Arguments must be JSON object");
    }
    
    if (arguments.size() > 1000) {
        throw new IllegalArgumentException("Too many arguments");
    }
}
```

### SQL Injection Prevention

If using databases:

```java
// WRONG:
String query = "SELECT * FROM wallets WHERE id = '" + walletId + "'";

// RIGHT (use prepared statements):
String query = "SELECT * FROM wallets WHERE id = ?";
pstmt = conn.prepareStatement(query);
pstmt.setString(1, walletId);
```

### Rate Limiting

```java
public class RateLimiter {
    private Map<String, Queue<Long>> requestTimes = new ConcurrentHashMap<>();
    private final int maxRequests = 1000;
    private final long windowMs = 60000;
    
    public boolean isAllowed(String clientId) {
        Queue<Long> times = requestTimes.computeIfAbsent(clientId, k -> 
            new ConcurrentLinkedQueue<>());
        
        long now = System.currentTimeMillis();
        times.removeIf(t -> now - t > windowMs);
        
        if (times.size() >= maxRequests) {
            return false;
        }
        times.add(now);
        return true;
    }
}
```

---

## Troubleshooting Guide

### Server Won't Start

**Symptom:** `Exception in thread "main" java.lang.NoClassDefFoundError`

**Cause:** Missing Jackson JAR in classpath

**Fix:** Rebuild with Maven:
```bash
mvn clean package
```

### Tools Not Found

**Symptom:** `Method not found: tools/call` or agent returns error

**Cause:** Tool not registered in MessageHandler

**Fix:** Check both:
1. Agent registered in `MessageHandler.initializeAgents()`
2. Tool added to `ToolRegistry.getToolsList()`

### High Memory Usage

**Symptom:** JVM heap > 512MB

**Cause:** Agent retaining data (see State Management section)

**Fix:** Limit heap with JVM flag:
```bash
java -Xmx256m -jar target/mcp-17-royalty.jar
```

---

This architecture document provides a complete blueprint for understanding, extending, and operating the MCP-17 Royalty Server in Java.
