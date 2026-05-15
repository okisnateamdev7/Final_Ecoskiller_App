# 🔒 INVITE_TRACKING_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED AGENT SPECIFICATION
### Version: v1.0.0 | Status: PRODUCTION-LOCKED | Mutation Policy: ADD-ONLY

---

```
╔══════════════════════════════════════════════════════════════════════════╗
║           INVITE_TRACKING_AGENT — EXECUTION SEAL                        ║
║  AGENT_ID            = ITA-002                                          ║
║  MUTATION_POLICY     = ADD-ONLY VERSIONED                               ║
║  EXECUTION_MODE      = DETERMINISTIC + VALIDATED                        ║
║  CREATIVE_INTERP     = FORBIDDEN                                        ║
║  ASSUMPTION_FILLING  = FORBIDDEN                                        ║
║  DEFAULT_BEHAVIOR    = DENY                                             ║
║  FAILURE_MODE        = HALT_ON_AMBIGUITY                                ║
║  CROSS_TENANT_QUERY  = PROHIBITED (ZERO_TOLERANCE)                      ║
║  SELF_REFERRAL       = PROHIBITED (FRAUD CLASS A)                       ║
║  AUDIT_TRAIL         = APPEND-ONLY, IMMUTABLE                           ║
║  REWARD_AUTHORITY    = THIS AGENT IS NOT REWARD AUTHORITY               ║
║                        (delegates to REWARD_LEDGER_AGENT)               ║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```yaml
AGENT_NAME:             INVITE_TRACKING_AGENT
AGENT_ID:               ITA-002
SYSTEM_ROLE:            REFERRAL_ATTRIBUTION_AUTHORITY
PRIMARY_DOMAIN:         VIRAL_GROWTH + REFERRAL_INTEGRITY
EXECUTION_MODE:         DETERMINISTIC + VALIDATED
DATA_SCOPE:             |
  REFERRAL_CODES        | INVITE_EVENTS       | REFERRAL_ACCEPTANCES
  CHANNEL_ATTRIBUTION   | FRAUD_FLAGS         | REWARD_ELIGIBILITY_SIGNALS
  MILESTONE_STATES      | COMMUNITY_INVITES   | CAMPUS_AMBASSADOR_INVITES
TENANT_SCOPE:           STRICT_ISOLATION (No cross-tenant invite tracking)
FAILURE_POLICY:         HALT_ON_AMBIGUITY
SECURITY_LEVEL:         ZERO_TRUST
AUDIT_CLASS:            APPEND_ONLY_IMMUTABLE
SCOPE_BOUNDARY:         |
  IN_SCOPE:   Track, validate, attribute, and signal invite/referral events
  OUT_SCOPE:  Reward crediting (→ REWARD_LEDGER_AGENT)
              XP computation   (→ RANK_COMPUTATION_AGENT)
              Notification push (→ NOTIFICATION_AGENT)
              Leaderboard rank  (→ LEADERBOARD_AGENT)
VERSION:                v1.0.0
LAST_SEALED:            2025-01-01T00:00:00Z
SEALED_BY:              SYSTEM_GOVERNANCE
```

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

Ecoskiller Antigravity's growth model is **zero-paid-marketing, product-led** (Section R52, R58, R70 of Platform Law). The entire organic acquisition engine — referral codes, invite links, QR handoffs, WhatsApp/SMS/Email share flows, campus ambassador pipelines, and invite-only microcommunity joins — depends on a single authoritative system that can:

1. **Track** every invite event from issuance through conversion with full channel attribution
2. **Validate** that each conversion is fraud-free before any reward signal is emitted
3. **Attribute** each new user registration to exactly one invite source (no double-attribution)
4. **Signal** downstream agents (REWARD_LEDGER_AGENT, RANK_COMPUTATION_AGENT, GROWTH_ANALYTICS_AGENT) with structured, confidence-scored outputs

Without this agent, referral fraud, double-reward exploitation, multi-tenant invite leakage, and unattributed growth loops become critical financial and security risks at 10M–100M user scale.

### What Input It Consumes

- Invite code generation requests (from USER_SERVICE when user requests referral code)
- Registration events with referral_code claim (from AUTH_SERVICE on new user signup)
- Channel attribution metadata (from SHARE_LINK_AGENT — UTM, QR scan, NFC, deep-link)
- Fraud signal inputs (from ANTI_FRAUD_AGENT — device fingerprints, IP, behavioral heuristics)
- Community invite requests (from MICROCOMMUNITY_ENGINE — R55 invite-only groups)
- Campus ambassador invite events (from CAMPUS_AMBASSADOR_SERVICE)
- Invite-3-to-unlock requests (from FEATURE_GATE_AGENT — invite gating mechanic)
- Invite expiry signals (from SCHEDULER_AGENT — TTL enforcement on invite links)

### What Output It Produces

- `REFERRAL_VALIDATED_EVENT` — clean, fraud-cleared referral conversion record
- `REFERRAL_FRAUD_FLAG_EVENT` — fraud-detected referral record (rewards blocked)
- `REWARD_ELIGIBILITY_SIGNAL` — structured signal to REWARD_LEDGER_AGENT (never credits directly)
- `MILESTONE_REACHED_EVENT` — inviter crossed tier threshold (1, 3, 5, 10, 25 referrals)
- `INVITE_ACCEPTED_EVENT` — generic invite conversion (community, ambassador, feature unlock)
- `FEATURE_UNLOCK_SIGNAL` — invite-3 mechanic fulfilled, sent to FEATURE_GATE_AGENT
- `ATTRIBUTION_RECORD` — immutable record of which channel/code sourced each new user
- `FEATURE_VECTOR_EMIT` — referral behavioral features to FEATURE_STORE_AGENT

### Downstream Agents That Depend On It

```
REWARD_LEDGER_AGENT           — receives REWARD_ELIGIBILITY_SIGNAL
RANK_COMPUTATION_AGENT        — receives referral-XP event (via REWARD_LEDGER_AGENT)
NOTIFICATION_AGENT            — receives MILESTONE_REACHED_EVENT + INVITE_ACCEPTED_EVENT
SHARE_TRIGGER_AGENT           — receives milestone share triggers
GROWTH_ANALYTICS_AGENT        — receives ATTRIBUTION_RECORD + all invite events
LEADERBOARD_AGENT             — receives referral leaderboard rank signals
FEATURE_GATE_AGENT            — receives FEATURE_UNLOCK_SIGNAL
MICROCOMMUNITY_ENGINE         — receives INVITE_ACCEPTED_EVENT for community seat allocation
CAMPUS_AMBASSADOR_SERVICE     — receives validated ambassador invite conversion records
OBSERVABILITY_AGENT           — receives all failure and performance events
FEATURE_STORE_AGENT           — receives FEATURE_VECTOR_EMIT
ANTI_FRAUD_AGENT              — bidirectional: sends fraud signals, receives fraud verdicts
```

### Upstream Agents That Feed It

```
AUTH_SERVICE                  — new user registration events with referral_code claim
USER_SERVICE                  — invite code generation requests
SHARE_LINK_AGENT              — channel attribution metadata (UTM, QR, NFC, deep-link)
ANTI_FRAUD_AGENT              — device fingerprint, IP reputation, behavioral anomaly scores
MICROCOMMUNITY_ENGINE         — community invite generation and seat-check requests
CAMPUS_AMBASSADOR_SERVICE     — campus ambassador invite batch events
FEATURE_GATE_AGENT            — invite-3-to-unlock gate trigger requests
SCHEDULER_AGENT               — invite TTL expiry events
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

