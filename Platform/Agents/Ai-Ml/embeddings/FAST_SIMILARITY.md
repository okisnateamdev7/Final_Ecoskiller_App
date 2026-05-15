# 🔒 FAST_SIMILARITY.md — ANTIGRAVITY SYSTEM SPEC
**Status:** SEALED · LOCKED · GOVERNED · DETERMINISTIC  
**Version:** 1.0.0  
**Mutation Policy:** Add-only via version bump  
**Execution Authority:** Human declaration only  
**Last Updated:** 2025-02-25  

---

## 🏢 EXECUTIVE CONTEXT

**Platform:** Ecoskiller Antigravity  
**Scale Target:** 10M–100M users  
**Architecture:** Microservices + Real-time Vector Search  
**Search Engine:** FAISS (Facebook AI Similarity Search)  
**Vector Dimension:** 384-dimensional (immutable)  
**Query Latency SLA:** < 50ms (p95)  
**Throughput:** 1,000+ similarity queries/second  
**Compliance:** Zero-trust, tenant-isolated, audit-logged  

---

## SECTION A — FAST SIMILARITY SYSTEM IDENTITY

### 1.1 AGENT SPECIFICATION (MANDATORY)

```
AGENT_NAME = FAST_SIMILARITY_ENGINE
SYSTEM_ROLE = Ultra-fast approximate nearest neighbor search for idea vectors
PRIMARY_DOMAIN = Vector Similarity Search & Discovery
EXECUTION_MODE = Deterministic + Real-time validated
DATA_SCOPE = FAISS indexes, similarity search results, query logs
TENANT_SCOPE = Strict isolation (multi-tenant, federated indexes)
FAILURE_POLICY = Halt on ambiguity, LOG, ESCALATE
ENVIRONMENT = Dev / Test / Staging / Production
DEPLOYMENT_MODEL = Stateless microservice + Distributed Vector Index
```

### 1.2 PURPOSE DECLARATION

**Problem Solved:**
- Enable sub-50ms similarity searches across 100M+ idea vectors
- Detect plagiarism & originality in real-time
- Power discovery engine with instant recommendations
- Support interactive idea exploration (no latency barriers)
- Scale to billions of similarity queries daily
- Maintain deterministic, reproducible search results

**Input Consumed:**
- Query vector (384-dimensional idea DNA vector)
- Search parameters (K neighbors, distance threshold, tenant_id)
- Tenant context (multi-tenant scoping)
- User preferences (optional ranking adjustments)

**Output Produced:**
- Top-K similar ideas (ranked by distance)
- Similarity scores (0-1.0, immutable)
- Execution metadata (latency, index version, query hash)
- Audit reference (immutable trace)

**Downstream Agents:**
- RANKING_ENGINE (uses similarity for ranking)
- DISCOVERY_ENGINE (powers recommendations)
- ORIGINALITY_DETECTOR (plagiarism checking)
- NOTIFICATION_AGENT (alerts about matches)
- ANALYTICS_AGENT (query analytics)

**Upstream Agents:**
- IDEA_DNA_GENERATOR (provides query vectors)
- IDENTITY_AGENT (user authentication)
- TENANT_ISOLATION_AGENT (multi-tenant scoping)
- CACHE_LAYER (Redis, pre-computed results)

---

## SECTION B — FAISS INDEX FUNDAMENTALS

### 2.1 WHAT IS FAISS?

**Definition:**
FAISS (Facebook AI Similarity Search) is a library for efficient similarity search and clustering of dense vectors. It provides fast approximate nearest neighbor search at scale.

```yaml
Key_Properties:

  Approximate_Search:
    - Not exact nearest neighbors (trade-off: speed vs accuracy)
    - 95% accuracy vs brute force on typical workloads
    - Orders of magnitude faster (10ms vs 1000ms for 1M vectors)
  
  Scalability:
    - Supports billions of vectors
    - Runs on CPUs (no GPU required for Antigravity)
    - Memory efficient (compression available)
    - Distributed index capability
  
  Determinism:
    - Same vector → same neighbors (deterministic)
    - Seeding: Fixed random seed for reproducibility
    - No non-deterministic operations
  
  Open_Source:
    - MIT License (permissive)
    - Production-proven (Meta uses at scale)
    - Active maintenance
    - C++ core with Python bindings

Performance_Characteristics:
  
  Index_Build_Time: O(n * log n) where n = vector count
  Query_Time: O(log n) to O(sqrt n) depending on index type
  Memory_Usage: ~10 bytes per vector (with compression)
  
  For 1M vectors (384-dim):
    Build_Time: ~30 minutes
    Query_Time: ~10ms (p50), ~50ms (p95)
    Memory: ~4GB (with IVF compression)
```

### 2.2 FAISS INDEX TYPES & SELECTION

