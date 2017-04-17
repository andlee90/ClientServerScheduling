package Main;

import Server.Server;
import Client.Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
/**
 * Created by Tim on 4/16/2017.
 */
public class MainErrorMessageFrame
{
    JPanel message;
    JDialog dialog;
    JFrame frame;
    public MainErrorMessageFrame(String error)
    {

        frame = new JFrame();
        createPanel(error);
        createDialog();
        JOptionPane.showConfirmDialog(frame, message, "Error Message:", JOptionPane.CANCEL_OPTION);


    }
    private void createDialog()
    {
        dialog = new JDialog(frame, "Error Message:");
        dialog.setModal(true);
        dialog.setContentPane(message);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
    }

    private void createPanel(String error)
    {
        message = new JPanel();
        message.add(new JLabel(error));
    }
}