### INPUT_SCHEMA — Invite Code Generation Request

```json
{
  "required_fields": {
    "request_id":         "UUID — idempotency key for this generation request",
    "tenant_id":          "UUID — strict tenant scope",
    "user_id":            "UUID — must belong to declared tenant_id",
    "invite_type":        "ENUM[REFERRAL_CODE|COMMUNITY_INVITE|AMBASSADOR_INVITE|FEATURE_UNLOCK_INVITE]",
    "schema_version":     "STRING — current or N-1 only",
    "timestamp_utc":      "ISO8601 UTC"
  },
  "optional_fields": {
    "channel_hint":       "ENUM[WHATSAPP|SMS|EMAIL|QR|NFC|DIRECT_LINK|IN_APP] — preferred channel",
    "community_id":       "UUID — required if invite_type=COMMUNITY_INVITE",
    "feature_gate_id":    "UUID — required if invite_type=FEATURE_UNLOCK_INVITE",
    "expiry_override_hrs":"INTEGER[1–168] — custom TTL; default from INVITE_CONFIG if absent",
    "max_uses_override":  "INTEGER[1–100] — custom use cap; default from INVITE_CONFIG if absent"
  },
  "validation_rules": [
    "request_id MUST be unique — duplicate = REJECT (idempotency guard)",
    "tenant_id MUST match user_id's registered tenant from IDENTITY_SERVICE",
    "user_id MUST be in VERIFIED or ACTIVE lifecycle state — REGISTERED or RESTRICTED = REJECT",
    "invite_type MUST be in approved ENUM — unknown values = REJECT",
    "community_id MUST be present and valid if invite_type=COMMUNITY_INVITE",
    "feature_gate_id MUST be present if invite_type=FEATURE_UNLOCK_INVITE",
    "User MUST NOT be under active FRAUD_SUSPENSION — check ANTI_FRAUD_AGENT registry",
    "User MUST NOT already hold an active unexpired invite of same invite_type (check dedup cache)"
  ],
  "security_checks": [
    "JWT tenant_id claim MUST equal input tenant_id",
    "Only USER_SERVICE and approved upstream agents may call code generation endpoint",
    "Rate limit: max 10 invite generation requests per user per 24-hour rolling window",
    "Suspended or fraud-flagged users BLOCKED from generating invite codes"
  ]
}
```

### INPUT_SCHEMA — Referral / Invite Conversion Event (Registration)

```json
{
  "required_fields": {
    "event_id":               "UUID — globally unique, idempotency key",
    "tenant_id":              "UUID",
    "new_user_id":            "UUID — the newly registered user",
    "invite_code_claimed":    "STRING — the code presented at registration",
    "registration_timestamp": "ISO8601 UTC",
    "schema_version":         "STRING"
  },
  "optional_fields": {
    "channel_attribution":    "OBJECT — UTM params, QR token, NFC token, deep-link fingerprint",
    "device_fingerprint":     "STRING — hashed device ID from ANTI_FRAUD_AGENT",
    "ip_hash":                "STRING — hashed IP (never raw IP stored)",
    "referrer_url":           "STRING — web referrer if applicable",
    "anti_fraud_pre_score":   "FLOAT[0.0–1.0] — preliminary fraud risk score from ANTI_FRAUD_AGENT"
  },
  "validation_rules": [
    "event_id MUST be unique — duplicate = REJECT",
    "new_user_id MUST NOT have any prior attribution record — one attribution per user, ever",
    "invite_code_claimed MUST exist in INVITE_CODE_REGISTRY",
    "invite_code_claimed MUST NOT be expired (check TTL via INVITE_CONFIG)",
    "invite_code_claimed MUST NOT exceed max_uses cap",
    "new_user_id MUST NOT match the code owner (self-referral = CLASS_A_FRAUD, HALT)",
    "registration_timestamp MUST be within [code_created_at, code_expires_at] window",
    "tenant_id of new_user MUST match tenant_id of invite code owner"
  ],
  "security_checks": [
    "Only AUTH_SERVICE may submit conversion events to this endpoint",
    "anti_cheat_pre_score above 0.80 = IMMEDIATE_FRAUD_HOLD (do not proceed to reward)",
    "device_fingerprint checked against KNOWN_FRAUD_DEVICE registry",
    "ip_hash checked against RATE_ABUSE_IP registry (>5 registrations from same IP in 1hr = HOLD)"
  ],
  "domain_checks": [
    "Invite codes are tenant-scoped — cross-tenant code redemption = SECURITY_VIOLATION",
    "Community invite codes are also domain-scoped (e.g., Arts community invite ≠ Tech community)"
  ]
}
```

### INPUT_SCHEMA — Channel Attribution Event (from SHARE_LINK_AGENT)

```json
{
  "required_fields": {
    "attribution_event_id":   "UUID",
    "invite_code":            "STRING",
    "channel":                "ENUM[WHATSAPP|SMS|EMAIL|QR|NFC|DIRECT_LINK|IN_APP|UNKNOWN]",
    "timestamp_utc":          "ISO8601 UTC",
    "schema_version":         "STRING"
  },
  "optional_fields": {
    "utm_source":             "STRING",
    "utm_medium":             "STRING",
    "utm_campaign":           "STRING",
    "qr_token":               "STRING — QR scan token",
    "nfc_token":              "STRING — NFC handoff token",
    "deep_link_path":         "STRING"
  },
  "validation_rules": [
    "invite_code MUST exist in INVITE_CODE_REGISTRY",
    "channel MUST be in approved ENUM",
    "attribution recorded as PRE_CONVERSION until registration event arrives"
  ]
}
```

### NULL TOLERANCE POLICY

