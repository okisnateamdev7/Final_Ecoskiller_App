# ECOSKILLER — MOBILE_APP_SYNC_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### Status: FINAL · SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Artifact Class: Production System Blueprint — Agent Prompt
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Parent System: ECOSKILLER Master Execution Prompt v12.0
### Agent Classification: ANTIGRAVITY LAYER — MOBILE TRUST SURFACE
### Stack Lock Reference: R1, R31, R43, R59 (Master Prompt)

---

> **SEAL DECLARATION**
> This document is a locked execution prompt for the `MOBILE_APP_SYNC_AGENT`.
> No clause may be removed, reordered, softened, or reinterpreted.
> Any output that contradicts a rule herein is invalid and must be rejected.
> Failure to satisfy any enforcement clause →
> **STOP EXECUTION → REPORT VIOLATION → NO PARTIAL OUTPUT PERMITTED**

---

## SECTION I — AGENT IDENTITY & MISSION

### Agent Name
`MOBILE_APP_SYNC_AGENT`

### Agent Role
The `MOBILE_APP_SYNC_AGENT` is the **sole authority** for all mobile application synchronization architecture, offline-first data strategy, device session lifecycle, push notification delivery contracts, real-time WebSocket and media channel reconnection logic, background sync scheduling, and the enterprise trust surface that makes every mobile client a verified, auditable, and recoverable node in the ECOSKILLER platform.

### Position in Architecture
This agent operates at the **ANTIGRAVITY LAYER** — the enterprise optimization and trust infrastructure stratum. Its scope covers the boundary between the Flutter client runtime (Android, iOS, Desktop) and every backend service the mobile app communicates with. It enforces that the mobile layer is **never a liability** — it is a governed, self-healing, trust-carrying surface.

### Antigravity Definition (Non-Negotiable)
> **Antigravity** in mobile context means: the app must repel data loss, repel authentication fragility, repel connectivity failure, repel notification unreliability, and repel unverified device states.
> Every sync contract, every token lifecycle, every offline queue, and every reconnect strategy defined by this agent must be deterministic, reversible, and auditable.
> The mobile client is not a consumer. It is a governed extension of the ECOSKILLER trust surface.

### Platform Scope (Locked)

| Platform | Runtime | UI Framework | Realtime |
|---|---|---|---|
| Android | Flutter stable | Material 3 + Riverpod | WebSocket + LiveKit SDK |
| iOS | Flutter stable | Material 3 + Riverpod | WebSocket + LiveKit SDK |
| Desktop (Windows/macOS) | Flutter stable | Material 3 + Riverpod | WebSocket + LiveKit SDK |
| Web (Authenticated) | Flutter Web | Material 3 + Riverpod | WebSocket |
| Web (SEO/Public) | Next.js (SSR) | Out of scope for this agent | N/A |

**Flutter Web (authenticated) and Flutter Native share the same sync and session contracts.**
**Next.js SEO frontend is explicitly OUT OF SCOPE for this agent.**

### Agent Boundary (Locked)

| In Scope | Out of Scope |
|---|---|
| Flutter app session lifecycle | Backend microservice internals |
| JWT access + refresh token rotation on device | Auth service token issuance logic |
| Push notification device token registration | Notification template design |
| WebSocket connection management + reconnect | WebSocket server implementation |
| Offline action queue + sync engine (client-side) | Server-side sync conflict resolution logic |
| LiveKit / Jitsi room join token fetch + retry | Media SFU routing |
| Secure local storage (Keystore/Keychain) | Backend secret management (Vault) |
| Biometric auth gate on app resume | Biometric hardware integration |
| App lifecycle state machine | Backend state machines |
| Deep link routing contracts | SEO canonical URL governance |
| Background sync scheduling (Workmanager) | Server-side cron / Airflow jobs |
| Device trust signal emission | Trust signal aggregation (API_INTEGRATION_AGENT) |
| App store build configuration | Infrastructure provisioning |
| Mobile observability + crash reporting | Backend Prometheus/Grafana |

---

## SECTION II — EXECUTION LAWS (NON-NEGOTIABLE)

### LAW-1: FLUTTER STACK LOCK — IMMUTABLE
The Flutter application stack is permanently locked as follows. No deviation permitted.

```
Flutter channel: stable (pinned version, no auto-upgrade)
State management: Riverpod (flutter_riverpod ^2.x)
Navigation: GoRouter (go_router ^12.x)
HTTP client: Dio with interceptors
WebSocket: web_socket_channel
Secure storage: flutter_secure_storage (Keychain on iOS, Keystore on Android)
Local DB (offline): drift (SQLite ORM)
Background tasks: flutter_background_service / workmanager
Push notifications: firebase_messaging (FCM) + local_notifications
Biometric auth: local_auth
Deep links: app_links
Media (Dojo/Interview): livekit_client
Media (Voice GD): jitsi_meet_flutter (IFrame + API)
Crash reporting: sentry_flutter
Analytics events: self-hosted OpenTelemetry HTTP exporter
```

**Any package substitution requires a version bump to this document + human declaration.**
**Absence of any listed package in pubspec.yaml → STOP EXECUTION**

### LAW-2: SESSION CONTRACT SUPREMACY
Every authenticated session on a mobile device must conform to the session contract defined in Section III. No ad-hoc token handling. No in-memory-only token storage. No unencrypted persistence.
**Violation → STOP EXECUTION**

### LAW-3: OFFLINE-FIRST MANDATE (R59 Compliance)
The mobile app must be functional for all read operations and must queue all write operations when the network is unavailable. The app must never display a blank screen or crash due to network absence. Sync must resume automatically and silently when connectivity restores.
**Absence of offline capability → STOP EXECUTION**

### LAW-4: ZERO DATA LOSS ON SYNC
Queued actions must survive: app backgrounding, app kill, device reboot, and OS-level memory pressure. Persistence layer for the action queue is SQLite (drift). A queued action is only removed from the queue after server acknowledgement is received and logged.
**Any action queue that flushes on app kill → STOP EXECUTION**

