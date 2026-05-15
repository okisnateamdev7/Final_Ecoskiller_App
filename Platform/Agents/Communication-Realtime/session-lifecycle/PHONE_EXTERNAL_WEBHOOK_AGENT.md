# PHONE_EXTERNAL_WEBHOOK_AGENT
## ECOSKILLER PLATFORM — SEALED EXECUTION PROMPT
```
PROMPT_CLASS         = PHONE_EXTERNAL_WEBHOOK_AGENT
EXECUTION_ENGINE     = ANTIGRAVITY
ENGINEERING_GRADE    = PRINCIPAL_ENGINEER
DOCUMENT_VERSION     = v1.0.0
STATUS               = FINAL · LOCKED · GOVERNED · DETERMINISTIC
MUTATION_POLICY      = ADD_ONLY VIA VERSION BUMP
ASSUMPTION_POLICY    = FORBIDDEN
IMPLICIT_BEHAVIOR    = FORBIDDEN
FAILURE_POLICY       = HARD_STOP → REPORT → NO PARTIAL OUTPUT
INTERPRETATION_AUTH  = NONE
EXECUTION_AUTHORITY  = HUMAN DECLARATION ONLY
```

---

## SECTION 0 — AGENT IDENTITY & SCOPE

```
AGENT_NAME           = PHONE_EXTERNAL_WEBHOOK_AGENT
AGENT_TYPE           = External Channel Orchestrator · Webhook Security Enforcer · Inbound/Outbound Integration Governor
AGENT_SCOPE          = ALL external communication channels + ALL inbound/outbound webhooks across every platform domain
PARENT_CONSTITUTION  = ECOSKILLER MASTER EXECUTION PROMPT v12.0
SIBLING_AGENTS       = MODEL_GOVERNANCE_REGISTRY_AGENT · UI_GENERATION_AGENT · BACKEND_CONTRACT_AGENT
CONTRACT_GATE_ROLE   = EXTERNAL_CHANNEL_READY signal — blocks Notification Service, Billing Service,
                       Integration Hub, and any service that dispatches or receives external data
                       until ALL channel contracts, webhook security rules, and provider bindings
                       are registered and validated by this agent.
```

This agent governs the complete lifecycle of:

- **Phone / SMS delivery** via Jasmin SMS Gateway (self-hosted)
- **Email delivery** via Postfix + Docker Mail Server (self-hosted)
- **Push notifications** via ntfy (self-hosted)
- **OTP generation, dispatch, validation, and expiry** across all channels
- **Inbound webhooks** — payment gateways, identity providers, third-party callbacks
- **Outbound webhooks** — SEO revalidation, Integration Hub, HRIS/LMS, Forgejo CI triggers
- **Webhook security** — signed payloads, HMAC verification, replay protection, IP allowlisting
- **Channel preference, fallback chains, and retry logic** per user role and event class
- **External provider blast-domain isolation** — no cross-domain provider bleed
- **Rate limiting and abuse prevention** on all outbound channels

```
ABSENCE OF THIS AGENT'S CHANNEL CONTRACTS → STOP EXECUTION
UNSIGNED WEBHOOK RECEIVED OR DISPATCHED  → HARD_STOP → REPORT UNSIGNED_WEBHOOK_VIOLATION
PLAINTEXT CHANNEL TRAFFIC                → HARD_STOP → REPORT PLAINTEXT_CHANNEL_FORBIDDEN
```

---

## SECTION 1 — SYSTEM CONTEXT (READ-ONLY)

Antigravity must treat the following as immutable platform reality. Do not reinterpret.

### 1.1 Platform Identity
```
PLATFORM_NAME        = ECOSKILLER
ARCHITECTURE_STYLE   = Event-Driven Microservices
NOTIFICATION_SERVICE = 12th core microservice (email · SMS · push · in-app · orchestration)
INTEGRATION_HUB      = 15th core microservice (LinkedIn · GitHub · LMS/HRIS · Webhook framework)
BILLING_SERVICE      = 13th core microservice (receives inbound payment webhooks)
MESSAGING_BACKBONE   = Apache Kafka 3.7.0 (all internal events)
SECRETS_STORE        = HashiCorp Vault (K8s Auth — no hardcoded credentials anywhere)
TLS_MINIMUM          = TLS 1.2 (TLS 1.3 preferred) — TLS 1.0/1.1/SSL FORBIDDEN
SMTP_WITHOUT_TLS     = FORBIDDEN
PLAINTEXT_WEBHOOK    = FORBIDDEN
SELF_HOSTED_MANDATE  = TRUE — no paid SaaS for any channel infrastructure
```

### 1.2 Channel Infrastructure Stack (LOCKED)
```
SMS_GATEWAY          = Jasmin SMS Gateway (self-hosted, Docker, ops namespace)
EMAIL_ENGINE         = Postfix (MTA) + Docker Mail Server (full bundle, self-hosted)
PUSH_ENGINE          = ntfy (self-hosted, lightweight, no Firebase, no APNs dependency for internal)
IN_APP_CHANNEL       = WebSocket command channel (existing realtime infrastructure)
EXTERNAL_DNS         = PowerDNS (self-hosted) — Cloudflare FORBIDDEN
TLS_ISSUER           = cert-manager (self-hosted, Let's Encrypt or internal CA)
WEBHOOK_RECEIVER     = Dedicated webhook-receiver microservice (ingress namespace)
WEBHOOK_DISPATCHER   = Integration Hub microservice (outbound webhooks)
RATE_LIMITER         = Envoy (per-IP + per-user) + Kong (API gateway rate limits)
SECRET_ROTATION      = HashiCorp Vault dynamic secrets — no static API keys in code
```

### 1.3 User Roles Under This Agent's Authority
```
ROLE_01 = STUDENT           (phone OTP verification, job alert SMS, GD session push)
ROLE_02 = TRAINER/MENTOR    (session reminder email + push, evaluation alert)
ROLE_03 = EVALUATOR/JUDGE   (assignment notification, scoring alert)
ROLE_04 = INSTITUTE_ADMIN   (bulk student notification, compliance alert email)
ROLE_05 = ENTERPRISE_ADMIN  (billing webhook receiver, invoice email, payment alerts)
ROLE_06 = RECRUITER_HR      (candidate pipeline update email + push)
ROLE_07 = PLATFORM_ADMIN    (system alert email + push, security incident SMS)
ROLE_08 = PARENT            (consent request SMS + email, child activity alert — read-only)
ROLE_09 = SOCIETY_ADMIN     (batch notification, commission payout SMS)
ROLE_10 = COACH/COORDINATOR (workshop alert push + SMS, attendance compliance warning)
ROLE_11 = AUTOMATION_AGENT  (webhook dispatch only — no direct user channel access)
```

---

## SECTION 2 — CHANNEL CONTRACT REGISTRY (NON-NEGOTIABLE)

### 2.1 Channel Standards
```
FORMAT              = Versioned YAML channel contract
VERSIONING          = MAJOR.MINOR.PATCH
BREAKING_CHANGE     = MAJOR (provider swap, security model change, schema change)
REGISTRY_LOCATION   = /contracts/channels/{channel_name}/v{version}/channel.yaml
GATE_PRODUCED       = external_channel_ready
GATE_CONSUMED_BY    = Notification Service · Billing Service · Integration Hub · Auth Service (OTP)
MISSING_CONTRACT    → STOP EXECUTION → REPORT CHANNEL_CONTRACT_MISSING:{channel_name}
```

### 2.2 Channel Catalog

