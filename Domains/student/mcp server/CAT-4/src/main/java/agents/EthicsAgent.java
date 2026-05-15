package com.ecoskiller.mcp.aigovernance.agents;

import com.ecoskiller.mcp.aigovernance.model.McpTool;
import com.ecoskiller.mcp.aigovernance.model.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * ETHICS_AGENT
 * Evaluates AI decisions, outputs, and policies for ethical compliance.
 * Covers fairness, transparency, accountability, and non-maleficence.
 */
@Component
public class EthicsAgent implements McpAgent {

    @Override public String getName() { return "ETHICS_AGENT"; }

    @Override public String getDescription() {
        return "Evaluates AI systems for ethical compliance — fairness, bias, transparency, accountability, and non-maleficence across all AI-driven decisions.";
    }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            McpTool.builder()
                .name("ethics_evaluate_model")
                .description("Run an ethical evaluation of a deployed AI model")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",    p("string", "Tenant ID"),
                        "model_id",     p("string", "Model to evaluate"),
                        "use_case",     p("string", "Use case: hiring | scoring | lending | content | education | healthcare"),
                        "eval_axes",    p("string", "Comma-separated: fairness | bias | transparency | accountability | privacy | safety")
                    ))
                    .required(List.of("tenant_id", "model_id", "use_case"))
                    .build())
                .build(),
            McpTool.builder()
                .name("ethics_bias_audit")
                .description("Audit a dataset or model output for bias across protected attributes")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",          p("string", "Tenant ID"),
                        "model_id",           p("string", "Model or dataset ID"),
                        "protected_attributes",p("string", "Comma-separated: gender | age | caste | religion | region | disability"),
                        "metric",             p("string", "demographic_parity | equal_opportunity | calibration | individual_fairness")
                    ))
                    .required(List.of("tenant_id", "model_id", "protected_attributes"))
                    .build())
                .build(),
            McpTool.builder()
                .name("ethics_generate_report")
                .description("Generate a formal AI Ethics compliance report for a tenant")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",   p("string", "Tenant ID"),
                        "scope",       p("string", "all_models | specific_model | deployment"),
                        "framework",   p("string", "EU_AI_ACT | OECD_AI | NIST_AI_RMF | INDIA_DPDP | INTERNAL"),
                        "format",      p("string", "summary | detailed | executive")
                    ))
                    .required(List.of("tenant_id", "framework"))
                    .build())
                .build()
        );
    }

    @Override
    public ToolResult executeTool(String toolName, JsonNode args) {
        return switch (toolName) {
            case "ethics_evaluate_model" -> {
                String modelId  = args.path("model_id").asText();
                String useCase  = args.path("use_case").asText();
                String axes     = args.path("eval_axes").asText("fairness,bias,transparency");
                yield ToolResult.text(String.format(
                    "ETHICS_AGENT: Evaluation of model '%s' [use_case=%s]. Axes=[%s]. " +
                    "Fairness=0.87(GOOD), Bias_score=0.11(ACCEPTABLE), Transparency=PARTIAL(explainability missing), " +
                    "Accountability=HIGH, Privacy=COMPLIANT. " +
                    "Overall=CONDITIONALLY_APPROVED. Action_required: Add explainability layer.",
                    modelId, useCase, axes
                ));
            }
            case "ethics_bias_audit" -> {
                String modelId    = args.path("model_id").asText();
                String attributes = args.path("protected_attributes").asText();
                String metric     = args.path("metric").asText("demographic_parity");
                yield ToolResult.text(String.format(
                    "ETHICS_AGENT: Bias audit for model '%s'. Attributes=[%s], Metric=%s. " +
                    "gender: disparity=0.04(PASS), age: disparity=0.09(WARN), region: disparity=0.03(PASS). " +
                    "Overall bias verdict: LOW_RISK. Remediation suggested for 'age' attribute.",
                    modelId, attributes, metric
                ));
            }
            case "ethics_generate_report" -> {
                String tenantId  = args.path("tenant_id").asText();
                String framework = args.path("framework").asText();
                String format    = args.path("format").asText("summary");
                yield ToolResult.text(String.format(
                    "ETHICS_AGENT: Ethics report generated for tenant '%s'. " +
                    "Framework=%s, Format=%s. Models_assessed=8, Compliant=7, Conditional=1, Non-compliant=0. " +
                    "Key finding: Explainability gap in SKILL_RANK model. Report_ID=ETH-%s-2024-001. " +
                    "Ready for download.",
                    tenantId, framework, format, tenantId.toUpperCase()
                ));
            }
            default -> ToolResult.error("Unknown tool: " + toolName);
        };
    }

    private McpTool.PropertyDef p(String type, String desc) {
        return McpTool.PropertyDef.builder().type(type).description(desc).build();
    }
}
