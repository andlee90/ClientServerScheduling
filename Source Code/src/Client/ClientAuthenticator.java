package Client;

import DataModels.DataUser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Handles all client authentication-related activities.
 */
public class ClientAuthenticator implements Runnable
{
    private String hostName;
    private int portNumber;
    private DataUser user;

    ClientAuthenticator(String pn, String hn, DataUser u)
    {
        this.hostName = hn;
        this.portNumber = Integer.parseInt(pn);
        this.user = u;
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

            oos.close();
            ois.close();
            socket.close();
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
