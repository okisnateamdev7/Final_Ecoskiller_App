# 🔍 PHONE_END_TO_END_TRACE_AGENT v1.0
**Status:** LOCKED · SEALED · DETERMINISTIC · PRODUCTION-GRADE  
**Mutation Policy:** Add-only via version bump  
**Execution Authority:** Automated distributed tracing with forensic capability  
**Artifact Class:** End-to-End Request Observability & Performance Intelligence System  
**Platform Target:** ECOSKILLER Multi-Tenant SaaS Platform  
**Deployment Context:** Multi-Environment (DEV · TEST · STAGING · PRODUCTION)

---

## 🔒 SECTION 1 — SYSTEM IDENTITY & PURPOSE

### A. Agent Identity
```
Agent Name: PHONE_END_TO_END_TRACE_AGENT
Agent Type: Distributed Request Tracing & Performance Intelligence System
Execution Mode: Real-Time Continuous Trace Collection & Analysis
Failure Philosophy: Capture → Correlate → Analyze → Alert → Optimize
```

### B. Core Responsibility
The PHONE_END_TO_END_TRACE_AGENT is a deterministic, context-propagating distributed tracing system that:

1. **Traces every request** across all microservices, databases, caches, queues, and external APIs
2. **Captures complete request lifecycle** from ingress to response with microsecond precision
3. **Identifies performance bottlenecks** through span analysis and critical path detection
4. **Correlates errors** across distributed service boundaries
5. **Enables forensic debugging** of production issues without code changes
6. **Detects anomalies** in latency patterns and dependency failures
7. **Maintains immutable trace audit** for compliance and SLA verification
8. **Provides business context** by linking traces to tenants, users, and transactions

### C. What This Agent Does NOT Do
- Does NOT sample production traffic (captures 100% of critical paths)
- Does NOT introduce >1ms overhead per service hop
- Does NOT expose sensitive data in trace attributes
- Does NOT allow trace data modification after capture
- Does NOT make architectural decisions based on trace data alone

---

## 🔒 SECTION 2 — DISTRIBUTED TRACING TAXONOMY

### A. Trace Structure Hierarchy

```
TRACE (end-to-end request journey)
  │
  ├─ ROOT SPAN (ingress entry point)
  │   │
  │   ├─ CHILD SPAN (service → service call)
  │   │   │
  │   │   ├─ CHILD SPAN (service → database)
  │   │   │   └─ ATTRIBUTES (query, rows, latency)
  │   │   │
  │   │   ├─ CHILD SPAN (service → cache)
  │   │   │   └─ ATTRIBUTES (cache hit/miss, key)
  │   │   │
  │   │   └─ CHILD SPAN (service → external API)
  │   │       └─ ATTRIBUTES (endpoint, status, retry count)
  │   │
  │   └─ CHILD SPAN (async event emission)
  │       └─ LINKED SPAN (background job execution)
  │
  └─ TRACE METADATA
      ├─ trace_id (globally unique)
      ├─ tenant_id (multi-tenant isolation)
      ├─ user_id (user context)
      ├─ session_id (user session)
      ├─ transaction_type (business context)
      └─ environment (dev/test/staging/production)
```

### B. Span Categories

#### 1. Service-to-Service Spans
```yaml
http_request_span:
  name: "POST /api/v1/jobs"
  kind: SERVER
  attributes:
    http.method: POST
    http.route: /api/v1/jobs
    http.status_code: 201
    http.request_content_length: 1024
    http.response_content_length: 256
    service.name: job-service
    service.version: 1.2.3
    tenant.id: tenant_abc123
    user.id: user_xyz789
```

#### 2. Database Spans
```yaml
database_query_span:
  name: "SELECT jobs"
  kind: CLIENT
  attributes:
    db.system: postgresql
    db.connection_string: postgresql://db-primary:5432/ecoskiller
    db.statement: "SELECT * FROM jobs WHERE tenant_id = $1 LIMIT 20"
    db.operation: SELECT
    db.rows_affected: 15
    db.query_duration_ms: 12.5
    db.connection_pool.active: 5
    db.connection_pool.idle: 15
```

#### 3. Cache Spans
```yaml
cache_lookup_span:
  name: "GET user:session:xyz789"
  kind: CLIENT
  attributes:
    cache.system: redis
    cache.operation: GET
    cache.key: "user:session:xyz789"
    cache.hit: true
    cache.ttl_remaining_seconds: 3456
    cache.latency_ms: 0.8
```

#### 4. Message Queue Spans
```yaml
message_publish_span:
  name: "PUBLISH job.created"
  kind: PRODUCER
  attributes:
    messaging.system: kafka
    messaging.destination: job-events
    messaging.destination_kind: topic
    messaging.message_id: msg_123abc
    messaging.message_payload_size_bytes: 512
    messaging.partition: 3

message_consume_span:
  name: "CONSUME job.created"
  kind: CONSUMER
  attributes:
    messaging.system: kafka
    messaging.destination: job-events
    messaging.consumer_group: analytics-consumer
    messaging.message_id: msg_123abc
    messaging.kafka.partition: 3
    messaging.kafka.offset: 789456
```

#### 5. External API Spans
```yaml
external_api_span:
  name: "POST https://api.stripe.com/v1/charges"
  kind: CLIENT
  attributes:
    http.method: POST
    http.url: https://api.stripe.com/v1/charges
    http.status_code: 200
    net.peer.name: api.stripe.com
    net.peer.port: 443
    external.service: stripe
    external.operation: create_charge
    external.retry_count: 0
    external.latency_ms: 245
```

#### 6. Background Job Spans
```yaml
background_job_span:
  name: "process_payment_settlement"
  kind: INTERNAL
  attributes:
    job.type: payment_settlement
    job.queue: high_priority
    job.attempts: 1
    job.max_attempts: 3
    job.started_at: "2026-03-04T10:15:23Z"
    job.completed_at: "2026-03-04T10:15:45Z"
    job.duration_seconds: 22
```

#### 7. Real-Time Media Spans
```yaml
jitsi_conference_span:
  name: "voice_gd_session_create"
  kind: SERVER
  attributes:
    media.system: jitsi
    media.room_id: "gd_banking_20260304_1234"
    media.participant_count: 6
    media.duration_seconds: 1800
    media.sfu_load_percent: 45
    media.quality_score: 0.92
```

---

## 🔒 SECTION 3 — OPENTELEMETRY INSTRUMENTATION SPECIFICATION

### A. Language-Specific SDKs

#### Python (FastAPI Backend Services)
```python
# requirements.txt
opentelemetry-api==1.21.0
opentelemetry-sdk==1.21.0
opentelemetry-instrumentation-fastapi==0.42b0
opentelemetry-instrumentation-sqlalchemy==0.42b0
opentelemetry-instrumentation-redis==0.42b0
opentelemetry-instrumentation-kafka-python==0.42b0
opentelemetry-instrumentation-requests==0.42b0
opentelemetry-exporter-otlp==1.21.0

# main.py - FastAPI Application Instrumentation
from fastapi import FastAPI, Request
from opentelemetry import trace
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor
from opentelemetry.instrumentation.sqlalchemy import SQLAlchemyInstrumentor
from opentelemetry.instrumentation.redis import RedisInstrumentor
from opentelemetry.sdk.resources import Resource, SERVICE_NAME, SERVICE_VERSION

# Configure tracer provider
resource = Resource(attributes={
    SERVICE_NAME: "auth-service",
    SERVICE_VERSION: "1.2.3",
    "deployment.environment": "production",
    "service.namespace": "ecoskiller",
})

trace.set_tracer_provider(TracerProvider(resource=resource))
tracer_provider = trace.get_tracer_provider()

# Configure OTLP exporter
otlp_exporter = OTLPSpanExporter(
    endpoint="http://otel-collector.monitoring.svc:4317",
    insecure=True
)

# Add batch span processor
tracer_provider.add_span_processor(
    BatchSpanProcessor(otlp_exporter, max_export_batch_size=512)
)

# Get tracer
tracer = trace.get_tracer(__name__)

# Create FastAPI app
app = FastAPI(title="Auth Service")

# Auto-instrument FastAPI
FastAPIInstrumentor.instrument_app(app)

# Auto-instrument SQLAlchemy
from database import engine
SQLAlchemyInstrumentor().instrument(engine=engine)

# Auto-instrument Redis
from redis_client import redis_client
RedisInstrumentor().instrument(redis_client=redis_client)

# Custom middleware for business context injection
@app.middleware("http")
async def add_business_context(request: Request, call_next):
    # Extract tenant and user from JWT or headers
    tenant_id = request.headers.get("X-Tenant-ID", "unknown")
    user_id = request.headers.get("X-User-ID", "unknown")
    
    # Get current span
    current_span = trace.get_current_span()
    
    # Add business context attributes
    current_span.set_attribute("tenant.id", tenant_id)
    current_span.set_attribute("user.id", user_id)
    current_span.set_attribute("request.path", request.url.path)
    current_span.set_attribute("request.method", request.method)
    
    # Process request
    response = await call_next(request)
    
    # Add response context
    current_span.set_attribute("http.status_code", response.status_code)
    
    return response

# Example endpoint with manual span creation
@app.post("/api/v1/auth/login")
async def login(credentials: LoginRequest):
    with tracer.start_as_current_span("validate_credentials") as span:
        span.set_attribute("auth.method", "password")
        
        # Validate credentials
        user = await validate_user(credentials.email, credentials.password)
        
        if not user:
            span.set_attribute("auth.result", "failed")
            span.add_event("authentication_failed", {
                "reason": "invalid_credentials",
                "email": credentials.email
            })
            raise HTTPException(status_code=401)
        
        span.set_attribute("auth.result", "success")
        span.set_attribute("user.id", user.id)
    
    with tracer.start_as_current_span("generate_jwt_tokens") as span:
        access_token = generate_access_token(user)
        refresh_token = generate_refresh_token(user)
        
        span.set_attribute("token.type", "jwt")
        span.set_attribute("token.expiry_seconds", 3600)
    
    return {
        "access_token": access_token,
        "refresh_token": refresh_token
    }

# Database query instrumentation (automatic via SQLAlchemy)
async def get_user_by_email(email: str):
    # This query is automatically traced by SQLAlchemyInstrumentor
    return await db.query(User).filter(User.email == email).first()

# Redis cache instrumentation (automatic via RedisInstrumentor)
async def get_cached_session(session_id: str):
    # This Redis call is automatically traced
    return await redis_client.get(f"session:{session_id}")

# Manual span for complex operations
async def process_payment(payment_data: dict):
    with tracer.start_as_current_span("process_payment") as span:
        span.set_attribute("payment.amount", payment_data["amount"])
        span.set_attribute("payment.currency", payment_data["currency"])
        
        # Call external payment gateway
        with tracer.start_as_current_span("stripe_api_call") as stripe_span:
            stripe_span.set_attribute("external.service", "stripe")
            stripe_span.set_attribute("external.operation", "create_charge")
            
            try:
                result = await stripe.create_charge(payment_data)
                stripe_span.set_attribute("payment.status", "success")
                stripe_span.set_attribute("payment.charge_id", result.id)
            except Exception as e:
                stripe_span.set_attribute("payment.status", "failed")
                stripe_span.record_exception(e)
                stripe_span.set_status(Status(StatusCode.ERROR, str(e)))
                raise
        
        return result
```