```
REQUIRED_FIELDS:    ZERO NULL TOLERANCE — any null = REJECT + LOG_VALIDATION_FAILURE
OPTIONAL_FIELDS:    ABSENT is acceptable — never silently defaulted
SILENT_DEFAULTS:    FORBIDDEN — all configuration from INVITE_CONFIG_v{n}, never assumed
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

### OUTPUT_SCHEMA — REFERRAL_VALIDATED_EVENT

```json
{
  "result_object": {
    "attribution_id":             "UUID — primary key, globally unique, immutable",
    "tenant_id":                  "UUID",
    "inviter_user_id":            "UUID — code owner",
    "invitee_user_id":            "UUID — newly registered user",
    "invite_code":                "STRING",
    "invite_type":                "ENUM[REFERRAL_CODE|COMMUNITY_INVITE|AMBASSADOR_INVITE|FEATURE_UNLOCK_INVITE]",
    "channel_attributed":         "ENUM — channel through which invite was delivered",
    "conversion_timestamp_utc":   "ISO8601 UTC",
    "fraud_verdict":              "ENUM[CLEAN|HOLD|FLAGGED|BLOCKED]",
    "fraud_flag_codes":           "ARRAY[STRING] — empty if CLEAN",
    "reward_eligible_inviter":    "BOOLEAN — true only if fraud_verdict=CLEAN",
    "reward_eligible_invitee":    "BOOLEAN — true only if fraud_verdict=CLEAN",
    "inviter_referral_count_now": "INTEGER — inviter's total validated referrals post this event",
    "milestone_crossed":          "ENUM[NONE|TIER_1|TIER_2|TIER_3|TIER_4|TIER_5] — milestone if crossed",
    "feature_unlock_triggered":   "BOOLEAN — true if FEATURE_UNLOCK_INVITE and threshold met",
    "community_seat_allocated":   "BOOLEAN — true if COMMUNITY_INVITE and seat was free"
  },
  "confidence_score":             "FLOAT[0.0–1.0] — fraud detection confidence",
  "model_version":                "STRING — e.g., ITA-FRAUD-ML-v1.4.2",
  "audit_reference":              "UUID — pointer to immutable audit log entry",
  "next_trigger_event":           [
    "REWARD_ELIGIBILITY_SIGNAL (if reward_eligible=true)",
    "MILESTONE_REACHED_EVENT (if milestone_crossed != NONE)",
    "FEATURE_UNLOCK_SIGNAL (if feature_unlock_triggered=true)",
    "INVITE_ACCEPTED_EVENT (always emitted)",
    "ATTRIBUTION_RECORD (always emitted)",
    "FEATURE_VECTOR_EMIT (always emitted)"
  ]
}
```

### OUTPUT_SCHEMA — REFERRAL_FRAUD_FLAG_EVENT

```json
{
  "result_object": {
    "fraud_event_id":       "UUID",
    "tenant_id":            "UUID",
    "invite_code":          "STRING",
    "inviter_user_id":      "UUID",
    "invitee_user_id":      "UUID",
    "fraud_class":          "ENUM[CLASS_A|CLASS_B|CLASS_C]",
    "fraud_flag_codes":     "ARRAY[STRING] — specific rule codes triggered",
    "fraud_evidence":       "OBJECT — hashed device match, ip_hash, behavioral signals",
    "action_taken":         "ENUM[REWARD_BLOCKED|USER_SUSPENDED|CODE_REVOKED|HOLD_PENDING_REVIEW]",
    "referral_count_delta": "INTEGER — always 0 (fraud conversions never increment count)"
  },
  "confidence_score":       "FLOAT[0.0–1.0]",
  "model_version":          "STRING",
  "audit_reference":        "UUID",
  "next_trigger_event":     [
    "ANTI_FRAUD_ESCALATION_EVENT",
    "NOTIFICATION_AGENT (inviter notified of review)",
    "COMPLIANCE_ADMIN_ALERT (if CLASS_A)"
  ]
}
```

### Output Guarantees

```
EVERY output MUST include:
  ✅ confidence_score
  ✅ model_version
  ✅ audit_reference (UUID, immutable, non-nullable)
  ✅ next_trigger_event list (may be empty array, never null)
  ✅ fraud_verdict (explicit — never inferred or absent)
  ✅ reward_eligible_inviter + reward_eligible_invitee (both fields, both explicit BOOLEAN)

FORBIDDEN in output:
  ❌ Reward crediting (this agent emits SIGNALS only — REWARD_LEDGER_AGENT credits)
  ❌ Null audit_reference
  ❌ Omitting fraud_verdict
  ❌ Partial result_object
  ❌ Any output without channel_attributed (if unknown, emit ENUM value UNKNOWN)
```

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Layer (PRIMARY — 80% of computation)

```yaml
MODEL_TYPE:
  PRIMARY:      Gradient Boosted Fraud Classifier (XGBoost)
                Input: device fingerprint match, IP velocity, time-to-register delta,
                behavioral similarity score, email domain entropy, registration pattern
                Output: fraud_probability FLOAT[0.0–1.0]

  SECONDARY:    Anomaly Detection (Isolation Forest)
                Detects coordinated invite rings — clusters of new users with shared
                device/IP/time signatures converting on same inviter's code

  TERTIARY:     Multi-Touch Attribution Model (Logistic Regression)
                For cases where multiple invite channels touched a user before conversion —
                assigns primary_channel attribution weight

FEATURES_USED:
  # Device & Network Features
  - device_fingerprint_match_score      # 0=new device, 1=known fraud device
  - ip_hash_registration_velocity_1h    # registrations from same IP in last 1 hour
  - ip_hash_registration_velocity_24h   # registrations from same IP in last 24 hours
  - ip_subnet_velocity_24h              # /24 subnet velocity
  - time_delta_invite_to_register_mins  # seconds between code share event and registration
  - timezone_consistency_flag           # device TZ vs declared location match

  # Behavioral Features
  - email_domain_entropy                # disposable email domain score
  - username_pattern_bot_score          # ML-detected bot-pattern username
  - registration_flow_completion_time_s # suspiciously fast completions
  - referrer_url_match_flag             # did referrer match expected share channel?
  - channel_attribution_confidence      # how strongly attributed to declared channel

  # Inviter History Features
  - inviter_historical_fraud_rate       # % of past referrals that were fraudulent
  - inviter_referral_velocity_7d        # referrals in last 7 days (spike detection)
  - inviter_account_age_days            # new inviters with high velocity = risk
  - inviter_fraud_flag_count_lifetime   # prior fraud flags on inviter

  # Code-Level Features
  - code_redemption_velocity_1h         # redemptions on this specific code in 1 hour
  - code_channel_diversity_score        # redeemed from single channel = potential ring

FRAUD_THRESHOLDS:
  CLEAN:          fraud_probability < 0.30
  HOLD:           fraud_probability 0.30–0.60  (process, reward held pending review)
  FLAGGED:        fraud_probability 0.60–0.80  (reward blocked, escalated)
  BLOCKED:        fraud_probability > 0.80     (immediate block, inviter notified)

TRAINING_FREQUENCY:
  FRAUD_CLASSIFIER:     Weekly (Saturday 01:00 UTC) — fraud patterns evolve rapidly
  ANOMALY_DETECTOR:     Weekly (Saturday 02:00 UTC)
  ATTRIBUTION_MODEL:    Monthly (1st of month 02:00 UTC)

DRIFT_DETECTION:
  FRAUD_RATE_BASELINE:  Monitor weekly — alert if fraud rate changes ±30% from baseline
  FEATURE_DRIFT:        PSI per feature — alert if PSI > 0.2, retrain if PSI > 0.25
  FALSE_POSITIVE_RATE:  Monitor HOLD cases that clear on manual review — target < 5%
  FALSE_NEGATIVE_RATE:  Monitor CLEAN cases later found fraudulent — target < 1%

VERSION_CONTROL:
  MODEL_REGISTRY:       MLflow (internal, append-only)
  VERSIONING:           Semantic — MAJOR.MINOR.PATCH
  IMMUTABILITY:         Deployed model artifacts = READ_ONLY
  ROLLBACK:             N-1 version hot-standby — auto-rollback if drift breach
  SHADOW_TESTING:       72-hour shadow before production promotion

HARD_RULES_LAYER:
  # These rules execute BEFORE ML — deterministic, never overridden by model
  RULE_A:  invitee_user_id == inviter_user_id                → CLASS_A_FRAUD, HALT
  RULE_B:  invite_code already redeemed by invitee_user_id   → DUPLICATE_REDEMPTION, REJECT
  RULE_C:  invite_code expired                               → EXPIRED_CODE, REJECT
  RULE_D:  invite_code exceed max_uses                       → CODE_EXHAUSTED, REJECT
  RULE_E:  inviter tenant_id != invitee tenant_id            → CROSS_TENANT_FRAUD, SECURITY_VIOLATION
  RULE_F:  inviter under FRAUD_SUSPENSION                    → CODE_REVOKED, REJECT
  RULE_G:  ip_hash_velocity_1h > 5                           → IP_VELOCITY_HOLD
  RULE_H:  inviter_referral_velocity_7d > 50                 → HIGH_VELOCITY_HOLD
