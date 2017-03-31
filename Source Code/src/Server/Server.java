package Server;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by andrewsmith on 3/29/17.
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

    private void createFrame()
    {
        JFrame frame = new ServerFrame(serverManager);
        frame.setTitle("Server@" + hostName + ":" + portNumber);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void createServer(String pn)
    {
        (new Thread(new ServerManager(pn))).start();
    }

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
