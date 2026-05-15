# 🏆 DIGITAL BADGE ISSUANCE AGENT — ANTIGRAVITY v1.0

**Artifact Class:** Production System Blueprint  
**Mutation Policy:** Add-only via version bump  
**Execution Mode:** Deterministic  
**Stack Lock:** Enforced  
**Interface Freeze:** Required  
**Domain:** Program & Event Management / Gamification  
**Subsystem:** Digital Badge Infrastructure  
**Standard Compliance:** Open Badges 3.0 (IMS Global)  

---

## 🔒 SECTION A — AGENT IDENTITY & AUTHORITY

### Agent Classification
```
Agent Name: DIGITAL_BADGE_ISSUANCE_AGENT
Agent Type: Autonomous Gamification & Achievement Engine
Execution Mode: Event-Driven + Rule-Based
Determinism Rule: Identical criteria → Identical badge award
Failure Mode: STOP → REPORT → NO PARTIAL BADGE
Human Override: Admin Revocation Only
AI Override: FORBIDDEN
Verification Standard: Open Badges 3.0 Compatible
```

### Authority Boundaries
```
PERMITTED:
✓ Badge award upon criteria satisfaction
✓ Badge criteria evaluation (automated)
✓ Badge metadata generation
✓ SVG/PNG badge image rendering
✓ Open Badges assertion creation
✓ Baked badge generation (embedded metadata)
✓ Badge verification endpoint serving
✓ Badge revocation processing
✓ Badge progression tracking
✓ Badge display orchestration
✓ Badge sharing facilitation

FORBIDDEN:
✗ Criteria modification post-creation
✗ Badge award without audit trail
✗ Backdating badge issuance
✗ Manual badge number assignment
✗ Bypassing anti-gaming checks
✗ Badge duplication for same achievement
✗ Criteria evaluation without event evidence
```

---

## 🔒 SECTION B — BADGE TAXONOMY (LOCKED)

### Badge Categories (Non-Negotiable)
```
1. ACHIEVEMENT BADGES (Performance-Based)
   - Skill Mastery Badges
   - Learning Milestone Badges
   - Performance Excellence Badges
   - Competition Winner Badges
   - Tournament Champion Badges

2. PROGRESSION BADGES (Journey-Based)
   - Level Advancement Badges (Bronze → Silver → Gold → Platinum)
   - XP Milestone Badges
   - Streak Milestone Badges
   - Belt Progression Badges (Dojo)
   - Course Completion Badges

3. PARTICIPATION BADGES (Engagement-Based)
   - Event Attendance Badges
   - Workshop Participation Badges
   - Community Contribution Badges
   - Challenge Participation Badges
   - Daily Login Streak Badges

4. REPUTATION BADGES (Trust-Based)
   - Verified Student Badge
   - Verified Trainer Badge
   - Verified Recruiter Badge
   - Community Leader Badge
   - Top Contributor Badge
   - Trusted Mentor Badge

5. INNOVATION BADGES (Creation-Based)
   - Idea Submission Badge
   - Innovation Licensed Badge
   - Patent Filed Badge
   - Creative Excellence Badge
   - Problem Solver Badge

6. SOCIAL BADGES (Network-Based)
   - Referral Champion Badge (5, 10, 25, 50, 100 referrals)
   - Influencer Badge (follower thresholds)
   - Collaboration Master Badge
   - Mentor of the Month Badge
   - Community Builder Badge

7. SPECIALIZATION BADGES (Domain-Based)
   - Subject Matter Expert Badge
   - Skill Domain Mastery Badge
   - Industry Specialist Badge
   - Tool Proficiency Badge
   - Technology Stack Badge

8. BEHAVIORAL BADGES (Conduct-Based)
   - Consistent Learner Badge
   - Helpful Community Member Badge
   - Quality Content Creator Badge
   - Ethical Practice Badge
   - Resilience Badge (comeback after failure)

9. TEMPORAL BADGES (Time-Based)
   - Early Adopter Badge
   - Founding Member Badge
   - Anniversary Badge (1Y, 2Y, 5Y, 10Y)
   - Season Champion Badge
   - Monthly Active Badge

10. RARE/SECRET BADGES (Discovery-Based)
    - Easter Egg Finder Badge
    - Platform Explorer Badge
    - Hidden Achievement Badge
    - Unicorn Badge (rare combinations)
    - Legend Status Badge
```

---

## 🔒 SECTION C — DATA MODEL (IMMUTABLE SCHEMA)

### Core Badge Definition Entity
```sql
CREATE TABLE badge_definitions (
    badge_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    badge_code VARCHAR(50) UNIQUE NOT NULL, -- Format: BADGE-{CATEGORY}-{NAME}
    badge_name VARCHAR(255) NOT NULL,
    badge_description TEXT NOT NULL,
    badge_category VARCHAR(50) NOT NULL, -- From taxonomy
    
    -- Badge Visuals
    badge_image_svg TEXT NOT NULL, -- Inline SVG or path
    badge_image_png TEXT NOT NULL, -- PNG path for compatibility
    badge_icon_emoji VARCHAR(10), -- Optional emoji representation
    badge_color_hex VARCHAR(7) NOT NULL, -- Primary badge color
    badge_tier VARCHAR(20), -- bronze, silver, gold, platinum, diamond
    
    -- Criteria Definition
    criteria_type VARCHAR(50) NOT NULL, -- single_event, accumulative, composite, time_bound
    criteria_rule JSONB NOT NULL, -- Rule engine expression
    criteria_threshold INTEGER, -- For accumulative badges
    criteria_time_window_days INTEGER, -- For time-bound criteria
    
    -- Progression & Stacking
    is_stackable BOOLEAN DEFAULT FALSE, -- Can be earned multiple times
    max_stack_count INTEGER, -- Max times earnable (NULL = unlimited)
    progression_tier INTEGER, -- 1, 2, 3... for tiered badges
    parent_badge_id UUID REFERENCES badge_definitions(badge_id), -- For progression chains
    
    -- Rarity & Gamification
    rarity_level VARCHAR(20) NOT NULL, -- common, uncommon, rare, epic, legendary
    points_value INTEGER NOT NULL DEFAULT 0, -- XP awarded upon earning
    is_secret BOOLEAN DEFAULT FALSE, -- Hidden until earned
    is_retired BOOLEAN DEFAULT FALSE, -- No longer earnable
    
    -- Display & Sharing
    display_priority INTEGER DEFAULT 0, -- Higher = shown first
    is_public BOOLEAN DEFAULT TRUE, -- Visible on public profile
    share_template_text TEXT, -- Default social share text
    
    -- Anti-Gaming
    cooldown_period_days INTEGER, -- Minimum time between re-earning
    fraud_check_enabled BOOLEAN DEFAULT TRUE,
    manual_approval_required BOOLEAN DEFAULT FALSE, -- Requires admin approval
    
    -- Open Badges 3.0 Compliance
    issuer_id UUID NOT NULL REFERENCES organizations(org_id),
    issuer_name VARCHAR(255) NOT NULL,
    issuer_url TEXT NOT NULL, -- Issuer homepage
    alignment JSONB, -- Educational alignment (optional)
    tags TEXT[], -- Keywords for discovery
    
    -- Metadata
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by UUID NOT NULL REFERENCES users(user_id),
    version INTEGER NOT NULL DEFAULT 1,
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_category CHECK (badge_category IN (
        'achievement', 'progression', 'participation', 'reputation',
        'innovation', 'social', 'specialization', 'behavioral',
        'temporal', 'rare_secret'
    )),
    CONSTRAINT valid_tier CHECK (badge_tier IN (
        'bronze', 'silver', 'gold', 'platinum', 'diamond', NULL
    )),
    CONSTRAINT valid_rarity CHECK (rarity_level IN (
        'common', 'uncommon', 'rare', 'epic', 'legendary'
    ))
);

-- Indexes
CREATE INDEX idx_badge_category ON badge_definitions(badge_category, is_retired);
CREATE INDEX idx_badge_tier ON badge_definitions(badge_tier);
CREATE INDEX idx_badge_rarity ON badge_definitions(rarity_level);
CREATE INDEX idx_badge_tenant ON badge_definitions(tenant_id);
```

