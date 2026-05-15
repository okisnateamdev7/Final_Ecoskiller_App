# mcp-24-scoring-billing

**Ecoskiller | CAT-24 — Scoring & Fairness / Security & Compliance / Billing & Quota / Event & Contract**  
MCP Server in Java | 36 Agents | 45 Tests | Priority: HIGH

---

## Agents (36)

### Scoring & Fairness (12)
| # | Tool Name | Agent |
|---|-----------|-------|
| 1 | `call_cost_calculation` | CALL_COST_CALCULATION_AGENT |
| 2 | `call_rate_limit` | CALL_RATE_LIMIT_AGENT |
| 3 | `high_usage_alert` | HIGH_USAGE_ALERT_AGENT |
| 4 | `offline_goto_dojo_score_sync` | OFFLINE_GOTO_DOJO_SCORE_SYNC_AGENT |
| 5 | `phone_ai_explainability` | PHONE_AI_EXPLAINABILITY_AGENT |
| 6 | `phone_behavior_analytics` | PHONE_BEHAVIOR_ANALYTICS_AGENT |
| 7 | `phone_scoring_input_sanitizer` | PHONE_SCORING_INPUT_SANITIZER_AGENT |
| 8 | `phone_speaking_time` | PHONE_SPEAKING_TIME_AGENT |
| 9 | `phone_score_dispute_analytics` | PHONE_SCORE_DISPUTE_ANALYTICS_AGENT |
| 10 | `score_bias_audit` | SCORE_BIAS_AUDIT_AGENT |
| 11 | `scoring_model_deprecation` | SCORING_MODEL_DEPRECATION_AGENT |
| 12 | `phone_minimum_participation` | PHONE_MINIMUM_PARTICIPATION_AGENT |

### Security & Compliance (11)
| # | Tool Name | Agent |
|---|-----------|-------|
| 13 | `media_session_security` | MEDIA_SESSION_SECURITY_AGENT |
| 14 | `voice_impersonation_detection` | VOICE_IMPERSONATION_DETECTION_AGENT |
| 15 | `phone_bot_voice_detection` | PHONE_BOT_VOICE_DETECTION_AGENT |
| 16 | `phone_domain_isolation` | PHONE_DOMAIN_ISOLATION_AGENT |
| 17 | `phone_role_escalation_guard` | PHONE_ROLE_ESCALATION_GUARD_AGENT |
| 18 | `tenant_audio_object_isolation` | TENANT_AUDIO_OBJECT_ISOLATION_AGENT |
| 19 | `tenant_transcript_encryption` | TENANT_TRANSCRIPT_ENCRYPTION_AGENT |
| 20 | `short_lived_token_revocation` | SHORT_LIVED_TOKEN_REVOCATION_AGENT |
| 21 | `phone_permission_matrix` | PHONE_PERMISSION_MATRIX_AGENT |
| 22 | `phone_cross_session_behavior` | PHONE_CROSS_SESSION_BEHAVIOR_AGENT |
| 23 | `human_override_audit` | HUMAN_OVERRIDE_AUDIT_AGENT |

### Billing & Quota (7)
| # | Tool Name | Agent |
|---|-----------|-------|
| 24 | `tenant_quota_enforcement` | TENANT_QUOTA_ENFORCEMENT_AGENT |
| 25 | `phone_resource_quota` | PHONE_RESOURCE_QUOTA_AGENT |
| 26 | `sms_segment_calculation` | SMS_SEGMENT_CALCULATION_AGENT |
| 27 | `telecom_usage_reconciliation` | TELECOM_USAGE_RECONCILIATION_AGENT |
| 28 | `phone_feature_gating` | PHONE_FEATURE_GATING_AGENT |
| 29 | `phone_tenant_boundary_enforcement` | PHONE_TENANT_BOUNDARY_ENFORCEMENT_AGENT |
| 30 | `phone_transparency_notification` | PHONE_TRANSPARENCY_NOTIFICATION_AGENT |

