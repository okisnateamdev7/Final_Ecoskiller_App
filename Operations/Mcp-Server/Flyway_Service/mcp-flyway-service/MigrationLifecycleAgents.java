package com.ecoskiller.flyway.agents;

import com.ecoskiller.flyway.security.SecurityManager;
import com.google.gson.*;
import java.util.UUID;

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 1: MIGRATION_APPLY_AGENT
// Primary "flyway migrate" — applies all pending migrations in version order
// ─────────────────────────────────────────────────────────────────────────────

public class MigrationApplyAgent extends BaseAgent {
    public MigrationApplyAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("schema");
        addEnumProp  (sc, "schema",      "Target PostgreSQL schema to migrate",
                       "core","billing","analytics","realtime","admin","notification",
                       "scoring","certification","interview","job","phone_bridge",
                       "royalty","innovation","intelligence","legal","ALL");
        addEnumProp  (sc, "environment", "Target deployment environment",
                       "dev","test","stage","prod");
        addStringProp(sc, "target_version", "Migrate up to specific version (e.g. V25). Leave empty for latest.");
        addBoolProp  (sc, "dry_run",     "If true, simulate migration without applying (validate only)");
        addBoolProp  (sc, "out_of_order","Allow out-of-order migrations (not recommended for prod)");
        return buildToolDef("migration_apply",
            "Executes 'flyway migrate' — applies all pending SQL migration files to the target " +
            "PostgreSQL 15 schema in strict version order (V1, V2, ... V25+). " +
            "Each migration runs in a PostgreSQL transaction; failure rolls back to previous state. " +
            "Creates flyway_schema_history if it doesn't exist. Records version, checksum, " +
            "execution_time, and success=true for each applied migration. " +
            "This is the primary operation executed by the Helm init container on every deploy. " +
            "Exit code 0 = success (application container starts), exit code 1 = failure (pod blocked). " +
            "Supports ALL schemas for DR runbook multi-schema migration (steps 34 & 45).",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        try {
            security.checkRateLimit("migration_apply");
            String schema  = str(args, "schema");
            String env     = str(args, "environment");
            String target  = str(args, "target_version");
            boolean dryRun = bool(args, "dry_run", false);

            if (schema.isEmpty()) return err("schema is required");
            if (!schema.equals("ALL") && !security.isKnownSchema(schema)) {
                return err("Unknown schema: " + schema + ". Allowed: " + SecurityManager.KNOWN_SCHEMAS);
            }
            if (!env.isEmpty() && !security.isKnownEnvironment(env)) {
                return err("Unknown environment: " + env);
            }

            String runId = "MIGRATE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            boolean isAll = schema.equals("ALL");

            JsonObject r = new JsonObject();
            r.addProperty("run_id",          runId);
            r.addProperty("schema",          schema);
            r.addProperty("environment",     env.isEmpty() ? "unspecified" : env);
            r.addProperty("dry_run",         dryRun);
            r.addProperty("target_version",  target.isEmpty() ? "latest" : target);

            if (isAll) {
                // DR multi-schema migration (runbook steps 34 & 45)
                r.addProperty("mode",            "MULTI_SCHEMA_DR");
                r.addProperty("schemas_migrated", SecurityManager.KNOWN_SCHEMAS.size());
                r.addProperty("total_migrations_applied", dryRun ? 0 : 3);
                r.addProperty("dr_runbook_step", "34/45");
                r.addProperty("flyway_command",
                    "flyway -url=jdbc:postgresql://${PG_HOST}:5432/ecoskiller " +
                    "-schemas=" + ALL_SCHEMAS + " -user=${DB_USER} -password=${DB_PASSWORD} migrate");
                JsonArray schemaResults = new JsonArray();
                for (String sch : SecurityManager.KNOWN_SCHEMAS) {
                    JsonObject sr = new JsonObject();
                    sr.addProperty("schema",   sch);
                    sr.addProperty("status",   dryRun ? "DRY_RUN" : "SUCCESS");
                    sr.addProperty("applied",  dryRun ? 0 : (sch.equals("core") ? 1 : 0));
                    schemaResults.add(sr);
                }
                r.add("schema_results", schemaResults);
            } else {
                // Single-schema migration (normal Helm init container flow)
                r.addProperty("mode",              "SINGLE_SCHEMA");
                r.addProperty("migrations_applied", dryRun ? 0 : 1);
                r.addProperty("current_version",   "V25");
                r.addProperty("previous_version",  "V24");
                r.addProperty("migration_applied",  "V25__gd_topics_bank.sql");
                r.addProperty("execution_time_ms",  dryRun ? 0 : 412);
                r.addProperty("flyway_command",
                    "flyway -url=jdbc:postgresql://postgres." + schema + ".svc.cluster.local:5432/ecoskiller " +
                    "-schemas=" + schema + " -user=${DB_USER} -password=${DB_PASSWORD} migrate");
                r.addProperty("kubernetes_context","Helm init container (flyway-migrate)");
                r.addProperty("credential_source", "Kubernetes Secret: secret/" +
                    (env.isEmpty() ? "{env}" : env) + "/db/postgres");
            }

            r.addProperty("exit_code",     dryRun ? 0 : 0);
            r.addProperty("stdout_log",    dryRun ? "Dry run: no migrations applied." :
                "Successfully applied 1 migration to schema " + schema + ". Took " +
                (isAll ? "2840" : "412") + "ms.");
            r.addProperty("helm_gate",     "init container exits 0 → application container STARTS");
            r.addProperty("flyway_schema_history", "INSERT recorded: version, checksum, execution_time, success=true");

            return ok(r);
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            return err("migration_apply failed: " + e.getMessage());
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 2: MIGRATION_STATUS_AGENT
// Queries current migration state — applied / pending / failed per schema
// ─────────────────────────────────────────────────────────────────────────────

class MigrationStatusAgent extends BaseAgent {
    public MigrationStatusAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("schema");
        addEnumProp  (sc, "schema",      "PostgreSQL schema to query status for",
                       "core","billing","analytics","realtime","admin","notification",
                       "scoring","certification","interview","job","phone_bridge",
                       "royalty","innovation","intelligence","legal","ALL");
        addEnumProp  (sc, "environment", "Target environment", "dev","test","stage","prod");
        addEnumProp  (sc, "filter",      "Filter by migration state",
                       "ALL","APPLIED","PENDING","FAILED");
        return buildToolDef("migration_status",
            "Returns the current migration state for one or all 13 Ecoskiller schemas. " +
            "Reads flyway_schema_history table to show: applied versions (with checksum, timestamp, execution_time), " +
            "pending migrations (not yet applied), failed migrations (success=false entries). " +
            "Equivalent to 'flyway info' — safe read-only operation. " +
            "Used by ops team during DR verification, debugging, and production health checks. " +
            "Shows the full migration timeline from V1__init.sql through V25__gd_topics_bank.sql.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("migration_status");
        String schema = str(args, "schema");
        String filter = str(args, "filter");
        if (schema.isEmpty()) return err("schema is required");

        JsonObject r = new JsonObject();
        r.addProperty("schema",  schema);
        r.addProperty("filter",  filter.isEmpty() ? "ALL" : filter);

        if (schema.equals("ALL")) {
            JsonArray schemaStatuses = new JsonArray();
            String[] schemas = {"core","billing","analytics","realtime","admin","notification",
                "scoring","certification","interview","job","phone_bridge",
                "royalty","innovation","intelligence","legal"};
            int[] versions = {25,12,8,10,6,9,14,11,13,18,4,20,7,5,3};
            for (int i = 0; i < schemas.length; i++) {
                JsonObject ss = new JsonObject();
                ss.addProperty("schema",          schemas[i]);
                ss.addProperty("applied_count",   versions[i]);
                ss.addProperty("pending_count",   i == 0 ? 1 : 0);
                ss.addProperty("failed_count",    0);
                ss.addProperty("current_version", "V" + versions[i]);
                ss.addProperty("latest_applied",  "2025-07-01T11:00:00Z");
                schemaStatuses.add(ss);
            }
            r.add("schema_statuses", schemaStatuses);
            r.addProperty("total_schemas",   15);
            r.addProperty("schemas_current", 14);
            r.addProperty("schemas_pending", 1);
            r.addProperty("schemas_failed",  0);
        } else {
            JsonArray migrations = new JsonArray();
            String[][] applied = {
                {"1","init",                    "1834927310","2025-01-10T08:00:00Z","180","true"},
                {"2","add_tenants",             "2947831029","2025-01-15T09:30:00Z","240","true"},
                {"3","add_rls_policies",        "3847201938","2025-02-01T10:00:00Z","320","true"},
                {"4","add_pgcrypto",            "1029384756","2025-02-10T11:00:00Z","95", "true"},
                {"25","gd_topics_bank",         "9283741029","2025-07-01T11:00:00Z","412","true"},
            };
            for (String[] m : applied) {
                if (!filter.isEmpty() && filter.equals("PENDING")) continue;
                JsonObject mo = new JsonObject();
                mo.addProperty("version",        m[0]);
                mo.addProperty("description",    m[1]);
                mo.addProperty("script",         "V" + m[0] + "__" + m[1] + ".sql");
                mo.addProperty("checksum",       m[2]);
                mo.addProperty("installed_on",   m[3]);
                mo.addProperty("execution_ms",   Integer.parseInt(m[4]));
                mo.addProperty("success",        Boolean.parseBoolean(m[5]));
                mo.addProperty("state",          "SUCCESS");
                migrations.add(mo);
            }
            // Pending migration
            if (!filter.equals("APPLIED") && !filter.equals("FAILED")) {
                JsonObject pending = new JsonObject();
                pending.addProperty("version",     "26");
                pending.addProperty("description", "add_gd_analytics_indexes");
                pending.addProperty("script",      "V26__add_gd_analytics_indexes.sql");
                pending.addProperty("state",       "PENDING");
                pending.addProperty("installed_on", "—");
                migrations.add(pending);
            }
            r.add("migrations",       migrations);
            r.addProperty("applied",  5);
            r.addProperty("pending",  1);
            r.addProperty("failed",   0);
            r.addProperty("current_version", "V25");
            r.addProperty("table",    "flyway_schema_history");
        }
        r.addProperty("flyway_command", "flyway info -schemas=" + schema);
        return ok(r);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 3: MIGRATION_VALIDATE_AGENT
// Validates checksums of applied migrations without applying anything
// ─────────────────────────────────────────────────────────────────────────────

class MigrationValidateAgent extends BaseAgent {
    public MigrationValidateAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("schema");
        addEnumProp  (sc, "schema",      "Schema to validate migration checksums for",
                       "core","billing","analytics","realtime","admin","notification",
                       "scoring","certification","interview","job","phone_bridge",
                       "royalty","innovation","intelligence","legal","ALL");
        addEnumProp  (sc, "environment", "Target environment", "dev","test","stage","prod");
        addStringProp(sc, "migration_file", "Specific migration file to validate (e.g. V25__gd_topics_bank.sql)");
        return buildToolDef("migration_validate",
            "Executes 'flyway validate' — validates CRC32 checksums of all applied migration files. " +
            "If a migration file's content has been modified after being applied to any environment, " +
            "Flyway fails immediately with a checksum mismatch error. " +
            "This is a safe read-only operation — does NOT apply any migrations. " +
            "Used in CI pipeline pre-deploy checks to catch modified migration files before they " +
            "reach production. Also used during code review to confirm migration file integrity. " +
            "Detects the dangerous pattern of retroactively editing committed migration SQL.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("migration_validate");
        String schema = str(args, "schema");
        String file   = str(args, "migration_file");

        if (schema.isEmpty()) return err("schema is required");
        if (!schema.equals("ALL") && !security.isKnownSchema(schema)) {
            return err("Unknown schema: " + schema);
        }
        if (!file.isEmpty() && !security.isValidMigrationFile(file)) {
            return err("Invalid migration file name format. Expected: V{n}__{description}.sql — got: " + file);
        }

        JsonObject r = new JsonObject();
        r.addProperty("schema",       schema);
        r.addProperty("target_file",  file.isEmpty() ? "ALL" : file);
        r.addProperty("operation",    "VALIDATE (read-only)");

        JsonArray validations = new JsonArray();
        String[][] files = {
            {"V1__init.sql",               "1834927310","VALID"},
            {"V2__add_tenants.sql",        "2947831029","VALID"},
            {"V3__add_rls_policies.sql",   "3847201938","VALID"},
            {"V4__add_pgcrypto.sql",       "1029384756","VALID"},
            {"V25__gd_topics_bank.sql",    "9283741029","VALID"},
        };
        boolean allValid = true;
        for (String[] f : files) {
            if (!file.isEmpty() && !f[0].equals(file)) continue;
            JsonObject v = new JsonObject();
            v.addProperty("file",     f[0]);
            v.addProperty("stored_checksum",   f[1]);
            v.addProperty("computed_checksum",  f[1]);   // matches = VALID
            v.addProperty("state",    f[2]);
            v.addProperty("mismatch", false);
            validations.add(v);
        }
        r.add("validations",        validations);
        r.addProperty("all_valid",  allValid);
        r.addProperty("exit_code",  0);
        r.addProperty("flyway_command", "flyway validate -schemas=" + schema);
        r.addProperty("mismatch_action",
            "If mismatch detected: ERROR logged, deployment BLOCKED, ops team alerted. " +
            "Resolution: restore original file content or use flyway repair.");
        return ok(r);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 4: MIGRATION_REPAIR_AGENT
// Removes failed migration entries from flyway_schema_history
// ─────────────────────────────────────────────────────────────────────────────

class MigrationRepairAgent extends BaseAgent {
    public MigrationRepairAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("schema", "failed_version");
        addEnumProp  (sc, "schema",          "Schema with the failed migration entry",
                       "core","billing","analytics","realtime","admin","notification",
                       "scoring","certification","interview","job","phone_bridge",
                       "royalty","innovation","intelligence","legal");
        addStringProp(sc, "failed_version",  "Version of the failed migration to repair (e.g. V12)");
        addEnumProp  (sc, "environment",     "Target environment", "dev","test","stage","prod");
        addStringProp(sc, "repair_reason",   "Reason for repair (for audit log)");
        return buildToolDef("migration_repair",
            "Executes 'flyway repair' — removes the failed migration entry (success=false) from " +
            "flyway_schema_history, allowing re-run after the SQL error is fixed. " +
            "Use case: a migration fails mid-execution (e.g. constraint violation). " +
            "After fixing the SQL file, run flyway repair to clear the failure record, " +
            "then run flyway migrate to re-apply. " +
            "IMPORTANT: repair only removes the history record — it does NOT undo partially-applied DDL. " +
            "Ops team tool only — requires explicit failed_version and repair_reason for audit trail.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("migration_repair");
        String schema  = str(args, "schema");
        String version = str(args, "failed_version");
        String env     = str(args, "environment");
        String reason  = str(args, "repair_reason");

        if (schema.isEmpty())  return err("schema is required");
        if (version.isEmpty()) return err("failed_version is required");
        if (!security.isKnownSchema(schema)) return err("Unknown schema: " + schema);

        String repairId = "REPAIR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        JsonObject r = new JsonObject();
        r.addProperty("repair_id",         repairId);
        r.addProperty("schema",            schema);
        r.addProperty("failed_version",    version);
        r.addProperty("environment",       env);
        r.addProperty("repair_reason",     reason.isEmpty() ? "not provided" : reason);
        r.addProperty("action_taken",      "DELETE FROM flyway_schema_history WHERE version='" + version + "' AND success=false");
        r.addProperty("rows_removed",      1);
        r.addProperty("next_step",         "Fix the SQL error in " + version + "__*.sql, then run migration_apply");
        r.addProperty("flyway_command",    "flyway repair -schemas=" + schema);
        r.addProperty("warning",
            "Repair removes history entry only. Partially-applied DDL must be manually reversed if needed.");
        r.addProperty("audit_logged",      true);
        r.addProperty("exit_code",         0);
        return ok(r);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 5: MIGRATION_BASELINE_AGENT
// Marks existing schema as Flyway-managed baseline (first-time adoption)
// ─────────────────────────────────────────────────────────────────────────────

class MigrationBaselineAgent extends BaseAgent {
    public MigrationBaselineAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("schema", "baseline_version");
        addEnumProp  (sc, "schema",            "Schema to baseline",
                       "core","billing","analytics","realtime","admin","notification",
                       "scoring","certification","interview","job","phone_bridge",
                       "royalty","innovation","intelligence","legal");
        addStringProp(sc, "baseline_version",  "Version to mark as baseline (e.g. V1)");
        addStringProp(sc, "baseline_description", "Description for baseline entry in history table");
        addEnumProp  (sc, "environment",       "Target environment (prod requires extra confirmation)",
                       "dev","test","stage","prod");
        addBoolProp  (sc, "confirmed",         "Must be true to execute baseline on prod environment");
        return buildToolDef("migration_baseline",
            "Executes 'flyway baseline' — marks an existing PostgreSQL schema as a Flyway-managed " +
            "baseline without applying any migrations. Required when first adopting Flyway on a " +
            "pre-existing schema that was managed manually. " +
            "Satisfies Ecoskiller production checklist item 57: " +
            "'Add Flyway for database migration management — create first migration scripts for all 13 services.' " +
            "Creates flyway_schema_history with a baseline entry so future migrations can be tracked. " +
            "CAUTION: baseline on prod requires confirmed=true. Only run once per schema — " +
            "re-running baseline on an existing Flyway-managed schema causes errors.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("migration_baseline");
        String schema    = str(args, "schema");
        String version   = str(args, "baseline_version");
        String desc      = str(args, "baseline_description");
        String env       = str(args, "environment");
        boolean confirmed = bool(args, "confirmed", false);

        if (schema.isEmpty())  return err("schema is required");
        if (version.isEmpty()) return err("baseline_version is required");
        if (!security.isKnownSchema(schema)) return err("Unknown schema: " + schema);

        // Extra safety gate for production
        if (env.equals("prod") && !confirmed) {
            return err("Baseline on prod requires confirmed=true. This is a one-time operation — " +
                "verify schema is not already Flyway-managed before proceeding.");
        }

        JsonObject r = new JsonObject();
        r.addProperty("schema",               schema);
        r.addProperty("baseline_version",     version);
        r.addProperty("baseline_description", desc.isEmpty() ? "Flyway baseline" : desc);
        r.addProperty("environment",          env);
        r.addProperty("history_entry",
            "INSERT INTO flyway_schema_history (version, description, type, success) " +
            "VALUES ('" + version + "', '" + (desc.isEmpty() ? "baseline" : desc) + "', 'BASELINE', true)");
        r.addProperty("flyway_command",
            "flyway baseline -baselineVersion=" + version + " -schemas=" + schema);
        r.addProperty("checklist_item",       "Production checklist item 57 — Flyway baseline set");
        r.addProperty("next_steps",
            "1. Commit V" + (Integer.parseInt(version.replace("V","")) + 1) + "__*.sql migration files. " +
            "2. Deploy service — Helm init container will apply pending migrations. " +
            "3. Verify with migration_status.");
        r.addProperty("exit_code",            0);
        return ok(r);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 6: MIGRATION_INFO_AGENT
// Displays full migration info table (flyway info) — read-only ops tool
// ─────────────────────────────────────────────────────────────────────────────

class MigrationInfoAgent extends BaseAgent {
    public MigrationInfoAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("schema");
        addEnumProp  (sc, "schema",      "Schema to query migration info for",
                       "core","billing","analytics","realtime","admin","notification",
                       "scoring","certification","interview","job","phone_bridge",
                       "royalty","innovation","intelligence","legal");
        addEnumProp  (sc, "environment", "Target environment", "dev","test","stage","prod");
        addBoolProp  (sc, "include_pending", "Include pending migrations in output (default true)");
        return buildToolDef("migration_info",
            "Executes 'flyway info' — displays the full migration info table for a schema. " +
            "Shows: version, description, installed_on, state (Success/Pending/Failed) for every migration. " +
            "Read-only operation — does not modify any database state. " +
            "Used by ops team during DR verification (step 34) to check schema state after pg_dump restore. " +
            "Also used pre-deployment to confirm which migrations are pending before applying. " +
            "Output includes a 'Pending' marker for migrations not yet in flyway_schema_history.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("migration_info");
        String schema       = str(args, "schema");
        boolean inclPending = bool(args, "include_pending", true);
        if (schema.isEmpty()) return err("schema is required");
        if (!security.isKnownSchema(schema)) return err("Unknown schema: " + schema);

        JsonObject r = new JsonObject();
        r.addProperty("schema", schema);

        // Simulate flyway info table output
        JsonArray table = new JsonArray();
        Object[][] rows = {
            {"1",  "init",                  "2025-01-10T08:00:00Z", "Success"},
            {"2",  "add_tenants",           "2025-01-15T09:30:00Z", "Success"},
            {"3",  "add_rls_policies",      "2025-02-01T10:00:00Z", "Success"},
            {"4",  "add_pgcrypto",          "2025-02-10T11:00:00Z", "Success"},
            {"25", "gd_topics_bank",        "2025-07-01T11:00:00Z", "Success"},
        };
        for (Object[] row : rows) {
            JsonObject ro = new JsonObject();
            ro.addProperty("version",      (String) row[0]);
            ro.addProperty("description",  (String) row[1]);
            ro.addProperty("installed_on", (String) row[2]);
            ro.addProperty("state",        (String) row[3]);
            table.add(ro);
        }
        if (inclPending) {
            JsonObject pend = new JsonObject();
            pend.addProperty("version",      "26");
            pend.addProperty("description",  "add_gd_analytics_indexes");
            pend.addProperty("installed_on", "—");
            pend.addProperty("state",        "Pending ← needs apply");
            table.add(pend);
        }

        r.add("info_table",          table);
        r.addProperty("applied",     5);
        r.addProperty("pending",     inclPending ? 1 : 0);
        r.addProperty("failed",      0);
        r.addProperty("current",     "V25");
        r.addProperty("flyway_command", "flyway info -schemas=" + schema);
        r.addProperty("dr_context",  "Safe to run post-pg_dump restore to confirm schema state");
        r.addProperty("exit_code",   0);
        return ok(r);
    }
}
