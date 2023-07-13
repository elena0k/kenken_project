package decoratorCelle;

import javax.swing.*;
import java.awt.*;

public class Cella extends JPanel implements ICella {

    private JTextField text;

    public Cella() {
        setText();
    }

    public void setText()
    {
        setLayout(new BorderLayout());
        text=new JTextField();
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




}