#### CHANNEL_001: SMS via Jasmin Gateway
```yaml
channel_id:          sms-jasmin-v1
channel_type:        SMS
provider:            Jasmin SMS Gateway
deployment:          self-hosted · Docker · ops namespace
image:               jookies/jasmin:latest  # Apache 2.0
k8s_service:         jasmin-sms.ops.svc.cluster.local
port:                2775  # SMPP
http_api_port:       8080  # Jasmin HTTP API
protocol:            SMPP v3.4 (primary) · HTTP REST (fallback)
tls_required:        TRUE (TLS 1.2 minimum on HTTP API)
secret_location:     vault://secret/ops/jasmin/smpp_credentials
sender_id:           ECOSKL  (6-char alphanumeric, regulatory compliant)
max_message_length:  160 chars (single SMS) · 612 chars (concatenated, max 4 segments)
encoding:            GSM-7 (Latin) | UCS-2 (Unicode/regional scripts)
delivery_report:     REQUIRED — logged to PostgreSQL (notification schema)
throughput_limit:    50 messages/second (configurable per operator binding)
use_cases:
  - OTP delivery (primary channel for Tier-1 students)
  - GD session reminder (T-30 min, T-5 min)
  - Commission payout notification (Society domain)
  - Parent consent request (when email not verified)
  - Critical security alert (account takeover detected)
  - Billing payment failure (tenant admin)
forbidden_use_cases:
  - Marketing/promotional bulk SMS (separate compliance process)
  - Sending PII in message body (phone numbers, Aadhaar, passwords)
  - Raw error messages or internal IDs in body
  - Cross-tenant SMS routing
rate_limit_per_user: 10 SMS/hour · 50 SMS/day
rate_limit_per_ip:   20 SMS/hour (OTP endpoint)
retry_policy:        3 attempts · exponential backoff (30s, 2m, 10m)
fallback_channel:    email-postfix-v1 (if SMS delivery fails after 3 retries)
```

#### CHANNEL_002: Email via Postfix + Docker Mail Server
```yaml
channel_id:          email-postfix-v1
channel_type:        EMAIL
provider:            Postfix MTA + Docker Mail Server
deployment:          self-hosted · Docker · ops namespace
image:               mailserver/docker-mailserver:latest  # MIT license
k8s_service:         mailserver.ops.svc.cluster.local
smtp_port:           587 (STARTTLS mandatory) · 465 (SMTPS)
plain_smtp_port_25:  INTERNAL relay only (no external plaintext)
tls_required:        TRUE — STARTTLS or SMTPS (TLS 1.2 minimum)
dkim_enabled:        TRUE — DKIM signing mandatory for all outbound email
spf_enabled:         TRUE — SPF record required in PowerDNS
dmarc_enabled:       TRUE — DMARC policy: quarantine (p=quarantine)
secret_location:     vault://secret/ops/mailserver/smtp_credentials
from_address:        noreply@ecoskiller.com (transactional)
                     alerts@ecoskiller.com (security/compliance)
                     billing@ecoskiller.com (invoices, payment alerts)
                     society@ecoskiller.com (society domain)
reply_to_policy:     suppressed for transactional · support@ecoskiller.com for alerts
max_email_size:      10 MB (including attachments)
rate_limit_per_user: 20 emails/hour · 100 emails/day
bounce_handling:     REQUIRED — hard bounce → mark address undeliverable → suppress future sends
spam_trap_handling:  REQUIRED — IP reputation monitoring via self-hosted reputation check
use_cases:
  - Account registration confirmation (with email OTP)
  - Parent consent request email (with signed consent link)
  - Licensing contract dispatch (PDF attachment)
  - Invoice delivery (PDF attachment via MinIO presigned URL)
  - Royalty payout confirmation
  - Billing failure alert
  - Certificate issuance notification (with QR verification link)
  - Security incident alert
  - Password reset (with time-limited signed token)
  - Weekly digest (opt-in only)
forbidden_use_cases:
  - Sending passwords or raw secrets in body
  - Sending Aadhaar or financial account numbers in body
  - Unsolicited marketing email without explicit opt-in
  - Email without unsubscribe link (for non-transactional)
  - Sending email from unverified sender domain
retry_policy:        5 attempts · exponential backoff (1m, 5m, 15m, 1h, 6h)
fallback_channel:    sms-jasmin-v1 (for OTP/critical only, if email bounces)
template_engine:     Jinja2 / Handlebars (server-side rendering only — no client-side)
template_storage:    /contracts/notifications/templates/email/{template_id}/v{version}/
attachment_storage:  MinIO presigned URL (max 7-day expiry) — no inline base64 attachments in email
```

#### CHANNEL_003: Push Notifications via ntfy
```yaml
channel_id:          push-ntfy-v1
channel_type:        PUSH
provider:            ntfy (self-hosted)
deployment:          self-hosted · Docker · ops namespace
image:               binwiederhier/ntfy:latest  # Apache 2.0
k8s_service:         ntfy.ops.svc.cluster.local
port:                80 (internal) · 443 (external via NGINX ingress)
tls_required:        TRUE (external endpoint — TLS 1.2 minimum)
secret_location:     vault://secret/ops/ntfy/access_token
topic_naming:        {tenant_id}_{user_id}_{channel_type}
                     e.g. t1a2b3_u9x8y7_alerts
topic_auth:          Per-user bearer token (Vault-issued, scoped to topic)
payload_max_size:    4 KB
firebase_dependency: NONE — ntfy self-hosted does not require Firebase
apns_dependency:     NONE for ntfy server-sent; mobile clients use SSE/WebSocket
use_cases:
  - GD session starting now (real-time, high priority)
  - Interview slot booked (immediate confirmation)
  - Dojo match assigned
  - Belt eligibility achieved
  - Royalty payment received
  - New job match above 85% score
  - Admin alert (system anomaly, security event)
  - Society batch attendance below threshold
rate_limit_per_user: 50 push/hour
priority_levels:     URGENT · HIGH · DEFAULT · LOW · MIN
  URGENT:            security alerts, GD/session imminent (< 5 min)
  HIGH:              payment events, consent requests
  DEFAULT:           job matches, reminders
  LOW:               digests, streak alerts
  MIN:               marketing-adjacent (opt-in required)
retry_policy:        3 attempts · 30s backoff · no retry on MIN priority
fallback_channel:    email-postfix-v1 (for HIGH+ priority if push fails)
forbidden_use_cases:
  - Sending PII in push body
  - Sending financial amounts in push body (use masked amounts only, e.g. "₹**,***")
  - Cross-tenant topic subscription
  - Sending push without user opt-in for LOW/MIN priority
```

#### CHANNEL_004: In-App Notifications via WebSocket
```yaml
channel_id:          inapp-websocket-v1
channel_type:        IN_APP
provider:            Existing WebSocket command channel (realtime infrastructure)
k8s_service:         realtime-service.realtime.svc.cluster.local
protocol:            WSS (WebSocket Secure) — WS (plain) FORBIDDEN
auth:                JWT bearer token (same session token as REST API)
message_format:      JSON — see SECTION 4.2 in-app notification schema
persistence:         notification_log table (notification PostgreSQL schema)
                     Undelivered notifications: held 7 days, then purged
rate_limit_per_user: 100 in-app/hour
delivery_ack:        Client must ACK receipt — unACKed after 30s → mark unread in DB
use_cases:
  - Real-time GD turn notifications (speaking token granted/revoked)
  - Dojo match state changes
  - Idea similarity check result
  - Score finalized
  - Admin moderation action result
  - All high-frequency, session-bound events
forbidden_use_cases:
  - Financial transaction confirmation (must also use email)
  - Legal/consent events (must also use email + SMS)
  - Security alerts (must also use email + push)
```

---

## SECTION 3 — OTP SUBSYSTEM CONTRACT

### 3.1 OTP Standards
```
OTP_SERVICE          = Auth Service (owns OTP generation and validation)
OTP_STORAGE          = Redis 7 (TTL-enforced, never PostgreSQL)
OTP_FORMAT           = 6-digit numeric (TOTP or random — see per-type spec)
OTP_ENCRYPTION       = Stored as SHA-256 hash in Redis (never plaintext)
OTP_CHANNEL_PRIORITY = See per-role matrix (Section 3.3)
BRUTE_FORCE_LIMIT    = 5 failed attempts → OTP invalidated → new OTP required
RATE_LIMIT           = 3 OTP requests per phone/email per 10 minutes
SECRET_LOCATION      = vault://secret/auth/otp_signing_key
AUDIT_LOG            = Every OTP request, validation attempt, expiry → audit log (compliance schema)
FORBIDDEN            = OTP value must NEVER appear in application logs
FORBIDDEN            = OTP must NEVER be sent via in-app channel alone (always SMS or email)
```

### 3.2 OTP Type Catalog

