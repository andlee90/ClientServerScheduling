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
public class ServerThread extends Thread
{
    private int threadId;
    private Socket socket;
    private String userAddress;

    ServerThread(Socket p, int id)
    {
        this.socket = p;
        this.threadId = id;
        start();
    }

    @Override
    public void run()
    {
        try
        {
            ObjectInputStream serverInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream serverOutputStream = new ObjectOutputStream(socket.getOutputStream());

            DataUser user = (DataUser)serverInputStream.readObject();
            user.setViewed(true);
            int userId = 0;

            try
            {
                userId = ServerDB.selectUserIdByUsernameAndPassword(user.getUserName(), user.getPassword());
            }
            catch(Exception e)
            {
                System.out.println("> Invalid login attempt from " + socket.getRemoteSocketAddress()
                        + "\nwith username: " + user.getUserName() + " and password: " + user.getPassword());
            }

            if(userId != 0)
            {
                user.setFirstName(ServerDB.selectFirstNameByUserId(userId));
                user.setLastName(ServerDB.selectLastNameByUserId(userId));

                ArrayList<Integer> scheduleIds = ServerDB.selectAllScheduleIdsByUserId(userId);
                ArrayList<String> schedules = new ArrayList<>();
                for (int id:scheduleIds)
                {
                    schedules.add(ServerDB.selectScheduleByScheduleId(id));
                }

                user.setSchedule(schedules);
                user.setValidity(true);
            }
            serverOutputStream.writeObject(user);

            if (user.getValidity())
            {
                DataMessage message = (DataMessage)serverInputStream.readObject();
                userAddress = message.getMessage();
                System.out.println("> " + userAddress + " connected");

                //TODO: figure out why +1 is needed to insert while +2 is needed for delete and fix
                while(!interrupted())
                {
                    String schedule;
                    int scheduleId;

                    DataCommand command = (DataCommand) serverInputStream.readObject();

                    if (command.getCommandType() == DataCommand.CommandType.CLOSE_SERVER)
                    {
                        System.out.println("> " + userAddress + " disconnected");
                        break;
                    }

                    else if (command.getCommandType() == DataCommand.CommandType.INSERT_SCHEDULE)
                    {
                        schedule = command.getModifiedSchedule();
                        scheduleId = ServerDB.selectScheduleIdByDayAndTime(
                                (schedule.substring(0,schedule.indexOf(" "))),
                                (schedule.substring(schedule.indexOf(" ")+1,schedule.length())));

                        ServerDB.insertUserSchedule(userId, scheduleId);
                        ArrayList<Integer> scheduleIds = ServerDB.selectAllScheduleIdsByUserId(userId);
                        ArrayList<String> schedules = new ArrayList<>();
                        for (int id:scheduleIds)
                        {
                            schedules.add(ServerDB.selectScheduleByScheduleId(id));
                        }
                        command.setUpdatedUserSchedules(schedules);
                        command.setIsModified(true);
                        serverOutputStream.writeObject(command);
                        System.out.println("> " + userAddress + " added " + schedule + " to their schedule");
                    }

                    else if (command.getCommandType() == DataCommand.CommandType.DELETE_SCHEDULE)
                    {
                        schedule = command.getModifiedSchedule();
                        scheduleId = ServerDB.selectScheduleIdByDayAndTime(
                                (schedule.substring(0,schedule.indexOf(" "))),
                                (schedule.substring(schedule.indexOf(" ")+2,schedule.length())));

                        ServerDB.deleteUserSchedule(userId, scheduleId);
                        ArrayList<Integer> scheduleIds = ServerDB.selectAllScheduleIdsByUserId(userId);
                        ArrayList<String> schedules = new ArrayList<>();
                        for (int id:scheduleIds)
                        {
                            schedules.add(ServerDB.selectScheduleByScheduleId(id));
                        }
                        command.setUpdatedUserSchedules(schedules);
                        command.setIsModified(true);
                        serverOutputStream.writeObject(command);
                        System.out.println("> " + userAddress + " removed " + schedule + " from their schedule");
                    }
                }
            }

            serverInputStream.close();
            serverOutputStream.close();
            ServerManager.clientConnections[threadId] = null;
            close();
        }
        catch (IOException e)
        {
            System.out.println("> " + "Exception caught when trying to listen on port or listening for a connection");
            System.out.println(e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    void close() throws IOException
    {
        this.socket.close();
    }
}
