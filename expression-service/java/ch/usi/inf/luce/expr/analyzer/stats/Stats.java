package ch.usi.inf.luce.expr.analyzer.stats;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"PMD.TooManyMethods"})
public class Stats {
    private final JavaStatsDbManager dbManager;

    public Stats(final JavaStatsDbManager dbManager) {
        this.dbManager = dbManager;
    }
    
    // ===== How much code is expressions? =====

    public double expressionsPercentageInTokens() throws SQLException {
        return expressionsPercentage("tokens");
    }

    public double expressionsPercentageInChars() throws SQLException {
        return expressionsPercentage("chars");
    }

    public double avgExpressionNodesPerClass() throws SQLException {
        return avgExpressionNodesPerCodeUnit("classes");
    }

    public double avgExpressionNodesPerMethod() throws SQLException {
        return avgExpressionNodesPerCodeUnit("methods");
    }

    // ===== Size of expressions =====

    public double avgAstNodesPerExpression() throws SQLException {
        final String query = String.format(
                "SELECT (CAST(expr_ast_nodes AS REAL) / total_expressions) AS avg_expr_ast_nodes_per_expr FROM "
                + "(SELECT COUNT(*) AS total_expressions FROM %1$s WHERE path_expr = path_expr_root), "
                + "(SELECT COUNT(*) AS expr_ast_nodes FROM %1$s)",
                ExprStatsTable.TABLE_NAME);
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            return pstmt.executeQuery().getDouble(1);
        }
    }

    public double avgTreeHeightPerExpression() throws SQLException {
        // This considers expressions as trees.
        // The height of a tree is the number of levels in the tree - 1.
        // Observe that this means that a tree with just one node (and thus one level)
        // has height 0, which might not be what we want.
        final String query = String.format(
                "SELECT AVG(tree_height) AS avg_tree_levels FROM "
                + "(SELECT path_expr_root, MAX(depth) AS tree_height FROM %1$s GROUP BY path_expr_root)",
                ExprStatsTable.TABLE_NAME);
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            return pstmt.executeQuery().getDouble(1);
        }
    }

    // ===== Different kinds of expressions =====

    public Map<String, Double> expressionNodesPrevalence() throws SQLException {
        final Map<String, Double> nodePrevalence = new HashMap<>();
        final String query = String.format(
                "SELECT ast_node, ((CAST(COUNT(*) AS REAL)) / (SELECT COUNT(*) FROM %1$s)) AS percentage_over_all_nodes"
                + " FROM %1$s GROUP BY ast_node",
                ExprStatsTable.TABLE_NAME);
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            final ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                nodePrevalence.put(rs.getString(1), rs.getDouble(2));
            }
            return nodePrevalence;
        }
    }

    public Map<String, Double> expressionNodesPresenceInProjects() throws SQLException {
        final Map<String, Double> nodeInProjects = new HashMap<>();
        final String query = String.format(
                "SELECT ast_node, (CAST(COUNT(DISTINCT project_path) AS REAL)) / "
                + " (SELECT COUNT(DISTINCT project_path) FROM projects) AS percentage_projects_containing_node"
                + " FROM %1$s INNER JOIN %2$s ON %2$s.filename = %1$s.filename"
                + " GROUP BY ast_node",
                ExprStatsTable.TABLE_NAME, ProjectsTable.TABLE_NAME);
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            final ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                nodeInProjects.put(rs.getString(1), rs.getDouble(2));
            }
            return nodeInProjects;
        }
    }
    
    // ===== Private methods =====

    private double expressionsPercentage(final String columnName) throws SQLException {
        final String query = String.format(
                "SELECT (CAST(expr_length AS REAL) / total_length) AS expr_length_percentage FROM "
                + "(SELECT SUM(%1$s) AS expr_length FROM %2$s WHERE path_expr = path_expr_root), "
                + "(SELECT SUM(%1$s) AS total_length FROM %3$s);",
                columnName, ExprStatsTable.TABLE_NAME, FileStatsTable.TABLE_NAME);
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            return pstmt.executeQuery().getDouble(1);
        }
    }
    
    private double avgExpressionNodesPerCodeUnit(final String columnName) throws SQLException {
        final String query = String.format(
                "SELECT (CAST(expr_ast_nodes AS REAL) / total) AS avg_expr_ast_nodes_per_code_unit FROM "
                + "(SELECT SUM(%1$s) AS total FROM %2$s), "
                + "(SELECT COUNT(*) AS expr_ast_nodes FROM %3$s)",
                columnName, FileStatsTable.TABLE_NAME, ExprStatsTable.TABLE_NAME);
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            return pstmt.executeQuery().getDouble(1);
        }
    }
}