#### OTP_TYPE_001: Phone Number Verification OTP
```yaml
otp_type:            phone_verification
trigger_event:       user.phone.verification_requested
channel:             SMS (sms-jasmin-v1)
length:              6 digits
ttl_seconds:         300  # 5 minutes
max_attempts:        5
message_template:    NT_OTP_001
body_pattern:        "Your ECOSKILLER verification code is {otp}. Valid for 5 minutes. Do not share."
on_success_event:    user.phone.verified
on_failure_event:    user.phone.verification_failed
on_expiry_event:     user.phone.otp_expired
redis_key_pattern:   otp:phone_verify:{user_id}:{phone_hash}
rate_limit:          3 requests per phone per 10 minutes
fallback:            NONE — phone verification requires SMS only
roles_allowed:       ALL (during registration and phone change flow)
```

#### OTP_TYPE_002: Email Verification OTP
```yaml
otp_type:            email_verification
trigger_event:       user.email.verification_requested
channel:             EMAIL (email-postfix-v1)
length:              6 digits
ttl_seconds:         900  # 15 minutes
max_attempts:        5
template_id:         NT_OTP_002
on_success_event:    user.email.verified
redis_key_pattern:   otp:email_verify:{user_id}:{email_hash}
rate_limit:          3 requests per email per 10 minutes
```

#### OTP_TYPE_003: Login Step-Up OTP (MFA Fallback — Tier-1 Students Only)
```yaml
otp_type:            login_stepup
trigger_event:       user.login.stepup_required
channel_priority:
  primary:           SMS (sms-jasmin-v1) — if phone verified
  fallback:          EMAIL (email-postfix-v1) — if phone not verified
length:              6 digits
ttl_seconds:         180  # 3 minutes — short window for step-up
max_attempts:        3     # lower — step-up is sensitive
on_failure_3x:       session.terminated + user.mfa.stepup_failed event
roles_allowed:       STUDENT (Tier-1) only — higher tiers use TOTP, not SMS OTP
                     FORBIDDEN for: PLATFORM_ADMIN, COMPLIANCE_ADMIN, TENANT_ADMIN
```

#### OTP_TYPE_004: Parent Consent OTP
```yaml
otp_type:            parent_consent
trigger_event:       parent.consent.otp_requested
channel_priority:
  primary:           SMS (sms-jasmin-v1)
  secondary:         EMAIL (email-postfix-v1) — sent simultaneously, not as fallback
length:              6 digits
ttl_seconds:         86400  # 24 hours — parent may not be immediately available
max_attempts:        5
on_success_event:    parent.consent.granted
on_expiry_event:     parent.consent.otp_expired → re-notify child user
audit_log:           MANDATORY — 18-year retention (CHILD_PROTECTION tag)
roles_allowed:       PARENT role only
```

#### OTP_TYPE_005: Password Reset OTP
```yaml
otp_type:            password_reset
trigger_event:       user.password.reset_requested
channel:             EMAIL (email-postfix-v1) — primary
                     SMS (sms-jasmin-v1) — only if email delivery fails AND phone is verified
length:              6 digits
ttl_seconds:         600  # 10 minutes
max_attempts:        3
on_success_event:    user.password.reset_token_issued  (short-lived JWT reset token)
on_failure_3x:       user.password.reset_locked (1-hour cooldown)
forbidden:           OTP must NOT be included in URL (phishing vector) — validated server-side only
```

#### OTP_TYPE_006: Critical Action Step-Up OTP (Financial / Legal)
```yaml
otp_type:            critical_action_stepup
trigger_events:
  - royalty.payout.dispatch_requested
  - licensing.contract.sign_requested
  - billing.refund.requested
  - user.account.deletion_requested
channel_priority:
  primary:           SMS (sms-jasmin-v1)
  secondary:         EMAIL (email-postfix-v1)
length:              6 digits
ttl_seconds:         120  # 2 minutes — financial action, very short window
max_attempts:        2    # minimum attempts — high-stakes action
on_failure_2x:       action.blocked + security.alert.dispatched to user + admin
audit_log:           MANDATORY — 15-year retention (FINANCIAL tag)
roles_allowed:       ALL roles performing financial or legal actions
```

### 3.3 OTP Channel × Role Matrix
```
ROLE                 PHONE_VERIFY  LOGIN_OTP     CRITICAL_OTP   CONSENT_OTP
STUDENT (Tier-1)     SMS           SMS→EMAIL      SMS→EMAIL      N/A
TRAINER/MENTOR       SMS           TOTP_REQUIRED  SMS→EMAIL      N/A
EVALUATOR/JUDGE      SMS           TOTP_REQUIRED  SMS→EMAIL      N/A
INSTITUTE_ADMIN      SMS           TOTP_REQUIRED  SMS→EMAIL      N/A
ENTERPRISE_ADMIN     SMS           TOTP_REQUIRED  SMS→EMAIL      N/A
PLATFORM_ADMIN       SMS           TOTP_REQUIRED  SMS+EMAIL(both) N/A
COMPLIANCE_ADMIN     SMS           HARDWARE_KEY   SMS+EMAIL(both) N/A
PARENT               SMS           SMS→EMAIL      SMS→EMAIL      SMS+EMAIL(both)
SOCIETY_ADMIN        SMS           TOTP_REQUIRED  SMS→EMAIL      N/A
COACH/COORDINATOR    SMS           SMS→EMAIL      SMS→EMAIL      N/A

KEY:
  SMS           = sms-jasmin-v1 only
  SMS→EMAIL     = SMS primary, EMAIL fallback
  SMS+EMAIL     = Both channels dispatched simultaneously
  TOTP_REQUIRED = Time-based OTP (Authenticator app) — SMS OTP FORBIDDEN
  HARDWARE_KEY  = FIDO2 hardware key — SMS OTP FORBIDDEN
```

---

## SECTION 4 — INBOUND WEBHOOK CONTRACT REGISTRY

### 4.1 Inbound Webhook Standards
```
RECEIVER_SERVICE     = webhook-receiver (dedicated microservice, ingress namespace)
ENDPOINT_PATTERN     = https://webhooks.ecoskiller.com/v1/{provider}/{event_type}
TLS_MINIMUM          = TLS 1.2 — all inbound webhooks must arrive over HTTPS
SIGNATURE_VERIFY     = MANDATORY for ALL inbound webhooks — unsigned = REJECT + LOG
REPLAY_PROTECTION    = MANDATORY — timestamp + nonce check (5-minute window)
IDEMPOTENCY          = MANDATORY — idempotency_key stored in Redis (24h TTL)
IP_ALLOWLIST         = MANDATORY per provider — see Section 4.3
PAYLOAD_SIZE_LIMIT   = 1 MB max per webhook payload
TIMEOUT              = 10 seconds to acknowledge (return HTTP 200) — processing is async
PROCESSING_MODE      = Async via Kafka event → webhook.inbound.received
AUDIT_LOG            = EVERY inbound webhook → audit log (compliance schema)
FAILURE_RESPONSE     = HTTP 200 always (to prevent retry storms) + internal error log
PLAINTEXT_WEBHOOK    = FORBIDDEN → HARD_STOP
UNSIGNED_WEBHOOK     = FORBIDDEN → REJECT (HTTP 400) + LOG + ALERT ops channel
```

### 4.2 Registered Inbound Webhook Providers

#### WEBHOOK_IN_001: Stripe Payment Gateway
```yaml
provider_id:         stripe-v1
provider_name:       Stripe
classification:      CRITICAL (payment processing)
blast_domain:        ECOSKILLER_CORE (billing domain only)
endpoint:            POST /v1/stripe/payment-events
signature_method:    HMAC-SHA256
signature_header:    Stripe-Signature
secret_location:     vault://secret/billing/stripe/webhook_secret
ip_allowlist:
  # Stripe webhook IPs — must be updated when Stripe publishes changes
  # Source: https://stripe.com/docs/ips
  - 3.18.12.63/32
  - 3.130.192.231/32
  - 13.235.14.237/32
  - 13.235.122.149/32
  - 18.211.135.69/32
  - 35.154.171.200/32
  - 52.15.183.38/32
  - 54.88.130.119/32
  - 54.187.174.169/32
  - 54.187.205.235/32
  - 54.187.216.72/32
  - 54.241.31.99/32
  - 54.241.31.102/32
  - 54.241.34.107/32
replay_window_seconds: 300
events_handled:
  - payment_intent.succeeded      → billing.payment.received (Kafka)
  - payment_intent.payment_failed → billing.payment.failed (Kafka)
  - invoice.paid                  → billing.invoice.paid (Kafka)
  - invoice.payment_failed        → billing.invoice.payment_failed (Kafka)
  - customer.subscription.updated → billing.subscription.updated (Kafka)
  - customer.subscription.deleted → billing.subscription.cancelled (Kafka)
  - charge.dispute.created        → billing.dispute.opened (Kafka)
  - charge.refunded               → billing.refund.issued (Kafka)
forward_to_service:  billing-service (via Kafka, async)
pii_in_payload:      STRIP before forwarding to Kafka — card numbers, CVV never stored
```

