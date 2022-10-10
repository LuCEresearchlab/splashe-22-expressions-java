package ch.usi.inf.luce.expr.util.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Class for debug logging.
 */
public final class DebugLogger implements Closeable {

    private static final Logger SYS_LOG = Logger.getLogger("DebugLog");

    @NotNull
    private final OutputStream debugStream;
    private final boolean enabled;

    public DebugLogger(@NotNull OutputStream debugStream,
                       boolean enabled) {
        this.debugStream = debugStream;
        this.enabled = enabled;
    }

    @Override
    public void close() throws IOException {
        debugStream.flush();
        if (!System.err.equals(debugStream)) {
            debugStream.close();
        }
    }

    public synchronized void log(int indent, @NotNull String base, @NotNull Object... args) {
        if (!enabled) {
            return;
        }

        log("\t".repeat(Math.max(0, indent)) + base, args);
    }

    public synchronized void log(@NotNull String base, @NotNull Object... args) {
        if (!enabled) {
            return;
        }

        final String formatted = String.format(base, args);
        final byte[] bytes = formatted.getBytes(StandardCharsets.UTF_8);
        try {
            debugStream.write(bytes);
            debugStream.flush();
        } catch (IOException e) {
            SYS_LOG.log(Level.WARNING, "Failed to write debug log", e);
        }
    }
}
