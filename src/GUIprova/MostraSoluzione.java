package GUIprova;

public class MostraSoluzione implements State {

    @Override
    public void terminaConfigurazione(Finestra kenken) {
        System.out.println("Necessario reset della griglia.");
    }

    @Override
    public void resetGioco(Finestra kenken) {
        kenken.setState(new Configurazione());
    }

    @Override
    public void terminaGioco(Finestra kenken) {

    }
}
