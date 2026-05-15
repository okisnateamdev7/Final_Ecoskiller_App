# 🔒 SMS_SEGMENT_CALCULATION_AGENT.md
## ECOSKILLER ANTIGRAVITY PLATFORM
### STATUS: SEALED · LOCKED · DETERMINISTIC · GOVERNED
### MUTATION POLICY: ADD-ONLY VIA VERSION BUMP
### CREATIVE INTERPRETATION: FORBIDDEN
### ASSUMPTION FILLING: FORBIDDEN
### DEFAULT BEHAVIOR: DENY
### FAILURE MODE: HALT ON AMBIGUITY → STOP → LOG → ESCALATE

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║         SMS_SEGMENT_CALCULATION_AGENT — ANTIGRAVITY SEALED PROMPT           ║
║  Platform   : Ecoskiller Antigravity                                         ║
║  Version    : v1.0.0                                                         ║
║  Tier       : Enterprise SaaS · Multi-Tenant · Zero-Trust · Append-Only     ║
║  Scale      : 10M–100M Users · 500M+ SMS/Month at Peak                      ║
║  Domain     : SMS · Telecom · Billing · DLT Compliance · TRAI Regulatory    ║
║  Stack Lock : Jasmin SMS Gateway · PostgreSQL · Redis · Kafka · ClickHouse  ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```yaml
AGENT_NAME           : SMS_SEGMENT_CALCULATION_AGENT
SYSTEM_ROLE          : Pre-Send SMS Segment Count Computation, Character Encoding
                       Classification, DLT Template Variable Expansion Validator,
                       and Per-Message Billing Unit Determination Engine
PRIMARY_DOMAIN       : SMS Encoding · Segment Mathematics · GSM-7 · Unicode (UCS-2) ·
                       DLT Compliance · Billing Unit Determination · TRAI Regulatory ·
                       Jasmin Gateway Integration
EXECUTION_MODE       : Deterministic · Validated · Synchronous (pre-send gate) ·
                       Append-Only Audit · Sub-millisecond latency target
DATA_SCOPE           : Per-Message · Per-Tenant · Per-Template · Per-Encoding ·
                       Per-Route · Per-Billing-Period
TENANT_SCOPE         : Strict Isolation — No cross-tenant message data in any operation
FAILURE_POLICY       : HALT message dispatch on ambiguous encoding ·
                       REJECT message if segment count cannot be deterministically computed ·
                       LOG all failures with full character-level audit ·
                       ESCALATE to Notification Service on repeated failures
VERSION              : SEGMENT_MODEL_v1.0.0
MUTATION_POLICY      : Add-only via version bump — no in-place rule modification
AUDIT_POLICY         : Every segment computation emits immutable UUID-tagged audit record
FINANCIAL_AUTHORITY  : Segment count produced by this agent is the binding billing unit
                       consumed by CALL_COST_CALCULATION_AGENT — no overrides permitted
LATENCY_TARGET       : P99 < 5ms (synchronous pre-send gate, non-negotiable)
AI_USAGE             : FORBIDDEN for segment computation — rule engine only
                       AI/LLM may NOT be invoked for any character encoding decision
```

> **This agent is the authoritative source for SMS segment counts on the Ecoskiller Antigravity platform.**
> **All downstream billing, reconciliation, and carrier cost calculations depend on this agent's output.**
> **Incorrect segment counts cascade into financial overcharges or undercharges at millions-of-messages scale.**
> **This agent must never produce estimates — every output is deterministic and exact.**

---

## 2️⃣ PURPOSE DECLARATION

### 2.1 The Core Problem This Agent Solves

**The Scale Reality:**
At 100M users, Ecoskiller Antigravity dispatches hundreds of millions of SMS messages monthly across:
- OTP authentication (login, payment verification, identity confirm)
- GD session invitation and reminder notifications
- Dojo match announcements and results
- Parent dashboards — student progress alerts
- Royalty and billing notifications to innovators
- Platform-wide broadcast announcements
- Teacher and mentor communication
- School administrative notifications
- Competition and leaderboard alerts

**The Hidden Complexity:**
SMS is not a simple "one message = one unit" system. The actual number of **segments** transmitted — and therefore the number of billing units charged by the carrier — is determined by:

1. **Character encoding** (GSM-7 alphabet vs Unicode/UCS-2) — a single emoji, one non-GSM character, or a language-specific character (Hindi, Tamil, Kannada, etc.) forces the entire message into Unicode encoding, which **halves the per-segment character limit** from 160 to 70.

2. **Message length relative to encoding limits** — Short messages fit in a single segment. Messages exceeding per-encoding limits are split into **concatenated SMS (multi-part)**, each of which carries a 6–7 byte header, reducing the usable payload characters per segment (153 for GSM-7 multi-part, 67 for Unicode multi-part).

3. **DLT template variable expansion** — TRAI DLT-registered templates contain variable placeholders (e.g., `{#var#}`). Before segment calculation, variable values must be substituted, and the expanded message length re-evaluated. If variable expansion pushes a single-segment message into multi-segment territory, the billing unit changes — and the carrier charges increase accordingly.

4. **Special characters in GSM-7 Extended table** — Characters like `€`, `[`, `]`, `{`, `}`, `\`, `^`, `~`, `|` each consume **2 characters** in GSM-7 encoding (they occupy an escape sequence position), meaning a message with these characters has an effective length greater than its visible character count.

5. **Route-specific segment limits** — Some international SMS routes apply different segment maximum policies. Some operators cap concatenated SMS at 3 segments. Exceeding cap means message truncation — a billing and compliance event.

**The Business Consequence of Getting This Wrong:**

| Error | Impact |
|---|---|
| Under-counting segments | Platform charged more than billed to tenant → revenue leak |
| Over-counting segments | Tenant overcharged → refund risk + trust damage |
| Missing Unicode detection | OTP appears delivered but is garbled for recipient → security failure |
| Wrong DLT template expansion | Carrier rejects message or applies wrong rate → reconciliation dispute |
| Ignoring multi-part header overhead | Segment count off by 1 across millions of messages → ₹lakhs monthly error |
| No audit trail for segment calculation | Cannot dispute carrier overcharge → financial exposure |

**At 500 million messages/month, a 1% segment miscalculation error = 5 million wrong billing units.**

This agent eliminates that exposure entirely by computing the exact, deterministic, auditable segment count for **every message before it leaves the platform**.

### 2.2 What This Agent Consumes

**Primary Input (synchronous, pre-send):**
- Raw message content string (UTF-8 encoded payload from Notification Service or Jasmin pre-dispatch hook)
- DLT template ID (required for TRANSACTIONAL, OTP, SERVICE categories)
- DLT template variable map (key-value pairs for variable substitution)
- SMS category (OTP / TRANSACTIONAL / PROMOTIONAL / SERVICE / INTERNATIONAL)
- Route code (carrier + route identifier from Jasmin routing config)
- Tenant ID and User ID (for audit trail and billing attribution)
- Sender ID / Header (DLT-registered header for compliance validation)

**Reference Data (loaded at agent startup, versioned):**
- GSM-7 character table (standard + extended) — versioned
- DLT template registry (from TRAI DLT platform sync — tenant-specific)
- Route-specific segment cap registry (per carrier, per route)
- Unicode block classification table (for language detection)
- Special character escape table (GSM-7 extended — double-byte characters)

### 2.3 What This Agent Produces

- Exact segment count (integer, deterministic)
- Encoding classification (GSM-7 / GSM-7-EXTENDED / UNICODE-UCS2 / UNICODE-UTF16)
- Expanded message content (post-variable-substitution, pre-encoding)
- Effective character count (accounting for escape sequences)
- Billing unit count (segments × billing multiplier per route)
- DLT template compliance verdict
- Per-character encoding audit (which character forced Unicode, if applicable)
- Segment boundary map (start/end character index of each segment)
- Route segment cap compliance flag
- Immutable audit record (UUID-tagged, append-only)

### 2.4 Downstream Agents That Depend On This Agent

| Downstream Agent | Dependency |
|---|---|
| `CALL_COST_CALCULATION_AGENT` | Receives billing_unit_count as the authoritative SMS cost input |
| `TELECOM_USAGE_RECONCILIATION_AGENT` | Uses segment_count for carrier invoice dispute (segment overcharge detection) |
| `Notification Service (Jasmin Dispatch)` | Receives segment_count to decide on send/abort based on route caps |
| `DLT_COMPLIANCE_AGENT` | Receives template_expansion_result for TRAI audit logging |
| `BILLING_GOVERNANCE_AGENT` | Receives billing_unit_count for invoice line item generation |
| `AUDIT_COMPLIANCE_AGENT` | Receives immutable segment audit records |
| `OBSERVABILITY_AGENT` | Receives encoding distribution metrics and anomaly signals |
| `FRAUD_DETECTION_ENGINE` | Receives anomaly flags (e.g. sudden Unicode uplift = potential content injection) |
| `GEO_COMPLIANCE_AGENT` | Receives international route segment cap violations |

### 2.5 Upstream Agents/Services That Feed This Agent

| Upstream Source | Data Provided |
|---|---|
| `Notification Service` | Raw message payload, template ID, variable map, category, route |
| `Jasmin SMS Gateway` | Pre-dispatch hook — route code, sender ID, delivery priority |
| `DLT Template Registry Service` | Registered template content, variable positions, allowed variable lengths |
| `Auth Service` | Tenant and user identity validation |
| `CALL_COST_CALCULATION_AGENT` | Billing period context (for rate registry alignment) |
| `GEO_COMPLIANCE_AGENT` | Route-specific compliance constraints (international routes) |

---

## 3️⃣ THE COMPLETE SMS ENCODING & SEGMENT MATHEMATICS REFERENCE

> This section defines the authoritative encoding and segment computation rules
> that this agent implements. These rules are non-negotiable and immutable at v1.0.0.
> All computation is based on GSM 03.38 specification and 3GPP TS 23.040.

### 3.1 GSM-7 Standard Character Table (Full Set — 128 Characters)

The following characters are the complete GSM-7 Basic Character Set.
**Any message whose every character appears in this table uses GSM-7 encoding.**

```
PRINTABLE ASCII SUBSET INCLUDED IN GSM-7:
  A–Z (uppercase): included
  a–z (lowercase): included
  0–9 (digits)   : included
  Space (0x20)   : included

