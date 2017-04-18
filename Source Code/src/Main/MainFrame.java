package Main;

import Server.Server;
import Client.Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Handles creation and management of all main user interface objects.
 */
class MainFrame extends JFrame
{
    private JFrame frame = this;
    private JButton createServerButton;
    private JButton joinServerButton;
    private JComboBox<Integer> cPortField;
    private JComboBox<Integer> sPortField;
    private JTextField maxCliField;
    private JTextField hostField;

    private String portNumber;

    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 200;

    MainFrame()
    {
        createButtons();
        createTextFields();
        createDropdowns();
        createPanel();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
    }

    private void createDropdowns()
    {
        Integer[] portArr = {9006,9007,9008,9010}; // Set the application port numbers here
        cPortField = new JComboBox<>(portArr);
        sPortField = new JComboBox<>(portArr);
    }

    private void createButtons()
    {
        createServerButton = new JButton("Create New Server");
        ActionListener clientListener = new CreateServerListener();
        createServerButton.addActionListener(clientListener);
        createServerButton.setEnabled(true);

        joinServerButton = new JButton("Join Server");
        ActionListener serverListener = new JoinServerListener();
        joinServerButton.addActionListener(serverListener);
        joinServerButton.setEnabled(true);
    }

    private void createTextFields()
    {
        maxCliField = new JTextField(4);
        hostField = new JTextField(10);
    }

    private void createPanel()
    {
        final int rows = 4;
        final int cols = 2;

        final JLabel emptyLabel = new JLabel("");
        emptyLabel.setBorder(BorderFactory.createMatteBorder(0, 1,0,0, Color.GRAY));

        JPanel container = new JPanel();
        JPanel serverPortFieldPanel = new JPanel();
        JPanel serverMaxCliFieldPanel = new JPanel();
        JPanel serverButtonPanel = new JPanel();
        JPanel clientPortFieldPanel = new JPanel();
        JPanel clientHostFieldPanel = new JPanel();
        JPanel clientButtonPanel = new JPanel();

        serverPortFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        serverPortFieldPanel.add(new JLabel("Port: "));
        serverPortFieldPanel.add(sPortField);

        serverMaxCliFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        serverMaxCliFieldPanel.add(new JLabel("Max Clients: "));
        serverMaxCliFieldPanel.add(maxCliField);

        serverButtonPanel.setLayout(new FlowLayout());
        serverButtonPanel.add(createServerButton);

        clientPortFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        clientPortFieldPanel.add(new JLabel("Port: "));
        clientPortFieldPanel.add(cPortField);
        clientPortFieldPanel.setBorder(BorderFactory.createMatteBorder(0, 1,0,0, Color.GRAY));

        clientHostFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        clientHostFieldPanel.add(new JLabel("Host: "));
        clientHostFieldPanel.add(hostField);
        clientHostFieldPanel.setBorder(BorderFactory.createMatteBorder(0, 1,0,0, Color.GRAY));

        clientButtonPanel.setLayout(new FlowLayout());
        clientButtonPanel.add(joinServerButton);
        clientButtonPanel.setBorder(BorderFactory.createMatteBorder(0, 1,0,0, Color.GRAY));

        container.setLayout(new GridLayout(rows, cols));
        container.add(serverPortFieldPanel);
        container.add(clientPortFieldPanel);
        container.add(serverMaxCliFieldPanel);
        container.add(clientHostFieldPanel);
        container.add(new JLabel(""));
        container.add(emptyLabel);
        container.add(serverButtonPanel);
        container.add(clientButtonPanel);

        this.add(container);
    }

    /**
     * Creates a new Sever object and hides the main user interface if all input is valid.
     */
    class CreateServerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            try
            {
                if (maxCliField.getText().equals(""))
                {
                    throw new IOException("Please enter a maximum number of clients.");
                }

                setVisible(false); // Hide Main GUI
                portNumber = sPortField.getSelectedItem().toString(); // Get string from text field
                String maxClients = maxCliField.getText(); // Get string from text field
                new Server(portNumber, maxClients, frame); // Create a new server
            }
            catch (IOException e)
            {
                new MainErrorMessageFrame(e.getLocalizedMessage());
                frame = new MainFrame();
                frame.setTitle("Client/Server Scheduling");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setResizable(false);
            }
        }
    }

    /**
     * Creates a new Client object and hides the main user interface if all input is valid.
     */
    class JoinServerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            try
            {
                if (hostField.getText().equals(""))
                {
                    throw new IOException("Please enter a valid Host name");
                }

                setVisible(false); // Hide Main GUI
                portNumber = cPortField.getSelectedItem().toString(); // Get string from text field
                String hostName = hostField.getText(); // Get string from text field
                new Client(portNumber, hostName, frame); // Create a new client
            }
            catch (IOException e)
            {
                frame.setEnabled((false));
                new MainErrorMessageFrame(e.getLocalizedMessage());
                frame.setEnabled((true));
                frame.toFront();
            }
        }
    }
}