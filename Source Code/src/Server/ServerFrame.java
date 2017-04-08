package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

/**
 * Handles creation and management of all server user interface objects.
 */
class ServerFrame extends JFrame
{
    private static final int FRAME_WIDTH = 725;
    private static final int FRAME_HEIGHT = 375;

    private JButton editSchedulesButton;
    private JButton closeServerButton;
    private JScrollPane scrollPane;
    private JFrame parentFrame;

    private ServerManager serverManager;

    ServerFrame(ServerManager sm, JFrame p)
    {
        this.serverManager = sm;
        this.parentFrame = p;

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
        PrintStream printStream = new PrintStream(new SeverOutputStream(textArea));
        System.setOut(printStream);
    }

    private void createButtons()
    {
        editSchedulesButton = new JButton("Modify Schedules");
        editSchedulesButton.setEnabled(true);
        EditSchedulesListener editSchedulesListener = new EditSchedulesListener();
        editSchedulesButton.addActionListener(editSchedulesListener);

        closeServerButton = new JButton("Close Server");
        closeServerButton.setEnabled(true);
        CloseServerListener closeServerListener = new CloseServerListener();
        closeServerButton.addActionListener(closeServerListener);
    }

    /**
     * Creates a new schedule editor object.
     */
    class EditSchedulesListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            new ServerScheduleEditor();
        }
    }

    /**
     * Hides the current frame and shows the main user interface.
     */
    //TODO: Actually close down server
    class CloseServerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            setVisible(false); // Hide Server GUI
            parentFrame.setVisible(true); // Show Main GUI
        }
    }
}