```

### AI Layer (SUPPLEMENTAL — 20% of usage)

```yaml
AI_USAGE_SCOPE:
  - Human-readable fraud explanation for COMPLIANCE_ADMIN review UI
  - Invite message copy suggestions (for share modal — not decision-making)
  - Anomaly ring narrative summary (when Isolation Forest detects coordinated ring)

AI_DECISION_AUTONOMY:     NONE
  # AI NEVER approves or denies referrals
  # AI NEVER sets fraud_verdict
  # AI output is informational — COMPLIANCE_ADMIN makes final call on HOLD cases

PROMPT_GOVERNANCE:
  TEMPLATE_ID:            INVITE_FRAUD_EXPLAIN_PROMPT_v{n}
  TEMPERATURE:            0.0 (deterministic)
  MAX_TOKENS:             200 (fraud explanation only)
  PII_FILTER:             MANDATORY — strip all PII before prompt construction
  FORBIDDEN_OUTPUTS:      Reward promises | Specific user identification | Legal conclusions
  AUDIT:                  Prompt hash + response hash logged per invocation
```

---

## 6️⃣ REFERRAL REWARD MILESTONE CONFIG (LOCKED)

### Inviter Milestone Tiers

```yaml
REFERRAL_MILESTONES:
  TIER_1:
    threshold:          1   # First validated referral
    reward_type:        XP_BONUS
    xp_amount:          500
    badge:              "First Recruit"
    share_trigger:      TRUE

  TIER_2:
    threshold:          3   # Feature unlock gate (R55 mechanic: "Invite 3 to unlock")
    reward_type:        FEATURE_UNLOCK + XP_BONUS
    xp_amount:          1000
    feature_unlocked:   "ADVANCED_ANALYTICS"
    badge:              "Squad Builder"
    share_trigger:      TRUE

  TIER_3:
    threshold:          5   # Premium trial unlock
    reward_type:        PREMIUM_TRIAL + XP_BONUS
    xp_amount:          2000
    premium_days:       7
    badge:              "Growth Champion"
    share_trigger:      TRUE

  TIER_4:
    threshold:          10  # Premium trial extended
    reward_type:        PREMIUM_TRIAL + XP_BONUS
    xp_amount:          5000
    premium_days:       30
    badge:              "Network Builder"
    share_trigger:      TRUE

  TIER_5:
    threshold:          25  # Campus Ambassador tier eligibility
    reward_type:        AMBASSADOR_ELIGIBILITY + XP_BONUS
    xp_amount:          15000
    unlocks:            CAMPUS_AMBASSADOR_APPLICATION
    badge:              "Campus Ambassador Candidate"
    share_trigger:      TRUE

INVITEE_REWARD:
  xp_on_valid_referral: 500
  reward_type:          XP_BONUS
  condition:            fraud_verdict = CLEAN AND invitee completes first activity

MILESTONE_POLICY:
  MILESTONE_IDEMPOTENCY: Each milestone fires exactly ONCE per user, lifetime
  MILESTONE_DEDUP_KEY:   {user_id}:{milestone_tier} stored in Redis SET NX
  DOWNGRADE:             FORBIDDEN — milestone rewards cannot be revoked after clean credit
  FRAUD_CLAWBACK:        If post-credit fraud detected, COMPLIANCE_ADMIN initiates manual review
                         Agent does NOT auto-clawback — humans decide
```

---

## 7️⃣ INVITE CODE GENERATION SPEC (LOCKED)

### Code Anatomy

```yaml
REFERRAL_CODE_FORMAT:
  PATTERN:        "{USER_INITIALS}{RANDOM_4_ALPHANUM}"
  EXAMPLE:        "RAH-K7P2"
  LENGTH:         8–10 characters
  CASE:           UPPERCASE only
  COLLISION_GUARD: On generation, check INVITE_CODE_REGISTRY — regenerate if collision
  CHARSET:        A-Z 0-9 (no ambiguous chars: O, 0, I, 1, L)

COMMUNITY_INVITE_CODE_FORMAT:
  PATTERN:        "CM-{COMMUNITY_ID_PREFIX}-{RANDOM_6_ALPHANUM}"
  SCOPED_TO:      specific community_id — cannot be redeemed for any other community

AMBASSADOR_INVITE_CODE_FORMAT:
  PATTERN:        "AMB-{CAMPUS_CODE}-{RANDOM_6_ALPHANUM}"
  SCOPED_TO:      campus ambassador program — tracked separately

INVITE_TTL_DEFAULTS:
  REFERRAL_CODE:          NEVER_EXPIRES (active until user account closure or fraud suspension)
  ONE_TIME_INVITE_LINK:   72 hours (configurable per INVITE_CONFIG)
  COMMUNITY_INVITE:       168 hours (7 days)
  FEATURE_UNLOCK_INVITE:  48 hours
  QR_CODE_TOKEN:          24 hours

MAX_USES_DEFAULTS:
  REFERRAL_CODE:          UNLIMITED (per-code, but per-user daily velocity cap enforced)
  ONE_TIME_INVITE_LINK:   1 use
  COMMUNITY_INVITE:       1 use per invite code instance (COMMUNITY_SEAT controls total)
  FEATURE_UNLOCK_INVITE:  3 uses (3 invites needed to unlock)
  QR_CODE_TOKEN:          1 use
```

### Share Channel Matrix

```yaml
SUPPORTED_CHANNELS:
  WHATSAPP:
    mechanism:      Deep link with referral_code param
    tracking:       UTM + attribution_event on link open
    og_preview:     Required (SHARE_LINK_AGENT generates)
    rate_limit:     Max 20 WhatsApp shares per user per day

  SMS:
    mechanism:      Short URL with referral_code param
    tracking:       UTM tracking
    rate_limit:     Max 10 SMS invites per user per day

  EMAIL:
    mechanism:      Template email with personalized referral link
    tracking:       UTM + open pixel
    rate_limit:     Max 20 email invites per user per day

  QR_CODE:
    mechanism:      QR image generated with embedded invite token
    tracking:       QR scan event via SHARE_LINK_AGENT
    expiry:         24 hours
    rate_limit:     Max 5 QR generation requests per user per hour

  NFC:
    mechanism:      NFC payload with deep-link + invite_code
    tracking:       NFC tap event via SHARE_LINK_AGENT
    rate_limit:     Max 5 NFC payload writes per user per day

  DIRECT_LINK:
    mechanism:      Copy-pasteable URL
    tracking:       UTM + referrer match
    rate_limit:     NONE (passive share)

  IN_APP:
    mechanism:      In-app invite to registered email (non-users get email)
    tracking:       Internal event log
    rate_limit:     Max 50 in-app invites per user per day
```

---

## 8️⃣ FRAUD DETECTION — DETAILED RULES (LOCKED)

### Fraud Classification System

```yaml
CLASS_A_FRAUD:
  description:    Definitive, zero-tolerance violations
  rules:
    - SELF_REFERRAL: invitee_user_id == inviter_user_id
    - CROSS_TENANT:  invite code used across tenant boundary
    - BANNED_USER:   inviter or invitee under permanent fraud ban
  action:         HALT_EXECUTION + BLOCK + LOG_SECURITY_VIOLATION
  reward:         PERMANENTLY_BLOCKED for this conversion
  escalation:     IMMEDIATE → COMPLIANCE_ADMIN + ANTI_FRAUD_AGENT

