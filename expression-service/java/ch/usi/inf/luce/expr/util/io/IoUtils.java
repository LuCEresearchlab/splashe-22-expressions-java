package ch.usi.inf.luce.expr.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Commonly used functions for I/O operations.
 */
public final class IoUtils {
    private static final int BUFFER_SIZE = 1024;

    private static final Logger LOG = Logger.getLogger("IoUtil");

    private IoUtils() {
    }
    
    public static String readFile(final String name) throws IOException {
        final URL url = Thread.currentThread().getContextClassLoader().getResource(name);
        if (url == null) {
            throw new IOException("Failed to obtain file URL");
        } else {
            return IoUtils.readContent(url.openStream(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Read the contents of a {@link InputStream} as a String.
     */
    public static String readContent(InputStream inStream) {
        return readContent(inStream, Charset.defaultCharset());
    }

    /**
     * Read the contents of a {@link InputStream} as a String
     * with a specified {@link Charset}.
     */
    public static String readContent(InputStream inStream,
                                     Charset charset) {
        final var sb = new StringBuilder();
        final var inputStreamReader = new InputStreamReader(inStream, charset);
        try (var reader = new BufferedReader(inputStreamReader)) {
            final var buffer = new char[BUFFER_SIZE];
            int read = reader.read(buffer, 0, BUFFER_SIZE);
            while (read > 0) {
                sb.append(buffer, 0, read);
                read = reader.read(buffer, 0, BUFFER_SIZE);
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Failed to read InputStream", e);
        }
        return sb.toString();
    }
}