#### WEBHOOK_IN_002: Razorpay Payment Gateway (India Primary)
```yaml
provider_id:         razorpay-v1
provider_name:       Razorpay
classification:      CRITICAL
blast_domain:        ECOSKILLER_CORE (billing domain only)
endpoint:            POST /v1/razorpay/payment-events
signature_method:    HMAC-SHA256
signature_header:    X-Razorpay-Signature
secret_location:     vault://secret/billing/razorpay/webhook_secret
ip_allowlist:
  # Razorpay webhook source IPs — verify at https://razorpay.com/docs/webhooks/
  - 13.234.25.104/32
  - 13.232.170.176/32
  - 52.66.193.64/32
  - 52.66.15.170/32
replay_window_seconds: 300
events_handled:
  - payment.captured              → billing.payment.received (Kafka)
  - payment.failed                → billing.payment.failed (Kafka)
  - subscription.activated        → billing.subscription.updated (Kafka)
  - subscription.cancelled        → billing.subscription.cancelled (Kafka)
  - refund.processed              → billing.refund.issued (Kafka)
  - dispute.created               → billing.dispute.opened (Kafka)
forward_to_service:  billing-service (via Kafka, async)
gst_fields:          PRESERVE — GSTIN, HSN codes required for Indian compliance
```

#### WEBHOOK_IN_003: Keycloak Identity Events
```yaml
provider_id:         keycloak-internal-v1
provider_name:       Keycloak 24.0 (self-hosted)
classification:      CRITICAL
blast_domain:        ECOSKILLER_CORE (identity domain)
endpoint:            POST /v1/keycloak/identity-events
transport:           Internal cluster only (ClusterIP — not exposed to internet)
signature_method:    HMAC-SHA256 (Keycloak event listener extension)
secret_location:     vault://secret/auth/keycloak/event_webhook_secret
ip_allowlist:        keycloak.core.svc.cluster.local only (internal)
events_handled:
  - LOGIN                         → user.session.started (Kafka)
  - LOGIN_ERROR                   → user.login.failed (Kafka)
  - LOGOUT                        → user.session.ended (Kafka)
  - REGISTER                      → user.account.created (Kafka)
  - UPDATE_PASSWORD               → user.password.changed (Kafka)
  - RESET_PASSWORD                → user.password.reset_completed (Kafka)
  - UPDATE_PROFILE                → user.profile.updated (Kafka)
  - REMOVE_TOTP                   → user.mfa.disabled (Kafka) + security.alert
  - CLIENT_INITIATED_ACCOUNT_LINKING → user.oauth.linked (Kafka)
forward_to_service:  user-service + auth-service (via Kafka, async)
```

#### WEBHOOK_IN_004: Forgejo CI/CD Events (Internal)
```yaml
provider_id:         forgejo-internal-v1
provider_name:       Forgejo (self-hosted)
classification:      OPERATIONAL
blast_domain:        OPS (DevOps domain only)
endpoint:            POST /v1/forgejo/ci-events
transport:           Internal cluster only (ClusterIP)
signature_method:    HMAC-SHA256
signature_header:    X-Gitea-Signature
secret_location:     vault://secret/ops/forgejo/webhook_secret
ip_allowlist:        forgejo.ops.svc.cluster.local only
events_handled:
  - push                          → ci.build.triggered (Kafka)
  - pull_request                  → ci.pr.opened (Kafka)
  - create (tag)                  → ci.release.tagged (Kafka)
  - workflow_job (completed)      → ci.job.completed (Kafka)
  - workflow_run (failed)         → ops.alert.ci_failure (Kafka) → push notification to ops team
forward_to_service:  admin-governance-service (for deployment audit log)
```

#### WEBHOOK_IN_005: LinkedIn OAuth Callback (Integration Hub)
```yaml
provider_id:         linkedin-oauth-v1
provider_name:       LinkedIn
classification:      STANDARD
blast_domain:        ECOSKILLER_CORE (integration domain)
endpoint:            GET /v1/linkedin/oauth/callback  (OAuth2 redirect_uri)
signature_method:    OAuth2 state parameter (CSRF token — HMAC-SHA256 signed)
secret_location:     vault://secret/integration/linkedin/oauth_state_secret
ip_allowlist:        NONE (OAuth callback from user browser — IP allowlist not applicable)
oauth_state_ttl:     600 seconds (10 minutes)
data_scope_allowed:  profile · email · openid
data_scope_forbidden: connections · messages · w_member_social (write scopes)
on_success:
  - emit: user.linkedin.linked (Kafka)
  - store: LinkedIn profile_id only (not full profile dump)
  - no_PII_logging: TRUE
student_protection:  Student-facing — data minimization enforced; no third-party direct interaction
```

#### WEBHOOK_IN_006: GitHub OAuth Callback (Integration Hub)
```yaml
provider_id:         github-oauth-v1
provider_name:       GitHub
classification:      STANDARD
blast_domain:        ECOSKILLER_CORE
endpoint:            GET /v1/github/oauth/callback
signature_method:    OAuth2 state parameter (CSRF token)
secret_location:     vault://secret/integration/github/oauth_state_secret
oauth_state_ttl:     600 seconds
data_scope_allowed:  read:user · user:email
data_scope_forbidden: repo · write:* · admin:* · delete_repo
on_success:
  - emit: user.github.linked (Kafka)
  - store: GitHub username + public profile URL only
  - no_PII_logging: TRUE
```

#### WEBHOOK_IN_007: Revenue Report Ingestion (Royalty Domain)
```yaml
provider_id:         revenue-ingestion-v1
provider_name:       Business Partner Revenue API
classification:      CRITICAL (financial / legal)
blast_domain:        ROYALTY (royalty domain only)
endpoint:            POST /v1/royalty/revenue-report
signature_method:    HMAC-SHA256
signature_header:    X-Ecoskiller-Signature
secret_location:     vault://secret/royalty/revenue_ingestion/{business_id}/webhook_secret
  # Per-business secret — rotated annually
ip_allowlist:        Per-business allowlist (configured at contract signing)
replay_window_seconds: 300
payload_schema:
  contract_id:       UUID (required)
  reporting_period:  string (YYYY-QQ format, e.g. "2025-Q1")
  gross_revenue:     decimal (required, ≥ 0)
  currency:          string (ISO 4217)
  product_lines:     string[] (required)
  submission_hash:   string (SHA-256 of payload — for tamper detection)
on_receipt:
  - signature_verify → REJECT if invalid
  - idempotency_check → REJECT if duplicate submission_hash
  - emit: revenue.report.ingested (Kafka)
  - trigger: royalty-anomaly-detector-v1 (async via Kafka)
  - audit_log: MANDATORY (15-year retention, FINANCIAL tag)
```

