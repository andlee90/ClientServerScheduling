package Client;

import DataModels.DataCommand;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Handles all client-related activities.
 */
public class Client
{
    private JFrame parentFrame;
    private String portNumber;
    private String hostName;

    /**
     * The command object is set by user input (i.e. clicking a button) and determines the details of the command issued
     * to the waiting server. It is "volatile" to allow it's state to be affected and the changes made to be read by
     * multiple threads. No actions occur when it's command type is DEFAULT.
     */
    volatile static DataCommand command = new DataCommand(DataCommand.CommandType.DEFAULT, null, null);

    /**
     * The currentUserSchedule object is set by the manager when a new command object is read. It is "volatile" to allow
     * it's state to be affected and the changes made to be read by multiple threads.
     */
    volatile static ArrayList<String> currentUserSchedules = new ArrayList<>();

    /**
     * The isValidHost bool is set to false when an invalid host exception is thrown. It is "volatile" to allow it's
     * state to be affected and the changes made to be read by multiple threads.
     */
    volatile static boolean isValidHost = true;

    public Client(String pn, String hn, JFrame pf)
    {
        this.parentFrame = pf;
        this.portNumber = pn;
        this.hostName = hn;

        createAuthenticationFrame();
    }

    private void createAuthenticationFrame()
    {
        new ClientAuthenticationFrame(portNumber, hostName, parentFrame);
    }

    /**
    * Sorts schedules so that they are grouped by day.
    */
    static void sortSchedules()
    {
        ArrayList<String> mondaySchedules = new ArrayList<>();
        ArrayList<String> tuesdaySchedules = new ArrayList<>();
        ArrayList<String> wednesdaySchedules = new ArrayList<>();
        ArrayList<String> thursdaySchedules = new ArrayList<>();
        ArrayList<String> fridaySchedules = new ArrayList<>();

        for (String schedule:currentUserSchedules)
        {
            if(schedule.substring(0,1).equals("M"))
            {
                mondaySchedules.add(schedule);
            }
            else if(schedule.substring(0,2).equals("Tu"))
            {
                tuesdaySchedules.add(schedule);
            }
            else if(schedule.substring(0,1).equals("W"))
            {
                wednesdaySchedules.add(schedule);
            }
            else if(schedule.substring(0,2).equals("Th"))
            {
                thursdaySchedules.add(schedule);
            }
            else if(schedule.substring(0,1).equals("F"))
            {
                fridaySchedules.add(schedule);
            }
        }

        currentUserSchedules.clear();
        currentUserSchedules.addAll(mondaySchedules);
        currentUserSchedules.addAll(tuesdaySchedules);
        currentUserSchedules.addAll(wednesdaySchedules);
        currentUserSchedules.addAll(thursdaySchedules);
        currentUserSchedules.addAll(fridaySchedules);
    }
}
