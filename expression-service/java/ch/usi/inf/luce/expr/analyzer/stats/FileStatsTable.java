package ch.usi.inf.luce.expr.analyzer.stats;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FileStatsTable {
    private final JavaStatsDbManager dbManager;
    public static final String TABLE_NAME = "filestats";

    public FileStatsTable(final JavaStatsDbManager dbManager) {
        this.dbManager = dbManager;
    }

    public int countSuccessfullyCompiledFiles() throws SQLException {
        final String query = "SELECT COUNT(filename) FROM " + TABLE_NAME + " WHERE compiles = 1;";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            return pstmt.executeQuery().getInt(1);
        }
    }
    
    public void saveCompiledFileStats(final String fileName, final int chars, final int tokens, 
                                       final int classes, final int methods, final int nodes) throws SQLException {
        saveFileStats(fileName, true, chars, tokens, classes, methods, nodes);
    }

    public void saveProblematicFileStats(final String fileName) throws SQLException {
        saveFileStats(fileName, false, 0, 0, 0, 0, 0);
    }
    
    private void saveFileStats(final String fileName, final boolean compiles, final int chars, final int tokens, 
                           final int classes, final int methods, final int nodes) throws SQLException {
        final String query = "INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, fileName);
            pstmt.setInt(2, compiles ? 1 : 0);
            pstmt.setInt(3, chars);
            pstmt.setInt(4, tokens);
            pstmt.setInt(5, classes);
            pstmt.setInt(6, methods);
            pstmt.setInt(7, nodes);
            pstmt.executeUpdate();
        }
    }
    
    public int getTotalTokens() throws SQLException {
        return sumColumn("tokens");
    }

    public int getTotalChars() throws SQLException {
        return sumColumn("chars");
    }
    
    private int sumColumn(final String columnName) throws SQLException {
        final String query = String.format("SELECT SUM(%s) FROM " + TABLE_NAME + ";", columnName);
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            return pstmt.executeQuery().getInt(1);
        }
    }
}
