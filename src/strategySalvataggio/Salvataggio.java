package strategySalvataggio;

import GUIprova.GrigliaGUI;
import GUIprova.KenkenGUI;

public interface Salvataggio {
    void salva(GrigliaGUI grigliaGUI);
    void salvaConNome(GrigliaGUI grigliaGUI);
    GrigliaGUI apri();
}
