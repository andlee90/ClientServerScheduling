package Server;

import javax.swing.*;
import java.awt.*;

/**
 * Created by andrewsmith on 2/4/17.
 */

class ServerFrame extends JFrame
{
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 400;

    JTextArea textArea;
    private ServerManager serverManager;

    ServerFrame(ServerManager sm)
    {
        this.serverManager = sm;

        createButtons();
        createTextArea();
        createPanel();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private void createPanel()
    {
        JPanel container = new JPanel();
        //JPanel textAreaPanel = new JPanel();
        //JPanel buttonPanel = new JPanel();

        //textAreaPanel.setLayout(new CardLayout());
        //textAreaPanel.add(new JLabel("Info: "));
        //textAreaPanel.add(textArea);

        container.setLayout(new FlowLayout(FlowLayout.CENTER));
        container.add(textArea);

        this.add(container);
    }

    private void createTextArea()
    {
        textArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
    }

    private void createButtons()
    {

    }
}