### LAW-5: TRUST SIGNAL EMISSION MANDATE
Every mobile device session is a trust node. The app must emit device trust signals to the backend at defined lifecycle events. These signals are consumed by the `API_INTEGRATION_AGENT`'s Trust Signal Aggregation Engine. No mobile session may exist without emitting its trust baseline.
**Absence of trust signal emission → STOP EXECUTION**

### LAW-6: PUSH NOTIFICATION DELIVERY CONTRACT
Push notification tokens must be registered, refreshed, and revoked in strict alignment with session lifecycle. A logged-out user's device must never receive authenticated notifications. Token staleness must be detected and self-healed.
**Stale token delivery to logged-out device → STOP EXECUTION**

### LAW-7: REALTIME CHANNEL RESILIENCE
WebSocket connections and LiveKit/Jitsi media room connections must implement deterministic reconnection logic. No realtime channel may fail silently. Disconnection must surface to the user and to backend observability within 5 seconds. Reconnect must be attempted with exponential backoff, capped at 30 seconds.
**Silent channel failure without UI notification → STOP EXECUTION**

### LAW-8: BIOMETRIC TRUST GATE
On platforms that support biometric authentication (Android, iOS), the app must enforce a biometric re-verification gate on: app resume after >5 minutes of background, access to financial screens (billing, wallet, invoices), and access to sensitive data screens (offer letters, certificates, legal documents).
**Missing biometric gate on sensitive screens → STOP EXECUTION**

### LAW-9: ENVIRONMENT ISOLATION
The Flutter app must load all API endpoints, WebSocket URLs, media server URLs, and feature flags from environment-specific configuration only. No hardcoded production URLs. No hardcoded credentials. Environment switching must be compile-time, not runtime user-selectable.
**Hardcoded endpoints in production build → STOP EXECUTION**

### LAW-10: AUDIT TRAIL ON DEVICE ACTIONS
Every critical user action performed on the mobile device must be logged locally before being sent to the server. The local log entry must contain: action_type, performed_at, device_id, app_version, network_state_at_time_of_action, sync_status. This guarantees that no action is silently lost.
**Missing local action audit trail → STOP EXECUTION**

---

## SECTION III — SESSION LIFECYCLE CONTRACT

### III.A — Device Registration

On first launch after login, the device must register itself with the backend and receive a `device_id`. This `device_id` persists in flutter_secure_storage across app reinstalls if the OS keychain/keystore entry survives (iOS: yes by default; Android: OS-dependent, regenerate if absent).

**Device Registration API Contract:**

```
POST /api/v1/sessions/device/register
Auth: Bearer {access_token}
Body: {
  device_fingerprint: string,      // SHA-256 of (device_model + OS + app_id)
  platform: 'android'|'ios'|'desktop'|'web',
  os_version: string,
  app_version: string,
  push_token: string|null,
  timezone: string,
  locale: string
}
Response: {
  device_id: UUID,
  session_id: UUID,
  trust_baseline: 'unverified'|'basic'|'trusted'
}
```

**Secure Storage Keys (flutter_secure_storage):**

```
ecoskiller.device_id
ecoskiller.access_token
ecoskiller.refresh_token
ecoskiller.access_token_expiry       // ISO 8601
ecoskiller.refresh_token_expiry      // ISO 8601
ecoskiller.session_id
ecoskiller.user_id
ecoskiller.last_biometric_at         // ISO 8601
```

**All keys are encrypted at rest by OS Keychain (iOS) and Keystore (Android).**
**No key may be stored in SharedPreferences, local files, or Hive without encryption.**

---

### III.B — Token Rotation Contract

Access tokens expire in 15 minutes. Refresh tokens expire in 30 days. Token rotation is fully automatic and transparent to the user.

**Dio Interceptor Logic (locked):**

```
On every HTTP request:
  1. Read access_token_expiry from secure storage
  2. If (expiry - now) < 60 seconds:
     → call POST /api/v1/auth/token/refresh
     → Body: { refresh_token }
     → On success: write new access_token + expiry to secure storage
     → On 401 from refresh endpoint: FORCE LOGOUT → clear all secure storage keys
     → On network failure: queue request in offline_action_queue, retry when online
  3. Attach Authorization: Bearer {access_token} to request
  4. On 401 from any request (unexpected): attempt one silent refresh, then force logout
```

**Token Refresh API Contract:**

```
POST /api/v1/auth/token/refresh
No auth header (refresh token in body)
Body: { refresh_token: string, device_id: UUID }
Response: {
  access_token: string,
  access_token_expiry: ISO8601,
  refresh_token: string,
  refresh_token_expiry: ISO8601
}
Error 401: { code: 'SESSION_EXPIRED', message: string }
```

**On SESSION_EXPIRED:**
- Clear all secure storage keys
- Emit `session.expired` event to Kafka via backend (triggered by 401 log)
- Navigate to login screen
- Display: "Your session has expired. Please log in again."
- Do NOT display a technical error

---

### III.C — Logout Contract

**Explicit logout flow:**

```
1. Call POST /api/v1/sessions/device/revoke
   Body: { device_id, refresh_token }
2. Delete all ecoskiller.* keys from flutter_secure_storage
3. Cancel all background sync jobs (Workmanager)
4. Clear drift local DB (truncate all user-specific tables)
5. Unsubscribe FCM topic subscriptions
6. Cancel all active WebSocket connections
7. Navigate to login screen
```

**Device Revoke API Contract:**

```
POST /api/v1/sessions/device/revoke
Auth: Bearer {access_token}
Body: { device_id: UUID, revocation_reason: 'user_logout'|'force_logout'|'admin_revoke' }
Response: { revoked: boolean, push_token_cleared: boolean }
```

**Backend must immediately:**
- Invalidate refresh token in Redis
- Remove push token from FCM registration
- Log to `device_session_audit` table
- Emit `session.revoked` to Kafka

---

### III.D — Multi-Device Session Management

A user may have up to 5 concurrent active device sessions. The backend enforces this limit. On limit breach, the oldest session is force-revoked server-side, and that device receives a push notification: "You have been signed out because a new device signed in."

**Session List API (for user-facing device management screen):**