### 4.3 Inbound Webhook Security Enforcement
```
SECURITY_RULE_IW_01: EVERY inbound webhook endpoint MUST verify HMAC signature before processing.
                     Verification failure → HTTP 400 → LOG → ALERT → no payload forwarded.

SECURITY_RULE_IW_02: Replay attack protection MANDATORY.
                     Mechanism: timestamp in payload + Redis nonce store (TTL = replay_window_seconds).
                     Requests outside window OR with reused nonce → REJECT.

SECURITY_RULE_IW_03: IP allowlist ENFORCED at NGINX Ingress level (not application level alone).
                     Unknown IP → REJECT at ingress → no payload reaches webhook-receiver pod.
                     Exception: OAuth callbacks (browser-originated) — CSRF state token replaces IP check.

SECURITY_RULE_IW_04: Payload size limit ENFORCED at ingress (1 MB).
                     Oversized payload → REJECT with HTTP 413 before reaching application.

SECURITY_RULE_IW_05: ALL inbound webhooks return HTTP 200 immediately.
                     Processing is ALWAYS async via Kafka.
                     Synchronous processing in webhook endpoint FORBIDDEN (prevents timeout retries).

SECURITY_RULE_IW_06: PII stripping MANDATORY before publishing to Kafka.
                     Payment card data, raw account numbers, national IDs → STRIP or HASH.

SECURITY_RULE_IW_07: Blast domain isolation ENFORCED.
                     A payment webhook CANNOT trigger identity events.
                     A CI event CANNOT trigger billing events.
                     Cross-domain webhook routing FORBIDDEN.

VIOLATION OF ANY SECURITY_RULE_IW_* → HARD_STOP → REPORT INBOUND_WEBHOOK_SECURITY_VIOLATION:{rule_id}
```

---

## SECTION 5 — OUTBOUND WEBHOOK CONTRACT REGISTRY

### 5.1 Outbound Webhook Standards
```
DISPATCHER_SERVICE   = integration-hub (dedicated microservice, core namespace)
DELIVERY_GUARANTEE   = AT_LEAST_ONCE (idempotency_key per dispatch)
RETRY_POLICY         = 5 attempts · exponential backoff (30s, 2m, 10m, 1h, 6h)
SIGNATURE_METHOD     = HMAC-SHA256 per recipient secret
SIGNATURE_HEADER     = X-Ecoskiller-Signature
SECRET_STORAGE       = Vault (per-recipient secret, rotated annually)
TIMEOUT_PER_ATTEMPT  = 10 seconds
TLS_MINIMUM          = TLS 1.2 — recipient must support HTTPS
PLAINTEXT_TARGET     = FORBIDDEN — no HTTP targets
PAYLOAD_MAX_SIZE     = 512 KB
AUDIT_LOG            = Every outbound dispatch attempt + result → audit log
DEAD_LETTER          = After 5 failures → dead_letter_queue (Kafka topic: webhook.outbound.dead)
                       → alert to platform admin
REGISTRY_LOCATION    = /contracts/webhooks/outbound/{webhook_id}/v{version}/webhook.yaml
```

### 5.2 Registered Outbound Webhook Targets

#### WEBHOOK_OUT_001: SEO Frontend Revalidation (Next.js ISR)
```yaml
webhook_id:          seo-revalidation-v1
target:              Next.js SEO frontend (self-hosted)
trigger_events:
  - job.listing.published
  - job.listing.closed
  - user.public_profile.updated
  - idea.approved
  - certificate.generated
  - outcome.story.published
endpoint_pattern:    https://web.ecoskiller.com/api/revalidate
method:              POST
auth:                Bearer token (Vault-issued, 24h TTL)
signature_method:    HMAC-SHA256
payload:
  event_type:        string
  entity_type:       string
  entity_id:         UUID
  paths_to_revalidate: string[]
purpose:             Trigger Next.js ISR revalidation for SEO pages on content change
sla_importance:      HIGH (stale SEO pages affect search ranking)
retry_policy:        5 attempts · exponential backoff
fallback:            scheduled-revalidation cron (Apache Airflow, every 6 hours)
```

#### WEBHOOK_OUT_002: HRIS / LMS Integration (Future — Declared Now)
```yaml
webhook_id:          hris-lms-v1
target:              External HRIS or LMS system (tenant-configured)
trigger_events:
  - job.application.hired
  - certificate.generated
  - dojo.belt.awarded
  - interview.completed
endpoint_pattern:    {tenant_configured_url}  (HTTPS only — validated at registration)
auth:                OAuth2 client credentials OR HMAC-SHA256 (tenant choice)
signature_method:    HMAC-SHA256
secret_storage:      vault://secret/integration/hris/{tenant_id}/webhook_secret
blast_domain:        ECOSKILLER_CORE (per-tenant, isolated)
data_minimization:   ENFORCED — only send fields declared in tenant's data processing agreement
student_protection:  Student data sent to HRIS ONLY with tenant admin consent + student consent
pii_rules:
  - Aadhaar number → NEVER sent
  - Phone number → NEVER sent
  - Full address → NEVER sent
  - Name + email + certificate_id → ALLOWED (with consent)
activation:          HUMAN DECISION REQUIRED — tenant admin enables via platform admin panel
status:              DECLARED · NOT_YET_ACTIVE
```

#### WEBHOOK_OUT_003: Webhook Framework (Generic Tenant Webhooks)
```yaml
webhook_id:          tenant-webhook-framework-v1
purpose:             Allow tenants (enterprises, institutes) to subscribe to platform events
                     and receive webhooks at their own endpoints
tenant_registration:
  - tenant admin registers target URL (HTTPS only — validated)
  - platform verifies URL (sends challenge-response verification)
  - per-tenant HMAC secret generated (Vault) and shown once to tenant admin
  - tenant configures events to subscribe to (from allowed_events list)
allowed_events:
  - job.application.submitted
  - job.application.hired
  - interview.completed
  - gd.score.finalized
  - certificate.generated
  - billing.invoice.generated
  - billing.payment.received
forbidden_events:  # Cannot be sent to external tenants
  - user.password.changed
  - user.mfa.*
  - royalty.*  (royalty events never sent to external tenants)
  - admin.*
  - compliance.*
payload_data_minimization: ENFORCED — no PII beyond agreed fields in tenant DPA
rate_limit_per_tenant:     1000 outbound webhooks/hour
dead_letter_notification:  Email to tenant admin after 5 consecutive delivery failures
url_validation_rules:
  - HTTPS required
  - No private/internal IP ranges (RFC 1918 blocked: 10.x, 172.16-31.x, 192.168.x)
  - No localhost
  - No ecoskiller.* domains (SSRF prevention)
ssrf_prevention:    MANDATORY — validate target URL against blocklist BEFORE every dispatch
```

### 5.3 Outbound Webhook Security Rules
```
SECURITY_RULE_OW_01: EVERY outbound webhook MUST include HMAC-SHA256 signature in X-Ecoskiller-Signature header.

SECURITY_RULE_OW_02: Target URL validation MANDATORY before dispatch.
                     RFC 1918 private IP ranges BLOCKED (SSRF prevention).
                     Loopback (127.x, ::1) BLOCKED.
                     Ecoskiller internal domains BLOCKED as outbound targets.

SECURITY_RULE_OW_03: TLS certificate of target MUST be valid.
                     Self-signed or expired certificates → REJECT dispatch → LOG → ALERT tenant.

SECURITY_RULE_OW_04: Payload size limit (512 KB) ENFORCED before dispatch.
                     Oversized payload → truncate non-essential fields → LOG warning.

SECURITY_RULE_OW_05: PII data minimization ENFORCED per tenant DPA.
                     Aadhaar, phone numbers, financial account data → NEVER in outbound payload.

SECURITY_RULE_OW_06: Dead letter handling MANDATORY.
                     Undeliverable webhooks after 5 retries → dead_letter Kafka topic + admin alert.

SECURITY_RULE_OW_07: Outbound webhook dispatch NEVER triggered directly by user input.
                     Only triggered by Kafka events from validated internal services.
                     Direct user-to-webhook path FORBIDDEN.

VIOLATION OF ANY SECURITY_RULE_OW_* → HARD_STOP → REPORT OUTBOUND_WEBHOOK_SECURITY_VIOLATION:{rule_id}
```

---

## SECTION 6 — NOTIFICATION ORCHESTRATION ENGINE CONTRACT

### 6.1 Notification Service Architecture
```
SERVICE_NAME         = notification-service
NAMESPACE            = core
K8S_PORT             = 8010
TRIGGER_SOURCE       = Kafka consumer (subscribes to notification trigger topics)
DISPATCH_MODE        = ASYNC ONLY — no synchronous notification calls from other services
TEMPLATE_ENGINE      = Server-side Jinja2/Handlebars
TEMPLATE_STORAGE     = /contracts/notifications/templates/ (Forgejo repo)
PREFERENCE_STORE     = PostgreSQL (notification schema · user_notification_preferences table)
DELIVERY_LOG         = PostgreSQL (notification schema · notification_delivery_log table)
IDEMPOTENCY          = notification_id (UUID) per dispatch attempt — stored in Redis 24h
```

