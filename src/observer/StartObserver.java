package observer;

import gestore_GUI.GrigliaGUI;
import gestore_GUI.PlayState;

import javax.swing.*;

public class StartObserver implements Observer {

    private JButton plsStart;
    GrigliaGUI subject;

    public StartObserver(JButton pls, GrigliaGUI subject) {
        this.plsStart = pls;
        this.subject = subject;
    }

    @Override
    public void update() {
        plsStart.setEnabled(subject.getState() instanceof PlayState);
    }
}