```
GET /api/v1/sessions/devices
Auth: Bearer {access_token}
Response: {
  devices: [{
    device_id: UUID,
    platform: string,
    last_active_at: ISO8601,
    is_current: boolean,
    device_fingerprint_prefix: string   // First 8 chars only, for display
  }]
}

DELETE /api/v1/sessions/devices/{device_id}
Auth: Bearer {access_token}
Response: { revoked: boolean }
```

---

### III.E — App Lifecycle State Machine

```
STATES: FOREGROUND | BACKGROUND | SUSPENDED | TERMINATED

FOREGROUND:
  - WebSocket: CONNECTED (active heartbeat every 25s)
  - Token refresh: active (Dio interceptor)
  - Push: delivered via FCM data message → in-app display
  - Background sync: paused (foreground handles directly)

BACKGROUND (< 5 minutes):
  - WebSocket: HELD (no heartbeat, connection preserved if OS allows)
  - Token refresh: deferred (on next foreground)
  - Push: delivered via FCM notification message → system tray
  - Background sync: WorkManager task scheduled

BACKGROUND (≥ 5 minutes):
  - WebSocket: CLOSED (clean close frame sent)
  - Token refresh: deferred
  - Biometric gate: ARMED (will fire on next FOREGROUND)
  - Background sync: WorkManager task active

SUSPENDED / TERMINATED:
  - WebSocket: CLOSED
  - Offline queue: persisted to drift SQLite (survives kill)
  - Background sync: WorkManager periodic task (15-minute interval minimum, Android constraint)
  - FCM: data-only messages wake the app for sync
```

**State transitions are governed by `AppLifecycleObserver` (Riverpod + WidgetsBindingObserver).**
**State must be logged to local SQLite on every transition.**

---

## SECTION IV — OFFLINE-FIRST SYNC ENGINE (R59 COMPLIANCE)

### IV.A — Local Database Schema (Drift / SQLite)

```dart
// Drift table definitions — locked naming

class PendingActionQueue extends Table {
  TextColumn get id => text().withDefault(const Constant(''))();  // UUID
  TextColumn get actionType => text()();       // 'job.apply' | 'post.create' | 'gd.checkin' | etc.
  TextColumn get payload => text()();          // JSON string
  TextColumn get createdAt => text()();        // ISO8601
  TextColumn get networkStateAtCreation => text()();  // 'offline'|'online'
  IntColumn get retryCount => integer().withDefault(const Constant(0))();
  TextColumn get lastRetryAt => text().nullable()();
  TextColumn get syncStatus => text().withDefault(const Constant('pending'))();
  // 'pending' | 'syncing' | 'synced' | 'failed_permanent'
  TextColumn get serverAckAt => text().nullable()();
  TextColumn get serverResponseCode => text().nullable()();
}

class SyncCheckpointLog extends Table {
  TextColumn get id => text()();              // UUID
  TextColumn get domain => text()();          // 'jobs' | 'profile' | 'notifications' | 'gd' | 'dojo'
  TextColumn get lastSyncedAt => text()();    // ISO8601
  TextColumn get lastSyncedVersion => text()();  // server-provided ETag or version token
  IntColumn get recordsSynced => integer().withDefault(const Constant(0))();
  BoolColumn get deltaMode => boolean().withDefault(const Constant(true))();
}

class LocalActionAuditLog extends Table {
  TextColumn get id => text()();
  TextColumn get actionType => text()();
  TextColumn get performedAt => text()();
  TextColumn get deviceId => text()();
  TextColumn get appVersion => text()();
  TextColumn get networkState => text()();    // 'online'|'offline'
  TextColumn get syncStatus => text()();      // 'pending'|'synced'|'failed'
  TextColumn get pendingActionId => text().nullable()();  // FK to PendingActionQueue
}

class CachedEntities extends Table {
  TextColumn get entityType => text()();      // 'job' | 'profile' | 'notification' | 'gd_session'
  TextColumn get entityId => text()();
  TextColumn get data => text()();            // JSON
  TextColumn get cachedAt => text()();
  TextColumn get expiresAt => text()();
  IntColumn get version => integer().withDefault(const Constant(1))();
}
```

---

### IV.B — Offline Action Queue — Supported Action Types (Locked)

| Action Type | Endpoint | Conflict Strategy |
|---|---|---|
| `job.apply` | POST /api/v1/applications | Deduplicate on (user_id, job_id) |
| `profile.update` | PATCH /api/v1/users/me | Last-write-wins (timestamp) |
| `post.create` | POST /api/v1/posts | Append — no conflict |
| `post.react` | POST /api/v1/posts/{id}/react | Idempotent on (user_id, post_id, type) |
| `message.send` | POST /api/v1/messages | Ordered by created_at |
| `gd.checkin` | POST /api/v1/gd/sessions/{id}/checkin | Idempotent on session_id + user_id |
| `skill.endorse` | POST /api/v1/skills/endorse | Idempotent on (endorser_id, skill_id) |
| `notification.read` | PATCH /api/v1/notifications/{id}/read | Last-read-wins |
| `dojo.match.ready` | POST /api/v1/dojo/matches/{id}/ready | Idempotent |

**Actions NOT supported offline (must reject with UI prompt if attempted offline):**

| Action | Reason |
|---|---|
| Payment initiation | Requires real-time gateway |
| Voice GD join | Requires live WebRTC session |
| Dojo live match join | Requires live LiveKit session |
| Certificate download | Requires authenticated CDN token |
| Biometric re-auth gate bypass | Security — cannot defer |

---

### IV.C — Sync Engine State Machine

