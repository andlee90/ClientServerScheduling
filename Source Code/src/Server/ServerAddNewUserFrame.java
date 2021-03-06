package Server;

import Main.MainErrorMessageFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Handles creation and management of all add new user interface objects.
 */
class ServerAddNewUserFrame extends JFrame
{
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 200;

    private JFrame parentFrame;
    private JFrame frame;
    private ServerUserEditorFrame serverUserEditorFrame;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JComboBox<String> isAdminListBox;
    private JButton addButton;
    private JButton cancelButton;

    ServerAddNewUserFrame(JFrame pf, ServerUserEditorFrame suef)
    {
        this.parentFrame = pf;
        this.serverUserEditorFrame = suef;
        createTextFields();
        createButtons();
        createComboBox();
        createPanels();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        frame = this;
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

    private void createComboBox()
    {
        ArrayList<String> optionList = new ArrayList<>();
        optionList.add(0, "Select");
        optionList.add(1, "Yes");
        optionList.add(2, "No");
        String[] optionArr = new String[optionList.size()];
        optionArr = optionList.toArray(optionArr);
        isAdminListBox = new JComboBox<>(optionArr);
        isAdminListBox.setSelectedIndex(0);
    }

    private void createPanels()
    {
        JPanel container = new JPanel();
        JPanel userNamePanel = new JPanel();
        JPanel passwordPanel = new JPanel();
        JPanel lastNamePanel = new JPanel();
        JPanel firstNamePanel = new JPanel();
        JPanel isAdminPanel = new JPanel();
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

        isAdminPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        isAdminPanel.add(new JLabel("Administrator?"));
        isAdminPanel.add(isAdminListBox);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        container.setLayout(new GridLayout(4,2));
        container.add(userNamePanel);
        container.add(passwordPanel);
        container.add(firstNamePanel);
        container.add(lastNamePanel);
        container.add(isAdminPanel);
        container.add(new JLabel(""));
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
           try
           {
               if(usernameField.getText().equals("") || passwordField.getText().equals("") ||
                       firstNameField.getText().equals("") || lastNameField.getText().equals("") ||
                       String.valueOf(isAdminListBox.getItemAt(isAdminListBox.getSelectedIndex())).equals("Select"))
               {
                   throw new IOException("Please fill out all required fields");
               }

               int isAdmin = 0;
               if (Objects.equals(String.valueOf(isAdminListBox.getItemAt(isAdminListBox.getSelectedIndex())), "Yes"))
               {
                   isAdmin = 1;
               }

               ServerDB.insertUser(usernameField.getText(), passwordField.getText(), lastNameField.getText(),
                       firstNameField.getText(), isAdmin); // Insert new user

                dispose();
                parentFrame.setEnabled(true);
                serverUserEditorFrame.updateTextAreaAndComboBox();
                System.out.println("> [" + Server.getDate() + "] Server@" + Server.getHost() + " added user '"
                        + usernameField.getText() + "'");
            }
            catch(Exception e)
            {
                frame.setEnabled(false);
                new MainErrorMessageFrame(e.getLocalizedMessage());
                frame.setEnabled(true);
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
            dispose(); // Destroy current frame
            parentFrame.setEnabled(true); // Enable Server frame
        }
    }
}
