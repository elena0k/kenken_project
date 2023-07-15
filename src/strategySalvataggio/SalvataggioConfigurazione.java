package strategySalvataggio;

import gestore_GUI.GrigliaGUI;
import gestore_GUI.PlayState;
import componenti.Gruppo;

import javax.swing.*;
import java.io.*;
import java.util.List;

public class SalvataggioConfigurazione implements Salvataggio{

    private File fileDiSalvataggio;

    @Override
    public void salvaConNome(GrigliaGUI grigliaGUI) {
        JFileChooser jfc = new JFileChooser();
        try
        {
            if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                fileDiSalvataggio = jfc.getSelectedFile();
            }
            if (fileDiSalvataggio != null)
            {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileDiSalvataggio));
                oos.writeObject(grigliaGUI.getKenken().getGroups());
                oos.close();
            }
            else
                JOptionPane.showMessageDialog(null, "Salvataggio non avvenuto");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }

    }

    @Override
    public void salva(GrigliaGUI grigliaGUI) {
        JFileChooser jfc = new JFileChooser();
        try
        {
            if (fileDiSalvataggio != null)
            {
                int ans = JOptionPane.showConfirmDialog(null,
                        "Vuoi sovrascrivere " + fileDiSalvataggio.getAbsolutePath() + "???");
                if (ans == 0)
                {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileDiSalvataggio));
                    oos.writeObject(grigliaGUI.getKenken().getGroups());
                    oos.close();
                }
                else
                    JOptionPane.showMessageDialog(null, "Salvataggio non avvenuto.");
            }
            else
            {
                if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
                    fileDiSalvataggio = jfc.getSelectedFile();
                if (fileDiSalvataggio != null)
                {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileDiSalvataggio));
                    oos.writeObject(grigliaGUI.getKenken().getGroups());
                    oos.close();
                }
                else
                    JOptionPane.showMessageDialog(null, "Salvataggio non avvenuto");
            }
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }

    @Override
    public GrigliaGUI apri() {
        //TODO risolvere kenken anche quando apro da file
        GrigliaGUI grigliaGUI=null;
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            ObjectInputStream ois = null;

            if (!jfc.getSelectedFile().exists())
                JOptionPane.showMessageDialog(null,
                        "Attenzione, stai provandoo ad aprire un file inesistente!!!");
            else
            {
                File fileDiApertura = jfc.getSelectedFile();
                try
                {
                    ois = new ObjectInputStream(new FileInputStream(fileDiApertura));
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
                try
                {
                    List<Gruppo> listaGruppi=(List<Gruppo>) ois.readObject();
                    ois.close();
                    grigliaGUI= new GrigliaGUI(4); //TODO dare dimensione corretta
                    for (int i = 0; i < listaGruppi.size(); i++)
                        grigliaGUI.getKenken().addGroup(listaGruppi.get(i));
                    grigliaGUI.redraw();
                    grigliaGUI.setState(new PlayState());
                }
                catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(null,
                            "ATTENZIONE, impossibile leggere un kenken da questo file.");
                    e1.printStackTrace();
                }
            }
        }
        return grigliaGUI;
    }

}

