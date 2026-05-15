# 🔒 MARKET_DEMAND_PREDICTION_AGENT — SEALED SPECIFICATION v1.0

**Agent Class:** ML/Intelligence/Safety Owner  
**Platform:** Ecoskiller Antigravity  
**Mutation Policy:** Add-only via version bump  
**Execution Mode:** Deterministic + Validated  
**Security Model:** Zero-trust multi-tenant isolation  
**Audit Policy:** Append-only immutable trail  
**Stack Compatibility:** 80% ML + 20% AI Semantic  

---

## 1️⃣ AGENT IDENTITY (MANDATORY)

```
AGENT_NAME = MARKET_DEMAND_PREDICTION_AGENT
SYSTEM_ROLE = Job Market Intelligence & Demand Forecasting Owner
PRIMARY_DOMAIN = Labor_Market_Intelligence
EXECUTION_MODE = Deterministic + Validated
DATA_SCOPE = Jobs, Skills, Salaries, Hiring Trends, Training Programs, Market Signals
TENANT_SCOPE = Strict Multi-Tenant Isolation (aggregated insights, NO raw cross-tenant data)
FAILURE_POLICY = Halt on ambiguity + Log + Escalate + Fallback to historical baseline
COMPLIANCE_SCOPE = Data Privacy, Salary Transparency, Non-Discriminatory Predictions
VERSION = 1.0.0
```

**Critical Context:**  
This agent operates within the Ecoskiller ecosystem where:
- 300 user types (students, trainers, institutes, companies, freelancers, government)
- 10M-100M users across Arts, Commerce, Science, Technology, Administration domains
- Job postings, skill development, project execution, education marketplace
- Users need guidance on which skills to learn, which careers to pursue
- Institutes need guidance on curriculum design
- Companies need visibility into talent supply
- Platform needs to match supply and demand efficiently

**Business Impact:**
- Drives student skill selection decisions (affects lifetime earnings)
- Influences institute curriculum investments (millions in costs)
- Guides company hiring strategies (talent acquisition ROI)
- Powers platform recommendation engines (user engagement)
- Enables proactive skill gap identification (economic impact)

---

## 2️⃣ PURPOSE DECLARATION

### What Problem This Agent Solves

**Primary Problems:**
1. **Future Skill Demand Prediction** - Forecast which skills will be in demand 3-36 months ahead
2. **Job Market Trend Analysis** - Identify growth/decline in job categories by domain/geography
3. **Salary Trend Forecasting** - Predict salary ranges for skill combinations and experience levels
4. **Skill Gap Identification** - Detect mismatches between talent supply and employer demand
5. **Training ROI Prediction** - Estimate success likelihood of training programs
6. **Course Enrollment Forecasting** - Predict demand for specific courses/certifications
7. **Hiring Demand Prediction** - Forecast employer hiring needs by sector/role/location
8. **Skill Obsolescence Warning** - Identify skills becoming less relevant
9. **Personalized Career Pathways** - Generate data-driven career recommendations
10. **Economic Impact Analysis** - Measure platform's contribution to employability

### What Input It Consumes

**Primary Input Sources:**
```
Historical Data (Platform Internal):
- Job postings (created, filled, expired) - 5 years history
- Skill demand signals (searches, applications, matches)
- User skill acquisition patterns
- Training program enrollments and completions
- Salary data (offers, acceptances)
- Placement success rates
- Geographic distribution data
- Domain-specific hiring trends
- User behavior patterns (passive intelligence)

External Market Signals:
- Industry growth reports (if available via API)
- Government labor statistics (if available)
- Technology adoption trends
- Economic indicators (GDP, unemployment rates)
- Education policy changes
- Regulatory changes affecting industries
```

**Real-Time Inputs:**
```
- New job postings (streaming)
- New skill searches (streaming)
- New course enrollments (streaming)
- User profile updates (streaming)
- Company hiring announcements (streaming)
```

### What Output It Produces

**Primary Output:**
```json
{
  "prediction_id": "UUID",
  "prediction_type": "skill_demand|job_trend|salary_forecast|skill_gap|training_roi",
  "generated_at": "ISO8601",
  "forecast_horizon": "3_months|6_months|1_year|3_years",
  "target_entity": {
    "entity_type": "skill|job_role|domain|geography|skill_combination",
    "entity_id": "string",
    "entity_name": "string"
  },
  
  "demand_forecast": {
    "current_demand_index": 0-100,
    "predicted_demand_index": 0-100,
    "demand_change_percentage": float,
    "demand_trend": "rising|stable|declining|volatile",
    "confidence_level": "high|medium|low",
    "confidence_score": 0.0-1.0
  },
  
  "time_series_prediction": [
    {
      "period": "2026-Q2",
      "demand_index": 0-100,
      "lower_bound": float,
      "upper_bound": float,
      "confidence_interval": "95%"
    }
  ],
  
  "contributing_factors": [
    {
      "factor": "string (e.g., 'AI adoption in healthcare')",
      "impact_weight": 0.0-1.0,
      "direction": "positive|negative",
      "evidence_strength": "strong|moderate|weak"
    }
  ],
  
  "skill_gap_analysis": {
    "current_supply": integer,
    "predicted_demand": integer,
    "gap_size": integer,
    "gap_percentage": float,
    "urgency": "critical|high|medium|low",
    "time_to_critical": "months"
  },
  
  "salary_forecast": {
    "current_median_salary": float,
    "predicted_median_salary": float,
    "salary_range_lower": float,
    "salary_range_upper": float,
    "growth_rate": "percentage",
    "market_competitiveness": "high|medium|low"
  },
  
  "geographic_breakdown": [
    {
      "location": "city/state/country",
      "demand_index": 0-100,
      "growth_rate": float,
      "market_maturity": "emerging|growing|mature|declining"
    }
  ],
  
  "similar_predictions": [
    {
      "entity_name": "string",
      "similarity_score": 0.0-1.0,
      "demand_pattern": "string"
    }
  ],
  
  "recommendations": {
    "for_students": ["string recommendations"],
    "for_trainers": ["string recommendations"],
    "for_institutes": ["string recommendations"],
    "for_companies": ["string recommendations"]
  },
  
  "risk_factors": [
    {
      "risk_type": "automation|regulation|economic|technological",
      "description": "string",
      "probability": 0.0-1.0,
      "impact_severity": "high|medium|low"
    }
  ],
  
  "ml_metadata": {
    "model_version": "string",
    "model_type": "time_series|regression|classification|ensemble",
    "training_date": "ISO8601",
    "feature_importance": {"feature": score},
    "prediction_confidence": 0.0-1.0,
    "historical_accuracy": 0.0-1.0
  },
  
  "data_freshness": {
    "data_as_of": "ISO8601",
    "data_lag_hours": integer,
    "data_completeness": 0.0-1.0,
    "external_data_included": boolean
  },
  
  "audit_metadata": {
    "audit_id": "UUID",
    "agent_version": "1.0.0",
    "data_sources": ["source_ids"],
    "computation_time_ms": integer,
    "anomaly_flags": []
  }
}
```

### Downstream Agents That Depend On It

```
1. RECOMMENDATION_ENGINE - Uses predictions for personalized skill recommendations
2. CAREER_PATH_PLANNER_AGENT - Uses forecasts to design career pathways
3. COURSE_SUGGESTION_AGENT - Uses demand data to recommend training programs
4. JOB_MATCH_SCORING_AGENT - Uses salary forecasts and demand for match scoring
5. INSTITUTE_CURRICULUM_ADVISOR_AGENT - Uses skill gaps to advise curriculum changes
6. TRAINER_MARKETPLACE_AGENT - Uses demand forecasts to match trainers with needs
7. USER_DASHBOARD_AGENT - Displays trend insights on user dashboards
8. ANALYTICS_REPORTING_AGENT - Aggregates predictions for platform analytics
9. NOTIFICATION_AGENT - Alerts users about skill demand changes
10. EMPLOYER_INSIGHTS_AGENT - Provides hiring market intelligence to companies
11. SKILL_GAP_ALERT_AGENT - Triggers proactive skill development programs
12. PRICING_OPTIMIZATION_AGENT - Adjusts course prices based on demand
```

### Upstream Agents That Feed It

```
1. JOB_AGGREGATION_AGENT - Provides job posting data (created, filled, expired)
2. USER_BEHAVIOR_TRACKING_AGENT - Provides skill search patterns, profile updates
3. SKILL_TAXONOMY_AGENT - Provides standardized skill classifications
4. SALARY_AGGREGATION_AGENT - Provides anonymized salary offer data
5. TRAINING_PROGRAM_TRACKING_AGENT - Provides enrollment and completion data
6. PLACEMENT_SUCCESS_AGENT - Provides hiring outcome data
7. EXTERNAL_DATA_INGESTION_AGENT - Provides industry reports, labor statistics
8. PASSIVE_INTELLIGENCE_AGENT - Provides user intelligence and behavior patterns
9. FEATURE_STORE_AGENT - Provides pre-computed aggregated features
10. GEOGRAPHIC_MARKET_AGENT - Provides location-specific economic indicators
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "prediction_request": {
    "required_fields": [
      "request_id",
      "requester_user_id",
      "requester_tenant_id",
      "prediction_type",
      "target_entity",
      "forecast_horizon",
      "request_timestamp"
    ],
    
    "optional_fields": [
      "geographic_filter",
      "domain_filter",
      "experience_level_filter",
      "include_external_data",
      "confidence_threshold",
      "custom_parameters"
    ]
  },
  
  "validation_rules": [
    "request_id MUST be valid UUID",
    "requester_user_id MUST exist in User table",
    "requester_tenant_id MUST exist in Tenant table",
    "prediction_type MUST be in ['skill_demand', 'job_trend', 'salary_forecast', 'skill_gap', 'training_roi', 'hiring_demand']",
    "target_entity MUST have valid entity_type and entity_id",
    "forecast_horizon MUST be in ['3_months', '6_months', '1_year', '3_years']",
    "request_timestamp MUST be current UTC timestamp",
    "geographic_filter MUST be valid location codes if provided",
    "domain_filter MUST be in ['Arts', 'Commerce', 'Science', 'Technology', 'Administration'] if provided"
  ],
  
  "security_checks": [
    "VERIFY requester_tenant_id matches requester_user_id tenant ownership",
    "VERIFY user has permission 'market_intelligence:view' OR 'premium_features:access'",
    "VERIFY domain_filter matches user's domain access permissions",
    "VERIFY request rate limit not exceeded (100 per hour per user)",
    "VERIFY no attempts to access restricted salary data without permission",
    "VERIFY geographic_filter doesn't violate data localization rules"
  ],
  
  "domain_checks": [
    "VERIFY target_entity exists in platform taxonomy",
    "VERIFY sufficient historical data exists for prediction (min 6 months)",
    "VERIFY requested combination is statistically valid (min sample size)",
    "VERIFY no data poisoning attempts (unusual request patterns)"
  ]
}
```

**Batch Processing Input:**
```json
BATCH_PREDICTION_INPUT: {
  "batch_id": "UUID",
  "batch_type": "scheduled|on_demand",
  "predictions": [
    {
      "target_entity": {},
      "forecast_horizon": "string"
    }
  ],
  "priority": "high|medium|low",
  "callback_url": "optional webhook"
}
```

**Rejection Policy:**
- Invalid prediction_type → REJECT with INPUT_INVALID_TYPE
- Target entity not found → REJECT with ENTITY_NOT_FOUND
- Insufficient historical data → REJECT with INSUFFICIENT_DATA
- Permission denied → REJECT with SECURITY_PERMISSION_DENIED
- Rate limit exceeded → REJECT with RATE_LIMIT_EXCEEDED
- Statistical invalidity → REJECT with INVALID_SAMPLE_SIZE

**All rejections MUST be logged to audit trail.**

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "type": "MarketDemandPrediction",
    "required_fields": [
      "prediction_id",
      "prediction_type",
      "target_entity",
      "demand_forecast",
      "ml_metadata",
      "data_freshness",
      "audit_metadata"
    ],
    "optional_fields": [
      "time_series_prediction",
      "contributing_factors",
      "skill_gap_analysis",
      "salary_forecast",
      "geographic_breakdown",
      "recommendations",
      "risk_factors"
    ],
    "conditional_fields": {
      "salary_forecast": "ONLY if prediction_type='salary_forecast'",
      "skill_gap_analysis": "ONLY if prediction_type='skill_gap'",
      "training_roi": "ONLY if prediction_type='training_roi'"
    }
  },
  
  "confidence_score": {
    "range": "0.0-1.0",
    "minimum_threshold": 0.60,
    "policy": "confidence < 0.60 → flag prediction as 'low confidence', include warning"
  },
  
  "model_version": {
    "format": "semantic_version",
    "example": "1.2.3",
    "required": true,
    "immutable": true
  },
  
  "audit_reference": {
    "type": "UUID",
    "immutable": true,
    "required": true,
    "retention": "7 years"
  },
  
  "next_trigger_events": [
    "RECOMMENDATION_ENGINE_UPDATE",
    "CAREER_PATH_PLANNER_UPDATE",
    "SKILL_GAP_ALERT (if gap identified)",
    "CURRICULUM_ADVISOR_UPDATE (if institutes affected)",
    "USER_NOTIFICATION (if user subscribed to updates)",
    "ANALYTICS_DASHBOARD_UPDATE"
  ]
}
```

**Output Guarantees:**
- Every output MUST include confidence_score and clearly state confidence level
- Predictions MUST include upper and lower bounds (confidence intervals)
- Salary predictions MUST be ranges, never single point estimates
- Geographic breakdowns MUST respect data localization rules
- All predictions MUST be explainable (contributing factors provided)
- Historical accuracy metrics MUST be included for trust
- Data freshness MUST be clearly indicated
- Predictions with confidence < 0.60 MUST include prominent warnings
- All timestamps MUST be UTC ISO8601
- All currency values MUST specify currency code (INR, USD, etc.)

**Output Freshness Policy:**
```
Skill Demand Predictions: Refresh every 24 hours
Job Trend Predictions: Refresh every 12 hours (high volatility)
Salary Forecasts: Refresh every 7 days (slower moving)
Skill Gap Analysis: Refresh every 24 hours
Training ROI Predictions: Refresh every 7 days
```

---

## 5️⃣ ML / AI LOGIC LAYER

### ML-Based Components (80%)

#### Component 1: Skill Demand Forecasting Model

**MODEL_TYPE:** Time Series Forecasting + Multi-variate Regression

**ARCHITECTURE:**
```
Base Models (Ensemble):
1. Prophet (Facebook) - Handles seasonality, holidays, trend changes
2. SARIMA - Seasonal autoregressive integrated moving average
3. XGBoost Regressor - Captures non-linear relationships
4. LSTM (Long Short-Term Memory) - Deep learning for complex patterns

