package Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Created by Tim on 4/3/2017.
 */
public class Authenticate extends JFrame
{
    private JLabel usernLbl;
    private JLabel pwLbl;
    private JButton blogin;
    private JButton closeBtn;
    private JPanel panel;
    private JTextField txuser;
    private JPasswordField pass;
    private JFrame parentFrame;
    private JFrame frame = this;


    public void createLabels()
    {
        usernLbl = new JLabel("Username: ");
        pwLbl = new JLabel("Password: ");
        usernLbl.setBounds(70,10,150,20);
        pwLbl.setBounds(70,55,150,20);
    }

    public void createButtons()
    {
        blogin = new JButton("Login");
        closeBtn = new JButton("Return");
        blogin.setBounds(50,110,80,20);
        closeBtn.setBounds(150,110,80,20);
    }
    public void createPanel()
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
    public void createTextField()
    {
        txuser = new JTextField(15);
        pass = new JPasswordField(15);
        txuser.setBounds(70,30,150,20);
        pass.setBounds(70,75,150,20);
    }

    public Authenticate(String pn, String hn, JFrame pf){

        super("Tutor Authentication");
        setSize(300,200);
        setLocation(500,280);
        parentFrame=pf;
        createButtons();
        createLabels();
        createTextField();
        createPanel();

        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        actionlogin(pn,hn,pf);
        actionReturn();
    }

    public void actionReturn(){
        closeBtn.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent ae)
            {
                setVisible(false);
                parentFrame.setVisible(true);
            }

        });
    }

    public void actionlogin(String pn, String hn, JFrame pf){
        blogin.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
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

            }
        });
    }
}
