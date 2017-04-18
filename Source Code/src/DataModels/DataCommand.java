package DataModels;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Data model for holding a command to be to passed to the server and to hold any results generated.
 */
public class DataCommand implements Serializable
{
    /**
     * Enum to hold different command types. Used to determine server's response to client's request.
     */
    public enum CommandType
    {
        DEFAULT, CLOSE_SERVER, INSERT_SCHEDULE, DELETE_SCHEDULE
    }

    private CommandType commandType; // Type of command
    private String modifiedSchedule; // Schedule to add or remove from database
    private ArrayList<String> updatedUserSchedules; // Updated list of schedules for user
    private boolean isModified; // true if command executed successfully

    public DataCommand(CommandType ct, String ms, ArrayList<String> uus)
    {
        this.commandType = ct;
        this.modifiedSchedule = ms;
        this.updatedUserSchedules = uus;
        this.isModified = false;
    }

    public CommandType getCommandType()
    {
        return commandType;
    }

    public String getModifiedSchedule()
    {
        return modifiedSchedule;
    }

    public ArrayList<String> getUpdatedUserSchedules()
    {
        return updatedUserSchedules;
    }

    public boolean getIsModified()
    {
        return isModified;
    }

    public void setCommandType(CommandType ct)
    {
        this.commandType = ct;
    }

    public void setModifiedSchedule(String ms)
    {
        this.modifiedSchedule = ms;
    }

    public void setUpdatedUserSchedules(ArrayList<String> uus)
    {
        this.updatedUserSchedules = uus;
    }

    public void setIsModified(boolean im)
    {
        this.isModified = im;
    }
}