Ensemble Strategy: Weighted average based on historical accuracy per skill category
```

**FEATURES_USED:**
```
Time-Based Features:
- Historical demand index (rolling 7, 30, 90, 180, 360 days)
- Seasonal patterns (month, quarter, academic calendar)
- Day of week patterns (job posting cycles)
- Holiday effects
- Trend acceleration/deceleration

Skill-Specific Features:
- Skill category (technical, soft, domain-specific)
- Skill age (years since first appearance in platform)
- Skill complexity (entry-level vs advanced)
- Skill complementarity (often learned with other skills)
- Skill substitutability (alternative skills)
- Certification availability (formal credentials exist?)

Market Signals:
- Job posting volume (daily, weekly, monthly aggregates)
- Application volume per job posting
- Skill search volume (user searches)
- Course enrollment trends
- Salary growth rate
- Placement success rate
- Employer demand signals (job view counts)

External Signals (if available):
- Industry growth indicators
- Technology adoption curves
- Government policy changes
- Economic indicators (GDP growth, unemployment)
- Education system changes

Derived Features:
- Demand velocity (rate of change)
- Demand volatility (standard deviation)
- Demand momentum (sustained growth vs spike)
- Market saturation indicators
- Skill lifecycle stage (emerging|growth|mature|declining)
```

**TRAINING_FREQUENCY:** Daily for high-demand skills, Weekly for long-tail skills

**DRIFT_DETECTION:**
```
- Monitor prediction error (MAPE - Mean Absolute Percentage Error)
- Alert if MAPE increases > 20% for 3 consecutive days
- Monitor distribution shift in input features (KL divergence)
- Track model accuracy per skill category
- Detect sudden demand shocks (outlier detection)
- Retrain models when drift detected for 7 consecutive days
```

**HORIZON-SPECIFIC MODELS:**
```
3-Month Forecast: High weight on recent trends, low weight on long-term seasonality
6-Month Forecast: Balanced weights
1-Year Forecast: Higher weight on seasonality and long-term trends
3-Year Forecast: Focus on structural trends, technology adoption curves, discount short-term volatility
```

**THRESHOLD_POLICY:**
```
High Confidence (0.80-1.0): < 15% prediction error historically
Medium Confidence (0.60-0.79): 15-30% prediction error
Low Confidence (< 0.60): > 30% prediction error OR insufficient data

Low confidence predictions MUST include warnings and wider confidence intervals
```

**VERSION_CONTROL:**
- Models versioned daily with performance metrics
- Last 30 model versions retained
- Rollback to previous version if performance degrades > 10%
- A/B testing for new model architectures (10% traffic)

---

#### Component 2: Job Market Trend Analysis Model

**MODEL_TYPE:** Classification + Clustering + Anomaly Detection

**ARCHITECTURE:**
```
Trend Classification: Random Forest Classifier
- Classes: rapid_growth, steady_growth, stable, slow_decline, rapid_decline, volatile
- Features: Job posting velocity, fill rate, time-to-fill, salary trends

Job Clustering: K-Means + DBSCAN
- Group similar jobs based on skill requirements, salary, domain
- Identify emerging job clusters (new role types)

Anomaly Detection: Isolation Forest
- Detect unusual hiring patterns (sudden surge or drop)
- Identify black swan events affecting job market
```

**FEATURES_USED:**
```
Job Posting Features:
- New job postings per day (by role, domain, geography)
- Job fill rate (percentage of jobs filled)
- Time-to-fill (days from posting to acceptance)
- Application-to-offer ratio
- Job reposting frequency (unfilled jobs reposted)

Employer Behavior:
- Employer hiring frequency
- Employer size category (SME, corporate, enterprise)
- Employer industry sector
- Employer location (tier-1, tier-2, tier-3 cities)

Candidate Behavior:
- Applications per job (supply signal)
- Profile view counts
- Skill search trends
- User skill acquisition patterns

Economic Context:
- Geographic economic indicators
- Industry-specific growth rates
- Regulatory changes affecting hiring
```

**TRAINING_FREQUENCY:** Weekly

**DRIFT_DETECTION:**
- Monitor classification accuracy weekly
- Alert if new job clusters emerge (unclassified patterns)
- Detect regime changes (sudden shift in job market dynamics)

---

#### Component 3: Salary Forecasting Model

**MODEL_TYPE:** Quantile Regression + Gradient Boosting

**ARCHITECTURE:**
```
Base Model: XGBoost Quantile Regressor
- Predicts 10th, 25th, 50th (median), 75th, 90th percentiles
- Handles non-linear relationships between skills and salary

Feature Engineering: Extensive skill combination encoding
- One-hot encoding of primary skills
- Skill synergy scores (combinations worth more than sum)
- Experience multipliers
```

**FEATURES_USED:**
```
Skill Features:
- Primary skill(s) (up to 3)
- Skill proficiency level (beginner, intermediate, advanced, expert)
- Skill rarity (supply vs demand ratio)
- Skill combinations (e.g., AI + Cloud + Python)
- Certification status (formal credentials)

Experience Features:
- Years of experience (total)
- Years with specific skill
- Project portfolio size and quality
- Previous job titles
- Company types worked at (startup, SME, corporate)

Geographic Features:
- Location (city-level granularity)
- Cost of living index
- Local market competitiveness
- Remote work availability

Job Features:
- Job role category
- Company size
- Industry sector
- Job level (entry, mid, senior, leadership)

Market Context:
- Historical salary trends (3-year rolling average)
- Inflation rate
- Supply-demand ratio for skill
```

**PRIVACY PROTECTION:**
```
CRITICAL: Salary data handling:
- All salary data anonymized (no individual identifiable)
- Minimum 10 data points required for any prediction
- Geographic masking (no predictions for small towns with < 50 data points)
- Outlier removal (top/bottom 1% excluded)
- Differential privacy noise added to protect individuals
- Salary data encrypted at rest
- Access restricted to authorized roles only
```

**TRAINING_FREQUENCY:** Bi-weekly

**DRIFT_DETECTION:**
- Monitor salary inflation rates
- Alert if predicted salaries deviate > 20% from recent offers
- Track geographic salary parity changes

---

#### Component 4: Skill Gap Analysis Model

**MODEL_TYPE:** Supply-Demand Matching + Optimization

**ARCHITECTURE:**
```
Supply Model: Count current skilled users (by skill, proficiency, location)
Demand Model: Aggregate employer demand signals (job postings, hiring intent)
Gap Calculator: Demand - Supply = Gap
Urgency Scorer: ML model predicting time-to-critical based on gap growth rate
```

**FEATURES_USED:**
```
Supply Side:
- User profiles with verified skills
- Training program completion rates
- Skill acquisition velocity (users learning skill)
- Graduation rates (students entering job market)
- Geographic distribution of talent

Demand Side:
- Open job requisitions
- Employer hiring plans (if disclosed)
- Job posting growth rate
- Application-to-offer ratios (tight market indicator)
- Salary increases (demand pressure indicator)

Gap Dynamics:
- Historical gap trends
- Gap closing velocity (training pipeline)
- Migration patterns (talent mobility)
```

**OUTPUT:**
```
Gap Categories:
- Critical (gap > 80%, time-to-crisis < 3 months)
- High (gap 50-80%, time-to-crisis 3-6 months)
- Medium (gap 20-50%, time-to-crisis 6-12 months)
- Low (gap < 20% OR surplus supply)
```

**TRAINING_FREQUENCY:** Daily (high priority for platform)

---

#### Component 5: Training ROI Prediction Model

**MODEL_TYPE:** Binary Classification + Expected Value Calculation

**ARCHITECTURE:**
```
Success Probability Model: Random Forest Classifier
- Predicts probability of successful placement after training
- Classes: placed_within_6_months, not_placed_6_months

ROI Calculator: Expected value model
- ROI = (Placement_Probability × Salary_Gain) - Training_Cost
```

**FEATURES_USED:**
```
Training Program Features:
- Program duration (hours, weeks)
- Program cost
- Curriculum quality score
- Trainer reputation score
- Completion rate (historical)
- Placement rate (historical)
- Skill relevance (demand index)

Learner Features:
- Prior education level
- Prior work experience
- Current skills (baseline)
- Learning velocity (from passive intelligence)
- Domain expertise
- Intelligence profile (if available)

Market Context:
- Current demand for training outcome skill
- Forecasted demand (6-12 months ahead)
- Supply-demand gap
- Salary expectations
```

**TRAINING_FREQUENCY:** Monthly

---

#### Component 6: Obsolescence Warning Model

**MODEL_TYPE:** Survival Analysis + Trend Detection

**ARCHITECTURE:**
```
Survival Model: Cox Proportional Hazards
- Predicts "time until skill becomes obsolete"
- Hazard factors: Technology disruption, automation, regulatory changes

Trend Detector: Change Point Detection
- Identifies inflection points in demand curves (peak → decline)
```

**FEATURES_USED:**
```
Skill Lifecycle Indicators:
- Demand trajectory (rising, peaking, declining)
- Job posting mentions (decreasing frequency)
- Salary trends (stagnating or declining)
- Replacement technology emergence
- Automation susceptibility score

Technology Disruption Signals:
- New technology adoption rates
- Industry transformation indicators
- Regulatory changes affecting skill
```

**OUTPUT:**
```
Obsolescence Risk:
- High Risk: Skill likely obsolete within 1-2 years
- Medium Risk: Skill declining, 3-5 year horizon
- Low Risk: Skill stable or growing
- Future-Proof: Skill demand accelerating
```

**TRAINING_FREQUENCY:** Quarterly

---

### AI-Based Components (20%)

#### Component 7: Semantic Job-Skill Matching (LLM)

**AI_USAGE_SCOPE:** Understanding unstructured job descriptions and skill descriptions

**FUNCTION:**
- Extract implied skills from job descriptions (not explicitly mentioned)
- Understand skill synonyms and variations
- Map informal skill names to taxonomy
- Identify emerging skills from new job postings

**PROMPT_GOVERNANCE:**
```
VERSION: 1.0.0
STRUCTURE: Deterministic extraction template
CREATIVITY: DISABLED (Temperature = 0.0)

Template:
---
Extract skills required from this job description.
Return standardized skill names from our taxonomy.
If skill not in taxonomy, mark as [EMERGING_SKILL].

Job Description: {job_description}

Output format (JSON only, no explanation):
{
  "required_skills": ["skill1", "skill2"],
  "preferred_skills": ["skill3"],
  "emerging_skills": ["[EMERGING_SKILL] skill_name"],
  "experience_level": "entry|mid|senior|expert",
  "domain": "Technology|Commerce|etc"
}
---
```

**CONSTRAINTS:**
- NO subjective interpretation of skill importance
- NO salary estimation (handled by ML model)
- NO career advice generation
- Output MUST map to existing skill taxonomy
- Timeout: 3 seconds max
- Fallback: Rule-based keyword extraction

**MODEL:** Claude Sonnet 4.5 (API)

---

#### Component 8: Trend Narrative Generation (LLM)

**AI_USAGE_SCOPE:** Generate human-readable explanations of predictions

**FUNCTION:**
- Convert ML predictions into natural language insights
- Explain contributing factors in accessible language
- Generate recommendations tailored to user type
- Create executive summaries for reports

**PROMPT_GOVERNANCE:**
```
VERSION: 1.0.0

Template:
---
Generate a concise explanation of this market prediction for a {user_type}.

Prediction Data:
- Skill: {skill_name}
- Current Demand Index: {current_demand}
- Predicted Demand (6 months): {predicted_demand}
- Trend: {trend_direction}
- Confidence: {confidence_score}
- Top Contributing Factors: {factors}

Output requirements:
- 3-5 sentences maximum
- Professional tone
- Actionable insights for {user_type}
- No jargon unless user_type = "data_analyst"
- Include confidence caveat if confidence < 0.80

User Type: {user_type} (student|trainer|institute|company|analyst)

Output plain text, no JSON.
---
```

**HUMAN OVERRIDE:**
- All AI-generated narratives logged for review
- Controversial predictions (major market shifts) require human approval before publishing
- Sentiment analysis on generated text (must be neutral, not alarmist)

---

## 6️⃣ SCALABILITY DESIGN

**Target Performance:**
```
EXPECTED_RPS = 100-500 prediction requests/second
BATCH_PROCESSING = 10,000 predictions per batch job (nightly)
LATENCY_TARGET = <3 seconds for single prediction (p95)
LATENCY_TARGET_BATCH = <30 minutes for 10,000 predictions
MAX_CONCURRENCY = 5,000 concurrent requests
QUEUE_STRATEGY = Priority queue (premium users, scheduled jobs, on-demand)
```

**Horizontal Scaling:**
- Stateless prediction service (no session state)
- Worker pool architecture (Kubernetes pods)
- Auto-scaling: CPU > 75% → scale up (max 50 pods)
- Auto-scaling: CPU < 30% for 10min → scale down (min 5 pods)
- Model inference servers separate from API servers
- Prediction results cached in Redis (1 hour TTL for frequent queries)

**Event-Driven Triggers:**
```
Trigger 1: USER_DASHBOARD_LOAD
- User views career insights page
- Generate personalized demand predictions (top 5 skills for user)
- Priority: MEDIUM

Trigger 2: SCHEDULED_BATCH_PREDICTION
- Every 24 hours at 2 AM UTC
- Refresh predictions for top 1000 skills
- Priority: HIGH

Trigger 3: JOB_POSTING_SURGE_DETECTED
- Real-time detection of sudden demand spike
- Trigger immediate re-prediction for affected skills
- Priority: URGENT

Trigger 4: CURRICULUM_DESIGN_REQUEST
- Institute requests skill gap analysis for curriculum planning
- Generate comprehensive multi-skill predictions
- Priority: HIGH

Trigger 5: API_REQUEST
- External API call from partner systems
- On-demand prediction generation
- Priority: STANDARD
```

**Async Processing:**
```
User Experience:
1. User requests prediction
2. API returns: "Prediction ID: xyz, Status: Processing, ETA: 3 seconds"
3. User polls status endpoint OR receives webhook callback
4. Prediction completes, user retrieves result