```yaml
Index_Type_Comparison:

  1_IVFFlat (Selected for Antigravity):
    Purpose: Fast search with minimal overhead
    Algorithm:
      - Inverted File (divide vector space into cells)
      - Flat quantizer (exact vectors stored)
    Params:
      - nlist: 100 (Voronoi cells for 1M vectors)
      - nprobe: 10 (cells to search per query)
    Performance:
      - Query_Time: ~10-50ms (1M vectors)
      - Accuracy: ~95% vs brute force
      - Memory: ~384MB (1M vectors)
    Use_Case: Balanced speed/accuracy (our choice)
  
  2_IVFPQFlat:
    Purpose: Compressed index (save memory)
    Algorithm: IVF + Product Quantization
    Params:
      - nlist: 100
      - m: 16 (compression factor)
    Performance:
      - Query_Time: ~5-20ms
      - Accuracy: ~85% vs brute force
      - Memory: ~50MB (1M vectors)
    Use_Case: Memory-constrained environments
    Note: Not selected (accuracy trade-off too high)
  
  3_HNSW:
    Purpose: Hierarchical navigable small world
    Algorithm: Graph-based nearest neighbor
    Params:
      - M: 5-48 (connectivity)
      - ef_construction: 200 (build-time effort)
    Performance:
      - Query_Time: ~1-10ms
      - Accuracy: ~98% vs brute force
      - Memory: ~2GB (1M vectors)
    Use_Case: GPU-less ultra-fast search
    Note: Considered, not selected (memory overhead)
  
  4_BruteForce (Baseline):
    Purpose: Exact neighbors (no approximation)
    Algorithm: Linear scan all vectors
    Params: None
    Performance:
      - Query_Time: ~1000ms (1M vectors)
      - Accuracy: 100% (exact)
      - Memory: ~1.5GB
    Use_Case: Validation, testing
    Note: Too slow for production (SLA = 50ms p95)

Why_IVFFlat_Selected:
  ✅ 95% accuracy acceptable for recommendations
  ✅ Sub-50ms latency (SLA compliant)
  ✅ Low memory footprint (~400MB per 1M vectors)
  ✅ Proven at scale (Meta/Facebook)
  ✅ Deterministic (fixed seed)
  ✅ CPU-only (no GPU dependency)
  ✅ Easy to rebuild (weekly, off-peak)
```

---

## SECTION C — FAST SIMILARITY QUERY ENGINE

### 3.1 QUERY PIPELINE

```yaml
Query_Processing_Flow:

  Step_1_Receive_Query:
    Input: Query vector (384 dims)
    Validation:
      - Vector dimension check (must be 384)
      - Tenant_id validation
      - Rate limit check
      - User authorization (RBAC)
    Output: Validated query + metadata
    Time: ~5ms
  
  Step_2_Check_Redis_Cache:
    Purpose: Avoid index queries for common searches
    Key: cache:similarity:hash(vector):tenant_id
    TTL: 24 hours (pre-computed recommendations)
    Hit_Rate: ~60% (from recommendation caching)
    Output: Cached results (if hit) OR proceed to Step 3
    Time: ~1ms (cache hit), ~0ms (miss overhead)
  
  Step_3_Query_FAISS_Index:
    Algorithm: IVFFlat approximate nearest neighbors
    Operation:
      1. Normalize query vector (L2 norm)
      2. Find nearest Voronoi cell (coarse search)
      3. Search nprobe=10 cells (fine search)
      4. Retrieve top-K vectors
      5. Calculate exact distances
    Parameters:
      - K: Typically 10 (top-10 recommendations)
      - Distance_Metric: L2 (Euclidean)
      - nprobe: 10 cells (balance speed vs accuracy)
      - Threshold: Optional (only return if distance < threshold)
    Output: Top-K similar vectors with distances
    Time: ~30-50ms (p95)
  
  Step_4_Convert_Distances_To_Similarities:
    Formula: similarity = 1.0 - (distance / max_distance)
    Range: 0.0 (dissimilar) to 1.0 (identical)
    Immutability: Score is fixed (never changes)
  
  Step_5_Rank_Results:
    Apply: Multi-factor ranking (similarity + metadata)
    Factors:
      - Similarity_score: 0.60
      - Recency_boost: 0.20 (recent ideas weighted)
      - Creator_reputation: 0.15
      - Engagement_signals: 0.05
    Output: Ranked list of top-K ideas
    Time: ~5ms
  
  Step_6_Fetch_Metadata:
    Query: PostgreSQL for idea titles, creators, dates
    Method: Batch query (single round-trip)
    Cache: Redis (idea metadata cache)
    Output: Enriched results with full metadata
    Time: ~10-20ms
  
  Step_7_Assemble_Response:
    Format:
      {
        "similarities": [
          {
            "idea_id": "UUID",
            "similarity_score": float,
            "distance": float,
            "rank": int,
            "title": "string",
            "creator_id": "UUID",
            "created_at": "ISO8601"
          }
        ],
        "query_metadata": {
          "query_time_ms": int,
          "index_version": "string",
          "tenant_id": "UUID",
          "cached": boolean,
          "timestamp": "ISO8601"
        }
      }
    Time: ~2ms
  
  Step_8_Log_Query:
    Action: Log to audit trail (async, non-blocking)
    Data:
      - Query hash (SHA-256 of vector)
      - Result IDs
      - User_id
      - Tenant_id
      - Latency
      - Cache hit/miss
    Storage: Append-only PostgreSQL
    Time: ~0ms (async fire-and-forget)
  
  Total_Latency_Budget:
    Sum: 5 + 1 + 40 + 5 + 5 + 15 + 2 + 0 = ~73ms
    P50: ~40ms (with cache)
    P95: ~50ms (SLA compliant)
    P99: ~100ms (acceptable tail)

Cache_Strategy:

  Query_Result_Cache (Redis):
    Purpose: Pre-computed recommendations
    Key: cache:similarity:{hash(user_vector)}:{tenant_id}
    Value: Serialized top-20 recommendations
    TTL: 6 hours (refresh if user activity detected)
    Refresh: On demand (user interacts with recommendations)
    Hit_Rate: ~60% (user preferences stable)
  
  Metadata_Cache (Redis):
    Purpose: Fast metadata lookup
    Key: meta:idea:{idea_id}
    Value: {title, creator, created_at, category, domain}
    TTL: 24 hours
    Hit_Rate: ~90% (metadata stable)
  
  Vector_Cache (Redis):
    Purpose: Quick vector re-access
    Key: dna:idea:{idea_id}
    Value: 384-dim vector (binary serialized)
    TTL: 24 hours
    Size: ~1.5MB per 1K vectors
    Use: Avoid repeated PostgreSQL queries
```