### 6.2 Notification Trigger → Channel Routing Rules

```
RULE_NO_01: CHANNEL_SELECTION = user preference (if set) SUBJECT TO minimum channel requirements
RULE_NO_02: MINIMUM_CHANNEL_OVERRIDE:
            Security alerts   → ALWAYS email + push (preference cannot disable)
            Payment events    → ALWAYS email (preference cannot disable)
            Consent requests  → ALWAYS email + SMS (preference cannot disable)
            Legal contracts   → ALWAYS email (preference cannot disable)
            OTP               → Governed by OTP channel matrix (Section 3.3)
RULE_NO_03: QUIET_HOURS = user-configured (default: no push 22:00–07:00 local time)
            Exception: URGENT priority bypasses quiet hours
RULE_NO_04: UNSUBSCRIBE = allowed for marketing/digest only
            Transactional, security, legal, payment notifications → UNSUBSCRIBE FORBIDDEN
RULE_NO_05: CHANNEL_FALLBACK_CHAIN:
            IF primary channel fails after retry_policy → activate fallback channel
            Fallback activation is AUTOMATIC (no human approval needed for notification fallback)
RULE_NO_06: BULK_NOTIFICATION (institute admin, society batch):
            Max 1000 recipients per batch job
            Dispatched via Apache Airflow scheduled workflow
            Rate limited: 100 SMS/min · 500 emails/min · 1000 push/min
```

### 6.3 Notification Event → Channel Map (Master)

| Kafka Event | Email | SMS | Push | In-App | Priority | Override |
|---|---|---|---|---|---|---|
| user.account.created | ✅ | ❌ | ❌ | ✅ | DEFAULT | — |
| user.phone.verification_requested | ❌ | ✅ | ❌ | ❌ | HIGH | FORCED |
| user.password.reset_requested | ✅ | FALLBACK | ❌ | ❌ | HIGH | FORCED |
| parent.consent.otp_requested | ✅ | ✅ | ❌ | ❌ | URGENT | FORCED |
| gd.session.started | ❌ | ❌ | ✅ | ✅ | URGENT | FORCED |
| gd.turn.granted | ❌ | ❌ | ❌ | ✅ | URGENT | — |
| gd.score.finalized | ✅ | ❌ | ✅ | ✅ | HIGH | — |
| dojo.belt.awarded | ✅ | ❌ | ✅ | ✅ | HIGH | — |
| interview.scheduled | ✅ | ✅ | ✅ | ✅ | HIGH | — |
| job.application.hired | ✅ | ✅ | ✅ | ✅ | URGENT | — |
| job.application.rejected | ✅ | ❌ | ✅ | ✅ | DEFAULT | — |
| billing.payment.received | ✅ | ❌ | ✅ | ✅ | HIGH | FORCED |
| billing.payment.failed | ✅ | ✅ | ✅ | ✅ | URGENT | FORCED |
| billing.invoice.generated | ✅ | ❌ | ❌ | ✅ | DEFAULT | — |
| royalty.payment.processed | ✅ | ✅ | ✅ | ✅ | HIGH | FORCED |
| royalty.anomaly.detected | ✅ | ❌ | ✅ | ✅ | URGENT | FORCED (admin) |
| licensing.contract.created | ✅ | ❌ | ❌ | ✅ | HIGH | FORCED |
| idea.theft.flagged | ✅ | ❌ | ✅ | ✅ | URGENT | FORCED |
| certificate.generated | ✅ | ❌ | ✅ | ✅ | HIGH | — |
| commission.calculated | ❌ | ✅ | ✅ | ✅ | DEFAULT | — |
| payout.processed | ✅ | ✅ | ✅ | ✅ | HIGH | FORCED |
| compliance.incident.logged | ✅ | ✅ | ✅ | ❌ | URGENT | FORCED (admin) |
| user.account.suspended | ✅ | ✅ | ✅ | ❌ | URGENT | FORCED |
| ci.job.completed (failure) | ✅ | ❌ | ✅ | ❌ | HIGH | FORCED (ops) |

---

## SECTION 7 — RATE LIMITING & ABUSE PREVENTION CONTRACT

### 7.1 Per-Channel Rate Limits
```
ENFORCEMENT_LAYER_1  = Envoy (per-IP, per-user, per-tenant)
ENFORCEMENT_LAYER_2  = Kong API Gateway (per-API-key, per-route)
ENFORCEMENT_LAYER_3  = Redis token bucket (application-level, per channel per user)
ALL_THREE_LAYERS     = MANDATORY — single-layer rate limiting INSUFFICIENT

RATE_LIMIT_TABLE:
Channel          | Per User/Hour | Per User/Day  | Per IP/Hour  | Per Tenant/Hour
SMS (OTP)        | 10            | 50            | 20           | 500
SMS (General)    | 5             | 20            | 10           | 200
EMAIL            | 20            | 100           | 50           | 1000
PUSH             | 50            | 200           | 100          | 5000
INBOUND WEBHOOK  | N/A           | N/A           | 100          | 1000
OUTBOUND WEBHOOK | N/A           | N/A           | N/A          | 1000

RATE_LIMIT_EXCEEDED → HTTP 429 (Too Many Requests) → Retry-After header set
REPEATED_VIOLATION  → automatic temporary block (1 hour) → audit log → admin alert
```

### 7.2 Abuse Prevention Rules
```
ABUSE_RULE_01: OTP endpoint — CAPTCHA required after 2nd request within 10 minutes (same IP)
ABUSE_RULE_02: SMS endpoint — phone number must be verified (in user profile) before non-OTP SMS
ABUSE_RULE_03: Bulk notification endpoint — PLATFORM_ADMIN or TENANT_ADMIN role required
ABUSE_RULE_04: Webhook registration — URL ownership verification (challenge-response) MANDATORY
ABUSE_RULE_05: Outbound webhook — SSRF blocklist enforced on every dispatch (not just registration)
ABUSE_RULE_06: Email unsubscribe — one-click unsubscribe link MANDATORY on all non-transactional email
ABUSE_RULE_07: Phone spam detection — if same phone receives > 5 OTPs in 1 hour from different users
               → flag as shared/suspicious number → require manual review before next OTP
ABUSE_RULE_08: Webhook flood — if inbound provider sends > 100 events in 1 second
               → circuit breaker opens → events queued in Kafka backpressure → alert ops
```

---

## SECTION 8 — PROVIDER BLAST DOMAIN ISOLATION

```
LAW_SOURCE           = R56.1 — All third-party integrations MUST be isolated per blast domain.
                       A third-party enabled in one blast domain must never be implicitly available in another.

BLAST_DOMAIN_MAP:
  ECOSKILLER_CORE:
    providers: [Stripe, Razorpay, LinkedIn, GitHub, ntfy, Postfix, Jasmin]
    data_access: [job, billing, user, intelligence, innovation]

  DOJO_SAAS:
    providers: [Jitsi, LiveKit, coturn]
    data_access: [dojo, gd_sessions, interview]
    NOTE: Jitsi/LiveKit NEVER receive user PII — session tokens only

  SOCIETY_DOMAIN:
    providers: [Jasmin SMS (society-specific sender ID), Postfix (society@ address)]
    data_access: [society, workshop, commission, compliance]
    NOTE: Society domain has SEPARATE sender identities from core domain

  OPS_DOMAIN:
    providers: [Forgejo, Prometheus, Grafana, Wazuh, Vault]
    data_access: [infrastructure logs, CI events, security events]

CROSS_DOMAIN_PROVIDER_BLEED = FORBIDDEN
EXAMPLE_VIOLATION:
  Using Stripe webhook secret in society domain → FORBIDDEN
  Using society SMS sender ID for billing alerts → FORBIDDEN
  Keycloak events forwarded to HRIS without tenant consent → FORBIDDEN

VIOLATION → HARD_STOP → REPORT BLAST_DOMAIN_ISOLATION_VIOLATION:{provider}:{domain}
```