Benefits:
- Non-blocking UI
- Better resource utilization
- Support for long-running batch predictions
```

**Idempotent Operations:**
```
- Same prediction request (skill + horizon + date) within 1 hour → return cached result
- Idempotency key: SHA256(skill_id + horizon + date_trunc_to_hour)
- Cache invalidation: New job posting data > 10% change → invalidate cache
```

**Database Optimization:**
```
Read Optimization:
- Time-series database (InfluxDB or TimescaleDB) for historical demand data
- Read replicas for analytics queries
- Materialized views for aggregated metrics (refreshed hourly)
- Partitioning by date range (query only relevant time windows)

Write Optimization:
- Batch inserts for prediction results (every 1000 predictions)
- Async write to audit log (via message queue)
- Streaming inserts for real-time demand signals (Kafka)

Caching Strategy:
- L1 Cache: In-memory (recent predictions, 1 hour TTL)
- L2 Cache: Redis (frequent queries, 24 hour TTL)
- L3 Cache: CDN (public aggregated reports, 7 day TTL)
```

**Model Serving Optimization:**
```
Model Loading:
- Models loaded in memory at service startup
- Lazy loading for long-tail predictions (load on first use)
- Model warm-up phase before accepting traffic

Inference Optimization:
- Batch inference (process 100 requests together)
- GPU acceleration for LSTM models
- Model quantization for faster inference
- Model distillation for lower latency (simplified models for routine predictions)
```

---

## 7️⃣ SECURITY ENFORCEMENT

**Tenant Isolation Validation:**
```python
def validate_prediction_request(user_id, tenant_id, prediction_request):
    # Verify user belongs to tenant
    assert user.tenant_id == tenant_id
    
    # Verify aggregated data only (no raw cross-tenant data exposure)
    # Market predictions use aggregated anonymized data across all tenants
    # Individual tenant predictions use only that tenant's data
    
    # If prediction includes salary data:
    if prediction_request.includes_salary:
        # Verify user has permission to view salary data
        assert user.has_permission('salary_insights:view')
        
        # Verify sufficient anonymization (min 10 samples)
        assert sample_size >= 10
    
    # Violation → STOP + LOG + ALERT_SECURITY_TEAM
```

**Domain Isolation Validation:**
```python
def validate_domain_access(user_id, domain_filter, prediction_request):
    # Verify user has access to requested domain
    if domain_filter:
        assert user.has_domain_access(domain_filter)
    
    # Verify no cross-domain data leakage in predictions
    assert prediction_data.domain in user.accessible_domains
    
    # Violation → STOP + LOG
```

**Role-Based Authorization:**
```
REQUIRED_PERMISSIONS:
- market_intelligence:view (basic predictions)
- premium_features:access (advanced forecasts, salary data)
- analyst_tools:access (raw data exports, custom models)

RESTRICTED_PERMISSIONS:
- salary_insights:view (salary forecast access)
- hiring_intelligence:view (employer hiring plans)
- competitive_intelligence:view (cross-company trends)
```

**Data Privacy Enforcement:**
```
Salary Data Privacy:
- Minimum aggregation: 10 data points
- Geographic masking: Cities with < 50 samples excluded
- Outlier removal: Top/bottom 1% removed
- Differential privacy: Random noise added (ε-differential privacy)
- Temporal aggregation: At least 30-day windows
- No individual salary disclosure under any circumstance

User Behavior Privacy:
- Skill searches aggregated (no individual tracking exposed)
- Job applications anonymized
- User profiles de-identified in aggregations
```

**Anti-Discrimination Safeguards:**
```
Predictions MUST NOT:
- Consider protected attributes (gender, race, religion, caste)
- Show different results based on user demographics
- Discriminate in salary predictions based on personal attributes
- Reinforce historical biases

Fairness Testing:
- Weekly audits for prediction bias across demographic groups
- Parity checks (similar users get similar predictions)
- Disparate impact analysis (no group systematically disadvantaged)
```

**Encryption Enforcement:**
- All prediction data encrypted at rest (AES-256)
- API communication over TLS 1.3
- Sensitive fields encrypted in logs (user_id, tenant_id, salary values)

**Audit Logging (Append-Only):**
```json
{
  "log_id": "UUID",
  "timestamp_utc": "ISO8601",
  "actor_type": "USER|SYSTEM|AGENT",
  "actor_id": "user_id OR agent_name",
  "action": "PREDICTION_REQUESTED",
  "prediction_id": "UUID",
  "prediction_type": "skill_demand|job_trend|salary_forecast",
  "target_entity": "skill_name OR job_role",
  "user_id": "ENCRYPTED",
  "tenant_id": "ENCRYPTED",
  "input_hash": "SHA256(input)",
  "output_hash": "SHA256(output)",
  "model_version": "1.2.3",
  "confidence_score": 0.85,
  "processing_time_ms": 2345,
  "data_sources": ["jobs_table", "skills_table", "salary_table"],
  "anomaly_flags": []
}
```

---

## 8️⃣ AUDIT & TRACEABILITY

**Immutable Audit Log Schema:**
```json
{
  "audit_id": "UUID (primary key)",
  "created_at": "TIMESTAMP (immutable)",
  "agent_name": "MARKET_DEMAND_PREDICTION_AGENT",
  "agent_version": "1.0.0",
  "event_type": "PREDICTION_GENERATED",
  
  "request_details": {
    "request_id": "UUID",
    "requester_user_id": "UUID",
    "requester_tenant_id": "UUID",
    "prediction_type": "string",
    "target_entity": {},
    "forecast_horizon": "string",
    "geographic_filter": "optional",
    "domain_filter": "optional"
  },
  
  "prediction_output": {
    "prediction_id": "UUID",
    "demand_index_current": float,
    "demand_index_predicted": float,
    "confidence_score": float,
    "output_hash": "SHA256"
  },
  
  "ml_execution": {
    "models_used": ["prophet_v1.2", "xgboost_v2.3"],
    "model_versions": ["1.2.0", "2.3.1"],
    "feature_count": integer,
    "training_date": "ISO8601",
    "historical_accuracy": 0.87,
    "prediction_confidence": 0.85,
    "inference_time_ms": 1234
  },
  
  "data_lineage": {
    "data_sources": [
      "jobs_table:2026-01-01_to_2026-02-25",
      "skills_aggregation:v3.2.0",
      "salary_index:february_2026"
    ],
    "data_freshness_hours": 2,
    "data_completeness": 0.98,
    "external_data_used": false
  },
  
  "decision_factors": [
    {"factor": "job_posting_growth", "weight": 0.35, "direction": "positive"},
    {"factor": "skill_search_trends", "weight": 0.25, "direction": "positive"},
    {"factor": "seasonal_adjustment", "weight": 0.15, "direction": "negative"}
  ],
  
  "quality_checks": {
    "confidence_threshold_met": true,
    "data_sufficiency_check": "passed",
    "bias_audit_check": "passed",
    "outlier_detection": "none"
  },
  
  "anomaly_flags": [
    {
      "type": "UNUSUAL_DEMAND_SPIKE",
      "severity": "medium",
      "description": "Demand increased 150% in last 7 days (possible data anomaly)",
      "confidence": 0.70,
      "action_taken": "Widened confidence interval"
    }
  ]
}
```

**Prediction Explainability Log:**
```json
{
  "explainability_id": "UUID",
  "prediction_id": "UUID",
  "explanation_method": "SHAP|LIME|Feature_Importance",
  
  "feature_contributions": [
    {
      "feature_name": "job_posting_velocity",
      "contribution_score": +0.12,
      "human_readable": "Recent surge in job postings increased demand by 12%"
    },
    {
      "feature_name": "seasonal_factor",
      "contribution_score": -0.05,
      "human_readable": "Seasonal hiring slowdown reduced demand by 5%"
    }
  ],
  
  "counterfactual_analysis": {
    "if_job_postings_flat": "demand_index would be 72 instead of 85",
    "if_no_external_data": "confidence would drop to 0.65"
  }
}
```

**Model Performance Tracking Log:**
```json
{
  "performance_log_id": "UUID",
  "model_name": "skill_demand_forecaster",
  "model_version": "1.2.0",
  "evaluation_date": "ISO8601",
  "evaluation_period": "2025-12-01_to_2026-02-25",
  
  "accuracy_metrics": {
    "MAPE": 12.5,  // Mean Absolute Percentage Error
    "RMSE": 8.3,   // Root Mean Squared Error
    "MAE": 6.7,    // Mean Absolute Error
    "R_squared": 0.89
  },
  
  "accuracy_by_category": {
    "technical_skills": {"MAPE": 10.2, "R_squared": 0.92},
    "soft_skills": {"MAPE": 18.7, "R_squared": 0.78},
    "domain_specific": {"MAPE": 14.1, "R_squared": 0.85}
  },
  
  "accuracy_by_horizon": {
    "3_months": 0.91,
    "6_months": 0.87,
    "1_year": 0.79,
    "3_years": 0.62
  },
  
  "drift_indicators": {
    "feature_distribution_shift": 0.08,  // KL divergence
    "prediction_bias": -0.03,  // Systematic over/under prediction
    "volatility_change": 0.12   // Increased uncertainty
  },
  
  "retraining_recommendation": "RETRAIN_SOON (drift detected for 5 days)"
}
```

**Logs MUST be:**
- Append-only (no updates or deletes)
- Stored in separate audit database (not production DB)
- Replicated to 3 separate storage regions
- Backed up daily
- Retained for 7 years (regulatory compliance)
- Queryable for performance analysis and debugging

---

## 9️⃣ FAILURE POLICY

**Invalid Input Handling:**
```
DETECTION: Missing required field OR invalid forecast_horizon
ACTION: STOP_EXECUTION
RESPONSE: {
  "status": "error",
  "error_code": "INPUT_INVALID",
  "message": "forecast_horizon must be one of: 3_months, 6_months, 1_year, 3_years",
  "request_id": "UUID"
}
LOG: LOG_INCIDENT with severity=ERROR
ESCALATE: NO (user error)
RETRY: User should correct input and retry
```

**Insufficient Historical Data:**
```
DETECTION: Target skill has < 6 months of data OR < 100 data points
ACTION: STOP_EXECUTION (do NOT make unreliable prediction)
RESPONSE: {
  "status": "error",
  "error_code": "INSUFFICIENT_DATA",
  "message": "Skill '{skill_name}' has insufficient historical data for reliable prediction. Need min 6 months or 100 data points. Current: 3 months, 45 data points.",
  "suggestion": "Check back in 3 months or try a related skill",
  "related_skills": ["skill_a", "skill_b"]
}
LOG: LOG_INCIDENT with severity=INFO
ESCALATE: NO
RETRY: NO (wait for more data)
```

**Model Unavailable:**
```
DETECTION: ML model service timeout or error
ACTION: FALLBACK to baseline prediction (historical average)
RESPONSE: {
  "status": "success",
  "prediction_id": "UUID",
  "demand_forecast": {
    "predicted_demand_index": 75,
    "demand_trend": "stable"
  },
  "warning": "Using historical baseline due to model unavailability. Confidence reduced.",
  "confidence_score": 0.50,
  "model_used": "historical_average_fallback"
}
LOG: LOG_INCIDENT with severity=CRITICAL
ESCALATE: DevOps team (PagerDuty) - model service down
RETRY: System auto-retries model service
```

**AI Timeout (LLM):**
```
DETECTION: LLM API call exceeds 3 seconds
ACTION: FALLBACK to rule-based extraction OR skip narrative generation
RESPONSE: Continue with prediction, skip or simplify narrative
LOG: LOG_INCIDENT with severity=WARNING
ESCALATE: If frequency > 15% → escalate to AI team
RETRY: NO (fallback successful)
```

**Data Corruption Detected:**
```
DETECTION: Anomalous data patterns (e.g., salary = $0, demand_index = 10000)
ACTION: STOP_EXECUTION + FLAG_DATA_ISSUE
RESPONSE: {
  "status": "error",
  "error_code": "DATA_CORRUPTION_DETECTED",
  "message": "Anomalous data detected. Prediction cannot be generated safely.",
  "anomaly_details": "Salary values out of expected range"
}
LOG: LOG_SECURITY_INCIDENT with severity=CRITICAL
ESCALATE: Data integrity team (immediate) + Security team
RETRY: After data cleanup
```

**Low Confidence Prediction:**
```
DETECTION: confidence_score < 0.60
ACTION: GENERATE_PREDICTION_WITH_STRONG_WARNING
RESPONSE: {
  "status": "success",
  "prediction_id": "UUID",
  "demand_forecast": {...},
  "confidence_score": 0.55,
  "confidence_level": "LOW",
  "warning": "⚠️ LOW CONFIDENCE: This prediction has high uncertainty. Use with caution. Consider checking back in 2 weeks for updated prediction.",
  "reasons_for_low_confidence": [
    "Limited historical data (< 12 months)",
    "High market volatility detected",
    "Conflicting signals from different data sources"
  ]
}
LOG: LOG_INCIDENT with severity=INFO
ESCALATE: NO (expected for emerging skills)
RETRY: NO (prediction delivered with appropriate caveats)
```

**Extreme Prediction Detection:**
```
DETECTION: Predicted change > 200% OR < -80% (unrealistic)
ACTION: CAP_PREDICTION + FLAG_FOR_REVIEW
RESPONSE: {
  "prediction_id": "UUID",
  "demand_forecast": {
    "predicted_demand_index": 150,  // capped at +200%
    "uncapped_prediction": 350,     // original unrealistic prediction
    "warning": "Extreme prediction detected and capped. This skill may be experiencing unprecedented growth. Human review recommended."
  },
  "confidence_score": 0.40,
  "requires_human_review": true
}
LOG: LOG_ANOMALY with severity=HIGH
ESCALATE: Market intelligence analyst for review
RETRY: NO (prediction delivered with caps and warnings)
```

**No Silent Failures Policy:**
- Every failure MUST be logged with context
- User MUST receive clear error message OR warning
- Critical failures MUST trigger alerts
- Degraded service (fallbacks) MUST be transparent to user
- Partial results MUST include warnings about limitations

---

## 🔟 INTER-AGENT DEPENDENCY MAP

### Upstream Agents (Input Providers)

```
AGENT: JOB_AGGREGATION_AGENT
PROVIDES: Job posting data (created, filled, expired, time-to-fill)
FREQUENCY: Real-time streaming + Daily aggregates
FALLBACK: Use cached data from last 24 hours
DEPENDENCY_TYPE: CRITICAL (cannot predict without job data)
```

```
AGENT: SKILL_TAXONOMY_AGENT
PROVIDES: Standardized skill classifications, skill relationships
FREQUENCY: On-demand API calls
FALLBACK: Use last known taxonomy version
DEPENDENCY_TYPE: MEDIUM (can proceed with stale taxonomy)
```

```
AGENT: SALARY_AGGREGATION_AGENT
PROVIDES: Anonymized salary offer data
FREQUENCY: Daily aggregates
FALLBACK: Skip salary predictions if unavailable
DEPENDENCY_TYPE: SOFT (only needed for salary forecasts)
```

```
AGENT: USER_BEHAVIOR_TRACKING_AGENT
PROVIDES: Skill search patterns, profile updates, learning activity
FREQUENCY: Real-time streaming
FALLBACK: Use historical behavior patterns
DEPENDENCY_TYPE: MEDIUM (improves accuracy but not essential)
```

```
AGENT: TRAINING_PROGRAM_TRACKING_AGENT
PROVIDES: Course enrollments, completions, trainer ratings
FREQUENCY: Daily aggregates
FALLBACK: Skip training ROI predictions if unavailable
DEPENDENCY_TYPE: SOFT (only needed for specific prediction types)
```

```
AGENT: PLACEMENT_SUCCESS_AGENT
PROVIDES: Hiring outcomes (time-to-placement, success rates)
FREQUENCY: Weekly aggregates
FALLBACK: Use historical placement rates
DEPENDENCY_TYPE: MEDIUM (validates demand predictions)
```

```
AGENT: EXTERNAL_DATA_INGESTION_AGENT
PROVIDES: Industry reports, government labor statistics
FREQUENCY: Weekly/Monthly
FALLBACK: Proceed without external data (reduced confidence)
DEPENDENCY_TYPE: SOFT (nice to have, not required)
```

```
AGENT: FEATURE_STORE_AGENT
PROVIDES: Pre-computed aggregated features (demand velocity, trend momentum)
FREQUENCY: Hourly refresh
FALLBACK: Compute features on-demand (slower)
DEPENDENCY_TYPE: MEDIUM (impacts performance, not accuracy)
```

```
AGENT: PASSIVE_INTELLIGENCE_AGENT
PROVIDES: User intelligence profiles, learning patterns
FREQUENCY: Daily aggregates
FALLBACK: Skip personalized recommendations
DEPENDENCY_TYPE: SOFT (enhances recommendations only)
```

```
AGENT: GEOGRAPHIC_MARKET_AGENT
PROVIDES: Location-specific economic indicators, cost of living
FREQUENCY: Monthly updates
FALLBACK: Use national averages
DEPENDENCY_TYPE: MEDIUM (needed for geographic predictions)
```

### Downstream Agents (Output Consumers)

```
AGENT: RECOMMENDATION_ENGINE
CONSUMES: demand_forecast, skill_gap_analysis
EVENT: RECOMMENDATION_UPDATE_REQUIRED
TRIGGER: On every high-confidence prediction (confidence > 0.75)
CRITICAL: HIGH (drives user engagement)
```

```
AGENT: CAREER_PATH_PLANNER_AGENT
CONSUMES: time_series_prediction, obsolescence_warnings, salary_forecast
EVENT: CAREER_PATH_RECALCULATION_REQUIRED
TRIGGER: On significant demand changes (> 20% shift)
CRITICAL: HIGH (long-term user decisions)
```

```
AGENT: COURSE_SUGGESTION_AGENT
CONSUMES: skill_gap_analysis, demand_forecast, training_roi predictions
EVENT: COURSE_CATALOG_PRIORITY_UPDATE
TRIGGER: Daily batch
CRITICAL: MEDIUM (impacts course visibility)
```

```
AGENT: JOB_MATCH_SCORING_AGENT
CONSUMES: demand_forecast, salary_forecast
EVENT: JOB_MATCH_SCORE_REFRESH
TRIGGER: Real-time on job matching
CRITICAL: HIGH (affects job recommendations)
```

```
AGENT: INSTITUTE_CURRICULUM_ADVISOR_AGENT
CONSUMES: skill_gap_analysis, demand_forecast (3-year horizon)
EVENT: CURRICULUM_REVIEW_ALERT
TRIGGER: When critical skill gaps identified
CRITICAL: HIGH (institutional decisions, high stakes)
```

```
AGENT: TRAINER_MARKETPLACE_AGENT
CONSUMES: demand_forecast, geographic_breakdown, training_roi predictions
EVENT: TRAINER_DEMAND_SIGNAL_UPDATE
TRIGGER: Daily batch
CRITICAL: MEDIUM (trainer supply optimization)
```

```
AGENT: USER_DASHBOARD_AGENT
CONSUMES: Full prediction output (all fields)
EVENT: DASHBOARD_WIDGET_UPDATE
TRIGGER: On user dashboard load (on-demand)
CRITICAL: HIGH (primary user interface)
```

```
AGENT: ANALYTICS_REPORTING_AGENT
CONSUMES: Aggregated predictions, model performance metrics
EVENT: ANALYTICS_DATASET_UPDATE
TRIGGER: Daily batch
CRITICAL: MEDIUM (business intelligence)
```

```
AGENT: NOTIFICATION_AGENT
CONSUMES: demand_forecast (significant changes), obsolescence_warnings
EVENT: USER_NOTIFICATION_TRIGGER
TRIGGER: When demand shifts > 30% OR obsolescence risk elevated
CRITICAL: MEDIUM (user engagement)
```

```
AGENT: EMPLOYER_INSIGHTS_AGENT
CONSUMES: hiring_demand predictions, salary_forecast, skill_gap_analysis
EVENT: EMPLOYER_INTELLIGENCE_UPDATE
TRIGGER: Weekly batch OR on-demand
CRITICAL: MEDIUM (B2B value proposition)
```

```
AGENT: SKILL_GAP_ALERT_AGENT
CONSUMES: skill_gap_analysis (critical gaps only)
EVENT: CRITICAL_SKILL_GAP_ALERT
TRIGGER: When gap urgency = "critical"
CRITICAL: HIGH (proactive intervention required)
```

```
AGENT: PRICING_OPTIMIZATION_AGENT
CONSUMES: demand_forecast, training_roi predictions
EVENT: PRICING_ADJUSTMENT_RECOMMENDATION
TRIGGER: Monthly batch
CRITICAL: LOW (pricing is strategic decision)
```

### Event Emission Structure

```json
EVENT_SCHEMA: {
  "event_type": "MARKET_PREDICTION_GENERATED",
  "event_id": "UUID",
  "timestamp_utc": "ISO8601",
  "source_agent": "MARKET_DEMAND_PREDICTION_AGENT",
  "source_version": "1.0.0",
  
  "payload": {
    "prediction_id": "UUID",
    "prediction_type": "skill_demand|job_trend|salary_forecast|skill_gap",
    "target_entity": {
      "entity_type": "skill|job_role|domain",
      "entity_id": "string",
      "entity_name": "string"
    },
    "forecast_horizon": "3_months|6_months|1_year|3_years",
    "demand_index_current": 0-100,
    "demand_index_predicted": 0-100,
    "demand_trend": "rising|stable|declining",
    "confidence_score": 0.0-1.0,
    "urgency_level": "critical|high|medium|low" (if skill_gap),
    "geographic_scope": ["locations"],
    "data_freshness_hours": integer
  },
  
  "downstream_triggers": [
    "RECOMMENDATION_UPDATE_REQUIRED",
    "CAREER_PATH_RECALCULATION_REQUIRED",
    "SKILL_GAP_ALERT (if critical)",
    "CURRICULUM_REVIEW_ALERT (if institute impacted)"
  ],
  
  "metadata": {
    "model_version": "1.2.0",
    "data_sources": ["jobs", "skills", "salaries"],
    "prediction_freshness": "real_time|scheduled|cached"
  }
}
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

