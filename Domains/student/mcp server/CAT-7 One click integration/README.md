# mcp-07-one-click-integrations

**Ecoskiller | CAT-7 — One-Click Integrations**  
MCP Server in Java | 2 Agents | Priority: HIGH

---

## Agents (2)

| #  | Tool Name        | Agent |
|----|------------------|-------|
| 29 | `sso_integration` | 73_SSO_INTEGRATION_AGENT |
| 30 | `calendar_sync`   | 81_CALENDAR_SYNC_MASTER_SEAL |

---

## Requirements

- Java 11+
- Maven 3.6+ *(build only — zero runtime dependencies)*

---

## Build

```bash
mvn clean package
# produces: target/mcp-07-one-click-integrations.jar
```

---

## Run the server

```bash
java -jar target/mcp-07-one-click-integrations.jar
```

Communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-07-one-click-integrations": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-07-one-click-integrations/target/mcp-07-one-click-integrations.jar"]
    }
  }
}
```

---

## Run tests

```bash
bash test_agents.sh            # 33 tests — pass/fail summary
bash test_agents.sh --verbose  # with full JSON output
```

---

## File Structure

```
mcp-07-one-click-integrations/
├── pom.xml                              ← Maven build (fat JAR, Java 11+)
├── test_agents.sh                       ← Bash test runner (33 tests)
├── README.md
└── src/main/java/ecoskiller/
    └── McpServer.java                   ← Main MCP server (both agents)
```

---

## Agent Capabilities

---

### 29. `sso_integration` — 73_SSO_INTEGRATION_AGENT

One-click SSO configuration for Ecoskiller across all major identity providers.

**Supported Providers**

| Provider | Protocol(s) |
|----------|-------------|
| Google Workspace | OIDC, OAuth 2.0 |
| Microsoft / Azure AD | SAML 2.0, OIDC, OAuth 2.0 |
| Okta | OIDC, SAML 2.0 |
| Auth0 | OIDC, OAuth 2.0 |
| LDAP / Active Directory | LDAP |
| Custom SAML | SAML 2.0 |

**Key features**
- Automatic metadata exchange (IDP ↔ SP)
- Role mapping: IDP groups → Ecoskiller roles (ADMIN / FACULTY / STUDENT / STAFF)
- MFA enforcement with TOTP, SMS, Email OTP
- PKCE + state + nonce validation
- JWT / RS256 token signing
- Configurable session timeout and IP+UA binding
- Full audit log: LOGIN, LOGOUT, TOKEN_REFRESH, MFA_CHALLENGE, ROLE_CHANGE

**Parameters**

| Param | Description |
|-------|-------------|
| `action` | `configure`, `test_connection`, `audit_log`, `status`, `revoke` |
| `provider` | `GOOGLE`, `MICROSOFT`, `AZURE_AD`, `OKTA`, `AUTH0`, `LDAP`, `SAML_CUSTOM` |
| `protocol` | `OIDC`, `OAUTH2`, `SAML2`, `LDAP` |
| `domain` | Your Ecoskiller domain (e.g. `school.ecoskiller.app`) |
| `client_id` | IDP application client ID |
| `client_secret` | IDP client secret (stored encrypted, returned redacted) |
| `metadata_url` | IDP well-known / metadata URL |
| `role_mapping` | `DEFAULT` or `CUSTOM` |
| `mfa_required` | `true` / `false` |
| `session_timeout_min` | Session lifetime in minutes (default: 480) |

**Sample call — configure Google SSO**
```bash
echo '{"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"sso_integration","arguments":{"action":"configure","provider":"GOOGLE","protocol":"OIDC","domain":"school.ecoskiller.app","client_id":"YOUR_CLIENT_ID","mfa_required":"true","session_timeout_min":"480"}}}' \
  | java -jar target/mcp-07-one-click-integrations.jar
```

---

### 30. `calendar_sync` — 81_CALENDAR_SYNC_MASTER_SEAL

Master bi-directional calendar synchronisation across all major calendar and conferencing platforms.

**Supported Providers**

| Provider | Notes |
|----------|-------|
| Google Calendar | OAuth 2.0, push webhooks |
| Microsoft Outlook | OAuth 2.0 / Graph API |
| Exchange Server | EWS / Graph API |
| Apple iCal | CalDAV |
| Zoom | Meeting sync |
| Microsoft Teams | Meeting sync |
| Ecoskiller Internal | Native scheduler |

**Key features**
- Push / Pull / Bidirectional sync modes
- Timezone normalisation (IANA tz database)
- Recurring event expansion and sync
- Conflict detection with configurable resolution strategies
- RSVP & attendee sync
- Video-link preservation (Zoom, Teams, Meet)
- Webhook / push-notification registration
- Incremental sync tokens for efficiency

**Parameters**

| Param | Description |
|-------|-------------|
| `action` | `sync`, `register_webhook`, `status`, `revoke`, `conflict_report` |
| `provider` | `GOOGLE_CALENDAR`, `MICROSOFT_OUTLOOK`, `EXCHANGE_SERVER`, `APPLE_ICAL`, `ZOOM`, `MS_TEAMS`, `ECOSKILLER_INTERNAL` |
| `user_id` | Ecoskiller user ID |
| `calendar_id` | Provider calendar ID (default: `primary`) |
| `sync_direction` | `PUSH`, `PULL`, `BIDIRECTIONAL` |
| `date_from` | Sync window start (ISO date) |
| `date_to` | Sync window end (ISO date) |
| `timezone` | IANA timezone (e.g. `Asia/Kolkata`, `UTC`) |
| `conflict_strategy` | `SOURCE_WINS`, `TARGET_WINS`, `LATEST_WINS`, `MANUAL` |
| `include_recurring` | `true` / `false` |

**Sample call — bidirectional Google Calendar sync**
```bash
echo '{"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"calendar_sync","arguments":{"action":"sync","provider":"GOOGLE_CALENDAR","user_id":"USR-1001","sync_direction":"BIDIRECTIONAL","timezone":"Asia/Kolkata","include_recurring":"true"}}}' \
  | java -jar target/mcp-07-one-click-integrations.jar
```

---

## Protocol

| Property    | Value                |
|-------------|----------------------|
| Transport   | stdio (stdin/stdout) |
| Format      | JSON-RPC 2.0         |
| MCP Version | 2024-11-05           |
| Methods     | `initialize`, `tools/list`, `tools/call`, `ping` |