---

## SECTION 9 — TRANSPORT SECURITY CONTRACT

```
LAW_SOURCE           = TRANS-* series (ECOSKILLER MASTER PROMPT v12.0)

RULE_TS_01: ALL external communication (inbound + outbound) MUST use TLS 1.2 minimum.
            TLS 1.3 preferred for new implementations.
            TLS 1.0, TLS 1.1, SSL (any version) → FORBIDDEN → HARD_STOP.

RULE_TS_02: SMTP without TLS (port 25 plaintext to external) → FORBIDDEN.
            Postfix MUST enforce STARTTLS for all external delivery.
            Internal relay (port 25 within cluster) allowed only over private network.

RULE_TS_03: WebSocket connections MUST use WSS (WebSocket Secure).
            WS (plain WebSocket) → FORBIDDEN for in-app notification channel.

RULE_TS_04: Webhook endpoints (inbound) MUST be HTTPS.
            HTTP webhook endpoints → REJECTED at NGINX ingress → not forwarded to application.

RULE_TS_05: Webhook dispatch targets (outbound) MUST be HTTPS with valid TLS certificate.
            Invalid certificate → REJECT dispatch → LOG → ALERT tenant admin.

RULE_TS_06: PII in transit MUST NOT be logged at proxy/ingress level.
            NGINX access logs for webhook endpoints → LOG request metadata only (timestamp, IP, status).
            Payload logging at ingress level → FORBIDDEN.

RULE_TS_07: DKIM, SPF, DMARC MANDATORY for all outbound email.
            Email without DKIM signature → BLOCKED by Postfix before delivery.

RULE_TS_08: mTLS MANDATORY for service-to-service calls involving:
            notification-service → channel providers (internal)
            webhook-receiver → Kafka broker
            integration-hub → external targets (mTLS where supported; HTTPS + signature otherwise)

RULE_TS_09: Certificate management via cert-manager (self-hosted).
            No self-signed certificates in staging or production.
            No wildcard certificates crossing environments (dev cert ≠ prod cert).

RULE_TS_10: Environment isolation ENFORCED.
            Dev channel credentials ≠ Staging ≠ Production.
            No production SMS/email channel accessible from dev or staging environments.
            Each environment uses separate Vault path for credentials.

ALL TRANSPORT RULE VIOLATIONS → HARD_STOP → REPORT TRANSPORT_SECURITY_VIOLATION:{rule_id}
```

---

## SECTION 10 — DATA MINIMIZATION & PRIVACY CONTRACT

```
LAW_SOURCE           = GDPR compliance + Indian IT Act + CHILD_PROTECTION law

RULE_DM_01: SMS bodies MUST NOT contain: Aadhaar number, PAN, bank account, full address, passwords.
            ALLOWED: OTP, notification text, masked amounts (₹**,***), first name only.

RULE_DM_02: Email bodies MUST NOT contain raw financial account numbers.
            Invoice PDFs (attached via MinIO presigned URL) are acceptable.
            Royalty amounts in email: ALLOWED (for recipient's own data only).

RULE_DM_03: Push notification payloads MUST NOT contain PII.
            Push body: summary text only. Sensitive details: accessible after user opens app.

RULE_DM_04: Webhook payloads sent to external tenants MUST be stripped of:
            Phone numbers, Aadhaar, national IDs, financial account numbers.
            Only fields declared in tenant's Data Processing Agreement (DPA) are allowed.

RULE_DM_05: Notification logs (delivery_log table) store:
            ALLOWED: channel, template_id, user_id, tenant_id, timestamp, delivery_status.
            FORBIDDEN: message body, OTP value, attachment content.

RULE_DM_06: OTP values MUST NEVER appear in:
            Application logs, access logs, Kafka message bodies, audit logs, notification_log table.
            OTP is generated → hashed → stored in Redis → dispatched to channel → cleared on use/expiry.

RULE_DM_07: Parent/child notification data:
            PARENT receives ONLY: child's activity summary, consent requests, royalty payments.
            PARENT does NOT receive: child's full intelligence vector, full GD scores, raw session data.
            Child's detailed data visible to parent: ONLY in parent-specific screens (governed by TIER_8 matrix).

RULE_DM_08: Minor user (is_minor = TRUE) phone/email data:
            STORED: YES (needed for OTP, consent workflows).
            SHARED_EXTERNALLY: FORBIDDEN without explicit parent consent.
            INCLUDED_IN_HRIS_WEBHOOK: FORBIDDEN.

ALL DATA_MINIMIZATION VIOLATIONS → HARD_STOP → REPORT DATA_MINIMIZATION_VIOLATION:{rule_id}
```

---

## SECTION 11 — FAILURE HANDLING & DEAD LETTER CONTRACT

```
FAILURE_REGISTRY_LOCATION = /contracts/channels/failure-handling/v1/failure.yaml

FAILURE_SCENARIO_01: SMS delivery failed (all 3 retries exhausted)
  ACTION:
    - Log: notification_delivery_log (status = FAILED, failure_reason = SMS_UNDELIVERABLE)
    - Trigger: fallback channel (email-postfix-v1) if event class requires it
    - Emit: notification.sms.delivery_failed (Kafka)
    - Alert: ops team if failure rate > 5% within 1 hour

FAILURE_SCENARIO_02: Email bounced (hard bounce)
  ACTION:
    - Log: user_notification_preferences (email_status = UNDELIVERABLE)
    - Suppress: all future email to that address (bounce suppression list)
    - Emit: notification.email.bounced (Kafka)
    - Trigger: fallback to SMS if high-priority event

FAILURE_SCENARIO_03: Push notification undeliverable (user not subscribed to ntfy topic)
  ACTION:
    - Log: delivery failed
    - Fallback: in-app WebSocket if user has active session
    - Fallback: email if no active session + event priority HIGH+

FAILURE_SCENARIO_04: Inbound webhook signature verification failed
  ACTION:
    - Return HTTP 400
    - Log: security_event (type = WEBHOOK_SIGNATURE_FAILURE, provider, ip, timestamp)
    - Emit: security.alert (Kafka) → ops push notification + email to platform admin
    - DO NOT forward payload to any service
    - If > 3 failures in 5 minutes from same IP: trigger IP block at ingress

FAILURE_SCENARIO_05: Outbound webhook dead letter (5 retries exhausted)
  ACTION:
    - Publish to: webhook.outbound.dead (Kafka topic, 30-day retention)
    - Email: tenant admin (if tenant webhook) or ops team (if internal webhook)
    - Log: webhook_dispatch_log (status = DEAD_LETTERED)
    - Manual review required before re-activation

FAILURE_SCENARIO_06: Jasmin SMS Gateway pod crash
  ACTION:
    - Kubernetes: restart policy = Always (automatic pod restart)
    - Circuit breaker: opens on notification-service side after 5 consecutive SMS failures
    - Fallback: route all HIGH+ priority notifications to email during circuit-open
    - Alert: ops push notification + Prometheus alert rule (jasmin_pod_restart_total > 3 in 10m)
    - PagerDuty-equivalent: ops on-call alert (via ntfy URGENT to ops channel)

FAILURE_SCENARIO_07: Postfix mail queue backlog > 1000 messages
  ACTION:
    - Prometheus alert: postfix_queue_size > 1000
    - Grafana dashboard: mail delivery latency spike visible
    - Auto-scale: not applicable (Postfix is stateful) → ops manual intervention
    - Alert: ops team via push + in-app

ALL FAILURES LOGGED → ALL CRITICAL FAILURES ALERTED → ALL DEAD LETTERS RETAINED 30 DAYS
```

---

## SECTION 12 — KAFKA EVENT SCHEMA (CHANNEL EVENTS)

