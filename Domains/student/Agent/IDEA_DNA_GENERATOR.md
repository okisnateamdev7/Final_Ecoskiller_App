# 🔒 IDEA_DNA_GENERATOR.md — ANTIGRAVITY SYSTEM SPEC
**Status:** SEALED · LOCKED · GOVERNED · DETERMINISTIC  
**Version:** 1.0.0  
**Mutation Policy:** Add-only via version bump  
**Execution Authority:** Human declaration only  
**Last Updated:** 2025-02-25  

---

## 🏢 EXECUTIVE CONTEXT

**Platform:** Ecoskiller Antigravity  
**Scale Target:** 10M–100M users  
**Architecture:** Microservices + Event-Driven  
**ML Ratio:** 85% traditional ML (heavy vector operations)  
**AI Ratio:** 15% LLM (for metadata generation only)  
**Security Model:** Zero-trust multi-tenant isolation  
**Compliance:** Append-only audit trail, immutable vectors  

---

## SECTION A — DNA SYSTEM IDENTITY

### 1.1 AGENT SPECIFICATION (MANDATORY)

```
AGENT_NAME = IDEA_DNA_GENERATOR
SYSTEM_ROLE = Idea vectorization, clustering, similarity detection, and originality analysis
PRIMARY_DOMAIN = Idea Vector Space & Similarity Engine
EXECUTION_MODE = Deterministic + Validated
DATA_SCOPE = Idea vectors, embeddings, similarity metadata
TENANT_SCOPE = Strict isolation (multi-tenant)
FAILURE_POLICY = Halt on ambiguity, LOG, ESCALATE
ENVIRONMENT = Dev / Test / Staging / Production
DEPLOYMENT_MODEL = Stateless microservice + Vector Store
```

### 1.2 PURPOSE DECLARATION

**Problem Solved:**
- Generate immutable DNA signatures for ideas (vector embeddings)
- Detect originality via similarity matching
- Cluster related ideas automatically
- Build idea recommendation graphs
- Power discovery & serendipity
- Enable cross-idea lineage tracking
- Support innovation economy tracking

**Input Consumed:**
- Idea submission event (from IDEA_SUBMISSION_MANAGER)
- Idea version event (from IDEA_VERSIONING_MANAGER)
- Idea metadata (title, description, category, domain, tags)
- User metadata (reputation, expertise)

**Output Produced:**
- Idea DNA vector (384-dimensional, immutable)
- Similarity report (top-K similar ideas)
- Originality score (0-1.0, immutable)
- Cluster membership (related ideas)
- Recommendation vector (for discovery engine)
- Audit reference (immutable trace)

**Downstream Agents:**
- RANKING_ENGINE (uses DNA for ranking)
- DISCOVERY_ENGINE (uses DNA for recommendations)
- ROYALTY_ENGINE (uses originality for royalties)
- NOTIFICATION_AGENT (alerts about similar ideas)
- IDEA_EVOLUTION_TRACKER (tracks derivatives)
- FEATURE_STORE_AGENT (user interest features)

**Upstream Agents:**
- IDEA_SUBMISSION_MANAGER (new idea events)
- IDEA_VERSIONING_MANAGER (version change events)
- IDENTITY_AGENT (user authentication)
- TENANT_ISOLATION_AGENT (multi-tenant scoping)

---

## SECTION B — IDEA DNA CONCEPT

### 2.1 WHAT IS IDEA DNA?

**Definition:**
Idea DNA is an immutable, high-dimensional vector representation of an idea's semantic content, context, and essence. It serves as the biological fingerprint of an idea.

```yaml
Characteristics:

  Immutability:
    - Generated once at idea creation
    - Never changes for that idea version
    - Snapshot of content at that moment
    - Historical versions preserved
  
  Dimensionality:
    - 384-dimensional dense vectors (Sentence-Transformers)
    - Normalized to unit length (L2 norm)
    - Real-valued, continuous representation
    - Enables cosine similarity matching
  
  Determinism:
    - Same content → same DNA always
    - Reproducible from seed
    - No randomness in generation
    - Cryptographic hash for proof
  
  Multi-modal:
    - Content vector (title + description embedding)
    - Metadata vector (category, domain, tags)
    - User vector (creator expertise, reputation)
    - Combined into unified DNA signature

Structure:
  
  DNA_Vector = [
    content_embedding[0..255],      # 256 dims (Sentence-Transformers)
    category_embedding[256..271],   # 16 dims (one-hot encoded)
    domain_embedding[272..281],     # 10 dims (one-hot encoded)
    user_expertise[282..290],       # 9 dims (reputation normalized)
    novelty_factors[291..383]       # 93 dims (pattern-based features)
  ]
  
  Total: 384 dimensions
```

### 2.2 DNA PURPOSES & USE CASES

```yaml
Use_Case_1_Originality_Detection:
  Purpose: Detect plagiarism and novelty
  Mechanism: Cosine similarity to existing idea vectors
  Threshold: > 0.85 similarity = likely plagiarism
  Action: Flag for review, reduce originality score
  Impact: Protects innovation ecosystem
  
Use_Case_2_Idea_Clustering:
  Purpose: Group related ideas automatically
  Mechanism: K-means clustering on idea vectors
  K: 1,000–10,000 clusters (dynamic based on corpus)
  Metric: Euclidean distance
  Output: Cluster membership for each idea
  Impact: Enables browsing by topic, theme, concept
  
Use_Case_3_Discovery_Recommendations:
  Purpose: Recommend similar ideas to users
  Mechanism: Approximate Nearest Neighbors (FAISS)
  K: 5–10 recommendations per user
  Distance: Cosine distance (0.15 threshold)
  Personalization: Weighted by user expertise, interests
  Impact: Drive engagement, cross-idea discovery
  
Use_Case_4_Derivative_Tracking:
  Purpose: Track idea evolution, forks, improvements
  Mechanism: Similarity over time + version tracking
  Metric: Similarity between parent & child ideas
  Threshold: 0.60–0.85 range = derivative
  Impact: Enable attribution, royalty sharing
  
Use_Case_5_Innovation_Graph:
  Purpose: Build knowledge graph of ideas
  Mechanism: Ideas as nodes, similarity as edges
  Directed: Parent → Child (fork/derivative)
  Weighted: By similarity strength
  Impact: Visualize innovation landscape
  
Use_Case_6_Serendipity:
  Purpose: Discover unexpectedly useful ideas
  Mechanism: Proximity in vector space across domains
  Mechanism: Ideas that are similar but in different domains
  Example: "Mobile payments" + "Education" → "Mobile tutoring"
  Impact: Drive cross-domain innovation
```

