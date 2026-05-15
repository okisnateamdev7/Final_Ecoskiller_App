# PIN_EXPIRY_ENFORCEMENT_AGENT
**System:** ECOSKILLER  
**Layer:** Session & Lifecycle  
**Target Engine:** ANTIGRAVITY  
**Status:** SEALED · LOCKED · GOVERNED · DETERMINISTIC  
**Version:** 1.0.0  
**Mutation Policy:** Add-only via version bump  
**Interpretation Authority:** NONE  
**Execution Authority:** Human declaration only  

---

## ⛔ LOCK DECLARATION

```
AGENT_ID           = PIN_EXPIRY_ENFORCEMENT_AGENT
EXECUTION_ENGINE   = ANTIGRAVITY
DOMAIN             = Session & Lifecycle
LAYER              = Auth + State + Enforcement
DETERMINISM_RULE   = Identical input → Identical output
FAILURE_MODE       = STOP → REPORT → NO PARTIAL OUTPUT
ANTIGRAVITY_SAFE   = TRUE
REINTERPRETATION   = FORBIDDEN
```

---

## I. AGENT PURPOSE (NON-NEGOTIABLE)

The `PIN_EXPIRY_ENFORCEMENT_AGENT` is a deterministic, rule-driven sub-agent operating within the **Session & Lifecycle** layer of ECOSKILLER.

Its sole mandate is:

> **To enforce the complete lifecycle of all PIN-based authentication tokens — from generation through expiry — with zero tolerance for override, discretion, or exception.**

This agent governs:
- OTP / PIN generation for login, verification, identity confirmation
- Session PIN tokens used in GD room entry, interview slot locks, and dojo match confirmation
- PIN expiry timers enforced via **Redis TTL** with atomic state transitions
- Hard blocking of expired PIN reuse across all entry points

This agent does NOT:
- Interpret intent
- Grant extensions based on user claims
- Allow retry after expiry without a full regeneration cycle

---

## II. SYSTEM ARCHITECTURE BINDING

### Governed By:
| Component | Role |
|---|---|
| **Redis** | Authoritative PIN state store — TTL-enforced, atomic |
| **Auth Service** | PIN issuance and lifecycle ownership |
| **Session Orchestrator** | Consumes agent verdicts for GD / Interview / Dojo entry |
| **Notification Service** | Triggers resend / expiry alerts |
| **PostgreSQL** | Immutable audit log of all PIN events |
| **Kafka / RabbitMQ** | Emits `pin.expired`, `pin.used`, `pin.regenerated` events |
| **WebSocket Channel** | Delivers real-time expiry countdown to frontend |
| **OPA (Open Policy Agent)** | Authorizes PIN validation attempts against RBAC rules |

### Antipatterns (FORBIDDEN):
- Checking PIN expiry in application memory
- Bypassing Redis TTL with application-layer timers
- Extending TTL without full audit log entry
- Allowing frontend to self-report PIN validity
- Storing raw PINs in PostgreSQL (hash only, never plaintext)

---

## III. PIN LIFECYCLE STATE MACHINE

```
[GENERATED] → [DISPATCHED] → [PENDING_USE]
                                    ↓
                           TTL active in Redis
                                    ↓
              ┌─────────────────────┴──────────────────────┐
              │                                            │
         [USED_VALID]                               [EXPIRED_TTL]
              │                                            │
         [CONSUMED]                              [BLOCKED + LOGGED]
              │                                            │
         [AUDIT_LOGGED]                         [REGENERATION_REQUIRED]
```

**State Rules:**
- States are strictly sequential — no backward transitions
- `EXPIRED_TTL` is terminal — no extension, no grace period
- `CONSUMED` is terminal — cannot be revalidated
- `BLOCKED` emits `pin.expired` event to Kafka immediately

---

## IV. PIN TYPES & TTL POLICY

| PIN Type | Context | TTL | Max Attempts | Regeneration |
|---|---|---|---|---|
| `OTP_LOGIN` | Email/SMS login | **300 seconds** | 3 | Full new PIN |
| `OTP_VERIFY` | Account verification | **600 seconds** | 3 | Full new PIN |
| `SESSION_GD_ENTRY` | GD room join token | **120 seconds** | 1 | NOT ALLOWED after expiry |
| `SESSION_INTERVIEW_SLOT` | Interview slot lock | **180 seconds** | 1 | Admin-triggered only |
| `SESSION_DOJO_CONFIRM` | Dojo match confirmation | **90 seconds** | 1 | NOT ALLOWED after expiry |
| `OTP_PASSWORD_RESET` | Password reset | **300 seconds** | 1 | Full new PIN |
| `OTP_DEVICE_BIND` | New device confirmation | **180 seconds** | 3 | Full new PIN |

