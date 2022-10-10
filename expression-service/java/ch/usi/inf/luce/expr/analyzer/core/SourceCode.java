package ch.usi.inf.luce.expr.analyzer.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SourceCode {
    private static final char NL = '\n';

    @NotNull
    public final String fileName;
    @NotNull
    public final String code;

    public SourceCode(@NotNull String code) {
        this("", code);
    }

    @JsonCreator
    public SourceCode(@JsonProperty("fileName") @Nullable String fileName,
                      @JsonProperty("code") @NotNull String code) {
        this.fileName = fileName == null ? "" : fileName;
        this.code = code;
    }

    public int getLineNumber(int position) {
        int line = 1;
        final int n = code.length();
        for (int i = 0; i < position && i < n; i++) {
            if (code.charAt(i) == NL) {
                line++;
            }
        }
        return line;
    }

    public String getCodeSubstring(int startPosition, int length) {
        return code.substring(startPosition, startPosition + length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourceCode)) {
            return false;
        }
        final SourceCode that = (SourceCode) o;
        return fileName.equals(that.fileName)
                && code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, code);
    }
}