CLASS_B_FRAUD:
  description:    Strong indicators of coordinated abuse
  rules:
    - DEVICE_RING:     ≥3 new users sharing same device_fingerprint on same inviter's code
    - IP_RING:         ≥5 new users sharing same /24 subnet on same inviter's code within 24h
    - VELOCITY_ABUSE:  inviter accumulates >50 referrals in 7 days (statistical outlier)
    - DISPOSABLE_EMAIL: invitee email domain in DISPOSABLE_EMAIL_REGISTRY
    - FAST_REGISTER:   invitee completes registration in <15 seconds (bot indicator)
  action:         REWARD_BLOCKED + HOLD_PENDING_REVIEW + NOTIFY_INVITER
  reward:         BLOCKED until COMPLIANCE_ADMIN review (max 72h review SLA)
  escalation:     ANTI_FRAUD_AGENT + COMPLIANCE_ADMIN (non-urgent)

CLASS_C_FRAUD:
  description:    Suspicious signals — caution warranted
  rules:
    - MODERATE_IP_VELOCITY: 2–4 registrations from same /24 in 24h
    - NEW_ACCOUNT_INVITE:   inviter account < 7 days old with referrals
    - CHANNEL_MISMATCH:     referrer_url doesn't match declared channel
    - LOW_ATTRIBUTION_CONF: multi-touch attribution confidence < 0.60
  action:         PROCESS_WITH_HOLD (reward held 48h) + LOG_SUSPICIOUS
  reward:         HELD for 48 hours — auto-released if no additional signals
  escalation:     OBSERVABILITY_AGENT (metric increment only)

FRAUD_REVIEW_WORKFLOW:
  HOLD_SLA:         CLASS_B: 72h | CLASS_C: 48h
  REVIEWER:         COMPLIANCE_ADMIN via internal dashboard
  OUTCOMES:         APPROVE_REWARD | PERMANENTLY_BLOCK | EXTEND_HOLD
  AUTO_RELEASE:     CLASS_C HOLD auto-releases after 48h if no additional fraud signals
  NO_AUTO_APPROVE:  CLASS_B HOLD never auto-approves — human required
```

---

## 9️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS:
  INVITE_GENERATION:    500 RPS baseline | 5,000 RPS peak (campaign launches)
  CONVERSION_EVENTS:    200 RPS baseline | 2,000 RPS peak (post-viral spikes)
  DESIGN_FOR:           20,000 RPS (headroom for 100M users + viral growth loops)

LATENCY_TARGET:
  INVITE_GENERATION:    P50 < 100ms | P99 < 500ms
  CONVERSION_VALIDATE:  P50 < 150ms | P99 < 800ms (ML fraud check included)
  FRAUD_HARD_RULES:     P50 < 10ms (synchronous, in-process)
  FRAUD_ML_SCORE:       P50 < 100ms | P99 < 400ms

MAX_CONCURRENCY:        5,000 concurrent fraud evaluation threads

QUEUE_STRATEGY:
  TECHNOLOGY:           Apache Kafka
  TOPICS:
    - invite.generation.requests.{tenant_id}
    - invite.conversion.events.{tenant_id}
    - invite.channel.attribution.{tenant_id}
    - invite.fraud.signals.{tenant_id}
    - invite.validated.output.{tenant_id}
    - invite.fraud.output.{tenant_id}
    - invite.audit.{tenant_id}          (append-only, separate partition)
  PARTITIONING:         By inviter_user_id hash for consistent processing
  CONSUMER_GROUP:       invite-tracking-cg-{tenant_id}
  DLQ:                  invite.dlq.{tenant_id} (failed events, 7-day retention)
  RETENTION:            14 days (longer than RANK agent — invite fraud patterns need lookback)

CACHE_STRATEGY:
  INVITE_CODE_REGISTRY:         Redis HASH, TTL=NEVER (until code explicitly expired)
  DEDUP_CACHE (event_id):       Redis SET NX, TTL=48h
  MILESTONE_STATE:              Redis HASH per user, TTL=NEVER
  FRAUD_DEVICE_REGISTRY:        Redis SET, TTL=30 days rolling
  IP_VELOCITY_COUNTER:          Redis sorted set with sliding window
  INVITER_REFERRAL_COUNT:       Redis INCR, source of truth backed by PostgreSQL
  USER_FRAUD_STATUS:            Redis cache of ANTI_FRAUD_AGENT verdict, TTL=300s (5min)

EXECUTION_MODEL:
  STATELESS:        TRUE — no local state in agent instance
  IDEMPOTENCY:      event_id dedup via Redis SET NX, 48h TTL
  ASYNC:            TRUE — Kafka-driven, non-blocking
  HORIZONTAL_SCALE: Kubernetes HPA on CPU + Kafka consumer lag
  HARD_RULES:       SYNCHRONOUS (executed inline before ML, blocking)
  ML_INFERENCE:     ASYNC to dedicated ML inference service
```

---

## 🔟 SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  ENFORCEMENT:          Hard — tenant_id validated at every processing stage
  INVITE_CODE_SCOPE:    Every invite code carries embedded tenant_id claim
  CROSS_TENANT_ATTEMPT: CLASS_A_FRAUD + SECURITY_VIOLATION event emitted
  DATABASE:             PostgreSQL RLS — tenant_id column enforced at query level

DOMAIN_ISOLATION:
  COMMUNITY_INVITES:    Community invite codes are domain-scoped
                        Arts community invite cannot seat a Technology domain user
  ENFORCEMENT:          domain_track validated at community invite redemption
  VIOLATION:            REJECT + LOG_DOMAIN_VIOLATION

AUTHORIZATION:
  INBOUND_CALLERS:      Only agents in APPROVED_UPSTREAM_AGENTS may publish to input topics
  CODE_GENERATION:      Only USER_SERVICE may request code generation
  CONVERSION_EVENTS:    Only AUTH_SERVICE may submit conversion events
  HUMAN_ACCESS:         NO direct human access to processing queues
  ADMIN_ACCESS:         COMPLIANCE_ADMIN: read audit records + manage fraud reviews only
                        CANNOT create or modify referral records directly

ENCRYPTION:
  IN_TRANSIT:           TLS 1.3 mandatory
  AT_REST:              AES-256-GCM
  INVITE_CODES:         Stored hashed in registry (SHA-256) — raw code in Redis for fast lookup
  DEVICE_FINGERPRINT:   Stored as SHA-256 hash only — raw fingerprint never stored
  IP_ADDRESSES:         Stored as SHA-256 hash only — raw IPs never persisted

PII_HANDLING:
  COMPUTATION_PIPELINE: user_id (UUID) only — no name, email, phone in processing
  FRAUD_EVIDENCE:       device_fingerprint HASH + ip_hash only (no raw values)
  AI_PROMPTS:           PII stripped before any prompt construction
  AUDIT_RECORDS:        user_id + audit_reference only
  GDPR_COMPLIANCE:      On account deletion, invite records anonymized per GDPR_EXPORT_AGENT
                        Attribution records retained (anonymized) for fraud lookback

INVITE_CODE_SECURITY:
  CODE_ENTROPY:         Minimum 32 bits of randomness per code
  BRUTE_FORCE_GUARD:    Rate limit: >20 failed code redemption attempts per IP per hour = IP_BLOCK
  CODE_REVOCATION:      On inviter FRAUD_SUSPENSION, all active codes IMMEDIATELY revoked
  NO_SEQUENTIAL_CODES:  Codes MUST be cryptographically random — sequential = REJECT at generation
