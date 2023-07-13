package decoratorCelle;

import javax.swing.*;

public abstract class AbstractDecoratorCella extends JPanel implements ICella {

    private final ICella wrapped;
    private int n;

    AbstractDecoratorCella(ICella wrapped)
    {
        this.wrapped=wrapped;
        this.n=n;
    }

    public JTextField getText()
    {
        return wrapped.getText();
    }

    public void setText()
    {
        wrapped.getText();
    }

    @Override
    public void setFont(int n) {
        wrapped.setFont(n);
    }
}