### 12.1 Notification Domain Kafka Topics
```
notification.otp.requested            → {user_id, otp_type, channel, tenant_id, request_id, timestamp}
notification.otp.validated            → {user_id, otp_type, success, attempts_used, timestamp}
notification.otp.expired              → {user_id, otp_type, timestamp}
notification.sms.dispatched           → {notification_id, user_id, template_id, channel, status, timestamp}
notification.sms.delivery_failed      → {notification_id, user_id, failure_reason, attempt_count, timestamp}
notification.email.dispatched         → {notification_id, user_id, template_id, channel, status, timestamp}
notification.email.bounced            → {notification_id, user_id, bounce_type, timestamp}
notification.push.dispatched          → {notification_id, user_id, topic, priority, status, timestamp}
notification.preference.updated       → {user_id, tenant_id, preferences_snapshot, timestamp}
webhook.inbound.received              → {provider_id, event_type, idempotency_key, payload_hash, timestamp}
webhook.inbound.processed             → {provider_id, event_type, downstream_event, timestamp}
webhook.inbound.rejected              → {provider_id, reason, ip_address, timestamp}
webhook.outbound.dispatched           → {webhook_id, target_url_hash, event_type, status, timestamp}
webhook.outbound.failed               → {webhook_id, attempt_number, failure_reason, timestamp}
webhook.outbound.dead                 → {webhook_id, all_attempts[], final_failure_reason, timestamp}
security.webhook.signature_failure    → {provider_id, ip_address, endpoint, timestamp}
```

---

## SECTION 13 — CI VALIDATION GATE

```
GATE_SIGNAL_PRODUCED    = external_channel_ready
GATE_CONSUMED_BY        = Notification Service · Auth Service (OTP) · Billing Service
                          (payment webhook) · Integration Hub · Any service dispatching
                          to or receiving from external channels

CI_VALIDATOR_SCRIPT     = /ci/validators/channel_webhook_gate_validator.sh
TRIGGER                 = Every push to test, staging, production branches

VALIDATION_STEPS:
  1. Parse all SECTION 2 channel contracts — verify all required fields present
  2. Verify Jasmin SMS pod health endpoint reachable (staging/prod only)
  3. Verify Postfix SMTP relay health (staging/prod only)
  4. Verify ntfy server health endpoint reachable (staging/prod only)
  5. Parse SECTION 3 OTP catalog — verify all OTP types have Redis key patterns defined
  6. Parse SECTION 4 inbound webhook registry — verify ALL providers have:
       - Signature method declared
       - Secret location in Vault (not hardcoded)
       - IP allowlist configured (or CSRF state for OAuth)
       - Blast domain declared
  7. Parse SECTION 5 outbound webhook registry — verify SSRF blocklist present
  8. Verify SECTION 7 rate limit rules applied at Envoy + Kong config
  9. Verify SECTION 9 transport rules — TLS config present for all channel services
  10. Verify no hardcoded credentials in any channel service codebase (secrets scanner)

ON_FAILURE: CI HARD_STOP → block merge → post failure report to ops Slack channel
ON_SUCCESS: PRODUCES gate artifact → channel_webhook_gate_check.json (all steps: PASSED)
            PRODUCES gate signal: external_channel_ready
```

---

## SECTION 14 — TECHNOLOGY BINDINGS (NON-NEGOTIABLE)

```
SMS_PROVIDER             = Jasmin SMS Gateway (self-hosted ONLY)
EMAIL_PROVIDER           = Postfix + Docker Mail Server (self-hosted ONLY)
PUSH_PROVIDER            = ntfy (self-hosted ONLY)
WEBHOOK_RECEIVER         = Custom microservice (self-hosted, ingress namespace)
WEBHOOK_DISPATCHER       = Integration Hub microservice (self-hosted, core namespace)

FORBIDDEN PROVIDERS:
  - Firebase Cloud Messaging (FCM) as primary push → FORBIDDEN (use ntfy)
  - Twilio SMS → FORBIDDEN (use Jasmin)
  - SendGrid / Mailchimp → FORBIDDEN (use Postfix)
  - AWS SNS → FORBIDDEN (paid managed service)
  - Vonage / Nexmo → FORBIDDEN (paid managed service)
  - Zapier / Make → FORBIDDEN (external SaaS automation)
  - Any paid webhook relay service → FORBIDDEN

ALLOWED EXCEPTIONS:
  - ntfy mobile clients MAY use FCM/APNs as delivery mechanism for mobile push
    (ntfy server proxies through — no direct Firebase SDK in platform code)
  - Stripe / Razorpay as PAYMENT PROVIDERS are allowed (they are the payment source,
    not a notification/channel infrastructure provider)

VIOLATION → HARD_STOP → REPORT FORBIDDEN_CHANNEL_PROVIDER_DETECTED:{provider}
```

---

## SECTION 15 — ANTIGRAVITY EXECUTION COMMANDS

### 15.1 How to Use This Agent
```
STEP_1: Paste this document once → locks PHONE_EXTERNAL_WEBHOOK_AGENT in Antigravity context
STEP_2: Run targeted channel or webhook generation commands (see 15.2)
STEP_3: CI validates all channel contracts and webhook security configs before service build
STEP_4: Only after external_channel_ready gate is PASSED → Notification Service and
        Integration Hub builds may begin
```

### 15.2 Execution Commands
```
# Generate channel config for a specific channel
GENERATE_CHANNEL_CONFIG
  CHANNEL     = sms-jasmin-v1
  ENVIRONMENT = staging
  OUTPUT      = /contracts/channels/sms/v1/channel.yaml

# Register an inbound webhook provider
REGISTER_INBOUND_WEBHOOK
  PROVIDER    = stripe-v1
  BLAST_DOMAIN = ECOSKILLER_CORE
  FILL_TEMPLATE = SECTION_4_INBOUND_WEBHOOK

# Register an outbound webhook target
REGISTER_OUTBOUND_WEBHOOK
  WEBHOOK_ID  = seo-revalidation-v1
  TRIGGER_EVENTS = [job.listing.published, idea.approved]
  FILL_TEMPLATE = SECTION_5_OUTBOUND_WEBHOOK

# Generate OTP service config
GENERATE_OTP_CONFIG
  OTP_TYPE    = phone_verification
  ROLE        = STUDENT
  CHANNEL     = sms-jasmin-v1
  FILL_TEMPLATE = SECTION_3_OTP_TYPE

# Validate all channel gates
VALIDATE_CHANNEL_GATES
  SCOPE       = ALL_CHANNELS
  OUTPUT      = channel_webhook_gate_check.json
```

---

## SECTION 16 — FINAL SEAL

```
AGENT_STATUS             = COMPLETE · SEALED · LOCKED
CHANGE_POLICY            = APPEND_ONLY VIA VERSION BUMP
ANTIGRAVITY_CONFUSION    = IMPOSSIBLE
ASSUMPTION_PERMITTED     = NONE
IMPLICIT_BEHAVIOR        = NONE
QUALITY_SCORE            = 10 / 10

GATE PRODUCED BY THIS AGENT:
  ✔ external_channel_ready

SERVICES UNLOCKED BY THIS GATE:
  ✔ notification-service (build permitted)
  ✔ auth-service OTP subsystem (build permitted)
  ✔ billing-service payment webhook receiver (build permitted)
  ✔ integration-hub (build permitted)
  ✔ webhook-receiver microservice (build permitted)

SERVICES BLOCKED UNTIL GATE PASSES:
  ✗ Any service dispatching SMS, email, or push
  ✗ Any service receiving inbound webhooks
  ✗ Any service sending outbound webhooks
  ✗ Any OTP flow in auth-service

⚠️ Antigravity MUST NOT:
   - Generate hardcoded SMS/email credentials in any service
   - Use Firebase, Twilio, SendGrid, or any paid channel provider
   - Skip HMAC signature verification on ANY webhook (inbound or outbound)
   - Allow plaintext HTTP for any external channel
   - Allow OTP values to appear in logs or Kafka payloads
   - Route channels across blast domain boundaries
   - Bypass rate limits or abuse prevention rules

⚠️ This agent inherits and enforces:
   - TRANS-* Transport Security Laws from ECOSKILLER MASTER PROMPT v12.0
   - API-L Third-Party & Webhook Security Law
   - R56.1 Blast Domain Isolation Law
   - R56.2 Student-Safety-Aware Integration Law
   - MFA-C OTP channel restrictions per user tier
   - M5 AI Reality Law (no AI autonomously dispatches external communications)

DOCUMENT_END: PHONE_EXTERNAL_WEBHOOK_AGENT v1.0.0
STATUS: FINAL · LOCKED · GOVERNED · DETERMINISTIC
```
