package Client;

import DataModels.DataUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles creation and management of all client user interface objects.
 */
class ClientFrame extends JFrame
{
    private static final int FRAME_WIDTH = 725;
    private static final int FRAME_HEIGHT = 375;

    private JButton editSchedulesButton;
    private JButton closeClientButton;
    private JScrollPane scrollPane;
    private JFrame parentFrame;
    private ClientManager clientManager;
    private DataUser user;

    ClientFrame(ClientManager sm, JFrame p, DataUser u)
    {
        this.clientManager = sm;
        this.parentFrame = p;
        this.user = u;

        createButtons();
        createTextArea();
        createPanels();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private void createPanels()
    {
        JPanel container = new JPanel();
        JPanel textAreaPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        textAreaPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        textAreaPanel.add(scrollPane);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(editSchedulesButton);
        buttonPanel.add(closeClientButton);

        container.setLayout(new FlowLayout());
        container.add(buttonPanel);
        container.add(textAreaPanel);

        this.add(container);
    }

    private void createTextArea()
    {
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        textArea.append("Username: " + user.getUserName() +
                "\n" + "Password: " + user.getPassword() +
                "\n" + "First Name: " + user.getFirstName() +
                "\n" + "Last Name: " + user.getLastName() +
                "\n" + "Schedule: ");
        for (String schedule:user.getSchedule())
        {
            textArea.append(schedule + "\n");
        }

        scrollPane = new JScrollPane(textArea);
    }

    private void createButtons()
    {
        editSchedulesButton = new JButton("Send");
        editSchedulesButton.setEnabled(true);
        ClientFrame.EditSchedulesListener editSchedulesListener = new ClientFrame.EditSchedulesListener();
        editSchedulesButton.addActionListener(editSchedulesListener);

        closeClientButton = new JButton("Log Out");
        closeClientButton.setEnabled(true);
        ClientFrame.CloseClientListener closeClientListener = new ClientFrame.CloseClientListener();
        closeClientButton.addActionListener(closeClientListener);
    }

    /**
     * Creates a new schedule editor object.
     */
    class EditSchedulesListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            //System.out.println(Thread.activeCount());
        }
    }

    /**
     * Hides the current frame and shows the main user interface.
     */
    //TODO: Actually close down client
    class CloseClientListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            setVisible(false); // Hide Server GUI
            parentFrame.setVisible(true); // Show Main GUI
        }
    }
}
