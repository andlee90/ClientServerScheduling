package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Manages all network and database related activities on a background
 * thread to avoid interfering with the user interface.
 */

class ServerManager implements Runnable
{
    private int portNumber;

    ServerManager(String pn)
    {
        this.portNumber = Integer.parseInt(pn);
    }

    @Override
    public void run()
    {
        ServerDB.createDB();

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                out.println(inputLine);
            }
        } catch (IOException e)
        {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
