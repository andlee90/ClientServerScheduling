package Client;

import DataModels.DataCommand;
import DataModels.DataUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    private JButton saveScheduleButton;
    static JButton closeButton;

    private static JFrame frame;
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
        frame = this;

        populateLists();
        createLabels();
        createButtons();
        createComboBoxes();
        createTextArea();
        createPanels();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        frame = this;

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
        addDayListBox.setEnabled(false);

        allTimeList.add(0, "Select Time");
        String[] timeArr = new String[allTimeList.size()];
        timeArr = allTimeList.toArray(timeArr);
        addTimeListBox = new JComboBox<>(timeArr);
        addTimeListBox.setSelectedIndex(0);
        addTimeListBox.setEnabled(false);

        userDayList.add(0, "Select Day");
        dayArr = new String[userDayList.size()];
        dayArr = userDayList.toArray(dayArr);
        removeDayListBox = new JComboBox<>(dayArr);
        removeDayListBox.setSelectedIndex(0);
        removeDayListBox.setEnabled(false);

        userTimeList.add(0, "Select Time");
        timeArr = new String[userTimeList.size()];
        timeArr = userTimeList.toArray(timeArr);
        removeTimeListBox = new JComboBox<>(timeArr);
        removeTimeListBox.setSelectedIndex(0);
        removeTimeListBox.setEnabled(false);

        if (user.getIsAdmin())
        {
            addDayListBox.setEnabled(true);
            addTimeListBox.setEnabled(true);
            removeDayListBox.setEnabled(true);
            removeTimeListBox.setEnabled(true);
        }
    }

    private void createPanels()
    {
        JPanel container = new JPanel();
        JPanel userSchedulePanel = new JPanel();
        JPanel scheduleEditPanel = new JPanel();
        JPanel scheduleAddPanel = new JPanel();
        JPanel scheduleRemovePanel = new JPanel();
        JPanel buttonPanel = new JPanel();

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

        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(saveScheduleButton);
        buttonPanel.add(closeButton);

        scheduleEditPanel.setLayout(new BoxLayout(scheduleEditPanel, BoxLayout.Y_AXIS));
        scheduleEditPanel.add(scheduleAddPanel);
        scheduleEditPanel.add(new JSeparator());
        scheduleEditPanel.add(scheduleRemovePanel);
        scheduleEditPanel.add(new JSeparator());
        scheduleEditPanel.add(buttonPanel);

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

        for(String schedule:user.getSchedule())
        {
            textArea.append(schedule + "\n");
        }
    }

    private void createButtons()
    {
        addScheduleButton = new JButton("Add Schedule");
        ActionListener addScheduleButtonListener = new AddScheduleButtonListener();
        addScheduleButton.addActionListener(addScheduleButtonListener);
        addScheduleButton.setEnabled(false);

        removeScheduleButton = new JButton("Remove Schedule");
        ActionListener removeScheduleButtonListener = new RemoveScheduleButtonListener();
        removeScheduleButton.addActionListener(removeScheduleButtonListener);
        removeScheduleButton.setEnabled(false);

        saveScheduleButton = new JButton("Save Schedule to File");
        ActionListener saveScheduleButtonListener = new SaveScheduleToFileListener();
        saveScheduleButton.addActionListener(saveScheduleButtonListener);
        saveScheduleButton.setEnabled(true);

        closeButton = new JButton("Close Connection");
        ActionListener closeButtonListener = new CloseButtonListener();
        closeButton.addActionListener(closeButtonListener);
        closeButton.setEnabled(true);

        if (user.getIsAdmin())
        {
            addScheduleButton.setEnabled(true);
            removeScheduleButton.setEnabled(true);
        }
    }

    private void updateTextArea()
    {
        textArea.setText("");
        Collections.sort(Client.currentUserSchedules);
        for(String schedule:Client.currentUserSchedules)
        {
            textArea.append(schedule + "\n");
        }
    }

    private void updateLists()
    {
        userDayList.clear();
        userTimeList.clear();
        userDayList.add(0, "Select Day");
        userTimeList.add(0, "Select Time");

        for (String schedule : Client.currentUserSchedules) {
            userDayList.add(schedule.substring(0, schedule.indexOf(" ")));
            userTimeList.add(schedule.substring(schedule.indexOf(" "), schedule.length()));
        }

        Set<String> daySet = new LinkedHashSet<>(userDayList);
        String[] dayArr = new String[daySet.size()];
        dayArr = daySet.toArray(dayArr);
        DefaultComboBoxModel dayModel = new DefaultComboBoxModel(dayArr);
        removeDayListBox.removeAllItems();
        removeDayListBox.setModel(dayModel);
        removeDayListBox.updateUI();
        removeDayListBox.setSelectedIndex(0);

        Set<String> timeSet = new LinkedHashSet<>(userTimeList);
        String[] timeArr = new String[timeSet.size()];
        timeArr = timeSet.toArray(timeArr);
        DefaultComboBoxModel timeModel = new DefaultComboBoxModel(timeArr);
        removeTimeListBox.removeAllItems();
        removeTimeListBox.setModel(timeModel);
        removeTimeListBox.updateUI();
        removeTimeListBox.setSelectedIndex(0);
    }

    /**
     * Listener for the add schedule button. Adds the selected schedule to the selected user's schedule.
     */
    class AddScheduleButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            String selected_day = String.valueOf(addDayListBox.getItemAt(addDayListBox.getSelectedIndex()));
            String selected_time = String.valueOf(addTimeListBox.getItemAt(addTimeListBox.getSelectedIndex()));
            String schedule = selected_day + " " + selected_time;

            if (!selected_day.equals("Select Day") && !selected_time.equals("Select Time"))
            {
                boolean exists = false;
                for (String s:Client.currentUserSchedules)
                {
                    if (schedule.equals(s))
                    {
                        exists = true;
                    }
                }
                if (!exists)
                {
                    Client.command.setModifiedSchedule(schedule);
                    Client.command.setCommandType(DataCommand.CommandType.INSERT_SCHEDULE);
                    user.getSchedule().add(schedule);

                    while(!Client.command.getIsModified())
                    {
                        try
                        {
                            TimeUnit.MILLISECONDS.sleep(1);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    updateTextArea();
                    updateLists();
                    Client.command.setIsModified(false);
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
            String selected_day = String.valueOf(removeDayListBox.getItemAt(removeDayListBox.getSelectedIndex()));
            String selected_time = String.valueOf(removeTimeListBox.getItemAt(removeTimeListBox.getSelectedIndex()));
            String schedule = selected_day + " " + selected_time;

            if (!selected_day.equals("Select Day") && !selected_time.equals("Select Time"))
            {
                Client.command.setModifiedSchedule(schedule);
                Client.command.setCommandType(DataCommand.CommandType.DELETE_SCHEDULE);
                user.getSchedule().remove(schedule);

                while(!Client.command.getIsModified())
                {
                    try
                    {
                        TimeUnit.MILLISECONDS.sleep(1);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

                updateTextArea();
                updateLists();
                Client.command.setIsModified(false);
            }
        }
    }

    /**
     * Creates a new file chooser and outputs a .txt file containing the accumulated server logs to the
     * selected destination.
     */
    class SaveScheduleToFileListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            setEnabled(false);

            final JFileChooser fc = new JFileChooser();
            String filename = user.getUserName() + "_schedule.txt";
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
     * Listener for the cancel button. Hides the schedule editor GUI
     */
    class CloseButtonListener implements ActionListener
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
