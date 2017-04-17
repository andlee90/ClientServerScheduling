package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Handles creation and management of all user editor user interface objects.
 */
class ServerUserEditorFrame extends JFrame
{
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 200;

    private JFrame parentFrame;
    private JFrame frame = this;
    private ServerUserEditorFrame serverUserEditorFrame = this;

    private JButton addUserButton;
    private JButton removeUserButton;
    private JButton closeButton;

    private JComboBox removeUserListBox;

    private JScrollPane scrollPane;
    private JTextArea textArea;

    ServerUserEditorFrame(JFrame pf)
    {
        this.parentFrame = pf;
        createButtons();
        createComboBoxes();
        createTextArea();
        createPanels();
        updateTextAreaAndComboBox();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
    }

    private void createButtons()
    {
        addUserButton = new JButton("Add User");
        ActionListener addUserButtonListener = new AddUserButtonListener();
        addUserButton.addActionListener(addUserButtonListener);
        addUserButton.setEnabled(true);

        removeUserButton = new JButton("Remove User");
        ActionListener removeUserButtonListener = new RemoveUserButtonListener();
        removeUserButton.addActionListener(removeUserButtonListener);
        removeUserButton.setEnabled(true);

        closeButton = new JButton("Close");
        ActionListener closeButtonListener = new CloseButtonListener();
        closeButton.addActionListener(closeButtonListener);
        closeButton.setEnabled(true);
    }

    private void createComboBoxes()
    {
        ArrayList<String> usernameList = ServerDB.selectAllUsernames();
        usernameList.add(0, "Select User");
        String[] usernameArr = new String[usernameList.size()];
        usernameArr = usernameList.toArray(usernameArr);
        removeUserListBox = new JComboBox(usernameArr);
        removeUserListBox.setSelectedIndex(0);
    }

    private void createTextArea()
    {
        textArea = new JTextArea(10, 15);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
    }

    private void createPanels()
    {
        JPanel container = new JPanel();
        JPanel textAreaPanel = new JPanel();
        JPanel componentPanel = new JPanel();
        JPanel comboboxPanel = new JPanel();
        JPanel removeUserButtonPanel = new JPanel();
        JPanel otherButtonPanel = new JPanel();

        textAreaPanel.setLayout(new BoxLayout(textAreaPanel, BoxLayout.PAGE_AXIS));
        textAreaPanel.add(scrollPane);

        comboboxPanel.setLayout(new FlowLayout());
        comboboxPanel.add(removeUserListBox);

        removeUserButtonPanel.setLayout(new FlowLayout());
        removeUserButtonPanel.add(removeUserButton);

        otherButtonPanel.setLayout(new FlowLayout());
        otherButtonPanel.add(addUserButton);
        otherButtonPanel.add(closeButton);

        componentPanel.setLayout(new GridLayout(4, 1));
        componentPanel.add(comboboxPanel);
        componentPanel.add(removeUserButtonPanel);
        componentPanel.add(new JLabel(""));
        componentPanel.add(otherButtonPanel);

        container.setLayout(new FlowLayout());
        container.add(textAreaPanel);
        container.add(componentPanel);

        this.add(container);
    }

    void updateTextAreaAndComboBox()
    {
        ArrayList<String> usernameList = ServerDB.selectAllUsernames();
        usernameList.add(0, "Select User");
        String[] usernameArr = new String[usernameList.size()];
        usernameList.toArray(usernameArr);
        DefaultComboBoxModel usernameModel = new DefaultComboBoxModel(usernameArr);
        removeUserListBox.setModel(usernameModel);
        removeUserListBox.updateUI();

        ArrayList<String> users = ServerDB.selectAllUsernames();
        textArea.setText("");
        for(String user:users)
        {
            textArea.append(user + "\n");
        }
    }

    /**
     * Listener for the add user button. Adds the selected user to the user's table.
     */
    class AddUserButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            setEnabled(false); // Hide Server GUI
            new ServerAddNewUser(frame, serverUserEditorFrame); // Create new user editor
        }
    }

    /**
     * Listener for the delete user button. Removes the selected user from the users table.
     */
    class RemoveUserButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            String selectedUser = String.valueOf(removeUserListBox.getItemAt(removeUserListBox.getSelectedIndex()));
            if(!selectedUser.equals("Select User"))
            {
                int selectedUserId = ServerDB.selectUserIdByUsername(selectedUser);
                ServerDB.deleteUser(selectedUserId);
                updateTextAreaAndComboBox();
                System.out.println("> [" + Server.getDate() + "] Server@" + Server.getHost() + " removed user '"
                        + selectedUser + "'");
            }
        }
    }

    /**
     * Listener for the close button. Destroys the user editor GUI and shows the Server GUI.
     */
    class CloseButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            dispose();
            parentFrame.setEnabled(true);
        }
    }
}
