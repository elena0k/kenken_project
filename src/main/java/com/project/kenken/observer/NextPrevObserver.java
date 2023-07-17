package com.project.kenken.observer;

import com.project.kenken.gestore_GUI.GrigliaGUI;
import com.project.kenken.gestore_GUI.ShowSolutionsState;

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
