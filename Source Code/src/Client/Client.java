package Client;

import DataModels.DataCommand;

import javax.swing.*;

/**
 * Handles all client-related activities.
 */
public class Client
{
    private JFrame parentFrame;
    private String portNumber;
    private String hostName;

    volatile static DataCommand command = new DataCommand(DataCommand.CommandType.DEFAULT, null);

    public Client(String pn, String hn, JFrame pf)
    {
        this.parentFrame = pf;
        this.portNumber = pn;
        this.hostName = hn;

        createAuthenticationFrame();
    }

    private void createAuthenticationFrame()
    {
        new ClientAuthenticationFrame(portNumber, hostName, parentFrame);
    }

}
