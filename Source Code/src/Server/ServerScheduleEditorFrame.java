package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by andrewsmith on 4/2/17.
 */
public class ServerScheduleEditorFrame extends JFrame
{
    private static final int FRAME_WIDTH = 725;
    private static final int FRAME_HEIGHT = 375;

    private JComboBox userList;

    ServerScheduleEditorFrame()
    {
        createButtons();
        createComboBox();
        createPanels();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private void createPanels()
    {
        JPanel container = new JPanel();
        JPanel userSelectPanel = new JPanel();

        userSelectPanel.setLayout(new FlowLayout());
        userSelectPanel.add(userList);

        container.setLayout(new FlowLayout());
        container.add(userSelectPanel);

        this.add(container);
    }

    private void createComboBox()
    {
        String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };

        userList = new JComboBox(petStrings);
        userList.setSelectedIndex(4);
        userList.addActionListener(new ComboBoxListener());
    }

    private void createButtons()
    {

    }

    /**
     * Creates a new Sever object and hides the main user interface.
     */
    class ComboBoxListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            //setVisible(false); // Hide Server GUI
            //parentFrame.setVisible(true); // Show Main GUI
        }
    }

}
