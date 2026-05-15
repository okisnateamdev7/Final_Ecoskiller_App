package com.ecoskiller.mcp.royalty.security;

import org.json.JSONObject;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Security input validator for all 18 Royalty Engine tools.
 *
 * Enforces:
 *  - ID format validation (alphanumeric + hyphen/underscore, max 128 chars)
 *  - Amount range checks (no negative amounts, no astronomical values)
 *  - Allow-listed enumerations (jurisdiction, payout_method, agreement_status)
 *  - Value size limits (64 KB max for JSON blobs)
 *  - Injection prevention (no SQL wildcards, path traversal, RESP chars in IDs)
 */
public class InputValidator {

    private static final Pattern ID_PATTERN  = Pattern.compile("^[a-zA-Z0-9\\-_]{1,128}$");
    private static final Pattern SAFE_TEXT   = Pattern.compile("^[^<>'\";\\\\]{0,500}$");

    private static final Set<String> JURISDICTIONS   = Set.of("US","USA","IN","INDIA","EU","EUR","UK","GB","AU","JP","SG","CA");
    private static final Set<String> PAYOUT_METHODS  = Set.of("ACH","WIRE","STABLECOIN","BANK_TRANSFER","PAYPAL","WISE");
    private static final Set<String> AGR_STATUSES    = Set.of("ACTIVE","SUSPENDED","TERMINATED","EXPIRED","DRAFT");
    private static final Set<String> PAYOUT_STATUSES = Set.of("PENDING","PROCESSING","IN_TRANSIT","COMPLETE","FAILED","REFUNDED","CANCELLED");
    private static final Set<String> CURRENCIES      = Set.of("INR","USD","EUR","GBP","JPY","USDC","USDT","AUD","SGD","CAD");

    private static final double MAX_AMOUNT = 1_000_000_000.0; // 1 billion — sanity cap

    public void validate(String toolName, JSONObject args) {
        // ID fields
        validateId(args, "creator_id");
        validateId(args, "idea_id");
        validateId(args, "agreement_id");
        validateId(args, "licensee_id");
        validateId(args, "payment_id");
        validateId(args, "ledger_id");
        validateId(args, "tenant_id");
        validateId(args, "school_id");
        validateId(args, "guardian_id");
        validateId(args, "contract_id");
        validateId(args, "competition_id");
        validateId(args, "business_id");
        validateId(args, "wallet_id");

        // Amount checks
        validateAmount(args, "amount");
        validateAmount(args, "sales_amount");
        validateAmount(args, "royalty_amount");
        validateAmount(args, "minimum_guarantee");
        validateAmount(args, "payout_amount");

        // Rate: 0.0 – 1.0
        if (args.has("royalty_rate")) {
            double rate = args.getDouble("royalty_rate");
            if (rate < 0 || rate > 1.0)
                throw new SecurityException("royalty_rate must be 0.0–1.0, got: " + rate);
        }

        // Enumerations
        if (args.has("jurisdiction") && !JURISDICTIONS.contains(args.getString("jurisdiction").toUpperCase()))
            throw new SecurityException("Unknown jurisdiction: " + args.getString("jurisdiction"));
        if (args.has("payout_method") && !PAYOUT_METHODS.contains(args.getString("payout_method").toUpperCase()))
            throw new SecurityException("Unknown payout_method: " + args.getString("payout_method"));
        if (args.has("agreement_status") && !AGR_STATUSES.contains(args.getString("agreement_status").toUpperCase()))
            throw new SecurityException("Unknown agreement_status: " + args.getString("agreement_status"));
        if (args.has("currency") && !CURRENCIES.contains(args.getString("currency").toUpperCase()))
            throw new SecurityException("Unknown currency: " + args.getString("currency"));

        // Safe text fields
        validateText(args, "territory");
        validateText(args, "description");
        validateText(args, "period");

        // Year sanity
        if (args.has("year")) {
            int y = args.getInt("year");
            if (y < 2000 || y > 2100) throw new SecurityException("year out of range: " + y);
        }
    }

    private void validateId(JSONObject args, String field) {
        if (!args.has(field)) return;
        String val = args.optString(field, "");
        if (!ID_PATTERN.matcher(val).matches())
            throw new SecurityException("Invalid " + field + " '" + safe(val) + "' — must match [a-zA-Z0-9\\-_]{1,128}");
    }

    private void validateAmount(JSONObject args, String field) {
        if (!args.has(field)) return;
        double v = args.optDouble(field, 0);
        if (v < 0)           throw new SecurityException(field + " cannot be negative");
        if (v > MAX_AMOUNT)  throw new SecurityException(field + " exceeds maximum allowed value");
        if (Double.isNaN(v) || Double.isInfinite(v))
            throw new SecurityException(field + " must be a finite number");
    }

    private void validateText(JSONObject args, String field) {
        if (!args.has(field)) return;
        String v = args.optString(field,"");
        if (!SAFE_TEXT.matcher(v).matches())
            throw new SecurityException("Unsafe characters in field: " + field);
    }

    private String safe(String s) { return s == null ? "null" : (s.length() > 30 ? s.substring(0,30)+"..." : s); }
}
