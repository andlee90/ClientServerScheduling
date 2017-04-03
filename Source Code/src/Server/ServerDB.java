package Server;

import java.sql.*;
import java.util.ArrayList;

/**
 * Manages all database transactions.
 */
class ServerDB
{
    private static String dbUrl = "jdbc:sqlite:sqlite/db/client_scheduler.db";

    /**
     * Creates a new database named client_scheduler.db.
     */
    static void createDB()
    {
        try (Connection conn = DriverManager.getConnection(dbUrl))
        {
            if (conn != null)
            {
                System.out.println("Connected to client_scheduler.db");
                createUsersTable();
                createSchedulesTable();
                createUserSchedTable();
            }

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void createUsersTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "user_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
                + "user_username TEXT NOT NULL UNIQUE,\n"
                + "user_password TEXT NOT NULL,\n"
                + "user_last_name TEXT NOT NULL,\n"
                + "user_first_name INTEGER NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement())
        {
            stmt.execute(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void createSchedulesTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS schedules (\n"
                + "schedule_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
                + "schedule_day TEXT NOT NULL,\n"
                + "schedule_time TEXT NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement())
        {
            stmt.execute(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void createUserSchedTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS user_schedules (\n"
                + "user_schedule_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
                + "user_id INTEGER NOT NULL,\n"
                + "schedule_id INTEGER NOT NULL,\n"
                + "FOREIGN KEY(user_id) REFERENCES users(user_id),\n"
                + "FOREIGN KEY(schedule_id) REFERENCES schedules(schedule_id)\n"
                + ");";



        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement())
        {
            stmt.execute(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static Connection connect()
    {
        Connection conn = null;
        try
        {
            conn = DriverManager.getConnection(dbUrl);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Insert a new row into the warehouses table
     *
     * @param user
     * @param pass
     * @param ln
     * @param fn
     */
    public static void insertUser(String user, String pass, String ln, String fn)
    {
        String sql = "INSERT INTO users(user_username,user_password,user_last_name,user_first_name) VALUES(?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.setString(3, ln);
            pstmt.setString(4, fn);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<String> selectAllUsers()
    {
        String sql = "SELECT user_username FROM users";
        ArrayList<String> usernames = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                //System.out.println(rs.getString("user_username"));
                usernames.add(rs.getString("user_username"));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return usernames;
    }

    public void delete(int id)
    {
        String sql = "DELETE FROM warehouses WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