### Badge Award Entity (User Achievement)
```sql
CREATE TABLE badge_awards (
    award_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    badge_id UUID NOT NULL REFERENCES badge_definitions(badge_id),
    recipient_id UUID NOT NULL REFERENCES users(user_id),
    
    -- Award Context
    awarded_at TIMESTAMP NOT NULL DEFAULT NOW(),
    award_trigger_event_id UUID, -- References event that triggered award
    award_trigger_event_type VARCHAR(50), -- course_completed, match_won, etc.
    
    -- Open Badges Assertion
    assertion_id UUID UNIQUE NOT NULL DEFAULT gen_random_uuid(),
    assertion_json JSONB NOT NULL, -- Full Open Badges 3.0 assertion
    baked_badge_url TEXT, -- URL to baked badge image
    verification_url TEXT NOT NULL, -- Public verification endpoint
    
    -- Metadata Capture
    achievement_data JSONB, -- Specific achievement context (score, rank, etc.)
    evidence_url TEXT, -- Link to evidence (project, certificate, etc.)
    narrative TEXT, -- Optional achievement story
    
    -- Status
    status VARCHAR(50) NOT NULL DEFAULT 'active', -- active, revoked, expired
    revocation_reason TEXT,
    revoked_at TIMESTAMP,
    revoked_by UUID REFERENCES users(user_id),
    expires_at TIMESTAMP, -- For time-limited badges
    
    -- Stack Count (for stackable badges)
    stack_position INTEGER DEFAULT 1, -- 1st, 2nd, 3rd time earning
    
    -- Display & Sharing
    is_featured BOOLEAN DEFAULT FALSE, -- Pinned to profile
    visibility VARCHAR(20) DEFAULT 'public', -- public, connections, private
    shared_count INTEGER DEFAULT 0, -- Times shared externally
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_status CHECK (status IN ('active', 'revoked', 'expired')),
    CONSTRAINT valid_visibility CHECK (visibility IN ('public', 'connections', 'private')),
    
    -- Uniqueness: Prevent duplicate awards for non-stackable badges
    CONSTRAINT unique_non_stackable_award UNIQUE NULLS NOT DISTINCT (
        badge_id, recipient_id, 
        CASE WHEN (SELECT is_stackable FROM badge_definitions WHERE badge_id = badge_awards.badge_id) = FALSE 
        THEN TRUE ELSE NULL END
    )
);

-- Indexes
CREATE INDEX idx_badge_awards_recipient ON badge_awards(recipient_id, awarded_at DESC);
CREATE INDEX idx_badge_awards_badge ON badge_awards(badge_id, status);
CREATE INDEX idx_badge_awards_assertion ON badge_awards(assertion_id);
CREATE INDEX idx_badge_awards_status ON badge_awards(status, awarded_at DESC);
CREATE INDEX idx_badge_awards_tenant ON badge_awards(tenant_id);
```

### Badge Criteria Evaluation Log
```sql
CREATE TABLE badge_criteria_evaluation_log (
    evaluation_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    badge_id UUID NOT NULL REFERENCES badge_definitions(badge_id),
    user_id UUID NOT NULL REFERENCES users(user_id),
    
    -- Evaluation Context
    evaluated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    trigger_event_id UUID,
    trigger_event_type VARCHAR(50),
    
    -- Evaluation Result
    criteria_met BOOLEAN NOT NULL,
    criteria_result JSONB NOT NULL, -- Detailed evaluation breakdown
    threshold_value NUMERIC,
    actual_value NUMERIC,
    
    -- Award Outcome
    badge_awarded BOOLEAN DEFAULT FALSE,
    award_id UUID REFERENCES badge_awards(award_id),
    award_blocked_reason TEXT, -- If criteria met but award blocked (cooldown, fraud, etc.)
    
    -- Audit
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id)
);

CREATE INDEX idx_criteria_eval_badge ON badge_criteria_evaluation_log(badge_id, evaluated_at DESC);
CREATE INDEX idx_criteria_eval_user ON badge_criteria_evaluation_log(user_id, evaluated_at DESC);
CREATE INDEX idx_criteria_eval_result ON badge_criteria_evaluation_log(criteria_met, badge_awarded);
```

### Badge Progress Tracker (For Accumulative Badges)
```sql
CREATE TABLE badge_progress (
    progress_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    badge_id UUID NOT NULL REFERENCES badge_definitions(badge_id),
    user_id UUID NOT NULL REFERENCES users(user_id),
    
    -- Progress Metrics
    current_value NUMERIC NOT NULL DEFAULT 0,
    target_value NUMERIC NOT NULL,
    progress_percentage NUMERIC GENERATED ALWAYS AS ((current_value / NULLIF(target_value, 0)) * 100) STORED,
    
    -- Timestamps
    started_at TIMESTAMP NOT NULL DEFAULT NOW(),
    last_updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    projected_completion_date TIMESTAMP, -- ML prediction (optional)
    
    -- Status
    is_completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP,
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    CONSTRAINT unique_user_badge_progress UNIQUE (badge_id, user_id, tenant_id)
);

CREATE INDEX idx_badge_progress_user ON badge_progress(user_id, is_completed);
CREATE INDEX idx_badge_progress_badge ON badge_progress(badge_id);
```

### Badge Collection (User Badge Showcase)
```sql
CREATE TABLE badge_collections (
    collection_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    collection_name VARCHAR(255) NOT NULL,
    collection_description TEXT,
    
    -- Collection Contents
    badge_award_ids UUID[] NOT NULL, -- Array of award_ids
    display_order INTEGER[] NOT NULL, -- Corresponding display order
    
    -- Visibility
    is_public BOOLEAN DEFAULT TRUE,
    is_featured BOOLEAN DEFAULT FALSE, -- Featured on profile
    
    -- Metadata
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id)
);

CREATE INDEX idx_badge_collections_user ON badge_collections(user_id);
```

---

## 🔒 SECTION D — BADGE CRITERIA ENGINE (RULE SYSTEM)

### Criteria Types & Formats

#### 1. SINGLE EVENT CRITERIA
```json
{
  "criteria_type": "single_event",
  "event_type": "course_completed",
  "conditions": {
    "course_id": "uuid-of-specific-course",
    "min_grade": 80
  }
}
```

#### 2. ACCUMULATIVE CRITERIA
```json
{
  "criteria_type": "accumulative",
  "metric": "courses_completed",
  "threshold": 10,
  "time_window_days": null,
  "conditions": {
    "min_grade_per_course": 70,
    "category": "programming"
  }
}
```

#### 3. COMPOSITE CRITERIA (AND logic)
```json
{
  "criteria_type": "composite",
  "operator": "AND",
  "sub_criteria": [
    {
      "metric": "courses_completed",
      "threshold": 5
    },
    {
      "metric": "projects_submitted",
      "threshold": 3
    },
    {
      "metric": "peer_endorsements",
      "threshold": 10
    }
  ]
}
```

#### 4. TIME-BOUND CRITERIA
```json
{
  "criteria_type": "time_bound",
  "metric": "consecutive_login_days",
  "threshold": 30,
  "time_window_days": 30,
  "reset_on_miss": true
}
```

#### 5. PERCENTILE CRITERIA (Competitive)
```json
{
  "criteria_type": "percentile",
  "metric": "total_xp",
  "percentile": 95,
  "comparison_scope": "institution",
  "time_window_days": 90
}
```

#### 6. STREAK CRITERIA
```json
{
  "criteria_type": "streak",
  "action": "daily_login",
  "streak_length": 7,
  "allow_freeze_tokens": false
}
```

#### 7. ACHIEVEMENT COMBO CRITERIA
```json
{
  "criteria_type": "achievement_combo",
  "required_badges": [
    "BADGE-ACH-MASTER-001",
    "BADGE-ACH-MASTER-002",
    "BADGE-ACH-MASTER-003"
  ],
  "all_required": true
}
```

