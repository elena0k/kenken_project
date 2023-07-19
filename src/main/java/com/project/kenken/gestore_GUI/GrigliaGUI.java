package com.project.kenken.gestore_GUI;

import com.project.kenken.componenti.Cella;
import com.project.kenken.componenti.Coordinate;
import com.project.kenken.componenti.Gruppo;
import com.project.kenken.eccezioni.VincoloNegativoException;
import com.project.kenken.eccezioni.VincoloNonValidoException;
import com.project.kenken.observer.Subject;
import com.project.kenken.risolutore.GroupsHistory;
import com.project.kenken.risolutore.KenkenGrid;
import com.project.kenken.state.State;
import com.project.kenken.utils.Utils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.project.kenken.utils.Utils.*;


public class GrigliaGUI extends Subject {

    private JPanel pannelloGriglia;
    private int n;
    private KenkenGrid kenken;
    private Cella[][] grigliaCelle;
    private boolean[][] cellaImpostata;
    private JMenuItem inserisci, redo, undo, clear, showSol, resetConfig, resetGame;
    private JPopupMenu popup;
    private Gruppo gruppoTmp;
    private boolean gruppoInserito, controlloAttivo;
    private ArrayList<int[][]> listaSoluzioni;
    private static int maxNumSol=0;
    private State state;
    private GroupsHistory careTaker;
    private AscoltatoreEventi actListener;
    private int[][] matriceScelte,matriceTmp;
    private MatteBorder border = new MatteBorder(1, 1, 1, 1, Color.black);