#### Node.js (WebSocket/Real-Time Services)
```javascript
// package.json dependencies
{
  "dependencies": {
    "@opentelemetry/api": "^1.7.0",
    "@opentelemetry/sdk-node": "^0.45.1",
    "@opentelemetry/auto-instrumentations-node": "^0.40.0",
    "@opentelemetry/exporter-trace-otlp-grpc": "^0.45.1",
    "@opentelemetry/resources": "^1.19.0",
    "@opentelemetry/semantic-conventions": "^1.19.0"
  }
}

// tracing.js - OpenTelemetry Bootstrap
const { NodeSDK } = require('@opentelemetry/sdk-node');
const { getNodeAutoInstrumentations } = require('@opentelemetry/auto-instrumentations-node');
const { OTLPTraceExporter } = require('@opentelemetry/exporter-trace-otlp-grpc');
const { Resource } = require('@opentelemetry/resources');
const { SemanticResourceAttributes } = require('@opentelemetry/semantic-conventions');

const resource = Resource.default().merge(
  new Resource({
    [SemanticResourceAttributes.SERVICE_NAME]: 'websocket-service',
    [SemanticResourceAttributes.SERVICE_VERSION]: '1.0.5',
    [SemanticResourceAttributes.DEPLOYMENT_ENVIRONMENT]: 'production',
    'service.namespace': 'ecoskiller',
  })
);

const traceExporter = new OTLPTraceExporter({
  url: 'http://otel-collector.monitoring.svc:4317',
});

const sdk = new NodeSDK({
  resource,
  traceExporter,
  instrumentations: [
    getNodeAutoInstrumentations({
      '@opentelemetry/instrumentation-http': { enabled: true },
      '@opentelemetry/instrumentation-express': { enabled: true },
      '@opentelemetry/instrumentation-ws': { enabled: true },
      '@opentelemetry/instrumentation-redis': { enabled: true },
      '@opentelemetry/instrumentation-pg': { enabled: true },
    }),
  ],
});

sdk.start();

process.on('SIGTERM', () => {
  sdk.shutdown()
    .then(() => console.log('Tracing terminated'))
    .catch((error) => console.log('Error terminating tracing', error))
    .finally(() => process.exit(0));
});

// app.js - WebSocket Server with Custom Spans
const express = require('express');
const WebSocket = require('ws');
const { trace, context, SpanStatusCode } = require('@opentelemetry/api');

const tracer = trace.getTracer('websocket-service');

const app = express();
const wss = new WebSocket.Server({ noServer: true });

// WebSocket connection handling with tracing
wss.on('connection', (ws, request) => {
  const span = tracer.startSpan('websocket_connection', {
    attributes: {
      'ws.connection_id': generateConnectionId(),
      'tenant.id': request.headers['x-tenant-id'],
      'user.id': request.headers['x-user-id'],
    }
  });

  const ctx = trace.setSpan(context.active(), span);

  ws.on('message', (data) => {
    context.with(ctx, () => {
      const messageSpan = tracer.startSpan('websocket_message_received', {
        attributes: {
          'ws.message_type': data.type,
          'ws.message_size_bytes': data.length,
        }
      });

      try {
        processMessage(data, ws);
        messageSpan.setStatus({ code: SpanStatusCode.OK });
      } catch (error) {
        messageSpan.recordException(error);
        messageSpan.setStatus({ 
          code: SpanStatusCode.ERROR, 
          message: error.message 
        });
      } finally {
        messageSpan.end();
      }
    });
  });

  ws.on('close', () => {
    span.addEvent('websocket_disconnected');
    span.end();
  });
});

// Redis pub/sub with tracing
const Redis = require('ioredis');
const redis = new Redis({ host: 'redis.core.svc' });

async function publishEvent(channel, message) {
  return tracer.startActiveSpan('redis_publish', async (span) => {
    span.setAttribute('messaging.system', 'redis');
    span.setAttribute('messaging.destination', channel);
    span.setAttribute('messaging.message_payload_size_bytes', message.length);

    try {
      await redis.publish(channel, message);
      span.setStatus({ code: SpanStatusCode.OK });
    } catch (error) {
      span.recordException(error);
      span.setStatus({ code: SpanStatusCode.ERROR });
      throw error;
    } finally {
      span.end();
    }
  });
}
```

#### Flutter (Mobile/Desktop Apps)
```dart
// pubspec.yaml
dependencies:
  opentelemetry: ^0.18.0
  http: ^1.1.0

// tracing_config.dart
import 'package:opentelemetry/api.dart';
import 'package:opentelemetry/sdk.dart';

class TracingConfig {
  static late TracerProvider tracerProvider;
  static late Tracer tracer;

  static void initialize() {
    final resource = Resource([
      Attribute.fromString('service.name', 'ecoskiller-mobile-app'),
      Attribute.fromString('service.version', '2.1.0'),
      Attribute.fromString('deployment.environment', 'production'),
    ]);

    tracerProvider = TracerProviderBase(
      processors: [
        BatchSpanProcessor(
          OtlpGrpcSpanExporter(
            Uri.parse('http://otel-collector.ecoskiller.com:4317'),
          ),
        ),
      ],
      resource: resource,
    );

    tracer = tracerProvider.getTracer('ecoskiller-mobile');
  }

  static void shutdown() {
    tracerProvider.shutdown();
  }
}

// http_client_with_tracing.dart
import 'package:http/http.dart' as http;
import 'package:opentelemetry/api.dart';

class TracedHttpClient {
  final Tracer tracer;

  TracedHttpClient(this.tracer);

  Future<http.Response> get(Uri url, {Map<String, String>? headers}) async {
    final span = tracer.startSpan('http_request', kind: SpanKind.client);
    
    span.setAttribute('http.method', 'GET');
    span.setAttribute('http.url', url.toString());
    span.setAttribute('http.scheme', url.scheme);
    span.setAttribute('http.host', url.host);
    
    // Inject trace context into headers
    final contextHeaders = <String, String>{};
    W3CTraceContextPropagator().inject(
      Context.current, 
      contextHeaders, 
      (carrier, key, value) => carrier[key] = value,
    );
    
    final allHeaders = {...?headers, ...contextHeaders};

    try {
      final response = await http.get(url, headers: allHeaders);
      
      span.setAttribute('http.status_code', response.statusCode);
      span.setAttribute('http.response_content_length', response.contentLength ?? 0);
      
      if (response.statusCode >= 400) {
        span.setStatus(StatusCode.error, 'HTTP ${response.statusCode}');
      } else {
        span.setStatus(StatusCode.ok);
      }
      
      return response;
    } catch (e) {
      span.recordException(e);
      span.setStatus(StatusCode.error, e.toString());
      rethrow;
    } finally {
      span.end();
    }
  }
}

// Example usage in app
void main() async {
  TracingConfig.initialize();
  
  runApp(MyApp());
}

class JobListScreen extends StatefulWidget {
  @override
  _JobListScreenState createState() => _JobListScreenState();
}

class _JobListScreenState extends State<JobListScreen> {
  Future<void> loadJobs() async {
    final span = TracingConfig.tracer.startSpan('load_jobs_screen');
    span.setAttribute('screen.name', 'JobList');
    span.setAttribute('user.id', currentUserId);
    span.setAttribute('tenant.id', currentTenantId);

    try {
      final httpClient = TracedHttpClient(TracingConfig.tracer);
      final response = await httpClient.get(
        Uri.parse('https://api.ecoskiller.com/api/v1/jobs'),
      );
      
      final jobs = parseJobs(response.body);
      span.setAttribute('jobs.count', jobs.length);
      
      setState(() {
        this.jobs = jobs;
      });
      
      span.setStatus(StatusCode.ok);
    } catch (e) {
      span.recordException(e);
      span.setStatus(StatusCode.error);
      showError(e.toString());
    } finally {
      span.end();
    }
  }
}
```

---

## 🔒 SECTION 4 — TRACE CONTEXT PROPAGATION

### A. W3C Trace Context Standard

#### HTTP Header Propagation
```
traceparent: 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01
tracestate: ecoskiller=tenant:abc123,user:xyz789,env:prod
```

