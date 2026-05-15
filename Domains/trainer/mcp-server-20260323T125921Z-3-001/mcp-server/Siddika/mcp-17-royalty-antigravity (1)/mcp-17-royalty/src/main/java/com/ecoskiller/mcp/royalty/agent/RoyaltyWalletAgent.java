package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * AGENT-03: ROYALTY_WALLET_AGENT
 * Manages royalty wallet: balance, credits, debits, payout requests.
 */
@Component
public class RoyaltyWalletAgent {

    @Tool(name = "wallet_balance_get",
          description = "Get current royalty wallet balance for a creator or partner.")
    public AgentResponse getBalance(
            @ToolParam(description = "Wallet owner user ID") String userId,
            @ToolParam(description = "Wallet type: CREATOR | PARTNER | FRANCHISE") String walletType) {

        return AgentResponse.ok("ROYALTY_WALLET_AGENT",
                "Wallet balance fetched for " + userId,
                Map.of(
                        "userId",           userId,
                        "walletType",       walletType,
                        "availableBalance", 12500.75,
                        "escrowHold",       2000.00,
                        "pendingPayout",    5000.00,
                        "totalEarned",      45000.00,
                        "currency",         "INR"
                ));
    }

    @Tool(name = "wallet_credit",
          description = "Credit royalty amount to wallet after a revenue event.")
    public AgentResponse creditWallet(
            @ToolParam(description = "User ID to credit") String userId,
            @ToolParam(description = "Amount to credit in INR") double amount,
            @ToolParam(description = "Source: SALE | SUBSCRIPTION | COMPETITION") String sourceEvent,
            @ToolParam(description = "Reference transaction ID") String referenceId) {

        return AgentResponse.ok("ROYALTY_WALLET_AGENT",
                "Wallet credited ₹" + amount + " for " + userId,
                Map.of(
                        "userId",        userId,
                        "credited",      amount,
                        "sourceEvent",   sourceEvent,
                        "referenceId",   referenceId,
                        "newBalance",    12500.75 + amount,
                        "transactionId", "TXN-" + System.currentTimeMillis()
                ));
    }

    @Tool(name = "wallet_payout_request",
          description = "Request payout from royalty wallet to bank account.")
    public AgentResponse requestPayout(
            @ToolParam(description = "User ID") String userId,
            @ToolParam(description = "Payout amount in INR") double amount,
            @ToolParam(description = "Bank account number") String bankAccount,
            @ToolParam(description = "IFSC code") String ifscCode) {

        return AgentResponse.ok("ROYALTY_WALLET_AGENT",
                "Payout request submitted ₹" + amount,
                Map.of(
                        "userId",        userId,
                        "payoutAmount",  amount,
                        "bankAccount",   "XXXX" + bankAccount.substring(Math.max(0, bankAccount.length() - 4)),
                        "ifscCode",      ifscCode,
                        "payoutStatus",  "PROCESSING",
                        "expectedDays",  "3-5 business days",
                        "payoutId",      "PAY-" + System.currentTimeMillis()
                ));
    }
}
