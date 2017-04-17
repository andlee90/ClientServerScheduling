package Server;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Handles creation and management of all server user interface objects.
 */
class ServerFrame extends JFrame
{
    private static final int FRAME_WIDTH = 725;
    private static final int FRAME_HEIGHT = 375;

    private JButton editSchedulesButton;
    private JButton editUsersButton;
    private JButton saveServerLogButton;
    private JButton closeServerButton;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JFrame parentFrame;
    private JFrame frame = this;

    ServerFrame(JFrame p)
    {
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
        buttonPanel.add(editUsersButton);
        buttonPanel.add(saveServerLogButton);
        buttonPanel.add(closeServerButton);

        container.setLayout(new FlowLayout());
        container.add(textAreaPanel);
        container.add(buttonPanel);

        this.add(container);
    }

    private void createTextArea()
    {
        textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        PrintStream printStream = new PrintStream(new SeverOutputStream(textArea));
        System.setOut(printStream);
    }

    private void createButtons()
    {
        editSchedulesButton = new JButton("Modify User Schedules");
        editSchedulesButton.setEnabled(true);
        EditSchedulesListener editSchedulesListener = new EditSchedulesListener();
        editSchedulesButton.addActionListener(editSchedulesListener);

        editUsersButton = new JButton("Add/Remove Users");
        editUsersButton.setEnabled(true);
        EditUsersListener editUsersListener = new EditUsersListener();
        editUsersButton.addActionListener(editUsersListener);

        saveServerLogButton = new JButton("Save Server Log");
        saveServerLogButton.setEnabled(true);
        SaveServerLogListener saveServerLogListener = new SaveServerLogListener();
        saveServerLogButton.addActionListener(saveServerLogListener);

        closeServerButton = new JButton("Close Server");
        closeServerButton.setEnabled(true);
        CloseServerListener closeServerListener = new CloseServerListener();
        closeServerButton.addActionListener(closeServerListener);
    }

    /**
     * Creates a new schedule editor object and hides current frame.
     */
    class EditSchedulesListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            setEnabled(false); // Hide Server GUI
            new ServerScheduleEditor(frame); // Create new schedule editor
        }
    }

    /**
     * Creates a new user editor object and hides current frame.
     */
    class EditUsersListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            setEnabled(false); // Hide Server GUI
            new ServerUserEditor(frame); // Create new user editor
        }
    }

    /**
     * Creates a new file chooser and outputs a .txt file containing the accumulated server logs to the
     * selected destination.
     */
    class SaveServerLogListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            setEnabled(false);

            final JFileChooser fc = new JFileChooser();
            String filename = Server.getHost() + "_server_log.txt";
            fc.setSelectedFile(new File(filename));
            int returnVal = fc.showSaveDialog(frame);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = fc.getSelectedFile();
                ArrayList<String> lines = new ArrayList<>();
                Collections.addAll(lines, textArea.getText().split("\\n"));
                Path path = file.toPath();
                
                try
                {
                    Files.write(path, lines, Charset.forName("UTF-8"));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            setEnabled(true);
        }
    }

    /**
     * Destroys the current frame, stops the server and shows the main user interface.
     */
    //TODO: Actually close down server
    class CloseServerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            Server.serverManager.interrupt(); // Shutdown server
            dispose(); // Destroy Server GUI
            parentFrame.setEnabled(true); // Show Main GUI
        }
    }
}