package com.ecoskiller.unleash.model;

import java.time.Instant;
import java.util.*;

/**
 * FeatureFlag — Ecoskiller Unleash domain model.
 *
 * Supports all patterns from the technical documentation:
 *   Strategies  : DEFAULT, USERID, TENANT, GROUP, GRADUAL_ROLLOUT,
 *                 CUSTOM_CONSTRAINT, SCHEDULED
 *   Variants    : weighted A/B arms (Control / Variant A / B / C)
 *   Lifecycle   : ACTIVE → DEPRECATED → ARCHIVED
 *   Audit       : immutable log, every change captured
 *   Multi-tenant: flags scoped per tenant (tenant-001-new-ui, etc.)
 *   Scheduling  : enable_at / disable_at timestamps
 *   Metrics     : evaluation count, enabled count, variant distribution
 */
public class FeatureFlag {

    // ── Enumerations ──────────────────────────────────────────────────────────

    public enum Strategy {
        DEFAULT,           // Simple global ON/OFF
        USERID,            // Target specific user IDs or emails
        TENANT,            // Target by tenant_id or tenant_plan
        GROUP,             // Target by user group / segment
        GRADUAL_ROLLOUT,   // hash(user_id) % 100 < rollout_pct
        CUSTOM_CONSTRAINT, // Composite: tenant AND group AND attribute
        SCHEDULED          // Time-window based activation
    }

    public enum LifecycleState { ACTIVE, DEPRECATED, ARCHIVED }

    public enum TenantPlan { FREE, PRO, ENTERPRISE, ANY }

    // ── Core fields ───────────────────────────────────────────────────────────
    private final String  name;               // e.g. scoring-ai-v2-experiment
    private final String  description;
    private       boolean enabled;
    private       Strategy strategy;
    private       LifecycleState lifecycleState;
    private       String  ownerTeam;          // e.g. scoring-team, ui-team
    private       String  environment;        // dev / test / staging / production
    private       String  tenantScope;        // null = all tenants, else tenant-001
    private       int     rolloutPercentage;  // 0-100 for GRADUAL_ROLLOUT
    private       Instant enableAt;           // SCHEDULED: when to enable
    private       Instant disableAt;          // SCHEDULED: when to disable

    // ── Targeting lists ───────────────────────────────────────────────────────
    private final List<String>  targetUserIds   = new ArrayList<>();
    private final List<String>  targetTenantIds = new ArrayList<>();
    private final List<String>  targetGroups    = new ArrayList<>();
    private final List<String>  customRules     = new ArrayList<>(); // expression strings
    private       TenantPlan    requiredPlan    = TenantPlan.ANY;

    // ── A/B variants ──────────────────────────────────────────────────────────
    private final List<Variant> variants = new ArrayList<>();

    // ── Immutable audit log ───────────────────────────────────────────────────
    private final List<AuditEntry> auditLog = new ArrayList<>();

    // ── Metrics ───────────────────────────────────────────────────────────────
    private long evaluationCount;
    private long enabledCount;
    private final Map<String, Long> variantCounts = new LinkedHashMap<>();

    // ── Timestamps ────────────────────────────────────────────────────────────
    private final Instant createdAt;
    private       Instant updatedAt;

