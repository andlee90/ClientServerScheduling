package Server;

import DataModels.DataCommand;
import DataModels.DataMessage;
import DataModels.DataUser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Manages a single client connection by first comparing the incoming
 * user object attributes against those in the database. The socket is
 * closed if authentication fails. Otherwise, communication with the
 * client continues.
 */
public class ServerThreadManager extends Thread
{
    private int threadId;
    private int MAX_CLIENTS;
    private Socket socket;
    private String userAddress;

    ServerThreadManager(Socket p, int id, int m)
    {
        this.socket = p;
        this.threadId = id;
        this.MAX_CLIENTS = m;
        start(); // Start on a new thread
    }

    @Override
    public void run()
    {
        try
        {
            ObjectInputStream serverInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream serverOutputStream = new ObjectOutputStream(socket.getOutputStream());

            DataUser user = (DataUser)serverInputStream.readObject(); // Get user from client
            user.setViewed(true); // Verify that user has been viewed

            int userId = 0;
            userId = ServerDB.selectUserIdByUsernameAndPassword(user.getUserName(), user.getPassword());

            if(userId != 0) // User found
            {
                user.setFirstName(ServerDB.selectFirstNameByUserId(userId)); // Get first name from db
                user.setLastName(ServerDB.selectLastNameByUserId(userId)); // Get last name from db
                if (ServerDB.selectIsAdminByUserId(userId) == 1)
                {
                    user.setIsAdmin(true); // Give user admin privilege
                }

                ArrayList<Integer> scheduleIds = ServerDB.selectAllScheduleIdsByUserId(userId); // Get ids for user's schedules
                ArrayList<String> schedules = new ArrayList<>();
                for (int id:scheduleIds)
                {
                    schedules.add(ServerDB.selectScheduleByScheduleId(id)); // Get user's schedules
                }

                user.setSchedule(schedules);
                user.setValidity(true); // User is valid
            }
            serverOutputStream.writeObject(user); // Send user to client

            if (user.getValidity()) // If user was valid
            {
                DataMessage message = (DataMessage)serverInputStream.readObject(); // Get message from client
                userAddress = message.getMessage();

                System.out.println("> [" + Server.getDate() + "] " + userAddress + " connected");
                System.out.println("> [" + Server.getDate() + "] Total connected clients: " + (threadId + 1) + "/"
                        + this.MAX_CLIENTS);

                //TODO: figure out why +1 is needed to insert while +2 is needed for delete and fix
                while(!interrupted()) // Wait for commands from client
                {
                    String schedule;
                    int scheduleId;

                    DataCommand command = (DataCommand) serverInputStream.readObject(); // Get command from client

                    if (command.getCommandType() == DataCommand.CommandType.CLOSE_SERVER)
                    {
                        System.out.println("> [" + Server.getDate() + "] " + userAddress + " disconnected");
                        System.out.println("> [" + Server.getDate() + "] Total connected clients: " + (threadId) + "/"
                                + this.MAX_CLIENTS);
                        break; // Shutdown thread and close connections
                    }

                    else if (command.getCommandType() == DataCommand.CommandType.INSERT_SCHEDULE)
                    {
                        schedule = command.getModifiedSchedule(); // Get schedule to modify
                        scheduleId = ServerDB.selectScheduleIdByDayAndTime(
                                (schedule.substring(0,schedule.indexOf(" "))),
                                (schedule.substring(schedule.indexOf(" ")+1,schedule.length())));

                        ServerDB.insertUserSchedule(userId, scheduleId); // Add schedule to DB
                        ArrayList<Integer> scheduleIds = ServerDB.selectAllScheduleIdsByUserId(userId);
                        ArrayList<String> schedules = new ArrayList<>();
                        for (int id:scheduleIds)
                        {
                            schedules.add(ServerDB.selectScheduleByScheduleId(id));
                        }
                        command.setUpdatedUserSchedules(schedules); // Get updated user schedules from DB
                        command.setIsModified(true); // Acknowledge that schedule has been modified
                        serverOutputStream.writeObject(command); // Send modifications back to client
                        System.out.println("> [" + Server.getDate() + "] " + userAddress + " added " + schedule
                                + " to their schedule");
                    }

                    else if (command.getCommandType() == DataCommand.CommandType.DELETE_SCHEDULE)
                    {
                        schedule = command.getModifiedSchedule(); // Get schedule to modify
                        scheduleId = ServerDB.selectScheduleIdByDayAndTime(
                                (schedule.substring(0,schedule.indexOf(" "))),
                                (schedule.substring(schedule.indexOf(" ")+2,schedule.length())));

                        ServerDB.deleteUserSchedule(userId, scheduleId); // Remove schedule from DB
                        ArrayList<Integer> scheduleIds = ServerDB.selectAllScheduleIdsByUserId(userId);
                        ArrayList<String> schedules = new ArrayList<>();
                        for (int id:scheduleIds)
                        {
                            schedules.add(ServerDB.selectScheduleByScheduleId(id));
                        }
                        command.setUpdatedUserSchedules(schedules); // Get updated user schedules from DB
                        command.setIsModified(true); // Acknowledge that schedule has been modified
                        serverOutputStream.writeObject(command); // Send modifications back to client
                        System.out.println("> [" + Server.getDate() + "] " + userAddress + " removed " + schedule
                                + " from their schedule");
                    }
                }
            }
            else
            {
                System.out.println("> [" + Server.getDate() + "] Invalid login attempt from "
                        + socket.getRemoteSocketAddress() + "\n> with username: " + user.getUserName()
                        + " and password: " + user.getPassword());
            }

            serverInputStream.close(); // Close streams
            serverOutputStream.close();
            ServerManager.clientConnections[threadId] = null; // Clear slot for another client
            close(); // Close socket
        }
        catch (IOException e)
        {
            ServerManager.clientConnections[threadId] = null; // Clear slot for another client
            System.out.println("> [" + Server.getDate() + "] Exception caught when trying to listen " +
                    "on port or listening for a connection");
            System.out.println("> [" + Server.getDate() + "] " + userAddress + " disconnected");
            System.out.println("> [" + Server.getDate() + "] Total connected clients: " + (threadId) + "/"
                    + this.MAX_CLIENTS);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Closes current socket
     */
    void close() throws IOException
    {
        this.socket.close();
    }
}