### 3.2 MULTI-TENANT ISOLATION IN SEARCH

```yaml
Tenant_Isolation_Strategy:

  Index_Partitioning:
    Approach: Logical partitioning (one index, logical scoping)
    Method:
      1. Store tenant_id with each vector
      2. Pre-filter queries by tenant_id
      3. Search only within tenant's vectors
    Implementation:
      - FAISS doesn't support filtering natively
      - Solution: Maintain separate indexes per tenant
      - Large_Tenant: Dedicated index (500K+ vectors)
      - Small_Tenant: Shared index with prefix scoping
    Scalability:
      - Tenants with 500K+ vectors: Dedicated IVFFlat index
      - Tenants with <500K vectors: Shared index (100 total max)
      - Dynamic repartitioning: Quarterly review
  
  Query_Scoping:
    Step_1: Validate tenant_id from JWT token
    Step_2: Load tenant's FAISS index (or shared index subset)
    Step_3: Query only that index
    Step_4: Filter results by tenant_id (belt-and-suspenders)
    Guarantee: Zero cross-tenant results
  
  Index_Building:
    Tenant_Exclusive:
      - Each tenant's index rebuilt separately
      - No tenant vectors mixed during build
      - No shared state between indexes
      - Atomic swap (no downtime)
    Shared_Index:
      - Multiple tenants in single index
      - Vector IDs prefixed with tenant_id
      - Filtering done post-search
      - Tenant A can never see Tenant B's vectors
  
  Access_Control:
    Authentication:
      - JWT token contains tenant_id
      - Token validation required
      - Suspended tenants: Index access revoked
    Authorization:
      - Only own tenant's vectors searchable
      - Cross-tenant queries: BLOCKED
      - Read-only access: Always
    Audit:
      - All queries logged with tenant_id
      - Compliance reports per tenant
      - Query monitoring per tenant
```

---

## SECTION D — INDEX LIFECYCLE & MAINTENANCE

### 4.1 INDEX BUILD & REFRESH SCHEDULE