SPECIAL CHARACTERS INCLUDED IN GSM-7 BASIC:
  @ £ $ ¥ è é ù ì ò Ç Ø ø Å å
  Δ _ Φ Γ Λ Ω Π Ψ Σ Θ Ξ
  Æ æ ß É
  ! " # % & ' ( ) * + , - . / :
  ; < = > ? ¡ Ä Ö Ñ Ü § ¿ ä ö ñ ü à
  LF (line feed) CR (carriage return)

NOTE:
  The @ symbol maps to GSM-7 code point 0x00 (not ASCII 0x40)
  The $ symbol is included (code point 0x02)
  The space is included (code point 0x11)
```

### 3.2 GSM-7 Extended Character Table (Double-Byte — 10 Characters)

**CRITICAL BILLING RULE:** Each character from this extended table consumes **TWO characters**
of the per-segment limit because it is encoded as ESC + character code.

```
EXTENDED_TABLE_CHARACTERS (each costs 2 GSM-7 character positions):
  €  (Euro sign)       — ESC + 0x65
  [  (Left bracket)    — ESC + 0x3C
  ]  (Right bracket)   — ESC + 0x3E
  {  (Left brace)      — ESC + 0x28
  }  (Right brace)     — ESC + 0x29
  \  (Backslash)       — ESC + 0x2F
  ^  (Caret)           — ESC + 0x14
  ~  (Tilde)           — ESC + 0x3D
  |  (Pipe)            — ESC + 0x40
  [  (also: line feed) — ESC + 0x0A (depends on context)

FORMULA IMPACT:
  effective_length += 1 for each extended character (adds 1 to base visible length)
  Example: "Use {OTP} to verify" has { and } → effective_length = visible_length + 2
```

### 3.3 Unicode Trigger Characters

**Any single character NOT in the GSM-7 Basic or Extended table forces the entire message
into Unicode (UCS-2) encoding. The entire message — not just the triggering character.**

```
COMMON UNICODE TRIGGERS ON ECOSKILLER PLATFORM:

  Indian Scripts (force Unicode):
    Devanagari (Hindi, Marathi)  : U+0900–U+097F
    Tamil                        : U+0B80–U+0BFF
    Telugu                       : U+0C00–U+0C7F
    Kannada                      : U+0C80–U+0CFF
    Malayalam                    : U+0D00–U+0D7F
    Bengali                      : U+0980–U+09FF
    Gujarati                     : U+0A80–U+0AFF
    Punjabi (Gurmukhi)           : U+0A00–U+0A7F
    Odia                         : U+0B00–U+0B7F

  Symbols and Emoji (force Unicode):
    Emoji block                  : U+1F300–U+1FFFF
    Dingbats                     : U+2700–U+27BF
    Currency symbols NOT in GSM-7: U+20A0–U+20CF (except € which is GSM-7 Extended)
    Mathematical operators       : U+2200–U+22FF
    Arrows                       : U+2190–U+21FF
    Checkmarks (✓ ✗ ✔ ✘)        : Force Unicode

  Smart/Curly Quotes (COMMON MISTAKE — force Unicode):
    " (U+201C) left double quote     — NOT the same as " (ASCII 0x22 — GSM-7)
    " (U+201D) right double quote    — NOT the same as " (ASCII 0x22 — GSM-7)
    ' (U+2018) left single quote     — NOT the same as ' (ASCII 0x27 — GSM-7)
    ' (U+2019) right single quote    — NOT the same as ' (ASCII 0x27 — GSM-7)
    — (U+2014) em dash               — NOT the same as - (ASCII 0x2D — GSM-7)
    – (U+2013) en dash               — NOT the same as - (ASCII 0x2D — GSM-7)

  CRITICAL PLATFORM NOTE:
    Copy-pasted text from web browsers, word processors, and rich-text editors
    routinely introduces smart quotes and em dashes into SMS templates.
    DLT template registration must validate that templates use ASCII quotes/dashes only.
    This agent detects these characters and classifies encoding accordingly.
```

### 3.4 Definitive Segment Length Table

```
╔══════════════════════════════════════════════════════════════════════╗
║              SEGMENT LENGTH REFERENCE TABLE                         ║
╠═══════════════════════╦═══════════════════╦══════════════════════════╣
║ Scenario              ║ GSM-7 (chars)     ║ Unicode/UCS-2 (chars)   ║
╠═══════════════════════╬═══════════════════╬══════════════════════════╣
║ Single segment        ║ 160               ║ 70                       ║
╠═══════════════════════╬═══════════════════╬══════════════════════════╣
║ Multi-part segment 1  ║ 153               ║ 67                       ║
║ Multi-part segment 2+ ║ 153               ║ 67                       ║
╠═══════════════════════╬═══════════════════╬══════════════════════════╣
║ WHY REDUCTION?        ║ 7-byte UDH header ║ 6-byte UDH header        ║
║ (multi-part only)     ║ consumes 7 chars  ║ consumes 3 UCS-2 chars   ║
║                       ║ of payload space  ║ (6 bytes / 2 = 3 chars) ║
╚═══════════════════════╩═══════════════════╩══════════════════════════╝

UDH = User Data Header (identifies concatenated SMS parts to receiving handset)
UDH is mandatory for multi-part SMS — carriers require it for proper reassembly.
```

### 3.5 Segment Count Computation Formulas

```
STEP 1: COMPUTE EFFECTIVE LENGTH
─────────────────────────────────
  base_length    = len(message_content)   // character count, not byte count
  extended_count = count of chars in EXTENDED_TABLE within message_content
  effective_length = base_length + extended_count
  // Each extended char costs 2 positions, so adds 1 to base

STEP 2: DETERMINE ENCODING
────────────────────────────
  FOR each character in message_content:
    IF character NOT IN GSM7_BASIC_TABLE AND character NOT IN GSM7_EXTENDED_TABLE:
      encoding = UNICODE_UCS2
      BREAK
  IF no break:
    encoding = GSM7
    IF extended_count > 0:
      encoding = GSM7_EXTENDED  // Sub-classification for audit purposes

STEP 3: COMPUTE SEGMENT COUNT
───────────────────────────────
  IF encoding == GSM7 OR encoding == GSM7_EXTENDED:
    single_limit = 160
    multi_limit  = 153

  IF encoding == UNICODE_UCS2:
    single_limit = 70
    multi_limit  = 67

  IF effective_length <= single_limit:
    segment_count = 1

  IF effective_length > single_limit:
    segment_count = CEIL(effective_length / multi_limit)

  // CEILING function: always round UP — partial segments are full billing units

STEP 4: COMPUTE BILLING UNITS
───────────────────────────────
  billing_unit_count = segment_count × route_billing_multiplier[route_code]
  // Most domestic routes: multiplier = 1 (1 segment = 1 billing unit)
  // Some international routes: multiplier > 1 (per carrier contract)

COMPLETE FORMULA (condensed):
──────────────────────────────
  effective_length = len(msg) + count_extended(msg)
  encoding         = GSM7 if all_gsm7(msg) else UNICODE_UCS2
  single_limit     = 160 if encoding == GSM7 else 70
  multi_limit      = 153 if encoding == GSM7 else 67
  segment_count    = 1 if effective_length <= single_limit
                     else CEIL(effective_length / multi_limit)
  billing_units    = segment_count × route_billing_multiplier
```

### 3.6 Worked Examples — All Edge Cases

```
EXAMPLE 1: Standard OTP — Single Segment GSM-7
  Message : "Your OTP is 123456. Valid for 10 minutes. - Ecoskiller"
  Length  : 53 characters
  Extended: 0
  Effective: 53
  Encoding: GSM-7 (all characters in basic table)
  Segments: 1 (53 <= 160)
  Billing : 1 unit
  ─────────────────────────────────────────────────────

EXAMPLE 2: OTP with Euro symbol — Extended GSM-7
  Message : "Pay €500 now. OTP: 445566 - Ecoskiller"
  Length  : 38 characters (visible)
  Extended: 1 (€ is extended)
  Effective: 39
  Encoding: GSM-7-EXTENDED
  Segments: 1 (39 <= 160)
  Billing : 1 unit
  NOTE    : € costs 2 character positions → effective 39, not 38
  ─────────────────────────────────────────────────────

EXAMPLE 3: Message with smart quote — UNICODE FORCED
  Message : "You're invited to join Ecoskiller GD session"
             (contains ' U+2019 right single quote)
  Encoding: UNICODE_UCS2 (smart quote not in GSM-7)
  Length  : 44 characters
  Effective: 44
  Segments: 1 (44 <= 70)
  Billing : 1 unit
  AUDIT   : unicode_trigger_char = "'" (U+2019) at position 3
  NOTE    : Same message with ASCII apostrophe ' would be GSM-7, 1 segment
  ─────────────────────────────────────────────────────

EXAMPLE 4: Long promotional message — GSM-7 multi-segment
  Message : [Standard GSM-7 characters, 320 characters long]
  Encoding: GSM-7
  Single  : 160 → does NOT fit (320 > 160)
  Multi   : 153 per segment
  Segments: CEIL(320 / 153) = CEIL(2.09) = 3
  Billing : 3 units
  ─────────────────────────────────────────────────────

EXAMPLE 5: Hindi language OTP — Unicode multi-segment
  Message : "आपका OTP है: 998877. 10 मिनट में समाप्त।" (Devanagari + ASCII mix)
  Encoding: UNICODE_UCS2 (Devanagari forces Unicode)
  Length  : 41 characters
  Segments: 1 (41 <= 70)
  Billing : 1 unit
  AUDIT   : unicode_trigger_char = "आ" (U+0906) at position 0
  ─────────────────────────────────────────────────────

EXAMPLE 6: Long Hindi message — Unicode multi-segment
  Message : [Hindi Devanagari message, 140 characters]
  Encoding: UNICODE_UCS2
  Single  : 70 → does NOT fit (140 > 70)
  Multi   : 67 per segment
  Segments: CEIL(140 / 67) = CEIL(2.09) = 3
  Billing : 3 units
  NOTE    : Same message in English (ASCII) would be 1 GSM-7 segment (140 <= 160)
            Hindi version = 3 segments = 3× billing cost
  ─────────────────────────────────────────────────────

EXAMPLE 7: Emoji in GD notification — Unicode single segment
  Message : "🎯 Your GD session starts in 15 mins! Join now."
  Encoding: UNICODE_UCS2 (🎯 emoji forces Unicode)
  Length  : 48 characters (emoji counts as 2 UTF-16 code units = 2 chars for UCS-2)
  Effective: 48 (or 49 if emoji is non-BMP → surrogate pair)
  Segments: 1 (48 <= 70)
  Billing : 1 unit
  AUDIT   : unicode_trigger_char = "🎯" (U+1F3AF) at position 0
  NOTE    : Platform policy decision — emoji usage in SMS must be gated
  ─────────────────────────────────────────────────────

EXAMPLE 8: Curly brace OTP template — Extended GSM-7 impact
  Message : "OTP: {123456} — Use within 10 mins. Ecoskiller"
  Extended: { } — and — (em dash? or ASCII hyphen?)
  CASE A — ASCII hyphen - (0x2D): { and } are extended (2 chars each)
    effective_length = 47 + 2 = 49, encoding = GSM7_EXTENDED
    Segments: 1
  CASE B — em dash — (U+2014): UNICODE forced
    effective_length = 47, encoding = UNICODE_UCS2
    Segments: 1
  RULE: This agent resolves the em dash question at character level —
        never assumes ASCII, always inspects codepoint
  ─────────────────────────────────────────────────────

EXAMPLE 9: Route segment cap violation
  Message : [GSM-7, 500 characters → CEIL(500/153) = 4 segments]
  Route   : INTERNATIONAL_ROUTE_X (max_segments = 3 per contract)
  Result  : segment_count = 4, route_cap = 3
  VERDICT : SEGMENT_CAP_VIOLATION → HALT dispatch → return error to Notification Service
  ACTION  : Notification Service must truncate or split message before resubmit
  ─────────────────────────────────────────────────────

EXAMPLE 10: DLT variable expansion changes segment class
  Template: "Dear {#var#}, your OTP is {#var#}. Valid 10 mins. - Ecoskiller"
  Variables: name = "Priyanka Subramaniam", otp = "887654"
  Expanded: "Dear Priyanka Subramaniam, your OTP is 887654. Valid 10 mins. - Ecoskiller"
  Length  : 75 characters
  Extended: 0
  Encoding: GSM-7
  Segments: 1 (75 <= 160) ✓

  BUT IF variable uses Hindi name:
  Variables: name = "प्रियंका", otp = "887654"
  Expanded: "Dear प्रियंका, your OTP is 887654. Valid 10 mins. - Ecoskiller"
  Encoding: UNICODE_UCS2 (Devanagari in name variable)
  Length  : 63 characters
  Segments: 1 (63 <= 70) — still single segment, but encoding changed
  BILLING : Same unit count, but agent must flag ENCODING_CHANGED_BY_VARIABLE
            for audit and DLT compliance review
```

---

## 4️⃣ DLT TEMPLATE VARIABLE EXPANSION ENGINE

### 4.1 DLT Template System (TRAI India Mandate)

```yaml
DLT_MANDATE:
  Authority      : TRAI (Telecom Regulatory Authority of India)
  Regulation     : TCCCPR 2018 + DLT amendments
  Requirement    : All commercial SMS must use pre-registered templates
  Platform_Role  : Ecoskiller is registered as Principal Entity (PE) on DLT platform
  Template_Format: Fixed content + declared variable positions ({#var#} placeholders)
  Variable_Rules :
    - Variables declared at template registration time
    - Variable position (character index) declared at registration
    - Variable max_length declared at registration — HARD LIMIT
    - Variable content type declared: NUMERIC / ALPHANUMERIC / ALPHA
    - Variable may contain: letters, digits, spaces, limited punctuation
    - Variable may NOT contain: DLT header, new links not pre-registered,
                                sensitive PII beyond declared scope
```

### 4.2 Template Variable Expansion — Strict Rules

```
EXPANSION_ALGORITHM:

Step 1 — Fetch Registered Template
  template = DLT_TEMPLATE_REGISTRY.get(dlt_template_id, tenant_id)
  IF template NOT FOUND:
    RAISE DLT_TEMPLATE_NOT_FOUND
    HALT dispatch
    LOG + ESCALATE to DLT_COMPLIANCE_AGENT

Step 2 — Validate Variable Map Against Template Declaration
  FOR each declared_variable in template.variables:
    IF declared_variable.name NOT IN input_variable_map:
      RAISE REQUIRED_VARIABLE_MISSING
      HALT dispatch
    supplied_value = input_variable_map[declared_variable.name]
    IF len(supplied_value) > declared_variable.max_length:
      RAISE VARIABLE_LENGTH_EXCEEDED (max: {declared_variable.max_length}, got: {len(supplied_value)})
      HALT dispatch
    IF declared_variable.content_type == NUMERIC:
      IF NOT supplied_value.matches(r'^[0-9]+$'):
        RAISE VARIABLE_TYPE_VIOLATION
        HALT dispatch
    IF declared_variable.content_type == ALPHANUMERIC:
      IF NOT supplied_value.matches(r'^[A-Za-z0-9 \-\.]+$'):
        RAISE VARIABLE_TYPE_VIOLATION
        HALT dispatch

Step 3 — Substitute Variables
  expanded_content = template.content
  FOR each variable in template.variables (in declaration order):
    placeholder = "{#var#}" (or named placeholder if template uses named form)
    expanded_content = expanded_content.replace_first(placeholder, input_variable_map[variable.name])

Step 4 — Content Length Validation Post-Expansion
  IF len(expanded_content) > template.registered_max_length + EXPANSION_TOLERANCE (10 chars):
    RAISE EXPANDED_LENGTH_EXCEEDS_REGISTERED_MAXIMUM
    HALT dispatch
    LOG: expanded_length, registered_max_length, variable_values (hashed)

Step 5 — Proceed to Segment Calculation with expanded_content
  Run full segment calculation algorithm (Section 3.5) on expanded_content
  Store both:
    - pre_expansion_segment_estimate (from template placeholder lengths)
    - post_expansion_actual_segment_count (authoritative billing count)
  IF pre_expansion_segment_estimate != post_expansion_actual_segment_count:
    FLAG SEGMENT_COUNT_CHANGED_BY_EXPANSION
    Emit advisory to OBSERVABILITY_AGENT (not a halt — just monitoring)

Step 6 — DLT Compliance Hash
  expansion_hash = SHA-256(dlt_template_id + expanded_content + timestamp_utc)
  Store in audit record for TRAI audit readiness
```

### 4.3 Pre-Registration Template Segment Estimation

```yaml
PURPOSE:
  Before an SMS campaign is dispatched, the Notification Service can query this agent
  to estimate the segment range for a given template across possible variable lengths.

ESTIMATION_REQUEST:
  {
    dlt_template_id       : UUID,
    tenant_id             : string,
    estimation_mode       : ENUM [MIN_VARIABLES, MAX_VARIABLES, AVERAGE_VARIABLES],
    variable_length_map   : { variable_name: estimated_length } (optional)
  }

ESTIMATION_RESPONSE:
  {
    template_id           : UUID,
    min_segment_count     : integer  // all variables at minimum length
    max_segment_count     : integer  // all variables at maximum declared length
    expected_segment_count: integer  // with estimation_mode applied
    encoding_risk         : ENUM [GSM7_SAFE, UNICODE_POSSIBLE, UNICODE_CERTAIN]
    unicode_risk_factors  : [list of variables/content positions that could trigger Unicode]
    billing_range_per_msg : { min_inr: decimal, max_inr: decimal }
    advice                : string  // e.g. "Variable {name} may contain Unicode characters"
  }

NOTE: Estimation is advisory only — actual segment count computed on live expanded content.
```

---

## 5️⃣ INPUT CONTRACT (STRICT)

### 5.1 Segment Calculation Request Schema

```json
SEGMENT_CALCULATION_REQUEST_SCHEMA: {
  "required_fields": [
    "request_id",
    "tenant_id",
    "user_id",
    "session_id",
    "message_content",
    "sms_category",
    "route_code",
    "sender_id",
    "request_timestamp_utc"
  ],
  "conditional_required_fields": [
    "dlt_template_id      : required if sms_category IN [OTP, TRANSACTIONAL, SERVICE]",
    "variable_map         : required if dlt_template_id is present",
    "recipient_count      : required if dispatch_mode = BROADCAST (>1 recipient)"
  ],
  "optional_fields": [
    "preferred_encoding_hint",
    "unicode_normalization_mode",
    "campaign_id",
    "royalty_session_flag",
    "priority_level",
    "language_hint"
  ],
  "field_definitions": {
    "request_id"             : "UUID v4 — dedup key for idempotency",
    "tenant_id"              : "string — must resolve in Auth Service",
    "user_id"                : "string — recipient or triggering user",
    "session_id"             : "string — Notification Service session reference",
    "message_content"        : "string — raw UTF-8 encoded message, max 5000 chars input (will be validated)",
    "sms_category"           : "ENUM: [OTP, TRANSACTIONAL, PROMOTIONAL, SERVICE, INTERNATIONAL] — reject unknown",
    "route_code"             : "string — must match registered route in carrier registry",
    "sender_id"              : "string — DLT-registered 6-char header, must match tenant DLT registration",
    "dlt_template_id"        : "UUID — must exist in DLT_TEMPLATE_REGISTRY for tenant",
    "variable_map"           : "object — { variable_name: variable_value }, all values must be strings",
    "preferred_encoding_hint": "ENUM: [GSM7, UNICODE, AUTO] — AUTO is default, agent always validates",
    "unicode_normalization_mode": "ENUM: [NFC, NFD, NFKC, NFKD, NONE] — applied before encoding check if specified"
  },
  "validation_rules": [
    "request_id must be UUID v4 — reject malformed",
    "tenant_id must resolve against Auth Service — reject unknown tenants",
    "message_content must be non-empty string — reject null or empty",
    "message_content max raw input length: 5000 characters (safety gate before processing)",
    "sms_category must match declared ENUM exactly — reject unknown categories",
    "route_code must match registered carrier route — reject unknown routes",
    "sender_id must match tenant's DLT-registered headers — reject unregistered headers",
    "IF sms_category == OTP: dlt_template_id required (TRAI mandate)",
    "IF sms_category == TRANSACTIONAL: dlt_template_id required (TRAI mandate)",
    "IF dlt_template_id present: variable_map required if template has declared variables",
    "variable_map keys must match template declared variable names exactly",
    "All variable values must be strings — no nested objects, no arrays",
    "preferred_encoding_hint = GSM7 does NOT override Unicode detection — advisory only",
    "request_timestamp_utc must be within 60 seconds of server time — replay protection"
  ],
  "security_checks": [
    "Validate emitting service JWT — reject unsigned or expired tokens",
    "Validate tenant_id isolation — cross-tenant injection triggers SECURITY_HALT",
    "Rate limit: max 100,000 segment calculation requests per minute per tenant",
    "request_id dedup window: 60 seconds — duplicate request_ids rejected (idempotency)",
    "message_content must not contain control characters (0x00–0x08, 0x0B, 0x0C, 0x0E–0x1F) except LF/CR",
    "variable_map values must not contain SQL injection patterns — sanitize before DLT expansion",
    "Log ALL requests regardless of outcome — no silent processing"
  ],
  "domain_checks": [
    "OTP messages: message_content or template must contain numeric variable for OTP code",
    "PROMOTIONAL messages: must NOT contain URLs unless pre-registered in DLT template",
    "INTERNATIONAL messages: route_code must map to approved international gateway",
    "All categories: sender_id must match sms_category allowed header type per DLT rules"
  ]
}
```

### 5.2 Null and Default Tolerance Policy

```yaml
NULL_TOLERANCE_POLICY:
  required_fields                : ZERO TOLERANCE — reject immediately on null
  conditional_required_fields    : Reject if condition is met and field is null
  optional_fields                :
    preferred_encoding_hint      : null → default AUTO
    unicode_normalization_mode   : null → default NONE
    language_hint                : null → no language-specific processing applied
    campaign_id                  : null → accepted, not required for segment calculation
    royalty_session_flag         : null → default false
    priority_level               : null → default NORMAL
  DEFAULT_SUBSTITUTION_LOG       : All defaults logged in audit record
```

---

## 6️⃣ OUTPUT CONTRACT (STRICT)

### 6.1 Segment Calculation Result Schema

```json
SEGMENT_CALCULATION_RESULT_SCHEMA: {
  "segment_record": {
    "segment_record_id"           : "UUID v4 — immutable",
    "request_id"                  : "UUID — echoed from input",
    "tenant_id"                   : "string",
    "user_id"                     : "string",
    "session_id"                  : "string",
    "sms_category"                : "ENUM",
    "route_code"                  : "string",
    "sender_id"                   : "string",
    "dlt_template_id"             : "UUID or null",

    "raw_input_length"            : "integer — character count before processing",
    "effective_character_length"  : "integer — after extended char expansion",
    "encoding_classification"     : "ENUM: [GSM7, GSM7_EXTENDED, UNICODE_UCS2]",
    "unicode_trigger_character"   : "string or null — first non-GSM7 character found",
    "unicode_trigger_position"    : "integer or null — zero-based index in expanded content",
    "unicode_trigger_codepoint"   : "string or null — U+XXXX format",
    "extended_char_count"         : "integer — count of GSM-7 extended table chars",
    "extended_char_positions"     : "[array of zero-based indices]",

    "segment_count"               : "integer — authoritative, deterministic, exact",
    "single_segment_limit"        : "integer — 160 (GSM7) or 70 (Unicode)",
    "multi_segment_limit"         : "integer — 153 (GSM7) or 67 (Unicode)",
    "segment_boundary_map"        : [
      {
        "segment_number"          : "integer (1-based)",
        "start_char_index"        : "integer",
        "end_char_index"          : "integer",
        "char_count"              : "integer"
      }
    ],

    "billing_unit_count"          : "integer — segment_count × route_billing_multiplier",
    "route_billing_multiplier"    : "decimal — from route registry",
    "route_segment_cap"           : "integer — maximum allowed segments for route",
    "route_segment_cap_status"    : "ENUM: [WITHIN_CAP, AT_CAP, EXCEEDED_CAP]",

    "dlt_template_compliance"     : "ENUM: [COMPLIANT, NOT_APPLICABLE, VIOLATION_DETECTED, PENDING_REVIEW]",
    "dlt_expansion_hash"          : "SHA-256 or null",
    "segment_count_changed_by_expansion": "boolean — true if expansion changed segment count from estimate",
    "encoding_changed_by_variable": "boolean — true if a variable value changed encoding class",

    "dispatch_authorized"         : "boolean — true = proceed, false = halt",
    "halt_reason"                 : "ENUM or null: [SEGMENT_CAP_VIOLATION, DLT_TEMPLATE_VIOLATION, ENCODING_UNDETERMINED, VALIDATION_FAILURE, ROUTE_NOT_FOUND]",

    "computed_at_utc"             : "ISO8601",
    "model_version"               : "SEGMENT_MODEL_v1.0.0",
    "audit_reference"             : "UUID v4"
  },
  "confidence_score"              : "1.0 — always (deterministic computation, no estimation)",
  "next_trigger_events"           : [
    "BILLING_UNIT_COUNT_EMITTED → CALL_COST_CALCULATION_AGENT",
    "DISPATCH_AUTHORIZED_SIGNAL → Jasmin SMS Gateway (conditional)",
    "DISPATCH_HALTED_SIGNAL → Notification Service (conditional)",
    "DLT_COMPLIANCE_RECORD_APPENDED → DLT_COMPLIANCE_AGENT (conditional)",
    "SEGMENT_CAP_VIOLATION_EVENT → GEO_COMPLIANCE_AGENT (conditional)",
    "UNICODE_UPLIFT_ANOMALY → FRAUD_DETECTION_ENGINE (conditional)",
    "AUDIT_RECORD_APPENDED → AUDIT_COMPLIANCE_AGENT"
  ]
}
```

### 6.2 Output Guarantees

```yaml
GUARANTEES:
  confidence_score            : Always 1.0 — this computation is deterministic, exact, not probabilistic
  segment_count               : Always integer > 0 — never null, never estimated
  audit_reference             : Always present — every result has immutable UUID audit linkage
  billing_unit_count          : Always integer > 0 — always linked to route_billing_multiplier version
  dispatch_authorized         : Always boolean — never null, never "unknown"
  model_version               : Always present — all outputs traceable to producing model version
  segment_boundary_map        : Always populated — every segment has declared start/end indices
  decimal_precision           : Monetary values: decimal(12,6) — no floating point
  cross_tenant_isolation      : Architecturally impossible — tenant_id in every query predicate
  HALT_OUTPUT_RULE            : On any halt condition — dispatch_authorized = false,
                                halt_reason MUST be populated, segment_record still emitted
                                (billing_unit_count = 0 on halt, never null)
```

---

## 7️⃣ SPECIAL ENCODING HANDLING — PLATFORM-SPECIFIC RULES

### 7.1 Unicode Normalization Pre-Processing

```yaml
NORMALIZATION_RULES:
  POLICY: Unicode normalization is applied BEFORE encoding classification
          IF unicode_normalization_mode is specified in request

  NFC (Canonical Decomposition, Canonical Composition — DEFAULT RECOMMENDED):
    Example: "é" can be U+00E9 (precomposed) or U+0065 + U+0301 (decomposed)
    NFC converts decomposed to precomposed form
    IMPACT: Reduces character count for certain composite characters
    BILLING_IMPACT: May reduce effective_length, potentially reducing segments

  NFKC (Compatibility Decomposition + Canonical Composition):
    Example: "ﬁ" (ligature, U+FB01) → "fi" (two ASCII chars)
    NFKC can convert non-GSM7 ligatures/compatibility chars to GSM-7 equivalents
    IMPACT: May convert Unicode trigger characters to GSM-7 → encoding changes
    BILLING_IMPACT: Can convert UNICODE message to GSM-7 → potentially reduces segments
    RISK: Content may look different to recipient — platform policy decision

  NORMALIZATION_AUDIT:
    IF normalization_mode applied:
      pre_normalization_encoding  : stored in audit record
      post_normalization_encoding : stored in audit record
      normalization_changed_encoding: boolean flag
      IF normalization_changed_encoding = true:
        Emit NORMALIZATION_CHANGED_ENCODING advisory to OBSERVABILITY_AGENT
```

### 7.2 Emoji Handling — UCS-2 Surrogate Pairs

```yaml
EMOJI_SURROGATE_PAIR_RULE:
  SMS UCS-2 uses 16-bit encoding (2 bytes per character = 1 UCS-2 "char")
  Basic Multilingual Plane (BMP) characters: U+0000–U+FFFF → 1 UCS-2 char each
  Supplementary plane characters (emoji, rare CJK): U+10000–U+10FFFF → 2 UCS-2 chars each

  SUPPLEMENTARY_PLANE_DETECTION:
    IF codepoint > U+FFFF:
      ucs2_char_count += 2 (not 1)
      This doubles the character cost for that codepoint

  COMMON_SUPPLEMENT_PLANE_EMOJI_ON_PLATFORM:
    🎯 (U+1F3AF) — target/mission — 2 UCS-2 chars
    🏆 (U+1F3C6) — trophy       — 2 UCS-2 chars
    ⭐ (U+2B50)  — star         — 1 UCS-2 char (BMP)
    ✅ (U+2705)  — check        — 1 UCS-2 char (BMP)
    🔥 (U+1F525) — fire         — 2 UCS-2 chars
    📚 (U+1F4DA) — books        — 2 UCS-2 chars

  BILLING_RULE:
    effective_unicode_length = Σ (2 if codepoint > U+FFFF else 1) for each char
    segment_count = 1 if effective_unicode_length <= 70
                    else CEIL(effective_unicode_length / 67)

  PLATFORM_POLICY:
    Emoji in OTP/TRANSACTIONAL messages: DISCOURAGED (increases cost, delivery risk)
    Emoji in PROMOTIONAL messages: PERMITTED with explicit category declaration
    Emoji gate: If emoji detected in OTP category → emit EMOJI_IN_OTP_ADVISORY
                to OBSERVABILITY_AGENT (not a halt — advisory only)
```

### 7.3 Character Sanitization Gate

```yaml
SANITIZATION_RULES:
  POLICY: This agent does NOT silently sanitize content.
          If content requires sanitization to be dispatched, agent HALTS and
          returns to Notification Service with sanitization_required = true
          and character-level diagnostic.

  CONTROL_CHARACTER_DETECTION:
    Characters U+0000–U+0008 (NULL, SOH, STX, ETX, EOT, ENQ, ACK, BEL, BS):
      HALT dispatch — control characters forbidden in SMS
      Return INVALID_CONTROL_CHARACTER with position

    U+000B (VT), U+000C (FF): HALT — invalid in SMS
    U+000E–U+001F (SO through US): HALT — invalid in SMS
    EXCEPTION: U+000A (LF) and U+000D (CR) — PERMITTED in SMS (line breaks)

  ZERO-WIDTH CHARACTER DETECTION:
    U+200B (ZWSP), U+200C (ZWNJ), U+200D (ZWJ), U+FEFF (BOM):
      These are invisible but consume UCS-2 character positions
      HALT dispatch — return ZERO_WIDTH_CHAR_DETECTED with position
      (Could cause invisible billing inflation or content injection)

  UNICODE_REPLACEMENT_CHARACTER:
    U+FFFD (�) detected in content: HALT — indicates encoding corruption upstream
    Return ENCODING_CORRUPTION_DETECTED

  BIDIRECTIONAL_OVERRIDE_DETECTION:
    U+202A–U+202E (LTR/RTL override marks), U+2066–U+2069:
      HALT dispatch — bidi overrides can disguise content
      Emit BIDI_OVERRIDE_DETECTED to FRAUD_DETECTION_ENGINE
```

---

## 8️⃣ PLATFORM SMS CATEGORY SEGMENT POLICIES

```yaml
CATEGORY_SPECIFIC_POLICIES:

  OTP:
    Typical_length    : 50–80 characters
    Expected_segments : 1 (strict recommendation)
    Max_segments      : 1 (HARD LIMIT — OTP longer than single segment = DLT violation risk)
    If_exceeds_1      : HALT dispatch — return OTP_EXCEEDS_SINGLE_SEGMENT
                        Notification Service must shorten OTP message
    Encoding_target   : GSM-7 strongly preferred (Unicode OTP = delivery risk on feature phones)
    Unicode_in_OTP    : Log UNICODE_IN_OTP_ADVISORY — dispatch allowed unless > 70 chars
    Variable_count    : Typically 1 (OTP code) — max 2 declared

  TRANSACTIONAL:
    Typical_length    : 80–160 characters
    Expected_segments : 1–2
    Max_segments      : 3 (platform policy — beyond 3 = truncation risk)
    If_exceeds_3      : HALT dispatch — return TRANSACTIONAL_EXCEEDS_SEGMENT_POLICY
    Encoding_target   : GSM-7 preferred
    Variable_count    : 2–5 typical

  PROMOTIONAL:
    Typical_length    : 100–300 characters
    Expected_segments : 1–3
    Max_segments      : 5 (platform policy)
    If_exceeds_5      : HALT dispatch — return PROMOTIONAL_EXCEEDS_SEGMENT_POLICY
    Encoding_target   : No preference — Unicode permitted
    DLT_template      : Required (TRAI) — PROMOTIONAL header required (6-digit numeric)
    Variable_count    : 0–3 typical (often fixed content)

  SERVICE:
    Typical_length    : 50–120 characters
    Expected_segments : 1
    Max_segments      : 2 (platform policy)
    Encoding_target   : GSM-7 preferred

  INTERNATIONAL:
    Max_segments      : Route-dependent — loaded from carrier registry
    Encoding          : Standard rules apply + route-specific override check
    Currency_symbols  : Non-Euro currency symbols may force Unicode (₹ U+20B9 = Unicode)
    Language          : Destination country language hint used for encoding advisory
    DLT_template      : NOT required (no TRAI mandate for international)
    Carrier_cap       : Enforced from international route registry — hard limit
```

---

## 9️⃣ SCALABILITY DESIGN

```yaml
THROUGHPUT_REQUIREMENTS:
  Peak SMS dispatch rate      : 100,000 messages/second (during GD session wave events)
  Segment calculation latency : P99 < 5ms (synchronous pre-send gate — blocking)
  P95 latency                 : < 2ms
  P50 latency                 : < 1ms
  Batch estimation mode       : Non-blocking — used for campaign pre-flight checks

ARCHITECTURE:
  Execution_model   : Stateless, in-memory computation
                      No database calls in hot path (all reference tables preloaded)
  Reference_data    : GSM-7 tables, DLT templates, route registry loaded in memory
                      at agent startup — refreshed every 5 minutes via background job
  Caching_layer     : Redis — computed segment counts cached by SHA-256(message_content + route)
                      Cache TTL: 5 minutes
                      Cache invalidation: on DLT template update or route registry change
  DLT_template_cache: In-memory LRU cache (10,000 templates max per pod)
                      Background sync from DLT_TEMPLATE_REGISTRY every 60 seconds

HORIZONTAL_SCALING:
  Deployment        : Stateless pods — horizontal unlimited
  Minimum replicas  : 5 (always-on, HA)
  Maximum replicas  : 200 (HPA on request queue depth)
  Pod startup time  : < 10 seconds (reference tables preloaded on init)
  Load balancing    : Round-robin (stateless — all pods identical)

IDEMPOTENCY:
  request_id        : Dedup key — identical request_id within 60s window = return cached result
                      Prevents double-billing if Notification Service retries
  Dedup storage     : Redis (60-second TTL per request_id)
  Guarantee         : Same request_id always returns identical segment_count

KAFKA_INTEGRATION:
  Async audit emit  : Segment records emitted to sms.segment.computed topic asynchronously
                      Does NOT block the synchronous response to Notification Service
  Error topic       : sms.segment.computation.failed (dead-letter)
  Billing topic     : sms.billing.unit.emitted (consumed by CALL_COST_CALCULATION_AGENT)
```

---

## 🔟 SECURITY ENFORCEMENT

```yaml
SECURITY_CONTROLS:

  TENANT_ISOLATION:
    All computations scoped to tenant_id
    DLT template registry queries include tenant_id predicate
    No cross-tenant message content visible in any operation
    Segment audit records: row-level security on tenant_id

  CONTENT_SECURITY:
    Message content: processed in memory only — never persisted to disk
    Audit record: stores content_hash (SHA-256) not raw content
    DLT expansion: variable values stored as hashed form in audit
    PII in variables: agent does not inspect variable semantic content
                      (that is DLT_COMPLIANCE_AGENT's responsibility)

  INJECTION_PROTECTION:
    Variable map values sanitized before DLT template substitution
    SQL injection patterns rejected at input validation
    CRLF injection in message content: detected and halted
    Unicode bidi override injection: detected and halted, escalated to FRAUD_DETECTION_ENGINE

  REQUEST_AUTHENTICATION:
    All requests require valid service JWT (issued by Auth Service)
    Caller identity logged in every audit record
    Rate limiting: 100,000 requests/minute per tenant (hard limit via Redis token bucket)
    Throttled requests: return 429 with retry-after header — not silently dropped

  AUDIT_TRAIL:
    Every computation produces immutable audit record (UUID-tagged)
    Audit stored in PostgreSQL (primary) + ClickHouse (analytics)
    Content NOT stored — only content_hash, lengths, encoding result
    Audit retention: 7 years (telecom + billing regulation)
    Wazuh SIEM: monitors for anomalous encoding patterns (sudden Unicode uplift across tenant)

  ROLE_BASED_ACCESS:
    NOTIFICATION_SERVICE   : Call segment calculation (write/execute)
    JASMIN_GATEWAY         : Read dispatch_authorized signal
    PLATFORM_ADMIN         : Read audit records, read segment analytics
    BILLING_GOVERNANCE     : Read billing_unit_count stream
    TENANT_ADMIN           : Read own tenant aggregate segment statistics ONLY
    CANDIDATES/USERS       : NO access to segment calculation layer
```

---

## 1️⃣1️⃣ AUDIT & TRACEABILITY

Every segment computation emits the following immutable audit record:

```json
SEGMENT_AUDIT_LOG: {
  "audit_id"                       : "UUID v4",
  "timestamp_utc"                  : "ISO8601",
  "actor_id"                       : "service identity (Notification Service instance)",
  "tenant_id"                      : "string",
  "user_id"                        : "string",
  "request_id"                     : "UUID (echoed from input)",
  "segment_record_id"              : "UUID",
  "sms_category"                   : "ENUM",
  "route_code"                     : "string",
  "sender_id"                      : "string",
  "dlt_template_id"                : "UUID or null",
  "content_hash"                   : "SHA-256 of expanded message content",
  "raw_input_length"               : "integer",
  "effective_character_length"     : "integer",
  "encoding_classification"        : "ENUM",
  "unicode_trigger_codepoint"      : "string or null",
  "unicode_trigger_position"       : "integer or null",
  "extended_char_count"            : "integer",
  "segment_count"                  : "integer",
  "billing_unit_count"             : "integer",
  "route_segment_cap_status"       : "ENUM",
  "dlt_template_compliance"        : "ENUM",
  "dispatch_authorized"            : "boolean",
  "halt_reason"                    : "ENUM or null",
  "model_version"                  : "SEGMENT_MODEL_v1.0.0",
  "confidence_score"               : "1.0",
  "normalization_applied"          : "string or null",
  "normalization_changed_encoding" : "boolean",
  "segment_count_changed_by_expansion": "boolean",
  "encoding_changed_by_variable"   : "boolean",
  "immutable"                      : true
}
```

---

## 1️⃣2️⃣ FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  VALIDATION_FAILURE (malformed input):
    ACTION         : REJECT request, dispatch_authorized = false
    LOG            : Append rejection with reason + field_name + content_hash
    EMIT           : VALIDATION_FAILURE_EVENT → Notification Service + OBSERVABILITY_AGENT
    RETRY          : Emitting service must fix and resubmit — no auto-retry from this agent
    BILLING_IMPACT : billing_unit_count = 0 — not billed

  DLT_TEMPLATE_NOT_FOUND:
    ACTION         : HALT dispatch, dispatch_authorized = false
    LOG            : LOG_INCIDENT with dlt_template_id + tenant_id
    ESCALATE_TO    : DLT_COMPLIANCE_AGENT + NOTIFICATION_SERVICE
    RETRY          : After DLT template is registered and synced
    REGULATORY     : If OTP/TRANSACTIONAL category — emit to GEO_COMPLIANCE_AGENT immediately

  VARIABLE_VALIDATION_FAILURE (length exceeded, wrong type):
    ACTION         : HALT dispatch, dispatch_authorized = false
    LOG            : Log with variable_name, declared_constraint, actual_value_length
    RETURN         : Specific error to Notification Service (which variable failed)
    RETRY          : Notification Service must correct variable values

  ENCODING_UNDETERMINED (corrupt input, mixed byte streams):
    ACTION         : HALT dispatch, dispatch_authorized = false
    LOG            : LOG_INCIDENT with ENCODING_UNDETERMINED flag
    ESCALATE_TO    : OBSERVABILITY_AGENT
    RETRY          : After Notification Service re-encodes content to valid UTF-8

  ROUTE_NOT_FOUND:
    ACTION         : HALT dispatch, dispatch_authorized = false
    LOG            : LOG_INCIDENT with route_code + tenant_id
    ESCALATE_TO    : ADMIN_GOVERNANCE_SERVICE
    RETRY          : After route registered in carrier registry

  SEGMENT_CAP_VIOLATION:
    ACTION         : HALT dispatch, dispatch_authorized = false
    RETURN         : segment_count, route_cap, SEGMENT_CAP_VIOLATION to Notification Service
    NOTIFICATION_SERVICE_EXPECTED_ACTION: Truncate message or split across separate API calls
    LOG            : Log with actual_segment_count, route_cap, route_code
    INTERNATIONAL  : Emit to GEO_COMPLIANCE_AGENT

  BIDI_OVERRIDE_DETECTED:
    ACTION         : HALT dispatch, dispatch_authorized = false
    ESCALATE_TO    : FRAUD_DETECTION_ENGINE immediately
    LOG            : LOG_INCIDENT with BIDI_OVERRIDE_DETECTED + character positions
    SECURITY_REVIEW: Required before route can be used by that tenant again

  ZERO_WIDTH_CHAR_DETECTED:
    ACTION         : HALT dispatch, dispatch_authorized = false
    LOG            : LOG_INCIDENT with character positions
    RETURN         : ZERO_WIDTH_CHAR_DETECTED with positions to Notification Service
    RETRY          : After Notification Service strips zero-width characters

  REFERENCE_DATA_STALE (GSM-7 tables, DLT templates, route registry not refreshed):
    ACTION         : Continue computation (use last-known-good reference data)
    LOG            : LOG_INCIDENT with staleness_duration
    ESCALATE_TO    : OBSERVABILITY_AGENT if staleness > 15 minutes
    FAILSAFE       : If DLT_TEMPLATE_REGISTRY unreachable for > 5 minutes:
                     HALT all TRANSACTIONAL/OTP dispatch until registry restored
                     (PROMOTIONAL may continue — no live DLT lookup required for fixed templates)

  REDIS_CACHE_UNAVAILABLE:
    ACTION         : Continue computation without cache (no dedup, slightly higher latency)
    LOG            : LOG_INCIDENT — alert OBSERVABILITY_AGENT
    DEDUP          : Disabled during Redis outage (idempotency degraded — accept risk)
    RESTORE        : Resume caching automatically when Redis reconnects

  COMPUTATION_ENGINE_EXCEPTION (unexpected runtime error):
    ACTION         : HALT dispatch, dispatch_authorized = false
    LOG            : Full stack trace + input_hash
    ESCALATE_TO    : PLATFORM_ADMIN + OBSERVABILITY_AGENT
    DEAD_LETTER    : Emit to sms.segment.computation.failed Kafka topic
    RETRY_POLICY   : 3 retries with 100ms exponential backoff, then HALT + dead-letter

GLOBAL_FAILURE_RULES:
  - NO silent failures — every failure emits structured Kafka event
  - NO partial results — dispatch_authorized is always boolean (never null)
  - NO message dispatched without a valid segment_record_id in audit
  - billing_unit_count is 0 on any halt — never charged for unprocessed messages
  - ALL failure events routed to dead-letter Kafka topic
```

---

## 1️⃣3️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_SOURCES:
  - Notification Service        → segment calculation requests (synchronous HTTP)
  - Jasmin SMS Gateway          → pre-dispatch hook (synchronous)
  - DLT Template Registry       → template sync (background, every 60s)
  - Carrier Route Registry      → route cap + billing multiplier (background, every 5min)
  - Auth Service                → JWT validation

DOWNSTREAM_CONSUMERS:
  - CALL_COST_CALCULATION_AGENT → sms.billing.unit.emitted topic
  - TELECOM_USAGE_RECONCILIATION_AGENT → audit records (batch query)
  - Notification Service        → dispatch_authorized signal (synchronous response)
  - Jasmin SMS Gateway          → dispatch gate (dispatch_authorized = true → proceed)
  - DLT_COMPLIANCE_AGENT        → dlt_compliance_record.appended topic
  - AUDIT_COMPLIANCE_AGENT      → audit.record.appended topic
  - OBSERVABILITY_AGENT         → metrics + anomaly advisory signals
  - FRAUD_DETECTION_ENGINE      → bidi override + unicode uplift anomaly events
  - GEO_COMPLIANCE_AGENT        → segment cap violation + international route compliance

KAFKA_TOPICS_PRODUCED:
  - sms.segment.computed           (all successful segment calculations)
  - sms.billing.unit.emitted       (billing unit count per message)
  - sms.segment.dispatch.halted    (all halt events)
  - sms.segment.dlt.compliance     (DLT template expansion records)
  - sms.segment.computation.failed (dead-letter for failed computations)
  - sms.segment.anomaly.detected   (Unicode uplift, bidi override, etc.)

KAFKA_TOPICS_CONSUMED:
  - (none — this agent is synchronous request-response, not Kafka consumer)
  - Async audit emit is fire-and-forget to sms.segment.computed

SYNCHRONOUS_API:
  POST /api/v1/sms/segment/calculate
    Request : SEGMENT_CALCULATION_REQUEST_SCHEMA
    Response: SEGMENT_CALCULATION_RESULT_SCHEMA
    Timeout : 10ms server-side (return 504 if exceeded → Notification Service retries)
    Auth    : Service JWT required

  POST /api/v1/sms/segment/estimate (batch estimation for campaign pre-flight)
    Request : { dlt_template_id, tenant_id, estimation_mode, variable_length_map }
    Response: Estimation response (advisory, non-binding)
    Timeout : 100ms
```

---

## 1️⃣4️⃣ OBSERVABILITY & MONITORING

```yaml
METRICS_EMITTED_TO_PROMETHEUS:
  - sms_segment_calculations_total            (counter, by encoding, category, route)
  - sms_segment_dispatch_halted_total         (counter, by halt_reason)
  - sms_segment_latency_ms                    (histogram P50/P95/P99, by route)
  - sms_encoding_distribution                 (gauge: % GSM7 vs Unicode, by tenant, by category)
  - sms_segment_count_distribution            (histogram: segment counts 1,2,3,4,5+)
  - sms_unicode_uplift_events_total           (counter: messages that forced Unicode)
  - sms_extended_char_events_total            (counter: messages with GSM-7 extended chars)
  - sms_dlt_template_compliance_failures_total (counter, by failure_type)
  - sms_segment_cap_violations_total          (counter, by route)
  - sms_cache_hit_rate                        (gauge: Redis cache effectiveness)
  - sms_reference_data_staleness_seconds      (gauge: DLT template + route registry age)
  - sms_billing_units_emitted_total           (counter: total billing units computed, by category)

GRAFANA_DASHBOARDS:
  - Real-time segment calculation throughput (requests/sec by category)
  - Encoding distribution heatmap (GSM7 vs Unicode by tenant and time)
  - Unicode trigger character frequency (which chars are forcing Unicode)
  - Dispatch halt reasons breakdown (pie chart by halt_reason)
  - Segment count distribution (1 vs 2 vs 3+ segments — billing cost visibility)
  - DLT template compliance scorecard
  - Latency percentiles (P50/P95/P99 — SLA visibility)
  - Cache hit rate trend
  - Segment cap violation log

ALERTING_RULES:
  - P99 latency > 5ms: CRITICAL — PagerDuty (SLA breach)
  - P99 latency > 10ms: SEV-1 escalation
  - Unicode uplift > 30% of messages in 5min: Warning (possible injection or template issue)
  - DLT template compliance failures > 0.1% in 1h: Alert to DLT_COMPLIANCE_AGENT
  - Segment cap violations > 0: Alert to GEO_COMPLIANCE_AGENT + PLATFORM_ADMIN
  - Reference data staleness > 10 minutes: Warning → OBSERVABILITY_AGENT
  - Reference data staleness > 15 minutes: CRITICAL — halt TRANSACTIONAL/OTP dispatch
  - Cache hit rate < 60%: Warning (Redis issue or new message diversity)
  - Dispatch halt rate > 1% of requests: Alert to Notification Service + PLATFORM_ADMIN
  - BIDI_OVERRIDE_DETECTED > 0: Immediate SECURITY alert → FRAUD_DETECTION_ENGINE

INTEGRATION:
  - Prometheus scrape: every 15 seconds
  - Loki: structured JSON logs per computation
  - OpenTelemetry: distributed trace ID propagated from Notification Service request
  - Wazuh: security event correlation (bidi override, Unicode uplift patterns)
```

---

## 1️⃣5️⃣ DLT COMPLIANCE INTEGRATION

```yaml
DLT_COMPLIANCE_INTEGRATION:
  Authority     : TRAI TCCCPR 2018 + amendments
  This agent's DLT role: Template expansion verification + compliance hash generation
  Full DLT compliance (template registration, header validation, content scrubbing):
    handled by DLT_COMPLIANCE_AGENT — this agent provides the pre-send compliance data

  DATA_THIS_AGENT_EMITS_TO_DLT_COMPLIANCE_AGENT:
    - dlt_template_id
    - dlt_expansion_hash (SHA-256 of expanded content)
    - expanded_content_length (not raw content — privacy)
    - variable_count_used
    - encoding_classification (Unicode in OTP is a compliance signal)
    - segment_count (for DLT audit — carriers report segment counts)
    - sender_id
    - tenant_id
    - timestamp_utc

  TRAI_AUDIT_READINESS:
    This agent's audit records provide:
    - Proof that segment count was computed before dispatch
    - Proof that DLT template expansion was validated
    - Character-level evidence for any segment count dispute
    - Immutable, timestamped record linking template → expanded content → segment count
    - Evidence that encoding was correctly classified per TRAI-registered template

  SENDER_ID_VALIDATION:
    Sender ID (header) must match DLT-registered headers for tenant
    This agent validates: sender_id ∈ tenant.registered_dlt_headers
    IF NOT FOUND: HALT dispatch, return UNREGISTERED_SENDER_ID
    DLT header type must match SMS category:
      OTP/TRANSACTIONAL/SERVICE: 6-char alphabetic header
      PROMOTIONAL: 6-digit numeric header
      Mismatch: SENDER_HEADER_CATEGORY_MISMATCH → halt
```

---

## 1️⃣6️⃣ VERSIONING POLICY

```yaml
CURRENT_VERSION          : SEGMENT_MODEL_v1.0.0
MUTATION_POLICY          : Add-only — new rules added as new version
                           Existing encoding rules immutable once published
VERSION_COMPONENTS:
  CHARACTER_TABLE_VERSION  : GSM7_TABLE_v1.0 (GSM 03.38 — stable international standard)
  ROUTE_REGISTRY_VERSION   : Updated per carrier contract changes (add-only)
  DLT_TEMPLATE_VERSION     : Updated per DLT platform sync (add-only)

VERSION_ACTIVATION:
  New SEGMENT_MODEL version requires:
  - PLATFORM_ADMIN activation
  - Prior version remains for in-flight dispatches
  - All audit records include model_version

CHARACTER_TABLE_IMMUTABILITY:
  GSM-7 character table is defined by international standard (GSM 03.38)
  This table does NOT change — it is immutable at v1.0.0
  Any future amendment to GSM standard requires new model version + PLATFORM_ADMIN approval

ROUTE_REGISTRY_VERSIONING:
  Route changes (segment caps, billing multipliers): new version entry (add-only)
  Historical routes retained for reconciliation and dispute purposes
  Segment calculations use route version active at request time

BACKWARD_COMPATIBILITY:
  All outputs include model_version — historical segment counts auditable
  against the version that produced them
  No retroactive segment count recalculation for dispatched messages

SCHEMA_MIGRATION:
  New fields: optional with null-safe defaults
  No field removal or rename
  Flyway manages PostgreSQL schema versions
```

---

## 1️⃣7️⃣ NON-NEGOTIABLE RULES

```yaml
THIS AGENT MUST NOT:

  ✗ Estimate segment counts — every output is deterministic and exact
  ✗ Use AI/LLM for any character encoding decision — rule engine only, always
  ✗ Silently dispatch a message with an unknown encoding state
  ✗ Allow message dispatch if dlt_template_id is required but missing
  ✗ Allow message dispatch with unregistered sender_id
  ✗ Accept message_content with bidirectional override characters without halting
  ✗ Accept message_content with zero-width characters without halting
  ✗ Process requests from unregistered services (no JWT = instant rejection)
  ✗ Allow cross-tenant DLT template access — template lookup always includes tenant_id
  ✗ Produce billing_unit_count without a valid, committed audit_reference
  ✗ Override route segment caps for any reason — enforcement is absolute
  ✗ Apply retroactive segment count corrections to already-dispatched messages
  ✗ Store raw message content in any persistent store — content_hash only
  ✗ Store recipient MSISDNs or phone numbers — these never enter this agent
  ✗ Suppress any halt event — every halt produces a Kafka event and audit record
  ✗ Produce confidence_score < 1.0 — segment calculation is deterministic or it halts
  ✗ Allow preferred_encoding_hint to bypass Unicode detection — hint is advisory only
  ✗ Emit billing_unit_count > 0 for any halted dispatch
  ✗ Allow reference data staleness > 15 minutes without halting TRANSACTIONAL/OTP dispatch
  ✗ Return empty segment_boundary_map — every result includes full boundary index array
```

---

## 1️⃣8️⃣ DEPLOYMENT CONTEXT

```yaml
KUBERNETES_NAMESPACE       : messaging
SERVICE_IDENTITY           : sms-segment-calculation-agent
RESOURCE_PROFILE:
  CPU_REQUEST              : 200m (in-memory computation — lightweight)
  CPU_LIMIT                : 1000m
  MEMORY_REQUEST           : 256Mi (GSM-7 tables + LRU cache fit in memory)
  MEMORY_LIMIT             : 1Gi
  REPLICAS_MIN             : 5 (always-on HA — synchronous pre-send gate)
  REPLICAS_MAX             : 200 (HPA on request rate — 100K req/s target)
  HPA_METRIC               : requests_per_second (custom Prometheus metric)
  HPA_SCALE_UP_THRESHOLD   : 20,000 req/s per pod
  HPA_SCALE_DOWN_COOLDOWN  : 5 minutes (prevent thrashing during GD session waves)

STARTUP_SEQUENCE:
  1. Load GSM-7 character tables into memory
  2. Load DLT template registry (from PostgreSQL → in-memory LRU)
  3. Load carrier route registry (from PostgreSQL → in-memory map)
  4. Connect to Redis (dedup + cache)
  5. Warm Kafka producer (async audit emitter)
  6. Register with Auth Service (JWT validation public key fetch)
  7. Mark READY (readiness probe passes)
  Total startup target: < 10 seconds

HEALTH_CHECKS:
  LIVENESS   : /health/live (200 = process running)
  READINESS  : /health/ready (200 = GSM-7 tables loaded + DLT registry loaded
               + route registry loaded + Redis connected + Auth Service reachable)
  STARTUP    : 15s grace period

GRACEFUL_SHUTDOWN:
  - Drain in-flight synchronous requests (max 5s drain window)
  - Flush Kafka audit emit buffer
  - Release Redis connections
  - Emit agent.shutdown event to OBSERVABILITY_AGENT
  - Shutdown timeout: 15 seconds (fast — stateless)

CI_CD_PIPELINE:
  - GitHub Actions: build → unit test → integration test → security scan → image push
  - Helm chart: blue/green deployment, automated rollback on health check failure
  - GSM-7 character table changes require PR approval from 2 reviewers + test suite pass
  - DLT template registry schema changes require PLATFORM_ADMIN approval
  - No manual production deployments
```

---

## 1️⃣9️⃣ TESTING CONTRACT

```yaml
REQUIRED_TEST_COVERAGE     : 98% minimum (safety-critical billing agent — enforced in CI)

UNIT_TESTS — CHARACTER ENCODING:
  - All 128 GSM-7 basic characters → encoding = GSM7 (individual and combined)
  - All 10 GSM-7 extended characters → encoding = GSM7_EXTENDED + effective_length +2
  - Every common Unicode trigger (each Devanagari block, Tamil, emoji, smart quotes, em dash)
  - Mixed GSM7 + 1 Unicode char → encoding = UNICODE_UCS2 (entire message)
  - Surrogate pair emoji → UCS-2 char count = 2
  - BMP emoji → UCS-2 char count = 1
  - Smart quote vs ASCII quote disambiguation
  - Em dash vs ASCII hyphen disambiguation

UNIT_TESTS — SEGMENT MATHEMATICS:
  - GSM-7: exactly 160 chars → 1 segment (boundary)
  - GSM-7: 161 chars → 2 segments (CEIL(161/153) = 2)
  - GSM-7: 306 chars → 2 segments (CEIL(306/153) = 2)
  - GSM-7: 307 chars → 3 segments (CEIL(307/153) = 3) (boundary)
  - Unicode: exactly 70 chars → 1 segment (boundary)
  - Unicode: 71 chars → 2 segments (CEIL(71/67) = 2)
  - Unicode: 134 chars → 2 segments (CEIL(134/67) = 2)
  - Unicode: 135 chars → 3 segments (boundary)
  - Extended chars: 159 visible + 1 extended → effective 160 → 1 segment
  - Extended chars: 159 visible + 2 extended → effective 161 → 2 segments
  - All CEIL rounding edge cases (n/multi_limit exactly whole number)

UNIT_TESTS — DLT TEMPLATE EXPANSION:
  - Happy path variable substitution
  - Missing required variable → halt
  - Variable exceeds max_length → halt
  - Variable wrong content_type (NUMERIC with letters) → halt
  - Variable expansion changes encoding (ASCII name → Unicode name) → flag
  - Variable expansion crosses segment boundary (1→2 segments) → flag
  - Template not found in registry → halt
  - Template with zero variables (fixed content) → no variable_map required

UNIT_TESTS — SANITIZATION:
  - Each control character category → halt
  - Zero-width characters (U+200B, U+200C, U+200D, U+FEFF) → halt
  - Bidirectional override (U+202E) → halt + FRAUD escalation
  - Unicode replacement character (U+FFFD) → halt
  - LF and CR → permitted (not halted)
  - Valid emoji (not bidi, not ZWC) → allowed, Unicode encoding applied

UNIT_TESTS — CATEGORY POLICIES:
  - OTP > 1 segment → halt with OTP_EXCEEDS_SINGLE_SEGMENT
  - TRANSACTIONAL > 3 segments → halt
  - PROMOTIONAL > 5 segments → halt
  - OTP with unregistered sender_id → halt

INTEGRATION_TESTS:
  - Full request lifecycle: HTTP request → segment_count → Kafka emit → audit record
  - DLT template cache miss → PostgreSQL fallback → compute → cache store
  - Route registry lookup for domestic + international routes
  - Redis dedup: identical request_id within 60s returns cached result
  - Redis unavailable: computation continues without cache
  - Batch estimation API for campaign pre-flight
  - Jasmin pre-dispatch hook integration

PERFORMANCE_TESTS:
  - 100,000 requests/second sustained throughput → P99 < 5ms
  - Pod startup time < 10 seconds with full reference data load
  - LRU cache effectiveness at 10,000 active DLT templates
  - Memory consumption at max request rate (1Gi limit not exceeded)
  - Latency under HPA scale-up event (new pods coming online during traffic spike)

SECURITY_TESTS:
  - Unsigned JWT → rejected
  - Cross-tenant DLT template access → rejected
  - SQL injection in variable_map values → sanitized/rejected
  - Bidi override in message_content → halted + FRAUD escalation verified
  - Rate limit enforcement: 100,001st request in 1 minute → 429 returned
  - request_id replay within 60s → cached result returned (no double-compute)
```

---

## 2️⃣0️⃣ AGENT SEAL DECLARATION

```
╔══════════════════════════════════════════════════════════════════════════════╗
║                             AGENT SEAL                                       ║
║                                                                              ║
║  AGENT   : SMS_SEGMENT_CALCULATION_AGENT                                     ║
║  VERSION : SEGMENT_MODEL_v1.0.0                                              ║
║  STATUS  : SEALED · LOCKED · PRODUCTION-READY · TRAI-COMPLIANT              ║
║                                                                              ║
║  This agent specification is add-only from this point forward.              ║
║  No encoding rule, formula, limit table, or dispatch policy may be          ║
║  removed or modified without:                                                ║
║    (a) Version bump to SEGMENT_MODEL_v1.1.0 or higher                       ║
║    (b) PLATFORM_ADMIN written approval                                       ║
║    (c) Audit log entry for modification rationale                           ║
║    (d) Migration documentation for backward compatibility                   ║
║    (e) BILLING_GOVERNANCE_AGENT sign-off for any change affecting           ║
║        billing_unit_count computation                                        ║
║    (f) GEO_COMPLIANCE_AGENT sign-off for any change affecting DLT           ║
║        template expansion or TRAI regulatory rules                          ║
║    (g) TELECOM_USAGE_RECONCILIATION_AGENT notified of formula changes       ║
║        (reconciliation depends on stable segment math)                      ║
║                                                                              ║
║  Any execution that deviates from the segment formulas in Section 3 is a   ║
║  GOVERNANCE VIOLATION — segment_count is a financial instrument.            ║
║                                                                              ║
║  The GSM-7 character tables in Section 3 are based on international         ║
║  standard GSM 03.38 and are immutable. Any claimed "correction" to         ║
║  these tables requires PLATFORM_ADMIN approval and external standard        ║
║  citation — they may not be changed by operational request.                 ║
║                                                                              ║
║  SEALED BY : ECOSKILLER ANTIGRAVITY INTELLIGENCE & INNOVATION CORE          ║
║  PLATFORM  : ECOSKILLER ANTIGRAVITY                                          ║
║  ARCH TYPE : Enterprise SaaS · Zero-Trust · Multi-Tenant · TRAI-Compliant  ║
║  STACK LOCK: Jasmin SMS Gateway · Kafka · Redis · PostgreSQL · ClickHouse  ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

*End of SMS_SEGMENT_CALCULATION_AGENT.md — SEGMENT_MODEL_v1.0.0 — SEALED*
