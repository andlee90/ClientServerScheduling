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
     * Creates a new database named client_scheduler.db if one does not already exist. Creates
     * necessary tables and adds default values if database is being created for the first time.
     */
    static void createDB()
    {
        try (Connection conn = DriverManager.getConnection(dbUrl))
        {
            if (conn != null)
            {
                DatabaseMetaData dbm = conn.getMetaData();
                ResultSet tables = dbm.getTables(null, null, "Users", null);
                if (tables.next())
                {
                    System.out.println("> [" + Server.getDate() + "] Connected to client_scheduler.db");
                }
                else
                {
                    System.out.println("> [" + Server.getDate() + "] Created client_scheduler.db");

                    createUsersTable();
                    System.out.println("> [" + Server.getDate() + "] Users table created");

                    createSchedulesTable();
                    System.out.println("> [" + Server.getDate() + "] Schedules table created");

                    createUserScheduleTable();
                    System.out.println("> [" + Server.getDate() + "] User schedules table created");

                    insertSchedules();
                    System.out.println("> [" + Server.getDate() + "] User Default schedules added");

                    insertUser("admin", "password", "Guy", "Buddy");
                    System.out.println("> [" + Server.getDate() + "] User Default user admin added");
                }
            }

        }
        catch (SQLException e)
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
    private static void createUserScheduleTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS user_schedules (\n"
                + "user_schedule_id INTEGER NOT NULL PRIMARY KEY,\n"
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
     * Auto-insert all needed schedules into the schedules table
     */
    private static void insertSchedules()
    {
        String sql = "INSERT INTO schedules(schedule_day, schedule_time) VALUES(?,?)";
        String[] daysArray = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[] timesArray = {"9am - 10am", "10am - 11am", "11am - 12pm", "1pm - 2pm", "2pm - 3pm"};

        for (String day:daysArray)
        {
            for (String time:timesArray)
            {
                try (Connection conn = connect();
                     PreparedStatement pstmt = conn.prepareStatement(sql))
                {
                    pstmt.setString(1, day);
                    pstmt.setString(2, time);
                    pstmt.executeUpdate();
                }
                catch (SQLException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Insert a new row into the users table
     *
     * @param user username of the user to be inserted.
     * @param pass password of the user to be inserted.
     * @param ln last name of the user to be inserted.
     * @param fn first name of the user to be inserted.
     */
    static void insertUser(String user, String pass, String ln, String fn)
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
     * Insert a new row into the user schedules table
     *
     * @param user_id user id of the user schedule to be inserted.
     * @param schedule_id schedule id of the user schedule to be inserted.
     */
    static void insertUserSchedule(int user_id, int schedule_id)
    {
        String sql = "INSERT INTO user_schedules(user_id,schedule_id) VALUES(?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, schedule_id);
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
     * Returns a day from the schedules table with the corresponding id.
     *
     * @param schedId the id of the schedule to select
     */
    static String selectDayByScheduleId(int schedId)
    {
        String sql = "SELECT schedule_day FROM schedules WHERE schedule_id = ?";
        String schedule = "";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1,schedId);
            ResultSet rs    = pstmt.executeQuery();

            while (rs.next())
            {
                schedule = rs.getString("schedule_day");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return schedule;
    }

    /**
     * Returns a time from the schedules table with the corresponding id.
     *
     * @param schedId the id of the schedule to select
     */
    static String selectTimeByScheduleId(int schedId)
    {
        String sql = "SELECT schedule_time FROM schedules WHERE schedule_id = ?";
        String schedule = "";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1,schedId);
            ResultSet rs    = pstmt.executeQuery();

            while (rs.next())
            {
                schedule = rs.getString("schedule_time");
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
     * Returns an user id for the corresponding username and password from the users table.
     *
     * @param username the username of the user to select.
     * @param password the password of the user to select.
     */
    static int selectUserIdByUsernameAndPassword(String username, String password)
    {
        String sql = "SELECT user_id FROM users WHERE user_username = ? AND user_password = ?";
        int userId = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            ResultSet rs    = pstmt.executeQuery();

            userId = rs.getInt("user_id");
        }
        catch (SQLException e)
        {
            System.out.println("> DB: No such user found: " + e.getMessage());
        }

        return userId;
    }

    /**
     * Returns an first name for the corresponding user id from the users table.
     *
     * @param userId the user id of the user to select.
     */
    static String selectFirstNameByUserId(int userId)
    {
        String sql = "SELECT user_first_name FROM users WHERE user_id = ?";
        String fn = "";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1,userId);
            ResultSet rs    = pstmt.executeQuery();

            fn = rs.getString("user_first_name");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return fn;
    }

    /**
     * Returns an last name for the corresponding user id from the users table.
     *
     * @param userId the user id of the user to select.
     */
    static String selectLastNameByUserId(int userId)
    {
        String sql = "SELECT user_last_name FROM users WHERE user_id = ?";
        String ln = "";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1,userId);
            ResultSet rs    = pstmt.executeQuery();

            ln = rs.getString("user_last_name");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return ln;
    }

    /**
     * Returns a schedule id for the corresponding day and time from the users table.
     *
     * @param day the day of the schedule to select.
     * @param time the time of the schedule to select.
     */
    static int selectScheduleIdByDayAndTime(String day, String time)
    {
        String sql = "SELECT schedule_id FROM schedules WHERE schedule_day = ? AND schedule_time = ?";
        int scheduleId = 1;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,day);
            pstmt.setString(2,time);
            ResultSet rs    = pstmt.executeQuery();

            while (rs.next())
            {
                scheduleId = rs.getInt("schedule_id");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return scheduleId;
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
     * Returns an arraylist of all distinct days from the schedules table.
     */
    static ArrayList<String> selectAllDays()
    {
        String sql = "SELECT DISTINCT schedule_day FROM schedules";
        ArrayList<String> days = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                days.add(rs.getString("schedule_day"));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return days;
    }

    /**
     * Returns an arraylist of all distinct times from the schedules table.
     */
    static ArrayList<String> selectAllTimes()
    {
        String sql = "SELECT DISTINCT schedule_time FROM schedules";
        ArrayList<String> times = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                times.add(rs.getString("schedule_time"));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return times;
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
    static void deleteUser(int userId)
    {
        String sql = "DELETE FROM users WHERE user_id = ?";

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

    /**
     * Delete an existing row from the user schedules table
     *
     * @param user_id user id of the user schedule to be deleted.
     * @param schedule_id schedule id of the user schedule to be deleted.
     */
    static void deleteUserSchedule(int user_id, int schedule_id)
    {
        String sql = "DELETE FROM user_schedules WHERE user_id = ? AND schedule_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, schedule_id);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
