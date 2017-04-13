package Client;

import DataModels.DataCommand;
import DataModels.DataUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Handles creation and management of all client user interface objects.
 */
class ClientFrame extends JFrame
{
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 250;

    private JComboBox<String> addDayListBox;
    private JComboBox<String> addTimeListBox;
    private JComboBox<String> removeDayListBox;
    private JComboBox<String> removeTimeListBox;

    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JLabel userLabel;

    private JButton addScheduleButton;
    private JButton removeScheduleButton;
    private JButton cancelButton;

    private JFrame parentFrame;
    private ClientManager clientManager;
    private DataUser user;

    private ArrayList<String> userDayList= new ArrayList<>();
    private ArrayList<String> userTimeList = new ArrayList<>();
    private ArrayList<String> allDayList= new ArrayList<>();
    private ArrayList<String> allTimeList = new ArrayList<>();

    ClientFrame(ClientManager sm, JFrame p, DataUser u)
    {
        this.clientManager = sm;
        this.parentFrame = p;
        this.user = u;

        populateLists();
        createLabels();
        createButtons();
        createComboBoxes();
        createTextArea();
        createPanels();
        updateTextArea();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private void createLabels()
    {
        userLabel = new JLabel(user.getLastName()
                + ", " + user.getFirstName());
    }

    private void populateLists()
    {
        allDayList.add("Monday");
        allDayList.add("Tuesday");
        allDayList.add("Wednesday");
        allDayList.add("Thursday");
        allDayList.add("Friday");

        allTimeList.add("9am - 10am");
        allTimeList.add("10am - 11am");
        allTimeList.add("11am - 12pm");
        allTimeList.add("1pm - 2pm");
        allTimeList.add("2pm - 3pm");

        for (String schedule:user.getSchedule())
        {
            userDayList.add(schedule.substring(0,schedule.indexOf(" ")));
            userTimeList.add(schedule.substring(schedule.indexOf(" "),schedule.length()));
        }
    }

    private void createComboBoxes()
    {
        allDayList.add(0, "Select Day");
        String[] dayArr = new String[allDayList.size()];
        dayArr = allDayList.toArray(dayArr);
        addDayListBox = new JComboBox<>(dayArr);
        addDayListBox.setSelectedIndex(0);

        allTimeList.add(0, "Select Time");
        String[] timeArr = new String[allTimeList.size()];
        timeArr = allTimeList.toArray(timeArr);
        addTimeListBox = new JComboBox<>(timeArr);
        addTimeListBox.setSelectedIndex(0);

        userDayList.add(0, "Select Day");
        dayArr = new String[userDayList.size()];
        dayArr = userDayList.toArray(dayArr);
        removeDayListBox = new JComboBox<>(dayArr);
        removeDayListBox.setSelectedIndex(0);

        userTimeList.add(0, "Select Time");
        timeArr = new String[userTimeList.size()];
        timeArr = userTimeList.toArray(timeArr);
        removeTimeListBox = new JComboBox<>(timeArr);
        removeTimeListBox.setSelectedIndex(0);
    }

    private void createPanels()
    {
        JPanel container = new JPanel();
        JPanel userSchedulePanel = new JPanel();
        JPanel scheduleEditPanel = new JPanel();
        JPanel scheduleAddPanel = new JPanel();
        JPanel scheduleRemovePanel = new JPanel();

        userSchedulePanel.setLayout(new BoxLayout(userSchedulePanel, BoxLayout.Y_AXIS));
        userSchedulePanel.add(userLabel);
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

    private void createTextArea()
    {
        textArea = new JTextArea(10, 15);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
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

    private void updateTextArea()
    {
        textArea.setText("");
        Collections.sort(user.getSchedule());
        for(String schedule:user.getSchedule())
        {
            textArea.append(schedule + "\n");
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
            String selected_day = String.valueOf(addDayListBox.getItemAt(addDayListBox.getSelectedIndex()));
            String selected_time = String.valueOf(addTimeListBox.getItemAt(addTimeListBox.getSelectedIndex()));
            String schedule = selected_day + " " + selected_time;

            if (!selected_day.equals("Select Day") && !selected_time.equals("Select Time"))
            {
                Client.command.setSchedule(schedule);
                Client.command.setCommandType(DataCommand.CommandType.INSERT_SCHEDULE);
                user.getSchedule().add(schedule);
                updateTextArea();
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
            String selected_day = String.valueOf(removeDayListBox.getItemAt(removeDayListBox.getSelectedIndex()));
            String selected_time = String.valueOf(removeTimeListBox.getItemAt(removeTimeListBox.getSelectedIndex()));
            String schedule = selected_day + " " + selected_time;

            if (!selected_day.equals("Select Day") && !selected_time.equals("Select Time"))
            {
                Client.command.setSchedule(schedule);
                Client.command.setCommandType(DataCommand.CommandType.DELETE_SCHEDULE);
                user.getSchedule().remove(schedule);
                updateTextArea();
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
            Client.command.setCommandType(DataCommand.CommandType.CLOSE_SERVER);
            setVisible(false);
            parentFrame.setVisible(true);
        }
    }
}
