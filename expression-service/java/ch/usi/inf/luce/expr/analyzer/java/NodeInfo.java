package ch.usi.inf.luce.expr.analyzer.java;

import ch.usi.inf.luce.expr.analyzer.core.ScopedTypingMap;
import ch.usi.inf.luce.expr.diagram.Node;
import ch.usi.inf.luce.expr.util.range.IntRange;
import org.jetbrains.annotations.NotNull;

public final class NodeInfo {
    public final long id;
    @NotNull
    public final IntRange location;
    @NotNull
    public final ScopedTypingMap typingMap;
    @NotNull
    public final Node node;

    public NodeInfo(long id,
                    @NotNull IntRange location,
                    @NotNull ScopedTypingMap typingMap,
                    @NotNull Node node) {
        this.id = id;
        this.location = location;
        this.typingMap = typingMap;
        this.node = node;
    }
}
