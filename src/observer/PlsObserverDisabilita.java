package observer;

import javax.swing.*;

public class PlsObserverDisabilita implements Observer{

    private JButton pls;

    public PlsObserverDisabilita(JButton pls){ this.pls=pls; }
    @Override
    public void update() {
        pls.setEnabled(false);
    }
}
