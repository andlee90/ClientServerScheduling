package Server;

import DataModels.DataUser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Manages all server-related network and database activities on a background
 * thread to avoid interfering with the user interface.
 */
class ServerManager implements Runnable
{
    private int portNumber;
    private DataUser user;

    ServerManager(String pn)
    {
        this.portNumber = Integer.parseInt(pn);
    }

    @Override
    public void run()
    {
        ServerDB.createDB();
        System.out.println("Waiting for clients...");
        try
        {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket pipe = serverSocket.accept();

            ObjectInputStream serverInputStream = new ObjectInputStream(pipe.getInputStream());
            ObjectOutputStream serverOutputStream = new ObjectOutputStream(pipe.getOutputStream());

            user = (DataUser)serverInputStream.readObject();
            int userId = ServerDB.selectUserIdByUsernameAndPassword(user.getUserName(), user.getPassword());

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
            serverInputStream.close();
            serverOutputStream.close();
            serverSocket.close();

            //TODO: this is no good, must find a better way
            serverSocket = new ServerSocket(portNumber);
            pipe = serverSocket.accept();

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(pipe.getInputStream()));
            System.out.println(stdIn.readLine());
        }
        catch (IOException e)
        {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
