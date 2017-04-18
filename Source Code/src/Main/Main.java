package Main;

import javax.swing.*;

/**
 * Launches the application and creates the main frame.
 */
public class Main
{
    public static void main(String[] args)
    {
        JFrame frame = new MainFrame();
        frame.setTitle("Welcome to Tutor Scheduling");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