### Criteria Evaluation Engine (Pseudocode)
```python
def evaluate_badge_criteria(badge_definition, user_id, trigger_event):
    """
    Deterministic badge criteria evaluation
    Returns: (criteria_met: bool, evaluation_details: dict)
    """
    
    criteria_type = badge_definition.criteria_type
    criteria_rule = badge_definition.criteria_rule
    
    # ANTI-GAMING CHECKS (Pre-Evaluation)
    if badge_definition.cooldown_period_days:
        last_award = get_last_award(badge_definition.badge_id, user_id)
        if last_award and days_since(last_award) < badge_definition.cooldown_period_days:
            return (False, {"blocked_reason": "cooldown_active"})
    
    if badge_definition.fraud_check_enabled:
        fraud_score = detect_fraud_signals(user_id, badge_definition.badge_id)
        if fraud_score > FRAUD_THRESHOLD:
            return (False, {"blocked_reason": "fraud_detected", "fraud_score": fraud_score})
    
    # CRITERIA EVALUATION
    if criteria_type == "single_event":
        return evaluate_single_event(criteria_rule, trigger_event)
    
    elif criteria_type == "accumulative":
        current_value = get_metric_value(user_id, criteria_rule.metric, criteria_rule.conditions)
        threshold = criteria_rule.threshold
        met = current_value >= threshold
        return (met, {
            "current_value": current_value,
            "threshold": threshold,
            "progress_percentage": (current_value / threshold) * 100
        })
    
    elif criteria_type == "composite":
        results = []
        for sub_criteria in criteria_rule.sub_criteria:
            sub_met, sub_details = evaluate_sub_criteria(user_id, sub_criteria)
            results.append(sub_met)
        
        if criteria_rule.operator == "AND":
            met = all(results)
        elif criteria_rule.operator == "OR":
            met = any(results)
        
        return (met, {"sub_criteria_results": results})
    
    elif criteria_type == "time_bound":
        return evaluate_time_bound(user_id, criteria_rule)
    
    elif criteria_type == "percentile":
        return evaluate_percentile(user_id, criteria_rule)
    
    elif criteria_type == "streak":
        return evaluate_streak(user_id, criteria_rule)
    
    elif criteria_type == "achievement_combo":
        user_badges = get_user_badge_codes(user_id)
        required_badges = criteria_rule.required_badges
        
        if criteria_rule.all_required:
            met = all(badge in user_badges for badge in required_badges)
        else:
            met = any(badge in user_badges for badge in required_badges)
        
        return (met, {
            "required": required_badges,
            "earned": [b for b in required_badges if b in user_badges]
        })
    
    else:
        raise ValueError(f"Unknown criteria_type: {criteria_type}")


def award_badge_if_criteria_met(badge_definition, user_id, trigger_event):
    """
    Orchestrate evaluation and award
    """
    # 1. Evaluate criteria
    criteria_met, evaluation_details = evaluate_badge_criteria(
        badge_definition, user_id, trigger_event
    )
    
    # 2. Log evaluation
    log_criteria_evaluation(
        badge_id=badge_definition.badge_id,
        user_id=user_id,
        criteria_met=criteria_met,
        evaluation_details=evaluation_details,
        trigger_event=trigger_event
    )
    
    # 3. Award if met
    if criteria_met:
        # Check for duplicates (non-stackable)
        if not badge_definition.is_stackable:
            existing_award = get_existing_award(badge_definition.badge_id, user_id)
            if existing_award:
                return {"status": "already_awarded", "award_id": existing_award.award_id}
        
        # Create badge award
        award = create_badge_award(
            badge_id=badge_definition.badge_id,
            recipient_id=user_id,
            trigger_event=trigger_event,
            achievement_data=evaluation_details
        )
        
        # Emit event
        emit_event("badge.awarded", {
            "award_id": award.award_id,
            "badge_id": badge_definition.badge_id,
            "recipient_id": user_id,
            "badge_name": badge_definition.badge_name,
            "points_awarded": badge_definition.points_value
        })
        
        return {"status": "awarded", "award_id": award.award_id}
    
    else:
        return {"status": "criteria_not_met", "details": evaluation_details}
```

---

## 🔒 SECTION E — OPEN BADGES 3.0 COMPLIANCE

### Open Badges Assertion Structure
```json
{
  "@context": "https://w3id.org/openbadges/v3",
  "type": "Achievement",
  "id": "https://antigravity.com/badges/assertions/{assertion_id}",
  "name": "{badge_name}",
  "description": "{badge_description}",
  "image": {
    "id": "https://antigravity.com/badges/images/{badge_id}.png",
    "type": "Image"
  },
  "criteria": {
    "narrative": "{human_readable_criteria}"
  },
  "issuer": {
    "id": "https://antigravity.com/issuer",
    "type": "Profile",
    "name": "Antigravity SaaS Platform",
    "url": "https://antigravity.com",
    "email": "badges@antigravity.com"
  },
  "credentialSubject": {
    "id": "did:example:{recipient_id}",
    "type": "AchievementSubject",
    "achievement": {
      "id": "https://antigravity.com/badges/{badge_id}",
      "type": "Achievement",
      "name": "{badge_name}",
      "description": "{badge_description}",
      "criteria": {
        "narrative": "{criteria_description}"
      },
      "achievementType": "{badge_category}"
    }
  },
  "issuanceDate": "2026-03-04T15:30:00Z",
  "expirationDate": "2027-03-04T15:30:00Z",
  "proof": {
    "type": "Ed25519Signature2020",
    "created": "2026-03-04T15:30:00Z",
    "verificationMethod": "https://antigravity.com/issuer#key-1",
    "proofPurpose": "assertionMethod",
    "proofValue": "{signature}"
  },
  "evidence": [
    {
      "id": "https://antigravity.com/evidence/{evidence_id}",
      "type": "Evidence",
      "name": "{evidence_name}",
      "description": "{evidence_description}",
      "narrative": "{achievement_story}"
    }
  ]
}
```

### Baked Badge Generation
```python
def create_baked_badge(badge_award, badge_definition):
    """
    Generate a PNG image with embedded Open Badges assertion metadata
    """
    
    # 1. Load base badge image
    base_image = load_png(badge_definition.badge_image_png)
    
    # 2. Create Open Badges assertion JSON
    assertion_json = generate_open_badges_assertion(badge_award, badge_definition)
    
    # 3. Embed assertion in PNG metadata (iTXt chunk)
    baked_image = embed_metadata_in_png(
        image=base_image,
        metadata_key="openbadges",
        metadata_value=json.dumps(assertion_json)
    )
    
    # 4. Upload to storage
    baked_badge_url = upload_to_storage(
        file=baked_image,
        path=f"/badges/baked/{badge_award.assertion_id}.png"
    )
    
    # 5. Update badge_award record
    update_badge_award(
        award_id=badge_award.award_id,
        baked_badge_url=baked_badge_url,
        assertion_json=assertion_json
    )
    
    return baked_badge_url
```

### Verification Endpoint
```
GET /api/v1/badges/verify/{assertion_id}

Response (Valid Badge):
{
  "status": "valid",
  "badge_name": "Course Completion - Python Fundamentals",
  "recipient_name": "John Doe",
  "recipient_id": "uuid",
  "issued_date": "2026-03-01T10:00:00Z",
  "issuer_name": "Antigravity",
  "criteria": "Successfully completed Python Fundamentals course with 85% grade",
  "evidence_url": "https://antigravity.com/certificates/cert-123",
  "open_badges_assertion": { ... },
  "verification_timestamp": "2026-03-04T15:30:00Z"
}

Response (Revoked Badge):
{
  "status": "revoked",
  "badge_name": "Course Completion - Python Fundamentals",
  "revocation_reason": "Course completion invalidated due to plagiarism",
  "revoked_date": "2026-03-03T12:00:00Z"
}

Response (Not Found):
{
  "status": "not_found",
  "message": "No badge found with this assertion ID"
}
```

---

## 🔒 SECTION F — BADGE AWARD PIPELINE (DETERMINISTIC)

### Pipeline Stages (Non-Negotiable Order)

