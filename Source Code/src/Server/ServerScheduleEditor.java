package Server;

import javax.swing.*;

/**
 * Handles all schedule editor activities.
 */
class ServerScheduleEditor
{
    ServerScheduleEditor()
    {
        createFrame();
    }

    /**
     * Builds frame for schedule editor interface.
     */
    private void createFrame()
    {
        JFrame serverScheduleEditorFrame = new ServerScheduleEditorFrame();
        serverScheduleEditorFrame.setTitle("Schedule Editor");
        serverScheduleEditorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverScheduleEditorFrame.setVisible(true);
        serverScheduleEditorFrame.setResizable(false);
    }
}
