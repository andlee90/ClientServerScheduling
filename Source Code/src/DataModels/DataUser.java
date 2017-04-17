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
    private boolean isAdmin;
    private ArrayList<String> schedule;
    private boolean isViewed;
    private boolean isValid;

    public DataUser(String un, String p, String fn, String ln, boolean ia, ArrayList<String> s)
    {
        this.userName = un;
        this.password = p;
        this.firstName = fn;
        this.lastName = ln;
        this.isAdmin = ia;
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

    public boolean getIsAdmin()
    {
        return isAdmin;
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

    public void setIsAdmin(boolean ia)
    {
        this.isAdmin = ia;
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
