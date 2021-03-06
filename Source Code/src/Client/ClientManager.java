package Client;

import DataModels.DataCommand;
import DataModels.DataMessage;
import DataModels.DataUser;
import Main.MainErrorMessageFrame;

import java.io.*;
import java.net.*;

/**
 * Manages all client-related network activities on a background thread to avoid interfering with the user interface.
 * First, a user model is passed to the server with the necessary info for authentication. If authentication succeeds,
 * a connection message is sent. Afterwards, multiple command models may be sent to perform various tasks.
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

            clientOutputStream.writeObject(user); // Send user to server
            user = (DataUser) clientInputStream.readObject();

            if(user.getValidity())
            {
                String userInput = user.getUserName() + "@" + Inet4Address.getLocalHost().getHostAddress();
                DataMessage message = new DataMessage(userInput);
                clientOutputStream.writeObject(message);

                while(!interrupted())
                {
                    DataCommand.CommandType ct = Client.command.getCommandType();

                    if(ct == DataCommand.CommandType.CLOSE_SERVER)
                    {
                        clientOutputStream.reset();
                        clientOutputStream.writeObject(Client.command);
                        Client.command.setCommandType(DataCommand.CommandType.DEFAULT);
                        Client.command.setModifiedSchedule(null);
                        break;
                    }
                    else if(ct == DataCommand.CommandType.INSERT_SCHEDULE)
                    {
                        clientOutputStream.reset();
                        clientOutputStream.writeObject(Client.command);

                        Client.command = (DataCommand) clientInputStream.readObject();
                        Client.currentUserSchedules = Client.command.getUpdatedUserSchedules();

                        Client.command.setCommandType(DataCommand.CommandType.DEFAULT);
                        Client.command.setUpdatedUserSchedules(null);
                        Client.command.setModifiedSchedule(null);
                    }
                    else if(ct == DataCommand.CommandType.DELETE_SCHEDULE)
                    {
                        clientOutputStream.reset();
                        clientOutputStream.writeObject(Client.command);

                        Client.command = (DataCommand) clientInputStream.readObject();
                        Client.currentUserSchedules = Client.command.getUpdatedUserSchedules();

                        Client.command.setCommandType(DataCommand.CommandType.DEFAULT);
                        Client.command.setUpdatedUserSchedules(null);
                        Client.command.setModifiedSchedule(null);
                    }
                }
            }

            clientInputStream.close();
            clientOutputStream.close();
            socket.close();
        }
        catch (UnknownHostException e)
        {
            Client.isValidHost=false;
            ClientAuthenticationFrame.cancelButton.doClick();
            ClientAuthenticationFrame.frame.setEnabled((false));
            new MainErrorMessageFrame("Unable to find host " + hostName);
            ClientAuthenticationFrame.frame.setEnabled((true));
        }
        catch (IOException e)
        {
            Client.isValidHost=false;
            ClientAuthenticationFrame.cancelButton.doClick();
            ClientAuthenticationFrame.frame.setEnabled((false));
            new MainErrorMessageFrame("Couldn't get I/O for the connection to "+ hostName);
            ClientAuthenticationFrame.frame.setEnabled((true));
        }
        catch (ClassNotFoundException e)
        {
            ClientAuthenticationFrame.cancelButton.doClick();
            ClientAuthenticationFrame.frame.setEnabled((false));
            new MainErrorMessageFrame("Class not found.");
            ClientAuthenticationFrame.frame.setEnabled((true));
        }
    }

    DataUser getUser()
    {
        return this.user;
    }
}
