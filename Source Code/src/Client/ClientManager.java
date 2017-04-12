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

            ObjectOutputStream clientOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream clientInputStream = new ObjectInputStream(socket.getInputStream());

            clientOutputStream.writeObject(user);
            user = (DataUser) clientInputStream.readObject();

            String userInput = user.getUserName() + "@" + Inet4Address.getLocalHost().getHostAddress();
            DataMessage message = new DataMessage(userInput);
            clientOutputStream.writeObject(message);

            while(true)
            {
                if(Client.command.getCommand() == DataCommand.Command.CLOSE_SERVER)
                {
                    clientOutputStream.writeObject(Client.command);
                    Client.command.setCommand(DataCommand.Command.DEFAULT);
                }
            }
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
        return this.user;
    }

    /*public DataCommand.Command getCommand()
    {
        return command;
    }

    public void setCommand(DataCommand.Command c)
    {
        System.out.println("inside set command");
        command = c;
    }*/
}
