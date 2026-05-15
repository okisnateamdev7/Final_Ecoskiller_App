package com.ecoskiller.mcp.licensing.tools;

import com.ecoskiller.mcp.licensing.security.SecurityManager;
import org.json.JSONObject;

public class RoyaltyRateValidationTool extends BaseTool {
    public RoyaltyRateValidationTool(SecurityManager security) { super(security); }

    private double getEnvDouble(String key, double def) {
        try { return Double.parseDouble(System.getenv(key)); } catch (Exception e) { return def; }
    }
    private int getEnvInt(String key, int def) {
        try { return Integer.parseInt(System.getenv(key)); } catch (Exception e) { return def; }
    }

    @Override
    public String execute(JSONObject args) throws Exception {
        return switch (require(args, "action")) {
            case "validate"    -> validate(args);
            case "get_config"  -> getConfig(args);
            case "set_config"  -> setConfig(args);
            default -> throw new IllegalArgumentException("Unknown action. Supported: validate | get_config | set_config");
        };
    }

    private String validate(JSONObject args) {
        double minRate  = getEnvDouble("DEFAULT_MIN_ROYALTY_RATE", 0.0001);
        double maxRate  = getEnvDouble("DEFAULT_MAX_ROYALTY_RATE", 0.0005);
        int    minYears = getEnvInt("DEFAULT_MIN_TERM_YEARS", 10);
        int    maxYears = getEnvInt("DEFAULT_MAX_TERM_YEARS", 15);

        double rate  = args.optDouble("proposed_rate", -1);
        int    years = args.optInt("proposed_term_years", -1);

        boolean rateValid  = rate  >= minRate && rate  <= maxRate;
        boolean yearsValid = years >= minYears && years <= maxYears;

        return ok(new JSONObject()
            .put("rate_valid",      rate < 0 ? "not_provided" : rateValid)
            .put("years_valid",     years < 0 ? "not_provided" : yearsValid)
            .put("proposed_rate",   rate)
            .put("proposed_years",  years)
            .put("min_rate",        minRate)
            .put("max_rate",        maxRate)
            .put("min_years",       minYears)
            .put("max_years",       maxYears)
            .put("validation_source","royalty.platform_rate_config + environment ConfigMap")
            .put("simulation",      simulationNote()));
    }

    private String getConfig(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_VIEWER, SecurityManager.ROLE_CREATE, SecurityManager.ROLE_ADMIN);
        String tenantId = security.sanitiseText(opt(args, "tenant_id", "default"), "tenant_id");

        return ok(new JSONObject()
            .put("tenant_id",        tenantId)
            .put("min_royalty_rate", getEnvDouble("DEFAULT_MIN_ROYALTY_RATE", 0.0001))
            .put("max_royalty_rate", getEnvDouble("DEFAULT_MAX_ROYALTY_RATE", 0.0005))
            .put("min_term_years",   getEnvInt("DEFAULT_MIN_TERM_YEARS", 10))
            .put("max_term_years",   getEnvInt("DEFAULT_MAX_TERM_YEARS", 15))
            .put("cache_key",        "contract_rate_config_cache:" + tenantId)
            .put("cache_ttl_seconds",600)
            .put("simulation",       simulationNote()));
    }

    private String setConfig(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_ADMIN);
        String tenantId = security.sanitiseText(require(args, "tenant_id"), "tenant_id");
        double minRate  = args.optDouble("min_rate", getEnvDouble("DEFAULT_MIN_ROYALTY_RATE", 0.0001));
        double maxRate  = args.optDouble("max_rate", getEnvDouble("DEFAULT_MAX_ROYALTY_RATE", 0.0005));
        int    minYears = args.optInt("min_term_years", getEnvInt("DEFAULT_MIN_TERM_YEARS", 10));
        int    maxYears = args.optInt("max_term_years", getEnvInt("DEFAULT_MAX_TERM_YEARS", 15));

        if (minRate >= maxRate) throw new IllegalArgumentException("min_rate must be less than max_rate");
        if (minYears >= maxYears) throw new IllegalArgumentException("min_term_years must be less than max_term_years");

        LOG.info("[RoyaltyRate] set_config tenant=" + tenantId + " rate=[" + minRate + "," + maxRate + "]");

        return ok(new JSONObject()
            .put("tenant_id",        tenantId)
            .put("min_royalty_rate", minRate)
            .put("max_royalty_rate", maxRate)
            .put("min_term_years",   minYears)
            .put("max_term_years",   maxYears)
            .put("note",             "Config saved to royalty.platform_rate_config; Redis cache invalidated")
            .put("applies_to",       "New contracts only — existing ACTIVE contracts unaffected")
            .put("simulation",       simulationNote()));
    }
}
