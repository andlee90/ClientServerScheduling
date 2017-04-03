package Client;
import javax.swing.*;

/**
 * Created by Tim on 4/3/2017.
 */
public class ClientAuthenticate extends JFrame
{
    private JLabel usernLbl;
    private JLabel pwLbl;
    private JButton blogin;
    private JButton closeBtn;
    private JPanel panel;
    private JTextField txuser;
    private JPasswordField pass;
    private JFrame parentFrame;
    private JFrame frame;

    public ClientAuthenticate(String pn, String hn, JFrame pf)
    {

        super("Tutor Authentication");
        setSize(300,200);
        setLocation(500,280);
        parentFrame=pf;
        frame = this;
        createButtons();
        createLabels();
        createTextField();
        createPanel();

        getContentPane().add(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        actionlogin(pn,hn);
        actionReturn();
    }


    private void createLabels()
    {
        usernLbl = new JLabel("Username: ");
        pwLbl = new JLabel("Password: ");
        usernLbl.setBounds(70,10,150,20);
        pwLbl.setBounds(70,55,150,20);
    }

    private void createButtons()
    {
        blogin = new JButton("Login");
        closeBtn = new JButton("Return");
        blogin.setBounds(50,110,80,20);
        closeBtn.setBounds(150,110,80,20);
    }
    private void createPanel()
    {
        panel= new JPanel();
        panel.setLayout (null);
        panel.add(blogin);
        panel.add(closeBtn);
        panel.add(usernLbl);
        panel.add(txuser);
        panel.add(pwLbl);
        panel.add(pass);

    }
    private void createTextField()
    {
        txuser = new JTextField(15);
        pass = new JPasswordField(15);
        txuser.setBounds(70,30,150,20);
        pass.setBounds(70,75,150,20);
    }



    private void actionReturn()
    {
        closeBtn.addActionListener(ae -> {
            setVisible(false);
            parentFrame.setVisible(true);
        });
    }

    private void actionlogin(String pn, String hn)
    {
        blogin.addActionListener(ae -> {
            String puname = txuser.getText();
            String ppaswd = pass.getText();
            if(puname.equals("andlee") && ppaswd.equals("12345"))
            {
                setVisible(false);
                new Client(pn,hn,frame);
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Wrong Password / Username");
                txuser.setText("");
                pass.setText("");
                txuser.requestFocus();
            }
        });
    }
}
