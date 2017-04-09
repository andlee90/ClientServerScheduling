package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Creates a server on a new thread and listens for incoming client connections.
 * Creates a new thread for handling each new connected client.
 */
class ServerManager implements Runnable
{
    private int portNumber;
    private ServerSocket serverSocket;
    private Socket socket;

    ServerManager(String pn)
    {
        this.portNumber = Integer.parseInt(pn);
        serverSocket = null;
        socket = null;
    }

    @Override
    public void run()
    {
        ServerDB.createDB();
        System.out.println("Waiting for clients...");

        try
        {
            serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException e)
        {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
        while (true)
        {
            try
            {
                socket = serverSocket.accept();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            new Thread(new ServerThread(socket)).start();
        }
    }
}
