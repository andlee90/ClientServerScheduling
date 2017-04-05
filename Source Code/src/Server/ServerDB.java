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
     * Creates a new database named client_scheduler.db if one does not already exist.
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

    /**
     * Creates a users table if one does not already exist.
     */
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

    /**
     * Creates a schedules table if one does not already exist.
     */
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

    /**
     * Creates a users-schedules linker table if one does not already exist.
     */
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

    /**
     * Creates a connection to client_scheduler.db.
     */
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
     * Insert a new row into the users table
     *
     * @param user username of the user to be inserted.
     * @param pass password of the user to be inserted.
     * @param ln last name of the user to be inserted.
     * @param fn first name of the user to be inserted.
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

    /**
     * Returns a schedule from the schedules table with the corresponding id.
     *
     * @param schedId the id of the schedule to select
     */
    static String selectScheduleByScheduleId(int schedId)
    {
        String sql = "SELECT schedule_day, schedule_time FROM schedules WHERE schedule_id = ?";
        String schedule = "";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1,schedId);
            ResultSet rs    = pstmt.executeQuery();

            while (rs.next())
            {
                schedule = rs.getString("schedule_day")
                        + " " + rs.getString("schedule_time");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return schedule;
    }

    /**
     * Returns an arraylist of all schedule ids from the user_schedules table for the corresponding user id.
     *
     * @param userId the id of the user to select
     */
    static ArrayList<Integer> selectAllScheduleIdsByUserId(int userId)
    {
        String sql = "SELECT schedule_id FROM user_schedules WHERE user_id = ?";
        ArrayList<Integer> scheduleIds = new ArrayList<>();

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1,userId);
            ResultSet rs    = pstmt.executeQuery();

            while (rs.next())
            {
                scheduleIds.add(rs.getInt("schedule_id"));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return scheduleIds;
    }

    /**
     * Returns an user id for the corresponding username from the users table.
     *
     * @param username the username of the user to select.
     */
    static int selectUserIdByUsername(String username)
    {
        String sql = "SELECT user_id FROM users WHERE user_username = ?";
        int userId = 1;
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,username);
            ResultSet rs    = pstmt.executeQuery();

            while (rs.next())
            {
                userId = rs.getInt("user_id");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return userId;
    }

    /**
     * Returns an arraylist of all usernames from the users table.
     */
    static ArrayList<String> selectAllUsernames()
    {
        String sql = "SELECT user_username FROM users";
        ArrayList<String> usernames = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                usernames.add(rs.getString("user_username"));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return usernames;
    }

    /**
     * Returns an arraylist of all user ids from the users table.
     */
    public static ArrayList<Integer> selectAllUserIds()
    {
        String sql = "SELECT user_id FROM users";
        ArrayList<Integer> userIds = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                userIds.add(rs.getInt("user_id"));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return userIds;
    }

    /**
     * Removes user with corresponding id from users table.
     *
     * @param userId the id of the user to be deleted.
     */
    public void delete(int userId)
    {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