    public GrigliaGUI(int n) {

        actListener = new AscoltatoreEventi();
        this.n = n;
        if(maxNumSol==0)
            maxNumSol = richiestaNumSoluzioni();
        kenken = new KenkenGrid(n, maxNumSol);
        grigliaCelle = new Cella[n][n];
        matriceScelte = new int[n][n];
        resetConfigurazione();
        pannelloGriglia = new JPanel();
        pannelloGriglia.setLayout(new GridLayout(n, n));
        pannelloGriglia.setSize(450, 450);
        gruppoInserito = false;
        careTaker = new GroupsHistory(kenken);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grigliaCelle[i][j] = new Cella();
                grigliaCelle[i][j].setN(n);
                grigliaCelle[i][j].setFont(n);
                grigliaCelle[i][j].mySetBorder(border);
                gestisciModificheCella(grigliaCelle[i][j], i, j);
                pannelloGriglia.add(grigliaCelle[i][j]);
            }
        }
        setState(ConfigState.getInstance());
        //TODO test
        //pannelloGriglia.remove(grigliaCelle[1][1]);
    }

    public GrigliaGUI(KenkenGrid kenken, int[][] matriceScelte,int maxSol) {

        actListener = new AscoltatoreEventi();
        this.n = kenken.getDim();
        maxNumSol=maxSol;
        this.kenken = new KenkenGrid(kenken.getGroups(),kenken.getDim(),maxSol );
        grigliaCelle = new Cella[n][n];
        this.matriceScelte = new int[n][n];
        for(int i=0; i<n;i++)
            this.matriceScelte[i]= Arrays.copyOf(matriceScelte[i],n);
        terminaConfigurazione();
        pannelloGriglia = new JPanel();
        pannelloGriglia.setLayout(new GridLayout(n, n));
        pannelloGriglia.setSize(450, 450);
        careTaker = new GroupsHistory(kenken);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grigliaCelle[i][j] = new Cella();
                grigliaCelle[i][j].setN(n);
                grigliaCelle[i][j].setFont(n);
                grigliaCelle[i][j].mySetBorder(border);
                gestisciModificheCella(grigliaCelle[i][j], i, j);
                pannelloGriglia.add(grigliaCelle[i][j]);
            }
        }
        redraw();
        setState(ConfigState.getInstance());
    }

    private int richiestaNumSoluzioni() {
        int ret = 0;
        for (; ; ) {
            String input = JOptionPane.showInputDialog("BENVENUTO!\n" +
                    "Questo è il gioco del kenken!\n" +
                    "Per di iniziare a giocare è necessario configurare la griglia,\n" +
                    "puoi scegliere se farlo manualmente o aprire una configurazione \n" +
                    "salvata in precedenza dal menù File \n \n" +
                    "Indicare il NUMERO MASSIMO DI SOLUZIONI che si desidera poter\n" +
                    "visualizzare (si noti che tale numero potrà essere cambiato anche \n" +
                    "durante la fase di configurazione e di gioco) \n" +
                    "     ");
            try {
                ret = Integer.parseInt(input);
                break;

            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(pannelloGriglia, "Inserire un intero!");
            }
        }
        return ret;
    }

    public KenkenGrid getKenken(){
        return new KenkenGrid(this.kenken);
    }

    public int[][] getMatriceScelte() {
        return Utils.copiaProfondaMatriceInt(matriceScelte);
    }

    public int getNumSol() {
        return kenken.getNrSol();
    }
    public int getN() {
        return this.n;
    }

    public State getState() {
        return this.state;
    }

    JPanel getPannelloGriglia() {
        return this.pannelloGriglia;
    }

    public void setControlloAttivo(boolean attivo) {
        this.controlloAttivo = attivo;
    }

    public void setState(State state) {
        this.state = state;
        notifyObservers();
        state.intercettaClick(this);
    }

    void mostraSoluzione(int index) {
        int[][] soluzioneCurr = listaSoluzioni.get(index);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grigliaCelle[i][j].mySetText(String.valueOf(soluzioneCurr[i][j]));
                grigliaCelle[i][j].repaint();
            }
        }
        pannelloGriglia.repaint();
    }

    void gestisciModificheCella(Cella cella, int i, int j) {
        cella.setDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                aggiornaMatriceScelte(cella, i, j);
                if (controlloAttivo)
                    verificaSoluzione();
            }

            public void removeUpdate(DocumentEvent e) {
                aggiornaMatriceScelte(cella, i, j);
                ripristinaSfondo();
                if (controlloAttivo)
                    verificaSoluzione();

            }

            public void insertUpdate(DocumentEvent e) {
                aggiornaMatriceScelte(cella, i, j);
                printMatriceScelte();
                if (controlloAttivo)
                    verificaSoluzione();
            }
        });
    }

    void ripristinaSfondo() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                grigliaCelle[i][j].mySetBackground(Color.WHITE);
    }

    private void aggiornaMatriceScelte(Cella cella, int i, int j) {
        int val;
        if (cella.getText().equals(""))
            val = 0;
        else val = Integer.parseInt(cella.getText());
        matriceScelte[i][j] = val;
    }

    List<Coordinate> verificaSoluzione() {
        List<Coordinate> celle_scorrette= new LinkedList<>();
        rispettaVincoli(celle_scorrette);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matriceScelte[i][j] != 0) {
                    if (!verificaColonna(matriceScelte[i][j], j, i,matriceScelte) ||
                            !verificaRiga(matriceScelte[i][j], i, j,matriceScelte)) {
                        grigliaCelle[i][j].mySetBackground(Color.RED);
                        if(!celle_scorrette.contains(new Coordinate(i,j)))
                            celle_scorrette.add(new Coordinate(i,j));
                    }
                }
            }
        }
        return celle_scorrette;
    }

    private void rispettaVincoli(List<Coordinate> celle_scorrette) {
        for (Gruppo g : kenken.getGroups()) {
            boolean gruppoIncompleto = false;
            for (Coordinate c : g.getListaCelle())
                if (matriceScelte[c.getRiga()][c.getColonna()] == 0) {
                    gruppoIncompleto = true; //gruppo incompleto
                    break;
                }
            if (!gruppoIncompleto && !verificaGruppo(g,matriceScelte)) {
                segnalaGruppo(g,celle_scorrette);
            }
        }
    }

    private void segnalaGruppo(Gruppo g,List<Coordinate> celle_scorrette) {
        for (Coordinate c : g.getListaCelle()){
            Coordinate errata=new Coordinate(c.getRiga(),c.getColonna());
            if(!celle_scorrette.contains(errata))
                celle_scorrette.add(errata);
            grigliaCelle[c.getRiga()][c.getColonna()].mySetBackground(Color.RED);
        }
    }

    void removeCelle() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                pannelloGriglia.remove(grigliaCelle[i][j]);
    }

    void impostaGruppi() {

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                grigliaCelle[i][j].setEnabled(false);
                grigliaCelle[i][j].setMouseAdapter((new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {

                        boolean primoElemento = false;
                        if (e.getButton() == MouseEvent.BUTTON3)
                            popup.show(e.getComponent(), e.getX(), e.getY());

                        for (int i = 0; i < n; i++) {
                            for (int j = 0; j < n; j++) {
                                inserisci.setEnabled(gruppoInserito);

                                if (e.getSource() == grigliaCelle[i][j].getTextField()) {
                                    if (e.getButton() == MouseEvent.BUTTON1) {
                                        if (!gruppoInserito) {
                                            gruppoTmp = new Gruppo();
                                            primoElemento = true;
                                        }
                                        if (!cellaImpostata[i][j]) {
                                            if (Utils.adiacenti(new Coordinate(i, j), gruppoTmp.getListaCelle()) || primoElemento) {
                                                gruppoInserito = true;
                                                grigliaCelle[i][j].mySetBackground(new Color(210, 210, 210));
                                                gruppoTmp.addCella(i, j);
                                                cellaImpostata[i][j] = true;
                                                System.out.println("impostata cella:<" + i + ":" + j + ">");
                                                primoElemento = false;
                                            } else primoElemento = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }));
            }
        }
    }

    public void resetConfigurazione() {
        cellaImpostata = new boolean[n][n];
    }

    public void terminaConfigurazione(){
        resetConfigurazione();
        for(int i=0; i<n; i++)
            for(int j=0; j<n;j++)
                cellaImpostata[i][j]=true;
    }

    void costruisciMenuConfig() {
        popup = new JPopupMenu();
        inserisci = new JMenuItem("insert");
        inserisci.addActionListener(actListener);
        popup.add(inserisci);

        undo = new JMenuItem("undo");
        undo.addActionListener(actListener);
        undo.setEnabled(false);
        popup.add(undo);

        redo = new JMenuItem("redo");
        redo.addActionListener(actListener);
        redo.setEnabled(false);
        popup.add(redo);

        pannelloGriglia.setComponentPopupMenu(popup);
    }

    void costruisciMenuPlay() {
        popup = new JPopupMenu();

        clear = new JMenuItem("clear");
        clear.addActionListener(actListener);
        popup.add(clear);

        showSol = new JMenuItem("reveal");
        showSol.addActionListener(actListener);
        showSol.setEnabled(false);
        popup.add(showSol);

        resetConfig = new JMenuItem("set config");
        resetConfig.addActionListener(actListener);
        resetConfig.setEnabled(false);
        popup.add(resetConfig);

        pannelloGriglia.setComponentPopupMenu(popup);
    }

    void costruisciMenuShow() {
        popup = new JPopupMenu();

        resetGame = new JMenuItem("back to game");
        resetGame.addActionListener(actListener);
        popup.add(resetGame);

        resetConfig = new JMenuItem("set config");
        resetConfig.addActionListener(actListener);
        popup.add(resetConfig);

        pannelloGriglia.setComponentPopupMenu(popup);
    }


    private boolean isConfigurata() {

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!cellaImpostata[i][j])
                    return false;
            }
        }
        return true;
    }

    private void inserisciBordi(Coordinate cur, Gruppo gruppo) {

        int borderDown = 1;
        int borderUp = 1;
        int borderSx = 1;
        int borderDx = 1;
        Coordinate[] adiacenti = listaAdiacenti(cur,n);
        int BOLD = 3;
        if (!gruppo.contains(adiacenti[0])) //top
            borderUp = BOLD;
        if (!gruppo.contains(adiacenti[1]))//left
            borderSx = BOLD;
        if (!gruppo.contains(adiacenti[2]))//bottom
            borderDown = BOLD;
        if (!gruppo.contains(adiacenti[3]))//right
            borderDx = BOLD;
        MatteBorder border = new MatteBorder(borderUp, borderSx, borderDown, borderDx, Color.BLACK);
        grigliaCelle[cur.getRiga()][cur.getColonna()].mySetBorder(border);

    }

    public void redraw() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grigliaCelle[i][j].mySetBorder(border);
                grigliaCelle[i][j].mySetBackground(Color.WHITE);
                if (!grigliaCelle[i][j].isCellaSemplice()) {
                    grigliaCelle[i][j].cleanVincolo();
                }
            }
        }
        resetConfigurazione();
        for (Gruppo g : kenken.getGroups()) {
            for (Coordinate c : g.getListaCelle()) {
                inserisciBordi(c, g);
                drawVincolo(g);
                cellaImpostata[c.getRiga()][c.getColonna()] = true;
                grigliaCelle[c.getRiga()][c.getColonna()].updateUI();
            }
        }
        pulisciGriglia();
    }


    private void drawVincolo(Gruppo gruppo) {
        Coordinate coordVincolo = eleggiIndice(gruppo);
        Cella cellaSemplice = grigliaCelle[coordVincolo.getRiga()][coordVincolo.getColonna()];
        cellaSemplice.setVincolo(gruppo.getVincolo(), gruppo.getOperazione());
    }

    public boolean haSoluzione() {
        boolean haSoluzione = kenken.getNrSol() != 0;
        System.out.println(kenken.getNrSol());
        return haSoluzione;
    }

    public void removeOldMenu() {

        if (popup != null)
            pannelloGriglia.remove(popup);
    }

    public void abilitaTextField(boolean bool) {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                grigliaCelle[i][j].setEnabled(bool);
    }

    void abilitaPopup() {
        if(popup==null)
            costruisciMenuPlay();
        if(haSoluzione())
            showSol.setEnabled(true);
        resetConfig.setEnabled(true);
    }

    private void printMatriceScelte() {
        System.out.println("MATRICE SCELTE");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(matriceScelte[i][j] + " ");
            }
            System.out.println("\n");
        }
    }

    void avviaSoluzione() {
        kenken.risolvi();
        listaSoluzioni = kenken.getListaSoluzioni();
    }

    private void pulisciGriglia() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                grigliaCelle[i][j].cleanText();
                grigliaCelle[i][j].repaint();
            }
        pannelloGriglia.repaint();
    }

    public void salvaInLocale(){
        matriceTmp= Utils.copiaProfondaMatriceInt(matriceScelte);
    }

    public void ripristinaGioco(int[][] matRipristino){
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                grigliaCelle[i][j].mySetText(String.valueOf(matRipristino[i][j]));
                grigliaCelle[i][j].repaint();
            }
        }
        if(!isEmpty(matRipristino)) {
            running = true;
        }
        pannelloGriglia.repaint();
    }



    class AscoltatoreEventi implements ActionListener {

        public void actionPerformed(ActionEvent a) {

            if (a.getSource() == redo) {
                careTaker.redo();
                if (!careTaker.canRedo())
                    redo.setEnabled(false);
                if (careTaker.canUndo())
                    undo.setEnabled(true);
                kenken.printGroups();
                redraw();
            }

            if (a.getSource() == undo) {
                careTaker.undo();
                if (!careTaker.canUndo())
                    undo.setEnabled(false);
                if(careTaker.canRedo())
                    redo.setEnabled(true);
                kenken.printGroups();
                redraw();
            }

            if (a.getSource() == resetConfig) {
                resetConfigurazione();
                setState(ConfigState.getInstance());
                kenken = new KenkenGrid(n);
                redraw();
                running=false;
            }

            if (a.getSource() == resetGame) {
                setState(PlayState.getInstance());
                redraw();
                ripristinaGioco(matriceTmp);
                showSol.setEnabled(true);
                resetConfig.setEnabled(true);
                notifyObservers();

            }

            if (a.getSource() == showSol) {
                salvaInLocale();
                setState((ShowSolutionsState.getInstance()));
                mostraSoluzione(0);
            }

            if (a.getSource() == clear) {
                pulisciGriglia();
            }

            if (a.getSource() == inserisci) {
                gruppoInserito = false;
                int vincolo = -1;

                for (; ; ) {
                    String input = JOptionPane.showInputDialog
                            ("          Fornire il valore intero del vincolo \n" +
                                    " ATTNE: per i blocchi formati da una singola cella \n" +
                                    " è necessario inserire un intero compreso tra 1 e " + n + "   \n" +
                                    "  ");
                    try {
                        vincolo = Integer.parseInt(input);
                        if (vincolo < 1)
                            throw new VincoloNegativoException();
                        if (gruppoTmp.getListaCelle().size() == 1 && vincolo > n)
                            throw new VincoloNonValidoException();
                        break;

                    } catch (RuntimeException e) {
                        JOptionPane.showMessageDialog(pannelloGriglia, "Inserire un intero!");
                    } catch (VincoloNegativoException e) {
                        JOptionPane.showMessageDialog(pannelloGriglia,
                                "Inserire un intero positivo!!!");
                    } catch (VincoloNonValidoException e) {
                        JOptionPane.showMessageDialog(pannelloGriglia,
                                "Inserire un intero positivo compreso tra 1 e  " + n + "!!!");
                    }
                }
                gruppoTmp.setVincolo(vincolo);
                String operazione = "";
                if (gruppoTmp.getListaCelle().size() > 1) {
                    for (; ; ) {
                        operazione = JOptionPane.showInputDialog("Fornire l'operazione: <+><-><%><x>");
                        if (operazione.matches("[+\\-%x]")) {
                            gruppoTmp.setOperazione(operazione);
                            break;
                        } else
                            JOptionPane.showMessageDialog(pannelloGriglia,
                                    "Inserire operazione valida!  <+><-><%><x>");
                    }
                }

                for (Coordinate c : gruppoTmp.getListaCelle()) {
                    int j = c.getColonna();
                    int i = c.getRiga();
                    grigliaCelle[i][j].mySetBackground(Color.WHITE);
                    inserisciBordi(c, gruppoTmp);
                }
                careTaker.save(kenken.getMemento());
                redo.setEnabled(false);
                kenken.printGroups();
                kenken.addGroup(gruppoTmp);

                if (isConfigurata()) {
                    setState(PlayState.getInstance());
                    System.out.println(kenken.getNrSol());
                }
                undo.setEnabled(true);
                drawVincolo(gruppoTmp);
            }
        }
    }
}
