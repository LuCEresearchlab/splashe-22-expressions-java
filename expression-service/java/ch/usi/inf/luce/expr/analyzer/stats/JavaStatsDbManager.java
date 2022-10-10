package ch.usi.inf.luce.expr.analyzer.stats;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

public class JavaStatsDbManager {

    private final Connection conn;
    private final Logger logger = Logger.getLogger("JavaStatsDbManager");

    public JavaStatsDbManager(@NotNull String dbFile) {
        conn = connectToDatabase(dbFile);
    }
    
    public JavaStatsDbManager(@NotNull Path dbFile) {
        conn = connectToDatabase(dbFile.toString());
    }

    public Connection getConnection() {
        return conn;
    }

    public void executeQueries(final String queries) {
        // Note: this assumes that queries contains one SQL statement per line.
        for (final String query : queries.split("\\r?\\n")) {
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.executeUpdate();
            } catch (SQLException e) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE, "Failed to execute queries using SQLite database", e);
                }
            }
        }
    }

    private Connection connectToDatabase(final String dbFile) {
        final SQLiteConfig config = new SQLiteConfig();
        config.setBusyTimeout(60_000);
        final SQLiteDataSource ds = new SQLiteDataSource(config);
        Connection conn = null;
        try {
            ds.setUrl("jdbc:sqlite:" + dbFile);
            conn = ds.getConnection();
        } catch (SQLException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "Failed to connect to SQLite database", e);
            }
        }
        return conn;
    }

}
