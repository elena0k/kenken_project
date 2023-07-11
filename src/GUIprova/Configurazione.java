package GUIprova;

public class Configurazione implements State {


    @Override
    public void terminaConfigurazione(Finestra kenken) {
        kenken.setState(new Play());
    }

    @Override
    public void resetGioco(Finestra kenken) {
        kenken.setState(new Configurazione());

    }

    @Override
    public void terminaGioco(Finestra kenken) {
        System.out.println("Necessaria terminazione della configurazione.");
    }
}
