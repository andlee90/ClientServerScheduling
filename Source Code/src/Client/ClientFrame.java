package Client;

import Client.ClientFrame;
import Client.ClientManager;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

/**
 * Created by Tim on 4/2/2017.
 */
public class ClientFrame extends JFrame
{
    private JTextField tempField;

    private static final int FRAME_WIDTH = 725;
    private static final int FRAME_HEIGHT = 375;

    private JButton editSchedulesButton;
    private JButton closeClientButton;
    private JScrollPane scrollPane;
    private JFrame parentFrame;
    private ClientManager clientManager;

    ClientFrame(ClientManager sm, JFrame p)
    {
        this.clientManager = sm;
        this.parentFrame = p;

        createButtons();
        createTextArea();
        createTextTemp();
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
        buttonPanel.add(tempField);
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
        scrollPane = new JScrollPane(textArea);
       // PrintStream printStream = new PrintStream(new SeverOutputStream(textArea));
       // System.setOut(printStream);
    }
    private void createTextTemp()
    {
        tempField = new JTextField(4);
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
     * Creates a new Scheduler object.
     */
    class EditSchedulesListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            //System.out.println(Thread.activeCount());
        }
    }

    /**
     * Creates a new Sever object and hides the main user interface.
     */
    class CloseClientListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            setVisible(false); // Hide Server GUI
            parentFrame.setVisible(true); // Show Main GUI
        }
    }
}
