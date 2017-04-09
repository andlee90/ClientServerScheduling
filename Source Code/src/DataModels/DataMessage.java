package DataModels;

import java.io.Serializable;

/**
 * Data model for holding a general message.
 */
public class DataMessage implements Serializable
{
    private String message;

    public DataMessage(String m)
    {
        this.message = m;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String m)
    {
        this.message = m;
    }
}
