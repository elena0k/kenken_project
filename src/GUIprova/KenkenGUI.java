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
    private GrigliaGUI grigliaGUI;

    private JMenuItem jmiApri, jmiSalva, jmiSalvaConNome,
            jmiHelp, jmiEsci, celle3,celle4,celle5,celle6;
    private JPanel pannelloGriglia;
    private JPanel pannelloPulsanti;
    private JButton plsCheck;
    private JButton plsPrev;
    private JButton plsNext;
    private JButton plsStart;
    private JPopupMenu popup;
    boolean hoModificheNonSalvate = false;
    private int indiceSoluzioneAttuale;
    private State state=new Configurazione();
    private final int BOLD=3;
    private final int DEFAULT=4;


    class AscoltatoreEventi implements ActionListener
    {
        Color colorBordo= new Color(0,0,0);
        Finestra f;
        public void actionPerformed(ActionEvent a) {

            if(a.getSource()==celle3) {
                resizeGriglia(3);
            }
            if(a.getSource()==celle4) {
                resizeGriglia(4);
            }
            if(a.getSource()==celle5) {
                resizeGriglia(5);
            }
            if(a.getSource()==celle6) {
                resizeGriglia(6);
            }
        }
    }



    public Finestra(int n)
    {
        setTitle("KenkenGUI ");
        AscoltatoreEventi actListener = new AscoltatoreEventi();

        this.n=n;
        grigliaGUI= new GrigliaGUI(n);

        pannelloGriglia= grigliaGUI.getPannelloGriglia();
        pannelloPulsanti=new JPanel();

        costruisciMenu(actListener);
        add(pannelloGriglia, BorderLayout.CENTER);
        add(pannelloPulsanti, BorderLayout.EAST);
        setSize(400, 400);
        setLocation(0, 0);
        state.terminaConfigurazione(this);
    }

    private void costruisciMenu(ActionListener ascoltatore) {

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

    public void resizeGriglia(int size)
    {
        remove(pannelloGriglia);
        grigliaGUI = new GrigliaGUI(size);
        pannelloGriglia=grigliaGUI.getPannelloGriglia();
        this.n=size;
        add(pannelloGriglia, BorderLayout.CENTER);
        pannelloGriglia.updateUI();
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