```yaml
Build_Schedule:

  Weekly_Full_Rebuild:
    Trigger: Sunday 2 AM UTC (off-peak)
    Frequency: Every 7 days
    Duration: ~30 minutes (1M vectors)
    Process:
      1. Fetch all idea DNA vectors from PostgreSQL
      2. Filter by tenant_id (per-tenant indexes)
      3. Normalize vectors (L2 norm)
      4. Train IVFFlat index (nlist=100)
      5. Add all vectors to index
      6. Save index to disk (immutable copy)
      7. Backup to S3 (daily snapshots)
      8. Atomic swap (switch active index)
      9. Notify downstream (index_refreshed event)
    Coordination:
      - Lock held during rebuild (queries redirected to cache)
      - Read-only fallback (use previous index if issues)
      - Rollback: Within 30 seconds (revert to previous)
  
  Incremental_Addition:
    Schedule: Every 1 hour
    Trigger: New ideas created since last build
    Process:
      1. Fetch ideas created in last hour
      2. Generate DNA vectors (if not done)
      3. Add vectors to in-memory index
      4. Persist to disk
      5. Emit index_updated event
    Guarantees:
      - New ideas searchable within 1 hour
      - No accuracy loss
      - Atomic operations (no partial states)
      - Rollback available (revert to previous hour)
  
  Index_Versioning:
    Format: index_v{timestamp}_{tenant_id}
    Retention: Keep 5 most recent versions
    Rollback: 30-day window (go back to any recent version)
    Immutability: Versions never modified (only created/deleted)

Index_Health_Monitoring:

  Metrics_Tracked:
    - Vector_count: Total vectors in index
    - Build_time: Duration of rebuild
    - Query_latency: p50, p95, p99
    - Accuracy: Compare against brute force (sample)
    - Cache_hit_rate: Cache effectiveness
    - Index_size: On-disk size
  
  Accuracy_Check:
    Frequency: Weekly (post-rebuild)
    Method:
      1. Sample 100 random queries
      2. Run against IVFFlat (approximate)
      3. Run against BruteForce (exact)
      4. Compare top-10 results
      5. Calculate accuracy
    SLA: >= 95% accuracy (alert if drops below)
    Action: If < 95%, rebuild with different nlist
  
  Freshness_Guarantee:
    Incremental_Lag: Max 1 hour
    Full_Rebuild: Weekly (Sunday 2 AM UTC)
    New_Ideas: Searchable within 1 hour
    Modified_Ideas: Reflected in next full rebuild
  
  Alerts:
    - Query_latency > 100ms (p95): Alert
    - Accuracy < 95%: Alert + rebuild
    - Index_build_failure: Halt + escalate
    - Cache_hit_rate drops 20%: Investigate
    - Vector_count anomaly: Review
```

---

## SECTION E — PERFORMANCE OPTIMIZATION

### 5.1 LATENCY OPTIMIZATION TECHNIQUES

```yaml
Optimization_Layer_1_Caching:
  
  Query_Caching:
    Purpose: Avoid repeated searches
    Method: Redis with 6-hour TTL
    Key: cache:similarity:{hash(vector)}:{tenant_id}
    Size: ~500KB per entry (top-20 results)
    Hit_Rate: ~60% (user preferences stable)
    Savings: 30ms per query (from 50ms to 20ms)
  
  Metadata_Caching:
    Purpose: Avoid repeated database queries
    Method: Redis with 24-hour TTL
    Key: meta:idea:{idea_id}
    Size: ~1KB per entry
    Hit_Rate: ~90%
    Savings: 10ms per result (batch lookups)
  
  Vector_Caching:
    Purpose: Avoid repeated vector fetches
    Method: Redis with 24-hour TTL
    Key: dna:idea:{idea_id}
    Size: ~2KB per entry (384-dim vector)
    Hit_Rate: ~50% (vectors accessed for recommendations)
    Savings: 5ms per vector fetch

Optimization_Layer_2_Parallelization:
  
  Batch_Queries:
    Allow: Multiple queries in single request
    Format: POST /similarity/batch (up to 100 queries)
    Processing: Parallel search (all queries at once)
    Speedup: ~5x (amortized overhead)
    Latency: ~40ms per query (even with batching)
  
  Async_Metadata_Fetch:
    Method: Fetch metadata while FAISS searches
    Timeline:
      - T0: Start FAISS search (20ms)
      - T10: Start metadata fetch (parallel)
      - T40: FAISS completes, metadata in progress
      - T50: Both complete, assemble response
    Speedup: Hide 20ms metadata latency
  
  Index_Sharding:
    For_Large_Deployments:
      - Shard 1: Ideas A-M (distributed FAISS)
      - Shard 2: Ideas N-Z
      - Query: Fan-out to all shards
      - Results: Merge and re-rank
      - Latency: Still ~50ms (FAISS is already fast)
      - Benefit: Horizontal scaling, no single bottleneck

Optimization_Layer_3_Index_Tuning:
  
  IVF_Parameter_Tuning:
    nlist (Voronoi cells):
      - Recommended: sqrt(n) where n = vector count
      - For 1M vectors: nlist = 1000
      - For 100M vectors: nlist = 10000
      - Trade-off: Higher nlist = slower build, faster query
    
    nprobe (cells to search per query):
      - Default: 10 (good for 95% accuracy)
      - Low_Accuracy_OK: nprobe = 4 (5-10ms, 90% accuracy)
      - High_Accuracy_Needed: nprobe = 20 (30-50ms, 98% accuracy)
      - SLA_Target: nprobe = 10 (balanced, meets 50ms p95)
  
  Quantization:
    Without_Quantization (selected):
      - Storage: 1.5GB per 1M vectors
      - Query_Time: 30-50ms
      - Accuracy: 95%+
    
    With_PQ_Quantization (alternative):
      - Storage: 150MB per 1M vectors (10x compression)
      - Query_Time: 5-20ms (faster)
      - Accuracy: 85% (some loss)
      - Use_Case: Memory-constrained (not Antigravity)

Optimization_Layer_4_Hardware:
  
  CPU_Selection:
    Recommended: High-core-count server (16+ cores)
    Rationale:
      - FAISS leverages multi-core parallelism
      - Index building: Uses all cores
      - Query serving: Benefits from multiple threads
    Server_Spec:
      - CPU: 16+ cores, 3GHz+ clock
      - RAM: 32GB (for 1M vectors + caches)
      - Storage: 500GB SSD (for indexes + backups)
      - Network: 10Gbps (for batch queries)
  
  GPU_Not_Required:
    - FAISS CPU mode sufficient
    - Latency SLA achievable without GPU
    - Cost: GPU expensive, not justified
    - Reliability: CPU more predictable

Optimization_Layer_5_Benchmarking:
  
  Continuous_Profiling:
    Collect_Metrics:
      - Query_latency (percentiles)
      - Index_build_time
      - Cache_hit_rate
      - Accuracy (sampled)
      - Memory_usage
      - CPU_utilization
    Frequency: Every query (sampled 1%)
    Storage: Time-series database (Prometheus)
  
  Performance_Regression_Testing:
    Trigger: Every index rebuild
    Process:
      1. Run 1000 test queries
      2. Measure latency distribution
      3. Compare to baseline
      4. If regression > 10%: Alert
      5. If regression > 20%: Rollback index
  
  Load_Testing:
    Frequency: Monthly
    Scenario_1_Sustained_Load:
      - Simulate: 1000 QPS sustained
      - Duration: 1 hour
      - Measure: Latency, memory, CPU
      - SLA: All p95 < 50ms
    
    Scenario_2_Burst_Load:
      - Simulate: 5000 QPS burst
      - Duration: 5 minutes
      - Measure: Latency stability
      - SLA: p99 < 100ms
    
    Scenario_3_Cache_Invalidation:
      - Simulate: Full cache miss
      - Measure: Latency without caching
      - SLA: p95 < 100ms (doubled)
```

