package Main;

//import Server.Server;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by andrewsmith on 2/4/17.
 */

public class MainFrame extends JFrame
{
    private JButton createServerButton;
    private JButton joinServerButton;

    private JTextField cPortField;
    private JTextField sPortField;
    private JTextField hostField;

    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 200;


    public MainFrame()
    {
        createButtons();
        createTextFields();
        createPanel();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private void createButtons()
    {
        ActionListener listener;

        createServerButton = new JButton("Create New Server");
        listener = new CreateServerListener();
        createServerButton.addActionListener(listener);
        createServerButton.setEnabled(false);

        joinServerButton = new JButton("Join Server");
        listener = new JoinServerListener();
        joinServerButton.addActionListener(listener);
        joinServerButton.setEnabled(false);
    }

    private void createPanel()
    {
        final int rows = 4;
        final int cols = 2;
        final JLabel emptyLabel = new JLabel("");
        emptyLabel.setBorder(BorderFactory.createMatteBorder(0, 1,0,0, Color.GRAY));

        JPanel container = new JPanel();
        JPanel serverFieldPanel = new JPanel();
        JPanel serverButtonPanel = new JPanel();
        JPanel clientPortFieldPanel = new JPanel();
        JPanel clientHostFieldPanel = new JPanel();
        JPanel clientButtonPanel = new JPanel();

        serverFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        serverFieldPanel.add(new JLabel("Port: "));
        serverFieldPanel.add(sPortField);

        serverButtonPanel.setLayout(new FlowLayout());
        serverButtonPanel.add(createServerButton);

        clientHostFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        clientHostFieldPanel.add(new JLabel("Host: "));
        clientHostFieldPanel.add(hostField);
        clientHostFieldPanel.setBorder(BorderFactory.createMatteBorder(0, 1,0,0, Color.GRAY));

        clientPortFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        clientPortFieldPanel.add(new JLabel("Port: "));
        clientPortFieldPanel.add(cPortField);
        clientPortFieldPanel.setBorder(BorderFactory.createMatteBorder(0, 1,0,0, Color.GRAY));

        clientButtonPanel.setLayout(new FlowLayout());
        clientButtonPanel.add(joinServerButton);
        clientButtonPanel.setBorder(BorderFactory.createMatteBorder(0, 1,0,0, Color.GRAY));

        container.setLayout(new GridLayout(rows, cols));
        container.add(new JLabel(""));
        container.add(clientHostFieldPanel);
        container.add(serverFieldPanel);
        container.add(clientPortFieldPanel);
        container.add(new JLabel(""));
        container.add(emptyLabel);
        container.add(serverButtonPanel);
        container.add(clientButtonPanel);

        this.add(container);
    }

    private void createTextFields()
    {
        cPortField = new JTextField(4);
        sPortField = new JTextField(4);
        hostField = new JTextField(10);
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