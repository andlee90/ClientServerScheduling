package Server;

import javax.swing.*;

/**
 * Handles all schedule editor activities.
 */
class ServerUserEditor
{
    private JFrame parentFrame;

    ServerUserEditor(JFrame pf)
    {
        this.parentFrame = pf;
        createFrame();
    }

    /**
     * Builds frame for user editor interface.
     */
    private void createFrame()
    {
        JFrame serverUserEditorFrame = new ServerUserEditorFrame(parentFrame);
        serverUserEditorFrame.setTitle("User Editor");
        serverUserEditorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        serverUserEditorFrame.setVisible(true);
        serverUserEditorFrame.setResizable(false);
    }
}
