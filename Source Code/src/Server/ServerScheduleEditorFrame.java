package Server;

import com.sun.xml.internal.bind.v2.TODO;

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
    //private static final int FRAME_HEIGHT = 700;

    private JComboBox usernameListBox;
    private JComboBox addDayListBox;
    private JComboBox addTimeListBox;
    private JComboBox removeDayListBox;
    private JComboBox removeTimeListBox;

    private JScrollPane scrollPane;
    private JTextArea textArea;

    private JButton addScheduleButton;
    private JButton removeScheduleButton;
    private JButton cancelButton;

    ServerScheduleEditorFrame()
    {
        createButtons();
        createComboBoxes();
        createTextArea();
        createPanels();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
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

    private void createComboBoxes()
    {
        ArrayList<String> usernameList = ServerDB.selectAllUsernames();
        usernameList.add(0, "Select User");
        String[] usernameArr = new String[usernameList.size()];
        usernameArr = usernameList.toArray(usernameArr);
        usernameListBox = new JComboBox(usernameArr);
        usernameListBox.setSelectedIndex(0);
        usernameListBox.addActionListener(new UsernameListBoxListener());

        ArrayList<String> dayList = ServerDB.selectAllDays();
        dayList.add(0, "Select Day");
        String[] dayArr = new String[dayList.size()];
        dayArr = dayList.toArray(dayArr);
        addDayListBox = new JComboBox(dayArr);
        addDayListBox.setSelectedIndex(0);

        ArrayList<String> timeList = ServerDB.selectAllTimes();
        timeList.add(0, "Select Time");
        String[] timeArr = new String[timeList.size()];
        timeArr = timeList.toArray(timeArr);
        addTimeListBox = new JComboBox(timeArr);
        addTimeListBox.setSelectedIndex(0);

        String selectDay = "Select Day";
        removeDayListBox = new JComboBox();
        removeDayListBox.addItem(selectDay);
        removeDayListBox.setSelectedIndex(0);

        String selectTime = "Select Time";
        removeTimeListBox = new JComboBox();
        removeTimeListBox.addItem(selectTime);
        removeTimeListBox.setSelectedIndex(0);
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

    private void createTextArea()
    {
        textArea = new JTextArea(10, 15);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
    }

    /**
     * Listener for the username combo box. Selecting a username prints that user's schedule to the frame's text area
     */
    class UsernameListBoxListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            String selected_text = String.valueOf(usernameListBox.getItemAt(usernameListBox.getSelectedIndex()));
            if (!selected_text.equals("Select User"))
            {
                int userId = ServerDB.selectUserIdByUsername(selected_text);
                ArrayList<Integer> scheduleIds = ServerDB.selectAllScheduleIdsByUserId(userId);
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

                textArea.setText("");
                for (int id:scheduleIds)
                {
                    textArea.append(ServerDB.selectScheduleByScheduleId(id) + "\n");
                }
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
    //TODO: Don't allow adding duplicates
    class AddScheduleButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            String selected_user = String.valueOf(usernameListBox.getItemAt(usernameListBox.getSelectedIndex()));
            String selected_day = String.valueOf(addDayListBox.getItemAt(addDayListBox.getSelectedIndex()));
            String selected_time = String.valueOf(addTimeListBox.getItemAt(addTimeListBox.getSelectedIndex()));

            if (!selected_user.equals("Select User") &&
                    !selected_day.equals("Select User") &&
                    !selected_time.equals("Select User"))
            {
                int userId = ServerDB.selectUserIdByUsername(selected_user);
                int scheduleId = ServerDB.selectScheduleIdByDayAndTime(selected_day, selected_time);

                ServerDB.insertUserSchedule(userId, scheduleId);

                ArrayList<Integer> scheduleIds = ServerDB.selectAllScheduleIdsByUserId(userId);
                textArea.setText("");
                for (int id:scheduleIds)
                {
                    textArea.append(ServerDB.selectScheduleByScheduleId(id) + "\n");
                }
            }
        }
    }

    /**
     * Listener for the delete schedule button. Removes the selected schedule from the selected user's schedule.
     */
    //TODO: Update comboboxes so they don't include deleted options
    class RemoveScheduleButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            String selected_user = String.valueOf(usernameListBox.getItemAt(usernameListBox.getSelectedIndex()));
            String selected_day = String.valueOf(removeDayListBox.getItemAt(removeDayListBox.getSelectedIndex()));
            String selected_time = String.valueOf(removeTimeListBox.getItemAt(removeTimeListBox.getSelectedIndex()));

            if (!selected_user.equals("Select User") &&
                    !selected_day.equals("Select User") &&
                    !selected_time.equals("Select User"))
            {
                int userId = ServerDB.selectUserIdByUsername(selected_user);
                int scheduleId = ServerDB.selectScheduleIdByDayAndTime(selected_day, selected_time);

                ServerDB.deleteUserSchedule(userId, scheduleId);

                ArrayList<Integer> scheduleIds = ServerDB.selectAllScheduleIdsByUserId(userId);
                textArea.setText("");
                for (int id:scheduleIds)
                {
                    textArea.append(ServerDB.selectScheduleByScheduleId(id) + "\n");
                }
            }
        }
    }

    /**
     * Listener for the cancel button. Hides the schedule editor GUI
     */

    class CancelButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            //TODO: actually destroy frame.
            setVisible(false); // Hide ScheduleEditor GUI
        }
    }
}
