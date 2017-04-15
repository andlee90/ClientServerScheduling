package DataModels;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Data model for holding a command to be to passed to the server.
 */
public class DataCommand implements Serializable
{
    public enum CommandType
    {
        DEFAULT, CLOSE_SERVER, INSERT_SCHEDULE, DELETE_SCHEDULE
    }

    private CommandType commandType;
    private String modifiedSchedule;
    private ArrayList<String> updatedUserSchedules;
    private boolean isModified;

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
