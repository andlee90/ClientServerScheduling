package Client;

import DataModels.DataUser;

import javax.swing.*;

/**
 * Handles all server-related activities.
 */
class Client
{
    private ClientManager clientManager;
    private JFrame parentFrame;
    private DataUser user;
    private String portNumber;
    private String hostName;

    Client(String pn, String hn, JFrame pf, DataUser u)
    {
        this.parentFrame = pf;
        this.portNumber = pn;
        this.hostName = hn;
        this.user = u;

        createClient(pn,hn);
        createFrame();
    }

    /**
     * Builds frame for client interface.
     */
    private void createFrame()
    {
        JFrame clientFrame = new ClientFrame(clientManager, parentFrame, user);
        clientFrame.setTitle("Client@" + hostName + ":" + portNumber);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientFrame.setVisible(true);
        clientFrame.setResizable(false);
    }

    /**
     * Starts a new ClientManager on a background thread.
     * @param pn the port number of the server to connect to.
     * @param hn th host name of the server to connect to.
     */
    private void createClient(String pn, String hn)
    {
        Thread clientThread = new Thread(clientManager = new ClientManager(pn,hn));
        clientThread.start();
    }
}
