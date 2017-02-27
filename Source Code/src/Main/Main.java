package Main;

import javax.swing.*;

/**
 * Created by andrewsmith on 2/4/17.
 */

public class Main
{
    public static void main(String[] args)
    {
        JFrame frame = new MainFrame();
        frame.setTitle("Client/Server Scheduling");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