**TTL is set at generation time — immutable thereafter.**  
**Attempts counter is atomic (Redis INCR) — no application-layer workaround.**

---

## V. REDIS KEY SCHEMA (ENFORCED)

```
pin:{pin_type}:{user_id}:{session_id}    → hashed_pin_value  [TTL = type-defined]
pin_attempt:{pin_type}:{user_id}         → attempt_count     [TTL = type-defined]
pin_status:{pin_type}:{user_id}          → PENDING|USED|EXPIRED|BLOCKED
```

**Rules:**
- All keys namespaced under `pin:` prefix
- Key collision = STOP EXECUTION (must be unique per session)
- TTL set atomically with `SET ... EX ... NX` — never `SET` then `EXPIRE` separately
- No key may exist without TTL (no persistent PINs)

---

## VI. ENFORCEMENT FLOW (DETERMINISTIC)

### 6.1 PIN Generation
```
1. Auth Service receives PIN generation request
2. Agent validates: no existing PENDING PIN for same type+user
3. Generate cryptographically random 6-digit PIN (SecureRandom / crypto.randomInt)
4. Hash PIN → bcrypt / argon2 (never store raw)
5. Atomically write to Redis:
   SET pin:{type}:{uid}:{sid} {hashed_pin} EX {ttl} NX
6. Set attempt counter:
   SET pin_attempt:{type}:{uid} 0 EX {ttl}
7. Set status:
   SET pin_status:{type}:{uid} PENDING EX {ttl}
8. Emit event: pin.generated → Kafka
9. Dispatch PIN to user via Notification Service
10. Write to PostgreSQL audit log: pin_event { type, user_id, session_id, action=GENERATED, timestamp }
```

### 6.2 PIN Validation
```
1. Receive PIN attempt from user
2. Check pin_status → if EXPIRED or BLOCKED → REJECT immediately, log ATTEMPT_ON_EXPIRED
3. Check pin_attempt counter → if >= MAX_ATTEMPTS → REJECT + set BLOCKED + emit pin.blocked
4. INCR pin_attempt counter atomically
5. Fetch hashed PIN from Redis
6. If Redis key missing → status = EXPIRED (TTL already elapsed) → REJECT + log
7. Compare submitted PIN with stored hash
8. If MISMATCH → increment counter, log ATTEMPT_FAILED, return generic error
9. If MATCH:
   a. Set pin_status → CONSUMED
   b. DEL all pin keys immediately (do not wait for TTL)
   c. Emit pin.used → Kafka
   d. Write audit log: action=CONSUMED
   e. Return VALID to calling service
```

### 6.3 Expiry Handling (Passive TTL + Active Detection)
```
1. Redis TTL elapses → key auto-deleted (passive expiry)
2. On any validation attempt after expiry:
   → Key missing → Agent declares EXPIRED → REJECT
3. Kafka consumer: pin.expired event triggers:
   → Notification Service: "Your PIN has expired. Request a new one."
   → Session Orchestrator: if GD/Interview/Dojo — mark slot as MISSED
   → Audit log: action=EXPIRED_TTL
4. No grace period. No backend extension. No exception pathway.
```

---

## VII. FAILURE HANDLING (ZERO DISCRETION)

| Failure | Agent Action |
|---|---|
| Redis unreachable | STOP validation → return `SERVICE_UNAVAILABLE` → NO FALLBACK TO DB |
| PIN key missing | Treat as EXPIRED → REJECT |
| Attempt limit exceeded | Hard block → `pin_status = BLOCKED` → no further attempts |
| Duplicate generation request | Return existing PIN remaining TTL (do not regenerate) |
| Clock skew on distributed nodes | Redis is single source of truth — wall clock irrelevant |
| Frontend claims PIN not expired | Frontend claim is IGNORED — Redis state is law |
| Admin requests extension | Only permitted via `ADMIN_PIN_OVERRIDE_EVENT` in audit log + explicit re-generation (no TTL extension) |

---

## VIII. EVENTS EMITTED (KAFKA TOPICS)

| Event | Trigger | Consumers |
|---|---|---|
| `pin.generated` | PIN created | Notification, Analytics |
| `pin.dispatched` | PIN sent to user | Notification |
| `pin.used` | Successful validation | Auth, Session Orchestrator |
| `pin.expired` | TTL elapsed + attempt detected | Notification, Session, Analytics |
| `pin.blocked` | Max attempts exceeded | Auth, Security (Wazuh), Analytics |
| `pin.regenerated` | New PIN issued after expiry | Notification, Analytics |
| `pin.attempt_failed` | Wrong PIN entered | Security (Wazuh), Analytics |

