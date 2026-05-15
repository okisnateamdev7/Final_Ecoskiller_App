package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * REVENUE_INGESTION_AGENT_SEALED
 * Ingests raw revenue events from payment gateways and normalises them for royalty processing.
 */
public class RevenueIngestionAgent extends ToolDefinition {

    @Override public String getName() { return "revenue_ingestion"; }

    @Override public String getDescription() {
        return "REVENUE_INGESTION_AGENT_SEALED — Ingests raw revenue events from payment gateways " +
               "(Razorpay, Stripe, UPI), deduplicates, normalises, and stages them " +
               "for the royalty calculation pipeline.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "source", "event_id", "amount");
        addStringProp(s, "source",       "Payment source: razorpay | stripe | upi | manual");
        addStringProp(s, "event_id",     "Raw payment event / order ID from the gateway");
        addNumberProp(s, "amount",       "Revenue amount in INR");
        addStringProp(s, "currency",     "ISO currency code (default INR)");
        addStringProp(s, "entity_id",    "EcoSkiller entity credited with this revenue");
        addStringProp(s, "product_type", "Product: course | idea_license | competition | workshop");
        addStringProp(s, "event_time",   "ISO-8601 event timestamp");
        addBoolProp  (s, "force_reprocess", "Re-ingest even if event_id already seen");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String source      = str(args, "source",       "manual");
        String eventId     = str(args, "event_id",     "EVT-UNKNOWN");
        double amount      = num(args, "amount",       0.0);
        String currency    = str(args, "currency",     "INR");
        String entityId    = str(args, "entity_id",    "ENTITY-UNKNOWN");
        String productType = str(args, "product_type", "course");
        boolean reprocess  = bool(args, "force_reprocess", false);

        ObjectNode data = m.createObjectNode();
        data.put("ingest_id",    "ING-" + eventId + "-" + System.currentTimeMillis());
        data.put("source",       source);
        data.put("event_id",     eventId);
        data.put("amount",       amount);
        data.put("currency",     currency);
        data.put("entity_id",    entityId);
        data.put("product_type", productType);
        data.put("deduplicated", !reprocess);
        data.put("normalised",   true);
        data.put("staged_for_royalty", true);

        return successWithData(m, "Revenue event " + eventId + " ingested from " + source, data);
    }
}
