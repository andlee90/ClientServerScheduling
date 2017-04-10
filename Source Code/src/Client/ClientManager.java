package Client;
import DataModels.DataCommand;
import DataModels.DataMessage;
import DataModels.DataUser;

import java.io.*;
import java.net.*;

/**
 * Manages all client-related network activities on a background
 * thread to avoid interfering with the user interface. First, a
 * user model is passed to the server with the necessary info
 * for authentication. If authentication succeeds, a connection
 * message is sent.
 */
class ClientManager extends Thread
{
    private int portNumber;
    private String hostName;
    private DataUser user;

    ClientManager(String pn, String hn, DataUser u)
    {
        this.portNumber = Integer.parseInt(pn);
        this.hostName = hn;
        this.user = u;
        start();
    }

    @Override
    public void run()
    {
        try
        {
            Socket socket = new Socket(hostName, portNumber);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(user);
            user = (DataUser) ois.readObject();

            String userInput = user.getUserName() + "@" + Inet4Address.getLocalHost().getHostAddress();
            DataMessage message = new DataMessage(userInput);
            oos.writeObject(message);
            //DataCommand command = new DataCommand(DataCommand.Command.CLOSE_SERVER);
            //oos.writeObject(command);
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);

        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    DataUser getUser()
    {
        return user;
    }
}
