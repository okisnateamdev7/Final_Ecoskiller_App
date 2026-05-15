# OpenTofu MCP Server - API Reference

## Protocol Overview

The OpenTofu MCP Server implements **JSON-RPC 2.0** protocol over stdin/stdout for communication with AI agents and clients.

### Request Format

```json
{
  "jsonrpc": "2.0",
  "method": "method_name",
  "params": { /* optional parameters */ },
  "id": "request_id"
}
```

### Response Format (Success)

```json
{
  "jsonrpc": "2.0",
  "result": { /* operation result */ },
  "id": "request_id"
}
```

### Response Format (Error)

```json
{
  "jsonrpc": "2.0",
  "error": {
    "code": -32600,
    "message": "Invalid Request",
    "data": "Additional error details"
  },
  "id": "request_id"
}
```

---

## Methods

### 1. `initialize`

Initialize the MCP server and retrieve capabilities.

**Request:**
```json
{
  "jsonrpc": "2.0",
  "method": "initialize",
  "id": "1"
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "result": {
    "serverInfo": {
      "name": "ecoskiller-opentofu-mcp",
      "version": "1.0.0"
    },
    "capabilities": {
      "tools": true,
      "stateManagement": true,
      "multiEnvironment": true,
      "multiCloud": true
    },
    "protocolVersion": "2024-11-05",
    "status": {
      "state": "READY",
      "orchestratorHealth": "HEALTHY",
      "supportedEnvironments": "dev,test,stage,prod",
      "supportedClouds": "gcp,aws"
    }
  },
  "id": "1"
}
```

---

### 2. `tools/list`

List all available OpenTofu operations.

**Request:**
```json
{
  "jsonrpc": "2.0",
  "method": "tools/list",
  "id": "2"
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "result": {
    "tools": [
      {
        "id": "1",
        "name": "plan",
        "description": "Generate and display execution plan for infrastructure changes",
        "inputSchema": {
          "type": "object",
          "properties": {
            "environment": {
              "type": "string",
              "enum": "dev,test,stage,prod",
              "description": "Target environment"
            },
            "workspace": {
              "type": "string",
              "description": "OpenTofu workspace"
            },
            "outPath": {
              "type": "string",
              "description": "Path to save plan file"
            }
          },
          "required": ["environment"]
        }
      },
      /* ... more tools ... */
    ],
    "toolCount": 15
  },
  "id": "2"
}
```

---

### 3. `tools/call`

Execute an OpenTofu operation.

**Request:**
```json
{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "params": {
    "name": "plan",
    "arguments": {
      "environment": "prod",
      "workspace": "prod"
    }
  },
  "id": "3"
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "result": {
    "status": "success",
    "operation": "plan",
    "environment": "prod",
    "output": "No changes. Your infrastructure matches the configuration...",
    "planFile": "/workspace/terraform/terraform.prod.plan",
    "changes": {
      "toCreate": 0,
      "toModify": 0,
      "toDestroy": 0
    },
    "timestamp": 1700000000000
  },
  "id": "3"
}
```

---

### 4. `state/list`

List resources in state.

**Request:**
```json
{
  "jsonrpc": "2.0",
  "method": "state/list",
  "params": {
    "environment": "prod"
  },
  "id": "4"
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "result": {
    "operation": "state_list",
    "status": "success",
    "environment": "prod",
    "resourcesInState": 42,
    "timestamp": 1700000000000
  },
  "id": "4"
}
```

---

### 5. `state/lock`

Acquire state lock.

**Request:**
```json
{
  "jsonrpc": "2.0",
  "method": "state/lock",
  "params": {
    "environment": "prod"
  },
  "id": "5"
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "result": {
    "operation": "state_lock",
    "status": "success",
    "environment": "prod",
    "lockId": "a1b2c3d4-e5f6-4789-0abc-def123456789",
    "acquireTime": 1700000000000
  },
  "id": "5"
}
```

---

### 6. `state/unlock`

Release state lock.

**Request:**
```json
{
  "jsonrpc": "2.0",
  "method": "state/unlock",
  "params": {
    "environment": "prod",
    "lockId": "a1b2c3d4-e5f6-4789-0abc-def123456789"
  },
  "id": "6"
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "result": {
    "operation": "state_unlock",
    "status": "success",
    "environment": "prod",
    "lockId": "a1b2c3d4-e5f6-4789-0abc-def123456789",
    "holdTime": 30000
  },
  "id": "6"
}
```

