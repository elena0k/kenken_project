package com.project.kenken.gestore_GUI;

import com.project.kenken.state.State;

public class PlayState implements State {

    private static PlayState INSTANCE = null;

    private PlayState() {
    }

    public static synchronized PlayState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayState();
        }
        return INSTANCE;
    }

    @Override
    public void intercettaClick(GrigliaGUI griglia) {

        griglia.removeOldMenu();
        griglia.costruisciMenuPlay();
        griglia.avviaSoluzione();
    }


}