**Feature Emission to Feature Store:**

This agent emits structured features that enhance passive intelligence measurement.

```python
EMIT_FEATURE_VECTOR:
{
  "feature_store_event": "MARKET_BEHAVIOR_FEATURE_CREATED",
  "source_agent": "MARKET_DEMAND_PREDICTION_AGENT",
  "user_id": "UUID",  // if user-specific analysis
  "aggregate_id": "skill_id OR domain_id",  // if aggregate analysis
  "timestamp_utc": "ISO8601",
  
  "market_intelligence_features": {
    "user_skill_market_alignment_score": 0.0-1.0,
    // How well user's skills match market demand
    
    "user_future_readiness_score": 0.0-1.0,
    // User's preparedness for future market (skills in growth areas)
    
    "user_obsolescence_risk_score": 0.0-1.0,
    // Risk of user's skills becoming obsolete
    
    "user_earning_potential_index": 0-100,
    // Predicted earning potential based on skill portfolio
    
    "skill_diversification_score": 0.0-1.0,
    // User has skills across multiple domains (risk mitigation)
    
    "market_timing_score": 0.0-1.0,
    // User learning skills at optimal time (just before demand surge)
  },
  
  "behavioral_intelligence_signals": {
    "trend_awareness": 0.0-1.0,
    // User searches for trending skills (market awareness)
    
    "adaptability_indicator": 0.0-1.0,
    // User pivots to growing areas when decline detected
    
    "strategic_learning_pattern": 0.0-1.0,
    // User follows logical skill progression paths
    
    "contrarian_indicator": 0.0-1.0,
    // User invests in emerging (not yet popular) skills (high risk/reward)
  },
  
  "career_intelligence_indicators": {
    "career_velocity": float,
    // Rate of skill acquisition vs market demand growth
    
    "career_trajectory_score": 0.0-1.0,
    // Quality of career path based on market predictions
    
    "income_growth_potential": "percentage",
    // Predicted income growth based on skill portfolio and market trends
  }
}
```

**Integration with User Profiles:**
```
These features feed back into user intelligence profiles, enabling:
- Personalized market intelligence dashboards
- Proactive career guidance notifications
- Gamification (badges for "Trend Spotter", "Future Ready", etc.)
- Enhanced job matching (market-aware scoring)
```

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

**While this agent doesn't directly handle "ideas," it supports the innovation economy by:**

**Skill Innovation Tracking:**
```python
EMIT_SKILL_INNOVATION_SIGNAL:
{
  "signal_type": "EMERGING_SKILL_DETECTED",
  "skill_name": "string",
  "skill_id": "UUID (new)",
  "detection_date": "ISO8601",
  "detection_source": "job_postings|course_enrollments|external_reports",
  
  "innovation_characteristics": {
    "novelty_score": 0.0-1.0,  // How new is this skill?
    "adoption_velocity": float,  // How fast is it being adopted?
    "demand_trajectory": "emerging|rapid_growth|mature",
    "estimated_market_size": integer  // Projected job openings
  },
  
  "skill_genealogy": {
    "parent_skills": ["skill_a", "skill_b"],  // Evolved from these skills
    "related_technologies": ["tech_a", "tech_b"],
    "innovation_type": "incremental|disruptive|radical"
  }
}
```

**This feeds into:**
- SKILL_TAXONOMY_AGENT (update taxonomy with new skills)
- RECOMMENDATION_ENGINE (suggest emerging skills to early adopters)
- INSTITUTE_CURRICULUM_ADVISOR_AGENT (alert about new skill requirements)
- TRAINER_MARKETPLACE_AGENT (create demand for trainers in new skill)

**Innovation ROI Predictions:**
```python
INNOVATION_ROI_FORECAST:
{
  "skill_id": "UUID",
  "skill_name": "string",
  "innovation_stage": "emerging|early_growth|mainstream|mature|declining",
  
  "early_adopter_advantage": {
    "salary_premium_percentage": float,  // e.g., +30% for early adopters
    "competitive_advantage_window": "months",  // Before skill becomes common
    "risk_score": 0.0-1.0  // Risk of investing in unproven skill
  },
  
  "innovation_lifecycle_prediction": {
    "peak_demand_date": "ISO8601",
    "plateau_date": "ISO8601",
    "obsolescence_date": "ISO8601 (optional)",
    "total_lifecycle_years": float
  }
}
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

**Rank Update Events:**

```python
TRIGGER_RANK_UPDATE:
{
  "event": "USER_MARKET_INTELLIGENCE_RANK_UPDATE",
  "user_id": "UUID",
  "reason": "high_market_alignment|future_ready_skills|trend_awareness",
  
  "rank_impact": {
    "market_intelligence_rank": +10 points,
    // User's skills highly aligned with market demand
    
    "future_readiness_rank": +15 points,
    // User has skills in growth areas (3-year forecast)
    
    "career_strategy_rank": +5 points,
    // User follows market-aware learning path
  },
  
  "triggered_by": {
    "prediction_id": "UUID",
    "user_skill_alignment_score": 0.92,
    "future_readiness_score": 0.87
  }
}
```

**Achievement/Badge Triggers:**

```python
TRIGGER_ACHIEVEMENT:
{
  "event": "USER_ACHIEVEMENT_UNLOCKED",
  "user_id": "UUID",
  "achievement_type": "MARKET_INTELLIGENCE_BADGE",
  "badge_tier": "bronze|silver|gold|platinum",
  
  "badge_categories": {
    "TREND_SPOTTER": "User learned skill 6 months before demand surge",
    "FUTURE_READY": "80%+ of user's skills in growth areas",
    "MARKET_MAVERICK": "User invested in 3+ emerging skills early",
    "DIVERSIFICATION_PRO": "Skills across 5+ domains with growth trajectory",
    "HIGH_EARNER_PATH": "Skill portfolio predicts top 10% salary trajectory"
  },
  
  "share_worthy": true,
  "reward": {
    "badge_icon": "url",
    "xp_bonus": 150,
    "platform_visibility_boost": true,
    "unlocks_premium_insights": true
  }
}
```

**Notification Triggers:**

```python
TRIGGER_PROACTIVE_NOTIFICATION:
{
  "event": "MARKET_INTELLIGENCE_ALERT",
  "user_id": "UUID",
  "notification_type": "opportunity|warning|insight",
  "urgency": "high|medium|low",
  
  "notification_templates": {
    "DEMAND_SURGE_ALERT": {
      "message": "🚀 Your skill '{skill_name}' is trending! Demand increased 65% this month. Update your profile to attract employers.",
      "cta": "Update Profile"
    },
    
    "OBSOLESCENCE_WARNING": {
      "message": "⚠️ Your skill '{skill_name}' is declining. Consider adding complementary skills: {suggested_skills}.",
      "cta": "Explore Learning Paths"
    },
    
    "EARLY_OPPORTUNITY": {
      "message": "💡 Emerging skill '{skill_name}' detected! Early adopters earn 30% more. Start learning now.",
      "cta": "View Courses"
    },
    
    "SALARY_GROWTH_INSIGHT": {
      "message": "📈 Good news! Your skill combination predicts 20% salary growth in next 12 months. Market demand is strong.",
      "cta": "View Market Report"
    },
    
    "SKILL_GAP_OPPORTUNITY": {
      "message": "🎯 Critical skill gap in your domain: '{skill_name}'. High demand, low supply. Great career opportunity!",
      "cta": "Learn This Skill"
    }
  }
}
```

**Gamification Integration:**

```python
GAMIFICATION_HOOKS:
{
  "leaderboards": [
    "Most Future-Ready Users (by future_readiness_score)",
    "Best Market Alignment (by skill_market_alignment_score)",
    "Top Trend Spotters (users who learned skills before demand surge)"
  ],
  
  "challenges": [
    {
      "challenge_name": "Future Skills Challenge",
      "goal": "Learn 3 skills with predicted >50% demand growth",
      "duration": "90 days",
      "reward": "Future Ready Badge + 500 XP"
    },
    {
      "challenge_name": "Market Maverick Challenge",
      "goal": "Invest in 2 emerging skills (demand_index < 30, growth > 80%)",
      "duration": "180 days",
      "reward": "Early Adopter Badge + Premium Insights Access"
    }
  ],
  
  "xp_earning_rules": [
    {"action": "View market insight report", "xp": 10},
    {"action": "Learn skill with >70% demand growth", "xp": 50},
    {"action": "Complete skill before demand peak", "xp": 100},
    {"action": "Achieve top 10% market alignment", "xp": 200}
  ]
}
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