---

## SECTION F — SEARCH QUALITY & ACCURACY

### 6.1 SIMILARITY ACCURACY GUARANTEE

```yaml
Accuracy_Definition:

  What_We_Measure:
    - Top-K recall: How many true neighbors are in top-K
    - Top-K precision: How many returned neighbors are true
    - Ranking_correlation: How well ranking matches true ranking
  
  SLA:
    - Top-10 recall: >= 95% (9 out of 10 true neighbors returned)
    - Top-10 precision: >= 95% (at least 9 out of 10 are true neighbors)
    - Ranking_correlation: >= 0.90 (Spearman correlation)
  
  Measurement:
    Frequency: Weekly post-rebuild
    Method:
      1. Random sample 100 queries
      2. Run against IVFFlat (approximate)
      3. Run against BruteForce (exact)
      4. Compare results
      5. Calculate metrics
    Alert: If any metric drops below SLA
    Action: Rebuild with adjusted nlist/nprobe

False_Negatives_&_False_Positives:

  False_Negative (Missing similar idea):
    Definition: True similar idea not in top-K
    Impact: User misses relevant idea
    Root_Cause: FAISS index incompleteness
    Mitigation:
      - High nprobe (10, default)
      - Frequent rebuilds (weekly)
      - Accuracy monitoring
    SLA: < 5% false negative rate
  
  False_Positive (Dissimilar idea in results):
    Definition: Low-similarity idea in top-K
    Impact: User sees irrelevant idea
    Root_Cause: Coarse Voronoi cell assignment
    Mitigation:
      - Post-search filtering (remove distance outliers)
      - Quality ranking (penalize low similarity)
    SLA: < 5% false positive rate

Ranking_Quality:

  Issue: FAISS returns top-K by distance
  Problem: Distance != importance (quality, recency matter)
  Solution: Multi-factor ranking post-search
  
  Ranking_Formula:
    score = (
      similarity * 0.60 +
      recency_boost * 0.20 +
      creator_reputation * 0.15 +
      engagement_signals * 0.05
    )
  
  Re-ranking_Process:
    1. FAISS returns top-20 by distance
    2. Calculate multi-factor score for each
    3. Re-rank by composite score
    4. Return top-10 (re-ranked)
  
  Impact: Better recommendations, not just similarity
```

---

## SECTION G — SECURITY & COMPLIANCE

### 7.1 SEARCH SECURITY

