package GUIprova;

import risolutore.Coordinate;
import risolutore.Gruppo;
import risolutore.KenkenGrid;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

class Finestra extends JFrame
{

    private int n;
    private KenkenGrid kenken;
    private JTextField[][] grigliaTxt;

    private boolean[][] cellaImpostata;
    private JMenuItem jmiApri, jmiSalva, jmiSalvaConNome,
            jmiHelp, jmiEsci,inserisci, celle3,celle4,celle5,celle6;
    private JPanel pannelloGriglia;
    private JPanel pannelloPulsanti;
    private JButton plsCheck;
    private JButton plsPrev;
    private JButton plsNext;
    private JButton plsStart;
    private JPopupMenu popup;
    boolean hoModificheNonSalvate = false;
    private int indiceSoluzioneAttuale;
    private Gruppo gruppoTmp;
    private boolean gruppoInserito;
    private State state=new Configurazione();
    private final int BOLD=3;
    private final int DEFAULT=4;


    class AscoltatoreEventi implements ActionListener
    {
        Color colorBordo= new Color(0,0,0);
        Finestra f;
        public void actionPerformed(ActionEvent a) {

            if(a.getSource()==inserisci)
            {
                gruppoInserito=false;
                int vincolo;
                for(;;)
                {
                    String input = JOptionPane.showInputDialog("Fornire il valore intero del vincolo");
                    try {
                        vincolo =Integer.parseInt(input);
                        break;
                    } catch (RuntimeException e) {
                        JOptionPane.showMessageDialog(pannelloGriglia, "Inserire un intero!");
                    }
                }
                gruppoTmp.setVincolo(vincolo);
                String operazione= JOptionPane.showInputDialog("Fornire l'operazione: <+><-><%><x>");
                //TODO controllo operazione ammessa con regex
                gruppoTmp.setOperazione(operazione);
                for(Coordinate c:gruppoTmp.getListaCelle())
                {
                    int j=c.getColonna();
                    int i=c.getRiga();
                    grigliaTxt[i][j].setBackground(new Color(255,255,255));
                    inserisciBordi(c);
                }
                kenken.addGroup(gruppoTmp);
                System.out.println(gruppoTmp);

            }
            if(a.getSource()==celle3)
            {
                resizeFinestra(3);

            }
        }

        private void inserisciBordi(Coordinate cur)
        {
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



    }


