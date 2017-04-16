package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles creation and management of all add new user interface objects.
 */
class ServerAddNewUserFrame extends JFrame
{
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 150;

    private JFrame parentFrame;
    private ServerUserEditorFrame serverUserEditorFrame;

    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField lastNameField;
    private JTextField firstNameField;

    private JButton addButton;
    private JButton cancelButton;

    ServerAddNewUserFrame(JFrame pf, ServerUserEditorFrame suef)
    {
        this.parentFrame = pf;
        this.serverUserEditorFrame = suef;
        createTextFields();
        createButtons();
        createPanels();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private void createTextFields()
    {
        usernameField = new JTextField(8);
        passwordField = new JTextField(8);
        lastNameField = new JTextField(8);
        firstNameField = new JTextField(8);
    }

    private void createButtons()
    {
        addButton = new JButton("Add");
        AddButtonListener addButtonListener = new AddButtonListener();
        addButton.addActionListener(addButtonListener);
        addButton.setEnabled(true);

        cancelButton = new JButton("Cancel");
        CancelButtonListener cancelButtonListener = new CancelButtonListener();
        cancelButton.addActionListener(cancelButtonListener);
        cancelButton.setEnabled(true);
    }

    private void createPanels()
    {
        JPanel container = new JPanel();
        JPanel userNamePanel = new JPanel();
        JPanel passwordPanel = new JPanel();
        JPanel lastNamePanel = new JPanel();
        JPanel firstNamePanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        userNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        userNamePanel.add(new JLabel("Username: "));
        userNamePanel.add(usernameField);

        passwordPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.add(new JLabel("Password: "));
        passwordPanel.add(passwordField);

        lastNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        lastNamePanel.add(new JLabel("Last Name: "));
        lastNamePanel.add(lastNameField);

        firstNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        firstNamePanel.add(new JLabel("First Name: "));
        firstNamePanel.add(firstNameField);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        container.setLayout(new GridLayout(3,2));
        container.add(userNamePanel);
        container.add(passwordPanel);
        container.add(firstNamePanel);
        container.add(lastNamePanel);
        container.add(new JLabel(""));
        container.add(buttonPanel);

        this.add(container);
    }

    /**
     * Listener for the add user button. Adds the selected user to the user's table.
     */
    class AddButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            if(usernameField.getText() != null &&
                    passwordField.getText() != null &&
                    firstNameField.getText() != null &&
                    lastNameField.getText() != null)
            {
                ServerDB.insertUser(usernameField.getText(),
                        passwordField.getText(),
                        lastNameField.getText(),
                        firstNameField.getText());

                dispose();
                parentFrame.setVisible(true);
                serverUserEditorFrame.updateTextAreaAndComboBox();
                System.out.println("> [" + Server.getDate() + "] Server@" + Server.getHost() + " added user '"
                        + usernameField.getText());
            }
        }
    }

    /**
     * Listener for the close button. Destroys the user editor GUI and shows the Server GUI.
     */
    class CancelButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            dispose();
            parentFrame.setEnabled(true);
        }
    }
}
