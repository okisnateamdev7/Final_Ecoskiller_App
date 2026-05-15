package com.ecoskiller.mcp.nginx;

import java.util.Map;

/**
 * Contract every NGINX MCP tool must implement.
 */
public interface NginxTool {

    /**
     * MCP tool descriptor returned in tools/list.
     */
    Map<String, Object> descriptor();

    /**
     * Execute the tool with the given (already-validated) arguments.
     *
     * @param  args sanitised argument map
     * @return any serialisable result
     * @throws Exception on tool-level failure
     */
    Object execute(Map<String, Object> args) throws Exception;
}
