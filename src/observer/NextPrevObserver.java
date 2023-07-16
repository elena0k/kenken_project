package observer;

import gestore_GUI.GrigliaGUI;
import gestore_GUI.ShowSolutionsState;

import javax.swing.*;

public class NextPrevObserver implements Observer {

    private final JButton pls;
    GrigliaGUI subject;

    public NextPrevObserver(JButton pls, GrigliaGUI subject) {
        this.pls = pls;
        this.subject = subject;
    }

    @Override
    public void update() {
        pls.setEnabled(subject.getState() instanceof ShowSolutionsState);
    }
}
