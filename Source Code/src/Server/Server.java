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
    private JFrame parentFrame;

    private String portNumber;
    private String hostName;

    public Server(String pn, JFrame pf)
    {
        this.parentFrame = pf;
        this.portNumber = pn;
        this.hostName = getHost();
        createServer(pn);
        createFrame();
    }

    /**
     * Builds frame for server interface.
     */
    private void createFrame()
    {
        JFrame serverFrame = new ServerFrame(serverManager, parentFrame);
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
        Thread serverThread = new Thread(serverManager = new ServerManager(pn));
        serverThread.start();
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