```yaml
Tenant_Isolation_Enforcement:
  
  Access_Control:
    Per_Query:
      1. Extract tenant_id from JWT token
      2. Load tenant's FAISS index
      3. Query only that index
      4. Return only tenant's vectors
      5. Log query with tenant_id
    Guarantee: Impossible to query cross-tenant
  
  Audit_Trail:
    Log_Structure:
      - timestamp: When query executed
      - user_id: Who executed query
      - tenant_id: Which tenant (for segregation)
      - query_hash: Hash of query vector (not vector itself)
      - result_count: K results returned
      - latency_ms: How long query took
      - cache_hit: Was result cached?
    
    Storage:
      - Append-only PostgreSQL
      - Immutable (no DELETE, no UPDATE)
      - Retention: 7 years (legal hold)
      - Access: Audited separately
    
    Use_Cases:
      - Compliance: Who searched for what
      - Performance: Identify slow queries
      - Abuse: Detect anomalous search patterns
      - Security: Detect unauthorized access attempts

Vector_Privacy:

  What_NOT_Stored:
    - Actual query vectors (store hash only)
    - User IDs (only metadata)
    - Personal information (encrypted separately)
  
  What_IS_Stored:
    - Query hash (SHA-256, one-way)
    - Result IDs (publicly knowable)
    - Latency metrics
    - Tenant ID (for isolation)
  
  Privacy_Guarantee:
    - No PII in query logs
    - Vectors not transmitted in logs
    - Differential privacy: Optional (if requested)

Encryption:

  In_Transit:
    Protocol: TLS 1.3
    Cipher: ECDHE-RSA-AES256-GCM-SHA384
    Certificate: Valid, from public CA
  
  At_Rest:
    FAISS_Index: Stored encrypted (AES-256)
    Redis_Cache: Redis SSL/TLS + encryption
    PostgreSQL: Encrypted (Transparent Data Encryption)
    S3_Backups: KMS encryption (tenant-specific keys)
  
  Key_Management:
    Master_Key: AWS CloudHSM (FIPS 140-2 Level 3)
    Rotation: Automatic (90 days)
    Per_Tenant: Separate encryption keys
    Audit: All key operations logged

Rate_Limiting:

  Per_User:
    - 10,000 similarity queries/day per user
    - 100 queries/minute burst
    - Soft limit: Warning at 80%
    - Hard limit: Block at 100%
    - Window: Sliding 24-hour
  
  Per_Tenant:
    - 1,000,000 queries/day per tenant
    - 10,000 queries/minute burst
    - Soft limit: Alert ops at 80%
    - Hard limit: Return 429 Too Many Requests
    - Window: Sliding 24-hour
  
  Implementation:
    Tool: Redis (atomic operations)
    Key: ratelimit:user:{user_id}:{date}
    Enforcement: Middleware (before FAISS query)
```

---

## SECTION H — SCALABILITY & DISTRIBUTED SEARCH

### 8.1 HORIZONTAL SCALING STRATEGY

```yaml
Scaling_Approach:

  Phase_1_Single_Machine:
    Scale: Up to 10M vectors (1 server)
    Setup:
      - Single IVFFlat index (nlist=3160)
      - PostgreSQL for metadata (1 primary)
      - Redis for caching (single instance)
      - FAISS index on SSD
    Performance:
      - Query_latency: ~30-50ms
      - Throughput: 1000+ QPS
  
  Phase_2_Read_Replicas:
    Scale: Up to 50M vectors
    Setup:
      - FAISS index on multiple servers (read-only replicas)
      - PostgreSQL read replicas
      - Redis cluster (3 nodes)
      - Load balancer distributes queries
    Distribution:
      - Query_1 → Server A (FAISS 1)
      - Query_2 → Server B (FAISS 2)
      - Query_3 → Server A (FAISS 1)
    Performance:
      - Query_latency: ~30-50ms (per-server)
      - Throughput: 5000+ QPS (distributed)
      - Consistency: Eventually consistent indexes
  
  Phase_3_Sharded_Indexes:
    Scale: 50M-500M vectors
    Setup:
      - Partition vector space by ID range
      - Shard_1: Ideas A-M (200M vectors)
      - Shard_2: Ideas N-Z (300M vectors)
      - Each shard: IVFFlat index (2-3 replicas)
      - Fan-out query service
    Query_Flow:
      1. Client sends query to router
      2. Router fans out to Shard_1 & Shard_2
      3. Both shards search in parallel
      4. Router merges top-K from each
      5. Returns top-20 (re-ranked) to client
    Performance:
      - Query_latency: ~50-80ms (fan-out overhead)
      - Throughput: 10000+ QPS
      - Scalability: Add shards as needed
  
  Phase_4_Federated_Search:
    Scale: 500M+ vectors (cross-tenant)
    Setup:
      - Separate FAISS cluster per tenant (large)
      - Shared FAISS cluster for small tenants
      - Federated query orchestrator
    Query_Flow:
      1. Client queries with tenant_id
      2. Router identifies tenant's index
      3. Route to appropriate index cluster
      4. Execute search (isolated by tenant)
    Isolation: Guaranteed (never cross-tenant)
    Performance: Same as Phase 3 (per-tenant)

Load_Balancing:

  Strategy: Round-robin with health checks
  Sticky_Sessions: None (stateless FAISS search)
  Health_Check:
    - Every 10 seconds
    - Query health endpoint
    - Remove unhealthy servers
  Failover: Automatic (to healthy replicas)

Consistency_Model:

  Approach: Eventually consistent
  Guarantee:
    - All replicas have same index within 1 hour
    - Incremental updates applied every hour
    - Full rebuild every 7 days (synchronized)
  
  Implications:
    - New ideas searchable within 1 hour
    - Deleted ideas removed within 1 hour
    - Modified vectors updated within 1 hour
  
  User_Experience:
    - Some queries may see slightly different results
    - Ranking may vary (newer replicas have newer data)
    - Acceptable for recommendations use-case
```

