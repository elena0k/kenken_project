package com.project.kenken.strategySalvataggio;

import com.project.kenken.gestore_GUI.GrigliaGUI;

public interface Salvataggio {
    void salva(GrigliaGUI grigliaGUI);

    void salvaConNome(GrigliaGUI grigliaGUI);

    GrigliaGUI apri();
}
