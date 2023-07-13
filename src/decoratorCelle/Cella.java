package decoratorCelle;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class Cella extends JPanel implements ICella {

    private JTextField text;

    public Cella() {

        setText();
    }

    public void setText()
    {
        System.out.println("dio banana 2");
        text=new JTextField();
        setLayout(new BorderLayout());
        text.setHorizontalAlignment(JTextField.CENTER);
        add(text, BorderLayout.CENTER);
    }

    @Override
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
        text.setBorder(border);
    }

    public void setMouseAdapter(MouseAdapter mouseAdapter) {
        text.addMouseListener(mouseAdapter);
    }
}
