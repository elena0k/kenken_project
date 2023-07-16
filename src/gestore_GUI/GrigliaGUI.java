package gestore_GUI;

import componenti.Cella;
import componenti.Coordinate;
import componenti.Gruppo;
import observer.Subject;
import risolutore.GroupsHistory;
import risolutore.KenkenGrid;
import state.State;

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
import java.util.LinkedList;
import java.util.List;

import static risolutore.KenkenGrid.ordina;

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
    //private int indiceSoluzioneCur=0;
    private int maxNumSol;
    private State state;
    private GroupsHistory careTaker;
    private AscoltatoreEventi actListener;
    private boolean hoModificheNonSalvate = false;
    private int[][] matriceScelte;
    private MatteBorder border = new MatteBorder(1, 1, 1, 1, Color.black);


    public GrigliaGUI(int n) {

        actListener = new AscoltatoreEventi();
        this.n = n;
        kenken = new KenkenGrid(n);
        grigliaCelle = new Cella[n][n];
        matriceScelte= new int[n][n];
        resetConfigurazione();
        pannelloGriglia = new JPanel();
        pannelloGriglia.setLayout(new GridLayout(n, n));
        pannelloGriglia.setSize(450, 450);
        gruppoInserito = false;
        careTaker = new GroupsHistory();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grigliaCelle[i][j] = new Cella();
                grigliaCelle[i][j].setN(n);
                grigliaCelle[i][j].setFont(n);
                grigliaCelle[i][j].mySetBorder(border);
                gestisciModificheCella(grigliaCelle[i][j],i,j);
                pannelloGriglia.add(grigliaCelle[i][j]);
            }
        }
        //setState(ConfigState.getInstance());
        //state.intercettaClick(this);

    }

    //TODO proteggere il metodo o passare copia
    public KenkenGrid getKenken() {return kenken;}

    public int getNumSol(){return kenken.getNrSol();}

    //public int getIndiceSoluzioneCur(){return indiceSoluzioneCur;}

    public void setMaxSol(int nrSol){this.maxNumSol=nrSol;}

    public void setControlloAttivo(boolean attivo){this.controlloAttivo=attivo;}

    public void setState(State state) {
        this.state = state;
        notifyObservers();
        state.intercettaClick(this);
    }

    public State getState() {
        return this.state;
    }

    void mostraSoluzione(int index){
        int[][] soluzioneCurr= listaSoluzioni.get(index);
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++){
                grigliaCelle[i][j].mySetText(String.valueOf(soluzioneCurr[i][j]));
                grigliaCelle[i][j].repaint();
            }
        }
        pannelloGriglia.repaint();
    }

    void gestisciModificheCella(Cella cella, int i, int j) {
        cella.setDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                aggiornaMatriceScelte(cella,i,j);
                if(controlloAttivo)
                    verificaSoluzione();
            }
            public void removeUpdate(DocumentEvent e) {
                aggiornaMatriceScelte(cella,i,j);
                ripristinaSfondo();
                if(controlloAttivo)
                    verificaSoluzione();

            }
            public void insertUpdate(DocumentEvent e) {
                aggiornaMatriceScelte(cella,i,j);
                printMatriceScelte();
                if(controlloAttivo)
                    verificaSoluzione();
            }
        });
    }

    void ripristinaSfondo(){
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++)
                grigliaCelle[i][j].mySetBackground(Color.WHITE);
    }

    private void aggiornaMatriceScelte(Cella cella, int i, int j) {
        int val;
        hoModificheNonSalvate = true;
        if(cella.getText().equals(""))
            val=0;
        else val=Integer.parseInt(cella.getText());
        matriceScelte[i][j]=val;
    }


    //TODO debuggare evidenziazione vincoli config3x3medium
    void verificaSoluzione() {
        rispettaVincoli();
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++){
                if(matriceScelte[i][j]!=0) {
                    if(! verificaColonna(matriceScelte[i][j], j, i) ||
                            !verificaRiga(matriceScelte[i][j],i,j)  )
                        grigliaCelle[i][j].mySetBackground(Color.RED);
                }
            }
        }
    }

    private boolean rispettaVincoli(){
        boolean ret=true;
        for(Gruppo g: kenken.getGroups()){
            for(Coordinate c: g.getListaCelle())
                if(matriceScelte[c.getRiga()][c.getColonna()]==0)
                    return true; //gruppo incompleto

            if(!verificaGruppo(g)) {
                segnalaGruppo(g);
                ret = false;
            }
        }
        return ret;
    }

    private void segnalaGruppo(Gruppo g){
        for(Coordinate c: g.getListaCelle())
            grigliaCelle[c.getRiga()][c.getColonna()].mySetBackground(Color.RED);
    }

    private boolean verificaGruppo(Gruppo gruppo){
        LinkedList<Coordinate> celle = gruppo.getListaCelle();
        int risultato=0;
        for (Coordinate c : celle) {
            if (gruppo.getOperazione().equals("+"))
                risultato += matriceScelte[c.getRiga()][c.getColonna()];
            if (gruppo.getOperazione().equals("-") || gruppo.getOperazione().equals("%"))
                risultato = ordina(matriceScelte[c.getRiga()][c.getColonna()], risultato, gruppo.getOperazione());
            if (gruppo.getOperazione().equals("x"))
                risultato *= matriceScelte[c.getRiga()][c.getColonna()];
        }
        return risultato==gruppo.getVincolo();
    }

    private boolean verificaColonna(int val, int j, int riga){
        for (int i = 0; i < this.n; i++)
            if (i != riga && matriceScelte[i][j] == val)
                return false;
        return true;
    }

    private boolean verificaRiga(int val, int i, int colonna){
        for (int j = 0; j < this.n; j++)
            if (j != colonna && matriceScelte[i][j] == val)
                return false;
        return true;
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
                                            if (adiacenti(new Coordinate(i, j), gruppoTmp.getListaCelle()) || primoElemento) {
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


    public int getN() {
        return this.n;
    }

    private boolean adiacenti(Coordinate cur, LinkedList<Coordinate> listaCelle) {

        for (Coordinate c : listaCelle) {
            if (adiacenteDx(c, cur) || adiacenteSx(c, cur) || adiacenteUp(c, cur) || adiacenteDown(c, cur))
                return true;
        }
        return false;
    }

    private boolean adiacenteDown(Coordinate c, Coordinate cur) {
        return c.getColonna() == cur.getColonna() && c.getRiga() + 1 == cur.getRiga();
    }

    private boolean adiacenteUp(Coordinate c, Coordinate cur) {
        return c.getColonna() == cur.getColonna() && c.getRiga() - 1 == cur.getRiga();
    }

    private boolean adiacenteSx(Coordinate c, Coordinate cur) {
        return c.getRiga() == cur.getRiga() && c.getColonna() - 1 == cur.getColonna();
    }

    private boolean adiacenteDx(Coordinate c, Coordinate cur) {
        return c.getRiga() == cur.getRiga() && c.getColonna() + 1 == cur.getColonna();
    }

    private Coordinate[] listaAdiacenti(Coordinate c) {

        Coordinate[] ret = new Coordinate[4];
        Coordinate top = new Coordinate(c.getRiga() - 1, c.getColonna());
        if (top.getRiga() >= 0)
            ret[0] = top;
        Coordinate down = new Coordinate(c.getRiga() + 1, c.getColonna());
        if (down.getRiga() <= n)
            ret[2] = down;
        Coordinate left = new Coordinate(c.getRiga(), c.getColonna() - 1);
        if (left.getColonna() >= 0)
            ret[1] = left;
        Coordinate right = new Coordinate(c.getRiga(), c.getColonna() + 1);
        if (right.getColonna() <= n)
            ret[3] = right;
        return ret;

    }

    public void resetConfigurazione() {
        cellaImpostata = new boolean[n][n];
    }

    JPanel getPannelloGriglia() {
        return this.pannelloGriglia;
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

        resetConfig = new JMenuItem("reset config");
        resetConfig.addActionListener(actListener);
        resetConfig.setEnabled(false);
        popup.add(resetConfig);

        pannelloGriglia.setComponentPopupMenu(popup);
    }

    void costruisciMenuShow() {

        popup.remove(showSol);
        popup.remove(clear);

        resetGame = new JMenuItem("reset game");
        resetGame.addActionListener(actListener);
        popup.add(resetGame);

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
        Coordinate[] adiacenti = listaAdiacenti(cur);
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

    private Coordinate eleggiIndice(Gruppo gruppo) {
        List<Coordinate> listaCelle = gruppo.getListaCelle();
        int minR = Integer.MAX_VALUE;
        int minC = Integer.MAX_VALUE;
        for (Coordinate c : listaCelle) {
            if (c.getRiga() < minR && c.getColonna() < minC) {
                minR = c.getRiga();
                minC = c.getColonna();
            } else if (c.getRiga() == minR && c.getColonna() < minC)
                minC = c.getColonna();

            else if (c.getColonna() == minC && c.getRiga() < minR)
                minR = c.getRiga();

        }
        return new Coordinate(minR, minC);
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

    void abilitaTextField(boolean bool){
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                grigliaCelle[i][j].setEnabled(bool);
    }

    void abilitaPopup(){
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

    void avviaSoluzione()
    {
        kenken.risolvi();
        listaSoluzioni=kenken.getListaSoluzioni();
    }

    private void pulisciGriglia() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                grigliaCelle[i][j].cleanText();
                grigliaCelle[i][j].repaint();
            }
        pannelloGriglia.repaint();
    }


    class AscoltatoreEventi implements ActionListener {

        public void actionPerformed(ActionEvent a) {

            if (a.getSource() == redo) {
                careTaker.redo(kenken);
                if (!careTaker.canRedo())
                    redo.setEnabled(false);
                if (careTaker.canUndo())
                    undo.setEnabled(true);
                kenken.printGroups();
                redraw();
            }

            if (a.getSource() == undo) {
                careTaker.undo(kenken);
                if (!careTaker.canUndo())
                    undo.setEnabled(false);
                else
                    redo.setEnabled(true);
                kenken.printGroups();
                redraw();
            }

            if (a.getSource() == resetConfig) {
                resetConfigurazione();
                setState(ConfigState.getInstance());
                kenken = new KenkenGrid(n);
                redraw();
            }

            if (a.getSource() == resetGame) {
                setState(PlayState.getInstance());
                redraw();
            }

            if (a.getSource() == showSol) {
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
                    state.intercettaClick(GrigliaGUI.this);
                    System.out.println(kenken.getNrSol());
                }
                undo.setEnabled(true);
                drawVincolo(gruppoTmp);

            }
        }
    }


}
