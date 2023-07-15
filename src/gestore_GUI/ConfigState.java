package gestore_GUI;

import gestore_GUI.GrigliaGUI;
import state.State;

public class ConfigState implements State {

    @Override
    public void intercettaClick(GrigliaGUI griglia) {
        griglia.impostaGruppi();
        griglia.costruisciMenuConfig();
    }
}
