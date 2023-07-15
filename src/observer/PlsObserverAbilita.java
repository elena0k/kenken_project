package observer;

import javax.swing.*;

public class PlsObserverAbilita implements Observer{

    private JButton pls;

    public PlsObserverAbilita(JButton pls){ this.pls=pls; }
    @Override
    public void update() {
        pls.setEnabled(true);
    }
}
