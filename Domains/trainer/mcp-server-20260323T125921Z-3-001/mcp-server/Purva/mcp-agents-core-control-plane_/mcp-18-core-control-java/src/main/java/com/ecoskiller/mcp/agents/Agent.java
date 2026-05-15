package com.ecoskiller.mcp.agents;

import com.fasterxml.jackson.databind.JsonNode;

public interface Agent {
    JsonNode execute(String toolName, JsonNode arguments) throws Exception;
    String getAgentName();
    String getDescription();
}
