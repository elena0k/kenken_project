package decoratorCelle;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class CellaIndexDecorator extends AbstractCella{

    private String stringConstraint;
    private JTextField vincolo;



    public CellaIndexDecorator(ComponentCella component) {
        super(component);
    }

    public void CellaIndexDecorator() {
        super.impostaTextField();
        vincolo.setText(stringConstraint);
        vincolo.setEnabled(false);
        setTextLayoutConfig();
    }

    public void setVincolo(int res, String op) {
        this.stringConstraint= "res"+op;
    }

    public JTextField getText() {
        return wrapped.getText();
    }

    public void setTextLayoutConfig()
    {
        setLayout(new BorderLayout());
        vincolo =new JTextField();
        add(vincolo, BorderLayout.NORTH);
    }


    public void setFont(int n) {
        if(n>4)
            vincolo.setFont(new Font("Courier New", Font.PLAIN, 5));
        else
            vincolo.setFont(new Font("Courier New", Font.PLAIN, 10));
    }

    public void setEnabled(boolean bool)
    {
        vincolo.setEnabled(bool);
    }

    public void mySetBorder(Border border)
    {
        vincolo.setBorder(border);
    }

    public void setMouseAdapter(MouseAdapter mouseAdapter) {
        vincolo.addMouseListener(mouseAdapter);
    }


}