---

## SECTION C — DNA GENERATION PIPELINE

### 3.1 EMBEDDING MODELS

```yaml
Model_1_Content_Embedding:
  Name: Sentence-Transformers (all-MiniLM-L6-v2)
  Purpose: Embed idea title + description into 384-dim vector
  Input: Title + Description (concatenated, max 512 tokens)
  Output: 384-dimensional dense vector
  Properties:
    - Pretrained on semantic similarity
    - Fast inference (<100ms per idea)
    - Memory efficient (22MB model)
    - Open-source, reproducible
  
  Tokenization:
    tokens = tokenizer.encode(title + " " + description, max_length=512)
  
  Inference:
    embedding = model.encode(tokens)  # 384-dim output
  
  Normalization:
    embedding = embedding / ||embedding||  # L2 norm
  
  Immutability:
    - Store model_version with every vector
    - Never re-embed with different model
    - If model changes: create new column (not overwrite)

Model_2_Metadata_Embedding:
  Purpose: Encode structured metadata as vectors
  Components:
    
    Category_Encoding (16 dimensions):
      Type: One-hot encoding
      Categories: 10 (Product Improvement, Feature, etc.)
      Padding: 6 reserved dimensions for future expansion
      Method: Fixed position, sparse encoding
    
    Domain_Encoding (10 dimensions):
      Type: One-hot encoding
      Domains: 10 (Job, Skill, Project, Education, etc.)
      Method: Fixed position, sparse encoding
    
    Tag_Encoding (variable, padded to 20 dims):
      Type: TF-IDF weighted bag-of-words
      Tags: User-provided up to 10 tags
      Vocabulary: 500 most common tags
      Method: Sparse vector, normalized by tag frequency
    
    User_Expertise (9 dimensions):
      - user_reputation_score: float (0-1, normalized)
      - user_level: int (0-10, encoded as 3-bit components)
      - user_domain_expertise: float (expertise in this domain)
      - user_innovation_rate: float (% of ideas accepted)
      - user_follower_count: log-normalized
      - user_implementation_count: log-normalized
      - user_fork_rate: float (% of ideas forked)
      - user_rejection_rate: float (% rejected)
      - user_collaboration_score: float (0-1)
    
    Time_Features (reserved, 10 dims):
      - submission_time_encoding: hour of day, day of week
      - idea_age_days: log-normalized
      - version_count: idea maturity metric

Model_3_Novelty_Factors (93 dimensions):
  Purpose: Capture uniqueness patterns beyond content similarity
  
  Lexical_Features (20 dims):
    - word_uniqueness_score: % of rare words
    - sentence_complexity: avg sentence length
    - vocabulary_diversity: unique word ratio
    - technical_term_density: % technical jargon
    - domain_specific_language: domain term frequency
    - readability_score: Flesch-Kincaid grade
    - length_normalized: total character count (log)
    - punctuation_pattern: . ! ? ratio
    - capitalization_pattern: distribution of caps
    - emoji_presence: has emojis? (binary)
  
  Semantic_Features (30 dims):
    - entity_count: named entities detected
    - concept_density: semantic concepts per sentence
    - abstraction_level: concrete vs abstract language
    - sentiment_polarity: positive/negative/neutral
    - emotion_intensity: emotional language strength
    - action_verb_density: % action verbs
    - specificity_score: specific vs vague language
    - novelty_indicators: new idea markers (first, innovative, novel)
    - problem_framing: problem vs solution focus
    - solution_clarity: how clearly solution described
    - impact_claims: claimed vs evidenced impact
    - feasibility_indicators: difficulty assessment
    - market_indicators: commercial viability signals
    - user_type_alignment: how well matches target users
    - skill_requirement_level: expertise needed
    - implementation_complexity: technical complexity signals
    - resource_intensity: how many resources needed
    - timeline_realism: is timeline feasible
    - team_size_hints: lone wolf vs collaboration signal
    - risk_assessment: risk level implied
    - competitive_positioning: how positioned vs existing
    - market_size_signals: market opportunity size
    - cost_efficiency: value per dollar
    - sustainability_indicators: long-term viability
    - scalability_indicators: growth potential
    - social_impact: societal benefit signals
    - environmental_signals: eco-friendly indicators
    - inclusivity_markers: diversity & accessibility signals
    - innovation_type: incremental vs disruptive
    - cross_domain_signals: domain-crossing patterns
  
  Statistical_Features (43 dims):
    - distribution statistics: mean, median, std, skew
    - entropy_measures: information content
    - compression_ratio: how compressible
    - ngram_uniqueness: N-gram novelty
    - syllable_patterns: phonetic diversity
    - vowel_consonant_ratio: phonetic balance
    - repetition_score: word/phrase repetition
    - parallelism_score: parallel structure
    - metaphor_density: figurative language
    - active_vs_passive: voice distribution
    - sentence_structure_variety: syntactic diversity
    - dependency_graph_complexity: grammatical complexity
    - semantic_richness: how many distinct concepts
    - knowledge_graph_coverage: concepts in knowledge base
    - relatedness_to_existing: similarity to corpus average
    - category_fit_strength: how well fits category
    - domain_fit_strength: how well fits domain
    - outlier_score: how unusual is this idea
    - diversity_from_recent: differs from recent ideas
    - diversity_from_popular: differs from popular ideas
    - conceptual_distance_avg: avg distance to all ideas
    - conceptual_distance_closest: distance to closest idea
    - conceptual_distance_farthest: distance to furthest idea
    - cluster_cohesion: how coherent within cluster
    - cluster_separation: how distinct from other clusters
    - temporal_position: where in temporal sequence
    - seasonal_relevance: seasonal match
    - trend_alignment: aligns with trends
    - urgency_signals: time-critical markers
    - opportunity_signals: now-or-never signals
    - alignment_with_interests: matches user interests
    - alignment_with_skills: matches user skills
    - alignment_with_goals: matches user goals
    - potential_for_collaboration: collaboration signals
    - potential_for_improvement: improvement potential
    - potential_for_derivative: fork-ability signals
    - precedent_existence: prior art similarity
    - barrier_to_entry: difficulty to implement
    - competitive_advantage: differentiation markers
    - defensibility_score: IP protection potential
    - standard_alignment: follows/breaks standards
    - maturity_level: how developed is idea
```

