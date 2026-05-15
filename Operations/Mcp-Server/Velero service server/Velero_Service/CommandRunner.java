package com.ecoskiller.mcp.velero.util;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

/**
 * CommandRunner — executes Velero CLI commands securely.
 *
 * SECURITY: Uses ProcessBuilder with an explicit argument list — NEVER
 * passes arguments through a shell string. This prevents ALL shell
 * injection attacks regardless of input content.
 *
 * Timeout: 5 minutes per command (velero restore can be slow).
 */
public class CommandRunner {

    private static final Logger LOGGER      = Logger.getLogger(CommandRunner.class.getName());
    private static final int    TIMEOUT_SEC = 300; // 5 minutes

    public static final class Result {
        public final int    exitCode;
        public final String stdout;
        public final String stderr;

        Result(int exitCode, String stdout, String stderr) {
            this.exitCode = exitCode;
            this.stdout   = stdout;
            this.stderr   = stderr;
        }

        public boolean success() { return exitCode == 0; }

        public String summary() {
            StringBuilder sb = new StringBuilder();
            if (!stdout.isBlank()) sb.append(stdout.trim());
            if (!stderr.isBlank()) {
                if (!sb.isEmpty()) sb.append("\n");
                sb.append("[stderr] ").append(stderr.trim());
            }
            return sb.toString();
        }
    }

    /**
     * Run a velero command.
     *
     * @param args Velero arguments — e.g. ["backup", "create", "my-backup"]
     *             DO NOT include "velero" itself — it is prepended automatically.
     * @return Result containing stdout, stderr, exitCode
     */
    public Result run(List<String> args) {
        List<String> cmd = new ArrayList<>();
        cmd.add("velero");
        cmd.addAll(args);

        LOGGER.info("[CMD] " + String.join(" ", cmd));

        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.environment().put("PATH", "/usr/local/bin:/usr/bin:/bin");
            pb.redirectErrorStream(false);

            Process process = pb.start();

            // Read stdout and stderr concurrently to prevent buffer deadlock
            Future<String> stdoutFuture = readAsync(process.getInputStream());
            Future<String> stderrFuture = readAsync(process.getErrorStream());

            boolean finished = process.waitFor(TIMEOUT_SEC, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                LOGGER.warning("[CMD] Timeout after " + TIMEOUT_SEC + "s: " + String.join(" ", cmd));
                return new Result(-1, "", "Command timed out after " + TIMEOUT_SEC + " seconds");
            }

            String stdout = stdoutFuture.get(5, TimeUnit.SECONDS);
            String stderr = stderrFuture.get(5, TimeUnit.SECONDS);
            int    code   = process.exitValue();

            LOGGER.info("[CMD] exitCode=" + code);
            return new Result(code, stdout, stderr);

        } catch (Exception e) {
            LOGGER.severe("[CMD] Execution failed: " + e.getMessage());
            return new Result(-1, "", "Execution error: " + e.getMessage());
        }
    }

    /** Convenience for dry-run (appends --dry-run flag). */
    public Result dryRun(List<String> args) {
        List<String> withDry = new ArrayList<>(args);
        withDry.add("--dry-run");
        return run(withDry);
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private Future<String> readAsync(InputStream is) {
        return Executors.newSingleThreadExecutor().submit(() -> {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            }
        });
    }
}
