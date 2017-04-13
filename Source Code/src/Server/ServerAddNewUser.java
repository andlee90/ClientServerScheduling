package Server;

import javax.swing.*;

/**
 * Handles all user adding activities.
 */
public class ServerAddNewUser
{
    private JFrame parentFrame;
    private ServerUserEditorFrame serverUserEditorFrame;

    ServerAddNewUser(JFrame pf, ServerUserEditorFrame suef)
    {
        this.parentFrame = pf;
        this.serverUserEditorFrame = suef;
        createFrame();
    }

    /**
     * Builds frame for user editor interface.
     */
    private void createFrame()
    {
        JFrame serverAddUserFrame = new ServerAddNewUserFrame(parentFrame, serverUserEditorFrame);
        serverAddUserFrame.setTitle("Add New User");
        serverAddUserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverAddUserFrame.setVisible(true);
        serverAddUserFrame.setResizable(false);
    }
}