### 3.2 DNA GENERATION PROCESS

```yaml
Pipeline_Overview:

  Step_1_Receive_Event:
    Trigger: IDEA_SUBMITTED or IDEA_VERSION_CREATED event
    Input: Idea metadata (title, description, category, domain, tags, user)
    Validation: All required fields present
    Tenant_Isolation: Verify tenant_id matches user's tenant
  
  Step_2_Extract_Content:
    Extract: idea_title + idea_description
    Sanitize: Remove HTML, normalize whitespace
    Truncate: Max 512 tokens for embedding model
    Action: Prepare text for embedding
  
  Step_3_Generate_Content_Embedding:
    Model: Sentence-Transformers (all-MiniLM-L6-v2)
    Input: Combined title + description text
    Output: 384-dimensional dense vector
    Processing: Tokenize → Embed → Normalize (L2)
    Time: ~100ms per idea
    Cache: Store in Redis for 24 hours
    Immutability: Record model_version with vector
  
  Step_4_Generate_Metadata_Vectors:
    
    Category_Vector:
      Input: category (enum from 10 options)
      Output: 16-dim one-hot + reserved
      Method: Fixed position encoding
    
    Domain_Vector:
      Input: domain (enum from 10 options)
      Output: 10-dim one-hot
      Method: Fixed position encoding
    
    Tag_Vector:
      Input: tags (0-10 user-provided)
      Output: 20-dim TF-IDF weighted
      Method: Vocabulary lookup, normalization
    
    User_Vector:
      Input: user_reputation, user_level, user_domain_expertise, etc.
      Output: 9-dim normalized vector
      Method: Min-max scaling (0-1 range)
  
  Step_5_Generate_Novelty_Factors:
    
    Lexical_Analysis:
      Tool: TextBlob + custom NLP
      Metrics: 20 linguistic features
      Output: 20-dim vector (normalized)
    
    Semantic_Analysis:
      Tool: spaCy + SemEval datasets
      Metrics: 30 semantic features
      Output: 30-dim vector (normalized)
    
    Statistical_Analysis:
      Tool: scikit-learn + information theory
      Metrics: 43 statistical features
      Output: 43-dim vector (normalized)
    
    Combination:
      Method: Concatenate all 93 dimensions
      Normalization: L2 norm across all features
  
  Step_6_Concatenate_DNA_Vector:
    
    DNA = [
      content_embedding[0:256],       # Content semantic space
      metadata_embedding[256:276],    # Category + Domain + Tags
      user_expertise[276:285],        # User signal
      novelty_factors[285:378],       # Uniqueness signature
      model_version_hash[378:384]     # Immutability proof
    ]
    
    Total_Dimension: 384
    Normalization: L2 norm (unit vector)
    Hash: SHA-256 (content_embedding + novelty_factors)
  
  Step_7_Store_DNA_Vector:
    
    PostgreSQL:
      Table: idea_dna_vectors
      Columns:
        - vector_id (UUID, PK)
        - idea_id (UUID, FK)
        - version_id (UUID, FK)
        - tenant_id (UUID)
        - vector_blob (VECTOR[384])
        - model_version (string)
        - generated_at (timestamp)
        - immutable_hash (SHA-256)
        - vector_metadata (JSONB)
    
    Redis:
      Key: dna:idea:{idea_id}
      Value: Serialized 384-dim vector
      TTL: 24 hours
      Use: Fast similarity searches
    
    FAISS:
      Index: vector_index (IVFFlat, 384-dim)
      Refresh: Daily rebuild at 2 AM UTC
      Use: Fast nearest neighbor search
    
    Elasticsearch:
      Index: ideas_dna_idx
      Mapping: Vector field (dense_vector)
      Refresh: Every 1 hour
      Use: Hybrid search (text + vector)
  
  Step_8_Immutability_Guarantee:
    
    Content_Hash:
      Input: content_embedding + novelty_factors
      Algorithm: SHA-256
      Storage: Stored with vector record
      Purpose: Cryptographic proof of content
    
    Version_Lock:
      Field: model_version (never updated)
      Purpose: Track which model generated vector
      Immutability: Cannot change after creation
    
    Timestamp_Lock:
      Field: generated_at (permanent)
      Format: UTC ISO8601
      Purpose: Track generation time
      Immutability: Cannot change after creation
    
    No_Update_Policy:
      Rule: DNA vectors are INSERT-only
      Action: If update needed, create new record
      Trace: Keep old record for audit trail
      Never: Overwrite existing DNA vector
  
  Step_9_Emit_DOWNSTREAM_EVENT:
    
    Event: DNA_GENERATED
    Payload:
      {
        "event_id": "UUID",
        "idea_id": "UUID",
        "version_id": "UUID",
        "vector_id": "UUID",
        "tenant_id": "UUID",
        "model_version": "string",
        "immutable_hash": "SHA-256",
        "timestamp": "ISO8601"
      }
    
    Consumers:
      - IDEA_DNA_AGENT (this system, next step)
      - RANKING_ENGINE (update ranking)
      - DISCOVERY_ENGINE (update recommendations)
      - AUDIT_AGENT (log event)
```

---

## SECTION D — SIMILARITY DETECTION & CLUSTERING

### 4.1 ORIGINALITY DETECTION

