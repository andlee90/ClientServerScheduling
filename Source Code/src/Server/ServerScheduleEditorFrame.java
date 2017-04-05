package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Handles creation and management of all schedule editor user interface objects.
 */
class ServerScheduleEditorFrame extends JFrame
{
    private static final int FRAME_WIDTH = 725;
    private static final int FRAME_HEIGHT = 375;

    private JComboBox userListBox;
    private JScrollPane scrollPane;
    private JTextArea textArea;

    ServerScheduleEditorFrame()
    {
        createButtons();
        createComboBox();
        createTextArea();
        createPanels();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private void createPanels()
    {
        JPanel container = new JPanel();
        JPanel userSelectPanel = new JPanel();
        JPanel textAreaPanel = new JPanel();

        userSelectPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        userSelectPanel.add(userListBox);

        textAreaPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        textAreaPanel.add(scrollPane);

        container.setLayout(new FlowLayout());
        container.add(userSelectPanel);
        container.add(textAreaPanel);

        this.add(container);
    }

    private void createComboBox()
    {
        ArrayList<String> usernameList = ServerDB.selectAllUsernames();
        usernameList.add(0, "Select User");
        String[] usernameArr = new String[usernameList.size()];
        usernameArr = usernameList.toArray(usernameArr);

        userListBox = new JComboBox(usernameArr);
        userListBox.setSelectedIndex(0);
        userListBox.addActionListener(new ComboBoxListener());
    }

    private void createButtons()
    {

    }

    private void createTextArea()
    {
        textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
    }

    /**
     * Listener for the user combo box. Selecting a user prints that user's schedule to the frame's text area
     */
    class ComboBoxListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            String selected_text = String.valueOf(userListBox.getItemAt(userListBox.getSelectedIndex()));
            if (!selected_text.equals("Select User"))
            {
                int userId = ServerDB.selectUserIdByUsername(selected_text);
                ArrayList<Integer> scheduleIds = ServerDB.selectAllScheduleIdsByUserId(userId);

                textArea.setText("");
                for (int id:scheduleIds)
                {
                    textArea.append(ServerDB.selectScheduleByScheduleId(id) + "\n");
                }
            }
        }
    }

}