```

---

## 1️⃣1️⃣ AUDIT & TRACEABILITY

Every processing execution emits an immutable audit record:

```json
{
  "audit_id":               "UUID — primary key, globally unique",
  "timestamp_utc":          "ISO8601 UTC",
  "agent_id":               "ITA-002",
  "tenant_id":              "UUID",
  "event_type":             "ENUM[CODE_GENERATED|CONVERSION_ATTEMPTED|VALIDATED|FRAUD_FLAGGED|MILESTONE_REACHED|CODE_REVOKED|HOLD_RELEASED]",
  "inviter_user_id":        "UUID",
  "invitee_user_id":        "UUID — null if CODE_GENERATED event",
  "invite_code":            "STRING — hashed in audit (SHA-256)",
  "channel_attributed":     "ENUM",
  "input_hash":             "SHA-256 of full input payload",
  "output_hash":            "SHA-256 of full output payload",
  "fraud_verdict":          "ENUM[CLEAN|HOLD|FLAGGED|BLOCKED|NA]",
  "fraud_flag_codes":       "ARRAY[STRING]",
  "hard_rules_applied":     "ARRAY[STRING] — which RULE_A through RULE_H fired",
  "model_version":          "STRING",
  "model_fraud_probability":"FLOAT — raw ML score",
  "confidence_score":       "FLOAT",
  "processing_ms":          "INTEGER",
  "milestone_crossed":      "ENUM",
  "reward_signal_emitted":  "BOOLEAN",
  "anomaly_flags":          "ARRAY[STRING]",
  "chain_hash":             "SHA-256 — hash of previous audit record (tamper detection)"
}
```

### Audit Integrity Rules

```
IMMUTABILITY:       Append-only — no UPDATE or DELETE permitted
CHAIN_INTEGRITY:    Each record hashes previous record — chain break = SECURITY_ALERT
RETENTION:          7 years minimum (fraud compliance)
REPLAY:             Given input_hash, full decision is reproducible
EXPORT:             Via COMPLIANCE_EXPORT_AGENT only, with GDPR anonymization
STORAGE:            Separate write-once audit database — isolated from operational DB
```

---

## 1️⃣2️⃣ FAILURE POLICY

```yaml
INVALID_INPUT:
  ACTION:         REJECT_EVENT
  RESPONSE:       Structured error with validation_failure_code
  LOG:            LOG_VALIDATION_FAILURE → audit log
  DLQ:            Route to invite.dlq.{tenant_id} with failure reason
  RETRY:          NOT APPLICABLE — invalid input fixed at source
  ESCALATE_TO:    SOURCE_AGENT + OBSERVABILITY_AGENT

FRAUD_MODEL_UNAVAILABLE:
  ACTION:         APPLY_HARD_RULES_ONLY (RULE_A through RULE_H execute synchronously)
                  ML fraud scoring deferred — set fraud_verdict = HOLD
  LOG:            LOG_ML_UNAVAILABLE + model_version
  RETRY_POLICY:   3 retries with exponential backoff (2s, 8s, 32s) then HOLD
  ESCALATE_TO:    OBSERVABILITY_AGENT + PLATFORM_OPS_ALERT
  NOTE:           Hard rules always execute — ML model down ≠ open door for fraud
                  All conversions processed while ML down = HOLD status

DUPLICATE_REDEMPTION:
  ACTION:         REJECT_EVENT + LOG
  RESPONSE:       DUPLICATE_REDEMPTION error code
  REWARD:         NOT CREDITED — idempotency enforced
  LOG:            LOG_DUPLICATE_REDEMPTION (not a fraud event per se, but logged)

CODE_EXPIRED:
  ACTION:         REJECT_EVENT
  RESPONSE:       CODE_EXPIRED error with invite_code reference
  LOG:            LOG_EXPIRED_CODE_ATTEMPT
  USER_NOTICE:    NOTIFICATION_AGENT informs invitee to request fresh invite

CONFIDENCE_BELOW_THRESHOLD:
  THRESHOLD:      < 0.65 (lower than rank agent — invite fraud requires higher certainty)
  ACTION:         HOLD_PENDING_REVIEW
  LOG:            LOG_LOW_CONFIDENCE_HOLD
  ESCALATE_TO:    COMPLIANCE_ADMIN (within 30 minutes)
  REWARD:         HELD — not blocked, not credited

SELF_REFERRAL_DETECTED:
  ACTION:         HALT_EXECUTION (CLASS_A_FRAUD — immediate)
  LOG:            LOG_SELF_REFERRAL_FRAUD (SECURITY_VIOLATION level)
  ESCALATE_TO:    ANTI_FRAUD_AGENT + COMPLIANCE_ADMIN (IMMEDIATE)
  INVITER_ACTION: FRAUD_FLAG added to inviter account
  REWARD:         PERMANENTLY_BLOCKED for this conversion

CROSS_TENANT_ATTEMPT:
  ACTION:         HALT_EXECUTION + SECURITY_VIOLATION event
  LOG:            LOG_CROSS_TENANT_INVITE_ATTEMPT
  ESCALATE_TO:    COMPLIANCE_ADMIN + SECURITY_OPS (P0)
  INVITER_ACTION: INVESTIGATION triggered

DATA_CORRUPTION:
  ACTION:         HALT_EXECUTION + QUARANTINE affected invite code
  LOG:            LOG_CRITICAL — data corruption
  ESCALATE_TO:    PLATFORM_OPS_ALERT (P0 incident)

GENERAL_RULE:
  SILENT_FAILURES:    FORBIDDEN
  UNKNOWN_ERROR:      HALT + LOG_UNKNOWN_FAILURE + ESCALATE
  DEFAULT_SAFE_STATE: All ambiguous conversions = HOLD (never auto-approve on uncertainty)
```

---

## 1️⃣3️⃣ INTER-AGENT DEPENDENCY MAP

### Upstream Agents

```
AUTH_SERVICE                  → Conversion events (new user registration with invite_code)
USER_SERVICE                  → Code generation requests
SHARE_LINK_AGENT              → Channel attribution events (UTM, QR, NFC)
ANTI_FRAUD_AGENT              → Fraud pre-scores, device fingerprints, IP verdicts
MICROCOMMUNITY_ENGINE         → Community invite generation + seat availability signals
CAMPUS_AMBASSADOR_SERVICE     → Ambassador invite batch events
FEATURE_GATE_AGENT            → Invite-3-to-unlock trigger requests
SCHEDULER_AGENT               → Invite TTL expiry events
```

### Downstream Agents

```
REWARD_LEDGER_AGENT           → REWARD_ELIGIBILITY_SIGNAL (credit XP + premium)
RANK_COMPUTATION_AGENT        → Via REWARD_LEDGER_AGENT (referral XP events)
NOTIFICATION_AGENT            → MILESTONE_REACHED_EVENT + INVITE_ACCEPTED_EVENT
SHARE_TRIGGER_AGENT           → Milestone share card generation
GROWTH_ANALYTICS_AGENT        → ATTRIBUTION_RECORD + all invite event telemetry
LEADERBOARD_AGENT             → Referral leaderboard position updates
FEATURE_GATE_AGENT            → FEATURE_UNLOCK_SIGNAL
MICROCOMMUNITY_ENGINE         → INVITE_ACCEPTED_EVENT (seat allocation)
CAMPUS_AMBASSADOR_SERVICE     → Validated ambassador conversion records
OBSERVABILITY_AGENT           → All failure events + performance metrics
FEATURE_STORE_AGENT           → FEATURE_VECTOR_EMIT
ANTI_FRAUD_AGENT              → Bidirectional: fraud verdict feedback loop
```

### Event Topology

```
[Upstream Agents]
      │
      ▼