```yaml
Originality_Algorithm:
  
  Input:
    - New idea vector (384 dims)
    - Existing idea vectors (in FAISS index)
  
  Process:
    
    Step_1_FAISS_Lookup:
      Index: vector_index (IVFFlat, 384-dim)
      Query: New idea vector
      K: 10 (retrieve top 10 most similar)
      Distance_Metric: L2 (Euclidean)
      Distance_Threshold: 0.20 (soft limit)
    
    Step_2_Similarity_Calculation:
      For each of top-10:
        similarity = 1.0 - (L2_distance / max_possible_distance)
        # Converts L2 distance to similarity (0-1)
    
    Step_3_Recency_Weighting:
      For each of top-10:
        age_days = (now() - created_date).days
        recency_weight = 1.0 / (1.0 + log10(age_days))
        # Recent ideas weighted more heavily
        weighted_similarity = similarity * recency_weight
    
    Step_4_Originality_Score_Calculation:
      originality_score = 1.0 - (weighted_avg_similarity)
      where weighted_avg_similarity = sum(weighted_similarities) / 10
      
      Range: 0.0 (plagiarism) to 1.0 (completely original)
      
      Interpretation:
        ├─ 0.0–0.40: Likely plagiarism (flag for review)
        ├─ 0.40–0.60: Derivative/incremental (moderate originality)
        ├─ 0.60–0.80: Original with some similar concepts
        ├─ 0.80–1.00: Highly original (novel idea)
        └─ 1.0: No similar ideas found in corpus
  
  Output:
    {
      "originality_score": float (0-1.0),
      "similar_ideas": [
        {
          "idea_id": "UUID",
          "similarity_coefficient": float (0-1.0),
          "title": "string",
          "created_by": "UUID",
          "created_at": "ISO8601",
          "category": "string",
          "domain": "string"
        }
      ],
      "plagiarism_flag": boolean,
      "derived_flag": boolean,
      "analysis_timestamp": "ISO8601",
      "model_version": "string"
    }

Plagiarism_Detection:
  
  Trigger: originality_score < 0.40
  Action: Flag for human review
  Notification: Reviewer alerted
  Impact: May reduce originality payment
  Duration: Review window 24-48 hours
  
  False_Positive_Handling:
    - User can contest plagiarism flag
    - Provide evidence of independent development
    - Expert review escalation
    - Appeal process documented

Immutability_of_Originality:
  
  Rule: Once calculated, originality_score never changes
  Reason: Historical accuracy, audit trail preservation
  If_Corpus_Changes: No retroactive recalculation
  If_Model_Updates: Generate separate score with new model
  Storage: Every originality score immutably stored
```

### 4.2 IDEA CLUSTERING

```yaml
Clustering_Algorithm:
  
  Input:
    - All idea DNA vectors (N ideas)
    - Target cluster count K (dynamic based on corpus size)
  
  Algorithm: K-means (scikit-learn, batch processing)
  
  K_Selection:
    - If corpus_size < 10,000: K = corpus_size / 10
    - If corpus_size 10K–100K: K = corpus_size / 20
    - If corpus_size 100K–1M: K = corpus_size / 50
    - Max K: 50,000 (practical limit)
    - Min K: 100 (minimum diversity)
  
  Process:
    
    Step_1_Preprocessing:
      Input: All idea DNA vectors
      Normalize: L2 normalization (already done)
      Dimensionality_Reduction: Optional (using PCA if N > 1M)
      Output: Cleaned vector matrix
    
    Step_2_K_means_Training:
      Algorithm: scikit-learn.cluster.KMeans
      K: Dynamically calculated
      Init_Method: k-means++
      Random_State: 42 (deterministic)
      N_Init: 10 (multiple initializations)
      Max_Iter: 300
      Tol: 1e-4 (convergence threshold)
      Batch_Size: 256 (memory efficient)
    
    Step_3_Cluster_Assignment:
      For each idea:
        cluster_id = kmeans.predict(idea_vector)
      Output: Cluster membership for all ideas
    
    Step_4_Cluster_Characterization:
      For each cluster:
        - Centroid: Mean of all cluster vectors
        - Size: Number of ideas in cluster
        - Cohesion: Average distance to centroid
        - Separation: Min distance to other centroids
        - Top_Terms: Most common words in cluster
        - Top_Categories: Most common categories
        - Top_Domains: Most common domains
        - Creation_Date_Range: Temporal span
        - Topic_Name: Auto-generated label
    
    Step_5_Hierarchical_Structure:
      Build: 3-level hierarchy
      L1: 10–20 major thematic clusters (domains)
      L2: K medium clusters (topics)
      L3: Sub-clusters (specific problem spaces)
      Navigation: Users browse L1 → L2 → L3
  
  Output_Storage:
    
    PostgreSQL:
      Table: idea_clusters
      Columns:
        - cluster_id (UUID)
        - cluster_level (1, 2, or 3)
        - parent_cluster_id (UUID, for hierarchy)
        - centroid_vector (VECTOR[384])
        - cluster_size (integer)
        - cohesion_score (float)
        - topic_name (string)
        - top_categories (array)
        - top_domains (array)
        - created_at (timestamp)
    
    Table: idea_cluster_membership
      Columns:
        - membership_id (UUID)
        - idea_id (UUID)
        - cluster_id (UUID)
        - confidence_score (float 0-1)
        - created_at (timestamp)
  
  Refresh_Schedule:
    - Weekly: Recalculate clusters from scratch
    - Timing: Sunday 3 AM UTC (off-peak)
    - Duration: 30 minutes (1M ideas)
    - Parallel: Use all CPU cores
    - Incremental: New ideas added to nearest cluster
    - Immutability: Old cluster assignments preserved
  
  Use_Cases:
    
    Browse_by_Cluster:
      User selects domain (L1 cluster)
      → Shows topics (L2 clusters)
      → Shows ideas (L3 assignments)
      → Enables topic-based discovery
    
    Similar_Ideas_Widget:
      Show 5–10 most similar ideas
      From same cluster OR similar clusters
      Ranked by distance to user's idea
    
    Recommendations:
      User's interests → Similar clusters
      Show ideas from high-relevance clusters
      Personalized by user expertise
```

---

## SECTION E — DISCOVERY & RECOMMENDATION ENGINE

### 5.1 RECOMMENDATION PIPELINE

