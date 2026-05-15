package com.ecoskiller.mcp.corporate.tools;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

/**
 * ASSET_AGENT_ANTIGRAVITY_ERP
 *
 * <p>Fixed-asset lifecycle. Disposal and revaluation require a CFO-level
 * approval token (ANTIGRAVITY_SEALED constraint).</p>
 */
@Slf4j
@Component
public class AssetTool implements AgentTool {

    private static final Set<String> CFO_OPS = Set.of("dispose_asset", "revalue_asset");

    @Override
    public String execute(JsonNode args) throws Exception {
        String tenantId = getString(args, "tenant_id");
        String action   = getString(args, "action");
        String assetId  = getString(args, "asset_id");
        String cfoToken = getString(args, "cfo_token");
        String period   = getString(args, "fiscal_period");

        log.info("[ASSET] tenant={} action={} asset={}", tenantId, action, assetId);

        if (CFO_OPS.contains(action) && cfoToken.isBlank()) {
            return sealed("Action '" + action + "' requires cfo_token. " +
                          "Obtain CFO approval before proceeding.");
        }

        return switch (action) {
            case "register_asset"         -> ok("register_asset",
                "Asset registered.\nasset_id: " + UUID.randomUUID());
            case "update_asset"           -> ok("update_asset",
                "Asset record updated for asset_id=" + assetId);
            case "run_depreciation"       -> queued("run_depreciation", UUID.randomUUID().toString(),
                "Period: " + period + "\nAsset scope: all active assets");
            case "revalue_asset"          -> ok("revalue_asset",
                "Revaluation applied for asset_id=" + assetId +
                "\nCFO approved by token: " + cfoToken.substring(0, Math.min(8, cfoToken.length())) + "…");
            case "dispose_asset"          -> ok("dispose_asset",
                "Asset disposed. Gain/loss journal created.\ndisposal_id: " + UUID.randomUUID());
            case "transfer_asset"         -> ok("transfer_asset",
                "Asset transferred to new cost centre.");
            case "get_asset_register"     -> queued("get_asset_register", UUID.randomUUID().toString(),
                "Format: detailed register");
            case "get_depreciation_schedule" -> ok("get_depreciation_schedule",
                "Schedule URL: /api/asset/" + assetId + "/depreciation-schedule");
            case "insure_asset"           -> ok("insure_asset",
                "Insurance policy linked to asset_id=" + assetId);
            default -> "Unknown action: " + action;
        };
    }
}
