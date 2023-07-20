package com.project.kenken.gestore_GUI;

import com.project.kenken.observer.CheckObserver;
import com.project.kenken.observer.NextPrevObserver;
import com.project.kenken.observer.StartObserver;
import com.project.kenken.risolutore.KenkenGrid;
import com.project.kenken.state.State;
import com.project.kenken.strategySalvataggio.Salvataggio;
import com.project.kenken.strategySalvataggio.SalvataggioJSon;
import com.project.kenken.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Finestra extends JFrame {

    private int n;
    private GrigliaGUI grigliaGUI;
    private JMenuItem jmiApri, jmiSalvaGame, jmiSalvaConfig,
            jmiHelp, max_sol, celle3, celle4, celle5, celle6;
    private JPanel pannelloGriglia, pannelloPulsanti;
    private JButton plsCheck, plsPrev, plsNext, plsStart;
    private Salvataggio salvataggio;
    private int indiceSoluzioneAttuale;
    private int checkPressCount;



    public Finestra(int n) {
        setTitle("KenkenGUI ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AscoltatoreEventi actListener = new AscoltatoreEventi();
        //TODO chiedo all'utente il tipo di salvataggio
        salvataggio = new SalvataggioJSon();
        this.n = n;
        grigliaGUI = new GrigliaGUI(n);
        pannelloGriglia = grigliaGUI.getPannelloGriglia();
        costruisciPannelloPulsanti(actListener);
        costruisciMenu(actListener);
        impostaObserver();
        grigliaGUI.setState(ConfigState.getInstance());
        add(pannelloGriglia, BorderLayout.CENTER);
        add(pannelloPulsanti, BorderLayout.EAST);
        setSize(480, 400);
        setLocation(0, 0);
    }

    private void costruisciPannelloPulsanti(AscoltatoreEventi actListener) {
        pannelloPulsanti = new JPanel();
        plsStart = new JButton("START");
        plsStart.setFont(new Font("Franklin Gothic Medium Cond", Font.BOLD, 14));
        plsStart.addActionListener(actListener);
        plsStart.setEnabled(false);

        plsPrev = new JButton("PREVIOUS");
        plsPrev.setFont(new Font("Franklin Gothic Medium Cond", Font.BOLD, 14));
        plsPrev.addActionListener(actListener);
        plsPrev.setEnabled(false);

        plsNext = new JButton("NEXT");
        plsNext.setFont(new Font("Franklin Gothic Medium Cond", Font.BOLD, 14));
        plsNext.addActionListener(actListener);
        plsNext.setEnabled(false);

        plsCheck = new JButton("CHECK OFF");
        plsCheck.setFont(new Font("Franklin Gothic Medium Cond", Font.BOLD, 14));
        plsCheck.addActionListener(actListener);
        plsCheck.setEnabled(true);

        pannelloPulsanti.setLayout(new GridLayout(4, 1, 10, 10));
        pannelloPulsanti.setSize(100, 400);
        pannelloPulsanti.add(plsStart);
        pannelloPulsanti.add(plsPrev);
        pannelloPulsanti.add(plsNext);
        pannelloPulsanti.add(plsCheck);

    }

    private void costruisciMenu(ActionListener ascoltatore) {

        JMenu fileMenu = new JMenu("File");

        JMenuBar jmbBarra = new JMenuBar();
        this.setJMenuBar(jmbBarra);
        jmiApri = new JMenuItem("Open");
        fileMenu.add(jmiApri);
        jmiApri.addActionListener(ascoltatore);

        jmiSalvaGame = new JMenuItem("Save game");
        fileMenu.add(jmiSalvaGame);
        jmiSalvaGame.addActionListener(ascoltatore);

        jmiSalvaConfig = new JMenuItem("Save grid");
        fileMenu.add(jmiSalvaConfig);
        jmiSalvaConfig.addActionListener(ascoltatore);

        jmbBarra.add(fileMenu);

        JMenu menuSize = new JMenu("Size");
        celle3 = new JMenuItem("3x3");
        menuSize.add(celle3);
        celle3.addActionListener(ascoltatore);

        celle4 = new JMenuItem("4x4");
        menuSize.add(celle4);
        celle4.addActionListener(ascoltatore);

        celle5 = new JMenuItem("5x5");
        menuSize.add(celle5);
        celle5.addActionListener(ascoltatore);

        celle6 = new JMenuItem("6x6");
        menuSize.add(celle6);
        celle6.addActionListener(ascoltatore);

        jmbBarra.add(menuSize);

        JMenu menuSettings = new JMenu("Settings");
        max_sol = new JMenuItem("max solutions");
        menuSettings.add(max_sol);
        max_sol.addActionListener(ascoltatore);
        jmbBarra.add(menuSettings);

        JMenu menuAbout = new JMenu("About");
        jmiHelp = new JMenuItem("Help");
        menuAbout.add(jmiHelp);
        jmiHelp.addActionListener(ascoltatore);

        jmbBarra.add(menuAbout);

    }


    public void resizeGriglia(int size) {
        remove(pannelloGriglia);
        grigliaGUI = new GrigliaGUI(size);
        impostaObserver();
        pannelloGriglia = grigliaGUI.getPannelloGriglia();
        this.n = size;
        add(pannelloGriglia, BorderLayout.CENTER);
        pannelloGriglia.updateUI();
    }

    public void impostaObserver() {
        grigliaGUI.attach(new StartObserver(plsStart, grigliaGUI));
        grigliaGUI.attach(new CheckObserver(plsCheck, grigliaGUI));
        grigliaGUI.attach(new NextPrevObserver(plsNext, grigliaGUI));
        grigliaGUI.attach(new NextPrevObserver(plsPrev, grigliaGUI));
    }


    class AscoltatoreEventi implements ActionListener {

        public void actionPerformed(ActionEvent a) {

            if (a.getSource() == celle3) {
                resizeGriglia(3);
                celle3.setEnabled(false);
                celle4.setEnabled(true);
                celle5.setEnabled(true);
                celle6.setEnabled(true);
                grigliaGUI.setState(ConfigState.getInstance());
            }
            if (a.getSource() == celle4) {
                resizeGriglia(4);
                celle3.setEnabled(true);
                celle4.setEnabled(false);
                celle5.setEnabled(true);
                celle6.setEnabled(true);
                grigliaGUI.setState(ConfigState.getInstance());
            }
            if (a.getSource() == celle5) {
                resizeGriglia(5);
                celle3.setEnabled(true);
                celle4.setEnabled(true);
                celle5.setEnabled(false);
                celle6.setEnabled(true);
                grigliaGUI.setState(ConfigState.getInstance());
            }
            if (a.getSource() == celle6) {
                resizeGriglia(6);
                celle3.setEnabled(true);
                celle4.setEnabled(true);
                celle5.setEnabled(true);
                celle6.setEnabled(false);
                grigliaGUI.setState(ConfigState.getInstance());
            }
            if (a.getSource() == plsStart) {
                int choice = 0;
                indiceSoluzioneAttuale = 0;
                checkPressCount = 0;
                if (!grigliaGUI.haSoluzione()) {
                    JOptionPane.showMessageDialog(null,
                            "La configurazione selezionata non presenta soluzioni!");
                }
                else{
                        choice = JOptionPane.showConfirmDialog(null,
                                "La configurazione scelta ammette soluzione\n" +
                                        "             VUOI SALVARLA?                 ");

                    if (choice == JOptionPane.YES_OPTION) {
                        salvataggio = new SalvataggioJSon();
                        salvataggio.salvaConfig(grigliaGUI);
                    }
                }
                plsStart.setEnabled(false);
                plsCheck.setText("CHECK OFF");
                grigliaGUI.setControlloAttivo(false);
                grigliaGUI.abilitaTextField(true);
                grigliaGUI.abilitaPopup();
            }

            if(a.getSource() == max_sol){
                if(grigliaGUI.getState() instanceof ShowSolutionsState)
                    JOptionPane.showMessageDialog(pannelloGriglia,
                            "Tornare in fase di gioco o configurazione per \n" +
                                    "poter cambiare il numero di soluzioni!!!");
                else {
                    int nrSol = 0;
                    for (; ; ) {

                        String input = JOptionPane.showInputDialog
                                (" Modifica il numero di soluzioni da visualizzare ");
                        try {
                            nrSol = Integer.parseInt(input);
                            break;

                        } catch (RuntimeException e) {
                            JOptionPane.showMessageDialog(pannelloGriglia, "Inserire un intero!");
                        }
                    }
                    KenkenGrid kenken = new KenkenGrid(grigliaGUI.getKenken().getGroups(), grigliaGUI.getKenken().getDim(), nrSol);
                    grigliaGUI.setKenkenGrid(kenken);
                    grigliaGUI.setMaxNumSol(nrSol);
                    grigliaGUI.avviaSoluzione();

                }
            }

            if (a.getSource() == jmiHelp)
                JOptionPane.showMessageDialog(null,
                        "-Puoi personalizzare la difficoltà dalla barra SIZE. \n" +
                                "-Usa il pulsante CHECK per verificare che tuttu i numeri \n" +
                                " inseriti nei blocchi rispettino i vincoli. \n" +
                                "-I numeri all'interno di uno stesso blocco possono ripetersi \n" +
                                " a patto che non si ripetano sulla riga o sulla colonna! \n" +
                                "-Usa i pulsanti NEXT e PREVIOUS per navigare tra le soluzioni.");

            if (a.getSource() == jmiApri) {
                GrigliaGUI tmp = salvataggio.apri();
                if(tmp !=null){
                    if (pannelloGriglia != null) {
                        grigliaGUI.removeCelle();
                        remove(pannelloGriglia);
                    }
                    grigliaGUI=tmp;
                    impostaObserver();
                    pannelloGriglia = grigliaGUI.getPannelloGriglia();
                    add(pannelloGriglia, BorderLayout.CENTER);
                    grigliaGUI.ripristinaGioco(grigliaGUI.getMatriceScelte());
                    grigliaGUI.setState(PlayState.getInstance());
                    if(!Utils.isEmpty((grigliaGUI.getMatriceScelte())))
                        grigliaGUI.abilitaPopup();
                    pannelloGriglia.updateUI();


                }
            }

            if(a.getSource() == jmiSalvaGame){
                salvataggio.salvaGame(grigliaGUI);
            }

            if(a.getSource() == jmiSalvaConfig){
                salvataggio.salvaConfig(grigliaGUI);
            }

            if (a.getSource() == plsNext) {
                if (indiceSoluzioneAttuale == grigliaGUI.getNumSol() - 1)
                    JOptionPane.showMessageDialog(null, "Sei arrivato all'ultima soluzione");
                else {
                    grigliaGUI.mostraSoluzione(++indiceSoluzioneAttuale);

                }
            }

            if (a.getSource() == plsPrev) {

                if (indiceSoluzioneAttuale == 0) {
                    grigliaGUI.mostraSoluzione(indiceSoluzioneAttuale);
                    JOptionPane.showMessageDialog(null, "Sei arrivato alla prima soluzione");
                } else {
                    grigliaGUI.mostraSoluzione(--indiceSoluzioneAttuale);
                }
            }

            if (a.getSource() == plsCheck) {
                checkPressCount++;
                if (checkPressCount % 2 == 1) {
                    plsCheck.setText("CHECK ON!");
                    grigliaGUI.setControlloAttivo(true);
                    grigliaGUI.verificaSoluzione();
                } else {
                    plsCheck.setText("CHECK OFF");
                    grigliaGUI.setControlloAttivo(false);
                    grigliaGUI.ripristinaSfondo();
                }
            }
        }
    }
}

public class KenkenGUI {
    private static final int DEFAULT = 4;
    public static void main(String[] args) {
        Finestra finestra = new Finestra(DEFAULT);
        finestra.setVisible(true);
        finestra.setResizable(false);
    }
}
