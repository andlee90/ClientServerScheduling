package DataModels;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Data model for holding info about a user including their associated schedule.
 */
public class DataUser implements Serializable
{
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private ArrayList<String> schedule;
    private boolean isViewed;
    private boolean isValid;

    public DataUser(String un, String p, String fn, String ln, ArrayList<String> s)
    {
        this.userName = un;
        this.password = p;
        this.firstName = fn;
        this.lastName = ln;
        this.schedule = s;
        this.isViewed = false;
        this.isValid = false;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public ArrayList<String> getSchedule()
    {
        return this.schedule;
    }

    public boolean getViewed()
    {
        return this.isViewed;
    }

    public boolean getValidity()
    {
        return this.isValid;
    }

    public void setUserName(String un)
    {
        this.userName = un;
    }

    public void setPassword(String p)
    {
        this.password = p;
    }

    public void setFirstName(String fn)
    {
        this.firstName = fn;
    }

    public void setLastName(String ln)
    {
        this.lastName = ln;
    }

    public void setSchedule(ArrayList<String> s)
    {
        this.schedule = s;
    }

    public void setViewed(boolean v)
    {
        this.isViewed = v;
    }

    public void setValidity(boolean v)
    {
        this.isValid = v;
    }
}