**Header Breakdown:**
```
traceparent format: {version}-{trace-id}-{parent-span-id}-{trace-flags}

00                               - version
4bf92f3577b34da6a3ce929d0e0e4736 - trace_id (128-bit, 32 hex chars)
00f067aa0ba902b7                 - parent_span_id (64-bit, 16 hex chars)
01                               - trace_flags (sampled=1)

tracestate: vendor-specific key-value pairs
```

#### Service-to-Service Propagation
```python
# Service A → Service B HTTP call with context propagation

import httpx
from opentelemetry import trace
from opentelemetry.propagate import inject

async def call_downstream_service(payload: dict):
    current_span = trace.get_current_span()
    
    # Prepare headers
    headers = {
        "Content-Type": "application/json",
        "X-Tenant-ID": get_current_tenant_id(),
    }
    
    # Inject trace context into headers
    inject(headers)
    
    # Headers now contain:
    # - traceparent: 00-{trace_id}-{span_id}-01
    # - tracestate: ecoskiller=...
    
    async with httpx.AsyncClient() as client:
        response = await client.post(
            "http://job-service.core.svc/api/v1/jobs",
            json=payload,
            headers=headers
        )
    
    return response.json()
```

#### Message Queue Propagation (Kafka)
```python
from kafka import KafkaProducer
from opentelemetry import trace
from opentelemetry.propagate import inject

def publish_job_created_event(job_data: dict):
    current_span = trace.get_current_span()
    
    # Create message headers
    headers = {}
    inject(headers)  # Injects traceparent into headers
    
    # Convert headers to Kafka format
    kafka_headers = [
        (key, value.encode('utf-8')) 
        for key, value in headers.items()
    ]
    
    # Add business context headers
    kafka_headers.extend([
        ('tenant_id', job_data['tenant_id'].encode('utf-8')),
        ('event_type', b'job.created'),
    ])
    
    producer.send(
        topic='job-events',
        value=json.dumps(job_data).encode('utf-8'),
        headers=kafka_headers
    )
```

```python
# Kafka consumer extracting trace context
from kafka import KafkaConsumer
from opentelemetry import trace
from opentelemetry.propagate import extract

consumer = KafkaConsumer('job-events')

for message in consumer:
    # Extract headers from Kafka message
    headers_dict = {
        key: value.decode('utf-8') 
        for key, value in message.headers
    }
    
    # Extract trace context
    ctx = extract(headers_dict)
    
    # Create new span linked to parent trace
    with tracer.start_as_current_span(
        "process_job_created_event",
        context=ctx,
        kind=trace.SpanKind.CONSUMER
    ) as span:
        span.set_attribute("messaging.system", "kafka")
        span.set_attribute("messaging.destination", "job-events")
        span.set_attribute("messaging.kafka.partition", message.partition)
        span.set_attribute("messaging.kafka.offset", message.offset)
        
        # Process message
        process_job_event(message.value)
```

#### Background Job Context Propagation (Redis Queue)
```python
# Enqueue background job with trace context
from rq import Queue
from opentelemetry import trace
from opentelemetry.propagate import inject

def enqueue_payment_processing(payment_id: str):
    current_span = trace.get_current_span()
    
    # Serialize trace context
    trace_context = {}
    inject(trace_context)
    
    # Enqueue job with trace context
    queue = Queue('payments', connection=redis_conn)
    queue.enqueue(
        process_payment_job,
        payment_id,
        meta={
            'trace_context': trace_context,
            'tenant_id': get_current_tenant_id(),
            'user_id': get_current_user_id(),
        }
    )
```

```python
# Background worker extracting trace context
from opentelemetry import trace
from opentelemetry.propagate import extract

def process_payment_job(payment_id: str):
    # Get job metadata
    job = get_current_job()
    trace_context = job.meta.get('trace_context', {})
    
    # Extract parent context
    ctx = extract(trace_context)
    
    # Create linked span
    with tracer.start_as_current_span(
        "background_payment_processing",
        context=ctx,
        kind=trace.SpanKind.INTERNAL
    ) as span:
        span.set_attribute("job.type", "payment_processing")
        span.set_attribute("payment.id", payment_id)
        span.set_attribute("tenant.id", job.meta['tenant_id'])
        
        # Process payment
        result = process_payment(payment_id)
        
        span.set_attribute("payment.status", result.status)
        
        return result
```

---

## 🔒 SECTION 5 — OPENTELEMETRY COLLECTOR CONFIGURATION

### A. Collector Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                  OpenTelemetry Collector                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    RECEIVERS                             │  │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌─────────┐ │  │
│  │  │   OTLP   │  │   OTLP   │  │  Jaeger  │  │  Zipkin │ │  │
│  │  │   gRPC   │  │   HTTP   │  │  Thrift  │  │   HTTP  │ │  │
│  │  │  :4317   │  │  :4318   │  │  :14268  │  │  :9411  │ │  │
│  │  └──────────┘  └──────────┘  └──────────┘  └─────────┘ │  │
│  └──────────────────────────────────────────────────────────┘  │
│                           │                                     │
│  ┌────────────────────────▼──────────────────────────────────┐ │
│  │                   PROCESSORS                              │ │
│  │  ┌──────────────┐  ┌──────────────┐  ┌────────────────┐ │ │
│  │  │    Batch     │  │  Attributes  │  │  Probabilistic │ │ │
│  │  │  Processor   │  │   Processor  │  │    Sampler     │ │ │
│  │  └──────────────┘  └──────────────┘  └────────────────┘ │ │
│  │  ┌──────────────┐  ┌──────────────┐  ┌────────────────┐ │ │
│  │  │   Resource   │  │   Tail-Based │  │    Filter      │ │ │
│  │  │  Processor   │  │    Sampler   │  │   Processor    │ │ │
│  │  └──────────────┘  └──────────────┘  └────────────────┘ │ │
│  └──────────────────────────────────────────────────────────┘ │
│                           │                                     │
│  ┌────────────────────────▼──────────────────────────────────┐ │
│  │                    EXPORTERS                              │ │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌─────────┐  │ │
│  │  │  Jaeger  │  │Prometheus│  │   Loki   │  │  Kafka  │  │ │
│  │  │  Storage │  │  Metrics │  │   Logs   │  │  Events │  │ │
│  │  └──────────┘  └──────────┘  └──────────┘  └─────────┘  │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### B. Collector Deployment Manifest

```yaml
# otel-collector-config.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: otel-collector-config
  namespace: monitoring
data:
  otel-collector-config.yaml: |
    receivers:
      otlp:
        protocols:
          grpc:
            endpoint: 0.0.0.0:4317
          http:
            endpoint: 0.0.0.0:4318
      
      jaeger:
        protocols:
          thrift_http:
            endpoint: 0.0.0.0:14268
      
      zipkin:
        endpoint: 0.0.0.0:9411

    processors:
      # Batch processor - reduces API calls
      batch:
        timeout: 1s
        send_batch_size: 1024
        send_batch_max_size: 2048
      
      # Add resource attributes
      resource:
        attributes:
          - key: cluster.name
            value: ecoskiller-production
            action: upsert
          - key: deployment.environment
            value: production
            action: upsert
      
      # Add custom attributes based on trace data
      attributes:
        actions:
          - key: tenant.id
            from_attribute: http.request.header.x-tenant-id
            action: upsert
          - key: user.id
            from_attribute: http.request.header.x-user-id
            action: upsert
      
      # Tail-based sampling - keep all errors and slow traces
      tail_sampling:
        decision_wait: 10s
        num_traces: 100000
        expected_new_traces_per_sec: 1000
        policies:
          # Always sample errors
          - name: errors
            type: status_code
            status_code:
              status_codes: [ERROR]
          
          # Always sample slow traces (>2s)
          - name: slow-traces
            type: latency
            latency:
              threshold_ms: 2000
          
          # Sample 100% of auth flows
          - name: auth-flows
            type: string_attribute
            string_attribute:
              key: http.route
              values: ["/api/v1/auth/.*"]
              enabled_regex_matching: true
          
          # Sample 100% of payment flows
          - name: payment-flows
            type: string_attribute
            string_attribute:
              key: transaction.type
              values: ["payment", "refund", "settlement"]
          
          # Sample 10% of normal traffic
          - name: probabilistic-normal
            type: probabilistic
            probabilistic:
              sampling_percentage: 10
      
      # Filter out health check traces
      filter:
        traces:
          span:
            - 'attributes["http.route"] == "/health"'
            - 'attributes["http.route"] == "/metrics"'
            - 'attributes["http.route"] == "/ready"'

    exporters:
      # Export to Jaeger for trace storage and UI
      jaeger:
        endpoint: jaeger-collector.monitoring.svc:14250
        tls:
          insecure: true
      
      # Export to Prometheus for metrics
      prometheus:
        endpoint: 0.0.0.0:8889
        namespace: otel
        const_labels:
          cluster: ecoskiller-production
      
      # Export trace events to Kafka for analytics
      kafka:
        brokers:
          - kafka.core.svc:9092
        topic: trace-events
        encoding: otlp_json
        metadata:
          full: true
      
      # Export to OTLP for downstream collectors (optional)
      otlp:
        endpoint: central-otel-collector.ecoskiller.com:4317
        tls:
          insecure: false
          cert_file: /etc/otel/certs/client.crt
          key_file: /etc/otel/certs/client.key

    service:
      pipelines:
        traces:
          receivers: [otlp, jaeger, zipkin]
          processors: [resource, attributes, tail_sampling, filter, batch]
          exporters: [jaeger, kafka]
        
        metrics:
          receivers: [otlp]
          processors: [batch]
          exporters: [prometheus]

---
# otel-collector-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: otel-collector
  namespace: monitoring
spec:
  replicas: 3
  selector:
    matchLabels:
      app: otel-collector
  template:
    metadata:
      labels:
        app: otel-collector
    spec:
      containers:
      - name: otel-collector
        image: otel/opentelemetry-collector-contrib:0.91.0
        args:
          - "--config=/conf/otel-collector-config.yaml"
        ports:
        - containerPort: 4317  # OTLP gRPC
          name: otlp-grpc
        - containerPort: 4318  # OTLP HTTP
          name: otlp-http
        - containerPort: 14268 # Jaeger Thrift
          name: jaeger-thrift
        - containerPort: 9411  # Zipkin
          name: zipkin
        - containerPort: 8889  # Prometheus metrics
          name: prometheus
        volumeMounts:
        - name: otel-collector-config
          mountPath: /conf
        resources:
          requests:
            memory: "1Gi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /
            port: 13133
        readinessProbe:
          httpGet:
            path: /
            port: 13133
      volumes:
      - name: otel-collector-config
        configMap:
          name: otel-collector-config

---
# otel-collector-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: otel-collector
  namespace: monitoring
spec:
  selector:
    app: otel-collector
  ports:
  - name: otlp-grpc
    port: 4317
    targetPort: 4317
    protocol: TCP
  - name: otlp-http
    port: 4318
    targetPort: 4318
    protocol: TCP
  - name: jaeger-thrift
    port: 14268
    targetPort: 14268
    protocol: TCP
  - name: zipkin
    port: 9411
    targetPort: 9411
    protocol: TCP
  - name: prometheus
    port: 8889
    targetPort: 8889
    protocol: TCP
  type: ClusterIP
```

