package com.ecoskiller.unleash.model;

import java.time.Instant;
import java.util.*;

/**
 * FeatureFlag — full domain model for an Ecoskiller Unleash feature flag.
 *
 * Mirrors the Unleash REST API flag structure and evaluation logic:
 *   - Strategies: STANDARD, USERID, ORGANIZATION, ROLE_BASED, GRADUAL_ROLLOUT, CUSTOM_CONSTRAINT
 *   - Variants for A/B testing (weighted traffic splitting)
 *   - Immutable audit log (every change recorded)
 *   - Evaluation metrics (count, enabled%, variant distribution)
 *   - Lifecycle states: ACTIVE → PENDING_CLEANUP → ARCHIVED
 */
public class FeatureFlag {

    public enum Strategy {
        STANDARD, USERID, ORGANIZATION, ROLE_BASED, GRADUAL_ROLLOUT, CUSTOM_CONSTRAINT
    }

    public enum LifecycleState {
        ACTIVE, PENDING_CLEANUP, ARCHIVED
    }

    // ── Core fields ────────────────────────────────────────────────────────────
    private final String name;
    private final String description;
    private boolean enabled;
    private Strategy strategy;
    private int rolloutPercentage;
    private final List<String> targetUserIds = new ArrayList<>();
    private final List<String> targetOrgIds  = new ArrayList<>();
    private final List<String> targetRoles   = new ArrayList<>();
    private final List<Variant> variants     = new ArrayList<>();
    private final List<AuditEntry> auditLog  = new ArrayList<>();
    private long evaluationCount;
    private long enabledCount;
    private LifecycleState lifecycleState;
    private String environment;
    private String ownerTeam;
    private final Instant createdAt;
    private Instant updatedAt;

    public FeatureFlag(String name, String description, String environment, String ownerTeam) {
        this.name           = name;
        this.description    = description;
        this.environment    = environment;
        this.ownerTeam      = ownerTeam;
        this.enabled        = false;            // safe default: always start disabled
        this.strategy       = Strategy.STANDARD;
        this.rolloutPercentage = 0;
        this.lifecycleState = LifecycleState.ACTIVE;
        this.createdAt      = Instant.now();
        this.updatedAt      = Instant.now();
    }

    // ── Getters ────────────────────────────────────────────────────────────────
    public String          getName()              { return name; }
    public String          getDescription()       { return description; }
    public boolean         isEnabled()            { return enabled; }
    public Strategy        getStrategy()          { return strategy; }
    public int             getRolloutPercentage() { return rolloutPercentage; }
    public List<String>    getTargetUserIds()     { return Collections.unmodifiableList(targetUserIds); }
    public List<String>    getTargetOrgIds()      { return Collections.unmodifiableList(targetOrgIds); }
    public List<String>    getTargetRoles()       { return Collections.unmodifiableList(targetRoles); }
    public List<Variant>   getVariants()          { return Collections.unmodifiableList(variants); }
    public List<AuditEntry> getAuditLog()         { return Collections.unmodifiableList(auditLog); }
    public long            getEvaluationCount()   { return evaluationCount; }
    public long            getEnabledCount()      { return enabledCount; }
    public LifecycleState  getLifecycleState()    { return lifecycleState; }
    public String          getEnvironment()       { return environment; }
    public String          getOwnerTeam()         { return ownerTeam; }
    public Instant         getCreatedAt()         { return createdAt; }
    public Instant         getUpdatedAt()         { return updatedAt; }

    // ── Mutators ───────────────────────────────────────────────────────────────
    public void setEnabled(boolean v)              { enabled = v;   touch(); }
    public void setStrategy(Strategy s)            { strategy = s;  touch(); }
    public void setLifecycleState(LifecycleState s){ lifecycleState = s; touch(); }

    public void setRolloutPercentage(int pct) {
        if (pct < 0 || pct > 100)
            throw new IllegalArgumentException("Rollout % must be 0-100, got: " + pct);
        rolloutPercentage = pct;
        touch();
    }