```
STAGE 1: EVENT INGESTION
├─ Input: User action event from event bus
├─ Examples: course_completed, match_won, login_streak, referral_accepted
├─ Validation: Event schema valid, user_id exists, tenant_id matches
└─ Output: Normalized event object

STAGE 2: BADGE CANDIDATE DISCOVERY
├─ Query: Find all active badges where criteria could be triggered by this event
├─ Filter: Badge not retired, tenant matches, user eligible
└─ Output: List of candidate badges to evaluate

STAGE 3: CRITERIA EVALUATION (Per Candidate)
├─ Load badge criteria_rule
├─ Execute evaluation logic (see Section D)
├─ Apply anti-gaming checks (cooldown, fraud detection)
├─ Log evaluation result
└─ Output: (criteria_met: bool, evaluation_details: dict)

STAGE 4: DUPLICATE PREVENTION
├─ For non-stackable badges: Check if user already has this badge
├─ For stackable badges: Check max_stack_count not exceeded
└─ Output: Award allowed (bool)

STAGE 5: MANUAL APPROVAL GATE (If Required)
├─ If badge.manual_approval_required == true:
│  ├─ Create pending approval request
│  ├─ Notify admin/mentor
│  └─ PAUSE pipeline (resume on approval)
└─ Else: Proceed to award

STAGE 6: BADGE AWARD CREATION
├─ Generate assertion_id (UUID)
├─ Create Open Badges 3.0 assertion JSON
├─ Insert badge_awards record
├─ Update badge_progress (mark completed if tracking)
└─ Output: badge_award object

STAGE 7: BAKED BADGE GENERATION
├─ Generate baked PNG with embedded metadata
├─ Upload to MinIO
├─ Update award record with baked_badge_url
└─ Output: baked_badge_url

STAGE 8: XP & POINTS AWARD
├─ Award badge.points_value XP to user
├─ Update user XP total
├─ Check for level-up (if XP threshold crossed)
└─ Emit xp.awarded event

STAGE 9: NOTIFICATION ORCHESTRATION
├─ Send in-app notification ("🏆 Badge Earned!")
├─ Send email notification with badge image
├─ Send push notification (mobile)
├─ Display badge unlock animation (frontend)
└─ Log notification delivery status

STAGE 10: EVENT EMISSION
├─ Emit badge.awarded event to event bus
├─ Payload: award_id, badge_id, recipient_id, points_awarded
├─ Consumers: Analytics, Leaderboard, Social Feed
└─ Output: Event published

STAGE 11: SOCIAL SHARING TRIGGER
├─ Suggest social share to user
├─ Pre-populate share text (badge.share_template_text)
├─ Generate shareable badge card image
└─ Track share count if shared
```

### Failure Handling (Per Stage)
```
ANY STAGE FAILURE:
→ STOP PIPELINE
→ ROLLBACK PARTIAL DATA
→ LOG FAILURE REASON
→ EMIT badge.award_failed EVENT
→ NOTIFY ADMIN (if critical failure)
→ NO PARTIAL BADGE CREATED
```

---

## 🔒 SECTION G — BADGE VISUAL DESIGN SYSTEM

### Badge Image Requirements
```
FORMAT: SVG (primary) + PNG (fallback)
SIZE: 512x512 pixels (PNG), scalable (SVG)
COLOR DEPTH: 24-bit RGB
TRANSPARENCY: Alpha channel required
FILE SIZE: Max 500 KB (SVG), Max 200 KB (PNG)

DESIGN ELEMENTS:
- Outer ring/border (color indicates tier)
- Icon/symbol (represents achievement)
- Background pattern (optional)
- Rarity indicator (corner badge or glow)
- Tier indicator (bronze/silver/gold badge)

COLOR PALETTE BY TIER:
- Bronze: #CD7F32
- Silver: #C0C0C0
- Gold: #FFD700
- Platinum: #E5E4E2
- Diamond: #B9F2FF

RARITY VISUAL INDICATORS:
- Common: No glow
- Uncommon: Green glow
- Rare: Blue glow
- Epic: Purple glow
- Legendary: Orange/gold glow + particle effects
```

### SVG Template Structure
```xml
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" width="512" height="512">
  <!-- Background -->
  <circle cx="256" cy="256" r="240" fill="{tier_color}" opacity="0.2"/>
  
  <!-- Outer Ring -->
  <circle cx="256" cy="256" r="240" fill="none" stroke="{tier_color}" stroke-width="8"/>
  
  <!-- Inner Background -->
  <circle cx="256" cy="256" r="200" fill="#ffffff"/>
  
  <!-- Icon (Achievement Symbol) -->
  <g id="icon">
    <!-- Custom SVG path for achievement icon -->
    <path d="..." fill="{primary_color}"/>
  </g>
  
  <!-- Rarity Glow (if applicable) -->
  <defs>
    <radialGradient id="glow">
      <stop offset="0%" stop-color="{rarity_color}" stop-opacity="0.8"/>
      <stop offset="100%" stop-color="{rarity_color}" stop-opacity="0"/>
    </radialGradient>
  </defs>
  <circle cx="256" cy="256" r="250" fill="url(#glow)" opacity="0.5"/>
  
  <!-- Tier Badge (Corner) -->
  <g id="tier-indicator">
    <circle cx="420" cy="92" r="50" fill="{tier_color}"/>
    <text x="420" y="105" text-anchor="middle" font-size="24" fill="#fff" font-weight="bold">
      {tier_initial}
    </text>
  </g>
</svg>
```

### Dynamic Badge Generation
```python
def generate_badge_image(badge_definition):
    """
    Dynamically generate badge SVG and PNG from template
    """
    
    # Load template
    template_svg = load_svg_template(badge_definition.badge_category)
    
    # Apply substitutions
    svg_content = template_svg.replace("{tier_color}", get_tier_color(badge_definition.badge_tier))
    svg_content = svg_content.replace("{primary_color}", badge_definition.badge_color_hex)
    svg_content = svg_content.replace("{rarity_color}", get_rarity_color(badge_definition.rarity_level))
    svg_content = svg_content.replace("{tier_initial}", badge_definition.badge_tier[0].upper())
    
    # Insert icon path
    icon_svg = get_icon_svg(badge_definition.badge_code)
    svg_content = svg_content.replace("<!-- Custom SVG path for achievement icon -->", icon_svg)
    
    # Save SVG
    svg_path = f"/badges/svg/{badge_definition.badge_id}.svg"
    save_file(svg_path, svg_content)
    
    # Convert to PNG
    png_path = f"/badges/png/{badge_definition.badge_id}.png"
    convert_svg_to_png(svg_path, png_path, size=512)
    
    return {
        "svg_path": svg_path,
        "png_path": png_path
    }
```

---

## 🔒 SECTION H — ANTI-GAMING & FRAUD PREVENTION

### Anti-Gaming Mechanisms
```
1. COOLDOWN PERIODS
   - Prevent rapid re-earning of stackable badges
   - Minimum time between awards (e.g., 24 hours)
   - Configured per badge (cooldown_period_days)

2. VELOCITY CHECKS
   - Detect suspiciously fast achievement accumulation
   - Flag if user earns >5 badges in <1 hour
   - Trigger manual review

3. PATTERN DETECTION
   - Detect coordinated badge farming (multiple accounts)
   - Identify click farms or bot activity
   - Cross-reference IP addresses and device fingerprints

4. PEER VALIDATION (for social badges)
   - Referral badges: Require referred user activity (not just signup)
   - Endorsement badges: Verify endorser authenticity
   - Collaboration badges: Verify mutual contribution

5. ACTIVITY AUTHENTICITY
   - Time-on-task validation (e.g., course completion must exceed min duration)
   - Quiz/assessment timestamps checked
   - Match duration and engagement verified

6. RATE LIMITING
   - Max badges earnable per user per day: 10
   - Max same-badge re-awards per user per month: 5 (if stackable)

7. MANUAL REVIEW QUEUE
   - High-value badges (>1000 XP) require admin approval
   - Rare/legendary badges flagged for review
   - Appeals process for revoked badges
```