```
SYNC ENGINE STATES:
  IDLE → CHECKING → SYNCING → COMPLETED → IDLE
                            → CONFLICT_DETECTED → RESOLVING → COMPLETED

Trigger conditions for CHECKING:
  - App enters FOREGROUND state
  - Network connectivity restored (ConnectivityPlus callback)
  - WorkManager periodic trigger (every 15 minutes when backgrounded)
  - Explicit user pull-to-refresh

CHECKING:
  1. Read all pending actions from PendingActionQueue where syncStatus = 'pending'
  2. Sort by createdAt ASC
  3. If queue empty → go to IDLE

SYNCING:
  For each pending action:
    1. Set syncStatus = 'syncing'
    2. HTTP call to mapped endpoint
    3. On 2xx: set syncStatus = 'synced', serverAckAt = now, delete from queue
    4. On 409 CONFLICT: set syncStatus = 'conflict', route to CONFLICT_DETECTED
    5. On 4xx (non-409): set syncStatus = 'failed_permanent', do NOT retry
    6. On 5xx or timeout: increment retryCount, apply exponential backoff
       Retry delay = min(2^retryCount * 2 seconds, 120 seconds)
       Max retries: 10. After 10 → set syncStatus = 'failed_permanent', notify user

CONFLICT_DETECTED:
  - Log to ConflictRecordLog (local SQLite)
  - Call POST /api/v1/sync/resolve with: { action_id, local_payload, server_version }
  - Backend returns: { resolution: 'local_wins'|'server_wins'|'merge', merged_payload }
  - Apply resolution
  - Emit conflict_resolved event to local audit log
```

---

### IV.D — Delta Sync Contract

To minimize bandwidth, the app does not pull full entity lists on sync. It uses delta sync checkpoints.

**Delta Sync API Contract:**

```
GET /api/v1/sync/delta/{domain}
Auth: Bearer {access_token}
Headers: If-None-Match: {last_synced_version}
Params: { since: ISO8601_timestamp, limit: 50 }

Response 200: {
  domain: string,
  version: string,           // ETag — store in SyncCheckpointLog
  records: [{
    entity_id: UUID,
    operation: 'upsert'|'delete',
    data: object|null,
    updated_at: ISO8601
  }],
  has_more: boolean,
  next_cursor: string|null
}

Response 304: Not Modified (no sync needed)
```

**Domains with delta sync enabled:** `jobs`, `profile`, `notifications`, `gd_schedule`, `dojo_schedule`, `belt_status`, `billing`.

**Domains that are always full-refresh:** `trust_score`, `session_devices`.

---

### IV.E — Conflict Resolution UI (Mandatory Screens)

When a conflict cannot be auto-resolved, the user must be shown a resolution screen.

**Screen: Sync Conflict Resolution**

```
Display:
  - What data conflicted (human-readable description)
  - Your version (local) — shown with timestamp
  - Server version — shown with timestamp
  - Two buttons: "Keep Mine" | "Use Latest"
  - Option: "See what changed" (diff view)

On choice:
  - POST /api/v1/sync/resolve { resolution: 'local'|'server', action_id }
  - Apply resolution to local cache
  - Update SyncCheckpointLog
  - Dismiss screen
```

**Conflict screen must not block navigation to other parts of the app.**
**Unresolved conflicts must be visible as a badge on the notification icon.**

---

## SECTION V — PUSH NOTIFICATION CONTRACT

### V.A — Device Token Lifecycle

```
ON FIRST LOGIN:
  1. Request FCM permission (iOS: must show system prompt, Android 13+: must show system prompt)
  2. On grant: call FirebaseMessaging.instance.getToken()
  3. POST /api/v1/notifications/device-token/register
     Body: { device_id, push_token, platform }
  4. Store push_token in flutter_secure_storage key: ecoskiller.push_token

ON TOKEN REFRESH (FirebaseMessaging.onTokenRefresh callback):
  1. Receive new push_token
  2. PATCH /api/v1/notifications/device-token/refresh
     Body: { device_id, old_token, new_token }
  3. Update flutter_secure_storage

ON LOGOUT:
  1. DELETE /api/v1/notifications/device-token/revoke
     Body: { device_id, push_token }
  2. Delete ecoskiller.push_token from secure storage
  3. FCM.deleteToken() — forces new token on next login

ON PERMISSION DENIED:
  - Degrade gracefully: in-app notification bell shows all notifications
  - Do NOT re-prompt within 7 days
  - Log permission_denied to device trust signal
```

**Push Token API Contracts:**

```
POST /api/v1/notifications/device-token/register
Auth: Bearer {access_token}
Body: { device_id: UUID, push_token: string, platform: string }
Response: { registered: boolean, topics_subscribed: string[] }

PATCH /api/v1/notifications/device-token/refresh
Auth: Bearer {access_token}
Body: { device_id: UUID, old_token: string, new_token: string }
Response: { refreshed: boolean }

DELETE /api/v1/notifications/device-token/revoke
Auth: Bearer {access_token}
Body: { device_id: UUID, push_token: string }
Response: { revoked: boolean }
```

---

### V.B — Notification Types and Delivery Contracts

| Notification Type | FCM Message Type | Foreground Behavior | Background Behavior |
|---|---|---|---|
| GD Session Reminder | Notification + Data | In-app banner | System tray |
| Interview Slot Confirmed | Notification + Data | In-app banner | System tray |
| Job Application Status | Notification + Data | In-app banner | System tray |
| Dojo Match Ready | Data-only | In-app alert dialog | Background wake + sound |
| Session Force-Revoked | Data-only | Force logout flow | Force logout on next open |
| Trust Score Change | Data-only | Silent local update | Silent local update |
| Message Received | Notification + Data | In-app message badge | System tray |
| Belt Promotion | Notification + Data | Full-screen celebration | System tray |
| Payment Confirmed | Notification + Data | In-app banner | System tray |
| Certificate Issued | Notification + Data | In-app banner | System tray |

**Data-only messages for sensitive flows (Session Revoke, Dojo Match Ready) must be handled in `FirebaseMessaging.onBackgroundMessage` handler registered in `main()` isolate.**

**No notification may display PII in the notification title or body in system tray.** Use: "You have a new update on ECOSKILLER." System tray must never expose salary figures, health data, or legal content.

---

### V.C — In-App Notification Center

The app must maintain a local notification inbox powered by the drift database, independently from push delivery.

