package Main;

//import Server.Server;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by andrewsmith on 2/4/17.
 */

public class MainFrame extends JFrame
{
    private JTextArea displayServersArea;
    private JButton createServerButton;
    private JButton joinServerButton;

    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 300;

    private static final int AREA_ROWS = 10;
    private static final int AREA_COLUMNS = 30;

    public MainFrame()
    {
        displayServersArea = new JTextArea(AREA_ROWS, AREA_COLUMNS);
        displayServersArea.setEditable(false);

        createButtons();
        createPanel();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private void createButtons()
    {
        ActionListener listener;

        createServerButton = new JButton("Create New Server");
        listener = new CreateServerListener();
        createServerButton.addActionListener(listener);

        joinServerButton = new JButton("Join Selected Server");
        listener = new JoinServerListener();
        joinServerButton.addActionListener(listener);
    }

    private void createPanel()
    {
        JPanel panel = new JPanel();

        JScrollPane scrollPane = new JScrollPane(displayServersArea);

        panel.add(scrollPane);
        //panel.setLayout(new BorderLayout(100, 100));
        panel.add(createServerButton);
        panel.add(joinServerButton);

        add(panel);
    }

    class CreateServerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            //Server server = new Server();

            //try {
            //    server.configure();
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}

        }
    }

    class JoinServerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {


        }
    }
}