### Fraud Detection Algorithm
```python
def detect_fraud_signals(user_id, badge_id):
    """
    Return fraud score (0-100, higher = more suspicious)
    """
    
    fraud_score = 0
    
    # Signal 1: Rapid badge accumulation
    recent_badges = get_badges_earned_last_24h(user_id)
    if recent_badges > 5:
        fraud_score += 20
    
    # Signal 2: New account (< 7 days old)
    account_age_days = get_account_age_days(user_id)
    if account_age_days < 7:
        fraud_score += 15
    
    # Signal 3: No profile completion
    profile_completion = get_profile_completion_percentage(user_id)
    if profile_completion < 30:
        fraud_score += 10
    
    # Signal 4: Suspicious referral pattern
    referral_pattern_suspicious = check_referral_pattern(user_id)
    if referral_pattern_suspicious:
        fraud_score += 25
    
    # Signal 5: Device fingerprint flagged
    device_flagged = check_device_blacklist(user_id)
    if device_flagged:
        fraud_score += 30
    
    # Signal 6: Abnormal activity hours (e.g., 3 AM activity every day)
    abnormal_hours = check_activity_hours(user_id)
    if abnormal_hours:
        fraud_score += 15
    
    # Signal 7: Badge farming pattern (same badge repeatedly)
    badge_repeat_count = get_badge_repeat_count(user_id, badge_id)
    if badge_repeat_count > 3:
        fraud_score += 20
    
    return min(fraud_score, 100)  # Cap at 100


FRAUD_THRESHOLD = 50  # Scores above this block badge award
```

---

## 🔒 SECTION I — GAMIFICATION MECHANICS INTEGRATION

### XP & Level System Integration
```sql
CREATE TABLE user_xp (
    user_id UUID PRIMARY KEY REFERENCES users(user_id),
    total_xp BIGINT NOT NULL DEFAULT 0,
    current_level INTEGER NOT NULL DEFAULT 1,
    xp_to_next_level INTEGER NOT NULL,
    
    -- Leaderboard
    institution_rank INTEGER,
    global_rank INTEGER,
    
    -- Metadata
    level_updated_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id)
);

CREATE TABLE level_definitions (
    level_number INTEGER PRIMARY KEY,
    xp_required BIGINT NOT NULL,
    level_name VARCHAR(50),
    unlocked_features TEXT[],
    badge_reward_id UUID REFERENCES badge_definitions(badge_id),
    
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id)
);

-- Example level progression:
-- Level 1: 0 XP
-- Level 2: 100 XP
-- Level 3: 250 XP
-- Level 4: 500 XP
-- Level 5: 1000 XP
-- Formula: xp_required = level^2 * 50
```

### Level-Up Badge Awards
```python
def check_level_up(user_id, xp_awarded):
    """
    Check if XP award triggers level up and associated badge
    """
    
    # Get current user XP
    user_xp = get_user_xp(user_id)
    new_total_xp = user_xp.total_xp + xp_awarded
    
    # Update XP
    update_user_xp(user_id, new_total_xp)
    
    # Check for level up
    next_level = user_xp.current_level + 1
    next_level_def = get_level_definition(next_level)
    
    if new_total_xp >= next_level_def.xp_required:
        # LEVEL UP!
        update_user_level(user_id, next_level)
        
        # Award level-up badge (if defined)
        if next_level_def.badge_reward_id:
            award_badge(
                badge_id=next_level_def.badge_reward_id,
                user_id=user_id,
                trigger_event_type="level_up",
                achievement_data={"level_reached": next_level}
            )
        
        # Emit event
        emit_event("user.level_up", {
            "user_id": user_id,
            "old_level": user_xp.current_level,
            "new_level": next_level,
            "total_xp": new_total_xp
        })
        
        # Unlock features
        unlock_features(user_id, next_level_def.unlocked_features)
```

### Streak System Integration
```sql
CREATE TABLE user_streaks (
    user_id UUID PRIMARY KEY REFERENCES users(user_id),
    
    -- Streak Metrics
    current_streak_days INTEGER NOT NULL DEFAULT 0,
    longest_streak_days INTEGER NOT NULL DEFAULT 0,
    
    -- Last Activity
    last_activity_date DATE NOT NULL,
    
    -- Freeze Tokens
    freeze_tokens_available INTEGER DEFAULT 0,
    freeze_tokens_used INTEGER DEFAULT 0,
    
    -- Metadata
    streak_started_at TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id)
);

-- Streak milestone badges
INSERT INTO badge_definitions (badge_code, badge_name, criteria_rule) VALUES
('BADGE-STK-007', '7-Day Streak', '{"criteria_type": "streak", "streak_length": 7}'),
('BADGE-STK-030', '30-Day Streak', '{"criteria_type": "streak", "streak_length": 30}'),
('BADGE-STK-100', '100-Day Streak', '{"criteria_type": "streak", "streak_length": 100}');
```

### Leaderboard Integration
```sql
CREATE TABLE leaderboards (
    leaderboard_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    leaderboard_type VARCHAR(50) NOT NULL, -- global, institution, skill, badge_collectors
    leaderboard_scope VARCHAR(50), -- institution_id, skill_id, etc.
    
    -- Time Period
    period_type VARCHAR(20) NOT NULL, -- daily, weekly, monthly, all_time
    period_start_date DATE NOT NULL,
    period_end_date DATE,
    
    -- Rankings (JSON array of user rankings)
    rankings JSONB NOT NULL,
    
    -- Metadata
    last_updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id)
);

-- Leaderboard ranking structure
{
  "rankings": [
    {
      "rank": 1,
      "user_id": "uuid",
      "user_name": "Alice",
      "score": 15000,
      "badges_earned": 45,
      "level": 12
    },
    {
      "rank": 2,
      "user_id": "uuid",
      "user_name": "Bob",
      "score": 14500,
      "badges_earned": 42,
      "level": 11
    }
  ]
}
```

---

## 🔒 SECTION J — EVENT-DRIVEN ARCHITECTURE

### Incoming Events (Badge Triggers)
```
EVENT: course.completed
PAYLOAD: {user_id, course_id, completion_date, grade, duration_minutes}
TRIGGERS: Course completion badges, learning milestone badges

EVENT: dojo.match_completed
PAYLOAD: {user_id, match_id, score, opponent_id, skill_id, belt_level}
TRIGGERS: Match participation badges, score achievement badges, belt progression badges

EVENT: dojo.belt_earned
PAYLOAD: {user_id, skill_id, belt_level, mentor_id}
TRIGGERS: Belt progression badges (White → Black)

EVENT: event.attended
PAYLOAD: {user_id, event_id, attendance_verified, duration_hours}
TRIGGERS: Event attendance badges, participation badges

EVENT: tournament.completed
PAYLOAD: {user_id, tournament_id, rank, total_participants, prize}
TRIGGERS: Tournament winner badges, competition achievement badges

EVENT: project.submitted
PAYLOAD: {user_id, project_id, category, evaluation_score}
TRIGGERS: Project submission badges, portfolio builder badges

EVENT: user.login
PAYLOAD: {user_id, login_timestamp, consecutive_day}
TRIGGERS: Login streak badges, daily active badges

EVENT: user.referred_user
PAYLOAD: {referrer_id, referred_user_id, referral_code, accepted_at}
TRIGGERS: Referral milestone badges (5, 10, 25, 50, 100 referrals)

EVENT: user.endorsed
PAYLOAD: {endorser_id, endorsed_user_id, skill_id, endorsement_count}
TRIGGERS: Endorsement milestone badges, reputation badges

EVENT: innovation.submitted
PAYLOAD: {user_id, idea_id, innovation_score, category}
TRIGGERS: Innovation submission badge, creative thinker badge

EVENT: innovation.licensed
PAYLOAD: {user_id, idea_id, licensing_company, royalty_rate}
TRIGGERS: Innovation licensed badge, entrepreneur badge

EVENT: intelligence.assessed
PAYLOAD: {user_id, assessment_id, dominant_intelligences}
TRIGGERS: Intelligence profile badge, self-awareness badge

EVENT: gd.completed
PAYLOAD: {user_id, session_id, performance_score, rank}
TRIGGERS: GD participation badge, communication excellence badge

EVENT: interview.completed
PAYLOAD: {user_id, interview_id, performance_score, feedback}
TRIGGERS: Interview completion badge, professional readiness badge

EVENT: social.post_viral
PAYLOAD: {user_id, post_id, engagement_count, reach}
TRIGGERS: Influencer badge, content creator badge

EVENT: community.contribution
PAYLOAD: {user_id, contribution_type, impact_score}
TRIGGERS: Community helper badge, top contributor badge
```

