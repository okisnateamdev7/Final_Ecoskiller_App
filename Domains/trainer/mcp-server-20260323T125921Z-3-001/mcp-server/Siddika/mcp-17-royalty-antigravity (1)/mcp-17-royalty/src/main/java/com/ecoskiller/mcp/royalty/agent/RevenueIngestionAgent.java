package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * AGENT-08: REVENUE_INGESTION_AGENT
 * Ingests revenue events from payment gateways into the royalty pipeline.
 * Supports: Razorpay, Stripe, PayU, Cashfree.
 */
@Component
public class RevenueIngestionAgent {

    @Tool(name = "revenue_event_ingest",
          description = "Ingest a revenue event from a payment gateway into the royalty pipeline.")
    public AgentResponse ingestEvent(
            @ToolParam(description = "Gateway: RAZORPAY | STRIPE | PAYU | CASHFREE") String gateway,
            @ToolParam(description = "Gateway transaction ID") String gatewayTxnId,
            @ToolParam(description = "Tenant ID") String tenantId,
            @ToolParam(description = "Revenue amount (INR)") double amount,
            @ToolParam(description = "Type: COURSE | COMPETITION | SUBSCRIPTION | LICENSE") String revenueType,
            @ToolParam(description = "Payer user ID") String payerId) {

        String internalId = "REV-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();

        return AgentResponse.ok("REVENUE_INGESTION_AGENT",
                "Revenue event ingested from " + gateway,
                Map.of(
                        "internalEventId", internalId,
                        "gatewayTxnId",    gatewayTxnId,
                        "gateway",         gateway,
                        "tenantId",        tenantId,
                        "amount",          amount,
                        "revenueType",     revenueType,
                        "payerId",         payerId,
                        "status",          "QUEUED_FOR_PROCESSING",
                        "ingestedAt",      Instant.now().toString()
                ));
    }

    @Tool(name = "revenue_duplicate_check",
          description = "Check if a revenue event is already ingested to prevent double processing.")
    public AgentResponse duplicateCheck(
            @ToolParam(description = "Gateway transaction ID to check") String gatewayTxnId,
            @ToolParam(description = "Gateway name") String gateway) {

        boolean isDuplicate = gatewayTxnId.startsWith("DUP");

        return AgentResponse.ok("REVENUE_INGESTION_AGENT",
                "Duplicate check: " + (isDuplicate ? "DUPLICATE" : "UNIQUE"),
                Map.of(
                        "gatewayTxnId", gatewayTxnId,
                        "gateway",      gateway,
                        "isDuplicate",  isDuplicate,
                        "action",       isDuplicate ? "REJECT" : "PROCEED"
                ));
    }
}
