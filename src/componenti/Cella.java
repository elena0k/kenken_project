package componenti;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class Cella extends JPanel {

    private JTextField text;
    private JTextField vincolo;

    public Cella() {

        setTextLayoutConfig();

    }

    public void setTextLayoutConfig()
    {
        text=new JTextField();
        setLayout(new BorderLayout());
        text.setHorizontalAlignment(JTextField.CENTER);
        add(text, BorderLayout.CENTER);
    }

    public void setVincolo(int num, String op)
    {
        vincolo=new JTextField();
        vincolo.setText(""+num+op);
        vincolo.setEnabled(false);
        add(vincolo,BorderLayout.NORTH);
        repaint();
    }

    public void cleanVincolo()
    {
        remove(vincolo);
        revalidate();
        repaint();
    }

    public boolean isCellaSemplice()
    {
        return vincolo==null;
    }


    public void setFont(int n) {
        if(n==6 || n==5)
            text.setFont(new Font("Courier New", Font.BOLD, 20));
        else if(n==3 || n==4)
            text.setFont(new Font("Courier New", Font.BOLD, 35));
    }


    public JTextField getText()
    {
        return text;
    }



    public void setEnabled(boolean bool)
    {
        text.setEnabled(bool);
    }

    public void mySetBorder(Border border)
    {
        setBorder(border);
    }

    public void setMouseAdapter(MouseAdapter mouseAdapter) {
        text.addMouseListener(mouseAdapter);
    }
}