---

## SECTION I — MONITORING & OBSERVABILITY

### 9.1 METRICS & ALERTING

```yaml
Key_Metrics:

  Latency_Metrics:
    - search_query_latency_ms: How long each query takes
    - index_build_duration_seconds: Weekly rebuild time
    - cache_lookup_latency_ms: Cache hit latency
    - metadata_fetch_latency_ms: Database query latency
  
  Throughput_Metrics:
    - search_queries_per_second: QPS
    - vectors_indexed_per_second: Index build rate
    - cache_operations_per_second: Cache throughput
  
  Quality_Metrics:
    - similarity_accuracy_percent: 95%+ target
    - cache_hit_rate_percent: ~60% target
    - false_negative_rate_percent: < 5% target
    - false_positive_rate_percent: < 5% target
  
  System_Metrics:
    - faiss_index_size_bytes: Index size
    - index_vector_count: Total vectors indexed
    - index_build_failures_total: Build errors
    - query_errors_total: Failed queries
    - rate_limit_hits_total: Rate limit violations

Alerting_Rules:

  Critical_Alerts:
    - index_build_failure: Page oncall immediately
    - query_error_rate > 1%: Page engineer
    - latency_p95 > 100ms for 5 min: Alert
    - faiss_index_corrupt: Page oncall
  
  Warning_Alerts:
    - accuracy < 95%: Alert to ML team
    - cache_hit_rate drops 20%: Investigate
    - index_age > 7 days: Build pending
    - vector_count unexpected: Review
  
  Info_Alerts:
    - Daily_summary: Index health report
    - Weekly_accuracy: Post-rebuild metrics
    - Monthly_performance_trends: Analysis

Dashboards:

  Real_Time_Operations:
    - Query latency (p50, p95, p99)
    - QPS (queries per second)
    - Error rate
    - Cache hit rate
    - Active indexes
  
  Index_Health:
    - Index size (bytes)
    - Vector count
    - Last rebuild time
    - Rebuild duration
    - Accuracy metrics
  
  Business_Metrics:
    - Recommendation_acceptance_rate
    - Discovery_engagement
    - User_retention (by recommendation quality)
```

---

## SECTION J — NON-NEGOTIABLE RULES

### 10.1 FORBIDDEN ACTIONS

```yaml
Rule_1_NO_MODIFICATION_OF_INDEXES:
  ❌ Forbidden:
    - Update index values after creation
    - Modify vectors in index
    - Retroactively adjust similarities
    - Rebuild index mid-query
  
  ✅ Required:
    - Indexes: Immutable after build
    - Rebuilds: Atomic (old → new swap)
    - Queries: Against consistent index
    - Versioning: Every rebuild creates new version

Rule_2_NO_CROSS_TENANT_LEAKAGE:
  ❌ Forbidden:
    - Query Tenant A with Tenant B's index
    - Mix tenant vectors in same index
    - Return cross-tenant results
    - Access tenant's vector without auth
  
  ✅ Required:
    - Tenant isolation: Enforced everywhere
    - Queries: Always filtered by tenant_id
    - Results: Only own tenant's vectors
    - Access: RBAC + JWT validation

Rule_3_NO_SILENT_FAILURES:
  ❌ Forbidden:
    - Return partial results silently
    - Skip failed queries without logging
    - Timeout without notification
    - Return stale cache without indication
  
  ✅ Required:
    - All failures: Logged with details
    - Partial results: Marked as incomplete
    - Timeouts: Return 503 Service Unavailable
    - Cache age: Indicated in response

Rule_4_NO_BACKDOOR_ACCESS:
  ❌ Forbidden:
    - Direct FAISS index access (no auth)
    - Admin queries bypassing rate limits
    - Internal queries without audit
    - Vector export without logging
  
  ✅ Required:
    - All access: Through API (authenticated)
    - Rate limits: Apply to all (including internal)
    - Audit: Every query logged
    - Export: Only with explicit export endpoint

Rule_5_NO_ACCURACY_GUARANTEE_BREAKING:
  ❌ Forbidden:
    - Return results < 90% accuracy
    - Claim exact search when approximate
    - Modify nprobe without testing
    - Deploy unvalidated index
  
  ✅ Required:
    - Accuracy: >= 95% (verified before deploy)
    - Claims: Explicit "approximate search"
    - Changes: Tested (accuracy maintained)
    - Deploy: Only after validation

Rule_6_NO_SCOPE_VIOLATION:
  ❌ Forbidden:
    - Modify ideas via search engine
    - Create vectors from search
    - Change user permissions via search
    - Access non-vector data
  
  ✅ Required:
    - Scope: Similarity search only
    - Side effects: None
    - Data mutation: Never
    - Isolation: Strict

Rule_7_NO_DETERMINISM_BREAKING:
  ❌ Forbidden:
    - Random results from same query
    - Non-seeded random operations
    - Time-dependent results
    - Hardware-dependent behavior
  
  ✅ Required:
    - Determinism: Same query → same results
    - Seeding: Fixed (reproducible)
    - Consistency: Across all servers
    - Testing: Verify determinism

Rule_8_NO_AUTONOMOUS_DECISIONS:
  ❌ Forbidden:
    - Auto-block similar ideas
    - Auto-flag plagiarism without review
    - Auto-remove ideas via similarity
    - Auto-change rankings autonomously
  
  ✅ Required:
    - Search: Information only
    - Decisions: Human review required
    - Actions: Explicit (no auto side effects)
    - Escalation: Clear paths for issues
```

