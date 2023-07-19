package com.project.kenken.observer;

import com.project.kenken.gestore_GUI.GrigliaGUI;
import com.project.kenken.gestore_GUI.PlayState;

import javax.swing.*;

public class StartObserver implements Observer {

    private JButton plsStart;
    GrigliaGUI subject;

    public StartObserver(JButton pls, GrigliaGUI subject) {
        this.plsStart = pls;
        this.subject = subject;
    }

    @Override
    public void update(boolean running) {
        if(!running)
            plsStart.setEnabled(subject.getState() instanceof PlayState);
        else {
            plsStart.setEnabled(false);
            subject.abilitaTextField(true);
        }
    }
}
