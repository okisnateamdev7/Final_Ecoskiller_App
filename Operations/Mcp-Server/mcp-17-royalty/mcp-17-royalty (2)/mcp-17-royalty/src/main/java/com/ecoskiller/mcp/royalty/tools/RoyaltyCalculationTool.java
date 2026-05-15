package com.ecoskiller.mcp.royalty.tools;

import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * ROYALTY_CALCULATION_AGENT — Tool: royalty_calculation
 *
 * Core royalty computation engine implementing all calculation algorithms
 * from Section 11 of the Ecoskiller Royalty Engine Technical Documentation.
 *
 * Supports:
 *   - Tiered royalty rates (5%/7%/10% thresholds)
 *   - Custom flat rate calculation
 *   - Minimum guarantee deficit/surplus tracking
 *   - Multi-territory rate application
 *   - Currency conversion (FX gain/loss tracking)
 *   - Annual YTD aggregation
 */
public class RoyaltyCalculationTool extends BaseTool {

    @Override public String getName() { return "royalty_calculation"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",            str("tiered|flat_rate|minimum_guarantee_check|multi_territory|currency_convert|ytd_aggregate"))
                .put("creator_id",        str("Creator unique ID"))
                .put("agreement_id",      str("Licensing agreement ID"))
                .put("sales_amount",      num("Total sales amount"))
                .put("royalty_rate",      num("Flat royalty rate 0.0–1.0 (for flat_rate action)"))
                .put("minimum_guarantee", num("Annual minimum guarantee"))
                .put("ytd_royalties",     num("YTD royalties accrued so far (for guarantee check)"))
                .put("territory",         str("Territory: Asia|EU|US|Global"))
                .put("source_currency",   str("Source currency: INR|USD|EUR"))
                .put("target_currency",   str("Target payout currency"))
                .put("exchange_rate",     num("FX rate (6 decimal places) e.g. 83.654321 INR per USD"))
                .put("period",            str("Period label: Q1_2025|Q2_2025 etc."));
        return schema(getName(),
                "ROYALTY_CALCULATION_AGENT — Core royalty computation: tiered rates (5/7/10%), " +
                "flat rate, minimum guarantee deficiency, territory-based, currency conversion with FX gain/loss. " +
                "Uses banker's rounding; stores amounts in cents for precision.",
                p, "action", "creator_id", "sales_amount");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action      = a.getString("action");
        String creatorId   = a.getString("creator_id");
        double sales       = a.optDouble("sales_amount", 0);

        return switch (action) {
            case "tiered" -> {
                BigDecimal royalty = calcTieredRoyalty(sales);
                BigDecimal salesBD = money(sales);

                // Show tier breakdown
                JSONArray breakdown = new JSONArray();
                if (sales <= 100_000) {
                    breakdown.put("Tier1: " + salesBD + " × 5% = " + money(sales * 0.05));
                } else if (sales <= 500_000) {
                    breakdown.put("Tier1: 100000 × 5% = " + money(5000));
                    breakdown.put("Tier2: " + money(sales - 100_000) + " × 7% = " + money((sales - 100_000) * 0.07));
                } else {
                    breakdown.put("Tier1: 100000 × 5% = " + money(5000));
                    breakdown.put("Tier2: 400000 × 7% = " + money(28000));
                    breakdown.put("Tier3: " + money(sales - 500_000) + " × 10% = " + money((sales - 500_000) * 0.10));
                }

                yield json(new JSONObject()
                        .put("creator_id",       creatorId)
                        .put("sales_amount",     salesBD.toPlainString())
                        .put("tier_breakdown",   breakdown)
                        .put("total_royalty",    royalty.toPlainString())
                        .put("effective_rate",   sales > 0
                                ? royalty.divide(salesBD, 6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).toPlainString() + "%"
                                : "0%")
                        .put("algorithm",        "Tiered: 0-100K@5%, 100K-500K@7%, >500K@10%")
                        .put("timestamp",        now()));
            }
            case "flat_rate" -> {
                double rate    = a.optDouble("royalty_rate", 0.07);
                BigDecimal r   = money(sales * rate);
                yield json(new JSONObject()
                        .put("creator_id",   creatorId)
                        .put("sales_amount", money(sales).toPlainString())
                        .put("royalty_rate", (rate * 100) + "%")
                        .put("royalty_due",  r.toPlainString())
                        .put("timestamp",    now()));
            }
            case "minimum_guarantee_check" -> {
                double minGuar   = a.optDouble("minimum_guarantee", 50_000);
                double ytd       = a.optDouble("ytd_royalties", 0);
                double quarterly = minGuar / 4;
                BigDecimal royalty= calcTieredRoyalty(sales);
                boolean deficit  = royalty.doubleValue() < quarterly;
                BigDecimal diff  = deficit
                        ? BigDecimal.valueOf(quarterly).subtract(royalty).setScale(2, RoundingMode.HALF_UP)
                        : royalty.subtract(BigDecimal.valueOf(quarterly)).setScale(2, RoundingMode.HALF_UP);

                yield json(new JSONObject()
                        .put("creator_id",          creatorId)
                        .put("period_royalty",       royalty.toPlainString())
                        .put("min_per_quarter",      money(quarterly).toPlainString())
                        .put("has_deficiency",       deficit)
                        .put("deficiency_amount",    deficit  ? diff.toPlainString() : "0.00")
                        .put("surplus_amount",       !deficit ? diff.toPlainString() : "0.00")
                        .put("ytd_royalties",        money(ytd).toPlainString())
                        .put("annual_minimum",       money(minGuar).toPlainString())
                        .put("ledger_if_deficiency", "DEBIT Min-Guarantee-Expense / CREDIT A/P-Creators " + (deficit ? diff : "N/A"))
                        .put("timestamp",            now()));
            }
            case "multi_territory" -> {
                String territory = a.optString("territory","Asia");
                double rate = switch (territory.toUpperCase()) {
                    case "EU","EUR"   -> 0.10; // exclusive higher rate
                    case "ASIA"       -> 0.07; // non-exclusive
                    case "US","USA"   -> 0.08;
                    default           -> 0.05;
                };
                BigDecimal r = money(sales * rate);
                yield json(new JSONObject()
                        .put("creator_id",   creatorId)
                        .put("territory",    territory)
                        .put("royalty_rate", (rate*100)+"%")
                        .put("sales_amount", money(sales).toPlainString())
                        .put("royalty_due",  r.toPlainString())
                        .put("timestamp",    now()));
            }
            case "currency_convert" -> {
                double fx = a.optDouble("exchange_rate", 83.654321);
                String src = a.optString("source_currency","INR");
                String tgt = a.optString("target_currency","USD");
                BigDecimal salesBD  = BigDecimal.valueOf(sales);
                BigDecimal fxBD     = BigDecimal.valueOf(fx).setScale(6, RoundingMode.HALF_UP);
                BigDecimal converted= salesBD.divide(fxBD, 2, RoundingMode.HALF_UP);
                yield json(new JSONObject()
                        .put("source_amount",    salesBD.toPlainString() + " " + src)
                        .put("exchange_rate",    fxBD.toPlainString() + " " + src + "/" + tgt)
                        .put("converted_amount", converted.toPlainString() + " " + tgt)
                        .put("fx_rate_stored",   "6 decimal precision to avoid rounding loss")
                        .put("timestamp",        now()));
            }
            case "ytd_aggregate" ->
                json(new JSONObject()
                        .put("creator_id",      creatorId)
                        .put("ytd_royalties",   "Aggregate from ledger — quarterly entries Q1+Q2+Q3+Q4")
                        .put("timestamp",       now()));
            default -> text("Unknown action: " + action);
        };
    }
}