**Metrics to Track:**

```yaml
PREDICTION_ACCURACY_METRICS:
  - name: mape_overall
    description: Mean Absolute Percentage Error (overall)
    target: "< 15%"
    alert_threshold: "> 25%"
    
  - name: mape_by_horizon
    targets:
      3_months: "< 10%"
      6_months: "< 15%"
      1_year: "< 20%"
      3_years: "< 30%"
    
  - name: prediction_bias
    description: Systematic over/under prediction
    target: "-5% to +5%"
    alert_threshold: "> 10% OR < -10%"
    
  - name: confidence_calibration
    description: Actual error vs predicted confidence alignment
    target: "Pearson correlation > 0.70"
    alert_threshold: "< 0.50"

SUCCESS_METRICS:
  - name: prediction_success_rate
    type: percentage
    target: "> 95%"
    alert_threshold: "< 90%"
    
  - name: predictions_per_day
    type: count
    target: "50,000 - 200,000"
    alert_threshold: "< 10,000 OR > 500,000"
    
  - name: cache_hit_rate
    type: percentage
    target: "> 60%"
    description: "Percentage of requests served from cache"
    
  - name: user_satisfaction_score
    type: rating
    target: "> 4.0 / 5.0"
    measurement: "User ratings of market insights"

LATENCY_METRICS:
  - name: p50_prediction_latency
    target: "< 1.5 seconds"
    alert_threshold: "> 3.0 seconds"
    
  - name: p95_prediction_latency
    target: "< 3.0 seconds"
    alert_threshold: "> 6.0 seconds"
    
  - name: p99_prediction_latency
    target: "< 6.0 seconds"
    alert_threshold: "> 10.0 seconds"
    
  - name: batch_processing_time
    target: "< 30 minutes for 10,000 predictions"
    alert_threshold: "> 60 minutes"

ERROR_METRICS:
  - name: error_rate
    type: percentage
    target: "< 2%"
    alert_threshold: "> 5%"
    
  - name: insufficient_data_rate
    type: percentage
    description: "Requests rejected due to insufficient data"
    target: "< 10%"
    alert_threshold: "> 20%"
    
  - name: model_failure_rate
    type: percentage
    target: "< 0.5%"
    alert_threshold: "> 2%"

DRIFT_METRICS:
  - name: feature_distribution_drift
    type: KL_divergence
    baseline: "weekly_moving_average"
    alert_threshold: "KL_divergence > 0.3"
    
  - name: prediction_distribution_drift
    type: KL_divergence
    baseline: "weekly_moving_average"
    alert_threshold: "KL_divergence > 0.2"
    
  - name: model_accuracy_degradation
    type: percentage_change
    baseline: "previous_month_accuracy"
    alert_threshold: "degradation > 10%"

BUSINESS_IMPACT_METRICS:
  - name: prediction_driven_enrollments
    description: "Course enrollments driven by demand forecasts"
    target: "> 20% of total enrollments"
    
  - name: prediction_accuracy_user_outcomes
    description: "Users who followed predictions achieved better placement"
    target: "> 15% improvement in placement rate"
    
  - name: institute_curriculum_adoption
    description: "Institutes that updated curriculum based on predictions"
    target: "> 30% adoption rate"
    
  - name: employer_satisfaction
    description: "Employers finding predictions valuable for hiring"
    target: "> 4.2 / 5.0 rating"

FAIRNESS_METRICS:
  - name: prediction_parity_across_genders
    description: "Similar users get similar predictions regardless of gender"
    target: "Parity score > 0.90"
    alert_threshold: "< 0.80"
    
  - name: prediction_parity_across_geographies
    description: "No systematic bias favoring metro vs tier-2/tier-3 cities"
    target: "Parity score > 0.85"
    alert_threshold: "< 0.75"
    
  - name: salary_prediction_fairness
    description: "No gender/caste/religion bias in salary predictions"
    target: "Bias < 2%"
    alert_threshold: "> 5%"
```

**OBSERVABILITY_AGENT Integration:**

```python
EMIT_METRICS_EVENT:
{
  "event": "AGENT_METRICS_UPDATE",
  "agent_name": "MARKET_DEMAND_PREDICTION_AGENT",
  "timestamp_utc": "ISO8601",
  
  "performance_metrics": {
    "predictions_last_hour": 8234,
    "success_rate": 0.973,
    "error_rate": 0.027,
    "avg_latency_ms": 1456,
    "p95_latency_ms": 2789,
    "p99_latency_ms": 4123,
    "cache_hit_rate": 0.68,
    "model_confidence_avg": 0.82
  },
  
  "accuracy_metrics": {
    "mape_overall": 13.2,
    "mape_3_months": 9.8,
    "mape_6_months": 14.1,
    "mape_1_year": 18.7,
    "prediction_bias": 2.3,
    "confidence_calibration": 0.76
  },
  
  "drift_indicators": {
    "feature_drift_kl": 0.12,
    "prediction_drift_kl": 0.08,
    "accuracy_degradation": -3.2  // -3.2% from last month (improved)
  },
  
  "anomalies_detected": [
    {
      "type": "SUDDEN_DEMAND_SPIKE",
      "affected_entity": "AI_Ethics_skill",
      "severity": "medium",
      "description": "Demand increased 180% in 3 days",
      "possible_cause": "Major AI regulation news",
      "action_taken": "Increased confidence interval, triggered re-prediction"
    }
  ],
  
  "business_impact": {
    "prediction_driven_actions_count": 12456,  // Users acted on predictions
    "average_user_satisfaction": 4.3,
    "institute_curriculum_changes": 23,  // Institutes updated curriculum
    "employer_intelligence_requests": 145
  }
}
```

**Dashboard Integration:**
- Real-time Grafana dashboard with drill-down capabilities
- Prometheus metrics scraping (15-second intervals)
- PagerDuty alerting for critical thresholds
- Weekly automated performance reports to product & ML teams
- Monthly business impact reports to executive team

---

## 1️⃣5️⃣ VERSIONING POLICY

**Version Scheme:** Semantic Versioning (MAJOR.MINOR.PATCH)

**MAJOR version change (X.0.0):**
- Model architecture complete rewrite (e.g., replace Prophet with new forecasting method)
- Breaking changes to prediction output schema
- Change in prediction methodology (e.g., shift from time-series to causal inference)
- Fundamental feature set changes
- Requires data migration and consumer agent updates

**MINOR version change (1.X.0):**
- New prediction types added (e.g., add "hiring_demand" prediction)
- New optional output fields
- Model improvements (same architecture, better features)
- Performance optimizations
- New data sources integrated
- Backward compatible changes

**PATCH version change (1.0.X):**
- Bug fixes (e.g., fix salary outlier handling)
- Model retraining with same architecture
- Configuration updates
- Minor accuracy improvements
- No functional changes

**Version Change Process:**

```
1. PROPOSAL
   - ML team submits version bump proposal
   - Include: Performance impact, accuracy gains, breaking changes

2. REVIEW
   - Tech lead + ML lead + Product manager review
   - Assess: Business value, risk, resource requirements

3. TEST
   - A/B testing on 10% traffic for 7 days
   - Compare: Accuracy, latency, user satisfaction
   - Validate: No prediction parity violations (fairness)

4. DOCUMENTATION
   - Update API documentation
   - Document model changes
   - Create migration guide (if breaking changes)

5. STAGED ROLLOUT
   - Stage 1: 10% traffic (3 days, monitor closely)
   - Stage 2: 50% traffic (3 days)
   - Stage 3: 100% traffic
   - Rollback triggers defined at each stage

6. VALIDATION
   - Monitor accuracy metrics for 14 days post-rollout
   - Collect user feedback
   - Performance regression analysis

7. ROLLBACK_PLAN
   - Instant rollback capability (config flag)
   - Data migration rollback scripts
   - Communication plan for downstream agents
```

**Version Storage:**
```
- Git tag: v1.2.3
- Model registry: models/market_demand_prediction/v1.2.3/
  - prophet_model.pkl
  - xgboost_model.pkl
  - lstm_model.h5
  - feature_config.json
  - model_metadata.json
- Database: agent_versions table with deployment history
- Audit log: Every prediction references exact version used
```

**Backward Compatibility:**
```
- API: Support last 2 MAJOR versions simultaneously
- Output schema: Additive only (never remove fields)
- Deprecated fields: 180-day sunset period with warnings
- Model versions: Last 3 MINOR versions available for comparison
```

**Rollback Capability:**
```
- Config-driven model selection (no code deploy needed)
- Instant rollback via feature flag (< 5 minutes)
- Maximum rollback window: 60 days (after that, old models deleted)
- Automatic rollback triggers:
  - Accuracy drops > 15%
  - Error rate > 10%
  - Latency > 2x baseline
  - Downstream agent failures > 5%
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES

**Agent MUST NOT:**

```
❌ Create hidden prediction logic not documented in spec
❌ Modify historical prediction records (append-only audit)
❌ Auto-delete audit logs (retention: 7 years minimum)
❌ Override safety/fairness checks (mandatory audits)
❌ Bypass data privacy protections (salary anonymization required)
❌ Mix raw cross-tenant data in predictions (aggregate only)
❌ Execute outside defined prediction types
❌ Make predictions without sufficient data (min 6 months or 100 samples)
❌ Predict beyond 3-year horizon (too unreliable)
❌ Generate predictions with confidence < 0.40 (threshold for usefulness)
❌ Share individual user data in market aggregations
❌ Use protected attributes (gender, race) as prediction features
❌ Provide financial advice (predictions are informational only)
❌ Guarantee prediction accuracy (always include uncertainty)
❌ Train on biased historical data without bias mitigation
❌ Cache predictions indefinitely (max 24 hours for volatile data)
```

**Agent MUST:**

```
✅ Validate data sufficiency before making predictions
✅ Include confidence intervals (upper and lower bounds)
✅ Log every prediction to immutable audit trail
✅ Emit structured events for downstream agents
✅ Provide explainability (contributing factors)
✅ Include data freshness timestamps
✅ Respect data localization rules (geographic restrictions)
✅ Perform fairness audits weekly (demographic parity checks)
✅ Cap extreme predictions (max +200%, min -80%)
✅ Flag low-confidence predictions with warnings
✅ Honor user privacy (no individual salary disclosure)
✅ Maintain <3 second p95 latency SLA
✅ Achieve >95% uptime SLA
✅ Retrain models when drift detected (MAPE > 25% for 7 days)
✅ Fallback to baseline when models unavailable
✅ Include historical accuracy metrics in outputs
✅ Track and report business impact (prediction-driven actions)
✅ Support disaster recovery (RPO < 15 minutes)
✅ Version all models and predictions
✅ Provide rollback capability (last 60 days)
```

---

## 1️⃣7️⃣ COMPLIANCE & REGULATORY REQUIREMENTS

**GDPR Compliance (European Users):**
```
- Right to explanation: Predictions must be explainable (feature contributions provided)
- Right to erasure: Support user data deletion while preserving aggregated insights
- Right to object: Users can opt-out of salary data contribution (anonymized)
- Data minimization: Collect only necessary data for predictions
- Purpose limitation: Market intelligence only, no other purposes
- Transparency: Clear disclosure of prediction methodology
- Data portability: Export user's prediction history in JSON format
```

**DPDP Act (India):**
```
- Data fiduciary obligations: Ecoskiller is data fiduciary for user data
- Purpose limitation: Market prediction purpose clearly defined
- Data localization: Indian user data stored in India
- Consent management: Users consent to market analysis of their data
- Sensitive personal data: Salary data handled with extra care (anonymization)
- Grievance redressal: Appeals process for disputed predictions
- Audit trail: All predictions logged for regulatory audit
```

**Fair Labor Standards & Anti-Discrimination:**
```
- No discriminatory predictions based on protected attributes
- Salary predictions must not reinforce gender/race pay gaps
- Job trend analysis must not disadvantage any demographic group
- Skill gap identification must be equitable (no bias toward metro areas only)
- Fairness audits mandatory (weekly demographic parity checks)
- Disparate impact analysis (quarterly reports)
```

**Data Privacy & Anonymization:**
```
- Salary data: Minimum 10 samples, outlier removal, differential privacy noise
- Job data: Company names anonymized in public reports
- User data: De-identification before aggregation
- Geographic data: Masking for small populations (< 50 users)
- No re-identification attacks possible (k-anonymity enforced)
```

**Transparency Requirements:**
```
- Model versions disclosed in predictions
- Data sources disclosed (internal, external)
- Historical accuracy disclosed
- Confidence levels clearly stated
- Limitations clearly communicated
- Disclaimer: "Predictions are informational, not guarantees"
```

---

## 1️⃣8️⃣ DISASTER RECOVERY & BUSINESS CONTINUITY

**Recovery Time Objective (RTO):** < 2 hours

**Recovery Point Objective (RPO):** < 15 minutes

**Backup Strategy:**
```
Real-time Replication:
- Audit logs replicated to 3 regions (AWS multi-region)
- Prediction cache replicated (Redis cluster)

