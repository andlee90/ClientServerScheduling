package Server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by andrewsmith on 3/31/17.
 */
public class ServerDB
{
    public static void createNewDatabase(String fileName)
    {
        String url = "jdbc:sqlite:sqlite/db/" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database, " + fileName + ", has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
