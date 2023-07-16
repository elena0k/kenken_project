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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GrigliaGUI extends Subject {

    private JPanel pannelloGriglia;
    private int n;
    private KenkenGrid kenken;
    private Cella[][] grigliaCelle;
    private boolean[][] cellaImpostata;
    private JMenuItem inserisci, redo, undo, clear, showSol, resetConfig, resetGame;
    private JPopupMenu popup;
    private Gruppo gruppoTmp;
    private boolean gruppoInserito;
    private ArrayList<int[][]> listaSoluzioni;
    private int indiceSoluzioneCur=0;
    private int maxNumSol;
    private State state;
    private final int BOLD = 3;
    private GroupsHistory careTaker;
    private AscoltatoreEventi actListener;
    private MatteBorder border = new MatteBorder(1, 1, 1, 1, Color.black);


    public GrigliaGUI(int n) {

        actListener = new AscoltatoreEventi();
        this.n = n;
        kenken = new KenkenGrid(n);
        grigliaCelle = new Cella[n][n];
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
                pannelloGriglia.add(grigliaCelle[i][j]);
            }
        }
        //setState(ConfigState.getInstance());
        //state.intercettaClick(this);

    }

    //TODO proteggere il metodo o passare copia
    public KenkenGrid getKenken() {
        return kenken;
    }

    public int getNumSol(){return kenken.getNrSol();}

    public int getIndiceSoluzioneCur(){return indiceSoluzioneCur;}

    public void setMaxSol(int nrSol){this.maxNumSol=nrSol;}

    public void setState(State state) {
        this.state = state;
        notifyObservers();
        state.intercettaClick(this);
    }

    public State getState() {
        return this.state;
    }

    public void mostraSoluzione(){
        int[][] soluzioneCurr= listaSoluzioni.get(indiceSoluzioneCur);
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++){
                grigliaCelle[i][j].getTextField().setText(String.valueOf(soluzioneCurr[i][j]));
                System.out.println(grigliaCelle[i][j].getTextField().getText()+" ");
                grigliaCelle[i][j].repaint();
            }
        }
        pannelloGriglia.repaint();
        this.indiceSoluzioneCur++;
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
        //clear.setEnabled(false);
        clear.addActionListener(actListener);
        popup.add(clear);

        showSol = new JMenuItem("reveal");
        showSol.addActionListener(actListener);
        popup.add(showSol);

        resetConfig = new JMenuItem("reset config");
        resetConfig.addActionListener(actListener);
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
    }

    public void evidenziaSoluzioniScorrette() {
        if (kenken.getNrSol() == 1) {
            int[][] soluzione = kenken.getListaSoluzioni().get(0);
            for (Gruppo g : kenken.getGroups()) {
                for (Coordinate c : g.getListaCelle()) {
                    String elem = grigliaCelle[c.getRiga()][c.getColonna()].getText();
                    if (elem != null && !elem.equals("")) {
                        System.out.println("dio banana:  " + elem);
                        int valoreInserito = Integer.parseInt(grigliaCelle[c.getRiga()][c.getColonna()].getText());
                        if (valoreInserito != soluzione[c.getRiga()][c.getColonna()])
                            grigliaCelle[c.getRiga()][c.getColonna()].mySetBackground(Color.RED);
                        else
                            grigliaCelle[c.getRiga()][c.getColonna()].mySetBackground(Color.GREEN);
                    }
                }
            }
        }
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

    public void settaPulsantiPlay() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grigliaCelle[i][j].setEnabled(true);
            }
        }
        if (popup != null)
            pannelloGriglia.remove(popup);
    }

    private void printCellaImpostata() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(cellaImpostata[i][j] + " ");
            }
            System.out.println("\n");
        }
    }

    void avviaSoluzione()
    {
        kenken.risolvi();
        listaSoluzioni=kenken.getListaSoluzioni();
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
                printCellaImpostata();
            }

            if (a.getSource() == undo) {
                careTaker.undo(kenken);
                if (!careTaker.canUndo())
                    undo.setEnabled(false);
                else
                    redo.setEnabled(true);
                kenken.printGroups();
                redraw();
                printCellaImpostata();
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
                mostraSoluzione();
            }

            if (a.getSource() == clear) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        grigliaCelle[i][j].cleanText();
                    }
                }
                pannelloGriglia.validate();
                pannelloGriglia.repaint();
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
