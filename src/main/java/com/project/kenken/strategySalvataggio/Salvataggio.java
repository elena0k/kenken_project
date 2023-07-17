package com.project.kenken.strategySalvataggio;

import com.project.kenken.gestore_GUI.GrigliaGUI;

import javax.swing.*;
import java.io.File;

public interface Salvataggio {

    default File impostaFileSalvataggio(){

        File fileDiSalvataggio=null;
        JFileChooser jfc = new JFileChooser();
        try {
            if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                fileDiSalvataggio = jfc.getSelectedFile();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return fileDiSalvataggio;
    }


    void salva(GrigliaGUI grigliaGUI);

    void salvaConNome(GrigliaGUI grigliaGUI);

    GrigliaGUI apri();
}
