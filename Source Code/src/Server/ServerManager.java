package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Creates a server on a new thread and listens for incoming client connections.
 * For each new client up to MAX_CLIENTS, a new thread is created for management
 * of that thread.
 */
class ServerManager extends Thread
{
    final private int MAX_CLIENTS;
    final private ServerSocket serverSocket;
    final private ServerThread[] clientConnections;

    ServerManager(String pn, String mc) throws IOException
    {
        int portNumber = Integer.parseInt(pn);
        this.MAX_CLIENTS = Integer.parseInt(mc);
        this.clientConnections = new ServerThread[MAX_CLIENTS];
        this.serverSocket = new ServerSocket(portNumber);
        start();
    }

    @Override
    public void run()
    {
        ServerDB.createDB();
        System.out.println("Total connected clients: 0/" + this.MAX_CLIENTS);

        while (true)
        {
            Socket socket = null;
            try
            {
                socket = this.serverSocket.accept();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            assignClientToThread(socket);
        }
    }

    private void assignClientToThread(Socket socket)
    {
        for (int i = 0; i < this.MAX_CLIENTS; i++)
        {
            if(this.clientConnections[i] == null)
            {
                System.out.println("Total connected clients: " + (i + 1) + "/" + this.MAX_CLIENTS);
                this.clientConnections[i] = new ServerThread(socket, i);
                break;
            }
        }
    }
}