---

### 7. `ping`

Health check.

**Request:**
```json
{
  "jsonrpc": "2.0",
  "method": "ping",
  "id": "7"
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "result": {
    "status": "pong",
    "timestamp": 1700000000000
  },
  "id": "7"
}
```

---

## Tools Reference

### `plan`
Generate execution plan without modifying infrastructure.

**Arguments:**
- `environment` (required): dev, test, stage, or prod
- `workspace` (optional): Terraform workspace
- `outPath` (optional): Path to save plan file

### `apply`
Apply infrastructure changes from plan.

**Arguments:**
- `environment` (required)
- `workspace` (optional)
- `planFile` (optional): Path to plan file

### `destroy`
Destroy managed infrastructure.

**Arguments:**
- `environment` (required)
- `workspace` (optional)

### `import`
Import existing cloud resources into state.

**Arguments:**
- `environment` (required)
- `resourceId` (required): Cloud resource ID
- `address` (required): State resource address

### `refresh`
Update local state from remote resources.

**Arguments:**
- `environment` (required)

### `validate`
Validate configuration syntax.

**Arguments:**
- `environment` (required)

### `fmt`
Format configuration files.

**Arguments:**
- `environment` (required)

### `init`
Initialize working directory.

**Arguments:**
- `environment` (required)

### `workspace_list`
List workspaces.

**Arguments:**
- `environment` (required)

### `workspace_select`
Select workspace.

**Arguments:**
- `environment` (required)
- `workspaceName` (required)

### `workspace_create`
Create workspace.

**Arguments:**
- `environment` (required)
- `workspaceName` (required)

### `state_list`
List state resources.

**Arguments:**
- `environment` (required)

### `state_show`
Show resource details.

**Arguments:**
- `environment` (required)
- `address` (optional)

### `state_rm`
Remove resource from state.

**Arguments:**
- `environment` (required)
- `address` (required)

### `cost_estimate`
Estimate cost impact.

**Arguments:**
- `environment` (required)

---

## Error Codes

| Code | Message | Meaning |
|------|---------|---------|
| -32700 | Parse error | Invalid JSON received |
| -32600 | Invalid Request | Request format incorrect |
| -32601 | Method not found | Unknown method |
| -32602 | Invalid params | Invalid parameters |
| -32603 | Internal error | Server error |
| -32000 | Security validation failed | Authentication/authorization failed |
| -32001 | Invalid environment | Unknown environment |
| -32002 | State locked | State is locked |
| -32003 | Rate limit exceeded | Too many requests |

---

## Examples

### Example 1: Plan Changes

```bash
curl -X POST http://localhost:9092 \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "method": "tools/call",
    "params": {
      "name": "plan",
      "arguments": {
        "environment": "dev",
        "workspace": "dev"
      }
    },
    "id": "1"
  }'
```

### Example 2: Apply Changes

```bash
curl -X POST http://localhost:9092 \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "method": "tools/call",
    "params": {
      "name": "apply",
      "arguments": {
        "environment": "prod"
      }
    },
    "id": "2"
  }'
```

### Example 3: Estimate Cost

```bash
curl -X POST http://localhost:9092 \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "method": "tools/call",
    "params": {
      "name": "cost_estimate",
      "arguments": {
        "environment": "prod"
      }
    },
    "id": "3"
  }'
```

### Example 4: Manage State Lock

```bash
# Acquire lock
curl -X POST http://localhost:9092 \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "method": "state/lock",
    "params": {
      "environment": "prod"
    },
    "id": "4"
  }'

# ... perform operations ...

# Release lock
curl -X POST http://localhost:9092 \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "method": "state/unlock",
    "params": {
      "environment": "prod",
      "lockId": "lock-id-from-response"
    },
    "id": "5"
  }'
```

---

## Environment Variables

All requests should target the correct environment:
- `dev` - Development
- `test` - Testing
- `stage` - Staging
- `prod` - Production

---

## Rate Limiting

- Default: 1000 requests per minute per client
- Configurable via environment variable

---

## Timeouts

- Request timeout: 30 seconds
- Long-running operations (apply, destroy): 3600 seconds

---

## Version

API Version: **2024-11-05**  
Server Version: **1.0.0**
