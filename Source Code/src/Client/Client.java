package Client;

import javax.swing.*;

/**
 * Created by Tim on 4/2/2017.
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
    }
}
