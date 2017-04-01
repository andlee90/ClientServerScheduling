package Server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages all database transactions.
 */
class ServerDB
{
    /**
     * Opens schedules.db or creates a new database named schedules.db
     */
    static void createDB()
    {
        String url = "jdbc:sqlite:sqlite/db/schedules.db";

        try (Connection conn = DriverManager.getConnection(url))
        {
            if (conn != null)
            {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The db driver is " + meta.getDriverName());
                System.out.println("Connected to the " + "schedules.db" + " database");
            }

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