---

## 🔒 SECTION 6 — JAEGER DISTRIBUTED TRACING BACKEND

### A. Jaeger Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      JAEGER STACK                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────────────────────────────────────────────┐  │
│  │             Jaeger Collector                         │  │
│  │  Receives traces from OTel Collector                 │  │
│  │  Validates & Processes spans                         │  │
│  │  Writes to storage backend                           │  │
│  └────────────────┬─────────────────────────────────────┘  │
│                   │                                         │
│                   ▼                                         │
│  ┌──────────────────────────────────────────────────────┐  │
│  │          Storage Backend (OpenSearch)                │  │
│  │  • Span storage with TTL                             │  │
│  │  • Service dependency graph storage                  │  │
│  │  • Index optimization for trace queries             │  │
│  └──────────────────────────────────────────────────────┘  │
│                   │                                         │
│  ┌────────────────┴─────────────┬───────────────────────┐  │
│  │                              │                       │  │
│  ▼                              ▼                       ▼  │
│  ┌──────────────┐  ┌────────────────────┐  ┌───────────┐  │
│  │Jaeger Query  │  │ Jaeger UI (Web)    │  │ Jaeger    │  │
│  │   API        │  │ Trace visualization│  │ Agent     │  │
│  │  Service     │  │ Service map        │  │ (Legacy)  │  │
│  └──────────────┘  └────────────────────┘  └───────────┘  │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### B. Jaeger Deployment

```yaml
# jaeger-opensearch-storage.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: jaeger-opensearch-config
  namespace: monitoring
data:
  ES_TAGS_AS_FIELDS_ALL: "true"
  ES_NUM_SHARDS: "3"
  ES_NUM_REPLICAS: "1"
  ES_INDEX_PREFIX: "jaeger"

---
# jaeger-collector.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: jaeger-collector
  namespace: monitoring
spec:
  replicas: 3
  selector:
    matchLabels:
      app: jaeger-collector
  template:
    metadata:
      labels:
        app: jaeger-collector
    spec:
      containers:
      - name: jaeger-collector
        image: jaegertracing/jaeger-collector:1.52
        ports:
        - containerPort: 14250  # gRPC
          name: grpc
        - containerPort: 14268  # HTTP
          name: http
        - containerPort: 9411   # Zipkin compatible
          name: zipkin
        env:
        - name: SPAN_STORAGE_TYPE
          value: opensearch
        - name: ES_SERVER_URLS
          value: http://opensearch.core.svc:9200
        - name: ES_USERNAME
          valueFrom:
            secretKeyRef:
              name: opensearch-credentials
              key: username
        - name: ES_PASSWORD
          valueFrom:
            secretKeyRef:
              name: opensearch-credentials
              key: password
        - name: COLLECTOR_QUEUE_SIZE
          value: "4000"
        - name: COLLECTOR_NUM_WORKERS
          value: "100"
        envFrom:
        - configMapRef:
            name: jaeger-opensearch-config
        resources:
          requests:
            memory: "1Gi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"

---
# jaeger-query.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: jaeger-query
  namespace: monitoring
spec:
  replicas: 2
  selector:
    matchLabels:
      app: jaeger-query
  template:
    metadata:
      labels:
        app: jaeger-query
    spec:
      containers:
      - name: jaeger-query
        image: jaegertracing/jaeger-query:1.52
        ports:
        - containerPort: 16686  # UI
          name: ui
        - containerPort: 16687  # Admin
          name: admin
        env:
        - name: SPAN_STORAGE_TYPE
          value: opensearch
        - name: ES_SERVER_URLS
          value: http://opensearch.core.svc:9200
        - name: ES_USERNAME
          valueFrom:
            secretKeyRef:
              name: opensearch-credentials
              key: username
        - name: ES_PASSWORD
          valueFrom:
            secretKeyRef:
              name: opensearch-credentials
              key: password
        - name: QUERY_BASE_PATH
          value: /jaeger
        envFrom:
        - configMapRef:
            name: jaeger-opensearch-config
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"

---
# jaeger-ui-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: jaeger-query
  namespace: monitoring
spec:
  selector:
    app: jaeger-query
  ports:
  - name: ui
    port: 16686
    targetPort: 16686
  type: ClusterIP

---
# jaeger-ui-ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: jaeger-ui
  namespace: monitoring
  annotations:
    cert-manager.io/cluster-issuer: letsencrypt-prod
    nginx.ingress.kubernetes.io/auth-type: basic
    nginx.ingress.kubernetes.io/auth-secret: jaeger-basic-auth
    nginx.ingress.kubernetes.io/auth-realm: 'Authentication Required - Jaeger UI'
spec:
  ingressClassName: nginx
  tls:
  - hosts:
    - jaeger.ecoskiller.com
    secretName: jaeger-tls
  rules:
  - host: jaeger.ecoskiller.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: jaeger-query
            port:
              number: 16686
```

---

## 🔒 SECTION 7 — TRACE ANALYSIS & PERFORMANCE INTELLIGENCE

### A. Critical Path Detection

```python
# critical_path_analyzer.py
from dataclasses import dataclass
from typing import List, Dict
import networkx as nx

@dataclass
class Span:
    span_id: str
    parent_span_id: str
    operation_name: str
    start_time_us: int
    duration_us: int
    service_name: str

def analyze_critical_path(trace_spans: List[Span]) -> Dict:
    """
    Identifies the critical path through a distributed trace.
    Critical path = sequence of spans that determines total trace latency.
    """
    # Build dependency graph
    graph = nx.DiGraph()
    
    for span in trace_spans:
        graph.add_node(span.span_id, **{
            'operation': span.operation_name,
            'duration': span.duration_us,
            'service': span.service_name,
            'start': span.start_time_us,
        })
        
        if span.parent_span_id:
            graph.add_edge(span.parent_span_id, span.span_id)
    
    # Find root span (no parent)
    root_spans = [n for n in graph.nodes() if graph.in_degree(n) == 0]
    if not root_spans:
        raise ValueError("No root span found")
    
    root = root_spans[0]
    
    # Calculate critical path using longest path algorithm
    def get_path_duration(path):
        return sum(graph.nodes[node]['duration'] for node in path)
    
    # Find all paths from root to leaves
    leaves = [n for n in graph.nodes() if graph.out_degree(n) == 0]
    critical_path = []
    max_duration = 0
    
    for leaf in leaves:
        try:
            path = nx.shortest_path(graph, root, leaf)
            duration = get_path_duration(path)
            if duration > max_duration:
                max_duration = duration
                critical_path = path
        except nx.NetworkXNoPath:
            continue
    
    # Build analysis result
    critical_spans = []
    for span_id in critical_path:
        node_data = graph.nodes[span_id]
        critical_spans.append({
            'span_id': span_id,
            'operation': node_data['operation'],
            'service': node_data['service'],
            'duration_ms': node_data['duration'] / 1000,
            'duration_percent': (node_data['duration'] / max_duration) * 100
        })
    
    # Identify bottlenecks (spans >20% of critical path)
    bottlenecks = [
        span for span in critical_spans 
        if span['duration_percent'] > 20
    ]
    
    return {
        'total_duration_ms': max_duration / 1000,
        'critical_path': critical_spans,
        'bottlenecks': bottlenecks,
        'path_efficiency': (max_duration / sum(s.duration_us for s in trace_spans)) * 100
    }

# Example usage
trace_analysis = analyze_critical_path(trace_spans)
print(f"Total request latency: {trace_analysis['total_duration_ms']}ms")
print(f"Bottlenecks found: {len(trace_analysis['bottlenecks'])}")
for bottleneck in trace_analysis['bottlenecks']:
    print(f"  - {bottleneck['service']}.{bottleneck['operation']}: "
          f"{bottleneck['duration_ms']}ms ({bottleneck['duration_percent']:.1f}%)")
```

### B. Service Dependency Map Generation

