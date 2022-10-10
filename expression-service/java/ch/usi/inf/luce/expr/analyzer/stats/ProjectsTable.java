package ch.usi.inf.luce.expr.analyzer.stats;

import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectsTable {
    private final JavaStatsDbManager dbManager;
    public static final String TABLE_NAME = "projects";

    public ProjectsTable(final JavaStatsDbManager dbManager) {
        this.dbManager = dbManager;
    }

    public void saveFileInProject(final Path projectPath, final String fileName) throws SQLException {
        final String query = "INSERT INTO " + TABLE_NAME + " VALUES (?,?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, projectPath.toString());
            pstmt.setString(2, fileName);
            pstmt.executeUpdate();
        }
    }
    
    public int countFilesInProject(final Path projectPath) throws SQLException {
        final String query = "SELECT COUNT(filename) FROM " + TABLE_NAME + " WHERE project_path = ?;";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, projectPath.toString());
            return pstmt.executeQuery().getInt(1);
        }
    }
}
