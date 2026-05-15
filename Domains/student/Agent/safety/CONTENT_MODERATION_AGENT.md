# 🔒 CONTENT_MODERATION_AGENT.md
## SEALED & LOCKED SPECIFICATION
### ML, Intelligence & Safety Owner | Ecoskiller Antigravity Platform
**Classification:** ENTERPRISE CRITICAL | CONTENT SAFETY LOCKED | IMMUTABLE REFERENCE

---

## 📋 EXECUTIVE SUMMARY

The **Content Moderation Agent (CMA)** is a deterministic, real-time content safety system designed to detect, filter, and remediate harmful, inappropriate, exploitative, and policy-violating content across all platform interactions—text communications, assessments, project submissions, group discussions (Dojo), images, and external links. It operates as the primary enforcement mechanism for platform safety policies while balancing free expression with protection from harm.

**Core Principles:**
- ✅ **Zero Tolerance for Exploitation Content** - CSAM, trafficking, abuse material removed immediately
- ✅ **Proportional Response** - Punishments match violation severity
- ✅ **Transparent Policy** - Users understand what's prohibited and why
- ✅ **Appeal Rights** - Users can dispute content moderation decisions
- ✅ **Multi-Modal Analysis** - Text, images, links, and context all evaluated
- ✅ **Context Awareness** - Same content different context = different decision
- ✅ **Continuous Learning** - Model improves from appeals and corrections

**Execution Model:** Hybrid (75% ML detection + 25% rule-based enforcement)  
**Tenant Scope:** Strict isolation per institution/enterprise  
**Failure Policy:** HALT + ESCALATE (safety-first, no borderline content published)  
**Audit Trail:** Append-only, immutable, permanent (7+ years retention)

---

## 1️⃣ AGENT IDENTITY (MANDATORY - LOCKED)

```
AGENT_NAME                    = CONTENT_MODERATION_AGENT
AGENT_ID                      = CMA_V1_20250227_SEALED
SYSTEM_ROLE                   = Content Safety, Policy Enforcement, Harm Prevention
PRIMARY_DOMAIN                = All User-Generated Content (text, image, media)
EXECUTION_MODE                = DETERMINISTIC + REAL-TIME (NO INTERPRETATION)
DATA_SCOPE                    = All content: messages, posts, uploads, links, profiles
TENANT_SCOPE                  = STRICT ISOLATION (No cross-tenant content leakage)
FAILURE_POLICY                = HALT_AND_REMOVE (safety first, content safety non-negotiable)
CREATIVE_INTERPRETATION       = FORBIDDEN
ASSUMPTION_FILLING            = FORBIDDEN
DEFAULT_BEHAVIOR              = REMOVE (when uncertain about content safety, err safe)
MUTATION_POLICY               = ADD_ONLY (No retroactive content deletion without audit)
COMPLIANCE_STANDARD           = COPPA, GDPR, UNCRC, Platform ToS, CDA 230
ENFORCEMENT_PHILOSOPHY        = Proportional, transparent, appealable
```

**Lock Status:** 🔐 IMMUTABLE | Hash: SHA256(spec_v1_sealed)  
**Owner:** Content Safety Officer + Compliance Officer  
**Review Interval:** Bi-weekly (content landscape evolves rapidly)  
**Authorization Required:** Content Safety Officer + Compliance Officer  
**Escalation Path:** Always → Content Safety Officer (no delays for harmful content)

---

## 2️⃣ PURPOSE DECLARATION (LOCKED)

### Problem Statement

Ecoskiller is an open platform where students, mentors, and educators collaborate. Bad actors exploit this openness:

1. **Exploitative Content**
   - Child sexual abuse material (CSAM)
   - Human trafficking solicitation
   - Sexual exploitation imagery
   - Violence and gore
   - Hate speech and discrimination

2. **Harmful Behavior**
   - Cyberbullying and harassment
   - Threats and intimidation
   - Doxxing and privacy violations
   - Spam and commercial exploitation
   - Scams and fraud

3. **Policy Violations**
   - Academic dishonesty (cheating, plagiarism)
   - Impersonation and identity fraud
   - Spam and commercial advertising
   - Self-harm content
   - Misinformation and disinformation

4. **Platform Abuse**
   - Coordinated harassment campaigns
   - Bot networks for artificial engagement
   - Copyright/trademark infringement
   - Illegal content (drugs, weapons, etc.)
   - Manipulation and social engineering

### Solution Scope

CMA provides multi-layered content safety:

**Layer 1: Real-Time Detection**
- Pre-publication scanning (block harmful content before users see it)
- Hash matching (known CSAM, copyrighted material)
- Keyword detection (explicit content, hate speech)

**Layer 2: Context Analysis**
- Natural language understanding (what is being said)
- Sentiment analysis (is tone aggressive/threatening)
- Relationship context (peer vs mentor, known relationship)
- Academic context (is this plagiarism or legitimate reference?)

**Layer 3: Media Analysis**
- Image classification (CSAM, NSFW, violence, hate imagery)
- Optical character recognition (text in images analyzed for hidden harmful content)
- Video analysis (violence, exploitation indicators)
- Metadata scanning (location data risks, device fingerprinting)

**Layer 4: Enforcement & Appeal**
- Content removal (if violation confirmed)
- Account actions (warning, suspension, ban)
- User notification (clear explanation of policy violated)
- Appeal process (users can dispute decisions)

### Input Consumed

```
- Text content (messages, posts, comments, assessments, projects)
- Images/media (uploads, profile pictures, evidence submissions)
- External links (URLs shared, external references)
- User context (age, role, relationship to recipient)
- Behavioral context (communication history, context of discussion)
- Metadata (timestamps, location, device, upload sources)
- Historical context (previous policy violations, warnings)
```

### Output Produced

```
MODERATION_DECISION:
{
  decision_id: UUID,
  content_id: UUID,
  decision: enum [APPROVED | REMOVED | FLAGGED | ESCALATED],
  violation_type: enum [CSAM | EXPLOITATION | HARASSMENT | HATE_SPEECH | VIOLENCE | SPAM | COPYRIGHTED | MISINFORMATION | SELF_HARM | POLICY_VIOLATION],
  severity: enum [CRITICAL | HIGH | MEDIUM | LOW],
  confidence_score: float [0-1],
  
  enforcement_action: enum [NONE | WARN_USER | REMOVE_CONTENT | REMOVE_CONTENT_AND_WARN | SUSPEND_ACCOUNT | BAN_ACCOUNT],
  user_notification: string (explanation sent to user),
  appeal_available: boolean,
  
  audit_reference: UUID,
  timestamp_utc: ISO8601,
  decision_reasoning: string
}
```

---

## 3️⃣ INPUT CONTRACT (STRICT - LOCKED)

### Input Schema Specification

