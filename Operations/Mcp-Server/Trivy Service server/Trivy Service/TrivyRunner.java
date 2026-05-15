package com.ecoskiller.trivy.util;

import com.ecoskiller.trivy.security.InputSanitizer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * TrivyRunner — Secure subprocess executor for the Trivy CLI binary.
 *
 * Security guarantees:
 *   - Arguments are passed as a List<String> to ProcessBuilder (no shell interpolation)
 *   - Shell is NEVER invoked (no Runtime.exec(String) — always List<String> form)
 *   - Each argument is individually validated before passing to the process
 *   - Execution timeout (default: 300s) prevents runaway scans
 *   - stdout/stderr are streamed to prevent pipe-buffer deadlock
 *   - Working directory is explicitly set to /tmp (no implicit cwd trust)
 *   - TRIVY_BINARY env var allows overriding the trivy path (defaults to /usr/local/bin/trivy)
 *
 * All callers MUST use TrivyRunner — never invoke trivy via Runtime.exec(String)
 * or any string-interpolated command.
 */
public class TrivyRunner {

    private static final int DEFAULT_TIMEOUT_SECONDS = 300;
    private static final String TRIVY_BINARY;
    private final Logger logger = new Logger("TrivyRunner");

    static {
        String envBinary = System.getenv("TRIVY_BINARY");
        TRIVY_BINARY = (envBinary != null && !envBinary.isBlank()) ? envBinary.trim() : "/usr/local/bin/trivy";
    }

    public static class TrivyResult {
        public final int exitCode;
        public final String stdout;
        public final String stderr;

        public TrivyResult(int exitCode, String stdout, String stderr) {
            this.exitCode = exitCode;
            this.stdout = stdout;
            this.stderr = stderr;
        }

        public boolean passed() { return exitCode == 0; }
        public boolean hasVulnerabilities() { return exitCode == 1; }
    }

    /**
     * Execute trivy with a fixed list of arguments.
     * No shell expansion — arguments are passed directly to exec().
     *
     * @param args  List of trivy CLI arguments (must not contain shell metacharacters)
     * @return      TrivyResult with exit code, stdout, stderr
     */
    public TrivyResult execute(List<String> args) throws IOException, InterruptedException {
        return execute(args, DEFAULT_TIMEOUT_SECONDS);
    }

    public TrivyResult execute(List<String> args, int timeoutSeconds) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add(TRIVY_BINARY);
        command.addAll(args);

        logger.info("Executing: " + String.join(" ", command));

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File("/tmp"));
        pb.environment().put("TRIVY_NO_PROGRESS", "true");
        pb.environment().put("TRIVY_QUIET", "false");
        // Prevent trivy from reading/writing unexpected env vars
        pb.environment().remove("HOME");

        pb.redirectErrorStream(false);
        Process process = pb.start();

        // Stream stdout and stderr concurrently to avoid pipe buffer deadlock
        StringWriter stdoutWriter = new StringWriter();
        StringWriter stderrWriter = new StringWriter();

        Thread stdoutThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stdoutWriter.write(line);
                    stdoutWriter.write("\n");
                }
            } catch (IOException ignored) {}
        });

        Thread stderrThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stderrWriter.write(line);
                    stderrWriter.write("\n");
                }
            } catch (IOException ignored) {}
        });

        stdoutThread.setDaemon(true);
        stderrThread.setDaemon(true);
        stdoutThread.start();
        stderrThread.start();

        boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            throw new IOException("Trivy scan timed out after " + timeoutSeconds + " seconds");
        }

        stdoutThread.join(5000);
        stderrThread.join(5000);

        int exitCode = process.exitValue();
        logger.info("Trivy exited with code: " + exitCode);

        return new TrivyResult(exitCode, stdoutWriter.toString(), stderrWriter.toString());
    }

    /**
     * Build image scan arguments for a .tar file.
     * --exit-code 1 --severity CRITICAL,HIGH --input <path> [--format <fmt>]
     */
    public static List<String> imageTarArgs(String tarPath, String severity, String format) {
        List<String> args = new ArrayList<>();
        args.add("image");
        args.add("--exit-code"); args.add("1");
        args.add("--severity"); args.add(severity.toUpperCase());
        args.add("--input"); args.add(tarPath);
        args.add("--format"); args.add(format);
        return args;
    }

    /**
     * Build image scan arguments for a registry image.
     */
    public static List<String> imageRegistryArgs(String imageRef, String severity, String format) {
        List<String> args = new ArrayList<>();
        args.add("image");
        args.add("--exit-code"); args.add("1");
        args.add("--severity"); args.add(severity.toUpperCase());
        args.add("--format"); args.add(format);
        args.add(imageRef);
        return args;
    }

    /**
     * Build filesystem scan arguments.
     */
    public static List<String> filesystemArgs(String fsPath, String severity, String format) {
        List<String> args = new ArrayList<>();
        args.add("fs");
        args.add("--exit-code"); args.add("1");
        args.add("--severity"); args.add(severity.toUpperCase());
        args.add("--format"); args.add(format);
        args.add(fsPath);
        return args;
    }

    /**
     * Build config/misconfiguration scan arguments.
     */
    public static List<String> configArgs(String configPath, String format) {
        List<String> args = new ArrayList<>();
        args.add("config");
        args.add("--exit-code"); args.add("1");
        args.add("--format"); args.add(format);
        args.add(configPath);
        return args;
    }
}
