package database;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String APP_DIR =
            System.getProperty("user.home") + "/dairy";

    private static final String DB_PATH =
            APP_DIR + "/life108.db";

    private static final String URL =
            "jdbc:sqlite:" + DB_PATH;

    public static Connection getConnection() {
        try {
            ensureDatabaseExists();
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void ensureDatabaseExists() {
        try {
            File dir = new File(APP_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dbFile = new File(DB_PATH);
            if (!dbFile.exists()) {
                try (InputStream is =
                        DatabaseConnection.class.getResourceAsStream("/life108.db")) {

                    if (is == null) {
                        throw new RuntimeException("❌ life108.db not found in resources/");
                    }

                    Files.copy(is, dbFile.toPath(),
                            StandardCopyOption.REPLACE_EXISTING);

                    System.out.println("✅ Database initialized at " + DB_PATH);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to initialize database", e);
        }
    }
}
