package com.project.kenken.strategySalvataggio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.kenken.componenti.Gruppo;
import com.project.kenken.gestore_GUI.GrigliaGUI;
import com.project.kenken.risolutore.KenkenGrid;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class SalvataggioJSon implements Salvataggio{

    private File fileDiSalvataggio;
    ObjectMapper mapper= new ObjectMapper();

    @Override
    public void salvaGame(GrigliaGUI grigliaGUI) {
        fileDiSalvataggio=impostaFileSalvataggio();
        if (fileDiSalvataggio != null) {
            scriviOggetto(grigliaGUI,false);
        } else
            JOptionPane.showMessageDialog(null, "Salvataggio non avvenuto");
    }

    private void scriviOggetto(GrigliaGUI grigliaGUI,boolean isConfig) {
        try{
            ObjectNode node= mapper.createObjectNode();
            node.put("dimensione",grigliaGUI.getN());
            node.put("max soluzioni",grigliaGUI.getKenken().getListaSoluzioni().size());
            node.set("lista gruppi",mapper.convertValue(grigliaGUI.getKenken().getGroups(), JsonNode.class));
            int[][] matVuota= new int[grigliaGUI.getN()][grigliaGUI.getN()];

            node.set("valori inseriti", mapper.convertValue(!isConfig? grigliaGUI.getMatriceScelte():matVuota,JsonNode.class));
            mapper.writeValue(fileDiSalvataggio,node);

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,
                    "ATTENZIONE, impossibile salvare un kenken su questo file.");
            e.printStackTrace();
        }
    }

    public void salvaConfig(GrigliaGUI grigliaGUI) {
        fileDiSalvataggio=impostaFileSalvataggio();
        if (fileDiSalvataggio != null) {
            scriviOggetto(grigliaGUI,true);
        } else
            JOptionPane.showMessageDialog(null, "Salvataggio non avvenuto");

    }

    @Override
    public GrigliaGUI apri() {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            ObjectInputStream ois = null;

            if (!jfc.getSelectedFile().exists())
                JOptionPane.showMessageDialog(null,
                        "Attenzione, stai provandoo ad aprire un file inesistente!!!");
            else {
                fileDiSalvataggio = jfc.getSelectedFile();
                try {
                    JsonNode node = mapper.readValue(fileDiSalvataggio, JsonNode.class);
                    int dim = node.get("dimensione").asInt();
                    int maxSol = node.get("max soluzioni").asInt();
                    List<Gruppo> gruppi = mapper.convertValue(node.get("lista gruppi"), new TypeReference<>() {
                    });
                    int[][] matriceScelte = mapper.convertValue(node.get("valori inseriti"), int[][].class);
                    KenkenGrid kenken = new KenkenGrid(gruppi, dim, maxSol);
                    return new GrigliaGUI(kenken, matriceScelte, maxSol);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "ATTENZIONE, file malformato!!!.");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
