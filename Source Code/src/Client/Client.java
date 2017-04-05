package Client;

import javax.swing.*;

/**
 * Created by Tim on 4/2/2017.
 */
public class Client
{
    private ClientManager clientManager;
    private JFrame parentFrame;

    private String portNumber;
    private String hostName;

    public Client(String pn, String hn, JFrame pf)
    {

        this.parentFrame = pf;
        this.portNumber = pn;
        this.hostName = hn;
        createClient(pn,hn);
        createFrame();

    }
    private void createFrame()
    {
        JFrame clientFrame = new ClientFrame(clientManager, parentFrame);
        clientFrame.setTitle("Client@" + hostName + ":" + portNumber);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientFrame.setVisible(true);
        clientFrame.setResizable(false);
    }
    private void createClient(String pn, String hn)
    {
        Thread clientThread = new Thread(clientManager = new ClientManager(pn,hn));
        clientThread.start();
    }
}