    class AscoltatoreMouse implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("dio banana");
            boolean primoElemento=false;
            if(! gruppoInserito) {
                gruppoTmp = new Gruppo();
                gruppoInserito = true;
                primoElemento=true;
            }
            for(int i=0; i<n;i++)
            {
                for(int j=0; j<n; j++)
                {
                    if(e.getSource()  ==grigliaTxt[i][j])
                    {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if(!cellaImpostata[i][j]) {
                                if (adiacenti(new Coordinate(i, j), gruppoTmp.getListaCelle()) || primoElemento) {
                                    grigliaTxt[i][j].setBackground(new Color(230, 230, 230));
                                    gruppoTmp.addCella(i, j);
                                    cellaImpostata[i][j] = true;
                                    System.out.println("impostata cella:<" + i + ":" + j);
                                    primoElemento = false;
                                }
                            }
                        }else primoElemento=false;
                    }
                        else if(e.getButton() == MouseEvent.BUTTON3) {
                            popup.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                }
            }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }


    }

    private boolean adiacenti(Coordinate cur, LinkedList<Coordinate> listaCelle) {
        for(Coordinate c:listaCelle)
        {
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

    private Coordinate[] listaAdiacenti(Coordinate c)
    {
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

    public Finestra(int n)
    {
        AscoltatoreEventi actListener = new AscoltatoreEventi();
        AscoltatoreMouse ascoltatoreMouse= new AscoltatoreMouse();

        this.n=n;

        kenken= new KenkenGrid(n);
        grigliaTxt =new JTextField[n][n];
        resetCelle();

        setTitle("kenken");
        pannelloGriglia= new JPanel();
        pannelloPulsanti=new JPanel();
        pannelloGriglia.setLayout(new GridLayout(n, n));
        pannelloGriglia.setSize(450, 450);


        gruppoInserito=false;

        costruisciMenu(actListener);


        Color colorBordo= new Color(0,0,0);
        MatteBorder border= new MatteBorder(1,1,1,1,colorBordo);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            {
                grigliaTxt[i][j] = new JTextField("");
                grigliaTxt[i][j].setSize(50, 50);

                grigliaTxt[i][j].setBorder(border);
                grigliaTxt[i][j].setEnabled(false);
                grigliaTxt[i][j].getDocument().addDocumentListener(new DocumentListener()
                {
                    public void changedUpdate(DocumentEvent e)
                    {
                        hoModificheNonSalvate = true;
                    }

                    public void removeUpdate(DocumentEvent e)
                    {
                        hoModificheNonSalvate = true;
                    }

                    public void insertUpdate(DocumentEvent e)
                    {
                        hoModificheNonSalvate = true;
                    }
                });
                grigliaTxt[i][j].addMouseListener(ascoltatoreMouse);


                if(n==6 || n==5)
                    grigliaTxt[i][j].setFont(new Font("Courier New", Font.BOLD, 20));
                else if(n==3 || n==4)
                    grigliaTxt[i][j].setFont(new Font("Courier New", Font.BOLD, 35));

                grigliaTxt[i][j].setHorizontalAlignment(JTextField.CENTER);
                pannelloGriglia.add(grigliaTxt[i][j]);
            }

        add(pannelloGriglia, BorderLayout.CENTER);
        add(pannelloPulsanti, BorderLayout.EAST);
        setSize(400, 400);
        setLocation(0, 0);
        state.terminaConfigurazione(this);

    }

    private void costruisciMenu(ActionListener ascoltatore) {
        popup=new JPopupMenu();
        inserisci= new JMenuItem("inserisci");
        inserisci.addActionListener(ascoltatore);
        popup.add(inserisci);
        pannelloGriglia.setComponentPopupMenu(popup);

        JMenu fileMenu = new JMenu("File");

        JMenuBar jmbBarra = new JMenuBar();
        this.setJMenuBar(jmbBarra);
        jmiApri = new JMenuItem("Apri");
        fileMenu.add(jmiApri);
        jmiApri.addActionListener(ascoltatore);

        jmiSalva = new JMenuItem("Salva");
        fileMenu.add(jmiSalva);
        jmiSalva.addActionListener(ascoltatore);

        jmiSalvaConNome = new JMenuItem("Salva con nome");
        fileMenu.add(jmiSalvaConNome);
        jmiSalvaConNome.addActionListener(ascoltatore);

        jmiEsci = new JMenuItem("Esci");
        fileMenu.add(jmiEsci);
        jmiEsci.addActionListener(ascoltatore);

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

        JMenu menuAbout = new JMenu("About");
        jmiHelp = new JMenuItem("Help");
        menuAbout.add(jmiHelp);
        jmiHelp.addActionListener(ascoltatore);

        jmbBarra.add(menuAbout);

    }

    public void resizeFinestra(int size)
    {
        setVisible(false);
        new Finestra(size);
        setVisible(true);
        setResizable(false);
    }

    public void resetCelle()
    {
        cellaImpostata= new boolean[n][n];
    }

    private boolean isConfigurata()
    {
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<n; j++)
            {
                if (!cellaImpostata[i][j])
                    return false;
            }
        }
        return true;
    }

    public void setState(State state)
    {
        this.state=state;
    }

}

public class KenkenGUI
{
    private static final int DEFAULT=4;

    public static void main(String[] args)
    {
        Finestra finestra = new Finestra(DEFAULT);
        finestra.setVisible(true);
        finestra.setResizable(false);
    }
}