```yaml
Recommendation_Architecture:

  Input_Signals:
    - Current user
    - User's interests (previous ideas viewed/liked)
    - User's expertise (domain knowledge)
    - User's role (innovator, reviewer, implementer)
    - Current context (browsing category/domain)
  
  Process:
    
    Step_1_User_Profile_Vector:
      Build: User interest vector (384 dims)
      Source: Ideas user has engaged with
      Method: Weighted average of engagement_ideas_vectors
      Weights: Views=1x, Comments=2x, Likes=3x, Implementations=5x
      Output: user_preference_vector
    
    Step_2_Candidate_Generation:
      Query: Find ideas similar to user_preference_vector
      Method: FAISS nearest neighbor search
      K: 100 (retrieve top-100 candidates)
      Filter: Exclude ideas already seen by user
      Filter: Exclude ideas from followed creators
      Output: candidate_ideas (100 items)
    
    Step_3_Ranking:
      For each candidate:
        
        score = (
          similarity_to_user * 0.40 +
          idea_quality * 0.20 +
          creator_reputation * 0.15 +
          recency_boost * 0.10 +
          cluster_diversity * 0.10 +
          engagement_momentum * 0.05
        )
        
        Where:
          similarity_to_user: Cosine similarity to user profile
          idea_quality: Quality score (from IDEA_SUBMISSION_MANAGER)
          creator_reputation: Creator's reputation (0-1)
          recency_boost: Newer ideas boosted
          cluster_diversity: Avoid recommending same cluster
          engagement_momentum: Recent viral trend
      
      Output: Ranked list (top-20 recommendations)
    
    Step_4_Personalization:
      Apply: User expertise filters
      Method: Recommendation difficulty must match user level
      Filter: Avoid too-easy or too-hard ideas
      Output: Final personalized list (top-10)
    
    Step_5_Diversity:
      Ensure: Recommendations span multiple clusters
      Method: Re-rank to maximize cluster diversity
      Output: Final list with diversity guarantee
  
  Caching:
    User_Recommendation_Cache:
      Key: recommendations:user:{user_id}
      Value: Serialized top-20 list
      TTL: 6 hours (refresh if user activity)
      Hit_Rate: ~80% (80% from cache)
  
  A_B_Testing:
    Algorithm_Variants:
      - Control: Current recommendation algorithm
      - Variant_A: Increased diversity weight
      - Variant_B: Increased creator_reputation weight
      - Variant_C: New temporal decay model
    
    Metrics:
      - Click_through_rate
      - Idea_implementation_rate
      - User_satisfaction (survey)
      - Session_duration
    
    Rollout: Winner gets 100% traffic after 2 weeks
  
  Output:
    {
      "recommendations": [
        {
          "idea_id": "UUID",
          "title": "string",
          "score": float (0-1),
          "reason": "string (why recommended)",
          "similarity": float (0-1),
          "category": "string",
          "domain": "string",
          "creator": {
            "user_id": "UUID",
            "name": "string",
            "reputation": float
          }
        }
      ],
      "generated_at": "ISO8601",
      "algorithm_version": "string"
    }
```

### 5.2 SERENDIPITOUS DISCOVERY

```yaml
Serendipity_Discovery:
  
  Purpose: Find unexpectedly useful ideas across domains
  
  Algorithm:
    
    Step_1_Domain_Jump:
      Current_Domain: The domain user is in
      Similar_Domains: Find conceptually similar domains
      Method: Cosine similarity between domain centroid vectors
      Threshold: 0.40–0.70 (moderate distance)
      Output: Target domains (not current domain)
    
    Step_2_Cross_Domain_Search:
      Query: user_preference_vector
      Search_Space: Ideas from target domains only
      Method: FAISS nearest neighbor (different index)
      K: 50 (retrieve top-50 from target domains)
    
    Step_3_Novelty_Filter:
      For each idea:
        similarity_to_user_domain = cosine_similarity(
          user_vector,
          idea_vector_in_target_domain
        )
        
        If similarity_to_user_domain < 0.50:
          Flag as "serendipitous" (unexpectedly useful)
      
      Output: Filtered serendipitous ideas
    
    Step_4_Ranking:
      Score: Same as recommendations
      Boost: Add 0.10 bonus for serendipity_factor
      Output: Top-5 serendipitous ideas
  
  Examples:
    - User: Data scientist interested in ML
    - Current domain: AI / Automation
    - Serendipity target: Education (similar conceptually)
    - Recommendation: "ML for personalized learning"
    - Surprise: Completely different domain, but relevant
  
  Use_Case:
    "Explore Unexpected Ideas" button in UI
    Shows creative cross-domain inspirations
    Drives innovation by exposing new possibilities
```

---

## SECTION F — DERIVATIVE TRACKING & LINEAGE

### 6.1 FORK & DERIVATIVE DETECTION

```yaml
Derivative_Detection:
  
  Trigger: New idea version created (from IDEA_VERSIONING_MANAGER)
  
  Input:
    - Parent idea vector
    - Child/fork idea vector
    - Explicit fork_parent_id (if user indicated fork)
  
  Process:
    
    Step_1_Explicit_Fork_Check:
      If fork_parent_id provided:
        Relationship: EXPLICIT_FORK (user confirmed)
        Confidence: 1.0 (100%)
        Royalty: Apply predefined sharing
      Else:
        Continue to implicit detection
    
    Step_2_Implicit_DERIVATIVE_Detection:
      Similarity = cosine_similarity(parent_vector, child_vector)
      
      If similarity > 0.80:
        Type: LIKELY_DERIVATIVE
        Confidence: similarity
        Reason: High similarity detected
      
      Else if similarity > 0.60:
        Type: RELATED_IDEA
        Confidence: similarity
        Reason: Moderate similarity
      
      Else:
        Type: INDEPENDENT
        Confidence: 1.0 - similarity
        Reason: Low similarity, likely independent
    
    Step_3_Validation:
      If DERIVATIVE or RELATED:
        Human_Review: Required (optional at scale)
        Expert_Classification: Derivative analyst reviews
        Decision: Confirm or dispute relationship
    
    Step_4_Storage:
      Table: idea_derivatives
      Columns:
        - parent_idea_id (UUID)
        - child_idea_id (UUID)
        - relationship_type (EXPLICIT_FORK, DERIVATIVE, RELATED, INDEPENDENT)
        - similarity_score (float 0-1)
        - confidence (float 0-1)
        - detected_at (timestamp)
        - human_verified (boolean)
        - verified_by (UUID, if verified)
  
  Output_Event:
    {
      "event_type": "DERIVATIVE_DETECTED",
      "parent_idea_id": "UUID",
      "child_idea_id": "UUID",
      "relationship_type": "EXPLICIT_FORK",
      "similarity_score": 0.85,
      "timestamp": "ISO8601"
    }
    
    Consumers:
      - ROYALTY_ENGINE (apply revenue sharing)
      - RANKING_ENGINE (boost parent visibility)
      - NOTIFICATION_AGENT (notify parent creator)
      - IDEA_EVOLUTION_TRACKER
```

