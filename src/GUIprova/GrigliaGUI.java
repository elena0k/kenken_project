package GUIprova;

import componenti.Cella;
import componenti.Coordinate;
import componenti.Gruppo;
import risolutore.GroupsHistory;
import risolutore.KenkenGrid;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

public class GrigliaGUI {

    private JPanel pannelloGriglia;
    private int n;
    private KenkenGrid kenken;
    private Cella[][] grigliaCelle;
    private boolean[][] cellaImpostata;
    private JMenuItem inserisci, redo, undo, cancel;
    private JPopupMenu popup;
    private Gruppo gruppoTmp;
    private boolean gruppoInserito;
    private State state=new ConfigState();
    private final int BOLD=3;
    private GroupsHistory careTaker;



    public GrigliaGUI(int n) {

        AscoltatoreEventi actListener = new AscoltatoreEventi();
        this.n=n;
        kenken= new KenkenGrid(n);
        grigliaCelle =new Cella[n][n];
        state=new ConfigState();
        resetConfigurazione();
        pannelloGriglia= new JPanel();
        pannelloGriglia.setLayout(new GridLayout(n, n));
        pannelloGriglia.setSize(450, 450);
        gruppoInserito=false;
        careTaker= new GroupsHistory();
        careTaker.save(kenken.getMemento());

        MatteBorder border= new MatteBorder(1,1,1,1,Color.black);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grigliaCelle[i][j] = new Cella();
                impostaFont(i,j);
                grigliaCelle[i][j].mySetBorder(border);
                pannelloGriglia.add((Component) grigliaCelle[i][j]);
            }
        }
        state.intercettaClick(this);
        costruisciMenu(actListener);
    }



    public void setState(State state)
    {
        this.state=state;
    }

    protected void impostaGruppi()
    {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                grigliaCelle[i][j].setEnabled(false);
                grigliaCelle[i][j].setMouseAdapter((new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("dio banana");
                        boolean primoElemento = false;

                        if (e.getButton() == MouseEvent.BUTTON3)
                            popup.show(e.getComponent(), e.getX(), e.getY());

                        for (int i = 0; i < n; i++) {
                            for (int j = 0; j < n; j++) {
                                if (gruppoInserito)
                                    inserisci.setEnabled(true);
                                else
                                    inserisci.setEnabled(false);

                                if (e.getSource() == grigliaCelle[i][j].getText()) {
                                    if (e.getButton() == MouseEvent.BUTTON1) {
                                        if (!gruppoInserito) {
                                            gruppoTmp = new Gruppo();
                                            primoElemento = true;
                                        }
                                        if (!cellaImpostata[i][j]) {
                                            if (adiacenti(new Coordinate(i, j), gruppoTmp.getListaCelle()) || primoElemento) {
                                                gruppoInserito = true;
                                                grigliaCelle[i][j].getText().setBackground(new Color(210, 210, 210));
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


    public int getN(){return this.n;}

    private boolean adiacenti(Coordinate cur, LinkedList<Coordinate> listaCelle) {

        for(Coordinate c:listaCelle) {
            if(adiacenteDx(c,cur) || adiacenteSx(c,cur) || adiacenteUp(c,cur) || adiacenteDown(c,cur))
                return true;
        }
        return false;
    }

    private boolean adiacenteDown(Coordinate c, Coordinate cur) {
        return c.getColonna()==cur.getColonna() && c.getRiga()+1==cur.getRiga();
    }

    private boolean adiacenteUp(Coordinate c, Coordinate cur) {
        return c.getColonna()==cur.getColonna() && c.getRiga()-1==cur.getRiga();
    }

    private boolean adiacenteSx(Coordinate c, Coordinate cur) {
        return c.getRiga()==cur.getRiga() && c.getColonna()-1==cur.getColonna();
    }

    private boolean adiacenteDx(Coordinate c, Coordinate cur) {
        return c.getRiga()==cur.getRiga() && c.getColonna()+1==cur.getColonna();
    }

    private Coordinate[] listaAdiacenti(Coordinate c) {

        Coordinate[] ret= new Coordinate[4];
        Coordinate top= new Coordinate(c.getRiga()-1, c.getColonna());
        if(top.getRiga()>=0)
            ret[0]=top;
        Coordinate down=new Coordinate(c.getRiga()+1, c.getColonna());
        if(down.getRiga()<=n)
            ret[2]=down;
        Coordinate left= new Coordinate(c.getRiga(), c.getColonna()-1);
        if(left.getColonna()>=0)
            ret[1]=left;
        Coordinate right= new Coordinate(c.getRiga(), c.getColonna()+1);
        if(right.getColonna()<=n)
            ret[3]=right;
        return ret;

    }


    public void resetConfigurazione() {
        cellaImpostata= new boolean[n][n];
    }

    protected JPanel getPannelloGriglia() {
        return this.pannelloGriglia;
    }

    private void costruisciMenu(ActionListener ascoltatore) {
        popup = new JPopupMenu();
        inserisci = new JMenuItem("inserisci");
        inserisci.addActionListener(ascoltatore);
        popup.add(inserisci);
        cancel = new JMenuItem("cancel");
        cancel.addActionListener(ascoltatore);
        popup.add(cancel);
        undo = new JMenuItem("undo");
        undo.addActionListener(ascoltatore);
        popup.add(undo);
        redo = new JMenuItem("redo");
        redo.addActionListener(ascoltatore);
        popup.add(redo);
        pannelloGriglia.setComponentPopupMenu(popup);
    }

    private void impostaFont(int i, int j) {
        if(n==6 || n==5)
            grigliaCelle[i][j].getText().setFont(new Font("Courier New", Font.BOLD, 20));
        else if(n==3 || n==4)
            grigliaCelle[i][j].getText().setFont(new Font("Courier New", Font.BOLD, 35));

    }

    protected boolean isConfigurata() {

        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if (!cellaImpostata[i][j])
                    return false;
            }
        }
        return true;
    }

    private void inserisciBordi(Coordinate cur) {

        int borderDown=1;
        int borderUp=1;
        int borderSx=1;
        int borderDx=1;
        Coordinate[] adiacenti= listaAdiacenti(cur);
        if(!gruppoTmp.contains(adiacenti[0])) //top
            borderUp=BOLD;
        if(!gruppoTmp.contains(adiacenti[1]))//left
            borderSx=BOLD;
        if(!gruppoTmp.contains(adiacenti[2]))//bottom
            borderDown=BOLD;
        if(!gruppoTmp.contains(adiacenti[3]))//right
            borderDx=BOLD;
        MatteBorder border= new MatteBorder(borderUp,borderSx,borderDown,borderDx,Color.BLACK);
        grigliaCelle[cur.getRiga()][cur.getColonna()].mySetBorder(border);

    }

    private Coordinate eleggiIndice(Gruppo gruppo)
    {
        List<Coordinate> listaCelle=gruppo.getListaCelle();
        int minR = Integer.MAX_VALUE;//listaCelle.get(0).getRiga();
        int minC=Integer.MAX_VALUE;//listaCelle.get(0).getColonna();
        for(Coordinate c:listaCelle)
        {
            if(c.getRiga()< minR && c.getColonna()<minC) {
                minR = c.getRiga();
                minC = c.getColonna();
            }
            else if(c.getRiga()==minR && c.getColonna()<minC)
                minC=c.getColonna();

            else if(c.getColonna()==minC && c.getRiga()<minR)
                minR=c.getRiga();

        }
        return new Coordinate(minR,minC);
    }

    class AscoltatoreEventi implements ActionListener {

        public void actionPerformed(ActionEvent a) {

            if(a.getSource()==undo){
                careTaker.undo(kenken);
                kenken.printGroups();
            }

            if(a.getSource()==inserisci) {
                gruppoInserito=false;
                int vincolo;
                for(;;) {
                    String input = JOptionPane.showInputDialog("Fornire il valore intero del vincolo");
                    try {
                        vincolo =Integer.parseInt(input);
                        break;
                    } catch (RuntimeException e) {
                        JOptionPane.showMessageDialog(pannelloGriglia, "Inserire un intero!");
                    }
                }
                gruppoTmp.setVincolo(vincolo);
                String operazione="";
                if(gruppoTmp.getListaCelle().size()>1) {
                    for(;;) {
                        operazione = JOptionPane.showInputDialog("Fornire l'operazione: <+><-><%><x>");
                        if (operazione.matches("[+\\-%x]")) {
                            gruppoTmp.setOperazione(operazione);
                            break;
                        }
                        else
                            JOptionPane.showMessageDialog(pannelloGriglia,
                                    "Inserire operazione valida!  <+><-><%><x>");
                    }
                }
                for(Coordinate c:gruppoTmp.getListaCelle()) {
                    int j=c.getColonna();
                    int i=c.getRiga();
                    grigliaCelle[i][j].getText().setBackground(Color.WHITE);
                    inserisciBordi(c);
                }

                kenken.addGroup(gruppoTmp);
                careTaker.save(kenken.getMemento());
                Coordinate coordVincolo =eleggiIndice(gruppoTmp);
                Cella cellaSemplice=grigliaCelle[coordVincolo.getRiga()][coordVincolo.getColonna()];
                cellaSemplice.setVincolo(vincolo,operazione);
                System.out.println("cellaIndice: "+coordVincolo);
                System.out.println(gruppoTmp);
            }
        }
    }
}
