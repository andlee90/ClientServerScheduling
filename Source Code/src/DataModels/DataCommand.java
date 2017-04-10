package DataModels;

import java.io.Serializable;

/**
 * Created by andrewsmith on 4/10/17.
 */
public class DataCommand implements Serializable
{
    public enum Command
    {
        CLOSE_SERVER, INSERT_SCHEDLE, DELETE_SCHEDULE
    }

    private Command command;

    public DataCommand(Command c)
    {
        this.command = c;
    }

    public Command getCommand()
    {
        return command;
    }

    public void setCommand(Command m)
    {
        this.command = m;
    }
}
