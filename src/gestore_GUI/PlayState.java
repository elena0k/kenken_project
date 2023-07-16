package gestore_GUI;

import state.State;

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

        griglia.settaPulsantiPlay();
        griglia.costruisciMenuPlay();
    }


}