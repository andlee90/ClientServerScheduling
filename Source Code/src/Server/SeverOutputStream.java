package Server;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class SeverOutputStream extends OutputStream
{
    private JTextArea textArea;

    SeverOutputStream(JTextArea textArea)
    {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException
    {
        textArea.append(String.valueOf((char)b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
