package strategySalvataggio;

import gestore_GUI.GrigliaGUI;

public interface Salvataggio {
    void salva(GrigliaGUI grigliaGUI);

    void salvaConNome(GrigliaGUI grigliaGUI);

    GrigliaGUI apri();
}
