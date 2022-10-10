package ch.usi.inf.luce.expr.analyzer.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ScopedTypingMap {
    @Nullable
    private final ScopedTypingMap parent;
    @NotNull
    private final Set<Entry> entries;

    @JsonCreator
    public ScopedTypingMap(@JsonProperty("parent") @Nullable ScopedTypingMap parent,
                           @JsonProperty("entries") @NotNull Set<Entry> entries) {
        this.parent = parent;
        this.entries = entries;
    }

    @NotNull
    public Optional<ScopedTypingMap> getParent() {
        return Optional.ofNullable(parent);
    }

    @NotNull
    public Set<Entry> getEntries() {
        final HashSet<Entry> allEntries = new HashSet<>(entries);
        if (parent != null) {
            allEntries.addAll(parent.getEntries());
        }
        return allEntries;
    }

    public void add(@NotNull Entry entry) {
        entries.add(entry);
    }

    @Override
    public String toString() {
        return (parent == null ? "" : parent + ", ")
                + entries.stream().map(Objects::toString).collect(Collectors.joining(", "));
    }

    public static final class Entry {
        @NotNull
        public final String type;
        @NotNull
        public final String name;

        @JsonCreator
        public Entry(@JsonProperty("type") @NotNull String type,
                     @JsonProperty("name") @NotNull String name) {
            this.type = type;
            this.name = name;
        }

        @Override
        public String toString() {
            return type + ' ' + name;
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Entry)) {
                return false;
            }
            final Entry that = (Entry) o;
            return type.equals(that.type)
                    && name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, name);
        }
    }
}
