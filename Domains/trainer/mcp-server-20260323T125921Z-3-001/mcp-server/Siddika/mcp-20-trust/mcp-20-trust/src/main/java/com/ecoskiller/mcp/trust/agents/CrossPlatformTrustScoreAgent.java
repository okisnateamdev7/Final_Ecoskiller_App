package com.ecoskiller.mcp.trust.agents;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * CROSS_PLATFORM_TRUST_SCORE_AGENT
 *
 * Computes and maintains a unified trust score for users/vendors across
 * all platforms and tenants in the Ecoskiller ecosystem.
 *
 * Actions:
 *   - COMPUTE_SCORE      : Calculate trust score for a principal
 *   - GET_SCORE          : Retrieve current trust score
 *   - UPDATE_SIGNALS     : Feed new trust signals to the model
 *   - COMPARE_ENTITIES   : Compare trust scores of multiple entities
 *   - EXPLAIN_SCORE      : Get explainability breakdown
 */
@Component
public class CrossPlatformTrustScoreAgent extends BaseAgent {

    @Override
    public String agentName() {
        return "CROSS_PLATFORM_TRUST_SCORE_AGENT";
    }

    @Override
    public Map<String, Object> invoke(String action, Map<String, Object> payload,
                                      String tenantId, String userId) {
        log.info("[{}] action={} tenant={}", agentName(), action, tenantId);

        return switch (action.toUpperCase()) {

            case "COMPUTE_SCORE" -> {
                String entityId   = str(payload, "entityId");
                String entityType = str(payload, "entityType");
                double score = 72.5 + (entityId.hashCode() % 25); // deterministic mock
                score = Math.max(0, Math.min(100, score));
                yield result(
                    "entityId",   entityId,
                    "entityType", entityType.isEmpty() ? "USER" : entityType,
                    "trustScore", score,
                    "grade",      score >= 80 ? "A" : score >= 60 ? "B" : score >= 40 ? "C" : "D",
                    "computed",   true,
                    "modelVersion","trust-score-v2.1",
                    "computedAt", java.time.Instant.now().toString()
                );
            }

            case "GET_SCORE" -> {
                String entityId = str(payload, "entityId");
                yield result(
                    "entityId",    entityId,
                    "trustScore",  78.4,
                    "grade",       "B",
                    "percentile",  65,
                    "lastUpdated", "2025-01-01T10:00:00Z",
                    "platforms",   List.of("ECOSKILLER","INSTITUTE_ERP","DOJO")
                );
            }

            case "UPDATE_SIGNALS" -> {
                String entityId = str(payload, "entityId");
                @SuppressWarnings("unchecked")
                Map<String,Object> signals = payload != null
                    ? (Map<String,Object>) payload.getOrDefault("signals", Map.of())
                    : Map.of();
                yield result(
                    "entityId",       entityId,
                    "signalsIngested", signals.size(),
                    "newScore",        81.2,
                    "delta",           +2.8,
                    "updatedAt",       java.time.Instant.now().toString(),
                    "recalcScheduled", true
                );
            }

            case "COMPARE_ENTITIES" -> {
                @SuppressWarnings("unchecked")
                List<String> entityIds = payload != null
                    ? (List<String>) payload.getOrDefault("entityIds", List.of())
                    : List.of();
                List<Map<String,Object>> scores = new ArrayList<>();
                int rank = 1;
                for (String id : entityIds) {
                    double s = 50 + (Math.abs(id.hashCode()) % 50);
                    scores.add(Map.of("entityId",id,"score",s,"rank",rank++));
                }
                scores.sort((a,b)->Double.compare((double)b.get("score"),(double)a.get("score")));
                yield result(
                    "compared",  entityIds.size(),
                    "ranking",   scores,
                    "topEntity", scores.isEmpty() ? null : scores.get(0).get("entityId")
                );
            }

            case "EXPLAIN_SCORE" -> {
                String entityId = str(payload, "entityId");
                yield result(
                    "entityId", entityId,
                    "score",    78.4,
                    "breakdown", Map.of(
                        "identityVerification",  Map.of("weight","25%","contribution",20.0,"status","VERIFIED"),
                        "behaviorConsistency",   Map.of("weight","20%","contribution",15.5,"status","GOOD"),
                        "platformEngagement",    Map.of("weight","20%","contribution",16.0,"status","ACTIVE"),
                        "consentCompliance",     Map.of("weight","15%","contribution",13.0,"status","COMPLIANT"),
                        "vendorRelationships",   Map.of("weight","10%","contribution",7.5,"status","MODERATE"),
                        "disputeHistory",        Map.of("weight","10%","contribution",6.4,"status","CLEAN")
                    ),
                    "explanation","Score reflects strong identity and engagement with moderate vendor profile"
                );
            }

            default -> result(
                "error",   "Unknown action: " + action,
                "allowed", List.of("COMPUTE_SCORE","GET_SCORE","UPDATE_SIGNALS",
                                   "COMPARE_ENTITIES","EXPLAIN_SCORE")
            );
        };
    }
}
