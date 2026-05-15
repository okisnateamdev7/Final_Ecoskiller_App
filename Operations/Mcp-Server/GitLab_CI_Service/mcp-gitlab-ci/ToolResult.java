package com.ecoskiller.mcp.gitlab.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * MCP tool response wrapper (MCP spec §5.4 — tools/call result).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ToolResult {

    public List<ContentBlock> content;
    public boolean isError;

    private ToolResult() {}

    public static ToolResult success(String text) {
        ToolResult r = new ToolResult();
        r.content = List.of(ContentBlock.text(text));
        r.isError = false;
        return r;
    }

    public static ToolResult error(String message) {
        ToolResult r = new ToolResult();
        r.content = List.of(ContentBlock.text("ERROR: " + message));
        r.isError = true;
        return r;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class ContentBlock {
        public String type;
        public String text;

        private ContentBlock() {}

        public static ContentBlock text(String text) {
            ContentBlock cb = new ContentBlock();
            cb.type = "text";
            cb.text = text;
            return cb;
        }
    }
}