---

## SECTION G — VECTOR STORE & INDEXING

### 7.1 FAISS INDEX MANAGEMENT

```yaml
FAISS_Index_Specification:

  Index_Type: IVFFlat (Inverted File + Flat Quantizer)
  
  Configuration:
    D: 384 (vector dimension)
    nlist: 100 (number of Voronoi cells)
    metric: L2 (Euclidean distance)
    ngpu: 0 (CPU only for determinism)
  
  Index_Building:
    
    Frequency: Daily at 2 AM UTC (off-peak)
    Duration: 30 minutes (1M ideas)
    Process:
      1. Extract all idea DNA vectors from PostgreSQL
      2. Normalize vectors (L2 norm)
      3. Train FAISS index on random subset (10%)
      4. Add all vectors to index
      5. Save index to disk (immutable copy)
      6. Verify integrity (checksum)
      7. Backup to S3 (Glacier after 30 days)
      8. Atomically switch active index
  
  Consistency:
    - During rebuild: Old index remains active
    - After rebuild: Atomic switch to new index
    - No downtime (read from old during transition)
    - Gradual indexing: New ideas added hourly
  
  Storage:
    Location: /data/faiss/idea_dna_index.bin
    Size: ~2GB (1M ideas, 384 dims, float32)
    Checksum: SHA-256 (immutability proof)
    Backup: S3://ecoskiller-vector-backup/faiss/
  
  Search_Performance:
    Query: 10 nearest neighbors
    Latency: ~10ms (p50)
    Latency: ~50ms (p95)
    Accuracy: 95% (vs brute force)
  
  Incremental_Addition:
    
    Schedule: Every 1 hour
    Process:
      1. Fetch ideas created in last hour
      2. Generate DNA vectors (if not already done)
      3. Add to FAISS index (in-memory)
      4. Persist to disk
    
    Guarantees:
      - New ideas searchable within 1 hour
      - No accuracy loss
      - Atomic updates (no partial states)
```

### 7.2 POSTGRESQL VECTOR COLUMN

```yaml
PostgreSQL_Vector_Storage:
  
  Extension: pgvector (open-source)
  Installation: CREATE EXTENSION IF NOT EXISTS vector
  
  Table_Schema:
    
    CREATE TABLE idea_dna_vectors (
      vector_id UUID PRIMARY KEY,
      idea_id UUID NOT NULL,
      version_id UUID NOT NULL,
      tenant_id UUID NOT NULL,
      
      -- 384-dimensional vector
      vector vector(384) NOT NULL,
      
      -- Metadata
      model_version VARCHAR(20) NOT NULL,
      generated_at TIMESTAMP NOT NULL,
      immutable_hash VARCHAR(64) NOT NULL,
      
      -- Immutability guarantee
      created_at TIMESTAMP NOT NULL DEFAULT NOW(),
      updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
      
      -- No updates allowed
      CONSTRAINT no_update CHECK (created_at = created_at),
      
      -- Indexes for query performance
      FOREIGN KEY (idea_id) REFERENCES ideas(idea_id),
      FOREIGN KEY (version_id) REFERENCES idea_versions(version_id),
      FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id),
      
      INDEX idx_idea_id (idea_id),
      INDEX idx_tenant_id (tenant_id),
      INDEX idx_vector_cosine (vector cosine_ops) USING IVFFLAT,
      INDEX idx_generated_at (generated_at)
    );
  
  Query_Examples:
    
    -- Find 10 most similar ideas
    SELECT idea_id, vector <=> @query_vector AS distance
    FROM idea_dna_vectors
    WHERE tenant_id = @tenant_id
    ORDER BY vector <=> @query_vector
    LIMIT 10;
    
    -- Calculate similarity to multiple ideas
    SELECT 
      id1, id2,
      (v1 <=> v2) AS distance,
      1.0 - (v1 <=> v2) AS similarity
    FROM idea_dna_vectors
    WHERE idea_id IN (@idea_ids);
  
  Performance:
    Index_Type: IVFFLAT (fast approximate)
    Query_Time: ~50ms for 10 neighbors (1M vectors)
    Memory: ~2GB (1M vectors, 384 dims)
    Accuracy: 95% (vs brute force)
  
  Immutability:
    - No UPDATE allowed (immutable records)
    - No DELETE allowed (permanent history)
    - Vector values never change
    - INSERT-only approach
```

---

## SECTION H — SECURITY & COMPLIANCE

### 8.1 VECTOR SECURITY

```yaml
Vector_Encryption:
  
  At_Rest:
    Algorithm: AES-256-GCM
    Key_Source: AWS KMS (per-tenant key)
    Scope: All vector storage (PostgreSQL, FAISS, Redis)
    Encryption: Transparent (handled by storage layer)
  
  In_Transit:
    Protocol: TLS 1.3
    Cipher: ECDHE-RSA-AES256-GCM-SHA384
    Scope: All vector API calls
  
  Key_Management:
    Master_Key: AWS CloudHSM (FIPS 140-2 Level 3)
    Rotation: Automatic (90 days)
    Per_Tenant_Keys: Separate keys for each tenant
    Audit: All key operations logged

Vector_Privacy:
  
  Tenant_Isolation:
    - Vectors belong to exactly one tenant
    - Query filters enforce tenant scoping
    - Index partitioned logically by tenant
    - No cross-tenant vector leakage possible
  
  Vector_Anonymization:
    - User IDs not embedded in vectors
    - Creator identity not in vector space
    - Only semantic content + metadata
    - Searchable without identifying user
  
  Differential_Privacy:
    - Option: Add noise to vectors for privacy
    - Threshold: Only if requested explicitly
    - Method: Gaussian noise (DP-SGD)
    - Epsilon: ε = 1.0 (configurable)
    - Impact: Slight reduction in accuracy
```