### Outgoing Events (Badge Lifecycle)
```
EVENT: badge.awarded
PAYLOAD: {
  award_id, badge_id, badge_name, recipient_id, 
  points_awarded, trigger_event_type, awarded_at
}
CONSUMERS: 
  - Notification Service (send congratulations)
  - XP Service (award points)
  - Analytics Service (track badge distribution)
  - Social Feed Service (post badge update)
  - Leaderboard Service (update rankings)

EVENT: badge.progress_updated
PAYLOAD: {
  user_id, badge_id, current_value, target_value, 
  progress_percentage, projected_completion_date
}
CONSUMERS:
  - User Dashboard (update progress bars)
  - Notification Service (milestone reminders)

EVENT: badge.revoked
PAYLOAD: {
  award_id, badge_id, recipient_id, revocation_reason, revoked_at
}
CONSUMERS:
  - Notification Service (inform user)
  - Audit Service (log revocation)
  - Analytics Service (track fraud)

EVENT: badge.shared
PAYLOAD: {
  award_id, badge_id, recipient_id, platform, shared_at
}
CONSUMERS:
  - Analytics Service (track viral spread)
  - Reputation Service (award sharing points)

EVENT: badge.collection_updated
PAYLOAD: {
  user_id, collection_id, badge_count, featured_badges
}
CONSUMERS:
  - Profile Service (update public profile)
  - SEO Service (regenerate profile page)
```

---

## 🔒 SECTION K — API CONTRACTS (OPENAPI SPEC)

### Badge Award API (Internal Service)
```yaml
POST /api/v1/badges/evaluate-and-award
Content-Type: application/json
Authorization: Bearer {service_token}

Request Body:
{
  "user_id": "uuid",
  "trigger_event_type": "course_completed",
  "trigger_event_data": {
    "course_id": "uuid",
    "grade": 87,
    "completion_date": "2026-03-01T10:00:00Z"
  }
}

Response (202 Accepted):
{
  "evaluations": [
    {
      "badge_id": "uuid",
      "badge_name": "Course Completion - Python",
      "criteria_met": true,
      "awarded": true,
      "award_id": "uuid"
    },
    {
      "badge_id": "uuid",
      "badge_name": "Excellence Achiever",
      "criteria_met": false,
      "reason": "Grade threshold not met (required 90, got 87)"
    }
  ]
}
```

### User Badges Retrieval API
```yaml
GET /api/v1/users/{user_id}/badges
Authorization: Bearer {user_token}

Query Parameters:
  - status: active | revoked | expired
  - category: achievement | progression | participation | etc.
  - limit: integer (default 20)
  - offset: integer (default 0)

Response (200 OK):
{
  "total_count": 45,
  "badges": [
    {
      "award_id": "uuid",
      "badge_id": "uuid",
      "badge_name": "7-Day Streak Champion",
      "badge_description": "Logged in for 7 consecutive days",
      "badge_image_url": "https://cdn.antigravity.com/badges/...",
      "badge_tier": "gold",
      "rarity_level": "rare",
      "awarded_at": "2026-03-01T10:00:00Z",
      "points_awarded": 100,
      "verification_url": "https://verify.antigravity.com/badges/...",
      "is_featured": true,
      "stack_position": 2
    }
  ]
}
```

### Badge Progress API
```yaml
GET /api/v1/users/{user_id}/badges/progress
Authorization: Bearer {user_token}

Response (200 OK):
{
  "in_progress": [
    {
      "badge_id": "uuid",
      "badge_name": "100-Course Master",
      "current_value": 73,
      "target_value": 100,
      "progress_percentage": 73,
      "estimated_completion_days": 27
    }
  ]
}
```

### Badge Verification API (Public)
```yaml
GET /api/v1/badges/verify/{assertion_id}

Response (200 OK):
{
  "status": "valid",
  "badge_name": "Innovation Champion",
  "recipient_name": "John Doe",
  "issued_date": "2026-03-01T10:00:00Z",
  "issuer_name": "Antigravity",
  "open_badges_assertion": { ... }
}
```

### Badge Sharing API
```yaml
POST /api/v1/badges/{award_id}/share
Content-Type: application/json
Authorization: Bearer {user_token}

Request Body:
{
  "platform": "linkedin",
  "custom_message": "Proud to have earned this badge!"
}

Response (200 OK):
{
  "share_url": "https://antigravity.com/share/badge/...",
  "share_image_url": "https://cdn.antigravity.com/badges/share/...",
  "pre_populated_text": "I just earned the Innovation Champion badge on Antigravity! 🏆"
}
```

---

## 🔒 SECTION L — BADGE DISPLAY & USER EXPERIENCE

### Badge Showcase UI Requirements
```
USER PROFILE BADGE GALLERY:
- Display up to 6 featured badges prominently
- Grid layout: 3 columns x 2 rows
- Badge click → Full details modal
- "View All Badges" button → Full collection page

BADGE DETAILS MODAL:
- Badge image (large, 512x512)
- Badge name and description
- Tier and rarity indicators
- Earned date
- Criteria (how it was earned)
- Evidence/proof link (if available)
- Verification URL + QR code
- Share buttons (LinkedIn, Twitter, Facebook)
- Download baked badge button

BADGE PROGRESS INDICATORS:
- Progress bar for accumulative badges
- Tooltip: "73/100 courses completed"
- Estimated completion date (if applicable)
- Motivational message: "27 more to go!"

BADGE UNLOCK ANIMATION:
- Trigger: Immediately after badge award
- Animation: Badge flies in from top, spins, glows
- Sound effect: Achievement chime
- Confetti effect (for rare+ badges)
- Display: Badge name, description, points awarded
- CTA: "Share with friends" or "View badge"
```

### Badge Notification Templates
```
IN-APP NOTIFICATION:
Title: "🏆 Badge Earned!"
Body: "Congratulations! You've earned the {badge_name} badge."
Action: "View Badge"

EMAIL NOTIFICATION:
Subject: "🎉 You've Earned a New Badge: {badge_name}!"
Body:
  - Badge image (visual)
  - Congratulatory message
  - Badge description
  - Points awarded
  - Share buttons
  - View badge CTA

PUSH NOTIFICATION:
Title: "🏆 New Badge!"
Body: "{badge_name} - {points} XP earned"
Action: Open app to view badge
```

### Social Sharing Card Generation
```python
def generate_badge_share_card(badge_award):
    """
    Generate shareable image card for social media
    Format: 1200x630 (Open Graph standard)
    """
    
    canvas = create_canvas(1200, 630)
    
    # Background gradient
    canvas.fill_gradient(
        start_color=badge_award.badge.badge_color_hex,
        end_color="#ffffff"
    )
    
    # Badge image (left side)
    badge_image = load_image(badge_award.badge.badge_image_png)
    canvas.draw_image(badge_image, x=100, y=115, size=400)
    
    # Text (right side)
    canvas.draw_text(
        text="I earned the",
        x=600, y=150,
        font_size=32,
        color="#333333"
    )
    canvas.draw_text(
        text=badge_award.badge.badge_name,
        x=600, y=220,
        font_size=48,
        font_weight="bold",
        color="#000000"
    )
    canvas.draw_text(
        text="badge on Antigravity!",
        x=600, y=290,
        font_size=32,
        color="#333333"
    )
    
    # Platform branding (bottom)
    canvas.draw_text(
        text="antigravity.com",
        x=600, y=500,
        font_size=28,
        color="#666666"
    )
    
    # Save
    card_url = canvas.save_to_storage(
        path=f"/badges/share-cards/{badge_award.award_id}.png"
    )
    
    return card_url
```

---

## 🔒 SECTION M — BADGE COLLECTIONS & SHOWCASES

