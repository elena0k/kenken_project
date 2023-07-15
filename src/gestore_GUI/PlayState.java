package gestore_GUI;

import gestore_GUI.GrigliaGUI;
import state.State;

public class PlayState implements State {


    @Override
    public void intercettaClick(GrigliaGUI griglia) {
       griglia.settaPulsantiPlay();
    }


}