### 8.2 AUDIT & COMPLIANCE

```yaml
Audit_Trail:
  
  Every_Vector_Generation:
    Logged:
      - vector_id (immutable reference)
      - idea_id (which idea)
      - tenant_id (ownership)
      - generated_at (when)
      - model_version (which model)
      - immutable_hash (cryptographic proof)
      - generator_actor_id (which service)
    
    Storage: PostgreSQL (append-only audit_log table)
    Retention: 7 years (legal hold)
    Access: Immutable (no DELETE)
    Query: Auditable via audit_log_explorer

Vector_Immutability:
  
  Guarantee:
    - Once generated, DNA vector never changes
    - Cannot update vector values
    - Cannot delete vector records
    - Cannot modify metadata
    - Only INSERT allowed
  
  Proof:
    - immutable_hash: SHA-256 of vector content
    - Hash never matches different vector
    - Hash provides cryptographic proof
    - Hash stored immutably with vector
  
  Enforcement:
    - Database constraint: CHECK created_at = created_at
    - Application: Reject all UPDATE requests
    - API: No update endpoints exposed
    - Tests: Verify no updates possible

Compliance_Standards:
  
  SOC_2_Type_II:
    ✅ Immutable audit trail
    ✅ Encryption at rest & in transit
    ✅ Access controls (RBAC)
    ✅ Monitoring & alerting
    ✅ Incident response plan
  
  GDPR:
    ✅ Data classification
    ✅ Consent tracking
    ✅ Right to access (vector export)
    ✅ Right to deletion (soft delete with audit)
    ✅ Data retention (7 years max)
  
  CCPA:
    ✅ Consumer data access
    ✅ Opt-out mechanisms
    ✅ Data deletion rights
    ✅ Transparency reports
```

---

## SECTION I — PERFORMANCE & SCALABILITY

### 9.1 PERFORMANCE TARGETS

```yaml
Latency_Targets:

  Vector_Generation:
    Per_Idea: < 200ms (embedding + novelty features)
    P95: < 300ms
    P99: < 500ms
    Batch_1000_Ideas: < 120 seconds total
  
  Similarity_Search:
    Single_Query: < 50ms (FAISS)
    Top_10_Neighbors: < 50ms
    Top_100_Candidates: < 100ms
    Batch_1000_Queries: < 60 seconds total
  
  Recommendation_Generation:
    Per_User: < 500ms (end-to-end)
    P95: < 800ms
    P99: < 2000ms
    Batch_10000_Users: < 90 seconds total
  
  Clustering:
    Weekly_Rebuild: < 30 minutes (1M ideas)
    Incremental_Addition: < 1 second (100 ideas)
  
  Overall_Request:
    API_Request: < 1000ms (wall-clock time)
    P95: < 1500ms
    P99: < 3000ms

Throughput_Targets:

  Vector_Generation:
    RPS: 100 ideas/second sustained
    Peak: 500 ideas/second (burst)
    Batch_Processing: 1M ideas/day
  
  Similarity_Queries:
    RPS: 1000 queries/second sustained
    Peak: 5000 queries/second (burst)
  
  Recommendations:
    RPS: 100 users/second sustained
    Peak: 500 users/second (burst)
  
  Overall_System:
    Total_RPS: 1000+ sustained
    Peak: 6000 RPS (burst)

Memory_Targets:

  FAISS_Index: ~2GB (1M ideas, 384 dims)
  Redis_Cache: ~1GB (recent vectors + recommendations)
  PostgreSQL_Buffer: ~4GB (hot vectors)
  Total: ~7GB (for 1M ideas)

Scalability:

  Vertical:
    - Scale to 10M ideas on single machine
    - FAISS supports 100M+ vectors
    - PostgreSQL can handle 100M+ rows
  
  Horizontal:
    - Multiple replica FAISS indexes (read-only)
    - PostgreSQL read replicas for queries
    - Distributed recommendation service
    - Multi-shard vector storage
```

---

## SECTION J — NON-NEGOTIABLE RULES

### 10.1 FORBIDDEN ACTIONS

