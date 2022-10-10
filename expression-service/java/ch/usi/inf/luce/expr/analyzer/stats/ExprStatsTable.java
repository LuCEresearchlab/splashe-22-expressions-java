package ch.usi.inf.luce.expr.analyzer.stats;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class ExprStatsTable {
    private final JavaStatsDbManager dbManager;
    public static final String TABLE_NAME = "exprstats";

    public ExprStatsTable(final JavaStatsDbManager dbManager) {
        this.dbManager = dbManager;
    }

    public void saveExpressionsStats(final String fileName, final Set<StatsInfo> stats) throws SQLException {
        final String query = "INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, fileName);
            for (final StatsInfo stat : stats) {
                final TreePath path = stat.getTreePath();
                pstmt.setString(2, path.getUniqueStringRepresentation());
                pstmt.setString(3, path.pathToExprRoot().getUniqueStringRepresentation());
                pstmt.setString(4, path.getExprConstructName());
                pstmt.setString(5, path.pathToDeclaringNode().getUniqueStringRepresentation());
                pstmt.setInt(6, (int) path.depth());
                pstmt.setInt(7, stat.getCharCount());
                pstmt.setInt(8, stat.getTokenCount());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
    
    public int countDistinctExpressionsInFile(final String filename) throws SQLException {
        final String query = "SELECT COUNT(DISTINCT path_expr_root) FROM " + TABLE_NAME + " WHERE filename = ?;";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, filename);
            return pstmt.executeQuery().getInt(1);
        }
    }

    public int countExpressionTokensInFile(final String fileName) throws SQLException {
        return countExpressionLengthInFile(fileName, "tokens");
    }
    
    public int countExpressionCharsInFile(final String fileName) throws SQLException {
        return countExpressionLengthInFile(fileName, "chars");
    }

    public int countExpressionAstNodesInFile(final String fileName) throws SQLException {
        final String query = "SELECT COUNT(ast_node) FROM " + TABLE_NAME + " WHERE filename = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, fileName);
            return pstmt.executeQuery().getInt(1);
        }
    }

    private int countExpressionLengthInFile(final String fileName, final String column) throws SQLException {
        // The condition "path_expr = path_expr_root" selects only expressions
        // at the root of their "expression subtrees".
        // Their length already encompasses the whole subtree.
        final String query = "SELECT SUM(" + column + ") FROM " + TABLE_NAME + " WHERE filename = ?"
                             + "AND path_expr = path_expr_root;";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, fileName);
            return pstmt.executeQuery().getInt(1);
        }
    }

    public int countAstNodeInFile(final String filename, final String astNode) throws SQLException {
        final String query = "SELECT COUNT(ast_node) FROM " + TABLE_NAME + " WHERE filename = ? AND ast_node = ?;";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, filename);
            pstmt.setString(2, astNode);
            return pstmt.executeQuery().getInt(1);
        }
    }
}
