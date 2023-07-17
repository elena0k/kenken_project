package com.project.kenken.strategySalvataggio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.kenken.componenti.Gruppo;
import com.project.kenken.gestore_GUI.GrigliaGUI;
import com.project.kenken.gestore_GUI.KenkenGUI;
import com.project.kenken.risolutore.KenkenGrid;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SalvataggioJSon implements Salvataggio{

    private File fileDiSalvataggio;
    ObjectMapper mapper= new ObjectMapper();
    SimpleModule celleModule = new SimpleModule();

    @Override
    public void salva(GrigliaGUI grigliaGUI) {
        fileDiSalvataggio=impostaFileSalvataggio();
        if (fileDiSalvataggio != null) {
            scriviOggetto(grigliaGUI);
        } else
            JOptionPane.showMessageDialog(null, "Salvataggio non avvenuto");
    }

    private void scriviOggetto(GrigliaGUI grigliaGUI) {
        try{
            ObjectNode node= mapper.createObjectNode();
            node.put("dimensione",grigliaGUI.getN());
            node.put("max soluzioni",grigliaGUI.getKenken().getListaSoluzioni().size());
            node.set("lista gruppi",mapper.convertValue(grigliaGUI.getKenken().getGroups(), JsonNode.class));
            node.set("valori inseriti", mapper.convertValue(grigliaGUI.getMatriceScelte(),JsonNode.class));
            mapper.writeValue(fileDiSalvataggio,node);

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,
                    "ATTENZIONE, impossibile salvare un kenken su questo file.");
            e.printStackTrace();
        }
    }

    @Override
    public void salvaConNome(GrigliaGUI grigliaGUI) {

    }

    @Override
    public GrigliaGUI apri() {
        fileDiSalvataggio=impostaFileSalvataggio();
        celleModule.addDeserializer(List.class, new CelleDeserializer());
        mapper.registerModule(celleModule);
        try {
            JsonNode node= mapper.readValue(fileDiSalvataggio,JsonNode.class);
            int dim= node.get("dimensione").asInt();
            int maxSol= node.get("max soluzioni").asInt();
            List<Gruppo> gruppi= mapper.convertValue(node.get("lista gruppi"),new TypeReference<>() {});
            int[][] matriceScelte= mapper.convertValue(node.get("valori inseriti"),int[][].class);
            KenkenGrid kenken= new KenkenGrid(gruppi,dim,maxSol);
            return new GrigliaGUI(kenken,matriceScelte,maxSol);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "ATTENZIONE, file malformato!!!.");
            e.printStackTrace();
        }

        return null;
    }
}