    public void addTargetUserId(String id)  { targetUserIds.add(id); touch(); }
    public void addTargetOrgId(String id)   { targetOrgIds.add(id);  touch(); }
    public void addTargetRole(String role)  { targetRoles.add(role); touch(); }
    public void clearTargetUserIds()        { targetUserIds.clear(); touch(); }
    public void clearTargetOrgIds()         { targetOrgIds.clear();  touch(); }
    public void clearTargetRoles()          { targetRoles.clear();   touch(); }
    public void addVariant(Variant v)       { variants.add(v);       touch(); }
    public void clearVariants()             { variants.clear();      touch(); }

    public void recordAudit(String user, String action, String delta) {
        auditLog.add(new AuditEntry(user, action, delta));
        touch();
    }

    public void recordEvaluation(boolean wasEnabled) {
        evaluationCount++;
        if (wasEnabled) enabledCount++;
    }

    private void touch() { updatedAt = Instant.now(); }

    // ── Evaluation engine ──────────────────────────────────────────────────────

    /**
     * Evaluate whether this flag is enabled for a request context.
     * Context keys: userId, orgId, role, sessionId, ipAddress
     *
     * Mirrors Unleash SDK isEnabled(flagName, context) — typical latency < 5ms.
     */
    public boolean evaluate(Map<String, String> ctx) {
        if (!enabled) return false;
        switch (strategy) {
            case STANDARD:
                return true;
            case USERID:
                String uid = ctx.get("userId");
                return uid != null && targetUserIds.contains(uid);
            case ORGANIZATION:
                String oid = ctx.get("orgId");
                return oid != null && targetOrgIds.contains(oid);
            case ROLE_BASED:
                String role = ctx.get("role");
                return role != null && targetRoles.contains(role.toUpperCase());
            case GRADUAL_ROLLOUT:
                // Deterministic: same user/session always gets same result
                String seed = ctx.getOrDefault("sessionId",
                              ctx.getOrDefault("userId", UUID.randomUUID().toString()));
                return (Math.abs(seed.hashCode()) % 100) < rolloutPercentage;
            case CUSTOM_CONSTRAINT:
                // AND-logic: all configured targets must match
                String cu = ctx.get("userId");
                String co = ctx.get("orgId");
                String cr = ctx.get("role");
                boolean um = targetUserIds.isEmpty() || (cu != null && targetUserIds.contains(cu));
                boolean om = targetOrgIds.isEmpty()  || (co != null && targetOrgIds.contains(co));
                boolean rm = targetRoles.isEmpty()   || (cr != null && targetRoles.contains(cr.toUpperCase()));
                return um && om && rm;
            default:
                return false;
        }
    }

    /**
     * Select which A/B variant to assign based on context.
     * Deterministic hash ensures same user always sees same variant.
     */
    public String selectVariant(Map<String, String> ctx) {
        if (variants.isEmpty()) return "control";
        int total = variants.stream().mapToInt(Variant::getWeight).sum();
        if (total == 0) return "control";
        String seed = ctx.getOrDefault("userId",
                      ctx.getOrDefault("sessionId", UUID.randomUUID().toString()));
        int h = Math.abs(seed.hashCode()) % total;
        int cumulative = 0;
        for (Variant v : variants) {
            cumulative += v.getWeight();
            if (h < cumulative) return v.getName();
        }
        return variants.get(variants.size() - 1).getName();
    }

    // ── Inner classes ──────────────────────────────────────────────────────────

    /** Immutable audit log entry — never deleted (compliance requirement). */
    public static class AuditEntry {
        public final String  user;
        public final String  action;
        public final String  delta;
        public final Instant timestamp;

        public AuditEntry(String user, String action, String delta) {
            this.user      = user;
            this.action    = action;
            this.delta     = delta;
            this.timestamp = Instant.now();
        }
    }

    /** A/B test variant with weighted traffic allocation. */
    public static class Variant {
        private final String name;
        private final int    weight;
        private final String payload;

        public Variant(String name, int weight, String payload) {
            this.name    = name;
            this.weight  = weight;
            this.payload = payload == null ? "" : payload;
        }

        public String getName()    { return name;    }
        public int    getWeight()  { return weight;  }
        public String getPayload() { return payload; }
    }
}
