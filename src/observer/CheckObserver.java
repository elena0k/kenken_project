package observer;

import gestore_GUI.GrigliaGUI;
import gestore_GUI.PlayState;

import javax.swing.*;

public class CheckObserver implements Observer{

    private JButton plsCheck;
    GrigliaGUI subject;

    public CheckObserver(JButton pls, GrigliaGUI subject){
        this.plsCheck =pls;
        this.subject=subject;
    }
    @Override
    public void update() {
        if(subject.getState() instanceof PlayState)
            plsCheck.setEnabled(true);
        else
            plsCheck.setEnabled(false);
    }
}
