package Server;

import javax.swing.*;

/**
 * Handles all schedule editor activities.
 */
class ServerScheduleEditor
{
    private JFrame parentFrame;

    ServerScheduleEditor(JFrame pf)
    {
        this.parentFrame = pf;
        createFrame();
    }

    /**
     * Builds frame for schedule editor interface.
     */
    private void createFrame()
    {
        JFrame serverScheduleEditorFrame = new ServerScheduleEditorFrame(parentFrame);
        serverScheduleEditorFrame.setTitle("Schedule Editor");
        serverScheduleEditorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverScheduleEditorFrame.setVisible(true);
        serverScheduleEditorFrame.setResizable(false);
    }
}
