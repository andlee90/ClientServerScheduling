package Server;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Handles all server-related activities.
 */
public class Server
{
    public static ServerManager serverManager;
    private JFrame parentFrame;

    private String portNumber;
    private String hostName;
    private String maxClients;

    public Server(String pn, String mc, JFrame pf) throws IOException
    {
        this.parentFrame = pf;
        this.portNumber = pn;
        this.maxClients = mc;
        this.hostName = getHost();
        createServer();
        createFrame();
    }

    /**
     * Builds frame for server interface.
     */
    private void createFrame()
    {
        JFrame serverFrame = new ServerFrame(parentFrame);
        serverFrame.setTitle("Server@" + hostName + ":" + portNumber);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.setVisible(true);
        serverFrame.setResizable(false);
    }

    /**
     * Starts a new ServerManager on a background thread.
     */
    private void createServer() throws IOException
    {
        serverManager = new ServerManager(portNumber, maxClients);
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