```python
# service_dependency_mapper.py
from collections import defaultdict
from typing import List, Dict, Set
import json

def generate_service_dependency_map(traces: List[Dict]) -> Dict:
    """
    Generates a service dependency map from collected traces.
    Shows which services call which services and with what frequency.
    """
    dependencies = defaultdict(lambda: defaultdict(int))
    service_operations = defaultdict(set)
    error_rates = defaultdict(lambda: {'total': 0, 'errors': 0})
    latencies = defaultdict(list)
    
    for trace in traces:
        for span in trace['spans']:
            service = span.get('service_name', 'unknown')
            operation = span.get('operation_name', 'unknown')
            parent_service = span.get('parent_service_name')
            
            # Track operations per service
            service_operations[service].add(operation)
            
            # Track dependencies
            if parent_service and parent_service != service:
                dependencies[parent_service][service] += 1
            
            # Track error rates
            error_rates[service]['total'] += 1
            if span.get('status_code') == 'ERROR':
                error_rates[service]['errors'] += 1
            
            # Track latencies
            if 'duration_ms' in span:
                latencies[service].append(span['duration_ms'])
    
    # Build dependency graph
    dependency_graph = {
        'nodes': [],
        'edges': []
    }
    
    # Add nodes (services)
    for service in service_operations.keys():
        total = error_rates[service]['total']
        errors = error_rates[service]['errors']
        error_rate = (errors / total * 100) if total > 0 else 0
        
        avg_latency = (
            sum(latencies[service]) / len(latencies[service])
            if latencies[service] else 0
        )
        
        dependency_graph['nodes'].append({
            'id': service,
            'operations': list(service_operations[service]),
            'request_count': total,
            'error_rate_percent': round(error_rate, 2),
            'avg_latency_ms': round(avg_latency, 2)
        })
    
    # Add edges (dependencies)
    for source_service, targets in dependencies.items():
        for target_service, call_count in targets.items():
            dependency_graph['edges'].append({
                'source': source_service,
                'target': target_service,
                'call_count': call_count
            })
    
    return dependency_graph

# Example output visualization
dependency_map = generate_service_dependency_map(collected_traces)

print("Service Dependency Map:")
print(json.dumps(dependency_map, indent=2))

# Output example:
# {
#   "nodes": [
#     {
#       "id": "api-gateway",
#       "operations": ["POST /api/v1/jobs", "GET /api/v1/jobs/:id"],
#       "request_count": 10000,
#       "error_rate_percent": 0.5,
#       "avg_latency_ms": 45.2
#     },
#     {
#       "id": "auth-service",
#       "operations": ["validate_jwt", "refresh_token"],
#       "request_count": 10000,
#       "error_rate_percent": 0.1,
#       "avg_latency_ms": 8.5
#     },
#     {
#       "id": "job-service",
#       "operations": ["create_job", "get_job", "list_jobs"],
#       "request_count": 5000,
#       "error_rate_percent": 1.2,
#       "avg_latency_ms": 125.7
#     }
#   ],
#   "edges": [
#     {"source": "api-gateway", "target": "auth-service", "call_count": 10000},
#     {"source": "api-gateway", "target": "job-service", "call_count": 5000},
#     {"source": "job-service", "target": "postgresql", "call_count": 5000}
#   ]
# }
```

### C. Latency Percentile Analysis

```sql
-- OpenSearch/Elasticsearch query for latency percentiles
GET jaeger-span-*/_search
{
  "size": 0,
  "query": {
    "bool": {
      "filter": [
        {"term": {"process.serviceName": "job-service"}},
        {"term": {"operationName": "create_job"}},
        {"range": {"startTimeMillis": {"gte": "now-1h"}}}
      ]
    }
  },
  "aggs": {
    "latency_percentiles": {
      "percentiles": {
        "field": "duration",
        "percents": [50, 75, 90, 95, 99, 99.9]
      }
    },
    "error_count": {
      "filter": {
        "term": {"tag.error": true}
      }
    }
  }
}

-- Expected response:
{
  "aggregations": {
    "latency_percentiles": {
      "values": {
        "50.0": 85000,    # P50 = 85ms
        "75.0": 120000,   # P75 = 120ms
        "90.0": 180000,   # P90 = 180ms
        "95.0": 250000,   # P95 = 250ms
        "99.0": 450000,   # P99 = 450ms
        "99.9": 1200000   # P99.9 = 1.2s
      }
    },
    "error_count": {
      "doc_count": 25
    }
  }
}
```

---

## 🔒 SECTION 8 — TRACE-BASED ALERTING

### A. Latency SLA Violations

```yaml
# Prometheus alert rules for trace-derived metrics
groups:
  - name: trace_latency_alerts
    interval: 30s
    rules:
      
      # API Gateway P95 latency violation
      - alert: APIGatewayP95LatencyHigh
        expr: |
          histogram_quantile(0.95,
            sum(rate(traces_span_duration_milliseconds_bucket{
              service_name="api-gateway",
              span_kind="server"
            }[5m])) by (le, operation)
          ) > 500
        for: 5m
        labels:
          severity: warning
          component: api-gateway
        annotations:
          summary: "API Gateway P95 latency >500ms"
          description: "Operation {{ $labels.operation }} P95: {{ $value }}ms"
      
      # Database query latency spike
      - alert: DatabaseQueryLatencySpike
        expr: |
          histogram_quantile(0.95,
            sum(rate(traces_span_duration_milliseconds_bucket{
              span_kind="client",
              db_system=~"postgresql|mysql"
            }[5m])) by (le, service_name, db_operation)
          ) > 100
        for: 3m
        labels:
          severity: warning
          component: database
        annotations:
          summary: "Database query P95 >100ms"
          description: "{{ $labels.service_name }} {{ $labels.db_operation }}: {{ $value }}ms"
      
      # External API call timeout rate
      - alert: ExternalAPITimeoutRateHigh
        expr: |
          sum(rate(traces_span_status_code_count{
            span_kind="client",
            status_code="error",
            external_service!=""
          }[5m])) by (external_service)
          /
          sum(rate(traces_span_count{
            span_kind="client",
            external_service!=""
          }[5m])) by (external_service)
          > 0.05
        for: 5m
        labels:
          severity: critical
          component: external_integration
        annotations:
          summary: "{{ $labels.external_service }} timeout rate >5%"
```

### B. Error Correlation Alerts

```yaml
  - name: trace_error_correlation
    interval: 30s
    rules:
      
      # Cascading failure detection
      - alert: CascadingServiceFailure
        expr: |
          count(
            sum(rate(traces_span_status_code_count{status_code="error"}[5m])) by (service_name) > 10
          ) > 3
        for: 2m
        labels:
          severity: critical
          incident_type: cascading_failure
        annotations:
          summary: "Cascading failure detected across >3 services"
          description: "Multiple services experiencing elevated error rates simultaneously"
      
      # Database connection pool exhaustion pattern
      - alert: DatabaseConnectionPoolExhaustionPattern
        expr: |
          sum(rate(traces_span_status_code_count{
            db_system=~"postgresql|mysql",
            status_code="error",
            error_message=~".*connection pool.*"
          }[5m])) by (service_name) > 5
        for: 1m
        labels:
          severity: critical
          component: database
        annotations:
          summary: "{{ $labels.service_name }} experiencing DB connection pool issues"
```

---

## 🔒 SECTION 9 — BUSINESS TRANSACTION TRACING

### A. Multi-Tenant Trace Isolation

```python
# Middleware to enforce tenant isolation in traces
from opentelemetry import trace
from fastapi import Request, HTTPException

async def tenant_isolation_middleware(request: Request, call_next):
    current_span = trace.get_current_span()
    
    # Extract tenant from JWT or header
    tenant_id = extract_tenant_from_request(request)
    
    if not tenant_id:
        raise HTTPException(status_code=401, detail="Tenant not identified")
    
    # Add tenant context to span
    current_span.set_attribute("tenant.id", tenant_id)
    current_span.set_attribute("tenant.type", get_tenant_type(tenant_id))
    
    # Also add to trace state for propagation
    current_span.set_attribute("tracestate.tenant", tenant_id)
    
    response = await call_next(request)
    return response

# Query traces by tenant
def get_tenant_traces(tenant_id: str, start_time: datetime, end_time: datetime):
    """
    Retrieve all traces for a specific tenant within a time window.
    Useful for tenant-specific debugging and SLA verification.
    """
    query = {
        "query": {
            "bool": {
                "filter": [
                    {"term": {"tag.tenant.id": tenant_id}},
                    {"range": {
                        "startTimeMillis": {
                            "gte": int(start_time.timestamp() * 1000),
                            "lte": int(end_time.timestamp() * 1000)
                        }
                    }}
                ]
            }
        },
        "sort": [{"startTimeMillis": {"order": "desc"}}],
        "size": 1000
    }
    
    # Execute against Jaeger/OpenSearch backend
    return execute_trace_query(query)
```

### B. Payment Transaction Tracing