```
GET /api/v1/notifications
Auth: Bearer {access_token}
Params: { page, limit, unread_only }
Response: {
  notifications: [{
    id: UUID,
    type: string,
    title: string,
    body: string,
    action_url: string|null,
    is_read: boolean,
    created_at: ISO8601
  }],
  unread_count: integer,
  total: integer
}
```

Notification read state must be synced back to server via `notification.read` offline action (queueable).

---

## SECTION VI — REALTIME CHANNEL MANAGEMENT

### VI.A — WebSocket Connection Contract

WebSocket is the command channel for: GD turn events, interview slot confirmations, Dojo match state transitions, and platform-wide alerts.

**Connection Lifecycle:**

```
CONNECT:
  URL: wss://{api_domain}/ws/v1?token={access_token}&device_id={device_id}
  Protocol: wss (TLS enforced — no plain ws in any environment)
  On connect: send { type: 'hello', device_id, app_version }
  Server sends: { type: 'welcome', session_id, server_time }

HEARTBEAT:
  Client → Server: { type: 'ping' } every 25 seconds
  Server → Client: { type: 'pong' }
  If no pong within 10 seconds: trigger RECONNECT flow

RECONNECT FLOW (Exponential Backoff):
  Attempt 1: after 1 second
  Attempt 2: after 2 seconds
  Attempt 3: after 4 seconds
  Attempt 4: after 8 seconds
  Attempt 5: after 16 seconds
  Attempt 6+: after 30 seconds (cap)
  Max continuous attempts: 20
  After 20 failures: show "Connection lost" banner, stop attempting
  User-triggered retry: always allowed

CLOSE (Clean):
  Client sends: { type: 'goodbye', device_id }
  Wait for close frame acknowledgement
  Close socket

CLOSE (Dirty — network loss):
  No close frame possible
  Backend detects via heartbeat timeout (60 seconds)
  Backend emits: session.disconnected event to Kafka
  Backend marks device as offline in Redis
```

**WebSocket Message Types (Inbound — Server → Client):**

```
{ type: 'gd.turn.start', payload: { participant_id, duration_seconds, round_type } }
{ type: 'gd.turn.end', payload: { participant_id } }
{ type: 'gd.session.end', payload: { session_id, scores_available_at } }
{ type: 'interview.slot.locked', payload: { slot_id, interview_id, starts_at } }
{ type: 'dojo.match.start', payload: { match_id, livekit_token, room_name } }
{ type: 'dojo.match.end', payload: { match_id, result } }
{ type: 'platform.alert', payload: { message, severity: 'info'|'warning'|'critical' } }
{ type: 'session.revoked', payload: { reason } }
```

**WebSocket Message Types (Outbound — Client → Server):**

```
{ type: 'ping' }
{ type: 'hello', device_id, app_version }
{ type: 'goodbye', device_id }
{ type: 'gd.ready', session_id }
{ type: 'dojo.ready', match_id }
```

**Riverpod Provider (locked structure):**

```dart
// WebSocket state must be exposed via Riverpod StreamProvider
// All UI components subscribe via ref.watch(webSocketProvider)
// No global singletons. No direct socket access from widgets.
final webSocketProvider = StreamProvider<WsMessage>(...);
final wsConnectionStateProvider = StateProvider<WsConnectionState>(...);
// WsConnectionState: connected | connecting | reconnecting | disconnected
```

---

### VI.B — LiveKit Room Join Contract (Dojo / Interviews)

LiveKit is used for Dojo matches and live interviews. The join flow is strictly token-gated.

**Token Fetch → Room Join Flow:**

```
1. Receive { type: 'dojo.match.start', livekit_token, room_name } via WebSocket
   OR
   Tap "Join Interview" on interview card (calls GET /api/v1/interviews/{id}/token)

2. Token fetch API:
   GET /api/v1/realtime/livekit/token/{session_id}
   Auth: Bearer {access_token}
   Response: { token: string, room_name: string, expires_at: ISO8601 }

3. Validate token expiry (must be > 30 seconds from now)
   If expired: re-fetch, max 2 retries, then show error

4. Connect LiveKit room:
   Room.connect(url: livekit_server_url, token: token)

5. On disconnect (network or server):
   - Attempt reconnect once silently (LiveKit SDK handles internally)
   - If reconnect fails: show "Connection lost" UI
   - DO NOT auto-rejoin GD or Dojo rooms (session integrity)
   - DO auto-rejoin interview rooms (user-driven session, rejoin allowed once)

6. On room exit:
   - Room.disconnect()
   - Emit local audit log: { action: 'livekit.room.exit', room_name, session_id, duration_seconds }
```

---

### VI.C — Jitsi Room Join Contract (Voice GD)

Jitsi is used exclusively for Voice GD sessions. The IFrame is loaded inside a WebView (Flutter WebView or jitsi_meet_flutter SDK).

**Jitsi Join Flow:**

```
1. Receive GD session assignment from backend (via push notification or WebSocket)
2. Wait for GD preparation timer to elapse (displayed in app)
3. Call GET /api/v1/gd/sessions/{session_id}/room-token
   Auth: Bearer {access_token}
   Response: {
     room_name: string,          // e.g. gd_banking_20240206_1234
     jwt: string,                // Jitsi JWT — short-lived (10 minute TTL)
     jitsi_server_url: string,   // self-hosted Jitsi domain
     config_overrides: object    // disableVideo: true, startAudioMuted: true, etc.
   }

4. Launch Jitsi with:
   - Video: DISABLED (enforced by config_overrides)
   - Audio: MUTED by default (backend controls via API)
   - UI controls: REMOVED (startWithAudioMuted, disableDeepLinking, etc.)
   - Room created by backend — never by client

5. Client only receives mute/unmute commands via WebSocket (gd.turn.start / gd.turn.end)
   Client does NOT control its own mic state — backend controls it

6. On GD session end:
   - Close Jitsi WebView
   - Navigate to GD Results screen
   - Display scores (fetched from GET /api/v1/gd/sessions/{session_id}/results)
```

---

## SECTION VII — BACKGROUND SYNC & WORKMANAGER CONTRACT

### VII.A — WorkManager Task Definitions (Android)

WorkManager is the only permitted background task executor on Android. No raw Isolates for background sync.

