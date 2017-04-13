package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Handles creation and management of all schedule editor user interface objects.
 */
class ServerScheduleEditorFrame extends JFrame
{
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 250;

    private JFrame parentFrame;

    private JButton addScheduleButton;
    private JButton removeScheduleButton;
    private JButton cancelButton;

    private JComboBox usernameListBox;
    private JComboBox addDayListBox;
    private JComboBox addTimeListBox;
    private JComboBox removeDayListBox;
    private JComboBox removeTimeListBox;

    private JScrollPane scrollPane;
    private JTextArea textArea;

    private ArrayList<String> currentUserSchedule;

    ServerScheduleEditorFrame(JFrame pf)
    {
        this.parentFrame = pf;
        createButtons();
        createComboBoxes();
        createTextArea();
        createPanels();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private void createButtons()
    {
        addScheduleButton = new JButton("Add Schedule");
        ActionListener addScheduleButtonListener = new AddScheduleButtonListener();
        addScheduleButton.addActionListener(addScheduleButtonListener);
        addScheduleButton.setEnabled(true);

        removeScheduleButton = new JButton("Remove Schedule");
        ActionListener removeScheduleButtonListener = new RemoveScheduleButtonListener();
        removeScheduleButton.addActionListener(removeScheduleButtonListener);
        removeScheduleButton.setEnabled(true);

        cancelButton = new JButton("Cancel");
        ActionListener cancelButtonListener = new CancelButtonListener();
        cancelButton.addActionListener(cancelButtonListener);
        cancelButton.setEnabled(true);
    }

    private void createComboBoxes()
    {
        String selectionDefault;

        ArrayList<String> usernameList = ServerDB.selectAllUsernames();
        usernameList.add(0, "Select User");
        String[] usernameArr = new String[usernameList.size()];
        usernameArr = usernameList.toArray(usernameArr);
        usernameListBox = new JComboBox(usernameArr);
        usernameListBox.setSelectedIndex(0);
        usernameListBox.addActionListener(new UsernameListBoxListener());

        selectionDefault = "Select Day";
        addDayListBox = new JComboBox();
        addDayListBox.addItem(selectionDefault);
        addDayListBox.setSelectedIndex(0);

        selectionDefault = "Select Time";
        addTimeListBox = new JComboBox();
        addTimeListBox.addItem(selectionDefault);
        addTimeListBox.setSelectedIndex(0);

        selectionDefault = "Select Day";
        removeDayListBox = new JComboBox();
        removeDayListBox.addItem(selectionDefault);
        removeDayListBox.setSelectedIndex(0);

        selectionDefault = "Select Time";
        removeTimeListBox = new JComboBox();
        removeTimeListBox.addItem(selectionDefault);
        removeTimeListBox.setSelectedIndex(0);
    }

    private void createTextArea()
    {
        textArea = new JTextArea(10, 15);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
    }

    private void createPanels()
    {
        JPanel container = new JPanel();
        JPanel userSchedulePanel = new JPanel();
        JPanel scheduleEditPanel = new JPanel();
        JPanel scheduleAddPanel = new JPanel();
        JPanel scheduleRemovePanel = new JPanel();

        userSchedulePanel.setLayout(new BoxLayout(userSchedulePanel, BoxLayout.Y_AXIS));
        userSchedulePanel.add(usernameListBox);
        userSchedulePanel.add(Box.createRigidArea(new Dimension(5,5)));
        userSchedulePanel.add(scrollPane);

        scheduleAddPanel.setLayout(new GridLayout(2,2));
        scheduleAddPanel.add(addDayListBox);
        scheduleAddPanel.add(addTimeListBox);
        scheduleAddPanel.add(new JLabel(""));
        scheduleAddPanel.add(addScheduleButton);

        scheduleRemovePanel.setLayout(new GridLayout(2,2));
        scheduleRemovePanel.add(removeDayListBox);
        scheduleRemovePanel.add(removeTimeListBox);
        scheduleRemovePanel.add(new JLabel(""));
        scheduleRemovePanel.add(removeScheduleButton);

        scheduleEditPanel.setLayout(new BoxLayout(scheduleEditPanel, BoxLayout.Y_AXIS));
        scheduleEditPanel.add(scheduleAddPanel);
        scheduleEditPanel.add(new JSeparator());
        scheduleEditPanel.add(scheduleRemovePanel);
        scheduleEditPanel.add(new JSeparator());
        scheduleEditPanel.add(cancelButton);

        container.setLayout(new FlowLayout());
        container.add(userSchedulePanel);
        container.add(scheduleEditPanel);

        this.add(container);
    }

    private void updateTextAreaAndComboBoxes(String su)
    {
        currentUserSchedule = new ArrayList<>();
        int userId = ServerDB.selectUserIdByUsername(su);
        ArrayList<Integer> scheduleIds = ServerDB.selectAllScheduleIdsByUserId(userId);

        textArea.setText("");
        for (int id:scheduleIds)
        {
            currentUserSchedule.add(ServerDB.selectScheduleByScheduleId(id));
            textArea.append(ServerDB.selectScheduleByScheduleId(id) + "\n");
        }

        // Add all days to add day combo box
        ArrayList<String> addDayList = ServerDB.selectAllDays();
        addDayList.add(0, "Select Day");
        String[] addDayArr = new String[addDayList.size()];
        addDayList.toArray(addDayArr);
        DefaultComboBoxModel addDayModel = new DefaultComboBoxModel(addDayArr);
        addDayListBox.setModel(addDayModel);
        addDayListBox.updateUI();

        // Add all times to add time combo box
        ArrayList<String> addTimeList = ServerDB.selectAllTimes();
        addTimeList.add(0, "Select Time");
        String[] addTimeArr = new String[addTimeList.size()];
        addTimeList.toArray(addTimeArr);
        DefaultComboBoxModel addTimeModel = new DefaultComboBoxModel(addTimeArr);
        addTimeListBox.setModel(addTimeModel);
        addTimeListBox.updateUI();

        // Add only user scheduled days to remove day combo box
        ArrayList<String> removeDayList = new ArrayList<>();
        for (int id:scheduleIds)
        {
            removeDayList.add(ServerDB.selectDayByScheduleId(id));
        }
        removeDayList.add(0, "Select Day");
        Set<String> removeDaySet = new LinkedHashSet<>(removeDayList);
        String[] removeDayArr = new String[removeDaySet.size()];
        removeDayArr = removeDaySet.toArray(removeDayArr);
        DefaultComboBoxModel dayModel = new DefaultComboBoxModel(removeDayArr);
        removeDayListBox.setModel( dayModel );
        removeDayListBox.updateUI();

        // Add only user scheduled times to remove time combo box
        ArrayList<String> removeTimeList = new ArrayList<>();
        for (int id:scheduleIds)
        {
            removeTimeList.add(ServerDB.selectTimeByScheduleId(id));
        }
        removeTimeList.add(0, "Select Time");
        Set<String> removeTimeSet = new LinkedHashSet<>(removeTimeList);
        String[] removeTimeArr = new String[removeTimeSet.size()];
        removeTimeArr = removeTimeSet.toArray(removeTimeArr);
        DefaultComboBoxModel timeModel = new DefaultComboBoxModel(removeTimeArr);
        removeTimeListBox.setModel( timeModel );
        removeTimeListBox.updateUI();
    }

    /**
     * Listener for the username combo box. Selecting a username prints that user's schedule to the frame's text area
     */
    class UsernameListBoxListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            String selectedUsername = String.valueOf(usernameListBox.getItemAt(usernameListBox.getSelectedIndex()));

            if (!selectedUsername.equals("Select User"))
            {
                updateTextAreaAndComboBoxes(selectedUsername);
            }
            else
            {
                textArea.setText("");
            }
        }
    }

    /**
     * Listener for the add schedule button. Adds the selected schedule to the selected user's schedule.
     */
    class AddScheduleButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            String selected_user = String.valueOf(usernameListBox.getItemAt(usernameListBox.getSelectedIndex()));
            String selected_day = String.valueOf(addDayListBox.getItemAt(addDayListBox.getSelectedIndex()));
            String selected_time = String.valueOf(addTimeListBox.getItemAt(addTimeListBox.getSelectedIndex()));
            String schedule = selected_day + " " + selected_time;

            if (!selected_user.equals("Select User") &&
                    !selected_day.equals("Select Day") &&
                    !selected_time.equals("Select Time"))
            {
                int userId = ServerDB.selectUserIdByUsername(selected_user);
                int scheduleId = ServerDB.selectScheduleIdByDayAndTime(selected_day, selected_time);

                boolean exists = false;
                for (String s:currentUserSchedule)
                {
                    if (schedule.equals(s))
                    {
                        exists = true;
                    }
                }
                if (!exists)
                {
                    ServerDB.insertUserSchedule(userId, scheduleId);
                    updateTextAreaAndComboBoxes(selected_user);
                }
            }
        }
    }

    /**
     * Listener for the delete schedule button. Removes the selected schedule from the selected user's schedule.
     */
    class RemoveScheduleButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            String selected_user = String.valueOf(usernameListBox.getItemAt(usernameListBox.getSelectedIndex()));
            String selected_day = String.valueOf(removeDayListBox.getItemAt(removeDayListBox.getSelectedIndex()));
            String selected_time = String.valueOf(removeTimeListBox.getItemAt(removeTimeListBox.getSelectedIndex()));

            if (!selected_user.equals("Select User") &&
                    !selected_day.equals("Select Day") &&
                    !selected_time.equals("Select Time"))
            {
                int userId = ServerDB.selectUserIdByUsername(selected_user);
                int scheduleId = ServerDB.selectScheduleIdByDayAndTime(selected_day, selected_time);

                ServerDB.deleteUserSchedule(userId, scheduleId);
                updateTextAreaAndComboBoxes(selected_user);
            }
        }
    }

    /**
     * Listener for the cancel button. Destroys the schedule editor GUI and shows the Server GUI.
     */
    class CancelButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            dispose();
            parentFrame.setVisible(true);
        }
    }
}