All events carry: `user_id`, `session_id`, `pin_type`, `timestamp`, `tenant_id`

---

## IX. AUDIT LOG SCHEMA (POSTGRESQL — IMMUTABLE)

```sql
CREATE TABLE pin_audit_log (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id       UUID NOT NULL,
  user_id         UUID NOT NULL,
  session_id      UUID,
  pin_type        VARCHAR(40) NOT NULL,   -- OTP_LOGIN, SESSION_GD_ENTRY, etc.
  action          VARCHAR(30) NOT NULL,   -- GENERATED, DISPATCHED, CONSUMED, EXPIRED_TTL, BLOCKED, ATTEMPT_FAILED
  ip_address      INET,
  user_agent      TEXT,
  created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Row-level security enforced per tenant
ALTER TABLE pin_audit_log ENABLE ROW LEVEL SECURITY;
-- No UPDATE, no DELETE permitted — WORM policy enforced at DB + application layer
```

---

## X. SECURITY BASELINE (NON-OPTIONAL)

- PINs stored as **hash only** — plaintext never persisted
- Redis keys use **NX flag** on SET — prevents race condition overwrites  
- Attempt counter uses **atomic INCR** — no TOCTOU vulnerability
- Wazuh alert threshold: **3 consecutive `pin.blocked` events per user within 10 minutes** → Suspicious activity flag
- Rate limiting enforced at Kong/Envoy: **max 5 PIN generation requests per user per 15 minutes**
- All PIN API endpoints behind **Keycloak JWT validation** even for unauthenticated users (device fingerprint minimum)
- PII in audit logs **encrypted at rest** (HashiCorp Vault key management)

---

## XI. ANTIGRAVITY EXECUTION DIRECTIVES

```
⚠️ ANTIGRAVITY MUST NOT reinterpret this agent's lifecycle rules.
⚠️ ANTIGRAVITY MUST NOT add discretionary expiry extension logic.
⚠️ ANTIGRAVITY MUST NOT generate UI that suggests PIN retry after BLOCKED state.
⚠️ ANTIGRAVITY MUST render PIN expiry countdown from WebSocket channel only.
⚠️ ANTIGRAVITY MUST surface only: time_remaining, attempt_count, status.
⚠️ ANTIGRAVITY MUST NOT surface raw PIN values in any UI layer.
```

### UI Contract (Flutter / Web):
```
Widget: PinExpiryCountdownWidget
  Input:  WebSocket event { remaining_seconds, attempts_left, status }
  States: ACTIVE | WARNING (≤30s) | EXPIRED | BLOCKED
  On EXPIRED: Show "PIN Expired — Request New" CTA only
  On BLOCKED: Show "Account temporarily locked" — no CTA for 15 minutes
  No retry button rendered when status = EXPIRED or BLOCKED
  Lifecycle: Disposed on CONSUMED or EXPIRED — no zombie listeners
```

---

## XII. PRODUCTION READINESS CHECKLIST

```
[ ] Redis NX+TTL atomic SET verified in integration tests
[ ] PIN hash algorithm confirmed: argon2id or bcrypt (cost ≥ 12)
[ ] All 7 Kafka events confirmed publishing and consuming
[ ] PostgreSQL audit table: UPDATE and DELETE blocked at DB level
[ ] Wazuh rule configured for pin.blocked threshold alert
[ ] Kong rate limit: 5 PIN gen / user / 15min active
[ ] WebSocket expiry countdown tested on mobile network throttle
[ ] GD/Interview/Dojo slot MISSED event confirmed on PIN expiry
[ ] Zero plaintext PIN in any log, metric, or trace (OpenTelemetry verified)
[ ] OPA policy: PIN validation only callable by Auth Service
[ ] Antigravity UI: no retry CTA rendered post-BLOCKED confirmed
```

Absence of any checked item → **STOP EXECUTION**

---

## XIII. FINAL LOCK

```
AGENT_LOCK_HASH     = PIN_EXPIRY_ENFORCEMENT_AGENT_v1.0.0
READY_FOR           = ANTIGRAVITY_PRODUCTION
ANTIGRAVITY_CONFUSION = IMPOSSIBLE
REINTERPRETATION    = FORBIDDEN
EXTENSION_BY_AI     = FORBIDDEN
OVERRIDE_BY_USER    = FORBIDDEN (Admin re-generation only, fully audited)
```

> **This agent is the law for all PIN lifecycles in ECOSKILLER.**  
> **Redis is the clock. PostgreSQL is the witness. Kafka is the signal.**  
> **Nothing expires gracefully. Everything expires exactly.**
