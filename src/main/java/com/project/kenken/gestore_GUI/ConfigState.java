package com.project.kenken.gestore_GUI;

import com.project.kenken.state.State;

public class ConfigState implements State {

    private static ConfigState INSTANCE = null;

    private ConfigState() {
    }

    public static synchronized ConfigState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigState();
        }
        return INSTANCE;
    }

    @Override
    public void intercettaClick(GrigliaGUI griglia) {
        griglia.impostaGruppi();
        griglia.costruisciMenuConfig();
    }
}