```json
{
  "INPUT_SCHEMA": {
    "required_fields": [
      {
        "field": "content_context",
        "type": "object",
        "schema": {
          "content_id": { "type": "UUID", "description": "Unique content identifier" },
          "content_type": { 
            "type": "enum",
            "values": [
              "TEXT_MESSAGE",
              "POST",
              "COMMENT",
              "ASSESSMENT_SUBMISSION",
              "PROJECT_SUBMISSION",
              "DOJO_POST",
              "IMAGE_UPLOAD",
              "VIDEO_UPLOAD",
              "LINK_SHARED",
              "PROFILE_DESCRIPTION",
              "DIRECT_MESSAGE"
            ],
            "required": true
          },
          "content_text": { 
            "type": "string or null",
            "description": "Full text content",
            "required": true
          },
          "content_media": { 
            "type": "binary or null",
            "description": "Image/video binary data",
            "required": false
          },
          "external_link": { 
            "type": "string or null",
            "description": "URL if link shared",
            "required": false
          },
          "timestamp_created": { "type": "ISO8601", "required": true },
          "tenant_id": { "type": "UUID", "description": "Tenant isolation enforcement" }
        },
        "required": true
      },
      {
        "field": "user_context",
        "type": "object",
        "description": "Information about content creator",
        "schema": {
          "user_id": { "type": "UUID", "description": "Hashed user identifier" },
          "user_role": { 
            "type": "enum",
            "values": ["STUDENT", "MENTOR", "EVALUATOR", "PARENT", "ADMIN"],
            "required": true
          },
          "age_cohort": { 
            "type": "enum",
            "values": ["13-14", "15-16", "17", "18+"],
            "required": false
          },
          "account_age_days": { "type": "integer", "required": true },
          "prior_violations": { 
            "type": "integer",
            "description": "Number of previous policy violations",
            "required": false
          },
          "account_status": { 
            "type": "enum",
            "values": ["ACTIVE", "WARNED", "SUSPENDED", "BANNED"],
            "required": true
          }
        },
        "required": true
      },
      {
        "field": "relationship_context",
        "type": "object",
        "description": "Context of where content was shared",
        "schema": {
          "recipient_user_ids": { 
            "type": "array[UUID]",
            "description": "Who sees this content (direct message recipients, group members, public)",
            "required": true
          },
          "is_public": { 
            "type": "boolean",
            "description": "Visible to all or restricted",
            "required": true
          },
          "context_domain": { 
            "type": "enum",
            "values": ["ACADEMIC", "SOCIAL", "DIRECT_MESSAGE", "DOJO_DISCUSSION", "PROJECT", "SKILL_LEARNING"],
            "required": true
          },
          "parent_content_id": { 
            "type": "UUID or null",
            "description": "If reply/comment, what is it responding to",
            "required": false
          }
        },
        "required": true
      },
      {
        "field": "moderation_trigger",
        "type": "object",
        "description": "What triggered moderation review",
        "schema": {
          "trigger_type": { 
            "type": "enum",
            "values": [
              "AUTOMATED_SCAN",
              "USER_REPORT",
              "KEYWORD_DETECTION",
              "IMAGE_ANALYSIS",
              "LINK_SCAN",
              "BEHAVIORAL_PATTERN",
              "MANUAL_REVIEW",
              "LAW_ENFORCEMENT_REPORT"
            ],
            "required": true
          },
          "reporter_id": { 
            "type": "UUID or null",
            "description": "User who reported (if user report)",
            "required": false
          },
          "report_reason": { 
            "type": "string or null",
            "description": "Why was content reported",
            "required": false
          }
        },
        "required": true
      }
    ],
    
    "optional_fields": [
      { "field": "previous_moderation_decisions", "type": "array[UUID]", "description": "Related prior decisions" },
      { "field": "law_enforcement_report", "type": "object", "description": "If law enforcement request" },
      { "field": "copyright_holder_report", "type": "object", "description": "If copyright/DMCA complaint" }
    ],
    
    "validation_rules": [
      {
        "rule": "CONTENT_MUST_BE_PROVIDED",
        "description": "Either text OR media must be provided (not both null)",
        "enforcement": "REJECT_INPUT"
      },
      {
        "rule": "TENANT_ISOLATION",
        "description": "Content must belong to user's tenant",
        "enforcement": "REJECT_INPUT"
      },
      {
        "rule": "TIMESTAMP_VALIDITY",
        "description": "Content timestamp must be recent (< 24 hours old)",
        "enforcement": "REJECT_INPUT"
      },
      {
        "rule": "USER_CONTEXT_REQUIRED",
        "description": "Cannot moderate without knowing who posted and in what context",
        "enforcement": "REJECT_INPUT"
      }
    ],
    
    "security_checks": [
      {
        "check": "ENCRYPTION_IN_TRANSIT",
        "description": "All content must arrive encrypted (TLS 1.3+)",
        "enforcement": "MANDATORY"
      },
      {
        "check": "PAYLOAD_SIGNATURE",
        "description": "Cryptographic signature of content for integrity",
        "enforcement": "MANDATORY"
      },
      {
        "check": "USER_ID_HASHING",
        "description": "User IDs hashed (no raw IDs in moderation logs)",
        "enforcement": "MANDATORY"
      }
    ]
  }
}
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT - LOCKED)

### Output Schema Specification

```json
{
  "OUTPUT_SCHEMA": {
    "moderation_decision": {
      "decision_id": {
        "type": "UUID",
        "description": "Unique immutable identifier for this decision",
        "immutable": true
      },
      "content_reference": {
        "type": "UUID",
        "description": "Audit trail: link to content_id being moderated"
      },
      "user_id_hashed": {
        "type": "SHA256_hash",
        "description": "Hashed user identifier (never raw)"
      },
      "tenant_id": {
        "type": "UUID",
        "description": "Tenant isolation enforcement"
      },
      "timestamp_utc": {
        "type": "ISO8601",
        "description": "UTC timestamp of moderation decision"
      },
      "decision_status": {
        "type": "enum",
        "values": [
          "APPROVED",
          "CONTENT_REMOVED",
          "CONTENT_FLAGGED",
          "PENDING_HUMAN_REVIEW",
          "ESCALATED_TO_LAW_ENFORCEMENT",
          "APPEALED"
        ],
        "locked": true
      },
      
      "violation_assessment": {
        "type": "object",
        "schema": {
          "violation_detected": {
            "type": "enum",
            "values": [
              "NONE",
              "CSAM",
              "SEXUAL_EXPLOITATION",
              "HUMAN_TRAFFICKING",
              "VIOLENCE_GORE",
              "HATE_SPEECH",
              "HARASSMENT_BULLYING",
              "THREATS_INTIMIDATION",
              "DOXXING_PRIVACY",
              "SPAM_SCAM",
              "COPYRIGHTED_MATERIAL",
              "PLAGIARISM",
              "MISINFORMATION",
              "SELF_HARM",
              "ILLEGAL_CONTENT",
              "IMPERSONATION",
              "POLICY_VIOLATION"
            ]
          },
          "violation_severity": {
            "type": "enum",
            "values": ["CRITICAL", "HIGH", "MEDIUM", "LOW"],
            "description": "CRITICAL = immediate removal, HIGH = remove + warn, MEDIUM = warn only, LOW = monitor"
          },
          "confidence_score": {
            "type": "float [0-1]",
            "description": "Certainty of violation (0.9+ = near certain)"
          },
          "detection_method": {
            "type": "enum",
            "values": ["ML_MODEL", "RULE_BASED", "KEYWORD_MATCH", "HASH_MATCH", "HUMAN_REVIEW"],
            "description": "How was violation detected"
          }
        }
      },
      
      "enforcement_decision": {
        "type": "object",
        "schema": {
          "action": {
            "type": "enum",
            "values": [
              "NONE",
              "WARN_USER",
              "REMOVE_CONTENT",
              "REMOVE_AND_WARN",
              "SUSPEND_ACCOUNT",
              "BAN_ACCOUNT",
              "ESCALATE_LAW_ENFORCEMENT",
              "FLAG_FOR_HUMAN_REVIEW"
            ],
            "description": "What action to take"
          },
          "action_justification": {
            "type": "string",
            "description": "Clear explanation of why this action chosen"
          },
          "repeat_violation_factor": {
            "type": "float [1.0-3.0]",
            "description": "Multiplier for repeat offenders (first offense = 1.0, repeat = 1.5-3.0)"
          },
          "user_warning_level": {
            "type": "integer [0-3]",
            "description": "Number of warnings before escalation (0=first offense, 3=ban recommended)"
          }
        }
      },
      
      "user_communication": {
        "type": "object",
        "schema": {
          "notify_user": {
            "type": "boolean",
            "description": "Should user be notified of decision"
          },
          "notification_template": {
            "type": "enum",
            "values": [
              "CONTENT_REMOVED_VIOLATION",
              "CONTENT_REMOVED_CSAM",
              "CONTENT_REMOVED_HARASSMENT",
              "WARNING_POLICY_VIOLATION",
              "ACCOUNT_SUSPENDED",
              "ACCOUNT_BANNED",
              "APPEAL_DENIED"
            ]
          },
          "user_message": {
            "type": "string",
            "description": "Non-technical explanation sent to user",
            "max_length": 500
          },
          "policy_reference": {
            "type": "string",
            "description": "Which policy violated (link to community guidelines)"
          },
          "appeal_information": {
            "type": "object",
            "properties": {
              "can_appeal": "boolean",
              "appeal_deadline": "ISO8601",
              "appeal_process_url": "string"
            }
          }
        }
      },
      
      "escalation_data": {
        "type": "object",
        "schema": {
          "escalate_to_human_review": {
            "type": "boolean",
            "description": "Should human review this decision"
          },
          "escalate_to_safety_officer": {
            "type": "boolean",
            "description": "Serious enough for safety officer attention"
          },
          "escalate_to_law_enforcement": {
            "type": "boolean",
            "description": "Criminal activity or harm requiring police"
          },
          "law_enforcement_contact": {
            "type": "string or null",
            "description": "Which agency (FBI, local police, NCMEC)"
          },
          "evidence_preservation": {
            "type": "boolean",
            "description": "Preserve content for investigation"
          }
        }
      }
    },
    
    "audit_trail": {
      "type": "object",
      "immutable": true,
      "schema": {
        "timestamp_utc": "ISO8601",
        "decision_path": {
          "type": "array[object]",
          "items": {
            "step": "integer",
            "check_name": "string",
            "result": "enum[PASS|FAIL|ESCALATE]",
            "confidence": "float [0-1]",
            "timestamp": "ISO8601"
          }
        },
        "model_version": "string",
        "appeal_history": {
          "type": "array[object]",
          "items": {
            "appeal_id": "UUID",
            "appeal_reason": "string",
            "appeal_decision": "enum[UPHELD|REVERSED|ESCALATED]",
            "appeal_timestamp": "ISO8601"
          }
        },
        "signature": "HMAC_SHA256(decision_payload + secret_key)"
      }
    },
    
    "next_actions": {
      "type": "array[object]",
      "items": {
        "action_type": "enum[CONTENT_REMOVAL | ACCOUNT_SUSPENSION | NOTIFICATION | ESCALATION | MONITORING]",
        "target_agent": "string",
        "priority": "enum[CRITICAL | HIGH | NORMAL]",
        "deadline": "ISO8601"
      }
    }
  }
}
```

---

## 5️⃣ CONTENT MODERATION LOGIC (LOCKED - 75% ML + 25% RULES)

### Architecture Overview

```
USER GENERATES CONTENT
  │
  ├─→ LAYER 1: IMMEDIATE SAFETY CHECKS (< 100ms)
  │   ├─ CSAM hash matching (known harmful material)
  │   ├─ Keyword blocklist (explicit content, slurs)
  │   ├─ Link scanning (malware, exploitation sites)
  │   └─ Metadata check (location data, device risks)
  │   ↓ CRITICAL violations → REMOVE IMMEDIATELY
  │
  ├─→ LAYER 2: ML-BASED ANALYSIS (75%)
  │   ├─ Text classification (hate speech, harassment, threats)
  │   ├─ Sentiment analysis (aggressive tone, bullying indicators)
  │   ├─ Image analysis (NSFW, violence, hate imagery)
  │   ├─ Behavioral analysis (spam patterns, coordinated abuse)
  │   └─ Context understanding (is this academic or exploitative?)
  │   ↓ HIGH violations → FLAG FOR REMOVAL
  │
  ├─→ LAYER 3: RULE-BASED CHECKS (25%)
  │   ├─ Academic policy (plagiarism detection, cheating)
  │   ├─ Relationship rules (appropriate communication, mentor boundaries)
  │   ├─ Domain rules (professional vs casual language by context)
  │   ├─ Age rules (age-appropriate content)
  │   └─ Escalation rules (known abusers, repeat violations)
  │   ↓ POLICY violations → TAKE APPROPRIATE ACTION
  │
  ├─→ LAYER 4: CONTEXT EVALUATION
  │   ├─ Relationship between users (peers, mentor-student)
  │   ├─ Public vs private (what's OK in DM vs public post)
  │   ├─ Academic context (references vs plagiarism)
  │   └─ User history (repeat offender vs first offense)
  │   ↓
  │
  └─→ MODERATION DECISION
     ├─ Approve (safe to publish)
     ├─ Warn user (policy violation, educational opportunity)
     ├─ Remove content (clear violation)
     ├─ Remove + suspend account (serious/repeat violation)
     └─ Escalate law enforcement (criminal activity, harm)
```

### Layer 1: Immediate Safety Checks (Deterministic - < 100ms)

#### 1.1 CSAM Hash Matching

```
RULE_SET: CHILD_SEXUAL_ABUSE_MATERIAL_DETECTION
EXECUTION_MODE: DETERMINISTIC (CRYPTOGRAPHIC HASH MATCHING)

Input: image_file (if media uploaded)

Process:
  1. Compute hash: SHA256(image) or PhotoDNA (NCMEC standard)
  
  2. Check against:
     ├─ NCMEC CyberTipline database (official CSAM hashes)
     ├─ FBI database
     ├─ Interpol database
     └─ Platform's internal CSAM blocklist
  
  3. If MATCH:
     ├─ Severity: CRITICAL (highest level)
     ├─ Action: 
     │  ├─ IMMEDIATELY REMOVE CONTENT
     │  ├─ SUSPEND ACCOUNT
     │  ├─ ESCALATE TO LAW ENFORCEMENT (automatic)
     │  ├─ PRESERVE EVIDENCE (don't modify)
     │  └─ ALERT PARENTS (if applicable)
     ├─ Legal: Mandatory reporting (COPPA, state laws)
     └─ NO HUMAN REVIEW NEEDED (automatic escalation)
  
  4. If NO MATCH but suspicious image:
     └─ Flag for manual review by trained moderator

Output: Hash match (YES/NO), CSAM status, law enforcement escalated
```

#### 1.2 Keyword Blocklist & Phrase Matching

```
RULE_SET: EXPLICIT_CONTENT_BLOCKING
EXECUTION_MODE: DETERMINISTIC (REGEX + KEYWORD MATCHING)

Input: content (text, OCR from images)

Process:
  1. Scan for CRITICAL keywords:
     ├─ CSAM solicitation ("send me photos", "meet in person")
     ├─ Sexual exploitation ("I'll pay for nude", "sell yourself")
     ├─ Violence ("going to kill you", "bomb threat")
     ├─ Slurs (racial, ethnic, gender-based epithets)
     ├─ Threats ("I know where you live", "I'm coming for you")
     └─ Self-harm ("going to kill myself", detailed methods)
  
  2. If CRITICAL keyword found:
     ├─ Severity: CRITICAL or HIGH (depends on specificity)
     ├─ Action: REMOVE_CONTENT + investigate user
     ├─ Flag: Possible harm to self/others
     └─ Escalate: Law enforcement if threats/violence
  
  3. Scan for HIGH keywords:
     ├─ Hate speech (systematic dehumanization)
     ├─ Harassment (targeted, repeated abuse)
     ├─ Doxxing (sharing personal info without consent)
     ├─ Sexual content (explicit descriptions)
     └─ Illegal content (drug sales, weapons)
  
  4. If HIGH keyword found:
     ├─ Severity: HIGH
     ├─ Action: REMOVE_CONTENT + warn user
     ├─ Escalation: Review user's history for patterns
     └─ Monitor: Increase oversight of this user
  
  5. Scan for MEDIUM keywords:
     ├─ Aggressive language (swearing, insults)
     ├─ Spam indicators (repetitive, commercial)
     ├─ Misinformation (factually false claims)
     └─ Policy violations (off-topic, inappropriate for context)
  
  6. If MEDIUM keyword found:
     ├─ Severity: MEDIUM
     ├─ Action: WARN_USER (educational)
     └─ Escalate: If repeat violations, escalate to removal

Output: Content risk (SAFE / SUSPICIOUS / HIGH / CRITICAL), matched phrases
```

#### 1.3 Link Scanning & URL Safety

```
RULE_SET: LINK_SAFETY_CHECK
EXECUTION_MODE: DETERMINISTIC (EXTERNAL SERVICE LOOKUP)

Input: url_shared (if link posted)

Process:
  1. Extract URL and check against:
     ├─ Google Safe Browsing API (malware, phishing)
     ├─ Virustotal (antivirus detection)
     ├─ URLhaus (phishing/malware databases)
     └─ Custom blocklist (exploitation sites, CSAM hosts)
  
  2. If UNSAFE:
     ├─ Severity: 
     │  ├─ CSAM host: CRITICAL
     │  ├─ Malware: HIGH
     │  ├─ Phishing: HIGH
     │  └─ Suspicious: MEDIUM
     ├─ Action: REMOVE_LINK + alert user
     └─ Warning: "This link is dangerous and has been removed"
  
  3. If SAFE:
     └─ Continue to ML analysis

Output: Link safety status (SAFE / SUSPICIOUS / UNSAFE), risk category
```

#### 1.4 Metadata & Privacy Checks

```
RULE_SET: METADATA_PRIVACY_ANALYSIS
EXECUTION_MODE: DETERMINISTIC (EXTRACTION + LOCATION CHECK)

Input: image_metadata, uploaded_media

Process:
  1. Extract metadata:
     ├─ GPS coordinates (location data)
     ├─ Device info (phone model, unique identifiers)
     ├─ Timestamp (when photo taken)
     ├─ Camera settings (used to identify device)
     └─ EXIF data (can reveal habits, patterns)
  
  2. If sensitive metadata found:
     ├─ Location data + minor = CRITICAL privacy risk
     ├─ Unique device ID = device fingerprinting risk
     ├─ Timestamp pattern = routine/schedule exposure
     ├─ Action: REMOVE metadata before publishing
     └─ Warn: "Location data removed for your safety"
  
  3. If image modification detected:
     ├─ Check for signs of manipulation
     ├─ Verify authenticity (used to prove deepfakes)
     └─ Flag if suspicious (could be misinformation)

Output: Privacy risks identified, metadata status, safety warnings
```

### Layer 2: ML-Based Detection (75%)

#### 2.1 Text Classification (Hate Speech, Harassment, Threats)

```
MODEL_TYPE: TRANSFORMER-BASED TEXT CLASSIFICATION (BERT/RoBERTa)

Features:
  - Semantic meaning (what content actually says)
  - Context (who is speaking to whom about what)
  - Tone (aggressive, sarcastic, sincere)
  - Intent (is speaker trying to harm/harass)
  - Target (individual vs group, named vs unnamed)

Training Data:
  ├─ Public datasets (Hate Speech, Toxic Comments)
  ├─ Platform-specific data (labeled violations from moderators)
  ├─ Law enforcement resources (threat analysis)
  └─ Academic research (harassment, bullying linguistics)

Detection Scope:
  ├─ Hate speech: Systematic dehumanization by identity
  ├─ Harassment: Targeted, repeated, unwanted communication
  ├─ Threats: Credible threats of violence or harm
  ├─ Bullying: Systematic mockery, humiliation, social exclusion
  └─ Incitement: Encouraging others to harm/harass

Output: Classification score (0-1 per category), severity level, confidence
```

#### 2.2 Sentiment & Aggression Analysis

```
MODEL_TYPE: SENTIMENT_ANALYSIS + AGGRESSION_DETECTOR

Purpose: Identify aggressive, threatening, or harmful tone even if no explicit harmful words

Features:
  - Emotional intensity (all-caps, exclamation marks, word emphasis)
  - Hostile language (direct attacks, name-calling)
  - Threat indicators (conditional harm, hypothetical violence)
  - Coercive language ("you must", "or else", manipulation)
  - Dehumanizing language (comparing people to objects/animals)

Detection Examples:
  - "you're an idiot" = LOW (insult)
  - "you're worthless and should die" = CRITICAL (threat + harm)
  - "i hope someone beats you up" = HIGH (incitement to violence)
  - "let's all report this person until they leave" = HIGH (coordinated harassment)
  - "you're wrong about that policy" = LOW (disagreement, not harassment)

Output: Aggression score (0-1), threat likelihood, hostile intent probability
```

#### 2.3 Image Classification (NSFW, Violence, Hate Imagery)

```
MODEL_TYPE: CONVOLUTIONAL NEURAL NETWORK + IMAGE CLASSIFIER

Categories:
  1. CRITICAL:
     ├─ CSAM (caught at Layer 1, but additional check)
     ├─ Real violence/gore (actual injury, death)
     ├─ Human trafficking imagery (exploitation)
     └─ Extreme hate imagery (ISIS, KKK, swastikas in context)
  
  2. HIGH:
     ├─ Sexual content (explicit, nudity)
     ├─ Graphic violence (animation, video game)
     ├─ Weapons (guns, explosives in threatening context)
     └─ Self-harm imagery (cutting, weapons for suicide)
  
  3. MEDIUM:
     ├─ Suggestive content (sexualized but not explicit)
     ├─ Violence simulator (fighting games)
     ├─ Weapons (neutral display, collections)
     └─ Alcohol/drugs (usage imagery)
  
  4. LOW:
     ├─ Natural (nudity in non-sexual context, like art)
     ├─ Medical (educational content about health)
     ├─ Newsworthy (legitimate journalism about violence)
     └─ Context-appropriate (boxing match, martial arts)

Context Matters:
  - Same image: Different severity based on context
  - Medical diagram of reproduction = SAFE (educational)
  - Same image sexualized in comment = REMOVE (inappropriate)
  - Video of boxing match = SAFE (sports)
  - Same violence promoted/glorified = REMOVE (incitement)

Output: Content classification, severity by category, context analysis, confidence
```

#### 2.4 Behavioral Pattern Analysis

```
MODEL_TYPE: ISOLATION_FOREST + SEQUENCE ANALYSIS

Detect spam, coordinated abuse, and platform manipulation:

Features:
  - Frequency patterns (messages per hour, posts per day)
  - Target patterns (same user attacked repeatedly)
  - Content repetition (identical/similar messages)
  - Relationship patterns (communicating with whom)
  - Time patterns (unusual hours, burst activity)
  - Engagement manipulation (artificial likes, follows, engagement)

Red Flags:
  - Sudden increase in messaging (spam, harassment campaign)
  - Same message to many users (mass spam)
  - Coordinated behavior (multiple accounts same content)
  - Targeting same user repeatedly (harassment)
  - Content duplication (copy-paste spam)
  - Bot-like patterns (perfect timing, no variation)
  - Love-bombing followed by demands (coercion pattern)

Output: Spam probability, coordination likelihood, pattern type, confidence
```

### Layer 3: Rule-Based Checks (25%)

#### 3.1 Academic Integrity Checks

```
RULE_SET: PLAGIARISM_AND_CHEATING_DETECTION
EXECUTION_MODE: DETERMINISTIC + PROBABILISTIC

For Assessment & Project Submissions:

Process:
  1. Text similarity checking:
     ├─ Compare against previous student submissions
     ├─ Compare against publicly available sources (Wikipedia, etc.)
     ├─ Compare against known essay mills
     └─ Check for unusual writing style (indicator of copying)
  
  2. Source citation analysis:
     ├─ Verify citations are proper and complete
     ├─ Check if all sources are actually cited
     ├─ Verify quotes are actually from cited sources
     ├─ Check for missed citations (implied copying)
  
  3. Anomaly detection:
     ├─ Is writing style consistent with student's previous work?
     ├─ Is vocabulary/complexity too advanced for student level?
     ├─ Are ideas coherent or fragmented (typical of patchwork copying)?
     ├─ Is technical accuracy suspicious (student doesn't know topic)
  
  4. If plagiarism detected:
     ├─ Severity level depends on extent
     ├─ First offense: WARN + educational opportunity
     ├─ Repeat offense: REMOVE + report to mentor
     ├─ Egregious: Academic misconduct report
  
  5. Context matters:
     ├─ Paraphrasing with citation = OK (learning)
     ├─ Paraphrasing without citation = violation
     ├─ Copying with citation = partial credit issue (not moderation)
     ├─ Copying without citation = plagiarism violation
     ├─ Submitting same work twice = self-plagiarism violation

Output: Plagiarism probability, extent of copying, citation completeness, severity
```

#### 3.2 Relationship & Boundary Rules

```
RULE_SET: COMMUNICATION_BOUNDARY_ENFORCEMENT
EXECUTION_MODE: DETERMINISTIC (CONTEXT-BASED RULES)

Mentor-Student Relationships:
  1. APPROPRIATE:
     ├─ Academic discussion during school hours
     ├─ Project feedback and guidance
     ├─ Career/skill development advice
     ├─ Professional, respectful tone
  
  2. INAPPROPRIATE:
     ├─ Personal topics unrelated to learning
     ├─ Requests for personal information
     ├─ Private communication when public available
     ├─ Off-platform messaging (unmonitored)
     ├─ Emotional support that goes beyond mentoring
     ├─ Any sexual or romantic content
  
  3. If boundary violation detected:
     ├─ Warn both users (mentor and student)
     ├─ Content flagged for human review
     ├─ Mentor assessed for pattern (one-time mistake vs systematic)
     └─ Escalate if serious (remove mentor if exploitation risk)

Peer-to-Peer Communication:
  1. APPROPRIATE:
     ├─ Collaborative academic work
     ├─ Friendly social interaction
     ├─ Peer support and encouragement
     ├─ Respectful disagreement
  
  2. INAPPROPRIATE:
     ├─ Bullying or mockery (even among peers)
     ├─ Exclusion or coordination against someone
     ├─ Romantic pressure or coercion
     ├─ Threats or intimidation
  
  3. If peer violation detected:
     ├─ Warn users involved
     ├─ If bullying: Document for escalation
     ├─ If threat: Escalate to safety officer
     └─ If harassment: Can remove contact between users

Output: Boundary compliance status, violation type, user roles involved, severity
```

#### 3.3 Age-Appropriateness Rules

```
RULE_SET: AGE_APPROPRIATE_CONTENT_ENFORCEMENT
EXECUTION_MODE: DETERMINISTIC (AGE-BASED RULES)

For Content Creators:
  1. 13-14 years:
     ├─ No sexual content (explicit or suggestive)
     ├─ No violence (graphic or detailed)
     ├─ No hate speech or discrimination
     ├─ No threats or intimidation
     ├─ Academic content: Age-appropriate topics
  
  2. 15-16 years:
     ├─ Limited sexual content (educational context OK, not exploitative)
     ├─ Violence: Educational/historical OK, glorification NOT
     ├─ Hate speech: ZERO tolerance
     ├─ Academic: Broader topics allowed
  
  3. 17+ years:
     ├─ More mature content allowed in academic context
     ├─ But exploitation/abuse still not allowed
     ├─ Hate speech still prohibited
     ├─ Clear identification of mature content recommended

For Content Received:
  1. Recommendation system enforcement:
     ├─ Don't recommend explicit content to minors
     ├─ Don't recommend violence to young students
     ├─ Age-gate sensitive topics (require acknowledgment)
  
  2. If minor exposed to inappropriate content:
     ├─ Remove from feed/visibility to that student
     ├─ Notify parents (if serious, like sexual content)
     ├─ Warn content creator (was it mislabeled as age-appropriate?)

Output: Age-appropriateness verdict (SAFE / QUESTIONABLE / UNSAFE for age), action
```

#### 3.4 Repeat Offender Escalation

```
RULE_SET: REPEAT_VIOLATION_ENFORCEMENT
EXECUTION_MODE: DETERMINISTIC (HISTORY-BASED ESCALATION)

Punishment Escalation:

First Offense (within 1 year):
  ├─ Severity: LOW/MEDIUM → WARN + educational message
  ├─ Severity: HIGH → REMOVE_CONTENT + warn
  ├─ Severity: CRITICAL → SUSPEND + human review
  └─ Opportunity to learn and improve

Second Offense:
  ├─ Same severity → Same action + stricter warning
  ├─ More serious → Escalate (if first was MEDIUM, second is HIGH)
  └─ Escalation: May result in temporary suspension

Third Offense:
  ├─ Escalate significantly (LOW → HIGH, MEDIUM → CRITICAL)
  ├─ Consider account suspension (1-30 days)
  ├─ Restrict posting privileges temporarily
  └─ Human review if concerning pattern

Fourth+ Offense:
  ├─ Account ban from platform
  ├─ Serious violations: Can be permanent
  ├─ Exception: If violations widely spaced (2+ years) may reset
  └─ No appeals on fourth ban

Mitigating Factors:
  - Long time since last violation (> 6 months) = may reset
  - Different violation type = not considered escalation
  - Evidence of learning (took educational course) = may reduce severity
  - User disputes and wins on appeal = violation removed from history

Output: Violation history, current violation count, recommended escalation
```

---

## 6️⃣ ENFORCEMENT ACTIONS (DETERMINISTIC - PROPORTIONAL)

```
ACTION MATRIX (based on severity + user history):

SEVERITY → ACTION:

CRITICAL Violations:
├─ CSAM detected → REMOVE + SUSPEND + LAW ENFORCEMENT (automatic)
├─ Exploitation attempt → REMOVE + SUSPEND + LAW ENFORCEMENT
├─ Threats/violence → REMOVE + SUSPEND + LAW ENFORCEMENT
├─ No human review needed (automatic escalation)
└─ User gets clear explanation of why

HIGH Violations:
├─ First offense → REMOVE_CONTENT + WARN + EDUCATE
├─ Repeat offense → REMOVE + SUSPEND (1-30 days)
├─ Pattern detected → SUSPEND + REVIEW + Restrict posting
└─ User can appeal decision

MEDIUM Violations:
├─ First offense → WARN_USER + educate on policy
├─ Repeat offense → REMOVE_CONTENT + WARN
├─ Pattern detected → RESTRICT posting (24-48 hours)
└─ User can learn and improve

LOW Violations:
├─ WARN_USER with clear explanation
├─ Educate on policy through in-app message
├─ No content removal (learning opportunity)
└─ Users almost never escalate
```

---

## 7️⃣ APPEAL PROCESS (LOCKED - TRANSPARENT & FAIR)

```
APPEAL_RIGHTS:

Who can appeal:
├─ Content creator (appealing content removal)
├─ User falsely accused (appealing warning)
├─ Account holder (appealing suspension)
└─ NOT law enforcement escalations (those are final)

Appeal process:
1. User submits appeal with explanation
2. NOT reviewed by original decision-maker
3. Independent human review within 48 hours
4. Clear explanation of decision (upheld or reversed)
5. If reversed: Content restored, apology given if applicable
6. If upheld: Appeal decision is final

Grounds for successful appeal:
├─ False positive (content doesn't actually violate policy)
├─ Procedural error (wrong policy cited)
├─ Context misunderstood (legitimate use of edgy language for effect)
├─ Accidental policy violation (honest mistake)
└─ Disproportionate punishment (action too severe for offense)

NOT grounds for appeal:
├─ "It's just my opinion" (policy is clear)
├─ "I didn't mean to offend" (impact matters, not intent)
├─ "Others say worse things" (whataboutism)
├─ "I'm a celebrity/famous so different rules" (no)
└─ "I paid money, so I shouldn't be punished" (rules apply to all)

Stats tracking:
├─ Appeal rate (% of decisions appealed)
├─ Reversal rate (% of appeals successful)
├─ If high reversal rate → Retrain decision-makers
└─ If low reversal rate → Appeal process working well
```

---

## 8️⃣ FAILURE POLICY (LOCKED - SAFETY FIRST)

```
PRINCIPLE: If uncertain, REMOVE content (safer than publishing harm)

SCENARIO 1: FALSE POSITIVE (Innocent content removed)
├─ Impact: User frustrated, content creator upset
├─ Risk: User learns system is unfair
├─ Decision: ACCEPT THIS RISK (better than false negative)
├─ Mitigation: 
│  ├─ Appeal process available (quick, fair)
│  ├─ Clear explanation (not "policy violation" alone)
│  ├─ Opportunity to resubmit (if it was honest mistake)
│  └─ First offense = warning, not removal (when uncertain)

SCENARIO 2: FALSE NEGATIVE (Harmful content not removed)
├─ Impact: User harmed, exposed to inappropriate/exploitative content
├─ Risk: CRITICAL (unacceptable)
├─ Decision: REVIEW MODEL IMMEDIATELY
├─ Action:
│  ├─ Retrospective analysis of missed case
│  ├─ Model retraining to prevent future misses
│  ├─ Potential user compensation
│  └─ Public explanation if serious (transparency)

SCENARIO 3: SYSTEM FAILURE (Moderation not working)
├─ If CMA unavailable: HALT all content submissions
├─ Prevent new content publication until restored
├─ Return error message: "Safety system temporarily unavailable"
├─ Recovery priority: CRITICAL (1-hour SLA)

SCENARIO 4: MODEL DRIFT (Detection accuracy degrades)
├─ Monitoring: Daily accuracy checks
├─ Alert: If accuracy drops > 5%
├─ Action: RETRAIN model, increase human moderation
├─ Escalation: Temp increase human review if drift detected

NO SCENARIO ALLOWS:
├─ Publishing known harmful content
├─ Delaying removal of clear violations
├─ Suppressing moderation actions for business reasons
├─ Prioritizing engagement over safety
└─ Ignoring false negatives (harmful content missed)
```

---

## 9️⃣ SECURITY ENFORCEMENT (LOCKED)

### Data Protection

```
MODERATION_DATA_PROTECTION:
├─ Never store raw user IDs in decision logs
├─ Hash all user references (SHA256 + salt)
├─ Encrypt decision details (AES-256-GCM)
├─ Removed content archived (can't be recovered by user)
├─ Archive retained 7 years (legal requirement)
└─ Access: Compliance officer, legal team, law enforcement only

APPEAL_DATA_PROTECTION:
├─ Appeal content encrypted
├─ Appeal decision history immutable
├─ Reversal decision logged permanently
└─ User can request full appeal history

MODERATOR_ACCESS:
├─ Moderators see only content being reviewed
├─ No access to user's private data
├─ No data export capabilities (prevent data theft)
├─ All moderator actions logged
├─ Moderator access revoked if suspicious activity

ENCRYPTION:
├─ TLS 1.3 for data in transit
├─ AES-256-GCM for data at rest
├─ Separate encryption keys per tenant
├─ Key rotation every 90 days
└─ HSM (Hardware Security Module) for key storage
```

---

## 🔟 INTER-AGENT DEPENDENCIES (LOCKED)

### Upstream Agents

```
USER_PROFILE_AGENT
├─ Provides: User role, age, account status, history
├─ Critical: Context for content moderation decisions
└─ Version: USER_PROFILE_v3.2.0+

CONTENT_UPLOAD_AGENT
├─ Provides: Content to be moderated (text, images, links)
├─ Frequency: Real-time (before content published)
└─ Version: UPLOAD_v1.5.0+

BEHAVIOR_MONITORING_AGENT
├─ Provides: Behavioral patterns (spam, harassment)
├─ Complements: Content moderation with context
└─ Version: BEHAVIOR_v1.2.0+

LEGAL_COMPLIANCE_SYSTEM
├─ Provides: Legal requirements, DMCA notices, law enforcement requests
├─ Contract: Binding compliance instructions
└─ Version: LEGAL_v1.0.0+
```

### Downstream Agents

```
CONTENT_REMOVAL_AGENT
├─ Consumes: REMOVE decisions from CMA
├─ Function: Delete content from public view
├─ Priority: IMMEDIATE (no delays)
└─ Version: REMOVAL_v1.3.0+

ACCOUNT_SUSPENSION_AGENT
├─ Consumes: SUSPEND/BAN decisions
├─ Function: Restrict account access, remove privileges
├─ Priority: IMMEDIATE
└─ Version: SUSPENSION_v1.2.0+

USER_NOTIFICATION_AGENT
├─ Consumes: Moderation decisions (explain to user)
├─ Function: Send clear, fair explanation
├─ Priority: NORMAL (but important for transparency)
└─ Version: NOTIFICATION_v1.4.0+

APPEAL_MANAGEMENT_AGENT
├─ Consumes: Appeal requests
├─ Function: Track appeals, route to reviewers
├─ Priority: HIGH (appeals need quick resolution)
└─ Version: APPEALS_v1.1.0+

LAW_ENFORCEMENT_ESCALATION
├─ Consumes: CRITICAL violations (CSAM, trafficking, threats)
├─ Function: Coordinate with authorities
├─ Priority: CRITICAL (automatic for serious crimes)
└─ Version: LAW_ENF_v1.0.0+

AUDIT_TRAIL_AGENT
├─ Consumes: All moderation decisions
├─ Function: Immutable logging for compliance
├─ Priority: CRITICAL (every decision logged)
└─ Version: AUDIT_v1.9.0+
```

---

## 1️⃣1️⃣ PERFORMANCE MONITORING (LOCKED)

```yaml
METRICS_PUBLISHED:
  
  ├─ MODERATION_DECISION_METRICS:
  │  ├─ decisions_total (by severity, action type)
  │  ├─ content_approved_rate (%)
  │  ├─ content_removed_rate (%)
  │  ├─ accounts_suspended_rate (%)
  │  ├─ decision_latency_p95 (milliseconds)
  │  ├─ false_positive_rate (%)
  │  ├─ false_negative_rate (%)
  │  └─ consistency_rate (% similar cases handled similarly)
  │
  ├─ VIOLATION_TYPE_METRICS:
  │  ├─ csam_detected (count per month)
  │  ├─ hate_speech_removed (count per month)
  │  ├─ harassment_cases (count per month)
  │  ├─ plagiarism_detected (count per month)
  │  ├─ spam_blocked (count per month)
  │  └─ copyrighted_content_removed (count)
  │
  ├─ APPEAL_METRICS:
  │  ├─ appeals_received (count)
  │  ├─ appeals_approved (%)
  │  ├─ appeals_denied (%)
  │  ├─ appeal_turnaround_time (hours)
  │  └─ user_satisfaction_with_appeals (%)
  │
  ├─ SYSTEM_HEALTH_METRICS:
  │  ├─ moderation_latency_p95 (< 1 second target)
  │  ├─ system_availability (%)
  │  ├─ ml_model_accuracy (%)
  │  ├─ false_alarm_rate (%)
  │  └─ processing_error_rate (%)
  │
  └─ LAW_ENFORCEMENT_COORDINATION:
     ├─ csam_reports_submitted (monthly)
     ├─ law_enforcement_cases_opened (monthly)
     ├─ time_to_escalation (minutes from detection)
     └─ successful_prosecution_rate (if available)

SLA_TARGETS:
  ├─ Critical content removal: < 1 minute
  ├─ High severity content removal: < 1 hour
  ├─ Medium severity moderation: < 24 hours
  ├─ Appeal response: < 48 hours
  ├─ System availability: 99.99% uptime (moderation critical)
  ├─ False positive rate: < 5% (acceptable with appeals)
  └─ False negative rate: < 1% (must catch harmful content)
```

---

## 1️⃣2️⃣ NON-NEGOTIABLE RULES (ABSOLUTE - SEALED)

```
🔒 SEALED & LOCKED RULES - NO EXCEPTIONS

RULE 1: CSAM IS NEVER ACCEPTABLE
  ✗ FORBIDDEN: Allowing CSAM to remain on platform
  ✗ FORBIDDEN: Delaying CSAM removal for any reason
  ✗ FORBIDDEN: Questioning CSAM removal decision
  ✓ REQUIRED: Automatic detection and immediate removal
  ✓ REQUIRED: Law enforcement notification (automatic)
  ✓ REQUIRED: Evidence preservation for investigation

RULE 2: EXPLOITATION & TRAFFICKING CONTENT BANNED
  ✗ FORBIDDEN: Allowing exploitation content
  ✗ FORBIDDEN: Facilitating human trafficking
  ✗ FORBIDDEN: Sexual exploitation material of anyone
  ✓ REQUIRED: Immediate removal and escalation
  ✓ REQUIRED: Law enforcement coordination
  ✓ REQUIRED: Accounts engaged in trafficking banned permanently

RULE 3: MODERATION CANNOT BE BIASED BY POLITICS
  ✗ FORBIDDEN: Suppressing content because of political viewpoint
  ✗ FORBIDDEN: Protecting content based on political alignment
  ✗ FORBIDDEN: Selective enforcement based on ideology
  ✓ REQUIRED: Neutral, consistent policy enforcement
  ✓ REQUIRED: Political speech protected (unless policy violation)
  ✓ REQUIRED: Same rules apply to all users, all viewpoints

RULE 4: TRANSPARENCY IN ENFORCEMENT
  ✗ FORBIDDEN: Removing content without explanation
  ✗ FORBIDDEN: Banning users without clear reason
  ✗ FORBIDDEN: Appeal process hidden or unavailable
  ✓ REQUIRED: Clear explanation of policy violated
  ✓ REQUIRED: Fair appeal process (human review)
  ✓ REQUIRED: Decisions documented and auditable

RULE 5: NO SHADOW BANNING
  ✗ FORBIDDEN: Hiding content from user without telling them
  ✗ FORBIDDEN: Secretly reducing reach/visibility
  ✗ FORBIDDEN: Throttling accounts without notification
  ✓ REQUIRED: Users know if content removed or restricted
  ✓ REQUIRED: Clear communication of any limitations
  ✓ REQUIRED: Appeal option available for all restrictions

RULE 6: PROPORTIONAL ENFORCEMENT
  ✗ FORBIDDEN: Same punishment for minor and major violations
  ✗ FORBIDDEN: Banning users for first minor offense
  ✗ FORBIDDEN: Escalating punishment inconsistently
  ✓ REQUIRED: Punishment matches severity
  ✓ REQUIRED: First-time offenders given warning (usually)
  ✓ REQUIRED: Escalation only for repeat/serious violations

RULE 7: HUMAN REVIEW AVAILABLE
  ✗ FORBIDDEN: All-automated moderation without appeal
  ✗ FORBIDDEN: Denying human review of moderation
  ✗ FORBIDDEN: Hidden AI decision-making
  ✓ REQUIRED: Appeal process accessible to all users
  ✓ REQUIRED: Human review of appeals
  ✓ REQUIRED: Transparency in what is automated vs human

RULE 8: PRIVACY OF MODERATION
  ✗ FORBIDDEN: Publicly shaming users for moderation
  ✗ FORBIDDEN: Posting removed content in moderation examples
  ✗ FORBIDDEN: Sharing user names in policy announcements
  ✓ REQUIRED: Moderation decisions kept private
  ✓ REQUIRED: Users informed privately of decisions
  ✓ REQUIRED: Removed content not publicly displayed
  ✓ REQUIRED: Only system operators can see removed content

RULE 9: NO COMMERCIAL PRESSURE ON MODERATION
  ✗ FORBIDDEN: Weakening moderation for "engagement"
  ✗ FORBIDDEN: Allowing rule-breaking by popular users
  ✗ FORBIDDEN: Prioritizing revenue over safety
  ✓ REQUIRED: Same rules for all users (popular or not)
  ✓ REQUIRED: Safety decisions never compromised for money
  ✓ REQUIRED: Moderation independent of business goals

RULE 10: CONTINUOUS IMPROVEMENT
  ✗ FORBIDDEN: Assuming current system is sufficient
  ✗ FORBIDDEN: Ignoring false positives without analyzing
  ✗ FORBIDDEN: Not learning from missed harmful content
  ✓ REQUIRED: Bi-weekly safety reviews (rapid content landscape)
  ✓ REQUIRED: Analysis of every false positive (improve precision)
  ✓ REQUIRED: Analysis of every false negative (improve recall)
  ✓ REQUIRED: Monthly threat landscape assessment

🔐 THESE RULES ARE IMMUTABLE AND CANNOT BE OVERRIDDEN EXCEPT BY:
  - Court order (documented, signed, legal review)
  - Emergency protecting users from immediate danger
  - Regulatory mandate change (law update)
  
  Even in these cases:
    → Deviation logged as "exception" with justification
    → Content Safety Officer + Legal approval required
    → Audit trail includes all details
    → After emergency, revert to strict compliance
```

---

## APPENDIX A: EXAMPLE MODERATION DECISIONS

### Example 1: Plagiarism Detection

```
CASE: Student submits project that's 75% copied from online source

DETECTION:
├─ Automated scan finds 75% similarity to WikiHow article
├─ Original source not cited
├─ Some sentences verbatim, some paraphrased
├─ Student has submitted unique work before (not serial plagiarizer)

ANALYSIS:
├─ Severity: MEDIUM (significant violation, but first offense)
├─ Context: Academic work, not malicious plagiarism
├─ Confidence: 95% (very clear similarity match)
├─ Intent: Likely copying to save time, not malicious

DECISION:
├─ Action: REMOVE_CONTENT + WARN_USER
├─ Explanation: "This submission is 75% copied from sources without citation. Please resubmit with proper citations."
├─ Educational message: Explain plagiarism policy and how to cite properly
├─ Opportunity: Student can resubmit with citations
├─ Appeal: Yes (if student disputes similarity analysis)

OUTCOME:
├─ Content removed from grade/portfolio
├─ Student receives 0 points for submission (consequence)
├─ Student can resubmit with proper citations
├─ If student resubmits with proper work: Full credit
├─ If pattern develops: Escalate to academic misconduct
```

### Example 2: Cyberbullying Case

```
CASE: Group of students targeting peer in Dojo, calling them stupid/worthless

MESSAGES:
1. "You're so stupid, nobody likes you"
2. "You should just leave, we don't want you here"
3. "Everyone thinks you're worthless" (tagged @ multiple students)
4. Coordinate plan: "Let's all unfollow them, let's report their posts"

DETECTION:
├─ CMA flags harassment pattern
├─ Multiple users targeting one
├─ Coordinated behavior detected
├─ Victim is clearly a minor (age 15)

ANALYSIS:
├─ Severity: HIGH (coordinated harassment of minor)
├─ Context: Bullying campaign, not one-off mean comment
├─ Confidence: 98% (very clear bullying pattern)
├─ Impact: Likely psychological harm to victim

DECISION FOR BULLIES:
├─ Action: REMOVE_POSTS + SUSPEND_ACCOUNTS (24-48 hours)
├─ Education: Required anti-bullying training to restore access
├─ Warning: "This is cyberbullying and violates platform policy"
├─ Escalation: If pattern continues → permanent ban

DECISION FOR VICTIM:
├─ Option 1: Allow victim to request blocking all harassers
├─ Option 2: Temporarily remove victim from Dojo if distressed
├─ Support: Connect victim to counseling resources
├─ Privacy: Don't make harassment public (keep it private)

OUTCOME:
├─ Bullies learn consequence (temporary suspension + education)
├─ Victim protected (harassers suspended, can block)
├─ Mentor notified to provide support
├─ Parent notified (minor being bullied)
├─ Follow-up: Check in with victim after 1 week
```

### Example 3: False Positive Appeal

```
CASE: Content flagged for hate speech but is actually critique

ORIGINAL CONTENT:
"The policy of treating all students equally regardless of starting point
is unfair to advanced students. We should have accelerated tracks for
gifted students so they're not held back by the average."

WHAT HAPPENED:
├─ CMA flagged: "is unfair to advanced students" + context
├─ System: Potential discrimination language detected
├─ Decision: REMOVED for potential discrimination

USER APPEAL:
├─ Student: "This was legitimate academic critique about educational policy"
├─ Claim: "I wasn't discriminating against any group, arguing for tracking"
├─ Evidence: Previous posts showing pattern of educational critiques

HUMAN REVIEW:
├─ Reviewer reads original content
├─ Conclusion: This is policy debate, not hate speech
├─ Context: Student has history of thoughtful critiques
├─ Misclassification: CMA mistook policy debate for discrimination
├─ Intent: Academic, not discriminatory

DECISION:
├─ Action: REVERSE removal, restore content
├─ Apology: "We removed your content by mistake. It was legitimate policy discussion."
├─ Explanation: "Our system misidentified your post as hateful when it was actually a thoughtful critique."
├─ Compensation: Restore to original position, no penalty
├─ Model improvement: Flag this case for model retraining
├─ User satisfaction: Likely restored

OUTCOME:
├─ Student sees moderation is fair and appealable
├─ Trust in system increases
├─ False positive documented for model improvement
├─ System learns to better distinguish debate from discrimination
```

---

## 🔒 SEAL & LOCK CERTIFICATE

```
╔════════════════════════════════════════════════════════════════════╗
║          CONTENT_MODERATION_AGENT.md                               ║
║            SEAL & LOCK CERTIFICATE (IMMUTABLE)                      ║
╠════════════════════════════════════════════════════════════════════╣
║                                                                    ║
║  SPECIFICATION VERSION:     CMA_v1.0.0_SEALED                     ║
║  CREATED:                   2025-02-27T14:45:00Z                  ║
║  OWNER:                     Content Safety Officer + Compliance    ║
║  COMPLIANCE FRAMEWORK:      CDA 230, COPPA, GDPR, Platform ToS    ║
║                                                                    ║
║  SPECIFICATION HASH:        {SHA256_OF_ENTIRE_MD_FILE}            ║
║  HASH VERIFICATION:         IMMUTABLE (Re-hash to verify)          ║
║                                                                    ║
║  LOCKED COMPONENTS:                                               ║
║    ✓ Agent Identity          [SECTION 1]                          ║
║    ✓ Input/Output Contracts  [SECTIONS 3-4]                       ║
║    ✓ Detection Logic         [SECTION 5]                          ║
║    ✓ Enforcement Actions     [SECTION 6]                          ║
║    ✓ Appeal Process          [SECTION 7]                          ║
║    ✓ Non-Negotiable Rules    [SECTION 12]                         ║
║                                                                    ║
║  CHANGES ALLOWED (Add-Only):                                      ║
║    ○ Enhanced detection methods                                   ║
║    ○ Improved ML models                                           ║
║    ○ Better user explanations                                     ║
║    ○ Faster appeal processing                                     ║
║    → All changes require version bump: v1.0.0 → v1.1.0            ║
║    → Previous version remains available (no deletion)             ║
║                                                                    ║
║  CHANGES FORBIDDEN:                                               ║
║    ✗ Removing CSAM detection                                      ║
║    ✗ Weakening harassment enforcement                             ║
║    ✗ Eliminating appeal process                                   ║
║    ✗ Hiding moderation decisions from users                       ║
║    ✗ Biased enforcement (political, favoritism)                   ║
║                                                                    ║
║  REVIEW SCHEDULE:                                                 ║
║    Frequency: BI-WEEKLY (content landscape evolves rapidly)       ║
║    Next review: 2025-03-13                                        ║
║    Reviewers: Content Safety Officer, Compliance Officer          ║
║    Focus: New threats, false positive patterns, appeals analysis   ║
║                                                                    ║
║  DEPLOYMENT AUTHORIZATION:                                        ║
║    Authorized by:  Content Safety Officer + Compliance Officer    ║
║    Signature:      [DOUBLE_SIGNATURE_REQUIRED]                    ║
║    Timestamp:      2025-02-27T14:45:00Z                           ║
║    Deployment SLA: Production IMMEDIATELY (safety critical)       ║
║                                                                    ║
║  TAMPER DETECTION:                                                ║
║    To verify this document has NOT been modified:                 ║
║    1. Copy entire .md file                                        ║
║    2. Run: sha256sum CONTENT_MODERATION_AGENT.md                  ║
║    3. Compare hash with: {SHA256_OF_ENTIRE_MD_FILE}               ║
║    4. If match → Document is SEALED & UNMODIFIED ✓               ║
║    5. If mismatch → TAMPERING DETECTED ✗ (Alert security)         ║
║                                                                    ║
║  AUDIT TRAIL:                                                     ║
║    All moderation decisions logged to: moderation-log topic        ║
║    Retention: 7 years minimum (regulatory requirement)            ║
║    Review by: Content Safety Officer (bi-weekly)                  ║
║    Accessibility: Appeals, compliance audits, law enforcement     ║
║                                                                    ║
║  ENFORCEMENT CLAUSE:                                              ║
║    ANY attempt to weaken content safety = IMMEDIATE ESCALATION    ║
║    Moderation decisions NEVER compromised for business reasons    ║
║    Appeal rights CANNOT be removed                                ║
║    CSAM detection MUST remain at full strength                    ║
║                                                                    ║
╚════════════════════════════════════════════════════════════════════╝

This document is the SINGLE SOURCE OF TRUTH for content moderation
on Ecoskiller. All implementations must conform to this specification.
Any deviations from these rules are PROHIBITED.

SIGNATURE: [CONTENT_SAFETY_OFFICER_SIGNATURE_HERE]
SIGNATURE: [COMPLIANCE_OFFICER_SIGNATURE_HERE]
TIMESTAMP: 2025-02-27T14:45:00Z
STATUS: 🔒 SEALED & LOCKED - CONTENT SAFETY IMMUTABLE
```

---

**END OF SPECIFICATION**

*This document is sealed, locked, and immutable. Content safety is non-negotiable. For clarifications, contact the Content Safety Officer immediately.*
