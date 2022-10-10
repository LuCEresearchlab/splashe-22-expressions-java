package ch.usi.inf.luce.expr.util.csv;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Csv writer util class that produces
 * <a href="https://datatracker.ietf.org/doc/html/rfc4180">RFC-4180</a>
 * compliant CSV files string contents.
 */
public final class CsvWriter {
    private static final String END_LINE = "\r\n";
    // RFC-4180.2.6: Fields containing line breaks (CRLF), double quotes,
    //               and commas should be enclosed in double-quotes.
    private static final Pattern TO_ESCAPE = Pattern.compile("[\r\n\",]");

    @NotNull
    @SuppressWarnings("PMD.AvoidStringBufferField")
    private final StringBuilder sb = new StringBuilder();
    private int numColumns = -1;

    /**
     * Escape strings according to the specifications.
     * <ul>
     *     <li><code>RFC-4180.2.5</code>: Each field may or may not be
     *         enclosed in double quotes [...]</li>
     *     <li><code>RFC-4180.2.6</code>: Fields containing line breaks
     *         (CRLF), double quotes, and commas should be enclosed
     *         in double-quotes.</li>
     *     <li><code>RFC-4180.2.7</code>: If double-quotes are used to
     *         enclose fields, then a double-quote appearing inside
     *         a field must be escaped by preceding it with
     *         another double quote.</li>
     * </ul>
     */
    @NotNull
    private static String escape(@NotNull String string) {
        if (TO_ESCAPE.matcher(string).find()) {
            return '"' + string.replace("\"", "\"\"") + '"';
        } else {
            return string;
        }
    }

    public void writeHeader(@NotNull String... columns) {
        if (numColumns != -1 || sb.length() != 0) {
            throw new IllegalStateException("Headers have already been written");
        }

        numColumns = columns.length;

        sb.append(Arrays.stream(columns)
                        .map(CsvWriter::escape)
                        .collect(Collectors.joining(",")))
                .append(END_LINE);
    }

    public void writeRow(@NotNull Object... row) {
        if (numColumns == -1 && sb.length() == 0) {
            // First row (no headers)
            numColumns = row.length;
        } else if (row.length != numColumns) {
            // Mismatched count of columns
            throw new IllegalArgumentException("Expected " + numColumns + " columns, got " + row.length);
        }
        sb.append(Arrays.stream(row)
                        .map(field -> {
                            if (field instanceof Boolean
                                    || field instanceof Character
                                    || field instanceof Integer
                                    || field instanceof Long) {
                                return field.toString();
                            } else if (field instanceof Float) {
                                return Float.toString((Float) field);
                            } else if (field instanceof Double) {
                                return Double.toString((Double) field);
                            } else {
                                return escape(field.toString());
                            }
                        })
                        .collect(Collectors.joining(",")))
                .append(END_LINE);
    }

    @NotNull
    public String getCsv() {
        return sb.toString();
    }
}
