package observer;

import gestore_GUI.GrigliaGUI;
import gestore_GUI.PlayState;
import gestore_GUI.ShowSolutionsState;

import javax.swing.*;

public class NextPrevObserver implements Observer{

    private JButton pls;
    GrigliaGUI subject;

    public NextPrevObserver(JButton pls, GrigliaGUI subject){
        this.pls=pls;
        this.subject=subject;
    }
    @Override
    public void update() {
        if(subject.getState() instanceof ShowSolutionsState)
            pls.setEnabled(true);
        else
            pls.setEnabled(false);
    }
}
