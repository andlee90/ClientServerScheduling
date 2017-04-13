package DataModels;

import java.io.Serializable;

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
    private String schedule;
    private boolean isValid;

    public DataCommand(CommandType ct, String s)
    {
        this.commandType = ct;
        this.schedule = s;
        this.isValid = false;
    }

    public CommandType getCommandType()
    {
        return commandType;
    }

    public String getSchedule()
    {
        return schedule;
    }

    public boolean getValidity()
    {
        return isValid;
    }

    public void setCommandType(CommandType c)
    {
        this.commandType = c;
    }

    public void setSchedule(String s)
    {
        this.schedule = s;
    }

    public void setValidity(boolean v)
    {
        this.isValid = v;
    }
}