### Event & Contract (6)
| # | Tool Name | Agent |
|---|-----------|-------|
| 31 | `kafka_event_schema_drift` | KAFKA_EVENT_SCHEMA_DRIFT_AGENT |
| 32 | `global_event_registry_sync` | GLOBAL_EVENT_REGISTRY_SYNC_AGENT |
| 33 | `phone_event_schema_validation` | PHONE_EVENT_SCHEMA_VALIDATION_AGENT |
| 34 | `phone_api_contract_registry` | PHONE_API_CONTRACT_REGISTRY_AGENT |
| 35 | `phone_participant_identity` | PHONE_PARTICIPANT_IDENTITY_AGENT |
| 36 | `phone_participation_reputation` | PHONE_PARTICIPATION_REPUTATION_AGENT |

---

## Requirements

- **Java 11+** (tested on Java 21)
- **Zero external dependencies** — pure Java stdlib only

---

## Build

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

Or use the convenience script:

```bash
chmod +x build.sh && ./build.sh
```

---

## Run tests

```bash
./build.sh test             # 45 tests — expected: 45 passed, 0 failed
./build.sh test --verbose   # with full JSON-RPC output
```

---

## Run the server

```bash
./build.sh server
# or directly:
java -cp out com.ecoskiller.mcp.server.McpServer
```

Communicates via **stdin/stdout** using **JSON-RPC 2.0**.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-24-scoring-billing": {
      "command": "java",
      "args": ["-cp", "/absolute/path/to/mcp-24-java/out",
               "com.ecoskiller.mcp.server.McpServer"]
    }
  }
}
```

---

## File Structure

```
mcp-24-java/
├── build.sh
├── claude_desktop_config.json
├── README.md
└── src/main/java/com/ecoskiller/mcp/
    ├── TestAgents.java                          ← 45 test cases
    ├── server/
    │   └── McpServer.java                      ← stdio entry point
    ├── protocol/
    │   ├── JsonRpcHandler.java                 ← dispatcher (36 agents)
    │   ├── JsonParser.java                     ← zero-dep JSON parser
    │   └── JsonSerializer.java                 ← zero-dep JSON serializer
    └── agents/
        ├── Agent.java                          ← interface
        ├── BaseAgent.java                      ← schema/arg helpers
        ├── CallCostCalculationAgent.java
        ├── CallRateLimitAgent.java
        ├── HighUsageAlertAgent.java
        ├── OfflineGoToDojoScoreSyncAgent.java
        ├── PhoneAiExplainabilityAgent.java
        ├── PhoneBehaviorAnalyticsAgent.java
        ├── PhoneScoringInputSanitizerAgent.java
        ├── PhoneSpeakingTimeAgent.java
        ├── PhoneScoreDisputeAnalyticsAgent.java
        ├── ScoreBiasAuditAgent.java
        ├── ScoringModelDeprecationAgent.java
        ├── PhoneMinimumParticipationAgent.java
        ├── MediaSessionSecurityAgent.java
        ├── VoiceImpersonationDetectionAgent.java
        ├── PhoneBotVoiceDetectionAgent.java
        ├── PhoneDomainIsolationAgent.java
        ├── PhoneRoleEscalationGuardAgent.java
        ├── TenantAudioObjectIsolationAgent.java
        ├── TenantTranscriptEncryptionAgent.java
        ├── ShortLivedTokenRevocationAgent.java
        ├── PhonePermissionMatrixAgent.java
        ├── PhoneCrossSessionBehaviorAgent.java
        ├── HumanOverrideAuditAgent.java
        ├── TenantQuotaEnforcementAgent.java
        ├── PhoneResourceQuotaAgent.java
        ├── SmsSegmentCalculationAgent.java
        ├── TelecomUsageReconciliationAgent.java
        ├── PhoneFeatureGatingAgent.java
        ├── PhoneTenantBoundaryEnforcementAgent.java
        ├── PhoneTransparencyNotificationAgent.java
        ├── KafkaEventSchemaDriftAgent.java
        ├── GlobalEventRegistrySyncAgent.java
        ├── PhoneEventSchemaValidationAgent.java
        ├── PhoneApiContractRegistryAgent.java
        ├── PhoneParticipantIdentityAgent.java
        └── PhoneParticipationReputationAgent.java
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Adding a new agent

1. Create `agents/MyNewAgent.java` extending `BaseAgent`
2. Implement `toolName()`, `description()`, `inputSchema()`, `execute()`
3. Register with `register(new MyNewAgent())` in `JsonRpcHandler`
4. Add test cases in `TestAgents.java`
5. Recompile: `javac -d out $(find src -name "*.java")`