Hourly Backups:
- Prediction results database (PostgreSQL)
- Model metadata and versioning data

Daily Backups:
- ML models and configurations (S3 versioned buckets)
- Historical market data (time-series DB)
- Feature store snapshots

Weekly Backups:
- Full system snapshot (disaster recovery)
- Training data archives
```

**Disaster Scenarios & Recovery:**

```
Scenario 1: Database Failure (PostgreSQL)
- Detection: Health check fails, queries timeout
- Response: Automatic failover to read replica (< 30 seconds)
- Recovery: Restore primary from latest backup
- Downtime: < 5 minutes (users may experience slight delay)
- Data loss: 0 (real-time replication)

Scenario 2: Model Service Failure
- Detection: Prediction requests fail, model API errors
- Response: Automatic failover to baseline models (historical averages)
- Recovery: Restart model service, verify health
- Downtime: 0 (degraded service, reduced accuracy)
- User impact: Lower confidence predictions, warnings displayed

Scenario 3: Cache Failure (Redis)
- Detection: Cache miss rate spikes to 100%
- Response: Predictions served directly from database (slower)
- Recovery: Restore Redis from backup, rebuild cache
- Downtime: 0 (increased latency 2-3x)
- User impact: Slower response times (3-5 seconds vs 1-2 seconds)

Scenario 4: Feature Store Failure
- Detection: Feature fetch requests fail
- Response: Compute features on-demand (slower)
- Recovery: Restore feature store service
- Downtime: 0 (degraded performance)
- User impact: Increased latency (5-7 seconds)

Scenario 5: Complete Region Failure (AWS region down)
- Detection: Multi-region health check fails
- Response: Traffic rerouted to backup region (automatic DNS failover)
- Recovery: Full region rebuild (parallel to backup region serving traffic)
- Downtime: < 10 minutes (DNS propagation)
- Data loss: < 15 minutes (RPO)

Scenario 6: Data Corruption (Bad model predictions)
- Detection: Anomalous prediction patterns (extreme values)
- Response: Rollback to previous model version
- Recovery: Investigate corruption, retrain model
- Downtime: < 30 minutes
- User impact: Some bad predictions served (< 1 hour window)
```

**Testing:**
```
Disaster Recovery Drills:
- Full region failover test: Quarterly
- Database failover test: Monthly
- Model rollback test: Monthly
- Cache failure simulation: Monthly

Backup Restoration:
- Database restore test: Weekly (staging environment)
- Model restore test: Weekly
- Audit log restore test: Monthly

Chaos Engineering:
- Random service failures (production): Weekly
- Network partition simulations: Bi-weekly
- Latency injection: Continuous (subset of traffic)
```

---

## 1️⃣9️⃣ HUMAN-IN-THE-LOOP REQUIREMENTS

**When Human Review Required:**

```
1. Low Confidence: confidence_score < 0.60
2. Extreme Predictions: Demand change > 200% OR < -80%
3. Critical Skill Gaps: Gap urgency = "critical" (affects many users)
4. Controversial Predictions: Major industry disruption forecasted
5. Fairness Violations: Demographic parity check fails
6. Model Anomalies: Prediction contradicts multiple data sources
7. High-Stakes Decisions: Institute curriculum changes (budget impact > $100K)
8. User Appeals: User disputes prediction affecting their career
9. New Skill Detection: Emerging skills (need human validation of skill name)
10. Regulatory Compliance: Predictions affecting regulated professions
```

**Human Review SLA:**
```
Priority 1 (Safety/Compliance): < 4 hours
Priority 2 (Critical Skill Gaps): < 24 hours
Priority 3 (User Appeals): < 48 hours
Priority 4 (Low Confidence): < 7 days
Priority 5 (Routine Audit): < 30 days
```

**Human Override Authority:**
```
Market Intelligence Analysts:
- Can override low-confidence predictions
- Can adjust extreme predictions (cap/floor)
- Can flag predictions for investigation

Domain Experts:
- Can validate emerging skill classifications
- Can provide context for anomalous trends
- Can confirm industry disruption signals

Compliance Officers:
- Can block predictions violating fairness rules
- Can require additional disclaimers
- Can mandate re-training with bias mitigation

Senior Analysts:
- Can approve major market shift predictions
- Can authorize high-stakes recommendations (institute curriculum)
- Can greenlight controversial forecasts

All overrides MUST:
- Be logged with detailed justification
- Include evidence supporting override decision
- Be reviewed by peer (second opinion required for high-stakes)
- Be tracked for pattern analysis (systematic override indicates model issue)
```

**Human-AI Collaboration Workflow:**

```
Step 1: AI Generates Prediction
- Model produces forecast with confidence score
- Explainability features extracted (SHAP values)
- Contributing factors identified

Step 2: Automated Quality Check
- Confidence threshold check
- Fairness audit
- Anomaly detection
- Extreme value detection

Step 3: Routing Decision
- High confidence + no issues → Auto-approve → Publish
- Low confidence OR issues detected → Route to human reviewer

Step 4: Human Review
- Reviewer sees:
  - Prediction details
  - Contributing factors (explainability)
  - Historical accuracy of similar predictions
  - Data quality indicators
  - Conflicting signals (if any)
- Reviewer makes decision:
  - Approve (publish as-is)
  - Approve with disclaimer (add warning)
  - Reject (do not publish)
  - Request re-analysis (with additional data)

Step 5: Feedback Loop
- Human decisions logged
- Agreement/disagreement with AI tracked
- Model retrained with human-labeled examples
- Threshold adjustments if needed
```

**Escalation Paths:**

```
Level 1: Automated Agent
- Handles: Routine predictions (confidence > 0.75, no issues)
- SLA: < 3 seconds

Level 2: Junior Market Analyst
- Handles: Low confidence (0.60-0.75), minor anomalies
- SLA: < 48 hours

Level 3: Senior Market Analyst
- Handles: Extreme predictions, critical skill gaps, user appeals
- SLA: < 24 hours

Level 4: Domain Expert
- Handles: Industry disruptions, emerging skills, controversial forecasts
- SLA: < 7 days

Level 5: Executive Review
- Handles: Platform-wide policy changes, major market shifts
- SLA: As needed (strategic decision)
```

---

## 2️⃣0️⃣ TESTING & QUALITY ASSURANCE

**Unit Tests (100% Coverage Required):**
```
Input validation:
- Invalid prediction_type rejection
- Missing required field rejection
- Invalid forecast_horizon rejection
- Tenant isolation verification

Prediction logic:
- Demand index calculation
- Confidence score calculation
- Time-series forecasting (mock models)
- Salary range calculation
- Skill gap computation

Output formatting:
- JSON schema compliance
- Timestamp formatting (UTC ISO8601)
- Currency code inclusion (salary fields)
- Confidence interval calculation

Error handling:
- Insufficient data handling
- Model failure fallback
- Extreme value capping
- Timeout handling
```

**Integration Tests:**
```
End-to-end prediction flow:
- Request → Validation → Feature extraction → Model inference → Output generation → Event emission
- Expected duration: < 3 seconds

Upstream agent integration:
- JOB_AGGREGATION_AGENT data fetch
- SALARY_AGGREGATION_AGENT data fetch (with privacy checks)
- SKILL_TAXONOMY_AGENT API calls
- FEATURE_STORE_AGENT feature retrieval

Downstream event triggering:
- RECOMMENDATION_ENGINE receives update event
- CAREER_PATH_PLANNER receives forecast data
- SKILL_GAP_ALERT triggered for critical gaps
- USER_NOTIFICATION sent for subscribed users

Database operations:
- Prediction result insert (atomic)
- Audit log append (immutable)
- Cache read/write (Redis)
- Time-series data query (optimized range queries)

Model API calls:
- Prophet model inference
- XGBoost model inference
- LLM API call (with timeout and fallback)
```

**ML Model Tests:**
```
Accuracy validation:
- Test set accuracy (held-out data): MAPE < 15%
- Cross-validation: 5-fold CV, consistent results
- Time-series split: Train on past, test on future (no look-ahead bias)

Robustness testing:
- Adversarial inputs (extreme values, missing data)
- Data distribution shifts (simulate market shocks)
- Edge cases (new skills with minimal data)

Fairness validation:
- Demographic parity (predictions similar across groups)
- Equal opportunity (skill recommendations equitable)
- Calibration (confidence scores accurate across groups)

Feature importance:
- SHAP values computed correctly
- Contributing factors explainable
- No unexpected feature dependencies
```

**Load & Stress Tests:**
```
Peak load:
- 500 RPS sustained for 2 hours
- 0 errors, <3 second p95 latency
- Cache hit rate > 60%

Burst traffic:
- 1000 RPS for 10 minutes (2x normal)
- Graceful degradation (increased latency acceptable)
- No service crashes

Sustained load:
- 300 RPS for 24 hours
- Memory leak detection (no memory growth)
- CPU utilization stable (< 80% average)

Batch processing:
- 10,000 predictions in < 30 minutes
- Parallel processing verification
- Database connection pool stress test

Concurrent users:
- 5,000 simultaneous requests
- Connection pool management
- Queue depth monitoring
```

**Security Tests:**
```
Authorization:
- Users without premium access cannot access salary predictions
- Cross-tenant prediction request blocked
- Domain filter enforcement verified

Data privacy:
- Salary data anonymization validated (no individual disclosure)
- Minimum sample size enforced (10+ samples)
- Geographic masking tested (small cities excluded)

Input validation:
- SQL injection attempts blocked
- Malformed JSON rejected
- Excessively large requests rejected (DoS protection)

API authentication:
- Invalid JWT rejected
- Expired token rejected
- Rate limiting enforced (100 requests/hour/user)
```

**Chaos Engineering:**
```
Service failures:
- Random model service kills (test fallback to baseline)
- Database connection drops (test retry logic)
- Cache failures (test direct DB access)

Network issues:
- Latency injection (simulate slow network)
- Packet loss simulation
- Intermittent connectivity

Data quality issues:
- Missing data injection (test handling)
- Corrupted data injection (test validation)
- Stale data simulation (test freshness checks)

Expected outcome: Graceful degradation, no cascading failures
```

---

## 2️⃣1️⃣ DEPLOYMENT CHECKLIST

**Pre-Deployment:**
```
✅ All tests passing (unit, integration, load, security)
✅ Code review approved (2+ senior engineers)
✅ ML model validation complete (accuracy benchmarks met)
✅ Security review complete (penetration testing passed)
✅ Fairness audit complete (demographic parity checks passed)
✅ Performance benchmarks met (latency, throughput)
✅ Documentation updated (API docs, model docs, runbooks)
✅ Version tag created (git tag v1.x.y)
✅ Rollback plan documented and tested
✅ On-call engineer assigned (pager duty)
✅ Stakeholders notified (product, analytics, support teams)
✅ Feature flags configured (for phased rollout)
✅ Monitoring dashboards prepared (Grafana, Prometheus)
```

**Deployment Process:**
```
Stage 1: STAGING ENVIRONMENT
- Deploy to staging
- Run smoke tests (15 minutes)
- Verify: Predictions generate correctly, events emitted, audit logs written
- Approval gate: Tech lead sign-off

Stage 2: PRODUCTION (Blue-Green, 5% Traffic)
- Deploy to production (blue-green strategy)
- Route 5% traffic to new version (A/B test)
- Monitor for 2 hours:
  - Error rate < 2%
  - Latency < 3 seconds p95
  - Accuracy (compare to old version)
  - No anomalies
- Approval gate: ML lead sign-off

Stage 3: PRODUCTION (25% Traffic)
- Increase traffic to 25%
- Monitor for 4 hours
- Verify: Downstream agents functioning correctly
- Check: User feedback, support tickets
- Approval gate: Product manager sign-off

Stage 4: PRODUCTION (50% Traffic)
- Increase traffic to 50%
- Monitor for 8 hours
- Analyze: Business impact metrics (prediction-driven actions)
- Approval gate: Automated (no alerts)

Stage 5: PRODUCTION (100% Traffic)
- Increase traffic to 100%
- Monitor for 24 hours (enhanced logging enabled)
- Verify: No performance degradation, no fairness violations
- Collect: User satisfaction metrics
- Approval gate: Automated (no critical alerts)

Stage 6: Post-Deployment Validation
- Run post-deployment test suite
- Verify: Model versions correct in production
- Check: Audit trail completeness
- Confirm: Backup and disaster recovery working
- Declare deployment successful

Note: At any stage, if rollback triggers are met, immediately rollback to previous version.
```

**Rollback Triggers:**
```
Automatic Rollback (no human approval needed):
❌ Error rate > 10%
❌ P95 latency > 8 seconds for 10 minutes
❌ Model inference failure rate > 5%
❌ Critical security vulnerability detected
❌ Data corruption detected
❌ Downstream agent failures > 10%

Manual Rollback (requires approval):
- Accuracy degradation > 20% (compared to previous version)
- User satisfaction score drops > 0.5 points
- Business impact metrics decline > 30%
- Fairness violations detected (demographic parity < 0.75)
- Controversial predictions causing user complaints
```

**Post-Deployment:**
```
✅ Verify Grafana dashboards (predictions/sec, latency, error rate)
✅ Check PagerDuty (no critical alerts)
✅ Verify audit logs (predictions being logged correctly)
✅ Confirm event emission (downstream agents receiving events)
✅ Monitor user feedback channels (support tickets, ratings)
✅ Run accuracy validation (compare predictions to outcomes over next 30 days)
✅ Check cache performance (hit rate, eviction rate)
✅ Verify database performance (query times, connection pool utilization)
✅ Confirm backup jobs running (audit logs, prediction data)
✅ Schedule post-deployment review meeting (1 week post-deployment)
```

---

## 2️⃣2️⃣ OPERATIONAL RUNBOOKS

### Runbook 1: High Error Rate

```
SYMPTOM: Error rate > 5% for 15 minutes
IMPACT: Users cannot get predictions, platform functionality degraded