```python
# Complete payment flow tracing example
from opentelemetry import trace
from opentelemetry.trace import Status, StatusCode

tracer = trace.get_tracer(__name__)

async def process_payment_transaction(payment_request: PaymentRequest):
    # Root span for entire payment transaction
    with tracer.start_as_current_span(
        "payment_transaction",
        kind=trace.SpanKind.SERVER
    ) as payment_span:
        
        # Business context
        payment_span.set_attribute("transaction.type", "payment")
        payment_span.set_attribute("payment.amount", payment_request.amount)
        payment_span.set_attribute("payment.currency", payment_request.currency)
        payment_span.set_attribute("tenant.id", payment_request.tenant_id)
        payment_span.set_attribute("user.id", payment_request.user_id)
        payment_span.set_attribute("payment.method", payment_request.method)
        
        try:
            # Step 1: Validate payment request
            with tracer.start_as_current_span("validate_payment") as validate_span:
                validation_result = await validate_payment(payment_request)
                validate_span.set_attribute("validation.result", validation_result.status)
                
                if not validation_result.valid:
                    validate_span.set_attribute("validation.errors", 
                                               json.dumps(validation_result.errors))
                    raise PaymentValidationError(validation_result.errors)
            
            # Step 2: Check fraud detection
            with tracer.start_as_current_span("fraud_detection") as fraud_span:
                fraud_score = await check_fraud(payment_request)
                fraud_span.set_attribute("fraud.score", fraud_score)
                fraud_span.set_attribute("fraud.threshold", 0.8)
                
                if fraud_score > 0.8:
                    fraud_span.add_event("high_fraud_risk_detected")
                    raise FraudDetectionError(f"Fraud score: {fraud_score}")
            
            # Step 3: Reserve funds (database transaction)
            with tracer.start_as_current_span("reserve_funds") as reserve_span:
                reservation = await reserve_user_funds(
                    payment_request.user_id,
                    payment_request.amount
                )
                reserve_span.set_attribute("reservation.id", reservation.id)
                reserve_span.set_attribute("db.transaction.isolation_level", "serializable")
            
            # Step 4: Call payment gateway
            with tracer.start_as_current_span(
                "payment_gateway_charge",
                kind=trace.SpanKind.CLIENT
            ) as gateway_span:
                gateway_span.set_attribute("external.service", "stripe")
                gateway_span.set_attribute("external.operation", "create_charge")
                gateway_span.set_attribute("payment.gateway.id", payment_request.gateway_id)
                
                charge_result = await stripe.create_charge({
                    "amount": payment_request.amount,
                    "currency": payment_request.currency,
                    "source": payment_request.token,
                })
                
                gateway_span.set_attribute("payment.charge.id", charge_result.id)
                gateway_span.set_attribute("payment.charge.status", charge_result.status)
            
            # Step 5: Settle transaction
            with tracer.start_as_current_span("settle_transaction") as settle_span:
                settlement = await settle_payment(
                    reservation.id,
                    charge_result.id
                )
                settle_span.set_attribute("settlement.id", settlement.id)
                settle_span.set_attribute("settlement.status", settlement.status)
            
            # Step 6: Emit payment success event
            with tracer.start_as_current_span(
                "emit_payment_success_event",
                kind=trace.SpanKind.PRODUCER
            ) as event_span:
                event_span.set_attribute("messaging.system", "kafka")
                event_span.set_attribute("messaging.destination", "payment-events")
                
                await kafka_producer.send("payment-events", {
                    "event_type": "payment.success",
                    "payment_id": settlement.id,
                    "amount": payment_request.amount,
                    "tenant_id": payment_request.tenant_id,
                    "trace_id": format(payment_span.get_span_context().trace_id, '032x')
                })
            
            # Success
            payment_span.set_attribute("payment.status", "success")
            payment_span.set_status(Status(StatusCode.OK))
            
            return PaymentResponse(
                payment_id=settlement.id,
                status="success",
                amount=payment_request.amount
            )
            
        except PaymentValidationError as e:
            payment_span.set_attribute("payment.status", "validation_failed")
            payment_span.record_exception(e)
            payment_span.set_status(Status(StatusCode.ERROR, "Validation failed"))
            raise
            
        except FraudDetectionError as e:
            payment_span.set_attribute("payment.status", "fraud_detected")
            payment_span.record_exception(e)
            payment_span.set_status(Status(StatusCode.ERROR, "Fraud detected"))
            
            # Alert security team
            await alert_security_team(payment_request, fraud_score)
            raise
            
        except Exception as e:
            payment_span.set_attribute("payment.status", "failed")
            payment_span.record_exception(e)
            payment_span.set_status(Status(StatusCode.ERROR, str(e)))
            
            # Rollback reservation if exists
            if 'reservation' in locals():
                await rollback_reservation(reservation.id)
            
            raise
```

### C. Group Discussion Session Tracing

```python
# Voice GD session complete trace
async def orchestrate_voice_gd_session(session_request: GDSessionRequest):
    with tracer.start_as_current_span(
        "voice_gd_session",
        kind=trace.SpanKind.SERVER
    ) as session_span:
        
        # Business context
        session_span.set_attribute("session.type", "voice_gd")
        session_span.set_attribute("session.id", session_request.session_id)
        session_span.set_attribute("session.topic", session_request.topic)
        session_span.set_attribute("session.participant_count", len(session_request.participants))
        session_span.set_attribute("tenant.id", session_request.tenant_id)
        
        # Step 1: Create Jitsi room
        with tracer.start_as_current_span("create_jitsi_room") as jitsi_span:
            room = await jitsi_api.create_room({
                "room_name": session_request.session_id,
                "config": {
                    "startWithAudioMuted": True,
                    "startWithVideoMuted": True,
                    "disableModeratorIndicator": True
                }
            })
            jitsi_span.set_attribute("media.room_id", room.id)
            jitsi_span.set_attribute("media.system", "jitsi")
        
        # Step 2: Admit participants
        with tracer.start_as_current_span("admit_participants") as admit_span:
            for participant in session_request.participants:
                with tracer.start_as_current_span("admit_participant") as p_span:
                    p_span.set_attribute("participant.id", participant.id)
                    p_span.set_attribute("participant.order", participant.speaking_order)
                    
                    token = await generate_jitsi_token(participant, room.id)
                    p_span.set_attribute("participant.token_generated", True)
                    
                    await notify_participant(participant.id, token, room.id)
        
        # Step 3: Run turn-based speaking engine
        with tracer.start_as_current_span("speaking_rounds") as rounds_span:
            for round_num in range(session_request.num_rounds):
                with tracer.start_as_current_span(f"round_{round_num}") as round_span:
                    round_span.set_attribute("round.number", round_num)
                    round_span.set_attribute("round.duration_seconds", 
                                            session_request.round_duration)
                    
                    for participant in session_request.participants:
                        with tracer.start_as_current_span("participant_turn") as turn_span:
                            turn_span.set_attribute("participant.id", participant.id)
                            
                            # Force unmute
                            await jitsi_api.unmute_participant(room.id, participant.id)
                            turn_span.add_event("participant_unmuted")
                            
                            # Wait for turn duration
                            await asyncio.sleep(session_request.round_duration)
                            
                            # Force mute
                            await jitsi_api.mute_participant(room.id, participant.id)
                            turn_span.add_event("participant_muted")
                            
                            # Capture metrics
                            speaking_stats = await get_speaking_stats(room.id, participant.id)
                            turn_span.set_attribute("speaking.duration_seconds", 
                                                   speaking_stats.duration)
                            turn_span.set_attribute("speaking.used_full_time", 
                                                   speaking_stats.used_full_time)
        
        # Step 4: Close session and score
        with tracer.start_as_current_span("finalize_session") as finalize_span:
            await jitsi_api.close_room(room.id)
            
            # Calculate scores
            scores = await calculate_gd_scores(session_request.session_id)
            finalize_span.set_attribute("scoring.completed", True)
            finalize_span.set_attribute("scores.count", len(scores))
        
        session_span.set_attribute("session.status", "completed")
        session_span.set_status(Status(StatusCode.OK))
```

---

## 🔒 SECTION 10 — TRACE DATA RETENTION & COMPLIANCE

### A. Retention Policy

```yaml
# OpenSearch Index Lifecycle Management for traces
trace_retention_policy:
  
  # Hot tier (recent traces, fast queries)
  hot:
    duration: 7 days
    shard_count: 5
    replica_count: 1
    refresh_interval: 5s
  
  # Warm tier (older traces, slower queries acceptable)
  warm:
    duration: 23 days
    shard_count: 2
    replica_count: 1
    refresh_interval: 30s
    force_merge: true
  
  # Cold tier (archived traces, rare access)
  cold:
    duration: 335 days  # Total 1 year
    shard_count: 1
    replica_count: 0
    searchable_snapshot: true
  
  # Delete after 1 year
  delete:
    duration: 365 days
```

### B. PII Scrubbing

```python
# Automatic PII scrubbing from trace attributes
from opentelemetry.sdk.trace.export import SpanProcessor
import re

class PIIScrubberSpanProcessor(SpanProcessor):
    """
    Scrubs PII from span attributes before export.
    Prevents sensitive data from being stored in trace backend.
    """
    
    PII_PATTERNS = {
        'email': re.compile(r'\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b'),
        'phone': re.compile(r'\b\d{3}[-.]?\d{3}[-.]?\d{4}\b'),
        'ssn': re.compile(r'\b\d{3}-\d{2}-\d{4}\b'),
        'credit_card': re.compile(r'\b\d{4}[\s-]?\d{4}[\s-]?\d{4}[\s-]?\d{4}\b'),
    }
    
    SENSITIVE_ATTRIBUTE_KEYS = {
        'password', 'secret', 'api_key', 'token', 'authorization',
        'credit_card', 'ssn', 'social_security'
    }
    
    def on_start(self, span, parent_context):
        pass
    
    def on_end(self, span):
        """Scrub PII from span attributes before export."""
        if not span.attributes:
            return
        
        scrubbed_attributes = {}
        
        for key, value in span.attributes.items():
            # Remove sensitive keys entirely
            if any(sensitive in key.lower() for sensitive in self.SENSITIVE_ATTRIBUTE_KEYS):
                scrubbed_attributes[key] = "[REDACTED]"
                continue
            
            # Scrub PII patterns from string values
            if isinstance(value, str):
                scrubbed_value = value
                for pii_type, pattern in self.PII_PATTERNS.items():
                    scrubbed_value = pattern.sub(f"[{pii_type.upper()}_REDACTED]", scrubbed_value)
                scrubbed_attributes[key] = scrubbed_value
            else:
                scrubbed_attributes[key] = value
        
        # Replace span attributes with scrubbed version
        span._attributes = scrubbed_attributes
    
    def shutdown(self):
        pass
    
    def force_flush(self, timeout_millis=30000):
        return True

# Add to tracer provider
tracer_provider.add_span_processor(PIIScrubberSpanProcessor())
```

### C. GDPR Compliance - Right to Deletion

