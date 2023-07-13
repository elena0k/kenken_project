package decoratorCelle;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.MouseAdapter;

public class AbstractCella extends JPanel implements ComponentCella {

    protected ComponentCella wrapped;

    public AbstractCella(ComponentCella component) {
        this.wrapped = component;
    }
    @Override
    public void impostaTextField() {

        wrapped.impostaTextField();
    }

    public JTextField getText()
    {
        return wrapped.getText();
    }

    public void setMouseAdapter(MouseAdapter mouseAdapter) {
        wrapped.getText().addMouseListener(mouseAdapter);
    }

    public void setEnabled(boolean bool)
    {
        wrapped.getText().setEnabled(bool);
    }

    public void mySetBorder(Border border)
    {
        wrapped.getText().setBorder(border);
    }

}
