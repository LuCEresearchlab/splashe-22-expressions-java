package ch.usi.inf.luce.expr.analyzer.stats;

public class StatsInfo {
    private final TreePath treePath;
    private final int charCount;
    private final int tokenCount;

    public StatsInfo(final TreePath treePath, final int charCount, final int tokenCount) {
        this.treePath = treePath;
        this.charCount = charCount;
        this.tokenCount = tokenCount;
    }

    public TreePath getTreePath() {
        return treePath;
    }
    
    public int getCharCount() {
        return charCount;
    }

    public int getTokenCount() {
        return tokenCount;
    }

}

