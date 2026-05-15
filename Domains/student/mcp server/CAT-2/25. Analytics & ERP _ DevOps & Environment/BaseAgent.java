package com.ecoskiller.analytics.agents;

import com.ecoskiller.analytics.models.AgentResponse;
import com.ecoskiller.analytics.models.McpTool;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

public abstract class BaseAgent {
    protected final ObjectMapper mapper = new ObjectMapper();

    public abstract String getName();
    public abstract List<McpTool> getTools();
    public abstract AgentResponse execute(String toolName, ObjectNode args);

    protected ObjectNode schema(String... fields) {
        ObjectNode props = mapper.createObjectNode();
        for (String f : fields) {
            ObjectNode fd = mapper.createObjectNode(); fd.put("type","string"); props.set(f, fd);
        }
        ObjectNode s = mapper.createObjectNode(); s.put("type","object"); s.set("properties",props);
        return s;
    }
    protected ObjectNode emptySchema() {
        ObjectNode s = mapper.createObjectNode(); s.put("type","object");
        s.set("properties", mapper.createObjectNode()); return s;
    }
    protected String uid(String prefix) { return prefix + "-" + System.currentTimeMillis(); }
}