### User-Created Badge Collections
```
FEATURE: Allow users to create custom badge collections/showcases

EXAMPLE COLLECTIONS:
- "My Best Achievements" (featured on profile)
- "Dojo Belts Collection" (all belt badges)
- "2026 Milestones" (badges earned this year)
- "Rare Badges" (only epic/legendary rarity)

COLLECTION OPERATIONS:
- Create collection
- Add/remove badges
- Reorder badges (drag & drop)
- Set collection visibility (public/private)
- Feature collection on profile
- Share collection URL

PUBLIC COLLECTION PAGE:
URL: /users/{username}/collections/{collection_id}
- SEO-optimized
- Open Graph tags for social sharing
- Grid display of all badges in collection
- Collection name and description
- Creator profile link
```

### Badge Recommendation Engine
```python
def recommend_badges_to_user(user_id):
    """
    Suggest badges user is close to earning
    """
    
    # Get all badges user hasn't earned yet
    available_badges = get_unearned_badges(user_id)
    
    recommendations = []
    
    for badge in available_badges:
        # Evaluate current progress
        criteria_met, evaluation = evaluate_badge_criteria(badge, user_id, None)
        
        if badge.criteria_type == "accumulative":
            progress_pct = evaluation.get("progress_percentage", 0)
            
            # Recommend if >50% complete
            if progress_pct > 50:
                recommendations.append({
                    "badge_id": badge.badge_id,
                    "badge_name": badge.badge_name,
                    "progress_percentage": progress_pct,
                    "current_value": evaluation.get("current_value"),
                    "target_value": evaluation.get("threshold"),
                    "estimated_days_to_completion": estimate_completion_days(
                        user_id, badge.criteria_rule
                    ),
                    "recommended_actions": get_next_actions(badge, user_id)
                })
    
    # Sort by progress (closest to completion first)
    recommendations.sort(key=lambda x: x["progress_percentage"], reverse=True)
    
    return recommendations[:10]  # Top 10 recommendations
```

---

## 🔒 SECTION N — BADGE ANALYTICS & INSIGHTS

### Badge Distribution Analytics
```sql
-- Most earned badges
SELECT 
    bd.badge_name,
    bd.badge_category,
    bd.rarity_level,
    COUNT(ba.award_id) as times_earned,
    COUNT(DISTINCT ba.recipient_id) as unique_recipients
FROM badge_awards ba
JOIN badge_definitions bd ON ba.badge_id = bd.badge_id
WHERE ba.status = 'active'
GROUP BY bd.badge_id, bd.badge_name, bd.badge_category, bd.rarity_level
ORDER BY times_earned DESC;

-- Rarest badges (earned by fewest users)
SELECT 
    bd.badge_name,
    bd.rarity_level,
    COUNT(DISTINCT ba.recipient_id) as unique_recipients,
    (COUNT(DISTINCT ba.recipient_id)::FLOAT / (SELECT COUNT(*) FROM users)) * 100 as earn_percentage
FROM badge_definitions bd
LEFT JOIN badge_awards ba ON bd.badge_id = ba.badge_id AND ba.status = 'active'
WHERE bd.is_retired = FALSE
GROUP BY bd.badge_id, bd.badge_name, bd.rarity_level
ORDER BY unique_recipients ASC
LIMIT 20;

-- Badge earn rate over time
SELECT 
    DATE_TRUNC('day', awarded_at) as date,
    COUNT(*) as badges_awarded
FROM badge_awards
WHERE awarded_at >= NOW() - INTERVAL '30 days'
GROUP BY date
ORDER BY date;
```

### User Badge Statistics
```python
def get_user_badge_stats(user_id):
    """
    Comprehensive badge statistics for a user
    """
    
    return {
        "total_badges_earned": count_user_badges(user_id),
        "badges_by_category": {
            "achievement": count_by_category(user_id, "achievement"),
            "progression": count_by_category(user_id, "progression"),
            "participation": count_by_category(user_id, "participation"),
            # ... etc
        },
        "badges_by_rarity": {
            "common": count_by_rarity(user_id, "common"),
            "uncommon": count_by_rarity(user_id, "uncommon"),
            "rare": count_by_rarity(user_id, "rare"),
            "epic": count_by_rarity(user_id, "epic"),
            "legendary": count_by_rarity(user_id, "legendary")
        },
        "badges_by_tier": {
            "bronze": count_by_tier(user_id, "bronze"),
            "silver": count_by_tier(user_id, "silver"),
            "gold": count_by_tier(user_id, "gold"),
            "platinum": count_by_tier(user_id, "platinum"),
            "diamond": count_by_tier(user_id, "diamond")
        },
        "total_xp_from_badges": sum_badge_points(user_id),
        "first_badge_earned": get_first_badge(user_id),
        "most_recent_badge": get_latest_badge(user_id),
        "rarest_badge_owned": get_rarest_badge(user_id),
        "completion_percentage": calculate_completion_pct(user_id),
        "institution_rank": get_badge_count_rank(user_id, "institution"),
        "global_rank": get_badge_count_rank(user_id, "global")
    }
```

---

## 🔒 SECTION O — BADGE REVOCATION & APPEALS

### Revocation Workflow
```python
def revoke_badge(award_id, revocation_reason, revoked_by_admin_id):
    """
    Revoke a badge award with full audit trail
    """
    
    # 1. Load badge award
    badge_award = get_badge_award(award_id)
    
    if not badge_award:
        raise ValueError("Badge award not found")
    
    if badge_award.status == "revoked":
        raise ValueError("Badge already revoked")
    
    # 2. Update status
    update_badge_award(
        award_id=award_id,
        status="revoked",
        revocation_reason=revocation_reason,
        revoked_at=datetime.now(),
        revoked_by=revoked_by_admin_id
    )
    
    # 3. Deduct XP
    deduct_xp(
        user_id=badge_award.recipient_id,
        xp_amount=badge_award.badge.points_value
    )
    
    # 4. Update badge progress (if tracking)
    if badge_award.badge.is_stackable:
        decrement_stack_count(badge_award.badge_id, badge_award.recipient_id)
    
    # 5. Log audit event
    create_audit_log(
        action="badge_revoked",
        entity_type="badge_award",
        entity_id=award_id,
        actor_id=revoked_by_admin_id,
        details={
            "badge_id": badge_award.badge_id,
            "recipient_id": badge_award.recipient_id,
            "revocation_reason": revocation_reason
        }
    )
    
    # 6. Emit event
    emit_event("badge.revoked", {
        "award_id": award_id,
        "badge_id": badge_award.badge_id,
        "recipient_id": badge_award.recipient_id,
        "revocation_reason": revocation_reason
    })
    
    # 7. Notify user
    send_notification(
        user_id=badge_award.recipient_id,
        type="badge_revoked",
        title="Badge Revoked",
        message=f"Your {badge_award.badge.badge_name} badge has been revoked. Reason: {revocation_reason}"
    )
    
    return {"status": "revoked", "award_id": award_id}
```

### Appeals Process
```sql
CREATE TABLE badge_revocation_appeals (
    appeal_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    award_id UUID NOT NULL REFERENCES badge_awards(award_id),
    appellant_user_id UUID NOT NULL REFERENCES users(user_id),
    
    -- Appeal Details
    appeal_reason TEXT NOT NULL,
    supporting_evidence_urls TEXT[],
    submitted_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Review
    status VARCHAR(50) NOT NULL DEFAULT 'pending', -- pending, approved, rejected
    reviewed_by UUID REFERENCES users(user_id),
    reviewed_at TIMESTAMP,
    review_notes TEXT,
    
    -- Outcome
    badge_reinstated BOOLEAN DEFAULT FALSE,
    
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    CONSTRAINT valid_appeal_status CHECK (status IN ('pending', 'approved', 'rejected'))
);
```

---

## 🔒 SECTION P — DEPLOYMENT & INFRASTRUCTURE

