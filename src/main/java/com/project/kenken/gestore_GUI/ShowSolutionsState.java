package com.project.kenken.gestore_GUI;

import com.project.kenken.state.State;

public class ShowSolutionsState implements State {

    private static ShowSolutionsState INSTANCE = null;

    private ShowSolutionsState() {
    }

    public static synchronized ShowSolutionsState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShowSolutionsState();
        }
        return INSTANCE;
    }

    @Override
    public void intercettaClick(GrigliaGUI griglia) {

        griglia.costruisciMenuShow();
        griglia.abilitaTextField(false);
    }
}
