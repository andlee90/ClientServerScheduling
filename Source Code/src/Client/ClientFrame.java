package Client;

import DataModels.DataCommand;
import DataModels.DataUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Handles creation and management of all client user interface objects.
 */
class ClientFrame extends JFrame
{
    private static final int FRAME_WIDTH = 725;
    private static final int FRAME_HEIGHT = 375;

    private JComboBox usernameListBox;
    private JComboBox addDayListBox;
    private JComboBox addTimeListBox;
    private JComboBox removeDayListBox;
    private JComboBox removeTimeListBox;

    private JButton requestButton;
    private JButton editSchedulesButton;
    private JButton closeClientButton;

    private JScrollPane scrollPane;

    private JFrame parentFrame;

    private ClientManager clientManager;
    private DataUser user;

    ArrayList<String> dayList;
    ArrayList<String> timeList;

    ClientFrame(ClientManager sm, JFrame p, DataUser u)
    {
        dayList= new ArrayList<String>();
        timeList = new ArrayList<String>();
        this.clientManager = sm;
        this.parentFrame = p;
        this.user = u;

        createButtons();
        createTextArea();
        createComboBoxes();
        createPanels();
        System.out.println(dayList);
        System.out.println(timeList);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private void createComboBoxes()
    {
        dayList.add(0, "Select Day");
        dayList.add("Entire Week");
        String[] dayArr = new String[dayList.size()];
        dayArr = dayList.toArray(dayArr);
        addDayListBox = new JComboBox(dayArr);
        addDayListBox.setSelectedIndex(0);

       /* timeList.add(0, "Select Time");
        String[] timeArr = new String[timeList.size()];
        timeArr = timeList.toArray(timeArr);
        addTimeListBox = new JComboBox(timeArr);
        addTimeListBox.setSelectedIndex(0);*/

        String selectDay = "Select Day";
        removeDayListBox = new JComboBox();
        removeDayListBox.addItem(selectDay);
        removeDayListBox.setSelectedIndex(0);

        String selectTime = "Select Time";
        removeTimeListBox = new JComboBox();
        removeTimeListBox.addItem(selectTime);
        removeTimeListBox.setSelectedIndex(0);
    }

    private void createPanels()
    {
        JPanel container = new JPanel();
        JPanel textAreaPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        textAreaPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        textAreaPanel.add(scrollPane);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(addDayListBox);
        buttonPanel.add(requestButton);
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
                "\n" + "Schedule:\n ");
        for (String schedule:user.getSchedule())
        {
            if(!(dayList.contains((schedule.substring(0,schedule.indexOf(" "))))))
            {
                dayList.add(schedule.substring(0,schedule.indexOf(" ")));
            }
            timeList.add(schedule.substring(schedule.indexOf(" "),schedule.length()-1));
            textArea.append(schedule + "\n");
        }

        scrollPane = new JScrollPane(textArea);
    }

    private void createButtons()
    {
        requestButton = new JButton("View Day");
        requestButton.setEnabled(true);
        ClientFrame.RequestListener RequestListener = new ClientFrame.RequestListener();
        requestButton.addActionListener(RequestListener);

        editSchedulesButton = new JButton("Edit Schedule");
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
    class RequestListener implements ActionListener
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
            Client.command.setCommandType(DataCommand.CommandType.CLOSE_SERVER);
            setVisible(false);
            parentFrame.setVisible(true);
        }
    }
}
