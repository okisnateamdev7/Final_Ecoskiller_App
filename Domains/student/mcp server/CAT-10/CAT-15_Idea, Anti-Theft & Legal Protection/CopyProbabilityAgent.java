package com.ecoskiller.mcp15;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.ecoskiller.mcp15.AgentUtils.*;

/**
 * COPY_PROBABILITY
 *
 * Estimates the probability that a target idea is a deliberate copy of an
 * original, combining similarity score, temporal delta, authorship overlap,
 * and behavioural signals into a single probabilistic verdict.
 */
public class CopyProbabilityAgent implements AgentHandler {

    @Override
    public ObjectNode toolDefinition(String toolName) {
        ObjectNode schema = schemaObject("original_content", "suspect_content");
        addStringProp(schema, "original_content",   "The original protected content/idea");
        addStringProp(schema, "suspect_content",     "The suspect/derivative content to evaluate");
        addStringProp(schema, "original_id",         "ID of the original idea (optional)");
        addStringProp(schema, "suspect_id",          "ID of the suspect idea (optional)");
        addStringProp(schema, "original_created_at", "ISO timestamp of original creation (optional)");
        addStringProp(schema, "suspect_created_at",  "ISO timestamp of suspect creation (optional)");
        addStringProp(schema, "original_author_id",  "Author ID of original (optional)");
        addStringProp(schema, "suspect_author_id",   "Author ID of suspect (optional)");
        addNumberProp(schema, "access_count",        "Number of times suspect user accessed original (optional)");

        return buildToolDef(
            toolName,
            "COPY_PROBABILITY — Estimates the probability (0–100%) that a suspect idea is a " +
            "deliberate copy of an original using multi-signal Bayesian-style scoring: " +
            "content similarity, temporal proximity, authorship patterns, and access history.",
            schema
        );
    }

    @Override
    public ObjectNode execute(JsonNode args) {
        String origContent  = args.path("original_content").asText();
        String suspContent  = args.path("suspect_content").asText();
        String originalId   = args.path("original_id").asText("ORIGINAL");
        String suspectId    = args.path("suspect_id").asText("SUSPECT");
        String origAuthor   = args.path("original_author_id").asText("");
        String suspAuthor   = args.path("suspect_author_id").asText("");
        int accessCount     = args.path("access_count").asInt(0);

        // ── Content similarity ────────────────────────────────────────────────
        double[] oVec = pseudoEmbed(origContent, 64);
        double[] sVec = pseudoEmbed(suspContent, 64);
        double contentSim = (cosineSimilarity(oVec, sVec) + 1.0) / 2.0;

        // ── Authorship signal ─────────────────────────────────────────────────
        boolean sameAuthor = !origAuthor.isBlank() && origAuthor.equals(suspAuthor);
        double authorSignal = sameAuthor ? 0.0 : 0.3;   // same author = low copy risk

        // ── Access signal ─────────────────────────────────────────────────────
        double accessSignal;
        if (accessCount >= 5)       accessSignal = 0.9;
        else if (accessCount >= 2)  accessSignal = 0.6;
        else if (accessCount == 1)  accessSignal = 0.3;
        else                        accessSignal = 0.1;

        // ── Bayesian-style weighted combination ───────────────────────────────
        double prior   = 0.1;   // base rate of copying
        double likelihoodGivenCopy    = contentSim * 0.6 + accessSignal * 0.25 + authorSignal * 0.15;
        double likelihoodGivenNoCopy  = (1 - contentSim) * 0.5 + 0.1;

        double posteriorCopy;
        double denom = prior * likelihoodGivenCopy + (1 - prior) * likelihoodGivenNoCopy;
        posteriorCopy = denom > 0 ? prior * likelihoodGivenCopy / denom : 0;
        posteriorCopy = Math.min(Math.max(posteriorCopy, 0), 1);

        int probabilityPct = (int) Math.round(posteriorCopy * 100);

        String classification;
        if (probabilityPct >= 90)      classification = "CONFIRMED_COPY";
        else if (probabilityPct >= 75) classification = "HIGHLY_PROBABLE_COPY";
        else if (probabilityPct >= 55) classification = "PROBABLE_COPY";
        else if (probabilityPct >= 35) classification = "POSSIBLE_COPY";
        else                           classification  = "LIKELY_INDEPENDENT";

        // ── Build result ──────────────────────────────────────────────────────
        ObjectNode result = MAPPER.createObjectNode();
        result.put("agent",          "COPY_PROBABILITY");
        result.put("status",         "EVALUATED");
        result.put("original_id",    originalId);
        result.put("suspect_id",     suspectId);
        result.put("timestamp",      now());
        result.put("classification", classification);
        result.put("probability_pct", probabilityPct);

        ObjectNode signals = result.putObject("signal_breakdown");
        signals.put("content_similarity",   r4(contentSim));
        signals.put("access_signal",        r4(accessSignal));
        signals.put("authorship_signal",    r4(authorSignal));
        signals.put("prior_base_rate",      prior);
        signals.put("posterior_copy_prob",  r4(posteriorCopy));

        ObjectNode action = result.putObject("recommended_actions");
        action.put("primary", probabilityPct >= 75 ? "INITIATE_IP_DISPUTE_PROCESS" :
                              probabilityPct >= 55 ? "REQUEST_OWNERSHIP_DECLARATION" :
                              probabilityPct >= 35 ? "ISSUE_SIMILARITY_WARNING" :
                                                     "LOG_AND_MONITOR");
        action.put("notify_owner",          probabilityPct >= 55);
        action.put("block_publication",     probabilityPct >= 75);
        action.put("escalate_to_legal",     probabilityPct >= 90);
        action.put("generate_evidence_pack", probabilityPct >= 55);

        ObjectNode evidence = result.putObject("evidence_hashes");
        evidence.put("original_hash", sha256(origContent));
        evidence.put("suspect_hash",  sha256(suspContent));
        evidence.put("report_hash",   sha256(originalId + suspectId + probabilityPct + now()));

        return result;
    }

    private static double r4(double v) {
        return Math.round(v * 10000.0) / 10000.0;
    }
}
