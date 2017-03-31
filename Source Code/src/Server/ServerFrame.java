package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by andrewsmith on 2/4/17.
 */

class ServerFrame extends JFrame
{
    private static final int FRAME_WIDTH = 750;
    private static final int FRAME_HEIGHT = 375;

    private JButton modifySchedulesButton;
    private JButton closeServerButton;

    private JScrollPane scrollPane;
    private JLabel textAreaLabel;

    private ServerManager serverManager;

    ServerFrame(ServerManager sm)
    {
        this.serverManager = sm;

        createButtons();
        createTextArea();
        createPanel();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private void createPanel()
    {
        JPanel container = new JPanel();
        JPanel textAreaPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        textAreaPanel.setLayout(new BoxLayout(textAreaPanel, BoxLayout.PAGE_AXIS));
        textAreaPanel.add(textAreaLabel);
        textAreaPanel.add(scrollPane);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(modifySchedulesButton);
        buttonPanel.add(closeServerButton);

        container.setLayout(new FlowLayout());
        container.add(textAreaPanel);
        container.add(buttonPanel);

        this.add(container);
    }

    private void createTextArea()
    {
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        textAreaLabel = new JLabel("Info:");
    }

    private void createButtons()
    {
        modifySchedulesButton = new JButton("Modify Schedules");
        modifySchedulesButton.setEnabled(true);

        closeServerButton = new JButton("Close Server");
        closeServerButton.setEnabled(true);
    }
}