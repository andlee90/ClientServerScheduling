package Client;
import DataModels.DataUser;
import Main.MainErrorMessageFrame;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

/**
 * Handles creation and management of all client authentication user interface objects.
 */
class ClientAuthenticationFrame extends JFrame
{
    static JButton cancelButton;
    static JFrame frame;

    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JButton loginButton;
    private JPanel container;
    private JTextField userField;
    private JPasswordField passwordField;
    private JFrame parentFrame;

    private String hostName;
    private String portNumber;
    private DataUser user;
    private ClientManager clientManager;

    ClientAuthenticationFrame(String pn, String hn, JFrame pf)
    {
        super("User Authentication");
        setSize(300,200);
        setLocation(500,280);

        parentFrame = pf;
        this.hostName = hn;
        this.portNumber = pn;
        frame = this;

        createButtons();
        createLabels();
        createTextField();
        createPanel();

        getContentPane().add(container);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        actionlogin();
        actionCancel();
        setLocationRelativeTo(null);
    }

    private void createLabels()
    {
        userNameLabel = new JLabel("Username: ");
        passwordLabel = new JLabel("Password: ");
        userNameLabel.setBounds(70,10,150,20);
        passwordLabel.setBounds(70,55,150,20);
    }

    private void createButtons()
    {
        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        loginButton.setBounds(50,110,80,20);
        cancelButton.setBounds(150,110,80,20);
    }

    private void createPanel()
    {
        container = new JPanel();
        container.setLayout (null);
        container.add(loginButton);
        container.add(cancelButton);
        container.add(userNameLabel);
        container.add(userField);
        container.add(passwordLabel);
        container.add(passwordField);
    }

    private void createTextField()
    {
        userField = new JTextField(15);
        passwordField = new JPasswordField(15);
        userField.setBounds(70,30,150,20);
        passwordField.setBounds(70,75,150,20);
    }

    private void actionCancel()
    {
        cancelButton.addActionListener(ae ->
        {
            setVisible(false);
            parentFrame.setVisible(true);
        });
    }

    private void actionlogin()
    {
        loginButton.addActionListener(ae ->
        {
            String puname = userField.getText();
            String ppaswd = passwordField.getText();

            user = new DataUser(puname, ppaswd, null, null, false, null);
            try
            {
                this.clientManager = new ClientManager(portNumber, hostName, user);

                int time = 0;
                while(!user.getViewed() && Client.isValidHost)
                {
                    try
                    {
                        TimeUnit.MILLISECONDS.sleep(1);
                        time++;
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    user = clientManager.getUser();
                    if (time > 5000)
                    {
                        new MainErrorMessageFrame("Connection timed out. Server may be full.");
                        break;
                    }
                }
            }
            catch (Exception ignored){}

            if (user.getValidity())
            {
                setVisible(false);
                createFrame();
            }

            else if (Client.isValidHost)
            {
                JOptionPane.showMessageDialog(null,"Incorrect Username or Password");
                userField.setText("");
                passwordField.setText("");
                userField.requestFocus();
            }
        });
    }

    /**
     * Builds frame for client interface.
     */
    private void createFrame()
    {
        JFrame clientFrame = new ClientFrame(parentFrame, user);
        clientFrame.setTitle(user.getUserName() + "@" + hostName + ":" + portNumber);
        clientFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        clientFrame.setVisible(true);
        clientFrame.setResizable(false);
    }
}