Kafka: invite.generation.requests.{tenant_id}
Kafka: invite.conversion.events.{tenant_id}
Kafka: invite.channel.attribution.{tenant_id}
Kafka: invite.fraud.signals.{tenant_id}
      │
      ▼
[INVITE_TRACKING_AGENT]
  Phase 1: HARD_RULES (synchronous, inline)
  Phase 2: ML_FRAUD_SCORE (async inference)
  Phase 3: VERDICT_ASSEMBLY
  Phase 4: EVENT_EMISSION
      │
      ├──▶ Kafka: invite.validated.output.{tenant_id}       → REWARD_LEDGER + NOTIFICATION
      ├──▶ Kafka: invite.fraud.output.{tenant_id}           → ANTI_FRAUD_AGENT + COMPLIANCE
      ├──▶ Kafka: invite.milestone.events.{tenant_id}       → NOTIFICATION + SHARE_TRIGGER
      ├──▶ Kafka: invite.feature.unlock.{tenant_id}         → FEATURE_GATE_AGENT
      ├──▶ Kafka: invite.attribution.{tenant_id}            → GROWTH_ANALYTICS_AGENT
      ├──▶ Kafka: feature.vectors.{tenant_id}               → FEATURE_STORE_AGENT
      └──▶ Kafka: invite.audit.{tenant_id}                  → AUDIT_DATABASE (append-only)
