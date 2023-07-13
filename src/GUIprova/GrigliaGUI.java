package GUIprova;

import decoratorCelle.Cella;
import risolutore.Coordinate;
import risolutore.Gruppo;
import risolutore.KenkenGrid;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class GrigliaGUI {

    private JPanel pannelloGriglia;
    private int n;
    private KenkenGrid kenken;
    private Cella[][] grigliaTxt;
    private boolean[][] cellaImpostata;
    private JMenuItem inserisci, redo, undo, cancel;
    private JPopupMenu popup;
    private Gruppo gruppoTmp;
    private boolean gruppoInserito;
    private final int BOLD=3;
    private final Color colorBordo= new Color(0,0,0);



    public GrigliaGUI(int n) {

        AscoltatoreEventi actListener = new AscoltatoreEventi();

        this.n=n;
        kenken= new KenkenGrid(n);
        grigliaTxt =new Cella[n][n];
        resetConfigurazione();
        pannelloGriglia= new JPanel();
        pannelloGriglia.setLayout(new GridLayout(n, n));
        pannelloGriglia.setSize(450, 450);
        gruppoInserito=false;

        MatteBorder border= new MatteBorder(1,1,1,1,colorBordo);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grigliaTxt[i][j] = new Cella();
                grigliaTxt[i][j].setSize(50, 50);

                grigliaTxt[i][j].setBorder(border);
                grigliaTxt[i][j].getText().setEnabled(false);
                grigliaTxt[i][j].getText().addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("dio banana");
                        boolean primoElemento=false;

                        if(e.getButton() == MouseEvent.BUTTON3)
                                popup.show(e.getComponent(), e.getX(), e.getY());

                        for(int i=0; i<n;i++) {
                            for(int j=0; j<n; j++) {
                                if(gruppoInserito)
                                    inserisci.setEnabled(true);
                                else
                                    inserisci.setEnabled(false);

                                if(e.getSource()  ==grigliaTxt[i][j].getText()) {
                                    if (e.getButton() == MouseEvent.BUTTON1) {
                                        if(! gruppoInserito) {
                                            gruppoTmp = new Gruppo();
                                            primoElemento=true;
                                        }
                                        if(!cellaImpostata[i][j]) {
                                            if (adiacenti(new Coordinate(i, j), gruppoTmp.getListaCelle()) || primoElemento) {
                                                gruppoInserito = true;
                                                grigliaTxt[i][j].getText().setBackground(new Color(210, 210, 210));
                                                gruppoTmp.addCella(i, j);
                                                cellaImpostata[i][j] = true;
                                                System.out.println("impostata cella:<" + i + ":" + j+">");
                                                primoElemento = false;
                                            }
                                            else primoElemento=false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                });

                impostaFont(i,j);

                //grigliaTxt[i][j].setHorizontalAlignment(JTextField.CENTER);
                pannelloGriglia.add(grigliaTxt[i][j]);
            }


        }
        costruisciMenu(actListener);
    }

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

    JPanel getPannelloGriglia() {
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
            grigliaTxt[i][j].getText().setFont(new Font("Courier New", Font.BOLD, 20));
        else if(n==3 || n==4)
            grigliaTxt[i][j].getText().setFont(new Font("Courier New", Font.BOLD, 35));

    }

    private boolean isConfigurata() {

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
        MatteBorder border= new MatteBorder(borderUp,borderSx,borderDown,borderDx,colorBordo);
        grigliaTxt[cur.getRiga()][cur.getColonna()].setBorder(border);

    }

    class AscoltatoreEventi implements ActionListener {

        public void actionPerformed(ActionEvent a) {

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
                if(gruppoTmp.getListaCelle().size()>1) {
                    for(;;) {
                        String operazione = JOptionPane.showInputDialog("Fornire l'operazione: <+><-><%><x>");
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
                    grigliaTxt[i][j].getText().setBackground(new Color(255,255,255));
                    inserisciBordi(c);
                }
                kenken.addGroup(gruppoTmp);
                System.out.println(gruppoTmp);
            }
        }
    }
}
