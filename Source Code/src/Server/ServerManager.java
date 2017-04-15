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
    static ServerThread[] clientConnections;

    ServerManager(String pn, String mc) throws IOException
    {
        int portNumber = Integer.parseInt(pn);
        this.MAX_CLIENTS = Integer.parseInt(mc);
        clientConnections = new ServerThread[MAX_CLIENTS];
        this.serverSocket = new ServerSocket(portNumber);
        start();
    }

    @Override
    public void run()
    {
        ServerDB.createDB();
        System.out.println("> Total connected clients: 0/" + this.MAX_CLIENTS);

        Socket socket = null;

        while (!interrupted())
        {
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
        try
        {
            for(ServerThread st:clientConnections)
            {
                st.close();
                st.interrupt();
            }
            // TODO: Address already in use (Bind failed) when restarting server on same port.
            serverSocket.setReuseAddress(true);
            serverSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void assignClientToThread(Socket socket)
    {
        for (int i = 0; i < this.MAX_CLIENTS; i++)
        {
            if(clientConnections[i] == null)
            {
                System.out.println("> Total connected clients: " + (i + 1) + "/" + this.MAX_CLIENTS);
                clientConnections[i] = new ServerThread(socket, i);
                break;
            }
        }
    }
}
