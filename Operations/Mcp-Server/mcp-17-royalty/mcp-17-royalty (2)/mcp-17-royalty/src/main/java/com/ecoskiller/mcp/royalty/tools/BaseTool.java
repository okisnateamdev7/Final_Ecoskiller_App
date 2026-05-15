package com.ecoskiller.mcp.royalty.tools;

import com.ecoskiller.mcp.royalty.McpTool;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Base for all 18 Royalty Engine MCP tools.
 * Provides schema helpers, response builders, and financial math utilities.
 */
public abstract class BaseTool implements McpTool {

    // ─── Schema builders ─────────────────────────────────────────────────────────

    protected JSONObject schema(String name, String desc, JSONObject props, String... required) {
        JSONObject inputSchema = new JSONObject().put("type","object").put("properties", props);
        if (required.length > 0) {
            JSONArray req = new JSONArray();
            for (String r : required) req.put(r);
            inputSchema.put("required", req);
        }
        return new JSONObject().put("name", name).put("description", desc).put("inputSchema", inputSchema);
    }

    protected JSONObject str(String desc)  { return new JSONObject().put("type","string").put("description",desc); }
    protected JSONObject num(String desc)  { return new JSONObject().put("type","number").put("description",desc); }
    protected JSONObject intP(String desc) { return new JSONObject().put("type","integer").put("description",desc); }
    protected JSONObject bool(String desc) { return new JSONObject().put("type","boolean").put("description",desc); }

    // ─── Response builders ────────────────────────────────────────────────────────

    protected JSONObject text(String msg) {
        return new JSONObject().put("content",
                new JSONArray().put(new JSONObject().put("type","text").put("text", msg)));
    }

    protected JSONObject json(JSONObject data) { return text(data.toString(2)); }

    // ─── Financial math ───────────────────────────────────────────────────────────

    /** Round to 2 decimal places using HALF_UP (monetary) */
    protected BigDecimal money(double d) {
        return BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP);
    }

    /** Tiered royalty calculation per spec Section 11 */
    protected BigDecimal calcTieredRoyalty(double salesAmount) {
        BigDecimal sales = BigDecimal.valueOf(salesAmount);
        BigDecimal royalty = BigDecimal.ZERO;
        // Tier 1: 0–100K @ 5%
        BigDecimal t1 = BigDecimal.valueOf(100_000);
        // Tier 2: 100K–500K @ 7%
        BigDecimal t2 = BigDecimal.valueOf(500_000);
        if (sales.compareTo(t1) <= 0) {
            royalty = sales.multiply(BigDecimal.valueOf(0.05));
        } else if (sales.compareTo(t2) <= 0) {
            royalty = t1.multiply(BigDecimal.valueOf(0.05))
                    .add(sales.subtract(t1).multiply(BigDecimal.valueOf(0.07)));
        } else {
            royalty = t1.multiply(BigDecimal.valueOf(0.05))
                    .add(t2.subtract(t1).multiply(BigDecimal.valueOf(0.07)))
                    .add(sales.subtract(t2).multiply(BigDecimal.valueOf(0.10)));
        }
        return royalty.setScale(2, RoundingMode.HALF_UP);
    }

    /** Tax withholding per jurisdiction (Section 13) */
    protected BigDecimal withholdingRate(String jurisdiction, boolean taxIdProvided) {
        return switch (jurisdiction.toUpperCase()) {
            case "US","USA"  -> taxIdProvided ? BigDecimal.ZERO : BigDecimal.valueOf(0.24);
            case "IN","INDIA"-> taxIdProvided ? BigDecimal.valueOf(0.10) : BigDecimal.valueOf(0.20);
            case "EU","EUR"  -> BigDecimal.ZERO; // B2B reverse charge
            default          -> BigDecimal.valueOf(0.10); // safe default
        };
    }

    /** Timestamp in ISO-8601 */
    protected String now() { return java.time.Instant.now().toString(); }
}