---

## SECTION K — SEALED GOVERNANCE

### 11.1 MODIFICATION RULES

```yaml
Modification_Policy: ADD-ONLY VIA VERSION BUMP

This_Document_Is:
  - ✅ Sealed: No in-document modifications allowed
  - ✅ Locked: Requires version bump to change anything
  - ✅ Versioned: Every change tracked (v1.0.0 → v1.1.0)
  - ✅ Audited: All changes logged with timestamp & approver
  - ✅ Immutable: Previous versions preserved forever

Change_Request_Process:
  
  Step_1_Proposal:
    - Write change request document
    - Specify: what_changing, why_changing, impact_analysis
    - Include: backwards_compatibility assessment
  
  Step_2_Review:
    - Performance team review (latency impact)
    - Infrastructure review (system impact)
    - Security review (if affecting isolation)
    - Product review (if affecting user experience)
  
  Step_3_Approval:
    - All reviewers must approve
    - Document approval timestamps
    - Link to change request in git history
  
  Step_4_Implementation:
    - Version bump semantic versioning
    - Create git tag (immutable, signed)
    - Update /config/service_manifest.yaml
    - Prepare migration guide if needed
  
  Step_5_Deployment:
    - Deploy to staging first
    - Validate behavior
    - Load test (verify latency SLA)
    - Deploy to production with canary
  
  Step_6_Documentation:
    - Update CHANGELOG.md
    - Update this spec (new version file)
    - Archive previous version
    - Communicate to stakeholders

Approval_Authority:
  - Performance: Performance engineer + architect
  - Infrastructure: Infra lead + DevOps
  - Security: CISO + security engineer
  - Product: Product manager
  - All must sign off before deployment
```

### 11.2 SEAL DECLARATION

```
🔒 SEAL CERTIFICATION

Document: FAST_SIMILARITY.md
Version: 1.0.0
Status: SEALED & LOCKED
Sealed_Date: 2025-02-25
Sealed_By: [AUTHORIZED_SIGNER]
Sealed_Timestamp: 2025-02-25T18:30:00Z

This document defines the FAST_SIMILARITY_ENGINE for Ecoskiller
Antigravity platform. It represents the complete, authoritative specification
for ultra-fast approximate nearest neighbor search on idea vectors.

SEALED guarantees:
✅ No modifications without version bump
✅ All changes require multi-level approval
✅ Previous versions archived immutably
✅ Audit trail of all changes maintained
✅ Backwards compatibility rules enforced
✅ Governance oversight mandatory

Authorized_Signatories:
- [Performance Lead] - Performance Authority
- [Infra Lead] - Infrastructure Authority
- [CISO Name] - Security Authority
- [Product Lead Name] - Product Authority

This seal is cryptographically bound to git commit hash:
Commit: [GIT_COMMIT_HASH_SHA256]
Tree: [GIT_TREE_HASH]
Sign: [GPG_SIGNATURE]

Any modification requires explicit re-sealing and re-certification.

Seal Integrity: VERIFIED ✅
```

---

## SUMMARY

This FAST_SIMILARITY.md specification defines a production-grade, ultra-fast similarity search engine for the Ecoskiller Antigravity platform.

**Key Characteristics:**
- ✅ Sub-50ms latency (SLA compliant)
- ✅ 1000+ RPS throughput
- ✅ 95%+ accuracy (vs brute force)
- ✅ 100M+ vector support
- ✅ Deterministic (same query → same results)
- ✅ Immutable indexes (versioned, no changes)
- ✅ Secure (tenant isolation, encryption, audit)
- ✅ Observable (comprehensive monitoring)
- ✅ Sealed (locked specification)

**Non-Negotiable:**
- No index modification (only creation)
- No cross-tenant leakage (strict isolation)
- No silent failures (all logged)
- No backdoor access (auth required)
- No accuracy breaking (verified)
- No scope violation (search only)
- No determinism breaking (reproducible)
- No autonomous decisions (human-in-loop)

**Execution Authority:** Human declaration only
**Mutation Policy:** Add-only via version bump
**Status:** SEALED & LOCKED
**Effective Date:** 2025-02-25

---

*End of Specification*