```python
# Delete all traces for a specific user (GDPR right to erasure)
async def delete_user_traces(user_id: str):
    """
    Deletes all trace data associated with a user.
    Required for GDPR compliance (right to erasure).
    """
    
    # Query OpenSearch for all traces with user_id
    delete_query = {
        "query": {
            "term": {
                "tag.user.id": user_id
            }
        }
    }
    
    # Delete from Jaeger backend (OpenSearch)
    await opensearch_client.delete_by_query(
        index="jaeger-span-*",
        body=delete_query,
        refresh=True
    )
    
    # Also delete from any analytics databases
    await clickhouse_client.execute(
        "DELETE FROM trace_analytics WHERE user_id = %(user_id)s",
        {'user_id': user_id}
    )
    
    # Audit log the deletion
    await audit_log.record({
        "action": "user_traces_deleted",
        "user_id": user_id,
        "deleted_at": datetime.utcnow(),
        "compliance_reason": "GDPR_right_to_erasure"
    })
    
    return {"status": "deleted", "user_id": user_id}
```

---

## 🔒 SECTION 11 — TRACE-DRIVEN DEVELOPMENT & TESTING

### A. Trace Replay for Testing

```python
# Replay production traces in test environment
from typing import Dict, List
import httpx

async def replay_trace(trace_id: str, target_environment: str = "staging"):
    """
    Replays a production trace in a test environment.
    Useful for reproducing bugs and testing fixes.
    """
    
    # Fetch original trace from Jaeger
    trace = await jaeger_client.get_trace(trace_id)
    
    # Extract root span (entry point)
    root_span = find_root_span(trace['spans'])
    
    # Reconstruct HTTP request from span attributes
    request = {
        'method': root_span.get('http.method'),
        'url': root_span.get('http.url'),
        'headers': extract_headers_from_span(root_span),
        'body': root_span.get('http.request.body'),
    }
    
    # Replace production URLs with staging URLs
    request['url'] = request['url'].replace(
        'api.ecoskiller.com',
        'api-staging.ecoskiller.com'
    )
    
    # Add trace replay header
    request['headers']['X-Trace-Replay'] = 'true'
    request['headers']['X-Original-Trace-ID'] = trace_id
    
    # Execute request in target environment
    async with httpx.AsyncClient() as client:
        response = await client.request(
            method=request['method'],
            url=request['url'],
            headers=request['headers'],
            json=request.get('body')
        )
    
    # Extract new trace ID from response
    new_trace_id = response.headers.get('X-Trace-ID')
    
    # Compare original vs replay trace
    comparison = await compare_traces(trace_id, new_trace_id)
    
    return {
        'original_trace_id': trace_id,
        'replay_trace_id': new_trace_id,
        'comparison': comparison
    }

def compare_traces(original_trace_id: str, replay_trace_id: str) -> Dict:
    """
    Compares two traces to identify differences.
    Useful for validating bug fixes.
    """
    original = jaeger_client.get_trace(original_trace_id)
    replay = jaeger_client.get_trace(replay_trace_id)
    
    return {
        'duration_diff_ms': abs(
            original['duration'] - replay['duration']
        ) / 1000,
        'span_count_diff': abs(
            len(original['spans']) - len(replay['spans'])
        ),
        'error_in_original': has_error_span(original),
        'error_in_replay': has_error_span(replay),
        'performance_improvement': (
            original['duration'] > replay['duration']
        )
    }
```

### B. Synthetic Transaction Monitoring

```python
# Continuous synthetic transactions with tracing
import asyncio
from opentelemetry import trace

tracer = trace.get_tracer("synthetic-monitor")

async def synthetic_job_creation_flow():
    """
    Synthetic transaction that exercises critical path.
    Runs every 5 minutes to detect regressions.
    """
    with tracer.start_as_current_span(
        "synthetic_job_creation",
        attributes={
            "synthetic": True,
            "test_type": "critical_path_monitoring"
        }
    ) as span:
        
        try:
            # Step 1: Authenticate
            auth_response = await httpx.post(
                "https://api.ecoskiller.com/api/v1/auth/login",
                json={
                    "email": "synthetic-test@ecoskiller.com",
                    "password": os.getenv("SYNTHETIC_PASSWORD")
                }
            )
            span.set_attribute("auth.status", auth_response.status_code)
            
            token = auth_response.json()['access_token']
            
            # Step 2: Create job
            job_response = await httpx.post(
                "https://api.ecoskiller.com/api/v1/jobs",
                headers={"Authorization": f"Bearer {token}"},
                json={
                    "title": "Synthetic Test Job",
                    "description": "Automated monitoring job",
                    "location": "Remote",
                }
            )
            span.set_attribute("job.creation.status", job_response.status_code)
            
            job_id = job_response.json()['id']
            
            # Step 3: Fetch job
            get_response = await httpx.get(
                f"https://api.ecoskiller.com/api/v1/jobs/{job_id}",
                headers={"Authorization": f"Bearer {token}"}
            )
            span.set_attribute("job.fetch.status", get_response.status_code)
            
            # Step 4: Delete job (cleanup)
            delete_response = await httpx.delete(
                f"https://api.ecoskiller.com/api/v1/jobs/{job_id}",
                headers={"Authorization": f"Bearer {token}"}
            )
            span.set_attribute("job.delete.status", delete_response.status_code)
            
            # Mark success
            span.set_status(Status(StatusCode.OK))
            span.set_attribute("synthetic.result", "success")
            
        except Exception as e:
            span.record_exception(e)
            span.set_status(Status(StatusCode.ERROR, str(e)))
            span.set_attribute("synthetic.result", "failure")
            
            # Alert on synthetic failure
            await alert_ops_team(
                "Synthetic transaction failed",
                trace_id=format(span.get_span_context().trace_id, '032x')
            )
            
            raise

# Run synthetic every 5 minutes
async def run_synthetic_monitoring():
    while True:
        await synthetic_job_creation_flow()
        await asyncio.sleep(300)  # 5 minutes
```

---

## 🔒 SECTION 12 — ENVIRONMENT-SPECIFIC TRACING CONFIGURATION

### A. Development Environment
```yaml
environment: dev
tracing_config:
  sampling_rate: 1.0  # 100% sampling
  export_batch_size: 128
  export_timeout_ms: 30000
  max_queue_size: 512
  
  enabled_instrumentations:
    - http
    - database
    - cache
    - messaging
  
  sensitive_data_scrubbing: false  # Allow PII in dev
  
  verbose_logging: true
  console_exporter: true  # Also log to console
```

### B. Test Environment
```yaml
environment: test
tracing_config:
  sampling_rate: 1.0  # 100% sampling for CI/CD
  export_batch_size: 256
  export_timeout_ms: 10000
  
  enabled_instrumentations:
    - http
    - database
    - cache
    - messaging
  
  sensitive_data_scrubbing: true
  
  trace_assertions_enabled: true  # Fail tests on span errors
```

### C. Staging Environment
```yaml
environment: staging
tracing_config:
  sampling_rate: 1.0  # 100% - production parity
  export_batch_size: 512
  export_timeout_ms: 5000
  
  enabled_instrumentations:
    - http
    - database
    - cache
    - messaging
    - external_apis
  
  sensitive_data_scrubbing: true
  pii_scrubber: enabled
  
  retention_days: 30
```

### D. Production Environment
```yaml
environment: production
tracing_config:
  # Tail-based sampling via OTel Collector
  sampling_strategy: tail_based
  
  # Always sample:
  always_sample_rules:
    - errors: true
    - slow_traces_ms: 2000
    - critical_paths:
      - "/api/v1/auth/*"
      - "/api/v1/payments/*"
    - high_value_tenants: true
  
  # Probabilistic sampling for normal traffic
  probabilistic_sampling_rate: 0.1  # 10%
  
  export_batch_size: 2048
  export_timeout_ms: 3000
  max_queue_size: 10000
  
  enabled_instrumentations:
    - http
    - database
    - cache
    - messaging
    - external_apis
    - background_jobs
  
  sensitive_data_scrubbing: true
  pii_scrubber: enabled
  gdpr_compliance: enabled
  
  retention_days: 365
  retention_policy: hot_warm_cold
```

---

## 🔒 SECTION 13 — TRACE ANALYTICS & INSIGHTS DASHBOARD

### A. Grafana Tracing Dashboard

```json
{
  "dashboard": {
    "title": "ECOSKILLER Distributed Tracing Analytics",
    "uid": "trace-analytics",
    "panels": [
      {
        "title": "Request Rate by Service",
        "type": "timeseries",
        "targets": [
          {
            "expr": "sum(rate(traces_span_count{span_kind=\"server\"}[5m])) by (service_name)",
            "legendFormat": "{{service_name}}"
          }
        ]
      },
      {
        "title": "P95 Latency by Service",
        "type": "timeseries",
        "targets": [
          {
            "expr": "histogram_quantile(0.95, sum(rate(traces_span_duration_milliseconds_bucket{span_kind=\"server\"}[5m])) by (le, service_name))",
            "legendFormat": "{{service_name}}"
          }
        ]
      },
      {
        "title": "Error Rate by Service",
        "type": "timeseries",
        "targets": [
          {
            "expr": "sum(rate(traces_span_status_code_count{status_code=\"error\"}[5m])) by (service_name) / sum(rate(traces_span_count[5m])) by (service_name) * 100",
            "legendFormat": "{{service_name}}"
          }
        ]
      },
      {
        "title": "Database Query Performance",
        "type": "table",
        "targets": [
          {
            "expr": "topk(10, histogram_quantile(0.95, sum(rate(traces_span_duration_milliseconds_bucket{span_kind=\"client\",db_system!=\"\"}[1h])) by (le, service_name, db_operation)))"
          }
        ],
        "transformations": [
          {
            "id": "organize",
            "options": {
              "columns": ["service_name", "db_operation", "P95_latency_ms"]
            }
          }
        ]
      },
      {
        "title": "External API Call Success Rate",
        "type": "stat",
        "targets": [
          {
            "expr": "sum(rate(traces_span_count{span_kind=\"client\",external_service!=\"\"}[5m])) - sum(rate(traces_span_status_code_count{span_kind=\"client\",external_service!=\"\",status_code=\"error\"}[5m]))",
            "legendFormat": "Success Rate"
          }
        ],
        "format": "percent"
      },
      {
        "title": "Top 10 Slowest Traces (Last Hour)",
        "type": "table",
        "targets": [
          {
            "datasource": "jaeger",
            "queryType": "search",
            "service": "*",
            "operation": "*",
            "limit": 10,
            "minDuration": "2s",
            "timeRange": "1h"
          }
        ]
      },
      {
        "title": "Service Dependency Graph",
        "type": "nodeGraph",
        "targets": [
          {
            "datasource": "jaeger",
            "queryType": "dependencies",
            "timeRange": "24h"
          }
        ]
      },
      {
        "title": "Tenant Request Distribution",
        "type": "piechart",
        "targets": [
          {
            "expr": "sum(increase(traces_span_count{tenant_id!=\"\"}[1h])) by (tenant_id)"
          }
        ]
      }
    ]
  }
}
```

