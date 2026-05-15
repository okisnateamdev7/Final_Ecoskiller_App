package com.ecoskiller.mcp.metallb.tools;
import com.ecoskiller.mcp.metallb.json.Json;
import java.util.*;

public class ToolRegistry {
    private final Map<String, McpTool> tools = new LinkedHashMap<>();
    public void registerAll() {
        for (ToolProvider p : List.of(new AllTools()))
            for (McpTool t : p.getTools()) tools.put(t.getName(), t);
    }
    public List<Json.Obj> getToolDefinitions() {
        List<Json.Obj> r = new ArrayList<>();
        for (McpTool t : tools.values()) r.add(t.getDefinition());
        return r;
    }
    public String callTool(String name, Json.Obj args) throws Exception {
        McpTool t = tools.get(name);
        if (t == null) throw new ToolNotFoundException(name);
        return t.execute(args);
    }
    public boolean hasTool(String name) { return tools.containsKey(name); }
    public int size() { return tools.size(); }
}
