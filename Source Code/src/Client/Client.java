package Client;

import javax.swing.*;

/**
 * Handles all client-related activities.
 */
public class Client
{
    private JFrame parentFrame;
    private String portNumber;
    private String hostName;

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