---

## 🔒 SECTION 14 — INCIDENT FORENSICS USING TRACES

### A. Root Cause Analysis Workflow

```python
# Automated root cause analysis from error traces
async def analyze_incident(trace_id: str) -> Dict:
    """
    Performs automated root cause analysis on a failed trace.
    """
    # Fetch full trace
    trace = await jaeger_client.get_trace(trace_id)
    
    # Find error spans
    error_spans = [
        span for span in trace['spans']
        if span.get('tags', {}).get('error') == True
    ]
    
    if not error_spans:
        return {"status": "no_errors_found"}
    
    # Get first error (root cause likely)
    first_error = min(error_spans, key=lambda s: s['startTime'])
    
    # Analyze error context
    analysis = {
        "trace_id": trace_id,
        "first_error": {
            "service": first_error['process']['serviceName'],
            "operation": first_error['operationName'],
            "timestamp": first_error['startTime'],
            "duration_ms": first_error['duration'] / 1000,
            "error_message": first_error.get('logs', [{}])[0].get('fields', {}).get('message'),
        },
        "affected_services": list(set(
            span['process']['serviceName']
            for span in error_spans
        )),
        "error_propagation_path": build_error_propagation_path(trace, first_error),
        "related_incidents": await find_similar_errors(
            first_error['process']['serviceName'],
            first_error['operationName'],
            time_window_hours=24
        )
    }
    
    # Check for common failure patterns
    analysis["failure_pattern"] = detect_failure_pattern(trace, first_error)
    
    # Suggested remediation
    analysis["suggested_remediation"] = suggest_remediation(
        analysis["failure_pattern"]
    )
    
    return analysis

def detect_failure_pattern(trace: Dict, error_span: Dict) -> str:
    """Identifies common failure patterns."""
    
    error_msg = str(error_span.get('logs', [{}])[0].get('fields', {}))
    
    if 'connection pool' in error_msg.lower():
        return "database_connection_pool_exhaustion"
    elif 'timeout' in error_msg.lower():
        return "external_api_timeout"
    elif '401' in error_msg or '403' in error_msg:
        return "authentication_authorization_failure"
    elif 'deadlock' in error_msg.lower():
        return "database_deadlock"
    elif 'out of memory' in error_msg.lower():
        return "memory_exhaustion"
    elif 'kafka' in error_msg.lower() and 'broker' in error_msg.lower():
        return "message_broker_unavailable"
    else:
        return "unknown_failure_pattern"

def suggest_remediation(failure_pattern: str) -> str:
    """Suggests remediation based on detected pattern."""
    
    remediation_map = {
        "database_connection_pool_exhaustion": 
            "Increase connection pool size or investigate long-running queries",
        "external_api_timeout":
            "Check external service status, increase timeout, or implement circuit breaker",
        "authentication_authorization_failure":
            "Verify JWT token validity and user permissions",
        "database_deadlock":
            "Review transaction isolation levels and query ordering",
        "memory_exhaustion":
            "Scale up service resources or investigate memory leak",
        "message_broker_unavailable":
            "Verify Kafka broker health and network connectivity",
        "unknown_failure_pattern":
            "Review error logs and trace details for manual investigation"
    }
    
    return remediation_map.get(failure_pattern, "Unknown pattern")
```

---

## 🔒 SECTION 15 — PRODUCTION READINESS CERTIFICATION

### A. Tracing Readiness Checklist

```yaml
tracing_production_readiness:
  
  instrumentation:
    - ✅ All HTTP services instrumented with OpenTelemetry
    - ✅ Database queries automatically traced
    - ✅ Cache operations traced
    - ✅ Message queue operations traced
    - ✅ External API calls traced
    - ✅ Background jobs linked to parent traces
    - ✅ WebSocket connections traced
    - ✅ Real-time media sessions traced
  
  context_propagation:
    - ✅ W3C Trace Context headers propagated
    - ✅ Service-to-service trace continuity verified
    - ✅ Async event trace linking functional
    - ✅ Background job trace linking functional
    - ✅ Mobile app → backend trace propagation working
  
  collector:
    - ✅ OpenTelemetry Collector deployed
    - ✅ High availability (3+ replicas)
    - ✅ Batch processing configured
    - ✅ Tail-based sampling configured
    - ✅ PII scrubbing enabled
    - ✅ Multiple exporters configured
  
  storage:
    - ✅ Jaeger deployed with OpenSearch backend
    - ✅ Retention policy configured (1 year)
    - ✅ Index lifecycle management active
    - ✅ Hot/warm/cold tier storage configured
  
  analysis:
    - ✅ Grafana dashboards created
    - ✅ Service dependency map visible
    - ✅ Latency percentile charts configured
    - ✅ Error rate monitoring active
    - ✅ Critical path analysis functional
  
  alerting:
    - ✅ Latency SLA violation alerts configured
    - ✅ Error correlation alerts configured
    - ✅ Cascading failure detection active
    - ✅ External API timeout alerts configured
  
  compliance:
    - ✅ PII scrubbing processor deployed
    - ✅ GDPR deletion workflow implemented
    - ✅ Audit logging of trace access enabled
    - ✅ Tenant isolation enforced in traces
  
  testing:
    - ✅ Synthetic transaction monitoring active
    - ✅ Trace replay functionality tested
    - ✅ CI/CD trace assertion tests passing
  
  documentation:
    - ✅ Instrumentation guide created
    - ✅ Trace analysis runbook created
    - ✅ Incident forensics guide created
    - ✅ Team training completed
```

### B. Final Enforcement

```
PHONE_END_TO_END_TRACE_AGENT may only be declared 
PRODUCTION-READY when ALL conditions are met:

✅ All microservices instrumented with OpenTelemetry SDK
✅ Trace context propagation working across all boundaries
✅ OpenTelemetry Collector deployed in HA mode
✅ Jaeger backend operational with persistent storage
✅ Tail-based sampling configured (100% errors, 10% normal)
✅ PII scrubbing processor active
✅ GDPR compliance mechanisms deployed
✅ Service dependency map auto-generating
✅ Critical path analysis functional
✅ Grafana tracing dashboards created
✅ Trace-based alerting rules active
✅ Synthetic transaction monitoring running
✅ Trace replay capability tested
✅ Incident forensics workflow documented
✅ Team trained on trace analysis
✅ 30-day trace retention verified
✅ Multi-environment tracing configurations deployed

Absence of ANY requirement above:
→ STOP EXECUTION  
→ REPORT INCOMPLETE TRACING SYSTEM  
→ NO PRODUCTION DEPLOYMENT PERMITTED
```

---

## 🔒 SECTION 16 — SEALED & LOCKED

**Artifact Status:** PRODUCTION-READY · DETERMINISTIC · GOVERNANCE-ENFORCED  
**Version:** 1.0  
**Last Updated:** 2026-03-04  
**Modification Policy:** Add-only via version bump  
**Interpretation Authority:** NONE  
**Execution Authority:** Automated distributed tracing with forensic capability  

This document defines the complete, deterministic, and enforceable end-to-end distributed tracing system for the ECOSKILLER multi-tenant SaaS platform. No deployment may proceed to production without full tracing agent operational status.

**END OF SPECIFICATION**

---

## 📋 APPENDIX A — INTERN QUICK START GUIDE

### Step 1: Install OpenTelemetry SDK (Python Service)
```bash
# In your Python service directory
pip install opentelemetry-api opentelemetry-sdk \
            opentelemetry-instrumentation-fastapi \
            opentelemetry-exporter-otlp

# Add to requirements.txt
```

### Step 2: Add Tracing to main.py
```python
# Copy this to the top of your main.py
from opentelemetry import trace
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor

# Initialize tracing
trace.set_tracer_provider(TracerProvider())
otlp_exporter = OTLPSpanExporter(endpoint="http://otel-collector.monitoring.svc:4317")
trace.get_tracer_provider().add_span_processor(BatchSpanProcessor(otlp_exporter))

# Your FastAPI app
app = FastAPI()

# Instrument FastAPI
FastAPIInstrumentor.instrument_app(app)
```

### Step 3: Deploy OTel Collector
```bash
kubectl apply -f infra/monitoring/otel-collector/
kubectl get pods -n monitoring -l app=otel-collector
```

### Step 4: Deploy Jaeger
```bash
kubectl apply -f infra/monitoring/jaeger/
kubectl port-forward -n monitoring svc/jaeger-query 16686:16686
# Open http://localhost:16686 in browser
```

### Step 5: Verify Tracing Works
```bash
# Make a request to your service
curl http://localhost:8000/api/v1/jobs

# Check Jaeger UI - you should see the trace appear
```

**Expected Result:** Complete request trace visible in Jaeger UI showing all service hops, database queries, and cache operations.

---

**END OF DOCUMENT**
