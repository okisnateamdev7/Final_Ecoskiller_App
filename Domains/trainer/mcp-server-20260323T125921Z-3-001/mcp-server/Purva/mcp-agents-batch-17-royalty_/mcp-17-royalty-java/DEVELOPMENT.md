# Development Guide — MCP-17 Royalty Server

## Table of Contents
1. [Local Development Setup](#setup)
2. [Creating Custom Agents](#agents)
3. [Testing](#testing)
4. [Debugging](#debugging)
5. [Performance Optimization](#optimization)
6. [Common Integrations](#integrations)

---

## Local Development Setup {#setup}

### Prerequisites

- Java 11+ (OpenJDK or Oracle JDK)
- Maven 3.6+
- Git
- IDE (IntelliJ IDEA, VS Code, or Eclipse)

### Getting Started

#### 1. Clone/Extract the Repository

```bash
cd /path/to/mcp-17-royalty-java
```

#### 2. Open in IDE

**IntelliJ IDEA:**
- File → Open → Select project folder
- Maven should auto-detect `pom.xml`
- IDE will download dependencies

**VS Code:**
- Open folder in VS Code
- Install "Extension Pack for Java" (Microsoft)
- Maven extension will detect project

**Eclipse:**
- File → Import → Existing Maven Projects
- Select project folder

#### 3. Build & Run

```bash
# Build
mvn clean install

# Run tests
mvn test

# Run server
mvn exec:java -Dexec.mainClass="com.ecoskiller.mcp.server.MCPServer"
```

#### 4. IDE Run Configurations

**IntelliJ:**
```
Run → Edit Configurations
+ New Configuration → Application
Main class: com.ecoskiller.mcp.server.MCPServer
```

**VS Code:**
```json
// .vscode/launch.json
{
  "version": "0.2.0",
  "configurations": [
    {
      "name": "MCP-17 Server",
      "type": "java",
      "name": "Launch MCPServer",
      "request": "launch",
      "mainClass": "com.ecoskiller.mcp.server.MCPServer",
      "projectName": "mcp-17-royalty",
      "cwd": "${workspaceFolder}"
    }
  ]
}
```

---

## Creating Custom Agents {#agents}

### Step 1: Create Agent Class

**File:** `src/main/java/com/ecoskiller/mcp/agents/MyCustomAgent.java`

```java
package com.ecoskiller.mcp.agents;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MyCustomAgent extends BaseAgent {
    
    @Override
    public String getAgentName() {
        return "MY_CUSTOM_AGENT";
    }

    @Override
    public String getDescription() {
        return "My custom agent that does something awesome";
    }

    @Override
    protected JsonNode executeToolLogic(String toolName, JsonNode arguments) throws Exception {
        logger.info("🔧 {} executing tool: {}", getAgentName(), toolName);
        
        // Extract parameters
        String param1 = getStringParam(arguments, "param1", "default");
        double param2 = getDoubleParam(arguments, "param2", 0.0);
        
        // Business logic
        String result = doSomething(param1, param2);
        
        // Return response
        ObjectNode data = JsonNodeFactory.instance.objectNode();
        data.put("result", result);
        data.put("param1_received", param1);
        data.put("param2_received", param2);
        
        return createResponse("success", data);
    }
    
    private String doSomething(String param1, double param2) {
        // Your business logic here
        return param1 + "_" + param2;
    }
}
```

### Step 2: Register in MessageHandler

**File:** `src/main/java/com/ecoskiller/mcp/handlers/MessageHandler.java`

In `initializeAgents()` method, add:

```java
private void initializeAgents() {
    // ... existing agents ...
    
    // Add your new agent
    agents.put("my_custom_tool", new MyCustomAgent());
    
    logger.info("✅ Initialized {} agents for CAT-17", agents.size());
}
```

### Step 3: Add to Tool Registry

**File:** `src/main/java/com/ecoskiller/mcp/handlers/ToolRegistry.java`

In `getToolsList()` method, add:

```java
// MY_CUSTOM_AGENT
tools.add(createTool("my_custom_tool", "MY_CUSTOM_AGENT",
    "My custom agent that does something awesome",
    new String[]{"param1", "param2"}));
```

### Step 4: Test Your Agent

```java
// Quick test
MyCustomAgent agent = new MyCustomAgent();
ObjectNode args = JsonNodeFactory.instance.objectNode();
args.put("param1", "test");
args.put("param2", 42.0);

JsonNode result = agent.execute("my_custom_tool", args);
System.out.println(result.toString());
```

### Step 5: Build & Deploy

```bash
mvn clean package
java -jar target/mcp-17-royalty.jar
```

---

## Testing {#testing}

### Unit Testing

Create test file: `src/test/java/com/ecoskiller/mcp/agents/MyCustomAgentTest.java`

```java
package com.ecoskiller.mcp.agents;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class MyCustomAgentTest {
    private final ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void testBasicExecution() throws Exception {
        MyCustomAgent agent = new MyCustomAgent();
        
        ObjectNode args = JsonNodeFactory.instance.objectNode();
        args.put("param1", "hello");
        args.put("param2", 99.9);
        
        JsonNode result = agent.execute("my_custom_tool", args);
        
        assertEquals("success", result.path("status").asText());
        assertEquals("hello", result.path("data").path("param1_received").asText());
        assertEquals(99.9, result.path("data").path("param2_received").asDouble(), 0.01);
    }
    
    @Test
    public void testWithMissingParameters() throws Exception {
        MyCustomAgent agent = new MyCustomAgent();
        
        ObjectNode args = JsonNodeFactory.instance.objectNode();
        // param1 missing, should use default
        
        JsonNode result = agent.execute("my_custom_tool", args);
        
        assertEquals("success", result.path("status").asText());
        assertEquals("default", result.path("data").path("param1_received").asText());
    }
    
    @Test
    public void testErrorHandling() throws Exception {
        MyCustomAgent agent = new MyCustomAgent();
        
        ObjectNode args = JsonNodeFactory.instance.objectNode();
        args.put("param1", null);  // Invalid input
        
        try {
            agent.execute("my_custom_tool", args);
            fail("Should have thrown exception");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Invalid"));
        }
    }
}
```

### Run Tests

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=MyCustomAgentTest

# Run with coverage
mvn jacoco:report
# Open target/site/jacoco/index.html
```

### Integration Testing

**File:** `src/test/java/com/ecoskiller/mcp/handlers/MessageHandlerTest.java`

```java
public class MessageHandlerTest {
    private final MessageHandler handler = new MessageHandler();
    private final ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void testFullMessageFlow() throws Exception {
        // Create request
        ObjectNode request = JsonNodeFactory.instance.objectNode();
        request.put("jsonrpc", "2.0");
        request.put("id", 1);
        request.put("method", "tools/call");
        
        ObjectNode params = JsonNodeFactory.instance.objectNode();
        params.put("name", "my_custom_tool");
        ObjectNode args = JsonNodeFactory.instance.objectNode();
        args.put("param1", "test");
        params.set("arguments", args);
        request.set("params", params);
        
        // Execute
        JsonNode response = handler.handle(request);
        
        // Assert
        assertEquals("2.0", response.path("jsonrpc").asText());
        assertEquals("success", response.path("result").path("status").asText());
    }
}
```

---

## Debugging {#debugging}

### Enable Debug Logging

**Option 1: Command Line**

```bash
JAVA_OPTS="-Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG" \
java -jar target/mcp-17-royalty.jar
```

**Option 2: Programmatically**

Add to `MCPServer.start()`:

```java
// Set to DEBUG for development
System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");
```

### Debug in IDE

**IntelliJ IDEA:**

1. Set breakpoints (click line number)
2. Run → Debug 'MCPServer'
3. Debugger toolbar appears
4. Step through code (F10=step, F11=step into)

**VS Code:**

1. Click line to set breakpoint
2. Run → Start Debugging
3. Use debug controls in sidebar

### Add Debug Statements

```java
logger.debug("Parameter received: {}", param1);
logger.debug("Calculated result: {}", result);
logger.debug("Response: {}", mapper.writeValueAsString(response));
```

### Test with Sample JSON

Create `test_request.json`:

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "tools/call",
  "params": {
    "name": "my_custom_tool",
    "arguments": {
      "param1": "test",
      "param2": 42.0
    }
  }
}
```

Test with:

```bash
# Send request via echo
echo '{"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"my_custom_tool","arguments":{"param1":"test"}}}' | java -jar target/mcp-17-royalty.jar
```

---

## Performance Optimization {#optimization}

### Profiling

**Using Java Flight Recorder:**

```bash
java -XX:StartFlightRecording=duration=60s,filename=recording.jfr \
     -jar target/mcp-17-royalty.jar
     
# Analyze with JDK Mission Control or IntelliJ
```

**Using YourKit Profiler:**

```bash
# Download from yourkit.com
java -agentpath:/path/to/yjpagent.so \
     -jar target/mcp-17-royalty.jar
```

### Memory Optimization

**Reduce heap size:**

```bash
java -Xmx256m -Xms128m -jar target/mcp-17-royalty.jar
```

**Enable aggressive JIT:**

```bash
java -XX:+TieredCompilation \
     -XX:TieredStopAtLevel=4 \
     -jar target/mcp-17-royalty.jar
```

### Cache Optimization

```java
public class RoyaltyCalculationAgent extends BaseAgent {
    private static final Map<String, Double> taxRateCache = 
        new ConcurrentHashMap<>();
    
    private double getTaxRate(String region) {
        return taxRateCache.computeIfAbsent(region, key -> 
            fetchTaxRate(key)  // Only fetch once
        );
    }
}
```

---

## Common Integrations {#integrations}

### Database Integration

**Add dependency to `pom.xml`:**

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

**Create Database Manager:**

```java
public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost/ecoskiller";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static double getRoyaltyRate(String entityId) throws SQLException {
        String query = "SELECT rate FROM royalty_rates WHERE entity_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, entityId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("rate");
            }
        }
        return 0.0;
    }
}
```

**Use in Agent:**

```java
public class RoyaltyCalculationAgent extends BaseAgent {
    @Override
    protected JsonNode executeToolLogic(String toolName, JsonNode arguments) throws Exception {
        double rate = DatabaseManager.getRoyaltyRate(entityId);
        // ... rest of logic
    }
}
```

### HTTP API Integration

**Add dependency:**

```xml
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.11.0</version>
</dependency>
```

**Create HTTP Client:**

```java
public class HttpClient {
    private static final OkHttpClient client = new OkHttpClient();
    
    public static String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, 
            MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
```

**Use in Agent:**

```java
String response = HttpClient.post("https://api.example.com/validate", jsonPayload);
JsonNode result = mapper.readTree(response);
```

### Kafka Integration

**Add dependency:**

```xml
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>3.5.1</version>
</dependency>
```

**Create Event Publisher:**

```java
public class KafkaEventPublisher {
    private final KafkaProducer<String, String> producer;
    
    public KafkaEventPublisher(String bootstrapServers) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
    }
    
    public void publishEvent(String topic, String key, String value) {
        ProducerRecord<String, String> record = 
            new ProducerRecord<>(topic, key, value);
        producer.send(record);
    }
}
```

**Use in Agent:**

```java
KafkaEventPublisher publisher = new KafkaEventPublisher("localhost:9092");
publisher.publishEvent("royalty-events", entityId, mapper.writeValueAsString(data));
```

### Redis Caching

**Add dependency:**

```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>5.0.1</version>
</dependency>
```

**Create Cache Manager:**

```java
public class CacheManager {
    private static final Jedis jedis = new Jedis("localhost", 6379);
    
    public static String get(String key) {
        return jedis.get(key);
    }
    
    public static void set(String key, String value, int ttlSeconds) {
        jedis.setex(key, ttlSeconds, value);
    }
}
```

**Use in Agent:**

```java
String cacheKey = "rate_" + entityId;
String cached = CacheManager.get(cacheKey);
if (cached == null) {
    double rate = calculateRate(entityId);
    CacheManager.set(cacheKey, String.valueOf(rate), 3600);
} else {
    rate = Double.parseDouble(cached);
}
```

---

## Best Practices

### Code Quality

```bash
# Use static analysis (SonarQube)
mvn sonar:sonar

# Code formatter (Google style)
mvn fmt:format
```

### Version Control

```bash
# Initialize git
git init
git add .
git commit -m "Initial MCP-17 Java implementation"

# Create branches for features
git checkout -b feature/new-agent-xyz
# ... make changes ...
git commit -am "Add new agent"
git push origin feature/new-agent-xyz
```

### Documentation

- Document all public methods with Javadoc
- Update README.md for new features
- Add inline comments for complex logic
- Create CHANGELOG.md entries

### Security

- Use parameterized queries for SQL
- Validate all inputs
- Never hardcode credentials (use environment variables)
- Use TLS/SSL for network communication

---

This guide covers all aspects of developing with the MCP-17 Royalty Server in Java.
