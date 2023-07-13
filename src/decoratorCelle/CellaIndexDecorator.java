package decoratorCelle;

import javax.swing.*;
import java.awt.*;
import java.sql.Wrapper;

public class CellaIndexDecorator extends AbstractDecoratorCella{

    private JTextField text;
    public CellaIndexDecorator(ICella wrapped)
    {
        super(wrapped);
    }

    public void setText()
    {
        setLayout(new BorderLayout());
        text=new JTextField();

        add(text, BorderLayout.NORTH);
    }

    @Override
    public void setFont(int n) {
        if(n>4)
            text.setFont(new Font("Courier New", Font.PLAIN, 5));
        else
            text.setFont(new Font("Courier New", Font.PLAIN, 10));
    }


}