DIAGNOSIS:
1. Check Grafana dashboard for error breakdown (by error type)
2. Check application logs (filter by severity=ERROR)
3. Identify most common error code:
   - INPUT_INVALID: User input issue (inform users)
   - INSUFFICIENT_DATA: Data availability issue (check upstream agents)
   - MODEL_FAILURE: Model service down (check model API)
   - DATABASE_ERROR: Database issue (check DB health)

RESOLUTION:
If INPUT_INVALID errors:
  → No action needed (user errors)
  → Monitor for patterns (possible API docs issue)

If INSUFFICIENT_DATA errors (> 20% of requests):
  → Check: JOB_AGGREGATION_AGENT health
  → Check: Database data freshness (last update timestamp)
  → Notify: Data pipeline team

If MODEL_FAILURE errors:
  → Check: Model service health endpoint
  → Check: Model server logs
  → Action: Restart model service OR enable fallback to baseline models
  → Notify: ML team

If DATABASE_ERROR:
  → Check: PostgreSQL health (connections, query times)
  → Action: Restart connection pool OR failover to read replica
  → Notify: Database team

ESCALATION:
- 15 minutes: On-call engineer
- 30 minutes: Tech lead + ML lead
- 60 minutes: VP Engineering
- 120 minutes: Incident commander (cross-functional)

POST-INCIDENT:
- Root cause analysis (within 24 hours)
- Update runbook if new failure mode discovered
- Implement preventive measures
```

### Runbook 2: Prediction Accuracy Degradation

```
SYMPTOM: MAPE > 25% for 7 consecutive days (alert triggered)
IMPACT: Users receiving low-quality predictions, trust in platform erodes

DIAGNOSIS:
1. Check model performance dashboard (accuracy by skill category, horizon)
2. Identify affected segments:
   - All skills OR specific categories (technical, soft, domain)?
   - All horizons OR specific (3-month, 1-year)?
   - All geographies OR specific regions?
3. Check for data issues:
   - Data freshness (is data stale?)
   - Data completeness (missing data from upstream agents?)
   - Data quality (anomalies, outliers, corruption?)
4. Check for market regime change:
   - Did a major event happen (economic shock, policy change)?
   - Are feature distributions shifting (drift detected)?
5. Review recent model deployments (did accuracy drop after new model version?)

RESOLUTION:
If data issue:
  → Fix data pipeline
  → Backfill missing data
  → Re-run predictions with corrected data
  → Notify: Data engineering team

If market regime change:
  → Retrain models with recent data (emphasize last 30-60 days)
  → Adjust feature weights (increase weight on recent trends)
  → Notify: Market intelligence analysts (document market shift)

If model issue:
  → Rollback to previous model version (immediate fix)
  → Investigate model failure (feature drift, data shift?)
  → Retrain model with updated data and features
  → A/B test new model before full deployment

If external shock (e.g., pandemic, war):
  → Reduce confidence scores (reflect increased uncertainty)
  → Widen confidence intervals
  → Add disclaimers to predictions ("high market volatility")
  → Consider disabling long-term forecasts (1-year, 3-year) temporarily

ESCALATION:
- 24 hours: ML lead
- 48 hours: Product manager + Analytics team
- 7 days: VP Product + VP Engineering (if unresolved)

PREVENTIVE MEASURES:
- Implement early warning system (alert when accuracy drops 10%)
- Automated model retraining pipeline (weekly)
- Enhanced drift detection (daily checks)
```

### Runbook 3: Fairness Violation Detected

```
SYMPTOM: Weekly fairness audit flags demographic parity < 0.80
IMPACT: Platform at risk of discriminatory outcomes, regulatory violation, user complaints

DIAGNOSIS:
1. Identify affected demographic group:
   - Gender? Geography? Domain?
2. Identify affected prediction type:
   - Skill demand? Salary forecast? Skill gap?
3. Analyze root cause:
   - Historical data bias (training data reflects past discrimination)?
   - Feature bias (features correlated with protected attributes)?
   - Model bias (model learned discriminatory patterns)?
   - Sample size issue (small sample for certain groups)?
4. Check if issue is recent (new) or historical (always present)

RESOLUTION:
Immediate action:
  → Flag affected predictions with disclaimer
  → Reduce confidence scores for affected groups
  → Notify: Compliance officer + Legal team

Short-term fix (24 hours):
  → Apply bias mitigation (re-weighting, threshold adjustment)
  → Review features (remove or transform biased features)
  → Test: Verify fairness metrics improve

Long-term fix (1 week):
  → Retrain model with fairness constraints
  → Implement fairness-aware algorithms (e.g., equalized odds)
  → Collect more data from underrepresented groups (if sample size issue)
  → Update fairness monitoring (tighter thresholds, more frequent audits)

ESCALATION:
- Immediate: Compliance officer + Legal team (regulatory risk)
- 24 hours: VP Engineering + Chief Ethics Officer
- 48 hours: CEO (if media attention or regulatory inquiry)

POST-INCIDENT:
- Publish transparency report (acknowledge issue, explain fix)
- External fairness audit (independent review)
- Update bias mitigation guidelines
- Training for ML team (fairness best practices)
```

### Runbook 4: Extreme Prediction Spike

```
SYMPTOM: Prediction shows demand increase > 200% OR decrease < -80%
IMPACT: Potentially misleading users, low credibility if prediction is wrong

DIAGNOSIS:
1. Check if prediction is for single skill OR multiple skills
2. Review contributing factors:
   - Which features drove the extreme prediction?
   - Is there a logical explanation (e.g., new technology, regulation)?
3. Check data quality:
   - Are there data entry errors (typos, duplicates)?
   - Are there outliers in raw data (anomalous job postings)?
4. Consult external sources:
   - Industry news (any major announcements?)
   - Google Trends (is skill trending?)
   - Government policies (any regulatory changes?)
5. Check model confidence:
   - Is confidence low (< 0.60)? Model is uncertain.
   - Is confidence high (> 0.80)? Model is confident (could be real signal).

RESOLUTION:
If data error:
  → Correct data
  → Re-run prediction
  → Invalidate cached result

If real signal (verified by external sources):
  → Keep prediction as-is
  → Add context/explanation ("Due to recent XYZ announcement...")
  → Flag for human review (domain expert validation)
  → Consider publishing market intelligence report (explain trend)

If uncertain:
  → Cap prediction (limit to +200% max, -80% min)
  → Lower confidence score
  → Add strong disclaimer ("Unprecedented market movement detected, use caution")
  → Flag for human review

If recurring false alarms:
  → Adjust model (increase regularization, outlier detection)
  → Retrain with more conservative thresholds

ESCALATION:
- Immediate: Market intelligence analyst (domain expert)
- 24 hours: Product manager (if affects user-facing features)
- 48 hours: Executive team (if major market shift with broad impact)

COMMUNICATION:
- If prediction is published: Proactive communication to affected users
- If prediction impacts institutes: Direct outreach to institute admins
- If prediction is controversial: Prepare FAQ, talking points
```

---

## 2️⃣3️⃣ KEY PERFORMANCE INDICATORS (KPIs)

**Business KPIs:**
```
Primary:
- Predictions Generated: > 50,000 per day
- User Actions Driven by Predictions: > 10,000 per week
  (e.g., course enrollments, profile updates, skill additions)
- User Satisfaction with Market Insights: > 4.0 / 5.0
- Prediction Accuracy (3-month horizon): MAPE < 12%
- Prediction Accuracy (1-year horizon): MAPE < 20%

Secondary:
- Institute Curriculum Changes Driven by Predictions: > 20 per quarter
- Employer Intelligence Requests: > 500 per month
- Prediction-Driven Revenue: > $100K per month
  (premium subscriptions, employer services)
- User Retention (Market Insights Users): > 75% (3-month retention)

Leading Indicators:
- Market Intelligence Dashboard Views: > 100,000 per week
- Prediction Report Downloads: > 5,000 per week
- User Subscriptions to Trend Alerts: > 50,000 active
```

**Technical KPIs:**
```
Performance:
- System Uptime: > 99.9% (SLA)
- Error Rate: < 2%
- Latency P50: < 1.5 seconds
- Latency P95: < 3.0 seconds
- Latency P99: < 6.0 seconds
- Batch Processing Efficiency: 10,000 predictions in < 30 minutes
- Cache Hit Rate: > 60%

Model Quality:
- Prediction Accuracy (MAPE): < 15% overall
- Model Confidence Calibration: Pearson > 0.70
- Prediction Bias: -5% to +5%
- Drift Detection Sensitivity: Catch drift within 7 days
- Model Retraining Frequency: Weekly for high-demand skills
```

**Operational KPIs:**
```
Reliability:
- Mean Time Between Failures (MTBF): > 720 hours (30 days)
- Mean Time to Recovery (MTTR): < 1 hour
- Incident Count (Critical): < 1 per month
- Incident Count (Major): < 3 per month
- Rollback Success Rate: 100%

Development Velocity:
- Deployment Frequency: Bi-weekly (every 2 weeks)
- Lead Time for Changes: < 7 days (idea to production)
- Change Failure Rate: < 5%
- Time to Restore Service: < 1 hour

Model Management:
- Model Training Time: < 4 hours (for full retrain)
- Model Deployment Time: < 30 minutes (rollout to production)
- Model Version Coverage: 100% (every prediction tagged with version)
- Model Rollback Capability: 100% (can rollback last 60 days)
```

**Fairness & Safety KPIs:**
```
Fairness:
- Demographic Parity: > 0.85 (predictions similar across groups)
- Equal Opportunity: > 0.85 (skill recommendations equitable)
- Salary Prediction Fairness: Bias < 3%
- Geographic Fairness: Parity > 0.80 (metro vs tier-2/tier-3)

Data Privacy:
- Salary Anonymization Compliance: 100%
- Minimum Sample Size Compliance: 100% (never < 10 samples)
- Geographic Masking Compliance: 100% (small cities protected)
- Individual Data Exposure Incidents: 0 (zero tolerance)

Compliance:
- GDPR Compliance: 100% (right to explanation, erasure, portability)
- DPDP Compliance: 100% (data localization, consent management)
- Fairness Audit Frequency: Weekly (no gaps)
- Regulatory Audit Pass Rate: 100%
```

**User Engagement KPIs:**
```
Adoption:
- Market Intelligence Feature Adoption: > 40% of active users
- Premium Market Insights Subscription Rate: > 5% of users
- Prediction Report Sharing: > 2,000 shares per week
- Career Planning Tool Usage (Prediction-Driven): > 30% of career planners

Satisfaction:
- Net Promoter Score (NPS): > 40
- User Trust Score (Survey): > 80%
- Prediction Usefulness Rating: > 4.2 / 5.0
- Prediction Accuracy Perceived by Users: > 75% "accurate or very accurate"

Retention:
- User Churn Rate (Market Insights Users): < 10% (monthly)
- Repeat Usage Rate: > 60% (users who view predictions 2+ times)
- Trend Alert Engagement: > 40% open rate, > 15% click-through rate
```

---

## 2️⃣4️⃣ CONTINUOUS IMPROVEMENT PROCESS

**Weekly:**
```
Model Performance Review:
- Review MAPE, prediction bias, confidence calibration
- Identify skills with degrading accuracy (flag for attention)
- Analyze new skills added (ensure sufficient data before predicting)
- Review anomaly detection flags (investigate unusual patterns)

Data Quality Checks:
- Check data freshness (all upstream sources up-to-date?)
- Check data completeness (missing data from any sources?)
- Validate data integrity (no corruption, outliers handled?)
- Review data volume (sufficient samples for predictions?)

User Feedback Analysis:
- Review user ratings of predictions (identify low-rated predictions)
- Analyze user comments/feedback (common complaints? suggestions?)
- Track prediction-driven actions (are users acting on predictions?)
- Identify high-value predictions (which predictions drove most actions?)

Feature Engineering:
- Review new feature candidates (proposed by ML team or analysts)
- Test new features (offline A/B testing on historical data)
- Analyze feature importance (SHAP values, feature correlations)
- Deprecate low-value features (reduce model complexity)
```

**Monthly:**
```
Model Retraining:
- Full model retraining on latest 2-year dataset
- A/B test new model vs current production model (10% traffic)
- Compare: Accuracy, latency, fairness, user satisfaction
- Deploy if improvement > 5% accuracy OR reduce latency > 20%

Fairness Audit:
- Comprehensive demographic parity analysis (gender, geography, domain)
- Disparate impact analysis (are any groups systematically disadvantaged?)
- Salary prediction fairness (no gender pay gap reinforcement)
- Document audit results (share with compliance team)