### Service Architecture
```
DEPLOYMENT MODEL: Kubernetes Stateless Deployment

Container Specs:
  Image: badge-issuance-agent:v1.0
  CPU: 2 cores
  Memory: 4 GB
  Replicas: 3 (min), 10 (max)
  Autoscaling: CPU > 70% OR Event queue > 1000

Dependencies:
  - PostgreSQL (badge metadata)
  - Redis (criteria evaluation cache, anti-gaming tracking)
  - MinIO (badge images)
  - Kafka/RabbitMQ (event bus)

Service Endpoints:
  - Internal API: http://badge-service:8080
  - Health check: /health
  - Metrics: /metrics (Prometheus format)
  - Verification API: https://verify.antigravity.com/badges
```

### Environment Variables
```bash
# Database
DB_HOST=postgres.default.svc.cluster.local
DB_PORT=5432
DB_NAME=antigravity
DB_USER=badge_service
DB_PASSWORD=<from_vault>

# Cache
REDIS_HOST=redis.default.svc.cluster.local
REDIS_PORT=6379
REDIS_PASSWORD=<from_vault>

# Storage
MINIO_ENDPOINT=minio.default.svc.cluster.local:9000
MINIO_ACCESS_KEY=<from_vault>
MINIO_SECRET_KEY=<from_vault>
MINIO_BUCKET=badges

# Event Bus
KAFKA_BROKERS=kafka.default.svc.cluster.local:9092
KAFKA_CONSUMER_GROUP=badge-service

# Anti-Gaming
FRAUD_DETECTION_ENABLED=true
FRAUD_THRESHOLD=50
MAX_BADGES_PER_DAY_PER_USER=10

# Feature Flags
OPEN_BADGES_ENABLED=true
BAKED_BADGES_ENABLED=true
MANUAL_APPROVAL_ENABLED=true
```

### Observability
```
METRICS:
- badges_awarded_total (counter, by category, rarity)
- badge_evaluation_duration_seconds (histogram)
- badge_criteria_met_percentage (gauge, by badge)
- fraud_detections_total (counter)
- badge_revocations_total (counter, by reason)
- pending_manual_approvals (gauge)

LOGS:
- Badge evaluation logs (DEBUG level)
- Award creation logs (INFO level)
- Fraud detection logs (WARN level)
- Revocation logs (WARN level)

ALERTS:
- fraud_detections_total > 100 in 1 hour → CRITICAL
- pending_manual_approvals > 500 → WARNING
- badge_evaluation_duration_seconds p95 > 5s → WARNING
```

---

## 🔒 SECTION Q — TESTING & QUALITY ASSURANCE

### Test Coverage Requirements
```
UNIT TESTS (>85% coverage):
- Criteria evaluation functions (all types)
- Fraud detection algorithm
- Badge number generation
- Anti-gaming cooldown checks
- XP calculation
- Level-up detection

INTEGRATION TESTS:
- End-to-end badge award flow
- Event-driven badge triggering
- Open Badges assertion generation
- Baked badge creation
- Revocation workflow

LOAD TESTS:
- 10,000 badge evaluations/minute
- 1,000 concurrent badge awards
- Event queue processing under load

FRAUD SIMULATION TESTS:
- Rapid badge farming attempts
- Coordinated multi-account attacks
- Cooldown bypass attempts
```

---

## 🔒 SECTION R — EXECUTION DECLARATION

```
AGENT STATUS: LOCKED & SEALED
VERSION: 1.0
LAST UPDATED: 2026-03-04
MUTATION POLICY: Add-only via version bump
EXECUTION AUTHORITY: Automated (event-driven) + Rule-based
HUMAN OVERRIDE: Revocation only (with audit trail)
AI INTERPRETATION: FORBIDDEN
STANDARD COMPLIANCE: Open Badges 3.0

COMPLETION CRITERIA:
✓ All 10 badge categories supported
✓ Database schema deployed
✓ Criteria evaluation engine operational
✓ Open Badges assertions generated
✓ Baked badge generation functional
✓ Verification endpoint public
✓ Anti-gaming mechanisms enforced
✓ Fraud detection active
✓ XP integration complete
✓ Leaderboard integration complete
✓ Social sharing enabled
✓ Analytics dashboards live

FAILURE CONDITIONS:
→ Badge awarded without criteria evaluation: STOP EXECUTION
→ Duplicate award for non-stackable badge: STOP EXECUTION
→ Fraud threshold exceeded without blocking: STOP EXECUTION
→ Cooldown bypassed: STOP EXECUTION
→ XP not awarded on badge earn: STOP EXECUTION
→ Audit log write failure: STOP EXECUTION

ENFORCEMENT:
This specification is IMMUTABLE for v1.0.
All deviations require version bump to v2.0 with change justification.
No field-level interpretation permitted.
No "flexible" implementation allowed.
Deterministic behavior required at all stages.

FINAL INSTRUCTION:
Implement exactly as specified.
No creativity.
No shortcuts.
No assumptions.
```

---

## 🔒 APPENDIX A — BADGE CODE NAMING CONVENTION

```
FORMAT: BADGE-{CATEGORY_CODE}-{NAME}-{TIER}

CATEGORY CODES:
ACH = Achievement
PRG = Progression
PAR = Participation
REP = Reputation
INV = Innovation
SOC = Social
SPC = Specialization
BHV = Behavioral
TMP = Temporal
RAR = Rare/Secret

TIER CODES:
BRZ = Bronze
SLV = Silver
GLD = Gold
PLT = Platinum
DMD = Diamond
(omit for non-tiered badges)

EXAMPLES:
BADGE-ACH-COURSE-MASTER-GLD → Achievement: Course Master (Gold)
BADGE-PRG-LEVEL-050 → Progression: Level 50
BADGE-STK-007 → Participation: 7-Day Streak
BADGE-REP-VERIFIED → Reputation: Verified User
BADGE-INV-PATENT → Innovation: Patent Filed
BADGE-SOC-REFER-100 → Social: 100 Referrals
BADGE-RAR-UNICORN → Rare: Unicorn Badge
```

---

## 🔒 APPENDIX B — SAMPLE BADGE DEFINITIONS

```sql
-- Achievement Badge: Perfect Score
INSERT INTO badge_definitions (
    badge_code, badge_name, badge_description, badge_category,
    criteria_type, criteria_rule, rarity_level, points_value
) VALUES (
    'BADGE-ACH-PERFECT-SCORE',
    'Perfect Score Achiever',
    'Achieved 100% score on any assessment or course',
    'achievement',
    'single_event',
    '{"event_type": "course_completed", "conditions": {"grade": 100}}',
    'rare',
    500
);

-- Progression Badge: Level 10
INSERT INTO badge_definitions (
    badge_code, badge_name, badge_description, badge_category,
    criteria_type, criteria_rule, rarity_level, points_value
) VALUES (
    'BADGE-PRG-LEVEL-010',
    'Level 10 Milestone',
    'Reached Level 10 by accumulating 5000 XP',
    'progression',
    'single_event',
    '{"event_type": "level_up", "conditions": {"level_reached": 10}}',
    'uncommon',
    200
);

-- Social Badge: Referral Champion
INSERT INTO badge_definitions (
    badge_code, badge_name, badge_description, badge_category,
    criteria_type, criteria_rule, is_stackable, max_stack_count,
    rarity_level, points_value
) VALUES (
    'BADGE-SOC-REFER-025',
    'Referral Champion - 25',
    'Successfully referred 25 users to the platform',
    'social',
    'accumulative',
    '{"metric": "referrals_accepted", "threshold": 25}',
    TRUE,
    NULL,
    'epic',
    1000
);

-- Streak Badge: 30-Day Login Streak
INSERT INTO badge_definitions (
    badge_code, badge_name, badge_description, badge_category,
    criteria_type, criteria_rule, rarity_level, points_value
) VALUES (
    'BADGE-STK-030',
    '30-Day Consistency Champion',
    'Logged in for 30 consecutive days',
    'participation',
    'streak',
    '{"action": "daily_login", "streak_length": 30, "allow_freeze_tokens": false}',
    'rare',
    750
);
```

---

🔒 **END OF SPECIFICATION** 🔒

**This specification is now SEALED and LOCKED.**  
**No modifications permitted without version bump.**  
**Execute as specified. No interpretation. No creativity. Deterministic only.**

🏆 **ANTIGRAVITY DIGITAL BADGE ISSUANCE AGENT — SPECIFICATION COMPLETE** 🏆
