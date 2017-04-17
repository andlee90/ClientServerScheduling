package Client;

import DataModels.DataCommand;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Handles all client-related activities.
 */
public class Client
{
    private JFrame parentFrame;
    private String portNumber;
    private String hostName;

    /**
     * The command object is set by user input (i.e. clicking a button) and determines the details of the command issued
     * to the waiting server. It is "volatile" to allow it's state to be affected and the changes made to be read by
     * multiple threads. No actions occur when it's command type is DEFAULT.
     */
    volatile static DataCommand command = new DataCommand(DataCommand.CommandType.DEFAULT, null, null);
    volatile static ArrayList<String> currentUserSchedules = new ArrayList<>();
    volatile static boolean isValidHost = true;
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