Drift Detection:
- Analyze feature distribution shifts (KL divergence over 30 days)
- Identify regime changes (market structure changes)
- Update baseline distributions (for next month's drift detection)
- Retrain models if significant drift detected

Business Impact Review:
- Track prediction-driven outcomes (course enrollments, job applications)
- Measure ROI (revenue generated by market intelligence feature)
- User retention analysis (do prediction users stay longer?)
- Institute adoption (how many institutes using predictions for curriculum?)

Documentation Update:
- Update API documentation (any new fields, endpoints?)
- Update model documentation (architecture, features, performance)
- Update runbooks (any new failure modes discovered?)
- Update training materials (for new team members)
```

**Quarterly:**
```
Major Model Review:
- Evaluate model architecture (should we try new algorithms?)
- Benchmark against state-of-the-art (research papers, competitions)
- Consider major upgrades (deep learning, causal inference, etc.)
- Plan multi-quarter ML roadmap (prioritize improvements)

Compliance & Security Audit:
- External security audit (penetration testing)
- GDPR compliance review (privacy policies, user rights)
- DPDP compliance review (data localization, consent)
- Fairness external audit (independent review of predictions)

Stakeholder Feedback:
- User surveys (large-scale, representative sample)
- Institute interviews (understand curriculum decision-making)
- Employer interviews (hiring intelligence needs)
- Analyst focus groups (advanced user needs)

Competitive Analysis:
- Review competitor offerings (LinkedIn, Glassdoor, Indeed, Naukri)
- Identify gaps (what are we missing?)
- Identify differentiators (what makes us unique?)
- Update product roadmap (based on competitive landscape)

Disaster Recovery Drill:
- Full region failover test
- Database restore test (production-scale data)
- Model rollback test (rollback 30 days)
- Validate RTO/RPO (< 2 hours RTO, < 15 minutes RPO)
```

**Annually:**
```
Full System Architecture Review:
- Evaluate tech stack (any obsolete technologies?)
- Consider major refactoring (improve maintainability?)
- Scalability assessment (can we handle 10x growth?)
- Cost optimization (reduce cloud costs without sacrificing quality)

ML Strategy Review:
- Evaluate 70/80 ML vs 20/30 AI split (still optimal?)
- Consider new ML paradigms (federated learning, on-device ML)
- Plan for emerging technologies (quantum ML, neuromorphic computing)
- Research investment areas (which ML problems need more research?)

Product Vision Alignment:
- Review business goals (does agent support company strategy?)
- Evaluate feature roadmap (are we building the right things?)
- User needs evolution (how have user needs changed?)
- Platform integration (how does this agent fit in ecosystem?)

Team & Process:
- Team skill assessment (what training is needed?)
- Process improvements (CI/CD, testing, deployment)
- Tool evaluation (better MLOps tools available?)
- Knowledge sharing (documentation, internal training)

Long-Term Roadmap:
- 3-year ML roadmap (major model architecture evolutions)
- 3-year product roadmap (new prediction types, features)
- 3-year infrastructure roadmap (scalability, multi-region)
- 3-year compliance roadmap (anticipate regulatory changes)
```

---

## 🔒 FINAL SEAL & LOCK

**This specification is:**
```
✔ SEALED - No creative interpretation allowed
✔ LOCKED - Changes require version bump + review + approval
✔ DETERMINISTIC - Same input → Same output (given same model version)
✔ COMPLIANT - GDPR + DPDP + Fair Labor Standards
✔ SECURE - Zero-trust + Multi-tenant isolation + Privacy protection
✔ AUDITABLE - Immutable audit trail (7-year retention)
✔ SCALABLE - 10M-100M users ready (50K+ predictions/day)
✔ OBSERVABLE - Full monitoring + alerting + dashboards
✔ RECOVERABLE - Disaster recovery tested (RTO < 2 hours, RPO < 15 minutes)
✔ EXPLAINABLE - ML predictions with contributing factors
✔ FAIR - Weekly fairness audits + demographic parity enforcement
✔ ACCURATE - Target MAPE < 15%, with confidence intervals
✔ HUMAN-SUPERVISED - Human-in-the-loop for critical decisions
✔ BUSINESS-ALIGNED - Clear KPIs + impact measurement
```

**DEVIATION FROM THIS SPEC:**
```
→ STOP EXECUTION
→ LOG INCIDENT (severity: CRITICAL)
→ ALERT ENGINEERING MANAGER + ML LEAD
→ REQUIRE SPECIFICATION AMENDMENT (versioned, reviewed, approved)
→ NO PRODUCTION DEPLOYMENT UNTIL RESOLVED
→ ESCALATE TO VP ENGINEERING IF DEVIATION UNRESOLVED IN 24 HOURS
```

**SPECIFICATION OWNERSHIP:**
```
Author: ML & Analytics Team
Reviewers: ML Lead, Data Science Lead, Product Manager, Compliance Officer
Approvers: VP Engineering, CTO, VP Product
Version: 1.0.0
Created: 2026-02-25
Last Updated: 2026-02-25
Next Review: 2026-05-25 (quarterly)
Changelog: See CHANGELOG.md
```

**CHANGES TO THIS SPECIFICATION:**
```
Required Process:
1. Submit pull request with full justification
2. Include: Expected impact (accuracy, latency, business value)
3. Require review by: ML lead + Product manager + 2 senior engineers
4. Require approval by: VP Engineering (major changes) OR Tech lead (minor changes)
5. Security review: If security-related (data access, privacy, fairness)
6. Compliance review: If regulatory implications (GDPR, DPDP, fairness)
7. Version bump: MAJOR, MINOR, or PATCH (semantic versioning)
8. Update CHANGELOG: Detailed change description
9. Notify dependent teams: Recommendation engine, career planner, etc.
10. Schedule deployment: Coordinate with on-call rotation
11. Post-deployment validation: 14-day monitoring period
```

---

## 📋 APPENDIX A: GLOSSARY

**Market Demand:** Quantity of jobs, roles, or skills employers are seeking

**Demand Index:** Normalized score (0-100) representing relative demand for a skill

**Demand Forecast:** Prediction of future demand for a skill/job at a future time

**Forecast Horizon:** Time period into the future (3 months, 6 months, 1 year, 3 years)

**Skill Gap:** Difference between employer demand and available talent supply

**Obsolescence:** Process of a skill becoming less relevant or outdated

**Time Series Forecasting:** ML technique for predicting future values based on historical patterns

**MAPE:** Mean Absolute Percentage Error - accuracy metric (lower is better, < 15% is good)

**Confidence Score:** Model's self-assessment of prediction reliability (0.0-1.0)

**Confidence Interval:** Range of values within which true value likely falls (e.g., 95% CI)

**Drift Detection:** Monitoring for degradation in model performance over time

**Fairness:** Ensuring predictions do not discriminate against demographic groups

**Demographic Parity:** Predictions are similar across demographic groups

**Differential Privacy:** Technique to protect individual data in aggregations (add noise)

**k-Anonymity:** Privacy technique ensuring at least k individuals in any data subset

**SHAP Values:** Explainability technique showing how features contribute to predictions

**Ensemble Model:** Combining multiple models for better accuracy

**Baseline Model:** Simple fallback model (historical average) when main model fails

**Idempotent Operation:** Operation that produces same result if executed multiple times

**Event-Driven Architecture:** System where components communicate via events (decoupled)

**Multi-Tenant Isolation:** Security principle preventing cross-tenant data access

**RTO (Recovery Time Objective):** Maximum acceptable downtime (< 2 hours)

**RPO (Recovery Point Objective):** Maximum acceptable data loss (< 15 minutes)

---

## 📋 APPENDIX B: ERROR CODES

```
INPUT VALIDATION ERRORS:
INPUT_INVALID_TYPE - prediction_type not recognized
INPUT_INVALID_HORIZON - forecast_horizon not valid
INPUT_MISSING_FIELD - Required field missing
INPUT_ENTITY_NOT_FOUND - Target entity (skill, job) not found
INPUT_INVALID_FILTER - Geographic or domain filter invalid

DATA ERRORS:
INSUFFICIENT_DATA - Not enough historical data for prediction (< 6 months or < 100 samples)
DATA_QUALITY_ISSUE - Data integrity problem detected
DATA_FRESHNESS_ISSUE - Data too stale (> 48 hours old)
DATA_CORRUPTION_DETECTED - Anomalous data patterns detected

SECURITY ERRORS:
SECURITY_PERMISSION_DENIED - User lacks required permission
SECURITY_TENANT_VIOLATION - Cross-tenant access attempt
SECURITY_RATE_LIMIT_EXCEEDED - Too many requests
SECURITY_DOMAIN_VIOLATION - Cross-domain access attempt

MODEL ERRORS:
MODEL_UNAVAILABLE - ML model service down
MODEL_TIMEOUT - Model inference took too long
MODEL_CONFIDENCE_LOW - Confidence < threshold (0.40)
MODEL_EXTREME_PREDICTION - Prediction capped (too extreme)

AI ERRORS:
AI_TIMEOUT - LLM API call timeout
AI_SERVICE_ERROR - LLM API returned error
AI_FALLBACK_USED - Using rule-based extraction (LLM failed)

SYSTEM ERRORS:
DATABASE_ERROR - Database operation failed
CACHE_ERROR - Cache operation failed
DEPENDENCY_UNAVAILABLE - Upstream agent unavailable (e.g., JOB_AGGREGATION_AGENT)
INTERNAL_ERROR - Unhandled exception
PREDICTION_FAILED - Generic prediction failure
```

---

## 📋 APPENDIX C: SAMPLE PREDICTION OUTPUT

```json
{
  "prediction_id": "550e8400-e29b-41d4-a716-446655440000",
  "prediction_type": "skill_demand",
  "generated_at": "2026-02-25T10:30:00Z",
  "forecast_horizon": "6_months",
  
  "target_entity": {
    "entity_type": "skill",
    "entity_id": "skill_python_programming",
    "entity_name": "Python Programming"
  },
  
  "demand_forecast": {
    "current_demand_index": 85,
    "predicted_demand_index": 92,
    "demand_change_percentage": 8.2,
    "demand_trend": "rising",
    "confidence_level": "high",
    "confidence_score": 0.87
  },
  
  "time_series_prediction": [
    {"period": "2026-03", "demand_index": 86, "lower_bound": 82, "upper_bound": 90, "confidence_interval": "95%"},
    {"period": "2026-04", "demand_index": 88, "lower_bound": 83, "upper_bound": 93, "confidence_interval": "95%"},
    {"period": "2026-05", "demand_index": 90, "lower_bound": 84, "upper_bound": 96, "confidence_interval": "95%"},
    {"period": "2026-06", "demand_index": 91, "lower_bound": 84, "upper_bound": 98, "confidence_interval": "95%"},
    {"period": "2026-07", "demand_index": 92, "lower_bound": 84, "upper_bound": 100, "confidence_interval": "95%"},
    {"period": "2026-08", "demand_index": 92, "lower_bound": 83, "upper_bound": 100, "confidence_interval": "95%"}
  ],
  
  "contributing_factors": [
    {
      "factor": "AI/ML adoption surge in enterprises",
      "impact_weight": 0.35,
      "direction": "positive",
      "evidence_strength": "strong"
    },
    {
      "factor": "Data science job posting growth (+25% YoY)",
      "impact_weight": 0.28,
      "direction": "positive",
      "evidence_strength": "strong"
    },
    {
      "factor": "Python course enrollments increasing (+18% MoM)",
      "impact_weight": 0.22,
      "direction": "positive",
      "evidence_strength": "moderate"
    },
    {
      "factor": "Government Digital India initiatives",
      "impact_weight": 0.15,
      "direction": "positive",
      "evidence_strength": "moderate"
    }
  ],
  
  "skill_gap_analysis": {
    "current_supply": 125000,
    "predicted_demand": 180000,
    "gap_size": 55000,
    "gap_percentage": 44.0,
    "urgency": "high",
    "time_to_critical": "9 months"
  },
  
  "salary_forecast": {
    "current_median_salary": 650000,
    "predicted_median_salary": 710000,
    "salary_range_lower": 580000,
    "salary_range_upper": 850000,
    "growth_rate": "9.2%",
    "market_competitiveness": "high",
    "currency": "INR"
  },
  
  "geographic_breakdown": [
    {"location": "Bangalore, Karnataka", "demand_index": 95, "growth_rate": 12.5, "market_maturity": "mature"},
    {"location": "Pune, Maharashtra", "demand_index": 88, "growth_rate": 15.2, "market_maturity": "growing"},
    {"location": "Hyderabad, Telangana", "demand_index": 92, "growth_rate": 10.8, "market_maturity": "mature"},
    {"location": "Delhi NCR", "demand_index": 90, "growth_rate": 8.3, "market_maturity": "mature"},
    {"location": "Mumbai, Maharashtra", "demand_index": 85, "growth_rate": 7.1, "market_maturity": "mature"}
  ],
  
  "recommendations": {
    "for_students": [
      "Prioritize learning Python - demand growing 8% over next 6 months",
      "Focus on AI/ML applications (highest growth area)",
      "Consider data science or backend development specialization",
      "Build portfolio projects to stand out in competitive market"
    ],
    "for_trainers": [
      "High demand for Python training - expand course offerings",
      "Focus on AI/ML integration (biggest growth driver)",
      "Consider advanced topics (deep learning, NLP) for premium courses",
      "Tier-2 cities showing strong growth (Pune +15%)"
    ],
    "for_institutes": [
      "Critical skill gap detected (44% shortage)",
      "Consider expanding Python curriculum across departments",
      "Salary growth 9% - strong ROI for students",
      "Industry partnerships recommended (high demand from employers)"
    ],
    "for_companies": [
      "Talent shortage expected (55,000 gap by Nov 2026)",
      "Proactive hiring recommended (before competition intensifies)",
      "Salaries likely to increase 9% - budget accordingly",
      "Consider internal training programs (upskill existing employees)"
    ]
  },
  
  "risk_factors": [
    {
      "risk_type": "automation",
      "description": "Some basic Python tasks may be automated by AI coding assistants",
      "probability": 0.30,
      "impact_severity": "low"
    },
    {
      "risk_type": "technological",
      "description": "New programming languages (Rust, Go) gaining traction",
      "probability": 0.25,
      "impact_severity": "low"
    }
  ],
  
  "ml_metadata": {
    "model_version": "1.2.3",
    "model_type": "ensemble_time_series",
    "training_date": "2026-02-20T00:00:00Z",
    "feature_importance": {
      "job_posting_growth": 0.35,
      "course_enrollment_trend": 0.28,
      "salary_growth": 0.22,
      "skill_search_volume": 0.15
    },
    "prediction_confidence": 0.87,
    "historical_accuracy": 0.91
  },
  
  "data_freshness": {
    "data_as_of": "2026-02-25T06:00:00Z",
    "data_lag_hours": 4,
    "data_completeness": 0.98,
    "external_data_included": false
  },
  
  "audit_metadata": {
    "audit_id": "660e8400-e29b-41d4-a716-446655440001",
    "agent_version": "1.0.0",
    "data_sources": [
      "jobs_table_2024_2026",
      "skills_aggregation_v3_2",
      "salary_index_feb_2026"
    ],
    "computation_time_ms": 1456,
    "anomaly_flags": []
  }
}
```

---

**END OF SPECIFICATION**

**Version:** 1.0.0  
**Status:** SEALED & LOCKED  
**Authority:** MASTER_AGENT_CREATION_PROMPT v1.0  
**Platform:** Ecoskiller Antigravity  
**Compliance:** GDPR + DPDP + Fair Labor Standards + Enterprise SaaS  
**Business Impact:** Career Guidance + Skill Development + Workforce Planning  

**This document is the single source of truth for MARKET_DEMAND_PREDICTION_AGENT implementation.**

**Agent enables:** Proactive career planning, data-driven curriculum design, smart skill development, employer hiring intelligence, platform growth through market insights.