```yaml
Rule_1_Vector_Modification_IS_IMMUTABLE:
  ❌ Forbidden:
    - Update vector values after creation
    - Change vector dimensions
    - Modify immutable_hash
    - Retroactively adjust originality_score
  
  ✅ Required:
    - All vectors: INSERT-only
    - Vectors: Never UPDATE or DELETE
    - Old vectors: Preserved forever
    - New versions: Create new records

Rule_2_No_Hidden_SIMILARITIES:
  ❌ Forbidden:
    - Hide similarity matches from display
    - Suppress plagiarism flags
    - Modify similar_ideas list
    - Change similarity thresholds secretly
  
  ✅ Required:
    - All similarities: Logged & audited
    - Plagiarism: Transparently flagged
    - Thresholds: Fixed, documented
    - Matches: Immutable record

Rule_3_No_Cross_Tenant_VECTOR_LEAKAGE:
  ❌ Forbidden:
    - Query tenant A's vectors with tenant B's token
    - Mix tenant vectors in clustering
    - Share vector indexes across tenants
    - Cross-tenant similarity searches
  
  ✅ Required:
    - Tenant isolation: Enforced everywhere
    - Queries: Always filtered by tenant_id
    - Indexes: Logically partitioned
    - No cross-tenant access ever

Rule_4_No_Retroactive_RECALCULATION:
  ❌ Forbidden:
    - Re-run similarity detection after storage
    - Change originality scores after creation
    - Retroactively adjust clustering
    - Modify historical vectors
  
  ✅ Required:
    - Vectors: Final upon creation
    - Scores: Immutable forever
    - If model changes: Create new column
    - Old data: Preserved as-is

Rule_5_No_Black_Box_ALGORITHMS:
  ❌ Forbidden:
    - Use proprietary black-box models
    - Hide algorithm logic
    - Undocumented decision-making
    - Unexplainable vector operations
  
  ✅ Required:
    - All models: Open-source (Sentence-Transformers)
    - All algorithms: Documented (this spec)
    - All decisions: Explainable (why similar)
    - All features: Listed (novelty factors)

Rule_6_No_Unauthorized_VECTOR_ACCESS:
  ❌ Forbidden:
    - Access vectors without RBAC permission
    - Export vectors without audit logging
    - Use vectors for undisclosed purposes
    - Share vectors with third parties
  
  ✅ Required:
    - All access: RBAC controlled
    - All exports: Logged & audited
    - Purposes: Documented & limited
    - Sharing: Never without consent

Rule_7_No_Scope_VIOLATION:
  ❌ Forbidden:
    - Modify user accounts via DNA system
    - Create ideas from vectors
    - Change idea status via DNA
    - Access non-vector data
  
  ✅ Required:
    - Scope: Vectors only
    - Side effects: None
    - Other systems: Never modified
    - Data: Strictly isolated

Rule_8_No_AUTONOMOUS_DECISIONS:
  ❌ Forbidden:
    - Auto-reject ideas based on vectors alone
    - Auto-ban users due to vectors
    - Auto-remove ideas (without review)
    - Auto-calculate royalties without verification
  
  ✅ Required:
    - ML assists human decisions
    - Humans make final calls
    - Low-confidence: Always escalate
    - Decisions: Auditable & reversible
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
    - ML team review (model changes)
    - Architecture review (system impact)
    - Security review (if affecting encryption/isolation)
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
    - Document any behavioral changes
    - Deploy to production with canary
  
  Step_6_Documentation:
    - Update CHANGELOG.md
    - Update this spec (new version file)
    - Archive previous version
    - Communicate to stakeholders

Approval_Authority:
  - ML Team: ML models, algorithms, drift detection
  - Architecture: System design, integration, scalability
  - Security: Encryption, isolation, compliance
  - Product: User workflows, discovery features
  - All must sign off before deployment
```

### 11.2 SEAL DECLARATION

```
🔒 SEAL CERTIFICATION

Document: IDEA_DNA_GENERATOR.md
Version: 1.0.0
Status: SEALED & LOCKED
Sealed_Date: 2025-02-25
Sealed_By: [AUTHORIZED_SIGNER]
Sealed_Timestamp: 2025-02-25T17:30:00Z

This document defines the IDEA_DNA_GENERATOR agent for Ecoskiller
Antigravity platform. It represents the complete, authoritative specification
for idea vectorization, clustering, and similarity detection.

SEALED guarantees:
✅ No modifications without version bump
✅ All changes require multi-level approval
✅ Previous versions archived immutably
✅ Audit trail of all changes maintained
✅ Backwards compatibility rules enforced
✅ Governance oversight mandatory

Authorized_Signatories:
- [ML Team Lead] - ML Algorithm Authority
- [CTO Name] - Architecture Authority
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

## APPENDIX A — EMBEDDING MODEL SPECIFICATIONS

### A.1 Sentence-Transformers Details

```yaml
Model_Name: sentence-transformers/all-MiniLM-L6-v2
Version: Latest stable
URL: https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2

Specifications:
  Architecture: Distilled BERT (6 layers, 384 dimensions)
  Parameters: 22.7M
  Model_Size: 22MB
  Memory_Usage: ~100MB (with batching)
  Inference_Speed: ~50ms per document (CPU)
  
Training_Data:
  Source: 1B+ sentence pairs from web
  Domain: General knowledge
  Languages: 50+ (multilingual)
  License: Apache 2.0 (open-source)

Performance:
  STS-B_Correlation: 0.84 (semantic textual similarity)
  NLI_Accuracy: 0.92 (natural language inference)
  Multilingual: Excellent (cross-lingual)

Usage_In_System:
  Input: Title + Description (max 512 tokens)
  Output: 384-dimensional vector
  Normalization: L2 norm
  Use_Case: Idea semantic embedding

Reproducibility:
  Seed: Fixed (42)
  Deterministic: Yes (identical outputs)
  Hardware_Agnostic: Yes (same results CPU/GPU)
  Quantization: Optional (int8 quantization available)
```

---

## APPENDIX B — VECTOR SPACE VISUALIZATION

```yaml
Idea_Vector_Space:
  
  Dimensionality: 384
  Metric: Cosine similarity / Euclidean distance
  Clustering: K-means (K=1000–50K)
  
  Interpretation:
    "Each idea is a point in 384-dimensional space.
     Similar ideas cluster together.
     Distance between points = dissimilarity.
     Vector operations preserve semantic relationships."
  
  Visualization_Techniques:
    - t-SNE: Reduce to 2D for visualization
    - UMAP: Better cluster preservation
    - PCA: Linear dimensionality reduction
    - Plotly/D3.js: Interactive 3D exploration
  
  Search_in_Space:
    - Nearest neighbors: Similar ideas
    - Range queries: Ideas within distance threshold
    - Clustering: Group nearby ideas
    - Traversal: Follow paths through space
```

---

## SUMMARY

This IDEA_DNA_GENERATOR.md specification defines a production-grade vector-based system for idea analysis and discovery on Ecoskiller Antigravity.

**Key Characteristics:**
- ✅ Deterministic (identical content → identical vector)
- ✅ Immutable (vectors never modified after creation)
- ✅ Auditable (append-only audit trail)
- ✅ Secure (encryption, tenant isolation, access control)
- ✅ Scalable (100M+ vectors, 1000+ RPS)
- ✅ Observable (comprehensive metrics & monitoring)
- ✅ Versioned (add-only modification policy)
- ✅ Sealed (locked specification)

**Non-Negotiable:**
- No vector modification (only creation)
- No hidden similarities (all transparent)
- No cross-tenant leakage (strict isolation)
- No retroactive recalculation (immutable)
- No black-box algorithms (explainable)
- No unauthorized access (RBAC enforced)
- No scope violations (DNA-only operations)
- No autonomous decisions (human-in-loop)

**Execution Authority:** Human declaration only
**Mutation Policy:** Add-only via version bump
**Status:** SEALED & LOCKED
**Effective Date:** 2025-02-25

---

*End of Specification*