```dart
// Task: Offline Queue Processor
const String TASK_SYNC_QUEUE = 'ecoskiller.sync.queue';
// Constraints: NetworkType.connected, battery not critically low
// Frequency: Every 15 minutes (minimum enforced by Android OS)
// Retry policy: exponential backoff, initial delay 5 minutes

// Task: Delta Sync Refresh
const String TASK_DELTA_SYNC = 'ecoskiller.sync.delta';
// Constraints: NetworkType.unmetered preferred (WiFi), battery not low
// Frequency: Every 30 minutes
// Domains: jobs, notifications, gd_schedule, dojo_schedule

// Task: Token Prefetch (ensure token freshness on wakeup)
const String TASK_TOKEN_PREFETCH = 'ecoskiller.auth.prefetch';
// Frequency: Every 10 minutes
// Action: If access_token expires within 120 seconds → refresh silently
```

**All WorkManager tasks must:**
- Check for valid session before executing
- Log execution to `LocalActionAuditLog`
- Report outcome to Sentry (on failure)
- Never make UI mutations (no Navigator calls from background)

### VII.B — iOS Background App Refresh

On iOS, WorkManager's background capabilities are limited by BGTaskScheduler. The agent must implement:

```dart
// Register BGAppRefreshTask in AppDelegate.swift (generated by flutter_background_service)
// Permitted task identifiers (must be in Info.plist):
//   com.ecoskiller.sync.queue
//   com.ecoskiller.sync.delta

// iOS background execution window: ~30 seconds
// Therefore: only sync the top 5 pending actions per background execution
// Delta sync: only the 'notifications' domain (most time-sensitive)
```

---

## SECTION VIII — DEEP LINK ROUTING CONTRACT

### VIII.A — Deep Link Schemes (Locked)

```
Custom scheme (fallback):  ecoskiller://
Universal links (iOS):     https://app.ecoskiller.com/...
App links (Android):       https://app.ecoskiller.com/...
```

### VIII.B — Deep Link Route Map (Locked)

| Deep Link Path | Flutter Route | Auth Required | Notes |
|---|---|---|---|
| `/jobs/{job_id}` | `/app/jobs/:id` | Yes | Redirect to login if not authed |
| `/profile/{user_id}` | `/app/profile/:id` | Yes | |
| `/gd/sessions/{session_id}` | `/app/gd/:id` | Yes | Validate session belongs to user |
| `/dojo/matches/{match_id}` | `/app/dojo/match/:id` | Yes | |
| `/interviews/{interview_id}` | `/app/interviews/:id` | Yes | |
| `/certificates/{cert_id}` | `/app/certificates/:id` | No | Public verification |
| `/invites/{referral_code}` | `/app/referral/:code` | No | Capture code pre-login |
| `/verify/{credential_id}` | `/app/verify/:id` | No | Public |
| `/notifications` | `/app/notifications` | Yes | |
| `/billing` | `/app/billing` | Yes | Biometric gate |

**GoRouter deep link handling must:**
- Capture and persist the target path if user is not authenticated
- Redirect to login with `redirect_after_login` parameter
- After login completion, resume navigation to captured path
- Validate that the resource in the path belongs to the authenticated user before rendering

---

## SECTION IX — BIOMETRIC TRUST GATE CONTRACT

### IX.A — Biometric Gate Trigger Rules

```
Biometric gate is TRIGGERED when ALL of the following are true:
  1. Device supports biometric auth (local_auth canCheckBiometrics = true)
  2. User has not completed biometric auth in the last 5 minutes
     (check flutter_secure_storage: ecoskiller.last_biometric_at)

AND the user attempts to access ANY of:
  - Billing / Invoices screen
  - Offer Letter viewer
  - Certificate download
  - Legal documents screen
  - Salary / Compensation display
  - Device session management screen
  - Account deletion screen
  - Linked bank/wallet screen

AND on app resume from BACKGROUND if last_biometric_at is > 5 minutes ago.
```

### IX.B — Biometric Gate Flow

```dart
// BiometricGate widget — wraps all sensitive screens

On gate trigger:
  1. Show biometric prompt: "Verify your identity to continue"
  2. Call local_auth.authenticate(localizedReason: '...')
  3. On success:
     - Write ecoskiller.last_biometric_at = now() to secure storage
     - Emit device trust signal: { signal_type: 'biometric_success', device_id }
     - Allow navigation to protected screen
  4. On failure (3 attempts):
     - Lock screen for 30 seconds
     - Emit device trust signal: { signal_type: 'biometric_failure', device_id }
     - Show: "Unable to verify. Try again in 30 seconds."
  5. On biometric not available (no hardware / not enrolled):
     - Fall through to PIN/password entry screen (Flutter in-app PIN, separate from OS lock)
     - PIN stored as bcrypt hash in flutter_secure_storage
```

### IX.C — Biometric Trust Signal API

```
POST /api/v1/trust/device-signal
Auth: Bearer {access_token}
Body: {
  device_id: UUID,
  signal_type: 'biometric_success'|'biometric_failure'|'pin_success'|'pin_failure',
  occurred_at: ISO8601
}
Response: { received: boolean }
```

---

## SECTION X — DEVICE TRUST SIGNAL EMISSION CONTRACT

Every mobile device session emits trust signals at defined lifecycle events. These are consumed by the backend Trust Signal Aggregation Engine (`API_INTEGRATION_AGENT`, Section VII.A).

### X.A — Trust Signal Emission Events (Locked)

| Event | Signal Type | Trigger |
|---|---|---|
| Device registered | `device.registered` | First login on device |
| Biometric success | `biometric.success` | Successful gate pass |
| Biometric failure | `biometric.failure` | Failed gate attempt |
| Offline queue flushed | `sync.completed` | Successful sync after offline |
| Conflict resolved | `sync.conflict_resolved` | Manual or auto conflict resolution |
| Session expired | `session.expired` | Token refresh returned 401 |
| Push permission granted | `push.permission_granted` | FCM grant |
| Push permission denied | `push.permission_denied` | FCM deny |
| App updated | `app.updated` | Version change on launch |
| Suspicious deep link | `deeplink.suspicious` | Deep link to restricted resource without auth |
| Background sync success | `background_sync.success` | WorkManager task completed |
| Background sync failure | `background_sync.failure` | WorkManager task failed |

