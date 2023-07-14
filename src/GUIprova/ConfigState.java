package GUIprova;

public class ConfigState implements State {

    @Override
    public void intercettaClick(GrigliaGUI griglia) {
        griglia.impostaGruppi();
    }
}
