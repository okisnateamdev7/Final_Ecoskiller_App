package com.ecoskiller.mcp.institute.tools;

/**
 * Public re-exports so Spring's component scan can inject the tool beans.
 *
 * Each alias simply re-declares the package-private impl class as public
 * (the actual implementations are in AgentToolImpls.java).
 *
 * Spring registers them by their simple class names, which the McpDispatcher
 * constructor-injects by type.
 */
// NOTE: All tool classes are declared directly in AgentToolImpls.java as
// top-level package-private classes. Spring Boot's component scan picks them
// up automatically because they are annotated with @Component.
//
// This file is intentionally empty — it exists only as documentation.