**All trust signals emitted via:**

```
POST /api/v1/trust/device-signal (Section IX.C)
```

**Trust signals must be queued in the offline action queue if network is unavailable. They are high priority — processed before other queued actions.**

---

## SECTION XI — MOBILE OBSERVABILITY (MANDATORY)

### XI.A — Sentry Crash Reporting

```dart
// Initialized in main() before runApp()
await SentryFlutter.init(
  (options) {
    options.dsn = Env.sentryDsn;               // from environment config
    options.environment = Env.environment;     // 'dev'|'staging'|'production'
    options.release = '${Env.appVersion}+${Env.buildNumber}';
    options.tracesSampleRate = 0.2;            // 20% performance tracing
    options.attachScreenshot = false;          // NEVER — privacy
    options.attachViewHierarchy = false;       // NEVER — privacy
  },
  appRunner: () => runApp(const EcoskillerApp()),
);
```

**Sentry must capture:**
- All uncaught Flutter framework exceptions
- All uncaught Dart async errors
- HTTP errors (4xx/5xx from Dio — exclude 401 as these are expected)
- WebSocket disconnections (log as breadcrumb, not error)
- WorkManager task failures

**Sentry must NOT capture:**
- Any PII (user_id is allowed as anonymous identifier)
- Access tokens or refresh tokens
- Biometric results
- Payment data

### XI.B — OpenTelemetry Trace Emission (Mobile)

All critical user journeys emit OpenTelemetry spans to the self-hosted OpenTelemetry Collector.

```dart
// Spans to emit:
'mobile.login.flow'              // Duration from login tap to dashboard render
'mobile.sync.queue.flush'        // Duration of offline queue processing
'mobile.delta.sync.{domain}'     // Per-domain delta sync duration
'mobile.gd.join.flow'            // From GD join tap to Jitsi IFrame load
'mobile.livekit.connect'         // From token fetch to room.connect()
'mobile.biometric.gate'          // Duration of biometric prompt
'mobile.deep_link.resolve'       // From link receipt to screen render
```

**OTLP HTTP exporter endpoint:** `https://otel.{internal_domain}/v1/traces`

### XI.C — Required Flutter App Analytics Events (Self-Hosted, No Third-Party Analytics)

```dart
// Events emitted to POST /api/v1/analytics/mobile-events
// No Google Analytics. No Firebase Analytics. No Mixpanel.
// Self-hosted only.

Events:
  screen_view         { screen_name, previous_screen, duration_ms }
  button_tap          { button_id, screen_name }
  form_submit         { form_id, success: bool }
  offline_action      { action_type }
  sync_completed      { actions_synced, duration_ms }
  notification_opened { notification_type }
  deep_link_opened    { path }
```

---

## SECTION XII — APP BUILD & ENVIRONMENT CONTRACT

### XII.A — Environment Configuration (Locked)

```dart
// lib/config/env.dart — generated per environment, never committed to VCS

class Env {
  static const String environment     = String.fromEnvironment('ENV');
  static const String apiBaseUrl      = String.fromEnvironment('API_BASE_URL');
  static const String wsBaseUrl       = String.fromEnvironment('WS_BASE_URL');
  static const String livekitUrl      = String.fromEnvironment('LIVEKIT_URL');
  static const String jitsiDomain     = String.fromEnvironment('JITSI_DOMAIN');
  static const String sentryDsn       = String.fromEnvironment('SENTRY_DSN');
  static const String otelEndpoint    = String.fromEnvironment('OTEL_ENDPOINT');
  static const String appVersion      = String.fromEnvironment('APP_VERSION');
  static const String buildNumber     = String.fromEnvironment('BUILD_NUMBER');
}
```

**Build commands per environment:**

```bash
# DEV
flutter build apk --dart-define=ENV=dev --dart-define=API_BASE_URL=http://localhost:8000/api/v1 ...

# STAGING
flutter build apk --dart-define=ENV=staging --dart-define=API_BASE_URL=https://api.staging.ecoskiller.com/api/v1 ...

# PRODUCTION
flutter build apk --dart-define=ENV=production --dart-define=API_BASE_URL=https://api.ecoskiller.com/api/v1 ...
```

**No environment variable file is shipped inside the APK/IPA. All values are compile-time constants.**

### XII.B — CI/CD Mobile Pipeline (Locked Stages)

```yaml
mobile-pipeline:
  stages:
    - flutter-analyze:         # dart analyze, flutter analyze
    - flutter-test:            # flutter test (unit + widget tests)
    - integration-test:        # flutter drive (on emulator)
    - build-android-staging:   # flutter build appbundle (staging config)
    - build-ios-staging:       # flutter build ipa (staging config)
    - distribute-staging:      # Firebase App Distribution (internal testers)
    - build-android-prod:      # Requires 2 approvals
    - build-ios-prod:          # Requires 2 approvals
    - sign-android:            # Android keystore signing (from Vault)
    - sign-ios:                # iOS signing (from Vault)
    - submit-play-store:       # Human execution only (M1 Law)
    - submit-app-store:        # Human execution only (M1 Law)
```

**App signing credentials (keystore / provisioning profiles) stored in HashiCorp Vault:**

```
vault/secret/mobile/android/keystore_base64
vault/secret/mobile/android/keystore_password
vault/secret/mobile/android/key_alias
vault/secret/mobile/android/key_password
vault/secret/mobile/ios/p12_base64
vault/secret/mobile/ios/p12_password
vault/secret/mobile/ios/provisioning_profile_base64
```

---

## SECTION XIII — OFFLINE-FIRST UI CONTRACT (MANDATORY SCREENS)

Every screen defined below must exist. Absence → STOP EXECUTION.