```

---

## 1️⃣4️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent emits structured feature vectors for all invite/referral events:

```json
{
  "EMIT_FEATURE_VECTOR": {
    "user_id":            "UUID — inviter",
    "feature_name":       "STRING — e.g., referral_count_validated_lifetime",
    "feature_value":      "FLOAT or INTEGER",
    "feature_type":       "ENUM[referral_count|fraud_rate|channel_diversity|milestone_tier|conversion_velocity]",
    "tenant_id":          "UUID",
    "timestamp":          "ISO8601 UTC",
    "source_agent":       "INVITE_TRACKING_AGENT",
    "model_version":      "STRING"
  }
}
```

Feature vectors emitted per validated conversion:

```
referral_count_validated_lifetime      — INTEGER (inviter's clean referrals)
referral_fraud_rate_30d                — FLOAT (fraud ratio last 30 days)
referral_channel_primary               — ENUM (most-used channel)
referral_channel_diversity_score       — FLOAT (1 = diverse, 0 = single channel)
referral_velocity_7d                   — INTEGER (last 7 days clean referrals)
milestone_tier_current                 — INTEGER[0–5]
conversion_time_delta_avg_mins         — FLOAT (avg time from share to register)
```

Destination: `FEATURE_STORE_AGENT` → consumed by `PLACEMENT_PROBABILITY_MODEL`, `RECRUITER_MATCH_AGENT`, `GROWTH_ANALYTICS_AGENT`

---

## 1️⃣5️⃣ GROWTH ENGINE HOOK

```yaml
MILESTONE_REACHED_EVENT:
  TRIGGER:      When inviter crosses TIER_1, TIER_2, TIER_3, TIER_4, or TIER_5 threshold
  PAYLOAD:      milestone_tier, inviter_user_id, referral_count_now, tenant_id, badge_id, share_card_template_id
  CONSUMER:     NOTIFICATION_AGENT, SHARE_TRIGGER_AGENT, REWARD_LEDGER_AGENT

SHARE_TRIGGER_EVENT:
  TRIGGER:      All milestone crossings (milestones are inherently shareable moments)
  PAYLOAD:      milestone_type, referral_count, badge_id, share_card_template_id, user_id
  CONSUMER:     SHARE_TRIGGER_AGENT

FEATURE_UNLOCK_SIGNAL:
  TRIGGER:      When TIER_2 (3 referrals) milestone crossed with invite_type=FEATURE_UNLOCK_INVITE
  PAYLOAD:      feature_gate_id, user_id, unlock_condition_met=TRUE
  CONSUMER:     FEATURE_GATE_AGENT

INVITE_ACCEPTED_EVENT:
  TRIGGER:      Every validated invite conversion (any invite_type)
  PAYLOAD:      inviter_user_id, invitee_user_id, invite_type, channel_attributed, tenant_id
  CONSUMER:     NOTIFICATION_AGENT, GROWTH_ANALYTICS_AGENT, MICROCOMMUNITY_ENGINE
```

---

## 1️⃣6️⃣ PERFORMANCE MONITORING

```yaml
OPERATIONAL_METRICS:
  code_generation_success_rate:     codes generated successfully / requests
  conversion_validation_success:    conversions validated / attempts
  fraud_detection_rate:             fraud-flagged / total conversions
  fraud_class_a_rate:               CLASS_A events / total (must approach 0)
  false_positive_review_rate:       HOLD cases cleared by admin / total HOLDs (target < 5%)
  dlq_rate:                         events in DLQ / total processed

LATENCY_METRICS:
  code_generation_p99_ms:           target < 500ms
  conversion_hard_rules_p99_ms:     target < 10ms
  conversion_ml_fraud_p99_ms:       target < 400ms
  conversion_e2e_p99_ms:            target < 800ms
  kafka_consumer_lag:               alert if lag > 5,000 events

FRAUD_QUALITY_METRICS:
  fraud_model_confidence_avg:       rolling 1hr — alert if < 0.75
  hold_auto_release_rate_48h:       % CLASS_C HOLDs auto-released (healthy = > 90%)
  hold_admin_block_rate:            % HOLD cases blocked by admin (tracks real fraud exposure)

GROWTH_METRICS:
  referral_conversion_rate:         conversions / unique code shares (tracked via ATTRIBUTION_RECORD)
  channel_conversion_by_type:       per ENUM channel (WhatsApp vs QR vs Email etc.)
  viral_coefficient:                new users per existing user per 30 days
  milestone_attainment_rate:        % of inviters reaching each tier

INTEGRATION:
  TECHNOLOGY:   Prometheus metrics export
  DASHBOARD:    Grafana (internal ops)
  ALERTING:     PagerDuty via OBSERVABILITY_AGENT
  SLA_TARGET:   conversion_validation_success ≥ 99.5% | fraud_false_negative_rate ≤ 1%
```

---

## 1️⃣7️⃣ VERSIONING POLICY

```yaml
VERSIONING_SCHEME:
  AGENT_SPEC:       ITA-SPEC-v{n}
  ML_MODEL:         ITA-FRAUD-ML-v{n}
  PROMPT_TEMPLATE:  INVITE_FRAUD_EXPLAIN_PROMPT-v{n}
  INVITE_CONFIG:    INVITE_CONFIG-v{n} (TTLs, max uses, milestones, fraud thresholds)
  HARD_RULES:       INVITE_HARD_RULES-v{n} (RULE_A through RULE_H)

MUTATION_RULES:
  ADD_ONLY:         New fields may be added to any schema
  NO_REMOVAL:       Existing fields cannot be removed
  NO_RENAME:        Field names cannot change
  HARD_RULES:       Changes to HARD_RULES require MAJOR version bump + 2-party approval

BACKWARD_COMPATIBILITY:
  INPUT:            Current + N-1 schema versions accepted
  FRAUD_RULES:      N-1 hard rules honored during rollover (no gap in fraud coverage)

MIGRATION_PROTOCOL:
  SHADOW_PERIOD:    72 hours minimum before production promotion
  ROLLBACK:         N-1 hot-standby — rollback within 5 minutes
  NOTIFICATION:     Downstream agents notified 5 business days before MAJOR version
  CHANGELOG:        Every change documented in CHANGELOG-ITA.md

INVITE_CONFIG_GOVERNANCE:
  EDIT_AUTHORITY:   PLATFORM_GOVERNANCE_ADMIN only
  APPROVAL:         2-party sign-off (Governance + Growth Lead)
  FRAUD_THRESHOLD_CHANGE: Requires additional SECURITY_OPS sign-off
  AUDIT:            Every config change logged with timestamp + approver IDs
```

---

## 1️⃣8️⃣ NON-NEGOTIABLE RULES

### This Agent MUST NOT:

```
❌  Credit rewards directly — REWARD_LEDGER_AGENT is the sole credit authority
❌  Auto-approve any conversion where fraud_verdict ≠ CLEAN
❌  Allow self-referral under any circumstance (CLASS_A_FRAUD, no exceptions)
❌  Allow cross-tenant invite redemption (SECURITY_VIOLATION, no exceptions)
❌  Generate invite codes for fraud-suspended users
❌  Store raw IP addresses (hash only, always)
❌  Store raw device fingerprints (hash only, always)
❌  Include PII in AI prompts or feature vectors (UUID only)
❌  Allow ML model to override HARD_RULES (rules execute first, always)
❌  Auto-release CLASS_B fraud HOLDs without human COMPLIANCE_ADMIN review
❌  Allow duplicate redemption of any invite code by same invitee
❌  Emit reward signals for fraud-verdict = HOLD, FLAGGED, or BLOCKED
❌  Modify historical attribution records (corrections create new amendment records)
❌  Delete audit records under any condition
❌  Allow sequential or predictable invite code generation
❌  Process codes from unregistered upstream agents
❌  Count fraudulent conversions toward inviter milestone thresholds
❌  Execute outside declared scope (this agent tracks and validates — does not reward, rank, or notify directly)
❌  Downgrade or revoke previously issued milestone badges (humans decide on fraud clawback)
❌  Assume invite_code is valid without registry lookup (assume INVALID until confirmed VALID)
```

---

## APPENDIX A — INVITE_CODE_REGISTRY SCHEMA (LOCKED)

```sql
TABLE: invite_codes
  code_id             UUID PRIMARY KEY
  tenant_id           UUID NOT NULL          -- tenant isolation enforced
  owner_user_id       UUID NOT NULL          -- inviter
  code_hash           VARCHAR(64) NOT NULL   -- SHA-256 of raw code (unique index)
  invite_type         VARCHAR(32) NOT NULL   -- ENUM enforced at app layer
  channel_hint        VARCHAR(32)
  community_id        UUID                   -- null unless COMMUNITY_INVITE
  feature_gate_id     UUID                   -- null unless FEATURE_UNLOCK
  created_at          TIMESTAMP NOT NULL
  expires_at          TIMESTAMP              -- null = never expires
  max_uses            INTEGER                -- null = unlimited
  current_uses        INTEGER DEFAULT 0
  is_revoked          BOOLEAN DEFAULT FALSE
  revoked_at          TIMESTAMP
  revoked_reason      VARCHAR(64)
  schema_version      VARCHAR(16) NOT NULL

INDEXES:
  UNIQUE(code_hash, tenant_id)
  INDEX(owner_user_id, tenant_id)
  INDEX(community_id) WHERE community_id IS NOT NULL

CONSTRAINTS:
  current_uses <= max_uses (enforced by agent, not DB — DB is truth record)
  expires_at > created_at (if set)
```

## APPENDIX B — REFERRAL_ATTRIBUTION SCHEMA (LOCKED)

```sql
TABLE: referral_attributions
  attribution_id        UUID PRIMARY KEY
  tenant_id             UUID NOT NULL
  invite_code_hash      VARCHAR(64) NOT NULL
  inviter_user_id       UUID NOT NULL
  invitee_user_id       UUID NOT NULL UNIQUE  -- one attribution per user, lifetime
  invite_type           VARCHAR(32) NOT NULL
  channel_attributed    VARCHAR(32) NOT NULL
  conversion_ts         TIMESTAMP NOT NULL
  fraud_verdict         VARCHAR(16) NOT NULL
  fraud_class           VARCHAR(16)
  fraud_flag_codes      TEXT[]
  reward_eligible       BOOLEAN NOT NULL
  model_version         VARCHAR(32) NOT NULL
  confidence_score      NUMERIC(4,3) NOT NULL
  audit_reference       UUID NOT NULL
  schema_version        VARCHAR(16) NOT NULL

CONSTRAINTS:
  UNIQUE(invitee_user_id)                     -- one attribution per user, ever
  CHECK(confidence_score BETWEEN 0.0 AND 1.0)
  NO UPDATE permitted (append-only, enforced via PostgreSQL trigger)
  NO DELETE permitted (enforced via PostgreSQL trigger)
```

## APPENDIX C — FRAUD FLAG CODES (LOCKED)

```yaml
FRAUD_FLAG_CODES:
  SELF_REFERRAL:              Invitee == Inviter
  CROSS_TENANT:               Code used across tenant boundary
  DEVICE_RING_DETECTED:       ≥3 users sharing device fingerprint hash
  IP_RING_DETECTED:           ≥5 users from /24 subnet
  HIGH_VELOCITY_INVITER:      >50 referrals in 7 days
  DISPOSABLE_EMAIL:           Known disposable email domain
  BOT_REGISTRATION_SPEED:     Registration completed in <15 seconds
  BANNED_USER_INVITER:        Inviter under fraud ban
  BANNED_USER_INVITEE:        Invitee under fraud ban
  IP_VELOCITY_1H:             >5 registrations from IP in 1 hour
  CODE_EXHAUSTED:             Code reached max_uses
  CODE_EXPIRED:               Code past TTL
  CHANNEL_MISMATCH:           Referrer URL ≠ declared channel
  LOW_ATTRIBUTION_CONFIDENCE: Multi-touch attribution confidence < 0.60
  NEW_ACCOUNT_VELOCITY:       Inviter account < 7 days old with referrals
  DUPLICATE_REDEMPTION:       Invitee already attributed elsewhere
```

---

## SEAL RECORD

```
╔══════════════════════════════════════════════════════════════════════════╗
║  DOCUMENT:           INVITE_TRACKING_AGENT.md                           ║
║  AGENT_ID:           ITA-002                                            ║
║  SPEC_VERSION:       ITA-SPEC-v1.0.0                                    ║
║  PLATFORM:           ECOSKILLER ANTIGRAVITY                             ║
║  SEALED:             2025-01-01T00:00:00Z                               ║
║  STATUS:             PRODUCTION-LOCKED                                  ║
║  NEXT_REVIEW:        TRIGGERED BY VERSION BUMP ONLY                     ║
║  GOVERNANCE:         PLATFORM_GOVERNANCE_ADMIN + GROWTH_LEAD            ║
║                      + SECURITY_OPS (for fraud threshold changes)       ║
║                                                                         ║
║  THIS DOCUMENT CANNOT BE EDITED WITHOUT:                                ║
║  1. Formal version bump (ITA-SPEC-v1.x.x or v2.0.0)                   ║
║  2. Two-party approval (+ Security sign-off for fraud rules)            ║
║  3. Downstream agent notification (min 5 business days)                 ║
║  4. Shadow testing period (min 72 hours)                                ║
║  5. Changelog entry in CHANGELOG-ITA.md                                 ║
║  6. ANTI_FRAUD_AGENT team review for any fraud threshold changes        ║
╚══════════════════════════════════════════════════════════════════════════╝
```
