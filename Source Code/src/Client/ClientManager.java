package Client;
import java.io.*;
import java.net.*;

/**
 * Created by Tim on 4/2/2017.
 */
class ClientManager implements Runnable
{
    private int portNumber;
    private String hostName;

    ClientManager(String pn, String hn)
    {
        this.portNumber = Integer.parseInt(pn);
        this.hostName=hn;
    }

    public void run()
    {
        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        )
        {
            String userInput="Client "+Inet4Address.getLocalHost().getHostAddress()+" connected.";
            out.println(userInput);
            echoSocket.close();
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);

        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }

}