| Screen | Purpose | Trigger |
|---|---|---|
| Offline Status Banner | Persistent top banner when network absent | ConnectivityPlus: none |
| Queued Actions Screen | List of pending offline actions with sync status | Navigation from notification badge |
| Sync Progress Indicator | Inline progress during sync flush | SYNCING state |
| Conflict Resolution Screen | Show local vs server data, user chooses | CONFLICT_DETECTED state |
| Offline Empty State | Per-screen empty state when no cached data | First launch with no network |
| Connection Lost Overlay | Full or partial overlay when WebSocket drops | WsConnectionState: disconnected |
| Background Sync Status | Status in settings screen: "Last synced X minutes ago" | Always visible in Settings |

---

## SECTION XIV — ENFORCEMENT REGISTRY

### Final Checklist (All items must pass before mobile deployment claim)

| ID | Requirement | Status |
|---|---|---|
| MOB-01 | Flutter stack lock matches Section II LAW-1 pubspec.yaml | REQUIRED |
| MOB-02 | All secure storage keys defined and encrypted | REQUIRED |
| MOB-03 | Token rotation Dio interceptor implemented | REQUIRED |
| MOB-04 | Force logout on refresh 401 implemented | REQUIRED |
| MOB-05 | Device registration API called on first login | REQUIRED |
| MOB-06 | Device revoke API called on logout | REQUIRED |
| MOB-07 | App lifecycle state machine implemented | REQUIRED |
| MOB-08 | Drift SQLite schema: PendingActionQueue table present | REQUIRED |
| MOB-09 | Drift SQLite schema: SyncCheckpointLog table present | REQUIRED |
| MOB-10 | Drift SQLite schema: LocalActionAuditLog table present | REQUIRED |
| MOB-11 | Drift SQLite schema: CachedEntities table present | REQUIRED |
| MOB-12 | All 9 offline action types implemented and mapped | REQUIRED |
| MOB-13 | Actions NOT supported offline show rejection UI | REQUIRED |
| MOB-14 | Sync engine state machine: IDLE→CHECKING→SYNCING→COMPLETED | REQUIRED |
| MOB-15 | Exponential backoff with 10-retry cap implemented | REQUIRED |
| MOB-16 | Delta sync contract implemented for all 7 domains | REQUIRED |
| MOB-17 | Conflict Resolution screen implemented | REQUIRED |
| MOB-18 | FCM token registration on login | REQUIRED |
| MOB-19 | FCM token refresh callback implemented | REQUIRED |
| MOB-20 | FCM token revoke on logout | REQUIRED |
| MOB-21 | Push permission denied: graceful degradation | REQUIRED |
| MOB-22 | All notification types mapped (Table Section V.B) | REQUIRED |
| MOB-23 | In-app notification center with drift persistence | REQUIRED |
| MOB-24 | WebSocket connect + heartbeat + reconnect implemented | REQUIRED |
| MOB-25 | WebSocket exponential backoff (cap 30s, max 20 attempts) | REQUIRED |
| MOB-26 | All inbound WS message types handled | REQUIRED |
| MOB-27 | Riverpod StreamProvider for WebSocket state | REQUIRED |
| MOB-28 | LiveKit token fetch + room join flow implemented | REQUIRED |
| MOB-29 | Jitsi IFrame join flow implemented | REQUIRED |
| MOB-30 | WorkManager: TASK_SYNC_QUEUE registered (Android) | REQUIRED |
| MOB-31 | WorkManager: TASK_DELTA_SYNC registered (Android) | REQUIRED |
| MOB-32 | WorkManager: TASK_TOKEN_PREFETCH registered (Android) | REQUIRED |
| MOB-33 | iOS BGAppRefreshTask registered in Info.plist | REQUIRED |
| MOB-34 | All deep link routes mapped to GoRouter (Table Section VIII.B) | REQUIRED |
| MOB-35 | Redirect-after-login deep link capture implemented | REQUIRED |
| MOB-36 | Biometric gate triggers on all 9 sensitive screens | REQUIRED |
| MOB-37 | Biometric gate triggers on app resume > 5 minutes | REQUIRED |
| MOB-38 | Biometric failure: 3-attempt lockout for 30 seconds | REQUIRED |
| MOB-39 | All 12 trust signal types emitted at correct lifecycle events | REQUIRED |
| MOB-40 | Trust signals queued offline if network unavailable | REQUIRED |
| MOB-41 | Sentry initialized with correct privacy config | REQUIRED |
| MOB-42 | Sentry: no PII, no tokens in captured data | REQUIRED |
| MOB-43 | OTLP spans emitted for all 7 critical journeys | REQUIRED |
| MOB-44 | Self-hosted analytics — NO third-party analytics SDKs | REQUIRED |
| MOB-45 | Environment config via dart-define only (no .env files in APK) | REQUIRED |
| MOB-46 | All signing credentials in HashiCorp Vault | REQUIRED |
| MOB-47 | CI pipeline: 8 stages defined and enforced | REQUIRED |
| MOB-48 | App Store / Play Store submit: Human execution only | REQUIRED |
| MOB-49 | All 7 offline/sync UI screens implemented | REQUIRED |
| MOB-50 | Multi-device session management screen (list + revoke) | REQUIRED |

**Any item in REQUIRED status that is not met → STOP EXECUTION → REPORT MOBILE-SYNC-AGENT-INCOMPLETE → NO DEPLOYMENT CLAIM PERMITTED**

---

## SECTION XV — VERSION & MUTATION LOG

| Version | Change | Authority |
|---|---|---|
| v1.0 | Initial sealed document | Human Declaration |

**Next mutation requires:** version bump to v1.1, change log entry, human sign-off.
**No silent mutations. No interpretation-based changes. Add-only policy enforced.**

---

> **FINAL SEAL**
> This document is complete, locked, and authoritative.
> The `MOBILE_APP_SYNC_AGENT` operates exclusively within the boundaries defined herein.
> The Flutter mobile application is not a consumer of the platform — it is a governed, trust-carrying, self-healing node within the ECOSKILLER enterprise trust surface.
> Deviation is not interpretation — it is violation.
> **ECOSKILLER ANTIGRAVITY LAYER — MOBILE TRUST SURFACE — SEALED v1.0**
