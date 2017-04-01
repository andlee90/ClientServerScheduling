package Server;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Handles all server-related activities.
 */
public class Server
{
    private ServerManager serverManager;

    private String portNumber;
    private String hostName;

    public Server(String pn)
    {
        this.portNumber = pn;
        this.hostName = getHost();
        createFrame();
        createServer(pn);
    }

    /**
     * Builds frame for server interface.
     */
    private void createFrame()
    {
        JFrame serverFrame = new ServerFrame(serverManager);
        serverFrame.setTitle("Server@" + hostName + ":" + portNumber);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.setVisible(true);
        serverFrame.setResizable(false);
    }

    /**
     * Starts a new ServerManager on a background thread.
     * @param pn the port number to host the server on.
     */
    private void createServer(String pn)
    {
        serverManager = new ServerManager(pn);
        (new Thread(serverManager)).start();
    }

    /**
     * Gets the network name of the host machine.
     */
    private String getHost()
    {
        String hostname = "Unknown";

        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }

        return hostname;
    }
}