    public FeatureFlag(String name, String description, String environment, String ownerTeam) {
        this.name           = name;
        this.description    = description;
        this.environment    = environment;
        this.ownerTeam      = ownerTeam;
        this.enabled        = false;   // ALWAYS start disabled — safe default
        this.strategy       = Strategy.DEFAULT;
        this.lifecycleState = LifecycleState.ACTIVE;
        this.rolloutPercentage = 0;
        this.createdAt      = Instant.now();
        this.updatedAt      = Instant.now();
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String          getName()               { return name; }
    public String          getDescription()        { return description; }
    public boolean         isEnabled()             { return enabled; }
    public Strategy        getStrategy()           { return strategy; }
    public LifecycleState  getLifecycleState()     { return lifecycleState; }
    public String          getOwnerTeam()          { return ownerTeam; }
    public String          getEnvironment()        { return environment; }
    public String          getTenantScope()        { return tenantScope; }
    public int             getRolloutPercentage()  { return rolloutPercentage; }
    public Instant         getEnableAt()           { return enableAt; }
    public Instant         getDisableAt()          { return disableAt; }
    public List<String>    getTargetUserIds()      { return Collections.unmodifiableList(targetUserIds); }
    public List<String>    getTargetTenantIds()    { return Collections.unmodifiableList(targetTenantIds); }
    public List<String>    getTargetGroups()       { return Collections.unmodifiableList(targetGroups); }
    public List<String>    getCustomRules()        { return Collections.unmodifiableList(customRules); }
    public TenantPlan      getRequiredPlan()       { return requiredPlan; }
    public List<Variant>   getVariants()           { return Collections.unmodifiableList(variants); }
    public List<AuditEntry> getAuditLog()          { return Collections.unmodifiableList(auditLog); }
    public long            getEvaluationCount()    { return evaluationCount; }
    public long            getEnabledCount()       { return enabledCount; }
    public Map<String,Long> getVariantCounts()     { return Collections.unmodifiableMap(variantCounts); }
    public Instant         getCreatedAt()          { return createdAt; }
    public Instant         getUpdatedAt()          { return updatedAt; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setEnabled(boolean v)               { enabled = v; touch(); }
    public void setStrategy(Strategy s)             { strategy = s; touch(); }
    public void setLifecycleState(LifecycleState s) { lifecycleState = s; touch(); }
    public void setOwnerTeam(String t)              { ownerTeam = t; touch(); }
    public void setTenantScope(String t)            { tenantScope = t; touch(); }
    public void setRequiredPlan(TenantPlan p)       { requiredPlan = p; touch(); }
    public void setEnableAt(Instant t)              { enableAt = t; touch(); }
    public void setDisableAt(Instant t)             { disableAt = t; touch(); }

    public void setRolloutPercentage(int pct) {
        if (pct < 0 || pct > 100) throw new IllegalArgumentException("Rollout % must be 0-100, got: " + pct);
        rolloutPercentage = pct; touch();
    }

    public void addTargetUserId(String id)   { targetUserIds.add(id); touch(); }
    public void addTargetTenantId(String id) { targetTenantIds.add(id); touch(); }
    public void addTargetGroup(String g)     { targetGroups.add(g); touch(); }
    public void addCustomRule(String r)      { customRules.add(r); touch(); }
    public void clearTargetUserIds()         { targetUserIds.clear(); touch(); }
    public void clearTargetTenantIds()       { targetTenantIds.clear(); touch(); }
    public void clearTargetGroups()          { targetGroups.clear(); touch(); }
    public void clearCustomRules()           { customRules.clear(); touch(); }
    public void addVariant(Variant v)        { variants.add(v); variantCounts.put(v.getName(), 0L); touch(); }
    public void clearVariants()              { variants.clear(); variantCounts.clear(); touch(); }

    public void recordAudit(String user, String action, String delta) {
        auditLog.add(new AuditEntry(user, action, delta)); touch();
    }

    public void recordEvaluation(boolean wasEnabled, String variant) {
        evaluationCount++;
        if (wasEnabled) {
            enabledCount++;
            if (variant != null) variantCounts.merge(variant, 1L, Long::sum);
        }
    }

    private void touch() { updatedAt = Instant.now(); }

    // ── Evaluation engine ──────────────────────────────────────────────────────
    /**
     * Evaluate flag for a given context.
     * Context keys: userId, tenantId, group, plan, environment, hour, email
     *
     * Implements Ecoskiller's consistent-hashing rollout:
     *   hash(userId) % 100 < rolloutPercentage → enabled
     */
    public boolean evaluate(Map<String, String> ctx) {
        if (!enabled) return false;

        // Check SCHEDULED constraints first
        if (strategy == Strategy.SCHEDULED) {
            Instant now = Instant.now();
            if (enableAt  != null && now.isBefore(enableAt))  return false;
            if (disableAt != null && now.isAfter(disableAt))  return false;
        }

        // Tenant plan gate (multi-tenant monetization)
        if (requiredPlan != TenantPlan.ANY) {
            String plan = ctx.getOrDefault("plan", "free").toUpperCase();
            if (!planMeetsRequirement(plan)) return false;
        }

        switch (strategy) {
            case DEFAULT:
                return true;

            case USERID: {
                String uid = ctx.get("userId");
                String email = ctx.get("email");
                return (uid != null && targetUserIds.contains(uid)) ||
                       (email != null && targetUserIds.contains(email));
            }

            case TENANT: {
                String tid = ctx.get("tenantId");
                return tid != null && targetTenantIds.contains(tid);
            }

            case GROUP: {
                String grp = ctx.get("group");
                return grp != null && targetGroups.contains(grp);
            }

            case GRADUAL_ROLLOUT: {
                // Consistent hashing: same user always sees same result
                String seed = ctx.getOrDefault("userId",
                              ctx.getOrDefault("sessionId", UUID.randomUUID().toString()));
                return (Math.abs(seed.hashCode()) % 100) < rolloutPercentage;
            }

            case CUSTOM_CONSTRAINT: {
                // AND-logic: user OR tenant OR group match, all configured lists must have a hit
                String uid = ctx.get("userId");
                String tid = ctx.get("tenantId");
                String grp = ctx.get("group");
                boolean um = targetUserIds.isEmpty()   || (uid != null && targetUserIds.contains(uid));
                boolean tm = targetTenantIds.isEmpty() || (tid != null && targetTenantIds.contains(tid));
                boolean gm = targetGroups.isEmpty()    || (grp != null && targetGroups.contains(grp));
                return um && tm && gm;
            }

            case SCHEDULED:
                return true; // time gate already checked above

            default:
                return false;
        }
    }

    /** Deterministic variant assignment — same userId → same variant always (no flickering). */
    public String selectVariant(Map<String, String> ctx) {
        if (variants.isEmpty()) return "control";
        int total = variants.stream().mapToInt(Variant::getWeight).sum();
        if (total == 0) return "control";
        String seed = ctx.getOrDefault("userId",
                      ctx.getOrDefault("sessionId", UUID.randomUUID().toString()));
        int h = Math.abs(seed.hashCode()) % total;
        int cum = 0;
        for (Variant v : variants) {
            cum += v.getWeight();
            if (h < cum) return v.getName();
        }
        return variants.get(variants.size() - 1).getName();
    }

    private boolean planMeetsRequirement(String userPlan) {
        switch (requiredPlan) {
            case FREE:       return true;
            case PRO:        return "PRO".equals(userPlan) || "ENTERPRISE".equals(userPlan);
            case ENTERPRISE: return "ENTERPRISE".equals(userPlan);
            default:         return true;
        }
    }

    // ── Inner types ───────────────────────────────────────────────────────────

    /** A/B test variant with weight and optional payload config. */
    public static class Variant {
        private final String name;
        private final int    weight;
        private final String payload;
        private final String description;

        public Variant(String name, int weight, String payload, String description) {
            this.name        = name;
            this.weight      = weight;
            this.payload     = payload == null ? "" : payload;
            this.description = description == null ? "" : description;
        }

        public String getName()        { return name; }
        public int    getWeight()      { return weight; }
        public String getPayload()     { return payload; }
        public String getDescription() { return description; }
    }

    /** Immutable audit entry — never deleted (compliance requirement). */
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
}
